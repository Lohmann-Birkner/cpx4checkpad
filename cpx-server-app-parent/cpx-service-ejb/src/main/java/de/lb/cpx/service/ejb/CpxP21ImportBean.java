/* 
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.checkpoint.drg.GDRGModel;
//import de.lb.cpx.gdv.gdvimport.GdvImportDocumentProcess;
import de.lb.cpx.grouper.model.transfer.BatchGroupParameter;
import de.lb.cpx.mail.SendMail;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.DetailsFilterEn;
import de.lb.cpx.model.enums.LockingCause;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.auth.CpxJob;
import de.lb.cpx.server.config.CpxServerConfig;
import de.lb.cpx.service.gdvimport.GdvImportDocumentProcessBean;
import de.lb.cpx.service.grouper.GrouperJobStarter;
import de.lb.cpx.service.information.DatabaseInfo;
import de.lb.cpx.service.scheduled_ejb.ImportProcessBillDate;
import de.lb.cpx.shared.dto.LockException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.operations.JobExecutionNotRunningException;
import javax.batch.runtime.BatchStatus;
import javax.ejb.AccessTimeout;
import javax.ejb.Asynchronous;
import javax.ejb.ConcurrencyManagement;
import static javax.ejb.ConcurrencyManagementType.BEAN;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Lock;
import javax.ejb.LockType;
import static javax.ejb.LockType.READ;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.Transactional;
import de.lb.cpx.service.merge.MergeJobStarter;
import de.lb.cpx.shared.dto.job.config.other.GdvImportDocumentJob;
import java.util.Properties;

/**
 *
 * @author wilde
 */
@Stateful
@LocalBean
@ConcurrencyManagement(BEAN) //Bean-Managed Concurrency
@AccessTimeout(value = 300000)
@Lock(READ)
@TransactionManagement(value = TransactionManagementType.BEAN)
public class CpxP21ImportBean implements CpxP21ImportBeanRemote {

    private static final Logger LOG = Logger.getLogger(CpxP21ImportBean.class.getName());

//    @EJB
//    private P21Importer p21Importer;

    @EJB
    private LockServiceBean dBLockService;

    @EJB
    private GrouperJobStarter grouperJob;

    @EJB
    private ImportProcessBillDate billImportJob;
        
    @EJB
    private MergeJobStarter mergeJob;
    
    @EJB
    private GdvImportDocumentProcessBean gdvImportDocumentProcess;

