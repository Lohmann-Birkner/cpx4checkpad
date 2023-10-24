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
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.commonDB.dao.Rules4DbInteractBean;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.server.commons.enums.RuleImportCheckFlags;
import de.lb.cpx.server.commons.enums.RuleOverrideFlags;
import de.lb.cpx.service.readrules.codeschecker.RulesCodeChecker;
import de.lb.cpx.service.rules.RulesExchangeHelper;
import de.lb.cpx.service.rules.RulesImportHelper;
import de.lb.cpx.service.rules.RulesTransferFactoryBean;
import de.lb.cpx.shared.rules.util.RuleValidationStatusEn;
import de.lb.cpx.shared.rules.util.RulesConverter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Bean to access data for the RuleEditor TODO:split bean in smaller ones
 *
 * @author wilde
 */
@Stateless
public class RuleEditorBean4DB {

    private static final Logger LOG = Logger.getLogger(RuleEditorBean4DB.class.getName());

    @EJB
    private Rules4DbInteractBean rules4DbInteractBean;

    @EJB
    private RulesTransferFactoryBean rulesTranserFactoryBean;
    
    @EJB 
    private RulesCodeChecker ruleCodeChecker;

//    @Override
    public List<CrgRulePools> getCrgRulePools(PoolTypeEn pType) {
        return rules4DbInteractBean.getCrgRulePoolsYearsDesc(pType);
    }

    public List<CrgRulePools> getCrgRulePools(PoolTypeEn pType, Long pCurrentUserId) {
        return rules4DbInteractBean.getCrgRulePoolsYearsDesc(pType, pCurrentUserId);
    }

//    @Override
    public List<CrgRulePools> getProdCrgRulePools() {
//        RuleTypeEn.STATE_NO.getTranslation().getValue();
//        Lang.get(RuleTypeEn.STATE_NO.getLangKey());
        return getCrgRulePools(PoolTypeEn.PROD);
    }

//    @Override
    public List<CrgRulePools> getDevCrgRulePools() {
        return getCrgRulePools(PoolTypeEn.DEV);
    }

//    @Override
    public Long savePool(CrgRulePools pPool, PoolTypeEn pType) throws RuleEditorProcessException {
        if (pType.equals(PoolTypeEn.DEV)) {
            return rules4DbInteractBean.saveNewPool(pPool, pType, ClientManager.getCurrentCpxUserId());
        } else {
            return rules4DbInteractBean.saveNewPool(pPool, pType);
        }
    }

