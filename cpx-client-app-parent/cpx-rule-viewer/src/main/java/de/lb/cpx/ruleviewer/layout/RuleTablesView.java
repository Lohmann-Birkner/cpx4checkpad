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
package de.lb.cpx.ruleviewer.layout;

import de.lb.cpx.rule.criteria.model.CriterionTree.Supergroup.Group.Criterion;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;

/**
 * Display for details in rule tables -Detail display should list all RuleTables
 * -content of the displayed table -when not own rule table, it should be in
 * readonly mode with possibility to create a new copy
 *
 * @author wilde
 */
public class RuleTablesView extends Control implements RuleValidatable{

    protected static final String SAVE_SELECTED_ITEM = "save.selected.item";

    @Override
    protected Skin<?> createDefaultSkin() {
        return new RuleTablesViewSkin1(this);
    }

    private final ObservableList<CrgRuleTables> items = FXCollections.observableArrayList();

    public ObservableList<CrgRuleTables> getItems() {
        return items;
    }
    private Callback<CrgRuleTables, Void> onSelected;

    public void setOnSelectedCallback(Callback<CrgRuleTables, Void> callback) {
        onSelected = callback;
    }

    public Callback<CrgRuleTables, Void> getOnSelected() {
        return onSelected;
    }
    private ObjectProperty<CrgRuleTables> selectedItemProperty;

    public ObjectProperty<CrgRuleTables> selectedItemProperty() {
        if (selectedItemProperty == null) {
            selectedItemProperty = new SimpleObjectProperty<>();
        }
        return selectedItemProperty;
    }

    public void selectItem(CrgRuleTables selectedItem) {
        selectedItemProperty().set(selectedItem);
    }

    public CrgRuleTables getSelectedItem() {
        return selectedItemProperty().get();
    }

    private ObjectProperty<Criterion> criterionProperty;

    public ObjectProperty<Criterion> criterionProperty() {
        if (criterionProperty == null) {
            criterionProperty = new SimpleObjectProperty<>();
        }
        return criterionProperty;
    }

    public Criterion getCriterion() {
        return criterionProperty().get();
    }

    public void setCriterion(Criterion pCriterion) {
        criterionProperty().set(pCriterion);
    }

    public void saveSelectedItem() {
        getProperties().put(SAVE_SELECTED_ITEM, null);
    }

    public boolean hasUnsaved() {
        if (getSkin() instanceof RuleTablesViewSkin1) {
            return ((RuleTablesViewSkin1) getSkin()).hasUnsaved();
        }
        return false;
    }

    public void saveAllUnsaved() {
        if (getSkin() instanceof RuleTablesViewSkin1) {
            ((RuleTablesViewSkin1) getSkin()).saveAllChanges();
        }
    }

    public void discardAllChanges() {
        if (getSkin() instanceof RuleTablesViewSkin1) {
            ((RuleTablesViewSkin1) getSkin()).discardAllChanges();
        }
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

    public void selectInListView(CrgRuleTables pTable) {
        if (getSkin() instanceof RuleTablesViewSkin1) {
            ((RuleTablesViewSkin1) getSkin()).selectRuleTables(pTable);
        }
    }
}
