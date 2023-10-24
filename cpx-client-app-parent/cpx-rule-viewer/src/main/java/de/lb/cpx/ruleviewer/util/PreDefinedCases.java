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
package de.lb.cpx.ruleviewer.util;

import de.lb.cpx.client.core.util.IcdHelper;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseFee;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TCaseWard;
import de.lb.cpx.model.TInsurance;
import de.lb.cpx.model.TLab;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.TPatientDetails;
import de.lb.cpx.model.enums.AdmissionCauseEn;
import de.lb.cpx.model.enums.AdmissionModeEn;
import de.lb.cpx.model.enums.AdmissionReasonEn;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.DischargeReasonEn;
import de.lb.cpx.model.enums.GenderEn;
import de.lb.cpx.model.enums.IcdcRefTypeEn;
import de.lb.cpx.model.enums.InsStatusEn;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author wilde
 */
public class PreDefinedCases {

    public static final String EXAMPLE_CASE_ONE_NUMBER = "123";
    public static final String EXAMPLE_CASE_TWO_NUMBER = "456";
    public static final String EXAMPLE_CASE_THREE_NUMBER = "789";
    private static PreDefinedCases INSTANCE;
//    private final List<TCase> predefinedCases;

    private PreDefinedCases() {
//        this.predefinedCases = new ArrayList<>();
//        predefinedCases.add(firstTestCase("123"));
//        predefinedCases.add(secondTestCase("456"));
//        predefinedCases.add(thirdTestCase("789"));
    }

