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
import de.lb.cpx.client.core.model.fx.labeled.LabeledDatePicker;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextArea;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.client.core.model.fx.labeled.LabeledToggleGroup;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmRequestOther;
import de.lb.cpx.wm.model.enums.WmAuditTypeEn;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.Validator;

/**
 * Skinable Class to handle the layout
 *
 * @author wilde
 */
public class OtherRequestEditorSkin extends BasicRequestEditorSkin<OtherRequestEditor, TWmRequestOther> {

    private static final Logger LOG = Logger.getLogger(OtherRequestEditorSkin.class.getName());
    
    private LabeledTextField ltfInsName;
//    private LabeledTextField ltfInsNumber;
    private LabeledTextField ltfInsEmployee;
    private LabeledDatePicker ldpAuditStart;
//    private LabeledComboBox<AuditTypeEn> lcbAuditType;
    private LabeledDatePicker ldpStartReport;
    private RequestStateCombobox lcbRequestState;
    private LabeledTextField ltfRequestResult;
    private LabeledTextArea ltaComment;
//    private Label lblInfoHeader;
    private LabeledTextField ltfZipCode;
    private LabeledTextField ltfCity;
    private LabeledTextField ltfAddress;
    private LabeledTextField ltfAreaCode;
    private LabeledTextField ltfTelefon;
    private LabeledTextField ltfFax;
    private Button btnInsSearch;

//    private static final String[] REQUEST_RESULTS = new String[]{"Offen", "Ja", "Nein"};
//    private static final String[] INSURANCE_TYPE = new String[] {"Gesetzlich","Privat"};
    private LabeledToggleGroup<RadioButton, WmAuditTypeEn> ltgAuditType;
    private LabeledTextField ltfRequestName;
    private LabeledCheckComboBox<Map.Entry<Long, String>> lcbAuditReasions;

