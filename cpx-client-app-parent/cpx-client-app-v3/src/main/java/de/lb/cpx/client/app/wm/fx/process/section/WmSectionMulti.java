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
package de.lb.cpx.client.app.wm.fx.process.section;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.List;

/**
 * general class for sections that allow to operations on multiple selected
 * entries (e.g. reminders, cases, documents).
 *
 * @author niemeier
 * @param <E> type
 */
public abstract class WmSectionMulti<E extends AbstractEntity> extends WmSection<E> {

    public WmSectionMulti(ProcessServiceFacade pFacade) {
        super(null, pFacade);
    }

    public WmSectionMulti(final String pTitle, final ProcessServiceFacade pFacade) {
        super(pTitle, pFacade);
    }

    public abstract List<E> getSelectedItems();

    protected void editItems() {
        for (E item : getSelectedItems()) {
            editItem(item);
        }
    }

    protected void removeItems() {
        for (E item : getSelectedItems()) {
            removeItem(item);
        }
    }

}
