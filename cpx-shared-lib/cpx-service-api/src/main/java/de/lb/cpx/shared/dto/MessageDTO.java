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
package de.lb.cpx.shared.dto;

import java.io.Serializable;
import javax.batch.runtime.BatchStatus;

/**
 *
 * @author niemeier
 */
public class MessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int phase;
    private final int maxPhases;
//    private final static int MAX_PHASES = 5;
    private final int subphase;
    private final int maxSubPhases;
    private final BatchStatus batchStatus;
    private final Exception exception;
    private final String comment;
    private final double percentPhase;
    private final double percentSubphase;

    private final long jobNumber;
    //private final Properties batchProperties;
    private final String batchReasonForFailure;

    public MessageDTO() {
        this(0, 0, 0, 0, BatchStatus.ABANDONED, null, 0L, null, null);
    }

    public MessageDTO(final int pPhase, final int pMaxPhases,
            final int pSubphase, final int pSubphases,
            final BatchStatus pBatchStatus, final Exception pException,
            final long pJobNumber/*, Properties batchProperties*/,
            final String pBatchReasonForFailure,
            final String pComment) {
        this.phase = pPhase;
        this.maxPhases = pMaxPhases;
        this.subphase = pSubphase;
        this.maxSubPhases = pSubphases;
        this.batchStatus = pBatchStatus == null ? BatchStatus.STARTED : pBatchStatus;
        this.exception = pException;
        this.jobNumber = pJobNumber;
        this.percentPhase = BatchStatus.COMPLETED.equals(this.batchStatus) ? 1.0D
                : (maxPhases == 0 ? 0.0D : (100.0D * phase) / maxPhases);
        this.percentSubphase = BatchStatus.COMPLETED.equals(this.batchStatus) ? 1.0D
                : (maxSubPhases == 0 ? 0.0D : (100.0D * subphase) / maxSubPhases);
//        Properties bp = batchProperties == null ? null : new Properties();
//        if (bp != null) {
//            bp.putAll(batchProperties);
//        }
//        this.batchProperties = bp;
        this.batchReasonForFailure = pBatchReasonForFailure == null ? "" : pBatchReasonForFailure.trim();
        this.comment = pComment == null ? "" : pComment.trim();
    }

    public long getJobNumber() {
        return jobNumber;
    }

//    public Properties getBatchProperties() {
//        Properties bp = batchProperties == null ? null : new Properties();
//        if (bp != null) {
//            bp.putAll(batchProperties);
//        }
//        return bp;
//    }
    public int getPhase() {
        return phase;
    }

    public int getMaxPhases() {
        return maxPhases;
    }

    public int getSubphase() {
        return subphase;
    }

    public int getMaxSubphases() {
        return maxSubPhases;
    }

    public BatchStatus getBatchStatus() {
        return batchStatus;
    }

    public Exception getException() {
        return exception;
    }

    public String getBatchReasonForFailure() {
        return batchReasonForFailure;
    }

    public String getComment() {
        return comment;
    }

    public double getPercentPhase() {
        return percentPhase;
    }

    public double getPercentSubphase() {
        return percentSubphase;
    }

}