    public Long findPoolId4poolNameAndDates(CrgRulePools pPool, PoolTypeEn pType) {
        if (pType.equals(PoolTypeEn.DEV)) {
            return rules4DbInteractBean.findPoolId4poolNameAndDates(pPool, pType, ClientManager.getCurrentCpxUserId());
        } else {
            return rules4DbInteractBean.findPoolId4poolNameAndDates(pPool, pType, null);
        }

    }

//    @Override
    public void updatePool(CrgRulePools pPool, PoolTypeEn pType) throws RuleEditorProcessException {
        rules4DbInteractBean.updatePool(pPool, pType);
//        try { // dev only to fill default data to pool items
//            pPool = rules4DbInteractBean.getRulePool2Id(pPool.getId(), pType);
//            pPool.getCrgRuleTableses().iterator().next().setCrgtMessage(new RuleTableMessageBuilder().setCodes("1-502.1,1-502.2,5-142.0,1-471.2*").setDescription("Regeltabellen sind nicht aktuell").setSeverity("error").setType("catalog").build("UTF-8"));
//            Iterator<CrgRules> it = pPool.getCrgRuleses().iterator();
//            it.next().setCrgrMessage(new RuleMessageBuilder().setTerm("Prozedur @ AOP_2021").setReason(MessageReasonEn.VALIDATION_CHECK_CATALOG_TRANSFER_TABLE).setDescription("Regelinhalt sind nicht aktuell").setSeverity("error").setType("catalog").add()
//                    .setTerm("Entlassungsgrund12 !=").setDescription("Entlassungsgrund unbekannt").setReason(MessageReasonEn.VALIDATION_NO_VALUE).setSeverity("error").setType("Stammdaten").build("UTF-8"));
////            it.next().setCrgrMessage(new RuleMessageBuilder().setTerm("Prozedur @ AOP_2021").setDescription("Regelinhalt sind nicht aktuell").setSeverity("error").setType("catalog").build("UTF-8"));
//        } catch (JsonProcessingException ex) {
//            Logger.getLogger(Rules4DbInteractBean.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(Rules4DbInteractBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

//    @Override
    public void updateDevPool(CrgRulePools pPool) throws RuleEditorProcessException {
        updatePool(pPool, PoolTypeEn.DEV);
    }

//    @Override
    public void updateProdPool(CrgRulePools pPool) throws RuleEditorProcessException {
        updatePool(pPool, PoolTypeEn.PROD);
    }

//    @Override
    public void deletePool(long pPoolId, PoolTypeEn pType) throws RuleEditorProcessException {
        rules4DbInteractBean.deletePool(pPoolId, pType);
    }

//    @Override
    public List<CrgRules> findRules(long pPoolId, PoolTypeEn pType) {
        long start = System.currentTimeMillis();
        List<CrgRules> rules = rules4DbInteractBean.findRulesForPool(pPoolId, pType);
        LOG.log(Level.INFO, "fetch list of rules ({0}) for pool {1} in {2} ms", new Object[]{rules != null ? rules.size() : "null", pPoolId, System.currentTimeMillis() - start});
        return rules;
    }

//    @Override
    public int findSizeOfRules(long pPoolId, PoolTypeEn pType) {

        return rules4DbInteractBean.countRules4PoolId(pPoolId, pType).intValue();
    }

    public int findSizeOfRuleTables2Pool(long pPoolId, PoolTypeEn pType) {

        return rules4DbInteractBean.countRuleTables4PoolId(pPoolId, pType).intValue();
    }

////    @Override 
//    public String saveRule(CrgRules rule, long poolId, PoolTypeEn poolType){
//        return rules4DbInteractBean.saveNewRule(rule, poolId, poolType);  
//    }
//    
//    @Override
    public List<CrgRuleTables> findRuleTablesForPool(long pPoolId, PoolTypeEn pType) {
        return rules4DbInteractBean.getRuleTables4Pool(pType, pPoolId);
    }

    public List<CrgRuleTables> findRuleTablesForPoolAndContentOrName(long pPoolId, PoolTypeEn pType, String pText) {
        String text = Objects.requireNonNullElse(pText, "");
        return rules4DbInteractBean.getRuleTables4YearAndContentOrName(pType, pPoolId, text);
    }

//    @Override
    public CrgRuleTables copyRuleTable(CrgRuleTables table) {
        return rules4DbInteractBean.copyRuleTable(table);
    }

//    @Override
    public List<CrgRuleTables> findAllRuleTables(PoolTypeEn pType) {
        return rules4DbInteractBean.getAllRuleTables4PoolType(pType);
    }

//    @Override
    public CrgRuleTables findRuleTable(String pTableId, PoolTypeEn poolType, long pPoolId) {
        return rules4DbInteractBean.findRuleTable4StringId(pTableId, poolType, pPoolId);
    }

//    @Override
    public CrgRuleTables createRuleTable(long pPoolId, PoolTypeEn pPoolType) throws RuleEditorProcessException {
        return rules4DbInteractBean.createRuleTable(pPoolId, pPoolType);
    }

//    @Override
    public void updateRule(CrgRules pRule, long pPoolId, PoolTypeEn pPoolType) throws RuleEditorProcessException {
        if(pRule.getCrgrDefinition()!= null && pRule.getCrgrDefinition().length != 0){
            checkRule4Year(pPoolId, pRule, pPoolType);
        }
        rules4DbInteractBean.updateRule(pRule, pPoolId, pPoolType);
    }
    
    private void checkRule4Year(long pPoolId, CrgRules pRule, PoolTypeEn pPoolType) throws RuleEditorProcessException{
                    CrgRulePools pool = rules4DbInteractBean.getRulePool2Id(pPoolId, pPoolType);

            if (pool == null) {
                throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE, "Der Pool für id " + String.valueOf(pPoolId) + " ist nicht vorhanden");
            }
            try{
                CRGRule cpRule = RulesConverter.getCpRuleFromRuleDefinition(pRule);
                List<Long> tabIds = RulesConverter.getRuleTableIds(cpRule);
                List <CrgRuleTables> tables = tabIds.isEmpty()?null:rules4DbInteractBean.findTables4PoolAndIdList(pPoolId, pPoolType, tabIds);

                ruleCodeChecker.checkRule4Year(pool.getCrgplPoolYear(), pRule, tables);

            }catch(Exception ex){
                throw RuleEditorProcessException.createRuleEditorProcessException(pRule, Level.SEVERE, "Fehler bei der Validierung der Regel  " + pRule.getCrgrNumber() );
            }

    }

//    @Override
    public void updateRuleTable(long pPoolId, PoolTypeEn pPoolType, CrgRuleTables pTable)  throws RuleEditorProcessException{

        if(pTable.getCrgtCategory() != null && !pTable.getCrgtContent().isEmpty()){
            CrgRulePools pool = rules4DbInteractBean.getRulePool2Id(pPoolId, pPoolType);
            if (pool == null) {
                throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE, "Der Pool für id " + String.valueOf(pPoolId) + " ist nicht vorhanden");
            }

            try{
                ruleCodeChecker.checkRuleTable4Year(pool.getCrgplPoolYear(), pTable);
            }catch(Exception ex){
                throw RuleEditorProcessException.createRuleEditorProcessException(pTable, Level.SEVERE, "Fehler bei der Validierung der Tabelle  " + pTable.getCrgtTableName() );
            }
        }
        rules4DbInteractBean.updateRuleTable(pPoolId, pPoolType, pTable);
        try{

                ruleCodeChecker.validateRules4Table(pPoolId, pPoolType, pTable.getId());
            
        }catch(Exception ex){
            throw RuleEditorProcessException.createRuleEditorProcessException(pTable, Level.SEVERE, "Fehler bei der Validierung der zusammenhängenden regeln der tabelle  " + pTable.getCrgtTableName() );
            
        }
    }

    /**
     * @param pRuleId rule database id to copy
     * @param pPoolId pool database id
     * @param pPoolType pooltype to determine if prod or dev
     * @return copied rule
     * @throws de.lb.cpx.server.rule.services.RuleEditorProcessException error
     */
