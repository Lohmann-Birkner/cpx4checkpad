/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.shared.dto.QuotaListItemDTO;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.FilterOption;
import de.lb.cpx.shared.filter.enums.QuotaListAttributes;
import de.lb.cpx.shared.filter.enums.SearchListAttributes;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;

/**
 *
 * @author niemeier
 */
@Stateless
public class QuotaListSearchService extends AbstractSearchService<QuotaListItemDTO> {
//    @EJB(name = "RuleReadBean",beanInterface = RuleReadBeanLocal.class)
//    RuleReadBean ruleBean;

    private static final Logger LOG = Logger.getLogger(QuotaListSearchService.class.getName());

    public QuotaListSearchService() throws CpxIllegalArgumentException {
        super(QuotaListAttributes.instance());
    }

    @Override
    protected void prepareSql(boolean pIsLocal, boolean pIsShowAllReminder, List<String> pColumns, List<String> pJoin, List<String> pWhere, List<String> pOrder, int pLimitFrom, int pLimitTo, final QueryType pQueryType) {
        pJoin.add("INNER JOIN T_PATIENT ON T_PATIENT.ID = T_CASE.T_PATIENT_ID ");
        pJoin.add("INNER JOIN T_CASE_DETAILS ON T_CASE_DETAILS.T_CASE_ID = T_CASE.ID");
        //pJoin.add("INNER JOIN T_GROUPING_RESULTS ON T_GROUPING_RESULTS.T_CASE_DETAILS_ID = T_CASE_DETAILS.ID");
        //pJoin.add("INNER JOIN T_CHECK_RESULT ON T_CHECK_RESULT.T_GROUPING_RESULTS_ID = T_GROUPING_RESULTS.ID");
        //pJoin.add("INNER JOIN T_ROLE_2_CHECK ON  T_ROLE_2_CHECK.T_CHECK_RESULT_ID = T_CHECK_RESULT.ID");
//        pJoin.add("LEFT JOIN T_GROUPING_RESULTS on T_CHECK_RESULT.T_GROUPING_RESULTS_ID = T_GROUPING_RESULTS.ID");
//        pJoin.add("LEFT JOIN T_CASE_DETAILS ON T_GROUPING_RESULTS.T_CASE_DETAILS_ID = T_CASE_DETAILS.ID");
//        pJoin.add("LEFT JOIN T_CHECK_RESULT ON T_CHECK_RESULT.ID = T_CASE_DETAILS.T_CASE_ID");

//        pJoin.add("LEFT JOIN T_WM_RISK ON T_WM_RISK.T_CASE_DETAILS_ID = T_CASE_DETAILS.ID ");
//        pJoin.add("LEFT JOIN T_WM_REQUEST ON T_WM_REQUEST.ID = T_WM_RISK.T_WM_REQUEST_ID ");
//        pJoin.add("LEFT JOIN T_WM_REQUEST_MDK ON T_WM_REQUEST_MDK.ID = T_WM_REQUEST.ID ");
//        pJoin.add("LEFT JOIN T_WM_PROCESS ON T_WM_PROCESS.ID = T_WM_REQUEST.T_WM_PROCESS_HOSPITAL_ID ");
//
        //DRG 
//        if (visibleColumnOptions.contains(QuotaListAttributes.csDrg)) {
        //pJoin.add("LEFT JOIN VIEW_GRPRES_"+grouperViewSuffix+" ON VIEW_GRPRES_"+grouperViewSuffix+".T_CASE_ICD_ID = T_CASE_DETAILS.ID_ICD_HDX");
        pJoin.add("INNER JOIN VIEW_GRPRES_" + grouper.name() + " VIEW_GRPRES ON VIEW_GRPRES.T_CASE_DETAILS_ID = T_CASE_DETAILS.ID");
//        }

// AGe 20200812:
        pJoin.add("LEFT JOIN T_WM_PROCESS_T_CASE ON T_WM_PROCESS_T_CASE.T_CASE_ID = T_CASE.ID ");
        pJoin.add("LEFT JOIN T_WM_PROCESS ON T_WM_PROCESS.ID = T_WM_PROCESS_T_CASE.T_WM_PROCESS_ID");
        pJoin.add("LEFT JOIN T_WM_REQUEST ON T_WM_REQUEST.T_WM_PROCESS_HOSPITAL_ID= T_WM_PROCESS.ID");
        pJoin.add("LEFT JOIN T_WM_REQUEST_MDK on T_WM_REQUEST_MDK.id = T_WM_REQUEST.ID");
        pJoin.add("LEFT JOIN T_WM_RISK ON T_WM_RISK.T_CASE_DETAILS_ID = T_CASE_DETAILS.ID");
        
        if (visibleColumnOptions.contains(QuotaListAttributes.processResult)) {
            pJoin.add("LEFT JOIN T_WM_PROCESS_HOSPITAL ON T_WM_PROCESS_HOSPITAL.ID = T_WM_PROCESS.ID");
            pJoin.add("LEFT JOIN T_WM_PROCESS_HOSPITAL_FINALIS ON T_WM_PROCESS_HOSPITAL_FINALIS.T_WM_PROCESS_HOSPITAL_ID = T_WM_PROCESS_HOSPITAL.ID");
        }

        

        if (isFiltered(QuotaListAttributes.caseSupplFees)) {
            pJoin.add("LEFT JOIN T_CASE_OPS_GROUPED ON T_CASE_OPS_GROUPED.T_GROUPING_RESULTS_ID = VIEW_GRPRES.ID");
            pJoin.add("LEFT JOIN T_CASE_SUPPL_FEE ON T_CASE_SUPPL_FEE.T_CASE_OPS_GROUPED_ID = T_CASE_OPS_GROUPED.ID");
        }
        
        pWhere.add("(T_CASE_DETAILS.ACTUAL_FL = 1 OR T_CASE_DETAILS.ACTUAL_FL IS NULL)");
        pWhere.add("(T_CASE_DETAILS.LOCAL_FL = " + (pIsLocal ? "1" : "0") + " OR T_CASE_DETAILS.LOCAL_FL IS NULL)");
//        if (grouper.equals(GDRGModel.AUTOMATIC)) {
//            pWhere.add("T_GROUPING_RESULTS.GRPRES_IS_AUTO_FL = 1");
//        } else {
//            pWhere.add("T_GROUPING_RESULTS.GRPRES_IS_AUTO_FL = 0");
//            pWhere.add("T_GROUPING_RESULTS.MODEL_ID_EN = '" + grouper.name() + "'");
//        }
//        pWhere.add("(T_ROLE_2_CHECK.USER_ROLE_ID = " + ClientManager.getCurrentCpxRoleId() + ")");
//        pWhere.add("T_CASE.CS_CASE_TYPE_EN = 'DRG'");
        pWhere.add("T_CASE.CANCEL_FL = 0");
        pWhere.add("T_CASE.CS_BILLING_DATE IS NOT NULL");
        pWhere.add("T_CASE_DETAILS.ADMISSION_REASON_12_EN IN ('01', '02')");
        pWhere.add("T_CASE_DETAILS.DISCHARGE_DATE IS NOT NULL");
        pWhere.add("(T_WM_REQUEST.REQUEST_TYPE IS NULL OR T_WM_REQUEST.REQUEST_TYPE = 2)");
//        pWhere.add("T_CHECK_RESULT.CRGR_ID in ()");

        if (!QueryType.RAW.equals(pQueryType)) {
            pJoin.add("LEFT JOIN T_LOCK ON T_LOCK.CASE_ID = T_CASE.ID ");
        }
        // CPX-565 : 23.10.2017 RSH- Anzeige der entlassenden FA in der Vorgangsliste.
        if (visibleColumnOptions.contains(SearchListAttributes.depDischarge)
                // || visibleColumnOptions.contains(SearchListAttributes.depDischargeName)
                || visibleColumnOptions.contains(SearchListAttributes.depDischarge301)
                || visibleColumnOptions.contains(SearchListAttributes.depAdmission)
                || visibleColumnOptions.contains(SearchListAttributes.depAdmission301)
                || visibleColumnOptions.contains(SearchListAttributes.depTreating)
                || visibleColumnOptions.contains(SearchListAttributes.depTreating301)) {
            pJoin.add("LEFT JOIN VIEW_DEPARTMENT ON T_CASE_DETAILS.ID = VIEW_DEPARTMENT.T_CASE_DETAILS_ID  AND VIEW_DEPARTMENT.LOCAL_FL=" + (pIsLocal ? "1" : "0"));
        }
    }