    public OtherRequestEditorSkin(OtherRequestEditor pSkinnable) throws IOException {
        super(pSkinnable, "/fxml/OtherRequestEditorFXML.fxml");
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void setUpNodes() {
        ltfInsName = (LabeledTextField) lookUpInRoot("#ltfInsName");
        ltfRequestName = (LabeledTextField) lookUpInRoot("#ltfRequestName");
//        ltfInsNumber = (LabeledTextField) root.lookup("#ltfInsNumber");
        ltfInsEmployee = (LabeledTextField) lookUpInRoot("#ltfInsEmployee");
        ldpAuditStart = (LabeledDatePicker) lookUpInRoot("#ldpAuditStart");
        ltgAuditType = (LabeledToggleGroup<RadioButton, WmAuditTypeEn>) lookUpInRoot("#ltgAuditType");
        lcbAuditReasions = (LabeledCheckComboBox<Map.Entry<Long, String>>) lookUpInRoot("#lcbAuditReasons");
//        Set<Map.Entry<Long, String>> list = MenuCache.instance().getMdkAuditReasonsEntries();
//        Set<Map.Entry<Long, String>> list = MenuCache.instance().getMdkAuditReasonsEntries(); //getSkinnable().getProcessServiceFacade().getValidUndeletedAuditReasonsEntries();
        Set<Map.Entry<Long, String>> list = MenuCache.instance().getAuditReasonsEntries(getSkinnable().getCase()!=null?getSkinnable().getCase().getCsCaseTypeEn():null);
        lcbAuditReasions.getItems().addAll(new ArrayList<>(list));
        lcbAuditReasions.setConverter(new StringConverter<Map.Entry<Long, String>>() {
            @Override
            public String toString(Map.Entry<Long, String> object) {
                return object == null ? "" : object.getValue();
            }

            @Override
            public Map.Entry<Long, String> fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        ldpStartReport = (LabeledDatePicker) lookUpInRoot("#ldpStartReport");
        lcbRequestState = (RequestStateCombobox) lookUpInRoot("#lcbRequestState");
        ltfRequestResult = (LabeledTextField) lookUpInRoot("#ltfRequestResult");
        ltaComment = (LabeledTextArea) lookUpInRoot("#ltaComment");
        addTextTemplateController(ltaComment);
//        lblInfoHeader = (Label) lookUpInRoot("#lblInfoHeader");
        ltfZipCode = new LabeledTextField();
        ltfZipCode.setSpacing(5.0);
        ltfCity = new LabeledTextField();
        ltfAddress = new LabeledTextField();
        ltfAddress.setSpacing(5.0);
        ltfAreaCode = new LabeledTextField();
        ltfAreaCode.setSpacing(5.0);
        ltfTelefon = new LabeledTextField();
        ltfTelefon.setSpacing(5.0);
        ltfFax = new LabeledTextField();
        ltfFax.setSpacing(5.0);
//        MenuCache.instance().getMdkAuditReasons();
        btnInsSearch = new Button();//(Button) lookUpInRoot("#btnInsSearch");
        ltfInsName.setAdditionalButton(btnInsSearch);
        ldpAuditStart.setDate(new Date());
        lcbRequestState.setItems(MenuCache.getMenuCacheRequestStates().values(new Date(), MenuCacheOptionsEn.IGNORE_INACTIVE));
    }

    @Override
    protected void updateRequestValues(TWmRequestOther pRequest) {
//        ltfInsNumber.setText(pRequest.getInsuranceIdentifier()!=null?pRequest.getInsuranceIdentifier():"");
        ltfInsName.setText(pRequest.getInstitutionName());
        ltfRequestName.setText(pRequest.getRequestName());
        ltfInsEmployee.setText(pRequest.getEditor());
        ldpAuditStart.setDate(pRequest.getStartAudit());
        ltgAuditType.select(pRequest.getAuditType());
        ldpStartReport.setDate(pRequest.getReportDate());
        lcbRequestState.selectByInternalId(pRequest.getRequestState());
        ltfRequestResult.setText(pRequest.getResultComment());
        ltaComment.setText(pRequest.getComment());
        ltfZipCode.setText(pRequest.getInstitutionZipCode());
        ltfCity.setText(pRequest.getInstitutionCity());
        ltfAddress.setText(pRequest.getInstitutionAddress());
        ltfAreaCode.setText(pRequest.getInstitutionAreaCode());
        ltfTelefon.setText(pRequest.getInstitutionTelefon());
        ltfFax.setText(pRequest.getInstitutionFax());
        updateAuditReasonsData(lcbAuditReasions,pRequest);
    }

    @Override
    protected void setUpLanguage() {
        ltfRequestName.setTitle("Anfrage-Name");
        ltfInsName.setTitle("Anfrage-Stelle");
//        ltfInsNumber.setTitle(Lang.getInsuranceIdent());
//        ltfInsNumber.applyFontWeightToTitle(FontWeight.BOLD);
        ltfInsEmployee.setTitle(Lang.getAuditEditor());
        ldpAuditStart.setTitle(Lang.getAuditProcessCreationDate());
        ltgAuditType.setTitle("Prüfart");
        ltgAuditType.setToggleFactory(new Callback<String, RadioButton>() {
            @Override
            public RadioButton call(String param) {
                return new RadioButton(param);
            }
        });
        lcbAuditReasions.setTitle("Prüfgründe");
        ltgAuditType.setValues(WmAuditTypeEn.values());
        ltgAuditType.select(WmAuditTypeEn.SINGLE_AUDIT);
        ldpStartReport.setTitle(Lang.getMdkReportCreationDate());
        lcbRequestState.setTitle(Lang.getAuditStatus());
        ltfRequestResult.setTitle(Lang.getResult());
        ltaComment.setTitle(Lang.getComment());
//        lblInfoHeader.setText("Anfrage-Stelle-Informationen");

        ltfZipCode.setTitle(Lang.getAddressZipCode());
        ltfCity.setTitle(Lang.getAddressCity());
        ltfAddress.setTitle(Lang.getAddressTypePostal());
        ltfAreaCode.setTitle(Lang.getAuditTelephoneAreaCode());
        ltfTelefon.setTitle(Lang.getAddressPhoneNumber());
        ltfFax.setTitle(Lang.getFax());
        btnInsSearch.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.SEARCH_PLUS));
        btnInsSearch.getStyleClass().add("cpx-icon-button");
        btnInsSearch.setText("");
        btnInsSearch.setTooltip(new Tooltip("Anfrage-Stelle-Informationen"));
    }

    @Override
    protected void setUpAutoCompletion() {
    }

    @Override
    protected void setUpValidation() {
        getSkinnable().getValidationSupport().registerValidator(ltfInsName.getControl(), Validator.createEmptyValidator("Bitte geben Sie eine Anfrage-Stelle an!", Severity.ERROR));
        getSkinnable().getValidationSupport().registerValidator(ltfRequestName.getControl(), Validator.createEmptyValidator("Bitte geben Sie einen Anfrage-Namen an!", Severity.ERROR));
    }

    @Override
    public void setUpdateListeners() {
        super.setUpdateListeners();

        btnInsSearch.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                VBox vBox = new VBox(8.0d);
                vBox.getChildren().addAll(ltfAddress, ltfZipCode, ltfCity, ltfAreaCode, ltfTelefon, ltfFax);
                PopOver popover = showInfoPopover(vBox);
                popover.setOnShowing(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent t) {
                        ltfInsName.setShowCaret(false);
                    }
                });
                popover.setOnHiding(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent t) {
                        ltfInsName.setShowCaret(true);
                    }
                });
                popover.show(btnInsSearch);
