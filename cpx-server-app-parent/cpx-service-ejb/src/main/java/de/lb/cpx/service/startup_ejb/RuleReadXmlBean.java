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
import de.checkpoint.ruleGrouper.cpx.CPXFileManager;
import de.checkpoint.ruleGrouper.data.CRGRuleTypes;
import de.lb.cpx.model.enums.RuleTypeEn;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.commonDB.dao.CdbUser2RoleDao;
import de.lb.cpx.server.commonDB.dao.CdbUserRolesDao;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.server.commonDB.model.rules.CrgRule2Role;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.server.commonDB.model.rules.RuleTablesIF;
import de.lb.cpx.service.readrules.RuleReadServiceLocal;
import de.lb.cpx.shared.rules.util.CpTable;
import de.lb.cpx.shared.rules.util.RulesConverter;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.inject.Inject;

/**
 * On service start reads the checkpoint rules from the defined path into the
 * array of CRGRules
 *
 * @author gerschmann
 */
@Singleton
//@Startup
//@DependsOn("InitBean")
public class RuleReadXmlBean extends RuleReadServiceLocal
        implements RuleReadBeanLocal {

    private static final Logger LOG = Logger.getLogger(RuleReadXmlBean.class.getName());

//
//    private static CRGRule[] mAllRulesWithYears = new CRGRule[0]; // Regeln nach Jahren;
//    private static final HashMap<Integer, HashMap<String, CRGRule>> ruleIdYear = new HashMap<>();
//    private static final HashMap<Long, CRGRule> rule2Id = new HashMap<>();
//    private static final HashMap<Integer, Map<String, String>> year2Tables = new HashMap<>();
//// for rules description to be shown as readonly in client we need them es Cpx rules with type ids, table ids and pool ids
//    private static final HashMap<Long, CrgRuleTypes> cpxRuleTypes = new HashMap<>();
//    private static final HashMap<String,CrgRuleTypes> cpxName2RuleTypes = new HashMap<>();
//    private static final HashMap<Long,  CrgRuleTables>cpxRuleTables = new HashMap<>();
//    private static final HashMap<Long,  Map<String,CrgRuleTables>>cpxPools2Tables = new HashMap<>();
//    private static final HashMap<Long, CrgRules> cpxRules = new HashMap<>();
//    private static final HashMap<Long, Map<Long, CrgRules>> cpxPools2Rules = new HashMap<>();
//    private static final HashMap<Long, CrgRulePools> cpxPools = new HashMap<>();
//
//
//    @EJB(name = "CPXDBRulesManager")
//    private CpxDBRulesManagerLocal cpxDBRulesManager;
    @Inject
    private CdbUserRolesDao cdbUserRolesDao;

    @Inject
    private CdbUser2RoleDao cdbuser2RoleDao;

    public RuleReadXmlBean() {
        //
    }

    /**
     * reads rulePath from properties
     *
     */
    private static String getRulePath() {

        CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
        return cpxProps.getCpxServerRulesDir();
    }

//    @PostConstruct
    @Override
    public synchronized void init() {
        reloadRules();
    }

    @Override
    protected synchronized void reloadRules() {
        //if (CpxSystemProperties.getInstance().getCpxWebAppFile()) {
        //    LOG.log(Level.WARNING, "This code is not running in CPX EAR File, but in CPX Web App. Rule reading will be skipped to prevent multiple reads!");
        //    return;
        //}
        try {
            long start = System.currentTimeMillis();
            clearAllMaps();
            String path = getRulePath();
//            boolean isDBRules = cpxServerConfig.getRulesDatabaseConfig();
//            if (isDBRules) {
//                LOG.log(Level.INFO, " we read rules from database");
//                mAllRulesWithYears = cpxDBRulesManager.getAllRules();
//            } else {
            LOG.log(Level.INFO, " we read all rules for all roles from Path {0}", path);
            mAllRulesWithYears = (CPXFileManager.ruleManager()).getAllRulesFromPath(path, null);
//            }
            fillId2YearMap();

            distributeTables();
            distributeTypes();
            distributeRules();
            LOG.log(Level.INFO, MessageFormat.format(" there are {0} rules; time for rules: {1}", getRules().length, (System.currentTimeMillis() - start)));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Cannot load Rules", e);
        }
    }

    @Override
    public CRGRule[] getRules() {
        if (mAllRulesWithYears == null) {
            reloadRules();
            if (mAllRulesWithYears == null) {
                mAllRulesWithYears = new CRGRule[0];
            }
        }
        return Arrays.copyOf(mAllRulesWithYears, mAllRulesWithYears.length);
    }

    /**
     * distributes rules from mAllRulesWithYears into
     */
    private void distributeRules() {
        List<Long> activeRoles = cdbuser2RoleDao.getUser2RoleIdsByUserId(ClientManager.getCurrentCpxUserId());
//        ClientManager.getCpxRoles();
//        Calendar cal = Calendar.getInstance(Locale.getDefault());
        if (mAllRulesWithYears == null) {
            return;
        }
        for (CRGRule rule : mAllRulesWithYears) {
//            HashMap<String, CRGRule> rule2year = ruleIdYear.get(rule.getYear());
//            if (rule2year == null) {
//                rule2year = new HashMap<>();
//                ruleIdYear.put(rule.getYear(), rule2year);
//            }
//            rule2year.put(rule.m_rid, rule);
            try {
                long ruleId = Long.parseLong(rule.m_rid);
                Map<String, CrgRuleTables> cpxTables = cpxPools2Tables.get((long) rule.getYear());
                rule2Id.put(ruleId, rule);
                CrgRuleTypes cpxType = cpxName2RuleTypes.get(rule.m_errorTypeText);
                if (cpxType == null) {

                    cpxType = CrgRuleTypes.getTypeInstance(PoolTypeEn.PROD);
                    cpxType.setCrgtShortText(rule.m_errorTypeText);
                    cpxType.setCrgtDisplayText(rule.m_errorTypeText);
                    cpxType.setId(getMaxId(cpxRuleTypes.keySet()) + 1);
                    cpxRuleTypes.put(cpxType.getId(), cpxType);
                    cpxName2RuleTypes.put(rule.m_errorTypeText, cpxType);
                    cpxRuleTypesIds.put(String.valueOf(cpxType.getId()), rule.m_errorTypeText);
                }

                CrgRules cpxRule = RulesConverter.getCpxRule(rule, null, true, PoolTypeEn.PROD, cpxType, cpxTables, false);
                cpxRule.setId(ruleId);
                cpxRule.setCrgRuleTypes(cpxType);
                cpxRules.put(ruleId, cpxRule);
                Map<Long, CrgRules> poolRules = cpxPools2Rules.get((long) rule.getYear());
                if (poolRules == null) {
                    poolRules = new HashMap<>();
                    cpxPools2Rules.put((long) rule.getYear(), poolRules);
                }
                poolRules.put(ruleId, cpxRule);
                CrgRulePools pool = cpxPools.get((long) rule.getYear());
                if (pool == null) {
                    pool = RulesConverter.getCpxRulePool4Year(0, PoolTypeEn.PROD);
                    cpxPools.put((long) rule.getYear(), pool);
                }
                cpxRule.setCrgRulePools(pool);

                pool.getCrgRuleses().add(cpxRule);
                distributeRule2Tables(rule, cpxRule, cpxTables, pool.getId());
                distributeRule2Roles(rule, cpxRule, pool.getId(), activeRoles);

            } catch (NumberFormatException e) {
                Logger.getLogger(RuleReadXmlBean.class.getName()).log(Level.INFO, " rule.m_rid " + rule.m_rid + "could not be converted to Long");
            }

        }

    }

    private long getMaxId(Set<Long> ids) {
        List<Long> list = new ArrayList<>(ids);
        Collections.sort(list);
        return list.get(list.size() - 1);
    }

    /**
     * für die Regeln, die aus der Dateiesystem kommen brauchen wir schnellen
     * zugriff auf die Regeltabellen
     */
    private void distributeTables() {

        long tabId = 1;
        if (ruleIdYear.isEmpty()) {
            return;
        }
        Set<Integer> years = ruleIdYear.keySet();
        if (years == null) {
            return;
        }
        Iterator<Integer> itr = years.iterator();
        StringBuilder info = new StringBuilder();
        //
        while (itr.hasNext()) {

            Integer year = itr.next();
            CrgRulePools pool = cpxPools.get(year);
            if (pool == null) {
                pool = RulesConverter.getCpxRulePool4Year(year, PoolTypeEn.PROD);
                cpxPools.put((long) year, pool);
            }
            Map<String, CrgRuleTables> cpxPool = cpxPools2Tables.get(year);
            if (cpxPool == null) {
                cpxPool = new HashMap<>();
                cpxPools2Tables.put((long) year, cpxPool);
            }
            info.append(year).append(": ");
            Map<String, RuleTablesIF> tables = ((CPXFileManager) (CPXFileManager.ruleManager())).getRuleTables4Pool(String.valueOf(year), year);
            if (tables != null) {
                info.append(tables.keySet().size()).append("; ");
                year2Tables.put(year, tables);
                Set<String> tabs = tables.keySet();
                for (String tab : tabs) {
                    CrgRuleTables cpxTable = CrgRuleTables.getTypeInstance(PoolTypeEn.PROD);
                    cpxTable.setId(tabId);
                    cpxTable.setCrgtTableName(tab);
                    cpxTable.setCrgtContent(((CpTable)tables.get(tab)).getContent());
                    cpxTable.setCrgtComment(((CpTable)tables.get(tab)).getComment() == null?null:
                            CRGRule.getDisplayText(((CpTable)tables.get(tab)).getComment()));
                    cpxRuleTables.put(tabId, cpxTable);
                    cpxPool.put(tab, cpxTable);
                    pool.getCrgRuleTableses().add(cpxTable);
                    tabId++;
                }
            }
        }
        LOG.log(Level.INFO, " there are rule tables for: {0}", info);
    }

    /**
     * Returns rule to year and ruleId
     *
     * @param year rule year
     * @param ruleId rules id
     * @return rule
     */
    @Override
    public CRGRule getRule(int year, long ruleId) {
        HashMap<String, CRGRule> rule2year = ruleIdYear.get(year);
        if (rule2year != null) {
            return rule2year.get(String.valueOf(ruleId));
        }
        return null;
    }

    /**
     * returns the Array of CRGRule Objects to the List of ruleIds
     *
     * @param ruleIds list of ruleIds, which are to be used bei Grouping
     * @return the Array of CRGRule Objects to the List of ruleIds
     */
    @Override
    public CRGRule[] getRule2ListId(List<Long> ruleIds) {
        return super.getCpRules2Ids(ruleIds);
//        if (ruleIds != null) {
//            CRGRule[] ret = new CRGRule[ruleIds.size()];
//            int ind = 0;
//            for (Long id : ruleIds) {
//                CRGRule rule = rule2Id.get(id);
//                if (rule != null) {
//                    ret[ind] = rule;
//                    ind++;
//                }
//            }
//            if (ind < ruleIds.size()) {
//                CRGRule[] ret1 = new CRGRule[ind];
//                System.arraycopy(ret, 0, ret1, 0, ind);
//                return ret1;
//            }
//            return ret;
//        }
//        return null; //2017-12-01 DNi: return new CRGRule[0] instead?!
    }

    @Override
    public Map<String, CRGRule> getRulesForYear(int year) {
        HashMap<String, CRGRule> ret = ruleIdYear.get(year);
        LOG.log(Level.INFO, MessageFormat.format("getRulesForYear: for year: {0} there are {1} rules", year, (ret == null ? "null" : ret.size())));
        if (ret == null) {
// there are no rule for year found. 
            ret = new HashMap<>();
            ruleIdYear.put(year, ret);
        }
        return ret;
    }

    private void distributeTypes() {
        try {
            String path = getRulePath();
            List<CRGRuleTypes> types = new ArrayList<>((CPXFileManager.ruleManager()).getRuleTypes(path + "csrules_types.xml"));
            long id = 1;
            for (CRGRuleTypes cpType : types) {
                CrgRuleTypes cpxType = RulesConverter.getCpxRuleType(cpType);
                cpxType.setId(id);
                cpxRuleTypes.put(id, cpxType);
                cpxName2RuleTypes.put(cpxType.getCrgtShortText(), cpxType);
                 cpxRuleTypesIds.put(String.valueOf(id), cpxType.getCrgtShortText());
                id++;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Cannot load rule types", e);

        }
    }

//    @Override
//    public List<CRGRule> getRules4Pool(String pool, int year) {
//        try{
//            List<CRGRule> retList = new ArrayList<>();
//           CRGRule[] rules = getRules();
//           if(rules == null || rules.length == 0){
//               return retList;
//           }
//           for(CRGRule rule:rules){
//               if(rule.getYear() == year){
//                   retList.add(rule);
//               }
//           }
//           return retList;
//        }catch(Exception e){
//            LOG.log(Level.SEVERE, "Cannot load rules for pool " + pool + " " + year, e);   
//            return null;
//        }
//    }
    @Override
    public void reloadRules4Pool(String poolName, int year) {
        this.reloadRules();
    }

    @Override
    public CrgRules getRule2Id(long pRId) {
        CrgRules cpxRule = cpxRules.get(pRId);
        if(cpxRule == null){
            return cpxRule;
        }
        if(cpxRule.getCrgRule2Roles() == null|| cpxRule.getCrgRule2Roles().isEmpty())    {
            Set<CrgRule2Role> r2rs = new HashSet<>();
            List<CdbUserRoles> roles = findUserRoles2Rule(pRId);
            for(CdbUserRoles role: roles){
                CrgRule2Role r2r = CrgRule2Role.getTypeInstance();
                r2r.setCrgRules(cpxRule);
                r2r.setCdbUserRoles(role);
                r2rs.add(r2r);
            }
            cpxRule.setCrgRule2Roles(r2rs);
        }
        return cpxRule;
    }

    @Override
    public List<CrgRules> getCpxRules() {

        return new ArrayList<>(cpxRules.values());
    }

    @Override
    public List<CrgRuleTypes> getCpxTypes() {
        return new ArrayList<>(cpxRuleTypes.values());
    }

    @Override
    public List<CrgRulePools> getCpxPools() {
        if (cpxPools.isEmpty()) {
            this.reloadRules();
        }
        return new ArrayList<>(cpxPools.values());
    }

    @Override
    public List<CrgRules> getRules4Year(long pPoolId) {

        Map<Long, CrgRules> rule2id = cpxPools2Rules.get(pPoolId);
        if (rule2id != null) {
            return new ArrayList<>(rule2id.values());
        }
        return new ArrayList<>();
    }

    @Override
    public int findSizeOfRules(long pPoolId) {
        return getRules4Year(pPoolId).size();
    }

    @Override
    public int findSizeOfRuleTables2Pool(long pPoolId) {
        return findRuleTables4Year(pPoolId).size();
    }

    @Override
    public List<CrgRuleTables> findRuleTables4Year(long pPoolId) {
        Map<String, CrgRuleTables> table2id = cpxPools2Tables.get(pPoolId);
        if (table2id != null) {
            return new ArrayList<>(table2id.values());
        }
        return new ArrayList<>();

    }

    @Override
    public List<CrgRuleTables> findRuleTables4YearAndContentOrName(long pPoolId, String pText) {
        String text = Objects.requireNonNullElse(pText, "");
        List<CrgRuleTables> tables = findRuleTables4Year(pPoolId);
        tables.removeIf((CrgRuleTables t) -> (!t.getCrgtContent().toLowerCase().contains(text.toLowerCase())) || (!t.getCrgtTableName().toLowerCase().contains(text.toLowerCase())));
        return tables;
    }

    @Override
    public List<CrgRuleTables> getCpxTables() {

        return new ArrayList<>(cpxRuleTables.values());

    }

    @Override
    public CrgRuleTables getCpxTable4Year(String pName, long pPoolId) {
        List<CrgRuleTables> tables = findRuleTables4Year(pPoolId);
        for (CrgRuleTables table : tables) {
            if (table.getCrgtTableName().equalsIgnoreCase(pName)) {
                return table;
            }
        }
        return null;
    }

    @Override
    public byte[] getRuleDefinition(long pRuleId, long pPoolId) {
        Map<Long, CrgRules> rule2id = cpxPools2Rules.get(pPoolId);
        if (rule2id != null) {
            CrgRules rule = rule2id.get(pRuleId);
            if (rule != null) {
                return rule.getCrgrDefinition();
            }
        }
        return null;
    }

    @Override
    public List<CdbUserRoles> findUserRoles2Rule(long pRuleId) {
        List<CdbUserRoles> cpxRoles = new ArrayList<>();
        CRGRule rule = rule2Id.get(pRuleId);
        if (rule != null) {

            long[] roleIds = rule.m_roles;
            if (roleIds != null) {
                for (long role : roleIds) {
                    //CdbUserRoles cpxRole = cdbUserRolesDao.findRoleById(role);
                    CdbUserRoles cpxRole = cdbUserRolesDao.findById(role);
                    if (cpxRole != null) {
                        cpxRoles.add(cpxRole);
                    }
                }
            }
        }
        return cpxRoles;
    }

    @Override
    public String getRuleTableContent(long pTableId) {
        CrgRuleTables table = cpxRuleTables.get(pTableId);
        if (table != null) {
            return table.getCrgtContent();
        }
        return null;
    }

    @Override
    public String getRuleTypeNameForRuleAndYear(long pRuleId, long pPoolId) {
        CrgRuleTypes type = getRuleTypeForRuleAndYear(pRuleId, pPoolId);
        if (type != null) {
            return type.getCrgtShortText();
        }
        return null;
    }

    private CrgRuleTypes getRuleTypeForRuleAndYear(long pRuleId, long pPoolId) {
        Map<Long, CrgRules> rules = cpxPools2Rules.get(pPoolId);
        if (rules != null) {
            CrgRules rule = rules.get(pRuleId);
            if (rule != null) {
                return rule.getCrgRuleTypes();

            }
        }
        return null;

    }

    @Override
    public String getRuleTypeDisplayNameForRuleAndYear(long pRuleId, long pPoolId) {
        CrgRuleTypes type = getRuleTypeForRuleAndYear(pRuleId, pPoolId);
        if (type != null) {
            return type.getCrgtDisplayText();
        }
        return null;
    }

    @Override
    public List<String> getRids4SearchOptions(Map<String, List<String>> pFilterOptionMap) {
        List<String> rids = new ArrayList<>();
        List<CrgRules> rules = getCpxRules();
        for (CrgRules rule : rules) {
            if (checkRuleWithFilterOptions(rule, pFilterOptionMap)) {
                rids.add(rule.getCrgrRid());
            }
        }
        return rids;
    }

    private boolean checkRuleWithFilterOptions(CrgRules rule, Map<String, List<String>> pFilterOptionMap) {
        Set<String> fields = pFilterOptionMap.keySet();

        for (String field : fields) {
            List<String> orStrings = pFilterOptionMap.get(field);
            for (String orStr : orStrings) {
                switch (field) {
                    case "crgrRuleErrorType":
                        String[] typeStr = orStr.split(",");
                        boolean found = false;
                        for (String tStr : typeStr) {
                            if (rule.getCrgrRuleErrorType().equals(RuleTypeEn.valueOf(tStr))) {
                                //assignment to found seems to be strange/useless/unwanted... Maybe the parenthesis is wrong?
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                             return false;
                        }

                        break;

                    case "crgrCaption":
                        if (!rule.getCrgrCaption().contains(orStr)) {
                            return false;
                        }
                        /* else {
                            int i = 0;
                        } */
                        break;
                    case "crgrSuggText":
                        if (!rule.getCrgrSuggText().contains(orStr)) {
                            return false;
                        }
                        break;
                    case "crgrCategory":
                        if (!rule.getCrgrCategory().contains(orStr)) {
                            return false;
                        }
                        break;
                    case "crgrNumber":
                        if (!rule.getCrgrNumber().contains(orStr)) {
                            return false;
                        }
                        break;
                    default:
                        LOG.log(Level.WARNING, "Unknown field: {0}", field);
                }

            }
        }
        return true;
    }

    @Override
    public void resetRules() {
        LOG.log(Level.FINEST, "reset is not implemented in xml based rules");
    }

    @Override
    public List<CrgRules> findRules4TableAndYear(long pPoolId, long pTableId) {
        Map<Long, List<CrgRules>> tables2rules = cpxPoolId2TableId2Rules.get(pPoolId);
        if (tables2rules == null) {
            cpxPoolId2TableId2Rules.put(pTableId, new HashMap<>());
            return new ArrayList<>();
        }
        List<CrgRules> rules = tables2rules.get(pTableId);
        if (rules == null) {
            rules = new ArrayList<>();
            tables2rules.put(pTableId, rules);
        }
        return rules;
    }

    private void distributeRule2Tables(CRGRule rule, CrgRules cpxRule, Map<String, CrgRuleTables> cpxTables, long pPoolId) {
        Map<Long, List<CrgRules>> tables2rules = cpxPoolId2TableId2Rules.get(pPoolId);
        if (rule.getTableNames() == null) {
            return;
        }
        if (tables2rules == null) {
            tables2rules = new HashMap<>();
            cpxPoolId2TableId2Rules.put(pPoolId, tables2rules);
        }
        List<String> tableNames = new ArrayList<>(rule.getTableNames());
        for (String tableName : tableNames) {
            CrgRuleTables cpxTable = cpxTables.get(tableName);
            if (cpxTable == null) {
                LOG.log(Level.INFO, "For tableName = {0} cpxTable is not found in pool id = {1}", new Object[]{tableName, String.valueOf(pPoolId)});
                continue;
            }
            List<CrgRules> ruleList = tables2rules.get(cpxTable.getId());
            if (ruleList == null) {
                ruleList = new ArrayList<>();
                tables2rules.put(cpxTable.getId(), ruleList);
            }
            ruleList.add(cpxRule);
        }
    }

    private void distributeRule2Roles(CRGRule rule, CrgRules cpxRule, long pPoolId, List<Long> userRoles) {
        Map<Long, List<CrgRules>> rules2pool = cpxPoolId2RoleId2Rules.get(pPoolId);
        if (rules2pool == null) {
            rules2pool = new HashMap<>();
            cpxPoolId2RoleId2Rules.put(pPoolId, rules2pool);
        }
        Map<Long, List<CrgRules>> norules2pool = cpxPoolId2RoleIdNoRules.get(pPoolId);
        if (norules2pool == null) {
            norules2pool = new HashMap<>();
            cpxPoolId2RoleIdNoRules.put(pPoolId, norules2pool);
        }
        long[] ruleRoles = rule.m_roles;
        if (ruleRoles == null || ruleRoles.length == 0) {
            for (long role : userRoles) {
                List<CrgRules> rules = norules2pool.get(role);
                if (rules == null) {
                    rules = new ArrayList<>();
                    norules2pool.put(role, rules);
                }
                rules.add(cpxRule);
            }
            return;
        }
        List<Long> testRoles = new ArrayList<>(userRoles);
        for (long role : ruleRoles) {
            List<CrgRules> rules = rules2pool.get(role);
            if (rules == null) {
                rules = new ArrayList<>();
                rules2pool.put(role, rules);
            }
            rules.add(cpxRule);
            testRoles.remove(role);
        }
        for (long role : testRoles) {
            List<CrgRules> rules = norules2pool.get(role);
            if (rules == null) {
                rules = new ArrayList<>();
                norules2pool.put(role, rules);
            }
            rules.add(cpxRule);

        }
    }

    @Override
    public List<CrgRules> getAllRules4PoolAndRole(long pPoolId, Long pRoleId, boolean is4role) {
        if (is4role) {
            return getRule4PoolAndRole(pPoolId, pRoleId, cpxPoolId2RoleId2Rules);
        } else {
            return getRule4PoolAndRole(pPoolId, pRoleId, cpxPoolId2RoleIdNoRules);
        }
    }

    private List<CrgRules> getRule4PoolAndRole(long pPoolId, Long pRoleId, Map<Long, Map<Long, List<CrgRules>>> rool2rulesMap) {
        Map<Long, List<CrgRules>> rules2pool = rool2rulesMap.get(pPoolId);
        if (rules2pool == null) {
            rules2pool = new HashMap<>();
            rool2rulesMap.put(pPoolId, rules2pool);
        }
        List<CrgRules> rules = rules2pool.get(pRoleId);
        if (rules == null) {
            rules = new ArrayList<>();
            rules2pool.put(pRoleId, rules);
        }
        return rules;

    }

    @Override
    public Long getRuleCountForRole(long pPoolId, long pRoleId) {
        Map<Long, List<CrgRules>> rules2pool = cpxPoolId2RoleId2Rules.get(pPoolId);
        if (rules2pool == null) {
            rules2pool = new HashMap<>();
            cpxPoolId2RoleId2Rules.put(pPoolId, rules2pool);
        }
        List<CrgRules> rules = rules2pool.get(pRoleId);
        if (rules == null) {
            rules = new ArrayList<>();
            rules2pool.put(pRoleId, rules);
        }
        return (long) rules.size();
    }

    @Override
    public List<CrgRules> getCpxRules4ids(List<Long> ruleIds) {
        List<CrgRules> rules = new ArrayList<>();
        if (ruleIds != null) {

            for (Long id : ruleIds) {
                CrgRules rule = cpxRules.get(id);
                if (rule != null) {
                    rules.add(rule);
                }
            }

        }
        return rules;

    }

    @Override
    public Map<String, String> getRuleTypes2Ids() {
        return cpxRuleTypesIds;
    }
}
