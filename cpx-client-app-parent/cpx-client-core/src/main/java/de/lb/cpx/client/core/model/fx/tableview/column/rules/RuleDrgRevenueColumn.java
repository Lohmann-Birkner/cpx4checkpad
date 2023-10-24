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
package de.lb.cpx.client.core.model.fx.tableview.column.rules;

import de.lb.cpx.client.core.model.fx.tableview.column.CurrencyColumn;
import de.lb.cpx.shared.dto.rules.CpxSimpleRuleDTO;

/**
 * column to display rule revenue "dErlös", worst case value expected as -99.999,99
 * @author wilde
 */
public class RuleDrgRevenueColumn extends CurrencyColumn<CpxSimpleRuleDTO> {
    
//    private static final Double WIDTH_DEFAULT = 70.0;
//    private static final Double WIDTH_100 = 78.0;
//    private static final Double WIDTH_1000 = 90.0;
    private static final Double WIDTH_10000 = 98.0;
    /**
     * creates new instance with default header
     */
    public RuleDrgRevenueColumn() {
        super("dErlös");
        setNewFixedWidth(/*WIDTH_DEFAULT*/WIDTH_10000);
        setResizable(false);
    }

    @Override
    public Double extractValue(CpxSimpleRuleDTO pValue) {
        Double val = pValue.getChkFeeSimulDiff();
//        updateWidthForValue(val);
        return val;
    }

//    private void updateWidthForValue(Double pValue) {
//        if(pValue == null){
//            return;
//        }
//        double absVal = Math.abs(pValue);
//        if (absVal > 100 && shouldChangeWidth(WIDTH_100)) {
//            setNewFixedWidth(WIDTH_100);
//        }
//        if (absVal > 1000 && shouldChangeWidth(WIDTH_1000)) {
//            setNewFixedWidth(WIDTH_1000);
//        }
//        if (absVal > 10000 && shouldChangeWidth(WIDTH_10000)) {
//            setNewFixedWidth(WIDTH_10000);
//        }
//    }
    
    private void setNewFixedWidth(double pWidth){
        setResizable(true);
        setMinWidth(pWidth);
        setMaxWidth(pWidth);
        setResizable(false);
    }

//    private boolean shouldChangeWidth(Double pNewWidth) {
//        //basically never shrink with value becomes smaller
//        if(getMaxWidth()<pNewWidth){
//            return true;
//        }
//        return false;
//    }

}
