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
package de.lb.cpx.ruleviewer.properties;

import de.lb.cpx.ruleviewer.event.RuleChangedEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

/**
 *
 * @author wilde
 */
public class StringEditor implements PropertyEditor<String> {

    private TextField textfield;
    private final PropertySheet.Item item;

    public StringEditor(PropertySheet.Item property) {
        item = property;
    }

    @Override
    public Node getEditor() {
        if (textfield == null) {
            textfield = new TextField();
            textfield.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                    item.setValue(getValue());
                    RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), this);
                    Event.fireEvent(textfield, saveEvent);
                }
            });
        }
        return textfield;
    }
    protected final TextField getTextField(){
        return (TextField) getEditor();
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
        ((TextInputControl) getEditor()).setText(value);
    }

}
