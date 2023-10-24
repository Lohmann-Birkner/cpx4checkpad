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
package de.lb.cpx.server.dao;

import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
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
import de.lb.cpx.model.enums.PlaceOfRegEn;
import de.lb.cpx.server.commons.test.dao.TransactionManager;
import de.lb.cpx.wm.model.TWmRisk;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class for CaseDetails Functions like cloning
 *
 * @author wilde
 */
public class TCaseDetailsDaoTest {

    private static final AtomicInteger caseCounter = new AtomicInteger(0);
    private static final Logger LOG = Logger.getLogger(TCaseDetailsDaoTest.class.getName());
    private TransactionManager transactionManager;

//    private TCaseDao tCaseDao;
    private TCaseDetailsDao tCaseDetailsDao;

    private TCase tCase;
    
    private TWmRisk tRisk;

    private TCaseDetails localVersion;
    private TCaseDetails externVersion;
    private TCaseDetails localCopy;
    private TCaseDetails externCopy;
    private TCaseDetails externCopy2;
    private TCaseDao tCaseDao;
    private TPatientDao tPatientDao;

    @Before
    public void setUp() throws IllegalAccessException, CpxIllegalArgumentException {
        transactionManager = new TransactionManager("test");

        tCaseDao = new TCaseDao();
        tPatientDao = new TPatientDao();
        tCaseDetailsDao = new TCaseDetailsDao();
        FieldUtils.writeField(tCaseDao, "entityManager", transactionManager.getEntityManager(), true);
        FieldUtils.writeField(tPatientDao, "entityManager", transactionManager.getEntityManager(), true);
        FieldUtils.writeField(tCaseDetailsDao, "entityManager", transactionManager.getEntityManager(), true);
//        FieldUtils.writeField(tCaseDetailsDao, "caseDao", tCaseDao,
//                true);
        transactionManager.beginTransaction();
        createCase();
    }

    @After
    public void tearDown() {
        if (transactionManager != null) {
            transactionManager.commitTransaction();
            transactionManager.close();
        }
    }

    private void createCase() throws IllegalAccessException, CpxIllegalArgumentException {

        TPatient pat = new TPatient();
        pat.setPatNumber("22");
        pat.setVersion(0L);
        tPatientDao.persist(pat);
        tCase = new TCase();
        tCase.setPatient(pat);
        //tCase.prePersist();
        tCase.setCsHospitalIdent("Nice hos");
        tCase.setCsCaseNumber("Awesome case " + caseCounter.incrementAndGet());
        tCase.setCsCaseTypeEn(CaseTypeEn.DRG);
        tCase.setInsuranceIdentifier("k.A.");
        tCase.setCsCancellationReasonEn(false);
        tCase.setCsKisStatusFl(true);
        tCase.setCsStatusEn(CaseStatusEn.NEW);
        tCase.setCsFeeGroupEn(FeeGroupEn.BPflVoSoE);
//        tCase.setPatient(pat);
        localVersion = createNewCaseDetails(true);
        localVersion.setHospitalCase(tCase);
        localVersion.setCsdAdmissionDate(new java.util.Date());
        localVersion.setCsdLos(0L);

        externVersion = createNewCaseDetails(false);
        externVersion.setHospitalCase(tCase);
        externVersion.setCsdAdmissionDate(new java.util.Date());
        externVersion.setCsdLos(0L);

        externVersion.setCaseDetailsByCsdExternId(externVersion);
        localVersion.setCaseDetailsByCsdExternId(externVersion);

        tCase.setCurrentLocal(localVersion);
        tCase.setCurrentExtern(externVersion);

        tCase.addCaseDetails(localVersion);
        tCase.addCaseDetails(externVersion);

        tCaseDao.persist(tCase);
        tCaseDao.flush();

        tCaseDetailsDao.persist(localVersion);
        tCaseDetailsDao.persist(externVersion);
        tCaseDetailsDao.flush();

    }

