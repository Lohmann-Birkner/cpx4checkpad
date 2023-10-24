/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.app.wm.fx.dialog.editor;

import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompany;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompanyCatalog;
import de.lb.cpx.client.core.model.fx.labeled.LabeledInfoTextField;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.shared.lang.Lang;
import java.util.Collection;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.Validator;

/**
 *
 * @author gerschmann
 */
public class InsuranceInfoTextField extends LabeledInfoTextField{

    private LabeledTextField ltfZipCode;
    private LabeledTextField ltfCity;
    private LabeledTextField ltfAddress;
    private LabeledTextField ltfAreaCode;
    private LabeledTextField ltfTelefon;
    private LabeledTextField ltfFax;

    
    public InsuranceInfoTextField() {
        super(); 
 
    }

    /**
     * creates a new textField with that label
     *
     * @param pLabel label text
     */
    public InsuranceInfoTextField(String pLabel) {
        this(pLabel, 0);

    }

    /**
     * creates a new textField with that label
     *
     * @param pLabel textArea
     * @param maxSize maximum size (maximum amount of characters)
     */
    public InsuranceInfoTextField(String pLabel, int maxSize) {
        super(pLabel,  maxSize); // by default don't show character counts

    }

    /**
     * creates new LabeledTextField with label and control
     *
     * @param pLabel label text
     * @param pTextField control
     */
    public InsuranceInfoTextField(String pLabel, TextField pTextField) {
        super(pLabel, pTextField);

    }

    /**
     * creates new LabeledTextField with label and control
     *
     * @param pLabel label text
     * @param pTextField control
     * @param maxSize maximum size (maximum amount of characters)
     */
    public InsuranceInfoTextField(String pLabel, TextField pTextField, int maxSize) {
        super(pLabel, pTextField, maxSize); 

    }
    
    @Override    
    protected void addSearchButton(){
        super.addSearchButton();
        this.getAdditionalButton().setTooltip(new Tooltip("Versicherungsinformationen"));
//        this.getTextProperty().addListener(new ClearAutoCompleteFieldsListener());
    }
    
    private SimpleObjectProperty<LabeledTextField> insNumberFieldProperty;
    
    private SimpleObjectProperty<LabeledTextField> getInsNumberFieldProperty(){
        if(insNumberFieldProperty == null){
            insNumberFieldProperty = new SimpleObjectProperty<LabeledTextField>();
        }
        return insNumberFieldProperty;
    }
    
    public void setRelatedInsNumberField(LabeledTextField pInsNumberField){
        getInsNumberFieldProperty().set(pInsNumberField);
        pInsNumberField.getTextProperty().addListener(new ClearAutoCompleteFieldsListener());
    }
    
    public LabeledTextField getRelatedInsNumberField(){
        return getInsNumberFieldProperty().get();
    } 
    
//        protected void addPopupListener(){
//        
//         getAdditionalButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                VBox vBox = new VBox(8.0d);
//                vBox.getChildren().addAll(ltfAddress, ltfZipCode, ltfCity, ltfAreaCode, ltfTelefon, ltfFax);
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
//
//            }
//
//        });
//   }
//

    @Override
    protected void setupPopupFields() {
        ltfZipCode = new LabeledTextField();
        addRelatedControl(ltfZipCode);
        ltfZipCode.setSpacing(5.0);
        ltfCity = new LabeledTextField();
        ltfCity.setSpacing(5.0);
        addRelatedControl(ltfCity);
        ltfAddress = new LabeledTextField();
        ltfAddress.setSpacing(5.0);
        addRelatedControl(ltfAddress);
        ltfAreaCode = new LabeledTextField();
        ltfAreaCode.setSpacing(5.0);
        addRelatedControl(ltfAreaCode);
        ltfTelefon = new LabeledTextField();
        ltfTelefon.setSpacing(5.0);
        addRelatedControl(ltfTelefon);
        ltfFax = new LabeledTextField();
        ltfFax.setSpacing(5.0);
        addRelatedControl(ltfFax);
        ltfZipCode.setTitle(Lang.getAddressZipCode());
        ltfZipCode.applyFontWeightToTitle(FontWeight.BOLD);
//        ltfZipCode.getTextProperty().addListener(new ClearAutoCompleteFieldsListener());
        ltfCity.setTitle(Lang.getAddressCity());
        ltfCity.applyFontWeightToTitle(FontWeight.BOLD);
//        ltfCity.getTextProperty().addListener(new ClearAutoCompleteFieldsListener());
        ltfAddress.setTitle(Lang.getAddressTypePostal());
        ltfAddress.applyFontWeightToTitle(FontWeight.BOLD);
//        ltfAddress.getTextProperty().addListener(new ClearAutoCompleteFieldsListener());
        ltfAddress.setPrefWidth(400);
        ltfAreaCode.setTitle(Lang.getAuditTelephoneAreaCode());
        ltfAreaCode.applyFontWeightToTitle(FontWeight.BOLD); 
//        ltfAreaCode.getTextProperty().addListener(new ClearAutoCompleteFieldsListener());
        ltfTelefon.setTitle(Lang.getAddressPhoneNumber());
        ltfTelefon.applyFontWeightToTitle(FontWeight.BOLD);
//        ltfTelefon.getTextProperty().addListener(new ClearAutoCompleteFieldsListener());
        ltfFax.setTitle(Lang.getFax());
        ltfFax.applyFontWeightToTitle(FontWeight.BOLD);
//        ltfFax.getTextProperty().addListener(new ClearAutoCompleteFieldsListener());
        
    }
    
