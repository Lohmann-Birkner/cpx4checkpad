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
package de.lb.cpx.service.merge;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.grouper.model.transfer.TransferMergePatient;
import de.lb.cpx.model.TCaseMergeMapping;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.dao.TCaseDao;
import de.lb.cpx.service.ejb.LockServiceBean;
import de.lb.cpx.service.helper.Callback;
import de.lb.cpx.service.helper.FailureCallback;
import de.lb.cpx.service.helper.ProgressCallback;
import de.lb.cpx.service.jms.producer.BatchGrouperMessageProducer;
import de.lb.cpx.service.jms.producer.StatusBroadcastProducer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.runtime.BatchStatus;
import javax.ejb.Lock;
import static javax.ejb.LockType.READ;
import javax.ejb.Stateless;
import de.lb.cpx.shared.dto.BatchMergingDTO;
import de.lb.cpx.shared.dto.broadcast.BroadcastOriginEn;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.jms.JMSException;
import org.hibernate.jdbc.Work;
import org.jboss.ejb3.annotation.TransactionTimeout;

/**
 *
 * @author gerschmann
 */
@Stateless
@Lock(READ)
public class MergeJobStarter {

    private static final Logger LOG = Logger.getLogger(MergeJobStarter.class.getName());
    
    private long mJobId = -1;

    private BatchStatus batchstatus = BatchStatus.ABANDONED;
    public static final int MAX_PHASES = 3;
    
    private final AtomicBoolean stopSignal = new AtomicBoolean(false);
    
    @Resource(name = "java:comp/DefaultManagedThreadFactory")
    private ManagedThreadFactory tf;    
    
    @EJB 
    private BatchGrouperMessageProducer producer;
    
    @EJB
    private TCaseDao caseDao;
    
    @EJB
    private StatusBroadcastProducer<String> broadcast;

    @EJB
    private LockServiceBean dBLockService;
    
    public long getJobId() {
        return mJobId;
    }
    
