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
 *
 * @author wilde
 */
public class IcdMergeDetailsScreen extends SimulationScreen<TCase, CaseMergeContent> {

    private final CaseMergingFacade facade;

    public IcdMergeDetailsScreen(ObservableList<CaseMergeContent> pListOfContent, CaseMergingFacade pFacade) throws IOException {
        super(CpxFXMLLoader.getLoader(IcdMergeDetailsFXMLController.class), pListOfContent);
        facade = pFacade;
        if (controller != null) {
            controller.afterInitialisingScene();
        }
    }

    public CaseMergingFacade getFacade() {
        return facade;
    }

    public TCaseDetails getMergedVersion() {
        return getListOfVersions().get(0).getCaseDetails();
    }

}
