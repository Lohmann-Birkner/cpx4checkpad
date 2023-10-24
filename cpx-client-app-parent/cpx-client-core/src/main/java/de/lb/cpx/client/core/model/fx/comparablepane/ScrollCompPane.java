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
package de.lb.cpx.client.core.model.fx.comparablepane;

import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.Iterator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Skin;

/**
 * ScrollPane as Comparable Pane used in simulation
 *
 * @author wilde
 * @param <E> type of content
 */
public abstract class ScrollCompPane<E extends ComparableContent<? extends AbstractEntity>> extends ComparablePane<ScrollPane, E> {

    public ScrollCompPane() {
        super();
        //bind scrollbar of the infopane after rendered
        getInfo().skinProperty().addListener(new ChangeListener<Skin<?>>() {
            @Override
            public void changed(ObservableValue<? extends Skin<?>> observable, Skin<?> oldValue, Skin<?> newValue) {
                for (Node n : getInfo().lookupAll(".scroll-bar")) {
                    if (n instanceof ScrollBar) {
                        ScrollBar bar = (ScrollBar) n;
                        if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                            bar.setDisable(true);
//                            getVScrollBar().visibleProperty().bindBidirectional(bar.visibleProperty());
                            getVScrollBar().visibleAmountProperty().bindBidirectional(bar.visibleAmountProperty());
                            getVScrollBar().valueProperty().bindBidirectional(bar.valueProperty());
                            getVScrollBar().minProperty().bindBidirectional(bar.minProperty());
                            getVScrollBar().maxProperty().bindBidirectional(bar.maxProperty());
                        }
                    }
                }
            }
        });
        //set some default values
        setMinWidthVBar(15);
        getInfo().setMaxWidth(USE_COMPUTED_SIZE);
        getInfo().setMaxHeight(USE_COMPUTED_SIZE);

    }

    /**
     * sets new menu control in itemContainer to reflect update Call
     * versionCtrlFactory
     */
    public void refreshMenu() {
        Iterator<E> it = getTableViewToVersion().keySet().iterator();
        while (it.hasNext()) {
            E next = it.next();
            getTableViewToVersion().get(next).setControl(getVersionCtrlFactory().call(next));
        }
    }
}
