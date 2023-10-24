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

import javafx.scene.control.PasswordField;

/**
 * labeled Passwort Field implementation
 *
 * @author wilde
 */
public class LabeledPasswortField extends LabeledTextControl<PasswordField> {

    /**
     * contruct new empty passwort field
     */
    public LabeledPasswortField() {
        super("LabeledPasswortField", new PasswordField());
    }

    /**
     * creates a new textField with that label
     *
     * @param pLabel label text
     */
    public LabeledPasswortField(String pLabel) {
        this(pLabel, 0);
    }

    /**
     * creates a new textField with that label
     *
     * @param pLabel textArea
     * @param maxSize maximum size (maximum amount of characters)
     */
    public LabeledPasswortField(String pLabel, int maxSize) {
        super(pLabel, new PasswordField());
    }

    /**
     * presets the password
     *
     * @param pPassword password to set
     */
    public void setPassword(String pPassword) {
        getControl().setText(pPassword);
    }

    @Override
    public CharsDisplayMode getDefaultCharsDisplayMode() {
        return CharsDisplayMode.ONLY_WHEN_RESTRICTED;
    }

}
