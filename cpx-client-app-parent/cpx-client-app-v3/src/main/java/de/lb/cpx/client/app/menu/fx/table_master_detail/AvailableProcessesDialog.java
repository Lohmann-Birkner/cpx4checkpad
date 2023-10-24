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
package de.lb.cpx.client.app.menu.fx.table_master_detail;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.cm.fx.simulation.menu.ProcessEditingResult;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.dialog.CreateProcessDialog;
import de.lb.cpx.client.core.model.cell.SimpleCellValueFactory;
import de.lb.cpx.client.core.model.fx.dialog.FormularDialog;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmProcess;
import de.lb.cpx.wm.model.TWmRequest;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * To show all created processes for particular case. User can create a new
 * process and request or use any existing process and create a request.
 *
 * @author nandola
 */
public class AvailableProcessesDialog extends FormularDialog<ProcessEditingResult> {
//  protected final ProcessServiceFacade facade;

//    protected final Dialog dialog;
    private static final Logger LOG = Logger.getLogger(AvailableProcessesDialog.class.getSimpleName());
    //private final LabeledComboBox<TWmProcess> ListofProcesses;
    private final TableView<TWmProcess> tvOfProcesses;

    public AvailableProcessesDialog(String dialogName, List<TWmProcess> processes, long hCaseId) {
        super(MainApp.getStage(), Modality.APPLICATION_MODAL, dialogName);
        Stage s = (Stage) this.getDialogPane().getScene().getWindow();
        s.setMaxHeight(100);
        s.setMaxWidth(475);

        //return Lang.getProcessNumber() + ": " + object.getWorkflowNumber() + ", " + Lang.getProcessDate() + ": " + formatter.format(object.getCreationDate());
        tvOfProcesses = buildProcessTable(processes, hCaseId);

        tvOfProcesses.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (MouseButton.PRIMARY == event.getButton()
                        && event.getClickCount() == 2) {
                    TWmProcess proc = tvOfProcesses.getSelectionModel().getSelectedItem();
                    if (proc == null) {
                        return;
                    }
                    LOG.log(Level.INFO, "Selected process number is {0}", String.valueOf(proc.getWorkflowNumber()));
                    boolean confirmBeforeOpen = true;
                    setResult(tvOfProcesses.getSelectionModel().getSelectedItem(), confirmBeforeOpen);
                }
            }
        });

//        tvOfProcesses = new LabeledComboBox<>(Lang.getProcessesAvailablity() + " : " + processes.size());
//        tvOfProcesses.setItems(processes);
//        tvOfProcesses.setConverter(new StringConverter<TWmProcess>() {
//            @Override
//            public String toString(TWmProcess object) {
////                Calendar cal = Calendar.getInstance(); // locale-specific
////                cal.setTime(object.getCreationDate());
////                cal.set(Calendar.SECOND, 0);
////                cal.set(Calendar.MILLISECOND, 0);
////                Date date = cal.getTime();
////                Date truncatedDate = DateUtils.truncate(object.getCreationDate(), Calendar.DATE);
//
////                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
//                SimpleDateFormat formatter = new SimpleDateFormat(Lang.getProcessListDateFormat());
////              
//                return Lang.getProcessNumber() + ": " + object.getWorkflowNumber() + ", " + Lang.getProcessDate() + ": " + formatter.format(object.getCreationDate());
////                return Lang.getProcessNumber() + ": " + object.getId() + ", " + Lang.getProcessDate() + ": " + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(object.getCreationDate());
////                return Lang.getProcessNumber() + ": " + object.getId() + ", " + Lang.getProcessDate() + ": " + object.getCreationDate() + ", WFType: " + object.getWorkflowType().toString() + ", WFState: " + object.getWorkflowState().toString();
//            }
//
//            @Override
//            public TWmProcess fromString(String string) {
//                return null;
//            }
//        });
        addControls(tvOfProcesses);

        Button openProcessBtn = getDialogSkin().getButton(ButtonType.OK);
        openProcessBtn.setText(Lang.getCaseOpenProcess());
