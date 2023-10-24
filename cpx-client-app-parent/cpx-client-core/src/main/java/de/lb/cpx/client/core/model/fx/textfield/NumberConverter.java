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

import de.lb.cpx.shared.filter.enums.SearchListFormatNumber;
import de.lb.cpx.shared.lang.Lang;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 *
 * @author gerschmann
 */
public  class NumberConverter extends StringConverter<Number> {

    private static final Logger LOG = Logger.getLogger(NumberConverter.class.getName());

    private String nullString = "0"; // default display string

    protected boolean checkNumber(Number pValue) {
        if (pValue == null) {
            return true;
        }
        if (getMinValue() != null) {
            if (pValue instanceof Integer && pValue.intValue() < getMinValue().intValue()) {
                            return false;
            } else if (pValue instanceof Double && pValue.doubleValue() < getMinValue().doubleValue()) {
                         return false;
                    }
                }

        if (getMaxValue() != null) {
            if (pValue instanceof Integer && pValue.intValue() > getMaxValue().intValue()) {
                            return false;
            } else if (pValue instanceof Double && pValue.doubleValue() > getMaxValue().doubleValue()) {
                         return false;
                    }
                }

        return true;
    }

    private Callback<String, Void> onValidationError;

    public void setValidationErrorCallback(Callback<String, Void> pCallback) {
        onValidationError = pCallback;
    }

    private ObjectProperty<Number> minValueProperty;

    public ObjectProperty<Number> minValueProperty() {
        if (minValueProperty == null) {
            minValueProperty = new SimpleObjectProperty<>();
        }
        return minValueProperty;
    }

    public Number getMinValue() {
        return minValueProperty().get();
    }

    public void setMinValue(Number pDate) {
        minValueProperty().set(pDate);
    }
    private ObjectProperty<Number> maxValueProperty;

    public ObjectProperty<Number> maxValueProperty() {
        if (maxValueProperty == null) {
            maxValueProperty = new SimpleObjectProperty<>();
        }
        return maxValueProperty;
    }

    public Number getMaxValue() {
        return maxValueProperty().get();
    }

    public void setMaxValue(Number pDate) {
        maxValueProperty().set(pDate);
    }

     private ObjectProperty<SearchListFormatNumber.SIGN_TYPE> signTypeProperty;

    public ObjectProperty<SearchListFormatNumber.SIGN_TYPE> signType() {
        if (signTypeProperty == null) {
            signTypeProperty = new SimpleObjectProperty<>(SearchListFormatNumber.SIGN_TYPE.BOTH);
        }
        return signTypeProperty;
    }

    public void setSignType(SearchListFormatNumber.SIGN_TYPE pSignType) {
        signType().set(pSignType);
    }

    public SearchListFormatNumber.SIGN_TYPE getSignType() {
        return signType().get();
    }

    private StringProperty itemName;

    public StringProperty itemName() {
        if (itemName == null) {
            itemName = new SimpleStringProperty();
        }
        return itemName;
    }

    public void setItemName(String itemName) {
        itemName().set(itemName);
    }

    public String getItemName() {
        return itemName().get();
    }

    @Override
    public String toString(Number pNumber) {
        if (pNumber != null) {
            try {
                if (checkNumber(pNumber)) {
                    String ret =  String.valueOf(pNumber).replace(".", Lang.getNumberFormatDecimal());
                    if (getItemName() != null) {
                        ret +=  getItemName();
                    }
                    return ret;
                }
            } catch (NumberFormatException ex) {
                LOG.log(Level.SEVERE, "Invalid number '" + pNumber + "' caused an error", ex);
                if (onValidationError != null) {
                    onValidationError.call("Wert: " + pNumber + ", ist nicht gültig");
                }
            }
        }
        return "";
    }

    @Override
    public Number fromString(String pString) {
       if (pString != null && !pString.isEmpty()) {
            try {

                Boolean isDouble = false;
                if(getItemName() != null && pString.endsWith(getItemName())){
                    pString = pString.substring(0, pString.length() - 1);
                }
                if(pString.contains(Lang.getNumberFormatDecimal())){
                    pString = pString.replace(Lang.getNumberFormatDecimal(), ".");
                    if(pString.startsWith(".")){
                        pString = 0 + pString;
                    }
                    if(pString.endsWith(".")){
                        pString += "0";
                    }
                    isDouble = true;
                }    

                Number retNumber = pString.isEmpty()?0.0:(isDouble?Double.parseDouble(pString):Integer.parseInt(pString));
                if (checkNumber(retNumber)) {
                    return retNumber;
                }
            } catch (NumberFormatException ex) {
                LOG.log(Level.SEVERE, "Invalid double '" + pString + "' caused an error", ex);
                if (onValidationError != null) {
                    onValidationError.call("Wert: " + pString + ", ist nicht gültig");
                    return Double.MIN_VALUE;
                }
            }
        }
        return null;
    }

    public boolean  checkNumber4StringValue(String pControlNewText) {
        if (pControlNewText == null || pControlNewText.isEmpty()) {
            return true;
        }
        Number val = fromString(pControlNewText);
        if (val != null) {
            return checkNumber(val);
        }
        return false;

    }

    /**
     * for check of formatter with defined item name
     *
     * @param toCheck
     * @return string
     */
    public String checkWithItemName(String toCheck) {
        if (getItemName() == null || toCheck == null || toCheck.isEmpty() || toCheck.endsWith(getItemName())) {
            return toCheck;
        }
        return toCheck + getItemName();
    }

    public void setNullString(String pNullString) {
        nullString = pNullString;
    }

    public String getNullString() {
        return nullString;
    }
}
