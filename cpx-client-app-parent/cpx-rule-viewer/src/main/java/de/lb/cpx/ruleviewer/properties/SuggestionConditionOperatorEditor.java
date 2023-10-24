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

import de.lb.cpx.rule.criteria.TypesAndOperationsManager;
import de.lb.cpx.rule.criteria.model.CriterionTree;
import de.lb.cpx.rule.criteria.model.TypesAndOperations;
import static de.lb.cpx.ruleviewer.properties.SuggestionOperatorEditor.ACTIONTYPE_ADD;
import static de.lb.cpx.ruleviewer.properties.SuggestionOperatorEditor.DATATYPE_ARRAY_STRING;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.controlsfx.control.PropertySheet;

/**
 * Editor for Operator in Condition
 *
 * @author wilde
 */
public class SuggestionConditionOperatorEditor extends SuggestionOperatorEditor {

    public SuggestionConditionOperatorEditor(PropertySheet.Item item) {
        super(item);
        suggestion.actionIdProperty().addListener(new ChangeListener<TypesAndOperations.SuggActions.SuggAction>() {
            @Override
            public void changed(ObservableValue<? extends TypesAndOperations.SuggActions.SuggAction> observable, TypesAndOperations.SuggActions.SuggAction oldValue, TypesAndOperations.SuggActions.SuggAction newValue) {
                suggestion.setConditionActive(isActive());
//                getEditor().setDisable(false);
            }
        });
        suggestion.criterionProperty().addListener(new ChangeListener<CriterionTree.Supergroup.Group.Criterion>() {
            @Override
            public void changed(ObservableValue<? extends CriterionTree.Supergroup.Group.Criterion> observable, CriterionTree.Supergroup.Group.Criterion oldValue, CriterionTree.Supergroup.Group.Criterion newValue) {
                suggestion.setConditionActive(isActive());
            }
        });
//        suggestion.conditionActiveProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                disable(newValue);
//            }
//        });
//        suggestion.setConditionActive(isActive());
        disable(isActive());
    }
    public void disable(Boolean pDisable) {
        if (pDisable) {
            getEditor().setDisable(false);
            setEditorValues();
        } else {
            getEditor().setDisable(true);
            setValue(null);
            setValues(null);
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
//        if (suggestion.getConditionOperator() != null) {
//            if ("".equals(suggestion.getConditionOperator().getName())) {
//                return false;
//            }
//        }
        return true;
    }

    @Override
    public List<TypesAndOperations.OperationGroups.OperationGroup.Operation> getOperatorList() {
        String datatype = suggestion.getCriterion().getCriterionType();
        if (DATATYPE_ARRAY_STRING.equals(datatype)) {
            String actionType = String.valueOf(suggestion.getActionId().getIdent());
            if (ACTIONTYPE_DELETE.equals(actionType)) {
                return TypesAndOperationsManager.instance().getOperationGroupByName(OP_TABLES_SUGG).getOperation();
            }
            if (ACTIONTYPE_CHANGE.equals(actionType)) {
                return TypesAndOperationsManager.instance().getOperationGroupByName(OP_TABLES_SUGG).getOperation();
            }
        }
        return null;
    }

    @Override
    public TypesAndOperations.OperationGroups.OperationGroup.Operation getOperation() {
        if(suggestion == null){
            return null;
        }
        return suggestion.getConditionOperator();
    }
    
    @Override
    public String getLinkedValue() {
        if(suggestion == null){
            return null;
        }
        return suggestion.getConditionValue();
    }

    @Override
    public boolean checkSyntaxError() {
        return isEmptyOperation(getOperation()) && !isEmptyLinkedValue(getLinkedValue());
    }
    
}