    public List<TCase> getPreDefinedCases() {
        ArrayList<TCase> predefinedCases = new ArrayList<>();
        predefinedCases.add(firstTestCase(EXAMPLE_CASE_ONE_NUMBER));
        predefinedCases.add(secondTestCase(EXAMPLE_CASE_TWO_NUMBER));
        predefinedCases.add(thirdTestCase(EXAMPLE_CASE_THREE_NUMBER));
//
//        predefinedCases.add(firstTestCase(EXAMPLE_CASE_ONE_NUMBER + "1"));
//        predefinedCases.add(secondTestCase(EXAMPLE_CASE_TWO_NUMBER + "1"));
//        predefinedCases.add(thirdTestCase(EXAMPLE_CASE_THREE_NUMBER + "1"));
//
//        predefinedCases.add(firstTestCase(EXAMPLE_CASE_ONE_NUMBER + "2"));
//        predefinedCases.add(secondTestCase(EXAMPLE_CASE_TWO_NUMBER+ "2"));
//        predefinedCases.add(thirdTestCase(EXAMPLE_CASE_THREE_NUMBER+ "2"));
//
//        predefinedCases.add(firstTestCase(EXAMPLE_CASE_ONE_NUMBER+ "3"));
//        predefinedCases.add(secondTestCase(EXAMPLE_CASE_TWO_NUMBER+ "3"));
//        predefinedCases.add(thirdTestCase(EXAMPLE_CASE_THREE_NUMBER+ "3"));
//
//        predefinedCases.add(firstTestCase(EXAMPLE_CASE_ONE_NUMBER+ "5"));
//        predefinedCases.add(secondTestCase(EXAMPLE_CASE_TWO_NUMBER+ "5"));
//        predefinedCases.add(thirdTestCase(EXAMPLE_CASE_THREE_NUMBER+ "5"));
//
//        predefinedCases.add(firstTestCase(EXAMPLE_CASE_ONE_NUMBER+ "6"));
//        predefinedCases.add(secondTestCase(EXAMPLE_CASE_TWO_NUMBER+ "6"));
//        predefinedCases.add(thirdTestCase(EXAMPLE_CASE_THREE_NUMBER+ "6"));
//
//        predefinedCases.add(firstTestCase(EXAMPLE_CASE_ONE_NUMBER+ "7"));
//        predefinedCases.add(secondTestCase(EXAMPLE_CASE_TWO_NUMBER+ "7"));
//        predefinedCases.add(thirdTestCase(EXAMPLE_CASE_THREE_NUMBER+ "7"));
//
//        predefinedCases.add(firstTestCase(EXAMPLE_CASE_ONE_NUMBER+ "8"));
//        predefinedCases.add(secondTestCase(EXAMPLE_CASE_TWO_NUMBER+ "8"));
//        predefinedCases.add(thirdTestCase(EXAMPLE_CASE_THREE_NUMBER+ "8"));
//
//        predefinedCases.add(firstTestCase(EXAMPLE_CASE_ONE_NUMBER+ "9"));
//        predefinedCases.add(secondTestCase(EXAMPLE_CASE_TWO_NUMBER+ "9"));
//        predefinedCases.add(thirdTestCase(EXAMPLE_CASE_THREE_NUMBER+ "9"));
//
//        predefinedCases.add(firstTestCase(EXAMPLE_CASE_ONE_NUMBER+ "10"));
//        predefinedCases.add(secondTestCase(EXAMPLE_CASE_TWO_NUMBER+ "10"));
//        predefinedCases.add(thirdTestCase(EXAMPLE_CASE_THREE_NUMBER+ "10"));
//
//        predefinedCases.add(firstTestCase(EXAMPLE_CASE_ONE_NUMBER+ "11"));
//        predefinedCases.add(secondTestCase(EXAMPLE_CASE_TWO_NUMBER+ "11"));
//        predefinedCases.add(thirdTestCase(EXAMPLE_CASE_THREE_NUMBER+ "11"));
//
//        predefinedCases.add(firstTestCase(EXAMPLE_CASE_ONE_NUMBER+ "12"));
//        predefinedCases.add(secondTestCase(EXAMPLE_CASE_TWO_NUMBER+ "12"));
//        predefinedCases.add(thirdTestCase(EXAMPLE_CASE_THREE_NUMBER+ "12"));
//
//        predefinedCases.add(firstTestCase(EXAMPLE_CASE_ONE_NUMBER+ "13"));
//        predefinedCases.add(secondTestCase(EXAMPLE_CASE_TWO_NUMBER+ "13"));
//        predefinedCases.add(thirdTestCase(EXAMPLE_CASE_THREE_NUMBER+ "13"));
//
//        predefinedCases.add(firstTestCase(EXAMPLE_CASE_ONE_NUMBER+ "14"));
//        predefinedCases.add(secondTestCase(EXAMPLE_CASE_TWO_NUMBER+ "14"));
//        predefinedCases.add(thirdTestCase(EXAMPLE_CASE_THREE_NUMBER+ "14"));
//
//        predefinedCases.add(firstTestCase(EXAMPLE_CASE_ONE_NUMBER+ "15"));
//        predefinedCases.add(secondTestCase(EXAMPLE_CASE_TWO_NUMBER+ "15"));
//        predefinedCases.add(thirdTestCase(EXAMPLE_CASE_THREE_NUMBER+ "15"));
        return predefinedCases;
    }

    public TCase getTestCase(String pNumber) {
        switch (pNumber) {
            case EXAMPLE_CASE_ONE_NUMBER:
                return getFirstTestCase();
            case EXAMPLE_CASE_TWO_NUMBER:
                return getSecondTestCase();
            case EXAMPLE_CASE_THREE_NUMBER:
                return getThirdTestCase();
            default:
                return null;
        }
    }

    public static PreDefinedCases instance() {
        if (INSTANCE == null) {
            INSTANCE = new PreDefinedCases();
        }
        return INSTANCE;
    }

    public TCase getFirstTestCase() {
        return firstTestCase(EXAMPLE_CASE_ONE_NUMBER);
    }

    public TCase getSecondTestCase() {
        return firstTestCase(EXAMPLE_CASE_TWO_NUMBER);
    }

    public TCase getThirdTestCase() {
        return firstTestCase(EXAMPLE_CASE_THREE_NUMBER);
    }

