/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  Bohm - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.service.startup_ejb.RuleReadServicBeanLocal;
import de.lb.cpx.shared.dto.RuleListItemDTO;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.FilterOption;
import de.lb.cpx.shared.filter.enums.RuleListAttributes;
import de.lb.cpx.shared.filter.enums.WorkingListAttributes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Bohm
 */
@Stateless
public class RuleListSearchService extends AbstractSearchService<RuleListItemDTO> {
//    @EJB(name = "RuleReadBean",beanInterface = RuleReadBeanLocal.class)
//    RuleReadBean ruleBean;

    private static final Logger LOG = Logger.getLogger(RuleListSearchService.class.getName());

    private RuleReadServicBeanLocal ruleReadBean;

    public RuleListSearchService() throws CpxIllegalArgumentException {
        super(RuleListAttributes.instance());
        try {
            Context ctx = new InitialContext();
            ruleReadBean = (RuleReadServicBeanLocal) ctx.lookup("java:module/RuleReadServiceBean");
        } catch (NamingException ex) {
            LOG.log(Level.SEVERE, "Can not find RuleReadBean search may not work properly!", ex);
        }
    }

    @Override
    protected void prepareSql(boolean pIsLocal, boolean pIsShowAllReminder, List<String> pColumns, List<String> pJoin, List<String> pWhere, List<String> pOrder, int pLimitFrom, int pLimitTo, final QueryType pQueryType) {
        pJoin.add("INNER JOIN T_CASE_DETAILS ON T_CASE_DETAILS.T_CASE_ID = T_CASE.ID");
        pJoin.add("INNER JOIN T_GROUPING_RESULTS ON T_GROUPING_RESULTS.T_CASE_DETAILS_ID = T_CASE_DETAILS.ID");
        pJoin.add("INNER JOIN T_CHECK_RESULT ON T_CHECK_RESULT.T_GROUPING_RESULTS_ID = T_GROUPING_RESULTS.ID");
        pJoin.add("INNER JOIN T_ROLE_2_CHECK ON  T_ROLE_2_CHECK.T_CHECK_RESULT_ID = T_CHECK_RESULT.ID");
        pJoin.add("LEFT JOIN VIEW_CASE_2_RULE_SELECTION ON T_CHECK_RESULT.ID = VIEW_CASE_2_RULE_SELECTION.T_CHECKRESULT_ID"
                + " AND T_CASE.ID = VIEW_CASE_2_RULE_SELECTION.T_CASE_ID");
//        pJoin.add("LEFT JOIN T_GROUPING_RESULTS on T_CHECK_RESULT.T_GROUPING_RESULTS_ID = T_GROUPING_RESULTS.ID");
//        pJoin.add("LEFT JOIN T_CASE_DETAILS ON T_GROUPING_RESULTS.T_CASE_DETAILS_ID = T_CASE_DETAILS.ID");
//        pJoin.add("LEFT JOIN T_CHECK_RESULT ON T_CHECK_RESULT.ID = T_CASE_DETAILS.T_CASE_ID");
        
        //CPX-2104 Add join of case suppl fees to query if fees should be considered as showing
        if (isFiltered(RuleListAttributes.caseSupplFees)) {
            pJoin.add("LEFT JOIN T_CASE_OPS_GROUPED ON T_CASE_OPS_GROUPED.T_GROUPING_RESULTS_ID = T_GROUPING_RESULTS.ID");
            pJoin.add("LEFT JOIN T_CASE_SUPPL_FEE ON T_CASE_SUPPL_FEE.T_CASE_OPS_GROUPED_ID = T_CASE_OPS_GROUPED.ID");
        }

        pWhere.add("(T_CASE_DETAILS.ACTUAL_FL = 1 OR T_CASE_DETAILS.ACTUAL_FL IS NULL)");
        pWhere.add("(T_CASE_DETAILS.LOCAL_FL = " + (pIsLocal ? "1" : "0") + " OR T_CASE_DETAILS.LOCAL_FL IS NULL)");

        if (grouper.equals(GDRGModel.AUTOMATIC)) {
            pWhere.add("T_GROUPING_RESULTS.GRPRES_IS_AUTO_FL = 1");
        } else {
            pWhere.add("T_GROUPING_RESULTS.GRPRES_IS_AUTO_FL = 0");
            pWhere.add("T_GROUPING_RESULTS.MODEL_ID_EN = '" + grouper.name() + "'");

        }
        pWhere.add("(T_ROLE_2_CHECK.USER_ROLE_ID = " + ClientManager.getCurrentCpxRoleId() + ")");
//        pWhere.add("T_CHECK_RESULT.CRGR_ID in ()");

    }

