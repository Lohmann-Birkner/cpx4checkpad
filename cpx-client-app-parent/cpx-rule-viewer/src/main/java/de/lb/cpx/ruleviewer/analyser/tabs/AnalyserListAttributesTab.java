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

import de.lb.cpx.client.core.model.fx.button.AddButton;
import de.lb.cpx.client.core.model.fx.button.DeleteButton;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.enums.CpxEnumInterface;
import de.lb.cpx.model.enums.LocalisationEn;
import de.lb.cpx.ruleviewer.analyser.attributes.AnalyserAttribute;
import de.lb.cpx.ruleviewer.analyser.attributes.AnalyserAttributes;
import de.lb.cpx.ruleviewer.analyser.attributes.AnalyserBeanProperty;
import de.lb.cpx.ruleviewer.analyser.editors.CaseChangedEvent;
import de.lb.cpx.ruleviewer.analyser.model.LabeledNode;
import de.lb.cpx.ruleviewer.analyser.model.RuleAnalyserItem;
import de.lb.cpx.server.commons.dao.AbstractVersionEntity;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javax.validation.constraints.NotNull;

/**
 *
 * @author wilde
 * @param <E> Entity type
 * @param <T> Attributes type
 */
public abstract class AnalyserListAttributesTab<E extends AbstractVersionEntity, T extends AnalyserAttributes> extends AsyncAnalyserAttributesTab<T> {

    public AnalyserListAttributesTab(String pTitle, T pAttributes) {
        super(pTitle, pAttributes);
        setRuleAnalyserItemFactory(new Callback<AnalyserAttribute, RuleAnalyserItem>() {
            @Override
            public RuleAnalyserItem call(AnalyserAttribute param) {
                RuleAnalyserItem item = new RuleAnalyserItem();
                item.setEditorFactorty(getEditorFactory());
                item.setTitle(param.getDisplayName());
                item.setOnDeleteCallback(new Callback<Void, Boolean>() {
                    @Override
                    public Boolean call(Void param) {
                        item.clearValue();
                        removeAnalyserItem(item);
                        CaseChangedEvent event = new CaseChangedEvent();
                        Event.fireEvent(item, event);
                        return true;
                    }
                });
                return item;
            }
        });

    }
    private Set<E> expanded = new HashSet<>();

    private boolean isExpaned(E pDepartment) {
        for (E expand : expanded) {
            if (expand.versionEquals(pDepartment)) {
                return true;
            }
        }
        return false;
    }

    public abstract List<E> getBeanList(TCase pFall);

    public abstract boolean removeItemFromEntity(@NotNull TCase pFall, @NotNull E pItem);

    public RuleAnalyserItem getRootItemForEntity(E pItem) {
        RuleAnalyserItem depRoot = new RuleAnalyserItem();
        depRoot.setOnDeleteCallback(new Callback<Void, Boolean>() {
            @Override
            public Boolean call(Void param) {
                removeItemFromEntity(getCase(), pItem);
                removeAnalyserItem(depRoot);
                CaseChangedEvent event = new CaseChangedEvent();
                Event.fireEvent(getContent(), event);
                return true;
            }
        });
        depRoot.setExpand(isExpaned(pItem));
        depRoot.expandProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    if (!isExpaned(pItem)) {
                        expanded.add(pItem);
                    }
                } else {
                    if (isExpaned(pItem)) {
                        expanded.remove(pItem);
                    }
                }
            }
        });
