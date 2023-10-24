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
package de.lb.cpx.service.gdv;

import de.lb.cpx.gdv.communication.utils.GdvUtils;
import de.lb.cpx.gdv.model.TGdvInDocument;
import de.lb.cpx.gdv.model.enums.ResponceTypeEn;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.dao.TGdvInDocumentDao;
import de.lb.cpx.service.ejb.GdvResponseRemote;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author gerschmann
 */
@Stateless
public class GdvResponseBean implements GdvResponseRemote{
    
   private static final Logger LOG = Logger.getLogger(GdvResponseBean.class.getName());

    @EJB
    TGdvInDocumentDao gdvInDocumentDao;
    
    @Override
    public String createGdvAnswer(String patientNumber, List<String> attachmentPath, String pDestPath) throws Exception{
       
       return createGdvResponse( patientNumber, attachmentPath, ResponceTypeEn.RUEKANTWORT, pDestPath);
    }
    
    public void createSession(String database){
         ClientManager.createJobSession(database);
    }
 
    @Override
    public String createGdvBill(String patientNumber, List<String> attachmentPath, String pDestPath) throws Exception{
       return createGdvResponse(patientNumber, attachmentPath,  ResponceTypeEn.RECHNUNG, pDestPath);
    }
    
    private String createGdvResponse( String patientNumber, List<String> attachmentPath,  ResponceTypeEn pType, String pDestPath) throws Exception{

       String damageNr =  GdvUtils.getDamageNrFromPatientNr(patientNumber);

       if(damageNr == null){
           LOG.log(Level.SEVERE, "no damage number found for patient number: {0}", patientNumber);
           throw new IllegalArgumentException();
       }
       if(!GdvUtils.checkDamageNr(damageNr)){
           LOG.log(Level.SEVERE, "damage number for patient number {0} does not match the declares format {1} ", new Object[]{ patientNumber, GdvUtils.PATTERN_DAMAGENR});
           throw new IllegalArgumentException();
       }
       List<TGdvInDocument>  xmls = gdvInDocumentDao.findContent2DamageNr(damageNr); 
       if(xmls == null || xmls.isEmpty()){
           LOG.log(Level.INFO, "no basic document for patient {0} and damage number {1} found", new Object[]{patientNumber, damageNr});
           throw new IllegalArgumentException(); 
       }
       return GdvUtils.generateResponse(damageNr, patientNumber, new String(xmls.get(0).getFileContent()), attachmentPath, pType, pDestPath);
        
    }
    

}
