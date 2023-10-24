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

import de.lb.cpx.model.enums.RuleTypeEn;
import de.lb.cpx.ruleviewer.event.RuleChangedEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

/**
 *
 * @author wilde
 */
public class RuleTypeChoiceEditor2 implements PropertyEditor<String> {

    private ComboBox<RuleTypeEn> editor;
    private final PropertySheet.Item item;

    public RuleTypeChoiceEditor2(PropertySheet.Item property) {
        item = property;
    }

    @Override
    public Node getEditor() {
        if (editor == null) {
            editor = new ComboBox<>(FXCollections.observableArrayList(RuleTypeEn.values()));
            editor.setConverter(new StringConverter<RuleTypeEn>() {
                @Override
                public String toString(RuleTypeEn object) {
                    return object != null ? object.getTranslation().toString() : null;
                }

                @Override
                public RuleTypeEn fromString(String string) {
                    return null;
                }
            });
            editor.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<RuleTypeEn>() {
                @Override
                public void changed(ObservableValue<? extends RuleTypeEn> observable, RuleTypeEn oldValue, RuleTypeEn newValue) {
                    item.setValue(getValue());
                    RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), this);
                    Event.fireEvent(editor, saveEvent);
                }
            });
        }
        return editor;
    }

    @Override
    public String getValue() {
        return getType(getComboBox().getSelectionModel().getSelectedItem());
    }

    public ComboBox<RuleTypeEn> getComboBox() {
        return (ComboBox<RuleTypeEn>) getEditor();
    }

    @Override
    public void setValue(String value) {
        RuleTypeEn type = getType(value);
        ((ComboBox<RuleTypeEn>) getEditor()).getSelectionModel().select(type);
    }

    private RuleTypeEn getType(String value) {
        if (value == null) {
            return null;
        }
        switch (value) {
            case "error":
                return RuleTypeEn.STATE_ERROR;
            case "warning":
                return RuleTypeEn.STATE_WARNING;
            case "suggestion":
                return RuleTypeEn.STATE_SUGG;
            case "no":
                return RuleTypeEn.STATE_NO;
            default:
                return null;
        }
    }

    private String getType(RuleTypeEn pType) {
        if (pType == null) {
            return "";
        }
        switch (pType) {
            case STATE_ERROR:
                return "error";
            case STATE_WARNING:
                return "warning";
            case STATE_SUGG:
                return "suggestion";
            case STATE_NO:
                return "no";
            default:
                return "";
        }
    }
}
