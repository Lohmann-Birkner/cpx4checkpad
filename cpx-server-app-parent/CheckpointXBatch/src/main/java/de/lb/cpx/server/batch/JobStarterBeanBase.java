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
 *    2016  wilde
 */
package de.lb.cpx.server.batch;

import de.lb.cpx.service.ejb.LockServiceBean;
import java.util.Properties;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.operations.JobExecutionNotRunningException;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.inject.Inject;

/**
 * base job starter bean to provide basic methodes
 *
 * @author wilde
 */
public class JobStarterBeanBase {

    private static final Logger LOG = Logger.getLogger(JobStarterBeanBase.class.getName());
    public static final String FAIL_REASON = "exception";
    
    @Inject
    private LockServiceBean dBLockService;

    /**
     * @param pJobXMLName file name of the job xml file in the resources
     * @param pProperties batch properties required to run the job
     * @return execution id of the newly created job
     */
    public long start(String pJobXMLName, Properties pProperties) {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        return jobOperator.start(pJobXMLName, pProperties);
    }

    /**
     * @param pOldExecId job id of previously stoped job
     * @param pProperties batch properties required to run the job
     * @return new job id of the running job
     */
    public long restart(long pOldExecId, Properties pProperties) {
        return BatchRuntime.getJobOperator().restart(pOldExecId, pProperties);
    }

    /**
     * @param pOldExecId job id of previously stoped job
     * @return new job id of the running job
     */
    public long restart(long pOldExecId) {
        return restart(pOldExecId, getParameters(pOldExecId));
    }

    /**
     * @param pExecId id of job to stop WARNING: stops async job might run an
     * undefined amount of time till jobOperator is able to stop it
     */
    public void stop(long pExecId) {
        BatchRuntime.getJobOperator().stop(pExecId);
    }

    public Properties getParameters(long pExecId) {
        return BatchRuntime.getJobOperator().getParameters(pExecId);
    }

    /**
     * try to stop Job in an Sync way. Methode will block until job Status is
     * set to STOPPED
     *
     * @param pExecId execution i of job to stop
     * @return future with stopping result if true job is successfully stopped
     */
    public Future<Boolean> stopSync(long pExecId) {
        final JobOperator jobOperator = BatchRuntime.getJobOperator();
        try {
            jobOperator.stop(pExecId);
        } catch (JobExecutionNotRunningException ex) {
            LOG.log(Level.WARNING, "Job already stopped", ex);
            return null;
        }
        //Awi-20170808-CPX-528:
        //unlock db, should be called after job is successfully stopped?
//            dBLockService.unlockDB();
        final ExecutorService executor = Executors.newFixedThreadPool(1);
        CompletionService<Boolean> completion = new ExecutorCompletionService<>(executor);

        completion.submit(new Runnable() {
            private boolean running = true;

            @Override
            public void run() {
                while (running) {
                    if (jobOperator.getJobExecution(pExecId).getBatchStatus().equals(BatchStatus.STOPPED) || jobOperator.getJobExecution(pExecId).getBatchStatus().equals(BatchStatus.FAILED)) {
                        running = false;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        LOG.log(Level.WARNING, null, ex);
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }, true);
        executor.shutdownNow();
        try {
            Future<Boolean> result = completion.take();
            if (result.get()) {
                dBLockService.unlockDatabase();
                LOG.info("abandon task");
                jobOperator.abandon(pExecId);
                return result;
            }
        } catch (ExecutionException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt();
        }
        return null;
    }

    /**
     * get the current BatchStatus of the job with executionId
     *
     * @param executionId id of the currentJob
     * @return BatchStatus Object
     */
    public BatchStatus getBatchStatus(Long executionId) {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        return jobOperator.getJobExecution(executionId).getBatchStatus();
    }
    
    public Exception getException(Long executionId){
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        return (Exception) jobOperator.getJobExecution(executionId).getJobParameters().get(FAIL_REASON);
    }

}
