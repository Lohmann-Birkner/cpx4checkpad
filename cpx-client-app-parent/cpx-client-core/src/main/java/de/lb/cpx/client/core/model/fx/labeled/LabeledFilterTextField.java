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
package de.lb.cpx.client.core.model.fx.labeled;

import de.lb.cpx.client.core.model.fx.textfield.FilterTextField;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 *
 * @author wilde
 * @param <T> Object type of filtered value
 */
public class LabeledFilterTextField<T>  extends LabeledTextField {
    private static final StringConverter DEFAULT_CONVERTER = new StringConverter<>() {
        @Override
        public String toString(Object object) {
            if(object == null){
                return null;
            }
            return String.valueOf(object);
        }

        @Override
        public Object fromString(String string) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    };
    
    public LabeledFilterTextField() {
        this("LabeledFilterTextField", new FilterTextField(),DEFAULT_CONVERTER);
    }

    public LabeledFilterTextField(String pLabel, FilterTextField pCtrl,StringConverter<T> pConverter) {
        super(pLabel, pCtrl);
        setConverter(Objects.requireNonNullElse(pConverter, DEFAULT_CONVERTER));
        setValue(getConverter().fromString(pCtrl.getText()));
        pCtrl.setMaxHeight(TextField.USE_PREF_SIZE);
        pCtrl.setMinHeight(TextField.USE_PREF_SIZE);
        pCtrl.setMinWidth(TextField.USE_PREF_SIZE);
        pCtrl.setMaxWidth(TextField.USE_PREF_SIZE);

        pCtrl.setPrefWidth(150);
        pCtrl.setPrefHeight(29);
        setMaxHeight(TextField.USE_PREF_SIZE);
        setMinHeight(TextField.USE_PREF_SIZE);
        setMinWidth(TextField.USE_PREF_SIZE);
        setMaxWidth(TextField.USE_PREF_SIZE);
        pCtrl.filterValueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                valueProperty().set(converter.fromString(getEditor().getText()));
            }
        });
        pCtrl.setValidateCallback(new Callback<String, String>() {
            @Override
            public String call(String param) {
                T value = converter.fromString(param);
                if (value == null) {
                    return null;
                }
                if(value instanceof Integer && isIntegerMinValue((Integer) value)){
//                    if (((Integer)value) == Integer.MIN_VALUE) {
                        return "";
//                    }
                }
                if(value instanceof Double && isDoubleMinValue((Double) value)){
//                    if (Double.doubleToRawLongBits(((Double)value)) == Double.doubleToRawLongBits(Double.MIN_VALUE)) {
                        return "";
//                    }
                }
                return String.valueOf(value);//getEditor().getText();
            }
        });
        valueProperty().addListener(new ChangeListener<T>() {
            @Override
            public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
                getEditor().setText(converter.toString(newValue));
            }
        });
    }
    private boolean isDoubleMinValue(Double pDouble){
        return Double.doubleToRawLongBits(pDouble) == Double.doubleToRawLongBits(Double.MIN_VALUE);
    }
    private boolean isIntegerMinValue(Integer pInt){
        return pInt == Integer.MIN_VALUE;
    }
    private StringConverter<T> converter;

    public final StringConverter<T> getConverter() {
        return converter;
    }

    public final void setConverter(StringConverter<T> pConverter) {
        converter = pConverter;
    }
    
    private ObjectProperty<T> valueProperty;

    public final ObjectProperty<T> valueProperty() {
        if (valueProperty == null) {
            valueProperty = new SimpleObjectProperty<>();
        }
        return valueProperty;
    }

    public T getValue() {
        return valueProperty().get();
    }

    public final void setValue(T pValue) {
        valueProperty().set(pValue);
    }

    public final TextField getEditor() {
        return getControl();
    }

    @Override
    public FilterTextField getControl() {
        return (FilterTextField) super.getControl();
    }
    
    
}
