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
package de.lb.cpx.server.batch.p21import2;

import de.checkpoint.drg.GDRGModel;
import de.checkpoint.enums.AppTypeEn;
import de.lb.cpx.model.enums.LockingCause;
import de.lb.cpx.server.auth.CaseEntityManagerFactoryCreator;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.auth.CpxJob;
import de.lb.cpx.server.commonDB.dao.CdbUsersDao;
import de.lb.cpx.server.dao.TPatientDao;
import de.lb.cpx.service.ejb.CpxP21ImportBean;
import de.lb.cpx.service.ejb.LockServiceBean;
import de.lb.cpx.service.ejb.LoginServiceEJB;
import de.lb.cpx.service.information.CpxPersistenceUnit;
import de.lb.cpx.service.startup_ejb.LicenseReadBean;
import de.lb.cpx.shared.dto.LockException;
import de.lb.cpx.shared.dto.MessageDTO;
import de.lb.cpx.shared.dto.job.config.CpxFileBasedImportJob;
import de.lb.cpx.shared.dto.job.config.CpxJobConstraints;
import de.lb.cpx.shared.dto.job.config.ImportMode;
import de.lb.cpx.shared.dto.job.config.file.FdseJob;
import de.lb.cpx.shared.dto.job.config.file.P21Job;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.batch.runtime.BatchStatus;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.PessimisticLockException;
import javax.security.auth.login.LoginException;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import module.AbstractImportFileModule;
import module.Fdse;
import module.P21;
import module.impl.ImportConfig;
import org.hibernate.jdbc.Work;
import org.jboss.ejb3.annotation.TransactionTimeout;
import process.FailureCallbackI;
import process.ProgressCallbackI;
import process.SuccessCallbackI;
import process.impl.ImportProcessFile;

