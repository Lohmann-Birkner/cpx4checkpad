/* 
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.catalog.service.ejb;

import de.checkpoint.ruleGrouper.CRGRule;
import de.lb.cpx.model.TCheckResult;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.shared.dto.rules.CpxSimpleRuleDTO;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 * Interface for RuleService
 *
 * @author wilde
 */
@Remote
public interface RuleServiceBeanRemote {

    /**
     * get List of all Rules read-in on StartUp
     *
     * @return List of all CrgRule Objects
     */
    CRGRule[] findAllRules();

    /**
     * get Map of all Rules for specified year, keys are RuleIds
     *
     * @param yearOfSignificance year of significance for rulemap
     * @return map of rules
     */
    Map<String, CRGRule> getRulesForYear(Integer yearOfSignificance);

    /**
     * get Map for rules for a specific year and rule Ids
     *
     * @param yearOfSignificance year where rules are significant
     * @param listOfIds list of RuleIds
     * @return Map of rule Dto Obejcts
     */
    Map<String, CpxSimpleRuleDTO> findRulesForYearAndIds(Integer yearOfSignificance, List<String> listOfIds);

    /**
     * get Map for rules for a specific year and rule Ids
     *
     * @param admissionDate admissionDate of Case where rules apply
     * @param listOfIds list of RuleIds
     * @return Map of rule Dto Obejcts
     */
    Map<String, CpxSimpleRuleDTO> findRulesAdmissionDateAndIds(Date admissionDate, List<String> listOfIds);

    /**
     * find rules for groupingResultId and admission date
     *
     * @param csdAdmissionDate date of admission, define which rule catalog is
     * to use
     * @param resultId id of the grouping result in the Database
     * @return List of CpxSimpleRuleDto, transport subset of rule information to
     * the client
     */
//    List<CpxSimpleRuleDTO> findRulesAdmissionDateAndGroupingId(Date csdAdmissionDate, long resultId);
    /**
     * find rules for year of significance and list of detected rule ids
     *
     * @param yearOfSignificance year to determine catalog to look in
     * @param listOfRules List of rules
     * @return List of CpxSimpleRuleDto, transport subset of rule information to
     * the client
     */
    List<CpxSimpleRuleDTO> findRulesAsList(Integer yearOfSignificance, List<TCheckResult> listOfRules);

    /**
     * find rules for groupingResultId and admission date
     *
     * @param admissionDate date of admission, define which rule catalog is to
     * use
     * @param resultId id of the grouping result in the Database
     * @param ruleId actual role id of the user
     * @return List of CpxSimpleRuleDto, transport subset of rule information to
     * the client
     */
    List<CpxSimpleRuleDTO> findRulesAdmissionDateAndGroupingId(Date admissionDate, long resultId, long ruleId);

    /**
     * find rules for groupingResultId and admission date
     *
     * @param admissionDate date of admission, define which rule catalog is to
     * use
     * @param resultId id of the grouping result in the Database
     * @return List of CpxSimpleRuleDto, transport subset of rule information to
     * the client
     */
    List<CpxSimpleRuleDTO> findRulesAdmissionDateAndGroupingId(Date admissionDate, long resultId);

    /**
     * @param pRId rId of the rule
     * @return Database entity of the rule Note: if this rule is stored in
     * memory id is 0
     */
    CrgRules getRule(long pRId);

    /**
     * print some rule data statistic
     */
    void printStatistic();

}
