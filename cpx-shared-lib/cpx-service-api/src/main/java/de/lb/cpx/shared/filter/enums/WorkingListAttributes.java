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
package de.lb.cpx.shared.filter.enums;

import de.lb.cpx.model.enums.AdmissionCauseEn;
import de.lb.cpx.model.enums.AdmissionReason2En;
import de.lb.cpx.model.enums.AdmissionReasonEn;
import de.lb.cpx.model.enums.CaseStatusEn;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.DischargeReasonEn;
import de.lb.cpx.model.enums.GenderEn;
import de.lb.cpx.model.enums.GrouperMdcOrSkEn;
import de.lb.cpx.model.enums.WeekdayEn;
import static de.lb.cpx.shared.filter.enums.WorkingListAttributes.csCaseTypeEn;
import de.lb.cpx.shared.lang.Lang;
import static de.lb.cpx.shared.lang.Lang.BILLING_DATE;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Wilde
 */
public class WorkingListAttributes extends SearchListAttributes {

    private static WorkingListAttributes instance = null;

    public static final String id = "id"; //caseId
    public static final String csdId = "csdId"; //caseDetailsId
//    public static final String patId = "patId"; //patientId
//    public static final String patdId = "patdId"; //patientDetailsId
    //final public static String grpresCode = "grpresCode";
    public static final String csCaseNumber = "csCaseNumber";

    //final public static String csStatusEn = "csStatusEn";
    //final public static String creationDate = "creationDate";
    public static final String csdAdmissionDate = "csdAdmissionDate";
    public static final String csdAdmissionDateEqual = "csdAdmissionDateEqual";
    public static final String csdAdmissionDateFrom = "csdAdmissionDateFrom";
    public static final String csdAdmissionDateTo = "csdAdmissionDateTo";
    public static final String csdAdmissionDateWeekday = "csdAdmissionDateWeekday";
    public static final String csdAdmCauseEn = "csdAdmCauseEn";
    public static final String csdAdmReason12En = "csdAdmReason12En";
    public static final String csdAdmReason34En = "csdAdmReason34En";
    public static final String csdDischargeDate = "csdDischargeDate";
    public static final String csdDischargeDateEqual = "csdDischargeDateEqual";
    public static final String csdDischargeDateFrom = "csdDischargeDateFrom";
    public static final String csdDischargeDateTo = "csdDischargeDateTo";
    public static final String csdDischargeDateWeekday = "csdDischargeDateWeekday";
    public static final String csdDisReason12En = "csdDisReason12En";
    public static final String csCaseTypeEn = "csCaseTypeEn";
    public static final String csHospitalIdent = "csHospitalIdent";
    public static final String grpresGroup = "grpresGroup";
    public static final String csdLeave = "csdLeave";
    public static final String csdLeaveEqual = "csdLeaveEqual";
    public static final String csdLeaveFrom = "csdLeaveFrom";
    public static final String csdLeaveTo = "csdLeaveTo";
    public static final String csdLos = "csdLos";
    public static final String csdLosEqual = "csdLosEqual";
    public static final String csdLosFrom = "csdLosFrom";
    public static final String csdLosTo = "csdLosTo";
    public static final String patNumber = "patNumber";
    public static final String csdAgeYears = "csdAgeYears";
    public static final String csdAgeYearsEqual = "csdAgeYearsEqual";
    public static final String csdAgeYearsFrom = "csdAgeYearsFrom";
    public static final String csdAgeYearsTo = "csdAgeYearsTo";
    public static final String csdAgeDays = "csdAgeDays";
    public static final String csdAgeDaysEqual = "csdAgeDaysEqual";
    public static final String csdAgeDaysFrom = "csdAgeDaysFrom";
    public static final String csdAgeDaysTo = "csdAgeDaysTo";
    //final public static String patGenderEn = "patGenderEn";
    public static final String csdAdmissionWeight = "csdAdmissionWeight";
    public static final String csdAdmissionWeightEqual = "csdAdmissionWeightEqual";
    public static final String csdAdmissionWeightFrom = "csdAdmissionWeightFrom";
    public static final String csdAdmissionWeightTo = "csdAdmissionWeightTo";
    public static final String csdHmv = "csdHmv";
    public static final String csdHmvEqual = "csdHmvEqual";
    public static final String csdHmvFrom = "csdHmvFrom";
    public static final String csdHmvTo = "csdHmvTo";
    //final public static String patdZipcode = "patdZipcode";
    //final public static String pccl = "pccl";
    //final public static String countSD = "countSD";
    //final public static String countProc = "countProc";
    public static final String csCreationDate = "csCreationDate";
    public static final String csCreationDateEqual = "csCreationDateEqual";
    public static final String csCreationDateFrom = "csCreationDateFrom";
    public static final String csCreationDateTo = "csCreationDateTo";
    public static final String csStatusEn = "csStatusEn";
    public static final String csdLosAlteration = "csdLosAlteration";
    public static final String csdLosAlterationEqual = "csdLosAlterationEqual";
    public static final String csdLosAlterationFrom = "csdLosAlterationFrom";
    public static final String csdLosAlterationTo = "csdLosAlterationTo";
    public static final String csdVersionNumber = "csdVersionNumber";
    public static final String csdVersionNumberEqual = "csdVersionNumberEqual";
    public static final String csdVersionNumberFrom = "csdVersionNumberFrom";
    public static final String csdVersionNumberTo = "csdVersionNumberTo";
    public static final String icdCode = "icdCode";
    public static final String patGenderEn = "patGenderEn";
    public static final String patdZipcode = "patdZipcode";
    public static final String insInsCompany = "insInsCompany";
    public static final String insInsCompanyName = "insInsCompanyName";// Versicherungsnamen
    public static final String insInsCompanyShortName = "insInsCompanyShortName"; //Versicherungs-/Krankenkassengruppen
    public static final String countSd = "countSd";
    public static final String countSdEqual = "countSdEqual";
    public static final String countSdFrom = "countSdFrom";
    public static final String countSdTo = "countSdTo";
    public static final String countProc = "countProc";
    public static final String countProcEqual = "countProcEqual";
    public static final String countProcFrom = "countProcFrom";
    public static final String countProcTo = "countProcTo";
    //Customer extensions:
    public static final String string1 = "string1";
    public static final String string2 = "string2";
    public static final String string3 = "string3";
    public static final String string4 = "string4";
    public static final String string5 = "string5";
    public static final String string6 = "string6";
    public static final String string7 = "string7";
    public static final String string8 = "string8";
    public static final String string9 = "string9";
    public static final String string10 = "string10";
    public static final String numeric1 = "numeric1";
    public static final String numeric2 = "numeric2";
    public static final String numeric3 = "numeric3";
    public static final String numeric4 = "numeric4";
    public static final String numeric5 = "numeric5";
    public static final String numeric6 = "numeric6";
    public static final String numeric7 = "numeric7";
    public static final String numeric8 = "numeric8";
    public static final String numeric9 = "numeric9";
    public static final String numeric10 = "numeric10";
    public static final String cwPositive = "cwPositive";
    public static final String cwNegative = "cwNegative";
    public static final String cwEffective = "cwEffective";
    public static final String cwEffectiveEqual = "cwEffectiveEqual";
    public static final String cwEffectiveFrom = "cwEffectiveFrom";
    public static final String cwEffectiveTo = "cwEffectiveTo";
    public static final String cwCatalog = "cwCatalog";
    public static final String csDrg = "csDrg";
    public static final String caseFees = "caseFees";
    public static final String caseSupplFees = "caseSupplFees";
    public static final String caseIcds = "caseIcds";
    public static final String caseOpses = "caseOpses";
    public static final String departmentMd = "departmentMd";
    public static final String admDiagnosis = "admDiagnosis"; //aufnehmende Diagnose
    public static final String hosDiagnosis = "hosDiagnosis"; //einweisende Diagnose
    public static final String caseDepartments = "caseDepartments";
    public static final String caseDepartmentsKey301 = "caseDepartmentsKey301";
//    public static final String caseDepartmentsMap = "caseDepartmentsMap";
    public static final String rules = "rules";
    public static final String dCWmin = "dCWmin";
    public static final String dCWminEqual = "dCWminEqual";
    public static final String dCWminFrom = "dCWminFrom";
    public static final String dCWminTo = "dCWminTo";
    public static final String dCWmax = "dCWmax";
    public static final String dCWmaxEqual = "dCWmaxEqual";
    public static final String dCWmaxFrom = "dCWmaxFrom";
    public static final String dCWmaxTo = "dCWmaxTo";
    public static final String isCancel = "isCancel";
    
