/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.job.fx.casemerging.tab;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.job.fx.casemerging.CaseMergingDetailsScene;
import de.lb.cpx.client.app.service.facade.CaseMergingFacade;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.alert.ConfirmDialog;
import de.lb.cpx.client.core.model.fx.button.StartStopButton;
import de.lb.cpx.client.core.model.fx.progress.FlowrateBar;
import de.lb.cpx.client.core.model.fx.progress.RingProgressIndicator;
import de.lb.cpx.client.core.model.fx.section.SectionHeader;
import de.lb.cpx.client.core.model.fx.tab.TwoLineTab;
import de.lb.cpx.client.core.util.BatchJobHelper;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.service.properties.CpxAuthorizationException;
import de.lb.cpx.shared.lang.Lang;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javax.jms.JMSException;
/**
 * Implements tab for merging overview
 *
 * @author wilde
 */
public class CaseMergingOverviewTab extends TwoLineTab {

    private static final Logger LOG = Logger.getLogger(CaseMergingOverviewTab.class.getName());

    protected VBox contentBox;
    protected final ReadOnlyBooleanWrapper showResultProperty = new ReadOnlyBooleanWrapper(false);
    protected final ReadOnlyBooleanWrapper taskRunning = new ReadOnlyBooleanWrapper(false);
    protected final CaseTypeEn grpresType;
    protected Node resultContent;
    protected Node loadingContent;

    private MergeJob mergeJob;

    private Label lblJobStatus;
    protected StartStopButton btnStartStop = new StartStopButton(Lang.getCaseMergingStartProcess(), Lang.getCaseMergingStopProcess());
    private FlowrateBar fbProgress;
    private RingProgressIndicator riProgress;
    
    protected final CaseMergingFacade facade;

    protected final CaseMergingMasterDetail mdMerge;
    //binding is false when tableview is empty or nothing is selected
    private final BooleanBinding btnBinding;
    private final Button btnMergeAndSave = new Button(Lang.getCaseMergingDoMergeAndSave());
    private final Button btnMerge = new Button(Lang.getCaseMergingDoMerge());
//    private final Map<Integer, MergeObject> mapMrgObj = new HashMap<>();
    private Callback<CaseMergingDetailsScene, Void> openMergeDetailsCallback;
    


    
//    private final EjbProxy<CpxP21ImportBeanRemote> mergeBean = Session.instance().getEjbConnector().connectCpxP21ImportBean();
    
