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
 *    2016  Husser - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.grouper;

import de.lb.cpx.service.helper.FailureCallback;
import de.lb.cpx.service.helper.Callback;
import de.lb.cpx.service.helper.ProgressCallback;
import de.lb.cpx.grouper.model.dto.GrouperResponseObject;
import de.lb.cpx.grouper.model.transfer.BatchGroupParameter;
import de.lb.cpx.grouper.model.transfer.TransferPatient;
import de.lb.cpx.model.TBatchResult;
import de.lb.cpx.model.TBatchGroupParameter;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.commonDB.dao.CdbUserRolesDao;
import de.lb.cpx.server.dao.TBatchResultDao;
import de.lb.cpx.service.ejb.LockServiceBean;
import de.lb.cpx.service.jms.producer.BatchGrouperMessageProducer;
import de.lb.cpx.service.jms.producer.StatusBroadcastProducer;
import de.lb.cpx.shared.dto.BatchGroupingDTO;
import de.lb.cpx.shared.dto.broadcast.BroadcastOriginEn;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.batch.runtime.BatchStatus;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Lock;
import static javax.ejb.LockType.READ;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.inject.Inject;
import javax.jms.JMSException;
import org.hibernate.jdbc.Work;
import org.jboss.ejb3.annotation.TransactionTimeout;

/**
 *
 * @author niemeier
 */
@Stateless
//@TransactionManagement(TransactionManagementType.BEAN)
//@TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
//@TransactionTimeout(value = 360, unit = TimeUnit.MINUTES)
//@ConcurrencyManagement(BEAN) //Bean-Managed Concurrency
@Lock(READ)
public class GrouperJobStarter {

    private static final Logger LOG = Logger.getLogger(GrouperJobStarter.class.getName());
    @EJB
    private LockServiceBean dBLockService;
    @Inject
    private CdbUserRolesDao userRoles;
    @Inject
//    private TCaseDao caseDao;
    private TBatchResultDao caseDao;
//    @EJB
//    private GrouperCommunication grouperCommunication;

    private final AtomicBoolean stopSignal = new AtomicBoolean(false);

////    @EJB(beanName = "GrouperServiceBean")
//    @Inject
//    private GrouperService grouperService;
//    @EJB(beanName = "RuleReadServiceBean")
//    private RuleReadServicBeanLocal ruleReadServiceBean;
    @Resource(name = "java:comp/DefaultManagedThreadFactory")
    private ManagedThreadFactory tf;

    @EJB
    private BatchGrouperMessageProducer producer;

    @EJB
    private StatusBroadcastProducer<String> broadcast;

//    @EJB
//    private TBatchResultDao batchResultDao;
    private BatchStatus batchstatus = BatchStatus.ABANDONED;

    private long mJobId = -1;

    public long getJobId() {
        return mJobId;
    }

