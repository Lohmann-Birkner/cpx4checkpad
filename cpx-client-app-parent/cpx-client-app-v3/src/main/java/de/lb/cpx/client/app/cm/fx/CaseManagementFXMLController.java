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
package de.lb.cpx.client.app.cm.fx;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.client.app.cm.fx.details.CmCaseBillsScene;
import de.lb.cpx.client.app.cm.fx.details.CmCaseDetailsScene;
import de.lb.cpx.client.app.cm.fx.details.CmCaseFeeScene;
import de.lb.cpx.client.app.cm.fx.details.CmCasePatientScene;
import de.lb.cpx.client.app.cm.fx.details.CmDepartmentsDetailsScene;
import de.lb.cpx.client.app.cm.fx.details.CmDocumentationScene;
import de.lb.cpx.client.app.cm.fx.details.CmIcdDetailsScene;
import de.lb.cpx.client.app.cm.fx.details.CmLaboratoryDataScene;
import de.lb.cpx.client.app.cm.fx.details.CmVersionManagementScene;
import de.lb.cpx.client.app.cm.fx.simulation.menu.CaseManagementMainMenu;
import de.lb.cpx.client.app.cm.fx.simulation.model.SimulationScreen;
import de.lb.cpx.client.app.cm.fx.simulation.model.VersionContent;
import de.lb.cpx.client.app.cm.fx.simulation.model.VersionManager;
import de.lb.cpx.client.app.cm.fx.simulation.tables.DrgTableView;
import de.lb.cpx.client.app.cm.fx.simulation.tables.RulesTableView;
import de.lb.cpx.client.app.job.fx.casemerging.CaseMergingDetailsScene;
import de.lb.cpx.client.app.job.fx.casemerging.tab.PatientCaseMergingOverviewTab;
import de.lb.cpx.client.app.menu.fx.event.DataActionEvent;
import de.lb.cpx.client.app.menu.model.ListType;
import de.lb.cpx.client.app.rulefilter.dialog.RuleFilterApplicationUsage;
import de.lb.cpx.client.app.rulefilter.dialog.RuleFilterDialog;
import de.lb.cpx.client.app.service.facade.CaseMergingFacade;
import de.lb.cpx.client.app.tabController.MergeTabController;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.CpxScreen;

import de.lb.cpx.client.core.model.fx.adapter.IWeakAdapter;
import de.lb.cpx.client.core.model.fx.adapter.WeakListAdapter;
import de.lb.cpx.client.core.model.fx.adapter.WeakMapAdapter;
import de.lb.cpx.client.core.model.fx.adapter.WeakPropertyAdapter;
import de.lb.cpx.client.core.model.fx.combobox.GrouperModelsCombobox;
import de.lb.cpx.client.core.model.task.TaskService;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import de.lb.cpx.client.core.util.ObjectConverter;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.client.core.util.VersionStringConverter;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.enums.CaseStatusEn;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.shared.dto.rules.CpxSimpleRuleDTO;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.BooleanSupplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.validation.constraints.NotNull;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 * FXML Controller class Controller class for the case management aka case
 * simulation
 *
 * @author wilde
 */

public class CaseManagementFXMLController extends MergeTabController <CaseManagementScene> {

//    public static final String REPORT_PATH = "PDF Reports\\" + Session.instance().getCpxUserName() + "\\";
    private static final Logger LOG = Logger.getLogger(CaseManagementFXMLController.class.getName());
    public static final String UPDATE_SUMMARY = "update.summary";
    //AWi-20170613-CPX-542:
    //openProperty to detect if something has to be opened
    private ObjectProperty<DataActionEvent<Long>> openEventProperty = new SimpleObjectProperty<>();
    private VersionManager versionManager;
    @FXML
    private TabPane tabContent;
    @FXML
    private VBox vBox4tabs;
//    private AnchorPane anchorPane4tabs;
            
//    @FXML
    private Tab tabIcd = new Tab();
//    @FXML
    private Tab tabCaseData = new Tab();
//    @FXML
    private TabPane tabPaneCaseContent = new TabPane();
    @FXML
    private DrgTableView<VersionContent> tvDrgResults;
//    @FXML
    private Tab tabCaseDetails = new Tab();
    @FXML
    private SplitPane spContent;
    @FXML
    private Label lblCaseNumber;
    @FXML
    private Label lblCseStatus;
    @FXML
    private ComboBox<CaseStatusEn> cbCseStatus;
    @FXML
    private Label lblPatientName;
    @FXML
    private Label lblCaseNumberValue;
    @FXML
    private Label lblPatientNameValue;
    @FXML
    private Label lblAdmissionDate;
    @FXML
    private Label lblDischargeDate;
    @FXML
    private Label lblAdmissionDateValue;
    @FXML
    private Label lblDischargeDateValue;
    @FXML
    private Label lblType;
    @FXML
    private Label lblLos;
    @FXML
    private Label lblTypeValue;
    @FXML
    private Label lblLosValue;
//    @FXML
    private Tab tabDepartmentData = new Tab();
//    @FXML
    private Tab tabCaseFee = new Tab();
    @FXML
    private Button btnCaseActions;
    @FXML
    private RulesTableView tvRules;
//    @FXML
    private Tab tabDocumentation = new Tab();
//    @FXML
    private Tab tabCaseBills = new Tab();
    @FXML
    private Button btnRuleFilter;
    @FXML
    private HBox hbHeader;
    @FXML
    private HBox hbRuleFilter;
    
//    private GrouperModelsCombobox cbGrouperModel;
//    @FXML
    private Tab tabLaboratoryData = new Tab();
//    @FXML
    private Tab tabPatientdata = new Tab();

    private PatientCaseMergingOverviewTab tabAllDrgCases;

    private PatientCaseMergingOverviewTab tabAllPeppCases;
    

    private WeakListAdapter listAdapter;
    private WeakMapAdapter mapAdapter;
    private WeakPropertyAdapter propAdapter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        cbGrouperModel = new GrouperModelsCombobox();
//        Callback <GDRGModel, Boolean> onUpdateParent = new Callback<GDRGModel, Boolean> () {
//            @Override
//            public Boolean call(GDRGModel p) {
//                reload4GrouperModel();
//                return true;
//            }
//        };
//        cbGrouperModel.setOnUpdateParent(onUpdateParent);
        initGrouperBox();