    @Override
    protected void disableControls(boolean armed) {
        getControl().setDisable(armed);
        ltfZipCode.setDisable(armed);
        ltfCity.setDisable(armed);
        ltfAddress.setDisable(armed);
        ltfAreaCode.setDisable(armed);
        ltfTelefon.setDisable(armed);
        ltfFax.setDisable(armed);
        this.getAdditionalButton().setDisable(armed);
    }
    
    
    public CpxInsuranceCompanyCatalog getInsuranceCatalog() {
        return (CpxInsuranceCompanyCatalog)getCatalog();
    }


    public void setInsuranceCatalog(CpxInsuranceCompanyCatalog pInsCatalog) {
        this.setCatalog(pInsCatalog);
    }


    
    @Override
    public void setUpAutoCompletion() {
        AutoCompletionBinding<String> nameComp = TextFields.bindAutoCompletion(getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
//                return getSkinnable().getInsuranceCatalog().findMatchesByInsuranceName(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
                return getInsuranceCatalog().findMatchesByInsName(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        nameComp.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                String[] split = getText().split(" , ");
                //risky call .. if someone changes this shit, we are screwed on runtime
//                if (split.length == 3) {
//                getSkinnable().setInsuranceCompany(getSkinnable().getInsuranceCatalog().getByCityInsNameIdent(split[2], split[0], split[1], AbstractCpxCatalog.DEFAULT_COUNTRY));
////                getSkinnable().setInsuranceCompany(getSkinnable().getInsuranceCatalog().getByName(ltfInsName.getText().split(" , ")[0], AbstractCpxCatalog.DEFAULT_COUNTRY));
//                }
//                else 
//Fix Exception wenn City is null 
                if (split[1] != null) {
                    setInsuranceCompany(getInsuranceCatalog().getByIdent(split[1], AbstractCpxCatalog.DEFAULT_COUNTRY));
                    setText(split[0]);
                }
            }
        });
        nameComp.prefWidthProperty().bind(widthProperty());

