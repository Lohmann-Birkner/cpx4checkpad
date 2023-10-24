/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.labeled;

import de.lb.cpx.client.core.model.fx.datepicker.DateConverter;
import de.lb.cpx.client.core.model.fx.datepicker.FormatedDatePicker;
import de.lb.cpx.shared.lang.Lang;
import java.time.LocalDate;
import java.util.Date;
import javafx.beans.property.ObjectProperty;
import javafx.util.Callback;

/**
 * creates a Labeled Datepicker
 *
 * @author wilde
 */
public class LabeledDatePicker extends LabeledControl<FormatedDatePicker> {

    /**
     * no-arg, sets titleLabel to Label, used for sceneBuilder
     */
    public LabeledDatePicker() {
        this("Label");
    }

    /**
     * construct new LabeldDatePicker with a Title
     *
     * @param pLabel label title
     */
    public LabeledDatePicker(String pLabel) {
        super(pLabel, new FormatedDatePicker());
        controlProperty.getValue().setPromptText(Lang.getDateFormat());
        DateConverter converter = new DateConverter();
        converter.setValidationErrorCallback(new Callback<String, Void>() {
            @Override
            public Void call(String param) {
                showErrorPopOver(param);
                return null;
            }
        });
        controlProperty.getValue().setConverter(converter);
    }

    public LocalDate convert(String pDate) {
        return getControl().getConverter().fromString(pDate);
    }

    /**
     * sets a localDate as value
     *
     * @param pDate local date to set
     */
    public void setLocalDate(LocalDate pDate) {
        getControl().setValue(pDate);
    }

    /**
     * sets a java.util.Date Object as value
     *
     * @param pDate set the date in the datepicker
     */
    public void setDate(Date pDate) {
        getControl().setValue(Lang.toLocalDate(pDate));
    }

    /**
     * the the curren value as localDate
     *
     * @return current value as localDate
     */
    public LocalDate getLocalDate() {
        return getControl().getValue();
    }

    /**
     * get the current Vlaue as java.util.date parsed by java.sql.Date.valueOf()
     *
     * @return current value as date
     */
    public Date getDate() {
        if (getLocalDate() == null) {
            return null;
        }
        return java.sql.Date.valueOf(getLocalDate());
    }

//    /*
//    *
//    * PRIVATE METHODES
//    *
//     */
//    private String getPattern(String pSubString) {
//        switch (pSubString.length()) {
//            case 2:
//                return "\\d\\d";
//            case 4:
//                return "\\d\\d\\d\\d";
//            default:
//                return "-1";
//        }
//    }
    /**
     * convert the current value in the editor textfield to a localdate and sets
     * it in the value property
     */
    public void convert() {
        getControl().setValue(convert(getControl().getEditor().getText()));
    }

    public ObjectProperty<LocalDate> valueProperty() {
        return getControl().valueProperty();
    }
}
