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
import static de.lb.cpx.service.ejb.AbstractSearchService.toLong;
import de.lb.cpx.shared.dto.WorkflowListItemDTO;
import de.lb.cpx.shared.filter.enums.SearchListAttributes;
import de.lb.cpx.shared.filter.enums.WorkflowListAttributes;
import de.lb.cpx.shared.lang.Lang;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;

/**
 *
 * @author Dirk Niemeier
 */
@Stateless
public class WorkflowListSearchService extends AbstractSearchService<WorkflowListItemDTO> {

//    @Inject
//    private CpxServerConfigLocal cpxConfig;
    public WorkflowListSearchService() throws CpxIllegalArgumentException {
        super(WorkflowListAttributes.instance());
    }

    @Override
    protected void prepareSql(boolean pIsLocal, boolean pIsShowAllReminders, List<String> pColumns, List<String> pJoin, List<String> pWhere, List<String> pOrder, int pLimitFrom, int pLimitTo, final QueryType pQueryType) {
        int columnCount = pColumns.size();

        if (pQueryType == null || QueryType.NORMAL.equals(pQueryType)) {
            pColumns.add("T_LOCK.SINCE LOCK_SINCE");
            pColumns.add("T_LOCK.EXPIRES LOCK_EXPIRES");
            pColumns.add("T_LOCK.USER_NAME LOCK_USER_NAME");
        }

        if (columnCount <= 0) {
            pColumns.add("T_WM_PROCESS.ID");
        }
        //pWhere.add("(T_WM_PROCESS_T_CASE.MAIN_CASE_FL = 1 OR T_WM_PROCESS_T_CASE.MAIN_CASE_FL IS NULL OR NOT EXISTS (SELECT 1 FROM T_WM_PROCESS_T_CASE WHERE T_WM_PROCESS_T_CASE.MAIN_CASE_FL = 1 AND T_WM_PROCESS_T_CASE.T_WM_PROCESS_ID = T_WM_PROCESS.ID))"); //show only cases that are marked as main case or null if there is no main case existing yet
        //pWhere.add("T_WM_PROCESS.CANCEL_FL= 0");
        List<String> joinTmp = new ArrayList<>();

        joinTmp.add("LEFT JOIN T_LOCK ON T_LOCK.PROCESS_ID = T_WM_PROCESS.ID ");

        joinTmp.add("LEFT JOIN T_WM_PROCESS_T_CASE ON T_WM_PROCESS_T_CASE.T_WM_PROCESS_ID = T_WM_PROCESS.ID AND (T_WM_PROCESS_T_CASE.MAIN_CASE_FL = 1 OR T_WM_PROCESS_T_CASE.MAIN_CASE_FL IS NULL OR NOT EXISTS (SELECT 1 FROM T_WM_PROCESS_T_CASE WHERE T_WM_PROCESS_T_CASE.MAIN_CASE_FL = 1 AND T_WM_PROCESS_T_CASE.T_WM_PROCESS_ID = T_WM_PROCESS.ID))");
        joinTmp.add("LEFT JOIN T_CASE ON T_WM_PROCESS_T_CASE.T_CASE_ID = T_CASE.ID ");
        //joinTmp.add("LEFT JOIN T_CASE_DETAILS ON T_CASE_DETAILS.ID = T_CASE.CS_" + (isLocal?"LOCAL":"EXTERN") + "_ACTUAL_ID ");
        joinTmp.add("LEFT JOIN T_CASE_DETAILS ON T_CASE_DETAILS.T_CASE_ID = T_CASE.ID AND (T_CASE_DETAILS.ACTUAL_FL = 1 OR T_CASE_DETAILS.ACTUAL_FL IS NULL) AND (T_CASE_DETAILS.LOCAL_FL = " + (pIsLocal ? "1" : "0") + " OR T_CASE_DETAILS.LOCAL_FL IS NULL) ");
        //joinTmp.add("LEFT JOIN (SELECT ASS.T_WM_PROCESS_ID, MAX(ASS.ID) ID FROM T_WM_REMINDER ASS GROUP BY ASS.T_WM_PROCESS_ID) T_ASS ON T_ASS.T_WM_PROCESS_ID = T_WM_PROCESS.ID "); // <- get sure that only one the last assignment (by id) is selected
        //joinTmp.add("LEFT JOIN T_WM_REMINDER ON T_WM_REMINDER.ID = T_ASS.ID ");

//      joinTmp.add("LEFT JOIN T_WM_REMINDER_SUBJECT ON T_WM_REMINDER_SUBJECT.ID = T_WM_REMINDER.SUBJECT_ID ");
        //joinTmp.add("LEFT JOIN T_WM_ASSIGNMENT_SUBJECT ON T_WM_ASSIGNMENT_SUBJECT.ID = T_WM_ASSIGNMENT.SUBJECT_ID ");
        //joinTmp.add("INNER JOIN CPX_COMMON.CDB_USERS CDB_USERS_SENDER ON CDB_USERS_SENDER.ID = T_WM_ASSIGNMENT.CREATION_USER ");
        //joinTmp.add("INNER JOIN CPX_COMMON.CDB_USERS CDB_USERS_RECEIVER ON CDB_USERS_SENDER.ID = T_WM_ASSIGNMENT.ASSIGNED_USER_ID ");
//        if (visibleColumnOptions.contains(WorkflowListAttributes.assLastModificationDate)) {
//            joinTmp.add("LEFT JOIN T_WM_PROCESS ON T_WM_PROCESS.P_ID = T_WM_PROCESS.ID");
//        }
        /*   if(visibleColumnOptions.contains(WorkflowListAttributes.remCreated)){
            joinTmp.add("LEFT JOIN VIEW_OLDEST_REMINDER ON VIEW_OLDEST_REMINDER.ID = T_WM_PROCESS.ID");
        }*/
        //CPX-1108 enable Filter mdkAuditReasons
        joinTmp.add("LEFT JOIN T_WM_PROCESS_HOSPITAL ON T_WM_PROCESS_HOSPITAL.ID = T_WM_PROCESS.ID");
        //joinTmp.add("LEFT JOIN T_WM_REQUEST ON T_WM_REQUEST.T_WM_PROCESS_HOSPITAL_ID = T_WM_PROCESS_HOSPITAL.ID");
//        if (isFiltered(WorkflowListAttributes.mdkAuditReasonsFilter)) {
//            //joinTmp.add("INNER JOIN T_WM_REQUEST ON T_WM_REQUEST.T_WM_PROCESS_HOSPITAL_ID = T_WM_PROCESS.ID");
//            //joinTmp.add("INNER JOIN T_WM_AUDIT_REASONS ON T_WM_AUDIT_REASONS.T_WM_REQUEST_ID = T_WM_REQUEST.ID");
//        }
//        if (isFiltered(WorkflowListAttributes.actionSubjectFilter)) {
////            joinTmp.add("LEFT JOIN T_WM_ACTION ON T_WM_ACTION.T_WM_PROCESS_ID = T_WM_PROCESS.ID");
//        }
//        //CPX-1220 Filter mdkStatus
        if (//visibleColumnOptions.contains(WorkflowListAttributes.requestState) || 
                visibleColumnOptions.contains(WorkflowListAttributes.mdkSubseqProcDate)
                ||visibleColumnOptions.contains(WorkflowListAttributes.insuranceRecivedBillDate)
                || visibleColumnOptions.contains(WorkflowListAttributes.mdHosStartAudit)) {
            joinTmp.add("LEFT JOIN  VIEW_LATEST_REQUEST_MDK ON VIEW_LATEST_REQUEST_MDK.T_WM_PROCESS_HOSPITAL_ID=T_WM_PROCESS_HOSPITAL.ID ");

        }
//        if (visibleColumnOptions.contains(WorkflowListAttributes.csCaseNumber) && !visibleColumnOptions.contains(WorkflowListAttributes.isCancel)) {
//            pWhere.add("T_WM_PROCESS.CANCEL_FL=0");
//        }
//        joinTmp.add("LEFT JOIN T_WM_REQUEST_MDK ON T_WM_REQUEST.ID = T_WM_REQUEST_MDK.ID");
//        joinTmp.add("LEFT JOIN T_WM_REQUEST_MDK ON T_WM_REQUEST_MDK.ID = T_WM_PROCESS.ID");

        if (visibleColumnOptions.contains(WorkflowListAttributes.requestState) || 
                visibleColumnOptions.contains(WorkflowListAttributes.requestStartAudit)
                 || visibleColumnOptions.contains(WorkflowListAttributes.requestReportDate)
                || visibleColumnOptions.contains(WorkflowListAttributes.requestType)
                ||visibleColumnOptions.contains(WorkflowListAttributes.auditNames)
                ) {
            //joinTmp.add("LEFT JOIN VIEW_LATEST_REQUEST ON T_WM_PROCESS.ID = VIEW_LATEST_REQUEST.T_WM_PROCESS_HOSPITAL_ID");
            //joinTmp.add("LEFT JOIN T_WM_REQUEST ON T_WM_REQUEST.T_WM_PROCESS_HOSPITAL_ID = T_WM_PROCESS.ID AND (CASE WHEN T_WM_REQUEST.MODIFICATION_DATE IS NULL THEN T_WM_REQUEST.CREATION_DATE ELSE T_WM_REQUEST.MODIFICATION_DATE END) = (SELECT MAX((CASE WHEN REQ.MODIFICATION_DATE IS NULL THEN REQ.CREATION_DATE ELSE REQ.MODIFICATION_DATE END)) FROM T_WM_REQUEST REQ WHERE REQ.T_WM_PROCESS_HOSPITAL_ID = T_WM_PROCESS.ID) ");
            joinTmp.add("LEFT JOIN VIEW_LATEST_REQUEST T_WM_REQUEST ON T_WM_REQUEST.T_WM_PROCESS_HOSPITAL_ID = T_WM_PROCESS.ID ");
            
            if(visibleColumnOptions.contains(WorkflowListAttributes.auditNames)){
                joinTmp.add("LEFT JOIN VW_AGG_AUDITS on VW_AGG_AUDITS.T_WM_REQUEST_ID = T_WM_REQUEST.ID");

            }
       }
        if (visibleColumnOptions.contains(WorkflowListAttributes.cwInitial) || visibleColumnOptions.contains(WorkflowListAttributes.cwDiff) || visibleColumnOptions.contains(WorkflowListAttributes.cwFinal)
                || visibleColumnOptions.contains(WorkflowListAttributes.drgInitial) || visibleColumnOptions.contains(WorkflowListAttributes.drgFinal)
                || visibleColumnOptions.contains(WorkflowListAttributes.supFeeInitial) || visibleColumnOptions.contains(WorkflowListAttributes.supFeeDiff) || visibleColumnOptions.contains(WorkflowListAttributes.supFeeFinal)
                || visibleColumnOptions.contains(WorkflowListAttributes.losInitial) || visibleColumnOptions.contains(WorkflowListAttributes.losDiff) || visibleColumnOptions.contains(WorkflowListAttributes.losFinal)
                //CPX-1028 RSH 20180713
                || visibleColumnOptions.contains(WorkflowListAttributes.processResult)
                || visibleColumnOptions.contains(WorkflowListAttributes.closingDate)
                //                || visibleColumnOptions.contains(WorkflowListAttributes.mainMdkAuditReasons)
                || visibleColumnOptions.contains(WorkflowListAttributes.revenueDiff)
                || visibleColumnOptions.contains(WorkflowListAttributes.revenueInit)
                || visibleColumnOptions.contains(WorkflowListAttributes.revenueFinal)
                || visibleColumnOptions.contains(WorkflowListAttributes.cwCareInitial) || visibleColumnOptions.contains(WorkflowListAttributes.cwCareDiff) || visibleColumnOptions.contains(WorkflowListAttributes.cwCareFinal)
                || visibleColumnOptions.contains(WorkflowListAttributes.savedMoney) 
                        || visibleColumnOptions.contains(WorkflowListAttributes.resultDelta)
                        || visibleColumnOptions.contains(WorkflowListAttributes.penaltyFee)) {
            joinTmp.add("LEFT JOIN T_WM_PROCESS_HOSPITAL ON T_WM_PROCESS_HOSPITAL.ID = T_WM_PROCESS.ID");
            joinTmp.add("LEFT JOIN VIEW_PROCESS_HOSPITAL_FINALIS ON VIEW_PROCESS_HOSPITAL_FINALIS.T_WM_PROCESS_HOSPITAL_ID = T_WM_PROCESS_HOSPITAL.ID");
        }
        // CPX-565 : 23.10.2017 RSH- Anzeige der entlassenden FA in der Fallliste.
        if (visibleColumnOptions.contains(SearchListAttributes.depDischarge)
                //            || visibleColumnOptions.contains(SearchListAttributes.depDischargeName)
                || visibleColumnOptions.contains(SearchListAttributes.depDischarge301)
                || visibleColumnOptions.contains(SearchListAttributes.depAdmission)
                || visibleColumnOptions.contains(SearchListAttributes.depAdmission301)
                || visibleColumnOptions.contains(SearchListAttributes.depTreating)
                || visibleColumnOptions.contains(SearchListAttributes.depTreating301)) {
            joinTmp.add("LEFT JOIN VIEW_DEPARTMENT ON T_CASE_DETAILS.ID = VIEW_DEPARTMENT.T_CASE_DETAILS_ID");

        }

//        if (visibleColumnOptions.contains(WorkflowListAttributes.mainMdkAuditReasons)) {
//            joinTmp.add("LEFT JOIN T_WM_AUDIT_REASONS MAIN_REASON ON MAIN_REASON.T_WM_PROCESS_FINAL_ID = T_WM_PROCESS_HOSPITAL_FINALIS.ID AND EXTENDED = 0");
//        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.actionComment)||visibleColumnOptions.contains(WorkflowListAttributes.latestActionSubject)) {
            joinTmp.add("LEFT JOIN VIEW_LATEST_ACTION  ON VIEW_LATEST_ACTION.T_WM_PROCESS_ID = T_WM_PROCESS.ID");
        }
        //RSH: 19042018 CPX-857
        //VIEW_OLDEST_REMINDER  : Vorgänge mit dem ältesten Wiedervorlagendatum (Wenn Due-Date ist für alle WV null ist , dann muss die letzte angelegte WV Angezeigt werden)
        //VIEW_ALL_REMINDER : Alle Reminder
        if (visibleColumnOptions.contains(WorkflowListAttributes.assSubject) || visibleColumnOptions.contains(WorkflowListAttributes.assSender) || visibleColumnOptions.contains(WorkflowListAttributes.assReceiver) || visibleColumnOptions.contains(WorkflowListAttributes.remLatestCreationDate)
                || visibleColumnOptions.contains(WorkflowListAttributes.remFinished) || visibleColumnOptions.contains(WorkflowListAttributes.wvPrio) || visibleColumnOptions.contains(WorkflowListAttributes.wvComment)) {
            final String reminderView = pIsShowAllReminders ? "VIEW_ALL_REMINDER" : "VIEW_OLDEST_OPEN_REMINDER";
            joinTmp.add("LEFT JOIN " + reminderView + " T_WM_REMINDER ON T_WM_REMINDER.T_WM_PROCESS_ID = T_WM_PROCESS.ID ");
            pColumns.add("T_WM_REMINDER.ID REMINDER_ID");
//            if (cpxConfig.getReminderConfig()) {
//                //ALL
//                //joinTmp.add("LEFT JOIN T_WM_REMINDER ON T_WM_REMINDER.T_WM_PROCESS_ID = T_WM_PROCESS.ID ");
//                joinTmp.add("LEFT JOIN VIEW_ALL_REMINDER T_WM_REMINDER ON T_WM_REMINDER.T_WM_PROCESS_ID = T_WM_PROCESS.ID ");
//            } else {
//                //OLDEST
//                //joinTmp.add("LEFT JOIN T_WM_REMINDER ON T_WM_REMINDER.T_WM_PROCESS_ID = T_WM_PROCESS.ID AND (T_WM_REMINDER.FINISHED_FL = 0 AND (CASE WHEN T_WM_REMINDER.DUE_DATE IS NULL THEN T_WM_REMINDER.CREATION_DATE ELSE T_WM_REMINDER.DUE_DATE END) = (SELECT MIN(CASE WHEN REM.DUE_DATE IS NULL THEN REM.CREATION_DATE ELSE REM.DUE_DATE END) FROM T_WM_REMINDER REM WHERE REM.T_WM_PROCESS_ID = T_WM_PROCESS.ID AND REM.FINISHED_FL = 0)) ");
//                joinTmp.add("LEFT JOIN VIEW_OLDEST_REMINDER T_WM_REMINDER ON T_WM_REMINDER.T_WM_PROCESS_ID = T_WM_PROCESS.ID ");
//            }
//            //joinTmp.add("LEFT JOIN VIEW_" + reminderViewSuffix + "_REMINDER ON T_WM_PROCESS.ID = VIEW_" + reminderViewSuffix + "_REMINDER.T_WM_PROCESS_ID");
        }

