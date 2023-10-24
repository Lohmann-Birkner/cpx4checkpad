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
import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.addcasewizard.model.table.OpsTableView;
import de.lb.cpx.client.app.service.facade.AddCaseServiceFacade;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxBaserate;
import de.lb.cpx.client.core.model.catalog.CpxBaserateCatalog;
import de.lb.cpx.client.core.model.catalog.CpxDrg;
import de.lb.cpx.client.core.model.catalog.CpxDrgCatalog;
import de.lb.cpx.client.core.model.catalog.CpxPepp;
import de.lb.cpx.client.core.model.catalog.CpxPeppCatalog;
import de.lb.cpx.client.core.model.catalog.layout.DrgCatalogLayout;
import de.lb.cpx.client.core.model.catalog.layout.PeppCatalogLayout;
import de.lb.cpx.client.core.model.fx.checked_combobox.CpxComboBoxListViewSkin;
import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledDatePicker;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTimeSpinner;
import de.lb.cpx.client.core.model.fx.textfield.IntegerTextField;
import de.lb.cpx.client.core.model.fx.textfield.NumberTextField;
import de.lb.cpx.client.core.model.fx.tooltip.DrgRevenueDayFeeLayout;
import de.lb.cpx.client.core.model.fx.tooltip.DrgRevenueLayout;
import de.lb.cpx.client.core.model.fx.tooltip.PeppRevenueLayout;
import de.lb.cpx.client.core.model.fx.tooltip.SimpleMathLayout;
import de.lb.cpx.client.core.util.ExtendedInfoHelper;
import de.lb.cpx.client.core.util.OverrunHelper;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseDrg;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCasePepp;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.enums.AdmissionModeEn;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.DischargeReason2En;
import de.lb.cpx.model.enums.DischargeReasonEn;
import de.lb.cpx.model.enums.DrgCorrTypeEn;
import de.lb.cpx.model.lang.CpxLanguageInterface;
import de.lb.cpx.service.ejb.LockService;
import de.lb.cpx.shared.lang.Lang;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Window;
import org.controlsfx.control.PopOver;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

/**
 * FXML Controller class to resolve/ finish the case creation user enters last
 * informations(discharge reasons,maindiagnosis) and has an overview over the
 * data entered in the previous steps can check the drg result with expected
 * results
 *
 * @author wilde RSH 2017.09.04 CPX-628
 */
public class AddCaseResolveCaseFXMLController extends Controller<CpxScene> {

    private static final Logger LOG = Logger.getLogger(AddCaseResolveCaseFXMLController.class.getSimpleName());

    public static final int STEP_INDEX = 3;
    @FXML
    private Label labelPatientDataTitle;
    @FXML
    private Label labelPatientNumber;
    @FXML
    private Label labelInsNumber;
    @FXML
    private Label labelPatientSecName;
    @FXML
    private Label labelCaseDataTitle;
    @FXML
    private Label labelHospital;
    @FXML
    private Label labelCaseNumber;
    @FXML
    private Label labelAdmissionDate;
//    private VBox vBoxIcd;
//    private VBox vBoxOps;
    @FXML
    private LabeledComboBox<DischargeReasonEn> cbDischargeReason;
    @FXML
    private LabeledComboBox<DischargeReason2En> cbDischargeReason2;
    @FXML
    private LabeledComboBox<AdmissionModeEn> cbAdmissionMode;
    @FXML
    private Label labelPatientSex;
    private IcdSummaryTableView tvIcd;
    @FXML
    private LabeledDatePicker dpDischargeDay;
    private TCase currentCase;
    private TPatient currentPatient;
    @FXML
    private LabeledTimeSpinner tsDischargeDay;
    @FXML
    private Label labelHoliday;
    private AddCaseServiceFacade serviceFacade;
    @FXML
    private Label labelDrgInfo;
    @FXML
    private Label labelCw;
    @FXML
    private Label labelPccl;
    @FXML
    private Label labelSurcharge;
    @FXML
    private Label labelDeduction;
    @FXML
    private Label labelRevenue;
    @FXML
    private Label labelPatientNumberData;
    @FXML
    private Label labelInsNumberData;
    @FXML
    private Label labelPatientFirstname;
    @FXML
    private Label labelPatientFirstNameData;
    @FXML
    private Label labelPatientSecNameData;
    @FXML
    private Label labelPatientSexData;
    @FXML
    private Label labelHospitalData;
    @FXML
    private Label labelCaseNumberData;
    @FXML
    private Label labelAdmissionDateData;
//    private Label labelIcdTitle;
//    private Label labelOpsTitle;
    @FXML
    private Label labelHolidayData;
    @FXML
    private Label labelHmw;
    @FXML
    private NumberTextField tfHmv;
    @FXML
    private Label labelDrgTitle;
    @FXML
    private Label labelCwData;
    @FXML
    private Label labelPcclData;
    @FXML
    private Label labelSurchargeData;
    @FXML
    private Label labelDeductionData;
//    @FXML
//    private Label labelRevenueData;
//    private Label labelSupplementaryFee;
//    private Label labelSupplementaryFeeData;
    @FXML
    private Label labelCaseResolveTitle;
    private Validator<DischargeReason2En> discharge2Validator;
    private Validator<DischargeReasonEn> dischargeValidator;
    private ValidationSupport validationSupport;
    @FXML
    private Label labelDayStay;
    @FXML
    private Label labeltfDaysoff;
    @FXML
    private Label tfDayStay;
    @FXML
    private IntegerTextField tfDaysoff;
    @FXML
    private IntegerTextField tfDaysoffNoCare;
    @FXML
    private Label labeltfDaysoffNoCareDeduct;
    @FXML
    private Label labeltfDaysoffWithCareDeduct;
    
