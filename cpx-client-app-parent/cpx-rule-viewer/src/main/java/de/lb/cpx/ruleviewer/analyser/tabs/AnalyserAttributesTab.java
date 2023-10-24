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
package de.lb.cpx.ruleviewer.analyser.tabs;

import de.lb.cpx.client.core.model.fx.button.SearchToggleButton;
import de.lb.cpx.client.core.model.fx.section.SectionHeader;
import de.lb.cpx.model.TCase;
import de.lb.cpx.ruleviewer.analyser.attributes.AnalyserAttribute;
import de.lb.cpx.ruleviewer.analyser.attributes.AnalyserAttributes;
import de.lb.cpx.ruleviewer.analyser.attributes.AnalyserBeanProperty;
import de.lb.cpx.ruleviewer.analyser.editors.CaseChangedEvent;
import de.lb.cpx.ruleviewer.analyser.model.LabeledNode;
import de.lb.cpx.ruleviewer.analyser.model.RuleAnalyserItem;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.DefaultPropertyEditorFactory;
import org.controlsfx.property.editor.PropertyEditor;

/**
 * Attributes tab
 *
 * @author wilde
 * @param <T> type
 */
public class AnalyserAttributesTab<T extends AnalyserAttributes> extends Tab {

    private FilteredList<RuleAnalyserItem> filterList;
    protected final T attributes;
    private FlowPane flowPane;
//    private final TabHeaderBox header;
    private final TextField searchTextField;
    private final VBox container;
    protected AttributeMenu menu;

    public AnalyserAttributesTab(String pTitle, T pAttributes) {
        super(pTitle);
//        header = new TabHeaderBox(pTitle);
//        setGraphic(header);

        getStyleClass().add("analyser-tab");
        attributes = pAttributes;
        if (attributes == null) {
            setDisable(true);
        }
        setClosable(false);

        filterList = new FilteredList<>(FXCollections.observableArrayList());
        searchTextField = new TextField();
        searchTextField.textProperty().bindBidirectional(searchTextProperty);
        searchTextField.setPromptText("Suchen");

        flowPane = new FlowPane();
        flowPane.setPadding(new Insets(5, 0, 5, 0));
        flowPane.setHgap(5);
        flowPane.setVgap(5);

        caseProperty().addListener(new ChangeListener<TCase>() {
            @Override
            public void changed(ObservableValue<? extends TCase> observable, TCase oldValue, TCase newValue) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        flowPane.getChildren().clear();
                        filterList = new FilteredList<>(FXCollections.observableArrayList(getDataList(newValue)));
                        searchItems(getSearchText());
                        flowPane.getChildren().addAll(filterList);
//                        flowPane.getChildren().addAll(getDataList(newValue));
                    }
                });
            }
        });
        container = new VBox(0, flowPane);
        container.setPadding(new Insets(5, 0, 0, 0));
        container.setFillWidth(true);
        VBox.setVgrow(flowPane, Priority.ALWAYS);
        showSearchFieldProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    if (!container.getChildren().contains(searchTextField)) {
                        container.getChildren().add(0, searchTextField);
                    }
                } else {
                    if (container.getChildren().contains(searchTextField)) {
                        container.getChildren().remove(searchTextField);
                        setSearchText("");
                    }
                }
            }
        });
        searchTextProperty.addListener(new ChangeListener<java.lang.String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                flowPane.getChildren().clear();
                searchItems(newValue);
                flowPane.getChildren().addAll(filterList);
            }
        });
        ScrollPane scrollPane = new ScrollPane(container);
//        flowPane.setOrientation(Orientation.VERTICAL);
        scrollPane.setFitToWidth(true);
//        scrollPane.setFitToHeight(true);
        setContent(scrollPane);

