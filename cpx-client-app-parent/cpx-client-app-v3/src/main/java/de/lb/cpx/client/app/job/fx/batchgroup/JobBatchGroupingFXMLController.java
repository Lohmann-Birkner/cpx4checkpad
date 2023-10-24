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
package de.lb.cpx.client.app.job.fx.batchgroup;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.rulefilter.dialog.RuleFilterApplicationUsage;
import de.lb.cpx.client.app.rulefilter.dialog.RuleFilterDialog;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.connection.jms.BatchTaskMessageHandler;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.CpxScreen;
import de.lb.cpx.client.core.model.fx.alert.NotificationsFactory;
import de.lb.cpx.client.core.model.fx.button.StartStopButton;
import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledDatePicker;
import de.lb.cpx.client.core.model.fx.labeled.LabeledLabel;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.client.core.model.fx.progress.FlowrateBar;
import de.lb.cpx.client.core.model.fx.progress.RingProgressIndicator;
import de.lb.cpx.client.core.model.fx.section.SectionHeader;
import de.lb.cpx.client.core.model.task.CpxTask;
import de.lb.cpx.client.core.util.BatchJobHelper;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.grouper.model.transfer.BatchGroupParameter;
import de.lb.cpx.model.TBatchCheckResult;
import de.lb.cpx.model.TBatchGroupParameter;
import de.lb.cpx.model.TBatchResult;
import de.lb.cpx.model.TBatchResult2Role;
import de.lb.cpx.model.enums.DetailsFilterEn;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.service.ejb.CpxP21ImportBeanRemote;
import de.lb.cpx.service.properties.CpxAuthorizationException;
import de.lb.cpx.shared.dto.BatchGroupingDTO;
import de.lb.cpx.shared.dto.LockException;
import de.lb.cpx.shared.lang.Lang;
import java.net.URL;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static javafx.scene.layout.Region.USE_PREF_SIZE;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.batch.runtime.BatchStatus;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 * FXML Controller class
 *
 * @author wilde
 */
public class JobBatchGroupingFXMLController extends Controller<CpxScreen> {

    private static final Logger LOG = Logger.getLogger(JobBatchGroupingFXMLController.class.getName());

    @FXML
    private SectionHeader shOverallHeader;
    @FXML
    private Label lblSettingsFunctions;
    @FXML
    private Label lblComment;
    @FXML
    private Label lblCaseDetailsCount;
    @FXML
    private CheckBox chkBatchGroupingDoRules;
    @FXML
    private CheckBox chkBatchGroupingDoRulesSimulate;
    @FXML
    private CheckBox chkBatchGroupingSupplementaryFee;
    @FXML
    private CheckBox chkBatchGroupingDoSimulate;
    @FXML
    private Label lblSettingsFilter;
    @FXML
    private CheckBox chkBatchGroupingGrouped;
    @FXML
    private LabeledComboBox<DetailsFilterEn> cbDetailsFilter;
//    @FXML
//    private CheckBox chkBatchGroupingExtern;
    @FXML
    private CheckBox chkBatchGroupingDoForActualRoleOnly;
//    @FXML
//    private CheckBox chkBatchGroupingMedAndRemedies;
    @FXML
    private StartStopButton btnStartStop;
    @FXML
    private Button btnRuleFilter;
    @FXML
    private RingProgressIndicator riProgress;
    @FXML
    private FlowrateBar fbProgress;
    @FXML
    private GridPane gpResultLayout;
    @FXML
    private SectionHeader shDrgResult;
    @FXML
    private LabeledLabel llBatchGroupingGroupedcases;
    @FXML
    private LabeledLabel llBatchGroupingErrordrgpepp;
    @FXML
    private LabeledLabel llBatchGroupingIntensivcare;
    @FXML
    private LabeledLabel llBatchGroupingKorrtrans;
    @FXML
    private LabeledLabel llBatchGroupingPccl;
    @FXML
    private LabeledLabel llBatchGroupingLos;
    @FXML
    private LabeledLabel llBatchGroupingKorrugvd;
    @FXML
    private LabeledLabel llBatchGroupingUnkcmi;
    @FXML
    private LabeledLabel llBatchGroupingCwdiffpos;
    @FXML
    private LabeledLabel llBatchGroupingTotalcases;
    @FXML
    private LabeledLabel llBatchGroupingDayprecare;
    @FXML
    private LabeledLabel llBatchGroupingNdi;
    @FXML
    private LabeledLabel llBatchGroupingNursingdays;
    @FXML
    private LabeledLabel llBatchGroupingDaysextern;
    @FXML
    private LabeledLabel llBatchGroupingKorrogvd;
    @FXML
    private LabeledLabel llBatchGroupingNinerdiag;
    @FXML
    private LabeledLabel llBatchGroupingCmieff;
    @FXML
    private LabeledLabel llBatchGroupingCwdiffneg;
    @FXML
    private SectionHeader shRuleResult;
    @FXML
    private TableView<TBatchCheckResult> tvDetectedRules;

    @FXML
    private TableColumn<TBatchCheckResult, String> columnTyp;
    @FXML
    private TableColumn<TBatchCheckResult, String> columnError;
    @FXML
    private TableColumn<TBatchCheckResult, String> columnWarning;
    @FXML
    private TableColumn<TBatchCheckResult, String> columnClues;
     @FXML
    private VBox vFunctionsId;
     
    private final EjbProxy<CpxP21ImportBeanRemote> grouperBean = Session.instance().getEjbConnector().connectCpxP21ImportBean();
    private ObjectProperty<Boolean> showResultLayout;
    private final SetProperty<Long> caseIds = new SimpleSetProperty<>();
    private final ReadOnlyBooleanWrapper isTaskRunning = new ReadOnlyBooleanWrapper(false);
    @FXML
    private VBox boxContent;
    private BatchJob job;
    @FXML
    private VBox boxLoadingLayout;
    @FXML
    private Label lblGroupingStatus;
    @FXML
    private VBox boxStartStop;
//    @FXML
//    private Label lblResultDate;
    @FXML
    private Label lblSettingsAdvanced;
    @FXML
    private LabeledTextField txtQueueSize;
    @FXML
    private LabeledTextField txtThreadCount;
    @FXML
    private LabeledTextField txtBlockSize;
    @FXML
    private CheckBox chkBatchGroupingDisableWriter;
    @FXML
    private GridPane settingsGrid;
    @FXML
    private VBox advancedBox;
    @FXML
    private VBox boxDisplayArea;
    @FXML
    private ScrollPane spContent;
    @FXML
    private LabeledDatePicker dtAdmissionDateFrom;
    @FXML
    private LabeledDatePicker dtAdmissionDateUntil;
    @FXML
    private LabeledDatePicker dtDischargeDateFrom;
    @FXML
    private LabeledDatePicker dtDischargeDateUntil;
    @FXML
    private Label lblConstraints;
    @FXML
    private VBox constraintsBox;
    @FXML
    private VBox datesBox;
    @FXML
    private HBox casesBox;
    @FXML
    private Label lblCaseIdsCount;
    @FXML
    private Button btnResetCaseIds;
    @FXML
    private CheckBox chkBatchGroupingDoHistoryCases;
    @FXML
    private HBox flowBarContainer;
    @FXML
    private VBox  buttonBox;
    @FXML
    private GridPane formerResults;
    
