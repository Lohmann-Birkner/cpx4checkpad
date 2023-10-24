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
package de.lb.cpx.server.merge;

import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.GenderEn;
import de.lb.cpx.model.enums.LocalisationEn;
import de.lb.cpx.server.commons.test.dao.TransactionManager;
import de.lb.cpx.server.dao.TCaseDao;
import de.lb.cpx.server.dao.TPatientDao;
import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class for the Case Merging Bean
 *
 * @author wilde
 */
public class CaseMergingBeanTest {

    private static final AtomicInteger caseCounter = new AtomicInteger(0);
    private static final Logger LOG = Logger.getLogger(CaseMergingBeanTest.class.getName());
    private TransactionManager transactionManager;
    private CaseMergeBean mergeBean;
    private TCaseDao tCaseDao;
    private TPatientDao tPatientDao;

    @Before
    public void setUp() throws IllegalAccessException {
        transactionManager = new TransactionManager("test");
        tCaseDao = new TCaseDao();
        tPatientDao = new TPatientDao();
        FieldUtils.writeField(tCaseDao, "entityManager", transactionManager.getEntityManager(),
                true);
        FieldUtils.writeField(tPatientDao, "entityManager", transactionManager.getEntityManager(),
                true);
        mergeBean = new CaseMergeBean();
//        FieldUtils.writeField(mergeBean, "caseDao", tCaseDao,
//            true);
        transactionManager.beginTransaction();

        createTestData();

    }

    @After
    public void tearDown() {
        if (transactionManager != null) {
            transactionManager.commitTransaction();
            transactionManager.close();
        }
    }

    @Test
    public void testCaseMergeForReadmissionDrg() throws CloneNotSupportedException {
        long start = System.currentTimeMillis();
        TCase merge = mergeBean.getMergedCaseByReadmission(tCaseDao.findAllDrg(), true);
        merge.setCsCaseNumber("Merged awesome case " + caseCounter.incrementAndGet());
        tCaseDao.persist(merge);
        LOG.info("Merge DRG-Case in " + (System.currentTimeMillis() - start) + " ms");
        Assert.assertNotNull("no merge result computed", merge);
        Assert.assertSame("hmv not as expected", 6, merge.getCurrentLocal().getCsdHmv());
        Assert.assertSame("not expected amount of departments", 3, merge.getCurrentLocal().getCaseDepartments().size());
        Assert.assertSame("not expected amount of icds", 4, merge.getCurrentLocal().getAllIcds().size());
        Assert.assertSame("not expected amount of opses", 4, merge.getCurrentLocal().getAllOpses().size());
        Assert.assertSame("not expected amount of sum_icds", 3, merge.getCurrentLocal().getSumOfIcd());
        Assert.assertSame("not expected amount of sum_opses", 4, merge.getCurrentLocal().getSumOfOps());
        Assert.assertSame("not expected amount of versions", 3, merge.getCaseDetails().size());
        Assert.assertSame("Not expected md ", "B", getMd(merge, true).getIcdcCode());
        Assert.assertSame("Count of Md not as expected", 1, getMdCount(merge, true));
        Assert.assertSame("Contains wrong pseudo department", Boolean.TRUE, containsPseudo("0002", merge, true));
    }

    @Test
    public void testCaseMergeForReadmissionPepp() throws CloneNotSupportedException {
        long start = System.currentTimeMillis();
        TCase merge = mergeBean.getMergedCaseByReadmission(tCaseDao.findAllPepp(), true);
        merge.setCsCaseNumber("Merged awesome case " + caseCounter.incrementAndGet());
        tCaseDao.persist(merge);
        LOG.info("Merge PEPP-Case in " + (System.currentTimeMillis() - start) + " ms");
        Assert.assertNotNull("no merge result computed", merge);
        Assert.assertSame("hmv not as expected", 9, merge.getCurrentLocal().getCsdHmv());
        Assert.assertSame("not expected amount of departments", 4, merge.getCurrentLocal().getCaseDepartments().size());
        Assert.assertSame("not expected amount of icds", 5, merge.getCurrentLocal().getAllIcds().size());
        Assert.assertSame("not expected amount of opses", 6, merge.getCurrentLocal().getAllOpses().size());
        Assert.assertSame("not expected amount of sum_icds", 4, merge.getCurrentLocal().getSumOfIcd());
        Assert.assertSame("not expected amount of sum_opses", 6, merge.getCurrentLocal().getSumOfOps());
        Assert.assertSame("not expected amount of versions", 3, merge.getCaseDetails().size());
        Assert.assertSame("Not expected md ", "A", getMd(merge, true).getIcdcCode());
        Assert.assertSame("Count of Md not as expected", 1, getMdCount(merge, true));
        Assert.assertSame("Contains wrong pseudo department", Boolean.TRUE, containsPseudo("0002", merge, true));
    }