//    @Override
    public CrgRules copyRule(long pRuleId, long pPoolId, PoolTypeEn pPoolType) throws RuleEditorProcessException {
        return rules4DbInteractBean.copyRule(pRuleId, pPoolId, pPoolType);
    }

//    @Override
    public void deleteRule(long pRuleId, long pPoolId, PoolTypeEn pPoolType) throws RuleEditorProcessException {
        rules4DbInteractBean.deleteRule(pRuleId, pPoolId, pPoolType);
    }

//    @Override
    public byte[] findRuleDefinition(long pRuleId, long pPoolId, PoolTypeEn pPoolType) {
        return rules4DbInteractBean.findRuleDefinition(pRuleId, pPoolId, pPoolType);
    }

//    @Override
    public List<CdbUserRoles> findUserRoles2Rule(long pRuleId, PoolTypeEn pPoolType) {
        return rules4DbInteractBean.findRoles2Rule(pRuleId, pPoolType);
    }

//    @Override
    public String getRuleTableContent(long id, PoolTypeEn poolType) {
        return rules4DbInteractBean.getContent2RuleTable(id, poolType);
    }

//    @Override
    public List<CrgRules> findRules4Table(long pPoolId, PoolTypeEn pPoolType, long pTableId) {
        return rules4DbInteractBean.findRulesForTable(pPoolId, pPoolType, pTableId);
    }

//    @Override
    public void deleteRuleTable(long pPoolId, PoolTypeEn pPoolType, CrgRuleTables pToDelete) throws RuleEditorProcessException {
//        if(rules4DbInteractBean.hasTableRuleRelation(pPoolId,pPoolType,pToDelete)){
//            throw RuleEditorProcessException.createRuleEditorProcessException(pToDelete, Level.SEVERE, "Regeltabelle kann nicht gelöscht werden, da noch Regeln auf sie verweisen!");
//        }
        rules4DbInteractBean.deleteRuleTable(pPoolId, pPoolType, pToDelete);
    }

//    @Override
    public String exportPoolAsXML(long pPoolId, PoolTypeEn pPoolType) throws RuleEditorProcessException {
        return exportPoolAsXML(pPoolId, pPoolType, null);
    }

