/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.app.job.fx.casemerging.tab;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.connection.jms.BatchTaskMessageHandler;
import de.lb.cpx.client.core.model.task.CpxTask;
import de.lb.cpx.client.core.util.BatchJobHelper;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.model.TCaseMergeMapping;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.service.ejb.CpxP21ImportBeanRemote;
import de.lb.cpx.service.properties.CpxAuthorizationException;
import de.lb.cpx.shared.dto.BatchMergingDTO;
import de.lb.cpx.shared.dto.LockException;
import de.lb.cpx.shared.lang.Lang;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javax.batch.runtime.BatchStatus;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

/**
 *
 * @author gerschmann
 */
public  class MergeJob extends CpxTask<Void> {

        private Long jobId;
        private final Logger LOG = Logger.getLogger(getClass().getSimpleName());
        private BatchTaskMessageHandler jmsHandler = null;
        private final ObjectProperty<BatchStatus> jobStatusProperty = new SimpleObjectProperty<>();
        private final ObjectProperty<BatchMergingDTO> jobDtoProperty = new SimpleObjectProperty<>();
        
        private final EjbProxy<CpxP21ImportBeanRemote> mergeBean = Session.instance().getEjbConnector().connectCpxP21ImportBean();
        private final CaseMergingOverviewTab starter;
        private final CaseTypeEn grpresType;
//        private final Long patientId;
//        private final Long currentCaseId;
        //        /**
//         * get if the task is still running
//         * @return isRunningProperty
//         */
//        public BooleanProperty getIsRunningProperty(){
//            return isRunningProperty;
//        }
        private final ExecutorService executor = Executors.newFixedThreadPool(1); 


        MergeJob(CaseMergingOverviewTab pStarter, CaseTypeEn pGrpresType) throws JMSException {
//            this(pStarter, pGrpresType, null, null);
//        }
//        
//         MergeJob(CaseMergingOverviewTab pStarter, CaseTypeEn pGrpresType, Long pPatientId, Long pCurrentCaseId)  throws JMSException {
            super();
            starter = pStarter;
            grpresType = pGrpresType;
//            patientId = pPatientId;
//            currentCaseId = pCurrentCaseId;
            initTask();
            jobStatusProperty.addListener(new ChangeListener<BatchStatus>() {
                @Override
                public void changed(ObservableValue<? extends BatchStatus> observable, BatchStatus oldValue, BatchStatus newValue) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
//                            lblGroupingStatus.setText(getJobStatus(newValue));
                            starter.lblJobStatus().setText(BatchJobHelper.getMergeJobStatus(newValue)); 
                        }
                    });

                    if (BatchStatus.STOPPED.equals(newValue)
                            || BatchStatus.ABANDONED.equals(newValue)) {
                        LOG.info("task canceled for id " + jobId);
                        starter.btnStartStop().setStartMode();
                        //grouperBean.get().stopBatchGrouping(jobId);
                        saveJobStatus(BatchStatus.STOPPED);
                        dispose();
                    }

