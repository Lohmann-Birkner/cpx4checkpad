/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  gerschmann - initial API and implementation and/or initial documentation
 */
//package de.lb.service.readmission;
//
//import de.checkpoint.drg.GDRGModel;
//import de.checkpoint.drg.RuleCodeMgr;
//import de.lb.cpx.exceptions.CpxIllegalArgumentException;
//import de.lb.cpx.model.TCaseMergeMapping;
//import de.lb.cpx.model.enums.CaseTypeEn;
//import static de.lb.cpx.model.enums.CaseTypeEn.DRG;
//import static de.lb.cpx.model.enums.CaseTypeEn.PEPP;
//import de.lb.cpx.server.commonDB.dao.CBaserateDao;
//import de.lb.cpx.server.commons.test.dao.TransactionManager;
//import de.lb.cpx.server.dao.TCaseDao;
//import de.lb.cpx.server.dao.TCaseMergeMappingDao;
//import de.lb.cpx.server.dao.TGroupingResultsDao;
//import de.lb.cpx.server.dao.TPatientDao;
//import de.lb.cpx.server.imp.model.ImportCase2;
//import de.lb.cpx.service.ejb.GrouperCommunication;
//import de.lb.cpx.service.ejb.GrouperServiceBean;
//import de.lb.cpx.service.ejb.ReadmissionServiceBean;
//import de.lb.cpx.service.readmission.CheckReadmissionsService;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Properties;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Future;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.persistence.TypedQuery;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Root;
//import org.apache.commons.lang3.reflect.FieldUtils;
//import org.junit.After;
//import static org.junit.Assert.*;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.BDDMockito;
//import org.powermock.api.mockito.PowerMockito;
//import org.powermock.core.classloader.annotations.PowerMockIgnore;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//
///**
// * Die vordefinierten Fälle werden aus §21 importiert und gegroupt. Danach
// * werden die Regeln der Wiederaufnahmeregelung auf sie angewendet Die Fälle,
// * die den Regeln entsprechen werden in die Tabelle TCaseMergeMapping
// * gespeichert
// *
// * @author gerschmann
// */
//@RunWith(PowerMockRunner.class)
//@PrepareForTest(RuleCodeMgr.class)
//@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.dom.*"})
//public class ReadmissionServiceBeanTest {
//
//    private static final Logger LOG = Logger.getLogger(ReadmissionServiceBeanTest.class.getName());
//
//    private final P21ImportContext importCtx = new P21ImportContext(false, "1522173720e");
//
//    private TransactionManager transactionManager;
//    private TCaseDao caseDao;
//    private TPatientDao patientDao;
//    private CBaserateDao baserateDao;
//    private TCaseMergeMappingDaoTest caseDaoReadm;
//    private TPatientDao patientDaoReadm;
//    private TGroupingResultsDao groupingResultsDao;
//    private final GrouperCommunication grouperCommunication = new GrouperCommunication();
//
//    private final P21DbObjectBuilderBean p21DbObjectBuilderBean = new P21DbObjectBuilderBean();
//
//    private final ReadmissionServiceBean readmissionServiceBean = new ReadmissionServiceBean();
//    private final GrouperServiceBean grouperServiceBean = new GrouperServiceBean();
//
//    public ReadmissionServiceBeanTest() {
//    }
//
//    protected <T> T createDao(final String name, final T dao) throws IllegalAccessException {
//        FieldUtils.writeField(dao, "entityManager", transactionManager.getEntityManager(), true);
//        FieldUtils.writeField(p21DbObjectBuilderBean, name, dao, true);
//        return dao;
//    }
//
//    protected <T> T createReadmDao(final String name, final T dao) throws IllegalAccessException {
//        FieldUtils.writeField(dao, "entityManager", transactionManager.getEntityManager(), true);
//        FieldUtils.writeField(readmissionServiceBean, name, dao, true);
//        return dao;
//    }
//
//    protected <T> T createBaserateDao(final String name, final T dao) throws IllegalAccessException {
//        FieldUtils.writeField(dao, "entityManager", transactionManager.getEntityManager(), true);
//        FieldUtils.writeField(grouperServiceBean, name, dao, true);
//        return dao;
//    }
//
//    protected <T> T createGrouperCommunicationDao(final String name, final T dao) throws IllegalAccessException {
//        FieldUtils.writeField(dao, "entityManager", transactionManager.getEntityManager(), true);
//        FieldUtils.writeField(grouperCommunication, name, dao, true);
//        return dao;
//    }
//
//    protected <T> T createEJB(final String name, final Object ejbName, final T ejb) throws IllegalAccessException {
//        FieldUtils.writeField(ejbName, name, ejb, true);
//        return ejb;
//    }
//
//    protected void createGrouper(GrouperServiceBean grouperService) throws IllegalAccessException {
//        grouperService.init();
//        FieldUtils.writeField(p21DbObjectBuilderBean, "grouperService", grouperService, true);
//    }
//
//    protected void createReadmissionService(CheckReadmissionsService readmissionService) throws IllegalAccessException {
//        FieldUtils.writeField(readmissionServiceBean, "readmissionService", readmissionService, true);
//    }
//
//    private <T> List<T> readEntities(final Class<T> clazz) {
//        final CriteriaBuilder criteriaBuilder
//                = transactionManager.getEntityManager().getCriteriaBuilder();
//
//        final CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
//
//        final Root<T> root = criteriaQuery.from(clazz);
//
//        criteriaQuery.select(root);
//
//        TypedQuery<T> query = transactionManager.getEntityManager().createQuery(criteriaQuery);
//        final List<T> resultList = query.getResultList();
//        return resultList;
//    }
//
//    @Before
//    public void setUp() throws IllegalAccessException {
//
//        transactionManager = new TransactionManager("test_readmission");
//        transactionManager.beginTransaction();
//        caseDao = createDao("hospitalCaseDao", new TCaseDao());
//        groupingResultsDao = createGrouperCommunicationDao("groupingResultsDao", new TGroupingResultsDao());
//        patientDao = createDao("patientDao", new TPatientDao());
//        baserateDao = createBaserateDao("baseRate", new CBaserateDao());
//        BaserateStore baserateStoreDao = new BaserateStore();
//        createEJB("baseRate", baserateStoreDao, baserateDao);
//        caseDaoReadm = createReadmDao("mappingDao", new TCaseMergeMappingDaoTest());
//        patientDaoReadm = createReadmDao("patientDao", new TPatientDao());
//        createGrouper(grouperServiceBean);
//        //createEJB("baseRate", grouperServiceBean, baserateDao);
//        createEJB("baseRateStore", grouperServiceBean, baserateStoreDao);
//        createReadmissionService(new CheckReadmissionsService());
//        createEJB("groupingResultsDao", grouperCommunication, groupingResultsDao);
//        createEJB("grouperCommunication", p21DbObjectBuilderBean, grouperCommunication);
//        createEJB("duplicateChecker", p21DbObjectBuilderBean, new DefaultDuplicateChecker());
//        createEJB("patientDao", readmissionServiceBean, patientDaoReadm);
//        createEJB("mappingDao", readmissionServiceBean, caseDaoReadm);
//
//        final String cpxHome = getCpxHome();
//        final String baseDir;
//        if (cpxHome.isEmpty()) {
//            LOG.log(Level.WARNING, "THERE'S NO CPX_HOME ON THIS SYSTEMS ENV! I TRY A BEST GUESS STRATEGY TO FIND SERVER DIRECTORY (WD_CPX_SERVER?)");
//            Properties props = System.getProperties();
//            String userDir = props.getProperty("user.dir");
//            int pos = userDir.indexOf("cpx-server-app-parent");
//            if (pos >= 0) {
//                userDir = userDir.substring(0, pos);
//            }
//            baseDir = userDir + "WD_CPX_Server";
//        } else {
//            LOG.log(Level.INFO, "Using CPX_HOME from Systems Environment: " + cpxHome);
//            baseDir = cpxHome;
//        }
//
//        final String cpxCatalogDir = baseDir + "\\catalog\\";
//        LOG.log(Level.ALL, "set catalog path = " + cpxCatalogDir);
//        //Overwrite Catalog dir from CpxSystemProperties!
//        PowerMockito.mockStatic(RuleCodeMgr.class);
//        BDDMockito.given(RuleCodeMgr.getCatalogPath()).willReturn(
//                cpxCatalogDir
//        );
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
//    /**
//     * Import for the defined case list.
//     *
//     * @param importCaseList defined case list (the case list differs from
//     * default)
//     * @param processor processor
//     * @param importCtx context
//     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException invalid argument
//     */
//    protected void importCaseList(final List<ImportCase2> importCaseList, IP21Processor processor, P21ImportContext importCtx) throws CpxIllegalArgumentException {
//
//        List<Future<Boolean>> result = new ArrayList<>();
//
//        for (ImportCase2 importCase : importCaseList) {
//            result.add(p21DbObjectBuilderBean.processDataFromHashMap(importCase, processor, importCtx, 1000));
//        }
//        for (Future<Boolean> future : result) {
//            try {
//                future.get();
//            } catch (InterruptedException | ExecutionException ex) {
//                Logger.getLogger(ReadmissionServiceBeanTest.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
//
//    /**
//     * Test of checkReadmissions method, of class ReadmissionServiceBean.
//     *
//     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException invalid argument
//     */
//    @Test
//    public void testCheckReadmissionsDrg() throws CpxIllegalArgumentException {
//        Logger.getLogger(ReadmissionServiceBeanTest.class.getName()).log(Level.INFO, " es werden die Wiederaufnahmen aus dem Testverzeichnis test_p21_drg geprüft");
//        assertEquals(true, true); //to prevent issue in SonarQube
//        testReadmissions(DRG, 12, 6);
//    }
//
//    /**
//     * Test of checkReadmissions method, of class ReadmissionServiceBean.
//     *
//     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException invalid argument
//     */
//    @Test
//    public void testCheckReadmissionsPepp() throws CpxIllegalArgumentException {
//        Logger.getLogger(ReadmissionServiceBeanTest.class.getName()).log(Level.INFO, " es werden die Wiederaufnahmen aus dem Testverzeichnis test_p21_drg geprüft");
//        assertEquals(true, true); //to prevent issue in SonarQube
//        testReadmissions(PEPP, 15, 8);
//    }
//
//    private String getCpxHome() {
//        String tmp = System.getenv("CPX_HOME");
//        return tmp == null ? "" : tmp.trim();
//    }
//
//    private void testReadmissions(CaseTypeEn type, int caseNumber, int mergeCaseNumber) throws CpxIllegalArgumentException {
//        List<ImportCase2> importCaseList = null;
//        try {
//            importCaseList = P21ImportCaseBuilder.buildImportCases(type);
//        } catch (Exception e) {
//            Logger.getLogger(ReadmissionServiceBeanTest.class.getName()).log(Level.SEVERE, " Fehler beim P21 Import", e);
//
//        }
//        importCaseList(importCaseList, new P21ProcessorV2014(), importCtx);
//
//        List<Long> patientIds = patientDao.getAllPatientIds();
//        assertTrue("Fehler bei Import der Testfällen. Keine Patientendaten gefunden!", patientIds.size() > 0);
////        patientDao.findAll();
//        int checkedCases = 0;
//        for (int i = 0, n = patientIds.size(); i < n; i++) {
//            Long patientId = patientIds.get(i);
////            TPatient p = patientDao.findById(patientId);
////             p.getCases();
//            checkedCases += readmissionServiceBean.checkReadmissions4Patient(patientId, type, true, GDRGModel.AUTOMATIC, true);
//        }
//        assertEquals("Fehler bei Import der Testfällen: ", caseNumber, checkedCases);
//
//        List<TCaseMergeMapping> mapping = readEntities(TCaseMergeMapping.class);
//        int count = 0;
//        for (TCaseMergeMapping map : mapping) {
//            if (map.getGrpresType().equals(type)) {
//                count++;
//                Logger.getLogger(ReadmissionServiceBeanTest.class.getName()).log(Level.INFO, " fallnr: "
//                        + map.getCaseByMergeMemberCaseId().getCsCaseNumber()
//                        + " getMrgMergeIdent: " + map.getMrgMergeIdent()
//                        + ", " + map.getMrgCondition1() + ", " + map.getMrgCondition2() + ", " + map.getMrgCondition3()
//                        + ", " + map.getMrgCondition4() + ", " + map.getMrgCondition5() + ", " + map.getMrgCondition6()
//                        + ", " + map.getMrgCondition7() + ", " + map.getMrgCondition8() + ", " + map.getMrgCondition9()
//                        + ", " + map.getMrgCondition10());
//            }
//        }
//        assertEquals("Fehler bei Import der Testfällen: ", mergeCaseNumber, count);
//
//    }
//
//    /**
//     * Überschreibt die Methode getNextMergeIdent von TCaseMergeMappingDao damit
//     * der Zugriff auf die Sequence in der DB unterbunden ist
//     */
//    class TCaseMergeMappingDaoTest extends TCaseMergeMappingDao {
//
//        private int mergeInd = 1;
//
//        @Override
//        public int getNextMergeIdent() {
//            return ++mergeInd;
//        }
//
//    }
//
//}
