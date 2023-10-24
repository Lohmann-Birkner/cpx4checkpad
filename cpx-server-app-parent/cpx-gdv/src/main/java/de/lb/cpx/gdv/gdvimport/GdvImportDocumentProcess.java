/*
 * Copyright (c) 2021 Lohmann & Birkner.
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
 *    2021  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.gdv.gdvimport;

import com.google.common.io.Files;
import de.FileUtils;
import de.lb.cpx.gdv.messages.Anhang;
import de.lb.cpx.gdv.messages.Header;
import de.lb.cpx.gdv.messages.Sachverstaendigenbeauftragung;
import de.lb.cpx.gdv.communication.GdvReader;
import de.lb.cpx.gdv.messages.GDV;
import de.lb.cpx.gdv.messages.Kommentar;
import de.lb.cpx.gdv.model.TGdvAttachment;
import de.lb.cpx.gdv.model.TGdvInDocument;
import de.lb.cpx.rule.util.XMLHandler;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;



/**
 * oversee of the defined directory for gdv - xml files,
 * copy new files into target directory
 * remove copied files from gdv - directory after copying
 * send mail when the new file was found
 * @author gerschmann
 */

public class GdvImportDocumentProcess {

    private static final Logger LOG = Logger.getLogger(GdvImportDocumentProcess.class.getName());
    private final StringBuilder protocol = new StringBuilder();
    private final SimpleDateFormat dateFormatDetailTime = new SimpleDateFormat("yyyyMMddHHmmss");
    private final String dateTimeString;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH_mm");
//    private final String dateString;
    private final Pattern PATTERN_4_VU_REF = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}-\\d{2}.\\d{2}.\\d{2}.\\d{5}$");

    public GdvImportDocumentProcess(){  
        dateTimeString = dateFormatDetailTime.format(new Date());
//        dateString = dateFormat.format(new Date());
    }
    
    /**
     * is for test only connection to db will not be used
     * @param gdvDirectory
     * @param targetDirectory
     * @param pArchivDir
     * @param emailFrom
     * @param emailPassword
     * @param emailTo 
     * @return  
     */
    public List<TGdvInDocument> importGdvImportDocuments(String gdvDirectory, String targetDirectory, String pArchivDir, String emailFrom, String emailPassword, String emailTo) 
            throws IllegalArgumentException{
        LOG.log(Level.INFO, "Start GdvImportDocumentProcess");
        List<TGdvInDocument> retDocumentList = new ArrayList<>();
        // 1. check gdv directory
        File gdvDir = checkDirectory(gdvDirectory);
        if(!gdvDir.canRead()){
            throw new IllegalArgumentException("Connot read from " + gdvDirectory); 
        }
        if(pArchivDir == null || pArchivDir.isEmpty()){
           pArchivDir = gdvDir.getAbsolutePath() + File.separator + "archiv";
        }
        File archivDir = new File(pArchivDir);
        if(!archivDir.exists() && !archivDir.mkdir() || !archivDir.canWrite()){
            throw new IllegalArgumentException("cannot archivate processed gdv files in " + archivDir.getAbsolutePath());        
        }
        
        // 2. check cpx directory
        File cpxDir = checkDirectory(targetDirectory);
        if(!cpxDir.canWrite()){
            throw new IllegalArgumentException("Connot write into " + cpxDir); 
        }

        // 3. get list of xml files from gdv directory
        String[] gdvFiles = gdvDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".xml");
            }
        });
        if(gdvFiles.length == 0){
            LOG.log(Level.INFO, "no GDV Files found");
            return null;
        }
        // 4. for each file from gdv directory

        for(String gdvFileName: gdvFiles){
            try {
                // 5. extract attachments and other significant data(schadensmeldung und s.w.)
                // 6. save attachments with generated names in cpx directory
                // 7. move processed file into archiv directory
                TGdvInDocument doc = processFile(gdvDir.getAbsolutePath() + File.separator + gdvFileName, cpxDir, archivDir);
                if(doc != null){
                    retDocumentList.add(doc);
                }
            } catch (IOException | IllegalArgumentException ex) {
                Logger.getLogger(GdvImportDocumentProcess.class.getName()).log(Level.SEVERE, null, ex);
                protocol.append("\nDatei ").append(gdvDir.getAbsolutePath()).append(File.separator).append(gdvFileName).append("wurde nicht bearbeitet");
            } 
        }
