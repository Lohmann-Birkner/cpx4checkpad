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
 * screen for displaying case fee (Entgelt) in the case simulation
 *
 * @author wilde
 */
public final class CmCaseFeeScene extends SimulationScreen<TCaseDetails, VersionContent> {

    private final VersionManager versionManager;

    public CmCaseFeeScene(VersionManager pVersionManager) throws IOException {
        super(CpxFXMLLoader.getLoader(CmCaseFeeFXMLController.class), pVersionManager.getManagedVersions());
        versionManager = pVersionManager;
        getController().init(pVersionManager);
    }

    @Override
    public CmCaseFeeFXMLController getController() {
        return (CmCaseFeeFXMLController) super.getController();
    }

    @Override
    public void refresh() {
        super.refresh();
        getController().init(versionManager);
    }
}
