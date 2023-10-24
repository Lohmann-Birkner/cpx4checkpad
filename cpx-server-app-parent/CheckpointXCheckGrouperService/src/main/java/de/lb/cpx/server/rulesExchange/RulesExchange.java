/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.rulesExchange;

import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.server.commons.enums.RuleOverrideFlags;
import de.lb.cpx.server.rule.services.RuleExchangeBeanRemote;
import de.lb.cpx.shared.rules.util.RulesImportStatus;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author gerschmann
 */
@Stateless
public class RulesExchange implements RulesExchangeLocal {

    private static final Logger LOG = Logger.getLogger(RulesExchange.class.getName());

    @EJB
    private RuleExchangeBeanRemote rulesExchangeEjb;

    @Override
    public String saveRules4PoolInDB(String poolName, int year) {
        LOG.log(Level.INFO, "I try to save rules  from the rules directory into crg_rules table");
        try {
            return rulesExchangeEjb.saveRules4PoolInDB(poolName, year);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, " error on creating pool :" + poolName + " and year = " + String.valueOf(year), ex);
            return " error on creating pool :" + poolName + " and year = " + String.valueOf(year) + " " + ex.toString();
        }
    }

    @Override
    public String saveRuleTypesInDB() {
        LOG.log(Level.INFO, "I try to save rule types from the rules directory into crg_rule_types table");
        try {
            return rulesExchangeEjb.saveRuleTypesInDB();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, " error on save rule types from the rules directory into crg_rule_types table", ex);
            return " error on save rule types from the rules directory into crg_rule_types table " + ex.toString();
        }
    }

    @Override
    public String createRulePoolInDb(String poolName, int year) {
        LOG.log(Level.INFO, "I try to create rool pool with name = " + poolName + " and year = " + String.valueOf(year));
        try {
            return rulesExchangeEjb.createRulePoolInDb(poolName, year);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, " error on creating pool :" + poolName + " and year = " + String.valueOf(year), ex);
            return " error on creating pool :" + poolName + " and year = " + String.valueOf(year) + " " + ex.toString();
        }
    }

    @Override
    public String saveRuleTables4PoolInDB(String poolName, int year) {
        LOG.log(Level.INFO, "I try to save rule tables for pool = " + poolName + " and year = " + String.valueOf(year));
        try {
            return rulesExchangeEjb.saveRuleTables4PoolInDB(poolName, year);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, " error save rule tables for pool :" + poolName + " and year = " + String.valueOf(year), ex);
            return " error on save rule tables for pool :" + poolName + " and year = " + String.valueOf(year) + " " + ex.toString();
        }
    }

    @Override
    public String importExternalRulesInDB(String poolName, int year, String fileName, String doOverride, long[] roleIdsList) {
        return importExternalRulesInDB(poolName, year, fileName, doOverride, roleIdsList, PoolTypeEn.PROD, "admin");
    }

    @Override
    public String importExternalRulesInDB(String poolName, int year, String fileName, String doOverride, long[] roleIdsList, PoolTypeEn what, String userName) {
        LOG.log(Level.INFO, "I try to save rules for pool = {0} and year = {1} from file {2} with mode {3} and roles assignment {4}", new Object[]{poolName, String.valueOf(year), fileName, doOverride, roleIdsList});
        try {

            return rulesExchangeEjb.importExternalRulesInDB(poolName, year, RuleOverrideFlags.valueOf(doOverride), roleIdsList, fileName, what, userName);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, doOverride + " error save rules  for pool = :" + poolName + " and year = " + String.valueOf(year) + " from file " + fileName + " with mode " + doOverride, ex);
            return " error on import rules for pool :" + poolName + " and year = " + String.valueOf(year) + " from file " + fileName + " with mode " + doOverride + " " + ex.toString();
        }

    }

    @Override
    public String exportRulesFromDB(String poolName, int year, String fileName, List<String> rules_rid_list) {
        LOG.log(Level.INFO, "I try to save rules from pool = {0} and year = {1} into file {2} that listed in {3}", new Object[]{poolName, String.valueOf(year), fileName, rules_rid_list});
        try {

            return rulesExchangeEjb.exportRulesFromDB(poolName, year, rules_rid_list, fileName);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, " Error on export rules, rid  " + rules_rid_list + " from pool = :" + poolName + " and year = " + String.valueOf(year) + " into file " + fileName, ex);
            return " Error on export rules, rid  " + rules_rid_list + " from pool = :" + poolName + " and year = " + String.valueOf(year) + " into file " + fileName;
        }
    }

    @Override
    public String saveRulesFromDB(String poolName, int year) {
        LOG.log(Level.INFO, "I try to save rules from pool = {0} and year = {1} into internal pool directory ", new Object[]{poolName, String.valueOf(year)});
        try {

            return rulesExchangeEjb.saveRulesFromDB(poolName, year);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, " Error on writing of rules   from pool = :" + poolName + " and year = " + String.valueOf(year), ex);
            return " Error on writing of rules   from pool = :" + poolName + " and year = " + String.valueOf(year);
        }
    }

    /**
     * Generates an XMl - String from Rules from RID - List
     *
     * @param poolName pool name
     * @param year pool year
     * @param rules_rid_list list of rids
     * @return a generated XML - String
     */
    @Override
    public String getRulesFromDBAsXmlString(String poolName, int year, List<String> rules_rid_list) {
        LOG.log(Level.INFO, "I try to get rules from pool = {0} and year = {1} for the given RID - List ", new Object[]{poolName, String.valueOf(year)});
        try {

            return rulesExchangeEjb.getRulesFromDBAsXmlString(poolName, year, rules_rid_list);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, " Error on reading of rules   from pool = :" + poolName + " and year = " + String.valueOf(year), ex);
            return " Error on writing of rules   from pool = :" + poolName + " and year = " + String.valueOf(year);
        }
    }

    @Override
    public RulesImportStatus importRulesInDBFromXmlString(String poolName, int year, String mode, long[] roleIdList, String rulesString) {
        LOG.log(Level.INFO, "I try to get rules from pool = {0} and year = {1} for the given RID - List ", new Object[]{poolName, String.valueOf(year)});
        try {

            return rulesExchangeEjb.importRulesInDBFromXmlString(poolName, year, RuleOverrideFlags.valueOf(mode), roleIdList, rulesString);
        } catch (Exception ex) {
            RulesImportStatus status = new RulesImportStatus();
            LOG.log(Level.SEVERE, " Error on reading of rules   from pool = :" + poolName + " and year = " + String.valueOf(year), ex);
            status.setImportMessage(" Error on writing of rules   from pool = :" + poolName + " and year = " + String.valueOf(year));
            return status;
        }
    }

}
