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
import de.lb.cpx.server.commonDB.model.rules.CrgRule2TableProd_;
import de.lb.cpx.server.commonDB.model.rules.CrgRulesProd;
import de.lb.cpx.server.commonDB.model.rules.CrgRulesProd_;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

/**
 *
 * @author gerschmann
 */
@Stateless
public class CrgRulesProdDao extends CrgRulesDao<CrgRulesProd, CrgRule2RoleProd> {

    public CrgRulesProdDao() {
        super(CrgRulesProd.class);
    }

    @Override
    protected String getTableName() {
        return "CRG_RULES_PROD";
    }

    public List<CrgRulesProd> getAllProdRules() {
        final TypedQuery<CrgRulesProd> query = getEntityManager().createQuery("from " + getEntityName(), CrgRulesProd.class);
        return query.getResultList();
    }

    @Override
    protected String getRfCode4Pool() {
        return CrgRulesProd_.CRG_RULE_POOLS_PROD;
    }

    @Override
    protected String getRule2Table() {
        return CrgRulesProd_.CRG_RULE2_TABLE_PROD;
    }

    @Override
    protected String getRfCode4Table() {
        return CrgRule2TableProd_.CRG_RULE_TABLES_PROD;
    }

    @Override
    protected String getRfCode4Type() {
        return CrgRulesProd_.CRG_RULE_TYPES_PROD;
    }

    @Override
    protected String getRule2Role() {
        return CrgRulesProd_.CRG_RULE2_ROLE_PROD;
    }

    @Override
    protected Class<CrgRule2RoleProd> getRule2RoleClass() {
        return CrgRule2RoleProd.class;
    }

    @Override
    protected String getRfRule2Role() {
        return CrgRule2RoleProd_.CRG_RULES_PROD;
    }
}
