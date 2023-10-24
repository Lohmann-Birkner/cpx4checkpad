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
package de.lb.cpx.ruleviewer.model.search;

import de.lb.cpx.ruleviewer.cache.RuleMetaDataCache;
import de.lb.cpx.ruleviewer.layout.RuleValidatable;
import de.lb.cpx.ruleviewer.layout.RuleView;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Skin;
import javafx.util.Callback;

/**
 *
 * @author wilde
 */
public class RuleTableSearchComboBox extends BasicSearchComboBox<CrgRuleTables> implements RuleValidatable{

    public RuleTableSearchComboBox() {
        super();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new RuleTableSearchComboBoxSkin(this);
    }

    @Override
    protected ObservableList<CrgRuleTables> populateItems() {
        ObservableList<CrgRuleTables> list = super.populateItems();
        list.setAll(RuleMetaDataCache.instance().getTablesForPool(RuleView.getFacade().getPool()));//RuleView.getFacade().findRuleTables());//getAllRuleTables());
        return list;
    }
    
    private ObjectProperty<Callback<String,String>> codeSuggestionCallbackProperty;
    private ObjectProperty<Callback<CrgRuleTables,byte[]>> validationCallbackProperty;
    @Override
    public ObjectProperty<Callback<String, String>> codeSuggestionCallbackProperty() {
        if(codeSuggestionCallbackProperty == null){
            codeSuggestionCallbackProperty  =  new SimpleObjectProperty<>();
        }
        return codeSuggestionCallbackProperty;
    }

    @Override
    public Callback<String, String> getCodeSuggestionCallback() {
        return codeSuggestionCallbackProperty().get();
    }

    @Override
    public void setCodeSuggestionCallback(Callback<String, String> pCallback) {
        codeSuggestionCallbackProperty().set(pCallback);
    }

    @Override
    public ObjectProperty<Callback<CrgRuleTables, byte[]>> validationCalllbackProperty() {
        if(validationCallbackProperty == null){
            validationCallbackProperty = new SimpleObjectProperty<>();
        }
        return validationCallbackProperty;
    }

    @Override
    public Callback<CrgRuleTables, byte[]> getValidationCalllback() {
        return validationCalllbackProperty().get();
    }

    @Override
    public void setValidationCalllback(Callback<CrgRuleTables, byte[]> pCallback) {
        validationCalllbackProperty().set(pCallback);
    }

}
