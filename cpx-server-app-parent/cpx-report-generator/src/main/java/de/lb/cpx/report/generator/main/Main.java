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
package de.lb.cpx.report.generator.main;

import de.lb.cpx.report.generator.fop.ReportGenerator;
import de.lb.cpx.system.properties.CpxSystemProperties;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

/**
 *
 * @author hasse
 */
public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException {
        final String cpxHome = CpxSystemProperties.getCpxHomeForTesting();

        final String reportDir = cpxHome + "\\reports\\";
        File xmlInputFile = new File(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getParentFile().getParentFile() + "\\classes\\file.xml");
        if (xmlInputFile.exists()) {
            //Copy file to a temporary location, because it gets automatically deleted after PDF is generated
            File tmpInputFile = new File(xmlInputFile.getParentFile().getAbsolutePath() + "\\file_test.xml");
            if (tmpInputFile.exists()) {
                //temporary file already exists, overwrite it!
                try {
                    Files.delete(tmpInputFile.toPath());
                    LOG.log(Level.FINEST, "deleted file: {0}", tmpInputFile.getAbsolutePath());
                } catch (IOException ex) {
                    LOG.log(Level.WARNING, "was not able to delete file: " + tmpInputFile.getAbsolutePath(), ex);
                }
//                if (tmpInputFile.delete()) {
//                    LOG.log(Level.FINEST, "temp file was successfully deleted: " + tmpInputFile.getAbsolutePath());
//                }
            }
            copyFile(xmlInputFile, tmpInputFile);
            xmlInputFile = tmpInputFile;
        }

        LOG.log(Level.INFO, "Using report directory: " + reportDir);
        LOG.log(Level.INFO, "Using input file: " + xmlInputFile.getAbsolutePath());
        final ReportGenerator reportgenerator = new ReportGenerator();
        try {
            final File outputFile = reportgenerator.generatePDFContent(reportDir, xmlInputFile,"DRG");
            LOG.log(Level.INFO, "Output file: " + outputFile.getAbsolutePath());
            Desktop.getDesktop().open(outputFile);
        } catch (IOException | ParserConfigurationException | TransformerException | SAXException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private static void copyFile(File source, File dest) throws IOException {
        try (InputStream is = new FileInputStream(source); OutputStream os = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
    }

}
