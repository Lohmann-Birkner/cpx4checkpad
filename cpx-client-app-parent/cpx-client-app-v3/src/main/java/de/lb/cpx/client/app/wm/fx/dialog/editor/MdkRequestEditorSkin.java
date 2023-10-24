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
//import de.lb.cpx.client.app.wm.fx.dialog.editor.ReminderLabeledCheckboxLink.WmReminderTypeEn;
import de.lb.cpx.client.app.wm.fx.process.section.WmReminderSectionForRequest;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.menu.cache.MenuCacheOptionsEn;
import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxMdk;
import de.lb.cpx.client.core.model.catalog.CpxMdkCatalog;
import de.lb.cpx.client.core.model.fx.combobox.RequestStateCombobox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledCheckComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledDatePicker;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextArea;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.server.commonDB.model.CDeadline; 
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmReminder;
import de.lb.cpx.wm.model.TWmRequest;
import de.lb.cpx.wm.model.TWmRequestMdk;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import javax.naming.NamingException;

/**
 * Skinable Class to handle the MDK layout
 *
 * @author shahin
 */
public class MdkRequestEditorSkin extends BasicRequestEditorSkin<MdkRequestEditor, TWmRequestMdk> {

    private static final Logger LOG = Logger.getLogger(MdkRequestEditorSkin.class.getName());

    private CpxMdkCatalog mdkCatalog;
    private MdInfoTextField tfMdkName;
    private LabeledTextField tfEditor;
    private LabeledDatePicker dpReqCreationDate;
    private LabeledDatePicker dpMdkStartAuditDate;
    private LabeledDatePicker dpBillCorrectionDeadlineDate;
    private LabeledDatePicker dpMdkAuditCompletionDeadlineDate;
    private LabeledDatePicker dpMdkReportCreationDate;
    private LabeledDatePicker dpMdkReportReceiveDate;
    private RequestStateCombobox cbMdkStatus;
    private LabeledCheckComboBox<Map.Entry<Long, String>> ckcbAuditReasons;
    private LabeledCheckComboBox<Map.Entry<Long, String>> ckcbExtendedAuditReasons;
    private Label lbMdkDocuments;
    private LabeledDatePicker dpMdkStartAuditExtendedDate;
    private LabeledDatePicker dpInsuranceRecivedBillDate;
    private CheckBox ckMdkDocumentRequest;
    private LabeledDatePicker dpMdkDocumentRequestDate;
    private LabeledDatePicker dpMdkDocumentDeliverDeadlineDate;
    private LabeledDatePicker ckMdkDocumentDeliveredDate;

    private CheckBox ckMdkDocumentDelivered;
    private CheckBox ckMdkReport;
    private CheckBox ckMdkFeePaid;
    private LabeledDatePicker dpMdkFeePaidDate;
    private ReminderLabeledCheckboxLink cklinkkWvDocDeliDead;
    private ReminderLabeledCheckboxLink ckWvFeePaid;
    private ReminderLabeledCheckboxLink ckWvSubseqProc;
    private CheckBox mdkSubseqProcCheckBox;
    private LabeledDatePicker dpMdkSubseqProcDate;
    private LabeledTextArea taMdkComment;
    private LabeledTextArea taUserComment;
//    private Button btnMdkSearch;
//    private LabeledTextField mdkDepartment;
//    private LabeledTextField zipCode;
//    private LabeledTextField address;
//    private LabeledTextField emailAdress;
//    private LabeledTextField areaCode;
//    private LabeledTextField telephone;
//    private LabeledTextField fax;
//    private ReminderComponents wvDocDeliDead;
//    private ReminderComponents wvFeePaid;
//    private ReminderComponents wvSubseqProc;
    private HBox hbMdkReminders;
//    private DeadlineList listofDeadlines;
    private WmReminderSectionForRequest wmMdkRemSection;
//    private TextTemplateController textTemplateController;

    public MdkRequestEditorSkin(MdkRequestEditor pSkinnable) throws IOException {
        super(pSkinnable, "/fxml/MDKRequestEditorFXML.fxml");
    }

