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
 * Column class to show Reference(Bezug) of a rule
 *
 * @author wilde
 */
public class RuleReferenceColumn extends StringColumn<CpxSimpleRuleDTO> {

    /**
     * creates new instance with overrun behavior
     */
    public RuleReferenceColumn() {
        super(Lang.getCaseResolveRulesRelation(), true);
//        widthProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                LOG.info("width " + newValue.doubleValue());
//            }
//        });
        setMinWidth(120);
        setMaxWidth(120);
        setResizable(false);
    }

    @Override
    public String extractValue(CpxSimpleRuleDTO pValue) {
        return pValue.getChkReference();
    }

}
