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
package de.lb.cpx.shared.filter.enums;

import de.lb.cpx.model.enums.AdmissionReasonEn;
import de.lb.cpx.model.enums.PlaceOfRegEn;
import de.lb.cpx.model.enums.QuarterEn;
import static de.lb.cpx.shared.filter.enums.WorkingListAttributes.csdAdmissionDate;
import static de.lb.cpx.shared.filter.enums.WorkingListAttributes.csdAdmissionDateFrom;
import static de.lb.cpx.shared.filter.enums.WorkingListAttributes.csdAdmissionDateTo;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.enums.WmWorkflowStateEn;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author niemeier
 */
public class QuotaListAttributes extends WorkingListAttributes {

    private static QuotaListAttributes instance = null;
    public static final String id = "id";
//    public static final String csBillingDate = "csBillingDate";
//    public static final String csBillingDateEqual = "csBillingDateEqual";
//    public static final String csBillingDateFrom = "csBillingDateFrom";
//    public static final String csBillingDateTo = "csBillingDateTo";
    public static final String csBillingQuarter = "csBillingQuarter";
    public static final String riskPercentTotal = "riskPercentTotal";
    public static final String riskPercentTotalFrom = "riskPercentTotalFrom";
    public static final String riskPercentTotalTo = "riskPercentTotalTo";
    public static final String riskPercentTotalEqual = "riskPercentTotalEqual";
    public static final String riskValueTotal = "riskValueTotal";
    public static final String riskValueTotalFrom = "riskValueTotalFrom";
    public static final String riskValueTotalTo = "riskValueTotalTo";
    public static final String riskValueTotalEqual = "riskValueTotalEqual";
    public static final String riskPlaceOfReg = "riskPlaceOfReg";
    public static final String workflowNumber = "workflowNumber";
    public static final String requestState = "requestState";
    public static final String wmState = "wmState";
    public static final String processTopic = "processTopic";
    public static final String processResult = "processResult";
    public static final String insuranceRecivedBillDate = "insuranceRecivedBillDate";
    public static final String insuranceRecivedBillDateFrom = "insuranceRecivedBillDateFrom";
    public static final String insuranceRecivedBillDateTo = "insuranceRecivedBillDateTo";
    public static final String insuranceRecivedBillDateEqual = "insuranceRecivedBillDateEqual";
    public static final String startAudit = "startAudit";
    public static final String mdkStartAuditDateFrom = "mdkStartAuditDateFrom";
    public static final String mdkStartAuditDateTo = "mdkStartAuditDateTo";
    public static final String mdkStartAuditDateEqual = "mdkStartAuditDateEqual";
    public static final String mdkStartAuditQuarter = "mdkStartAuditQuarter";
    public static final String mdkStartAuditExtendedDate = "mdkStartAuditExtendedDate";
    public static final String mdkStartAuditExtendedDateFrom = "mdkStartAuditExtendedDateFrom";
    public static final String mdkStartAuditExtendedDateTo = "mdkStartAuditExtendedDateTo";
    public static final String mdkStartAuditExtendedDateEqual = "mdkStartAuditExtendedDateEqual";
    public static final String reportDate = "reportDate";
    public static final String mdkReportCreationDateFrom = "mdkReportCreationDateFrom";
    public static final String mdkReportCreationDateTo = "mdkReportCreationDateTo";
    public static final String mdkReportCreationDateEqual = "mdkReportCreationDateEqual";
    public static final String mdkReportCreationQuarter = "mdkReportCreationQuarter";
    public static final String mdk = "mdk";
    public static final String requestId = "requestId";
    public static final String processId = "processId";

    protected static final List<String> QUOTA_COLUMNS = Arrays.asList(WorkingListAttributes.id, csHospitalIdent, csCaseNumber, csCaseTypeEn, riskPlaceOfReg, riskPercentTotal, riskValueTotal);

    protected QuotaListAttributes() {
        initKeys();
    }

    public static List<String> getQuotaColumns() {
        return new ArrayList<>(QUOTA_COLUMNS);
    }

