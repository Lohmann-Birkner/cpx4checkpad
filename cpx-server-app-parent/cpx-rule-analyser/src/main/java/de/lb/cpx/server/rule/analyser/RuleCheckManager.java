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
package de.lb.cpx.server.rule.analyser;

import de.checkpoint.ruleGrouper.data.CRGRulePool;
import de.lb.cpx.shared.rules.util.CpxRuleManagerIF;
import de.lb.cpx.shared.rules.util.RuleTablesManager;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implements Method getStringArrayFromRuleTables which the class CpxCRGRule
 * uses by applying of table methods(in table, not in table, multiple in table
 * and so on)
 *
 * @author gerschmann
 */
public class RuleCheckManager implements CpxRuleManagerIF {

    /**
     * saves the contents of rule tables
     */
    private static final RuleTablesManager mRuleTablesManager = new RuleTablesManager();

    /**
     * CpxCRGRule uses this method when there is any table operation in the rule
     * syntax. It gets the table content for rule according to the pool- and
     * year identification, saved in this rule For rule check are pool- und year
     * name not required
     *
     * @param pTableName table name
     * @param pPoolIdent rule pool ident
     * @param pYear rule pool year
     * @return content of the table as an array of strings or null when the
     * table is not found
     * @throws Exception error
     */
    @Override
    public String[] getStringArrayFromRuleTables(String pTableName, String pPoolIdent, int pYear) throws Exception {

        return mRuleTablesManager.getStringArrayFromRuleTables(pTableName);
    }

    public void resetTables() {
        mRuleTablesManager.resetAllTables();
    }

    void fillRuleTables(Map<String, String> pRuleTables) {
        resetTables();
        if (pRuleTables == null || pRuleTables.isEmpty()) {
            return;
        }
        Set<String> tabNames = pRuleTables.keySet();
        tabNames.forEach((key) -> {
            mRuleTablesManager.addRuleTable(key, pRuleTables.get(key));
        });
    }

    @Override
    public int[] getIntArrayFromRuleTables(String tableName, String ruleIdent, int year) throws Exception {
        return mRuleTablesManager.getIntArrayFromRuleTables(tableName);
    }

    @Override
    public double[] getDoubleArrayFromRuleTables(String tableName, String ruleIdent, int year) throws Exception {
        return mRuleTablesManager.getDoubleArrayFromRuleTables(tableName);

    }

    @Override
    public long[] getLongArrayFromRuleTables(String tableName, String ruleIdent, int year) throws Exception {
        return mRuleTablesManager.getLongArrayFromRuleTables(tableName);
    }

    @Override
    public Date[] getDateArrayFromRuleTables(String tableName, String ruleIdent, int year) throws Exception {
        return mRuleTablesManager.getDateArrayFromRuleTables(tableName);
    }

    @Override
    public List<CRGRulePool> getRulePools() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
