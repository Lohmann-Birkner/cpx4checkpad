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
 *    2019  Shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.dao.TLockDao;
import de.lb.cpx.service.properties.CpxAuthorizationException;
import de.lb.cpx.service.properties.RoleProperties;
import de.lb.cpx.shared.dto.LockDTO;
import de.lb.cpx.shared.lang.Lang;

/**
 *
 * @author Shahin
 */
public class CpxAuthorizationChecker {

    private CpxAuthorizationChecker() {
        //
    }

    private static RoleProperties getProps() {
        return ClientManager.getCurrentCpxUser().getRoleProperties(ClientManager.getCurrentCpxRoleId());
    }

    private static String createMessage(final RoleProperties pRoleProperties, final String pReason) {
        return Lang.getAuthorizationDialogMessage(pRoleProperties == null ? "---" : pRoleProperties.getName(), pReason) + "\n" + Lang.getAuthorizationDialogMessageContact();
    }

    public static void checkDeleteCase() {
        final RoleProperties prop = getProps();
        if (prop == null || !prop.isDeleteCaseAllowed()) {
            throw new CpxAuthorizationException(prop, createMessage(prop, "Fälle löschen"));
        }
    }

    public static void checkCancelCase() {
        final RoleProperties prop = getProps();
        if (prop == null || !prop.isCancelCaseAllowed()) {
            throw new CpxAuthorizationException(prop, createMessage(prop, "Fälle stornieren"));
        }
    }

    public static void checkDeleteProcess() {
        final RoleProperties prop = getProps();
        if (prop == null || !prop.isDeleteProcessAllowed()) {
            throw new CpxAuthorizationException(prop, createMessage(prop, "Vorgänge löschen"));
        }
    }

    public static void checkUnlockCase() {
        final RoleProperties prop = getProps();
        if (prop == null || !prop.isUnlockCaseAllowed()) {
            throw new CpxAuthorizationException(prop, createMessage(prop, "Fälle entsperren"));
        }
    }

    private static TLockDao getLockDao() {
        return ClientManager.lookup(TLockDao.class);
    }

    public static void checkUnlockCase(final long pCaseId) {
        boolean sameUser = false;
        for (LockDTO lockDto : getLockDao().getCaseLock(pCaseId)) {
            final String currentClientId = ClientManager.getCurrentCpxClientId();
            if (lockDto.getClientId().equalsIgnoreCase(currentClientId)) {
                sameUser = true;
            }
        }
        final RoleProperties prop = getProps();
        if (prop == null || (!prop.isUnlockCaseAllowed() && !sameUser)) {
            throw new CpxAuthorizationException(prop, createMessage(prop, "Fälle entsperren"));
        }
    }

    public static void checkUnlockProcess() {
        final RoleProperties prop = getProps();
        if (prop == null || !prop.isUnlockProcessAllowed()) {
            throw new CpxAuthorizationException(prop, createMessage(prop, "Vorgänge entsperren"));
        }
    }

    public static void checkUnlockProcess(final long pProcessId) {
        boolean sameUser = false;
        for (LockDTO lockDto : getLockDao().getProcessLock(pProcessId)) {
            final String currentClientId = ClientManager.getCurrentCpxClientId();
            if (lockDto.getClientId().equalsIgnoreCase(currentClientId)) {
                sameUser = true;
            }
        }
        final RoleProperties prop = getProps();
        if (prop == null || (!prop.isUnlockProcessAllowed() && !sameUser)) {
            throw new CpxAuthorizationException(prop, createMessage(prop, "Vorgänge entsperren"));
        }
    }

    public static void checkEditReminder() {
        final RoleProperties prop = getProps();
        if (prop == null || !prop.isEditReminderAllowed()) {
            throw new CpxAuthorizationException(prop, createMessage(prop, "Wiedervorlagen bearbeiten"));
        }
    }

    public static void checkEditReminderOfOtherUser(long pCreationUserId) {
        final RoleProperties prop = getProps();
        if (pCreationUserId != ClientManager.getCurrentCpxUserId()) {
            if (prop == null || !prop.isEditReminderOfOtherUserAllowed()) {
                throw new CpxAuthorizationException(prop, createMessage(prop, "Wiedervorlagen anderer Anwender bearbeiten"));
            }
        }
    }

    public static void checkEditAction() {
        final RoleProperties prop = getProps();
        if (prop == null || !prop.isEditActionAllowed()) {
            throw new CpxAuthorizationException(prop, createMessage(prop, "Aktionen bearbeiten"));
        }
    }

    public static void checkEditDocument() {
        final RoleProperties prop = getProps();
        if (prop == null || !prop.isEditDocumentAllowed()) {
            throw new CpxAuthorizationException(prop, createMessage(prop, "Dokumente bearbeiten"));
        }
    }

    public static void checkEditDocumentOfOtherUser(long pCreationUserId) {

        final RoleProperties prop = getProps();
        if (pCreationUserId != ClientManager.getCurrentCpxUserId()) {
            if (prop == null || !prop.isEditDocumentOfOtherUserAllowed()) {
                throw new CpxAuthorizationException(prop, createMessage(prop, "Dokumente anderer Anwender bearbeiten"));
            }
        }
    }

    public static void checkEditRequestOfOtherUser(long pCreationUserId) {
        final RoleProperties prop = getProps();
        if (pCreationUserId != ClientManager.getCurrentCpxUserId()) {
            if (prop == null || !prop.isEditRequestOfOtherUserAllowed()) {
                throw new CpxAuthorizationException(prop, createMessage(prop, "Anfragen anderer Anwender bearbeiten"));
            }
        }
    }

    public static void checkDoFinalisation() {
        final RoleProperties prop = getProps();
        if (prop == null || !prop.isDoFinalisationAllowed()) {
            throw new CpxAuthorizationException(prop, createMessage(prop, "Vorgangsabschluss"));
        }
    }

    public static void checkEditRule() {
        final RoleProperties prop = getProps();
        if (prop == null || !prop.isEditRuleAllowed()) {
            throw new CpxAuthorizationException(prop, createMessage(prop, "Regeln ändern"));
        }
    }

    public static void checkBatchgrouping() {
        final RoleProperties prop = getProps();
        if (prop == null || !prop.isBatchgroupingAllowed()) {
            throw new CpxAuthorizationException(prop, createMessage(prop, "Batchgroupen"));
        }
    }

    public static void checkCaseMerging() {
        final RoleProperties prop = getProps();
        if (prop == null || !prop.isCaseMergingAllowed()) {
            throw new CpxAuthorizationException(prop, createMessage(prop, "Fallzusammenführung"));
        }
    }

    public static void checkExportData() {
        final RoleProperties prop = getProps();
        if (prop == null || !prop.isExportDataAllowed()) {
            throw new CpxAuthorizationException(prop, createMessage(prop, "Batchgroupen"));
        }
    }
}
