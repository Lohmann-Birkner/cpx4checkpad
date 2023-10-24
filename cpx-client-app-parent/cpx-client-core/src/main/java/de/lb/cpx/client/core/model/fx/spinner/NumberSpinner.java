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
package de.lb.cpx.client.core.model.fx.spinner;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

/**
 * NumberSpinner for Cpx, Numberrange is 0 to 10000
 *
 * @author wilde
 */
public class NumberSpinner extends CpxSpinner<Integer> {

    private static final Logger LOG = Logger.getLogger(NumberSpinner.class.getName());
    //default alignment center left, 'cause default oriantation in the application
    private ObjectProperty<Pos> textAlignmentProperty = new SimpleObjectProperty<>(Pos.CENTER_LEFT);

    public NumberSpinner(Integer initialValue, Integer minValue, Integer maxValue) {
        super(String.valueOf(initialValue), new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue, initialValue));
        setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                return object == null ? "" : String.valueOf(object);
            }

            @Override
            public Integer fromString(String string) {
                try {
                    Integer val = parse(string);
                    getCurrentValue().set(val);
                    return val;
                } catch (NumberFormatException ex) {
                    LOG.log(Level.WARNING, "This is a not valid number: " + string, ex);
                }
                setText(String.valueOf(getCurrentValue().get()));
                return getCurrentValue().get();
            }
        });
        getCurrentValue().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue != null) {
                    setText(String.valueOf(newValue));
                } else {
                    getCurrentValue().set(oldValue.intValue());
                }
            }
        });
        getEditor().addEventHandler(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("-?\\d*")) {
                event.consume();
            }
        });
        getEditor().alignmentProperty().bindBidirectional(textAlignmentProperty);
    }

    /**
     * Construct new NumberSpinner instance with initialValue
     *
     * @param initialValue initialValue set in Spinner
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException invalid argument
     */
    public NumberSpinner(Integer initialValue) {
        this(initialValue, 0, 10000);
    }

    /**
     * Construct new NumberSpinner instance with initialValue 0
     *
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException illegal argument
     */
    public NumberSpinner() {
        this(0);//,new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000,Integer.parseInt("0")));
    }

    /**
     * get last valid IntegerProperty of the Spinner
     *
     * @return Integerproperty that contains last valid Integer
     */
    public ObjectProperty<Integer> getIntegerProperty() {
        return getCurrentValue();
    }

    /**
     * get last valid IntegerProperty of the Spinner
     *
     * @return Integerproperty that contains last valid Integer
     */
    public Integer getInteger() {
        return getCurrentValue().get();
    }

    public void setInteger(Integer integer) {
//        getEditor().setText(String.valueOf(integer));
        if (integer == null) {
            integer = 0;
        }
        getCurrentValue().setValue(integer);
    }

    /**
     * redirect to getInteger()
     *
     * @return value
     */
    public Integer getNumber() {
        return getInteger();
    }

    /**
     * redirect to setInteger
     *
     * @param pValue value
     */
    public void setNumber(final Number pValue) {
        setInteger(pValue == null ? null : pValue.intValue());
    }

    /**
     * redirect to setInteger()
     *
     * @param pValue value
     */
    public void setValue(final Integer pValue) {
        setInteger(pValue);
    }

    /**
     * tries to parse value as integer
     *
     * @param pValue value
     */
    public void setValue(final String pValue) {
        String value = pValue == null ? "" : pValue.trim();
        Integer val = null;
        if (!value.isEmpty()) {
            try {
                val = parse(value);
            } catch (NumberFormatException ex) {
                LOG.log(Level.WARNING, "This is a not valid number: " + value, ex);
            }
        }
        setInteger(val);
    }

    @Override
    public final Integer parse(String pValue) {
        if (pValue == null || pValue.trim().isEmpty()) {
            return null;
        }
        return Integer.parseInt(pValue.trim());
    }

    /**
     * @param pPos new position of the text alignment in the textfield editor
     */
    public void setTextAlignment(Pos pPos) {
        textAlignmentProperty.set(pPos);
    }

    /**
     * @return currently set text alignment in the textfield editor
     */
    public Pos getTextAlignment() {
        return textAlignmentProperty.get();
    }

    /**
     * @return textalignment property for bindings
     */
    public ObjectProperty<Pos> textAlignmentProperty() {
        return textAlignmentProperty;
    }

}
