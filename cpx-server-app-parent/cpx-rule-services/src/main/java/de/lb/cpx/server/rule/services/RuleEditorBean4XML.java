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
package de.lb.cpx.server.rule.services;

import de.checkpoint.ruleGrouper.data.CRGRulePool;
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
import de.lb.cpx.service.readrules.RuleReadServiceLocal;
import de.lb.cpx.service.rules.RulesTransferFactoryBean;
import de.lb.cpx.service.startup_ejb.RuleReadBeanLocal;
import de.lb.cpx.shared.rules.util.RulesConverter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author gerschmann
 */
@Stateless
public class RuleEditorBean4XML extends RuleReadServiceLocal {

    private static final Logger LOG = Logger.getLogger(RuleEditorBean4XML.class.getName());

    @EJB
    RulesTransferFactoryBean rulesTransferFactoryBean;

    @EJB(beanName = "RuleReadXmlBean")
    private RuleReadBeanLocal ruleStartUpbean;

//    //@Override
    public List<CrgRulePools> getCrgRulePools(PoolTypeEn pType) {
        if (pType.equals(PoolTypeEn.DEV)) {
            return getDevCrgRulePools();
        } else {
            return getProdCrgRulePools();
        }
    }

//    //@Override
    public List<CrgRulePools> getProdCrgRulePools() {
        if (cpxPools.isEmpty()) {
            List<CRGRulePool> pools = rulesTransferFactoryBean.getRulePoolsIntern();

            for (CRGRulePool pool : pools) {
                CrgRulePools cpxPool = RulesConverter.getCpxRulePool(pool, PoolTypeEn.PROD);
                cpxPools.put(cpxPool.getId(), cpxPool);

            }

        }
        return new ArrayList<>(cpxPools.values());

    }