    @Test
    public void testMdForPepp() {
        LinkedHashMap<String, MdHelper> map = getFirstMap();
        TCaseIcd md = mergeBean.checkForMD(map, CaseTypeEn.PEPP);
        Assert.assertSame("first constellation failed,wrong code ", "F22.0", md.getIcdcCode());
        Assert.assertSame("third constellation failed, no md ", Boolean.TRUE, md.getIcdcIsHdxFl());

        LinkedHashMap<String, MdHelper> map1 = getSecondMap();
        TCaseIcd md1 = mergeBean.checkForMD(map1, CaseTypeEn.PEPP);
        Assert.assertSame("second constellation failed, wrong code ", "F20.0", md1.getIcdcCode());
        Assert.assertSame("third constellation failed,no md ", Boolean.TRUE, md1.getIcdcIsHdxFl());

        LinkedHashMap<String, MdHelper> map2 = getThirdMap();
        TCaseIcd md2 = mergeBean.checkForMD(map2, CaseTypeEn.PEPP);
        Assert.assertSame("third constellation failed, wrong code ", "F20.0", md2.getIcdcCode());
        Assert.assertSame("third constellation failed, no md", Boolean.TRUE, md2.getIcdcIsHdxFl());
    }

    private LinkedHashMap<String, MdHelper> getFirstMap() {
        LinkedHashMap<String, MdHelper> map = new LinkedHashMap<>();
        map.put("F20.0", new MdHelper(createIcd("F20.0", true, true), 13));
        map.put("F22.0", new MdHelper(createIcd("F22.0", true, true), 15));
//        map.put("F20.0", new MdHelper(createIcd("F20.0", true, true), 6));
        return map;
    }

    private LinkedHashMap<String, MdHelper> getSecondMap() {
        LinkedHashMap<String, MdHelper> map = new LinkedHashMap<>();
        map.put("F20.0", new MdHelper(createIcd("F20.0", true, true), 17));
        map.put("F22.0", new MdHelper(createIcd("F22.0", true, true), 15));
//        map.put("F20.0", new MdHelper(createIcd("F20.0", true, true), 10));
        return map;
    }

    private LinkedHashMap<String, MdHelper> getThirdMap() {
        LinkedHashMap<String, MdHelper> map = new LinkedHashMap<>();
        map.put("F20.0", new MdHelper(createIcd("F20.0", true, true), 15));
        map.put("F22.0", new MdHelper(createIcd("F22.0", true, true), 15));
//        map.put("F20.0", new MdHelper(createIcd("F20.0", true, true), 8));
        return map;
    }

