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

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * Skin class for ring progress indicator sets radius to default 60.0 and ring
 * width to 5.0
 *
 * @author wilde
 */
public class RingProgressIndicatorSkin implements Skin<RingProgressIndicator> {

    private final RingProgressIndicator indicator;
    private final Label percentLabel = new Label();
    private final Label statusLabel = new Label();
    private final Circle circle = new Circle();
    private final StackPane container = new StackPane();
    private final StackPane wrapper = new StackPane();
    private final Arc fillerArc = new Arc();
    private final RotateTransition transition = new RotateTransition(Duration.millis(2000), fillerArc);
    private final DoubleProperty radiusProperty = new SimpleDoubleProperty(60.0);
    private final DoubleProperty ringWidthProperty = new SimpleDoubleProperty(5.0);

    /**
     * creates new skin for control
     *
     * @param indicator control to skin
     */
    public RingProgressIndicatorSkin(final RingProgressIndicator indicator) {
        this.indicator = indicator;
        circle.getStyleClass().add("ringindicator-circle");
        initContainer(indicator);
        initFillerArc();
        initListeners();
        updateRadii();
        initTransition();
        initIndeterminate(indicator.isIndeterminate());
        initPercentLabel(indicator.getProgress());
        initStatusLabel();
    }

    /**
     * @return radius of the ringProgressIndicator
     */
    public Double getRadius() {
        return radiusProperty.get();
    }

    /**
     * @param pRadius set radius
     */
    public void setRadius(double pRadius) {
        radiusProperty.set(pRadius);
    }

    /**
     * @return ring width
     */
    public Double getRingWidth() {
        return ringWidthProperty.get();
    }

    /**
     * @param pRingWidth set ring width
     */
    public void setRingWidth(double pRingWidth) {
        ringWidthProperty.set(pRingWidth);
    }

    @Override
    public RingProgressIndicator getSkinnable() {
        return indicator;
    }

    @Override
    public Node getNode() {
        return wrapper;
    }

    @Override
    public void dispose() {
        transition.stop();
    }

    /*
    *
    * Private Methodes
    *
     */
    private void setProgressLabel(Double value) {
        if (value >= 0) {
            percentLabel.setText(String.format("%d%%", value.intValue()));
        }
    }

    private void initTransition() {
        transition.setAutoReverse(false);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.setDelay(Duration.ZERO);
        transition.setInterpolator(Interpolator.LINEAR);
        transition.setByAngle(360);
    }

    private void initFillerArc() {
        fillerArc.setManaged(false);
        fillerArc.getStyleClass().add("ringindicator-filler");
        fillerArc.setStartAngle(90);
        fillerArc.setLength(indicator.getProgress() * -3.6);
    }

    private void initContainer(final RingProgressIndicator indicator) {
        wrapper.alignmentProperty().bindBidirectional(indicator.alignmentProperty());
        container.getStylesheets().addAll(indicator.getStylesheets());
        container.getStyleClass().addAll("ringindicator-container");
        container.setMaxHeight(Region.USE_PREF_SIZE);
        container.setMaxWidth(Region.USE_PREF_SIZE);
        VBox box = new VBox(percentLabel, statusLabel);
        box.setAlignment(Pos.CENTER);
        container.getChildren().addAll(fillerArc, circle, box);
        wrapper.getChildren().add(container);
//        wrapper.setStyle("-fx-background-color:red");
//        container.setStyle("-fx-background-color:yellow");
    }

    private void updateRadii() {
        circle.setRadius(getRadius() + getRingWidth());
        fillerArc.setRadiusY(getRadius() + (getRingWidth() / 2));
        fillerArc.setRadiusX(getRadius() + (getRingWidth() / 2));
        fillerArc.setStrokeWidth(getRingWidth());
    }

    private void initPercentLabel(double value) {
        setProgressLabel(value);
        percentLabel.getStyleClass().add("ringindicator-label-percent");
    }

    private void initStatusLabel() {
        statusLabel.getStyleClass().add("ringindicator-label-status");
        statusLabel.textProperty().bindBidirectional(indicator.statusProperty());
    }

    private void initIndeterminate(boolean newVal) {
        percentLabel.setVisible(!newVal);
        statusLabel.setVisible(!newVal);
        if (newVal) {
            fillerArc.setLength(360);
            fillerArc.getStyleClass().add("ringindicator-indeterminate");
            if (indicator.isVisible()) {
                transition.play();
            }
        } else {
            fillerArc.getStyleClass().remove("ringindicator-indeterminate");
            fillerArc.setRotate(0);
            transition.stop();
        }
    }

    private void initListeners() {
        container.widthProperty().addListener((o, oldVal, newVal) -> {
            fillerArc.setCenterX(newVal.intValue() / 2d);
        });
        container.heightProperty().addListener((o, oldVal, newVal) -> {
            fillerArc.setCenterY(newVal.intValue() / 2d);
        });

        indicator.indeterminateProperty().addListener((o, oldVal, newVal) -> {
            initIndeterminate(newVal);
            if (newVal) {
                percentLabel.setText("");
                statusLabel.setText("");
            }
        });
        indicator.progressProperty().addListener((o, oldVal, newVal) -> {
            if (newVal.intValue() >= 0) {
                setProgressLabel(newVal.doubleValue());
                fillerArc.setLength(newVal.intValue() * -3.6);
            }
        });
        ringWidthProperty.addListener((o, oldVal, newVal) -> {
            updateRadii();
        });
        indicator.visibleProperty().addListener((o, oldVal, newVal) -> {
            if (newVal && indicator.isIndeterminate()) {
                transition.play();
            } else {
                transition.pause();
            }
        });
    }
}