        if (visibleColumnOptions.contains(SearchListAttributes.patDateOfBirth) || visibleColumnOptions.contains(SearchListAttributes.patFirstName) || visibleColumnOptions.contains(SearchListAttributes.patName) || visibleColumnOptions.contains(SearchListAttributes.patSecondName)
                || visibleColumnOptions.contains(WorkflowListAttributes.patNumber) || visibleColumnOptions.contains(WorkflowListAttributes.insNumber) || visibleColumnOptions.contains(WorkflowListAttributes.insInsCompany)) {
            joinTmp.add("INNER JOIN T_PATIENT ON T_PATIENT.ID = T_WM_PROCESS.T_PATIENT_ID ");

            // joinTmp.add("LEFT JOIN T_INSURANCE ON T_INSURANCE.PATIENT_ID = T_PATIENT.ID ");
            // pWhere.add("(T_INSURANCE.INS_IS_ACTUAL_FL=1 OR T_INSURANCE.INS_IS_ACTUAL_FL IS NULL)");
        }
        
        if (visibleColumnOptions.contains(WorkflowListAttributes.processTopic)
                || visibleColumnOptions.contains(WorkflowListAttributes.billCorrectionDeadline)
                || visibleColumnOptions.contains(WorkflowListAttributes.mdkAuditCompletionDeadline)
                || visibleColumnOptions.contains(WorkflowListAttributes.mdkDocumentDeliverDeadline)
                || visibleColumnOptions.contains(WorkflowListAttributes.preliminaryProceedingsClosedDeadline)
                || visibleColumnOptions.contains(WorkflowListAttributes.dataRecordCorrectionDeadline)
                || visibleColumnOptions.contains(WorkflowListAttributes.preliminaryProceedingAnswerDeadline)
                || visibleColumnOptions.contains(WorkflowListAttributes.billCorrectionDeadlineDays)
                || visibleColumnOptions.contains(WorkflowListAttributes.mdkAuditCompletionDeadlineDays)
                || visibleColumnOptions.contains(WorkflowListAttributes.mdkDocumentDeliverDeadlineDays)
                || visibleColumnOptions.contains(WorkflowListAttributes.preliminaryProceedingsClosedDeadlineDays)
                || visibleColumnOptions.contains(WorkflowListAttributes.dataRecordCorrectionDeadlineDays)
                || visibleColumnOptions.contains(WorkflowListAttributes.preliminaryProceedingAnswerDeadlineDays)
// review
                || visibleColumnOptions.contains(WorkflowListAttributes.reviewDeadline)
                || visibleColumnOptions.contains(WorkflowListAttributes.reviewRenewalDeadline)
                || visibleColumnOptions.contains(WorkflowListAttributes.reviewInsReplyDeadline)
                || visibleColumnOptions.contains(WorkflowListAttributes.reviewReplySendDocDeadline)
                || visibleColumnOptions.contains(WorkflowListAttributes.reviewCompletionDeadline)
                || visibleColumnOptions.contains(WorkflowListAttributes.reviewDeadlineDays)
                || visibleColumnOptions.contains(WorkflowListAttributes.reviewRenewalDeadlineDays)
                || visibleColumnOptions.contains(WorkflowListAttributes.reviewInsReplyDeadlineDays)
                || visibleColumnOptions.contains(WorkflowListAttributes.reviewReplySendDocDeadlineDays)
                || visibleColumnOptions.contains(WorkflowListAttributes.reviewCompletionDeadlineDays)
                
                || visibleColumnOptions.contains(WorkflowListAttributes.requestType)) {

            joinTmp.add("LEFT JOIN T_WM_PROCESS_HOSPITAL ON T_WM_PROCESS_HOSPITAL.ID = T_WM_PROCESS.ID");
            //joinTmp.add("LEFT JOIN T_WM_REQUEST ON T_WM_REQUEST.T_WM_PROCESS_HOSPITAL_ID = T_WM_PROCESS_HOSPITAL.ID");
            

        }

