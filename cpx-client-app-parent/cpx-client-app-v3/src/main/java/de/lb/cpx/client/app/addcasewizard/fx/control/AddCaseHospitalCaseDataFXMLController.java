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

import de.lb.cpx.client.app.addcasewizard.dialog.HospitalSearchDialog;
import de.lb.cpx.client.app.service.facade.AddCaseServiceFacade;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.catalog.CpxHospital;
import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledDatePicker;
import de.lb.cpx.client.core.model.fx.labeled.LabeledIntegerTextField;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextArea;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTimeSpinner;
import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TLab;
import de.lb.cpx.model.enums.AdmissionCauseEn;
import de.lb.cpx.model.enums.AdmissionReason2En;
import de.lb.cpx.model.enums.AdmissionReasonEn;
import de.lb.cpx.model.enums.AgeEn;
import de.lb.cpx.model.enums.CaseStatusEn;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.lang.CpxLanguageInterface;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.glyphfont.FontAwesome;
import static org.controlsfx.tools.ValueExtractor.addObservableValueExtractor;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

/**
 * FXML Controller class Second step in the manual case creation, Here the user
 * should enter all case specific and department specific informations
 *
 * @author wilde , RSH 07092017 CPX-628
 */
public class AddCaseHospitalCaseDataFXMLController extends Controller<CpxScene> {
    private static final int MAX_AGE_YEAR = 124;
    private static final int MAX_AGE_DAYS = 366;
    
    private static final Logger LOG = Logger.getLogger(AddCaseHospitalCaseDataFXMLController.class.getSimpleName());
    //private final Comparator<IcdOverviewDTO> sortByCode = Comparator.comparing(IcdOverviewDTO::hasHbxFl).reversed().thenComparing(IcdOverviewDTO::getIcdCode);
    private String versionComment = "";
    public static final int STEP_INDEX = 2;
    @FXML
    private LabeledTextField tfCaseNumber;
    @FXML
    private Label labelFeeGroup;
    @FXML
    private LabeledTextField tfHospitalIdent;
    @FXML
    private LabeledDatePicker dpAdmissionDate;
    @FXML
    private LabeledComboBox<AdmissionCauseEn> cbAdmissionCause;
    @FXML
    private LabeledComboBox<AdmissionReasonEn> cbAdmissionReason12;
    @FXML
    private LabeledComboBox<AdmissionReason2En> cbAdmissionReason34;
    @FXML
    private LabeledTextField tfOriginHospital;
    @FXML
    private LabeledTextField tfDoctor;
    @FXML
    private LabeledIntegerTextField nsAge;
    @FXML
    private LabeledComboBox<AgeEn> cbAge;
    @FXML
    private LabeledIntegerTextField nsWeight;
//    @FXML
//    private Button buttonAddDepartment;
//    @FXML
//    private Button buttonRemoveDepartment;
//    @FXML
//    private LabeledTreeView<DepartmentNode> tvDepartments;

//    private final ObjectProperty<TCaseDepartment> currentDepartment = new SimpleObjectProperty<>();
    private ObjectProperty<CaseTypeEn> caseTypeProperty = new SimpleObjectProperty<>();
    private AddCaseServiceFacade serviceFacade;
    private ToggleGroup tgCaseTyp;
    @FXML
    private HBox hBoxCaseTypes;
    @FXML
    private RadioButton rbCaseTypeDrg;
    @FXML
    private RadioButton rbCaseTypePepp;
    @FXML
    private RadioButton rbCaseTypeOther;
    @FXML
    private LabeledTimeSpinner tsAdmissionDate;
    private ValidationSupport validationSupport;
    private final BooleanProperty loadedProperty = new SimpleBooleanProperty(false);
//    @FXML
//    private VBox vbDepartmentDetails;
//    @FXML
//    private Button buttonSuche;
    private AutoCompletionBinding<String> oriHospitalComp;
//    @FXML
    private Button editCommentButton;
    @FXML
    private LabeledDatePicker dpBirthDate;
//    @FXML
//    private ScrollPane spDepartments;
    @FXML
    private Tab tbCaseData;
    @FXML
    private Tab tbBillData;
    @FXML
    private TabPane tbPaneContent;
    
    private boolean anonymous = true;
    
    private BooleanProperty isBusyProperty = new SimpleBooleanProperty(false);

    public AddCaseHospitalCaseDataFXMLController() {

    }
//
//    protected LabeledTreeView<DepartmentNode> getTvDepartments() {
//        return tvDepartments;
//    }
//
    protected AddCaseServiceFacade getServiceFacade() {
        return serviceFacade;
    }

    protected ObjectProperty<CaseTypeEn> caseTypeProperty() {
        return caseTypeProperty;
    }

    protected LabeledDatePicker getDpAdmissionDate() {
        return dpAdmissionDate;
    }

    protected LabeledTimeSpinner getTsAdmissionDate() {
        return tsAdmissionDate;
    }
//
//    private final ListChangeListener<TreeItem<DepartmentNode>> treeSelectionListener = c -> {
//        if (!c.getList().isEmpty()) {
//            TreeItem<DepartmentNode> selected = c.getList().get(0);
//            showDepartmentDetails(selected.getValue());
//            selected.getValue();
//            selected.getValue();
//        }
//    };
//    private final ListChangeListener<TreeItem<DepartmentNode>> treeDisableListener = c -> {
//        if (c.getList().size() <= 1) {
//            buttonRemoveDepartment.setDisable(true);
//        } else {
//            buttonRemoveDepartment.setDisable(false);
//        }
//        tvDepartments.refresh();
//    };
    private final ChangeListener<String> hospitalIdentListener = (observable, oldValue, newValue) -> {
        CpxHospital hospital = serviceFacade.getHospitalByIdent(newValue);
        if (hospital.getId() != 0L) {
            tfHospitalIdent.setTooltip(new Tooltip(hospital.toString()));
        } else {
            tfHospitalIdent.setTooltip(null);
        }
//            //CPX-1091 RSH 20180719
//            validationSupport.registerValidator(tfCaseNumber.getControl(), new Validator<String>() {
//                @Override
//                public ValidationResult apply(Control t, String u) {
//                    ValidationResult res = new ValidationResult();
//                    res.addErrorIf(t, Lang.getValidationErrorNoCaseNumber(), u.trim().isEmpty());
//                    return res;
//                }
//            });
//            validationSupport.registerValidator(tfHospitalIdent.getControl(), new Validator<String>() {
//                @Override
//                public ValidationResult apply(Control t, String u) {
//                    ValidationResult res = new ValidationResult();
//                    res.addErrorIf(t, Lang.getValidationErrorInvalidHospitalIdent(), !serviceFacade.isHospitalExisting(u));
//                    return res;
//                }
//            });
    };
    //CPX-1091 RSH 20180719
//    private final ChangeListener<String> caseNummerListener = (observable, oldValue, newValue) -> {
//        validationSupport.registerValidator(tfCaseNumber.getControl(), (Control t, String u) -> {
//            ValidationResult res = new ValidationResult();
//            res.addErrorIf(t, Lang.getValidationErrorNoCaseNumber(), u.isEmpty());
//
////                        res.addErrorIf(tfCaseNumber.getControl(), Lang.getValidationWarningCombinationAllreadyExistsIdentCsNumber(), serviceFacade.checkIfCaseExists(tfCaseNumber.getText(), tfHospitalIdent.getText()));
////                    res.addWarningIf(tfHospitalIdent.getControl(), Lang.getValidationWarningCombinationAllreadyExistsIdentCsNumber(), serviceFacade.checkIfCaseExists(tfCaseNumber.getText().trim(), tfHospitalIdent.getText()));
//            return res;
//        });
//        validationSupport.registerValidator(tfHospitalIdent.getControl(), (Control t, String u) -> {
//            ValidationResult res = new ValidationResult();
//            res.addErrorIf(t, Lang.getValidationErrorInvalidHospitalIdent(), !serviceFacade.isHospitalExisting(u));
////                    res.addWarningIf(tfHospitalIdent.getControl(), Lang.getValidationWarningCombinationAllreadyExistsIdentCsNumber(), serviceFacade.checkIfCaseExists(tfCaseNumber.getText().trim(), tfHospitalIdent.getText()));
//
//            return res;
//        });
//    };