//        ok.setDisable(true);

        //Don't fu**ing close the dialog when user clicks on 'OK' (or 'Vorgang öffnen' as we call it here)!
        getDialogPane().lookupButton(ButtonType.OK).addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    // to prevent the dialog to close
                    event.consume();
                    boolean confirmBeforeOpen = false;
                    setResult(tvOfProcesses.getSelectionModel().getSelectedItem(), confirmBeforeOpen);
                }
        );

        getDialogSkin().addButtonTypes(ButtonType.NEXT);
        Button createProcess = getDialogSkin().getButton(ButtonType.NEXT);
        createProcess.setText(Lang.getProcessCreateButton());
        createProcess.setAlignment(Pos.BOTTOM_LEFT);

//        tvOfProcesses.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TWmProcess>() {
//            @Override
//            public void changed(ObservableValue<? extends TWmProcess> observable, TWmProcess oldValue, TWmProcess newValue) {
////                if (ListofProcesses.getSelectedItemProperty().isNotNull().equals(true)) {
////                if (tvOfProcesses.getControl().getSelectionModel().isEmpty()) {
//////                    createProcess.setDisable(false);
////                    ok.setDisable(true);
////                } else {
//////                    createProcess.setDisable(true);
//////                    ok.setDisable(false);
////                }
//            }
//        });
        // to set first item as selected in comboBox.
        //tvOfProcesses.getControl().getSelectionModel().selectFirst();
//        ok.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
////                System.out.println("pressed OK button");
//                if (tvOfProcesses.getSelectionModel().getSelectedItem() != null) {
//                    TWmProcess process = wmServiceBean.get().findProcess(tvOfProcesses.getSelectionModel().getSelectedItem().id, true, TWmProcessHospital.class);
//                    // needs to pass processId to create new request for that process
////                    System.out.println("process.id: " + process.id);
//                    AddRequestDialog dialog = null;
//                    try {
//                        dialog = new AddRequestDialog(new ProcessServiceFacade(process.id), hCaseId);
//                    } catch (CpxIllegalArgumentException ex) {
//                        LOG.log(Level.SEVERE, "Cannot open request dialog", ex);
//                        MainApp.showErrorMessageDialog(ex);
//                        return;
//                    }
////                    AddRequestDialog dialog = new AddRequestDialog(new ProcessServiceFacade(process.id));
//                    dialog.show();
//                    //AWi-20170612-CPX-542:
//                    //observe result of the dialog
//                    dialog.getResultsProperty().addListener(new ChangeListener<TWmRequest>() {
//                        @Override
//                        public void changed(ObservableValue<? extends TWmRequest> observable, TWmRequest oldValue, TWmRequest newValue) {
//                            if (newValue != null) {
//                                getResultsProperty().set(newValue.getProcessHospital());
//                            }
//                        }
//                    });
//                } else {
////                    System.out.println("Please select any process to create new request for that process");
//                }
//            }
//        });
        createProcess.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                System.out.println("pressed createProcess button");
//                if (ListofProcesses.getSelectedItem() == null) {
                // It will create new process and request.
                CreateProcessDialog dialog = null;
                try {
                    dialog = new CreateProcessDialog(Lang.getWmRequestcreationTitle(), hCaseId, new ProcessServiceFacade(-1L));
                } catch (CpxIllegalArgumentException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                    MainApp.showErrorMessageDialog(ex);
                    return;
                }
                dialog.show();
                //AWi-20170612-CPX-542:
                //observe result of the dialog
                dialog.getResultsProperty().addListener(new ChangeListener<TWmRequest>() {
                    @Override
                    public void changed(ObservableValue<? extends TWmRequest> observable, TWmRequest oldValue, TWmRequest newValue) {
                        if (newValue != null) {
                            boolean confirmBeforeOpen = true;
                            setResult(newValue.getProcessHospital(), confirmBeforeOpen);
                        }
                    }
                });
