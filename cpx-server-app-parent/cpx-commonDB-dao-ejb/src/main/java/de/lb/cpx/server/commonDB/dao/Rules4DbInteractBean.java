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
 *    2019  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commonDB.dao;

import com.google.common.collect.Lists;
import de.checkpoint.ruleGrouper.CRGRule;
import de.checkpoint.server.data.caseRules.DatRuleElement;
import de.lb.cpx.model.enums.RuleTypeEn;
import de.lb.cpx.rule.element.CaseRuleManager;
import de.lb.cpx.rule.element.model.Rule;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.server.commonDB.model.enums.RuleTableCategoryEn;
import de.lb.cpx.server.commonDB.model.rules.CrgRule2Role;
import de.lb.cpx.server.commonDB.model.rules.CrgRule2Table;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.server.commons.enums.RuleImportCheckFlags;
import de.lb.cpx.server.commons.enums.RuleOverrideFlags;
import de.lb.cpx.server.rule.services.RuleEditorProcessException;
import de.lb.cpx.server.rule.services.RuleExchangeError;
import de.lb.cpx.server.rule.services.RuleExchangeResult;
import de.lb.cpx.shared.rules.util.RuleValidationStatusEn;
import de.lb.cpx.shared.rules.util.RulesConverter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.hibernate.Hibernate;

/**
 * Encapsulates all interactions with Database Rule tables. Distinguishes
 * between DEV and PROD tables and applies the reqired actions to requered
 * tables
 *
 * @author gerschmann
 */
@Stateless
public class Rules4DbInteractBean {

    private static final Logger LOG = Logger.getLogger(Rules4DbInteractBean.class.getName());

    @Inject
    private CrgRulePoolsProdDao crgRulePoolProdDao;

    @Inject
    private CrgRulePoolsDevDao crgRulePoolDevDao;

    @Inject
    private CrgRuleTypesDevDao crgRuleTypesDevDao;

    @Inject
    private CrgRuleTypesProdDao crgRuleTypesProdDao;

    @Inject
    private CrgRuleTableDevDao crgRuleTablesDevDao;

    @Inject
    private CrgRuleTableProdDao crgRuleTablesProdDao;

    @Inject
    private CrgRule2TableDevDao crgRule2TableDevDao;

    @Inject
    private CrgRule2TableProdDao crgRule2TableProdDao;

    @Inject
    private CrgRule2RoleDevDao crgRule2RoleDevDao;

    @Inject
    private CrgRule2RoleProdDao crgRule2RoleProdDao;

    @Inject
    private CrgRulesDevDao crgRulesDevDao;

    @Inject
    private CrgRulesProdDao crgRulesProdDao;

    @Inject
    private CdbUserRolesDao userRolesDao;

    @Inject
    private CdbUser2RoleDao user2RoleDao;
    
    /**
     * returns a production pool with name and year
     *
     * @param poolName name
     * @param year year
     * @return pool instance for the name and year
     */
    public CrgRulePools getRulePool(String poolName, int year) {
        return getRulePool(poolName, year, PoolTypeEn.PROD);
    }

