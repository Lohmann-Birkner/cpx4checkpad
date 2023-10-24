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
package de.lb.cpx.client.core.model.fx.titledpane;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.TitledPane;

/**
 * Javafx TitledPane with menu and async loading of content
 *
 * Menu is set in setGrahpic, setting another grahic object will disable the
 * default menu!
 *
 * @author wilde
 */
public class MenuTitledPane extends TitledPane {

    @Override
    protected Skin<?> createDefaultSkin() {
        return new MenuTitledPaneSkin(this);
    }

    private final BooleanProperty hideMenuProperty = new SimpleBooleanProperty(false);

    public BooleanProperty hideMenuProperty() {
        return hideMenuProperty;
    }

    public boolean isHideMenu() {
        return hideMenuProperty().get();
    }

    public void setHideMenu(boolean pHide) {
        hideMenuProperty().set(pHide);
    }
    private final ObservableList<Node> menuItems = FXCollections.observableArrayList();

    public final ObservableList<Node> getMenuItems() {
        return menuItems;
    }

    public void addMenuItem(Node pNode) {
        getMenuItems().add(pNode);
    }

    public void addAllMenuItems(Node... pNodes) {
        getMenuItems().addAll(pNodes);
    }

    public boolean removeMenuItems(Node pNode) {
        return getMenuItems().remove(pNode);
    }

    public boolean removeAllMenuItems(Node... pNodes) {
        return getMenuItems().removeAll(pNodes);
    }

    public void setAllMenuItems(Node... pNodes) {
        getMenuItems().setAll(pNodes);
    }
}
