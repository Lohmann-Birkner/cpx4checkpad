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
package de.lb.cpx.client.ruleeditor.menu;

import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.button.AddButton;
import de.lb.cpx.client.core.model.fx.button.DeleteButton;
import de.lb.cpx.client.core.model.fx.button.EditButton;
import de.lb.cpx.client.core.model.fx.button.ExportButton;
import de.lb.cpx.client.core.model.fx.button.ImportButton;
import de.lb.cpx.client.core.model.fx.button.SearchToggleButton;
import de.lb.cpx.client.core.model.fx.section.SectionHeader;
import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.client.ruleeditor.events.UpdateRuleEvent;
import de.lb.cpx.client.ruleeditor.events.UpdateRuleTypeEvent;
import de.lb.cpx.client.ruleeditor.menu.dialogs.CreatePoolDialog;
import de.lb.cpx.client.ruleeditor.menu.dialogs.ExportPoolDialog;
import de.lb.cpx.client.ruleeditor.menu.dialogs.ImportPoolDialog;
import de.lb.cpx.client.ruleeditor.menu.filterlists.RuleListScene;
import de.lb.cpx.client.ruleeditor.menu.filterlists.tableview.RuleListTableView;
import de.lb.cpx.client.ruleeditor.menu.popover.EditPoolPopOver;
import de.lb.cpx.client.ruleeditor.model.dnd.DndRulesToPool;
import de.lb.cpx.client.ruleeditor.model.titledPane.ListViewTitledPane;
import de.lb.cpx.client.ruleeditor.task.CopyToTask;
import de.lb.cpx.client.ruleeditor.task.ExportPoolTask;
import de.lb.cpx.client.ruleeditor.task.ImportRulesTask;
import de.lb.cpx.ruleviewer.cache.RuleMetaDataCache;
import de.lb.cpx.ruleviewer.event.RefreshEvent;
import de.lb.cpx.ruleviewer.event.RuleTableChangedEvent;
import de.lb.cpx.ruleviewer.model.RuleMessageIndicator;
import de.lb.cpx.ruleviewer.util.PoolTypeHelper;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePoolsDev;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePoolsProd;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.server.commons.enums.RuleImportCheckFlags;
import de.lb.cpx.server.commons.enums.RuleOverrideFlags;
import de.lb.cpx.server.rule.services.RuleEditorProcessException;
import de.lb.cpx.server.rule.services.RuleExchangeError;
import de.lb.cpx.server.rule.services.RuleExchangeResult;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.concurrent.Worker;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author wilde
 */
public class PoolOverviewFXMLController extends Controller<PoolOverviewScene> {

    @FXML
    private VBox boxPools;
    @FXML
    private AnchorPane apPoolContent;
    @FXML
    private SectionHeader shPoolsMenu;
    private ProductivePools prodPools;
    private WorkingPools devPools;

    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        prodPools = new ProductivePools();
        prodPools.setText("Produktivpools");
//        prod.setMaxHeight(Double.MAX_VALUE);
        devPools = new WorkingPools();
        devPools.setText("Arbeitspools");
//        work.setMaxHeight(Double.MAX_VALUE);

        VBox.setVgrow(prodPools, Priority.ALWAYS);
        VBox.setVgrow(devPools, Priority.ALWAYS);
        boxPools.getChildren().addAll(prodPools, devPools);
        shPoolsMenu.getMenuItems().addAll(new AddPoolButton(null), new ImportPoolButton(), new ExportPoolButton());

    }

    @Override
    public void afterInitialisingScene() {
        super.afterInitialisingScene(); //To change body of generated methods, choose Tools | Templates.
        getScene().selectedPoolProperty().addListener(new ChangeListener<CrgRulePools>() {
            @Override
            public void changed(ObservableValue<? extends CrgRulePools> ov, CrgRulePools t, CrgRulePools t1) {
                updateContent(t,t1);
            }

        });
        getScene().getProperties().addListener(new MapChangeListener<Object, Object>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
                if (change.wasAdded()) {
                    if (PoolOverviewScene.RELOAD_POOLS_DEV.equals(change.getKey())) {
                        devPools.reload();
                        getScene().getProperties().remove(PoolOverviewScene.RELOAD_POOLS_DEV);
                    }
                    if (PoolOverviewScene.RELOAD_POOLS_PROD.equals(change.getKey())) {
                        prodPools.reload();
                        getScene().getProperties().remove(PoolOverviewScene.RELOAD_POOLS_PROD);
                    }
                }
                setContent(createFilterListMasterDetail(getScene().getSelectedPool()));
            }
        });
        setContent(createFilterListMasterDetail(getScene().getSelectedPool()));
        getScene().addEventFilter(UpdateRuleEvent.updateRuleEvent(), (UpdateRuleEvent t) -> {
            Event.fireEvent(getContent(), t);
        });
