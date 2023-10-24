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
package de.lb.cpx.client.core.model.fx.dialog;

import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.GridPane;

/**
 * skin class for the dialog pane, handles layout for the content of an dialog
 *
 * @author wilde
 */
public class DialogPaneSkin {

    private final DialogPane dialogPane;
    private Node headerGrid;
    private Node buttonBar;

    /**
     * contruct a new dialogPane skin and sets default layout propertys to the
     * pane overrides old ones
     *
     * @param pDialogPane pane to set style
     */
    public DialogPaneSkin(DialogPane pDialogPane) {
        dialogPane = pDialogPane;
        pDialogPane.getStylesheets().add(getClass().getResource("/styles/cpx-default.css").toExternalForm());
        if (!pDialogPane.getButtonTypes().contains(ButtonType.CANCEL)) {
            pDialogPane.getButtonTypes().add(ButtonType.CANCEL);
        }
        getComponents();
//        if(draggable){
//            getComponents();
//            UndecoratedWindowHelper.enableDragOn(buttonBar, getStage());
//            UndecoratedWindowHelper.enableDragOn(headerGrid, getStage());
//            //when stage is shown set min height and width for content
//            getStage().setOnShowing(new EventHandler<WindowEvent>() {
//                @Override
//                public void handle(WindowEvent event) {
//                    getStage().sizeToScene();
//                    getStage().setMinWidth(dialogPane.getWidth());
//                    getStage().setMinHeight(dialogPane.getHeight());
//                }
//            });
//        }
    }
//    public Stage getStage(){
//        return (Stage) dialogPane.getScene().getWindow();
//    }

    public GridPane getHeaderGrid() {
        return (GridPane) headerGrid;
    }

    public ButtonBar getButtonBar() {
        return (ButtonBar) buttonBar;
    }

    //dummy getting of comonents (headerGrid/buttonbar) 
    //if header is set somewhere else grid will be overwritten
    //risky and workaround to avoid other dumb shit
    protected void getComponents() {
        for (Node node : dialogPane.getChildren()) {
            if (node instanceof GridPane) {
                node.setId("headerTextPanel");
                headerGrid = node;
                continue;
            }
            if (node instanceof ButtonBar) {
                node.setId("buttonBar");
                buttonBar = node;
                continue;
            }
        }
    }

    public void dispose() {
        headerGrid = null;
        buttonBar = null;
        dialogPane.getChildren().clear();
        dialogPane.getButtonTypes().clear();
    }
}
