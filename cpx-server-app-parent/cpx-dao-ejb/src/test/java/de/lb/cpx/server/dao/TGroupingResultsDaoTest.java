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
 *    2017  Reem - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.dao;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TCaseOpsGrouped;
import de.lb.cpx.model.TCaseSupplFee;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.enums.AdmissionByLawEn;
import de.lb.cpx.model.enums.AdmissionCauseEn;
import de.lb.cpx.model.enums.AdmissionModeEn;
import de.lb.cpx.model.enums.AdmissionReason2En;
import de.lb.cpx.model.enums.AdmissionReasonEn;
import de.lb.cpx.model.enums.CaseStatusEn;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.DischargeReason2En;
import de.lb.cpx.model.enums.DischargeReasonEn;
import de.lb.cpx.model.enums.GroupResultPdxEn;
import de.lb.cpx.model.enums.GrouperMdcOrSkEn;
import de.lb.cpx.model.enums.GrouperStatusEn;
import de.lb.cpx.model.enums.LocalisationEn;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.server.commons.test.dao.TransactionManager;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author shahin
 */
public class TGroupingResultsDaoTest {

    private static final Logger LOG = Logger.getLogger(TGroupingResultsDaoTest.class.getName());

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    private TransactionManager transactionManager;

    private TCaseDetailsDao tCaseDetailsDao;
    private TCaseDao tCaseDao;
    private TPatientDao tPatientDao;
    private TCaseOpsDao tCaseOpsDao;
    private TGroupingResultsDao tGroupingResultsDao;

    public TGroupingResultsDaoTest() {
    }

    @Before
    public void setUp() throws IllegalAccessException {

        transactionManager = new TransactionManager("test");

        tCaseDetailsDao = new TCaseDetailsDao();
        tCaseOpsDao = new TCaseOpsDao();
        tCaseDao = new TCaseDao();
        tPatientDao = new TPatientDao();
        tGroupingResultsDao = new TGroupingResultsDao();
        FieldUtils.writeField(tCaseDetailsDao, "entityManager", transactionManager.getEntityManager(), true);
        FieldUtils.writeField(tCaseOpsDao, "entityManager", transactionManager.getEntityManager(), true);
        FieldUtils.writeField(tCaseDetailsDao, "entityManager", transactionManager.getEntityManager(), true);
        FieldUtils.writeField(tGroupingResultsDao, "entityManager", transactionManager.getEntityManager(), true);
        FieldUtils.writeField(tCaseDao, "entityManager", transactionManager.getEntityManager(), true);
        FieldUtils.writeField(tPatientDao, "entityManager", transactionManager.getEntityManager(), true);
        transactionManager.beginTransaction();
        createCaseDetails();
    }

    @After
    public void tearDown() {
        if (transactionManager != null) {
            transactionManager.commitTransaction();
            transactionManager.close();
        }
    }

