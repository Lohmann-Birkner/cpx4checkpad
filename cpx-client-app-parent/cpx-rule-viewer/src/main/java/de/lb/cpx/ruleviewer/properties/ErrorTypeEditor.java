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

import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.ruleviewer.cache.RuleMetaDataCache;
import de.lb.cpx.ruleviewer.event.RuleChangedEvent;
import de.lb.cpx.ruleviewer.layout.RuleView;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.util.StringConverter;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

/**
 *
 * @author wilde
 */
public class ErrorTypeEditor implements PropertyEditor<String> {

    private ComboBox<CrgRuleTypes> combobox;
    private final PropertySheet.Item item;

    public ErrorTypeEditor(PropertySheet.Item property) {
        item = property;
    }

    @Override
    public Node getEditor() {
        if (combobox == null) {
            combobox = new ComboBox<>(FXCollections.observableArrayList(RuleMetaDataCache.instance().getRuleTypes(RuleView.getFacade().getPoolType())));
            ComboBoxListViewSkin<CrgRuleTypes> sk = new ComboBoxListViewSkin<>(combobox);
            combobox.setSkin(sk);
            combobox.setConverter(new StringConverter<CrgRuleTypes>() {
                @Override
                public String toString(CrgRuleTypes object) {
                    if (object == null) {
                        return "";
                    }
                    return object.getCrgtShortText() + " - " + object.getCrgtDisplayText();
                }

                @Override
                public CrgRuleTypes fromString(String string) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
            combobox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CrgRuleTypes>() {
                @Override
                public void changed(ObservableValue<? extends CrgRuleTypes> observable, CrgRuleTypes oldValue, CrgRuleTypes newValue) {
                    item.setValue(getValue());
                    if (sk.getPopupContent() instanceof ListView) {
                        ((ListView) sk.getPopupContent()).scrollTo(newValue);
                    }
                    RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), this);
                    Event.fireEvent(combobox, saveEvent);
                }
            });
        }
        return combobox;
    }

    @Override
    public String getValue() {
        return String.valueOf(((ComboBox<CrgRuleTypes>) getEditor()).getSelectionModel().getSelectedItem().getId());
    }

    @Override
    public void setValue(String value) {
        try {
            int ident = Integer.parseInt(value);
            CrgRuleTypes selected = RuleMetaDataCache.instance().getRuleTypeForId(ident, RuleView.getFacade().getPoolType());
            if (selected != null) {
                ((ComboBox<CrgRuleTypes>) getEditor()).getSelectionModel().select(selected);
            } else {
                ((ComboBox<CrgRuleTypes>) getEditor()).getSelectionModel().selectFirst();
                RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), this);
                Event.fireEvent(combobox, saveEvent);
            }
        } catch (NumberFormatException ex) {
            MainApp.showErrorMessageDialog(ex, "Regeltyp konnte nicht gesetzt werden.\nBitte überpürfen Sie die Regeldefinition, oder setzen Sie den Regeltyp erneut manuel.");
        }
    }

}
