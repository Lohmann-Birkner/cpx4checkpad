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

import de.lb.cpx.shared.lang.Lang;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * Implements Bar to show flowrate (Durchsatz) of cases per second in the
 * batchgrouping creates control who handles a vertical progress bar and labels
 * to specefiy upper and lower borders
 *
 * @author wilde
 */
public final class FlowrateBar extends Control {

    private final DoubleProperty progressProperty = new SimpleDoubleProperty(-1);
    private final StringProperty upperBorderProperty = new SimpleStringProperty("");
    private final StringProperty lowerBorderProperty = new SimpleStringProperty("");
    private final StringProperty currentRateProperty = new SimpleStringProperty("");
    private final ReadOnlyBooleanWrapper indeterminate = new ReadOnlyBooleanWrapper(false);
    private final ObjectProperty<Level> levelProperty = new SimpleObjectProperty<>();
    private static final double INDETERMINATE_VALUE = -1;

    /**
     * no arg constructor for scene builder
     */
    public FlowrateBar() {
        this.getStylesheets().add(RingProgressIndicator.class.getResource("/styles/cpx-default.css").toExternalForm());
        getStyleClass().add("flowrate-bar");
        setHeight(200.00);
        setWidth(100.0);
        setProgress(-1);

        levelProperty.addListener(new ChangeListener<Level>() {
            @Override
            public void changed(ObservableValue<? extends Level> observable, Level oldValue, Level newValue) {
                setBorderTexts(newValue);
            }

            private void setBorderTexts(Level newValue) {
                switch (newValue) {
                    case LEVEL100:
                        setUpperBorderText("100");
                        setLowerBorderText("0");
                        break;
                    case LEVEL200:
                        setUpperBorderText("200");
                        setLowerBorderText("100");
                        break;
                    case LEVEL300:
                        setUpperBorderText("300");
                        setLowerBorderText("200");
                        break;
                    case LEVEL400:
                        setUpperBorderText("400");
                        setLowerBorderText("300");
                        break;
                    case LEVEL500:
                        setUpperBorderText("500");
                        setLowerBorderText("400");
                        break;
                    case LEVEL600:
                        setUpperBorderText("600");
                        setLowerBorderText("500");
                        break;
                    case LEVEL700:
                        setUpperBorderText("700");
                        setLowerBorderText("600");
                        break;
                    case LEVEL800:
                        setUpperBorderText("800");
                        setLowerBorderText("700");
                        break;
                    case LEVEL900:
                        setUpperBorderText("900");
                        setLowerBorderText("800");
                        break;
                    case LEVEL1000:
                        setUpperBorderText("1000");
                        setLowerBorderText("900");
                        break;
                    case LEVEL1100:
                        setUpperBorderText("1100");
                        setLowerBorderText("1000");
                        break;
                }
            }
        });
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new FlowrateBarSkin(this);
    }

    /**
     * @return get progress of the flowbar
     */
    public double getProgress() {
        return progressProperty.get();
    }

    /**
     * @param pProgress set progress to value (0-100)
     */
    public void setProgress(double pProgress) {
        if (pProgress > 100) {
            pProgress = 100.0d;
        }
        progressProperty.set(pProgress);
        indeterminate.set(Double.doubleToRawLongBits(pProgress) == Double.doubleToRawLongBits(INDETERMINATE_VALUE));
    }

    /**
     * @return progress property
     */
    public DoubleProperty getProgressProperty() {
        return progressProperty;
    }

    /**
     * @param pText set text to upper border
     */
    public void setUpperBorderText(String pText) {
        upperBorderProperty.set(pText);
    }

    /**
     * @param pText set text to lower border
     */
    public void setLowerBorderText(String pText) {
        lowerBorderProperty.set(pText);
    }

    /**
     * @param pText set the current rate text
     */
    public void setCurrentRateText(String pText) {
        currentRateProperty.set(pText);
    }

    /**
     * @return gets upper border text property
     */
    public StringProperty getUpperBorderTextProperty() {
        return upperBorderProperty;
    }

    /**
     * @return gets lower border text property
     */
    public StringProperty getLowerBorderTextProperty() {
        return lowerBorderProperty;
    }

    /**
     * @return get the current rate text property
     */
    public StringProperty getCurrentRateTextProperty() {
        return currentRateProperty;
    }

    /**
     * @return is bar indeterminate
     */
    public boolean isIndeterminate() {
        return indeterminate.get();
    }

    /**
     * make flowbar indeterminate
     */
    public void indeterminate() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setUpperBorderText("");
                setLowerBorderText("");
                setCurrentRateText("");
                setProgress(INDETERMINATE_VALUE);
            }
        });
    }

//    private void setLevel(Level pLevel) {
//        levelProperty.set(pLevel);
//    }
    /**
     * sets flow presented in the ui there are three different levels 0-100
     * 100-200 200-300 value in flow will be calculated for the specific level
     * and upper and lower border will be set accordingly
     *
     * @param pFlow value of the current flow
     */
    public void setFlow(Double pFlow) {
        setFlow(pFlow, Lang.getFlowrateStatusText(pFlow));
    }

    /**
     * sets flow presented in the ui there are three different levels 0-100
     * 100-200 200-300 value in flow will be calculated for the specific level
     * and upper and lower border will be set accordingly
     *
     * @param pFlow value of the current flow
     * @param pText current rate text to be displayed
     */
    public void setFlow(Double pFlow, String pText) {
        if (pFlow > 0 && pFlow <= 100) {
            levelProperty.set(Level.LEVEL100);
            setProgress(pFlow);
        }
        if (pFlow > 100 && pFlow <= 200) {
            levelProperty.set(Level.LEVEL200);
            setProgress(pFlow - 100);
        }
        if (pFlow > 200 && pFlow <= 300) {
            levelProperty.set(Level.LEVEL300);
            setProgress(pFlow - 200);
        }
        if (pFlow > 300 && pFlow <= 400) {
            levelProperty.set(Level.LEVEL400);
            setProgress(pFlow - 300);
        }
        if (pFlow > 400 && pFlow <= 500) {
            levelProperty.set(Level.LEVEL500);
            setProgress(pFlow - 400);
        }
        if (pFlow > 500 && pFlow <= 600) {
            levelProperty.set(Level.LEVEL600);
            setProgress(pFlow - 500);
        }
        if (pFlow > 600 && pFlow <= 700) {
            levelProperty.set(Level.LEVEL700);
            setProgress(pFlow - 600);
        }
        if (pFlow > 700 && pFlow <= 800) {
            levelProperty.set(Level.LEVEL800);
            setProgress(pFlow - 700);
        }
        if (pFlow > 800 && pFlow <= 900) {
            levelProperty.set(Level.LEVEL900);
            setProgress(pFlow - 800);
        }
        if (pFlow > 900 && pFlow <= 1000) {
            levelProperty.set(Level.LEVEL1000);
            setProgress(pFlow - 900);
        }
        if (pFlow > 1000) {
            levelProperty.set(Level.LEVEL1100);
            setProgress(pFlow - 1000);
        }
        setCurrentRateText(pText);
    }

    private enum Level {
        LEVEL100, LEVEL200, LEVEL300, LEVEL400, LEVEL500, LEVEL600, LEVEL700, LEVEL800, LEVEL900, LEVEL1000, LEVEL1100
    }
}
