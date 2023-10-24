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
package de.lb.cpx.client.core.model.fx.tableview.column.rules;

import de.lb.cpx.client.core.model.fx.tableview.column.StringColumn;
import de.lb.cpx.shared.dto.rules.CpxSimpleRuleDTO;
import de.lb.cpx.shared.lang.Lang;

/**
 *
 * @author wilde
 */
public class RuleRiskWastePercentValueColumn extends StringColumn<CpxSimpleRuleDTO>{

    public RuleRiskWastePercentValueColumn() {
        super(Lang.getRiskEditorRiskWasteValueObj().getAbbreviation());
        setMinWidth(65.0);
        setMaxWidth(65.0);
        setResizable(false);
    }

    @Override
    public String extractValue(CpxSimpleRuleDTO pValue) {
        Integer percent = pValue.getRiskPercentValue();
        return percent==null?"":new StringBuilder().append(String.valueOf(percent)).append("%").toString();
    }
    
}