        hbRuleFilter.getChildren().add(cbGrouperModel);
        propAdapter = new WeakPropertyAdapter();
        
        listAdapter = new WeakListAdapter();
        mapAdapter = new WeakMapAdapter();
        tabIcd.setText(Lang.getCaseResolveICD() + "/" + Lang.getCaseResolveOPS());
        tabPatientdata.setText("Patientendaten");
        tabCaseData.setText(Lang.getCaseData());
        tabCaseDetails.setText(Lang.getVersioncontrollTitle());
        tabCaseFee.setText(Lang.getCasefee());
        tabDepartmentData.setText(Lang.getDepartments());
        tabDocumentation.setText(Lang.getDocumentation());
        tabCaseBills.setText(Lang.getSapBills());
        tabLaboratoryData.setText(Lang.getLabData());
        tabAllDrgCases = new PatientCaseMergingOverviewTab(CaseTypeEn.DRG, "Gegroupte Fälle des Patienten:", CaseMergingFacade.MERGELIST_TYPE.PATIENT);    
        tabAllPeppCases = new PatientCaseMergingOverviewTab(CaseTypeEn.PEPP, "Gegroupte Fälle des Patienten:", CaseMergingFacade.MERGELIST_TYPE.PATIENT);   
         addCaseTabs();

         
        tabAllDrgCases.setOnOpenMergeDetails(new Callback<CaseMergingDetailsScene, Void>() {
            @Override
            public Void call(CaseMergingDetailsScene param) { 
                openMergedDetails(param);
                return null;
            }
        });
        tabAllPeppCases.setOnOpenMergeDetails(new Callback<CaseMergingDetailsScene, Void>() {
            @Override
            public Void call(CaseMergingDetailsScene param) {
                openMergedDetails(param);

                return null;
            }
        });
//        tabPaneCaseContent.getTabs().add(tabAllDrgCases);
//        tabPaneCaseContent.getTabs().add(tabAllPeppCases);
//        
        