/**
 *
 * @author Dirk Niemeier
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class P21Importer2 {

    private static final Logger LOG = Logger.getLogger(P21Importer2.class.getName());
    //private TransformResult mTransformResult = null;

//    @EJB
//    private TCaseDao hospitalCaseDao;
//    @EJB
//    private GrouperCommunication grouperCommunication;
//    @EJB
//    private GrouperService grouperService;
    //@Resource
    //private UserTransaction userTransaction;
    //final public int CHUNK_SIZE = 10000;
    //final public int MAX_THREADS = 12;
    //final public static boolean REMOVE_CONSTRAINTS = true; //Set true to gain much more better performance!
    //JsonReader mReaderCases;
    //JsonReader mReaderPatients;
    //private Gson mGson;
    //private long mExecutionId = -1;
    //private ProgressCallbackI mProgressCallback = new ProgressCallback();
    //private FailureCallbackI mFailureCallback = new FailureCallback();
//    @Resource(name = "java:comp/DefaultManagedThreadFactory")
//    private ManagedThreadFactory tf;
    @Inject
    private JMSContext jmsContext;

    @Resource(lookup = "java:jboss/exported/jms/queue/CpxBatchImportStatusQueue")
    private Destination destination;

    private int messagesSent = 0;

    @Inject
    private TPatientDao patientDao;

    @Inject
    private CdbUsersDao usersDao;

    //@Inject
    //TCaseDao caseDao;
    @EJB
    private LockServiceBean dBLockService;

    @Inject
    private CpxP21ImportBean importBean;

    @Inject
    private LoginServiceEJB loginService;

//    @Inject
//    private GrouperJobStarter2 grouperJobStarter;
    @EJB
    private LicenseReadBean licenseBean;

    public boolean loginForExternalCall() {
        return loginService.loginForExternalCall();
    }

    public Long prepareBatchgrouping(final String pDatabase) throws LockException {
        return importBean.prepareBatchgrouping(pDatabase);
    }

    public void startBatchgrouping(final Long pExecutionId) {
        importBean.startBatchGrouping(pExecutionId);
    }

//    public void startBillImport(final Long pExecutionId, final String pDirectory) {
////        importBean.startBillImport(pExecutionId, pDirectory);
//    }

//    public long getExecutionId() {
//        return mExecutionId;
//    }
    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public JsonObject prepareImport(final String pDirectoryName, final String pDatabase, final String pCheckType, final Integer pThreadCount, final boolean pDoGroup, final String pGrouperModel) {
        //mExecutionId = -1;
        long executionId = -1;
        if (pDirectoryName != null) {
            String dir = pDirectoryName.trim().toLowerCase();
            String id = dir.substring(dir.lastIndexOf('_') + 1).trim();
            if (!id.isEmpty()) {
                executionId = Long.valueOf(id);
                LOG.log(Level.FINEST, "P21Importer2:prepareImport:if-pDirectoryName: ExecutionID: {0}", executionId);
            }
        }

        //mStartTime = System.currentTimeMillis();
        if (!setDatabase(pDatabase)) {
            throw new IllegalArgumentException("Was not able to use database '" + pDatabase + "'");
        }

        try {
            dBLockService.lockDatabase(LockingCause.IMPORT);
            //dBLockService.unlockDB();
        } catch (LockException | PessimisticLockException ex) {
            LOG.log(Level.SEVERE, "Cannot start import, (Pessimistic)LockException happened on database " + pDatabase, ex);
            //final FailureCallbackI failureCallback = new FailureCallback(executionId);
            String message = "Die Datenbank " + pDatabase + " ist gesperrt";
            return Json.createObjectBuilder()
                    .add("executionId", executionId)
                    .add("status", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .add("message", message + ": " + ex.getMessage())
                    .build();
            //throw new WebApplicationException(message, ex, Response.Status.INTERNAL_SERVER_ERROR);
//            if (ex instanceof LockException) {
//                final LockException lockEx = (LockException) ex;
//                message = "Die Datenbank " + pDatabase + " ist gesperrt: " + lockEx.getMessage();
//            }
            //failureCallback.execute(message, ex);
//            return null;
        } catch (Exception ex) {
            Throwable e = ex;
            while (e.getCause() != null) {
                e = e.getCause();
            }
            LOG.log(Level.SEVERE, "Cannot start import, error happened on database " + pDatabase, e);
//            final FailureCallbackI failureCallback = new FailureCallback(executionId);
            String message = "Bei der Datenbank " + pDatabase + " ist ein Fehler aufgetreten";
//            if (ex instanceof LockException) {
//                final LockException lockEx = (LockException) ex;
//                message = "Die Datenbank " + pDatabase + " ist gesperrt: " + lockEx.getMessage();
//            }
//            failureCallback.execute(message, (Exception) e);
            //throw new WebApplicationException(message, ex, Response.Status.INTERNAL_SERVER_ERROR);
            return Json.createObjectBuilder()
                    .add("executionId", executionId)
                    .add("status", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .add("message", message + ": " + ex.getMessage())
                    .build();
//            return null;
        }

        //try {
        //  startImportProcess(mExecutionId, pDirectoryName, pDatabase, pThreadCount);
        //} finally {
        //  if (dBLockService != null) {
        //    dBLockService.unlockDB();
        //  }
        //}
        //System.out.println("END");
        LOG.log(Level.FINEST, "P21Importer2:prepareImport:vor return: ExecutionID: {0}", executionId);
        //return executionId;
        return Json.createObjectBuilder()
                .add("executionId", executionId)
                .add("status", Response.Status.OK.getStatusCode())
                .add("message", "OK.")
                .build();
    }

    public void unlockDatabase() {
        if (dBLockService != null) {
            dBLockService.unlockDatabase();
        }
    }

    @TransactionTimeout(value = 360, unit = TimeUnit.MINUTES)
    @Asynchronous
    public void startImportProcess(final long pExecutionId, final String pDirectoryName,
            final String pDatabase, final String pCheckType, final Integer pThreadCount,
            final boolean pDoGroup, final String pGrouperModel, final String pModule) {
        //final String inputDirectory = "C:\\p21-import_\\inek-small\\";
        LOG.log(Level.FINEST, "P21Importer2:startImportProcess:parameter: ExecutionID: {0}", pExecutionId);
        final String inputDirectory = "C:\\p21-import\\" + pDirectoryName;
        final String outputDirectory = "C:\\p21-import\\cpx_transformation\\";
        //final boolean isOracle = patientDao.isOracle();
        final String databaseIdentifier = CaseEntityManagerFactoryCreator.getDbIdentifier(pDatabase);
        final AbstractImportFileModule<?> module;
        if ("p21".equalsIgnoreCase(pModule)) {
            final P21Job p21Job = new P21Job(
                    inputDirectory,
                    ImportMode.getImportMode(pCheckType),
                    true,
                    new CpxJobConstraints(/* put your parameters here! */)
            );
            final P21 p21Module = new P21(p21Job, outputDirectory);
            module = p21Module;
        } else if ("fdse".equalsIgnoreCase(pModule)) {
            final FdseJob fdseJob = new FdseJob(
                    inputDirectory,
                    ImportMode.getImportMode(pCheckType),
                    true,
                    new CpxJobConstraints(/* put your parameters here! */)
            );
            final Fdse fdseModule = new Fdse(fdseJob, outputDirectory);
            module = fdseModule;
        } else {
            LOG.log(Level.WARNING, "unknown import module: {0}", pModule);
            return;
        }
