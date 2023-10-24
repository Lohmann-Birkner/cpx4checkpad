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

import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.server.commons.enums.RuleOverrideFlags;
import de.lb.cpx.shared.rules.util.RulesImportStatus;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author wilde
 */
@Remote
public interface RuleExchangeBeanRemote {

    /**
     * checks whether the rule pool with this parameter exists and when not
     * creates one. When exists returns no error but message
     *
     * @param poolName pool name
     * @param year year
     * @return pool whatever(?)
     */
    String createRulePoolInDb(String poolName, int year);

    /**
     * reads the csrues_types.xml from the WD_CPX_SERVER/rules directory and
     * saves them in the CRG_RULE_TYPES table and overrides the ones with the
     * same ident.
     *
     * @return whatever(?)
     */
    String saveRuleTypesInDB();

    /**
     * reads XML File from the directory SW_SERVER/rules/poolName and saves them
     * in DB Checks, whether POOl - line in CRG_RULE_POOLS exists and when not,
     * creates it so, that the imorted rules have always the pool reference the
     * rule types are to be already there. If poolName == String.valueOf(year)
     * this is the year directory else the name of the directory is
     * poolname_year (compatibility to Checkpoint).
     *
     * @param poolName pool name
     * @param year year
     * @return whatever(?)
     */
    String saveRules4PoolInDB(String poolName, int year);

    /**
     * saves rule tables from the directory SW_SERVER/rules/poolName/tables in
     * DB
     *
     * @param poolName pool name
     * @param year year
     * @return whatever(?)
     */
    String saveRuleTables4PoolInDB(String poolName, int year);

    /**
     * saves the rules, which are saved in the external xml form in DB, creates
     * pool if it does not exist, overrides the existing rule on set condition
     *
     * @param poolName pool name
     * @param year rool year
     * @param fileName name of file
     * @param doOverride mode of override option
     * @param roleIdList list with selected role IDs
     * @return status of import
     */
    RulesImportStatus importRulesInDB(String poolName, int year, RuleOverrideFlags doOverride, long[] roleIdList, String fileName);

   /**
     * saves the rules, which are saved in the external xml form in DB, creates
     * pool if it does not exist, overrides the existing rule on set condition
     *
     * @param poolName pool name
     * @param year rool year
     * @param fileName name of file
     * @param doOverride mode of override option
     * @param roleIdList list with selected role IDs
     * @param types2create rule types, which are to be created bevore rules import
     * @return status of import
     */
    RulesImportStatus importRulesInDB(String poolName, int year, RuleOverrideFlags doOverride, long[] roleIdList, String fileName, List<String>types2create);
    /**
     * saves the rules, which are saved in the external xml form in DB, creates
     * pool if it does not exist, overrides the existing rule on set condition
     *
     * @param poolName pool name
     * @param year rool year
     * @param fileName name of file
     * @param doOverride mode of override option
     * @param roleIdList list with selected role IDs
     * @return status of import
     */
    String importExternalRulesInDB(String poolName, int year, RuleOverrideFlags doOverride, long[] roleIdList, String fileName);

    /**
     * saves the rules, which are saved in the external xml form in DB, creates
     * pool if it does not exist, overrides the existing rule on set condition
     *
     * @param poolName pool name
     * @param year rool year
     * @param fileName name of file
     * @param doOverride mode of override option
     * @param roleIdList list with selected role IDs
     * @param what typ des regelpools(DEV/PROD)
     * @param userName user name
     * @return status of import
     */
    String importExternalRulesInDB(String poolName, int year, RuleOverrideFlags doOverride, long[] roleIdList, String fileName, PoolTypeEn what, String userName);

    /**
     * validate the rules, which are saved in the external xml form in DB,
     * creates pool if it does not exist, overrides the existing rule on set
     * condition
     *
     * @param poolName pool name
     * @param year rool year
     * @param fileName name of file
     * @param doOverride mode of override option
     * @param roleIdList list with selected role IDs
     * @return status of validation
     */
    RulesImportStatus validateExternalRulesInDB(String poolName, int year, String fileName, RuleOverrideFlags doOverride, long[] roleIdList);

    /**
     * export of rules with listed rids in rules_rid_list as [rid1,rid2,rid3..]
     * from pool and jear in file filename on path C:\rules_import\export
     *
     * @param poolName pool name
     * @param year year
     * @param fileName file name
     * @param rules_rid_list if null, export all rules from pool
     * @return information about errors and doubles as String
     */
    String exportRulesFromDB(String poolName, int year, List<String> rules_rid_list, String fileName);

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
     * @param doOverride mode of override option
     * @param roleIdList list with selected role IDs
     * @param RulesString Rules as XML - String
     * @return status of import
     */
    RulesImportStatus importRulesInDBFromXmlString(String poolName, int year, RuleOverrideFlags doOverride, long[] roleIdList, String RulesString);

}
