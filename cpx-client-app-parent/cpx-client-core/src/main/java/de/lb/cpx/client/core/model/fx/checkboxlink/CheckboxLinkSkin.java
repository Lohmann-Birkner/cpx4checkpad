/*
 * Copyright (c) 2016 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2018  shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.checkboxlink;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 *
 * @author Shahin
 */
public class CheckboxLinkSkin extends SkinBase<CheckboxLink> {

    private CheckBox checkbox = new CheckBox("");
    private Hyperlink link = new Hyperlink();
    private HBox root = new HBox(checkbox, link);

    private final EventHandler<MouseEvent> focusListener = new EventHandler<>() {
        @Override
        public void handle(MouseEvent event) {
            getSkinnable().requestFocus();
        }
    };

    public CheckboxLinkSkin(CheckboxLink pSkinnable) {
        super(pSkinnable);
        getChildren().add(root);
        root.setAlignment(Pos.BOTTOM_LEFT);
        link.visibleProperty().bind(Bindings
                .when(checkbox.selectedProperty())
                .then(true)
                .otherwise(false));
        link.getStyleClass().add("link-class");

        link.textProperty().bind(pSkinnable.textProperty());

        link.onActionProperty().bind(pSkinnable.onActionProperty());

        link.addEventFilter(MouseEvent.MOUSE_CLICKED, focusListener);
        checkbox.addEventFilter(MouseEvent.MOUSE_CLICKED, focusListener);
        root.setOnMouseClicked(focusListener);
        pSkinnable.selectedValueProperty().bindBidirectional(checkbox.selectedProperty());

        pSkinnable.selectedValueProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    if (pSkinnable.getOnAction() == null) {
                        return;
                    }
                    pSkinnable.getOnAction().handle(new ActionEvent());
                }
            }
        });
    }

}
