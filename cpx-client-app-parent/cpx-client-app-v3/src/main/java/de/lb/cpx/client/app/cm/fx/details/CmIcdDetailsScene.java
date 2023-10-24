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
 * screen to show the icd and ops data in the case management / case simulation
 * scene
 *
 * @author wilde
 */
public final class CmIcdDetailsScene extends SimulationScreen<TCaseDetails, VersionContent> {

    private final VersionManager versionManager;

    /**
     * consturct new icd/ops screen
     *
     * @param pManager versionmanager instance
     * @throws IOException thrown when no fxml is found
     */
    public CmIcdDetailsScene(VersionManager pManager) throws IOException {//ObservableList<VersionContent> pListOfVersions,CaseServiceFacade pServiceFacade) throws IOException{
        super(CpxFXMLLoader.getLoader(CmIcdDetailsFXMLController.class), pManager.getManagedVersions());
//        long start = System.currentTimeMillis();
        versionManager = pManager;

        getController().init(pManager.getManagedVersions(), pManager.getServiceFacade());
        getController().afterInitialisingScene();
//        LOG.info("init controller in " + (System.currentTimeMillis()-start));
    }

    @Override
    public CmIcdDetailsFXMLController getController() {
        return (CmIcdDetailsFXMLController) super.getController();
    }

    public VersionManager getVersionManager() {
        return versionManager;
    }

//    public final LaboratoryDataListFilterManager getFilterManager() {
//        return LaboratoryDataListFilterManager.getInstance();
//    }
}