    @Override
    @SuppressWarnings("unchecked")
    protected final void setUpNodes() {

        tfMdkName = (MdInfoTextField) lookUpInRoot("#tfMdkName");
        tfEditor = (LabeledTextField) lookUpInRoot("#tfEditor");
        
//        if(getSkinnable().getProcessServiceFacade() != null && getSkinnable().getCase() != null){
//            textTemplateController = new TextTemplateController(TextTemplateTypeEn.RequestContext, getSkinnable().getProcessServiceFacade(), getWindow(), getSkinnable().getCase());
//        }
        // for the TextField, use custom TextFieldSkin
//        TextFieldSkin customContextSkin = new TextTemplateController(TextTemplateTypeEn.RequestContext).customContextSkin(tfEditor.getControl());
        /*     TextFieldSkin customContextSkin = textTemplateController.customContextSkin(tfEditor.getControl());
        tfEditor.getControl().setSkin(customContextSkin);
         */
        dpReqCreationDate = (LabeledDatePicker) lookUpInRoot("#dpReqCreationDate");
        dpReqCreationDate.setDate(new Date());

        dpMdkStartAuditDate = (LabeledDatePicker) lookUpInRoot("#dpMdkStartAuditDate");
        dpBillCorrectionDeadlineDate = (LabeledDatePicker) lookUpInRoot("#dpBillCorrectionDeadlineDate");
        dpMdkAuditCompletionDeadlineDate = (LabeledDatePicker) lookUpInRoot("#dpMdkAuditCompletionDeadlineDate");
        dpMdkReportCreationDate = (LabeledDatePicker) lookUpInRoot("#dpMdkReportCreationDate");
        dpMdkReportReceiveDate = (LabeledDatePicker) lookUpInRoot("#dpMdkReportReceiveDate");
        cbMdkStatus = (RequestStateCombobox) lookUpInRoot("#cbMdkStatus");
        lbMdkDocuments = (Label) lookUpInRoot("#lbMdkDocuments");
//        ckcbAuditReasons = (LabeledCheckComboBox<CMdkAuditreason> ) lookUpInRoot("#ckcbAuditReasons");
//        ckcbExtendedAuditReasons = (LabeledCheckComboBox<CMdkAuditreason> ) lookUpInRoot("#ckcbExtendedAuditReasons");
        ckcbExtendedAuditReasons = (LabeledCheckComboBox) lookUpInRoot("#ckcbExtendedAuditReasons");
//        Set<Map.Entry<Long, String>> list = MenuCache.instance().getMdkAuditReasonsEntries();
//        Set<Map.Entry<Long, String>> list = MenuCache.instance().getMdkAuditReasonsEntries(dpReqCreationDate.getDate());
        Set<Map.Entry<Long, String>> list = MenuCache.instance().getAuditReasonsEntries(getSkinnable().getCase()!=null?getSkinnable().getCase().getCsCaseTypeEn():null); //getSkinnable().getProcessServiceFacade().getValidUndeletedAuditReasonsEntries();
        ckcbExtendedAuditReasons.getItems().addAll(new ArrayList<>(list));
        ckcbExtendedAuditReasons.getControl().setConverter(new StringConverter<Map.Entry<Long, String>>() {
            @Override
            public String toString(Map.Entry<Long, String> t) {
                return t == null ? "" : t.getValue();
            }

            @Override
            public Map.Entry<Long, String> fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        ckcbAuditReasons = (LabeledCheckComboBox) lookUpInRoot("#ckcbAuditReasons");
        ckcbAuditReasons.getItems().addAll(new ArrayList<>(list));
        ckcbAuditReasons.getControl().setConverter(new StringConverter<Map.Entry<Long, String>>() {
            @Override
            public String toString(Map.Entry<Long, String> t) {
                return t == null ? "" : t.getValue();
            }

            @Override
            public Map.Entry<Long, String> fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        dpMdkStartAuditExtendedDate = (LabeledDatePicker) lookUpInRoot("#dpMdkStartAuditExtendedDate");
        dpInsuranceRecivedBillDate = (LabeledDatePicker) lookUpInRoot("#dpInsuranceRecivedBillDate");
        ckMdkDocumentRequest = (CheckBox) lookUpInRoot("#ckMdkDocumentRequest");
        dpMdkDocumentRequestDate = (LabeledDatePicker) lookUpInRoot("#dpMdkDocumentRequestDate");
        dpMdkDocumentRequestDate.setDisable(true);
        dpMdkDocumentDeliverDeadlineDate = (LabeledDatePicker) lookUpInRoot("#dpMdkDocumentDeliverDeadlineDate");
        ckMdkDocumentDeliveredDate = (LabeledDatePicker) lookUpInRoot("#ckMdkDocumentDeliveredDate");
        cklinkkWvDocDeliDead = (ReminderLabeledCheckboxLink) lookUpInRoot("#cklinkkWvDocDeliDead");
        cklinkkWvDocDeliDead.setVisible(Session.instance().getRoleProperties().isEditReminderAllowed());
        ckMdkDocumentDelivered = (CheckBox) lookUpInRoot("#ckMdkDocumentDelivered");
        ckMdkReport = (CheckBox) lookUpInRoot("#ckMdkReport");
        ckMdkFeePaid = (CheckBox) lookUpInRoot("#ckMdkFeePaid");
        dpMdkFeePaidDate = (LabeledDatePicker) lookUpInRoot("#dpMdkFeePaidDate");
        ckWvFeePaid = (ReminderLabeledCheckboxLink) lookUpInRoot("#ckWvFeePaid");
        ckWvFeePaid.setVisible(Session.instance().getRoleProperties().isEditReminderAllowed());
        ckWvSubseqProc = (ReminderLabeledCheckboxLink) lookUpInRoot("#ckWvSubseqProc");
        ckWvSubseqProc.setVisible(Session.instance().getRoleProperties().isEditReminderAllowed());
        mdkSubseqProcCheckBox = (CheckBox) lookUpInRoot("#mdkSubseqProcCheckBox");
        dpMdkSubseqProcDate = (LabeledDatePicker) lookUpInRoot("#dpMdkSubseqProcDate");

        taMdkComment = (LabeledTextArea) lookUpInRoot("#taMdkComment");
        addTextTemplateController(taMdkComment);
        //add texttemplate custom MenuItem to the ContextMenu
//        TextAreaSkin customContextSkinMC = new TextTemplateController(TextTemplateTypeEn.RequestContext).customContextSkin(taMdkComment.getControl());
//        if(textTemplateController != null){
//            TextAreaSkin customContextSkinMC = textTemplateController.customContextSkin(taMdkComment.getControl());
//            taMdkComment.getControl().setSkin(customContextSkinMC);
//        }
        taUserComment = (LabeledTextArea) lookUpInRoot("#taUserComment");
        addTextTemplateController(taUserComment);
//        TextAreaSkin customContextSkinUC = new TextTemplateController(TextTemplateTypeEn.RequestContext).customContextSkin(taUserComment.getControl());
//        if(textTemplateController != null){
//            TextAreaSkin customContextSkinUC = textTemplateController.customContextSkin(taUserComment.getControl());
//            taUserComment.getControl().setSkin(customContextSkinUC);
//        }
        dpMdkFeePaidDate.setDisable(true);
        dpMdkSubseqProcDate.setDisable(true);
//        btnMdkSearch = new Button();//(Button) lookUpInRoot("#btnMdkSearch");
//        tfMdkName.setAdditionalButton(btnMdkSearch);
//        //listofCMdkStatus = (List<CWmListMdkState>) lookUpInRoot("#listofCMdkStatus");
        mdkCatalog = getSkinnable().getMdkCatalog();
        tfMdkName.setMdkCatalog(mdkCatalog);
//        mdkDepartment = new LabeledTextField(Lang.getMdkDepartment());
//        mdkDepartment.setPrefWidth(300);
//        mdkDepartment.setSpacing(5.0);
//        zipCode = new LabeledTextField(Lang.getMdkAreaCode());
//        zipCode.setSpacing(5.0);
//        address = new LabeledTextField(Lang.getMdkAddress());
//        address.setSpacing(5.0);
//        emailAdress = new LabeledTextField(Lang.getMdkEmail());
//        emailAdress.setSpacing(5.0);
//        areaCode = new LabeledTextField(Lang.getMdkTelephoneAreaCode());
//        areaCode.setSpacing(5.0);
//        telephone = new LabeledTextField(Lang.getMdkTelephone());
//        telephone.setSpacing(5.0);
//        fax = new LabeledTextField(Lang.getMdkFax());
//        fax.setSpacing(5.0);
        hbMdkReminders = (HBox) lookUpInRoot("#hbMdkReminders");
//        Set<Map.Entry<Long, String>> reminderSubject = MenuCache.instance().getReminderSubjectEntries();
//        wvDocDeliDead = new ReminderComponents();
//        wvFeePaid = new ReminderComponents();
//        wvSubseqProc = new ReminderComponents();
        //cbMdkStatus.setItems(Session.instance().getEjbConnector().connectProcessServiceBean().get().getAllMdkStatesObjects(new Date()));
        cbMdkStatus.setItems(MenuCache.getMenuCacheRequestStates().values(new Date(), MenuCacheOptionsEn.IGNORE_INACTIVE));
        if (getSkinnable().getRequest().getId() != 0) {
            //Editing an existing request
            if(getSkinnable().getProcessServiceFacade() != null){
                if (!getSkinnable().getProcessServiceFacade().getRemindersForRequest(getSkinnable().getRequest().getId()).isEmpty()) {
                    wmMdkRemSection = new WmReminderSectionForRequest(getSkinnable().getProcessServiceFacade(), getSkinnable().getRequest());
                    hbMdkReminders.getChildren().addAll(wmMdkRemSection.getRoot());
                }
            }
        } else {
            //Creating a new request
            //restore recent used MDK for a new request
            final Long mdkInternalId = Session.instance().getRecentMdkInternalId();
            getSkinnable().getRequest().setMdkInternalId(mdkInternalId);
        }
//        listofDeadlines = new DeadlineList(Session.instance().getEjbConnector().connectProcessServiceBean().get().getDeadlines(getSkinnable().getCaseAdmissionDate()));
    }

    @Override
    protected final void setUpLanguage() {
        tfMdkName.setTitle(Lang.getMdkName());
        tfEditor.setTitle(Lang.getMdkEditor());
//        btnMdkSearch.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.SEARCH_PLUS));
//        btnMdkSearch.getStyleClass().add("cpx-icon-button");
//        btnMdkSearch.setText("");

        lbMdkDocuments.setText(Lang.getMdkDocuments());
        dpMdkAuditCompletionDeadlineDate.setTitle(Lang.getMdkAuditCompletionDeadline());
        dpMdkAuditCompletionDeadlineDate.setDisable(true);
        dpReqCreationDate.setTitle(Lang.getMdkProcessStartDate()); // process creation date
        dpMdkStartAuditDate.setTitle(Lang.getMdkStartAudit());
        dpBillCorrectionDeadlineDate.setTitle(Lang.getMdkBillCorrectionDeadline());
        dpBillCorrectionDeadlineDate.setDisable(true);
        dpMdkReportReceiveDate.setTitle(Lang.getMdkReportReceiveDate());
        dpMdkReportReceiveDate.setDisable(true);
        dpMdkReportCreationDate.setTitle(Lang.getMdkReportCreationDate());
        dpMdkReportCreationDate.setDisable(true);
        dpMdkStartAuditExtendedDate.setTitle(Lang.getMdkStartAuditExtended());
        dpInsuranceRecivedBillDate.setTitle(Lang.getInsuranceBillRecived());
        dpMdkDocumentRequestDate.setTitle(Lang.getMdkDocumentRequest());
        ckMdkDocumentDeliveredDate.setTitle(Lang.getMdkDocumentDelivered());
        ckMdkDocumentDeliveredDate.setDisable(true);
        dpMdkDocumentDeliverDeadlineDate.setTitle(Lang.getMdkDeadlineForSubmission());
        dpMdkDocumentDeliverDeadlineDate.setDisable(true);
        dpMdkFeePaidDate.setTitle("Fort bezahlt am");    // the date when MDK asks for where is document.
        dpMdkSubseqProcDate.setTitle("Nachverfahren");    // the date when MDK asks for where is document.
        ckMdkDocumentRequest.setText("");
        ckMdkDocumentDelivered.setText("");
        ckMdkDocumentDelivered.setDisable(true);
        ckMdkReport.setText("");
        ckMdkReport.setDisable(false);
        cklinkkWvDocDeliDead.setTitle("Wiedervorlage");
        cklinkkWvDocDeliDead.getControl().setText("öffnen..");
        cklinkkWvDocDeliDead.setDisable(true);
        ckMdkFeePaid.setText("");
        ckMdkFeePaid.setDisable(true);
        ckWvFeePaid.getControl().setText("öffnen..");
        ckWvFeePaid.setTitle("Wiedervorlage");
        ckWvSubseqProc.getControl().setText("öffnen..");
        ckWvFeePaid.setDisable(true);
        ckWvSubseqProc.setTitle("Wiedervorlage");
        mdkSubseqProcCheckBox.setText("");
        ckWvSubseqProc.setDisable(true);
        taMdkComment.setTitle(Lang.getMdkComment());
        taUserComment.setTitle(Lang.getMdkUserComment());
        ckcbAuditReasons.setTitle(Lang.getMdkAuditReasons());
        ckcbExtendedAuditReasons.setTitle(Lang.getMdkExtendedAuditReasons());
//        btnMdkSearch.setTooltip(new Tooltip(Lang.getMdkInformations()));
        dpMdkAuditCompletionDeadlineDate.setTooltip(new Tooltip(Lang.getMdkAuditCompletionDeadline()));
        dpReqCreationDate.setTooltip(new Tooltip(Lang.getMdkProcessStartDate()));
        dpMdkStartAuditDate.setTooltip(new Tooltip(Lang.getMdkStartAudit()));
        dpBillCorrectionDeadlineDate.setTooltip(new Tooltip(Lang.getMdkBillCorrectionDeadline()));
        dpMdkReportReceiveDate.setTooltip(new Tooltip(Lang.getMdkReportReceiveDate()));
        dpMdkReportCreationDate.setTooltip(new Tooltip(Lang.getMdkReportCreationDate()));
        dpMdkStartAuditExtendedDate.setTooltip(new Tooltip(Lang.getMdkStartAuditExtended()));
        Tooltip.install(cklinkkWvDocDeliDead, new Tooltip("Wiedervorlage bearbeiten"));
        cbMdkStatus.setTitle(Lang.getMdkStatus());

    }

    @Override
    public void setUpdateListeners() {
        super.setUpdateListeners();
        tfMdkName.setUpdateListeners();
        cklinkkWvDocDeliDead.setupListeners();
        ckWvFeePaid.setupListeners();
        ckWvSubseqProc.setupListeners();

//        btnMdkSearch.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                VBox vBox = new VBox(8.0d);
//                vBox.getChildren().addAll(mdkDepartment, zipCode, address, emailAdress, areaCode, telephone, fax);
//                PopOver popover = showInfoPopover(vBox);
//                popover.setOnShowing(new EventHandler<WindowEvent>() {
//                    @Override
//                    public void handle(WindowEvent t) {
//                        tfMdkName.setShowCaret(false);
//                    }
//                });
//                popover.setOnHiding(new EventHandler<WindowEvent>() {
//                    @Override
//                    public void handle(WindowEvent t) {
//                        tfMdkName.setShowCaret(true);
//                    }
//                });
//                popover.show(btnMdkSearch);
////                showInfoPopover(vBox).show(btnMdkSearch);
//            }
//
//        });
        if (getSkinnable().getRequest() != null) {
            //if (getSkinnable().getRequest().getId() != 0L) {
            final Long mdkInternalId = getSkinnable().getRequest().getMdkInternalId();
            if (mdkInternalId != null) {
                updateCatalogValues(getSkinnable().getMdkCatalog().getByCode(String.valueOf(mdkInternalId), AbstractCpxCatalog.DEFAULT_COUNTRY));
            }
        }
//        getSkinnable().mdkCatalogProperty().addListener(new ChangeListener<CpxMdkCatalog>() {
//            @Override
//            public void changed(ObservableValue<? extends CpxMdkCatalog> observable, CpxMdkCatalog oldValue, CpxMdkCatalog newValue) {
//                if (newValue == null) {
//                    return;
//                }
//                updateCatalogValues(newValue.getByCode(String.valueOf(getSkinnable().getRequest().getMdkInternalId()), AbstractCpxCatalog.DEFAULT_COUNTRY));
//            }
//        });

//        tfMdkName.getControl().focusedProperty().addListener((arg0, oldValue, newValue) -> {
//            if (!newValue) { //when focus lost
//                if (!StringUtils.trimToEmpty(tfMdkName.getText()).isEmpty()) {
//                    if (mdkCatalog.getByFullMdkName(tfMdkName.getText(), AbstractCpxCatalog.DEFAULT_COUNTRY).getMdkName() == null) {
//                        String error = Lang.getMdkNameValidate();
//                        LOG.log(Level.WARNING, error);
//                    }
//                }
//            }
//        });

//        tfEditor.getControl().textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                if (tfEditor.getControl().getText() != null && !tfEditor.getControl().getText().isEmpty() && tfEditor.getControl().getText().length() > 49) {
//                    MainApp.showInfoMessageDialog("Bearbeiter Feld darf maximal 50 Zeichen enthalten.", getWindow());
//                }
//            }
//        });
//avoid NPE with Case have no  Deadlines , listofDeadlines initialization in setUpNodes() 
//        listofDeadlines = new DeadlineList(Session.instance().getEjbConnector().connectProcessServiceBean().get().getDeadlines(getSkinnable().getCaseAdmissionDate()));
        final CDeadline billCorrectionDeadline = MenuCache.getMenuCacheDeadlines().getBcd(getSkinnable().getCaseAdmissionDate());
        final CDeadline auditCompletionDeadline = MenuCache.getMenuCacheDeadlines().getAcd(getSkinnable().getCaseAdmissionDate());
        dpMdkStartAuditDate.getControl().valueProperty().addListener((ov, oldValue, newValue) -> {
            if (billCorrectionDeadline != null) {
                final Date bcdDeadlineDt = billCorrectionDeadline.calculateDeadlineExtern(dpMdkStartAuditDate.getDate());
                dpBillCorrectionDeadlineDate.setDate(bcdDeadlineDt);
                dpBillCorrectionDeadlineDate.setDisable(true);
            } else {
                MainApp.showWarningMessageDialog(Lang.getMdkBillCorrectionDeadlineError(), getWindow());
                dpBillCorrectionDeadlineDate.setDisable(false);
            }

            if (auditCompletionDeadline != null) {
                //4 Frist Abschluss MDK-Prüfgrund
                final Date acdDeadlineDt = auditCompletionDeadline.calculateDeadlineExtern(dpMdkStartAuditDate.getDate());
                dpMdkAuditCompletionDeadlineDate.setDate(acdDeadlineDt);
                dpMdkAuditCompletionDeadlineDate.setDisable(true);
            } else {
                MainApp.showWarningMessageDialog(Lang.getMdkAuditCompletionDeadlineError(), getWindow());
                dpMdkAuditCompletionDeadlineDate.setDisable(false);
            }
        });
        //CPX-1965
        dpMdkStartAuditExtendedDate.getControl().valueProperty().addListener((ov, oldValue, newValue) -> {
            if (auditCompletionDeadline != null) {
                //4 Frist Abschluss MDK-Prüfgrund
                final Date acdDeadlineDt = auditCompletionDeadline.calculateDeadlineExtern(dpMdkStartAuditExtendedDate.getDate());
                dpMdkAuditCompletionDeadlineDate.setDate(acdDeadlineDt);
                dpMdkAuditCompletionDeadlineDate.setDisable(true);
            } else {
                MainApp.showWarningMessageDialog(Lang.getMdkAuditCompletionDeadlineError(), getWindow());
                dpMdkAuditCompletionDeadlineDate.setDisable(false);
            }
        });
        ckcbAuditReasons.registerTooltip();
        ckcbExtendedAuditReasons.getControl().getCheckModel().getCheckedItems().addListener(new ListChangeListener<Map.Entry<Long, String>>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Map.Entry<Long, String>> c) {
                if (getSkinnable().getRequest() == null) {
                    dpMdkStartAuditExtendedDate.setDisable(false);
                    if (ckcbExtendedAuditReasons.getControl().getCheckModel().getCheckedItems().isEmpty()) {
                        dpMdkStartAuditExtendedDate.setDisable(true);
                    }
                } else if (getSkinnable().getRequest() != null) {
                    dpMdkStartAuditExtendedDate.setDisable(false);
                    if (ckcbExtendedAuditReasons.getControl().getCheckModel().getCheckedItems().isEmpty()) {
                        dpMdkStartAuditExtendedDate.setDisable(true);
                    }
//                    List<Map.Entry<Integer, String>> listDistinct = ckcbExtendedAuditReasons.getCheckModel().getCheckedItems().stream().distinct().collect(Collectors.toList());
//                    ErprTooltip.setText(listDistinct.toString().substring(1, listDistinct.toString().length() - 1));
                }
            }
        });
        ckcbExtendedAuditReasons.registerTooltip();
//        cklinkkWvDocDeliDead.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                wvDocDeliDead.showWvPopover(cklinkkWvDocDeliDead);
//            }
//        });
//
//        ckWvFeePaid.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                wvFeePaid.showWvPopover(ckWvFeePaid);
//
//            }
//
//        });
//
//        ckWvSubseqProc.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                wvSubseqProc.showWvPopover(ckWvSubseqProc);
//            }
//        });

        ckMdkDocumentDelivered.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                if (ckMdkDocumentDelivered.isSelected()) {
                    ckMdkDocumentDeliveredDate.setDisable(false);
                } else {
                    ckMdkDocumentDeliveredDate.setDisable(true);
                    ckMdkDocumentDeliveredDate.setDate(null);
                }
            }
        });