    private final ChangeListener<String> originHospitalListener = (observable, oldValue, newValue) -> {
        CpxHospital hospital = serviceFacade.getHospitalByIdent(newValue);
        if (hospital.getId() != 0L) {
            //CPX-938
            tfOriginHospital.setTooltip(new Tooltip(Lang.getOriginHospitalObj().getTooltip() + ":\n" + hospital.toString()));
            LOG.log(Level.INFO, "originHospital is changed ...");
        } else {
            tfOriginHospital.setTooltip(new Tooltip(Lang.getOriginHospitalObj().getTooltip()));
            LOG.log(Level.WARNING, "originHospital is null!");
        }
        validationSupport.registerValidator(cbAdmissionCause, (Control t, AdmissionCauseEn u) -> {
            ValidationResult res = new ValidationResult();
            res.addWarningIf(t, Lang.getValidationWarningNoOriginHospital(), !serviceFacade.isHospitalExisting(newValue) && (u.equals(AdmissionCauseEn.V) || u.equals(AdmissionCauseEn.K)));
            return res;
        });
    };

    private final ChangeListener<Toggle> toggleListener = (observable, oldValue, newValue) -> {
        caseTypeProperty.set((CaseTypeEn) newValue.getUserData());
        LOG.log(Level.INFO, "Case type  is changed ...");
    };

    private final ChangeListener<AgeEn> ageListener = (observable, oldValue, newValue) -> {
        if(oldValue != null && newValue != null && oldValue.equals(newValue)){
            return;
        }
        cbAge.setTooltip(new Tooltip(cbAge.getControl().getConverter().toString(newValue)));
//         if (!validationSupport.getRegisteredControls().contains(nsAge.getControl())) {
//                validationSupport.registerValidator(nsAge.getControl(), new Validator<String>() {
//                    @Override
//                    public ValidationResult apply(Control t, String u) {
//                        ValidationResult res = new ValidationResult();
//
//                        res.addErrorIf(t, Lang.getValidationErrorNoAlter() + "\n" + Lang.getValidationErrorNoAlterBirthdate(), u.equals("0") || u.isEmpty());
//                        int age = Integer.parseInt(u);
//                        res.addErrorIf(t, "Alter in Tagen darf nicht größer als 366 sein", age > 366 && cbAge.getSelectedItem() == AgeEn.AGEINDAYS);
//                        res.addErrorIf(t, "Alter in Jahren darf nicht größer als 124 sein", age > 124 && cbAge.getSelectedItem() == AgeEn.AGEINYEARS);
//
//                        return res;
//                    }
//                });
//         }
        if(nsAge.getValue() > 0
//                && (nsAge.getValue()<= MAX_AGE_DAYS && cbAge.getSelectedItem() == AgeEn.AGEINDAYS
//                 || nsAge.getValue()<= MAX_AGE_YEAR && cbAge.getSelectedItem() == AgeEn.AGEINYEARS)
                ) {
            if(nsAge.getValue()> MAX_AGE_DAYS && cbAge.getSelectedItem() == AgeEn.AGEINDAYS
                 || nsAge.getValue()> MAX_AGE_YEAR && cbAge.getSelectedItem() == AgeEn.AGEINYEARS){
                nsAge.setValue(null);
                
            }else{
                int ag = nsAge.getValue();
                nsAge.setValue(null);
                nsAge.setValue(ag);
            }
            dpBirthDate.setDate(null);
        }
        setAndComputeGebInGui();
//        if (cbAge.getSelectedItem() == AgeEn.AGEINDAYS) {
//            if (dpAdmissionDate.getLocalDate() != null) {
//
//                if (nsAge.getValue() <= 366) {
//                    dpBirthDate.setDate(null);
//                    setAndComputeGebInGui(dpAdmissionDate.getLocalDate(), 0);
//                } else {
//                    nsAge.setTooltip(null);
//                }
//                nsWeight.setDisable(false);
//            }
//        } else if (dpAdmissionDate.getLocalDate() != null) {
//            if (nsAge.getValue() < 150) {
//                setAndComputeGebInGui(dpAdmissionDate.getLocalDate(), nsAge.getValue());
//            } else {
//                nsAge.setTooltip(null);
//            }
//             nsWeight.setDisable(true);
//             nsWeight.setValue(0);
//        }
    };
    private final ChangeListener<Number> gebListener = (observable, oldValue, newValue) -> {
        //CPX-1104 negative value is not allowed
        if(oldValue == null && newValue == null || oldValue != null && newValue != null && oldValue.equals(newValue)){
            return;
        }
        if (dpAdmissionDate.getLocalDate() != null) {
            setAndComputeGebInGui();
//            if (cbAge.getSelectedItem() == AgeEn.AGEINDAYS) {
//                setAndComputeGebInGui(dpAdmissionDate.getLocalDate(), 0);
//            } else {
//                setAndComputeGebInGui(dpAdmissionDate.getLocalDate(), nsAge.getValue());
//            }
        }
    };

//    private final ChangeListener<TCaseDepartment> departmentListener = (observable, oldValue, newValue) -> {
//        if (newValue != null) {
//            for (TreeItem<DepartmentNode> item : tvDepartments.getTreeView().getRoot().getChildren()) {
//                if (item.getValue().getDepartment().equals(newValue)) {
//                    showDepartmentDetails(item.getValue());
//                }
//            }
//        } else {
////            try {
//            showDepartmentDetails(new DepartmentNode(this, null));
////            } catch (CpxIllegalArgumentException ex) {
////                LOG.log(Level.SEVERE, "Cannot show department, an error occured", ex);
////            }
//        }
//    };

    private final ChangeListener<LocalDate> admDateListener = (observable, oldValue, newValue) -> {
//            if (newValue instanceof LocalDate) {
//                LocalDate date = (LocalDate) newValue;
//                setAndComputeAgeValueInGui(date);
        if(oldValue == null && newValue == null || oldValue != null && newValue != null && oldValue.equals(newValue)){
            return;
        }
        if (newValue != null) {

//          setAndComputeGebInGui(newValue, nsAge.getValue());
            setAndComputeGebInGui();
        }
        LOG.log(Level.FINEST, "Admission Date is changed to {0}...", newValue);
//            }
    };

    private final ChangeListener<LocalTime> admTimeListener = (observable, oldValue, newValue) -> {
        if (dpAdmissionDate.getLocalDate() == null) {
            dpAdmissionDate.setLocalDate(LocalDate.now());
        }
    };

