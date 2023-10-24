/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.simulation.menu;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.menu.fx.event.DataActionEvent;
import de.lb.cpx.client.app.menu.fx.event.Events;
import de.lb.cpx.client.app.menu.fx.table_master_detail.AvailableProcessesDialog;
import de.lb.cpx.client.app.menu.model.ListType;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.dialog.CreateProcessDialog;
import de.lb.cpx.client.core.model.fx.alert.ConfirmDialog;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmProcess;
import de.lb.cpx.wm.model.TWmRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;

/**
 *
 * @author niemeier
 */
public abstract class ProcessEditingEvent implements EventHandler<ActionEvent> {

    private static final Logger LOG = Logger.getLogger(ProcessEditingEvent.class.getName());

    private final long caseId;
    private final List<TWmProcess> processes;

    public ProcessEditingEvent(final long pCaseId, List<TWmProcess> pProcesses) {
        this(pCaseId, pProcesses, true);
    }

    public ProcessEditingEvent(final long pCaseId, List<TWmProcess> pProcesses, final boolean pShow) {
        caseId = pCaseId;
        processes = pProcesses == null ? new ArrayList<>() : new ArrayList<>(pProcesses);
        if (pShow) {
            showDialog();
        }
    }

    public final void showDialog() {
        if (processes.isEmpty()) {
            LOG.log(Level.WARNING, "list of processes are empty");
            try {
                createNewProcess();
            } catch (CpxIllegalArgumentException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        } else {
            LOG.log(Level.INFO, "list of processes are not empty, so show available processes");
            openExistingProcess();
        }
    }

//    @Override
//    public void handle(Event t) {
//        //hide();
//    }
    private void createNewProcess() throws CpxIllegalArgumentException {
        CreateProcessDialog dialog = new CreateProcessDialog(Lang.getWmRequestcreationTitle(), caseId, new ProcessServiceFacade(-1L));
        dialog.getResultsProperty().addListener(new ChangeListener<TWmRequest>() {
            @Override
            public void changed(ObservableValue<? extends TWmRequest> observable, TWmRequest oldValue, TWmRequest newValue) {
                //check if newValue exists, and have an existing processes with valid id
                if (newValue != null && newValue.getProcessHospital() != null && newValue.getProcessHospital().getId() != 0) {
                    ConfirmDialog confirm = new ConfirmDialog(MainApp.getWindow(), Lang.getProcessConfirm(String.valueOf(newValue.getProcessHospital().getWorkflowNumber())));
                    confirm.showAndWait().ifPresent(new Consumer<ButtonType>() {
                        @Override
                        public void accept(ButtonType t) {
                            if (t.equals(ButtonType.YES)) {
//                                    openItem(newValue.getProcessHospital().getId(), ListType.WORKFLOW_LIST);
                                Events.instance().setNewEvent(new DataActionEvent<>(newValue.getProcessHospital().getId(), ListType.WORKFLOW_LIST));
                            }
                        }
                    });
                }
//                    else {
//                        MainApp.showErrorMessageDialog(Lang.getProcessErrorOpen());
//                    }
            }
        });
        //CPX-2192:
        //change to simple show, dialog is always modal und should block the user input automaticaly
        //showAndWait will prevent further ui computation on screens 'under' it. This is also true for
        //other Dialogs! this Behavior resulted in disabling the whole client in some cases! Because the new dialog 
        //is not rendered properly!
        dialog.show();
    }

    private void openExistingProcess() {
        final AvailableProcessesDialog dialog = new AvailableProcessesDialog(Lang.getProcessesAvailablityDialog(), processes, caseId);
        dialog.getResultsProperty().addListener(new ChangeListener<ProcessEditingResult>() {
            @Override
            public void changed(ObservableValue<? extends ProcessEditingResult> observable, ProcessEditingResult oldValue, ProcessEditingResult newValue) {
                if (newValue != null && newValue.getProcessId() != 0) {
                    if (!newValue.isConfirmBeforeOpen()) {
                        //Needs no user confirmation
                        openProcess(dialog, newValue.getProcessId());
                        return;
                    }
                    Scene dialogScene = dialog.getDialogPane().getScene();
                    ConfirmDialog confirm = new ConfirmDialog(dialogScene == null ? MainApp.getWindow() : dialogScene.getWindow(), Lang.getProcessConfirm(String.valueOf(newValue.getWorkflowNumber())));
                    confirm.showAndWait().ifPresent(new Consumer<ButtonType>() {
                        @Override
                        public void accept(ButtonType t) {
                            if (t.equals(ButtonType.YES)) {
//                                    openItem(newValue.getId(), ListType.WORKFLOW_LIST);
                                openProcess(dialog, newValue.getProcessId());
                            } else {
                                dialog.getResultsProperty().set(null);
                            }
                        }
                    });
                } // else {
//                        MainApp.showErrorMessageDialog(Lang.getProcessErrorOpen());
//                    }
            }
        });
        //change to simple show to avoid freezing of the client due to 
        //stacked dialogs that use show and wait
        dialog.show();
    }

    private void openProcess(AvailableProcessesDialog pDialog, final long pProcessId) {
        pDialog.close();
        Events.instance().setNewEvent(new DataActionEvent<>(pProcessId, ListType.WORKFLOW_LIST));
    }
}
