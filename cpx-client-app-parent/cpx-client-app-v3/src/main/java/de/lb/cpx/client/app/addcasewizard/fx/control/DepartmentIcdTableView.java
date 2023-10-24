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

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.client.app.addcasewizard.model.table.EditableIcdTableView;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.easycoder.EasyCoderDialog;
import de.lb.cpx.client.core.model.catalog.CpxIcdCatalog;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.util.OverrunHelper;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.server.commonDB.model.CIcdCatalog;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
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
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javax.validation.constraints.NotNull;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import org.controlsfx.validation.ValidationResult;

/*
 *
 * DEPARTMENT TABLE VIEW
 * create department based tableview
 * should be refactoered uses old base class
 *
 * @author wilde
 */
public class DepartmentIcdTableView extends EditableIcdTableView {

    private static final Logger LOG = Logger.getLogger(DepartmentIcdTableView.class.getName());

    private final TCaseDepartment department;
    private final AddCaseHospitalCaseDetailsFXMLController parentCtrl;


    /**
     * constructor, creates new instance
     *
     * @param pDepartment department
     */
    DepartmentIcdTableView(AddCaseHospitalCaseDetailsFXMLController pParentCtrl, TCaseDepartment pDepartment) {
        super();
        ToggleGroup tGroup = new ToggleGroup();
        parentCtrl = pParentCtrl;
        department = pDepartment;
        addIcdRow();
        principalDiagnosisColumn.setText(Lang.getDepartmentMainDiagnosisObj().getAbbreviation());
//            principalDiagnosisColumn.textProperty().addListener(new ChangeListener<String>() {
//                @Override
//                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                    System.out.println("new value " + newValue);
//                }
//            });
        //CPX-977 
        icdTextColumn.setCellValueFactory((TableColumn.CellDataFeatures<TCaseIcd, String> param) -> {
            String lang1 = CpxClientConfig.instance().getLanguage();
            int year1 = CpxClientConfig.instance().getSelectedGrouper() == GDRGModel.AUTOMATIC ? Lang.toYear(parentCtrl.getParentController().getDpAdmissionDate().getDate()) : CpxClientConfig.instance().getSelectedGrouper().getModelYear();
            return new SimpleObjectProperty<>(param.getValue().getIcdcCode() == null ? null : CpxIcdCatalog.instance().getByCode(param.getValue().getIcdcCode(), lang1, year1).getDescription());
        });
        icdTextColumn.setCellFactory((TableColumn<TCaseIcd, String> param) -> {
            TableCell<TCaseIcd, String> cell = new TableCell<TCaseIcd, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setGraphic(null);
                        return;
                    }

//                        TCaseIcd icd = (TCaseIcd) cell.getTableRow().getItem();
//                        CIcdCatalog icdCat = CpxIcdCatalog.instance().getByCode(icd.getIcdcCode(), lang, year);
//                        if (item != null) {
                    //CPX-997 addOverrunInfoButton for Icd Text
                    HBox hLabel = new HBox();
                    hLabel.setMinWidth(400);
                    hLabel.setMaxWidth(400);
                    hLabel.setAlignment(Pos.CENTER_LEFT);
                    Label label = new Label(item);
                    hLabel.getChildren().add(label);
                    OverrunHelper.addOverrunInfoButton(label);
                    setGraphic(hLabel);
//                        }
                }

            };
            return cell;
        });

        //CPX-997 sort icdCodeColumn
        icdCodeColumn.setCellValueFactory((TableColumn.CellDataFeatures<TCaseIcd, String> param) -> new SimpleObjectProperty<>(param.getValue().getIcdcCode()));
        icdCodeColumn.setCellFactory((TableColumn<TCaseIcd, String> param) -> {
            TableCell<TCaseIcd, String> cell = new TableCell<TCaseIcd, String>() {
               
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setText("");
                        return;
                    }
                    setText(item);
                }

            };
            cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                 BooleanProperty busy = new SimpleBooleanProperty(false);
                @Override
                public void handle(MouseEvent event) {
                    if (cell.isEmpty()) {
                        return;
                    }
                    TCaseIcd icd = cell.getTableRow().getItem();
                    if (icd != null) {
                        handleEditEvent(icd);
                    } else {
                        refresh();
                    }
                }

                private String icdCodeGenerator(String text) {

                    int year = parentCtrl.getParentController().getDpAdmissionDate().getLocalDate().getYear();
                     setCatalogYear(parentCtrl.getParentController().getDpAdmissionDate().getDate());
                    String icdCode = checkCode(text);
                    if (!validateIcd(text)) {
                        AlertDialog dialog = AlertDialog.createInformationDialog(Lang.getGroupResultCodeValidInvalid() + " '" + icdCode + "'\n" + Lang.getCatalogIcdError(String.valueOf(year)), parentCtrl.getWindow());
                        dialog.setHeaderText(Lang.getDaysUnbilledDialogConfirm());
                        dialog.setResizable(true);
                        dialog.initOwner(parentCtrl.getScene().getOwner());
                        dialog.getDialogPane().setPrefSize(450, 230);
                        dialog.showAndWait();

                    return null;
                    } else {
                        return icdCode;
                    }
                }
                
                private String checkCode(@NotNull String text){
                    String icdCode = text.trim().toUpperCase();
                    if (icdCode.isEmpty()) {
                        return null;
                    }
                    if (icdCode.length() > 3 && !icdCode.contains(".")) {
                        icdCode = icdCode.substring(0, 3) + "." + icdCode.substring(3, icdCode.length());
                    }
                    return icdCode;
                }
                
                private boolean validateIcd(@NotNull String text){
                    if(text.length() == 0 ){
                        return true;
                    }
                    String lang = CpxClientConfig.instance().getLanguage();
                    int year = parentCtrl.getParentController().getDpAdmissionDate().getLocalDate().getYear();
                    CIcdCatalog icdCat = CpxIcdCatalog.instance().getByCode(checkCode(text), lang, year);
                    return (icdCat!= null && icdCat.getIcdIsCompleteFl() != null && icdCat.getIcdIsCompleteFl()) ;
                }

                private void handelIcdCode(String newIcd, TCaseIcd icd) {
                    String icdCode = icdCodeGenerator(newIcd);
                    if (icdCode != null) {
                        saveIcd(icdCode, icd);
                    }
                    scrollTo(getItems().indexOf(icd) + 1);
                    parentCtrl.getParentController().setIsBusy(false);
                }

                private void handleEditEvent(TCaseIcd icd) {
                    if (parentCtrl.getParentController().getDpAdmissionDate().getLocalDate() != null) {
                        TextField newIcd = new TextField(icd.getIcdcCode());

//                            cell.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
//                                @Override
//                                public void handle(MouseEvent e) {
//                                    refresh();
//                                }
//                            });
                        newIcd.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                            if (newIcd.getText() != null && newIcd.getText().length() > 6) {
                                newIcd.setText(newIcd.getText().substring(0, 6));
                            }

//                            parentCtrl.getValidationSupport().registerValidator(newIcd, (Control t, String u) -> {
//                                ValidationResult res = new ValidationResult();
//                                if (u != null) {
//                                    int year = parentCtrl.getParentController().getDpAdmissionDate().getLocalDate().getYear();
//                                    res.addErrorIf(t, Lang.getCatalogIcdError(String.valueOf(year)), !validateIcd(newIcd.getText()));
//
//                                }
//                                return res;
//                            });
                        
                        });
