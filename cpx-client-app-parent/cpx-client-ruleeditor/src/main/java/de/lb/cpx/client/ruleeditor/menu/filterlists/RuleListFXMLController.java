/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.ruleeditor.menu.filterlists;

import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.button.AddButton;
import de.lb.cpx.client.core.model.fx.button.CopyButton;
import de.lb.cpx.client.core.model.fx.button.DeleteButton;
import de.lb.cpx.client.core.model.fx.button.ExportButton;
import de.lb.cpx.client.core.model.fx.button.ImportButton;
import de.lb.cpx.client.core.model.fx.button.ReloadButton;
import de.lb.cpx.client.core.model.fx.button.SearchToggleButton;
import de.lb.cpx.client.core.model.fx.file_chooser.FileChooserFactory;
import de.lb.cpx.client.core.model.fx.masterdetail.MasterDetailBorderPane;
import de.lb.cpx.client.core.model.fx.masterdetail.TableViewMasterDetailPane;
import de.lb.cpx.client.core.model.fx.pane.AsyncPane;
import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.client.core.model.task.TaskService;
import de.lb.cpx.client.ruleeditor.events.OpenRuleEvent;
import de.lb.cpx.client.ruleeditor.events.UpdateRoleEvent;
import de.lb.cpx.client.ruleeditor.events.UpdateRuleEvent;
import de.lb.cpx.client.ruleeditor.events.UpdateRuleTypeEvent;
import de.lb.cpx.client.ruleeditor.events.UpdateRulesEvent;
import de.lb.cpx.client.ruleeditor.export.RuleListCsvExporter;
import de.lb.cpx.client.ruleeditor.export.RuleListExporter;
import de.lb.cpx.client.ruleeditor.export.RuleListXslxExporter;
import de.lb.cpx.client.ruleeditor.menu.dialogs.UnsavedContentDialog;
import de.lb.cpx.client.ruleeditor.menu.dialogs.buttontypes.RuleEditorButtonTypes;
import de.lb.cpx.client.ruleeditor.menu.filterlists.model.CdbUserRolesItem;
import de.lb.cpx.client.ruleeditor.menu.filterlists.model.CrgRuleTablesItem;
import de.lb.cpx.client.ruleeditor.menu.filterlists.tableview.RuleListTableView;
import de.lb.cpx.client.ruleeditor.menu.filterlists.tabs.RulePreview;
import de.lb.cpx.client.ruleeditor.menu.filterlists.tabs.RuleTablesDetail;
import de.lb.cpx.client.ruleeditor.menu.filterlists.tabs.UserRolesDetail;
import de.lb.cpx.client.ruleeditor.model.dnd.DndRulesToPool;
import de.lb.cpx.client.ruleeditor.task.CopyToTask;
import de.lb.cpx.client.ruleeditor.task.ExportPoolTask;
import de.lb.cpx.client.ruleeditor.task.ExportRulesAsCsvTask;
import de.lb.cpx.client.ruleeditor.task.ExportRulesAsXslxTask;
import de.lb.cpx.client.ruleeditor.task.ExportRulesTask;
import de.lb.cpx.client.ruleeditor.task.ImportRulesTask;
import de.lb.cpx.ruleviewer.cache.RuleMetaDataCache;
import de.lb.cpx.ruleviewer.enums.ViewMode;
import de.lb.cpx.ruleviewer.event.RefreshEvent;
import de.lb.cpx.ruleviewer.event.RuleTableChangedEvent;
import de.lb.cpx.ruleviewer.model.RuleMessageIndicator;
import de.lb.cpx.ruleviewer.util.PoolTypeHelper;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePoolsProd;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.server.commons.enums.RuleImportCheckFlags;
import de.lb.cpx.server.commons.enums.RuleOverrideFlags;
import de.lb.cpx.server.rule.services.RuleEditorProcessException;
import de.lb.cpx.server.rule.services.RuleExchangeError;
import de.lb.cpx.server.rule.services.RuleExchangeResult;
import de.lb.cpx.shared.json.RuleMessageReader;
import de.lb.cpx.shared.json.RuleTableMessageReader;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Worker;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javax.validation.constraints.NotNull;

/**
 * FXML Controller class
 *
 * @author wilde
 */
public class RuleListFXMLController extends Controller<RuleListScene> {

    private static final Logger LOG = Logger.getLogger(RuleListFXMLController.class.getName());

    @FXML
    private TabPane tpRuleList;
    private RuleListTab tabRuleList;
    private RuleTablesTab tabRuleTables;
    private RolesTab tabRoles;
    
    private static final PseudoClass ERROR_CSS_CLASS = PseudoClass.getPseudoClass("error");

    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @Override
    public void afterInitialisingScene() {
        super.afterInitialisingScene(); //To change body of generated methods, choose Tools | Templates.
        tabRuleList = new RuleListTab();
        tabRuleTables = new RuleTablesTab();
        tabRoles = new RolesTab();
        tpRuleList.getTabs().addAll(tabRuleList, tabRuleTables, tabRoles);
        restoreSelection();
        getScene().getRoot().addEventFilter(UpdateRuleEvent.updateRuleEvent(), (UpdateRuleEvent t) -> {
            if (getScene().getPool().equals(t.getPool())) {
                if(t.isValidationInvalid()){
                    t.getRule().setCrgrMessage(getScene().getRuleMessage(t.getRule()));//getScene().validateRule(t.getRule()));
                }
                reloadRule(t.getRule(),false);
                tabRoles.reload();
                t.consume();
            }
        });
        getScene().getRoot().addEventFilter(UpdateRulesEvent.updateRulesEvent(), (UpdateRulesEvent t) -> {
            if (getScene().getPool().equals(t.getPool())) {
                for(CrgRules rule : t.getRules()){
                    if(t.isValidationInvalid()){
                        rule.setCrgrMessage(getScene().getRuleMessage(rule));//getScene().validateRule(rule));
                    }
                    reloadRule(rule,true);
                }
                tabRoles.reload();
                tabRuleTables.refresh();
                tabRuleList.refresh();
                t.consume();
            }
        });
        getScene().getRoot().addEventFilter(RuleTableChangedEvent.ruleTableChangedEvent(), new EventHandler<RuleTableChangedEvent>() {
            @Override
            public void handle(RuleTableChangedEvent event) {
                event.consume();
                reloadRuleTables();
            }

            private void reloadRuleTables() {
                tabRuleTables.reload();
            }
        });
//        getScene().getRoot().addEventFilter(EventType.ROOT, new EventHandler<Event>() {
//            @Override
//            public void handle(Event t) {
//                LOG.info("set event " + t.getClass().getSimpleName());
//            }
//        });
        getScene().getRoot().addEventFilter(UpdateRuleTypeEvent.updateRuleTypeEvent(), (UpdateRuleTypeEvent t) -> {
            LOG.info("update eventtype, reload tab content rulesTables");
            tabRuleList.reload();
            t.consume();
        });
        tpRuleList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) -> {
            if (newValue == null) {
                return;
            }
            Session.instance().setRecentRuleListTab(newValue.getClass().getSimpleName());
        });
//        getScene().getProperties().addListener(new MapChangeListener<Object, Object>() {
//            @Override
//            public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
//                if(change.wasAdded()){
//                    if(RuleListScene.UPDATE_POOL_DATA.equals(change.getKey())){
//                        updatePoolData();
//                        getScene().getProperties().remove(RuleListScene.UPDATE_POOL_DATA);
//                    }
//                }
//            }
//        });
    }

    @Override
    public void reload() {
        if (tpRuleList.getSelectionModel().getSelectedItem() instanceof RuleListTab) {
            RuleListTab ruleList = (RuleListTab) tpRuleList.getSelectionModel().getSelectedItem();
            ruleList.reload();
        }
    }

    @Override
    public void refresh() {
        if (tpRuleList.getSelectionModel().getSelectedItem() instanceof RuleTablesTab) {
            tabRuleTables.reload();
        }
    }

    public void reloadRule(CrgRules pRule,boolean pForcefully) {
//        if (tpRuleList.getSelectionModel().getSelectedItem() instanceof RuleListTab) {
//            RuleListTab ruleList = (RuleListTab) tpRuleList.getSelectionModel().getSelectedItem();
//            ruleList.updateRule(pRule);
//        
//        }
        tabRuleList.updateRule(pRule,pForcefully);
    }

    private void restoreSelection() {
        String selection = Session.instance().getRecentRuleListTab();
        if (selection == null) {
            tpRuleList.getSelectionModel().selectFirst();
            return;
        }
        if (selection.endsWith(tabRuleList.getClass().getSimpleName())) {
            tpRuleList.getSelectionModel().select(tabRuleList);
        }
        if (selection.endsWith(tabRuleTables.getClass().getSimpleName())) {
            tpRuleList.getSelectionModel().select(tabRuleTables);
        }
        if (selection.endsWith(tabRoles.getClass().getSimpleName())) {
            tpRuleList.getSelectionModel().select(tabRoles);
        }
    }

