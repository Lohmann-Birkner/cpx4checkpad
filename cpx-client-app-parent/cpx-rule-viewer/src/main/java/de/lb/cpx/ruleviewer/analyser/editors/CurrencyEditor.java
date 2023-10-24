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
package de.lb.cpx.ruleviewer.analyser.editors;

import de.lb.cpx.client.core.model.fx.textfield.CurrencyTextField;
import java.math.BigDecimal;
import java.util.Objects;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

/**
 *
 * @author wilde
 */
public class CurrencyEditor implements PropertyEditor<Double> {

//    private DoubleSpinner spinner;
    private final PropertySheet.Item item;
    private double oldValue;
    private CurrencyTextField spinner;

    public CurrencyEditor(PropertySheet.Item property) {
        item = property;
    }

    @Override
    public Node getEditor() {
        if (spinner == null) {
            spinner = new CurrencyTextField();
//            spinner = new DoubleSpinner(Double.parseDouble(String.valueOf(item.getValue() != null ? item.getValue() : 0.0d)), 0.0d, 1000.0d, 1);
            spinner.setValue(getNumberValue());
            oldValue = spinner.getValue().doubleValue();//spinner.getValue()!=null?spinner.getValue().doubleValue():0.0;
            spinner.setStyle("-fx-font-size:14px;");
            spinner.setMaxWidth(Double.MAX_VALUE);
//            spinner.valueProperty().addListener(new ChangeListener<Integer>() {
//                @Override
//                public void changed(ObservableValue<? extends Integer> ov, Integer t, Integer t1) {
//                    item.setValue(getValue());
//                    CaseChangedEvent changeEvent = new CaseChangedEvent(CaseChangedEvent.caseChangedEvent());
//                    Event.fireEvent(spinner, changeEvent);
//                }
//            });
            spinner.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue) {
                        updateValue();
                    }
                }
            });
            spinner.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (KeyCode.ENTER.equals(event.getCode())) {
                        event.consume();
                        updateValue();
                    }
                }
            });
        }
        return spinner;
    }

    private void updateValue() {
        if (getValue() == null || BigDecimal.valueOf(getValue()).equals(BigDecimal.valueOf(oldValue))) {
            spinner.setValue(oldValue);
            spinner.selectAll();
            return;
        }
        item.setValue(getValue());
        oldValue = getValue();
        CaseChangedEvent saveEvent = new CaseChangedEvent(CaseChangedEvent.caseChangedEvent());
        Event.fireEvent(spinner, saveEvent);
    }

    @Override
    public Double getValue() {
        return ((CurrencyTextField) getEditor()).getValue().doubleValue();
    }

    @Override
    public void setValue(Double value) {
        ((CurrencyTextField) getEditor()).setValue(value);
    }
    
    public Number getNumberValue() {
        return Double.valueOf(Objects.requireNonNullElse(valueToString(),"0.0"));
    }
    
    public String valueToString(){
        if(item.getValue() == null){
            return null;
        }
        return String.valueOf(item.getValue());
    }
}
