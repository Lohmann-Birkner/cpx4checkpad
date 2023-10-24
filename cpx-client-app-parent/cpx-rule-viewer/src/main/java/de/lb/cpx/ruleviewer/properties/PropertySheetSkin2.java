/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.properties;

import de.lb.cpx.client.core.model.fx.labeled.LabeledTextArea;
import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import impl.org.controlsfx.skin.PropertySheetSkin;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import org.controlsfx.control.PropertySheet;

/**
 *
 * @author wilde
 */
public class PropertySheetSkin2 extends PropertySheetSkin {

    public PropertySheetSkin2(PropertySheet propertySheet) {
        super(propertySheet);
        ScrollPane pane = (ScrollPane) ((BorderPane) getChildren().get(0)).getCenter();
        pane.setFitToHeight(true);
        for (Node child : ((Pane) pane.getContent()).getChildren()) {
            if (child instanceof LabeledTextArea) {
                GridPane.setVgrow(child, Priority.ALWAYS);
            }
            if (child instanceof Label) {
                GridPane.setValignment(child, VPos.TOP);
            }
        }
        pane.contentProperty().addListener(new ChangeListener<Node>() {
            @Override
            public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
                for (Node child : ((Pane) newValue).getChildren()) {
                    if (child instanceof LabeledTextArea) {
                        GridPane.setVgrow(child, Priority.ALWAYS);
                    }
                    if (child instanceof Label) {
                        Label lbl = (Label) child;
                        PropertySheet.Item item = getItem(lbl.getText());
                        if(item == null){
                            continue;
                        }
                        lbl.setOnMouseEntered(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent t) {
                                lbl.setTooltip(new CpxTooltip(item.getDescription()+"\n"+item.getValue(),0,2000,0,true));
                            }
                        });
                        lbl.setOnMouseExited(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent t) {
                                lbl.setTooltip(null);
                            }
                        });
                    }
                }
            }
        });
    }
    
    public PropertySheet.Item getItem(String pDescription){
        for(PropertySheet.Item item : getSkinnable().getItems()){
            if(item.getDescription().toLowerCase().equals(pDescription.toLowerCase())){
                return item;
            }
        }
        return null;
    }
}