       public CaseMergingOverviewTab(CaseTypeEn pGrpresType) throws CpxIllegalArgumentException {
           this(pGrpresType, Lang.getCaseMerging(), CaseMergingFacade.MERGELIST_TYPE.ALL);
       }
    /**
     * creates new instance, is TwoLineTab
     *
     * @param pGrpresType type of overview list drg,pepp,etc
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException invalid argument
     */
    public CaseMergingOverviewTab(CaseTypeEn pGrpresType, String pTitle, CaseMergingFacade.MERGELIST_TYPE pMergeListType) throws CpxIllegalArgumentException {
        super("", pTitle, pGrpresType.name());
        checkType(pGrpresType);
        facade = new CaseMergingFacade(pGrpresType, pMergeListType); 
        grpresType = pGrpresType;

        showResultProperty.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    try {
                        showResultContent();
                    } catch (CpxIllegalArgumentException ex) {
                        Logger.getLogger(CaseMergingOverviewTab.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    showLoadingContent();
                }
            }
        });
        //if taskrunning goes from true to false, reset view if it not already happend
        taskRunningProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (Objects.equals(newValue, Boolean.FALSE) && Objects.equals(showResultProperty.get(), Boolean.FALSE)) {
//                    showResultProperty.set(true);
                    btnStartStop.setStartMode();
                }
            }
        });
        mdMerge = new CaseMergingMasterDetail(pGrpresType, facade);
        mdMerge.setResizableWithParent(mdMerge.getMasterNode(), true);
        mdMerge.setDividerPosition(0, 0.75);
        
        btnBinding = Bindings.or(
                Bindings.or(
                //CPX-815 mdMerge.isEmptyProperty() replaced with this: Bindings.isEmpty(mdMerge.getItems())
                    Bindings.isEmpty(mdMerge.getItems()),
                    mdMerge.getTableView().getSelectionModel().selectedIndexProperty().isEqualTo(-1)),
                    mdMerge.getDisableSimulate()
            );

        setContent(createContent());
        setClosable(false);
    }



    @Override
    public void reload() {
        super.reload();
        mdMerge.reload();
    }

    @Override
    public TwoLineTab.TabType getTabType() {
        switch (grpresType) {
            case DRG:
                return TwoLineTab.TabType.JOBCASEMERGING_DRG;
            case PEPP:
                return TwoLineTab.TabType.JOBCASEMERGING_PEPP;
            default:
                LOG.log(Level.WARNING, "Unknown grouper result type for tab: " + grpresType);
        }
        return null;
    }

    /**
     * @return readonly property to indicate if task is running
     */
    public final ReadOnlyBooleanProperty taskRunningProperty() {
        return taskRunning;
    }

    public void deselect() {
        mdMerge.getTableView().getSelectionModel().clearSelection();
    }

    public void setOnOpenMergeDetails(Callback<CaseMergingDetailsScene, Void> pCallback) {
        if(mdMerge != null){
            mdMerge.setOnOpenMergeDetails(pCallback);
        }
        openMergeDetailsCallback = pCallback;
    }

    private Boolean checkType(CaseTypeEn pType) {
        switch (pType) {
            case DRG:
                return true;
            case PEPP:
                return true;
            default:
                return false;
        }
    }
    
    public CaseMergingFacade facade(){
        return facade;
    }
    
    public  Label lblJobStatus(){
        return lblJobStatus;
    }
    
    public   StartStopButton btnStartStop(){
        return btnStartStop;
    }
    
    public FlowrateBar fbProgress(){
        return fbProgress;
    }
    
    public RingProgressIndicator riProgress(){
        return riProgress;
    }

    private void doStartMergeJob(ActionEvent event){
        showResultProperty.set(false);
        try {
             mergeJob = new MergeJob(this, grpresType);
            mergeJob.start();
            event.consume();
        } catch (JMSException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

    }
    
    private Node createContent() {
        contentBox = new VBox();
        contentBox.setPadding(new Insets(10, 0, 0, 0));
        if (facade.mergeCasesExists()) {
            showResultProperty.set(true);
        }

        btnStartStop.setOnStartEvent(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!Session.instance().isFallzusammenführungAllowed()) {
                    MainApp.showAuthorizationMessage(new CpxAuthorizationException(Session.instance().getRoleProperties(), Lang.getAuthorizationDialogMessage(Session.instance().getRoleProperties() == null ? "---" : Session.instance().getRoleProperties().getName(), "Fallzusammenführung") + "\n" + Lang.getAuthorizationDialogMessageContact()));
                } else { 
                    GDRGModel currGm = CpxClientConfig.instance().getSelectedGrouper();
                    if(mdMerge.getPatient() == null){
                        if (!facade.checkDatabaseRequirements(currGm, grpresType)) {
                            ConfirmDialog checkGrouper = new ConfirmDialog(MainApp.getWindow(), Lang.getCaseMergingWarningGrouped(currGm.name()));
                            checkGrouper.showAndWait().ifPresent(new Consumer<ButtonType>() {
                                @Override
                                public void accept(ButtonType t) {
                                    if (!t.equals(ButtonType.YES)){
                                        return;
                                    }
                                    doStartMergeJob(event);
                                }
                            });
                        }else{
                            doStartMergeJob(event);
                        }
                    }else{
                        int count = facade.doCheckMerging4Patient(mdMerge.getPatient(), grpresType);
                        mdMerge.reload();
                    }
                    
                }
            }
        });
        
        btnStartStop.setOnStopEvent(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (mergeJob != null) {
                    mergeJob.stop();
                    event.consume();
                }
            }
        });
        
        btnMerge.disableProperty().bind(Bindings.or(Bindings.or(btnBinding, taskRunningProperty()), mdMerge.getDisableSimulate()));
        btnMerge.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {

                if (!Session.instance().isFallzusammenführungAllowed()) {
                    MainApp.showAuthorizationMessage(new CpxAuthorizationException(Session.instance().getRoleProperties(), Lang.getAuthorizationDialogMessage(Session.instance().getRoleProperties() == null ? "---" : Session.instance().getRoleProperties().getName(), "Fallzusammenführung") + "\n" + Lang.getAuthorizationDialogMessageContact()));
                } else {
                    if (mdMerge.getSelectedItem() == null) {
                        return;
                    }
                    if (mdMerge.getSelectedItem().getMrgMergeIdent() == 0) {

                        return;
                    }
                    if (openMergeDetailsCallback != null) {
                        try {
                            openMergeDetailsCallback.call(mdMerge.createMergedDetails());
                        } catch (CpxIllegalArgumentException ex) {
                            Logger.getLogger(CaseMergingOverviewTab.class.getName()).log(Level.SEVERE, "Was not able to create merged details", ex);
                        }
                    }
                }
            }
        });
        btnMergeAndSave.setDisable(true);
        btnMergeAndSave.disableProperty().bind(Bindings.or(btnBinding, taskRunningProperty()));
        btnMergeAndSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!Session.instance().isFallzusammenführungAllowed()) {
                    MainApp.showAuthorizationMessage(new CpxAuthorizationException(Session.instance().getRoleProperties(), Lang.getAuthorizationDialogMessage(Session.instance().getRoleProperties() == null ? "---" : Session.instance().getRoleProperties().getName(), "Fallzusammenführung") + "\n" + Lang.getAuthorizationDialogMessageContact()));
                } else {
                    Integer ident = mdMerge.getSelectedItem().getMrgMergeIdent();
                    mdMerge.performMergeAndSave(ident);
                    mdMerge.getTableView().getSelectionModel().clearSelection();
                    mdMerge.refreshTableView();
                }
            }
        });
        SectionHeader menuBar = new SectionHeader("");
        if(facade.isSaveCaseMergingAllowed()){
        menuBar.addMenuItems(btnStartStop, btnMerge, btnMergeAndSave);
        }else{
            menuBar.addMenuItems(btnStartStop, btnMerge);
        }
        contentBox.getChildren().add(0, menuBar);
        return contentBox;
    }

    private Node getResultContent() throws CpxIllegalArgumentException {
        if (resultContent == null) { 
            mdMerge.reload();
            mdMerge.setRowMouseListener(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() >= 2) {
                        if (mdMerge.getSelectedItem() == null) {
                            return;
                        }
                        if (openMergeDetailsCallback != null) {
                            try {
                                openMergeDetailsCallback.call(mdMerge.createMergedDetails());
                            } catch (CpxIllegalArgumentException ex) {
                                Logger.getLogger(CaseMergingOverviewTab.class.getName()).log(Level.SEVERE, "Was not able to create merged details", ex);
                            }
                        }
                    }
                }
            });
            VBox.setVgrow(mdMerge.getMasterPane(), Priority.ALWAYS);
            VBox.setVgrow(mdMerge, Priority.ALWAYS);
