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
import de.lb.cpx.service.helper.Callback;
import de.lb.cpx.service.helper.FailureCallback;
import de.lb.cpx.service.helper.ProgressCallback;
import de.lb.cpx.service.helper.ResponseWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
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
 * @author gerschmann
 */

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class MergeResponseWriter extends ResponseWriter{

    private static final Logger LOG = Logger.getLogger(MergeResponseWriter.class.getName());

    public void start(Connection connection, 
            CaseTypeEn pGrpresType, 
            GDRGModel grpModel, 
            BlockingQueue<TransferMergePatient> responseQueue, 
            AtomicInteger patientCount, 
//            AtomicInteger caseCount, 
            AtomicBoolean stopSignal, 
            Callback stoppedCb, 
            AtomicBoolean responseWriterStopped, 
            Callback responseWriterFinishedCb, 
            ProgressCallback responseWriterProgressCb, 
            AtomicBoolean responseLoaderFinished, 
            ProgressCallback distributionProgressCb, 
            FailureCallback failureCb, 
            AtomicReference<List<TCaseMergeMapping>> batchResult,
            AtomicLong responseWriterTimer, 
            AtomicLong mergeResponseLoaderTotalTime, 
            AtomicLong mergeResponseWriterTotalTime) {
        this.stopSignal = stopSignal;
        this.responseWriterStopped = responseWriterStopped;
        this.stoppedCb = stoppedCb;


        final long startTime = System.currentTimeMillis();
        int counter = 0;

        try( MergePrepStorer prepStorer = new MergePrepStorer(connection)) {
            
            TransferMergePatient response;
            while(true){
                if (checkStopped()) {
                    return;
                }
                while ((response = responseQueue.poll()) != null) {
                    if (stopSignal.get()) {
                        break;
                    }
                    final long timeStart = System.nanoTime();
                    counter++;
// write into prepStorer
                    prepStorer.insertMappingData(response, pGrpresType);
                    responseWriterTimer.addAndGet(System.nanoTime() - timeStart);    
                }
                if (responseLoaderFinished.get() && responseQueue.isEmpty()) {
                    break;
                } else {
//                    LOG.log(Level.INFO, "GrouperResponseWriter is waiting...");

                    Thread.sleep(1000L);
                }
                
            }
            if (checkStopped()) {
                return;
            }
            
            mergeResponseLoaderTotalTime.set(System.currentTimeMillis() - mergeResponseLoaderTotalTime.get());
            mergeResponseWriterTotalTime.set(System.currentTimeMillis());
            LOG.log(Level.INFO, "now distribute data"); 
 //dstribute data      
            prepStorer.distributeData(this, distributionProgressCb, pGrpresType, grpModel);
            mergeResponseWriterTotalTime.set(System.currentTimeMillis() - mergeResponseWriterTotalTime.get());
            if (checkStopped()) {
                return;
            }
            
            responseWriterFinishedCb.execute();
        } catch (SQLException | InterruptedException ex) {
            LOG.log(Level.SEVERE, null, ex);
            failureCb.execute("error occured in MergeResponseWriter", ex);
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
