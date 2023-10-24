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

import de.lb.cpx.server.commonDB.model.rules.CrgRuleTablesDev;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTablesDev_;
import javax.ejb.Stateless;

/**
 *
 * @author gerschmann
 */
@Stateless
public class CrgRuleTableDevDao extends CrgRuleTablesDao<CrgRuleTablesDev> {

    public CrgRuleTableDevDao() {
        super(CrgRuleTablesDev.class);
    }

    @Override
    protected String getTableName() {
        return "CRG_RULE_TABLES_DEV";
    }

    @Override
    protected String getRfCode4Pool() {
        return CrgRuleTablesDev_.CRG_RULE_POOLS_DEV;
    }

    @Override
    protected String getTableNameField() {
        return CrgRuleTablesDev_.CRGT_TABLE_NAME;
    }

}
