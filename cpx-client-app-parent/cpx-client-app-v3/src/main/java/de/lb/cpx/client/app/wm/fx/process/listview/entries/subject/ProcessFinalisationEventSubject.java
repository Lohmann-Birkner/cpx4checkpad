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

import de.lb.cpx.wm.model.TWmEvent;
import de.lb.cpx.wm.model.TWmProcessHospitalFinalisation;
import de.lb.cpx.wm.model.enums.WmEventTypeEn;

/**
 * Simple Pojo to store values for ListView
 *
 * @author niemeier
 */
public class ProcessFinalisationEventSubject extends EventSubject<TWmProcessHospitalFinalisation> {

    public ProcessFinalisationEventSubject(final WmEventTypeEn pType, final TWmProcessHospitalFinalisation pItem) {
        super(pType, pItem);
    }

    public ProcessFinalisationEventSubject(TWmEvent pEvent) {
        super(pEvent);
    }

    @Override
    public Object[] getTextParameters() {
        return new Object[]{};
    }

//    private String getProcessResult() {
////        if (!item.isProcessHospital()) {
////            return null;
////        }
////        TWmProcessHospital process = (TWmProcessHospital) item;
////        if (process.getProcessHospitalFinalisation() == null) {
////            return PLACEHOLDER; //"----";
////        }
//        if (!item.isProcessHospital()) {
//            return null;
//        }
//        TWmProcessHospital proc = (TWmProcessHospital) item;
//        if (proc.getProcessHospitalFinalisation() == null) {
//            return null;
//        }
//        final long internalId = proc.getProcessHospitalFinalisation().getClosingResult();
//        if (internalId == 0L) {
//            return PLACEHOLDER;
//        }
//        String cache = MenuCache.instance().getProcessResultName(internalId);
//        return cache == null ? MessageFormat.format(PLACEHOLDER_ID, internalId) /* "----" */ : cache;
//    }
}
