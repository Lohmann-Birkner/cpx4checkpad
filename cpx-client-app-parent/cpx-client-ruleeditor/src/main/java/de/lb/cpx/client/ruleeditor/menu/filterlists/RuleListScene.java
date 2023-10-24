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
package de.lb.cpx.client.ruleeditor.menu.filterlists;

import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import de.lb.cpx.client.ruleeditor.menu.PoolOverviewScene;
import de.lb.cpx.client.ruleeditor.menu.filterlists.model.CdbUserRolesItem;
import de.lb.cpx.ruleviewer.cache.RuleMetaDataCache;
import de.lb.cpx.ruleviewer.util.PoolTypeHelper;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePoolsDev;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.server.commons.enums.RuleImportCheckFlags;
import de.lb.cpx.server.commons.enums.RuleOverrideFlags;
import de.lb.cpx.server.rule.services.RuleEditorBeanRemote;
import de.lb.cpx.server.rule.services.RuleEditorProcessException;
import de.lb.cpx.server.rule.services.RuleExchangeError;
import de.lb.cpx.server.rule.services.RuleExchangeResult;
import de.lb.cpx.server.rule.services.RuleLockBeanRemote;
import de.lb.cpx.service.ejb.TransferCatalogBeanRemote;
import de.lb.cpx.shared.json.RuleTableMessage;
import de.lb.cpx.shared.json.RuleTableMessageReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javax.naming.NamingException;
import javax.validation.constraints.NotNull;

/**
 * Filterlist impl. of the rule list of an pool
 *
 * @author wilde
 */
public class RuleListScene extends CpxScene {

    private RuleEditorBeanRemote ruleEditorBean;
    private RuleLockBeanRemote ruleLockBean;
    private TransferCatalogBeanRemote transferCatalogBean;

    public static final String UPDATE_POOL_DATA = "update-pool-data";
    private static final String BEAN_NOT_NULL = "Bean can not be null";
    private static final String POOL_NOT_NULL = "Pool can not be null";

    public RuleListScene(CrgRulePools pPool) throws IOException {
        super(CpxFXMLLoader.getLoader(RuleListFXMLController.class));
        initBeans();
        setPool(pPool);
        getController().afterInitialisingScene();
    }

    private ObjectProperty<CrgRulePools> poolProperty;

    public ObjectProperty<CrgRulePools> poolProperty() {
        if (poolProperty == null) {
            poolProperty = new SimpleObjectProperty<>();
        }
        return poolProperty;
    }

    public CrgRulePools getPool() {
        return poolProperty().get();
    }

    public void setPool(CrgRulePools pPool) {
        poolProperty().setValue(pPool);
    }

    public List<CrgRules> loadRules(int pStartIndex, int pEndIndex) {
        if (ruleEditorBean == null) {
            LOG.warning("can not load rules, can not connect to ruleEditorBean");
            return new ArrayList<>();
        }
        if (getPool() == null) {
            LOG.warning("can not load rules, pool is null");
            return new ArrayList<>();
        }
        List<CrgRules> rules = ruleEditorBean.findRules(getPool().getId(), PoolTypeHelper.getPoolType(getPool()));
        return rules;
    }
    public PoolTypeEn getPoolType(){
        return PoolTypeHelper.getPoolType(getPool());
    }
    private static final Logger LOG = Logger.getLogger(RuleListScene.class.getName());

