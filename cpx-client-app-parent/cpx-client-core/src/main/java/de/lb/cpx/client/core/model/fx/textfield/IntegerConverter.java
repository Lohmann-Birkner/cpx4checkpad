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

import de.lb.cpx.shared.filter.enums.SearchListFormatInteger;
import de.lb.cpx.shared.filter.enums.SearchListFormatNumber;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * Integer ConverterClass handles parsing errors handles min max errors
 *
 * @author wilde
 */
public class IntegerConverter extends StringConverter<Integer> {

    private static final Logger LOG = Logger.getLogger(IntegerConverter.class.getName());

    @Override
    public String toString(Integer pInt) {
        if (pInt != null) {
            try {
                if (checkInteger(pInt)) {
                    return String.valueOf(pInt);
                }
            } catch (NumberFormatException ex) {
                LOG.log(Level.SEVERE, "Invalid date '" + String.valueOf(pInt) + "' caused an error", ex);
                if (onValidationError != null) {
                    onValidationError.call("Wert: " + String.valueOf(pInt) + ", ist nicht gültig");
                }
            }
        }
        return "";
    }

    @Override
    public Integer fromString(String string) {
        if (string != null && !string.isEmpty()) {
            try {
                Integer integer = Integer.valueOf(string);
                if (checkInteger(integer)) {
                    return integer;
                }
            } catch (NumberFormatException ex) {
                LOG.log(Level.SEVERE, "Invalid date '" + string + "' caused an error", ex);
                if (onValidationError != null) {
                    onValidationError.call("Wert: " + string + ", ist nicht gültig");
                }
                return Integer.MIN_VALUE;
            }
        }
        return null;
    }

    protected boolean checkInteger(Integer pValue) throws NumberFormatException {
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
    private ObjectProperty<Integer> minValueProperty;

    public ObjectProperty<Integer> minValueProperty() {
        if (minValueProperty == null) {
            minValueProperty = new SimpleObjectProperty<>();
        }
        return minValueProperty;
    }

    public Integer getMinValue() {
        return minValueProperty().get();
    }

    public void setMinValue(Integer pDate) {
        minValueProperty().set(pDate);
    }
    private ObjectProperty<Integer> maxValueProperty;

    public ObjectProperty<Integer> maxValueProperty() {
        if (maxValueProperty == null) {
            maxValueProperty = new SimpleObjectProperty<>();
        }
        return maxValueProperty;
    }

    public Integer getMaxValue() {
        return maxValueProperty().get();
    }

    public void setMaxValue(Integer pDate) {
        maxValueProperty().set(pDate);
    }
    private ObjectProperty<SearchListFormatInteger.SIGN_TYPE> signTypeProperty;

    public ObjectProperty<SearchListFormatInteger.SIGN_TYPE> signType() {
        if (signTypeProperty == null) {
            signTypeProperty = new SimpleObjectProperty<>(SearchListFormatNumber.SIGN_TYPE.BOTH);
        }
        return signTypeProperty;
    }

    public void setSignType(SearchListFormatNumber.SIGN_TYPE pSignType) {
        signType().set(pSignType);
    }

    public SearchListFormatInteger.SIGN_TYPE getSignType() {
        return signType().get();
    }

}
