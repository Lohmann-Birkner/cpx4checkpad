/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.tab;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.core.model.fx.tab.TwoLineTab;
import de.lb.cpx.client.core.model.fx.tab.TwoLineTab.TabType;
import de.lb.cpx.model.TPatient;

/**
 * Implementation of the Processoverview Tab
 *
 * @author wilde
 */
public class POverviewTab extends TwoLineTab {

    public POverviewTab(ProcessServiceFacade serviceFacade) {
        super();
        TPatient patient = serviceFacade.getPatient();
        setTitle(patient.getPatNumber());
        setDescription(patient.getPatFullName());
//        setImage(serviceFacade.getPatientImage());
        setGlyph("\uf07c");
        setClosable(false);
    }

    @Override
    public TabType getTabType() {
        return TabType.POVERVIEW;
    }

}
