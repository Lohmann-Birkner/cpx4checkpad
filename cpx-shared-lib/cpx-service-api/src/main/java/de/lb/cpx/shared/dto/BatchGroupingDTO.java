/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Husser - DTO which is transferred to the client to show how much objects are grouped.
 */
package de.lb.cpx.shared.dto;

import de.lb.cpx.model.TBatchResult;
import javax.batch.runtime.BatchStatus;

/**
 *
 * @author Husser
 */
public class BatchGroupingDTO extends MessageDTO {

    private static final long serialVersionUID = 1L;

//    private int groupedItems = 0;
//    private int numberOfAllItems = 0;
//    private Double percent = 0.0;
    private final double caseDetailsPerSecond;
//    private final String comment;
    private final TBatchResult batchResult;

    public BatchGroupingDTO() {
        this(0, 0, 0, 0, BatchStatus.ABANDONED, null, 0L, null, null, 0.0D, null);
    }

    public BatchGroupingDTO(int pPhase, int pMaxPhases, int pSubphase, int pSubphases, BatchStatus pBatchStatus, Exception pException, long pJobNumber, String pBatchReasonForFailure, String pComment, final double pCaseDetailsPerSecond) {
        super(pPhase, pMaxPhases, pSubphase, pSubphases, pBatchStatus, pException, pJobNumber, pBatchReasonForFailure, pComment);
        caseDetailsPerSecond = pCaseDetailsPerSecond;
        batchResult = null;
    }

    public BatchGroupingDTO(int pPhase, int pMaxPhases, int pSubphase, int pSubphases, BatchStatus pBatchStatus, Exception pException, long pJobNumber, String pBatchReasonForFailure, String pComment, final double pCaseDetailsPerSecond, final TBatchResult pBatchResult) {
        super(pPhase, pMaxPhases, pSubphase, pSubphases, pBatchStatus, pException, pJobNumber, pBatchReasonForFailure, pComment);
        caseDetailsPerSecond = pCaseDetailsPerSecond;
        batchResult = pBatchResult;
    }

    //public BatchGroupingDTO() {
    //}
//    public BatchGroupingDTO(final int pGroupedItems, final int pNumberOfAllItems, final double pFlowPerSecond, final String commentMsg, final BatchStatus pBatchStatus, final Exception pException) {
//        this.groupedItems = pGroupedItems;
//        this.numberOfAllItems = pNumberOfAllItems;
//        this.caseDetailsPerSecond = pFlowPerSecond;
//        this.percent = (100.0d * pGroupedItems) / (pNumberOfAllItems == 0 ? 1 : pNumberOfAllItems);
//        //this.percent = percent;
//        this.comment = (commentMsg == null ? "" : commentMsg.trim());
//        this.batchStatus = pBatchStatus == null ? BatchStatus.STARTED : pBatchStatus;
//        this.exception = pException;
//    }
//
//    public int getGroupedItems() {
//        return groupedItems;
//    }
//
//    public int getNumberOfAllItems() {
//        return numberOfAllItems;
//    }
//
//    public String getComment() {
//        return comment;
//    }
    public Double getCaseDetailsPerSecond() {
        return caseDetailsPerSecond;
    }

//    public Double getPercent() {
//        return percent;
//    }
//    @Override
//    public String toString() {
//        return "BatchGroupingDTO{" + "groupedItems=" + groupedItems + ", numberOfAllItems=" + numberOfAllItems + ", percent=" + percent + ", flowPerSecond=" + caseDetailsPerSecond + ", comment=" + comment + '}';
//    }
//
//    /**
//     * @param caseDetailsPerSecond the caseDetailsPerSecond to set
//     */
//    public void setCaseDetailsPerSecond(double caseDetailsPerSecond) {
//        this.caseDetailsPerSecond = caseDetailsPerSecond;
//    }
    /**
     * @return the batchResult
     */
    public TBatchResult getBatchResult() {
        return batchResult;
    }

}