    @Override
    protected QuotaListItemDTO fillDto(Map<String, Object> pItems, Map<String, String> pUniqueDatabaseFields) {
        QuotaListItemDTO dto = new QuotaListItemDTO();

        //T_CASE_ID
        dto.setId(toLong((Number) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.id))));
        dto.setRequestId(toLong((Number) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.requestId))));
        dto.setProcessId(toLong((Number) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.processId))));

        if (visibleColumnOptions.contains(QuotaListAttributes.csHospitalIdent)) {
            dto.setCsHospitalIdent((String) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.csHospitalIdent)));
        }

        if (visibleColumnOptions.contains(QuotaListAttributes.csCaseNumber)) {
            dto.setCsCaseNumber((String) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.csCaseNumber)));
        }

        if (visibleColumnOptions.contains(QuotaListAttributes.csDrg)) {
            dto.setCsDrg((String) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.csDrg)));
        }
        if (visibleColumnOptions.contains(QuotaListAttributes.icdCode)) { //ICDC_CODE
            //Haupt-ICD
            dto.setIcdCode((String) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.icdCode)));
        }
        if (visibleColumnOptions.contains(QuotaListAttributes.csdAdmReason12En)) {
            dto.setCsdAdmReason12En((String) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.csdAdmReason12En)));
        }

        if (visibleColumnOptions.contains(QuotaListAttributes.csdAdmissionDate)) {
            dto.setCsdAdmissionDate((Date) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.csdAdmissionDate)));
        }

        if (visibleColumnOptions.contains(QuotaListAttributes.csdDischargeDate)) {
            dto.setCsdDischargeDate((Date) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.csdAdmissionDate)));
        }

        if (visibleColumnOptions.contains(QuotaListAttributes.csBillingDate)) {
            dto.setCsBillingDate((Date) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.csBillingDate)));
        }

        if (visibleColumnOptions.contains(QuotaListAttributes.csBillingQuarter)) {
            dto.setCsBillingQuarter(toInt((Number) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.csBillingQuarter))));
        }

        if (visibleColumnOptions.contains(QuotaListAttributes.riskPercentTotal)) {
            dto.setRiskPercentTotal(toInt((Number) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.riskPercentTotal))));
        }

        if (visibleColumnOptions.contains(QuotaListAttributes.riskValueTotal)) {
            dto.setRiskValueTotal(toDouble((Number) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.riskValueTotal))));
        }

        if (visibleColumnOptions.contains(QuotaListAttributes.riskPlaceOfReg)) {
            dto.setRiskPlaceOfReg((String) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.riskPlaceOfReg)));
        }

        if (visibleColumnOptions.contains(QuotaListAttributes.insInsCompany)) {
            //?
            dto.setInsInsCompany((String) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.insInsCompany)));
        }
        if (visibleColumnOptions.contains(QuotaListAttributes.insInsCompanyName)) {
            dto.setInsInsCompanyName((String) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.insInsCompanyName)));
        }
        if (visibleColumnOptions.contains(QuotaListAttributes.insInsCompanyShortName)) {
            dto.setInsInsCompanyShortName((String) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.insInsCompanyShortName)));
        }

        if (visibleColumnOptions.contains(SearchListAttributes.lock)) {
            dto.setLockSince((Date) pItems.get("LOCK_SINCE"));
            dto.setLockExpires((Date) pItems.get("LOCK_EXPIRES"));
            dto.setLockUserName((String) pItems.get("LOCK_USER_NAME"));
            dto.setLock(toBool(pItems.get(pUniqueDatabaseFields.get(SearchListAttributes.lock))));
        }

        // CPX-565 : 23.10.2017 RSH- Anzeige der entlassenden FA in der Vorgangsliste.
        if (visibleColumnOptions.contains(QuotaListAttributes.depDischarge)) {
            dto.setDepDischarge((String) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.depDischarge)));
        }
        if (visibleColumnOptions.contains(QuotaListAttributes.depDischarge301)) {
            dto.setDepDischarge301((String) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.depDischarge301)));
        }
        if (visibleColumnOptions.contains(QuotaListAttributes.depAdmission)) {
            dto.setDepAdmission((String) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.depAdmission)));
        }
        if (visibleColumnOptions.contains(QuotaListAttributes.depAdmission301)) {
            dto.setDepAdmission301((String) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.depAdmission301)));
        }
        if (visibleColumnOptions.contains(QuotaListAttributes.depTreating)) {
            dto.setDepTreating((String) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.depTreating)));
        }
        if (visibleColumnOptions.contains(QuotaListAttributes.depTreating301)) {
            dto.setDepTreating301((String) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.depTreating301)));
        }
        if (visibleColumnOptions.contains(QuotaListAttributes.startAudit)) {
            dto.setStartAudit((Date) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.startAudit)));
        }
        if (visibleColumnOptions.contains(QuotaListAttributes.mdkStartAuditQuarter)) {
            dto.setMdkStartAuditQuarter(toInt((Number) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.mdkStartAuditQuarter))));
        }
        if (visibleColumnOptions.contains(QuotaListAttributes.mdkStartAuditExtendedDate)) {
            dto.setMdkStartAuditExtendedDate((Date) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.mdkStartAuditExtendedDate)));
        }
        if (visibleColumnOptions.contains(QuotaListAttributes.reportDate)) {
            dto.setReportDate((Date) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.reportDate)));
        }
        if (visibleColumnOptions.contains(QuotaListAttributes.mdkReportCreationQuarter)) {
            dto.setMdkReportCreationQuarter(toInt((Number) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.mdkReportCreationQuarter))));
        }
        if (visibleColumnOptions.contains(QuotaListAttributes.processTopic)) {
            dto.setProcessTopic(toLong((Number) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.processTopic))));
        }
        if (visibleColumnOptions.contains(QuotaListAttributes.processResult)) {
            dto.setProcessResult(toLong((Number) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.processResult))));
        }
        if (visibleColumnOptions.contains(QuotaListAttributes.wmState)) {
            dto.setWmState(toInt((String) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.wmState))));
        }
        if (visibleColumnOptions.contains(QuotaListAttributes.requestState)) {
            dto.setRequestState(toLong((Number) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.requestState))));
        }
        if (visibleColumnOptions.contains(QuotaListAttributes.workflowNumber)) {
            dto.setWorkflowNumber(toLong((Number) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.workflowNumber))));
        }
        if (visibleColumnOptions.contains(QuotaListAttributes.insuranceRecivedBillDate)) {
            dto.setInsuranceRecivedBillDate((Date) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.insuranceRecivedBillDate)));
        }
        if (visibleColumnOptions.contains(QuotaListAttributes.mdk)) {
            dto.setMdk(toLong((Number) pItems.get(pUniqueDatabaseFields.get(QuotaListAttributes.mdk))));
        }

        return dto;
    }

    @Override
    public SearchResult<QuotaListItemDTO> getAllWithCriteriaForFilter(final boolean pIsLocal, final GDRGModel pGrouperModel, final int pPage, final int pFetchSize, Map<ColumnOption, List<FilterOption>> pFilterOptionMap) {
// for rule fields we have to find rule ids, and than select the results from t_check_result with the rearranged    pFilterOptionMap    

        try {
            return super.getAllWithCriteriaForFilter(pIsLocal, pGrouperModel, pPage, pFetchSize, pFilterOptionMap);
        } catch (IOException | InterruptedException ex) {
            LOG.log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt();
            return new SearchResult<>();
        }
    }

}