//    private void updatePoolData() {
//
//    }
    private class RuleListTab extends RuleEditorTab {

        private TableViewMasterDetailPane<CrgRules> mdPane;
        private RuleListTableView tableView;

        public RuleListTab() {
            super();
            setText("Regelliste");
            setErroneous(getScene().containsErroneousRule());
            selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                if (t1) {
                    if (!initContent()) {
                        if (getTableView().getItems().isEmpty()) {
                            reload();
                        }
                    }

                }
            });
            getScene().poolProperty().addListener((ObservableValue<? extends CrgRulePools> ov, CrgRulePools t, CrgRulePools t1) -> {
                onPoolChanged(t1);
            });
            onPoolChanged(getScene().getPool());
        }

        @Override
        public void refresh() {
            super.refresh(); //To change body of generated methods, choose Tools | Templates.
            setErroneous(getScene().containsErroneousRule());
        }
        
        private void onPoolChanged(@NotNull CrgRulePools pPool) {
            Objects.requireNonNull(pPool, "Pool must not be null!");
            if (isSelected()) {
                reload();
            }
        }

        public RuleListTableView getTableView() {
            return tableView;
        }

        private boolean initContent() {
            if (mdPane == null) {
                tableView = new RuleTableView();
                mdPane = new TableViewMasterDetailPane<>(tableView);
                tableView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends CrgRules> ov, CrgRules t, CrgRules t1) -> {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            mdPane.setDetail(createDetailNode(t1, getScene().getPool()));
                        }
                    });
                });
                tableView.setOnRowDragDetected((MouseEvent t) -> {
                    t.getTarget();
                    if (tableView.getSelectionModel().isEmpty()) {
                        t.consume();
                        return;
                    }
                    Dragboard db = tableView.startDragAndDrop(TransferMode.ANY);
                    ClipboardContent content1 = new ClipboardContent();
                    ObservableList<CrgRules> selection = tableView.getSelectionModel().getSelectedItems();
                    content1.put(DndRulesToPool.DATA_FORMAT, new DndRulesToPool(selection.stream().collect(Collectors.toList()), getScene().getPool()));
                    db.setContent(content1);
                    t.consume();
                });
                setContent(mdPane);
                tableView.reload();
                return true;
            }
            return false;
        }

        public void reload() {
            if (tableView != null) {
                tableView.reload();
            }
        }

        private Parent createDetailNode(CrgRules pRule, CrgRulePools pPool) {
            if (pRule == null) {
                VBox box = new VBox(new Label("Nichts ausgewählt"));
                box.setAlignment(Pos.CENTER);
                return box;
            }
            RulePreview preview = new RulePreview();
            preview.setRule(pRule);
            preview.setPool(pPool);
            return preview;
        }

        private void updateRule(CrgRules pRule,boolean pForcefully) {
            getTableView().updateRule(pRule);
            if(pForcefully){
//                getTableView().getSelectionModel().select(null);
                int index = indexOfRuleById(pRule.getId());
                if (index == -1) {
                    //abort if rule is not present in the client
                    //because its filtered
                    //update backing list in tableview??
                    return;
                }
                getTableView().getItems().set(index, pRule);
//                getTableView().getSelectionModel().select(pRule);
            }else{
                updateRuleIfSelected(pRule);
            }
        }
        private void updateRuleIfSelected(CrgRules pRule){
            CrgRules selected = getTableView().getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (selected.getId() == pRule.getId()) {
                    getTableView().getSelectionModel().select(null);
                    int index = getTableView().getItems().indexOf(selected);
                    if (index == -1) {
                        //abort if rule is not present in the client
                        //because its filtered
                        //update backing list in tableview??
                        return;
                    }
                    getTableView().getItems().set(index, pRule);
                    getTableView().getSelectionModel().select(pRule);
                }
            }
        }
        
        private int indexOfRuleById(long pRuleId){
            for(int idx = 0;idx<getTableView().getItems().size();idx++){
                if(getTableView().getItems().get(idx).getId() == pRuleId){
                    return idx;
                }
            }
            return -1;
        }
    }

    private class RuleTableView extends RuleListTableView {

        public RuleTableView() {
            super(RuleListFXMLController.this.getScene().getFilterManager());
            AddButton btnAddMenu = new AddButton();
            btnAddMenu.setDisable(!RuleListFXMLController.this.getScene().isEditable());
            btnAddMenu.setTooltip(new Tooltip("Regel hinzufügen"));
            btnAddMenu.setOnAction((ActionEvent t) -> {
                long start = System.currentTimeMillis();
                CrgRules rule = RuleListFXMLController.this.getScene().createRule();
                LOG.info("get new Rule from Server in " + (System.currentTimeMillis() - start));
                databaseList.add(rule);
                reload();
                getSelectionModel().clearSelection();
                getSelectionModel().select(rule);
                lastSelected = getSelectionModel().getSelectedIndex();
                OpenRuleEvent event = new OpenRuleEvent(OpenRuleEvent.openRuleEvent(),
                        RuleListFXMLController.this.getScene().getPool(),
                        rule);
                Event.fireEvent(RuleTableView.this.getScene(), event);
                callRefresh();
                LOG.info("create Rule in " + (System.currentTimeMillis() - start));
            });
            DeleteButton btnDelete = new DeleteButton();
            btnDelete.setTooltip(new Tooltip("Regel(n) löschen"));
            btnDelete.setOnAction((ActionEvent event) -> {
                onDelete(getSelectionModel().getSelectedItems());
            });
            if (RuleListFXMLController.this.getScene().isEditable()) {
                btnDelete.disableProperty().bind(getSelectionModel().selectedItemProperty().isNull());
            } else {
                btnDelete.setDisable(true);
            }

//            ClearLocksButton btnClearLocks = new ClearLocksButton();
//            btnClearLocks.setOnAction((ActionEvent t) -> {
//                if (RuleListFXMLController.this.getScene().clearLocks()) {
//                    MainApp.showInfoMessageDialog("Regeln für diesen Pool erfolgreich entsperrt");
//                } else {
//                    MainApp.showErrorMessageDialog("Regeln für diesen Pool konnten nicht entsperrt werden!");
//                }
//            });

            ExportButton btnExport = new ExportButton();
            btnExport.setTooltip(new Tooltip("Alle Regeln exportieren"));
            btnExport.setOnAction((ActionEvent event) -> {
                DirectoryChooser directoryChooser = FileChooserFactory.instance().createDirectoryChooser(FileChooserFactory.RULE_EXPORT);//new DirectoryChooser();
//                    directoryChooser.setInitialDirectory(new File(CpxSystemProperties.getInstance().getCpxProgramDir()));
                File selectedDirectory = directoryChooser.showDialog(MainApp.getStage());
                if (selectedDirectory == null) {
                    //No Directory selected
                    return;
                }
                CpxClientConfig.instance().setUserRecentFileChooserPath(selectedDirectory, FileChooserFactory.RULE_EXPORT);
                LOG.info("ExportPath: " + selectedDirectory.getAbsolutePath());

                ExportPoolTask task = new ExportPoolTask(RuleListFXMLController.this.getScene().getPool(), selectedDirectory) {
                    @Override
                    public String getPoolAsXml(CrgRulePools pPool) {
                        return RuleListFXMLController.this.getScene().getPoolAsXml();
                    }
                };
                task.start();
            });

            ImportButton btnImport = new ImportButton();
            btnImport.setTooltip(new Tooltip("Alle Regeln aus einer Datei importieren"));
            btnImport.setDisable(!RuleListFXMLController.this.getScene().isEditable());
            btnImport.setOnAction((ActionEvent event) -> {
                FileChooser fileChooser = FileChooserFactory.instance().createFileChooser(FileChooserFactory.RULE_IMPORT);//new FileChooser();
                fileChooser.setTitle("Importdatei auswählen");
//                    fileChooser.setInitialDirectory(new File(CpxSystemProperties.getInstance().getCpxProgramDir()));
                fileChooser.getExtensionFilters().addAll(
                        //                        new FileChooser.ExtensionFilter("All Images", "*.*"),
                        new FileChooser.ExtensionFilter("XML", "*.xml")
                //                        new FileChooser.ExtensionFilter("PNG", "*.png")
                );
                File file = fileChooser.showOpenDialog(MainApp.getWindow());
                if (file == null) {
                    event.consume();
                    return;
                }
                CpxClientConfig.instance().setUserRecentFileChooserPath(file, FileChooserFactory.RULE_IMPORT);
                ImportRulesTask task = new ImportRulesTask(file) {
                    @Override
                    public List<RuleExchangeError> importRules(String pContent, RuleImportCheckFlags pDoCheck, RuleOverrideFlags pDoOverride) throws RuleEditorProcessException {
                        return RuleListFXMLController.this.getScene().importRules(pContent, pDoCheck, pDoOverride);
                    }

                    @Override
                    public void afterTask(Worker.State pState) {
                        super.afterTask(pState);
                        //CPX-1959: clear caches for pool and type after import
                        CrgRulePools pool = RuleListFXMLController.this.getScene().getPool();
                        RuleMetaDataCache.instance().clearRuleTypesForPoolType(PoolTypeHelper.getPoolType(pool));
                        RuleMetaDataCache.instance().clearPoolToTableCache(pool);
//                        RuleMetaDataCache.instance().clearPoolToTableCache(pool);
                        //force reload
                        databaseList.clear();
                        getFilterManager().resetColumns();
                        reload();
                        tabRuleList.refresh();
                        tabRoles.markDirty();
                        tabRuleTables.markDirty();
                        tabRuleTables.refresh();
                    }
                };
                task.start();
            });
            setOnRowClick((MouseEvent t) -> {
                if (t.getClickCount() >= 2 && getSelectionModel().getSelectedItem() != null) {
                    OpenRuleEvent event = new OpenRuleEvent(OpenRuleEvent.openRuleEvent(),
                            RuleListFXMLController.this.getScene().getPool(),
                            getSelectionModel().getSelectedItem());
                    Event.fireEvent(RuleTableView.this, event);
                }
            });
            setOnDelete((List<CrgRules> pRules) -> {
                try {
                    RuleListFXMLController.this.getScene().deleteRules(pRules);
                    databaseList.removeAll(pRules);
                    reload();
                    return true;
                } catch (RuleEditorProcessException ex) {
                    MainApp.showErrorMessageDialog(" Beim Löschen der Regel: " + pRules.stream().map((t) -> {
                        return t.getCrgrNumber(); //To change body of generated lambdas, choose Tools | Templates.
                    }).collect(Collectors.joining(",")) + " ist ein Fehler aufgetreten");
                    Logger.getLogger(RuleListScene.class.getName()).log(Level.SEVERE, null, ex);
                    return false;
                }
            });
            getMenuItems().add(btnAddMenu);
            getMenuItems().add(btnDelete);
            getMenuItems().add(btnImport);
            getMenuItems().add(btnExport);
//            getMenuItems().add(btnClearLocks);
        }

        @Override
        public void callRefresh() {
            super.callRefresh(); //To change body of generated methods, choose Tools | Templates.
            tabRuleList.refresh();
            tabRuleTables.refresh();
        }
        
        @Override
        public List<CrgRules> loadItems(int pStartIndex, int pEndIndex) {
            long start = System.currentTimeMillis();
            getFilterManager().hasMessages(RuleListFXMLController.this.getScene().containsErroneousRule());
            List<CrgRules> rules = RuleListFXMLController.this.getScene().loadRules(pStartIndex, pEndIndex);
            LOG.log(Level.INFO, "Load list of rules ({0}) in {1} ms", new Object[]{rules != null ? rules.size() : "null", System.currentTimeMillis() - start});
            databaseList.addAll(rules);
            return rules;
        }

        @Override
        public ContextMenu createContextMenu() {
            ContextMenu menu = super.createContextMenu();
            MenuItem delete = new MenuItem("Regel(n) löschen");
            //AWi delete should always be allowed following proposal from AGe
//            delete.setDisable(!RuleListFXMLController.this.getScene().isEditable());
            delete.setOnAction((ActionEvent t) -> {
                onDelete(getSelectionModel().getSelectedItems());
            });

            MenuItem dublicate = new MenuItem("Regel duplizieren");
            dublicate.setDisable(!RuleListFXMLController.this.getScene().isEditable());
            dublicate.setOnAction((ActionEvent t) -> {
                long start = System.currentTimeMillis();
                CrgRules dublicate1 = RuleListFXMLController.this.getScene().copyRule(getSelectionModel().getSelectedItem());
                if (dublicate1 != null) {
                    LOG.info("dublicate Rule in " + (System.currentTimeMillis() - start));
                    databaseList.add(dublicate1);
                    reload();
                    getSelectionModel().clearSelection();
                    getSelectionModel().select(dublicate1);
                    lastSelected = getSelectionModel().getSelectedIndex();
                    OpenRuleEvent event = new OpenRuleEvent(OpenRuleEvent.openRuleEvent(),
                            RuleListFXMLController.this.getScene().getPool(),
                            dublicate1);
                    Event.fireEvent(RuleTableView.this.getScene(), event);
                    callRefresh();
                    LOG.info("dublicate, refresh and open rule in " + (System.currentTimeMillis() - start));
//                    databaseList.add(dublicate1);
//                    reload();
//                    callRefresh();
                }
            });
            Menu copyTo = new CopyToMenuItem();
            Menu exportMenu = new ExportMenu();
            menu.getItems().addAll(dublicate, copyTo, exportMenu, new SeparatorMenuItem(), delete);
            return menu;
        }
        private class ExportMenu extends Menu{
            public ExportMenu(){
                super("Regel(n) exportieren");
                getItems().addAll(new ExportSelectionXmlItem(), new ExportSelectionCsvItem(), new ExportSelectionXslxItem());
            }
        }
        private class ExportSelectionCsvItem extends MenuItem{
            public ExportSelectionCsvItem(){
                super("als .csv");
                setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        DirectoryChooser directoryChooser = FileChooserFactory.instance().createDirectoryChooser("rule_export");//new DirectoryChooser();
                        File selectedDirectory = directoryChooser.showDialog(MainApp.getStage());
                        if (selectedDirectory == null) {
                            //No Directory selected
                            return;
                        }
                        LOG.info("ExportPath: " + selectedDirectory.getAbsolutePath());
                        ExportRulesAsCsvTask task = new ExportRulesAsCsvTask(getFilter().getColumnsOrdered(), getItems(), RuleListFXMLController.this.getScene().getPool(), selectedDirectory);
                        task.start();
                    }
                });
                
            }
        }
        private class ExportSelectionXslxItem extends MenuItem{
            public ExportSelectionXslxItem(){
                super("als .xlsx");
                setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        DirectoryChooser directoryChooser = FileChooserFactory.instance().createDirectoryChooser("rule_export");//new DirectoryChooser();
                        File selectedDirectory = directoryChooser.showDialog(MainApp.getStage());
                        if (selectedDirectory == null) {
                            //No Directory selected
                            return;
                        }
                        LOG.info("ExportPath: " + selectedDirectory.getAbsolutePath());
                        CpxClientConfig.instance().setUserRecentFileChooserPath(selectedDirectory, "rule_export");
                        ExportRulesAsXslxTask task = new ExportRulesAsXslxTask(getFilter().getColumnsOrdered(), getItems(), RuleListFXMLController.this.getScene().getPool(), selectedDirectory);
                        task.start();
                    }
                });
            }
        }
        private class ExportSelectionXmlItem extends MenuItem {

            public ExportSelectionXmlItem() {
                super("als .xml");
                setOnAction((ActionEvent t) -> {
                    DirectoryChooser directoryChooser = FileChooserFactory.instance().createDirectoryChooser("rule_export");//new DirectoryChooser();
                    File selectedDirectory = directoryChooser.showDialog(MainApp.getStage());
                    if (selectedDirectory == null) {
                        //No Directory selected
                        return;
                    }
                    LOG.info("ExportPath: " + selectedDirectory.getAbsolutePath());
                    CpxClientConfig.instance().setUserRecentFileChooserPath(selectedDirectory, "rule_export");
                    ExportRulesTask task = new ExportRulesTask(getSelectionModel().getSelectedItems(), RuleListFXMLController.this.getScene().getPool(), selectedDirectory) {
                        @Override
                        public String getRulesAsXml(List<CrgRules> pRules) {
                            return RuleListFXMLController.this.getScene().getRulesFromPoolAsXml(getRuleIds(pRules));
                        }
                    };
                    task.start();
                });
            }

        }

        private class CopyToMenuItem extends Menu {

            public CopyToMenuItem() {
                //AWi following proposal from AGe, if scene is not etiable e.g. prod pool, than text will change to indicate that rule will be moved(removed) from prod 
                super(RuleListFXMLController.this.getScene().isEditable() ? "Regel(n) kopieren nach" : "Regel(n) verschieben nach");
                List<CrgRulePools> availablePools = RuleListFXMLController.this.getScene().getAvailableTargetPools();
                for (CrgRulePools pool : availablePools) {
                    MenuItem item = new MenuItem(pool.getCrgplIdentifier() + " (" + getPoolType(pool) + ")");
                    item.setMnemonicParsing(false);
                    item.setOnAction((ActionEvent t) -> {
                        CopyToTask task = new CopyToTask(RuleListFXMLController.this.getScene().getPool(), pool, getSelectionModel().getSelectedItems()) {
                            @Override
                            public void afterTask(Worker.State pState) {
                                super.afterTask(pState); //To change body of generated methods, choose Tools | Templates.
                                if (Worker.State.SUCCEEDED.equals(pState)) {
                                    RuleMetaDataCache.instance().clearRuleTypesForPoolType(PoolTypeHelper.getPoolType(pool));
                                    RuleMetaDataCache.instance().clearPoolToTableCache(pool);
                                    callRefresh();
                                    if(!RuleListFXMLController.this.getScene().isEditable()){
                                        databaseList.removeAll(getSelectionModel().getSelectedItems());
                                        reload();
                                    }
                                }
                            }

                            @Override
                            public RuleExchangeResult call(CrgRulePools pOrigin, CrgRulePools pTarget, List<Long> pRules, RuleImportCheckFlags pCheckFlag, RuleOverrideFlags pOverrideFlag) {
                                return RuleListFXMLController.this.getScene().copyRulesTo(pRules, pTarget, pCheckFlag, pOverrideFlag);
                            }

                        };
                        task.start();
                    });
                    getItems().add(item);
                }
            }

            private String getPoolType(CrgRulePools pool) {
                if (pool instanceof CrgRulePoolsProd) {
                    return "Produktivpool";
                }
                return "Arbeitspool";
            }
        }

    }
    private class RuleEditorTab extends Tab{
        
        public RuleEditorTab(){
            super();
            getStyleClass().add("no-text-tab");
            Label label = new Label(getText());
            label.textProperty().bind(textProperty());
            RuleMessageIndicator indicator = new RuleMessageIndicator();
            indicator.setPadding(new Insets(0));
            indicator.setTooltip(new CpxTooltip("In diesem Tab befinden sich einer oder mehr ungültige Einträge!",100,5000,100,true));
            HBox header = new HBox(5,label);
            setGraphic(header);
            erroneousProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                    if(t1){
                        if(!header.getChildren().contains(indicator)){
                            header.getChildren().add(0,indicator);
                        }
                        label.setStyle("-fx-text-fill:orangered");
                    }else{
                        if(header.getChildren().contains(indicator)){
                            header.getChildren().remove(indicator);
                        }
                        label.setStyle("-fx-text-fill:white01");
                    }
                    label.pseudoClassStateChanged(ERROR_CSS_CLASS, t1);
                }
            });
            label.pseudoClassStateChanged(ERROR_CSS_CLASS, isErroneous());
        }
        private final ReadOnlyBooleanWrapper erroneousProperty = new ReadOnlyBooleanWrapper();
        public final ReadOnlyBooleanProperty erroneousProperty(){
            return erroneousProperty.getReadOnlyProperty();
        }
        public final void markErroneous(){
            setErroneous(true);
        }
        public final boolean isErroneous(){
            return erroneousProperty().get();
        }
        protected final void setErroneous(boolean pErroneous){
            erroneousProperty.set(pErroneous);
        }
        
        private boolean dirty;
        public void markDirty(){
            setDirty(true);
        }
        public final boolean isDirty(){
            return dirty;
        }
        protected final void setDirty(boolean pDirty){
            dirty = pDirty;
        }
        
        public void refresh(){
            LOG.info("Call refresh, but not yet implemented!");
        }
    }
    private class RuleTablesTab extends RuleEditorTab{

        private RulesTablesMasterDetail mdPane;

        public RuleTablesTab() {
            super();
            setErroneous(getScene().containsErroneousRuleTable());
            setText("Regeltabellen");
            selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (newValue) {
                    initContent();
                }
            });
            getScene().poolProperty().addListener((ObservableValue<? extends CrgRulePools> ov, CrgRulePools t, CrgRulePools t1) -> {
                if (isSelected()) {
                    reload();
                }
            });
            setOnSelectionChanged((Event change) -> {
                if (mdPane == null) {
                    return;
                }
                if (isSelected()) {
                    return;
                }
                if (mdPane.hasUnsavedContent()) {
                    UnsavedContentDialog dialog = new UnsavedContentDialog();
                    dialog.showAndWait().ifPresent((ButtonType t) -> {
                        if (ButtonType.CANCEL.equals(t)) {
                            Platform.runLater(() -> {
                                tpRuleList.getSelectionModel().select(tabRuleTables);
                            });
                            return;
                        }
                        if (RuleEditorButtonTypes.SAVE_ALL.equals(t)) {
                            TaskService<Void> updateRoleTask = new TaskService<Void>("Neue Regel/Rollen Beziehung wird gespeichert!") {
                                @Override
                                public Void call() {
                                    ListView<CrgRuleTablesItem> listView = mdPane.getListView();
                                    if (listView == null) {
                                        //should not occure
                                        LOG.warning("ListView is not properly initialized!");
                                        return null;
                                    }
                                    List<CrgRuleTables> dirtyTables = new ArrayList<>();
                                    for (CrgRuleTablesItem item : listView.getItems()) {
                                        if (item.isDirty()) {
                                            if (item.getStateManager() != null) {
                                                item.getStateManager().save();
                                            }
                                            dirtyTables.add(item.getTable());
                                            CrgRulePools pool = RuleListFXMLController.this.getScene().getPool();
                                            RuleMetaDataCache.instance().updateTableInPoolToTableMap(pool, item.getTable());
                                            Session.instance().getEjbConnector().connectRuleEditorBean().get().updateRuleTable(pool.getId(), PoolTypeHelper.getPoolType(pool), item.getTable());
                                        }
                                    }
                                    //validate rules
                                    //if performance issue- the update should be more smart
                                    //this implementation could validate rules multiple times!
                                    //should check if rule is initalized/validated if not skip 
                                    for (CrgRuleTables item : dirtyTables) {
//                                        Platform.runLater(new Runnable() {
//                                            @Override
//                                            public void run() {
                                                List<CrgRules> rules = RuleListFXMLController.this.getScene().getRulesForTable(item);
                                                for (CrgRules rule : rules) {
                                                    rule.setCrgrMessage(getScene().getRuleMessage(rule));//getScene().validateRule(rule));
                                                    new RuleMessageReader().read(rule);
                                                    reloadRule(rule,true);
                                                }
//                                            }
//                                        });
                                    }
                                    return null;
                                }

                                @Override
                                public void afterTask(Worker.State pState) {
                                    super.afterTask(pState); //To change body of generated methods, choose Tools | Templates.
                                    if (Worker.State.SUCCEEDED.equals(pState)) {
                                        ListView<CrgRuleTablesItem> listView = mdPane.getListView();
                                        if (listView != null) {
                                            listView.refresh();
                                        }
                                        tabRuleTables.reload();
                                        tabRuleTables.refresh();
                                        tabRuleList.refresh();
//                                                    Event.fireEvent(RuleListFXMLController.this.getScene(), t);
                                    }
                                }

                            };
                            updateRoleTask.start();
                            return;
                        }
                        if (RuleEditorButtonTypes.DISCARD.equals(t)) {

                            int index = getSelectedIndex();
                            mdPane.selectItem(null);
                            RuleMetaDataCache.instance().clearPoolToTableCache(RuleListFXMLController.this.getScene().getPool());
                            ListView<CrgRuleTablesItem> listView = mdPane.getListView();
                            if (listView != null) {
                                listView.refresh();
                            }
                            mdPane.reload();
                            selectIndex(index);
                            return;
                        }
                    });
                }
            });
        }
        @Override
        public void refresh() {
            super.refresh(); //To change body of generated methods, choose Tools | Templates.
            setErroneous(getScene().containsErroneousRuleTable());
        }
        private void initContent() {
            if (mdPane == null) {
                mdPane = new RulesTablesMasterDetail() {
                    @Override
                    public List<CrgRuleTablesItem> fetchList() {
                        String text = mdPane.getFilterText();
                        List<CrgRuleTables> list = RuleListFXMLController.this.getScene().findRuleTables(text);
                        if (list == null) {
                            return new ArrayList<>();
                        }
                        list.sort(Comparator.comparing(CrgRuleTables::getCrgtTableName));

                        List<CrgRuleTablesItem> items = list.stream().map((t) -> {
                            return new CrgRuleTablesItem(t); //To change body of generated lambdas, choose Tools | Templates.
                        }).collect(Collectors.toCollection(ArrayList::new));
                        setDirty(false);
                        return items;
                    }
                };
                mdPane.setOnSearchTitle((String p) -> (CrgRuleTablesItem t) -> {
                    if (t.getTable().getCrgtTableName() == null) {
                        return false;
                    }
                    return t.getTable().getCrgtTableName().toLowerCase().contains(p.toLowerCase());
                });
                mdPane.unsavedContentProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                    setText((t1 ? "*" : "") + "Regeltabellen");
                });
                setContent(mdPane);