        //CPX-1381
        ckMdkReport.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                if (ckMdkReport.isSelected()) {
                    dpMdkReportCreationDate.setDisable(false);
                    dpMdkReportReceiveDate.setDisable(false);
                } else {
                    dpMdkReportCreationDate.setDisable(true);
                    dpMdkReportReceiveDate.setDisable(true);
                }
            }
        });

//        mdkDepartment.getControl().focusedProperty().addListener((arg0, oldValue, newValue) -> {
//            if (!newValue) { //when focus lost
//                if (!StringUtils.trimToEmpty(mdkDepartment.getText()).isEmpty()) {
//                    if (mdkCatalog.getByMDKDienststelle(mdkDepartment.getText(), AbstractCpxCatalog.DEFAULT_COUNTRY).getMdkDepartment() == null) {
//                        String error = Lang.getMdkDepartmentValidate();
//                        LOG.log(Level.WARNING, error);
//                    }
//                }
//            }
//        });
//
//        zipCode.getControl().focusedProperty().addListener((arg0, oldValue, newValue) -> {
//            if (!newValue) { //when focus lost
//                if (!StringUtils.trimToEmpty(zipCode.getText()).isEmpty()) {
//                    if (mdkCatalog.getByMDKPostleitzahl(zipCode.getText(), AbstractCpxCatalog.DEFAULT_COUNTRY).getMdkZipCode() == null) {
//                        String error = Lang.getMdkZipCodeValidate();
//                        LOG.log(Level.WARNING, error);
//                    }
//                }
//            }
//        });

        ckMdkDocumentRequest.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                if (ckMdkDocumentRequest.isSelected()) {
                    dpMdkDocumentRequestDate.setDisable(false);
                    ckMdkFeePaid.setDisable(false);
                    ckMdkDocumentDelivered.setDisable(dpMdkDocumentRequestDate.getDate() == null);
                    ckMdkDocumentDeliveredDate.setDisable(ckMdkDocumentDelivered.isDisabled());
                    cklinkkWvDocDeliDead.setDisable(false);

                } else {
                    dpMdkDocumentRequestDate.setDisable(true);
                    ckMdkFeePaid.setDisable(true);
                    ckMdkFeePaid.setSelected(false);
                    ckMdkDocumentDelivered.setDisable(true);
                    ckMdkDocumentDelivered.setSelected(false);
                    ckMdkDocumentDeliveredDate.setDisable(true);
                    cklinkkWvDocDeliDead.setDisable(true);
                    dpMdkDocumentRequestDate.setDate(null);
                    ckMdkDocumentDeliveredDate.setDate(null);
                    dpMdkFeePaidDate.setDate(null);

                }
            }
        });

        final CDeadline proposalSubsequentProceedingsDeadline = MenuCache.getMenuCacheDeadlines().getPspd(getSkinnable().getCaseAdmissionDate());
        dpMdkReportReceiveDate.getControl().valueProperty().addListener((ov, oldValue, newValue) -> {
            if (proposalSubsequentProceedingsDeadline != null) {
                final Date pspdDeadlineDt = proposalSubsequentProceedingsDeadline.calculateDeadlineExtern(dpMdkReportReceiveDate.getDate());
                dpMdkSubseqProcDate.setDate(pspdDeadlineDt);
//                mdkSubseqProcCheckBox.setSelected(true);
//                dpMdkSubseqProcDate.setDisable(false);
            } else {
                MainApp.showWarningMessageDialog(Lang.getMdkProposalSubsequentProceedingsDeadline(), getWindow());
                dpMdkSubseqProcDate.setDisable(true);
                mdkSubseqProcCheckBox.setSelected(false);
            }
        });

        final CDeadline documentDeliveryDeadline = MenuCache.getMenuCacheDeadlines().getDdd(getSkinnable().getCaseAdmissionDate());
        final CDeadline continuationFeeDeadline = MenuCache.getMenuCacheDeadlines().getCfd(getSkinnable().getCaseAdmissionDate());
        // this listner set the date of Abgabefrist bis based on some calculations.
        dpMdkDocumentRequestDate.getControl().valueProperty().addListener((ov, oldValue, newValue) -> {
            final Date date = dpMdkDocumentRequestDate.getDate();
            ckMdkDocumentDelivered.setDisable(dpMdkDocumentRequestDate.getDate() == null);
            ckMdkDocumentDeliveredDate.setDisable(!ckMdkDocumentDelivered.isSelected());

            if (ckMdkFeePaid.isSelected()) {
                if (documentDeliveryDeadline != null && continuationFeeDeadline != null) {
                    final Date deadlineDt1 = documentDeliveryDeadline.calculateDeadlineExtern(date);
                    final Date deadlineDt2 = continuationFeeDeadline.calculateDeadlineExtern(deadlineDt1);

                    dpMdkDocumentDeliverDeadlineDate.setDate(deadlineDt2);
                    dpMdkDocumentDeliverDeadlineDate.setDisable(true);
                } else {
                    if (documentDeliveryDeadline == null && continuationFeeDeadline != null) {
                        MainApp.showWarningMessageDialog(Lang.getMdkDocumentDeliverDeadlineError(), getWindow());
                    }
                    if (continuationFeeDeadline == null && documentDeliveryDeadline != null) {
                        MainApp.showWarningMessageDialog(Lang.getMdkContinuationFeeDeadlineError(), getWindow());
                    }
                    if (documentDeliveryDeadline == null && continuationFeeDeadline == null) {
                        MainApp.showWarningMessageDialog(Lang.getMdkDocumentDeliverAndContinuationFeeDeadlinesError(), getWindow());
                    }
                    dpMdkDocumentDeliverDeadlineDate.setDisable(false);
                }
            }

            if (ckMdkFeePaid.isSelected() == false) {
                if (documentDeliveryDeadline != null) {
                    final Date deadlineDt = documentDeliveryDeadline.calculateDeadlineExtern(date);
                    final Date remDeadlineDt = documentDeliveryDeadline.calculateDeadline(date);
                    dpMdkDocumentDeliverDeadlineDate.setDate(deadlineDt);
                    dpMdkDocumentDeliverDeadlineDate.setDisable(true);
                    cklinkkWvDocDeliDead.setReminderDueTo(remDeadlineDt, documentDeliveryDeadline.getDlReminderType());
//                    wvDocDeliDead.setDueTo(remDeadlineDt);
//                    if (wvDocDeliDead.getRemTypeInternalId() == 0L) {
//                        wvDocDeliDead.setRemType(documentDeliveryDeadline.getDlReminderType());
//                    }
                } else {
                    MainApp.showWarningMessageDialog(Lang.getMdkDocumentDeliverDeadlineError(), getWindow());
                    dpMdkDocumentDeliverDeadlineDate.setDisable(false);
                }
            }
        });

        // this listener is used to set the date of Abgabefrist bis based on Fortsetzungpauschale gezahlt.
        ckMdkFeePaid.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                final Date date = dpMdkDocumentRequestDate.getDate();
                ckWvFeePaid.setReminderDueTo(continuationFeeDeadline != null?continuationFeeDeadline.calculateDeadline(date):dpMdkFeePaidDate.getDate(),
                        continuationFeeDeadline == null?null:continuationFeeDeadline.getDlReminderType());
