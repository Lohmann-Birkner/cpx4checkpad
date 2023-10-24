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
package de.lb.cpx.client.ruleeditor.menu;

import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import de.lb.cpx.ruleviewer.util.PoolTypeHelper;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.server.commons.enums.RuleImportCheckFlags;
import de.lb.cpx.server.commons.enums.RuleOverrideFlags;
import de.lb.cpx.server.rule.services.RuleEditorBeanRemote;
import de.lb.cpx.server.rule.services.RuleEditorProcessException;
import de.lb.cpx.server.rule.services.RuleExchangeError;
import de.lb.cpx.server.rule.services.RuleExchangeResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javax.ejb.EJBTransactionRolledbackException;
import javax.naming.NamingException;

/**
 * Creates new PoolOverview Scene to display in Toolbarmenu of the ruleeditor
 *
 * @author wilde
 */
public class PoolOverviewScene extends CpxScene {

    private static final Logger LOG = Logger.getLogger(PoolOverviewScene.class.getName());

    public static final String RELOAD_POOLS_DEV = "reload.pools.dev";
    public static final String RELOAD_POOLS_PROD = "reload.pools.prod";

    private RuleEditorBeanRemote ruleEditorBean;

    /**
     * creates new instance
     *
     * @throws IOException thrown if fmxl is not found
     */
    public PoolOverviewScene() throws IOException {
        super(CpxFXMLLoader.getLoader(PoolOverviewFXMLController.class));
        initBeans();
        setSceneTitle("Regelpools");
        getController().afterInitialisingScene();
    }