//                mdPane.reload();
            }
            if(isDirty()){
                mdPane.reload();
            }
        }

        public int getSelectedIndex() {
            ListView<CrgRuleTablesItem> listView = mdPane.getListView();
            if (listView != null) {
                return listView.getSelectionModel().getSelectedIndex();
            }
            return -1;
        }

        public void selectIndex(int pIndex) {
            ListView<CrgRuleTablesItem> listView = mdPane.getListView();
            if (listView == null) {
                return;
            }
            listView.getSelectionModel().select(pIndex);
        }

        private void reload() {
            if (mdPane != null) {
                mdPane.reload();
            }
        }

//        private void refresh() {
//
//        }
    }

    private class RolesTab extends RuleEditorTab {

        private RolesMasterDetail mdPane;

        public RolesTab() {
            super();
            setText("Rollen");
            setOnSelectionChanged((Event change) -> {
                if (mdPane == null) {
                    return;
                }
                if (isSelected()) {
                    return;
                }
                if (mdPane.hasUnsavedContent()) {
                    UnsavedContentDialog dialog = new UnsavedContentDialog();
                    dialog.showAndWait().ifPresent((ButtonType t) -> {
                        if (ButtonType.CANCEL.equals(t)) {
                            Platform.runLater(() -> {
                                tpRuleList.getSelectionModel().select(tabRoles);
                            });
                            return;
                        }
                        if (RuleEditorButtonTypes.SAVE_ALL.equals(t)) {
                            TaskService<Void> updateRoleTask = new TaskService<Void>("Neue Regel/Rollen Beziehung wird gespeichert!") {
                                @Override
                                public Void call() {
                                    ListView<CdbUserRolesItem> listView = mdPane.getListView();
                                    if (listView == null) {
                                        return null;
                                    }
                                    for (CdbUserRolesItem item : listView.getItems()) {
                                        if (item.isDirty()) {
//                                                        item.getStateManager().save();
                                            RuleListFXMLController.this.getScene().updateRoleToRule(item);
                                            ArrayList<CrgRules> newItems = item.getStateManager().getCurrentStateItem();
                                            ArrayList<CrgRules> oldItems = item.getStateManager().getOriginalStateItem();
                                            List<Long> changeset = item.getRuleChangeSet(oldItems, newItems);
                                            Event.fireEvent(RuleListFXMLController.this.getScene(),
                                                    new UpdateRoleEvent(UpdateRoleEvent.updateRoleEvent(),
                                                            RuleListFXMLController.this.getScene().getPool(),
                                                            item.getRole().getId(),
                                                            changeset));
                                            item.getStateManager().save();
                                        }
                                    }
                                    return null;
                                }

                                @Override
                                public void afterTask(Worker.State pState) {
                                    super.afterTask(pState); //To change body of generated methods, choose Tools | Templates.
                                    if (Worker.State.SUCCEEDED.equals(pState)) {
                                        ListView<CdbUserRolesItem> listView = mdPane.getListView();
                                        if (listView != null) {
                                            listView.refresh();
                                        }
                                        tabRuleList.reload();
//                                                    Event.fireEvent(RuleListFXMLController.this.getScene(), t);
                                    }
                                }

                            };
                            updateRoleTask.start();
                            return;
                        }
                        if (RuleEditorButtonTypes.DISCARD.equals(t)) {

                            int index = getSelectedIndex();
                            mdPane.selectItem(null);
                            mdPane.reload();
                            selectIndex(index);
                            return;
                        }
                    });
                }
            });
            selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (newValue) {
                    initContent();
                }
            });
            getScene().poolProperty().addListener((ObservableValue<? extends CrgRulePools> ov, CrgRulePools t, CrgRulePools t1) -> {
                if (isSelected()) {
                    reload();
                }
            });
        }

        public int getSelectedIndex() {
            ListView<CdbUserRolesItem> listView = mdPane.getListView();
            if (listView == null) {
                return -1;
            }
            return listView.getSelectionModel().getSelectedIndex();
        }

        public void selectIndex(int pIndex) {
            ListView<CdbUserRolesItem> listView = mdPane.getListView();
            if (listView == null) {
                return;
            }
            listView.getSelectionModel().select(pIndex);
        }

        private void initContent() {
            if (mdPane == null) {
                mdPane = new RolesMasterDetail() {
                    @Override
                    public List<CdbUserRolesItem> fetchList() {
                        List<CdbUserRoles> list = MenuCache.instance().getRoles();//RuleListFXMLController.this.getScene().findRuleTables();
                        if (list == null) {
                            return new ArrayList<>();
                        }
                        list.sort(Comparator.comparing(CdbUserRoles::getCdburName));

                        List<CdbUserRolesItem> items = list.stream().map((t) -> {
                            return new CdbUserRolesItem(t) {
                                @Override
                                public ArrayList<CrgRules> fetchAvailableItems(CdbUserRoles pRoles) {
                                    return RuleListFXMLController.this.getScene().getAllAvailableRulesForRole(pRoles);
                                }

                                @Override
                                public ArrayList<CrgRules> fetchActiveItems(CdbUserRoles pRoles) {
                                    return RuleListFXMLController.this.getScene().getAllActiveRulesForRole(pRoles);
                                }
                            }; //To change body of generated lambdas, choose Tools | Templates.
                        }).collect(Collectors.toCollection(ArrayList::new));
                        setDirty(false);
                        return items;
                    }
                };
                mdPane.setOnSearchTitle((String p) -> (CdbUserRolesItem t) -> {
                    if (t.getRole().getCdburName() == null) {
                        return false;
                    }
                    return t.getRole().getCdburName().toLowerCase().contains(p.toLowerCase());
                });
                mdPane.unsavedContentProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                    setText((t1 ? "*" : "") + "Rollen");
                });
                setContent(mdPane);