//                VBox box = new VBox(shMergeList,mdMerge);
            VBox box = new VBox(mdMerge);
            box.setSpacing(10.0);
            resultContent = box;
            VBox.setVgrow(resultContent, Priority.ALWAYS);
            
        }
        return resultContent;
    }

    private Node getLoadingContent() {
        if (loadingContent == null) {
            GridPane gp = new GridPane();
            gp.setPrefHeight(300);
            gp.getRowConstraints().add(new RowConstraints(GridPane.USE_COMPUTED_SIZE, GridPane.USE_COMPUTED_SIZE, Double.MAX_VALUE, Priority.ALWAYS, VPos.CENTER, true));
            gp.getColumnConstraints().add(new ColumnConstraints(10, GridPane.USE_COMPUTED_SIZE, GridPane.USE_COMPUTED_SIZE, Priority.SOMETIMES, HPos.CENTER, true));
            gp.getColumnConstraints().add(new ColumnConstraints(10, GridPane.USE_COMPUTED_SIZE, GridPane.USE_COMPUTED_SIZE, Priority.SOMETIMES, HPos.CENTER, true));
            for (ColumnConstraints col : gp.getColumnConstraints()) {
                col.setPercentWidth(50.0);
            }
            gp.setHgap(100);
            gp.setMaxHeight(400);
            gp.setMaxWidth(300);
            riProgress = new RingProgressIndicator();
            riProgress.setAlignment(Pos.CENTER_RIGHT);
            riProgress.setMaxWidth(Double.MAX_VALUE);
            fbProgress = new FlowrateBar();
            gp.addColumn(0, riProgress);
            gp.addColumn(1, fbProgress);
            gp.setAlignment(Pos.CENTER);
            lblJobStatus = new Label(BatchJobHelper.getMergeJobStatus(null));
            VBox wrapper1 = new VBox(gp, lblJobStatus);
            wrapper1.setSpacing(10.0);
            wrapper1.setAlignment(Pos.CENTER);
            VBox.setVgrow(wrapper1, Priority.ALWAYS);
            VBox wrapper = new VBox(wrapper1);
            wrapper.setAlignment(Pos.CENTER);
            VBox.setVgrow(wrapper, Priority.ALWAYS);
            loadingContent = wrapper;
            fbProgress.setMaxHeight(400);
            fbProgress.setMinHeight(400);
        }
        
        riProgress.setProgress(-1);
        fbProgress.setProgress(-1);
        return loadingContent;
    }

    protected void showLoadingContent() {
        if (resultContent != null && contentBox.getChildren().contains(resultContent)) {
            contentBox.getChildren().remove(resultContent);
        }
        contentBox.getChildren().add(getLoadingContent());
    }

    protected void showResultContent() throws CpxIllegalArgumentException {
        if (loadingContent != null && contentBox.getChildren().contains(loadingContent)) {
            contentBox.getChildren().remove(loadingContent);
        }
        contentBox.getChildren().add(getResultContent());
    }
    

    @Override
    public void close() {
        super.close();
        if (mergeJob != null && mergeJob.isRunning()) {
            LOG.log(Level.INFO, "forcing stop of case merging job");
            mergeJob.stop();
        }
        mergeJob = null;
        facade.clearCache();
    }
    
    public ReadOnlyBooleanWrapper showResultProperty(){
        return showResultProperty;
    }

    /*
     * 
     * CASE MERGING TABLE VIEW 
     * 
     */
