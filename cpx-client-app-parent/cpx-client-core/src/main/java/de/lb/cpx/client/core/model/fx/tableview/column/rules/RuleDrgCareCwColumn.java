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

import de.lb.cpx.client.core.model.fx.tableview.column.StringColumn;
import de.lb.cpx.shared.dto.rules.CpxSimpleRuleDTO;
import de.lb.cpx.shared.lang.Lang;

/**
 * column to display care cw "dPflege", worst case value expected as -999,9999
 * @author wilde
 */
public class RuleDrgCareCwColumn extends StringColumn<CpxSimpleRuleDTO> {

    /**
     * creates new instance with default header
     */
    public RuleDrgCareCwColumn() {
        super("dPflege");
        setMinWidth(83.0);
        setMaxWidth(83.0);
        setResizable(false);
    }

    @Override
    public String extractValue(CpxSimpleRuleDTO pValue) {
        Double val = pValue.getChkCwCareSimulDiff();
        return Lang.toDecimal(val, 4);
    }

}
