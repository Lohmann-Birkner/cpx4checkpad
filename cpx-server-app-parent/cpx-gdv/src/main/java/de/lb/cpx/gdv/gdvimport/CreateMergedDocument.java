/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.gdv.gdvimport;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import java.awt.Dimension;
import java.io.IOException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.BreakType;

/**
 *
 * @author gerschmann
 */
public class CreateMergedDocument {

    private static final Logger LOG = Logger.getLogger(CreateMergedDocument.class.getName());
    
    
    public CreateMergedDocument(){
        
    }
    
    public void createWordDocument(String pInDir, String pOutFile) throws IOException
    {
        File inputDirectory = new File(pInDir);
        if(!inputDirectory.exists() || !inputDirectory.isDirectory() || !inputDirectory.canRead()){
            LOG.log(Level.INFO, "cannot create word file from path {0}", pInDir);
            return;
        }
        File[] files = inputDirectory.listFiles();
        if(files == null || files.length == 0){
            LOG.log(Level.INFO, "directory {0} is empty", pInDir);
            return;
        }
        try (XWPFDocument doc = new XWPFDocument()) {
                XWPFParagraph p = doc.createParagraph();

                XWPFRun r = p.createRun();
                
                for(File file: files){
                    if(file.getName().toLowerCase().trim().endsWith(".txt")){
                        continue;
                    }
                    try{
                        addToDocument(r, file);
                    }catch(IOException | org.apache.poi.openxml4j.exceptions.InvalidFormatException ex){
                        LOG.log(Level.SEVERE,"could not add file " + file.getAbsolutePath(), ex);
                    }
                }
            try (FileOutputStream out = new FileOutputStream(pOutFile)) {
                doc.write(out);

            }
            doc.close();
        }
        
    }
    

    private void addToDocument(XWPFRun r, File pFile) throws FileNotFoundException, IOException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
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
                   return;

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
}

    
    public static void main(String[] args) throws IOException  {
        
        CreateMergedDocument document = new CreateMergedDocument();
        String inDir = "D:\\temp\\cpx_documents\\2311645380079S";
        String outFile = "D:\\temp\\cpx_documents\\2311645380079S.docx";
        document.createWordDocument(inDir, outFile);
    }

}
