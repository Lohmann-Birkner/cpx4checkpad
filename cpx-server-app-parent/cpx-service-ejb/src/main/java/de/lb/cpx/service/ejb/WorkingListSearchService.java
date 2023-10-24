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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.commonDB.dao.CDrgCatalogDao;
import de.lb.cpx.server.commonDB.model.CDrgCatalog;
import static de.lb.cpx.service.ejb.AbstractSearchService.toInt;
import static de.lb.cpx.service.ejb.AbstractSearchService.toLong;
import de.lb.cpx.shared.dto.WorkingListItemDTO;
import de.lb.cpx.shared.filter.enums.SearchListAttributes;
import de.lb.cpx.shared.filter.enums.WorkingListAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Dirk Niemeier
 */
@Stateless
public class WorkingListSearchService extends AbstractSearchService<WorkingListItemDTO> {

    @Inject
    private CDrgCatalogDao catalogDao;

    private List<CDrgCatalog> drgCatalog = null;

    public WorkingListSearchService() throws CpxIllegalArgumentException {
        super(WorkingListAttributes.instance());
    }

    @Override
    protected void prepareSql(boolean pIsLocal, final boolean pIsAllReminder, List<String> pColumns, List<String> pJoin, List<String> pWhere, List<String> pOrder, int pLimitFrom, int pLimitTo, final QueryType pQueryType) {

        List<String> joinTmp = new ArrayList<>();
        if (QueryType.RAW.equals(pQueryType)) {
            pColumns.add("T_PATIENT.ID T_PATIENT_ID");
            pColumns.add("T_PATIENT_DETAILS.ID T_PATIENT_DETAILS_ID");
        } else {
            if (pQueryType == null || QueryType.NORMAL.equals(pQueryType)) {
                pColumns.add("T_LOCK.SINCE LOCK_SINCE");
                pColumns.add("T_LOCK.EXPIRES LOCK_EXPIRES");
                pColumns.add("T_LOCK.USER_NAME LOCK_USER_NAME");
            } else {
                pColumns.add("T_CASE.ID");

            }
        }

        if (visibleColumnOptions.contains(WorkingListAttributes.rules)) { // always FALSE?
        }
        if (isFiltered(WorkingListAttributes.rules)) {
            //"LEFT JOIN T_CASE_DETAILS ON T_CASE_DETAILS.T_CASE_ID = T_CASE.ID AND (T_CASE_DETAILS.ACTUAL_FL = 1 OR T_CASE_DETAILS.ACTUAL_FL IS NULL) AND (T_CASE_DETAILS.LOCAL_FL = " + (pIsLocal ? "1" : "0") + " OR T_CASE_DETAILS.LOCAL_FL IS NULL)"
//            pJoin.add("LEFT JOIN T_CHECK_RESULT ON T_GROUPING_RESULTS.ID = T_CHECK_RESULT.T_GROUPING_RESULTS_ID");

//                return "EXISTS(SELECT 1 FROM T_CHECK_RESULT \n"
//                        + " LEFT JOIN T_GROUPING_RESULTS ON T_CHECK_RESULT.T_GROUPING_RESULTS_ID = T_GROUPING_RESULTS.ID  \n"
//                        + " WHERE " + pAttributeValue + " AND T_GROUPING_RESULTS.T_CASE_DETAILS_ID = T_CASE_DETAILS.ID) \n";
        }

        if (visibleColumnOptions.contains(WorkingListAttributes.icdCode)) { //ICDC_CODE
            //Haupt-ICD
            //pJoin.add("LEFT JOIN T_CASE_ICD ON T_CASE_ICD.ID = T_CASE_DETAILS.ID_ICD_HDX ");
            //pJoin.add("LEFT JOIN " + getMainIcdSql(isLocal) + " VIEW_CASE_MAIN_ICD ON VIEW_CASE_MAIN_ICD.HOSD_ID = T_CASE_DETAILS.ID ");
        }
//        if (visibleColumnOptions.contains(WorkingListAttributes.cwNegative)) {
//            // pJoin.add("LEFT JOIN VIEW_CW_NEGATIVE ON VIEW_CW_NEGATIVE.ID = T_CASE.ID"); needs work
//        }
//        if (visibleColumnOptions.contains(WorkingListAttributes.cwPositive)) {
//            //pJoin.add("LEFT JOIN VIEW_CW_POSITIVE ON VIEW_CW_POSITIVE.ID = T_CASE.ID "); needs work 
//        }
//         if (visibleColumnOptions.contains(WorkingListAttributes.dCWmin)||visibleColumnOptions.contains(WorkingListAttributes.dCWmax)) {
////             pJoin.add("LEFT JOIN T_GROUPING_RESULTS ON T_GROUPING_RESULTS.HOSD_ID = T_CASE_DETAILS.ID");
////           pJoin.add("LEFT JOIN T_ROLE_2_RESULT ON T_GROUPING_RESULTS.ID = T_ROLE_2_RESULT.GRPRES_ID"); 
////    pJoin.add("LEFT JOIN VIEW_GRPRES_"+grouperViewSuffix+" ON T_ROLE_2_RESULT.GRPRES_ID = VIEW_GRPRES_"+grouperViewSuffix+".ID ");
// pJoin.add("LEFT JOIN VIEW_CW ON VIEW_CW.GRPRES_ID = VIEW_GRPRES_"+grouperViewSuffix+".ID ");
// pWhere.add("(T_CASE_DETAILS.CSD_IS_LOCAL_FL = " + (pIsLocal ? "1" : "0") + " OR T_CASE_DETAILS.CSD_IS_LOCAL_FL IS NULL)");
//
//        }
        if (visibleColumnOptions.contains(WorkingListAttributes.csCaseNumber) && !visibleColumnOptions.contains(WorkingListAttributes.isCancel)) {
            pWhere.add("T_CASE.CANCEL_FL=0");
        }

        //CPX-529 Add join of case fees to query if fees should be considered as showing
        if (isFiltered(WorkingListAttributes.caseFees)) {
            pJoin.add("LEFT JOIN T_CASE_FEE ON T_CASE_DETAILS.ID = T_CASE_FEE.T_CASE_DETAILS_ID");
        }
        //CPX-1572 Add join of case suppl fees to query if fees should be considered as showing
//        if (isFiltered(WorkingListAttributes.caseSupplFees)) {
//            pJoin.add("LEFT JOIN T_GROUPING_RESULTS ON T_GROUPING_RESULTS.T_CASE_DETAILS_ID = T_CASE_DETAILS.ID");
//            pJoin.add("LEFT JOIN T_CASE_OPS_GROUPED ON T_CASE_OPS_GROUPED.T_GROUPING_RESULTS_ID = T_GROUPING_RESULTS.ID");
//            pJoin.add("LEFT JOIN T_CASE_SUPPL_FEE ON T_CASE_SUPPL_FEE.T_CASE_OPS_GROUPED_ID = T_CASE_OPS_GROUPED.ID");
//        }
        //AWi-20170616-CPX-551
        //added filter for secondary diagnoses
        if (isFiltered(WorkingListAttributes.caseIcds) || isFiltered(WorkingListAttributes.caseOpses)
                || isFiltered(WorkingListAttributes.departmentMd) || isFiltered(WorkingListAttributes.admDiagnosis) || isFiltered(WorkingListAttributes.hosDiagnosis)) {

//            pJoin.add("LEFT JOIN T_CASE_DEPARTMENT ON T_CASE_DETAILS.ID = T_CASE_DEPARTMENT.HOSD_ID");
            if (isFiltered(WorkingListAttributes.caseIcds) || isFiltered(WorkingListAttributes.departmentMd)
                    || isFiltered(WorkingListAttributes.admDiagnosis) || isFiltered(WorkingListAttributes.hosDiagnosis)) {
//                pJoin.add("LEFT JOIN T_CASE_ICD ON T_CASE_DEPARTMENT.ID = T_CASE_ICD.T_CASE_DEPARTMENT_ID");
            }
            if (isFiltered(WorkingListAttributes.caseIcds)) {
//                pWhere.add("(T_CASE_ICD.MAIN_DIAG_CASE_FL = 0)");
//                pSubQuery.add("(SELECT * FROM T_CASE_ICD WHERE "+ createWhereString(, , )+ "AND T_CASE_ICD.MAIN_DIAG_CASE_FL = 0)");
            }
            if (isFiltered(WorkingListAttributes.caseOpses)) {
//                pJoin.add("LEFT JOIN T_CASE_OPS ON T_CASE_DEPARTMENT.ID = T_CASE_OPS.T_CASE_DEPARTMENT_ID");
            }
            if (isFiltered(WorkingListAttributes.departmentMd)) {
//                pWhere.add("(T_CASE_ICD.MAIN_DIAG_DEP_FL = 1)");
            }
            if (isFiltered(WorkingListAttributes.hosDiagnosis)) {
//                pWhere.add("(T_CASE_ICD.ICDC_TYPE_EN = 1)");
            }
            if (isFiltered(WorkingListAttributes.admDiagnosis)) {
//                pWhere.add("(T_CASE_ICD.ICDC_TYPE_EN = 2)");
            }
        }
        if (QueryType.RAW.equals(pQueryType)
                || visibleColumnOptions.contains(SearchListAttributes.insNumber) || visibleColumnOptions.contains(WorkingListAttributes.patDateOfBirth) || visibleColumnOptions.contains(WorkingListAttributes.patFirstName) || visibleColumnOptions.contains(WorkingListAttributes.patGenderEn) || visibleColumnOptions.contains(WorkingListAttributes.patName)
                || visibleColumnOptions.contains(WorkingListAttributes.patNumber) || visibleColumnOptions.contains(WorkingListAttributes.patSecondName) || visibleColumnOptions.contains(WorkingListAttributes.patdZipcode) || visibleColumnOptions.contains(WorkingListAttributes.insInsCompany) /*|| visibleColumnOptions.contains(WorkingListAttributes.insNumber)*/) {
            joinTmp.add("INNER JOIN T_PATIENT ON T_PATIENT.ID = T_CASE.T_PATIENT_ID ");
            joinTmp.add("LEFT JOIN T_PATIENT_DETAILS ON T_PATIENT_DETAILS.T_PATIENT_ID = T_PATIENT.ID AND (T_PATIENT_DETAILS.PATD_IS_ACTUAL_FL = 1 OR T_PATIENT_DETAILS.PATD_IS_ACTUAL_FL IS NULL)");
            //joinTmp.add("LEFT JOIN T_INSURANCE ON T_PATIENT.PAT_INS_ACT_ID = T_INSURANCE.ID ");
            //  joinTmp.add("LEFT JOIN T_INSURANCE ON T_INSURANCE.PATIENT_ID = T_PATIENT.ID ");
            // pWhere.add("(T_INSURANCE.INS_IS_ACTUAL_FL = 1 OR T_INSURANCE.INS_IS_ACTUAL_FL IS NULL)");
            //pWhere.add("(T_PATIENT_DETAILS.PATD_IS_ACTUAL_FL = 1 OR T_PATIENT_DETAILS.PATD_IS_ACTUAL_FL IS NULL)");
        }

        //DRG 
        if (visibleColumnOptions.contains(WorkingListAttributes.csDrg)
                || visibleColumnOptions.contains(WorkingListAttributes.pccl)
                || visibleColumnOptions.contains(WorkingListAttributes.cwEffective)
                || visibleColumnOptions.contains(WorkingListAttributes.grpresGroup)
                || visibleColumnOptions.contains(WorkingListAttributes.dCWmin)
                || visibleColumnOptions.contains(WorkingListAttributes.dCWmax)
                || visibleColumnOptions.contains(WorkingListAttributes.careCw)               
                || isFiltered(WorkingListAttributes.caseSupplFees)
                ||  visibleColumnOptions.contains(WorkingListAttributes.dRevenueMin) 
                ||  visibleColumnOptions.contains(WorkingListAttributes.dRevenueMax)
                ||  visibleColumnOptions.contains(WorkingListAttributes.dCareCWmin)
                ||  visibleColumnOptions.contains(WorkingListAttributes.dCareCWmax)) {
            //pJoin.add("LEFT JOIN VIEW_GRPRES_"+grouperViewSuffix+" ON VIEW_GRPRES_"+grouperViewSuffix+".T_CASE_ICD_ID = T_CASE_DETAILS.ID_ICD_HDX");
            pJoin.add("LEFT JOIN VIEW_GRPRES_" + grouper.name() + " VIEW_GRPRES ON VIEW_GRPRES.T_CASE_DETAILS_ID = T_CASE_DETAILS.ID");

            if (visibleColumnOptions.contains(WorkingListAttributes.dCWmin) 
                    || visibleColumnOptions.contains(WorkingListAttributes.dCWmax)
                    ||  visibleColumnOptions.contains(WorkingListAttributes.dRevenueMin) 
                    ||  visibleColumnOptions.contains(WorkingListAttributes.dRevenueMax)
                    ||  visibleColumnOptions.contains(WorkingListAttributes.dCareCWmin)
                    ||  visibleColumnOptions.contains(WorkingListAttributes.dCareCWmax)) {
                pJoin.add("LEFT JOIN VIEW_CW ON VIEW_CW.T_GROUPING_RESULTS_ID = VIEW_GRPRES.ID AND VIEW_CW.USER_ROLE_ID = " + ClientManager.getCurrentCpxRoleId());
//                pJoin.add("LEFT JOIN VIEW_GRPRES_" + grouperViewSuffix + " ON VIEW_GRPRES_" + grouperViewSuffix + ".T_CASE_DETAILS_ID = T_CASE_DETAILS.ID");
                //pWhere.add("(T_CASE_DETAILS.LOCAL_FL = " + (pIsLocal ? "1" : "0") + " OR T_CASE_DETAILS.LOCAL_FL IS NULL)");

            }
//             if (!visibleColumnOptions.contains(WorkingListAttributes.isCancel))
//                   pWhere.add("CANCEL_FL=0")  ;
//             else 
        if (isFiltered(WorkingListAttributes.caseSupplFees)) {
             pJoin.add("LEFT JOIN T_CASE_OPS_GROUPED ON T_CASE_OPS_GROUPED.T_GROUPING_RESULTS_ID = VIEW_GRPRES.ID");
            pJoin.add("LEFT JOIN T_CASE_SUPPL_FEE ON T_CASE_SUPPL_FEE.T_CASE_OPS_GROUPED_ID = T_CASE_OPS_GROUPED.ID");
        }

        }
        drgCatalog = catalogDao.getEntries(grouper == null ? 0 : grouper.getModelYear(), "de");

        //joinTmp.add("LEFT JOIN T_CASE_DETAILS ON T_CASE_DETAILS.ID = T_CASE.CS_" + (isLocal?"LOCAL":"EXTERN") + "_ACTUAL_ID ");
        joinTmp.add("LEFT JOIN T_CASE_DETAILS ON T_CASE_DETAILS.T_CASE_ID = T_CASE.ID AND (T_CASE_DETAILS.ACTUAL_FL = 1 OR T_CASE_DETAILS.ACTUAL_FL IS NULL) AND (T_CASE_DETAILS.LOCAL_FL = " + (pIsLocal ? "1" : "0") + " OR T_CASE_DETAILS.LOCAL_FL IS NULL)");

        if (!QueryType.RAW.equals(pQueryType)) {
            joinTmp.add("LEFT JOIN T_LOCK ON T_LOCK.CASE_ID = T_CASE.ID ");
        }
// CPX-565 : 23.10.2017 RSH- Anzeige der entlassenden FA in der Vorgangsliste.
        if (visibleColumnOptions.contains(SearchListAttributes.depDischarge)
                // || visibleColumnOptions.contains(SearchListAttributes.depDischargeName)
                || visibleColumnOptions.contains(SearchListAttributes.depDischarge301)
                || visibleColumnOptions.contains(SearchListAttributes.depAdmission)
                || visibleColumnOptions.contains(SearchListAttributes.depAdmission301)
                || visibleColumnOptions.contains(SearchListAttributes.depTreating)
                || visibleColumnOptions.contains(SearchListAttributes.depTreating301)) {
            joinTmp.add("LEFT JOIN VIEW_DEPARTMENT ON T_CASE_DETAILS.ID = VIEW_DEPARTMENT.T_CASE_DETAILS_ID  AND VIEW_DEPARTMENT.LOCAL_FL=" + (pIsLocal ? "1" : "0"));

        }
        for (int i = joinTmp.size() - 1; i >= 0; i--) {
            pJoin.add(0, joinTmp.get(i));
        }

    }

//    public int dropView(final String pViewName) {
//        int result = 0;
//        String viewName = (pViewName == null) ? "" : pViewName.trim();
//        if (viewName.isEmpty()) {
//            return result;
//        }
//        result = getSession().createSQLQuery(String.format("DROP VIEW %s", pViewName)).executeUpdate();
//        return result;
//    }
//
//    public int createView(final String pViewName, final String pQuery) {
//        int result = 0;
//        String viewName = (pViewName == null) ? "" : pViewName.trim();
//        String query = (pQuery == null) ? "" : pQuery.trim();
//        if (viewName.isEmpty()) {
//            return result;
//        }
//        dropView(pViewName);
//        if (query.isEmpty()) {
//            return result;
//        }
//        String view_query = String.format("CREATE VIEW %s AS \n %s", viewName, query);
//        SQLQuery viewQuery = getSession().createSQLQuery(view_query);
//        result = viewQuery.executeUpdate();
//        return result;
//    }
    @Override
    protected WorkingListItemDTO fillDto(final Map<String, Object> pItems, final Map<String, String> pUniqueDatabaseFields) {
        //WorkingListItemDTO dto = (WorkingListItemDTO) pDto;
        //System.out.println(pItems.get("ZEILEN"));
        Number number;
        WorkingListItemDTO dto = new WorkingListItemDTO();
        //for(int i = 0; i < items.length; i++) {

        if (visibleColumnOptions.contains(WorkingListAttributes.csDrg)) {
            String drg = (String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csDrg));
            dto.setCsDrg(drg);
        }
        // Field[] f = WorkingListAttributes.class.getDeclaredFields(); -- Mayebe later
        dto.setCsdId(toLong((Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdId))));
        if (visibleColumnOptions.contains(WorkingListAttributes.csCreationDate)) {
            //Anlegedatum
            dto.setCsCreationDate((Date) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csCreationDate)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.csCaseNumber)) {
            //Fallnummer
            dto.setCsCaseNumber((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csCaseNumber)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.isCancel)) {
            //CancelFl
            dto.setIsCancel(toBool(pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.isCancel))));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.csCaseTypeEn)) {
            //Fallart
            dto.setCsCaseTypeEn((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csCaseTypeEn)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.csStatusEn)) {
            //FS (Fallstatus)
            dto.setCsStatusEn((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csStatusEn)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.csdAdmissionDate)) {
            //Aufnahmedatum
            dto.setCsdAdmissionDate((Date) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdAdmissionDate)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.csdAdmissionDateWeekday)) {
            //Aufnahmedatum Wochentag
            number = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdAdmissionDateWeekday));
            if (number != null) {
                dto.setCsdAdmissionDateWeekday(toInt(number));
            }

        }
        if (visibleColumnOptions.contains(WorkingListAttributes.csdAdmissionWeight)) {
            //Aufnahmegewicht
            number = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdAdmissionWeight));
            if (number != null) {
                dto.setCsdAdmissionWeight(toInt(number));
            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.csdDischargeDate)) {
            //Entlassungsdatum
            dto.setCsdDischargeDate((Date) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdDischargeDate)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.csdDischargeDateWeekday)) {
            //Entlassungsdatum Wochentag
            number = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdDischargeDateWeekday));
            if (number != null) {
                dto.setCsdDischargeDateWeekday(toInt(number));
            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.csdAgeYears)) {
            //Alter Jahre
            number = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdAgeYears));
            if (number != null) {
                dto.setCsdAgeYears(toInt(number));
            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.csdAgeDays)) {
            //Alter Tage
            number = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdAgeDays));
            if (number != null) {
                dto.setCsdAgeDays(toInt(number));
            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.csdHmv)) {
            //Beatmung
            number = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdHmv));
            if (number != null) {
                dto.setCsdHmv(toInt(number));
            }

        }
        if (visibleColumnOptions.contains(WorkingListAttributes.csdLos)) {
            //VWD (Verweildauer)           
            number = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdLos));
            if (number != null) {
                dto.setCsdLos(toLong(number));
            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.csdLeave)) {
            //Urlaubstage
            number = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdLeave));
            if (number != null) {
                dto.setCsdLeave(toInt(number));
            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.csdLosAlteration)) {
            //Length of Stay
            number = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdLosAlteration));
            if (number != null) {
                dto.setCsdLosAlteration(toLong(number));
            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.csdAdmReason12En)) {
            //Aufnahmegrund
            dto.setCsdAdmReason12En((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdAdmReason12En)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.csdAdmReason34En)) {
            //Aufnahmegrund 02
            dto.setCsdAdmReason34En((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdAdmReason34En)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.csdAdmCauseEn)) {
            //Aufnahmeanlass
            dto.setCsdAdmCauseEn((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdAdmCauseEn)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.csdDisReason12En)) {
            //Entlassungsgrund
            dto.setCsdDisReason12En((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdDisReason12En)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.csHospitalIdent)) {
            //IKZ
            dto.setCsHospitalIdent((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csHospitalIdent)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.patNumber)) {
            //Pat.-Nummer
            dto.setPatNumber((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.patNumber)));
        }
        
        if (visibleColumnOptions.contains(WorkingListAttributes.csBillingDate)) {
            dto.setCsBillingDate((Date) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csBillingDate)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.pccl)) {
            number = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.pccl));
            if (number != null) {
                dto.setPccl(toInt(number));
            }
        }
        /*
    if (visibleColumnOptions.contains(WorkingListAttributes.csdAdmModEn)) {
      //?
      dto.set((String)items.get(""));
      continue;
    }
    if (visibleColumnOptions.contains(WorkingListAttributes.csdAdmLawEn)) {
      //?
      dto.set((String)items.get(""));
      continue;
    }
    if (visibleColumnOptions.contains(WorkingListAttributes.csdVersion)) {
      //Version
      dto.set((String)items.get(""));
      continue;
    }
    if (visibleColumnOptions.contains(WorkingListAttributes.csdDisReason3En)) {
      //?
      dto.set((String)items.get(""));
      continue;
    }
    if (visibleColumnOptions.contains(WorkingListAttributes.patTitle)) {
      //Patient Titel
      dto.set((String)items.get(""));
      continue;
    }
         */
        if (visibleColumnOptions.contains(WorkingListAttributes.icdCode)) { //ICDC_CODE
            //Haupt-ICD
            dto.setIcdCode((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.icdCode)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.patGenderEn)) {
            //Geschlecht
            dto.setPatGenderEn((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.patGenderEn)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.patdZipcode)) {
            //Einzugsgebiet
            dto.setPatdZipcode((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.patdZipcode)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.insInsCompany)) {
            //?
            dto.setInsInsCompany((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.insInsCompany)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.insInsCompanyName)) {
            dto.setInsInsCompanyName((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.insInsCompanyName)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.insInsCompanyShortName)) {
            dto.setInsInsCompanyShortName((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.insInsCompanyShortName)));
        }
