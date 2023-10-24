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
import de.lb.cpx.client.core.easycoder.EasyCoderDialog;
import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledDatePicker;
import de.lb.cpx.client.core.model.fx.labeled.LabeledNumberSpinner;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTimeSpinner;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.enums.AdmissionModeEn;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.shared.lang.Lang;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.util.StringConverter;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;

/*
 *
 * DEPARTMENT NODE
 *
 * @author wilde
 */
public class DepartmentNode extends VBox {

    private static final Logger LOG = Logger.getLogger(DepartmentNode.class.getName());

    private final TCaseDepartment department;
    private final ValidationSupport validSupport = new ValidationSupport();
    private LabeledTextField tfDepartmentName;
    private LabeledNumberSpinner nsVentilation;
    private LabeledDatePicker dpStayFrom;
    private LabeledDatePicker dpStayTo;
    private DepartmentOpsTableView tableOps;
    private DepartmentIcdTableView tableIcd;
    private LabeledComboBox<AdmissionModeEn> cbAdmissionMode;
    private Label labelOps;
    private Label labelIcd;
    private Button buttonEditIcdOps;
    private LabeledTimeSpinner tsStayTo;
    private LabeledTimeSpinner tsStayFrom;
    private TabPane tabPane;
    private Tab icdTab;
    private Tab opsTab;
    private final AddCaseHospitalCaseDetailsFXMLController parentCtrl;

    /**
     * creates new instance for department
     *
     * @param pDepartment department entity
     */
//    DepartmentNode(AddCaseHospitalCaseDataFXMLController pParentCtrl, TCaseDepartment pDepartment) {
    DepartmentNode(AddCaseHospitalCaseDetailsFXMLController pParentCtrl, TCaseDepartment pDepartment) {
        super();
        parentCtrl = pParentCtrl;
        department = pDepartment;
        setMargin(this, new Insets(5));
        setUpCtrls();
        setUpLocalization();
        setUpValues();
        setUpListeners();
        setUpBindingAndValidation();
        createLayout();
        if (department == null) {
            setDisableInDepartments(true);
        }
    }
//        public void dispose(){
//            validSupport = null;
//            getChildren().clear();
//            tableIcd.getItems().clear();
//            tableOps.getItems().clear();
//        }

    /**
     * @param disable controls in this node
     */
    public final void setDisableInDepartments(boolean disable) {
        tfDepartmentName.setDisable(disable);
        nsVentilation.setDisable(disable);
        dpStayFrom.setDisable(disable);
        tsStayFrom.setDisable(disable);
        dpStayTo.setDisable(disable);
        tsStayTo.setDisable(disable);
        buttonEditIcdOps.setDisable(disable);
        cbAdmissionMode.setDisable(disable);
        tableIcd.setDisable(disable);
        tableOps.setDisable(disable);
    }

    /**
     * clear values of the controls in the node
     */
    public void clearCtrls() {
        getChildren().clear();
    }

    /**
     * @return validation result of the node
     */
    public ValidationResult getValidationResult() {
        return validSupport.getValidationResult();
    }

    /**
     * @return indicator if validtion is invalid
     */
    public boolean isInvalid() {
        return validSupport.isInvalid();
    }

    /**
     * @return department entity
     */
    public TCaseDepartment getDepartment() {
        return department;
    }

    /**
     * @return icd tableview
     */
    public DepartmentIcdTableView getIcdTableView() {
        parentCtrl.refresh();
        return tableIcd;
    }

    /**
     * @return ctrl for department name
     */
    public LabeledTextField getDepartmentNameCtrl() {
        return tfDepartmentName;
    }

    /**
     * @return ops table view
     */
    public EditableOpsTableView getOpsTableView() {
        return tableOps;
    }

