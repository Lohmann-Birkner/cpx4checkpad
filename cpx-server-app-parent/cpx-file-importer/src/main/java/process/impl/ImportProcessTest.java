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
 *    2017  niemeier - initial API and implementation and/or initial documentation
 */
package process.impl;

import de.lb.cpx.cpx.license.crypter.License;
import de.lb.cpx.server.commons.enums.DbDriverEn;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * For internal use (developer test imports)
 *
 * @author niemeier
 */
public class ImportProcessTest {

    public static final int THREADCOUNT = 12; //This value is not used at the moment/2017-10-17

    public static final String CPX_COMMON_ORACLE = "CPX_COMMON_ORACLE";
    public static final String CPX_COMMON_SQLSRV = "CPX_COMMON_SQLSRV";
    public static final String DIRK_ORACLE = "DIRK_ORACLE";
    public static final String SPACKODATENBANK_ORACLE = "SPACKODATENBANK_ORACLE";
    public static final String P21_PAT_SQLSRV = "P21_PAT";
    public static final String AWI_ORACLE = "AWI_ORACLE";
    public static final String DIRK2_ORACLE = "DIRK2_ORACLE";
    public static final String DIRK3_ORACLE = "DIRK3_ORACLE";
    public static final String SAP_TEST_ESSEN_SQLSRV = "SAP_TEST_ESSEN_SQLSRV";
    public static final String DIRK_SQLSRV = "DIRK_SQLSRV";
    public static final String DIRK2_SQLSRV = "DIRK2_SQLSRV";
    public static final String DIRK3_SQLSRV = "DIRK3_SQLSRV";
    public static final String DIRK19_ORACLE = "DIRK19_ORACLE";
    public static final String DIRK20_ORACLE = "DIRK20_ORACLE";
    public static final String DIRK21_ORACLE = "DIRK21_ORACLE";
    public static final String DIRK22_ORACLE = "DIRK22_ORACLE";
    public static final String DIRK19_SQLSRV = "DIRK19_SQLSRV";
    public static final String DIRK22_SQLSRV = "DIRK22_SQLSRV";
    public static final String CPX05 = "CPX05";
    public static final String CPX_KURUMI = "CPX_KURUMI";
    public static final String DB_SH_ORACLE = "DB_SH_ORACLE";
    public static final String CPX_TEST_ORACLE = "CPX_TEST";
    public static final String CPX_HUGE_800000_ORACLE = "CPX_HUGE_800000";
    public static final String CPX_TEST_SQLSRV = "CPX_TEST";
    public static final String CPX_HUGE_800000_SQLSRV = "CPX_HUGE_800000";
    public static final String CPX_MEDIUM = "CPX_MEDIUM";
    public static final String CPX_BIG = "CPX_BIG";
    public static final String CPX_400000_SQLSRV = "CPX_BIG_400000";
    public static final String CPX_HUGE_SQLSRV = "CPX_HUGE";
    public static final String DB_PNA = "DB_PNA";
    public static final String CPX_DAK_8M_ORACLE = "CPX_DAK";
    public static final String CPX_P21IMPORT_TEST = "CPX_P21IMPORT_TEST";

    protected static final Map<String, DbConfigTest> TEST_DBS;

