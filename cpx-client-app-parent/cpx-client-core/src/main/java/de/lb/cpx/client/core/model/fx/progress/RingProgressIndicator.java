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

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * Implementation of an Ring Progress Indicator JavaFx ProgressIndicator is not
 * styleable enough to match new layout style class for css is ringindicator
 *
 * @author wilde
 */
public final class RingProgressIndicator extends Control {

    private static final int INDETERMINATE_VALUE = -1;
    private final ReadOnlyDoubleWrapper progress = new ReadOnlyDoubleWrapper(0);
    private final ReadOnlyBooleanWrapper indeterminate = new ReadOnlyBooleanWrapper(false);
    private final StringProperty statusProperty = new SimpleStringProperty("");
    private final ReadOnlyObjectWrapper<Pos> alignmentProperty = new ReadOnlyObjectWrapper<>(Pos.CENTER);

    /**
     * creates new progress indicator with start value
     *
     * @param pProgress start value
     */
    public RingProgressIndicator(Integer pProgress) {
        super();
        this.getStylesheets().add(RingProgressIndicator.class.getResource("/styles/cpx-default.css").toExternalForm());
        setProgress(pProgress);
        indeterminateProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    indeterminate();
                }
            }
        });

    }

    /**
     * no arg constructor for scene builder, sets indicator in indeterminate
     * mode
     */
    public RingProgressIndicator() {
        this(-1);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new RingProgressIndicatorSkin(this);
    }

    /**
     * @return get current Progress
     */
    public double getProgress() {
        return progress.get();
    }

    /**
     * @param pProgressValue set progressValue
     */
    public void setProgress(double pProgressValue) {
        if (pProgressValue > 100) {
            pProgressValue = 100;
        }
        progress.set(pProgressValue);
        indeterminate.set(pProgressValue < 0);
    }

    /**
     * @return progress property
     */
    public ReadOnlyDoubleProperty progressProperty() {
        return progress.getReadOnlyProperty();
    }

    /**
     * @return indicator if indicator is indetermante
     */
    public boolean isIndeterminate() {
        return indeterminate.get();
    }

    /**
     * sets indicator to indetermante
     */
    public void indeterminate() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setProgress(INDETERMINATE_VALUE);
            }
        });
    }

    /**
     * @return indeterminate property
     */
    public BooleanProperty indeterminateProperty() {
        return indeterminate;
    }

    /**
     * @return status text property
     */
    public StringProperty statusProperty() {
        return statusProperty;
    }

    /**
     * @return status text of the progress, is shown beneath percent label if
     * set
     */
    public String getStatusText() {
        return statusProperty.get();
    }

    /**
     * @param pText set status text of the progress, is shown beneath percent
     * label
     */
    public void setStatusText(String pText) {
        statusProperty.set(pText);
    }

    /**
     * @return alignment Property
     */
    public ObjectProperty<Pos> alignmentProperty() {
        return alignmentProperty;
    }

    /**
     * @return alignemnt
     */
    public Pos getAlignment() {
        return alignmentProperty.get();
    }

    /**
     * @param pPos set alignment to posistion
     */
    public void setAlignment(Pos pPos) {
        alignmentProperty.set(pPos);
    }
}