//                mdPane.reload();
            }
            if(isDirty()){
                mdPane.reload();
            }
        }

        private void reload() {
            if (mdPane != null) {
                mdPane.reload();
            }
        }

//        private void refresh() {
//
//        }
    }

    private abstract class RulesTablesMasterDetail extends MasterDetailBorderPane {

        private boolean selectFirst = false;

        private AsyncPane<ListView<CrgRuleTablesItem>> asyncPane;
        private FilteredList<CrgRuleTablesItem> filteredList;
        private final ReadOnlyObjectWrapper<CrgRuleTablesItem> selectedItemProperty = new ReadOnlyObjectWrapper<>();
        private final BooleanProperty showSearchFieldProperty = new SimpleBooleanProperty(false);
        private Callback<String, Predicate<CrgRuleTablesItem>> onSearchTitle = (String p) -> (CrgRuleTablesItem t) -> true;
        private BooleanProperty unsavedContentProperty;

        public RulesTablesMasterDetail() {
            super();
            setMaster(createMaster());
            selectedItemProperty().addListener((ObservableValue<? extends CrgRuleTablesItem> observable, CrgRuleTablesItem oldValue, CrgRuleTablesItem newValue) -> {
                setDetail(createDetail(newValue));
            });
            addEventFilter(RefreshEvent.refreshEvent(), (RefreshEvent event) -> {
                ListView<CrgRuleTablesItem> lv = getListView();
                if (lv != null) {
                    lv.refresh();
                }
            });
            filterTextProperty.addListener((ObservableValue<? extends String> ov, String t, String t1) -> {
                asyncPane.reload();
//                    filteredList.setPredicate(getOnSearchTitle().call(t1));
            });
        }
//        private Parent createDetail(CrgRuleTablesItem pItem){
//            return createDetail(pItem!=null?pItem.getTable():null);
//        }

        private Parent createDetail(CrgRuleTablesItem pItem) {
            if (pItem == null) {
                HBox box = new HBox(new Label("Sie haben keinen Eintrag ausgewählt!"));
                box.setAlignment(Pos.CENTER);
                return box;
            }
            if (pItem.getTable().getCrgtContent() == null) {
                String content = RuleListFXMLController.this.getScene().getRuleTableContent(pItem.getTable());
                pItem.getTable().setCrgtContent(content != null ? content : "");
                pItem.getStateManager().init(pItem.getTable());
            }
            if(pItem.getTable().getCrgtComment() == null){
                String comment = RuleListFXMLController.this.getScene().getRuleTableComment(pItem.getTable());
                pItem.getTable().setCrgtComment(comment != null ? comment : "");
                pItem.getStateManager().init(pItem.getTable());
            }
            CrgRulePools pool = RuleListFXMLController.this.getScene().getPool();
            List<CrgRules> rules = RuleListFXMLController.this.getScene().getRulesForTable(pItem.getTable());
            RuleTablesDetail detailContent = new RuleTablesDetail(pool, pItem, rules); //new Label(newValue.getCrgtTableName());
            PoolTypeEn type = PoolTypeHelper.getPoolType(pool);
            detailContent.setViewMode(PoolTypeEn.PROD.equals(type) ? ViewMode.READ_ONLY : ViewMode.READ_WRITE);
            detailContent.setCodeSuggestionCallback(new Callback<String, String>() {
                @Override
                public String call(String p) {
                    return RuleListFXMLController.this.getScene().findCodeSuggestionsForCode(p, pItem.getTable());
                }
            });
            detailContent.setValidationCalllback(new Callback<CrgRuleTables,byte[]>(){
                @Override
                public byte[] call(CrgRuleTables p) {
                   return RuleListFXMLController.this.getScene().validateRuleTable(p); 
                }
            });
            return detailContent;
        }

        private ListView<CrgRuleTablesItem> getListView() {
            if (asyncPane.getContent() != null && asyncPane.getContent() instanceof ListView) {
                return (ListView<CrgRuleTablesItem>) asyncPane.getContent();
            }
            return null;
        }

        private Pane createMaster() {
            VBox header = createHeader();
            asyncPane = new AsyncPane<>() {
                private ListView<CrgRuleTablesItem> view;

                @Override
                public ListView<CrgRuleTablesItem> loadContent() {
                    view = new ListView<>() {
                        @Override
                        public void refresh() {
                            setUnsavedContent(false);
                            super.refresh(); //To change body of generated methods, choose Tools | Templates.
                        }

                    };
                    
                    view.setCellFactory((ListView<CrgRuleTablesItem> param) -> {
                        ListCell<CrgRuleTablesItem> cell = new ListCell<CrgRuleTablesItem>() {
                            @Override
                            protected void updateItem(CrgRuleTablesItem item, boolean empty) {
                                super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                                if (item == null || empty) {
                                    setGraphic(null);
                                    return;
                                }
                                Label label = new Label((item.isDirty() ? "*" : "") + (item.getTable().getCrgtTableName()));
                                if (item.isDirty()) {
                                    setUnsavedContent(true);
                                }
                                label.setPadding(new Insets(10, 0, 10, 0));
                                if(item.getTable().getCrgtMessage()!=null){
                                    RuleMessageIndicator indicator = new RuleMessageIndicator();
                                    indicator.setTooltip(new CpxTooltip(new RuleTableMessageReader().getRuleTableMessageTooltip(item.getTable()), 100, 5000, 100, true));
                                    label.setGraphic(indicator);
//                                    label.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"), true);
                                }
                                label.pseudoClassStateChanged(ERROR_CSS_CLASS, item.getTable().getCrgtMessage()!=null);
                                setGraphic(label);
//                                    setText(item.getCrgtTableName());
                            }

                        };
                        return cell;
                    });
                    List<CrgRuleTablesItem> fetchedList = fetchList();
                    filteredList = new FilteredList<>(FXCollections.observableArrayList(fetchedList));
                    view.getStyleClass().add("stay-selected-list-view");
                    view.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                    view.setItems(filteredList);
                    view.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends CrgRuleTablesItem> observable, CrgRuleTablesItem oldValue, CrgRuleTablesItem newValue) -> {
                        selectItem(newValue);
                    });
//                    if(!SELECT_FIRST){
//                        view.getSelectionModel().select(getSelectedItem());
//                    }else{
//                        view.getSelectionModel().select(0);
//                        SELECT_FIRST = false;
//                    }
//                    view.scrollTo(getSelectedItem());
                    return view;
                }

                @Override
                public void afterTask(Worker.State pState) {
                    super.afterTask(pState); //To change body of generated methods, choose Tools | Templates.
                    if (Worker.State.SUCCEEDED.equals(pState)) {
                        if (getSelectedItem() == null) {
                            return;
                        }
                        if (!selectFirst) {
                            int idx = view.getItems().indexOf(getSelectedItem());
                            selectItem(null);
                            view.getSelectionModel().clearAndSelect(idx);
//                            createDetail(getSelectedItem());
//                            view.getSelectionModel().select(idx);
                        } else {
                            view.getSelectionModel().select(0);
                            selectFirst = false;
                        }
//                    Platform.runLater(new Runnable() {
//                        @Override
//                        public void run() {
//                            setDetail(createDetail(getSelectedItem()));
//                        }
//                    });
                        view.scrollTo(getSelectedItem());
                    }
                }

            };
            VBox master = new VBox(4, header, asyncPane);
            VBox.setVgrow(asyncPane, Priority.ALWAYS);
