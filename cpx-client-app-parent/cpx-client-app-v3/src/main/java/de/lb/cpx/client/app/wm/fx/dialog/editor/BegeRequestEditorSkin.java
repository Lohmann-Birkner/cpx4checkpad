/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.dialog.editor;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.menu.cache.MenuCacheOptionsEn;
import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompany;
import de.lb.cpx.client.core.model.fx.combobox.RequestStateCombobox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledCheckComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledDatePicker;
import de.lb.cpx.client.core.model.fx.labeled.LabeledLabel;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextArea;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmRequestBege;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

/**
 *
 * @author wilde
 */
public class BegeRequestEditorSkin extends BasicRequestEditorSkin<BegeRequestEditor, TWmRequestBege>{

    private static final Logger LOG = Logger.getLogger(BegeRequestEditorSkin.class.getName());
    private LabeledTextField tfInsuranceName;
    private LabeledTextField tfInsuranceCity;
    private LabeledTextField tfInsuranceIdent;
    private LabeledTextField tfInsuranceZipCode;
    private Label labelAddData;
    private LabeledLabel labelAddress;
    private LabeledLabel labelFax;
    private LabeledLabel labelTelephoneNumber;
    private LabeledTextField tfContactPerson;
    private LabeledTextField tfExtensionNo;
    private LabeledTextField tfFaxNo;
    private LabeledTextArea taComment;
    private LabeledDatePicker ldpAuditStart;
    private LabeledDatePicker ldpStartReport;
    private RequestStateCombobox lcbRequestState;
    private LabeledCheckComboBox<Map.Entry<Long, String>> lcbAuditReasons;
    private Label labelRequestData;
    
    public BegeRequestEditorSkin(BegeRequestEditor pSkinnable) throws IOException {
        super(pSkinnable, "/fxml/BegeRequestEditorFXML.fxml");
    }

