/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.util;

import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 * Extended Info helper class adds additional info behavior to node either with
 * info button or as hyperlink
 *
 * @author wilde
 */
public class ExtendedInfoHelper {

    public static Pane addInfoPane(Pane pPane, Node pContent) {
        return addInfoPane(pPane, pContent, null);
    }

    public static Pane addInfoPane(Pane pPane, Node pContent, PopOver.ArrowLocation pArrowLocation) {
        return addInfoPane(pPane, new Callback<Void, Node>() {
            @Override
            public Node call(Void param) {
                return pContent;
            }
        }, pArrowLocation);
    }

    public static Pane addInfoPane(Pane pPane, Callback<Void, Node> pContentFactory, PopOver.ArrowLocation pArrowLocation) {
        Label label = new Label();
        label.setPrefHeight(Label.USE_COMPUTED_SIZE);
        label.getStyleClass().add("add-info-label");
        Glyph glyph = ResourceLoader.getGlyph(FontAwesome.Glyph.INFO);
        glyph.setFontSize(10);
        label.setGraphic(glyph);
        label.setAlignment(Pos.BOTTOM_CENTER);
        StackPane stkPane = new StackPane(pPane, label);
        stkPane.getStyleClass().add("cpx-add-info-pane");
        stkPane.prefWidthProperty().bind(pPane.widthProperty().add(10));
        stkPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            private ExtendedInfoPopover popover;

            @Override
            public void handle(MouseEvent event) {
                if (popover == null) {
//                    popover = new ExtendedInfoPopover(pContentFactory.call(null),pArrowLocation!=null?pArrowLocation:PopOver.ArrowLocation.TOP_CENTER);
                    popover = new ExtendedInfoPopover();
                    popover.setDefaultArrowLocation(pArrowLocation != null ? pArrowLocation : PopOver.ArrowLocation.TOP_CENTER);
                }
                if (!popover.isShowing()) {
                    popover.setContentNode(pContentFactory.call(null));
                    popover.show(stkPane);
                } else {
                    popover.hide();
                    popover = null;
                }
            }
        });
        StackPane.setAlignment(pPane, Pos.CENTER_RIGHT);
        StackPane.setAlignment(label, Pos.BOTTOM_RIGHT);
        return stkPane;
    }

    public static Pane addInfoPane(Label pLabel, Node pContent, PopOver.ArrowLocation pArrowLocation) {
        return addInfoPane(pLabel, new Callback<Void, Node>() {
            @Override
            public Node call(Void param) {
                return pContent;
            }
        }, pArrowLocation);
    }

    public static Pane addInfoPane(Label pLabel, Callback<Void, Node> pContentFactory, PopOver.ArrowLocation pArrowLocation) {
        VBox pane = new VBox(pLabel);
        pane.setFillWidth(true);
        pLabel.setStyle("-fx-font-size:15;");
        pane.setAlignment(Pos.CENTER_LEFT);
        pane.setPadding(new Insets(0, 8, 0, 0));
        return addInfoPane(pane, pContentFactory, pArrowLocation);
    }

    private static class ExtendedInfoPopover extends AutoFitPopOver {

        public ExtendedInfoPopover(Node content) {
            this();
            setContentNode(content);
        }

        public ExtendedInfoPopover(Node pContent, PopOver.ArrowLocation pArrowLocation) {
            this(pContent);
            setDefaultArrowLocation(pArrowLocation);
        }

        public ExtendedInfoPopover() {
            super();
            setHideOnEscape(true);
            setDetachable(false);
            setAutoHide(true);
            setAutoFix(true);
        }
    }
}
