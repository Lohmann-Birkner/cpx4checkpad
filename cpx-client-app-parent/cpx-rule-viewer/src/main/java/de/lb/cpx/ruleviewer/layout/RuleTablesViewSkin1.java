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
package de.lb.cpx.ruleviewer.layout;

import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.model.fx.button.EditCommentButton;
import de.lb.cpx.client.core.model.fx.masterdetail.ListViewMasterDetailPane;
import de.lb.cpx.client.core.model.fx.pane.AsyncPane;
import de.lb.cpx.client.core.model.fx.tooltip.BasicTooltip;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.rule.criteria.CriteriaHelper;
import de.lb.cpx.rule.criteria.model.CriterionTree;
import de.lb.cpx.ruleviewer.cache.RuleMetaDataCache;
import de.lb.cpx.ruleviewer.event.RuleChangedEvent;
import de.lb.cpx.ruleviewer.event.RuleTableChangedEvent;
import de.lb.cpx.ruleviewer.model.ruletable.content.RuleTableContentPane;
import de.lb.cpx.ruleviewer.model.ruletable.content.RuleTableContentPane.RuleTableContentParam;
import de.lb.cpx.ruleviewer.model.ruletable.content.model.Item;
import de.lb.cpx.ruleviewer.model.state.StateManager;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.shared.json.RuleTableMessageReader;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * Skin class to show Content of the ruletables
 *
 * @author wilde
 */
public class RuleTablesViewSkin1 extends SkinBase<RuleTablesView> {

    private final ListViewMasterDetailPane<CrgRuleTablesItem> masterDetail;
    private final ListView<CrgRuleTablesItem> listView;
    private final TextField searchField;
    private FilteredList<CrgRuleTablesItem> filterList;

    public RuleTablesViewSkin1(RuleTablesView pSkinnable) {
        super(pSkinnable);
        getSkinnable().setPadding(new Insets(8));
        masterDetail = new ListViewMasterDetailPane<>();
        listView = new ListView<>();
        listView.getStyleClass().add("stay-selected-list-view");
        filterList = new FilteredList<>(makeItems(pSkinnable.getItems()));
        filterList.setPredicate((t) -> {
            return true; //To change body of generated lambdas, choose Tools | Templates.
        });
        listView.setItems(filterList);
        searchField = new TextField();
        searchField.setPromptText("Hier Suchtext eingeben");
        VBox sepBox = new VBox(5, searchField, new Separator(Orientation.HORIZONTAL));
        masterDetail.setListView(listView, sepBox);
        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.isEmpty()) {
                    filterList.setPredicate((CrgRuleTablesItem t) -> true);
                } else {
                    filterList.setPredicate((CrgRuleTablesItem t) -> t.getTable().getCrgtTableName().toUpperCase().contains(newValue.toUpperCase()));
                }
            }
        });
        masterDetail.setMasterDetailRatio(2.0);
        getChildren().add(masterDetail);
        masterDetail.setCellFactory(new Callback<ListView<CrgRuleTablesItem>, ListCell<CrgRuleTablesItem>>() {
            @Override
            public ListCell<CrgRuleTablesItem> call(ListView<CrgRuleTablesItem> param) {
                return new RuleTableListCell();
            }
        });
        pSkinnable.getItems().addListener(new ListChangeListener<CrgRuleTables>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends CrgRuleTables> c) {
                filterList = new FilteredList<>(makeItems(getSkinnable().getItems()), (t) -> {
                    return true; //To change body of generated lambdas, choose Tools | Templates.
                });
            }
        });

        masterDetail.getSelectedItemProperty().addListener(new ChangeListener<CrgRuleTablesItem>() {
            @Override
            public void changed(ObservableValue<? extends CrgRuleTablesItem> observable, CrgRuleTablesItem oldValue, CrgRuleTablesItem newValue) {
                updateDetail(newValue);
                getSkinnable().selectItem(newValue != null ? newValue.getTable() : null);
            }
        });
        getSkinnable().getProperties().addListener(new MapChangeListener<Object, Object>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
                if (change.wasAdded()) {
                    if (RuleTablesView.SAVE_SELECTED_ITEM.equals(change.getKey())) {
//                        saveDisplayedItem();
                        getSkinnable().getProperties().remove(RuleTablesView.SAVE_SELECTED_ITEM);
                    }
                }
            }
        });
