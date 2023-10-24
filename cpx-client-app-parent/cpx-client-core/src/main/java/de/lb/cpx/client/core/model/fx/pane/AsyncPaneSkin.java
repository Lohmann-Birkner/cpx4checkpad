/*
 * Copyright (c) 2018 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.pane;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.AnchorPane;

/**
 * Aync Pane Skin definies layout
 *
 * @author wilde
 * @param <T> node type
 */
public class AsyncPaneSkin<T extends Node> extends SkinBase<AsyncPane<T>> {

    private final AnchorPane root = new AnchorPane();

    public AsyncPaneSkin(AsyncPane<T> pSkinnable) {
        super(pSkinnable);
        getChildren().add(root);

        pSkinnable.contentProperty().addListener(new ChangeListener<Node>() {
            @Override
            public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
                setContentInUi(newValue);
//                root.prefWidth(AnchorPane.USE_COMPUTED_SIZE);
//                root.prefHeight(AnchorPane.USE_COMPUTED_SIZE);
            }
        });
        setContentInUi(pSkinnable.getContent());
        if (pSkinnable.getAutoLoadContent()) {
            pSkinnable.reload();
        } else {
            pSkinnable.setContent(pSkinnable.getLoadingLayout());
        }
    }

    private void setContentInUi(Node newValue) {
        root.getChildren().clear();
        if (newValue == null) {
            return;
        }
        AnchorPane.setTopAnchor(newValue, 0.0);
        AnchorPane.setRightAnchor(newValue, 0.0);
        AnchorPane.setBottomAnchor(newValue, 0.0);
        AnchorPane.setLeftAnchor(newValue, 0.0);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (!root.getChildren().contains(newValue)) {
                    root.getChildren().add(0, newValue);
                }
            }
        });
    }
}
