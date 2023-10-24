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

import de.lb.cpx.client.app.addcasewizard.dialog.InsuranceSearchDialog;
import de.lb.cpx.client.app.service.facade.AddCaseServiceFacade;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompany;
import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledDatePicker;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.client.core.model.fx.labeled.LabeledToggleGroup;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.TInsurance;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.TPatientDetails;
import de.lb.cpx.model.enums.CountryEn;
import de.lb.cpx.model.enums.GenderEn;
import de.lb.cpx.model.enums.InsStatusEn;
import de.lb.cpx.shared.lang.Lang;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.stage.Window;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

/**
 * FXML Controller class
 *
 * @author wilde
 */
public class AddCasePatientDataFXMLController extends Controller<CpxScene> {

    private static final Logger LOG = Logger.getLogger(AddCasePatientDataFXMLController.class.getSimpleName());

    private AddCaseServiceFacade serviceFacade;
    private TPatient currentPatient;
    private CpxInsuranceCompany currentInsurance;
    private boolean anonymousMode = true;
//    private LabeledToggleGroup genderGroup;

    public static final int STEP_INDEX = 1;

    //patientData
    @FXML
    private VBox vBoxPatientData;
    @FXML
    private LabeledTextField tfPatientNumber;
    @FXML
    private GridPane gpPatientNames;
    @FXML
    private LabeledTextField tfFirstName;
    @FXML
    private LabeledTextField tfLastName;
//    private LabeledDatePicker dpBirthday;
//    private Label labelSex;
//    @FXML
//    private RadioButton rbFemaleSex;
//    @FXML
//    private RadioButton rbMaleSex;
//    @FXML
//    private RadioButton rbUnknownSex;

    //AddressData
    @FXML
    private GridPane gpPatientAddresses;
    @FXML
    private Label labelPostalAddress;
    @FXML
    private LabeledTextField tfPostalStreet;

    @FXML
    private Label labelPatientDataTitle;
    @FXML
    private Label labelResidentialAddress;
    @FXML
    private LabeledTextField tfPostalZipCode;
    @FXML
    private LabeledTextField tfPostalTown;
    @FXML
    private LabeledTextField tfResidentialStreet;
    @FXML
    private LabeledTextField tfResidentailZipcode;
    @FXML
    private LabeledTextField tfResidentailTown;

    //Insurance
    @FXML
    private VBox vBoxInsuranceData;
    @FXML
    private Label labelInsurancetitle;
//    @FXML
//    private LabeledComboBox<String> cbInsuranceTyp;
    @FXML
    private LabeledTextField tfInsuranceIdent;
    @FXML
    private Label labelInsuranceName;
//    @FXML
//    private GridPane gpAddInsurance;
    @FXML
    private Label labelInsuranceDuration;
    @FXML
    private LabeledDatePicker dpDuratioTo;
    @FXML
    private LabeledDatePicker dpDuratioFrom;
    @FXML
    private Label labelInsuranceInsuree;
    @FXML
    private LabeledTextField tfInsureeFirstName;
    @FXML
    private LabeledTextField tfInsureeLastName;
    @FXML
    private LabeledComboBox<InsStatusEn> cbInsuranceStatus;
    @FXML
    private CheckBox chkbAlternateAddress;
    @FXML
    private GridPane gpAlternateAddress;
//    private ValidationSupport validationSupport;
    @FXML
    private LabeledComboBox<CountryEn> cbResidentailCountry;
    @FXML
    private LabeledComboBox<CountryEn> cbPostalCountry;
//    private ValidationSupport validationSupport;
    private ValidationSupport validationSupport;
    private final BooleanProperty loadedProperty = new SimpleBooleanProperty(false);
//    @FXML
    private Button btnSearch;
    @FXML
    private LabeledToggleGroup<RadioButton, GenderEn> genderGroup;

    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        validationSupport = new ValidationSupport();

        btnSearch = new Button();
        tfInsuranceIdent.setAdditionalButton(btnSearch);