//                        busy.addListener(new ChangeListener<Boolean>(){
//                            @override
//                            public void changed(ObservableValue<? extends Boolean > observable, Boolean oldValue, Boolean newValue){
//                                parentCtrl.getValidationSupport().registerValidator(newIcd, (Control t, String u) -> {
//                                    ValidationResult res = new ValidationResult();
//                                    if (u != null) {
//                                        int year = parentCtrl.getParentController().getDpAdmissionDate().getLocalDate().getYear();
//                                        res.addErrorIf(t, "is busy", busy.get());
//
//                                    }
//                                    return res;
//                                });
//                        
//                                
//                            }
//                            
//                        });
                        
                        newIcd.focusedProperty().addListener((arg0, oldValue, newValue) -> {
                            if (!newValue) {
                                if (newIcd.getText() != null && !trimToEmpty(newIcd.getText()).isEmpty()) {
                                    refresh();
                                    handelIcdCode(trimToEmpty(newIcd.getText()), icd);
//                                    LOG.log(Level.FINEST, "set Busy:true");
//                                    parentCtrl.getParentController().setIsBusy(true);
//
                                } else {
                                    refresh();
                                    LOG.log(Level.FINEST, "set Busy:false");
//                                    parentCtrl.getParentController().setIsBusy(false);
                                }
                            }else{
                                LOG.log(Level.INFO, "new value");
                            }

                        });
                        newIcd.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>(){
                            @Override
                            public void handle(ActionEvent event) {
                                event.consume();
                                LOG.log(Level.FINEST, "set Busy:true");
                                parentCtrl.getParentController().setIsBusy(true);
//                                busy.set(true);
                                return;
                            }                            
                        });
                        
                        newIcd.setOnKeyPressed((KeyEvent ke) -> {
                            //                                    if (ke.getCode().equals(KeyCode.TAB)) {
//                                        refresh();
//                                        LOG.log(Level.INFO, "Tab at ICD is detect  , save ICD ...");
//                                        if (newIcd.getText() != null) {
//                                            refresh();
//                                            
//                                            handelIcdCode(newIcd, icd);
//                                        } else {
//                                            refresh();
//                                            ke.consume();
//                                            LOG.log(Level.INFO, "ICD text is null! ");
//                                        }
//
//                                    }
//Easy Coder öffen if Enter
                            if (ke.getCode().equals(KeyCode.ENTER)) {
                                LOG.log(Level.INFO, "Enter at ICD is detect , open Easy Coder ...");
                                String searchItem = trimToEmpty(newIcd.getText());
                                newIcd.setText("");
                                editIcd(searchItem);
                                scrollTo(cell.getTableRow().getIndex() + 1);
                                LOG.log(Level.FINEST, "set Busy:false");
                                parentCtrl.getParentController().setIsBusy(false);
//                                busy.set(false);
                                refresh();
                            }
                            if (ke.getCode().equals(KeyCode.ESCAPE)) {
                                scrollTo(getItems().indexOf(icd) + 1);
                                newIcd.setText("");
 LOG.log(Level.FINEST, "set Busy:false");
 parentCtrl.getParentController().setIsBusy(false);
//                                busy.set(false);
                                refresh();
                            }
                           if (ke.getCode().equals(KeyCode.TAB)) {
                                scrollTo(getItems().indexOf(icd) + 1);
                                newIcd.setText("");
LOG.log(Level.FINEST, "set Busy:false");       
parentCtrl.getParentController().setIsBusy(false);
//                                busy.set(false);
                                refresh();
                            }
                        });
                        newIcd.setPrefWidth(80);
                        cell.setGraphic(newIcd);
                        Platform.runLater(newIcd::requestFocus);
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

        principalDiagnosisColumn.setCellFactory((TableColumn<TCaseIcd, TCaseIcd> param) -> {
            TableCell<TCaseIcd, TCaseIcd> cell = new TableCell<>() {
                private final RadioButton rb = new RadioButton();

                @Override
                protected void updateItem(TCaseIcd item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        return;
                    }
                    TCaseIcd icd = item;
                    rb.setUserData(icd);
                    rb.setSelected(icd.getIcdcIsHdbFl());
                    rb.setOnMouseClicked((MouseEvent event) -> {
                        //                                    if (isArmed()) {
                        if (rb.isFocused()) {
                            if (!icd.getIcdcIsHdbFl()) {
                                resetMainDiagnosis();
                                resetMainDpDiagnosis();
                                icd.setIcdcIsHdbFl(true);
//                                                if(department.isHA()) icd.setIcdcIsHdxFl(true);
                                if (!icd.getIcdIsToGroupFl()) {
                                    icd.setIcdIsToGroupFl(true);
                                }
                                refresh();
                            }
                        }
//                                    } else {
//                                        resetMainDiagnosis();
//                                        rb.setSelected(icd.getIcdcIsHdxFl());
//                                        refresh();
//                                    }
                    });
                    setGraphic(rb);
                    rb.setToggleGroup(tGroup);
                }
            };
            return cell;
        });
    }

    @Override
    public void refresh() {
        super.refresh();
        ObservableList<TCaseIcd> itms = getItems();
        setItems(null);
        setItems(itms);


    }

    @Override
    public void remove(TCaseIcd pItem) {
        super.remove(pItem);
        department.getCaseIcds().remove(pItem);
    }

    /**
     * Add blank Icd Row
     */
    public final void addIcdRow() {
        TCaseIcd pItem = new TCaseIcd(department.getCreationUser());
        super.add(pItem);
    }

    /**
     * Save Text from User as ICD
     *
     * @param userText Text from User
     * @param icd ICD
     */
    public void saveIcd(String userText, TCaseIcd icd) {
        LOG.log(Level.INFO, "save ICD ... ");
        if (userText != null) {
            icd.setIcdcCode(userText);
            if (icd.getIcdcCode() != null) {
                icd.setCaseDepartment(department);
                department.getCaseIcds().add(icd);
            }
            refresh();
            removeBlankRows(getItems());
            addIcdRow();
            refresh();

        }
    }

    /**
     * Öffnen Easy Coder for ICD search
     *
     * @param userText Text from User
     */
    public void editIcd(String userText) {
        LOG.log(Level.INFO, "Search ICD Code '" + userText + "' in easy Coder ...");
        final EasyCoderDialog catalog = parentCtrl.initEasyCoder();

        if (catalog == null) {
            return;
        }

        if (userText != null) {
            catalog.setDiagnosenSearch(userText);
        } else {
            catalog.setDiagnosenSearch("");
        }
        catalog.showAndWait().ifPresent((ButtonType t) -> {
            if (t.equals(ButtonType.OK)) {
                List<TCaseIcd> tmpIcdList = new ArrayList<>();
                for (TCaseIcd caseIcd : catalog.getSelectedTCaseIcd()) {
                    caseIcd.setCaseDepartment(department);
                    department.getCaseIcds().add(caseIcd);
                    tmpIcdList.add(caseIcd);
                }
                Set<TCaseIcd> icdSet1 = new HashSet<>();
                icdSet1.addAll(tmpIcdList);
                icdSet1.addAll(department.getCaseIcds());
                parentCtrl.setNewIcdData(icdSet1);
                addIcdRow();
                parentCtrl.removeBlankIcds(icdSet1);
                refresh();
            } else if (t.equals(ButtonType.CANCEL)) {
                catalog.closeEasyCoder();

            }
 LOG.log(Level.FINEST, "set Busy:false");
 parentCtrl.getParentController().setIsBusy(false);
        });
        refresh();
    }
}