//        getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Node>() {
//            @Override
//            public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
//                if(oldValue != null){
//                    oldValue.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected_node"), false);
//                }
//                if(newValue !=null){
//                    newValue.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected_node"), true);
//                }
//            }
//        });
    }

    public T getAttributes() {
        return attributes;
    }

    public boolean removeAnalyserItem(RuleAnalyserItem pItem) {
        if (pItem.getParentItem() == null) {
            return flowPane.getChildren().remove(pItem);
        } else {
            pItem.getParentItem().getItems().remove(pItem);
            if (pItem.getParentItem().getItems().isEmpty()) {
                pItem.getParentItem().getOnDeleteCallback().call(null);
            }
            return true;
        }
    }
    private Callback<PropertySheet.Item, PropertyEditor<?>> editorFactory = new DefaultPropertyEditorFactory();

    public Callback<PropertySheet.Item, PropertyEditor<?>> getEditorFactory() {
        return editorFactory;
    }

    public void setEditorFactorty(Callback<PropertySheet.Item, PropertyEditor<?>> pFactory) {
        editorFactory = pFactory;
    }

    private ObjectProperty<TCase> caseProperty;

    public final ObjectProperty<TCase> caseProperty() {
        if (caseProperty == null) {
            caseProperty = new SimpleObjectProperty<>();
        }
        return caseProperty;
    }

    public void setCase(TCase pCase) {
        caseProperty().set(pCase);
    }

    public TCase getCase() {
        return caseProperty.get();
    }

