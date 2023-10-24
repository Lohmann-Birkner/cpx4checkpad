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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.filter.enums;

import de.lb.cpx.model.enums.AdmissionCauseEn;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.enums.WmEventTypeEn;
import de.lb.cpx.wm.model.enums.WmRequestTypeEn;
import de.lb.cpx.wm.model.enums.WmWorkflowStateEn;
import de.lb.cpx.wm.model.enums.Tp301Key30En;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Wilde
 */
public final class WorkflowListAttributes extends SearchListAttributes {

    private static WorkflowListAttributes instance = null;

    public static final String id = "id"; //processId
    public static final String csId = "csId"; //caseId
    public static final String csdId = "csdId"; //caseDetailsId
    public static final String workflowNumber = "workflowNumber";
    public static final String courtFileNumber = "courtFileNumber";
    public static final String lawerFileNumber = "lawerFileNumber";
    public static final String creationDate = "creationDate";
    public static final String creationDateEqual = "creationDateEqual";
    public static final String creationDateFrom = "creationDateFrom";
    public static final String creationDateTo = "creationDateTo";
//    public static final String subject = "subject";
    public static final String wmState = "wmState"; //
//    public static final String wmType = "wmType";
    public static final String patNumber = "patNumber"; //Pat.-Nummer
    public static final String csHospitalIdent = "csHospitalIdent";
    public static final String csCaseNumber = "csCaseNumber";
    //final public static String csStatusEn = "csStatusEn";
    //final public static String creationDate = "creationDate";
    public static final String csdAdmissionDate = "csdAdmissionDate";
    public static final String csdAdmissionDateEqual = "csdAdmissionDateEqual";
    public static final String csdAdmissionDateFrom = "csdAdmissionDateFrom";
    public static final String csdAdmissionDateTo = "csdAdmissionDateTo";
    public static final String csdAdmCauseEn = "csdAdmCauseEn";
    //final public static String assCreationDate = "assCreationDate";
    public static final String assSender = "assSender";
    public static final String assReceiver = "assReceiver";
    public static final String assSubject = "assSubject";
    public static final String assLastModificationDate = "assLastModificationDate";
    public static final String assLastModificationDateEqual = "assLastModificationDateEqual";
    public static final String assLastModificationDateFrom = "assLastModificationDateFrom";
    public static final String assLastModificationDateTo = "assLastModificationDateTo";
//    public static final String csTypeOfService = "csTypeOfService";
    public static final String insInsCompany = "insInsCompany"; //IZK der Versischerung
    public static final String insInsCompanyName = "insInsCompanyName";// Versicherungsnamen
    public static final String insInsCompanyShortName = "insInsCompanyShortName"; //Versicherungs-/Krankenkassengruppen
    public static final String requestStartAudit = "requestStartAudit";
    public static final String requestStartAuditEqual = "requestStartAuditEqual";
    public static final String requestStartAuditTo = "requestStartAuditTo";
    public static final String requestStartAuditFrom = "requestStartAuditFrom";
    public static final String requestReportDate = "requestReportDate";
    public static final String requestReportDateEqual = "requestReportDateEqual";
    public static final String requestReportDateTo = "requestReportDateTo";
    public static final String requestReportDateFrom = "requestReportDateFrom";
    public static final String remLatestCreationDate = "remLatestCreationDate";
    public static final String remLatestCreationDateEqual = "remLatestCreationDateEqual";
    public static final String remLatestCreationDateFrom = "remLatestCreationDateFrom";
    public static final String remLatestCreationDateTo = "remLatestCreationDateTo";
    public static final String remLatestCreationDateOpen = "remLatestCreationDateOpen";
    public static final String remLatestCreationDateExp = "remLatestCreationDateExp";
    public static final String remLatestCreationDateToday = "remLatestCreationDateToday";
    public static final String vmModUser = "vmModUser";
    public static final String assUser = "assUser";
    public static final String remFinished = "remFinished";
    public static final String csdDischargeDate = "csdDischargeDate";
    public static final String csdDischargeDateEqual = "csdDischargeDateEqual";
    public static final String csdDischargeDateFrom = "csdDischargeDateFrom";
    public static final String csdDischargeDateTo = "csdDischargeDateTo";
    public static final String processTopic = "processTopic";
    public static final String processResult = "processResult";
    public static final String billCorrectionDeadline = "billCorrectionDeadline";
    public static final String billCorrectionDeadlineFrom = "billCorrectionDeadlineFrom";
    public static final String billCorrectionDeadlineTo = "billCorrectionDeadlineTo";
    public static final String billCorrectionDeadlineExp = "billCorrectionDeadlineExp";
    public static final String billCorrectionDeadlineOpen = "billCorrectionDeadlineOpen";
    public static final String mdkAuditCompletionDeadline = "mdkAuditCompletionDeadline";
    public static final String mdkAuditCompletionDeadlineFrom = "mdkAuditCompletionDeadlineFrom";
    public static final String mdkAuditCompletionDeadlineTo = "mdkAuditCompletionDeadlineTo";
    public static final String mdkAuditCompletionDeadlineExp = "mdkAuditCompletionDeadlineExp";
    public static final String mdkAuditCompletionDeadlineOpen = "mdkAuditCompletionDeadlineOpen";
    public static final String mdkDocumentDeliverDeadline = "mdkDocumentDeliverDeadline";
    public static final String mdkDocumentDeliverDeadlineFrom = "mdkDocumentDeliverDeadlineFrom";
    public static final String mdkDocumentDeliverDeadlineTo = "mdkDocumentDeliverDeadlineTo";
    public static final String mdkDocumentDeliverDeadlineExp = "mdkDocumentDeliverDeadlineExp";
    public static final String mdkDocumentDeliverDeadlineOpen = "mdkDocumentDeliverDeadlineOpen";
    public static final String preliminaryProceedingsClosedDeadline = "preliminaryProceedingsClosedDeadline";
    public static final String preliminaryProceedingsClosedDeadlineFrom = "preliminaryProceedingsClosedDeadlineFrom";
    public static final String preliminaryProceedingsClosedDeadlineTo = "preliminaryProceedingsClosedDeadlineTo";
    public static final String preliminaryProceedingsClosedDeadlineExp = "preliminaryProceedingsClosedDeadlineExp";
    public static final String preliminaryProceedingsClosedDeadlineOpen = "preliminaryProceedingsClosedDeadlineOpen";
    public static final String dataRecordCorrectionDeadline = "dataRecordCorrectionDeadline";
    public static final String dataRecordCorrectionDeadlineFrom = "dataRecordCorrectionDeadlineFrom";
    public static final String dataRecordCorrectionDeadlineTo = "dataRecordCorrectionDeadlineTo";
    public static final String dataRecordCorrectionDeadlineExp = "dataRecordCorrectionDeadlineExp";
    public static final String dataRecordCorrectionDeadlineOpen = "dataRecordCorrectionDeadlineOpen";
    public static final String preliminaryProceedingAnswerDeadline = "preliminaryProceedingAnswerDeadline";
    public static final String preliminaryProceedingAnswerDeadlineFrom = "preliminaryProceedingAnswerDeadlineFrom";
    public static final String preliminaryProceedingAnswerDeadlineTo = "preliminaryProceedingAnswerDeadlineTo";
    public static final String preliminaryProceedingAnswerDeadlineExp = "preliminaryProceedingAnswerDeadlineExp";
    public static final String preliminaryProceedingAnswerDeadlineOpen = "preliminaryProceedingAnswerDeadlineOpen";
    public static final String billCorrectionDeadlineDays = "billCorrectionDeadlineDays";
    public static final String mdkAuditCompletionDeadlineDays = "mdkAuditCompletionDeadlineDays";
    public static final String mdkDocumentDeliverDeadlineDays = "mdkDocumentDeliverDeadlineDays";
    public static final String preliminaryProceedingsClosedDeadlineDays = "preliminaryProceedingsClosedDeadlineDays";
    public static final String dataRecordCorrectionDeadlineDays = "dataRecordCorrectionDeadlineDays";
    public static final String preliminaryProceedingAnswerDeadlineDays = "preliminaryProceedingAnswerDeadlineDays";
    public static final String requestType = "requestType";
    public static final String wvComment = "wvComment";
    public static final String actionComment = "actionComment";
    public static final String wvPrio = "wvPrio";
//    public static final String mainMdkAuditReasons = "mainMdkAuditReasons";
    public static final String mdkAuditReasonsFilter = "mdkAuditReasonsFilter";
    public static final String processFinalAuditReasonsFilter = "processFinalAuditReasonsFilter";
    public static final String requestStatusFilter = "mdkStatusFilter";
    public static final String mdkSubseqProcDate = "mdkSubseqProcDate";
    public static final String mdkSubseqProcDateEqual = "mdkSubseqProcDateEqual";
    public static final String mdkSubseqProcDateFrom = "mdkSubseqProcDateFrom";
    public static final String mdkSubseqProcDateTo = "mdkSubseqProcDateTo";
    public static final String requestState = "mdkState";
    public static final String cwInitial = "cwInitial";
    public static final String cwInitialEqual = "cwInitialEqual";
    public static final String cwInitialFrom = "cwInitialFrom";
    public static final String cwInitialTo = "cwInitialTo";
    public static final String cwDiff = "cwDiff";
    public static final String cwDiffEqual = "vEqual";
    public static final String cwDiffFrom = "cwDiffeFrom";
    public static final String cwDiffTo = "cwDiffTo";
    public static final String cwFinal = "cwFinal";
    public static final String cwFinalEqual = "cwFinalEqual";
    public static final String cwFinalFrom = "cwFinalFrom";
    public static final String cwFinalTo = "cwFinalTo";
    public static final String drgInitial = "drgInitial";
    public static final String drgFinal = "drgFinal";
    public static final String closingDate = "closingDate";
    public static final String closingDateEqual = "closingDateEqual";
    public static final String closingDateFrom = "closingDateFrom";
    public static final String closingDateTo = "closingDateTo";
    public static final String supFeeInitial = "supFeeInitial";
    public static final String supFeeInitialEqual = "supFeeInitialEqual";
    public static final String supFeeInitialFrom = "supFeeInitialFrom";
    public static final String supFeeInitialTo = "supFeeInitialTo";
    public static final String supFeeDiff = "supFeeDiff";
    public static final String supFeeDiffEqual = "supFeeDiffEqual";
    public static final String supFeeDiffFrom = "supFeeDiffFrom";
    public static final String supFeeDiffTo = "supFeeDiffTo";
    public static final String supFeeFinal = "supFeeFinal";
    public static final String supFeeFinalEqual = "supFeeFinalEqual";
    public static final String supFeeFinalFrom = "supFeeFinalFrom";
    public static final String supFeeFinalTo = "supFeeFinalTo";
    public static final String losInitial = "losInitial";
    public static final String losInitialEqual = "losInitialEqual";
    public static final String losInitialFrom = "losInitialFrom";
    public static final String losInitialTo = "losInitialTo";
    public static final String losDiff = "losDiff";
    public static final String losDiffEqual = "losDiffEqual";
    public static final String losDiffFrom = "losDiffFrom";
    public static final String losDiffTo = "losDiffTo";
    public static final String losFinal = "losFinal";
    public static final String losFinalEqual = "losFinalEqual";
    public static final String losFinalFrom = "losFinalFrom";
    public static final String losFinalTo = "losFinalTo";
    public static final String caseType = "caseType";
    public static final String revenueDiff = "revenueDiff";
    public static final String revenueDiffEqual = "revenueDiffEqual";
    public static final String revenueDiffFrom = "revenueDiffFrom";
    public static final String revenueDiffTo = "revenueDiffTo";
    public static final String actionSubjectFilter = "actionSubjectFilter";
    public static final String eventTypeFilter = "eventTypeFilter";
    public static final String isCancel = "isCancel";
    public static final String insuranceRecivedBillDate = "insuranceRecivedBillDate";
    public static final String insuranceRecivedBillDateFrom = "insuranceRecivedBillDateFrom";
    public static final String insuranceRecivedBillDateTo = "insuranceRecivedBillDateTo";
    public static final String insuranceRecivedBillDateEqual = "insuranceRecivedBillDateEqual";
    public static final String auditNames="auditNames";
    public static final String cwCareInitial = "cwCareInitial";
    public static final String cwCareInitialEqual = "cwCareInitialEqual";
    public static final String cwCareInitialFrom = "cwCareInitialFrom";
    public static final String cwCareInitialTo = "cwCareInitialTo";
    public static final String cwCareDiff = "cwCareDiff";
    public static final String cwCareDiffEqual = "cwCareEqual";
    public static final String cwCareDiffFrom = "cwCareDiffeFrom";
    public static final String cwCareDiffTo = "cwCareDiffTo";
    public static final String cwCareFinal = "cwCareFinal";
    public static final String cwCareFinalEqual = "cwCareFinalEqual";
    public static final String cwCareFinalFrom = "cwCareFinalFrom";
    public static final String cwCareFinalTo = "cwCareFinalTo";
    public static final String savedMoney = "savedMoney";
    public static final String savedMoneyFrom = "savedMoneyFrom";
    public static final String savedMoneyTo = "savedMoneyTo";
    public static final String resultDelta = "resultDelta";
    public static final String resultDeltaFrom = "resultDeltaFrom";
    public static final String resultDeltaTo = "resultDeltaTo";
    public static final String latestActionSubject = "latestActionSubject";
    public static final String revenueInit = "revenueInit";
    public static final String revenueInitEqual = "revenueInitEqual";
    public static final String revenueInitFrom = "revenueInitFrom";
    public static final String revenueInitTo = "revenueInitTo";
    public static final String revenueFinal = "revenueFinal";
    public static final String revenueFinalEqual = "revenueFinalEqual";
    public static final String revenueFinalFrom = "revenueFinalFrom";
    public static final String revenueFinalTo = "revenueFinalTo";
    // review deadlines
    // reviewDeadline
    public static final String reviewDeadline = "reviewDeadline";
    public static final String reviewDeadlineFrom = "reviewDeadlineFrom";
    public static final String reviewDeadlineTo = "reviewDeadlineTo";
    public static final String reviewDeadlineExp = "reviewDeadlineExp";
    public static final String reviewDeadlineOpen = "reviewDeadlineOpen";
    // renewalDeadline
    public static final String reviewRenewalDeadline = "reviewRenewalDeadline";
    public static final String reviewRenewalDeadlineFrom = "reviewRenewalDeadlineFrom";
    public static final String reviewRenewalDeadlineTo = "reviewRenewalDeadlineTo";
    public static final String reviewRenewalDeadlineExp = "reviewRenewalDeadlineExp";
    public static final String reviewRenewalDeadlineOpen = "reviewRenewalDeadlineOpen";
    // insReplyDeadline
    public static final String reviewInsReplyDeadline = "reviewInsReplyDeadline";
    public static final String reviewInsReplyDeadlineFrom = "reviewInsReplyDeadlineFrom";
    public static final String reviewInsReplyDeadlineTo = "reviewInsReplyDeadlineTo";
    public static final String reviewInsReplyDeadlineExp = "reviewInsReplyDeadlineExp";
    public static final String reviewInsReplyDeadlineOpen = "reviewInsReplyDeadlineOpen";
    // replySendDocDeadline
    public static final String reviewReplySendDocDeadline = "reviewReplySendDocDeadline";
    public static final String reviewReplySendDocDeadlineFrom = "reviewReplySendDocDeadlineFrom";
    public static final String reviewReplySendDocDeadlineTo = "reviewReplySendDocDeadlineTo";
    public static final String reviewReplySendDocDeadlineExp = "reviewReplySendDocDeadlineExp";
    public static final String reviewReplySendDocDeadlineOpen = "reviewReplySendDocDeadlineOpen";
    // completionDeadline
    public static final String reviewCompletionDeadline = "reviewCompletionDeadline";
    public static final String reviewCompletionDeadlineFrom = "reviewCompletionDeadlineFrom";
    public static final String reviewCompletionDeadlineTo = "reviewCompletionDeadlineTo";
    public static final String reviewCompletionDeadlineExp = "reviewCompletionDeadlineExp";
    public static final String reviewCompletionDeadlineOpen = "reviewCompletionDeadlineOpen";
    
