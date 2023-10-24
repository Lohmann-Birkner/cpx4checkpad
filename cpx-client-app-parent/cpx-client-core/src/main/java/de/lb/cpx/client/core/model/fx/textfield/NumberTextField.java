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

import de.lb.cpx.shared.lang.Lang;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

/**
 *
 * @author gerschmann
 */
public class NumberTextField extends TextField {

    private SimpleStringProperty displayValueProperty;

    private Callback<String, String> validateInputCallback = new Callback<String, String>() {
        @Override
        public String call(String param) {
            return param != null ? param/*.replace(Lang.getNumberFormatDecimal(), ".")*/ : null;
        }
    };
    
    private Callback<Void, Void> additionalFunctionCallback;
    public void setAdditionalFunctionCallback(Callback<Void, Void> pCallback){
        additionalFunctionCallback = pCallback;
    }
    public Callback<Void, Void> getAdditionalFunctionCallback(){
        return additionalFunctionCallback;
    }

    private final NumberConverter converter;

    public NumberTextField() {
        converter = new NumberConverter();
        addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    validateValue(getText());
                }
            }
        });
        focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                    LOG.info("textfield focus change from " + oldValue + " to " + newValue);

                if (!newValue) {
                    validateOnFocusLost();
                }
            }
        });
        displayValueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                setValue(converter.fromString(checkTextDigits(newValue)));
            }
        });
        valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setText(checkTextDigits(converter.toString(newValue)));
            }
        });
        setPattern(getPatternString());
        String val = converter.toString(getValue());
        setText(validateInputCallback.call(val));
    }

    public void setPattern(String pPattern) {
        Pattern pattern = Pattern.compile(String.format(pPattern));
        setPattern(pattern);
    }

    protected final void setPattern(Pattern pattern) {

        TextFormatter<String> formatter;
        formatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                if (change.getControlNewText().isEmpty()) {
                    return change;
                }

                if (pattern.matcher(converter.checkWithItemName(change.getControlNewText())).matches()
                        && converter.checkNumber4StringValue(change.getControlNewText())) {
                    return change;
                }
                return null;
            }
        });
        setTextFormatter(formatter);

    }

    protected final void validateValue() {
        validateValue(getText());
    }

    protected final void validateValue(String pNewValue) {
        if (validateInputCallback != null) {
            String value = validateInputCallback.call(pNewValue);
            if (value == null || value.isEmpty()) {
                setText(converter.getNullString());
                setDisplayValue(converter.getNullString());
            } else {
                setDisplayValue(converter.checkWithItemName(value));
                setText(converter.checkWithItemName(value));
            }
        } else {
            setDisplayValue(converter.checkWithItemName(getText()));
            setText(converter.checkWithItemName(getText()));
        }
        if(getAdditionalFunctionCallback() != null){
            getAdditionalFunctionCallback().call(null);
        }
    }

    public final StringProperty displayValueProperty() {
        if (displayValueProperty == null) {
            displayValueProperty = new SimpleStringProperty();
        }
        return displayValueProperty;
    }

    public String getDisplayValue() {
        return displayValueProperty().get();
    }

    public void setDisplayValue(String pValue) {
        displayValueProperty().set(pValue);
    }

    protected void validateOnFocusLost() {
        if (getText() == null || getText().isEmpty()) {
            setDisplayValue(converter.getNullString());
            return;
        }

        if (getText().equals(getDisplayValue())) {
            return;
        }
        validateValue();

    }

    public String checkTextDigits(String text) {
        return text;
    }

    private ObjectProperty<Number> valueProperty;

    public final ObjectProperty<Number> valueProperty() {
        if (valueProperty == null) {
            valueProperty = new SimpleObjectProperty<>();
        }
        return valueProperty;
    }

    public final Number getValue() {
        return valueProperty().get();
    }

    public void setValue(Number pValue) {
        valueProperty().set(pValue == null ? 0 : pValue);
    }
    
    public void setValidateInputCallback(Callback<String, String> pCallback) {
        validateInputCallback = pCallback;
    }

    /**
     * to override for different restriction patterns
     *
     * @return
     */
    protected String getPatternString() {
        return String.format("-?(\\d*(%s-?\\d*)*)", Lang.getNumberFormatDecimal());
    }

    public final NumberConverter getConverter() {
        return converter;
    }

}