    private void createTestData() {
        TPatient patient = new TPatient();
        patient.setPatGenderEn(GenderEn.M);
        patient.setPatNumber("00000");
        //DRG-Cases
        TCase cse1 = createCase(CaseTypeEn.DRG);
        cse1.setPatient(patient);
        TCaseDetails local1 = cse1.getCurrentLocal();
        local1.setCsdAdmissionDate(Date.valueOf("2017-1-1"));
        local1.setCsdDischargeDate(Date.valueOf("2017-1-2"));
        addDepartment(local1, "1000", 3);
        addIcds(local1, "1000", createIcd("A"), createIcd("B", true, true));
        addOpses(local1, "1000", createOps("1"), createOps("2"));

        TCase cse2 = createCase(CaseTypeEn.DRG);
        cse2.setPatient(patient);
        TCaseDetails local2 = cse2.getCurrentLocal();
        local2.setCsdAdmissionDate(Date.valueOf("2017-1-4"));
        local2.setCsdDischargeDate(Date.valueOf("2017-1-5"));
        addDepartment(local2, "2000", 3);
        addIcds(local2, "2000", createIcd("C", true, true), createIcd("D"), createIcd("A", false, false));
        addOpses(local2, "2000", createOps("3"), createOps("4"));

        //Pepp-Cases
        TCase cse3 = createCase(CaseTypeEn.PEPP);
        cse3.setPatient(patient);
        TCaseDetails local3 = cse3.getCurrentLocal();
        local3.setCsdAdmissionDate(Date.valueOf("2017-1-1"));
        local3.setCsdDischargeDate(Date.valueOf("2017-1-2"));
        addDepartment(local3, "3000", 3);
        addIcds(local3, "3000", createIcd("A", true, true), createIcd("B"));
        addOpses(local3, "3000", createOps("1"), createOps("2"));

        TCase cse4 = createCase(CaseTypeEn.PEPP);
        cse4.setPatient(patient);
        TCaseDetails local4 = cse4.getCurrentLocal();
        local4.setCsdAdmissionDate(Date.valueOf("2017-1-4"));
        local4.setCsdDischargeDate(Date.valueOf("2017-1-6"));
        addDepartment(local4, "4000", 3);
        addIcds(local4, "4000", createIcd("C", true, true), createIcd("D"));
        addOpses(local4, "4000", createOps("3"), createOps("4"));

        TCase cse5 = createCase(CaseTypeEn.PEPP);
        cse5.setPatient(patient);
        TCaseDetails local5 = cse5.getCurrentLocal();
        local5.setCsdAdmissionDate(Date.valueOf("2017-1-6"));
        local5.setCsdDischargeDate(Date.valueOf("2017-1-8"));
        addDepartment(local5, "5000", 3);
        addIcds(local5, "5000", createIcd("A", true, true), createIcd("E"));
        addOpses(local5, "5000", createOps("5"), createOps("6"));

        patient.getCases().add(cse1);
        patient.getCases().add(cse2);
        patient.getCases().add(cse3);
        patient.getCases().add(cse4);
        patient.getCases().add(cse5);

        tPatientDao.persist(patient);
        tCaseDao.persist(cse1);
        tCaseDao.persist(cse2);
        tCaseDao.persist(cse3);
        tCaseDao.persist(cse4);
        tCaseDao.persist(cse5);
    }

    private TCase createCase(CaseTypeEn pType) {
        TCase cse = new TCase();
        cse.setCsHospitalIdent("Nice hos");
        cse.setCsCaseNumber("Awesome case " + caseCounter.incrementAndGet());
        cse.setCsCaseTypeEn(pType);
        cse.setInsuranceIdentifier("k.A.");
        addCaseDetails(cse);
        return cse;
    }

    private void addCaseDetails(TCase pCse) {
        TCaseDetails extern = new TCaseDetails();
        extern.setCsdIsActualFl(true);
        extern.setCsdIsLocalFl(false);
        extern.setHospitalCase(pCse);
        extern.setCsdAdmissionDate(new java.util.Date());
        extern.setCsdLos(0L);
        extern.setHdIcdCode("k.A.");
        //extern.setCsdInsCompany("000000");

        TCaseDetails local = new TCaseDetails();
        local.setCsdIsActualFl(true);
        local.setCsdIsLocalFl(true);
        local.setHospitalCase(pCse);
        local.setCsdAdmissionDate(new java.util.Date());
        local.setCsdLos(0L);
        local.setHdIcdCode("k.A.");
        //local.setCsdInsCompany("000000");

        pCse.getCaseDetails().add(extern);
        pCse.getCaseDetails().add(local);
    }

    private void addDepartment(TCaseDetails pDet, String pDepShort, java.util.Date pAdmDate, java.util.Date pDisDate, int pHmv, boolean admFlg, boolean disFlg, boolean thrFlg) {
        TCaseDepartment dep = new TCaseDepartment();
        dep.setDepcAdmDate(pAdmDate);
        dep.setDepcDisDate(pDisDate);
        dep.setDepShortName("CHIA");
        dep.setDepKey301(pDepShort);
        dep.setDepcIsAdmissionFl(admFlg);
        dep.setDepcIsDischargeFl(disFlg);
        dep.setDepcIsTreatingFl(thrFlg);
        dep.setDepcHmv(pHmv);
        dep.setCaseDetails(pDet);
        pDet.addDepartment(dep);
        pDet.setCsdHmv(pHmv + pDet.getCsdHmv());
    }

