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

import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Skin;
import javafx.scene.layout.Region;

/**
 * Helper Class to Handle ScrollBars in different Layouts, like
 * TableView,ScrollPane etc.
 *
 * @author wilde
 */
public class ScrollBarHelper {

    /**
     * sets listeners to scrollbar to react to changes, changes policy to
     * show_allways, to avoid redrawing of the content if scrollbar is shown
     *
     * @param pScrollPane pane to add behaviour to
     * @param pOrientation orientation of the scrollbar to change
     */
    public static void handleScrollBars(ScrollPane pScrollPane, Orientation pOrientation) {

        if (pOrientation == null) {
            Logger.getLogger(ScrollBar.class.getSimpleName()).warning("no orientation is set, do not add listeners");
            return;
        }
        switch (pOrientation) {
            case HORIZONTAL:
                pScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                break;
            case VERTICAL:
                pScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                break;

        }
        //width change on drawing, bar is only than present
        ((Region) pScrollPane.getContent()).widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                for (final Node node : pScrollPane.lookupAll(".scroll-bar")) {
                    if (node instanceof ScrollBar) {
                        ScrollBar sb = (ScrollBar) node;
                        if (sb.getOrientation() == pOrientation) {
                            sb.setVisible(Double.doubleToRawLongBits(sb.getVisibleAmount()) != Double.doubleToRawLongBits(1.0d));
                            if (pOrientation.equals(Orientation.HORIZONTAL)) {
                                sb.widthProperty().addListener(new ChangeListener<Number>() {
                                    @Override
                                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                        sb.setVisible(Double.doubleToRawLongBits(sb.getVisibleAmount()) != Double.doubleToRawLongBits(1.0d));
                                    }
                                });
                            }
                            if (pOrientation.equals(Orientation.VERTICAL)) {
                                sb.heightProperty().addListener(new ChangeListener<Number>() {
                                    @Override
                                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                        sb.setVisible(Double.doubleToRawLongBits(sb.getVisibleAmount()) != Double.doubleToRawLongBits(1.0d));
                                    }
                                });
                            }
                        }
                        sb.visibleProperty().addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                if (newValue && Double.doubleToRawLongBits(sb.getVisibleAmount()) == Double.doubleToRawLongBits(1.0d)) {
                                    sb.setVisible(oldValue);
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public static void handleScrollBars(ScrollPane pScrollPane) {
        handleScrollBars(pScrollPane, Orientation.HORIZONTAL);
        handleScrollBars(pScrollPane, Orientation.VERTICAL);
    }

    public static ObjectProperty<ScrollBar> getScrollBarProperty(ScrollPane pScrollPane, Orientation pOrientation) {
        ObjectProperty<ScrollBar> barProperty = new SimpleObjectProperty<>();
        ScrollBar sb = getScrollBar(pScrollPane, pOrientation);
        if (sb != null) {
            barProperty.set(sb);
            return barProperty;
        }
        pScrollPane.skinProperty().addListener(new ChangeListener<Skin<?>>() {
            @Override
            public void changed(ObservableValue<? extends Skin<?>> observable, Skin<?> oldValue, Skin<?> newValue) {
                ScrollBar sb = getScrollBar(pScrollPane, pOrientation);
                barProperty.set(sb);
            }
        });
        return barProperty;
    }

    public static ScrollBar getScrollBar(ScrollPane pScrollPane, Orientation pOrientation) {
        for (final Node node : pScrollPane.lookupAll(".scroll-bar")) {
            if (node instanceof ScrollBar) {
                ScrollBar sb = (ScrollBar) node;
                if (sb.getOrientation() == pOrientation) {
                    return sb;
                }
            }
        }
        return null;
    }

    public static void bindTo(ScrollBar pBind, ScrollBar pTo) {

//        pBind.visibleProperty().bindBidirectional(pTo.visibleProperty());
        pBind.visibleAmountProperty().bindBidirectional(pTo.visibleAmountProperty());
        pBind.valueProperty().bindBidirectional(pTo.valueProperty());
        pBind.minProperty().bindBidirectional(pTo.minProperty());
        pBind.maxProperty().bindBidirectional(pTo.maxProperty());
    }
}
