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

import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.Skinnable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Skin class of menued control
 *
 * @author wilde
 */
public class MenuedControlSkin implements Skin<Skinnable> {

    //skinable
    private final MenuedControl<?> skinnable;
    //ui items
    private final HBox menu = new HBox();
    private final VBox root = new VBox(menu);

    public MenuedControlSkin(MenuedControl<?> pSkinnable) {
        skinnable = pSkinnable;
        //set layout
        root.getStyleClass().add("menued-control");
        menu.setAlignment(Pos.CENTER_RIGHT);
        menu.setSpacing(5.0);
        //bind menu items to menu nodes list
        Bindings.bindContent(menu.getChildren(), pSkinnable.getMenuNodes());
        root.setFillWidth(true);
        root.setSpacing(5.0);
        VBox.setVgrow(skinnable.getControl(), Priority.ALWAYS);
        root.getChildren().add(skinnable.getControl());
    }

    @Override
    public Skinnable getSkinnable() {
        return skinnable;
    }

    @Override
    public Node getNode() {
        return root;
    }

    @Override
    public void dispose() {
        root.getChildren().clear();
    }

}