    private void addDepartment(TCaseDetails pDet, String pDepShort, int pHmv) {
        addDepartment(pDet, pDepShort, pDet.getCsdAdmissionDate(), pDet.getCsdDischargeDate(), pHmv, false, false, false);
    }

    private void addOpses(TCaseDetails pDet, String pDepShort, TCaseOps... opses) {
        if (pDet.getCaseDepartments().isEmpty()) {
            LOG.info("can not add, departments not set");
            return;
        }
        for (TCaseDepartment dep : pDet.getCaseDepartments()) {
            if (dep.getDepShortName().equals(pDepShort)) {
                addOpses(dep, opses);
                return;
            }
        }
        addOpses(pDet.getCaseDepartments().iterator().next(), opses);
    }

    private void addOpses(TCaseDepartment dep, TCaseOps... opses) {
        for (TCaseOps ops : opses) {
            ops.setCaseDepartment(dep);
            dep.getCaseOpses().add(ops);
        }
    }

    private void addIcds(TCaseDetails pDet, String pDepShort, TCaseIcd... icds) {
        if (pDet.getCaseDepartments().isEmpty()) {
            LOG.info("can not add, departments not set");
            return;
        }
        for (TCaseDepartment dep : pDet.getCaseDepartments()) {
            if (dep.getDepShortName().equals(pDepShort)) {
                addIcds(dep, icds);
                return;
            }
        }
        addIcds(pDet.getCaseDepartments().iterator().next(), icds);
    }

    private void addIcds(TCaseDepartment dep, TCaseIcd... icds) {
        for (TCaseIcd ops : icds) {
            ops.setCaseDepartment(dep);
            dep.getCaseIcds().add(ops);
        }
    }

    private TCaseIcd createIcd(String pCode, boolean md, boolean fabMd) {
//        TCaseIcd icd = new TCaseIcd();
//        icd.setIcdcIsHdxFl(md);
//        icd.setIcdcIsHdbFl(fabMd);
//        icd.setIcdcCode(pCode);
//        return icd;
        return createIcd(pCode, md, fabMd, LocalisationEn.L);
    }

    private TCaseIcd createIcd(String pCode, boolean md, boolean fabMd, LocalisationEn pLoc) {
        TCaseIcd icd = new TCaseIcd();
        icd.setIcdcIsHdxFl(md);
        icd.setIcdcIsHdbFl(fabMd);
        icd.setIcdcCode(pCode);
        icd.setIcdcLocEn(pLoc);
        return icd;
    }

    private TCaseIcd createIcd(String pCode) {
        return createIcd(pCode, false, false);
    }

    private TCaseOps createOps(String pCode, Date pDate) {
        TCaseOps ops = new TCaseOps();
        ops.setOpscCode(pCode);
        ops.setOpscDatum(pDate);
        return ops;
    }

    private TCaseOps createOps(String pCode) {
        return createOps(pCode, null);
    }

    private TCaseIcd getMd(TCase pCase, boolean pLocal) {
        TCaseDetails details;
        if (pLocal) {
            details = pCase.getCurrentLocal();
        } else {
            details = pCase.getCurrentExtern();
        }
        for (TCaseDepartment dep : details.getCaseDepartments()) {
            for (TCaseIcd icd : dep.getCaseIcds()) {
                if (icd.getIcdcIsHdxFl()) {
                    return icd;
                }
            }
        }
        return null;
    }

    private int getMdCount(TCase pCase, boolean pLocal) {
        TCaseDetails details;
        if (pLocal) {
            details = pCase.getCurrentLocal();
        } else {
            details = pCase.getCurrentExtern();
        }
        int count = 0;
        for (TCaseDepartment dep : details.getCaseDepartments()) {
            for (TCaseIcd icd : dep.getCaseIcds()) {
                if (icd.getIcdcIsHdxFl()) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean containsPseudo(String pPseudo, TCase pCase, boolean pLocal) {
        TCaseDetails details;
        if (pLocal) {
            details = pCase.getCurrentLocal();
        } else {
            details = pCase.getCurrentExtern();
        }
        for (TCaseDepartment dep : details.getCaseDepartments()) {
            if (dep.getDepKey301().equals(pPseudo) && dep.isPseudo()) {
                return true;
            }
        }
        return false;
    }
}
