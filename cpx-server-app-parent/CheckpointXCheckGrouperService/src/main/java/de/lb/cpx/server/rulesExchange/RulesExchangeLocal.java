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
import de.lb.cpx.shared.rules.util.RulesImportStatus;
import java.util.List;
import javax.ejb.Local;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 *
 * @author gerschmann
 */
@Local
@SecurityDomain(value = "cpx")
public interface RulesExchangeLocal {

    /**
     * reads XML File from the directory SW_SERVER/rules/poolName and saves them
     * in DB Checks, whether POOl - line in CRG_RULE_POOLS exists and when not,
     * creates it so, that the imorted rules have always the pool reference the
     * rule types are to be already there. If poolName == String.valueOf(year)
     * this is the year directory else the name of the directory is
     * poolname_year (compatibility to Checkpoint).
     *
     * @param poolName source/destinaltion pool name
     * @param year source/destinaltion pool year
     * @return whatever(?)
     */
    String saveRules4PoolInDB(String poolName, int year);

    /**
     * reads XML File SW_SERVER/rules/csrules_types.xml and saves the rule types
     * in the DB table CRG_RULE_TYPES, compares the imported types with saved
     * ones
     *
     * @return whatever(?)
     */
    String saveRuleTypesInDB();

    /**
     * creates rule pool in the DB Table CRG_RULE_POOLS
     *
     * @param poolName destinaltion pool name
     * @param year destinaltion pool year
     * @return whatever(?)
     */
    String createRulePoolInDb(String poolName, int year);

    /**
     * reads all rule tables from file system on path
     * SW_SERVER/rules/poolname_year/table and saves them in Table
     * CRG_RULE_TABLES
     *
     * @param poolName destination pool name
     * @param year destination year
     * @return whatever(?)
     *
     */
    String saveRuleTables4PoolInDB(String poolName, int year);

    /**
     * Import of rules from file
     *
     * @param poolName destinaltion pool
     * @param year pool year
     * @param fileName filename of file placed in defined directory
     * @param doOverride how to handle rules, types or tables
     * @param roleIdsList list of role ids
     * @return whatever(?)
     */
    String importExternalRulesInDB(String poolName, int year, String fileName, String doOverride, long[] roleIdsList);

    /**
     * Import of rules from file
     *
     * @param poolName destinaltion pool
     * @param year pool year
     * @param fileName filename of file placed in defined directory
     * @param doOverride how to handle rules, types or tables
     * @param roleIdsList list of role ids
     * @param what _
     * @param userName user name
     * @return whatever(?)
     */
    String importExternalRulesInDB(String poolName, int year, String fileName, String doOverride, long[] roleIdsList, PoolTypeEn what, String userName);

    /**
     * export of rulse with listed rids in rules_rid_list as [rid1,rid2,rid3..]
     * from pool and jear in file filename on path C:\rules_import\export
     *
     * @param poolName source pool name
     * @param year source year
     * @param filename restination file name
     * @param rules_rid_list list of rule rids
     * @return whatever(?)
     */
    String exportRulesFromDB(String poolName, int year, String filename, List<String> rules_rid_list);

    /**
     * saves rules from DB in the rule directory for internal usage, overrides
     * its content in SW_SERVER/rules/poolname_poolyear
     *
     * @param poolName source pool name
     * @param year source year
     * @return whatever(?)
     */
    String saveRulesFromDB(String poolName, int year);

    /**
     * Generates an XMl - String from Rules from RID - List
     *
     * @param poolName pool name
     * @param year pool year
     * @param rules_rid_list list of rids
     * @return a generated XML - String
     */
    String getRulesFromDBAsXmlString(String poolName, int year, List<String> rules_rid_list);

    /**
     * saves the rules, which are saved in the external xml form in DB, creates
     * pool if it does not exist, overrides the existing rule on set condition
     *
     * @param poolName pool name
     * @param year rool year
     * @param mode how to handle rules, types or tables
     * @param roleIdList list with selected role IDs
     * @param RulesString Rules as XML - String
     * @return status of import
     */
    RulesImportStatus importRulesInDBFromXmlString(String poolName, int year, String mode, long[] roleIdList, String RulesString);

}
