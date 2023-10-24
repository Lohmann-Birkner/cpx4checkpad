/*
 * Copyright (c) 2019 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.rule.services;

import de.checkpoint.ruleGrouper.CRGRule;
import de.checkpoint.ruleGrouper.data.CRGRuleTypes;
import de.checkpoint.server.data.caseRules.DatCaseRuleMgr;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.commonDB.dao.CdbUserRolesDao;
import de.lb.cpx.server.commonDB.dao.CdbUsersDao;
import de.lb.cpx.server.commonDB.dao.Rules4DbInteractBean;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.server.commonDB.model.CdbUsers;
import de.lb.cpx.server.commonDB.model.rules.CrgRule2Role;
import de.lb.cpx.server.commonDB.model.rules.CrgRule2Table;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.server.commonDB.model.rules.RuleTablesIF;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.server.commons.enums.RuleImportCheckFlags;
import de.lb.cpx.server.commons.enums.RuleOverrideFlags;
import de.lb.cpx.service.rules.RulesExchangeHelper;
import de.lb.cpx.service.rules.RulesImportHelper;
import de.lb.cpx.service.rules.RulesTransferFactoryBean;
import de.lb.cpx.shared.rules.util.CpTable;
import de.lb.cpx.shared.rules.util.RuleValidationStatusEn;
import de.lb.cpx.shared.rules.util.RulesConverter;
import de.lb.cpx.shared.rules.util.RulesImportStatus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author wilde
 */
@Remote
@Stateless
public class RuleExchangeBean implements RuleExchangeBeanRemote {

    private static final Logger LOG = Logger.getLogger(RuleExchangeBean.class.getName());

    @Inject
    private RulesTransferFactoryBean rulesTranserFactoryBean;

    @Inject
    private Rules4DbInteractBean rules4DbInteractBean;

//    @EJB
//    private RuleEditorBean4DB ruleEditorBean4DB;
    @Inject
    private CdbUserRolesDao userRolesDao;

    @Inject
    private CdbUsersDao userDao;

    @Override
    public String createRulePoolInDb(String poolName, int year) {
        try {
            CrgRulePools pool = getOrCreateRulePool(poolName, year, PoolTypeEn.PROD, null);
            return pool.toString();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error on creating of rule pool with name = " + poolName + " and year " + String.valueOf(year), ex);
            return "Error on creating of rule pool with name = " + poolName + " and year " + String.valueOf(year) + " " + ex.getMessage();
        }

    }

    /**
     * checks whether exists the rule pool with poolName and year and creates if
     * not
     *
     * @param poolName pool name
     * @param year pool year
     * @return found or created pool in DB
     * @throws Exception
     */
    private CrgRulePools getOrCreateRulePool(String poolName, int year, PoolTypeEn what, Long userId) throws Exception {
        CrgRulePools pool = rules4DbInteractBean.getRulePool(poolName, year, what);
        if (pool == null) {
            pool = CrgRulePools.getTypeInstance(what);
            pool.setCrgplIdentifier(poolName);
            pool.setCrgplPoolYear(year);
            pool.setCrgplFrom(getFirstDayOfYear(year));
            pool.setCrgplTo(getLastDayOfYear(year));
            setCreationUser(pool, what, userId);
            rules4DbInteractBean.savePool(pool);
        }
        return pool;

    }