    private long jobId = 0L;

//    @Override
//    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
//    @Lock(LockType.READ)
//    @Asynchronous
//    public Long startImportProcess(String directoryName, int maxImportCount, String checkType, boolean doGroup, String grouperModel, String database)
//            throws FileNotFoundException {
//        long executionId;
//        CpxJob cpxJob = ClientManager.createJobSession(database);
////            String stringCurrentCpxJobId = ClientManager.getCurrentCpxJobId();
//        //executionId = Long.valueOf(cpxJob.getClientId().replace("job", ""));
////            executionId = 
////           jobId = Long.valueOf(stringCurrentCpxJobId.replace("job",""));
//        try {
//            executionId = p21Importer.importP21Data(directoryName, maxImportCount, checkType, doGroup, grouperModel, database);
//        } catch (LockException | FileNotFoundException e) {
//            throw new PessimisticLockException(e);
//        }
//
//        return executionId;
//    }
//    @Override
//    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
//    @Lock(LockType.READ)
//    @Asynchronous    
//    public void restartImportProcess(String directoryName, int maxImportCount, String checkType, boolean doGroup, String grouperModel, String database) {
//        ClientManager.createJobSession(database);
//        try {
//            p21Importer.restartImport(directoryName, maxImportCount, checkType, doGroup, grouperModel);
//        } catch (LockException e) {
//            LOG.log(Level.SEVERE, "DB ist gesperrt", e);
//        }
//    }
//    @Override
//    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
//    public void startBatchGrouping(String admissionDateFrom, String admissionDateUntil, String dischargeDateFrom, String dischargeDateUntil, boolean grouped, boolean extern,
//            boolean doRules, boolean doRulesSimulate, boolean supplementaryFee, boolean do4ActualRoleOnly, boolean medAndRemedies, boolean doSimulate, String grouperModel,
//            String database, String queueSize, String blockSize, String threadCount, boolean disableWriter) throws LockException {
//        ClientManager.createJobSession(database);
//        grouperJob.startGroupingJob(admissionDateFrom, admissionDateUntil, dischargeDateFrom, dischargeDateUntil, grouped, extern,
//                doRules, doRulesSimulate, supplementaryFee, do4ActualRoleOnly, medAndRemedies, doSimulate, grouperModel,
//                queueSize, blockSize, threadCount, disableWriter);
//    }
//
//    @Override
//    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
//    public void startBatchGrouping(String admissionDateFrom, String admissionDateUntil, String dischargeDateFrom, String dischargeDateUntil, boolean grouped, boolean extern,
//            boolean doRules, boolean doRulesSimulate, boolean supplementaryFee, boolean do4ActualRoleOnly, boolean medAndRemedies, boolean doSimulate, String grouperModel,
//            String database) throws LockException {
//        startBatchGrouping(admissionDateFrom, admissionDateUntil, dischargeDateFrom, dischargeDateUntil, grouped, extern,
//                doRules, doRulesSimulate, supplementaryFee, do4ActualRoleOnly, medAndRemedies, doSimulate, grouperModel, database, "", "", "", false);
//    }
    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    @Override
    public Long prepareBatchgroupingWithoutLocking(final String pDatabase) {
        final long executionId = System.currentTimeMillis();

        if (!setDatabase(pDatabase)) {
            throw new IllegalArgumentException("Was not able to use database '" + pDatabase + "'");
        }

//        try {
//            dBLockService.lockDatabase(LockingCause.BATCH_GROUPING);
//        } catch (LockException ex) {
//            LOG.log(Level.SEVERE, "Batchgrouping failed, because some hospital cases or whole database is already locked");
//            throw ex;
//        }
        return executionId;
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    @Override
    public Long prepareBatchgrouping(final String pDatabase) throws LockException {
        return prepareBatchProcess(pDatabase, LockingCause.BATCH_GROUPING);
    }
    
    private Long prepareBatchProcess(final String pDatabase, LockingCause pCause)throws LockException {
        final long executionId = System.currentTimeMillis();

        if (!setDatabase(pDatabase)) {
            throw new IllegalArgumentException("Was not able to use database '" + pDatabase + "'");
        }
//        final long executionId = System.currentTimeMillis();
//
//        if (!setDatabase(pDatabase)) {
//            throw new IllegalArgumentException("Was not able to use database '" + pDatabase + "'");
//        }

        try {
            dBLockService.lockDatabase(pCause);
        } catch (LockException ex) {
            LOG.log(Level.SEVERE, "Batch process failed, because some hospital cases or whole database is already locked");
            throw ex;
        }
        return executionId;
        
    }

    //long mStartTime = System.currentTimeMillis();
    //long mStartTime2 = mStartTime;
    //final long mStartupTime = mStartTime;
    public static boolean setDatabase(final String pDatabase) {
        try {
            CpxJob cpxJob = ClientManager.createJobSession(pDatabase);
        } catch (IllegalStateException ex) {
            LOG.log(Level.SEVERE, "A problem occured when I tried to create a job session!", ex);
            return false;
        }
        return true;
    }

//    @Override
//    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
//    @Lock(LockType.READ)
//    @Asynchronous
//    public void startBatchGrouping(final long pExecutionId, List<Long> pCaseIds, String admissionDateFrom, String admissionDateUntil, String dischargeDateFrom, String dischargeDateUntil, boolean grouped, BatchGroupParameter.DetailsFilterEn pDetailsFilter,
//            boolean doRules, boolean doRulesSimulate, boolean supplementaryFee, boolean do4ActualRoleOnly, boolean medAndRemedies, boolean doSimulate, String grouperModel,
//            String database, String queueSize, String blockSize, String threadCount, boolean disableWriter, List<Long> ruleIds) {
//        //ClientManager.createJobSession(database);
//        grouperJob.startGroupingJob(pExecutionId, pCaseIds, admissionDateFrom, admissionDateUntil, dischargeDateFrom, dischargeDateUntil, grouped, pDetailsFilter,
//                doRules, doRulesSimulate, supplementaryFee, do4ActualRoleOnly, medAndRemedies, doSimulate, grouperModel, queueSize, blockSize, threadCount, disableWriter, ruleIds);
//    }
    @Override
    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
    @Lock(LockType.READ)
    @Asynchronous
    public void startBatchGrouping(final long pExecutionId) {
        // Batchgrouping job must group local and kis cases
        BatchGroupParameter param = new BatchGroupParameter();
//        param.setDetailsFilter(DetailsFilterEn.ACTUAL_BOTH);
        grouperJob.startGroupingJob(pExecutionId, param);
    }

    @Override
    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
    @Lock(LockType.READ)
    @Asynchronous
    public void startBillImport(final long pExecutionId, final String pDirectory, final DatabaseInfo dbInfo) {
        final String database = ClientManager.getActualDatabase();
        LOG.log(Level.INFO, "Bill import job starts with these parameters: database = {0}, directory = {1}", new Object[]{database, pDirectory});
        final CpxServerConfig cpxServerConfig = new CpxServerConfig();
        try(Connection connection = cpxServerConfig.getJdbcConnection(database)) {
            LOG.log(Level.WARNING, "bill import has to be implemented!");
            billImportJob.startBillDateImportJob(pExecutionId, connection, pDirectory, dbInfo);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, MessageFormat.format("was not able to connect database {0}", database), ex);
        }
    }

