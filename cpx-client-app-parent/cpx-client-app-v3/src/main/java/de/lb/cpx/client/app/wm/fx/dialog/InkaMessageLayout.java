/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018 nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.dialog;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.alert.NotificationsFactory;
import de.lb.cpx.client.core.model.fx.contextmenu.CtrlContextMenu;
import de.lb.cpx.client.core.model.fx.dialog.FormularDialog;
import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledDatePicker;
import de.lb.cpx.client.core.model.fx.labeled.LabeledLabel;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextArea;
import de.lb.cpx.client.core.model.fx.tableview.AsyncTableView;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.kaininka.dto.InkaPvvOverviewDTO;
import de.lb.cpx.model.TP301Inka;
import de.lb.cpx.model.TP301KainInka;
import de.lb.cpx.model.TP301KainInkaPvt;
import de.lb.cpx.model.TP301KainInkaPvv;
import de.lb.cpx.model.enums.CpxEnumInterface;
import de.lb.cpx.service.ejb.ProcessServiceBeanRemote;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.enums.Tp301Key30En;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.ejb.AsyncResult;
import javax.ejb.EJBException;
import org.controlsfx.control.Notifications;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

/**
 * Common properties and controls of Inka message dialogs.
 *
 * @author nandola
 */
public class InkaMessageLayout extends FormularDialog<TP301KainInka> {
//public class InkaMessageLayout {

    private static final Logger LOG = Logger.getLogger(InkaMessageLayout.class.getName());
    private ProcessServiceFacade serviceFacade;
    private LabeledLabel lbCaseData;
    private Label ltCaseNo;
    private Label ltIkz;
    private Label ltPatNo;
    private Label ltVersIk;
    private Label ltCaseNoValue;
    private Label ltIkzValue;
    private Label ltPatNoValue;
    private Label ltVersIkValue;
    private LabeledLabel lbPvvInfo;
    private LabeledComboBox<Tp301Key30En> cbTp301Key30;
//    private LabeledComboBox<String> cbBillNo;
    private LabeledComboBox<TP301KainInkaPvv> cbBillNo;
    private Button btnNewPvv;
    private Button btnUpdatePvv;
    private LabeledDatePicker ldBillDate;
    private LabeledTextArea ltaFreeText;
    private AsyncTableView<InkaPvvOverviewDTO> pvvDetailsTableView;
//    private Set<TP301KainInkaPvv> setOfPvvs;
    private List<TP301KainInkaPvv> setOfPvvs;
//    private Set<TP301KainInkaPvt> setOfPvts;
    private List<TP301KainInkaPvt> setOfPvts;
    private final ObservableList<TP301KainInkaPvv> listOfPvv = FXCollections.observableArrayList();
    private final ObservableList<InkaPvvOverviewDTO> listOfAllPvvOverviewDTO = FXCollections.observableArrayList();
    private ObservableList<InkaPvvOverviewDTO> listOfInkaPvvOverviewDTO;
    private SortedList<InkaPvvOverviewDTO> sortedList;
    private final List<List<InkaPvvOverviewDTO>> listOfInkaPvvOverviewDtoLists = new ArrayList<>();
    private final EjbProxy<ProcessServiceBeanRemote> processServiceBean;
    private TP301KainInka kainInka;
    private TP301KainInka newKainInka;
    private TP301Inka inka;

    public static final int COMMENT_MAX_CHARS = 6400;

    public InkaMessageLayout(ProcessServiceFacade pServiceFacade) {
        this(pServiceFacade, null);
    }

    public InkaMessageLayout(ProcessServiceFacade pServiceFacade, final TP301KainInka kainInka) {
        super(MainApp.getStage(), Modality.APPLICATION_MODAL, "INKA-Nachricht");

        processServiceBean = Session.instance().getEjbConnector().connectProcessServiceBean();

        sortedList = new SortedList<>(listOfAllPvvOverviewDTO);

        listOfAllPvvOverviewDTO.addListener(new ListChangeListener<InkaPvvOverviewDTO>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends InkaPvvOverviewDTO> c) {
                sortedList = new SortedList<>(listOfAllPvvOverviewDTO, new Comparator<InkaPvvOverviewDTO>() {
                    @Override
                    public int compare(InkaPvvOverviewDTO o1, InkaPvvOverviewDTO o2) {
                        if (listOfPvv.indexOf(o1.getPvv()) > listOfPvv.indexOf(o2.getPvv())) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                }
                );
            }
        });

        serviceFacade = pServiceFacade;
        this.kainInka = kainInka; // either type of KAIN (kainReceived event type) or INKA (inkaStored event type)