    private static Date getFirstDayOfYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, Calendar.JANUARY, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    private static Date getLastDayOfYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, Calendar.DECEMBER, 31);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    @Override
    public String saveRuleTypesInDB() {
        try {
            Map<String, CrgRuleTypes> types = getOrCreateRuleTypes(RulesTransferFactoryBean.TRANSFER_TYPE.EXPORT_INTERN);
            if (types == null) {
                LOG.log(Level.WARNING, "Could not read rule types on server path");
                return "Could not read rule types on server path";
            } else {
                StringBuilder ret = new StringBuilder();
                Set<String> cptypes = types.keySet();
                for (String cpType : cptypes) {
                    ret.append(cpType).append("<br>");
                }
                return ret.toString();
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error on reading of rule types from path", ex);
            return "Error on reading of rule types from path" + ex.getMessage();
        }

    }

    private Map<String, CrgRuleTypes> getOrCreateRuleTypes(RulesTransferFactoryBean.TRANSFER_TYPE type) throws Exception {
        List<CRGRuleTypes> cpRuleTypes;
        if (rulesTranserFactoryBean != null) {
            cpRuleTypes = rulesTranserFactoryBean.getRuleTypes(type);
            if (cpRuleTypes == null) {
                LOG.log(Level.WARNING, "Could not read rule types on server path");
                return null;
            } else {
                return getOrCreateRuleTypes(cpRuleTypes);
            }
        } else {
            LOG.log(Level.WARNING, "Could not find rule types on server path");
            return null;
        }

    }

    /**
     * reads the rule types file from the defined path on the file system and
     * saves the CrgRuleTypes objects in the DB. In doing so checks, whether any
     * rule type in the DB has the ident of the read rule type and if so updates
     * it form the
     *
     * @return
     * @throws Exception
     */
    private Map<String, CrgRuleTypes> getOrCreateRuleTypes(List<CRGRuleTypes> cpRuleTypes) throws Exception {
        return getOrCreateRuleTypes(cpRuleTypes, new RulesExchangeHelper(), RuleOverrideFlags.SAVE_NEW);
    }

    private Map<String, CrgRuleTypes> getOrCreateRuleTypes(List<CRGRuleTypes> cpRuleTypes, RulesExchangeHelper helper, RuleOverrideFlags doWhat) throws Exception {
        return getOrCreateRuleTypes(cpRuleTypes, helper, doWhat, PoolTypeEn.PROD, null);
    }

    private Map<String, CrgRuleTypes> getOrCreateRuleTypes(List<CRGRuleTypes> cpRuleTypes, RulesExchangeHelper helper, RuleOverrideFlags doWhat, PoolTypeEn pType, Long userId) throws Exception {

        Map<String, CrgRuleTypes> oldRuleTypes = rules4DbInteractBean.getRuleTypes(pType);

        for (CRGRuleTypes rType : cpRuleTypes) {
            CrgRuleTypes oldType = oldRuleTypes.get(rType.crgrt_shorttext);
            RuleValidationStatusEn flag = RuleValidationStatusEn.NEW_RULE_TYPE;
            if (oldType == null) {
                oldType = RulesConverter.getCpxRuleType(rType, pType);
                setCreationUser(oldType, pType, userId);
                rules4DbInteractBean.saveRuleType(oldType);
            } else {
                oldType = RulesConverter.updatecpxTypeFromCpType(rType, oldType, pType);
                flag = RuleValidationStatusEn.RULE_TYPE_FOUND_IN_DB;
            }
            if (helper != null) {
                helper.addRuleType(flag, oldType);
            }

            oldRuleTypes.put(oldType.getCrgtShortText(), oldType);
        }
        return oldRuleTypes;

    }

    @Override
    public String saveRules4PoolInDB(String poolName, int year) {
        return saveRules4PoolInDB(poolName, year, RuleOverrideFlags.SAVE_NEW, PoolTypeEn.PROD, null);
    }

    public String saveRules4PoolInDB(String poolName, int year, RuleOverrideFlags doWhat, PoolTypeEn pType, Long userId) {
        RulesExchangeHelper helper = new RulesExchangeHelper();
        try {
            if (rulesTranserFactoryBean == null) {
                return "rulesTranserFactoryBean is not found";
            }
// prepare all data from DB            
// check rule pool     
            CrgRulePools pool = getOrCreateRulePool(poolName, year, pType, userId);
// check rule types 
            Map<String, CrgRuleTypes> ruleTypes = getOrCreateRuleTypes(RulesTransferFactoryBean.TRANSFER_TYPE.IMPORT_INTERN);
// read rules from path        
            List<CRGRule> cpRules = rulesTranserFactoryBean.getRules4Pool(poolName, year, RulesTransferFactoryBean.TRANSFER_TYPE.IMPORT_INTERN);

            helper = this.checkRuleTables4PoolInDB(pool, helper);
// now save rules
            helper = saveRulesInDB(helper, pool, ruleTypes, cpRules, doWhat);
            /// ab here
            return helper.toString();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error on writing of rules for pool " + poolName + " " + String.valueOf(year), ex);
            helper.addLoggerText("Error on reading of writing of rules  from path" + poolName + " " + String.valueOf(year) + ": " + ex.getMessage());
            return helper.toString();
        }

    }

    /**
     * saves CP Rules in DB tables, the rule tables are to be already saved in
     * the DB
     *
     * @param helper
     * @param pool pool for the rules
     * @param ruleTypes known rule types in the system, new rule types are to be
     * already saved in DB
     * @param cpRules
     * @param doWhat - how to handle doubles -> TODO.
     * @return
     */
    private RulesExchangeHelper saveRulesInDB(RulesExchangeHelper helper,
            CrgRulePools pool,
            Map<String, CrgRuleTypes> ruleTypes,
            List<CRGRule> cpRules,
            RuleOverrideFlags doWhat) {
        return saveRulesInDB(helper,
                pool,
                ruleTypes,
                cpRules,
                doWhat, null);
    }

    private RulesExchangeHelper saveRulesInDB(RulesExchangeHelper helper,
            CrgRulePools pool,
            Map<String, CrgRuleTypes> ruleTypes,
            List<CRGRule> cpRules,
            RuleOverrideFlags doWhat, long[] roleIds) {
        return saveRulesInDB(helper,
                pool,
                ruleTypes,
                cpRules,
                doWhat, roleIds,
                PoolTypeEn.PROD,
                null);
    }

    private RulesExchangeHelper saveRulesInDB(RulesExchangeHelper helper,
            CrgRulePools pool,
            Map<String, CrgRuleTypes> ruleTypes,
            List<CRGRule> cpRules,
            RuleOverrideFlags doWhat, long[] roleIds,
            PoolTypeEn pType,
            Long userId) {
        if (cpRules == null || cpRules.isEmpty()) {
            helper.setImportStatus(RuleValidationStatusEn.NO_RULES_4_IMPORT_FOUND);
            LOG.log(Level.WARNING, " could not read rules from rule path for {0} {1}", new Object[]{pool.getCrgplIdentifier(), String.valueOf(pool.getCrgplPoolYear())});
            helper.addLoggerText(" could not read rules from rule path for " + pool.getCrgplIdentifier() + " " + String.valueOf(pool.getCrgplPoolYear()));
            return helper;
        }
// get rule tables for this pool and year
        Map<String, CrgRuleTables> ruleTables = getTables4Pool(pool);
// get old rules from pool when exist
        Set<CrgRules> oldRules = pool.getCrgRuleses();
        Map<String, CrgRules> rulesMap = RulesConverter.getCpxRulesFromPoolAsMap(oldRules);
        if (rulesMap == null) {

            helper.addLoggerText("error on converting of rules set into hashMap");
            return helper;
        }
        for (CRGRule cpRule : cpRules) {
            String cpIdent = RulesConverter.getIdentConstantPart(cpRule.getRuleNumber());
            if (cpIdent == null || cpIdent.isEmpty()) {
                continue;
            }
            CrgRules oldRule = rulesMap.get(cpIdent);
            CrgRuleTypes cpxType = ruleTypes.get(cpRule.m_errorTypeText);

            CrgRules fromCpRule = RulesConverter.getCpxRule(cpRule, null, true, pType, cpxType, ruleTables, false);
            byte[] testDefinition = fromCpRule.getCrgrDefinition();
            CRGRule testFrom = new CRGRule(RulesConverter.getRuleDomElementFromDbString(testDefinition), pool.getCrgplIdentifier(), null, CRGRule.RULE_ANALYSE_NO_TABLES);
            CRGRule cpTest = RulesConverter.getCpRuleWithTablesIds(cpRule, cpxType, ruleTables);
//            LOG.log(Level.INFO, "cpTest: " + cpTest.toString());
//            LOG.log(Level.INFO, "testFrom: " + testFrom.toString());

            if (cpxType == null) {
//                    helper.getNewRuleTypes().add(cpRule.m_errorTypeText);
                helper.addRuleType(RuleValidationStatusEn.RULE_TYPE_NOT_FOUND, cpRule.m_errorTypeText, fromCpRule);
                // helper.
// create a new rule type for m_errorTypeText; shortText = longText
                try {
                    cpxType = createCpxRuleType(cpRule.m_errorTypeText, pType, userId);
                    ruleTypes.put(cpRule.m_errorTypeText, cpxType);
                    // we do it once more, because of setting ids into rules instead of definitions
                    fromCpRule = RulesConverter.getCpxRule(cpRule, null, true, pType, cpxType, ruleTables, false);
                } catch (Exception ex) {
                    if (oldRule != null) {
                        helper.getRules4NewTypes().add(oldRule.getCrgrIdentifier());
                    }
                    // érror on creating of new  rule type ignore Rule 
                    LOG.log(Level.SEVERE, " rule type for rule " + cpRule.getRuleID() + ": " + cpRule.toString() + " not found, could not be created, rule ignored", ex);
                    helper.addLoggerText("rule type for rule " + cpRule.getRuleID() + ": " + cpRule.toString() + " not found, rule ignored<br>");
                    continue;
                }

            }

            Set<CrgRule2Table> rule2tables = new HashSet<>(); // rule to tables
            Set<CrgRule2Role> rule2roles = new HashSet<>();// rule to user roles
            boolean doPersist = false;

            List<String> tableNames = new ArrayList<>(cpRule.m_tableNames);
            if (oldRule == null) {
                oldRule = fromCpRule;
                oldRule.setCrgRuleTypes(cpxType);
                oldRule.setCrgRulePools(pool);
// we save rules always, tables that are used in these rules and are not found in DB can be imported later, the user gets logging
                helper.addRule(RuleValidationStatusEn.NEW_RULE, fromCpRule);
                doPersist = true;
                testDefinition = fromCpRule.getCrgrDefinition();
                testFrom = new CRGRule(RulesConverter.getRuleDomElementFromDbString(testDefinition), pool.getCrgplIdentifier(), null, CRGRule.RULE_ANALYSE_NO_TABLES);

//            LOG.log(Level.INFO, "testFrom: " + testFrom.toString());
            } else {
// TODO: we have to decide what to do with rules with same RIDs. Now we override rules and write logging 
                rule2tables = oldRule.getCrgRule2Tables();
                rule2roles = oldRule.getCrgRule2Roles();
                byte[] ruleDefinition = oldRule.getCrgrDefinition();

                CRGRule cpxOld = new CRGRule(RulesConverter.getRuleDomElementFromDbString(ruleDefinition), pool.getCrgplIdentifier(), null, CRGRule.RULE_ANALYSE_NO_TABLES);
                if (!cpTest.toString().equals(cpxOld.toString()) || oldRule.getCrgrRid() == null || oldRule.getCrgrRid().isEmpty()) {

                    fromCpRule = RulesConverter.getCpxRule(cpRule, oldRule, true, pType, cpxType, ruleTables, false);
// TODO: now we save rules always, tables that are used in these rules and are not found in DB can be imported later, the user gets logging
                    testDefinition = fromCpRule.getCrgrDefinition();
                    testFrom = new CRGRule(RulesConverter.getRuleDomElementFromDbString(testDefinition), pool.getCrgplIdentifier(), null, CRGRule.RULE_ANALYSE_NO_TABLES);

                    LOG.log(Level.INFO, "testFrom: {0}", testFrom);
                    doPersist = true;

                    helper.addRule(RuleValidationStatusEn.SAME_RULE_OTHER_LOGIC, oldRule, cpxOld.toString() + " : " + cpRule.toString());
                    oldRule = fromCpRule;
                } else {
                    helper.addCompareRuleHeader(oldRule, fromCpRule);

                }

            }
            if (doWhat.equals(RuleOverrideFlags.CHECK_ONLY)) {
// here we pick all the tables from imported rules, which are not in DB. 
// Till this point of time all rule tables, which were imported with the external file were analysed already.
// now we check only those tables, which are not found in file. They are either in database or missed
                if (!tableNames.isEmpty()) {
                    for (String tableName : tableNames) {
                        CrgRuleTables cpxTable = ruleTables.get(tableName);
                        if (cpxTable == null) {
                            helper.addTable(RuleValidationStatusEn.NO_TABLE_FOUND, tableName);
                        } else {
                            helper.addTable(RuleValidationStatusEn.NO_TABLE_IN_XML_BUT_IN_DB_FOUND, tableName);
                        }
                    }
                }
                continue;
            }
            ArrayList<CrgRule2Table> rule2Tables2delete = new ArrayList<>();
// are the tables that are used in this rule already saved in DB? when not add to the table list, which will be saved at the end of rule import                   
            if (!tableNames.isEmpty()) {
                if (rule2tables == null) {
                    rule2tables = new HashSet<>();
                }
                oldRule.setCrgRule2Tables(rule2tables);
                // check, whether some old table references are not used anymore     

                for (CrgRule2Table rr2t : rule2tables) {
                    CrgRuleTables tab = rr2t.getCrgRuleTables();
                    if (tab == null) {
                        continue;
                    }
                    if (!tableNames.contains(tab.getCrgtTableName())) {
                        rule2Tables2delete.add(rr2t);
                    }
                }
                for (String tableName : tableNames) {
                    if (ruleTables.get(tableName) == null) {
                        helper.addNotFoundTable4Rule(cpRule, tableName);
                    } else {
                        // fill rule2table 
                        boolean found = false;
                        for (CrgRule2Table rr2t : rule2tables) {
                            CrgRuleTables tab = rr2t.getCrgRuleTables();
                            if (tab == null) {
                                continue;
                            }
                            if (tab.getCrgtTableName().equals(tableName)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            CrgRule2Table rr2t = CrgRule2Table.getTypeInstance(pType);
                            rr2t.setCrgRuleTables(ruleTables.get(tableName));
                            rr2t.setCrgRules(oldRule);
                            rule2tables.add(rr2t);
                            doPersist = true;
                        }

                    }
                }

            } else {
                if (rule2tables != null && !rule2tables.isEmpty()) {
                    // remove when not used
                    rule2Tables2delete.addAll(rule2tables);
                }

            }
            if (!rule2Tables2delete.isEmpty()) {
                for (CrgRule2Table rr2t : rule2Tables2delete) {
                    rule2tables.remove(rr2t);
                    rules4DbInteractBean.deleteRule2Tables(rr2t);
                }

                oldRule.setCrgRule2Tables(rule2tables.isEmpty() ? null : rule2tables);
                doPersist = true;
            }
            // fill rule2role
            ArrayList<CrgRule2Role> roles2delete = new ArrayList<>();
            long[] cpRoleIds = cpRule.m_roles;
            if (roleIds != null) {
                cpRoleIds = roleIds;
            }
            if (cpRoleIds == null || cpRoleIds.length == 0) {
                helper.addLoggerText("rule rid = " + cpRule.getRuleID() + ": " + cpRule.toString() + " has no role assignments<br>");
                LOG.log(Level.WARNING, "rule rid = {0}: {1} has no role assignments", new Object[]{cpRule.getRuleID(), cpRule});
                if (rule2roles != null && !rule2roles.isEmpty()) {
                    // remove role assignments
                    roles2delete.addAll(rule2roles);
                }
            } else {

                if (rule2roles == null) {
                    rule2roles = new HashSet<>();

                }
                oldRule.setCrgRule2Roles(rule2roles);
                // which roles are not used anymore    
                ArrayList<Long> usedRoles = new ArrayList<>();
                for (CrgRule2Role r2r : rule2roles) {
                    CdbUserRoles role = r2r.getCdbUserRoles();
                    if (role == null) {
                        continue;
                    }
                    if (!DatCaseRuleMgr.isForRole(role.getId(), cpRoleIds)) {
                        roles2delete.add(r2r);
                    } else {
                        usedRoles.add(role.getId());

                    }
                }
                // are there new roles?
                for (long ll : cpRoleIds) {
                    if (!usedRoles.contains(ll)) {
                        // find role to id                  
                        CdbUserRoles role = userRolesDao.findById(ll);
                        if (role == null) {
                            helper.addLoggerText("rule rid = " + cpRule.getRuleID() + ": " + cpRule.toString() + " can not be assigned to the role roleID:" + ll + "<br>");
                            LOG.log(Level.WARNING, "rule rid = {0}: {1} can not be assigned to the role roleID:{2}", new Object[]{cpRule.getRuleID(), cpRule, ll});
                        } else {
                            CrgRule2Role rr2r = CrgRule2Role.getTypeInstance(pType);
                            rr2r.setCdbUserRoles(role);
                            rr2r.setCrgRules(oldRule);
                            rule2roles.add(rr2r);
                            doPersist = true;
                        }
                    }
                }

            }
            // delete not used role assignements
            if (!roles2delete.isEmpty()) {
                for (CrgRule2Role rr2r : roles2delete) {
                    rule2roles.remove(rr2r);
                    rules4DbInteractBean.deleteRule2Role(rr2r);
                    doPersist = true;
                }
            }
            if (doPersist) {
                oldRule.setCrgrIdentifier(String.valueOf(pool.getCrgplPoolYear()) + "_" + cpIdent);
                LOG.log(Level.INFO, oldRule.getCrgrIdentifier());
                setCreationUser(oldRule, pType, userId);
                rules4DbInteractBean.saveRule(oldRule);

                if (oldRule.getCrgrRid() == null || oldRule.getCrgrRid().isEmpty()) {
                    oldRule.setCrgrRid(String.valueOf(oldRule.getId()));
                }
            }
            helper.addLoggerText(cpRule.toString() + "<br>");
        }
        helper.add2LogNotFoundTables4Rules();

        return helper;

    }

    /**
     * returns the List of ruleTables in pool organised as Map name->table
     *
     * @param pool
     * @return
     */
    private Map<String, CrgRuleTables> getTables4Pool(CrgRulePools pool) {

        if (pool == null) {
            return null;
        }
        Map<String, CrgRuleTables> retMap = new HashMap<>();
        Set<CrgRuleTables> tables = pool.getCrgRuleTableses();
        if (tables == null) {
            return retMap;
        }

        for (CrgRuleTables table : tables) {
            String name = table.getCrgtTableName();
            if (name != null && !name.isEmpty()) {
                retMap.put(name, table);
            }
        }
        return retMap;

    }

    @Override
    public String saveRuleTables4PoolInDB(String poolName, int year) {
        try {
            if (rulesTranserFactoryBean == null) {
                return "rulesTranserFactoryBean is not found";
            }
// check rule pool     
            CrgRulePools pool = getOrCreateRulePool(poolName, year, PoolTypeEn.PROD, null);
            RulesExchangeHelper helper = checkRuleTables4PoolInDB(pool, null);
// save from helper
//            if(helper == null){
//                LOG.log(Level.WARNING, "Error on writing of rule tables for pool " + poolName + " " + String.valueOf(year));
//                return "Error on writing of rule tables for pool " + poolName + " " + String.valueOf(year);
//            }
            StringBuilder lRet = new StringBuilder();
            helper.addNewTableList(lRet);
            helper.addChangedTableList(lRet);

            return helper.toString();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error on writing of rule tables for pool " + poolName + " " + String.valueOf(year), ex);
            return "Error on reading of writing of rule tables  for pool " + poolName + " " + String.valueOf(year) + ": " + ex.getMessage();

        }

    }

    private RulesExchangeHelper checkRuleTables4PoolInDB(CrgRulePools pool, RulesExchangeHelper helper) {
        if (helper == null) {
            helper = new RulesExchangeHelper();
        }
        if (pool == null) {
            helper.addLoggerText("pool is null cannot look for tables");
            return helper;
        }
// read rule tables from path
        Map<String, RuleTablesIF> tables = rulesTranserFactoryBean.getRuleTables4Pool(pool.getCrgplIdentifier(), pool.getCrgplPoolYear(), RulesTransferFactoryBean.TRANSFER_TYPE.IMPORT_INTERN);
        return saveTablesInDB(pool, helper, tables);

    }

    private RulesExchangeHelper saveTablesInDB(CrgRulePools pool, RulesExchangeHelper helper, Map<String, RuleTablesIF> tables) {
        return saveTablesInDB(pool, helper, tables, RuleOverrideFlags.SAVE_NEW, PoolTypeEn.PROD, null);
    }

//    private RulesExchangeHelper saveTablesInDB(CrgRulePools pool, RulesExchangeHelper helper, Map<String, String> tables, RuleOverrideFlags doWhat) {
//        return saveTablesInDB(pool, helper, tables, doWhat, PoolTypeEn.PROD, null);
//    }
    private RulesExchangeHelper saveTablesInDB(CrgRulePools pool, RulesExchangeHelper helper, Map<String, RuleTablesIF> tables, RuleOverrideFlags doWhat, PoolTypeEn pType, Long userId) {
        if (tables == null) {
            LOG.log(Level.WARNING, "Error on reading of writing of rule tables  for pool {0} {1}", new Object[]{pool.getCrgplIdentifier(), String.valueOf(pool.getCrgplPoolYear())});
            helper.addLoggerText("Error on reading of writing of rule tables  for pool " + pool.getCrgplIdentifier() + " " + pool.getCrgplPoolYear());
            return helper;

        }
        if (tables.isEmpty()) {
            LOG.log(Level.WARNING, "no tables found for  pool {0} {1}", new Object[]{pool.getCrgplIdentifier(), String.valueOf(pool.getCrgplPoolYear())});
            helper.addLoggerText("no tables found for  pool " + pool.getCrgplIdentifier() + " " + pool.getCrgplPoolYear());
            return helper;
        }

// get rule tables for this pool and year
        Map<String, CrgRuleTables> ruleTables = getTables4Pool(pool);
        if (ruleTables == null) {
            //LOG.log(Level.WARNING, "Error on reading of rule tables  from DB forpool {0} {1}", new Object[]{pool.getCrgplIdentifier(), String.valueOf(pool.getCrgplPoolYear())});
            String errorMessage = "Error on writing of rule tables for pool " + pool;
            LOG.log(Level.WARNING, errorMessage);
            helper.addLoggerText(errorMessage);
            return helper;
        }
        Set<String> tableNames = tables.keySet();

        for (String tableName : tableNames) {
            CrgRuleTables newTable = RulesConverter.getCpxRuleTable(tableName, (CpTable)tables.get(tableName), pType);
            CrgRuleTables oldTable = ruleTables.get(tableName);
            newTable.setCrgRulePools(pool);

            if (oldTable == null) {
                helper.addTable(RuleValidationStatusEn.NEW_RULE_TABLE, tableName);
                pool.getCrgRuleTableses().add(newTable);
                oldTable = newTable;

            } else {
// compare contents now always override       
                if (RulesConverter.tableContentEquals(oldTable.getCrgtContent(), newTable.getCrgtContent())) {
                    helper.addTable(RuleValidationStatusEn.SAME_TABLE, tableName);
                    continue;
                }

                helper.addTable(RuleValidationStatusEn.SAME_TABLE_OTHER_CONTENT, tableName, oldTable.getCrgtContent(), newTable.getCrgtContent());
                oldTable.setCrgtContent(newTable.getCrgtContent());
            }
            if (!doWhat.equals(RuleOverrideFlags.CHECK_ONLY)) {
                setCreationUser(oldTable, pType, userId);
                rules4DbInteractBean.saveRuleTable(oldTable);
            }
        }
        return helper;

    }

    @Override
    public String importExternalRulesInDB(String poolName, int year, RuleOverrideFlags doOverride, long[] roleIdsList, String fileName) {
        return importExternalRulesInDB(poolName, year, doOverride, roleIdsList, fileName, PoolTypeEn.PROD, "admin");
    }

    @Override
    public String importExternalRulesInDB(String poolName, int year, RuleOverrideFlags doOverride, long[] roleIdsList, String fileName, PoolTypeEn what, String userName) {
        RulesExchangeHelper helper = new RulesExchangeHelper();
        try {
            Long userId = ClientManager.getCurrentCpxUserId();
            if (userId == null) {
                //               ClientManager.addCpxClient(fileName, pCpxUser);
                CdbUsers user = userDao.getCdbUsers(userName);
                if (user != null) {
                    userId = user.getId();
                }
            }
            if (rulesTranserFactoryBean == null) {
                return "rulesTranserFactoryBean is not found";
            }
            RulesImportHelper imp = rulesTranserFactoryBean.doRuleImportFromFile(poolName, year, fileName);
            if (imp == null) {
                LOG.log(Level.WARNING, " error on import rules from file {0} for year {1}", new Object[]{fileName, String.valueOf(year)});
                return " error on import rules from file " + fileName + " for year " + String.valueOf(year);
            }
            // prepare all data from DB            
// check rule pool     
            CrgRulePools pool = getOrCreateRulePool(poolName, year, what, userId);

            helper = saveTablesInDB(pool, helper, imp.getCpTables(), doOverride, what, userId);
// now save rules
            helper = saveRulesInDB(helper, pool,
                    getOrCreateRuleTypes(imp.getCpRuleTypes(), helper, doOverride, what, userId),
                    imp.getCpRules(), doOverride, roleIdsList, what, userId);
            /// ab here
            if (what.equals(PoolTypeEn.PROD)) {
                helper = rulesTranserFactoryBean.checkDoRefresh(poolName, year, RulesTransferFactoryBean.TRANSFER_TYPE.EXPORT_EXTERN, helper);
            }
            return helper.getJsonString();

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, " error on import rules from file " + fileName + " for year " + String.valueOf(year), ex);
            return " error on import rules from file " + fileName + " for year " + String.valueOf(year);
        }

    }

    @Override
    public String exportRulesFromDB(String poolName, int year, List<String> rules_rid_list, String fileName) {

        try {
            if (rulesTranserFactoryBean == null) {
                return "rulesTranserFactoryBean is not found";
            }

// check rule pool     
            CrgRulePools pool = getOrCreateRulePool(poolName, year, PoolTypeEn.PROD, null);

            List<CrgRules> rules;

            if (rules_rid_list == null || rules_rid_list.isEmpty()) {
                LOG.log(Level.INFO, "we export the whole pool {0} and year = {1}", new Object[]{poolName, String.valueOf(year)});
                Set<CrgRules> rr = pool.getCrgRuleses();

                rules = rr == null ? new ArrayList<>() : new ArrayList<>(rr);
            } else {
                rules = rules4DbInteractBean.getRules4RidList(rules_rid_list, pool);
            }
            if (rules == null) {
                LOG.log(Level.INFO, " no rules for pool {0} and year = {1} found", new Object[]{poolName, String.valueOf(year)});
                return " no rules for pool " + poolName + " and year = " + String.valueOf(year) + " found";
            }

            RulesExchangeHelper helper = rulesTranserFactoryBean.doRulesExportInFile(rules, fileName, pool.getCrgplPoolYear());
            if (fileName == null || fileName.isBlank()) {
                return helper.getXmlString4Export();
            } else {

                return helper.toString();
            }

        } catch (Exception ex) {
            String log = " all rules ";
            if (rules_rid_list != null) {
                String[] loggi = new String[rules_rid_list.size()];
                rules_rid_list.toArray(loggi);
                log = Arrays.toString(loggi);
            }
            LOG.log(Level.SEVERE, " Error on export rules,  " + log + " from pool = :" + poolName + " and year = " + String.valueOf(year) + " into file " + fileName, ex);
            return " Error on export rules,  " + log + " from pool = :" + poolName + " and year = " + String.valueOf(year) + " into file " + fileName;
        }

    }

    @Override
    public String saveRulesFromDB(String poolName, int year) {
        try {
            if (rulesTranserFactoryBean == null) {
                return "rulesTranserFactoryBean is not found";
            }

// check rule pool     
            CrgRulePools pool = getOrCreateRulePool(poolName, year, PoolTypeEn.PROD, null);
            Set<CrgRules> rr = pool.getCrgRuleses();

            RulesExchangeHelper helper = rulesTranserFactoryBean.doExportInternalRules(poolName, year, rr == null ? new ArrayList<>() : new ArrayList<>(rr));

            return helper.toString();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, " Error on export rules tables from pool = :" + poolName + " and year = " + String.valueOf(year) + " into file internal directory", ex);
            return " Error on export rules tables from pool = :" + poolName + " and year = " + String.valueOf(year) + " into file internal directory";

        }

    }

    /**
     * creates new CrgRuleType for short text
     *
     * @param m_errorTypeText
     * @return
     */
