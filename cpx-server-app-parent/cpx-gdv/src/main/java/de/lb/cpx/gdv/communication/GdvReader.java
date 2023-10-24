/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.gdv.communication;

import de.lb.cpx.gdv.messages.GDV;
import de.lb.cpx.gdv.messages.Sachverstaendigenbeauftragung;
import de.lb.cpx.rule.util.XMLHandler;
import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBException;
import org.apache.poi.ss.formula.functions.T;


/**
 *
 * @author gerschmann
 */
public class GdvReader {

    private static final Logger LOG = Logger.getLogger(GdvReader.class.getName());
    
        private static GdvReader instance;
        
        private GdvReader(){
        }
    
        public static synchronized GdvReader getInstance() {
        if (instance == null) {
            instance = new GdvReader();
            
        }
        return instance;
    }


    
    public Sachverstaendigenbeauftragung getExpertCommissioningFromXml(@NotNull GDV gdv) {
            return (Sachverstaendigenbeauftragung)GdvReader.getNachricht4Typ(gdv, Sachverstaendigenbeauftragung.class); 
     }
    
    public GDV getGdvFromXml(@NotNull File pXmlFile, StringBuilder pProtocol) {
        try{
           return (GDV)XMLHandler.unmarshalXML(pXmlFile.toURI().toURL(), GDV.class);
           
        }catch(MalformedURLException | JAXBException ex){
            LOG.log(Level.SEVERE, "could not extract GDV class from xml file: " + pXmlFile.getAbsolutePath(), ex);
            if(pProtocol != null){
                pProtocol.append("could not extract GDV class from xml file: ").append(pXmlFile.getAbsolutePath());
            }
            return null;
        }
    }
    

    /**
     *
     * @param pGdv
     * @param clazz
     * @return
     */
    public static Object getNachricht4Typ(GDV pGdv, Class clazz){
        if(clazz == null){
            return null;
        }
        List<Object> members = pGdv.getAuftragWerkstattvermittlungOrBehebungsbeauftragungOrFahrzeugbewertung();
        if(members.isEmpty() ){
            return null;
        }
        for(Object obj: members){
           if(obj.getClass().equals(clazz)){
               return obj;
           }
        }
        return null;
    }
    

}
