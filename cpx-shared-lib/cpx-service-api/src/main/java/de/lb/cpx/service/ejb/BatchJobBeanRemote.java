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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.model.enums.CaseTypeEn;
import javax.batch.runtime.BatchStatus;
import javax.ejb.Remote;

/**
 * inteface to interact with jobs
 *
 * @author wilde
 */
@Remote
public interface BatchJobBeanRemote {

    /**
     * start merge job
     *
     * @param pModel grouper model
     * @param pGrpresType grpres type, drg/pepp/etc
     * @param pDatabase selected database
     * @return started job id
     */
    Long startMergeJob(GDRGModel pModel, CaseTypeEn pGrpresType, String pDatabase);

    /**
     * @param pExecutionId stop job with that id
     */
    void stopMergeJob(long pExecutionId);

    /**
     * @param pOldExecutionId restart job with old id
     * @return new job id
     */
    Long restartMergeJob(Long pOldExecutionId);

    /**
     * @param pExecId execution id of a job
     * @return job status of the job with the exection id
     */
    BatchStatus getBatchStatus(long pExecId);

    Exception getException(long pExecId);

}
