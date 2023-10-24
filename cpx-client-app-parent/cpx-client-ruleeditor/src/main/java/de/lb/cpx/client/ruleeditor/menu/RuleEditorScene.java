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

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import de.lb.cpx.client.ruleeditor.events.UpdateRuleEvent;
import de.lb.cpx.client.ruleeditor.menu.dialogs.buttontypes.RuleEditorButtonTypes;
import de.lb.cpx.grouper.model.transfer.TransferRuleAnalyseResult;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.ruleviewer.event.RuleTableChangedEvent;
import de.lb.cpx.ruleviewer.util.PoolTypeHelper;
import de.lb.cpx.server.commonDB.model.CCase;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.server.rule.services.RuleEditorBeanRemote;
import de.lb.cpx.server.rule.services.RuleEditorProcessException;
import de.lb.cpx.server.rule.services.RuleLockBeanRemote;
import de.lb.cpx.service.ejb.SingleCaseGroupingEJBRemote;
import de.lb.cpx.service.ejb.TransferCatalogBeanRemote;
import de.lb.cpx.shared.dto.LockException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.scene.control.ButtonType;
import javax.naming.NamingException;

/**
 *
 * @author wilde
 */
public class RuleEditorScene extends CpxScene {

    private static final Logger LOG = Logger.getLogger(RuleEditorScene.class.getName());

    protected static final String UPDATE_METADATA = "update.metadata";
    protected static final String DISCARD_RULE_CHANGE = "discard.rule.change";
    private RuleEditorBeanRemote ruleEditorBean;
    private RuleLockBeanRemote ruleLockBean;


    public RuleEditorScene(CrgRulePools pPool, CrgRules pRule) throws IOException, NamingException, LockException {
        super(CpxFXMLLoader.getLoader(RuleEditorFXMLController.class));
        initBeans();
        if (!PoolTypeEn.PROD.equals(PoolTypeHelper.getPoolType(pPool))) {
            ruleLockBean.lock(pPool.getId(), pRule.getId());
        }
        setPool(pPool);
        setRule(pRule);
//        addEventHandler(RuleTableChangedEvent.ruleTableChangedEvent(), new EventHandler<RuleTableChangedEvent>() {
//            @Override
//            public void handle(RuleTableChangedEvent event) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//        });
        getController().afterInitialisingScene();
    }

    private final ReadOnlyObjectWrapper<CrgRules> ruleProperty = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<CrgRules> ruleProperty() {
        return ruleProperty.getReadOnlyProperty();
    }

    private void setRule(CrgRules pRule) {
        ruleProperty.set(pRule);
    }

    public CrgRules getRule() {
        return ruleProperty().get();
    }
    private final ReadOnlyObjectWrapper<CrgRulePools> poolProperty = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<CrgRulePools> poolProperty() {
        return poolProperty.getReadOnlyProperty();
    }

    private void setPool(CrgRulePools pRule) {
        poolProperty.set(pRule);
    }

    public CrgRulePools getPool() {
        return poolProperty().get();
    }

    public boolean isEditable() {
        return !PoolTypeEn.PROD.equals(getPoolType());
    }

    private final BooleanProperty showAnalyserProperty = new SimpleBooleanProperty(false);

    public BooleanProperty showAnalyserProperty() {
        return showAnalyserProperty;
    }

    public boolean isShowAnalyser() {
        return showAnalyserProperty.get();
    }

    public void setShowAnalyser(boolean pShowAnalyser) {
        showAnalyserProperty.set(pShowAnalyser);
    }
//    public List<CrgRuleTables> findRuleTables(){
//        if(ruleEditorBean == null){
//            LOG.warning("can not find RuleTables, Bean not initialized!");
//            return new ArrayList<>();
//        }
//        if(getPool()==null){
//            LOG.info("can not find RuleTables, Pool is null");
//            return new ArrayList<>();
//        }
//        return ruleEditorBean.findRuleTablesForPool(getPool().getId());
//    } 

    private void initBeans() throws NamingException {
        ruleEditorBean = Session.instance().getEjbConnector().connectRuleEditorBean().getWithEx();
        ruleLockBean = Session.instance().getEjbConnector().connectRuleLockBean().getWithEx();
    }