    @SuppressWarnings("unchecked")
    private <T extends CrgRulePoolsDao<E>, E extends CrgRulePools> T getPoolDao(PoolTypeEn poolTypeEn) {
        switch (poolTypeEn) {
            case DEV:
                return (T) crgRulePoolDevDao;
            case PROD:
                return (T) crgRulePoolProdDao;
            default:
                LOG.log(Level.WARNING, "unknown pool type: {0} -> use productive rule pool as fallback", poolTypeEn.name());
                return (T) crgRulePoolProdDao;
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends CrgRuleTypesDao<E>, E extends CrgRuleTypes> T getRuleTypesDao(PoolTypeEn poolTypeEn) {
        switch (poolTypeEn) {
            case DEV:
                return (T) crgRuleTypesDevDao;
            case PROD:
                return (T) crgRuleTypesProdDao;
            default:
                LOG.log(Level.WARNING, "unknown pool type: {0} -> use productive rule types as fallback", poolTypeEn.name());
                return (T) crgRuleTypesProdDao;
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends CrgRuleTablesDao<E>, E extends CrgRuleTables> T getRuleTablesDao(PoolTypeEn poolTypeEn) {
        switch (poolTypeEn) {
            case DEV:
                return (T) crgRuleTablesDevDao;
            case PROD:
                return (T) crgRuleTablesProdDao;
            default:
                LOG.log(Level.WARNING, "unknown pool type: {0} -> use productive rule tables as fallback", poolTypeEn.name());
                return (T) crgRuleTablesProdDao;
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends CrgRule2TableDao<E>, E extends CrgRule2Table> T getRule2TableDao(PoolTypeEn poolTypeEn) {
        switch (poolTypeEn) {
            case DEV:
                return (T) crgRule2TableDevDao;
            case PROD:
                return (T) crgRule2TableProdDao;
            default:
                LOG.log(Level.WARNING, "unknown pool type: {0} -> use productive rule2table as fallback", poolTypeEn.name());
                return (T) crgRule2TableProdDao;
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends CrgRule2RoleDao<E>, E extends CrgRule2Role> T getRule2RoleDao(PoolTypeEn poolTypeEn) {
        switch (poolTypeEn) {
            case DEV:
                return (T) crgRule2RoleDevDao;
            case PROD:
                return (T) crgRule2RoleProdDao;
            default:
                LOG.log(Level.WARNING, "unknown pool type: {0} -> use productive rule2role as fallback", poolTypeEn.name());
                return (T) crgRule2RoleProdDao;
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends CrgRulesDao<E, R>, E extends CrgRules, R extends CrgRule2Role> T getRulesDao(PoolTypeEn poolTypeEn) {
        switch (poolTypeEn) {
            case DEV:
                return (T) crgRulesDevDao;
            case PROD:
                return (T) crgRulesProdDao;
            default:
                LOG.log(Level.WARNING, "unknown pool type: {0} -> use productive rules as fallback", poolTypeEn.name());
                return (T) crgRulesProdDao;
        }
    }

    /**
     * return rule pool of asked type to pool name and year
     *
     * @param poolName pool name
     * @param year year
     * @param poolTypeEn pool type
     * @return Pool instance for name, year and pooltype
     */
    public CrgRulePools getRulePool(String poolName, int year, PoolTypeEn poolTypeEn) {
        CrgRulePoolsDao<? extends CrgRulePools> dao = getPoolDao(poolTypeEn);
        if (dao != null) {
            return dao.getRulePool(poolName, year);
        }
        return null;
    }

    /**
     * saves pool object in db, its type will be determined with
     *
     * @param pool pool instance to save
     */
    public void savePool(CrgRulePools pool) {

        savePool(pool, checkEntityType(pool));
    }

    /**
     * saves pool for defined poolType
     *
     * @param pool pool object
     * @param poolTypeEn pool type
     */
    public void savePool(CrgRulePools pool, PoolTypeEn poolTypeEn) {
        CrgRulePoolsDao<CrgRulePools> dao = getPoolDao(poolTypeEn);
        setCreationParams(pool, poolTypeEn);
        dao.persist(pool);
        dao.flush();
    }

    public Long saveNewPool(CrgRulePools pool, PoolTypeEn poolTypeEn) throws RuleEditorProcessException {
        return saveNewPool(pool, poolTypeEn, null);
    }

    public Long saveNewPool(CrgRulePools pool, PoolTypeEn poolTypeEn, Long pCurrentUserId) throws RuleEditorProcessException {
        CrgRulePoolsDao<CrgRulePools> dao = getPoolDao(poolTypeEn);
        setCreationParams(pool, poolTypeEn);
        // check, whether there are a pool with this name
        CrgRulePools pl = dao.findPoolWithNameAndDates(pool, pCurrentUserId);
        if (pl != null) {
            throw RuleEditorProcessException.createRuleEditorProcessException(pool, Level.INFO, "Der Pool " + pool.getCrgplIdentifier()
                    + " für das Jahr " + pool.getCrgplPoolYear() + " ist schon vorhanden");
        }
        dao.persist(pool);
        dao.flush();
        return pool.getId();
    }

    public Long findPoolId4poolNameAndDates(CrgRulePools pool, PoolTypeEn poolTypeEn, Long pCurrentUserId) {
        CrgRulePoolsDao<CrgRulePools> dao = getPoolDao(poolTypeEn);
        setCreationParams(pool, poolTypeEn);
        // check, whether there are a pool with this name
        CrgRulePools pl = dao.findPoolWithNameAndDates(pool, pCurrentUserId);
        if (pl != null) {
            return pl.getId();
        } else {
            return 0L;
        }
    }

    public Map<String, CrgRuleTypes> getRuleTypes() {
        return getRuleTypes(PoolTypeEn.PROD);
    }

    public Map<String, CrgRuleTypes> getRuleTypes(PoolTypeEn poolTypeEn) {
        CrgRuleTypesDao<CrgRuleTypes> dao = getRuleTypesDao(poolTypeEn);
        if (dao != null) {
            return dao.getRuleTypes();
        }
        return new HashMap<>();
    }

    /**
     * @param pType type to determine if prod or dev
     * @return list of all rule types for this type
     */
    public List<CrgRuleTypes> getRuleTypesAsList(PoolTypeEn pType) {
        CrgRuleTypesDao<CrgRuleTypes> dao = getRuleTypesDao(pType);
        if (dao != null) {
            return dao.getRuleTypesAsList();
        }
        return new ArrayList<>();
    }

    /**
     * saves rule type in prod pool
     *
     * @param rType CrgRuletype object
     */
    public void saveRuleType(CrgRuleTypes rType) {
        saveRuleType(rType, checkEntityType(rType));
    }

    /**
     * saves rule type in the table which is defined with poolTypeEn
     *
     * @param rType CrgRuletypeObject
     * @param poolTypeEn DEV/PROD
     */
    public void saveRuleType(CrgRuleTypes rType, PoolTypeEn poolTypeEn) {
        CrgRuleTypesDao<CrgRuleTypes> dao = getRuleTypesDao(poolTypeEn);
        dao.persist(rType);
        rType.setCrgtIdent((int) rType.getId());
        setCreationParams(rType, poolTypeEn);
        dao.flush();
    }

    /**
     * saves one CrgRuleTable Object in PROD pool
     *
     * @param rTable CrgRuleTable Object
     */
    public void saveRuleTable(CrgRuleTables rTable) {
        saveRuleTable(rTable, checkEntityType(rTable));
    }

    /**
     * saves CrgRuleTables object in the table of defined type
     *
     * @param rTable table to save
     * @param poolTypeEn DEV/PROD
     */
    public void saveRuleTable(CrgRuleTables rTable, PoolTypeEn poolTypeEn) {
        CrgRuleTablesDao<CrgRuleTables> dao = getRuleTablesDao(poolTypeEn);
        setCreationParams(rTable, poolTypeEn);
        dao.persist(rTable);
        dao.flush();
    }

    private static PoolTypeEn checkEntityType(AbstractEntity e) {
        if (e.getClass().getSimpleName().endsWith("Dev")) {
            return PoolTypeEn.DEV;
        }
        return PoolTypeEn.PROD;
    }

    /**
     * removes CrgRule2Table object from PROD table
     *
     * @param r2t CrgRule2Table Object to be deleted
     */
    public void deleteRule2Tables(CrgRule2Table r2t) {
        deleteRule2Tables(r2t, PoolTypeEn.PROD);
    }

    /**
     * removes CrgRule2Table object from the defined table
     *
     * @param r2t CrgRule2Table object
     * @param poolTypeEn DEV/PROD
     */
    public void deleteRule2Tables(CrgRule2Table r2t, PoolTypeEn poolTypeEn) {
        CrgRule2TableDao<CrgRule2Table> dao = getRule2TableDao(poolTypeEn);
        dao.deleteById(r2t.getId());
    }

    public void deleteRule2Role(CrgRule2Role r2r) {

        deleteRule2Role(r2r, PoolTypeEn.PROD);
    }

    public void deleteRule2Role(CrgRule2Role r2r, PoolTypeEn poolTypeEn) {
        CrgRule2RoleDao<CrgRule2Role> dao = getRule2RoleDao(poolTypeEn);
        dao.deleteById(r2r.getId());
    }

    public void saveRule(CrgRules crgRule) {
        saveRule(crgRule, PoolTypeEn.PROD);
    }

    public void saveRule(CrgRules crgRule, PoolTypeEn poolTypeEn) {
        CrgRulesDao<CrgRules, CrgRule2Role> dao = getRulesDao(poolTypeEn);
        setCreationParams(crgRule, poolTypeEn);
        dao.persist(crgRule);
        dao.flush();
    }

    public List<CrgRules> getRules4RidList(List<String> pRulesRidList, CrgRulePools pool) {
        CrgRulesDao<CrgRules, CrgRule2Role> dao = getRulesDao(checkEntityType(pool));
        return dao.getRules4RidList(pRulesRidList);
    }

    public List<CrgRules> getAllRules() {
        return getAllRules(PoolTypeEn.PROD);
    }

    public List<CrgRules> getAllRules(PoolTypeEn poolTypeEn) {
        CrgRulesDao<CrgRules, CrgRule2Role> dao = getRulesDao(poolTypeEn);
        return dao.getAllRules();
    }

    public CrgRules findRuleById(long pRId) {
        return findRuleById(pRId, PoolTypeEn.PROD);
    }

    public CrgRules findRuleById(long pRId, PoolTypeEn poolTypeEn) {
        CrgRulesDao<CrgRules, CrgRule2Role> dao = getRulesDao(poolTypeEn);
        CrgRules rule = dao.findById(pRId);
        if (rule != null) {
            Hibernate.initialize(rule.getCrgRuleTypes());
            Hibernate.initialize(rule.getCrgRulePools());
            Hibernate.initialize(rule.getCrgRule2Roles());
            if (rule.getCrgRule2Roles() != null) {
                for (CrgRule2Role r2r : rule.getCrgRule2Roles()) {
                    Hibernate.initialize(r2r.getCdbUserRoles());
                }
            }
        }
        return rule;
    }

    /**
     * returns the list of pool objects of asked type
     *
     * @param pType pool type(dev/prod)
     * @return list of pool objects of asked type
     */
    public List<CrgRulePools> getCrgRulePools(PoolTypeEn pType) {
//        try {
        CrgRulePoolsDao<CrgRulePools> dao = getPoolDao(pType);
        return dao.findAll();
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, " Error on getting rule pools " + pType.getDescription(), ex);
//            return new ArrayList<>();
//        }
    }

    /**
     * returns the list of pool objects of asked type
     *
     * @param pType pool type(dev/prod)
     * @return list of pool objects of asked type
     */
    public List<CrgRulePools> getCrgRulePoolsYearsDesc(PoolTypeEn pType) {
        return getCrgRulePoolsYearsDesc(pType, null);
    }

    /**
     * returns the list of pool objects of asked type for creation user sorted
     * with year desc
     *
     * @param pType pool type(dev/prod)
     * @param pCurrentUserId current user, if null - for all
     * @return list of pool objects of asked type
     */
    public List<CrgRulePools> getCrgRulePoolsYearsDesc(PoolTypeEn pType, Long pCurrentUserId) {
//        try {
        CrgRulePoolsDao<CrgRulePools> dao = getPoolDao(pType);
        return dao.findAllYearsDesc(pCurrentUserId);
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, " Error on getting rule pools " + pType.getDescription(), ex);
//            return new ArrayList<>();
//        }
    }

    public List<CrgRules> findRulesForPool(long pPoolId, PoolTypeEn pType) {
//        try {
        CrgRulesDao<CrgRules, CrgRule2Role> dao = getRulesDao(pType);
        List<CrgRules> retList = dao.getRules4PoolId(pPoolId);
        if (retList == null) {
            return new ArrayList<>();
        } else {
            for (CrgRules rule : retList) {
                Hibernate.initialize(rule.getCrgRuleTypes());
            }
            return retList;
        }
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, " Error on getting rule for pool " + pPoolId + " " + pType.getDescription(), ex);
//            return new ArrayList<>();
//
//        }
    }

    public CrgRules findRuleByIdWithoutDefinition(long pPoolId, long pRuleId, PoolTypeEn pType) {
        CrgRulesDao<CrgRules, CrgRule2Role> dao = getRulesDao(pType);

        return dao.getRuleByIdWithoutDefinition(pRuleId, pPoolId);
    }

    /**
     * returns pool, defined with its type and id
     *
     * @param pPoolId pool id
     * @param pType for this type
     * @return pool, defined with its type and id
     */
    public CrgRulePools getRulePool2Id(long pPoolId, PoolTypeEn pType) {
//        try {
        CrgRulePoolsDao<CrgRulePools> dao = getPoolDao(pType);
        return dao.findById(pPoolId);
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, " Error on getting rule pools " + pType.getDescription(), ex);
//            return null;
//        }
    }

    /**
     * returns the number of rules in the pool, defined with its type and id
     *
     * @param pPoolId pool id
     * @param pType for this type
     * @return the number of rules in the pool, defined with its type and id
     */
    public Number countRules4PoolId(long pPoolId, PoolTypeEn pType) {
//        try {
        CrgRulesDao<CrgRules, CrgRule2Role> dao = getRulesDao(pType);
        return dao.countRules4PoolId(pPoolId);
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, " Error on getting rule count for  pool " + pType.getDescription(), ex);
//            return null;
//
//        }

    }

    public Number countRuleTables4PoolId(long pPoolId, PoolTypeEn pType) {
        CrgRuleTablesDao<CrgRuleTables> dao = getRuleTablesDao(pType);
        return dao.countRuleTables4PoolId(pPoolId);
    }

    /**
     * returns the CrgRuleTables object to its name and pool, defined with its
     * type and id
     *
     * @param pIdString Id der Tabelle asString
     * @param pType pool type
     * @param pPoolId pool id
     * @return the CrgRuleTables object to its name and pool, defined with its
     * type and id
     */
    public CrgRuleTables findRuleTable4StringId(String pIdString, PoolTypeEn pType, long pPoolId) {
//        try{
        CrgRuleTablesDao<CrgRuleTables> dao = getRuleTablesDao(pType);
        long pId = Long.parseLong(pIdString);
        return dao.findById(pId);
//        }catch(NumberFormatException ex){
//            LOG.log(Level.SEVERE, " Error on getting rule table " + pIdString + " from pool " + pType.getDescription(), ex);
//            return null;
//            
//        }
    }

    public List<CrgRuleTables> findRuleTables4IdAsStringList(long pPoolId, List<String >pIdList, PoolTypeEn pType){

        List<Long> ids = new ArrayList<>();
        pIdList.stream().filter((part) -> (part != null &&! part.trim().isEmpty())).forEachOrdered((part) -> {
            ids.add(Long.parseLong(part));
        });
        if(!ids.isEmpty()){
            return findTables4PoolAndIdList(pPoolId, pType, ids);
        }
        return new ArrayList<>();
    }
            
    public byte[] findRuleDefinition(long pRuleId, long pPoolId, PoolTypeEn pType) {
//        try {
        CrgRulesDao<CrgRules, CrgRule2Role> dao = getRulesDao(pType);
        byte[] ret = dao.getRuleDefinition(pRuleId, pPoolId);
        long start = System.currentTimeMillis();
        CRGRule rule = RulesConverter.getCpRuleFromRuleDefinition(ret, null);
        if(rule != null){
            ret = RulesConverter.getRuleContent(rule, true, null, null);
        }
        LOG.log(Level.INFO, "rule convert time: " + String.valueOf(System.currentTimeMillis() - start));
        return ret;
    }

    /**
     * returns the list of tables of the defined type from the pool, defined
     * with its id
     *
     * @param pType type of the pool
     * @param pPoolId pool id to fetch tables for
     * @return list of all rule tables for that pool
     *
     */
    public List<CrgRuleTables> getRuleTables4Pool(PoolTypeEn pType, long pPoolId) {
//        try{
        CrgRuleTablesDao<CrgRuleTables> dao = getRuleTablesDao(pType);
        return dao.getRuleTable4Pool(pPoolId);
//        }catch(Exception ex){
//            LOG.log(Level.SEVERE, " Error on getting rule tables from pool " + pType.getDescription(), ex);
//            return null;
//            
//        }
    }
    
    public  List<CrgRuleTables>  getRuleTables4PoolWithContent(PoolTypeEn pType, long pPoolId) {

        CrgRuleTablesDao<CrgRuleTables> dao = getRuleTablesDao(pType);
        return dao.getRuleTables4PoolWithContent(pPoolId); 
    }

    public List<CrgRuleTables> getRuleTables4YearAndContentOrName(PoolTypeEn pType, long pPoolId, String pText) {
        String text = Objects.requireNonNullElse(pText, "");
        CrgRuleTablesDao<CrgRuleTables> dao = getRuleTablesDao(pType);
        return dao.getRuleTable4PoolAndNameOrContent(pPoolId, text);
    }

    /**
     * returns the list of all rule tybles of the defined type
     *
     * @param pType pooltype
     * @return list of all tables for the rule type
     */
    public List<CrgRuleTables> getAllRuleTables4PoolType(PoolTypeEn pType) {
//        try{
        CrgRuleTablesDao<CrgRuleTables> dao = getRuleTablesDao(pType);
        return dao.findAll();
//        }catch(Exception ex){
//            LOG.log(Level.SEVERE, " Error on getting rule tables from pool " + pType.getDescription(), ex);
//            return null;
//            
//        }
    }

    /**
     * creates new rule table in the same pool and copies contents of all fields
     *
     * @param table rule table
     * @return the copy of the table
     */
    public CrgRuleTables copyRuleTable(CrgRuleTables table) {
//     try{   
        PoolTypeEn pType = checkEntityType(table);
        CrgRuleTablesDao<CrgRuleTables> dao = getRuleTablesDao(pType);
        table = dao.findById(table.getId());
        CrgRulePoolsDao<CrgRulePools> poolDao = getPoolDao(pType);
        CrgRulePools pl = poolDao.findById(table.getCrgRulePools().getId());
        List<String> names = dao.getLikeTableNames(table.getCrgtTableName(), pl.getId());
        String suffix = getLastSuffix(names);

        CrgRuleTables newTable = CrgRuleTables.getTypeInstance(pType);
        poolDao.merge(pl);
        newTable.setCrgtTableName(table.getCrgtTableName() + suffix);
//        long plId = pl.getId();
        newTable.setCrgRulePools(pl);
        pl.getCrgRuleTableses().add(newTable);
        //set creation values, no auto setting of these fields implemented!
        setCreationParams(newTable, pType, ClientManager.getCurrentCpxUserId());

        newTable.setCrgtContent(table.getCrgtContent());
        newTable.setCrgtComment(table.getCrgtComment());
        poolDao.flush();
        return newTable;
//     }catch(Exception ex){
//             LOG.log(Level.SEVERE, " Error on creating a copy of the existing table  " + table.getTableName(), ex);
//            return null;        
//     }    

    }

    /**
     * creates new rule in the same pool and copies contents of all fields
     *
     * @param pRuleId ruleId
     * @param pPoolId pool id
     * @param pPoolType pool type
     * @return new rule
     * @throws de.lb.cpx.server.rule.services.RuleEditorProcessException
     * exception
     */
    public CrgRules copyRule(long pRuleId, long pPoolId, PoolTypeEn pPoolType) throws RuleEditorProcessException {
        try {
            CrgRulesDao<CrgRules, CrgRule2Role> ruleDao = getRulesDao(pPoolType);
            CrgRules srcRule = ruleDao.findById(pRuleId);
            if (srcRule == null) {
                throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE, "Die Regel zu id " + pRuleId + " ist nicht vorhanden");
            }
            CrgRulePoolsDao<CrgRulePools> poolDao = getPoolDao(pPoolType);
            CrgRulePools pl = poolDao.findById(pPoolId);
            if (pl == null) {
                throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE, "Der Pool für id " + pPoolId + " ist nicht vorhanden");
            }

            CrgRules destRule = CrgRules.getTypeInstance(pPoolType);
            destRule = RulesConverter.copyCrgRuleFileds(srcRule, destRule);
            destRule = RulesConverter.copyR2rAndr2tRelations(srcRule, destRule, pPoolType, ClientManager.getCurrentCpxUserId());
            destRule.setCrgrIdentifier("temp");
// vorbereitung der  regel zum speichern   
            poolDao.merge(pl);
            setCreationParams(destRule, pPoolType);
            destRule.setCrgRulePools(pl);

            pl.getCrgRuleses().add(destRule);
            poolDao.flush();

            ruleDao.merge(destRule);
            long id = destRule.getId();
            destRule.generateCrgrIdentifier();
            destRule.setCrgrRid(String.valueOf(id));
            CRGRule cpRule = RulesConverter.getCpRule(destRule);
            destRule.setCrgrDefinition(RulesConverter.getRuleContent(cpRule, true, destRule.getCrgRuleTypes(), null));

            ruleDao.flush();
            return destRule;

        } catch (RuleEditorProcessException ex) {
            LOG.log(Level.SEVERE, "Es ist ein Fehler bei kopieren der Regel zu Id = " + pRuleId + " aufgetretten", ex);
            throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE,
                    "Es ist ein Fehler bei kopieren der Regel zu Id = " + pRuleId + " aufgetretten" + ex.getMessage());
        }
    }

    private String getLastSuffix(List<String> names) {
        if (names == null || names.isEmpty()) {
            return "(1)";
        }
        int last = 0;
        for (String name : names) {
            String suffix = "";
            int pos = name.trim().indexOf('(');
            if (pos > 0) {
                suffix = name.substring(pos + 1);
                int endpos = suffix.indexOf(')');
                if (endpos > 0) {
                    suffix = suffix.substring(0, endpos);
                    try {
                        int num = Integer.parseInt(suffix);
                        if (num > last) {
                            last = num;
                        }
                    } catch (NumberFormatException ex) {
                        LOG.log(Level.WARNING, suffix + " is not a number ", ex);
                    }
                }
            }
        }

        return "(" + (last + 1) + ")";
    }

    public List<CdbUserRoles> findRoles2Rule(long pRuleId, PoolTypeEn pPoolType) {
//        try{
        CrgRule2RoleDao<CrgRule2Role> dao = getRule2RoleDao(pPoolType);
        return dao.findUserRoles2RuleId(pRuleId);
//        }catch(Exception ex){
//             LOG.log(Level.SEVERE, " Error on getting user roles for rule with id  " + String.valueOf(pRuleId) + " for " +  pPoolType.getDescription(), ex);
//            return new ArrayList<>();        
//        }
    }

    /**
     * returns the content of Table, defined with ist Id
     *
     * @param pTableId table id
     * @param pPoolType pool type
     * @return the content of Table, defined with ist Id
     */
    public String getContent2RuleTable(long pTableId, PoolTypeEn pPoolType) {
//        try{
        CrgRuleTablesDao<CrgRuleTables> dao = getRuleTablesDao(pPoolType);
        CrgRuleTables table = dao.findById(pTableId);
        if (table != null) {
            return table.getCrgtContent();
        }
        return "";
//        }catch(Exception ex){
//             LOG.log(Level.SEVERE, " Error on getting of content of a rule table with id  " + String.valueOf(pTableId) + " for " +  pPoolType.getDescription(), ex);
//            return "";        
//        }
    }

    /**
     * updates the table
     *
     * @param pPoolId pool id
     * @param pPoolType pool type
     * @param pTable Table
     */
    public void updateRuleTable(long pPoolId, PoolTypeEn pPoolType, CrgRuleTables pTable) {
//        try{
        CrgRulePoolsDao<CrgRulePools> poolDao = getPoolDao(pPoolType);
        CrgRulePools pl = poolDao.findById(pPoolId);
        setModificationParams(pTable, pPoolType);
        pTable.setCrgRulePools(pl);
        CrgRuleTablesDao<CrgRuleTables> rtabDao = getRuleTablesDao(pPoolType);
        rtabDao.merge(pTable);
//        }catch(Exception ex){
//             LOG.log(Level.SEVERE, " Error on saving of a rule table with   " +pTable.getCrgtTableName() + " for " +  pPoolType.getDescription(), ex);
//            
//        }
    }

    public void updateRuleType(CrgRuleTypes pType, PoolTypeEn pPoolType) throws RuleEditorProcessException {
//        try{
        CrgRuleTypesDao<CrgRuleTypes> typeDao = getRuleTypesDao(pPoolType);
        CrgRuleTypes oldType = typeDao.findById(pType.getId());
        if (oldType == null) {
            throw RuleEditorProcessException.createRuleEditorProcessException(pType, Level.SEVERE, "Der Regeltyp " + pType.getCrgtShortText() + " ist nicht vorhanden");
        }
        setModificationParams(pType, pPoolType);

        typeDao.merge(pType);
//        }catch(Exception ex){
//             LOG.log(Level.SEVERE, " Error on saving of a rule table with   " +pTable.getCrgtTableName() + " for " +  pPoolType.getDescription(), ex);
//            
//        }
    }

    public List<CrgRules> findRulesForTable(long pPoolId, PoolTypeEn pPoolType, long pTableId) {
//       try{
        CrgRulesDao<CrgRules, CrgRule2Role> dao = getRulesDao(pPoolType);
        return dao.getRules4PoolAndTableId(pPoolId, pTableId);
//        }catch(Exception ex){
//             LOG.log(Level.SEVERE, " Error on getting of list of rules for table with id  " + String.valueOf(pTableId) + " for " +  pPoolType.getDescription(), ex);
//            return new ArrayList<>();        
//        } 
    }

    public List<CrgRules> findFullRulesForTable(long pPoolId, PoolTypeEn pPoolType, long pTableId) {
//       try{
        CrgRulesDao<CrgRules, CrgRule2Role> dao = getRulesDao(pPoolType);
        return dao.getFullRules4PoolAndTableId(pPoolId, pTableId);
//        }catch(Exception ex){
//             LOG.log(Level.SEVERE, " Error on getting of list of rules for table with id  " + String.valueOf(pTableId) + " for " +  pPoolType.getDescription(), ex);
//            return new ArrayList<>();        
//        } 
    }

    public CrgRules createRule(long pPoolId, PoolTypeEn pPoolType) {
        //long start = System.currentTimeMillis();
        CrgRules rule = CrgRules.getTypeInstance(pPoolType);
//        LOG.info("get rule from TypeInstance " +(System.currentTimeMillis()-start));
        CrgRules rule2 = saveNewRule(rule, pPoolId, pPoolType);
//        LOG.info("saveNewRule " +(System.currentTimeMillis()-start));
        return rule2;
    }

    /**
     * saves new Rule in pool
     *
     * @param rule new Rule
     * @param pPoolId pool id
     * @param pPoolType pool type
     * @return rid of saved pool
     */
    private CrgRules saveNewRule(CrgRules rule, long pPoolId, PoolTypeEn pPoolType) {
        //long start = System.currentTimeMillis();
        try {
            CrgRulePoolsDao<CrgRulePools> poolDao = getPoolDao(pPoolType);
            CrgRulePools pl = poolDao.findById(pPoolId);
//            LOG.info("find Pool " + (System.currentTimeMillis()-start));
            if (pl == null) {
                throw RuleEditorProcessException.createRuleEditorProcessException(rule, Level.SEVERE, "Der Pool für id " + pPoolId + " ist nicht vorhanden");
            }
            CrgRuleTypes type = getTmpRuleType(pPoolType);
//            if(type == null){
//                 throw RuleEditorProcessException.createRuleEditorProcessException(rule, Level.SEVERE, "Der temporäre Regeltype konnte nicht gefunden/angelegt werden, Pool für id " + String.valueOf(pPoolId) + " ist nicht vorhanden");
//               
//            }
// vorbereitung der leeren regel zum speichern   
            if (rule.getCrgrNumber() == null || rule.getCrgrNumber().isEmpty()) {
                rule.setCrgrNumber("(new*)");
            }
//            poolDao.merge(pl);     
//            LOG.info("after first merge Pool in " + (System.currentTimeMillis()-start));
            setCreationParams(rule, pPoolType);
//            LOG.info("create default params " + (System.currentTimeMillis()-start));
            rule.setCrgrIdentifier("temp");
            rule.setCrgrValidFrom(pl.getCrgplFrom());
            rule.setCrgrValidTo(pl.getCrgplTo());
            rule.setCrgRuleTypes(type);
            rule.setCrgrRid("0");
//            LOG.info("update new rule and pool " + (System.currentTimeMillis()-start));
//            type.getCrgRuleses().add(rule);
//            LOG.info("add to type " + (System.currentTimeMillis() - start));
            rule.setCrgRulePools(pl);
//            LOG.info("setRule " + (System.currentTimeMillis()-start));
//            pl.getCrgRuleses().add(rule);
//            LOG.info("add to Pool " + (System.currentTimeMillis()-start));
//            LOG.info("update new rule and pool " + (System.currentTimeMillis()-start));
            poolDao.flush();
//            LOG.info("update Pool in " + (System.currentTimeMillis()-start));
            CrgRulesDao<CrgRules, CrgRule2Role> ruleDao = getRulesDao(pPoolType);
            ruleDao.saveOrUpdate(rule);
            long id = rule.getId();
            rule.generateCrgrIdentifier();
            rule.setCrgrRid(String.valueOf(id));
            if (rule.getCrgrRuleErrorType() == null) {
                rule.setCrgrRuleErrorType(RuleTypeEn.STATE_NO);
            }
//            LOG.info("update new Rule values in " + (System.currentTimeMillis()-start));
            // fill rules definition
            CRGRule cpRule = RulesConverter.getCpRule(rule);
            cpRule.m_rootElement = new DatRuleElement(null);
            cpRule.m_rid = String.valueOf(id);
            rule.setCrgrDefinition(RulesConverter.getRuleContent(cpRule, true, type, new HashMap<>()));

//            LOG.info("create new RuleDefinition " + (System.currentTimeMillis()-start));
            ruleDao.flush();
//            LOG.info("create new Rule in " + (System.currentTimeMillis()-start));
            return rule;
        } catch (RuleEditorProcessException ex) {
            LOG.log(Level.SEVERE, " Error on saving of a new rule for " + pPoolType.getDescription(), ex);

        }
        return null;
    }

    private void setCreationParams(AbstractEntity entity, PoolTypeEn poolType) {
        setCreationParams(entity, poolType, ClientManager.getCurrentCpxUserId());
    }

    public void setCreationParams(AbstractEntity entity, PoolTypeEn poolType, Long userId) {
        if (poolType.equals(PoolTypeEn.DEV)) {
            entity.setCreationDate(new Date());
            if (entity.getCreationUser() == null) {
                entity.setCreationUser(userId);
            }
        }
    }

    private void setModificationParams(AbstractEntity entity, PoolTypeEn poolType) {
        setModificationParams(entity, poolType, ClientManager.getCurrentCpxUserId());
    }

    private void setModificationParams(AbstractEntity entity, PoolTypeEn poolType, Long userId) {
        if (poolType.equals(PoolTypeEn.DEV)) {
            entity.setModificationDate(new Date());
            entity.setModificationUser(userId);

        }
    }

    /**
     * looks for temporary rule type in db and creates one, if not found any
     *
     * @param pPoolType pool type
     * @return rule type
     */
    public CrgRuleTypes getTmpRuleType(PoolTypeEn pPoolType) {
//        try{
        CrgRuleTypesDao<CrgRuleTypes> typeDao = this.getRuleTypesDao(pPoolType);
        CrgRuleTypes ruleType = typeDao.findRuleTypeWithShortText("(newType*)");
        if (ruleType == null) {
            ruleType = createAndPersistRuleType(pPoolType, "(newType*)", "temporäre Regeltyp");
//            ruleType = CrgRuleTypes.getTypeInstance(pPoolType);
//            ruleType.setCrgtShortText("(newType*)");
//            ruleType.setCrgtDisplayText("temporäre Regeltyp");
//            ruleType.setCrgtReadonly(false);
//            setCreationParams(ruleType, pPoolType);
//            typeDao.persist(ruleType);

        }
        return ruleType;
//        }catch(Exception ex){
//            LOG.log(Level.SEVERE, " Error on find/create a temporary rule type ", ex);       
//            return null;
//        }
//        
    }

    public CrgRuleTypes createRuleType(PoolTypeEn pPoolType, String pShortText, String pDescription) {
        if (pPoolType == null) {
            return null;
        }
        CrgRuleTypes ruleType = CrgRuleTypes.getTypeInstance(pPoolType);
        ruleType.setCrgtShortText(pShortText);
        ruleType.setCrgtDisplayText(pDescription);
        ruleType.setCrgtReadonly(false);
        setCreationParams(ruleType, pPoolType);
        return ruleType;
    }

    public CrgRuleTypes createAndPersistRuleType(PoolTypeEn pPoolType, String pShortText, String pDescription) {
        CrgRuleTypes ruleType = createRuleType(pPoolType, pShortText, pDescription);
        if (ruleType == null) {
            return null;
        }
        CrgRuleTypesDao<CrgRuleTypes> typeDao = this.getRuleTypesDao(pPoolType);
        typeDao.persist(ruleType);
        return ruleType;
    }

    /**
     * delete CrgRuleTable
     *
     * @param pPoolId poold id, could maybe be removed
     * @param pPoolType pooltype to determine if prod or dev
     * @param pToDelete crgRuleTable to delete
     * @throws de.lb.cpx.server.rule.services.RuleEditorProcessException error
     */
    public void deleteRuleTable(long pPoolId, PoolTypeEn pPoolType, CrgRuleTables pToDelete) throws RuleEditorProcessException {
        if (hasTableRuleRelation(pPoolId, pPoolType, pToDelete)) {
            throw RuleEditorProcessException.createRuleEditorProcessException(pToDelete, Level.SEVERE, "Regeltabelle kann nicht gelöscht werden, da noch Regeln auf sie verweisen!");
        }
        CrgRuleTablesDao<CrgRuleTables> dao = getRuleTablesDao(pPoolType);
//        CrgRulePoolsDao poolDao = getPoolDao(pPoolType);
//        CrgRulePools pool = (CrgRulePools)poolDao.findById(pPoolId);
//        if(pool != null){
//            pool.getCrgRuleTableses().remove(pToDelete);
//        }
        pToDelete.setCrgRulePools(null);
        dao.deleteById(pToDelete.getId());
    }

    /**
     * deletes a rule with pRuleId from pool with pPoolId and type pPoolType
     *
     * @param pRuleId rule id
     * @param pPoolId pool id
     * @param pPoolType pooltype
     * @throws RuleEditorProcessException error
     */
    public void deleteRule(long pRuleId, long pPoolId, PoolTypeEn pPoolType) throws RuleEditorProcessException {
        CrgRulesDao<CrgRules, CrgRule2Role> ruleDao = this.getRulesDao(pPoolType);
//        CrgRules rule = (CrgRules)ruleDao.findById(pRuleId);
        if (ruleDao.exists(pPoolId)) {
            throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE, "Es konnte keine Regel zu Id = " + pRuleId + " gefunden werden");
        }
//        try{
// einträge für Rule2Table löschen
        CrgRule2TableDao<CrgRule2Table> r2tDao = this.getRule2TableDao(pPoolType);
        r2tDao.remove4ruleId(pRuleId);
//        }catch(Exception ex){
//            LOG.log(Level.SEVERE,  "Es ist ein Fehler bei löschen der Einträgen aus RULE_2_TABLE zu Regel zu Id = " + pRuleId + " aufgetretten", ex);
//            throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE, 
//                    "Es ist ein Fehler bei löschen der Einträgen aus RULE_2_TABLE zu Regel zu Id = " + pRuleId + " aufgetretten" + ex.getMessage());
//             
//        }
// einträge für Rule2Role löschen
        CrgRule2RoleDao<CrgRule2Role> r2rDao = getRule2RoleDao(pPoolType);
//        try{
        r2rDao.remove4RuleId(pRuleId);
//        }catch(Exception ex){
//             LOG.log(Level.SEVERE,  "Es ist ein Fehler bei löschen der Einträgen aus RULE_2_ROLE zu Regel zu Id = " + pRuleId + " aufgetretten", ex);
//            throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE, 
//                    "Es ist ein Fehler bei löschen der Einträgen aus RULE_2_ROLE zu Regel zu Id = " + pRuleId + " aufgetretten" + ex.getMessage());
//             
//        }

// regel löschen
        ruleDao.deleteById(pRuleId);
    }

    public void deleteRules(List<Long> pRuleIds, PoolTypeEn pPoolType) {
        CrgRulesDao<CrgRules, CrgRule2Role> ruleDao = this.getRulesDao(pPoolType);
// einträge für Rule2Table löschen
        CrgRule2TableDao<CrgRule2Table> r2tDao = this.getRule2TableDao(pPoolType);
        r2tDao.remove4ruleIds(pRuleIds);
// einträge für Rule2Role löschen
        CrgRule2RoleDao<CrgRule2Role> r2rDao = getRule2RoleDao(pPoolType);

        r2rDao.remove4RuleIds(pRuleIds);

        ruleDao.deleteByIds(pRuleIds);
    }

    /**
     * @param pPoolId pool id of the table
     * @param pPoolType pooltype to determine if prod or dev
     * @param pTable table entity
     * @return indicator if table has relation to rules
     */
    public boolean hasTableRuleRelation(long pPoolId, PoolTypeEn pPoolType, CrgRuleTables pTable) {
        Long size = sizeOfTableRuleRelation(pPoolId, pPoolType, pTable);
        return size != 0L;
    }

    /**
     * @param pPoolId pool id of the table
     * @param pPoolType pooltype to determine dev or prod
     * @param pTable table to check
     * @return number of rules related to the table
     */
    public Long sizeOfTableRuleRelation(long pPoolId, PoolTypeEn pPoolType, CrgRuleTables pTable) {
        CrgRulesDao<CrgRules, CrgRule2Role> dao = getRulesDao(pPoolType);
        return dao.getSizeOfRuleTableRelation(pPoolId, pTable);
    }
    
    /**
     * @param pPoolId pool id of the table
     * @param pPoolType pooltype to determine dev or prod
     * @param pTables table to check
     * @return number of rules related to the table
     */
    public Long sizeOfTableRuleRelation(long pPoolId, PoolTypeEn pPoolType, List<CrgRuleTables> pTables) {
        CrgRulesDao<CrgRules, CrgRule2Role> dao = getRulesDao(pPoolType);
        return dao.getSizeOfRuleTableRelation(pPoolId, pTables);
    }
    
    /**
     * checks whether there are some rules with this type
     *
     * @param pTypeId type id
     * @param pPoolType pool type
     * @return indicator if type has relation to rules
     */
    public boolean hasTypeRuleRelation(long pTypeId, PoolTypeEn pPoolType) {
        Long size = sizeOfTypeRuleRelation(pTypeId, pPoolType);
        return size != 0L;
    }

    /**
     * gets the number of rules to this type
     *
     * @param pTypeId type id
     * @param pPoolType pool type
     * @return number of rules related to the type
     */
    public Long sizeOfTypeRuleRelation(long pTypeId, PoolTypeEn pPoolType) {
        CrgRulesDao<CrgRules, CrgRule2Role> dao = getRulesDao(pPoolType);
        return dao.getSizeOfRuleTypeRelation(pTypeId);
    }

    /**
     * deletes an empty pool
     *
     * @param pPoolId pool id
     * @param pType pool type
     * @throws de.lb.cpx.server.rule.services.RuleEditorProcessException
     * exception
     */
    public void deletePool(long pPoolId, PoolTypeEn pType) throws RuleEditorProcessException {
// check, whether pool is empty
// has tables?
        try {
            List<CrgRuleTables> tables = getRuleTables4Pool(pType, pPoolId);
            if (tables != null && !tables.isEmpty()) {
                LOG.log(Level.SEVERE, "Der Pool mit Id = " + pPoolId + " wird von " + tables.size() + " Tabellen referenziert, kann nicht gelöscht werden");
                throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE, "Der Pool mit Id = " + pPoolId + " wird von " + tables.size() + " referenziert, kann nicht gelöscht werden");
            }
// has rules?
            Number ruleCount = this.countRules4PoolId(pPoolId, pType);
            if (ruleCount != null && ruleCount.intValue() != 0) {
                LOG.log(Level.SEVERE, "Der Pool mit Id = " + pPoolId + " wird von " + ruleCount.intValue() + " Regeln referenziert, kann nicht gelöscht werden");
                throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE, "Der Pool mit Id = " + pPoolId + " wird von " + ruleCount.intValue() + " Regeln referenziert, kann nicht gelöscht werden");
            }
// now delete
            CrgRulePoolsDao<CrgRulePools> poolDao = this.getPoolDao(pType);
            poolDao.deleteById(pPoolId);
        } catch (RuleEditorProcessException ex) {
            LOG.log(Level.SEVERE, "Es ist ein Fehler beim Löschen des Regelpools zu Id = " + pPoolId + " aufgetretten", ex);
            throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE,
                    "Es ist ein Fehler beim Löschen des Regelpools zu Id = " + pPoolId + " aufgetretten" + ex.getMessage());

        }
    }

    /**
     * updates the existing rule and its references in DB
     *
     * @param pRule RuleEntity
     * @param pPoolId Pool ID
     * @param pPoolType Pool Type
     * @throws RuleEditorProcessException exception
     */
    public void updateRule(CrgRules pRule, long pPoolId, PoolTypeEn pPoolType) throws RuleEditorProcessException {
        try {
            Map<String, RuleTableCategoryEn> tableCategories = new HashMap <>();
            checkCreationParameter(pRule, pPoolType);
            CRGRule cpRule = RulesConverter.getCpRuleFromRuleDefinition(pRule, tableCategories);
            cpRule.setErrorMessage(pRule.getCrgrMessage());
            List<Long> tabIds = RulesConverter.getRuleTableIds(cpRule);
            long[] roleIds = cpRule.m_roles;

            Long typeId = RulesConverter.getRuleTypeId(cpRule);
            CrgRulesDao<CrgRules, CrgRule2Role> ruleDao = this.getRulesDao(pPoolType);
// update header fields            
            pRule = RulesConverter.getCpxRule(cpRule, pRule, true, pPoolType, null, null, true);
// set new type
            CrgRuleTypesDao<CrgRuleTypes> ruleTypesDao = this.getRuleTypesDao(pPoolType);
            pRule.setCrgRuleTypes(ruleTypesDao.findById(typeId));

// set new tables
            CrgRuleTablesDao<CrgRuleTables> rtabDao = getRuleTablesDao(pPoolType);
            
            CrgRule2TableDao<CrgRule2Table> r2tDao = getRule2TableDao(pPoolType);
            LOG.log(Level.FINE, "for Rule: " + pRule.getCrgrCaption() + " id = " + pRule.getId());
            r2tDao.remove4ruleId(pRule.getId());
            if (!tabIds.isEmpty()) {
                Set<CrgRule2Table> r2tSet = new HashSet<>();
                for (Long tabId : tabIds) {
                    CrgRule2Table rr2t = CrgRule2Table.getTypeInstance(pPoolType);
                    this.setCreationParams(rr2t, pPoolType);
                    CrgRuleTables tab = rtabDao.findById(tabId);
                    if(tab != null && tab.getCrgtCategory() == null){
                        tab.setCrgtCategory(tableCategories.get(String.valueOf(tabId)));
                    }
                    rr2t.setCrgRuleTables(tab);
                    rr2t.setCrgRules(pRule);
                    r2tSet.add(rr2t);
                }
                pRule.setCrgRule2Tables(r2tSet);
                
            }
// set new roles
            CrgRule2RoleDao<CrgRule2Role> r2rDao = getRule2RoleDao(pPoolType);
            r2rDao.remove4RuleId(pRule.getId());

            if (roleIds == null) {

// TODO: what roles, now we get the roles of the active user
                addUserRoles2Rule(pRule, pPoolType);
            } else {
                List<Long> userRoles = getRoleIds4CurrentUser();
                Set<CrgRule2Role> r2rSet = new HashSet<>();
                for (long roleId : roleIds) {
                    if (userRoles != null && !userRoles.contains(roleId)) {
                        continue;
                    }
                    CrgRule2Role r2r = CrgRule2Role.getTypeInstance(pPoolType);
                    this.setCreationParams(r2r, pPoolType);
                    r2r.setCrgRules(pRule);
                    CdbUserRoles usr = userRolesDao.findById(roleId);
                    if (usr == null) {
                        continue;
                    }
                    r2r.setCdbUserRoles(usr);
                    r2rSet.add(r2r);
//                    pRule.getCrgRule2Roles().add(r2r);
                }
                pRule.setCrgRule2Roles(r2rSet);
                if (pRule.getCrgRule2Roles() == null || pRule.getCrgRule2Roles().isEmpty()) {
                    addUserRoles2Rule(pRule, pPoolType);
                }
            }

            CrgRulePoolsDao<CrgRulePools> poolDao = this.getPoolDao(pPoolType);
            CrgRulePools pool = poolDao.findById(pPoolId);
            pRule.setCrgRulePools(pool);
            pRule.setCrgrMessage(cpRule.getErrorMessage());
            setModificationParams(pRule, pPoolType);
            ruleDao.merge(pRule);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Es ist ein Fehler beim Update der Regel " + pRule.getCrgrIdentifier() + " aufgetreten", ex);
            throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE,
                    "Es ist ein Fehler beim Update der Regel " + pRule.getCrgrIdentifier() + " aufgetreten" + ex.getMessage());

        }
    }

    
    public List<Long> getRoleIds4CurrentUser(){
        List<Long> userRoles = null;
        if (ClientManager.getCurrentCpxUserId() != null) {
            userRoles = user2RoleDao.getUser2RoleIdsByUserId(ClientManager.getCurrentCpxUserId());

        }
        return userRoles;
    }
    
    private void addUserRoles2Rule(CrgRules pRule, PoolTypeEn pPoolType) {

        List<Long> userRoles = getRoleIds4CurrentUser();

        if (userRoles != null) {
            for (long roleId : userRoles) {
                CrgRule2Role r2r = CrgRule2Role.getTypeInstance(pPoolType);
                this.setCreationParams(r2r, pPoolType);
                r2r.setCrgRules(pRule);
                CdbUserRoles cpxRole = userRolesDao.findById(roleId);
                if (cpxRole == null) {
                    continue;
                }
                r2r.setCdbUserRoles(cpxRole);
                pRule.getCrgRule2Roles().add(r2r);
            }
        }

    }

    /**
     * checks whether the rule has all roles from roleIds and when not adds them
     * to this rule
     *
     * @param pRule rule to check
     * @param pPoolType rules pool type
     * @param roleIds role ids
     */
    private void checkRuleWithRoles(CrgRules pRule, PoolTypeEn pPoolType, long[] roleIds) {
        CrgRule2RoleDao<CrgRule2Role> r2rDao = getRule2RoleDao(pPoolType);
        List<Long> usedRolesIds = r2rDao.findUserRolesIds2RuleId(pRule.getId());
        boolean added = false;
        for (long roleId : roleIds) {
            if (!usedRolesIds.contains(roleId)) {
                CdbUserRoles cpxRole = userRolesDao.findById(roleId);
                if (cpxRole == null) {
                    continue;
                }
                CrgRule2Role r2r = CrgRule2Role.getTypeInstance(pPoolType);
                this.setCreationParams(r2r, pPoolType);
                r2r.setCrgRules(pRule);
                r2r.setCdbUserRoles(cpxRole);
                pRule.getCrgRule2Roles().add(r2r);
                added = true;
            }
        }
        if (added) {
            CrgRulesDao<CrgRules, CrgRule2Role> rulesDao = getRulesDao(pPoolType);
            rulesDao.merge(pRule);

        }
    }

    public CrgRuleTables saveNewRuleTable(long pPoolId, PoolTypeEn pPoolType, CrgRuleTables pTable) throws RuleEditorProcessException {
        try {
            CrgRulePoolsDao<CrgRulePools> poolDao = getPoolDao(pPoolType);
            CrgRulePools pl = poolDao.findById(pPoolId);
            if (pl == null) {
                throw RuleEditorProcessException.createRuleEditorProcessException(pTable, Level.SEVERE, "Der Pool für id " + pPoolId + " ist nicht vorhanden");
            }
            poolDao.merge(pl);
            pTable.setCrgRulePools(pl);
            if (pTable.getCrgtContent() == null) {
                pTable.setCrgtContent("");
            }
            this.setCreationParams(pTable, pPoolType);
            pl.getCrgRuleTableses().add(pTable);

            return pTable;
        } catch (RuleEditorProcessException ex) {
            LOG.log(Level.SEVERE, " Error on saving of a new rule for " + pTable.getCrgtTableName(), ex);
            throw RuleEditorProcessException.createRuleEditorProcessException(pTable, Level.SEVERE, " Error on saving of a new rule for " + pTable.getCrgtTableName() + ex.getMessage());

        }

    }

    public CrgRuleTables createRuleTable(long pPoolId, PoolTypeEn pPoolType) throws RuleEditorProcessException {
        CrgRuleTables pTable = CrgRuleTables.getTypeInstance(pPoolType);
        pTable.setCrgtTableName("(newTable)*");
        return saveNewRuleTable(pPoolId, pPoolType, pTable);
    }

    public List<CrgRules> findRulesForPool(long pPoolId, PoolTypeEn pPoolType, List<Long> ruleIds) throws RuleEditorProcessException {
        return findRulesForPool(pPoolId, pPoolType, ruleIds, null);
    }

    public List<CrgRules> findRulesForPool(long pPoolId, PoolTypeEn pPoolType, List<Long> ruleIds, Long userId) throws RuleEditorProcessException {
//        try{
        CrgRulesDao<CrgRules, CrgRule2Role> dao = getRulesDao(pPoolType);
        List<CrgRules> rules = dao.getRulesDefinitions4PoolAndRuleIds(pPoolId, ruleIds, userId);
        if (rules == null || rules.isEmpty()) {
            return new ArrayList<>();
//            throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.INFO, "There are no rules for id list found, poolId:" + pPoolId);
        }
// we need typeIds and table ids to replace them with names for rules export
        for (CrgRules rule : rules) {
            Hibernate.initialize(rule.getCrgRuleTypes());
        }

        return rules;
//        }catch(Exception ex){
//             LOG.log(Level.SEVERE, " Error on getting rules from pool " +  pPoolId, ex);
//            throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE, " Error on getting rules from pool " +  pPoolId + ex.getMessage());
//        }
    }

    public Map<String, CrgRuleTypes> getId2RuleTypeMap(PoolTypeEn pPoolType) {
        CrgRuleTypesDao<CrgRuleTypes> typesDao = getRuleTypesDao(pPoolType);
        return typesDao.getId2RuleType();

    }

    public Map<String, CrgRuleTables> getId2Tables(long pPoolId, PoolTypeEn pPoolType) {
        CrgRuleTablesDao<CrgRuleTables> tablesDao = getRuleTablesDao(pPoolType);
        return tablesDao.getId2Tables(pPoolId);

    }

    public void updatePool(CrgRulePools pPool, PoolTypeEn pPoolType) {
//          try{
        CrgRulePoolsDao<CrgRulePools> poolDao = getPoolDao(pPoolType);
        poolDao.merge(pPool);
//          }catch(Exception ex){
//            LOG.log(Level.SEVERE,  "Es ist ein Fehler beim Update des Pools " + pPool.getCrgplIdentifier()+ " aufgetretten", ex);
//           throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE, 
//                   "Es ist ein Fehler beim Update des Pools " + pPool.getCrgplIdentifier() + " aufgetretten" + ex.getMessage());
//        }
    }

    /**
     * saves new rule types from list, that not found in db
     *
     * @param newRuleTypes types to import
     * @param pPoolType pool type
     * @param result array of collisions to return to client has to be not null
     * @return all types that are saved in db as map name to type
     * @throws RuleEditorProcessException error
     */
    public Map<String, CrgRuleTypes> importRuleTypes(List<CrgRuleTypes> newRuleTypes, PoolTypeEn pPoolType, RuleExchangeResult result) throws RuleEditorProcessException {
        return importRuleTypes(newRuleTypes, pPoolType, result, RuleOverrideFlags.SAVE_OLD, RuleImportCheckFlags.CHECK_4_COLLISIONS);
    }

    public Map<String, CrgRuleTypes> importRuleTypes(List<CrgRuleTypes> newRuleTypes, PoolTypeEn pPoolType, RuleExchangeResult result,
            RuleOverrideFlags doOverride, RuleImportCheckFlags doCheck) throws RuleEditorProcessException {

        try {
            if (result == null) {
                LOG.log(Level.SEVERE, "Es können keine Fehler des Imports ermittelt werden");
                throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE,
                        "Es können keine Fehler des Imports ermittelt werden");
            }
            Map<String, CrgRuleTypes> oldRuleTypes = getRuleTypes(pPoolType);
            CrgRuleTypesDao<CrgRuleTypes> ruleTypesDao = getRuleTypesDao(pPoolType);
            for (CrgRuleTypes type : newRuleTypes) {
                CrgRuleTypes oldType = oldRuleTypes.get(type.getCrgtShortText());
                if (oldType == null) { //                        || doOverride.equals(RuleOverrideFlags.SAVE_NEW) && doCheck.equals(RuleImportCheckFlags.NO_CHECK_4_COLLISIONS)
                    setCreationParams(type, pPoolType);
                    ruleTypesDao.persist(type);
                    oldRuleTypes.put(type.getCrgtShortText(), type);
                    result.getSuccessRuleTypeIds().add(type.getId());

                } else {
                    // TODO: do we have any errors here?                    errors.add(new RuleExchangeError(oldType.getId(), RuleValidationStatusEn.RULE_TYPE_FOUND_IN_DB, pPoolType));
                    //if (oldType != null) {
                    if (doOverride.equals(RuleOverrideFlags.SAVE_NEW) && doCheck.equals(RuleImportCheckFlags.NO_CHECK_4_COLLISIONS)) {
                        oldType.setCrgtDisplayText(type.getCrgtDisplayText());
                        oldType.setCrgtIdent(type.getCrgtIdent());
                        oldType.setCrgtReadonly(type.isCrgtReadonly());
                        ruleTypesDao.merge(oldType);
                    }
                    result.getSuccessRuleTypeIds().add(oldType.getId());
                    //}
                }
            }
            ruleTypesDao.flush();
            return oldRuleTypes;
        } catch (RuleEditorProcessException ex) {
            LOG.log(Level.SEVERE, "Es ist ein Fehler beim Import der Regeltypen  aufgetretten", ex);
            throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE,
                    "Es ist ein Fehler beim Import der Regeltypen  aufgetretten" + ex.getMessage());
        }

    }

    public Map<String, CrgRuleTables> importRuleTables(long pPoolId, List<CrgRuleTables> newRuleTables, PoolTypeEn pPoolType, RuleExchangeResult result) throws RuleEditorProcessException {
        return importRuleTables(pPoolId, newRuleTables, pPoolType, result, RuleOverrideFlags.SAVE_OLD, RuleImportCheckFlags.CHECK_4_COLLISIONS);
    }

    public Map<String, CrgRuleTables> importRuleTables(long pPoolId, List<CrgRuleTables> newRuleTables, PoolTypeEn pPoolType, RuleExchangeResult result,
            RuleOverrideFlags doOverride, RuleImportCheckFlags doCheck) throws RuleEditorProcessException {
        if (result == null) {
            LOG.log(Level.SEVERE, "Es können keine Fehler des Imports ermittelt werden");
            throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE,
                    "Es können keine Fehler des Imports ermittelt werden");
        }
        try {
            CrgRuleTablesDao<CrgRuleTables> tablesDao = getRuleTablesDao(pPoolType);
            Map<String, CrgRuleTables> name2table = tablesDao.getName2Tables(pPoolId);
            for (CrgRuleTables newTable : newRuleTables) {
//                 LOG.info("table2import = " + newTable.getCrgtTableName());
                CrgRuleTables oldTable = name2table.get(newTable.getCrgtTableName());
                if (oldTable == null// || doOverride.equals(RuleOverrideFlags.SAVE_NEW) && doCheck.equals(RuleImportCheckFlags.NO_CHECK_4_COLLISIONS)
                        ) {
                    this.saveNewRuleTable(pPoolId, pPoolType, newTable);
                    tablesDao.flush();
                    name2table.put(newTable.getCrgtTableName(), newTable);
//                     LOG.info("new table id =" + newTable.getId());
                    result.getSuccessRuleTableIds().add(newTable.getId());
                } else {
                    boolean commentEquals = RulesConverter.tableCommentEquals(oldTable.getCrgtComment(), newTable.getCrgtComment());
                    if (doCheck.equals(RuleImportCheckFlags.CHECK_4_COLLISIONS)
                            && (!RulesConverter.tableContentEquals(oldTable.getCrgtContent(), newTable.getCrgtContent())
                            || !commentEquals)) {
                        if (commentEquals) {
                            result.getErrors().add(new RuleExchangeError(oldTable.getId(), RuleValidationStatusEn.SAME_TABLE_OTHER_CONTENT, pPoolType, newTable.getCrgtContent()));
                        } else {
                            result.getErrors().add(new RuleExchangeError(oldTable.getId(), RuleValidationStatusEn.SAME_TABLE_OTHER_COMMENT, pPoolType, newTable.getCrgtComment()));
                            
                        }
                    } else {
                        if (doOverride.equals(RuleOverrideFlags.SAVE_NEW) && doCheck.equals(RuleImportCheckFlags.NO_CHECK_4_COLLISIONS)) {
                            oldTable.setCrgtContent(newTable.getCrgtContent());
                            oldTable.setCrgtComment(newTable.getCrgtComment());
                            oldTable.setCrgtMessage(newTable.getCrgtMessage());
                            tablesDao.merge(oldTable);
                        }
                        result.getSuccessRuleTableIds().add(oldTable.getId());
                    }

                }
            }
            tablesDao.flush();
            return name2table;
        } catch (RuleEditorProcessException ex) {
            LOG.log(Level.SEVERE, "Es ist ein Fehler beim Import der Regeltabellen  aufgetretten", ex);
            throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE,
                    "Es ist ein Fehler beim Import der Regeltabellen  aufgetretten" + ex.getMessage());
        }

    }

    public RuleExchangeResult importRules(long pPoolId, List<CrgRules> newRulesRules, PoolTypeEn pPoolType, RuleExchangeResult result) throws RuleEditorProcessException {
        return importRules(pPoolId, newRulesRules, pPoolType, result, RuleOverrideFlags.SAVE_OLD, RuleImportCheckFlags.CHECK_4_COLLISIONS);
    }

    public RuleExchangeResult importRules(long pPoolId, List<CrgRules> newRulesRules, PoolTypeEn pPoolType, RuleExchangeResult result,
            RuleOverrideFlags doOverride, RuleImportCheckFlags doCheck) throws RuleEditorProcessException {
        return importRules(pPoolId, newRulesRules, pPoolType, result,
                doOverride, doCheck, null);
    }

    public RuleExchangeResult importRules(long pPoolId, List<CrgRules> newRulesRules, PoolTypeEn pPoolType, RuleExchangeResult result,
            RuleOverrideFlags doOverride, RuleImportCheckFlags doCheck, long[] roleIdsList) throws RuleEditorProcessException {
        if (result == null) {
            LOG.log(Level.SEVERE, "Es können keine Fehler des Imports ermittelt werden");
            throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE,
                    "Es können keine Fehler des Imports ermittelt werden");
        }
        try {
            CrgRulePools pool = getRulePool2Id(pPoolId, pPoolType);
            if (pool == null) {
                throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE, "Der Pool für id " + pPoolId + " ist nicht vorhanden");
            }
            // map constantpart of ident to rule
            Map<String, CrgRules> oldRules2Ident = RulesConverter.getCpxRulesFromPoolAsMap(pool.getCrgRuleses());
            for (CrgRules newRule : newRulesRules) {
                if(newRule.getCrgrMessage() != null){
                    LOG.log(Level.FINEST, "rule: " + newRule.getName());
                }
                String newIdent = RulesConverter.getIdentConstantPart(newRule.getCrgrIdentifier());
                CrgRules oldRule = oldRules2Ident.get(newIdent);
                if (oldRule == null || doCheck.equals(RuleImportCheckFlags.NO_CHECK_4_COLLISIONS)) {
                    if (oldRule == null || doOverride.equals(RuleOverrideFlags.SAVE_BOTH)) {
                        CrgRules rule = saveNewRule(CrgRules.getTypeInstance(pPoolType), pPoolId, pPoolType);
                        if (rule != null) {
                            rule.setCrgrDefinition(newRule.getCrgrDefinition());
                            if (doOverride.equals(RuleOverrideFlags.SAVE_BOTH)) {
                                rule.generateCrgrIdentifier();
                            }
                            rule.setCrgrMessage(newRule.getCrgrMessage());
                            updateRule(rule, pPoolId, pPoolType);
                            result.getSuccessRuleIds().add(rule.getId());
                        }
                    } else if (oldRule != null && doOverride.equals(RuleOverrideFlags.SAVE_NEW)) {
                        oldRule.setCrgrDefinition(newRule.getCrgrDefinition());
                        oldRule.setCrgrMessage(newRule.getCrgrMessage());
                        updateRule(oldRule, pPoolId, pPoolType);
                        result.getSuccessRuleIds().add(oldRule.getId());
                    }
                } else {
                    if (oldRule != null && doCheck.equals(RuleImportCheckFlags.CHECK_4_COLLISIONS)) {
                        if (RulesConverter.compareCpxRules(oldRule, newRule, pool.getCrgplIdentifier(), pPoolType, result.getErrors())) {
                            
// rules are same
// check, whether the rule has all roles from roleIdsList - it is Import through web         
                            if (roleIdsList != null) {
// roleList kann be only merged     
                                checkRuleWithRoles(oldRule, pPoolType, roleIdsList);
                            }
                            result.getSuccessRuleIds().add(oldRule.getId());
                        } else {
                            if (doOverride.equals(RuleOverrideFlags.SAVE_NEW)) {
// update with new content                                
                                oldRule.setCrgrDefinition(newRule.getCrgrDefinition());
                                oldRule.setCrgrMessage(newRule.getCrgrMessage());
                                updateRule(oldRule, pPoolId, pPoolType);
                                result.getSuccessRuleIds().add(oldRule.getId());

                            } else if (doOverride.equals(RuleOverrideFlags.SAVE_BOTH)) {
// create copy
                                CrgRules rule = saveNewRule(CrgRules.getTypeInstance(pPoolType), pPoolId, pPoolType);
                                if (rule != null) {
                                    rule.setCrgrDefinition(newRule.getCrgrDefinition());
                                    rule.setCrgrMessage(newRule.getCrgrMessage());
                                    rule.generateCrgrIdentifier();

                                    updateRule(rule, pPoolId, pPoolType);
                                    result.getSuccessRuleIds().add(rule.getId());
                                }

                            }
                        }
                    }
                }
            }

        } catch (RuleEditorProcessException ex) {
            LOG.log(Level.SEVERE, "Es ist ein Fehler beim Import der Regeln  aufgetretten", ex);
            throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE,
                    "Es ist ein Fehler beim Import der Regeln  aufgetretten" + ex.getMessage());
        }
        return result;
    }

    public String getTypeNameForRule(long pRuleId, long pPoolId, PoolTypeEn poolType) {
        CrgRuleTypesDao<CrgRuleTypes> dao = getRuleTypesDao(poolType);
        return dao.getRuleTypeNameForRuleAndPool(pRuleId, pPoolId);
    }

    public String getTypeDisplayNameForRule(long pRuleId, long pPoolId, PoolTypeEn poolType) {
        CrgRuleTypesDao<CrgRuleTypes> dao = getRuleTypesDao(poolType);
        return dao.getRuleTypeDisplayNameForRuleAndPool(pRuleId, pPoolId);
    }

    public List<CrgRuleTables> findTables4PoolAndIdList(long pPoolId, PoolTypeEn pPoolType, List<Long> tableIds) throws RuleEditorProcessException {
        try {
            CrgRuleTablesDao<CrgRuleTables> tablesDao = getRuleTablesDao(pPoolType);
            return tablesDao.findTables4PoolAndIdList(pPoolId, tableIds);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Es ist ein Fehlerermitteln der Regeltabellen zu gegebener Id -Liste  aufgetretten", ex);
            throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE,
                    "Es ist ein Fehlerermitteln der Regeltabellen zu gegebener Id -Liste  aufgetretten " + ex.getMessage());
        }
    }

    public void updateRuleTable(CrgRuleTables pRuleTable, PoolTypeEn pType) {
        CrgRuleTablesDao<CrgRuleTables> dao = getRuleTablesDao(pType);
        dao.saveOrUpdate(pRuleTable);
    }

    public List<CrgRules> findAllRulesForPoolAndRole(long pPoolId, PoolTypeEn pPoolType, long pRoleId, boolean pIncludeRole) {
        CrgRulesDao<CrgRules, CrgRule2Role> dao = getRulesDao(pPoolType);
        List<CrgRules> rules = dao.getAllRulesForPoolAndRole(pPoolId, pRoleId, pIncludeRole);
        for (CrgRules rule : rules) {
            Hibernate.initialize(rule.getCrgRuleTypes());
        }
        return rules;
    }

    public Long getRuleCountForRole(long pPoolId, PoolTypeEn pType, long pRoleId) {
        CrgRulesDao<CrgRules, CrgRule2Role> dao = getRulesDao(pType);
        return dao.countRulesForRole(pPoolId, pRoleId);
    }

    public List<CrgRules> getAllRulesExcluded(PoolTypeEn poolTypeEn) {
        CrgRulesDao<CrgRules, CrgRule2Role> dao = getRulesDao(poolTypeEn);
        List<CrgRules> results = dao.getAllRules_excludeContent();
        for (CrgRules result : results) {
            Hibernate.initialize(result.getCrgRulePools());
            Hibernate.initialize(result.getCrgRuleTypes());
        }
        return results;
    }

    public void deleteRuleType(long pTypeId, PoolTypeEn pPoolType) throws RuleEditorProcessException {
        if (hasTypeRuleRelation(pTypeId, pPoolType)) {
            LOG.log(Level.INFO, "Es sind die regeln zu dem zu löschenden Regeltyp vorhanden, dieser typ kann nicht gelöscht werden");
            throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.INFO,
                    "Es sind die Regeln zu dem zu löschenden Regeltyp vorhanden, dieser Typ kann nicht gelöscht werden");

        }
        CrgRuleTypesDao<CrgRuleTypes> typesDao = getRuleTypesDao(pPoolType);
        typesDao.deleteById(pTypeId);
    }

    public void deleteRuleTypes(List<Long> pTypesIds, PoolTypeEn pPoolType) {

        CrgRuleTypesDao<CrgRuleTypes> typesDao = getRuleTypesDao(pPoolType);
        typesDao.deleteByIds(pTypesIds);
    }

    public List<CrgRulePools> getCrgRulePools(PoolTypeEn pPoolTypeEn, Long pCurrentCpxUserId) {
        if (pCurrentCpxUserId == null || pCurrentCpxUserId <= 0) {
            return new ArrayList<>();
        }
        CrgRulePoolsDao<CrgRulePools> poolDao = this.getPoolDao(pPoolTypeEn);
        return poolDao.findAll4CreationUser(pCurrentCpxUserId);
    }

    public List<CrgRuleTypes> getCrgRuleTypes(PoolTypeEn pPoolTypeEn, Long pCurrentCpxUserId) {
        if (pCurrentCpxUserId == null || pCurrentCpxUserId <= 0) {
            return new ArrayList<>();
        }
        CrgRuleTypesDao<CrgRuleTypes> typesDao = getRuleTypesDao(pPoolTypeEn);
        return typesDao.findRuleTypes4CreationUser(pCurrentCpxUserId);
    }

    public List<CrgRules> getCrgRules(PoolTypeEn pPoolTypeEn, Long pCurrentCpxUserId) {
//       if(pCurrentCpxUserId == null || pCurrentCpxUserId <= 0){
//            return new ArrayList<>();
//        }
        CrgRulesDao<CrgRules, CrgRule2Role> rulesDao = getRulesDao(pPoolTypeEn);
        List<CrgRules> rules = rulesDao.getAllRules_excludeContentAndUser(pCurrentCpxUserId);
        for (CrgRules rule : rules) {
            Hibernate.initialize(rule.getCrgRulePools());
            Hibernate.initialize(rule.getCrgRuleTypes());
        }
        return rules;
    }

    public List<CrgRules> getAllCrgRules4User(PoolTypeEn pPoolTypeEn, Long pCurrentCpxUserId) {
        if (pCurrentCpxUserId == null || pCurrentCpxUserId <= 0) {
            return new ArrayList<>();
        }
        CrgRulesDao<CrgRules, CrgRule2Role> rulesDao = getRulesDao(pPoolTypeEn);
        List<CrgRules> rules = rulesDao.getAllRules4User(pCurrentCpxUserId);
        for (CrgRules rule : rules) {
            Hibernate.initialize(rule.getCrgRulePools());
            Hibernate.initialize(rule.getCrgRuleTypes());
        }
        return rules;
    }

    public List<CrgRules> getAllCrgRules4PoolId(PoolTypeEn pPoolTypeEn, Long pPoolId) {
        if (pPoolId == null || pPoolId <= 0) {
            return new ArrayList<>();
        }
        CrgRulesDao<CrgRules, CrgRule2Role> rulesDao = getRulesDao(pPoolTypeEn);
        List<CrgRules> rules = rulesDao.getAllRules4PoolId(pPoolId);
        for (CrgRules rule : rules) {
            Hibernate.initialize(rule.getCrgRulePools());
            Hibernate.initialize(rule.getCrgRuleTypes());
            Hibernate.initialize(rule.getCrgRule2Roles());
            if (rule.getCrgRule2Roles() != null) {
                for (CrgRule2Role r2r : rule.getCrgRule2Roles()) {
                    Hibernate.initialize(r2r.getCdbUserRoles());
                }
            }
        }
        return rules;
    }

    public List<CrgRules> getAllCrgRules4Grouper(PoolTypeEn pPoolTypeEn, Long pCurrentCpxUserId) {
        if (pCurrentCpxUserId == null || pCurrentCpxUserId <= 0) {
            return new ArrayList<>();
        }
        CrgRulesDao<CrgRules, CrgRule2Role> rulesDao = getRulesDao(pPoolTypeEn);
        List<CrgRules> rules = rulesDao.getAllRules4CreationUser(pCurrentCpxUserId);
        return rules;
    }

    public void updateRole2(long pRoleId, long pPoolId, PoolTypeEn pType, List<Long> pNewRuleIds) throws RuleEditorProcessException {
        long computeChangeSet = System.currentTimeMillis();
        CrgRule2RoleDao<CrgRule2Role> roleDao = getRule2RoleDao(pType);
        CrgRulesDao<CrgRules, CrgRule2Role> ruleDao = getRulesDao(pType);

        List<Long> oldRuleIds = roleDao.findRuleIdsForRole(pRoleId, pPoolId);
        List<Long> toRemoveRole = new ArrayList<>();
        List<Long> toAddRole = new ArrayList<>();

        oldRuleIds.stream().filter((oldRuleId) -> (!pNewRuleIds.contains(oldRuleId))).forEachOrdered((oldRuleId) -> {
            toRemoveRole.add(oldRuleId);
        });
        pNewRuleIds.stream().filter((newRuleId) -> (!oldRuleIds.contains(newRuleId))).forEachOrdered((newRuleId) -> {
            toAddRole.add(newRuleId);
        });
        LOG.info("size of rules to add role " + toAddRole.size());
        LOG.info("size of Role to remove " + toRemoveRole.size());
        LOG.info("compute changeSet in " + (System.currentTimeMillis() - computeChangeSet));

        long overall = System.currentTimeMillis();
        for (Long toRemove : toRemoveRole) {
            long start = System.currentTimeMillis();
            roleDao.remove4RuleIdAndRole(toRemove, pRoleId);
            LOG.info("delete role to rule references " + (System.currentTimeMillis() - start));
            byte[] ruleDefinition = ruleDao.getRuleDefinition(toRemove, pPoolId);
            LOG.info("fetch rule definition " + (System.currentTimeMillis() - start));
            Rule xml = CaseRuleManager.transformRuleInUTF16(ruleDefinition);
            LOG.info("transform rule " + (System.currentTimeMillis() - start));
            String roles = Lists.newArrayList(xml.getRole().split(",")).stream().filter(new Predicate<String>() {
                @Override
                public boolean test(String t) {
                    return t.equals(String.valueOf(pRoleId));
                }
            }).collect(Collectors.joining(","));
            xml.setRole(roles);
            LOG.info("change roles in rule " + (System.currentTimeMillis() - start));
            try {
                ruleDefinition = CaseRuleManager.transformObject(xml, "UTF-16");
                LOG.info("transform rulexml to bytearray in " + (System.currentTimeMillis() - start));
                ruleDao.updateRuleDefinition(toRemove, pPoolId, ruleDefinition);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Rules4DbInteractBean.class.getName()).log(Level.SEVERE, null, ex);
                throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE, ex.getMessage());
            }
            LOG.info("update rule definition " + (System.currentTimeMillis() - start));
        }
        LOG.info("time to update role for rules(" + toRemoveRole.size() + "): " + (System.currentTimeMillis() - overall));
    }

    /**
     * update Role compute change set and update Rules accordingly, remove or
     * add role defintion via update Rule
     *
     * @param pRoleId role id in common db
     * @param pPoolId pool id
     * @param pType type to determine dev or prod
     * @param pNewRuleIds ids of rules active for this pRoleId
     * @throws de.lb.cpx.server.rule.services.RuleEditorProcessException thrown
     * when update failed
     */
    public void updateRole(long pRoleId, long pPoolId, PoolTypeEn pType, List<Long> pNewRuleIds) throws RuleEditorProcessException {
        long computeChangeSet = System.currentTimeMillis();
        CrgRule2RoleDao<CrgRule2Role> roleDao = getRule2RoleDao(pType);
        CrgRulesDao<CrgRules, CrgRule2Role> ruleDao = getRulesDao(pType);

        List<Long> oldRuleIds = roleDao.findRuleIdsForRole(pRoleId, pPoolId);
        List<Long> toRemoveRole = new ArrayList<>();
        List<Long> toAddRole = new ArrayList<>();

        oldRuleIds.stream().filter((oldRuleId) -> (!pNewRuleIds.contains(oldRuleId))).forEachOrdered((oldRuleId) -> {
            toRemoveRole.add(oldRuleId);
        });
        pNewRuleIds.stream().filter((newRuleId) -> (!oldRuleIds.contains(newRuleId))).forEachOrdered((newRuleId) -> {
            toAddRole.add(newRuleId);
        });
        LOG.info("size of rules to add role " + toAddRole.size());
        LOG.info("size of Role to remove " + toRemoveRole.size());
        LOG.info("compute changeSet in " + (System.currentTimeMillis() - computeChangeSet));
        long overall = System.currentTimeMillis();
        List<CrgRules> rulesToRemoveRole = toRemoveRole.isEmpty() ? new ArrayList<>() : ruleDao.findRulesByIds(toRemoveRole, pPoolId);
//        for(Long toRemove : toRemoveRole){
        for (CrgRules rule : rulesToRemoveRole) {
//            rule.setCrgrDefinition(ruleDao.getRuleDefinition(rule.getId(), pPoolId));
//            long start = System.currentTimeMillis();
//            CrgRules rule = ruleDao.getRuleByIdWithoutDefinition(toRemove, pPoolId);
            rule.setCrgrDefinition(ruleDao.getRuleDefinition(rule.getId(), pPoolId));
//            LOG.info("time to fetch single rule: " + (System.currentTimeMillis()-start));
//            start = System.currentTimeMillis();
            rule.getCrgRule2Roles().removeIf(new Predicate<CrgRule2Role>() {
                @Override
                public boolean test(CrgRule2Role t) {
                    return t.getCdbUserRoles().getId() == pRoleId;
                }
            });
            roleDao.remove4RuleIdAndRole(rule.getId(), pRoleId);
            Rule xml = CaseRuleManager.transformRuleInUTF16(rule.getCrgrDefinition());
            String roles = Lists.newArrayList(xml.getRole().split(",")).stream().filter(new Predicate<String>() {
                @Override
                public boolean test(String t) {
                    return !t.equals(String.valueOf(pRoleId));
                }
            }).collect(Collectors.joining(","));
            xml.setRole(roles);
            try {
                rule.setCrgrDefinition(CaseRuleManager.transformObject(xml, "UTF-16"));
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Rules4DbInteractBean.class.getName()).log(Level.SEVERE, null, ex);
                throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE, ex.getMessage());
            }
            ruleDao.saveOrUpdate(rule);
