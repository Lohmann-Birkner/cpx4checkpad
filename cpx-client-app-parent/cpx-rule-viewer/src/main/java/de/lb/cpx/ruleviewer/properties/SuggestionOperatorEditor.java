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

import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.rule.criteria.model.CriterionTree;
import de.lb.cpx.rule.criteria.model.TypesAndOperations;
import de.lb.cpx.rule.criteria.model.TypesAndOperations.OperationGroups.OperationGroup.Operation;
import de.lb.cpx.ruleviewer.model.Suggestion;
import de.lb.cpx.shared.json.RuleMessage;
import java.util.List;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.BeanProperty;

/**
 * Operator Editor for Suggestions Operator for suggestions are dependend on
 * criterion with the stored datatype and/or the tooltips in case of possible
 * enum also dependend on the actiontype
 *
 * @author wilde
 */
public abstract class SuggestionOperatorEditor extends BasicOperatorEditor {

    protected static final String DATATYPE_STRING = "DATATYPE_STRING";
    protected static final String DATATYPE_DOUBLE = "DATATYPE_DOUBLE";
    protected static final String DATATYPE_ARRAY_DOUBLE = "DATATYPE_ARRAY_DOUBLE";
    protected static final String DATATYPE_INTEGER = "DATATYPE_INTEGER";
    protected static final String DATATYPE_ARRAY_INTEGER = "DATATYPE_ARRAY_INTEGER";
    protected static final String DATATYPE_ARRAY_STRING = "DATATYPE_ARRAY_STRING";
    protected static final String OP_EQUAL = "opList_equal_sugg";
    protected static final String OP_NUMERIC = "opList_numeric_sugg";
    protected static final String OP_TABLES_IN_EQUAL_ONLY_SUGG = "opList_tables_InEqualOnly_suggs";
    protected static final String OP_EQUAL_SUGG = "opList_equal_sugg";
    protected static final String OP_TABLES_SUGG = "opList_tables_suggs";
    protected static final String ACTIONTYPE_DELETE = "0";
    protected static final String ACTIONTYPE_ADD = "1";
    protected static final String ACTIONTYPE_CHANGE = "2";

    private static final Logger LOG = Logger.getLogger(SuggestionOperatorEditor.class.getName());

    protected Suggestion suggestion;

    public SuggestionOperatorEditor(PropertySheet.Item item) {
        super(item);
        Object bean = ((BeanProperty) item).getBean();
        if (!(bean instanceof Suggestion)) {
            LOG.severe("bean of the editor is not a Suggestion!");
            return;
        }
        suggestion = (Suggestion) bean;
        //react to changes in suggestion
        suggestion.criterionProperty().addListener(new ChangeListener<CriterionTree.Supergroup.Group.Criterion>() {
            @Override
            public void changed(ObservableValue<? extends CriterionTree.Supergroup.Group.Criterion> observable, CriterionTree.Supergroup.Group.Criterion oldValue, CriterionTree.Supergroup.Group.Criterion newValue) {
                setEditorValues();
            }
        });
        suggestion.actionIdProperty().addListener(new ChangeListener<TypesAndOperations.SuggActions.SuggAction>() {
            @Override
            public void changed(ObservableValue<? extends TypesAndOperations.SuggActions.SuggAction> observable, TypesAndOperations.SuggActions.SuggAction oldValue, TypesAndOperations.SuggActions.SuggAction newValue) {
                setEditorValues();
            }
        });
        suggestion.messageProperty().addListener(new ChangeListener<RuleMessage>() {
            @Override
            public void changed(ObservableValue<? extends RuleMessage> ov, RuleMessage t, RuleMessage t1) {
                checkRuleMessage(suggestion);
            }
        });
        checkRuleMessage(suggestion);
        setEditorValues();
    }

    public abstract List<Operation> getOperatorList();

    protected void setEditorValues() {
        //if values some how not set, show empty list
        if (suggestion.getCriterion() == null) {
            setValues(null);
            return;
        }
        if (suggestion.getActionId() == null) {
            setValues(null);
            return;
        }

        setValues(getOperatorList());
//        selectFirst();
    }
    public abstract Operation getOperation();
    
    public abstract String getLinkedValue();
    
    public abstract boolean checkSyntaxError();
    
    private void checkRuleMessage(Suggestion suggestion) {
        if(suggestion == null){
            updateRuleMessageTooltip(null);
            return;
        }
        /*if(isEmptyOperation(getOperation()) && !isEmptyLinkedValue(getLinkedValue()) && suggestion.getMessage()!=null){*/
        if(checkSyntaxError() && suggestion.getMessage() != null){
            updateRuleMessageTooltip(new CpxTooltip(suggestion.getMessage().getReason().getTranslation().getValue(), 100, 5000, 100, true));
        }else{
            getClass();
            updateRuleMessageTooltip(null);
        }
    }
    
    private void updateRuleMessageTooltip(CpxTooltip pTooltip){
        if(pTooltip != null){
            setShowMessage(true);
            setMessageTooltip(pTooltip);
        }else{
            setShowMessage(false);
            setMessageTooltip(null);
        }
    }
    
    public boolean isEmptyOperation(Operation pOperation){
        if(pOperation == null){
            return true;
        }
        return pOperation.getName().isEmpty();
    }
    
    public boolean isEmptyLinkedValue(String pValue){
        if(pValue == null){
            return true;
        }
        return pValue.trim().isEmpty();
    }
    
}