        if (visibleColumnOptions.contains(WorkflowListAttributes.kainKeyEn) 
                || visibleColumnOptions.contains(WorkflowListAttributes.dateOfBenefitLawDecision)) {
           joinTmp.add("LEFT JOIN VIEW_WM_KAIN_KEY_LIST ON VIEW_WM_KAIN_KEY_LIST.T_WM_PROCESS_ID = T_WM_PROCESS.ID");
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.inkaKeyEn) ) {
           joinTmp.add("LEFT JOIN VIEW_WM_INKA_KEY_LIST ON VIEW_WM_INKA_KEY_LIST.T_WM_PROCESS_ID = T_WM_PROCESS.ID");
        }
        

        if (visibleColumnOptions.contains(WorkflowListAttributes.workflowNumber) && !visibleColumnOptions.contains(WorkflowListAttributes.isCancel)) {
            pWhere.add("T_WM_PROCESS.CANCEL_FL=0");
        }

        //CPX-1108 RSH 20181008   skripte für anzeige erste prüfgrunde der Anfrage 
//        if (visibleColumnOptions.contains(WorkflowListAttributes.mdkAuditReasons)) {
////                joinTmp.add("LEFT JOIN T_WM_PROCESS_HOSPITAL ON T_WM_PROCESS_HOSPITAL.ID = T_WM_PROCESS.ID");
////                joinTmp.add("LEFT JOIN AT_WM_REQUEST ON T_WM_REQUEST.T_WM_PROCESS_HOSPITAL_ID = T_WM_PROCESS_HOSPITAL.ID");
//            joinTmp.add("LEFT JOIN MDK_AUDIT_REASONS ON MDK_AUDIT_REASONS.T_WM_REQUEST_ID = T_WM_REQUEST.ID");
//            pWhere.add("MDK_AUDIT_REASONS.ID in (\n"
//                    + "select ID from (\n"
//                    + "SELECT Min(MDK_AUDIT_REASONS.ID)ID,T_WM_REQUEST_ID\n"
//                    + "FROM MDK_AUDIT_REASONS where MDK_AUDIT_REASONS.EXTENDED=0\n"
//                    + "group by T_WM_REQUEST_ID))");
//            
//        }
        //pWhere.add("(T_CASE_DETAILS.ACTUAL_FL = 1 OR T_CASE_DETAILS.ACTUAL_FL IS NULL)");
        //pWhere.add("(T_CASE_DETAILS.LOCAL_FL = " + (pIsLocal ? "1" : "0") + " OR T_CASE_DETAILS.LOCAL_FL IS NULL)");

        /* 
      joinTmp.add("LEFT JOIN T_CASE_DETAILS ON T_CASE_DETAILS.ID = T_CASE.CS_" + (isLocal?"LOCAL":"EXTERN") + "_ACTUAL_ID ");
      joinTmp.add("LEFT JOIN T_PATIENT ON T_PATIENT.ID = T_CASE.T_PATIENT_ID ");
      joinTmp.add("LEFT JOIN T_PATIENT_DETAILS ON T_PATIENT.PAT_DETAILS_ACT_ID = T_PATIENT_DETAILS.ID ");
      
         */
        for (int i = joinTmp.size() - 1; i >= 0; i--) {
            pJoin.add(0, joinTmp.get(i));
        }
    }

    @Override
    protected WorkflowListItemDTO fillDto(Map<String, Object> pItems, final Map<String, String> pUniqueDatabaseFields) {
        //WorkingListItemDTO dto = (WorkingListItemDTO) pDto;
        Number number;
        WorkflowListItemDTO dto = new WorkflowListItemDTO();
        //for(int i = 0; i < items.length; i++) {

        //dto.setCsId(toLong((Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.csId))));
        //dto.setCsdId(toLong((Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.csdId))));
        if (visibleColumnOptions.contains(SearchListAttributes.lock)) {
            dto.setLockSince((Date) pItems.get("LOCK_SINCE"));
            dto.setLockExpires((Date) pItems.get("LOCK_EXPIRES"));
            dto.setLockUserName((String) pItems.get("LOCK_USER_NAME"));
            dto.setLock(toBool(pItems.get(pUniqueDatabaseFields.get(SearchListAttributes.lock))));
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.creationDate)) {
            //Anlegedatum
            dto.setCreationDate((Date) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.creationDate)));
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.workflowNumber)) {
            Object workflownumber = pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.workflowNumber));