    //Leak small amount of memory, due to not removing the extraxctors 
    //switch to init in main app to keep in mind all extractors init in runtime?
//    static{
//        ValueExtractor.addObservableValueExtractor(new Predicate<Control>() {
//            @Override
//            public boolean test(Control t) {
//                return t instanceof TreeView;
//            }
//        }, c -> ((TreeView) c).editableProperty());
//        ValueExtractor.addObservableValueExtractor(new Predicate<Control>() {
//            @Override
//            public boolean test(Control t) {
//                return t instanceof TreeCell;
//            }
//        }, c -> ((TreeCell) c).editableProperty());
//        ValueExtractor.addObservableValueExtractor(new Predicate<Control>() {
//            @Override
//            public boolean test(Control t) {
//                return t instanceof Label;
//            }
//        }, c -> ((Label) c).textProperty());
//    }
    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        setUpLanguage();
        validationSupport = new ValidationSupport();
//        setUpDepartmentTreeView();
        setUpTextFields();
        setUpToogleGroup();
        setUpComboboxes();
//        setUpDepartmentCtrl();
        setUpDatePicker();
    }
    private void setUpLanguage() {
        cbAdmissionCause.setTitle(Lang.getAdmissionCause());
        dpAdmissionDate.setTitle(Lang.getAdmissionDate());
        cbAdmissionReason12.setTitle(Lang.getAdmissionReason());
        cbAdmissionReason34.setTitle(Lang.getAdmissionReason2());
        dpBirthDate.setTitle(Lang.getDateOfBirth());
        tfCaseNumber.setTitle(Lang.getCaseNumber());
        tfDoctor.setTitle(Lang.getDoctorIndent());
        tfDoctor.setTooltip(new Tooltip(Lang.getDoctorIndentObj().getTooltip()));
        tfDoctor.applyFontWeightToTitle(FontWeight.BOLD);
        tfHospitalIdent.setTitle(Lang.getHospitalIdent());
        tfHospitalIdent.applyFontWeightToTitle(FontWeight.BOLD);
        tfOriginHospital.setTitle(Lang.getOriginHospital());
        tfOriginHospital.setTooltip(new Tooltip(Lang.getOriginHospitalObj().getTooltip()));
        tfOriginHospital.applyFontWeightToTitle(FontWeight.BOLD);
        tsAdmissionDate.setTitle(Lang.getDurationTime());
        nsAge.setTitle(Lang.getAge());
        cbAge.setTitle(Lang.getDurationDeclaration());
        labelFeeGroup.setText(Lang.getFeeGroup());
        tbCaseData.setText("Falldaten");
        tbBillData.setText("Rechnungen");
         nsWeight.setTitle(Lang.getWeight());
//        tvDepartments.setTitle(Lang.getDepartments());
//        buttonAddDepartment.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.PLUS));
//        buttonAddDepartment.setText("");
//        buttonAddDepartment.setTooltip(new Tooltip(Lang.getAddCaseAddDepartment()));
//        buttonRemoveDepartment.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.MINUS));
//        buttonRemoveDepartment.setText("");
//        buttonRemoveDepartment.setTooltip(new Tooltip(Lang.getAddCaseRemoveDepartment()));
        Button buttonSuche = new Button();
        buttonSuche.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                addSucheDialog(t);
            }
        });
        buttonSuche.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.SEARCH_PLUS));
//        buttonSuche.getStyleClass().add("cpx-icon-button");
        buttonSuche.setText("");
        buttonSuche.setTooltip(new Tooltip(Lang.getAddCaseSearchHospitalOptionen()));
        tfHospitalIdent.setAdditionalButton(buttonSuche);
        editCommentButton = new Button();
        editCommentButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                editCommentHandler();
            }
        });
        editCommentButton.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.EDIT));
        editCommentButton.setTooltip(new Tooltip(Lang.getVersioncontrollVersionComment() + "\n bis " + LabeledTextArea.CASE_VERSION_SIZE + " Zeichnen"));
        tfCaseNumber.setAdditionalButton(editCommentButton);
        versionComment = "";
        tbPaneContent.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                if (newValue.getUserData() == null) {
                    loadContent(newValue);
                    return;
                }
                refreshContent(newValue);

            }
        });

    }
    private void refreshContent(Tab pTab){
        if(pTab.equals(tbCaseData)){
            AddCaseHospitalCaseDetailsScene caseDetailsScene = (AddCaseHospitalCaseDetailsScene)tbCaseData.getUserData();
            caseDetailsScene.refresh();
        }else if(pTab.equals(tbBillData)){
            AddCaseHospitalCaseBillsScene caseBillsScene = (AddCaseHospitalCaseBillsScene)tbBillData.getUserData();
            if(caseBillsScene != null){
                caseBillsScene.refresh();
            }
        }
    }
    
    public void init(boolean anonymousMode, AddCaseServiceFacade addCaseFacade) throws IOException  {
        if (!loadedProperty.get()) {
            serviceFacade = addCaseFacade;
            loadContent(tbCaseData);
            anonymous = anonymousMode;
            LOG.log(Level.FINEST, "ananymous mode:"  + String.valueOf(anonymous));
// now add bill data
            loadContent(tbBillData);
            registerValidationSupport();
        }
        loadedProperty.set(true);
    }
    
    private void loadContent(Tab pTab){
        if(pTab.equals(tbCaseData)){
            try{
                AddCaseHospitalCaseDetailsScene caseDetailsScene = new AddCaseHospitalCaseDetailsScene(serviceFacade);
                tbCaseData.setUserData(caseDetailsScene);
                CpxFXMLLoader.setAnchorsInNode(caseDetailsScene.getRoot());
                tbCaseData.setContent(caseDetailsScene.getRoot());
                caseDetailsScene.getController().setParentController(this);
                //show Dummy entry
                caseDetailsScene.getController().addDepartment();
                caseDetailsScene.getController().setDataIfPossible();
            }catch(Exception ex){
                LOG.log(Level.SEVERE, "try to load case data", ex);
            }

        }else  if(pTab.equals(tbBillData)){
            try{
                AddCaseHospitalCaseBillsScene caseBillsScene = new AddCaseHospitalCaseBillsScene(serviceFacade);
                tbBillData.setUserData(caseBillsScene);
                tbBillData.setContent(caseBillsScene.getRoot());
                caseBillsScene.getController().setParentController(this);
//                caseBillsScene.getController().addBill();
                caseBillsScene.getController().setDataIfPossible();
            }catch(Exception ex){
                 LOG.log(Level.SEVERE, "try to load bill data", ex);
            }

        }
    }

//    protected EasyCoderDialog initEasyCoder() {
//        final Date admissionDate = dpAdmissionDate.getDate();
//
//        if (admissionDate == null) {
//            AlertDialog alert = AlertDialog.createErrorDialog("Bitte geben Sie zuerst ein Aufnahmedatum an!", this.getScene().getWindow());
//            alert.initOwner(AddCaseHospitalCaseDataFXMLController.this.getScene().getOwner());
//            alert.show();
//            return null;
//        }
//        EasyCoderDialog easyCoder = new EasyCoderDialog(getScene().getWindow(), Modality.APPLICATION_MODAL, "ICD-, OPS-Bearbeitung", admissionDate);
//        easyCoder.initOwner(AddCaseHospitalCaseDataFXMLController.this.getScene().getOwner());
//        return easyCoder;
//    }
//
    public ValidationSupport getValidationSupport() {
        return validationSupport;
    }

    public Long getCurrentUserId(){
         Long userId = null;
        try {
            userId = Session.instance().getCpxUserId();

        } catch (IllegalStateException stateExc) {
            LOG.log(Level.WARNING, "Can't detect user that want to create a new Version, reason " + stateExc.getMessage(), stateExc);
        }
        return userId;

    }