//    @Override
    public String exportPoolAsXML(long pPoolId, PoolTypeEn pPoolType, List<Long> ruleIds) throws RuleEditorProcessException {
        List<CrgRules> rules4Pool = rules4DbInteractBean.findRulesForPool(pPoolId, pPoolType, ruleIds);
        Map<String, CrgRuleTables> tables = rules4DbInteractBean.getId2Tables(pPoolId, pPoolType);
        CrgRulePools pool = rules4DbInteractBean.getRulePool2Id(pPoolId, pPoolType);
        List<Long> userRoles = rules4DbInteractBean.getRoleIds4CurrentUser();
        Long[] roleIds = new Long[userRoles == null?0:userRoles.size()];
        if(userRoles != null){
            roleIds = userRoles.toArray(roleIds);
        }
        if (pool == null) {
            throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE, "Der Pool für id " + String.valueOf(pPoolId) + " und pool type " + pPoolType.getDescription() + " ist nicht vorhanden");
        }
        
        Map<Long, CrgRuleTypes> usedRuleTypes = new HashMap<>();
        try {
            List<CrgRuleTypes> usedTypes = new ArrayList<>();
            List<CrgRuleTables> usedTables = new ArrayList<>();

            List<CrgRules> retRules = new ArrayList<>();
            for (CrgRules rule : rules4Pool) {
                CrgRuleTypes type = rule.getCrgRuleTypes();
                CrgRules newRule = RulesConverter.getRule4Export(rule, pPoolType, type, tables, usedTables, roleIds);
                newRule.setCrgRuleTypes(type);
                retRules.add(newRule);
                usedRuleTypes.put(type.getId(), type);
            }
            usedTypes = new ArrayList<>(usedRuleTypes.values());
//            for(CrgRuleTables tab: usedTables){
//                LOG.info("Table:" + tab.getCrgtTableName());
//            }
            RulesExchangeHelper helper = rulesTranserFactoryBean.doRulesExportInFile(retRules, usedTables, usedTypes, pool.getCrgplPoolYear());
            return helper.getXmlString4Export();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error on getting rules from pool " + pPoolId, ex);
            throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE, " Error on getting rules from pool " + pPoolId + ex.getMessage());
        }
    }

//    @Override
    public CrgRules createRule(long pPoolId, PoolTypeEn pPoolType) throws RuleEditorProcessException {
//        long start = System.currentTimeMillis();
        CrgRules rule = rules4DbInteractBean.createRule(pPoolId, pPoolType);
//        LOG.info("create new Rule in " + (System.currentTimeMillis()-start) + " ms");
// add message
       checkRule4Year(pPoolId, rule, pPoolType);

        CrgRules rule2 = rules4DbInteractBean.findRuleByIdWithoutDefinition(pPoolId, rule.getId(), pPoolType);
//        LOG.info("load Rule in " + (System.currentTimeMillis()-start) + " ms");
        return rule2;
    }

//    @Override
    public CrgRuleTables saveRuleTable(long pPoolId, PoolTypeEn poolType, CrgRuleTables pTables) throws RuleEditorProcessException {
        return rules4DbInteractBean.saveNewRuleTable(pPoolId, poolType, pTables);
    }

//    @Override
    public RuleExchangeResult copyRules(List<Long> ruleIds, long poolId, PoolTypeEn pPoolType, long destPoolId, PoolTypeEn pDestPoolType) throws RuleEditorProcessException {
        return copyRules(ruleIds, poolId, pPoolType, destPoolId, pDestPoolType, RuleOverrideFlags.SAVE_OLD, RuleImportCheckFlags.CHECK_4_COLLISIONS);
    }

//    @Override
    public RuleExchangeResult copyRules(List<Long> ruleIds, long pPoolId, PoolTypeEn pPoolType, long destPoolId, PoolTypeEn pDestPoolType, RuleOverrideFlags doOverride, RuleImportCheckFlags doCheck) throws RuleEditorProcessException {
        CrgRulePools pool = rules4DbInteractBean.getRulePool2Id(pPoolId, pPoolType);

        if (pool == null) {
            throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE, "Der Pool für id " + String.valueOf(pPoolId) + " und pool type " + pPoolType.getDescription() + " ist nicht vorhanden");
        }

        CrgRulePools destPool = rules4DbInteractBean.getRulePool2Id(destPoolId, pDestPoolType);
        if (destPool == null) {
            throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE, "Der Pool für id " + String.valueOf(destPoolId) + " und pool type " + pDestPoolType.getDescription() + " ist nicht vorhanden");
        }
