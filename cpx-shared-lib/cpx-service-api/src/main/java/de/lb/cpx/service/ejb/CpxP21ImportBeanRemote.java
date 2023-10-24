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
 *    2016  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.grouper.model.transfer.BatchGroupParameter;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.service.information.DatabaseInfo;
import de.lb.cpx.shared.dto.LockException;
import de.lb.cpx.shared.dto.job.config.other.GdvImportDocumentJob;
import javax.batch.runtime.BatchStatus;
import javax.ejb.Remote;

/**
 *
 * @author Dirk Niemeier
 */
@Remote
public interface CpxP21ImportBeanRemote {

//    Long startImportProcess(String string, int i, String checkType, boolean doGroup, String grouperModel, String database)
//            throws FileNotFoundException;
//    void startImportProcess(Properties props) throws CpxIllegalArgumentException;
//    void restartImportProcess(String directoryName, int maxImportCount, String checkType, boolean doGroup, String grouperModel, String database) throws CpxIllegalArgumentException;
//    void startBatchGrouping(String admissionDateFrom, String admissionDateUntil, String dischargeDateFrom, String dischargeDateUntil, boolean grouped, boolean extern,
//            boolean doRules, boolean doRulesSimulate, boolean supplementaryFee, boolean do4ActualRoleOnly, boolean medAndRemedies, boolean doSimulate, String grouperModel, String database, String queueSize, String blockSize, String threadCount, boolean disableWriter) throws LockException;
//
//    void startBatchGrouping(String admissionDateFrom, String admissionDateUntil, String dischargeDateFrom, String dischargeDateUntil, boolean grouped, boolean extern,
//            boolean doRules, boolean doRulesSimulate, boolean supplementaryFee, boolean do4ActualRoleOnly, boolean medAndRemedies, boolean doSimulate, String grouperModel, String database) throws LockException;
    /**
     * prepares batch grouping
     *
     * @param pDatabase database
     * @return restarted executionId of the job
     */
    public Long prepareBatchgroupingWithoutLocking(final String pDatabase);

    /**
     * prepares batch grouping
     *
     * @param pDatabase database
     * @return restarted executionId of the job
     * @throws de.lb.cpx.shared.dto.LockException db is locked
     */
    public Long prepareBatchgrouping(final String pDatabase) throws LockException;

//    /**
//     * start Job with ExecutionId
//     *
//     * @param pExecutionId job id
//     * @param pCaseIds list of hospital case ids to be grouped
//     * @param admissionDateFrom admission date - optional
//     * @param admissionDateUntil admission date - optional
//     * @param dischargeDateFrom discharge date - optional
//     * @param dischargeDateUntil discharge date - optional
//     * @param grouped should group Cases without DRG/PEPP
//     * @param pDetailsFilter should group HIS Cases
//     * @param doRules group with automatic case check
//     * @param doRulesSimulate group with automatic case correction
//     * @param supplementaryFee distribute fees
//     * @param do4ActualRoleOnly only use specified role
//     * @param medAndRemedies do gk check
//     * @param doSimulate do case simulation
//     * @param grouperModel groupermodel to use while grouping, WARNING some old
//     * Groupermodels are broken and cause exceptions
//     * @param database database for grouping
//     * @param queueSize queue size
//     * @param blockSize block size
//     * @param threadCount thread count
//     * @param disableWriter disable writer
//     * @param ruleIds rule ids
//     */
//    void startBatchGrouping(final long pExecutionId, List<Long> pCaseIds, String admissionDateFrom, String admissionDateUntil, String dischargeDateFrom, String dischargeDateUntil, boolean grouped, BatchGroupParameter.DetailsFilterEn pDetailsFilter,
//            boolean doRules, boolean doRulesSimulate, boolean supplementaryFee, boolean do4ActualRoleOnly, boolean medAndRemedies, boolean doSimulate, String grouperModel, String database,
//            String queueSize, String blockSize, String threadCount, boolean disableWriter, List<Long> ruleIds);
    /**
     * start Job with ExecutionId and default grouper settings
     *
     * @param pExecutionId job id
     */
    void startBatchGrouping(final long pExecutionId);
    
    void startBillImport(final long pExecutionId, final String pDirectory, final DatabaseInfo dbInfo);

    /**
     * start Job with ExecutionId
     *
     * @param pExecutionId job id
     * @param pParameter batch grouping parameters
     */
    void startBatchGrouping(final long pExecutionId, final BatchGroupParameter pParameter);

//    /**
//     * restart Job with ExecutionId
//     *
//     * @param executionId execution id of the previouse job
//     * @param admissionDateFrom admission date - optional
//     * @param admissionDateUntil admission date - optional
//     * @param dischargeDateFrom discharge date - optional
//     * @param dischargeDateUntil discharge date - optional
//     * @param grouped should group Cases without DRG/PEPP
//     * @param extern should group HIS Cases
//     * @param doRules group with automatic case check
//     * @param doRulesSimulate group with automatic case correction
//     * @param supplementaryFee distribute fees
//     * @param do4ActualRoleOnly only use specified role
//     * @param medAndRemedies do gk check
//     * @param doSimulate do case simulation
//     * @param grouperModel groupermodel to use while grouping, WARNING some old
//     * Groupermodels are broken and cause exceptions
//     * @param database database for grouping
//     * @return restarted executionId of the job
//     * @throws de.lb.cpx.shared.dto.LockException db is locked
//     */
//    Long restartBatchGrouping(Long executionId, String admissionDateFrom, String admissionDateUntil, String dischargeDateFrom, String dischargeDateUntil, boolean grouped, boolean extern,
//            boolean doRules, boolean doRulesSimulate, boolean supplementaryFee, boolean do4ActualRoleOnly, boolean medAndRemedies, boolean doSimulate, String grouperModel,
//            String database) throws LockException;
    /**
     * Stop job with the executionId
     *
     * @param executionId id of job to Stop
     */
    void stopBatchGrouping(Long executionId);

    /**
     * get the current BatchStatus for executionId
     *
     * @param executionId job execution Id
     * @return BatchStatus Object
     */
    BatchStatus getBatchStatus(Long executionId);

    int getCaseDetailsCount(BatchGroupParameter pParameter);
    
    void startBatchMerging(Long pExcecutionId, CaseTypeEn pGrpresType, GDRGModel grouperModel);
    
    public Long prepareBatchMerging(final String pDatabase) throws LockException;

    BatchStatus getBatchMergeStatus(Long executionId);

    void stopBatchMerging(Long executionId);
    
    void startGdvDocumentImport(Long pExecutionId, GdvImportDocumentJob pGdvImportDocumentJob);
}