    public ObjectProperty<Boolean> showResultLayoutProperty(){
        if(showResultLayout == null){
            showResultLayout = new SimpleObjectProperty<>();
        }
        return showResultLayout;
    }
    public void setShowResultLayout(boolean pShow){
        showResultLayoutProperty().set(pShow);
    }
    public Boolean getShowResultLayout(){
        return Objects.requireNonNullElse(showResultLayoutProperty().get(),false);
    }
    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        setUpLanguage();
        setUpSettingsListener();
        setUpTableColumns();
        updateCaseDetailsCount();
        formerResults.maxHeightProperty().bind(vFunctionsId.heightProperty().subtract(lblCaseDetailsCount.heightProperty()).subtract(buttonBox.heightProperty()).subtract(20.0));
        Glyph filterGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.LIST_ALT);
        filterGlyph.setStyle("-fx-text-fill: white;");
        btnRuleFilter.setGraphic(filterGlyph);
        btnRuleFilter.setOnAction((ActionEvent t) -> {
            RuleFilterDialog ruleFilterDialog = new RuleFilterDialog(RuleFilterApplicationUsage.BatchAdministration);
            ruleFilterDialog.showAndWait().ifPresent(new Consumer<ButtonType>() {
                @Override
                public void accept(ButtonType t) {
                    if (ButtonType.OK.equals(t)) {
                        List<CrgRules> listOfRules = ruleFilterDialog.getResults();
                        // TODO: check with selected rules 
                        btnStartStop.getOnAction().handle(new GroupWithRulesEvent(listOfRules));
                    }
                }
            });
        });

        btnStartStop.setOnStartEvent(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (!Session.instance().isBatchgroupingAllowed()) {
                    MainApp.showAuthorizationMessage(new CpxAuthorizationException(Session.instance().getRoleProperties(), Lang.getAuthorizationDialogMessage(Session.instance().getRoleProperties() == null ? "---" : Session.instance().getRoleProperties().getName(), "Batchgrouping") + "\n" + Lang.getAuthorizationDialogMessageContact()));

                } else {

                    setShowResultLayout(false);
//                if(job == null){
                    try {
                        if (!(event instanceof GroupWithRulesEvent)) {
                            job = new BatchJob();
                        } else {
                            job = new BatchJob(((GroupWithRulesEvent) event).getRules());
                        }
                        job.start();
                        event.consume();
//                        jobId = job.getJobId();
                    } catch (Exception ex) {
                        LOG.log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        btnStartStop.setOnStopEvent(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                showResultLayout.set(true);
                if (job != null /* && job.isRunning() */) {
                    job.stop();
                    event.consume();
                }
            }
        });

        boxContent.getChildren().clear();

        reload();

        datesBox.visibleProperty().bind(caseIds.emptyProperty());
        casesBox.visibleProperty().bind(caseIds.emptyProperty().not());
        lblCaseIdsCount.textProperty().bind(caseIds.sizeProperty().asString());
        fbProgress.prefHeightProperty().bind(riProgress.heightProperty().add(100));
    }

    @Override
    public void reload() {
        super.reload();
        TBatchResult oldResult = getTBatchResult();
        if (oldResult != null) {
            if(getShowResultLayout()){
                //show result even if value still holdes true - in that case change listener is not called results in 
                //non-update of ui
                
                showResultLayout();
            }else{
                setShowResultLayout(true);
            }
            updateCaseDetailsCount();
            setUpBatchResult(oldResult);
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    showResultLayout();
//                }
//            });
        }else{
            NotificationsFactory.instance().createInformationNotification().text("Für das Grouper-Model: " + getSelectedGrouper().name() + " konnten keine Grouping-Ergebnisse ermittelt werden.").show();
            addFormerResults(null);
            updateCaseDetailsCount();
            boxContent.getChildren().clear();
        }
    }

    /**
     * @return property to reflect if a task is running in this controller,
     * could be refactored
     */
    public ReadOnlyBooleanProperty isTaskRunningProperty() {
        return isTaskRunning;
    }

    private void setUpLanguage() {

//        String roleName = "-";
//        try {
//            roleName = Session.instance().getEjbConnector().connectLoginServiceBean().getActualRoleName();
//        } catch (NamingException ex) {
//            LOG.log(Level.SEVERE, null, ex);
//        }
        shOverallHeader.setTitle(Lang.getBatchprocessing());
        lblSettingsFilter.setText(Lang.getFilter());
        lblSettingsFunctions.setText(Lang.getFunctions());
        chkBatchGroupingDoForActualRoleOnly.setText(Lang.getMenuBatchGroupingDoForActualRoleOnly());
        chkBatchGroupingDoForActualRoleOnly.setTooltip(new Tooltip(Lang.getMenuBatchGroupingDoForActualRoleOnlyTooltip()));
        chkBatchGroupingDoRules.setText(Lang.getMenuBatchGroupingDoRules());
        chkBatchGroupingDoRules.setTooltip(new Tooltip(Lang.getMenuBatchGroupingDoRulesTooltip()));
        chkBatchGroupingDoRulesSimulate.setText(Lang.getMenuBatchGroupingDoRulesSimulate());
        chkBatchGroupingDoRulesSimulate.setTooltip(new Tooltip(Lang.getMenuBatchGroupingDoRulesSimulateTooltip()));
        chkBatchGroupingDoSimulate.setText(Lang.getMenuBatchGroupingDoSimulate());
        chkBatchGroupingDoSimulate.setTooltip(new Tooltip(Lang.getMenuBatchGroupingDoSimulateTooltip()));
//        chkBatchGroupingDoSimulate.setVisible(false);

        chkBatchGroupingDoHistoryCases.setText(Lang.getMenuBatchGroupingDoHistoryCases());
        chkBatchGroupingDoHistoryCases.setTooltip(new Tooltip(Lang.getMenuBatchGroupingDoHistoryCasesTooltip()));

//        chkBatchGroupingExtern.setText(Lang.getMenuBatchGroupingExtern());
//        chkBatchGroupingExtern.setTooltip(new Tooltip(Lang.getMenuBatchGroupingExternTooltip()));
        cbDetailsFilter.setTitle(Lang.getMenuBatchGroupingDetailsFilter());
        cbDetailsFilter.setConverter(new StringConverter<DetailsFilterEn>(){
            @Override
            public String toString(DetailsFilterEn t) {
                return t == null ? "" : t.getLangKey();
            }

            @Override
            public DetailsFilterEn fromString(String string) {
               return DetailsFilterEn.valueOf(string);
            }
        });

        cbDetailsFilter.setTooltip(new Tooltip(Lang.getMenuBatchGroupingDetailsFilterTooltip()));
        cbDetailsFilter.setItems(DetailsFilterEn.values());
        chkBatchGroupingGrouped.setText(Lang.getMenuBatchGroupingGrouped());
        chkBatchGroupingGrouped.setTooltip(new Tooltip(Lang.getMenuBatchGroupingGroupedTooltip()));
//        chkBatchGroupingMedAndRemedies.setText(Lang.getMenuBatchGroupingMedAndRemedies());
//        chkBatchGroupingMedAndRemedies.setTooltip(new Tooltip(Lang.getMenuBatchGroupingMedAndRemediesTooltip()));
        chkBatchGroupingSupplementaryFee.setText(Lang.getMenuBatchGroupingSupplementaryFee());
        chkBatchGroupingSupplementaryFee.setTooltip(new Tooltip(Lang.getMenuBatchGroupingSupplementaryFeeTooltip()));
        btnRuleFilter.setText(Lang.getRuleFilterButtonText());
        btnStartStop.setStartText(Lang.getMenuBatchGroupingStart());
        btnStartStop.setStopText(Lang.getCancel());

        shDrgResult.setTitle(Lang.getBatchprocessingResult());

        llBatchGroupingCmieff.setTitle(Lang.getDialogBatchGroupingCmieff());
        llBatchGroupingCmieff.setTooltip(new Tooltip(Lang.getDialogBatchGroupingCmieffTooltip()));
        llBatchGroupingCmieff.addStyleClassToTitle("cpx-detail-label");

        llBatchGroupingKorrogvd.setTitle(Lang.getDialogBatchGroupingKorrogvd());
        llBatchGroupingKorrogvd.setTooltip(new Tooltip(Lang.getDialogBatchGroupingKorrogvdTooltip()));
        llBatchGroupingKorrogvd.addStyleClassToTitle("cpx-detail-label");

        llBatchGroupingKorrugvd.setTitle(Lang.getDialogBatchGroupingKorrugvd());
        llBatchGroupingKorrugvd.setTooltip(new Tooltip(Lang.getDialogBatchGroupingKorrugvdTooltip()));
        llBatchGroupingKorrugvd.addStyleClassToTitle("cpx-detail-label");

        llBatchGroupingKorrtrans.setTitle(Lang.getDialogBatchGroupingKorrtrans());
        llBatchGroupingKorrtrans.setTooltip(new Tooltip(Lang.getDialogBatchGroupingKorrtransTooltip()));
        llBatchGroupingKorrtrans.addStyleClassToTitle("cpx-detail-label");

        llBatchGroupingCwdiffneg.setTitle(Lang.getDialogBatchGroupingCwdiffneg());
        llBatchGroupingCwdiffneg.setTooltip(new Tooltip(Lang.getDialogBatchGroupingCwdiffnegTooltip("-")));
        llBatchGroupingCwdiffneg.addStyleClassToTitle("cpx-detail-label");

        llBatchGroupingCwdiffpos.setTitle(Lang.getDialogBatchGroupingCwdiffpos());
        llBatchGroupingCwdiffpos.setTooltip(new Tooltip(Lang.getDialogBatchGroupingCwdiffposTooltip("-")));
        llBatchGroupingCwdiffpos.addStyleClassToTitle("cpx-detail-label");

        llBatchGroupingDaysextern.setTitle(Lang.getDialogBatchGroupingDaysextern());
        llBatchGroupingDaysextern.setTooltip(new Tooltip(Lang.getDialogBatchGroupingDaysexternTooltip()));
        llBatchGroupingDaysextern.addStyleClassToTitle("cpx-detail-label");

        llBatchGroupingErrordrgpepp.setTitle(Lang.getDialogBatchGroupingErrordrgpepp());
        llBatchGroupingErrordrgpepp.setTooltip(new Tooltip(Lang.getDialogBatchGroupingErrordrgpeppTooltip()));
        llBatchGroupingErrordrgpepp.addStyleClassToTitle("cpx-detail-label");

        llBatchGroupingGroupedcases.setTitle(Lang.getDialogBatchGroupingGroupedcases());
        llBatchGroupingGroupedcases.setTooltip(new Tooltip(Lang.getDialogBatchGroupingGroupedcasesTooltip()));
        llBatchGroupingGroupedcases.addStyleClassToTitle("cpx-detail-label");

        llBatchGroupingIntensivcare.setTitle(Lang.getDialogBatchGroupingIntensivcare());
        llBatchGroupingIntensivcare.setTooltip(new Tooltip(Lang.getDialogBatchGroupingIntensivcareTooltip()));
        llBatchGroupingIntensivcare.addStyleClassToTitle("cpx-detail-label");

        llBatchGroupingLos.setTitle(Lang.getDialogBatchGroupingLos());
        llBatchGroupingLos.setTooltip(new Tooltip(Lang.getDialogBatchGroupingLosTooltip()));
        llBatchGroupingLos.addStyleClassToTitle("cpx-detail-label");
        llBatchGroupingNdi.setTitle(Lang.getDialogBatchGroupingNdi());
        llBatchGroupingNdi.setTooltip(new Tooltip(Lang.getDialogBatchGroupingNdiTooltip()));
        llBatchGroupingNdi.addStyleClassToTitle("cpx-detail-label");

        llBatchGroupingNinerdiag.setTitle(Lang.getDialogBatchGroupingNinerdiag());
        llBatchGroupingNinerdiag.setTooltip(new Tooltip(Lang.getDialogBatchGroupingNinerdiagTooltip()));
        llBatchGroupingNinerdiag.addStyleClassToTitle("cpx-detail-label");

        llBatchGroupingNursingdays.setTitle(Lang.getDialogBatchGroupingNursingdays());
        llBatchGroupingNursingdays.setTooltip(new Tooltip(Lang.getDialogBatchGroupingNursingdaysTooltip()));
        llBatchGroupingNursingdays.addStyleClassToTitle("cpx-detail-label");

        llBatchGroupingPccl.setTitle(Lang.getDialogBatchGroupingPccl());
        llBatchGroupingPccl.setTooltip(new Tooltip(Lang.getDialogBatchGroupingPcclTooltip()));
        llBatchGroupingPccl.addStyleClassToTitle("cpx-detail-label");

        llBatchGroupingDayprecare.setTitle(Lang.getDialogBatchGroupingDayprecare());
        llBatchGroupingDayprecare.setTooltip(new Tooltip(Lang.getDialogBatchGroupingDayprecareTooltip()));
        llBatchGroupingDayprecare.addStyleClassToTitle("cpx-detail-label");

        llBatchGroupingTotalcases.setTitle(Lang.getDialogBatchGroupingTotalcases());
        llBatchGroupingTotalcases.setTooltip(new Tooltip(Lang.getDialogBatchGroupingTotalcasesTooltip()));
        llBatchGroupingTotalcases.addStyleClassToTitle("cpx-detail-label");

        llBatchGroupingUnkcmi.setTitle(Lang.getDialogBatchGroupingUnkcmi());
        llBatchGroupingUnkcmi.setTooltip(new Tooltip(Lang.getDialogBatchGroupingUnkcmiTooltip()));
        llBatchGroupingUnkcmi.addStyleClassToTitle("cpx-detail-label");

        shRuleResult.setTitle(Lang.getCheckresultResult());
        columnClues.setText(Lang.getDialogBatchGroupingRulesClues());
        columnError.setText(Lang.getDialogBatchGroupingRulesError());
        columnWarning.setText(Lang.getDialogBatchGroupingRulesWarning());
        columnTyp.setText(Lang.getDialogBatchGroupingRulesTyp());

        txtQueueSize.setTitle("Queue Size");
        txtBlockSize.setTitle("Block Size");
        txtThreadCount.setTitle("Thread Count");

        //Hide grid column with advanced batch grouping settings
        settingsGrid.getChildren().remove(advancedBox);
        settingsGrid.getColumnConstraints().get(2).setPrefWidth(0.0d);
    }

    private void updateCaseDetailsCount() {
        Platform.runLater(() -> {
            lblCaseDetailsCount.setText("----");
        });
        new Thread(() -> {
            int count = grouperBean.get().getCaseDetailsCount(getParameters(null));
            Platform.runLater(() -> {
                lblCaseDetailsCount.setText(MessageFormat.format(Lang.getMenuBatchInfoGrouped(), count));
            });
        }).start();
    }

    @Override
    public void refresh() {
        super.refresh();
        //on refresh set up language 
        //possibly needed for role change?
        setUpLanguage();
    }

    @Override
    public boolean close() {
        final boolean res = super.close();
        if (job != null && job.isRunning()) {
            LOG.log(Level.INFO, "forcing stop of batch grouping job");
            job.stop();
        }
        job = null;
        return res;
    }

    private void setUpSettingsListener() {

        chkBatchGroupingDoForActualRoleOnly.disableProperty().bind(btnStartStop.isStartModeProperty().not());
        chkBatchGroupingDoForActualRoleOnly.setSelected(CpxClientConfig.instance().getBatchSettingsDo4ActualRole());
        chkBatchGroupingDoForActualRoleOnly.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                CpxClientConfig.instance().setBatchSettingsDo4ActualRole(newValue);
                updateCaseDetailsCount();
            }
        });
        chkBatchGroupingDoRules.disableProperty().bind(btnStartStop.isStartModeProperty().not());
        chkBatchGroupingDoRules.setSelected(CpxClientConfig.instance().getBatchSettingsDoRules());
        chkBatchGroupingDoRules.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                CpxClientConfig.instance().setBatchSettingsDoRules(newValue);
                updateCaseDetailsCount();
            }
        });
        chkBatchGroupingDoRulesSimulate.disableProperty().bind(btnStartStop.isStartModeProperty().not());
        chkBatchGroupingDoRulesSimulate.setSelected(CpxClientConfig.instance().getBatchSettingsdoRulesSimulate());
        chkBatchGroupingDoRulesSimulate.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                CpxClientConfig.instance().setBatchSettingsDoRulesSimulate(newValue);
                updateCaseDetailsCount();
            }
        });
        chkBatchGroupingDoSimulate.disableProperty().bind(btnStartStop.isStartModeProperty().not());
        chkBatchGroupingDoSimulate.setSelected(CpxClientConfig.instance().getBatchSettingsDoSimulate());
        chkBatchGroupingDoSimulate.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                CpxClientConfig.instance().setBatchSettingsDoSimulate(newValue);
                updateCaseDetailsCount();
            }
        });

        cbDetailsFilter.disableProperty().bind(btnStartStop.isStartModeProperty().not());
        cbDetailsFilter.select(CpxClientConfig.instance().getBatchSettingsDetailsFilter());
        cbDetailsFilter.getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DetailsFilterEn>() {
            @Override
            public void changed(ObservableValue<? extends DetailsFilterEn> observable, DetailsFilterEn oldValue, DetailsFilterEn newValue) {
                if (newValue == null) {
                    cbDetailsFilter.select(DetailsFilterEn.ACTUAL_LOCAL);
                    return;
                }
                CpxClientConfig.instance().setBatchSettingsDetailsFilter(newValue);
                updateCaseDetailsCount();
            }
        });
        chkBatchGroupingGrouped.disableProperty().bind(btnStartStop.isStartModeProperty().not());
        chkBatchGroupingGrouped.setSelected(CpxClientConfig.instance().getBatchSettingsGrouped());
        chkBatchGroupingGrouped.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                CpxClientConfig.instance().setBatchSettingsGrouped(newValue);
                updateCaseDetailsCount();
            }
        });