//        module.getInputConfig().setTargetDatabase(databaseIdentifier); //databaseIdentifier
//        module.getInputConfig().setImportMode(ImportMode.getImportMode(pCheckType));
//        module.getInputConfig().setConstraints(new CpxJobConstraints(/* put your parameters here! */));
        final ImportConfig<AbstractImportFileModule<? extends CpxFileBasedImportJob>> importConfig = new ImportConfig<>(
                databaseIdentifier,
                module,
                licenseBean.getLicense()
        );
        ProgressCallbackI progressCallback = new ProgressCallback(pExecutionId);
        SuccessCallbackI successCallback = new SuccessCallback(pExecutionId);
        FailureCallbackI failureCallback = new FailureCallback(pExecutionId);

//        final AtomicBoolean failed = new AtomicBoolean(false);
        usersDao.getSession().doWork(new Work() {
            @Override
            public void execute(final Connection commonDbConnection) throws SQLException {
                patientDao.getSession().doWork(new Work() {
                    @Override
                    public void execute(final Connection caseDbConnection) throws SQLException {
                        ImportProcessFile<AbstractImportFileModule<? extends CpxFileBasedImportJob>> importProcess = new ImportProcessFile<>();
                        importProcess.setCommonDbConnection(commonDbConnection);
                        importProcess.setCaseDbConnection(caseDbConnection);
                        importProcess.getProgressor().setProgressCallback(progressCallback);
                        importProcess.getProgressor().setSuccessCallback(successCallback);
                        importProcess.getProgressor().setFailureCallback(failureCallback);
//                        try {
                        importProcess.startImportProcess(importConfig);
//                        } catch (IOException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException | IllegalStateException | InstantiationException | InterruptedException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException | NoSuchAlgorithmException | SQLException | ParseException ex) {
//                            failed.set(true);
//                            failureCallback.execute("An error occured during import\r\n" + ex.getMessage(), ex);
//                            LOG.log(Level.SEVERE, null, ex);
//                            if (ex instanceof InterruptedException) {
//                                Thread.currentThread().interrupt();
//                            }
//                        }
                    }
                });
            }
        });

//        if (pDoGroup) {
//            groupNewCases(pGrouperModel, pDatabase);
//        }
//        if (!failed.get()) {
//            //progressCallback.execute(Progressor.MAX_PHASES, Progressor.MAX_PHASES, "Import beendet");
//            successCallback.execute("Import beendet");
//        }
    }