//    private void addDepartment() {
//        DepartmentNode department = new DepartmentNode(this, new TCaseDepartment(getCurrentUserId()));
//        TreeItem<DepartmentNode> newItem = createAndAddNewTreeItem(department);
//        department.setUpValues();
//        currentDepartment.set(department.getDepartment());
//        tvDepartments.getTreeView().getSelectionModel().select(newItem);
//        LOG.log(Level.INFO, "Department is added..");
////        department.newIcd();
////        department.newOps();
//    }
//
//    @FXML
//    private void removeDepartment(ActionEvent event) {
//        TreeItem<DepartmentNode> selected = tvDepartments.getTreeView().getSelectionModel().getSelectedItem();
//        TreeItem<DepartmentNode> newlySelectedItem = tvDepartments.removeAndSelectPrevious(selected);
//        currentDepartment.set(newlySelectedItem.getValue().getDepartment());
//        if (serviceFacade.getCurrentCase() != null && serviceFacade.getCurrentCase().getCurrentExtern() != null) {
//            if (serviceFacade.getCurrentCase().getCurrentExtern().getCaseDepartments().contains(selected.getValue().getDepartment())) {
//                serviceFacade.getCurrentCase().getCurrentExtern().getCaseDepartments().remove(selected.getValue().getDepartment());
//            }
//        }
//        LOG.log(Level.INFO, "Department " + selected.getValue().getDepartment() + "is deleted..");
//    }
//
//    private void setUpDepartmentTreeView() {
//        TreeItem<DepartmentNode> root = new TreeItem<>();
//        tvDepartments.prefHeightProperty().bind(spDepartments.heightProperty().subtract(5));
//        tvDepartments.getTreeView().setRoot(root);
//        tvDepartments.getTreeView().setShowRoot(false);
//        tvDepartments.getTreeView().getSelectionModel().getSelectedItems().addListener(treeSelectionListener);
////        tvDepartments.getTreeView().setSkin(new TreeViewSkin<DepartmentNode>(tvDepartments.getTreeView()){
////            @Override
////            public TreeCell<DepartmentNode> createCell() {
////                TreeCell<DepartmentNode> cell = new TreeCell<DepartmentNode>();
////                cell.setPadding(new Insets(8, 0, 8, 0));
////                cell.updateTreeView(getSkinnable());
////                return cell; 
//////                return super.createCell();
////            }
////            
////        });
//        tvDepartments.getTreeView().setCellFactory((TreeView<DepartmentNode> param) -> {
//            TreeCell<DepartmentNode> cell = new TreeCell<DepartmentNode>() {
//                private final Label label = new Label();
//
//                @Override
//                protected void updateItem(DepartmentNode myObj, boolean empty) {
//                    super.updateItem(myObj, empty);
//                    if (empty || myObj == null) {
//                        setText(null);
//                        setGraphic(null);
//                        //reset label
//                        label.setText(null);
//                        return;
//                    }
//                    setGraphic(label);
//                    if (myObj.getDepartment().getDepKey301() != null) {
//                        label.setText("");
//                        label.setText(myObj.getDepartment().getDepKey301());
//
//                    } else {
//                        label.setText("");
//                        label.setText("####");
//                    }
//                    if (!validationSupport.getRegisteredControls().contains(label)) {
//                        validationSupport.registerValidator(label, (Control t, String u) -> {
//                            ValidationResult res = new ValidationResult();
//                            if (u != null) {
//                                res.addErrorIf(t, Lang.getDepartmentValidationError(), myObj.isInvalid());
//                            }
//                            return res;
//                        });
//                    }
//                }
//            };
//            //
////                //add padding to show validation icon
//            cell.setPadding(new Insets(8, 0, 8, 0));
//////                cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
//////                    @Override
//////                    public void handle(MouseEvent event) {
//////                        if (event.getClickCount() >= 1) {
//////                            if (cell.getItem() != null) {
//////                                showDepartmentDetails(cell.getItem());
//////
//////                                cell.getItem().newIcd();
//////                                cell.getItem().newOps();
//////
//////                            }
//////                        }
//////                    }
////                });
//            return cell;
//        }//
//        );
//        tvDepartments.getTreeView().getRoot().getChildren().addListener(treeDisableListener);
//    }
//
//    protected void showDepartmentDetails(DepartmentNode pDetails) {
//        vbDepartmentDetails.getChildren().clear();
//        vbDepartmentDetails.getChildren().add(pDetails);
//
//        pDetails.setUpValues();
//    }

    private void setUpTextFields() {
        AutoCompletionBinding<String> hosIdentComp = TextFields.bindAutoCompletion(tfHospitalIdent.getControl(), (AutoCompletionBinding.ISuggestionRequest param) -> serviceFacade.getBestMatchesForHospital(param.getUserText()));
        hosIdentComp.prefWidthProperty().bind(tfHospitalIdent.widthProperty());
        tfHospitalIdent.getControl().textProperty().addListener(hospitalIdentListener);
//        tfCaseNumber.getControl().textProperty().addListener(caseNummerListener);
//CPX-1103 RSH-20180807
        tfHospitalIdent.getControl().focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                validationSupport.registerValidator(tfHospitalIdent.getControl(), (Control t, String u) -> {
                    ValidationResult res = new ValidationResult();
                    res.addErrorIf(t, Lang.getValidationErrorInvalidHospitalIdent(), !serviceFacade.isHospitalExisting(u));
                    if (!trimToEmpty(tfCaseNumber.getText()).isEmpty() && serviceFacade.isHospitalExisting(trimToEmpty(tfHospitalIdent.getText()))) {
                        res.addWarningIf(tfHospitalIdent.getControl(), Lang.getValidationWarningCombinationAllreadyExistsIdentCsNumber(), serviceFacade.checkIfCaseExists(trimToEmpty(tfCaseNumber.getText()), trimToEmpty(tfHospitalIdent.getText())));
                    }

                    return res;
                });
            }
        });
        tfCaseNumber.setMaxSize(25);
        tfCaseNumber.getControl().focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                validationSupport.registerValidator(tfHospitalIdent.getControl(), (Control t, String u) -> {
                    ValidationResult res = new ValidationResult();
                    res.addErrorIf(t, Lang.getValidationErrorInvalidHospitalIdent(), !serviceFacade.isHospitalExisting(u));
                    if (!trimToEmpty(tfCaseNumber.getText()).isEmpty() && serviceFacade.isHospitalExisting(trimToEmpty(tfHospitalIdent.getText()))) {
                        res.addWarningIf(tfHospitalIdent.getControl(), Lang.getValidationWarningCombinationAllreadyExistsIdentCsNumber(), serviceFacade.checkIfCaseExists(trimToEmpty(tfCaseNumber.getText()), trimToEmpty(tfHospitalIdent.getText())));
                    }

                    return res;
                });
            }
        });

