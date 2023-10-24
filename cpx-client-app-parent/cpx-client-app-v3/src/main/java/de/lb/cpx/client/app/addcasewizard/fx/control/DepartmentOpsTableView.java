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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.addcasewizard.fx.control;

import de.lb.cpx.client.app.addcasewizard.model.table.EditableOpsTableView;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.easycoder.EasyCoderDialog;
import de.lb.cpx.client.core.model.catalog.CpxOpsCatalog;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.labeled.LabeledDatePicker;
import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.server.commonDB.model.COpsCatalog;
import de.lb.cpx.shared.lang.AbstractLang;
import de.lb.cpx.shared.lang.Lang;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import javax.validation.constraints.NotNull;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import org.controlsfx.control.PopOver;

/*
 *
 * DEPARTMENT OPS TABLE VIEW
 *
 * @author wilde
 */
public class DepartmentOpsTableView extends EditableOpsTableView {

    private static final Logger LOG = Logger.getLogger(DepartmentOpsTableView.class.getName());

    private final TCaseDepartment department;
    private final AddCaseHospitalCaseDetailsFXMLController parentCtrl;

    DepartmentOpsTableView(AddCaseHospitalCaseDetailsFXMLController pParentCtrl, TCaseDepartment pDepartment) {
        super();
        parentCtrl = pParentCtrl;
        department = pDepartment;
        addOpsRow();
        //CPX-1022 RSH  sort opsDateColumn
        opsDateColumn.setCellValueFactory((TableColumn.CellDataFeatures<TCaseOps, Date> param) -> new SimpleObjectProperty<>(param.getValue().getOpscDatum()));
//            opsDateColumn.setSortable(true);
        opsDateColumn.setCellFactory(new Callback<TableColumn<TCaseOps, Date>, TableCell<TCaseOps, Date>>() {
            @Override
            public TableCell<TCaseOps, Date> call(TableColumn<TCaseOps, Date> param) {

                TableCell<TCaseOps, Date> cell = new TableCell<TCaseOps, Date>() {
                    @Override
                    protected void updateItem(Date item, boolean empty) {
                        super.updateItem(item, empty);
                        setText("");
                        if (empty || getItem() == null) {
                            return;
                        }
                        setText(Lang.toDate(item));
                        if (item != null && dateoutRange(Lang.toLocalDate(item))) {
                            setStyle("-fx-text-fill: goldenrod");
                            setTooltip(new Tooltip(Lang.getValidationWarningOpsDate()));
                        } else {
                            setTooltip(null);
                        }
                    }
                };
//                    cell.itemProperty().addListener(new ChangeListener() {
//                        @Override
//                        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
//                            if (cell.getTableRow() != null && newValue != null) {
//                                cell.setItem((Date) newValue);
//                                TCaseOps ops = getOpsFromRow(cell);
//                                if (ops != null) {
//                                    ops.setOpscDatum((Date) newValue);
//                                    refresh();
//                                }
//                            }
//                        }
//                    });

                //creates Popover layout to manipulate Date-Object
                cell.setOnMouseClicked((MouseEvent event) -> {
                    if (cell.isEmpty()) {
                        return;
                    }
                    //CPX-642 RSH :08122017 Improved date input in the procedures
                    TCaseOps ops = getOpsFromRow(cell);
                    if (ops != null && ops.getOpscCode() == null) {
                        AlertDialog dialog = AlertDialog.createErrorDialog(Lang.getOPSCodeValidation(), ButtonType.OK);
                        dialog.initOwner(parentCtrl.getScene().getOwner()); 
                        dialog.showAndWait();
                        LOG.log(Level.WARNING, "OPS is null!");
                        return;
                    }
                    HBox hBox = new HBox();
                    VBox vBox = new VBox();
                    Label label = new Label(" ");
                    vBox.getChildren().addAll(hBox);
                    LabeledDatePicker datePicker = new LabeledDatePicker(trimToEmpty(label.getText()));
                    if (department.getDepcAdmDate() != null) {
                        if (ops == null) {
                            datePicker.setDate(null);
                        } else if (ops.getOpscDatum() == null) {
//                                    datePicker.setDate(new Date(Lang.toDate(department.getDepcAdmDate())));
//                                    datePicker.setDate(department.getDepcAdmDate());
                            datePicker.setDate(java.sql.Date.valueOf(Lang.toLocalDate(department.getDepcAdmDate())));
                            ops.setOpscDatum(department.getDepcAdmDate());
                        } else {
//                                    datePicker.setDate(new Date(Lang.toDate(Lang.toLocalDate(ops.getOpscDatum()))));
//                                    datePicker.setDate(ops.getOpscDatum());
                            datePicker.setDate(java.sql.Date.valueOf(Lang.toLocalDate(ops.getOpscDatum())));
                        }
//                                cell.setItem(java.sql.Date.valueOf(datePicker.getLocalDate()));

                    }
                    hBox.getChildren().addAll(datePicker);
                    datePicker.getControl().valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
                        //                                    if (newValue instanceof LocalDate) {
                        LOG.log(Level.WARNING, "add OPS Date...");
                        cell.setItem(java.sql.Date.valueOf(datePicker.getLocalDate()));
//                                       opsDateColumn.getTableView().refresh();

//                                        if (datePicker.getDate().after(department.getDepcAdmDate()) || datePicker.getDate().equals(department.getDepcAdmDate())) {
////                                            cell.setItem(java.sql.Date.valueOf(datePicker.getLocalDate()));
//
//                                        } else {
////                                            cell.setItem(null);
////                                            AlertDialog.createErrorDialog("Geben Sie bitte güliges Datum ein", ButtonType.OK).showAndWait();
//                                        }
//                                    }
                    }//                                @Override
                    //                                public void changed(ObservableValue<? extends LocalDate> ov, LocalDate t, LocalDate newValue) {
                    //                                    if (newValue instanceof LocalDate) {
                    //                                        LOG.log(Level.WARNING, "add OPS Date...");
                    ////                                        cell.setItem(java.sql.Date.valueOf(datePicker.getLocalDate()));
                    //                                        cell.setItem(java.sql.Date.valueOf(newValue));
                    //
                    //                                    }
                    //                                }
                    );
                    PopOver popover = new AutoFitPopOver();
                    popover.setHideOnEscape(true);
                    popover.setAutoHide(true);
                    popover.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
                    popover.setDetachable(false);
                    popover.setContentNode(vBox);
                    popover.show(cell);
                    //                            datePicker.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    popover.getContentNode().setOnKeyPressed(new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent ke) {
                            if (ke.getCode().equals(KeyCode.ENTER)) {
                                if (datePicker.getLocalDate() != null) {
                                    cell.setItem(java.sql.Date.valueOf(datePicker.getLocalDate()));
                                    final LocalDate date = AbstractLang.toLocalDate(datePicker.getControl().getEditor().getText());
                                    datePicker.setDate(java.sql.Date.valueOf(date));
                                    cell.setText(Lang.toDate(datePicker.getLocalDate()));
                                    ops.setOpscDatum(java.sql.Date.valueOf(datePicker.getLocalDate()));
                                    if (dateoutRange(Lang.toLocalDate(cell.getText()))) {
                                        datePicker.showErrorPopOver(Lang.getValidationWarningOpsDate(), 5);
                                        popover.hide(Duration.seconds(6));
                                        setStyle("-fx-text-fill: goldenrod");
                                    } else {
                                        popover.hide(Duration.ZERO);
                                    }

                                } else {
                                    cell.setText(Lang.toDate(ops.getOpscDatum()));
                                }
                                ke.consume();
                            }
                            if (ke.getCode().equals(KeyCode.ESCAPE)) {
                                ke.consume();
                                refresh();
                            }
                        }

                    });
                    popover.ownerWindowProperty().get().setOnCloseRequest((WindowEvent event1) -> {
                        popover.hide(Duration.ZERO);
                    });
                });
                return cell;

            }

            private boolean dateoutRange(LocalDate date) {
                Boolean outRange = false;
                if (department.getDepcAdmDate() != null) {
                    if (!Lang.toLocalDate(department.getDepcAdmDate()).minusDays(1).isBefore(date)) {
                        outRange = true;
                    }
                }
                if (department.getDepcDisDate() != null) {
                    if (!Lang.toLocalDate(department.getDepcDisDate()).plusDays(1).isAfter(date)) {
                        outRange = true;
                    }
                }
                return outRange;
            }

            private TCaseOps getOpsFromRow(TableCell<TCaseOps, Date> pCell) {
                if (pCell.getTableRow().getItem() != null) {
                    return pCell.getTableRow().getItem();
                }
                return null;
            }
        });
        //CPX-997 Sort OpsCpdeColumn 
        opsCodeColumn.setCellValueFactory((TableColumn.CellDataFeatures<TCaseOps, String> param) -> new SimpleObjectProperty<>(param.getValue().getOpscCode()));
        opsCodeColumn.setCellFactory((TableColumn<TCaseOps, String> param) -> {
            TableCell<TCaseOps, String> cell = new TableCell<TCaseOps, String>() {

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText("");
                    if (item == null) {                       
                        return;
                    }
//                            TCaseOps ops = (TCaseOps) item;
                    setText(item);
                }
            };
            cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (cell.isEmpty()) {
                        return;
                    }
                    TCaseOps ops = cell.getTableRow().getItem();
                    if (ops != null) {
                        handleEditEvent(ops);
                    } else {
                        refresh();
                    }
                }

        private String opsCodeGenerator(String text) {
            if(text == null){
                return "";
            }
            String opsCode = checkCode(text);
            int year = parentCtrl.getParentController().getDpAdmissionDate().getLocalDate().getYear();
            setCatalogYear(parentCtrl.getParentController().getDpAdmissionDate().getDate());
            if(!validateOps(opsCode)){
                AlertDialog dialog = AlertDialog.createInformationDialog(Lang.getGroupResultCodeValidInvalid() + " '" + opsCode + "'\n" + Lang.getCatalogOpsError(String.valueOf(year)), parentCtrl.getWindow());
                dialog.setHeaderText(Lang.getDaysUnbilledDialogConfirm());
                dialog.setResizable(true);
                dialog.getDialogPane().setPrefSize(450, 230);
                dialog.initOwner(parentCtrl.getScene().getOwner());
                dialog.showAndWait();
                return null;
            } else {
                return opsCode;
        }

    }
                
                
    private String checkCode(@NotNull String text){
        String opsCode = text.trim();
        if (opsCode.isEmpty()) {
            return null;
        }
        if (opsCode.length() > 1) {
            if (!opsCode.contains("-")) {
                opsCode = opsCode.substring(0, 1) + "-" + opsCode.substring(1, opsCode.length());
            }
            if (opsCode.length() > 5 && !opsCode.contains(".")) {
                opsCode = opsCode.substring(0, 5) + "." + opsCode.substring(5, opsCode.length());
            }

        }
        return opsCode;

    }
    
    private boolean validateOps(@NotNull String text){
        if(text.length() == 0 ){
            return true;
        }
        String lang = CpxClientConfig.instance().getLanguage();
        int year = parentCtrl.getParentController().getDpAdmissionDate().getLocalDate().getYear();
        COpsCatalog opsCat = CpxOpsCatalog.instance().getByCode(text, lang, year);
        return (opsCat!= null && opsCat.getOpsIsCompleteFl() != null && opsCat.getOpsIsCompleteFl()); 
        
    }
            private void handelOpsCode(String newOps, TCaseOps ops) {
                String opsCode = opsCodeGenerator(newOps);
                if (opsCode != null) {
                    saveOps(opsCode, ops);
                }
                scrollTo(ops);
            }

            private void handleEditEvent(TCaseOps ops) {
                if (parentCtrl.getParentController().getDpAdmissionDate().getLocalDate() != null) {
                    TextField newOps = new TextField(ops.getOpscCode());
                    int selectedIndex = getSelectionModel().getSelectedIndex();
                        newOps.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent e) {

                                refresh();
                            }
                        });
                    newOps.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                        if (newOps.getText() != null && newOps.getText().length() > 9) {
                            newOps.setText(newOps.getText().substring(0, 9));
                        }
                    });
                    newOps.focusedProperty().addListener((arg0, oldValue, newValue) -> {
                        if (!newValue) {
                            if (newOps.getText() != null && !trimToEmpty(newOps.getText()).isEmpty()) {
                                refresh();
                                handelOpsCode(trimToEmpty(newOps.getText()), ops);
                            } else {
                                refresh();
                            }
                        }

                    });
                    newOps.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>(){
                        @Override
                        public void handle(ActionEvent event) {
                            event.consume();
                            parentCtrl.getParentController().setIsBusy(true);
//                                busy.set(true);
                            return;
                        }                            
                    });

                        newOps.setOnKeyPressed((KeyEvent ke) -> {
                            //save OPS Code if TAB
//                                    if (ke.getCode().equals(KeyCode.TAB)) {
//                                        LOG.log(Level.INFO, "Tab at  OPS is detect ,save OPS ... ");
//                                        if (newOps.getText() != null) {
//                                        handelOpsCode(newOps,ops);
//
//                                        } else {
//                                            ke.consume();
//                                            LOG.log(Level.WARNING, "Ops is null! ");
//                                        }
//                                    }
//Easy Coder öffen if Enter
                            if (ke.getCode().equals(KeyCode.ENTER)) {
                                LOG.log(Level.INFO, "Enter at  OPS is detect ,search OPS ...  ");
                                String searchItem = trimToEmpty(newOps.getText());
                                newOps.setText("");
                                editOps(searchItem);
                                scrollTo(ops);
                                parentCtrl.getParentController().setIsBusy(false);
                                refresh();

                            }
                            if (ke.getCode().equals(KeyCode.ESCAPE)) {
                                scrollTo(getItems().indexOf(ops) + 1);
                                newOps.setText("");
                                parentCtrl.getParentController().setIsBusy(false);
                                refresh();
                            }
                            if (ke.getCode().equals(KeyCode.TAB)) {
                                scrollTo(getItems().indexOf(ops) + 1);
                                newOps.setText("");
                                parentCtrl.getParentController().setIsBusy(false);
                                refresh();
                            }
                        });
                        newOps.setPrefWidth(80);
                        cell.setGraphic(newOps);
                        Platform.runLater(newOps::requestFocus);
                    } else {
                        refresh();
                        LOG.log(Level.WARNING, "admissiondate is null!");
                        AlertDialog dialog = AlertDialog.createInformationDialog(Lang.getValidationErrorNoAdmissionDayCase());
                        dialog.setHeaderText(Lang.getDaysUnbilledDialogConfirm());
                        dialog.setResizable(true);
                        dialog.getDialogPane().setPrefSize(450, 80);
                        dialog.initOwner(parentCtrl.getScene().getOwner());
                        dialog.showAndWait();
                    }
                }
            });
            return cell;
        });

    }

    @Override
    public void refresh() {
        super.refresh();
        ObservableList<TCaseOps> itms = getItems();
        setItems(null);
        setItems(itms);
    }

    @Override
    public void remove(TCaseOps pItem) {
        super.remove(pItem);
        department.getCaseOpses().remove(pItem);
    }

    /**
     * Add blank OPS Row
     */
    public final void addOpsRow() {
        TCaseOps pItem = new TCaseOps(department.getCreationUser());
        super.add(pItem);
    }

    /**
     * save userText as OPS
     *
     * @param userText Text from User
     * @param ops OPS
     */
    private void saveOps(String userText, TCaseOps ops) {
        LOG.log(Level.INFO, "save OPS ...");
        if (userText != null) {
            ops.setOpscCode(userText);
            if (ops.getOpscCode() != null) {
                ops.setCaseDepartment(department);
                department.getCaseOpses().add(ops);
                ops.setOpscDatum(department.getDepcAdmDate());
            }
            refresh();
            removeBlankRows(getItems());
            addOpsRow();
        }
    }

    /**
     * Öffnen Easy Coder for OPS Search
     *
     * @param userText Text from User
     */
    private void editOps(String userText) {
        LOG.log(Level.INFO, "search ops code'" + userText + "' in Easy Coder.");
        final EasyCoderDialog catalog = parentCtrl.initEasyCoder(); 

        if (catalog == null) {
            return;
        }

        if (userText != null) {
            catalog.setProzudureSearch(userText);
        } else {
            catalog.setProzudureSearch("");
        }
        catalog.showAndWait().ifPresent((ButtonType t) -> {
            if (t.equals(ButtonType.OK)) {
                List<TCaseOps> tmpOpsList = new ArrayList<>();
                catalog.closeEasyCoder();
                for (TCaseOps caseOps : catalog.getSelectedTCaseOps()) {
                    caseOps.setCaseDepartment(department);
                    department.getCaseOpses().add(caseOps);
                    tmpOpsList.add(caseOps);
                }
                Set<TCaseOps> opsSet = new HashSet<>();
                opsSet.addAll(department.getCaseOpses());
                opsSet.addAll(tmpOpsList);
                parentCtrl.removeBlankOpses(opsSet);
                parentCtrl.setNewOpsData(opsSet);
                addOpsRow();

                refresh();
            } else if (t.equals(ButtonType.CANCEL)) {
                catalog.closeEasyCoder();
            }
            parentCtrl.getParentController().setIsBusy(false);
        });
        refresh();
    }

}
