/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.properties.risk;

import de.lb.cpx.rule.element.model.Risk;
import java.util.logging.Logger;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.BeanProperty;
import org.controlsfx.property.editor.PropertyEditor;

/**
 *
 * @author wilde
 */
public abstract class RiskEditor implements PropertyEditor<String> {
    private static final Logger LOG = Logger.getLogger(RiskEditor.class.getName());
    
    private RiskEditorItem riskItem;
    private final PropertySheet.Item item;

    public RiskEditor(PropertySheet.Item property) {
        item = property;
    }

    public RiskEditorItem getRiskItem() {
        if (riskItem == null) {
            Object bean = ((BeanProperty) item).getBean();
            if(bean == null){
                LOG.severe("bean value is null!");
                return null;
            }
            if (!(bean instanceof RiskEditorItem)) {
                LOG.severe("bean of the editor is not of expected value!\nExpected: " + RiskEditorItem.class.getName() + " but was: " + bean.getClass().getName());
                return null;
            }
            riskItem = (RiskEditorItem) bean;
        }
        return riskItem;
    }
    public Risk getRisk(){
        if(getRiskItem() == null){
            return null;
        }
        return getRiskItem().getRisk();
    }
    
}
