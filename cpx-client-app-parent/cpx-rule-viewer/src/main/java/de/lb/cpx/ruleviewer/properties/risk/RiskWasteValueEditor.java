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

import de.lb.cpx.client.core.model.fx.textfield.NumberTextField;
import de.lb.cpx.client.core.model.fx.textfield.PercentTextField;
import java.util.Objects;
import org.controlsfx.control.PropertySheet;

/**
 *
 * @author wilde
 */
public class RiskWasteValueEditor extends RiskNumberEditor{

    public RiskWasteValueEditor(PropertySheet.Item item) {
        super(item);
    }

    @Override
    public NumberTextField getNumberEditor() {
        return new PercentTextField();
    }

    @Override
    public void setNumberValue(Number pValue) {
        Integer value = pValue!=null?pValue.intValue():null;
        getRiskItem().setRiskWasteValue(value!=null?String.valueOf(value):"");
    }

    @Override
    public Number getNumberValue() {
        String value = Objects.requireNonNullElse(getRiskItem().getRiskWasteValue(),"0");
        if(value.isEmpty()){
            return null;
        }
        int val = Double.valueOf(value).intValue();
        return val;
    }

}