//                } else {
//                    createProcess.setDisable(true);
//                }
            }
        });
    }

    private void setResult(TWmProcess pProcess, final boolean pConfirmBeforeOpen) {
        ProcessEditingResult result = new ProcessEditingResult(pProcess, pConfirmBeforeOpen);
        getResultsProperty().set(result);
    }

    @Override
    public ProcessEditingResult onSave() {
        return null;
    }

    @SuppressWarnings("unchecked")
    private TableView<TWmProcess> buildProcessTable(final List<TWmProcess> pProcesses, final long pCaseId) {
//        final TableColumn colIsMainCase = new TableColumn("");
//        colIsMainCase.setCellValueFactory(new SimpleCellValueFactory<>());
//        colIsMainCase.setCellFactory(new Callback<TableColumn<Object, TWmProcess>, TableCell<Object, TWmProcess>>() {
//            @Override
//            public TableCell<Object, TWmProcess> call(TableColumn<Object, TWmProcess> param) {
//                TableCell<Object, TWmProcess> cell = new TableCell<>() {
//
//                    @Override
//                    protected void updateItem(Object item, boolean empty) {
//                        super.updateItem(item, empty);
//                        if (item == null) {
//                            setText("");
//                            return;
//                        }
//                        TWmProcess proc = (TWmProcess) item;
//                        
//                        boolean isMainCase = proc.getMainCase().id == pCaseId;
//                        //setText(proc.getProcessCases().getMainCase() ? "B" : ""));
//                        setText(isMainCase ? "B" : "");
//                    }
//                };
//                return cell;
//            }
//        });

        final TableColumn<TWmProcess, TWmProcess> colWorkflowNumber = new TableColumn<>(Lang.getProcessNumber());
        final SimpleDateFormat formatter = new SimpleDateFormat(Lang.getProcessListDateFormat());
        colWorkflowNumber.setCellValueFactory(new SimpleCellValueFactory<>());
        colWorkflowNumber.setCellFactory(new Callback<TableColumn<TWmProcess, TWmProcess>, TableCell<TWmProcess, TWmProcess>>() {
            @Override
            public TableCell<TWmProcess, TWmProcess> call(TableColumn<TWmProcess, TWmProcess> param) {
                TableCell<TWmProcess, TWmProcess> cell = new TableCell<>() {

                    @Override
                    protected void updateItem(TWmProcess item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setText("");
                            return;
                        }
                        TWmProcess proc = item;
                        setText(String.valueOf(proc.getWorkflowNumber()));
                    }
                };
                return cell;
            }
        });

        final TableColumn<TWmProcess, TWmProcess> colProcessDate = new TableColumn<>(Lang.getProcessDate());
        //colProcessDate.setPrefWidth(200d);
        colProcessDate.setCellValueFactory(new SimpleCellValueFactory<>());
        colProcessDate.setCellFactory(new Callback<TableColumn<TWmProcess, TWmProcess>, TableCell<TWmProcess, TWmProcess>>() {
            @Override
            public TableCell<TWmProcess, TWmProcess> call(TableColumn<TWmProcess, TWmProcess> param) {
                TableCell<TWmProcess, TWmProcess> cell = new TableCell<>() {

                    @Override
                    protected void updateItem(TWmProcess item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setText("");
                            return;
                        }
                        TWmProcess proc = item;
                        setText(formatter.format(proc.getCreationDate()));
                    }
                };
                return cell;
            }
        });

        ObservableList<TWmProcess> obsProcesses = FXCollections.observableArrayList(pProcesses);
        final TableView<TWmProcess> tv = new TableView<>(obsProcesses);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tv.getColumns().addAll(/* colIsMainCase, */colWorkflowNumber, colProcessDate);
        tv.setMaxHeight(150d);
        tv.requestFocus();
        tv.getSelectionModel().selectFirst();
        tv.getStyleClass().add("stay-selected-table-view");
        return tv;
    }

}
