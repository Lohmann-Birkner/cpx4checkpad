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
import de.lb.cpx.client.core.model.fx.dialog.FormularDialog;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.shared.lang.Lang;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
public class DocumentImportPatientSelectDialog extends FormularDialog<TPatient> {

    private final static String STAY_SELECTED_CLASS = "stay-selected-table-view";

    @SuppressWarnings("unchecked")
    public DocumentImportPatientSelectDialog(TextFlow pTitle, final List<TPatient> pPatients, Window pParentWindow, Callback<TPatient, Void> pCallback) {
        super(pParentWindow, Modality.WINDOW_MODAL, "Patient auswählen");
        ObservableList<TPatient> obsPatient = FXCollections.observableArrayList(pPatients);
        TableView<TPatient> tv = new TableView<>(obsPatient);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        final TableColumn<TPatient, TPatient> colPatientName = new TableColumn<>("Patient");
        colPatientName.setCellValueFactory(new SimpleCellValueFactory<>());
        colPatientName.setCellFactory((TableColumn<TPatient, TPatient> param) -> {
            TableCell<TPatient, TPatient> cell = new TableCell<>() {

                @Override
                protected void updateItem(TPatient item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setText("");
                        return;
                    }
                    TPatient pat = item;
                    setText(pat.getPatFullName());
                }
            };
            return cell;
        });
        final TableColumn<TPatient, TPatient> colPatientNumber = new TableColumn<>("Patient Nr.");
        colPatientNumber.setCellValueFactory(new SimpleCellValueFactory<>());
        colPatientNumber.setCellFactory((TableColumn<TPatient, TPatient> param) -> {
            TableCell<TPatient, TPatient> cell = new TableCell<>() {

                @Override
                protected void updateItem(TPatient item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setText("");
                        return;
                    }
                    TPatient pat = item;
                    setText(pat.getPatNumber());
                }
            };
            return cell;
        });
        final TableColumn<TPatient, TPatient> colPatientDateOfBirth = new TableColumn<>("Geburtsdatum");
        colPatientDateOfBirth.setCellValueFactory(new SimpleCellValueFactory<>());
        colPatientDateOfBirth.setCellFactory((TableColumn<TPatient, TPatient> param) -> {
            TableCell<TPatient, TPatient> cell = new TableCell<>() {

                @Override
                protected void updateItem(TPatient item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setText("");
                        return;
                    }
                    TPatient pat = item;
                    setText(Lang.toDate(pat.getPatDateOfBirth()));
                }
            };
            return cell;
        });
        tv.getColumns().addAll(colPatientNumber, colPatientDateOfBirth, colPatientName);
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

        tv.setOnMouseClicked((MouseEvent event1) -> {
            if (MouseButton.PRIMARY == event1.getButton() && event1.getClickCount() == 2) {
                this.setResults(tv.getSelectionModel().getSelectedItem());
                this.resultProperty().set(ButtonType.NEXT);
            }
        });
        VBox box = new VBox(pTitle, tv);
        tv.getStyleClass().add(STAY_SELECTED_CLASS);
        this.getDialogPane().setContent(box);
        this.getDialogPane().getScene().getWindow().setOnShown((evt) -> tv.requestFocus());

        this.getDialogSkin().setButtonTypes(ButtonType.NEXT, ButtonType.CANCEL);
        this.resultProperty().addListener((observable) -> {
            MainApp.getToolbarMenuScene().openDocumentImport();
            if (this.resultProperty().get() == ButtonType.NEXT) {
                //DocumentSearchProcess result = dlg.getResults();
                TPatient selected = tv.getSelectionModel().getSelectedItem();
                if (pCallback != null) {
                    pCallback.call(selected);
                }
            }
        });
        this.getDialogPane().setPrefWidth(500d);
    }

    @Override
    public TPatient onSave() {
        return null;
    }
}