    static {
        TEST_DBS = new HashMap<>();
        TEST_DBS.put(CPX_COMMON_ORACLE, new DbConfigTest(DbDriverEn.ORACLE, "LBBatch-01", "1522", "ORCL12", "CPX_COMMON", "oracle"));
        TEST_DBS.put(CPX_COMMON_SQLSRV, new DbConfigTest(DbDriverEn.SQLSRV, "LBBatch-01", "1433", "CPX_COMMON", "sa", "sb"));
        TEST_DBS.put(DIRK_ORACLE, new DbConfigTest(DbDriverEn.ORACLE, "LBBatch-01", "1522", "ORCL12", "DIRK", "oracle"));
        TEST_DBS.put(CPX_P21IMPORT_TEST, new DbConfigTest(DbDriverEn.ORACLE, "LBBatch-01", "1522", "ORCL12", "CPX_P21IMPORT_TEST", "oracle"));
        TEST_DBS.put(AWI_ORACLE, new DbConfigTest(DbDriverEn.ORACLE, "LBBatch-01", "1522", "ORCL12", "WI_DB", "oracle"));
        TEST_DBS.put(DIRK2_ORACLE, new DbConfigTest(DbDriverEn.ORACLE, "LBBatch-01", "1522", "ORCL12", "DIRK2", "oracle"));
        TEST_DBS.put(DIRK3_ORACLE, new DbConfigTest(DbDriverEn.ORACLE, "LBBatch-01", "1522", "ORCL12", "DIRK3", "oracle"));
        TEST_DBS.put(DIRK19_ORACLE, new DbConfigTest(DbDriverEn.ORACLE, "LBBatch-01", "1522", "ORCL12", "DIRK19", "oracle"));
        TEST_DBS.put(DIRK20_ORACLE, new DbConfigTest(DbDriverEn.ORACLE, "LBBatch-01", "1522", "ORCL12", "DIRK20", "oracle"));
        TEST_DBS.put(DIRK21_ORACLE, new DbConfigTest(DbDriverEn.ORACLE, "LBBatch-01", "1522", "ORCL12", "DIRK21", "oracle"));
        TEST_DBS.put(DIRK22_ORACLE, new DbConfigTest(DbDriverEn.ORACLE, "LBBatch-01", "1522", "ORCL12", "DIRK22", "oracle"));
        TEST_DBS.put(DIRK22_ORACLE, new DbConfigTest(DbDriverEn.ORACLE, "LBBatch-01", "1522", "ORCL12", "DIRK22", "oracle"));
        TEST_DBS.put(CPX05, new DbConfigTest(DbDriverEn.ORACLE, "LBBatch-01", "1522", "ORCL12", "CPX5", "oracle"));
        TEST_DBS.put(CPX_KURUMI, new DbConfigTest(DbDriverEn.ORACLE, "LBBatch-01", "1522", "ORCL12", "CPX_KURUMI", "oracle"));
        TEST_DBS.put(P21_PAT_SQLSRV, new DbConfigTest(DbDriverEn.SQLSRV, "LBBatch-01", "1433", "P21_pat", "sa", "sb"));
        TEST_DBS.put(DIRK_SQLSRV, new DbConfigTest(DbDriverEn.SQLSRV, "LBBatch-01", "1433", "DIRK", "sa", "sb"));
        TEST_DBS.put(DIRK2_SQLSRV, new DbConfigTest(DbDriverEn.SQLSRV, "LBBatch-01", "1433", "DIRK2", "sa", "sb"));
        TEST_DBS.put(DIRK3_SQLSRV, new DbConfigTest(DbDriverEn.SQLSRV, "LBBatch-01", "1433", "DIRK3", "sa", "sb"));
        TEST_DBS.put(SAP_TEST_ESSEN_SQLSRV, new DbConfigTest(DbDriverEn.SQLSRV, "LBBatch-01", "1433", "SAP_TEST_ESSEN", "sa", "sb"));
        TEST_DBS.put(DIRK19_SQLSRV, new DbConfigTest(DbDriverEn.SQLSRV, "LBBatch-01", "1433", "DIRK19", "sa", "sb"));
        TEST_DBS.put(DIRK22_SQLSRV, new DbConfigTest(DbDriverEn.SQLSRV, "LBBatch-01", "1433", "DIRK22", "sa", "sb"));
//            TEST_DBS.put(HUGE_SQLSRV, new DbConfigTest("jdbc:sqlserver://lbbatch-01:1433;DATABASE=HUGE;sendStringParametersAsUnicode=false", "sa", "sb"));
        TEST_DBS.put(DB_SH_ORACLE, new DbConfigTest(DbDriverEn.ORACLE, "LBBatch-01", "1522", "ORCL12", "DB_SH", "oracle"));
        TEST_DBS.put(CPX_TEST_ORACLE, new DbConfigTest(DbDriverEn.ORACLE, "LBBatch-01", "1522", "ORCL12", "CPX_TEST", "oracle"));
        TEST_DBS.put(CPX_HUGE_800000_ORACLE, new DbConfigTest(DbDriverEn.ORACLE, "LBBatch-01", "1522", "ORCL12", "CPX_HUGE_800000", "oracle"));
        TEST_DBS.put(DB_PNA, new DbConfigTest(DbDriverEn.ORACLE, "LBBatch-01", "1522", "ORCL12", "DB_PNA", "oracle"));
//            TEST_DBS.put(CPX_DAK_8M_ORACLE, new DbConfigTest("jdbc:oracle:thin:@localhost:1521:ORCL", "CPX_DAK_8M", "oracle"));
        TEST_DBS.put(CPX_DAK_8M_ORACLE, new DbConfigTest(DbDriverEn.ORACLE, "cpx2018-02", "1521", "ORCL", "CPX_DAK", "oracle"));

        TEST_DBS.put(CPX_TEST_SQLSRV, new DbConfigTest(DbDriverEn.SQLSRV, "cpx2018-01", "1433", "CPX_TEST", "sa", "sb"));
//            put(CPX_HUGE_800000_SQLSRV, new DbConfigTest("jdbc:sqlserver://cpx2018-01:1433;DATABASE=CPX_HUGE_800000;sendStringParametersAsUnicode=false", "sa", "sb"));
        TEST_DBS.put(CPX_HUGE_800000_SQLSRV, new DbConfigTest(DbDriverEn.SQLSRV, "cpx2017-03", "1433", "CPX_HUGE_800000", "sa", "sb"));
        TEST_DBS.put(CPX_MEDIUM, new DbConfigTest(DbDriverEn.SQLSRV, "cpx2017-03", "1433", "CPX_MEDIUM", "sa", "sb"));
        TEST_DBS.put(CPX_BIG, new DbConfigTest(DbDriverEn.SQLSRV, "cpx2017-03", "1433", "CPX_BIG", "sa", "sb"));
        TEST_DBS.put(CPX_400000_SQLSRV, new DbConfigTest(DbDriverEn.SQLSRV, "cpx2018-01", "1433", "CPX_BIG_400000", "sa", "sb"));
        TEST_DBS.put(CPX_HUGE_SQLSRV, new DbConfigTest(DbDriverEn.SQLSRV, "cpx2017-03", "1433", "CPX_HUGE", "sa", "sb"));
        TEST_DBS.put(SPACKODATENBANK_ORACLE, new DbConfigTest(DbDriverEn.ORACLE, "LBBatch-01", "1522", "ORCL12", "SPACKODATENBANK", "oracle"));
//    TEST_DBS.put(CPX_HUGE_800000, new DbConfigTest("jdbc:sqlserver://CPX2018-01:1433;DATABASE=CPX_HUGE_800000;sendStringParametersAsUnicode=false", "sa", "sb"));
//            TEST_DBS.put(DB_PNA_ORACLE, new DbConfigTest("jdbc:oracle:thin:@LBBatch-01:1522:ORCL12", "DB_PNA", "oracle"));
    }

