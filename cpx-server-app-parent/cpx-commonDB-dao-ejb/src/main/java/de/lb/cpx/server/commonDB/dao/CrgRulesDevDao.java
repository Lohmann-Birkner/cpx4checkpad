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

import de.lb.cpx.server.commonDB.model.rules.CrgRule2RoleDev;
import de.lb.cpx.server.commonDB.model.rules.CrgRulesDev;
import de.lb.cpx.server.commonDB.model.rules.CrgRulesDev_;
import de.lb.cpx.server.commonDB.model.rules.CrgRule2TableDev_;
import de.lb.cpx.server.commonDB.model.rules.CrgRule2RoleDev_;
import javax.ejb.Stateless;

/**
 *
 * @author gerschmann
 */
@Stateless
public class CrgRulesDevDao extends CrgRulesDao<CrgRulesDev, CrgRule2RoleDev> {

    public CrgRulesDevDao() {
        super(CrgRulesDev.class);
    }

    @Override
    protected String getTableName() {
        return "CRG_RULES_DEV";
    }

    @Override
    protected String getRfCode4Pool() {
        return CrgRulesDev_.CRG_RULE_POOLS_DEV;
    }

    @Override
    protected String getRule2Table() {
        return CrgRulesDev_.CRG_RULE2_TABLE_DEV;
    }

    @Override
    protected String getRfCode4Table() {
        return CrgRule2TableDev_.CRG_RULE_TABLES_DEV;
    }

    @Override
    protected String getRfCode4Type() {
        return CrgRulesDev_.CRG_RULE_TYPES_DEV;
    }

    @Override
    protected String getRule2Role() {
        return CrgRulesDev_.CRG_RULE2_ROLE_DEV;
    }

    @Override
    protected Class<CrgRule2RoleDev> getRule2RoleClass() {
        return CrgRule2RoleDev.class;
    }

    @Override
    protected String getRfRule2Role() {
        return CrgRule2RoleDev_.CRG_RULES_DEV;
    }

}
