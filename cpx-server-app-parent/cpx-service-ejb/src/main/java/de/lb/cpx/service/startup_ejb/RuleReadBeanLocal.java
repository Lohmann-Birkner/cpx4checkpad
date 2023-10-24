/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.startup_ejb;

import de.checkpoint.ruleGrouper.CRGRule;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import java.util.List;
import java.util.Map;
import javax.ejb.LocalBean;

/**
 *
 * @author gerschmann
 */
@LocalBean
public interface RuleReadBeanLocal {

    /**
     *
     * @return all rules that can be used for grouping, they will be set in the
     * rule kernel
     */
    CRGRule[] getRules();

    CRGRule getRule(int year, long ruleId);

    CRGRule[] getRule2ListId(List<Long> ruleIds);

    Map<String, CRGRule> getRulesForYear(int year);

//    List<CRGRuleTypes> getRuleTypes();
//    
//    List<CRGRule> getRules4Pool(String pool, int year);
//
    void reloadRules4Pool(String poolName, int year);

    CrgRules getRule2Id(long pRId);

    List<CrgRules> getCpxRules();

    List<CrgRuleTypes> getCpxTypes();

    List<CrgRulePools> getCpxPools();

    List<CrgRules> getRules4Year(long pPoolId);

    int findSizeOfRules(long pPoolId);

    public int findSizeOfRuleTables2Pool(long pPoolId);

    List<CrgRuleTables> findRuleTables4Year(long pPoolId);

    /**
     * search list of tables for pool id and the name of the and the content to
     * provide matches
     *
     * @param pPoolId pool to search tables
     * @param pText text to search in table content and name
     * @return list of all matching tables
     */
    List<CrgRuleTables> findRuleTables4YearAndContentOrName(long pPoolId, String pText);

    List<CrgRuleTables> getCpxTables();

    CrgRuleTables getCpxTable4Year(String pName, long pPoolId);

    byte[] getRuleDefinition(long pRuleId, long pPoolId);

    List<CdbUserRoles> findUserRoles2Rule(long pRuleId);

    String getRuleTableContent(long pTableId);

    public List<CrgRules> findRules4TableAndYear(long pPoolId, long pTableId);

    String getRuleTypeNameForRuleAndYear(long pRuleId, long pPoolId);

    String getRuleTypeDisplayNameForRuleAndYear(long pRuleId, long pPoolId);

    List<String> getRids4SearchOptions(Map<String, List<String>> pFilterOptionMap);

    public void resetRules();

    public void init();

    public List<CrgRules> getAllRules4PoolAndRole(long pPoolId, Long pRoleId, boolean is4role);

    public Long getRuleCountForRole(long pPoolId, long pRoleId);

    public List<CrgRules> getCpxRules4ids(List<Long> ruleIds);

    public Map<String, String> getRuleTypes2Ids();

}
