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
package de.lb.cpx.client.core.model.formatter;

import java.util.regex.Pattern;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

/**
 * numberformatter base class, to allow only numbers in text input fields
 *
 * @author wilde
 */
public class NumberFormatter extends TextFormatter<Integer> {

    /**
     * creates new formatter with format \\d* converter type of
     * IntegerStringConverter
     */
    public NumberFormatter() {
        super(new IntegerStringConverter(), 0, c -> Pattern.matches("-?\\d*", c.getText()) ? c : null);
    }
}