//
//        // 8. send mail with generated proptocol
//        SendMail mail = new SendMail();
//        mail.sendMail(emailTo, emailFrom, emailPassword, "GDV Import Protokoll", protocol.toString());
        return retDocumentList;
     }

    private File checkDirectory(String pAbsPath){

        File dir = new File(pAbsPath);
        if(!dir.exists()){
            throw new IllegalArgumentException("No valid path for directory: " + pAbsPath);        
        }
        if(!dir.isDirectory()){
             throw new IllegalArgumentException("Path: " + pAbsPath + " is not a directory"); 
        }
        return dir;
    }

    
    private TGdvInDocument processFile(String gdvFileName, @NotNull File cpxDir, @NotNull File archivDir)  throws IOException, IllegalArgumentException {
        protocol.append("\nDatei: ").append(gdvFileName);
        // 5. extract attachments and other significant data(schadensmeldung und s.w.)
        // 6. save attachments with generated names in cpx directory
          File gdvFile = new File(gdvFileName);
        
        TGdvInDocument inDocument = new TGdvInDocument();

        if(extractData(gdvFile, cpxDir, inDocument)){
        // 7. move processed file into archiv directory
            String gdvNew = archivDir.getAbsolutePath() + File.separator +
                    Files.getNameWithoutExtension(gdvFile.getName()) + "_" + dateTimeString + "." + Files.getFileExtension(gdvFile.getName());
            File gdvNewFile = new File(gdvNew);
            if(!FileUtils.copyFile(gdvFile, gdvNewFile)){
                protocol.append("\nFehler beim Kopieren in Zielverzeichnis ").append(archivDir.getAbsolutePath()).append("ist aufgetretten");
            }else{
                FileUtils.deleteFile(gdvFile);
            }
            inDocument.setFileName(gdvNewFile.getName());
            inDocument.setFilePath(gdvNewFile.getAbsolutePath());
            return inDocument;
        }else{
            Set<TGdvAttachment> attachmentLines = inDocument.getAttchments();
            if(!attachmentLines.isEmpty()){
//TODO: remove generated attachment files because of an error by extracting of some attachments, the listis not complete     
                Iterator<TGdvAttachment> itr = attachmentLines.iterator();
                while(itr.hasNext()){
                    FileUtils.deleteFile(new File(itr.next().getAttachmentPath()));
                }
            }
            return null;
        }
    }


    private boolean extractData(@NotNull File gdvFile, @NotNull File destDir, @NotNull TGdvInDocument pDocument)  {

        if(!gdvFile.canRead()){
            protocol.append("\nDie Datei kann nicht gelesen werden");
            return false;
        }
        String schadenNr = "";
        GDV gdv = GdvReader.getInstance().getGdvFromXml(gdvFile, protocol);
        if(gdv == null){
            return false;
        }
        String startStr = dateFormat.format(new Date());
        String endStr = timeFormat.format(new Date());
        String ref = gdv.getVorsatz().getVUReferenz();
            if(PATTERN_4_VU_REF.matcher(ref).matches()){
                startStr = ref.substring(0, 10);
                endStr = ref.substring(11, 16).replace(".", "-");
            }
            Sachverstaendigenbeauftragung beauftr = GdvReader.getInstance().getExpertCommissioningFromXml(gdv);
        if(beauftr != null){
            StringBuffer info = new StringBuffer(dateTimeString + "\n");
            info.append(gdvFile.getName());
//Extract Schadensnummer
            Header header = beauftr.getHeader();

            if(header == null){
                protocol.append("\nKein gültigen Header in Beauftragung gefunden, Beauftragung wird ignoriert");
                return false;
            }
            schadenNr = header.getSchadenNr();// + "_"+ header.getVersicherungsscheinNr();
            info.append("\nSchadensnummer:").append("\t").append(schadenNr).append("\n");
            protocol.append("\nSchadensnummer:").append("\t").append(schadenNr).append("\n");
// Extrakt Anschprechspartner HUK
            List<Sachverstaendigenbeauftragung.PartnerdatenBlock>  partners = beauftr.getPartnerdatenBlock();
            if(partners.isEmpty()){
                protocol.append("\nFür die Beauftragung mit Schadensnummer ").append(schadenNr).append(" ist Ansprechspartner bei HUK nicht ausgewiesen");
            }

            for(Sachverstaendigenbeauftragung.PartnerdatenBlock partner: partners){
                try{
                    if(partner.getPartnerdaten() != null ){
                        if( partner.getPartnerdaten().getAdresse() != null
                                &&(partner.getPartnerdaten().getAdresse().getName1() != null
                                && partner.getPartnerdaten().getAdresse().getName1().startsWith("HUK-COBURG")
                                 || partner.getPartnerdaten().getIdentifikationPartner() != null && partner.getPartnerdaten().getIdentifikationPartner().getAdresskennzeichen().equals("20")  )){
                                    info.append("\nAnsprechpartner:").append("\t").append(partner.getPartnerdaten().getAdresse().getName3()).append("\n");
                                    if(partner.getPartnerdaten().getKommunikation() != null){
                                        info.append("Telefon:").append("\t").append(partner.getPartnerdaten().getKommunikation().getNummer()).append("\n");
                                    }
                        
                        } else  if(partner.getPartnerdaten().getIdentifikationPartner() != null && partner.getPartnerdaten().getIdentifikationPartner().getAdresskennzeichen().equals("15") ){
                                    info.append("\nAnspruchsteller:").append("\t").append(partner.getPartnerdaten().getAdresse().getName3()).
                                            append(" ").append(partner.getPartnerdaten().getAdresse().getName1()).append(",\t Geburtsdatum:")
                                            .append(partner.getPartnerdaten().getPersonendaten().getGeburtsdatum()).append("\n");
                        } 
                            
                    }
                    if (partner.getKommentar() != null && !partner.getKommentar().isEmpty()){
                            List<Kommentar>comments = partner.getKommentar();
                            StringBuffer comm = new StringBuffer();
                            for(Kommentar comment: comments){
                                if(comment.getReferenzierungPartner() != null && comment.getReferenzierungPartner().getAdresskennzeichen() != null && comment.getReferenzierungPartner().getAdresskennzeichen().equals("20") ){
                                    comm.append(comment.getKommentar1() == null?"":comment.getKommentar1().getValue())
                                            .append(" ").append(comment.getKommentar2() == null?"":comment.getKommentar2().getValue());
                                }
                            }
                            info.append("Kommentar:").append("\t").append(comm.toString()).append("\n");
                    }

                }catch(Exception ex){
                    LOG.log(Level.INFO, "Fehler beim ermitteln des Ansprechspartners HUK oder Antragstellers");
                }
                
            }
// extract comment

//Exctract Anhänge

            List <Anhang> anhaenge = beauftr.getAnhang();
            if(anhaenge.isEmpty()){
                protocol.append("\nFür die Beauftragung mit Schadensnummer ").append(schadenNr).append(" keine Dokumente gefunden");
//                return false;
            }
            // filename: schadensnummer + anhangsatzNr+dateiname(anhang) 
            Set<TGdvAttachment> attachmentLines = new HashSet<>();
            // create directory schadennr
            String schadenDirName = FileUtils.validateFilename(schadenNr);
            schadenDirName = schadenDirName.replaceAll("-", "");
            schadenDirName = schadenDirName.replaceAll("_", "");
            schadenDirName = schadenDirName.replaceAll("/", "");
            File schadenDir = new File(destDir.getAbsolutePath() + File.separator + FileUtils.validateFilename(schadenDirName));
            if(schadenDir.exists() && schadenDir.isDirectory()){
                destDir = schadenDir;
            }else{
                if(schadenDir.mkdir() &&schadenDir.canWrite()){
                    destDir = schadenDir;
                }else{
                   protocol.append("\nFür die Beauftragung mit Schadensnummer ").append(schadenNr).append(" konnte Verzeichnis nicht angelegt werden, die Anhänge werden in ").append(destDir.getAbsolutePath()).append(" geschrieben");
                }
            }
            info.append("Anzahl Anhänge:").append(anhaenge.size()).append("\n");
            protocol.append("Anzahl Dokumente:").append(anhaenge.size()).append("\n");
            List<File> files = new ArrayList<>();
            for(Anhang anhang: anhaenge){

                String fileName =
//                        dateString +  "_" + 
                        startStr + "_" + 
                        FileUtils.validateFilename(anhang.getBeschreibung() + "_" +
                        anhang.getDateiname().toUpperCase()) + "_" + endStr;
                byte[] inhalt = anhang.getInhalt().getValue();
                int len = anhang.getLaenge().getWert().intValue();
                File outputFile = new File(destDir.getAbsolutePath() + File.separator + fileName + "." + anhang.getAnhangsart());
                int i = 1;
                while(outputFile.exists()){
                    outputFile = new File(destDir.getAbsolutePath() + File.separator + fileName + "(" + i + ")" + "." + anhang.getAnhangsart());
                    i++;
                }
                
                try {
                    org.apache.commons.io.FileUtils.writeByteArrayToFile(outputFile, inhalt);
                    protocol.append("\n").append(outputFile.getAbsolutePath()).append("\n");
                    files.add(outputFile);
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, "could not save attachment " + outputFile.getAbsolutePath(), ex);
                    protocol.append("\nDatei ").append(outputFile.getAbsolutePath()).append(" wurde nicht gespeichert");
                    return false;
                }
                if(anhang.getBeschreibung() != null && anhang.getBeschreibung().length() > 0){
//                    info.append(anhang.getDateiname()).append(":\t").append(anhang.getBeschreibung()).append("\n");
                }
// add to pAttachmentLines: schadenNr, outputFile.getAbsolutePath(), pArchivPath
               TGdvAttachment attachment = new TGdvAttachment();
               attachment.setDamageReportNumber(schadenNr);
               attachment.setAttachmentName(outputFile.getName());
               attachment.setAttachmentPath(outputFile.getAbsolutePath());
               attachment.setGdvInDocument(pDocument);
               attachmentLines.add(attachment);
            }
            pDocument.setAttchments(attachmentLines);
// create beauftragung without attachments
            try{
                 gdv.getAuftragWerkstattvermittlungOrBehebungsbeauftragungOrFahrzeugbewertung().clear();
                 beauftr.getAnhang().clear();
                 gdv.getAuftragWerkstattvermittlungOrBehebungsbeauftragungOrFahrzeugbewertung().add(beauftr);
                 String xml = XMLHandler.marshalXML(gdv, GDV.class, "UTF-8");
                 byte[] bytes = xml.getBytes();
                 pDocument.setFileContent(bytes);
                 pDocument.setFileSize(bytes.length);
            }catch(JAXBException ex){
                LOG.log(Level.SEVERE, "could not convert gdv to byte content", ex);
                protocol.append("\nFehler bei der Vorbereitung der Beauftragungsdaten für Sichern in der Datenbank");
                return false;
            }
// write info file

                File outputFile = new File(destDir.getAbsolutePath() + File.separator + "info.txt" );
                
                try {
                    org.apache.commons.io.FileUtils.writeByteArrayToFile(outputFile, info.toString().getBytes(), true);
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, "could not save info file " + outputFile.getAbsolutePath(), ex);
                    protocol.append("\nDatei ").append(outputFile.getAbsolutePath()).append(" wurde nicht gespeichert");
                }
