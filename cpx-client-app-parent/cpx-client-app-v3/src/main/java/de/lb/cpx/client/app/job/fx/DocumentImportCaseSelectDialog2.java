/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.job.fx;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.core.model.cell.SimpleCellValueFactory;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.dialog.FormularDialog;
import de.lb.cpx.model.TCase;
import de.lb.cpx.shared.lang.Lang;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Window;
import javafx.util.Callback;

/**
 *
 * @author niemeier
 */
public class DocumentImportCaseSelectDialog2 extends FormularDialog<TCase> {

    private final static String STAY_SELECTED_CLASS = "stay-selected-table-view";

    @SuppressWarnings("unchecked")
    public DocumentImportCaseSelectDialog2(TextFlow pTitle, final List<TCase> pCases, Window pParentWindow, Callback<TCase, Void> pCallback) {
        super(pParentWindow, Modality.WINDOW_MODAL, "Fall auswählen");
//        final FormularDialog<DocumentSearchCaseItemDto> dlg = new FormularDialog<DocumentSearchCaseItemDto>(pParentWindow, Modality.WINDOW_MODAL, "Vorgang auswählen") {
//            @Override
//            public DocumentSearchCaseItemDto onSave() {
//                return null;
//            }
//        };
        ObservableList<TCase> obsCases = FXCollections.observableArrayList(pCases);
        TableView<TCase> tv = new TableView<>(obsCases);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        final TableColumn<TCase, TCase> colHospitalIdent = new TableColumn<>("IKZ");
        //colProcessDate.setPrefWidth(200d);
        colHospitalIdent.setCellValueFactory(new SimpleCellValueFactory<>());
        colHospitalIdent.setCellFactory((TableColumn<TCase, TCase> param) -> {
            TableCell<TCase, TCase> cell = new TableCell<>() {

                @Override
                protected void updateItem(TCase item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setText("");
                        return;
                    }
                    TCase cs = item;
                    setText(cs.getCsHospitalIdent());
                }
            };
            return cell;
        });

        final TableColumn<TCase, TCase> colCaseNumber = new TableColumn<>("Fallnr.");
        //colProcessDate.setPrefWidth(200d);
        colCaseNumber.setCellValueFactory(new SimpleCellValueFactory<>());
        colCaseNumber.setCellFactory((TableColumn<TCase, TCase> param) -> {
            TableCell<TCase, TCase> cell = new TableCell<>() {

                @Override
                protected void updateItem(TCase item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setText("");
                        return;
                    }
                    TCase cs = item;
                    setText(cs.getCsCaseNumber());
                }
            };
            return cell;
        });

        final TableColumn<TCase, TCase> colCaseStatus = new TableColumn<>("Fallstatus");
        //colProcessDate.setPrefWidth(200d);
        colCaseStatus.setCellValueFactory(new SimpleCellValueFactory<>());
        colCaseStatus.setCellFactory((TableColumn<TCase, TCase> param) -> {
            TableCell<TCase, TCase> cell = new TableCell<>() {

                @Override
                protected void updateItem(TCase item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setText("");
                        return;
                    }
                    TCase cs = item;
                    setText(cs.getCsCaseTypeEn().getTranslation().getValue());
                }
            };
            return cell;
        });

        final TableColumn<TCase, TCase> colAdmissionDate = new TableColumn<>("Aufnahmedatum");
        //colProcessDate.setPrefWidth(200d);
        colAdmissionDate.setCellValueFactory(new SimpleCellValueFactory<>());
        colAdmissionDate.setCellFactory((TableColumn<TCase, TCase> param) -> {
            TableCell<TCase, TCase> cell = new TableCell<>() {

                @Override
                protected void updateItem(TCase item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setText("");
                        return;
                    }
                    TCase cs = item;
                    setText(Lang.toDate(cs.getCurrentLocal().getCsdAdmissionDate()));
                }
            };
            return cell;
        });

        final TableColumn<TCase, TCase> colDischargeDate = new TableColumn<>("Entlassungsdatum");
        //colProcessDate.setPrefWidth(200d);
        colDischargeDate.setCellValueFactory(new SimpleCellValueFactory<>());
        colDischargeDate.setCellFactory((TableColumn<TCase, TCase> param) -> {
            TableCell<TCase, TCase> cell = new TableCell<>() {

                @Override
                protected void updateItem(TCase item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setText("");
                        return;
                    }
                    TCase cs = item;
                    setText(Lang.toDate(cs.getCurrentLocal().getCsdDischargeDate()));
                }
            };
            return cell;
        });

        final TableColumn<TCase, TCase> colPatientNumber = new TableColumn<>("Pat.-Nr.");
        //colProcessDate.setPrefWidth(200d);
        colPatientNumber.setCellValueFactory(new SimpleCellValueFactory<>());
        colPatientNumber.setCellFactory((TableColumn<TCase, TCase> param) -> {
            TableCell<TCase, TCase> cell = new TableCell<>() {

                @Override
                protected void updateItem(TCase item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setText("");
                        return;
                    }
                    TCase cs = item;
                    setText(cs.getPatient().getPatNumber());
                }
            };
            return cell;
        });

        final TableColumn<TCase, TCase> colPatientName = new TableColumn<>("Patient");
        //colProcessDate.setPrefWidth(200d);
        colPatientName.setCellValueFactory(new SimpleCellValueFactory<>());
        colPatientName.setCellFactory((TableColumn<TCase, TCase> param) -> {
            TableCell<TCase, TCase> cell = new TableCell<>() {

                @Override
                protected void updateItem(TCase item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setText("");
                        return;
                    }
                    TCase cs = item;
                    setText(cs.getPatient().getPatFullName());
                }
            };
            return cell;
        });

        tv.getColumns().addAll(/* colIsMainCase, */colHospitalIdent, colCaseNumber, colCaseStatus, colAdmissionDate, colDischargeDate, colPatientNumber, colPatientName);
        tv.setMinHeight(50d);
        tv.setMinWidth(650d);
        //tv.setPrefHeight(100d);
        tv.requestFocus();
        if (!tv.getItems().isEmpty()) {
            tv.getSelectionModel().select(tv.getItems().iterator().next());
        }

        tv.setOnKeyPressed((evt) -> {
            if (evt.getCode() == KeyCode.ENTER) {
                this.setResults(tv.getSelectionModel().getSelectedItem());
                this.resultProperty().set(ButtonType.NEXT);
            }
            if (evt.getCode() == KeyCode.ESCAPE) {
                this.resultProperty().set(ButtonType.CANCEL);
            }
        });

        tv.setOnMouseClicked((MouseEvent event) -> {
            if (MouseButton.PRIMARY == event.getButton()
                    && event.getClickCount() == 2) {
                this.setResults(tv.getSelectionModel().getSelectedItem());
                this.resultProperty().set(ButtonType.NEXT);
            }
        });

        //dlg.getDialogSkin().getButtonTypes().add(1, ButtonType.OK);
        Button openCase = this.getDialogSkin().getButton(ButtonType.OK);
        openCase.setText("Fall öffnen");
        openCase.setAlignment(Pos.BOTTOM_CENTER);

        //((Button) dlg.getDialogPane().lookupButton(ButtonType.OK)).setAlignment(Pos.BOTTOM_LEFT);
        openCase.addEventFilter(
                ActionEvent.ACTION,
                evt -> {
                    // to prevent the dialog to close
                    evt.consume();
                    TCase selected = tv.getSelectionModel().getSelectedItem();
                    if (selected != null) {
                        final Window window = this.getDialogPane().getContent().getScene().getWindow();
                        AlertDialog dlg2 = AlertDialog.createConfirmationDialog("Möchten Sie den Fall " + selected.getCsHospitalIdent() + "/" + selected.getCsCaseNumber() + " öffnen?", window);
                        dlg2.initModality(Modality.WINDOW_MODAL);
                        dlg2.showAndWait().ifPresent((ButtonType t) -> {
                            if (t.equals(ButtonType.OK)) {
                                MainApp.getToolbarMenuScene().openCase(selected.id);
                            }
                        });
                    }
                }
        );

