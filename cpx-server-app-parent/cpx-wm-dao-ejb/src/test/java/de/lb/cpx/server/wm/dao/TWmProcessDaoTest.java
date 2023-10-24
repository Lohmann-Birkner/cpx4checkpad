/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Husser - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.wm.dao;

import de.lb.cpx.model.TPatient;
import de.lb.cpx.server.commonDB.dao.CdbSequenceDao;
import de.lb.cpx.server.commons.test.dao.TransactionManager;
import de.lb.cpx.server.dao.TPatientDao;
import de.lb.cpx.server.generator.SequenceNumberGenerator;
import de.lb.cpx.wm.model.TWmProcess;
import de.lb.cpx.wm.model.TWmProcessHospital;
import de.lb.cpx.wm.model.enums.WmWorkflowStateEn;
import de.lb.cpx.wm.model.enums.WmWorkflowTypeEn;
import java.util.List;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Husser
 */
public class TWmProcessDaoTest {

    private TransactionManager transactionManager;

    private TWmProcessDao processDao;
    private TPatientDao patientDao;

    private static final long LAST_USER_ID = 1;
    private static final long ANOTHER_USER_ID = 2;

    private TPatient patient1;
    private TPatient patient2;

    private static final String PATIENT_NUMBER = "patientNumber";
    private static final String ANOTHER_PATIENT_NUMBER = "anotherPatientNumber";
    private SequenceNumberGenerator numberGenerator;
    private CdbSequenceDao sequenceDao;

    @Before
    public void setUp() throws IllegalAccessException {

        transactionManager = new TransactionManager("test");

        processDao = new TWmProcessDao();
        patientDao = new TPatientDao();
        sequenceDao = new CdbSequenceDao();
        numberGenerator = new SequenceNumberGenerator();
        FieldUtils.writeField(processDao, "entityManager", transactionManager.getEntityManager(), true);
        FieldUtils.writeField(patientDao, "entityManager", transactionManager.getEntityManager(), true);
        FieldUtils.writeField(sequenceDao, "entityManager", transactionManager.getEntityManager(), true);
        FieldUtils.writeField(processDao, "numberGenerator", numberGenerator, true);
        FieldUtils.writeField(numberGenerator, "seqDao", sequenceDao, true);

        patient1 = new TPatient();
        patient1.setPatNumber(PATIENT_NUMBER);

        patient2 = new TPatient();
        patient2.setPatNumber(ANOTHER_PATIENT_NUMBER);

        transactionManager.beginTransaction();

        patientDao.persist(patient1);
        patientDao.persist(patient2);
        patientDao.flush();
    }

    @After
    public void tearDown() {
        if (transactionManager != null) {
            transactionManager.commitTransaction();
            transactionManager.close();
        }
    }

    @Test
    public void testFindWorkflowsByLastUser() {

        List<TWmProcess> workflowsOfLastUser = processDao.findWorkflowsByLastUser(LAST_USER_ID);
        assertTrue(workflowsOfLastUser.isEmpty());

        createWorkflows();

        assertTrue(processDao.findAll().size() == 2);
        workflowsOfLastUser = processDao.findWorkflowsByLastUser(LAST_USER_ID);
        assertTrue(workflowsOfLastUser.size() == 1);
        assertEquals(LAST_USER_ID, workflowsOfLastUser.get(0).getProcessModificationUser());
    }

    @Test
    public void testFindWorkflowsByPatient() {

        List<TWmProcess> workflowsOfLastUser = processDao.findWorkflowsByPatient(patient2);
        assertTrue(workflowsOfLastUser.isEmpty());

        createWorkflows();

        assertTrue(processDao.findAll().size() == 2);
        workflowsOfLastUser = processDao.findWorkflowsByPatient(patient2);
        assertTrue(workflowsOfLastUser.size() == 1);
        assertEquals(patient2, workflowsOfLastUser.get(0).getPatient());
    }

    private void createWorkflows() {

        TWmProcess process1 = new TWmProcessHospital();
        process1.setProcessModificationUser(LAST_USER_ID);
        process1.setWorkflowState(WmWorkflowStateEn.offen);
        process1.setWorkflowType(WmWorkflowTypeEn.statKH);
        process1.setPatient(patient1);

        TWmProcess process2 = new TWmProcessHospital();
        process2.setProcessModificationUser(ANOTHER_USER_ID);
        process2.setWorkflowState(WmWorkflowStateEn.offen);
        process2.setWorkflowType(WmWorkflowTypeEn.statKH);
        process2.setPatient(patient2);

        processDao.persist(process1);
        processDao.persist(process2);

        processDao.flush();
    }

}
