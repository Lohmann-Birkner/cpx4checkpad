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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import org.apache.commons.text.StringEscapeUtils;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

/**
 *
 * @author wilde
 */
public class FormatedTextEditor implements PropertyEditor<String> {

    private final PropertySheet.Item item;
    private TextField txtField;

    public FormatedTextEditor(PropertySheet.Item property) {
        item = property;
    }

    @Override
    public Node getEditor() {
        if (txtField == null) {
            txtField = new TextField();
            txtField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    item.setValue(getValue());
                }
            });
        }
//        txtField.setTextFormatter(new TextFormatter<>(new StringConverter<String>() {
//            @Override
//            public String toString(String object) {
//                return StringEscapeUtils.unescapeHtml(StringEscapeUtils.unescapeXml(object));
//            }
//
//            @Override
//            public String fromString(String string) {
//                return StringEscapeUtils.escapeHtml(StringEscapeUtils.unescapeXml(string));
//            }
//        }));
        return txtField;
    }

    @Override
    public String getValue() {
        if (((TextField) getEditor()).getText() == null || ((TextInputControl) getEditor()).getText().isEmpty()) {
            return null;
        }
        return StringEscapeUtils.escapeHtml4(StringEscapeUtils.unescapeXml(((TextInputControl) getEditor()).getText()));
    }

    @Override
    public void setValue(String value) {
        ((TextInputControl) getEditor()).setText(StringEscapeUtils.unescapeHtml4(StringEscapeUtils.unescapeXml(value)));
    }

}
