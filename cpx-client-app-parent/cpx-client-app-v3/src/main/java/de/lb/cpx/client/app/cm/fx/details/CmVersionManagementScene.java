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
 * screen to manage version of a case, with versions are in this instance case
 * details
 *
 * @author wilde
 */
public final class CmVersionManagementScene extends SimulationScreen<TCaseDetails, VersionContent> {

    /**
     * creats a new screen instance to manage case details
     *
     * @param pManager verion manager instance to access server functions and
     * update display status of an version
     * @throws IOException thrown when fmxl is not found
     */
    public CmVersionManagementScene(VersionManager pManager) throws IOException {
        super(CpxFXMLLoader.getLoader(CmVersionManagementFXMLController.class), pManager.getManagedVersions());
        getController().init(pManager);
    }

    @Override
    public CmVersionManagementFXMLController getController() {
        return (CmVersionManagementFXMLController) super.getController();
    }
}