    private void setUpCtrls() {
        tfDepartmentName = new LabeledTextField();
        nsVentilation = new LabeledNumberSpinner();
        dpStayTo = new LabeledDatePicker();
        tsStayTo = new LabeledTimeSpinner();
        dpStayFrom = new LabeledDatePicker();
        tsStayFrom = new LabeledTimeSpinner();
        cbAdmissionMode = new LabeledComboBox<>();
        buttonEditIcdOps = new Button();
        labelOps = new Label();
        labelIcd = new Label();

        tableOps = new DepartmentOpsTableView(parentCtrl, department);
        tableIcd = new DepartmentIcdTableView(parentCtrl, department);  
        tableOps.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableIcd.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        cbAdmissionMode.setItems(FXCollections.observableArrayList(AdmissionModeEn.values(parentCtrl.getParentController().caseTypeProperty().get())));
        cbAdmissionMode.setConverter(new StringConverter<AdmissionModeEn>() {
            @Override
            public String toString(AdmissionModeEn object) {
                if (object == null) {
                    return "";
                }
                return object.toString();
            }

            @Override
            public AdmissionModeEn fromString(String string) {
                return AdmissionModeEn.valueOf(string);
            }
        });
        //buttonEditIcdOps.setText(Lang.getEdit());
        buttonEditIcdOps.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.KEY));
        buttonEditIcdOps.getStyleClass().add("cpx-icon-button");
    }

    private void setUpLocalization() {
        tfDepartmentName.setTitle(Lang.getDepartmentTitle());
        tfDepartmentName.applyFontWeightToTitle(FontWeight.BOLD);
        nsVentilation.setTitle(Lang.getArtificialVentilation());
        cbAdmissionMode.setTitle(Lang.getModeOfAdmissionDepartment());
        dpStayFrom.setTitle(Lang.getInputDateFrom());
        tsStayFrom.setTitle(Lang.getInputTimeFrom());
        dpStayTo.setTitle(Lang.getInputDateTo());
        tsStayTo.setTitle(Lang.getInputTimeTo());
        labelIcd.setText(Lang.getDiagnosis());
        labelOps.setText(Lang.getOPSCode());

        tableOps.setPlaceholder(new Label(Lang.getOpsPlaceholder()));
        tableIcd.setPlaceholder(new Label(Lang.getIcdPlaceholder()));
    }

    public final void setUpValues() {
        if (department != null) {
            String depDesc = parentCtrl.getServiceFacade().getDepartmentNameWithDesc(department.getDepKey301(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            tfDepartmentName.setText(depDesc);
            if (depDesc != null && !depDesc.isEmpty()) {
                tfDepartmentName.setTooltip(new Tooltip(depDesc));
            } else {
                tfDepartmentName.setTooltip(null);
            }
            parentCtrl.setNewIcdData(department.getCaseIcds());
            parentCtrl.setNewOpsData(department.getCaseOpses());
            nsVentilation.getControl().getEditor().setText(String.valueOf(department.getDepcHmv()));
            //RSH 20170901:CPX-628  
            int currentIndex = parentCtrl.getTvDepartments().indexOf(this);
            dpStayTo.setLocalDate(Lang.toLocalDate(department.getDepcDisDate()));
            if (currentIndex <= 0) {
                dpStayFrom.setLocalDate(department.getDepcAdmDate() != null ? Lang.toLocalDate(department.getDepcAdmDate()) : parentCtrl.getParentController().getDpAdmissionDate().getLocalDate());
                tsStayFrom.getControl().setLocalTime(department.getDepcAdmDate() != null ? Lang.toLocalTime(department.getDepcAdmDate()) : parentCtrl.getParentController().getTsAdmissionDate().getControl().getLocalTime());
            } else {
                TCaseDepartment predecessor = parentCtrl.getTvDepartments().getTreeView().getRoot().getChildren().get(currentIndex - 1).getValue().getDepartment();
                dpStayTo.setLocalDate(null);
                //RSH 20170901:CPX-628  
                if (department.getDepcAdmDate() != null) {
                    dpStayFrom.setLocalDate(Lang.toLocalDate(department.getDepcAdmDate()));
                    tsStayFrom.getControl().setLocalTime(Lang.toLocalTime(department.getDepcAdmDate()));
                } else {
                    dpStayFrom.setLocalDate(Lang.toLocalDate(predecessor.getDepcDisDate()));
                    tsStayFrom.getControl().setLocalTime(Lang.toLocalTime(predecessor.getDepcDisDate()));
                }
            }

            dpStayTo.setLocalDate(Lang.toLocalDate(department.getDepcDisDate()));

            cbAdmissionMode.getControl().getSelectionModel().select(department.getDepcAdmodEn());
            newIcd();
            newOps();

        } else {
            tfDepartmentName.setText("");
            tfDepartmentName.setTooltip(null);
            nsVentilation.getControl().getEditor().setText("0");
            tableIcd.getItems().clear();
            tableOps.getItems().clear();
            nsVentilation.getControl().getEditor().setText("0");
            dpStayFrom.setLocalDate(null);//getEditor().setText("");
            dpStayTo.setLocalDate(null);
            cbAdmissionMode.getControl().getSelectionModel().clearSelection();
            newIcd();
            newOps();
        }
    }

    private void setUpListeners() {
        parentCtrl.getParentController().caseTypeProperty().addListener((ObservableValue<? extends CaseTypeEn> observable, CaseTypeEn oldValue, CaseTypeEn newValue) -> {
            cbAdmissionMode.setItems(FXCollections.observableArrayList(AdmissionModeEn.values(newValue)));
            cbAdmissionMode.getControl().getSelectionModel().selectFirst();
        });
        cbAdmissionMode.getControl().getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends AdmissionModeEn> observable, AdmissionModeEn oldValue, AdmissionModeEn newValue) -> {
            if (newValue != null) {
                if (department != null) {
                    department.setDepcAdmodEn(newValue);
                }
            }
        });
        nsVentilation.getControl().getIntegerProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (department != null) {
                department.setDepcHmv(newValue.intValue());
            }
        });

        dpStayTo.getControl().valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            if (newValue != null) {
                if (dpStayFrom.getLocalDate() != null && newValue.isBefore(dpStayFrom.getLocalDate())) {
                    dpStayTo.setLocalDate(oldValue);
                    return;
                }
                if (department != null) {
                    LocalDateTime dt = LocalDateTime.of(newValue, tsStayTo.getControl().getLocalTime());
                    department.setDepcDisDate(java.sql.Timestamp.valueOf(dt));
                }
                tableOps.refresh();
            }
        });
        tsStayTo.getControl().getTimeProperty().addListener((ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) -> {
            if (newValue != null) {
                if (dpStayTo.getLocalDate() == null) {
                    return;
                }
                if (department != null) {
                    LocalDateTime dt = LocalDateTime.of(dpStayTo.getLocalDate(), newValue);
                    department.setDepcDisDate(java.sql.Timestamp.valueOf(dt));
                }
                tableOps.refresh();
            }
        });
        dpStayFrom.getControl().valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            if (newValue != null) {
                if (dpStayTo.getLocalDate() != null && newValue.isAfter(dpStayTo.getLocalDate())) {
                    dpStayFrom.setLocalDate(oldValue);
                    return;
                }
                if (department != null) {
                    LocalDateTime dt = LocalDateTime.of(newValue, tsStayTo.getControl().getLocalTime());
                    department.setDepcAdmDate(java.sql.Timestamp.valueOf(dt));
                }
                tableOps.refresh();
            }
        });
        tsStayFrom.getControl().getTimeProperty().addListener((ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) -> {
            if (newValue != null) {
                if (dpStayFrom.getLocalDate() == null) {
                    return;
                }
                if (department != null) {
                    LocalDateTime dt = LocalDateTime.of(dpStayFrom.getLocalDate(), newValue);
                    department.setDepcAdmDate(java.sql.Timestamp.valueOf(dt));
                }
                tableOps.refresh();
            }
        });
        Tooltip buttonEditIcdOpsTooltip = new Tooltip();
        buttonEditIcdOpsTooltip.setMaxWidth(300);
        buttonEditIcdOpsTooltip.setWrapText(true);
        buttonEditIcdOpsTooltip.setText(Lang.getAddCaseEditIcdOpsDescription());
        buttonEditIcdOps.setOnMouseEntered((MouseEvent event) -> {
            buttonEditIcdOpsTooltip.show(buttonEditIcdOps, event.getScreenX() + 8, event.getScreenY() + 8);
            ValidationResult validRes = parentCtrl.getParentController().getValidationSupport().getValidationResult();
            LOG.info("Number of Errors: " + validRes.getErrors().size());
            for (ValidationMessage msg : validRes.getErrors()) {
                LOG.info(msg.getText());
    }
        });
        buttonEditIcdOps.setOnMouseExited((MouseEvent event) -> {
            buttonEditIcdOpsTooltip.hide();
        });
        buttonEditIcdOps.setOnAction((ActionEvent event) -> {
            editIcdOps(event);
            parentCtrl.showDepartmentDetails(DepartmentNode.this);
//                    newIcd();
//                    newOps();
            parentCtrl.refresh();
        });
        //listen to admDate Datepicker, if this node is the first, try to set admission date as start date
        parentCtrl.getParentController().getDpAdmissionDate().getControl().valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            if (parentCtrl.getTvDepartments().indexOf(DepartmentNode.this) == 0) {
                dpStayFrom.getControl().setValue(newValue);
            }
        });
        //RSH 20170901:CPX-625 
        parentCtrl.getParentController().getTsAdmissionDate().getControl().valueProperty().addListener((ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) -> {
            if (parentCtrl.getTvDepartments().indexOf(DepartmentNode.this) == 0) {
                tsStayFrom.getControl().setLocalTime(newValue);
            }
        });
    }

    private void setUpBindingAndValidation() {
        AutoCompletionBinding<String> depNameComp = TextFields.bindAutoCompletion(tfDepartmentName.getControl(), (AutoCompletionBinding.ISuggestionRequest param) -> parentCtrl.getServiceFacade().getBestMatchesForDepartment(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY));
        depNameComp.prefWidthProperty().bind(tfDepartmentName.widthProperty());

//            tfDepartmentName.getControl().textProperty().addListener(new ChangeListener<String>() {
//                @Override
//                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        tfDepartmentName.getControl().focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) {
                if (department != null && parentCtrl.getServiceFacade().isDepartmentExisting(trimToEmpty(tfDepartmentName.getText()).split(" : ")[0])) { 
                    tfDepartmentName.setTooltip(new Tooltip(trimToEmpty(tfDepartmentName.getText())));
                    department.setDepKey301(trimToEmpty(tfDepartmentName.getText()).split(" : ")[0]);
                    tfDepartmentName.getControl().positionCaret(0);
                    parentCtrl.getTvDepartments().refresh();
                    tableIcd.refresh();
                    tableOps.refresh();
                }

                validSupport.registerValidator(tfDepartmentName.getControl(), (Control t, String u) -> {
                    ValidationResult result = new ValidationResult();
                    result.addErrorIf(t, Lang.getValidationErrorInvalidDepartment(), u == null || u.isEmpty() || !parentCtrl.getServiceFacade().isDepartmentExisting(u.split(":")[0]));
                    if (u != null && !u.isEmpty() && u.contains(":") && u.split(":").length == 2) {
                        result.addErrorIf(t, Lang.getValidationErrorInvalidDepartment(), !parentCtrl.getServiceFacade().isDepartmentNameExisting(u.split(":")[0], u.split(":")[1]));
                    }

                    return result;
                });
            }
        });

        Platform.runLater(() -> {
            validSupport.initInitialDecoration();
            validSupport.registerValidator(tfDepartmentName.getControl(), (Control t, String u) -> {
                ValidationResult result = new ValidationResult();
                result.addErrorIf(t, Lang.getValidationErrorInvalidDepartment(), u == null || u.isEmpty());

                return result;
            });
            validSupport.registerValidator(tableIcd, (Control t, ObservableList<TCaseIcd> u) -> {
                ValidationResult res = new ValidationResult();
                if (department != null) {

                    boolean hasHdbFl = false;
                    for (TCaseIcd icd : department.getCaseIcds()) {
                        if (icd.getIcdcIsHdbFl()) {
                            hasHdbFl = true;
                            break;
                        }
                    }
                    res.addErrorIf(t, Lang.getDepartmentValidationIcdCountError(), department.getCaseIcds().isEmpty() && !department.isPseudo());
                    res.addErrorIf(t, Lang.getDepartmentMainDiagnosisError(), !hasHdbFl && !department.isPseudo());

                    //Awi-20170711:
                    //prevent showing warning if error is set, otherwise warning will be shown and not error
                    if (hasHdbFl && !department.getCaseIcds().isEmpty()) {
                        res.addWarningIf(t, Lang.getValidationWarningPseudoDepartment(), department.isPseudo() && !department.getCaseIcds().isEmpty());
                    }
                    parentCtrl.getTvDepartments().refresh();
                }
                return res;
            });
            validSupport.registerValidator(dpStayTo.getControl(), (Control t, LocalDate u) -> {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, Lang.getValidationErrorNoDischargeDayDepartment(), dpStayTo.getDate() == null);
                parentCtrl.getTvDepartments().refresh();
                return res;
            });
            validSupport.registerValidator(dpStayFrom.getControl(), (Control t, LocalDate u) -> {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, Lang.getValidationErrorNoDischargeDayDepartment(), dpStayFrom.getDate() == null);
                parentCtrl.getTvDepartments().refresh();
                return res;
            });
            validSupport.registerValidator(tableOps, (Control t, ObservableList<TCaseOps> u) -> {
                ValidationResult res = new ValidationResult();
                if (department != null) {
                    boolean hasNoDate = false;
                    for (TCaseOps ops : department.getCaseOpses()) {
                        if (ops.getOpscDatum() == null) {
                            hasNoDate = true;
                            break;
                        }
                    }
                    parentCtrl.getTvDepartments().refresh();
                }
                return res;
            });
        });
    }

    private void createLayout() {
        setSpacing(12.0);
        setFillWidth(true);

        GridPane gp = new GridPane();
        gp.setHgap(12.0);
        gp.setVgap(12.0);
        HBox hFrom = new HBox(dpStayFrom, tsStayFrom);
        hFrom.setSpacing(5.0);
        HBox.setHgrow(dpStayFrom, Priority.ALWAYS);
        HBox.setHgrow(tsStayFrom, Priority.ALWAYS);
        HBox hTo = new HBox(dpStayTo, tsStayTo);
        hTo.setSpacing(5.0);
        HBox.setHgrow(dpStayTo, Priority.ALWAYS);
        HBox.setHgrow(tsStayTo, Priority.ALWAYS);
        HBox h1 = new HBox(hFrom, hTo);
        HBox.setHgrow(hFrom, Priority.ALWAYS);
        HBox.setHgrow(hTo, Priority.ALWAYS);
        h1.setSpacing(12.0);
        HBox h2 = new HBox(cbAdmissionMode, nsVentilation);
        HBox.setHgrow(cbAdmissionMode, Priority.ALWAYS);
        HBox.setHgrow(nsVentilation, Priority.ALWAYS);
        h2.setSpacing(12.0);
        gp.addColumn(0, h1);
        gp.addColumn(1, h2);

        gp.getColumnConstraints().add(new ColumnConstraints(10, 100, USE_COMPUTED_SIZE, Priority.SOMETIMES, HPos.CENTER, true));
        gp.getColumnConstraints().add(new ColumnConstraints(10, 100, USE_COMPUTED_SIZE, Priority.SOMETIMES, HPos.CENTER, true));
        HBox h3 = new HBox(buttonEditIcdOps);
        h3.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(h3, Priority.ALWAYS);
        gp.addColumn(2, h3);
        tabPane = new TabPane();
        icdTab = new Tab();
        icdTab.setText(Lang.getDiagnosis());
        VBox wrapper = new VBox(tableIcd);
        VBox.setMargin(tableIcd, new Insets(0, 0, 8, 5));
        icdTab.setContent(wrapper);
        icdTab.setClosable(false);
        tabPane.getTabs().add(icdTab);
        opsTab = new Tab();
        opsTab.setText(Lang.getOPSCode());
        opsTab.setContent(tableOps);
        opsTab.setClosable(false);
        tabPane.getTabs().add(opsTab);

        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(tabPane);

//            HBox h4 = new HBox(labelIcd, h3);
//            VBox v1 = new VBox(tabPane);
//            v1.setSpacing(5.0);
//            VBox v2 = new VBox(labelOps, tableOps);
//            v2.setSpacing(5.0);
        getChildren().addAll(tfDepartmentName, gp, mainPane);
    }

    public void newIcd() {
        List<TCaseIcd> tmpIcdList = new ArrayList<>();
        TCaseIcd caseIcd = new TCaseIcd(parentCtrl.getParentController().getCurrentUserId());
        tmpIcdList.add(caseIcd);
        Set<TCaseIcd> icdSet = new HashSet<>();
        icdSet.addAll(tableIcd.getItems());
        icdSet.addAll(tmpIcdList);
        parentCtrl.setNewIcdData(icdSet);
    }

    public void newOps() {
        List<TCaseOps> tmpOpsList = new ArrayList<>();
        TCaseOps caseOps = new TCaseOps(parentCtrl.getParentController().getCurrentUserId());
        tmpOpsList.add(caseOps);
        Set<TCaseOps> opsSet = new HashSet<>();
        opsSet.addAll(tableOps.getItems());
        opsSet.addAll(tmpOpsList);
        parentCtrl.setNewOpsData(opsSet);
    }

    private void editIcdOps(ActionEvent event) {
        LOG.log(Level.INFO, "open Easy Coder ...");
        final EasyCoderDialog catalog = parentCtrl.initEasyCoder();

        if (catalog == null) {
            return;
        }
        catalog.showAndWait().ifPresent((ButtonType t) -> {
            if (t.equals(ButtonType.OK)) {
                List<TCaseIcd> tmpIcdList = new ArrayList<>();
                List<TCaseOps> tmpOpsList = new ArrayList<>();
                for (TCaseOps caseOps : catalog.getSelectedTCaseOps()) {
                    if (caseOps.getOpscCode() != null) {
                        caseOps.setCaseDepartment(department);
                        department.getCaseOpses().add(caseOps);
                        tmpOpsList.add(caseOps);
                    }
                }
                for (TCaseIcd caseIcd : catalog.getSelectedTCaseIcd()) {
                    if (caseIcd.getIcdcCode() != null) {
                        caseIcd.setCaseDepartment(department);
                        department.getCaseIcds().add(caseIcd);
                        tmpIcdList.add(caseIcd);
                    }
                }
                Set<TCaseIcd> icdSet = new HashSet<>();
//                        icdSet.addAll(tableIcd.getItems());
                icdSet.addAll(tmpIcdList);

                parentCtrl.removeBlankIcds(icdSet);
//                        setNewIcdData(icdSet);
//                        department.getCaseIcds().clear();
//                        department.setCaseIcds(icdSet);
                Set<TCaseOps> opsSet = new HashSet<>();
//                        opsSet.addAll(tableOps.getItems());
                opsSet.addAll(tmpOpsList);
                parentCtrl.removeBlankOpses(opsSet);
//                        setNewOpsData(opsSet);
            }
        });

    }

}