//            Long l;
//            if (workflownumber instanceof java.math.BigInteger) {
//                l = ((Number) workflownumber).longValue();
//            } else {
//                l = ((Number) workflownumber).longValue();
//            }
            dto.setWorkflowNumber(((Number) workflownumber).longValue());
        }
//        if (visibleColumnOptions.contains(WorkflowListAttributes.subject)) {
//            dto.setSubject((String) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.subject)));
//        }
        //RSH 11.10.2017 CPX-645
        if (visibleColumnOptions.contains(WorkflowListAttributes.wmState)) {
            //dto.setWmState((String) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.wmState)));
            dto.setWmState(toInt((String) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.wmState))));
        }
//        if (visibleColumnOptions.contains(WorkflowListAttributes.wmType)) {
//            //
//            dto.setWmType((String) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.wmType)));
//        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.patNumber)) {
            //Pat.-Nummer
            //columns.add("T_PATIENT.PAT_NUMBER");
            dto.setPatNumber((String) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.patNumber)));
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.csHospitalIdent)) {
            //IKZ
            //columns.add("T_CASE.CS_HOSPITAL_IDENT");
            dto.setCsHospitalIdent((String) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.csHospitalIdent)));
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.csCaseNumber)) {
            //Fallnummer
            //columns.add("T_CASE.CS_CASE_NUMBER");
            dto.setCsCaseNumber((String) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.csCaseNumber)));
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.csdAdmissionDate)) {
            //Aufnahmedatum
            //columns.add("T_CASE_DETAILS.ADMISSION_CAUSE_EN");
            dto.setCsdAdmissionDate((Date) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.csdAdmissionDate)));
        }
        //RSH - CPX-629 :20170829
        if (visibleColumnOptions.contains(WorkflowListAttributes.csdDischargeDate)) {
            //Entlassungsdatum
            //columns.add("T_CASE_DETAILS.CSD_DISCHARGE_DATE");
            dto.setCsdDischargeDate((Date) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.csdDischargeDate)));
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.assSubject)) {
            number = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.assSubject));
            if (number != null) {
                dto.setAssSubject(toLong(number));
            }
            dto.setReminderId(toLong((Number) pItems.get("REMINDER_ID")));
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.assSender)) {
            //Wiedervorlage (Ersteller)           
            number = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.assSender));
            if (number != null) {
                dto.setAssSender(toLong(number));
            }
        }

        if (visibleColumnOptions.contains(WorkflowListAttributes.assReceiver)) {
            //Wiedervorlage (Empfänger)
            number = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.assReceiver));
            if (number != null) {
                dto.setAssReceiver(toLong(number));
            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.assLastModificationDate)) {
            dto.setAssLastModificationDate((Date) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.assLastModificationDate)));
        }
        if(visibleColumnOptions.contains(WorkflowListAttributes.auditNames)){
            dto.setAuditNames((String)pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.auditNames)));
        }
        //CPX-1026
