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

import de.lb.cpx.rule.criteria.model.CriterionTree;
import de.lb.cpx.rule.criteria.model.TypesAndOperations;
import de.lb.cpx.shared.json.RuleMessage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.controlsfx.control.PropertySheet;

/**
 * Editor for condition Value in suggestion
 *
 * @author wilde
 */
public class SuggestionCondValueEditor extends SuggestionValueEditor {

    protected static final String DATATYPE_ARRAY_STRING = "DATATYPE_ARRAY_STRING";
    protected static final String ACTIONTYPE_ADD = "1";

    public SuggestionCondValueEditor(PropertySheet.Item item) {
        super(item);
        getSuggestion().actionIdProperty().addListener(new ChangeListener<TypesAndOperations.SuggActions.SuggAction>() {
            @Override
            public void changed(ObservableValue<? extends TypesAndOperations.SuggActions.SuggAction> observable, TypesAndOperations.SuggActions.SuggAction oldValue, TypesAndOperations.SuggActions.SuggAction newValue) {
                suggestion.setConditionActive(isActive());
//                getEditor().setDisable(false);
            }
        });
        getSuggestion().criterionProperty().addListener(new ChangeListener<CriterionTree.Supergroup.Group.Criterion>() {
            @Override
            public void changed(ObservableValue<? extends CriterionTree.Supergroup.Group.Criterion> observable, CriterionTree.Supergroup.Group.Criterion oldValue, CriterionTree.Supergroup.Group.Criterion newValue) {
                suggestion.setConditionActive(isActive());
            }
        });
        getSuggestion().conditionActiveProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                disable(!newValue);
            }
        });
        getSuggestion().conditionOperatorProperty().addListener(new ChangeListener<TypesAndOperations.OperationGroups.OperationGroup.Operation>() {
            @Override
            public void changed(ObservableValue<? extends TypesAndOperations.OperationGroups.OperationGroup.Operation> observable, TypesAndOperations.OperationGroups.OperationGroup.Operation oldValue, TypesAndOperations.OperationGroups.OperationGroup.Operation newValue) {
                saveLastValueForType(OperationType.castOperation(oldValue), getValue());
                String val = getLastValueForOperationType(OperationType.NONE);
                if(isEmptyOperator(oldValue) && (val!=null && !val.isEmpty())){
                    val = getLastValueForOperationType(OperationType.NONE);
                }else{
                    val = getLastValueForOperationType(OperationType.castOperation(newValue));//isEmptyOperator(oldValue)?getLastValueForOperationType(OperationType.NONE)
                }
                setValue(val);
                disable(isActive());
                refreshEditor();
            }
        });
        getSuggestion().setConditionActive(isActive());
        disable(suggestion.isConditionActive());
    }
    public final void disable(Boolean pDisable) {
        if (pDisable) {
            getEditor().setDisable(false);
        } else {
            getEditor().setDisable(true);
            setValue(getValue() != null ? "" : null);
        }
    }

    private boolean isActive() {
        if (suggestion.getCriterion() == null) {
            return false;
        }
        if (suggestion.getActionId() == null) {
            return false;
        }
        String actionType = String.valueOf(suggestion.getActionId().getIdent());
        if (actionType.equals(ACTIONTYPE_ADD)) {
            return false;
        }
        if (!DATATYPE_ARRAY_STRING.equals(suggestion.getCriterion().getCriterionType())) {
            return false;
        }
        if(!suggestion.isConditionDisplayed()){
            return false;
        }
        return true;
    }

    @Override
    public String getValue() {
        return getSuggestion().getConditionValue();
    }

    @Override
    public void setValue(String value) {
        saveLastValueForType(OperationType.castOperation(getSuggestion().getConditionOperator()), value);
        getSuggestion().setConditionValue(value);
    }

    @Override
    public TypesAndOperations.OperationGroups.OperationGroup.Operation getOperation() {
        return getSuggestion().getConditionOperator();
    }

    @Override
    public String getCode() {
        return getSuggestion().getConditionValue();
    }

    @Override
    protected String getNotificationFromMessage(RuleMessage pMessage) {
        if(pMessage != null && pMessage.getDescription()!= null && pMessage.getDescription().toLowerCase().contains("wenn-bedingung")){
            return pMessage.getDescription();
        }
        return super.getNotificationFromMessage(pMessage);
    }
    
    
}
