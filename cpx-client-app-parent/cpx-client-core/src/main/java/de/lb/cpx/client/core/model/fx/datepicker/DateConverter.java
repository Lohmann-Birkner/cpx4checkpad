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
package de.lb.cpx.client.core.model.fx.datepicker;

import de.lb.cpx.shared.lang.Lang;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 *
 * @author wilde
 */
public class DateConverter extends StringConverter<LocalDate> {

    private static final Logger LOG = Logger.getLogger(DateConverter.class.getName());

    @Override
    public String toString(LocalDate date) {
        if (date != null) {
            try {
                if (checkDate(date)) {
                    return DateTimeFormatter.ofPattern(Lang.getDateFormat()).format(date);
                }
            } catch (DateTimeException ex) {
                LOG.log(Level.SEVERE, "Invalid date caused an error", ex);
                if (onValidationError != null) {
                    onValidationError.call(Lang.getInputDateError(date.toString()));
                }
            }
        }
        return "";
    }

    @Override
    public LocalDate fromString(String string) {
        if (string != null && !string.isEmpty()) {
            try {
                LocalDate date = LocalDate.parse(string, DateTimeFormatter.ofPattern(Lang.getDateFormat()));
                if (checkDate(date)) {
                    return date;
                }
            } catch (DateTimeException ex) {
                LOG.log(Level.SEVERE, "Invalid date caused an error", ex);
                if (onValidationError != null) {
                    onValidationError.call(Lang.getInputDateError(string));
                }
            }
        }
        return null;
    }

    protected boolean checkDate(LocalDate pDate) throws DateTimeException {
        if (getMinDate() != null) {
            if (pDate.isBefore(getMinDate())) {
                throw new DateTimeException("date is lower than min date");
            }
        }
        if (getMaxDate() != null) {
            if (pDate.isAfter(getMaxDate())) {
                throw new DateTimeException("date is higher than min date");
            }
        }
        return true;
    }
    private Callback<String, Void> onValidationError;

    public void setValidationErrorCallback(Callback<String, Void> pCallback) {
        onValidationError = pCallback;
    }
    private ObjectProperty<LocalDate> minDateProperty;

    public ObjectProperty<LocalDate> minDateProperty() {
        if (minDateProperty == null) {
            minDateProperty = new SimpleObjectProperty<>();
        }
        return minDateProperty;
    }

    public LocalDate getMinDate() {
        return minDateProperty().get();
    }

    public void setMinDate(LocalDate pDate) {
        minDateProperty().set(pDate);
    }
    private ObjectProperty<LocalDate> maxDateProperty;

    public ObjectProperty<LocalDate> maxDateProperty() {
        if (maxDateProperty == null) {
            maxDateProperty = new SimpleObjectProperty<>();
        }
        return maxDateProperty;
    }

    public LocalDate getMaxDate() {
        return maxDateProperty().get();
    }

    public void setMaxDate(LocalDate pDate) {
        maxDateProperty().set(pDate);
    }
}