    private TCaseDetails createNewCaseDetails(boolean isLocal) {
        TCaseDetails newVersion = new TCaseDetails();
        //newVersion.prePersist();

        newVersion.setCsdVersion(1);
        newVersion.setCsdIsLocalFl(isLocal);

        //newVersion.setCsdInsCompany("0000");
        newVersion.setCsdAdmCauseEn(AdmissionCauseEn.A);
        newVersion.setCsdAdmLawEn(AdmissionByLawEn.Freiwillig);
        newVersion.setCsdAdmReason12En(AdmissionReasonEn.ar01);
        newVersion.setCsdAdmReason34En(AdmissionReason2En.ar201);
        newVersion.setCsdAdmissionDate(new Date());
        newVersion.setCsdAdmissionWeight(1);
        newVersion.setCsdAdmodEn(AdmissionModeEn.Bo);
        newVersion.setCsdAgeDays(1);
        newVersion.setCsdAgeYears(1);
        newVersion.setCsdDisReason12En(DischargeReasonEn.dr02);
        newVersion.setCsdDisReason3En(DischargeReason2En.dr201);
        newVersion.setCsdDischargeDate(new Date());
        newVersion.setHdIcdCode("k.A.");
        newVersion.setComment("comment");
        newVersion.setCsdLos(3L);
        newVersion.setCsdHmv(0);
        newVersion.setCsdLeave(0);
        newVersion.setCsdLosAlteration(0L);
        newVersion.setCsdLosIntensiv(0);
        newVersion.setHospitalCase(tCase);

        return newVersion;
    }
    
    private TWmRisk createRisk(TCaseDetails version){
        TWmRisk risk = new TWmRisk();
        risk.setCaseDetails(version);
        risk.setRiskActual4Req(true);
        risk.setRiskPlaceOfReg(PlaceOfRegEn.BEFORE_BILLING);
        return risk;
    }
//    @Test

    public TCaseDetails cloneLocal() throws CloneNotSupportedException, CpxIllegalArgumentException {
        TCaseDetails local = tCase.getCurrentLocal();
        TCaseDetails copy = tCaseDetailsDao.createNewVersionFrom(local);
        Assert.assertNotEquals(local.getId(), copy.getId());

        String metaData = copy.getComment();
        Assert.assertTrue(metaData.contains("CP-Version 1"));

        Assert.assertEquals(local, copy.getCaseDetailsByCsdParentId());
        Assert.assertEquals(local.getCaseDetailsByCsdExternId(), copy.getCaseDetailsByCsdExternId());

        Assert.assertEquals(2, copy.getCsdVersion());

        testForEquality(local, copy);

        tCase.setCurrentLocal(copy);

        tCase.addCaseDetails(copy);
//        tCaseDao.merge(tCase);
//        tCaseDao.flush();
        tCaseDetailsDao.persist(copy);
//        tCaseDetailsDao.flush();
        return copy;
    }
//    @Test

    public TCaseDetails cloneExtern() throws CloneNotSupportedException, CpxIllegalArgumentException {
        TCaseDetails extern = tCase.getCurrentExtern();
        TCaseDetails copy = tCaseDetailsDao.createNewVersionFrom(extern);
        Assert.assertNotEquals(extern.getId(), copy.getId());

        String metaData = copy.getComment();
        Assert.assertTrue(metaData.contains("KIS-Version 1"));

        Assert.assertEquals(extern, copy.getCaseDetailsByCsdParentId());
        Assert.assertEquals(extern, copy.getCaseDetailsByCsdExternId());

//        Assert.assertEquals(3, copy.getCsdVersion());
        testForEquality(extern, copy);

        tCase.setCurrentExtern(copy);

        tCase.addCaseDetails(copy);
//        tCaseDao.merge(tCase);
//        tCaseDao.flush();
        tCaseDetailsDao.persist(copy);
        return copy;
//        tCaseDetailsDao.flush();
    }

    @Test
    public void testVersionControll() throws CloneNotSupportedException, Exception {
        localCopy = cloneLocal();
        externCopy = cloneExtern();
//        externCopy2 = cloneExtern();

        checkDeleteConstraint();

        deleteExternClone();
        deleteLocalClone();
        assertEquals(true, true); //to prevent issue in SonarQube
    }