//            asyncPane.setPadding(new Insets(8, 0, 0, 0));
            master.setFillWidth(true);
            master.getStyleClass().add("rule-tables-master");
            master.setPadding(new Insets(0, 5, 0, 0));
            return master;
        }

        public final ReadOnlyObjectProperty<CrgRuleTablesItem> selectedItemProperty() {
            return selectedItemProperty.getReadOnlyProperty();
        }

        public void selectItem(CrgRuleTablesItem pTable) {
            selectedItemProperty.set(pTable);
        }

        public CrgRuleTablesItem getSelectedItem() {
            return selectedItemProperty.get();
        }

        public boolean isShowSearchField() {
            return showSearchFieldProperty.get();
        }

        public abstract List<CrgRuleTablesItem> fetchList();

        public void setOnSearchTitle(Callback<String, Predicate<CrgRuleTablesItem>> pCallback) {
            onSearchTitle = pCallback;
        }

        public Callback<String, Predicate<CrgRuleTablesItem>> getOnSearchTitle() {
            return onSearchTitle;
        }
        private final StringProperty filterTextProperty = new SimpleStringProperty("");

        public void setFilterText(String pText) {
            filterTextProperty.set(pText);
        }

        public String getFilterText() {
            return filterTextProperty.get();
        }

        private VBox createHeader() {
            Pane menu = createMenu();
            Pane searchBox = createSearchBox();
//            Separator sep = new Separator(Orientation.HORIZONTAL);

            VBox box = new VBox(7, menu/*,sep*/);
            if (isShowSearchField()) {
                box.getChildren().add(searchBox);
            }
            showSearchFieldProperty.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (newValue) {
                    if (!box.getChildren().contains(searchBox)) {
                        box.getChildren().add(searchBox);
                    }
                } else {
                    box.getChildren().remove(searchBox);
                }
            });
            return box;
        }

        private Pane createMenu() {
            AddButton btnAdd = new AddButton();
            btnAdd.setTooltip(new Tooltip("Regeltabelle hinzufügen"));
            btnAdd.setDisable(!RuleListFXMLController.this.getScene().isEditable());
            btnAdd.setOnAction((ActionEvent event) -> {
                CrgRuleTables newTable = RuleListFXMLController.this.getScene().createTable();
                RuleMetaDataCache.instance().addTableInPoolToTableMap(RuleListFXMLController.this.getScene().getPool(), newTable);
//                selectItem(new CrgRuleTablesItem(newTable));
                selectFirst = true;
                asyncPane.reload();
                tabRuleList.refresh();
                tabRuleTables.refresh();
            });
            DeleteButton btnDelete = new DeleteButton();
            btnDelete.setTooltip(new Tooltip("Regeltabelle löschen"));
            btnDelete.setDisable(!RuleListFXMLController.this.getScene().isEditable());
            if (!btnDelete.isDisabled()) {
                btnDelete.disableProperty().bind(selectedItemProperty().isNull());
            }
            btnDelete.setOnAction((ActionEvent event) -> {
                ListView<CrgRuleTablesItem> listview = getListView();
                if(listview==null){
                    LOG.warning("Can not delete item, listview is not initialized!");
                    return;
                }
                int selectedItems = listview.getSelectionModel().getSelectedItems().size();
                boolean isMulti = selectedItems>1;
//                CrgRuleTablesItem selected = getSelectedItem();
//                long sizeOfRelations = RuleListFXMLController.this.getScene().getRelatedRulesCount4Table(selected.getTable());
                long sizeOfRelations = RuleListFXMLController.this.getScene().getRelatedRulesCount4Table(listview.getSelectionModel().getSelectedItems().stream().map((t) -> {
                    return t.getTable();
                }).collect(Collectors.toList()));
                if (sizeOfRelations > 0) {
                    String msg;
                    if(!isMulti){
                        CrgRuleTablesItem selected = getSelectedItem();
                        msg = "Regeltabelle: " + selected.getTable().getCrgtTableName() + " kann nicht gelöscht werden:"
                            + "\nAnzahl der Regel(n), die die Regeltabelle: " + selected.getTable().getCrgtTableName() + " referenzieren: " + sizeOfRelations;
                    }else{
                        msg = "Die ausgewählten Regeltabellen ("+selectedItems+") können nicht gelöscht werden:"
                            + "\nAnzahl der Regel(n), die die Regeltabellen referenzieren: " + sizeOfRelations;    
                    }
                    AlertDialog dialog = AlertDialog.createWarningDialog(msg, ButtonType.OK);
                    dialog.showAndWait();
                    return;
                }
                String msg;
                if(!isMulti){
                    CrgRuleTablesItem selected = getSelectedItem();
                    msg = "Regeltabelle: " + selected.getTable().getCrgtTableName() + "\nWirklich löschen?";
                }else{
                    msg = "Die ausgewählten Regeltabellen (" + selectedItems + ")\nWirklich löschen?";    
                }
                AlertDialog dialog = AlertDialog.createWarningDialog(msg, ButtonType.OK, ButtonType.CANCEL);
                dialog.showAndWait().ifPresent(new Consumer<ButtonType>() {
                    @Override
                    public void accept(ButtonType t) {
                        if (ButtonType.OK.equals(t)) {
                            for (CrgRuleTablesItem selected : getSelectedItems()) {
                                long start = System.currentTimeMillis();
                                deleteRuleTable(selected.getTable());
                                LOG.info("delete of " + selected.getTable().getCrgtTableName() + " took " + (System.currentTimeMillis() - start) + " ms");
                            }
                            tabRuleList.refresh();
                            tabRuleTables.refresh();
                            asyncPane.reload();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    RefreshEvent event = new RefreshEvent(RefreshEvent.refreshEvent(), asyncPane);
                                    Event.fireEvent(asyncPane, event);
                                }
                            });
                        }
                    }
                    private List<CrgRuleTablesItem> getSelectedItems(){
                        ListView<CrgRuleTablesItem> lv = getListView();
                        if(lv == null){
                            return new ArrayList<>();
                        }
                        return lv.getSelectionModel().getSelectedItems();
                    }
                    private void deleteRuleTable(CrgRuleTables pToDelete) {
                        int index = filteredList.indexOf(pToDelete);
                        long start = System.currentTimeMillis();
                        boolean success = RuleListFXMLController.this.getScene().deleteTable(pToDelete);
                        LOG.info("delete " + pToDelete.getCrgtTableName() + " on server successful? " + success + " in " + (System.currentTimeMillis() - start) + " ms");
                        if (success) {
                            RuleMetaDataCache.instance().removeTableInPoolToTableMap(RuleListFXMLController.this.getScene().getPool(), pToDelete);
                            //select next item if list is not empty or index exceeded range of filterlist
                            //otherwise select nothing
                            if (!filteredList.isEmpty() && filteredList.size() > index) {
                                Integer next = getNextIndex(index);
                                selectItem(next != null ? filteredList.get(next) : null);
                            } else {
                                selectItem(null);
                            }
//                            asyncPane.reload();
                            LOG.info("delete " + pToDelete.getCrgtTableName() + " on client side took " + (System.currentTimeMillis() - start) + " ms");
                        }
                    }

                    private Integer getNextIndex(int index) {
                        if (index == -1) {
                            return null;
                        }
                        int next = index + 1;
                        if (next > filteredList.size()) {
                            return null;
                        }
                        return next;
                    }
                });
            });
            ReloadButton btnReload = new ReloadButton();
            btnReload.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    asyncPane.reload();
                }
            });
            SearchToggleButton btnSearch = new SearchToggleButton();
            btnSearch.setTooltip(new Tooltip("Regeltabellen durchsuchen"));
            btnSearch.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                showSearchFieldProperty.set(t1);
            });

            CopyButton btnCopy = new CopyButton();
            btnCopy.setTooltip(new Tooltip("Regeltabelle kopieren"));
            btnCopy.setDisable(!RuleListFXMLController.this.getScene().isEditable());
            if (!btnCopy.isDisabled()) {
                btnCopy.disableProperty().bind(selectedItemProperty().isNull());
            }
            btnCopy.setOnAction((ActionEvent event) -> {
                CrgRuleTables copy = RuleListFXMLController.this.getScene().copyTable(getSelectedItem().getTable());
                RuleMetaDataCache.instance().addTableInPoolToTableMap(RuleListFXMLController.this.getScene().getPool(), copy);
//                SELECT_FIRST = true;
                CrgRuleTablesItem item = new CrgRuleTablesItem(copy);
                selectItem(item);
                asyncPane.reload();
            });

            HBox menuBox = new HBox(5, btnSearch, btnAdd, btnCopy, btnDelete, btnReload);
            btnSearch.setMaxHeight(Double.MAX_VALUE);
            menuBox.setFillHeight(true);
            menuBox.setPrefHeight(32);
            menuBox.setAlignment(Pos.CENTER_RIGHT);
            HBox.setHgrow(menuBox, Priority.ALWAYS);
            HBox box = new HBox(5, menuBox);
            box.setAlignment(Pos.CENTER_LEFT);
            return box;
        }

        private Pane createSearchBox() {
            TextField field = new TextField();
            field.textProperty().addListener((ObservableValue<? extends String> ov, String t, String t1) -> {
                setFilterText(t1);
//                    filteredList.setPredicate(getOnSearchTitle().call(t1));
            });
            field.setFont(Font.font(field.getFont().getFamily(), 15));
            field.setPromptText("Nach Tabellennamen suchen");
            VBox box = new VBox(field);
            box.setPadding(new Insets(5));
            box.setFillWidth(true);
            return box;
        }

        private void reload() {
            if (asyncPane != null) {
                asyncPane.reload();
            }
        }

        public BooleanProperty unsavedContentProperty() {
            if (unsavedContentProperty == null) {
                unsavedContentProperty = new SimpleBooleanProperty(false);
            }
            return unsavedContentProperty;
        }

        public void setUnsavedContent(boolean pUnsavedContent) {
            unsavedContentProperty().set(pUnsavedContent);
        }

        public boolean hasUnsavedContent() {
            return unsavedContentProperty().get();
        }
    }

    private abstract class RolesMasterDetail extends MasterDetailBorderPane {

        private AsyncPane<ListView<CdbUserRolesItem>> asyncPane;
        private FilteredList<CdbUserRolesItem> filteredList;
        private final ReadOnlyObjectWrapper<CdbUserRolesItem> selectedItemProperty = new ReadOnlyObjectWrapper<>();
        private final BooleanProperty showSearchFieldProperty = new SimpleBooleanProperty(false);
        private BooleanProperty unsavedContentProperty;
        private Callback<String, Predicate<CdbUserRolesItem>> onSearchTitle = (String p) -> (CdbUserRolesItem t) -> true;

        public RolesMasterDetail() {
            super();
            setMaster(createMaster());
            selectedItemProperty().addListener((ObservableValue<? extends CdbUserRolesItem> observable, CdbUserRolesItem oldValue, CdbUserRolesItem newValue) -> {
                setDetail(createDetail(newValue));
            });
            addEventFilter(RefreshEvent.refreshEvent(), (RefreshEvent event) -> {
                ListView<CdbUserRolesItem> lv = getListView();
                if (lv != null) {
                    lv.refresh();
                }
            });
        }

        private Parent createDetail(CdbUserRolesItem pItem) {
            if (pItem == null) {
                HBox box = new HBox(new Label("Sie haben keinen Eintrag ausgewählt!"));
                box.setAlignment(Pos.CENTER);
                return box;
            }
            UserRolesDetail detailContent = new UserRolesDetail(pItem);

            detailContent.addEventHandler(UpdateRoleEvent.updateRoleEvent(), (UpdateRoleEvent t) -> {
                LOG.info("update role event!");
                if (t.getRuleIds().isEmpty()) {
                    t.consume();
                    return;
                }
                TaskService<Void> updateRoleTask = new TaskService<Void>("Neue Regel/Rollen Beziehung wird gespeichert!") {
                    @Override
                    public Void call() {
                        RuleListFXMLController.this.getScene().updateRoleToRule(pItem);
                        return null;
                    }

                    @Override
                    public void afterTask(Worker.State pState) {
                        super.afterTask(pState); //To change body of generated methods, choose Tools | Templates.
                        if (Worker.State.SUCCEEDED.equals(pState)) {
                            ListView<CdbUserRolesItem> listView = getListView();
                            if (listView != null) {
                                listView.refresh();
                            }
                            tabRuleList.reload();
                            Event.fireEvent(RuleListFXMLController.this.getScene(), t);
                        }
                    }

                };
                updateRoleTask.start();
            });
            PoolTypeEn type = PoolTypeHelper.getPoolType(RuleListFXMLController.this.getScene().getPool());
            detailContent.setViewMode(PoolTypeEn.PROD.equals(type) ? ViewMode.READ_ONLY : ViewMode.READ_WRITE);
            detailContent.setPool(RuleListFXMLController.this.getScene().getPool());
            return detailContent;
        }

        private ListView<CdbUserRolesItem> getListView() {
            if (asyncPane.getContent() != null && asyncPane.getContent() instanceof ListView) {
                return (ListView<CdbUserRolesItem>) asyncPane.getContent();
            }
            return null;
        }

        private Pane createMaster() {
            VBox header = createHeader();
            asyncPane = new AsyncPane<>() {
                @Override
                public ListView<CdbUserRolesItem> loadContent() {
                    ListView<CdbUserRolesItem> view = new ListView<>() {
                        @Override
                        public void refresh() {
                            setUnsavedContent(false);
                            super.refresh(); //To change body of generated methods, choose Tools | Templates.
                        }
                    };
                    view.setCellFactory((ListView<CdbUserRolesItem> param) -> {
                        ListCell<CdbUserRolesItem> cell = new ListCell<CdbUserRolesItem>() {
                            @Override
                            protected void updateItem(CdbUserRolesItem item, boolean empty) {
                                super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                                if (item == null || empty) {
                                    setGraphic(null);
                                    return;
                                }
                                //query in update item is usually not a smart thing to do..
                                //Maybe fix this possible issue later
                                Long count = RuleListFXMLController.this.getScene().getRuleCountForRole(item.getRole().getId());
                                Label label = new Label((item.isDirty() ? "*" : "") + (item.getRole().getCdburName()) + "(" + count + ")");
                                if (item.isDirty()) {
                                    setUnsavedContent(true);
                                }
                                label.setPadding(new Insets(10, 0, 10, 0));
                                setGraphic(label);
//                                    setText(item.getCrgtTableName());
                            }

                        };
                        return cell;
                    });
                    List<CdbUserRolesItem> fetchedList = fetchList();
                    filteredList = new FilteredList<>(FXCollections.observableArrayList(fetchedList));
                    view.getStyleClass().add("stay-selected-list-view");
                    view.setItems(filteredList);
                    view.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends CdbUserRolesItem> observable, CdbUserRolesItem oldValue, CdbUserRolesItem newValue) -> {
                        selectItem(newValue);
                    });
                    view.getSelectionModel().select(getSelectedItem());
                    Platform.runLater(() -> {
                        setDetail(createDetail(getSelectedItem()));
                    });
                    view.scrollTo(getSelectedItem());
                    return view;
                }
            };
            VBox master = new VBox(4, header, asyncPane);
            VBox.setVgrow(asyncPane, Priority.ALWAYS);
