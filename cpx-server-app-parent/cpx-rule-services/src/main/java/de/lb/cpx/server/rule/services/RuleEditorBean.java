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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.lb.cpx.grouper.model.transfer.Transfer4RuleAnalyse;
import de.lb.cpx.grouper.model.transfer.TransferCase;
import de.lb.cpx.grouper.model.transfer.TransferRuleAnalyseResult;
import de.lb.cpx.model.TCase;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.commonDB.dao.CCaseDao;
import de.lb.cpx.server.commonDB.dao.Rules4DbInteractBean;
import de.lb.cpx.server.commonDB.model.CCase;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.server.commons.dao.MenuCacheEntity;
import de.lb.cpx.server.commons.enums.RuleImportCheckFlags;
import de.lb.cpx.server.commons.enums.RuleOverrideFlags;
import de.lb.cpx.server.config.CpxServerConfigLocal;
import de.lb.cpx.server.rule.analyser.RuleCheckService;
import de.lb.cpx.service.ejb.GrouperCommunication;
import de.lb.cpx.shared.rules.enums.CaseValidationGroupErrList;
import de.lb.cpx.system.properties.CpxSystemProperties;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.hibernate.HibernateException;
import org.wildfly.common.annotation.NotNull;

/**
 *
 * @author gerschmann
 */
@Stateless
public class RuleEditorBean implements RuleEditorBeanRemote {

    private static final Logger LOG = Logger.getLogger(RuleEditorBean.class.getName());

    @EJB(name = "CpxServerConfig")
    private CpxServerConfigLocal cpxServerConfig;

    @EJB
    private Rules4DbInteractBean rules4DbInteractBean;

//    @EJB(beanName = "RuleReadXmlBean")
//    private RuleReadBeanLocal ruleStartUpbean;
    @EJB
    private RuleEditorBean4DB ruleEditorBean;

    @EJB(beanName = "RuleCheckServiceLocal")
    private RuleCheckService ruleCheckService;

    @EJB
    private GrouperCommunication grouperCommunication;

    @EJB
    private CCaseDao analyserCaseDao;

    @EJB
    private RuleEditorBean4XML ruleEditorBeanXML;

    @Override
    public Map<Long, CrgRules> getRules4user() {
        return getMenuCacheItems();
    }

    public Map<Long, CrgRules> getMenuCacheItems() {
        final List<CrgRules> result;
        if (isProd()) {
            if (isFromDb()) {
                // load rules without content ?? (like, getAllRules_excludeContentAndUser)
                result = rules4DbInteractBean.getCrgRules(PoolTypeEn.PROD, null);
            } else {

                result = ruleEditorBeanXML.getRules4user();

            }
        } else {
            result = rules4DbInteractBean.getCrgRules(PoolTypeEn.DEV, ClientManager.getCurrentCpxUserId());
        }

        return MenuCacheEntity.toMap(result);
//        final Map<Long, CrgRules> map = new TreeMap<>();
//        for (CrgRules rule : result) {
//            map.put(rule.id, rule);
//        }
//        return map;
    }

    @Override
    public List<CrgRulePools> getRulePools4user() {
        if (isProd()) {
            if (isFromDb()) {
                return rules4DbInteractBean.getCrgRulePools(PoolTypeEn.PROD);
            } else {

                return ruleEditorBeanXML.getRulePools4user();

//                return ruleEditorBeanXML.getRulePools4user(); // we get all rule pools from file system
            }
        } else {
            return rules4DbInteractBean.getCrgRulePools(PoolTypeEn.DEV, ClientManager.getCurrentCpxUserId());
        }
    }

    @Override
    public List<CrgRuleTypes> getRuleTypes4user() {
        if (isProd()) {
            if (isFromDb()) {
                return rules4DbInteractBean.getRuleTypesAsList(PoolTypeEn.PROD);
            } else {
                return ruleEditorBeanXML.getRuleTypes4user();
            }
        } else {
            return rules4DbInteractBean.getRuleTypesAsList(PoolTypeEn.DEV);//, ClientManager.getCurrentCpxUserId());RuleTypes are for all users unique
        }
    }

    /**
     * temporary decision bevore i know what settigs are to use for pools
     *
     */
    private PoolTypeEn getPoolType() {
        if (cpxServerConfig.isRuleEditorClient()) {
            return PoolTypeEn.DEV;
        }
        return PoolTypeEn.PROD;
    }