        if (kainInka.getMessageType().equals("KAIN")) {

        } else if (kainInka.getMessageType().equals("INKA")) {
            // Need to find a proper way to get ALL PVTs for a PVV.
            this.kainInka.getKainInkaPvvs().forEach(new Consumer<TP301KainInkaPvv>() {
                @Override
                public void accept(TP301KainInkaPvv pvv) {
//                    pvv.setKainInkaPvts(new HashSet<>(processServiceBean.getAllPvtsForPvv(pvv.getId())));
                    if (pvv != null && pvv.getId() > 0L) {
                        pvv.setKainInkaPvts(processServiceBean.get().getAllPvtsForPvv(pvv.getId()));
                    }
                }
            });
        }

//        Pane setUpLayoutForInkaMsg = setUpLayoutForInkaMsg(pServiceFacade, kainInka);
//        addControlGroup(setUpLayoutForInkaMsg);
        getDialogPane().sceneProperty().get().getWindow().centerOnScreen();
//        ButtonType sendBtnType = new ButtonType("Senden");
//        ButtonType saveBtnType = new ButtonType("Speichern");
//        getDialogPane().getButtonTypes().addAll(sendBtnType);
//        getDialogSkin().getButton(sendBtnType).disableProperty().bind(Bindings.isEmpty(listOfPvv));
//        getDialogSkin().getButton(saveBtnType).disableProperty().bind(Bindings.isEmpty(listOfPvv));

//        getDialogSkin().getButton(ButtonType.OK).disableProperty().bind(Bindings.isEmpty(listOfPvv));
//        getDialogPane().getButtonTypes().remove(ButtonType.OK);
//        getDialogSkin().getButton(ButtonType.OK).setText("Speichern");
        //can't create more than 10 PVV segments per INKA msg
        listOfPvv.addListener(new ListChangeListener<TP301KainInkaPvv>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends TP301KainInkaPvv> c) {
                if (c.getList().size() >= 10) {
                    btnNewPvv.setDisable(true);
                } else {
                    btnNewPvv.setDisable(false);
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    public Pane setUpLayoutForInkaMsg(ProcessServiceFacade pServiceFacade, TP301KainInka kainInka) {
        VBox vbox = new VBox();
        vbox.setSpacing(5);

        HBox HcaseData = new HBox();
        lbCaseData = new LabeledLabel("Falldaten");
        lbCaseData.setStyle("-fx-font-size: 15px;");
        lbCaseData.setStyle("-fx-font-weight: bold;");
        HBox.setHgrow(HcaseData, Priority.ALWAYS);
        HcaseData.getChildren().addAll(lbCaseData);

        GridPane gpInfos = new GridPane();
        gpInfos.setPadding(new Insets(-15, 0, 0, 0));
        gpInfos.setVgap(5.0);
        gpInfos.setHgap(5.0);

        ltCaseNo = new Label("Fall-Nr");
        HBox.setHgrow(ltCaseNo, Priority.ALWAYS);
        ltCaseNo.getStyleClass().add("cpx-detail-label");
        // Pna: 30.09.19 CPX-1989 (Information is not in the correct position on the display of the INKA message)
//        ltCaseNoValue = new Label(kainInka.getInsuranceCaseNumber() == null ? "" : kainInka.getInsuranceCaseNumber());
        ltCaseNoValue = new Label(kainInka.getHospitalNumberPatient());
        HBox.setHgrow(ltCaseNoValue, Priority.ALWAYS);
        ltCaseNoValue.setWrapText(true);

        ltIkz = new Label("IKZ");
        HBox.setHgrow(ltIkz, Priority.ALWAYS);
        ltIkz.getStyleClass().add("cpx-detail-label");
        ltIkzValue = new Label(kainInka.getHospitalIdentifier());
        HBox.setHgrow(ltIkzValue, Priority.ALWAYS);
        ltIkzValue.setWrapText(true);

        AnchorPane a1 = new AnchorPane();
        a1.minWidth(100);
        HBox.setHgrow(a1, Priority.ALWAYS);
        AnchorPane a2 = new AnchorPane();
        a2.minWidth(100);
        HBox.setHgrow(a2, Priority.ALWAYS);

        ltPatNo = new Label("Pat.-Nr");
        HBox.setHgrow(ltPatNo, Priority.ALWAYS);
        ltPatNo.getStyleClass().add("cpx-detail-label");
//        ltPatNoValue = new Label(kainInka.getHospitalNumberPatient());    // this field is a Casenumber (CPX-1989, test by Lars)
        ltPatNoValue = new Label(pServiceFacade.getPatient().getPatNumber());
        HBox.setHgrow(ltPatNoValue, Priority.ALWAYS);
        ltPatNoValue.setWrapText(true);

        ltVersIk = new Label("Vers.-Ik");
        HBox.setHgrow(ltVersIk, Priority.ALWAYS);
        ltVersIk.getStyleClass().add("cpx-detail-label");
        ltVersIkValue = new Label(kainInka.getInsuranceIdentifier());
        HBox.setHgrow(ltVersIkValue, Priority.ALWAYS);
        ltVersIkValue.setWrapText(true);

        gpInfos.add(ltCaseNo, 0, 0);
        gpInfos.add(ltCaseNoValue, 1, 0);
//        gpInfos.add(a1, 2, 0);
        gpInfos.add(ltIkz, 3, 0);
        gpInfos.add(ltIkzValue, 4, 0);
        gpInfos.add(ltPatNo, 0, 1);
        gpInfos.add(ltPatNoValue, 1, 1);
//        gpInfos.add(a3, 2, 1);
        gpInfos.add(ltVersIk, 3, 1);
        gpInfos.add(ltVersIkValue, 4, 1);

        lbPvvInfo = new LabeledLabel("PVV Information");
        lbPvvInfo.setStyle("-fx-font-size: 15px;");
        lbPvvInfo.setStyle("-fx-font-weight: bold;");

        cbTp301Key30 = new LabeledComboBox<>("Info PrüfvV");
        cbTp301Key30.setPadding(new Insets(-15, 0, 0, 0));
        int admYear = pServiceFacade.getCurrentKisDetailsVersion() == null?0:pServiceFacade.getCurrentKisDetailsVersion().getCsdAdmissionYear();
        for (Tp301Key30En e : Tp301Key30En.values()) {
            if (e.isInka() && admYear >= e.getFirstYear() && admYear <= e.getLastYear()) {
                cbTp301Key30.getItems().add(e);
            }
        }
//        cbTp301Key30.setItems(Tp301Key30.values());
        cbTp301Key30.getControl().getItems().addListener(new ListChangeListener<Tp301Key30En>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Tp301Key30En> c) {
                if (cbTp301Key30.getSelectedItem() != null) {
                    btnNewPvv.setDisable(true);
                } else {
                    btnNewPvv.setDisable(false);
                }
            }
        });

        HBox hbox = new HBox();
        hbox.setSpacing(12.0);
        HBox.setHgrow(hbox, Priority.ALWAYS);
        cbBillNo = new LabeledComboBox<>("Rechnungsnummer");
        HBox.setHgrow(cbBillNo, Priority.ALWAYS);

        VBox vbBtnNewPvv = new VBox();
        vbBtnNewPvv.setPadding(new Insets(20, 0, 0, 0));
        btnNewPvv = new Button("neues PVV");
        btnNewPvv.setPadding(new Insets(10, 0, 0, 0));
        HBox.setHgrow(btnNewPvv, Priority.ALWAYS);
        vbBtnNewPvv.getChildren().add(btnNewPvv);

        VBox vbBtnUpdatePvv = new VBox();
        vbBtnUpdatePvv.setPadding(new Insets(20, 0, 0, 0));
        btnUpdatePvv = new Button("Update PVV");
        btnUpdatePvv.setPadding(new Insets(10, 0, 0, 0));
        HBox.setHgrow(btnUpdatePvv, Priority.ALWAYS);
        vbBtnUpdatePvv.getChildren().add(btnUpdatePvv);
        btnUpdatePvv.setDisable(true);

        ldBillDate = new LabeledDatePicker("Rechnungsdatum");
        HBox.setHgrow(ldBillDate, Priority.ALWAYS);
        ldBillDate.getControl().setDisable(true);
//        ldBillDate.setDate();
        AnchorPane a3 = new AnchorPane();
        HBox.setHgrow(a3, Priority.ALWAYS);
        AnchorPane a4 = new AnchorPane();
        HBox.setHgrow(a4, Priority.ALWAYS);
        AnchorPane a5 = new AnchorPane();
        HBox.setHgrow(a5, Priority.ALWAYS);
        hbox.getChildren().addAll(cbBillNo, a3, vbBtnNewPvv, a4, vbBtnUpdatePvv, a5, ldBillDate);

        ltaFreeText = new LabeledTextArea("Freitext");
        ltaFreeText.setPrefHeight(150);
        ltaFreeText.setMaxSize(COMMENT_MAX_CHARS);
        //Pna: 12.03.2019 (show warning message in case of user enters Invalid characters)
        ltaFreeText.getControl().setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String character = event.getCharacter();
                if (character.equals("+") || character.equals("'") || character.equals("#") || character.equals(":")) {
                    MainApp.showWarningMessageDialog("Einige Trennzeichen ( : , ' , # , + ) sind hier nicht zulässig..", getDialogPane().sceneProperty().get().getWindow());
                    String text = ltaFreeText.getText();
                    text = text.substring(0, text.length() - 1);  // remove invalid (last) char
                    ltaFreeText.setText(text);
                    ltaFreeText.setCaretPosition(ltaFreeText.getText().length());
                }
                if (character.equals("\r\n") || character.equals("\r") || character.equals("\n") || character.equals("\t")) {
//                    MainApp.showInfoMessageDialog("Wagenrücklauf, Zeilenvorschub und Tabulatortaste werden nicht als Trennzeichen interpretiert" , getOwner());
                    MainApp.showInfoMessageDialog("Wagenrücklauf, Zeilenvorschub und Tabulatortaste sollen nicht als Trennzeichen benutzt werden", getOwner());
                    String text = ltaFreeText.getText();
                    text = text.substring(0, text.length() - 1) + " ";  // remove last char and append single whitespace
                    ltaFreeText.setText(text);
                    ltaFreeText.setCaretPosition(ltaFreeText.getText().length());
                }
            }
        });

        btnNewPvv.setDisable(true);
        ltaFreeText.getControl().selectionProperty().addListener(new ChangeListener<IndexRange>() {
            @Override
            public void changed(ObservableValue<? extends IndexRange> observable, IndexRange oldValue, IndexRange newValue) {
                if (ltaFreeText.getText().length() > COMMENT_MAX_CHARS
                        || listOfPvv.size() >= 10
                        || cbTp301Key30.getControl().getSelectionModel().isEmpty()
                        || cbBillNo.getControl().getSelectionModel().isEmpty()
                        || ldBillDate.getControl().getValue() == null) {
                    btnNewPvv.setDisable(true);
                } else {
                    btnNewPvv.setDisable(false);
                }
            }
        });

        cbTp301Key30.getSelectedItemProperty().addListener(new ChangeListener<Tp301Key30En>() {
            @Override
            public void changed(ObservableValue<? extends Tp301Key30En> observable, Tp301Key30En oldValue, Tp301Key30En newValue) {
                if (ltaFreeText.getText().length() > COMMENT_MAX_CHARS
                        || listOfPvv.size() >= 10
                        || cbTp301Key30.getControl().getSelectionModel().isEmpty()
                        || cbBillNo.getControl().getSelectionModel().isEmpty()
                        || ldBillDate.getControl().getValue() == null) {
                    btnNewPvv.setDisable(true);
                } else {
                    btnNewPvv.setDisable(false);
                }
            }
        });

        cbBillNo.getSelectedItemProperty().addListener(new ChangeListener<TP301KainInkaPvv>() {
            @Override
            public void changed(ObservableValue<? extends TP301KainInkaPvv> observable, TP301KainInkaPvv oldValue, TP301KainInkaPvv newValue) {
                if (ltaFreeText.getText().length() > COMMENT_MAX_CHARS
                        || listOfPvv.size() >= 10
                        || cbTp301Key30.getControl().getSelectionModel().isEmpty()
                        || cbBillNo.getControl().getSelectionModel().isEmpty()
                        || ldBillDate.getControl().getValue() == null) {
                    btnNewPvv.setDisable(true);
                } else {
                    btnNewPvv.setDisable(false);
                }
            }
        });

        ldBillDate.getControl().addEventHandler(EventType.ROOT, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (ltaFreeText.getText().length() > COMMENT_MAX_CHARS
                        || listOfPvv.size() >= 10
                        || cbTp301Key30.getControl().getSelectionModel().isEmpty()
                        || cbBillNo.getControl().getSelectionModel().isEmpty()
                        || ldBillDate.getControl().getValue() == null) {
                    btnNewPvv.setDisable(true);
                } else {
                    btnNewPvv.setDisable(false);
                }
            }
        });

        ltaFreeText.getControl().setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!ltaFreeText.getText().isEmpty() && ltaFreeText.getText().length() > COMMENT_MAX_CHARS) {
                    MainApp.showErrorMessageDialog("Bitte schreiben Sie weniger als " + COMMENT_MAX_CHARS + " Zeichen in den Freitextbereich.");
                }
            }
        });

        pvvDetailsTableView = new AsyncTableView<InkaPvvOverviewDTO>() {
            @Override
            public Future<List<InkaPvvOverviewDTO>> getFuture() {
//                return new AsyncResult<>(listOfAllPvvOverviewDTO);
                return new AsyncResult<>(new ArrayList<>(sortedList));
//                return new AsyncResult<>(FXCollections.observableList(new ArrayList<> (sortedList)));
            }
        };

