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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.section.operations;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.process.section.details.*;
import de.lb.cpx.model.TCase;
import de.lb.cpx.wm.model.TWmProcessCase;

/**
 * this is just a wrapper and redirects to WmServiceOverviewOperations
 *
 * @author niemeier
 */
public class WmCaseOperations extends WmOperations<TCase> {

    final WmServiceOverviewOperations serviceOverviewOperations;

    public WmCaseOperations(ProcessServiceFacade pFacade) {
        super(pFacade);
        serviceOverviewOperations = new WmServiceOverviewOperations(facade);
    }

    @Override
    public ItemEventHandler openItem(final TCase pItem) {
        TWmProcessCase pc = WmServiceOverviewDetails.createFakeProcessCase(pItem);
        return serviceOverviewOperations.openItem(pc);
    }

    @Override
    public ItemEventHandler editItem(final TCase pItem) {
        TWmProcessCase pc = WmServiceOverviewDetails.createFakeProcessCase(pItem);
        return serviceOverviewOperations.editItem(pc);
    }

    @Override
    public ItemEventHandler createItem() {
        return serviceOverviewOperations.createItem();
    }

    @Override
    public ItemEventHandler removeItem(final TCase pItem) {
        TWmProcessCase pc = WmServiceOverviewDetails.createFakeProcessCase(pItem);
        return serviceOverviewOperations.removeItem(pc);
    }

    @Override
    public String getItemName() {
        return serviceOverviewOperations.getItemName();
    }

}
