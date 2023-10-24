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
package de.lb.cpx.report.generator.test;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseComment;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.enums.CommentTypeEn;
import de.lb.cpx.model.enums.CountryEn;
import de.lb.cpx.model.enums.IdentClassEn;
import de.lb.cpx.model.enums.StateEn;
import de.lb.cpx.report.generator.fop.ReportGenerator;
import de.lb.cpx.report.generator.xml.XmlGenerator;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.commonDB.dao.CHospitalDao;
import de.lb.cpx.server.commonDB.dao.CInsuranceCompanyDao;
import de.lb.cpx.server.commonDB.dao.CWardDao;
import de.lb.cpx.server.commonDB.model.CHospital;
import de.lb.cpx.server.commonDB.model.CInsuranceCompany;
import de.lb.cpx.server.commonDB.model.CWard;
import de.lb.cpx.server.commonDB.model.CdbUsers;
import de.lb.cpx.server.commons.test.dao.TransactionManager;
import de.lb.cpx.server.config.CpxServerConfig;
import de.lb.cpx.server.dao.TCaseCommentDao;
import de.lb.cpx.server.dao.TCaseDao;
import de.lb.cpx.server.dao.TCaseDetailsDao;
import de.lb.cpx.server.dao.TGroupingResultsDao;
import de.lb.cpx.server.dao.TPatientDao;
import de.lb.cpx.system.properties.CpxSystemProperties;
import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test for XML file creation.
 *
 * @author nandola
 */
@Stateless
public class ReportGeneratorTest {

    private static final String CASE_NUMBER = "case_number";
    private static final String HOSP_NUMBER = "hos_ident";
    private static final String PAT_NUMBER = "pat_no";
    private static final String WARD_SHORT_NUMBER = "ward_no";
    private static final String INS_COMP_NUMBER = "insCom_no";
    private static final String CASE_COMMENT_NUMBER = "caseComment_no";
    //                    try {
//                    String reportPath = reportGen.generatePDFContent(xmlFile);
//                    if (reportPath != null && reportPath.isEmpty() == false) {
//                        report = new File(reportPath);
//                        FileInputStream in = new FileInputStream(report);
//                        byteArray = IOUtils.toByteArray(in);
//                        if (byteArray != null && byteArray.length != 0) {
//                            System.out.println("Report is Generated");
//                        }
//                    } else {
//                        System.out.println("Report isn't generated or doesn't exist");
//                    }
//                } catch (TransformerException | ParserConfigurationException | SAXException | IOException ex) {
//                    Logger.getLogger(ReportGeneratorTest.class.getName()).log(Level.SEVERE, null, ex);
//                }
    private static final Logger LOG = Logger.getLogger(ReportGeneratorTest.class.getName());

    // Executed once, before the start of all tests. It is used to perform time intensive activities, for example, to connect to a database. Methods marked with this annotation need to be defined as static to work with JUnit.
    @BeforeClass
    public static void setUpClass() {
        LOG.log(Level.INFO, "running ReportGeneratorTest init");
    }

    // Executed once, after all tests have been finished. It is used to perform clean-up activities, for example, to disconnect from a database. Methods annotated with this annotation need to be defined as static to work with JUnit.
    @AfterClass
    public static void tearDownClass() {
        LOG.log(Level.INFO, "running ReportGeneratorTest cleanUp");
    }

    private ReportGenerator reportGen;
//    private XmlGenerator xmlGen;
    @Inject
    private XmlGenerator xmlGenerator;
    private TransactionManager transactionManager;
    private TCaseDao hospitalCaseDao;
    private TPatientDao patientDao;
    private ClientManager clientManager;
    private CHospitalDao hospDao;
    private CHospital cHospital;
    private CInsuranceCompanyDao insuCompDao;
    private CInsuranceCompany cInsComp;
    private CWardDao wardDao;
    private CWard cWard;
    private TCaseDetails tCaseDetails;
    private TCaseDetails tCaseDetails2;
    private TCaseDetailsDao tCaseDetailsDao;
    private TGroupingResultsDao groupingResultDao;
    private TGroupingResults tGroupingResults;
    private CdbUsers cdbUsers;
    private TCaseComment caseComment;
    private TCaseCommentDao caseCommentDao;

//    private File report;
//    private byte[] byteArray;
    private TCase tCase;
    private TPatient tPatient;

