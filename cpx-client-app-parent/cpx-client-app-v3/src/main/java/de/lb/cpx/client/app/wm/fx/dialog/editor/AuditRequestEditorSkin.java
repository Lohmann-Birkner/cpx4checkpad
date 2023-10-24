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
 *    2018  shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.dialog.editor;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.wm.fx.process.section.WmReminderSectionForRequest;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.menu.cache.MenuCacheOptionsEn;
import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompany;
import de.lb.cpx.client.core.model.fx.combobox.RequestStateCombobox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledCheckBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledCheckComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledCheckboxLink;
import de.lb.cpx.client.core.model.fx.labeled.LabeledDatePicker;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextArea;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.client.core.model.fx.labeled.LabeledToggleGroup;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.server.commonDB.model.CDeadline;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmReminder;
import de.lb.cpx.wm.model.TWmRequest;
import de.lb.cpx.wm.model.TWmRequestAudit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.naming.NamingException;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.Validator;

/**
 * Skinable Class to handle the Audit Request layout
 *
 * @author shahin
 */
public class AuditRequestEditorSkin extends BasicRequestEditorSkin<AuditRequestEditor, TWmRequestAudit> {

    private static final Logger LOG = Logger.getLogger(AuditRequestEditorSkin.class.getName());

    @FXML
    private LabeledTextField tfInsuName;
    @FXML
    private Button btnInsuSearch;
    private LabeledTextField tfInsuIdent;
    @FXML
    private LabeledTextField tfEditor;
    @FXML
    private LabeledCheckComboBox<Map.Entry<Long, String>> ckcbAuditReasons;
    @FXML
    private LabeledDatePicker dpDateOfProcessCreation;
    @FXML
    private LabeledDatePicker dpDataRecordCorrDeadlineDate;
    @FXML
    private LabeledDatePicker dpPrelProcClosedDeadlineDate;
    @FXML
    private LabeledDatePicker dpPreTrialEnd;
    @FXML
    private CheckBox ckPreTrialEnd;
    @FXML
    private LabeledDatePicker dpSentOn;
    @FXML
    private CheckBox ckSentOn;
    @FXML
    private Label lbCaseDialog;
    @FXML
    private Label lbPreTrial;
    @FXML
    private LabeledDatePicker dpRequest;
    @FXML
    private LabeledDatePicker dpPrelProcAnsDeadlineDate;
    @FXML
    private LabeledCheckBox ckCaseDialog;
    @FXML
    private LabeledCheckBox ckCaseDialogEnd;
    @FXML
    private LabeledDatePicker dpCaseDialogEndDate;
    @FXML
    private LabeledDatePicker dpCaseDialogBillCorrDeadlineDate;
    private LabeledTextField tfZipCode;
    private LabeledTextField tfCity;
    private LabeledTextField tfAddress;
    private LabeledTextField tfTelePrefix;
    private LabeledTextField tfTelephone;
    private LabeledTextField tfFax;
    @FXML
    private RequestStateCombobox cbStatus;
    @FXML
    private HBox hbReminders;
    @FXML
    private LabeledTextArea taAuditComment;
    @FXML
    private LabeledTextArea taUserComment;
    @FXML
    private LabeledCheckboxLink ckLkDataRecordCorrDeadlineDate;
    private ReminderComponents wvDataRecordCorr;
    @FXML
    private LabeledCheckboxLink ckLkPrelProcClosedDeadlineDate;
    private ReminderComponents wvPrelProcClosed;
    @FXML
    private LabeledCheckboxLink ckLkPrelProcAnsDeadlineDate;
    private ReminderComponents wvPrelProcAns;
    @FXML
    private LabeledCheckboxLink ckLkCaseDialogBillCorrDeadlineDateRem;
    private ReminderComponents wvCaseDialogBillCorrection;
    @FXML
    private LabeledToggleGroup<RadioButton, PreTrialStateEn> ltgPeTrialState;
//    private static final String[] REQUEST_RESULTS = new String[]{"Offen", "Ja", "Nein"};
//    private DeadlineList listofDeadlines;
    private WmReminderSectionForRequest auditRemSection;
    private CDeadline dataRecordCorrectionDeadline;
    private CDeadline preliminaryProceedingsClosedDeadline;
    private CDeadline preliminaryProceedingsAnswerDeadline;
    private CDeadline caseDialogBillCorrDeadline;
    private LabeledDatePicker dpReportDate;

