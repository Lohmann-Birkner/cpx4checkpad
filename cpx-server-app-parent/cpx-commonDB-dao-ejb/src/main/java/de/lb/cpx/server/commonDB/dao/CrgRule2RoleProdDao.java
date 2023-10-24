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

import de.lb.cpx.server.commonDB.model.rules.CrgRule2RoleProd;
import de.lb.cpx.server.commonDB.model.rules.CrgRule2RoleProd_;
import de.lb.cpx.server.commonDB.model.rules.CrgRulesProd_;
import javax.ejb.Stateless;

/**
 *
 * @author gerschmann
 */
@Stateless
public class CrgRule2RoleProdDao extends CrgRule2RoleDao<CrgRule2RoleProd> {

    public CrgRule2RoleProdDao() {
        super(CrgRule2RoleProd.class);
    }

    @Override
    protected String getRfCode4Rule() {
        return CrgRule2RoleProd_.CRG_RULES_PROD;
    }

    @Override
    protected String getRf4UserRoles() {
        return CrgRule2RoleProd_.CDB_USER_ROLES;
    }

    @Override
    protected String getTableName() {
        return "CRG_RULE_2_ROLE_PROD";
    }

    @Override
    protected String getRfCode4Pool() {
        return CrgRulesProd_.CRG_RULE_POOLS_PROD;
    }

}
