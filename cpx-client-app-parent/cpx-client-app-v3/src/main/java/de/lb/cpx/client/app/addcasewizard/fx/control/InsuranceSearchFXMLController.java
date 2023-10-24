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

import de.lb.cpx.client.app.addcasewizard.dialog.InsuranceSearchDialog.InsuranceSearchScreen;
import de.lb.cpx.client.app.service.facade.AddCaseServiceFacade;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompany;
import de.lb.cpx.client.core.model.fx.labeled.LabeledLabel;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.shared.lang.Lang;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.text.FontWeight;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author shahin
 */
public class InsuranceSearchFXMLController extends Controller<InsuranceSearchScreen> {

    private AddCaseServiceFacade serviceFacade;
    @FXML
    private LabeledTextField tfInsName;
    @FXML
    private LabeledTextField tfInsIdent;
    @FXML
    private LabeledTextField tfCity;
    @FXML
    private LabeledTextField tfzipCode;
    @FXML
    private LabeledLabel labelPrefix;
    @FXML
    private LabeledLabel labelPhone;
    @FXML
    private LabeledLabel labelFax;
    @FXML
    private LabeledLabel labelAddress;
    @FXML
    private Label labelInfo;

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
        tfInsIdent.setTitle(Lang.getInsuranceIdent());
        tfInsIdent.applyFontWeightToTitle(FontWeight.BOLD);
        tfInsName.setTitle(Lang.getAuditInsuName());
        tfInsName.applyFontWeightToTitle(FontWeight.BOLD);
        labelAddress.setTitle(Lang.getAddress());
        labelAddress.applyFontWeightToTitle(FontWeight.BOLD);
        tfCity.setTitle(Lang.getAddressCity());
        tfCity.applyFontWeightToTitle(FontWeight.BOLD);
        tfzipCode.setTitle(Lang.getAddressZipCode());
        tfzipCode.applyFontWeightToTitle(FontWeight.BOLD);
        labelPrefix.setTitle("Vorwahl");
        labelPrefix.applyFontWeightToTitle(FontWeight.BOLD);

        labelPhone.setTitle(Lang.getAddressPhoneNumber());
        labelPhone.applyFontWeightToTitle(FontWeight.BOLD);

        labelFax.setTitle(Lang.getFax());

        labelFax.applyFontWeightToTitle(FontWeight.BOLD);
        labelInfo.setText(Lang.getAuditInfoLabel());
        //resetDataForInsurance();
        tfInsIdent.setText("");
        tfInsName.setText("");
        tfInsIdent.setText("");
        tfCity.setText("");
        setLabel(false);

    }

    /**
     * set up TextFields ,Controllers
     */
    private void setUpTextFields() {
//        
        AutoCompletionBinding<String> InsIdentComp = TextFields.bindAutoCompletion(tfInsIdent.getControl(), (AutoCompletionBinding.ISuggestionRequest param) -> serviceFacade.getMatchesForInsuranceNumber(param.getUserText()));
        InsIdentComp.prefWidthProperty().bind(tfInsIdent.widthProperty());
        InsIdentComp.setHideOnEscape(true);
        InsIdentComp.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<String> event) -> {
            String str = tfInsIdent.getText();
            // setDataForInsuracne(serviceFacade.getInsuranceByIdent(str));
            setDataForInsuracne(serviceFacade.getInsuranceDetailByIdent(str));
        });
//        InsIdentComp.prefWidthProperty().bind(tfInsIdent.widthProperty());
//        tfInsIdent.getControl().textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                
//               if(tfInsIdent.getText().isEmpty()){
//                  resetDataForInsurance();
//               }
//                
//           } 
//
//    
//        });
//        
        AutoCompletionBinding<String> InsNameComp = TextFields.bindAutoCompletion(tfInsName.getControl(), (AutoCompletionBinding.ISuggestionRequest param) -> serviceFacade.getMatchesForInsuranceName(param.getUserText()));