    public static final String reviewDeadlineDays = "reviewDeadlineDays";
    public static final String reviewRenewalDeadlineDays = "reviewRenewalDeadlineDays";
    public static final String reviewInsReplyDeadlineDays = "reviewInsReplyDeadlineDays";
    public static final String reviewReplySendDocDeadlineDays = "reviewReplySendDocDeadlineDays";
    public static final String reviewCompletionDeadlineDays = "reviewCompletionDeadlineDays";
//penalty fee    
    public static final String penaltyFee = "penaltyFee";
    public static final String penaltyFeeFrom = "penaltyFeeFrom";
    public static final String penaltyFeeTo = "penaltyFeeTo";
// kain/inka key
    public static final String kainKeyEn = "kainKeyEn";
    public static final String inkaKeyEn = "inkaKeyEn";
//  einleitung PrüfV MD
    public static final String mdHosStartAudit = "mdHosStartAudit";
    public static final String mdHosStartAuditFrom = "mdHosStartAuditFrom"; 
    public static final String mdHosStartAuditTo = "mdHosStartAuditTo"; 


//   Datum der leistungsrechtlichen Entscheidung  
    public static final String dateOfBenefitLawDecision = "dateOfBenefitLawDecision";
     public static final String dateOfBenefitLawDecisionFrom = "dateOfBenefitLawDecisionFrom";
    public static final String dateOfBenefitLawDecisionTo = "dateOfBenefitLawDecisionTo";

    
    
