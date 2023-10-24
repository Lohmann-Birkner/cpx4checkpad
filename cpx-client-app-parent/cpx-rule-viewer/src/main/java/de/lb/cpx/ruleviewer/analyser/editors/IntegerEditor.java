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

import de.lb.cpx.client.core.model.fx.spinner.NumberSpinner;
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
 * @author wilde
 */
public class IntegerEditor implements PropertyEditor<Integer> {

    private NumberSpinner spinner;
    private final PropertySheet.Item item;
    private Integer oldValue;

    public IntegerEditor(PropertySheet.Item property) {
        item = property;
    }

    @Override
    public Node getEditor() {
        if (spinner == null) {
            spinner = new NumberSpinner(item.getValue() != null ? Integer.parseInt(String.valueOf(item.getValue())) : 0, -10000, 10000);
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
        if (getValue() == null || getValue().equals(oldValue)) {
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
    public Integer getValue() {
        return ((NumberSpinner) getEditor()).getValue();
    }

    @Override
    public void setValue(Integer value) {
        ((NumberSpinner) getEditor()).setInteger(value);
    }

}
