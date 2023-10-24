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
import de.lb.cpx.model.TCase2RuleSelection;
import de.lb.cpx.model.TCheckResult;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.dao.TCheckResultDao;
import de.lb.cpx.server.dao.TGroupingResultsDao;
import de.lb.cpx.service.startup_ejb.RuleReadServicBeanLocal;
import de.lb.cpx.shared.dto.rules.CpxSimpleRuleDTO;
import de.lb.cpx.shared.rules.util.RulesConverter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Implementation for Rule Service
 *
 * @author wilde
 */
@Stateless(name = "RuleServiceBean")
public class RuleServiceBean implements RuleServiceBeanRemote {

    private static final Logger LOG = Logger.getLogger(RuleServiceBean.class.getSimpleName());

    @EJB(beanName = "RuleReadServiceBean")
    private RuleReadServicBeanLocal ruleStartUpbean;
    @EJB
    private TCheckResultDao checkResultDao;
    @EJB
    private TGroupingResultsDao groupingResultDao;

    @Override
    public de.checkpoint.ruleGrouper.CRGRule[] findAllRules() {
        return ruleStartUpbean.getRules();
    }

    @Override
    public Map<String, CRGRule> getRulesForYear(Integer yearOfSignificance) {
        return ruleStartUpbean.getRulesForYear(yearOfSignificance);
    }

    @Override
    public Map<String, CpxSimpleRuleDTO> findRulesForYearAndIds(Integer yearOfSignificance, List<String> listOfIds) {
        Map<String, CRGRule> mapOfRules = ruleStartUpbean.getRulesForYear(yearOfSignificance);
        Map<String, CpxSimpleRuleDTO> clientRuleMap = new HashMap<>();
        //find in Map Rules and add them to the result map
        for (String key : mapOfRules.keySet()) {
            if (listOfIds.contains(key)) {
                CRGRule currentRule = mapOfRules.get(key);

//                CpxSimpleRuleDTO dto = new CpxSimpleRuleDTO(currentRule.m_rid, currentRule.m_number, currentRule.m_typeText,
//                        currentRule.m_errorTypeText, currentRule.m_text, currentRule.m_caption, currentRule.m_notice, currentRule.m_suggestion);
                CpxSimpleRuleDTO dto = new CpxSimpleRuleDTO(currentRule);
                dto.setRisks(RulesConverter.createCpxRisksFromCpRule(currentRule));
                clientRuleMap.put(key, dto);
                listOfIds.remove(key);
                LOG.info("findRulesForYearAndIds");
            }
        }
        if (!listOfIds.isEmpty()) {
            for (String id : listOfIds) {
                LOG.warning("no rule for RuleId " + id);
            }
        }
        return clientRuleMap;
    }

    @Override
    public List<CpxSimpleRuleDTO> findRulesAsList(Integer yearOfSignificance, List<TCheckResult> listOfRuleResults){
        return findRulesAsList(yearOfSignificance, listOfRuleResults, null);
    }
    
    public List<CpxSimpleRuleDTO> findRulesAsList(Integer yearOfSignificance, List<TCheckResult> listOfRuleResults, List<TCase2RuleSelection> selList) {
//        Map<String, CRGRule> mapOfRules = ruleStartUpbean.getRulesForYear(yearOfSignificance);
        List<CpxSimpleRuleDTO> clientRuleMap = new ArrayList<>();
        //find in Map Rules and add them to the result map
        for (final TCheckResult ruleResult : listOfRuleResults) {
//            CRGRule currentRule = mapOfRules.get(String.valueOf(ruleResult.getRuleid()));
            CrgRules currentRule = ruleStartUpbean.getRule2Id(ruleResult.getRuleid());
            if (currentRule != null) {
                CpxSimpleRuleDTO dto = new CpxSimpleRuleDTO(currentRule);
                //set add Values if there is a detected rule for te current 
                dto.setChkCwDrg(ruleResult.getChkDrg());
                dto.setChkCwSimulDiff(ruleResult.getChkCwSimulDiff());
                dto.setChkFeeSimulDiff(ruleResult.getChkFeeSimulDiff());
                dto.setChkCwCareSimulDiff(ruleResult.getChkCwCareSimulDiff());
                dto.setChkReference(ruleResult.getChkReferences());
                dto.setRisks(RulesConverter.createCpxRisksFromCpxRule(currentRule));
                if(selList != null){
                    for(TCase2RuleSelection sel: selList){
                        if(sel.getRuleid().equals(currentRule.getCrgrRid())){
                            dto.setSelectedRuleFl(true);
                            break;
                        }
                    }
                }
                clientRuleMap.add(dto);
//// my test                
//                CrgRules crgRule = ruleStartUpbean.getRule2Id(ruleResult.getRuleid());
//                LOG.info("CrgRules for " + ruleResult.getRuleid() + (crgRule == null?"not found":crgRule.toString()) );
            } else {
                LOG.warning("No rule found with id " + ruleResult.getRuleid() + " for catalog year " + yearOfSignificance);
            }
        }

        return clientRuleMap;
    }

    @Override
    public Map<String, CpxSimpleRuleDTO> findRulesAdmissionDateAndIds(Date admissionDate, List<String> listOfIds) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(admissionDate);
        Integer yearOfSignificance = calendar.get(Calendar.YEAR);
        return findRulesForYearAndIds(yearOfSignificance, listOfIds);
    }

    @Override
    public List<CpxSimpleRuleDTO> findRulesAdmissionDateAndGroupingId(Date admissionDate, long groupingResultsId, long roleId) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(admissionDate);
        List<TCheckResult> chkList = checkResultDao.findDetectedRules(groupingResultsId, roleId);
        List<TCase2RuleSelection> selList = groupingResultDao.getCheckCaseRules4GroupingResult(groupingResultsId);
        return findRulesAsList(calendar.get(Calendar.YEAR), chkList, selList);
    }

    @Override
    public List<CpxSimpleRuleDTO> findRulesAdmissionDateAndGroupingId(Date admissionDate, long groupingResultsId) {
        return findRulesAdmissionDateAndGroupingId(admissionDate, groupingResultsId, ClientManager.getCurrentCpxUser().getActualRoleId());
    }

    @Override
    public CrgRules getRule(long pRId) {
        return ruleStartUpbean.getRule2Id(pRId);
    }

    @Override
    public void printStatistic() {
        int MAX_LENGHT = 1100;
        int count = 0;
        int totalcount = 0;
        CRGRule[] rules = ruleStartUpbean.getRules();
//        Collection<CRGRule> rules2017 = ruleStartUpbean.getRulesForYear(2017).values();
//        rules = ruleStartUpbean.getRulesForYear(2017).values().toArray(new CRGRule[rules2017.size()]);
        totalcount = rules.length;
        for (CRGRule rule : rules) {
            if (rule.getRuleNotice() == null) {
                continue;
            }
            if (rule.getRuleNotice().isEmpty()) {
                continue;
            }
            if (rule.getRuleNotice().length() > MAX_LENGHT) {
                count++;
            }
        }
        LOG.info("Rules[" + totalcount + "] which notice exceeding lenght of: " + MAX_LENGHT + " are " + count);
    }

 }