    private void initBeans() {
        try {
            ruleEditorBean = Session.instance().getEjbConnector().connectRuleEditorBean().getWithEx();
        } catch (NamingException ex) {
            Logger.getLogger(PoolOverviewScene.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private final ReadOnlyObjectWrapper<CrgRulePools> selectedPoolProperty = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<CrgRulePools> selectedPoolProperty() {
        return selectedPoolProperty.getReadOnlyProperty();
    }

    public CrgRulePools getSelectedPool() {
        return selectedPoolProperty.get();
    }

    public void selectPool(CrgRulePools pPool) {
        selectedPoolProperty.set(pPool);
    }

    protected List<CrgRulePools> getWorkingPools() {
        if (ruleEditorBean == null) {
            return new ArrayList<>();
        }
        return ruleEditorBean.getDevCrgRulePools();
    }

    protected List<CrgRulePools> getProductivePools() {
        if (ruleEditorBean == null) {
            return new ArrayList<>();
        }
        return ruleEditorBean.getProdCrgRulePools();
    }

    public long savePool(CrgRulePools pNewPool, PoolTypeEn pType) throws RuleEditorProcessException {
        Objects.requireNonNull(pNewPool, "Pool can not be null");
        pType = Objects.requireNonNullElse(pType, PoolTypeHelper.getPoolType(pNewPool));
        return ruleEditorBean.savePool(pNewPool, pType);
    }

    public void reloadProdPools() {
        getProperties().put(RELOAD_POOLS_PROD, null);
    }

    public void reloadDevPools() {
        getProperties().put(RELOAD_POOLS_DEV, null);
    }

    public void reloadPools() {
        reloadDevPools();
        reloadProdPools();
    }

    public void reloadPool(PoolTypeEn type) {
        if (type == null) {
            reloadPools();
            return;
        }
        switch (type) {
            case DEV:
                reloadDevPools();
                break;
            case PROD:
                reloadProdPools();
        }
    }

    public void updatePool(CrgRulePools pPool, PoolTypeEn pType) {
        if (ruleEditorBean == null) {
            LOG.warning("bean is null");
            return;
        }
        try {
            ruleEditorBean.updatePool(pPool, pType);
        } catch (EJBTransactionRolledbackException ex) {
            MainApp.showErrorMessageDialog(ex, "Beim Ändern des Regelpools " + pPool.getCrgplIdentifier() + " ist ein Fehler aufgetretten!\n" + ex.getMessage());
        }
    }

    public void updateDevPool(CrgRulePools pPool) {
        updatePool(pPool, PoolTypeEn.DEV);
    }

    public void updateProdPool(CrgRulePools pPool) {
        updatePool(pPool, PoolTypeEn.PROD);
    }

    public void deletePool(CrgRulePools pPool, PoolTypeEn pType) {
        if (ruleEditorBean == null) {
            LOG.warning("bean is null");
            return;
        }
        try {
            ruleEditorBean.deletePool(pPool.getId(), pType);
        } catch (EJBTransactionRolledbackException ex) {
            MainApp.showErrorMessageDialog(ex, "Beim Löschen des Regelpools " + pPool.getCrgplIdentifier() + " ist ein Fehler aufgetreten!\n" + ex.getMessage());
        }
    }

    public int findSizeOfRules(long id, PoolTypeEn pType) {
        if (ruleEditorBean == null) {
            LOG.warning("bean is null");
            return -1;
        }
        return ruleEditorBean.findSizeOfRules(id, pType);
    }

    public int findSizeOfRuleTables(long id, PoolTypeEn pType) {
        if (ruleEditorBean == null) {
            LOG.warning("bean is null");
            return -1;
        }
        return ruleEditorBean.findSizeOfRuleTables2Pool(id, pType);
    }

    /**
     * calls with default override values CheckFlag is NO_CHECK OverrideFlag is
     * SAVE_BOTH
     *
     * @param pOrigin original pool
     * @param pTarget target pool
     * @param ruleIds ids of rule to copy
     * @return result of copying
     * @throws RuleEditorProcessException thrown if processing of rules can not
     * be done, due to incorrect xml etc.
     */
    public RuleExchangeResult copyRulesTo(CrgRulePools pOrigin, CrgRulePools pTarget, List<Long> ruleIds) throws RuleEditorProcessException {
        return copyRulesTo(pOrigin, pTarget, ruleIds, RuleImportCheckFlags.NO_CHECK_4_COLLISIONS, RuleOverrideFlags.SAVE_BOTH);
    }

    /**
     * @param pOrigin original pool
     * @param pTarget target pool
     * @param pRules ids of rule to copy
     * @param pCheckFlag flag to determine if collision detection should be done
     * or not
     * @param pOverrideFlag flag determine override mode
     * @return result of copying
     * @throws RuleEditorProcessException thrown if processing of rules can not
     * be done, due to incorrect xml etc.
     */
    public RuleExchangeResult copyRulesTo(CrgRulePools pOrigin, CrgRulePools pTarget, List<Long> pRules, RuleImportCheckFlags pCheckFlag, RuleOverrideFlags pOverrideFlag) throws RuleEditorProcessException {
        if (ruleEditorBean == null) {
            LOG.warning("RuleEditorBean is null!");
            throw RuleEditorProcessException.createRuleEditorProcessException(null, Level.SEVERE, "Regeln konnten nicht kopiert werden, Server nicht erreichbar!");
        }
        return ruleEditorBean.copyRules(pRules, pOrigin.getId(), PoolTypeHelper.getPoolType(pOrigin), pTarget.getId(), PoolTypeHelper.getPoolType(pTarget), pOverrideFlag, pCheckFlag);
    }

    public List<RuleExchangeError> importPool(CrgRulePools pPool, PoolTypeEn pType, String pContent, RuleImportCheckFlags pDoCheck, RuleOverrideFlags pDoOverride) throws RuleEditorProcessException {
        RuleEditorBeanRemote bean = Objects.requireNonNull(ruleEditorBean, "Bean can not be null");
        CrgRulePools pool = Objects.requireNonNull(pPool, "Pool can not be null");
        PoolTypeEn type = Objects.requireNonNull(pType, "Type can not be null");
        String content = Objects.requireNonNull(pContent, "Content can not be null");
        RuleOverrideFlags doOverride = Objects.requireNonNull(pDoOverride, "Content can not be null");
        RuleImportCheckFlags doCheck = Objects.requireNonNull(pDoCheck, "Content can not be null");
        RuleExchangeResult result = bean.importPoolFromXML(pool.getId(), type, content, doOverride, doCheck);
        return result == null ? new ArrayList<>() : result.getErrors();
    }

    public String getPoolAsXML(CrgRulePools pPool) {
        if (ruleEditorBean == null) {
            return null;
        }
        if (pPool == null) {
            return null;
        }
        try {
            return ruleEditorBean.exportPoolAsXML(pPool.getId(), PoolTypeHelper.getPoolType(pPool));
        } catch (RuleEditorProcessException ex) {
            Logger.getLogger(PoolOverviewScene.class.getName()).log(Level.SEVERE, null, ex);
            MainApp.showErrorMessageDialog(ex);
        }
        return null;
    }

    public long findPoolId4poolNameAndDates(CrgRulePools pPool, PoolTypeEn pType) {
        Objects.requireNonNull(pPool, "Pool can not be null");
        pType = Objects.requireNonNullElse(pType, PoolTypeHelper.getPoolType(pPool));
        return ruleEditorBean.findPoolId4poolNameAndDates(pPool, pType);
    }

    public boolean containsAnyMessage(long pId, PoolTypeEn pType) {
        if (ruleEditorBean == null) {
            LOG.warning("bean is null");
            return false;
        }
        return ruleEditorBean.containsAnyMessage(pId, pType);
    }
}
