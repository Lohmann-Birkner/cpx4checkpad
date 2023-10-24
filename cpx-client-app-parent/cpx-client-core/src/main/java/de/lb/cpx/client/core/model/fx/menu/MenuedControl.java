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
package de.lb.cpx.client.core.model.fx.menu;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * implementation for a menued control similar to labeled control, only with
 * defined menu
 *
 * @author wilde
 * @param <T> type of control to add menu to
 */
public abstract class MenuedControl<T extends Control> extends Control {

    //control wrapped by menu
    private final ObjectProperty<T> controlProperty = new SimpleObjectProperty<>(null);
    //nodes to show in menu area
    private final ObservableList<Node> menuNodes = FXCollections.observableArrayList();

    public MenuedControl(T pControl) {
        controlProperty.set(pControl);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new MenuedControlSkin(this);
    }

    /**
     * @return wrapped controls
     */
    public T getControl() {
        return controlProperty.get();
    }

    /**
     * @return menu nodes to show
     */
    public ObservableList<Node> getMenuNodes() {
        return menuNodes;
    }

    /**
     * @param pNodes nodes to add
     * @return if adding was successful
     */
    public boolean addMenuNodes(Node... pNodes) {
        return menuNodes.addAll(pNodes);
    }

    /**
     * @param pNode nodes to remove
     * @return if removing was successful
     */
    public boolean removeMenuNode(Node pNode) {
        return menuNodes.remove(pNode);
    }

}
