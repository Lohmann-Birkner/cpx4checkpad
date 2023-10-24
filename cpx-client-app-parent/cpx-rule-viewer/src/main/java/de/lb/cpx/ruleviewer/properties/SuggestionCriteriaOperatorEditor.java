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
import de.lb.cpx.rule.criteria.model.TypesAndOperations;
import java.util.List;
import org.controlsfx.control.PropertySheet;

/**
 * Specific operator editor for criteria operator for suggestion
 *
 * @author wilde
 */
public class SuggestionCriteriaOperatorEditor extends SuggestionOperatorEditor {

    public SuggestionCriteriaOperatorEditor(PropertySheet.Item item) {
        super(item);
    }

    @Override
    public List<TypesAndOperations.OperationGroups.OperationGroup.Operation> getOperatorList() {
        TypesAndOperationsManager manager = TypesAndOperationsManager.instance();
        String datatype = suggestion.getCriterion().getCriterionType();
        //check if there is an enum present
        if (DATATYPE_INTEGER.equals(datatype) || DATATYPE_DOUBLE.equals(datatype)) {
            //possible enum
            if (!suggestion.getCriterion().getTooltip().isEmpty()) {
                if (suggestion.getCriterion().getTooltip().get(0).getValue() != null) {
                    //simple check for enum, if value is present - it is considert as enum
                    return manager.getOperationGroupByName(OP_EQUAL).getOperation();
                } else {
                    //is simple numeric value
                    return manager.getOperationGroupByName(OP_NUMERIC).getOperation();
                }

            }
        }
        if (DATATYPE_ARRAY_INTEGER.equals(datatype) || DATATYPE_ARRAY_DOUBLE.equals(datatype)) {
            return manager.getOperationGroupByName(OP_EQUAL).getOperation();
        }
        //handle string array
        //here selection is dependent to actionType
        if (DATATYPE_ARRAY_STRING.equals(datatype)) {
            String actionType = String.valueOf(suggestion.getActionId().getIdent());
            if (ACTIONTYPE_ADD.equals(actionType)) {
                return manager.getOperationGroupByName(OP_TABLES_IN_EQUAL_ONLY_SUGG).getOperation();
            }
            if (ACTIONTYPE_CHANGE.equals(actionType)) {
                return manager.getOperationGroupByName(OP_EQUAL_SUGG).getOperation();
            }
            if (ACTIONTYPE_DELETE.equals(actionType)) {
                return manager.getOperationGroupByName(OP_TABLES_SUGG).getOperation();
            }
        }
        //handle string
        //here selection is dependent to actionType
        if (DATATYPE_STRING.equals(datatype)) {
            String actionType = String.valueOf(suggestion.getActionId().getIdent());
            if (ACTIONTYPE_CHANGE.equals(actionType)) {
                return manager.getOperationGroupByName(OP_EQUAL_SUGG).getOperation();
            }
            if (ACTIONTYPE_ADD.equals(actionType)) {
                return manager.getOperationGroupByName(OP_EQUAL_SUGG).getOperation();
            }
            if (ACTIONTYPE_DELETE.equals(actionType)) {
                return manager.getOperationGroupByName(OP_TABLES_SUGG).getOperation();
            }
        }
        return null;
    }

    @Override
    protected void setEditorValues() {
        super.setEditorValues(); //To change body of generated methods, choose Tools | Templates.
        if (suggestion.getSuggestion().getOp() == null || suggestion.getSuggestion().getOp().isEmpty()) {
            if (suggestion.getSuggestion().getValueAttribute() != null && !suggestion.getSuggestion().getValueAttribute().isEmpty()) {
                selectFirst();
            }
        }
    }

    @Override
    public TypesAndOperations.OperationGroups.OperationGroup.Operation getOperation() {
        if(suggestion == null){
            return null;
        }
        return suggestion.getCriterionOperator();
    }
    
    @Override
    public String getLinkedValue() {
        if(suggestion == null){
            return null;
        }
        return suggestion.getCriterionValue();
    }

//    @Override
//    public boolean isEmptyLinkedValue(String pValue) {
//        return 
//    }

    @Override
    public boolean checkSyntaxError() {
        return isEmptyOperation(getOperation());
    }
    
}