   //These Columns are selected if a empty,new Filter is created
    protected static final List<String> DEFAULT_COLUMNS = Arrays.asList(workflowNumber, csCaseNumber, remLatestCreationDate, assSubject, assReceiver, remFinished, vmModUser, assUser, assLastModificationDate, patNumber, patName, csdAdmissionDate, csdDischargeDate);
    protected static final List<String> DEADLINES = Arrays.asList(billCorrectionDeadlineDays, mdkAuditCompletionDeadlineDays, mdkDocumentDeliverDeadlineDays, dataRecordCorrectionDeadlineDays, preliminaryProceedingAnswerDeadlineDays, preliminaryProceedingsClosedDeadlineDays);
    protected static final List<String> DEADLINES2 = Arrays.asList(billCorrectionDeadline, mdkAuditCompletionDeadline, preliminaryProceedingsClosedDeadline, dataRecordCorrectionDeadline, preliminaryProceedingAnswerDeadline, mdkDocumentDeliverDeadline);

    //private final List<CWmListReminderSubject> listofReminderSubjectObjects = null;
    //private ProcessServiceBeanRemote ProcessServiceBean;
    protected WorkflowListAttributes() {
        initKeys();
    }

    public static synchronized WorkflowListAttributes instance() {
        if (instance == null) {
            instance = new WorkflowListAttributes();
        }
        return instance;
    }

    public static List<String> getDefaultColumns() {
        return new ArrayList<>(DEFAULT_COLUMNS);
    }

    /**
     * TODO: move to base class
     *
     * @return creates list of default columns
     */
    public List<SearchListAttribute> createDefaultColumns() {
        List<SearchListAttribute> list = new ArrayList<>();
        for (String key : getDefaultColumns()) {
            SearchListAttribute attribute = get(key);
            list.add(attribute);
        }
        return list;
    }

    public static List<String> getDeadlines() {
        return new ArrayList<>(DEADLINES);
    }

    public static List<String> getDeadlines2() {
        return new ArrayList<>(DEADLINES2);
    }

//CPX-1108 RSH 20180814
    protected void initNoColumnKeys() {
        add(mdkAuditReasonsFilter, "T_WM_AUDIT_REASONS", "AUDIT_REASON_NUMBER", Lang.MDK_AUDIT_REASONS)
                .setFormat(new SearchListFormatMap(MdkAuditReasonsMap.class))
                .setSize(200)
                .setNoColumn(true);
        add(processFinalAuditReasonsFilter, "T_WM_AUDIT_REASONS", "AUDIT_REASON_NUMBER", Lang.PROCESS_FINAL_AUDIT_REASONS)
                .setFormat(new SearchListFormatMap(MdkAuditReasonsMap.class))
                .setSize(200)
                .setNoColumn(true);
//        add(requestStatusFilter, "T_WM_REQUEST", "REQUEST_STATE", Lang.getRequestStatus())
//                .setFormat(new SearchListFormatMap(MdkStatesMap.class))
//                .setSize(200)
//                .setNoColumn(true);

        add(actionSubjectFilter, "T_WM_ACTION", "ACTION_SUBJECT_ID", Lang.ACTION_SUBJECT)
                .setFormat(new SearchListFormatMap(ActionSubjectMap.class))
                .setSize(200)
                .setNoColumn(true);
        add(eventTypeFilter, "T_WM_EVENT", "EVENT_TYPE", Lang.EVENT_TYPE)
                .setFormat(new SearchListFormatEnum(WmEventTypeEn.class))
                .setSize(200)
                .setNoColumn(true);
    }

