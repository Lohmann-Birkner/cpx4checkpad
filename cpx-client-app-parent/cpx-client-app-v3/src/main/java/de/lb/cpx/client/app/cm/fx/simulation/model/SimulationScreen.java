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
package de.lb.cpx.client.app.cm.fx.simulation.model;

import de.lb.cpx.client.core.model.CpxScreen;
import de.lb.cpx.client.core.model.fx.comparablepane.ComparableContent;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.control.ScrollBar;

/**
 *
 * @author wilde
 * @param <E> entity type shown
 * @param <T> type of compareable content ,enclosing object
 */
public abstract class SimulationScreen<E extends AbstractEntity, T extends ComparableContent<E>> extends CpxScreen {

    private final ObservableList<T> listOfVersions;
    private final DoubleProperty hBarValueProperty = new SimpleDoubleProperty(1.0);
    private final DoubleProperty hBarMinProperty = new SimpleDoubleProperty(0.0);
    private final DoubleProperty hBarMaxProperty = new SimpleDoubleProperty(1.0);

    public SimulationScreen(FXMLLoader pLoader, ObservableList<T> pListOfContent) {
        super(pLoader);
        listOfVersions = pListOfContent;
    }

    /**
     * bind bar realign to prev state
     *
     * @param pHBar scrollbar
     */
    public void bindHScrollBar(ScrollBar pHBar) {
        if (pHBar == null) {
            return;
        }
        unBindhScrollBar(pHBar);
        if (pHBar.getOrientation().equals(Orientation.HORIZONTAL)) {
            pHBar.valueProperty().bindBidirectional(hBarValueProperty);
            pHBar.minProperty().bindBidirectional(hBarMinProperty);
            pHBar.maxProperty().bindBidirectional(hBarMaxProperty);
        }
    }

    public void unBindhScrollBar(ScrollBar pHBar) {
        pHBar.valueProperty().unbind();
        pHBar.minProperty().unbind();
        pHBar.maxProperty().unbind();
    }

    public ObservableList<T> getListOfVersions() {
        return listOfVersions;
    }
}
