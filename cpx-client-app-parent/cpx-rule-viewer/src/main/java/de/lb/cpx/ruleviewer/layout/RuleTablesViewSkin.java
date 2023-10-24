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

import de.lb.cpx.client.core.model.fx.masterdetail.ListViewMasterDetailPane;
import de.lb.cpx.client.core.model.fx.pane.AsyncPane;
import de.lb.cpx.client.core.model.fx.tooltip.BasicTooltip;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.rule.criteria.CriteriaHelper;
import de.lb.cpx.rule.criteria.model.CriterionTree;
import de.lb.cpx.ruleviewer.event.RuleChangedEvent;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * Skin class to show Content of the ruletables
 *
 * @author wilde
 */
public class RuleTablesViewSkin extends SkinBase<RuleTablesView> {

    private static final Logger LOG = Logger.getLogger(RuleTablesViewSkin.class.getName());

    private final ListViewMasterDetailPane<CrgRuleTables> masterDetail;
    private final ListView<CrgRuleTables> listView;
    private final TextField searchField;
    private FilteredList<CrgRuleTables> filterList;

    public RuleTablesViewSkin(RuleTablesView pSkinnable) {
        super(pSkinnable);
        getSkinnable().setPadding(new Insets(8));
        masterDetail = new ListViewMasterDetailPane<>();
        listView = new ListView<>();
        listView.getStyleClass().add("stay-selected-list-view");
        filterList = new FilteredList<>(pSkinnable.getItems());
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
                    filterList.setPredicate((CrgRuleTables t) -> true);
                } else {
                    filterList.setPredicate((CrgRuleTables t) -> t.getCrgtTableName().toUpperCase().contains(newValue.toUpperCase()));
                }
            }
        });
        masterDetail.setMasterDetailRatio(2.0);
        getChildren().add(masterDetail);
        masterDetail.setCellFactory(new Callback<ListView<CrgRuleTables>, ListCell<CrgRuleTables>>() {
            @Override
            public ListCell<CrgRuleTables> call(ListView<CrgRuleTables> param) {
                return new ListCell<CrgRuleTables>() {
                    @Override
                    protected void updateItem(CrgRuleTables item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        if (item == null || empty) {
                            setGraphic(null);
                            return;
                        }
                        Label lbl = new Label(item.getCrgtTableName());
                        HBox box = new HBox(5, lbl);
                        HBox.setHgrow(lbl, Priority.ALWAYS);
                        box.setAlignment(Pos.CENTER_LEFT);
                        lbl.setMaxWidth(Double.MAX_VALUE);
                        setGraphic(box);
                        Label icon = new Label();
                        icon.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.EYE));
                        box.getChildren().add(0, icon);
                        icon.setVisible(!Objects.equals(item.getCreationUser(), RuleView.getFacade().getCurrentUser()));
                    }
                };
            }
        });
        pSkinnable.getItems().addListener(new ListChangeListener<CrgRuleTables>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends CrgRuleTables> c) {
                filterList = new FilteredList<>(getSkinnable().getItems(), (t) -> {
                    return true; //To change body of generated lambdas, choose Tools | Templates.
                });
            }
        });

        masterDetail.getSelectedItemProperty().addListener(new ChangeListener<CrgRuleTables>() {
            @Override
            public void changed(ObservableValue<? extends CrgRuleTables> observable, CrgRuleTables oldValue, CrgRuleTables newValue) {
                if (newValue == null) {
                    masterDetail.setDetail(new Pane());
                    getSkinnable().selectItem(null);
                    return;
                }
                listView.requestFocus();
                saveDisplayedItem();
                RuleTableDetail detail = new RuleTableDetail(newValue);
                getSkinnable().selectItem(newValue);
                detail.setPadding(new Insets(0, 0, 0, 5));
                masterDetail.setDetail(detail);

            }
        });
        getSkinnable().getProperties().addListener(new MapChangeListener<Object, Object>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
                if (change.wasAdded()) {
                    if (RuleTablesView.SAVE_SELECTED_ITEM.equals(change.getKey())) {
                        saveDisplayedItem();
                        getSkinnable().getProperties().remove(RuleTablesView.SAVE_SELECTED_ITEM);
                    }
                }
            }
        });
        getSkinnable().selectedItemProperty().addListener(new ChangeListener<CrgRuleTables>() {
            @Override
            public void changed(ObservableValue<? extends CrgRuleTables> observable, CrgRuleTables oldValue, CrgRuleTables newValue) {
                if (!filterList.contains(newValue)) {
                    searchField.setText("");
                }
                selectRuleTables(newValue);
            }
        });
        selectRuleTables(getSkinnable().getSelectedItem());
    }

    private void saveDisplayedItem() {
        Node node = masterDetail.getDetail();
        if (node instanceof RuleTableDetail) {
            ((RuleTableDetail) node).checkBeforeSave();
        }
    }

    private void selectRuleTables(CrgRuleTables pTable) {
        if (pTable == null) {
            listView.getSelectionModel().select(null);
            return;
        }
        for (CrgRuleTables item : listView.getItems()) {
            if (item.getCrgtTableName().equals(pTable.getCrgtTableName())) {
                listView.getSelectionModel().select(item);
                break;
            }
        }
    }

    protected class RuleTableDetail extends VBox {

        private final CrgRuleTables table;
        private FlowPane flowPane;
        private final Node menuNode;
        private StringProperty nameProperty;

        public RuleTableDetail(CrgRuleTables pTable) {
            super();
            long start = System.currentTimeMillis();
            table = pTable;
            setSpacing(5.0);
            setFillWidth(true);
            Separator sep = new Separator(Orientation.HORIZONTAL);
            VBox sepBox = new VBox(/*label,*/sep);
            sepBox.setFillWidth(true);
            menuNode = createMenu();
            AsyncPane<FlowPane> asyncPane = new AsyncPane<>() {
                @Override
                public FlowPane loadContent() {
                    flowPane = new RuleTableFlowPane(pTable.getCrgtContent(), isEditable());
                    return flowPane;
                }
            };
            ScrollPane spItems = new ScrollPane(asyncPane);
            spItems.setFitToHeight(true);
            spItems.setFitToWidth(true);
            VBox.setVgrow(spItems, Priority.ALWAYS);
            getChildren().addAll(menuNode, sepBox, spItems);
            setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    requestFocus();
                }
            });
        }

        public boolean isEditable() {
            return Objects.equals(table.getCreationUser(), RuleView.getFacade().getCurrentUser());
//            return Objects.equals(table.getCreationUser(), getCurrentUser());//RuleView.getFacade().getCurrentUser();
        }

        private Node createMenu() {
            Node nameNode = createNameNode();
            Button btnAction = createActionButton();
            HBox.setHgrow(nameNode, Priority.ALWAYS);
            HBox menu = new HBox(5, nameNode, btnAction);
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
                            checkBeforeSave();
                        }
                    }
                });
                field.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        if (KeyCode.ENTER.equals(event.getCode())) {
                            checkBeforeSave();
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
                });
            } else {
                btn.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.COPY));
                btn.setTooltip(new BasicTooltip("Kopieren", "Erstellt eine neue editierbare Kopie des Eintrags"));
                btn.setOnAction((ActionEvent event) -> {
                    copyTable();
                });
            }
            btn.setMaxHeight(Double.MAX_VALUE);
            return btn;
        }

        private String getContent() {
            if (flowPane == null) {
                return "";
            }
            return flowPane.getChildren().stream().filter((Node t) -> t instanceof Item).map(Node::toString).collect(Collectors.joining(","));
        }

        private void saveTable() {
            saveTable(getName(), getContent());
        }

        private void saveTable(String pName, String pContent) {
            table.setCrgtTableName(pName);
            table.setCrgtContent(pContent);
            RuleView.getFacade().updateRuleTable(table);
            listView.refresh();
//            Event.fireEvent(getSkinnable(), new ActionEvent(listView, this));
            Event.fireEvent(getSkinnable(), new RuleChangedEvent());
            LOG.info("name " + pName + " content " + table.getCrgtContent());
        }

        private void checkBeforeSave() {
            if (!isEditable()) {
                return;
            }
            String name = getName() != null ? getName() : "";
            String content = getContent();
            if (!name.equals(table.getCrgtTableName()) || !content.equals(table.getCrgtContent())) {
                //check here, and show some dialog, because the user have not saved if he wants to discard changes
                saveTable(name, content);
            }
        }

        private void copyTable() {
            CrgRuleTables copy = RuleView.getFacade().createCopy(table);
            List<CrgRuleTables> tables = RuleView.getFacade().findRuleTables();//getAllRuleTables();
            getSkinnable().getItems().setAll(tables);
            filterList = new FilteredList<>(FXCollections.observableList(tables));
//            CrgRuleTables copy = getSkinnable().copyTable(table);//RuleView.getFacade().createCopy(table);
//            getSkinnable().getItems().setAll(getSkinnable().getAllRuleTables());//RuleView.getFacade().getAllRuleTables());
//            filterList = new FilteredList<>(FXCollections.observableList(getSkinnable().getAllRuleTables()));//RuleView.getFacade().getAllRuleTables()));
            listView.setItems(filterList);
            listView.refresh();
            selectRuleTables(copy);
//            listView.getSelectionModel().select(copy);
        }
    }

    protected class RuleTableFlowPane extends FlowPane {

        protected static final String BORDER_STYLE = "-fx-border-color:lightgrey";
        private final Logger logger = Logger.getLogger(RuleTableFlowPane.class.getName());

        public RuleTableFlowPane(String pContent, boolean pEditable) {
            super();
            setVgap(10);
            setHgap(10);
            if (pEditable) {
                getChildren().add(createAddButton());
            }
            addItems(pContent, pEditable);
            setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    requestFocus();
                }
            });
        }

        public final Button createAddButton() {
            Button btn = new Button("Hinzufügen");
            btn.setMaxHeight(Double.MAX_VALUE);
            btn.getStyleClass().add("cpx-icon-button");
            btn.setStyle(BORDER_STYLE);
            btn.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.PLUS_CIRCLE).color(Color.GREEN));
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Item item = createItem("", true);
                    item.setEditbale(true);
                    getChildren().add(1, item);
                }
            });
            return btn;
        }

        private Item createItem(String pText, boolean pEditable) {
            Item item = new Item(pText, pEditable);
            item.setOnDeleteRequested(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            getChildren().remove(item);
                        }
                    });
                    event.consume();
                }
            });
            item.setMultiInputCallback(new Callback<String, Void>() {
                @Override
                public Void call(String param) {
                    addItems(param, pEditable);
                    return null;
                }
            });
            return item;
        }

        //error prone, regrex would be a lot smarter if content has mixed separators
        private String[] splitContent(String pContent) {
            if (pContent.contains(" ")) {
                return pContent.split(" ");
            }
            if (pContent.contains(",")) {
                return pContent.split(",");
            }
            if (pContent.contains("\n")) {
                return pContent.split("\n");
            }
            return new String[0];
        }

        private void addItems(String content, boolean pEditable) {
            List<Item> items = new ArrayList<>();
            for (String text : splitContent(content)) {
                items.add(createItem(text, pEditable));
            }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    long start = System.currentTimeMillis();
                    getChildren().addAll(items);
                    logger.info("add all items in " + (System.currentTimeMillis() - start) + " ms");
                }
            });
        }
    }

    protected class Item extends HBox {

        protected static final String BORDER_STYLE = "-fx-border-color:lightgrey";
        private static final String SAVE_DATA = "save.data";
        private EventHandler<ActionEvent> onDeleteRequested;
        private Callback<String, Void> multiInputCallback;
        private BooleanProperty editableProperty;
        private StringProperty textProperty;

        public Item(String pText, Boolean pEditable) {
            super();
            setStyle(BORDER_STYLE);
            setAlignment(Pos.CENTER_LEFT);
            setText(pText);
            setPadding(new Insets(5));
            setSpacing(5);
            if (pEditable) {
                setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        getChildren().get(0).getProperties().put(SAVE_DATA, null);
                        setEditbale(!isEditable());
                    }
                });
            }
            editableProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    updateUi(pEditable);
                }
            });
            updateUi(pEditable);
        }

        @Override
        public String toString() {
            return getText();
        }

        private void updateUi(boolean pEditable) {
            getChildren().clear();
            Node text = getTextNode();
            HBox.setHgrow(text, Priority.ALWAYS);
            getChildren().add(text);
            if (pEditable) {
                Button delete = new Button();
                delete.getStyleClass().add("cpx-icon-button");
                delete.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.CLOSE));
                getChildren().add(delete);
                delete.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (getOnDeleteRequested() != null) {
                            getOnDeleteRequested().handle(event);
                        }
                    }
                });
            }
        }

        public EventHandler<ActionEvent> getOnDeleteRequested() {
            return onDeleteRequested;
        }

        public void setOnDeleteRequested(EventHandler<ActionEvent> onDelete) {
            onDeleteRequested = onDelete;
        }

        public Callback<String, Void> getMultiInputCallback() {
            return multiInputCallback;
        }

        public void setMultiInputCallback(Callback<String, Void> pCallback) {
            multiInputCallback = pCallback;
        }

        private boolean isMultiInput(String pText) {
            return pText.contains(",") || pText.contains(" ") || pText.contains("\n");
        }

        private void saveText(String pText) {
            pText = pText.replace("''", " ").replace("'", "");
            if (isMultiInput(pText)) {
                if (getMultiInputCallback() != null) {
                    getMultiInputCallback().call(pText);
                    setText("");
                    if (getOnDeleteRequested() != null) {
                        getOnDeleteRequested().handle(new ActionEvent());
                    }
                }
            } else {
                setText(pText);
            }
        }

        private Node getTextNode() {
            if (isEditable()) {
                return createEditControl();
            } else {
                Text label = new Text();
                label.textProperty().bind(textProperty());
                if (CriteriaHelper.isEnum(getSkinnable().getCriterion())) {
                    String content = CriteriaHelper.getTooltipValue(label.getText(), getSkinnable().getCriterion());
                    if (content != null) {
                        Tooltip.install(Item.this, new BasicTooltip(getSkinnable().getCriterion().getCpname(), content));
                    }
                }
                return label;
            }
        }

        public final BooleanProperty editableProperty() {
            if (editableProperty == null) {
                editableProperty = new SimpleBooleanProperty(false);
            }
            return editableProperty;
        }

        public boolean isEditable() {
            return editableProperty().get();
        }

        public void setEditbale(Boolean pEditable) {
            editableProperty().set(pEditable);
        }

        public StringProperty textProperty() {
            if (textProperty == null) {
                textProperty = new SimpleStringProperty();
            }
            return textProperty;
        }

        public final void setText(String pText) {
            textProperty().set(pText);
        }

        public String getText() {
            return textProperty().get();
        }

        private Node createEditControl() {
            CriterionTree.Supergroup.Group.Criterion criterion = getSkinnable().getCriterion();
            //TODO: detect what kind of criterion is displayed and show values for this?
            if (CriteriaHelper.isEnum(criterion)) {
                return createEnumComboBox(criterion.getTooltip());
            }
            return createEditTextField();
        }

        private TextField createEditTextField() {
            TextField field = new TextField(getText());
            field.setId("field-edit");
            field.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue) {
                        setEditbale(false);
                        if (field.getText().isEmpty()) {
                            if (getOnDeleteRequested() != null) {
                                getOnDeleteRequested().handle(new ActionEvent());
                            }
                        } else {
                            saveText(field.getText());
                        }
                    }
                }
            });
            field.getProperties().addListener(new MapChangeListener<Object, Object>() {
                @Override
                public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
                    if (change.wasAdded()) {
                        if (SAVE_DATA.equals(change.getKey())) {
                            String text = field.getText();
                            saveText(text);
                            field.getProperties().remove(SAVE_DATA);
                        }
                    }
                }
            });
            field.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (KeyCode.ENTER.equals(event.getCode())) {
                        setEditbale(false);
                        String text = field.getText();
                        saveText(text);
                        event.consume();
                    }
                    if (KeyCode.ESCAPE.equals(event.getCode())) {
                        setEditbale(false);
                        event.consume();
                    }
                }
            });
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    field.selectAll();
                    field.requestFocus();
                }
            });
            return field;
        }

        private Node createEnumComboBox(List<CriterionTree.Supergroup.Group.Criterion.Tooltip> tooltip) {
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
            if (!getText().isEmpty()) {
                for (CriterionTree.Supergroup.Group.Criterion.Tooltip tip : tooltip) {
                    if (tip.getValue().equals(getText())) {
                        cb.getSelectionModel().select(tip);
                        break;
                    }
                }
            }
            cb.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CriterionTree.Supergroup.Group.Criterion.Tooltip>() {
                @Override
                public void changed(ObservableValue<? extends CriterionTree.Supergroup.Group.Criterion.Tooltip> observable, CriterionTree.Supergroup.Group.Criterion.Tooltip oldValue, CriterionTree.Supergroup.Group.Criterion.Tooltip newValue) {
                    if (newValue == null) {
                        setText("");
                        return;
                    }
                    setText(newValue.getValue());
                }
            });
            cb.getProperties().addListener(new MapChangeListener<Object, Object>() {
                @Override
                public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
                    if (change.wasAdded()) {
                        if (SAVE_DATA.equals(change.getKey())) {
                            String selected = cb.getSelectionModel().getSelectedItem().getValue();
                            String text = selected != null ? selected : "";
                            saveText(text);
                            cb.getProperties().remove(SAVE_DATA);
                        }
                    }
                }
            });
            cb.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue) {
                        setEditbale(false);
                        if (cb.getSelectionModel().getSelectedItem() == null) {
                            if (getOnDeleteRequested() != null) {
                                getOnDeleteRequested().handle(new ActionEvent());
                            }
                        } else {
                            saveText(cb.getSelectionModel().getSelectedItem().getValue());
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
}
