/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.gdv.communication.utils;

import de.lb.cpx.gdv.communication.GdvRechnung;
import de.lb.cpx.gdv.communication.GdvRueckantwort;
import de.lb.cpx.gdv.messages.GDV;
import de.lb.cpx.gdv.model.enums.ResponceTypeEn;
import de.lb.cpx.rule.util.XMLHandler;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.constraints.NotNull;

/**
 *
 * @author gerschmann
 */
public class GdvUtils {

    private static final Logger LOG = Logger.getLogger(GdvUtils.class.getName());

//    public enum RESPONCE_TYPE{
//        BEAUFTRAGUNG(){
//            public String toString(){
//                return "Beauftragung";
//            }
//        },
//        RUEKANTWORT(){
//            public String toString(){
//                return "Rückantwort";
//            }
//        },
//        RECHNUNG(){
//            public String toString(){
//                return "Rechnung";
//            }
//        }
//    };
    
    public static final String PATTERN_DAMAGENR = "^\\d{2}-\\d{2}-\\d{3}/\\d{6}-[A-Z]";
    public static final String PATTERN_INSURANCENR = "\\d{3}/\\d{6}-[A-Z]";
//    private SimpleDateFormat dateFormatDetailTime = new SimpleDateFormat("yyyyMMddHHmmss");    
//    private  SimpleDateFormat dateFormatDetailTimeReverse = new SimpleDateFormat("ddMMyyyy-HHmmss");    
  
    
    public static String getDamageNrFromPatientNr(String pPatientNumber){
        // Patient number consists of damage key, initials and birth year separated with "_": 12-11-123/456789-A_AB_1987
        if(pPatientNumber == null || pPatientNumber.length() == 0){
            return null;
        }
        String[] parts = pPatientNumber.split("_");
        if (parts.length < 3){
            return null;
        }
        StringBuilder damageNumber = new StringBuilder();
        for(int i = 0; i < parts.length - 2; i++){
           damageNumber.append(parts[i]);
           if(i < parts.length - 3){
               damageNumber.append("/");
           }
        }
        return damageNumber.toString();
    }
    
    public static String generateResponse(@NotNull String pDamageNumber, @NotNull String pPatientNumber, 
            @NotNull String pInputContent, List<String> pAttachmentPath, ResponceTypeEn pType, String destPath) throws Exception{
        GDV gdv = (GDV) XMLHandler.unmarshalXML(pInputContent, GDV.class);
        SimpleDateFormat dateFormatDetailTime = new SimpleDateFormat("yyyyMMddHHmmss");    
        String dateString = dateFormatDetailTime.format(new Date());
        // check beauftragung 
        String ret = "";
         switch(pType){
            case RUEKANTWORT:
                ret = generateRueckantwort(gdv, pDamageNumber, pPatientNumber, pAttachmentPath);
                break;
            case RECHNUNG:
                ret = generateRechnung(gdv, pDamageNumber, pPatientNumber, pAttachmentPath);
        }
        if(ret.length() > 0){
            File file = new File(destPath + File.separator +pType.name() + "_" + dateString + ".xml" );
            org.apache.commons.io.FileUtils.writeByteArrayToFile(file, ret.getBytes());
            return file.getAbsolutePath();

        }
        return "no xml file generated";
    }
    
    private static String generateRueckantwort(GDV pGdv, String pDamageNumber, String pPatientNumber, List<String> pAttachmentPath) throws Exception {
        
         GdvRueckantwort answer = new GdvRueckantwort(pDamageNumber, pAttachmentPath);
         answer.fillDataFromBeauftragung(pGdv, pPatientNumber); 
         GDV ret = answer.getGdv();
         return XMLHandler.marshalXML(ret, GDV.class, "UTF-8");
    }

    private static String generateRechnung(GDV pGdv, String pDamageNumber, String pPatientNumber, List<String> pAttachmentPath)  throws Exception {
        GdvRechnung bill = new GdvRechnung(pDamageNumber, pAttachmentPath);
        bill.fillDataFromBeauftragung(pGdv, pPatientNumber); 
        GDV ret = bill.getGdv();
        return XMLHandler.marshalXML(ret, GDV.class, "UTF-8");
    }
    
    public static boolean checkDamageNr(String pSchadensNr){
        return pSchadensNr.matches(PATTERN_DAMAGENR);
    }
    
    public static String  createVersNrFromSchadensNr(String pSchadensNr) throws IllegalArgumentException{
        //12-11-123/456789-A
        Pattern pattern = Pattern.compile(PATTERN_INSURANCENR);
        Matcher matcher = pattern.matcher(pSchadensNr);
        if(matcher.find()){
            return matcher.group();
        }
        throw new IllegalArgumentException();   
    }
    

    public static String[] getDocCreationDateWithTime(){
       SimpleDateFormat dateFormatDetailTimeReverse = new SimpleDateFormat("ddMMyyyy-HHmmss");    
       String time = dateFormatDetailTimeReverse.format(new Date());
       return time.split("-");
    }
    
}