//        depRoot.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                if (MouseButton.PRIMARY.equals(event.getButton())) {
//                    getSelectionModel().select(depRoot);
//                }
//            }
//        });
        for (AnalyserAttribute att : getAttributes().getSortedAttributes()) {
            AnalyserBeanProperty beanProp = new AnalyserBeanProperty(pItem, att);
            if (beanProp.getValue() == null) {
                continue;
            }
            if (beanProp.getValue() instanceof Boolean) {
                if (!((Boolean) beanProp.getValue())) {
                    continue;
                }
            }
            if (beanProp.getValue() instanceof LocalisationEn) {
                if (((CpxEnumInterface<Integer>) beanProp.getValue()).getLangKey().isEmpty()) {
                    continue;
                }
            }
            RuleAnalyserItem attItem = getRuleAnalyserItemFactory().call(att);
            attItem.setBeanProperty(beanProp);
            attItem.setParentItem(depRoot);
            attItem.getEditor().addEventFilter(CaseChangedEvent.caseChangedEvent(), new EventHandler<CaseChangedEvent>() {
                @Override
                public void handle(CaseChangedEvent t) {
                    Event.fireEvent(getContent(), t);
                }
            });
            depRoot.getItems().add(attItem);
        }
        return depRoot;
    }

    public MultiAttributesItem getMenuRoot(E pItem) {
        MultiAttributesItem depRoot = new MultiAttributesItem();
        depRoot.setOnDeleteAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                deleteRoot(pItem);
                CaseChangedEvent changeEvent = new CaseChangedEvent();
                Event.fireEvent(depRoot, changeEvent);
            }
        });
        return depRoot;
    }

    public boolean deleteRoot(E pItem) {
        return false;
    }

    public void createAndAddNewEntity() {

    }
    private ObjectProperty<Comparator<E>> comparatorProperty;

    public ObjectProperty<Comparator<E>> comparatorProperty() {
        if (comparatorProperty == null) {
            comparatorProperty = new SimpleObjectProperty<>(Comparator.comparing(E::toString));
        }
        return comparatorProperty;
    }

    public Comparator<E> getComparator() {
        return comparatorProperty().get();
    }

    public void setComparator(Comparator<E> pComparator) {
        comparatorProperty().set(pComparator);
    }

    @Override
    protected List<RuleAnalyserItem> getDataList(TCase pFall) {
        List<RuleAnalyserItem> items = new ArrayList<>();
        for (E bean : getBeanList(pFall)) {
            RuleAnalyserItem depRoot = getRootItemForEntity(bean);
            items.add(depRoot);
        }
        return items;
    }

    @Override
    public AttributeMenu getMenu() {
        menu = new MultiAttributesMenu(getText(), 6);
        return menu;
    }

    private class MultiAttributesMenu extends AttributeMenu {

        private final Integer columns;
        private ArrayList<MultiAttributesItem> items;
        private FlowPane pane;

        public MultiAttributesMenu(String pTitle, Integer pColumns) {
            super(pTitle);
            columns = pColumns;
            AddButton btn = new AddButton();
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    createAndAddNewEntity();
                    reload();
                }
            });
            addToMenu(btn);
        }

        @Override
        public void afterTask(Worker.State pState) {
            super.afterTask(pState); //To change body of generated methods, choose Tools | Templates.
            for (MultiAttributesItem item : items) {
                item.prefWidthProperty().bind(pane.widthProperty().divide(columns));
            }
        }

        @Override
        public Node createContent(String pText) {

            pane = new FlowPane();
            pane.setHgap(5.0);
            pane.setVgap(5.0);

            items = new ArrayList<>();

            for (E department : getBeanList(getCase())) {
                MultiAttributesItem depRoot = getMenuRoot(department);//new MultiAttributesItem();
                List<Node> itms = new ArrayList<>();
                for (AnalyserAttribute att : getAttributes().getSortedAttributes()) {
                    AnalyserBeanProperty beanProp = new AnalyserBeanProperty(department, att);

                    LabeledNode labeled = new LabeledNode(att.getDisplayName(), beanProp, getEditorFactory());
                    itms.add(labeled);
                }
                //add add position 1 after header
                depRoot.getChildren().addAll(1, itms);
                if (pText.isEmpty()) {
                    items.add(depRoot);
                } else {
                    if (checkRootItem(pText, depRoot)) {
                        items.add(depRoot);
                    }
                }
                if (columns != null) {
                    depRoot.prefWidthProperty().bind(pane.widthProperty().divide(columns));
                }
//                items.add(depRoot);
            }
            pane.getChildren().addAll(items);
            return pane;
        }

        private boolean checkRootItem(String pText, MultiAttributesItem depRoot) {
            if (pText.isEmpty()) {
                return true;
            }
            if (depRoot.getTitle().toLowerCase().contains(pText.toLowerCase())) {
                return true;
            }
            for (Node item : depRoot.getChildren()) {
                if (item instanceof LabeledNode) {
                    if (((LabeledNode) item).getTitle().toLowerCase().contains(pText.toLowerCase())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public class MultiAttributesItem extends VBox {

        private final Label title;
        private final FlowPane pane;
        private final Button btnDelete;
        private final HBox header;

        public MultiAttributesItem() {
            super(5.0);
            setStyle("-fx-border-color: lightgrey;");
            title = new Label();
            title.getStyleClass().add("attribute-item");
            title.setStyle("-fx-font-weight:normal;");
            pane = new FlowPane();
            btnDelete = createDeleteButton();
            HBox menu = new HBox(5.0, btnDelete);
//            menu.getStyleClass().add("attribute-menu-header");
            HBox.setHgrow(menu, Priority.ALWAYS);
            menu.setAlignment(Pos.CENTER_RIGHT);

            header = new HBox(5.0, title, menu);
            header.setAlignment(Pos.CENTER_LEFT);
            getChildren().addAll(header, pane);
            setPadding(new Insets(5.0));
        }

        public void setTooltip(Tooltip pTip) {
            Tooltip.install(header, pTip);
        }

        public MultiAttributesItem(String pTitle, List<LabeledNode> pItems) {
            this();
            setTitle(pTitle);
            getItems().addAll(pItems);
        }

        private Button createDeleteButton() {
            DeleteButton btn = new DeleteButton();
            return btn;
        }

        public final ObservableList<Node> getItems() {
            return pane.getChildren();
        }

        public final void setTitle(String pTitle) {
            title.setText(pTitle);
        }

        public final String getTitle() {
            return title.getText();
        }

        public void setOnDeleteAction(EventHandler<ActionEvent> pHandler) {
            btnDelete.setOnAction(pHandler);
        }
    }

}