    private TCase firstTestCase(String pCaseNumber) {

        TCase cse = new TCase();
        cse.setCsCaseNumber(pCaseNumber);
        cse.setCsCaseTypeEn(CaseTypeEn.DRG);
        cse.setCsHospitalIdent("999999999");
        cse.setCsBillingDate(new GregorianCalendar(2018, Calendar.FEBRUARY, 14).getTime());
//        cse.setBaserate(3530.50);
        Date dateAdm = new GregorianCalendar(2018, Calendar.FEBRUARY, 11).getTime();
        Date dateDis = new GregorianCalendar(2018, Calendar.FEBRUARY, 14).getTime();

        TPatient patient = new TPatient();
        patient.setPatGenderEn(GenderEn.M);
        patient.setPatDateOfBirth(new GregorianCalendar(1968, Calendar.SEPTEMBER, 21).getTime());
        cse.setPatient(patient);

        TPatientDetails patDetails = new TPatientDetails();
        patDetails.setPatdIsActualFl(true);
        patDetails.setPatdCity("Musterstadt");
        patDetails.setPatdZipcode("11111");
        patient.getPatientDetailList().add(patDetails);

        TInsurance ins = new TInsurance();
        ins.setInsIsActualFl(true);
        ins.setInsStatusEn(InsStatusEn.self);
        patient.getInsurances().add(ins);

        TCaseDetails details = new TCaseDetails();
        cse.getCaseDetails().add(details);
        details.setCsdAdmissionDate(dateAdm);
        details.setCsdDischargeDate(dateDis);
        details.setCsdIsLocalFl(true);
        details.setCsdIsActualFl(true);
        details.setCsdAdmCauseEn(AdmissionCauseEn.E);
        details.setCsdAdmReason12En(AdmissionReasonEn.ar02);
        details.setCsdAgeYears(50);
        details.setCsdDisReason12En(DischargeReasonEn.dr01);
//        details.setcsd(GenderEn.m);
        details.setCsdHmv(0);
        details.setCsdLos(3L);
        details.setCsdAdmissionWeight(60);

        TCaseDepartment department = new TCaseDepartment();
        department.setDepKey301("1500");
        department.setDepcAdmodEn(AdmissionModeEn.HA);
        department.setDepcAdmDate(dateAdm);
        department.setDepcDisDate(dateDis);
        details.getCaseDepartments().add(department);

        TCaseIcd icd1 = new TCaseIcd();
        icd1.setIcdcIsHdxFl(true);
        icd1.setIcdcCode("M75.1");
        TCaseIcd icd2 = new TCaseIcd();
        icd2.setIcdcCode("I10.00");
        TCaseIcd icd3 = new TCaseIcd();
        icd3.setIcdcCode("M65.81");
        TCaseIcd icd4 = new TCaseIcd();
        icd4.setIcdcCode("M75.5");
        TCaseIcd icd5 = new TCaseIcd();
        icd5.setIcdcCode("R52.0");
        TCaseIcd icd6 = new TCaseIcd();
        icd6.setIcdcCode("Z48.0");
        TCaseIcd icd7 = new TCaseIcd();
        icd7.setIcdcCode("Z74.1");
        department.getCaseIcds().addAll(Arrays.asList(icd1, icd2, icd3, icd4, icd5, icd6, icd7));

        TCaseOps ops1 = new TCaseOps();
        ops1.setOpscCode("5-805.7");
        TCaseOps ops2 = new TCaseOps();
        ops2.setOpscCode("5-811.20");
        TCaseOps ops3 = new TCaseOps();
        ops3.setOpscCode("5-814.3");
        TCaseOps ops4 = new TCaseOps();
        ops4.setOpscCode("5-855.71");
        TCaseOps ops5 = new TCaseOps();
        ops5.setOpscCode("8-910");
//        Ops ops6 = new Ops("2-5");
//        Ops ops7 = new Ops("1-8");
//        Ops ops8 = new Ops("1-10");
//        Ops ops9 = new Ops("2-20");
//        Ops ops10 = new Ops("5-9");
//        Ops ops11 = new Ops("1-2");
//        Ops ops12 = new Ops("2-10");
        department.getCaseOpses().addAll(Arrays.asList(ops1, ops2, ops3, ops4,
                ops5)
        //                ,ops6,ops7
        //                ,ops8,ops9,ops10,ops11
        //                ,ops12
        );
        details.setComment(getTestCaseComment(cse));
        //create case fees
        Set<TCaseFee> fees = new HashSet<>();
        TCaseFee fee1 = new TCaseFee();
        fee1.setFeecFeekey("7010L63F");
        fee1.setFeecValue(1515.58);
        fee1.setFeecCount(1);
        TCaseFee fee2 = new TCaseFee();
        fee2.setFeecFeekey("47200002");
        fee2.setFeecValue(0);
        fee2.setFeecCount(1);
        TCaseFee fee3 = new TCaseFee();
        fee3.setFeecFeekey("75101002");
        fee3.setFeecValue(70);
        fee3.setFeecCount(1);
        TCaseFee fee4 = new TCaseFee();
        fee4.setFeecFeekey("48000001");
        fee4.setFeecValue(1515.58);
        fee4.setFeecCount(1);
        TCaseFee fee5 = new TCaseFee();
        fee5.setFeecFeekey("47100006");
        fee5.setFeecValue(25);
        fee5.setFeecCount(1);
        fees.addAll(Arrays.asList(fee1, fee2, fee3, fee4, fee5));
        details.setCaseFees(fees);
        return cse;
    }