//        getSkinnable().selectedItemProperty().addListener(new ChangeListener<CrgRuleTables>() {
//            @Override
//            public void changed(ObservableValue<? extends CrgRuleTables> observable, CrgRuleTables oldValue, CrgRuleTables newValue) {
//                LOG.info("select item in skinnable " + (newValue != null ? newValue.getCrgtTableName() : "null"));
//            }
//        });
//        getSkinnable().selectedItemProperty().addListener(new ChangeListener<CrgRuleTables>() {
//            @Override
//            public void changed(ObservableValue<? extends CrgRuleTables> observable, CrgRuleTables oldValue, CrgRuleTables newValue) {
//                if(!filterList.stream().anyMatch((CrgRuleTablesItem t) -> compareRuleTables(t.getTable(), newValue))){
//                    searchField.setText("");
//                }
//                selectRuleTables(newValue);
//            }
//        });
        selectRuleTables(getSkinnable().getSelectedItem());
    }
    public ListView<CrgRuleTablesItem> getListView(){
        return listView;
    }
    public boolean hasUnsaved() {
        if (listView == null) {
            return false;
        }
        for (CrgRuleTablesItem item : listView.getItems()) {
            if (item.isDirty()) {
                return true;
            }
        }
        return false;
    }

    public void saveAllChanges() {
        if (listView == null) {
            return;
        }
        for (CrgRuleTablesItem item : listView.getItems()) {
            if (item.isDirty()) {
                if (item.getDetail() != null) {
                    item.getDetail().saveTable();
                }

            }
        }
        RuleTableChangedEvent event = new RuleTableChangedEvent(RuleTableChangedEvent.ruleTableChangedEvent());
        Event.fireEvent(getSkinnable(), event);
    }

    public void discardAllChanges() {
        if (listView == null) {
            return;
        }
        for (CrgRuleTablesItem item : listView.getItems()) {
            if (item.isDirty()) {
                item.setDetail(null);
                item.discardAllChanges();
            }
        }
        CrgRuleTablesItem lastSelected = listView.getSelectionModel().getSelectedItem();
        updateDetail(lastSelected);
        listView.refresh();
    }

    private void updateDetail(CrgRuleTablesItem newValue) {
        if (newValue == null || newValue.getTable() == null) {
            masterDetail.setDetail(new Pane());
            getSkinnable().selectItem(null);
            return;
        }
//        listView.requestFocus();
        RuleTableDetail detail = getDetail(newValue);
        masterDetail.setDetail(detail);
    }

    private RuleTableDetail getDetail(CrgRuleTablesItem newValue) {
        if (newValue.getDetail() == null) {
            RuleTableDetail detail = new RuleTableDetail(newValue);
            getSkinnable().selectItem(newValue.getTable());
            detail.setPadding(new Insets(0, 0, 0, 5));
            newValue.setDetail(detail);
        }
        return newValue.getDetail();
    }

    private ObservableList<CrgRuleTablesItem> makeItems(List<CrgRuleTables> pList) {
        ArrayList<CrgRuleTablesItem> list = pList.stream().map((t) -> {
            return new CrgRuleTablesItem(t); //To change body of generated lambdas, choose Tools | Templates.
        }).collect(Collectors.toCollection(ArrayList::new));
        return FXCollections.observableArrayList(list);
    }

    private ObservableList<CrgRuleTablesItem> makeItems(ObservableList<CrgRuleTables> pList) {
        ArrayList<CrgRuleTablesItem> list = pList.stream().map((t) -> {
            return new CrgRuleTablesItem(t); //To change body of generated lambdas, choose Tools | Templates.
        }).collect(Collectors.toCollection(ArrayList::new));
        return FXCollections.observableArrayList(list);
    }

    //class to display a rule table as details
    protected class RuleTableDetail extends VBox {

        private final CrgRuleTables table;
        private ContentFlowPane flowPane;
        private final Node menuNode;
        private final CrgRuleTablesItem item;

        /**
         * creates new instance for item
         *
         * @param pItem item to create detai for
         */
        public RuleTableDetail(CrgRuleTablesItem pItem) {
            super();
//            long start = System.currentTimeMillis();
            item = pItem;
            table = pItem.getTable();
            if (table.getCrgtContent() == null) {
                table.setCrgtContent(RuleView.getFacade().getRuleTableContent(table));
            }
            if (table.getCrgtComment() == null) {
                table.setCrgtComment(RuleView.getFacade().getRuleTableComment(table));
            }
            pItem.stateManager.init(table);
            setSpacing(5.0);
            setFillWidth(true);
            Separator sep = new Separator(Orientation.HORIZONTAL);
            VBox sepBox = new VBox(/*label,*/sep);
            sepBox.setFillWidth(true);
            menuNode = createMenu();
            AsyncPane<Node> asyncPane = new AsyncPane<>() {
                @Override
                public Node loadContent() {
                    flowPane = new ContentFlowPane();//new RuleTableContentPane(RuleView.getFacade().getRuleTableContent(table),isEditable());
                    updateRuleTableValidation(table);
                    flowPane.setContent(table, isEditable());
                    flowPane.setValidationCallback(getSkinnable().getCodeSuggestionCallback());
                    flowPane.setOnContentChanged(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent t) {
                            RuleView.getFacade().getRuleTableContent(table);
                            String content = RuleTableDetail.this.getContent();
                            if (getSkinnable().getValidationCalllback() != null) {
                                table.setCrgtContent(content);
                                byte[] newMessage = getSkinnable().getValidationCalllback().call(table);
                                table.setCrgtMessage(newMessage);
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        updateRuleTableValidation(table);
                                        listView.refresh();
                                    }
                                });
                            }else{
                                table.setCrgtContent(content);
                                listView.refresh();
                            }
                        }
                    });
                    ScrollPane spItems = new ScrollPane(flowPane);
