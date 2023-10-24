/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.core.model.task;

import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.service.ejb.SingleCaseGroupingEJBRemote;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

/**
 *
 * @author gerschmann
 */
public class GroupPatientCasesTask extends CpxTask<List<TGroupingResults>>{

    private static final Logger LOG = Logger.getLogger(GroupPatientCasesTask.class.getName());
    
    private TPatient patient;
    
    public GroupPatientCasesTask(TPatient pPatient){
        patient = pPatient;
        initTask();
    }

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
        if (patient == null) {
            return new ArrayList<>();
        }
        List<TGroupingResults> result = new ArrayList<>();
        Future<List<TGroupingResults>> future = null;
        try {
            SingleCaseGroupingEJBRemote caseGroupingEJB = Session.instance().getEjbConnector().connectSingleCaseGroupingBean().get();
            
            Set<TCase> cases = patient.getCases();
            if(cases == null || cases.isEmpty()){
                 return new ArrayList<>();
            }

            future = caseGroupingEJB.groupPatientCaseDetails(patient.getId(), true,
                    Session.instance().getCpxUserId(), Session.instance().getCpxActualRoleId(),
                    CpxClientConfig.instance().getSelectedGrouper());
            result.addAll(future.get()); 
            //Timeout is defined in SingleCaseGroupingEJB!
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
