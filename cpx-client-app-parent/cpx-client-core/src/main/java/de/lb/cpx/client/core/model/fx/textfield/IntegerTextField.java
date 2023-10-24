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

/**
 *
 * @author wilde
 */
public class IntegerTextField extends NumberTextField {

    public IntegerTextField() {
        super();
    }

    @Override
    protected String getPatternString() {
        return String.format("-?\\d*");
    }

    public void setMaxValue(Integer pMaxValue) {
        getConverter().setMaxValue(pMaxValue);
    }

    public Integer getMaxValue() {
        return (Integer) getConverter().getMaxValue();
    }

    public void setMinValue(Integer pMinValue) {
        getConverter().setMinValue(pMinValue);
    }

    public Integer getMinValue() {
        return (Integer) getConverter().getMinValue();
    }

}