    @Override
    protected void initKeys() {
        initNoColumnKeys();
        add(id, "T_WM_PROCESS", "ID", null) //Vorgangs-ID
                .setFormat(new SearchListFormatInteger())
                .setVisible(false);

        add(csId, "T_CASE", "ID", null) //Fall-ID
                .setFormat(new SearchListFormatInteger())
                .setVisible(false);

        add(csdId, "T_CASE_DETAILS", "ID", null) //Fall-Details-ID
                .setFormat(new SearchListFormatInteger())
                .setVisible(false);

        add(workflowNumber, "T_WM_PROCESS", "WORKFLOW_NUMBER", Lang.WORKFLOW_NUMBER) //Vorgangsnr.
                .setFormat(new SearchListFormatInteger()
                        .setSignType(SearchListFormatInteger.SIGN_TYPE.POSITIVE))
                .setOperator(SearchListAttribute.OPERATOR.EQUAL)
                .setSize(100);
        add(courtFileNumber, "T_WM_PROCESS", "COURT_FILE_NUMBER", Lang.COURT_FILE_NUMBER) //Aktenzeichen (Gericht)
                .setFormat(new SearchListFormatString())
                .setSize(100);

        add(lawerFileNumber, "T_WM_PROCESS", "LAWER_FILE_NUMBER", Lang.LAWER_FILE_NUMBER) //Aktenzeichen (RA)
                .setFormat(new SearchListFormatString())
                .setSize(100);

        add(creationDate, "T_WM_PROCESS", "CREATION_DATE", Lang.PROCESS_START_DATE) //Anlegedatum
                //                .setFormat(new SearchListFormatDateTime())
                //                .setSize(120);
                .setFormat(new SearchListFormatDateTime())
                .setSize(130)
                .addEqualChild(add(creationDateEqual, getByKey(creationDate), Lang.PROCESS_START_DATE))
                .addBetweenChildren(
                        add(creationDateFrom, getByKey(creationDate), Lang.PROCESS_START_DATE_FROM),
                        add(creationDateTo, getByKey(creationDate), Lang.PROCESS_START_DATE_TO)
                );
        add(insuranceRecivedBillDate, "VIEW_LATEST_REQUEST_MDK", "INSURANCE_RECIVED_BILL", Lang.INSURANCE_BILL_RECIVED)
                .setFormat(new SearchListFormatDateTime())
                .setSize(130)
                .addEqualChild(add(insuranceRecivedBillDateEqual, getByKey(insuranceRecivedBillDate), Lang.INSURANCE_BILL_RECIVED))
                .addBetweenChildren(
                        add(insuranceRecivedBillDateFrom, getByKey(insuranceRecivedBillDate), Lang.INSURANCE_BILL_RECIVED_FROM),
                        add(insuranceRecivedBillDateTo, getByKey(insuranceRecivedBillDate), Lang.INSURANCE_BILL_RECIVED_TO)
                );
//        add(subject, "T_WM_PROCESS_HOSPITAL", "PROCESS_TOPIC", Lang.WORKFLOW_SUBJECT) //
//                .setFormat(new SearchListFormatMap(ProcessTopicMap.class))
//                .setSize(100);
        add(wmState, "T_WM_PROCESS", "WM_STATE", Lang.WORKFLOW_STATE) //
                .setFormat(new SearchListFormatEnum(WmWorkflowStateEn.class))
                .setSize(60);

//        add(wmType, "T_WM_PROCESS", "WM_TYPE", Lang.WORKFLOW_TYPE) //
//                .setFormat(new SearchListFormatCommon().setDataType(WorkflowType.class))
//                .setSize(60);
        add(patNumber, "T_PATIENT", "PAT_NUMBER", Lang.PATIENT_NUMBER) //Patientennummer
                .setFormat(new SearchListFormatString())
                .setSize(100);

        add(csHospitalIdent, "T_CASE", "CS_HOSPITAL_IDENT", Lang.HOSPITAL_IDENTIFIER)
                .setFormat(new SearchListFormatString())//IKZ des Krankenhauses
                .setHospital(true)
                .setSize(100);

        add(csCaseNumber, "T_CASE", "CS_CASE_NUMBER", Lang.CASE_NUMBER) //Fallnummer
                .setFormat(new SearchListFormatString())
                .setSize(100);
//        add(isCancel, "T_CASE", "CANCEL_FL", Lang.SAP_REFERENCE_TYPE_CANCELLATION) //cancelFl
//                .setFormat(new SearchListFormatBoolean())
//                .setSize(30);
        add(csdAdmissionDate, "T_CASE_DETAILS", "ADMISSION_DATE", Lang.ADMISSION_DATE) //Aufnahmedatum
                .setFormat(new SearchListFormatDateTime())
                .setSize(130)
                .addEqualChild(add(csdAdmissionDateEqual, getByKey(csdAdmissionDate), Lang.ADMISSION_DATE))
                .addBetweenChildren(
                        add(csdAdmissionDateFrom, getByKey(csdAdmissionDate), Lang.ADMISSION_DATE_FROM),
                        add(csdAdmissionDateTo, getByKey(csdAdmissionDate), Lang.ADMISSION_DATE_TO)
                );

        add(csdAdmCauseEn, "T_CASE_DETAILS", "ADMISSION_CAUSE_EN", Lang.ADMISSION_CAUSE) //Aufnahmeanlass
                .setFormat(new SearchListFormatEnum(AdmissionCauseEn.class))
                .setSize(60);

        add(csdDischargeDate, "T_CASE_DETAILS", "DISCHARGE_DATE", Lang.DISCHARGE_DATE) //Entlassungsdatum
                .setFormat(new SearchListFormatDateTime())
                .setSize(130)
                .addEqualChild(add(csdDischargeDateEqual, getByKey(csdDischargeDate), Lang.DISCHARGE_DATE))
                .addBetweenChildren(
                        add(csdDischargeDateFrom, getByKey(csdDischargeDate), Lang.DISCHARGE_DATE_FROM),
                        add(csdDischargeDateTo, getByKey(csdDischargeDate), Lang.DISCHARGE_DATE_TO)
                );
        add(assSubject, "T_WM_REMINDER", "SUBJECT", Lang.REMINDER_SUBJECT) //Wiedervorlage
                .setFormat(new SearchListFormatMap(ReminderSubjectMap.class))
                .setSize(100);

        add(assSender, "T_WM_REMINDER", "CREATION_USER", Lang.REMINDER_SENDER) //Wiedervorlage Sender
                .setFormat(new SearchListFormatMap(UserMap.class))
                .setSize(100);

        add(assReceiver, "T_WM_REMINDER", "ASSIGNED_USER_ID", Lang.REMINDER_RECEIVER) //Wiedervorlage Empfänger
                .setFormat(new SearchListFormatMap(UserMap.class))
                .setSize(130);

        add(assLastModificationDate, "T_WM_PROCESS", "LAST_PROCESS_MODIFICATION", Lang.UPDATED)
                //                .setFormat(new SearchListFormatDateTime())
                //                .setSize(130);
                .setFormat(new SearchListFormatDateTime())
                .setSize(130)
                .addEqualChild(add(assLastModificationDateEqual, getByKey(assLastModificationDate), Lang.UPDATED))
                .addBetweenChildren(
                        add(assLastModificationDateFrom, getByKey(assLastModificationDate), Lang.UPDATED_FROM),
                        add(assLastModificationDateTo, getByKey(assLastModificationDate), Lang.UPDATED_TO)
                );

//CPX-1026
//        add(csTypeOfService, "T_CASE_DETAILS", "ADMISSION_REASON_12_EN", Lang.TYPE_OF_SERVICE)
//                .setSize(100)
//                .setFormat(new SearchListFormatCommon().setDataType(WmProcessTypeOfService.class));
        //CPX-565 Name of Kasse as Tooltip 
        add(insInsCompany, "T_CASE", "INSURANCE_IDENTIFIER", Lang.INSURANCE_COMPANY) //IKZ der Versicherung
                .setFormat(new SearchListFormatString())
                .setInsurance(true)
                .setSize(100);
        //CPX-994 Name of Kasse as column
        add(insInsCompanyName, "T_CASE", "INSURANCE_IDENTIFIER", Lang.INSURANCE_NAME) //Versicherungsnamen
                .setFormat(new SearchListFormatMap(InsuranceMap.class))
                .setSortable(false)
                .setSize(100);
        add(insInsCompanyShortName, "T_CASE", "INSURANCE_IDENTIFIER", Lang.INS_SHORT) //Versicherungs-/Krankenkassengruppen
                .setFormat(new SearchListFormatMap(InsShortMap.class))
                .setSortable(false)
                .setSize(100);
        add(requestStartAudit, "T_WM_REQUEST", "START_AUDIT", Lang.INITIATION_TEST_PROCESSES)
                .setFormat(new SearchListFormatDateTime())
                .setSize(130)
                //CPX-1127  RSH 2018-08-20
                .addEqualChild(add(requestStartAuditEqual, getByKey(requestStartAudit), Lang.INITIATION_TEST_PROCESSES))
                .addBetweenChildren(
                        add(requestStartAuditFrom, getByKey(requestStartAudit), Lang.INITIATION_TEST_PROCESSES_FROM),
                        add(requestStartAuditTo, getByKey(requestStartAudit), Lang.INITIATION_TEST_PROCESSES_TO)
                );
        add(requestReportDate, "T_WM_REQUEST", "REPORT_DATE", Lang.REQUEST_REPORT_DATE) 
                .setFormat(new SearchListFormatDateTime())
                .setSize(130)
                //CPX-1127  RSH 2018-08-20
                .addEqualChild(add(requestReportDateEqual, getByKey(requestReportDate), Lang.getRequestReportDate())) 
                .addBetweenChildren(
                        add(requestReportDateFrom, getByKey(requestReportDate), Lang.getRequestReportDateFrom()), 
                        add(requestReportDateTo, getByKey(requestReportDate), Lang.getRequestReportDateTo()) 
                );

        add(remLatestCreationDate, "T_WM_REMINDER", "DUE_DATE", Lang.REMINDER_LATEST)
                .setFormat(new SearchListFormatDateTime())
                .setSize(130)
                .addEqualChild(add(remLatestCreationDateEqual, getByKey(remLatestCreationDate), Lang.REMINDER_LATEST))
                .addBetweenChildren(
                        add(remLatestCreationDateFrom, getByKey(remLatestCreationDate), Lang.REMINDER_LATEST_FROM),
                        add(remLatestCreationDateTo, getByKey(remLatestCreationDate), Lang.REMINDER_LATEST_TO)
                )
                .addOpenChild(add(remLatestCreationDateOpen, getByKey(remLatestCreationDate), "Wiedervorlage offen"))
                .addExpChild(add(remLatestCreationDateExp, getByKey(remLatestCreationDate), "Wiedervorlage bis heute"))
                .addTodayChild(add(remLatestCreationDateToday, getByKey(remLatestCreationDate), "Wiedervorlagen heute"));

        add(vmModUser, "T_WM_PROCESS", "PROCESS_MODIFICATION_USER", Lang.PROCESS_MODIFICATION_USER)
                .setFormat(new SearchListFormatMap(UserMap.class))
                .setSize(100);
        add(assUser, "T_WM_PROCESS", "ASSIGNED_USER", Lang.PROCESS_USER)
                .setFormat(new SearchListFormatMap(UserMap.class))
                .setSize(100);

        add(remFinished, "T_WM_REMINDER", "FINISHED_FL", Lang.PROCESS_STATUS_REMINDER)
                .setFormat(new SearchListFormatBoolean())
                .setSize(130);
        //RSH 15.03.2018 CPX-867
        add(processTopic, "T_WM_PROCESS_HOSPITAL", "PROCESS_TOPIC", Lang.PROCESS_TOPIC)
                .setFormat(new SearchListFormatMap(ProcessTopicMap.class))
                .setSize(100);
        //RSH 06.07.2018 CPX-1028
        add(processResult, "VIEW_PROCESS_HOSPITAL_FINALIS", "CLOSING_RESULT", Lang.PROCESS_FINALISATION_RESULT)
                .setFormat(new SearchListFormatMap(ProcessResultMap.class))
                .setSize(130);
        //RSH 11.01.2019 CPX-1355
        add(closingDate, "VIEW_PROCESS_HOSPITAL_FINALIS", "CLOSING_DATE_PROCESS", Lang.PROCESS_FINALISATION_CLOSING_DATE)
                .setFormat(new SearchListFormatDateTime())
                .setSize(130)
                .addEqualChild(add(closingDateEqual, getByKey(closingDate), Lang.PROCESS_FINALISATION_CLOSING_DATE))
                .addBetweenChildren(
                        add(closingDateFrom, getByKey(closingDate), Lang.PROCESS_FINALISATION_CLOSING_DATE_FROM),
                        add(closingDateTo, getByKey(closingDate), Lang.PROCESS_FINALISATION_CLOSING_DATE_TO)
                );

        //RSH 05042018 - CPX-908
        add(mdkAuditCompletionDeadline, "T_WM_PROCESS_HOSPITAL", "AUDIT_PREL_PROC_CL_DEADLINE", Lang.WORKFLOW_MDK_AUDIT_COMPLETION_DEADLINE)
                .setFormat(new SearchListFormatDateTime())
                .setSize(180)
                .addBetweenChildren(
                        add(mdkAuditCompletionDeadlineFrom, getByKey(mdkAuditCompletionDeadline), Lang.MDK_AUDIT_COMPLETION_DEADLINE_FROM),
                        add(mdkAuditCompletionDeadlineTo, getByKey(mdkAuditCompletionDeadline), Lang.MDK_AUDIT_COMPLETION_DEADLINE_TO)
                )
                .addOpenChild(add(mdkAuditCompletionDeadlineOpen, getByKey(mdkAuditCompletionDeadline), Lang.PROCESS_DEADLINE_OPEN))
                .addExpChild(add(mdkAuditCompletionDeadlineExp, getByKey(mdkAuditCompletionDeadline), Lang.PROCESS_DEADLINE_EXPIRED));

        add(billCorrectionDeadline, "T_WM_PROCESS_HOSPITAL", "MDK_BILL_CORRECTION_DEADLINE", Lang.BILL_CORRECTION_DEADLINE)
                .setFormat(new SearchListFormatDateTime())
                .setSize(180)
                .addBetweenChildren(
                        add(billCorrectionDeadlineFrom, getByKey(billCorrectionDeadline), Lang.BILL_CORRECTION_DEADLINE_FROM),
                        add(billCorrectionDeadlineTo, getByKey(billCorrectionDeadline), Lang.BILL_CORRECTION_DEADLINE_TO)
                )
                .addOpenChild(add(billCorrectionDeadlineOpen, getByKey(billCorrectionDeadline), Lang.PROCESS_DEADLINE_OPEN))
                .addExpChild(add(billCorrectionDeadlineExp, getByKey(billCorrectionDeadline), Lang.PROCESS_DEADLINE_EXPIRED));

        add(mdkDocumentDeliverDeadline, "T_WM_PROCESS_HOSPITAL", "MDK_DOC_DELIVER_DEADLINE", Lang.MDK_DOCUMENT_DELIVER_DEADLINE)
                .setFormat(new SearchListFormatDateTime())
                .setSize(180)
                .addBetweenChildren(
                        add(mdkDocumentDeliverDeadlineFrom, getByKey(mdkDocumentDeliverDeadline), Lang.MDK_DOCUMENT_DELIVER_DEADLINE_FROM),
                        add(mdkDocumentDeliverDeadlineTo, getByKey(mdkDocumentDeliverDeadline), Lang.MDK_DOCUMENT_DELIVER_DEADLINE_TO)
                )
                .addOpenChild(add(mdkDocumentDeliverDeadlineOpen, getByKey(mdkDocumentDeliverDeadline), Lang.PROCESS_DEADLINE_OPEN))
                .addExpChild(add(mdkDocumentDeliverDeadlineExp, getByKey(mdkDocumentDeliverDeadline), Lang.PROCESS_DEADLINE_EXPIRED));

        //RSH 05042018 - CPX-908
        add(preliminaryProceedingsClosedDeadline, "T_WM_PROCESS_HOSPITAL", "MDK_AUDIT_COMPLETION_DEADLINE", Lang.PRELIMINAR_PROCEEDINS_CLOSED_DEADLINE)
                .setFormat(new SearchListFormatDateTime())
                .setSize(180)
                .addBetweenChildren(
                        add(preliminaryProceedingsClosedDeadlineFrom, getByKey(preliminaryProceedingsClosedDeadline), Lang.PRELIMINAR_PROCEEDINS_CLOSED_DEADLINE_FROM),
                        add(preliminaryProceedingsClosedDeadlineTo, getByKey(preliminaryProceedingsClosedDeadline), Lang.PRELIMINAR_PROCEEDINS_CLOSED_DEADLINE_TO)
                )
                .addOpenChild(add(preliminaryProceedingsClosedDeadlineOpen, getByKey(preliminaryProceedingsClosedDeadline), Lang.PROCESS_DEADLINE_OPEN))
                .addExpChild(add(preliminaryProceedingsClosedDeadlineExp, getByKey(preliminaryProceedingsClosedDeadline), Lang.PROCESS_DEADLINE_EXPIRED));

        add(preliminaryProceedingAnswerDeadline, "T_WM_PROCESS_HOSPITAL", "AUDIT_PREL_PROC_ANS_DEADLINE", Lang.PRELIMINAR_PROCEEDINS_ANSWER_DEADLINE)
                .setFormat(new SearchListFormatDateTime())
                .setSize(180)
                .addBetweenChildren(
                        add(preliminaryProceedingAnswerDeadlineFrom, getByKey(preliminaryProceedingAnswerDeadline), Lang.PRELIMINAR_PROCEEDINS_ANSWER_DEADLINE_FROM),
                        add(preliminaryProceedingAnswerDeadlineTo, getByKey(preliminaryProceedingAnswerDeadline), Lang.PRELIMINAR_PROCEEDINS_ANSWER_DEADLINE_TO)
                )
                .addOpenChild(add(preliminaryProceedingAnswerDeadlineOpen, getByKey(preliminaryProceedingAnswerDeadline), Lang.PROCESS_DEADLINE_OPEN))
                .addExpChild(add(preliminaryProceedingAnswerDeadlineExp, getByKey(preliminaryProceedingAnswerDeadline), Lang.PROCESS_DEADLINE_EXPIRED));

        add(dataRecordCorrectionDeadline, "T_WM_PROCESS_HOSPITAL", "AUDIT_DATA_REC_CORR_DEADLINE", Lang.DATA_RECORD_CORRECTION_DEADLINE)
                .setFormat(new SearchListFormatDateTime())
                .setSize(180)
                .addBetweenChildren(
                        add(dataRecordCorrectionDeadlineFrom, getByKey(dataRecordCorrectionDeadline), Lang.DATA_RECORD_CORRECTION_DEADLINE_FROM),
                        add(dataRecordCorrectionDeadlineTo, getByKey(dataRecordCorrectionDeadline), Lang.DATA_RECORD_CORRECTION_DEADLINE_TO)
                )
                .addOpenChild(add(dataRecordCorrectionDeadlineOpen, getByKey(dataRecordCorrectionDeadline), Lang.PROCESS_DEADLINE_OPEN))
                .addExpChild(add(dataRecordCorrectionDeadlineExp, getByKey(dataRecordCorrectionDeadline), Lang.PROCESS_DEADLINE_EXPIRED));

        add(billCorrectionDeadlineDays, "", "", Lang.BILL_CORRECTION_DEADLINE_DAYS)
                .setFormat(new SearchListFormatInteger())
                .setSize(100)
                .setSortable(false)
                .setNoFilter(true);

        add(mdkAuditCompletionDeadlineDays, "", "", Lang.MDK_AUDIT_COMPLETION_DEADLINE_DAYS)
                .setFormat(new SearchListFormatInteger())
                .setSize(100)
                .setSortable(false)
                .setNoFilter(true);

        add(mdkDocumentDeliverDeadlineDays, "", "", Lang.MDK_DOCUMENT_DELIVER_DEADLINE_DAYS)
                .setFormat(new SearchListFormatInteger())
                .setSize(100)
                .setSortable(false)
                .setNoFilter(true);

        add(preliminaryProceedingsClosedDeadlineDays, "", "", Lang.PRELIMINAR_PROCEEDINS_CLOSED_DEADLINE_DAYS)
                .setFormat(new SearchListFormatInteger())
                .setSize(100)
                .setSortable(false)
                .setNoFilter(true);

        add(preliminaryProceedingAnswerDeadlineDays, "", "", Lang.PRELIMINAR_PROCEEDINS_ANSWER_DEADLINE_DAYS)
                .setFormat(new SearchListFormatInteger())
                .setSize(100)
                .setSortable(false)
                .setNoFilter(true);

        add(dataRecordCorrectionDeadlineDays, "", "", Lang.DATA_RECORD_CORRECTION_DEADLINE_DAYS)
                .setFormat(new SearchListFormatInteger())
                .setSize(100)
                .setSortable(false)
                .setNoFilter(true);

        add(requestType, "T_WM_REQUEST", "REQUEST_TYPE", Lang.REQUEST_TYPE)
                .setFormat(new SearchListFormatEnum(WmRequestTypeEn.class))
                .setSize(130);
        add(wvComment, "T_WM_REMINDER", "REMINDER_COMMENT", Lang.REMINDER_COMMENT)
                .setFormat(new SearchListFormatString())
                .setSize(100);
        add(actionComment, "VIEW_LATEST_ACTION", "ACTION_COMMENT", Lang.ACTION_COMMENT)
                .setFormat(new SearchListFormatString())
                .setSize(100)
                .setLongContent(true);
        add(wvPrio, "$IFNULL(T_WM_REMINDER.HIGH_PRIO_FL, 0)", "HIGH_PRIO_FL", Lang.PRIORITY_HIGH)
                .setFormat(new SearchListFormatBoolean())
                //.setNoFilter(true)
                //.setSearchControlDataTyp(Boolean.class)
                //.setIsClientSide(true)
                .setSize(30);
//
//        add(mainMdkAuditReasons, "MAIN_REASON", "AUDIT_REASON_NUMBER", Lang.MAIN_AUDIT_REASON)
//                //                .setFormat(new SearchListFormatString())
//                .setFormat(new SearchListFormatMap(MdkAuditReasonsMap.class))
//                .setSize(100);

        add(cwInitial, "VIEW_PROCESS_HOSPITAL_FINALIS", "CW_INITIAL", Lang.PROCESS_FINALISATION_CW_INITIAL)
                //                .setFormat(new SearchListFormatDouble())
                //                .setSize(100);
                .setFormat(new SearchListFormatDouble())
                .setSize(130)
                //                .addEqualChild(add(cwInitialEqual, getByKey(cwInitial), Lang.PROCESS_FINALISATION_CW_INITIAL))
                .addBetweenChildren(
                        add(cwInitialFrom, getByKey(cwInitial), Lang.PROCESS_FINALISATION_CW_INITIAL_FROM),
                        add(cwInitialTo, getByKey(cwInitial), Lang.PROCESS_FINALISATION_CW_INITIAL_TO)
                );

        add(cwDiff, "VIEW_PROCESS_HOSPITAL_FINALIS", "CW_DIFF", Lang.PROCESS_FINALISATION_CW_DIFF)
                //                .setFormat(new SearchListFormatDouble())
                //                .setSize(100);
                .setFormat(new SearchListFormatDouble())
                .setSize(130)
                //                .addEqualChild(add(cwDiffEqual, getByKey(cwDiff), Lang.PROCESS_FINALISATION_CW_DIFF))
                .addBetweenChildren(
                        add(cwDiffFrom, getByKey(cwDiff), Lang.PROCESS_FINALISATION_CW_DIFF_FROM),
                        add(cwDiffTo, getByKey(cwDiff), Lang.PROCESS_FINALISATION_CW_DIFF_TO)
                );

        add(cwFinal, "VIEW_PROCESS_HOSPITAL_FINALIS", "CW_FINAL", Lang.PROCESS_FINALISATION_CW_FINAL)
                //                .setFormat(new SearchListFormatDouble())
                //                .setSize(100);
                .setFormat(new SearchListFormatDouble())
                .setSize(130)
                //                .addEqualChild(add(cwFinalEqual, getByKey(cwFinal), Lang.PROCESS_FINALISATION_CW_FINAL))
                .addBetweenChildren(
                        add(cwFinalFrom, getByKey(cwFinal), Lang.PROCESS_FINALISATION_CW_FINAL_FROM),
                        add(cwFinalTo, getByKey(cwFinal), Lang.PROCESS_FINALISATION_CW_FINAL_TO)
                );

        add(drgInitial, "VIEW_PROCESS_HOSPITAL_FINALIS", "DRG_INITIAL", Lang.PROCESS_FINALISATION_DRG_INITIAL)
                .setFormat(new SearchListFormatString())
                .setSize(100);

        add(drgFinal, "VIEW_PROCESS_HOSPITAL_FINALIS", "DRG_FINAL", Lang.PROCESS_FINALISATION_DRG_FINAL)
                .setFormat(new SearchListFormatString())
                .setSize(100);

        add(supFeeInitial, "VIEW_PROCESS_HOSPITAL_FINALIS", "INITIAL_SUPPLEMENTARY_FEE", Lang.PROCESS_FINALISATION_SUPPLFEE_INITIAL)
                //                .setFormat(new SearchListFormatDouble())
                //                .setSize(100);
                .setFormat(new SearchListFormatDouble())
                .setSize(130)
                //                .addEqualChild(add(supFeeInitialEqual, getByKey(supFeeInitial), Lang.PROCESS_FINALISATION_SUPPLFEE_INITIAL))
                .addBetweenChildren(
                        add(supFeeInitialFrom, getByKey(supFeeInitial), Lang.PROCESS_FINALISATION_SUPPLFEE_INITIAL_FROM),
                        add(supFeeInitialTo, getByKey(supFeeInitial), Lang.PROCESS_FINALISATION_SUPPLFEE_INITIAL_TO)
                );

        add(supFeeDiff, "VIEW_PROCESS_HOSPITAL_FINALIS", "DIFF_SUPPLEMENTARY_FEE", Lang.PROCESS_FINALISATION_SUPPLFEE_DIFF)
                //                .setFormat(new SearchListFormatDouble())
                //                .setSize(100);
                .setFormat(new SearchListFormatDouble())
                .setSize(130)
                //                .addEqualChild(add(supFeeDiffEqual, getByKey(supFeeDiff), Lang.PROCESS_FINALISATION_SUPPLFEE_DIFF))
                .addBetweenChildren(
                        add(supFeeDiffFrom, getByKey(supFeeDiff), Lang.PROCESS_FINALISATION_SUPPLFEE_DIFF_FROM),
                        add(supFeeDiffTo, getByKey(supFeeDiff), Lang.PROCESS_FINALISATION_SUPPLFEE_DIFF_TO)
                );

        add(supFeeFinal, "VIEW_PROCESS_HOSPITAL_FINALIS", "FINAL_SUPPLEMENTARY_FEE", Lang.PROCESS_FINALISATION_SUPPLFEE_FINAL)
                //                .setFormat(new SearchListFormatDouble())
                //                .setSize(100);
                .setFormat(new SearchListFormatDouble())
                .setSize(130)
                //                .addEqualChild(add(supFeeFinalEqual, getByKey(supFeeFinal), Lang.PROCESS_FINALISATION_SUPPLFEE_FINAL))
                .addBetweenChildren(
                        add(supFeeFinalFrom, getByKey(supFeeFinal), Lang.PROCESS_FINALISATION_SUPPLFEE_FINAL_FROM),
                        add(supFeeFinalTo, getByKey(supFeeFinal), Lang.PROCESS_FINALISATION_SUPPLFEE_FINAL_TO)
                );

        add(losInitial, "VIEW_PROCESS_HOSPITAL_FINALIS", "LOS_INITIAL", Lang.PROCESS_FINALISATION_LOS_INITIAL)
                //                .setFormat(new SearchListFormatDouble())
                //                .setSize(100);
                .setFormat(new SearchListFormatInteger())
                .setSize(130)
                .addEqualChild(add(losInitialEqual, getByKey(losInitial), Lang.PROCESS_FINALISATION_LOS_INITIAL))
                .addBetweenChildren(
                        add(losInitialFrom, getByKey(losInitial), Lang.PROCESS_FINALISATION_LOS_INITIAL_FROM),
                        add(losInitialTo, getByKey(losInitial), Lang.PROCESS_FINALISATION_LOS_INITIAL_TO)
                );

        add(losDiff, "VIEW_PROCESS_HOSPITAL_FINALIS", "LOS_DIFF", Lang.PROCESS_FINALISATION_LOS_DIFF)
                //                .setFormat(new SearchListFormatDouble())
                //                .setSize(100);
                .setFormat(new SearchListFormatInteger())
                .setSize(130)
                .addEqualChild(add(losDiffEqual, getByKey(losDiff), Lang.PROCESS_FINALISATION_LOS_DIFF))
                .addBetweenChildren(
                        add(losDiffFrom, getByKey(losDiff), Lang.PROCESS_FINALISATION_LOS_DIFF_FROM),
                        add(losDiffTo, getByKey(losDiff), Lang.PROCESS_FINALISATION_LOS_DIFF_TO)
                );

        add(losFinal, "VIEW_PROCESS_HOSPITAL_FINALIS", "LOS_FINAL", Lang.PROCESS_FINALISATION_LOS_FINAL)
                //                .setFormat(new SearchListFormatDouble())
                //                .setSize(100);
                .setFormat(new SearchListFormatInteger())
                .setSize(130)
                .addEqualChild(add(losFinalEqual, getByKey(losFinal), Lang.PROCESS_FINALISATION_LOS_FINAL))
                .addBetweenChildren(
                        add(losFinalFrom, getByKey(losFinal), Lang.PROCESS_FINALISATION_LOS_FINAL_FROM),
                        add(losFinalTo, getByKey(losFinal), Lang.PROCESS_FINALISATION_LOS_FINAL_TO)
                );
        add(caseType, "T_CASE", "CS_CASE_TYPE_EN", Lang.CASE_TYPE) //Fallart (PEPP, DRG)
                .setFormat(new SearchListFormatEnum(CaseTypeEn.class))
                .setSize(60);

        add(revenueDiff, "VIEW_PROCESS_HOSPITAL_FINALIS", "REVENUE_DIFF", Lang.PROCESS_FINALISATION_REVENUE_DIFF)
                .setFormat(new SearchListFormatDouble())
                .setSize(130)
                //                .addEqualChild(add(revenueDiffEqual, getByKey(revenueDiff), Lang.PROCESS_FINALISATION_REVENUE_DIFF))
                .addBetweenChildren(
                        add(revenueDiffFrom, getByKey(revenueDiff), Lang.PROCESS_FINALISATION_REVENUE_DIFF_FROM),
                        add(revenueDiffTo, getByKey(revenueDiff), Lang.PROCESS_FINALISATION_REVENUE_DIFF_TO)
                );
        add(requestState, "T_WM_REQUEST", "REQUEST_STATE", Lang.REQUEST_STATUS)
                .setFormat(new SearchListFormatMap(MdkStatesMap.class))
                .setSize(200);
        add(mdkSubseqProcDate, "VIEW_LATEST_REQUEST_MDK", "SUBSEQUENT_PROCEEDING_DATE", Lang.MDK_SUBSEQUENT_PROCEEDING_DATE)
                .setFormat(new SearchListFormatDateTime())
                .setSize(130)
                .addEqualChild(add(mdkSubseqProcDateEqual, getByKey(mdkSubseqProcDate), Lang.MDK_SUBSEQUENT_PROCEEDING_DATE))
                .addBetweenChildren(
                        add(mdkSubseqProcDateFrom, getByKey(mdkSubseqProcDate), Lang.MDK_SUBSEQUENT_PROCEEDING_DATE_FORM),
                        add(mdkSubseqProcDateTo, getByKey(mdkSubseqProcDate), Lang.MDK_SUBSEQUENT_PROCEEDING_DATE_TO)
                );
        add(auditNames, "VW_AGG_AUDITS", "AUDIT_NAMES_LIST", Lang.AUDIT_AUDIT_REASONS)
                .setFormat(new SearchListFormatMap(MdkAuditReasonsMap.class))
                .setSize(200);
        add(isCancel, "T_WM_PROCESS", "CANCEL_FL", Lang.SAP_REFERENCE_TYPE_CANCELLATION) //cancelFl
                .setFormat(new SearchListFormatBoolean())
                .setSize(30);
        
        add(cwCareInitial, "VIEW_PROCESS_HOSPITAL_FINALIS", "CW_CARE_INITIAL", Lang.PROCESS_FINALISATION_CW_CARE_INITIAL)
                //                .setFormat(new SearchListFormatDouble())
                //                .setSize(100);
                .setFormat(new SearchListFormatDouble())
                .setSize(130).setIs4DecimalDigits(true)
                //                .addEqualChild(add(cwInitialEqual, getByKey(cwInitial), Lang.PROCESS_FINALISATION_CW_INITIAL))
                .addBetweenChildren(
                        add(cwCareInitialFrom, getByKey(cwCareInitial), Lang.PROCESS_FINALISATION_CW_CARE_INITIAL_FROM),
                        add(cwCareInitialTo, getByKey(cwCareInitial), Lang.PROCESS_FINALISATION_CW_CARE_INITIAL_TO)
                );

        add(cwCareDiff, "VIEW_PROCESS_HOSPITAL_FINALIS", "CW_CARE_DIFF", Lang.PROCESS_FINALISATION_CW_CARE_DIFF)
                //                .setFormat(new SearchListFormatDouble())
                //                .setSize(100);
                .setFormat(new SearchListFormatDouble())
                .setSize(130).setIs4DecimalDigits(true)
                //                .addEqualChild(add(cwDiffEqual, getByKey(cwDiff), Lang.PROCESS_FINALISATION_CW_DIFF))
                .addBetweenChildren(
                        add(cwCareDiffFrom, getByKey(cwCareDiff), Lang.PROCESS_FINALISATION_CW_CARE_DIFF_FROM),
                        add(cwCareDiffTo, getByKey(cwCareDiff), Lang.PROCESS_FINALISATION_CW_CARE_DIFF_TO)
                );

        add(cwCareFinal, "VIEW_PROCESS_HOSPITAL_FINALIS", "CW_CARE_FINAL", Lang.PROCESS_FINALISATION_CW_CARE_FINAL)
                //                .setFormat(new SearchListFormatDouble())
                //                .setSize(100);
                .setFormat(new SearchListFormatDouble())
                .setSize(130).setIs4DecimalDigits(true)
                //                .addEqualChild(add(cwFinalEqual, getByKey(cwFinal), Lang.PROCESS_FINALISATION_CW_FINAL))
                .addBetweenChildren(
                        add(cwCareFinalFrom, getByKey(cwCareFinal), Lang.PROCESS_FINALISATION_CW_CARE_FINAL_FROM),
                        add(cwCareFinalTo, getByKey(cwCareFinal), Lang.PROCESS_FINALISATION_CW_CARE_FINAL_TO)
                );
        add(savedMoney, "VIEW_PROCESS_HOSPITAL_FINALIS", "SAVED_MONEY", Lang.PROCESS_FINALISATION_SAVED_MONEY)
                //                .setFormat(new SearchListFormatDouble())
                //                .setSize(100);
                .setFormat(new SearchListFormatDouble())
                .setSize(130)
                //                .addEqualChild(add(cwFinalEqual, getByKey(cwFinal), Lang.PROCESS_FINALISATION_CW_FINAL))
                .addBetweenChildren(
                        add(savedMoneyFrom, getByKey(savedMoney), Lang.PROCESS_FINALISATION_SAVED_MONEY_FROM),
                        add(savedMoneyTo, getByKey(savedMoney), Lang.PROCESS_FINALISATION_SAVED_MONEY_TO)
                );
        add(resultDelta, "VIEW_PROCESS_HOSPITAL_FINALIS", "RESULT_DELTA", Lang.PROCESS_FINALISATION_RESULT_DELTA)
                //                .setFormat(new SearchListFormatDouble())
                //                .setSize(100);
                .setFormat(new SearchListFormatDouble())
                .setSize(130)
                //                .addEqualChild(add(cwFinalEqual, getByKey(cwFinal), Lang.PROCESS_FINALISATION_CW_FINAL))
                .addBetweenChildren(
                        add(resultDeltaFrom, getByKey(resultDelta), Lang.PROCESS_FINALISATION_RESULT_DELTA_FROM),
                        add(resultDeltaTo, getByKey(resultDelta), Lang.PROCESS_FINALISATION_RESULT_DELTA_TO)
                );
        
        add(latestActionSubject, "VIEW_LATEST_ACTION", "ACTION_SUBJECT_ID", Lang.ACTION_SUBJECT_LATEST)
                .setFormat(new SearchListFormatMap(ActionSubjectMap.class))
                .setSize(130);

        add(revenueInit, "VIEW_PROCESS_HOSPITAL_FINALIS", "REVENUE_INITIAL", Lang.PROCESS_FINALISATION_REVENUE_INIT)
                .setFormat(new SearchListFormatDouble())
                .setSize(130)
//                .addEqualChild(add(revenueInitEqual, getByKey(revenueInit), Lang.PROCESS_FINALISATION_REVENUE_DIFF))
                .addBetweenChildren(
                        add(revenueInitFrom, getByKey(revenueInit), Lang.PROCESS_FINALISATION_REVENUE_INIT_FROM),
                        add(revenueInitTo, getByKey(revenueInit), Lang.PROCESS_FINALISATION_REVENUE_INIT_TO)
                );
        add(revenueFinal, "VIEW_PROCESS_HOSPITAL_FINALIS", "REVENUE_FINAL", Lang.PROCESS_FINALISATION_REVENUE_FINAL)
                .setFormat(new SearchListFormatDouble())
                .setSize(130)
//                .addEqualChild(add(revenueInitEqual, getByKey(revenueInit), Lang.PROCESS_FINALISATION_REVENUE_DIFF))
                .addBetweenChildren(
                        add(revenueFinalFrom, getByKey(revenueFinal), Lang.PROCESS_FINALISATION_REVENUE_FINAL_FROM),
                        add(revenueFinalTo, getByKey(revenueFinal), Lang.PROCESS_FINALISATION_REVENUE_FINAL_TO)
                );
// review columns
    // reviewDeadline
//    public static final String reviewDeadline = "reviewDeadline";
//    public static final String reviewDeadlineFrom = "reviewDeadlineFrom";
//    public static final String reviewDeadlineTo = "reviewDeadlineTo";
//    public static final String reviewDeadlineExp = "reviewDeadlineExp";
//    public static final String reviewDeadlineOpen = "reviewDeadlineOpen";
        add(reviewDeadline, "T_WM_PROCESS_HOSPITAL", "REVIEW_DEADLINE", Lang.PROCESS_REVIEW_DEADLINE_DATE)
                .setFormat(new SearchListFormatDateTime())
                .setSize(180)
                .addBetweenChildren(
                        add(reviewDeadlineFrom, getByKey(reviewDeadline), Lang.PROCESS_REVIEW_DEADLINE_DATE_FROM),
                        add(reviewDeadlineTo, getByKey(reviewDeadline), Lang.PROCESS_REVIEW_DEADLINE_DATE_TO)
                )
                .addOpenChild(add(reviewDeadlineOpen, getByKey(reviewDeadline), Lang.PROCESS_DEADLINE_OPEN))
                .addExpChild(add(reviewDeadlineExp, getByKey(reviewDeadline), Lang.PROCESS_DEADLINE_EXPIRED));
//    // renewalDeadline
//    public static final String reviewRenewalDeadline = "reviewRenewalDeadline";
//    public static final String reviewRenewalDeadlineFrom = "reviewRenewalDeadlineFrom";
//    public static final String reviewRenewalDeadlineTo = "reviewRenewalDeadlineTo";
//    public static final String reviewRenewalDeadlineExp = "reviewRenewalDeadlineExp";
//    public static final String reviewRenewalDeadlineOpen = "reviewRenewalDeadlineOpen";
        add(reviewRenewalDeadline, "T_WM_PROCESS_HOSPITAL", "REVIEW_RENEWAL_DEADLINE", Lang.PROCESS_REVIEW_DEADLINE_EXTENDED_DATE)
                .setFormat(new SearchListFormatDateTime())
                .setSize(180)
                .addBetweenChildren(
                        add(reviewRenewalDeadlineFrom, getByKey(reviewRenewalDeadline), Lang.PROCESS_REVIEW_DEADLINE_EXTENDED_DATE_FROM),
                        add(reviewRenewalDeadlineTo, getByKey(reviewRenewalDeadline), Lang.PROCESS_REVIEW_DEADLINE_EXTENDED_DATE_TO)
                )
                .addOpenChild(add(reviewRenewalDeadlineOpen, getByKey(reviewRenewalDeadline), Lang.PROCESS_DEADLINE_OPEN))
                .addExpChild(add(reviewRenewalDeadlineExp, getByKey(reviewRenewalDeadline), Lang.PROCESS_DEADLINE_EXPIRED));
//    // insReplyDeadline
//    public static final String reviewInsReplyDeadline = "reviewInsReplyDeadline";
//    public static final String reviewInsReplyDeadlineFrom = "reviewInsReplyDeadlineFrom";
//    public static final String reviewInsReplyDeadlineTo = "reviewInsReplyDeadlineTo";
//    public static final String reviewInsReplyDeadlineExp = "reviewInsReplyDeadlineExp";
//    public static final String reviewInsReplyDeadlineOpen = "reviewInsReplyDeadlineOpen";
        add(reviewInsReplyDeadline, "T_WM_PROCESS_HOSPITAL", "REVIEW_INS_REPLY_DEADLINE", Lang.PROCESS_REVIEW_DEADLINE_ANSWER_INSURANCE_DATE)
                .setFormat(new SearchListFormatDateTime())
                .setSize(180)
                .addBetweenChildren(
                        add(reviewInsReplyDeadlineFrom, getByKey(reviewInsReplyDeadline), Lang.PROCESS_REVIEW_DEADLINE_ANSWER_INSURANCE_DATE_FROM),
                        add(reviewInsReplyDeadlineTo, getByKey(reviewInsReplyDeadline), Lang.PROCESS_REVIEW_DEADLINE_ANSWER_INSURANCE_DATE_TO)
                )
                .addOpenChild(add(reviewInsReplyDeadlineOpen, getByKey(reviewInsReplyDeadline), Lang.PROCESS_DEADLINE_OPEN))
                .addExpChild(add(reviewInsReplyDeadlineExp, getByKey(reviewInsReplyDeadline), Lang.PROCESS_DEADLINE_EXPIRED));
//    // replySendDocDeadline
//    public static final String reviewReplySendDocDeadline = "reviewReplySendDocDeadline";
//    public static final String reviewReplySendDocDeadlineFrom = "reviewReplySendDocDeadlineFrom";
//    public static final String reviewReplySendDocDeadlineTo = "reviewReplySendDocDeadlineTo";
//    public static final String reviewReplySendDocDeadlineExp = "reviewReplySendDocDeadlineExp";
//    public static final String reviewReplySendDocDeadlineOpen = "reviewReplySendDocDeadlineOpen";
        add(reviewReplySendDocDeadline, "T_WM_PROCESS_HOSPITAL", "REVIEW_REPLY_SEND_DOC_DL", Lang.PROCESS_REVIEW_DEADLINE_SEND_ON_DATE)
                .setFormat(new SearchListFormatDateTime())
                .setSize(180)
                .addBetweenChildren(
                        add(reviewReplySendDocDeadlineFrom, getByKey(reviewReplySendDocDeadline), Lang.PROCESS_REVIEW_DEADLINE_SEND_ON_DATE_FROM),
                        add(reviewReplySendDocDeadlineTo, getByKey(reviewReplySendDocDeadline), Lang.PROCESS_REVIEW_DEADLINE_SEND_ON_DATE_TO)
                )
                .addOpenChild(add(reviewReplySendDocDeadlineOpen, getByKey(reviewReplySendDocDeadline), Lang.PROCESS_DEADLINE_OPEN))
                .addExpChild(add(reviewReplySendDocDeadlineExp, getByKey(reviewReplySendDocDeadline), Lang.PROCESS_DEADLINE_EXPIRED));
//    // completionDeadline
//    public static final String reviewCompletionDeadline = "reviewCompletionDeadline";
//    public static final String reviewCompletionDeadlineFrom = "reviewCompletionDeadlineFrom";
//    public static final String reviewCompletionDeadlineTo = "reviewCompletionDeadlineTo";
//    public static final String reviewCompletionDeadlineExp = "reviewCompletionDeadlineExp";
//    public static final String reviewCompletionDeadlineOpen = "reviewCompletionDeadlineOpen";
        add(reviewCompletionDeadline, "T_WM_PROCESS_HOSPITAL", "REVIEW_COMPLETION_DEADLINE", Lang.PROCESS_REVIEW_COMPLETION_DEADLINE_DATE)
                .setFormat(new SearchListFormatDateTime())
                .setSize(180)
                .addBetweenChildren(
                        add(reviewCompletionDeadlineFrom, getByKey(reviewCompletionDeadline), Lang.PROCESS_REVIEW_COMPLETION_DEADLINE_DATE_FROM),
                        add(reviewCompletionDeadlineTo, getByKey(reviewCompletionDeadline), Lang.PROCESS_REVIEW_COMPLETION_DEADLINE_DATE_TO)
                )
                .addOpenChild(add(reviewCompletionDeadlineOpen, getByKey(reviewCompletionDeadline), Lang.PROCESS_DEADLINE_OPEN))
                .addExpChild(add(reviewCompletionDeadlineExp, getByKey(reviewCompletionDeadline), Lang.PROCESS_DEADLINE_EXPIRED));


        add(reviewDeadlineDays, "", "", Lang.PROCESS_REVIEW_DEADLINE_DAYS)
                .setFormat(new SearchListFormatInteger())
                .setSize(100)
                .setSortable(false)
                .setNoFilter(true);

        add(reviewRenewalDeadlineDays, "", "", Lang.PROCESS_REVIEW_DEADLINE_EXTENDED_DAYS)
                .setFormat(new SearchListFormatInteger())
                .setSize(100)
                .setSortable(false)
                .setNoFilter(true);

        add(reviewInsReplyDeadlineDays, "", "", Lang.PROCESS_REVIEW_DEADLINE_ANSWER_INSURANCE_DAYS)
                .setFormat(new SearchListFormatInteger())
                .setSize(100)
                .setSortable(false)
                .setNoFilter(true);

        add(reviewReplySendDocDeadlineDays, "", "", Lang.PROCESS_REVIEW_DEADLINE_SEND_ON_DAYS)
                .setFormat(new SearchListFormatInteger())
                .setSize(100)
                .setSortable(false)
                .setNoFilter(true);

        add(reviewCompletionDeadlineDays, "", "", Lang.PROCESS_REVIEW_COMPLETION_DEADLINE_DAYS)
                .setFormat(new SearchListFormatInteger())
                .setSize(100)
                .setSortable(false)
                .setNoFilter(true);

        add(penaltyFee, "VIEW_PROCESS_HOSPITAL_FINALIS", "PENALTY_FEE", Lang.PROCESS_FINALISATION_PENALTY_FEE)
                //                .setFormat(new SearchListFormatDouble())
                //                .setSize(100);
                .setFormat(new SearchListFormatDouble())
                .setSize(130)
                //                .addEqualChild(add(cwFinalEqual, getByKey(cwFinal), Lang.PROCESS_FINALISATION_CW_FINAL))
                .addBetweenChildren(
                        add(penaltyFeeFrom, getByKey(penaltyFee), Lang.PROCESS_FINALISATION_PENALTY_FEE_FROM),
                        add(penaltyFeeTo, getByKey(penaltyFee), Lang.PROCESS_FINALISATION_PENALTY_FEE_TO)
                );
        
        add(kainKeyEn, "VIEW_WM_KAIN_KEY_LIST", "KEY_LIST", Lang.PROCESS_KAIN_KEY) //kain
                .setFormat(new SearchListFormatEnum(Tp301Key30En.class))
//                .setFormat(new SearchListFormatString())
                .setSize(130).setOperator(SearchListAttribute.OPERATOR.LIKE_BOTH_SIDES);

        add(inkaKeyEn, "VIEW_WM_INKA_KEY_LIST", "KEY_LIST", Lang.PROCESS_INKA_KEY) //inka
//                .setFormat(new SearchListFormatString())
                .setFormat(new SearchListFormatEnum(Tp301Key30En.class))
                .setSize(130).setOperator(SearchListAttribute.OPERATOR.LIKE_BOTH_SIDES);

        add(mdHosStartAudit, "VIEW_LATEST_REQUEST_MDK", "MD_HOS_START_AUDIT", Lang.MDK_START_AUDIT)
                .setFormat(new SearchListFormatDateTime())
                .setSize(180)
                .addBetweenChildren(
                        add(mdHosStartAuditFrom, getByKey(mdHosStartAudit), Lang.MDK_START_AUDIT_FROM),
                        add(mdHosStartAuditTo, getByKey(mdHosStartAudit), Lang.MDK_START_AUDIT_TO)
                );


        add(dateOfBenefitLawDecision, "VIEW_WM_KAIN_KEY_LIST", "DECISION_DATE", Lang.PROCESS_KAIN_DATE_BENEFIT_LAW_DECISION)
                .setFormat(new SearchListFormatDateTime())
                .setSize(180)
                .addBetweenChildren(
                        add(dateOfBenefitLawDecisionFrom, getByKey(dateOfBenefitLawDecision), Lang.PROCESS_KAIN_DATE_BENEFIT_LAW_DECISION_FROM),
                        add(dateOfBenefitLawDecisionTo, getByKey(dateOfBenefitLawDecision), Lang.PROCESS_KAIN_DATE_BENEFIT_LAW_DECISION_TO)
                );


    }

}
