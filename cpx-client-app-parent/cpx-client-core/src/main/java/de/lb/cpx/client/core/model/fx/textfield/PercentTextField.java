/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.textfield;

import java.util.regex.Pattern;

/**
 * overrides IntegerTextField with Percent properties
 *
 * @author gerschmann
 */
public class PercentTextField extends IntegerTextField {

    private static final String RESTRICTION = "([0-9]*)[%]{1}?$";

    public PercentTextField() {
        super();
        getConverter().setMinValue(0);
        getConverter().setMaxValue(100);
        getConverter().setItemName("%");
        getConverter().setNullString("0%");
        setText("0%");
    }

    @Override
    public void setPattern(String pattern) {
        Pattern pat = Pattern.compile(RESTRICTION);
        setPattern(pat);
    }

}
