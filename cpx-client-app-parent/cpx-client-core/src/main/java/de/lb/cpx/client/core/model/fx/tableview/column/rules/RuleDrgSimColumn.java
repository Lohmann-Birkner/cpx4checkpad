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
package de.lb.cpx.client.core.model.fx.tableview.column.rules;

import de.lb.cpx.client.core.model.fx.tableview.column.StringColumn;
import de.lb.cpx.shared.dto.rules.CpxSimpleRuleDTO;
import de.lb.cpx.shared.lang.Lang;

/**
 * column class to display the drg simulation of the rule
 *
 * @author wilde
 */
public class RuleDrgSimColumn extends StringColumn<CpxSimpleRuleDTO> {

    /**
     * creates new instance
     */
    public RuleDrgSimColumn() {
        super(Lang.getCaseResolveRulesDRGSimulationObj().getAbbreviation());
//        widthProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                LOG.info("width " + newValue.doubleValue());
//            }
//        });
        setMinWidth(58.0);
        setMaxWidth(58.0);
        setResizable(false);
    }

    @Override
    public String extractValue(CpxSimpleRuleDTO pValue) {
        return pValue.getChkCwDrg();
    }

}