//    private CrgRuleTypes createCpxRuleType(String m_errorTypeText) throws Exception {
//        return createCpxRuleType(m_errorTypeText, PoolTypeEn.PROD, null);
//    }
    private CrgRuleTypes createCpxRuleType(String m_errorTypeText, PoolTypeEn typeEn, Long userId) throws Exception {

        CrgRuleTypes rType = CrgRuleTypes.getTypeInstance(typeEn);
        rType.setCrgtShortText(m_errorTypeText);
        rType.setCrgtDisplayText(m_errorTypeText);
        setCreationUser(rType, typeEn, userId);
        rules4DbInteractBean.saveRuleType(rType, typeEn);
        return rType;

    }

    @Override
    public RulesImportStatus importRulesInDB(String poolName, int year, RuleOverrideFlags doOverride, long[] roleIdsList, String fileName) {
        return importRulesInDB(poolName, year, doOverride, roleIdsList, fileName, "");
    }

    private RulesImportStatus importRulesInDB(String poolName, int year, RuleOverrideFlags doOverride, 
            long[] roleIdsList, String fileName, String ruleString) {
        RulesExchangeHelper helper = new RulesExchangeHelper();
        try {
            if (rulesTranserFactoryBean == null) {
                helper.addErrorStatus(RuleValidationStatusEn.ERROR, "rulesTranserFactoryBean is not found");
                return helper.getRulesImportStatus();
            }
            RulesImportHelper impHelper = rulesTranserFactoryBean.doRuleImportFromFile(poolName, year, fileName, ruleString, null, roleIdsList, 0, true);
            if (impHelper == null) {
                LOG.log(Level.WARNING, " error on import rules from file {0} for year {1}", new Object[]{fileName, String.valueOf(year)});
                helper.addErrorStatus(RuleValidationStatusEn.ERROR, " error on import rules from file " + fileName + " for year " + String.valueOf(year));
                return helper.getRulesImportStatus();
            }
            // prepare all data from DB            
// check rule pool     
            CrgRulePools pool = getOrCreateRulePool(poolName, year, PoolTypeEn.PROD, null);
            RuleExchangeResult result = new RuleExchangeResult();
// check and save types 
            Map<String, CrgRuleTypes> name2type = rules4DbInteractBean.importRuleTypes(impHelper.getRuleTypesAsCpx(PoolTypeEn.PROD), PoolTypeEn.PROD, result, doOverride, RuleImportCheckFlags.CHECK_4_COLLISIONS);
// check and save tables
            Map<String, CrgRuleTables> name2table = rules4DbInteractBean.importRuleTables(pool.getId(), impHelper.getValidRuleTablesAsCpx(PoolTypeEn.PROD), PoolTypeEn.PROD, result, doOverride, RuleImportCheckFlags.CHECK_4_COLLISIONS);

            result = rules4DbInteractBean.importRules(pool.getId(), impHelper.getRulesAsCpx(PoolTypeEn.PROD, name2type, name2table, result.getErrors(), true), PoolTypeEn.PROD, result,
                    doOverride, RuleImportCheckFlags.CHECK_4_COLLISIONS, roleIdsList);

//            helper = saveTablesInDB(pool, helper, imp.getCpTables(), doOverride);
//// now save rules
//            helper = saveRulesInDB(helper, pool,
//                    getOrCreateRuleTypes(imp.getCpRuleTypes(), helper, doOverride),
//                    imp.getCpRules(), doOverride, roleIdsList);
//            /// ab here
//            helper = rulesTranserFactoryBean.checkDoRefresh(poolName, year, RulesTransferFactoryBean.TRANSFER_TYPE.EXPORT_EXTERN, helper);
            return helper.getRulesImportStatus();

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, " error on import rules from file " + fileName + " for year " + String.valueOf(year), ex);
            helper.addErrorStatus(RuleValidationStatusEn.ERROR, " error on import rules from file " + fileName + " for year " + String.valueOf(year));
            return helper.getRulesImportStatus();
        }

    }

    @Override
    public RulesImportStatus validateExternalRulesInDB(String poolName, int year, String fileName, RuleOverrideFlags doOverride, long[] roleIdsList) {
        RulesExchangeHelper helper = new RulesExchangeHelper();
        try {
            if (rulesTranserFactoryBean == null) {
                helper.addErrorStatus(RuleValidationStatusEn.ERROR, "rulesTranserFactoryBean is not found");
                helper.setImportStatus(RuleValidationStatusEn.ERROR);
                return helper.getRulesImportStatus();
            }
            RulesImportHelper imp = rulesTranserFactoryBean.doRuleImportFromFile(poolName, year, fileName);
            if (imp == null) {
                LOG.log(Level.WARNING, " error on import rules from file {0} for year {1}", new Object[]{fileName, String.valueOf(year)});
                helper.addErrorStatus(RuleValidationStatusEn.ERROR, " error on import rules from file " + fileName + " for year " + year);
                helper.setImportStatus(RuleValidationStatusEn.ERROR);
                return helper.getRulesImportStatus();
            }
            if(imp.getCpRules() == null || imp.getCpRules().isEmpty()){
                 LOG.log(Level.WARNING, " error on import rules from file {0} for year {1}", new Object[]{fileName, String.valueOf(year)});
                helper.addErrorStatus(RuleValidationStatusEn.ERROR, " no rules in file " + fileName + " for year " + year + " found");
                RulesImportStatus st =  helper.getRulesImportStatus();
               st.setEndImportStatus(RuleValidationStatusEn.NO_RULES_4_IMPORT_FOUND);
               return st;
            }

            List<RuleExchangeError> result = new ArrayList<>();
            imp.validateCpRules(result, PoolTypeEn.PROD);
            if (imp.getCpRules() == null) {
                helper.addErrorStatus(RuleValidationStatusEn.ERROR, " error on import rules from file " + fileName + " for year " + year);

            } else if (!result.isEmpty()) {
                helper.createCollisionStatus(result);
                if(!imp.getErrRules().isEmpty()){
//                    List<CrgRuleTypes> cpxTypes = imp.getRuleTypesAsCpx(PoolTypeEn.PROD);
                    // create xml from errornious rules
                    // has to have list of all tables for export
                    RulesExchangeHelper tmpHelper = rulesTranserFactoryBean.doRulesExportInFile(imp.getCpxErrRules(PoolTypeEn.PROD), 
                            imp.getErrCpxTables(PoolTypeEn.PROD), null, 0); 
                    helper.saveErrorXmlString(tmpHelper.getXmlString4Export());
                    // set it into helper
                }
            }

            return helper.getRulesImportStatus(); 
//
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, " error on import rules from file " + fileName + " for year " + String.valueOf(year), ex);
            helper.addErrorStatus(RuleValidationStatusEn.ERROR, " error on import rules from file " + fileName + " for year " + String.valueOf(year));
            helper.setImportStatus(RuleValidationStatusEn.ERROR);
            return helper.getRulesImportStatus();
        }

    }
    
    
    /**
     * Generates an XMl - String from Rules from RID - List
     *
     * @param poolName pool name
     * @param year pool year
     * @param rules_rid_list list of rids
     * @return a generated XML - String
     */
    @Override
    public String getRulesFromDBAsXmlString(String poolName, int year, List<String> rules_rid_list) {

        return exportRulesFromDB(poolName, year, rules_rid_list, null);
    }

    /**
     * saves the rules, which are saved in the external xml form in DB, creates
     * pool if it does not exist, overrides the existing rule on set condition
     *
     * @param poolName pool name
     * @param year rool year
     * @param doOverride mode of override option
     * @param roleIdList list with selected role IDs
     * @param RulesString Rules as XML - String
     * @return status of import
     */
    @Override
    public RulesImportStatus importRulesInDBFromXmlString(String poolName, int year, RuleOverrideFlags doOverride, long[] roleIdList, String RulesString) {
        return this.importRulesInDB(poolName, year, doOverride, roleIdList, null, RulesString);
    }

    private void setCreationUser(AbstractEntity entity, PoolTypeEn poolType, Long userId) {
        if (poolType.equals(PoolTypeEn.DEV)) {
            entity.setCreationUser(userId);
        }
    }

    @Override
    public RulesImportStatus importRulesInDB(String poolName, int year, RuleOverrideFlags doOverride, long[] roleIdList, String fileName, List<String> types2create) {
        if(types2create != null && !types2create.isEmpty()){

            LOG.log(Level.INFO, "create new rule types");
            try {
                List<CrgRuleTypes> newRuleTypes = new ArrayList<>();
                Map<String, CrgRuleTypes> oldRuleTypes = rules4DbInteractBean.getRuleTypes(PoolTypeEn.PROD);
                 for(String type: types2create){
                     CrgRuleTypes cpxType = oldRuleTypes.get(type);
                     if(cpxType == null){
                         cpxType = CrgRuleTypes.getTypeInstance(PoolTypeEn.PROD);
                         cpxType.setCrgtShortText(type);
                         cpxType.setCrgtDisplayText(type);
                         newRuleTypes.add(cpxType);
                     }
                 }
                rules4DbInteractBean.importRuleTypes(newRuleTypes, PoolTypeEn.PROD, new RuleExchangeResult());
                
            } catch (RuleEditorProcessException ex) {
                Logger.getLogger(RuleExchangeBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return importRulesInDB(poolName, year, doOverride, roleIdList, fileName);
    }

}