    public static final String dCareCWmin = "dCareCWmin";
    public static final String dCareCWminEqual = "dCareCWminEqual";
    public static final String dCareCWminFrom = "dCareCWminFrom";
    public static final String dCareCWminTo = "dCareCWminTo";
    public static final String dCareCWmax = "dCareCWmax";
    public static final String dCareCWmaxEqual = "dCareCWmaxEqual";
    public static final String dCareCWmaxFrom = "dCareCWmaxFrom";
    public static final String dCareCWmaxTo = "dCareCWmaxTo";
    
    public static final String dRevenueMin = "dRevenueMin";
    public static final String dRevenueMinEqual = "dRevenueMinEqual";
    public static final String dRevenueMinFrom = "dRevenueMinFrom";
    public static final String dRevenueMinTo = "dRevenueMinTo";
    public static final String dRevenueMax = "dRevenueMax";
    public static final String dRevenueMaxEqual = "dRevenueMaxEqual";
    public static final String dRevenueMaxFrom = "dRevenueMaxFrom";
    public static final String dRevenueMaxTo = "dRevenueMaxTo";
    
    public static final String careCw = "careCw";
    public static final String careCwFrom = "careCwFrom";
    public static final String careCwTo = "careCwTo";
    
    public static final String pccl = "pccl";
    public static final String pcclEqual = "pcclEqual";    
    public static final String pcclFrom = "pcclFrom";    
    public static final String pcclTo = "pcclTo";    
    
    // billing date
    public static final String csBillingDate = "csBillingDate";
    public static final String csBillingDateFrom = "csBillingDateFrom";
    public static final String csBillingDateTo = "csBillingDateTo";
     public static final String csBillingDateEqual = "csBillingDateEqual";

    //These Columns are selected if a empty,new Filter is created
    protected static final List<String> DEFAULT_COLUMNS = Arrays.asList(csHospitalIdent, csCaseNumber, patName, patGenderEn, csdAgeYears, csStatusEn, csdAdmissionDate, csdDischargeDate, csDrg, grpresGroup, csdLos, cwEffective, dCWmax, dCWmin);


