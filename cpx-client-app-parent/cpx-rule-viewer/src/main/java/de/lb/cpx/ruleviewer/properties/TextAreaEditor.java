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

import de.lb.cpx.client.core.model.fx.labeled.LabeledTextArea;
import javafx.beans.value.ObservableValue;
import javafx.geometry.VPos;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.AbstractPropertyEditor;

/**
 * Editor to helps handle text add in
 *
 * @author wilde
 */
public class TextAreaEditor extends AbstractPropertyEditor<String, LabeledTextArea> {//implements PropertyEditor<String>{

    private final Integer maxSize = 4000;
    private final PropertySheet.Item item;

    public TextAreaEditor(PropertySheet.Item property) {
        super(property, new LabeledTextArea(""));
        item = property;
        getEditor().setMaxSize(maxSize);
        getEditor().setInfoPosition(VPos.BOTTOM);
    }

    public void setSize(Integer pSize) {
        getEditor().setMaxSize(pSize);
    }

    public Integer getSize() {
        return getEditor().getMaxSize();
    }

    @Override
    protected ObservableValue<String> getObservableValue() {
        return getEditor().getControl().textProperty();
    }

    @Override
    public void setValue(String value) {
        getEditor().getControl().setText(value);
    }

}