//        hosNameComp = TextFields.bindAutoCompletion(tfHospitalName.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
//            @Override
//            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
//                return serviceFacade.getBestMatchesForHospitalName(param.getUserText());
//            }
//        });
//        hosNameComp.prefWidthProperty().bind(tfHospitalName.widthProperty());
//        tfHospitalName.getControl().textProperty().addListener(hospitalNameListener);
        tfOriginHospital.getControl().focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) {
                validationSupport.registerValidator(tfOriginHospital.getControl(), (Control t, String u) -> {
                    ValidationResult res = new ValidationResult();
                    res.addErrorIf(t, "IKZ einweisendes Krankenhaus ist nicht Kooekt", !u.isEmpty() && !serviceFacade.isHospitalExisting(u));
                    return res;
                });

            }

            oriHospitalComp = TextFields.bindAutoCompletion(tfOriginHospital.getControl(), (AutoCompletionBinding.ISuggestionRequest param) -> serviceFacade.getBestMatchesForHospital(param.getUserText()));
            oriHospitalComp.prefWidthProperty().bind(tfOriginHospital.widthProperty());
        });
        tfOriginHospital.getControl().textProperty().addListener(originHospitalListener);
        tfDoctor.getControl().focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) {
                validationSupport.registerValidator(tfDoctor.getControl(), (Control t, String u) -> {
                    ValidationResult res = new ValidationResult();
                    res.addErrorIf(t, "IKZ einweisendes Arz ist nicht Kooekt", !u.isEmpty() && serviceFacade.getBestMatchesForDoctor(u).isEmpty());
                    return res;
                });

            }
        });
        AutoCompletionBinding<String> docComp = TextFields.bindAutoCompletion(tfDoctor.getControl(), (AutoCompletionBinding.ISuggestionRequest param) -> serviceFacade.getBestMatchesForDoctor(param.getUserText()));
        docComp.prefWidthProperty().bind(tfDoctor.widthProperty());
        nsAge.getControl().textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (nsAge != null && nsAge.getText() != null && nsAge.getText().length() > 3) {
                nsAge.setText(nsAge.getText().substring(0, 3));
            }
        });
    }

    private void setUpToogleGroup() {
        tgCaseTyp = new ToggleGroup();

//        if (Session.instance().getLicense().isDrgModule()) {
        rbCaseTypeDrg.setText(Lang.getCaseTypeDRG());
        rbCaseTypeDrg.setToggleGroup(tgCaseTyp);
        rbCaseTypeDrg.setUserData(CaseTypeEn.DRG);
//        }
//        if (Session.instance().getLicense().isPeppModule()) {
        rbCaseTypePepp.setText(Lang.getCaseTypePEPP());
        rbCaseTypePepp.setToggleGroup(tgCaseTyp);
        rbCaseTypePepp.setUserData(CaseTypeEn.PEPP);
//        }

        rbCaseTypeOther.setText(Lang.getCaseTypeOTHER());
        rbCaseTypeOther.setToggleGroup(tgCaseTyp);
        rbCaseTypeOther.setUserData(CaseTypeEn.OTHER);

        tgCaseTyp.selectedToggleProperty().addListener(toggleListener);
//        tgCaseTyp.selectToggle(rbCaseTypeOther);
        if (Session.instance().getLicense().isDrgModule()) {
            tgCaseTyp.selectToggle(rbCaseTypeDrg);
        } else if (Session.instance().getLicense().isPeppModule()) {
            tgCaseTyp.selectToggle(rbCaseTypePepp);
        } else {
            tgCaseTyp.selectToggle(rbCaseTypeOther);
        }

        handleDRGAndOtherToggleOptions();
        handlePEPPToggleOption();

//        if (tgCaseTyp.getSelectedToggle() != null && !tgCaseTyp.getSelectedToggle().isSelected()) {
//            tgCaseTyp.selectToggle(rbCaseTypeDrg);
//        }
        LOG.log(Level.INFO, "set up Case type ...");

    }

    private void handleDRGAndOtherToggleOptions() {
        if (!Session.instance().getLicense().isDrgModule()) {
//            tgCaseTyp.getToggles().remove(rbCaseTypeDrg);
//            hBoxCaseTypes.getChildren().remove(rbCaseTypeDrg);
            rbCaseTypeDrg.setDisable(true);
            rbCaseTypeDrg.setSelected(false);
            rbCaseTypeOther.setDisable(true);
            rbCaseTypeOther.setSelected(false);
//            tgCaseTyp.selectToggle(rbCaseTypePepp);
        }
    }

    private void handlePEPPToggleOption() {
        if (!Session.instance().getLicense().isPeppModule()) {
//            tgCaseTyp.getToggles().remove(rbCaseTypePepp);
//            hBoxCaseTypes.getChildren().remove(rbCaseTypePepp);
            rbCaseTypePepp.setDisable(true);
            rbCaseTypePepp.setSelected(false);
//            tgCaseTyp.selectToggle(rbCaseTypeDrg);
        }
    }

    private void setUpComboboxes() {
        cbAdmissionReason12.getControl().setValue(AdmissionReasonEn.ar01);
        cbAdmissionCause.getControl().setValue(AdmissionCauseEn.E);
        cbAdmissionReason34.getControl().setValue(AdmissionReason2En.ar201);

        cbAge.getControl().getSelectionModel().select(AgeEn.AGEINYEARS);
        cbAge.setItems(FXCollections.observableArrayList(AgeEn.values()));
        cbAge.setConverter(new StringConverter<AgeEn>() {
            @Override
            public String toString(AgeEn object) {
                if (object == null) {
                    return "";
                }
                return object.getLangKey();
            }

            @Override
            public AgeEn fromString(String string) {
                return AgeEn.valueOf(string);
            }
        });
        cbAge.setTooltip(new Tooltip(cbAge.getSelectedItem().getTranslation().getValue()));
        cbAge.getControl().getSelectionModel().selectedItemProperty().addListener(ageListener);

        nsAge.valueProperty().addListener(gebListener);
        dpBirthDate.getControl().valueProperty().addListener(dpBithListener);
        cbAdmissionCause.setItems(FXCollections.observableArrayList(AdmissionCauseEn.values()));
        cbAdmissionCause.setConverter(new StringConverter<AdmissionCauseEn>() {
            @Override
            public String toString(AdmissionCauseEn object) {
                if (object == null) {
                    return "";
                }
                return object.toString();
            }

            @Override
            public AdmissionCauseEn fromString(String string) {
                return AdmissionCauseEn.valueOf(string);
            }
        });

        cbAdmissionReason12.setItems(FXCollections.observableArrayList(AdmissionReasonEn.values()));
        cbAdmissionReason12.setConverter(new StringConverter<AdmissionReasonEn>() {
            @Override
            public String toString(AdmissionReasonEn object) {
                if (object == null) {
                    return "";
                }
                return object.toString();
            }

            @Override
            public AdmissionReasonEn fromString(String string) {
                return AdmissionReasonEn.valueOf(string);
            }
        });

        cbAdmissionReason34.setItems(FXCollections.observableArrayList(AdmissionReason2En.values()));
        cbAdmissionReason34.setConverter(new StringConverter<AdmissionReason2En>() {
            @Override
            public String toString(AdmissionReason2En object) {
                if (object == null) {
                    return "";
                }
                return object.toString();
            }

            @Override
            public AdmissionReason2En fromString(String string) {
                return AdmissionReason2En.valueOf(string);
            }
        });

    }

//    private void setUpDepartmentCtrl() {
//
//        currentDepartment.addListener(departmentListener);
//
//    }

    private final ChangeListener<LocalDate> dpBithListener = (observable, oldValue, newValue) -> {
        //if (newValue instanceof LocalDate) {
        if(oldValue != null && newValue != null && oldValue.equals(newValue)){
            return;
        }
        LocalDate date = newValue;
        if (date != null && dpAdmissionDate.getDate() != null) {
            if (dpBirthDate.getDate().before(dpAdmissionDate.getDate())) {
                serviceFacade.getCurrentPatient().setPatDateOfBirth(dpBirthDate.getDate());

                setAndComputeAgeValueInGui(dpAdmissionDate.getLocalDate());
            } else if(dpBirthDate.getLocalDate().equals(dpAdmissionDate.getLocalDate())){
                cbAge.getControl().getSelectionModel().select(AgeEn.AGEINDAYS);
                nsAge.getControl().setText("1");
//                nsAge.setValue(1);
            }else{
                dpBirthDate.setDate(null);
//                nsAge.setValue(0);
                nsAge.getControl().setText("0");
                
                dpBirthDate.showErrorPopOver("Ungültige Geburtsdatum. Bitte geben Sie einen gültigen Eintrag an");
            }
        }
    };

    private void setUpDatePicker() {

        dpAdmissionDate.getControl().valueProperty().addListener(admDateListener);
        tsAdmissionDate.getControl().getTimeProperty().addListener(admTimeListener);
        dpBirthDate.getControl().valueProperty().addListener(dpBithListener);


    }

    private void setAndComputeAgeValueInGui(LocalDate date) {
        Date birthDay = dpBirthDate.getDate();
        if (birthDay != null && date != null) {
            LOG.log(Level.INFO, "Compute age value (with birthDay = {0}, date = {1})...", new Object[]{birthDay, date});
            LocalDate elapsedTime = Lang.getElapsedTime(birthDay, date);
            if (elapsedTime.getYear() >= 1) {
                cbAge.getControl().setValue(AgeEn.AGEINYEARS);
                int age = elapsedTime.getYear();
                if(dpBirthDate.getLocalDate().getMonth().equals(date.getMonth()) && dpBirthDate.getLocalDate().getDayOfMonth() == date.getDayOfMonth() ){
                    age++;
                }
                nsAge.getControl().setText(String.valueOf(age));
//                nsAge.setValue(age);
            } else {
                cbAge.getControl().setValue(AgeEn.AGEINDAYS);
//                nsAge.setValue(elapsedTime.getDayOfYear() > 0 ? elapsedTime.getDayOfYear() : 1);
                nsAge.getControl().setText(elapsedTime.getDayOfYear() > 0 ?String.valueOf(elapsedTime.getDayOfYear()) : "1");
            }
        }
    }
    
    public void  setAndComputeGebInGui(){

        if (dpAdmissionDate.getLocalDate() != null) {
//            setAndComputeAgeValueInGui(dpAdmissionDate.getLocalDate());
            setAndComputeGebInGui(dpAdmissionDate.getLocalDate(), this.dpBirthDate.getLocalDate(),nsAge.getValue(), cbAge.getSelectedItem());
        }
    }
    
