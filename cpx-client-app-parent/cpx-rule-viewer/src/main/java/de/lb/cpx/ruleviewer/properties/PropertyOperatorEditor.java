/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.properties;

import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.rule.criteria.CriteriaHelper;
import de.lb.cpx.rule.criteria.TypesAndOperationsManager;
import de.lb.cpx.rule.criteria.model.CriterionTree;
import de.lb.cpx.rule.criteria.model.TypesAndOperations;
import de.lb.cpx.ruleviewer.model.Link;
import de.lb.cpx.ruleviewer.model.Term;
import de.lb.cpx.shared.json.RuleMessage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.BeanProperty;

/**
 *
 * @author wilde
 */
public class PropertyOperatorEditor extends BasicOperatorEditor {

    private static final String NO_CRIT = "rules.txt.crit.sole.hpn.tooltip.1.2";

    public PropertyOperatorEditor(PropertySheet.Item item) {
        super(item);
        Object bean = ((BeanProperty) item).getBean();
        if (bean instanceof Term) {
            ((Term) bean).firstConditionProperty().addListener(new ChangeListener<CriterionTree.Supergroup.Group.Criterion>() {
                @Override
                public void changed(ObservableValue<? extends CriterionTree.Supergroup.Group.Criterion> observable, CriterionTree.Supergroup.Group.Criterion oldValue, CriterionTree.Supergroup.Group.Criterion newValue) {
                    checkDisable(((Term)bean));
                    if (newValue == null) {
                        return;
                    }
                    String group = "";
                    //update criterion type
                    if (!CriteriaHelper.isEnum(newValue)) {
                        String critType = ((Term) bean).getFirstCondition().getCriterionType();
                        group = TypesAndOperationsManager.instance().getCtriterionTypeByName(critType).getOperationGroup();
                    } else {
                        group = CriteriaHelper.isPccl(((Term) bean).getFirstCondition())?"opList_compare_only":"opList_equal";
                    }
                    TypesAndOperations.OperationGroups.OperationGroup gr = TypesAndOperationsManager.instance().getOperationGroupByName(group);
                    setValues(gr.getOperation());//.toArray(new TypesAndOperations.OperationGroups.OperationGroup.Operation[gr.getOperation().size()]));
                    if (getValue() == null || (!gr.getOperation().contains(getValue()))) {
                        selectFirst();
                    }
                }
            });
            ((Term) bean).messageProperty().addListener(new ChangeListener<RuleMessage>() {
                @Override
                public void changed(ObservableValue<? extends RuleMessage> ov, RuleMessage t, RuleMessage t1) {
                    checkRuleMessage(((Term)bean));
                }
            });
            checkRuleMessage((Term)bean);
            setValue(((Term) bean).getOperator());
            checkDisable((Term)bean);
            if (((Term) bean).getFirstCondition() == null) {
                return;
            }
            String group = "";
            //update criterion type
            if (!CriteriaHelper.isEnum(((Term) bean).getFirstCondition())) {
                String critType = ((Term) bean).getFirstCondition().getCriterionType();
                group = TypesAndOperationsManager.instance().getCtriterionTypeByName(critType).getOperationGroup();
            } else {
                group = CriteriaHelper.isPccl(((Term) bean).getFirstCondition())?"opList_compare_only":"opList_equal";
            }
            TypesAndOperations.OperationGroups.OperationGroup gr = TypesAndOperationsManager.instance().getOperationGroupByName(group);
            setValues(gr.getOperation());//.toArray(new TypesAndOperations.OperationGroups.OperationGroup.Operation[gr.getOperation().size()]));
        }
        if (bean instanceof Link) {
            setValue(((Link) bean).getOperator());
            String critType = "opListNested";//((Term)bean).getFirstCondition().getCriterionType();
            String type = TypesAndOperationsManager.instance().getCtriterionTypeByName(critType).getOperationGroup();
            TypesAndOperations.OperationGroups.OperationGroup gr = TypesAndOperationsManager.instance().getOperationGroupByName(type);
            setValues(gr.getOperation());//.toArray(new TypesAndOperations.OperationGroups.OperationGroup.Operation[gr.getOperation().size()]));
        }
    }
    private void checkDisable(Term pTerm){
        if (pTerm == null || (pTerm.getFirstCondition() == null || NO_CRIT.equals(pTerm.getFirstCondition().getName()))) {
            disableEditor(true);
            return;
        }
        disableEditor(false);
    }
//    private boolean isEnum(CriterionTree.Supergroup.Group.Criterion pCriterion){
//        for(CriterionTree.Supergroup.Group.Criterion.Tooltip tip : pCriterion.getTooltip()){
//            if(tip.getValue()!=null){
//                return true;
//            }
//        }
//        return false;
//    }

    private void checkRuleMessage(Term term) {
        if(term == null){
            updateRuleMessageTooltip(null);
            return;
        }
        if(!isOperator(term.getOperator()) && term.getMessage()!=null){
            updateRuleMessageTooltip(new CpxTooltip(term.getMessage().getReason().getTranslation().getValue(), 100, 5000, 100, true));
        }else{
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
    
    private boolean isOperator(TypesAndOperations.OperationGroups.OperationGroup.Operation pOperation){
        if(pOperation == null){
            return false;
        }
        if("".equals(pOperation.getName())){
            return false; // empty operation
        }
        return true;
    }
}
