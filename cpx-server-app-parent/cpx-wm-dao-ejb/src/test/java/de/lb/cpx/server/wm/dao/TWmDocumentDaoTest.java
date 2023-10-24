/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.wm.dao;

import de.lb.cpx.model.TPatient;
import de.lb.cpx.server.commonDB.dao.CdbSequenceDao;
import de.lb.cpx.server.commons.test.dao.TransactionManager;
import de.lb.cpx.server.dao.TPatientDao;
import de.lb.cpx.server.generator.SequenceNumberGenerator;
import de.lb.cpx.wm.model.TWmDocument;
import de.lb.cpx.wm.model.TWmProcess;
import de.lb.cpx.wm.model.TWmProcessHospital;
import de.lb.cpx.wm.model.enums.WmWorkflowStateEn;
import de.lb.cpx.wm.model.enums.WmWorkflowTypeEn;
import java.util.List;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author nandola
 */
public class TWmDocumentDaoTest {

    private TransactionManager transactionManager;
    private TWmProcessDao processDao;
    private TWmDocumentDao docDao;
    private TPatientDao patientDao;
    private SequenceNumberGenerator numberGenerator;
    private CdbSequenceDao sequenceDao;
    //private TPatient patient1;
    //private TWmProcess process;

    private static final long ASSIGNED_USER_ID = 1;
    private static final long DOCUMENT_ID = 123456789;
    private static final String PATIENT_NUMBER = "patientNumber";

    @Before
    public void setUp() throws IllegalAccessException {
        transactionManager = new TransactionManager("test");

        docDao = new TWmDocumentDao();
        processDao = new TWmProcessDao();
        patientDao = new TPatientDao();
        sequenceDao = new CdbSequenceDao();
        numberGenerator = new SequenceNumberGenerator();

        FieldUtils.writeField(processDao, "entityManager", transactionManager.getEntityManager(), true);
        FieldUtils.writeField(patientDao, "entityManager", transactionManager.getEntityManager(), true);
        FieldUtils.writeField(docDao, "entityManager", transactionManager.getEntityManager(), true);

        FieldUtils.writeField(sequenceDao, "entityManager", transactionManager.getEntityManager(), true);
        FieldUtils.writeField(processDao, "numberGenerator", numberGenerator, true);
        FieldUtils.writeField(numberGenerator, "seqDao", sequenceDao, true);

//        patient1 = new TPatient();
//        patient1.setPatNumber(PATIENT_NUMBER);
//        process = new TWmProcessHospital();
//        process.setProcessModificationUser(1L);
//        process.setWorkflowState(WorkflowState.offen);
//        process.setWorkflowType(WorkflowType.statKH);
//        process.setPatient(patient1);
        transactionManager.beginTransaction();

//        patientDao.persist(patient1);
//        patientDao.flush();
//        processDao.persist(process);
//        processDao.flush();
    }

    @After
    public void tearDown() {
        if (transactionManager != null) {
            transactionManager.commitTransaction();
            transactionManager.close();
        }
    }

    @Test
    public void testDocuments() {

        createDocuments();

        List<TWmDocument> allDocs = docDao.findAll();
        assertTrue(!allDocs.isEmpty()); //list of docs are not empty
        assertTrue(allDocs.size() == 2); //total two documents
//        assertEquals(allDocs.get(0).getDocumentType(), WmDocumentType.mdkGut.toString());
    }

    private void createDocuments() {
        TWmDocument doc = new TWmDocument();
        TWmDocument doc2 = new TWmDocument();

        TPatient patient = new TPatient();
        patient.setPatNumber("patNumber");

        TWmProcess process = new TWmProcessHospital();
        process.setProcessModificationUser(1L);
        process.setWorkflowState(WmWorkflowStateEn.offen);
        process.setWorkflowType(WmWorkflowTypeEn.statKH);
        process.setPatient(patient);
//        Set<TWmDocument> setOfDocs = new HashSet<>();
//        setOfDocs.add(doc);
//        setOfDocs.add(doc2);
//        process.setDocuments(setOfDocs);

//        doc.setDocumentType(WmDocumentTypeEn.mdkGut.toString());
        doc.setDocumentType(1L);
//        doc.setId(123456789);
        doc.setName("test document");
        doc.setProcess(process);

//        doc2.setDocumentType(WmDocumentTypeEn.mdkGut.toString());
        doc2.setDocumentType(2L);
//        doc2.setId(1234567891);
        doc2.setName("test new document");
        doc2.setProcess(process);

        patientDao.persist(patient);
        processDao.persist(process);
        docDao.persist(doc);
        docDao.persist(doc2);

        docDao.flush();

    }

}
