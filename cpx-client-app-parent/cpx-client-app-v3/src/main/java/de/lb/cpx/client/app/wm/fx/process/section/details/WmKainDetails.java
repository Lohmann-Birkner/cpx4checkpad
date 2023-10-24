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
package de.lb.cpx.client.app.wm.fx.process.section.details;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmKainOperations;
import de.lb.cpx.model.TP301Kain;
import de.lb.cpx.shared.lang.Lang;

/**
 * Detailclass to expose detail pane to the history, avoid doublicate source
 * code
 *
 * @author wilde
 */
public class WmKainDetails extends WmKainInkaDetails<TP301Kain> {

    public WmKainDetails(ProcessServiceFacade pFacade, TP301Kain pItem) {
        super(pFacade, pItem);
    }

    @Override
    public String getDetailTitle() {
        return Lang.getEventNameKain();
    }

    @Override
    public WmKainOperations getOperations() {
        return new WmKainOperations(facade);
    }

}
