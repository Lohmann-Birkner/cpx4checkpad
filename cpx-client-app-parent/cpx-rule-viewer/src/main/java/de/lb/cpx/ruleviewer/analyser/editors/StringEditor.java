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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

/**
 * Possible Duplcation of other class de.lb.cpx.ruleviewer.properties.DateEditor
 * Behavior should be unified
 *
 * @author wilde
 */
public class StringEditor implements PropertyEditor<String> {

    private TextField textfield;
    private final PropertySheet.Item item;
    private String oldValue;

    public StringEditor(PropertySheet.Item property) {
        item = property;
    }

    @Override
    public Node getEditor() {
        if (textfield == null) {
            textfield = new TextField(item.getValue() != null ? String.valueOf(item.getValue()) : "");
            oldValue = textfield.getText();
            textfield.setStyle("-fx-font-size:14px;");
//            textfield.textProperty().addListener(new ChangeListener<String>() {
//                @Override
//                public void changed(ObservableValue<? extends String> ov, String t, String t1) {
//                    item.setValue(getValue());
//                    CaseChangedEvent saveEvent = new CaseChangedEvent(CaseChangedEvent.caseChangedEvent());
//                    Event.fireEvent(textfield, saveEvent);
//                }
//            });

            textfield.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue) {
                        updateValue();
                    }
                }

//                private void updateValue() {
//                    if(getValue() == null || getValue().equals(oldValue)){
//                        textfield.setText(oldValue);
//                        textfield.selectAll();
//                        return;
//                    }
//                    item.setValue(getValue());
//                    oldValue = getValue();
//                    CaseChangedEvent saveEvent = new CaseChangedEvent(CaseChangedEvent.caseChangedEvent());
//                    Event.fireEvent(textfield, saveEvent);
//                }
            });
            textfield.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (KeyCode.ENTER.equals(event.getCode())) {
                        event.consume();
                        updateValue();
//                        textfield.getParent().requestFocus();
//                        item.setValue(getValue());
//                        CaseChangedEvent saveEvent = new CaseChangedEvent(CaseChangedEvent.caseChangedEvent());
//                        Event.fireEvent(textfield, saveEvent);
                    }
                }
            });
        }
        return textfield;
    }

    private void updateValue() {
        if (getValue() == null || getValue().equals(oldValue)) {
            textfield.setText(oldValue);
            textfield.selectAll();
            return;
        }
        item.setValue(getValue());
        oldValue = getValue();
        CaseChangedEvent saveEvent = new CaseChangedEvent(CaseChangedEvent.caseChangedEvent());
        Event.fireEvent(textfield, saveEvent);
    }

    @Override
    public String getValue() {
        if (((TextField) getEditor()).getText() == null || ((TextInputControl) getEditor()).getText().isEmpty()) {
            return null;
        }
        return ((TextInputControl) getEditor()).getText();
    }

    @Override
    public void setValue(String value) {
        if (value == null) {
            item.setValue("");
            return;
        }
        ((TextInputControl) getEditor()).setText(value);
    }

}
