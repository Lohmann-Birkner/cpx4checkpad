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

import de.lb.cpx.server.commonDB.model.rules.CrgRule2TableDev;
import de.lb.cpx.server.commonDB.model.rules.CrgRule2TableDev_;
import javax.ejb.Stateless;

/**
 *
 * @author gerschmann
 */
@Stateless
public class CrgRule2TableDevDao extends CrgRule2TableDao<CrgRule2TableDev> {

    public CrgRule2TableDevDao() {
        super(CrgRule2TableDev.class);
    }

    @Override
    protected String getTableName() {
        return "CRG_RULE_2_TABLE_DEV";
    }

    @Override
    protected String getRfCode4Rule() {
        return CrgRule2TableDev_.CRG_RULES_DEV;
    }

}