//        if (visibleColumnOptions.contains(WorkflowListAttributes.csTypeOfService)) {
//            String typ = (String) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.csTypeOfService));
//            if (typ != null) {
//                dto.setCsTypeOfService(typ.equals("04") ? Lang.getAmbulant() : Lang.getStationary());
//            }
//        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.insInsCompany)) {
            dto.setInsInsCompany((String) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.insInsCompany)));
        }
        //CPX-994 RSH 20180815
        if (visibleColumnOptions.contains(WorkflowListAttributes.insInsCompanyName)) {
            dto.setInsInsCompanyName((String) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.insInsCompanyName)));
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.insInsCompanyShortName)) {
            dto.setInsInsCompanyShortName((String) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.insInsCompanyShortName)));
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.wvPrio)) {
            //RSH 22.02.2018 CPX-836  , CPX-910
            dto.setWvPrio(toBool(pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.wvPrio))));
        }

        if (visibleColumnOptions.contains(WorkflowListAttributes.requestStartAudit)) {
            dto.setRequestStartAudit((Date) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.requestStartAudit)));
        }

        if (visibleColumnOptions.contains(WorkflowListAttributes.requestReportDate)) {
            dto.setRequestReportDate((Date) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.requestReportDate)));
        }

        if (visibleColumnOptions.contains(WorkflowListAttributes.remLatestCreationDate)) {
            dto.setRemLatestCreationDate((Date) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.remLatestCreationDate)));
        }

        if (visibleColumnOptions.contains(WorkflowListAttributes.vmModUser)) {
            number = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.vmModUser));
            if (number != null) {
                dto.setVmModUser(toLong(number));
            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.assUser)) {
            number = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.assUser));
            if (number != null) {
                dto.setAssUser(toLong(number));
            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.remFinished)) {
            dto.setRemFinished(toBool(pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.remFinished))));
            //RSH 22.02.2018 CPX-836  
