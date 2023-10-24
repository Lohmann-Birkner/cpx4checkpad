/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.readrules;

import de.checkpoint.ruleGrouper.CRGRule;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.config.CpxServerConfigLocal;
import de.lb.cpx.service.startup_ejb.RuleReadBeanLocal;
import de.lb.cpx.service.startup_ejb.RuleReadServicBeanLocal;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

/**
 *
 * @author gerschmann
 */
@Stateless
@Local
//@Startup
//@Singleton
public class RuleReadServiceBean implements RuleReadServicBeanLocal {

    @EJB(beanName = "RuleReadXmlBean")
    private RuleReadBeanLocal ruleStartUpbean;

    @EJB(beanName = "RuleReadDbBean")
    private RuleReadBeanLocal ruleReadDbBean;

    @EJB(name = "CpxServerConfig")
    private CpxServerConfigLocal cpxServerConfig;

    private RuleReadBeanLocal getRuleBean() {
        if (cpxServerConfig.getRulesDatabaseConfig()) {
            return ruleReadDbBean;
        } else {
            return ruleStartUpbean;
        }

    }

    @PostConstruct
    public void init() {
        getRules();
    }

    @Override
    public CRGRule[] getRules() {
        return getRuleBean().getRules();
    }

    @Override
    public CRGRule getRule(int year, long ruleId) {
        return getRuleBean().getRule(year, ruleId);
    }

    @Override
    public CRGRule[] getRule2ListId(List<Long> ruleIds) {
        return getRuleBean().getRule2ListId(ruleIds);
    }

    @Override
    public Map<String, CRGRule> getRulesForYear(int year) {
        return getRuleBean().getRulesForYear(year);
    }

    @Override
    public void reloadRules4Pool(String poolName, int year) {
        getRuleBean().reloadRules4Pool(poolName, year);
    }

    @Override
    public CrgRules getRule2Id(long pRId) {
        return getRuleBean().getRule2Id(pRId);
    }

//    @Override
//    public List<CrgRules> getCpxRules() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public List<CrgRuleTypes> getCpxTypes() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public List<CrgRulePools> getCpxPools() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//    @Override
//    public List<CrgRules> getCpxRules() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public List<CrgRuleTypes> getCpxTypes() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public List<CrgRulePools> getCpxPools() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public List<CrgRules> getRules4Year(long pPoolId) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public int findSizeOfRules(long pPoolId) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public List<CrgRuleTables> findRuleTables4Year(long pPoolId) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public List<CrgRuleTables> getCpxTables() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public CrgRuleTables getCpxTable4Year(String pName, long pPoolId) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public byte[] getRuleDefinition(long pRuleId, long pPoolId) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public List<CdbUserRoles> findUserRoles2Rule(long pRuleId) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public String getRuleTableContent(long pTableId) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public String getRuleTypeNameForRuleAndYear(long pRuleId, long pPoolId) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public String getRuleTypeDisplayNameForRuleAndYear(long pRuleId, long pPoolId) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    @Override
    public List<String> getRids4SearchOptions(Map<String, List<String>> pFilterOptionMap) {
        return getRuleBean().getRids4SearchOptions(pFilterOptionMap);
    }

    @Override
    public void resetRuleList() {
        getRuleBean().resetRules();
    }

    @Override
    public Map<String, String> getRuleTypes2Ids() {
        return getRuleBean().getRuleTypes2Ids();
    }
}