//            LOG.info("time to remove role for single rule: " + (System.currentTimeMillis()-start));
        }
        List<CrgRules> rulesToAddRole = toAddRole.isEmpty() ? new ArrayList<>() : ruleDao.findRulesByIds(toAddRole, pPoolId);
//        for(Long toAdd : toAddRole){
        for (CrgRules rule : rulesToAddRole) {
//            rule.setCrgrDefinition(ruleDao.getRuleDefinition(rule.getId(), pPoolId));
//            long start = System.currentTimeMillis();
            rule.setCrgrDefinition(ruleDao.getRuleDefinition(rule.getId(), pPoolId));
//            LOG.info("time to fetch single rule: " + (System.currentTimeMillis()-start));
//            start = System.currentTimeMillis();
            CrgRule2Role newRole = CrgRule2Role.getTypeInstance(pType);
            newRole.setCrgRules(rule);
            this.setCreationParams(newRole, pType);
            newRole.setCdbUserRoles(userRolesDao.findById(pRoleId));
            roleDao.addNewItem(newRole);
            Rule xml = CaseRuleManager.transformRuleInUTF16(rule.getCrgrDefinition());
            List<String> roles = Lists.newArrayList(xml.getRole().split(","));
            if (!roles.contains(String.valueOf(pRoleId))) {
                roles.add(String.valueOf(pRoleId));
            }
            xml.setRole(roles.stream().collect(Collectors.joining(",")));
            try {
                rule.setCrgrDefinition(CaseRuleManager.transformObject(xml, "UTF-16"));
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Rules4DbInteractBean.class.getName()).log(Level.SEVERE, null, ex);
                throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE, ex.getMessage());
            }
            ruleDao.saveOrUpdate(rule);
