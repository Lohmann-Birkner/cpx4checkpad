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
import de.lb.cpx.client.core.model.fx.pane.AsyncPane;
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
import java.util.Objects;
import java.util.function.Predicate;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Worker;
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
 * Tab to show AnalyserAttributes analyser attributes are categorized case data,
 * tab should provide base implementation to avoid multiple similar
 * implementations this tab implements the computation of its content async
 *
 * @author wilde
 * @param <T> type of analyser attributes to show in tab
 */
public class AsyncAnalyserAttributesTab<T extends AnalyserAttributes> extends Tab {

//    private FilteredList<RuleAnalyserItem> filterList;
    protected final T attributes;
//    private FlowPane flowPane;
    private final TextField searchTextField;
    private final VBox container;
    protected AttributeMenu menu;
    private final AsyncOverviewContent asyncContent;

    public AsyncAnalyserAttributesTab(String pTitle, T pAttributes) {
        super(pTitle);

        getStyleClass().add("analyser-tab");
        attributes = pAttributes;
        if (attributes == null) {
            setDisable(true);
        }
        setClosable(false);

//        filterList = new FilteredList<>(FXCollections.observableArrayList());
        searchTextField = new TextField();
        searchTextField.textProperty().bindBidirectional(searchTextProperty);
        searchTextField.setPromptText("Suchen");

//        caseProperty().addListener(new ChangeListener<TCase>() {
//            @Override
//            public void changed(ObservableValue<? extends TCase> observable, TCase oldValue, TCase newValue) {
//                reload();
//            }
//        });
        asyncContent = new AsyncOverviewContent();
        container = new VBox(0, asyncContent);
        container.setPadding(new Insets(5, 0, 0, 0));
        container.setFillWidth(true);
        VBox.setVgrow(asyncContent, Priority.ALWAYS);
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
                searchItems(newValue);
            }
        });
        setContent(container);
    }

    public T getAttributes() {
        return attributes;
    }

    public boolean removeAnalyserItem(RuleAnalyserItem pItem) {
        if (pItem.getParentItem() == null) {
            return true;
//            return flowPane.getChildren().remove(pItem);
        } else {
            boolean result = pItem.getParentItem().getItems().remove(pItem);
            if (pItem.getParentItem().getItems().isEmpty()) {
                pItem.getParentItem().getOnDeleteCallback().call(null);
            }
            return result;
        }
    }
    private Callback<PropertySheet.Item, PropertyEditor<?>> editorFactory = new DefaultPropertyEditorFactory();

    public Callback<PropertySheet.Item, PropertyEditor<?>> getEditorFactory() {
        return editorFactory;
    }

    public void setEditorFactorty(Callback<PropertySheet.Item, PropertyEditor<?>> pFactory) {
        editorFactory = pFactory;
    }

//    public final ObjectProperty<TCase> caseProperty() {
//        if (caseProperty == null) {
//            caseProperty = new SimpleObjectProperty<>();
//        }
//        return caseProperty;
//    }
//
//    public void setCase(TCase pCase) {
//        caseProperty().set(pCase);
//    }
//
//    public TCase getCase() {
//        return caseProperty.get();
//    }
    public TCase loadCase() {
        return null;
    }

    protected TCase getCase() {
        return loadCase();
    }
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
        public RuleAnalyserItem call(AnalyserAttribute pAttribute) {
            pAttribute.getKey();
            AnalyserBeanProperty beanProp = new AnalyserBeanProperty(getBean(pAttribute), pAttribute);
            Object value = beanProp.getValue();//getValue(en, pFall);
            //skip if no value is set
            if (value == null) {
                return null;
            }
            RuleAnalyserItem item = new RuleAnalyserItem();
            item.setTitle(pAttribute.getDisplayName());
            item.setBeanProperty(beanProp);
            item.setOnDeleteCallback(new Callback<Void, Boolean>() {
                @Override
                public Boolean call(Void param) {
//                        flowPane.getChildren().remove(item);
                    beanProp.setValue(null);
                    beanProp.getValue();
                    //force full set value again to null
                    //in rare circumstances it can happen that case object is not updated properly
                    //to do: investigate why!
                    AnalyserBeanProperty beanProp2 = new AnalyserBeanProperty(getBean(pAttribute), pAttribute);
                    beanProp2.setValue(null);
                    CaseChangedEvent event = new CaseChangedEvent();
                    Event.fireEvent(getContent(), event);
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
        menu = new SingleAttributeMenu(getText());
        return menu;
    }

    protected List<RuleAnalyserItem> getDataList(TCase pFall) {
        List<RuleAnalyserItem> items = new ArrayList<>();
        if (pFall == null) {
            return items;
        }
        List<AnalyserAttribute> sorted = attributes.getSortedAttributes();
        for (AnalyserAttribute att : sorted) {
            RuleAnalyserItem item = getRuleAnalyserItemFactory().call(att);//createAnalyserItem(att, pFall,getBeanFactory().call(att), flowPane);
            if (item != null) {
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
        asyncContent.setPredicate(pText.isEmpty() ? new Predicate<RuleAnalyserItem>() {
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
//        flowPane.getChildren().clear();
//        filterList = new FilteredList<>(FXCollections.observableArrayList(getDataList(getCase())));
//        searchItems(getSearchText());
//        flowPane.getChildren().addAll(filterList);
        asyncContent.reload();
        if (menu != null) {
            menu.reload();
        }
    }

    public void clearContent() {
        asyncContent.clear();
    }

    private class AsyncOverviewContent extends AsyncPane<Node> {

        private final FlowPane pane;
        private final ScrollPane scrollPane;

        public AsyncOverviewContent() {
            super();
            pane = new FlowPane();
            pane.setPadding(new Insets(5, 0, 5, 0));
            pane.setHgap(5);
            pane.setVgap(5);
            predicateProperty().addListener(new ChangeListener<Predicate<RuleAnalyserItem>>() {
                @Override
                public void changed(ObservableValue<? extends Predicate<RuleAnalyserItem>> observable, Predicate<RuleAnalyserItem> oldValue, Predicate<RuleAnalyserItem> newValue) {
                    reload();
                }
            });
            scrollPane = new ScrollPane(pane);
            scrollPane.setFitToWidth(true);
//            pane.minHeightProperty().bind(minHeightProperty());
//            pane.prefHeightProperty().bind(prefHeightProperty());
//            pane.maxHeightProperty().bind(maxHeightProperty());
//            
//            pane.prefWrapLengthProperty().bind(prefHeightProperty());
        }

        @Override
        public Node loadContent() {
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    pane.getChildren().clear();
//                }
//            });
            if (getCase() == null) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        pane.getChildren().clear();
                    }
                });
                return pane;
            }
            FilteredList<RuleAnalyserItem> filterList = new FilteredList<>(FXCollections.observableArrayList(getDataList(getCase())));
            filterList.setPredicate(getPredicate());
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    pane.getChildren().clear();
                    pane.getChildren().addAll(filterList);
                }
            });
//            pane.maxHeightProperty().bind(prefHeightProperty());
            return scrollPane;
        }

        private final Predicate<RuleAnalyserItem> defaultPredicate = (RuleAnalyserItem t) -> true;
        private final ReadOnlyObjectWrapper<Predicate<RuleAnalyserItem>> predicateProperty = new ReadOnlyObjectWrapper<>(defaultPredicate);

        public final ReadOnlyObjectProperty<Predicate<RuleAnalyserItem>> predicateProperty() {
            return predicateProperty.getReadOnlyProperty();
        }

        public Predicate<RuleAnalyserItem> getPredicate() {
            return predicateProperty().get();
        }

        public void setPredicate(Predicate<RuleAnalyserItem> pPredicate) {
            pPredicate = Objects.requireNonNullElse(pPredicate, defaultPredicate);
            predicateProperty.set(pPredicate);
        }

        @Override
        public void reload() {
            super.reload(); //To change body of generated methods, choose Tools | Templates.
        }

        public void clear() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    pane.getChildren().clear();
                }
            });
        }
    }

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

        private final SectionHeader header;
        private Node content;
        private final TextField searchField;