//        long start = System.currentTimeMillis();
        String RulesAsXml = this.exportPoolAsXML(pPoolId, pPoolType, ruleIds);
//        LOG.info("create xml from selected rules " + (System.currentTimeMillis()-start));
        RuleExchangeResult result = importPoolFromXML(destPoolId, pDestPoolType, RulesAsXml, null, doOverride, doCheck, true, pool.getCrgplPoolYear());
        if(pPoolType.equals(PoolTypeEn.PROD) && (result.getErrors() == null || result.getErrors().isEmpty())
                && result.getSuccessRuleIds() != null 
                && result.getSuccessRuleIds().size() == ruleIds.size() ){
            // by source prod pool we do not copy rules, we move them. So after successful copiing we delete rules from prod pool
            deleteRules(ruleIds, pool.getId( ),pPoolType);
        }
        return result;
    }

//    @Override
    public RuleExchangeResult copyTables(List<Long> tableIds, long poolId, PoolTypeEn pPoolType, long destPoolId, PoolTypeEn pDestPoolType) throws RuleEditorProcessException {
        return copyTables(tableIds, poolId, pPoolType, destPoolId, pDestPoolType, RuleOverrideFlags.SAVE_OLD, RuleImportCheckFlags.CHECK_4_COLLISIONS);
    }

//    @Override
    public RuleExchangeResult copyTables(List<Long> tableIds, long pPoolId, PoolTypeEn pPoolType, long destPoolId, PoolTypeEn pDestPoolType, RuleOverrideFlags doOverride, RuleImportCheckFlags doCheck) throws RuleEditorProcessException {
        CrgRulePools pool = rules4DbInteractBean.getRulePool2Id(pPoolId, pPoolType);

        if (pool == null) {
            throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE, "Der Pool für id " + String.valueOf(pPoolId) + " und pool type " + pPoolType.getDescription() + " ist nicht vorhanden");
        }

        CrgRulePools destPool = rules4DbInteractBean.getRulePool2Id(destPoolId, pDestPoolType);
        if (destPool == null) {
            throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE, "Der Pool für id " + String.valueOf(destPoolId) + " und pool type " + pDestPoolType.getDescription() + " ist nicht vorhanden");
        }

        RuleExchangeResult result = new RuleExchangeResult();
// get Tables to copy
        List<CrgRuleTables> srcTables = rules4DbInteractBean.findTables4PoolAndIdList(pPoolId, pPoolType, tableIds);
        if (srcTables == null || srcTables.isEmpty()) {
            throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE, "Es wurden keine Tabellen zu der vorgegebenen Id - Liste gefunden");

        }
        rules4DbInteractBean.importRuleTables(destPoolId, srcTables, pDestPoolType, result, doOverride, doCheck);
        return result;

    }

//    @Override
    public RuleExchangeResult importPoolFromXML(long pPoolId, PoolTypeEn pPoolType, String pXmlFileContent) throws RuleEditorProcessException {
        return importPoolFromXML(pPoolId, pPoolType, pXmlFileContent, null, RuleOverrideFlags.SAVE_OLD, RuleImportCheckFlags.CHECK_4_COLLISIONS, false, 0);
    }

    public RuleExchangeResult importPoolFromXML(long pPoolId, PoolTypeEn pPoolType, String pXmlFileContent, RuleOverrideFlags pDoOverride, RuleImportCheckFlags pDoCheck) throws RuleEditorProcessException {
        return importPoolFromXML(pPoolId, pPoolType, pXmlFileContent, null, pDoOverride, pDoCheck, false, 0);
    }
    


//    @Override
    public RuleExchangeResult importPoolFromXML(long pPoolId, PoolTypeEn pPoolType, 
            String pXmlFileContent, List<String> RuleIdents4import, RuleOverrideFlags doOverride, 
            RuleImportCheckFlags doCheck, boolean doTransferCheck, int pSrcYear
    ) throws RuleEditorProcessException {
        RuleExchangeResult result = new RuleExchangeResult();
        CrgRulePools pool = rules4DbInteractBean.getRulePool2Id(pPoolId, pPoolType);

        if (pool == null) {
            throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE, "Der Pool für id " + String.valueOf(pPoolId) + " ist nicht vorhanden");
        }
        RulesImportHelper helper = null;