        AutoCompletionBinding<String> patNumberComp = TextFields.bindAutoCompletion(tfPatientNumber.getControl(), (AutoCompletionBinding.ISuggestionRequest param) -> {
            String txt = param.getUserText();
            return serviceFacade.getMatchesForPatientNumber(txt);
        });
        tfPatientNumber.setMaxSize(50);
        patNumberComp.prefWidthProperty().bind(tfPatientNumber.widthProperty());
        tfPatientNumber.getControl().textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            currentPatient = serviceFacade.loadPatient(newValue);
            LOG.log(Level.FINE, "load Patient " + newValue);
            setUpPatientData();
        });

        AutoCompletionBinding<String> insIdentComp = TextFields.bindAutoCompletion(tfInsuranceIdent.getControl(), (AutoCompletionBinding.ISuggestionRequest param) -> {
            String txt = param.getUserText();
            Collection<String> listofmatches = serviceFacade.getMatchesForIdent(txt);//, cbInsuranceTyp.getSelectedItem());
            return listofmatches;
        });
        insIdentComp.prefWidthProperty().bind(tfInsuranceIdent.widthProperty());
        tfInsuranceIdent.getControl().textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            currentInsurance = serviceFacade.loadInsurance(newValue);
            if (currentInsurance != null) {
                labelInsuranceName.setText(currentInsurance.getInscName());
                labelInsuranceName.setTooltip(getInsuranceTooltip(currentInsurance));
            } else {
                labelInsuranceName.setText("");
            }
        });
        //ende
        dpDuratioTo.getControl().valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            if (newValue != null) {
                if (dpDuratioFrom.getControl().getValue() != null && newValue.isBefore(dpDuratioFrom.getControl().getValue())) {
                    dpDuratioTo.getControl().setValue(oldValue);
                }
            }
        });
        //beginn
        dpDuratioFrom.getControl().valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            if (newValue != null) {
                if (dpDuratioTo.getControl().getValue() != null && newValue.isAfter(dpDuratioTo.getControl().getValue())) {
                    dpDuratioFrom.getControl().setValue(oldValue);
                }
            }
        });
        tfFirstName.getControl().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                updateInsureeFields();
            }
        });
        tfLastName.getControl().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                updateInsureeFields();
            }
        });
//        genderGroup = new LabeledToggleGroup();
//         genderGroup = new LabeledToggleGroup<RadioButton,GenderEn>(Lang.getGender());
        genderGroup.setToggleFactory(RadioButton::new);
//        rbMaleSex.setUserData(GenderEn.M);
//        rbFemaleSex.setUserData(GenderEn.W);
//        rbUnknownSex.setUserData(GenderEn.U);

        genderGroup.setValues(GenderEn.values());
