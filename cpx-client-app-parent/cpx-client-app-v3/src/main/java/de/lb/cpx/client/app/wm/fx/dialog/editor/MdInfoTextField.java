/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.app.wm.fx.dialog.editor;

import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxMdk;
import de.lb.cpx.client.core.model.catalog.CpxMdkCatalog;
import de.lb.cpx.client.core.model.fx.labeled.LabeledInfoTextField;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.server.commonDB.model.CMdk;
import de.lb.cpx.shared.lang.Lang;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.Validator;

/**
 *
 * @author gerschmann
 */
public class MdInfoTextField extends LabeledInfoTextField{
    private LabeledTextField mdkDepartment;
    private LabeledTextField zipCode;
    private LabeledTextField address;
    private LabeledTextField emailAdress;
    private LabeledTextField areaCode;
    private LabeledTextField telephone;
    private LabeledTextField fax;
    private static final Logger LOG = Logger.getLogger(MdInfoTextField.class.getName());


    private ObjectProperty<CMdk> mdkProperty;
    
    public ObjectProperty<CMdk> mdkProperty(){
        if(mdkProperty == null){
            mdkProperty = new SimpleObjectProperty<>();
        }
        return mdkProperty;
    }
            
    public MdInfoTextField() {
        super(); 
 
    }

    /**
     * creates a new textField with that label
     *
     * @param pLabel label text
     */
    public MdInfoTextField(String pLabel) {
        this(pLabel, 0);

    }

    /**
     * creates a new textField with that label
     *
     * @param pLabel textArea
     * @param maxSize maximum size (maximum amount of characters)
     */
    public MdInfoTextField(String pLabel, int maxSize) {
        super(pLabel,  maxSize); // by default don't show character counts

    }


    /**
     * creates new LabeledTextField with label and control
     *
     * @param pLabel label text
     * @param pTextField control
     */
    public MdInfoTextField(String pLabel, TextField pTextField) {
        super(pLabel, pTextField);

    }

    /**
     * creates new LabeledTextField with label and control
     *
     * @param pLabel label text
     * @param pTextField control
     * @param maxSize maximum size (maximum amount of characters)
     */
    public MdInfoTextField(String pLabel, TextField pTextField, int maxSize) {
        super(pLabel, pTextField, maxSize); 

    }
    
//    protected void addPopupListener(){
//        
//         getAdditionalButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                VBox vBox = new VBox(8.0d);
//                vBox.getChildren().addAll(mdkDepartment, zipCode, address, emailAdress, areaCode, telephone, fax);
//                PopOver popover = showInfoPopover(vBox);
//                popover.setOnShowing(new EventHandler<WindowEvent>() {
//                    @Override
//                    public void handle(WindowEvent t) {
//                        setShowCaret(false);
//                    }
//                });
//                popover.setOnHiding(new EventHandler<WindowEvent>() {
//                    @Override
//                    public void handle(WindowEvent t) {
//                        setShowCaret(true);
//                    }
//                });
//                popover.show(getAdditionalButton());
////                showInfoPopover(vBox).show(btnMdkSearch);
//            }
//
//        });
//   }

    @Override    
    protected void addSearchButton(){
        super.addSearchButton();
        this.getAdditionalButton().setTooltip(new Tooltip(Lang.getMdkInformations()));
    }

    @Override
    protected void setupPopupFields() {

        mdkDepartment = new LabeledTextField(Lang.getMdkDepartment());
        mdkDepartment.setPrefWidth(300);
        mdkDepartment.setSpacing(5.0);
        addRelatedControl(mdkDepartment);
        zipCode = new LabeledTextField(Lang.getMdkAreaCode());
        zipCode.setSpacing(5.0);
        addRelatedControl(zipCode);
        address = new LabeledTextField(Lang.getMdkAddress());
        address.setSpacing(5.0);
        addRelatedControl(address);
        emailAdress = new LabeledTextField(Lang.getMdkEmail());
        emailAdress.setSpacing(5.0);
        addRelatedControl(emailAdress);
        areaCode = new LabeledTextField(Lang.getMdkTelephoneAreaCode());
        areaCode.setSpacing(5.0);
        addRelatedControl(areaCode);
        telephone = new LabeledTextField(Lang.getMdkTelephone());
        telephone.setSpacing(5.0);
        addRelatedControl(telephone);
        fax = new LabeledTextField(Lang.getMdkFax());
        fax.setSpacing(5.0);
        addRelatedControl(fax);

    }

    public CpxMdkCatalog getMdkCatalog() {
        return (CpxMdkCatalog)getCatalog();
    }


