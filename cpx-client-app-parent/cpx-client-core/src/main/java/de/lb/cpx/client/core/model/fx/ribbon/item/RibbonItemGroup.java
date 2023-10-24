/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.ribbon.item;

import com.sun.javafx.event.EventHandlerManager;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.event.EventDispatchChain;
import javafx.event.EventTarget;
import javafx.geometry.Side;
import javafx.scene.Node;

/**
 * group of items in a RibbonItem
 *
 * @author wilde
 */
public class RibbonItemGroup implements EventTarget, Styleable {

//extends Control{
    private static final String DEFAULT_STYLE_CLASS = "ribbon-group";
    private final ObservableList<Node> items;
    //title side, defines where the title is placed
    private ObjectProperty<Side> titleSideProperty;
    //title property
    private StringProperty titleProperty;
    //property to define, if title should be displayed
    private BooleanProperty showTitleProperty;
    private final EventHandlerManager eventHandlerManager = new EventHandlerManager(this);
    private final StringProperty idProperty = new SimpleStringProperty();
    private final ObservableList<String> styleClass = FXCollections.observableArrayList();
    private final StringProperty styleProperty = new SimpleStringProperty();
    private ReadOnlyObjectWrapper<RibbonItem> ribbonItem;
    private final InvalidationListener parentDisabledChangedListener = valueModel -> {
        updateDisabled();
    };
    private BooleanProperty disable;

    /**
     * construct new instance
     */
    public RibbonItemGroup() {
        super();
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.items = FXCollections.observableArrayList();
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return Collections.emptyList();
    }

    /**
     * @return list of items in group
     */
    public ObservableList<Node> getItems() {
        return items;
    }

    /**
     * @return title side property stores where the title should appear default
     * buttom
     */
    public ObjectProperty<Side> titleSideProperty() {
        if (titleSideProperty == null) {
            titleSideProperty = new SimpleObjectProperty<>(Side.BOTTOM);
        }
        return titleSideProperty;
    }

    /**
     * @return side on which the title should appear
     */
    public Side getTitleSide() {
        return titleSideProperty().get();
    }

    /**
     * @param pSide set new title side
     */
    public void setTitleSide(Side pSide) {
        titleSideProperty().set(pSide);
    }

    /**
     * @return title property for the group
     */
    public StringProperty titleProperty() {
        if (titleProperty == null) {
            titleProperty = new SimpleStringProperty("");
        }
        return titleProperty;
    }

    /**
     * @return group title default empty
     */
    public String getTitle() {
        return titleProperty().get();
    }

    /**
     * @param pTitle set new title to group
     */
    public void setTitle(String pTitle) {
        titleProperty().set(pTitle);
    }

    /**
     * @return the show title property
     */
    public BooleanProperty showTitleProperty() {
        if (showTitleProperty == null) {
            showTitleProperty = new SimpleBooleanProperty(true);
        }
        return showTitleProperty;
    }

    /**
     * @param pShow set show title
     */
    public void setShowTitle(boolean pShow) {
        showTitleProperty().set(pShow);
    }

    /**
     * @return indicator if the title is currently displayed
     */
    public boolean isShowTitle() {
        return showTitleProperty().get();
    }

    @Override
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        return tail.prepend(eventHandlerManager);
    }

    @Override
    public String getTypeSelector() {
        return "RibbonItemGroup";
    }

    public void setId(String pId) {
        idProperty.set(pId);
    }

    @Override
    public String getId() {
        return idProperty.get();
    }

    @Override
    public ObservableList<String> getStyleClass() {
        return styleClass;
    }

    @Override
    public String getStyle() {
        return styleProperty.get();
    }

    public void setStyle(String pStyle) {
        styleProperty.set(pStyle);
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    final void setRibbonItem(RibbonItem value) {
        ribbonItem().set(value);
    }

    public final RibbonItem getRibbonItem() {
        return ribbonItem == null ? null : ribbonItem.get();
    }

    public final ReadOnlyObjectProperty<RibbonItem> ribbonItemProperty() {
        return ribbonItem().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<RibbonItem> ribbonItem() {
        if (ribbonItem == null) {
            ribbonItem = new ReadOnlyObjectWrapper<RibbonItem>(this, "ribbonItem") {
                private WeakReference<RibbonItem> oldParent;

                @Override
                protected void invalidated() {
                    if (oldParent != null && oldParent.get() != null) {
                        oldParent.get().disabledProperty().removeListener(parentDisabledChangedListener);
                    }
                    updateDisabled();
                    RibbonItem newParent = get();
                    if (newParent != null) {
                        newParent.disabledProperty().addListener(parentDisabledChangedListener);
                    }
                    oldParent = new WeakReference<>(newParent);
                    super.invalidated();
                }
            };
        }
        return ribbonItem;
    }

    public final void setDisable(boolean value) {
        disableProperty().set(value);
    }

    public final boolean isDisable() {
        return disable == null ? false : disable.get();
    }

    public final BooleanProperty disableProperty() {
        if (disable == null) {
            disable = new BooleanPropertyBase(false) {
                @Override
                protected void invalidated() {
                    updateDisabled();
                }

                @Override
                public Object getBean() {
                    return RibbonItemGroup.this;
                }

                @Override
                public String getName() {
                    return "disable";
                }
            };
        }
        return disable;
    }

    private void updateDisabled() {
        final RibbonItem ribbItem = getRibbonItem();
        boolean disabled = isDisable() || (ribbItem != null && ribbItem.isDisabled());
        setDisable(disabled);

        for (Node item : items) {
            item.setDisable(disabled);
        }
    }

    @Override
    public Styleable getStyleableParent() {
        return ribbonItemProperty().get();
    }

    @Override
    public ObservableSet<PseudoClass> getPseudoClassStates() {
        return FXCollections.emptyObservableSet();
    }
}