    public AuditRequestEditorSkin(AuditRequestEditor pSkinnable) throws IOException {
        super(pSkinnable, "/fxml/AuditRequestEditorFXML.fxml");
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void setUpNodes() {

        tfInsuName = (LabeledTextField) lookUpInRoot("#tfInsuName");
        tfInsuIdent = new LabeledTextField();
        tfEditor = (LabeledTextField) lookUpInRoot("#tfEditor");
        cbStatus = (RequestStateCombobox) lookUpInRoot("#cbStatus");
        ckcbAuditReasons = (LabeledCheckComboBox) lookUpInRoot("#ckcbAuditReasons");
        dpDateOfProcessCreation = (LabeledDatePicker) lookUpInRoot("#dpDateOfProcessCreation");
        dpReportDate = (LabeledDatePicker) lookUpInRoot("#dpReportDate");
        dpDataRecordCorrDeadlineDate = (LabeledDatePicker) lookUpInRoot("#dpDataRecordCorrDeadlineDate");
        dpPrelProcClosedDeadlineDate = (LabeledDatePicker) lookUpInRoot("#dpPrelProcClosedDeadlineDate");
        dpPreTrialEnd = (LabeledDatePicker) lookUpInRoot("#dpPreTrialEnd");
        dpPreTrialEnd.setDisable(true);
        dpSentOn = (LabeledDatePicker) lookUpInRoot("#dpSentOn");
        dpSentOn.setDisable(true);
        dpRequest = (LabeledDatePicker) lookUpInRoot("#dpRequest");
        dpPrelProcAnsDeadlineDate = (LabeledDatePicker) lookUpInRoot("#dpPrelProcAnsDeadlineDate");
        dpCaseDialogBillCorrDeadlineDate = (LabeledDatePicker) lookUpInRoot("#dpCaseDialogBillCorrDeadlineDate");
        dpCaseDialogEndDate = (LabeledDatePicker) lookUpInRoot("#dpCaseDialogEndDate");
        ckPreTrialEnd = (CheckBox) lookUpInRoot("#ckPreTrialEnd");
        ckSentOn = (CheckBox) lookUpInRoot("#ckSentOn");
        lbCaseDialog = (Label) lookUpInRoot("#lbCaseDialog");
        lbPreTrial = (Label) lookUpInRoot("#lbPreTrial");
        ckCaseDialog = (LabeledCheckBox) lookUpInRoot("#ckCaseDialog");
        ckCaseDialogEnd = (LabeledCheckBox) lookUpInRoot("#ckCaseDialogEnd");
        taAuditComment = (LabeledTextArea) lookUpInRoot("#taAuditComment");
        taUserComment = (LabeledTextArea) lookUpInRoot("#taUserComment");
        addTextTemplateController(taAuditComment);
        addTextTemplateController(taUserComment);
        ltgPeTrialState = (LabeledToggleGroup<RadioButton, PreTrialStateEn>) lookUpInRoot("#ltgPeTrialState");
        //lblInformations = new LabeledLabel(Lang.getAuditInfoLabel());
        tfZipCode = new LabeledTextField(Lang.getAuditAreaCode());
        tfZipCode.setSpacing(5.0);
        tfCity = new LabeledTextField(Lang.getAuditCity());
        tfCity.setSpacing(5.0);
        tfAddress = new LabeledTextField(Lang.getAuditAddress());
        tfAddress.setSpacing(5.0);
        tfTelePrefix = new LabeledTextField(Lang.getAuditTelephoneAreaCode());
        tfTelePrefix.setSpacing(5.0);
        tfTelephone = new LabeledTextField(Lang.getAuditTelephone());
        tfTelephone.setSpacing(5.0);
        tfFax = new LabeledTextField(Lang.getAuditFax());
        tfFax.setSpacing(5.0);
        btnInsuSearch = new Button();//(Button) lookUpInRoot("#btnInsuSearch");
        btnInsuSearch.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.SEARCH_PLUS));
        btnInsuSearch.getStyleClass().add("cpx-icon-button");
        btnInsuSearch.setText("");
        tfInsuName.setAdditionalButton(btnInsuSearch);
        ckLkDataRecordCorrDeadlineDate = (LabeledCheckboxLink) lookUpInRoot("#ckLkDataRecordCorrDeadlineDate");
        ckLkPrelProcClosedDeadlineDate = (LabeledCheckboxLink) lookUpInRoot("#ckLkPrelProcClosedDeadlineDate");
        wvDataRecordCorr = new ReminderComponents();
        wvPrelProcAns = new ReminderComponents();
        wvPrelProcClosed = new ReminderComponents();
        wvCaseDialogBillCorrection = new ReminderComponents();
        ckLkDataRecordCorrDeadlineDate = (LabeledCheckboxLink) lookUpInRoot("#ckLkDataRecordCorrDeadlineDate");
        ckLkPrelProcAnsDeadlineDate = (LabeledCheckboxLink) lookUpInRoot("#ckLkPrelProcAnsDeadlineDate");
        ckLkCaseDialogBillCorrDeadlineDateRem = (LabeledCheckboxLink) lookUpInRoot("#ckLkCaseDialogBillCorrDeadlineDateRem");
        hbReminders = (HBox) lookUpInRoot("#hbReminders");
//        Set<Map.Entry<Long, String>> list = MenuCache.instance().getMdkAuditReasonsEntries();
//        Set<Map.Entry<Long, String>> list = MenuCache.instance().getMdkAuditReasonsEntries(); //getSkinnable().getProcessServiceFacade().getValidUndeletedAuditReasonsEntries();
        Set<Map.Entry<Long, String>> list = MenuCache.instance().getAuditReasonsEntries(getSkinnable().getCase()!=null?getSkinnable().getCase().getCsCaseTypeEn():null);
        ckcbAuditReasons.getItems().addAll(new ArrayList<>(list));
        ckcbAuditReasons.registerTooltip();
        ckcbAuditReasons.setConverter(new StringConverter<Map.Entry<Long, String>>() {
            @Override
            public String toString(Map.Entry<Long, String> object) {
                return object == null ? "" : object.getValue();
            }

            @Override
            public Map.Entry<Long, String> fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        if (getSkinnable().getRequest().getId() != 0 && !getSkinnable().getProcessServiceFacade().getRemindersForRequest(getSkinnable().getRequest().getId()).isEmpty()) {
            auditRemSection = new WmReminderSectionForRequest(getSkinnable().getProcessServiceFacade(), getSkinnable().getRequest());

            hbReminders.getChildren().addAll(auditRemSection.getRoot());
        }
//        listofDeadlines = new DeadlineList(Session.instance().getEjbConnector().connectProcessServiceBean().get().getDeadlines(getSkinnable().getCaseAdmissionDate()));
        dataRecordCorrectionDeadline = MenuCache.getMenuCacheDeadlines().getDrcd(getSkinnable().getCaseAdmissionDate());
        preliminaryProceedingsClosedDeadline = MenuCache.getMenuCacheDeadlines().getPpcd(getSkinnable().getCaseAdmissionDate());
        preliminaryProceedingsAnswerDeadline = MenuCache.getMenuCacheDeadlines().getPpad(getSkinnable().getCaseAdmissionDate());
        caseDialogBillCorrDeadline = MenuCache.getMenuCacheDeadlines().getCbcd(getSkinnable().getCaseAdmissionDate());
        cbStatus.setItems(MenuCache.getMenuCacheRequestStates().values(new Date(), MenuCacheOptionsEn.IGNORE_INACTIVE));
        dpDateOfProcessCreation.setDate(new Date());
    }

    @Override
    public void setUpdateListeners() {
        super.setUpdateListeners();
        btnInsuSearch.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                VBox vBox = new VBox(8.0d);
                vBox.getChildren().addAll(tfInsuIdent, tfCity, tfTelePrefix, tfTelephone, tfZipCode, tfAddress);
                PopOver popover = showInfoPopover(vBox);
                popover.setOnShowing(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent t) {
                        tfInsuName.setShowCaret(false);
                    }
                });
                popover.setOnHiding(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent t) {
                        tfInsuName.setShowCaret(true);
                    }
                });
                popover.show(btnInsuSearch);
