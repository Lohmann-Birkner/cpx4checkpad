/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.gdv;

import de.lb.cpx.gdv.communication.GdvBeauftragung;
import de.lb.cpx.gdv.communication.ItemsCreater;
import de.lb.cpx.gdv.gdvimport.GdvImportDocumentProcess;
import de.lb.cpx.gdv.messages.GDV;
import de.lb.cpx.rule.util.XMLHandler;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author gerschmann
 */
public class Gdv {

    private static final Logger LOG = Logger.getLogger(Gdv.class.getName());
    private static final String attachmentDir = "d:\\temp\\attachments_in";
    private static final String attachmentDirTest = "d:\\temp\\attachments_out";
    private static final String xmlDir = "d:\\temp\\gdv_xml";
    private static final String schadenNr = "12-11-503/000001-K";
    

    
    public static void main(String[]args){
        
        try {
//            GdvRueckfrage answer = new GdvRueckfrage();
//            GDV gdv = answer.getGdv();
//            String path = "D:\\Projekte\\gdv\\1.Datensatz zum Testen.xml";
            String test ="12-11-123/456789-A";
            String test1 ="12-11-1234/456789-A";
            String test2 ="12-11-1234/456789-AB";
            String test3 ="12-11-1234/4567w89-A";
            String test4 =" 12-11-123/456789-A";
            String patt = "^\\d{2}-\\d{2}-\\d{3}/\\d{6}-[A-Z]";

            Pattern pattern = Pattern.compile("\\d{3}/\\d{6}-[A-Z]");
            if(test.matches(patt)){
                LOG.log(Level.INFO, "OK");
                Matcher matcher = pattern.matcher(test);
                if(matcher.find()){
                    String res = matcher.group();
                    LOG.log(Level.INFO, res);
                }
            }
            if(test1.matches(patt)){
                LOG.log(Level.INFO, "not OK");
            }
            if(test2.matches(patt)){
                LOG.log(Level.INFO, "not OK");
            }
            if(test3.matches(patt)){
                LOG.log(Level.INFO, "not OK");
            }
             if(test4.matches(patt)){
                LOG.log(Level.INFO, "not OK");
            }
           Gdv gdv = new Gdv();
            
            File file = gdv.createBeauftragung();

            if(file.exists() && file.canRead()){
                gdv.createAttachments(file);
            }

//            URL url = new File(path).toURI().toURL();

//            String xml = XMLHandler.marshalXML(gdv, GDV.class, "UTF-8");
//            LOG.log(Level.INFO, xml);
//            GDV gdv1 = (GDV)XMLHandler.unmarshalXML(url, GDV.class);
//            Sachverstaendigenbeauftragung bef = (Sachverstaendigenbeauftragung)GdvReader.getNachricht4Typ(gdv1, Sachverstaendigenbeauftragung.class); 
//            LOG.log(Level.INFO, "gdv1 ok");
        } catch (JAXBException | IOException ex) {
            Logger.getLogger(Gdv.class.getName()).log(Level.SEVERE, null, ex);
        }
        LOG.log(Level.INFO, " completed!!");
        System.exit(0);
    }

    private  void createAttachments(File file) {
        GdvImportDocumentProcess process = new GdvImportDocumentProcess();
        File dest = new File(attachmentDirTest);
        if(!dest.exists()){
            dest.mkdir();
        }
        if(dest.exists() && dest.isDirectory() && process.extractData(file, dest)){
            LOG.log(Level.INFO, "Success!!");
        }
    }

    private File createBeauftragung() throws JAXBException, IOException{
        List<String> attachments = getAttachmentsFromDir(attachmentDir);
        GdvBeauftragung beauftr = new GdvBeauftragung(schadenNr, attachments);
        GDV gdv = beauftr.getGdv();
        String xml = XMLHandler.marshalXML(gdv, GDV.class, "UTF-8");
        File file = new File(xmlDir + File.separator + "test_beauftr.xml" );
        org.apache.commons.io.FileUtils.writeByteArrayToFile(file, xml.getBytes());
        return file;

    }

    private List<String> getAttachmentsFromDir(String attachmentDir) {
        List<String> paths = new ArrayList<>();
       Collection<File> files = FileUtils.listFiles(new File(attachmentDir), ItemsCreater.SUFFIX, false);
       if(files != null){
           files.forEach((file) -> {
               paths.add(file.getAbsolutePath());
            });
       }
       return paths;
    }

}