    public long startMergingJob(final Long pExecutionId, final CaseTypeEn pGrpresType, final GDRGModel grouperModel) {
       final long jobId = pExecutionId; //ThreadLocalRandom.current().nextInt(min, max);
        mJobId = jobId;

        if (BatchStatus.STARTING.equals(batchstatus) || BatchStatus.STARTED.equals(batchstatus) || BatchStatus.STOPPING.equals(batchstatus)) {
            //throw new IllegalArgumentException("Another batch job is running");
            LOG.log(Level.SEVERE, "Another batch merge job is running!");
            final String comment = null;
            final double caseDetailsPerSecond = 0.0D;
            final BatchMergingDTO dto = new BatchMergingDTO(
                    MAX_PHASES, MAX_PHASES,
                    0, 0,
                    BatchStatus.FAILED, null,
                    jobId, "Es läuft bereits ein anderer BatchMerging Job",
                    comment, caseDetailsPerSecond);
            sendNumberOfGroupedItemsMessage(jobId, dto);
            return jobId;
        }
        
        batchstatus = BatchStatus.STARTING;
//        final int min = 1;
//        final int max = Integer.MAX_VALUE;

        final BatchMergingDTO startingDto = new BatchMergingDTO(
                1, MAX_PHASES,
                0, 0,
                BatchStatus.STARTING, null,
                jobId, "",
                "", 0.0D
        );
        sendNumberOfGroupedItemsMessage(jobId, startingDto);


        final long startTime = System.currentTimeMillis();

        batchstatus = BatchStatus.STARTED;
        LOG.log(Level.INFO, "Merge process started for " + pGrpresType.name());

        final BatchMergingDTO startedDto = new BatchMergingDTO(
                1, MAX_PHASES,
                0, 0,
                BatchStatus.STARTED, null,
                jobId, "",
                "", 0.0D
        );
        sendNumberOfGroupedItemsMessage(jobId, startedDto);
        broadcast.send(BroadcastOriginEn.MERGING, "Fallzusammenführung wurde gestartet", jobId, BatchStatus.STARTED);
// declaration for used container variables and simple variables that are used  in process threads
        final AtomicBoolean requestLoaderFinished = new AtomicBoolean(false);
        final AtomicInteger responseLoaderFinishedTmp = new AtomicInteger(0);
        final AtomicBoolean responseLoaderFinished = new AtomicBoolean(false);
        final AtomicInteger numberOfThreads = new AtomicInteger(1);  // let one thread for  response loader only
        
        final AtomicInteger patientCount = new AtomicInteger(-1);
//        final AtomicInteger caseCount = new AtomicInteger(-1);
        
        final AtomicLong mergeRequestLoaderTotalTime = new AtomicLong();
        final AtomicLong mergeResponseLoaderTotalTime = new AtomicLong();
        final AtomicLong mergeResponseWriterTotalTime = new AtomicLong();


        final AtomicLong requestLoaderTimer = new AtomicLong();
        final AtomicLong responseLoaderTimer = new AtomicLong();
        final AtomicLong responseWriterTimer = new AtomicLong();
        
        final AtomicBoolean requestLoaderStopped = new AtomicBoolean(false);
        final AtomicInteger responseLoaderStopped = new AtomicInteger(0);
        final AtomicBoolean responseWriterStopped = new AtomicBoolean(false);

        // result of merge process
        final AtomicReference<List<TCaseMergeMapping>> batchResult = new AtomicReference<>(new ArrayList<>());
        // databaseName
        final String database = caseDao.getConnectionDatabase();
        final boolean isOracle = caseDao.isOracle();
//Process queues:
// request queue
        final BlockingQueue<TransferMergePatient> requestQueue = new ArrayBlockingQueue<>(500);
// response queue
        final BlockingQueue<TransferMergePatient> responseQueue = new ArrayBlockingQueue<>(500);
        
// callbacks that are used in process threads        

// request loader finished callback
        final Callback requestLoaderFinishedCb = new Callback() {
            @Override
            public void execute() {
                requestLoaderFinished.set(true);
                LOG.log(Level.INFO, "request loader has finished!");
            }
        };
// response loader finished callback        
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
// response writer finished callback        
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
                final long patientPerSecond = Math.round(patientCount.get() / (Double.doubleToRawLongBits(elapsedTimeInSec) == Double.doubleToRawLongBits(0.0d) ? 1L : elapsedTimeInSec));
                final double timePerCaseDetail = (double) elapsedTime / patientCount.get();
//                long diff = TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.MILLISECONDS);

                final NumberFormat nf = NumberFormat.getInstance();
                nf.setMaximumFractionDigits(2);
                //nf.setGroupingUsed(true);

                final String comment = "Fallzusammenführung erfolgreich beendet (Start: " + sdf.format(new Date(startTime)) 
                        + ", Ende: " + sdf.format(new Date(endTime)) 
                        + ", Dauer: " + sdf.format(new Date(elapsedTime - (60 * 60 * 1000))) 
                        + ", geprüft Patienten: " + patientCount.get() 
                        + ", Performance: " + patientPerSecond 
                        + " Falldetails pro Sekunde bzw. " + nf.format(timePerCaseDetail).replace(".", ",") + " ms pro Falldetail)";
                final Exception ex = null;
//                final TBatchResult batchResult = batchResultDao.loadEagerly(batchResultId.get());
                LOG.log(Level.INFO, comment);
                //do some conversions here, because elapsedTime represents the total time in seconds, but grouperRequestLoaderTotalTime, grouperResponseLoaderTotalTime and grouperResponseWriterTotalTime represent overlapping consumed cpu time
                long elapsedTimeTotal = mergeRequestLoaderTotalTime.get() 
                        + mergeResponseLoaderTotalTime.get() 
                        + mergeResponseWriterTotalTime.get();
                
                if (elapsedTimeTotal > 0L) {
//                    long elapsedTimePhase1 = Math.round(grouperRequestLoaderTotalTime.get() / ((double) elapsedTimeTotal / elapsedTime));
//                    long elapsedTimePhase2 = Math.round(grouperResponseLoaderTotalTime.get() / ((double) elapsedTimeTotal / elapsedTime));
//                    long elapsedTimePhase3 = Math.round(grouperResponseWriterTotalTime.get() / ((double) elapsedTimeTotal / elapsedTime));
                    //long elapsedTimeMisc = elapsedTime - (elapsedTimePhase1 + elapsedTimePhase2 + elapsedTimePhase3);
                    LOG.log(Level.INFO, "time consumed by phases:\n"
                            + "Phase 1 (" + MergeRequestLoader.class.getSimpleName() + "): " + (mergeRequestLoaderTotalTime.get() / 1000L) + " seconds, " + sdf.format(new Date(mergeRequestLoaderTotalTime.get() - (60 * 60 * 1000))) + " (" + nf.format((mergeRequestLoaderTotalTime.get() / (double) elapsedTimeTotal) * 100L) + " %)\n"
                            + "Phase 2 (" + MergeResponseLoader.class.getSimpleName() + "): " + (mergeResponseLoaderTotalTime.get() / 1000L) + " seconds, " + sdf.format(new Date(mergeResponseLoaderTotalTime.get() - (60 * 60 * 1000))) + " (" + nf.format((mergeResponseLoaderTotalTime.get() / (double) elapsedTimeTotal) * 100L) + " %)\n"
                            + "Phase 3 (" + MergeResponseWriter.class.getSimpleName() + "): " + (mergeResponseWriterTotalTime.get() / 1000L) + " seconds, " + sdf.format(new Date(mergeResponseWriterTotalTime.get() - (60 * 60 * 1000))) + " (" + nf.format((mergeResponseWriterTotalTime.get() / (double) elapsedTimeTotal) * 100L) + " %)"
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

                final BatchMergingDTO dto = new BatchMergingDTO(
                        MAX_PHASES, MAX_PHASES,
                        patientCount.get(), patientCount.get(),
                        BatchStatus.COMPLETED, ex,
                        jobId, reason,
                        comment, patientPerSecond,
                        batchResult.get()
                );
                sendNumberOfGroupedItemsMessage(jobId, dto);

                broadcast.send(BroadcastOriginEn.MERGING, "Fallzusammenführung wurde erfolgreich beendet!", jobId, BatchStatus.COMPLETED, ex);
            }
        };

// stopped callback
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
                        && (patientCount.get() == -1 || (responseLoaderStopped.get() == numberOfThreads.get() && responseWriterStopped.get()))) {
                    LOG.log(Level.INFO, "Batchgrouping was stopped");
                    stopSignal.set(false);
                    dBLockService.unlockDatabase();
                    batchstatus = BatchStatus.ABANDONED;

                    final Exception ex = null;
                    final String reason = "";
                    final String comment = "Batchgrouping wurde gestoppt";
                    final double caseDetailsPerSecond = 0.0D;
                    final BatchMergingDTO dto = new BatchMergingDTO(
                            MAX_PHASES, MAX_PHASES,
                            0, patientCount.get(),
                            BatchStatus.STOPPED, ex,
                            jobId, reason,
                            comment, caseDetailsPerSecond);
                    sendNumberOfGroupedItemsMessage(jobId, dto);
                    broadcast.send(BroadcastOriginEn.MERGING, "Fallzusammenführung wurde abgebrochen", jobId, BatchStatus.STOPPED);
                }
            }
        };