//
    
    private void setAndComputeGebInGui(LocalDate date, Integer pAge) {
        if (date != null && pAge != 0) {
            LOG.log(Level.INFO, "Compute Geburtsdatum value (with date = {0}, pAge = {1})...", new Object[]{date, pAge});
            int year = date.getYear() - pAge;

            final Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
//            cal.set(Calendar.MONTH, Calendar.JANUARY);
            cal.set(Calendar.DAY_OF_YEAR, 1);
//            Date geb = new Date(year - 1900, 1, 1); -> deprecated and by the way: january is 0 and not 1!
            final Date geb = cal.getTime();

             serviceFacade.getCurrentPatient().setPatDateOfBirth(geb);
            
//            Format formatter = new SimpleDateFormat("dd.mm.yyyy");
//            String s = formatter.format(geb);
            dpBirthDate.setDate(serviceFacade.getCurrentPatient().getPatDateOfBirth());
            nsAge.setTooltip(new Tooltip(Lang.getPatientNumber() + " " + serviceFacade.getCurrentPatient().getPatNumber() + "\n" + Lang.getDateOfBirth() + " " + Lang.toDate(serviceFacade.getCurrentPatient().getPatDateOfBirth())));
        }
    }
    
    private void setAndComputeGebInGui(LocalDate admDate, LocalDate birthDate, Integer pAge, AgeEn ageType) {
        if(admDate == null){
            return;
        }
         final Calendar cal = Calendar.getInstance();
        if(this.anonymous){
            //birth date always the first day of the year for ageType = 'Years' and null for ageType = 'Days'
            if(pAge == null || pAge == 0){
                if(birthDate != null){
// calculate age and set birth date to the first date of the year or null  
                    if(!birthDate.isBefore(admDate)){
                        cbAge.getControl().setValue(AgeEn.AGEINDAYS);
                        nsAge.setValue(1);
                        dpBirthDate.setDate(null);
                    }else{
 

                        long yearsBetween= ChronoUnit.YEARS.between( birthDate, admDate);
                        if(yearsBetween > 0){
                            nsAge.setValue(yearsBetween);
                            cbAge.getControl().setValue(AgeEn.AGEINYEARS);
                           setAndComputeGebInGui(admDate, (int)yearsBetween);
                        }else{
                            long daysBetween =  ChronoUnit.DAYS.between( birthDate, admDate);
                            nsAge.setValue(daysBetween);
                            cbAge.getControl().setValue(AgeEn.AGEINDAYS);
                             setAndComputeGebInGui(admDate, 0);
                             dpBirthDate.setDate(null);
                        }
                          
                    }
                }
            }else{
                // calculate birth date to 01.01.year
                if(ageType.equals(AgeEn.AGEINYEARS) && pAge <= MAX_AGE_YEAR){
                    setAndComputeGebInGui(admDate, pAge);
                }else{
                    

                    dpBirthDate.setDate(null);
                }
            }

        }else{
            // Age has to be set to the moment of the admission date from birth date. If birth date is not set, than it will be the first day of the year
            if(birthDate != null){
                // calculate age to admissionDate
                    long yearsBetween= ChronoUnit.YEARS.between( birthDate, admDate);
                    if(yearsBetween > 0){
                        nsAge.setValue(yearsBetween);
                        cbAge.getControl().setValue(AgeEn.AGEINYEARS);

                    }else{
                        long daysBetween =  ChronoUnit.DAYS.between( birthDate, admDate);
                        nsAge.setValue(daysBetween);
                        cbAge.getControl().setValue(AgeEn.AGEINDAYS);

                    }
            }else{
                if(pAge != null && pAge != 0){
// set birth Date to the first day of the year                    
               // calculate birth date to 01.01.year
                    if(ageType.equals(AgeEn.AGEINYEARS) && pAge <= MAX_AGE_YEAR){
                        setAndComputeGebInGui(admDate, pAge);
                    }else{
                        // calculate birth date
                         dpBirthDate.setDate(CpxLanguageInterface.localToDate(admDate.minusDays(pAge)));
                    }
                }
            }
        }
    }
    
    public TCase cacheCaseData() {
        LOG.log(Level.INFO, "save case data ...");
        TCase currentCase = serviceFacade.getCurrentCase();
        //if case is null, initalize new Case

        if (currentCase == null) {
            currentCase = new TCase(getCurrentUserId());
            currentCase.setCsStatusEn(CaseStatusEn.NEW);
            currentCase.setPatient(serviceFacade.getCurrentPatient());
            TCaseDetails externDetails = new TCaseDetails(getCurrentUserId());
            externDetails.setCsdGenderEn(serviceFacade.getCurrentPatient().getPatGenderEn());
            currentCase.setCurrentExtern(externDetails);
            externDetails.setHospitalCase(currentCase);
            currentCase.setCaseLabor(new HashSet<TLab>());
        }
        ((AddCaseHospitalCaseDetailsScene)tbCaseData.getUserData()).getController().cacheDepartmentData(currentCase); 
        if(tbBillData.getUserData() != null){
            ((AddCaseHospitalCaseBillsScene)tbBillData.getUserData()).getController().cacheBillData(currentCase); 
        }
        currentCase.getCurrentExtern().setComment(versionComment);
        currentCase.setCsCaseNumber(trimToEmpty(tfCaseNumber.getText()));
        currentCase.setCsHospitalIdent(trimToEmpty(tfHospitalIdent.getText()));
        currentCase.setCsDoctorIdent(trimToEmpty(tfDoctor.getText()));
        currentCase.setCsCaseTypeEn((CaseTypeEn) tgCaseTyp.getSelectedToggle().getUserData());
        currentCase.getPatient().setPatDateOfBirth(dpBirthDate.getDate());
        if (dpAdmissionDate.getLocalDate() != null) {
            currentCase.getCurrentExtern().setCsdAdmissionDate(CpxLanguageInterface.localToDate(LocalDateTime.of(dpAdmissionDate.getLocalDate(), tsAdmissionDate.getControl().getLocalTime())));//Date.valueOf(dpAdmissionDate.getValue()));
        }
        LOG.log(Level.INFO, "save  insurance identifier ...");
        //CPX-1412
        currentCase.setInsuranceNumberPatient(currentCase.getPatient().getPatNumber());
        currentCase.setInsuranceIdentifier(serviceFacade.getCurrentPatient().getPatInsuranceActual().getInsInsuranceCompany());
        LOG.log(Level.INFO, "save  admissionCause,admissionReason,age ...");
        currentCase.getCurrentExtern().setCsdAdmCauseEn(cbAdmissionCause.getSelectedItem());
        currentCase.getCurrentExtern().setCsdAdmReason12En(cbAdmissionReason12.getSelectedItem());
        currentCase.getCurrentExtern().setCsdAdmReason34En(cbAdmissionReason34.getSelectedItem());
//        currentCase.getCurrentExtern().setCsdAdmissionWeight((Integer) nsAdmissionWeight.getControl().getValue());

        //set Age, reset prev Value to Zero
        switch (cbAge.getSelectedItem()) {
            case AGEINDAYS:
                currentCase.getCurrentExtern().setCsdAgeDays(nsAge.getValue());
                currentCase.getCurrentExtern().setCsdAgeYears(0);
                break;
            case AGEINYEARS:
                currentCase.getCurrentExtern().setCsdAgeYears(nsAge.getValue());
                currentCase.getCurrentExtern().setCsdAgeDays(0);
                break;
        }
        currentCase.getCurrentExtern().setCsdAdmissionWeight(currentCase.getCurrentExtern().getCsdAgeDays() > 0?nsWeight.getValue():0);
//        if (dpAdmissionDate.getLocalDate() != null && nsAge.getValue() != null) {
//            if (cbAge.getSelectedItem() == AgeEn.AGEINDAYS) {
//                setAndComputeGebInGui(dpAdmissionDate.getLocalDate(), 0);
//            } else {
//                setAndComputeGebInGui(dpAdmissionDate.getLocalDate(), nsAge.getValue());
//            }
//        }
           setAndComputeGebInGui();
serviceFacade.setCurrentCase(currentCase);
        LOG.log(Level.INFO, "check if case " + tfCaseNumber.getText() + " exists ...");
        //CPX-827 RSH 27.03.2018
        if (serviceFacade.checkIfCaseExists(trimToEmpty(tfCaseNumber.getText()), trimToEmpty(tfHospitalIdent.getText()))) {
            LOG.log(Level.INFO, "Case" + tfCaseNumber.getText() + " exists... ");
            TCase dbCase = serviceFacade.loadExistingCase(trimToEmpty(tfCaseNumber.getText()), trimToEmpty(tfHospitalIdent.getText()));
            LOG.log(Level.INFO, "load Case " + tfCaseNumber.getText() + "...");
            if (!StringUtils.trimToEmpty(dbCase.getPatient().getPatNumber()).equalsIgnoreCase(
                    StringUtils.trimToEmpty(serviceFacade.getCurrentPatient().getPatNumber()))) {
                serviceFacade.setCurrentPatient(serviceFacade.loadPatient(dbCase.getPatient().getPatNumber()));
                LOG.log(Level.INFO, "load Patient " + dbCase.getPatient().getPatNumber() + "... ");
            }
            TCaseDetails newVersionExtern = serviceFacade.getCurrentCase().getCurrentExtern();
            newVersionExtern.setCsdVersion(dbCase.getCurrentExtern().getCsdVersion() + 1);
            newVersionExtern.setComment(versionComment);
            newVersionExtern.setCsdAdmissionDate(CpxLanguageInterface.localToDate(LocalDateTime.of(dpAdmissionDate.getLocalDate(), tsAdmissionDate.getControl().getLocalTime())));
            dbCase.setCsStatusEn(CaseStatusEn.NEW_VERS);
            newVersionExtern.setHospitalCase(dbCase);
            for (TCaseDetails det : dbCase.getCaseDetails()) {
                det.setCsdIsActualFl(false);
            }
            newVersionExtern.setCsdIsActualFl(true);
            dbCase.getCaseDetails().add(newVersionExtern);
            serviceFacade.setCurrentCase(dbCase);

        }

        return currentCase;
    }

