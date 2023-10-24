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

import de.lb.cpx.grouper.model.transfer.TransferRuleAnalyseResult;
import de.lb.cpx.model.TCase;
import de.lb.cpx.server.commonDB.model.CCase;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.server.commons.enums.RuleImportCheckFlags;
import de.lb.cpx.server.commons.enums.RuleOverrideFlags;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;
import javax.validation.constraints.NotNull;

/**
 *
 * @author wilde
 */
@Remote
public interface RuleEditorBeanRemote {

    /**
     * @param pType pooltypes
     * @return list of all rules for type
     */
    List<CrgRulePools> getCrgRulePools(PoolTypeEn pType);

    List<CrgRulePools> getProdCrgRulePools();

    List<CrgRulePools> getDevCrgRulePools();

    /**
     * saves a new empty pool object in DB
     *
     * @param pPool pool to save
     * @param pType type to determine if prod or dev
     * @return new id of the pool
     * @throws de.lb.cpx.server.rule.services.RuleEditorProcessException thrown
     * if same pool is already stored in database
     */
    Long savePool(CrgRulePools pPool, PoolTypeEn pType) throws RuleEditorProcessException;

    /**
     * returns id of pool in db
     *
     * @param pPool pool
     * @param pType pool lype
     * @return id of the pool
     */
    Long findPoolId4poolNameAndDates(CrgRulePools pPool, PoolTypeEn pType);

    /**
     * save changed pool object
     *
     * @param pPool pool entity to update
     * @param pType type to determine if prod or dev
     * @throws de.lb.cpx.server.rule.services.RuleEditorProcessException
     * exception
     */
    void updatePool(CrgRulePools pPool, PoolTypeEn pType) throws RuleEditorProcessException;

    void updateDevPool(CrgRulePools pPool) throws RuleEditorProcessException;

    void updateProdPool(CrgRulePools pPool) throws RuleEditorProcessException;

    /**
     * delete pool with all rules and tables
     *
     * @param pPoolId pool by id to delete
     * @param pType type of pool to determine if pord or dev
     * @throws de.lb.cpx.server.rule.services.RuleEditorProcessException error
     */
    void deletePool(long pPoolId, PoolTypeEn pType) throws RuleEditorProcessException;

    List<CrgRules> findRules(long pPoolId, PoolTypeEn pType);

    int findSizeOfRules(long pPoolId, PoolTypeEn pType);

    int findSizeOfRuleTables2Pool(long pPoolId, PoolTypeEn pType);

    /**
     * (persist)empty rule
     *
     * @param pPoolId pool id to save rule in
     * @param poolType type of pool
     * @return generated rid to identify newly created rule in client, assume
     * that rid is unique
     * @throws de.lb.cpx.server.rule.services.RuleEditorProcessException error
     */
//    String saveRule(CrgRules rule, long pPoolId, PoolTypeEn poolType) throws RuleEditorProcessException;
    CrgRules createRule(long pPoolId, PoolTypeEn poolType) throws RuleEditorProcessException;

    CrgRules copyRule(long pRuleId, long pPoolId, PoolTypeEn poolType) throws RuleEditorProcessException;

    void deleteRule(long pRuleId, long pPoolId, PoolTypeEn poolType) throws RuleEditorProcessException;

    List<CrgRuleTables> findRuleTablesForPool(long pPoolId, PoolTypeEn pType);

    List<CrgRuleTables> findRuleTablesForPoolAndContentOrName(long pPoolId, PoolTypeEn pType, String pText);

    /**
     * create the copy of table in the same pool
     *
     * @param table table to copy
     * @return copy of the rule stored in database
     */
    CrgRuleTables copyRuleTable(CrgRuleTables table);

    List<CrgRuleTables> findAllRuleTables(PoolTypeEn pType);

    CrgRuleTables findRuleTable(String pName, PoolTypeEn poolType, long pPoolId);

    CrgRuleTables saveRuleTable(long pPoolId, PoolTypeEn poolType, CrgRuleTables pTables) throws RuleEditorProcessException;

    CrgRuleTables createRuleTable(long pPoolId, PoolTypeEn poolType) throws RuleEditorProcessException;

    void updateRule(CrgRules pRule, long pPoolId, PoolTypeEn pPoolType) throws RuleEditorProcessException;

    /**
     * update table in database
     *
     * @param pPoolId pool id
     * @param poolType type of the pool to determine if prod or dev
     * @param pTable table to update values
     */
    void updateRuleTable(long pPoolId, PoolTypeEn poolType, CrgRuleTables pTable);