//    private class CaseMergingMasterDetail extends TableViewMasterDetailPane<TCaseMergeMapping> {
//
//        //cell factory to handle overrun behavior
//        private Callback<TableColumn<TCaseMergeMapping, String>, TableCell<TCaseMergeMapping, String>> overrunCellFactory
//                = (TableColumn<TCaseMergeMapping, String> param) -> {
//                    return new TableCell<TCaseMergeMapping, String>() {
//                @Override
//                protected void updateItem(String item, boolean empty) {
//                    super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
//                    if (item == null || empty) {
//                        setGraphic(null);
//                        return;
//                    }
//                    Label label = new Label(item);
//                    setGraphic(label);
//                    OverrunHelper.addInfoTooltip(label);
//                }
//            };
//                };
//
//        /**
//         * construct new instance ResizePolicy is set to uncontrained
//         */
//        public CaseMergingMasterDetail(CaseTypeEn pType) {
//            super(new AsyncTableView<TCaseMergeMapping>() {
//                @Override
//                public Future<List<TCaseMergeMapping>> getFuture() {
//                    try {
//                        return new AsyncResult<>(facade.getObservableMergingCases());
//                    } catch (CpxIllegalArgumentException ex) {
//                        LOG.log(Level.SEVERE, "Can not load merge data, reason: {0}", ex);
//                    }
//                    return new AsyncResult<>(new ArrayList<>());
//                }
//            }, TableView.UNCONSTRAINED_RESIZE_POLICY);
//            getSelectedItemProperty().addListener(new ChangeListener<TCaseMergeMapping>() {
//                @Override
//                public void changed(ObservableValue<? extends TCaseMergeMapping> observable, TCaseMergeMapping oldValue, TCaseMergeMapping newValue) {
//                    try {
//                        setDetail(createDetailAsync(newValue));
//                    } catch (CpxIllegalArgumentException ex) {
//                        Logger.getLogger(CaseMergingOverviewTab.class.getName()).log(Level.SEVERE, "Was not able to create detail", ex);
//                    }
//                }
//            });
//
//            setPlaceholderText(Lang.getCaseMergingTablePlaceholder());
//            getTableView().getStyleClass().add("stay-selected-table-view");
//            setDividerPosition(0, 0.8);
//
//            switch (pType) {
//                case DRG:
//                    setDrgColumns();
//                    break;
//                case PEPP:
//                    setPeppColumns();
//                    break;
//                default:
//                    LOG.warning("can not set items in overview list! unknown grprestype " + pType.name());
//            }
//            MenuItem itMergeAndSave = new MenuItem(Lang.getCaseMergingDoMergeAndSave());
//            itMergeAndSave.setDisable(true);
//            itMergeAndSave.setOnAction(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent t) {
//                    if (getSelectedItem() == null) {
//                        return;
//                    }
//                    performMergeAndSave(getSelectedItem().getMrgMergeIdent());
//                    getTableView().getSelectionModel().clearSelection();
//                }
//            });
//
//            MenuItem itMerge = new MenuItem(Lang.getCaseMergingDoMerge());
//            itMerge.setOnAction(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent t) {
//                    if (getSelectedItem() == null) {
//                        return;
//                    }
//                    if (openMergeDetailsCallback != null) {
//                        try {
//                            openMergeDetailsCallback.call(createMergedDetails(getSelectedItem().getMrgMergeIdent()));
//                        } catch (CpxIllegalArgumentException ex) {
//                            Logger.getLogger(CaseMergingOverviewTab.class.getName()).log(Level.SEVERE, "Was not able to create merged details", ex);
//                        }
//                    }
//                }
//            });
//            getRowContextMenu().getItems().addAll(itMerge, itMergeAndSave);
//        }
//        private Parent createDetailAsync(TCaseMergeMapping pMapping ) throws CpxIllegalArgumentException{
//            AsyncPane<Parent> pane = new AsyncPane<>(true) {
//                @Override
//                public Parent loadContent() {
//                    return createDetail(pMapping);
//                }
//            };
//            return pane;
//        }
//        private Parent createDetail(TCaseMergeMapping pMapping) throws CpxIllegalArgumentException {
//            if (pMapping == null) {
//                return new HBox();
//            }
//            long startTotal = System.currentTimeMillis();
//            long start = System.currentTimeMillis();
//            List<TCaseMergeMapping> mappings = facade.getCaseMergeByMergeId(pMapping.getMrgMergeIdent());
//            TCase mrg = null;
//            TGroupingResults grpRes = null;
//            if (!mapMrgObj.containsKey(pMapping.getMrgMergeIdent())) {
//                try {
//                    mrg = facade.mergeById(pMapping.getMrgMergeIdent());
//                    if (mrg == null) {
//                        MainApp.showWarningMessageDialog("Fallzusammenführung ist an dieser Stelle nicht möglich.\nFühren Sie noch einmal 'Fälle ermitteln' durch!");
//                        return new HBox();
//                    }
//                    grpRes = facade.getGrouperResult(mrg);
//                } catch (IllegalArgumentException ex) {
//                    MainApp.showErrorMessageDialog(ex + " \n Ident:" + pMapping.getMrgMergeIdent());
//                }
//                mapMrgObj.put(pMapping.getMrgMergeIdent(), new MergeObject(mrg, grpRes));
//                LOG.info("time to merge and group " + (System.currentTimeMillis() - start) + " ms");
//            } else {
//                MergeObject mrgObj = mapMrgObj.get(pMapping.getMrgMergeIdent());
//                mrg = mrgObj.getMergedCase();
//                grpRes = mrgObj.getGrResults();
//            }
//            if (mrg == null) {
//                return new HBox();
//            }
//            if (grpRes == null) {
//                return new HBox();
//            }
//            MrgOvDetectedReasonsSection conLayout = new MrgOvDetectedReasonsSection(pMapping);
//            HBox.setHgrow(conLayout.getRoot(), Priority.ALWAYS);
//
//            MrgOvCaseDataSection grLayout = new MrgOvCaseDataSection(pMapping.getMrgMergeIdent(), mrg, grpRes, mappings, facade);
//            HBox.setHgrow(grLayout.getRoot(), Priority.ALWAYS);
//
//            HBox content = new HBox(conLayout.getRoot(), grLayout.getRoot());
//
//            content.setFillHeight(true);
//            content.setSpacing(10.0);
//            LOG.info("render details in " + (System.currentTimeMillis() - startTotal) + " ms");
//            return content;
//        }
//
//        public void reload() {
//            if (getTableView() instanceof AsyncTableView) {
//                ((AsyncTableView) getTableView()).reload();
//            }
//        }
//
//        private void setDrgColumns() {
//            ColorIndicatorColumn colColor = new ColorIndicatorColumn();
//            getColumns().add(colColor);
//            MergeIdColumn colMergeId = new MergeIdColumn();
//            getColumns().add(colMergeId);
//            CaseNumberColumn colCaseNumber = new CaseNumberColumn();
//            getColumns().add(colCaseNumber);
//            HospitalColumn colHosNumber = new HospitalColumn();
//            getColumns().add(colHosNumber);
//            InsuranceColumn colInsComp = new InsuranceColumn();
//            getColumns().add(colInsComp);
//            DrgColumn colDrgCode = new DrgColumn();
//            getColumns().add(colDrgCode);
//            AdmissionDateColumn colAdmDate = new AdmissionDateColumn();
//            getColumns().add(colAdmDate);
//            DischargeDateColumn colDisDate = new DischargeDateColumn();
//            getColumns().add(colDisDate);
//            AdmissionReasonColumn colAdmReason = new AdmissionReasonColumn();
//            getColumns().add(colAdmReason);
//            colAdmReason.prefWidthProperty().bind(
//                    widthProperty()
//                            .subtract(colAdmDate.widthProperty())
//                            .subtract(colCaseNumber.widthProperty())
//                            .subtract(colDisDate.widthProperty())
//                            .subtract(colDrgCode.widthProperty())
//                            .subtract(colHosNumber.widthProperty())
//                            .subtract(colInsComp.widthProperty())
//                            .subtract(colMergeId.widthProperty())
//                            .subtract(colColor.widthProperty())
//                            .subtract(5)
//            );
//        }
//
//        private void setPeppColumns() {
//            ColorIndicatorColumn colColor = new ColorIndicatorColumn();
//            getColumns().add(colColor);
//            MergeIdColumn colMergeId = new MergeIdColumn();
//            getColumns().add(colMergeId);
//            CaseNumberColumn colCaseNumber = new CaseNumberColumn();
//            getColumns().add(colCaseNumber);
//            HospitalColumn colHosNumber = new HospitalColumn();
//            getColumns().add(colHosNumber);
//            InsuranceColumn colInsComp = new InsuranceColumn();
//            getColumns().add(colInsComp);
//            PeppColumn colDrgCode = new PeppColumn();
//            getColumns().add(colDrgCode);
//            AdmissionDateColumn colAdmDate = new AdmissionDateColumn();
//            getColumns().add(colAdmDate);
//            DischargeDateColumn colDisDate = new DischargeDateColumn();
//            getColumns().add(colDisDate);
//            AdmissionReasonColumn colAdmReason = new AdmissionReasonColumn();
//            getColumns().add(colAdmReason);
//
//            colAdmReason.prefWidthProperty().bind(
//                    widthProperty()
//                            .subtract(colAdmDate.widthProperty())
//                            .subtract(colCaseNumber.widthProperty())
//                            .subtract(colDisDate.widthProperty())
//                            .subtract(colDrgCode.widthProperty())
//                            .subtract(colHosNumber.widthProperty())
//                            .subtract(colInsComp.widthProperty())
//                            .subtract(colColor.widthProperty())
//                            .subtract(colMergeId.widthProperty())
//                            .subtract(5)
//            );
//        }
//
//        /*
//        * Column definition
//         */
//        private class ColorIndicatorColumn extends TableColumn<TCaseMergeMapping, BRACKETTYPE> {
//
//            ColorIndicatorColumn() {
//                super("");
//                setSortable(false);
//                setResizable(false);
//                setMaxWidth(15.0);
//                setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TCaseMergeMapping, BRACKETTYPE>, ObservableValue<BRACKETTYPE>>() {
//                    @Override
//                    public ObservableValue<BRACKETTYPE> call(CellDataFeatures<TCaseMergeMapping, BRACKETTYPE> param) {
//                        if (param.getValue() == null) {
//                            return null;
//                        }
//
//                        ObjectProperty<BRACKETTYPE> typeProperty = new SimpleObjectProperty<>();
//                        if (getTableItems().indexOf(param.getValue()) == 0) {
//                            typeProperty.setValue(BRACKETTYPE.OPEN);
//                            return typeProperty;
//                        }
//                        if (getTableItems().indexOf(param.getValue()) == getTableItems().size() - 1) {
//                            typeProperty.setValue(BRACKETTYPE.CLOSE);
//                            return typeProperty;
//                        }
//                        int index = getTableItems().indexOf(param.getValue());
//                        TCaseMergeMapping prev = getTableItems().get(index - 1);
//                        TCaseMergeMapping next = getTableItems().get(index + 1);
//
//                        if (Objects.equals(prev.getMrgMergeIdent(), param.getValue().getMrgMergeIdent()) && Objects.equals(next.getMrgMergeIdent(), param.getValue().getMrgMergeIdent())) {
//                            typeProperty.setValue(BRACKETTYPE.CONTINUE);
//                            return typeProperty;
//                        }
//                        if (!Objects.equals(prev.getMrgMergeIdent(), param.getValue().getMrgMergeIdent()) && Objects.equals(next.getMrgMergeIdent(), param.getValue().getMrgMergeIdent())) {
//                            typeProperty.setValue(BRACKETTYPE.OPEN);
//                            return typeProperty;
//                        }
//                        if (Objects.equals(prev.getMrgMergeIdent(), param.getValue().getMrgMergeIdent()) && !Objects.equals(next.getMrgMergeIdent(), param.getValue().getMrgMergeIdent())) {
//                            typeProperty.setValue(BRACKETTYPE.CLOSE);
//                            return typeProperty;
//                        }
//                        return typeProperty;
//                    }
//                });
//                setCellFactory(new Callback<TableColumn<TCaseMergeMapping, BRACKETTYPE>, TableCell<TCaseMergeMapping, BRACKETTYPE>>() {
//                    @Override
//                    public TableCell<TCaseMergeMapping, BRACKETTYPE> call(TableColumn<TCaseMergeMapping, BRACKETTYPE> p) {
//                        TableCell<TCaseMergeMapping, BRACKETTYPE> cell = new TableCell<TCaseMergeMapping, BRACKETTYPE>() {
//                            @Override
//                            protected void updateItem(BRACKETTYPE t, boolean bln) {
//                                super.updateItem(t, bln); //To change body of generated methods, choose Tools | Templates.
//                                if (t == null || bln) {
//                                    setGraphic(null);
//                                    return;
//                                }
//                                switch (t) {
//                                    case OPEN:
//                                        setGraphic(new BracketOpen(4.5f, 8));
//                                        break;
//                                    case CONTINUE:
//                                        setGraphic(new BracketContinue(4.5f));
//                                        break;
//                                    case CLOSE:
//                                        setGraphic(new BracketClose(4.5f, 8));
//                                        break;
//                                    default:
//                                        setGraphic(null);
//                                }
//                            }
//                        };
//                        return cell;
//                    }
//                });
//            }
//
//        }
//
//        private class MergeIdColumn extends StringColumn<TCaseMergeMapping> {//TableColumn<TCaseMergeMapping, String> {
//
//            MergeIdColumn() {
//                super(Lang.getCaseMergingIdObj().getAbbreviation());
//                setSortable(false);
//                setMinWidth(50.0);
//                setCellFactory(overrunCellFactory);
//            }
//
//            @Override
//            public String extractValue(TCaseMergeMapping pValue) {
//                return String.valueOf(pValue.getMrgMergeIdent());
//            }
//
//        }
//
//        private class CaseNumberColumn extends StringColumn<TCaseMergeMapping> {//TableColumn<TCaseMergeMapping, String> {
//
//            CaseNumberColumn() {
//                super(Lang.getCaseNumber());
//                setSortable(false);
//                setMinWidth(50.0);
//                setCellFactory(overrunCellFactory);
//            }
//
//            @Override
//            public String extractValue(TCaseMergeMapping pValue) {
//                return pValue.getCaseByMergeMemberCaseId().getCsCaseNumber();
//            }
//
//        }
//
//        private class InsuranceColumn extends StringColumn<TCaseMergeMapping> {//TableColumn<TCaseMergeMapping, String> {
//
//            InsuranceColumn() {
//                super(Lang.getInsurance());
//                setSortable(false);
//                setMinWidth(100.0);
//                setCellFactory(overrunCellFactory);
//            }
//
//            @Override
//            public String extractValue(TCaseMergeMapping pValue) {
//                TPatient pat = facade.loadPatient(pValue.getCaseByMergeMemberCaseId().getPatient().getId());
//                return pat.getPatInsuranceActual() != null ? pat.getPatInsuranceActual().getInsInsuranceCompany() : "";
//            }
//
//        }
//
//        private class HospitalColumn extends StringColumn<TCaseMergeMapping> {//TableColumn<TCaseMergeMapping, String> {
//
//            HospitalColumn() {
//                super(Lang.getHospitalIdentifierObj().getAbbreviation());
//                setSortable(false);
//                setMinWidth(100.0);
//                setCellFactory(overrunCellFactory);
//            }
//
//            @Override
//            public String extractValue(TCaseMergeMapping pValue) {
//                return String.valueOf(pValue.getCaseByMergeMemberCaseId().getCsHospitalIdent());
//            }
//        }
//
//        private class AdmissionDateColumn extends StringColumn<TCaseMergeMapping> {//TableColumn<TCaseMergeMapping, String> {
//
//            AdmissionDateColumn() {
//                super(Lang.getAdmissionDate());
//                setSortable(false);
//                setMinWidth(100.0);
//                setCellFactory(overrunCellFactory);
//            }
//
//            @Override
//            public String extractValue(TCaseMergeMapping pValue) {
//                return Lang.toDate(pValue.getCaseByMergeMemberCaseId().getCurrentLocal().getCsdAdmissionDate());
//            }
//        }
//
//        private class AdmissionReasonColumn extends StringColumn<TCaseMergeMapping> {//TableColumn<TCaseMergeMapping, String> {
//
//            AdmissionReasonColumn() {
//                super(Lang.getAdmissionReason());
//                setSortable(false);
//                setMinWidth(55.0);
//                setCellFactory(overrunCellFactory);
//            }
//
//            @Override
//            public String extractValue(TCaseMergeMapping pValue) {
//                return String.valueOf(pValue.getCaseByMergeMemberCaseId().getCurrentLocal().getCsdAdmReason12En().getTranslation().getValue());
//            }
//        }
//
//        private class DrgColumn extends StringColumn<TCaseMergeMapping> {//TableColumn<TCaseMergeMapping, String> {
//
//            DrgColumn() {
//                super(Lang.getDRGObj().getAbbreviation());
//                setSortable(false);
//                setMinWidth(55.0);
//                setCellFactory(overrunCellFactory);
//            }
//
//            @Override
//            public String extractValue(TCaseMergeMapping pValue) {
//                return String.valueOf(pValue.getGrpresId().getGrpresCode());
//            }
//        }
//
//        private class PeppColumn extends StringColumn<TCaseMergeMapping> {//TableColumn<TCaseMergeMapping, String> {
//
//            PeppColumn() {
//                super(Lang.getPEPPObj().getAbbreviation());
//                setSortable(false);
//                setMinWidth(55.0);
//                setCellFactory(overrunCellFactory);
//            }
//
//            @Override
//            public String extractValue(TCaseMergeMapping pValue) {
//                return String.valueOf(pValue.getGrpresId().getGrpresCode());
//            }
//        }
//
//        private class DischargeDateColumn extends StringColumn<TCaseMergeMapping> {//TableColumn<TCaseMergeMapping, String> {
//
//            DischargeDateColumn() {
//                super(Lang.getDischargeDate());
//                setSortable(false);
//                setMinWidth(110.0);
//                setCellFactory(overrunCellFactory);
//            }
//
//            @Override
//            public String extractValue(TCaseMergeMapping pValue) {
//                return Lang.toDate(pValue.getCaseByMergeMemberCaseId().getCurrentLocal().getCsdDischargeDate());
//            }
//        }
//    }

    /*
     * 
     * MERGE JOB 
     * 
     */

    
