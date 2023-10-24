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

import de.checkpoint.server.rmServer.caseManager.RmcWiederaufnahmeIF;
import de.lb.cpx.grouper.model.transfer.TransferMergeCandidate;
import de.lb.cpx.grouper.model.transfer.TransferMergePatient;
import de.lb.cpx.model.TCaseMergeMapping;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.service.ejb.ReadmissionService;
import de.lb.cpx.service.helper.Callback;
import de.lb.cpx.service.helper.FailureCallback;
import de.lb.cpx.service.helper.ResponseLoader;
import java.util.ArrayList;
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
import javax.inject.Inject;

/**
 *
 * @author gerschmann
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class MergeResponseLoader extends ResponseLoader{

    private static final Logger LOG = Logger.getLogger(MergeResponseLoader.class.getName());

    private int counter = 0;
    
@Inject
ReadmissionService readmissionService;
     
     public void start(
            final CaseTypeEn pGrpresType,
            //            final GrouperCallback grouperCb,
            final BlockingQueue<TransferMergePatient> pSourceQueue,
            final BlockingQueue<TransferMergePatient> pTargetQueue,
            final AtomicInteger patientCount,
//            final AtomicInteger caseCount,
            final AtomicBoolean stopSignal,
            final Callback stoppedCb,
            final AtomicInteger responseLoaderStopped,
            final Callback responseLoaderFinishedCb,
            final AtomicBoolean requestLoaderFinished,
            final FailureCallback failureCb,
            final AtomicReference<List<TCaseMergeMapping>> batchResult,
            final AtomicLong timer) {
        this.stopSignal = stopSignal;
        this.responseLoaderStopped = responseLoaderStopped;
        this.stoppedCb = stoppedCb;
        
        TransferMergePatient request;
        try{
            while (true) {
                if (checkStopped()) {
                    return;
                }
                while ((request = pSourceQueue.poll()) != null) {
                    if (stopSignal.get()) {
                        break;
                    }
                    final long timeStart = System.nanoTime();
                    //                LOG.log(Level.INFO, "Pick request from queue");
                    try {
//                        List<TransferMergeCandidate> candidates = request.getCases2merge();
                        TransferMergePatient response = processMergeRequest(request, pGrpresType);
                        if(response != null){
                            addOneResponse(response, pTargetQueue, timer, batchResult, timeStart);
                        }
                        
                    } catch (InterruptedException ex) {
                        LOG.log(Level.SEVERE, null, ex);
                        failureCb.execute("error occured in GrouperResponseLoader for case " + request.getPatientId(), ex);
                        stopSignal.set(true);
                        responseLoaderStopped.incrementAndGet();
                        stopped = true;
                        stoppedCb.execute();
                        Thread.currentThread().interrupt();
                        return;
                    }catch(Exception ex){
                        LOG.log(Level.SEVERE, "error occured in GrouperResponseLoader for case " + request.getPatientId() + " we go on with other cases", ex);
                        failureCb.execute("error occured in GrouperResponseLoader for case " + request.getPatientId(), ex);

                    }
                }
                if (requestLoaderFinished.get() && pSourceQueue.isEmpty()) {
                    break;
                } else {
    //                    LOG.log(Level.INFO, "ResponseLoader is waiting...");
                    Thread.sleep(500L);
                }     
            }
            responseLoaderFinishedCb.execute();
         
        } catch (InterruptedException ex) {
               LOG.log(Level.SEVERE, null, ex);
               failureCb.execute("error occured in GrouperResponseLoader", ex);
               stopSignal.set(true);
               responseLoaderStopped.incrementAndGet();
               stopped = true;
               stoppedCb.execute();
               Thread.currentThread().interrupt();
           } 
           finally {
               if (!stopped) {
                   responseLoaderStopped.incrementAndGet();
                   stopped = true;
               }
        }
    }


    private TransferMergePatient processMergeRequest(TransferMergePatient request, CaseTypeEn pGrpresType) {
        if(request != null){
            if(request.getPatientId() == 1376151){
                int i = 0;
            }
            ArrayList<RmcWiederaufnahmeIF> candidates = request.getRmcWiederaufnahmeList(); 
            readmissionService.checkReadmissions(candidates, pGrpresType.equals(CaseTypeEn.PEPP));
            
        }
        return request;
    }

    private void addOneResponse(TransferMergePatient response, BlockingQueue<TransferMergePatient> pTargetQueue, 
            AtomicLong timer, AtomicReference<List<TCaseMergeMapping>> batchResult, long timeStart)  throws InterruptedException {
        response = checkMergeResults(response, batchResult);
        if(response != null){
            
            while (pTargetQueue.remainingCapacity() <= 0) {
                Thread.sleep(500L);
                if (checkStopped()) {
                    return;
                }
            }
            timer.addAndGet(System.nanoTime() - timeStart);
            pTargetQueue.put(response);
            //                            LOG.log(Level.INFO, "put in response in queue queue size = " + pTargetQueue.remainingCapacity());
            counter++;
            //                            LOG.log(Level.INFO, "case counter =" + counter);
            //                            if (counter % 1000 == 0) {
            //                                LOG.log(Level.INFO, "Created response for case details " + counter);
            //                                //                                sendStatusJobMessage(executionId, phase.get(), caseNo /* number of files written */, caseCount /* of total number of files */, batchstatus, "Schreibe Fall " + String.format(java.util.Locale.GERMAN, "%,d", caseNo) + "/" + String.format(java.util.Locale.GERMAN, "%,d", caseDetailsCount) + " in die CSV-Dateien...");
            //                            }
        }
            
    }

    private TransferMergePatient checkMergeResults(TransferMergePatient response, AtomicReference<List<TCaseMergeMapping>> batchResult) {
       if(response== null || batchResult == null){
           return null;
       }
       ArrayList<RmcWiederaufnahmeIF> candidates = response.getRmcWiederaufnahmeList();
       if(candidates== null || candidates.size() < 2){
           return null;
       }
       if(response.getPatientId() == 1445151){
           for(RmcWiederaufnahmeIF cand: candidates){
              TransferMergeCandidate.printCandidateString(cand);
           }
       }
        ArrayList<RmcWiederaufnahmeIF> candidates1 = new ArrayList<>();
        for(RmcWiederaufnahmeIF candidate: candidates){
            if(candidate.getMergeId() <= 0){
                response.removeCaseCandidate(candidate);
                continue;
            } 
           
            candidates1.add(candidate);
        }
        if(candidates1.size() < 2){
            return null;
        }
        response.setRmcWiederaufnahmeList(candidates1);
        return response;
    }
    
}
