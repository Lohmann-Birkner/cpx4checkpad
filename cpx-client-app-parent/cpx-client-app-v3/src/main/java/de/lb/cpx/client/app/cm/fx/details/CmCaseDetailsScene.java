/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.details;

import de.lb.cpx.client.app.cm.fx.simulation.model.SimulationScreen;
import de.lb.cpx.client.app.cm.fx.simulation.model.VersionContent;
import de.lb.cpx.client.app.cm.fx.simulation.model.VersionManager;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import de.lb.cpx.model.TCaseDetails;
import java.io.IOException;

/**
 * screen for the case details in the case management / case simulation
 *
 * @author wilde
 */
public final class CmCaseDetailsScene extends SimulationScreen<TCaseDetails, VersionContent> {

    private final VersionManager versionManager;

//    private final ObservableList<VersionContent> listOfVersions;
//    private final Double SIZE_OF_VERSION_CONTENT = 320.0d;
    /**
     * construct a new instance of the screen
     *
     * @param pManager version manager
     * @throws IOException thrown when no fxml file is found
     */
    public CmCaseDetailsScene(VersionManager pManager) throws IOException {//ObservableList<VersionContent> pListOfVersions,CaseServiceFacade pServiceFacade) throws IOException{
        super(CpxFXMLLoader.getLoader(CmCaseDetailsFXMLController.class), pManager.getManagedVersions());
//        long start = System.currentTimeMillis();
        versionManager = pManager;
        getController().init(pManager.getManagedVersions(), pManager.getServiceFacade());
        getController().afterInitialisingScene();
    }

    @Override
    public CmCaseDetailsFXMLController getController() {
        return (CmCaseDetailsFXMLController) super.getController(); //To change body of generated methods, choose Tools | Templates.
    }

    public VersionManager getVersionManager() {
        return versionManager;
    }
}