//        genderGroup.selectFirst();
        setGender(GenderEn.U);
        setUpLanguage();

        gpAlternateAddress.visibleProperty().bind(chkbAlternateAddress.selectedProperty());

        //handle different Insurance Status
        cbInsuranceStatus.getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<InsStatusEn>() {
            @Override
            public void changed(ObservableValue<? extends InsStatusEn> observable, InsStatusEn oldValue, InsStatusEn newValue) {
                if (newValue != null) {
                    switch (newValue) {
                        case self:
                            disable();
                            break;
                        default:
                            enable();
                    }
                } else {
                    disable();
                }
                updateInsureeFields();
            }

            private void disable() {
                tfInsureeFirstName.setText("");
                tfInsureeFirstName.setDisable(true);
                tfInsureeLastName.setText("");
                tfInsureeLastName.setDisable(true);
            }

            private void enable() {
                tfInsureeFirstName.setDisable(false);
                tfInsureeLastName.setDisable(false);
            }
        });

        cbInsuranceStatus.setItems(FXCollections.observableArrayList(InsStatusEn.values()));
        cbInsuranceStatus.getControl().setValue(InsStatusEn.self);
        cbPostalCountry.setItems(FXCollections.observableArrayList(CountryEn.values()));
        cbResidentailCountry.setItems(FXCollections.observableArrayList(CountryEn.values()));

    }

    private Tooltip getInsuranceTooltip(CpxInsuranceCompany currentInsurance) {
        Tooltip tip = new Tooltip();
        if (currentInsurance != null) {
            tip.setText(currentInsurance.toString());
        }
        return tip;
    }

    /**
     * init the controller with values set anonymus mode, validation support and
     * db values
     *
     * @param pAnonymus indicates if a personized view should show or not
     * @param facade service facade to cache and fetch db data
     */
    public void init(boolean pAnonymus, AddCaseServiceFacade facade) {
        if (!loadedProperty.get()) {
            this.anonymousMode = pAnonymus;
            this.serviceFacade = facade;
            this.currentPatient = serviceFacade.getCurrentPatient();
            if (anonymousMode) {
                vBoxPatientData.getChildren().remove(gpPatientNames);
                vBoxPatientData.getChildren().remove(gpPatientAddresses);
//                vBoxInsuranceData.getChildren().remove(gpAddInsurance);
            }
//            cbInsuranceTyp.setItems(FXCollections.observableArrayList(serviceFacade.getInsuranceTypes()));
            setUpPatientData();

            registerValidationSupport();
            validationSupport.initInitialDecoration();
        }
        loadedProperty.set(true);
    }

    public ValidationSupport getValidationSupport() {
        return validationSupport;
    }

    private void setUpLanguage() {
//        labelSex.setText(Lang.getGender());
//        rbMaleSex.setText(Lang.getGenderMale());
//        rbFemaleSex.setText(Lang.getGenderFemale());
//        rbUnknownSex.setText(Lang.getGenderUnknown());
        genderGroup.setTitle(Lang.getGender());
        labelInsuranceDuration.setText(Lang.getInsuranceValidFromTo());
        labelInsuranceInsuree.setText(Lang.getInsuranceInsuree());
        labelInsuranceName.setText(Lang.getInsuranceIdent());
        labelInsurancetitle.setText(Lang.getInsurance());
        labelInsurancetitle.setText(Lang.getInsuranceData());
        labelPatientDataTitle.setText(Lang.getPatientData());
        tfPatientNumber.setTitle(Lang.getPatientNumber());
        tfPatientNumber.applyFontWeightToTitle(FontWeight.BOLD);
        tfFirstName.setTitle(Lang.getPatientFirstName());
        tfLastName.setTitle(Lang.getPatientLastName());
//        dpBirthday.setTitle(Lang.getDateOfBirth());
        tfResidentialStreet.setTitle(Lang.getAddressStreet());
        tfResidentailZipcode.setTitle(Lang.getAddressZipCode());
        tfResidentailTown.setTitle(Lang.getAddressCity());
        tfPostalStreet.setTitle(Lang.getAddressStreet());
        tfPostalZipCode.setTitle(Lang.getAddressZipCode());
        tfPostalTown.setTitle(Lang.getAddressCity());
        cbResidentailCountry.setTitle(Lang.getCountry());
        cbPostalCountry.setTitle(Lang.getCountry());
//        cbInsuranceTyp.setTitle(Lang.getInsuranceType());
        tfInsuranceIdent.setTitle(Lang.getInsuranceIdent());
        tfInsuranceIdent.applyFontWeightToTitle(FontWeight.BOLD);
        cbInsuranceStatus.setTitle(Lang.getInsuranceStatus());
        tfInsureeFirstName.setTitle(Lang.getPatientFirstName());
        tfInsureeLastName.setTitle(Lang.getPatientLastName());
        dpDuratioFrom.setTitle(Lang.getDurationFrom());
        dpDuratioTo.setTitle(Lang.getDurationTo());
        chkbAlternateAddress.setText(Lang.getAddressDiffer());
        labelPostalAddress.setText(Lang.getAddressTypePostal());
        labelResidentialAddress.setText(Lang.getAddressTypeResidential());
        btnSearch.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.SEARCH_PLUS));
