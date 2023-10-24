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

import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * Implementation of the Case Version Tooltip to unify behaviour
 *
 * @author wilde
 */
public class SimpleMathLayout extends GridPane {

    private final String operator;
    private final String result;
    private static final String TEXT_COLOR = "black";
    private static final String EQUALS_SYMBOL ="=";

    /**
     * creates default tooltip for details with default font size 15 open delay
     * 200ms visibleDuration 5000ms closeDelay 100ms hideOnExit true
     *
     * @param pOperator operator to be displayed
     * @param pResult result to show
     * @param pObjects list of line objects for math operation
     */
    public SimpleMathLayout(String pOperator, String pResult, LineObject... pObjects) {
        super();
        operator = pOperator;
        result = pResult;
        populatePane(pOperator, pResult);
    }

    public void setLineObjects(LineObject... pObjects) {
        populatePane(operator, result, pObjects);
    }

    private void populatePane(String pOperator, String pResult, LineObject... pObjects) {
        if (pObjects.length == 0) {
            return;
        }
        getStyleClass().add("default-grid");
        setStyle("-fx-hgap: 5;-fx-vgap: 5;");
        int index;
        for (index = 0; index < pObjects.length; index++) {
            LineObject obj = pObjects[index];
            Label sumText = new Label(obj.getDescription());
            sumText.setStyle("-fx-text-fill:" + TEXT_COLOR);
            Label sum = new Label(obj.getValue());
            sum.setStyle("-fx-text-fill:" + TEXT_COLOR);
            
            Label equals = new Label(EQUALS_SYMBOL);
            equals.setStyle("-fx-text-fill:" + TEXT_COLOR);
            
            GridPane.setHalignment(sum, HPos.RIGHT);
            GridPane.setHgrow(sum, Priority.ALWAYS);
            add(sumText, 1, index);
            add(equals, 2, index);
            add(sum, 3, index);
            if (index > 0) {
                Label lblOperator = new Label(obj.getOpterator()==null?pOperator:obj.getOpterator());
                lblOperator.setStyle("-fx-text-fill:" + TEXT_COLOR);
                add(lblOperator, 0, index);
            }
        }

        Separator sep = new Separator(Orientation.HORIZONTAL);
        Label res = new Label(pResult);
        res.setStyle("-fx-text-fill:" + TEXT_COLOR);
//        Label lblOperator = new Label(pOperator);
//        lblOperator.setStyle("-fx-text-fill:" + TEXT_COLOR);
        
        GridPane.setHalignment(res, HPos.RIGHT);
        GridPane.setHgrow(res, Priority.ALWAYS);

        index++;
        add(sep, 3, index);
        index++;
        add(res, 3, index);
    }

}