    /**
     * @param pRuleId rule database id to fetch content
     * @param pPoolId pool of the rule
     * @param pPoolType type of the choosen pool
     * @return byte array stored with content of the rule
     */
    byte[] findRuleDefinition(long pRuleId, long pPoolId, PoolTypeEn pPoolType);

    /**
     * returns the list of Roles assigned to the rule with ruleId in pool with
     * poolId
     *
     * @param pRuleId database id of the rule
     * @param pPoolType type of the enclosing pool
     * @return List of all roles for that rule
     */
    List<CdbUserRoles> findUserRoles2Rule(long pRuleId, PoolTypeEn pPoolType);

    /**
     * @param pTableId table database id
     * @param poolType pool type to determine if prod or dev
     * @return String content of the rule table
     */
    String getRuleTableContent(long pTableId, PoolTypeEn poolType);

    /**
     * Methode to get all rules associated to a particular rules table
     *
     * @param pPoolId id of the rule pool
     * @param pPoolType type of the pool
     * @param pTableId id of the rule table
     * @return list of all rules for this this table in the pool
     */
    List<CrgRules> findRules4Table(long pPoolId, PoolTypeEn pPoolType, long pTableId);

    /**
     * delete CtgRuleTable in Database, throw exception if Table can not be
     * deleted due to still existing references to rules
     *
     * @param pPoolId pool id to where is table stored
     * @param poolType pooltype of the pool
     * @param toDelete table to delete
     * @throws de.lb.cpx.server.rule.services.RuleEditorProcessException thrown
     * if delete could not be executed
     */
    void deleteRuleTable(long pPoolId, PoolTypeEn poolType, CrgRuleTables toDelete) throws RuleEditorProcessException;

    /**
     *
     * Gets the number of all rules associated with table pTable
     *
     * @param pPoolId id of the rule pool
     * @param poolType type of the pool
     * @param pTable table
     * @return returns count of associated rules
     */
    long getRelatedRulesCount4Table(long pPoolId, PoolTypeEn poolType, CrgRuleTables pTable);
    
    /**
     *
     * Gets the number of all rules associated with table pTable
     *
     * @param pPoolId id of the rule pool
     * @param poolType type of the pool
     * @param pTables table
     * @return returns count of associated rules
     */
    long getRelatedRulesCount4Table(long pPoolId, PoolTypeEn poolType, List<CrgRuleTables> pTables);
    
    /**
     * export pool whole pool as xml
     *
     * @param pPoolId pool database id
     * @param pPoolType pool type to determine prod or dev
     * @return pool as xml string
     * @throws de.lb.cpx.server.rule.services.RuleEditorProcessException error
     */
    String exportPoolAsXML(long pPoolId, PoolTypeEn pPoolType) throws RuleEditorProcessException;

    /**
     * export of selected Rules from pool as xml
     *
     * @param pPoolId pool database id
     * @param pPoolType pool type to determine prod or dev
     * @param ruleIds the list of rule ids
     * @return pool as xml string
     * @throws de.lb.cpx.server.rule.services.RuleEditorProcessException error
     */
    String exportPoolAsXML(long pPoolId, PoolTypeEn pPoolType, List<Long> ruleIds) throws RuleEditorProcessException;

    /**
     * ruleswithout collisions are to copy at once, those that have collisions
     * are to return with collision description
     *
     * @param ruleIds rule ids
     * @param poolId pool id
     * @param pPoolType pool type
     * @param destPoolId destination pool id
     * @param pDestPoolType destination pool type
     * @return Error list of not imported rules, types tables
     * @throws de.lb.cpx.server.rule.services.RuleEditorProcessException thrown
     * if processing failed due to incorrect file structure etc.
     */
    RuleExchangeResult copyRules(List<Long> ruleIds, long poolId, PoolTypeEn pPoolType, long destPoolId, PoolTypeEn pDestPoolType) throws RuleEditorProcessException;

    RuleExchangeResult copyRules(List<Long> ruleIds, long poolId, PoolTypeEn pPoolType, long destPoolId, PoolTypeEn pDestPoolType, RuleOverrideFlags doOverride, RuleImportCheckFlags doCheck) throws RuleEditorProcessException;

    RuleExchangeResult copyTables(List<Long> tableIds, long poolId, PoolTypeEn pPoolType, long destPoolId, PoolTypeEn pDestPoolType) throws RuleEditorProcessException;

    RuleExchangeResult copyTables(List<Long> tableIds, long poolId, PoolTypeEn pPoolType, long destPoolId, PoolTypeEn pDestPoolType, RuleOverrideFlags doOverride, RuleImportCheckFlags doCheck) throws RuleEditorProcessException;