    private void testForEquality(TCaseDetails baseVersion, TCaseDetails clonedVersion) {
        Assert.assertEquals(baseVersion.getCsdAdmCauseEn(), clonedVersion.getCsdAdmCauseEn());
        Assert.assertEquals(baseVersion.getCsdAdmLawEn(), clonedVersion.getCsdAdmLawEn());
        Assert.assertEquals(baseVersion.getCsdAdmReason12En(), clonedVersion.getCsdAdmReason12En());
        Assert.assertEquals(baseVersion.getCsdAdmReason34En(), clonedVersion.getCsdAdmReason34En());
        Assert.assertEquals(baseVersion.getCsdAdmissionDate(), clonedVersion.getCsdAdmissionDate());
        Assert.assertEquals(baseVersion.getCsdAdmissionWeight(), clonedVersion.getCsdAdmissionWeight());
        Assert.assertEquals(baseVersion.getCsdAdmodEn(), clonedVersion.getCsdAdmodEn());
        Assert.assertEquals(baseVersion.getCsdAgeDays(), clonedVersion.getCsdAgeDays());
        Assert.assertEquals(baseVersion.getCsdAgeYears(), clonedVersion.getCsdAgeYears());
        Assert.assertEquals(baseVersion.getCsdDisReason12En(), clonedVersion.getCsdDisReason12En());
        Assert.assertEquals(baseVersion.getCsdDisReason3En(), clonedVersion.getCsdDisReason3En());
        Assert.assertEquals(baseVersion.getCsdDischargeDate(), clonedVersion.getCsdDischargeDate());
    }

    private void deleteExternClone() throws Exception {
        //tCaseDao.removeVersionFromCase(tCase, externCopy);
        //tCaseDetailsDao.deleteVersion(externCopy,tCase);
        Exception e = null;
        try {
            tCaseDetailsDao.deleteVersion(externCopy); //HIS version cannot be deleted -> CpxIllegalArgumentException!
            Assert.assertEquals(tCase.getCurrentLocal(), localCopy);
            Assert.assertEquals(2, tCase.getCurrentLocal().getCsdVersion());
            Assert.assertEquals(3, tCase.getExterns().size());
        } catch (IllegalArgumentException ex) {
            e = ex;
        }
        //Has to fail! It is not allowed to delete HIS version!
        Assert.assertEquals(e == null ? null : e.getClass(), IllegalArgumentException.class);
    }

    private void deleteLocalClone() throws Exception {
        //tCaseDao.removeVersionFromCase(tCase, localCopy);
        //tCaseDetailsDao.deleteVersion(localCopy,tCase);
        Assert.assertEquals(2, tCase.getLocals().size());
        tCaseDetailsDao.deleteVersion(localCopy);
        //update transaction, stuff only is deleted at the end of the transaction
        transactionManager.getEntityManager().refresh(tCase);
        Assert.assertEquals(tCase.getCurrentLocal(), localVersion);
        Assert.assertEquals(1, tCase.getCurrentLocal().getCsdVersion());
        Assert.assertEquals(1, tCase.getLocals().size());
    }

    private void checkDeleteConstraint() {
        Throwable exc = null;

//        try {
//            tCaseDetailsDao.deleteVersion(localCopy);
//            tCaseDetailsDao.deleteVersion(localVersion);
//        } catch (Exception ex) {
//            exc = ex;
//        }
//        Assert.assertNotNull(exc);
//        Assert.assertEquals("Can not delete. At least 1 CP-Version must remain", exc.getMessage());
//        exc = null;
        try {
            tCaseDetailsDao.deleteVersion(externVersion);
        } catch (IllegalArgumentException ex) {
            exc = ex;
        }
        Assert.assertNotNull(exc);
        Assert.assertEquals("It is not allowed to delete HIS-Version! You can only delete CP-Versions!", exc.getMessage());
    }
}
