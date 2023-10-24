/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.analyser.editors;

import de.lb.cpx.client.core.model.fx.spinner.DoubleSpinner;
import java.math.BigDecimal;
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
public class DoubleEditor implements PropertyEditor<Double> {

    private DoubleSpinner spinner;
    private final PropertySheet.Item item;
    private double oldValue;

    public DoubleEditor(PropertySheet.Item property) {
        item = property;
    }

    @Override
    public Node getEditor() {
        if (spinner == null) {
            spinner = new DoubleSpinner(Double.parseDouble(String.valueOf(item.getValue() != null ? item.getValue() : 0.0d)), 0.0d, 1000.0d, 1);
            oldValue = spinner.getValue();
            spinner.getEditor().setStyle("-fx-font-size:14px;");
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
            spinner.getEditor().addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
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
            spinner.getValueFactory().setValue(oldValue);
            spinner.getEditor().selectAll();
            return;
        }
        item.setValue(getValue());
        oldValue = getValue();
        CaseChangedEvent saveEvent = new CaseChangedEvent(CaseChangedEvent.caseChangedEvent());
        Event.fireEvent(spinner, saveEvent);
    }

    @Override
    public Double getValue() {
        return ((DoubleSpinner) getEditor()).getValue();
    }

    @Override
    public void setValue(Double value) {
        ((DoubleSpinner) getEditor()).getValueFactory().setValue(value);
    }

}
