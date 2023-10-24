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

import de.lb.cpx.rule.criteria.TypesAndOperationsManager;
import de.lb.cpx.rule.criteria.model.TypesAndOperations;
import de.lb.cpx.ruleviewer.model.Link;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.BeanProperty;

/**
 *
 * @author wilde
 */
public class LinkOperatorEditor extends BasicOperatorEditor {

    public LinkOperatorEditor(PropertySheet.Item item) {
        super(item);
        //setValues(OperatorEn.getLinkValues());
        Object bean = ((BeanProperty) item).getBean();
        if (bean instanceof Link) {
            setValue(((Link) bean).getOperator());
            String critType = "opListNested";
            TypesAndOperations.OperationGroups.OperationGroup gr = TypesAndOperationsManager.instance().getOperationGroupByName(critType);
            setValues(gr.getOperation());//.toArray(new TypesAndOperations.OperationGroups.OperationGroup.Operation[gr.getOperation().size()]));
//            selectFirst();
        }
//        setValue(OperatorEn.AND);
//        setSelectedValue(OperatorEn.AND);
    }

}
