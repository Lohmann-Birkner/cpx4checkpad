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
import de.lb.cpx.ruleviewer.model.Term;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.BeanProperty;

/**
 * editor for the term value
 *
 * @author wilde
 */
public class TermValueEditor extends ValueEditor {

    private static final Logger LOG = Logger.getLogger(TermValueEditor.class.getName());

    private Term term;

    public TermValueEditor(PropertySheet.Item item) {
        super(item);
        getTerm().firstConditionProperty().addListener(new ChangeListener<CriterionTree.Supergroup.Group.Criterion>() {
            @Override
            public void changed(ObservableValue<? extends CriterionTree.Supergroup.Group.Criterion> observable, CriterionTree.Supergroup.Group.Criterion oldValue, CriterionTree.Supergroup.Group.Criterion newValue) {
                refreshEditor();
            }
        });
        getTerm().operatorProperty().addListener(new ChangeListener<TypesAndOperations.OperationGroups.OperationGroup.Operation>() {
            @Override
            public void changed(ObservableValue<? extends TypesAndOperations.OperationGroups.OperationGroup.Operation> observable, TypesAndOperations.OperationGroups.OperationGroup.Operation oldValue, TypesAndOperations.OperationGroups.OperationGroup.Operation newValue) {
                saveLastValueForType(OperationType.castOperation(oldValue), getValue());
//                setValue(getLastValueForOperationType(!isEmptyOperator(oldValue)?OperationType.castOperation(newValue):null));
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
    public boolean isEmptyOperator(TypesAndOperations.OperationGroups.OperationGroup.Operation pOperation){
        if(pOperation == null){
            return true;
        }
        return pOperation.getName().isEmpty();
    }
    @Override
    public CriterionTree.Supergroup.Group.Criterion getCriterion() {
        return getTerm().getFirstCondition();
    }

    @Override
    public final String getValue() {
        return getTerm().getSecondCondition();
    }

    @Override
    public void setValue(String value) {
        saveLastValueForType(OperationType.castOperation(getTerm().getOperator()), value);
        getTerm().setSecondCondition(value);
    }

    public Term getTerm() {
        if (term == null) {
            Object bean = ((BeanProperty) getItem()).getBean();
            if (!(bean instanceof Term)) {
                LOG.severe("bean of the editor is not a Term!");
                return null;
            }
            term = (Term) bean;
        }
        return term;
    }

    @Override
    public TypesAndOperations.OperationGroups.OperationGroup.Operation getOperation() {
        return getTerm().getOperator();
    }

    @Override
    public String getCode() {
        return getTerm().getSecondCondition();
    }

}
