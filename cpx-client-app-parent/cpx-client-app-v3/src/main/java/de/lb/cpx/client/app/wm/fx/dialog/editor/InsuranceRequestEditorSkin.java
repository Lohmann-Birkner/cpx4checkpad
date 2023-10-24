/*
 * Copyright (c) 2018 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.dialog.editor;

import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.menu.cache.MenuCacheOptionsEn;
import de.lb.cpx.client.core.model.fx.combobox.RequestStateCombobox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledCheckComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledDatePicker;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextArea;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.client.core.model.fx.labeled.LabeledToggleGroup;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmRequestInsurance;
import de.lb.cpx.wm.model.enums.WmAuditTypeEn;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import javafx.scene.control.RadioButton;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * Skinable Class to handle the layout
 *
 * @author wilde
 */
public class InsuranceRequestEditorSkin extends BasicRequestEditorSkin<InsuranceRequestEditor, TWmRequestInsurance> {

//    private Parent root;
    private InsuranceInfoTextField ltfInsName;
    private LabeledTextField ltfInsNumber;
    private LabeledTextField ltfInsEmployee;
    private LabeledComboBox<InsuranceType> lcbPrivateInsured;
    private LabeledDatePicker ldpAuditStart;
//    private LabeledComboBox<AuditTypeEn> lcbAuditType;
    private LabeledDatePicker ldpStartReport;
    private RequestStateCombobox lcbRequestState;
    private LabeledTextField ltfRequestResult;
    private LabeledTextArea ltaComment;
//    private Label lblInfoHeader;
//    private LabeledTextField ltfZipCode;
//    private LabeledTextField ltfCity;
//    private LabeledTextField ltfAddress;
//    private LabeledTextField ltfAreaCode;
//    private LabeledTextField ltfTelefon;
//    private LabeledTextField ltfFax;
//    private Button btnInsSearch;
//
//    private static final String[] REQUEST_RESULTS = new String[]{"Offen", "Ja", "Nein"};
//    private static final String[] INSURANCE_TYPE = new String[] {"Gesetzlich","Privat"};
    private LabeledToggleGroup<RadioButton, WmAuditTypeEn> ltgAuditType;
    private LabeledCheckComboBox<Map.Entry<Long, String>> chkcbAuditReasons;

