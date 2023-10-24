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
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.AbstractPropertyEditor;

/**
 *
 * @author wilde
 */
public class RuleTypeChoiceEditor extends AbstractPropertyEditor<RuleTypeEn, ComboBox<RuleTypeEn>> {

    public RuleTypeChoiceEditor(PropertySheet.Item property) {
        super(property, new ComboBox<>(FXCollections.observableArrayList(RuleTypeEn.values())));
        getEditor().setConverter(new StringConverter<RuleTypeEn>() {
            @Override
            public String toString(RuleTypeEn object) {
                return object != null ? object.getTranslation().getValue() : null;
            }

            @Override
            public RuleTypeEn fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    @Override
    protected ObservableValue<RuleTypeEn> getObservableValue() {
        return getEditor().getSelectionModel().selectedItemProperty();
    }

    @Override
    public void setValue(RuleTypeEn value) {
        getEditor().getSelectionModel().select(value);
    }

}