//        if (visibleColumnOptions.contains(WorkingListAttributes.caseDepartments)) {
//            dto.setDepartment((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.caseDepartments)));
//        }
        if (visibleColumnOptions.contains(WorkingListAttributes.countSd)) {
            //Summe ND
            number = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.countSd));
            if (number != null) {
                dto.setCountSd(toInt(number));
            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.countProc)) {
            //Summe Proz.
            number = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.countProc));
            if (number != null) {
                dto.setCountProc(toInt(number));
            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.string1)) {
            dto.setString1((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.string1)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.string2)) {
            dto.setString2((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.string2)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.string3)) {
            dto.setString3((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.string3)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.string4)) {
            dto.setString4((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.string4)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.string5)) {
            dto.setString5((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.string5)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.string6)) {
            dto.setString6((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.string6)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.string7)) {
            dto.setString7((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.string7)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.string8)) {
            dto.setString8((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.string8)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.string9)) {
            dto.setString9((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.string9)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.string10)) {
            dto.setString10((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.string10)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.numeric1)) {
            Number numeric = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.numeric1));
            if (numeric != null) {
                dto.setNumeric1(toInt(numeric));
            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.numeric2)) {
            Number numeric = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.numeric2));
            if (numeric != null) {
                dto.setNumeric2(toInt(numeric));
            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.numeric3)) {
            Number numeric = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.numeric3));
            if (numeric != null) {
                dto.setNumeric3(toInt(numeric));
            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.numeric4)) {
            Number numeric = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.numeric4));
            if (numeric != null) {
                dto.setNumeric4(toInt(numeric));
            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.numeric5)) {
            Number numeric = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.numeric5));
            if (numeric != null) {
                dto.setNumeric5(toInt(numeric));
            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.numeric6)) {
            Number numeric = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.numeric6));
            if (numeric != null) {
                dto.setNumeric6(toInt(numeric));
            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.numeric7)) {
            Number numeric = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.numeric7));
            if (numeric != null) {
                dto.setNumeric7(toInt(numeric));
            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.numeric8)) {
            Number numeric = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.numeric8));
            if (numeric != null) {
                dto.setNumeric8(toInt(numeric));
            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.numeric9)) {
            Number numeric = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.numeric9));
            if (numeric != null) {
                dto.setNumeric9(toInt(numeric));
            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.numeric10)) {
            Number numeric = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.numeric10));
            if (numeric != null) {
                dto.setNumeric10(toInt(numeric));
            }
        }