    @Override
    protected void setUpNodes() {
        tfInsuranceName = (LabeledTextField) lookUpInRoot("#tfInsuranceName");//new LabeledTextField(Lang.getInsuranceAssociationName());
        tfInsuranceName.setStyle(FONT_WEIGHT_BOLD);
        tfInsuranceName.getControl().setStyle(FONT_WEIGHT_NORMAL);
        tfInsuranceName.getControl().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (tfInsuranceName.getControl().getText() != null && tfInsuranceName.getControl().getText().isEmpty()) {
                    tfInsuranceIdent.getControl().clear();
                    tfInsuranceZipCode.getControl().clear();
                    tfInsuranceCity.getControl().clear();
                    tfExtensionNo.getControl().clear();
                    tfFaxNo.getControl().clear();

                    labelAddress.getControl().setText("");
                    labelFax.getControl().setText("");
                    labelTelephoneNumber.getControl().setText("");
                }
            }
        });
        tfInsuranceName.getControl().focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                String text = Objects.requireNonNullElse(tfInsuranceName.getText(), "");
                if (!text.isEmpty()) {
                    if (getSkinnable().getInsuranceCatalog().getByName(text, AbstractCpxCatalog.DEFAULT_COUNTRY).getInscName() == null) {
                        String error = "Ungültiger BG-Name. Bitte geben Sie einen gültigen Eintrag an.";
                        LOG.log(Level.WARNING, error);
//                        MainApp.showErrorMessageDialog(error);
                    }
                }
            }
        });

        tfInsuranceCity = (LabeledTextField) lookUpInRoot("#tfInsuranceCity");//new LabeledTextField(Lang.getAddressCity());
        tfInsuranceCity.setStyle(FONT_WEIGHT_BOLD);
        tfInsuranceCity.getControl().setStyle(FONT_WEIGHT_NORMAL);
        tfInsuranceCity.getControl().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (tfInsuranceCity.getControl().getText() != null && tfInsuranceCity.getControl().getText().isEmpty()) {
                    tfInsuranceIdent.getControl().clear();
                    tfInsuranceZipCode.getControl().clear();
                    tfInsuranceName.getControl().clear();
                    tfExtensionNo.getControl().clear();
                    tfFaxNo.getControl().clear();
                }
            }
        });
        tfInsuranceCity.getControl().focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                String text = Objects.requireNonNullElse(tfInsuranceCity.getText(), "");
                if (!text.isEmpty()) {
                    if (getSkinnable().getInsuranceCatalog().getByCity(text, AbstractCpxCatalog.DEFAULT_COUNTRY).getInscCity() == null) {
                        String error = "Ungültiger Stadt. Bitte geben Sie einen gültigen Eintrag an.";
                        LOG.log(Level.WARNING, error);
//                        MainApp.showErrorMessageDialog(error);
                    }
                }
            }
        });

        tfInsuranceIdent = (LabeledTextField) lookUpInRoot("#tfInsuranceIdent");//new LabeledTextField(Lang.getIdentNumber());
        tfInsuranceIdent.setStyle(FONT_WEIGHT_BOLD);
        tfInsuranceIdent.getControl().setStyle(FONT_WEIGHT_NORMAL);
        tfInsuranceIdent.getControl().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (tfInsuranceIdent.getControl().getText() != null && tfInsuranceIdent.getControl().getText().isEmpty()) {
                    tfInsuranceCity.getControl().clear();
                    tfInsuranceZipCode.getControl().clear();
                    tfInsuranceName.getControl().clear();
                    tfExtensionNo.getControl().clear();
                    tfFaxNo.getControl().clear();
                }
            }
        });
        tfInsuranceIdent.getControl().focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                String text = Objects.requireNonNullElse(tfInsuranceIdent.getText(), "");
                if (!text.isEmpty()) {
                    if (getSkinnable().getInsuranceCatalog().getByIdent(text, AbstractCpxCatalog.DEFAULT_COUNTRY).getInscIdent() == null) {
                        String error = "Ungültiger IK Nummer. Bitte geben Sie einen gültigen Eintrag an.";
                        LOG.log(Level.WARNING, error);
//                        MainApp.showErrorMessageDialog(error);
                    }
                }
            }
        });

        tfInsuranceZipCode = (LabeledTextField) lookUpInRoot("#tfInsuranceZipCode");//new LabeledTextField(Lang.getAddressZipCode());
        tfInsuranceZipCode.setStyle(FONT_WEIGHT_BOLD);
        tfInsuranceZipCode.getControl().setStyle(FONT_WEIGHT_NORMAL);
        tfInsuranceZipCode.getControl().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (tfInsuranceZipCode.getControl().getText() != null && tfInsuranceZipCode.getControl().getText().isEmpty()) {
                    tfInsuranceCity.getControl().clear();
                    tfInsuranceIdent.getControl().clear();
                    tfInsuranceName.getControl().clear();
                    tfExtensionNo.getControl().clear();
                    tfFaxNo.getControl().clear();
                }
            }
        });
        tfInsuranceZipCode.getControl().focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                String text = Objects.requireNonNullElse(tfInsuranceZipCode.getText(), "");
                if (!text.isEmpty()) {
                    if (getSkinnable().getInsuranceCatalog().getByZip(text, AbstractCpxCatalog.DEFAULT_COUNTRY).getInscZipCode() == null) {
                        String error = "Ungültiger Postleitzahl. Bitte geben Sie einen gültigen Eintrag an.";
                        LOG.log(Level.WARNING, error);
//                        MainApp.showErrorMessageDialog(error);
                    }
                }
            }
        });