    @Override
    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
    @Lock(LockType.READ)
    @Asynchronous
    public void startBatchGrouping(final long pExecutionId, final BatchGroupParameter pParameter) {
        //ClientManager.createJobSession(database);
        grouperJob.startGroupingJob(pExecutionId, pParameter);
    }

//    @Override
//    @Lock(LockType.READ)
//    public Long restartBatchGrouping(Long executionId, String admissionDateFrom, String admissionDateUntil, String dischargeDateFrom, String dischargeDateUntil, boolean grouped, boolean extern,
//            boolean doRules, boolean doRulesSimulate, boolean supplementaryFee, boolean do4ActualRoleOnly, boolean medAndRemedies, boolean doSimulate, String grouperModel,
//            String database) {
//        return grouperJob.restartGroupingJob(executionId, admissionDateFrom, admissionDateUntil, dischargeDateFrom, dischargeDateUntil, grouped, extern, doRules, doRulesSimulate, supplementaryFee, do4ActualRoleOnly, medAndRemedies, doSimulate, grouperModel);
//    }
    @Override
    @Lock(LockType.READ)
    public void stopBatchGrouping(Long executionId) {
        LOG.log(Level.INFO, "stop method called");
        try {
            grouperJob.stopGroupingJob(executionId);
        } catch (JobExecutionNotRunningException ex) {
            LOG.log(Level.WARNING, "Was not able to stop grouper job with this execution id: " + executionId, ex);
        }
    }

    @Override
    @Lock(LockType.READ)
    public BatchStatus getBatchStatus(Long executionId) {
        return grouperJob.getBatchStatus(executionId);
    }

