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
package de.lb.cpx.client.core.model.fx.labeled;

import de.lb.cpx.client.core.model.fx.textfield.NumberTextField;
import javafx.scene.control.TextField;

/**
 * Class for a Labeled Number Text field, textfield only allows numbers to be
 * entered by the user
 *
 * @author wilde
 */
public class LabeledNumberTextField extends LabeledTextField {

    /**
     * no-arg constructor for scene builder
     */
    public LabeledNumberTextField() {
        this("Label");
    }

    /**
     * constructor, creates instance with label
     *
     * @param pLabel label text
     */
    public LabeledNumberTextField(String pLabel) {
        this(pLabel, new NumberTextField());
    }

    /**
     * creates new instance
     *
     * @param pLabel label text
     * @param pTextField number textfield to display
     */
    public LabeledNumberTextField(String pLabel, TextField pTextField) {
        super(pLabel, pTextField);
    }

    @Override
    public final NumberTextField getControl() {
        return (NumberTextField) super.getControl();
    }

}
