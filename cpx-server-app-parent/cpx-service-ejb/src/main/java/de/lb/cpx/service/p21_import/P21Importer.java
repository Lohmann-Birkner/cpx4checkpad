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
 *    2016  Husser - Bean for starting and restarting the P21-Import. If more than maxImportCount cases are in the fall.csv, then the import is splitted in separate job executions.
 */
package de.lb.cpx.service.p21_import;

import de.lb.cpx.model.enums.LockingCause;
import de.lb.cpx.service.ejb.LockServiceBean;
import de.lb.cpx.shared.dto.LockException;
import de.lb.cpx.shared.p21util.P21Util;
import de.lb.cpx.system.properties.CpxSystemProperties;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.operations.JobExecutionNotMostRecentException;
import javax.batch.operations.JobOperator;
import javax.batch.operations.NoSuchJobException;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.JobInstance;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.PessimisticLockException;
import javax.transaction.Transactional;

/**
 *
 * @author Husser
 */
@Stateless
public class P21Importer {

    private static final Logger LOG = Logger.getLogger(P21Importer.class.getSimpleName());
    private long executionId = 0L;

    @EJB
    private LockServiceBean dBLockService;

//    @EJB
//    ManipulateConstraints manipulateConstraints;
    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
    public long importP21Data(String directoryName, int maxImportCount, String checkType, boolean doGroup, String grouperModel, String database) throws FileNotFoundException, LockException {

        try {
            dBLockService.lockDatabase(LockingCause.IMPORT);
//            dBLockService.unlockDB();
        } catch (PessimisticLockException e) {
            throw new PessimisticLockException(e);
        }
//        manipulateConstraints.deactivateConstraints4Import();
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Properties props = new Properties();
        props.setProperty("p21ImportDirectory", "C:\\p21-import\\" + directoryName);
        props.setProperty("numberOfItems", String.valueOf(maxImportCount));
        props.setProperty("checkType", checkType);
        props.setProperty("doGroup", String.valueOf(doGroup));
        props.setProperty("grouperModel", grouperModel);
        props.setProperty("database", database);

        int numberOfJobs = getNumberOfJobs(directoryName, maxImportCount, props);
        props.setProperty("numberOfJobs", String.valueOf(numberOfJobs));
        props.setProperty("jobNumber", String.valueOf(0));
        executionId = jobOperator.start("p21ImportJob", props);

        LOG.info("Starting Job...");

        return executionId;
    }

    @Asynchronous
    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
    public void importP21Data(Properties properties) throws LockException {

        dBLockService.lockDatabase(LockingCause.IMPORT);

        JobOperator jobOperator = BatchRuntime.getJobOperator();
        jobOperator.start("p21ImportJob", properties);
        LOG.info("Starting Job...");
    }

    private int getNumberOfJobs(String directoryName, int maxImportCount, Properties props) throws FileNotFoundException {
        String filename = "C:\\p21-import\\" + directoryName + "\\" + P21Util.FILENAME_P21FALL;
        int counter = -1;
        //try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
        try ( BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), CpxSystemProperties.DEFAULT_ENCODING))) {
            String currentLine = bufferedReader.readLine();
            while (currentLine != null) {
                counter++;
                currentLine = bufferedReader.readLine();
            }
        } catch (IOException e) {
            LOG.log(Level.INFO, "End of file reached", e);
        }
        props.setProperty("numberOfAllItems", String.valueOf(counter));
        int numberOfJobs = counter / maxImportCount;
        if ((counter % maxImportCount) > 0) {
            numberOfJobs++;
        }
        return numberOfJobs;
    }

    @Asynchronous
    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
    public void restartImport(String directoryName, int maxImportCount, String checkType, boolean doGroup, String grouperModel) throws LockException {

        dBLockService.lockDatabase(LockingCause.IMPORT);

        boolean restarted = false;
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Properties props = new Properties();
        props.setProperty("p21ImportDirectory", "C:\\p21-import\\" + directoryName);
        props.setProperty("numberOfItems", String.valueOf(maxImportCount));
        props.setProperty("checkType", checkType);
        props.setProperty("doGroup", String.valueOf(doGroup));
        props.setProperty("grouperModel", grouperModel);

        try {
            List<JobInstance> jobInstances = jobOperator.getJobInstances("p21ImportJob", 0, 1);
            if (jobInstances != null && !jobInstances.isEmpty()) {

                JobInstance jobInstance = jobInstances.get(0);

                List<JobExecution> jobExecutions = jobOperator.getJobExecutions(jobInstance);
                for (JobExecution jobExecution : jobExecutions) {
                    long execId = jobExecution.getExecutionId();
                    if (jobExecution.getBatchStatus() == BatchStatus.FAILED || jobExecution.getBatchStatus() == BatchStatus.STOPPED) {
                        try {
                            jobOperator.restart(execId, props);
                        } catch (JobExecutionNotMostRecentException e) {
                            LOG.log(Level.INFO, "Restart failed for execution id: " + execId, e);
                            continue;
                        }
                        LOG.info("Restarting Job...");
                        restarted = true;
                        break;
                    }
                }
            }
        } catch (NoSuchJobException e) {
            LOG.log(Level.INFO, "Job not found.", e);
        }
        if (!restarted) {
            LOG.log(Level.INFO, "No restart performed");
        }
    }

}