//        getScene().addEventFilter(UpdateRulesEvent.updateRulesEvent(), (UpdateRulesEvent t) -> {
//            Event.fireEvent(getContent(), t);
//        });
        getScene().addEventFilter(RuleTableChangedEvent.ruleTableChangedEvent(), (RuleTableChangedEvent t) -> {
            Event.fireEvent(getContent(), t);
        });
        getScene().addEventFilter(UpdateRuleTypeEvent.updateRuleTypeEvent(), (UpdateRuleTypeEvent t) -> {
            Event.fireEvent(getContent(), t);
        });
    }
    private void updateContent(CrgRulePools pOldPool,CrgRulePools pNewPool) {
        if (pNewPool == null) {
            devPools.deselect();
            prodPools.deselect();
            return;
        }
        if (pNewPool instanceof CrgRulePoolsDev) {
            prodPools.deselect();
            devPools.selectItem(pNewPool);
        }
        if (pNewPool instanceof CrgRulePoolsProd) {
            devPools.deselect();
            prodPools.selectItem(pNewPool);
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setContent(createFilterListMasterDetail(pNewPool));
            }
        });
    }
    @Override
    public void reload() {
        super.reload(); //To change body of generated methods, choose Tools | Templates.
//        setContent(createFilterListMasterDetail(getScene().getSelectedPool()));
    }

    private void setContent(Node pNode) {
        apPoolContent.getChildren().clear();
        AnchorPane.setTopAnchor(pNode, 0.0);
        AnchorPane.setRightAnchor(pNode, 0.0);
        AnchorPane.setBottomAnchor(pNode, 0.0);
        AnchorPane.setLeftAnchor(pNode, 0.0);
        apPoolContent.getChildren().add(pNode);
    }

    private Node getContent() {
        if (apPoolContent.getChildren().isEmpty()) {
            return null;
        }
        return apPoolContent.getChildren().get(0);
    }
    private ObjectProperty<RuleListScene> contentSceneProperty;

    public ObjectProperty<RuleListScene> contentSceneProperty() {
        if (contentSceneProperty == null) {
            contentSceneProperty = new SimpleObjectProperty<>();
        }
        return contentSceneProperty;
    }

    public RuleListScene getContentScene() {
        return contentSceneProperty().get();
    }

    public void setContentScene(RuleListScene pScene) {
        contentSceneProperty().set(pScene);
    }

    private Node createFilterListMasterDetail(CrgRulePools t1) {
        if (t1 == null) {
            VBox placeholder = new VBox(new Label("Nichts ausgewählt"));
            placeholder.setAlignment(Pos.CENTER);
            return placeholder;
        }
        try {
            RuleListScene listScene = new RuleListScene(t1);
            listScene.getRoot().addEventFilter(RefreshEvent.refreshEvent(), new EventHandler<RefreshEvent>() {
                @Override
                public void handle(RefreshEvent t) {
                    if (devPools != null) {
                        devPools.refresh();
                    }
                    if (prodPools != null) {
                        prodPools.refresh();
                    }
                }
            });
            setContentScene(listScene);
            return listScene.getRoot();
        } catch (IOException ex) {
            Logger.getLogger(PoolOverviewFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            return new VBox(new Label(ex.getMessage()));
        }

    }

    private class ProductivePools extends ListViewTitledPane<CrgRulePools> {

        public ProductivePools() {
            super();
            setAnimated(false);
            SearchToggleButton btnSearch = new SearchToggleButton();
            btnSearch.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                    setShowSearchBox(t1);
                }
            });
            Button btnAdd = new AddPoolButton(PoolTypeEn.PROD);
            setCellFactory(new PoolCellFactory());
            getMenuItems().addAll(btnSearch, btnAdd);//,new ImportButton(),new ExportButton());
            setOnSearchTitle(new Callback<String, Predicate<CrgRulePools>>() {
                @Override
                public Predicate<CrgRulePools> call(String p) {
                    return new Predicate<CrgRulePools>() {
                        @Override
                        public boolean test(CrgRulePools t) {
                            if (t.getCrgplIdentifier() == null) {
                                return false;
                            }
                            return t.getCrgplIdentifier().toLowerCase().contains(p.toLowerCase());
                        }

                    };
                }
            });
            selectedItemProperty().addListener(new ChangeListener<CrgRulePools>() {
                @Override
                public void changed(ObservableValue<? extends CrgRulePools> ov, CrgRulePools t, CrgRulePools t1) {
//                    PoolOverviewFXMLController.this.getScene().selectPool(null);
                    PoolOverviewFXMLController.this.getScene().selectPool(t1);
                }
            });
            expandedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    setMaxHeight(newValue ? Double.MAX_VALUE : 0.0);
                }
            });
            setMaxHeight(isExpanded() ? Double.MAX_VALUE : 0.0);
        }

        @Override
        public List<CrgRulePools> fetchList() {
            return PoolOverviewFXMLController.this.getScene().getProductivePools();
        }
        
    }
    private class WorkingPools extends ListViewTitledPane<CrgRulePools> {

        public WorkingPools() {
            super();
            setAnimated(false);
            SearchToggleButton btnSearch = new SearchToggleButton();
            btnSearch.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                    setShowSearchBox(t1);
                }
            });
            Button btnAdd = new AddPoolButton(PoolTypeEn.DEV);
            getMenuItems().addAll(btnSearch, btnAdd);//,new ImportButton(),new ExportButton());
            setCellFactory(new PoolCellFactory());
            setOnSearchTitle(new Callback<String, Predicate<CrgRulePools>>() {
                @Override
                public Predicate<CrgRulePools> call(String p) {
                    return new Predicate<CrgRulePools>() {
                        @Override
                        public boolean test(CrgRulePools t) {
                            if (t.getCrgplIdentifier() == null) {
                                return false;
                            }
                            return t.getCrgplIdentifier().toLowerCase().contains(p.toLowerCase());
                        }

                    };
                }
            });

            selectedItemProperty().addListener(new ChangeListener<CrgRulePools>() {
                @Override
                public void changed(ObservableValue<? extends CrgRulePools> ov, CrgRulePools t, CrgRulePools t1) {
//                    PoolOverviewFXMLController.this.getScene().selectPool(null);
                    PoolOverviewFXMLController.this.getScene().selectPool(t1);
                }
            });
            expandedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    setMaxHeight(newValue ? Double.MAX_VALUE : 0.0);
                }
            });
            setMaxHeight(isExpanded() ? Double.MAX_VALUE : 0.0);
        }

        @Override
        public List<CrgRulePools> fetchList() {
            return PoolOverviewFXMLController.this.getScene().getWorkingPools();
        }

    }

    private class PoolCellFactory implements Callback<ListView<CrgRulePools>, ListCell<CrgRulePools>> {

        @Override
        public ListCell<CrgRulePools> call(ListView<CrgRulePools> p) {
            return new PoolListCell();
        }

    }

    private class PoolListCell extends ListCell<CrgRulePools> {

        private static final String DEFAULT_BORDER_STYLE = "";
        private static final String ALL_BORDER_STYLE = "-fx-border-style: solid;\n"
                + "    -fx-border-width: 2;\n"
                + "    -fx-border-color: grey; ";

        public PoolListCell() {
            super();
            setOnDragExited(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    // remove all dnd effects
                    setStyle(DEFAULT_BORDER_STYLE);
                }
            });
            setOnDragOver(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent t) {
                    DndRulesToPool content = (DndRulesToPool) t.getDragboard().getContent(DndRulesToPool.DATA_FORMAT);
                    if (isEmpty()) {
                        return;
                    }
                    if (getItem() == null) {
                        return;
                    }

                    if (PoolTypeEn.PROD.equals(PoolTypeHelper.getPoolType(content.getOrigin())) && PoolTypeEn.PROD.equals(PoolTypeHelper.getPoolType(getItem()))) {
                        return;
                    }
                    if (getItem().getId() == content.getOrigin().getId()) {
                        return;
                    }
                    t.acceptTransferModes(TransferMode.ANY);
                    setStyle(ALL_BORDER_STYLE);
                }
            });
            setOnDragDropped(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent t) {
                    DndRulesToPool content = (DndRulesToPool) t.getDragboard().getContent(DndRulesToPool.DATA_FORMAT);
                    if (content == null) {
                        t.consume();
                        return;
                    }

                    CopyToTask task = new CopyToTask(content.getOrigin(), getItem(), content.getRules()) {

                        @Override
                        public void afterTask(Worker.State pState) {
                            super.afterTask(pState);
                            if (Worker.State.SUCCEEDED.equals(pState)) {
                                getListView().refresh();
                                if (t.getGestureSource() instanceof RuleListTableView) {
                                    RuleListTableView view = (RuleListTableView) t.getGestureSource();
                                    view.removeRules(content.getRules());
                                    CrgRulePools pool = getItem();
                                    RuleMetaDataCache.instance().clearRuleTypesForPoolType(PoolTypeHelper.getPoolType(pool));
                                    RuleMetaDataCache.instance().clearPoolToTableCache(pool);
                                }
                            }
                        }

                        @Override
                        public RuleExchangeResult call(CrgRulePools pOrigin, CrgRulePools pTarget, List<Long> pRules, RuleImportCheckFlags pCheckFlag, RuleOverrideFlags pOverrideFlag) {
                            try {
                                return PoolOverviewFXMLController.this.getScene().copyRulesTo(pOrigin, pTarget, pRules, pCheckFlag, pOverrideFlag);
                            } catch (RuleEditorProcessException ex) {
                                Logger.getLogger(PoolOverviewFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            return null;
                        }

                    };
                    task.start();
                }
            });
        }

        @Override
        protected void updateItem(CrgRulePools t, boolean bln) {
            super.updateItem(t, bln); //To change body of generated methods, choose Tools | Templates.
            if (t == null || bln) {
                setGraphic(null);
                return;
            }

            Label title = new Label(t.getCrgplIdentifier() /*+ (t.isCrgplIsActive() ? " - aktiv" : "")*/);
            Label from = new Label(t.getCrgplFrom() != null ? Lang.toDate(t.getCrgplFrom()) : "----");
            Label to = new Label(t.getCrgplTo() != null ? Lang.toDate(t.getCrgplTo()) : "----");
            Label year = new Label(String.valueOf(t.getCrgplPoolYear()));
            //risky server call, maybe executed to many times on large lists??
            PoolTypeEn type = PoolTypeHelper.getPoolType(t);
            int size = PoolOverviewFXMLController.this.getScene().findSizeOfRules(t.getId(), type);
            Label count = new Label(String.valueOf(size));//t.getCrgRuleses().size()));

            HBox details = new HBox(new Label("Jahr:"), year, new Label("von:"), from, new Label("bis:"), to, new Label("Anzahl:"), count);
            details.setFillHeight(true);
            details.setSpacing(5);

            HBox menu = new HBox(new EditPoolButton(this, t, type), new DeletePoolButton(t, type));
            menu.setAlignment(Pos.CENTER_RIGHT);
            HBox.setHgrow(menu, Priority.ALWAYS);
            HBox header = new HBox(title, menu);
//            if(!t.isCrgplIsActive()){
//                Label inActive = new Label("(Inaktiv)");
//                inActive.setFont(Font.font(inActive.getFont().getFamily(), FontPosture.ITALIC, inActive.getFont().getSize()));
//                header.getChildren().add(1, inActive);
//            }
            //not ideal - it is unknown how often updateItem is called! Every time to call the server could be impactful on the performance!
            boolean hasError = PoolOverviewFXMLController.this.getScene().containsAnyMessage(t.getId(), type);
            if(hasError){
                RuleMessageIndicator indicator = new RuleMessageIndicator();
                indicator.setPadding(Insets.EMPTY);
                indicator.setTooltip(new CpxTooltip("Eine oder mehrere Regeln und/oder Regeltabellen enthalten Fehler!", 100, 5000, 100, true));
                header.getChildren().add(0, indicator);
                title.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"), true);
            }
            header.setSpacing(5);
            header.setAlignment(Pos.CENTER_LEFT);

            VBox content = new VBox(header, details);
            content.setSpacing(5);
            content.setFillWidth(true);
            setGraphic(content);

        }
    }

    private class DeletePoolButton extends DeleteButton {

        public DeletePoolButton(CrgRulePools pPool, PoolTypeEn pType) {
            super();
//            getStyleClass().add("cpx-icon-button");
            setTooltip(new Tooltip("Pool löschen"));
//            setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.TRASH));
            setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    int sizeOfRules = PoolOverviewFXMLController.this.getScene().findSizeOfRules(pPool.getId(), pType);
                    int sizeOfTables = PoolOverviewFXMLController.this.getScene().findSizeOfRuleTables(pPool.getId(), pType);
                    if (sizeOfRules > 0 || sizeOfTables > 0) {
                        AlertDialog dialog = AlertDialog.createWarningDialog("Pool: " + pPool.getCrgplIdentifier() + " kann nicht gelöscht werden: "
                                + (sizeOfRules == 0 ? "" : "\nAnzahl der Regel(n) die der Pool: " + pPool.getCrgplIdentifier() + " referenziert: " + sizeOfRules)
                                + (sizeOfTables == 0 ? "" : "\nAnzahl der Regeltabellen(n) die der Pool: " + pPool.getCrgplIdentifier() + " referenziert: " + sizeOfTables),
                                ButtonType.OK);
                        dialog.showAndWait();
                        return;
                    }
                    AlertDialog dialog = AlertDialog.createWarningDialog("Pool: " + pPool.getCrgplIdentifier() + "\nWirklich löschen?", ButtonType.OK, ButtonType.CANCEL);
                    dialog.showAndWait().ifPresent(new Consumer<ButtonType>() {
                        @Override
                        public void accept(ButtonType t) {
                            if (ButtonType.OK.equals(t)) {
                                PoolOverviewFXMLController.this.getScene().deletePool(pPool, pType);
                                PoolOverviewFXMLController.this.getScene().reloadPool(pType);
                            }
                        }
                    });
                }
            });
