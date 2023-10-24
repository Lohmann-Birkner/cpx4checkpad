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
package de.lb.cpx.client.core.model.fx.tooltip;

import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.server.commons.dao.AbstractCatalogEntity;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;

/**
 * Implementation of the Case Version Tooltip to unify behaviour
 *
 * @author wilde
 * @param <T> type of catalog to show
 */
public abstract class CatalogTooltip<T extends AbstractCatalogEntity> extends BasicTooltip {

    private final ObjectProperty<T> catalogItemProperty = new SimpleObjectProperty<>();
    private final StringProperty countryProperty = new SimpleStringProperty(AbstractCpxCatalog.DEFAULT_COUNTRY);
    private final IntegerProperty yearOfValidityProperty = new SimpleIntegerProperty(0);

    /**
     * creates new Tooltip instance with default font size 15
     *
     * @param pOpenDelay open delay in ms
     * @param pVisibleDuration time before fading in ms
     * @param pCloseDelay close delay in ms
     * @param pHideOnExit hide on exit flag
     */
    public CatalogTooltip(double pOpenDelay, double pVisibleDuration, double pCloseDelay, boolean pHideOnExit) {
        super(pOpenDelay, pVisibleDuration, pCloseDelay, pHideOnExit);

        catalogItemProperty.addListener(new ChangeListener<T>() {
            @Override
            public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
                if (newValue != null) {
                    setContentNode(createContentNode());
                }
            }
        });
    }

    /**
     * creates default tooltip for details with default font size 15 open delay
     * 200ms visibleDuration 5000ms closeDelay 100ms hideOnExit true
     *
     * @param pTitle title to show
     */
    public CatalogTooltip(String pTitle) {
        this(200, Double.POSITIVE_INFINITY, 100, true);
        setTitle(pTitle);
    }

    public abstract T fetchData();

    public abstract Node createContentNode();

    public void setCountry(String pCountry) {
        countryProperty.set(pCountry);
    }

    public String getCountry() {
        return countryProperty.get();
    }

    public void setYearOfValidity(Integer pYear) {
        yearOfValidityProperty.set(pYear);
    }

    public Integer getYearOfValidity() {
        return yearOfValidityProperty.get();
    }

    public void setCatalogItem(T pCatalogItem) {
        catalogItemProperty.set(pCatalogItem);
    }

    public T getCatalogItem() {
        return catalogItemProperty.get();
    }
}
