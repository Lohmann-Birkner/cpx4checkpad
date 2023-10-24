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
 *    2019  shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.details;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.service.facade.CaseServiceFacade;
import de.lb.cpx.client.app.wm.fx.process.patient_health_status_details.AcgVisualisationWeb;
import de.lb.cpx.client.app.wm.fx.process.patient_health_status_details.PatientHealthStatusVisualization;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompanyCatalog;
import de.lb.cpx.client.core.model.fx.checked_combobox.CpxComboBox;
import de.lb.cpx.client.core.model.fx.datepicker.FormatedDatePicker;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.enums.GenderEn;
import de.lb.cpx.shared.lang.Lang;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class creates and manage the patient Data
 *
 *
 * @author shahin
 */
public class CmCasePatientFXMLController extends Controller<CmCasePatientScene> {

    private CaseServiceFacade serviceFacade;

    @FXML
    private TextField tfPatientNumber;
    @FXML
    private TextField tfPatientFirstName;
    @FXML
    private TextField tfPatientLastName;
    @FXML
    private TextField tfStreet;
    @FXML
    private TextField tfZipCode;
    @FXML
    private TextField tfcity;
    @FXML
    private TextField tfIPatientInsuranceNumber;
    @FXML
    private Label lblPatientFirstName;
    @FXML
    private Label lblDateOfBirth;
    @FXML
    private Label lblStreet;
    @FXML
    private Label lblPatientLastName;
    @FXML
    private Label lblInsurance;
    @FXML
    private Label lblCity;
    @FXML
    private Label lblZipCode;
    @FXML
    private Label lblPatientnsuranceNumber;
    @FXML
    private RowConstraints gpPatient;
    @FXML
    private Label lblPatientNumber;
    private final CpxInsuranceCompanyCatalog insuranceCatalog = CpxInsuranceCompanyCatalog.instance();
    @FXML
    private FormatedDatePicker dpDateOfBirth;
    @FXML
    private TextField tftCountry;
    @FXML
    private CpxComboBox<GenderEn> cbGenderText;
    @FXML
    private Label lblGender;
    @FXML
    private Label lblCountry;
    @FXML
    private TextField tfInsurance;
    @FXML
    private Label lblInsuranceGroup;
    @FXML
    private Label lblInsuranceGroupText;
    @FXML
    private AnchorPane anchorPaneParent;
    @FXML
    private VBox labelPane;

    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setUpLanguage();
        cbGenderText.getItems().addAll(GenderEn.values());
//        genderGroup.setToggleFactory(new Callback<String, RadioButton>() {
//            @Override
//            public RadioButton call(String param) {
//                return new RadioButton(param);
//            }
//        });
//        genderGroup.setValues(GenderEn.values());
//        InsuranceGroup.setToggleFactory(new Callback<String, RadioButton>() {
//            @Override
//            public RadioButton call(String param) {
//                return new RadioButton(param);
//            }
//        });
//        InsuranceGroup.setValues(InsStatusEn.values());

    }

    @Override
    public void afterInitialisingScene() {
        super.afterInitialisingScene(); //To change body of generated methods, choose Tools | Templates.
        setUpValue(serviceFacade.getPatient());
        //reset the processing of patient data
        setUpEditable(false);
//        setUpOrignalTooltips(serviceFacade.getPatient());
//        setUpListeners();
//        setUpAutoCompletion();
    }

    /**
     * init the view with values
     *
     * @param pServiceFacade servicefacade to get access to server functions
     */
    public void init(CaseServiceFacade pServiceFacade) {
        serviceFacade = pServiceFacade;
//        listOfversions = pListOfVersions;

    }

    @Override
    public void refresh() {
        super.refresh(); //To change body of generated methods, choose Tools | Templates.

    }

    /*
    * private methodes
     */
    private void setUpLanguage() {
        lblPatientNumber.setText(Lang.getPatientNumber());
        lblPatientFirstName.setText(Lang.getPatientFirstName());
        lblPatientLastName.setText(Lang.getPatientLastName());
        lblDateOfBirth.setText(Lang.getDateOfBirth());
        lblGender.setText(Lang.getGender());
        lblStreet.setText(Lang.getAddressStreet());
        lblZipCode.setText(Lang.getAddressZipCode());
        lblCity.setText(Lang.getAddressCity());
        lblInsurance.setText(Lang.getInsurance());
        lblCountry.setText(Lang.getCountry());
        lblInsuranceGroup.setText(Lang.getInsuranceStatus());
        lblPatientnsuranceNumber.setText(Lang.getInsuranceNumber());

    }

    private void setUpValue(TPatient patient) {
        if (patient == null) {
            MainApp.showErrorMessageDialog("Patient can not be found!");
            return;
        }

        tfPatientNumber.setText(patient.getPatNumber());
        tfPatientFirstName.setText(patient.getPatFirstName() != null ? patient.getPatFirstName() : "");
        tfPatientLastName.setText(patient.getPatSecName() != null ? patient.getPatSecName() : "");
        dpDateOfBirth.setValue(patient.getPatDateOfBirth() != null ? Lang.toLocalDate(patient.getPatDateOfBirth()) : null);
        //lblGenderText.setText(patient.getPatGenderEn() != null ? patient.getPatGenderEn().toString() : "");
        cbGenderText.getSelectionModel().select(patient.getPatGenderEn());
        tfStreet.setText(patient.getPatDetailsActual().getPatdAddress() != null ? patient.getPatDetailsActual().getPatdAddress() : "");
        tfZipCode.setText(patient.getPatDetailsActual().getPatdZipcode() != null ? patient.getPatDetailsActual().getPatdZipcode() : "");
        tfcity.setText(patient.getPatDetailsActual().getPatdCity() != null ? patient.getPatDetailsActual().getPatdCity() : "");
        tftCountry.setText(patient.getPatDetailsActual().getPatdCountry() != null ? patient.getPatDetailsActual().getPatdCountry().toString() : "");
        tfInsurance.setText(serviceFacade.getCurrentCase().getInsuranceIdentifier() != null ? serviceFacade.getCurrentCase().getInsuranceIdentifier() : "");
        tfIPatientInsuranceNumber.setText(serviceFacade.getCurrentCase().getInsuranceNumberPatient() != null ? serviceFacade.getCurrentCase().getInsuranceNumberPatient() : "");
        lblInsuranceGroupText.setText(patient.getPatInsuranceActual() != null && patient.getPatInsuranceActual().getInsStatusEn() != null ? patient.getPatInsuranceActual().getInsStatusEn().toString() : "");
        if (!tfInsurance.getText().isEmpty()) {
            tfInsurance.setTooltip(new Tooltip(insuranceCatalog.getByIdent(tfInsurance.getText(), AbstractCpxCatalog.DEFAULT_COUNTRY).toString()));
        }

        if (CpxClientConfig.instance().getCommonHealthStatusVisualization()) {
//            PatientHealthStatusVisualization phsViz = new PatientHealthStatusVisualization(patient);
            AcgVisualisationWeb web = new AcgVisualisationWeb(patient.getPatNumber(), 4, 4, 0.65);
            //VBox vbox = phsViz.getFullscreenContent();
            VBox vbox = web.getContent();
//            vbox.setPrefHeight(0);
            //vbox.setStyle("-fx-background-color: yellow;");
            //vbox.setStyle("-fx-font-size: 16px;");
            //vbox.setPrefWidth(300d);
            AnchorPane.setTopAnchor(vbox, 15d);
            AnchorPane.setRightAnchor(vbox, 15d);
            AnchorPane.setBottomAnchor(vbox, 15d);
            AnchorPane.setLeftAnchor(vbox, 15d);
//            anchorPaneParent.getChildren().clear();
            vbox.prefHeightProperty().bind(labelPane.heightProperty());
            anchorPaneParent.getChildren().add(vbox);


        }
    }

