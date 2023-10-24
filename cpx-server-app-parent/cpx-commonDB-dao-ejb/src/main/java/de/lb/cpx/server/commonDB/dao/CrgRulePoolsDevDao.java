/*
 * Copyright (c) 2019 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2019  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commonDB.dao;

import de.lb.cpx.server.commonDB.model.rules.CrgRulePoolsDev;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePoolsDev_;
import javax.ejb.Stateless;

/**
 *
 * @author gerschmann
 */
@Stateless
public class CrgRulePoolsDevDao extends CrgRulePoolsDao<CrgRulePoolsDev> {

    public CrgRulePoolsDevDao() {
        super(CrgRulePoolsDev.class);
    }

    @Override
    protected String getRfCode4Id() {
        return CrgRulePoolsDev_.ID;
    }

    @Override
    protected String getRfCode4Rule() {
        return CrgRulePoolsDev_.CRG_RULESES_DEV;
    }

    @Override
    protected String getRfCode4RuleTable() {
        return CrgRulePoolsDev_.CRG_RULE_TABLES_DEV;
    }

    @Override
    protected String getSuffix() {
        return "_DEV";
    }

}