//                AnchorPane ap = new AnchorPane();
//                AnchorPane.setBottomAnchor(tv, 0d);
//                AnchorPane.setLeftAnchor(tv, 0d);
//                AnchorPane.setTopAnchor(tv, 0d);
//                AnchorPane.setRightAnchor(tv, 0d);
        VBox box = new VBox(pTitle, tv);
//                ap.getChildren().add(box);
        //ap.getChildren().add(tv);
        //tv.getSelectionModel().selectFirst();
        tv.getStyleClass().add(STAY_SELECTED_CLASS);
        this.getDialogPane().setContent(box);
//                dlg.onShownProperty().addListener((observable) -> {
//                    tv.requestFocus();
//                });
//                
//                dlg.onShowingProperty().addListener((observable) -> {
//                    tv.requestFocus();
//                });

        this.getDialogPane().getScene().getWindow().setOnShown((evt) -> tv.requestFocus());

        this.getDialogSkin().addButtonTypes(ButtonType.NEXT);
        Button okBtn = this.getDialogSkin().getButton(ButtonType.NEXT);
        okBtn.setText("OK");

        this.resultProperty().addListener((observable) -> {
            MainApp.getToolbarMenuScene().openDocumentImport();
            if (this.resultProperty().get() == ButtonType.NEXT) {
                //DocumentSearchProcess result = dlg.getResults();
                TCase selected = tv.getSelectionModel().getSelectedItem();
                if (pCallback != null) {
                    pCallback.call(selected);
                }
            }
        });

//                GridPane processGrid = new GridPane();
//                processGrid.setVgap(10d);
//                processGrid.setHgap(5d);
//                processGrid.getColumnConstraints().add(0, new ColumnConstraints());
//                processGrid.getColumnConstraints().add(1, new ColumnConstraints());
//                processGrid.getColumnConstraints().get(0).setHgrow(Priority.ALWAYS);
//                processGrid.getColumnConstraints().get(1).setHgrow(Priority.NEVER);
//                
//                int row = 0;
//                for(DocumentSearchProcess process: processes) {
//                    Button btn = new Button("Vorgang " + String.valueOf(process.workflowNumber));
//                    btn.setMaxWidth(Integer.MAX_VALUE);
//                    if (process.isMainFl || row == 0) {
//                        btn.setStyle("-fx-font-weight: bold");
//                        btn.requestFocus();
//                    }
//                    btn.setOnAction((evt) -> {
//                        txtProcessNumber.setText(String.valueOf(process.workflowNumber));
//                        dlg.close();
//                    });
//                    processGrid.addRow(row++, btn);
//                }
//                dlg.getButtonTypes().remove(ButtonType.OK);
//                dlg.getDialogPane().setContent(processGrid);
        this.getDialogPane().setPrefWidth(500d);
    }

    @Override
    public TCase onSave() {
        return null;
    }
}
