/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.task;

import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.service.ejb.SingleCaseGroupingEJBRemote;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

/**
 *
 * @author wilde
 */
public class GroupCaseDetailsTask extends CpxTask<List<TGroupingResults>> {

//    private CpxTask cpxGroupingTask;
    private static final Logger LOG = Logger.getLogger(GroupCaseDetailsTask.class.getSimpleName());
    private final TCaseDetails detailsToGroup;
    private final List<Long> ruleIds;

    public GroupCaseDetailsTask(TCaseDetails pDetails) {
        super();
        detailsToGroup = pDetails;
        ruleIds = null;
        initTask();

    }

    public GroupCaseDetailsTask(TCaseDetails pDetails, List<Long> pRuleIds) {
        super();
        detailsToGroup = pDetails;
        ruleIds = pRuleIds == null ? null : new ArrayList<>(pRuleIds);
        initTask();
    }

//    public GroupingTask(boolean pIsLocal,boolean isAutoGrouping,List<Long> caseIds){
//        this.selectedCaseIds = caseIds.toArray(new Long[caseIds.size()]);
////        selectedCaseIds = Arrays.asList(caseId);
////        selectedCaseIds.add(caseId);
//        this.isAutoGrouping = isAutoGrouping;
//        this.isLocal = pIsLocal;
//        
//        initTask();
//    } 
//    
//    public GroupingTask(boolean pIsLocal,boolean isAutoGrouping,Long... caseIds){
//        this.selectedCaseIds = caseIds;
////        selectedCaseIds = Arrays.asList(caseId);
////        selectedCaseIds.add(caseId);
//        this.isAutoGrouping = isAutoGrouping;
//        this.isLocal = pIsLocal;
//        
//        initTask();
//    }
    private void initTask() {
        setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                cancel(true);
            }
        });
        setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                List<TGroupingResults> results = null;
                try {
                    results = get();
                } catch (ExecutionException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                    Thread.currentThread().interrupt();
                }
                //            //int successfulCounter = 0;
                //            if (results != null) {
                //              for(TGroupingResults result: results) {
                //                if (result.getValue() instanceof Throwable) {
                //                  MainApp.showErrorMessageDialog((Throwable)result.getValue(), "Error occured while grouping of case Id " + result.getKey());
                //                }
                //              }
                //          }

                //            //int successfulCounter = 0;
                //            if (results != null) {
                //              for(TGroupingResults result: results) {
                //                if (result.getValue() instanceof Throwable) {
                //                  MainApp.showErrorMessageDialog((Throwable)result.getValue(), "Error occured while grouping of case Id " + result.getKey());
                //                }
                //              }
                //          }
            }
        });
        setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                BasicMainApp.showErrorMessageDialog(getException(), "Error occured while grouping");
            }
        });

    }

    @Override
    public void start() {
        if (stop()) {
            return;
        }

        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public boolean stop() {
        if (isRunning()) {
            LOG.info("Will cancel grouper now...");
            cancel();
            while (!isDone()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                    Thread.currentThread().interrupt();
                }
            }
            LOG.log(Level.INFO, "Previous started grouper was cancelled");
            //return cpxGroupingTask;
            return true;
        }

        return false;

    }

    @Override
    protected List<TGroupingResults> call() throws Exception {
        if (detailsToGroup == null) {
            return new ArrayList<>();
        }
        List<TGroupingResults> result = new ArrayList<>();
        Future<List<TGroupingResults>> future = null;
        try {
            SingleCaseGroupingEJBRemote caseGroupingEJB = Session.instance().getEjbConnector().connectSingleCaseGroupingBean().get();
            future = caseGroupingEJB.groupCaseDetails(detailsToGroup.getId(), detailsToGroup.getCsdIsLocalFl(),
                    Session.instance().getCpxUserId(), Session.instance().getCpxActualRoleId(),
                    CpxClientConfig.instance().getSelectedGrouper(), ruleIds);
            result = future.get(); //Timeout is defined in SingleCaseGroupingEJB!
        } catch (InterruptedException | ExecutionException ex) {
            LOG.log(Level.FINE, "Task was interrupted", ex);
            if (future != null && !future.isCancelled()) {
                future.cancel(true);
            }
            Thread.currentThread().interrupt();
        }

        return result;
    }
}