    private final BooleanProperty loadedProperty = new SimpleBooleanProperty(false);
    @FXML
    private Label labelBaserate;

    @FXML
    private Label LabelBaserateData;
    @FXML
    private Label labelDrg;
    @FXML
    private Label labelUnkCw;
    @FXML
    private Label labelUnkCwData;
    @FXML
    private Tab icdTab;
    @FXML
    private Tab opsTab;
    @FXML
    private Label labelCaseStatus;
    @FXML
    private Label labelCaseStatusData;
    @FXML
    private HBox boxRevenue;
    
    private static final String VAL_0000 = "0,000";

    protected TCase getCurrentCase() {
        return currentCase;
    }

    /**
     * Initializes the controller class. TODO: Check if selection is valid
     *
     * @param url url
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //set localizied Values
        setUpLanguage();
        validationSupport = new ValidationSupport();

        cbAdmissionMode.setItems(FXCollections.observableArrayList(AdmissionModeEn.values()));
        cbAdmissionMode.getControl().getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends AdmissionModeEn> observable, AdmissionModeEn oldValue, AdmissionModeEn newValue) -> {
            //CPX-642 The grouper must consider the Admission Mode
            if (newValue != null) {
                currentCase.getCurrentExtern().setCsdAdmodEn(cbAdmissionMode.getSelectedItem());
                LOG.log(Level.INFO, "Admission Mode is changed..");
            }

            performGroup();
        });

        dischargeValidator = (Control t, DischargeReasonEn u) -> {
            ValidationResult res = new ValidationResult();
            res.addWarningIf(t, Lang.getValidationWarningDischargeReason12MatchNot34(), u.isInvalidForDischargeReason2(cbDischargeReason2.getSelectedItem()));
            return res;
        };
        discharge2Validator = (Control t, DischargeReason2En u) -> {
            ValidationResult res = new ValidationResult();
            res.addWarningIf(t, Lang.getValidationWarningDischargeReason34MatchNot12(), u.isInvalidForDischargeReason(cbDischargeReason.getSelectedItem()));
            return res;
        };

        cbDischargeReason.setItems(FXCollections.observableArrayList(DischargeReasonEn.values()));
        cbDischargeReason.getControl().getSelectionModel().select(DischargeReasonEn.dr01);
        cbDischargeReason.getControl().getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends DischargeReasonEn> observable, DischargeReasonEn oldValue, DischargeReasonEn newValue) -> {
            cbDischargeReason.setValue(newValue);
            validationSupport.registerValidator(cbDischargeReason2, discharge2Validator);
            currentCase.getCurrentExtern().setCsdDisReason12En(cbDischargeReason.getSelectedItem());
            LOG.log(Level.INFO, "Discharge Reason is changed..");
            performGroup();
        });

        cbDischargeReason.getControl().skinProperty().addListener((ObservableValue<? extends Skin<?>> observable, Skin<?> oldValue, Skin<?> newValue) -> {
            ((CpxComboBoxListViewSkin) newValue).getListView().maxWidthProperty().bind(cbDischargeReason.widthProperty());
            ((CpxComboBoxListViewSkin) cbDischargeReason.getControl().getSkin()).getListView().minWidthProperty().bind(cbDischargeReason.widthProperty());
            ((CpxComboBoxListViewSkin) cbDischargeReason.getControl().getSkin()).getListView().prefWidthProperty().bind(cbDischargeReason.widthProperty());
        });

        cbDischargeReason2.setItems(FXCollections.observableArrayList(DischargeReason2En.values()));
        cbDischargeReason2.getControl().getSelectionModel().select(DischargeReason2En.dr201);
        cbDischargeReason2.getControl().getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends DischargeReason2En> observable, DischargeReason2En oldValue, DischargeReason2En newValue) -> {
            cbDischargeReason2.setValue(newValue);
            validationSupport.registerValidator(cbDischargeReason, dischargeValidator);
            currentCase.getCurrentExtern().setCsdDisReason3En(cbDischargeReason2.getSelectedItem());
            LOG.log(Level.INFO, "Discharge Reason 2 is changed..");
            performGroup();
        });

        dpDischargeDay.getControl().valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            TCaseDepartment dep = serviceFacade.getLastDepartment();
            if (dep != null && newValue != null) {
                if (newValue.isBefore(Lang.toLocalDate(dep.getDepcDisDate()))) {
                    dpDischargeDay.setLocalDate(oldValue);
                    LOG.log(Level.WARNING, "Discharge Day is  invalid ..");
                    return;
                }
            }
            LOG.log(Level.INFO, "Discharge Day is changed..");
            performGroup();
        });

        tfDaysoff.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Integer value = newValue!=null?newValue.intValue():0;
                currentCase.getCurrentExtern().setCsdLosAlteration(-1 * Long.valueOf(value));
                performGroup();
            }
        });

       tfDaysoffNoCare.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Integer value = newValue!=null?newValue.intValue():0;
                currentCase.getCurrentExtern().setCsdLosMdAlteration(-1 * value);
                performGroup();
            }
        });

        tfHmv.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                currentCase.getCurrentExtern().setCsdHmv(newValue != null ? newValue.intValue() : 0);
                performGroup();
            }
        });
    }

    private void setUpLanguage() {
        labelAdmissionDate.setText(Lang.getAdmissionDate());
        labelCaseDataTitle.setText(Lang.getCase());
        labelCaseNumber.setText(Lang.getCaseNumberObj().abbreviation);
        labelCaseResolveTitle.setText(Lang.getCaseFileFinalisation());
        labelCw.setText(Lang.getCaseResolveCWEff());
        labelDeduction.setText(Lang.getDeduction());
        labelDrgTitle.setText(Lang.getAddCaseDrgTitle());
        labelDrg.setText("DRG");
        labelUnkCw.setText(Lang.getCaseResolveICDUncorrectedCW());
        labelUnkCwData.setText("0");
        labelHmw.setText(Lang.getArtificialVentilationObj().getAbbreviation());
        labelHoliday.setText(Lang.getDaysCalculated());
        labelHospital.setText(Lang.getHospital());
        labelInsNumber.setText(Lang.getInsuranceNumberObj().abbreviation);
        labelPatientDataTitle.setText(Lang.getPatient());
        labelPatientFirstname.setText(Lang.getPatientFirstName());
        labelPatientNumber.setText(Lang.getPatientNumberObj().abbreviation);
        labelPatientSecName.setText(Lang.getPatientLastName());
        labelPatientSex.setText(Lang.getGender());
        labelPccl.setText(Lang.getPCCL());
        labelRevenue.setText(Lang.getRevenue());
        labelBaserate.setText(Lang.getBaserate());
        labelSurcharge.setText(Lang.getSurcharge());

        dpDischargeDay.setTitle(Lang.getDischargeDate());
        tsDischargeDay.setTitle(Lang.getDurationTime());
        cbAdmissionMode.setTitle(Lang.getModeOfAdmission());
        cbDischargeReason.setTitle(Lang.getDischargeReason());
        cbDischargeReason2.setTitle(Lang.getDischargeReason2());
        labelDayStay.setText(Lang.getDaysStay());
        labeltfDaysoff.setText(Lang.getCasefeeDaysoff());
        labeltfDaysoffNoCareDeduct.setText(Lang.getDaysSimulNoDeductionCare());
        labeltfDaysoffWithCareDeduct.setText(Lang.getDaysSimulWithDeductionCare());
        tfHmv.setValue(0);
        tfDaysoff.setValue(0);
        tfDaysoffNoCare.setValue(0);
        tfDaysoffNoCare.setPattern(String.format("\\d*"));

    }

    public void init(boolean anonymousMode, AddCaseServiceFacade addCaseFacade) throws CpxIllegalArgumentException {
        if (!loadedProperty.get()) {
            serviceFacade = addCaseFacade;
        }
        loadedProperty.set(true);

        serviceFacade = addCaseFacade;
        //set patientData
        currentPatient = addCaseFacade.getCurrentPatient();
        if (currentPatient != null) {
            labelPatientFirstNameData.setText(currentPatient.getPatFirstName());
            labelPatientSecNameData.setText(currentPatient.getPatSecName());
            labelPatientNumberData.setText(currentPatient.getPatNumber());
            labelPatientSexData.setText(currentPatient.getPatGenderEn() != null ? currentPatient.getPatGenderEn().toString() : null);
        } else {
            LOG.warning("no Patient is currently set! Should not happend!");
        }

        //set caseData
        currentCase = addCaseFacade.getCurrentCase();
        if (currentCase == null) {
            MainApp.showErrorMessageDialog("Can not open case", getWindow());
            LOG.log(Level.SEVERE, "Can not open case ..");
            return;
        }

        if (serviceFacade.checkIfCaseExists(currentCase.getCsCaseNumber(), currentCase.getCsHospitalIdent())) {
            labelCaseStatus.setText("Version");
            labelCaseStatus.setVisible(true);
            labelCaseStatusData.setText(String.valueOf(currentCase.getCurrentExtern().getCsdVersion()));
            labelCaseStatusData.setVisible(true);
        } else {
            labelCaseStatus.setVisible(false);
            labelCaseStatusData.setVisible(false);

        }
        labelInsNumberData.setText(currentCase.getInsuranceNumberPatient());
        labelCaseNumberData.setText(currentCase.getCsCaseNumber());
        labelHospitalData.setText(currentCase.getCsHospitalIdent());
        cbAdmissionMode.setItems(FXCollections.observableArrayList(AdmissionModeEn.values(currentCase.getCsCaseTypeEn())));
        if (cbAdmissionMode.getItems().contains(currentCase.getCurrentExtern().getCsdAdmodEn())) {
            cbAdmissionMode.select(currentCase.getCurrentExtern().getCsdAdmodEn());
        } else {
            cbAdmissionMode.getControl().getSelectionModel().selectFirst();
        }
        // changed on 03.07.2017 by Pna
        cbDischargeReason.setValue(currentCase.getCurrentExtern().getCsdDisReason12En());
        cbDischargeReason2.setValue(currentCase.getCurrentExtern().getCsdDisReason3En());
        if (currentCase.getCurrentExtern() != null) {
            labelAdmissionDateData.setText(Lang.toDate(currentCase.getCurrentExtern().getCsdAdmissionDate()));
            tvIcd = new IcdSummaryTableView(this, Lang.toYear(currentCase.getCurrentExtern().getCsdAdmissionDate()));
            tvIcd.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tvIcd.setPlaceholder(new Label(Lang.getIcdPlaceholder()));
            icdTab.setText(Lang.getDiagnosis());
            icdTab.setContent(tvIcd);
            currentCase.getCurrentExtern().getCaseDepartments();
            tvIcd.getItems().clear();
            if (this.currentCase.getCurrentExtern() != null) {
                this.currentCase.getCurrentExtern().getCaseDepartments().stream().forEach((department) -> {
                    tvIcd.addIcds(department.getCaseIcds());
                    //CPX-826
                    if (department.isTreating()) {

                        cbAdmissionMode.select(currentCase.getCurrentExtern().getCsdAdmodEn());
                        for (TCaseIcd icd : department.getCaseIcds()) {
                            if (icd.getIcdcIsHdbFl()) {
                                tvIcd.resetMainDiagnosis();
                                LOG.log(Level.INFO, "HD is automatically determined ..");
                                icd.setIcdcIsHdxFl(true);
                                break;
                            }
                        }
                    } else {
                        cbAdmissionMode.getControl().getSelectionModel().selectFirst();
                    }
                });
            }
            OpsTableView tvOps = new OpsTableView(this);
            tvOps.setPlaceholder(new Label(Lang.getOpsPlaceholder()));
            tvOps.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tvOps.getItems().clear();
            tvOps.setItems(addCaseFacade.getOpsForCurrentCase());
            opsTab.setText(Lang.getOPSCode());
            opsTab.setContent(tvOps);

            //try to get last Department discharge Day and set that value as Discharge Day!
            TCaseDepartment lastDepartment = serviceFacade.getLastDepartment();
            if (lastDepartment != null) {
                LOG.log(Level.INFO, "save Discharge Date ..");
                currentCase.getCurrentExtern().setCsdDischargeDate(lastDepartment.getDepcDisDate());
                //grouping triggered after setting the discharge day until then case is not groupable
                dpDischargeDay.setLocalDate(Lang.toLocalDate(lastDepartment.getDepcDisDate()));
                //RSH 20170901:CPX-625  
                tsDischargeDay.getControl().setLocalTime(Lang.toLocalTime(lastDepartment.getDepcDisDate()));
            }
        }
        tfHmv.setValue(currentCase.getCurrentExtern() == null ? 0 : currentCase.getCurrentExtern().getCsdHmv());
        registerValidationSupport();
    }

    public ValidationSupport getValidationSupport() {
        return validationSupport;
    }

    //CPX-1107 If an error occurs, the dialog will addCase Dilaog open
    // storeCase : return false , if an error occurs
    public Boolean storeCase() throws CloneNotSupportedException {
        //persist Data
        LOG.log(Level.INFO, " store Case " + currentCase.getCsCaseNumber());
        TCaseDetails externDetails = currentCase.getCurrentExtern();
        if (dpDischargeDay.getLocalDate() != null) {
            externDetails.setCsdDischargeDate(CpxLanguageInterface.localToDate(LocalDateTime.of(dpDischargeDay.getLocalDate(), tsDischargeDay.getControl().getLocalTime())));
        } else {
            externDetails.setCsdDischargeDate(new Date());
            LOG.log(Level.SEVERE, " Discharge Date  is null!");
        }
        externDetails.setCsdHmv(tfHmv.getValue()!=null?tfHmv.getValue().intValue():0);

        externDetails.setCsdLosAlteration(0L);
        externDetails.setCsdLos(!tfDayStay.getText().trim().isEmpty() ? Long.valueOf(tfDayStay.getText().trim()) : 0L);
        // ohne Pflege muss abgezogen werden
        externDetails.setCsdLeave(!labelHolidayData.getText().trim().isEmpty() ? 
                (-1 *(tfDaysoffNoCare.getValue() == null ? 0:tfDaysoffNoCare.getValue() ).intValue() +
                        Integer.valueOf(labelHolidayData.getText().trim()).intValue())
                : 0);

        externDetails.setCsdAdmodEn(cbAdmissionMode.getSelectedItem());
        externDetails.setCsdDisReason12En(cbDischargeReason.getSelectedItem());
        externDetails.setCsdDisReason3En(cbDischargeReason2.getSelectedItem());
        currentCase.setPatient(currentPatient);
        //CPX-1106
        externDetails.setComment(getCaseComment() + externDetails.getComment());
        calculateLeave(externDetails);
        Long currentUserId = null;
        try{
            currentUserId = Session.instance().getCpxUserId();
        }catch (IllegalStateException stateExc) {
            LOG.log(Level.WARNING, "Can't detect user that want to create a new Version, reason " + stateExc.getMessage(), stateExc);
        }
        TCaseDetails localDetails = externDetails.cloneWithoutIds(currentUserId);
        localDetails.setCsdIsLocalFl(true);

        currentCase.setCurrentExtern(externDetails);
        currentCase.setCurrentLocal(localDetails);
        //CPX-827
        LOG.log(Level.INFO, "check if Case exists");
        return MainApp.execWithLockDialog((Boolean param) -> {
            if (serviceFacade.checkIfCaseExists(currentCase.getCsCaseNumber(), currentCase.getCsHospitalIdent())) {
                TCase dbCase = serviceFacade.loadCase(currentCase.getCsCaseNumber(), currentCase.getCsHospitalIdent());

                if (currentCase.getCaseDetails().size() > dbCase.getCaseDetails().size()) {
                    EjbProxy<LockService> lockServiceBean = Session.instance().getEjbConnector().connectLockServiceBean();
                    lockServiceBean.get().checkCaseLock(currentCase.getId());
                    serviceFacade.updateCurrentCase();
                    return true;
                } else { //Kein New Version angelegt
                    serviceFacade.getCurrentCase().setId(-3);
                }
            } else {
                serviceFacade.saveCurrentCase();
                return true;
            }
            return false;
        }, getWindow());
    }

    @Override
    public void performGroup() {
        LOG.info("set new Grouper Results");

        if (currentCase.getCurrentExtern().getMainDiagnosis() == null) {
            LOG.info("will not group, no main diagnosis detected");
            return;
        }
        TGroupingResults tempResults = null;
        try {
            tempResults = serviceFacade.getTempGroupingResults(currentCase, CpxClientConfig.instance().getSelectedGrouper());
        } catch (CpxIllegalArgumentException ex) {
            Logger.getLogger(AddCaseResolveCaseFXMLController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        if (tempResults != null) {

            //RSH 06.11.2017 CPX-628
            // Tage in  Pseudo Department as TOB "Tage ohne Berechnung"
            labelHolidayData.setText(String.valueOf(tempResults.getCalculatedLeave() 
                    + (tfDaysoff.getValue()!=null?tfDaysoff.getValue().intValue():0)
                    + (tfDaysoffNoCare.getValue()!=null?tfDaysoffNoCare.getValue().intValue():0))
            );

//            calculateLeave(currentCase.getCurrentExtern());

            tfDayStay.setText(String.valueOf(tempResults.getCalculatedLengthOfStay()));
            tfDayStay.setTooltip(new Tooltip("Belegtage = " + getLosDisplay(tempResults.getCalculatedLengthOfStay(), Long.valueOf(labelHolidayData.getText().trim()))));

            if (CaseTypeEn.DRG.equals(tempResults.getGrpresType())) {
                labelDrgInfo.setText(serviceFacade.getDrgCatalogDescriptionText(tempResults.getGrpresCode(), currentCase.getCurrentExtern().getCsdAdmissionDate()));
                OverrunHelper.addOverrunInfoButton(labelDrgInfo);
            }
            if (CaseTypeEn.PEPP.equals(tempResults.getGrpresType())) {
                labelDrg.setText(Lang.getPEPP());
                labelDrgInfo.setText(serviceFacade.getPeppCatalogDescriptionText(tempResults.getGrpresCode(), currentCase.getCurrentExtern().getCsdAdmissionDate()));
                OverrunHelper.addOverrunInfoButton(labelDrgInfo);
            }

            if (CaseTypeEn.DRG.equals(tempResults.getGrpresType())) {
                labelDrg.setText("DRG");
                TCaseDrg caseDrg = (TCaseDrg) tempResults;
                labelCwData.setText(Lang.toDecimal(caseDrg.getDrgcCwEffectiv(), 3));
                labelCwData.setTooltip(new Tooltip(Lang.toDecimal(caseDrg.getDrgcCwEffectiv(), 3)));
            } else {
                labelCwData.setText("0");
                labelCwData.setTooltip(new Tooltip("0"));

            }
            labelPcclData.setText(String.valueOf(tempResults.getGrpresPccl()));
            labelPcclData.setTooltip(new Tooltip(String.valueOf(tempResults.getGrpresPccl())));

            if (CaseTypeEn.DRG.equals(tempResults.getGrpresType())) {
                TCaseDrg caseDrg = (TCaseDrg) tempResults;
                double cwCorr = caseDrg.getDrgcCwCorr();
                DrgCorrTypeEn cwEnum = caseDrg.getDrgcTypeOfCorrEn();

                if (Double.doubleToRawLongBits(cwCorr) != Double.doubleToRawLongBits(0.0d) && cwEnum != null && DrgCorrTypeEn.Surcharge.equals(cwEnum)) {
                    labelSurchargeData.setText(Lang.toDecimal(cwCorr));
                    labelSurchargeData.setTooltip(new Tooltip(Lang.toDecimal(cwCorr)));
                } else {
                    labelSurchargeData.setText(VAL_0000);
                    labelSurchargeData.setTooltip(new Tooltip(VAL_0000));
                }
            } else {
                labelSurchargeData.setText(VAL_0000);
                labelSurchargeData.setTooltip(new Tooltip(VAL_0000));
            }
            if (CaseTypeEn.DRG.equals(tempResults.getGrpresType())) {
                TCaseDrg caseDrg = (TCaseDrg) tempResults;
                double cwCorr = caseDrg.getDrgcCwCorr();
                DrgCorrTypeEn cwEnum = caseDrg.getDrgcTypeOfCorrEn();

                if (Double.doubleToRawLongBits(cwCorr) != Double.doubleToRawLongBits(0.0d) && cwEnum != null && (DrgCorrTypeEn.Deduction.equals(cwEnum) || DrgCorrTypeEn.DeductionTransfer.equals(cwEnum))) {
                    labelDeductionData.setText(Lang.toDecimal(cwCorr) + Lang.getCurrencySymbol());
                    labelDeductionData.setTooltip(new Tooltip(Lang.toDecimal(cwCorr) + Lang.getCurrencySymbol()));
                } else {
                    labelDeductionData.setText(VAL_0000);
                    labelDeductionData.setTooltip(new Tooltip(VAL_0000));
                }
            } else {
                labelDeductionData.setText(VAL_0000);
                labelDeductionData.setTooltip(new Tooltip(VAL_0000));
            }
            CpxBaserateCatalog catalog = CpxBaserateCatalog.instance();
            CpxBaserate caseBr = catalog.findDrgBaserate(currentCase.getCsHospitalIdent(), currentCase.getCurrentExtern().getCsdAdmissionDate(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            CpxBaserate careBr = catalog.findCareBaserate(currentCase.getCsHospitalIdent(), currentCase.getCurrentExtern().getCsdAdmissionDate(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            
            if (caseBr.getId() != 0L) {

                LabelBaserateData.setTooltip(new Tooltip(Lang.toDecimal(caseBr.getBaseFeeValue())));
                LabelBaserateData.setText(Lang.toDecimal(caseBr.getBaseFeeValue(), 2));
                setComputedRevenueData(tempResults,caseBr,careBr);
            } else {
                MainApp.showErrorMessageDialog("Es konnte für das IKZ " + currentCase.getCsHospitalIdent() + " und das Aufnahmedatum " + Lang.toDateTime(currentCase.getCurrentExtern().getCsdAdmissionDate()) + " keine Baserate gefunden werden!", getScene().getWindow());

            }
            if (CaseTypeEn.DRG.equals(tempResults.getGrpresType())) {
                TCaseDrg caseDrg = (TCaseDrg) tempResults;
                Label labelDrgCode = new Label(caseDrg.getGrpresCode());
                labelDrgCode.setMinWidth(50);
                CpxDrg catalog1 = CpxDrgCatalog.instance().getByCode(tempResults.getGrpresCode(), "de", CpxClientConfig.instance().getSelectedGrouper() == GDRGModel.AUTOMATIC ? Lang.toYear(currentCase.getCurrentExtern().getCsdAdmissionDate()) : CpxClientConfig.instance().getSelectedGrouper().getModelYear());
                double unkCw = caseDrg.getDrgcCwCatalog();//DrgCatalogLayout.getUncCwForAdmission(catalog1, currentCase.getCurrentExtern().getCsdAdmodEn());
                LOG.log(Level.INFO, "DrgCatalog : " + catalog1.getDrgYear() + " ,Drg: " + tempResults.getGrpresCode() + " ,unk.cw" + unkCw);

                DrgCatalogLayout layout = new DrgCatalogLayout(catalog1,
                        currentCase.getCurrentExtern().getCsdAdmodEn(),
                        caseDrg);
                labelUnkCwData.setText(Lang.toDecimal(unkCw, 3));

                Pane graph = ExtendedInfoHelper.addInfoPane(labelDrgCode, layout, null);
                graph.setMinWidth(50);
                graph.setMaxWidth(60);
                labelDrgInfo.setGraphic(graph);

            } else if (CaseTypeEn.PEPP.equals(tempResults.getGrpresType())) {
                TCasePepp casePepp = (TCasePepp) tempResults;
                Label labelPepp = new Label(casePepp.getGrpresCode());
                CpxPepp catalog1 = CpxPeppCatalog.instance().getByCode(tempResults.getGrpresCode(), "de", CpxClientConfig.instance().getSelectedGrouper() == GDRGModel.AUTOMATIC ? Lang.toYear(currentCase.getCurrentExtern().getCsdAdmissionDate()) : CpxClientConfig.instance().getSelectedGrouper().getModelYear());
                PeppCatalogLayout layout = new PeppCatalogLayout(catalog1, tempResults.getGrpresGroup(), tempResults.getCalculatedLengthOfStay());
                Pane graph = ExtendedInfoHelper.addInfoPane(labelPepp, layout, null);
                graph.setMinWidth(50);
                graph.setMaxWidth(60);
                labelDrgInfo.setGraphic(graph);
            }
        } else {
            MainApp.showErrorMessageDialog(Lang.getGrouperErrorUngroupable(), getWindow());
            LOG.log(Level.SEVERE, "Grouper errorr");
        }
    }

    private Long calculateLeave(TCaseDetails pDetails) {
        //reset leave and set new values
        return pDetails.getCsdLosAlteration();
    }

    private String getLosDisplay(Long los, Long leave) {
        return los
                + " ("
                + (los + leave)
                + " - " + leave
                + ")";
    }

    private void registerValidationSupport() {
        Platform.runLater(() -> {
            validationSupport.initInitialDecoration();
            validationSupport.registerValidator(cbDischargeReason, dischargeValidator);
            validationSupport.registerValidator(cbDischargeReason2, discharge2Validator);
            validationSupport.registerValidator(dpDischargeDay, Validator.createEmptyValidator(Lang.getValidationErrorNoDischargeDayCase()));
            validationSupport.registerValidator(dpDischargeDay, (Control t, LocalDate u) -> {
                ValidationResult res = new ValidationResult();
                res.addWarningIf(t, "dis date dont match", !Lang.toLocalDate(serviceFacade.getLastDepartment().getDepcDisDate()).equals(u));
                return res;
            });
            validationSupport.registerValidator(tvIcd, (Control t, ObservableList<TCaseIcd> u) -> {
                ValidationResult res = new ValidationResult();
                boolean hasHdx = false;
                for (TCaseIcd icd : tvIcd.getItems()) {
                    if (icd.getIcdcIsHdxFl()) {
                        hasHdx = true;
                        break;
                    }
                }
//                        check for pseudo departments, pseudo departments are valid without main diagnosis
                boolean allPseudo = true;
                for (TCaseIcd icd : tvIcd.getItems()) {
                    if (!icd.getCaseDepartment().isPseudo()) {
                        allPseudo = false;
                        break;
                    }
                }
                res.addErrorIf(t, Lang.getGrouperStatusInvalidPrincipalIcd(), !hasHdx && !allPseudo);
                return res;
            });
        });

    }

    @Override
    public ValidationResult getValidationResult() {
        return validationSupport.getValidationResult();
    }

    //CPX-1106
    private String getCaseComment() {
        String user = "user nicht bekannt";
        try {
            user = Session.instance().getCpxUserName();

        } catch (IllegalStateException stateExc) {
            LOG.log(Level.WARNING, "Can't detect user that want to create a new Version, reason " + stateExc.getMessage(), stateExc);
        }
        String date = Lang.toDateTime(new Date());
        return user + " ; " + date + " ; " + Lang.getVersionParentAddCase() + " //// ";
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

    private void setComputedRevenueData(TGroupingResults pGroupResult, CpxBaserate pCaseBr, CpxBaserate pCareBr) {
        Objects.requireNonNull(pGroupResult, "Grouping Result is null");
        Objects.requireNonNull(pCaseBr, "Case Baserate is null");
        Objects.requireNonNull(pCareBr, "Care Baserate is null");
        Double rev = 0.0;
        SimpleMathLayout tip = null;
        if (CaseTypeEn.DRG.equals(pGroupResult.getGrpresType())) {
            TCaseDrg caseDrg = (TCaseDrg) pGroupResult;
            rev = caseDrg.getRevenue(pCaseBr.getBaseFeeValue(), pCareBr.getBaseFeeValue());
            if (caseDrg.isNegotiatedDayFee()) {
                tip = new DrgRevenueDayFeeLayout(rev, pCareBr.getBaseFeeValue(), caseDrg);
            } else {
                tip = new DrgRevenueLayout(rev, Objects.requireNonNullElse(pCaseBr.getBaseFeeValue(),0.0), Objects.requireNonNullElse(pCareBr.getBaseFeeValue(),0.0), caseDrg);
            }
        } else if (CaseTypeEn.PEPP.equals(pGroupResult.getGrpresType())) {
            TCasePepp casePepp = (TCasePepp) pGroupResult;
            rev = casePepp.getRevenue();
            tip = new PeppRevenueLayout(rev, casePepp.getPeppcGrades(), Objects.requireNonNullElse(casePepp.getPeppcPayClass(), 1), casePepp.getModelIdEn().getModelYear());
        }
        String revenue = Lang.toDecimal(rev, 2);
        Label lblRevenue = new Label(revenue);
        lblRevenue.setMinWidth(Label.USE_PREF_SIZE);
        lblRevenue.setMaxWidth(Label.USE_PREF_SIZE);
        if(tip !=null){
            Node node = ExtendedInfoHelper.addInfoPane(lblRevenue, tip, PopOver.ArrowLocation.TOP_RIGHT);
            boxRevenue.getChildren().setAll(labelRevenue,node);
        }else{
            boxRevenue.getChildren().setAll(labelRevenue,lblRevenue);
        }
    }

}