    private boolean isProd() {
        return getPoolType().equals(PoolTypeEn.PROD);
    }

    private boolean isFromDb() {
        return cpxServerConfig.getRulesDatabaseConfig();
    }

    @Override
    public List<CrgRulePools> getCrgRulePools(PoolTypeEn pType) {
        if (isFromDb() || pType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.getCrgRulePools(pType);
        } else {

            return ruleEditorBeanXML.getRulePools4user();

        }
    }

    @Override
    public List<CrgRulePools> getProdCrgRulePools() {
        if (isFromDb()) {
            return ruleEditorBean.getCrgRulePools(PoolTypeEn.PROD);
        } else {
            return ruleEditorBeanXML.getRulePools4user();
        }
    }

    @Override
    public List<CrgRulePools> getDevCrgRulePools() {
//        if (isFromDb()) {
//            // we show dev - pools for active user only           
//            return ruleEditorBean.getCrgRulePools(PoolTypeEn.DEV, ClientManager.getCurrentCpxUserId());
//        } else {
////            return ruleStartUpbean.getCpxPools();rules from files are only prod
//            return new ArrayList<>();
//        }
        return ruleEditorBean.getCrgRulePools(PoolTypeEn.DEV, ClientManager.getCurrentCpxUserId());
    }

    @Override
    public Long savePool(CrgRulePools pPool, PoolTypeEn pType) throws RuleEditorProcessException {
        if (isFromDb() || pType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.savePool(pPool, pType);
        } else {

            // TODO: Save to filesystem
            return 0L;
        }
    }

    @Override
    public Long findPoolId4poolNameAndDates(CrgRulePools pPool, PoolTypeEn pType) {
        if (isFromDb() || pType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.findPoolId4poolNameAndDates(pPool, pType);
        } else {
            //TODO
            return 0L;
        }
    }

    @Override
    public void updatePool(CrgRulePools pPool, PoolTypeEn pType) throws RuleEditorProcessException {
        if (isFromDb() || pType.equals(PoolTypeEn.DEV)) {
            ruleEditorBean.updatePool(pPool, pType);
        } else {
//TODO:
        }
    }

    @Override
    public void updateDevPool(CrgRulePools pPool) throws RuleEditorProcessException {
//        if (isFromDb()) {
        ruleEditorBean.updateDevPool(pPool);
//        }
    }

    @Override
    public void updateProdPool(CrgRulePools pPool) throws RuleEditorProcessException {
        if (isFromDb()) {
            ruleEditorBean.updateProdPool(pPool);
        } else {
            // TODO: 
        }
    }

    @Override
    public void deletePool(long pPoolId, PoolTypeEn pType) throws RuleEditorProcessException {

        if (isFromDb() || pType.equals(PoolTypeEn.DEV)) {
            ruleEditorBean.deletePool(pPoolId, pType);
        } else {
            //TODO:
        }

    }

    @Override
    public List<CrgRules> findRules(long pPoolId, PoolTypeEn pType) {
        if (isFromDb() || pType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.findRules(pPoolId, pType);
        } else {

            return ruleEditorBeanXML.findRules(pPoolId, pType);

        }
    }

    @Override
    public int findSizeOfRules(long pPoolId, PoolTypeEn pType) {

        if (isFromDb() || pType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.findSizeOfRules(pPoolId, pType);
        } else {
            return ruleEditorBeanXML.findSizeOfRules(pPoolId, pType);
        }

    }

    @Override
    public int findSizeOfRuleTables2Pool(long pPoolId, PoolTypeEn pType) {

        if (isFromDb() || pType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.findSizeOfRuleTables2Pool(pPoolId, pType);
        } else {
            return ruleEditorBeanXML.findSizeOfRuleTables2Pool(pPoolId, pType);
        }

    }