//            asyncPane.setPadding(new Insets(8, 0, 0, 0));
            master.setFillWidth(true);
            master.getStyleClass().add("rule-tables-master");
            master.setPadding(new Insets(0, 5, 0, 0));
            return master;
        }

        public final ReadOnlyObjectProperty<CdbUserRolesItem> selectedItemProperty() {
            return selectedItemProperty.getReadOnlyProperty();
        }

        public void selectItem(CdbUserRolesItem pTable) {
            selectedItemProperty.set(pTable);
        }

        public CdbUserRolesItem getSelectedItem() {
            return selectedItemProperty.get();
        }

        public boolean isShowSearchField() {
            return showSearchFieldProperty.get();
        }

        public BooleanProperty unsavedContentProperty() {
            if (unsavedContentProperty == null) {
                unsavedContentProperty = new SimpleBooleanProperty(false);
            }
            return unsavedContentProperty;
        }

        public void setUnsavedContent(boolean pUnsavedContent) {
            unsavedContentProperty().set(pUnsavedContent);
        }

        public boolean hasUnsavedContent() {
            return unsavedContentProperty().get();
        }

        public abstract List<CdbUserRolesItem> fetchList();

        public void setOnSearchTitle(Callback<String, Predicate<CdbUserRolesItem>> pCallback) {
            onSearchTitle = pCallback;
        }

        public Callback<String, Predicate<CdbUserRolesItem>> getOnSearchTitle() {
            return onSearchTitle;
        }

        private VBox createHeader() {
            Pane menu = createMenu();
            Pane searchBox = createSearchBox();
//            Separator sep = new Separator(Orientation.HORIZONTAL);

            VBox box = new VBox(7, menu/*,sep*/);
            if (isShowSearchField()) {
                box.getChildren().add(searchBox);
            }
            showSearchFieldProperty.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (newValue) {
                    if (!box.getChildren().contains(searchBox)) {
                        box.getChildren().add(searchBox);
                    }
                } else {
                    box.getChildren().remove(searchBox);
                }
            });
            return box;
        }

        private Pane createMenu() {

            SearchToggleButton btnSearch = new SearchToggleButton();
            btnSearch.setTooltip(new Tooltip("Rollen durchsuchen"));
            btnSearch.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                showSearchFieldProperty.set(t1);
            });
            ReloadButton btnReload = new ReloadButton();
            btnReload.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    asyncPane.reload();
                }
            });
            
            HBox menuBox = new HBox(5, btnSearch, btnReload);
            btnSearch.setMaxHeight(Double.MAX_VALUE);
            menuBox.setFillHeight(true);
            menuBox.setPrefHeight(32);
            menuBox.setAlignment(Pos.CENTER_RIGHT);
            HBox.setHgrow(menuBox, Priority.ALWAYS);
            HBox box = new HBox(5, menuBox);
            box.setAlignment(Pos.CENTER_LEFT);
            return box;
        }

        private Pane createSearchBox() {
            TextField field = new TextField();
            field.textProperty().addListener((ObservableValue<? extends String> ov, String t, String t1) -> {
                filteredList.setPredicate(getOnSearchTitle().call(t1));
            });
            field.setFont(Font.font(field.getFont().getFamily(), 15));
            field.setPromptText("Nach Rollenname suchen");
            VBox box = new VBox(field);
            box.setPadding(new Insets(5));
            box.setFillWidth(true);
            return box;
        }

        private void reload() {
            if (asyncPane != null) {
                asyncPane.reload();
            }
        }

        public void refresh() {
            setDetail(createDetail(getSelectedItem()));
        }
    }
}
