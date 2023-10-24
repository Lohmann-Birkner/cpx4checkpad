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
 *    2019  shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.autocompletion;

import de.lb.cpx.client.core.model.fx.labeled.LabeledDatePicker;
import de.lb.cpx.shared.lang.Lang;
import java.io.Serializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author shahin
 * @param <T> type
 */
public class AutocompletionMultiColDatePicker<T extends Serializable> extends LabeledDatePicker implements AutocompletionMultiColInterface<T> {

    private final Autocompletion<T, ?> autocompletion;

    public AutocompletionMultiColDatePicker() {
        autocompletion = new Autocompletion<>(this);
    }

    @Override
    public void setListener() {
        //Hide always by focus-in (optional) and out
        focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            autocompletion.getEntriesPopup().hide();
        });
        setOnKeyReleased((KeyEvent event) -> {
            if (autocompletion.isPopupShowing() && (event.getCode() == KeyCode.BACK_SPACE)) {
                autocompletion.getEntriesPopup().hide();
                //return; //activate this to deactivate search on backspace key
            }

            if ((autocompletion.isPopupShowing() && (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.UP
                    || event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT))
                    || event.getCode() == KeyCode.ESCAPE) {
                return;
            }
            autocompletion.startSearch(event);
        });
    }

    @Override
    public String getText2() {
        return Lang.toDate(getDate());
    }

    @Override
    public Autocompletion<T, ?> getAutocompletion() {
        return autocompletion;
    }

    @Override
    public void selectItem(String[] result, int position) {
        //
    }

}
