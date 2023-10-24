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
package de.lb.cpx.client.app.wm.fx.process.completion;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.WmMainFrameFXMLController;
import de.lb.cpx.client.app.wm.fx.process.completion.gridpane.VersionOverviewGridPane;
import de.lb.cpx.client.app.wm.fx.process.completion.risk.CompletionFinalRisk;
import de.lb.cpx.client.app.wm.fx.process.completion.risk.CompletionRiskManager;
import de.lb.cpx.client.app.wm.fx.process.completion.risk.CompletionVersionRisk;
import de.lb.cpx.client.app.wm.fx.process.completion.risk.ProcessCompletionRisk;
import de.lb.cpx.client.app.wm.fx.process.completion.risk.ProcessCompletionRiskSkin;
import de.lb.cpx.client.app.wm.fx.process.completion.risk.RiskDisplayMode;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxBaserate;
import de.lb.cpx.client.core.model.catalog.CpxHospitalCatalog;
import de.lb.cpx.client.core.model.fx.alert.ConfirmDialog;
import de.lb.cpx.client.core.model.fx.combobox.ProcessResultCombobox;
import de.lb.cpx.client.core.model.fx.combobox.ProcessTopicCombobox;
import de.lb.cpx.client.core.model.fx.combobox.UsersCombobox;
import de.lb.cpx.client.core.model.fx.dialog.TitledDialog;
import de.lb.cpx.client.core.model.fx.labeled.LabeledAuditReasonsCheckedComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledCurrencyTextField;
import de.lb.cpx.client.core.model.fx.labeled.LabeledDatePicker;
import de.lb.cpx.client.core.model.fx.labeled.LabeledIntegerTextField;
import de.lb.cpx.client.core.model.fx.labeled.LabeledLabel;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextArea;
import de.lb.cpx.client.core.model.fx.tableview.column.PcclColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.RevenueColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.drg.DrgCatalogColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.drg.DrgCodeColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.drg.DrgCorrectionColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.drg.DrgCwEffColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.drg.DrgSuppFeeColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.pepp.PeppCarePercAdultColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.pepp.PeppCarePercInfColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.pepp.PeppCatalogColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.pepp.PeppCodeColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.pepp.PeppCwEffColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.pepp.PeppDailyFeeValueColumn;
import de.lb.cpx.client.core.model.fx.tableview.column.pepp.PeppSuppFeeColumn;
import de.lb.cpx.client.core.util.OverrunHelper;
import de.lb.cpx.client.core.util.VersionStringConverter;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseDrg;
import de.lb.cpx.model.TCasePepp;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.model.enums.VersionRiskTypeEn;
import de.lb.cpx.server.commonDB.model.CMdkAuditreason;
import de.lb.cpx.server.commonDB.model.CWmListProcessResult;
import de.lb.cpx.server.commonDB.model.CWmListProcessTopic;
import de.lb.cpx.server.commonDB.model.enums.RiskAreaEn;
import de.lb.cpx.service.properties.CpxAuthorizationException;
import de.lb.cpx.service.properties.RoleProperties;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmFinalisationRisk;
import de.lb.cpx.wm.model.TWmProcessHospital;
import de.lb.cpx.wm.model.TWmProcessHospitalFinalisation;
import de.lb.cpx.wm.model.TWmRequest;
import de.lb.cpx.wm.model.TWmRisk;
import de.lb.cpx.wm.model.TWmRiskDetails;
import de.lb.cpx.wm.model.enums.WmEventTypeEn;
import de.lb.cpx.wm.model.enums.WmWorkflowStateEn;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.util.Callback;
import javax.validation.constraints.NotNull;

/**
 * FXML Controller class For ProcessCompletion Screen
 *
 * @author wilde
 */
public class ProcessCompletionFXMLController extends Controller<CpxScene> {

    private static final Logger LOG = Logger.getLogger(ProcessCompletionFXMLController.class.getName());

    @FXML
    private LabeledAuditReasonsCheckedComboBox cbMainReviewCause;
    @FXML
    private LabeledAuditReasonsCheckedComboBox cbAdditionalReviewCause;
    @FXML
    private ProcessTopicCombobox cbReviewKind;
    @FXML
    private LabeledLabel labelStartReview;
    @FXML
    private LabeledDatePicker dpFinishDate;
    @FXML
    private VBox vbBaserate;
    @FXML
    private Label labelBaserate;
//    private LabeledLabel labelHosIdent;
    @FXML
    private LabeledIntegerTextField tfDiffDays;
//    @FXML
//    private LabeledDoubleTextField tfDiffMoney;
    @FXML
    private UsersCombobox cbAssignableUsers;
    @FXML
    private ProcessResultCombobox cbReviewResult;
    @FXML
    private Label labelProcessStatus;
    @FXML
    private Button buttonFinishProcess;
    @FXML
    private Label labelDrgArea;
    @FXML
    private VersionOverviewGridPane gpVersionDetails;
    @FXML
    private LabeledTextArea taComment;

