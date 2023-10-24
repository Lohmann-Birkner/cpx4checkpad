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
package de.lb.cpx.service.gdvimport;

import de.lb.cpx.mail.SendMail;
import de.lb.cpx.gdv.gdvimport.GdvImportDocumentProcess;
import de.lb.cpx.gdv.model.TGdvInDocument;
import de.lb.cpx.server.dao.TGdvInDocumentDao;
import java.util.logging.Level;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Lock;
import static javax.ejb.LockType.READ;
import javax.ejb.Stateless;




/**
 * oversee of the defined directory for gdv - xml files,
 * copy new files into target directory
 * remove copied files from gdv - directory after copying
 * send mail when the new file was found
 * @author gerschmann
 */
@Stateless
@Lock(READ)
public class GdvImportDocumentProcessBean {
    
    @EJB
    TGdvInDocumentDao dao;
    
    private static final Logger LOG = Logger.getLogger(GdvImportDocumentProcessBean.class.getName());


    
    public GdvImportDocumentProcessBean(){  
    }
    
//    public GdvImportDocumentProcess(Connection pCaseDbConnection){   
//        caseDbConnection = pCaseDbConnection;
//    }
    
    /**
     * is for test only connection to db will not be used
     * @param gdvDirectory
     * @param targetDirectory
     * @param pArchivDir
     * @param emailFrom
     * @param emailPassword
     * @param emailTo 
     */
    public void startGdvImportDocument(String gdvDirectory, String targetDirectory, String pArchivDir, String emailFrom, String emailPassword, String emailTo) 
            throws IllegalArgumentException{
        LOG.log(Level.INFO, "Start GdvImportDocumentProcess");
        
        GdvImportDocumentProcess gdvImportDocumentProcess= new GdvImportDocumentProcess();
        List<TGdvInDocument>  docList = gdvImportDocumentProcess.importGdvImportDocuments(gdvDirectory, targetDirectory, pArchivDir, emailFrom, emailPassword, emailTo);
        if(docList != null && !docList.isEmpty()){
            try{
                for(TGdvInDocument doc:docList){
                    dao.persist(doc);
                    dao.flush();
                }
//                dao.mergeList(docList);
            }catch(Exception ex){
                LOG.log(Level.SEVERE, "could not save document list", ex);
            }
            // now send the infomail
            if(emailFrom == null || emailFrom.isEmpty() || emailPassword == null || emailPassword.isEmpty()||emailTo == null || emailTo.isEmpty()){
               LOG.log(Level.INFO, "email_from " + ((emailFrom == null || emailFrom.isEmpty())?"not set":emailFrom)
                + "\nemail_to " + ((emailTo == null || emailTo.isEmpty())?"not set":emailTo)
                + (( emailPassword == null || emailPassword.isEmpty())?"emailPassword not set":"")
               );
                LOG.log(Level.INFO, "cannot send mails, GDV Import Protokoll:" + gdvImportDocumentProcess.getProtocol());
            }else{
                try{
                    SendMail mail = new SendMail();
                    mail.sendMail(emailTo, emailFrom, emailPassword, "GDV Import Protokoll", gdvImportDocumentProcess.getProtocol());
                }catch(IllegalArgumentException ex){
                    LOG.log(Level.SEVERE, "", ex);
                    LOG.log(Level.INFO, "email_from " + ((emailFrom == null || emailFrom.isEmpty())?"not set":emailFrom)
                    + "\nemail_to " + ((emailTo == null || emailTo.isEmpty())?"not set":emailTo)
                    + (( emailPassword == null || emailPassword.isEmpty())?"emailPassword not set":"")
                   );
                    LOG.log(Level.INFO, "cannot send mails, GDV Import Protokoll:" + gdvImportDocumentProcess.getProtocol());
                   
                }
            }
        }
    }

}