    public void setMdkCatalog(CpxMdkCatalog pMdkCatalog) {
        this.setCatalog(pMdkCatalog);
    }

    
    public void setUpdateListeners(){
        addPopupListener();
        getControl().focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if (!StringUtils.trimToEmpty(getText()).isEmpty()) {
                    CMdk mdk = getMdkCatalog().getByFullMdkName(getText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
                    if(mdk.getMdkName()== null) {
                        String error = Lang.getMdkNameValidate();
                        LOG.log(Level.WARNING, error);
                    }  else{
                        mdkProperty().set(mdk);
                    }
                }
            }
        });

        mdkDepartment.getControl().focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if (!StringUtils.trimToEmpty(mdkDepartment.getText()).isEmpty()) {
                    if (getMdkCatalog().getByMDKDienststelle(mdkDepartment.getText(), AbstractCpxCatalog.DEFAULT_COUNTRY).getMdkDepartment() == null) {
                        String error = Lang.getMdkDepartmentValidate();
                        LOG.log(Level.WARNING, error);
                    }
                }
            }
        });

        zipCode.getControl().focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if (!StringUtils.trimToEmpty(zipCode.getText()).isEmpty()) {
                    if (getMdkCatalog().getByMDKPostleitzahl(zipCode.getText(), AbstractCpxCatalog.DEFAULT_COUNTRY).getMdkZipCode() == null) {
                        String error = Lang.getMdkZipCodeValidate();
                        LOG.log(Level.WARNING, error);
                    }
                }
            }
        });


        address.getControl().focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if (!address.getText().isEmpty()) {
                    if (getMdkCatalog().getByAnschrift(address.getText(), AbstractCpxCatalog.DEFAULT_COUNTRY).getMdkStreet() == null) {
                        String error = Lang.getMdkAddressValidate();
                        LOG.log(Level.WARNING, error);
                    }
                }
            }
        });

        emailAdress.getControl().focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if (!emailAdress.getText().isEmpty()) {
                    if (getMdkCatalog().getByMailAdresse(emailAdress.getText(), AbstractCpxCatalog.DEFAULT_COUNTRY).getMdkEmail() == null) {
                        String error = Lang.getMdkMailAddressValidate();
                        LOG.log(Level.WARNING, error);
                    }
                }
            }
        });

        fax.getControl().focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if (!fax.getText().isEmpty()) {
                    if (getMdkCatalog().getByFax(fax.getText(), AbstractCpxCatalog.DEFAULT_COUNTRY).getMdkFax() == null) {
                        String error = Lang.getMdkFaxValidate();
                        LOG.log(Level.WARNING, error);
                    }
                }
            }
        });

        telephone.getControl().focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if (!telephone.getText().isEmpty()) {
                    if (getMdkCatalog().getByTelefon(telephone.getText(), AbstractCpxCatalog.DEFAULT_COUNTRY).getMdkPhone() == null) {
                        String error = Lang.getMdkPhoneNoValidate();
                        LOG.log(Level.WARNING, error);
                    }
                }
            }
        });
        
    }
    
    public void setUpAutoCompletion(){
    AutoCompletionBinding<String> ACBmdkName = TextFields.bindAutoCompletion(getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return getMdkCatalog().findMatchesByMdkName(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        ACBmdkName.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                String str[] = getText().split(", ");
                updateCatalogValues(getMdkCatalog().getByNameDeptCity(str[0], str[1], str[2], AbstractCpxCatalog.DEFAULT_COUNTRY));
            }
        });

        ACBmdkName.prefWidthProperty().bind(widthProperty());
        

        AutoCompletionBinding<String> ACBdienstStelle = TextFields.bindAutoCompletion(mdkDepartment.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return getMdkCatalog().findMatchesByMdkDienstStelle(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        ACBdienstStelle.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                String str = mdkDepartment.getText();
                updateCatalogValues((CpxMdk) getMdkCatalog().getByMDKDienststelle(str, AbstractCpxCatalog.DEFAULT_COUNTRY));
            }
        });
        ACBdienstStelle.prefWidthProperty().bind(mdkDepartment.widthProperty());

        AutoCompletionBinding<String> ACBpostleitzahl = TextFields.bindAutoCompletion(zipCode.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return getMdkCatalog().findMatchesByPostleitzahl(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        ACBpostleitzahl.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                String str = zipCode.getText();
                updateCatalogValues((CpxMdk) getMdkCatalog().getByMDKPostleitzahl(str, AbstractCpxCatalog.DEFAULT_COUNTRY));
            }
        });
        ACBpostleitzahl.prefWidthProperty().bind(zipCode.widthProperty());

        AutoCompletionBinding<String> ACBanschrift = TextFields.bindAutoCompletion(address.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return getMdkCatalog().findMatchesByAnschrift(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        ACBanschrift.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                String str = address.getText();
                updateCatalogValues((CpxMdk) getMdkCatalog().getByAnschrift(str, AbstractCpxCatalog.DEFAULT_COUNTRY));
            }
        });
        ACBanschrift.prefWidthProperty().bind(address.widthProperty());

        AutoCompletionBinding<String> ACBmailAdresse = TextFields.bindAutoCompletion(emailAdress.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return getMdkCatalog().findMatchesByMailAdresse(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        ACBmailAdresse.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                String str = emailAdress.getText();
                updateCatalogValues((CpxMdk) getMdkCatalog().getByMailAdresse(str, AbstractCpxCatalog.DEFAULT_COUNTRY));
            }
        });

        AutoCompletionBinding<String> ACBvorwahl = TextFields.bindAutoCompletion(areaCode.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return getMdkCatalog().findMatchesByVorwahl(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        ACBvorwahl.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                String str = areaCode.getText();
                updateCatalogValues((CpxMdk) getMdkCatalog().getByMailVorwahl(str, AbstractCpxCatalog.DEFAULT_COUNTRY));
            }
        });

        AutoCompletionBinding<String> ACBtelefon = TextFields.bindAutoCompletion(telephone.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return getMdkCatalog().findMatchesByTelefon(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        ACBtelefon.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                String str = telephone.getText();
                updateCatalogValues((CpxMdk) getMdkCatalog().getByTelefon(str, AbstractCpxCatalog.DEFAULT_COUNTRY));
            }
        });

        AutoCompletionBinding<String> ACBfax = TextFields.bindAutoCompletion(fax.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return getMdkCatalog().findMatchesByFax(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        ACBfax.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                String str = fax.getText();
                updateCatalogValues(getMdkCatalog().getByFax(str, AbstractCpxCatalog.DEFAULT_COUNTRY));
            }
        });

        
    }
    
    public void setupValidation(){
       getValidationSupport().registerValidator(getControl(), new Validator<String>() {
            @Override
            public ValidationResult apply(Control t, String u) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, "Bitte geben Sie die IKZ der Versicherung an!", u == null || u.isEmpty());
//                res.addErrorIf(t, "not valid", (getSkinnable().getRequest().getInsuranceIdentifier()!=null && getSkinnable().getRequest().getInsuranceIdentifier().isEmpty()));
                if (u != null) {
                    String str[] = u.split(", ");
                    res.addErrorIf(t, "Bitte geben Sie eine korrekte IKZ ein!", !getMdkCatalog().hasEntry(str[0], AbstractCpxCatalog.DEFAULT_COUNTRY));
                }
                return res;
            }
        });

        getValidationSupport().registerValidator(getControl(), new Validator<String>() {
            @Override
            public ValidationResult apply(Control t, String mdkNameDeptCity) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, "Bitte geben Sie den MDK Namen ein!", (mdkNameDeptCity == null || mdkNameDeptCity.isEmpty()));
                String str[] = mdkNameDeptCity == null ? new String[0] : mdkNameDeptCity.split(", ");
//                        res.addErrorIf(t, Lang.getValidationErrorInvalidMdkName(), (mdkNameDeptCity == null || mdkNameDeptCity.isEmpty()) || !mdkCatalog.hasEntry(mdkNameDeptCity, AbstractCpxCatalog.DEFAULT_COUNTRY));
                // Needs to be check, what exactly do we need here?
                switch (str.length) {
                    case 3:
                        res.addErrorIf(t, Lang.getValidationErrorInvalidMdkName(), !getMdkCatalog().hasEntry(str[0], AbstractCpxCatalog.DEFAULT_COUNTRY) || !getMdkCatalog().hasDeptEntry(str[1], AbstractCpxCatalog.DEFAULT_COUNTRY) || !getMdkCatalog().hasCityEntry(str[2], AbstractCpxCatalog.DEFAULT_COUNTRY));
                        getValidationSupport().setErrorDecorationEnabled(true);
                        break;
                    case 2:
                        res.addErrorIf(t, Lang.getValidationErrorInvalidMdkName(), !getMdkCatalog().hasEntry(str[0], AbstractCpxCatalog.DEFAULT_COUNTRY) || !getMdkCatalog().hasDeptEntry(str[1], AbstractCpxCatalog.DEFAULT_COUNTRY));
                        getValidationSupport().setErrorDecorationEnabled(true);
                        break;
                    case 1:
                        res.addErrorIf(t, Lang.getValidationErrorInvalidMdkName(), !getMdkCatalog().hasEntry(str[0], AbstractCpxCatalog.DEFAULT_COUNTRY));
                        getValidationSupport().setErrorDecorationEnabled(true);
                        break;
                    default:
                        break;
                }
                return res;
            }
        });

        getValidationSupport().registerValidator(mdkDepartment.getControl(), new Validator<String>() {
            @Override
            public ValidationResult apply(Control t, String mdkDept) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, "Bitte geben Sie die MDK Dienststelle ein! ", (mdkDept == null || mdkDept.isEmpty()));
                res.addErrorIf(t, "MDK Dienststelle ist nicht korrekt!", (!getMdkCatalog().hasDeptEntry(mdkDept, AbstractCpxCatalog.DEFAULT_COUNTRY)));
                getValidationSupport().setErrorDecorationEnabled(true);
                return res;
            }
        });

        getValidationSupport().registerValidator(zipCode.getControl(), false, new Validator<String>() {
            @Override
            public ValidationResult apply(Control t, String mdkZipCode) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, "MDK Postleitzahl ist nicht korrekt!", (mdkZipCode != null && !mdkZipCode.isEmpty()) && !getMdkCatalog().hasZipCodeEntry(mdkZipCode, AbstractCpxCatalog.DEFAULT_COUNTRY));
                return res;
            }
        });

        getValidationSupport().registerValidator(address.getControl(), false, new Validator<String>() {
            @Override
            public ValidationResult apply(Control t, String mdkAddress) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, "MDK Anschrift ist nicht korrekt!", (mdkAddress != null && !mdkAddress.isEmpty()) && !getMdkCatalog().hasAddressEntry(mdkAddress, AbstractCpxCatalog.DEFAULT_COUNTRY));
                return res;
            }
        });

        getValidationSupport().registerValidator(emailAdress.getControl(), false, new Validator<String>() {
            @Override
            public ValidationResult apply(Control t, String mdkEmailAddress) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, "MDK Mail-Adresse ist nicht korrekt!", (mdkEmailAddress != null && !mdkEmailAddress.isEmpty()) && !getMdkCatalog().hasEmailAddressEntry(mdkEmailAddress, AbstractCpxCatalog.DEFAULT_COUNTRY));
                return res;
            }
        });

        getValidationSupport().registerValidator(areaCode.getControl(), false, new Validator<String>() {
            @Override
            public ValidationResult apply(Control t, String mdkAreaCode) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, "MDK Vorwahl ist nicht korrekt!", (mdkAreaCode != null && !mdkAreaCode.isEmpty()) && !getMdkCatalog().hasAreaCodeEntry(mdkAreaCode, AbstractCpxCatalog.DEFAULT_COUNTRY));
                return res;
            }
        });
        getValidationSupport().registerValidator(telephone.getControl(), false, new Validator<String>() {
            @Override
            public ValidationResult apply(Control t, String mdkTelephone) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, "MDK Telefon ist nicht korrekt!", (mdkTelephone != null && !mdkTelephone.isEmpty()) && !getMdkCatalog().hasTelephoneEntry(mdkTelephone, AbstractCpxCatalog.DEFAULT_COUNTRY));
                return res;
            }
        });
        getValidationSupport().registerValidator(fax.getControl(), false, new Validator<String>() {
            @Override
            public ValidationResult apply(Control t, String mdkFax) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, "MDK Fax ist nicht korrekt!", (mdkFax != null && !mdkFax.isEmpty()) && !getMdkCatalog().hasFaxEntry(mdkFax, AbstractCpxCatalog.DEFAULT_COUNTRY));
                return res;
            }
        });

        
    }
    
    public void updateCatalogValues(CpxMdk mdk){
        if (mdk.id == 0L) {
            setText(mdk.getMdkName());
        } else {
            setText(mdk.getMdkName() + ", " + mdk.getMdkDepartment() + ", " + mdk.getMdkCity());
        }

        mdkDepartment.setText(mdk.getMdkDepartment());
        zipCode.setText(mdk.getMdkZipCode());
        address.setText(mdk.getMdkStreet());
        emailAdress.setText(mdk.getMdkEmail());
        areaCode.setText(mdk.getMdkPhonePrefix());
        telephone.setText(mdk.getMdkPhone());
        fax.setText(mdk.getMdkFax());
        
    }
    public void disableControls(boolean armed) {
        getControl().setDisable(armed);
        this.getAdditionalButton().setDisable(armed);
        mdkDepartment.setDisable(armed);
        zipCode.setDisable(armed);
        address.setDisable(armed);
        emailAdress.setDisable(armed);
        areaCode.setDisable(armed);
        telephone.setDisable(armed);
        fax.setDisable(armed);        
    }

    public Long getMdkInternalId() {
        return getMdkCatalog().getByFullMdkName(getControl().getText(), AbstractCpxCatalog.DEFAULT_COUNTRY).getMdkInternalId();
}

    void setMdInternalId(Long mdInternalId) {
        updateCatalogValues(getMdkCatalog().getByInternalId(mdInternalId,  AbstractCpxCatalog.DEFAULT_COUNTRY));
    }




}
