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
package de.lb.cpx.client.app.wm.fx.process.section.details;

import de.lb.cpx.client.app.menu.fx.event.DataActionEvent;
import de.lb.cpx.client.app.menu.fx.event.Events;
import de.lb.cpx.client.app.menu.model.ListType;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.process.patient_health_status_details.AcgVisualisationWeb;
import de.lb.cpx.client.app.wm.fx.process.patient_health_status_details.PatientHealthStatusVisualization;
import de.lb.cpx.client.app.wm.fx.process.section.WmPatientSection;
import de.lb.cpx.client.app.wm.fx.process.section.operations.ItemEventHandler;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmCaseOperations;
import de.lb.cpx.client.app.wm.fx.process.section.operations.WmPatientOperations;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.model.fx.contextmenu.CtrlContextMenu;
import de.lb.cpx.client.core.model.fx.tableview.AsyncTableView;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TInsurance;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.TPatientDetails;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmProcess;
import de.lb.cpx.wm.model.enums.WmWorkflowStateEn;
import de.lb.cpx.wm.model.enums.WmWorkflowTypeEn;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javax.ejb.AsyncResult;

/**
 * Detailclass to expose detail pane to the history, avoid doublicate source
 * code
 *
 * @author wilde
 */
public class WmPatientDetails extends WmDetails<TPatient> {

    private static final Logger LOG = Logger.getLogger(WmPatientDetails.class.getName());

    public WmPatientDetails(ProcessServiceFacade pFacade, TPatient pItem) {
        super(pFacade, pItem);
    }

    private AsyncTableView<TWmProcess> processAsyncTableView;
//    public final static String HEALTH_STATUS_VIS_PANE_ID = "health_status_vis_pane";
//    private PatientHealthStatusVisualization phsViz;
    private AcgVisualisationWeb phsVizWeb;

//    private final PatientHealthStatusVisualization phsViz;
//    private StackPane phsVizThumbnailPane;
//    public WmPatientDetails(final ProcessServiceFacade pFacade, final TPatient pPatient) {
//        facade = pFacade;
//        patient = pPatient;
//    }
    @Override
    public String getDetailTitle() {
        return Lang.getEventNamePatient() + ": " + item.getPatNumber();
    }

//    @Override
//    protected Button createMenuItem() {
//        //NOT IMPLEMENTED YET
//        return null;
//    }
    @Override
    protected Parent getDetailNode() {
        VBox detailContent = new VBox();
        detailContent.setSpacing(10.0);
        TitledPane patientDetails = setUpPatientDetails();
        TitledPane casePane = setUpCaseTitledPane();
        TitledPane processPane = setUpProcessTitledPane();
        TitledPane insPane = setUpInsurancePane();
        TitledPane healthStatusPane = setUpHealthStatusVisualizationPane();

        Pane leftSpace = new Pane();
        Pane rightSpace = new Pane();
        HBox.setHgrow(leftSpace, Priority.ALWAYS);
        HBox.setHgrow(rightSpace, Priority.ALWAYS);
        HBox imageViewBox = new HBox();
//        imageViewBox.getChildren().addAll(leftSpace, detailPatientImage, rightSpace);
         if (CpxClientConfig.instance().getCommonHealthStatusVisualization()){
            detailContent.getChildren().addAll(patientDetails, casePane, processPane, insPane, 
            healthStatusPane, 
            imageViewBox);
         }else{
            detailContent.getChildren().addAll(patientDetails, casePane, processPane, insPane, 
//                healthStatusPane, 
                imageViewBox);
         }

//            WmDetailSection detail = new WmDetailSection();
        //detail.setContent(detailContent);
        ScrollPane scrollPane = new ScrollPane(detailContent);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        return scrollPane;
    }

