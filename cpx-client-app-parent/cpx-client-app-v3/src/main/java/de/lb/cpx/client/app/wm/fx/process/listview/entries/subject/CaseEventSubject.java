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

import de.lb.cpx.model.TCase;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmEvent;
import de.lb.cpx.wm.model.enums.WmEventTypeEn;

/**
 * Simple Pojo to store values for ListView
 *
 * @author niemeier
 */
public class CaseEventSubject extends EventSubject<TCase> {

    public CaseEventSubject(final WmEventTypeEn pType, final TCase pItem) {
        super(pType, pItem);
    }

    public CaseEventSubject(TWmEvent pEvent) {
        super(pEvent);
    }

    @Override
    public Object[] getTextParameters() {
        return new Object[]{
            getCaseType(),
            getCaseNumber()
        };
    }

    private String getCaseType() {
        if (item.getCsCaseTypeEn() == null) {
            return PLACEHOLDER; //"----";
        }
        return item.getCsCaseTypeEn().getTranslation().getValue();
    }

    private String getCaseNumber() {
        if (item.getCsCaseNumber() == null) {
            return PLACEHOLDER; //"----";
        }
        return item.getCsCaseNumber();
    }

    @Override
    public String getRemovedDescription() {
        return super.getRemovedDescription(Lang.getEventNameCase()); //Fall wurde gelöscht
    }

}