//                showInfoPopover(vBox).show(btnInsuSearch);
            }
        });
        dpPrelProcAnsDeadlineDate.setDisable(true);
        ckCaseDialog.getControl().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                if (ckCaseDialog.isSelected()) {
                    dpRequest.setDisable(false);
                    dpRequest.setDate(new Date());
                    if (preliminaryProceedingsAnswerDeadline != null) {
                        final Date deadlineDt = preliminaryProceedingsAnswerDeadline.calculateDeadlineExtern(dpRequest.getDate());

                        dpPrelProcAnsDeadlineDate.setDate(deadlineDt);
                        dpPrelProcAnsDeadlineDate.setDisable(true);
                    } else {
                        MainApp.showWarningMessageDialog(Lang.getAuditPrelProcAnsDeadlineError(), getWindow());
                        dpPrelProcAnsDeadlineDate.setDisable(false);
                    }
               } else {
                    dpRequest.setDisable(true);
                    dpRequest.getControl().getEditor().clear();
                    dpRequest.setDate(null);
                    dpPrelProcAnsDeadlineDate.setDisable(true);
                    dpPrelProcAnsDeadlineDate.getControl().getEditor().clear();
                    dpPrelProcAnsDeadlineDate.setDate(null);
                }
            }
        });

        dpPrelProcAnsDeadlineDate.setDisable(true);
        ckCaseDialogEnd.getControl().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                if (ckCaseDialogEnd.isSelected()) {
                    dpCaseDialogEndDate.setDisable(false);
                    dpCaseDialogEndDate.setDate(new Date());
                    if (caseDialogBillCorrDeadline != null) {
                        final Date deadlineDt = caseDialogBillCorrDeadline.calculateDeadlineExtern(dpCaseDialogEndDate.getDate());

                        dpCaseDialogBillCorrDeadlineDate.setDate(deadlineDt);
                        dpCaseDialogBillCorrDeadlineDate.setDisable(true);
                    } else {
                        MainApp.showWarningMessageDialog(Lang.getAuditCaseDialogBillCorrDeadlineError(), getWindow());
                        if(getSkinnable().isEditable()){
                            dpCaseDialogBillCorrDeadlineDate.setDisable(false);
                        }
                    }
                } else {
                    dpCaseDialogEndDate.setDisable(true);
                    dpCaseDialogEndDate.getControl().getEditor().clear();
                    dpCaseDialogEndDate.setDate(null);
                    dpCaseDialogBillCorrDeadlineDate.setDisable(true);
                    dpCaseDialogBillCorrDeadlineDate.getControl().getEditor().clear();
                    dpCaseDialogBillCorrDeadlineDate.setDate(null);
                }
            }
        });

        dpRequest.getControl().valueProperty().addListener((ov, oldValue, newValue) -> {
            if (preliminaryProceedingsAnswerDeadline != null) {
                final Date deadlineDtEx = preliminaryProceedingsAnswerDeadline.calculateDeadlineExtern(dpRequest.getDate());

                dpPrelProcAnsDeadlineDate.setDate(deadlineDtEx);
                dpPrelProcAnsDeadlineDate.setDisable(true);
                final Date deadlineDt = preliminaryProceedingsAnswerDeadline.calculateDeadline(dpRequest.getDate());

                wvPrelProcAns.setDueTo(deadlineDt);
                if (wvPrelProcAns.getRemTypeInternalId() == 0L) {
                    wvPrelProcAns.setRemType(preliminaryProceedingsAnswerDeadline.getDlReminderType());
                }
            } else {
                wvPrelProcAns.setDueTo(null);
                dpPrelProcAnsDeadlineDate.setDisable(false);
            }
        });

        dpCaseDialogEndDate.getControl().valueProperty().addListener((ov, oldValue, newValue) -> {
            if (caseDialogBillCorrDeadline != null) {
                final Date deadlineDtEx = caseDialogBillCorrDeadline.calculateDeadlineExtern(dpCaseDialogEndDate.getDate());

                dpCaseDialogBillCorrDeadlineDate.setDate(deadlineDtEx);
                dpCaseDialogBillCorrDeadlineDate.setDisable(true);
                final Date deadlineDt = caseDialogBillCorrDeadline.calculateDeadline(dpCaseDialogEndDate.getDate());

                wvCaseDialogBillCorrection.setDueTo(deadlineDt);
                if (wvCaseDialogBillCorrection.getRemTypeInternalId() == 0L) {
                    wvCaseDialogBillCorrection.setRemType(caseDialogBillCorrDeadline.getDlReminderType());
                }
            } else {
                wvCaseDialogBillCorrection.setDueTo(null);
                dpCaseDialogBillCorrDeadlineDate.setDisable(false);
            }
        });

        ckLkDataRecordCorrDeadlineDate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final Date deadlineDt = dataRecordCorrectionDeadline == null ? null : dataRecordCorrectionDeadline.calculateDeadline(dpDateOfProcessCreation.getDate());
                wvDataRecordCorr.setDueTo(deadlineDt);
                if (wvDataRecordCorr.getRemTypeInternalId() == 0L && dataRecordCorrectionDeadline != null) {
                    wvDataRecordCorr.setRemType(dataRecordCorrectionDeadline.getDlReminderType());
                }
                wvDataRecordCorr.showWvPopover(ckLkDataRecordCorrDeadlineDate);
            }

        });
        ckLkPrelProcAnsDeadlineDate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final Date deadlineDt = preliminaryProceedingsAnswerDeadline == null ? null : preliminaryProceedingsAnswerDeadline.calculateDeadline(dpRequest.getDate());

                wvPrelProcAns.setDueTo(deadlineDt);
                if (wvPrelProcAns.getRemTypeInternalId() == 0L && preliminaryProceedingsAnswerDeadline != null) {
                    wvPrelProcAns.setRemType(preliminaryProceedingsAnswerDeadline.getDlReminderType());
                }
                wvPrelProcAns.showWvPopover(ckLkPrelProcAnsDeadlineDate);
            }

        });
        ckLkPrelProcClosedDeadlineDate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final Date deadlineDt = preliminaryProceedingsClosedDeadline == null ? null : preliminaryProceedingsClosedDeadline.calculateDeadline(dpDateOfProcessCreation.getDate());
                wvPrelProcClosed.setDueTo(deadlineDt);
                if (wvPrelProcClosed.getRemTypeInternalId() == 0L && preliminaryProceedingsClosedDeadline != null) {
                    wvPrelProcClosed.setRemType(preliminaryProceedingsClosedDeadline.getDlReminderType());
                }
                wvPrelProcClosed.showWvPopover(ckLkPrelProcClosedDeadlineDate);
            }

        });
        ckLkCaseDialogBillCorrDeadlineDateRem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final Date deadlineDt = preliminaryProceedingsAnswerDeadline == null ? null : preliminaryProceedingsAnswerDeadline.calculateDeadline(dpCaseDialogEndDate.getDate());

                wvCaseDialogBillCorrection.setDueTo(deadlineDt);
                if (wvCaseDialogBillCorrection.getRemTypeInternalId() == 0L && preliminaryProceedingsAnswerDeadline != null) {
                    wvCaseDialogBillCorrection.setRemType(preliminaryProceedingsAnswerDeadline.getDlReminderType());
                }
                wvCaseDialogBillCorrection.showWvPopover(ckLkCaseDialogBillCorrDeadlineDateRem);
            }

        });

        ckSentOn.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                if (ckSentOn.isSelected()) {
                    dpSentOn.setDisable(false);
                } else {
                    dpSentOn.setDisable(true);
                }
            }
        });
        ckPreTrialEnd.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                if (ckPreTrialEnd.isSelected()) {
                    dpPreTrialEnd.setDisable(false);
                } else {
                    dpPreTrialEnd.setDisable(true);
                }
            }
        });
    }

    @Override
    protected void setUpLanguage() {
        tfInsuName.setTitle(Lang.getAuditInsuName());
        tfInsuIdent.setTitle(Lang.getAuditIdent());
        tfInsuIdent.setPrefWidth(300);
        tfEditor.setTitle(Lang.getAuditEditor());
//        cbStatus.getItems().addAll(REQUEST_RESULTS);
        cbStatus.setTitle(Lang.getAuditStatus());
        ckcbAuditReasons.setTitle(Lang.getMdkAuditReasons());
        dpDateOfProcessCreation.setTitle(Lang.getAuditProcessCreationDate());
        dpReportDate.setTitle(Lang.getMdkReportCreationDate());
        dpDataRecordCorrDeadlineDate.setTitle(Lang.getAuditDataRecoCorrDeadline());
        dpPrelProcClosedDeadlineDate.setTitle(Lang.getAuditPrelProcClosedDeadline());
        dpPreTrialEnd.setTitle(Lang.getAuditPreTrialEndDate());
        dpSentOn.setTitle(Lang.getAuditSentOnDate());
        dpRequest.setTitle(Lang.getAuditRequestDate());
        dpPrelProcAnsDeadlineDate.setTitle(Lang.getAuditAnsDeadline());
        dpCaseDialogBillCorrDeadlineDate.setTitle("Rechnungskorrektur Ende Falldialog");
        dpCaseDialogEndDate.setTitle(Lang.getAuditCaseDialogEndDate());
        ckPreTrialEnd.setText("");
        ckSentOn.setText("");
        lbCaseDialog.setText(Lang.getAuditCaseDialog());
        lbPreTrial.setText("Vorverfahren");
        ckCaseDialog.setTitle("");
        ckCaseDialogEnd.setTitle("");
        taAuditComment.setTitle(Lang.getAuditComment());
        taUserComment.setTitle(Lang.getAuditUserComment());
        btnInsuSearch.setTooltip(new Tooltip(Lang.getMdkInformations()));
        ckLkDataRecordCorrDeadlineDate.setTitle("Wiedervorlage");
        ckLkDataRecordCorrDeadlineDate.getControl().setText("öffnen");
        ckLkPrelProcClosedDeadlineDate.setTitle("Wiedervorlage");
        ckLkPrelProcClosedDeadlineDate.getControl().setText("öffnen");
        ckLkPrelProcAnsDeadlineDate.setTitle("Wiedervorlage");
        ckLkPrelProcAnsDeadlineDate.getControl().setText("öffnen");
        ckLkCaseDialogBillCorrDeadlineDateRem.setTitle("Wiedervorlage");
        ckLkCaseDialogBillCorrDeadlineDateRem.getControl().setText("öffnen");
        ltgPeTrialState.setTitle(Lang.getAuditPreTrial());
        ltgPeTrialState.setToggleFactory(new Callback<String, RadioButton>() {
            @Override
            public RadioButton call(String param) {
                return new RadioButton(param);
            }
        });

        ltgPeTrialState.setValues(PreTrialStateEn.values());
        ltgPeTrialState.select(PreTrialStateEn.Ja);
    }

    @Override
    protected void setUpValidation() {
        getSkinnable().getValidationSupport().registerValidator(tfInsuName.getControl(), new Validator<String>() {
            @Override
            public ValidationResult apply(Control t, String insName) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, "Bitte geben Sie den Krankenkasse Namen ein!", (insName == null || insName.isEmpty()));
                String str[] = insName == null ? new String[0] : insName.split(" , ");
                switch (str.length) {
                    case 2:
                        res.addErrorIf(t, "Krankenkasse Name ist nicht korrekt!", !getSkinnable().getInsuranceCatalog().hasEntryOfInsName(str[0], AbstractCpxCatalog.DEFAULT_COUNTRY) || !getSkinnable().getInsuranceCatalog().hasEntry(str[1], AbstractCpxCatalog.DEFAULT_COUNTRY));
                        getSkinnable().getValidationSupport().setErrorDecorationEnabled(true);
                        break;
                    case 1:
                        res.addErrorIf(t, "Krankenkasse Name ist nicht korrekt!", !getSkinnable().getInsuranceCatalog().hasEntryOfInsName(str[0], AbstractCpxCatalog.DEFAULT_COUNTRY));
                        getSkinnable().getValidationSupport().setErrorDecorationEnabled(true);
                        break;
                    default:
                        break;
                }
                return res;
            }
        });

        getSkinnable().getValidationSupport().registerValidator(tfInsuIdent.getControl(), new Validator<String>() {
            @Override
            public ValidationResult apply(Control t, String u) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, "Bitte geben Sie den Krankenkasse Ident ein!", (u == null || u.isEmpty()));
                res.addErrorIf(t, "Krankenkasse Ident ist nicht korrekt!", (!getSkinnable().getInsuranceCatalog().hasEntry(u, AbstractCpxCatalog.DEFAULT_COUNTRY)));
                getSkinnable().getValidationSupport().setErrorDecorationEnabled(true);
                return res;
            }
        });

        getSkinnable().getValidationSupport().registerValidator(tfZipCode.getControl(), false, new Validator<String>() {
            @Override
            public ValidationResult apply(Control t, String u) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, "Krankenkasse Postleitzahl ist nicht korrekt!", (u != null && !u.isEmpty()) && !getSkinnable().getInsuranceCatalog().hasEntryOfInsZipCode(u, AbstractCpxCatalog.DEFAULT_COUNTRY));
                return res;
            }
        });

        getSkinnable().getValidationSupport().registerValidator(tfCity.getControl(), false, new Validator<String>() {
            @Override
            public ValidationResult apply(Control t, String u) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, "Krankenkasse Ort ist nicht korrekt!", (u != null && !u.isEmpty()) && !getSkinnable().getInsuranceCatalog().hasEntryOfInsCity(u, AbstractCpxCatalog.DEFAULT_COUNTRY));
                return res;
            }
        });

        getSkinnable().getValidationSupport().registerValidator(tfAddress.getControl(), false, new Validator<String>() {
            @Override
            public ValidationResult apply(Control t, String u) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, "Krankenkasse Anschrift ist nicht korrekt!", (u != null && !u.isEmpty()) && !getSkinnable().getInsuranceCatalog().hasEntryOfAddress(u, AbstractCpxCatalog.DEFAULT_COUNTRY));
                return res;
            }
        });

        getSkinnable().getValidationSupport().registerValidator(tfTelePrefix.getControl(), false, new Validator<String>() {
            @Override
            public ValidationResult apply(Control t, String u) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, "Krankenkasse Vorwahl ist nicht korrekt!", (u != null && !u.isEmpty()) && !getSkinnable().getInsuranceCatalog().hasEntryOfAreaCode(u, AbstractCpxCatalog.DEFAULT_COUNTRY));
                return res;
            }
        });

        getSkinnable().getValidationSupport().registerValidator(tfTelephone.getControl(), false, new Validator<String>() {
            @Override
            public ValidationResult apply(Control t, String u) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, "Krankenkasse Telefon ist nicht korrekt!", (u != null && !u.isEmpty()) && !getSkinnable().getInsuranceCatalog().hasEntryOfTelephone(u, AbstractCpxCatalog.DEFAULT_COUNTRY));
                return res;
            }
        });

        getSkinnable().getValidationSupport().registerValidator(tfFax.getControl(), false, new Validator<String>() {
            @Override
            public ValidationResult apply(Control t, String u) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, "Krankenkasse Fax ist nicht korrekt!", (u != null && !u.isEmpty()) && !getSkinnable().getInsuranceCatalog().hasEntryOfFax(u, AbstractCpxCatalog.DEFAULT_COUNTRY));
                return res;
            }
        });

        if (MenuCache.getMenuCacheDeadlines().isEmpty()) {
            String error = Lang.getDeadlinesError();
            LOG.log(Level.WARNING, error);
            MainApp.showWarningMessageDialog(error, getWindow());
        }

        if (!MenuCache.getMenuCacheDeadlines().isEmpty() && dataRecordCorrectionDeadline != null) {
            final Date deadlineDtEx = dataRecordCorrectionDeadline.calculateDeadlineExtern(dpDateOfProcessCreation.getDate() == null ? new Date() : dpDateOfProcessCreation.getDate());
            dpDataRecordCorrDeadlineDate.setDate(deadlineDtEx);

            dpDataRecordCorrDeadlineDate.setDisable(true);
        } else {
            MainApp.showWarningMessageDialog(Lang.getAuditDataRecCorrDeadlineError(), getWindow());
            if(getSkinnable().isEditable()){ //CPX-2359 AWi: ??? is this correct? otherwise there is enabled control in disabled ui
                dpDataRecordCorrDeadlineDate.setDisable(false);
            }
        }
        if (preliminaryProceedingsClosedDeadline != null) {
            final Date deadlinePPEx = preliminaryProceedingsClosedDeadline.calculateDeadlineExtern(dpDateOfProcessCreation.getDate());
            dpPrelProcClosedDeadlineDate.setDate(deadlinePPEx);
            dpPrelProcClosedDeadlineDate.setDisable(deadlinePPEx != null);
        }
        dpDataRecordCorrDeadlineDate.getControl().valueProperty().addListener((ov, oldValue, newValue) -> {
            final Date deadlineDt = dataRecordCorrectionDeadline == null ? null : dataRecordCorrectionDeadline.calculateDeadline(dpDateOfProcessCreation.getDate());
            wvDataRecordCorr.setDueTo(deadlineDt);
            if (wvDataRecordCorr.getRemTypeInternalId() == 0L && dataRecordCorrectionDeadline != null) {
                wvDataRecordCorr.setRemType(dataRecordCorrectionDeadline.getDlReminderType());
            }
        });
        dpPrelProcClosedDeadlineDate.getControl().valueProperty().addListener((ov, oldValue, newValue) -> {
            final Date deadlineDt = preliminaryProceedingsClosedDeadline == null ? null : preliminaryProceedingsClosedDeadline.calculateDeadline(dpDateOfProcessCreation.getDate());
            final Date date = deadlineDt == null ? dpPrelProcClosedDeadlineDate.getDate() : deadlineDt;

            wvPrelProcClosed.setDueTo(date);
            if (wvPrelProcClosed.getRemTypeInternalId() == 0L && preliminaryProceedingsClosedDeadline != null) {
                wvPrelProcClosed.setRemType(preliminaryProceedingsClosedDeadline.getDlReminderType());
            }
        });
        dpPrelProcAnsDeadlineDate.getControl().valueProperty().addListener((ov, oldValue, newValue) -> {
            wvPrelProcAns.setDueTo(dpPrelProcAnsDeadlineDate.getDate());
            if (wvPrelProcAns.getRemTypeInternalId() == 0L && preliminaryProceedingsAnswerDeadline != null) {
                wvPrelProcAns.setRemType(preliminaryProceedingsAnswerDeadline.getDlReminderType());
            }
        });
        dpDateOfProcessCreation.getControl().valueProperty().addListener((ov, oldValue, newValue) -> {
            if (dataRecordCorrectionDeadline != null) {
                final Date deadlineDtEx = dataRecordCorrectionDeadline.calculateDeadlineExtern(dpDateOfProcessCreation.getDate());
                final Date deadlineDt = dataRecordCorrectionDeadline.calculateDeadline(dpDateOfProcessCreation.getDate());
                dpDataRecordCorrDeadlineDate.setDate(deadlineDtEx);
                dpDataRecordCorrDeadlineDate.setDisable(true);
//                final Date date = deadlineDt == null ? dpDataRecordCorrDeadlineDate.getDate() : deadlineDt;
                wvDataRecordCorr.setDueTo(deadlineDt);
                if (wvDataRecordCorr.getRemTypeInternalId() == 0L) {
                    wvDataRecordCorr.setRemType(dataRecordCorrectionDeadline.getDlReminderType());
                }
            } else {
                if(getSkinnable().isEditable()){ //CPX-2359 AWi: ??? is this correct? otherwise there is enabled control in disabled ui
                    dpDataRecordCorrDeadlineDate.setDisable(false);
                }
                wvDataRecordCorr.setDueTo(null);
            }
            if (preliminaryProceedingsClosedDeadline != null) {
                final Date deadlineDtEx = preliminaryProceedingsClosedDeadline.calculateDeadlineExtern(dpDateOfProcessCreation.getDate());
                final Date deadlineDt = preliminaryProceedingsClosedDeadline.calculateDeadline(dpDateOfProcessCreation.getDate());

                dpPrelProcClosedDeadlineDate.setDate(deadlineDtEx);
                dpPrelProcClosedDeadlineDate.setDisable(true);
                wvPrelProcClosed.setDueTo(deadlineDt);
                if (wvPrelProcClosed.getRemTypeInternalId() == 0L) {
                    wvPrelProcClosed.setRemType(preliminaryProceedingsClosedDeadline.getDlReminderType());
                }
            } else {
                dpPrelProcClosedDeadlineDate.setDisable(false);
                wvPrelProcClosed.setDueTo(null);
            }
        });

        getSkinnable().getValidationSupport().registerValidator(wvDataRecordCorr.getUserCtrl().getControl(), new Validator<String>() {
            @Override
            public ValidationResult apply(Control t, String u) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, Lang.getReminderValidationWarningNoReceiver(), ckLkDataRecordCorrDeadlineDate.isChecked() && (u == null || u.isEmpty()));

                try {
                    res.addErrorIf(t, Lang.getReminderValidationErrorNoValidReceiver(), ckLkDataRecordCorrDeadlineDate.isChecked() && MenuCache.instance().getUserId(u) == null);
                } catch (NamingException ex) {
                    Logger.getLogger(ReminderComponents.class.getName()).log(Level.SEVERE, null, ex);
                }
                return res;
            }
        });
        getSkinnable().getValidationSupport().registerValidator(wvPrelProcAns.getUserCtrl().getControl(), new Validator<String>() {
            @Override
            public ValidationResult apply(Control t, String u) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, Lang.getReminderValidationWarningNoReceiver(), ckLkPrelProcAnsDeadlineDate.isChecked() && (u == null || u.isEmpty()));

                try {
                    res.addErrorIf(t, Lang.getReminderValidationErrorNoValidReceiver(), ckLkPrelProcAnsDeadlineDate.isChecked() && MenuCache.instance().getUserId(u) == null);
                } catch (NamingException ex) {
                    Logger.getLogger(ReminderComponents.class.getName()).log(Level.SEVERE, null, ex);
                }
                return res;
            }
        });
        getSkinnable().getValidationSupport().registerValidator(wvPrelProcClosed.getUserCtrl().getControl(), new Validator<String>() {
            @Override
            public ValidationResult apply(Control t, String u) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, Lang.getReminderValidationWarningNoReceiver(), ckLkPrelProcClosedDeadlineDate.isChecked() && (u == null || u.isEmpty()));

                try {
                    res.addErrorIf(t, Lang.getReminderValidationErrorNoValidReceiver(), ckLkPrelProcClosedDeadlineDate.isChecked() && MenuCache.instance().getUserId(u) == null);
                } catch (NamingException ex) {
                    Logger.getLogger(ReminderComponents.class.getName()).log(Level.SEVERE, null, ex);
                }
                return res;
            }
        });
        getSkinnable().getValidationSupport().registerValidator(wvCaseDialogBillCorrection.getUserCtrl().getControl(), new Validator<String>() {
            @Override
            public ValidationResult apply(Control t, String u) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, Lang.getReminderValidationWarningNoReceiver(), ckLkCaseDialogBillCorrDeadlineDateRem.isChecked() && (u == null || u.isEmpty()));

                try {
                    res.addErrorIf(t, Lang.getReminderValidationErrorNoValidReceiver(), ckLkCaseDialogBillCorrDeadlineDateRem.isChecked() && MenuCache.instance().getUserId(u) == null);
                } catch (NamingException ex) {
                    Logger.getLogger(ReminderComponents.class.getName()).log(Level.SEVERE, null, ex);
                }
                return res;
            }
        });
        dpRequest.setDisable(!ckCaseDialog.isSelected());
        dpCaseDialogEndDate.setDisable(!ckCaseDialogEnd.isSelected());
        dpCaseDialogBillCorrDeadlineDate.setDisable(dpCaseDialogBillCorrDeadlineDate.getDate() != null || 
                !(dpCaseDialogBillCorrDeadlineDate.getDate() == null && ckCaseDialog.isSelected() && getSkinnable().isEditable()));
    }

    @Override
    protected void setUpAutoCompletion() {
        AutoCompletionBinding<String> ACBinsuName = TextFields.bindAutoCompletion(tfInsuName.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return getSkinnable().getInsuranceCatalog().findMatchesByInsuranceName(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        ACBinsuName.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                String[] split = tfInsuName.getText().split(" , ");
                updateCatalogValues(getSkinnable().getInsuranceCatalog().getByIdent(split[1].trim(), AbstractCpxCatalog.DEFAULT_COUNTRY));
            }
        });
        ACBinsuName.prefWidthProperty().bind(tfInsuName.widthProperty());

        AutoCompletionBinding<String> ACBIk = TextFields.bindAutoCompletion(tfInsuIdent.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return getSkinnable().getInsuranceCatalog().findMatchesByInsuranceNumber(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        ACBIk.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                String[] split = tfInsuIdent.getText().split(" , ");
                updateCatalogValues(getSkinnable().getInsuranceCatalog().getByCode(split[0].trim(), AbstractCpxCatalog.DEFAULT_COUNTRY));
            }
        });
        ACBIk.prefWidthProperty().bind(tfInsuIdent.widthProperty());

        AutoCompletionBinding<String> ACBzipCode = TextFields.bindAutoCompletion(tfZipCode.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return getSkinnable().getInsuranceCatalog().findMatchesByInsZipCode(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        ACBzipCode.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                if (tfZipCode.getText().contains(",")) {
                    String[] split = tfZipCode.getText().split(" , ");
                    updateCatalogValues(getSkinnable().getInsuranceCatalog().getByZipInsNameIdent(split[0], split[1], split[2], AbstractCpxCatalog.DEFAULT_COUNTRY));
                }
            }
        });
        ACBzipCode.prefWidthProperty().bind(tfZipCode.widthProperty());

        AutoCompletionBinding<String> ACBcity = TextFields.bindAutoCompletion(tfCity.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return getSkinnable().getInsuranceCatalog().findMatchesByInsuranceCity(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        ACBcity.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                if (tfCity.getText().contains(",")) {
                    String[] split = tfCity.getText().split(" , ");
                    updateCatalogValues(getSkinnable().getInsuranceCatalog().getByCityInsNameIdent(split[0], split[1], split[2], AbstractCpxCatalog.DEFAULT_COUNTRY));
                }
            }
        });
        ACBcity.prefWidthProperty().bind(tfCity.widthProperty());

        AutoCompletionBinding<String> ACBaddress = TextFields.bindAutoCompletion(tfAddress.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return getSkinnable().getInsuranceCatalog().findMatchesByAddress(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        ACBaddress.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                if (tfAddress.getText().contains(",")) {
                    String[] split = tfAddress.getText().split(" , ");
                    updateCatalogValues(getSkinnable().getInsuranceCatalog().getByAddressNameIdent(split[0], split[1], split[2], AbstractCpxCatalog.DEFAULT_COUNTRY));
                }
            }
        });
        ACBaddress.prefWidthProperty().bind(tfAddress.widthProperty());

        AutoCompletionBinding<String> ACBPhonePrefix = TextFields.bindAutoCompletion(tfTelePrefix.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return getSkinnable().getInsuranceCatalog().findMatchesByPhonePrefix(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        ACBPhonePrefix.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                if (tfTelePrefix.getText().contains(",")) {
                    String[] split = tfTelePrefix.getText().split(" , ");
                    updateCatalogValues(getSkinnable().getInsuranceCatalog().getByPhonePrefixNameIdent(split[0], split[1], split[2], AbstractCpxCatalog.DEFAULT_COUNTRY));
                }
            }
        });
        ACBPhonePrefix.prefWidthProperty().bind(tfTelePrefix.widthProperty());

        AutoCompletionBinding<String> ACBPhone = TextFields.bindAutoCompletion(tfTelephone.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return getSkinnable().getInsuranceCatalog().findMatchesByPhoneNo(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        ACBPhone.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                updateCatalogValues(getSkinnable().getInsuranceCatalog().getByPhoneNo(tfTelephone.getText(), AbstractCpxCatalog.DEFAULT_COUNTRY));

            }
        });
        ACBPhone.prefWidthProperty().bind(tfTelephone.widthProperty());

        AutoCompletionBinding<String> ACBFax = TextFields.bindAutoCompletion(tfFax.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return getSkinnable().getInsuranceCatalog().findMatchesByFaxNo(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            }
        });
        ACBFax.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                updateCatalogValues(getSkinnable().getInsuranceCatalog().getByFaxNo(tfFax.getText(), AbstractCpxCatalog.DEFAULT_COUNTRY));

            }
        });
        ACBFax.prefWidthProperty().bind(tfFax.widthProperty());
    }

    @Override
    protected void updateRequestValues(TWmRequestAudit pRequest) {
        dpDateOfProcessCreation.setDate(getSkinnable().getRequest().getStartAudit());
        dpReportDate.setDate(pRequest.getReportDate());
        tfEditor.setText(pRequest.getEditor());
        if (pRequest.getPreTrialState() != null && pRequest.getPreTrialState().intValue() >= 0) {
            ltgPeTrialState.select(PreTrialStateEn.findById(pRequest.getPreTrialState()));

        }

        if (pRequest.getRequestState() != null && pRequest.getRequestState().intValue() >= 0) {

           cbStatus.selectByInternalId(pRequest.getRequestState());
        }

        dpDataRecordCorrDeadlineDate.setDate(pRequest.getDataRecordCorrectionDeadline());
        dpPrelProcClosedDeadlineDate.setDate(pRequest.getPreliminaryProceedingsClosedDeadline());

        if (pRequest.isPreTrialEndFl()) {
            ckPreTrialEnd.setSelected(true);
            dpPreTrialEnd.setDate(pRequest.getPreTrialEnd());
        }
        if (pRequest.isSentOnFl()) {
            ckSentOn.setSelected(true);
            dpSentOn.setDate(pRequest.getSentOn());
        }

        if (pRequest.isCaseDialogFl()) {
            ckCaseDialog.setSelected(true);
            dpRequest.setDate(pRequest.getRequestDate());
            dpPrelProcAnsDeadlineDate.setDate(pRequest.getAnswerDeadline());
//            dpCaseDialogBillCorrDeadlineDate.setDate(pRequest.getCaseDialogBillCorrDeadline());
        }
        if (pRequest.isCaseDialogEndFl()) {
            ckCaseDialogEnd.setSelected(true);
            dpCaseDialogEndDate.setDate(pRequest.getCaseDialogEndDate());
            dpCaseDialogBillCorrDeadlineDate.setDate(pRequest.getCaseDialogBillCorrDeadline());
            dpCaseDialogBillCorrDeadlineDate.setDisable(pRequest.getCaseDialogBillCorrDeadline() != null);
        }
        taUserComment.setText(pRequest.getComment());
        taAuditComment.setText(pRequest.getUserComment());

        CpxInsuranceCompany insComp = getSkinnable().getInsuranceCatalog().getByCode(pRequest.getInsuranceIdentifier(), AbstractCpxCatalog.DEFAULT_COUNTRY);
        tfInsuName.setText(insComp.getInscName().trim() + " , " + insComp.getInscIdent());
        tfInsuIdent.setText(insComp.getInscIdent());
        tfZipCode.setText(insComp.getInscZipCode());
        tfCity.setText(insComp.getInscCity());
        tfAddress.setText(insComp.getInscAddress());
        tfTelePrefix.setText(insComp.getInscPhonePrefix());
        tfTelephone.setText(insComp.getInscPhone());
        tfFax.setText(insComp.getInscFax());
        updateAuditReasonsData(ckcbAuditReasons,pRequest);
        getSkinnable().setInsuranceCompany(insComp);
        getSkinnable().getCatalogValidationResult().invalidate();
    }

    private void updateCatalogValues(CpxInsuranceCompany insComp) {
        if (insComp.id == 0L) {
            tfInsuName.setText(insComp.getInscName());
        } else {
            tfInsuName.setText(insComp.getInscName().trim() + " , " + insComp.getInscIdent());
        }
        tfInsuIdent.setText(insComp.getInscIdent());
        tfZipCode.setText(insComp.getInscZipCode());
        tfCity.setText(insComp.getInscCity());
        tfAddress.setText(insComp.getInscAddress());
        tfTelePrefix.setText(insComp.getInscPhonePrefix());
        tfTelephone.setText(insComp.getInscPhone());
        tfFax.setText(insComp.getInscFax());
        getSkinnable().setInsuranceCompany(insComp);
        getSkinnable().getCatalogValidationResult().invalidate();

    }

    @Override
    protected void updateRequestData() {
        getSkinnable().getRequest().setEditor(tfEditor.getText());
        getSkinnable().getRequest().setDataRecordCorrectionDeadline(dpDataRecordCorrDeadlineDate.getDate());
        getSkinnable().getRequest().setPreliminaryProceedingsClosedDeadline(dpPrelProcClosedDeadlineDate.getDate());
        getSkinnable().getRequest().setPreTrialEndFl(ckPreTrialEnd.isSelected());
        getSkinnable().getRequest().setPreTrialEnd(dpPreTrialEnd.getDate());
        getSkinnable().getRequest().setSentOnFl(ckSentOn.isSelected());
        getSkinnable().getRequest().setSentOn(dpSentOn.getDate());
        getSkinnable().getRequest().setCaseDialogFl(ckCaseDialog.getControl().isSelected());
        getSkinnable().getRequest().setCaseDialogEndFl(ckCaseDialogEnd.getControl().isSelected());
        getSkinnable().getRequest().setRequestDate(dpRequest.getDate());
        getSkinnable().getRequest().setAnswerDeadline(dpPrelProcAnsDeadlineDate.getDate());
        getSkinnable().getRequest().setCaseDialogEndDate(dpCaseDialogEndDate.getDate());
        getSkinnable().getRequest().setCaseDialogBillCorrDeadline(dpCaseDialogBillCorrDeadlineDate.getDate());
        final Long internalId = cbStatus.getSelectedInternalId();
        getSkinnable().getRequest().setRequestState(internalId == null ? 0L : internalId);
        getSkinnable().getRequest().setComment(taUserComment.getText());//taAuditComment.getText());
        getSkinnable().getRequest().setUserComment(taAuditComment.getText());
        getSkinnable().getRequest().setInsuranceIdentifier(tfInsuIdent.getText());
        getSkinnable().getRequest().setStartAudit(dpDateOfProcessCreation.getDate());
        getSkinnable().getRequest().setReportDate(dpReportDate.getDate());
        getSkinnable().getRequest().setPreTrialState(ltgPeTrialState.getSelected().id);
        updateAuditReasons(ckcbAuditReasons,getSkinnable().getRequest());
        try {
            getSkinnable().setReminders(getAuditRequestReminders());
        } catch (NamingException ex) {
            Logger.getLogger(AuditRequestEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    private void updateAuditReasons(TWmRequestAudit request) {
//        request.getAuditReasons().clear();
//        ObservableList<Map.Entry<Long, String>> checked = ckcbAuditReasons.getCheckModel().getCheckedItems();
//        for (Map.Entry<Long, String> check : checked) {
//            Long key = check.getKey();
//            if (!request.containsAuditReason(key)) {
//                request.addAuditReason(key, false);
//            }
//        }
//
//    }
//
//    private void updateAuditReasonsData(TWmRequestAudit request) {
//        nextVal:for (TWmMdkAuditReasons reason : request.getAuditReasons(false)) {
//            for (Map.Entry<Long, String> item : ckcbAuditReasons.getItems()) {
//                if (item.getKey().equals(reason.getAuditReasonNumber())) {
//                    ckcbAuditReasons.getCheckModel().check(item);
//                    continue nextVal;
//                }
//            }
//            LOG.warning("Can not find entry for AuditReasonNumber: " + reason.getAuditReasonNumber() + "!\nWas the CaseType changed or was it deleted?");
//        }
//
//    }

    public List<TWmReminder> getAuditRequestReminders() throws NamingException {
        TWmRequest reqObject = getSkinnable().getRequest();
        final ArrayList<TWmReminder> remSet = new ArrayList<>();
        if (reqObject == null) {
            LOG.log(Level.SEVERE, "request object is null!");
            return remSet;
        }
        if (reqObject.isRequestAudit()) {
            if (ckLkDataRecordCorrDeadlineDate.isChecked()) {
                TWmReminder rem = new TWmReminder();
                setReminderData(rem, WmReminderTypeEn.DataRecordCorr);
                remSet.add(rem);
            }
            if (ckLkPrelProcAnsDeadlineDate.isChecked()) {
                TWmReminder rem = new TWmReminder();
                setReminderData(rem, WmReminderTypeEn.PrelProcAns);
                remSet.add(rem);
            }
            if (ckLkPrelProcClosedDeadlineDate.isChecked()) {
                TWmReminder rem = new TWmReminder();
                setReminderData(rem, WmReminderTypeEn.PrelProcClosed);
                remSet.add(rem);
            }
            if (ckLkCaseDialogBillCorrDeadlineDateRem.isChecked()) {
                TWmReminder rem = new TWmReminder();
                setReminderData(rem, WmReminderTypeEn.PrelProcBillCorr);
                remSet.add(rem);
            }
           return remSet;
        } else {
            return null;
        }
    }

    private void setReminderData(TWmReminder rem, WmReminderTypeEn remTyp) throws NamingException {
        if (remTyp == WmReminderTypeEn.DataRecordCorr) {
            rem.setCreationDate(new Date());
            rem.setCreationUser(Session.instance().getCpxUserId());
            rem.setAssignedUserId(wvDataRecordCorr.getAssUser());
            rem.setSubject(wvDataRecordCorr.getRemTypeInternalId());
            rem.setDueDate(wvDataRecordCorr.getDueTo() != null ? wvDataRecordCorr.getDueTo() : new Date());
            rem.setComment(wvDataRecordCorr.getComment());
            rem.setHighPrio(wvDataRecordCorr.getPrio());

        }
        if (remTyp == WmReminderTypeEn.PrelProcAns) {
            rem.setCreationUser(Session.instance().getCpxUserId());
            rem.setCreationDate(new Date());
            rem.setAssignedUserId(wvPrelProcAns.getAssUser());
            rem.setSubject(wvPrelProcAns.getRemTypeInternalId());
            rem.setDueDate(wvPrelProcAns.getDueTo() != null ? wvPrelProcAns.getDueTo() : new Date());
            rem.setComment(wvPrelProcAns.getComment());
            rem.setHighPrio(wvPrelProcAns.getPrio());
        }
        if (remTyp == WmReminderTypeEn.PrelProcClosed) {
            rem.setCreationUser(Session.instance().getCpxUserId());
            rem.setCreationDate(new Date());
            rem.setAssignedUserId(wvPrelProcClosed.getAssUser());
            rem.setSubject(wvPrelProcClosed.getRemTypeInternalId());
            rem.setDueDate(wvPrelProcClosed.getDueTo() != null ? wvPrelProcClosed.getDueTo() : new Date());
            rem.setComment(wvPrelProcClosed.getComment());
            rem.setHighPrio(wvPrelProcClosed.getPrio());

        }
        if (remTyp == WmReminderTypeEn.PrelProcBillCorr) {
            rem.setCreationUser(Session.instance().getCpxUserId());
            rem.setCreationDate(new Date());
            rem.setAssignedUserId(wvCaseDialogBillCorrection.getAssUser());
            rem.setSubject(wvCaseDialogBillCorrection.getRemTypeInternalId());
            rem.setDueDate(wvCaseDialogBillCorrection.getDueTo() != null ? wvCaseDialogBillCorrection.getDueTo() : new Date());
            rem.setComment(wvCaseDialogBillCorrection.getComment());
            rem.setHighPrio(wvCaseDialogBillCorrection.getPrio());
        }

    }

    @Override
    protected void disableControls(boolean armed) {
        tfInsuName.setDisable(armed);
        btnInsuSearch.setDisable(armed);
        tfInsuIdent.setDisable(armed);
        tfEditor.setDisable(armed);
        dpDateOfProcessCreation.setDisable(armed);
        dpDataRecordCorrDeadlineDate.setDisable(armed);
        dpPrelProcClosedDeadlineDate.setDisable(armed);
        ckPreTrialEnd.setDisable(armed);
        dpPreTrialEnd.setDisable(ckPreTrialEnd.isDisable() || !ckPreTrialEnd.isSelected());
        ckSentOn.setDisable(armed);
        dpSentOn.setDisable(ckSentOn.isDisable() || !ckSentOn.isSelected());
        lbCaseDialog.setDisable(armed);
        lbPreTrial.setDisable(armed);
        dpRequest.setDisable(armed);
        ckCaseDialog.setDisable(armed);
        ckCaseDialogEnd.setDisable(armed);
        tfZipCode.setDisable(armed);
        tfCity.setDisable(armed);
        tfAddress.setDisable(armed);
        tfTelePrefix.setDisable(armed);
        tfTelephone.setDisable(armed);
        tfFax.setDisable(armed);
        cbStatus.setDisable(armed);
        hbReminders.setDisable(armed);
        taAuditComment.setDisable(armed);
        taUserComment.setDisable(armed);
        ckLkDataRecordCorrDeadlineDate.setDisable(armed);
        ckLkPrelProcClosedDeadlineDate.setDisable(armed);
        ckLkPrelProcAnsDeadlineDate.setDisable(armed);
        ckLkCaseDialogBillCorrDeadlineDateRem.setDisable(armed);
        ltgPeTrialState.setDisable(armed);
        ckcbAuditReasons.setDisable(armed);
        dpReportDate.setDisable(armed);
        dpDataRecordCorrDeadlineDate.setDisable(armed);
        dpCaseDialogBillCorrDeadlineDate.setDisable(dpCaseDialogBillCorrDeadlineDate.getDate() != null );
    }

    private enum WmReminderTypeEn {

        DataRecordCorr, PrelProcAns, PrelProcClosed, PrelProcBillCorr;

    }

    private enum PreTrialStateEn {
        Ja(0, "JA"),
        Nein(1, "Nein"),
        Unbestimmt(2, "unbestimmt");

        private final long id;
        private final String langKey;

        private PreTrialStateEn(long id, String langKey) {
            this.id = id;
            this.langKey = langKey;
        }

        public String getLangKey() {
            return langKey;
        }

        public long getIdforLangKey() {
            return id;
        }

        public static PreTrialStateEn findById(final long pkey) {
            if (pkey == 0) {
                return Ja;
            }
            if (pkey == 1) {
                return Nein;
            }
            if (pkey == 2) {
                return Unbestimmt;
            }
            return Unbestimmt;
        }

    }

}
