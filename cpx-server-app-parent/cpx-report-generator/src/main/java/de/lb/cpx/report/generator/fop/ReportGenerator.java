/* 
 * Copyright (c) 2016 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.report.generator.fop;

import de.lb.cpx.server.config.CpxServerConfigLocal;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopConfParser;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.events.Event;
import org.apache.fop.events.EventFormatter;
import org.apache.fop.events.EventListener;
import org.apache.fop.events.model.EventSeverity;
import org.xml.sax.SAXException;

/**
 * Fop PDF Generator fuer Checkpoint X(treme) Die urspruengliche Implementierung
 * wurde hier auf die aktuellste FOP Version 2.1 angepasst.
 *
 * @since Aug/Sep 2016
 * @author BHa, Pna
 */
@Stateless
public class ReportGenerator {

    private static final Logger LOG = Logger.getLogger(ReportGenerator.class.getName());
    private final CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
//    @EJB(name = "CpxServerConfig")
    @Inject
    private CpxServerConfigLocal cpxServerConfig;

    /**
     * to make a report using two input files, e.g.xml data file and xsl format
     * file
     *
     * @param inputXML file which contains all data about case
     * @param caseType Case Type for XSL Layout DRG/PEPP
     * @throws TransformerException transformer exception
     * @throws ParserConfigurationException parser configuration exception
     * @throws SAXException sax exception
     * @throws IOException io exception
     * @return pdf report file
     */
    public File generatePDFContent(final File inputXML,final String caseType) throws TransformerException, ParserConfigurationException, SAXException, IOException {
        return generatePDFContent(cpxProps.getCpxServerReportsDir(), inputXML, caseType);
    }

