/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 */
public class LockDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(LockDTO.class.getName());

    public final String database;
    //public final Long requestedCaseId;
    public final Date since;
    public final Date expires;
    public final Long userId;
    public final String userName;
    public final String cause;
    public final String clientId;
    public final long caseId;
    public final long processId;
    public final String hosIdent;
    public final String caseNumber;
    public final Long workflowNumber;

    //Also possible useful informations
    //requested by (userid, clientid, username)
    //requested at (datetime)
    public LockDTO(final String pDatabase, /* final Long pRequestedCaseId, */
            final Date pSince, final Date pExpires, final Long pUserId,
            final String pUserName, final String pCause, final String pClientId,
            final long pCaseId, final String pHosIdent, final String pCaseNumber,
            final long pProcessId, final Long pWorkflowNumber) {
        database = pDatabase == null ? "" : pDatabase.trim();
        //requestedCaseId = (pRequestedCaseId != null && pRequestedCaseId.equals(0L))?null:pRequestedCaseId;
        since = pSince;
        expires = pExpires;
        userId = (pUserId != null && pUserId.equals(0L)) ? null : pUserId;
        userName = pUserName == null ? "" : pUserName.trim();
        cause = pCause == null ? "" : pCause.trim();
        clientId = pClientId == null ? "" : pClientId.trim();
        caseId = pCaseId;
        hosIdent = pHosIdent == null ? "" : pHosIdent.trim();
        caseNumber = pCaseNumber == null ? "" : pCaseNumber.trim();
        processId = pProcessId;
        workflowNumber = (pWorkflowNumber != null && pWorkflowNumber.equals(0L)) ? null : pWorkflowNumber;
    }

    public static LockDTO createWithClientId(final String pClientId) {
        final String database = "";
        final Date since = null;
        final Date expires = null;
        final Long userId = null;
        final String userName = "";
        final String cause = "";
        final String clientId = (pClientId == null ? "" : pClientId.trim());
        final long caseId = 0L;
        final String hosIdent = "";
        final String caseNumber = "";
        final long processId = 0L;
        final Long workflowNumber = null;
        return new LockDTO(database, since, expires, userId, userName, cause,
                clientId, caseId, hosIdent, caseNumber, processId, workflowNumber);
    }

    /**
     * @return the database
     */
    public String getDatabase() {
        return database;
    }

    /**
     * @return the requestedCaseId
     */
//  public Long getRequestedCaseId() {
//    return requestedCaseId;
//  }
    /**
     * @return the since
     */
    public Date getSince() {
        return since;
    }

    /**
     * @return the expires
     */
    public Date getExpires() {
        return expires;
    }

    /**
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return the cause
     */
    public String getCause() {
        return cause;
    }

    /**
     * @return the clientId
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * @return the caseId
     */
    public long getCaseId() {
        return caseId;
    }

    /**
     * @return the caseId
     */
    public long getProcessId() {
        return processId;
    }

    /**
     * @return the workflowNumber
     */
    public Long getWorkflowNumber() {
        return workflowNumber;
    }

    /**
     * @return the hosIdent
     */
    public String getHosIdent() {
        return hosIdent;
    }

    /**
     * @return the caseNumber
     */
    public String getCaseNumber() {
        return caseNumber;
    }

    public boolean isCaseLock() {
        return caseId > -1;
    }

    public boolean isProcessLock() {
        return processId > -1;
    }

    public boolean isDbLock() {
        return caseId == -1 && processId == -1;
    }

    public String getCaseKey() {
        return hosIdent + "_" + caseNumber;
    }

    /**
     * case -&gt; case number process -&gt; workflow number database -&gt;
     * database name (dbsysX:XXX)
     *
     * @return label, key or name of lock entry
     */
    public String getEntityLabel() {
        if (isCaseLock()) {
            return caseNumber;
        }
        if (isProcessLock()) {
            return String.valueOf(workflowNumber);
        }
        if (isDbLock()) {
            return database;
        }
        LOG.log(Level.WARNING, "Unknown lock entry!");
        return "";
    }

//    /**
//     * case -> TCase
//     * process -> TWmProcess
//     * database -> null
//     * @return entity class for lock entry
//     */
//    public Class<? extends AbstractEntity> getEntityClass() {
//        if (isCaseLock()) {
//            return TCase.class;
//        }
//        if (isProcessLock()) {
//            return TWmProcess.class;
//        }
//        return null;
//    }
    /**
     * case -&gt; case id process -&gt; process id database -&gt; null
     *
     * @return id for lock entry
     */
    public Long getEntityId() {
        if (isCaseLock()) {
            return caseId;
        }
        if (isProcessLock()) {
            return processId;
        }
//        if (isDbLock()) {
//            return null;
//        }
        return null;
    }

    public boolean isMatching(final LockDTO pMatch) {
        if (pMatch == null) {
            throw new IllegalArgumentException("to find out if lock dto matches specific criteria, you have to specify a matching lock dto (pMatch cannot be null!)");
        }
        return (pMatch.caseId == 0L || Objects.equals(pMatch.caseId, this.caseId))
                && (pMatch.caseNumber.isEmpty() || Objects.equals(pMatch.caseNumber, this.caseNumber))
                && (pMatch.cause.isEmpty() || Objects.equals(pMatch.cause, this.cause))
                && (pMatch.clientId.isEmpty() || Objects.equals(pMatch.clientId, this.clientId))
                && (pMatch.database.isEmpty() || Objects.equals(pMatch.database, this.database))
                && (pMatch.expires == null || Objects.equals(pMatch.expires, this.expires))
                && (pMatch.hosIdent.isEmpty() || Objects.equals(pMatch.hosIdent, this.hosIdent))
                && (pMatch.processId == 0L || Objects.equals(pMatch.processId, this.processId))
                && (pMatch.since == null || Objects.equals(pMatch.since, this.since))
                && (pMatch.userId == null || Objects.equals(pMatch.userId, this.userId))
                && (pMatch.userName.isEmpty() || Objects.equals(pMatch.userName, this.userName))
                && (pMatch.workflowNumber == null || Objects.equals(pMatch.workflowNumber, this.workflowNumber));
    }

}