    /**
     * List of test databases
     *
     * @return test databases
     */
    public static Map<String, DbConfigTest> getTestDbs() {
        return new HashMap<>(TEST_DBS);
    }

//    /**
//     * SAMPLE METHOD TO TEST P21 IMPORT
//     *
//     * @author Dirk Niemeier
//     * @param args arguments
//     * @throws Exception Exception
//     */
//    public static void main(String[] args) throws Exception {
//        //*** HEY FELLOW, PLEASE INSERT YOUR TEST CONFIGURATION HERE ***//
//        LOG.log(Level.INFO, "You're starting import for developer's internal test use!");
//        final DbConfigTest commonDbConfig = TEST_DBS.get(CPX_COMMON); //CUSTOMIZE THIS!
//        final DbConfigTest caseDbConfig = TEST_DBS.get(DIRK_SQLSRV); //CUSTOMIZE THIS!
//        //final String inputDirectory = "E:\\test"; //CUSTOMIZE THIS! (it's faster when you use a SSD instead of HDD here!)
//        final String inputDirectory = INEK_SMALL_8; //CUSTOMIZE THIS! (it's faster when you use a SSD instead of HDD here!)
//        final String outputDirectory = P21_OUTPUT_DIRECTORY; //CUSTOMIZE THIS! (but should not be neccessary, it's faster when you use a SSD instead of HDD here!)
//        final ImportMode importMode = ImportMode.Version; //CUSTOMIZE THIS! (use 'version', 'overwrite' or 'truncate')
//        final ImportModuleI module = new Sample(inputDirectory, outputDirectory); //CUSTOMIZE THIS! (use P21, Fdse or Sample here)
//        final ImportConfig<? extends ImportModuleI> importConfig = new ImportConfig<>(String.valueOf(caseDbConfig), module, importMode);
//
//        LOG.log(Level.INFO, "Will import " + module.getName() + " files from: " + inputDirectory);
//        LOG.log(Level.INFO, "Will use this import mode: " + importMode.name);
//
//        //*** NOW LEAN BACK AND OBSERVE ***//
//        ImportProcessFile process = new ImportProcessFile();
//        process.start(importConfig, commonDbConfig, caseDbConfig);
//    }
    public static License getFullFeaturedTestLicense() throws IOException {
        return License.loadFromLicenseData(
                "4349465237316C58327569707A396A7547315474583752337A\n" +
"6C7451345732720A6A4C4B476430656B73646E377156747456\n" +
"4F4E496C34556478536836306C38360A6C6B704A5033726C55\n" +
"486636306E43512F7169574F4635786B4C4E45435371654131\n" +
"48646251724B587151447674775363464537366A746D432B48\n" +
"65482B5533523154554159774A74637565646A4F62476D5745\n" +
"466856697A5946724E79742F467A6D68747935665768394A59\n" +
"754835366A4F6E68413D3D0A594C794264624B613279503732\n" +
"3145755976506369413D3D0A7A5561384C52525A3572443143\n" +
"394961716236736F773D3D0A702B5A71534A4B6169656E7837\n" +
"53417548663561666F6D5363624E4D3632694B663767676464\n" +
"6C61584C423041467348645159424A513D3D0A31384F455763\n" +
"70357468454936712F344646684E45773D3D0A336559364138\n" +
"476661517251424B7345496A7747497A744759697A30315239\n" +
"73646F6151737246583364773D0A34614158515A3542644E55\n" +
"5770534B557050436254413D3D0A"
        );
    }

}
