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

import de.lb.cpx.server.commonDB.model.rules.CrgRulePoolsProd;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePoolsProd_;
import javax.ejb.Stateless;

/**
 *
 * @author gerschmann
 */
@Stateless
public class CrgRulePoolsProdDao extends CrgRulePoolsDao<CrgRulePoolsProd> {

    public CrgRulePoolsProdDao() {
        super(CrgRulePoolsProd.class);
    }
    
    @Override
    protected String getRfCode4Id() {
        return CrgRulePoolsProd_.ID;
    }
    
    @Override
    protected String getRfCode4Rule() {
        return CrgRulePoolsProd_.CRG_RULESES_PROD;
    }

    @Override
    protected String getRfCode4RuleTable() {
        return CrgRulePoolsProd_.CRG_RULE_TABLES_PROD;
    }

    @Override
    protected String getSuffix() {
        return "_PROD";
    }
}
