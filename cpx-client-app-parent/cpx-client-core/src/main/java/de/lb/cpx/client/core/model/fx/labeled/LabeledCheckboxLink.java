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
 *    2018  shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.labeled;

import de.lb.cpx.client.core.model.fx.checkboxlink.CheckboxLink;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * implents a labeled CheckboxLink
 *
 * @author shahin
 */
public class LabeledCheckboxLink extends LabeledControl<CheckboxLink> {

    /**
     * no-arg for scenebuilder, default title is: Label
     */
    public LabeledCheckboxLink() {
        this("Label");
    }

    /**
     * creates a new labeled Checkbox
     *
     * @param pLabel label to set above the control
     */
    public LabeledCheckboxLink(String pLabel) {
        super(pLabel, new CheckboxLink());
    }

    /**
     *
     *
     * @return if user checked CheckboxLink or not
     */
    public boolean isChecked() {
        return getControl().isSelected();

    }

    public void setChecked(boolean b) {
        getControl().setSelectedValue(b);

    }

    /**
     * sets the CheckboxLink text (Text next to checkbox)
     *
     * @param pText text to set to the checkbox
     */
    public void setCheckBoxText(String pText) {
        getControl().setText(pText);
    }

    /**
     * clears (set empty string) as checkboxLink text
     */
    public void clearCheckBoxText() {
        getControl().setText("");
    }

    /**
     * @param handler handler for event to happen on onAction on control
     */
    public void setOnAction(EventHandler<ActionEvent> handler) {
        getControl().setOnAction(handler);
    }

}
