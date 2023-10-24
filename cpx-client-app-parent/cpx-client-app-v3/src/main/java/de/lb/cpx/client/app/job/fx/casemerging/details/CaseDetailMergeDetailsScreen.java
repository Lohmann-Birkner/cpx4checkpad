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
package de.lb.cpx.client.app.job.fx.casemerging.details;

import de.lb.cpx.client.app.cm.fx.simulation.model.SimulationScreen;
import de.lb.cpx.client.app.job.fx.casemerging.model.CaseMergeContent;
import de.lb.cpx.client.app.service.facade.CaseMergingFacade;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import java.io.IOException;
import javafx.collections.ObservableList;

/**
 * Scene for CaseData in merge scene
 *
 * @author wilde
 */
public class CaseDetailMergeDetailsScreen extends SimulationScreen<TCase, CaseMergeContent> {

    private final CaseMergingFacade facade;

    /**
     * creates new instance
     *
     * @param pListOfContent list of content
     * @param pFacade facade to handle db access
     * @throws IOException if fxml can not be found
     */
    public CaseDetailMergeDetailsScreen(ObservableList<CaseMergeContent> pListOfContent, CaseMergingFacade pFacade) throws IOException {
        super(CpxFXMLLoader.getLoader(CaseDetailMergeDetailsFXMLController.class), pListOfContent);
        facade = pFacade;
        if (controller != null) {
            controller.afterInitialisingScene();
        }
    }

    /**
     * @return merging facade
     */
    public CaseMergingFacade getFacade() {
        return facade;
    }

    public TCaseDetails getMergedVersion() {
        return getListOfVersions().get(0).getCaseDetails();
    }
}