    private TitledPane setUpPatientDetails() {
        TitledPane tpPatient = new TitledPane();
        tpPatient.setExpanded(!WmPatientSection.isPhsVizThumbnailClicked());
        tpPatient.setText(Lang.getGeneral());
        GridPane gpInfos = new GridPane();
        gpInfos.setHgap(10.0);
        gpInfos.setVgap(10.0);
        Label labelPatientFistnameText = new Label(Lang.getPatientFirstName());
        labelPatientFistnameText.getStyleClass().add("cpx-detail-label");
        Label labelPatientFistnameTextDetails = new Label(Lang.capitalize(item.getPatFirstName()));
        Label labelPatientLastnameText = new Label(Lang.getPatientLastName());
        labelPatientLastnameText.getStyleClass().add("cpx-detail-label");
        Label labelPatientLastnameDetails = new Label(Lang.capitalize(item.getPatSecName()));
        Label labelPatientBirthdayText = new Label(Lang.getDateOfBirth() + " - " + Lang.getAge());
        labelPatientBirthdayText.getStyleClass().add("cpx-detail-label");
        labelPatientBirthdayText.setWrapText(true);
        GridPane.setValignment(labelPatientBirthdayText, VPos.TOP);
        TCase baseCase = facade.getMainCase(facade.getCurrentProcess());
        Label labelPatientBirthdayDetails = new Label();
        if(baseCase!= null){
             TCaseDetails det = facade.getCurrentLocal(baseCase.getId());
            if(det != null){
                labelPatientBirthdayDetails.setText((item.getPatDateOfBirth() != null
                ? (Lang.toDate(item.getPatDateOfBirth()) + " - "):"") + (det.getCsdAgeYears() > 0?(det.getCsdAgeYears() + " " + Lang.getAgeYears()):(det.getCsdAgeDays() + " " + Lang.getAgeDays())));
                labelPatientBirthdayDetails.setTooltip(new Tooltip("am " + Lang.toDate(det.getCsdAdmissionDate())));
                
            }else{
                labelPatientBirthdayDetails.setText(item.getPatDateOfBirth() != null
                        ? Lang.toDate(item.getPatDateOfBirth()) + " - " + Lang.getElapsedTimeToNow(item.getPatDateOfBirth()).getYear() : "");
                
            }
        }else{
                labelPatientBirthdayDetails.setText(item.getPatDateOfBirth() != null
                        ? Lang.toDate(item.getPatDateOfBirth()) + " - " + Lang.getElapsedTimeToNow(item.getPatDateOfBirth()).getYear() : "");
                
        }
//        Label labelPatientBirthdayDetails = new Label(item.getPatDateOfBirth() != null
//                ? Lang.toDate(item.getPatDateOfBirth()) + " - " + Lang.getElapsedTimeToNow(item.getPatDateOfBirth()).getYear() : "");
        labelPatientBirthdayDetails.setWrapText(true);
        GridPane.setValignment(labelPatientBirthdayDetails, VPos.TOP);

        Label labelPatientAddressText = new Label(Lang.getAddress());
        labelPatientAddressText.getStyleClass().add("cpx-detail-label");
        Label labelPatientAddress = new Label(item.getPatDetailsActual().getPatdAddress());

        Label labelPatientZipCodeCityText = new Label(Lang.getAddressCity());
        labelPatientZipCodeCityText.getStyleClass().add("cpx-detail-label");
        TPatientDetails patDetailsActl = item.getPatDetailsActual();
        Label labelPatientZipCodeCity = new Label((patDetailsActl.getPatdZipcode() != null ? patDetailsActl.getPatdZipcode() : "")
                + (patDetailsActl.getPatdCity() != null ? " / " + patDetailsActl.getPatdCity() : ""));

        gpInfos.add(labelPatientFistnameText, 0, 0);
        gpInfos.add(labelPatientFistnameTextDetails, 1, 0);
        gpInfos.add(labelPatientLastnameText, 0, 1);
        gpInfos.add(labelPatientLastnameDetails, 1, 1);
        gpInfos.add(labelPatientBirthdayText, 0, 2);
        gpInfos.add(labelPatientBirthdayDetails, 1, 2);
        gpInfos.add(labelPatientAddressText, 0, 3);
        gpInfos.add(labelPatientAddress, 1, 3);
        gpInfos.add(labelPatientZipCodeCityText, 0, 4);
        gpInfos.add(labelPatientZipCodeCity, 1, 4);

        ColumnConstraints columnConstraintHalf = new ColumnConstraints();
        columnConstraintHalf.setPercentWidth(50);

        gpInfos.getColumnConstraints().add(columnConstraintHalf);

        tpPatient.setContent(gpInfos);

        return tpPatient;
    }