//    @TransactionTimeout(value = 360, unit = TimeUnit.MINUTES)
//    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
//    public void groupNewCases(final String pGrouperModel, final String pDatabase) {
//        LOG.log(Level.INFO, "doGroup = true and grouperModel = '" + pGrouperModel + "', will obtain list of imported case ids...");
//        final GDRGModel grouperModel = getGrouperModel(pGrouperModel);
//        if (grouperModel == null) {
//            LOG.log(Level.SEVERE, "There is no grouperModel named '" + pGrouperModel + "'!");
//            //Exit here?!
//        }
//
//        final List<Long> caseIds = new ArrayList<>();
//        patientDao.getSession().doWork(new Work() {
//            @Override
//            public void execute(final Connection connection) throws SQLException {
//                try (final Statement stmt = connection.createStatement()) {
//                    final String query = "SELECT NEW_CASE_ID FROM IMEX_CASE WHERE NEW_CASE_ID IS NOT NULL"; // AND NEW_VERSION = 1 <- Is it senseful to group cases that have not changed?
//                    try (ResultSet rs = stmt.executeQuery(query)) {
//                        while (rs.next()) {
//                            final long caseId = rs.getLong("NEW_CASE_ID");
//                            if (caseId == 0L) {
//                                continue;
//                            }
//                            caseIds.add(caseId);
//                        }
//                    }
//                }
//            }
//        });
//
//        LOG.log(Level.INFO, "Will group " + caseIds.size() + " imported hospital cases now...");
//
//        final String admissionDateFrom = "";
//        final String admissionDateUntil = "";
//        final String dischargeDateFrom = "";
//        final String dischargeDateUntil = "";
//        final boolean grouped = false;
//        final boolean extern = true;
//        final boolean doRules = true;
//        final boolean doRulesSimulate = true;
//        final boolean supplementaryFee = true;
//        final boolean do4ActualRoleOnly = false;
//        final boolean medAndRemedies = true;
//        final boolean doSimulate = true;
//        //final String grouperModel = GDRGModel.AUTOMATIC.name();
//        //final String database = pDatabase;
//        final String queueSize = "1500";
//        final String blockSize = "3";
//        final String threadCount = "AUTO";
//        final boolean disableWriter = false;
////        try {
////            long jobId = grouperJobStarter.startGroupingJob(caseIds, admissionDateFrom, admissionDateUntil,
////                    dischargeDateFrom, dischargeDateUntil, grouped, extern, doRules,
////                    doRulesSimulate, supplementaryFee, do4ActualRoleOnly,
////                    medAndRemedies, doSimulate, pGrouperModel, queueSize, blockSize, threadCount, disableWriter);
////            while (!grouperJobStarter.getBatchStatus(jobId).equals(BatchStatus.COMPLETED)
////                    && !grouperJobStarter.getBatchStatus(jobId).equals(BatchStatus.FAILED)) {
////                try {
////                    Thread.sleep(1000L);
////                } catch (InterruptedException ex) {
////                    LOG.log(Level.SEVERE, null, ex);
////                }
////            }
//////            grouperJobStarter.startGroupingJob(caseIds, admissionDateFrom, admissionDateUntil,
//////                    dischargeDateFrom, dischargeDateUntil, grouped, extern, doRules,
//////                    doRulesSimulate, supplementaryFee, do4ActualRoleOnly,
//////                    medAndRemedies, doSimulate, pGrouperModel, database, queueSize,
//////                    blockSize, threadCount, disableWriter);
////        } catch (LockException ex) {
////            LOG.log(Level.SEVERE, null, ex);
////        }
//
////        try {
////            startGroupingThread(caseIds, grouperModel, pDatabase);
////        } catch (InterruptedException ex) {
////            LOG.log(Level.SEVERE, null, ex);
////        }
//        LOG.log(Level.INFO, "Grouping of " + caseIds.size() + " imported hospital cases had finished!");
//    }
//    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
//    public void groupNewCases(final String pGrouperModel, final String pDatabase) {
//        LOG.log(Level.INFO, "doGroup = true and grouperModel = '" + pGrouperModel + "', will obtain list of imported case ids...");
//        final GDRGModel grouperModel = getGrouperModel(pGrouperModel);
//        if (grouperModel == null) {
//            LOG.log(Level.SEVERE, "There is no grouperModel named '" + pGrouperModel + "'!");
//            //Exit here?!
//        }
//
//        final List<Long> caseIds = new ArrayList<>();
//        patientDao.getSession().doWork(new Work() {
//            @Override
//            public void execute(final Connection connection) throws SQLException {
//                try (final Statement stmt = connection.createStatement()) {
//                    final String query = "SELECT NEW_CASE_ID FROM IMEX_CASE WHERE NEW_CASE_ID IS NOT NULL"; // AND NEW_VERSION = 1 <- Is it senseful to group cases that have not changed?
//                    try (ResultSet rs = stmt.executeQuery(query)) {
//                        while (rs.next()) {
//                            final long caseId = rs.getLong("NEW_CASE_ID");
//                            if (caseId == 0L) {
//                                continue;
//                            }
//                            caseIds.add(caseId);
//                        }
//                    }
//                }
//            }
//        });
//
//        LOG.log(Level.INFO, "Will group " + caseIds.size() + " imported hospital cases now...");
//
//        try {
//            startGroupingThread(caseIds, grouperModel, pDatabase);
//        } catch (InterruptedException ex) {
//            LOG.log(Level.SEVERE, null, ex);
//        }
//
//        LOG.log(Level.INFO, "Grouping of " + caseIds.size() + " imported hospital cases had finished!");
//    }
//
//    @Transactional
//    public void startGroupingThread(final List<Long> pCaseIds, final GDRGModel pGrouperModel, final String pDatabase) throws InterruptedException {
//        final AtomicBoolean readingFinished = new AtomicBoolean(false);
//        final int threadCount = Runtime.getRuntime().availableProcessors();
//        //Map<String, List<String[]>> linesMap = new HashMap<>(0);
//        //final ExecutorService lScheduler = Executors.newFixedThreadPool(mThreadCount);
//        final CountDownLatch executionCompleted = new CountDownLatch(threadCount + 1);
//        final BlockingQueue<TCase> lBlockingQueue = new LinkedBlockingQueue<>(500); //Can hold up to 500 hospital cases in memory at once
//        final int caseIdsSize = pCaseIds.size();
//        tf.newThread(new Runnable() {
//            @Override
//            //@Transactional
//            @Transactional(value = Transactional.TxType.REQUIRES_NEW)
//            public void run() {
//                //setDatabase(pDatabase);
//                Iterator<Long> it = pCaseIds.iterator();
//                int i = 0;
//                while (it.hasNext()) {
//                    try {
//                        i++;
//                        long caseId = it.next();
//                        //TCase hospitalCase = hospitalCaseDao.findById(caseId);
//                        TCase hospitalCase = hospitalCaseDao.findCaseForCaseNumberEagerForDocumentImport(caseId);
//                        if (hospitalCase != null) {
//                            LOG.log(Level.INFO, i + "/" + caseIdsSize + ": Will group case with id = " + caseId + ", hospital ident = '" + hospitalCase.getCsHospitalIdent() + "' and case number = '" + hospitalCase.getCsCaseNumber() + "'...");
//                            final List<TCaseDetails> details = new ArrayList<>();
//                            details.add(hospitalCase.getCurrentExtern());
//                            details.add(hospitalCase.getCurrentLocal());
//                            for (TCaseDetails csd : details) {
//                                for (TCaseDepartment dep : csd.getCaseDepartments()) {
//                                    dep.getCaseIcds();
//                                    dep.getCaseOpses();
//                                }
//                            }
//                        } else {
//                            LOG.log(Level.INFO, i + "/" + caseIdsSize + ": Hospital case with id = " + caseId + " does not exist!");
//                            continue;
//                        }
//                        //performGroup(hospitalCase, false, grouperModel.getGDRGVersion());// external case
//                        //performGroupAndCheck(hospitalCase, true, grouperModel.getGDRGVersion()); // local case
//                        lBlockingQueue.offer(hospitalCase, 10, TimeUnit.SECONDS);
//                    } catch (InterruptedException ex) {
//                        LOG.log(Level.SEVERE, null, ex);
//                    }
//                }
//                readingFinished.set(true);
//                executionCompleted.countDown();
//            }
//        }).start();
//
//        final AtomicInteger counter = new AtomicInteger(0);
//        for (int i = 1; i <= threadCount; i++) {
//            tf.newThread(new Runnable() {
//                @Override
//                @Transactional(value = Transactional.TxType.REQUIRES_NEW)
//                public void run() {
//                    //setDatabase(pDatabase);
//                    try {
//                        TCase hospitalCase = null;
//                        //while(!readingFinished.get() || (sa = lBlockingQueue.poll()) != null) {
//                        while (true) {
//                            while ((hospitalCase = lBlockingQueue.poll()) != null) {
//                                int count = counter.incrementAndGet();
//                                LOG.log(Level.INFO, count + "/" + caseIdsSize + ": Will group case with id = " + hospitalCase.getId() + ", hospital ident = '" + hospitalCase.getCsHospitalIdent() + "' and case number = '" + hospitalCase.getCsCaseNumber() + "'...");
//                                performGroup(hospitalCase, false, pGrouperModel.getGDRGVersion());// external case
//                                performGroupAndCheck(hospitalCase, true, pGrouperModel.getGDRGVersion()); // local case
//                                hospitalCaseDao.merge(hospitalCase);
//                                //int lineCount = sa[sa.length - 1];
//                                //if (count % 100000 == 0) {
//                                //  LOGGER.log(Level.INFO, fileName + ": Count " + count);
//                                //}
//                            }
//                            if (readingFinished.get()) {
//                                break;
//                            }
//                            Thread.sleep(1000L);
//                        }
//                    } catch (InterruptedException ex) {
//                        LOG.log(Level.SEVERE, "Grouping thread was interrupted", ex);
//                    } catch (CpxIllegalArgumentException ex) {
//                        LOG.log(Level.SEVERE, "Was not able to group this case", ex);
//                    }
//                    executionCompleted.countDown();
//                }
//            }).start();
//        }
//        executionCompleted.await();
//        //mLogger.log(Level.INFO, "Finished reading from file " + fileName);
//        //return counter.get();
//    }
    private void sendStatusJobMessage(final long pExcecutionId, final int pPhase, final int pMaxPhases, final int pSubphase, final int pMaxSubphases, final String pComment, final BatchStatus pBatchStatus) throws JMSException {
        final BatchStatus batchStatus;
        if (pBatchStatus == null) {
            batchStatus = (pSubphase >= pMaxSubphases ? BatchStatus.COMPLETED : BatchStatus.STARTED);
        } else {
            batchStatus = pBatchStatus;
        }
        LOG.log(Level.FINEST, "P21Importer2:sendStatusJobMessage:if: ExecutionID: {0}", pExcecutionId);
        sendObjectMessage(pExcecutionId, new MessageDTO(pPhase, pMaxPhases, pSubphase, pMaxSubphases, batchStatus, null, 0, null, pComment), "importStatusMessage");
        messagesSent++;
    }

    private void sendStatusFailureJobMessage(final long pExecutionId, String pReasonForFailure, final Exception pException) throws JMSException {
        try {
            LOG.log(Level.FINEST, "JmsProducer send message. Import failure: {0}", pReasonForFailure);
            final int phases = 0;
            final int maxPhases = 0;
            final int subphases = 0;
            final int maxSubphases = 0;
            sendObjectMessage(pExecutionId, new MessageDTO(phases, maxPhases, subphases, maxSubphases, BatchStatus.FAILED, pException, 0, pReasonForFailure, ""), "importStatusMessage");
        } catch (NumberFormatException | JMSException ex) {
            LOG.log(Level.SEVERE, "Can not send message", ex);
        }
    }

    public void sendObjectMessage(final long pExecutionId, MessageDTO messageDTO, String type) throws JMSException {
        LOG.log(Level.FINEST, "P21Importer2:sendObjectMessage: ExecutionID: {0}", pExecutionId);
        ObjectMessage message = jmsContext.createObjectMessage(messageDTO);
        message.setStringProperty("ClientId", ClientManager.getCurrentCpxClientId());
        message.setStringProperty("importMessageType", type);
        message.setStringProperty("ExecutionId", String.valueOf(pExecutionId));
        JMSProducer producer = jmsContext.createProducer();
        producer.setTimeToLive(60000);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT).send(destination, message);
    }