    public static synchronized QuotaListAttributes instance() {
        if (instance == null) {
            instance = new QuotaListAttributes();
        }
        return instance;
    }

    @Override
    protected final void initKeys() {
        add(id, "T_CASE", "ID", "CaseId") //Fall-ID
                .setVisible(false);
        initNoColumnKeys();

        add(csCaseNumber, "T_CASE", "CS_CASE_NUMBER", Lang.CASE_NUMBER)
                .setFormat(new SearchListFormatString())
                .setSize(100);
        add(csHospitalIdent, "T_CASE", "CS_HOSPITAL_IDENT", Lang.HOSPITAL_IDENTIFIER) //IKZ des Krankenhauses
                .setFormat(new SearchListFormatString())
                .setHospital(true)
                .setSize(100);
        add(csdAdmReason12En, "T_CASE_DETAILS", "ADMISSION_REASON_12_EN", Lang.ADMISSION_REASON) //Aufnahmegrund (Stelle 1 und 2)
                .setFormat(new SearchListFormatEnum(AdmissionReasonEn.class))
                .setSize(60);
//        add(csCaseTypeEn, "T_CASE", "CS_CASE_TYPE_EN", Lang.CASE_TYPE) //Fallart (PEPP, DRG)
//                .setFormat(new SearchListFormatEnum(CaseTypeEn.class))
//                .setSize(60);
        add(csdAdmissionDate, "T_CASE_DETAILS", "ADMISSION_DATE", Lang.ADMISSION_DATE) //Aufnahmedatum
                .setFormat(new SearchListFormatDateTime())
                .setSize(130)
                .addBetweenChildren(
                        add(csdAdmissionDateFrom, "T_CASE_DETAILS", "ADMISSION_DATE", Lang.ADMISSION_DATE_FROM)
                                //.setOperator(SearchListAttribute.OPERATOR.GREATER_THAN_OR_EQUAL_TO)
                                .setFormat(new SearchListFormatDateTime()),
                        add(csdAdmissionDateTo, "T_CASE_DETAILS", "ADMISSION_DATE", Lang.ADMISSION_DATE_TO)
                                //.setOperator(SearchListAttribute.OPERATOR.LESS_THAN_OR_EQUAL_TO)
                                //.setNoFilter(true)
                                .setFormat(new SearchListFormatDateTime())
                );
        add(csdDischargeDate, "T_CASE_DETAILS", "DISCHARGE_DATE", Lang.DISCHARGE_DATE) //Entlassungsdatum
                .setFormat(new SearchListFormatDateTime())
                .setSize(130)
                .addEqualChild(add(csdDischargeDateEqual, getByKey(csdDischargeDate), Lang.DISCHARGE_DATE))
                .addBetweenChildren(
                        add(csdDischargeDateFrom, getByKey(csdDischargeDate), Lang.DISCHARGE_DATE_FROM),
                        add(csdDischargeDateTo, getByKey(csdDischargeDate), Lang.DISCHARGE_DATE_TO)
                );
        add(csDrg, "VIEW_GRPRES", "GRPRES_CODE", "DRG") // drg
                .setFormat(new SearchListFormatString())
                .setSize(130);
//                .setFormat(new SearchListFormatDateTime())
//                .setSize(130);
        add(workflowNumber, "T_WM_PROCESS", "WORKFLOW_NUMBER", Lang.WORKFLOW_NUMBER) //Vorgangsnr.
                .setFormat(new SearchListFormatInteger()
                        .setSignType(SearchListFormatInteger.SIGN_TYPE.POSITIVE))
                .setOperator(SearchListAttribute.OPERATOR.EQUAL)
                .setSize(100);
        add(csBillingDate, "T_CASE", "CS_BILLING_DATE", Lang.BILLING_DATE) //Entlassungsdatum
                .setFormat(new SearchListFormatDateTime())
                .setSize(130)
                .addEqualChild(add(csBillingDateEqual, getByKey(csBillingDate), Lang.BILLING_DATE))
                .addBetweenChildren(
                        add(csBillingDateFrom, getByKey(csBillingDate), Lang.BILLING_DATE_FROM),
                        add(csBillingDateTo, getByKey(csBillingDate), Lang.BILLING_DATE_TO)
                );
        add(csBillingQuarter, "$QUARTER(T_CASE.CS_BILLING_DATE)", "QUARTER", Lang.BILLING_QUARTER) //Entlassungsdatum Wochentag
                .setFormat(new SearchListFormatEnum(QuarterEn.class))
                .setSize(60);
        add(icdCode, "T_CASE_DETAILS", "HD_ICD_CODE", Lang.ICDCODE) //Haupt-ICD
                .setFormat(new SearchListFormatString())
                .setSize(60);
        add(insInsCompany, "T_CASE", "INSURANCE_IDENTIFIER", Lang.INSURANCE_COMPANY) //Versicherung
                .setFormat(new SearchListFormatString())
                .setInsurance(true)
                .setSize(100);
        add(insInsCompanyName, "T_CASE", "INSURANCE_IDENTIFIER", Lang.INSURANCE_NAME) //Versicherungsnamen
                .setFormat(new SearchListFormatMap(InsuranceMap.class))
                .setSortable(false)
                .setSize(100);
        add(insInsCompanyShortName, "T_CASE", "INSURANCE_IDENTIFIER", Lang.INS_SHORT) //Versicherungs-/Krankenkassengruppen
                .setFormat(new SearchListFormatMap(InsShortMap.class))
                .setSortable(false)
                .setSize(100);
        add(riskPercentTotal, "T_WM_RISK", "RISK_PERCENT_TOTAL", Lang.RISK_PERCENT_TOTAL) //Risiko in % (gesamt)
                .setFormat(new SearchListFormatInteger())
                .setSize(100)
                .addEqualChild(add(riskPercentTotalEqual, getByKey(riskPercentTotal), Lang.RISK_PERCENT_TOTAL_EQUAL))
                .addBetweenChildren(
                        add(riskPercentTotalFrom, getByKey(riskPercentTotal), Lang.RISK_PERCENT_TOTAL_FROM),
                        add(riskPercentTotalTo, getByKey(riskPercentTotal), Lang.RISK_PERCENT_TOTAL_TO)
                );
        add(riskValueTotal, "T_WM_RISK", "RISK_VALUE_TOTAL", Lang.RISK_VALUE_TOTAL) //Risiko in % (gesamt)
                .setFormat(new SearchListFormatDouble())
                .setSize(100)
                .addEqualChild(add(riskValueTotalEqual, getByKey(riskValueTotal), Lang.RISK_VALUE_TOTAL_EQUAL))
                .addBetweenChildren(
                        add(riskValueTotalFrom, getByKey(riskValueTotal), Lang.RISK_VALUE_TOTAL_FROM),
                        add(riskValueTotalTo, getByKey(riskValueTotal), Lang.RISK_VALUE_TOTAL_TO)
                );
        add(riskPlaceOfReg, "T_WM_RISK", "RISK_PLACE_OF_REG", Lang.RISK_PLACE_OF_REG) //Ort der (Risiko-)Erfassung
                .setFormat(new SearchListFormatEnum(PlaceOfRegEn.class))
                .setSize(100);
        add(requestState, "T_WM_REQUEST", "REQUEST_STATE", Lang.MDK_STATUS)
                .setFormat(new SearchListFormatMap(MdkStatesMap.class))
                .setSize(200);
        add(processResult, "T_WM_PROCESS_HOSPITAL_FINALIS", "CLOSING_RESULT", Lang.PROCESS_FINALISATION_RESULT)
                .setFormat(new SearchListFormatMap(ProcessResultMap.class))
                .setSize(130);
        add(processTopic, "T_WM_PROCESS_HOSPITAL", "PROCESS_TOPIC", Lang.PROCESS_TOPIC)
                .setFormat(new SearchListFormatMap(ProcessTopicMap.class))
                .setSize(100);
        add(wmState, "T_WM_PROCESS", "WM_STATE", Lang.WORKFLOW_STATE) //
                .setFormat(new SearchListFormatEnum(WmWorkflowStateEn.class))
                .setSize(60);
        add(insuranceRecivedBillDate, "T_WM_REQUEST_MDK", "INSURANCE_RECIVED_BILL", Lang.INSURANCE_BILL_RECIVED)
                .setFormat(new SearchListFormatDateTime())
                .setSize(130)
                .addEqualChild(add(insuranceRecivedBillDateEqual, getByKey(insuranceRecivedBillDate), Lang.INSURANCE_BILL_RECIVED))
                .addBetweenChildren(
                        add(insuranceRecivedBillDateFrom, getByKey(insuranceRecivedBillDate), Lang.INSURANCE_BILL_RECIVED_FROM),
                        add(insuranceRecivedBillDateTo, getByKey(insuranceRecivedBillDate), Lang.INSURANCE_BILL_RECIVED_TO)
                );
        add(startAudit, "T_WM_REQUEST", "START_AUDIT", Lang.MDK_START_AUDIT)
                .setFormat(new SearchListFormatDateTime())
                .setSize(130)
                .addEqualChild(add(mdkStartAuditDateEqual, getByKey(startAudit), Lang.MDK_START_AUDIT_EQUAL))
                .addBetweenChildren(add(mdkStartAuditDateFrom, getByKey(startAudit), Lang.MDK_START_AUDIT_FROM),
                        add(mdkStartAuditDateTo, getByKey(startAudit), Lang.MDK_START_AUDIT_TO)
                );
        add(mdkStartAuditQuarter, "$QUARTER(T_WM_REQUEST_MDK.MDK_START_AUDIT)", "QUARTER", Lang.MDK_START_AUDIT_QUARTER) //Entlassungsdatum Wochentag
                .setFormat(new SearchListFormatEnum(QuarterEn.class))
                .setSize(60);
        add(mdkStartAuditExtendedDate, "T_WM_REQUEST_MDK", "MDK_START_AUDIT_EXTENDED", Lang.MDK_START_AUDIT_EXTENDED)
                .setFormat(new SearchListFormatDateTime())
                .setSize(130)
                .addEqualChild(add(mdkStartAuditExtendedDateEqual, getByKey(mdkStartAuditExtendedDate), Lang.MDK_START_AUDIT_EXTENDED_EQUAL))
                .addBetweenChildren(
                        add(mdkStartAuditExtendedDateFrom, getByKey(mdkStartAuditExtendedDate), Lang.MDK_START_AUDIT_EXTENDED_FROM),
                        add(mdkStartAuditExtendedDateTo, getByKey(mdkStartAuditExtendedDate), Lang.MDK_START_AUDIT_EXTENDED_TO)
                );
        add(reportDate, "T_WM_REQUEST", "REPORT_DATE", Lang.MDK_REPORT_CREATION_DATE)
                .setFormat(new SearchListFormatDateTime())
                .setSize(130)
                .addEqualChild(add(mdkReportCreationDateEqual, getByKey(reportDate), Lang.MDK_REPORT_CREATION_DATE_EQUAL))
                .addBetweenChildren(add(mdkReportCreationDateFrom, getByKey(reportDate), Lang.MDK_REPORT_CREATION_DATE_FROM),
                        add(mdkReportCreationDateTo, getByKey(reportDate), Lang.MDK_REPORT_CREATION_DATE_TO)
                );
        add(mdkReportCreationQuarter, "$QUARTER(T_WM_REQUEST.REPORT_DATE)", "QUARTER", Lang.MDK_REPORT_CREATION_QUARTER)
                .setFormat(new SearchListFormatEnum(QuarterEn.class))
                .setSize(60);        
        add(mdk, "T_WM_REQUEST_MDK", "MDK_INTERNAL_ID", Lang.MDK_NAME)
                .setFormat(new SearchListFormatMap(MdkMap.class))
                .setSize(130);
        add(requestId, "T_WM_REQUEST", "ID", null) //Anfrage-ID
                .setFormat(new SearchListFormatInteger())
                .setVisible(false);
        add(processId, "T_WM_PROCESS", "ID", null) //Vorgangs-ID
                .setFormat(new SearchListFormatInteger())
                .setVisible(false);
    }

}