//                    spItems.setFitToHeight(true);
                    spItems.setFitToWidth(true);
                    return spItems;
                }
                
            };
            asyncPane.setPrefHeight(379.0d);
//            ScrollPane spItems = new ScrollPane(asyncPane);
//            spItems.setFitToHeight(true);
//            spItems.setFitToWidth(true);
//            spItems.setPrefHeight(379.0d);
//            asyncPane.prefWidthProperty().bind(Bindings.add(-5, spItems.widthProperty()));
//            asyncPane.prefHeightProperty().bind(Bindings.add(-5, spItems.heightProperty()));
            VBox.setVgrow(asyncPane, Priority.ALWAYS);
            getChildren().addAll(menuNode, sepBox, asyncPane);
            setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    requestFocus();
                }
            });
        }
        public void updateRuleTableValidation(CrgRuleTables pTable){
            updateRuleTableValidation(pTable!=null?pTable.getCrgtMessage():null);
        }
        public void updateRuleTableValidation(byte[] pMessage){
            if(flowPane == null){
                LOG.warning("can not update RuleValidation -> FlowPane is somehow null!");
                return;
            }
            try {
                flowPane.setValidationMessage(pMessage!=null?new RuleTableMessageReader().readUtf8AndGetCodes(pMessage):null);
                flowPane.refreshSeverityAsync();
            } catch (IOException ex) {
                Logger.getLogger(RuleTablesViewSkin1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        public boolean isEditable() {
            return Objects.equals(table.getCreationUser(), RuleView.getFacade().getCurrentUser());
//            return Objects.equals(table.getCreationUser(), getCurrentUser());//RuleView.getFacade().getCurrentUser();
        }

        private Node createMenu() {
            Node nameNode = createNameNode();
            Button btnAction = createActionButton();
            HBox.setHgrow(nameNode, Priority.ALWAYS);
            Button commentButton = createEditCommentButton();
            HBox menu = new HBox(5, nameNode, btnAction, commentButton);
            menu.setMinHeight(HBox.USE_PREF_SIZE);
            menu.setPrefHeight(29.0);
            menu.setMaxHeight(HBox.USE_PREF_SIZE);
            menu.setAlignment(Pos.CENTER_LEFT);
            return menu;
        }

        private Node createNameNode() {
            if (isEditable()) {
                TextField field = new TextField();
                field.textProperty().bindBidirectional(nameProperty());
                field.focusedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if (!newValue) {
//                            checkBeforeSave();
                            table.setCrgtTableName(field.getText());
                            item.getStateManager().checkDirty(table);
                            listView.refresh();
                        }
                    }
                });
                field.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        if (KeyCode.ENTER.equals(event.getCode())) {
//                            Platform.runLater(new Runnable() {
//                                @Override
//                                public void run() {
//                                    checkBeforeSave();
                            table.setCrgtTableName(field.getText());
//                                    item.getStateManager();
                            item.getStateManager().checkDirty(table);
                            listView.refresh();
//                                }
//                            });
                        }
                    }
                });
                field.setMaxWidth(Double.MAX_VALUE);
                return field;
            } else {
                Label label = new Label();
                label.textProperty().bind(nameProperty());
                label.setMaxWidth(Double.MAX_VALUE);
                return label;
            }
        }

        private StringProperty nameProperty;

        public StringProperty nameProperty() {
            if (nameProperty == null) {
                nameProperty = new SimpleStringProperty(table.getCrgtTableName());
            }
            return nameProperty;
        }

        public void setName(String pName) {
            nameProperty().set(pName);
        }

        public String getName() {
            return nameProperty().get();
        }

        private Button createActionButton() {
            Button btn = new Button();
            btn.getStyleClass().add("cpx-icon-button");
            if (isEditable()) {
                btn.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.SAVE));
                btn.setTooltip(new BasicTooltip("Speichern", "Speichert die Daten des Eintrags"));
                btn.setOnAction((ActionEvent event) -> {
                    saveTable();
                    byte[] msg = getSkinnable().getValidationCalllback().call(table); //get validation on save
                    table.setCrgtMessage(msg); //copied code - refactor to methode
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            updateRuleTableValidation(table);
                            byte[] msg = getSkinnable().getValidationCalllback().call(table); //get validation on save
                            listView.refresh();
                        }
                    });
                    RuleTableChangedEvent change = new RuleTableChangedEvent(RuleTableChangedEvent.ruleTableChangedEvent());
                    Event.fireEvent(getSkinnable(), change);
                });
            } else {
                btn.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.COPY));
                btn.setTooltip(new BasicTooltip("Kopieren", "Erstellt eine neue editierbare Kopie des Eintrags"));
                btn.setOnAction((ActionEvent event) -> {
                    copyTable();
                    RuleTableChangedEvent change = new RuleTableChangedEvent(RuleTableChangedEvent.ruleTableChangedEvent());
                    Event.fireEvent(getSkinnable(), change);
                });
            }
            btn.setMaxHeight(Double.MAX_VALUE);
            return btn;
        }
        