//    private ObjectProperty<SingleSelectionModel<Node>> selectionModelProperty;
//    public ObjectProperty<SingleSelectionModel<Node>> selectionModelProperty(){
//        if(selectionModelProperty == null){
//            selectionModelProperty = new SimpleObjectProperty<>(new SingleSelectionModel<Node>() {
//                @Override
//                protected Node getModelItem(int index) {
//                    return flowPane.getChildren().get(index);
//                }
//
//                @Override
//                protected int getItemCount() {
//                    return flowPane.getChildren().size();
//                }
//            });
//        }
//        return selectionModelProperty;
//    }
//    public SelectionModel<Node> getSelectionModel(){
//        return selectionModelProperty().get();
//    }
    private Callback<AnalyserAttribute, Object> beanFactory = new Callback<AnalyserAttribute, Object>() {
        @Override
        public Object call(AnalyserAttribute param) {
            return getCase();
        }
    };

    public final void setBeanFactory(Callback<AnalyserAttribute, Object> pFactory) {
        beanFactory = pFactory;
    }

    public Callback<AnalyserAttribute, Object> getBeanFactory() {
        return beanFactory;
    }

    public Object getBean(AnalyserAttribute pAttribute) {
        return beanFactory.call(pAttribute);
    }

    private final StringProperty searchTextProperty = new SimpleStringProperty("");

    public void setSearchText(String pText) {
        searchTextProperty.set(pText);
    }

    public String getSearchText() {
        return searchTextProperty.get();
    }
    private BooleanProperty showSearchFieldProeprty = new SimpleBooleanProperty(false);

    public final BooleanProperty showSearchFieldProperty() {
        return showSearchFieldProeprty;
    }

    public Boolean isShowSearchField() {
        return showSearchFieldProperty().get();
    }

    public void setShowSearchField(boolean pShow) {
        showSearchFieldProperty().set(pShow);
    }
    private Callback<AnalyserAttribute, RuleAnalyserItem> ruleAnalyserItemFactory = new Callback<AnalyserAttribute, RuleAnalyserItem>() {
        @Override
        public RuleAnalyserItem call(AnalyserAttribute param) {
            AnalyserBeanProperty beanProp = new AnalyserBeanProperty(getBean(param), param);
            Object value = beanProp.getValue();//getValue(en, pFall);
            //skip if no value is set
            if (value == null) {
                return null;
            }
            RuleAnalyserItem item = new RuleAnalyserItem();
            item.setTitle(param.getDisplayName());
            item.setBeanProperty(beanProp);
            item.setOnDeleteCallback(new Callback<Void, Boolean>() {
                @Override
                public Boolean call(Void param) {
                    flowPane.getChildren().remove(item);
                    beanProp.setValue(null);
                    CaseChangedEvent event = new CaseChangedEvent();
                    Event.fireEvent(getContent(), event);
//                        tvTestResult.refresh();
                    return true;
                }
            });
            return item;
        }
    };

    public final void setRuleAnalyserItemFactory(Callback<AnalyserAttribute, RuleAnalyserItem> pFactory) {
        ruleAnalyserItemFactory = pFactory;
    }

    public Callback<AnalyserAttribute, RuleAnalyserItem> getRuleAnalyserItemFactory() {
        return ruleAnalyserItemFactory;
    }

    public AttributeMenu getMenu() {
//        if(menu == null){
        menu = new SingleAttributeMenu(getText());
//        }
        return menu;
    }

    protected List<RuleAnalyserItem> getDataList(TCase pFall) {
        List<RuleAnalyserItem> items = new ArrayList<>();
        if (pFall == null) {
            return items;
        }
        List<AnalyserAttribute> sorted = attributes.getSortedAttributes();
//        sorted.sort(Comparator.comparing(AnalyserAttribute::getDisplayName));
        for (AnalyserAttribute att : sorted) {
            RuleAnalyserItem item = getRuleAnalyserItemFactory().call(att);//createAnalyserItem(att, pFall,getBeanFactory().call(att), flowPane);
            if (item != null) {
//                item.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//                    @Override
//                    public void handle(MouseEvent event) {
//                        if (MouseButton.PRIMARY.equals(event.getButton())) {
//                            getSelectionModel().select(item);
//                        }
//                    }
//                });
                item.getEditor().addEventFilter(CaseChangedEvent.caseChangedEvent(), new EventHandler<CaseChangedEvent>() {
                    @Override
                    public void handle(CaseChangedEvent t) {
                        Event.fireEvent(getContent(), t);
                    }
                });
                item.setMaxWidth(500.0d);
                items.add(item);
            }
        }
        return items;
    }

    private void searchItems(String pText) {
        filterList.setPredicate(pText.isEmpty() ? new Predicate<RuleAnalyserItem>() {
            @Override
            public boolean test(RuleAnalyserItem t) {
                return true;
            }
        } : new Predicate<RuleAnalyserItem>() {
            @Override
            public boolean test(RuleAnalyserItem t) {
                if (t.getTitle() == null) {
                    return false;
                }
                if (t.getText() == null) {
                    return false;
                }
                return checkAnalyserItem(t, pText);
            }

            private boolean checkAnalyserItem(RuleAnalyserItem pItem, String pText) {
                if (pItem.getItems().isEmpty()) {
                    return pItem.getTitle().toLowerCase().contains(pText.toLowerCase()) || pItem.getText().toLowerCase().contains(pText.toLowerCase());
                }
                return checkAnalyserItem(pItem.getItems().iterator().next(), pText);
//                for (RuleAnalyserItem item : pItem.getItems()) {
//                    return checkAnalyserItem(item, pText);
//                }
//                return false;
            }
        });
    }

    public void reload() {
        flowPane.getChildren().clear();
        filterList = new FilteredList<>(FXCollections.observableArrayList(getDataList(getCase())));
        searchItems(getSearchText());
        flowPane.getChildren().addAll(filterList);
//        if(getSelectionModel().getSelectedItem() instanceof RuleAnalyserItem){
//            getSelectionModel().select(getSelectedAnalyserItem((RuleAnalyserItem) getSelectionModel().getSelectedItem(),filterList));
//        }
        if (menu != null) {
            menu.reload();
        }
    }