//        btnSearch.getStyleClass().add("cpx-icon-button");
        btnSearch.setText("");
        btnSearch.setTooltip(new Tooltip(Lang.getAddCaseSearchInsuranceOptionen()));
        btnSearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                addInsSearchDialog(t);
            }
        });
    }

    private void setGender(GenderEn gender) {
        genderGroup.select(gender);
//        switch (gender) {
//            case M:
//                genderGroup.select(gender);
//                break;
//            case W:
//                genderGroup.select(rbFemaleSex);
//                break;
//            default:
//                genderGroup.select(rbUnknownSex);
//        }
    }

    private void enableControlls() {
        setDisableControlls(false);
    }

    private void setDisableControlls(boolean armed) {
        tfFirstName.setDisable(armed);
        tfLastName.setDisable(armed);
        // CPX-628 The date of birth from the patient should be editable
//        rbMaleSex.setDisable(armed);
//        rbFemaleSex.setDisable(armed);
//        rbUnknownSex.setDisable(armed);
        genderGroup.setDisable(armed);
        tfPostalStreet.setDisable(armed);
        tfPostalZipCode.setDisable(armed);
        tfPostalTown.setDisable(armed);
        cbPostalCountry.setDisable(armed);
        chkbAlternateAddress.setDisable(armed);
        tfResidentialStreet.setDisable(armed);
        tfResidentailZipcode.setDisable(armed);
        tfResidentailTown.setDisable(armed);
        cbResidentailCountry.setDisable(armed);
//        labelSex.setDisable(armed);
        labelResidentialAddress.setDisable(armed);
        labelPostalAddress.setDisable(armed);
    }

    private void disableControlls() {
        setDisableControlls(true);
    }

    private void setUpPatientData() {
        //detect if patient exist in db, if true disable controlls and set Values
        if (currentPatient != null && currentPatient.getId() > 0) {
            disableControlls();
            setPatientDataInGui(currentPatient);
        } else {
            enableControlls();
            removePatientDataInGui();
        }
    }

    public TPatient cachePatient() throws CpxIllegalArgumentException {
        LOG.log(Level.INFO, "cache current Patient..");
        TPatient patient;
        if (currentPatient != null) {
            patient = currentPatient;
        } else {
            patient = new TPatient();
            TPatientDetails details = new TPatientDetails();
            details.setPatdPostDiff(false);
            patient.setPatDetailsActual(details);
            details.setPatient(patient);
        }
        updatePatientData(patient);

        createAndSetInsuranceData(patient);
        serviceFacade.setCurrentPatient(patient);
        return patient;
    }
    
    private boolean isSelfInsured(){
        return InsStatusEn.self.equals(cbInsuranceStatus.getSelectedItem());
    }
    
    private void updateInsureeFields(){
        tfInsureeFirstName.setText(isSelfInsured()?tfFirstName.getText():null);
        tfInsureeLastName.setText(isSelfInsured()?tfLastName.getText():null);
    }
    
    private void updatePatientData(TPatient patient) {
        if (!anonymousMode) {
            patient.setPatFirstName(trimToEmpty(tfFirstName.getText()));
            patient.setPatSecName(trimToEmpty(tfLastName.getText()));
            patient.setPatNumber(trimToEmpty(tfPatientNumber.getText()));
            //avoid problem with Reprü
            if (patient.getPatDetailsActual() == null) {
                TPatientDetails details = new TPatientDetails();
                details.setPatdPostDiff(false);
                patient.setPatDetailsActual(details);
                details.setPatient(patient);
            }
            patient.getPatDetailsActual().setPatdZipcode(trimToEmpty(tfResidentailZipcode.getText()));
            patient.getPatDetailsActual().setPatdAddress(trimToEmpty(tfResidentialStreet.getText()));
            patient.getPatDetailsActual().setPatdCountry(cbResidentailCountry.getControl().getValue());
            patient.getPatDetailsActual().setPatdCity(trimToEmpty(tfResidentailTown.getText()));

            if (chkbAlternateAddress.isSelected()) {
                patient.getPatDetailsActual().setPatdPostDiff(true);
                patient.getPatDetailsActual().setPatdPostZipcode(trimToEmpty(tfPostalZipCode.getText()));
                patient.getPatDetailsActual().setPatdPostAddress(trimToEmpty(tfPostalStreet.getText()));
                patient.getPatDetailsActual().setPatdPostCountry(cbPostalCountry.getControl().getValue());
                patient.getPatDetailsActual().setPatdPostCity(trimToEmpty(tfPostalTown.getText()));
            }

//            if (dpBirthday.getDate() != null) {
//                patient.setPatDateOfBirth(Date.valueOf(dpBirthday.getLocalDate()));
//            }
        }
        if (genderGroup.getSelected() != null) {
            patient.setPatGenderEn(genderGroup.getSelected());
        }
    }

    private void createAndSetInsuranceData(TPatient patient) throws CpxIllegalArgumentException {

        if (currentInsurance != null) {
            LOG.log(Level.INFO, "Set insurance data..");
            TInsurance ins = new TInsurance();

            ins.setPatient(patient);
            patient.getInsurances().add(ins);
            patient.setPatInsuranceActual(ins);

            ins.setInsInsuranceCompany(currentInsurance.getInscIdent());
            ins.setInsStatusEn(cbInsuranceStatus.getControl().getValue());
//            if (cbInsuranceStatus.getControl().getValue().equals(InsStatusEn.self)) {
//                ins.setInsureeFirstName(trimToEmpty(tfFirstName.getText()));
//                ins.setInsureeLastName(trimToEmpty(tfLastName.getText()));
//            } else {
                ins.setInsureeFirstName(trimToEmpty(tfInsureeFirstName.getText()));
                ins.setInsureeLastName(trimToEmpty(tfInsureeLastName.getText()));
//            }
            if (!anonymousMode) {
                if (dpDuratioFrom.getControl().getValue() != null && dpDuratioTo.getControl().getValue() != null) {
                    ins.setInsValidFrom(Date.valueOf(dpDuratioFrom.getControl().getValue()));
                    ins.setInsValidTo(Date.valueOf(dpDuratioTo.getControl().getValue()));
                }
            }

        } else {
            //create dummy insurance ... here should be called an Error for missing insurance 
            LOG.log(Level.INFO, "No Insurance found! Create One");
//            LOG.severe("No Insurance found! Create One");
            TInsurance ins = new TInsurance();
            ins.setPatient(patient);
            patient.getInsurances().add(ins);
            patient.setPatInsuranceActual(ins);

            ins.setInsInsuranceCompany("####");
            ins.setInsStatusEn(cbInsuranceStatus.getControl().getValue());
            ins.setInsureeFirstName(trimToEmpty(tfInsureeFirstName.getText()));
            ins.setInsureeLastName(trimToEmpty(tfInsureeLastName.getText()));
            if (!anonymousMode) {
                if (dpDuratioFrom.getControl().getValue() != null && dpDuratioTo.getControl().getValue() != null) {
                    ins.setInsValidFrom(Date.valueOf(dpDuratioFrom.getControl().getValue()));
                    ins.setInsValidTo(Date.valueOf(dpDuratioTo.getControl().getValue()));
                }
            }
        }
    }

    private void removePatientDataInGui() {
        tfFirstName.setText("");
        tfLastName.setText("");
        tfResidentialStreet.setText("");
        tfResidentailTown.setText("");
        tfResidentailZipcode.setText("");
        tfPostalStreet.setText("");
        tfPostalTown.setText("");
        tfPostalZipCode.setText("");
        dpDuratioTo.setDate(null);
        dpDuratioFrom.setDate(null);
        tfInsuranceIdent.setText("");
        tfInsureeFirstName.setText("");
        tfInsureeLastName.setText("");
//    dpBirthday.setDate(null);
    }

    private void setPatientDataInGui(TPatient patient) {
        setGender(patient.getPatGenderEn());
        tfFirstName.setText(patient.getPatFirstName());
        tfLastName.setText(patient.getPatSecName());
//        if (patient.getPatDateOfBirth() != null) {
//            dpBirthday.setLocalDate(Lang.toLocalDate(patient.getPatDateOfBirth()));
//        }
        TPatientDetails details = patient.getPatDetailsActual();

        //set residentialAddress
        if (details != null) {
            tfResidentialStreet.setText(details.getPatdAddress());
            tfResidentailTown.setText(details.getPatdCity());
            tfResidentailZipcode.setText(details.getPatdZipcode());
            if (details.getPatdCountry() != null) {
                cbResidentailCountry.getControl().setValue(details.getPatdCountry());
            }

            //set postalAddress
            tfPostalStreet.setText(details.getPatdPostAddress());
            tfPostalTown.setText(details.getPatdPostCity());
            tfPostalZipCode.setText(details.getPatdPostZipcode());
            if (details.getPatdPostCountry() != null) {
                cbPostalCountry.getControl().setValue(details.getPatdPostCountry());
            }
        }
        TInsurance ins = patient.getPatInsuranceActual();

        if (ins == null) {
            LOG.log(Level.WARNING, "Patient with number '" + patient.getPatNumber() + "' (id " + patient.getId() + ") has no actual insurance!");
        }

        tfInsuranceIdent.setText(ins == null ? "" : ins.getInsInsuranceCompany());
        tfInsureeFirstName.setText(ins == null ? "" : ins.getInsureeFirstName());
        tfInsureeLastName.setText(ins == null ? "" : ins.getInsureeLastName());
        if (ins != null && ins.getInsValidTo() != null) {
            dpDuratioTo.getControl().setValue(Lang.toLocalDate(ins.getInsValidTo()));
        }
        if (ins != null && ins.getInsValidFrom() != null) {
            dpDuratioFrom.getControl().setValue(Lang.toLocalDate(ins.getInsValidFrom()));
        }
        if (ins != null && ins.getInsStatusEn() != null) {
            cbInsuranceStatus.getControl().setValue(ins.getInsStatusEn());
        }
    }

    private void registerValidationSupport() {
        Platform.runLater(() -> {
            validationSupport.registerValidator(tfPatientNumber.getControl(), Validator.createEmptyValidator(Lang.getValidationErrorNoPatientNumber()));
            validationSupport.registerValidator(tfInsuranceIdent.getControl(), (Control t, String u) -> {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, Lang.getValidationErrorInvalidInsuranceIdent(), !serviceFacade.isInsuranceExisting(u));
                return res;
            });
//                validationSupport.registerValidator(dpBirthday.getControl(), new Validator<LocalDate>() {
//                    @Override
//                    public ValidationResult apply(Control t, LocalDate u) {
//                        ValidationResult result = new ValidationResult();
//                        if (currentPatient == null) {
//                            result.addErrorIf(t, Lang.getValidationErrorNoPatientBirthday(), u == null);
//                        }
//                        return result;
//                    }
//                });
            validationSupport.registerValidator(tfLastName.getControl(), (Control t, String u) -> {
                ValidationResult result = new ValidationResult();
                if (currentPatient == null && u != null) {
                    result.addErrorIf(t, Lang.getValidationErrorNoPatientLastName(), u.isEmpty());
                }
                return result;
            });
        });
    }

    @FXML
    private void addInsSearchDialog(ActionEvent event) {
        addInsSearchDialog();
    }
    // RSH 02112017 CPX-642

    private void addInsSearchDialog() {
        //AWi-20170911
        //Added owner to cpx scene to hold reference of window to enable bluring of dialogs in dialogs
        LOG.log(Level.INFO, "add Insurance search dialod..");
        InsuranceSearchDialog dialog = new InsuranceSearchDialog(Lang.getAddCaseSearchInsuranceOptionen(), getScene().getOwner());
        dialog.showAndWait().ifPresent((ButtonType t) -> {
            if (t.equals(ButtonType.OK)) {
                CpxInsuranceCompany ins = dialog.getInsurance();
                if (ins == null) {
                    LOG.log(Level.WARNING, "Selected insurance is null!");
                }
                tfInsuranceIdent.setText(ins == null ? "" : ins.getInscIdent());
            }
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

}