//                wvFeePaid.setDueTo(dpMdkFeePaidDate.getDate());
//                if (continuationFeeDeadline != null) {
//                    final Date deadlineDt = continuationFeeDeadline.calculateDeadline(date);
//                    wvFeePaid.setDueTo(deadlineDt);
//                    if (wvFeePaid.getRemTypeInternalId() == 0L) {
//                        wvFeePaid.setRemType(continuationFeeDeadline.getDlReminderType());
//                    }
//                }
                if (date != null) {
                    if (ckMdkFeePaid.isSelected()) {
                        if (documentDeliveryDeadline != null && continuationFeeDeadline != null) {
                            final Date deadlineDddDt = documentDeliveryDeadline.calculateDeadlineExtern(date);
                            final Date deadlineCfdDt = continuationFeeDeadline.calculateDeadlineExtern(deadlineDddDt);
                            final Date remDeadlineCfdDt = continuationFeeDeadline.calculateDeadline(deadlineDddDt);

                            dpMdkDocumentDeliverDeadlineDate.setDate(deadlineCfdDt);
//                            wvDocDeliDead.setDueTo(remDeadlineCfdDt);
//                            if (wvDocDeliDead.getRemTypeInternalId() == 0L) {
//                                wvDocDeliDead.setRemType(documentDeliveryDeadline.getDlReminderType());
//                            }
                            cklinkkWvDocDeliDead.setReminderDueTo(remDeadlineCfdDt, documentDeliveryDeadline.getDlReminderType());
                            dpMdkDocumentDeliverDeadlineDate.setDisable(true);
                        } else {
                            if (documentDeliveryDeadline == null && continuationFeeDeadline != null) {
                                MainApp.showWarningMessageDialog(Lang.getMdkDocumentDeliverDeadlineError(), getWindow());
                            }
                            if (continuationFeeDeadline == null && documentDeliveryDeadline != null) {
                                MainApp.showWarningMessageDialog(Lang.getMdkContinuationFeeDeadlineError(), getWindow());
                            }
                            if (documentDeliveryDeadline == null && continuationFeeDeadline == null) {
                                MainApp.showWarningMessageDialog(Lang.getMdkDocumentDeliverAndContinuationFeeDeadlinesError(), getWindow());
                            }
                            dpMdkDocumentDeliverDeadlineDate.setDisable(false);
                        }
                    }

                    if (ckMdkFeePaid.isSelected() == false) {
                        if (documentDeliveryDeadline != null) {
                            final Date deadlineDt = documentDeliveryDeadline.calculateDeadlineExtern(date);
                            final Date remDeadlineDt = documentDeliveryDeadline.calculateDeadline(date);

                            dpMdkDocumentDeliverDeadlineDate.setDate(deadlineDt);
//                            wvDocDeliDead.setDueTo(remDeadlineDt);
//                            if (wvDocDeliDead.getRemTypeInternalId() == 0L) {
//                                wvDocDeliDead.setRemType(documentDeliveryDeadline.getDlReminderType());
//                            }
                            cklinkkWvDocDeliDead.setReminderDueTo(remDeadlineDt, documentDeliveryDeadline.getDlReminderType());
                            dpMdkDocumentDeliverDeadlineDate.setDisable(true);
                        } else {
                            MainApp.showWarningMessageDialog(Lang.getMdkDocumentDeliverDeadlineError(), getWindow());
                            dpMdkDocumentDeliverDeadlineDate.setDisable(false);
                        }
                    }
                }
            }
        });

        dpMdkFeePaidDate.getControl().valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                final Date date = dpMdkFeePaidDate.getDate();
//                wvFeePaid.setDueTo(date);
//                if (continuationFeeDeadline != null) {
//                    final Date remDeadlineDt = continuationFeeDeadline.calculateDeadline(date);
//                    wvFeePaid.setDueTo(remDeadlineDt);
//                    if (wvFeePaid.getRemTypeInternalId() == 0L) {
//                        wvFeePaid.setRemType(continuationFeeDeadline.getDlReminderType());
//                    }
//                }
                ckWvFeePaid.setReminderDueTo(continuationFeeDeadline != null?continuationFeeDeadline.calculateDeadline(date):date, 
                        continuationFeeDeadline == null?null:continuationFeeDeadline.getDlReminderType());
            }
        });

        ckMdkFeePaid.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                if (ckMdkFeePaid.isSelected()) {
                    dpMdkFeePaidDate.setDisable(false);
                    ckWvFeePaid.setDisable(false);

                } else {
                    dpMdkFeePaidDate.setDisable(true);
                    dpMdkFeePaidDate.setDate(null);
                    ckWvFeePaid.setDisable(true);
                }
            }
        });

        mdkSubseqProcCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                if (mdkSubseqProcCheckBox.isSelected()) {
                    dpMdkSubseqProcDate.setDisable(false);
                    ckWvSubseqProc.setDisable(false);
                } else {
                    dpMdkSubseqProcDate.setDisable(true);
                    dpMdkSubseqProcDate.setDate(null);
                    ckWvSubseqProc.setDisable(false);
                }
            }
        });

        dpMdkSubseqProcDate.getControl().valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                final Date date = dpMdkReportReceiveDate.getDate();
//                wvSubseqProc.setDueTo(dpMdkSubseqProcDate.getDate());
//                if (proposalSubsequentProceedingsDeadline != null) {
//                    final Date deadlineDt = proposalSubsequentProceedingsDeadline.calculateDeadline(date);
//                    wvSubseqProc.setDueTo(deadlineDt);
//                    if (wvSubseqProc.getRemTypeInternalId() == 0L) {
//                        wvSubseqProc.setRemType(proposalSubsequentProceedingsDeadline.getDlReminderType());
//                    }
//                }
                ckWvSubseqProc.setReminderDueTo(proposalSubsequentProceedingsDeadline != null?proposalSubsequentProceedingsDeadline.calculateDeadline(date):dpMdkSubseqProcDate.getDate(), 
                        proposalSubsequentProceedingsDeadline == null?null:proposalSubsequentProceedingsDeadline.getDlReminderType());
            }
        });