    @Override
    public boolean close() {
        if (!PoolTypeEn.PROD.equals(PoolTypeHelper.getPoolType(getPool()))) {
            ruleLockBean.unlock(getPool().getId(), getRule().getId());
        }
        return super.close(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void refresh() {

        super.refresh(); //To change body of generated methods, choose Tools | Templates.
    }

    private final BooleanProperty unsavedProperty = new SimpleBooleanProperty(false);

    public BooleanProperty unsavedProperty() {
        return unsavedProperty;
    }

    public boolean isUnsaved() {
        return unsavedProperty.get();
    }

    public void setUnsaved(boolean pUnsaved) {
        unsavedProperty().set(pUnsaved);
    }

    public void saveRule(CrgRules rule) {
        if (ruleEditorBean == null) {
            return;
        } 
        try {
            ruleEditorBean.updateRule(rule, getPool().getId(), getPoolType());
            rule.setCrgrMessage(getRuleMessage(rule));
        } catch (RuleEditorProcessException ex) {
            MainApp.showErrorMessageDialog(ex, "Beim Speichern der Regel " + rule.getCrgrIdentifier() + " ist ein Fehler aufgetretten!\n" + ex.getMessage());
        }
        //update some rule values that could have changed
        getRule().setCrgrNumber(rule.getCrgrNumber());
        getRule().setCrgrDefinition(rule.getCrgrDefinition());
        getRule().setCrgrMessage(rule.getCrgrMessage());
        getProperties().put(UPDATE_METADATA, null);

        UpdateRuleEvent event = new UpdateRuleEvent(UpdateRuleEvent.updateRuleEvent(), getPool(), rule, false);
        Event.fireEvent(this, event);
        //Second event maybe unnessecary .. should check update rule and with rule and compare if in displayed ruletable 
        //rule is displayed if so .. refresh
        RuleTableChangedEvent event2 = new RuleTableChangedEvent(RuleTableChangedEvent.ruleTableChangedEvent());
        Event.fireEvent(this, event2);
    }

    public PoolTypeEn getPoolType() {
        return PoolTypeHelper.getPoolType(getPool());
    }

    public AlertDialog getUnSavedDialog() {
        return getUnSavedDialog(null);
    }
    private final ButtonType[] shutdownDialogButtons = new ButtonType[]{RuleEditorButtonTypes.SAVE_ALL,RuleEditorButtonTypes.DISCARD};
    private final ButtonType[] closeDialogButtons = new ButtonType[]{RuleEditorButtonTypes.SAVE_AND_CLOSE,RuleEditorButtonTypes.DISCARD,ButtonType.CANCEL};
    
    public AlertDialog getUnSavedDialog(String pDescription, boolean pShutdown){
        AlertDialog dialog = AlertDialog.createWarningDialog(new StringBuilder("Sie haben ungesicherte Änderungen!").append(pDescription!=null?"\n":"").append(Objects.requireNonNullElse(pDescription, "")).toString(),
                pShutdown?shutdownDialogButtons:closeDialogButtons);
        return dialog;
    }
    public AlertDialog getUnSavedDialog(String pDescription) {
        return getUnSavedDialog(pDescription, false);
    }
    public AlertDialog getUnSavedDialog(CrgRules pRule, CrgRulePools pPool) {
        return getUnSavedDialog(pRule,pPool,false);
    }
    public AlertDialog getUnSavedDialog(CrgRules pRule, CrgRulePools pPool,boolean pShutdown) {
        return getUnSavedDialog(new StringBuilder("In der Regel: ")
                .append(pRule.getCrgrNumber())
                .append(" aus dem Pool: ")
                .append(pPool.getCrgplIdentifier())
                .append(" wurden nicht gesicherte Änderungen festgestellt!\nWie wollen Sie fortfahren?").toString(),pShutdown);
    }
    public void saveRule() {
        ((RuleEditorFXMLController) getController()).saveRule();
    }

    public void updateRole(long pRole) {
        ((RuleEditorFXMLController) getController()).updateRole(pRole);
    }

    public void analyseRuleForTest(String ruleXml, TCase pTestCase) {
//        RuleCheckServiceBeanRemote analyzer = Session.instance().getEjbConnector().connectRuleCheckBean().get();
        RuleEditorBeanRemote analyzer = Session.instance().getEjbConnector().connectRuleEditorBean().get();
        TransferRuleAnalyseResult result = analyzer.analyseRule(ruleXml, pTestCase, getPoolType());
        String msg = pTestCase.getCurrentLocal() == null ? result.toString() : ("\nFalldaten:\n" + pTestCase.getCurrentLocal().getComment() + "\n\nTestresultat:\n" + result.toString());
        AlertDialog dialog = AlertDialog.createInformationDialog(msg);
        dialog.showAndWait();
    }

    public TransferRuleAnalyseResult analyseRule(String ruleXml, TCase pCase) {
        return analyseRule(ruleXml, pCase, null);
    }

    public TransferRuleAnalyseResult analyseRule(String ruleXml, TCase pCase, Map<String, String> pTables) {
        Objects.requireNonNull(ruleXml, "Rule can not be null");
        Objects.requireNonNull(pCase, "Case can not be null");
        RuleEditorBeanRemote analyzer = Session.instance().getEjbConnector().connectRuleEditorBean().get();
        TransferRuleAnalyseResult result = analyzer.analyseRule(ruleXml, pCase, pTables, getPoolType());
//        TransferRuleResult result = analyzer.analyseRule(ruleXml, pCase,pTables);
        return result;
    }
    
    public TGroupingResults getTmpGroupingResults4Case(TCase pCase){
        SingleCaseGroupingEJBRemote grouper = Session.instance().getEjbConnector().connectSingleCaseGroupingBean().get();
        return grouper.getTempGroupingResults(pCase, Boolean.TRUE, GDRGModel.AUTOMATIC);
    }
    
    public TransferRuleAnalyseResult analyseHistoryRule(String ruleXml, TCase pCase, Map<String, String> pTables, List<TCase>historyCases) {
        Objects.requireNonNull(ruleXml, "Rule can not be null");
        Objects.requireNonNull(pCase, "Case can not be null");
        RuleEditorBeanRemote analyzer = Session.instance().getEjbConnector().connectRuleEditorBean().get();
        TransferRuleAnalyseResult result = analyzer.analyseRule(ruleXml, pCase, pTables, getPoolType(), historyCases);
//        TransferRuleResult result = analyzer.analyseRule(ruleXml, pCase,pTables);
        return result;
    }

    public List<CCase> getAllAnalyserCasesForUser() {
        long start = System.currentTimeMillis();
        RuleEditorBeanRemote bean = Session.instance().getEjbConnector().connectRuleEditorBean().get();
        List<CCase> list = bean.getAllAnalyserCases(Session.instance().getCpxUserId());
        LOG.info("get all cCases in: " + (System.currentTimeMillis() - start) + " ms");
        return list;
    }

    public TCase loadHospitalCase(CCase p) {
        long start = System.currentTimeMillis();
        if (p == null) {
            return null;
        }
        RuleEditorBeanRemote bean = Session.instance().getEjbConnector().connectRuleEditorBean().get();
        TCase hospitalCase = bean.getHospitalCaseFromCaseCopy(p.getId());
        LOG.info("get TCase from ccase in: " + (System.currentTimeMillis() - start) + " ms");
        return hospitalCase;
    }

    public Boolean deleteAnalyserCases(List<Long> pCases) {
        long start = System.currentTimeMillis();
        if (pCases == null || pCases.isEmpty()) {
            return false;
        }
        RuleEditorBeanRemote bean = Session.instance().getEjbConnector().connectRuleEditorBean().get();
        bean.deleteAnalyserCases(pCases);
        LOG.info("get TCase from ccase in: " + (System.currentTimeMillis() - start) + " ms");
        return true;
    }

    public Boolean saveAnalyserCase(CCase pCase) {
        long start = System.currentTimeMillis();
        if (pCase == null) {
            return false;
        }
        RuleEditorBeanRemote bean = Session.instance().getEjbConnector().connectRuleEditorBean().get();
        bean.saveAnalyserCases(pCase);
        LOG.info("get TCase from ccase in: " + (System.currentTimeMillis() - start) + " ms");
        return true;
    }

    public Boolean saveHospitalCaseContent(long pAnalyserCaseId, TCase pCase) {
        long start = System.currentTimeMillis();
        if (pCase == null) {
            return false;
        }
        RuleEditorBeanRemote bean = Session.instance().getEjbConnector().connectRuleEditorBean().get();
        bean.updateAnalyserCaseContent(pAnalyserCaseId, pCase);
        LOG.info("update case content of ccase " + pAnalyserCaseId + " in: " + (System.currentTimeMillis() - start) + " ms");
        return true;
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
            msg = bean.validateRule(getPool().getId(),getPoolType(),pRule);
        } catch (Exception ex) {
            Logger.getLogger(RuleEditorScene.class.getName()).log(Level.SEVERE, null, ex);
        }
        LOG.info("validate Rule: " + pRule + " in " + (System.currentTimeMillis() - start) + " ms");
        return msg;
    }

    public void discardRuleChange() {
        getProperties().put(DISCARD_RULE_CHANGE, null);
    }

    public byte[] getRuleMessage(CrgRules rule) {
        return Session.instance().getEjbConnector().connectRuleEditorBean().get().getRuleMessage(rule.getId(), getPool().getId(), getPoolType());
    }
}