    private TCase secondTestCase(String pCaseNumber) {
        TCase cse = new TCase();
        cse.setCsHospitalIdent("999999999");
        cse.setCsCaseNumber(pCaseNumber);

        TPatient patient = new TPatient();
        patient.setPatGenderEn(GenderEn.W);
        patient.setPatDateOfBirth(new GregorianCalendar(1948, Calendar.FEBRUARY, 5).getTime());
        cse.setPatient(patient);
//        cse.setBaserate(3012.04);

        TPatientDetails patDetails = new TPatientDetails();
        patDetails.setPatdIsActualFl(true);
        patDetails.setPatdCity("Stadt2");
        patDetails.setPatdZipcode("19191");
        patient.getPatientDetailList().add(patDetails);

        TInsurance ins = new TInsurance();
        ins.setInsIsActualFl(true);
        ins.setInsStatusEn(InsStatusEn.family);
        patient.getInsurances().add(ins);

        TLab lab1 = new TLab();
        lab1.setLabText("Lab1");
        lab1.setLabDescription("test1");

        TLab lab2 = new TLab();
        lab2.setLabText("Lab2");
        lab2.setLabDescription("test1");

        cse.getCaseLabor().add(lab1);
        cse.getCaseLabor().add(lab2);

        Date dateAdm = new GregorianCalendar(2019, Calendar.APRIL, 7).getTime();
        Date dateDis = new GregorianCalendar(2019, Calendar.APRIL, 19).getTime();

        TCaseDetails details = new TCaseDetails();
        cse.getCaseDetails().add(details);
        details.setCsdAdmissionDate(dateAdm);
        details.setCsdDischargeDate(dateDis);
        details.setCsdIsLocalFl(true);
        details.setCsdIsActualFl(true);
        details.setCsdAdmCauseEn(AdmissionCauseEn.N);
        details.setCsdAdmReason12En(AdmissionReasonEn.ar07);
        details.setCsdAgeYears(70);
        details.setCsdDisReason12En(DischargeReasonEn.dr01);
        details.setCsdHmv(0);
        details.setCsdAdmissionWeight(55);
//        details.setCsdLos(12L);

        TCaseDepartment department = new TCaseDepartment();
        department.setDepKey301("1500");
        department.setDepcAdmodEn(AdmissionModeEn.HA);
        department.setDepcAdmDate(dateAdm);
        department.setDepcDisDate(new GregorianCalendar(2019, Calendar.APRIL, 9).getTime());
        details.getCaseDepartments().add(department);

        TCaseIcd icd1 = new TCaseIcd();
        icd1.setIcdcIsHdxFl(true);
        icd1.setIcdcCode("J44.09");
        TCaseIcd icd2 = new TCaseIcd();
        icd2.setIcdcCode("B95.6");
        TCaseIcd icd3 = new TCaseIcd();
        icd3.setIcdcCode("E89.0");
        TCaseIcd icd4 = new TCaseIcd();
        icd4.setIcdcCode("I10.00");
        TCaseIcd icd5 = new TCaseIcd();
        icd5.setIcdcCode("J96.00");
        TCaseIcd icd6 = new TCaseIcd();
        icd6.setIcdcCode("M81.40");
        TCaseIcd icd7 = new TCaseIcd();
        icd7.setIcdcCode("N39.0");
        department.getCaseIcds().addAll(Arrays.asList(icd1, icd2, icd3, icd4, icd5, icd6, icd7));
        TCaseOps ops1 = new TCaseOps();
        ops1.setOpscCode("1-710");

        department.getCaseOpses().addAll(Arrays.asList(ops1));

        TCaseDepartment department2 = new TCaseDepartment();
        department2.setDepKey301("1000");
        department2.setDepcAdmodEn(AdmissionModeEn.HA);
        department2.setDepcAdmDate(new GregorianCalendar(2019, Calendar.APRIL, 9).getTime());
        department2.setDepcDisDate(dateDis);
        details.getCaseDepartments().add(department2);

        TCaseWard ward1 = new TCaseWard();
        ward1.setWardcIdent("A01");
        department2.getCaseWards().add(ward1);
        TCaseWard ward2 = new TCaseWard();
        ward2.setWardcIdent("B02");
        department2.getCaseWards().add(ward2);

        TCaseIcd icd21 = new TCaseIcd();
        icd21.setIcdcCode("A44.09");
        TCaseIcd icd22 = new TCaseIcd();
        icd22.setIcdcCode("B95.6");
        TCaseIcd icd23 = new TCaseIcd();
        icd23.setIcdcCode("G89.0");
        TCaseIcd icd24 = new TCaseIcd();
        icd24.setIcdcCode("H10.00");
        TCaseIcd icd25 = new TCaseIcd();
        icd25.setIcdcCode("O96.00");
//        TCaseIcd icd26 = new TCaseIcd ();
//        icd6.setIcdcCode("M81.40");
//        TCaseIcd icd27 = new TCaseIcd ();
//        icd7.setIcdcCode("N39.0");
        ward1.getCaseIcds().addAll(Arrays.asList(icd21, icd22));
        ward2.getCaseIcds().addAll(Arrays.asList(icd23, icd24, icd25));

        TCaseOps ops11 = new TCaseOps();
        ops11.setOpscCode("6-710");
        TCaseOps ops21 = new TCaseOps();
        ops21.setOpscCode("9-810");

        ward1.getCaseOpses().addAll(Arrays.asList(ops11));
        ward2.getCaseOpses().addAll(Arrays.asList(ops21));
        department2.getCaseIcds().addAll(Arrays.asList(icd21, icd22, icd23, icd24, icd25));
        department2.getCaseOpses().addAll(Arrays.asList(ops11, ops21));
//       Set<TCaseFee> fees = new HashSet<>();
//        TCaseFee fee1 = new TCaseFee();
//        fee1.setFeecFeekey("7010L63F");
//        fee1.setFeecValue(1515.58);
//        fee1.setFeecCount(1);
//        TCaseFee fee2 = new TCaseFee();
//        fee2.setFeecFeekey("47200002");
//        fee2.setFeecValue(0);
//        fee2.setFeecCount(1);
//        TCaseFee fee3 = new TCaseFee();
//        fee3.setFeecFeekey("75101002");
//        fee3.setFeecValue(70);
//        fee3.setFeecCount(1);
//        TCaseFee fee4 = new TCaseFee();
//        fee4.setFeecFeekey("48000001");
//        fee4.setFeecValue(1515.58);
//        fee4.setFeecCount(1);
//        TCaseFee fee5 = new TCaseFee();
//        fee5.setFeecFeekey("47100006");
//        fee5.setFeecValue(25);
//        fee5.setFeecCount(1);
//        fees.addAll(Arrays.asList(fee1, fee2, fee3, fee4, fee5));
//        details.setCaseFees(fees);
        return cse;
    }