//        
//        private String getTableComment(){
//            if(table == null || table.getCrgtComment() == null){
//                return "";
//            }
//            return table.getCrgtComment();
//        }
//        
//        private void  setTableComment(String t){
//            if(table != null){
//                table.setCrgtComment(t);
//            }
//        }
        
        private Button createEditCommentButton() {
            EditCommentButton btnEditComment = new EditCommentButton(PopOver.ArrowLocation.LEFT_TOP);
            btnEditComment.setComment(table.getCrgtComment());
            btnEditComment.isEditable(isEditable());
            btnEditComment.setOnUpdateComment(new Callback<String, Boolean>(){ 
                @Override
                public Boolean call(String p) {
                    try{
                        table.setCrgtComment(p);
                    return true;
                    }catch(Exception ex){
                        LOG.log(Level.SEVERE, "Cannot save rule table content", ex);
                        return false;
                    }
                }
                
            });

            return btnEditComment;
        }

        private String getContent() {
            if (flowPane == null) {
                return "";
            }
            String content = flowPane.getChildren().stream().filter((Node t) -> t instanceof Item).map(Node::toString).collect(Collectors.joining(","));
            return content;
        }

        private void saveTable() {
            saveTable(getName(), getContent());
        }

        private void saveTable(String pName, String pContent) {
            table.setCrgtTableName(pName);
            table.setCrgtContent(pContent);
            item.getStateManager().save();
            RuleView.getFacade().updateRuleTable(table);
            listView.refresh();
//            Event.fireEvent(getSkinnable(), new ActionEvent(listView, this));
            Event.fireEvent(getSkinnable(), new RuleChangedEvent());
            LOG.info("name " + pName + " content " + table.getCrgtContent());
        }

        private void copyTable() {
            CrgRuleTables copy = RuleView.getFacade().createCopy(table);
            if (copy == null) {
                MainApp.showErrorMessageDialog("Fehler beim Anlegen der Kopie der Regeltabelle " + table.getCrgtTableName() + " die Kopie konnte nicht abgelegt werden");
                return;
            }
            CrgRulePools pool = RuleView.getFacade().getPool();
            RuleMetaDataCache.instance().addTableInPoolToTableMap(pool, copy);
            List<CrgRuleTables> tables = RuleMetaDataCache.instance().getTablesForPool(pool);//RuleView.getFacade().findRuleTables();//getAllRuleTables();
            tables.sort(Comparator.comparing(CrgRuleTables::getCrgtTableName));
            getSkinnable().getItems().setAll(tables);
            filterList = new FilteredList<>(makeItems(tables));
            listView.setItems(filterList);
            listView.refresh();
            selectRuleTables(copy);
        }

    }

    protected final void selectRuleTables(CrgRuleTables pTable) {
        if (pTable == null) {
            listView.getSelectionModel().select(null);
            return;
        }
        for (CrgRuleTablesItem item : listView.getItems()) {
            if (item.getTable().getCrgtTableName().equals(pTable.getCrgtTableName())) {
                listView.getSelectionModel().select(item);
                listView.scrollTo(item);
                break;
            }
        }
    }

    private boolean compareRuleTables(CrgRuleTables pTable1, CrgRuleTables pTable2) {
        if (pTable1 == null) {
            return false;
        }
        if (pTable2 == null) {
            return false;
        }
        if (!pTable1.getCrgtTableName().equals(pTable2.getCrgtTableName())) {
            return false;
        }
        if (pTable1.getCrgtContent() == null) {
            return false;
        }
        if (pTable2.getCrgtContent() == null) {
            return false;
        }
        if (!pTable1.getCrgtContent().equals(pTable2.getCrgtContent())) {
            return false;
        }
        return true;
    }

    private class ContentFlowPane extends RuleTableContentPane/*RuleTableSearchPane*/ {
        private ContentFlowPane() {
            super();
            setItemFactory(new Callback<RuleTableContentParam, Item>() {
                @Override
                public Item call(RuleTableContentParam param) {
                    Item item = new Item(param.getText(), param.isEditable());
                    item.setEditorFactory(new Callback<String, Control>() {
                        @Override
                        public Control call(String param) {
                            CriterionTree.Supergroup.Group.Criterion criterion = getSkinnable().getCriterion();
                            //TODO: detect what kind of criterion is displayed and show values for this?
                            if (CriteriaHelper.isEnum(criterion)) {
                                return createEnumComboBox(item, criterion.getTooltip());
                            }
                            return null;
                        }
                    });
                    item.setTooltipFactory(new Callback<String, Tooltip>() {
                        @Override
                        public Tooltip call(String param) {
                            if (CriteriaHelper.isEnum(getSkinnable().getCriterion())) {
                                String content = CriteriaHelper.getTooltipValue(param, getSkinnable().getCriterion());
                                if (content != null) {
                                    return new BasicTooltip(getSkinnable().getCriterion().getCpname(), content);
                                }
                            }
                            return null;
                        }
                    });
                    return item;
                }
            });
        }

        public void setContent(CrgRuleTables table, boolean pEditable) {
            super.setContent(RuleView.getFacade().getRuleTableContent(table), pEditable); //To change body of generated methods, choose Tools | Templates.
        }
        
        private Control createEnumComboBox(Item pItem, List<CriterionTree.Supergroup.Group.Criterion.Tooltip> tooltip) {
            ComboBox<CriterionTree.Supergroup.Group.Criterion.Tooltip> cb = new ComboBox<>(FXCollections.observableArrayList(tooltip));
            cb.setId("field-edit");
            cb.setConverter(new StringConverter<CriterionTree.Supergroup.Group.Criterion.Tooltip>() {
                @Override
                public String toString(CriterionTree.Supergroup.Group.Criterion.Tooltip object) {
                    return object == null ? "" : CriteriaHelper.getTooltipDescription(object);
                }

                @Override
                public CriterionTree.Supergroup.Group.Criterion.Tooltip fromString(String string) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
            //select value by old value
            if (!pItem.getText().isEmpty()) {
                for (CriterionTree.Supergroup.Group.Criterion.Tooltip tip : tooltip) {
                    if (tip.getValue().equals(pItem.getText())) {
                        cb.getSelectionModel().select(tip);
                        break;
                    }
                }
            }
            cb.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CriterionTree.Supergroup.Group.Criterion.Tooltip>() {
                @Override
                public void changed(ObservableValue<? extends CriterionTree.Supergroup.Group.Criterion.Tooltip> observable, CriterionTree.Supergroup.Group.Criterion.Tooltip oldValue, CriterionTree.Supergroup.Group.Criterion.Tooltip newValue) {
                    if (newValue == null) {
                        pItem.setText("");
                        return;
                    }
                    pItem.setText(newValue.getValue());
                }
            });
            cb.getProperties().addListener(new MapChangeListener<Object, Object>() {
                @Override
                public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
                    if (change.wasAdded()) {
                        if (Item.SAVE_DATA.equals(change.getKey())) {
                            String selected = cb.getSelectionModel().getSelectedItem().getValue();
                            String text = selected != null ? selected : "";
                            pItem.saveText(text);
                            cb.getProperties().remove(Item.SAVE_DATA);
                        }
                    }
                }
            });
            cb.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue) {
                        pItem.setEditbale(false);
                        if (cb.getSelectionModel().getSelectedItem() == null) {
                            if (pItem.getOnDeleteRequested() != null) {
                                pItem.getOnDeleteRequested().handle(new ActionEvent());
                            }
                        } else {
                            pItem.saveText(cb.getSelectionModel().getSelectedItem().getValue());
                        }
                    }
                }
            });
            cb.setMaxWidth(150);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    cb.getSelectionModel().selectFirst();
                    cb.requestFocus();
                }
            });
            return cb;
        }

    }

    protected class CrgRuleTablesItem {

        private CrgRuleTables table;
        private final CrgRuleTableItemStateManager stateManager;
        private RuleTableDetail detail;

        public CrgRuleTablesItem(CrgRuleTables pTable) {
            this.table = pTable;
            stateManager = new CrgRuleTableItemStateManager();
//            stateManager.init(pTable);
        }

        public CrgRuleTables getTable() {
            return table;
        }

        public StateManager<CrgRuleTables> getStateManager() {
            return stateManager;
        }

        public boolean isDirty() {
            if (getTable() == null || getTable().getCrgtContent() == null) {
                return false;
            }
            if (!stateManager.isInitialized()) {
                return false;
            }
            return !stateManager.check();
        }

        private void setDetail(RuleTableDetail pDetail) {
            detail = pDetail;
        }

        public RuleTableDetail getDetail() {
            return detail;
        }

        public void discardAllChanges() {
            getStateManager().revertToOriginalState();
            table = getStateManager().getCurrentStateItem();
        }

        private class CrgRuleTableItemStateManager extends StateManager<CrgRuleTables> {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean compare(CrgRuleTables pItem1, CrgRuleTables pItem2) throws IOException {
                return compareRuleTables(pItem1, pItem2);
//                if (pItem1 == null) {
//                    return false;
//                }
//                if (pItem2 == null) {
//                    return false;
//                }
//                if (!pItem1.getCrgtTableName().equals(pItem2.getCrgtTableName())) {
//                    return false;
//                }
////                if(!pItem1.getCrgtContent().equals(pItem2.getCrgtContent())){
////                    return false;
////                }
//                return true;
            }

        }
    }
    private static final Logger LOG = Logger.getLogger(RuleTablesViewSkin1.class.getName());

}
