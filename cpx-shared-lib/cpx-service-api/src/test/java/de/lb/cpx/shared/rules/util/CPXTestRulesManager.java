/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.rules.util;

import de.checkpoint.ruleGrouper.data.CRGRulePool;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author gerschmann
 */
public class CPXTestRulesManager implements CpxRuleManagerIF {

    private static final RuleTablesManager mRuleTablesManager = new RuleTablesManager();
    private static final Logger LOG = Logger.getLogger(CPXTestRulesManager.class.getName());

    public CPXTestRulesManager() {

    }

    @Override
    public String[] getStringArrayFromRuleTables(String tableName, String poolIdent, int year) throws Exception {
        return mRuleTablesManager.getStringArrayFromRuleTables(getTabIdent(poolIdent, tableName, year));
    }

    private String getTabIdent(String poolIdent, String tableName, int year) {
        tableName = tableName.replaceAll("'", "");
        if (poolIdent == null) {
            return tableName.toLowerCase();
        }
        return (poolIdent + "_" + tableName + "_" + String.valueOf(year)).toLowerCase();

    }

    public void addRuleTable(String tableName, String poolIdent, int year, String tabContent) {
        String key = getTabIdent(poolIdent, tableName, year);
        mRuleTablesManager.addRuleTable(key, tabContent);
    }

//    @Override
    @Override
    public int[] getIntArrayFromRuleTables(String tableName, String poolIdent, int year) throws Exception {
        return mRuleTablesManager.getIntArrayFromRuleTables(getTabIdent(poolIdent, tableName, year));

    }

//    @Override
    @Override
    public double[] getDoubleArrayFromRuleTables(String tableName, String poolIdent, int year) throws Exception {
        return mRuleTablesManager.getDoubleArrayFromRuleTables(getTabIdent(poolIdent, tableName, year));
    }

//    @Override
    @Override
    public long[] getLongArrayFromRuleTables(String tableName, String poolIdent, int year) throws Exception {
        return mRuleTablesManager.getLongArrayFromRuleTables(getTabIdent(poolIdent, tableName, year));
    }

//    @Override
    @Override
    public Date[] getDateArrayFromRuleTables(String tableName, String poolIdent, int year) throws Exception {
        return mRuleTablesManager.getDateArrayFromRuleTables(getTabIdent(poolIdent, tableName, year));
    }

//    @Override
    @Override
    public List<CRGRulePool> getRulePools() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