    private TCase thirdTestCase(String pCaseNumber) {
        TCase cse = new TCase();
        cse.setCsHospitalIdent("999999999");
        cse.setCsCaseNumber(pCaseNumber);
//        cse.setBaserate(3012.04);
        TPatient patient = new TPatient();
        patient.setPatGenderEn(GenderEn.M);
        patient.setPatDateOfBirth(new GregorianCalendar(1980, Calendar.OCTOBER, 25).getTime());
        cse.setPatient(patient);

        TPatientDetails patDetails = new TPatientDetails();
        patDetails.setPatdIsActualFl(true);
        patDetails.setPatdCity("Musterstadt");
        patDetails.setPatdZipcode("11111");
        patient.getPatientDetailList().add(patDetails);

        TInsurance ins = new TInsurance();
        ins.setInsIsActualFl(true);
        ins.setInsStatusEn(InsStatusEn.senior);
        patient.getInsurances().add(ins);

        Date dateAdm = new GregorianCalendar(2019, Calendar.SEPTEMBER, 15).getTime();
        Date dateDis = new GregorianCalendar(2019, Calendar.SEPTEMBER, 17).getTime();

        TCaseDetails details = new TCaseDetails();
        cse.getCaseDetails().add(details);
        details.setCsdAdmissionDate(dateAdm);
        details.setCsdDischargeDate(dateDis);
        details.setCsdIsLocalFl(true);
        details.setCsdIsActualFl(true);
        details.setCsdAdmCauseEn(AdmissionCauseEn.N);
        details.setCsdAdmReason12En(AdmissionReasonEn.ar07);
        details.setCsdAgeYears(39);
        details.setCsdDisReason12En(DischargeReasonEn.dr01);
        details.setCsdHmv(0);
        details.setCsdLos(2L);
        details.setCsdAdmissionWeight(90);

        TCaseDepartment department = new TCaseDepartment();
        department.setDepKey301("1500");
        department.setDepcAdmodEn(AdmissionModeEn.HA);
        department.setDepcAdmDate(dateAdm);
        department.setDepcDisDate(dateDis);
        details.getCaseDepartments().add(department);

        TCaseIcd icd1 = new TCaseIcd();
        icd1.setIcdcCode("T42.4");
        TCaseIcd icd2 = new TCaseIcd();
        icd2.setIcdcCode("J96.00");
        TCaseIcd icd3 = new TCaseIcd();
        icd3.setIcdcCode("R40.2");
        TCaseIcd icd4 = new TCaseIcd();
        icd4.setIcdcCode("T40.7");
        TCaseIcd icd5 = new TCaseIcd();
        icd5.setIcdcCode("T43.0");
        TCaseIcd icd6 = new TCaseIcd();
        icd6.setIcdcCode("T43.6");
        TCaseIcd icd7 = new TCaseIcd();
        icd7.setIcdcCode("T68");

//        icd1.setIcdcReftypeEn(IcdcRefTypeEn.Stern);
//        icd1.setRefIcd(icd2);
//        icd2.setIcdcReftypeEn(IcdcRefTypeEn.Kreuz);
//        icd2.setRefIcd(icd1);
        IcdHelper.setSecIcdReference(IcdcRefTypeEn.Kreuz, icd1, icd2);

        department.getCaseIcds().addAll(Arrays.asList(icd1, icd2, icd3, icd4, icd5, icd6, icd7));

        TCaseOps ops1 = new TCaseOps();
        ops1.setOpscCode("3-200");
        TCaseOps ops2 = new TCaseOps();
        ops2.setOpscCode("8-701");
        TCaseOps ops3 = new TCaseOps();
        ops3.setOpscCode("8-980.0");
        department.getCaseOpses().addAll(Arrays.asList(ops1, ops2, ops3));
//       Set<TCaseFee> fees = new HashSet<>();
//        TCaseFee fee1 = new TCaseFee();
//        fee1.setFeecFeekey("7010L63F");
//        fee1.setFeecValue(1515.58);
//        fee1.setFeecCount(1);
//        TCaseFee fee2 = new TCaseFee();
//        fee2.setFeecFeekey("47200002");
//        fee2.setFeecValue(0);
//        fee2.setFeecCount(1);
//        TCaseFee fee3 = new TCaseFee();
//        fee3.setFeecFeekey("75101002");
//        fee3.setFeecValue(70);
//        fee3.setFeecCount(1);
//        TCaseFee fee4 = new TCaseFee();
//        fee4.setFeecFeekey("48000001");
//        fee4.setFeecValue(1515.58);
//        fee4.setFeecCount(1);
//        TCaseFee fee5 = new TCaseFee();
//        fee5.setFeecFeekey("47100006");
//        fee5.setFeecValue(25);
//        fee5.setFeecCount(1);
//        fees.addAll(Arrays.asList(fee1, fee2, fee3, fee4, fee5));
//        details.setCaseFees(fees);
        return cse;
    }

