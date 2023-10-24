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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.util;

import java.util.Set;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * Helper class to access node objects
 *
 * @author wilde
 */
public class NodeUtil {

    /**
     * get the scrollbar with a specific orientation from a node if any is
     * present
     *
     * @param pNode node to look up the scrollbar
     * @param pOrientation orientation of the scrollbar
     * @return scrollbar reference
     */
    public static ScrollBar getScrollbar(Node pNode, Orientation pOrientation) {
        Set<Node> nodes = pNode.lookupAll(".scroll-bar");
        for (final Node node : nodes) {
            if (node instanceof ScrollBar) {
                ScrollBar sb = (ScrollBar) node;
                if (sb.getOrientation() == pOrientation) {
                    return sb;
                }
            }
        }
        return null;
    }

    /**
     * tries to handle scrollbars if default policy behaviour does not match the
     * desired outcome Policy ALWAYS, shows as name suggest always the bar, not
     * needed im content is smaller than the ScrollPane Policy AS_NEEDED, shows
     * the bar over the content when drawn, information get lost that way
     * Methode switches the policies according to the size of the content when
     * the width property of the scrollpane changes (for example on resize of
     * the parent window)
     *
     * @param pScrollPane pane to add behaviour to
     */
    public static void handleScrollBarPolicies(ScrollPane pScrollPane) {
        pScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        pScrollPane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (pScrollPane.getContent().getBoundsInLocal().getWidth() > pScrollPane.getWidth()) {
                    pScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                } else {
                    pScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                }
            }
        });
        //bug fix for resizing if user clicked into the scrollpane, not optimal needs to be fixed! AWi 20170106
        pScrollPane.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<javafx.event.Event>() {
            @Override
            public void handle(javafx.event.Event event) {
                if (pScrollPane.getContent().getBoundsInLocal().getWidth() > pScrollPane.getWidth()) {
                    pScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                } else {
                    pScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                }
            }
        });
    }

    /**
     * @param pNode set anchorpane anchors to 0
     */
    public static void setAnchorsToZero(Node pNode) {
        AnchorPane.setTopAnchor(pNode, 0.0);
        AnchorPane.setRightAnchor(pNode, 0.0);
        AnchorPane.setBottomAnchor(pNode, 0.0);
        AnchorPane.setLeftAnchor(pNode, 0.0);
    }
}