//        labelInsurance = new Label(Lang.getInsuranceAssociation() + " " + requestType.toString());
        labelAddData = (Label) lookUpInRoot("#labelAddData");//new Label(Lang.getAdditionalData());
        labelAddData.setStyle(FONT_WEIGHT_BOLD);

        labelAddress = (LabeledLabel) lookUpInRoot("#labelAddress");//new LabeledLabel(Lang.getAddress());
        labelFax = (LabeledLabel) lookUpInRoot("#labelFax");//new LabeledLabel(Lang.getFax());
        labelTelephoneNumber = (LabeledLabel) lookUpInRoot("#labelTelephoneNumber");// new LabeledLabel(Lang.getAddressPhoneNumber());

        HBox hbox = new HBox();
        hbox.setSpacing(12.0);
        HBox.setHgrow(hbox, Priority.ALWAYS);
        tfContactPerson = (LabeledTextField) lookUpInRoot("#tfContactPerson");//new LabeledTextField(Lang.getBegeEditor(), 50);   // OR new LabeledTextField(Lang.getBegeEditor(), 50, false);
        tfExtensionNo = (LabeledTextField) lookUpInRoot("#tfExtensionNo");//new LabeledTextField(Lang.getBegeDirectPhoneNo(), 20);

        tfFaxNo = (LabeledTextField) lookUpInRoot("#tfFaxNo");// new LabeledTextField(Lang.getBegeDirectFax(), 20);
        
        labelRequestData = (Label) lookUpInRoot("#labelRequestData");
        labelRequestData.setStyle(FONT_WEIGHT_BOLD);
        
        ldpAuditStart = (LabeledDatePicker) lookUpInRoot("#ldpAuditStart");
        //set some kidn of default date
        ldpAuditStart.setDate(new Date());
        ldpStartReport = (LabeledDatePicker) lookUpInRoot("#ldpStartReport");
        lcbRequestState = (RequestStateCombobox) lookUpInRoot("#lcbRequestState");
        lcbRequestState.setItems(MenuCache.getMenuCacheRequestStates().values(new Date(), MenuCacheOptionsEn.IGNORE_INACTIVE));
        lcbAuditReasons = (LabeledCheckComboBox) lookUpInRoot("#lcbAuditReasons");
        Set<Map.Entry<Long, String>> list = MenuCache.instance().getAuditReasonsEntries(getSkinnable().getCase()!=null?getSkinnable().getCase().getCsCaseTypeEn():null); 
        lcbAuditReasons.getItems().addAll(new ArrayList<>(list));
        lcbAuditReasons.getControl().setConverter(new StringConverter<Map.Entry<Long, String>>() {
            @Override
            public String toString(Map.Entry<Long, String> t) {
                return t == null ? "" : t.getValue();
            }

            @Override
            public Map.Entry<Long, String> fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        lcbAuditReasons.registerTooltip();
        
        taComment = (LabeledTextArea) lookUpInRoot("#taComment");
        addTextTemplateController(taComment);
    }
    
    
    @Override
    protected void setUpLanguage() {
        tfInsuranceName.setTitle(Lang.getInsuranceAssociationName());
        tfInsuranceCity.setTitle(Lang.getAddressCity());
        tfInsuranceIdent.setTitle(Lang.getIdentNumber());
        tfInsuranceZipCode.setTitle(Lang.getAddressZipCode());
        labelAddData.setText(Lang.getAdditionalData());
        labelRequestData.setText("Anfrageeigenschaften");
        labelAddress.setTitle(Lang.getAddress());
        labelFax.setTitle(Lang.getFax());
        labelTelephoneNumber.setTitle(Lang.getAddressPhoneNumber());
        tfContactPerson.setTitle(Lang.getBegeEditor()); 
        tfExtensionNo.setTitle(Lang.getBegeDirectPhoneNo());
        tfFaxNo.setTitle(Lang.getBegeDirectFax());
        ldpAuditStart.setTitle(Lang.getAuditProcessCreationDate());
        ldpStartReport.setTitle(Lang.getMdkReportCreationDate());
        lcbRequestState.setTitle(Lang.getAuditStatus());
        lcbAuditReasons.setTitle(Lang.getMdkAuditReasons());
        taComment.setTitle(Lang.getComment());
    }

    @Override
    protected void setUpValidation() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ValidationSupport validationSupport = getSkinnable().getValidationSupport();
                if (validationSupport != null) {
                    validationSupport.registerValidator(tfInsuranceIdent.getControl(), new Validator<String>() {
                        @Override
                        public ValidationResult apply(Control t, String u) {
                            ValidationResult res = new ValidationResult();
                            if (u == null || u.isEmpty()) {
                                res.addErrorIf(t, "Bitte geben Sie die IK-Nummer ein!", (u == null || u.isEmpty()));
                            } else if (!u.isEmpty()) {
                                res.addErrorIf(t, Lang.getValidationErrorInvalidInsuranceIdent(), (!getSkinnable().getInsuranceCatalog().hasEntry(u, AbstractCpxCatalog.DEFAULT_COUNTRY)));
                            }
                            validationSupport.setErrorDecorationEnabled(true);
                            return res;
                        }
                    });

                    validationSupport.registerValidator(tfInsuranceName.getControl(), new Validator<String>() {
                        @Override
                        public ValidationResult apply(Control t, String u) {
                            ValidationResult res = new ValidationResult();
                            if (u == null || u.isEmpty()) {
                                res.addErrorIf(t, "Bitte geben Sie den BG-Namen ein!", (u == null || u.isEmpty()));
                            } else if (!u.isEmpty()) {
                                res.addErrorIf(t, "BG Name ist nicht korrekt!", (!getSkinnable().getInsuranceCatalog().hasEntryOfInsName(u, AbstractCpxCatalog.DEFAULT_COUNTRY)));
                            }
                            validationSupport.setErrorDecorationEnabled(true);
                            return res;
                        }
                    });
                    validationSupport.registerValidator(tfInsuranceCity.getControl(), false, new Validator<String>() {
                        @Override
                        public ValidationResult apply(Control t, String u) {
                            ValidationResult res = new ValidationResult();
                            if (u != null && !u.isEmpty()) {
                                res.addErrorIf(t, "BG Stadt ist nicht korrekt!", !getSkinnable().getInsuranceCatalog().hasEntryOfInsCity(u, AbstractCpxCatalog.DEFAULT_COUNTRY));
//                                res.addWarningIf(t, "BG Stadt ist nicht korrekt!", !insuranceCatalog.hasEntryOfInsCity(u, AbstractCpxCatalog.DEFAULT_COUNTRY));
                            }
                            validationSupport.setErrorDecorationEnabled(true);
                            return res;
                        }
                    });

                    validationSupport.registerValidator(tfInsuranceZipCode.getControl(), false, new Validator<String>() {
                        @Override
                        public ValidationResult apply(Control t, String u) {
                            ValidationResult res = new ValidationResult();
                            res.addErrorIf(t, "BG Postleitzahl ist nicht korrekt!", (u != null && !u.isEmpty()) && !getSkinnable().getInsuranceCatalog().hasEntryOfInsZipCode(u, AbstractCpxCatalog.DEFAULT_COUNTRY));
                            validationSupport.setErrorDecorationEnabled(true);
                            return res;
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void setUpAutoCompletion() {
        AutoCompletionBinding<String> cityComp = TextFields.bindAutoCompletion(tfInsuranceCity.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return getSkinnable().getInsuranceCatalog().findMatchesByInsuranceCity(param.getUserText(), 12, AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        cityComp.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                if (tfInsuranceCity.getText().contains("-")) {
                    String[] split = tfInsuranceCity.getText().split(" - ");
                    setInsuranceDataForBege(getSkinnable().getInsuranceCatalog().getByName(split[1], AbstractCpxCatalog.DEFAULT_COUNTRY));
                }
            }
        });
        cityComp.prefWidthProperty().bind(tfInsuranceCity.widthProperty());

        AutoCompletionBinding<String> nameComp = TextFields.bindAutoCompletion(tfInsuranceName.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return getSkinnable().getInsuranceCatalog().findMatchesByInsuranceName(param.getUserText(), 12, AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        nameComp.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                setInsuranceDataForBege(getSkinnable().getInsuranceCatalog().getByName(tfInsuranceName.getText(), AbstractCpxCatalog.DEFAULT_COUNTRY));
            }
        });
        nameComp.prefWidthProperty().bind(tfInsuranceName.widthProperty());

        AutoCompletionBinding<String> identComp = TextFields.bindAutoCompletion(tfInsuranceIdent.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return getSkinnable().getInsuranceCatalog().findMatchesByInsuranceNumber(param.getUserText(), 12, AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        identComp.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                setInsuranceDataForBege(getSkinnable().getInsuranceCatalog().getByCode(tfInsuranceIdent.getText(), AbstractCpxCatalog.DEFAULT_COUNTRY));
            }
        });
        identComp.prefWidthProperty().bind(tfInsuranceIdent.widthProperty());
        AutoCompletionBinding<String> zipCodeComp = TextFields.bindAutoCompletion(tfInsuranceZipCode.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return getSkinnable().getInsuranceCatalog().findMatchesByInsuranceZipCode(param.getUserText(), 12, AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        zipCodeComp.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                if (tfInsuranceZipCode.getText().contains("-")) {
                    String[] split = tfInsuranceZipCode.getText().split(" - ");
                    setInsuranceDataForBege(getSkinnable().getInsuranceCatalog().getByName(split[1], AbstractCpxCatalog.DEFAULT_COUNTRY));
                }
            }
        });
        zipCodeComp.prefWidthProperty().bind(tfInsuranceZipCode.widthProperty());
    }

    @Override
    protected void updateRequestValues(TWmRequestBege pRequest) {
        taComment.setText(pRequest.getComment());
        tfContactPerson.setText(pRequest.getBegeEditor());
        tfExtensionNo.setText(pRequest.getDirectPhone());
        tfFaxNo.setText(pRequest.getDirectFax());
        ldpAuditStart.setDate(pRequest.getStartAudit()!=null?pRequest.getStartAudit():new Date());
        ldpStartReport.setDate(pRequest.getReportDate());
        lcbRequestState.selectByInternalId(pRequest.getRequestState());
        String begeInsIdent = pRequest.getInsuranceIdentifier();
        if (begeInsIdent != null) {
            CpxInsuranceCompany insComp = getSkinnable().getInsuranceCatalog().getByCode(begeInsIdent, AbstractCpxCatalog.DEFAULT_COUNTRY);
            tfInsuranceName.setText(insComp.getInscName());
            tfInsuranceIdent.setText(insComp.getInscIdent());
            tfInsuranceZipCode.setText(insComp.getInscZipCode());
            tfInsuranceCity.setText(insComp.getInscCity());
            tfInsuranceCity.setText(insComp.getInscCity());
            labelAddress.setText(insComp.getInscAddress());
            labelFax.setText(insComp.getFaxNumber());
            labelTelephoneNumber.setText(insComp.getPhoneNumber());
            getSkinnable().setInsuranceCompany(insComp);
            getSkinnable().getCatalogValidationResult().invalidate();
        } else {
            String error = "Bege Error (Bege Insurance Ident is null. So can't fetch previous data. Select new Bege Institute)";
            LOG.log(Level.WARNING, error);
            MainApp.showWarningMessageDialog(error);
        }
        updateAuditReasonsData(lcbAuditReasons,pRequest);
    }

    @Override
    protected void disableControls(boolean armed) {
        taComment.setDisable(armed);
        tfInsuranceName.setDisable(armed);
        tfInsuranceCity.setDisable(armed);
        tfInsuranceIdent.setDisable(armed);
        tfInsuranceZipCode.setDisable(armed);
        labelAddress.setDisable(armed);
        labelTelephoneNumber.setDisable(armed);
        labelFax.setDisable(armed);
        tfContactPerson.setDisable(armed);
        tfExtensionNo.setDisable(armed);
        tfFaxNo.setDisable(armed);
        lcbAuditReasons.setDisable(armed);
        ldpAuditStart.setDisable(armed);
        ldpStartReport.setDisable(armed);
        lcbRequestState.setDisable(armed);
        labelAddData.setDisable(armed);
    }

    @Override
    protected void updateRequestData() {
        getSkinnable().getRequest().setInsuranceIdentifier(tfInsuranceIdent.getText());
        getSkinnable().getRequest().setComment(taComment.getText());
        getSkinnable().getRequest().setBegeEditor(tfContactPerson.getText());
        getSkinnable().getRequest().setDirectPhone(tfExtensionNo.getText());
        getSkinnable().getRequest().setDirectFax(tfFaxNo.getText());
        final Long internalId = lcbRequestState.getSelectedInternalId();
        getSkinnable().getRequest().setRequestState(internalId == null ? 0L : internalId);
        getSkinnable().getRequest().setStartAudit(ldpAuditStart.getDate());
        getSkinnable().getRequest().setReportDate(ldpStartReport.getDate());
        updateAuditReasons(lcbAuditReasons,getSkinnable().getRequest());
    }
    
    private void setInsuranceDataForBege(CpxInsuranceCompany assoc) {
        tfInsuranceCity.setText(assoc.getInscCity());
        tfInsuranceIdent.setText(assoc.getInscIdent());
        tfInsuranceName.setText(assoc.getInscName());
        tfInsuranceZipCode.setText(assoc.getInscZipCode());
        labelAddress.setText(assoc.getInscAddress());
        labelFax.setText(assoc.getFaxNumber());
        labelTelephoneNumber.setText(assoc.getPhoneNumber());
        tfExtensionNo.setText(assoc.getPhoneNumber());
        tfFaxNo.setText(assoc.getFaxNumber());
        getSkinnable().setInsuranceCompany(assoc);
        getSkinnable().getCatalogValidationResult().invalidate();
    }

    
}
