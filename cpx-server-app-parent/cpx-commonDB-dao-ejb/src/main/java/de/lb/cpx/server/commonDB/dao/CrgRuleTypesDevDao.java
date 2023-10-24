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

import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypesDev;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypesDev_;
import de.lb.cpx.server.commonDB.model.rules.CrgRulesDev_;
import javax.ejb.Stateless;

/**
 *
 * @author gerschmann
 */
@Stateless
public class CrgRuleTypesDevDao extends CrgRuleTypesDao<CrgRuleTypesDev> {

    /**
     * Creates a new instance.
     */
    public CrgRuleTypesDevDao() {
        super(CrgRuleTypesDev.class);
    }

    @Override
    public String getType2Rule() {
        return CrgRuleTypesDev_.CRG_RULESES_DEV;
    }

    @Override
    public String getRfCode4Pool() {
        return CrgRulesDev_.CRG_RULE_POOLS_DEV;
    }

    @Override
    public String getRfCode4Rule() {
        return CrgRulesDev_.ID;
    }

    @Override
    protected String getTableName() {
        return "CRG_RULE_TYPES_DEV";
    }

}
