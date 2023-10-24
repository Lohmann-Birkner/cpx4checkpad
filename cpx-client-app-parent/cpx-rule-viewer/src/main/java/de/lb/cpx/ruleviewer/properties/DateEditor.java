/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.properties;

import de.lb.cpx.client.core.model.fx.datepicker.FormatedDatePicker;
import de.lb.cpx.ruleviewer.event.RuleChangedEvent;
import de.lb.cpx.shared.lang.Lang;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.DatePicker;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

/**
 * Date editor to set and modify dates
 *
 * @author wilde
 */
public class DateEditor implements PropertyEditor<String> {

    private DatePicker picker;
    private final PropertySheet.Item item;

    public DateEditor(PropertySheet.Item property) {
        item = property;
    }

    @Override
    public Node getEditor() {
        if (picker == null) {
            picker = new FormatedDatePicker();
            picker.getEditor().setStyle("-fx-font-size:14px;");
            picker.valueProperty().addListener(new ChangeListener<LocalDate>() {
                @Override
                public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                    item.setValue(getValue());
//                    item.getValue();
                    RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), this);
                    Event.fireEvent(picker, saveEvent);
                }
            });
            picker.getEditor().textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                    if (t1.isEmpty()) {
                        picker.setValue(null);
                    }
                }
            });
            picker.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                    if (!t1) {
                        item.setValue(getValue());
                        RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), this);
                        Event.fireEvent(picker, saveEvent);
                    }
                }
            });
        }
        return picker;
    }

    @Override
    public String getValue() {
        if (((DatePicker) getEditor()).getValue() == null) {
            return null;
        }
        return Lang.toDate(((DatePicker) getEditor()).getValue());
    }

    @Override
    public void setValue(String value) {
        if (value == null) {
            return;
        }
        if (value.isEmpty()) {
            return;
        }
        ((ComboBoxBase<LocalDate>) getEditor()).setValue(LocalDate.parse(value, DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

}