    private TitledPane setUpCaseTitledPane() {
        TitledPane pane = new TitledPane();
        pane.setExpanded(false);
        pane.setText(Lang.getPatientCases());
        ListView<TCase> caseList = new ListView<>();
        caseList.setItems(FXCollections.observableArrayList(item.getCases()));
        caseList.setCellFactory(new Callback<ListView<TCase>, ListCell<TCase>>() {
            @Override
            public ListCell<TCase> call(ListView<TCase> param) {
                return new ListCell<TCase>() {
                    @Override
                    public void updateItem(TCase item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText("");
                            return;
                        }

                        setText(item.getCsCaseNumber());
                    }
                };
            }
        });

        caseList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() >= 2) {
                    TCase item = caseList.getSelectionModel().getSelectedItem();
                    //AWi-20170809-CPX-600:
                    //checks if locked and item is null 
                    if (item == null) {
                        return;
                    }
//                    if (m_serviceFacade.isLocked(item.getId())) {
//                        MainApp.showErrorMessageDialog(Lang.getCaseLockedObj().getTooltip());
//                        return;
//                    }
                    //facade.loadAndShow(TwoLineTab.TabType.CASE, item.getId());
                    ItemEventHandler eh = new WmCaseOperations(facade).openItem(item);
                    if (eh != null) {
                        eh.handle(null);
                    }
                }
            }
        });

        pane.setContent(caseList);
        return pane;
    }

    @SuppressWarnings("unchecked")
    private TitledPane setUpProcessTitledPane() {
        TitledPane pane = new TitledPane();
        pane.setExpanded(false);
        pane.setText("Vorgänge");

        List<TWmProcess> processes = facade.findWorkflowsByPatient(item);
        ObservableList<TWmProcess> oListProcesses = FXCollections.observableArrayList(processes);

        processAsyncTableView = new AsyncTableView<TWmProcess>() {
            @Override
            public Future<List<TWmProcess>> getFuture() {
                return new AsyncResult<>(oListProcesses);
            }
        };
        processAsyncTableView.reload();
        processAsyncTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//        processAsyncTableView.setMaxHeight(400);
//        HBox.setHgrow(processAsyncTableView, Priority.ALWAYS);
//        VBox.setVgrow(processAsyncTableView, Priority.ALWAYS);

        oListProcesses.addListener(new ListChangeListener<TWmProcess>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends TWmProcess> c) {
                processAsyncTableView.reload();
            }
        });

        TableColumn<TWmProcess, Long> col1 = new TableColumn<>("Nummer");
        col1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TWmProcess, Long>, ObservableValue<Long>>() {
            @Override
            public ObservableValue<Long> call(TableColumn.CellDataFeatures<TWmProcess, Long> param) {
                return new SimpleObjectProperty<>(param.getValue().getWorkflowNumber());
            }
        });
        col1.setCellFactory(new Callback<TableColumn<TWmProcess, Long>, TableCell<TWmProcess, Long>>() {
            @Override
            public TableCell<TWmProcess, Long> call(TableColumn<TWmProcess, Long> param) {
                return new TableCell<TWmProcess, Long>() {
                    @Override
                    protected void updateItem(Long item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText("");
                            return;
                        }
                        setText(String.valueOf(item));
                    }

                };
            }
        });

        TableColumn<TWmProcess, Date> col2 = new TableColumn<>("Erstellt");
        col2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TWmProcess, Date>, ObservableValue<Date>>() {
            @Override
            public ObservableValue<Date> call(TableColumn.CellDataFeatures<TWmProcess, Date> param) {
                return new SimpleObjectProperty<>(param.getValue().getCreationDate());
            }
        });
        col2.setCellFactory(new Callback<TableColumn<TWmProcess, Date>, TableCell<TWmProcess, Date>>() {
            @Override
            public TableCell<TWmProcess, Date> call(TableColumn<TWmProcess, Date> param) {
                return new TableCell<TWmProcess, Date>() {
                    @Override
                    protected void updateItem(Date item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText("");
                            return;
                        }
                        setText(Lang.toDate(item));
                    }

                };
            }
        });

        TableColumn<TWmProcess, WmWorkflowStateEn> col3 = new TableColumn<>("Status");
        col3.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TWmProcess, WmWorkflowStateEn>, ObservableValue<WmWorkflowStateEn>>() {
            @Override
            public ObservableValue<WmWorkflowStateEn> call(TableColumn.CellDataFeatures<TWmProcess, WmWorkflowStateEn> param) {
                return new SimpleObjectProperty<>(param.getValue().getWorkflowState());
            }
        });
        col3.setCellFactory(new Callback<TableColumn<TWmProcess, WmWorkflowStateEn>, TableCell<TWmProcess, WmWorkflowStateEn>>() {
            @Override
            public TableCell<TWmProcess, WmWorkflowStateEn> call(TableColumn<TWmProcess, WmWorkflowStateEn> param) {
                return new TableCell<TWmProcess, WmWorkflowStateEn>() {
                    @Override
                    protected void updateItem(WmWorkflowStateEn item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText("");
                            return;
                        }
                        setText(item.getTranslation().value);
                    }

                };
            }
        });

        TableColumn<TWmProcess, WmWorkflowTypeEn> col4 = new TableColumn<>("Typ");
        col4.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TWmProcess, WmWorkflowTypeEn>, ObservableValue<WmWorkflowTypeEn>>() {
            @Override
            public ObservableValue<WmWorkflowTypeEn> call(TableColumn.CellDataFeatures<TWmProcess, WmWorkflowTypeEn> param) {
                return new SimpleObjectProperty<>(param.getValue().getWorkflowType());
            }
        });
        col4.setCellFactory(new Callback<TableColumn<TWmProcess, WmWorkflowTypeEn>, TableCell<TWmProcess, WmWorkflowTypeEn>>() {
            @Override
            public TableCell<TWmProcess, WmWorkflowTypeEn> call(TableColumn<TWmProcess, WmWorkflowTypeEn> param) {
                return new TableCell<TWmProcess, WmWorkflowTypeEn>() {
                    @Override
                    protected void updateItem(WmWorkflowTypeEn item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText("");
                            return;
                        }
                        setText(item.getTranslation().value);
                    }
                };
            }
        });

        processAsyncTableView.getColumns().addAll(col1, col2, col3, col4);