// failure callback
        final FailureCallback failureCb = new FailureCallback() {
            @Override
            public void execute(final String pReason, Exception ex) {
                batchstatus = BatchStatus.FAILED;
                final double caseDetailsPerSecond = 0.0D;
                final String comment = "";
                //exception class is maybe not available on client (javax.jms.JMSException: org.jboss.weld.exceptions.CreationException)
                final BatchMergingDTO dto = new BatchMergingDTO(
                        MAX_PHASES, MAX_PHASES,
                        0, 0,
                        BatchStatus.FAILED, null,
                        jobId, pReason + (ex == null ? "" : ": " + ex.getMessage()),
                        comment, caseDetailsPerSecond);
                sendNumberOfGroupedItemsMessage(jobId, dto);
            }
        };

        // requestloader progress callback
        final ProgressCallback requestLoaderProgressCb = new ProgressCallback() {
            @Override
            public void execute(long pStartTime, int pStep, int pMaxSteps, final String pComment) {
                final int phase = 1;
                final String reason = "";
                final Exception ex = null;
                final double caseDetailsPerSecond = 0.0D;
        
                final BatchMergingDTO dto = new BatchMergingDTO(
                        phase, MAX_PHASES,
                        pStep, pMaxSteps,
                        BatchStatus.STARTED, ex,
                        jobId, reason,
                        pComment, caseDetailsPerSecond);
                sendNumberOfGroupedItemsMessage(jobId, dto);
                LOG.log(Level.INFO, "Phase " + dto.getPhase() + "/" + dto.getMaxPhases() + ", Schritt " + dto.getSubphase() + "/" + dto.getMaxSubphases() + ": " + dto.getComment());
                //LOG.log(Level.INFO, "Processed: " + pProcessedDetails + "/" + caseDetailsCount.get() + ", Performance: " + caseDetailsPerSecond + " case details/s, ETA: " + etaStr);
            }
            @Override
             public void execute(final long pStartTime, final int pStep, final int pMaxSteps, 
                        final int subphase, final int subphases,final String pComment){
                final int phase = 1;
                final String reason = "";
                final Exception ex = null;
                final BatchMergingDTO dto = new BatchMergingDTO(
                        phase, MAX_PHASES,
                        subphase, subphases,
                        BatchStatus.STARTED, ex,
                        jobId, reason,
                        pComment);
                sendNumberOfGroupedItemsMessage(jobId, dto);
                LOG.log(Level.INFO, "Phase " + dto.getPhase() + "/" + dto.getMaxPhases() + ", Schritt " + dto.getSubphase() + "/" + dto.getMaxSubphases() + ": " + dto.getComment());
                 
             }
        };

        // response writer progress callback
        final ProgressCallback requestLoaderProgress1 = new ProgressCallback() {
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
                final BatchMergingDTO dto = new BatchMergingDTO(
                        phase, MAX_PHASES,
                        pProcessedDetails, pAllDetails,
                        BatchStatus.STARTED, ex,
                        jobId, reason,
                        comment, caseDetailsPerSecond);
                sendNumberOfGroupedItemsMessage(jobId, dto);
                LOG.log(Level.INFO, "Phase " + dto.getPhase() + "/" + dto.getMaxPhases() + ", case detail " + dto.getSubphase() + "/" + dto.getMaxSubphases() + ", Performance: " + caseDetailsPerSecond + " case details/s, ETA: " + etaStr);
                //LOG.log(Level.INFO, "Processed: " + pProcessedDetails + "/" + pAllDetails + ", Performance: " + caseDetailsPerSecond + " case details/s, ETA: " + etaStr);
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
                final BatchMergingDTO dto = new BatchMergingDTO(
                        phase, MAX_PHASES,
                        pProcessedDetails, pAllDetails,
                        BatchStatus.STARTED, ex,
                        jobId, reason,
                        comment, caseDetailsPerSecond);
                sendNumberOfGroupedItemsMessage(jobId, dto);
                LOG.log(Level.INFO, "Phase " + dto.getPhase() + "/" + dto.getMaxPhases() + ", case detail " + dto.getSubphase() + "/" + dto.getMaxSubphases() + ", Performance: " + caseDetailsPerSecond + " case details/s, ETA: " + etaStr);
                //LOG.log(Level.INFO, "Processed: " + pProcessedDetails + "/" + pAllDetails + ", Performance: " + caseDetailsPerSecond + " case details/s, ETA: " + etaStr);
            }
        };
