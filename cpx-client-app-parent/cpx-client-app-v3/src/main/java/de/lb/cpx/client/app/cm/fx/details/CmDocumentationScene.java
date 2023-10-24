/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.details;

import de.lb.cpx.client.app.cm.fx.simulation.model.VersionManager;
import de.lb.cpx.client.app.service.facade.CaseServiceFacade;
import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.model.CpxScreen;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import java.io.IOException;

/**
 * Scene for the documentation Tab
 *
 * @author wilde
 */
public class CmDocumentationScene extends CpxScreen {

    private CaseServiceFacade facade;
    private VersionManager versionManager;

    /**
     * @param pManager manager to access version handling
     * @throws IOException thrown if fxml was not found or erroneous
     */
    public CmDocumentationScene(VersionManager pManager) throws IOException {
        super(CpxFXMLLoader.getLoader(CmDocumentationFXMLController.class));
        if(pManager == null){
            MainApp.showErrorMessageDialog("Es konnte kein Versionsmanager gefunden werden!");
            return;
        }
        facade = pManager.getServiceFacade();
        versionManager = pManager;
        getController().afterInitialisingScene();
    }

    public VersionManager getVersionManager() {
        return versionManager;
    }

    /**
     * @return facade set in scene
     */
    public CaseServiceFacade getFacade() {
        return facade;
    }

}