    @Override
    protected RuleListItemDTO fillDto(Map<String, Object> pItems, Map<String, String> pUniqueDatabaseFields) {
        RuleListItemDTO dto = new RuleListItemDTO();

        String value = null;
        Number x = null;
        Number number = null;

        Number ruleId = (Number) pItems.get(pUniqueDatabaseFields.get(RuleListAttributes.crgrId));

        if (ruleId != null) {
            dto.setCrgrId(ruleId.intValue());
            Date admDate = (Date) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdAdmissionDate));
            if (admDate != null) {
                dto.setCsdAdmissionDate(admDate);
                //long start = System.currentTimeMillis();
                CrgRules rule = ruleReadBean.getRule2Id(ruleId.longValue());
                if (rule != null) {
                    dto.setRuleDescription(rule.getCrgrCaption());
                    dto.setRuleSuggestion(rule.getCrgrSuggText());
                    dto.setTypeText(rule.getCrgrRuleErrorType().getInternalKey());
                    dto.setXRuleNumber(rule.getCrgrNumber());
                    dto.setCrgrCaption(rule.getCrgrCaption());
                    dto.setCrgrCategory(rule.getCrgrCategory());

                }
                //long end = System.currentTimeMillis();
                //System.out.println("Getting Rool finished it took "+(end - start) / 1000d+" Seconds");
                //  dto.setRuleDescription(rule.m_ruleText);
            }
        }

        //ChkCwSimulDiff
        x = (Number) pItems.get(pUniqueDatabaseFields.get(RuleListAttributes.chkCwSimulDiff));
        if (x == null) {
            dto.setChkCwSimulDiff(null);
        } else {
            dto.setChkCwSimulDiff(x.doubleValue());
        }

        //DRG
        value = (String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csDrg));
        dto.setCsDrg(value);

        //Case Number
        value = (String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csCaseNumber));
        dto.setCsCaseNumber(value);

        //Cw Effective
        x = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.cwEffective));
        if (x == null) {
            dto.setCwEffective(null);
        } else {
            dto.setCwEffective(x.doubleValue());
        }

        //VWD (Verweildauer)           
        number = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdLos));
        if (number != null) {
            dto.setCsdLos(toLong(number));
        }

        //Beatmung
        number = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdHmv));
        if (number != null) {
            dto.setCsdHmv(toInt(number));
        }

        //Alter
        number = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdAgeYears));
        if (number != null) {
            dto.setCsdAgeYears(toInt(number));
        }

        //Fallstatus
        dto.setCsStatusEn((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csStatusEn)));

        //T_CASE_ID
        dto.setId(toLong((Number) pItems.get(pUniqueDatabaseFields.get(RuleListAttributes.id))));

        //T_CHECK_RESULT_ID
        dto.setCheckResultId(toLong((Number) pItems.get(pUniqueDatabaseFields.get(RuleListAttributes.checkResultId))));
        
        Number sel = (Number)pItems.get(pUniqueDatabaseFields.get(RuleListAttributes.ruleSelected));

        Boolean bValue = sel == null?false:sel.intValue() == 1;
        dto.setRuleSelected(bValue);

        return dto;
    }

    @Override
    protected String ruleFilterSubQuery(String pAttributeValue) {
        return pAttributeValue;
    }

    @Override
    public SearchResult<RuleListItemDTO> getAllWithCriteriaForFilter(final boolean pIsLocal, final GDRGModel pGrouperModel, final int pPage, final int pFetchSize, Map<ColumnOption, List<FilterOption>> pFilterOptionMap) {
// for rule fields we have to find rule ids, and than select the results from t_check_result with the rearranged    pFilterOptionMap    

        Set<ColumnOption> columnOptions = pFilterOptionMap.keySet();
        Map<String, List<String>> ruleSearchOptions = new HashMap<>();
        ColumnOption ridOption = null;
        for (ColumnOption opt : columnOptions) {
            if (RuleListAttributes.getRuleFieldNames().contains(opt.getDisplayName())) {
                List<FilterOption> fOpts = pFilterOptionMap.get(opt);

                for (FilterOption fOpt : fOpts) {
                    if (fOpt.getValue() != null && !fOpt.getValue().isEmpty()) {
                        List<String> lst = ruleSearchOptions.get(fOpt.field);
                        if (lst == null) {
                            lst = new ArrayList<>();
                            ruleSearchOptions.put(RuleListAttributes.getRuleFieldsNames2Field().get(opt.getDisplayName()), lst);
                        }
                        lst.add(fOpt.getValue());

                    }
                }

            }
            if (opt.getDisplayName().equals("Regel ID")) {
                ridOption = opt;
            }
        }
        if (!ruleSearchOptions.isEmpty()) {
            List<String> rids = ruleReadBean.getRids4SearchOptions(ruleSearchOptions);
            if (ridOption != null && rids != null && !rids.isEmpty()) {
                List<FilterOption> ridOptions = pFilterOptionMap.get(ridOption);

                FilterOption opt = ridOptions.get(0);
                opt.setValue(String.join(",", rids));
                pFilterOptionMap.put(ridOption, ridOptions);
            } else {
                return new SearchResult<>();

            }
        }

        try {
            return super.getAllWithCriteriaForFilter(pIsLocal, pGrouperModel, pPage, pFetchSize, pFilterOptionMap);
        } catch (IOException | InterruptedException ex) {
            LOG.log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt();
            return new SearchResult<>();
        }
    }

}
