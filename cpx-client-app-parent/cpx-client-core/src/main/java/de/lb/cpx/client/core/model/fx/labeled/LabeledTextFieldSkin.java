/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.labeled;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 *
 * @author wilde
 */
public class LabeledTextFieldSkin extends LabeledControlSkin<TextField> {

    private static final PseudoClass HIDE_CARET_CLASS = PseudoClass.getPseudoClass("hideCaret");

    public LabeledTextFieldSkin(String pLabel, LabeledTextField pControl) {
        super(pLabel, pControl);
        setAdditionalButton(pControl.getAdditionalButton());
        pControl.additionalButtonProperty().addListener(new ChangeListener<Button>() {
            @Override
            public void changed(ObservableValue<? extends Button> ov, Button t, Button t1) {
                setAdditionalButton(t1);
            }
        });

        pControl.showCaretProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                pControl.getControl().pseudoClassStateChanged(HIDE_CARET_CLASS, !t1);
            }
        });
    }

    private void setAdditionalButton(Button pButton) {
        if (getContent().getChildren().size() > 1) {
            //risky should search for some search button to remove correct entry
            getContent().getChildren().remove(1);
        }
        if (pButton == null) {
            return;
        }
        getContent().getChildren().add(pButton);
    }
}