//            if (isOracle()) {
//                number = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.remFinished));
//                if (number != null) {
//                    if (number.intValue() == 1) {
//                        dto.setRemFinished(Lang.getProcessStatusReminderClosed());
//                    } else if (number.intValue() == 0) {
//                        dto.setRemFinished(Lang.getProcessStatusReminderOpen());
//                    }
//                }
//            } else if (pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.remFinished)) != null) {
//                if ((Boolean) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.remFinished))) {
//                    dto.setRemFinished(Lang.getProcessStatusReminderClosed());
//                } else {
//                    dto.setRemFinished(Lang.getProcessStatusReminderOpen());
//                }
//            }
        }

        if (visibleColumnOptions.contains(WorkflowListAttributes.csdAdmCauseEn)) {
            dto.setCsdAdmCauseEn((String) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.csdAdmCauseEn)));
        }
        Date today = new Date();
        DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss.S");
        if (visibleColumnOptions.contains(WorkflowListAttributes.billCorrectionDeadline)) {
            Date bill = (Date) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.billCorrectionDeadline));
            dto.setBillCorrectionDeadline(bill);
            if (bill != null) {
                //CPX-887 RSH 22.03.2018              
                Long l = Lang.toDaysBetween(today, bill);
                dto.setBillCorrectionDeadlineDays(l.intValue());

            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.mdkAuditCompletionDeadline)) {
            Date audit = (Date) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.mdkAuditCompletionDeadline));
            dto.setMdkAuditCompletionDeadline(audit);
            if (audit != null) {
                //CPX-887 RSH 22.03.2018
                Long l = Lang.toDaysBetween(today, audit);
                dto.setMdkAuditCompletionDeadlineDays(l.intValue());
            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.mdkDocumentDeliverDeadline)) {
            Date doc = (Date) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.mdkDocumentDeliverDeadline));
            dto.setMdkDocumentDeliverDeadline(doc);
            if (doc != null) {
                //CPX-887 RSH 22.03.2018
                Long l = Lang.toDaysBetween(today, doc);
                dto.setMdkDocumentDeliverDeadlineDays(l.intValue());
            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.preliminaryProceedingAnswerDeadline)) {
            Date answer = (Date) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.preliminaryProceedingAnswerDeadline));
            dto.setPreliminaryProceedingAnswerDeadline(answer);
            if (answer != null) {
                //CPX-887 RSH 22.03.2018
                Long l = Lang.toDaysBetween(today, answer);
                dto.setPreliminaryProceedingAnswerDeadlineDays(l.intValue());
            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.preliminaryProceedingsClosedDeadline)) {
            Date closed = (Date) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.preliminaryProceedingsClosedDeadline));
            dto.setPreliminaryProceedingsClosedDeadline(closed);
            if (closed != null) {
                //CPX-887 RSH 22.03.2018
                Long l = Lang.toDaysBetween(today, closed);
                dto.setPreliminaryProceedingsClosedDeadlineDays(l.intValue());
            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.dataRecordCorrectionDeadline)) {
            Date data = (Date) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.dataRecordCorrectionDeadline));
            dto.setDataRecordCorrectionDeadline(data);
            if (data != null) {
                //CPX-887 RSH 22.03.2018
                Long l = Lang.toDaysBetween(today, data);
                dto.setDataRecordCorrectionDeadlineDays(l.intValue());
            }
        }
