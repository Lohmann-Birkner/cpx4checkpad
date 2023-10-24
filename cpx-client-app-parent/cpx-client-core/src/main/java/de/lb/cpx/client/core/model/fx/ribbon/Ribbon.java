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
package de.lb.cpx.client.core.model.fx.ribbon;

import de.lb.cpx.client.core.model.fx.ribbon.item.RibbonItem;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * Ribbon implementation to have some kind of additional toolbar menu, like the
 * windows element
 *
 * @author wilde
 */
public class Ribbon extends Control {

    private static final String DEFAULT_STYLE_CLASS = "ribbon";
    //items/tabs to be shown in the ribbon
    private ObservableList<RibbonItem> items;

    /**
     * creates new instance
     */
    public Ribbon() {
        items = FXCollections.observableArrayList();
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        sideProperty().addListener(new ChangeListener<Side>() {
            @Override
            public void changed(ObservableValue<? extends Side> observable, Side oldValue, Side newValue) {
                updateItemOrientation(newValue);
            }
        });
        updateItemOrientation(getSide());
        items.addListener(new ListChangeListener<RibbonItem>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends RibbonItem> c) {
                if (c.next()) {
                    if (c.wasAdded()) {
                        for (RibbonItem item : c.getAddedSubList()) {
                            item.setOrientation(getOrientationForSide(getSide()));
                        }
                    }
                }
            }
        });
//        RibbonItem dummy = new RibbonItem();
//        dummy.setText("title");
//        RibbonItemGroup dummyGroup = new RibbonItemGroup();
//        dummyGroup.getItems().addAll(new ComboBox<>(),new ComboBox<>());
//        dummy.getItems().add(dummyGroup);
//        getItems().add(dummy);

    }

    /**
     * update oreintation of all currently placed tab item due to orientaton set
     *
     * @param pSide side of the tabpane
     */
    protected final void updateItemOrientation(Side pSide) {
        updateItemOrientation(getOrientationForSide(pSide));
    }

    //convert side to orientation for the content
    private Orientation getOrientationForSide(Side pSide) {
        switch (pSide) {
            case TOP:
            case BOTTOM:
                return Orientation.HORIZONTAL;
            case LEFT:
            case RIGHT:
                return Orientation.VERTICAL;
            default:
                return Orientation.HORIZONTAL;
        }
    }

    /**
     * update roentation of all all currently placed tab items
     *
     * @param pOrientation orientation desired
     */
    protected final void updateItemOrientation(Orientation pOrientation) {
        for (RibbonItem item : items) {
            item.setOrientation(pOrientation);
        }
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new RibbonSkin(this);
    }

    /**
     * @return list of all tabs/items in the ribbon
     */
    public ObservableList<RibbonItem> getItems() {
        return items;
    }
    //store selected item
    private final ReadOnlyObjectWrapper<RibbonItem> selectedItemProperty = new ReadOnlyObjectWrapper<>();

    /**
     * @return selected item property as read only property
     */
    public ReadOnlyObjectProperty<RibbonItem> selectedItemProperty() {
        return selectedItemProperty.getReadOnlyProperty();
    }

    /**
     * @return current selected item
     */
    public RibbonItem getSelectedItem() {
        return selectedItemProperty.get();
    }

    /**
     * @param pItem select item
     */
    public void select(RibbonItem pItem) {
        selectedItemProperty.set(pItem);
    }
    //side property whre the tab/items headers should be shown
    private ObjectProperty<Side> sideProperty;

    /**
     * @return side where the headers are to be shown, top/bottom horizontal
     * right/left vertical default: top
     */
    public final ObjectProperty<Side> sideProperty() {
        if (sideProperty == null) {
            sideProperty = new SimpleObjectProperty<>(Side.TOP);
        }
        return sideProperty;
    }

    /**
     * @return current selected side
     */
    public final Side getSide() {
        return sideProperty().get();
    }

    /**
     * @param pSide set side where the header should be shown, top/bottom
     * horizontal right/left vertical
     */
    public void setSide(Side pSide) {
        sideProperty().setValue(pSide);
    }

    public void clear() {
        getItems().clear();
        items = null;
    }
}