//        processAsyncTableView.setRowFactory(new Callback<TableView<TWmProcess>, TableRow<TWmProcess>>() {
//            @Override
//            public TableRow<TWmProcess> call(TableView<TWmProcess> tableView) {
//                final TableRow<TWmProcess> row = new TableRow<>();
//                final ContextMenu contextMenu = new CtrlContextMenu();
//
//                final MenuItem openMenuItem = new MenuItem("Vorgang öffnen");
//                openMenuItem.setOnAction(new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//                        TWmProcess process = row.getItem();
//                        if (process != null) {
//                            Events.instance().setNewEvent(new DataActionEvent<>(process.getId(), ListType.WORKFLOW_LIST));
//                        }
//                    }
//                });
//                contextMenu.getItems().addAll(openMenuItem);
//
//                // Set context menu on row, but use a binding to make it only show for non-empty rows:  
//                row.contextMenuProperty().bind(
//                        Bindings.when(row.emptyProperty())
//                        .then((ContextMenu) null)
//                        .otherwise(Bindings.when(getArmedProperty()).then(contextMenu).otherwise((ContextMenu) null))
//                );
//
//                row.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                    @Override
//                    public void handle(MouseEvent event) {
//                        if (MouseButton.PRIMARY.equals(event.getButton())) {
//                            invalidateRequestDetailProperty();
//                        }
//                        if (MouseButton.PRIMARY.equals(event.getButton()) && event.getClickCount() == 2) {
//                            //Open a process on double click
//                            openMenuItem.fire();
//                        }
//                    }
//                });
//                return row;
//            }
//        });
        processAsyncTableView.setOnRowClick(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (MouseButton.PRIMARY.equals(event.getButton()) && event.getClickCount() == 2) {
                    //Open a process on double click
                    handleProcessOpeningEvent();
                }
            }
        });

        processAsyncTableView.setRowContextMenu(createContextMenu());