// review deadlines        
        if (visibleColumnOptions.contains(WorkflowListAttributes.reviewDeadline)) {
            Date data = (Date) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.reviewDeadline));
            dto.setReviewDeadline(data);
            if (data != null) {
                //CPX-887 RSH 22.03.2018
                Long l = Lang.toDaysBetween(today, data);
                dto.setReviewDeadlineDays(l.intValue());
            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.reviewRenewalDeadline)) {
            Date data = (Date) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.reviewRenewalDeadline));
            dto.setReviewRenewalDeadline(data);
            if (data != null) {
                //CPX-887 RSH 22.03.2018
                Long l = Lang.toDaysBetween(today, data);
                dto.setReviewRenewalDeadlineDays(l.intValue());
            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.reviewInsReplyDeadline)) {
            Date data = (Date) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.reviewInsReplyDeadline));
            dto.setReviewInsReplyDeadline(data);
            if (data != null) {
                //CPX-887 RSH 22.03.2018
                Long l = Lang.toDaysBetween(today, data);
                dto.setReviewInsReplyDeadlineDays(l.intValue());
            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.reviewReplySendDocDeadline)) {
            Date data = (Date) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.reviewReplySendDocDeadline));
            dto.setReviewReplySendDocDeadline(data);
            if (data != null) {
                //CPX-887 RSH 22.03.2018
                Long l = Lang.toDaysBetween(today, data);
                dto.setReviewReplySendDocDeadlineDays(l.intValue());
            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.reviewCompletionDeadline)) {
            Date data = (Date) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.reviewCompletionDeadline));
            dto.setReviewCompletionDeadline(data);
            if (data != null) {
                //CPX-887 RSH 22.03.2018
                Long l = Lang.toDaysBetween(today, data);
                dto.setReviewCompletionDeadlineDays(l.intValue());
            }
        }




        if (visibleColumnOptions.contains(WorkflowListAttributes.processTopic)) {
            number = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.processTopic));
            if (number != null) {
                dto.setProcessTopic(toLong(number));
            }
        }
        //CPX-1028 RSH 20180713
        if (visibleColumnOptions.contains(WorkflowListAttributes.processResult)) {
            number = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.processResult));
            if (number != null) {
                dto.setProcessResult(toLong(number));
            }
        }
        //RSH 11.01.2019 CPX-1355
        if (visibleColumnOptions.contains(WorkflowListAttributes.closingDate)) {
            dto.setClosingDate((Date) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.closingDate)));

        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.requestType)) {
            number = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.requestType));
            if (number != null) {
                dto.setRequestType(toInt(number));
            }
        }
        // CPX-565 : 23.10.2017 RSH- Anzeige der entlassenden FA in der Vorgangsliste.
        if (visibleColumnOptions.contains(WorkflowListAttributes.depDischarge)) {

            dto.setDepDischarge((String) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.depDischarge)));
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.depDischarge301)) {

            dto.setDepDischarge301((String) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.depDischarge301)));
        }
