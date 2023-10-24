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

import de.lb.cpx.client.core.model.async.IAsyncObject;
import de.lb.cpx.shared.lang.Lang;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author wilde
 * @param <T> node type
 */
public class LoadingPane<T extends Node> extends VBox {

    public LoadingPane(IAsyncObject<T> pAsync) {
        setAlignment(Pos.CENTER);
        ProgressIndicator pi = new ProgressIndicator(-1);
//            pi.setMinHeight(100d);
        pi.getStyleClass().add("async-progress-indicator");
        setAlignment(Pos.CENTER);
        Label status = new Label(Lang.getPleaseWait());
        getChildren().addAll(pi, status);
        setSpacing(10.0);
        if (pAsync.isAbortable()) {
            Button button = new Button(Lang.getCancel());
            getChildren().add(button);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (pAsync.getTask() != null) {
                        pAsync.getTask().stop();
                    } else {
                        pAsync.afterTask(Worker.State.CANCELLED);
                    }
                }
            });
        }
        parentProperty().addListener(new ChangeListener<Parent>() {
            @Override
            public void changed(ObservableValue<? extends Parent> observable, Parent oldValue, Parent newValue) {
                if (newValue != null && newValue instanceof Pane) {
//                    pi.minHeightProperty().bind(((Pane) newValue).widthProperty().divide(5));
                }
            }
        });
    }
}