//    private void setDataIfPossible() {
//        if (tvDepartments.getTreeView().getSelectionModel().getSelectedItem() != null) {
//            currentDepartment.setValue(tvDepartments.getTreeView().getSelectionModel().getSelectedItem().getValue().getDepartment());
//        }
//        if (!tvDepartments.getTreeView().getRoot().getChildren().isEmpty() && serviceFacade.getCurrentCase() != null) {
//            tvDepartments.getTreeView().getRoot().getChildren().clear();
//            createAndAddNewTreeItems(serviceFacade.getCurrentCase().getCurrentExtern().getCaseDepartments());
//            if (currentDepartment != null) {
//                tvDepartments.getTreeView().getSelectionModel().selectFirst();
//                currentDepartment.setValue(tvDepartments.getTreeView().getSelectionModel().getSelectedItem().getValue().getDepartment());
//            }
//        }
//        if (dpAdmissionDate.getLocalDate() != null) {
////            setAndComputeAgeValueInGui(dpAdmissionDate.getLocalDate());
//            setAndComputeGebInGui(dpAdmissionDate.getLocalDate(), nsAge.getValue());
//        }
//    }
//
//    private TreeItem<DepartmentNode> createAndAddNewTreeItem(DepartmentNode department) {
//        TreeItem<DepartmentNode> newItem = new TreeItem<>(department);
//        tvDepartments.getTreeView().getRoot().getChildren().add(newItem);
//        return newItem;
//    }
//
//    private void createAndAddNewTreeItems(Set<TCaseDepartment> departments) {
//        List<TreeItem<DepartmentNode>> items = new ArrayList<>();
//        for (TCaseDepartment department : departments) {
//            items.add(new TreeItem<>(new DepartmentNode(this, department)));
//        }
//        tvDepartments.getTreeView().getRoot().getChildren().addAll(items);
//    }
//
//    protected void setNewIcdData(Set<TCaseIcd> caseIcds) {
//
//        TreeItem<DepartmentNode> selected = tvDepartments.getTreeView().getSelectionModel().getSelectedItem();
//        if (selected != null && selected.getValue().getIcdTableView() != null) {
//            selected.getValue().getIcdTableView().setItemSet(caseIcds);
//        }
//    }
//
//    protected void removeBlankIcds(Set<TCaseIcd> caseIcds) {
//        TreeItem<DepartmentNode> selected = tvDepartments.getTreeView().getSelectionModel().getSelectedItem();
//        if (selected != null && selected.getValue().getIcdTableView() != null) {
//            selected.getValue().getIcdTableView().removeBlankRows(caseIcds);
//        }
//    }
//
//    protected void setNewOpsData(Set<TCaseOps> caseOps) {
//        TreeItem<DepartmentNode> selected = tvDepartments.getTreeView().getSelectionModel().getSelectedItem();
//        if (selected != null && selected.getValue().getOpsTableView() != null) {
//            selected.getValue().getOpsTableView().setItemSet(caseOps);
//        }
//    }
//
//    protected void removeBlankOpses(Set<TCaseOps> caseOps) {
//        TreeItem<DepartmentNode> selected = tvDepartments.getTreeView().getSelectionModel().getSelectedItem();
//        if (selected != null && selected.getValue().getOpsTableView() != null) {
//            selected.getValue().getOpsTableView().removeBlankRows(caseOps);
//        }
//    }
//
    private void registerValidationSupport() {
        addObservableValueExtractor(c -> c instanceof DatePicker, c -> ((DatePicker) c).valueProperty());
        addObservableValueExtractor(c -> c instanceof LabeledTimeSpinner, c -> ((LabeledTimeSpinner) c).accessibleHelpProperty());

        Platform.runLater(() -> {
            validationSupport.initInitialDecoration();
            validationSupport.registerValidator(tfCaseNumber.getControl(), (Control t, String u) -> {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, Lang.getValidationErrorNoCaseNumber(), u.trim().isEmpty());
//                        res.addErrorIf(tfCaseNumber.getControl(), Lang.getValidationWarningCombinationAllreadyExistsIdentCsNumber(), serviceFacade.checkIfCaseExists(tfCaseNumber.getText(), tfHospitalIdent.getText()));
//                        res.addWarningIf(tfHospitalIdent.getControl(), Lang.getValidationWarningCombinationAllreadyExistsIdentCsNumber(), serviceFacade.checkIfCaseExists(tfCaseNumber.getText().trim(), tfHospitalIdent.getText()));

                return res;
            });
            validationSupport.registerValidator(tfHospitalIdent.getControl(), (Control t, String u) -> {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, Lang.getValidationErrorInvalidHospitalIdent(), !serviceFacade.isHospitalExisting(u));
//                        res.addWarningIf(tfHospitalIdent.getControl(), Lang.getValidationWarningCombinationAllreadyExistsIdentCsNumber(), serviceFacade.checkIfCaseExists(tfCaseNumber.getText().trim(), tfHospitalIdent.getText()));

                return res;
            });
//                validationSupport.registerValidator(tfHospitalName.getControl(), new Validator<String>() {
//                    @Override
//                    public ValidationResult apply(Control t, String u) {
//                        ValidationResult res = new ValidationResult();
////                        res.addErrorIf(t, Lang.getValidationErrorInvalidHospitalIdent(), !serviceFacade.isHospitalNameExisting(u));
//                        res.addWarningIf(tfHospitalName.getControl(), Lang.getValidationWarningCombinationAllreadyExistsIdentCsNumber(), serviceFacade.checkIfCaseExists(tfCaseNumber.getText(), tfHospitalIdent.getText()));
//
//                        return res;
//                    }
//                });
//            if (!validationSupport.getRegisteredControls().contains(nsAge.getControl())) {
                validationSupport.registerValidator(nsAge.getControl(), (Control t, String u) -> {
                    ValidationResult res = new ValidationResult();
                    res.addErrorIf(nsAge.getControl(), Lang.getValidationErrorNoAlter() + "\n" + Lang.getValidationErrorNoAlterBirthdate(), u.equals("0") || u.isEmpty());
//
//                    res.addErrorIf(nsAge.getControl(), "Alter in Tagen darf nicht größer als 366", Objects.requireNonNullElse(nsAge.getValue(),0) >= 366 && cbAge.getSelectedItem() == AgeEn.AGEINDAYS);
//                    res.addErrorIf(nsAge.getControl(), "Alter in Jahren darf nicht größer als 150", Objects.requireNonNullElse(nsAge.getValue(),0) > 130 && cbAge.getSelectedItem() == AgeEn.AGEINYEARS);
                        int age = 0;
                        try{
                            age = Integer.parseInt(u);
                        }catch(NumberFormatException ex){
                            
                        }
                        res.addErrorIf(t, "Alter in Tagen darf nicht größer als " + MAX_AGE_DAYS + " sein", age > MAX_AGE_DAYS && cbAge.getSelectedItem() == AgeEn.AGEINDAYS);
                        res.addErrorIf(t, "Alter in Jahren darf nicht größer als " + MAX_AGE_YEAR +" sein", age > MAX_AGE_YEAR && cbAge.getSelectedItem() == AgeEn.AGEINYEARS);

                    return res;
                });
//            }
            validationSupport.registerValidator(cbAdmissionCause.getControl(), (Control t, AdmissionCauseEn u) -> {
                ValidationResult res = new ValidationResult();
                res.addWarningIf(t, Lang.getValidationWarningNoOriginHospital(), trimToEmpty(tfOriginHospital.getText()).isEmpty() && (u != null && (u.equals(AdmissionCauseEn.V) || u.equals(AdmissionCauseEn.K))));
                return res;
            });
            validationSupport.registerValidator(dpAdmissionDate.getControl(), Validator.createEmptyValidator(Lang.getValidationErrorNoAdmissionDayCase()));
//                validationSupport.registerValidator(nsAge.getControl(), Validator.createEmptyValidator(Lang.getValidationErrorNoAdmissionDayCase()));
        });
    }
    //RSH 22.09.2017 CPX-628

    private void addSucheDialog(ActionEvent event) {
        addSucheDialog();
    }

    /**
     * Hospital search for ident,name,addresse,city,zip code
     */
    private void addSucheDialog() {
        //AWi-20170911
        //Added owner to cpx scene to hold reference of window to enable bluring of dialogs in dialogs
        LOG.log(Level.INFO, "add hospital seach dialog ...");
        HospitalSearchDialog dialog = new HospitalSearchDialog(Lang.getAddCaseSearchHospitalOptionen(), getScene().getOwner());
        dialog.showAndWait().ifPresent((ButtonType t) -> {
            if (t.equals(ButtonType.OK)) {

                CpxHospital hos = dialog.getHopital();
                if (hos == null) {
                    LOG.log(Level.WARNING, "Selected Hospital is null!");
                }

                tfHospitalIdent.setText(hos == null ? "" : dialog.getHopital().getHosIdent());
            }
        });
    }

    /**
     *
     * @param pAufnahmedatum admissiondate
     * @param pEntlassungsdatum dischargedate
     * @return days Between admissiondate and dischargedate
     */
    private int daysBetween(final Date pAufnahmedatum, final Date pEntlassungsdatum) {
        if (pAufnahmedatum == null) {
            return -1;
        }
        if (pEntlassungsdatum == null) {
            return -1;
        }
//        Calendar cal1 = new GregorianCalendar();
//        cal1.setTime(pAufnahmedatum);
//        cal1.set(Calendar.HOUR_OF_DAY, 0);
//        cal1.set(Calendar.MINUTE, 0);
//        Calendar cal2 = new GregorianCalendar();
//        cal2.setTime(pEntlassungsdatum);
//        cal2.set(Calendar.HOUR_OF_DAY, 0);
//        cal2.set(Calendar.MINUTE, 0);
//        final int days = (Math.round((cal2.getTime().getTime() -
//                cal1.getTime().getTime()) /
//                (1000 * 60 * 60 * 24)) + 1);
        final float time = getTimeElapsed(pAufnahmedatum, pEntlassungsdatum);
        final int days = (Math.round(time / (1000 * 60 * 60 * 24)) + 1);
        return days;
    }
    public float getTimeElapsed(Date pDate1, Date pDate2){
        Calendar cal1 = new GregorianCalendar();
        cal1.setTime(pDate1);
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        Calendar cal2 = new GregorianCalendar();
        cal2.setTime(pDate2);
        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal2.set(Calendar.MINUTE, 0);
        //sonar
        return (float)cal2.getTime().getTime() - (float)cal1.getTime().getTime();
    }

    private void editCommentHandler() {
        VBox vBox = new VBox();

        LabeledTextArea taComment = new LabeledTextArea(Lang.getVersioncontrollVersionComment(), LabeledTextArea.CASE_VERSION_SIZE);
        taComment.setText(versionComment);
        taComment.setWrapText(true);
        taComment.setEditable(true);

        //removed delayed field to handle updates in an unified way through the application
        taComment.getControl().focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            //ignore focus gain
            if (newValue) {
                return;
            }
            //get text on focus lost
            if (versionComment.equals(taComment.getText())) {
                return;
            }
            versionComment = taComment.getText();
        });

        vBox.getChildren().addAll(taComment);
        vBox.setPadding(new Insets(12.0, 12.0, 12.0, 12.0));
        PopOver popover = new AutoFitPopOver();
        popover.setHideOnEscape(true);
        popover.setAutoHide(true);
        popover.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        popover.setDetachable(false);
        popover.setContentNode(vBox);
        popover.setOnShowing(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                tfCaseNumber.setShowCaret(false);
            }
        });
        popover.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                tfCaseNumber.setShowCaret(true);
            }
        });
        popover.show(editCommentButton);
        popover.getContentNode().setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                popover.hide(Duration.ZERO);

            }
        });
        popover.ownerWindowProperty().get().setOnCloseRequest((WindowEvent event) -> {
            popover.hide(Duration.ZERO);
        });
    }

    @Override
    public CpxScene getScene() {
        return super.getScene();
    }

    public Window getWindow() {
        CpxScene s = getScene();
        if (s == null) {
            return null;
        }
        return s.getOwner();
    }