//        chkBatchGroupingMedAndRemedies.setSelected(CpxClientConfig.instance().getBatchSettingsMedAndRemedies());
//        chkBatchGroupingMedAndRemedies.selectedProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                CpxClientConfig.instance().setBatchSettingsMedsAndRemedies(newValue);
//            }
//        });
        chkBatchGroupingSupplementaryFee.disableProperty().bind(btnStartStop.isStartModeProperty().not());
        chkBatchGroupingSupplementaryFee.setSelected(CpxClientConfig.instance().getBatchSettingsSupplementaryFee());
        chkBatchGroupingSupplementaryFee.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                CpxClientConfig.instance().setBatchSettingsSupplementaryFee(newValue);
                updateCaseDetailsCount();
            }
        });
        chkBatchGroupingDoHistoryCases.disableProperty().bind(btnStartStop.isStartModeProperty().not());
        chkBatchGroupingDoHistoryCases.setSelected(CpxClientConfig.instance().getBatchSettingsDoHistoryCases());
        chkBatchGroupingDoHistoryCases.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                CpxClientConfig.instance().setBatchSettingsDoHistoryCases(newValue);
                updateCaseDetailsCount();
            }
        });
        dtAdmissionDateFrom.disableProperty().bind(btnStartStop.isStartModeProperty().not());
        dtAdmissionDateFrom.setDate(CpxClientConfig.instance().getBatchSettingsAdmissionDateFrom());
        dtAdmissionDateFrom.getControl().valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                CpxClientConfig.instance().setBatchSettingsAdmissionDateFrom(Lang.localToDate(newValue));
                updateCaseDetailsCount();
            }
        });
        dtAdmissionDateUntil.disableProperty().bind(btnStartStop.isStartModeProperty().not());
        dtAdmissionDateUntil.setDate(CpxClientConfig.instance().getBatchSettingsAdmissionDateUntil());
        dtAdmissionDateUntil.getControl().valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                CpxClientConfig.instance().setBatchSettingsAdmissionDateUntil(Lang.localToDate(newValue));
                updateCaseDetailsCount();
            }
        });
        dtDischargeDateFrom.disableProperty().bind(btnStartStop.isStartModeProperty().not());
        dtDischargeDateFrom.setDate(CpxClientConfig.instance().getBatchSettingsDischargeDateFrom());
        dtDischargeDateFrom.getControl().valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                CpxClientConfig.instance().setBatchSettingsDischargeDateFrom(Lang.localToDate(newValue));
                updateCaseDetailsCount();
            }
        });
        dtDischargeDateUntil.disableProperty().bind(btnStartStop.isStartModeProperty().not());
        dtDischargeDateUntil.setDate(CpxClientConfig.instance().getBatchSettingsDischargeDateUntil());
        dtDischargeDateUntil.getControl().valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                CpxClientConfig.instance().setBatchSettingsDischargeDateUntil(Lang.localToDate(newValue));
                updateCaseDetailsCount();
            }
        });
        showResultLayoutProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (newValue) {
//                            boxContent.getChildren().clear();
                            hideLoadingLayout();
                            showResultLayout();
                        } else {
//                            boxContent.getChildren().clear();
                            hideResultLayout();
                            showLoadingLayout();
                        }
                    }
                });

            }
        });

        btnRuleFilter.disableProperty().bind(btnStartStop.isStartModeProperty().not());
        btnResetCaseIds.disableProperty().bind(btnStartStop.isStartModeProperty().not());
    }

    private void showResultLayout() {
        if (!boxContent.getChildren().contains(gpResultLayout)) {
            boxContent.getChildren().add(gpResultLayout);
//            fbProgress.prefHeightProperty().bind(gpResultLayout.heightProperty().subtract(60).divide(2));
        }
    }

    private void hideResultLayout() {
        boxContent.getChildren().remove(gpResultLayout);
//        fbProgress.prefHeightProperty().unbind();
    }

    private void showLoadingLayout() {
        riProgress.indeterminate();
        fbProgress.indeterminate();
        if (!boxContent.getChildren().contains(boxLoadingLayout)) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    boxContent.getChildren().add(boxLoadingLayout);
                }
            });
        }
    }

    private void hideLoadingLayout() {
        boxContent.getChildren().remove(boxLoadingLayout);
    }

    private void setUpBatchResult(TBatchResult pResult) {

//        TBatchResult result = resultFacade.findBatchResult(CpxClientConfig.instance().getSelectedGrouper(), !CpxClientConfig.instance().getBatchSettingsExtern());
//        TBatchResult result = null;
        //Logger.getLogger(getClass().getSimpleName()).severe("No BatchResult found!");
        if (pResult != null) {
            LOG.info("Found Batchresult CreationDate: " + Lang.toDateTime(pResult.getCreationDate()));
            TBatchResult2Role role = null;
            if (pResult.getBatchres2role().size() >= 1) {
                role = pResult.getB2RForRoleId(Session.instance().getCpxActualRoleId());
            }
            if (role != null && pResult.getBatchresCaseCount() > 0){
                //ueberarbetien nach annas vorschlag
//                if (pResult.getBatchresCaseCount() > 0) {
                     String roleName = Session.instance().getEjbConnector().connectAuthServiceBean().get().findRoleNameById(role.getRoleId());
                    llBatchGroupingCwdiffneg.setText(Lang.toDecimal(role.getB2rMinDcwNegSum()));
                    llBatchGroupingCwdiffpos.setText(Lang.toDecimal(role.getB2rMaxDcwPosSum()));
                    //set new updated tooltips
                    llBatchGroupingCwdiffneg.setTooltip(new Tooltip(Lang.getDialogBatchGroupingCwdiffnegTooltip(roleName)));
                    llBatchGroupingCwdiffpos.setTooltip(new Tooltip(Lang.getDialogBatchGroupingCwdiffposTooltip(roleName)));
                    tvDetectedRules.setItems(FXCollections.observableArrayList(role.getBatchCheckResult()));
//                }

 //                tvDetectedRules.getSortOrder().add(columnTyp);
               
            }else{
                String roleName = Session.instance().getEjbConnector().connectAuthServiceBean().get().findRoleNameById(Session.instance().getCpxActualRoleId());

                llBatchGroupingCwdiffneg.setText(Lang.toDecimal(0));
                llBatchGroupingCwdiffpos.setText(Lang.toDecimal(0));
                //set new updated tooltips
                llBatchGroupingCwdiffneg.setTooltip(new Tooltip(Lang.getDialogBatchGroupingCwdiffnegTooltip(roleName)));
                llBatchGroupingCwdiffpos.setTooltip(new Tooltip(Lang.getDialogBatchGroupingCwdiffposTooltip(roleName)));
                tvDetectedRules.setItems(FXCollections.observableArrayList(new ArrayList<TBatchCheckResult>()));

            }
             Platform.runLater(() -> tvDetectedRules.refresh());
//            lblResultDate.setText(pResult.getCreationDate() != null ? Lang.getBatchprocessingLastProcess(Lang.toDateTime(pResult.getCreationDate())) : "");
            llBatchGroupingCmieff.setText(Lang.toDecimal(pResult.getCWEff()));
            llBatchGroupingUnkcmi.setText(Lang.toDecimal(pResult.getCWUnk()));

            llBatchGroupingKorrogvd.setText(String.valueOf(pResult.getBatchresHtpCount()));
            llBatchGroupingKorrugvd.setText(String.valueOf(pResult.getBatchresLtpCount()));
            llBatchGroupingKorrtrans.setText(String.valueOf(pResult.getBatchresTransfCount()));
            llBatchGroupingErrordrgpepp.setText(String.valueOf(pResult.getBatchresErrDrgCount()));
            llBatchGroupingGroupedcases.setText(String.valueOf(pResult.getBatchresGroupedCount()));
            llBatchGroupingIntensivcare.setText(String.valueOf(pResult.getBatchresCaseIntensivCount()));
            llBatchGroupingLos.setText(Lang.toDecimal(pResult.getLos(), 4));
            llBatchGroupingNdi.setText(Lang.toDecimal(pResult.getNDI(), 4));
            llBatchGroupingNursingdays.setText(String.valueOf(pResult.getBatchresCareDaysSum()));
            llBatchGroupingPccl.setText(Lang.toDecimal(pResult.getPccl(), 4));
            llBatchGroupingDayprecare.setText(String.valueOf(pResult.getBatchresDayCareCount()));
            llBatchGroupingTotalcases.setText(String.valueOf(pResult.getBatchresCaseCount()));
            llBatchGroupingNinerdiag.setText(String.valueOf(pResult.getBatchresAux9Count()));
            llBatchGroupingDaysextern.setText(String.valueOf(pResult.getBatchresNalosSum()));
            txtQueueSize.setTitle("Queue Size:");
            txtBlockSize.setTitle("Block Size:");
            txtThreadCount.setTitle("Thread Count:");
            refreshParameterControls(pResult);

        } else {
            MainApp.showErrorMessageDialog(Lang.getGroupResultNoResult());
//            lblResultDate.setText(Lang.getGroupResultNoResult());            
            LOG.info("no BatchResult found");
        }

    }
    
    private void refreshParameterControls(TBatchResult pResult){
    if(pResult == null){
        return;
    }
    TBatchGroupParameter param = pResult.getBatchGroupParameter();
    if(param != null){

        dtAdmissionDateFrom.setDate(param.getAdmissionDateFrom());
        dtAdmissionDateUntil.setDate(param.getAdmissionDateUntil());
        dtDischargeDateFrom.setDate(param.getDischargeDateFrom());
        dtDischargeDateUntil.setDate(param.getDischargeDateUntil());
        chkBatchGroupingGrouped.setSelected(!param.isGrouped());
        cbDetailsFilter.select(param.getDetailsFilter());
        chkBatchGroupingDoRules.setSelected(param.isDoRules());
        chkBatchGroupingDoRulesSimulate.setSelected(param.isDoRulesSimulate());
        chkBatchGroupingSupplementaryFee.setSelected(param.isDoSupplementaryFees());
        chkBatchGroupingDoForActualRoleOnly.setSelected(param.isDo4actualRoleOnly());
        chkBatchGroupingDoSimulate.setSelected(param.isDoSimulate());
        chkBatchGroupingDoHistoryCases.setSelected(param.isDoHistoryCases());
    }
    updateCaseDetailsCount();
    addFormerResults(pResult);
    }
    
    private void addFormerResults(TBatchResult pResult){
//        String KEY_STYLE = "-fx-text-fill:-black05";
//        String VALUE_STYLE = "-fx-text-fill:-text-main";
        ObservableList<Node> nodes = formerResults.getChildren();
        formerResults.getChildren().removeAll(nodes);
        ObservableList<RowConstraints> rowConstraints =  formerResults.getRowConstraints();
        formerResults.getRowConstraints().removeAll(rowConstraints);

        if(pResult == null){
            Label lbGroupDate = new Label("Letzte Batchverarbeitung");
            lbGroupDate.setMinWidth(180);
            lbGroupDate.getStyleClass().add("cpx-detail-label" );
            Label lb = new Label("Für das Grouper-Model: " + getSelectedGrouper().name() + " konnten keine Grouping-Ergebnisse ermittelt werden.");
            lb.setWrapText(true);
            formerResults.addRow(0, lbGroupDate, lb);
            formerResults.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, 35, Priority.ALWAYS, VPos.TOP, true));
            resetParameterFields();
            return;
        }
        TBatchGroupParameter param = pResult.getBatchGroupParameter();
        if(param == null){
            Label lbGroupDate = new Label("Letzte Batchverarbeitung");
            lbGroupDate.setMinWidth(180);
            lbGroupDate.getStyleClass().add("cpx-detail-label" );
            Label lb = new Label("Für das Grouper-Model: " + getSelectedGrouper().name() + 
                    "; durchgeführt am " + Lang.toDateTime(pResult.getCreationDate()) +
                    ", konnten keine angewendeten Parameter ermittelt werden.");
            lb.setWrapText(true);
            formerResults.addRow(0, lbGroupDate, lb);
            resetParameterFields();
//            formerResults.getRowConstraints().add(new RowConstraints(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, 50, Priority.ALWAYS, VPos.TOP, true));
            return;
            
        }
        int rowCount = 0;
