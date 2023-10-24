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
package de.lb.cpx.client.core.model.fx.labeled;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.CheckBox;

/**
 * implents a labeled Checkbox
 *
 * @author wilde
 */
public class LabeledCheckBox extends LabeledControl<CheckBox> {

    public LabeledCheckBox() {
        this("Label");
    }

    /**
     * creates a new labeled Checkbox
     *
     * @param pLabel label to set above the control
     */
    public LabeledCheckBox(String pLabel) {
        super(pLabel, new CheckBox());
    }

    /**
     * returns current value of the checkbox
     *
     * @return if user checked box or not
     */
    public boolean isChecked() {
        return getControl().isSelected();
    }

    /**
     * return the current value property
     *
     * @return property if the checkBox is checked or not
     */
    public BooleanProperty isCheckedProperty() {
        return getControl().selectedProperty();
    }

    /**
     * return current vlaue of the check box (returns same value as isChecked())
     *
     * @return if checkbox is checked
     */
    public boolean isSelected() {
        return getControl().isSelected();
    }

    /**
     * return the current value property (returns same value as
     * isCheckedProperty())
     *
     * @return property if the checkBox is checked or not
     */
    public BooleanProperty isSelectedProperty() {
        return getControl().selectedProperty();
    }

    /**
     * sets the Checkbox text (Text next to checkbox)
     *
     * @param pText text to set to the checkbox
     */
    public void setCheckBoxText(String pText) {
        getControl().setText(pText);
    }

    /**
     * clears (set empty string) as checkbox text
     */
    public void clearCheckBoxText() {
        getControl().setText("");
    }

    public void setSelected(boolean b) {
        getControl().selectedProperty().set(b);
    }

}
