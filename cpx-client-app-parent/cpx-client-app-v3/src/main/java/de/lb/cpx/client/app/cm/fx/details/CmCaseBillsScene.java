/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.details;

import de.lb.cpx.client.app.service.facade.CaseServiceFacade;
import de.lb.cpx.client.core.model.CpxScreen;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import java.io.IOException;

/**
 * Scene for the Case Bills Tab
 *
 * @author nandola
 */
//class CmCaseBillsScene extends SimulationScreen {
public class CmCaseBillsScene extends CpxScreen {

    private final CaseServiceFacade facade;

    /**
     * @param cFacade service facade to access database/server functions
     * @throws IOException thrown if fxml was not found or erroneous
     */
    public CmCaseBillsScene(CaseServiceFacade cFacade) throws IOException {
        super(CpxFXMLLoader.getLoader(CmCaseBillsFXMLController.class));
//        super(CpxFXMLLoader.getLoader(getClass().getResource("/fxml/cmCaseBillsFXML.fxml")));
        facade = cFacade;
//        getController().afterInitialisingScene();
        getController().init(cFacade);
    }

    /**
     * @return facade set in scene
     */
    public CaseServiceFacade getFacade() {
        return facade;
    }

    @Override
    public CmCaseBillsFXMLController getController() {
        return (CmCaseBillsFXMLController) super.getController();
    }
}
