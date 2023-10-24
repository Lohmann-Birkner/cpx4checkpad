/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.textfield;

import de.lb.cpx.shared.lang.Lang;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * Double String converter Class
 *
 * @author wilde
 */
public class DoubleConverter extends StringConverter<Double> {

    private static final Logger LOG = Logger.getLogger(DoubleConverter.class.getName());

    @Override
    public String toString(Double pInt) {
        if (pInt != null) {
            try {
                if (checkDouble(pInt)) {
                    return String.valueOf(pInt).replace(".", Lang.getNumberFormatDecimal());
                }
            } catch (NumberFormatException ex) {
                LOG.log(Level.SEVERE, "Invalid double '" + String.valueOf(pInt) + "' caused an error", ex);
                if (onValidationError != null) {
                    onValidationError.call("Wert: " + String.valueOf(pInt) + ", ist nicht gültig");
                }
            }
        }
        return "";
    }

    @Override
    public Double fromString(String string) {
        if (string != null && !string.isEmpty()) {
            try {
                if(string.contains(Lang.getNumberFormatDecimal())){
                    string = string.replace(Lang.getNumberFormatDecimal(), ".");
                }
                Double integer = Double.valueOf(string);
                if (checkDouble(integer)) {
                    return integer;
                }
            } catch (NumberFormatException ex) {
                LOG.log(Level.SEVERE, "Invalid double '" + string + "' caused an error", ex);
                if (onValidationError != null) {
                    onValidationError.call("Wert: " + string + ", ist nicht gültig");
                    return Double.MIN_VALUE;
                }
            }
        }
        return null;
    }

    protected boolean checkDouble(Double pValue) throws NumberFormatException {
        if (getMinValue() != null) {
            if (pValue < getMinValue()) {
                throw new NumberFormatException("value is lower than min value");
            }
        }
        if (getMaxValue() != null) {
            if (pValue > getMaxValue()) {
                throw new NumberFormatException("value is higher than max value");
            }
        }
        return true;
    }
    private Callback<String, Void> onValidationError;

    public void setValidationErrorCallback(Callback<String, Void> pCallback) {
        onValidationError = pCallback;
    }
    private ObjectProperty<Double> minValueProperty;

    public ObjectProperty<Double> minValueProperty() {
        if (minValueProperty == null) {
            minValueProperty = new SimpleObjectProperty<>();
        }
        return minValueProperty;
    }

    public Double getMinValue() {
        return minValueProperty().get();
    }

    public void setMinValue(Double pDate) {
        minValueProperty().set(pDate);
    }
    private ObjectProperty<Double> maxValueProperty;

    public ObjectProperty<Double> maxValueProperty() {
        if (maxValueProperty == null) {
            maxValueProperty = new SimpleObjectProperty<>();
        }
        return maxValueProperty;
    }

    public Double getMaxValue() {
        return maxValueProperty().get();
    }

    public void setMaxValue(Double pDate) {
        maxValueProperty().set(pDate);
    }
}