    public static final int MAX_PHASES = 3;

//    @Inject
//    GrouperRequestLoader requestLoader;
//    @Inject
//    GrouperResponseLoader responseLoader;
//    @Inject
//    GrouperResponseWriter responseWriter;
//    @Inject
//    private CpxServerConfig serverConfig;
    //@Inject
    //private PrepStorer prepStorer;
//    /**
//     * starts GroupingJob and returns ExecutionId of the newly started job
//     *
//     * @param pExecutionId job id
//     * @param pCaseIds list of hospital case ids to be grouped
//     * @param pAdmissionDateFrom admission date - optional
//     * @param pAdmissionDateUntil admission date - optional
//     * @param pDischargeDateFrom discharge date - optional
//     * @param pDischargeDateUntil discharge date - optional
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
//     * @param pQueueSize queue size
//     * @param pBlockSize block size
//     * @param pThreadCount thread count
//     * @param pDisableWriter disable writer
//     * @return Exectution Id of the Job
//     * @throws de.lb.cpx.shared.dto.LockException db is locked
//     */
////    @TransactionTimeout(value = 360, unit = TimeUnit.MINUTES)
//    @Lock(READ)
//    public long startGroupingJob(final long pExecutionId, final List<Long> pCaseIds, final String pAdmissionDateFrom, final String pAdmissionDateUntil,
//            final String pDischargeDateFrom, final String pDischargeDateUntil, boolean grouped, BatchGroupParameter.DetailsFilterEn pDetailsFilter,
//            boolean doRules, boolean doRulesSimulate, boolean supplementaryFee, boolean do4ActualRoleOnly, boolean medAndRemedies, boolean doSimulate, String grouperModel,
//            final String pQueueSize, final String pBlockSize, final String pThreadCount, final boolean pDisableWriter) throws LockException {
//
//        return startGroupingJob(pExecutionId, pCaseIds, pAdmissionDateFrom, pAdmissionDateUntil,
//                pDischargeDateFrom, pDischargeDateUntil, grouped, pDetailsFilter,
//                doRules, doRulesSimulate, supplementaryFee, do4ActualRoleOnly, medAndRemedies, doSimulate, grouperModel,
//                pQueueSize, pBlockSize, pThreadCount, pDisableWriter,
//                null);
//    }
//
//    /**
//     * starts GroupingJob and returns ExecutionId of the newly started job
//     *
//     * @param pExecutionId job id
//     * @param pCaseIds list of hospital case ids to be grouped
//     * @param pAdmissionDateFrom admission date - optional
//     * @param pAdmissionDateUntil admission date - optional
//     * @param pDischargeDateFrom discharge date - optional
//     * @param pDischargeDateUntil discharge date - optional
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
//     * @param pQueueSize queue size
//     * @param pBlockSize block size
//     * @param pThreadCount thread count
//     * @param pDisableWriter disable writer
//     * @param ruleIds rule ids
//     * @return Exectution Id of the Job
//     */
//    public long startGroupingJob(final long pExecutionId, final List<Long> pCaseIds, final String pAdmissionDateFrom, final String pAdmissionDateUntil,
//            final String pDischargeDateFrom, final String pDischargeDateUntil, boolean grouped, BatchGroupParameter.DetailsFilterEn pDetailsFilter,
//            boolean doRules, boolean doRulesSimulate, boolean supplementaryFee, boolean do4ActualRoleOnly, boolean medAndRemedies, boolean doSimulate, String grouperModel,
//            final String pQueueSize, final String pBlockSize, final String pThreadCount, final boolean pDisableWriter,
//            List<Long> ruleIds) {
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        Date admissionDateFrom = null;
//        if (pAdmissionDateFrom != null && !pAdmissionDateFrom.trim().isEmpty()) {
//            try {
//                admissionDateFrom = df.parse(pAdmissionDateFrom);
//            } catch (ParseException ex) {
//                LOG.log(Level.SEVERE, "Admission date from is invalid!", ex);
//            }
//        }
//        Date admissionDateUntil = null;
//        if (pAdmissionDateUntil != null && !pAdmissionDateUntil.trim().isEmpty()) {
//            try {
//                admissionDateUntil = df.parse(pAdmissionDateUntil);
//            } catch (ParseException ex) {
//                LOG.log(Level.SEVERE, "Admission date until is invalid!", ex);
//            }
//        }
//        Date dischargeDateFrom = null;
//        if (pDischargeDateFrom != null && !pDischargeDateFrom.trim().isEmpty()) {
//            try {
//                dischargeDateFrom = df.parse(pDischargeDateFrom);
//            } catch (ParseException ex) {
//                LOG.log(Level.SEVERE, "Discharge date from is invalid!", ex);
//            }
//        }
//        Date dischargeDateUntil = null;
//        if (pDischargeDateUntil != null && !pDischargeDateUntil.trim().isEmpty()) {
//            try {
//                dischargeDateUntil = df.parse(pDischargeDateUntil);
//            } catch (ParseException ex) {
//                LOG.log(Level.SEVERE, "Discharge date until is invalid!", ex);
//            }
//        }
//
//        final int threadCount = BatchGroupParameter.toThreadCount(pThreadCount);
//        final int queueSize = BatchGroupParameter.toQueueSize(pBlockSize);
//        final int blockSize = BatchGroupParameter.toBlockSize(pBlockSize);
//
//        final BatchGroupParameter groupingParameter = new BatchGroupParameter();
//        final GDRGModel md = grouperModel == null ? GDRGModel.AUTOMATIC : GDRGModel.getModel2Name(grouperModel);
//        String roleIds = "";
//        if (doRules) {
//            roleIds = getRoleIdsProperty(do4ActualRoleOnly);
//        }
//        groupingParameter.setDoRules(doRules);
//        groupingParameter.setDoRulesSimulate(doRulesSimulate);
//        groupingParameter.setDoSupplementaryFees(supplementaryFee);
//        groupingParameter.setDo4actualRoleOnly(do4ActualRoleOnly);
//        groupingParameter.setDoMedAndRemedies(medAndRemedies);
//        groupingParameter.setDoSimulate(doSimulate);
//        groupingParameter.setModelId(md.getGDRGVersion());
//        groupingParameter.setModel(md);
//        //groupingParameter.setDoInclHis(pDetailsFilter);
//        groupingParameter.setDetailsFilter(pDetailsFilter);
//        groupingParameter.setRoleIdsString(roleIds);
//        groupingParameter.setQueueSize(queueSize);
//        groupingParameter.setBlockSize(blockSize);
//        groupingParameter.setThreadCount(threadCount);
//        groupingParameter.setDisableWriter(pDisableWriter);
//        groupingParameter.setRuleIds(ruleIds);
//        groupingParameter.setGrouped(grouped);
//        groupingParameter.setAdmissionDateFrom(admissionDateFrom);
//        groupingParameter.setAdmissionDateUntil(admissionDateUntil);
//        groupingParameter.setDischargeDateFrom(dischargeDateFrom);
//        groupingParameter.setDischargeDateUntil(dischargeDateUntil);
//        groupingParameter.setCaseIds(pCaseIds);
//
//        return startGroupingJob(pExecutionId, groupingParameter);
//    }

    @Lock(READ)
    public int getCaseDetailsCount(final BatchGroupParameter groupingParameter) {
        final boolean isOracle = caseDao.isOracle();
        final String query = "SELECT COUNT(*) " + GrouperRequestLoader.getFromWhereQuery(groupingParameter, isOracle);
        Number count = (Number) caseDao.getSession().createNativeQuery(query).getSingleResult();
        return count.intValue();
//        try (Connection conn = caseDao.getConnection(); Statement stmt = conn.createStatement()) {
//            try (ResultSet rs = stmt.executeQuery(query)) {
//                while(rs.next()) {
//                    return rs.getInt(1);
//                }
//            }
//        } catch (SQLException ex) {
//            LOG.log(Level.SEVERE, MessageFormat.format("cannot determine the number of case details to group: {0}", query), ex);
//        }
//        return -1;
    }

