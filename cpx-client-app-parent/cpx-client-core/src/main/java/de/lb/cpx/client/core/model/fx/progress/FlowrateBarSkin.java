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

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.control.Skinnable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * creates Skin class for the flow rate bar, handles ui elements of the control
 *
 * @author wilde
 */
public class FlowrateBarSkin implements Skin<Skinnable> {

    private final HBox root = new HBox();
    private final AnchorPane labelArea = new AnchorPane();
    private final AnchorPane progressArea = new AnchorPane();
    private final VerticalProgressBar progressBar = new VerticalProgressBar(200, 40);
    private final Label upperBorder = new Label("");
    private final Label lowerBorder = new Label("");
    private final Label currentRate = new Label("");
    private final FlowrateBar skinnable;

    /**
     * @param pFlowrateBar creates new skin for flowrate bar
     */
    public FlowrateBarSkin(FlowrateBar pFlowrateBar) {
        skinnable = pFlowrateBar;
        setUpLabelArea();
        setUpProgressArea();
        root.setFillHeight(true);
        root.getChildren().addAll(progressArea, labelArea);
        progressBar.indeterminateProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                upperBorder.setText("");
                lowerBorder.setText("");
                currentRate.setText("");
            }
        });
    }

    //init ui for label area ( place labels in anchorpane)
    private void setUpLabelArea() {
        skinnable.getUpperBorderTextProperty().bindBidirectional(upperBorder.textProperty());
        skinnable.getLowerBorderTextProperty().bindBidirectional(lowerBorder.textProperty());
        skinnable.getCurrentRateTextProperty().bindBidirectional(currentRate.textProperty());
        labelArea.getChildren().addAll(upperBorder, currentRate, lowerBorder);
        HBox.setHgrow(labelArea, Priority.ALWAYS);

        AnchorPane.setTopAnchor(upperBorder, 0.0);
        AnchorPane.setLeftAnchor(upperBorder, 0.0);
        AnchorPane.setLeftAnchor(lowerBorder, 0.0);
        AnchorPane.setBottomAnchor(lowerBorder, 0.0);
        AnchorPane.setTopAnchor(currentRate, 0.0);
        AnchorPane.setLeftAnchor(currentRate, 0.0);
        AnchorPane.setBottomAnchor(currentRate, 0.0);

    }

    //init ui for process area (set and bind progress bar)
    private void setUpProgressArea() {
        VBox wrapper = new VBox(new Group(progressBar));
        progressBar.progressProperty().bind(
                Bindings.when(skinnable.getProgressProperty().greaterThan(1))
                        .then(skinnable.getProgressProperty().divide(100))
                        .otherwise(skinnable.getProgressProperty())
        );
        progressArea.getChildren().add(wrapper);
        progressBar.getVerticalHeightProperty().bind(skinnable.heightProperty().add(-10.0));

        AnchorPane.setTopAnchor(wrapper, 5.0);
        AnchorPane.setLeftAnchor(wrapper, 5.0);
        AnchorPane.setRightAnchor(wrapper, 5.0);
        AnchorPane.setBottomAnchor(wrapper, 5.0);
    }

    @Override
    public Skinnable getSkinnable() {
        return skinnable;
    }

    @Override
    public Node getNode() {
        return root;
    }

    @Override
    public void dispose() {
    }

}