//        private ScrollPane scrollPane;
        private AsyncPane<ScrollPane> asyncContent;
        private final VBox scrollPane;

        public AttributeMenu(String pTitle) {
            super(5.0);
            setFillWidth(true);
            setPadding(new Insets(5));
            setAlignment(Pos.CENTER_LEFT);
            header = new SectionHeader(pTitle);
//            scrollPane = new ScrollPane();
//            scrollPane.setFitToWidth(true);
//
//            scrollPane.setMinHeight(500.0d);
//            scrollPane.setMaxHeight(500.0d);
//            scrollPane.setMinWidth(1100.0d);
//            scrollPane.setMaxWidth(1100.0d);
//            loadContent("");
//            getChildren().addAll(header,sp);
            addToMenu(createSearchButton());
            searchField = new TextField();
            searchField.getStyleClass().add("attribute-menu-search-field");
            searchField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    asyncContent.reload();
//                    Platform.runLater(new Runnable() {
//                        @Override
//                        public void run() {
//                            loadContent(newValue);
//                        }
//                    });
                }
            });
            asyncContent = new AsyncPane<>() {
                @Override
                public void beforeTask() {
                    super.beforeTask(); //To change body of generated methods, choose Tools | Templates.
                    scrollPane.setAlignment(Pos.CENTER);
                }

                @Override
                public ScrollPane loadContent() {
                    ScrollPane sp = new ScrollPane();
                    sp.setFitToWidth(true);
                    sp.setContent(AttributeMenu.this.loadContent(searchField.getText()));
                    return sp;
                }

                @Override
                public void afterTask(Worker.State pState) {
                    super.afterTask(pState); //To change body of generated methods, choose Tools | Templates.
                    scrollPane.setAlignment(Pos.TOP_LEFT);
                    AttributeMenu.this.afterTask(pState);
                }

            };
            ScrollPane sp = new ScrollPane(asyncContent);
            sp.setFitToWidth(true);
            sp.setFitToHeight(true);
            scrollPane = new VBox(sp);
            VBox.setVgrow(scrollPane, Priority.ALWAYS);
            scrollPane.setFillWidth(true);
            scrollPane.setMinHeight(500.0d);
            scrollPane.setMaxHeight(500.0d);
            scrollPane.setMinWidth(1100.0d);
            scrollPane.setMaxWidth(1100.0d);

//            scrollPane.setContent(asyncContent);
            getChildren().addAll(header, scrollPane);
        }

        public void afterTask(Worker.State pState) {

        }

        private Node loadContent(String pFilter) {
            content = createContent(pFilter);
            content.getStyleClass().add("attribute-menu");
            return content;
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
            header.getMenuItems().add(pNode);
        }

        public abstract Node createContent(String pSearch);

        private void reload() {
            asyncContent.reload();
//            loadContent(searchField.getText());
        }
    }

}