//    private  class MergeJob extends CpxTask<Void> {
//
//        private Long jobId;
//        private final Logger logger = Logger.getLogger(getClass().getSimpleName());
//        private BatchTaskMessageHandler jmsHandler = null;
//        private final ObjectProperty<BatchStatus> jobStatusProperty = new SimpleObjectProperty<>();
//        private final ObjectProperty<BatchMergingDTO> jobDtoProperty = new SimpleObjectProperty<>();
//        //        /**
////         * get if the task is still running
////         * @return isRunningProperty
////         */
////        public BooleanProperty getIsRunningProperty(){
////            return isRunningProperty;
////        }
//        private final ExecutorService executor = Executors.newFixedThreadPool(1); 
//
//
//        MergeJob() throws JMSException {
//            super();
//            initTask();
//            jobStatusProperty.addListener(new ChangeListener<BatchStatus>() {
//                @Override
//                public void changed(ObservableValue<? extends BatchStatus> observable, BatchStatus oldValue, BatchStatus newValue) {
//                    Platform.runLater(new Runnable() {
//                        @Override
//                        public void run() {
////                            lblGroupingStatus.setText(getJobStatus(newValue));
//                            lblJobStatus.setText(BatchJobHelper.getMergeJobStatus(newValue)); 
//                        }
//                    });
//
//                    if (BatchStatus.STOPPED.equals(newValue)
//                            || BatchStatus.ABANDONED.equals(newValue)) {
//                        LOG.info("task canceled for id " + jobId);
//                        btnStartStop.setStartMode();
//                        //grouperBean.get().stopBatchGrouping(jobId);
//                        saveJobStatus(BatchStatus.STOPPED);
//                        dispose();
//                    }
//
//                    if (BatchStatus.STARTED.equals(newValue)) {
//                        btnStartStop.setStopMode();
//                        btnStartStop.updateButtonText();
//                        showResultProperty.set(false);
//                        //grouperBean.get().stopBatchGrouping(jobId);
//                        saveJobStatus(BatchStatus.STARTED);
//                    }
//                    if (BatchStatus.STOPPING.equals(newValue)) {
//                        btnStartStop.setStopping();
//                        //grouperBean.get().stopBatchGrouping(jobId);
//                        saveJobStatus(BatchStatus.STOPPING);
//                    }
//                    if (BatchStatus.FAILED.equals(newValue)) {
//                        Exception ex = jobDtoProperty.get() == null ? null : jobDtoProperty.get().getException();
//                        String reason = jobDtoProperty.get() == null ? null : jobDtoProperty.get().getBatchReasonForFailure();
//                        boolean isLockException = ex instanceof LockException;
//                        if (isLockException) {
//                            LOG.log(Level.SEVERE, "Batchmerging failed because I cannot obtain database lock");
//                            LOG.log(Level.FINER, "Database is already locked", ex);
//                        } else {
//                            LOG.log(Level.SEVERE, "Batchgrouping failed", ex);
//                        }
//                        de.lb.cpx.client.core.MainApp.showErrorMessageDialog(ex, "Beim ERmitteln der zusammenzuführenden Fällen ist ein Fehler aufgetreten" + (reason == null || reason.trim().isEmpty() ? "" : ":\n\n" + reason));
//                        //getException().printStackTrace();
//                        saveJobStatus(BatchStatus.FAILED);
//                        dispose();
//                    }
//                    if (BatchStatus.COMPLETED.equals(newValue)) {
//                        List<TCaseMergeMapping> results = null;
//                        results = jobDtoProperty.get() == null ? null : jobDtoProperty.get().getResultList();
//                        if (results != null) {
////                            setUpBatchResult(results);
//                            facade.reloadMergeCaseList();
//                            reload();
//                            saveJobStatus(BatchStatus.COMPLETED);
//                        } else {
//                            if (jobStatusProperty.get() == BatchStatus.FAILED) {
//                                de.lb.cpx.client.core.MainApp.showErrorMessageDialog(Lang.getErrorOccured()); 
//                                saveJobStatus(BatchStatus.FAILED);
//                            }
//                            if (jobStatusProperty.get() == BatchStatus.STOPPED) {
//                                saveJobStatus(BatchStatus.STOPPED);
//                            }
//                        }
//                        dispose();
//                    }
//                }
//            });
//            if(lblJobStatus != null){
//               lblJobStatus.setText(BatchJobHelper.getMergeJobStatus(null));
//            }
//        }
//    
//
//
//
//        /**
//         * restart specific Job by its id
//         *
//         * @param executionId unique job id, that should be restarted, a once
//         * completed Job can't be restartet
//         * @throws Exception thrown when a completed job is attemped to restart
//         */
//        public void restart(Long executionId) throws Exception {
//            jobId = executionId;
//            try {
//                BatchStatus currentStatus = mergeBean.get().getBatchMergeStatus(jobId);
//                if (BatchStatus.STOPPED.equals(currentStatus)
//                        || BatchStatus.ABANDONED.equals(currentStatus)) {
//                    call();
//                }
//            } catch (LockException | ParseException | ExecutionException | CpxAuthorizationException ex) {
//                de.lb.cpx.client.core.MainApp.showErrorMessageDialog(ex, "Error occured while grouping");
//            } catch (InterruptedException ex) {
//                LOG.log(Level.SEVERE, null, ex);
//                Thread.currentThread().interrupt();
//            }
//        }
//
//        /**
//         * restart last job
//         *
//         * @throws Exception if job cant be restartet
//         */
//        public void restart() throws Exception {
//            if (jobId != null) {
//                restart(jobId);
//            }
//        }
//
//        /**
//         * get the current Job id of a running Task
//         *
//         * @return current JobId, null if no task is currently running
//         */
//        public Long getJobId() {
//            if (isRunning()) {
//                return jobId;
//            }
//            return null;
//        }
//
//        @Override
//        protected Void call() throws LockException, ParseException, InterruptedException, ExecutionException, CpxAuthorizationException {
//            CpxClientConfig conf = CpxClientConfig.instance();
//            //get target database from Properties
//
//            String database = Session.instance().getCpxDatabase(); //conf.getLastSessionDatabase();
//
//            GDRGModel grouperModel = conf.getSelectedGrouper();
////
////            final String queueSize = txtQueueSize.getText().trim().toUpperCase();
////            final String blockSize = txtBlockSize.getText().trim().toUpperCase();
////            final String threadCount = "1"; //txtThreadCount.getText().trim().toUpperCase();
////            final boolean disableWriter = chkBatchGroupingDisableWriter.isSelected();
//
//            //starts ja new Job if no Job id currently available, attempt to restart if one is there
//            //2018-06-28 DNi: BEGIN > CHECK LOCKS ON DATABASE < BEGIN
//            Platform.runLater(() -> {
//                lblJobStatus.setText(null);
//                riProgress.setStatusText(null);
//            });
//            jobId = de.lb.cpx.client.core.MainApp.execWithLockDialog((Object param) -> {
//                try {
//                    if (btnStartStop.isDisabled()) {
//                        return null;
//                    }
//                    LOG.log(Level.INFO, "Start batchgmerging now...");
//                    btnStartStop.setStopMode();
//                    btnStartStop.setStarting();
//                    return mergeBean.get().prepareBatchMerging(database);
//                } catch (LockException | CpxAuthorizationException exc) {
//                    btnStartStop.setStartMode();
//                    btnStartStop.updateButtonText();
//                    LOG.log(Level.SEVERE, "Batchgmerging cannot start, some cases or whole database is already locked");
//                    //MainApp.showErrorMessageDialog(exc);
//                    throw exc;
//                }
//            }, (Object param) -> {
//                //aborted
//                dispose();
//                return null;
//            });
//            if (jobId == null) {
//                stop();
//                cancel();
//                return null;
//            } else {
//
//                mergeBean.get().startBatchMerging(jobId, grpresType, grouperModel);
//
//            }
//            return null;
//
//        }
//
//        /**
//         * Stop current Task
//         */
//        @Override
//        public boolean stop() {
//            if (jobStatusProperty.get() == BatchStatus.STARTED) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        LOG.info("call stop job id " + jobId);
//                        btnStartStop.setStopping();
//                        mergeBean.get().stopBatchMerging(jobId);
//                    }
//                }).start();
//            }
//            return true;
//        }
//
//        @Override
//        public void dispose() {
//            super.dispose();
//            if (jmsHandler != null) {
//                jmsHandler.close();
//            }
//            btnStartStop.setStartMode();
//            executor.shutdownNow();
//            showResultProperty.set(true);
//        }
//
//        /**
//         * Get current JobStatus
//         *
//         * @return JobStatus StringProperty
//         */
//        public ObjectProperty<BatchStatus> getJobStatusProperty() {
//            return jobStatusProperty;
//        }
//
//        private void initTask() throws JMSException {
//            jmsHandler = new BatchTaskMessageHandler();
//            jmsHandler.setOnMessageListener(new MessageListener() {
//                @Override
//                public void onMessage(Message message) {
//                    Platform.runLater(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                if (message instanceof TextMessage) {
//                                    TextMessage msg = (TextMessage) message;
//                                    lblJobStatus.setText(msg.getText());
//                                }
//                                if (message instanceof ObjectMessage) {
////                                    LOG.info("get Message´for client " + message.getIntProperty("ClientId"));
//                                    ObjectMessage msg = (ObjectMessage) message;
//
//                                    if (msg.getObject() instanceof BatchMergingDTO) {
//                                        BatchMergingDTO dto = (BatchMergingDTO) msg.getObject();
//                                        LOG.log(Level.INFO, String.valueOf(dto.getBatchStatus()));
//                                        if (dto.getPhase() == 2 ) {
//                                            riProgress.setStatusText(dto.getComment());
//                                            lblJobStatus.setText(null);
//                                            fbProgress.setFlow(dto.getCaseDetailsPerSecond());
//                                        } else {
//                                            riProgress.setStatusText(null);
//                                            lblJobStatus.setText(dto.getComment());
//                                            fbProgress.indeterminate();
//                                        }
//                                        riProgress.setProgress(dto.getPercentSubphase());
//                                         if (jobDtoProperty.getValue() != null
//                                                && BatchStatus.STARTED.equals(dto.getBatchStatus())
//                                                && (BatchStatus.STOPPING.equals(jobDtoProperty.getValue().getBatchStatus()) || BatchStatus.STOPPED.equals(jobDtoProperty.getValue().getBatchStatus()))) {
//                                            //do nothing
//                                            //discard STARTED events after STOPPING signal was send from server
//                                            LOG.log(Level.FINEST, "message is discarded: " + dto.getBatchStatus());
//                                        } else {
//                                            jobDtoProperty.set(dto);
//                                            jobStatusProperty.set(dto.getBatchStatus());
//                                        }
//                                    } else if (msg.getObject() instanceof String) {
//                                        String comment = (String) msg.getObject();
//                                        lblJobStatus.setText(comment);
//                                    }
//
//                                }
//                                if (message != null && !jmsHandler.isClosed()) {
//                                    message.acknowledge();
//                                }
//                            } catch (JMSException | IllegalStateException ex) {
//                                LOG.log(Level.WARNING, null, ex);
//                            }
//                        }
//                    });
//                }
//            });
//
//        }
//
//        public void saveJobStatus(BatchStatus batchStatus) {
//            CpxClientConfig conf = CpxClientConfig.instance();
//            conf.setLastBatchJobId(jobId);
//            conf.setLastBatchJobStatus(batchStatus.name());
//        }
//
//
//
//    }