    /**
     * starts GroupingJob and returns ExecutionId of the newly started job
     *
     * @param pExecutionId job id
     * @param groupingParameter batch grouping parameters
     * @return Exectution Id of the Job
     */
//    @TransactionTimeout(value = 360, unit = TimeUnit.MINUTES)    
    @Lock(READ)
    public long startGroupingJob(final long pExecutionId, final BatchGroupParameter groupingParameter) {
        final long jobId = pExecutionId; //ThreadLocalRandom.current().nextInt(min, max);
        mJobId = jobId;

        if (groupingParameter == null) {
            //throw new IllegalArgumentException("grouping parameters cannot be null!");
            LOG.log(Level.SEVERE, "grouping parameters cannot be null!");
            final String comment = null;
            final double caseDetailsPerSecond = 0.0D;
            final BatchGroupingDTO dto = new BatchGroupingDTO(
                    MAX_PHASES, MAX_PHASES,
                    0, 0,
                    BatchStatus.FAILED, null,
                    jobId, "Es wurden keine Batchgrouping-Einstellungen übergeben",
                    comment, caseDetailsPerSecond);
            sendNumberOfGroupedItemsMessage(jobId, dto);
            return jobId;
        }
        
        String roleIds = "";
        if (groupingParameter.isDoRules()) {
            roleIds = getRoleIdsProperty(groupingParameter.isDo4actualRoleOnly());
        }
        groupingParameter.setRoleIdsString(roleIds);

        if (BatchStatus.STARTING.equals(batchstatus) || BatchStatus.STARTED.equals(batchstatus) || BatchStatus.STOPPING.equals(batchstatus)) {
            //throw new IllegalArgumentException("Another batch job is running");
            LOG.log(Level.SEVERE, "Another batch job is running!");
            final String comment = null;
            final double caseDetailsPerSecond = 0.0D;
            final BatchGroupingDTO dto = new BatchGroupingDTO(
                    MAX_PHASES, MAX_PHASES,
                    0, 0,
                    BatchStatus.FAILED, null,
                    jobId, "Es läuft bereits ein anderer Batchgrouping Job",
                    comment, caseDetailsPerSecond);
            sendNumberOfGroupedItemsMessage(jobId, dto);
            return jobId;
        }

        if (groupingParameter.getThreadCount() <= 0 || groupingParameter.getThreadCount() > 128) {
            //throw new IllegalArgumentException("Thread Count is invalid: " + groupingParameter.getThreadCount());
            LOG.log(Level.SEVERE, "Thread Count is invalid: {0}!", groupingParameter.getThreadCount());
            final String comment = null;
            final double caseDetailsPerSecond = 0.0D;
            final BatchGroupingDTO dto = new BatchGroupingDTO(
                    MAX_PHASES, MAX_PHASES,
                    0, 0,
                    BatchStatus.FAILED, null,
                    jobId, MessageFormat.format("Es wurde eine ungültige Anzahl an Threads übergeben: {0}!", groupingParameter.getThreadCount()),
                    comment, caseDetailsPerSecond);
            sendNumberOfGroupedItemsMessage(jobId, dto);
            return jobId;
        }

        batchstatus = BatchStatus.STARTING;
//        final int min = 1;
//        final int max = Integer.MAX_VALUE;

        final BatchGroupingDTO startingDto = new BatchGroupingDTO(
                1, MAX_PHASES,
                0, 0,
                BatchStatus.STARTING, null,
                jobId, "",
                "", 0.0D
        );
        sendNumberOfGroupedItemsMessage(jobId, startingDto);

        final String database = caseDao.getConnectionDatabase();

        if (groupingParameter.getCaseIds() != null && groupingParameter.getCaseIds().isEmpty()) {
            batchstatus = BatchStatus.COMPLETED;
            return mJobId;
        }

        final long startTime = System.currentTimeMillis();

        batchstatus = BatchStatus.STARTED;
        LOG.log(Level.INFO, "Batchgrouping has started with params: " + groupingParameter.toString());

        final BatchGroupingDTO startedDto = new BatchGroupingDTO(
                1, MAX_PHASES,
                0, 0,
                BatchStatus.STARTED, null,
                jobId, "",
                "", 0.0D
        );
        sendNumberOfGroupedItemsMessage(jobId, startedDto);
        broadcast.send(BroadcastOriginEn.BATCHGROUPING, "Batchgrouping wurde gestartet", jobId, BatchStatus.STARTED);

//        if (groupingParameter.getRuleIds() != null) {
//            grouperService.setRuleList(ruleReadServiceBean.getRule2ListId(groupingParameter.getRuleIds()));
//            LOG.log(Level.INFO, "BATCHGROUPING-GROUPER-" + database + ": rules: " + grouperService.getRulesCount());
//        } else {
//            grouperService.resetRuleList();
//            LOG.log(Level.INFO, "BATCHGROUPING-GROUPER-" + database + ": rules: " + grouperService.getRulesCount());
//        }
//        final Date adf = admissionDateFrom;
//        final Date adu = admissionDateUntil;
//        final Date ddf = dischargeDateFrom;
//        final Date ddu = dischargeDateUntil;
        final BlockingQueue<TransferPatient> requestQueue = new ArrayBlockingQueue<>(500);
        final BlockingQueue<GrouperResponseObject> responseQueue = new ArrayBlockingQueue<>(500);
        final boolean isOracle = caseDao.isOracle();

        final AtomicInteger caseCount = new AtomicInteger(-1);
        final AtomicInteger caseDetailsCount = new AtomicInteger(-1);
        final AtomicReference<TBatchResult> batchResult = new AtomicReference<>(new TBatchResult());

        final AtomicBoolean requestLoaderFinished = new AtomicBoolean(false);
        final AtomicInteger responseLoaderFinishedTmp = new AtomicInteger(0);
        final AtomicBoolean responseLoaderFinished = new AtomicBoolean(false);

        final AtomicBoolean requestLoaderStopped = new AtomicBoolean(false);
        final AtomicInteger responseLoaderStopped = new AtomicInteger(0);
        final AtomicBoolean responseWriterStopped = new AtomicBoolean(false);

        final AtomicLong requestLoaderTimer = new AtomicLong();
        final AtomicLong responseLoaderTimer = new AtomicLong();
        final AtomicLong responseWriterTimer = new AtomicLong();

        final AtomicLong grouperRequestLoaderTotalTime = new AtomicLong();
        final AtomicLong grouperResponseLoaderTotalTime = new AtomicLong();
        final AtomicLong grouperResponseWriterTotalTime = new AtomicLong();

        final AtomicInteger numberOfThreads = new AtomicInteger(groupingParameter.getThreadCount());
        LOG.log(Level.INFO, "numberOfThreads = " + numberOfThreads);

        final Callback requestLoaderFinishedCb = new Callback() {
            @Override
            public void execute() {
                requestLoaderFinished.set(true);
                LOG.log(Level.INFO, "request loader has finished!");
            }
        };
        final Callback responseLoaderFinishedCb = new Callback() {
            @Override
            public void execute() {
                int finishedLoaders = responseLoaderFinishedTmp.incrementAndGet();
                if (finishedLoaders >= numberOfThreads.get()) {
                    responseLoaderFinished.set(true);
//                    grouperResponseLoaderTotalTime.set(System.currentTimeMillis() - grouperResponseLoaderTotalTime.get());
                }
                LOG.log(Level.INFO, finishedLoaders + " response loaders have finished!");
            }
        };
        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        final Callback responseWriterFinishedCb = new Callback() {
            @Override
            public void execute() {
                LOG.log(Level.INFO, "response writer has finished!");
//                grouperResponseWriterTotalTime.set(System.currentTimeMillis() - grouperResponseWriterTotalTime.get());
                dBLockService.unlockDatabase();
                batchstatus = BatchStatus.COMPLETED;
                final String reason = "";

                final long endTime = System.currentTimeMillis();
                final long elapsedTime = endTime - startTime;
                final double elapsedTimeInSec = (elapsedTime / 1000d);
                final long caseDetailsPerSecond = Math.round(caseDetailsCount.get() / (Double.doubleToRawLongBits(elapsedTimeInSec) == Double.doubleToRawLongBits(0.0d) ? 1L : elapsedTimeInSec));
                final double timePerCaseDetail = (double) elapsedTime / caseDetailsCount.get();
//                long diff = TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.MILLISECONDS);

                final NumberFormat nf = NumberFormat.getInstance();
                nf.setMaximumFractionDigits(2);
                //nf.setGroupingUsed(true);

                final String comment = "Batchgrouping erfolgreich beendet (Start: " + sdf.format(new Date(startTime)) + ", Ende: " + sdf.format(new Date(endTime)) + ", Dauer: " + sdf.format(new Date(elapsedTime - (60 * 60 * 1000))) + ", Gegroupte Falldetails: " + caseDetailsCount.get() + ", Performance: " + caseDetailsPerSecond + " Falldetails pro Sekunde bzw. " + nf.format(timePerCaseDetail).replace(".", ",") + " ms pro Falldetail)";
                final Exception ex = null;
//                final TBatchResult batchResult = batchResultDao.loadEagerly(batchResultId.get());
                LOG.log(Level.INFO, comment);
                //do some conversions here, because elapsedTime represents the total time in seconds, but grouperRequestLoaderTotalTime, grouperResponseLoaderTotalTime and grouperResponseWriterTotalTime represent overlapping consumed cpu time
                long elapsedTimeTotal = grouperRequestLoaderTotalTime.get() + grouperResponseLoaderTotalTime.get() + grouperResponseWriterTotalTime.get();
                if (elapsedTimeTotal > 0L) {
//                    long elapsedTimePhase1 = Math.round(grouperRequestLoaderTotalTime.get() / ((double) elapsedTimeTotal / elapsedTime));
//                    long elapsedTimePhase2 = Math.round(grouperResponseLoaderTotalTime.get() / ((double) elapsedTimeTotal / elapsedTime));
//                    long elapsedTimePhase3 = Math.round(grouperResponseWriterTotalTime.get() / ((double) elapsedTimeTotal / elapsedTime));
                    //long elapsedTimeMisc = elapsedTime - (elapsedTimePhase1 + elapsedTimePhase2 + elapsedTimePhase3);
                    LOG.log(Level.INFO, "time consumed by phases:\n"
                            + "Phase 1 (" + GrouperRequestLoader.class.getSimpleName() + "): " + (grouperRequestLoaderTotalTime.get() / 1000L) + " seconds, " + sdf.format(new Date(grouperRequestLoaderTotalTime.get() - (60 * 60 * 1000))) + " (" + nf.format((grouperRequestLoaderTotalTime.get() / (double) elapsedTimeTotal) * 100L) + " %)\n"
                            + "Phase 2 (" + GrouperResponseLoader.class.getSimpleName() + "): " + (grouperResponseLoaderTotalTime.get() / 1000L) + " seconds, " + sdf.format(new Date(grouperResponseLoaderTotalTime.get() - (60 * 60 * 1000))) + " (" + nf.format((grouperResponseLoaderTotalTime.get() / (double) elapsedTimeTotal) * 100L) + " %)\n"
                            + "Phase 3 (" + GrouperResponseWriter.class.getSimpleName() + "): " + (grouperResponseWriterTotalTime.get() / 1000L) + " seconds, " + sdf.format(new Date(grouperResponseWriterTotalTime.get() - (60 * 60 * 1000))) + " (" + nf.format((grouperResponseWriterTotalTime.get() / (double) elapsedTimeTotal) * 100L) + " %)"
                    //+ "Miscellaneous: " + (elapsedTimeMisc / 1000L) + " seconds, " + sdf.format(new Date(elapsedTimeMisc - (60 * 60 * 1000)))
                    );
                    final long totalConsumption = requestLoaderTimer.get() + responseLoaderTimer.get() + responseWriterTimer.get();
                    if (totalConsumption > 0L) {
                        final double nanoSecondsToSecondsFactor = 1_000_000_000D;
//                        final double nanoSecondsToMillisecondsFactor = 1_000_000D;
//                        final double factor = (totalConsumption / nanoSecondsToMillisecondsFactor / elapsedTimePhase2);
                        //LOG.log(Level.INFO, "Total time consumed during phase 2: " + nf.format(totalConsumption / nanoSecondsToSecondsFactor) + " seconds");
                        LOG.log(Level.INFO, "Time consumed by components during phase 2: Request Loader: " + nf.format(requestLoaderTimer.get() / nanoSecondsToSecondsFactor) + " seconds (" + nf.format((requestLoaderTimer.get() / (double) totalConsumption) * 100L) + " %), Response Loader (" + numberOfThreads.get() + " threads): " + nf.format(responseLoaderTimer.get() / nanoSecondsToSecondsFactor) + " seconds (" + nf.format((responseLoaderTimer.get() / (double) totalConsumption) * 100L) + " %), Response Writer: " + nf.format(responseWriterTimer.get() / nanoSecondsToSecondsFactor) + " seconds (" + nf.format((responseWriterTimer.get() / (double) totalConsumption) * 100L) + " %)");
                    }
                }
// todo add parameter group parameter
                TBatchGroupParameter param = new TBatchGroupParameter();
                param.setCreationDate(new Date());
                param.setAdmissionDateFrom(groupingParameter.getAdmissionDateFrom());
                param.setAdmissionDateUntil(groupingParameter.getAdmissionDateUntil());
                param.setDischargeDateFrom(groupingParameter.getDischargeDateFrom());
                param.setDischargeDateUntil(groupingParameter.getDischargeDateUntil());
                param.setGrouped(groupingParameter.isGrouped());
                param.setDetailsFilter(groupingParameter.getDetailsFilter());
                param.setDoRules(groupingParameter.isDoRules());
                param.setDoRulesSimulate(groupingParameter.isDoRulesSimulate());
                param.setDoSupplementaryFees(groupingParameter.isDoSupplementaryFees());
                param.setDo4actualRoleOnly(groupingParameter.isDo4actualRoleOnly());
                param.setDoSimulate(groupingParameter.isDoSimulate());
                param.setDoHistoryCases(groupingParameter.isDoHistoryCases());
                param.setDoUseAllRules(groupingParameter.getRuleIds() == null || groupingParameter.getRuleIds().isEmpty());
                param.setBatchResult( batchResult.get());
                batchResult.get().setBatchGrouperParameter(param);
                final BatchGroupingDTO dto = new BatchGroupingDTO(
                        MAX_PHASES, MAX_PHASES,
                        caseDetailsCount.get(), caseDetailsCount.get(),
                        BatchStatus.COMPLETED, ex,
                        jobId, reason,
                        comment, caseDetailsPerSecond,
                        batchResult.get()
                );
                sendNumberOfGroupedItemsMessage(jobId, dto);

                broadcast.send(BroadcastOriginEn.BATCHGROUPING, "Batchgrouping wurde erfolgreich beendet!", jobId, BatchStatus.COMPLETED, ex);
            }
        };

//        if (groupingParameter.getRuleIds() != null) {
//            //                long timestamp2 = System.currentTimeMillis();
//            grouperService.setRuleList(ruleReadServiceBean.getRule2ListId(groupingParameter.getRuleIds()));
//            LOG.log(Level.INFO, "BATCHGROUPING-GROUPER-" + database + ": rules: " + grouperService.getRulesCount());
//        } else {
//            grouperService.resetRuleList();
//            LOG.log(Level.INFO, "BATCHGROUPING-GROUPER-" + database + ": rules: " + grouperService.getRulesCount());
//        }
//        final GrouperCallback grouperCb = new GrouperCallback() {
//            @Override
//            public GrouperResponseObject processRuleGrouperRequest(TransferCase pTransferCase) {
//                Object result = grouperService.processRuleGrouperRequest(pTransferCase);
//                if (result instanceof GrouperResponseObject) {
//                    return (GrouperResponseObject) result;
//                }
//                return null;
//            }
//
//            @Override
//            public GrouperResponseObject processGrouperRequest(TransferCase pTransferCase) {
//                Object result = grouperService.processGrouperRequest(pTransferCase);
//                if (result instanceof GrouperResponseObject) {
//                    return (GrouperResponseObject) result;
//                }
//                return null;
//            }
//        };
        final Callback stoppedCb = new Callback() {
            @Override
            public void execute() {
//                LOG.log(Level.INFO, "check stoppedCb:"
//                        + "\nrequestLoaderStopped.get() = " + requestLoaderStopped.get()
//                        + "\ncaseDetailsCount.get() = " + caseDetailsCount.get()
//                        + "\nresponseLoaderStopped.get() = " + responseLoaderStopped.get()
//                        + "\nnumberOfThreads.get() = " + numberOfThreads.get()
//                        + "\nresponseWriterStopped.get() = " + responseWriterStopped.get());
                LOG.log(Level.INFO, "check stop conditions now...");
//                LOG.log(Level.INFO, 
//                        "requestLoaderStopped.get() = " + requestLoaderStopped.get() + "\r\n" +
//                        "caseDetailsCount.get() = " + caseDetailsCount.get() + "\r\n" +
//                        "responseLoaderStopped.get() = " + responseLoaderStopped.get() + "\r\n" +
//                        "numberOfThreads.get() = " + numberOfThreads.get() + "\r\n" +
//                        "responseWriterStopped.get() = " + responseWriterStopped.get() + "\r\n" + 
//                        "(caseDetailsCount.get() == -1 || (responseLoaderStopped.get() == numberOfThreads.get() && responseWriterStopped.get())) = " + ((caseDetailsCount.get() == -1 || (responseLoaderStopped.get() == numberOfThreads.get() && responseWriterStopped.get()))) + "\r\n" 
//                );
                if (requestLoaderStopped.get()
                        && (caseDetailsCount.get() == -1 || (responseLoaderStopped.get() == numberOfThreads.get() && responseWriterStopped.get()))) {
                    LOG.log(Level.INFO, "Batchgrouping was stopped");
                    stopSignal.set(false);
                    dBLockService.unlockDatabase();
                    batchstatus = BatchStatus.ABANDONED;

                    final Exception ex = null;
                    final String reason = "";
                    final String comment = "Batchgrouping wurde gestoppt";
                    final double caseDetailsPerSecond = 0.0D;
                    final BatchGroupingDTO dto = new BatchGroupingDTO(
                            MAX_PHASES, MAX_PHASES,
                            0, caseDetailsCount.get(),
                            BatchStatus.STOPPED, ex,
                            jobId, reason,
                            comment, caseDetailsPerSecond);
                    sendNumberOfGroupedItemsMessage(jobId, dto);
                    broadcast.send(BroadcastOriginEn.BATCHGROUPING, "Batchgrouping wurde abgebrochen", jobId, BatchStatus.STOPPED);
                }
            }
        };
        final FailureCallback failureCb = new FailureCallback() {
            @Override
            public void execute(final String pReason, Exception ex) {
                batchstatus = BatchStatus.FAILED;
                final double caseDetailsPerSecond = 0.0D;
                final String comment = "";
                //exception class is maybe not available on client (javax.jms.JMSException: org.jboss.weld.exceptions.CreationException)
                final BatchGroupingDTO dto = new BatchGroupingDTO(
                        MAX_PHASES, MAX_PHASES,
                        0, 0,
                        BatchStatus.FAILED, null,
                        jobId, pReason + (ex == null ? "" : ": " + ex.getMessage()),
                        comment, caseDetailsPerSecond);
                sendNumberOfGroupedItemsMessage(jobId, dto);
            }
        };

        final ProgressCallback requestLoaderProgressCb = new ProgressCallback() {
            @Override
            public void execute(long pStartTime, int pStep, int pMaxSteps, final String pComment) {
                final int phase = 1;
                final String reason = "";
                final Exception ex = null;
                final double caseDetailsPerSecond = 0.0D;
                final BatchGroupingDTO dto = new BatchGroupingDTO(
                        phase, MAX_PHASES,
                        pStep, pMaxSteps,
                        BatchStatus.STARTED, ex,
                        jobId, reason,
                        pComment, caseDetailsPerSecond);
                //final BatchGroupingDTO dto = new BatchGroupingDTO(pProcessedDetails, caseDetailsCount.get(), caseDetailsPerSec, comment);
                sendNumberOfGroupedItemsMessage(jobId, dto);
                LOG.log(Level.INFO, "Phase " + dto.getPhase() + "/" + dto.getMaxPhases() + ", Schritt " + dto.getSubphase() + "/" + dto.getMaxSubphases() + ": " + dto.getComment());
                //LOG.log(Level.INFO, "Processed: " + pProcessedDetails + "/" + caseDetailsCount.get() + ", Performance: " + caseDetailsPerSecond + " case details/s, ETA: " + etaStr);
            }
        };

        final ProgressCallback responseWriterProgressCb = new ProgressCallback() {
            @Override
            public void execute(long pStartTime, int pProcessedDetails, int pAllDetails, final String pComment) {
                final long elapsedTime = System.currentTimeMillis() - pStartTime;
                final double elapsedTimeInSec = (elapsedTime / 1000d);
                final long caseDetailsPerSecond = Math.round(pProcessedDetails / (Double.doubleToRawLongBits(elapsedTimeInSec) == Double.doubleToRawLongBits(0.0d) ? 1L : elapsedTimeInSec));
                final long eta = startTime + ((pAllDetails * elapsedTime) / pProcessedDetails);
                final String etaStr = sdf.format(new Date(eta));
                final String comment = pProcessedDetails + "/" + pAllDetails + "\r\nETA: " + etaStr + pComment;

                final int phase = 2;
                final String reason = "";
                final Exception ex = null;
                final BatchGroupingDTO dto = new BatchGroupingDTO(
                        phase, MAX_PHASES,
                        pProcessedDetails, pAllDetails,
                        BatchStatus.STARTED, ex,
                        jobId, reason,
                        comment, caseDetailsPerSecond);
                //final BatchGroupingDTO dto = new BatchGroupingDTO(pProcessedDetails, caseDetailsCount.get(), caseDetailsPerSec, comment);
                sendNumberOfGroupedItemsMessage(jobId, dto);
                LOG.log(Level.INFO, "Phase " + dto.getPhase() + "/" + dto.getMaxPhases() + ", case detail " + dto.getSubphase() + "/" + dto.getMaxSubphases() + ", Performance: " + caseDetailsPerSecond + " case details/s, ETA: " + etaStr);
                //LOG.log(Level.INFO, "Processed: " + pProcessedDetails + "/" + pAllDetails + ", Performance: " + caseDetailsPerSecond + " case details/s, ETA: " + etaStr);
            }
        };

        final ProgressCallback distributionProgressCb = new ProgressCallback() {
            @Override
            public void execute(long pStartTime, int pStep, int pMaxSteps, final String pComment) {
                final int phase = 3;
                final String reason = "";
                final Exception ex = null;
                final double caseDetailsPerSecond = 0.0D;
                final BatchGroupingDTO dto = new BatchGroupingDTO(
                        phase, MAX_PHASES,
                        pStep, pMaxSteps,
                        BatchStatus.STARTED, ex,
                        jobId, reason,
                        pComment, caseDetailsPerSecond);
                //final BatchGroupingDTO dto = new BatchGroupingDTO(pProcessedDetails, caseDetailsCount.get(), caseDetailsPerSec, comment);
                sendNumberOfGroupedItemsMessage(jobId, dto);
                LOG.log(Level.INFO, "Phase " + dto.getPhase() + "/" + dto.getMaxPhases() + ", Schritt " + dto.getSubphase() + "/" + dto.getMaxSubphases() + ": " + dto.getComment());
                //LOG.log(Level.INFO, "Processed: " + pProcessedDetails + "/" + caseDetailsCount.get() + ", Performance: " + caseDetailsPerSecond + " case details/s, ETA: " + etaStr);
            }
        };

        caseDao.getSession().doWork(new Work() {
            @Override
            @TransactionTimeout(value = 360, unit = TimeUnit.MINUTES)
            public void execute(final Connection connection) throws SQLException {
                tf.newThread(new Runnable() {
                    @Override
                    @Asynchronous
                    public void run() {
//                        grouperRequestLoaderTotalTime.set(System.currentTimeMillis());
                        final GrouperRequestLoader requestLoader = ClientManager.lookup(GrouperRequestLoader.class);
                        Thread.currentThread().setName("REQUEST-LOADER-" + database);
                        requestLoader.start(connection,
                                groupingParameter, isOracle, requestQueue,
                                caseCount, caseDetailsCount, stopSignal, stoppedCb,
                                requestLoaderStopped, requestLoaderFinishedCb,
                                requestLoaderProgressCb,
                                failureCb,
                                requestLoaderTimer,
                                grouperRequestLoaderTotalTime,
                                grouperResponseLoaderTotalTime);
                    }
                }).start();

                while (caseDetailsCount.get() == -1 && !stopSignal.get()) {
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException ex) {
                        LOG.log(Level.SEVERE, null, ex);
                        Thread.currentThread().interrupt();
                    }
                }

                if (numberOfThreads.get() > caseDetailsCount.get()) {
                    numberOfThreads.set(caseDetailsCount.get() == 0 ? 1 : caseDetailsCount.get());
                }

                for (int i = 1; i <= numberOfThreads.get(); i++) {
                    final int threadNo = i;
                    tf.newThread(new Runnable() {
                        @Override
                        @Asynchronous
                        public void run() {
//                            grouperResponseLoaderTotalTime.set(System.currentTimeMillis());
                            final GrouperResponseLoader responseLoader;
                            synchronized (stopSignal) {
                                try {
                                    responseLoader = ClientManager.lookup(GrouperResponseLoader.class);
                                } catch (Exception ex) {
                                    failureCb.execute("cannot initialize response loader", ex);
                                    stopSignal.set(true);
                                    responseLoaderStopped.incrementAndGet();
                                    stoppedCb.execute();
                                    return;
                                }
                            }
                            Thread.currentThread().setName("RESPONSE-LOADER-" + threadNo + "-" + database);
                            responseLoader.start(groupingParameter /*, grouperCb */, requestQueue,
                                    responseQueue, caseCount, caseDetailsCount, stopSignal,
                                    stoppedCb, responseLoaderStopped, responseLoaderFinishedCb,
                                    requestLoaderFinished,
                                    failureCb, batchResult,
                                    responseLoaderTimer);
                        }
                    }).start();
                }

                tf.newThread(new Runnable() {
                    @Override
                    @Asynchronous
                    public void run() {
                        //grouperResponseWriterTotalTime.set(System.currentTimeMillis());
                        final GrouperResponseWriter responseWriter = ClientManager.lookup(GrouperResponseWriter.class);
                        Thread.currentThread().setName("RESPONSE-WRITER-" + database);
                        responseWriter.start(connection, groupingParameter, responseQueue,
                                caseCount, caseDetailsCount, stopSignal, stoppedCb,
                                responseWriterStopped, responseWriterFinishedCb,
                                responseWriterProgressCb, responseLoaderFinished,
                                distributionProgressCb,
                                failureCb,
                                batchResult,
                                responseWriterTimer,
                                grouperResponseLoaderTotalTime,
                                grouperResponseWriterTotalTime
                        );
                    }
                }).start();
            }
        });