//        if (visibleColumnOptions.contains(WorkingListAttributes.csdDouble1)) {
//            dto.setCsdDouble1((Double) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdDouble1)));
//        }
//        if (visibleColumnOptions.contains(WorkingListAttributes.csdDouble2)) {
//            dto.setCsdDouble2((Double) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdDouble2)));
//        }
//        if (visibleColumnOptions.contains(WorkingListAttributes.csdDouble3)) {
//            dto.setCsdDouble3((Double) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdDouble3)));
//        }
//        if (visibleColumnOptions.contains(WorkingListAttributes.csdDouble4)) {
//            dto.setCsdDouble4((Double) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdDouble4)));
//        }
//        if (visibleColumnOptions.contains(WorkingListAttributes.csdDouble5)) {
//            dto.setCsdDouble5((Double) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdDouble5)));
//        }
//        if (visibleColumnOptions.contains(WorkingListAttributes.csdDate1)) {
//            dto.setCsdDate1((Date) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdDate1)));
//        }
//        if (visibleColumnOptions.contains(WorkingListAttributes.csdDate2)) {
//            dto.setCsdDate2((Date) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdDate2)));
//        }
//        if (visibleColumnOptions.contains(WorkingListAttributes.csdDate3)) {
//            dto.setCsdDate3((Date) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdDate3)));
//        }
//        if (visibleColumnOptions.contains(WorkingListAttributes.csdDate4)) {
//            dto.setCsdDate4((Date) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdDate4)));
//        }
//        if (visibleColumnOptions.contains(WorkingListAttributes.csdDate5)) {
//            dto.setCsdDate5((Date) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdDate5)));
//        }
        if (visibleColumnOptions.contains(SearchListAttributes.lock)) {
            dto.setLockSince((Date) pItems.get("LOCK_SINCE"));
            dto.setLockExpires((Date) pItems.get("LOCK_EXPIRES"));
            dto.setLockUserName((String) pItems.get("LOCK_USER_NAME"));
            dto.setLock(toBool(pItems.get(pUniqueDatabaseFields.get(SearchListAttributes.lock))));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.cwPositive)) {
            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.cwPositive));
            if (value == null) {
                dto.setCwPositive(0.0);
            } else {
                dto.setCwPositive(value.doubleValue());
            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.cwNegative)) {
            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.cwNegative));
            if (value == null) {
                dto.setCwNegative(0.0);
            } else {
                dto.setCwNegative(value.doubleValue());
            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.cwEffective)) {
            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.cwEffective));
            if (value == null) {
                dto.setCwEffective(null);
            } else {
                dto.setCwEffective(value.doubleValue());
            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.cwCatalog) && visibleColumnOptions.contains(WorkingListAttributes.csDrg)) {
            dto.setCwCatalog(findCwForDrg(dto.getCsDrg()));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.grpresGroup)) {
            dto.setGrpresGroup((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.grpresGroup)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.dCWmin)) {
            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.dCWmin));
            if (value == null) {
                dto.setDCWmin(null);

            } else {
                dto.setDCWmin(value.doubleValue());

            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.dCWmax)) {
            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.dCWmax));
            if (value == null) {
                dto.setDCWmax(null);

            } else {

                dto.setDCWmax(value.doubleValue());
            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.dCareCWmin)) {
            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.dCareCWmin));
            if (value == null) {
                dto.setDCareCWmin(null);

            } else {
                dto.setDCareCWmin(value.doubleValue());

            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.dCareCWmax)) {
            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.dCareCWmax));
            if (value == null) {
                dto.setDCareCWmax(null);

            } else {

                dto.setDCareCWmax(value.doubleValue());
            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.dRevenueMin)) {
            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.dRevenueMin));
            if (value == null) {
                dto.setDRevenueMin(null);

            } else {
                dto.setDRevenueMin(value.doubleValue());

            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.dRevenueMax)) {
            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.dRevenueMax));
            if (value == null) {
                dto.setDRevenueMax(null);

            } else {

                dto.setDRevenueMax(value.doubleValue());
            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.careCw)) {
            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.careCw));
            if (value == null) {
                dto.setCareCw(null);

            } else {

                dto.setCareCw(value.doubleValue());
            }
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.csdVersionNumber)) {
            dto.setCsdVersionNumber(toLong((Number) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.csdVersionNumber))));
        }
        // CPX-565 : 23.10.2017 RSH- Anzeige der entlassenden FA in der Vorgangsliste.
        if (visibleColumnOptions.contains(WorkingListAttributes.depDischarge)) {

            dto.setDepDischarge((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.depDischarge)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.depDischarge301)) {

            dto.setDepDischarge301((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.depDischarge301)));
        }