// distribution progress callback 
        final ProgressCallback distributionProgressCb = new ProgressCallback() {
            @Override
            public void execute(long pStartTime, int pStep, int pMaxSteps, final String pComment) {
                final int phase = 3;
                final String reason = "";
                final Exception ex = null;
                final double patientPerSecond = 0.0D;
                final BatchMergingDTO dto = new BatchMergingDTO(
                        phase, MAX_PHASES,
                        pStep, pMaxSteps,
                        BatchStatus.STARTED, ex,
                        jobId, reason,
                        pComment, patientPerSecond);
                //final BatchGroupingDTO dto = new BatchGroupingDTO(pProcessedDetails, caseDetailsCount.get(), caseDetailsPerSec, comment);
                sendNumberOfGroupedItemsMessage(jobId, dto);
                LOG.log(Level.INFO, "Phase " + dto.getPhase() + "/" + dto.getMaxPhases() + ", Schritt " + dto.getSubphase() + "/" + dto.getMaxSubphases() + ": " + dto.getComment());
                //LOG.log(Level.INFO, "Processed: " + pProcessedDetails + "/" + caseDetailsCount.get() + ", Performance: " + caseDetailsPerSecond + " case details/s, ETA: " + etaStr);
            }
        };

// start of all functionalities in

        caseDao.getSession().doWork(new Work() {
            @Override
            @TransactionTimeout(value = 360, unit = TimeUnit.MINUTES)
            public void execute(final Connection connection) throws SQLException {
                tf.newThread(new Runnable() {
                    @Override
                    @Asynchronous
                    public void run() {
//                        grouperRequestLoaderTotalTime.set(System.currentTimeMillis());
                        final MergeRequestLoader requestLoader = ClientManager.lookup(MergeRequestLoader.class);
                        Thread.currentThread().setName("REQUEST-LOADER-" + database);
                        requestLoader.start(
                                connection,
                                pGrpresType,
                                grouperModel, 
                                isOracle, 
                                requestQueue,
                                patientCount, 
//                                caseCount, 
                                stopSignal, 
                                stoppedCb,
                                requestLoaderStopped, 
                                requestLoaderFinishedCb,
                                requestLoaderProgressCb,
//                                requestLoaderProgress1,
                                failureCb,
                                requestLoaderTimer,
                                mergeRequestLoaderTotalTime,
                                mergeResponseLoaderTotalTime);
                    }
                }).start();

                while (patientCount.get() == -1 && !stopSignal.get()) {
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException ex) {
                        LOG.log(Level.SEVERE, null, ex);
                        Thread.currentThread().interrupt();
                    }
                }

                if (numberOfThreads.get() > patientCount.get()) {
                    numberOfThreads.set(patientCount.get() == 0 ? 1 : patientCount.get());
                }

                for (int i = 1; i <= numberOfThreads.get(); i++) {
                    final int threadNo = i;
                    tf.newThread(new Runnable() {
                        @Override
                        @Asynchronous
                        public void run() {
//                            grouperResponseLoaderTotalTime.set(System.currentTimeMillis());
                            final MergeResponseLoader responseLoader;
                            synchronized (stopSignal) {
                                try {
                                    responseLoader = ClientManager.lookup(MergeResponseLoader.class);
                                } catch (Exception ex) {
                                    failureCb.execute("cannot initialize response loader", ex);
                                    stopSignal.set(true);
                                    responseLoaderStopped.incrementAndGet();
                                    stoppedCb.execute();
                                    return;
                                }
                            }
                            Thread.currentThread().setName("RESPONSE-LOADER-" + threadNo + "-" + database);
                            responseLoader.start(pGrpresType, requestQueue,
                                    responseQueue, patientCount, 
//                                    caseCount, 
                                    stopSignal,
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
                        final MergeResponseWriter responseWriter = ClientManager.lookup(MergeResponseWriter.class);
                        Thread.currentThread().setName("RESPONSE-WRITER-" + database);
                        responseWriter.start(connection, pGrpresType, grouperModel, responseQueue,
                                patientCount, 
//                                caseCount, 
                                stopSignal, stoppedCb,
                                responseWriterStopped, responseWriterFinishedCb,
                                responseWriterProgressCb, responseLoaderFinished,
                                distributionProgressCb,
                                failureCb,
                                batchResult,
                                responseWriterTimer,
                                mergeResponseLoaderTotalTime,
                                mergeResponseWriterTotalTime
                        );
                    }
                }).start();
            }
        });


        return jobId;
    }
    
    private void sendNumberOfGroupedItemsMessage(final long pExecutionId, final BatchMergingDTO pBatchMergingDto) {
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
                producer.sendObjectMessage(pExecutionId, pBatchMergingDto, 500);
            } catch (JMSException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        } else {
            LOG.warning("Can't send Message, MessageProducer is null!");
        }
    }
    
    public Long restartMergingJob(Long executionId, String admissionDateFrom, String admissionDateUntil, String dischargeDateFrom, String dischargeDateUntil, boolean grouped, boolean extern, boolean doRules, boolean doRulesSimulate, boolean supplementaryFee, boolean do4ActualRoleOnly, boolean medAndRemedies, boolean doSimulate, String grouperModel) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Lock(READ)
    public boolean stopMergingJob(Long executionId) {
        if (BatchStatus.STARTED.equals(batchstatus)
                || BatchStatus.STARTING.equals(batchstatus)) {
            LOG.log(Level.INFO, "received stop signal");
            batchstatus = BatchStatus.STOPPING;
            stopSignal.set(true);

            final BatchMergingDTO startedDto = new BatchMergingDTO(
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

//get number of patients who have more than on case of type pGrpresType
    @Lock(READ)
    public int getPatientCount(final  CaseTypeEn pGrpresType) {
        final boolean isOracle = caseDao.isOracle();
        final String query = "SELECT COUNT(*) FROM T_PATIENT "
                + "WHERE ID IN ("
                + "SELECT T_PATIENT_ID FROM T_CASE WHERE CANCEL_FL = 0 AND CS_CASE_TYPE_EN = " + pGrpresType.name() 
                + " GROUP BY T_PATIENT_ID HAVING COUNT(T_PATIENT_ID) > 1)";
        Number count = (Number) caseDao.getSession().createNativeQuery(query).getSingleResult();
        return count.intValue();

    }
}
