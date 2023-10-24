/*
 * Copyright (c) 2016 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2016  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.util;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.cm.fx.CaseManagementScene;
import de.lb.cpx.client.app.wm.fx.WmMainFrameScene;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.shared.dto.LockException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dirk Niemeier
 */
public class SceneLoader {

    private static final Logger LOG = Logger.getLogger(SceneLoader.class.getName());

//  public static class CpxFxmlUrl {
//    public URL url;
//    public String path;
//    
//    public CpxFxmlUrl(final URL pUrl, final String pPath) {
//      url = pUrl;
//      path = pPath;
//    }
//  }
    public static synchronized SceneLoader getInstance() {
        return new SceneLoader();
    }

//    public CpxScene loadCaseDetailsMainFrame(long pCaseId){
//        try {
//            CpxScene caseOverView = CpxFXMLLoader.getScene(CaseDetailsMainViewFXMLController.class);
//            ((CaseDetailsMainViewFXMLController)caseOverView.getController()).initGuiWithCaseId(pCaseId, false);
//            return caseOverView;
//        } catch (Exception exc){
//            MainApp.showErrorMessageDialog(exc);
//        }
//        return null;
//    }
    public CpxScene loadCaseMangementScene(long pCaseId) {
        return MainApp.execWithLockDialog((Object param) -> {
            LOG.log(Level.INFO, "Open hospital case with id " + pCaseId + "...");
            try {
                return new CaseManagementScene(pCaseId);
            } catch (IOException | CpxIllegalArgumentException exc) {
                LOG.log(Level.SEVERE, "Loading management scene failed for this case id: " + pCaseId, exc);
                MainApp.showErrorMessageDialog(exc);
            } catch (LockException exc) {
                LOG.log(Level.SEVERE, "Case with id " + pCaseId + " or whole database is already locked");
                LOG.log(Level.FINER, "Was not able to obtain lock for case with id " + pCaseId, exc);
                throw exc;
                //MainApp.showErrorMessageDialog(exc);
            }
            return null;
        });
    }

    public CpxScene loadWmMainFrame(Long pProcessId) {
        return MainApp.execWithLockDialog((Object param) -> {
            try {
                return new WmMainFrameScene(pProcessId);
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Loading mainframe failed for this process id: " + pProcessId, ex);
                MainApp.showErrorMessageDialog(ex);
                //      ex.printStackTrace();
            } catch (LockException exc) {
                LOG.log(Level.SEVERE, "Process with id " + pProcessId + " or whole database is already locked");
                LOG.log(Level.FINER, "Was not able to obtain lock for process with id " + pProcessId, exc);
                //MainApp.showErrorMessageDialog(exc);
                throw exc;
            }
            return null;
        });
    }
}
