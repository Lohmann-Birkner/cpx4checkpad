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

import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.server.commons.test.dao.TransactionManager;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for patient dao
 *
 * @author wilde
 */
public class TPatientDaoTest {

    private static final AtomicInteger caseCounter = new AtomicInteger(0);
    private static final LocalDate CURRENT_DATE = LocalDate.now();
    private static final Logger LOG = Logger.getLogger(TPatientDaoTest.class.getName());
    private TransactionManager transactionManager;

    private TPatientDao tPatientDao;

    private List<TPatient> tCases;
//    private TCase tCase1;
//    private TCase tCase2;

    @Before
    public void setUp() throws IllegalAccessException {

//        Server webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
//        Server tcpServer = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092").start();
        transactionManager = new TransactionManager("test");

        tPatientDao = new TPatientDao();
        FieldUtils.writeField(tPatientDao, "entityManager", transactionManager.getEntityManager(),
                true);

        transactionManager.beginTransaction();
        TPatient patient = new TPatient();
        patient.setPatNumber("1");
//        patient.setId(1);

        TCase case1 = new TCase();
        case1.setCsHospitalIdent("Nice hos");
        case1.setCsCaseNumber("Awesome case " + caseCounter.incrementAndGet());
        case1.setCsCaseTypeEn(CaseTypeEn.DRG);
        case1.setPatient(patient);
        case1.setCsCaseTypeEn(CaseTypeEn.DRG);
        case1.setInsuranceIdentifier("k.A.");
        patient.getCases().add(case1);
        TCase case2 = new TCase();
        case2.setCsHospitalIdent("Nice hos");
        case2.setCsCaseNumber("Awesome case " + caseCounter.incrementAndGet());
        case2.setCsCaseTypeEn(CaseTypeEn.DRG);
        case2.setPatient(patient);
        case2.setCsCaseTypeEn(CaseTypeEn.DRG);
        case2.setInsuranceIdentifier("k.A.");
        patient.getCases().add(case2);

        tPatientDao.persist(patient);
    }

    @After
    public void tearDown() {
        if (transactionManager != null) {
            transactionManager.commitTransaction();
            transactionManager.close();
        }
    }

    @Test
    public void testMergeFind() {
        List<Integer> idList = tPatientDao.findPatientIdsForMerging(CaseTypeEn.DRG);
        Assert.assertEquals(idList.size(), 1);
    }

}
