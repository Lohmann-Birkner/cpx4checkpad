/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.util;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextInputControl;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * implementation of autosave detection
 *
 * @author wilde
 */
@Deprecated(since = "1.05")
public class DelayedFields {

    /**
     * Bind autosave to textInputControll, uses callback to inform about changes
     * when saving should be done, delay can be specified after which seconds
     * the callback should be called
     *
     * @param field textfield to bind autosave to
     * @param callBack callback that is called when saving should be done,
     * String value is the new value of the textfield when delay is reached,
     * boolean indicates if saving was successful
     * @param delayToSave delay after which the callback should be called
     */
    public static void bindDelay(TextInputControl field, Callback<String, Boolean> callBack, int delayToSave) {
        field.textProperty().addListener(new ChangeListener<String>() {
            private Timeline delay;

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (delay != null && delay.statusProperty().isEqualTo(Status.RUNNING).get()) {
                    delay.stop();
                }

                EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (!callBack.call(newValue)) {
//                      LOG.warning("Delay failed for value " + newValue);
                            field.setText(oldValue);
                        }
                    }
                };

                if (delayToSave == 0) {
                    handler.handle(null);
                } else {
                    delay = new Timeline(new KeyFrame(Duration.millis(delayToSave), handler));
                    delay.play();
                }
            }
        });
    }

    /**
     * Bind autosave to textInputControll, uses callback to inform about changes
     * when saving should be done, delay can be specified after which seconds
     * the callback should be called default delay is 5 seconds
     *
     * @param field textfield to bind autosave to
     * @param callBack callback that is called when saving should be done,
     * String value is the new value of the textfield when delay is reached,
     * boolean indicates if saving was successful
     */
    public static void bindDelay(TextInputControl field, Callback<String, Boolean> callBack) {
        bindDelay(field, callBack, 5000);
    }

    private DelayedFields() {
    }
}