//        InsNameComp.setPrefWidth(550);
        InsNameComp.prefWidthProperty().bind(tfInsName.widthProperty());
        InsNameComp.setHideOnEscape(true);
        InsNameComp.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<String> event) -> {
            String str = tfInsName.getText();
            // str=INSC_NAME , INSC_IDENT,INSC_CITY
            setDataForInsuracne(serviceFacade.getInsuranceByName(str));
        });

        AutoCompletionBinding<String> InsZipCodeComp = TextFields.bindAutoCompletion(tfzipCode.getControl(), (AutoCompletionBinding.ISuggestionRequest param) -> serviceFacade.getMatchesForInsuranceZipCode(param.getUserText()));
        InsZipCodeComp.setPrefWidth(550);
        InsZipCodeComp.setHideOnEscape(true);
        InsZipCodeComp.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<String> event) -> {
            String str = tfzipCode.getText();
            setDataForInsuracne(serviceFacade.getInsuranceByZipCode(str));
        });

        InsZipCodeComp.prefWidthProperty().bind(tfzipCode.widthProperty());
        tfzipCode.getControl().textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (tfzipCode.getText().isEmpty()) {
                resetDataForInsurance();
            }
        });

        AutoCompletionBinding<String> InsCityComp = TextFields.bindAutoCompletion(tfCity.getControl(), (AutoCompletionBinding.ISuggestionRequest param) -> serviceFacade.getMatchesForInsuranceCity(param.getUserText()));
        InsCityComp.setPrefWidth(550);
        InsCityComp.setHideOnEscape(true);
        InsCityComp.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<String> event) -> {
            String str = tfCity.getText();
            setDataForInsuracne(serviceFacade.getInsuranceByCity(str));
        });
        InsCityComp.prefWidthProperty().bind(tfCity.widthProperty());
//        tfCity.getControl().textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                
//               if(tfCity.getText().isEmpty()){
//                  resetDataForInsurance();
//               }
//                
//           } 
//
//    
//        });

    }

    /**
     *
     * @param pIns CpxInsuranceCompany Katalog
     */
    public void setCpxInsuranceCompany(CpxInsuranceCompany pIns) {
        getScene().setInsurance(pIns);
    }

    /**
     * Based on CpxInsuranceCompany, set Insurance data (ident ,name,addresse,
     * zip code, city,prefix ,phone ,Fax).
     *
     * @param pIns CpxInsuranceCompany Katalog
     */
    public void setDataForInsuracne(CpxInsuranceCompany pIns) {
        tfInsIdent.setText(pIns.getInscIdent());
        tfInsIdent.setTooltip(new Tooltip(pIns.getInscIdent()));

        tfInsName.setText(pIns.getInscName());
        tfInsName.setTooltip(new Tooltip(pIns.getInscName()));

        tfzipCode.setText(pIns.getInscZipCode() != null ? pIns.getInscZipCode() : "");
        tfzipCode.setTooltip(new Tooltip(pIns.getInscZipCode()));

        tfCity.setText(pIns.getInscCity());
        tfCity.setTooltip(new Tooltip(pIns.getInscCity()));

        labelAddress.setText(pIns.getInscAddress());
        labelAddress.setTooltip(new Tooltip(pIns.getInscAddress()));

        labelPrefix.setText(pIns.getInscPhonePrefix());
        labelPrefix.setTooltip(new Tooltip(pIns.getInscPhonePrefix()));

        labelPhone.setText(pIns.getPhoneNumber());
        labelPhone.setTooltip(new Tooltip(pIns.getPhoneNumber()));

        labelFax.setText(pIns.getFaxNumber());
        labelFax.setTooltip(new Tooltip(pIns.getFaxNumber()));

        setCpxInsuranceCompany(pIns);
        setLabel(true);

    }

    private void setLabel(boolean b) {
        labelAddress.setVisible(b);
        labelFax.setVisible(b);
        labelPhone.setVisible(b);
        labelPrefix.setVisible(b);
        labelInfo.setVisible(b);

    }

    /**
     * refrech Insurance data (ident ,name,addresse, zip code, city ,prefix
     * ,phone ,Fax ).
     *
     */
    public void resetDataForInsurance() {
        tfInsIdent.setText("");
        tfInsIdent.setTooltip(new Tooltip(""));

        tfInsName.setText("");
        tfInsName.setTooltip(new Tooltip(""));

        tfzipCode.setText("");
        tfzipCode.setTooltip(new Tooltip(""));

        tfCity.setText("");
        tfCity.setTooltip(new Tooltip(""));

        labelAddress.setText("");
        labelAddress.setTooltip(new Tooltip(""));

        labelPrefix.setText("");
        labelPrefix.setTooltip(new Tooltip(""));

        labelPhone.setText("");
        labelPhone.setTooltip(new Tooltip(""));

        labelFax.setText("");
        labelFax.setTooltip(new Tooltip(""));
    }
}