//        processAsyncTableView.showContextMenuProperty().bind(getArmedProperty()); -> //2019-11-07 DNi: important enough?!

        pane.setContent(processAsyncTableView);
        return pane;
    }

    private TitledPane setUpInsurancePane() {
        TitledPane pane = new TitledPane();
        pane.setExpanded(false);
        pane.setText(Lang.getInsuranceData());
        ListView<TInsurance> insList = new ListView<>();
        insList.setItems(FXCollections.observableArrayList(item.getInsurances()));
        insList.setCellFactory(new Callback<ListView<TInsurance>, ListCell<TInsurance>>() {
            @Override
            public ListCell<TInsurance> call(ListView<TInsurance> param) {
                return new ListCell<TInsurance>() {
                    @Override
                    public void updateItem(TInsurance item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText("");
                            return;
                        }
                        setText(item.getInsNumber() != null ? item.getInsNumber() : "");
                        setTooltip(new Tooltip(facade.findInsuranceToolTipText(item.getInsInsuranceCompany())));
                        if (item.getInsIsActualFl()) {
                            setText(getText().concat(" (" + Lang.getActual() + ")"));
                        }
                    }
                };
            }
        });

        pane.setContent(insList);
        return pane;
    }

    private TitledPane setUpHealthStatusVisualizationPane() {
        if (item == null) {
            LOG.log(Level.WARNING, "m_currentPatient is null!");
        }
        TitledPane pane = new TitledPane();
//        pane.setId(HEALTH_STATUS_VIS_PANE_ID);
        pane.setExpanded(WmPatientSection.isPhsVizThumbnailClicked());
        pane.setAnimated(false); //otherwise event for visualization will be fired multiple times
        pane.setText("Gesundheitszustand"); //Patient Health Status

        //pane.setMinHeight(600d);
//        pane.setMaxHeight(600d);

//        if (phsViz != null) {
//            //TitledPane pane = (TitledPane) detailContent.getScene().lookup("#" + WmPatientDetails.HEALTH_STATUS_VIS_PANE_ID);
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    VBox vbox = phsViz.getCompactContent();
//                    vbox.prefWidthProperty().bind(pane.widthProperty());
//                    pane.setContent(vbox);
//                }
//            });
//        }

        if (phsVizWeb != null) {
            //TitledPane pane = (TitledPane) detailContent.getScene().lookup("#" + WmPatientDetails.HEALTH_STATUS_VIS_PANE_ID);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    VBox vbox = phsVizWeb.getContent(getFacade().checkAcgConnection());
                    phsVizWeb.getMouseDoubleClick().addListener(new ChangeListener<Boolean> (){
                       @Override
                       public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            openPatient();
                       };
                   }); 

                    vbox.prefWidthProperty().bind(pane.widthProperty());
                    pane.setContent(vbox);
                }
            });
        }

        //vbox.setStyle("-fx-padding: 0 0 0 -8px");
        VBox.setVgrow(pane, Priority.ALWAYS);
        HBox.setHgrow(pane, Priority.ALWAYS);
        return pane;
    }

    public void openPatient() {
        //facade.loadAndShow(TwoLineTab.TabType.PATIENT, currentPatient.getId());
        ItemEventHandler eh = new WmPatientOperations(facade).openItem(item);
        if (eh != null) {
            eh.handle(null);
        }
    }

    private ContextMenu createContextMenu() {
        final ContextMenu contextMenu = new CtrlContextMenu<>();

        final MenuItem openMenuItem = new MenuItem("Vorgang öffnen");
        openMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleProcessOpeningEvent();
            }
        });
        contextMenu.getItems().addAll(openMenuItem);

        return contextMenu;
    }

    private void handleProcessOpeningEvent() {
        TWmProcess process = processAsyncTableView.getSelectionModel().getSelectedItem();
        if (process != null) {
            Events.instance().setNewEvent(new DataActionEvent<>(process.getId(), ListType.WORKFLOW_LIST));
        }
    }

//    public void setPhsViz(final PatientHealthStatusVisualization pPhsViz) {
//        this.phsViz = pPhsViz;
//    }

    public void setPhsVizWeb(AcgVisualisationWeb phsVizWeb) {
        this.phsVizWeb = phsVizWeb;
    }

    @Override
    public WmPatientOperations getOperations() {
        return new WmPatientOperations(facade);
    }

}