    public InsuranceRequestEditorSkin(InsuranceRequestEditor pSkinnable) throws IOException {
        super(pSkinnable, "/fxml/InsuranceRequestEditorFXML.fxml");
//        loadRoot();
//        setUpLanguage();
//        getSkinnable().validationSupportProperty().addListener(new ChangeListener<ValidationSupport>() {
//            @Override
//            public void changed(ObservableValue<? extends ValidationSupport> observable, ValidationSupport oldValue, ValidationSupport newValue) {
//                setUpValidation();
//            }
//        });
//        pSkinnable.getProperties().addListener(new MapChangeListener<Object, Object>() {
//            @Override
//            public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
//                if(change.wasAdded()){
//                    if(InsuranceRequestEditor.UPDATE_REQUEST_DATA.equals(change.getKey())){
//                        updateRequestData();
//                        pSkinnable.getProperties().remove(InsuranceRequestEditor.UPDATE_REQUEST_DATA);
//                    }
//                }
//            }
//        });
//        setUpValidation();
//        setUpAutoCompletion();
//        setUpdateListeners();
//        if(getSkinnable().getRequest().getId() != 0L){
//            updateRequestValues(getSkinnable().getRequest());
//            updateCatalogValues(getSkinnable().getInsuranceCatalog().getByCode(getSkinnable().getRequest().getInsuranceIdentifier(), AbstractCpxCatalog.DEFAULT_COUNTRY));
//        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected final void setUpNodes() {
        ltfInsName = (InsuranceInfoTextField) lookUpInRoot("#ltfInsName");
        ltfInsNumber = (LabeledTextField) lookUpInRoot("#ltfInsNumber");
        ltfInsEmployee = (LabeledTextField) lookUpInRoot("#ltfInsEmployee");
        lcbPrivateInsured = (LabeledComboBox) lookUpInRoot("#lcbPrivateInsured");
        ldpAuditStart = (LabeledDatePicker) lookUpInRoot("#ldpAuditStart");
        ltgAuditType = (LabeledToggleGroup<RadioButton, WmAuditTypeEn>) lookUpInRoot("#ltgAuditType");
        ldpStartReport = (LabeledDatePicker) lookUpInRoot("#ldpStartReport");
        lcbRequestState = (RequestStateCombobox) lookUpInRoot("#lcbRequestState");
        ltfRequestResult = (LabeledTextField) lookUpInRoot("#ltfRequestResult");
        chkcbAuditReasons = (LabeledCheckComboBox) lookUpInRoot("#chkcbAuditReasons");
        ltaComment = (LabeledTextArea) lookUpInRoot("#ltaComment");
        addTextTemplateController(ltaComment);
        ltfInsName.setInsuranceCatalog(getSkinnable().getInsuranceCatalog());  
        ltfInsName.setRelatedInsNumberField(ltfInsNumber);
//        lblInfoHeader = (Label) lookUpInRoot("#lblInfoHeader");
//        ltfZipCode = new LabeledTextField();
//        ltfZipCode.setSpacing(5.0);
//        ltfCity = new LabeledTextField();
//        ltfCity.setSpacing(5.0);
//        ltfAddress = new LabeledTextField();
//        ltfAddress.setSpacing(5.0);
//        ltfAreaCode = new LabeledTextField();
//        ltfAreaCode.setSpacing(5.0);
//        ltfTelefon = new LabeledTextField();
//        ltfTelefon.setSpacing(5.0);
//        ltfFax = new LabeledTextField();
//        ltfFax.setSpacing(5.0);
//        btnInsSearch = new Button();//(Button) lookUpInRoot("#btnInsSearch");
//        ltfInsName.setAdditionalButton(btnInsSearch);
        lcbRequestState.setItems(MenuCache.getMenuCacheRequestStates().values(new Date(), MenuCacheOptionsEn.IGNORE_INACTIVE));
        
        Set<Map.Entry<Long, String>> list = MenuCache.instance().getAuditReasonsEntries(getSkinnable().getCase()!=null?getSkinnable().getCase().getCsCaseTypeEn():null);
        chkcbAuditReasons.getItems().addAll(new ArrayList<>(list));
        chkcbAuditReasons.registerTooltip();
        chkcbAuditReasons.setConverter(new StringConverter<Map.Entry<Long, String>>() {
            @Override
            public String toString(Map.Entry<Long, String> object) {
                return object == null ? "" : object.getValue();
            }

            @Override
            public Map.Entry<Long, String> fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    @Override
    protected final void setUpLanguage() {
        ltfInsName.setTitle(Lang.getInsuranceName());
//        ltfInsName.getTextProperty().addListener(new ClearAutoCompleteFieldsListener());
        ltfInsName.applyFontWeightToTitle(FontWeight.BOLD);
        ltfInsNumber.setTitle(Lang.getInsuranceIdent());
        ltfInsNumber.applyFontWeightToTitle(FontWeight.BOLD);
//        ltfInsNumber.getTextProperty().addListener(new ClearAutoCompleteFieldsListener());
        ltfInsName.setRelatedInsNumberField(ltfInsNumber);
        ltfInsEmployee.setTitle(Lang.getAuditEditor());
        lcbPrivateInsured.setTitle("Art der Versicherung");
        lcbPrivateInsured.getItems().addAll(InsuranceType.values());
        lcbPrivateInsured.getControl().getSelectionModel().selectFirst();
        ldpAuditStart.setTitle(Lang.getAuditProcessCreationDate());
        ltgAuditType.setTitle("Prüfart");
        ltgAuditType.setToggleFactory(new Callback<String, RadioButton>() {
            @Override
            public RadioButton call(String param) {
                return new RadioButton(param);
            }
        });
        ltgAuditType.setValues(WmAuditTypeEn.values());
        ltgAuditType.select(WmAuditTypeEn.SINGLE_AUDIT);
        ldpStartReport.setTitle(Lang.getMdkReportCreationDate());
        lcbRequestState.setTitle(Lang.getAuditStatus());
//        lcbRequestState.getItems().addAll(REQUEST_RESULTS);
        ltfRequestResult.setTitle(Lang.getResult());
        ltaComment.setTitle(Lang.getComment());
//        lblInfoHeader.setText("Versicherungsinformationen");

//        ltfZipCode.setTitle(Lang.getAddressZipCode());
//        ltfZipCode.applyFontWeightToTitle(FontWeight.BOLD);
//        ltfZipCode.getTextProperty().addListener(new ClearAutoCompleteFieldsListener());
//        ltfCity.setTitle(Lang.getAddressCity());
//        ltfCity.applyFontWeightToTitle(FontWeight.BOLD);
//        ltfCity.getTextProperty().addListener(new ClearAutoCompleteFieldsListener());
//        ltfAddress.setTitle(Lang.getAddressTypePostal());
//        ltfAddress.applyFontWeightToTitle(FontWeight.BOLD);
//        ltfAddress.getTextProperty().addListener(new ClearAutoCompleteFieldsListener());
//        ltfAddress.setPrefWidth(400);
//        ltfAreaCode.setTitle(Lang.getAuditTelephoneAreaCode());
//        ltfAreaCode.applyFontWeightToTitle(FontWeight.BOLD);
//        ltfAreaCode.getTextProperty().addListener(new ClearAutoCompleteFieldsListener());
//        ltfTelefon.setTitle(Lang.getAddressPhoneNumber());
//        ltfTelefon.applyFontWeightToTitle(FontWeight.BOLD);
//        ltfTelefon.getTextProperty().addListener(new ClearAutoCompleteFieldsListener());
//        ltfFax.setTitle(Lang.getFax());
//        ltfFax.applyFontWeightToTitle(FontWeight.BOLD);
//        ltfFax.getTextProperty().addListener(new ClearAutoCompleteFieldsListener());
//        btnInsSearch.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.SEARCH_PLUS));
//        btnInsSearch.getStyleClass().add("cpx-icon-button");
//        btnInsSearch.setText("");
//        btnInsSearch.setTooltip(new Tooltip("Versicherungsinformationen"));
        chkcbAuditReasons.setTitle(Lang.getMdkAuditReasons());
        
        ldpAuditStart.setDate(new Date());
    }

    @Override
    public void setUpdateListeners() {
        super.setUpdateListeners();
    
        ltfInsName.setUpdateListeners((getSkinnable().getRequest() == null||getSkinnable().getRequest().getId() == 0L)?null:getSkinnable().getRequest().getInsuranceIdentifier());
//        if (getSkinnable().getRequest() != null) {
//            if (getSkinnable().getRequest().getId() != 0L) {
//                updateCatalogValues(getSkinnable().getInsuranceCatalog().getByCode(getSkinnable().getRequest().getInsuranceIdentifier(), AbstractCpxCatalog.DEFAULT_COUNTRY));
//            }
//        }
//        getSkinnable().insuranceCompanyProperty().addListener(new ChangeListener<CpxInsuranceCompany>() {
//            @Override
//            public void changed(ObservableValue<? extends CpxInsuranceCompany> observable, CpxInsuranceCompany oldValue, CpxInsuranceCompany newValue) {
//                if (newValue == null) {
//                    return;
//                }
//                updateCatalogValues(newValue);
//            }
//        });
//        btnInsSearch.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                VBox vBox = new VBox(8.0d);
//                vBox.getChildren().addAll(ltfAddress, ltfZipCode, ltfCity, ltfAreaCode, ltfTelefon, ltfFax);
//                PopOver popover = showInfoPopover(vBox);
//                popover.setOnShowing(new EventHandler<WindowEvent>() {
//                    @Override
//                    public void handle(WindowEvent t) {
//                        ltfInsName.setShowCaret(false);
//                    }
//                });
//                popover.setOnHiding(new EventHandler<WindowEvent>() {
//                    @Override
//                    public void handle(WindowEvent t) {
//                        ltfInsName.setShowCaret(true);
//                    }
//                });
//                popover.show(btnInsSearch);
////                showMdkInfoPopover();
//            }
//
//        });
    }

    @Override
    protected void setUpAutoCompletion() {
        ltfInsName.setUpAutoCompletion();
//        AutoCompletionBinding<String> nameComp = TextFields.bindAutoCompletion(ltfInsName.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
//            @Override
//            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
////                return getSkinnable().getInsuranceCatalog().findMatchesByInsuranceName(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
//                return getSkinnable().getInsuranceCatalog().findMatchesByInsName(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
//            }
//        });
//        nameComp.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
//            @Override
//            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
//                String[] split = ltfInsName.getText().split(" , ");
//                //risky call .. if someone changes this shit, we are screwed on runtime
////                if (split.length == 3) {
////                getSkinnable().setInsuranceCompany(getSkinnable().getInsuranceCatalog().getByCityInsNameIdent(split[2], split[0], split[1], AbstractCpxCatalog.DEFAULT_COUNTRY));
//////                getSkinnable().setInsuranceCompany(getSkinnable().getInsuranceCatalog().getByName(ltfInsName.getText().split(" , ")[0], AbstractCpxCatalog.DEFAULT_COUNTRY));
////                }
////                else 
////Fix Exception wenn City is null 
//                if (split[1] != null) {
//                    getSkinnable().setInsuranceCompany(getSkinnable().getInsuranceCatalog().getByIdent(split[1], AbstractCpxCatalog.DEFAULT_COUNTRY));
//                    ltfInsName.setText(split[0]);
//                }
//            }
//        });
//        nameComp.prefWidthProperty().bind(ltfInsName.widthProperty());
//
//        AutoCompletionBinding<String> numberComp = TextFields.bindAutoCompletion(ltfInsNumber.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
//            @Override
//            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
//                return getSkinnable().getInsuranceCatalog().findMatchesByInsuranceNumber(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
//            }
//        });
//        numberComp.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
//            @Override
//            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
//                getSkinnable().setInsuranceCompany(getSkinnable().getInsuranceCatalog().getByCode(ltfInsNumber.getText().split(" , ")[0], AbstractCpxCatalog.DEFAULT_COUNTRY));
//                ltfInsNumber.setText(ltfInsNumber.getText().split(" , ")[0]);
//            }
//        });
//        numberComp.prefWidthProperty().bind(ltfInsNumber.widthProperty());
//
//        AutoCompletionBinding<String> zipComp = TextFields.bindAutoCompletion(ltfZipCode.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
//            @Override
//            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
//                return getSkinnable().getInsuranceCatalog().findMatchesByInsZipCode(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
//            }
//        });
//        zipComp.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
//            @Override
//            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
//                String[] array = ltfZipCode.getText().split(" , ");
//                getSkinnable().setInsuranceCompany(getSkinnable().getInsuranceCatalog().getByZipInsNameIdent(array[0], array[1], array[2], AbstractCpxCatalog.DEFAULT_COUNTRY));
//            }
//        });
//        zipComp.prefWidthProperty().bind(ltfZipCode.widthProperty());
//
//        AutoCompletionBinding<String> adressComp = TextFields.bindAutoCompletion(ltfAddress.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
//            @Override
//            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
//                return getSkinnable().getInsuranceCatalog().findMatchesByAddress(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
//            }
//        });
//        adressComp.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
//            @Override
//            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
//                String[] array = ltfAddress.getText().split(" , ");
//                getSkinnable().setInsuranceCompany(getSkinnable().getInsuranceCatalog().getByAddressNameIdent(array[0], array[1], array[2], AbstractCpxCatalog.DEFAULT_COUNTRY));
//            }
//        });
//        adressComp.prefWidthProperty().bind(ltfAddress.widthProperty());
//
//        AutoCompletionBinding<String> cityComp = TextFields.bindAutoCompletion(ltfCity.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
//            @Override
//            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
//                return getSkinnable().getInsuranceCatalog().findMatchesByCity(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
//            }
//        });
//        cityComp.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
//            @Override
//            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
//                String[] array = ltfCity.getText().split(" , ");
//                getSkinnable().setInsuranceCompany(getSkinnable().getInsuranceCatalog().getByCityInsNameIdent(array[0], array[1], array[2], AbstractCpxCatalog.DEFAULT_COUNTRY));
//            }
//        });
//        cityComp.prefWidthProperty().bind(ltfAddress.widthProperty());
//
//        AutoCompletionBinding<String> preFixComp = TextFields.bindAutoCompletion(ltfAreaCode.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
//            @Override
//            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
//                return getSkinnable().getInsuranceCatalog().findMatchesByPhonePrefix(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
//            }
//        });
//        preFixComp.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
//            @Override
//            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
//                String[] array = ltfAreaCode.getText().split(" , ");
//                getSkinnable().setInsuranceCompany(getSkinnable().getInsuranceCatalog().getByPhonePrefixNameIdent(array[0], array[1], array[2], AbstractCpxCatalog.DEFAULT_COUNTRY));
//            }
//        });
//        preFixComp.prefWidthProperty().bind(ltfAreaCode.widthProperty());
//
//        AutoCompletionBinding<String> telefonComp = TextFields.bindAutoCompletion(ltfTelefon.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
//            @Override
//            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
//                return getSkinnable().getInsuranceCatalog().findMatchesByPhoneNo(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
//            }
//        });
//        telefonComp.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
//            @Override
//            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
////                String[] array = ltfTelefon.getText().split(" , ");
//                getSkinnable().setInsuranceCompany(getSkinnable().getInsuranceCatalog().getByPhoneNo(ltfTelefon.getText(), AbstractCpxCatalog.DEFAULT_COUNTRY));
//            }
//        });
//        telefonComp.prefWidthProperty().bind(ltfAreaCode.widthProperty());
//
//        AutoCompletionBinding<String> faxComp = TextFields.bindAutoCompletion(ltfFax.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
//            @Override
//            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
//                return getSkinnable().getInsuranceCatalog().findMatchesByFaxNo(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
//            }
//        });
//        faxComp.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
//            @Override
//            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
////                String[] array = ltfTelefon.getText().split(" , ");
//                getSkinnable().setInsuranceCompany(getSkinnable().getInsuranceCatalog().getByFaxNo(ltfFax.getText(), AbstractCpxCatalog.DEFAULT_COUNTRY));
//            }
//        });
//        faxComp.prefWidthProperty().bind(ltfFax.widthProperty());
    }

    @Override
    protected void setUpValidation() {
//        getSkinnable().getValidationSupport().registerValidator(ltfInsNumber.getControl(), new Validator<String>() {
//            @Override
//            public ValidationResult apply(Control t, String u) {
//                ValidationResult res = new ValidationResult();
//                res.addErrorIf(t, "Bitte geben Sie die IKZ der Versicherung an!", u.isEmpty());
////                res.addErrorIf(t, "not valid", (getSkinnable().getRequest().getInsuranceIdentifier()!=null && getSkinnable().getRequest().getInsuranceIdentifier().isEmpty()));
//                res.addErrorIf(t, "Bitte geben Sie eine korrekte IKZ ein!", !getSkinnable().getInsuranceCatalog().hasEntry(u, AbstractCpxCatalog.DEFAULT_COUNTRY));
//                return res;
//            }
//        });
        ltfInsName.setValidationSupport(getSkinnable().getValidationSupport());
        ltfInsName.setupValidation();

//            getSkinnable().getValidationSupport().registerValidator(lcbAuditType.getControl(), new Validator<AuditTypeEn>() {
//                @Override
//                public ValidationResult apply(Control t, AuditTypeEn u) {
//                    ValidationResult res = new ValidationResult();
//                    res.addErrorIf(t, "Bitte wählen Sie eine Prüfart aus!", u == null);
//                    return res;
//                }
//            });

    }

    @Override
    protected void updateRequestValues(TWmRequestInsurance pRequest) {
        ltfInsNumber.setText(pRequest.getInsuranceIdentifier() != null ? pRequest.getInsuranceIdentifier() : "");
        ltfInsEmployee.setText(pRequest.getEditor());
        lcbPrivateInsured.getControl().getSelectionModel().select(pRequest.isPublicInsured() ? InsuranceType.PUBLIC : InsuranceType.PRIVATE);
        ldpAuditStart.setDate(pRequest.getStartAudit());
        ltgAuditType.select(pRequest.getAuditType());
        ldpStartReport.setDate(pRequest.getReportDate());
        lcbRequestState.selectByInternalId(pRequest.getRequestState());
        ltfRequestResult.setText(pRequest.getResultComment());
        ltaComment.setText(pRequest.getComment());

        updateAuditReasonsData(chkcbAuditReasons, pRequest);
    }

//    private void updateCatalogValues(CpxInsuranceCompany newValue) {
//        ltfInsName.updateCatalogValues(newValue);
////        ltfInsName.setText(newValue.getInscName());
////        ltfInsNumber.setText(newValue.getInscIdent() != null ? newValue.getInscIdent() : "");
////        ltfZipCode.setText(newValue.getInscZipCode());
////        ltfCity.setText(newValue.getInscCity());
////        ltfAddress.setText(newValue.getInscAddress());
////        ltfAreaCode.setText(newValue.getInscPhonePrefix());
////        ltfTelefon.setText(newValue.getInscPhone());
////        ltfFax.setText(newValue.getFaxNumber());
//        getSkinnable().setInsuranceCompany(newValue);
//        getSkinnable().getCatalogValidationResult().invalidate();
//    }

    @Override
    protected void updateRequestData() {
        getSkinnable().getRequest().setAuditType(ltgAuditType.getSelected());
        getSkinnable().getRequest().setPublicInsured((lcbPrivateInsured.getSelectedItem() == InsuranceType.PUBLIC));
        Long internalId = lcbRequestState.getSelectedInternalId();
        getSkinnable().getRequest().setRequestState(internalId == null ? 0L : internalId);
        getSkinnable().getRequest().setStartAudit(ldpAuditStart.getDate());
        getSkinnable().getRequest().setReportDate(ldpStartReport.getDate());
        getSkinnable().getRequest().setComment(ltaComment.getText());
        getSkinnable().getRequest().setEditor(ltfInsEmployee.getText());
        getSkinnable().getRequest().setInsuranceIdentifier(ltfInsNumber.getText());
        getSkinnable().getRequest().setResultComment(ltfRequestResult.getText());
        updateAuditReasons(chkcbAuditReasons, getSkinnable().getRequest());

        getSkinnable().getCatalogValidationResult().invalidate();
    }

    @Override
    protected void disableControls(boolean armed) {
        ltfInsName.disableControls(armed); 
        ltfInsNumber.setDisable(armed);
        ltfInsEmployee.setDisable(armed);
        lcbPrivateInsured.setDisable(armed);
        ldpAuditStart.setDisable(armed);
        ldpStartReport.setDisable(armed);
        lcbRequestState.setDisable(armed);
        ltfRequestResult.setDisable(armed);
        ltaComment.setDisable(armed);
//        ltfZipCode.setDisable(armed);
//        ltfCity.setDisable(armed);
//        ltfAddress.setDisable(armed);
//        ltfAreaCode.setDisable(armed);
//        ltfTelefon.setDisable(armed);
//        ltfFax.setDisable(armed);
//        btnInsSearch.setDisable(armed);
        ltgAuditType.setDisable(armed);
        chkcbAuditReasons.setDisable(armed);
    }

//    public CpxInsuranceCompany getInsuranceCompany() {
//        return ltfInsName.getInsuranceCompany();
//    }
//
//    private class ClearAutoCompleteFieldsListener implements ChangeListener<String> {
//
//        @Override
//        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//            if (newValue != null && newValue.isEmpty()) {
//                ltfInsName.getControl().clear();
//                ltfInsNumber.getControl().clear();
//                ltfZipCode.getControl().clear();
//                ltfCity.getControl().clear();
//                ltfAddress.getControl().clear();
//                ltfAreaCode.getControl().clear();
//                ltfTelefon.getControl().clear();
//                ltfFax.getControl().clear();
//            }
//        }
//    }

    private enum InsuranceType {
        PUBLIC("Gesetzlich"), PRIVATE("Privat");
        private final String description;

        private InsuranceType(String pDescription) {
            description = pDescription;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return description;
        }
    }
}