//        long start = System.currentTimeMillis();
        try {
            helper = rulesTranserFactoryBean.doRuleImportFromFile(pool.getCrgplIdentifier(), 
                    pool.getCrgplPoolYear(), null, pXmlFileContent, null, null, pSrcYear, true
            );
//            LOG.info("import from String into CP klasses " + (System.currentTimeMillis()-start));  

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, " Fehler bei der Auswertung des Import Strings", ex);
            throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE, "Fehler beim Lesen der Regeldatei");

        }
        if (helper != null) {

// the helper has all rules, tables and type in Checkpoin format            
// check and save types 
            Map<String, CrgRuleTypes> name2type = rules4DbInteractBean.importRuleTypes(helper.getRuleTypesAsCpx(pPoolType), pPoolType, result, doOverride, doCheck);
//////            LOG.info("after save rule types " + (System.currentTimeMillis()-start));
// check and save tables
            Map<String, CrgRuleTables> name2table = rules4DbInteractBean.importRuleTables(pPoolId, helper.getRuleTablesAsCpx(pPoolType), pPoolType, result, doOverride, doCheck);
//            LOG.info("after save rule tables " + (System.currentTimeMillis()-start));
//            Set<String> tabNames = name2table.keySet();
//            for (String name : tabNames) {
////                LOG.info("tabName =" + name + " tableid =" + name2table.get(name).getId());
//            }
            List<CrgRuleTypes> newRuleTypes = helper.checkRuleTypes(pPoolType, name2type);
            if (!newRuleTypes.isEmpty()) {
// save new types in DB before rules check happens            
                for (CrgRuleTypes type : newRuleTypes) {
                    rules4DbInteractBean.saveRuleType(type, pPoolType);
                    name2type.put(type.getCrgtShortText(), type);
                }
            }
// check and save rules
            result = rules4DbInteractBean.importRules(pPoolId, helper.getRulesAsCpx(pPoolType, name2type, name2table, result.getErrors()), pPoolType, result, doOverride, doCheck);
//           LOG.info("after save rules " + (System.currentTimeMillis()-start));
//           if(!result.getErrors().isEmpty()){
//               LOG.log(Level.INFO, "errors:");
//               for(RuleExchangeError err: result.getErrors()){
//                   LOG.log(Level.INFO, err.toString());
//               }
//           }else{
//               LOG.log(Level.INFO, "success");
//           }
            return result;
        }else{
            result.getErrors().add(new RuleExchangeError(0, RuleValidationStatusEn.NO_RULES_4_IMPORT_FOUND, pPoolType, ""));
            return result;
        }
////        return null;
    }

//    @Override
    public List<CrgRuleTypes> findAllRuleTypes(PoolTypeEn pType) {
        return rules4DbInteractBean.getRuleTypesAsList(pType);
    }

//    @Override
    public String getRuleTypeNameForRule(long pRuleId, long pPoolId, PoolTypeEn poolType) {
        return rules4DbInteractBean.getTypeNameForRule(pRuleId, pPoolId, poolType);
    }

//    @Override
    public String getRuleTypeDisplayNameForRule(long pRuleId, long pPoolId, PoolTypeEn poolType) {
        return rules4DbInteractBean.getTypeDisplayNameForRule(pRuleId, pPoolId, poolType);
    }

//    @Override
    public void updateRuleTable(CrgRuleTables pRuleTable, PoolTypeEn pType) {
        rules4DbInteractBean.updateRuleTable(pRuleTable, pType);
    }

//    @Override
    public List<CrgRules> getAllRules(PoolTypeEn poolTypeEn) {
//        return rules4DbInteractBean.getAllRules(poolTypeEn);
        return rules4DbInteractBean.getAllRulesExcluded(poolTypeEn);
    }

//    @Override
    public Long getRuleCountForRole(long pPoolId, PoolTypeEn pType, long pRoleId) {
        return rules4DbInteractBean.getRuleCountForRole(pPoolId, pType, pRoleId);
    }

//    @Override
    public List<CrgRules> getAllAvailableRulesForRole(Long pRoleId, long pPoolId, PoolTypeEn pPoolType) {
        return rules4DbInteractBean.findAllRulesForPoolAndRole(pPoolId, pPoolType, pRoleId, false);
    }

