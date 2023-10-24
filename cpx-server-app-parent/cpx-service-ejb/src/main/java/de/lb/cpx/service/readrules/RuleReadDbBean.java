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
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.commonDB.dao.Rules4DbInteractBean;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.server.config.CpxServerConfigLocal;
import de.lb.cpx.service.startup_ejb.CpxDBRulesManagerLocal;
import de.lb.cpx.service.startup_ejb.RuleReadBeanLocal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateful;

/**
 * service for usage of rules for grouping processes from DB
 *
 * @author gerschmann
 */
@Stateful

@Local
public class RuleReadDbBean //extends RuleReadServiceLocal 
        implements RuleReadBeanLocal {

    private static final Logger LOG = Logger.getLogger(RuleReadDbBean.class.getName());

    @EJB
    private Rules4DbInteractBean rules4DbInteractBean;

    @EJB
    private CpxDBRulesManagerLocal cpxDbRulesManager;

    @EJB(name = "CpxServerConfig")
    private CpxServerConfigLocal cpxServerConfig;

    private final Map<String, CRGRule[]> rules2UserID = new HashMap<>();

    @Override
    public CRGRule[] getRules() {
        long start = System.currentTimeMillis();
        CRGRule[] mAllRulesWithYears = rules2UserID.get(getClientKey());
        LOG.log(Level.INFO, "getRules: has rules for userId: {0} {1}", new Object[]{getClientKey(), mAllRulesWithYears == null ? "null" : mAllRulesWithYears.length});
        if (mAllRulesWithYears == null || mAllRulesWithYears.length == 0) {

//            clearAllMaps();
            mAllRulesWithYears = cpxDbRulesManager.getAllRules(null, getRuleType());
            LOG.log(Level.INFO, "getRules: time:" + String.valueOf(System.currentTimeMillis() - start) + " has rules:" + (mAllRulesWithYears == null ? "null" : mAllRulesWithYears.length));
            if (mAllRulesWithYears != null) {
                rules2UserID.put(getClientKey(), mAllRulesWithYears);
            }
        }
        LOG.log(Level.INFO, "getRules: time for rules: " + String.valueOf(System.currentTimeMillis() - start));
        return mAllRulesWithYears == null ? new CRGRule[0] : Arrays.copyOf(mAllRulesWithYears, mAllRulesWithYears.length);
    }

    private PoolTypeEn getRuleType() {
        return cpxServerConfig.isRuleEditorClient() ? PoolTypeEn.DEV : PoolTypeEn.PROD;
    }

    private String getClientKey() {
        if (getRuleType().equals(PoolTypeEn.PROD)) {
            return PoolTypeEn.PROD.name();
        } else {
            return ClientManager.getCurrentCpxClientId();
        }
    }

    @PostConstruct
    @Override
    public void init() {
//        long start = System.currentTimeMillis();
//        LOG.log(Level.INFO, "init: time for rules: " + String.valueOf(System.currentTimeMillis() - start) + " has rules:" + (mAllRulesWithYears == null ? "null" : mAllRulesWithYears.length));
    }

//    @Override
//    protected synchronized void reloadRules() {
//        long start = System.currentTimeMillis();
////        clearAllMaps();
//        mAllRulesWithYears = cpxDbRulesManager.getAllRules(null, getRuleType());
//        LOG.log(Level.INFO, "reloadRules: time:" + String.valueOf(System.currentTimeMillis() - start) + " has rules:" + (mAllRulesWithYears == null ? "null" : mAllRulesWithYears.length));
//        List<CrgRules> cpxRules = null;
//        if (getRuleType().equals(PoolTypeEn.DEV)) {
//            cpxRules = rules4DbInteractBean.getAllCrgRules4Grouper(PoolTypeEn.DEV, ClientManager.getCurrentCpxUserId());
//             LOG.log(Level.INFO, "reloadRules: time for rules from DB: " + String.valueOf(System.currentTimeMillis() - start) + " has rules:" + (cpxRules == null ? "null" : cpxRules.size()));
//            start = System.currentTimeMillis();
//             mAllRulesWithYears = cpxDbRulesManager.getAllRules(cpxRules, getRuleType());
//
//            LOG.log(Level.INFO, "reloadRules: time for convert cpxRule->cpRule: " + String.valueOf(System.currentTimeMillis() - start) + " has rules:" + (mAllRulesWithYears == null ? "null" : mAllRulesWithYears.length));
//            } else {
//            cpxRules = rules4DbInteractBean.getAllRules();
//              mAllRulesWithYears = cpxDbRulesManager.getAllRules(null, getRuleType());
//        }
//       distributeCpRules();
//    }
// die Methode wird eigentlich nicht benutzt
    @Override
    public CRGRule getRule(int year, long ruleId) {
        CrgRules cpxRule = getRule2Id(ruleId);
        if (cpxRule != null) {
            List<CrgRules> cpxRules = new ArrayList<>();
            cpxRules.add(cpxRule);
            CRGRule[] rules = cpxDbRulesManager.getAllRules(cpxRules, getRuleType());
            if (rules != null && rules.length > 0) {
                return rules[0];
            }

        }
        return null;
    }

    @Override
    public CRGRule[] getRule2ListId(List<Long> ruleIds) {

        List<CrgRules> cpxRules = rules4DbInteractBean.getRuleList4Grouper(getRuleType(), ruleIds);

//        cpxRules = getCpxRules();
        return cpxDbRulesManager.getAllRules(cpxRules, getRuleType());

    }

// die Methode wird eigentlich nicht benutzt
    @Override
    public Map<String, CRGRule> getRulesForYear(int year) {
        return null;
    }

    @Override
    public void reloadRules4Pool(String poolName, int year) {

    }

    @Override
    public CrgRules getRule2Id(long pRId) {
        return rules4DbInteractBean.findRuleById(pRId, getRuleType());
    }

    @Override
    public List<CrgRules> getCpxRules() {
        if (getRuleType().equals(PoolTypeEn.PROD)) {
            return rules4DbInteractBean.getAllCrgRules4User(getRuleType(), null);
        } else {
            return rules4DbInteractBean.getAllCrgRules4User(getRuleType(), ClientManager.getCurrentCpxUserId());
        }
    }

    @Override
    public List<CrgRuleTypes> getCpxTypes() {
        return rules4DbInteractBean.getCrgRuleTypes(getRuleType(), ClientManager.getCurrentCpxUserId());
    }

    @Override
    public List<CrgRulePools> getCpxPools() {
        return rules4DbInteractBean.getCrgRulePools(getRuleType(), ClientManager.getCurrentCpxUserId());
    }

    @Override
    public List<CrgRules> getRules4Year(long pPoolId) {
        return rules4DbInteractBean.findRulesForPool(pPoolId, getRuleType());
    }

    @Override
    public int findSizeOfRules(long pPoolId) {
        return rules4DbInteractBean.countRules4PoolId(pPoolId, getRuleType()).intValue();
    }

    @Override
    public List<CrgRuleTables> findRuleTables4Year(long pPoolId) {
        return rules4DbInteractBean.getRuleTables4Pool(getRuleType(), pPoolId);
    }

    @Override
    public List<CrgRuleTables> findRuleTables4YearAndContentOrName(long pPoolId, String pText) {
        return rules4DbInteractBean.getRuleTables4YearAndContentOrName(getRuleType(), pPoolId, pText);
    }

    @Override
    public List<CrgRuleTables> getCpxTables() {
        return null;
    }

    @Override
    public CrgRuleTables getCpxTable4Year(String pName, long pPoolId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte[] getRuleDefinition(long pRuleId, long pPoolId) {
        return null;
    }

    @Override
    public List<CdbUserRoles> findUserRoles2Rule(long pRuleId) {
        return null;
    }

    @Override
    public String getRuleTableContent(long pTableId) {
        return rules4DbInteractBean.getContent2RuleTable(pTableId, getRuleType());
    }

    @Override
    public String getRuleTypeNameForRuleAndYear(long pRuleId, long pPoolId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getRuleTypeDisplayNameForRuleAndYear(long pRuleId, long pPoolId) {
        return null;
    }

//    private void distributeCpRules() {
//         if (mAllRulesWithYears == null ) {
//            return;
//        }
//         for (CRGRule rule : mAllRulesWithYears) {
//             try{
//                long ruleId = Long.parseLong(rule.m_rid);
//                rule2Id.put(ruleId, rule);
//             }catch (NumberFormatException e) {
//                LOG.log(Level.INFO, " rule.m_rid " + rule.m_rid + "could not be converted to Long");
//            }
//             
//        }
//         for(CrgRules cpxRule: cpxRuleList) {
//             try {
//                 long poolId = (long)cpxRule.getCrgRulePools().getId();
//                 Map<Long, CrgRules> poolRules = cpxPools2Rules.get(poolId);
//                 if(poolRules == null){
//                     poolRules = new HashMap<>();
//                     cpxPools2Rules.put(poolId, poolRules);
//                 }
//                 poolRules.put(cpxRule.getId(), cpxRule);
//                 CrgRulePools pool =  cpxPools.get(poolId);
//                 if(pool == null){
//                     
//                     cpxPools.put(poolId, pool);
//                 }
//
//                 cpxRules.put(cpxRule.getId(), cpxRule);
//                 
//             } catch (Exception ex) {
//                 LOG.log(Level.SEVERE, "Error on distributing of rules from DB", ex);
//             }
//        };
//    }
    @Override
    public int findSizeOfRuleTables2Pool(long pPoolId) {
        return rules4DbInteractBean.countRuleTables4PoolId(pPoolId, getRuleType()).intValue();
    }

    @Override
    public List<String> getRids4SearchOptions(Map<String, List<String>> pFilterOptionMap) {
        return rules4DbInteractBean.getRids4SearchOptions(getRuleType(), pFilterOptionMap);
    }

    @Override
    public void resetRules() {
        rules2UserID.remove(getClientKey());
    }

    @Override
    public List<CrgRules> findRules4TableAndYear(long pPoolId, long pTableId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CrgRules> getAllRules4PoolAndRole(long pPoolId, Long pRoleId, boolean is4role) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long getRuleCountForRole(long pPoolId, long pRoleId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CrgRules> getCpxRules4ids(List<Long> ruleIds) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, String> getRuleTypes2Ids() {
        Map<String, String> retMap = new HashMap<>();
        List<CrgRuleTypes> types = rules4DbInteractBean.getRuleTypesAsList(getRuleType());
        if (types == null) {
            return retMap;
        }
        for (CrgRuleTypes type : types) {
            retMap.put(String.valueOf(type.getId()), type.getCrgtShortText());
        }
        return retMap;
    }
}