    protected WorkingListAttributes() {
        initKeys2();
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

    public static synchronized WorkingListAttributes instance() {
        if (instance == null) {
            instance = new WorkingListAttributes();
        }
        return instance;
    }

    /*
    public License getLicense() {
        CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
        final String licenseFilename = cpxProps.getCpxServerLicenseDir() + "\\" + LicenseWriter.DEFAULT_LICENSE_FILENAME;
        License license = null;
        try {
            license = License.loadFromLicenseFile(licenseFilename);
        } catch (IllegalArgumentException ex) {
            LOG.log(Level.FINER, "No CPX license available", ex);
        }
        return license;
    }
     */
    private void initKeys2() {
        initKeys();
    }

    protected void initNoColumnKeys() {
        //20180629-AWI:
        //NOTE: client currently only draws simple text fields, 
        //if some other format than string is to be supported fix implementation in FilterTableView first!
        //CPX-529
        add(caseFees, "T_CASE_FEE", "FEEC_FEEKEY", Lang.CASEFEE)
                .setFormat(new SearchListFormatString())
                .setSize(200)
                .setNoColumn(true);
        //CPX-1572 
        add(caseSupplFees, "T_CASE_SUPPL_FEE", "SUPPL_FEE_CODE", Lang.CASE_RESOLVE_SUPPLEMENTARY_CHARGE)
                .setFormat(new SearchListFormatString())
                .setSize(200)
                .setNoColumn(true);

        //CPX-551
        add(caseIcds, "T_CASE_ICD", "ICDC_CODE", Lang.SECONDARY_DIAGNOSES)
                .setFormat(new SearchListFormatString())
                .setSize(200)
                .setNoColumn(true);

        add(caseOpses, "T_CASE_OPS", "OPSC_CODE", Lang.OPSCODE)
                .setFormat(new SearchListFormatString())
                .setSize(200)
                .setNoColumn(true);

        add(departmentMd, "T_CASE_ICD", "ICDC_CODE", Lang.DEPARTMENT_MAIN_DIAGNOSIS)
                .setFormat(new SearchListFormatString())
                .setSize(200)
                .setNoColumn(true);

        add(admDiagnosis, "T_CASE_ICD", "ICDC_CODE", Lang.ADMISSION_DIAGNOSIS)
                .setFormat(new SearchListFormatString())
                .setSize(200)
                .setNoColumn(true);

        add(hosDiagnosis, "T_CASE_ICD", "ICDC_CODE", Lang.HOSPITALIZATION_DIAGNOSIS)
                .setFormat(new SearchListFormatString())
                .setSize(200)
                .setNoColumn(true);

        add(caseDepartments, "T_CASE_DEPARTMENT", "DEP_SHORT_NAME", Lang.DEPARTMENTS)
                .setFormat(new SearchListFormatString())
                .setSize(200)
                .setNoColumn(true);

//        add(caseDepartments, "T_CASE_DEPARTMENT", "DEP_SHORT_NAME", Lang.DEPARTMENTS)
//                .setFormat(new SearchListStringFormatMap(DepartmentShortNameMap.class))
//                .setSize(200)
//                .setNoColumn(true);
        add(caseDepartmentsKey301, "T_CASE_DEPARTMENT", "DEP_KEY_301", "Abteilungen (§301)")
                .setFormat(new SearchListFormatString())
                .setSize(200)
                .setNoColumn(true);

        add(rules, "T_CHECK_RESULT", "CRGR_ID", "Regel-ID")
                .setFormat(new SearchListFormatString())
                .setSize(200)
                .setNoColumn(true);
    }

    @Override
    protected void initKeys() {
        add(id, "T_CASE", "ID", null) //Fall-ID
                .setFormat(new SearchListFormatInteger())
                .setVisible(false);

        initNoColumnKeys();

        add(csdId, "T_CASE_DETAILS", "ID", null) //Fall-Details-ID
                .setFormat(new SearchListFormatInteger())
                .setVisible(false);

//        add(patId, "T_PATIENT", "ID", "PatientId") //Patienten-ID
//                .setVisible(false);
//
//        add(patdId, "T_PATIENT_DETAILS", "ID", "PatientDetailsId") //Patienten-Details-ID
//                .setVisible(false);

        /*      add(grpresCode, Lang.FEE_GROUP) //Entgeltbereich
              .setFormat(new SearchListFormatCommon().setDataType(FeeGroup.class))
              .setSize(85)
              .setDisabled(true);
         */
        add(csCaseNumber, "T_CASE", "CS_CASE_NUMBER", Lang.CASE_NUMBER) //Fallnummer
                .setFormat(new SearchListFormatString())
                .setSize(100);

        add(csHospitalIdent, "T_CASE", "CS_HOSPITAL_IDENT", Lang.HOSPITAL_IDENTIFIER) //IKZ des Krankenhauses
                .setFormat(new SearchListFormatString())
                .setHospital(true)
                .setSize(100);

        add(csCreationDate, "T_CASE", "CREATION_DATE", Lang.CREATION_DATE) //Anlegedatum
                .setFormat(new SearchListFormatDateTime())
                .setSize(130)
                .addEqualChild(add(csCreationDateEqual, getByKey(csCreationDate), Lang.CREATION_DATE))
                .addBetweenChildren(
                        add(csCreationDateFrom, getByKey(csCreationDate), Lang.CREATION_DATE_FROM),
                        add(csCreationDateTo, getByKey(csCreationDate), Lang.CREATION_DATE_TO)
                );
//                .setFormat(new SearchListFormatDateTime())
//                .setSize(130);

        add(csStatusEn, "T_CASE", "CS_STATUS_EN", Lang.CASE_STATUS) //FS (Fallstatus)
                .setFormat(new SearchListFormatEnum(CaseStatusEn.class))
                .setSize(70);
        add(isCancel, "T_CASE", "CANCEL_FL", Lang.SAP_REFERENCE_TYPE_CANCELLATION) //cancelFl
                .setFormat(new SearchListFormatBoolean())
                .setSize(30);

        add(icdCode, "T_CASE_DETAILS", "HD_ICD_CODE", Lang.ICDCODE) //Haupt-ICD
                .setFormat(new SearchListFormatString())
                .setSize(60);

        add(patGenderEn, "T_CASE_DETAILS", "GENDER_EN", Lang.GENDER) //Geschlecht
                .setFormat(new SearchListFormatEnum(GenderEn.class))
                .setSize(40);

        add(patdZipcode, "T_PATIENT_DETAILS", "PATD_ZIPCODE", Lang.ADDRESS_ZIP_CODE) //Einzugsgebiet
                .setFormat(new SearchListFormatString())
                .setSize(40);

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
        add(countSd, "T_CASE_DETAILS", "SUM_OF_ICD", Lang.COUNT_SD) //Anzahl Nebendiagnosen
                //                .setFormat(new SearchListFormatInteger()
                //                        .setSignType(SearchListFormatInteger.SIGN_TYPE.POSITIVE))
                //                .setSize(40);
                .setFormat(new SearchListFormatInteger()
                        .setSignType(SearchListFormatInteger.SIGN_TYPE.POSITIVE))
                .setSize(40)
                .addEqualChild(add(countSdEqual, getByKey(countSd), Lang.COUNT_SD))
                .addBetweenChildren(
                        add(countSdFrom, getByKey(countSd), Lang.COUNT_SD_FROM),
                        add(countSdTo, getByKey(countSd), Lang.COUNT_SD_TO)
                );

        add(countProc, "T_CASE_DETAILS", "SUM_OF_OPS", Lang.COUNT_PROC) //Anzahl Prozeduren
                //                .setFormat(new SearchListFormatInteger()
                //                        .setSignType(SearchListFormatInteger.SIGN_TYPE.POSITIVE))
                //                .setSize(40);
                .setFormat(new SearchListFormatInteger()
                        .setSignType(SearchListFormatInteger.SIGN_TYPE.POSITIVE))
                .setSize(40)
                .addEqualChild(add(countProcEqual, getByKey(countProc), Lang.COUNT_PROC))
                .addBetweenChildren(
                        add(countProcFrom, getByKey(countProc), Lang.COUNT_PROC_FROM),
                        add(countProcTo, getByKey(countProc), Lang.COUNT_PROC_TO)
                );
        /*
      add(csStatusEn, Lang.CASE_STATUS) //Fallstatus
              .setFormat(new SearchListFormatCommon().setDataType(CsStatus.class))
              .setSize(40)
              .setDisabled(true);
      
      add(creationDate, Lang.CREATION_DATE) //Anlegedatum des Falls
              .setFormat(new SearchListFormatDateTime())
              .setSize(100)
              .setDisabled(true);
         */
        add(csdAdmissionDate, "T_CASE_DETAILS", "ADMISSION_DATE", Lang.ADMISSION_DATE) //Aufnahmedatum
                .setFormat(new SearchListFormatDateTime())
                .setSize(130)
                .addEqualChild(add(csdAdmissionDateEqual, getByKey(csdAdmissionDate), Lang.ADMISSION_DATE))
                .addBetweenChildren(
                        add(csdAdmissionDateFrom, getByKey(csdAdmissionDate), Lang.ADMISSION_DATE_FROM),
                        add(csdAdmissionDateTo, getByKey(csdAdmissionDate), Lang.ADMISSION_DATE_TO)
                );

//                .addSingleChild(add(csdAdmissionDateEqual, "T_CASE_DETAILS", "ADMISSION_DATE", Lang.ADMISSION_DATE)
//                                //.setOperator(OPERATOR.GREATER_THAN_OR_EQUAL_TO)
//                                .setFormat(new SearchListFormatDateTime())
//                )
//                .addBetweenChildren(
//                        add(csdAdmissionDateFrom, "T_CASE_DETAILS", "ADMISSION_DATE", Lang.ADMISSION_DATE_FROM)
//                                //.setOperator(OPERATOR.GREATER_THAN_OR_EQUAL_TO)
//                                .setFormat(new SearchListFormatDateTime()),
//                        add(csdAdmissionDateTo, "T_CASE_DETAILS", "ADMISSION_DATE", Lang.ADMISSION_DATE_TO)
//                                //.setOperator(OPERATOR.LESS_THAN_OR_EQUAL_TO)
//                                //.setNoFilter(true)
//                                .setFormat(new SearchListFormatDateTime())
//                );
        add(csdAdmissionDateWeekday, "$WEEKDAY(T_CASE_DETAILS.ADMISSION_DATE)", "WEEKDAY", Lang.ADMISSION_WEEKDAY) //Aufnahmedatum Wochentag
                .setFormat(new SearchListFormatEnum(WeekdayEn.class))
                .setSize(60);

        add(csdAdmCauseEn, "T_CASE_DETAILS", "ADMISSION_CAUSE_EN", Lang.ADMISSION_CAUSE) //Aufnahmeanlass
                .setFormat(new SearchListFormatEnum(AdmissionCauseEn.class))
                .setSize(60);

        add(csdAdmReason12En, "T_CASE_DETAILS", "ADMISSION_REASON_12_EN", Lang.ADMISSION_REASON) //Aufnahmegrund (Stelle 1 und 2)
                .setFormat(new SearchListFormatEnum(AdmissionReasonEn.class))
                .setSize(60);

        add(csdAdmReason34En, "T_CASE_DETAILS", "ADMISSION_REASON_34_EN", Lang.ADMISSION_REASON_2) //Aufnahmegrund (Stelle 3 und 4)
                .setFormat(new SearchListFormatEnum(AdmissionReason2En.class))
                .setSize(60);

        add(csdDischargeDate, "T_CASE_DETAILS", "DISCHARGE_DATE", Lang.DISCHARGE_DATE) //Entlassungsdatum
                .setFormat(new SearchListFormatDateTime())
                .setSize(130)
                .addEqualChild(add(csdDischargeDateEqual, getByKey(csdDischargeDate), Lang.DISCHARGE_DATE))
                .addBetweenChildren(
                        add(csdDischargeDateFrom, getByKey(csdDischargeDate), Lang.DISCHARGE_DATE_FROM),
                        add(csdDischargeDateTo, getByKey(csdDischargeDate), Lang.DISCHARGE_DATE_TO)
                );
//                .setFormat(new SearchListFormatDateTime())
//                .setSize(130);

        add(csdDischargeDateWeekday, "$WEEKDAY(T_CASE_DETAILS.DISCHARGE_DATE)", "WEEKDAY", Lang.DISCHARGE_WEEKDAY) //Entlassungsdatum Wochentag
                .setFormat(new SearchListFormatEnum(WeekdayEn.class))
                .setSize(60);

        add(csdDisReason12En, "T_CASE_DETAILS", "DISCHARGE_REASON_12_EN", Lang.DISCHARGE_REASON) //Entlassungsgrund (Stelle 1 und 2)
                .setFormat(new SearchListFormatEnum(DischargeReasonEn.class))
                .setSize(60);

        add(csCaseTypeEn, "T_CASE", "CS_CASE_TYPE_EN", Lang.CASE_TYPE) //Fallart (PEPP, DRG)
                .setFormat(new SearchListFormatEnum(CaseTypeEn.class))
                .setSize(60);

        add(grpresGroup, "VIEW_GRPRES", "GRPRES_GROUP", Lang.MDC) //Grouperergebnis Hauptdiagnosegruppe (Major Diagnostic Category)
                .setFormat(new SearchListFormatEnum(GrouperMdcOrSkEn.class))
                .setSize(40);
//        add(grpresGroup, "VIEW_GRPRES", "GRPRES_GROUP", getMdcSkColumnHeaderName()) //Grouperergebnis Hauptdiagnosegruppe (Major Diagnostic Category)
//                .setFormat(new SearchListFormatEnum(GrouperMdcOrSkEn.class))
//                .setSize(40);

        add(csdLeave, "T_CASE_DETAILS", "LEAVE", Lang.UNBILLED_DAYS) //Tage ohne Berechnung
                .setFormat(new SearchListFormatInteger()
                        .setMaxLength(10)
                        .setSignType(SearchListFormatInteger.SIGN_TYPE.POSITIVE))
                .setSize(40)
                .addEqualChild(add(csdLeaveEqual, getByKey(csdLeave), Lang.UNBILLED_DAYS))
                .addBetweenChildren(
                        add(csdLeaveFrom, getByKey(csdLeave), Lang.UNBILLED_DAYS_FROM),
                        add(csdLeaveTo, getByKey(csdLeave), Lang.UNBILLED_DAYS_TO)
                );
//                .setFormat(new SearchListFormatInteger()
//                        .setMaxLength(10)
//                        .setSignType(SearchListFormatInteger.SIGN_TYPE.POSITIVE))
//                .setSize(40);

        add(csdLos, "T_CASE_DETAILS", "LOS", Lang.LENGTH_OF_STAY) //Verweildauer
                .setFormat(new SearchListFormatInteger()
                        .setMaxLength(10))
                .setSize(60)
                .addEqualChild(add(csdLosEqual, getByKey(csdLos), Lang.LENGTH_OF_STAY))
                .addBetweenChildren(
                        add(csdLosFrom, getByKey(csdLos), Lang.LENGTH_OF_STAY_FROM),
                        add(csdLosTo, getByKey(csdLos), Lang.LENGTH_OF_STAY_TO)
                );
//                .addSingleChild(add(csdLosEqual, "T_CASE_DETAILS", "LOS", Lang.ADMISSION_DATE)
//                                //.setOperator(OPERATOR.GREATER_THAN_OR_EQUAL_TO)
//                )
//                .addBetweenChildren(
//                        add(csdLosFrom, "T_CASE_DETAILS", "LOS", Lang.LENGTH_OF_STAY_FROM)
//                                //.setOperator(OPERATOR.GREATER_THAN_OR_EQUAL_TO)
//                                .setFormat(new SearchListFormatInteger()
//                                        .setMaxLength(10))
//                                .setSize(60),
//                        add(csdLosTo, "T_CASE_DETAILS", "LOS", Lang.LENGTH_OF_STAY_FROM)
//                                .setFormat(new SearchListFormatInteger()
//                                        .setMaxLength(10))
//                                .setSize(60)
//                );

        add(csdLosAlteration, "T_CASE_DETAILS", "LOS_ALTERATION", Lang.LOS_ALTERATION) // ?
                //                .setFormat(new SearchListFormatInteger()
                //                        .setMaxLength(10))
                //                .setSize(60);
                .setFormat(new SearchListFormatInteger()
                        .setMaxLength(10))
                .setSize(60)
                .addEqualChild(add(csdLosAlterationEqual, getByKey(csdLosAlteration), Lang.LOS_ALTERATION))
                .addBetweenChildren(
                        add(csdLosAlterationFrom, getByKey(csdLosAlteration), Lang.LOS_ALTERATION_FROM),
                        add(csdLosAlterationTo, getByKey(csdLosAlteration), Lang.LOS_ALTERATION_TO)
                );

        add(patNumber, "T_PATIENT", "PAT_NUMBER", Lang.PATIENT_NUMBER) //Patientennummer
                .setSize(100);

        add(csdAgeDays, "T_CASE_DETAILS", "AGE_DAYS", Lang.AGE_IN_DAYS) //Alter in Tagen
                //                .setFormat(new SearchListFormatInteger()
                //                        .setMaxLength(10)
                //                        .setSignType(SearchListFormatInteger.SIGN_TYPE.POSITIVE))
                //                .setSize(40);
                .setFormat(new SearchListFormatInteger()
                        .setMaxLength(10)
                        .setSignType(SearchListFormatInteger.SIGN_TYPE.POSITIVE))
                .setSize(40)
                .addEqualChild(add(csdAgeDaysEqual, getByKey(csdAgeDays), Lang.AGE_IN_DAYS))
                .addBetweenChildren(
                        add(csdAgeDaysFrom, getByKey(csdAgeDays), Lang.AGE_IN_DAYS_FROM),
                        add(csdAgeDaysTo, getByKey(csdAgeDays), Lang.AGE_IN_DAYS_TO)
                );

        add(csdAgeYears, "T_CASE_DETAILS", "AGE_YEARS", Lang.AGE_IN_YEARS) //Alter in Jahren
                //                .setFormat(new SearchListFormatInteger()
                //                        .setMaxLength(10)
                //                        .setSignType(SearchListFormatInteger.SIGN_TYPE.POSITIVE))
                //                .setSize(40);
                .setFormat(new SearchListFormatInteger()
                        .setMaxLength(10)
                        .setSignType(SearchListFormatInteger.SIGN_TYPE.POSITIVE))
                .setSize(40)
                .addEqualChild(add(csdAgeYearsEqual, getByKey(csdAgeYears), Lang.AGE_IN_YEARS))
                .addBetweenChildren(
                        add(csdAgeYearsFrom, getByKey(csdAgeYears), Lang.AGE_IN_YEARS_FROM),
                        add(csdAgeYearsTo, getByKey(csdAgeYears), Lang.AGE_IN_YEARS_TO)
                );

        /*
      add(patGenderEn, Lang.GENDER) //Geschlecht
              .setFormat(new SearchListFormatCommon().setDataType(Gender.class))
              .setSize(40)
              .setDisabled(true);
         */
        add(csdAdmissionWeight, "T_CASE_DETAILS", "ADMISSION_WEIGHT", Lang.ADMISSION_WEIGHT) //Aufnahmegewicht
                //                .setFormat(new SearchListFormatInteger()
                //                        .setSignType(SearchListFormatInteger.SIGN_TYPE.POSITIVE))
                //                .setSize(60);
                .setFormat(new SearchListFormatInteger()
                        .setSignType(SearchListFormatInteger.SIGN_TYPE.POSITIVE))
                .setSize(60)
                .addEqualChild(add(csdAdmissionWeightEqual, getByKey(csdAdmissionWeight), Lang.ADMISSION_WEIGHT))
                .addBetweenChildren(
                        add(csdAdmissionWeightFrom, getByKey(csdAdmissionWeight), Lang.ADMISSION_WEIGHT_FROM),
                        add(csdAdmissionWeightTo, getByKey(csdAdmissionWeight), Lang.ADMISSION_WEIGHT_TO)
                );

        add(csdHmv, "T_CASE_DETAILS", "HMV", Lang.ARTIFICIAL_VENTILATION) //Beatmungsstunden
                //                .setFormat(new SearchListFormatInteger()
                //                        .setSignType(SearchListFormatInteger.SIGN_TYPE.POSITIVE))
                //                .setSize(60);
                .setFormat(new SearchListFormatInteger()
                        .setSignType(SearchListFormatInteger.SIGN_TYPE.POSITIVE))
                .setSize(60)
                .addEqualChild(add(csdHmvEqual, getByKey(csdHmv), Lang.ARTIFICIAL_VENTILATION))
                .addBetweenChildren(
                        add(csdHmvFrom, getByKey(csdHmv), Lang.ARTIFICIAL_VENTILATION_FROM),
                        add(csdHmvTo, getByKey(csdHmv), Lang.ARTIFICIAL_VENTILATION_TO)
                );

        add(csdVersionNumber, "T_CASE_DETAILS", "VERSION_NUMBER", Lang.VERSION_NUMBER) //Versionsnummer
                //                .setFormat(new SearchListFormatInteger()
                //                        .setSignType(SearchListFormatInteger.SIGN_TYPE.POSITIVE))
                //                .setSize(60);
                .setFormat(new SearchListFormatInteger()
                        .setSignType(SearchListFormatInteger.SIGN_TYPE.POSITIVE))
                .setSize(60)
                .addEqualChild(add(csdVersionNumberEqual, getByKey(csdVersionNumber), Lang.VERSION_NUMBER))
                .addBetweenChildren(
                        add(csdVersionNumberFrom, getByKey(csdVersionNumber), Lang.VERSION_NUMBER_FROM),
                        add(csdVersionNumberTo, getByKey(csdVersionNumber), Lang.VERSION_NUMBER_TO)
                );

        add(string1, "T_CASE", "STRING_01", Lang.CASE_STRING_1) //String 1 (customer extension)
                .setFormat(new SearchListFormatString())
                .setSize(100);

        add(string2, "T_CASE", "STRING_02", Lang.CASE_STRING_2) //String 2 (customer extension)
                .setFormat(new SearchListFormatString())
                .setSize(100);

        add(string3, "T_CASE", "STRING_03", Lang.CASE_STRING_3) //String 3 (customer extension)
                .setFormat(new SearchListFormatString())
                .setSize(100);

        add(string4, "T_CASE", "STRING_04", Lang.CASE_STRING_4) //String 4 (customer extension)
                .setFormat(new SearchListFormatString())
                .setSize(100);

        add(string5, "T_CASE", "STRING_05", Lang.CASE_STRING_5) //String 5 (customer extension)
                .setFormat(new SearchListFormatString())
                .setSize(100);

        add(string6, "T_CASE", "STRING_06", Lang.CASE_STRING_6) //String 6 (customer extension)
                .setFormat(new SearchListFormatString())
                .setSize(100);

        add(string7, "T_CASE", "STRING_07", Lang.CASE_STRING_7) //String 7 (customer extension)
                .setFormat(new SearchListFormatString())
                .setSize(100);

        add(string8, "T_CASE", "STRING_08", Lang.CASE_STRING_8) //String 8 (customer extension)
                .setFormat(new SearchListFormatString())
                .setSize(100);

        add(string9, "T_CASE", "STRING_09", Lang.CASE_STRING_9) //String 9 (customer extension)
                .setFormat(new SearchListFormatString())
                .setSize(100);

        add(string10, "T_CASE", "STRING_10", Lang.CASE_STRING_10) //String 10 (customer extension)
                .setFormat(new SearchListFormatString())
                .setSize(100);

        add(numeric1, "T_CASE", "NUMERIC_01", Lang.CASE_NUMERIC_1) //Integer 1 (customer extension) 
                .setFormat(new SearchListFormatInteger()
                );
        add(numeric2, "T_CASE", "NUMERIC_02", Lang.CASE_NUMERIC_2) //Integer 2 (customer extension)
                .setFormat(new SearchListFormatInteger());

        add(numeric3, "T_CASE", "NUMERIC_03", Lang.CASE_NUMERIC_3) //Integer 3 (customer extension)
                .setFormat(new SearchListFormatInteger());

        add(numeric4, "T_CASE", "NUMERIC_04", Lang.CASE_NUMERIC_4) //Integer 4 (customer extension)
                .setFormat(new SearchListFormatInteger());

        add(numeric5, "T_CASE", "NUMERIC_05", Lang.CASE_NUMERIC_5) //Integer 5 (customer extension)
                .setFormat(new SearchListFormatInteger());

        add(numeric6, "T_CASE", "NUMERIC_06", Lang.CASE_NUMERIC_6) //Integer 6 (customer extension)
                .setFormat(new SearchListFormatInteger());

        add(numeric7, "T_CASE", "NUMERIC_07", Lang.CASE_NUMERIC_7) //Integer 7 (customer extension)
                .setFormat(new SearchListFormatInteger());

        add(numeric8, "T_CASE", "NUMERIC_08", Lang.CASE_NUMERIC_8) //Integer 8 (customer extension)
                .setFormat(new SearchListFormatInteger());

        add(numeric9, "T_CASE", "NUMERIC_09", Lang.CASE_NUMERIC_9) //Integer 9 (customer extension)
                .setFormat(new SearchListFormatInteger());

        add(numeric10, "T_CASE", "NUMERIC_10", Lang.CASE_NUMERIC_10) //Integer 10 (customer extension)
                .setFormat(new SearchListFormatInteger());
//
//        add(csdDouble1, "T_CASE_DETAILS", "CSD_DOUBLE_1", Lang.CASE_DETAILS_DOUBLE_1) //Double 1 (customer extension)
//                .setFormat(new SearchListFormatCommon().setDataType(Double.class))
//                .setSize(130)
//                .setVisible(false);
//
//        add(csdDouble2, "T_CASE_DETAILS", "CSD_DOUBLE_2", Lang.CASE_DETAILS_DOUBLE_2) //Double 2 (customer extension)
//                .setFormat(new SearchListFormatCommon().setDataType(Double.class))
//                .setSize(130)
//                .setVisible(false);
//
//        add(csdDouble3, "T_CASE_DETAILS", "CSD_DOUBLE_3", Lang.CASE_DETAILS_DOUBLE_3) //Double 3 (customer extension)
//                .setFormat(new SearchListFormatCommon().setDataType(Double.class))
//                .setSize(130)
//                .setVisible(false);
//
//        add(csdDouble4, "T_CASE_DETAILS", "CSD_DOUBLE_4", Lang.CASE_DETAILS_DOUBLE_4) //Double 4 (customer extension)
//                .setFormat(new SearchListFormatCommon().setDataType(Double.class))
//                .setSize(130)
//                .setVisible(false);
//
//        add(csdDouble5, "T_CASE_DETAILS", "CSD_DOUBLE_5", Lang.CASE_DETAILS_DOUBLE_5) //Double 5 (customer extension)
//                .setFormat(new SearchListFormatCommon().setDataType(Double.class))
//                .setSize(130)
//                .setVisible(false);
//
//        add(csdDate1, "T_CASE_DETAILS", "CSD_DATE_1", Lang.CASE_DETAILS_DATE_1) //Date 1 (customer extension)
//                .setFormat(new SearchListFormatDateTime())
//                .setSize(130)
//                .setVisible(false);
//
//        add(csdDate2, "T_CASE_DETAILS", "CSD_DATE_2", Lang.CASE_DETAILS_DATE_2) //Date 2 (customer extension)
//                .setFormat(new SearchListFormatDateTime())
//                .setSize(130)
//                .setVisible(false);
//
//        add(csdDate3, "T_CASE_DETAILS", "CSD_DATE_3", Lang.CASE_DETAILS_DATE_3) //Date 3 (customer extension)
//                .setFormat(new SearchListFormatDateTime())
//                .setSize(130)
//                .setVisible(false);
//
//        add(csdDate4, "T_CASE_DETAILS", "CSD_DATE_4", Lang.CASE_DETAILS_DATE_4) //Date 4 (customer extension)
//                .setFormat(new SearchListFormatDateTime())
//                .setSize(130)
//                .setVisible(false);
//
//        add(csdDate5, "T_CASE_DETAILS", "CSD_DATE_5", Lang.CASE_DETAILS_DATE_5) //Date 5 (customer extension)
//                .setFormat(new SearchListFormatDateTime())
//                .setSize(130)
//                .setVisible(false);
        add(cwPositive, "VIEW_CW_POS", "CW_POSITIVE", Lang.CASE_CW_POSITIVE) //max cw positive
                .setFormat(new SearchListFormatDouble()
                        .setSignType(SearchListFormatInteger.SIGN_TYPE.POSITIVE))
                .setVisible(false)
                .setSize(130);

        add(cwNegative, "VIEW_CW_NEG", "CW_NEGATIVE", Lang.CASE_CW_NEGATIVE) //max cw negative
                .setFormat(new SearchListFormatDouble()
                        .setSignType(SearchListFormatInteger.SIGN_TYPE.NEGATIVE))
                .setVisible(false)
                .setSize(130);

        add(cwEffective, "VIEW_GRPRES", "DRGC_CW_EFFECTIV", Lang.CASE_CW_EFFECTIVE)//effektiver cw 
                .setFormat(new SearchListFormatDouble())//new SearchListFormatCommon().setDataType(String.class))
                .setSize(130)
                //                .addEqualChild(add(cwEffectiveEqual, getByKey(cwEffective), Lang.CASE_CW_EFFECTIVE))
                .addBetweenChildren(
                        add(cwEffectiveFrom, getByKey(cwEffective), Lang.CASE_CW_EFFECTIVE_FROM),
                        add(cwEffectiveTo, getByKey(cwEffective), Lang.CASE_CW_EFFECTIVE_TO)
                );
//                .setSize(130);

        add(cwCatalog, "C_DRG_CATALOG", "DRG_MD_CW", Lang.CASE_CW_CATALOG)// cw aus drg katalog
                .setFormat(new SearchListFormatDouble()
                        .setSignType(SearchListFormatInteger.SIGN_TYPE.POSITIVE))
                .setSize(130)
                .setVisible(false)
                .setNoFilter(true);

        add(csDrg, "VIEW_GRPRES", "GRPRES_CODE", "DRG") // drg
                .setFormat(new SearchListFormatString())
                .setSize(130);
        /*
      add(patdZipcode, Lang.ZIP_CODE) //Postleitzahl (des Patienten)
              .setSize(60)
              .setDisabled(true);
         */

 /*
      add(pccl, Lang.PCCL) //Patient Clinical Complexity Level, patientenbezogener Gesamtschweregrad
              .setSize(40)
              .setDisabled(true);
         */
 /*
      add(countSD, Lang.COUNT_SD) //Anzahl der Nebendiagnosen
               .setFormat(new SearchListFormatInteger()
              .setSize(60)
              .setDisabled(true);
         */
 /*
      add(countProc, Lang.COUNT_PROC) //Anzahl der Prozeduren
               .setFormat(new SearchListFormatInteger()
              .setSize(60)
              .setDisabled(true);
         */
        add(dCWmin, "VIEW_CW", "MIN_DCW_NEGATIVE", Lang.CASE_DELTA_CWMINIMAL) //Delta-CW minimal
                .setFormat(new SearchListFormatDouble())
                .setSize(60)
                //                .addEqualChild(add(dCWminEqual, getByKey(dCWmin), Lang.CASE_DELTA_CWMINIMAL))
                .addBetweenChildren(
                        add(dCWminFrom, getByKey(dCWmin), Lang.CASE_DELTA_CWMINIMAL_FROM),
                        add(dCWminTo, getByKey(dCWmin), Lang.CASE_DELTA_CWMINIMAL_TO)
                );

        add(dCWmax, "VIEW_CW", "MAX_DCW_POSITIVE", Lang.CASE_DELTA_CWMAXIMAL) //Delta-CW maximal
                .setFormat(new SearchListFormatDouble())
                .setSize(60)
                //                .addEqualChild(add(dCWmaxEqual, getByKey(dCWmax), Lang.CASE_DELTA_CWMAXIMAL))
                .addBetweenChildren(
                        add(dCWmaxFrom, getByKey(dCWmax), Lang.CASE_DELTA_CWMAXIMAL_FROM),
                        add(dCWmaxTo, getByKey(dCWmax), Lang.CASE_DELTA_CWMAXIMAL_TO)
                );
        
        //pflege +/-
        add(dCareCWmin, "VIEW_CW", "MIN_DCW_CARE_NEGATIVE", Lang.CASE_CARE_CWMINIMAL) //Delta-Pflege minimal
                .setFormat(new SearchListFormatDouble())
                .setSize(60).setIs4DecimalDigits(true)
//                .addEqualChild(add(dCWminEqual, getByKey(dCWmin), Lang.CASE_DELTA_CWMINIMAL))
                .addBetweenChildren(
                        add(dCareCWminFrom, getByKey(dCareCWmin), Lang.CASE_CARE_CWMINIMAL_FROM),
                        add(dCareCWminTo, getByKey(dCareCWmin), Lang.CASE_CARE_CWMINIMAL_TO)
                );

        add(dCareCWmax, "VIEW_CW", "MAX_DCW_CARE_POSITIVE", Lang.CASE_CARE_CWMAXIMAL) //Delta-Pflege maximal
                .setFormat(new SearchListFormatDouble())
                .setSize(60).setIs4DecimalDigits(true)
//                .addEqualChild(add(dCWmaxEqual, getByKey(dCWmax), Lang.CASE_DELTA_CWMAXIMAL))
                .addBetweenChildren(
                        add(dCareCWmaxFrom, getByKey(dCareCWmax), Lang.CASE_CARE_CWMAXIMAL_FROM),
                        add(dCareCWmaxTo, getByKey(dCareCWmax), Lang.CASE_CARE_CWMAXIMAL_TO)
                );
        
        //Erlös +/-
        add(dRevenueMin, "VIEW_CW", "MIN_DFEE_NEGATIVE", Lang.CASE_DELTA_REVENUE_MINIMAL) //Erlös minimal
                .setFormat(new SearchListFormatDouble())
                .setSize(60)
//                .addEqualChild(add(dCWminEqual, getByKey(dCWmin), Lang.CASE_DELTA_CWMINIMAL))
                .addBetweenChildren(
                        add(dRevenueMinFrom, getByKey(dRevenueMin), Lang.CASE_DELTA_REVENUE_MINIMAL_FROM),
                        add(dRevenueMinTo, getByKey(dRevenueMin), Lang.CASE_DELTA_REVENUE_MINIMAL_TO)
                );

        add(dRevenueMax, "VIEW_CW", "MAX_DFEE_POSITIVE", Lang.CASE_DELTA_REVENUE_MAXIMAL) //Erlösmaximal
                .setFormat(new SearchListFormatDouble())
                .setSize(60)
//                .addEqualChild(add(dCWmaxEqual, getByKey(dCWmax), Lang.CASE_DELTA_CWMAXIMAL))
                .addBetweenChildren(
                        add(dRevenueMaxFrom, getByKey(dRevenueMax), Lang.CASE_DELTA_REVENUE_MAXIMAL_FROM),
                        add(dRevenueMaxTo, getByKey(dRevenueMax), Lang.CASE_DELTA_REVENUE_MAXIMAL_TO)
                );
        //PflegeCW
        add(careCw, "VIEW_GRPRES", "DRGC_CARE_CW", Lang.CASE_CARE_CW)//effektiver cw 
                .setFormat(new SearchListFormatDouble())//new SearchListFormatCommon().setDataType(String.class))
                .setSize(130).setIs4DecimalDigits(true)
                //                .addEqualChild(add(cwEffectiveEqual, getByKey(cwEffective), Lang.CASE_CW_EFFECTIVE))
                .addBetweenChildren(add(careCwFrom, getByKey(careCw), Lang.CASE_CARE_CW_FROM),
                        add(careCwTo, getByKey(careCw), Lang.CASE_CARE_CW_TO)
                );
    

    // billing date
         add(csBillingDate, "T_CASE", "CS_BILLING_DATE", Lang.BILLING_DATE)//effektiver cw 
                .setFormat(new SearchListFormatDateTime())
                .setSize(130)
                .addEqualChild(add(csBillingDateEqual, getByKey(csBillingDate), Lang.BILLING_DATE))
                .addBetweenChildren(
                        add(csBillingDateFrom, getByKey(csBillingDate), Lang.BILLING_DATE_FROM),
                        add(csBillingDateTo, getByKey(csBillingDate), Lang.BILLING_DATE_TO)
                );
    
    
            add(pccl, "VIEW_GRPRES", "GRPRES_PCCL", Lang.RULES_TXT_PCCL_DIS)
                //                .setFormat(new SearchListFormatInteger()
                //                        .setSignType(SearchListFormatInteger.SIGN_TYPE.POSITIVE))
                //                .setSize(40);
                .setFormat(new SearchListFormatInteger()
                        .setSignType(SearchListFormatInteger.SIGN_TYPE.POSITIVE))
                .setSize(40)
                .addEqualChild(add(pcclEqual, getByKey(pccl), "PCCL"))
                .addBetweenChildren(
                        add(pcclFrom, getByKey(pccl),"Von  PCCL"),
                        add(pcclTo, getByKey(pccl), "Bis PCCL")
                );
    }
   
}