//            setOnAction(new AddPoolEventHandler(pType));
        }
    }

    private class EditPoolButton extends EditButton {

        public EditPoolButton(ListCell<CrgRulePools> pOwner, CrgRulePools pPool, PoolTypeEn pType) {
            super();
            setTooltip(new Tooltip("Pool bearbeiten"));
            setOnAction(new EventHandler<ActionEvent>() {
                private EditPoolPopOver popover;

                @Override
                public void handle(ActionEvent t) {
                    long start = System.currentTimeMillis();
                    if (popover == null) {
                        popover = new EditPoolPopOver(pPool, pType);
                        popover.setAnimated(false);
                        popover.setUpdateCallback(new Callback<CrgRulePools, Boolean>() {
                            @Override
                            public Boolean call(CrgRulePools pPool) {
                                PoolOverviewFXMLController.this.getScene().updatePool(pPool, pType);
                                PoolOverviewFXMLController.this.getScene().reloadPool(pType);
                                return true;
                            }
                        });
                    }
                    if (!popover.isShowing()) {
                        popover.show(pOwner);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                PoolOverviewFXMLController.this.getScene().selectPool(pPool);
                            }
                        });
                    }
                    LOG.finest("handle on edit click in " + (System.currentTimeMillis() - start) + " ms");
                }
            });
        }
    }

    private class AddPoolButton extends AddButton {

        public AddPoolButton(PoolTypeEn pType) {
            super();
//            getStyleClass().add("cpx-icon-button");
            setTooltip(new Tooltip("Pool hinzufügen"));
//            setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.PLUS));
            setOnAction(new AddPoolEventHandler(pType));
        }

    }

    private class AddPoolEventHandler implements EventHandler<ActionEvent> {

        private final PoolTypeEn type;

        public AddPoolEventHandler(PoolTypeEn pType) {
            type = pType;
        }

        @Override
        public void handle(ActionEvent t) {
            CreatePoolDialog dialog = new CreatePoolDialog(type);
            dialog.showAndWait().ifPresent(new Consumer<ButtonType>() {
                @Override
                public void accept(ButtonType t) {
                    if (t.equals(ButtonType.OK)) {
                        CrgRulePools pool = dialog.onSave();
                        try {
                            PoolOverviewFXMLController.this.getScene().savePool(pool, type);
                        } catch (RuleEditorProcessException ex) {
                            Logger.getLogger(PoolOverviewFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                            MainApp.showErrorMessageDialog(ex, "Beim Anlegen des Regelpools " + pool.getCrgplIdentifier() + " ist ein Fehler aufgetreten!\n" + ex.getMessage());
                            return;
                        }
                        getScene().reloadPool(type);
//                        WorkingPools.this.reload();
                    }
                }
            });
        }
    }

    private class ImportPoolButton extends ImportButton {

        public ImportPoolButton() {
            super();
            setTooltip(new Tooltip("Regeln in neuen Pool importieren"));
            setOnAction(new ImportPoolEventHandler());
        }
    }

    private class ImportPoolEventHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent t) {
            ImportPoolDialog dialog = new ImportPoolDialog(null);
            dialog.showAndWait().ifPresent(new Consumer<ButtonType>() {
                @Override
                public void accept(ButtonType t) {
                    boolean showImportDialog = false;
                    if (t.equals(ButtonType.OK)) {
                        CrgRulePools pool = dialog.onSave();
                        PoolTypeEn type = dialog.getType();
                        try {
                            long poolId = PoolOverviewFXMLController.this.getScene().findPoolId4poolNameAndDates(pool, dialog.getType());
                            if (poolId == 0L) {
                                pool.setId(PoolOverviewFXMLController.this.getScene().savePool(pool, dialog.getType()));
                            } else {
                                pool.setId(poolId);
                                showImportDialog = true;
                            }
                        } catch (RuleEditorProcessException ex) {
                            MainApp.showErrorMessageDialog(ex, "Beim Anlegen des Regelpools " + pool.getCrgplIdentifier() + " ist ein Fehler aufgetreten!\n" + ex.getMessage());
                            Logger.getLogger(PoolOverviewFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                            return;
                        }
                        ImportRulesTask task = new ImportRulesTask(dialog.getFile()) {
                            @Override
                            public List<RuleExchangeError> importRules(String pContent, RuleImportCheckFlags pDoCheck, RuleOverrideFlags pDoOverride) throws RuleEditorProcessException {
                                return getScene().importPool(pool, type, pContent, pDoCheck, pDoOverride);
                            }

                            @Override
                            public void afterTask(Worker.State pState) {
                                super.afterTask(pState);
                                //CPX-1959: clear caches for pool and type after import
                                RuleMetaDataCache.instance().clearRuleTypesForPoolType(type);
//                                RuleMetaDataCache.instance().clearPoolToTableCache(pool);
                                getScene().reloadPool(type);
                            }

                        };
                        task.start(showImportDialog);
                    }
                }
            });
        }

    }

    private class ExportPoolButton extends ExportButton {

        public ExportPoolButton() {
            super();
            setTooltip(new Tooltip("Regeln aus Pool exportieren"));
            setOnAction(new ExportPoolEventHandler());
        }

    }

    private class ExportPoolEventHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            ExportPoolDialog dialog = new ExportPoolDialog();
            List<CrgRulePools> pools = new ArrayList<>();
            pools.addAll(devPools.getItems());
            pools.addAll(prodPools.getItems());
            dialog.setPools(pools);
            dialog.showAndWait().ifPresent((t) -> {
                if (ButtonType.OK.equals(t)) {
                    ExportPoolTask task = new ExportPoolTask(dialog.getPool(), dialog.getDir()) {
                        @Override
                        public String getPoolAsXml(CrgRulePools pPool) {
                            return PoolOverviewFXMLController.this.getScene().getPoolAsXML(pPool);
                        }
                    };
                    task.start();
                }
            });
        }

    }
}
