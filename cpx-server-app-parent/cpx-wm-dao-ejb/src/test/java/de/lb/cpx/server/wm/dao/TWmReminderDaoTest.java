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
import de.lb.cpx.wm.model.TWmReminder;
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
public class TWmReminderDaoTest {

    private TransactionManager transactionManager;

    private TWmReminderDao reminderDao;
    //private TWmReminderSubjectDao reminderSubjectDao;
    private TWmProcessDao processDao;
    private TPatientDao patientDao;

    private static final long ASSIGNED_USER_ID = 1;
    private static final long ANOTHER_USER_ID = 2;
    private SequenceNumberGenerator numberGenerator;
    private CdbSequenceDao sequenceDao;

    @Before
    public void setUp() throws IllegalAccessException {

        transactionManager = new TransactionManager("test");

        reminderDao = new TWmReminderDao();
        //reminderSubjectDao = new TWmReminderSubjectDao();
        processDao = new TWmProcessDao();
        patientDao = new TPatientDao();
        sequenceDao = new CdbSequenceDao();
        numberGenerator = new SequenceNumberGenerator();

        FieldUtils.writeField(reminderDao, "entityManager", transactionManager.getEntityManager(), true);
        //FieldUtils.writeField(reminderSubjectDao, "entityManager", transactionManager.getEntityManager(), true);
        FieldUtils.writeField(processDao, "entityManager", transactionManager.getEntityManager(), true);
        FieldUtils.writeField(patientDao, "entityManager", transactionManager.getEntityManager(), true);

        FieldUtils.writeField(sequenceDao, "entityManager", transactionManager.getEntityManager(), true);
        FieldUtils.writeField(processDao, "numberGenerator", numberGenerator, true);
        FieldUtils.writeField(numberGenerator, "seqDao", sequenceDao, true);

        transactionManager.beginTransaction();
    }

    @After
    public void tearDown() {
        if (transactionManager != null) {
            transactionManager.commitTransaction();
            transactionManager.close();
        }
    }

    @Test
    public void testRemindersByUser() {

        List<TWmReminder> remindersByUser = reminderDao.findRemindersByUser(ASSIGNED_USER_ID);
        assertTrue(remindersByUser.isEmpty());

        createReminders();

        assertTrue(reminderDao.findAll().size() == 2);
        remindersByUser = reminderDao.findRemindersByUser(ASSIGNED_USER_ID);
        assertTrue(remindersByUser.size() == 1);
        assertEquals(ASSIGNED_USER_ID, remindersByUser.get(0).getAssignedUserId());
    }

    private void createReminders() {

        TWmReminder reminder1 = new TWmReminder();
        TWmReminder reminder2 = new TWmReminder();

        TPatient patient = new TPatient();
        patient.setPatNumber("patNumber");

        //TWmReminderSubject subject = new TWmReminderSubject();
        //subject.setSubject("testSubject");
        TWmProcess process = new TWmProcessHospital();
        process.setProcessModificationUser(1L);
        process.setWorkflowState(WmWorkflowStateEn.offen);
        process.setWorkflowType(WmWorkflowTypeEn.statKH);
        process.setPatient(patient);
        process.setCourtFileNumber("courtFileNumber");
        process.setLawerFileNumber("lawerFileNumber");
        reminder1.setAssignedUserId(ASSIGNED_USER_ID);
//        reminder1.setSubject(WmReminderSubject.WaitingForMdkReport);
        reminder1.setSubject(1L);
        reminder1.setProcess(process);

        reminder2.setAssignedUserId(ANOTHER_USER_ID);
//        reminder2.setSubject(WmReminderSubject.WaitingForMdkReport);
        reminder2.setSubject(2L);
        reminder2.setProcess(process);

        patientDao.persist(patient);
        //reminderSubjectDao.persist(subject);
        processDao.persist(process);

        reminderDao.persist(reminder1);
        reminderDao.persist(reminder2);
        reminderDao.flush();
    }

}