        tabPaneCaseContent.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                if (newValue.getContent() == null) {
                    loadContent(newValue);
                    return;
                }
                //refresh values, to reflect changes
                //used to reset the width of the combobox in the version 'menu' should resize regarding the currently selected tab
                //combobox in the case details tab has a different width than in icd-ops tab
                refreshContent(newValue);

            }

 
        });

        btnCaseActions.setTooltip(new Tooltip(Lang.getCaseSimulatonActions()));
        btnCaseActions.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ELLIPSIS_V));
        btnCaseActions.setOnAction((ActionEvent event) -> {
            CaseManagementMainMenu menu = new CaseManagementMainMenu(versionManager) {
                @Override
                public void openItem(Long pId, ListType pType) {
                    setOpenEvent(new DataActionEvent<>(pId, pType));
                }
            };
            menu.show(btnCaseActions, 0);
//            tvDrgResults.setShowMenu(!tvDrgResults.getShowMenu());
        });
        cbCseStatus.getItems().addAll(FXCollections.observableArrayList(CaseStatusEn.values()));
        cbCseStatus.setConverter(new StringConverter<CaseStatusEn>() {
            @Override
            public String toString(CaseStatusEn t) {
                if (t == null) {
                    return "";
                }
                return t.getTranslation().getValue();
            }

            @Override
            public CaseStatusEn fromString(String string) {
                return CaseStatusEn.valueOf(string);
            }
        });
        cbCseStatus.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CaseStatusEn>() {
            @Override
            public void changed(ObservableValue<? extends CaseStatusEn> observable, CaseStatusEn oldVal, CaseStatusEn newValue) {
                if(newValue != null && getCase() != null){
                    getCase().setCsStatusEn(newValue);
                    versionManager.getServiceFacade().saveCaseEntity();

                }
            }

        });
        propAdapter.addChangeListener(hbHeader.widthProperty(),new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                StackPane header = (StackPane) tbpOpenContent.lookup(".tab-header-area");
//                if (header != null) {
//                    //compute new Values translate whole header to the left and than set padding to the right
//                    //not really nice, but found no other way, should be changed to a more clear way
//                    //tbMenu is normaly not present in that controller .. can be solved maybe with an lookup on this controller parent?!
//                    header.setTranslateX(-1.0d * newValue.intValue());
//                    header.setPadding(new Insets(0, 0, 0, newValue.doubleValue() + hBoxMetaData.getWidth()));//tbMenu.getPrefWidth()));
//                }
                updateHeaderPosition();
            }
        });
    }
     public void reload4GrouperModel(){
         versionManager.refreshVersionContent4GrouperModel();
         closeMergeTabs(tabPaneCaseContent);
         refreshContent(tabIcd);
         if(versionManager.getServiceFacade().hasManyDrgCases()){
             tabAllDrgCases.setCurrentPatient(versionManager.getCurrentPatient());
            tabAllDrgCases.reload4GrouperModel();
         };
         if(versionManager.getServiceFacade().hasManyPeppCases()){
             tabAllPeppCases.setCurrentPatient(versionManager.getCurrentPatient());
            tabAllPeppCases.reload4GrouperModel();
         }
     }


     //refreshes tab after content was updated to show actual values
     private void refreshContent(Tab pTab) {
         if ((pTab.getUserData()) != null && pTab.getUserData() instanceof SimulationScreen) {
             SimulationScreen<?, ?> currentContent = (SimulationScreen<?, ?>) pTab.getUserData();
             currentContent.refresh();//reload();
         }
         // reload to reflect immediately added/deleted Icds and/or Opses in departmentDetails tab.
         if (pTab.getUserData() instanceof CmDepartmentsDetailsScene) {
             @SuppressWarnings("unchecked")
             CmDepartmentsDetailsScene<TCaseDetails> departmentsDetailsScene = (CmDepartmentsDetailsScene<TCaseDetails>) pTab.getUserData();
             departmentsDetailsScene.setItems(versionManager.getCurrentLocals().stream().sorted(new Comparator<TCaseDetails>() {
                 @Override
                 public int compare(TCaseDetails o1, TCaseDetails o2) {
                     if (o1 == null || o2 == null) {
                         return -1;
                     }
                     return Integer.compare(o2.getCsdVersion(), o1.getCsdVersion());
                 }
             }).collect(Collectors.toList()));
             if (departmentsDetailsScene.getSelected() == null) {
                 departmentsDetailsScene.select(versionManager.getCurrentLocal());
             }
//                    departmentsDetailsScene.refresh();
             departmentsDetailsScene.reload();
         }
         if (pTab.getUserData() instanceof CmDocumentationScene) {
             //refresh list of stored case versions? request new one? invalidate??
             CmDocumentationScene currentContent = (CmDocumentationScene) pTab.getUserData();
             currentContent.refresh();//reload();
         }
         if(pTab instanceof PatientCaseMergingOverviewTab){
             ((PatientCaseMergingOverviewTab) pTab).setCurrentPatient(versionManager.getCurrentPatient(), versionManager.getServiceFacade().getCurrentCase());
             ((PatientCaseMergingOverviewTab) pTab).reload();
         }
     }
     
    @Override
    public void afterInitialisingScene() {
        super.afterInitialisingScene();
        tvDrgResults.setCaseType(getScene().getDisplayedCase().getCsCaseTypeEn());
        tvDrgResults.setOnReload(new Callback<Void, List<VersionContent>>() {
            @Override
            public List<VersionContent> call(Void param) {
                if (versionManager == null) {
                    return new ArrayList<>();
                }
                return versionManager.getManagedVersions();
            }
        });
        propAdapter.addChangeListener(tvDrgResults.getSelectionModel().selectedItemProperty(),new ChangeListener<VersionContent>() {

            private final ChangeListener<TGroupingResults> grListener = new ChangeListener<TGroupingResults>() {
                @Override
                public void changed(ObservableValue<? extends TGroupingResults> observable, TGroupingResults oldValue, TGroupingResults newValue) {
                    updateRulesThreadSafe(tvDrgResults.getSelectionModel().getSelectedItem());
                }
            };
            private final ChangeListener<TCaseDetails> detailsListener = new ChangeListener<TCaseDetails>() {
                @Override
                public void changed(ObservableValue<? extends TCaseDetails> observable, TCaseDetails oldValue, TCaseDetails newValue) {
                    updateRulesThreadSafe(tvDrgResults.getSelectionModel().getSelectedItem());
                }
            };

            @Override
            public void changed(ObservableValue<? extends VersionContent> observable, VersionContent oldValue, VersionContent newValue) {
                if (oldValue != null) {
                    oldValue.groupingResultProperty().removeListener(grListener);
                    oldValue.contentProperty().removeListener(detailsListener);
                }
                if (newValue != null) {
                    newValue.groupingResultProperty().addListener(grListener);
                    newValue.contentProperty().addListener(detailsListener);
                }
                updateRulesThreadSafe(newValue);
            }

            private void updateRulesThreadSafe(VersionContent pContent) {
                //make sure rules are updated on applicationThread to avoid IllegalStateException
                //occured 20200617 in reprü system, hopefully fixed this issue
                if (Platform.isFxApplicationThread()) {
                    updateRules(pContent);
                } else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            updateRules(pContent);
                        }
                    });
                }
            }

            private void updateRules(VersionContent pContent) {
                tvRules.getItems().clear();
                tvRules.getSortOrder().clear();
                //if deselected clear items an show empty tableview
                if (pContent == null) {
                    return;
                }
                //add rules
                // TODO set enable/disable checkbox for selection rules
                tvRules.setRulesSelect(!pContent.getCaseDetails().getCsdIsLocalFl() 
                        && pContent.getCaseDetails().getCsdVersion() == 1
                        // only for roles with right = canSetRelevanceFlag
                && versionManager.getServiceFacade().canSelectRules());
                tvRules.getItems().addAll(pContent.getDetectedRules());

            }
        });
        tvRules.setCaseType(getScene().getDisplayedCase().getCsCaseTypeEn());
        tvRules.setRulesSelect(false);
        //TODO: tvRules.setCallback to save rule selection
        Callback<CpxSimpleRuleDTO, Boolean> onSelectRule = new Callback<CpxSimpleRuleDTO, Boolean>(){
            public Boolean call(CpxSimpleRuleDTO rule){
                versionManager.setOrUpdateRuleSelectFlag(rule);
            return true;
            }
        };
        tvRules.setSaveSelection4Rule(onSelectRule);

    }