//    @Override
    public List<CrgRules> getAllActiveRulesForRole(Long pRoleId, long pPoolId, PoolTypeEn pPoolType) {
        return rules4DbInteractBean.findAllRulesForPoolAndRole(pPoolId, pPoolType, pRoleId, true);
    }
//    @Override

    public void deleteRules(List<Long> pRuleIds, long pPoolId, PoolTypeEn poolType) throws RuleEditorProcessException {
        rules4DbInteractBean.deleteRules(pRuleIds, poolType);
    }

//    @Override
    public void deleteRuleTables(long pPoolId, PoolTypeEn poolType, List<Long> tableIds) throws RuleEditorProcessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    @Override
    public CrgRuleTypes createRuleType(PoolTypeEn pPoolType) throws RuleEditorProcessException {
        return rules4DbInteractBean.createAndPersistRuleType(pPoolType, "Neu", "Neuer Regeltyp");//
    }

//    @Override
    public void deleteRuleTypes(List<Long> pTypesIds, PoolTypeEn pPoolType) throws RuleEditorProcessException {
        rules4DbInteractBean.deleteRuleTypes(pTypesIds, pPoolType);
    }

//    @Override
    public void deleteRuleType(long pTypeId, PoolTypeEn pPoolType) throws RuleEditorProcessException {
        rules4DbInteractBean.deleteRuleType(pTypeId, pPoolType);
    }

//    @Override
    public void updateRuleType(CrgRuleTypes pRuleType, PoolTypeEn poolType) throws RuleEditorProcessException {
        rules4DbInteractBean.updateRuleType(pRuleType, poolType);
    }

    public void updateRole(long pRoleId, long pPoolId, PoolTypeEn pType, List<Long> pRuleIds) throws RuleEditorProcessException {
        rules4DbInteractBean.updateRole(pRoleId, pPoolId, pType, pRuleIds);
    }

    /**
     *
     * @param tableIds table ids
     * @param pType pool type
     * @return rule table names
     * @throws RuleEditorProcessException error
     */
    public Map<String, String> getRuleTableNames4List(List<String> tableIds, PoolTypeEn pType) throws RuleEditorProcessException {
        return rules4DbInteractBean.getRuleTableNames4List(tableIds, pType);
    }

    public long getRelatedRulesCount4Table(long pPoolId, PoolTypeEn pPoolType, CrgRuleTables pTable) {
        return rules4DbInteractBean.sizeOfTableRuleRelation(pPoolId, pPoolType, pTable);
    }
    
    public long getRelatedRulesCount4Table(long pPoolId, PoolTypeEn pPoolType, List<CrgRuleTables> pTables) {
        return rules4DbInteractBean.sizeOfTableRuleRelation(pPoolId, pPoolType, pTables);
    }
    
    public long getSizeOfTypeRuleRelation(long pTypeId, PoolTypeEn pPoolType) {
        return rules4DbInteractBean.sizeOfTypeRuleRelation(pTypeId, pPoolType);
    }

    public String getRuleTableComment(long pId, PoolTypeEn pPoolType) {
         return rules4DbInteractBean.getComment2RuleTable(pId, pPoolType);
    }

    public boolean containsPoolErrorMessages(long pPoolId, PoolTypeEn pType) {
        return rules4DbInteractBean.containsPoolErrorMessages(pPoolId,pType);
    }

    public boolean containsPoolRuleMessages(long pPoolId, PoolTypeEn pType) {
        return rules4DbInteractBean.containsPoolRuleMessages(pPoolId,pType);
    }

    public boolean containsPoolRuleTableMessages(long pPoolId, PoolTypeEn pType) {
        return rules4DbInteractBean.containsPoolRuleTableMessages(pPoolId,pType);
    }

    public byte[] getRuleMessage(long pRuleId, long pPoolId, PoolTypeEn pType) {
        return rules4DbInteractBean.getRuleMessage(pRuleId,pPoolId,pType);
    }
    
    public byte[] getRuleTableMessage(long pRuleTableId, long pPoolId, PoolTypeEn pType) {
        return rules4DbInteractBean.getRuleTableMessage(pRuleTableId,pPoolId,pType);
    }
}
