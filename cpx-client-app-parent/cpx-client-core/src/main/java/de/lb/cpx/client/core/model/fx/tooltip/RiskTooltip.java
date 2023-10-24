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
 *    2020  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.tooltip;

import de.lb.cpx.shared.dto.rules.CpxSimpleRuleDTO;

/**
 *
 * @author gerschmann
 */
public class RiskTooltip extends BasicTooltip{
    
    RiskToolTipLayout layout = new RiskToolTipLayout();
    public RiskTooltip(String title){
        super(200, 5000, 100, true);
        setTitle(title);
        setContentNode(layout);
    }


    public void setValues(CpxSimpleRuleDTO rule, boolean is4billing) {
        layout.setValues(rule, is4billing);
    }
    
}
