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
package de.lb.cpx.client.core.model.fx.progress;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * Implements vertical ProgressBar Warning: Due to JavaFx not supporting
 * progressBar in vertical mode Use only to set sizes Methodes with the keyword
 * vertical, other methodes and or properties may result in unexpected behavior
 *
 * @author wilde
 */
public final class VerticalProgressBar extends ProgressBar {

    public DoubleProperty vHeightProperty = new SimpleDoubleProperty(0.0);
    public DoubleProperty vWidthProperty = new SimpleDoubleProperty(0.0);

    /**
     * no arg constructor creates new bar with default values for pref height
     * height 200 width 20
     */
    public VerticalProgressBar() {
        this(200, 20);
    }

    /**
     * creates new vertical bar with pref height and pref width
     *
     * @param pHeight pref height of the bar
     * @param pWidth pref width of the bar
     */
    public VerticalProgressBar(double pHeight, double pWidth) {
        super();
        setVerticalHeight(pHeight);
        setVertivalWidth(pWidth);
        vHeightProperty.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setVerticalSizes(newValue.doubleValue(), vWidthProperty.get());
            }
        });
        vWidthProperty.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setVerticalSizes(vHeightProperty.get(), newValue.doubleValue());
            }
        });
    }

    /**
     * @return current vertical width
     */
    public Double getVerticalWidth() {
        return vWidthProperty.get();
    }

    /**
     * @return current vertical height
     */
    public Double getVerticalHeight() {
        return vHeightProperty.get();
    }

    /**
     * @param pVerticalHeight set vertical height
     */
    public void setVerticalHeight(Double pVerticalHeight) {
        vHeightProperty.set(pVerticalHeight);
    }

    /**
     * @param pVerticalWidth set vertical width
     */
    public void setVertivalWidth(Double pVerticalWidth) {
        vWidthProperty.set(pVerticalWidth);
    }

    /**
     * @return vertical width property
     */
    public DoubleProperty getVerticalWidthProperty() {
        return vWidthProperty;
    }

    /**
     * @return vertical height
     */
    public DoubleProperty getVerticalHeightProperty() {
        return vHeightProperty;
    }

    /**
     * WARNING: Is control is rotated using this height value might result in
     * unexpected behavior
     *
     * @param value set Height
     */
    @Override
    protected void setHeight(double value) {
        super.setHeight(value); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * WARNING: Is control is rotated using this width value might result in
     * unexpected behavior
     *
     * @param value set width
     */
    @Override
    protected void setWidth(double value) {
        super.setWidth(value); //To change body of generated methods, choose Tools | Templates.
    }

    //compute vertical sizes
    private void setVerticalSizes(double pHeight, double pWidth) {
        setMinSize(StackPane.USE_PREF_SIZE, StackPane.USE_PREF_SIZE);
        setPrefSize(pHeight, pWidth);
        setMaxSize(StackPane.USE_PREF_SIZE, StackPane.USE_PREF_SIZE);
        getTransforms().setAll(
                new Translate(0, pHeight),
                new Rotate(-90, 0, 0)
        );
    }
}
