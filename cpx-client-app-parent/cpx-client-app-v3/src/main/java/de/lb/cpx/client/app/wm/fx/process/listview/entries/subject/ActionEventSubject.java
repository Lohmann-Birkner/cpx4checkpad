/*
 * Copyright (c) 2019 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.listview.entries.subject;

import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmAction;
import de.lb.cpx.wm.model.TWmEvent;
import de.lb.cpx.wm.model.enums.WmEventTypeEn;
import java.text.MessageFormat;

/**
 * Simple Pojo to store values for ListView
 *
 * @author niemeier
 */
public class ActionEventSubject extends EventSubject<TWmAction> {

    public ActionEventSubject(final WmEventTypeEn pType, final TWmAction pItem) {
        super(pType, pItem);
    }

    public ActionEventSubject(TWmEvent pEvent) {
        super(pEvent);
    }

    @Override
    public Object[] getTextParameters() {
        return new Object[]{
            getActionType()
        };
    }

    private String getActionType() {
//        if (item == null) {
//            return PLACEHOLDER; //"----";
//        }
        final long internalId = item.getActionType();
        if (internalId == 0L) {
            return PLACEHOLDER;
        }
        String cache = MenuCache.instance().getActionSubjectName(internalId);
        return cache == null ? MessageFormat.format(PLACEHOLDER_ID, internalId) /* "----" */ : cache;
    }

    @Override
    public String getRemovedDescription() {
        return super.getRemovedDescription(Lang.getEventNameAction()); //Aktion wurde gelöscht
    }

}