//    private class BracketContinue extends HBox {
//
//        private final Path path = new Path();
//        private final VLineTo vline;
//
//        BracketContinue(double pStartX) {
//            path.setStroke(Color.BLACK);
//            setMinWidth(10);
//            setMaxWidth(10);
//            setFillHeight(true);
//            setAlignment(Pos.CENTER_LEFT);
//            MoveTo mt = new MoveTo(pStartX, 0);
//            path.getElements().add(mt);
//            vline = new VLineTo();
//            path.getElements().add(vline);
//
//            vline.yProperty().bind(heightProperty().subtract(2));
//            getChildren().add(path);
//        }
//    }
//
//    private class BracketOpen extends HBox {
//
//        private final Path path = new Path();
//
//        BracketOpen(double pStartX, double pMarginTop) {
//            path.setStroke(Color.BLACK);
//            setMinWidth(10);
//            setMaxWidth(10);
//            setFillHeight(true);
//            setAlignment(Pos.CENTER_LEFT);
//            MoveTo mt = new MoveTo(pStartX + 2, 0);
//            path.getElements().add(mt);
//            HLineTo topLine = new HLineTo(-2);
//            path.getElements().add(topLine);
//            VLineTo vline = new VLineTo();
//            path.getElements().add(vline);
//
//            vline.yProperty().bind(heightProperty().subtract(2 + pMarginTop));
//            HBox.setMargin(path, new Insets(pMarginTop, 0, 0, 0));
//
//            getChildren().add(path);
//        }
//    }
//
//    private class BracketClose extends HBox {
//
//        private final Path path = new Path();
//
//        BracketClose(double pStartX, double pMarginBottom) {
//            setMinWidth(10);
//            setMaxWidth(10);
//            setFillHeight(true);
//            setAlignment(Pos.CENTER_LEFT);
//            MoveTo mt = new MoveTo(pStartX, 0); // X-axis parameter starts from half of the square width
//            path.getElements().add(mt);
//            path.setStroke(Color.BLACK);
//            VLineTo vline = new VLineTo();
//            path.getElements().add(vline);
//
//            HLineTo bottomLine = new HLineTo(pStartX + 8);
//            bottomLine.setAbsolute(true);
//            path.getElements().add(bottomLine);
//            vline.yProperty().bind(heightProperty().subtract(2 + pMarginBottom));
//            HBox.setMargin(path, new Insets(0, 0, pMarginBottom, 0));
//            getChildren().add(path);
//        }
//    }
//
//    public enum BRACKETTYPE {
//        OPEN, CLOSE, CONTINUE;
//    }

    public void reload4GrouperModel() {
        facade.reloadMergeCaseList();
        reload();
    }

}