    // Executed before each test. It is used to prepare the test environment(e.g., read input data, initialize the class).
    @Before
    public void setUp() throws IllegalAccessException, CpxIllegalArgumentException {
        final String cpxHome = getCpxHome();
        final String baseDir;
        if (cpxHome.isEmpty()) {
            LOG.log(Level.WARNING, "THERE'S NO CPX_HOME ON THIS SYSTEMS ENV! I TRY A BEST GUESS STRATEGY TO FIND SERVER DIRECTORY (WD_CPX_SERVER?)");
            Properties props = System.getProperties();
            String userDir = props.getProperty("user.dir");
            int pos = userDir.indexOf("cpx-");
            if (pos >= 0) {
                userDir = userDir.substring(0, pos);
            }
            baseDir = userDir + "WD_CPX_Server";
        } else {
            LOG.log(Level.INFO, "Using CPX_HOME from Systems Environment: " + cpxHome);
            baseDir = cpxHome;
        }
        CpxSystemProperties.setCpxHome(baseDir);

        transactionManager = new TransactionManager("test");

        xmlGenerator = new XmlGenerator();
        reportGen = new ReportGenerator();
        clientManager = new ClientManager();

        hospitalCaseDao = new TCaseDao();
        patientDao = new TPatientDao();
        hospDao = new CHospitalDao();
        insuCompDao = new CInsuranceCompanyDao();
        groupingResultDao = new TGroupingResultsDao();
        wardDao = new CWardDao();
        tCaseDetailsDao = new TCaseDetailsDao();
        caseCommentDao = new TCaseCommentDao();
        CpxServerConfig cpxServerConfig = new CpxServerConfig();

        cdbUsers = new CdbUsers();
        cdbUsers.setUTitle("Herr");
        cdbUsers.setUFirstName("Michael");
        cdbUsers.setULastName("Jordan");

        FieldUtils.writeField(hospitalCaseDao, "entityManager", transactionManager.getEntityManager(), true);
        FieldUtils.writeField(patientDao, "entityManager", transactionManager.getEntityManager(), true);
        FieldUtils.writeField(hospDao, "entityManager", transactionManager.getEntityManager(), true);
        FieldUtils.writeField(insuCompDao, "entityManager", transactionManager.getEntityManager(), true);
        FieldUtils.writeField(wardDao, "entityManager", transactionManager.getEntityManager(), true);
        FieldUtils.writeField(groupingResultDao, "entityManager", transactionManager.getEntityManager(), true);
        FieldUtils.writeField(xmlGenerator, "hospDao", hospDao, true);
        FieldUtils.writeField(xmlGenerator, "insuCompDao", insuCompDao, true);
        FieldUtils.writeField(xmlGenerator, "resultDao", groupingResultDao, true);
        FieldUtils.writeField(tCaseDetailsDao, "entityManager", transactionManager.getEntityManager(), true);
//        FieldUtils.writeField(clientManager, "entityManager", transactionManager.getEntityManager(), true);
        FieldUtils.writeField(xmlGenerator, "cdbUsers", cdbUsers, true);
//        FieldUtils.writeField(xmlGenerator, "clientManager", clientManager, true);
        FieldUtils.writeField(caseCommentDao, "entityManager", transactionManager.getEntityManager(), true);
        FieldUtils.writeField(xmlGenerator, "caseCommentDao", caseCommentDao, true);
        FieldUtils.writeField(xmlGenerator, "cpxServerConfig", cpxServerConfig, true);

        tCase = new TCase();
        tCase.setCsCaseNumber(CASE_NUMBER);
//        tCase.setCsHospitalIdent(HOSP_NUMBER);

        tPatient = new TPatient();
        tPatient.setPatNumber(PAT_NUMBER);
        Set<TCase> setOfCases = new HashSet<>(0);
        setOfCases.add(tCase);
        tPatient.setCases(setOfCases);
        // set patient to the case.
        tCase.setPatient(tPatient);

        cHospital = new CHospital();
        cHospital.setHosIdent(HOSP_NUMBER);
        cHospital.setHosIdentClassEn(IdentClassEn.id10);
        tCase.setCsHospitalIdent(cHospital.getHosIdent());

        cInsComp = new CInsuranceCompany();
        cInsComp.setCountryEn(CountryEn.de);
        cInsComp.setInscIdent("101320043");
        cInsComp.setInscIdentClassEn(IdentClassEn.id10);
        cInsComp.setInscName("Betriebskrankenkasse S-H");
        cInsComp.setInscShort("BKK");
        cInsComp.setStateEn(StateEn.st01);

        cWard = new CWard();
        cWard.setCHospital(cHospital);
        cWard.setWardShort(WARD_SHORT_NUMBER);
        Set<CWard> setOfWards = new HashSet<>(0);
        setOfWards.add(cWard);
        cHospital.setCWards(setOfWards);

        tCaseDetails = new TCaseDetails();
        tCaseDetails.setCsdAdmissionDate(new Date());
        //tCaseDetails.setCsdInsCompany(INS_COMP_NUMBER);
        tCaseDetails2 = new TCaseDetails();
        tCaseDetails2.setCsdAdmissionDate(new Date());
        //tCaseDetails2.setCsdInsCompany(INS_COMP_NUMBER);
        tCase.setCurrentLocal(tCaseDetails);
        tCase.setCurrentExtern(tCaseDetails2);

        tGroupingResults = new TGroupingResults();
        tGroupingResults.setCaseDetails(tCaseDetails);
        tGroupingResults.setModelIdEn(GDRGModel.AUTOMATIC);

        caseComment = new TCaseComment();
        caseComment.setActive(Boolean.TRUE);
        caseComment.setId(1L);
        caseComment.setTypeEn(CommentTypeEn.caseReview);
        caseComment.setnumber(2L);
        caseComment.setVersion(0L);
        caseComment.setTCaseId(tCase);
        Set<TCaseComment> caseComments = new HashSet<>(0);
        caseComments.add(caseComment);
        tCase.setCaseComments(caseComments);

//        SessionContext sessionContext = clientManager.getSessionContext();
        transactionManager.beginTransaction();

        patientDao.persist(tPatient);
        hospitalCaseDao.persist(tCase);
        hospDao.persist(cHospital);
        insuCompDao.persist(cInsComp);
        wardDao.persist(cWard);
        tCaseDetailsDao.persist(tCaseDetails);
        groupingResultDao.persist(tGroupingResults);
//        caseCommentDao.persist(caseComment);

        hospitalCaseDao.flush();
        patientDao.flush();
        hospDao.flush();
        insuCompDao.flush();
        wardDao.flush();
        tCaseDetailsDao.flush();
        groupingResultDao.flush();
//        caseCommentDao.flush();
    }