        AutoCompletionBinding<String> numberComp = TextFields.bindAutoCompletion(getRelatedInsNumberField().getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return getInsuranceCatalog().findMatchesByInsuranceNumber(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        numberComp.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                setInsuranceCompany(getInsuranceCatalog().getByCode(getRelatedInsNumberField().getText().split(" , ")[0], AbstractCpxCatalog.DEFAULT_COUNTRY));
                getRelatedInsNumberField().setText(getRelatedInsNumberField().getText().split(" , ")[0]);
            }
        });
        numberComp.prefWidthProperty().bind(getRelatedInsNumberField().widthProperty());

        AutoCompletionBinding<String> zipComp = TextFields.bindAutoCompletion(ltfZipCode.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return getInsuranceCatalog().findMatchesByInsZipCode(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        zipComp.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                String[] array = ltfZipCode.getText().split(" , ");
                setInsuranceCompany(getInsuranceCatalog().getByZipInsNameIdent(array[0], array[1], array[2], AbstractCpxCatalog.DEFAULT_COUNTRY));
            }
        });
        zipComp.prefWidthProperty().bind(ltfZipCode.widthProperty());

        AutoCompletionBinding<String> adressComp = TextFields.bindAutoCompletion(ltfAddress.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return getInsuranceCatalog().findMatchesByAddress(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        adressComp.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                String[] array = ltfAddress.getText().split(" , ");
                setInsuranceCompany(getInsuranceCatalog().getByAddressNameIdent(array[0], array[1], array[2], AbstractCpxCatalog.DEFAULT_COUNTRY));
            }
        });
        adressComp.prefWidthProperty().bind(ltfAddress.widthProperty());

        AutoCompletionBinding<String> cityComp = TextFields.bindAutoCompletion(ltfCity.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return getInsuranceCatalog().findMatchesByCity(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        cityComp.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                String[] array = ltfCity.getText().split(" , ");
                setInsuranceCompany(getInsuranceCatalog().getByCityInsNameIdent(array[0], array[1], array[2], AbstractCpxCatalog.DEFAULT_COUNTRY));
            }
        });
        cityComp.prefWidthProperty().bind(ltfAddress.widthProperty());

        AutoCompletionBinding<String> preFixComp = TextFields.bindAutoCompletion(ltfAreaCode.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return getInsuranceCatalog().findMatchesByPhonePrefix(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        preFixComp.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                String[] array = ltfAreaCode.getText().split(" , ");
                setInsuranceCompany(getInsuranceCatalog().getByPhonePrefixNameIdent(array[0], array[1], array[2], AbstractCpxCatalog.DEFAULT_COUNTRY));
            }
        });
        preFixComp.prefWidthProperty().bind(ltfAreaCode.widthProperty());

        AutoCompletionBinding<String> telefonComp = TextFields.bindAutoCompletion(ltfTelefon.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return getInsuranceCatalog().findMatchesByPhoneNo(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        telefonComp.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
//                String[] array = ltfTelefon.getText().split(" , ");
                setInsuranceCompany(getInsuranceCatalog().getByPhoneNo(ltfTelefon.getText(), AbstractCpxCatalog.DEFAULT_COUNTRY));
            }
        });
        telefonComp.prefWidthProperty().bind(ltfAreaCode.widthProperty());

        AutoCompletionBinding<String> faxComp = TextFields.bindAutoCompletion(ltfFax.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return getInsuranceCatalog().findMatchesByFaxNo(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        faxComp.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
//                String[] array = ltfTelefon.getText().split(" , ");
                setInsuranceCompany(getInsuranceCatalog().getByFaxNo(ltfFax.getText(), AbstractCpxCatalog.DEFAULT_COUNTRY));
            }
        });
        faxComp.prefWidthProperty().bind(ltfFax.widthProperty());
    }
    private ObjectProperty<CpxInsuranceCompany> insuranceCompanyProperty;

    public ObjectProperty<CpxInsuranceCompany> insuranceCompanyProperty() {
        if (insuranceCompanyProperty == null) {
            insuranceCompanyProperty = new SimpleObjectProperty<>();
        }
        return insuranceCompanyProperty;
    }
    
    public CpxInsuranceCompany getInsuranceCompany() {
        return insuranceCompanyProperty().get();
    }

    public void setInsuranceCompany(CpxInsuranceCompany pInsuranceCompany) {
        insuranceCompanyProperty().set(pInsuranceCompany);
    }
    
    public void updateCatalogValues(CpxInsuranceCompany newValue) {
        setText(newValue.getInscName());
        this.getRelatedInsNumberField().setText(newValue.getInscIdent() != null ? newValue.getInscIdent() : "");
        ltfZipCode.setText(newValue.getInscZipCode());
        ltfCity.setText(newValue.getInscCity());
        ltfAddress.setText(newValue.getInscAddress());
        ltfAreaCode.setText(newValue.getInscPhonePrefix());
        ltfTelefon.setText(newValue.getInscPhone());
        ltfFax.setText(newValue.getFaxNumber());
        setInsuranceCompany(newValue);

    }

    public void setUpdateListeners(String pInsIdent) {
        addPopupListener();
        if (pInsIdent != null) {

                updateCatalogValues(getInsuranceCatalog().getByCode(pInsIdent, AbstractCpxCatalog.DEFAULT_COUNTRY));
            
        }
        insuranceCompanyProperty().addListener(new ChangeListener<CpxInsuranceCompany>() {
            @Override
            public void changed(ObservableValue<? extends CpxInsuranceCompany> observable, CpxInsuranceCompany oldValue, CpxInsuranceCompany newValue) {
                if (newValue == null) {
                    return;
                }
                updateCatalogValues(newValue);
            }
        });
    }

    public void setupValidation() {
        getValidationSupport().registerValidator(this.getRelatedInsNumberField().getControl(), new Validator<String>() {
            @Override
            public ValidationResult apply(Control t, String u) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, "Bitte geben Sie die IKZ der Versicherung an!", u.isEmpty());
//                res.addErrorIf(t, "not valid", (getSkinnable().getRequest().getInsuranceIdentifier()!=null && getSkinnable().getRequest().getInsuranceIdentifier().isEmpty()));
                res.addErrorIf(t, "Bitte geben Sie eine korrekte IKZ ein!", !getInsuranceCatalog().hasEntry(u, AbstractCpxCatalog.DEFAULT_COUNTRY));
                return res;
            }
        });
    }
}