//        sortedList.comparatorProperty().bind(pvvDetailsTableView.comparatorProperty());
        pvvDetailsTableView.setEditable(true);

        // allows the individual cells to be selected
//        pvvDetailsTableView.getSelectionModel().cellSelectionEnabledProperty().set(true);
        pvvDetailsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        pvvDetailsTableView.getStyleClass().add("stay-selected-table-view");
        HBox.setHgrow(pvvDetailsTableView, Priority.ALWAYS);
        VBox.setVgrow(pvvDetailsTableView, Priority.ALWAYS);

//        sortedList.addListener(new ListChangeListener<InkaPvvOverviewDTO>() {
//            @Override
//            public void onChanged(ListChangeListener.Change<? extends InkaPvvOverviewDTO> c) {
//                pvvDetailsTableView.reload();
//            }
//        });
        listOfAllPvvOverviewDTO.addListener(new ListChangeListener<InkaPvvOverviewDTO>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends InkaPvvOverviewDTO> c) {
                pvvDetailsTableView.reload();
            }
        });
        pvvDetailsTableView.reload();

        InfoKey30Column infoKey30Column = new InfoKey30Column();
        BillNoColumn billNoColumn = new BillNoColumn();
        PvvColumn pvvColumn = new PvvColumn();
        PvtColumn pvtColumn = new PvtColumn();
        PvtTextColumn pvtTextColumn = new PvtTextColumn();

        pvvDetailsTableView.getColumns().addAll(pvvColumn, infoKey30Column, billNoColumn, pvtColumn, pvtTextColumn);

        pvvDetailsTableView.getSortOrder().add(pvvColumn);

        kainInka.getKainInkaPvvs().forEach(new Consumer<TP301KainInkaPvv>() {
            @Override
            public void accept(TP301KainInkaPvv pvv) {
                if (pvv != null) {
                    if (cbBillNo.getItems().isEmpty()) {
                        cbBillNo.getItems().add(pvv);
                    }
                    if (!cbBillNo.getItems().contains(pvv)) {
                        cbBillNo.getItems().forEach(new Consumer<TP301KainInkaPvv>() {
                            @Override
                            public void accept(TP301KainInkaPvv t) {
                                if (!t.getBillNr().equals(pvv.getBillNr())) {
                                    cbBillNo.getItems().add(pvv);
                                }
                            }
                        });
                    }
                }
            }
        });
        cbBillNo.setConverter(new StringConverter<TP301KainInkaPvv>() {
            @Override
            public String toString(TP301KainInkaPvv pvv) {
                return pvv == null ? "" : pvv.getBillNr();
            }

            @Override
            public TP301KainInkaPvv fromString(String string) {
                return null;
            }
        });
        cbBillNo.getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TP301KainInkaPvv>() {
            @Override
            public void changed(ObservableValue<? extends TP301KainInkaPvv> observable, TP301KainInkaPvv oldValue, TP301KainInkaPvv newValue) {
                ldBillDate.setDate(newValue.getBillDate());
            }
        });

//        setOfPvvs = new HashSet<>();
        setOfPvvs = new ArrayList<>();
//        newKainInka = new TP301Inka();
//        newKainInka = new TP301KainInka();
        inka = new TP301Inka(); // create new Inka Object (TP301Inka is child of TP301KainInka)

        //create new PVV button
        btnNewPvv.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AddPvvButtonAction();
            }
        });

        // update PVV button
        btnUpdatePvv.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                UpdatePvvButtonAction();
            }
        });

        // show complete text in the text area and set values to both comboBoxes corresponding to the selected table row.
        pvvDetailsTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<InkaPvvOverviewDTO>() {
            @Override
            public void changed(ObservableValue<? extends InkaPvvOverviewDTO> observable, InkaPvvOverviewDTO oldValue, InkaPvvOverviewDTO newValue) {
                StringBuilder sb = new StringBuilder();
                if (newValue == null) {
                    ltaFreeText.setText(sb.toString());
                    return;
                }
                if (newValue.getPvv() != null && newValue.getPvv().getKainInkaPvts() != null) {
                    newValue.getPvv().getKainInkaPvts().forEach(new Consumer<TP301KainInkaPvt>() {
//                    List<TP301KainInkaPvt> allPvts = processServiceBean.getAllPvtsForPvv(newValue.getPvv().getId());
//                    allPvts.forEach(new Consumer<TP301KainInkaPvt>() {
                        @Override
                        public void accept(TP301KainInkaPvt t) {
                            sb.append(t.getText()); // for each pvt, get text and append it 
                        }
                    });
                }
                ltaFreeText.setText(sb.toString());

                if (newValue.getPvv() != null) {
                    // set key30 value
                    String informationKey30 = newValue.getPvv().getInformationKey30();
                    Tp301Key30En key30 = (Tp301Key30En) CpxEnumInterface.findEnum(Tp301Key30En.values(), informationKey30);
                    cbTp301Key30.getControl().setValue(key30);

                    // set bill no. value
//                    String billNr = newValue.getPvv().getBillNr();
                    cbBillNo.getControl().setValue(newValue.getPvv());
//                    cbBillNo.setValue(newValue.getPvv());
                }

                if (pvvDetailsTableView.getSelectionModel().isEmpty() || pvvDetailsTableView.getSelectionModel().getSelectedItem() == null) {
                    btnUpdatePvv.setDisable(true);
                } else {
                    btnUpdatePvv.setDisable(false);
                }
            }
        });

        // delete a row from the tableView (for each table row, add removeMenuItem)