    /**
     * imports rules from xml file
     *
     * @param pPoolId target pool to import into
     * @param pPoolType type to determine if prod or dev
     * @param pXmlFileContent xml file content
     * @return Error list of not imported rules, types tables
     * @throws de.lb.cpx.server.rule.services.RuleEditorProcessException thrown
     * if processing failed due to incorrect file structure etc.
     */
    RuleExchangeResult importPoolFromXML(long pPoolId, PoolTypeEn pPoolType, String pXmlFileContent) throws RuleEditorProcessException;

    /**
     * imports rules from xml file
     *
     * @param pPoolId target pool to import into
     * @param pPoolType type to determine if prod or dev
     * @param pXmlFileContent xml file content
     * @param RuleIdents4import list of rule idents for rules to import
     * @param doOverride override policy
     * @param doCheck check for import, if not, all rules and tables will be
     * saved im pool without validation
     * @return Error list of not imported rules, types tables
     * @throws de.lb.cpx.server.rule.services.RuleEditorProcessException thrown
     * if processing failed due to incorrect file structure etc.
     */
    RuleExchangeResult importPoolFromXML(long pPoolId, PoolTypeEn pPoolType, String pXmlFileContent, List<String> RuleIdents4import, RuleOverrideFlags doOverride, RuleImportCheckFlags doCheck) throws RuleEditorProcessException;

    /**
     * @param pType type to determine prod or dev
     * @return list of all rule types stored for this types
     */
    List<CrgRuleTypes> findAllRuleTypes(PoolTypeEn pType);

    /**
     * @param pRuleId rule database id
     * @param pPoolId pool id rule is stored into
     * @param poolType type to determine prod or dev
     * @return string of rule type name
     */
    String getRuleTypeNameForRule(long pRuleId, long pPoolId, PoolTypeEn poolType);

    /**
     * @param pRuleId rule database id
     * @param pPoolId pool id rule is stored into
     * @param poolType type to determine prod or dev
     * @return string of rule type display name
     */
    String getRuleTypeDisplayNameForRule(long pRuleId, long pPoolId, PoolTypeEn poolType);

    /**
     * Update rule table in database
     *
     * @param ruleTable ruletable to update
     * @param type type to determine prod or dev
     */
    void updateRuleTable(CrgRuleTables ruleTable, PoolTypeEn type);

    /**
     * get Count for the number of rules active for a user role
     *
     * @param pPoolId pool id
     * @param pType type to determine dev or prod
     * @param pRoleId role id
     * @return count of rules
     */
    Long getRuleCountForRole(long pPoolId, PoolTypeEn pType, long pRoleId);

    List<CrgRules> getAllActiveRulesForRole(Long pRoleId, long pPoolId, PoolTypeEn pPoolType);

    List<CrgRules> getAllAvailableRulesForRole(Long pRoleId, long pPoolId, PoolTypeEn pPoolType);

    /**
     * delete the list of rules from pool
     *
     * @param pRuleIds list of rule ids
     * @param pPoolId pool id
     * @param poolType pool type
     * @throws RuleEditorProcessException exception which occurs by this action
     */
    void deleteRules(List<Long> pRuleIds, long pPoolId, PoolTypeEn poolType) throws RuleEditorProcessException;

    /**
     * deletes the list of rule tables from pool
     *
     * @param pPoolId pool id
     * @param poolType pool type
     * @param tableIds list of tables to delete
     * @throws RuleEditorProcessException error
     */
    void deleteRuleTables(long pPoolId, PoolTypeEn poolType, List<Long> tableIds) throws RuleEditorProcessException;

    CrgRuleTypes createRuleType(PoolTypeEn poolType) throws RuleEditorProcessException;

    void deleteRuleTypes(List<Long> pTypesIds, PoolTypeEn poolType) throws RuleEditorProcessException;

    void deleteRuleType(long pTypeId, PoolTypeEn poolType) throws RuleEditorProcessException;

    void updateRuleType(CrgRuleTypes pRuleType, PoolTypeEn poolType) throws RuleEditorProcessException;

    /**
     * get all rules based on given pool type
     *
     * @param poolTypeEn type of pool
     * @return list of asked rules
     *
     */
    List<CrgRules> getAllRules(PoolTypeEn poolTypeEn);

    /**
     *
     * @return list of rules for this session. If it is prod - than all rules
     * from PROD - Pools , by rule editor according to settings. Now it is DEV
     * and from Pools, that are created with the actual user
     */
    Map<Long, CrgRules> getRules4user();

    /**
     *
     * @return list of rule pools for this session. If it is prod - than all
     * rules from PROD - Pools , by rule editor according to settings. Now it is
     * DEV and from Pools, that are created with the actual user
     */
    List<CrgRulePools> getRulePools4user();

