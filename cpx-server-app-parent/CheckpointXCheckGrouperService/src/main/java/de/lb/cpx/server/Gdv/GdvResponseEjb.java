/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.server.Gdv;

import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.service.ejb.GdvResponseRemote;
import de.lb.cpx.service.information.CpxDatabase;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author gerschmann
 */
@Stateless
public class GdvResponseEjb implements GdvResponseLocal{
    
    @EJB
    GdvResponseRemote gdvResponse;

    @Override
    public String createGdvAnswer(String database, String patientNumber,  List<String > attachmentPath, String pDestPath) throws Exception{
        gdvResponse.createSession(database);
        return gdvResponse.createGdvAnswer( patientNumber, attachmentPath, pDestPath);
    }

    @Override
    public String createGdvBill(String database, String patientNumber,  List<String > attachmentPath, String pDestPath)  throws Exception{
        gdvResponse.createSession(database);
         return gdvResponse.createGdvBill( patientNumber, attachmentPath, pDestPath); 
    }
    
}