// last group date        
        Label lbGroupDate = new Label("Letzte Batchverarbeitung");
        lbGroupDate.setMinWidth(180);
        lbGroupDate.getStyleClass().add("cpx-detail-label" );
        Label vGroupDate = new Label(Lang.toDateTime(param.getCreationDate()));
        formerResults.addRow(rowCount++, lbGroupDate, vGroupDate);
        formerResults.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, 35, Priority.ALWAYS, VPos.TOP, true));

        Label detFilter = new Label(param.getDetailsFilter().getLangKey());
        detFilter.getStyleClass().add("cpx-detail-label");
        Label csCount = new Label("gegroupte Anzahl der Fallversionen: " + pResult.getBatchresCaseCount());
        formerResults.addRow(rowCount++, detFilter, csCount);
        formerResults.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, 35, Priority.ALWAYS, VPos.TOP, true));
        int emptyRowsCount = 0;
        if(param.isDoSupplementaryFees() || param.isDo4actualRoleOnly() || param.isDoSimulate() ||  !param.isGrouped()){
            if(param.isDoSupplementaryFees()){
                Label doSupplementaryFees = new Label(chkBatchGroupingSupplementaryFee.getText());
    //            doRules.getStyleClass().add("cpx-detail-label");
                formerResults.add(doSupplementaryFees, 1, rowCount++, 1, 1);
                formerResults.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, 35, Priority.ALWAYS, VPos.TOP, true));

            }else{
                emptyRowsCount++;
            }
            if(param.isDo4actualRoleOnly()){
            Label doBatchGroupingDoForActualRoleOnly = new Label(chkBatchGroupingDoForActualRoleOnly.getText());
    //            doRules.getStyleClass().add("cpx-detail-label");
                formerResults.add(doBatchGroupingDoForActualRoleOnly, 1, rowCount++, 1, 1);
                formerResults.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, 35, Priority.ALWAYS, VPos.TOP, true));
            }else{
                emptyRowsCount++;
            }
            if(param.isDoSimulate()){
            Label doBatchGroupingDoSimulate = new Label(chkBatchGroupingDoSimulate.getText());
    //            doRules.getStyleClass().add("cpx-detail-label");
                formerResults.add(doBatchGroupingDoSimulate, 1, rowCount++, 1, 1);
                formerResults.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, 35, Priority.ALWAYS, VPos.TOP, true));
            }else{
                emptyRowsCount++;
            }
            if( !param.isGrouped()){
            Label doBatchGroupingGrouped = new Label(chkBatchGroupingGrouped.getText());
    //            doRules.getStyleClass().add("cpx-detail-label");
                    formerResults.add(doBatchGroupingGrouped, 1, rowCount++, 1, 1);
                    formerResults.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, 35, Priority.ALWAYS, VPos.TOP, true));
            }else{
                emptyRowsCount++;
            }

        }  else{
            emptyRowsCount += 4;
        }

    if(param.isDoRules() || param.isDoRulesSimulate() || param.isDoHistoryCases()){
//        Label dummy = new Label();
//           formerResults.add(dummy, 0, rowCount++, 2, 1);
//           formerResults.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, 10, Priority.ALWAYS, VPos.TOP, true));
        Label dummy1 = new Label("Regelanwendung");
        dummy1.getStyleClass().add("cpx-detail-label");
////           formerResults.add(dummy1, 0, rowCount++, 2, 1);
////           formerResults.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, 35, Priority.NEVER, VPos.TOP, true));
        Label lbRulesCount = new Label((param.isDoUseAllRules()?"Alle Regel, ":"Regelauswahl, ") + "Anzahl: " + pResult.getRulesCount());
//        lbRulesCount.getStyleClass().add("cpx-detail-label");
//        Label vRulesCount = new Label();
        formerResults.addRow(rowCount++, dummy1, lbRulesCount);
        formerResults.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, 35, Priority.ALWAYS, VPos.TOP, true));
        if(param.isDoRules()){
            Label doRules = new Label(chkBatchGroupingDoRules.getText());
//            doRules.getStyleClass().add("cpx-detail-label");
            formerResults.add(doRules, 1, rowCount++, 1, 1);
            formerResults.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, 35, Priority.ALWAYS, VPos.TOP, true));

        }else{
                emptyRowsCount++;
            }
        if(param.isDoRulesSimulate()){
            Label doRSimul = new Label(chkBatchGroupingDoRulesSimulate.getText());
//            doRSimul.getStyleClass().add("cpx-detail-label");
            formerResults.add(doRSimul, 1, rowCount++, 1, 1);
            formerResults.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, 35, Priority.ALWAYS, VPos.TOP, true));

        }else{
                emptyRowsCount++;
            }
        if(param.isDoHistoryCases()){
            Label doHis = new Label(chkBatchGroupingDoHistoryCases.getText());
//            doRSimul.getStyleClass().add("cpx-detail-label");
            formerResults.add(doHis, 1, rowCount++, 1, 1);
            formerResults.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, 35, Priority.ALWAYS, VPos.TOP, true));

        }else{
                emptyRowsCount++;
            }
    }else{
        emptyRowsCount+= 4;
    }
    if(param.getAdmissionDateFrom() != null || param.getAdmissionDateUntil()!= null || param.getDischargeDateFrom() != null || param.getDischargeDateUntil()!= null ){
//        Label dummy = new Label();
//           formerResults.add(dummy, 0, rowCount++, 2, 1);
//           formerResults.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, 10, Priority.ALWAYS, VPos.TOP, true));
        Label dummy1 = new Label("Einschränkungen");
        dummy1.getStyleClass().add("cpx-detail-label");
           formerResults.add(dummy1, 0, rowCount++, 2, 1);
           formerResults.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, 35, Priority.ALWAYS, VPos.TOP, true));
        if(param.getAdmissionDateFrom() != null || param.getAdmissionDateUntil()!= null){
            Label left = new Label("Aufn.-Datum"); 
            left.getStyleClass().add("cpx-detail-label");
            String rightText = "";
            if(param.getAdmissionDateFrom() != null){
                rightText += "von " + Lang.toDate(param.getAdmissionDateFrom()) + " ";
            }
            if( param.getAdmissionDateUntil()!= null){
                rightText += "bis " + Lang.toDate(param.getAdmissionDateUntil());
            }
            Label right = new Label(rightText);
             formerResults.addRow(rowCount++, left, right);
             formerResults.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, 35, Priority.ALWAYS, VPos.TOP, true));
            
        }else{
            emptyRowsCount++;
        }
         
        if(param.getDischargeDateFrom() != null || param.getDischargeDateUntil()!= null){
            Label left = new Label("Entl.-Datum"); 
            left.getStyleClass().add("cpx-detail-label");
            String rightText = "";
            if(param.getDischargeDateFrom() != null){
                rightText += "von " + Lang.toDate(param.getDischargeDateFrom()) + " ";
            }
            if( param.getDischargeDateUntil()!= null){
                rightText += "bis " + Lang.toDate(param.getDischargeDateUntil());
            }
            Label right = new Label(rightText);
             formerResults.addRow(rowCount++, left, right);
             formerResults.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, 35, Priority.ALWAYS, VPos.TOP, true));
        }else{
        emptyRowsCount++;
        }
    }else{
            emptyRowsCount += 3;
    }
    for(int i = 0; i < emptyRowsCount; i++){
    Label dummy = new Label();
       formerResults.add(dummy, 0, rowCount++, 2, 1);
       formerResults.getRowConstraints().add(new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, 10, Priority.SOMETIMES, VPos.TOP, true));

    }
}

    private void setUpTableColumns() {
        columnTyp.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TBatchCheckResult, String>, ObservableValue<String>>() {
            private final StringProperty value = new SimpleStringProperty();

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TBatchCheckResult, String> param) {
                if (param.getValue() == null) {
                    return null;
                }
                value.set(param.getValue().getBcheckresRuleType());
                return value;
            }
        });
        columnError.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TBatchCheckResult, String>, ObservableValue<String>>() {
            private final StringProperty value = new SimpleStringProperty();

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TBatchCheckResult, String> param) {
                if (param.getValue() == null) {
                    return null;
                }
                value.set(String.valueOf(param.getValue().getBchechresErrCount()));
                return value;
            }
        });
        columnWarning.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TBatchCheckResult, String>, ObservableValue<String>>() {
            private final StringProperty value = new SimpleStringProperty();

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TBatchCheckResult, String> param) {
                if (param.getValue() == null) {
                    return null;
                }
                value.set(String.valueOf(param.getValue().getBchechresWarnCount()));
                return value;
            }
        });
        columnClues.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TBatchCheckResult, String>, ObservableValue<String>>() {
            private final StringProperty value = new SimpleStringProperty();

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TBatchCheckResult, String> param) {
                if (param.getValue() == null) {
                    return null;
                }
                value.set(String.valueOf(param.getValue().getBatchresAdviceCount()));
                return value;
            }
        });

    }
    private GDRGModel getSelectedGrouper(){
        return CpxClientConfig.instance().getSelectedGrouper();
    }
    private TBatchResult getTBatchResult() {
        return Session.instance().getEjbConnector().connectBatchResultBean().get().findBatchResultForRole(getSelectedGrouper(), null); //, !chkBatchGroupingExtern.isSelected());
    }

    @FXML
    private void resetCaseIds(ActionEvent event) {
        caseIds.clear();
        updateCaseDetailsCount();
    }

    private void resetParameterFields() {
        dtAdmissionDateFrom.setDate(null);
        dtAdmissionDateUntil.setDate(null);
        dtDischargeDateFrom.setDate(null);
        dtDischargeDateUntil.setDate(null);
        chkBatchGroupingGrouped.setSelected(false);
        cbDetailsFilter.select(DetailsFilterEn.ACTUAL_LOCAL);
        chkBatchGroupingDoRules.setSelected(tvDetectedRules.getItems() != null && !tvDetectedRules.getItems().isEmpty());
        chkBatchGroupingDoRulesSimulate.setSelected(tvDetectedRules.getItems() != null && !tvDetectedRules.getItems().isEmpty());
        chkBatchGroupingSupplementaryFee.setSelected(false);
        chkBatchGroupingDoForActualRoleOnly.setSelected(false);
        chkBatchGroupingDoSimulate.setSelected(false);
        chkBatchGroupingDoHistoryCases.setSelected(false);
        caseIds.clear();
        updateCaseDetailsCount();
        
    }

    private class GroupWithRulesEvent extends ActionEvent {

        private static final long serialVersionUID = 1L;

        private final List<CrgRules> rules;

        public GroupWithRulesEvent(List<CrgRules> pRules) {
            rules = pRules == null ? new ArrayList<>() : new ArrayList<>(pRules);
        }

        public List<CrgRules> getRules() {
            return new ArrayList<>(rules);
        }
    }

    private BatchGroupParameter getParameters(final List<Long> pRuleIds) {
        final BatchGroupParameter param = new BatchGroupParameter();
        final List<Long> ids = caseIds.isEmpty() ? null : new ArrayList<>(caseIds.get());
        final CpxClientConfig conf = CpxClientConfig.instance();
//        final String database = Session.instance().getCpxDatabase(); //conf.getLastSessionDatabase();
        final GDRGModel grouperModel = conf.getSelectedGrouper();
        final String queueSize = txtQueueSize.getText().trim().toUpperCase();
        final String blockSize = txtBlockSize.getText().trim().toUpperCase();
        final String threadCount = "1"; //txtThreadCount.getText().trim().toUpperCase();
        final boolean disableWriter = chkBatchGroupingDisableWriter.isSelected();

        param.setCaseIds(ids);
        param.setAdmissionDateFrom(dtAdmissionDateFrom.getDate());
        param.setAdmissionDateUntil(dtAdmissionDateUntil.getDate());
        param.setDischargeDateFrom(dtDischargeDateFrom.getDate());
        param.setDischargeDateUntil(dtDischargeDateUntil.getDate());
        param.setGrouped(!chkBatchGroupingGrouped.isSelected());
        param.setDetailsFilter(cbDetailsFilter.getSelectedItem());
        param.setDoRules(chkBatchGroupingDoRules.isSelected());
        param.setDoRulesSimulate(chkBatchGroupingDoRulesSimulate.isSelected());
        param.setDoSupplementaryFees(chkBatchGroupingSupplementaryFee.isSelected());
        param.setDo4actualRoleOnly(chkBatchGroupingDoForActualRoleOnly.isSelected());
        param.setDoSimulate(chkBatchGroupingDoSimulate.isSelected());
        param.setDoHistoryCases(chkBatchGroupingDoHistoryCases.isSelected());
        param.setDoMedAndRemedies(false);
        param.setModel(grouperModel);
//        param.setModelId(grouperModel.getGDRGVersion());
        param.setQueueSize(BatchGroupParameter.toQueueSize(queueSize));
        param.setBlockSize(BatchGroupParameter.toBlockSize(blockSize));
        param.setThreadCount(BatchGroupParameter.toThreadCount(threadCount));
        param.setDisableWriter(disableWriter);
        param.setRuleIds(pRuleIds);
        return param;
    }

    /**
     * private class to start / stop job on the server side, overwatch the job
     * status and react accordingly messy implementation due to old code ToDO:
     * refactor initial implementation to a more cleaner variant
     */
    private class BatchJob extends CpxTask<Void> {

        private Long jobId;
        private final Logger logger = Logger.getLogger(BatchJob.class.getName());
        private BatchTaskMessageHandler jmsHandler = null;
        private final ObjectProperty<BatchStatus> jobStatusProperty = new SimpleObjectProperty<>();
        private final ObjectProperty<BatchGroupingDTO> jobDtoProperty = new SimpleObjectProperty<>();
        //        /**
//         * get if the task is still running
//         * @return isRunningProperty
//         */
//        public BooleanProperty getIsRunningProperty(){
//            return isRunningProperty;
//        }
        private final ExecutorService executor = Executors.newFixedThreadPool(1);
        private List<Long> rulesFromFilter = null;

        BatchJob() throws JMSException {
            super();
            initTask();
            jobStatusProperty.addListener(new ChangeListener<BatchStatus>() {
                @Override
                public void changed(ObservableValue<? extends BatchStatus> observable, BatchStatus oldValue, BatchStatus newValue) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
//                            lblGroupingStatus.setText(getJobStatus(newValue));
                            lblGroupingStatus.setText(BatchJobHelper.getLocalizedStatus(newValue));
                        }
                    });

                    if (BatchStatus.STOPPED.equals(newValue)
                            || BatchStatus.ABANDONED.equals(newValue)) {
                        logger.info("task canceled for id " + jobId);
                        btnStartStop.setStartMode();
                        //grouperBean.get().stopBatchGrouping(jobId);
                        saveJobStatus(BatchStatus.STOPPED);
                        dispose();
                    }

                    if (BatchStatus.STARTED.equals(newValue)) {
                        btnStartStop.setStopMode();
                        btnStartStop.updateButtonText();
                        setShowResultLayout(false);
                        //grouperBean.get().stopBatchGrouping(jobId);
                        saveJobStatus(BatchStatus.STARTED);
                    }
                    if (BatchStatus.STOPPING.equals(newValue)) {
                        btnStartStop.setStopping();
                        //grouperBean.get().stopBatchGrouping(jobId);
                        saveJobStatus(BatchStatus.STOPPING);
                    }
                    if (BatchStatus.FAILED.equals(newValue)) {
                        Exception ex = jobDtoProperty.get() == null ? null : jobDtoProperty.get().getException();
                        String reason = jobDtoProperty.get() == null ? null : jobDtoProperty.get().getBatchReasonForFailure();
                        boolean isLockException = ex instanceof LockException;
                        if (isLockException) {
                            logger.log(Level.SEVERE, "Batchgrouping failed because I cannot obtain database lock");
                            logger.log(Level.FINER, "Database is already locked", ex);
                        } else {
                            logger.log(Level.SEVERE, "Batchgrouping failed", ex);
                        }
                        MainApp.showErrorMessageDialog(ex, "Beim Batchgroupen ist ein Fehler aufgetreten" + (reason == null || reason.trim().isEmpty() ? "" : ":\n\n" + reason));
                        //getException().printStackTrace();
                        saveJobStatus(BatchStatus.FAILED);
                        dispose();
                    }
                    if (BatchStatus.COMPLETED.equals(newValue)) {
                        TBatchResult results = null;
                        results = jobDtoProperty.get() == null ? null : jobDtoProperty.get().getBatchResult();
                        if (results != null) {
                            results.setCreationDate(new Date());
                            setUpBatchResult(results);
                            saveJobStatus(BatchStatus.COMPLETED);
                        } else {
                            if (jobStatusProperty.get() == BatchStatus.FAILED) {
                                MainApp.showErrorMessageDialog(Lang.getErrorOccured());
                                saveJobStatus(BatchStatus.FAILED);
                            }
                            if (jobStatusProperty.get() == BatchStatus.STOPPED) {
                                saveJobStatus(BatchStatus.STOPPED);
                            }
                        }
                        dispose();
                    }
                }
            });
            lblGroupingStatus.setText(BatchJobHelper.getLocalizedStatus(null));
        }

        BatchJob(List<CrgRules> rules) throws JMSException {
            this();
            rulesFromFilter = getRuleIds(rules);
        }

        /**
         * restart specific Job by its id
         *
         * @param executionId unique job id, that should be restarted, a once
         * completed Job can't be restartet
         * @throws Exception thrown when a completed job is attemped to restart
         */
        public void restart(Long executionId) throws Exception {
            jobId = executionId;
            try {
                BatchStatus currentStatus = grouperBean.get().getBatchStatus(jobId);
                if (BatchStatus.STOPPED.equals(currentStatus)
                        || BatchStatus.ABANDONED.equals(currentStatus)) {
                    call();
                }
            } catch (LockException | ParseException | ExecutionException | CpxAuthorizationException ex) {
                MainApp.showErrorMessageDialog(ex, "Error occured while grouping");
            } catch (InterruptedException ex) {
                logger.log(Level.SEVERE, null, ex);
                Thread.currentThread().interrupt();
            }
        }

        /**
         * restart last job
         *
         * @throws Exception if job cant be restartet
         */
        public void restart() throws Exception {
            if (jobId != null) {
                restart(jobId);
            }
        }

        /**
         * get the current Job id of a running Task
         *
         * @return current JobId, null if no task is currently running
         */
        public Long getJobId() {
            if (isRunning()) {
                return jobId;
            }
            return null;
        }

        @Override
        protected Void call() throws LockException, ParseException, InterruptedException, ExecutionException, CpxAuthorizationException {
//            CpxClientConfig conf = CpxClientConfig.instance();
//            //get target database from Properties
//
            String database = Session.instance().getCpxDatabase(); //conf.getLastSessionDatabase();
//
//            GDRGModel grouperModel = conf.getSelectedGrouper();
//
//            final String queueSize = txtQueueSize.getText().trim().toUpperCase();
//            final String blockSize = txtBlockSize.getText().trim().toUpperCase();
//            final String threadCount = "1"; //txtThreadCount.getText().trim().toUpperCase();
//            final boolean disableWriter = chkBatchGroupingDisableWriter.isSelected();

            //starts ja new Job if no Job id currently available, attempt to restart if one is there
            //2018-06-28 DNi: BEGIN > CHECK LOCKS ON DATABASE < BEGIN
            Platform.runLater(() -> {
                lblComment.setText(null);
                riProgress.setStatusText(null);
            });
            jobId = MainApp.execWithLockDialog((Object param) -> {
                try {
                    if (btnStartStop.isDisabled()) {
                        return null;
                    }
                    logger.log(Level.INFO, "Start batchgrouping now...");
                    btnStartStop.setStopMode();
                    btnStartStop.setStarting();
                    return grouperBean.get().prepareBatchgrouping(database);
                } catch (LockException | CpxAuthorizationException exc) {
                    btnStartStop.setStartMode();
                    btnStartStop.updateButtonText();
                    logger.log(Level.SEVERE, "Batchgrouping cannot start, some cases or whole database is already locked");
                    //MainApp.showErrorMessageDialog(exc);
                    throw exc;
                }
            }, (Object param) -> {
                //aborted
                dispose();
                return null;
            });
            if (jobId == null) {
                stop();
                cancel();
                return null;
            } else {

                grouperBean.get().startBatchGrouping(jobId, getParameters(rulesFromFilter));

            }
            return null;

        }

        /**
         * Stop current Task
         */
        @Override
        public boolean stop() {
            if (jobStatusProperty.get() == BatchStatus.STARTED) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        logger.info("call stop job id " + jobId);
                        btnStartStop.setStopping();
                        grouperBean.get().stopBatchGrouping(jobId);
                    }
                }).start();
            }
            return true;
        }

        @Override
        public void dispose() {
            super.dispose();
            if (jmsHandler != null) {
                jmsHandler.close();
            }
            btnStartStop.setStartMode();
            executor.shutdownNow();
            setShowResultLayout(true);
        }

        /**
         * Get current JobStatus
         *
         * @return JobStatus StringProperty
         */
        public ObjectProperty<BatchStatus> getJobStatusProperty() {
            return jobStatusProperty;
        }

        private void initTask() throws JMSException {
            jmsHandler = new BatchTaskMessageHandler();
            jmsHandler.setOnMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (message instanceof TextMessage) {
                                    TextMessage msg = (TextMessage) message;
                                    lblComment.setText(msg.getText());
                                }
                                if (message instanceof ObjectMessage) {
//                                    LOG.info("get Message´for client " + message.getIntProperty("ClientId"));
                                    ObjectMessage msg = (ObjectMessage) message;

                                    if (msg.getObject() instanceof BatchGroupingDTO) {
                                        BatchGroupingDTO dto = (BatchGroupingDTO) msg.getObject();
                                        //LOG.log(Level.INFO, String.valueOf(dto.getBatchStatus()));
                                        if (dto.getPhase() == 2) {
                                            riProgress.setStatusText(dto.getComment());
                                            lblComment.setText(null);
                                            fbProgress.setFlow(dto.getCaseDetailsPerSecond());
                                        } else {
                                            riProgress.setStatusText(null);
                                            lblComment.setText(dto.getComment());
                                            fbProgress.indeterminate();
                                        }
                                        riProgress.setProgress(dto.getPercentSubphase());
                                        //numberOfGroupedChunks = dto.getSubphase();
                                        //fbProgress.setVisible(dto.getCaseDetailsPerSecond() > 0.0D);
                                        if (jobDtoProperty.getValue() != null
                                                && BatchStatus.STARTED.equals(dto.getBatchStatus())
                                                && (BatchStatus.STOPPING.equals(jobDtoProperty.getValue().getBatchStatus()) || BatchStatus.STOPPED.equals(jobDtoProperty.getValue().getBatchStatus()))) {
                                            //do nothing
                                            //discard STARTED events after STOPPING signal was send from server
                                            logger.log(Level.FINEST, "message is discarded: " + dto.getBatchStatus());
                                        } else {
                                            jobDtoProperty.set(dto);
                                            jobStatusProperty.set(dto.getBatchStatus());
                                        }
                                    } else if (msg.getObject() instanceof String) {
                                        String comment = (String) msg.getObject();
                                        lblComment.setText(comment);
                                    }

                                }
                                if (message != null && !jmsHandler.isClosed()) {
                                    message.acknowledge();
                                }
                            } catch (JMSException | IllegalStateException ex) {
                                logger.log(Level.WARNING, null, ex);
                            }
                        }
                    });
                }
            });

        }

        public void saveJobStatus(BatchStatus batchStatus) {
            CpxClientConfig conf = CpxClientConfig.instance();
            conf.setLastBatchJobId(jobId);
            conf.setLastBatchJobStatus(batchStatus.name());
        }

        private List<Long> getRuleIds(List<CrgRules> rules) {
            if (rules == null || rules.isEmpty()) {
                return null;
            }
            List<Long> ruleIds = new ArrayList<>();
            for (CrgRules rule : rules) {
                ruleIds.add(rule.getId());
            }
            return ruleIds;
        }

    }

    public void setCaseIds(long[] pCaseIds) {
        caseIds.clear();
        List<Long> list = Arrays.stream(pCaseIds).boxed().collect(Collectors.toList());
        ObservableSet<Long> obs = FXCollections.observableSet();
        obs.addAll(list);
        caseIds.setValue(obs);
        updateCaseDetailsCount();
    }

}