// try to create merged docx Document    
            String wordPath = destDir.getAbsolutePath() + File.separator + startStr + "_Schriftwechsel_HUK_" + endStr + ".docx";
            try{
                createWordDocument(info.toString(), files, wordPath);
                protocol.append("\nDie Worddatei als Zusammenfassung aller gelieferten Dokumenten ").append(wordPath).append(" wurde angelegt\n");
            }catch(IOException ex){
                LOG.log(Level.SEVERE, "could not create merged docx file", ex);
            }
        }
        return true;
    }

    public boolean extractData(File gdvFile, File destDir) {
        return extractData(gdvFile, destDir, new TGdvInDocument()) ;
    }

    public String getProtocol() {
        return protocol.toString();
    }

    private void createWordDocument(String pInfoText, List<File> files, String pWordPath) throws IOException{
        try (XWPFDocument doc = new XWPFDocument()) {
            XWPFParagraph p = doc.createParagraph();

            XWPFRun r = p.createRun();
            String[] info = pInfoText.split("\n");
            for(String line: info){
                r.setText(line);
                r.addCarriageReturn();
            }
            r.addBreak(BreakType.PAGE);

            for(File file: files){
                if(file.getName().toLowerCase().trim().endsWith("info.txt")){
                    continue;
                }
                try{
                    if(!addToDocument(r, file)){
                        protocol.append("\n")
                                .append(file.getAbsolutePath())
                                .append(" konnte nicht in die Worddatei zugefügt werden:")
                                .append("\n:Dateiformat wird nicht unterstützt.")
                                .append("\n Unterstützte Formate: emf|wmf|pict|jpeg|png|dib|gif|tiff|eps|bmp|wpg\n");
                    }
                }catch(IOException | org.apache.poi.openxml4j.exceptions.InvalidFormatException ex){
                    LOG.log(Level.SEVERE,"could not add file " + file.getAbsolutePath(), ex);
                    protocol.append("\n").append(file.getAbsolutePath()).append(" konnte nicht in die Worddatei zugefügt werden\n");
                }
            }
            try (FileOutputStream out = new FileOutputStream(pWordPath)) {
                doc.write(out);

            }
            doc.close();
        }
   }

   private boolean addToDocument(XWPFRun r, File pFile) throws FileNotFoundException, IOException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
        String path = pFile.getAbsolutePath().toLowerCase().trim();
         int format;
                if (path.endsWith(".emf")) {
                    format = XWPFDocument.PICTURE_TYPE_EMF;
                } else if (path.endsWith(".wmf")) {
                    format = XWPFDocument.PICTURE_TYPE_WMF;
                } else if (path.endsWith(".pict")) {
                    format = XWPFDocument.PICTURE_TYPE_PICT;
                } else if (path.endsWith(".jpeg") || path.endsWith(".jpg")) {
                    format = XWPFDocument.PICTURE_TYPE_JPEG;
                } else if (path.endsWith(".png")) {
                    format = XWPFDocument.PICTURE_TYPE_PNG;
                } else if (path.endsWith(".dib")) {
                    format = XWPFDocument.PICTURE_TYPE_DIB;
                } else if (path.endsWith(".gif")) {
                    format = XWPFDocument.PICTURE_TYPE_GIF;
                } else if (path.endsWith(".tiff") || path.endsWith(".tif") ) {
                    format = XWPFDocument.PICTURE_TYPE_TIFF;
                } else if (path.endsWith(".eps")) {
                    format = XWPFDocument.PICTURE_TYPE_EPS;
                } else if (path.endsWith(".bmp")) {
                    format = XWPFDocument.PICTURE_TYPE_BMP;
                } else if (path.endsWith(".wpg")) {
                    format = XWPFDocument.PICTURE_TYPE_WPG;
                } else {
                   LOG.log(Level.INFO,"Unsupported file format:{0}. Expected emf|wmf|pict|jpeg|png|dib|gif|tiff|eps|bmp|wpg", path);
                   return false;

                }

                r.setText(path);
                r.addBreak();
                try (FileInputStream is = new FileInputStream(path)) {

                    BufferedImage bimg = ImageIO.read(new File(path));
                    Dimension dim = new Dimension(bimg.getWidth(), bimg.getHeight());

                    double wid = dim.getWidth();
                    double hight = dim.getHeight();
                    
                    double scale = 1.0;
                    if(wid >72*6){
                        scale = (72*6)/wid;
                    }
                    r.addPicture(is, format, path, Units.toEMU(wid*scale), Units.toEMU(hight * scale)); 
                }
                r.addBreak(BreakType.PAGE);
                return true;
}


}