/*        pvvDetailsTableView.setRowFactory(new Callback<TableView<InkaPvvOverviewDTO>, TableRow<InkaPvvOverviewDTO>>() {
            @Override
            public TableRow<InkaPvvOverviewDTO> call(TableView<InkaPvvOverviewDTO> tableView) {
                final TableRow<InkaPvvOverviewDTO> row = new TableRow<>();
                final ContextMenu contextMenu = new ContextMenu();
                final MenuItem removeMenuItem = new MenuItem(Lang.getDelete());

                removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        InkaPvvOverviewDTO item = row.getItem();
                        TP301KainInkaPvv selectedPvv = row.getItem().getPvv();
//                        Set<TP301KainInkaPvt> allPvtsForSelectedPvv = selectedPvv.getKainInkaPvts();
                        List<TP301KainInkaPvt> allPvtsForSelectedPvv = selectedPvv.getKainInkaPvts();

                        // remove selected PVV
                        setOfPvvs.remove(selectedPvv);
                        listOfPvv.remove(selectedPvv);

                        List<InkaPvvOverviewDTO> DtosToRemove = new ArrayList<>();
                        Set<TP301KainInkaPvt> pvtsToremove = new HashSet<>();

                        listOfAllPvvOverviewDTO.forEach(new Consumer<InkaPvvOverviewDTO>() {
                            @Override
                            public void accept(InkaPvvOverviewDTO dto) {
                                if (dto.getPvv().equals(selectedPvv)) {
                                    DtosToRemove.add(dto);
//                                    pvtsToremove.add(dto.getPvt());
                                }
                            }
                        });

                        listOfAllPvvOverviewDTO.removeAll(DtosToRemove);
                        listOfInkaPvvOverviewDTO.removeAll(DtosToRemove);

                        allPvtsForSelectedPvv.forEach(new Consumer<TP301KainInkaPvt>() {
                            @Override
                            public void accept(TP301KainInkaPvt pvt) {
                                pvtsToremove.add(pvt);
                            }
                        });

                        setOfPvts.removeAll(pvtsToremove);
                        allPvtsForSelectedPvv.removeAll(pvtsToremove);
                        selectedPvv.getKainInkaPvts().removeAll(pvtsToremove);

                        pvvDetailsTableView.reload();
                        pvvDetailsTableView.refresh();
                    }
                });
                contextMenu.getItems().addAll(removeMenuItem);

                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                        .then((ContextMenu) null)
                        .otherwise(contextMenu)
                );
                return row;
            }
        });
         */
        pvvDetailsTableView.setRowContextMenu(createContextMenu());

        vbox.getChildren().addAll(HcaseData, gpInfos, lbPvvInfo, cbTp301Key30, ltaFreeText, hbox, pvvDetailsTableView);

        validationSupport = getValidationSupport();
        validationSupport.setErrorDecorationEnabled(false);
        registerValidatorControls();
        validationSupport.initInitialDecoration();

        return vbox;
    }

    private ContextMenu createContextMenu() {
        final ContextMenu contextMenu = new CtrlContextMenu<>();
        final MenuItem removeMenuItem = new MenuItem(Lang.getDelete());
        removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                InkaPvvOverviewDTO item = pvvDetailsTableView.getSelectionModel().getSelectedItem();
                TP301KainInkaPvv selectedPvv = pvvDetailsTableView.getSelectionModel().getSelectedItem().getPvv();
//                Set<TP301KainInkaPvt> allPvtsForSelectedPvv = selectedPvv.getKainInkaPvts();
                List<TP301KainInkaPvt> allPvtsForSelectedPvv = selectedPvv.getKainInkaPvts();

                // remove selected PVV
                setOfPvvs.remove(selectedPvv);
                listOfPvv.remove(selectedPvv);

                List<InkaPvvOverviewDTO> DtosToRemove = new ArrayList<>();
                Set<TP301KainInkaPvt> pvtsToremove = new HashSet<>();

                listOfAllPvvOverviewDTO.forEach(new Consumer<InkaPvvOverviewDTO>() {
                    @Override
                    public void accept(InkaPvvOverviewDTO dto) {
                        if (dto.getPvv().equals(selectedPvv)) {
                            DtosToRemove.add(dto);
//                                    pvtsToremove.add(dto.getPvt());
                        }
                    }
                });

                listOfAllPvvOverviewDTO.removeAll(DtosToRemove);
                listOfInkaPvvOverviewDTO.removeAll(DtosToRemove);

                listOfInkaPvvOverviewDtoLists.removeAll(listOfInkaPvvOverviewDTO.stream().collect(Collectors.toList()));
//                listOfInkaPvvOverviewDtoLists.removeAll(DtosToRemove);

                allPvtsForSelectedPvv.forEach(new Consumer<TP301KainInkaPvt>() {
                    @Override
                    public void accept(TP301KainInkaPvt pvt) {
                        pvtsToremove.add(pvt);
                    }
                });

                setOfPvts.removeAll(pvtsToremove);
                allPvtsForSelectedPvv.removeAll(pvtsToremove);
                selectedPvv.getKainInkaPvts().removeAll(pvtsToremove);

                pvvDetailsTableView.reload();
//              pvvDetailsTableView.refresh();
            }
        });

        contextMenu.getItems().addAll(removeMenuItem);
        return contextMenu;
    }

    // In future, we may needs to handle specific characters for DAK (ISKV case) like CP (getContent() of the FMKAINFreitextDlg.java)
    private String getRemovedUNACharString(String s) {
        s = s.replace("+", ""); // or  s = s.replace("+", "?+");
        s = s.replace("'", ""); // or  s = s.replace("'", "?'");
        s = s.replace("#", ""); // or s = s.replace("#", "?#");
        s = s.replace(":", ""); // or s = s.replace(":", "?:");
//        s = s.replace("?", "");
//        s = s.replace("*", "");
        s = s.replace("\r\n", " ");
        s = s.replace("\r", " ");
        s = s.replace("\n", " ");
        s = s.replace("\t", " ");
        s = s.trim();
        return s;
    }

    // Create new PVV element with the set of PVTs and DTOs
    private void AddPvvButtonAction() {
        String tp301Key30value = cbTp301Key30.getSelectedItem() == null ? "" : cbTp301Key30.getSelectedItem().getViewId();
//                String billNo = cbBillNo.getSelectedItem() == null ? "" : cbBillNo.getSelectedItem();
        String billNo = cbBillNo.getSelectedItem().getBillNr() == null ? "" : cbBillNo.getSelectedItem().getBillNr();
        Date billDate = ldBillDate.getDate() == null ? null : ldBillDate.getDate();
//        String CompleteText = ltaFreeText.getText() == null ? "" : ltaFreeText.getText();
        String CompleteText = ltaFreeText.getText() == null ? "" : getRemovedUNACharString(ltaFreeText.getText());

        TP301KainInkaPvv pvv = new TP301KainInkaPvv();
        pvv.setBillDate(billDate);
        pvv.setBillNr(billNo);
        pvv.setInformationKey30(tp301Key30value);
//                pvv.setP301KainInkaId(kainInka);
        pvv.setP301KainInkaId(inka);

        listOfPvv.add(pvv);

//                Set<TP301KainInkaPvt> setOfPvts = new HashSet<>();
//        setOfPvts = new HashSet<>();
        setOfPvts = new ArrayList<>();

        int len = CompleteText.length() / 256;

        int mod = CompleteText.length() % 256;

        listOfInkaPvvOverviewDTO = FXCollections.observableArrayList();

        if (CompleteText.length() == 0) {
            InkaPvvOverviewDTO inkaPvvOverviewDTO = new InkaPvvOverviewDTO(pvv, null);
            inkaPvvOverviewDTO.setPvv(pvv);
            inkaPvvOverviewDTO.setPvt(null);
            inkaPvvOverviewDTO.setPvtText(null);
            listOfAllPvvOverviewDTO.add(inkaPvvOverviewDTO);
            listOfInkaPvvOverviewDTO.add(inkaPvvOverviewDTO);

            setOfPvvs.add(pvv);
//           pvv.setKainInkaPvts(null);
            pvv.setKainInkaPvts(setOfPvts);

//           listOfInkaPvvOverviewDtoLists.add(listOfInkaPvvOverviewDTO);
        }
        if (CompleteText.length() > 0) {
            if (mod > 0) {
                for (int i = 0; i < len + 1; i++) {
                    TP301KainInkaPvt pvt = new TP301KainInkaPvt();
                    pvt.setP301KainInkaPvvId(pvv);
                    if (i != len) {
                        pvt.setText(CompleteText.substring((255 * i) + i, (255 * i) + i + 1 + 255));
                    } else if (i == len) {
                        pvt.setText(CompleteText.substring((255 * i) + i, (255 * i) + i + mod));
                    }
                    setOfPvts.add(pvt);
                }
            } else {
                for (int i = 0; i < len; i++) {
                    TP301KainInkaPvt pvt = new TP301KainInkaPvt();
                    pvt.setP301KainInkaPvvId(pvv);
                    pvt.setText(CompleteText.substring((255 * i) + i, (255 * i) + i + 1 + 255));
                    setOfPvts.add(pvt);
                }
            }

            setOfPvts.forEach(new Consumer<TP301KainInkaPvt>() {
                @Override
                public void accept(TP301KainInkaPvt pvt) {
                    InkaPvvOverviewDTO inkaPvvOverviewDTO = new InkaPvvOverviewDTO(pvv, pvt);
                    inkaPvvOverviewDTO.setPvv(pvv);
                    inkaPvvOverviewDTO.setPvt(pvt);
                    inkaPvvOverviewDTO.setPvtText(pvt.getText());

                    listOfAllPvvOverviewDTO.add(inkaPvvOverviewDTO);
                    listOfInkaPvvOverviewDTO.add(inkaPvvOverviewDTO);
                }
            });
            setOfPvvs.add(pvv);
            pvv.setKainInkaPvts(setOfPvts);

            listOfInkaPvvOverviewDtoLists.add(listOfInkaPvvOverviewDTO.stream().collect(Collectors.toList()));
        }
    }

    // Update a PVV element (its PVTs and DTOs as well), to change bill no, key30 and pvt text values.
    private void UpdatePvvButtonAction() {
        // selected InkaPvvOverviewDTO object (table row)
        InkaPvvOverviewDTO selectedItem = pvvDetailsTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            //selected PVV segment
            TP301KainInkaPvv selectedPvv = selectedItem.getPvv();

//            Set<TP301KainInkaPvt> allPvtsForSelectedPvv = selectedItem.getPvv().getKainInkaPvts();
            List<TP301KainInkaPvt> allPvtsForSelectedPvv = selectedItem.getPvv().getKainInkaPvts();
//            List<TP301KainInkaPvt> allPvtsForSelectedPvv = processServiceBean.getAllPvtsForPvv(selectedItem.getPvv().getId());

            // update key30 value
            Tp301Key30En selectedKey30 = cbTp301Key30.getSelectedItem();
            selectedItem.getPvv().setInformationKey30(selectedKey30.getId());  // also update key30 value in the corresponding PVV.
            pvvDetailsTableView.getItems().forEach(new Consumer<InkaPvvOverviewDTO>() {
                @Override
                public void accept(InkaPvvOverviewDTO t) {
                    if (t.getPvv().equals(selectedPvv)) {
                        t.setInfoKey30(selectedKey30.getId());
                    }
                }
            });

            // update bill no. value
            TP301KainInkaPvv selectedBillNo = cbBillNo.getSelectedItem();
            selectedItem.getPvv().setBillNr(selectedBillNo.getBillNr()); // also update billNo value in the corresponding PVV.
            pvvDetailsTableView.getItems().forEach(new Consumer<InkaPvvOverviewDTO>() {
                @Override
                public void accept(InkaPvvOverviewDTO t) {
                    if (t.getPvv().equals(selectedPvv)) {
                        t.setBillNo(selectedBillNo.getBillNr());
                    }
                }
            });

            // get new (updated) free texts in forms of hashMap values 
            HashMap<Integer, String> hm = getFreeText();

            // If hashMap size is less than no. of PVTs for selected PVV, remove additional PVTs and related DTOs
            Set<TP301KainInkaPvt> pvtsToremove = new HashSet<>();
            ObservableList<InkaPvvOverviewDTO> DtosToremove = FXCollections.observableArrayList();

            if (allPvtsForSelectedPvv != null) {
                // If selected PVV has atleast one PVT
                if (!allPvtsForSelectedPvv.isEmpty()) {
//            if (allPvtsForSelectedPvv != null && !allPvtsForSelectedPvv.isEmpty()) {
                    //20.09.2018, clear previous list
                    listOfInkaPvvOverviewDTO.clear();
                    //for each PVTs, update corresponding pvt text or remove additional pvt
                    allPvtsForSelectedPvv.forEach(new Consumer<TP301KainInkaPvt>() {
//                        StringBuilder fullText = new StringBuilder();
                        private int i = 0;

                        @Override
                        public void accept(TP301KainInkaPvt pvt) {
//                            fullText.append(t.gettext());
//                            if (!t.gettext().equals(listOfUpdatePvtTexts.get(0))) {
//                                t.setText(listOfUpdatePvtTexts.get(0));
//                                selectedItem.setPvtText(listOfUpdatePvtTexts.get(0));
//                            }
//                            if (!fullText.toString().equals(freeText)) {
//                            } else {
//                                //don't need to do anything
//                                System.out.println("Full Text is the same!!");
//                            }

                            // update pvt text in a PVT Object (if text differs)
                            if (!pvt.getText().equals(hm.get(i))) {
                                if (hm.get(i) == null) {    // when no corresponding hashMap value, for given PVT
                                    pvtsToremove.add(pvt);
                                }
                                pvt.setText(hm.get(i) == null ? "" : hm.get(i));
                            }

                            // update pvt text in a tableView
                            pvvDetailsTableView.getItems().forEach(new Consumer<InkaPvvOverviewDTO>() {
                                @Override
                                public void accept(InkaPvvOverviewDTO dto) {
                                    // dto.getPvt() is null, when we create PVV segment without pvt.
                                    if (dto != null && dto.getPvt() != null && dto.getPvt().equals(pvt)) {
                                        if (hm.get(i) == null) {
                                            DtosToremove.add(dto);
                                        }
                                        dto.setPvtText(hm.get(i) == null ? "" : hm.get(i));
                                        //20.09.2018, add already existing DTOs of selected PVV
                                        listOfInkaPvvOverviewDTO.add(dto);
                                    }
                                }
                            });

                            i++;
                        }
                    });

                    // remove additional pvts and DTOs (when pvt segments has no corresponding hashmap values).
                    setOfPvts.removeAll(pvtsToremove);
                    allPvtsForSelectedPvv.removeAll(pvtsToremove);

                    listOfAllPvvOverviewDTO.removeAll(DtosToremove);
                    listOfInkaPvvOverviewDTO.removeAll(DtosToremove);
//                listOfInkaPvvOverviewDtoLists.remove(listOfInkaPvvOverviewDTO.stream().collect(Collectors.toList()));

                    // create new pvt segments for remaining (additional) hashMap values.
                    if (hm.size() > allPvtsForSelectedPvv.size()) {
//                    listOfInkaPvvOverviewDTO.clear();
//                    allPvtsForSelectedPvv.forEach(new Consumer<TP301KainInkaPvt>() {
//                        @Override
//                        public void accept(TP301KainInkaPvt t) {
//                           listOfInkaPvvOverviewDTO.add(t.);
//                        }
//                    });
                        for (int j = allPvtsForSelectedPvv.size(); j < hm.size(); j++) {
                            TP301KainInkaPvt pvt = new TP301KainInkaPvt();
//                        pvt.setP301KainInkaPvvId(selectedItem.getPvv());
                            pvt.setP301KainInkaPvvId(selectedPvv);
                            pvt.setText(hm.get(j));
//                        setOfPvts.add(pvt);
                            allPvtsForSelectedPvv.add(pvt);
                            InkaPvvOverviewDTO inkaPvvOverviewDTO = new InkaPvvOverviewDTO(selectedPvv, pvt);
                            inkaPvvOverviewDTO.setPvv(selectedPvv);
                            inkaPvvOverviewDTO.setPvt(pvt);
                            inkaPvvOverviewDTO.setPvtText(pvt.getText());

                            listOfAllPvvOverviewDTO.add(inkaPvvOverviewDTO);
                            listOfInkaPvvOverviewDTO.add(inkaPvvOverviewDTO);
                        }
//                        setOfPvvs.add(selectedItem.getPvv()); // Already there
//                        selectedItem.getPvv().setKainInkaPvts(setOfPvts); // setOfPvts gives more elements

                        listOfInkaPvvOverviewDtoLists.add(listOfInkaPvvOverviewDTO.stream().collect(Collectors.toList()));
                    }

                } else {   // if selected PVV segment, has no pvt

                    //remove selected dto (item)
                    listOfAllPvvOverviewDTO.removeAll(selectedItem);
                    listOfInkaPvvOverviewDTO.removeAll(selectedItem);
//                        setOfPvvs.remove(selectedItem.getPvv());

                    // create new pvt segments based on hashmap values.
                    if (!hm.isEmpty()) {
//                            setOfPvts.clear();
                        listOfInkaPvvOverviewDTO.clear();
                        for (int j = 0; j < hm.size(); j++) {
                            TP301KainInkaPvt pvt = new TP301KainInkaPvt();
                            pvt.setP301KainInkaPvvId(selectedPvv);
                            pvt.setText(hm.get(j));
//                        setOfPvts.add(pvt);
                            allPvtsForSelectedPvv.add(pvt); // NP bcoz no pvts

                            InkaPvvOverviewDTO inkaPvvOverviewDTO = new InkaPvvOverviewDTO(selectedPvv, pvt);
                            inkaPvvOverviewDTO.setPvv(selectedPvv);
                            inkaPvvOverviewDTO.setPvt(pvt);
                            inkaPvvOverviewDTO.setPvtText(pvt.getText());

                            listOfAllPvvOverviewDTO.add(inkaPvvOverviewDTO);
                            listOfInkaPvvOverviewDTO.add(inkaPvvOverviewDTO);
                        }
//                            setOfPvvs.add(selectedItem.getPvv()); //already there
//                            selectedItem.getPvv().setKainInkaPvts(setOfPvts); // setOfPvts gives more elements
                        listOfInkaPvvOverviewDtoLists.add(listOfInkaPvvOverviewDTO.stream().collect(Collectors.toList()));
                    }
                }
            }
            pvvDetailsTableView.reload();
//            pvvDetailsTableView.refresh();
        }
    }

    // create a HashMap based on updated free text values, to update/add/remove PVTs and DTOs.
    private HashMap<Integer, String> getFreeText() {
//        List<String> listOfUpdatePvtTexts = new ArrayList<>();
        HashMap<Integer, String> hm = new HashMap<>();
//        String freeText = ltaFreeText.getText();
        String freeText = getRemovedUNACharString(ltaFreeText.getText());
        int len = freeText.length() / 256;
        int mod = freeText.length() % 256;

        if (freeText.length() == 0) {
//            hm.put(0, "");  // update with empty freetext
        } else if (freeText.length() > 0) {
            if (mod > 0) {
                for (int i = 0; i < len + 1; i++) {
                    if (i != len) {
                        String substring = freeText.substring((255 * i) + i, (255 * i) + i + 1 + 255);
//                        listOfUpdatePvtTexts.add(substring);
                        hm.put(i, substring);
                    } else if (i == len) {
                        String substring = freeText.substring((255 * i) + i, (255 * i) + i + mod);
//                        listOfUpdatePvtTexts.add(substring);
                        hm.put(i, substring);
                    }
                }
            } else {
                for (int i = 0; i < len; i++) {
                    String substring = freeText.substring((255 * i) + i, (255 * i) + i + 1 + 255);
//                    listOfUpdatePvtTexts.add(substring);
                    hm.put(i, substring);
                }
            }
        }
        return hm;
    }

    // Get existing (selected) Inka Message and set its required data in the Dialog.
    void setDataFromCreatedInkaMsg() {
        if (this.kainInka != null && this.kainInka.getMessageType().equals("INKA")) {
            List<TP301KainInkaPvv> kainInkaPvvs = this.kainInka.getKainInkaPvvs();

            listOfPvv.addAll(kainInkaPvvs);
            setOfPvvs.addAll(kainInkaPvvs);

            kainInkaPvvs.forEach(new Consumer<TP301KainInkaPvv>() {
                @Override
                public void accept(TP301KainInkaPvv pvv) {
                    if (pvv != null) {
                        listOfInkaPvvOverviewDTO = FXCollections.observableArrayList(); // for each PVV, keep a track of DTOs
//                        Set<TP301KainInkaPvt> kainInkaPvts = pvv.getKainInkaPvts();   //gives null pointer bcoz of hibernate session is closed (even don't work with eager loading)
                        List<TP301KainInkaPvt> kainInkaPvts = pvv.getKainInkaPvts();   //gives null pointer bcoz of hibernate session is closed (even don't work with eager loading)
//                        List<TP301KainInkaPvt> kainInkaPvts = processServiceBean.get().getAllPvtsForPvv(pvv.getId());

//                        setOfPvts = new HashSet<>();
                        setOfPvts = new ArrayList<>();
                        setOfPvts.addAll(kainInkaPvts);

                        //pna: 17.12.2018 (bug: without PVT elements, PVV shoud be shown in dialog)
                        if (kainInkaPvts != null && kainInkaPvts.isEmpty()) {
                            InkaPvvOverviewDTO dto = new InkaPvvOverviewDTO(pvv, null);
                            dto.setPvv(pvv);
                            dto.setBillNo(pvv.getBillNr());
                            dto.setInfoKey30(pvv.getInformationKey30());
                            dto.setPvt(null);
                            dto.setPvtText("");

                            listOfAllPvvOverviewDTO.add(dto);
                            listOfInkaPvvOverviewDTO.add(dto);
                        }
                        if (kainInkaPvts != null && !kainInkaPvts.isEmpty()) {
                            kainInkaPvts.forEach(new Consumer<TP301KainInkaPvt>() {
                                @Override
                                public void accept(TP301KainInkaPvt pvt) {
                                    InkaPvvOverviewDTO dto = new InkaPvvOverviewDTO(pvv, pvt);
                                    dto.setPvv(pvv);
                                    dto.setBillNo(pvv.getBillNr());
                                    dto.setInfoKey30(pvv.getInformationKey30());
                                    dto.setPvt(pvt);
                                    dto.setPvtText(pvt.getText());

                                    listOfAllPvvOverviewDTO.add(dto);
                                    listOfInkaPvvOverviewDTO.add(dto);
                                }
                            });
                        }

                        String informationKey30 = pvv.getInformationKey30();
                        Tp301Key30En key30 = (Tp301Key30En) CpxEnumInterface.findEnum(Tp301Key30En.values(), informationKey30);
                        cbTp301Key30.getControl().setValue(key30);
                        cbBillNo.getControl().setValue(pvv);
                        ldBillDate.setDate(pvv.getBillDate());

                        if (kainInkaPvts != null) {
                            StringBuilder sb = new StringBuilder();
                            kainInkaPvts.forEach(new Consumer<TP301KainInkaPvt>() {
                                @Override
                                public void accept(TP301KainInkaPvt pvt) {
                                    sb.append(pvt.getText());
                                }
                            });
                            ltaFreeText.setText(sb.toString());
                        }
                    }

                    if (listOfInkaPvvOverviewDTO != null) {
                        listOfInkaPvvOverviewDtoLists.add(listOfInkaPvvOverviewDTO.stream().collect(Collectors.toList()));
                    }

                }
            });
//            if (listOfInkaPvvOverviewDTO != null) {
//                listOfInkaPvvOverviewDtoLists.add(listOfInkaPvvOverviewDTO.stream().collect(Collectors.toList()));
//            }
        }

    }

    private class PvvColumn extends TableColumn<InkaPvvOverviewDTO, InkaPvvOverviewDTO> {

        PvvColumn() {
            super("PVV");
            setMinWidth(40.0d);
            setMaxWidth(40.0d);
            setResizable(false);
            setSortable(false);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<InkaPvvOverviewDTO, InkaPvvOverviewDTO>, ObservableValue<InkaPvvOverviewDTO>>() {
                @Override
                public ObservableValue<InkaPvvOverviewDTO> call(TableColumn.CellDataFeatures<InkaPvvOverviewDTO, InkaPvvOverviewDTO> param) {
                    return new SimpleObjectProperty<>(param.getValue());
                }
            });
            setCellFactory(new Callback<TableColumn<InkaPvvOverviewDTO, InkaPvvOverviewDTO>, TableCell<InkaPvvOverviewDTO, InkaPvvOverviewDTO>>() {
                @Override
                public TableCell<InkaPvvOverviewDTO, InkaPvvOverviewDTO> call(TableColumn<InkaPvvOverviewDTO, InkaPvvOverviewDTO> param) {
                    return new TableCell<InkaPvvOverviewDTO, InkaPvvOverviewDTO>() {
                        @Override
                        protected void updateItem(InkaPvvOverviewDTO item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null || empty) {
                                setGraphic(null);
                                return;
                            }

                            Label label = new Label(String.valueOf(listOfPvv.indexOf(item.getPvv()) + 1));
//                            Label label = new Label(String.valueOf(listOfInkaPvvOverviewDTO.indexOf(item.getPvv()) + 1));
                            setGraphic(label);
                        }

                    };
                }
            });
        }
    }

    private class InfoKey30Column extends TableColumn<InkaPvvOverviewDTO, InkaPvvOverviewDTO> {

        InfoKey30Column() {
            super("Info PrüfvV");
            setMinWidth(70.0d);
            setMaxWidth(70.0d);
            setResizable(false);
            setSortable(false);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<InkaPvvOverviewDTO, InkaPvvOverviewDTO>, ObservableValue<InkaPvvOverviewDTO>>() {
                @Override
                public ObservableValue<InkaPvvOverviewDTO> call(TableColumn.CellDataFeatures<InkaPvvOverviewDTO, InkaPvvOverviewDTO> param) {
                    return new SimpleObjectProperty<>(param.getValue());
                }
            });
            setCellFactory(new Callback<TableColumn<InkaPvvOverviewDTO, InkaPvvOverviewDTO>, TableCell<InkaPvvOverviewDTO, InkaPvvOverviewDTO>>() {
                @Override
                public TableCell<InkaPvvOverviewDTO, InkaPvvOverviewDTO> call(TableColumn<InkaPvvOverviewDTO, InkaPvvOverviewDTO> param) {
                    return new TableCell<InkaPvvOverviewDTO, InkaPvvOverviewDTO>() {
                        @Override
                        protected void updateItem(InkaPvvOverviewDTO item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null || empty) {
                                setGraphic(null);
                                return;
                            }
                            Label label = new Label(item.getInfoKey30());
                            setGraphic(label);
                        }

                    };
                }
            });

        }
    }

    private class BillNoColumn extends TableColumn<InkaPvvOverviewDTO, InkaPvvOverviewDTO> {

        BillNoColumn() {
            super("Rech.-Nr.");
            setMinWidth(70.0d);
            setMaxWidth(500.0d);
            setResizable(true);
            setSortable(false);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<InkaPvvOverviewDTO, InkaPvvOverviewDTO>, ObservableValue<InkaPvvOverviewDTO>>() {
                @Override
                public ObservableValue<InkaPvvOverviewDTO> call(TableColumn.CellDataFeatures<InkaPvvOverviewDTO, InkaPvvOverviewDTO> param) {
                    return new SimpleObjectProperty<>(param.getValue());
                }
            });
            setCellFactory(new Callback<TableColumn<InkaPvvOverviewDTO, InkaPvvOverviewDTO>, TableCell<InkaPvvOverviewDTO, InkaPvvOverviewDTO>>() {
                @Override
                public TableCell<InkaPvvOverviewDTO, InkaPvvOverviewDTO> call(TableColumn<InkaPvvOverviewDTO, InkaPvvOverviewDTO> param) {
                    Tooltip billNoTT = new Tooltip();
                    billNoTT.setStyle("-fx-font-size: 12px");
                    billNoTT.setWrapText(true);
                    billNoTT.setMaxWidth(150);
//                    return new TableCell<InkaPvvOverviewDTO, InkaPvvOverviewDTO>() {
                    TableCell<InkaPvvOverviewDTO, InkaPvvOverviewDTO> cell = new TableCell<InkaPvvOverviewDTO, InkaPvvOverviewDTO>() {
                        @Override
                        protected void updateItem(InkaPvvOverviewDTO item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null || empty) {
                                setGraphic(null);
                                return;
                            }

                            Label label = new Label(item.getBillNo());
                            setGraphic(label);
                            billNoTT.setText(item.getBillNo());
                            setTooltip(billNoTT);
                        }

                    };
                    return cell;
                }
            });
        }
    }

    private class PvtColumn extends TableColumn<InkaPvvOverviewDTO, InkaPvvOverviewDTO> {

        PvtColumn() {
            super("PVT");
            setMinWidth(40.0d);
            setMaxWidth(40.0d);
            setResizable(false);
            setSortable(false);
            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<InkaPvvOverviewDTO, InkaPvvOverviewDTO>, ObservableValue<InkaPvvOverviewDTO>>() {
                @Override
                public ObservableValue<InkaPvvOverviewDTO> call(TableColumn.CellDataFeatures<InkaPvvOverviewDTO, InkaPvvOverviewDTO> param) {
                    return new SimpleObjectProperty<>(param.getValue());
                }
            });

            setCellFactory(new Callback<TableColumn<InkaPvvOverviewDTO, InkaPvvOverviewDTO>, TableCell<InkaPvvOverviewDTO, InkaPvvOverviewDTO>>() {
                @Override
                public TableCell<InkaPvvOverviewDTO, InkaPvvOverviewDTO> call(TableColumn<InkaPvvOverviewDTO, InkaPvvOverviewDTO> param) {
                    return new TableCell<InkaPvvOverviewDTO, InkaPvvOverviewDTO>() {
                        @Override
                        protected void updateItem(InkaPvvOverviewDTO item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null || empty) {
                                setGraphic(null);
                                return;
                            }
                            listOfInkaPvvOverviewDtoLists.forEach(new Consumer<List<InkaPvvOverviewDTO>>() {
                                @Override
                                public void accept(List<InkaPvvOverviewDTO> t) {
                                    if (t.contains(item)) {
                                        t.forEach(new Consumer<InkaPvvOverviewDTO>() {
                                            @Override
                                            public void accept(InkaPvvOverviewDTO dto) {
                                                if (dto.equals(item)) {
//                                                    Label label = new Label(String.valueOf(t.indexOf(item) + 1));
                                                    Label label;
                                                    if (dto.getPvt() != null && !dto.getPvt().getText().isEmpty()) {
                                                        label = new Label(String.valueOf(t.indexOf(item) + 1));
                                                    } else {
                                                        label = new Label("");
                                                    }
                                                    setGraphic(label);
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    };
                }
            });
//            listOfInkaPvvOverviewDTO.clear();
        }
    }

    private class PvtTextColumn extends TableColumn<InkaPvvOverviewDTO, InkaPvvOverviewDTO> {

        PvtTextColumn() {
            super("PVT Text");
            setMinWidth(40.0d);
//            setMaxWidth(40.0d);
//            setResizable(false);
            setSortable(false);

            setCellValueFactory(new Callback<TableColumn.CellDataFeatures<InkaPvvOverviewDTO, InkaPvvOverviewDTO>, ObservableValue<InkaPvvOverviewDTO>>() {
                @Override
                public ObservableValue<InkaPvvOverviewDTO> call(TableColumn.CellDataFeatures<InkaPvvOverviewDTO, InkaPvvOverviewDTO> param) {
                    return new SimpleObjectProperty<>(param.getValue());
                }
            });

            setCellFactory(new Callback<TableColumn<InkaPvvOverviewDTO, InkaPvvOverviewDTO>, TableCell<InkaPvvOverviewDTO, InkaPvvOverviewDTO>>() {
                @Override
                public TableCell<InkaPvvOverviewDTO, InkaPvvOverviewDTO> call(TableColumn<InkaPvvOverviewDTO, InkaPvvOverviewDTO> param) {
//                    return new TableCell<InkaPvvOverviewDTO, InkaPvvOverviewDTO>() {
                    Tooltip pvtTextTT = new Tooltip();
                    pvtTextTT.setStyle("-fx-font-size: 12px");
                    pvtTextTT.setWrapText(true);
                    pvtTextTT.setMaxWidth(600);
                    TableCell<InkaPvvOverviewDTO, InkaPvvOverviewDTO> cell = new TableCell<InkaPvvOverviewDTO, InkaPvvOverviewDTO>() {
                        @Override
                        protected void updateItem(InkaPvvOverviewDTO item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null || empty) {
                                setGraphic(null);
                                return;
                            }

                            Label label = new Label(item.getPvtText());
                            setGraphic(label);
                            pvtTextTT.setText(item.getPvtText());
                            setTooltip(pvtTextTT);
                        }
                    };
                    cell.setEditable(true);
                    return cell;
                }

            });
        }
    }

    // Common method to set tooltip for each column cells.
//    private <T> void addTooltipToColumnCells(TableColumn<InkaPvvOverviewDTO, T> column) {
//
//        Callback<TableColumn<InkaPvvOverviewDTO, T>, TableCell<InkaPvvOverviewDTO, T>> existingCellFactory = column.getCellFactory();
//
//        column.setCellFactory(c -> {
//            TableCell<InkaPvvOverviewDTO, T> cell = existingCellFactory.call(c);
//            Tooltip tooltip = new Tooltip();
//            tooltip.textProperty().bind(cell.itemProperty().asString());
//            cell.setTooltip(tooltip);
//            return cell;
//        });
//    }
//
//    @SuppressWarnings("unchecked")
//    private void editFocusedCell() {
//        final TablePosition< InkaPvvOverviewDTO, ?> focusedCell = pvvDetailsTableView
//                .focusModelProperty().get().focusedCellProperty().get();
//        pvvDetailsTableView.edit(focusedCell.getRow(), focusedCell.getTableColumn());
//    }
    private TableColumn< InkaPvvOverviewDTO, ?> getTableColumn(
            final TableColumn< InkaPvvOverviewDTO, ?> column, int offset) {
        int columnIndex = pvvDetailsTableView.getVisibleLeafIndex(column);
        int newColumnIndex = columnIndex + offset;
        return pvvDetailsTableView.getVisibleLeafColumn(newColumnIndex);
    }

//    @SuppressWarnings("unchecked")
//    private void selectPrevious() {
//        if (pvvDetailsTableView.getSelectionModel().isCellSelectionEnabled()) {
//            // in cell selection mode, we have to wrap around, going from
//            // right-to-left, and then wrapping to the end of the previous line
//            TablePosition< InkaPvvOverviewDTO, ?> pos = pvvDetailsTableView.getFocusModel()
//                    .getFocusedCell();
//            if (pos.getColumn() - 1 >= 0) {
//                // go to previous row
//                pvvDetailsTableView.getSelectionModel().select(pos.getRow(),
//                        getTableColumn(pos.getTableColumn(), -1));
//            } else if (pos.getRow() < pvvDetailsTableView.getItems().size()) {
//                // wrap to end of previous row
//                pvvDetailsTableView.getSelectionModel().select(pos.getRow() - 1,
//                        pvvDetailsTableView.getVisibleLeafColumn(
//                                pvvDetailsTableView.getVisibleLeafColumns().size() - 1));
//            }
//        } else {
//            int focusIndex = pvvDetailsTableView.getFocusModel().getFocusedIndex();
//            if (focusIndex == -1) {
//                pvvDetailsTableView.getSelectionModel().select(pvvDetailsTableView.getItems().size() - 1);
//            } else if (focusIndex > 0) {
//                pvvDetailsTableView.getSelectionModel().select(focusIndex - 1);
//            }
//        }
//    }
    // With OK (renamed, Speichern) button, save new INKA msg (or update existing Inka msg) in DB.
    @Override
    public TP301KainInka onSave() {
        return onSave(true, true);
    }

    public TP301KainInka onSave(final boolean pStore, final boolean isCreateNewEvent) {
        if (this.kainInka.getMessageType().equals("KAIN")) {
            inka.setKainInkaPvvs(setOfPvvs);
            inka.setContactReference(kainInka.getContactReference());
            inka.setCostUnitSap(kainInka.getCostUnitSap());
            inka.setCurrentTransactionNr(kainInka.getCurrentTransactionNr());
            inka.setHospitalIdentifier(kainInka.getHospitalIdentifier());
            inka.setHospitalNumberPatient(kainInka.getHospitalNumberPatient());
            inka.setInsuranceCaseNumber(kainInka.getInsuranceCaseNumber());
            inka.setInsuranceIdentifier(kainInka.getInsuranceIdentifier());
            inka.setInsuranceRefNumber(kainInka.getInsuranceRefNumber());
            inka.setProcessingRef(kainInka.getProcessingRef());
            inka.setTCaseId(kainInka.getTCaseId());

            inka.setIsSendedFl(false);
//            inka.setIsCancelledFl(false);
            inka.setReady4sendingFl(true);

            newKainInka = inka;

            TP301KainInka storedKainInka = newKainInka;
            if (pStore && newKainInka != null) {
                storedKainInka = serviceFacade.storeKainInka(newKainInka);
                LOG.log(Level.INFO, "Inka message is created and saved successfully...");
            }
//            return newKainInka;
            return storedKainInka;

        } else if (this.kainInka.getMessageType().equals("INKA")) {
            // update values to the existing kainInka (of type INKA) 
            this.kainInka.setKainInkaPvvs(setOfPvvs);
            this.kainInka.setContactReference(kainInka.getContactReference());
            this.kainInka.setCostUnitSap(kainInka.getCostUnitSap());
            this.kainInka.setCurrentTransactionNr(kainInka.getCurrentTransactionNr());
            this.kainInka.setHospitalIdentifier(kainInka.getHospitalIdentifier());
            this.kainInka.setHospitalNumberPatient(kainInka.getHospitalNumberPatient());
            this.kainInka.setInsuranceCaseNumber(kainInka.getInsuranceCaseNumber());
            this.kainInka.setInsuranceIdentifier(kainInka.getInsuranceIdentifier());
            this.kainInka.setInsuranceRefNumber(kainInka.getInsuranceRefNumber());
            this.kainInka.setProcessingRef(kainInka.getProcessingRef());
            this.kainInka.setTCaseId(kainInka.getTCaseId());

            TP301KainInka updatedKainInka = this.kainInka;
            if (pStore && this.kainInka != null) {
                updatedKainInka = serviceFacade.updateKainInka(this.kainInka, isCreateNewEvent);
                LOG.log(Level.INFO, "Inka message is updated successfully...");
            }
//            return this.kainInka;
            return updatedKainInka;
        }
//        return this.kainInka;
        LOG.log(Level.SEVERE, "Provided kainInka Object is neither of type Kain nor Inka....");
        return null;
    }

    public TP301KainInka onSend(TP301KainInka kainInka) {
        if (kainInka != null && kainInka.isInka()) {
            TP301KainInka sendInkaMessage = kainInka;
            try {
                //just for testing
//                long sendingInkaNumber = processServiceBean.get().createNextSendingInkaNumberValue();

                sendInkaMessage = serviceFacade.sendInkaMessage(kainInka);

                Notifications notifications = NotificationsFactory.instance().createInformationNotification();
                notifications.title("Inka-Nachricht wurde erfolgreich gesendet.")
                        .text("Inka Nachricht mit IK des Krankenhauses " + kainInka.getHospitalIdentifier() + "und IK der Krankenkasse " + (kainInka.getInsuranceIdentifier()) + " wurde erfolgreich gesendet.")
                        .onAction((event) -> {
//                            BasicMainApp.openUrl();
//                            BasicMainApp.showInfoMessageDialog("Inka-Nachricht wurde erfolgreich gesendet.");
                        })
                        .show();

                // only after successful sending, set sending flag and sending date
                ((TP301Inka) sendInkaMessage).setIsSendedFl(true);
                ((TP301Inka) sendInkaMessage).setSendingDate(new Date());
//                ((TP301Inka) kainInka).setProcessingRef("75");  // 75 ---> to indicate that Inka message is sent

//                sendInkaMessage = serviceFacade.updateKainInka(sendInkaMessage, false);
                serviceFacade.updateKainInka(sendInkaMessage, false);

            } catch (EJBException ex) {
                LOG.log(Level.SEVERE, "Was not able to send Inka Message", ex);
                Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
//                MainApp.showErrorMessageDialog("Die INKA-Nachricht konnte nicht verschickt werden", ex.getMessage(), stage);
                MainApp.showErrorMessageDialog("Die INKA-Nachricht konnte nicht verschickt werden", ex.getCause().getLocalizedMessage(), stage);
            }
            return sendInkaMessage;
        }
        LOG.log(Level.SEVERE, "Can't send an Inka message as provided kainInka Object is either null or is not type of Inka....");
        return null;
    }

    public TP301KainInka onCancel(TP301KainInka kainInka) {
        if (kainInka != null && kainInka.isInka()) {
            TP301KainInka cancelInkaMessage = kainInka;
            try {
//                ((TP301Inka) kainInka).getCurrentNrSending();
                cancelInkaMessage = serviceFacade.cancelInkaMessage(kainInka);

                Notifications notifications = NotificationsFactory.instance().createInformationNotification();
                notifications.title("Inka-Nachricht wurde erfolgreich storniert.")
                        .text("Inka Nachricht mit IK des Krankenhauses " + kainInka.getHospitalIdentifier() + "und IK der Krankenkasse " + (kainInka.getInsuranceIdentifier()) + " wurde erfolgreich storniert.")
                        .onAction((event) -> {
                        })
                        .show();

                ((TP301Inka) cancelInkaMessage).setIsCancelledFl(true);
                cancelInkaMessage.setProcessingRef("76");  // 76 ---> to indicate that Inka message is cancelled

                serviceFacade.updateKainInka(cancelInkaMessage, false);

            } catch (EJBException ex) {
                LOG.log(Level.SEVERE, "Was not able to cancel an Inka Message", ex);
                Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
                MainApp.showErrorMessageDialog("Die INKA-Nachricht konnte nicht storniert werden", ex.getCause().getLocalizedMessage(), stage);
            }
            return cancelInkaMessage;
        } else {
            LOG.log(Level.SEVERE, "Can't cancel an Inka Message as provided kainInka Object is either null or is not type of Inka....");
            return null;
        }
    }

    private ObjectProperty<ValidationSupport> validationSupportProperty;

    private ObjectProperty<ValidationSupport> validationSupportProperty() {
        if (validationSupportProperty == null) {
            validationSupportProperty = new SimpleObjectProperty<>();
        }
        return validationSupportProperty;
    }

    public void setValidationSupport(ValidationSupport validationSupport) {
        validationSupportProperty().set(validationSupport);
    }

    final ValidationSupport getValidationSupport() {
        return validationSupportProperty().get();
    }

    private void registerValidatorControls() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                if (validationSupport != null) {
                    validationSupport.registerValidator(cbTp301Key30.getControl(), true, new Validator<Tp301Key30En>() {
                        @Override
                        public ValidationResult apply(Control t, Tp301Key30En u) {
                            ValidationResult res = new ValidationResult();
                            if (u == null) {
                                res.addErrorIf(t, "Bitte wählen Sie Info PrüfvV aus!", (u == null));
                            }
                            validationSupport.setErrorDecorationEnabled(true);
                            return res;
                        }
                    });

                    validationSupport.registerValidator(cbBillNo.getControl(), true, new Validator<TP301KainInkaPvv>() {
                        @Override
                        public ValidationResult apply(Control t, TP301KainInkaPvv u) {
                            ValidationResult res = new ValidationResult();
                            if (u == null || u.getBillNr().isEmpty()) {
                                res.addErrorIf(t, "Bitte wählen Sie Rechnungsnummer aus!", (u == null || u.getBillNr().isEmpty()));
                            }
                            validationSupport.setErrorDecorationEnabled(true);
                            return res;
                        }
                    });

                    validationSupport.registerValidator(ldBillDate.getControl(), true, new Validator<java.time.LocalDate>() {
                        @Override
                        public ValidationResult apply(Control t, java.time.LocalDate u) {
                            ValidationResult res = new ValidationResult();
                            if (u == null) {
                                res.addErrorIf(t, "Bitte legen Sie das Rechnungsdatum fest!", (u == null));
                            }
                            validationSupport.setErrorDecorationEnabled(true);
                            return res;
                        }
                    });

//                    validationSupport.registerValidator(pvvDetailsTableView, true, new Validator<com.sun.javafx.collections.ObservableSequentialListWrapper>() {
//                        @Override
//                        public ValidationResult apply(Control t, com.sun.javafx.collections.ObservableSequentialListWrapper u) {
//                            ValidationResult res = new ValidationResult();
//                            if (u == null || u.isEmpty() || pvvDetailsTableView.getItems().isEmpty()) {
//                                res.addErrorIf(t, "Bitte erstellen Sie mindestens eine Pvv!", (u == null || u.isEmpty() || pvvDetailsTableView.getItems().isEmpty()));
//                            }
////                            validationSupport.setErrorDecorationEnabled(true);
//                            return res;
//                        }
//                    });
//                    validationSupport.registerValidator(pvvDetailsTableView, true, new Validator<com.sun.javafx.collections.ObservableIntegerArrayImpl>() {
//                        @Override
//                        public ValidationResult apply(Control t, com.sun.javafx.collections.ObservableIntegerArrayImpl u) {
//                            ValidationResult res = new ValidationResult();
//                            if (u == null || pvvDetailsTableView.getItems().isEmpty()) {
//                                res.addErrorIf(t, "Bitte erstellen Sie mindestens eine Pvv!", (u == null || pvvDetailsTableView.getItems().isEmpty()));
//                            }
////                            validationSupport.setErrorDecorationEnabled(true);
//                            return res;
//                        }
//                    });
                    validationSupport.registerValidator(pvvDetailsTableView, new Validator<Object>() {
                        @Override
                        public ValidationResult apply(Control t, Object u) {
                            ValidationResult res = new ValidationResult();
                            if (u == null || pvvDetailsTableView.getItems().isEmpty()) {
                                res.addErrorIf(t, "Bitte erstellen Sie mindestens eine Pvv!", (u == null || pvvDetailsTableView.getItems().isEmpty()));
                            }
//                            validationSupport.setErrorDecorationEnabled(true);
                            return res;
                        }
                    });
                }
            }
        });
    }

}
