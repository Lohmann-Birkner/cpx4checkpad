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
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

/**
 * Editor to manipulate simple bool values in Analyser
 *
 * @author wilde
 */
public class BooleanEditor implements PropertyEditor<Boolean> {

    private ComboBox<BooleanEditorValue> editor;
    private final PropertySheet.Item item;

    public BooleanEditor(PropertySheet.Item property) {
        item = property;
    }

    @Override
    public Node getEditor() {
        if (editor == null) {
            editor = new ComboBox<>(FXCollections.observableArrayList(BooleanEditorValue.values()));
            editor.setConverter(new StringConverter<BooleanEditorValue>() {
                @Override
                public String toString(BooleanEditorValue object) {
                    return object != null ? object.getDisplayText() : null;
                }

                @Override
                public BooleanEditorValue fromString(String string) {
                    return BooleanEditorValue.getValue(string);
                }
            });
            editor.getSelectionModel().select(item != null ? BooleanEditorValue.getValue((Boolean) item.getValue()) : null);
            editor.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BooleanEditorValue>() {
                @Override
                public void changed(ObservableValue<? extends BooleanEditorValue> observable, BooleanEditorValue oldValue, BooleanEditorValue newValue) {
                    item.setValue(getValue());
                    CaseChangedEvent changeEvent = new CaseChangedEvent(CaseChangedEvent.caseChangedEvent());
                    Event.fireEvent(editor, changeEvent);
                }
            });
            editor.setMaxWidth(Double.MAX_VALUE);
        }
        return editor;
    }

    @Override
    public Boolean getValue() {
        return ((ComboBox<BooleanEditorValue>) getEditor()).getSelectionModel().getSelectedItem().getValue();
    }

    @Override
    public void setValue(Boolean value) {
        editor.getSelectionModel().select(BooleanEditorValue.getValue(value));
    }

    private enum BooleanEditorValue {
        TRUE("Ja", true), FALSE("Nein", false);
        private final Boolean value;
        private final String displayText;

        private BooleanEditorValue(String pDisplayText, Boolean pValue) {
            value = pValue;
            displayText = pDisplayText;
        }

        public Boolean getValue() {
            return value;
        }

        public String getDisplayText() {
            return displayText;
        }

        public static BooleanEditorValue getValue(Boolean pBool) {
            if (pBool == null) {
                return BooleanEditorValue.FALSE;
            }
            for (BooleanEditorValue val : values()) {
                if (val.getValue().equals(pBool)) {
                    return val;
                }
            }
            return null;
        }

        public static BooleanEditorValue getValue(String pDisplayText) {
            for (BooleanEditorValue val : values()) {
                if (val.getDisplayText().equals(pDisplayText)) {
                    return val;
                }
            }
            return null;
        }
    }
}