//        if (visibleColumnOptions.contains(WorkingListAttributes.depDischargeName)) {
//
//            dto.setDepDischargeName((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.depDischargeName)));
//        }
        if (visibleColumnOptions.contains(WorkingListAttributes.depAdmission)) {

            dto.setDepAdmission((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.depAdmission)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.depAdmission301)) {

            dto.setDepAdmission301((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.depAdmission301)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.depTreating)) {

            dto.setDepTreating((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.depTreating)));
        }
        if (visibleColumnOptions.contains(WorkingListAttributes.depTreating301)) {

            dto.setDepTreating301((String) pItems.get(pUniqueDatabaseFields.get(WorkingListAttributes.depTreating301)));
        }
        return dto;
    }

    private double findCwForDrg(String drg) {
        if (drg != null) {
            for (CDrgCatalog c : drgCatalog) {
                if (c.getDrgDrg().equals(drg)) {
                    return c.getDrgMdCw().doubleValue();
                }
            }
        }
        return 0.0;
    }

    @Override
    protected String ruleFilterSubQuery(String pAttributeValue) {
        return "EXISTS(SELECT 1 FROM T_CHECK_RESULT \n"
                + " INNER JOIN VIEW_GRPRES_" + grouper.name() + " VIEW_GRPRES ON T_CHECK_RESULT.T_GROUPING_RESULTS_ID = VIEW_GRPRES.ID  "
                + " WHERE " + pAttributeValue + " AND VIEW_GRPRES.T_CASE_DETAILS_ID = T_CASE_DETAILS.ID) \n";
    }

}