    @Override
    public CrgRules createRule(long pPoolId, PoolTypeEn pPoolType) throws RuleEditorProcessException {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.createRule(pPoolId, pPoolType);
        } else {
            //TODO:
            return null;
        }
    }

    @Override
    public CrgRules copyRule(long pRuleId, long pPoolId, PoolTypeEn pPoolType) throws RuleEditorProcessException {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.copyRule(pRuleId, pPoolId, pPoolType);
        } else {
            //TODO:
            return null;
        }
    }

    @Override
    public void deleteRule(long pRuleId, long pPoolId, PoolTypeEn pPoolType) throws RuleEditorProcessException {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            ruleEditorBean.deleteRule(pRuleId, pPoolId, pPoolType);
        } else {
            //TODO:
        }

    }

    @Override
    public List<CrgRuleTables> findRuleTablesForPool(long pPoolId, PoolTypeEn pType) {

        if (isFromDb() || pType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.findRuleTablesForPool(pPoolId, pType);
        } else {
            return this.ruleEditorBeanXML.findRuleTablesForPool(pPoolId, pType);
        }

    }

    @Override
    public List<CrgRuleTables> findRuleTablesForPoolAndContentOrName(long pPoolId, PoolTypeEn pType, String pText) {
        pText = pText.replace("*", "%");

        if (isFromDb() || pType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.findRuleTablesForPoolAndContentOrName(pPoolId, pType, pText);
        } else {
            return ruleEditorBeanXML.findRuleTablesForPoolAndContentOrName(pPoolId, pType, pText);
        }

    }

    @Override
    public CrgRuleTables copyRuleTable(CrgRuleTables table) {
        PoolTypeEn type = (table.getClass().getSimpleName().endsWith("Dev")) ? PoolTypeEn.DEV : PoolTypeEn.PROD;

        if (isFromDb() || type.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.copyRuleTable(table);
        } else {
//TODO
            return null;
        }
    }

    @Override
    public List<CrgRuleTables> findAllRuleTables(PoolTypeEn pType) {

        if (isFromDb() || pType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.findAllRuleTables(pType);
        } else {
            return ruleEditorBeanXML.findAllRuleTables(pType);
        }
    }

    @Override
    public CrgRuleTables findRuleTable(String pName, PoolTypeEn pPoolType, long pPoolId) {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.findRuleTable(pName, pPoolType, pPoolId);
        } else {
            return ruleEditorBeanXML.findRuleTable(pName, pPoolType, pPoolId);
        }

    }

    @Override
    public CrgRuleTables saveRuleTable(long pPoolId, PoolTypeEn pPoolType, CrgRuleTables pTables) throws RuleEditorProcessException {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.saveRuleTable(pPoolId, pPoolType, pTables);
        } else {
            //TODO:

            return null;
        }

    }

    @Override
    public CrgRuleTables createRuleTable(long pPoolId, PoolTypeEn pPoolType) throws RuleEditorProcessException {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.createRuleTable(pPoolId, pPoolType);
        } else {
            // TODO:

            return null;
        }

    }

    @Override
    public void updateRule(CrgRules pRule, long pPoolId, PoolTypeEn pPoolType) throws RuleEditorProcessException {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            ruleEditorBean.updateRule(pRule, pPoolId, pPoolType);
        } else {
            //TODO
        }

    }

    @Override
    public void updateRuleTable(long pPoolId, PoolTypeEn pPoolType, CrgRuleTables pTable) {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            ruleEditorBean.updateRuleTable(pPoolId, pPoolType, pTable);
        } else {
            //TODO:
        }

    }

    @Override
    public byte[] findRuleDefinition(long pRuleId, long pPoolId, PoolTypeEn pPoolType) {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.findRuleDefinition(pRuleId, pPoolId, pPoolType);
        } else {
            return ruleEditorBeanXML.findRuleDefinition(pRuleId, pPoolId, pPoolType);
        }

    }

    @Override
    public List<CdbUserRoles> findUserRoles2Rule(long pRuleId, PoolTypeEn pPoolType) {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.findUserRoles2Rule(pRuleId, pPoolType);
        } else {
            return ruleEditorBeanXML.findUserRoles2Rule(pRuleId, pPoolType);
        }

    }

    @Override
    public String getRuleTableContent(long pTableId, PoolTypeEn pPoolType) {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.getRuleTableContent(pTableId, pPoolType);
        } else {
            return ruleEditorBeanXML.getRuleTableContent(pTableId, pPoolType);
        }

    }

    @Override
    public List<CrgRules> findRules4Table(long pPoolId, PoolTypeEn pPoolType, long pTableId) {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.findRules4Table(pPoolId, pPoolType, pTableId);
        } else {
            //TODO:

            return ruleEditorBeanXML.findRules4Table(pPoolId, pPoolType, pTableId);
        }
    }

    @Override
    public void deleteRuleTable(long pPoolId, PoolTypeEn pPoolType, CrgRuleTables toDelete) throws RuleEditorProcessException {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            ruleEditorBean.deleteRuleTable(pPoolId, pPoolType, toDelete);
        } else {
            //TODO:
        }

    }

    @Override
    public long getRelatedRulesCount4Table(long pPoolId, PoolTypeEn pPoolType, CrgRuleTables pTable) {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.getRelatedRulesCount4Table(pPoolId, pPoolType, pTable);
        } else {
            //TODO:
            return 0L;
        }

    }
    
    @Override
    public long getRelatedRulesCount4Table(long pPoolId, PoolTypeEn pPoolType, List<CrgRuleTables> pTables) {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.getRelatedRulesCount4Table(pPoolId, pPoolType, pTables);
        } else {
            //TODO:
            return 0L;
        }

    }

    @Override
    public long getSizeOfTypeRuleRelation(long pTypeId, PoolTypeEn pPoolType) {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            return rules4DbInteractBean.sizeOfTypeRuleRelation(pTypeId, pPoolType);
        } else {
            //TODO:
            return 0L;
        }

    }

    @Override
    public String exportPoolAsXML(long pPoolId, PoolTypeEn pPoolType) throws RuleEditorProcessException {
        return exportPoolAsXML(pPoolId, pPoolType, null);
    }

    @Override
    public String exportPoolAsXML(long pPoolId, PoolTypeEn pPoolType, List<Long> ruleIds) throws RuleEditorProcessException {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.exportPoolAsXML(pPoolId, pPoolType, ruleIds);
        } else {
            //TODO

            return null;
        }

    }

    @Override
    public RuleExchangeResult copyRules(List<Long> ruleIds, long pPoolId, PoolTypeEn pPoolType, long destPoolId, PoolTypeEn pDestPoolType) throws RuleEditorProcessException {

        return this.copyRules(ruleIds, pPoolId, pPoolType, destPoolId, pDestPoolType, RuleOverrideFlags.SAVE_OLD, RuleImportCheckFlags.CHECK_4_COLLISIONS);

    }

    @Override
    public RuleExchangeResult copyRules(List<Long> ruleIds, long pPoolId, PoolTypeEn pPoolType, long destPoolId, PoolTypeEn pDestPoolType, RuleOverrideFlags doOverride, RuleImportCheckFlags doCheck) throws RuleEditorProcessException {
            if (isFromDb()|| pPoolType.equals(PoolTypeEn.DEV) && pDestPoolType.equals(PoolTypeEn.DEV)) {
                return ruleEditorBean.copyRules(ruleIds, pPoolId, pPoolType, destPoolId, pDestPoolType, doOverride, doCheck);
            } else {
                if (pDestPoolType.equals(PoolTypeEn.PROD)) {
                    //TODO:
                    // we do not copy rules in the prod pool when die rules are in files
                    return null;
                } else {
                    if (pPoolType.equals(PoolTypeEn.DEV)) {
                        return ruleEditorBean.copyRules(ruleIds, pPoolId, pPoolType, destPoolId, pDestPoolType, doOverride, doCheck);
                    } else {
                        // source - pool is XML; dest - pool is DB

                        String rulesXml = ruleEditorBeanXML.exportPoolAsXML(pPoolId, pPoolType, ruleIds);
                        return ruleEditorBean.importPoolFromXML(destPoolId, pDestPoolType, rulesXml, null, doOverride, doCheck, true, (int)pPoolId);// xml: poolId = pool year
                    }
                }
            }        

    }

    @Override
    public RuleExchangeResult copyTables(List<Long> tableIds, long pPoolId, PoolTypeEn pPoolType, long destPoolId, PoolTypeEn pDestPoolType) throws RuleEditorProcessException {

        if (isFromDb()|| pPoolType.equals(PoolTypeEn.DEV) && pDestPoolType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.copyTables(tableIds, pPoolId, pPoolType, destPoolId, pDestPoolType);
        } else {
            if (pDestPoolType.equals(PoolTypeEn.PROD)) {
                //TODO:
                return null;
            }else{
                // TODO:
                // get tables from files

                // save tables in dev - pool
                return null;//ruleEditorBean.copyTables(tableIds, pPoolId, pPoolType, destPoolId, pDestPoolType);
            }
        }
    }

    @Override
    public RuleExchangeResult copyTables(List<Long> tableIds, long pPoolId, PoolTypeEn pPoolType, long destPoolId, PoolTypeEn pDestPoolType, RuleOverrideFlags doOverride, RuleImportCheckFlags doCheck) throws RuleEditorProcessException {

            if (isFromDb()|| pPoolType.equals(PoolTypeEn.DEV) && pDestPoolType.equals(PoolTypeEn.DEV)) {
                return ruleEditorBean.copyRules(tableIds, pPoolId, pPoolType, destPoolId, pDestPoolType, doOverride, doCheck);
            } else {



                if (pDestPoolType.equals(PoolTypeEn.PROD)) {
                    //TODO:
                    return null;

                } else {
                // TODO:
                // get tables from files
                // save tables in dev - pool
                return null;//return ruleEditorBean.copyRules(tableIds, pPoolId, pPoolType, destPoolId, pDestPoolType, doOverride, doCheck);
                }
            }
        
    }

    @Override
    public RuleExchangeResult importPoolFromXML(long pPoolId, PoolTypeEn pPoolType, String pXmlFileContent) throws RuleEditorProcessException {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.importPoolFromXML(pPoolId, pPoolType, pXmlFileContent);
        }
        //TODO:
        return null;

    }

    @Override
    public RuleExchangeResult importPoolFromXML(long pPoolId, PoolTypeEn pPoolType, String pXmlFileContent, List<String> RuleIdents4import, RuleOverrideFlags doOverride, RuleImportCheckFlags doCheck) throws RuleEditorProcessException {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.importPoolFromXML(pPoolId, pPoolType, pXmlFileContent, RuleIdents4import, doOverride, doCheck, true, 0);
        }
        //TODO:
        return null;

    }

    @Override
    public RuleExchangeResult importPoolFromXML(long pPoolId, PoolTypeEn pPoolType, String pXmlFileContent, RuleOverrideFlags pDoOverride, RuleImportCheckFlags pDoCheck) throws RuleEditorProcessException {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.importPoolFromXML(pPoolId, pPoolType, pXmlFileContent, pDoOverride, pDoCheck);
        }
        //TODO
        return null;

    }

    @Override
    public RuleExchangeResult importPoolFromXML(long pPoolId, PoolTypeEn pPoolType, 
            String pXmlFileContent, RuleOverrideFlags pDoOverride,
            RuleImportCheckFlags pDoCheck, boolean doTransferCheck
    ) throws RuleEditorProcessException {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.importPoolFromXML(pPoolId, pPoolType, pXmlFileContent, null, pDoOverride, pDoCheck, doTransferCheck, 0);
        }
        //TODO
        return null;

    }

    @Override
    public List<CrgRuleTypes> findAllRuleTypes(PoolTypeEn pType) {

        if (isFromDb() || pType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.findAllRuleTypes(pType);
        } else {
            return ruleEditorBeanXML.findAllRuleTypes(pType);
        }

    }

    @Override
    public String getRuleTypeNameForRule(long pRuleId, long pPoolId, PoolTypeEn pType) {

        if (isFromDb() || pType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.getRuleTypeNameForRule(pRuleId, pPoolId, pType);
        } else {
            return ruleEditorBeanXML.getRuleTypeNameForRule(pRuleId, pPoolId, pType);
        }

    }

    @Override
    public String getRuleTypeDisplayNameForRule(long pRuleId, long pPoolId, PoolTypeEn pPoolType) {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.getRuleTypeDisplayNameForRule(pRuleId, pPoolId, pPoolType);
        } else {
            return ruleEditorBeanXML.getRuleTypeDisplayNameForRule(pRuleId, pPoolId, pPoolType);
        }

    }

    @Override
    public void updateRuleTable(CrgRuleTables ruleTable, PoolTypeEn pPoolType) {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            ruleEditorBean.updateRuleTable(ruleTable, pPoolType);
        } else {
            //TODO:
        }

    }

    @Override
    public Long getRuleCountForRole(long pPoolId, PoolTypeEn pType, long pRoleId) {

        if (isFromDb() || pType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.getRuleCountForRole(pPoolId, pType, pRoleId);
        } else {
            return ruleEditorBeanXML.getRuleCountForRole(pPoolId, pType, pRoleId);
        }

    }

    @Override
    public List<CrgRules> getAllActiveRulesForRole(Long pRoleId, long pPoolId, PoolTypeEn pPoolType) {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.getAllActiveRulesForRole(pRoleId, pPoolId, pPoolType);
        } else {
            return ruleEditorBeanXML.getAllActiveRulesForRole(pRoleId, pPoolId, pPoolType);
        }

    }

    @Override
    public List<CrgRules> getAllAvailableRulesForRole(Long pRoleId, long pPoolId, PoolTypeEn pPoolType) {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.getAllAvailableRulesForRole(pRoleId, pPoolId, pPoolType);
        } else {
            return ruleEditorBeanXML.getAllAvailableRulesForRole(pRoleId, pPoolId, pPoolType);
        }

    }

    @Override
    public void deleteRules(List<Long> pRuleIds, long pPoolId, PoolTypeEn pPoolType) throws RuleEditorProcessException {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            ruleEditorBean.deleteRules(pRuleIds, pPoolId, pPoolType);
        } else {
            //TODO for PROD&&Filesystem
        }

    }

    @Override
    public void deleteRuleTables(long pPoolId, PoolTypeEn pPoolType, List<Long> tableIds) throws RuleEditorProcessException {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            ruleEditorBean.deleteRuleTables(pPoolId, pPoolType, tableIds);
        } else {
            //TODO for PROD&&Filesystem
        }
    }

    @Override
    public CrgRuleTypes createRuleType(PoolTypeEn pPoolType) throws RuleEditorProcessException {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.createRuleType(pPoolType);
        }
        //TODO for PROD&&Filesystem
        return null;
    }

    @Override
    public void deleteRuleTypes(List<Long> pTypesIds, PoolTypeEn pPoolType) throws RuleEditorProcessException {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            ruleEditorBean.deleteRuleTypes(pTypesIds, pPoolType);
        }
    }

    @Override
    public void deleteRuleType(long pTypeId, PoolTypeEn pPoolType) throws RuleEditorProcessException {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            ruleEditorBean.deleteRuleType(pTypeId, pPoolType);
        }

    }

    @Override
    public void updateRuleType(CrgRuleTypes pRuleType, PoolTypeEn pPoolType) throws RuleEditorProcessException {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            ruleEditorBean.updateRuleType(pRuleType, pPoolType);
        }

    }

    @Override
    public List<CrgRules> getAllRules(PoolTypeEn pPoolType) {

        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.getAllRules(pPoolType);
        } else {
            return ruleEditorBeanXML.getAllRules(pPoolType);
        }
    }

    @Override
    public void updateRole(long pRoleId, long pPoolId, PoolTypeEn pType, List<Long> pRuleIds) throws RuleEditorProcessException {

        if (isFromDb() || pType.equals(PoolTypeEn.DEV)) {
            ruleEditorBean.updateRole(pRoleId, pPoolId, pType, pRuleIds);
            return;
        } else {
            //TODO:
        }
        //       throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TransferRuleAnalyseResult analyseRule(@NotNull String pRule, @NotNull TCase pCase, PoolTypeEn pPoolType) {
        return analyseRule(pRule, pCase, null, pPoolType);
//        Objects.requireNonNull(pRule, "Rule can not be null");
//        Objects.requireNonNull(pCase, "Case can not be null");
//        TransferCase cse = new TransferCase();
//
//        //TransferCaseHelper.transformTCaseToTransferCase(pCase, true, 0);
//        if(grouperCommunication.fillGrouperRequest(pCase, cse, true, 0)){
//            Transfer4RuleAnalyse objToAnalyse = new Transfer4RuleAnalyse(cse, pRule);
//            try {
//           List<String> tableIds = RulesConverter.getRuleTablesFromRuleDefinition(pRule);
//            if(tableIds != null && !tableIds.isEmpty()){
//                Map<String, String> tables = ruleEditorBean.getRuleTables4IdList(tableIds, PoolTypeEn.DEV);
//            }
//                return ruleCheckService.checkRule4Case(objToAnalyse);
//            } catch (Exception ex) {
//                //todo: Better Exception??
//                Logger.getLogger(RuleEditorBean.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }else{
//             Logger.getLogger(RuleEditorBean.class.getName()).log(Level.WARNING, "could not fill transfer case");
//        }
//        return null;
    }

    @Override
    public TransferRuleAnalyseResult analyseRule(@NotNull String pRule, @NotNull TCase pCase, Map<String, String> pTables, PoolTypeEn pPoolType, List<TCase> pHistoryCases) {
        Objects.requireNonNull(pRule, "Rule can not be null");
        Objects.requireNonNull(pCase, "Case can not be null");
        TransferCase cse = new TransferCase();
        List<CaseValidationGroupErrList> errorList = grouperCommunication.fillGrouperRequest(pCase, pCase.getCurrentLocal(), cse, 0, null, null, new ArrayList<>());
        List <TransferCase> transferHistory = new ArrayList<>();
        if(pHistoryCases != null && !pHistoryCases.isEmpty()){
            for(TCase cs: pHistoryCases){
                TransferCase hisCase = new TransferCase();

                if(cse.getCaseNumber().equals(cs.getCsCaseNumber())){
                    continue;
                }
                List<CaseValidationGroupErrList>  dummy = grouperCommunication.fillGrouperRequest(cs, cs.getCurrentLocal(), hisCase, 0, null, null, new ArrayList<>());
                if(dummy.size() == 0){
                    transferHistory.add(hisCase);
                }
            }
        }
        Transfer4RuleAnalyse objToAnalyse = new Transfer4RuleAnalyse(cse, pRule, pTables, errorList, transferHistory);
        if (pTables != null && !pTables.isEmpty()) {
            objToAnalyse.setTableId2NameMapping(ruleEditorBean.getRuleTableNames4List(new ArrayList<>(pTables.keySet()), pPoolType));
        }
        try {

            TransferRuleAnalyseResult res = ruleCheckService.checkRule4Case(objToAnalyse);

            res.setCaseYear(res.getGroupResult() == null ? 0 : res.getGroupResult().getCaseYear());
            res.printErrorList();
            LOG.log(Level.INFO, res.getRuleResult() == null ? ("no rule result" + pRule) : res.getRuleResult().toString());
            return res;
        } catch (Exception ex) {
            //todo: Better Exception??
            LOG.log(Level.SEVERE, null, ex);
        }
        TransferRuleAnalyseResult res = new TransferRuleAnalyseResult();
        res.setCaseYear(cse.getAdmissionYear());
        res.setCaseValidationGroupErrList(errorList);
        res.printErrorList();
        return res;
    }

    @Override
    public List<CCase> getAllAnalyserCases(long cpxUserId) {
        List<CCase> list = analyserCaseDao.findAllCasesForUser(cpxUserId);
        return list;
    }

    @Override
    public TCase getHospitalCaseFromCaseCopy(CCase pAnalyserCase) {
//        analyserCaseDao.findById(pAnalyserCase.getId());
        long start = System.currentTimeMillis();
        byte[] content = analyserCaseDao.getCaseContent(pAnalyserCase.getId());//analyserCaseDao.merge(pAnalyserCase).getContent();
        if (content == null || content.length == 0) {
            LOG.log(Level.WARNING, "ccase with id " + pAnalyserCase.id + " has no content!");
            return null;
        }
        LOG.info("get cCase content in " + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        TCase cs;
        try {
            cs = new ObjectMapper().readValue(content, TCase.class);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot unserialize case from common case with id " + pAnalyserCase.id + " (case id is " + pAnalyserCase.getCaseId() + ")", ex);
            return null;
        }
        LOG.info("get tCase from cCase in " + (System.currentTimeMillis() - start));
        return cs;
    }

    @Override
    public TCase getHospitalCaseFromCaseCopy(long pAnalyserCaseId) {
        long start = System.currentTimeMillis();
        byte[] content = analyserCaseDao.getCaseContent(pAnalyserCaseId);//analyserCaseDao.merge(pAnalyserCase).getContent();
        if (content == null || content.length == 0) {
            LOG.log(Level.WARNING, "ccase with id " + pAnalyserCaseId + " has no content!");
            return null;
        }
        LOG.info("get cCase content in " + (System.currentTimeMillis() - start));
        //force utf-8 encoding, in test data there are different encodings for TLab data
        //conversion necessary to avoid errors in json parser 
        InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(content), StandardCharsets.UTF_8);
        start = System.currentTimeMillis();
        TCase cs;
        try {
            cs = new ObjectMapper().readValue(reader, TCase.class);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot unserialize case from common case with id " + pAnalyserCaseId + "", ex);
            return null;
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(RuleEditorBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        LOG.info("get tCase from cCase in " + (System.currentTimeMillis() - start));
        return cs;
    }

    @Override
    public Boolean deleteAnalyserCases(List<Long> pCases) {
        try {
            return analyserCaseDao.deleteCasesById(pCases);
        } catch (HibernateException ex) {
            LOG.severe("Could not delete Entries, Reason: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public Collection<String> getMatchesAnalyserCaseCategory(String pText) {
        return analyserCaseDao.getMatchesAnalyserCaseCategory(pText);
    }

    @Override
    public Boolean saveAnalyserCases(CCase pCase) {
        CCase cse = analyserCaseDao.findById(pCase.getId());//analyserCaseDao.merge(pCase);
        cse.setName(pCase.getName());
        return true;
    }

    @Override
    public Boolean updateAnalyserCaseContent(@NotNull Long pAnalyserCaseId, @NotNull TCase pHospCase) {
        pAnalyserCaseId = Objects.requireNonNull(pAnalyserCaseId, "AnalyserCaseId can not be null");
        pHospCase = Objects.requireNonNull(pHospCase, "HospCase can not be null");
        ObjectMapper objectMapper = new ObjectMapper();
        String serializedData;
        try {
            serializedData = objectMapper.writeValueAsString(pHospCase);
            //objectMapper.writeValue(new File("target/car.json"), cs);
        } catch (JsonProcessingException ex) {
            LOG.log(Level.SEVERE, "Cannot serialize case: " + pHospCase, ex);
            return false;
        }
        CCase cse = analyserCaseDao.findById(pAnalyserCaseId);
        if (cse == null) {
            LOG.severe("AnalyerCase with id " + pAnalyserCaseId + " not found!");
            return false;
        }
        final String charset = CpxSystemProperties.DEFAULT_ENCODING;
        try {
            cse.setContent(serializedData.getBytes(charset));
        } catch (UnsupportedEncodingException ex) {
            LOG.log(Level.SEVERE, "Cannot apply charset to getBytes: " + charset, ex);
            return false;
        }
        return true;
    }

    @Override
    public java.util.List<CCase> getAllAnalyserCasesForCurrentUser() {
        Long user = ClientManager.getCurrentCpxUserId();
        return analyserCaseDao.findAllCasesForUser(user);
    }

    @Override
    public TransferRuleAnalyseResult analyseRule(String pRule, TCase pCase, Map<String, String> pTables, PoolTypeEn pPoolType) {
        return analyseRule(pRule, pCase, pTables, pPoolType, null);
    }

    @Override
    public String getRuleTableComment(long pId, PoolTypeEn pPoolType) {
        if (isFromDb() || pPoolType.equals(PoolTypeEn.DEV)) {
            return ruleEditorBean.getRuleTableComment(pId, pPoolType);
        } else {
            return "";
        }
    }

    @Override
    public boolean containsAnyMessage(long pPoolId, PoolTypeEn pType) {
        return ruleEditorBean.containsPoolErrorMessages(pPoolId,pType);
    }

    @Override
    public boolean containsRuleMessage(long pPoolId, PoolTypeEn pType) {
        return ruleEditorBean.containsPoolRuleMessages(pPoolId,pType);
    }

    @Override
    public boolean containsRuleTableMessage(long pPoolId, PoolTypeEn pType) {
        return ruleEditorBean.containsPoolRuleTableMessages(pPoolId,pType);
    }
    
    @Override
    public byte[] getRuleMessage(long pRuleId, long pPoolId, PoolTypeEn pType){
        return ruleEditorBean.getRuleMessage(pRuleId, pPoolId, pType);
    }
    
    @Override
    public byte[] getRuleTableMessage(long pRuleTableId, long pPoolId, PoolTypeEn pType){
        return ruleEditorBean.getRuleTableMessage(pRuleTableId, pPoolId, pType);
    }
}