    private void initBeans() {
        try {
            ruleEditorBean = Session.instance().getEjbConnector().connectRuleEditorBean().getWithEx();
            ruleLockBean = Session.instance().getEjbConnector().connectRuleLockBean().getWithEx();
            transferCatalogBean = Session.instance().getEjbConnector().connectTransferCatalogBean().getWithEx();
        } catch (NamingException ex) {
            Logger.getLogger(PoolOverviewScene.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public String saveRule(CrgRules rule) {
//        if(ruleEditorBean == null){
//            return null;
//        }
//        return ruleEditorBean.saveRule(rule,getPool().getId(),PoolTypeHelper.getPoolType(getPool()));
//    }
    public CrgRules createRule() {
        if (ruleEditorBean == null) {
            return null;
        }
        try {
            long start = System.currentTimeMillis();
            long poolId = getPool().getId();
            LOG.info("get PoolID " + (System.currentTimeMillis() - start));
            PoolTypeEn type = PoolTypeHelper.getPoolType(getPool());
            LOG.info("get Pooltype " + (System.currentTimeMillis() - start));
            CrgRules rule = ruleEditorBean.createRule(poolId, type);
            LOG.info("get created Rule From Server in " + (System.currentTimeMillis() - start));
            rule.setCrgrDefinition(ruleEditorBean.findRuleDefinition(rule.getId(), poolId, type));
            LOG.info("load definition in " + (System.currentTimeMillis() - start));
            return rule;
        } catch (RuleEditorProcessException ex) {
            MainApp.showErrorMessageDialog("Die Regel konnte nicht angelegt werden!");
            Logger.getLogger(RuleListScene.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void deleteRule(CrgRules pRule) {
        if (ruleEditorBean == null) {
            return;
        }
        try {
            ruleEditorBean.deleteRule(pRule.getId(), getPool().getId(), PoolTypeHelper.getPoolType(getPool()));
        } catch (RuleEditorProcessException ex) {
            MainApp.showErrorMessageDialog(" Beim Löschen der Regel " + pRule.getCrgrIdentifier() + " ist ein Fehler aufgetretten");
            Logger.getLogger(RuleListScene.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteRules(List<CrgRules> pRules) throws RuleEditorProcessException {
        if (ruleEditorBean == null) {
            return;
        }
//        try {
        ruleEditorBean.deleteRules(pRules.stream().map((t) -> {
            return t.getId();
        }).collect(Collectors.toList()), getPool().getId(), PoolTypeHelper.getPoolType(getPool()));
//        } catch (RuleEditorProcessException ex) {
//            MainApp.showErrorMessageDialog(" Beim Löschen der Regel " + pRules.stream().map((t) -> {
//                return t.getCrgrNumber(); //To change body of generated lambdas, choose Tools | Templates.
//            }).collect(Collectors.joining(",")) + " ist ein Fehler aufgetretten");
//            Logger.getLogger(RuleListScene.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public CrgRules copyRule(CrgRules pRule) {
        if (ruleEditorBean == null) {
            return null;
        }
        try {
            return ruleEditorBean.copyRule(pRule.getId(), getPool().getId(), PoolTypeHelper.getPoolType(getPool()));
        } catch (RuleEditorProcessException ex) {
            MainApp.showErrorMessageDialog(" Beim Kopieren der Regel " + pRule.getCrgrIdentifier() + " ist ein Fehler aufgetretten");
            Logger.getLogger(RuleListScene.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean clearLocks() {
        if (ruleLockBean == null) {
            return false;
        }
        if (getPool() == null) {
            return false;
        }
        ruleLockBean.clearLocksInPool(getPool().getId());
        return true;
    }

    public List<CrgRuleTables> findRuleTables() {
//        if(ruleEditorBean == null){
//            return new ArrayList<>();
//        }
        if (getPool() == null) {
            return new ArrayList<>();
        }
        return RuleMetaDataCache.instance().getTablesForPool(getPool());//ruleEditorBean.findRuleTablesForPool(getPool().getId(), PoolTypeHelper.getPoolType(getPool()));
    }

    public List<CrgRuleTables> findRuleTables(String pText) {
//        if(ruleEditorBean == null){
//            return new ArrayList<>();
//        }
        String text = Objects.requireNonNullElse(pText, "");
        if (getPool() == null) {
            return new ArrayList<>();
        }

        return ruleEditorBean.findRuleTablesForPoolAndContentOrName(getPool().getId(), PoolTypeHelper.getPoolType(getPool()), text);
    }

    public String getRuleTableContent(CrgRuleTables newValue) {
        if (ruleEditorBean == null) {
            return "";
        }
        if (getPool() == null) {
            return "";
        }
        return ruleEditorBean.getRuleTableContent(newValue.getId(), PoolTypeHelper.getPoolType(getPool()));
    }
   public String getRuleTableComment(CrgRuleTables newValue) {
        if (ruleEditorBean == null) {
            return "";
        }
        if (getPool() == null) {
            return "";
        }
        return ruleEditorBean.getRuleTableComment(newValue.getId(), PoolTypeHelper.getPoolType(getPool()));
    }

    public List<CrgRules> getRulesForTable(CrgRuleTables newValue) {
        if (newValue == null) {
            return new ArrayList<>();
        }
        if (ruleEditorBean == null) {
            return new ArrayList<>();
        }
        if (getPool() == null) {
            return new ArrayList<>();
        }
        return ruleEditorBean.findRules4Table(getPool().getId(), PoolTypeHelper.getPoolType(getPool()), newValue.getId());
    }

    public CrgRuleTables createTable() {
        if (ruleEditorBean == null) {
            return null;
        }
        if (getPool() == null) {
            return null;
        }

        try {
            return ruleEditorBean.createRuleTable(getPool().getId(), PoolTypeHelper.getPoolType(getPool()));
        } catch (RuleEditorProcessException ex) {
            MainApp.showErrorMessageDialog(ex, "Beim anlegen einer neuen Regeltabelle ist ein Fehler aufgetretten!\n" + ex.getMessage());
        }
        return null;
    }

    public CrgRuleTables copyTable(CrgRuleTables pTable) {
        if (ruleEditorBean == null) {
            return null;
        }
        return ruleEditorBean.copyRuleTable(pTable);
    }

    public boolean deleteTable(CrgRuleTables toDelete) {
        if (ruleEditorBean == null) {
            return false;
        }
        try {
            ruleEditorBean.deleteRuleTable(getPool().getId(), PoolTypeHelper.getPoolType(getPool()), toDelete);
            return true;
        } catch (RuleEditorProcessException ex) {
            Logger.getLogger(RuleListScene.class.getName()).log(Level.SEVERE, null, ex);
            MainApp.showErrorMessageDialog(ex);
            return false;
        }
    }
    
    public long getRelatedRulesCount4Table(List<CrgRuleTables> pTables) {
        if (ruleEditorBean == null) {
            LOG.warning("bean is null");
            return -1;
        }
        try {
            return ruleEditorBean.getRelatedRulesCount4Table(getPool().getId(), PoolTypeHelper.getPoolType(getPool()), pTables);

        } catch (RuleEditorProcessException ex) {
            Logger.getLogger(RuleListScene.class.getName()).log(Level.SEVERE, null, ex);
            MainApp.showErrorMessageDialog(ex);
            return -1;
        }
    }
    
    public long getRelatedRulesCount4Table(CrgRuleTables pTable) {
        if (ruleEditorBean == null) {
            LOG.warning("bean is null");
            return -1;
        }
        try {
            return ruleEditorBean.getRelatedRulesCount4Table(getPool().getId(), PoolTypeHelper.getPoolType(getPool()), pTable);

        } catch (RuleEditorProcessException ex) {
            Logger.getLogger(RuleListScene.class.getName()).log(Level.SEVERE, null, ex);
            MainApp.showErrorMessageDialog(ex);
            return -1;
        }
    }

    public String getPoolAsXml() {
        if (ruleEditorBean == null) {
            return null;
        }
        if (getPool() == null) {
            return null;
        }
        try {
            return ruleEditorBean.exportPoolAsXML(getPool().getId(), PoolTypeHelper.getPoolType(getPool()));
        } catch (RuleEditorProcessException ex) {
            Logger.getLogger(RuleListScene.class.getName()).log(Level.SEVERE, null, ex);
            MainApp.showErrorMessageDialog(ex);
        }
        return null;
    }

    public List<RuleExchangeError> importRules(String content, RuleImportCheckFlags pCheckFlag, RuleOverrideFlags pOverrideFlag) throws RuleEditorProcessException {
        if (ruleEditorBean == null) {
            throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE, "Verbindung zum Server nicht initialisiert!");
        }
        if (getPool() == null) {
            throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE, "Kein Pool ausgewählt!");
        }
        RuleExchangeResult result = ruleEditorBean.importPoolFromXML(getPool().getId(), PoolTypeHelper.getPoolType(getPool()), content, pOverrideFlag, pCheckFlag, true);
        return result == null ? new ArrayList<>() : result.getErrors();
    }

    public void copyRuleTo(CrgRules selectedItem, CrgRulePools pool) {
        if (ruleEditorBean == null) {
            return;
        }
        if (getPool() == null) {
            return;
        }
        try {
            ruleEditorBean.copyRules(List.of(selectedItem.getId()), getPool().getId(), PoolTypeHelper.getPoolType(getPool()), pool.getId(), PoolTypeHelper.getPoolType(pool));
        } catch (RuleEditorProcessException ex) {
            MainApp.showErrorMessageDialog(ex);
            Logger.getLogger(RuleListScene.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public RuleExchangeResult copyRulesTo(@NotNull List<Long> pRules, @NotNull CrgRulePools pTarget) {
        return copyRulesTo(pRules, pTarget, RuleImportCheckFlags.NO_CHECK_4_COLLISIONS, RuleOverrideFlags.SAVE_BOTH);
    }

    public RuleExchangeResult copyRulesTo(@NotNull List<Long> pRules, @NotNull CrgRulePools pTarget, RuleImportCheckFlags pCheckFlag, RuleOverrideFlags pOverrideFlag) {
        pTarget = Objects.requireNonNull(pTarget, "Target Pool can not be null");
        pRules = Objects.requireNonNull(pRules, "List of selected Items can not be null");
        pCheckFlag = Objects.requireNonNull(pCheckFlag, "CheckFlag can not be null");
        pOverrideFlag = Objects.requireNonNull(pOverrideFlag, "OverrideFlag can not be null");

        if (ruleEditorBean == null) {
            return null;
        }
        if (getPool() == null) {
            return null;
        }
        try {
            RuleExchangeResult result = ruleEditorBean.copyRules(pRules,
                    getPool().getId(),
                    PoolTypeHelper.getPoolType(getPool()),
                    pTarget.getId(),
                    PoolTypeHelper.getPoolType(pTarget),
                    pOverrideFlag,
                    pCheckFlag
            );
            return result;
        } catch (RuleEditorProcessException ex) {
            MainApp.showErrorMessageDialog(ex);
            Logger.getLogger(RuleListScene.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<CrgRulePools> getAvailableTargetPools() {
        if (ruleEditorBean == null) {
            return new ArrayList<>();
        }
        List<CrgRulePools> available = new ArrayList<>();
        available.addAll(ruleEditorBean.getDevCrgRulePools());

        if (getPool() instanceof CrgRulePoolsDev) {
            //add prod if pool is dev pool
            available.addAll(ruleEditorBean.getProdCrgRulePools());
        }
        available.remove(getPool());
        return available;
    }

    public String getRulesFromPoolAsXml(List<Long> pRuleIds) {
        if (ruleEditorBean == null) {
            return null;
        }
        if (getPool() == null) {
            return null;
        }
        try {
            return ruleEditorBean.exportPoolAsXML(getPool().getId(), PoolTypeHelper.getPoolType(getPool()), pRuleIds);
        } catch (RuleEditorProcessException ex) {
            Logger.getLogger(RuleListScene.class.getName()).log(Level.SEVERE, null, ex);
            MainApp.showErrorMessageDialog(ex);
        }
        return null;
    }

    public boolean isEditable() {
        return PoolTypeEn.DEV.equals(PoolTypeHelper.getPoolType(getPool()));
    }
//    public void setFilterManager(FilterManager pManager) {
//        filterManagerProperty().set(pManager);
//    }
//    
//    //filter manager property to react to changes in manager
//    private ObjectProperty<FilterManager> filterManagerProperty;
//    
//    /**
//    * @return get filter manager property for bidnings or listen to changes
//    */
//    public ObjectProperty<FilterManager> filterManagerProperty() {
//        if (filterManagerProperty == null) {
//            filterManagerProperty = new SimpleObjectProperty<>();
//        }
//        return filterManagerProperty;
//    }
//    
//    /**
//    * @return filter manager currently set
//    */
//    public final FilterManager getFilterManager() {
//        return filterManagerProperty().get();
//    }

    public final RuleListFilterManager getFilterManager() {
        RuleListFilterManager ret = RuleListFilterManager.getInstance();
        ret.setPoolType(PoolTypeHelper.getPoolType(getPool()));
        ret.hasMessages(containsErroneousRule());
        return ret;
    }

    public Long getRuleCountForRole(long pRoleId) {
        if (ruleEditorBean == null) {
            return -1L;
        }
        if (getPool() == null) {
            return -1L;
        }
        return ruleEditorBean.getRuleCountForRole(getPool().getId(), PoolTypeHelper.getPoolType(getPool()), pRoleId);
    }

    public ArrayList<CrgRules> getAllAvailableRulesForRole(@NotNull CdbUserRoles pRoles) {
        pRoles = Objects.requireNonNull(pRoles, "role can not be null");
        return (ArrayList<CrgRules>) ruleEditorBean.getAllAvailableRulesForRole(pRoles.getId(), getPool().getId(), PoolTypeHelper.getPoolType(getPool()));
    }

    public ArrayList<CrgRules> getAllActiveRulesForRole(@NotNull CdbUserRoles pRoles) {
        pRoles = Objects.requireNonNull(pRoles, "role can not be null");
        return (ArrayList<CrgRules>) ruleEditorBean.getAllActiveRulesForRole(pRoles.getId(), getPool().getId(), PoolTypeHelper.getPoolType(getPool()));

    }

    public void updateRoleToRule(CdbUserRolesItem pRoleToRule) {
        CdbUserRolesItem roleToRule = Objects.requireNonNull(pRoleToRule, "RoleToRule Item can not be null!");
        RuleEditorBeanRemote bean = Objects.requireNonNull(ruleEditorBean, BEAN_NOT_NULL);
        CrgRulePools pool = Objects.requireNonNull(getPool(), POOL_NOT_NULL);

        try {
            bean.updateRole(roleToRule.getRole().getId(), pool.getId(), PoolTypeHelper.getPoolType(pool), roleToRule.getActiveItems().stream().map((t) -> {
                return t.getId();
            }).collect(Collectors.toList()));
        } catch (RuleEditorProcessException ex) {
            Logger.getLogger(RuleListScene.class.getName()).log(Level.SEVERE, null, ex);
            MainApp.showErrorMessageDialog(ex);
        }
    }

    private BooleanProperty unsavedContentProperty;

    public BooleanProperty unsavedContentProperty() {
        if (unsavedContentProperty == null) {
            unsavedContentProperty = new SimpleBooleanProperty(false);
        }
        return unsavedContentProperty;
    }

    public void setUnsavedContentProperty(Boolean pUnsaved) {
        unsavedContentProperty().set(pUnsaved);
    }

    public Boolean hasUnsavedContent() {
        return unsavedContentProperty().get();
    }

    public boolean containsErroneousRule() {
        RuleEditorBeanRemote bean = Objects.requireNonNull(ruleEditorBean, BEAN_NOT_NULL);
        CrgRulePools pool = Objects.requireNonNull(getPool(), POOL_NOT_NULL);
        return bean.containsRuleMessage(pool.getId(),PoolTypeHelper.getPoolType(pool));
    }
    
    public boolean containsErroneousRuleTable() {
        RuleEditorBeanRemote bean = Objects.requireNonNull(ruleEditorBean, BEAN_NOT_NULL);
        CrgRulePools pool = Objects.requireNonNull(getPool(), POOL_NOT_NULL);
        
        return bean.containsRuleTableMessage(pool.getId(),PoolTypeHelper.getPoolType(pool));
    }


    public String findCodeSuggestionsForCode(String pCatalogCode, CrgRuleTables pTable) {
        try {
            TransferCatalogBeanRemote bean = Objects.requireNonNull(transferCatalogBean, BEAN_NOT_NULL);
            RuleTableMessage msg = new RuleTableMessageReader().readSingleResultOrNull(pTable.getCrgtMessage(),"UTF-8");
            return bean.getCodeSuggestion(pCatalogCode, pTable.getCrgtCategory(), msg.getSrcYear(), msg.getDestYear(), pTable.getId());
        } catch (IOException ex) {
            Logger.getLogger(RuleListScene.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public byte[] validateRuleTable(CrgRuleTables p) {
        Objects.requireNonNull(p, "RuleTable to validate can not be null!");
        try {
            TransferCatalogBeanRemote bean = Objects.requireNonNull(transferCatalogBean, BEAN_NOT_NULL);
            return bean.validateRuleTable(getPool().getCrgplPoolYear(), p);
        } catch (Exception ex) {
            Logger.getLogger(RuleListScene.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public byte[] validateRule(CrgRules pRule){
        long start = System.currentTimeMillis();
        if (pRule == null) {
            return null;
        }
        if(getPool() == null){
            return null;
        }
        TransferCatalogBeanRemote bean = Session.instance().getEjbConnector().connectTransferCatalogBean().get();
        byte[] msg = null;
        try {
//            CaseRuleManager.byteToString(pRule.getCrgrDefinition(), "UTF-16");
            msg = bean.validateRule(getPool().getId(),PoolTypeHelper.getPoolType(getPool()),pRule);
//            new RuleMessageReader().readUtf8(msg);
        } catch (Exception ex) {
            Logger.getLogger(RuleListScene.class.getName()).log(Level.SEVERE, null, ex);
        }
        LOG.info("validate Rule: " + pRule + " in " + (System.currentTimeMillis() - start) + " ms");
        return msg;
    }

    public byte[] getRuleMessage(CrgRules pRule) {
        long start = System.currentTimeMillis();
        if (pRule == null) {
            return null;
        }
        if(getPool() == null){
            return null;
        }
        RuleEditorBeanRemote bean = Objects.requireNonNull(ruleEditorBean, BEAN_NOT_NULL);
        byte[] msg = null;
        try {
//            CaseRuleManager.byteToString(pRule.getCrgrDefinition(), "UTF-16");
            msg = bean.getRuleMessage(pRule.getId(),getPool().getId(),PoolTypeHelper.getPoolType(getPool()));
//            new RuleMessageReader().readUtf8(msg);
        } catch (Exception ex) {
            Logger.getLogger(RuleListScene.class.getName()).log(Level.SEVERE, null, ex);
        }
        LOG.info("validate Rule: " + pRule + " in " + (System.currentTimeMillis() - start) + " ms");
        return msg;
    }
}