    private String getTestCaseComment(TCase cse) {
        Objects.requireNonNull(cse);
        Objects.requireNonNull(cse.getPatient());
        Objects.requireNonNull(cse.getCurrentLocal());
        Objects.requireNonNull(cse.getCurrentLocal().getLastDepartment());

        return "Fallnummer: " + cse.getCsCaseNumber() + " Geschlecht: " + cse.getPatient().getPatGenderEn().name() + "\n"
                + "Aufnahmedatum: " + Lang.toDate(cse.getCurrentLocal().getCsdAdmissionDate()) + " Entlassungsdatum: " + Lang.toDate(cse.getCurrentLocal().getCsdDischargeDate()) + "\n"
                + "Aufnahmeanlass: " + cse.getCurrentLocal().getCsdAdmodEn().name() + " AlterInJahren: " + cse.getCurrentLocal().getCsdAgeYears() + "\n"
                + "Aufnahmegrund12: " + cse.getCurrentLocal().getCsdAdmReason12En().name() + " Entlassungsgrund01: " + cse.getCurrentLocal().getCsdDisReason12En().name() + "\n"
                + "Beatmungsstunden: " + cse.getCurrentLocal().getCsdHmv() + " Vwd: " + cse.getCurrentLocal().getCsdLos() + "\n"
                + "Fachabteilung: " + cse.getCurrentLocal().getLastDepartment().getDepShortName() + " Fab-Aufnhamegrund: " + cse.getCurrentLocal().getLastDepartment().getDepcAdmodEn().name() + "\n"
                + "Fab-Aufnahmedatum: " + Lang.toDate(cse.getCurrentLocal().getLastDepartment().getDepcAdmDate()) + " Geschlecht: " + Lang.toDate(cse.getCurrentLocal().getLastDepartment().getDepcDisDate()) + "\n"
                + "Prozeduren: " + cse.getCurrentLocal().getLastDepartment().getCaseOpses().stream().map((t) -> {
                    return t.getOpscCode();
                }).collect(Collectors.joining(",")) + "\n"
                + "Prozeduren: " + cse.getCurrentLocal().getLastDepartment().getCaseIcds().stream().map((t) -> {
                    return t.getIcdcCode();
                }).collect(Collectors.joining(","));
        //                        "Geschlecht: "+getPatient().getPatGenderEn().name()+" Geschlecht: "+getPatient().getPatGenderEn().name()+"\n"+
        //                        "Geschlecht: "+getPatient().getPatGenderEn().name()+" Geschlecht: "+getPatient().getPatGenderEn().name()+"\n"+
        //                        "Geschlecht: "+getPatient().getPatGenderEn().name()+" Geschlecht: "+getPatient().getPatGenderEn().name()+"\n";
    }
}