    private static String getCpxHome() {
        String tmp = System.getenv("CPX_HOME");
        return tmp == null ? "" : tmp.trim();
    }

    // Executed after each test. It is used to cleanup the test environment (e.g., delete temporary data, restore defaults). It can also save memory by cleaning up expensive memory structures.  
    @After
    public void tearDown() {
        if (transactionManager != null) {
            transactionManager.commitTransaction();
            transactionManager.close();
        }
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test.
    // Identifies a method as a test method.
    @Test
    public void generateXMLFileTest() {
        TCase hospitalCase = hospitalCaseDao.findCaseForCaseNumberAndIdent(CASE_NUMBER, HOSP_NUMBER);
        File generateCaseDataXML = xmlGenerator.generateCaseDataXML(hospitalCase, GDRGModel.AUTOMATIC);

        if (generateCaseDataXML != null) {
            assertNotNull("XML file isn't generated or it is null", generateCaseDataXML);
            assertTrue("The length of the XML file is zero", generateCaseDataXML.length() != 0);
            assertTrue("XML file isn't exist", generateCaseDataXML.exists());
            assertTrue(generateCaseDataXML.isFile());
            assertSame("File name ends with (suffix): xml", generateCaseDataXML.getName().endsWith("xml"), true);
            assertSame("File name starts with (prefix): tmpfile", generateCaseDataXML.getName().startsWith("tmpfile"), true);

            if (generateCaseDataXML.exists()) {
                String xmlFilePath = generateCaseDataXML.getAbsolutePath();
                File xmlFile = generateCaseDataXML.getAbsoluteFile();
                if (xmlFile != null) {
                    LOG.log(Level.INFO, null, "XML file is created");
                }
            } else {
                LOG.log(Level.INFO, null, "XML file is either null or doesn't exist");
            }
        }
    }
}