//        address.getControl().focusedProperty().addListener((arg0, oldValue, newValue) -> {
//            if (!newValue) { //when focus lost
//                if (!address.getText().isEmpty()) {
//                    if (mdkCatalog.getByAnschrift(address.getText(), AbstractCpxCatalog.DEFAULT_COUNTRY).getMdkStreet() == null) {
//                        String error = Lang.getMdkAddressValidate();
//                        LOG.log(Level.WARNING, error);
//                    }
//                }
//            }
//        });
//
//        emailAdress.getControl().focusedProperty().addListener((arg0, oldValue, newValue) -> {
//            if (!newValue) { //when focus lost
//                if (!emailAdress.getText().isEmpty()) {
//                    if (mdkCatalog.getByMailAdresse(emailAdress.getText(), AbstractCpxCatalog.DEFAULT_COUNTRY).getMdkEmail() == null) {
//                        String error = Lang.getMdkMailAddressValidate();
//                        LOG.log(Level.WARNING, error);
//                    }
//                }
//            }
//        });
//
//        fax.getControl().focusedProperty().addListener((arg0, oldValue, newValue) -> {
//            if (!newValue) { //when focus lost
//                if (!fax.getText().isEmpty()) {
//                    if (mdkCatalog.getByFax(fax.getText(), AbstractCpxCatalog.DEFAULT_COUNTRY).getMdkFax() == null) {
//                        String error = Lang.getMdkFaxValidate();
//                        LOG.log(Level.WARNING, error);
//                    }
//                }
//            }
//        });
//
//        telephone.getControl().focusedProperty().addListener((arg0, oldValue, newValue) -> {
//            if (!newValue) { //when focus lost
//                if (!telephone.getText().isEmpty()) {
//                    if (mdkCatalog.getByTelefon(telephone.getText(), AbstractCpxCatalog.DEFAULT_COUNTRY).getMdkPhone() == null) {
//                        String error = Lang.getMdkPhoneNoValidate();
//                        LOG.log(Level.WARNING, error);
//                    }
//                }
//            }
//        });
    }

    @Override
    protected void setUpAutoCompletion() {
//        AutoCompletionBinding<String> ACBmdkName = TextFields.bindAutoCompletion(tfMdkName.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
//            @Override
//            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
//                return mdkCatalog.findMatchesByMdkName(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
//            }
//        });
//        ACBmdkName.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
//            @Override
//            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
//                String str[] = tfMdkName.getText().split(", ");
//                updateCatalogValues(mdkCatalog.getByNameDeptCity(str[0], str[1], str[2], AbstractCpxCatalog.DEFAULT_COUNTRY));
//            }
//        });
////        validationSupport.registerValidator(tfMdkName.getControl(), new Validator<String>() {
////            @Override
////            public ValidationResult apply(Control t, String u) {
////                ValidationResult res = new ValidationResult();
////                res.addErrorIf(t, Lang.getValidationErrorInvalidMdkName(), (u == null || u.isEmpty()) || !mdkCatalog.hasEntry(u, AbstractCpxCatalog.DEFAULT_COUNTRY));
////                return res;
////            }
////        });
//
//        ACBmdkName.prefWidthProperty().bind(tfMdkName.widthProperty());
//        

//        AutoCompletionBinding<String> ACBdienstStelle = TextFields.bindAutoCompletion(mdkDepartment.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
//            @Override
//            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
//                return mdkCatalog.findMatchesByMdkDienstStelle(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
//            }
//        });
//        
        tfMdkName.setUpAutoCompletion();
//        ACBdienstStelle.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
//            @Override
//            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
//                String str = mdkDepartment.getText();
//                updateCatalogValues((CpxMdk) mdkCatalog.getByMDKDienststelle(str, AbstractCpxCatalog.DEFAULT_COUNTRY));
//            }
//        });
//        ACBdienstStelle.prefWidthProperty().bind(mdkDepartment.widthProperty());
//
//        AutoCompletionBinding<String> ACBpostleitzahl = TextFields.bindAutoCompletion(zipCode.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
//            @Override
//            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
//                return mdkCatalog.findMatchesByPostleitzahl(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
//            }
//        });
//        ACBpostleitzahl.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
//            @Override
//            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
//                String str = zipCode.getText();
//                updateCatalogValues((CpxMdk) mdkCatalog.getByMDKPostleitzahl(str, AbstractCpxCatalog.DEFAULT_COUNTRY));
//            }
//        });
//        ACBpostleitzahl.prefWidthProperty().bind(zipCode.widthProperty());
//
//        AutoCompletionBinding<String> ACBanschrift = TextFields.bindAutoCompletion(address.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
//            @Override
//            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
//                return mdkCatalog.findMatchesByAnschrift(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
//            }
//        });
//        ACBanschrift.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
//            @Override
//            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
//                String str = address.getText();
//                updateCatalogValues((CpxMdk) mdkCatalog.getByAnschrift(str, AbstractCpxCatalog.DEFAULT_COUNTRY));
//            }
//        });
//        ACBanschrift.prefWidthProperty().bind(address.widthProperty());
//
//        AutoCompletionBinding<String> ACBmailAdresse = TextFields.bindAutoCompletion(emailAdress.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
//            @Override
//            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
//                return mdkCatalog.findMatchesByMailAdresse(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
//            }
//        });
//        ACBmailAdresse.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
//            @Override
//            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
//                String str = emailAdress.getText();
//                updateCatalogValues((CpxMdk) mdkCatalog.getByMailAdresse(str, AbstractCpxCatalog.DEFAULT_COUNTRY));
//            }
//        });
//
//        AutoCompletionBinding<String> ACBvorwahl = TextFields.bindAutoCompletion(areaCode.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
//            @Override
//            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
//                return mdkCatalog.findMatchesByVorwahl(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
//            }
//        });
//        ACBvorwahl.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
//            @Override
//            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
//                String str = areaCode.getText();
//                updateCatalogValues((CpxMdk) mdkCatalog.getByMailVorwahl(str, AbstractCpxCatalog.DEFAULT_COUNTRY));
//            }
//        });
//
//        AutoCompletionBinding<String> ACBtelefon = TextFields.bindAutoCompletion(telephone.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
//            @Override
//            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
//                return mdkCatalog.findMatchesByTelefon(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
//            }
//        });
//        ACBtelefon.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
//            @Override
//            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
//                String str = telephone.getText();
//                updateCatalogValues((CpxMdk) mdkCatalog.getByTelefon(str, AbstractCpxCatalog.DEFAULT_COUNTRY));
//            }
//        });
//
//        AutoCompletionBinding<String> ACBfax = TextFields.bindAutoCompletion(fax.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
//            @Override
//            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
//                return mdkCatalog.findMatchesByFax(param.getUserText(), AbstractCpxCatalog.DEFAULT_COUNTRY);
//            }
//        });
//        ACBfax.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
//            @Override
//            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
//                String str = fax.getText();
//                updateCatalogValues(mdkCatalog.getByFax(str, AbstractCpxCatalog.DEFAULT_COUNTRY));
//            }
//        });

    }

    @Override
    protected void setUpValidation() {

//        getSkinnable().getValidationSupport().registerValidator(tfMdkName.getControl(), new Validator<String>() {
//            @Override
//            public ValidationResult apply(Control t, String u) {
//                ValidationResult res = new ValidationResult();
//                res.addErrorIf(t, "Bitte geben Sie die IKZ der Versicherung an!", u == null || u.isEmpty());
////                res.addErrorIf(t, "not valid", (getSkinnable().getRequest().getInsuranceIdentifier()!=null && getSkinnable().getRequest().getInsuranceIdentifier().isEmpty()));
//                if (u != null) {
//                    String str[] = u.split(", ");
//                    res.addErrorIf(t, "Bitte geben Sie eine korrekte IKZ ein!", !getSkinnable().getMdkCatalog().hasEntry(str[0], AbstractCpxCatalog.DEFAULT_COUNTRY));
//                }
//                return res;
//            }
//        });
//
//        getSkinnable().getValidationSupport().registerValidator(tfMdkName.getControl(), new Validator<String>() {
//            @Override
//            public ValidationResult apply(Control t, String mdkNameDeptCity) {
//                ValidationResult res = new ValidationResult();
//                res.addErrorIf(t, "Bitte geben Sie den MDK Namen ein!", (mdkNameDeptCity == null || mdkNameDeptCity.isEmpty()));
//                String str[] = mdkNameDeptCity == null ? new String[0] : mdkNameDeptCity.split(", ");
////                        res.addErrorIf(t, Lang.getValidationErrorInvalidMdkName(), (mdkNameDeptCity == null || mdkNameDeptCity.isEmpty()) || !mdkCatalog.hasEntry(mdkNameDeptCity, AbstractCpxCatalog.DEFAULT_COUNTRY));
//                // Needs to be check, what exactly do we need here?
//                switch (str.length) {
//                    case 3:
//                        res.addErrorIf(t, Lang.getValidationErrorInvalidMdkName(), !mdkCatalog.hasEntry(str[0], AbstractCpxCatalog.DEFAULT_COUNTRY) || !mdkCatalog.hasDeptEntry(str[1], AbstractCpxCatalog.DEFAULT_COUNTRY) || !mdkCatalog.hasCityEntry(str[2], AbstractCpxCatalog.DEFAULT_COUNTRY));
//                        getSkinnable().getValidationSupport().setErrorDecorationEnabled(true);
//                        break;
//                    case 2:
//                        res.addErrorIf(t, Lang.getValidationErrorInvalidMdkName(), !mdkCatalog.hasEntry(str[0], AbstractCpxCatalog.DEFAULT_COUNTRY) || !mdkCatalog.hasDeptEntry(str[1], AbstractCpxCatalog.DEFAULT_COUNTRY));
//                        getSkinnable().getValidationSupport().setErrorDecorationEnabled(true);
//                        break;
//                    case 1:
//                        res.addErrorIf(t, Lang.getValidationErrorInvalidMdkName(), !mdkCatalog.hasEntry(str[0], AbstractCpxCatalog.DEFAULT_COUNTRY));
//                        getSkinnable().getValidationSupport().setErrorDecorationEnabled(true);
//                        break;
//                    default:
//                        break;
//                }
//                return res;
//            }
//        });
        tfMdkName.setValidationSupport(getSkinnable().getValidationSupport());
        tfMdkName.setupValidation();
//        getSkinnable().getValidationSupport().registerValidator(mdkDepartment.getControl(), new Validator<String>() {
//            @Override
//            public ValidationResult apply(Control t, String mdkDept) {
//                ValidationResult res = new ValidationResult();
//                res.addErrorIf(t, "Bitte geben Sie die MDK Dienststelle ein! ", (mdkDept == null || mdkDept.isEmpty()));
//                res.addErrorIf(t, "MDK Dienststelle ist nicht korrekt!", (!mdkCatalog.hasDeptEntry(mdkDept, AbstractCpxCatalog.DEFAULT_COUNTRY)));
//                getSkinnable().getValidationSupport().setErrorDecorationEnabled(true);
//                return res;
//            }
//        });
//
//        getSkinnable().getValidationSupport().registerValidator(zipCode.getControl(), false, new Validator<String>() {
//            @Override
//            public ValidationResult apply(Control t, String mdkZipCode) {
//                ValidationResult res = new ValidationResult();
//                res.addErrorIf(t, "MDK Postleitzahl ist nicht korrekt!", (mdkZipCode != null && !mdkZipCode.isEmpty()) && !mdkCatalog.hasZipCodeEntry(mdkZipCode, AbstractCpxCatalog.DEFAULT_COUNTRY));
//                return res;
//            }
//        });
//
//        getSkinnable().getValidationSupport().registerValidator(address.getControl(), false, new Validator<String>() {
//            @Override
//            public ValidationResult apply(Control t, String mdkAddress) {
//                ValidationResult res = new ValidationResult();
//                res.addErrorIf(t, "MDK Anschrift ist nicht korrekt!", (mdkAddress != null && !mdkAddress.isEmpty()) && !mdkCatalog.hasAddressEntry(mdkAddress, AbstractCpxCatalog.DEFAULT_COUNTRY));
//                return res;
//            }
//        });
//
//        getSkinnable().getValidationSupport().registerValidator(emailAdress.getControl(), false, new Validator<String>() {
//            @Override
//            public ValidationResult apply(Control t, String mdkEmailAddress) {
//                ValidationResult res = new ValidationResult();
//                res.addErrorIf(t, "MDK Mail-Adresse ist nicht korrekt!", (mdkEmailAddress != null && !mdkEmailAddress.isEmpty()) && !mdkCatalog.hasEmailAddressEntry(mdkEmailAddress, AbstractCpxCatalog.DEFAULT_COUNTRY));
//                return res;
//            }
//        });
//
//        getSkinnable().getValidationSupport().registerValidator(areaCode.getControl(), false, new Validator<String>() {
//            @Override
//            public ValidationResult apply(Control t, String mdkAreaCode) {
//                ValidationResult res = new ValidationResult();
//                res.addErrorIf(t, "MDK Vorwahl ist nicht korrekt!", (mdkAreaCode != null && !mdkAreaCode.isEmpty()) && !mdkCatalog.hasAreaCodeEntry(mdkAreaCode, AbstractCpxCatalog.DEFAULT_COUNTRY));
//                return res;
//            }
//        });
//        getSkinnable().getValidationSupport().registerValidator(telephone.getControl(), false, new Validator<String>() {
//            @Override
//            public ValidationResult apply(Control t, String mdkTelephone) {
//                ValidationResult res = new ValidationResult();
//                res.addErrorIf(t, "MDK Telefon ist nicht korrekt!", (mdkTelephone != null && !mdkTelephone.isEmpty()) && !mdkCatalog.hasTelephoneEntry(mdkTelephone, AbstractCpxCatalog.DEFAULT_COUNTRY));
//                return res;
//            }
//        });
//        getSkinnable().getValidationSupport().registerValidator(fax.getControl(), false, new Validator<String>() {
//            @Override
//            public ValidationResult apply(Control t, String mdkFax) {
//                ValidationResult res = new ValidationResult();
//                res.addErrorIf(t, "MDK Fax ist nicht korrekt!", (mdkFax != null && !mdkFax.isEmpty()) && !mdkCatalog.hasFaxEntry(mdkFax, AbstractCpxCatalog.DEFAULT_COUNTRY));
//                return res;
//            }
//        });
//
            cklinkkWvDocDeliDead.setValidationSupport(getSkinnable().getValidationSupport());
            ckWvFeePaid.setValidationSupport(getSkinnable().getValidationSupport());
            ckWvSubseqProc.setValidationSupport(getSkinnable().getValidationSupport());
            cklinkkWvDocDeliDead.setupValidation();
            ckWvFeePaid.setupValidation();
            ckWvSubseqProc.setupValidation();


//        getSkinnable().getValidationSupport().registerValidator(wvDocDeliDead.getUserCtrl().getControl(), new Validator<String>() {
//                    @Override
//                    public ValidationResult apply(Control t, String u) {
//                        ValidationResult res = new ValidationResult();
//                        res.addErrorIf(t, Lang.getReminderValidationWarningNoReceiver(), cklinkkWvDocDeliDead.isChecked() && (u == null || u.isEmpty()));
//
//                        try {
//                            res.addErrorIf(t, Lang.getReminderValidationErrorNoValidReceiver(), cklinkkWvDocDeliDead.isChecked() && MenuCache.instance().getUserId(u) == null);
//                        } catch (NamingException ex) {
//                            Logger.getLogger(ReminderComponents.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                        return res;
//                    }
//                });
//        getSkinnable().getValidationSupport().registerValidator(wvFeePaid.getUserCtrl().getControl(), new Validator<String>() {
//            @Override
//            public ValidationResult apply(Control t, String u) {
//                ValidationResult res = new ValidationResult();
//                res.addErrorIf(t, Lang.getReminderValidationWarningNoReceiver(), ckWvFeePaid.isChecked() && (u == null || u.isEmpty()));
//
//                try {
//                    res.addErrorIf(t, Lang.getReminderValidationErrorNoValidReceiver(), ckWvFeePaid.isChecked() && MenuCache.instance().getUserId(u) == null);
//                } catch (NamingException ex) {
//                    Logger.getLogger(ReminderComponents.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                return res;
//            }
//        });
//        getSkinnable().getValidationSupport().registerValidator(wvSubseqProc.getUserCtrl().getControl(), new Validator<String>() {
//            @Override
//            public ValidationResult apply(Control t, String u) {
//                ValidationResult res = new ValidationResult();
//                res.addErrorIf(t, Lang.getReminderValidationWarningNoReceiver(), ckWvSubseqProc.isChecked() && (u == null || u.isEmpty()));
//
//                try {
//                    res.addErrorIf(t, Lang.getReminderValidationErrorNoValidReceiver(), ckWvSubseqProc.isChecked() && MenuCache.instance().getUserId(u) == null);
//                } catch (NamingException ex) {
//                    Logger.getLogger(ReminderComponents.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                return res;
//            }
//        });
        if (MenuCache.getMenuCacheDeadlines().isEmpty()) {
            String error = Lang.getDeadlinesError();
            LOG.log(Level.WARNING, error);
            MainApp.showWarningMessageDialog(error, getWindow());
        }
    }

    @Override
    protected void updateRequestValues(TWmRequestMdk pRequest) {

        CDeadline continuationFeeDeadline = MenuCache.getMenuCacheDeadlines().getCfd(getSkinnable().getCaseAdmissionDate());
        tfEditor.setText(pRequest.getMdkEditor()); // editor
//        if(getSkinnable().getProcessServiceFacade()!=null){
//            dpReqCreationDate.setDate(getSkinnable().getProcessServiceFacade().getCurrentProcess().getCreationDate());
//        }
        dpReqCreationDate.setDate(computeProcessDate(pRequest));
        dpMdkStartAuditExtendedDate.setDate(pRequest.getMdkStartAuditExtended());   // mdk start audit extended
        dpMdkStartAuditDate.setDate(pRequest.getMdkStartAudit());  // mdk start audit
        dpBillCorrectionDeadlineDate.setDate(pRequest.getBillCorrectionDeadline());  // bill correction deadline
        dpInsuranceRecivedBillDate.setDate(pRequest.getInsuranceRecivedBill());
        dpMdkReportCreationDate.setDate(pRequest.getReportDate());     // report creation date
        dpMdkAuditCompletionDeadlineDate.setDate(pRequest.getMdkAuditCompletionDeadline());  // mdk audit completion deadline
        if (pRequest.isContinuationFeePaidFl()) {
            ckMdkFeePaid.setSelected(true);  // labeled checkbox for continuation fee paid 
            dpMdkFeePaidDate.setDate(pRequest.getMdkContinuationFeePaidDeadline());
            dpMdkFeePaidDate.setDisable(false);
            ckWvFeePaid.setDisable(false);
            final Date date = dpMdkFeePaidDate.getDate();
//            wvFeePaid.setDueTo(date);
//            if (continuationFeeDeadline != null) {
//                final Date deadlineDt = continuationFeeDeadline.calculateDeadline(date);
//                wvFeePaid.setDueTo(deadlineDt);
//                if (wvFeePaid.getRemTypeInternalId() == 0L) {
//                    wvFeePaid.setRemType(continuationFeeDeadline.getDlReminderType());
//                }
//            }
            ckWvFeePaid.setReminderDueTo(continuationFeeDeadline != null?continuationFeeDeadline.calculateDeadline(date):dpMdkFeePaidDate.getDate(), 
                    continuationFeeDeadline == null?null:continuationFeeDeadline.getDlReminderType());
        }
        if (pRequest.isMdkSubProceedingDeadlineFl()) {
            mdkSubseqProcCheckBox.setSelected(true);  // labeled checkbox for Subseq Proceeding  
            dpMdkSubseqProcDate.setDate(pRequest.getMdkSubProceedingDeadline());
            dpMdkSubseqProcDate.setDisable(false);
            ckWvSubseqProc.setDisable(false);
        }

        dpMdkReportReceiveDate.setDate(pRequest.getMdkReportReceiveDate());    // report receive date
        CDeadline documentDeliveryDeadline = MenuCache.getMenuCacheDeadlines().getDdd(getSkinnable().getCaseAdmissionDate());
        if (pRequest.isMdkDocumentRequestFl()) {
            ckMdkDocumentRequest.setSelected(true);  // checkbox for document request at
            dpMdkDocumentRequestDate.setDate(pRequest.getMdkDocumentRequest());  // document request at
            dpMdkDocumentRequestDate.setDisable(false);
            ckMdkFeePaid.setDisable(false);
            ckMdkDocumentDelivered.setDisable(dpMdkDocumentRequestDate.getDate() == null);
            cklinkkWvDocDeliDead.setDisable(false);
            dpMdkDocumentDeliverDeadlineDate.setDate(pRequest.getMdkDocumentDeliverDeadline());    // deadline for submission
            final Date date = dpMdkDocumentDeliverDeadlineDate.getDate();
//            wvDocDeliDead.setDueTo(date);
//            if (documentDeliveryDeadline != null) {
//                final Date deadlineDt = documentDeliveryDeadline.calculateDeadline(date);
//                wvDocDeliDead.setDueTo(deadlineDt);
//                if (wvDocDeliDead.getRemTypeInternalId() == 0L) {
//                    wvDocDeliDead.setRemType(documentDeliveryDeadline.getDlReminderType());
//                }
//            }
            cklinkkWvDocDeliDead.setReminderDueTo(documentDeliveryDeadline != null?documentDeliveryDeadline.calculateDeadline(date):dpMdkDocumentDeliverDeadlineDate.getDate(), 
                    documentDeliveryDeadline == null?null:documentDeliveryDeadline.getDlReminderType());
        }
        if (pRequest.isMdkDocumentDeliveredFl()) {
            ckMdkDocumentDelivered.setSelected(true);    // checkbox for document delivered at
            ckMdkDocumentDeliveredDate.setDate(pRequest.getMdkDocumentDelivered());  // document delivered at
            ckMdkDocumentDeliveredDate.setDisable(false);
        }
        //AWi: somehow mdkreportfl is not set properly, enable checkbox if corresponding dates store values
        if (pRequest.getReportDate()!= null || pRequest.getMdkReportReceiveDate()!=null){//pRequest.isMdkReportFl()) {
            ckMdkReport.setSelected(true);
            dpMdkReportCreationDate.setDisable(false);
            dpMdkReportReceiveDate.setDisable(false);
        }
        cbMdkStatus.selectByInternalId(pRequest.getRequestState());
        taUserComment.setText(pRequest.getComment());
        taMdkComment.setText(pRequest.getUserComment());
        if (pRequest.getMdkInternalId() != null) {
            CpxMdk mdk = mdkCatalog.getByInternalId(pRequest.getMdkInternalId(), AbstractCpxCatalog.DEFAULT_COUNTRY);
        } else {
            String error = Lang.getMdkEmptyMdkDataDialog();
            LOG.log(Level.WARNING, error);
            MainApp.showErrorMessageDialog(error, getWindow());
        }

        updateAuditReasonsData(ckcbAuditReasons,pRequest);
        updateAuditReasonsExtendedData(ckcbExtendedAuditReasons,pRequest);
    }
    protected Date computeProcessDate(TWmRequestMdk pRequest){
        if(pRequest.getStartAudit() != null){
            return pRequest.getStartAudit();
        }
        if(getSkinnable().getProcessDate() != null){
            return getSkinnable().getProcessDate();
        }
        if(getSkinnable().getProcessServiceFacade()!=null){
            return getSkinnable().getProcessServiceFacade().getCurrentProcess().getCreationDate();
        }
        return new Date();
    }
    protected void updateCatalogValues(CpxMdk mdk) {
//        if (mdk.id == 0L) {
//            tfMdkName.setText(mdk.getMdkName());
//        } else {
//            tfMdkName.setText(mdk.getMdkName() + ", " + mdk.getMdkDepartment() + ", " + mdk.getMdkCity());
//        }
        tfMdkName.updateCatalogValues(mdk);
//        mdkDepartment.setText(mdk.getMdkDepartment());
//        zipCode.setText(mdk.getMdkZipCode());
//        address.setText(mdk.getMdkStreet());
//        emailAdress.setText(mdk.getMdkEmail());
//        areaCode.setText(mdk.getMdkPhonePrefix());
//        telephone.setText(mdk.getMdkPhone());
//        fax.setText(mdk.getMdkFax());
        getSkinnable().setMdk(mdk);
        getSkinnable().getCatalogValidationResult().invalidate();
    }

    @Override
    public void disableControls(boolean armed) {
        tfMdkName.disableControls(armed);
//        tfMdkName.setDisable(armed);
        tfEditor.setDisable(armed);
        dpReqCreationDate.setDisable(armed);
        dpMdkStartAuditDate.setDisable(armed);
        ckMdkReport.setDisable(armed);
        dpMdkReportCreationDate.setDisable(ckMdkReport.isDisable() || !ckMdkReport.isSelected());
        dpMdkReportReceiveDate.setDisable(ckMdkReport.isDisable() || !ckMdkReport.isSelected());
        cbMdkStatus.setDisable(armed);
        ckcbAuditReasons.setDisable(armed);
        ckcbExtendedAuditReasons.setDisable(armed);
        lbMdkDocuments.setDisable(armed);
        dpMdkStartAuditExtendedDate.setDisable(armed);
        dpInsuranceRecivedBillDate.setDisable(armed);
        ckMdkDocumentRequest.setDisable(armed);
        dpMdkDocumentRequestDate.setDisable(ckMdkDocumentRequest.isDisable() || !ckMdkDocumentRequest.isSelected());
        ckMdkDocumentDelivered.setDisable(armed);
        ckMdkDocumentDeliveredDate.setDisable(ckMdkDocumentDelivered.isDisable() || !ckMdkDocumentDelivered.isSelected());
        cklinkkWvDocDeliDead.setDisable(ckMdkDocumentDelivered.isDisable() || !ckMdkDocumentDelivered.isSelected());
        ckMdkFeePaid.setDisable(armed);
        dpMdkFeePaidDate.setDisable(ckMdkFeePaid.isDisable() || !ckMdkFeePaid.isSelected());
        ckWvFeePaid.setDisable(ckMdkFeePaid.isDisable() || !ckMdkFeePaid.isSelected());
        mdkSubseqProcCheckBox.setDisable(armed);
        dpMdkSubseqProcDate.setDisable(mdkSubseqProcCheckBox.isDisable() || !mdkSubseqProcCheckBox.isSelected());
        ckWvSubseqProc.setDisable(mdkSubseqProcCheckBox.isDisable() || !mdkSubseqProcCheckBox.isSelected());
        taMdkComment.setDisable(armed);
        taUserComment.setDisable(armed);
//        btnMdkSearch.setDisable(armed);
//        mdkDepartment.setDisable(armed);
//        zipCode.setDisable(armed);
//        address.setDisable(armed);
//        emailAdress.setDisable(armed);
//        areaCode.setDisable(armed);
//        telephone.setDisable(armed);
//        fax.setDisable(armed);
        hbMdkReminders.setDisable(armed);
    }

    @Override
    protected void updateRequestData() {
        getSkinnable().getRequest().setMdkEditor(tfEditor.getText());
        getSkinnable().getRequest().setStartAudit(dpReqCreationDate.getDate());
        getSkinnable().getRequest().setMdkStartAuditExtended(dpMdkStartAuditExtendedDate.getDate());
        getSkinnable().getRequest().setMdkStartAudit(dpMdkStartAuditDate.getDate());
        getSkinnable().getRequest().setBillCorrectionDeadline(dpBillCorrectionDeadlineDate.getDate());
        getSkinnable().getRequest().setInsuranceRecivedBill(dpInsuranceRecivedBillDate.getDate());
        if (ckMdkReport.isSelected()) {
            getSkinnable().getRequest().setReportDate(dpMdkReportCreationDate.getDate());
            getSkinnable().getRequest().setMdkReportReceiveDate(dpMdkReportReceiveDate.getDate());
        }
        getSkinnable().getRequest().setMdkAuditCompletionDeadline(dpMdkAuditCompletionDeadlineDate.getDate());
        getSkinnable().getRequest().setMdkContinuationFeePaidDeadline(dpMdkFeePaidDate.getDate());
        getSkinnable().getRequest().setMdkSubProceedingDeadline(dpMdkSubseqProcDate.getDate());
        getSkinnable().getRequest().setMdkSubProceedingDeadlineFl(mdkSubseqProcCheckBox != null ? mdkSubseqProcCheckBox.isSelected() : false);
        getSkinnable().getRequest().setContinuationFeePaidFl(ckMdkFeePaid != null ? ckMdkFeePaid.isSelected() : false);
        getSkinnable().getRequest().setMdkDocumentRequestFl(ckMdkDocumentRequest.isSelected());
        getSkinnable().getRequest().setMdkDocumentRequest(dpMdkDocumentRequestDate.getDate());
        getSkinnable().getRequest().setMdkDocumentDeliveredFl(ckMdkDocumentDelivered.isSelected());
        //CPX-1381
        getSkinnable().getRequest().setMdkReportFl(ckMdkReport.isSelected());
        getSkinnable().getRequest().setMdkDocumentDelivered(ckMdkDocumentDeliveredDate.getDate());
        getSkinnable().getRequest().setMdkDocumentDeliverDeadline(dpMdkDocumentDeliverDeadlineDate.getDate());
        //CPX-1205 Mdk State from cpx_common
        final Long internalId = cbMdkStatus.getSelectedInternalId();
        getSkinnable().getRequest().setRequestState(internalId == null ? 0L : internalId);
        getSkinnable().getRequest().setComment(taUserComment.getText());
        getSkinnable().getRequest().setUserComment(taMdkComment.getText());
        getSkinnable().getRequest().setReminderDeliverDeadlineFl(false);
        getSkinnable().getRequest().setMdkInternalId(tfMdkName.getMdkInternalId());
        updateAuditReasons(ckcbAuditReasons,getSkinnable().getRequest());
        updateAuditReasonsExtended(ckcbExtendedAuditReasons,getSkinnable().getRequest());
        try {
            getSkinnable().setReminders(getMdkRequestReminders());
        } catch (NamingException ex) {
            Logger.getLogger(MdkRequestEditorSkin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    private class ClearAutoCompleteFieldsListener implements ChangeListener<String> {
//
//        @Override
//        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//            tfMdkName.getControl().clear();
//            mdkDepartment.getControl().clear();
//            zipCode.getControl().clear();
//            emailAdress.getControl().clear();
//            areaCode.getControl().clear();
//            telephone.getControl().clear();
//            fax.getControl().clear();
//        }
//    }
//
//    private void showMdkInfoPopover() {
//
//        VBox vBox = new VBox();
//        vBox.getChildren().addAll(mdkDepartment, zipCode, address, emailAdress, areaCode, telephone, fax);
//        vBox.setPadding(new Insets(12.0, 12.0, 12.0, 12.0));
//        PopOver popover = new AutoFitPopOver();
//        popover.setHideOnEscape(true);
//        popover.setAutoHide(true);
//        popover.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
//        popover.setDetachable(false);
//
//        popover.setContentNode(vBox);
//        popover.show(btnMdkSearch);
//        popover.getContentNode().setOnKeyPressed(new EventHandler<KeyEvent>() {
//
//            @Override
//            public void handle(KeyEvent ke) {
//                if (ke.getCode().equals(KeyCode.ENTER)) {
//                    popover.hide(Duration.ZERO);
//
//                }
//            }
//        });
//        popover.ownerWindowProperty().get().setOnCloseRequest(new EventHandler<WindowEvent>() {
//            @Override
//            public void handle(WindowEvent event) {
//                popover.hide(Duration.ZERO);
//            }
//        })
    
//    private void updateAuditReasons(TWmRequestMdk request) {
//        //request.getAuditReasons().clear();
//        //ObservableList<Map.Entry<Long, String>> checked = ckcbAuditReasons.getCheckModel().getCheckedItems();
//        List<Map.Entry<Long, String>> items = ckcbAuditReasons.getItems();
//        for (Map.Entry<Long, String> item : items) {
//            final Long key = item.getKey();
//            final boolean checked = ckcbAuditReasons.getCheckModel().isChecked(item);
//            final boolean contains = request.containsAuditReason(key);
//            if (checked) {
//                if (!contains) {
//                    LOG.log(Level.INFO, "add new main audit reason number {0} to request", key);
//                    request.addAuditReason(key, false);
//                }
//            } else {
//                if (contains) {
//                    LOG.log(Level.INFO, "remove main audit reason number {0} from request", key);
//                    request.removeAuditReason(key);
//                }
//            }
//        }
//    }
//
//    private void updateAuditReasonsExtended(TWmRequestMdk request) {
//        //request.getAuditReasons(true).clear();
//        //ObservableList<Map.Entry<Long, String>> extendedChecked = ckcbExtendedAuditReasons.getCheckModel().getCheckedItems();
//        List<Map.Entry<Long, String>> extendedItems = ckcbExtendedAuditReasons.getItems();
//        for (Map.Entry<Long, String> item : extendedItems) {
//            final Long key = item.getKey();
//            final boolean checked = ckcbExtendedAuditReasons.getCheckModel().isChecked(item);
//            final boolean contains = request.containsAuditReasonExtended(key);
//            if (checked) {
//                if (!contains) {
//                    LOG.log(Level.INFO, "add new extended audit reason number {0} to request", key);
//                    request.addAuditReason(key, true);
//                }
//            } else {
//                if (contains) {
//                    LOG.log(Level.INFO, "remove extended audit reason number {0} from request", key);
//                    request.removeAuditReasonExtended(key);
//                }
//            }
//        }
//    }
//
//    private void updateAuditReasonsData(TWmRequestMdk request) {
//        nextVal:for (TWmMdkAuditReasons reason : request.getAuditReasons(false)) {
//            for (Map.Entry<Long, String> item : ckcbAuditReasons.getItems()) {
//                if (item.getKey().equals(reason.getAuditReasonNumber())) {
//                    ckcbAuditReasons.getCheckModel().check(item);
//                    continue nextVal;
//                }
//            }
//            LOG.warning("Can not find entry for AuditReasonNumber: " + reason.getAuditReasonNumber() + "!\nWas the CaseType changed or was it deleted?");
//        }
//    }
//
//    private void updateAuditReasonsExtendedData(TWmRequestMdk request) {
//        nextVal:for (TWmMdkAuditReasons extendedReason : request.getAuditReasons(true)) {
//            for (Map.Entry<Long, String> item : ckcbExtendedAuditReasons.getItems()) {
//                if (item.getKey().equals(extendedReason.getAuditReasonNumber())) {
//                    ckcbExtendedAuditReasons.getCheckModel().check(item);
//                    continue nextVal;
//                }
//            }
//            LOG.warning("Can not find entry for AuditReasonNumber: " + extendedReason.getAuditReasonNumber() + "!\nWas the CaseType changed or was it deleted?");
//        }
//    }

//    private void showMdkReminder(AsyncTableView<TWmReminder> mdkReminders) {
//        hbMdkReminders.setPrefHeight(150);
//        hbMdkReminders.getChildren().clear();
//        hbMdkReminders.getChildren().add(mdkReminders);
//    }
    public List<TWmReminder> getMdkRequestReminders() throws NamingException {
        TWmRequest reqObject = getSkinnable().getRequest();
        final ArrayList<TWmReminder> remSet = new ArrayList<>();
        if (reqObject == null) {
            LOG.log(Level.SEVERE, "request object is null!");
            return remSet;
        }
        if (reqObject.isRequestMdk()) {
            if (cklinkkWvDocDeliDead.isChecked()) {
                remSet.add( cklinkkWvDocDeliDead.setReminderData());
//                TWmReminder rem = new TWmReminder();
//                setReminderData(rem, WmReminderTypeEn.MD_DOCUMENT_DELIVERED);
//                remSet.add(rem);
            }
            if (ckWvFeePaid.isChecked()) {
                remSet.add(ckWvFeePaid.setReminderData());
//                TWmReminder rem = new TWmReminder();
//                setReminderData(rem, WmReminderTypeEn.MD_CONTINUATION_FEE);
//                remSet.add(rem);
            }
            if (ckWvSubseqProc.isChecked()) {
                remSet.add(ckWvSubseqProc.setReminderData());
//                TWmReminder rem = new TWmReminder();
//                setReminderData(rem, WmReminderTypeEn.MD_SUBSEQUENT_PROCEEDING);
//                remSet.add(rem);
            }
            return remSet;
        } else {
            return null;
        }
    }

//    private void setReminderData(TWmReminder rem, WmReminderTypeEn remTyp) throws NamingException {
//    
//        if (remTyp == WmReminderTypeEn.MD_DOCUMENT_DELIVERED) {
//            ReminderComponents wvDocDeliDead = cklinkkWvDocDeliDead.getReminderComponents();
//            rem.setCreationDate(new Date());
//            rem.setCreationUser(Session.instance().getCpxUserId());
//            rem.setAssignedUserId(wvDocDeliDead.getAssUser());
//            rem.setSubject(wvDocDeliDead.getRemTypeInternalId());
//
//            rem.setDueDate(wvDocDeliDead.getDueTo());
//            rem.setComment(wvDocDeliDead.getComment());
//            rem.setHighPrio(wvDocDeliDead.getPrio());
//
//        }
//        if (remTyp == WmReminderTypeEn.MD_CONTINUATION_FEE) {
//            ReminderComponents wvFeePaid = ckWvFeePaid.getReminderComponents();
//            rem.setCreationUser(Session.instance().getCpxUserId());
//            rem.setCreationDate(new Date());
//            rem.setAssignedUserId(wvFeePaid.getAssUser());
//            rem.setSubject(wvFeePaid.getRemTypeInternalId());
//            rem.setDueDate(wvFeePaid.getDueTo());
//            rem.setComment(wvFeePaid.getComment());
//            rem.setHighPrio(wvFeePaid.getPrio());
//        }
//        if (remTyp == WmReminderTypeEn.MD_SUBSEQUENT_PROCEEDING) {
//            ReminderComponents wvSubseqProc = ckWvSubseqProc.getReminderComponents();
//            rem.setCreationUser(Session.instance().getCpxUserId());
//            rem.setCreationDate(new Date());
//            rem.setAssignedUserId(wvSubseqProc.getAssUser());
//            rem.setSubject(wvSubseqProc.getRemTypeInternalId());
//            rem.setDueDate(wvSubseqProc.getDueTo());
//            rem.setComment(wvSubseqProc.getComment());
//            rem.setHighPrio(wvSubseqProc.getPrio());
//
//        }
//
//    }

    public void setDetailPane(Parent detail) {
        hbMdkReminders.getChildren().clear();
        hbMdkReminders.getChildren().add(detail);
    }

//    private enum WmReminderTypeEn {
//
//        DOCUMENT_DELIVERED, CONTINUATION_FEE, SUBSEQUENT_PROCEEDING;
//
//    }

}