//    public void openDocument(File myFile) {
//        if (myFile == null) {
//            LOG.log(Level.SEVERE, "myFile is null!");
//            return;
//        }
//        if (!myFile.exists() || !myFile.isFile()) {
//            LOG.log(Level.SEVERE, MessageFormat.format("PDF file does not exists: {0}", myFile.getAbsolutePath()));
//        }
//        HostServices hostServices = BasicMainApp.instance().getHostServices();
//        hostServices.showDocument(myFile.getAbsolutePath());
//        //if (Desktop.isDesktopSupported()) {
//        //    try {
//        //        //For cross platform use.
//        //        Desktop.getDesktop().open(myFile);
//        //    } catch (IOException ex) {
//        //        LOG.log(Level.SEVERE, "The PDF file is possibly corrupted", ex);
//        //        // no application registered for PDFs
//        //    }
//        //}
//    }
    public void setupSummary() {
        TCase cs = getCase();
        TCaseDetails csDetails = cs == null ? null : cs.getCurrentLocal(); //extern or local?
        TPatient patient = cs == null ? null : cs.getPatient(); //extern or local?
        String placeholder = "-";

        lblCaseNumber.setText(Lang.getCaseNumber());
        lblPatientName.setText(Lang.getPatientNameObj().getAbbreviation());
        lblAdmissionDate.setText(Lang.getAdmissionDateObj().getAbbreviation());
        lblDischargeDate.setText(Lang.getDischargeDateObj().getAbbreviation());
        lblType.setText(Lang.getCaseResolveKind());
        lblLos.setText(Lang.getLOS());

        lblCseStatus.setText(Lang.getCaseStatus());
        lblCaseNumberValue.setText(cs == null ? placeholder : cs.getCsCaseNumber());
        lblPatientNameValue.setText(patient == null ? placeholder : patient.getPatNumber());
        lblAdmissionDateValue.setText(csDetails == null ? placeholder : Lang.toDate(csDetails.getCsdAdmissionDate()) + " " + Lang.toTime(csDetails.getCsdAdmissionDate()));
        lblDischargeDateValue.setText(csDetails == null ? placeholder : Lang.toDate(csDetails.getCsdDischargeDate()) + " " + Lang.toTime(csDetails.getCsdDischargeDate()));
        lblTypeValue.setText(cs == null ? placeholder : String.valueOf(cs.getCsCaseTypeEn() == null ? placeholder : cs.getCsCaseTypeEn().getTranslation()));
        lblLosValue.setText(csDetails == null ? placeholder : getLosDisplay(csDetails));
         if(cs == null){
            cbCseStatus.getSelectionModel().clearSelection();
         }else{
            cbCseStatus.getSelectionModel().select(cs.getCsStatusEn());
         }

//         updateHeaderPosition(tabContent, hbRuleFilter.getWidth(), hbHeader.getWidth());
         if( getScene().getParentTabPane() == null){// in case it was set through WmMainFrameFxmlController
            getScene().setParentTabPane(tabContent);
         }
//         addCaseTabs();
    }
    
    public void refreshIcds(){
        CmIcdDetailsScene icdData = (CmIcdDetailsScene)tabIcd.getUserData();
        if(icdData != null){
            icdData.getController().reload();
        }
        
    }
    
    private void updateHeaderPosition(){
        boolean contains = vBox4tabs.getChildren().contains(tabPaneCaseContent);
        if(contains){
            vBox4tabs.getChildren().remove(tabPaneCaseContent);
        }
        updateHeaderPosition(tabContent, hbRuleFilter.getWidth(), hbHeader.getWidth());
        if(contains){
            vBox4tabs.getChildren().add(tabPaneCaseContent);
        }
    }
    
    private void addCaseTabs(){

        tabPaneCaseContent.getTabs().add(adjustTab(tabIcd));
        tabPaneCaseContent.getTabs().add(adjustTab(tabCaseData));
        tabPaneCaseContent.getTabs().add(adjustTab(tabPatientdata));
        tabPaneCaseContent.getTabs().add(adjustTab(tabCaseDetails));
        tabPaneCaseContent.getTabs().add(adjustTab(tabCaseFee));
        tabPaneCaseContent.getTabs().add(adjustTab(tabDepartmentData));
        tabPaneCaseContent.getTabs().add(adjustTab(tabDocumentation));
        tabPaneCaseContent.getTabs().add(adjustTab(tabCaseBills));
        tabPaneCaseContent.getTabs().add(adjustTab(tabLaboratoryData));
        tabPaneCaseContent.getTabs().add(adjustTab(tabAllDrgCases));
        tabPaneCaseContent.getTabs().add(adjustTab(tabAllPeppCases));
        tabPaneCaseContent.setMaxWidth(USE_COMPUTED_SIZE);
        tabPaneCaseContent.setMaxHeight(USE_COMPUTED_SIZE);
        tabPaneCaseContent.setPrefWidth(USE_COMPUTED_SIZE);
        tabPaneCaseContent.setPrefHeight(USE_COMPUTED_SIZE);
        
        vBox4tabs.getChildren().add(tabPaneCaseContent);

    }
    
    private Tab adjustTab(Tab pTab){
        pTab.setClosable(false);
        return pTab;

    }
    
    
    private String getMathSign(long pValue){
        return pValue >= 0 ? " + " : " - ";
    }
    
    private String getLosDisplay(TCaseDetails pVersionDetails) {
        long losAlt = Objects.requireNonNullElse(pVersionDetails.getCsdLosAlteration(), 0L);
        long los = Objects.requireNonNullElse(pVersionDetails.getCsdLos(), 0L);
        int losMdAlt =  Objects.requireNonNullElse(pVersionDetails.getCsdLosMdAlteration(), 0);
        return pVersionDetails.getCsdLos()
                + " ("
                + (los - losAlt - losMdAlt)
                + (getMathSign(losAlt)) + Math.abs(losAlt)
                 + (losMdAlt == 0?"":(getMathSign(losMdAlt)) + Math.abs(losMdAlt))
                + ")";
    }

    /**
     * sets up content after drawing
     *
     * @param pVersionManager manager instance to store and get actual values to
     * display
     * @param pExternVersionId external case version to show
     * @param pLocalVersionId internal case version to show
     */
    public void initializeContent(VersionManager pVersionManager,Long pExternVersionId, Long pLocalVersionId){
        versionManager = pVersionManager;
        listAdapter.addChangeListener(versionManager.getManagedVersions(),new UpdateMenuListener());
//        if(getScene().hasGroupingResult(pExternVersionId==null?versionManager.getCurrentExternId():pExternVersionId,
//                pLocalVersionId==null?versionManager.getCurrentLocalId():pLocalVersionId)){
            initContentSync(pExternVersionId,pLocalVersionId);
            LOG.info("init Content in SyncMode - groupingResults are found for model");
//        }else{
//            initContentAsync(pExternVersionId,pLocalVersionId);
//            LOG.info("init Content in AsyncMode - groupingResults or atleast one result was not found (group anew)");
//        }
//        versionManager.addVersionsToManagedVersions(pExternVersionId, pLocalVersionId);
// 
//        mapAdapter.addChangeListener(versionManager.getServiceFacade().getProperties(),new MapChangeListener<Object, Object>() {
//            @Override
//            public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
//                if (change.wasAdded() && UPDATE_SUMMARY.equals(change.getKey())) {
//                    setupSummary();
//                    versionManager.getServiceFacade().getProperties().remove(UPDATE_SUMMARY);
//                }
//            }
//        });
//
//        setRuleFilterButton();
//        setupSummary();
//        loadContent(tabIcd);
        
//        new TaskService<Void>("Für das Grouper-Modell wurden noch keine Grouping-Ergebnisse erstellt.\nDies kann einen Moment dauern..."){
//            @Override
//            public Void call() {
//                versionManager.addVersionsToManagedVersions(pExternVersionId, pLocalVersionId);
//
//                mapAdapter.addChangeListener(versionManager.getServiceFacade().getProperties(), new MapChangeListener<Object, Object>() {
//                    @Override
//                    public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
//                        if (change.wasAdded() && UPDATE_SUMMARY.equals(change.getKey())) {
//                            setupSummary();
//                            versionManager.getServiceFacade().getProperties().remove(UPDATE_SUMMARY);
//                        }
//                    }
//                });
//
//                waitUntilConditionIsMet(new BooleanSupplier() {
//                    @Override
//                    public boolean getAsBoolean() {
//                        return !isGrounpingRunning();
//                    }
//                }, 60);
//                return null;
//            }
//
//            @Override
//            public void afterTask(Worker.State pState) {
//                super.afterTask(pState); //To change body of generated methods, choose Tools | Templates.
//                setRuleFilterButton();
//                setupSummary();
//                loadContent(tabIcd);
//            }
//            
//        }.start();
    }
    private void waitUntilConditionIsMet(BooleanSupplier awaitedCondition, int timeoutInSec) {
        boolean done;
        long startTime = System.currentTimeMillis();
        do {
            done = awaitedCondition.getAsBoolean();
        } while (!done && System.currentTimeMillis() - startTime < timeoutInSec * 1000);
    }

    public boolean isGrounpingRunning() {
        boolean running = false;
        for(VersionContent version : versionManager.getManagedVersions()){
            if(version.isGrouperRunning()){
                running = true;
            }
        }
        return running;
    }
    public void initializeContent(VersionManager pVersionManager){
        initializeContent(pVersionManager, null, null);
    }
    /**
     * @return current displayed case
     */
    public TCase getCase() {
        return versionManager.getServiceFacade().getCurrentCase();
    }

    /**
     * @return alternative root object, needed for the display of the simulation
     * in the process overview
     */
    public Parent getRootWithoutHeader() {
        return spContent;
    }

    @Override
    public void refresh() {
        long start = System.currentTimeMillis();
        tvDrgResults.refresh();
        if (((SimulationScreen) tabPaneCaseContent.getSelectionModel().getSelectedItem().getUserData()) != null) {
            ((CpxScreen) tabPaneCaseContent.getSelectionModel().getSelectedItem().getUserData()).refresh();
        }
        LOG.log(Level.INFO, "overall refresh in {0} ms", System.currentTimeMillis() - start);
    }

    /**
     * @param pEvent event to set
     */
    private void setOpenEvent(DataActionEvent<Long> pEvent) {
        openEventProperty.set(pEvent);
    }

    /**
     * @return event property for binding
     */
    public ObjectProperty<DataActionEvent<Long>> getOpenEventProperty() {
        return openEventProperty;
    }

    /**
     * @return action event
     */
    public DataActionEvent<Long> getOpenEvent() {
        return openEventProperty.get();
    }

    /*
    * private methodes
     */
    //load content regarding the selected tab in the tab view
    //load content if it was not loaded before
    private void loadContent(Tab newValue) {
        if (newValue.equals(tabIcd)) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        long start = System.currentTimeMillis();
                        CmIcdDetailsScene currentContent = new CmIcdDetailsScene(versionManager);
                        tabIcd.setUserData(currentContent);
                        CpxFXMLLoader.setAnchorsInNode(currentContent.getRoot());
                        tabIcd.setContent(currentContent.getRoot());
                        LOG.log(Level.FINER, "load icd in {0} ms", System.currentTimeMillis() - start);
                    } catch (IOException ex) {
                        LOG.log(Level.SEVERE, null, ex);
                    }
                }
            });
        }
        if (newValue.equals(tabCaseData)) {
            try {
                CmCaseDetailsScene currentContent = new CmCaseDetailsScene(versionManager);

                tabCaseData.setUserData(currentContent);
                CpxFXMLLoader.setAnchorsInNode(currentContent.getRoot());
                tabCaseData.setContent(currentContent.getRoot());

            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        if (newValue.equals(tabCaseDetails)) {
            try {
                CmVersionManagementScene currentContent = new CmVersionManagementScene(versionManager);
                tabCaseDetails.setUserData(currentContent);
                CpxFXMLLoader.setAnchorsInNode(currentContent.getRoot());
                tabCaseDetails.setContent(currentContent.getRoot());
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        if (newValue.equals(tabPatientdata)) {
            try {
//               long start = System.currentTimeMillis();
                CmCasePatientScene currentContent = new CmCasePatientScene(versionManager);
                tabPatientdata.setUserData(currentContent);
                CpxFXMLLoader.setAnchorsInNode(currentContent.getRoot());
                tabPatientdata.setContent(currentContent.getRoot());
//               LOG.info("set content of tab "+ newValue.getText() + "in " + (System.currentTimeMillis()-start) + " ms");  

            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        if (newValue.equals(tabCaseFee)) {
            try {
//               long start = System.currentTimeMillis();
                CmCaseFeeScene currentContent = new CmCaseFeeScene(versionManager);
                tabCaseFee.setUserData(currentContent);
                CpxFXMLLoader.setAnchorsInNode(currentContent.getRoot());
                tabCaseFee.setContent(currentContent.getRoot());
//               LOG.info("set content of tab "+ newValue.getText() + "in " + (System.currentTimeMillis()-start) + " ms");  

            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        if (newValue.equals(tabDepartmentData)) {
            try {
                List<TCaseDetails> versions = versionManager.getCurrentLocals().stream().collect(Collectors.toList());
                versions.sort(new Comparator<TCaseDetails>() {
                    @Override
                    public int compare(TCaseDetails o1, TCaseDetails o2) {
                        if (o1 == null || o2 == null) {
                            return -1;
                        }
                        return Integer.valueOf(o2.getCsdVersion()).compareTo(o1.getCsdVersion());
                    }
                });
                CmDepartmentsDetailsScene<TCaseDetails> currentContent = new CmDepartmentsDetailsScene<>(versionManager.getCurrentLocal(), versions,
                        new ObjectConverter<TCaseDetails, TCaseDetails>() {
                    @Override
                    public TCaseDetails to(TCaseDetails pObject) {
                        return pObject;
                    }

                    @Override
                    public TCaseDetails from(TCaseDetails pObject) {
                        return pObject;
                    }
                });
                VersionStringConverter conv = new VersionStringConverter(VersionStringConverter.DisplayMode.ACTUAL);
                currentContent.setNameConverter(conv);
                currentContent.selectedItemProperty().addListener(new ChangeListener<TCaseDetails>() {

                    @Override
                    public void changed(ObservableValue<? extends TCaseDetails> observable, TCaseDetails oldValue, TCaseDetails newValue) {
                        currentContent.getComboBox().setTooltip(conv.getParentTooltip(newValue));
                    }
                });
                tabDepartmentData.setContent(currentContent.getRoot());
                CpxFXMLLoader.setAnchorsInNode(currentContent.getRoot());
                tabDepartmentData.setUserData(currentContent);
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        if (newValue.equals(tabDocumentation)) {
            try {
                CmDocumentationScene currentContent = new CmDocumentationScene(versionManager);
                tabDocumentation.setContent(currentContent.getRoot());
                CpxFXMLLoader.setAnchorsInNode(currentContent.getRoot());
                tabDocumentation.setUserData(currentContent);
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        // True, if SAP module (SAP bills) needs to be shown. check condition based on SAP license module or write a flag in cpx_server_config.xml
        // By default, this tab is deactivated.
        if (versionManager.getServiceFacade().getSapBillDisplayTab()) {
            if (newValue.equals(tabCaseBills)) {
                try {
//                    TCase currentCase = getCase();
                    CmCaseBillsScene currentContent = new CmCaseBillsScene(versionManager.getServiceFacade());
                    tabCaseBills.setUserData(currentContent);
                    CpxFXMLLoader.setAnchorsInNode(currentContent.getRoot());
                    tabCaseBills.setContent(currentContent.getRoot());
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
            }
        } else {
            tabPaneCaseContent.getTabs().remove(tabCaseBills);
        }

//        List<TLab> allLaboratoryData = versionManager.getServiceFacade().getAllLaboratoryData(getCase().id);
//        if (allLaboratoryData != null && !allLaboratoryData.isEmpty()) {
        if (versionManager.getServiceFacade().getLaboratoryDataDisplayTab()) {
            if (newValue.equals(tabLaboratoryData)) {
                try {
                    CmLaboratoryDataScene currentContent = new CmLaboratoryDataScene(versionManager.getServiceFacade());
                    tabLaboratoryData.setUserData(currentContent);
                    CpxFXMLLoader.setAnchorsInNode(currentContent.getRoot());
                    tabLaboratoryData.setContent(currentContent.getRoot());
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
            }
        } else {
            tabPaneCaseContent.getTabs().remove(tabLaboratoryData);
        }
        if(versionManager.getServiceFacade().hasManyDrgCases()){
            if( newValue.equals  (tabAllDrgCases) ){
                CpxFXMLLoader.setAnchorsInNode(tabAllDrgCases.getContent().getScene().getRoot());
//
////               tabAllDrgCases.setCurrentPatient(versionManager.getCurrentPatient(), versionManager.getServiceFacade().getCurrentCase());
////               tabAllDrgCases.reload();
            }
        }else{
            tabPaneCaseContent.getTabs().remove(tabAllDrgCases);
        }
    
       if(versionManager.getServiceFacade().hasManyPeppCases()){
//           if(newValue.equals(tabAllPeppCases)){
//                tabAllPeppCases.setCurrentPatient(versionManager.getCurrentPatient(), versionManager.getServiceFacade().getCurrentCase());
//                tabAllPeppCases.reload();
//           }
        }else{
            tabPaneCaseContent.getTabs().remove(tabAllPeppCases);
        }
    }
    private final Map<VersionContent, List<ChangeListener<? extends Object>>> listenerMap = new HashMap<>();

    private void setListeners(VersionContent versionContent) {
        List<ChangeListener<? extends Object>> listeners = new ArrayList<>();
//        versionContent.contentProperty().addListener(new ChangeListener<TCaseDetails>() {
//            @Override
//            public void changed(ObservableValue<? extends TCaseDetails> observable, TCaseDetails oldValue, TCaseDetails newValue) {
////                tvDrgResults.refreshOpenRules();
//                if (versionContent.getCaseDetails() != null && versionContent.getCaseDetails().getCsdIsActualFl() && (versionContent.getCaseDetails().getCsdIsLocalFl())) {
//                    setupSummary();
//                }
//                tvRules.refresh();
//            }
//        });
        VersionContentListener contentListener = new VersionContentListener(versionContent);
        versionContent.contentProperty().addListener(contentListener);
        GrouperResultListener grListener = new GrouperResultListener(versionContent);
        versionContent.groupingResultProperty().addListener(grListener);

        listeners.add(contentListener);
        listeners.add(grListener);
        listenerMap.put(versionContent, listeners);

        //AWi-20170807-CPX-528
        //rules moved to outside of the tableview, update if another version is selected
//        tvDrgResults.getSelectionModel().selectedItemProperty().addListener(updateRulesListener);
    }

    private void removeListeners(@NotNull VersionContent versionContent) {
        versionContent = Objects.requireNonNull(versionContent, "VersionContent can not be null");
        var listeners = listenerMap.get(versionContent);
        for (var listener : listeners) {
            if (listener instanceof GrouperResultListener) {
                versionContent.groupingResultProperty().removeListener((GrouperResultListener) listener);
            }
            if (listener instanceof VersionContentListener) {
                versionContent.contentProperty().removeListener((VersionContentListener) listener);
            }
        }
        listeners.clear();
        listenerMap.remove(versionContent);
//        tvDrgResults.getSelectionModel().selectedItemProperty().removeListener(updateRulesListener);
    }


    private void initContentAsync(Long pExternVersionId, Long pLocalVersionId) {
        new TaskService<Void>("Für das Grouper-Modell wurden noch keine Grouping-Ergebnisse erstellt!\n\nDies kann einen Moment dauern..."){
            @Override
            public Void call() {
                versionManager.addVersionsToManagedVersions(pExternVersionId, pLocalVersionId);

                mapAdapter.addChangeListener(versionManager.getServiceFacade().getProperties(), new MapChangeListener<Object, Object>() {
                    @Override
                    public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
                        if (change.wasAdded() && UPDATE_SUMMARY.equals(change.getKey())) {
                            setupSummary();
                            versionManager.getServiceFacade().getProperties().remove(UPDATE_SUMMARY);
                        }
                    }
                });

                waitUntilConditionIsMet(new BooleanSupplier() {
                    @Override
                    public boolean getAsBoolean() {
                        return !isGrounpingRunning();
                    }
                }, 60);
                return null;
            }

            @Override
            public void afterTask(Worker.State pState) {
                super.afterTask(pState); //To change body of generated methods, choose Tools | Templates.
                setRuleFilterButton();
                setupSummary();
//                loadContent(tabIcd);
            }
            
        }.start();
    }

    private void initContentSync(Long pExternVersionId, Long pLocalVersionId) {
        versionManager.addVersionsToManagedVersions(pExternVersionId, pLocalVersionId);

        mapAdapter.addChangeListener(versionManager.getServiceFacade().getProperties(), new MapChangeListener<Object, Object>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
                if (change.wasAdded() && UPDATE_SUMMARY.equals(change.getKey())) {
                    setupSummary();
                    versionManager.getServiceFacade().getProperties().remove(UPDATE_SUMMARY);
                }
            } 
        });

        setRuleFilterButton();
        setupSummary();
        loadContent(tabCaseData);
        loadContent(tabIcd);
    }

    public void checkGrouperModel() {
       if(cbGrouperModel != null){
           cbGrouperModel.getSelectionModel().select(CpxClientConfig.instance().getSelectedGrouper());
       }
    }

    private class GrouperResultListener implements ChangeListener<TGroupingResults> {

        private final VersionContent versionContent;

        public GrouperResultListener(VersionContent pContent) {
            versionContent = pContent;
        }

        @Override
        public void changed(ObservableValue<? extends TGroupingResults> ov, TGroupingResults t, TGroupingResults t1) {
            tvRules.refresh();
            //CPX-1500, maybe unnecessary
            if (versionContent.getCaseDetails().getCsdIsActualFl() && (versionContent.getCaseDetails().getCsdIsLocalFl())) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setupSummary();
                        refreshIcds();
                    }
                });
            }
//                tvDrgResults.refreshOpenRules();
        }

    }

    private class VersionContentListener implements ChangeListener<TCaseDetails> {

        private final VersionContent versionContent;

        public VersionContentListener(VersionContent pContent) {
            versionContent = pContent;
        }

        @Override
        public void changed(ObservableValue<? extends TCaseDetails> ov, TCaseDetails t, TCaseDetails t1) {
            if (versionContent.getCaseDetails() != null && versionContent.getCaseDetails().getCsdIsActualFl() && (versionContent.getCaseDetails().getCsdIsLocalFl())) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setupSummary();
                    }
                });
            }
            getScene().checkGrouperYear(versionContent.getCaseDetails());
            tvRules.refresh();
        }

    }
    private boolean disposeAdapter(IWeakAdapter<?,?> pAdapter){
        if(pAdapter == null){
            return false;
        }
        pAdapter.dispose();
        return true;
    }
    @Override
    public boolean close() {
        super.close();
        if(disposeAdapter(propAdapter)){
            propAdapter = null;
        }
        if(disposeAdapter(listAdapter)){
            listAdapter = null;
        }
        if(disposeAdapter(mapAdapter)){
            mapAdapter = null;
        }
//        if(versionManager != null){
//            versionManager.getManagedVersions().removeListener(updateListener);
//            updateListener = null;
//            versionManager.getServiceFacade().getProperties().removeListener(PROPERTIES_LISTENER);
//        }
//        tvDrgResults.getSelectionModel().selectedItemProperty().removeListener(updateRulesListener);
        tvDrgResults.getColumns().clear();
        for (Tab tab : tabPaneCaseContent.getTabs()) {
            if (tab.getUserData() == null) {
                continue;
            }
            if(tab.getUserData() instanceof CpxScreen){
                ((CpxScreen) tab.getUserData()).close();
            }
            if(tab.getUserData() instanceof CpxScene){
                ((CpxScene) tab.getUserData()).close();
            }
        }
        if(versionManager != null){
            versionManager.destroy();
        }
        listenerMap.clear();
        return true;
    }

//    private final ChangeListener<VersionContent> updateRulesListener = new ChangeListener<VersionContent>() {
//
//        private final ChangeListener<TGroupingResults> grListener = new ChangeListener<TGroupingResults>() {
//            @Override
//            public void changed(ObservableValue<? extends TGroupingResults> observable, TGroupingResults oldValue, TGroupingResults newValue) {
//                updateRulesThreadSafe(tvDrgResults.getSelectionModel().getSelectedItem());
//            }
//        };
//        private final ChangeListener<TCaseDetails> detailsListener = new ChangeListener<TCaseDetails>() {
//            @Override
//            public void changed(ObservableValue<? extends TCaseDetails> observable, TCaseDetails oldValue, TCaseDetails newValue) {
//                updateRulesThreadSafe(tvDrgResults.getSelectionModel().getSelectedItem());
//            }
//        };
//
//        @Override
//        public void changed(ObservableValue<? extends VersionContent> observable, VersionContent oldValue, VersionContent newValue) {
//            if (oldValue != null) {
//                oldValue.groupingResultProperty().removeListener(grListener);
//                oldValue.contentProperty().removeListener(detailsListener);
//            }
//            if (newValue != null) {
//                newValue.groupingResultProperty().addListener(grListener);
//                newValue.contentProperty().addListener(detailsListener);
//            }
//            updateRulesThreadSafe(newValue);
//        }
//
//        private void updateRulesThreadSafe(VersionContent pContent){
//            //make sure rules are updated on applicationThread to avoid IllegalStateException
//            //occured 20200617 in reprü system, hopefully fixed this issue
//            if(Platform.isFxApplicationThread()){
//                updateRules(pContent);
//            }else{
//                Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        updateRules(pContent);
//                    }
//                });
//            }
//        }
//        private void updateRules(VersionContent pContent) {
//            tvRules.getItems().clear();
//            tvRules.getSortOrder().clear();
//            //if deselected clear items an show empty tableview
//            if (pContent == null) {
//                return;
//            }
//            //add rules
//            tvRules.getItems().addAll(pContent.getDetectedRules());
//
//        }
//    };

    private void setRuleFilterButton() {
        if (versionManager.getServiceFacade().isRuleEditorClient()) {
            Glyph filterGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.LIST_ALT);
//            filterGlyph.setStyle("-fx-text-fill: white;");
            btnRuleFilter.setStyle("-fx-text-fill: white;");
            filterGlyph.getStyleClass().add("cpx-icon-button");
            btnRuleFilter.setGraphic(filterGlyph);
            btnRuleFilter.setText("Regelfilter");
//        btnRuleFilter.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ELLIPSIS_V));
            btnRuleFilter.setTooltip(new Tooltip("Regelfilter"));
            btnRuleFilter.setOnAction((ActionEvent event) -> {
                RuleFilterDialog ruleFilterDialog = new RuleFilterDialog(RuleFilterApplicationUsage.BatchAdministration);
                ruleFilterDialog.showAndWait().ifPresent((ButtonType t) -> {
                    if (ButtonType.OK.equals(t)) {
                        List<CrgRules> selectedRules = ruleFilterDialog.getResults();
                        List<Long> selectedRuleIds = getRuleIds(selectedRules);

                        // todo: set rules into grouperService and group case details   
                        VersionContent vers = null;
                        for (VersionContent version : versionManager.getManagedVersions()) {
                            if (version.getContent().getCsdIsActualFl() && version.getContent().getCsdIsLocalFl()) {
                                vers = version;
                                break;
                            }
                        }
                        if (vers != null) {
                            vers.performGroup(selectedRuleIds);
                        }

                    }
                });
            });
        } else {
            btnRuleFilter.setVisible(false);
//            hbRuleFilter.getChildren().remove(btnRuleFilter);
//            hbHeader.getChildren().remove(hbRuleFilter);

        }
    }

    private class UpdateMenuListener implements ListChangeListener<VersionContent> {

        @Override
        public void onChanged(ListChangeListener.Change<? extends VersionContent> c) {
            while (c.next()) {
                if (c.wasAdded()) {
                    //add newly created versions to the drg table view and add combobox to the version 'menu'
                    for (VersionContent version : c.getAddedSubList()) {
                        setListeners(version);
                        versionManager.markAsDisplayed(version.getContent());
                        if(c.getFrom()!= 0){ //ignore first update where size was zero
                            getScene().checkGrouperYear(version.getContent());
                        }
                    }
                    tvDrgResults.reload();
                }
                if (c.wasRemoved()) {
                    //remove version from the drg table view and remove combobox from the version 'menu'
                    for (VersionContent version : c.getRemoved()) {
                        removeListeners(version);
                        versionManager.unMarkAsDisplayed(version.getContent());
                        version.destroy();
                    }
                    tvDrgResults.reload();
                }
            }
        }
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
    
    private void openMergedDetails(CaseMergingDetailsScene mrgMergeScene) {
        super.openMergedDetails(mrgMergeScene, tabAllDrgCases, tabAllPeppCases, tabPaneCaseContent);
    }
    
    // temporary test code
    
    
//    /*
//    * private classes
//     */
//    private void openMergedDetails(CaseMergingDetailsScene mrgMergeScene) {
//        if (mrgMergeScene == null || getScene().getParentTabPane() == null) {
//            return;
//        }
//        try {
//            Tab oldTab = findTab4MergeIdent(mrgMergeScene.getMergeIdent(), getScene().getParentTabPane());
//            if(oldTab == null){
//                TwoLineTab tab = createNewMergingDetailsTab(mrgMergeScene);
//                getScene().getParentTabPane().getTabs().add(tab);
//                getScene().getParentTabPane().getSelectionModel().select(tab);
//            }else{
//                getScene().getParentTabPane().getSelectionModel().select(oldTab);
//            }
//        } catch (IllegalArgumentException ex) {
//            MainApp.showErrorMessageDialog(ex);
//        }
//    }
//    
//    private Tab findTab4MergeIdent(Integer mergeIdent, TabPane parentPane){
//        for(Tab tab: parentPane.getTabs()){
//            if(tab instanceof TwoLineTab && ((TwoLineTab)tab).getTabType().equals(TwoLineTab.TabType.CASEMERGING)){
//               if(((TwoLineTab)tab).getDescription() != null && ((TwoLineTab)tab).getDescription().equals(Lang.getCaseMergingIdObj().getAbbreviation() + ": " + String.valueOf(mergeIdent))){
//                   return tab;
//               }
//           }
//        }
//        return null;
//    }
//
//    private TwoLineTab createNewMergingDetailsTab(CaseMergingDetailsScene mergingScene) {
//        if (mergingScene == null) {
//            return null;
//        }
//
//        TwoLineTab tab = new TwoLineTab("", Lang.getCaseMerging(), Lang.getCaseMergingIdObj().getAbbreviation() + ": " + mergingScene.getMergeIdent()) {
//            @Override
//            public TwoLineTab.TabType getTabType() {
//                return TwoLineTab.TabType.CASEMERGING;
//            }
//        };
//        tab.setClosable(true);
//        tab.setContent(mergingScene.getRoot());
//        tab.setOnClosed(new EventHandler<Event>() {
//            @Override
//            public void handle(Event event) {
//                CaseTypeEn caseType = mergingScene.getMergedCase().getCsCaseTypeEn();
//                switch (caseType) {
//                    case DRG:
//                        tabPaneCaseContent.getSelectionModel().select(tabAllDrgCases);
//                        tabAllDrgCases.deselect();
//                        break;
//                    case PEPP:
//                        tabPaneCaseContent.getSelectionModel().select(tabAllPeppCases);
//                        tabAllPeppCases.deselect();
//                        break;
//                    default:
//                        LOG.log(Level.WARNING, "Unknown case type: " + caseType);
//                }
//            }
//        });
//        mergingScene.setOnClose(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                tab.close();
//                tab.getOnClosed().handle(new Event(EventType.ROOT));
//                tabPaneCaseContent.getTabs().remove(tab);
//                if (mergingScene.getMergedCase() == null) {
//                    return;
//                }
//                switch (mergingScene.getMergedCase().getCsCaseTypeEn()) {
//                    case DRG:
//                        tabAllDrgCases.reload();
//                        break;
//                    case PEPP:
//                        tabAllPeppCases.reload();
//                        break;
//                    default:
//                        LOG.warning("Case Type not known, reload nothing");
//                }
//            }
//        });
//
//        return tab;
//    }
//

}