    private void createCaseDetails() throws IllegalAccessException {

        TPatient patient = new TPatient();
        patient.setPatNumber("1");
        tPatientDao.persist(patient);

        TCase cs = new TCase();
        cs.setPatient(patient);
        cs.setCsHospitalIdent("Nice hos");
        cs.setCsCaseNumber("My hos case");
        cs.setCsCaseTypeEn(CaseTypeEn.DRG);
//        cs.setPatient(patient);
        cs.setCsCaseTypeEn(CaseTypeEn.DRG);
        cs.setInsuranceIdentifier("k.A.");
        cs.setCsCancellationReasonEn(true);
        cs.setCsKisStatusFl(true);
//        cs.setPatient(patient);
        cs.setCsStatusEn(CaseStatusEn.CLOSED);
        tCaseDao.persist(cs);
        TCaseSupplFee fee = new TCaseSupplFee();
        fee.setCsuplfeeCode("ZE137.02");//ZE-Code
        fee.setCsuplValue(860.31);//Eurowert zu Zusatzentgelt .
        fee.setCsuplTypeEn(SupplFeeTypeEn.ZE);// Art des Zusatzentgeltes (ZE, ZP, ET) .
        fee.setCsuplCwValue(0);//Ermittelte CW 
        fee.setCsuplCount(1);//Anzahl des Zusatzentgeltes .

        TCaseDetails details1 = new TCaseDetails();
        details1.setHospitalCase(cs);
        details1.setCsdVersion(1); //version
        details1.setCsdIsLocalFl(false); //nicht local
        details1.setCsdIsActualFl(true);
        details1.setCsdAdmissionDate(new Date());
        details1.setCsdLos(0L);
        //details1.setCsdInsCompany("0000");// IKZ
        details1.setCsdAdmCauseEn(AdmissionCauseEn.A);// Aufnahmeanlass 
        details1.setCsdAdmLawEn(AdmissionByLawEn.Freiwillig);
        details1.setCsdAdmReason12En(AdmissionReasonEn.ar01);//Aufnahmegrund 1
        details1.setCsdAdmReason34En(AdmissionReason2En.ar201);// Aufnahmegrund 2
        details1.setCsdAdmissionWeight(1);// gewischt 
        details1.setCsdAdmodEn(AdmissionModeEn.Bo);//Erbringungsart
        details1.setCsdAgeDays(1);// Alter in  Tage
        details1.setCsdAgeYears(1);//Alter in Jahre
        details1.setCsdDisReason12En(DischargeReasonEn.dr02);//Entlassunggrund 1 
        details1.setCsdDisReason3En(DischargeReason2En.dr201);//Entlassunggrund 2 
        details1.setHdIcdCode("k.A.");
        details1.setCsdAgeDays(5);
        details1.setCsdLeave(5);
        details1.setCsdLos(6L);
        details1.setCsdLosIntensiv(0);
        details1.setCsdLosIntensiv(0);
        details1.setVersion(0L);

        TCaseDepartment dep1 = new TCaseDepartment();
        dep1.setDepKey301("0000");//Abteilungsschlüssel nach §301
        dep1.setDepShortName("abcd");//Abteilungsschlüssel nach §301
        dep1.setCaseDetails(details1);//Verweis auf  TCaseDetails.
        dep1.setDepcAdmDate(new Date());//Datum, an dem die Bewegung begann.
        dep1.setDepcDisDate(new Date());
        dep1.setDepcHmv(0);
        dep1.setDepcIsAdmissionFl(true);
        dep1.setDepcIsDischargeFl(true);
        dep1.setDepcIsTreatingFl(true);
        TCaseOps ops1 = new TCaseOps();
        ops1.setOpsIsToGroupFl(true);
        ops1.setOpscCode("5-11");
        ops1.setOpscLocEn(LocalisationEn.E);
        ops1.setOpscDatum(new Date());
        ops1.setCaseDepartment(dep1);//Verweis auf TCaseDepartment
        ops1.setOpscCode("8-83b.bb");//OPS - Code
        ops1.setOpsIsToGroupFl(true);
        ops1.setOpscLocEn(LocalisationEn.E);
//        ops1.setId(3);
        dep1.getCaseOpses().add(ops1);
        dep1.setCaseDetails(details1);
        details1.getCaseDepartments().add(dep1);

        TCaseOpsGrouped opsGrouper = new TCaseOpsGrouped();
        opsGrouper.setOpsResU4gFl(true);// Grouper Flag
        opsGrouper.setOpsResValidEn(0);// Gültigkeit der OPS
        ops1.getCaseOpsGroupeds().add(opsGrouper);
        opsGrouper.setCaseOps(ops1); //Verweis auf  TCaseOps
        fee.setCaseOpsGrouped(opsGrouper);

        opsGrouper.setCaseSupplFees(fee);

        TGroupingResults result = new TGroupingResults();
        result.setGrpresType(CaseTypeEn.DRG);// Art der Fall
        result.setGrpresCode("A90A");// DRG Code
        result.setGrpresGpdx(GroupResultPdxEn.pdx0);// Fehlerinfo für HD
        result.setGrpresGroup(GrouperMdcOrSkEn.pre);
        result.setGrpresGst(GrouperStatusEn.GST00);//Grouperstatus
        result.setGrpresIsAutoFl(true);//Flag für Automatische Grouper
        result.setGrpresPccl(0);//PCCL des Falles.
        result.setCalculatedLeave(0);
        result.setCalculatedLengthOfStay(0);
        result.setModelIdEn(GDRGModel.GDRG2013);// Groupermodell 
        result.setGrpresIsAutoFl(true);
        result.setGrpresIsDayCareFl(true);
        result.setGrpresIsNegotiatedFl(true);
        result.setCaseDetails(details1);

        opsGrouper.setGroupingResults(result);
        fee.setCaseOpsGrouped(opsGrouper);
        opsGrouper.setCaseSupplFees(fee);

        details1.getGroupingResultses().add(result);
        result.setCaseDetails(details1);//Verweis auf TCaseDetails

        result.getCaseOpsGroupeds().add(opsGrouper);
        opsGrouper.setGroupingResults(result);

        tGroupingResultsDao.persist(result);

        TCaseSupplFee fee2 = new TCaseSupplFee();
        fee2.setCsuplfeeCode("ZE60.01");
        fee2.setCsuplValue(1261.97);
        fee2.setCsuplTypeEn(SupplFeeTypeEn.ZE);
        fee2.setCsuplCwValue(0);
        fee2.setCsuplCount(2);

        TCaseDetails details2 = new TCaseDetails();
        details2.setHospitalCase(cs);
        details2.setCsdVersion(1); //version
        details2.setCsdIsLocalFl(false); //nicht local
        details2.setCsdIsActualFl(true);
        details2.setCsdAdmissionDate(new Date());
        details2.setCsdLos(0L);
        //details2.setCsdInsCompany("0000");// IKZ
        details2.setCsdAdmCauseEn(AdmissionCauseEn.A);// Aufnahmeanlass 
        details2.setCsdAdmLawEn(AdmissionByLawEn.Freiwillig);
        details2.setCsdAdmReason12En(AdmissionReasonEn.ar01);//Aufnahmegrund 1 
        details2.setCsdAdmReason34En(AdmissionReason2En.ar201);// Aufnahmegrund 2 
        details2.setCsdAdmissionWeight(1);// gewischt 
        details2.setCsdAdmodEn(AdmissionModeEn.Bo);
        details2.setCsdAgeDays(1);// Alter in  Tage
        details2.setCsdAgeYears(1);//Alter in Jahre
        details2.setCsdDisReason12En(DischargeReasonEn.dr02);//Entlassunggrund 1 
        details2.setCsdDisReason3En(DischargeReason2En.dr201);//Entlassunggrund 2 
        details2.setHdIcdCode("k.A.");
        dep1.setCaseDetails(details2);
        TCaseDepartment dep2 = new TCaseDepartment();
        dep2.setDepKey301("1600");
        dep2.setDepShortName("efgh");
        details2.getCaseDepartments().add(dep2);
        dep2.setCaseDetails(details2);
        dep2.setDepcAdmDate(new Date());
        dep2.setDepcDisDate(new Date());
        TCaseOps ops2 = new TCaseOps();
        dep2.getCaseOpses().add(ops2);
        ops2.setCaseDepartment(dep2);
        ops2.setOpscCode("8-982.1");

        TCaseOpsGrouped opsGrouper2 = new TCaseOpsGrouped();
        opsGrouper2.setOpsResU4gFl(true);
        opsGrouper2.setOpsResValidEn(0);
        ops2.setOpsIsToGroupFl(true);
        ops2.setOpscLocEn(LocalisationEn.E);
        ops2.getCaseOpsGroupeds().add(opsGrouper2);
        opsGrouper2.setCaseOps(ops2);
        fee2.setCaseOpsGrouped(opsGrouper2);
        opsGrouper2.setCaseSupplFees(fee2);

        TGroupingResults result2 = new TGroupingResults();
        result2.setGrpresType(CaseTypeEn.DRG);
        result2.setGrpresCode("A90A");
        result2.setGrpresGpdx(GroupResultPdxEn.pdx0);
        result2.setGrpresGroup(GrouperMdcOrSkEn.pre);
        result2.setGrpresGst(GrouperStatusEn.GST00);
        result2.setGrpresIsAutoFl(true);
        result2.setGrpresPccl(0);
        result2.setGrpresType(CaseTypeEn.DRG);
        result2.setCalculatedLeave(0);
        result2.setCalculatedLengthOfStay(0);
        result2.setModelIdEn(GDRGModel.GDRG2013);
        result2.setGrpresIsAutoFl(true);
        result2.setGrpresIsDayCareFl(true);
        result2.setGrpresIsNegotiatedFl(true);
        result2.setCaseDetails(details1);

        details2.getGroupingResultses().add(result2);
        result2.setCaseDetails(details2);
        opsGrouper2.setCaseOps(ops2);
        fee2.setCaseOpsGrouped(opsGrouper2);
        opsGrouper2.setCaseSupplFees(fee2);
        result2.getCaseOpsGroupeds().add(opsGrouper2);
        opsGrouper2.setGroupingResults(result2);

        tGroupingResultsDao.persist(result2);//////

        tGroupingResultsDao.flush();
    }

    @Test
    public void getSupplementaryValueForIdTest() {
        List<TGroupingResults> db = tGroupingResultsDao.findAll();

        TGroupingResults result1 = db.get(0);
        TGroupingResults result2 = db.get(1);

        double sppFee1 = tGroupingResultsDao.getSupplementaryValueForId(result1.id);
        Assert.assertEquals(860.31, sppFee1, 0.0);

        double sppFee2 = tGroupingResultsDao.getSupplementaryValueForId(result2.id);
        Assert.assertEquals(2523.94, sppFee2, 0.0);

    }

}
