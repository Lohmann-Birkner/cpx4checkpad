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
package de.lb.cpx.ruleviewer.model.combobox;

import de.lb.cpx.client.core.model.fx.checked_combobox.CpxComboBox;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import javafx.css.PseudoClass;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * Combobox to handle RuleTables Combobox should be searchable and show
 * ruleTable Entities: CrgRuleTables
 *
 * @author wilde
 */
public class RuleTableComboBox extends CpxComboBox<CrgRuleTables> {//ComboBox<CrgRuleTables>{

    public RuleTableComboBox() {
        super();
        setConverter(new StringConverter<CrgRuleTables>() {
            @Override
            public String toString(CrgRuleTables t) {
                return t != null ? t.getCrgtTableName() : "";
            }

            @Override
            public CrgRuleTables fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        setCellFactory(new Callback<ListView<CrgRuleTables>, ListCell<CrgRuleTables>>() {
            @Override
            public ListCell<CrgRuleTables> call(ListView<CrgRuleTables> p) {
                return new RuleTableComboBoxCell();
            }
        });
    }
    
    private class RuleTableComboBoxCell extends ListCell<CrgRuleTables>{
        private final Label title;

        public RuleTableComboBoxCell() {
            super();
            title = new Label();
            setGraphic(title);
        }
        
        @Override
        protected void updateItem(CrgRuleTables item, boolean empty) {
            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
            if(item == null || empty){
                title.setText("");
                return;
            }
            title.setText(getConverter().toString(item));
            title.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"), item.getCrgtMessage()!=null);
        }
        
    }
}
