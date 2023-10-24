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
package de.lb.cpx.server.dao;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.grouper.model.dto.OpsOverviewDTO;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TCaseOpsGrouped;
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
import de.lb.cpx.model.enums.FeeGroupEn;
import de.lb.cpx.model.enums.LocalisationEn;
import de.lb.cpx.server.commons.test.dao.TransactionManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test-Class for the TCaseOpsDao methodes
 *
 * @author wilde
 */
public class TCaseOpsDaoTest {

    private static final Logger LOG = Logger.getLogger(TCaseOpsDaoTest.class.getName());
    private TransactionManager transactionManager;
    private TCaseDetailsDao tCaseDetailsDao;
    private TCaseOpsDao tCaseOpsDao;
    private TGroupingResultsDao tGroupingResultsDao;
    private TCaseDepartmentDao tCaseDepartmentDao;
    private TCaseDao tCaseDao;
    private TPatientDao tPatientDao;

    @Before
    public void setUp() throws IllegalAccessException {
        transactionManager = new TransactionManager("test");

        tCaseDetailsDao = new TCaseDetailsDao();
        FieldUtils.writeField(tCaseDetailsDao, "entityManager", transactionManager.getEntityManager(), true);
        tCaseDepartmentDao = new TCaseDepartmentDao();
        FieldUtils.writeField(tCaseDetailsDao, "entityManager", transactionManager.getEntityManager(), true);
        tCaseOpsDao = new TCaseOpsDao();
        FieldUtils.writeField(tCaseOpsDao, "entityManager", transactionManager.getEntityManager(), true);
        tGroupingResultsDao = new TGroupingResultsDao();
        FieldUtils.writeField(tGroupingResultsDao, "entityManager", transactionManager.getEntityManager(), true);
        tCaseDao = new TCaseDao();
        FieldUtils.writeField(tCaseDao, "entityManager", transactionManager.getEntityManager(), true);
        tPatientDao = new TPatientDao();
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

    @Test
    public void testComputeOverviewData() {
        List<TCaseDetails> db = tCaseDetailsDao.findAll();
        TCaseDetails dataSet1 = db.get(0);
        TCaseDetails dataSet2 = db.get(1);
        List<Long> versionIds = new ArrayList<>();
        versionIds.add(dataSet1.getId());
        versionIds.add(dataSet2.getId());

        List<OpsOverviewDTO> result1 = tCaseOpsDao.computeOverviewList(versionIds);
        Assert.assertEquals(4, result1.size());

        //simulate adding icd in ui
//        TCaseOps newOps = new TCaseOps();
//        newOps.setOpscCode("A09.0");
//
//        newOps.setCreationDate(new Date());
//        newOps.setCaseDepartment(dataSet2.getLastDepartment());
        TCaseOps newOps = addOps("A09.0", dataSet2.getLastDepartment());
        newOps.setCreationDate(new Date());
        tCaseOpsDao.persist(newOps);
        tCaseOpsDao.flush();
        List<OpsOverviewDTO> result2 = tCaseOpsDao.computeOverviewList(versionIds);
        Assert.assertEquals(5, result2.size());
    }

    private void createCaseDetails() {
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
        cs.setCsCancellationReasonEn(false);
        cs.setCsKisStatusFl(true);
        cs.setCsStatusEn(CaseStatusEn.NEW);
        cs.setCsFeeGroupEn(FeeGroupEn.BPflVoSoE);
        tCaseDao.persist(cs);
        TCaseDetails details1 = new TCaseDetails();
        details1.setHospitalCase(cs);
        details1.setCsdVersion(1);
        details1.setCsdIsLocalFl(false);
        details1.setCsdAdmissionDate(new Date());
        details1.setCsdLos(0L);
        details1.setCsdAdmCauseEn(AdmissionCauseEn.A);
        details1.setCsdAdmLawEn(AdmissionByLawEn.Freiwillig);
        details1.setCsdAdmReason12En(AdmissionReasonEn.ar01);
        details1.setCsdAdmReason34En(AdmissionReason2En.ar201);
        details1.setCsdAdmissionWeight(1);
        details1.setCsdAdmodEn(AdmissionModeEn.Bo);
        details1.setCsdAgeDays(1);
        details1.setCsdAgeYears(1);
        details1.setCsdDisReason12En(DischargeReasonEn.dr02);
        details1.setCsdDisReason3En(DischargeReason2En.dr201);
        details1.setHdIcdCode("k.A.");

        createGroupingResult(details1);

        TCaseDepartment dep1 = new TCaseDepartment();
        dep1.setDepShortName("shortname");
        dep1.setDepKey301("0000");

        dep1.setDepcAdmDate(new Date());
        dep1.setDepcDisDate(new Date());
        dep1.setCaseDetails(details1);
        details1.getCaseDepartments().add(dep1);

//        TCaseOps ops1 = new TCaseOps();
//        dep1.getCaseOpses().add(ops1);
//        ops1.setCaseDepartment(dep1);
//        ops1.setOpscCode("A09.0");
//        TCaseOps ops2 = new TCaseOps();
//        dep1.getCaseOpses().add(ops2);
//        ops2.setCaseDepartment(dep1);
//        ops2.setOpscCode("B96.5");
//        TCaseOps ops3 = new TCaseOps();
//        dep1.getCaseOpses().add(ops3);
//        ops3.setCaseDepartment(dep1);
//        ops3.setOpscCode("C18.3");
//        TCaseOps ops4 = new TCaseOps();
//        dep1.getCaseOpses().add(ops4);
//        ops4.setCaseDepartment(dep1);
//        ops4.setOpscCode("C18.3");
        addOps("A09.0", dep1);
        addOps("B96.5", dep1);
        addOps("C18.3", dep1);
        addOps("C18.3", dep1);
        tCaseDetailsDao.persist(details1);

        TCaseDetails details2 = new TCaseDetails();
//        details2.setId(2);
        details2.setHospitalCase(cs);
        details2.setCsdVersion(1);
        details2.setCsdIsLocalFl(true);
        details2.setCsdAdmissionDate(new Date());
        details2.setCsdLos(0L);
        //details2.setCsdInsCompany("0000");
        details2.setCsdAdmCauseEn(AdmissionCauseEn.A);
        details2.setCsdAdmLawEn(AdmissionByLawEn.Freiwillig);
        details2.setCsdAdmReason12En(AdmissionReasonEn.ar01);
        details2.setCsdAdmReason34En(AdmissionReason2En.ar201);
        details2.setCsdAdmissionWeight(1);
        details2.setCsdAdmodEn(AdmissionModeEn.Bo);
        details2.setCsdAgeDays(1);
        details2.setCsdAgeYears(1);
        details2.setCsdDisReason12En(DischargeReasonEn.dr02);
        details2.setCsdDisReason3En(DischargeReason2En.dr201);
        details2.setHdIcdCode("k.A.");
        createGroupingResult(details2);

        TCaseDepartment dep2 = new TCaseDepartment();
        dep2.setDepShortName("shortName");
        dep2.setDepKey301("0000");
        dep2.setDepcAdmDate(new Date());
        dep2.setDepcDisDate(new Date());
        details2.getCaseDepartments().add(dep2);
        dep2.setCaseDetails(details2);

//        TCaseOps ops5 = new TCaseOps();
//        dep2.getCaseOpses().add(ops5);
//        ops5.setCaseDepartment(dep2);
//        ops5.setOpscCode("A09.0");
//        TCaseOps ops6 = new TCaseOps();
//        dep2.getCaseOpses().add(ops6);
//        ops6.setCaseDepartment(dep2);
//        ops6.setOpscCode("B96.5");
//        TCaseOps ops7 = new TCaseOps();
//        ops7.setCaseDepartment(dep2);
//        dep2.getCaseOpses().add(ops7);
//        ops7.setOpscCode("C18.3");
//        TCaseOps ops8 = new TCaseOps();
//        ops8.setCaseDepartment(dep2);
//        dep2.getCaseOpses().add(ops8);
//        ops8.setOpscCode("C18.3");
        addOps("A09.0", dep2);
        addOps("B96.5", dep2);
        addOps("C18.3", dep2);
        addOps("C18.3", dep2);
//        addOps(pOpsCode, dep2);

        tCaseDetailsDao.persist(details2);

        tCaseDetailsDao.flush();
    }

    private TCaseOps addOps(String pOpsCode, TCaseDepartment pDepartment) {
        TCaseOps ops = new TCaseOps();
        ops.setCaseDepartment(pDepartment);
        pDepartment.getCaseOpses().add(ops);
        ops.setOpscCode(pOpsCode);
        ops.setOpsIsToGroupFl(true);
        ops.setOpscLocEn(LocalisationEn.E);
        ops.setOpscDatum(new Date());
        addGroupedOps(ops);
        return ops;
    }

    private TCaseOpsGrouped addGroupedOps(TCaseOps pOps) {
        TCaseOpsGrouped gr = new TCaseOpsGrouped();
        gr.setCaseOps(pOps);
        gr.setGroupingResults(tGroupingResultsDao.findAll().get(0));

        pOps.getCaseOpsGroupeds().add(gr);
        return gr;
    }

    private void createGroupingResult(TCaseDetails pDetails) {
        TGroupingResults res = new TGroupingResults();
        res.setModelIdEn(GDRGModel.GDRG10);
        pDetails.getGroupingResultses().add(res);
        res.setCaseDetails(pDetails);
        tGroupingResultsDao.persist(res);
        tGroupingResultsDao.flush();
    }
}