//    /**
//     * group one case and write the results into corresponding Entities
//     */
//    private void performGroup(final TCase hospitalCase, final boolean isLocal, final int modelId) throws CpxIllegalArgumentException {
//        TransferCase requestObject = new TransferCase();
//
//        grouperCommunication.fillGrouperRequest(hospitalCase, requestObject, isLocal, modelId);
//        Object responceObject = grouperService.processGrouperRequest(requestObject);
//        if (responceObject instanceof GrouperResponseObject) {
//            grouperCommunication.fillGrouperResults(hospitalCase, (GrouperResponseObject) responceObject);
//        }
//    }
//
//    /**
//     * group and rule check one case and write the results into corresponding
//     * Entities
//     *
//     * @param hospitalCase, a case to group
//     * @param isLocal flag, which determins what case details are to use.
//     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException illegal argument
//     */
//    private void performGroupAndCheck(final TCase hospitalCase, final boolean isLocal, final int modelId) throws CpxIllegalArgumentException {
//        TransferCase requestObject = new TransferCase();
//
//        final boolean groupingRequestPerformed = grouperCommunication.fillGrouperRequest(hospitalCase, requestObject, isLocal, modelId);
//
//        //2018-03-01 DNi - Ticket #CPX-848: Stop further processing if no grouping request was performed! (strange errors can occur if we would follow the path here)
//        if (!groupingRequestPerformed) {
//            LOG.log(Level.WARNING, "No grouping performed for this case: " + (hospitalCase == null ? "NULL" : hospitalCase.getCaseKey()) + " (id " + (hospitalCase == null ? "NULL" : String.valueOf(hospitalCase.getId())) + ")");
//            return;
//        }
//
//        Object responceObject = grouperService.processRuleGrouperRequest(requestObject); //don't to this without proper grouping request!
//        if (responceObject instanceof GrouperResponseObject) {
//            //GrouperCommunication.fillGrouperResults(hospitalCase, (GrouperResponseObject) responceObject);
//            grouperCommunication.fillGrouperResults(hospitalCase, (GrouperResponseObject) responceObject);
//        }
//    }
    public class FailureCallback implements FailureCallbackI {

        public final long executionId;

        public FailureCallback(final long pExecutionId) {
            executionId = pExecutionId;
        }

        @Override
        public void execute(final String pReasonForFailure, final Exception pException) {
            try {
                sendStatusFailureJobMessage(executionId, pReasonForFailure, pException);
            } catch (JMSException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }

    public class ProgressCallback implements ProgressCallbackI {

        public final long executionId;

        public ProgressCallback(final long pExecutionId) {
            executionId = pExecutionId;
        }

        @Override
        public void execute(final int pPhase, final int pMaxPhases, final int pSubphase, final int pMaxSubphases, final String pMessage) {
            try {
                sendStatusJobMessage(executionId, pPhase, pMaxPhases, pSubphase, pMaxSubphases, pMessage, BatchStatus.STARTED);
            } catch (JMSException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }

    public class SuccessCallback implements SuccessCallbackI {

        public final long executionId;

        public SuccessCallback(final long pExecutionId) {
            executionId = pExecutionId;
        }

        @Override
        public void execute(final String pMessage) {
            try {
                final int phases = 0;
                final int maxPhases = 0;
                final int subphases = 0;
                final int maxSubphases = 0;
                sendStatusJobMessage(executionId, phases, maxPhases, subphases, maxSubphases, pMessage, BatchStatus.COMPLETED);
            } catch (JMSException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }

    //long mStartTime = System.currentTimeMillis();
    //long mStartTime2 = mStartTime;
    //final long mStartupTime = mStartTime;
    public static boolean setDatabase(final String pDatabase) {
        try {
            CpxJob cpxJob = ClientManager.createJobSession(pDatabase);
            LOG.log(Level.INFO, "CPX Job initiated: " + cpxJob.getClientId());
        } catch (IllegalStateException ex) {
            LOG.log(Level.SEVERE, "A problem occured when I tried to create a job session!", ex);
            return false;
        }
        return true;
    }

    public static GDRGModel getGrouperModel(final String pGrouperModel) {
        final String grpModel = (pGrouperModel == null) ? "" : pGrouperModel.trim();
        GDRGModel grouperModel = GDRGModel.getModel2Name(grpModel);
        if (grouperModel == null) {
            for (GDRGModel model : GDRGModel.values()) {
                if (model.name().equalsIgnoreCase(grpModel)) {
                    grouperModel = model;
                    break;
                }
            }
        }
        return grouperModel;
    }

    public boolean login(final String pClientId, final String pUserName, final String pHashedPassword, final String pDatabase, final String pAppTypeEn) throws LoginException {
        ClientManager.manipulateUserPrincipal(pClientId);
        return loginService.login(pClientId, pUserName, pHashedPassword, pDatabase, null, AppTypeEn.findByName(pAppTypeEn), true);
    }

    public int getNewClientId() {
        int clientId = loginService.getNewClientId();
        return clientId;
    }
    
    public String getUserRolePropertiesAsString(final String pClientId) {
        ClientManager.manipulateUserPrincipal(pClientId);
        return loginService.getActualRolePropertiesAsString();
    }
    
    public long getUserId(final String pClientId) {
        ClientManager.manipulateUserPrincipal(pClientId);
        return loginService.getActualUserId();
    }
    
    public Date getCurrentUserLastAction(final String pClientId) {
        ClientManager.manipulateUserPrincipal(pClientId);
        return ClientManager.getCurrentCpxUser().getLastActionAt().getTime();
    }
    
    public List<CpxPersistenceUnit> getPersistenceUnits(final String pClientId){
        ClientManager.manipulateUserPrincipal(pClientId);
        return ClientManager.getAvailablePersistenceUnits();
    }
}
