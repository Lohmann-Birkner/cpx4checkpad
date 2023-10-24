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
package de.lb.cpx.service.readrules;

import de.checkpoint.ruleGrouper.CRGRule;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.commonDB.model.rules.RuleTablesIF;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author gerschmann
 */
public class RuleReadServiceLocal {

    protected CRGRule[] mAllRulesWithYears = null; // Regeln nach Jahren;
    protected Map<Integer, HashMap<String, CRGRule>> ruleIdYear = new HashMap<>();
    protected Map<Long, CRGRule> rule2Id = new HashMap<>();
    protected Map<Integer, Map<String, RuleTablesIF>> year2Tables = new HashMap<>();
// for rules description to be shown as readonly in client we need them es Cpx rules with type ids, table ids and pool ids
    protected Map<Long, CrgRuleTypes> cpxRuleTypes = new HashMap<>();
    protected Map<String, String> cpxRuleTypesIds = new HashMap<>(); // id as String to type name
    protected Map<String, CrgRuleTypes> cpxName2RuleTypes = new HashMap<>();
    protected Map<Long, CrgRuleTables> cpxRuleTables = new HashMap<>();
    protected Map<Long, Map<String, CrgRuleTables>> cpxPools2Tables = new HashMap<>();
    protected Map<Long, CrgRules> cpxRules = new HashMap<>();
    protected Map<Long, Map<Long, CrgRules>> cpxPools2Rules = new HashMap<>();
    protected Map<Long, CrgRulePools> cpxPools = new HashMap<>();
    protected Map<Long, Map<Long, List<CrgRules>>> cpxPoolId2TableId2Rules = new HashMap<>();
    protected Map<Long, Map<Long, List<CrgRules>>> cpxPoolId2RoleId2Rules = new HashMap<>();
    protected Map<Long, Map<Long, List<CrgRules>>> cpxPoolId2RoleIdNoRules = new HashMap<>();// Rules which do not have thise role

    protected synchronized void reloadRules() {
    }

    ;
    
    protected void clearAllMaps() {
        ruleIdYear.clear();
        rule2Id.clear();
        year2Tables.clear();
        cpxRuleTypes.clear();
        cpxRuleTables.clear();
        cpxRules.clear();
        cpxPools2Rules.clear();
        cpxName2RuleTypes.clear();
        cpxPools2Tables.clear();
        cpxPools.clear();
        cpxPoolId2TableId2Rules.clear();
        cpxPoolId2RoleId2Rules.clear();
        cpxPoolId2RoleIdNoRules.clear();
        cpxRuleTypesIds.clear();
    }

    protected void fillId2YearMap() {
        if (mAllRulesWithYears == null) {
            return;
        }
        for (CRGRule rule : mAllRulesWithYears) {
            HashMap<String, CRGRule> rule2year = ruleIdYear.get(rule.getYear());
            if (rule2year == null) {
                rule2year = new HashMap<>();
                ruleIdYear.put(rule.getYear(), rule2year);
            }
            rule2year.put(rule.m_rid, rule);
        }
    }

    public CRGRule[] getCpRules2Ids(List<Long> ruleIds) {
        if (ruleIds != null) {
            CRGRule[] ret = new CRGRule[ruleIds.size()];
            int ind = 0;
            for (Long id : ruleIds) {
                CRGRule rule = rule2Id.get(id);
                if (rule != null) {
                    ret[ind] = rule;
                    ind++;
                }
            }
            if (ind < ruleIds.size()) {
                CRGRule[] ret1 = new CRGRule[ind];
                System.arraycopy(ret, 0, ret1, 0, ind);
                return ret1;
            }
            return ret;
        }
        return null; //2017-12-01 DNi: return new CRGRule[0] instead?!
    }

}