    /**
     * to make a report using two input files, e.g. xml data file and xsl format
     * file
     *
     * @param pReportsDirectory reports directory (can be used for testing
     * purposes if there is no CPX_HOME available)
     * @param inputXML file which contains all data about case
     * @param caseType Case Type for XSL Layout DRG/PEPP
     * @return report path
     * @throws javax.xml.transform.TransformerException transformer exception
     * @throws javax.xml.parsers.ParserConfigurationException parser
     * configuration exception
     * @throws org.xml.sax.SAXException sax exception
     * @throws java.io.IOException io exception
     */
    public File generatePDFContent(final String pReportsDirectory, final File inputXML,final String caseType) throws TransformerException, ParserConfigurationException, SAXException, IOException {
        //check conditions first
        if (inputXML == null) {
            throw new IllegalArgumentException("inputXML is null!");
        }
        if (pReportsDirectory == null || pReportsDirectory.trim().isEmpty()) {
            throw new IllegalArgumentException("reports directory is null or empty!");
        }
        final File reportsDirectory = new File(pReportsDirectory);

        if (!reportsDirectory.exists() || reportsDirectory.isFile()) {
            throw new IllegalArgumentException("reports directory does not exist (either missing or is a file instead): " + reportsDirectory.getAbsolutePath());
        }

        final File outputDir = new File(cpxProps.getTmpDir(), "reports");
        final File reportDir = new File(pReportsDirectory);
        if (!outputDir.exists()) {
            if (outputDir.mkdirs()) {
                LOG.log(Level.INFO, "temp directory was created: " + outputDir.getAbsolutePath());
            }
        }
        final File outputFilePDF = new File(outputDir, "Abschlussbericht_" + System.currentTimeMillis() + ".pdf");
        final File pathFopXconf = new File(reportDir, "fop.xconf");
        
        String xslFilePath = "";
        if(caseType.equals("PEPP")){
            xslFilePath = cpxServerConfig.getPdfReportPeppXslFilePath();
            LOG.log(Level.INFO,"PEPP Case XSL will be used for Generation...");
        }else{
            xslFilePath = cpxServerConfig.getPdfReportXslFilePath();
            LOG.log(Level.INFO,"DRG Case XSL will be used for Generation...");
        }
        final File inputXSL;
        if (xslFilePath != null && !xslFilePath.isEmpty()) {
            if (xslFilePath.startsWith("reports\\")) {
                inputXSL = new File(cpxProps.getCpxHome() + cpxProps.getFileSeparator() + xslFilePath);
            } else {
                inputXSL = new File(xslFilePath);
            }
        } else {
            throw new IllegalArgumentException("Konfigurieren Sie das Element \"<report_xsl_file>\" in der Datei cpx_server_config.xml (Bitte geben Sie einen gültigen XSL-Dateipfad ein)");
        }
//        final File inputXSL = new File(cpxServerConfig.getPdfReportXslFilePath()); // based on type of Report
//        final File inputXSL = new File(pReportsDirectory + cpxProps.getFileSeparator()+ "ONLY NAME OF THE XSL FILE"); // based on type of Report

        /*
        String pdfReportType = cpxServerConfig.getPdfReportType();
        File inputXSL = null;
        switch (pdfReportType.toLowerCase().trim()) {
            case "none":
                break;
            case "bg":
                inputXSL = new File(pReportsDirectory + cpxProps.getFileSeparator() + "cpx_case_completion_intern_BG.xsl"); // for internal report 
                break;
            case "huk":
                inputXSL = new File(pReportsDirectory + cpxProps.getFileSeparator() + "cpx_case_completion_HUK.xsl"); // for huk specific report
                break;
            default:
                // by default, any report format??
                break;
        }
         */
        if (!pathFopXconf.exists() || pathFopXconf.isDirectory()) {
            throw new IllegalArgumentException("Diese Datei existiert nicht (entweder fehlt oder ist stattdessen ein Verzeichnis): " + pathFopXconf.getAbsolutePath());
        }

//        if (inputXSL == null) {
//            throw new IllegalArgumentException("Konfigurieren Sie das Element \"<report_xsl_file>\" in der Datei cpx_server_config.xml (Bitte geben Sie einen gültigen XSL-Dateipfad ein)" + "\n");
//        }
        if (!inputXSL.exists() || inputXSL.isDirectory()) {
            throw new IllegalArgumentException("Diese Datei existiert nicht (entweder fehlt oder ist stattdessen ein Verzeichnis): " + inputXSL.getAbsolutePath());
        }

        if (!inputXML.exists() || inputXML.isDirectory()) {
            throw new IllegalArgumentException("Diese Datei existiert nicht (entweder fehlt oder ist stattdessen ein Verzeichnis): " + inputXML.getAbsolutePath());
        }

        final List<String> errorsMsgs = new ArrayList<>();

        LOG.log(Level.INFO, "Will generate PDF now... (XSL Input: " + inputXSL.getAbsolutePath() + ", XML Input: " + inputXML.getAbsolutePath() + ", PDF Output: " + outputFilePDF.getAbsolutePath());

        try (OutputStream m_outputStream_pdf = new FileOutputStream(outputFilePDF)) {
            Transformer transformer = null;
            Result sax = null;
            Fop fop = null;
            try {
                //parsing configuration
                FopConfParser parser = new FopConfParser(pathFopXconf);
                //building the factory with the user options
                FopFactoryBuilder fopFactoryBuilder = parser.getFopFactoryBuilder();
                fopFactoryBuilder.setStrictFOValidation(false)
                        .setStrictUserConfigValidation(false);
                FopFactory fopFact = fopFactoryBuilder.build();
                // a user agent is needed for transformation
                //FOUserAgent foUserAgent = fopFact.newFOUserAgent();
                FOUserAgent foUserAgent = fopFact.newFOUserAgent();
                foUserAgent.getEventBroadcaster().addEventListener(new EventListener() {
                    @Override
                    public void processEvent(Event event) {
                        String msg = EventFormatter.format(event);
                        EventSeverity severity = event.getSeverity();
                        if (severity == EventSeverity.INFO) {
                            LOG.log(Level.FINER, msg);
                        } else if (severity == EventSeverity.WARN) {
                            LOG.log(Level.FINE, "[WARN] {0}", msg);
                        } else if (severity == EventSeverity.ERROR) {
                            errorsMsgs.add("Error: " + msg);
                            //throw new IllegalStateException("Error occured in report generation: " + msg);
                        } else if (severity == EventSeverity.FATAL) {
                            errorsMsgs.add("Fatal: " + msg);
                        } else {
                            assert false;
                        }
                    }
                });
                fop = fopFact.newFop(MimeConstants.MIME_PDF, foUserAgent, m_outputStream_pdf);
                LOG.log(Level.INFO, "FOP Factory Initialization");
            } catch (SAXException | IOException e) {
                LOG.log(Level.SEVERE, MessageFormat.format("Something went wrong, maybe the configuration file is corrupted: {0}", pathFopXconf.getAbsolutePath()), e);
            }
            try (FileInputStream fisXml = new FileInputStream(inputXML); FileInputStream fisXsl = new FileInputStream(inputXSL)) {
                Source xml = new StreamSource(fisXml);
                Source xsl = new StreamSource(fisXsl);
                if (fop != null) {
                    // Setup XSLT
                    TransformerFactory tFactory = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl", null);
                    tFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                    tFactory.setErrorListener(new ErrorListener() {
                        @Override
                        public void warning(TransformerException exception) throws TransformerException {
                            LOG.log(Level.WARNING, "[WARN] " + exception.getMessage());
                            LOG.log(Level.FINEST, null, exception);
                        }

                        @Override
                        public void error(TransformerException exception) throws TransformerException {
                            LOG.log(Level.SEVERE, null, exception);
                            errorsMsgs.add("Error: " + exception);
                        }

                        @Override
                        public void fatalError(TransformerException exception) throws TransformerException {
                            LOG.log(Level.SEVERE, null, exception);
                            errorsMsgs.add("Fatal: " + exception);
                        }
                    });
                    transformer = tFactory.newTransformer(xsl);
                    if (transformer != null) {
                        //            Resulting SAX events (the generated FO) must be piped through to FOP
                        sax = new SAXResult(fop.getDefaultHandler());
                        //            Start XSLT transformation and FOP processing
                        //            That's where the XML is first transformed to XSL-FO and then PDF is created
                        transformer.transform(xml, sax);
                    }
                    if (!errorsMsgs.isEmpty()) {
                        String[] tmp = new String[errorsMsgs.size()];
                        errorsMsgs.toArray(tmp);
                        String errorMsg = String.join("\r\n", tmp);
                        throw new IllegalStateException("Problems occured in report generation:\r\n"
                                + errorMsg + "\r\n"
                                + "with XLS Input: " + inputXSL.getName() + ", XML Input: " + inputXML.getName() + ", PDF Output: " + outputFilePDF.getName());
                    }
                    LOG.log(Level.INFO, "Transformation is successful (XML Input to SAX Result)!");
                }
            }

            LOG.log(Level.INFO, "PDF File is Generated! -> " + outputFilePDF.getAbsolutePath());

            if (!deleteFile(inputXML)) {
                LOG.log(Level.WARNING, "Deletion failed, will delete this file on exit instead: " + inputXML.getAbsolutePath());
                inputXML.deleteOnExit();
            }
            return outputFilePDF;
        }
    }

    public boolean deleteFile(final File file) {
        if (file == null) {
            return true;
        }
        if (!file.exists()) {
            return true;
        }
        if (file.isDirectory()) {
            LOG.log(Level.SEVERE, "This is a directory but not a document file: " + file.getAbsolutePath());
            return true;
        }
        boolean result = false;
        try {
            Files.delete(file.toPath());
            result = true;
            //LOG.log(Level.INFO, "deleted file: {0}", file.getAbsolutePath());
            LOG.log(Level.FINE, "Document is deleted from the file system: " + file.getAbsolutePath());
        } catch (IOException ex) {
            LOG.log(Level.WARNING, "Document was not deleted from the file system: " + file.getAbsolutePath(), ex);
        }
//        final boolean result = file.delete();
//        if (result) {
//            LOG.log(Level.FINE, "Document is deleted from the file system: " + file.getAbsolutePath());
//        } else {
//            LOG.log(Level.WARNING, "Document was not deleted from the file system: " + file.getAbsolutePath());
//        }
        return result;
    }

}
