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
 *    2019  niemeier - DTO which is transferred to the client to show how much objects are grouped.
 */
package de.lb.cpx.shared.dto.broadcast;

import de.lb.cpx.service.information.ConnectionString;
import java.io.Serializable;
import javax.batch.runtime.BatchStatus;

/**
 *
 * @author niemeier
 * @param <T> result type
 */
public class StatusBroadcastDTO<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final BroadcastOriginEn origin; //e.g. BATCHGROUPING, IMPORT, MERGING
    private final String clientId;
    private final Long userId;
    private final String userName;
    private final ConnectionString connectionString; //e.g. dbsys1:DB_PNA
    private final String comment;
    private final Long jobNumber;
    private final BatchStatus batchStatus;
    private final Exception exception;
    private final T result;
    //private final String batchReasonForFailure;    

    public StatusBroadcastDTO(final BroadcastOriginEn pOrigin, final String pClientId, final Long pUserId,
            final String pUserName, final String pDatabase, final String pComment, final Long pJobNumber,
            final BatchStatus pBatchStatus, final Exception pException, final T pResult) {
        this.origin = pOrigin;
        this.clientId = pClientId == null ? "" : pClientId.trim();
        this.userId = pUserId != null && pUserId.equals(0L) ? null : pUserId;
        this.userName = pUserName == null ? "" : pUserName.trim();
        this.connectionString = new ConnectionString(pDatabase == null ? "" : pDatabase.trim());
        this.comment = pComment == null ? "" : pComment.trim();
        this.jobNumber = pJobNumber != null && pJobNumber.equals(0L) ? null : pJobNumber;
        //this.batchStatus = pBatchStatus == null ? BatchStatus.COMPLETED : pBatchStatus;
        this.batchStatus = pBatchStatus;
        this.exception = pException;
        this.result = pResult;
    }

//    public StatusBroadcastDTO(final String pClientId, final Long pUserId, 
//            final String pUserName, final String pComment) {
//        this(pClientId, pUserId, pUserName, pComment, null, null, null);
//    }
    /**
     * @return the clientId
     */
    public String getClientId() {
        return clientId;
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
     * @return the database
     */
    public String getDatabase() {
        return connectionString == null ? "" : connectionString.getDatabase();
    }

    /**
     * @return the persistence unit
     */
    public String getPersistenceUnit() {
        return connectionString == null ? "" : connectionString.getPersistenceUnit();
    }

    /**
     * @return the connection string
     */
    public ConnectionString getConnectionString() {
        return connectionString;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @return the jobNumber
     */
    public Long getJobNumber() {
        return jobNumber;
    }

    /**
     * @return the batchStatus
     */
    public BatchStatus getBatchStatus() {
        return batchStatus;
    }

    /**
     * @return the exception
     */
    public Exception getException() {
        return exception;
    }

    public T getResult() {
        return result;
    }

//    public void setResult(final T pResult) {
//        result = pResult;
//    }
    /**
     * @return the origin
     */
    public BroadcastOriginEn getOrigin() {
        return origin;
    }

    @Override
    public String toString() {
        return "StatusBroadcastDTO{" + "clientId=" + clientId + ", userId=" + userId + ", userName=" + userName + ", database=" + connectionString + ", comment=" + comment + ", jobNumber=" + jobNumber + ", batchStatus=" + batchStatus + ", exception=" + exception + '}';
    }

}
