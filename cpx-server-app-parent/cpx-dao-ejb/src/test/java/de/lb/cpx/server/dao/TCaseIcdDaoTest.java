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
import de.lb.cpx.grouper.model.dto.IcdOverviewDTO;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.enums.AdmissionByLawEn;
import de.lb.cpx.model.enums.AdmissionCauseEn;
import de.lb.cpx.model.enums.AdmissionModeEn;
import de.lb.cpx.model.enums.AdmissionReason2En;
import de.lb.cpx.model.enums.AdmissionReasonEn;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.DischargeReason2En;
import de.lb.cpx.model.enums.DischargeReasonEn;
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
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author wilde
 */
@Ignore
public class TCaseIcdDaoTest {

    private static final Logger LOG = Logger.getLogger(TCaseIcdDaoTest.class.getName());

    private TransactionManager transactionManager;
    private TCaseDetailsDao tCaseDetailsDao;
    private TCaseIcdDao tCaseIcdDao;
    private TCaseDao tCaseDao;
    private TPatientDao tPatientDao;

    @Before
    public void setUp() throws IllegalAccessException {
        transactionManager = new TransactionManager("test");

        tCaseDao = new TCaseDao();
        FieldUtils.writeField(tCaseDao, "entityManager", transactionManager.getEntityManager(), true);
        tPatientDao = new TPatientDao();
        FieldUtils.writeField(tPatientDao, "entityManager", transactionManager.getEntityManager(), true);
        tCaseDetailsDao = new TCaseDetailsDao();
        FieldUtils.writeField(tCaseDetailsDao, "entityManager", transactionManager.getEntityManager(), true);
        tCaseIcdDao = new TCaseIcdDao();
        FieldUtils.writeField(tCaseIcdDao, "entityManager", transactionManager.getEntityManager(), true);
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
    public void testSimEquals() {
        TCaseIcd icd1 = new TCaseIcd();
        icd1.setIcdcCode("A");

        TCaseIcd icd2 = new TCaseIcd();
        icd2.setIcdcCode("A");

        Assert.assertTrue(icd1.simulationEquals(icd2));

        icd2.setIcdcLocEn(LocalisationEn.L);
        Assert.assertFalse(icd1.simulationEquals(icd2));

        icd1.setIcdcLocEn(LocalisationEn.L);
        Assert.assertTrue(icd1.simulationEquals(icd2));

    }

    @Test
    public void testComputeOverviewData() {
        List<TCaseDetails> db = tCaseDetailsDao.findAll();
        TCaseDetails dataSet1 = db.get(0);
        TCaseDetails dataSet2 = db.get(1);
        List<Long> versionIds = new ArrayList<>();
        versionIds.add(dataSet1.getId());
        versionIds.add(dataSet2.getId());

        List<IcdOverviewDTO> result1 = tCaseIcdDao.computeOverviewList(versionIds, GDRGModel.GDRG2013);
        Assert.assertEquals(3, result1.size());

        //simulate adding icd in ui
        TCaseIcd newIcd = new TCaseIcd();
        newIcd.setIcdcCode("A09.0");

        newIcd.setCreationDate(new Date());
        newIcd.setCaseDepartment(dataSet2.getLastDepartment());

        tCaseIcdDao.persist(newIcd);
        tCaseIcdDao.flush();

        List<IcdOverviewDTO> result2 = tCaseIcdDao.computeOverviewList(versionIds, GDRGModel.GDRG2013);
        Assert.assertEquals(4, result2.size());
    }

    @Test
    public void removeIcd() {
        List<TCaseDetails> db = tCaseDetailsDao.findAll();
//        TCaseDetails dataSet1 = db.get(0);
        TCaseDetails dataSet2 = db.get(1);
//        List<Long> versionIds = new ArrayList<>();
//        versionIds.add(dataSet1.getId());
//        versionIds.add(dataSet2.getId());

        List<TCaseIcd> result1 = tCaseIcdDao.findAll(); //tCaseIcdDao.computeOverviewList(versionIds,GDRGModel.GDRG2013);
        Assert.assertEquals(6, result1.size());

        //simulate adding icd in ui
        TCaseIcd newIcd = new TCaseIcd();
        newIcd.setIcdcCode("A09.0");

        newIcd.setCreationDate(new Date());
        newIcd.setCaseDepartment(dataSet2.getLastDepartment());

        tCaseIcdDao.persist(newIcd);
        tCaseIcdDao.flush();

        List<TCaseIcd> result2 = tCaseIcdDao.findAll();//.computeOverviewList(versionIds,GDRGModel.GDRG2013);
        Assert.assertEquals(7, result2.size());

        tCaseIcdDao.remove(tCaseIcdDao.findById(newIcd.getId()));
        tCaseIcdDao.flush();

        List<TCaseIcd> result3 = tCaseIcdDao.findAll(); //tCaseIcdDao.computeOverviewList(versionIds,GDRGModel.GDRG2013);
        Assert.assertEquals(6, result3.size());
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
//        cs.setCsCansellationReasonEn(false);
//        cs.setCsKisStatusFl(true);
//        cs.setCsStatusEn(CsStatus.NEW);
//        cs.setCsFeeGroupEn(FeeGroup.BPflVoSoE);
        tCaseDao.persist(cs);

        TCaseDetails details1 = new TCaseDetails();
        details1.setHospitalCase(cs);
        details1.setCsdVersion(1);
        details1.setCsdIsLocalFl(false);
        details1.setCsdAdmissionDate(new Date());
        details1.setCsdLos(0L);

        //details1.setCsdInsCompany("0000");
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

        TCaseDepartment dep1 = new TCaseDepartment();
        dep1.setDepShortName("0000");
        dep1.setDepKey301("0000");
        details1.getCaseDepartments().add(dep1);
        dep1.setCaseDetails(details1);
        dep1.setDepcAdmDate(new Date());
        dep1.setDepcDisDate(new Date());
        TCaseIcd icd1 = new TCaseIcd();
        dep1.getCaseIcds().add(icd1);
        icd1.setCaseDepartment(dep1);
        icd1.setIcdcIsHdxFl(true);
        icd1.setIcdcCode("A09.0");
        TCaseIcd icd2 = new TCaseIcd();
        dep1.getCaseIcds().add(icd2);
        icd2.setCaseDepartment(dep1);
        icd2.setIcdcIsHdxFl(false);
        icd2.setIcdcCode("B96.5");
        TCaseIcd icd3 = new TCaseIcd();
        dep1.getCaseIcds().add(icd3);
        icd3.setCaseDepartment(dep1);
        icd3.setIcdcIsHdxFl(false);
        icd3.setIcdcCode("C18.3");

        details1.setHdIcdCode("A09.0");
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

        TCaseDepartment dep2 = new TCaseDepartment();
        dep2.setDepShortName("0000");
        dep2.setDepKey301("0000");
        details2.getCaseDepartments().add(dep2);
        dep2.setCaseDetails(details2);
        dep2.setDepcAdmDate(new Date());
        dep2.setDepcDisDate(new Date());
        TCaseIcd icd4 = new TCaseIcd();
        dep2.getCaseIcds().add(icd4);
        icd4.setCaseDepartment(dep2);
        icd4.setIcdcIsHdxFl(true);
        icd4.setIcdcCode("A09.0");
        TCaseIcd icd5 = new TCaseIcd();
        dep2.getCaseIcds().add(icd5);
        icd5.setCaseDepartment(dep2);
        icd5.setIcdcIsHdxFl(false);
        icd5.setIcdcCode("B96.5");
        TCaseIcd icd6 = new TCaseIcd();
        icd6.setCaseDepartment(dep2);
        dep2.getCaseIcds().add(icd6);
        icd6.setIcdcIsHdxFl(false);
        icd6.setIcdcCode("C18.3");

        details2.setHdIcdCode("A09.0");
        tCaseDetailsDao.persist(details2);

        tCaseDetailsDao.flush();
    }
}
