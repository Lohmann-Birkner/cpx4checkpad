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

import de.lb.cpx.rule.criteria.model.TypesAndOperations;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.controlsfx.control.PropertySheet;

/**
 * Value editor for crition value in suggestion
 *
 * @author wilde
 */
public class SuggestionCritValueEditor extends SuggestionValueEditor {

    public SuggestionCritValueEditor(PropertySheet.Item item) {
        super(item);
        getSuggestion().criterionOperatorProperty().addListener(new ChangeListener<TypesAndOperations.OperationGroups.OperationGroup.Operation>() {
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
                refreshEditor();
            }
        });
    }

    @Override
    public String getValue() {
        return getSuggestion().getCriterionValue();
    }

    @Override
    public void setValue(String value) {
        saveLastValueForType(OperationType.castOperation(getSuggestion().getCriterionOperator()), value);
        getSuggestion().setCriterionValue(value);
    }

    @Override
    public TypesAndOperations.OperationGroups.OperationGroup.Operation getOperation() {
        return getSuggestion().getCriterionOperator();
    }

    @Override
    public String getCode() {
        return getSuggestion().getCriterionValue(); //To change body of generated methods, choose Tools | Templates.
    }

}