//            updateRule(rule, pPoolId, pType);
//            LOG.info("time to add role for single rule: " + (System.currentTimeMillis()-start));
        }
        long time = System.currentTimeMillis() - overall;
        LOG.info("time to update role for rules(" + toRemoveRole.size() + "): " + time);
    }

    public List<CrgRules> getRuleList4Grouper(PoolTypeEn pPoolTypeEn, List<Long> ruleIds) {
        CrgRulesDao<CrgRules, CrgRule2Role> rulesDao = getRulesDao(pPoolTypeEn);
        List<CrgRules> rules = rulesDao.getRuleList4Grouper(ruleIds);
        for (CrgRules rule : rules) {
            Hibernate.initialize(rule.getCrgRulePools());
            Hibernate.initialize(rule.getCrgRuleTypes());
        }
        return rules;
    }

    private void checkCreationParameter(CrgRules pRule, PoolTypeEn pPoolTypeEn) {
        CrgRulesDao<CrgRules, CrgRule2Role> rulesDao = getRulesDao(pPoolTypeEn);
        if (pRule.getId() > 0) {
            CrgRules tmpRule = rulesDao.findById(pRule.getId());
            if (tmpRule != null) {
                pRule.setCreationUser(tmpRule.getCreationUser());
                pRule.setCreationDate(tmpRule.getCreationDate());
            }
        }
    }

    public Map<String, String> getRuleTableNames4List(List<String> pTableIds, PoolTypeEn pPoolType) {
        if (pTableIds == null || pTableIds.isEmpty()) {
            return new HashMap<>();
        }
        List<Long> lTableIds = new ArrayList<>();
        for (String pId : pTableIds) {
            try {
                lTableIds.add(Long.parseLong(pId));
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "could not convert TableId as String = " + pId + " into Long value");
            }
        }
        if (!lTableIds.isEmpty()) {
            Map<String, String> retMap = new HashMap<>();
            CrgRuleTablesDao<CrgRuleTables> tablesDao = getRuleTablesDao(pPoolType);
            List<CrgRuleTables> tables = tablesDao.getRuleTableNames2Ids(lTableIds);
            for (CrgRuleTables tab : tables) {
                retMap.put(String.valueOf(tab.getId()), tab.getCrgtTableName());
            }
            return retMap;
        }

        return new HashMap<>();
    }

    public List<String> getRids4SearchOptions(PoolTypeEn pPoolTypeEn, Map<String, List<String>> pFilterOptionMap) {
        CrgRulesDao<CrgRules, CrgRule2Role> rulesDao = getRulesDao(pPoolTypeEn);
        return rulesDao.getRids4SearchOptions(pFilterOptionMap);

    }

    public long getRulesCount4PoolAndUser(PoolTypeEn pPoolType, long pPoolId, Long userId) {
        CrgRulesDao<CrgRules, CrgRule2Role> rulesDao = getRulesDao(pPoolType);
        return rulesDao.getRulesCount4PoolAndUser(pPoolId, userId);
    }

    public String getComment2RuleTable(long pId, PoolTypeEn pPoolType) {
        CrgRuleTablesDao<CrgRuleTables> dao = getRuleTablesDao(pPoolType);
        CrgRuleTables table = dao.findById(pId);
        if (table != null) {
            return table.getCrgtComment();
        }
        return "";
    }

    public boolean containsPoolErrorMessages(long pPoolId, PoolTypeEn pType) {
        CrgRulePoolsDao<CrgRulePools> poolDao = getPoolDao(pType);
        return poolDao.containsPoolErrorMessages(pPoolId);
    }

    public boolean containsPoolRuleMessages(long pPoolId, PoolTypeEn pType) {
        CrgRulePoolsDao<CrgRulePools> poolDao = getPoolDao(pType);
        return poolDao.containsPoolRuleMessages(pPoolId);
    }

    public boolean containsPoolRuleTableMessages(long pPoolId, PoolTypeEn pType) {
        CrgRulePoolsDao<CrgRulePools> poolDao = getPoolDao(pType);
        return poolDao.containsPoolRuleTableMessages(pPoolId);
    }

    public byte[] getRuleMessage(long pRuleId, long pPoolId, PoolTypeEn pType) {
        CrgRulesDao<CrgRules, CrgRule2Role> ruleDao = getRulesDao(pType);
        return ruleDao.getRuleMessage(pRuleId, pPoolId);
    }
    
    public byte[] getRuleTableMessage(long pRuleTableId, long pPoolId, PoolTypeEn pType) {
        CrgRuleTablesDao<CrgRuleTables> tableDao = getRuleTablesDao(pType);
        return tableDao.getRuleTableMessage(pRuleTableId, pPoolId);
    }
    
}
