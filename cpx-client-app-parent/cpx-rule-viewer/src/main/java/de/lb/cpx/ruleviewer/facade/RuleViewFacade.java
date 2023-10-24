/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde
 */
package de.lb.cpx.ruleviewer.facade;

import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.ruleviewer.util.PoolTypeHelper;
import de.lb.cpx.ruleviewer.util.TestPools;
import de.lb.cpx.server.commonDB.model.rules.CrgRule2Table;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.server.rule.services.RuleEditorProcessException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import org.hibernate.Hibernate;

/**
 * Facade to access data, should be modified?
 *
 * @author wilde
 */
public class RuleViewFacade {

    private static final Logger LOG = Logger.getLogger(RuleViewFacade.class.getName());

    private CrgRules rule;
    private CrgRulePools pool;
    private List<CrgRuleTables> ruleTables;

    public void setRule(CrgRules pRule) {
        rule = pRule;
    }

    public CrgRules getRule() {
        return rule;
    }

    public void setPool(CrgRulePools pPool) {
        pool = pPool;
    }

    public CrgRulePools getPool() {
        return pool;
    }
//    public Set<CrgRule2Table> getTables() {
//        if (rule == null) {
//            return new HashSet<>();
//        }
//        return rule.getCrgRule2Tables();
//    }
//
//    public CrgRuleTables getTable(String pName) {
//        pName = pName.replace("'", "");
//        for (CrgRule2Table table : getTables()) {
//            if (pName.toLowerCase().equals(table.getCrgRuleTables().getCrgtTableName().toLowerCase())) {
//                return table.getCrgRuleTables();
//            }
//        }
//        return null;
//    }

    public PoolTypeEn getPoolType() {
        return PoolTypeHelper.getPoolType(getPool());
    }

    private CrgRuleTables findRulesTableLocal(String pName) {
        pName = pName.replace("'", "");
        for (CrgRule2Table table : getRule().getCrgRule2Tables()) {
            if (pName.toLowerCase().equals(table.getCrgRuleTables().getCrgtTableName().toLowerCase())) {
                return table.getCrgRuleTables();
            }
        }
        return null;
    }

    private CrgRuleTables findRulesTableServer(String pName) {
        try {
            return Session.instance().getEjbConnector().connectRuleEditorBean().getWithEx().findRuleTable(pName, getPoolType(), getPool().getId());
        } catch (NamingException ex) {
            Logger.getLogger(RuleViewFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            LOG.log(Level.FINEST, "NullPointerException occured", ex);
            return TestPools.instance().findRuleTables(pName);
        }
        return null;
    }

    public CrgRuleTables findRuleTable(String pName) {
        if (Hibernate.isInitialized(getRule().getCrgRule2Tables()) && !getRule().getCrgRule2Tables().isEmpty()) {
            return findRulesTableLocal(pName);
        }
        return findRulesTableServer(pName);
    }

    /**
     * @return get cached ruletable, Maybe out of date!
     */
    public List<CrgRuleTables> getRuleTables() {
        if (ruleTables == null) {
            ruleTables = findRuleTables();
        }
        return ruleTables;
    }

    public void resetRuleTables() {
        ruleTables = null;
    }

    /**
     * @return loads complete new rule table from server!
     */
    public List<CrgRuleTables> findRuleTables() {
        try {
            List<CrgRuleTables> list = Session.instance().getEjbConnector().connectRuleEditorBean().getWithEx().findRuleTablesForPool(getPool().getId(), getPoolType());
            list.sort(Comparator.comparing(CrgRuleTables::getCrgtTableName));
            return list;
        } catch (NamingException ex) {
            Logger.getLogger(RuleViewFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex1) {
            LOG.log(Level.FINEST, "NullPointerException occured", ex1);
            return TestPools.instance().getAllRuleTables();
        }
        return new ArrayList<>();
    }

    public long getCurrentUser() {
        long id = Session.instance().getCpxUserId();
//      Logger.getLogger(RuleViewFacade.class.getName()).log(Level.INFO, " userId = " + id);
        return id;

    }

    public void addRuleTable(CrgRuleTables pTable) {
        try {
            Session.instance().getEjbConnector().connectRuleEditorBean().getWithEx().saveRuleTable(getPool().getId(), getPoolType(), pTable);
        } catch (NamingException ex) {
            Logger.getLogger(RuleViewFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex1) {
            LOG.log(Level.FINEST, "NullPointerException occured", ex1);
            TestPools.instance().getAllRuleTables().add(pTable);
            CrgRule2Table mapping = new CrgRule2Table();
            mapping.setCrgRuleTables(pTable);
            mapping.setCrgRules(getRule());
            getRule().getCrgRule2Tables().add(mapping);
        } catch (RuleEditorProcessException ex) {
            MainApp.showErrorMessageDialog(ex, "Beim Speichern der Tabelle " + pTable.getCrgtTableName() + " ist ein Fehler aufgetretten!\n" + ex.getMessage());
        }
//        getAllRuleTables().add(pTables);
//        CrgRule2Table mapping = new CrgRule2Table();
//        mapping.setCrgRuleTables(pTables);
//        mapping.setCrgRules(getRule());
//        getRule().getCrgRule2Tables().add(mapping);
    }

    public CrgRuleTables createCopy(CrgRuleTables pTable) {
//        return pTable;
        try {
            return Session.instance().getEjbConnector().connectRuleEditorBean().getWithEx().copyRuleTable(pTable);
        } catch (NamingException ex) {
            Logger.getLogger(RuleViewFacade.class.getName()).log(Level.SEVERE, null, ex);
            return pTable;
        } catch (NullPointerException ex1) {
            LOG.log(Level.FINEST, "NullPointerException occured", ex1);
            CrgRuleTables copy = new CrgRuleTables();
            copy.setCreationUser(1L);
            copy.setId(TestPools.instance().getAllRuleTables().size() + 1L);
            copy.setCrgtTableName(pTable.getCrgtTableName());
            copy.setCrgtContent(pTable.getCrgtContent());
            TestPools.instance().getAllRuleTables().add(copy);
            CrgRule2Table mapping = new CrgRule2Table();
            mapping.setCrgRuleTables(copy);
            mapping.setCrgRules(getRule());
            getRule().getCrgRule2Tables().add(mapping);
            return copy;
        }

    }

    public void updateRuleTable(CrgRuleTables pTable) {
        try {
            Session.instance().getEjbConnector().connectRuleEditorBean().getWithEx().updateRuleTable(getPool().getId(), getPoolType(), pTable);
        } catch (NamingException ex) {
            Logger.getLogger(RuleViewFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getRuleTableContent(CrgRuleTables table) {
        if (table == null) {
            return "";
        }
        if (table.getCrgtContent() != null) {
            return table.getCrgtContent();
        }
        try {
            return Session.instance().getEjbConnector().connectRuleEditorBean().getWithEx().getRuleTableContent(table.getId(), getPoolType());
        } catch (NamingException ex) {
            Logger.getLogger(RuleViewFacade.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public String getRuleTableComment(CrgRuleTables table) {
        if (table == null) {
            return "";
        }
        if (table.getCrgtComment() != null) {
            return table.getCrgtComment();
        }
        try {
            return Session.instance().getEjbConnector().connectRuleEditorBean().getWithEx().getRuleTableComment(table.getId(), getPoolType());
        } catch (NamingException ex) {
            Logger.getLogger(RuleViewFacade.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

}