                    if (BatchStatus.STARTED.equals(newValue)) {
                        starter.btnStartStop().setStopMode();
                        starter.btnStartStop().updateButtonText();
                        starter.showResultProperty().set(false);
                        //grouperBean.get().stopBatchGrouping(jobId);
                        saveJobStatus(BatchStatus.STARTED);
                    }
                    if (BatchStatus.STOPPING.equals(newValue)) {
                        starter.btnStartStop().setStopping();
                        //grouperBean.get().stopBatchGrouping(jobId);
                        saveJobStatus(BatchStatus.STOPPING);
                    }
                    if (BatchStatus.FAILED.equals(newValue)) {
                        Exception ex = jobDtoProperty.get() == null ? null : jobDtoProperty.get().getException();
                        String reason = jobDtoProperty.get() == null ? null : jobDtoProperty.get().getBatchReasonForFailure();
                        boolean isLockException = ex instanceof LockException;
                        if (isLockException) {
                            LOG.log(Level.SEVERE, "Batchmerging failed because I cannot obtain database lock");
                            LOG.log(Level.FINER, "Database is already locked", ex);
                        } else {
                            LOG.log(Level.SEVERE, "Batchgrouping failed", ex);
                        }
                        de.lb.cpx.client.core.MainApp.showErrorMessageDialog(ex, "Beim ERmitteln der zusammenzuführenden Fällen ist ein Fehler aufgetreten" + (reason == null || reason.trim().isEmpty() ? "" : ":\n\n" + reason));
                        //getException().printStackTrace();
                        saveJobStatus(BatchStatus.FAILED);
                        dispose();
                    }
                    if (BatchStatus.COMPLETED.equals(newValue)) {
                        List<TCaseMergeMapping> results = null;
                        results = jobDtoProperty.get() == null ? null : jobDtoProperty.get().getResultList();
                        if (results != null) {
//                            setUpBatchResult(results);
                            starter.facade().reloadMergeCaseList();
                            starter.reload();
                            saveJobStatus(BatchStatus.COMPLETED);
                        } else {
                            if (jobStatusProperty.get() == BatchStatus.FAILED) {
                                de.lb.cpx.client.core.MainApp.showErrorMessageDialog(Lang.getErrorOccured()); 
                                saveJobStatus(BatchStatus.FAILED);
                            }
                            if (jobStatusProperty.get() == BatchStatus.STOPPED) {
                                saveJobStatus(BatchStatus.STOPPED);
                            }
                        }
                        dispose();
                    }
                }
            });
            if(starter.lblJobStatus() != null){
               starter.lblJobStatus().setText(BatchJobHelper.getMergeJobStatus(null));
            }
        }


        /**
         * restart specific Job by its id
         *
         * @param executionId unique job id, that should be restarted, a once
         * completed Job can't be restartet
         * @throws Exception thrown when a completed job is attemped to restart
         */
        public void restart(Long executionId) throws Exception {
            jobId = executionId;
            try {
                BatchStatus currentStatus = mergeBean.get().getBatchMergeStatus(jobId);
                if (BatchStatus.STOPPED.equals(currentStatus)
                        || BatchStatus.ABANDONED.equals(currentStatus)) {
                    call();
                }
            } catch (LockException | ParseException | ExecutionException | CpxAuthorizationException ex) {
                de.lb.cpx.client.core.MainApp.showErrorMessageDialog(ex, "Error occured while grouping");
            } catch (InterruptedException ex) {
                LOG.log(Level.SEVERE, null, ex);
                Thread.currentThread().interrupt();
            }
        }

        /**
         * restart last job
         *
         * @throws Exception if job cant be restartet
         */
        public void restart() throws Exception {
            if (jobId != null) {
                restart(jobId);
            }
        }

        /**
         * get the current Job id of a running Task
         *
         * @return current JobId, null if no task is currently running
         */
        public Long getJobId() {
            if (isRunning()) {
                return jobId;
            }
            return null;
        }

        @Override
        protected Void call() throws LockException, ParseException, InterruptedException, ExecutionException, CpxAuthorizationException {
            CpxClientConfig conf = CpxClientConfig.instance();
            //get target database from Properties

            String database = Session.instance().getCpxDatabase(); //conf.getLastSessionDatabase();

            GDRGModel grouperModel = conf.getSelectedGrouper();
//
//            final String queueSize = txtQueueSize.getText().trim().toUpperCase();
//            final String blockSize = txtBlockSize.getText().trim().toUpperCase();
//            final String threadCount = "1"; //txtThreadCount.getText().trim().toUpperCase();
//            final boolean disableWriter = chkBatchGroupingDisableWriter.isSelected();

            //starts ja new Job if no Job id currently available, attempt to restart if one is there
            //2018-06-28 DNi: BEGIN > CHECK LOCKS ON DATABASE < BEGIN
            Platform.runLater(() -> {
                starter.lblJobStatus().setText(null);
                starter.riProgress().setStatusText(null);
            });
            jobId = de.lb.cpx.client.core.MainApp.execWithLockDialog((Object param) -> {
                try {
                    if (starter.btnStartStop().isDisabled()) {
                        return null;
                    }
                    LOG.log(Level.INFO, "Start batchgmerging now...");
                    starter.btnStartStop().setStopMode();
                    starter.btnStartStop().setStarting();
                    return mergeBean.get().prepareBatchMerging(database);
                } catch (LockException | CpxAuthorizationException exc) {
                    starter.btnStartStop().setStartMode();
                    starter.btnStartStop().updateButtonText();
                    LOG.log(Level.SEVERE, "Batchgmerging cannot start, some cases or whole database is already locked");
                    //MainApp.showErrorMessageDialog(exc);
                    throw exc;
                }
            }, (Object param) -> {
                //aborted
                dispose();
                return null;
            });
            if (jobId == null) {
                stop();
                cancel();
                return null;
            } else {

                mergeBean.get().startBatchMerging(jobId, grpresType, grouperModel);

            }
            return null;

        }

        /**
         * Stop current Task
         */
        @Override
        public boolean stop() {
            if (jobStatusProperty.get() == BatchStatus.STARTED) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LOG.info("call stop job id " + jobId);
                        starter.btnStartStop().setStopping();
                        mergeBean.get().stopBatchMerging(jobId);
                    }
                }).start();
            }
            return true;
        }

        @Override
        public void dispose() {
            super.dispose();
            if (jmsHandler != null) {
                jmsHandler.close();
            }
            starter.btnStartStop().setStartMode();
            executor.shutdownNow();
            starter.showResultProperty().set(true);
            
        }

        /**
         * Get current JobStatus
         *
         * @return JobStatus StringProperty
         */
        public ObjectProperty<BatchStatus> getJobStatusProperty() {
            return jobStatusProperty;
        }

        private void initTask() throws JMSException {
            jmsHandler = new BatchTaskMessageHandler();
            jmsHandler.setOnMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (message instanceof TextMessage) {
                                    TextMessage msg = (TextMessage) message;
                                    starter.lblJobStatus().setText(msg.getText());
                                }
                                if (message instanceof ObjectMessage) {
//                                    LOG.info("get Message´for client " + message.getIntProperty("ClientId"));
                                    ObjectMessage msg = (ObjectMessage) message;

                                    if (msg.getObject() instanceof BatchMergingDTO) {
                                        BatchMergingDTO dto = (BatchMergingDTO) msg.getObject();
                                        LOG.log(Level.INFO, String.valueOf(dto.getBatchStatus()));
                                        if (dto.getPhase() == 2 ) {
                                            starter.riProgress().setStatusText(dto.getComment());
                                            starter.lblJobStatus().setText(null);
                                            starter.fbProgress().setFlow(dto.getCaseDetailsPerSecond());
                                        } else {
                                            starter.riProgress().setStatusText(null);
                                            starter.lblJobStatus().setText(dto.getComment());
                                            starter.fbProgress().indeterminate();
                                        }
                                        starter.riProgress().setProgress(dto.getPercentSubphase());
                                         if (jobDtoProperty.getValue() != null
                                                && BatchStatus.STARTED.equals(dto.getBatchStatus())
                                                && (BatchStatus.STOPPING.equals(jobDtoProperty.getValue().getBatchStatus()) || BatchStatus.STOPPED.equals(jobDtoProperty.getValue().getBatchStatus()))) {
                                            //do nothing
                                            //discard STARTED events after STOPPING signal was send from server
                                            LOG.log(Level.FINEST, "message is discarded: " + dto.getBatchStatus());
                                        } else {
                                            jobDtoProperty.set(dto);
                                            jobStatusProperty.set(dto.getBatchStatus());
                                        }
                                    } else if (msg.getObject() instanceof String) {
                                        String comment = (String) msg.getObject();
                                        starter.lblJobStatus().setText(comment);
                                    }

                                }
                                if (message != null && !jmsHandler.isClosed()) {
                                    message.acknowledge();
                                }
                            } catch (JMSException | IllegalStateException ex) {
                                LOG.log(Level.WARNING, null, ex);
                            }
                        }
                    });
                }
            });

        }

        public void saveJobStatus(BatchStatus batchStatus) {
            CpxClientConfig conf = CpxClientConfig.instance();
            conf.setLastBatchJobId(jobId);
            conf.setLastBatchJobStatus(batchStatus.name());
        }



    }

