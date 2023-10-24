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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.job.fx.casemerging.details;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.cm.fx.simulation.tables.CaseDetailsScrollPane;
import de.lb.cpx.client.app.job.fx.casemerging.model.CaseMergeContent;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.catalog.CpxHospital;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompany;
import de.lb.cpx.client.core.model.fx.comparablepane.ComparablePaneSkin;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.math.utility.PatientHelper;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class to handle case data in merge scene
 *
 * @author wilde
 */
public class CaseDetailMergeDetailsFXMLController extends Controller<CaseDetailMergeDetailsScreen> {

    @FXML
    private CaseDetailsScrollPane<CaseMergeContent> gpCaseDetails;
    @FXML
    private ScrollPane spGeneralData;
    @FXML
    private HBox hBoxGeneralData;
    @FXML
    private GridPane gpGeneralData;
    @FXML
    private Label lblPatientText;
    @FXML
    private Label lblAgeText;
    @FXML
    private Label lblCaseNumberText;
    @FXML
    private Label lblPatientNameData;
    @FXML
    private Label lblPatientNumberData;
    @FXML
    private VBox vBoxWeightAndAgeData;
    @FXML
    private Label lblPatientAgeData;
    @FXML
    private Label lblCaseNumberData;
    @FXML
    private Label lblInsuranceText;
    @FXML
    private Label lblInsuranceCompanyData;
    @FXML
    private Label lblInsuranceNumberData;
    @FXML
    private Label lblHospitalNameData;
    @FXML
    private Label lblHospitalNumberData;
    @FXML
    private Label lblHospitalText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources); //To change body of generated methods, choose Tools | Templates.
        setUpLanguage();
    }

    @Override
    public void afterInitialisingScene() {
        super.afterInitialisingScene();
        gpCaseDetails.showMenu();
        gpCaseDetails.setVersionContentWidth(350);
        ((ComparablePaneSkin)gpCaseDetails.getSkin()).setTableToVersionListener(new MergeVersionListener<>(gpCaseDetails));
        Bindings.bindContent(gpCaseDetails.getVersionList(), getScene().getListOfVersions());
        TCase hcase = gpCaseDetails.getBaseVersion().getContent();
        setCaseData(hcase);
        setPatientData(hcase.getPatient());

    }

    private void setUpLanguage() {
        lblAgeText.setText(Lang.getAge());
        lblCaseNumberText.setText(Lang.getCaseNumber());
        lblHospitalText.setText(Lang.getHospital());
        lblInsuranceText.setText(Lang.getInsurance());
        lblPatientText.setText(Lang.getPatient());
    }

    private void setPatientData(TPatient patient) {
        if (patient == null) {
            MainApp.showErrorMessageDialog("Patient can not be found!");
            return;
        }
        //sets the full name of the patient or only the second name if firstname is not set
        lblPatientNameData.setText(patient.getPatSecName() != null
                ? patient.getPatSecName()
                : "" + (patient.getPatFirstName() != null ? (", " + patient.getPatFirstName()) : ""));
        lblPatientNumberData.setText(patient.getPatNumber());
        lblPatientAgeData.setText(patient.getPatDateOfBirth() != null
                ? PatientHelper.getCurrentAgeLocalizedInYearsOrDays(patient.getPatDateOfBirth())
                : "");
//        try{
//            if (!patient.getInsurances().isEmpty()) {
//                lblInsuranceNumberData.setText(patient.getCurrentInsurance().getInsNumber());
//                CpxInsuranceCompany insurance = getScene().getFacade().getInsuranceData(patient.getCurrentInsurance().getInsNumber());
//                lblInsuranceCompanyData.setText(insurance.getInscName());
//                lblInsuranceCompanyData.setTooltip(new Tooltip(insurance.toString()));
//            }
//        }catch(Exception ex){
//            //catch NullPointer if set of insurance of patient is not initialized
//            LOG.log(Level.INFO, "Patient: " + patient.getId() + " has no Insurance!", ex);
//            lblInsuranceNumberData.setText("");
//            lblInsuranceCompanyData.setText("");
//        }
    }

    private void setCaseData(TCase currentCase) {
        lblCaseNumberData.setText(currentCase.getCsCaseNumber());
        lblHospitalNumberData.setText(currentCase.getCsHospitalIdent());
        CpxHospital hospital = getScene().getFacade().getHospitalData(currentCase.getCsHospitalIdent());
        lblHospitalNameData.setText(hospital.getHosName());
        lblHospitalNameData.setTooltip(new Tooltip(hospital.toString()));

        lblInsuranceNumberData.setText(currentCase.getInsuranceIdentifier());
        CpxInsuranceCompany insurance = getScene().getFacade().getInsuranceData(currentCase.getInsuranceIdentifier());
        lblInsuranceCompanyData.setText(insurance.getInscName());
        lblInsuranceCompanyData.setTooltip(new Tooltip(insurance.toString()));

    }

}