    @Override
    @Lock(LockType.READ)
    public int getCaseDetailsCount(final BatchGroupParameter pParameter) {
        return grouperJob.getCaseDetailsCount(pParameter);
    }

//    @Override
//    public void startImportProcess(Properties props) {
//        LOG.info("Starting Job " + props.getProperty("jobNumber") + " ...");
//        String database = props.getProperty("database");
//        ClientManager.createJobSession(database);
//        try {
//            p21Importer.importP21Data(props);
//        } catch (LockException ex) {
//            Logger.getLogger(CpxP21ImportBean.class.getName()).log(Level.SEVERE, "DB ist gesperrt", ex);
//        }
//    }
    public void setJobId(final long pJobId) {
        jobId = pJobId;
    }

    public long getJobId() {
        return jobId;
    }

//    public void unlockDatabase() {
//        if (dBLockService != null) {
//            dBLockService.unlockDatabase();
//        }
//    }
    @Override
    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
    @Lock(LockType.READ)
    @Asynchronous
    public void startBatchMerging(Long pExecutionId, CaseTypeEn pGrpresType, GDRGModel grouperModel) {
        
        mergeJob.startMergingJob(pExecutionId, pGrpresType, grouperModel);
    }

    @Override
    public Long prepareBatchMerging(String pDatabase) throws LockException {
        return prepareBatchProcess(pDatabase, LockingCause.BATCH_MERGING);
    }

    @Override
    @Lock(LockType.READ)
    public BatchStatus getBatchMergeStatus(Long executionId) {
        return mergeJob.getBatchStatus(executionId);
    }

    @Override
    @Lock(LockType.READ)
    public void stopBatchMerging(Long executionId) {
       LOG.log(Level.INFO, "stop method called");
        try {
            mergeJob.stopMergingJob(executionId);
        } catch (JobExecutionNotRunningException ex) {
            LOG.log(Level.WARNING, "Was not able to stop merging job with this execution id: " + executionId, ex);
        }
    }
    @Override
    @Lock(LockType.READ)
    public void startGdvDocumentImport(Long pExecutionId, GdvImportDocumentJob pGdvImportDocumentJob) {
        final String database = ClientManager.getActualDatabase();
        LOG.log(Level.INFO, "gdv import job starts with these parameters: database = {0},  {1}", new Object[]{database, pGdvImportDocumentJob.toString()});
        final CpxServerConfig cpxServerConfig = new CpxServerConfig();
        try(Connection connection = cpxServerConfig.getJdbcConnection(database)) {
//            GdvImportDocumentProcess gdvImportDocumentProcess = new GdvImportDocumentProcess(connection);
            Properties props = System.getProperties();
            if(pGdvImportDocumentJob.getEmailHost() != null && pGdvImportDocumentJob.getEmailHost().trim().length()> 0 ){
                System.setProperty(SendMail.MAIL_HOST_PROPERTY_KEY, pGdvImportDocumentJob.getEmailHost());
            }
            if(pGdvImportDocumentJob.getEmailPort()!= null && pGdvImportDocumentJob.getEmailPort().trim().length()> 0 ){
                System.setProperty(SendMail.MAIL_PORT_PROPERTY_KEY, pGdvImportDocumentJob.getEmailPort());
            }
            if(pGdvImportDocumentJob.getEmailDebug()!= null && pGdvImportDocumentJob.getEmailDebug().trim().length()> 0 ){
                System.setProperty(SendMail.MAIL_DEBUG_PROPERTY_KEY, pGdvImportDocumentJob.getEmailDebug());
            }else{
                System.setProperty("mail.debug", "false");
            }
            
            gdvImportDocumentProcess.startGdvImportDocument(
                pGdvImportDocumentJob.getGdvDirectory(), 
                pGdvImportDocumentJob.getTargetDirectory(),
                pGdvImportDocumentJob.getArchivDirectory(),
                pGdvImportDocumentJob.getEmailFrom(),
                pGdvImportDocumentJob.getEmailPassword(),
                pGdvImportDocumentJob.getEmailTo());
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, MessageFormat.format("was not able to connect database {0}", database), ex);
        }
    }


}
