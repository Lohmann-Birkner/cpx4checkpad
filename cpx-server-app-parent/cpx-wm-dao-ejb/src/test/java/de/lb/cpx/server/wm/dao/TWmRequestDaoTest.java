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
//package de.lb.cpx.server.wm.dao;
//
//import de.lb.cpx.server.commons.test.dao.TransactionManager;
//import de.lb.cpx.wm.model.TWmRequestMdk;
//import org.apache.commons.lang3.reflect.FieldUtils;
//import org.junit.After;
//import org.junit.Before;
//
///**
// *
// * @author Husser
// */
//public class TWmRequestDaoTest {
//
//    private TransactionManager transactionManager;
//
//    private TWmRequestDao requestDao;
//
//    private static final String IK_NUMBER = "ikNumber1";
//    private static final String ANOTHER_IK_NUMBER = "ikNumber2";
//
//    @Before
//    public void setUp() throws IllegalAccessException {
//
//        transactionManager = new TransactionManager("test");
//
//        requestDao = new TWmRequestDao();
//        FieldUtils.writeField(requestDao, "entityManager", transactionManager.getEntityManager(), true);
//
//        transactionManager.beginTransaction();
//
//    }
//
//    @After
//    public void tearDown() {
//        if (transactionManager != null) {
//            transactionManager.commitTransaction();
//            transactionManager.close();
//        }
//    }
//
//    /*
//    @Test
//    public void testFindRequestsByIK() {
//
//        List<TWmRequest> requestsOfIK = requestDao.findRequestsByInstitution(IK_NUMBER);
//        assertTrue(requestsOfIK.isEmpty());
//
//        createRequests();
//        createRequestsMdk();
//
//        assertTrue(requestDao.findAll().size() == 3);
//        requestsOfIK = requestDao.findRequestsByInstitution(IK_NUMBER);
//        assertTrue(requestsOfIK.size() == 2);
//        assertEquals(IK_NUMBER, requestsOfIK.get(0).getIkNumber());
//
//    }
//     */
//    private void createRequests() {
//
//        TWmRequestMdk request1 = new TWmRequestMdk();
//        //request1.setRequestType(WmRequestType.bege);
//        //request1.setRequestEnumType(WmRequestTypeEn.bege);
////        request1.setIkNumber(IK_NUMBER);
//        request1.setMdkInternalId(4L); //ID in Common-DB (C_MDK)
//
//        TWmRequestMdk request2 = new TWmRequestMdk();
//        //request2.setRequestType(WmRequestType.bege);
//        //request2.setRequestEnumType(WmRequestTypeEn.bege);
////        request2.setIkNumber(ANOTHER_IK_NUMBER);
//        request1.setMdkInternalId(5L); //ID in Common-DB (C_MDK)
//
//        requestDao.persist(request1);
//        requestDao.persist(request2);
//        requestDao.flush();
//    }
//
//    private void createRequestsMdk() {
//
//        TWmRequestMdk request1 = new TWmRequestMdk();
//        //request1.setRequestType(WmRequestType.bege);
//        //request1.setRequestEnumType(WmRequestTypeEn.bege);
////        request1.setIkNumber(IK_NUMBER);
//        request1.setMdkInternalId(3L); //ID in Common-DB (C_MDK)
//
//        requestDao.persist(request1);
//        requestDao.flush();
//    }
//
//}