        return jobId;
    }

    public Long restartGroupingJob(Long executionId, String admissionDateFrom, String admissionDateUntil, String dischargeDateFrom, String dischargeDateUntil, boolean grouped, boolean extern, boolean doRules, boolean doRulesSimulate, boolean supplementaryFee, boolean do4ActualRoleOnly, boolean medAndRemedies, boolean doSimulate, String grouperModel) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Lock(READ)
    public boolean stopGroupingJob(Long executionId) {
        if (BatchStatus.STARTED.equals(batchstatus)
                || BatchStatus.STARTING.equals(batchstatus)) {
            LOG.log(Level.INFO, "received stop signal");
            batchstatus = BatchStatus.STOPPING;
            stopSignal.set(true);

            final BatchGroupingDTO startedDto = new BatchGroupingDTO(
                    MAX_PHASES, MAX_PHASES,
                    0, 0,
                    BatchStatus.STOPPING, null,
                    mJobId, "",
                    "", 0.0D
            );
            sendNumberOfGroupedItemsMessage(mJobId, startedDto);

            return true;
        }
        return false;
    }

    public boolean isStopping() {
        return BatchStatus.STOPPING.equals(batchstatus);
    }

    public boolean isStopped() {
        return BatchStatus.STOPPED.equals(batchstatus);
    }

    public boolean isFailed() {
        return BatchStatus.FAILED.equals(batchstatus);
    }

    public BatchStatus getBatchStatus(Long executionId) {
        return batchstatus;
    }

    private void sendNumberOfGroupedItemsMessage(final long pExecutionId, final BatchGroupingDTO pBatchGroupingDto) {
        //Awi-20170802-CPX-528
        //Highly unsure if this is a better way to send processed items to the client
        //int numberOfGroupedItems = tBatchResult.getBatchresGroupedCount();//numberOfGroupedChunks * jobChunkSize;
        //BatchGroupingDTO groupingDTO = new BatchGroupingDTO();
        //groupingDTO.setComment(numberOfGroupedItems + "/" + totalNumberOfCases);//"Anzahl der gegroupten Fälle " + numberOfGroupedItems +"/"+totalNumberOfCases);
        //groupingDTO.setGroupedItems((100.0d * numberOfGroupedItems) / totalNumberOfCases);
        //groupingDTO.setFlowPerSecond(Double.valueOf(numberOfGroupedItems));
//        groupingDTO.setGroupedItems((1.0d * numberOfGroupedItems) / totalNumberOfCases);
        if (producer != null) {
            try {
                producer.sendObjectMessage(pExecutionId, pBatchGroupingDto, 1000);
            } catch (JMSException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        } else {
            LOG.warning("Can't send Message, MessageProducer is null!");
        }
    }

    private String getRoleIdsProperty(boolean do4ActualRoleOnly) {
        StringBuilder usedRoles = new StringBuilder();

        if (do4ActualRoleOnly) {
            LOG.log(Level.INFO, "we use rules for the actual role of the actual user only because of the setting do4ActualRoleOnly= true");
            usedRoles.append(ClientManager.getCurrentCpxRoleId());
        }
        if (usedRoles.length() == 0) {
            LOG.log(Level.INFO, "we try to get all valid roles of the system, because do4ActualRoleOnly = false or actual role was not found");
            List<Long> roleIds = userRoles.getAllValidRoleIds();
            for (Long id : roleIds) {
                usedRoles.append(id);
                usedRoles.append(",");
            }
        }

        return usedRoles.toString();
    }
}