    //@Override
    public List<CrgRulePools> getDevCrgRulePools() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public Long savePool(CrgRulePools pPool, PoolTypeEn pType) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public Long findPoolId4poolNameAndDates(CrgRulePools pPool, PoolTypeEn pType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public void updatePool(CrgRulePools pPool, PoolTypeEn pType) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public void updateDevPool(CrgRulePools pPool) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public void updateProdPool(CrgRulePools pPool) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public void deletePool(long pPoolId, PoolTypeEn pType) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public List<CrgRules> findRules(long pPoolId, PoolTypeEn pType) {
        return ruleStartUpbean.
                getRules4Year(pPoolId);
    }

    //@Override
    public int findSizeOfRules(long pPoolId, PoolTypeEn pType) {
        return ruleStartUpbean.findSizeOfRules(pPoolId);
    }

    //@Override
    public int findSizeOfRuleTables2Pool(long pPoolId, PoolTypeEn pType) {
        return ruleStartUpbean.findSizeOfRuleTables2Pool(pPoolId);
    }

    //@Override
    public CrgRules createRule(long pPoolId, PoolTypeEn poolType) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public CrgRules copyRule(long pRuleId, long pPoolId, PoolTypeEn poolType) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public void deleteRule(long pRuleId, long pPoolId, PoolTypeEn poolType) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public List<CrgRuleTables> findRuleTablesForPool(long pPoolId, PoolTypeEn pType) {
        return ruleStartUpbean.findRuleTables4Year(pPoolId);
    }

    //@Override
    public List<CrgRuleTables> findRuleTablesForPoolAndContentOrName(long pPoolId, PoolTypeEn pType, String pText) {
        return ruleStartUpbean.findRuleTables4YearAndContentOrName(pPoolId, pText);
    }

    //@Override
    public CrgRuleTables copyRuleTable(CrgRuleTables table) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public List<CrgRuleTables> findAllRuleTables(PoolTypeEn pType) {
        return ruleStartUpbean.getCpxTables();
    }

    //@Override
    public CrgRuleTables findRuleTable(String pName, PoolTypeEn poolType, long pPoolId) {
        return ruleStartUpbean.getCpxTable4Year(pName, pPoolId);
    }

    //@Override
    public CrgRuleTables saveRuleTable(long pPoolId, PoolTypeEn poolType, CrgRuleTables pTables) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public CrgRuleTables createRuleTable(long pPoolId, PoolTypeEn poolType) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public void updateRule(CrgRules pRule, long pPoolId, PoolTypeEn pPoolType) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public void updateRuleTable(long pPoolId, PoolTypeEn poolType, CrgRuleTables pTable) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public byte[] findRuleDefinition(long pRuleId, long pPoolId, PoolTypeEn pPoolType) {
        return ruleStartUpbean.getRuleDefinition(pRuleId, pPoolId);
    }

    //@Override
    public List<CdbUserRoles> findUserRoles2Rule(long pRuleId, PoolTypeEn pPoolType) {
        return ruleStartUpbean.findUserRoles2Rule(pRuleId);
    }

    //@Override
    public String getRuleTableContent(long pTableId, PoolTypeEn poolType) {
        return ruleStartUpbean.getRuleTableContent(pTableId);
    }

    //@Override
    public List<CrgRules> findRules4Table(long pPoolId, PoolTypeEn pPoolType, long pTableId) {
        return ruleStartUpbean.findRules4TableAndYear(pPoolId, pTableId);
    }

    //@Override
    public void deleteRuleTable(long pPoolId, PoolTypeEn poolType, CrgRuleTables toDelete) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public long getRelatedRulesCount4Table(long pPoolId, PoolTypeEn poolType, CrgRuleTables pTable) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public String exportPoolAsXML(long pPoolId, PoolTypeEn pPoolType) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    /**
     * Reads Rule pool from the file system into XML - String
     *
     * @param pPoolId Pool yeare
     * @param pPoolType PROd - only
     * @param ruleIds list of rules for export
     * @return
     * @throws RuleEditorProcessException
     */
    public String exportPoolAsXML(long pPoolId, PoolTypeEn pPoolType, List<Long> ruleIds) throws RuleEditorProcessException {
        List<CrgRules> rules4Pool = ruleStartUpbean.getCpxRules4ids(ruleIds);
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public RuleExchangeResult copyRules(List<Long> ruleIds, long poolId, PoolTypeEn pPoolType, long destPoolId, PoolTypeEn pDestPoolType) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public RuleExchangeResult copyRules(List<Long> ruleIds, long poolId, PoolTypeEn pPoolType, long destPoolId, PoolTypeEn pDestPoolType, RuleOverrideFlags doOverride, RuleImportCheckFlags doCheck) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public RuleExchangeResult copyTables(List<Long> tableIds, long poolId, PoolTypeEn pPoolType, long destPoolId, PoolTypeEn pDestPoolType) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public RuleExchangeResult copyTables(List<Long> tableIds, long poolId, PoolTypeEn pPoolType, long destPoolId, PoolTypeEn pDestPoolType, RuleOverrideFlags doOverride, RuleImportCheckFlags doCheck) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public RuleExchangeResult importPoolFromXML(long pPoolId, PoolTypeEn pPoolType, String pXmlFileContent) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public RuleExchangeResult importPoolFromXML(long pPoolId, PoolTypeEn pPoolType, String pXmlFileContent, List<String> RuleIdents4import, RuleOverrideFlags doOverride, RuleImportCheckFlags doCheck) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public List<CrgRuleTypes> findAllRuleTypes(PoolTypeEn pType) {
        return ruleStartUpbean.getCpxTypes();
    }

    //@Override
    public String getRuleTypeNameForRule(long pRuleId, long pPoolId, PoolTypeEn poolType) {
        return ruleStartUpbean.getRuleTypeNameForRuleAndYear(pRuleId, pPoolId);
    }

    //@Override
    public String getRuleTypeDisplayNameForRule(long pRuleId, long pPoolId, PoolTypeEn poolType) {
        return ruleStartUpbean.getRuleTypeDisplayNameForRuleAndYear(pRuleId, pPoolId);
    }

    //@Override
    public void updateRuleTable(CrgRuleTables ruleTable, PoolTypeEn type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public Long getRuleCountForRole(long pPoolId, PoolTypeEn pType, long pRoleId) {
        return ruleStartUpbean.getRuleCountForRole(pPoolId, pRoleId);
    }

    //@Override
    public List<CrgRules> getAllActiveRulesForRole(Long pRoleId, long pPoolId, PoolTypeEn pPoolType) {
        return ruleStartUpbean.getAllRules4PoolAndRole(pPoolId, pRoleId, true);
    }

    //@Override
    public List<CrgRules> getAllAvailableRulesForRole(Long pRoleId, long pPoolId, PoolTypeEn pPoolType) {
        return ruleStartUpbean.getAllRules4PoolAndRole(pPoolId, pRoleId, false);
    }

    //@Override
    public void deleteRules(List<Long> pRuleIds, long pPoolId, PoolTypeEn poolType) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public void deleteRuleTables(long pPoolId, PoolTypeEn poolType, List<Long> tableIds) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public CrgRuleTypes createRuleType(PoolTypeEn poolType) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public void deleteRuleTypes(List<Long> pTypesIds, PoolTypeEn poolType) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public void deleteRuleType(long pTypeId, PoolTypeEn poolType) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public void updateRuleType(CrgRuleTypes pRuleType, PoolTypeEn poolType) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public List<CrgRules> getAllRules(PoolTypeEn poolTypeEn) {
        return ruleStartUpbean.getCpxRules();
    }

    //@Override
    public List<CrgRules> getRules4user() {
        return ruleStartUpbean.getCpxRules();
    }

    //@Override
    public List<CrgRulePools> getRulePools4user() {
        return ruleStartUpbean.getCpxPools();
    }

    //@Override
    public List<CrgRuleTypes> getRuleTypes4user() {
        return ruleStartUpbean.getCpxTypes();
    }

    //@Override
    public void updateRole(long pRoleId, long pPoolId, PoolTypeEn pType, List<Long> pRuleIds) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public TransferRuleAnalyseResult analyseRule(String pRule, TCase pCase, PoolTypeEn pPoolType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public TransferRuleAnalyseResult analyseRule(String pRule, TCase pCase, Map<String, String> pTables, PoolTypeEn pPoolType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public long getSizeOfTypeRuleRelation(long pTypeId, PoolTypeEn pPoolType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public RuleExchangeResult importPoolFromXML(long pPoolId, PoolTypeEn pPoolType, String pXmlFileContent, RuleOverrideFlags pDoOverride, RuleImportCheckFlags pDoCheck) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public List<CCase> getAllAnalyserCases(long cpxUserId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public TCase getHospitalCaseFromCaseCopy(CCase pAnalyserCase) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public TCase getHospitalCaseFromCaseCopy(long pAnalyserCaseId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public Boolean deleteAnalyserCases(List<Long> pCases) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public Collection<String> getMatchesAnalyserCaseCategory(String pText) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public Boolean saveAnalyserCases(CCase pCase) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public Boolean updateAnalyserCaseContent(Long pAnalyserCaseId, TCase pHospCase) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //@Override
    public List<CCase> getAllAnalyserCasesForCurrentUser() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