//    private void cacheDepartmentData(TCase currentCase) {
//        //clear old results, to ensure that only newly cloned Departments are in the list
//        int hmv = 0;
////        int tob = 0;
//        for (TreeItem<DepartmentNode> treeItem : tvDepartments.getTreeView().getRoot().getChildren()) {
//            TCaseDepartment value = treeItem.getValue().getDepartment();
//            //CPX-1090 avoid NPE in getSortedDepartments
//            if (value.getDepcAdmDate() == null) {
//                value.setDepcAdmDate(dpAdmissionDate.getDate());
//            }
//            //CPX-1097 
//            for (TCaseOps op : value.getCaseOpses()) {
//                if (op.getOpscDatum() == null) {
//                    op.setOpscDatum(value.getDepcAdmDate());
//                }
//            }
//            removeBlankIcds(value.getCaseIcds());
//            removeBlankOpses(value.getCaseOpses());
//            if (!currentCase.getCurrentExtern().getCaseDepartments().contains(value)) {
//                value.setCaseDetails(currentCase.getCurrentExtern());
//                currentCase.getCurrentExtern().getCaseDepartments().add(value);
//            }
//            hmv = hmv + value.getDepcHmv();
//            //RSH 06.11.2017 CPX-628
//            // Tage in  Pseudo Department as TOB "Tage ohne Berechnung" 
//            // AGe nicht nötig - die werden beim Groupen berechnet und eingetragen
////            if (value.isPseudo()) {
////                int daysBetween = daysBetween(value.getDepcAdmDate(), value.getDepcDisDate());
////                if(currentCase.getCsCaseTypeEn().equals(CaseTypeEn.PEPP)){
////                    daysBetween-= 2; // not 100% sure, but will be corrected by grouping
////                }else{
////                    daysBetween -= 1;
////                }
////                if(daysBetween < 0){
////                    daysBetween = 0;
////                }
////                tob = daysBetween + tob;
////                LOG.log(Level.INFO, "Compute TOB ...");
////            }
//        }
////        currentCase.getCurrentExtern().setCsdLeave(tob);
//        currentCase.getCurrentExtern().setCsdHmv(hmv);
//    }
//

    public int getAge() {
        return nsAge.getValue();
    }

    public String getPatientInsurance() {
        try{
        return serviceFacade.getCurrentPatient().getPatInsuranceActual().getInsInsuranceCompany();
        }catch(NullPointerException exc){
            LOG.log(Level.INFO, "insurance company  not set");
            return "";
        }
    }

    public Boolean isBusy() {
        return isBusyProperty.get();
    }
    
    public void setIsBusy(boolean pBusy){
        LOG.log(Level.FINEST, "set Busy:" + String.valueOf(pBusy));
        isBusyProperty.set(pBusy);
    }
}
