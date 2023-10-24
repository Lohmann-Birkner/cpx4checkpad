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

import de.lb.cpx.shared.dto.UserDTO;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import de.lb.cpx.wm.model.TWmProcess;
import de.lb.cpx.wm.model.enums.WmEventTypeEn;

/**
 * Simple Pojo to store values for ListView
 *
 * @author niemeier
 */
public class ProcessUserChangedEventSubject extends EventSubject<TWmProcess> {

    private final UserDTO newUser;
    private final UserDTO oldUser;

    public ProcessUserChangedEventSubject(final TWmProcess pItem, final UserDTO pOldUser, final UserDTO pNewUser) {
        super(WmEventTypeEn.processUserChanged, pItem);
        oldUser = pOldUser;
        newUser = pNewUser;
    }

//    public ProcessTopicChangedEventSubject(TWmEvent pEvent) {
//        super(pEvent);
//    }
    @Override
    public Object[] getTextParameters() {
        return new Object[]{
            getOldUserName(),
            getNewUserName()
        };
    }

    private String getOldUserName() {
        return oldUser == null ? "" : oldUser.getUserName();
    }

    private String getNewUserName() {
        return newUser == null ? "" : newUser.getUserName();
    }

    @Override
    protected Translation getTranslation(WmEventTypeEn pType) {
        if (oldUser == null) {
            return Lang.get(Lang.EVENT_TYPE_PROCESS_USER_SET_DESCRIPTION, getTextParameters());
        } 
         else if (newUser == null) {
            return Lang.get(Lang.EVENT_TYPE_PROCESS_USER_DELETE_DESCRIPTION, getTextParameters());
        } else {
            return Lang.get(Lang.EVENT_TYPE_PROCESS_USER_CHANGED_DESCRIPTION, getTextParameters());
        }
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
    /**
     * @return the newUser
     */
    public UserDTO getNewUser() {
        return newUser;
    }

    /**
     * @return the oldUser
     */
    public UserDTO getOldUser() {
        return oldUser;
    }

}
