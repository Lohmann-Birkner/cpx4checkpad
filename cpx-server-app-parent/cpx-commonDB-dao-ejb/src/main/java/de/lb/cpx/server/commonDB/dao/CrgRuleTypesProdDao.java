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

import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypesProd;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypesProd_;
import de.lb.cpx.server.commonDB.model.rules.CrgRulesProd_;
import javax.ejb.Stateless;

/**
 *
 * @author gerschmann
 */
@Stateless
public class CrgRuleTypesProdDao extends CrgRuleTypesDao<CrgRuleTypesProd> {

    /**
     * Creates a new instance.
     */
    public CrgRuleTypesProdDao() {
        super(CrgRuleTypesProd.class);
    }

    @Override
    public String getType2Rule() {
        return CrgRuleTypesProd_.CRG_RULESES_PROD;
    }

    @Override
    public String getRfCode4Pool() {
        return CrgRulesProd_.CRG_RULE_POOLS_PROD;
    }

    @Override
    public String getRfCode4Rule() {
        return CrgRulesProd_.ID;
    }

    @Override
    protected String getTableName() {
        return "CRG_RULE_TYPES_PROD";
    }
}
