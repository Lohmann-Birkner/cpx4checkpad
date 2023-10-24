/*
 * Copyright (c) 2021 Lohmann & Birkner.
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
 *    2021  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.dto;
import javax.batch.runtime.BatchStatus;
import de.lb.cpx.model.TCaseMergeMapping;
import java.util.List;

/**
 *
 * @author gerschmann
 */
public class BatchMergingDTO extends MessageDTO{
    private final double caseDetailsPerSecond; 
    private final List<TCaseMergeMapping> resultList;

    public BatchMergingDTO() {
        this(0, 0, 0, 0, BatchStatus.ABANDONED, null, 0L, null, null, 0.0D, null);
    }

    public BatchMergingDTO(int pPhase, int pMaxPhases, int pSubphase, int pSubphases, BatchStatus pBatchStatus, Exception pException, long pJobNumber, String pBatchReasonForFailure, String pComment, final double pCaseDetailsPerSecond) {
        super(pPhase, pMaxPhases, pSubphase, pSubphases, pBatchStatus, pException, pJobNumber, pBatchReasonForFailure, pComment);
        caseDetailsPerSecond = pCaseDetailsPerSecond;
        resultList = null;
    }

    public BatchMergingDTO(int pPhase, int pMaxPhases, int pSubphase, int pSubphases, BatchStatus pBatchStatus, Exception pException, long pJobNumber, String pBatchReasonForFailure, String pComment, final double pCaseDetailsPerSecond, 
            List<TCaseMergeMapping> pResultList) {
        super(pPhase, pMaxPhases, pSubphase, pSubphases, pBatchStatus, pException, pJobNumber, pBatchReasonForFailure, pComment);
        caseDetailsPerSecond = pCaseDetailsPerSecond;
        resultList = pResultList;
    }

    public BatchMergingDTO(int phase, int pMaxPhases, int subphase, int subphases, BatchStatus batchStatus, Exception ex, long jobId, String reason, String pComment) {
        super(phase, pMaxPhases, subphase, subphases, batchStatus, ex,jobId, reason, pComment);
        caseDetailsPerSecond = 0;
        resultList = null;
    }

    
    public Double getCaseDetailsPerSecond() {
        return caseDetailsPerSecond;
    }
    
    public List<TCaseMergeMapping> getResultList(){
        return resultList;
    }
}
