/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
package de.lb.cpx.client.core.model.formatter;

import java.util.regex.Pattern;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.DoubleStringConverter;

/**
 * DoubleFormatter base class, to allow only double numbers in text input fields
 *
 * @author shahin
 */
public class DoubleFormatter extends TextFormatter<Double> {

    /**
     * creates new Double formatter DoubleStringConverter
     */
    public DoubleFormatter() {
        super(new DoubleStringConverter(), 0.0, c -> Pattern.matches("-?\\d*\\.?\\d*", c.getText()) ? c : null);

    }
}