//    private Node getSelectedAnalyserItem(RuleAnalyserItem pItem, FilteredList<RuleAnalyserItem> pItems) {
//        for (RuleAnalyserItem item : pItems) {
//            if (item.getTitle().equals(pItem.getTitle())) {
////                if(item.getBeanProperty().getBean().equals(pItem.getBeanProperty().getBean())){
//                return item;
////                }
//            }
//        }
//        return null;
//    }
    public class SingleAttributeMenu extends AttributeMenu {

        public SingleAttributeMenu(String pTitle) {
            super(pTitle);
        }

        @Override
        public final Node createContent(String pText) {
            FlowPane pane = new FlowPane();
            pane.setHgap(5.0);
            pane.setVgap(5.0);

            List<Node> items = new ArrayList<>();
            for (AnalyserAttribute att : attributes.getSortedAttributes()) {
                AnalyserBeanProperty beanProp = new AnalyserBeanProperty(getBean(att), att);
                LabeledNode labeled = new LabeledNode(att.getDisplayName(), beanProp);
                if (pText.isEmpty()) {
                    items.add(labeled);
                } else {
                    if (labeled.getTitle().toLowerCase().contains(pText.toLowerCase())) {
                        items.add(labeled);
                    }
                }
            }
            pane.getChildren().addAll(items);
            return pane;
        }
    }

    public abstract class AttributeMenu extends VBox {

//        private final HBox menu;
        private final SectionHeader header;
//        protected final Callback<PropertySheet.Item, PropertyEditor<?>> editorFactory = new DefaultPropertyEditorFactory();
        private Node content;
        private final TextField searchField;
        private final ScrollPane sp;

        public AttributeMenu(String pTitle) {
            super(5.0);
            setFillWidth(true);
            setPadding(new Insets(5));
            setAlignment(Pos.CENTER_LEFT);
//            header = new HBox(new Label(pTitle));
            header = new SectionHeader(pTitle);
//            content = createContent("");
//            content.getStyleClass().add("attribute-menu");
            sp = new ScrollPane();
            sp.setFitToWidth(true);

            sp.setMinHeight(500.0d);
            sp.setMaxHeight(500.0d);
            sp.setMinWidth(1100.0d);
            sp.setMaxWidth(1100.0d);
            loadContent("");
            getChildren().addAll(header, sp);
            addToMenu(createSearchButton());
            searchField = new TextField();
            searchField.getStyleClass().add("attribute-menu-search-field");
            searchField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            loadContent(newValue);
                        }
                    });
                }
            });
        }

        private void loadContent(String pFilter) {
            content = createContent(pFilter);
            content.getStyleClass().add("attribute-menu");
            sp.setContent(content);
        }

        private ToggleButton createSearchButton() {
            ToggleButton btnSearch = new SearchToggleButton();
            btnSearch.getStyleClass().add("analyser-tab-menu-toggle-button");
            btnSearch.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (newValue) {
                        if (!getChildren().contains(searchField)) {
                            getChildren().add(1, searchField);
                        }
                    } else {
                        if (getChildren().contains(searchField)) {
                            getChildren().remove(searchField);
                        }
                    }
                }
            });
            return btnSearch;
        }

        public void addToMenu(Node pNode) {
//            header.getChildren().add(pNode);
            header.getMenuItems().add(pNode);
        }

        public abstract Node createContent(String pSearch);

//        protected final Predicate<String> DEFAULT_PREDICATE = new Predicate<String>() {
//            @Override
//            public boolean test(String t) {
//                return true;
//            }
//        };
//        private final ReadOnlyObjectWrapper<Predicate<String>> predicateProperty = new ReadOnlyObjectWrapper<>(DEFAULT_PREDICATE);
//        public ReadOnlyObjectProperty<Predicate<String>> predicateProperty(){
//            return predicateProperty.getReadOnlyProperty();
//        }
//        public Predicate<String> getPredicate(){
//            return predicateProperty.get();
//        }
//        public void setPredicate(Predicate<String> pPredicate){
//            predicateProperty.set(pPredicate);
//        }
        private void reload() {
            loadContent(searchField.getText());
        }
    }

}