//                showInfoPopover(vBox).show(btnInsSearch);
            }

        });
    }

    @Override
    protected void updateRequestData() {
        getSkinnable().getRequest().setRequestName(ltfRequestName.getText());
        getSkinnable().getRequest().setInstitutionName(ltfInsName.getText());
        getSkinnable().getRequest().setAuditType(ltgAuditType.getSelected());
        Long internalId = lcbRequestState.getSelectedInternalId();
        getSkinnable().getRequest().setRequestState(internalId == null ? 0L : internalId);
        getSkinnable().getRequest().setStartAudit(ldpAuditStart.getDate());
        getSkinnable().getRequest().setReportDate(ldpStartReport.getDate());
        getSkinnable().getRequest().setComment(ltaComment.getText());
        getSkinnable().getRequest().setEditor(ltfInsEmployee.getText());
        getSkinnable().getRequest().setResultComment(ltfRequestResult.getText());
        updateAuditReasons(lcbAuditReasions,getSkinnable().getRequest());
        getSkinnable().getRequest().setInstitutionZipCode(ltfZipCode.getText());
        getSkinnable().getRequest().setInstitutionCity(ltfCity.getText());
        getSkinnable().getRequest().setInstitutionAddress(ltfAddress.getText());
        getSkinnable().getRequest().setInstitutionAreaCode(ltfAreaCode.getText());
        getSkinnable().getRequest().setInstitutionTelefon(ltfTelefon.getText());
        getSkinnable().getRequest().setInstitutionFax(ltfFax.getText());
    }


    @Override
    protected void disableControls(boolean armed) {
        ltfInsName.setDisable(armed);
        ltfInsEmployee.setDisable(armed);
        ldpAuditStart.setDisable(armed);
        ldpStartReport.setDisable(armed);
        lcbRequestState.setDisable(armed);
        ltfRequestResult.setDisable(armed);
        ltaComment.setDisable(armed);
        ltfZipCode.setDisable(armed);
        ltfCity.setDisable(armed);
        ltfAddress.setDisable(armed);
        ltfAreaCode.setDisable(armed);
        ltfTelefon.setDisable(armed);
        ltfFax.setDisable(armed);
        btnInsSearch.setDisable(armed);
        lcbAuditReasions.setDisable(armed);
        ltfRequestName.setDisable(armed);
        ltgAuditType.setDisable(armed);
    }

}