//        if (visibleColumnOptions.contains(WorkflowListAttributes.DepDischargeName)) {
//
//            dto.setDepDischargeName((String) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.DepDischargeName)));
//        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.depAdmission)) {

            dto.setDepAdmission((String) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.depAdmission)));
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.depAdmission301)) {

            dto.setDepAdmission301((String) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.depAdmission301)));
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.depTreating)) {

            dto.setDepTreating((String) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.depTreating)));
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.depTreating301)) {

            dto.setDepTreating301((String) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.depTreating301)));
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.wvComment)) {

            dto.setWvComment((String) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.wvComment)));
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.actionComment)) {
            Object actionCommentObject = pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.actionComment));
            String actionComment = "";
            if (actionCommentObject != null) {
                if (isOracle()) {
                    try {
                        StringBuilder sb = new StringBuilder();
                        Reader reader = ((Clob) actionCommentObject).getCharacterStream();
                        try ( BufferedReader br = new BufferedReader(reader)) {
                            String line;
                            while (null != (line = br.readLine())) {
                                sb.append(line);
                            }
                            actionComment = sb.toString();
                        }
                    } catch (SQLException | IOException ex) {
                        Logger.getLogger(WorkflowListSearchService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    actionComment = (String) actionCommentObject;
                }
            }
            dto.setActionComment(actionComment);

        }
//        //CPX-1108 
//        if (visibleColumnOptions.contains(WorkflowListAttributes.mainMdkAuditReasons)) {
//            number = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.mainMdkAuditReasons));
//            if (number != null) {
//                dto.setMainMdkAuditReasons(toLong(number));
//            }
//        }

        if (visibleColumnOptions.contains(WorkflowListAttributes.drgFinal)) {
            dto.setDrgFinal((String) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.drgFinal)));
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.drgInitial)) {
            dto.setDrgInitial((String) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.drgInitial)));
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.cwInitial)) {
            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.cwInitial));
            if (value == null) {
                dto.setCwInitial(null);
            } else {
                dto.setCwInitial(value.doubleValue());
            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.cwDiff)) {
            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.cwDiff));
            if (value == null) {
                dto.setCwDiff(null);
            } else {
                dto.setCwDiff(value.doubleValue());
            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.cwFinal)) {
            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.cwFinal));
            if (value == null) {
                dto.setCwFinal(null);
            } else {
                dto.setCwFinal(value.doubleValue());
            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.supFeeInitial)) {
            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.supFeeInitial));
            if (value == null) {
                dto.setSupFeeInitial(null);
            } else {
                dto.setSupFeeInitial(value.doubleValue());
            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.supFeeDiff)) {
            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.supFeeDiff));
            if (value == null) {
                dto.setSupFeeDiff(null);
            } else {
                dto.setSupFeeDiff(value.doubleValue());
            }
        }

        if (visibleColumnOptions.contains(WorkflowListAttributes.supFeeFinal)) {
            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.supFeeFinal));
            if (value == null) {
                dto.setSupFeeFinal(null);
            } else {
                dto.setSupFeeFinal(value.doubleValue());
            }

        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.losInitial)) {
            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.losInitial));
            if (value == null) {
                dto.setLosInitial(null);
            } else {
                dto.setLosInitial(value.intValue());
            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.losDiff)) {

            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.losDiff));
            if (value == null) {
                dto.setLosDiff(null);
            } else {
                dto.setLosDiff(value.intValue());
            }
        }

        if (visibleColumnOptions.contains(WorkflowListAttributes.losFinal)) {
            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.losFinal));
            if (value == null) {
                dto.setLosFinal(null);
            } else {
                dto.setLosFinal(value.intValue());
            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.caseType)) {
            //Abrechnungsart 
            dto.setCaseType((String) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.caseType)));
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.revenueDiff)) {
            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.revenueDiff));
            if (value == null) {
                dto.setRevenueDiff(null);
            } else {
                dto.setRevenueDiff(value.doubleValue());
            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.revenueInit)) {
            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.revenueInit));
            if (value == null) {
                dto.setRevenueInit(null); 
            } else {
                dto.setRevenueInit(value.doubleValue()); 
            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.revenueFinal)) {
            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.revenueFinal));
            if (value == null) {
                dto.setRevenueFinal(null); 
            } else {
                dto.setRevenueFinal(value.doubleValue()); 
            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.requestState)) {
            number = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.requestState));
            if (number != null) {
                dto.setMdkState(toLong(number));
            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.mdkSubseqProcDate)) {
            dto.setMdkSubseqProcDate((Date) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.mdkSubseqProcDate)));
        }
         if (visibleColumnOptions.contains(WorkflowListAttributes.insuranceRecivedBillDate)) {
            dto.setInsuranceRecivedBillDate((Date) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.insuranceRecivedBillDate)));
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.courtFileNumber)) {

            dto.setCourtFileNumber((String) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.courtFileNumber)));
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.lawerFileNumber)) {

            dto.setLawerFileNumber((String) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.lawerFileNumber)));
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.isCancel)) {
            //CancelFl
            dto.setIsCancel(toBool(pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.isCancel))));
        }
        
        if (visibleColumnOptions.contains(WorkflowListAttributes.cwCareInitial)) {
            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.cwCareInitial));
            if (value == null) {
                dto.setCwCareInitial(null);
            } else {
                dto.setCwCareInitial(value.doubleValue());
            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.cwCareDiff)) {
            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.cwCareDiff));
            if (value == null) {
                dto.setCwCareDiff(null);
            } else {
                dto.setCwCareDiff(value.doubleValue());
            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.cwCareFinal)) {
            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.cwCareFinal));
            if (value == null) {
                dto.setCwCareFinal(null);
            } else {
                dto.setCwCareFinal(value.doubleValue());
            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.savedMoney)) {
            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.savedMoney));
            if (value == null) {
                dto.setSavedMoney(null);
            } else {
                dto.setSavedMoney(value.doubleValue());
            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.resultDelta)) {
            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.resultDelta));
            if (value == null) {
                dto.setResultDelta(null);
            } else {
                dto.setResultDelta(value.doubleValue());
            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.latestActionSubject)) {
            number = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.latestActionSubject));
            if (number != null) {
                dto.setLatestActionSubject(toLong(number));
            }
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.penaltyFee)) {
            Number value = (Number) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.penaltyFee));
            if (value == null) {
                dto.setPenaltyFee(null);
            } else {
                dto.setPenaltyFee(value.doubleValue());
            }
        }
        
        if (visibleColumnOptions.contains(WorkflowListAttributes.kainKeyEn)) {
            Object value = pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.kainKeyEn));
            dto.setKainKeyEn( value == null?"":(String)value);
        }
        
        if (visibleColumnOptions.contains(WorkflowListAttributes.inkaKeyEn)) {
            Object value = pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.inkaKeyEn));
            dto.setInkaKeyEn( value == null?"":(String)value);

        }
        
        if (visibleColumnOptions.contains(WorkflowListAttributes.mdHosStartAudit)) {
            dto.setMdHosStartAudit((Date) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.mdHosStartAudit)));
        }
        if (visibleColumnOptions.contains(WorkflowListAttributes.dateOfBenefitLawDecision)) {
            dto.setDateOfBenefitLawDecision((Date) pItems.get(pUniqueDatabaseFields.get(WorkflowListAttributes.dateOfBenefitLawDecision)));
        }

        return dto;
    }

}
