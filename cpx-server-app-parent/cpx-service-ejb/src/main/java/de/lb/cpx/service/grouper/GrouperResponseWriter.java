/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.grouper;

import de.lb.cpx.service.helper.FailureCallback;
import de.lb.cpx.service.helper.Callback;
import de.lb.cpx.service.helper.ProgressCallback;
import de.lb.cpx.grouper.model.dto.GrouperResponseObject;
import de.lb.cpx.grouper.model.transfer.BatchGroupParameter;
import de.lb.cpx.grouper.model.transfer.TransferCase;
import de.lb.cpx.grouper.model.transfer.TransferGroupResult;
import de.lb.cpx.grouper.model.transfer.TransferIcd;
import de.lb.cpx.model.TBatchResult;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.service.ejb.GrouperCommunication;
import de.lb.cpx.service.helper.ResponseWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 *
 * @author niemeier
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class GrouperResponseWriter extends ResponseWriter{

    private static final Logger LOG = Logger.getLogger(GrouperResponseWriter.class.getName());
//    private AtomicBoolean stopSignal;
//    private AtomicBoolean responseWriterStopped;
//    private Callback stoppedCb;
//    private long batchResultId = 0L;

    public void start(
            final Connection conn,
            final BatchGroupParameter pParameter,
            final BlockingQueue<GrouperResponseObject> pSourceQueue,
            final AtomicInteger caseCount,
            final AtomicInteger caseDetailsCount,
            final AtomicBoolean stopSignal,
            final Callback stoppedCb,
            final AtomicBoolean responseWriterStopped,
            final Callback responseWriterFinishedCb,
            final ProgressCallback responseWriterProgressCb,
            final AtomicBoolean responseLoaderFinished,
            final ProgressCallback distributionProgressCb,
            final FailureCallback failureCb,
            final AtomicReference<TBatchResult> batchResult,
            final AtomicLong timer,
            final AtomicLong grouperResponseLoaderTotalTime,
            final AtomicLong grouperResponseWriterTotalTime) {
        this.stopSignal = stopSignal;
        this.responseWriterStopped = responseWriterStopped;
        this.stoppedCb = stoppedCb;

        final long startTime = System.currentTimeMillis();
        int counter = 0;
        try ( PrepStorer prepStorer = new PrepStorer(conn)) {
            GrouperResponseObject response;
            while (true) {
                if (checkStopped()) {
                    return;
                }
//                LOG.log(Level.INFO, "some in results - queue = " + pSourceQueue.remainingCapacity());
                while ((response = pSourceQueue.poll()) != null) {
                    if (stopSignal.get()) {
                        break;
                    }
                    final long timeStart = System.nanoTime();
                    counter++;
//                LOG.log(Level.INFO, "Pick response from queue");
                    TGroupingResults principal = null;
                    final TransferCase result = response.getResult();
                    final CaseTypeEn caseType = CaseTypeEn.findById(result.getCaseType());
                   
                    for (Map.Entry<Long, TransferIcd> entry : result.getIcd2id().entrySet()) {
                        TransferIcd transferIcd = entry.getValue();
                        TransferGroupResult transferResult = transferIcd.getGroupResult();
                        TGroupingResults grouperResult = GrouperCommunication.createOneGroupingResult(transferResult, caseType);
                        if (grouperResult != null) {
                            if (transferIcd.isHdx()) {
                                principal = grouperResult;
                                GrouperCommunication.addSimulatedAndRuleResults(grouperResult, transferResult, response, result);
                            }
                            prepStorer.insertGroupingResult(grouperResult, result, transferResult, transferIcd);
                        }
                    }
                    if (principal == null) {
                        TransferGroupResult transferResult = result.getGroupResult2principalNull();
                        if (transferResult != null) {
                            TGroupingResults grouperResult = GrouperCommunication.createOneGroupingResult(transferResult, caseType);
                            GrouperCommunication.addSimulatedAndRuleResults(grouperResult, transferResult, response, result);
                            prepStorer.insertGroupingResult(grouperResult, result, transferResult, null);
                        }
                    }
                    prepStorer.setLos(result.getCaseDetailsId(), result.getLengthOfStay());
                    if (counter % 250 == 0 || counter == caseDetailsCount.get()) {
                        final String comment = "";
                        responseWriterProgressCb.execute(startTime, counter, caseDetailsCount.get(), comment);
                    }
                    timer.addAndGet(System.nanoTime() - timeStart);
//LOG.log(Level.INFO, "case counter =" + counter);
                }
                if (responseLoaderFinished.get() && pSourceQueue.isEmpty()) {
                    break;
                } else {
//                    LOG.log(Level.INFO, "GrouperResponseWriter is waiting...");
                    //final int minimumWaitingInserts = PrepStorer.CHUNK_INSERT_SIZE / 2;
//                    prepStorer.executeInsert(PrepStorer.CHUNK_INSERT_SIZE); //execute batch as long as we are waiting for responses
                    Thread.sleep(1000L);
                }
            }
            if (checkStopped()) {
                return;
            }
            grouperResponseLoaderTotalTime.set(System.currentTimeMillis() - grouperResponseLoaderTotalTime.get());
            grouperResponseWriterTotalTime.set(System.currentTimeMillis());
            LOG.log(Level.INFO, "now distribute data");
            prepStorer.distributeData(this, distributionProgressCb, batchResult, pParameter);
            //batchResult.get().setId(batchResultIdTmp);
            grouperResponseWriterTotalTime.set(System.currentTimeMillis() - grouperResponseWriterTotalTime.get());
            if (checkStopped()) {
                return;
            }
            responseWriterFinishedCb.execute();
        } catch (SQLException | InterruptedException ex) {
            LOG.log(Level.SEVERE, null, ex);
            failureCb.execute("error occured in GrouperResponseWriter", ex);
            stopSignal.set(true);
            responseWriterStopped.set(true);
            stoppedCb.execute();
            if (ex instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        } finally {
            responseWriterStopped.set(true); 
        }
    }

}
