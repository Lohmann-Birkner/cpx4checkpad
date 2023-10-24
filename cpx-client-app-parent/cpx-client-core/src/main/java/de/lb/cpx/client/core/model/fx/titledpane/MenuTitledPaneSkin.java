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

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.skin.TitledPaneSkin;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Skin for the MenuTitlePane
 *
 * @author wilde
 */
public class MenuTitledPaneSkin extends TitledPaneSkin {

    private final Label title = new Label();
    private final HBox boxMenuItems = new HBox(9.0);

    public MenuTitledPaneSkin(MenuTitledPane tp) {
        super(tp);
        tp.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        title.textProperty().bind(tp.textProperty());
        Bindings.bindContent(boxMenuItems.getChildren(), tp.getMenuItems());

        boxMenuItems.setPadding(new Insets(0, 12, 0, 0));
        HBox.setHgrow(boxMenuItems, Priority.ALWAYS);
        boxMenuItems.setAlignment(Pos.CENTER_RIGHT);
        HBox menuBox = new HBox(title, boxMenuItems);
        boxMenuItems.setVisible(!tp.isHideMenu());
        boxMenuItems.visibleProperty().bind(tp.hideMenuProperty().not());
        menuBox.minWidthProperty().bind(tp.widthProperty().subtract(25));
        menuBox.prefWidthProperty().bindBidirectional(tp.prefWidthProperty());
//        menuBox.maxWidthProperty().bind(tp.widthProperty().subtract(25));
        tp.setGraphic(menuBox);
    }

}