    /**
     *
     * @return list of rule types for this session. If it is prod - than all
     * rules from PROD - Pools , by rule editor according to settings. Now it is
     * DEV and from Pools, that are created with the actual user
     */
    List<CrgRuleTypes> getRuleTypes4user();

    /**
     * update Role to new values!
     *
     * @param pRoleId role to Update
     * @param pPoolId in which role should be updated
     * @param pType type to determine prod or dev
     * @param pRuleIds ids of active rules
     * @throws de.lb.cpx.server.rule.services.RuleEditorProcessException thrown
     * if update failed
     */
    void updateRole(long pRoleId, long pPoolId, PoolTypeEn pType, List<Long> pRuleIds) throws RuleEditorProcessException;

    TransferRuleAnalyseResult analyseRule(@NotNull String pRule, @NotNull TCase pCase, PoolTypeEn pPoolType);

    TransferRuleAnalyseResult analyseRule(@NotNull String pRule, @NotNull TCase pCase, Map<String, String> pTables, PoolTypeEn pPoolType);

    TransferRuleAnalyseResult analyseRule(@NotNull String pRule, @NotNull TCase pCase, Map<String, String> pTables, PoolTypeEn pPoolType, List<TCase> pHistoryCases);

    long getSizeOfTypeRuleRelation(long pTypeId, PoolTypeEn pPoolType);

    /**
     * import pool from xml file
     *
     * @param pPoolId pool id to import rules into
     * @param pPoolType pool type of pool
     * @param pXmlFileContent string content as xml
     * @param pDoOverride override strategy
     * @param pDoCheck check strategy
     * @return import result with failed rules
     * @throws RuleEditorProcessException exception if import failed
     */
    RuleExchangeResult importPoolFromXML(long pPoolId, PoolTypeEn pPoolType, String pXmlFileContent, RuleOverrideFlags pDoOverride, RuleImportCheckFlags pDoCheck) throws RuleEditorProcessException;
    /**
     * import pool from xml file
     *
     * @param pPoolId pool id to import rules into
     * @param pPoolType pool type of pool
     * @param pXmlFileContent string content as xml
     * @param pDoOverride override strategy
     * @param pDoCheck check strategy
     * @param pDoTransferCheck check codes strategy
     * @return import result with failed rules
     * @throws RuleEditorProcessException exception if import failed
     */
    RuleExchangeResult importPoolFromXML(long pPoolId, PoolTypeEn pPoolType, String pXmlFileContent, 
            RuleOverrideFlags pDoOverride, RuleImportCheckFlags pDoCheck, boolean pDoTransferCheck) throws RuleEditorProcessException;

    /**
     * @param cpxUserId cpx user id
     * @return all analyser cases for this user
     */
    List<CCase> getAllAnalyserCases(long cpxUserId);

    /**
     * @param pAnalyserCase analyser case copy stored usually in commondb
     * @return tcase object created from content of analyser case
     */
    TCase getHospitalCaseFromCaseCopy(CCase pAnalyserCase);

    /**
     * @param pAnalyserCaseId analyser case database id
     * @return tcase object created from content of analyser case
     */
    TCase getHospitalCaseFromCaseCopy(long pAnalyserCaseId);

    /**
     * @param pCases list of database ids to delete
     * @return indicator if delete was successful
     */
    Boolean deleteAnalyserCases(List<Long> pCases);

    Collection<String> getMatchesAnalyserCaseCategory(String pText);

    Boolean saveAnalyserCases(CCase pCase);

    Boolean updateAnalyserCaseContent(Long pAnalyserCaseId, TCase pHospCase);

    java.util.List<CCase> getAllAnalyserCasesForCurrentUser();


    /**
     * @param pId table database id
     * @param pPoolType pool type to determine if prod or dev
     * @return String comment of the rule table
     */
    String getRuleTableComment(long pId, PoolTypeEn pPoolType);
    
    /**
     * @param pId pool database id
     * @param pType type of pool
     * @return indicator if the pool contains an erroneous rule or rule table 
     */
    boolean containsAnyMessage(long pId, PoolTypeEn pType);
    /**
     * @param pId pool database id
     * @param pType type of pool
     * @return indicator if the pool contains an erroneous rule
     */
    boolean containsRuleMessage(long pId, PoolTypeEn pType);
    /**
     * @param pId pool database id
     * @param pType type of pool
     * @return indicator if the pool contains an erroneous rule table 
     */
    boolean containsRuleTableMessage(long pId, PoolTypeEn pType);

    public byte[] getRuleMessage(long pRuleId, long pPoolId, PoolTypeEn poolType);
    
    public byte[] getRuleTableMessage(long pRuleId, long pPoolId, PoolTypeEn poolType);
}
