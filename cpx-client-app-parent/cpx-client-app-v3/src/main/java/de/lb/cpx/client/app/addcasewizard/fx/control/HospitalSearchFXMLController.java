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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.addcasewizard.fx.control;

import de.lb.cpx.client.app.addcasewizard.dialog.HospitalSearchDialog.HospitalSearchScreen;
import de.lb.cpx.client.app.service.facade.AddCaseServiceFacade;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.catalog.CpxHospital;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TLab;
import de.lb.cpx.model.enums.CaseStatusEn;
import de.lb.cpx.shared.lang.Lang;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Tooltip;
import javafx.scene.text.FontWeight;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author shahin
 */
public class HospitalSearchFXMLController extends Controller<HospitalSearchScreen> {

    private AddCaseServiceFacade serviceFacade;
    @FXML
    private LabeledTextField tfCity;
    @FXML
    private LabeledTextField tfAddresse;

    @FXML
    private LabeledTextField tfHosName;
    @FXML
    private LabeledTextField tfHosIdent;
    @FXML
    private LabeledTextField tfzipCode;

    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serviceFacade = new AddCaseServiceFacade();
        setUpLanguage();
        setUpTextFields();

    }

    /**
     * set up languages
     */
    private void setUpLanguage() {
        tfHosIdent.setTitle(Lang.getHospitalIdent());
        tfHosIdent.applyFontWeightToTitle(FontWeight.BOLD);
        tfHosName.setTitle(Lang.getHospitalName());
        tfHosName.applyFontWeightToTitle(FontWeight.BOLD);
        tfAddresse.setTitle(Lang.getAddress());
        tfAddresse.applyFontWeightToTitle(FontWeight.BOLD);
        tfCity.setTitle(Lang.getAddressCity());
        tfCity.applyFontWeightToTitle(FontWeight.BOLD);
        tfzipCode.setTitle(Lang.getAddressZipCode());
        tfzipCode.applyFontWeightToTitle(FontWeight.BOLD);
    }

    /**
     * set up TextFields ,Controllers
     */
    private void setUpTextFields() {

        AutoCompletionBinding<String> hosIdentComp = TextFields.bindAutoCompletion(tfHosIdent.getControl(), (AutoCompletionBinding.ISuggestionRequest param) -> serviceFacade.getBestMatchesForHospitalIdent(param.getUserText()));
        hosIdentComp.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<String> event) -> {
            String str = tfHosIdent.getText();
//                setDataForHospital(serviceFacade.getHospitalByIdent(str));
            setDataForHospital(serviceFacade.getHospitaldetailsByIdent(str));
        });
        hosIdentComp.prefWidthProperty().bind(tfHosIdent.widthProperty());
        tfHosIdent.getControl().textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (tfHosIdent.getText() == null || tfHosIdent.getText().isEmpty()) {
                removeDataForHospital();
            }
        });
        AutoCompletionBinding<String> hosNameComp = TextFields.bindAutoCompletion(tfHosName.getControl(), (AutoCompletionBinding.ISuggestionRequest param) -> serviceFacade.getBestMatchesForHospitalName(param.getUserText()));
        hosNameComp.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<String> event) -> {
            String str = tfHosName.getText();
//                setDataForHospital(serviceFacade.getHospitalByName(str));
            setDataForHospital(serviceFacade.getHospitaldetailsByName(str));
        });
        hosNameComp.prefWidthProperty().bind(tfHosName.widthProperty());
        tfHosName.getControl().textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (tfHosName.getText() == null || tfHosName.getText().isEmpty()) {
                removeDataForHospital();
            }
        });

        AutoCompletionBinding<String> zipCode = TextFields.bindAutoCompletion(tfzipCode.getControl(), (AutoCompletionBinding.ISuggestionRequest param) -> serviceFacade.getBestMatchesForHospitalZipCode(param.getUserText()));
        zipCode.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<String> event) -> {
            String str = tfzipCode.getText();
            setDataForHospital(serviceFacade.getHospitalDetailsByZipCode(str));
        });
        zipCode.prefWidthProperty().bind(tfzipCode.widthProperty());
        tfzipCode.getControl().textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (tfzipCode.getText() == null || tfzipCode.getText().isEmpty()) {
                removeDataForHospital();
            }
        });

        AutoCompletionBinding<String> addresse = TextFields.bindAutoCompletion(tfAddresse.getControl(), (AutoCompletionBinding.ISuggestionRequest param) -> serviceFacade.getBestMatchesForHospitalAddresse(param.getUserText()));
        addresse.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<String> event) -> {
            String str = tfAddresse.getText();
            setDataForHospital(serviceFacade.getHospitalByAddresse(str));
        });
        addresse.prefWidthProperty().bind(tfAddresse.widthProperty());
        tfAddresse.getControl().textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (tfAddresse.getText() == null || tfAddresse.getText().isEmpty()) {
                removeDataForHospital();
            }
        });

        AutoCompletionBinding<String> city = TextFields.bindAutoCompletion(tfCity.getControl(), (AutoCompletionBinding.ISuggestionRequest param) -> serviceFacade.getBestMatchesForHospitalCity(param.getUserText()));
        city.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<String> event) -> {
            String str = tfCity.getText();
            setDataForHospital(serviceFacade.getHospitalDetailsByCity(str));
        });
        city.prefWidthProperty().bind(tfCity.widthProperty());
        tfCity.getControl().textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (tfCity.getText() == null || tfCity.getText().isEmpty()) {
                removeDataForHospital();
            }
        });

    }

    /**
     *
     * @return currentCase
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException invalid argument
     */
    public TCase onSave() throws CpxIllegalArgumentException {
        TCase currentCase = serviceFacade.getCurrentCase();
        if (currentCase == null) {
            currentCase = new TCase();
            currentCase.setCsStatusEn(CaseStatusEn.NEW);
            currentCase.setPatient(serviceFacade.getCurrentPatient());
            TCaseDetails externDetails = new TCaseDetails();
            currentCase.setCurrentExtern(externDetails);
            externDetails.setHospitalCase(currentCase);
            currentCase.setCsHospitalIdent(tfHosIdent.getText());
            currentCase.setCaseLabor(new HashSet<TLab>());

        }
        serviceFacade.setCurrentCase(currentCase);

        return currentCase;
    }

    /**
     *
     * @param phospital CpxHospital Katalog
     */
    public void setCpxHospital(CpxHospital phospital) {
        getScene().setHospital(phospital);
    }

    /**
     * Based on CpxHospital, set Hospital data (ident ,name,addresse, zip code,
     * city).
     *
     * @param phospital CpxHospital Katalog
     */
    public void setDataForHospital(CpxHospital phospital) {
        tfHosIdent.setText(phospital.getHosIdent());
        tfHosIdent.setTooltip(new Tooltip(phospital.getHosIdent()));
        tfHosName.setText(phospital.getHosName());
        tfHosName.setTooltip(new Tooltip(phospital.getHosName()));
        tfAddresse.setText(phospital.getHosAddress());
        tfAddresse.setTooltip(new Tooltip(phospital.getHosAddress()));

        tfzipCode.setText(phospital.getHosZipCode());
        tfzipCode.setTooltip(new Tooltip(phospital.getHosZipCode()));
        tfCity.setText(phospital.getHosCity());
        //tfCity.setText(hospital.getHosCity()+" ,"+hospital.getHosName()+" ,"+hospital.getHosIdent());
        tfCity.setTooltip(new Tooltip(phospital.getHosCity()));
        //serviceFacade.getCurrentCase().setCsHospitalIdent(tfHosIdent.getText());
        setCpxHospital(phospital);

    }

    /**
     * refrech Hospital data (ident ,name,addresse, zip code, city).
     *
     */
    public void removeDataForHospital() {
        tfHosIdent.setText("");
        tfHosIdent.setTooltip(null);
        tfHosName.setText("");
        tfHosName.setTooltip(null);
        tfAddresse.setText("");
        tfAddresse.setTooltip(null);
        tfzipCode.setText("");
        tfzipCode.setTooltip(null);
        tfCity.setText("");
        tfCity.setTooltip(null);
    }

}