    private final BooleanProperty processClosedProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty readOnlyProperty = new SimpleBooleanProperty(false);
    private final ObjectProperty<TCaseDetails> localDetailsProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<TCaseDetails> externDetailsProperty = new SimpleObjectProperty<>();
    private ProcessServiceFacade serviceFacade;
    private final VersionStringConverter converter = new VersionStringConverter();
    private final RoleProperties prop = Session.instance().getRoleProperties();
    @FXML
    private Button btnVersionSelect;
//    @FXML
//    private LabeledIntegerTextField tfRisk;
    @FXML
    private LabeledCurrencyTextField tfResultDelta;
    @FXML
    private LabeledCurrencyTextField tfPenaltyFee;
    @FXML
    private LabeledLabel labelHosIdent;
    @FXML
    private VBox boxInitialRisk;
    @FXML
    private VBox boxFinalRisk;
    @FXML
    private HBox boxRiskResult;
    @FXML
    private ScrollPane spMdRisk;
    @FXML
    private HBox boxMdRisk;
    @FXML
    private HBox boxBottom;
//    private ProcessCompletionRisk finalRiskView;
    private CompletionFinalRisk finalRiskView;
    private CompletionRiskManager riskManager;

    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setUpLanguage();
        setUpListeners();
//        tfDiffDays.setTextFormatter(new NumberFormatter());
//        tfDiffMoney.setTextFormatter(new DoubleFormatter());
//        tfRisk.setTextFormatter(new DoubleFormatter());
//        tfResultDelta.setTextFormatter(new DoubleFormatter());
//      labelHosIdent.setContentAlignment(Pos.TOP_LEFT);
    }

    /**
     * init controller with values
     *
     * @param pServiceFacade service facade
     */
    public void init(ProcessServiceFacade pServiceFacade) {
        serviceFacade = pServiceFacade;
        gpVersionDetails.initServiceFacade(pServiceFacade);
        labelStartReview.setText(Lang.toDate(pServiceFacade.getCurrentProcess().getCreationDate()));
        setCatalogData(pServiceFacade);
        setValues();
        determinAuditReasonsForLastRequest();
//        long start = System.currentTimeMillis();
//        try {
//            setUpRiskArea();
//        } catch (IOException ex) {
//            LOG.warning("can not set up risk layout due to fxml error!");
//            Logger.getLogger(ProcessCompletionFXMLController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        LOG.log(Level.INFO, "time to setup risk in ms: {0}", System.currentTimeMillis() - start);
    }

    @Override
    public void refresh() {
        super.refresh();
        if (gpVersionDetails != null) {
            gpVersionDetails.refresh();
        }
    }

    @Override
    public void reload() {
        super.reload();
        if (gpVersionDetails != null && !isProcessClosed()) {
            setValues();
            gpVersionDetails.reload();
            updateRisks();
        }
    }

    //sets alnguage from lang class
    private void setUpLanguage() {
        cbMainReviewCause.setTitle(Lang.getAuditAuditReasons());
        cbAdditionalReviewCause.setTitle(Lang.getMdkExtendedAuditReasons());
        cbReviewKind.setTitle(Lang.getProcessTopic());
        labelStartReview.setTitle(Lang.getProcessStartDateObj().getAbbreviation());
        labelStartReview.addStyleClassToTitle("cpx-detail-label");
        dpFinishDate.setTitle(Lang.getProcessCompletionFinalisation());
        labelBaserate.setText("Baserate(s)"/*Lang.getBaserate()*/);
        labelBaserate.getStyleClass().add("cpx-detail-label");
        labelHosIdent.setTitle(Lang.getHospitalIdent());
        labelHosIdent.addStyleClassToTitle("cpx-detail-label");
        labelHosIdent.setContentAlignment(Pos.TOP_LEFT);
        tfDiffDays.setTitle(Lang.getDifferenceDays());
//        tfRisk.setTitle(Lang.getProcessFinalisationRisk());
        tfResultDelta.setTitle(Lang.getProcessFinalisationResultDelta());
        tfPenaltyFee.setTitle(Lang.getProcessFinalisationPenaltyFee());
//        tfDiffMoney.setTitle(Lang.getDifferenceCurrency(Lang.getCurrencySymbol()));
        cbAssignableUsers.setTitle(Lang.getMdkEditor());
        cbReviewResult.setTitle(Lang.getResult());
        labelProcessStatus.setText(Lang.getProcessStatusOpen());
        buttonFinishProcess.setText(Lang.getProcessStatusClose());
        labelDrgArea.setText("");//Lang.getDRGObj().getAbbreviation());
        taComment.setTitle("");
        btnVersionSelect.setText(Lang.getProcessFinalisationVersionSelectObj().abbreviation);
    }

    public boolean isProcessClosed() {
        return processClosedProperty.get();
    }

    private void setValues() {
        serviceFacade.loadBaseCase();
        TCase baseCase = serviceFacade.getCurrentProcess().getMainCase();
        if (baseCase == null) {
            LOG.log(Level.WARNING, "No Base case is set in the process {0}", serviceFacade.getCurrentProcess().getId());
            return;
        }
        gpVersionDetails.setCaseType(baseCase.getCsCaseTypeEn());
        labelDrgArea.setText(getDrgText(baseCase.getCsCaseTypeEn()));
        TWmProcessHospital processHospital = (TWmProcessHospital) serviceFacade.getCurrentProcess();

        processClosedProperty.set(processHospital.isIsClosed());

        readOnlyProperty.set(!prop.isDoFinalisationAllowed());
        if (processClosedProperty.get()) {
            setUpDataForStoredValues();
        } else {
            setUpDataForActualValues();
        }
        if (readOnlyProperty.get()) {
            setUpControllerForReadOnly();
            String reason = "";
            if (!prop.isDoFinalisationAllowed()) {
                reason = Lang.getProcessFinalisationNoRight();
            }
//          if( serviceFacade.getProcess().getMainCase().getCsCansellationReasonEn()) reason="Basis Fall ist strorniert\n";
            MainApp.showInfoMessageDialog(Lang.getProcessFinalisationReadonlyMessage() + "\n" + Lang.getProcessFinalisationReadonlyReason(reason), MainApp.getWindow());
//          MainApp.showAuthorizationMessage(new CpxAuthorizationException(prop, Lang.getAuthorizationDialogMessage(prop == null ? "---" : prop.getName(), "Vorgangsabschluss") + "\n" + Lang.getAuthorizationDialogMessageContact()));
        }

    }

    private Label keyLabel(String pText) {
        Label label = new Label(pText);
        label.getStyleClass().add("cpx-detail-label");
        label.setPrefWidth(100);
        return label;
    }

    private Label ValueLabel(String pText) {
        Label label = new Label(pText);
        label.setPrefWidth(100);
        return label;
    }

    private void setBaserate(TCaseDetails pInitialVersion) {
        switch (serviceFacade.getCurrentProcess().getMainCase().getCsCaseTypeEn()) {
            case DRG:
                labelHosIdent.setText(serviceFacade.getCurrentProcess().getMainCase().getCsHospitalIdent() );
                labelHosIdent.setTooltip(new Tooltip(CpxHospitalCatalog.instance().getByCode(labelHosIdent.getText(), AbstractCpxCatalog.DEFAULT_COUNTRY).toString()));
                if (vbBaserate.getChildren().size() == 1) {
                    CpxBaserate pBaseRate = serviceFacade.getDrgBaseRate(pInitialVersion);
                    if(pBaseRate == null){
                         vbBaserate.getChildren().add(new Label("DRG-Baserate: nicht gesetzt"));
                    }else{
                    vbBaserate.getChildren().add(new Label("DRG-Baserate: "));

                    vbBaserate.getChildren().add(new HBox(ValueLabel(Lang.toDecimal(pBaseRate.getBaseFeeValue(), 2) + Lang.getCurrencySymbol()), 
                            ValueLabel(Lang.toDate(pBaseRate.getBaseValidFrom())),
                            ValueLabel(Lang.toDate(pBaseRate.getBaseValidTo()))));
                    }
// check, whether there are more than one care baserate        
                     List<CpxBaserate> baseRateList = serviceFacade.findDrgCareBaserates4HosIdent(pInitialVersion.getHospitalCase().getCsHospitalIdent());
                     if(baseRateList != null  && !baseRateList.isEmpty()){

                             vbBaserate.getChildren().add(new Label("Pflege-Baserate: " ));
                             setBaserateList(pInitialVersion, baseRateList);
//                             for(CpxBaserate pBaseRate: baseRateList){
//                                 if(!pBaseRate.getBaseValidTo().after(pInitialVersion.getCsdAdmissionDate())){
//                                     continue;
//                                 }
//                                 if(!pBaseRate.getBaseValidFrom().before(pInitialVersion.getCsdDischargeDate())){
//                                     break;
//                                 }
//                                 vbBaserate.getChildren().add(new HBox(ValueLabel(Lang.toDecimal(pBaseRate.getBaseFeeValue(), 2)), 
//                                         ValueLabel(Lang.toDate(pBaseRate.getBaseValidFrom())),
//                                         ValueLabel(Lang.toDate(pBaseRate.getBaseValidTo()))));
//                             }
                         }
                     
                }
                Tooltip.install(vbBaserate, new Tooltip(Lang.getProcessBaserateTooltip(converter.toString(pInitialVersion), Lang.toDate(pInitialVersion.getCsdAdmissionDate()))));
//                labelBaserate.setTooltip(new Tooltip(Lang.getProcessBaserateTooltip(converter.toString(pInitialVersion), Lang.toDate(pInitialVersion.getCsdAdmissionDate()))));
                labelHosIdent.setTooltip(new Tooltip(CpxHospitalCatalog.instance().getByCode(pInitialVersion.getHospitalCase().getCsHospitalIdent(), AbstractCpxCatalog.DEFAULT_COUNTRY).toString()));
                return;
            case PEPP:
                labelHosIdent.setVisible(false);
                labelBaserate.setText(Lang.getProcessBaseratePepp(pInitialVersion.getHospitalCase().getCsHospitalIdent()));
                if (vbBaserate.getChildren().size() == 1) {
                    vbBaserate.getChildren().add(new HBox(keyLabel(Lang.getProcessBaseratePeppValue()), keyLabel(Lang.getProcessBaseratePeppValidFrom()), keyLabel(Lang.getProcessBaseratePeppValidto())));
                    List<CpxBaserate> baseRateList = serviceFacade.findPeppBaserate4HosIdent(pInitialVersion.getHospitalCase().getCsHospitalIdent());
//                    baseRateList.forEach((pBaseRate) -> {
//                        vbBaserate.getChildren().add(new HBox(ValueLabel(Lang.toDecimal(pBaseRate.getBaseFeeValue(), 2)), ValueLabel(Lang.toDate(pBaseRate.getBaseValidFrom())), ValueLabel(Lang.toDate(pBaseRate.getBaseValidTo()))));
//                    });
                      setBaserateList(pInitialVersion, baseRateList);
                }
                labelBaserate.setTooltip(new Tooltip(CpxHospitalCatalog.instance().getByCode(pInitialVersion.getHospitalCase().getCsHospitalIdent(), AbstractCpxCatalog.DEFAULT_COUNTRY).toString()));
                return;
            default:
                labelBaserate.setText(Lang.toDecimal(0.0));
        }

    }
    
    private void setBaserateList(@NotNull TCaseDetails pInitialVersion, @NotNull List<CpxBaserate> baseRateList ){

        for(CpxBaserate pBaseRate: baseRateList){
            if(!pBaseRate.getBaseValidTo().after(pInitialVersion.getCsdAdmissionDate())){
                continue;
            }
            if(pInitialVersion.getCsdDischargeDate() != null && !pBaseRate.getBaseValidFrom().before(pInitialVersion.getCsdDischargeDate())){
                break;
            }
            vbBaserate.getChildren().add(new HBox(ValueLabel(Lang.toDecimal(pBaseRate.getBaseFeeValue(), 2) + Lang.getCurrencySymbol()), 
                ValueLabel(Lang.toDate(pBaseRate.getBaseValidFrom())),
                ValueLabel(Lang.toDate(pBaseRate.getBaseValidTo()))));
            if( pInitialVersion.getCsdDischargeDate() == null){
                break;
            }
        }

    }
    //set listeners to handle closed process state
    //and bind case versions

    private void setUpListeners() {

        cbMainReviewCause.disableProperty().bind(processClosedProperty);
        labelBaserate.disableProperty().bind(processClosedProperty);
        vbBaserate.disableProperty().bind(processClosedProperty);
        labelHosIdent.disableProperty().bind(processClosedProperty);
        labelStartReview.disableProperty().bind(processClosedProperty);
        cbAdditionalReviewCause.disableProperty().bind(processClosedProperty);
        cbReviewKind.disableProperty().bind(processClosedProperty);
        tfDiffDays.disableProperty().bind(processClosedProperty);
//        tfRisk.disableProperty().bind(processClosedProperty);
        tfResultDelta.disableProperty().bind(processClosedProperty);
        tfPenaltyFee.disableProperty().bind(processClosedProperty);
//        tfDiffMoney.disableProperty().bind(processClosedProperty);
        cbReviewResult.disableProperty().bind(processClosedProperty);
        dpFinishDate.disableProperty().bind(processClosedProperty);
        taComment.disableProperty().bind(processClosedProperty);
        cbAssignableUsers.disableProperty().bind(processClosedProperty);
        btnVersionSelect.disableProperty().bind(processClosedProperty);
//        boxBottom.disableProperty().bind(processClosedProperty);
        //set label text for process "state" 
        processClosedProperty.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    buttonFinishProcess.setText(Lang.getProcessStatusReopen());
                    labelProcessStatus.setText(Lang.getProcessStatusClosed());
                } else {
                    buttonFinishProcess.setText(Lang.getProcessStatusClose());
                    labelProcessStatus.setText(Lang.getProcessStatusOpen());
                }
            }
        });
        //flip disabled property
        buttonFinishProcess.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!prop.isDoFinalisationAllowed()) {
                    RoleProperties roleProp = Session.instance().getRoleProperties();
//                    MainApp.showErrorMessageDialog(new SecurityException("Sie verfügen nicht über die notwendigen Rechte für diese Aktion!"));
                    MainApp.showAuthorizationMessage(new CpxAuthorizationException(roleProp, Lang.getAuthorizationDialogMessage(roleProp == null ? "----" : roleProp.getName(), "Vorgangabschloss") + "\n" + Lang.getAuthorizationDialogMessageContact()));

                    event.consume();
                    return;
                }
                if (processClosedProperty.get()) {
                    if (!readOnlyProperty.get()) {
                        processClosedProperty.set(false);
                    }
                    ((TWmProcessHospital) serviceFacade.getCurrentProcess()).setIsClosed(false);
                    serviceFacade.getCurrentProcess().setWorkflowState(WmWorkflowStateEn.offen);
                    serviceFacade.createAndStoreEvent(WmEventTypeEn.processReopened, null, "");
                    serviceFacade.updateProcess(serviceFacade.getCurrentProcess());
//                    setUpDataForActualValues();
                    //update view with new values
                    setValues();
                } else {

                    if(externDetailsProperty.get() != null && externDetailsProperty.get().getCsdDischargeDate() == null){
                        ConfirmDialog noDisDate = new ConfirmDialog(MainApp.getWindow(), Lang.getNoDischargeDateFound() + "\n" + Lang.getProcessFinalisationProceed());
                        noDisDate.showAndWait().ifPresent(new Consumer<ButtonType>() {
                            @Override
                            public void accept(ButtonType t) {
                                if (t.equals(ButtonType.YES)) {
                                    tryCloseProcess();
                                }else{
                                    return;
                                }
                            }

                        });
                    }else{
                        tryCloseProcess();
                    }
                }                    

                serviceFacade.getProperties().put(WmMainFrameFXMLController.REFRESH_SUMMARY, Boolean.TRUE);

        }

    private void tryCloseProcess(){

        if (serviceFacade.getCurrentProcess().getMainCase().getCsCancellationReasonEn()) {
            MainApp.showInfoMessageDialog(Lang.getProcessFinalisationMainCaseStorno(), MainApp.getWindow());
            taComment.setText(Lang.getProcessFinalisationMainCaseStorno());

        }
        if (cbMainReviewCause.getCheckedNumbers().isEmpty()) {
            //show warning dialog - abort if user 
            //RSH - CPX-625 : 20170829
            // ConfirmDialog noMainReviewCause = new ConfirmDialog(MainApp.getWindow(), Lang.getProcessFinalisationErrorNoMainReviewCause()+"\n"+Lang.getProceed());        
            ConfirmDialog noMainReviewCause = new ConfirmDialog(MainApp.getWindow(), Lang.getProcessFinalisationErrorNoMainReviewCause() + "\n" + Lang.getProcessFinalisationProceed());
//                        noMainReviewCause.getDialogPane().setPrefSize(350, 200);
            noMainReviewCause.showAndWait().ifPresent(new Consumer<ButtonType>() {
                @Override
                public void accept(ButtonType t) {
                    if (t.equals(ButtonType.YES)) {
                        checkOpenRemindersAndSaveResults();

                    }
                }

            });
        } else{
            checkOpenRemindersAndSaveResults();
        }
                
    }

            private void checkOpenRemindersAndSaveResults() {
                int openReminderCount = serviceFacade.getOpenRemindersCount();
                if (openReminderCount > 0) {
                    ConfirmDialog openReminders = new ConfirmDialog(MainApp.getWindow(),
                            "Vorgang enthält noch " + openReminderCount + " offenen Wiedervorlagen.\n" + Lang.getProcessFinalisationProceed());
                    openReminders.showAndWait().ifPresent(new Consumer<ButtonType>() {
                        @Override
                        public void accept(ButtonType t) {
                            if (t.equals(ButtonType.YES)) {
                                saveOrUpdateCompletionResults();
                            }
                        }
                    });
                } else {
                    saveOrUpdateCompletionResults();
                }
            }


            private void saveOrUpdateCompletionResults() {
                processClosedProperty.set(true);
                assert cbAssignableUsers.getSelectedItem() != null : "User is not set";
                serviceFacade.getCurrentProcess().setAssignedUser(cbAssignableUsers.getSelectedId());
                ((TWmProcessHospital) serviceFacade.getCurrentProcess()).setIsClosed(true);
                serviceFacade.getCurrentProcess().setWorkflowState(WmWorkflowStateEn.geschlossen);
                ((TWmProcessHospital) serviceFacade.getCurrentProcess()).setProcessHospitalFinalisation(createOrUpdateProcessResult());
                TWmProcessHospitalFinalisation finalis = ((TWmProcessHospital) serviceFacade.getCurrentProcess()).getProcessHospitalFinalisation();
//                TWmProcessHospitalFinalisation updated = serviceFacade.saveOrUpdateProcessResult(((TWmProcessHospital) serviceFacade.getCurrentProcess()).getProcessHospitalFinalisation());
                if (finalRiskView != null && finalRiskView.getRisk() != null) {
                    TWmFinalisationRisk finalRisk = finalRiskView.getRisk();
//                    throw new UnsupportedOptionException();
//                    TWmRisk finalRisk = finalRiskView.getRisk();
                    finalRisk.setProcessHospitalFinalisation(finalis);
                    finalis.setRisks(finalRisk);
//                    serviceFacade.saveOrUpdateFinalisationRisk(finalRisk);
                }
                serviceFacade.saveOrUpdateProcessResult(finalis);
                //AWi-20180508-CPX-???
                //Add event to history .. yet no description text is specified
                serviceFacade.createAndStoreEvent(WmEventTypeEn.processClosed, null, "");
                serviceFacade.updateProcess(serviceFacade.getCurrentProcess());
            }
        });
        externDetailsProperty.bindBidirectional(gpVersionDetails.getInitialVersionProperty());
        localDetailsProperty.bindBidirectional(gpVersionDetails.getFinalVersionProperty());
    }

    private void setCatalogData(ProcessServiceFacade serviceFacade) {
        List<CMdkAuditreason> auditReasons = serviceFacade.getAvailableAuditReasons();
        List<CMdkAuditreason> auditReasons2 = serviceFacade.getAvailableAuditReasons();
        List<CWmListProcessResult> processResults = serviceFacade.getAvailableProcessResults();
        List<CWmListProcessTopic> processTopics = serviceFacade.getAvailableProcessTopics();
        if (auditReasons.isEmpty() || processResults.isEmpty() || processTopics.isEmpty()) {
            MainApp.showErrorMessageDialog(Lang.getProcessErrorCore_data(Lang.toDate(serviceFacade.getCurrentProcess().getCreationDate())));
        }
        cbAssignableUsers.setItems(MenuCache.instance().getValidUserMapEntries().stream().map((t) -> {
            return t.getValue();
        }).collect(Collectors.toList()));//serviceFacade.getAllUsers());
        cbMainReviewCause.setItems(auditReasons2);
        cbAdditionalReviewCause.setItems(auditReasons);
        cbAdditionalReviewCause.registerTooltip();
        cbReviewResult.setItems(processResults);
        cbReviewResult.getControl().getSelectionModel().selectFirst();
        cbReviewKind.setItems(processTopics);
        cbReviewKind.getControl().getSelectionModel().selectFirst();
    }

    private void setUpDataForActualValues() {
        TCase baseCase = serviceFacade.getCurrentProcess().getMainCase();
//        gpVersionDetails.setCaseType(baseCase.getCsCaseTypeEn());
        localDetailsProperty.set(baseCase.getCurrentLocal());//serviceFacade.getCurrentAssessmentVersion())
        externDetailsProperty.set(serviceFacade.getCurrentKisDetailsVersion());//serviceFacade.getCurrentBillingVersion()//baseCase.getCurrentExtern());
        dpFinishDate.setDate(new Date());
        setBaserate(baseCase.getCurrentExtern());
        //select current user by default
        if (serviceFacade.getCurrentProcess().getAssignedUser() == null) {
            cbAssignableUsers.selectCurrentUser();
        } else {
            cbAssignableUsers.selectById(serviceFacade.getCurrentProcess().getAssignedUser());
        }
        cbReviewKind.selectByIdent(((TWmProcessHospital) serviceFacade.getCurrentProcess()).getProcessTopic());
        if (((TWmProcessHospital) serviceFacade.getCurrentProcess()).getProcessHospitalFinalisation() != null) {
            setProcessResultValues(((TWmProcessHospital) serviceFacade.getCurrentProcess()).getProcessHospitalFinalisation());
        }

    }

    private void setUpControllerForReadOnly() {
        btnVersionSelect.disableProperty().bind(readOnlyProperty);
        cbAdditionalReviewCause.disableProperty().bind(readOnlyProperty);
        cbAssignableUsers.disableProperty().bind(readOnlyProperty);
        cbMainReviewCause.disableProperty().bind(readOnlyProperty);
        cbReviewKind.disableProperty().bind(readOnlyProperty);
        cbReviewResult.disableProperty().bind(readOnlyProperty);
//       buttonFinishProcess.disableProperty().bind(readOnlyProperty);
        taComment.disableProperty().bind(readOnlyProperty);
        tfDiffDays.disableProperty().bind(readOnlyProperty);
//        tfRisk.disableProperty().bind(readOnlyProperty);
        tfResultDelta.disableProperty().bind(readOnlyProperty);
        tfPenaltyFee.disableProperty().bind(readOnlyProperty);
//        tfDiffMoney.disableProperty().bind(readOnlyProperty);

    }

    private void setUpDataForStoredValues() {
        TWmProcessHospital process = (TWmProcessHospital) serviceFacade.getCurrentProcess();

        TWmProcessHospitalFinalisation processResult = process.getProcessHospitalFinalisation();
        if (processResult != null) {
            gpVersionDetails.fillRowsWithStoredData(processResult);
            TCaseDetails initalVersion = serviceFacade.getCaseDetails(processResult.getInitialVersion());
            setBaserate(initalVersion);
            setProcessResultValues(processResult);
            cbAssignableUsers.selectById(process.getAssignedUser());
            if (!cbReviewKind.selectByIdent(process.getProcessTopic())) {
                cbReviewKind.select(serviceFacade.getProcessTopic(process.getProcessTopic()));
            }
            updateRisks();
        } else {
            MainApp.showErrorMessageDialog("No Process result is found in the Database!");
        }

    }

    private TWmProcessHospitalFinalisation createOrUpdateProcessResult() {
        TWmProcessHospitalFinalisation result = ((TWmProcessHospital) serviceFacade.getCurrentProcess()).getProcessHospitalFinalisation();
        if (result == null) {
            result = new TWmProcessHospitalFinalisation();
            result.setProcessHospital((TWmProcessHospital) serviceFacade.getCurrentProcess());
        }
        ((TWmProcessHospital) serviceFacade.getCurrentProcess()).setProcessTopic(cbReviewKind.getSelectedItem() != null ? cbReviewKind.getSelectedItem().getWmPtInternalId() : null);
        result.setAuditReasonsExtended(cbAdditionalReviewCause.getCheckedNumbers());
        result.setMainAuditReasons(cbMainReviewCause.getCheckedNumbers());
        assert dpFinishDate.getDate() != null : "Closing date is not set";
        result.setClosingDate(dpFinishDate.getDate());
        result.setClosingResult(Objects.requireNonNullElse(cbReviewResult.getSelectedInternalId(), -1L));
        VersionOverviewGridPane.CompletionResult compResult = gpVersionDetails.getCompletionResult();
        result.setCwDiff(compResult.getDiffCw());
        result.setCwFinal(compResult.getFinalCw());
        result.setCwInitial(compResult.getInitialCw());
        result.setCwCareDiff(compResult.getDiffCareCw());
        result.setCwCareFinal(compResult.getFinalCareCw());
        result.setCwCareInitial(compResult.getInitialCareCw());
        result.setDrgFinal(compResult.getFinalDrg());
        result.setDrgInitial(compResult.getInitialDrg());
        if(gpVersionDetails.getFinalVersion() != null){
            result.setFinalVersion(gpVersionDetails.getFinalVersion().getId());
            updateVersionType(gpVersionDetails.getFinalVersion(),VersionRiskTypeEn.CASE_FINALISATION);
        }
        result.setFinalVersionComment(compResult.getFinalComment());
        result.setFinalVersionNumber(compResult.getFinalVersion());
        if(gpVersionDetails.getInitialVersion() != null){
            result.setInitialVersion(gpVersionDetails.getInitialVersion().getId());
        }
        result.setInitialVersionComment(compResult.getInitialComment());
        result.setInitialVersionNumber(compResult.getInitialVersion());
        result.setLosDiff(compResult.getDiffLos().intValue());
        result.setLosFinal(compResult.getFinalLos().intValue());
        result.setLosInitial(compResult.getInitialLos().intValue());
        result.setResultComment(taComment.getText());
        result.setRevenueDiff(compResult.getDiffRevenue());
        result.setRevenueFinal(compResult.getFinalRevenue());
        result.setRevenueInitial(compResult.getInitialRevenue());
        result.setFinalSupplementaryFee(compResult.getFinalSuppFee());
        result.setInitialSupplementaryFee(compResult.getInitialSuppFee());
        result.setDiffSupplementaryFee(compResult.getDiffSupplementaryFee());

        //these whacky textfields, are some redundant?
        result.setSavedDays(Objects.requireNonNullElse(tfDiffDays.getValue(), 0));
//        result.setRisk(!tfRisk.getText().isEmpty() ? Double.valueOf(tfRisk.getText()) : 0);
        result.setResultDelta(Objects.requireNonNullElse(tfResultDelta.getValue(), 0.0));
        result.setPenaltyFee(Objects.requireNonNullElse(tfPenaltyFee.getValue(), 0.0));
//        result.setSavedMoney(!tfDiffMoney.getText().isEmpty() ? Double.valueOf(tfDiffMoney.getText()) : 0.0);
        if (finalRiskView != null && finalRiskView.getRisk() != null) {
//            result.setRisk(finalRiskView.getRisk().getRiskPercentTotal());
            result.setSavedMoney(finalRiskView.getRisk().getRiskValueTotal().doubleValue());
        }
        return result;
    }

    private void setProcessResultValues(TWmProcessHospitalFinalisation processResult) {
        dpFinishDate.setDate(processResult.getClosingDate());
        cbAdditionalReviewCause.checkByNumbers(processResult.getAuditReasonsExtendedList());
        cbMainReviewCause.checkByNumbers(processResult.getMainAuditReasonList());
        cbReviewResult.selectByInternalId(processResult.getClosingResult());
        tfDiffDays.setValue(processResult.getSavedDays());
//        tfRisk.setText(String.valueOf(processResult.getRisk()));
        tfResultDelta.setValue(processResult.getResultDelta());
        tfPenaltyFee.setValue(processResult.getPenaltyFee());
//        tfDiffMoney.setText(Double.toString(processResult.getSavedMoney()));
        taComment.setText(processResult.getResultComment());
    }

    @FXML
    private void onVersionSelect(ActionEvent event) {
        if (serviceFacade.getCurrentProcess().getMainCase().getCsCancellationReasonEn()) {
            MainApp.showInfoMessageDialog(Lang.getProcessFinalisationVersionComparisonNotallowed(), MainApp.getWindow());
            event.consume();
        } else {
            VersionSelectionDialog dialog = new VersionSelectionDialog();
            dialog.showAndWait();
        }
    }

    private String getDrgText(CaseTypeEn csCaseTypeEn) {
        switch (csCaseTypeEn) {
            case DRG:
                return Lang.getDRGObj().getAbbreviation();
            case PEPP:
                return Lang.getPEPP();
            default:
                return "----";
        }
    }

    private void determinAuditReasonsForLastRequest() {
        TWmRequest req = serviceFacade.getLastObsRequest();
        List<Long> ids = new ArrayList<>();
        if (req != null) {
            req.getAuditReasons(false).forEach((reason) -> {
                ids.add(reason.getAuditReasonNumber());
            });
            List<Long> idsExtended = new ArrayList<>();
            req.getAuditReasons(true).forEach((reason) -> {
                idsExtended.add(reason.getAuditReasonNumber());
            });
            cbMainReviewCause.checkByNumbers(ids);
            cbAdditionalReviewCause.checkByNumbers(idsExtended);
        }
    }

    private void setUpRiskArea() throws IOException {
        if (riskManager == null) {
            riskManager = new CompletionRiskManager(serviceFacade);
        }
        riskManager.load();
        //create and sync bars
        ProcessCompletionRisk initialRisk = createAndSetInitialRisk(riskManager);
        List<ProcessCompletionRisk<TWmRisk, TWmRiskDetails>> mdRisks = createAndSetMdRisks(riskManager);
        ProcessCompletionRisk finalRisk = createAndSetFinalRisk(riskManager);

        ProcessCompletionRiskSkin finalRiskSkin = new ProcessCompletionRiskSkin(finalRisk);
        finalRisk.setSkin(finalRiskSkin);
        ScrollBar finalScrollBar = finalRiskSkin.getHBar();

        ProcessCompletionRiskSkin initialRiskSkin = new ProcessCompletionRiskSkin(initialRisk);
        initialRisk.setSkin(initialRiskSkin);
        ScrollBar initialSrollBar = initialRiskSkin.getHBar();
        initialSrollBar.valueProperty().bindBidirectional(finalScrollBar.valueProperty());

        for (ProcessCompletionRisk<TWmRisk, TWmRiskDetails> mdRisk : mdRisks) {
            ProcessCompletionRiskSkin mdRiskSkin = new ProcessCompletionRiskSkin(mdRisk);
            mdRisk.setSkin(mdRiskSkin);
            ScrollBar mdRiskSrollBar = mdRiskSkin.getHBar();
            mdRiskSrollBar.valueProperty().bindBidirectional(finalScrollBar.valueProperty());
        }
    }

    private double computeSpMdRiskWidth(List<TWmRisk> pRisks) {
        if (pRisks == null) {
            return 0.0;
        }
        switch (pRisks.size()) {
            case 0:
                return 0;
            case 1:
                return 225;
            default:
                return 440;
        }
    }

    private Insets computeSpMdRiskPadding(List<TWmRisk> pRisks) {
        if (pRisks == null) {
            return new Insets(0);
        }
        if (pRisks.size() <= 2) {
            return new Insets(0, 0, 10, 0);
        }
        return new Insets(0);
    }

    private void updateRisks() {
        if (riskManager != null && !isProcessClosed()) {
            if(!riskManager.hasFinalRiskChanged()){
                riskManager.cacheFinalRiskState(finalRiskView.getRisk());
            }else{
                riskManager.cacheFinalRiskState(null);
            }
        }
        boxInitialRisk.getChildren().clear();
        boxMdRisk.getChildren().clear();
        boxFinalRisk.getChildren().clear();
        try {
            setUpRiskArea();
        } catch (IOException ex) {
            LOG.warning("can not set up risk layout due to fxml error!");
            Logger.getLogger(ProcessCompletionFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ProcessCompletionRisk<TWmRisk,TWmRiskDetails> createAndSetInitialRisk(CompletionRiskManager manager) {
        ProcessCompletionRisk<TWmRisk,TWmRiskDetails> caseRiskView = new CompletionVersionRisk(manager.getInitialCaseRisk());
        Bindings.bindContent(caseRiskView.riskAreas(), manager.riskAreas());
        VBox.setVgrow(caseRiskView, Priority.ALWAYS);
        boxInitialRisk.getChildren().add(caseRiskView);
        return caseRiskView;
    }

    private List<ProcessCompletionRisk<TWmRisk,TWmRiskDetails>> createAndSetMdRisks(CompletionRiskManager manager) {
        List<TWmRisk> mdRisks = manager.getMdRequestRisks();
        if (mdRisks == null || mdRisks.isEmpty()) {
            //remove scrollpane
            if (boxRiskResult.getChildren().contains(spMdRisk)) {
                boxRiskResult.getChildren().remove(spMdRisk);
                return new ArrayList<>();
            }
        } else {
            if (!boxRiskResult.getChildren().contains(spMdRisk)) {
                boxRiskResult.getChildren().add(spMdRisk);
            }
        }
        spMdRisk.setPrefWidth(computeSpMdRiskWidth(mdRisks));
        spMdRisk.setPadding(computeSpMdRiskPadding(mdRisks));
        List<ProcessCompletionRisk<TWmRisk,TWmRiskDetails>> mdCompeletionRisks = new ArrayList<>();
        if (mdRisks != null) {
            for (TWmRisk risk : mdRisks) {
                ProcessCompletionRisk mdRiskView = new CompletionVersionRisk(risk);
                Bindings.bindContent(mdRiskView.riskAreas(), manager.riskAreas());
                boxMdRisk.getChildren().add(mdRiskView);
                mdCompeletionRisks.add(mdRiskView);
                manager.setRiskAreaValuesForFinalRisk(risk);
            }
        }
        manager.getLastRequestRiskAreaValues();
        return mdCompeletionRisks;
    }

    private CompletionFinalRisk createAndSetFinalRisk(CompletionRiskManager manager) {
        TWmFinalisationRisk risk = manager.getFinalRisk();
        finalRiskView = new CompletionFinalRisk(risk);//ProcessCompletionRisk(manager.getFinalRisk());
        finalRiskView.setRiskAreaValueMap(manager.getLastRequestRiskAreaValues());
        finalRiskView.setShowVBar(true);
        finalRiskView.setDisplayMode(RiskDisplayMode.RISK_VALUE_ONLY);
        Bindings.bindContent(finalRiskView.riskAreas(), manager.riskAreas());
        finalRiskView.editableProperty().bind(processClosedProperty.not());
        VBox.setVgrow(finalRiskView, Priority.ALWAYS);
        finalRiskView.setDeleteRiskArea(new Callback<RiskAreaEn, Boolean>() {
            @Override
            public Boolean call(RiskAreaEn param) {
//                throw new UnsupportedOptionException();
                return manager.removeRiskArea(param);
            }
        });
        finalRiskView.setAddRiskArea(new Callback<RiskAreaEn, Boolean>() {
            @Override
            public Boolean call(RiskAreaEn param) {
                return manager.addRiskArea(param);
            }
        });
        boxFinalRisk.getChildren().add(finalRiskView);
        return finalRiskView;
    }

    private void updateVersionType(TCaseDetails pCaseVersion, VersionRiskTypeEn pVersionType) {
        if(pVersionType == null){
            return;
        }
        if(pCaseVersion == null){
            return;
        }
        if(!pVersionType.equals(pCaseVersion.getCsdVersRiskTypeEn())){
            ConfirmDialog diag = new ConfirmDialog(MainApp.getWindow(), "Der Versionstyp der Version: " + VersionStringConverter.convertSimple(pCaseVersion) +
                    " entspricht nicht dem erwarteten Typ: " + pVersionType.getTranslation().getValue()+"!\n"+
                    "Soll dies korrigiert werden?");
            diag.showAndWait().ifPresent(new Consumer<ButtonType>() {
                @Override
                public void accept(ButtonType t) {
                    if(ButtonType.YES.equals(t)){
                        pCaseVersion.setCsdVersRiskTypeEn(pVersionType);
                        serviceFacade.saveOrUpdateCaseVersion(pCaseVersion);
                    }
                }
            });
        }
    }


    private class VersionSelectionDialog extends TitledDialog {

        private Label lblKisText;
        private Label lblCpText;
        //billing
        private VersionOverview kisVersions;
        //assessment
        private VersionOverview cpVersions;
        private VersionOverviewGridPane gpSelected;

        VersionSelectionDialog() {
            super(Lang.getProcessFinalisationVersionSelect(), MainApp.getWindow(), Modality.APPLICATION_MODAL, true);
//            getDialogSkin().setMinWidth(1200);
            createContent();
            setLanguage();
            setValues();
            resultProperty().addListener(new ChangeListener<ButtonType>() {
                @Override
                public void changed(ObservableValue<? extends ButtonType> observable, ButtonType oldValue, ButtonType newValue) {
                    if (newValue.equals(ButtonType.OK)) {
                        externDetailsProperty.set(kisVersions.getSelectionModel().getSelectedItem());
                        localDetailsProperty.set(cpVersions.getSelectionModel().getSelectedItem());
                    }
                }
            });
        }

        private void createContent() {
            CaseTypeEn type = serviceFacade.getCurrentProcess().getMainCase().getCsCaseTypeEn();
            lblKisText = new Label();
            kisVersions = new VersionOverview(type);
            lblCpText = new Label();
            cpVersions = new VersionOverview(type);
            gpSelected = new VersionOverviewGridPane(type);
            VBox wrapper = new VBox(lblKisText, kisVersions, lblCpText, cpVersions, gpSelected);
//            VBox wrapper = new VBox(cpVersions);
            wrapper.setSpacing(5.0);
            wrapper.setFillWidth(true);
            setContent(wrapper);
        }

        private void setLanguage() {
            lblKisText.setText("Abrechnung-Version" + " (" + Lang.getProcessInitial() + ")");
            lblCpText.setText("Ergebnis-Version" + " (" + Lang.getProcessFinal() + ")");
//            getDialogSkin().getButton(ButtonType.OK).setText(Lang.get);
        }

        private void setValues() {
            gpSelected.initServiceFacade(serviceFacade);
            TCase baseCase = serviceFacade.getCurrentProcess().getMainCase();
            //assesment
            cpVersions.getItems().addAll(baseCase.getLocals());//serviceFacade.getAssessmentVersions(baseCase)
            //billing
            kisVersions.getItems().addAll(baseCase.getExterns());

            gpSelected.getInitialVersionProperty().bind(kisVersions.getSelectionModel().selectedItemProperty());
            gpSelected.getFinalVersionProperty().bind(cpVersions.getSelectionModel().selectedItemProperty());

            if (localDetailsProperty.get() != null) {
                cpVersions.getSelectionModel().select(localDetailsProperty.get());
            } else {
                cpVersions.getSelectionModel().selectFirst();
            }
            if (externDetailsProperty.get() != null) {
                kisVersions.getSelectionModel().select(externDetailsProperty.get());
            } else {
                kisVersions.getSelectionModel().selectFirst();
            }
        }

    }
    //Tableview to show version and groupingResult data in the version selection dialog

    private class VersionOverview extends TableView<TCaseDetails> {

        private final Map<TCaseDetails, TGroupingResults> groupingResultsMap = new HashMap<>();

        /**
         * creates new instance for the choosen type
         *
         * @param pType case type drg,pepp..etc
         */
        VersionOverview(CaseTypeEn pType) {
            super();
            //add case type switch
            setPrefHeight(200);
            setPrefWidth(800);
            setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
            getStyleClass().add("stay-selected-table-view");
            //react to changes to the item set
            getItems().addListener(new ListChangeListener<TCaseDetails>() {
                @Override
                public void onChanged(ListChangeListener.Change<? extends TCaseDetails> c) {
                    while (c.next()) {
                        if (c.wasAdded()) {
                            for (TCaseDetails added : c.getAddedSubList()) {
                                loadGroupingResults(added);
                            }
                        }
                        if (c.wasRemoved()) {
                            for (TCaseDetails removed : c.getRemoved()) {
                                groupingResultsMap.remove(removed);
                            }
                        }
                    }
                }
            });
            switch (pType) {
                case DRG:
                    setDrgColumns();
                    break;
                case PEPP:
                    setPeppColumns();
                    break;
                default:
                    setDefaultColumns();
            }
        }

        @SuppressWarnings("unchecked")
        private void setDrgColumns() {
            DrgCatalogCodeColumn codCol = new DrgCatalogCodeColumn();
            DrgTextColumn textCol = new DrgTextColumn();
            CwEffColumn cwEffCol = new CwEffColumn();
            PcclCol pcclCol = new PcclCol();
//            SurchargeColumn surCol = new SurchargeColumn();
//            DeductionColumn dedCol = new DeductionColumn();
            CorrectionColumn corrCol = new CorrectionColumn();
            DrgRevColumn revCol = new DrgRevColumn();
            SupplFeeSfColumn supCol = new SupplFeeSfColumn();
            CommentColumn comCol = new CommentColumn();
            VersionColumn verCol = new VersionColumn();

            textCol.prefWidthProperty().bind(
                    widthProperty()
                            .subtract(codCol.widthProperty())
                            .subtract(cwEffCol.widthProperty())
                            .subtract(pcclCol.widthProperty())
                            //                            .subtract(surCol.widthProperty())
                            //                            .subtract(dedCol.widthProperty())
                            .subtract(corrCol.widthProperty())
                            .subtract(revCol.widthProperty())
                            .subtract(supCol.widthProperty())
                            .subtract(verCol.widthProperty())
                            .subtract(2) // a border stroke?
                            .multiply(0.5d)
            );
            comCol.prefWidthProperty().bind(
                    widthProperty()
                            .subtract(codCol.widthProperty())
                            .subtract(cwEffCol.widthProperty())
                            .subtract(pcclCol.widthProperty())
                            //                            .subtract(surCol.widthProperty())
                            //                            .subtract(dedCol.widthProperty())
                            .subtract(corrCol.widthProperty())
                            .subtract(revCol.widthProperty())
                            .subtract(supCol.widthProperty())
                            .subtract(verCol.widthProperty())
                            .subtract(2) // a border stroke?
                            .multiply(0.5d)
            );
            textCol.setResizable(false);
            comCol.setResizable(false);

            getColumns().addAll(codCol,
                    textCol,
                    cwEffCol,
                    pcclCol,
                    //                    surCol,
                    //                    dedCol,
                    corrCol,
                    revCol,
                    supCol,
                    verCol,
                    comCol
            );
        }

        @Override
        public void refresh() {
            reloadAllGroupingResults();
            super.refresh(); //To change body of generated methods, choose Tools | Templates.
        }

        private void reloadAllGroupingResults() {
            getItems().forEach((item) -> {
                loadGroupingResults(item);
            });
        }

        private void loadGroupingResults(TCaseDetails pDetails) {
            groupingResultsMap.put(pDetails, serviceFacade.getDrgResult(pDetails.getId(), CpxClientConfig.instance().getSelectedGrouper()));
        }

        @SuppressWarnings("unchecked")
        private void setPeppColumns() {
            PeppCatalogCodeColumn codCol = new PeppCatalogCodeColumn();
            PeppTextColumn textCol = new PeppTextColumn();
            PeppCwColumn cwEffCol = new PeppCwColumn();
            PcclCol pcclCol = new PcclCol();
            PeppInfCareColumn surCol = new PeppInfCareColumn();
            PeppAdultCareColumn dedCol = new PeppAdultCareColumn();
            PeppRevColumn revCol = new PeppRevColumn();
            SupplFeeSpColumn supCol = new SupplFeeSpColumn();
            SupplFeeDfColumn supdfCol = new SupplFeeDfColumn();
            CommentColumn comCol = new CommentColumn();
            VersionColumn verCol = new VersionColumn();

            textCol.prefWidthProperty().bind(
                    widthProperty()
                            .subtract(codCol.widthProperty())
                            .subtract(cwEffCol.widthProperty())
                            .subtract(pcclCol.widthProperty())
                            .subtract(surCol.widthProperty())
                            .subtract(dedCol.widthProperty())
                            .subtract(revCol.widthProperty())
                            .subtract(supCol.widthProperty())
                            .subtract(supdfCol.widthProperty())
                            .subtract(verCol.widthProperty())
                            .subtract(2) // a border stroke?
                            .multiply(0.5d)
            );
            comCol.prefWidthProperty().bind(
                    widthProperty()
                            .subtract(codCol.widthProperty())
                            .subtract(cwEffCol.widthProperty())
                            .subtract(pcclCol.widthProperty())
                            .subtract(surCol.widthProperty())
                            .subtract(dedCol.widthProperty())
                            .subtract(revCol.widthProperty())
                            .subtract(supCol.widthProperty())
                            .subtract(supdfCol.widthProperty())
                            .subtract(verCol.widthProperty())
                            .subtract(2) // a border stroke?
                            .multiply(0.5d)
            );
            textCol.setResizable(false);
            comCol.setResizable(false);

            getColumns().addAll(codCol,
                    textCol,
                    cwEffCol,
                    pcclCol,
                    surCol,
                    dedCol,
                    revCol,
                    supCol,
                    supdfCol,
                    verCol,
                    comCol
            );
        }

        @SuppressWarnings("unchecked")
        private void setDefaultColumns() {
            CommentColumn comCol = new CommentColumn();
            VersionColumn verCol = new VersionColumn();
            comCol.prefWidthProperty().bind(
                    widthProperty()
                            .subtract(verCol.widthProperty())
                            .subtract(2) // a border stroke?
                            .multiply(0.5d)
            );
            comCol.setResizable(false);

            getColumns().addAll(
                    verCol,
                    comCol
            );
        }

        private class DrgCatalogCodeColumn extends DrgCodeColumn<TCaseDetails> {

            DrgCatalogCodeColumn() {
                super();
                setSortable(false);
                setResizable(false);
                setMinWidth(55.0);
                setMaxWidth(65.0);
            }

            @Override
            public TCaseDrg getValue(TCaseDetails pValue) {
                return (TCaseDrg) groupingResultsMap.get(pValue);
            }
        }

        private class PeppCatalogCodeColumn extends PeppCodeColumn<TCaseDetails> {

            PeppCatalogCodeColumn() {
                super();
                setSortable(false);
                setResizable(false);
                setMinWidth(55.0);
                setMaxWidth(65.0);
            }

            @Override
            public TCasePepp getValue(TCaseDetails pValue) {
                return (TCasePepp) groupingResultsMap.get(pValue);
            }
        }

        private class DrgTextColumn extends DrgCatalogColumn<TCaseDetails> {

            DrgTextColumn() {
                super();
                setSortable(false);
                setMinWidth(50);
            }

            @Override
            public TCaseDrg getValue(TCaseDetails pValue) {
                return (TCaseDrg) groupingResultsMap.get(pValue);
            }
        }

        private class PeppTextColumn extends PeppCatalogColumn<TCaseDetails> {

            PeppTextColumn() {
                super();
                setSortable(false);
                setMinWidth(50);
            }

            @Override
            public TCasePepp getValue(TCaseDetails pValue) {
                return (TCasePepp) groupingResultsMap.get(pValue);
            }
        }

        private class CwEffColumn extends DrgCwEffColumn<TCaseDetails> {

            CwEffColumn() {
                super();
                setSortable(false);
                setResizable(false);
                setMinWidth(70.0);
                setMaxWidth(75.0);
            }

            @Override
            public TCaseDrg getValue(TCaseDetails pValue) {
                return (TCaseDrg) groupingResultsMap.get(pValue);
            }
        }

        private class PeppCwColumn extends PeppCwEffColumn<TCaseDetails> {

            PeppCwColumn() {
                super();
                setSortable(false);
                setResizable(false);
                setMinWidth(70.0);
                setMaxWidth(75.0);
            }

            @Override
            public TCasePepp getValue(TCaseDetails pValue) {
                return (TCasePepp) groupingResultsMap.get(pValue);
            }
        }

        private class PcclCol extends PcclColumn<TCaseDetails> {

            PcclCol() {
                super();
                setSortable(false);
                setResizable(false);
                setMinWidth(48.0);
                setMaxWidth(48.0);
            }

            @Override
            public TGroupingResults getValue(TCaseDetails pValue) {
                return groupingResultsMap.get(pValue);
            }
        }

//        private class DeductionColumn extends DrgDeductionColumn<TCaseDetails> {
//
//            DeductionColumn() {
//                super();
//                setResizable(false);
//                setMinWidth(65.0);
//                setMaxWidth(65.0);
//                setSortable(false);
//            }
//
//            @Override
//            public TCaseDrg getValue(TCaseDetails pValue) {
//                return (TCaseDrg) groupingResultsMap.get(pValue);
//            }
//        }
//
//        private class SurchargeColumn extends DrgSurchargeColumn<TCaseDetails> {
//
//            SurchargeColumn() {
//                super();
//                setResizable(false);
//                setMinWidth(65.0);
//                setMaxWidth(65.0);
//                setSortable(false);
//            }
//
//            @Override
//            public TCaseDrg getValue(TCaseDetails pValue) {
//                return (TCaseDrg) groupingResultsMap.get(pValue);
//            }
//        }
        private class CorrectionColumn extends DrgCorrectionColumn<TCaseDetails> {

            CorrectionColumn() {
                super();
                setResizable(false);
                setMinWidth(80.0);
                setMaxWidth(80.0);
                setSortable(false);
            }

            @Override
            public TCaseDrg getValue(TCaseDetails pValue) {
                return (TCaseDrg) groupingResultsMap.get(pValue);
            }

        }

        private class PeppInfCareColumn extends PeppCarePercInfColumn<TCaseDetails> {

            PeppInfCareColumn() {
                super();
                setResizable(false);
                setMinWidth(95.0);
                setMaxWidth(95.0);
                setSortable(false);
            }

            @Override
            public TCasePepp getValue(TCaseDetails pValue) {
                return (TCasePepp) groupingResultsMap.get(pValue);
            }
        }

        private class PeppAdultCareColumn extends PeppCarePercAdultColumn<TCaseDetails> {

            PeppAdultCareColumn() {
                super();
                setResizable(false);
                setMinWidth(75.0);
                setMaxWidth(75.0);
                setSortable(false);
            }

            @Override
            public TCasePepp getValue(TCaseDetails pValue) {
                return (TCasePepp) groupingResultsMap.get(pValue);
            }
        }

        private class DrgRevColumn extends RevenueColumn<TCaseDetails> {

            DrgRevColumn() {
                super();
                setSortable(false);
                setResizable(false);
                setMinWidth(100.0);
                setMaxWidth(100.0);
            }

            @Override
            public Double getValue(TCaseDetails pValue) {
                TCaseDrg grouperResult = (TCaseDrg) groupingResultsMap.get(pValue);
                if (grouperResult == null) {
                    return 0.0;
                }
                return grouperResult.getRevenue(serviceFacade.getDrgBaseRateFeeValue(pValue), serviceFacade.getCareBaseRateFeeValue(pValue));
            }
        }

        private class PeppRevColumn extends RevenueColumn<TCaseDetails> {

            PeppRevColumn() {
                super();
                setSortable(false);
                setResizable(false);
                setMinWidth(100.0);
                setMaxWidth(100.0);
            }

            @Override
            public Double getValue(TCaseDetails pValue) {
                TCasePepp grouperResult = (TCasePepp) groupingResultsMap.get(pValue);
                if (grouperResult == null) {
                    return 0.0;
                }
                return grouperResult.getRevenue();
            }
        }

        private class SupplFeeSfColumn extends DrgSuppFeeColumn<TCaseDetails> {

            SupplFeeSfColumn() {
                super();
                setSortable(false);
                setResizable(false);
                setMinWidth(100.0);
                setMaxWidth(100.0);
            }

            @Override
            public Double getValue(TCaseDetails pValue) {
                return serviceFacade.findSupplementaryFee(pValue.getId(), SupplFeeTypeEn.ZE);
            }
        }

        private class SupplFeeSpColumn extends PeppSuppFeeColumn<TCaseDetails> {

            SupplFeeSpColumn() {
                super();
                setSortable(false);
                setResizable(false);
                setMinWidth(100.0);
                setMaxWidth(100.0);
            }

            @Override
            public Double getValue(TCaseDetails pValue) {
                return serviceFacade.findSupplementaryFee(pValue.getId(), SupplFeeTypeEn.ZP);
            }
        }

        private class SupplFeeDfColumn extends PeppDailyFeeValueColumn<TCaseDetails> {

            SupplFeeDfColumn() {
                super();
                setSortable(false);
                setResizable(false);
                setMinWidth(100.0);
                setMaxWidth(100.0);
            }

            @Override
            public Double getValue(TCaseDetails pValue) {
                return serviceFacade.findSupplementaryFee(pValue.getId(), SupplFeeTypeEn.ET);
            }
        }

        private class CommentColumn extends TableColumn<TCaseDetails, String> {

            CommentColumn() {
                super(Lang.getVersioncontrollComment());
                setSortable(false);
                setMinWidth(50);
                setMaxWidth(Double.MAX_VALUE);
                setCellValueFactory(new Callback<CellDataFeatures<TCaseDetails, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<TCaseDetails, String> param) {
                        return new SimpleStringProperty(param.getValue().getComment());
                    }
                });
                setCellFactory(new Callback<TableColumn<TCaseDetails, String>, TableCell<TCaseDetails, String>>() {
                    @Override
                    public TableCell<TCaseDetails, String> call(TableColumn<TCaseDetails, String> param) {
                        return new TableCell<TCaseDetails, String>() {
                            @Override
                            protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                                if (item == null || empty) {
                                    setGraphic(null);
                                    return;
                                }
                                //sets label in topleft, to prevent layout error 
                                Label label = new Label(item);
                                setAlignment(Pos.TOP_LEFT);
                                setGraphic(label);
                                OverrunHelper.addOverrunInfoButton(label, item);
                            }

                        };
                    }
                });
            }

        }

        private class VersionColumn extends TableColumn<TCaseDetails, String> {

            VersionColumn() {
                super(Lang.getVersion());

                setSortable(false);
                setResizable(false);
                setMinWidth(55.0);
                setMaxWidth(65.0);
                setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TCaseDetails, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<TCaseDetails, String> param) {
                        return new SimpleStringProperty(String.valueOf(param.getValue().getCsdVersion()));
                    }
                });
            }

        }
    }
}