//    private void setUpListeners() {
//        tfPatientNumber.focusedProperty().addListener((arg0, oldValue, newValue) -> {
//            if (!newValue && (tfPatientNumber.getText() == null ? tfPatientNumber.getTooltip().getText() != null : !tfPatientNumber.getText().equals(tfPatientNumber.getTooltip().getText()))) {
//                {
//
////                    if (serviceFacade.checkIfPatientNumberExists(tfPatientNumber.getText())) {
////                        MainApp.showErrorMessageDialog("Patientnummer" + tfPatientNumber.getText() + " ist schon vorhanden");
////                        tfPatientNumber.setText(tfPatientNumber.getTooltip().getText());
////                    } else {
//                    tfPatientNumber.setStyle("-fx-text-fill: -red01");
//                    serviceFacade.getCurrentCase().getPatient().setPatNumber(tfPatientNumber.getText());
//                    serviceFacade.savePatientEntity(serviceFacade.getCurrentCase().getPatient());
////                    }
//                }
//            }
//        });
//        tfPatientFirstName.textProperty().addListener(new ChangeListener<String>() {
//
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                tfPatientFirstName.setText(newValue);
//                if (tfPatientFirstName.getText() == null ? tfPatientFirstName.getTooltip().getText() != null : !tfPatientFirstName.getText().equals(tfPatientFirstName.getTooltip().getText())) {
//                    tfPatientFirstName.setStyle("-fx-text-fill: -red01");
//                }
//
//                serviceFacade.getCurrentCase().getPatient().setPatFirstName(newValue);
//                serviceFacade.savePatientEntity(serviceFacade.getCurrentCase().getPatient());
//            }
//        });
//        tfPatientLastName.textProperty().addListener(new ChangeListener<String>() {
//
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                tfPatientLastName.setText(newValue);
//                if (tfPatientLastName.getText() == null ? tfPatientLastName.getTooltip().getText() != null : !tfPatientLastName.getText().equals(tfPatientLastName.getTooltip().getText())) {
//                    tfPatientLastName.setStyle("-fx-text-fill: -red01");
//                } else {
//                    tfPatientLastName.setStyle("-fx-text-fill: -black01");
//                }
//                serviceFacade.getCurrentCase().getPatient().setPatSecName(newValue);
//                serviceFacade.savePatientEntity(serviceFacade.getCurrentCase().getPatient());
//            }
//        });
//        tfStreet.textProperty().addListener(new ChangeListener<String>() {
//
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                tfStreet.setText(newValue);
//                if (tfStreet.getText() == null ? tfStreet.getTooltip().getText() != null : !tfStreet.getText().equals(tfStreet.getTooltip().getText())) {
//                    tfStreet.setStyle("-fx-text-fill: -red01");
//                } else {
//                    tfStreet.setStyle("-fx-text-fill: -black01");
//                }
//                serviceFacade.getCurrentCase().getPatient().getPatDetailsActual().setPatdAddress(newValue);
//                serviceFacade.savePatientEntity(serviceFacade.getCurrentCase().getPatient());
//            }
//        });
//        tfcity.textProperty().addListener(new ChangeListener<String>() {
//
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                tfcity.setText(newValue);
//                if (tfcity.getText() == null ? tfcity.getTooltip().getText() != null : !tfcity.getText().equals(tfcity.getTooltip().getText())) {
//                    tfcity.setStyle("-fx-text-fill: -red01");
//                } else {
//                    tfcity.setStyle("-fx-text-fill: -black01");
//                }
//                serviceFacade.getCurrentCase().getPatient().getPatDetailsActual().setPatdCity(newValue);
//                serviceFacade.savePatientEntity(serviceFacade.getCurrentCase().getPatient());
//            }
//        });
//        tfZipCode.textProperty().addListener(new ChangeListener<String>() {
//
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                tfZipCode.setText(newValue);
//                if (tfZipCode.getText() == null ? tfZipCode.getTooltip().getText() != null : !tfZipCode.getText().equals(tfZipCode.getTooltip().getText())) {
//                    tfZipCode.setStyle("-fx-text-fill: -red01");
//                } else {
//                    tfZipCode.setStyle("-fx-text-fill: -black01");
//                }
//                serviceFacade.getCurrentCase().getPatient().getPatDetailsActual().setPatdZipcode(newValue);
//                serviceFacade.savePatientEntity(serviceFacade.getCurrentCase().getPatient());
//            }
//        });
//
//        tfInsurance.textProperty().addListener(new ChangeListener<String>() {
//
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                tfInsurance.setText(newValue);
//                if (tfInsurance.getText() == null ? tfInsurance.getTooltip().getText() != null : !tfInsurance.getText().equals(tfInsurance.getTooltip().getText())) {
//                    tfInsurance.setStyle("-fx-text-fill: -red01");
//                } else {
//                    tfInsurance.setStyle("-fx-text-fill: -black01");
//                }
//                serviceFacade.getCurrentCase().setInsuranceIdentifier(newValue);
//                serviceFacade.savePatientEntity(serviceFacade.getCurrentCase().getPatient());
//            }
//        });
//        tfIPatientInsuranceNumber.textProperty().addListener(new ChangeListener<String>() {
//
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                tfIPatientInsuranceNumber.setText(newValue);
//                if (tfIPatientInsuranceNumber.getText() == null ? tfIPatientInsuranceNumber.getTooltip().getText() != null : !tfIPatientInsuranceNumber.getText().equals(tfIPatientInsuranceNumber.getTooltip().getText())) {
//                    tfIPatientInsuranceNumber.setStyle("-fx-text-fill: -red01");
//                } else {
//                    tfIPatientInsuranceNumber.setStyle("-fx-text-fill: -black01");
//                }
//                serviceFacade.getCurrentCase().setInsuranceNumberPatient(newValue);
//                serviceFacade.savePatientEntity(serviceFacade.getCurrentCase().getPatient());
//            }
//        });
////        genderGroup.getControl().getToggleGroup().selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
////            @Override
////            public void changed(ObservableValue<? extends Toggle> ov,
////                    Toggle toggle, Toggle new_toggle) {
////                if (new_toggle != null) {
////                    serviceFacade.getCurrentCase().getPatient().setPatGenderEn(genderGroup.getSelected());
////                }
////                serviceFacade.savePatientEntity(serviceFacade.getCurrentCase().getPatient());
////
////            }
////        });
////        InsuranceGroup.getControl().getToggleGroup().selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
////            @Override
////            public void changed(ObservableValue<? extends Toggle> ov,
////                    Toggle toggle, Toggle new_toggle) {
////                if (new_toggle != null) {
////                    serviceFacade.getCurrentCase().getPatient().getPatInsuranceActual().setInsStatusEn(InsuranceGroup.getSelected());
////                }
////                serviceFacade.savePatientEntity(serviceFacade.getCurrentCase().getPatient());
////
////            }
////        });
//
//    }
//    private void setUpAutoCompletion() {
//        AutoCompletionBinding<String> insIdentComp = TextFields.bindAutoCompletion(tfInsurance, new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
//            @Override
//            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
//                String txt = param.getUserText();
//                Collection<String> listofmatches = insuranceCatalog.findMatchesByIdent(txt, AbstractCpxCatalog.DEFAULT_COUNTRY);
//                return listofmatches;
//            }
//        });
//        insIdentComp.prefWidthProperty().bind(tfInsurance.widthProperty());
//    }
//    private void setUpOrignalTooltips(TPatient patient) {
//        String orignal = "original Wert in Datenbank:\n";
//        tfPatientNumber.setTooltip(new Tooltip(orignal + patient.getPatNumber()));
//        tfPatientFirstName.setTooltip(new Tooltip(patient.getPatFirstName() != null ? orignal + patient.getPatFirstName() : ""));
//        tfPatientLastName.setTooltip(new Tooltip(patient.getPatSecName() != null ? orignal + patient.getPatSecName() : ""));
//        dpDateOfBirth.setTooltip(new Tooltip(patient.getPatDateOfBirth() != null ? orignal + Lang.toDate(patient.getPatDateOfBirth()) : ""));
////        genderGroup.setTooltip(new Tooltip(patient.getPatGenderEn() != null ? orignal + patient.getPatGenderEn().toString() : ""));
//        tfStreet.setTooltip(new Tooltip(patient.getPatDetailsActual().getPatdAddress() != null ? orignal + patient.getPatDetailsActual().getPatdAddress() : ""));
//        tfZipCode.setTooltip(new Tooltip(patient.getPatDetailsActual().getPatdZipcode() != null ? orignal + patient.getPatDetailsActual().getPatdZipcode() : ""));
//        tfcity.setTooltip(new Tooltip(patient.getPatDetailsActual().getPatdCity() != null ? orignal + patient.getPatDetailsActual().getPatdCity() : ""));
//        tfInsurance.setTooltip(new Tooltip(serviceFacade.getCurrentCase().getInsuranceIdentifier() != null ? orignal + insuranceCatalog.findMatchesByIdent(serviceFacade.getCurrentCase().getInsuranceIdentifier(), AbstractCpxCatalog.DEFAULT_COUNTRY).toString() : ""));
//        tfIPatientInsuranceNumber.setTooltip(new Tooltip(serviceFacade.getCurrentCase().getInsuranceNumberPatient() != null ? orignal + serviceFacade.getCurrentCase().getInsuranceNumberPatient() : ""));
////        InsuranceGroup.setTooltip(new Tooltip(patient.getPatInsuranceActual().getInsStatusEn() != null ? orignal + patient.getPatInsuranceActual().getInsStatusEn().toString() : ""));
//    }
    private void setUpEditable(boolean editable) {

        tfIPatientInsuranceNumber.setEditable(editable);
        tfInsurance.setEditable(editable);
        tfPatientFirstName.setEditable(editable);
        tfPatientLastName.setEditable(editable);
        tfPatientNumber.setEditable(editable);
        tfStreet.setEditable(editable);
        tfZipCode.setEditable(editable);
        tfcity.setEditable(editable);
        dpDateOfBirth.setEditable(editable);
        tftCountry.setEditable(editable);
        dpDateOfBirth.setDisable(!editable);
        cbGenderText.setDisable(!editable);
    }
}
