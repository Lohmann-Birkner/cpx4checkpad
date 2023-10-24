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
 *    2017  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package distributor.impl;

import de.lb.cpx.cpx.license.crypter.License;
import de.lb.cpx.shared.dto.job.config.CpxJobConstraints;
import de.lb.cpx.shared.dto.job.config.CpxJobImportConfig;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.str.utils.StrUtils;
import distributor.CpxDistributorI;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.ImportModuleI;
import module.impl.ImportConfig;
import org.apache.commons.lang3.StringUtils;
import progressor.ProgressorI;
import util.DbBuilder;

/**
 *
 * @author Dirk Niemeier
 * @param <T> config type
 */
public class CpxDistributor<T extends ImportModuleI<? extends CpxJobImportConfig>> implements CpxDistributorI<T> {

    static final Logger LOG = Logger.getLogger(CpxDistributor.class.getName());
    long mMeanTime = System.currentTimeMillis();
    final long mStartupTime = mMeanTime;
    private ImportConfig<T> mImportConfig = null;
    final static boolean DO_CREATE_INDEXES = false;
    //final static boolean DO_REFRESH_INDEXES = true;
    private static final String BILLING_DATE_SQL = "UPDATE /*+ PARALLEL(T_CASE 5)*/ T_CASE SET CS_BILLING_DATE = (SELECT MIN(INVOICE_DATE) FROM T_SAP_FI_BILL WHERE EXISTS (SELECT 1 FROM T_SAP_FI_BILLPOSITION WHERE T_SAP_FI_BILL.ID=T_SAP_FI_BILLPOSITION.T_SAP_FI_BILL_ID AND T_SAP_FI_BILLPOSITION.REFERENCE_ID LIKE 'ZDRG%') AND T_CASE.ID=T_SAP_FI_BILL.T_CASE_ID) ";
    private static final String CASE_DETAILS_CANCEL_REASON_KIS = "2"; //CASE_DETAILS_CANCEL_REASON_EN 1 = MERGE, 2 = KIS
    private final ProgressorI mProgressor;
    private long jobUserId;
    private boolean isP21Import;

    public CpxDistributor(final ProgressorI pProgressor) {
        mProgressor = pProgressor;
        if (pProgressor == null) {
            throw new IllegalArgumentException("Progressor cannot be null!");
        }
    }

    @Override
    public ProgressorI getProgressor() {
        return mProgressor;
    }

    public static String getBillingDateSql() {
        return getBillingDateSql(null);
    }

    public static String getBillingDateSql(final Long pCaseId) {
        String sql = BILLING_DATE_SQL;
        if (pCaseId != null && !pCaseId.equals(0L)) {
            sql += " WHERE T_CASE.ID = " + pCaseId;
        }
        return sql;
    }

    @Override
    public void distributeData(final ImportConfig<T> pImportConfig, final Connection pCommonDbConnection, final Connection pCaseDbConnection, final boolean pLogQueries) throws IllegalArgumentException, IllegalAccessException, SQLException, IOException, ParseException {
        //List<LineEntity> lineEntities = AbstractLine.getLineEntities();
        mMeanTime = System.currentTimeMillis();
        mImportConfig = pImportConfig;
        String[][] indexNames = new String[][]{
            new String[]{"IDX_T_PATIENT_DETAILS_PAT_ID", "T_PATIENT_DETAILS", "T_PATIENT_ID"},
            new String[]{"IDX_T_INSURANCE_PAT_ID", "T_INSURANCE", "T_PATIENT_ID"},
            new String[]{"IDX_T_CASE_DETAILS_HOSC_ID", "T_CASE_DETAILS", "T_CASE_ID"},
            new String[]{"IDX_T_CASE_DEP_HOSD_ID", "T_CASE_DEPARTMENT", "T_CASE_DETAILS_ID"},
            new String[]{"IDX_T_CASE_WARD_DEPC_ID", "T_CASE_WARD", "T_CASE_DEPARTMENT_ID"},
            new String[]{"IDX_T_CASE_FEE_HOSD_ID", "T_CASE_FEE", "T_CASE_DETAILS_ID"},
            new String[]{"IDX_T_CASE_OPS_DEPC_ID", "T_CASE_OPS", "T_CASE_DEPARTMENT_ID"},
            new String[]{"IDX_T_CASE_ICD_DEPC_ID", "T_CASE_ICD", "T_CASE_DEPARTMENT_ID"},
            new String[]{"IDX_T_CASE_ICD_REF_ID", "T_CASE_ICD", "T_CASE_ICD_ID"},
            new String[]{"IDX_T_CASE_DETAILS_EXTERN_ID", "T_CASE_DETAILS", "EXTERN_ID"},
            new String[]{"IDX_T_CASE_DETAILS_PARENT_ID", "T_CASE_DETAILS", "PARENT_ID"},
            //new String[] { "IDX_T_CASE_DETAILS_ICD_HDX", "T_CASE_DETAILS", "ID_ICD_HDX" },
            new String[]{"IDX_GROUPING_RESULTS_HOSD_ID", "T_GROUPING_RESULTS", "T_CASE_DETAILS_ID"},
            new String[]{"IDX_GROUPING_RESULTS_ICD_ID", "T_GROUPING_RESULTS", "T_CASE_ICD_ID"},
            new String[]{"IDX_CASE_CASE_NUMBER", "T_CASE", "CS_CASE_NUMBER"},
            new String[]{"IDX_CASE_PATIENT_ID", "T_CASE", "T_PATIENT_ID"},};

        try (Statement caseDbStmt = pCaseDbConnection.createStatement()) {
            try (Statement commonDbStmt = pCommonDbConnection.createStatement()) {
                final Query commonDbQry = new Query(commonDbStmt, pLogQueries);
                final Query caseDbQry = new Query(caseDbStmt, pLogQueries);
                jobUserId = getJobUserId(commonDbQry);

                getProgressor().sendProgress("Lösche alte IMEX-Tabellen und IMEX-Views");
                printTime("Drop all imex tables & views...");
                dropImexTables(caseDbQry);
                dropImexViews(caseDbQry);
                printTime("All imex tables & views were dropped!");

                getProgressor().sendProgress("Erzeuge Indizes");
                if (DO_CREATE_INDEXES) {
                    printTime("Create indexes...");
                    createIndexes(caseDbQry, indexNames);
                    printTime("Indexes created!");
                }

                getProgressor().sendProgress("Lösche Fälle");
                if (pImportConfig.isOverwriteCases()) {
                    printTime("Drop (overwrite) existing cases...");
                    dropCases(caseDbQry);
                    printTime("Existing cases dropped (will be overwritten)!");
                } else {
                    //
                }

                //qry.executeUpdate("CREATE INDEX IDX_IMEX_DEP_TMP_VERLDATUM ON IMEX_DEPARTMENT_TMP (VERLEGUNGSDATUM) " + parallel());
                //qry.executeUpdate("CREATE INDEX IDX_IMEX_DEP_TMP_ENTLDATUM ON IMEX_DEPARTMENT_TMP (ENTLASSUNGSDATUM) " + parallel());
                printTime("Create IMEX_PATIENT...");

                dropTable(caseDbQry, "IMEX_PATIENT");

                getProgressor().sendProgress("Erzeuge IMEX_PATIENT");
                caseDbQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_PATIENT NOLOGGING PARALLEL 5 AS " : "")
                        + " SELECT "
                        + "   TMP2.*, "
                        + "   CASE WHEN OLD_PATIENT_ID IS NOT NULL THEN OLD_PATIENT_ID ELSE NULL END NEW_PATIENT_ID, "
                        + "   ACT_INS.ID ACT_INS_ID "
                        + (isOracle() ? "" : " INTO IMEX_PATIENT ")
                        + " FROM ( "
                        + "   SELECT "
                        + "     TMP.*, "
                        + "     ACT_PAT_DETAILS.ID ACT_PAT_DETAILS_ID "
                        + "   FROM ( "
                        + "     SELECT IMEX_PATIENT_TMP.*, T_PATIENT.ID OLD_PATIENT_ID, CASE WHEN T_PATIENT.ID IS NULL THEN 1 ELSE 0 END IS_NEW_PATIENT "
                        + "     FROM IMEX_PATIENT_TMP "
                        + "     LEFT JOIN T_PATIENT ON T_PATIENT.PAT_NUMBER = IMEX_PATIENT_TMP.PATNR "
                        + "   ) TMP "
                        + "   LEFT JOIN T_PATIENT_DETAILS ACT_PAT_DETAILS ON ACT_PAT_DETAILS.T_PATIENT_ID = OLD_PATIENT_ID AND ACT_PAT_DETAILS.PATD_IS_ACTUAL_FL = 1 "
                        + " ) TMP2 "
                        + " LEFT JOIN T_INSURANCE ACT_INS ON ACT_INS.T_PATIENT_ID = OLD_PATIENT_ID AND ACT_INS.INS_IS_ACTUAL_FL = 1 ");
                caseDbQry.executeUpdate("UPDATE IMEX_PATIENT SET NEW_PATIENT_ID = " + nextSqVal("T_PATIENT_SQ") + " WHERE NEW_PATIENT_ID IS NULL");

                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_PATIENT_PATNR ON IMEX_PATIENT (PATNR) " + parallel());
                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_PATIENT_IS_NEW_PATIEN ON IMEX_PATIENT (IS_NEW_PATIENT) " + parallel());
                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_PATIENT_PATIENT_ID1 ON IMEX_PATIENT (OLD_PATIENT_ID) " + parallel());
                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_PATIENT_PATIENT_ID2 ON IMEX_PATIENT (NEW_PATIENT_ID) " + parallel());
                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_PATIENT_PATIENT_DT_ID ON IMEX_PATIENT (ACT_PAT_DETAILS_ID) " + parallel());
                caseDbQry.executeUpdate(DbBuilder.getImexPkDefinition("IMEX_PATIENT"));

                printTime("IMEX_PATIENT created!");

                printTime("Create IMEX_MAIN_ICD...");

                dropTable(caseDbQry, "IMEX_MAIN_ICD");

                getProgressor().sendProgress("Erzeuge IMEX_MAIN_ICD");
                caseDbQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_MAIN_ICD NOLOGGING PARALLEL 5 AS " : "")
                        + "  SELECT IMEX_DIAGNOSE_TMP.IKZ, IMEX_DIAGNOSE_TMP.FALLNR, "
                        + "         IMEX_DIAGNOSE_TMP.CODE, IMEX_DIAGNOSE_TMP.REF_ICD_TYPE, "
                        + "         IMEX_DIAGNOSE_TMP.LOKALISATION, IMEX_DIAGNOSE_TMP.NR "
                        + (isOracle() ? "" : " INTO IMEX_MAIN_ICD ")
                        + "  FROM IMEX_DIAGNOSE_TMP "
                        + "  INNER JOIN IMEX_DEPARTMENT_TMP ON IMEX_DEPARTMENT_TMP.NR = IMEX_DIAGNOSE_TMP.DEP_NR "
                        //+ "  WHERE IMEX_DEPARTMENT_TMP.ERBRINGUNGSART LIKE 'H%' "
                        + "  WHERE IMEX_DIAGNOSE_TMP.HDX = 1 "
                        + "    AND IMEX_DIAGNOSE_TMP.NR = (SELECT MIN(TMP.NR) FROM IMEX_DIAGNOSE_TMP TMP WHERE TMP.IKZ = IMEX_DIAGNOSE_TMP.IKZ AND TMP.FALLNR = IMEX_DIAGNOSE_TMP.FALLNR AND TMP.CODE = IMEX_DIAGNOSE_TMP.CODE AND TMP.HDX = IMEX_DIAGNOSE_TMP.HDX)"); //ignore duplicate icd codes with hdx=1 (only consider the first one)
                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_MAIN_ICD_FALL ON IMEX_MAIN_ICD (IKZ, FALLNR) " + parallel());
                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_MAIN_ICD_NR ON IMEX_MAIN_ICD (NR) " + parallel());
                caseDbQry.executeUpdate(DbBuilder.getImexPkDefinition("IMEX_MAIN_ICD"));

                printTime("IMEX_MAIN_ICD created!");

                printTime("Create IMEX_DIAGNOSE...");

                dropTable(caseDbQry, "IMEX_DIAGNOSE");

                //remove duplicated main icds and correct references for secondary diagnosis
                getProgressor().sendProgress("Erzeuge IMEX_DIAGNOSE");
                caseDbQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_DIAGNOSE NOLOGGING PARALLEL 5 AS " : "")
                        + "  SELECT "
                        //  + "IMEX_CASE.NEW_CASE_ID, "
                        //  + "IMEX_CASE.NEW_PATIENT_ID, "
                        + "IMEX_DIAGNOSE_TMP.NR, "
                        + "IMEX_DIAGNOSE_TMP.TP_SOURCE, "
                        + "IMEX_DIAGNOSE_TMP.TP_ID, "
                        + "IMEX_DIAGNOSE_TMP.IKZ, "
                        + "IMEX_DIAGNOSE_TMP.FALLNR, "
                        + "IMEX_DIAGNOSE_TMP.PATNR, "
                        + "IMEX_DIAGNOSE_TMP.CODE, "
                        + "IMEX_DIAGNOSE_TMP.DEP_NR, "
                        + "IMEX_DIAGNOSE_TMP.WARD_NR, "
                        + "IMEX_DIAGNOSE_TMP.ICD_TYPE, "
                        + "(CASE WHEN IMEX_DIAGNOSE_TMP.REF_ICD_NR IS NOT NULL AND IMEX_MAIN_ICD_REF.NR IS NULL AND IMEX_MAIN_ICD_REF2.NR IS NOT NULL THEN IMEX_MAIN_ICD_REF2.NR ELSE IMEX_DIAGNOSE_TMP.REF_ICD_NR END) REF_ICD_NR, "
                        + "IMEX_DIAGNOSE_TMP.REF_ICD_TYPE, "
                        + "IMEX_DIAGNOSE_TMP.LOKALISATION, "
                        + "IMEX_DIAGNOSE_TMP.TO_GROUP, "
                        + "IMEX_DIAGNOSE_TMP.HDB, "
                        + "(CASE WHEN IMEX_MAIN_ICD.NR IS NULL THEN 0 ELSE IMEX_DIAGNOSE_TMP.HDX END) HDX "
                        + (isOracle() ? "" : " INTO IMEX_DIAGNOSE ")
                        + "FROM IMEX_DIAGNOSE_TMP "
                        // + "  INNER JOIN IMEX_CASE ON IMEX_CASE.IKZ = IMEX_DIAGNOSE_TMP.IKZ AND IMEX_CASE.FALLNR = IMEX_DIAGNOSE_TMP.FALLNR "
                        + "LEFT JOIN IMEX_MAIN_ICD ON IMEX_MAIN_ICD.NR = IMEX_DIAGNOSE_TMP.NR "
                        + "LEFT JOIN IMEX_MAIN_ICD IMEX_MAIN_ICD_REF ON IMEX_MAIN_ICD_REF.NR = IMEX_DIAGNOSE_TMP.REF_ICD_NR "
                        + "LEFT JOIN IMEX_DIAGNOSE_TMP TMP ON TMP.NR = IMEX_DIAGNOSE_TMP.REF_ICD_NR "
                        + "LEFT JOIN IMEX_MAIN_ICD IMEX_MAIN_ICD_REF2 ON IMEX_MAIN_ICD_REF2.CODE = TMP.CODE AND " + isNull2("IMEX_MAIN_ICD_REF2.REF_ICD_TYPE", "0") + " = " + isNull2("TMP.REF_ICD_TYPE", "0") + " AND " + isNull2("IMEX_MAIN_ICD_REF2.LOKALISATION", "0") + " = " + isNull2("TMP.LOKALISATION", "0") + " AND IMEX_MAIN_ICD_REF2.IKZ = TMP.IKZ AND IMEX_MAIN_ICD_REF2.FALLNR = TMP.FALLNR "
                        + "WHERE (IMEX_MAIN_ICD.NR IS NOT NULL OR IMEX_DIAGNOSE_TMP.HDX = 0)");
                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_DIAGNOSE_FALL ON IMEX_DIAGNOSE (IKZ, FALLNR) " + parallel());
                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_DIAGNOSE_NR ON IMEX_DIAGNOSE (NR) " + parallel());
                caseDbQry.executeUpdate(DbBuilder.getImexPkDefinition("IMEX_DIAGNOSE"));

                printTime("IMEX_DIAGNOSE created!");

                printTime("Create IMEX_SUM_OP_ND...");

                dropTable(caseDbQry, "IMEX_SUM_OP_ND");

                getProgressor().sendProgress("Erzeuge IMEX_SUM_OP_ND");
                caseDbQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_SUM_OP_ND NOLOGGING PARALLEL 5 AS " : "")
                        + "  SELECT "
                        + "    TMP.*, "
                        + "    COUNT(IMEX_DIAGNOSE.NR) SUM_OF_ICD "
                        + (isOracle() ? "" : " INTO IMEX_SUM_OP_ND ")
                        + "  FROM ( "
                        + "    SELECT IMEX_CASE_TMP.NR, IMEX_CASE_TMP.IKZ, IMEX_CASE_TMP.FALLNR, COUNT(IMEX_PROCEDURE_TMP.NR) SUM_OF_OPS "
                        + "    FROM IMEX_CASE_TMP "
                        + "    LEFT JOIN IMEX_PROCEDURE_TMP ON (IMEX_PROCEDURE_TMP.IKZ = IMEX_CASE_TMP.IKZ AND IMEX_PROCEDURE_TMP.FALLNR = IMEX_CASE_TMP.FALLNR) "
                        + "    GROUP BY IMEX_CASE_TMP.NR, IMEX_CASE_TMP.IKZ, IMEX_CASE_TMP.FALLNR "
                        + "  ) TMP "
                        + "  LEFT JOIN IMEX_DIAGNOSE ON (IMEX_DIAGNOSE.IKZ = TMP.IKZ AND IMEX_DIAGNOSE.FALLNR = TMP.FALLNR AND IMEX_DIAGNOSE.HDX = 0) "
                        + "  GROUP BY TMP.NR, TMP.IKZ, TMP.FALLNR, TMP.SUM_OF_OPS");

                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_SUM_OP_ND_FALL ON IMEX_SUM_OP_ND (IKZ, FALLNR) " + parallel());
                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_SUM_OP_ND_NR ON IMEX_SUM_OP_ND (NR) " + parallel());
                caseDbQry.executeUpdate(DbBuilder.getImexPkDefinition("IMEX_SUM_OP_ND"));

                printTime("IMEX_SUM_OP_ND created!");

                printTime("Create IMEX_PROCEDURE_DEP...");

                dropTable(caseDbQry, "IMEX_PROCEDURE_DEP");

                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_PROC_TMP_DATUM ON IMEX_PROCEDURE_TMP (DATUM) " + parallel());

                getProgressor().sendProgress("Erzeuge IMEX_PROCEDURE_DEP");
                caseDbQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_PROCEDURE_DEP NOLOGGING PARALLEL 5 AS " : "")
                        + "SELECT "
                        + "   TMP2.NR, TMP2.IKZ, TMP2.FALLNR, TMP2.DATUM, TMP2.VERLEGUNGSDATUM, TMP2.CODE, "
                        + "   CASE WHEN (DEPARTMENT_NR2 IS NOT NULL AND DEPARTMENT_NR2 > 0) THEN DEPARTMENT_NR2 ELSE (SELECT MIN(NR) FROM IMEX_DEPARTMENT_TMP WHERE IMEX_DEPARTMENT_TMP.IKZ = TMP2.IKZ AND IMEX_DEPARTMENT_TMP.FALLNR = TMP2.FALLNR) END DEP_NR "
                        + (isOracle() ? "" : " INTO IMEX_PROCEDURE_DEP ")
                        + "FROM ("
                        + "  SELECT "
                        + "     TMP.NR, TMP.IKZ, TMP.FALLNR, TMP.DATUM, TMP.VERLEGUNGSDATUM, TMP.CODE, "
                        + "     CASE WHEN (DEPARTMENT_NR IS NOT NULL AND DEPARTMENT_NR > 0) THEN DEPARTMENT_NR ELSE (SELECT MAX(NR) FROM IMEX_DEPARTMENT_TMP WHERE IMEX_DEPARTMENT_TMP.IKZ = TMP.IKZ AND IMEX_DEPARTMENT_TMP.FALLNR = TMP.FALLNR AND IMEX_DEPARTMENT_TMP.VERLEGUNGSDATUM = TMP.VERLEGUNGSDATUM) END DEPARTMENT_NR2"
                        + "   FROM ("
                        + "     SELECT IMEX_PROCEDURE_TMP.NR, IMEX_PROCEDURE_TMP.IKZ, IMEX_PROCEDURE_TMP.FALLNR, IMEX_PROCEDURE_TMP.DEP_NR DEPARTMENT_NR, IMEX_PROCEDURE_TMP.DATUM, IMEX_PROCEDURE_TMP.CODE, MAX(IMEX_DEPARTMENT_TMP.VERLEGUNGSDATUM) VERLEGUNGSDATUM "
                        + "     FROM IMEX_PROCEDURE_TMP "
                        + "     LEFT JOIN IMEX_DEPARTMENT_TMP ON IMEX_DEPARTMENT_TMP.IKZ = IMEX_PROCEDURE_TMP.IKZ AND IMEX_DEPARTMENT_TMP.FALLNR = IMEX_PROCEDURE_TMP.FALLNR AND IMEX_DEPARTMENT_TMP.VERLEGUNGSDATUM <= IMEX_PROCEDURE_TMP.DATUM "
                        + "     GROUP BY IMEX_PROCEDURE_TMP.NR, IMEX_PROCEDURE_TMP.IKZ, IMEX_PROCEDURE_TMP.FALLNR, IMEX_PROCEDURE_TMP.DEP_NR, IMEX_PROCEDURE_TMP.DATUM, IMEX_PROCEDURE_TMP.CODE "
                        + "   ) TMP "
                        + " ) TMP2");

                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_PROC_DEP_FALL ON IMEX_PROCEDURE_DEP (IKZ, FALLNR) " + parallel());
                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_PROC_DEP_DEP ON IMEX_PROCEDURE_DEP (DEP_NR) " + parallel());
                //caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_PROC_DEP_PATNR ON IMEX_PROCEDURE_DEP (PATNR) " + parallel());
                //qry.executeUpdate("CREATE INDEX IDX_IMEX_PROCEDURE_IS_NEW_CASE ON IMEX_PROCEDURE (IS_NEW_CASE)");
                //caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_PROCEDURE_FALL_ID1 ON IMEX_PROCEDURE (NEW_CASE_ID) " + parallel());
                caseDbQry.executeUpdate(DbBuilder.getImexPkDefinition("IMEX_PROCEDURE_DEP"));

                printTime("IMEX_PROCEDURE_DEP created!");

                generateAdmissionMode(caseDbQry);

                printTime("Create IMEX_DEPARTMENT...");

                dropTable(caseDbQry, "IMEX_DEPARTMENT");

                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_DEP_TMP_ENTLDATUM ON IMEX_DEPARTMENT_TMP (ENTLASSUNGSDATUM) " + parallel());
                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_DEP_TMP_VERLDATUM ON IMEX_DEPARTMENT_TMP (VERLEGUNGSDATUM) " + parallel());

                getProgressor().sendProgress("Erzeuge IMEX_DEPARTMENT");
                caseDbQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_DEPARTMENT NOLOGGING PARALLEL 5 AS " : "")
                        + "SELECT "
                        + "   TMP2.*, "
                        + "   (SELECT MIN(VERLEGUNGSDATUM) FROM IMEX_DEPARTMENT_TMP DEP WHERE DEP.IS_HOSPITAL = 0 AND DEP.IS_PSEUDO = 0 AND DEP.ERBRINGUNGSART_INT = TMP2.VERLGRUND_BEHANDELNDE_FAB AND DEP.DAUER = TMP2.DAUER_BEHANDELNDE_FAB AND DEP.IKZ = TMP2.IKZ AND DEP.FALLNR = TMP2.FALLNR) VERLDATUM_BEHANDELNDE_FAB "
                        + (isOracle() ? "" : " INTO IMEX_DEPARTMENT ")
                        + "   FROM ( "
                        + "     SELECT "
                        + "       TMP.*, "
                        // + "      (SELECT MIN(VERLEGUNGSDATUM) FROM IMEX_DEPARTMENT_TMP DEP WHERE DEP.IS_HOSPITAL = 0 AND DEP.IS_PSEUDO = 0 AND DEP.DAUER = TMP.DAUER_BEHANDELNDE_FAB AND DEP.IKZ = TMP.IKZ AND DEP.FALLNR = TMP.FALLNR) VERLDATUM_BEHANDELNDE_FAB "
                        + "       (SELECT MIN(ERBRINGUNGSART_INT) FROM IMEX_DEPARTMENT_TMP DEP WHERE DEP.IS_HOSPITAL = 0 AND DEP.IS_PSEUDO = 0 AND DEP.DAUER = TMP.DAUER_BEHANDELNDE_FAB AND DEP.IKZ = TMP.IKZ AND DEP.FALLNR = TMP.FALLNR) VERLGRUND_BEHANDELNDE_FAB "
                        + "     FROM ( "
                        + "       SELECT "
                        + "         IMEX_DEPARTMENT_TMP.*, "
                        //          + "         (CASE "
                        //          + "           WHEN ERBRINGUNGSART = 'HA' THEN 1 "
                        //          + "           WHEN ERBRINGUNGSART = 'HaBh' THEN 2 "
                        //          + "           WHEN ERBRINGUNGSART = 'Bo' THEN 3 "
                        //          + "           WHEN ERBRINGUNGSART = 'BoBa' THEN 4 "
                        //          + "           WHEN ERBRINGUNGSART = 'BoBh' THEN 5 "
                        //          + "           WHEN ERBRINGUNGSART = 'BoBaBh' THEN 6 "
                        //          + "           WHEN ERBRINGUNGSART = 'HaBha' THEN 7 "
                        //          + "           ELSE -1 "
                        //          + "         END) ERBRINGUNGSART_INT, "
                        + "       (SELECT MIN(VERLEGUNGSDATUM) FROM IMEX_DEPARTMENT_TMP DEP WHERE DEP.IKZ = IMEX_DEPARTMENT_TMP.IKZ AND DEP.FALLNR = IMEX_DEPARTMENT_TMP.FALLNR) VERLDATUM_AUFNEHMENDE_FAB, "
                        + "       (SELECT MAX(VERLEGUNGSDATUM) FROM IMEX_DEPARTMENT_TMP DEP WHERE DEP.IKZ = IMEX_DEPARTMENT_TMP.IKZ AND DEP.FALLNR = IMEX_DEPARTMENT_TMP.FALLNR) VERLDATUM_ENTLASSENDE_FAB, "
                        + "       (SELECT MAX(DAUER) FROM IMEX_DEPARTMENT_TMP DEP WHERE DEP.IS_HOSPITAL = 0 AND DEP.IS_PSEUDO = 0 AND DEP.IKZ = IMEX_DEPARTMENT_TMP.IKZ AND DEP.FALLNR = IMEX_DEPARTMENT_TMP.FALLNR) DAUER_BEHANDELNDE_FAB "
                        + "     FROM IMEX_DEPARTMENT_TMP "
                        + "   ) TMP "
                        + " ) TMP2 "
                        + " ORDER BY TMP2.VERLEGUNGSDATUM, TMP2.ENTLASSUNGSDATUM, TMP2.CODE");

                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_DEP_ENTLDATUM ON IMEX_DEPARTMENT (ENTLASSUNGSDATUM) " + parallel());
                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_DEP_VERLDATUM ON IMEX_DEPARTMENT (VERLEGUNGSDATUM) " + parallel());
                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_DEP_FALL ON IMEX_DEPARTMENT (IKZ, FALLNR) " + parallel());
                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_DEP_PATNR ON IMEX_DEPARTMENT (PATNR) " + parallel());
                //qry.executeUpdate("CREATE INDEX IDX_IMEX_DEPARTMENT_IS_NEW_DEPARTMENT ON IMEX_DEPARTMENT (IS_NEW_DEPARTMENT)");
                caseDbQry.executeUpdate(DbBuilder.getImexPkDefinition("IMEX_DEPARTMENT"));

                printTime("IMEX_DEPARTMENT created!");

                printTime("Analyze Departments...");
                createImexBehandelndeFab(caseDbQry);
                createImexAufnehmendeFab(caseDbQry);
                createImexEntlassendeFab(caseDbQry);
                printTime("Departments analyzed!");

                printTime("Create IMEX_CASE...");

                dropTable(caseDbQry, "IMEX_CASE");

                getProgressor().sendProgress("Erzeuge IMEX_CASE");
                caseDbQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_CASE NOLOGGING PARALLEL 5 AS " : "")
                        + "SELECT "
                        + "   TMP2.*, "
                        + "   CASE WHEN OLD_CASE_ID IS NOT NULL THEN OLD_CASE_ID ELSE NULL END NEW_CASE_ID, "
//                        + "   ACT_HOSD_LOCAL.ID ACT_HOSD_LOCAL_ID, "
                        + "   ACT_HOSD_LOCAL.T_CASE_DETAILS_ID ACT_HOSD_LOCAL_ID, "
                        + "   " + isNull("ACT_HOSD_LOCAL.VERSION_NUMBER", "0") + " + 1 NEW_LOCAL_CSD_VERSION"
                        + (isOracle() ? "" : " INTO IMEX_CASE ")
//                        + " FROM ("
//                        + "   SELECT "
//                        + "     TMP.*, "
//                        + "     ACT_HOSD_EXTERN.T_CASE_DETAILS_ID ACT_HOSD_EXTERN_ID, "
////                        + "     ACT_HOSD_EXTERN.ID ACT_HOSD_EXTERN_ID, "
//                        + "     " + isNull("ACT_HOSD_EXTERN.VERSION_NUMBER", "0") + " + 1 NEW_EXTERN_CSD_VERSION, "
//                        + "     CASE "
//                        + "       WHEN ACT_STORNIERT = 0     AND STORNIERT = 0     THEN 0 "
//                        + "       WHEN ACT_STORNIERT = 0     AND STORNIERT = 1     THEN 1 "
//                        + "       WHEN ACT_STORNIERT = 0     AND STORNIERT IS NULL THEN 0 "
//                        + "       WHEN ACT_STORNIERT = 1     AND STORNIERT = 0     THEN 0 "
//                        + "       WHEN ACT_STORNIERT = 1     AND STORNIERT = 1     THEN 1 "
//                        + "       WHEN ACT_STORNIERT = 1     AND STORNIERT IS NULL THEN 1 "
//                        + "       WHEN ACT_STORNIERT IS NULL AND STORNIERT = 0     THEN 0 "
//                        + "       WHEN ACT_STORNIERT IS NULL AND STORNIERT = 1     THEN NULL "
//                        + "       WHEN ACT_STORNIERT IS NULL AND STORNIERT IS NULL THEN 0 "
//                        + "     END NEW_STORNIERT "
                        //                        + "     CASE "
                        //                        + "       WHEN ACT_STORNIERT = 0     AND STORNIERT = 0     THEN 1 "
                        //                        + "       WHEN ACT_STORNIERT = 0     AND STORNIERT = 1     THEN 1 "
                        //                        + "       WHEN ACT_STORNIERT = 0     AND STORNIERT IS NULL THEN 1 "
                        //                        + "       WHEN ACT_STORNIERT = 1     AND STORNIERT = 0     THEN 1 "
                        //                        + "       WHEN ACT_STORNIERT = 1     AND STORNIERT = 1     THEN 0 "
                        //                        + "       WHEN ACT_STORNIERT = 1     AND STORNIERT IS NULL THEN 0 "
                        //                        + "       WHEN ACT_STORNIERT IS NULL AND STORNIERT = 0     THEN 1 "
                        //                        + "       WHEN ACT_STORNIERT IS NULL AND STORNIERT = 1     THEN 0 "
                        //                        + "       WHEN ACT_STORNIERT IS NULL AND STORNIERT IS NULL THEN 1 "
                        //                        + "     END NEW_STORNIERT_IMPORT "
//                        + "   FROM ( "
//                        + "     SELECT IMEX_CASE_TMP.*, T_CASE.ID OLD_CASE_ID, CASE WHEN T_CASE.ID IS NULL THEN 1 ELSE 0 END IS_NEW_CASE, "
//                        + "            IMEX_MAIN_ICD.CODE MAIN_ICD_CODE, IMEX_MAIN_ICD.NR MAIN_ICD_NR, "
//                        + "            IMEX_SUM_OP_ND.SUM_OF_OPS, IMEX_SUM_OP_ND.SUM_OF_ICD, IMEX_PATIENT.NEW_PATIENT_ID, "
//                        + "            CASE WHEN IMEX_BEHANDELNDE_FAB.ERBRINGUNGSART IS NULL THEN 'NR' ELSE IMEX_BEHANDELNDE_FAB.ERBRINGUNGSART END ERBRINGUNGSART,  "
//                        + "            T_CASE.CANCEL_FL ACT_STORNIERT, "
//                        + "            CASE WHEN IMEX_CASE_TMP.GESCHLECHT IS NULL THEN IMEX_PATIENT.GESCHLECHT ELSE IMEX_CASE_TMP.GESCHLECHT END NEW_GESCHLECHT, "
//                        + "            CASE WHEN IMEX_CASE_TMP.FALLSTATUS IS NULL THEN T_CASE.CS_STATUS_EN ELSE IMEX_CASE_TMP.FALLSTATUS END NEW_FALLSTATUS, "
//                        + "            CASE WHEN IMEX_CASE_TMP.FALLSTATUS IS NULL THEN 0 ELSE 1 END KIS_STATUS_FL "
//                        + "     FROM IMEX_CASE_TMP "
//                        + "     INNER JOIN IMEX_PATIENT ON IMEX_PATIENT.PATNR = IMEX_CASE_TMP.PATNR "
//                        + "     LEFT JOIN T_CASE ON IMEX_CASE_TMP.IKZ = T_CASE.CS_HOSPITAL_IDENT AND IMEX_CASE_TMP.FALLNR = T_CASE.CS_CASE_NUMBER "
//                        + "     LEFT JOIN IMEX_MAIN_ICD ON IMEX_CASE_TMP.IKZ = IMEX_MAIN_ICD.IKZ AND IMEX_CASE_TMP.FALLNR = IMEX_MAIN_ICD.FALLNR "
//                        + "     LEFT JOIN IMEX_SUM_OP_ND ON IMEX_CASE_TMP.IKZ = IMEX_SUM_OP_ND.IKZ AND IMEX_CASE_TMP.FALLNR = IMEX_SUM_OP_ND.FALLNR "
//                        + "     LEFT JOIN IMEX_BEHANDELNDE_FAB ON IMEX_CASE_TMP.IKZ = IMEX_BEHANDELNDE_FAB.IKZ AND IMEX_CASE_TMP.FALLNR = IMEX_BEHANDELNDE_FAB.FALLNR "
//                        + "   ) TMP "
//                        + " LEFT JOIN T_CASE_DETAILS ACT_HOSD_EXTERN ON ACT_HOSD_EXTERN.T_CASE_ID = OLD_CASE_ID AND ACT_HOSD_EXTERN.ACTUAL_FL = 1 AND ACT_HOSD_EXTERN.LOCAL_FL = 0 "
//                        + " ) TMP2 "
//                        + " LEFT JOIN T_CASE_DETAILS ACT_HOSD_LOCAL ON ACT_HOSD_LOCAL.T_CASE_ID = OLD_CASE_ID AND ACT_HOSD_LOCAL.ACTUAL_FL = 1 AND ACT_HOSD_LOCAL.LOCAL_FL = 1"
                        + " FROM (" +
                        "   SELECT      TMP.*, " +
                "        ACT_HOSD_EXTERN.t_case_details_id ACT_HOSD_EXTERN_ID," +
                "		ISNULL(ACT_HOSD_EXTERN.VERSION_NUMBER, '0') + 1 NEW_EXTERN_CSD_VERSION, " +
                "		     CASE        " +
                "				WHEN ACT_STORNIERT = 0     AND STORNIERT = 0     THEN 0        " +
                "				WHEN ACT_STORNIERT = 0     AND STORNIERT = 1     THEN 1        " +
                "				WHEN ACT_STORNIERT = 0     AND STORNIERT IS NULL THEN 0        " +
                "				WHEN ACT_STORNIERT = 1     AND STORNIERT = 0     THEN 0       " +
                "			    WHEN ACT_STORNIERT = 1     AND STORNIERT = 1     THEN 1        " +
                "				WHEN ACT_STORNIERT = 1     AND STORNIERT IS NULL THEN 1        " +
                "				WHEN ACT_STORNIERT IS NULL AND STORNIERT = 0     THEN 0        " +
                "				WHEN ACT_STORNIERT IS NULL AND STORNIERT = 1     THEN NULL        " +
                "				WHEN ACT_STORNIERT IS NULL AND STORNIERT IS NULL THEN 0      " +
                "			END " +
                "				NEW_STORNIERT    FROM (" +
                "				      SELECT IMEX_CASE_TMP.*, T_CASE.ID OLD_CASE_ID, " +
                "					  CASE " +
                "						WHEN T_CASE.ID IS NULL THEN 1 ELSE 0 " +
                "					 END IS_NEW_CASE," +
                "					 IMEX_MAIN_ICD.CODE MAIN_ICD_CODE," +
                "					 IMEX_MAIN_ICD.NR MAIN_ICD_NR," +
                "					 IMEX_SUM_OP_ND.SUM_OF_OPS, " +
                "					 IMEX_SUM_OP_ND.SUM_OF_ICD, " +
                "					 IMEX_PATIENT.NEW_PATIENT_ID,             " +
                "					 CASE " +
                "						WHEN IMEX_BEHANDELNDE_FAB.ERBRINGUNGSART IS NULL THEN 'NR' ELSE IMEX_BEHANDELNDE_FAB.ERBRINGUNGSART " +
                "					END ERBRINGUNGSART,              " +
                "					T_CASE.CANCEL_FL ACT_STORNIERT,             " +
                "					CASE " +
                "						WHEN IMEX_CASE_TMP.GESCHLECHT IS NULL THEN IMEX_PATIENT.GESCHLECHT ELSE IMEX_CASE_TMP.GESCHLECHT " +
                "					END NEW_GESCHLECHT," +
                "					CASE " +
                "						WHEN IMEX_CASE_TMP.FALLSTATUS IS NULL THEN T_CASE.CS_STATUS_EN ELSE IMEX_CASE_TMP.FALLSTATUS " +
                "					END NEW_FALLSTATUS," +
                "					CASE " +
                "						WHEN IMEX_CASE_TMP.FALLSTATUS IS NULL THEN 0 ELSE 1 " +
                "					END KIS_STATUS_FL      " +
                "					FROM IMEX_CASE_TMP      " +
                "					INNER JOIN IMEX_PATIENT ON IMEX_PATIENT.PATNR = IMEX_CASE_TMP.PATNR     " +
                "					 LEFT JOIN T_CASE ON IMEX_CASE_TMP.IKZ = T_CASE.CS_HOSPITAL_IDENT AND IMEX_CASE_TMP.FALLNR = T_CASE.CS_CASE_NUMBER      " +
                "					 LEFT JOIN IMEX_MAIN_ICD ON IMEX_CASE_TMP.IKZ = IMEX_MAIN_ICD.IKZ AND IMEX_CASE_TMP.FALLNR = IMEX_MAIN_ICD.FALLNR      " +
                "					 LEFT JOIN IMEX_SUM_OP_ND ON IMEX_CASE_TMP.IKZ = IMEX_SUM_OP_ND.IKZ AND IMEX_CASE_TMP.FALLNR = IMEX_SUM_OP_ND.FALLNR      " +
                "					 LEFT JOIN IMEX_BEHANDELNDE_FAB ON IMEX_CASE_TMP.IKZ = IMEX_BEHANDELNDE_FAB.IKZ AND IMEX_CASE_TMP.FALLNR = IMEX_BEHANDELNDE_FAB.FALLNR    " +
                "			  ) TMP " +

                "			  left join " +
                "			  (select td.id t_case_details_id, td.t_case_id t_case_id, tmp_local_vers.vers_number version_number from" +
                "					(select t_case_id, max(version_number) vers_number from t_case_details where local_fl = 0 group by t_case_id) tmp_local_vers" +
                "						inner join t_case_details td on td.t_case_id = tmp_local_vers.T_CASE_ID and td.VERSION_NUMBER = tmp_local_vers.vers_number" +
                "				and td.LOCAL_FL = 0) ACT_HOSD_EXTERN" +

                "			  on ACT_HOSD_EXTERN.t_case_id= OLD_CASE_ID" +
                "			  " +
                "		) TMP2  " +
                "		left join" +
                "			  (select td.id t_case_details_id, td.t_case_id t_case_id, tmp_local_vers.vers_number version_number from" +
                "					(select t_case_id, max(version_number) vers_number from t_case_details where local_fl = 1 group by t_case_id) tmp_local_vers" +
                "						inner join t_case_details td on td.t_case_id = tmp_local_vers.T_CASE_ID and td.VERSION_NUMBER = tmp_local_vers.vers_number" +
                "				and td.LOCAL_FL = 1) ACT_HOSD_LOCAL" +
                "		on ACT_HOSD_LOCAL.t_case_id= OLD_CASE_ID" 
                
                );
                caseDbQry.executeUpdate("UPDATE IMEX_CASE SET NEW_CASE_ID = " + nextSqVal("T_CASE_SQ") + " WHERE NEW_CASE_ID IS NULL");

                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_CASE_FALL ON IMEX_CASE (IKZ, FALLNR) " + parallel());
                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_CASE_PATNR ON IMEX_CASE (PATNR) " + parallel());
                //qry.executeUpdate("CREATE INDEX IDX_IMEX_CASE_IS_NEW_CASE ON IMEX_CASE (IS_NEW_CASE)");
                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_CASE_FALL_ID1 ON IMEX_CASE (NEW_CASE_ID) " + parallel());
                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_CASE_FALL_ID2 ON IMEX_CASE (OLD_CASE_ID) " + parallel());
                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_CASE_FALL_DT_ID ON IMEX_CASE (ACT_HOSD_EXTERN_ID) " + parallel());
                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_CASE_MAIN_ICD_NR ON IMEX_CASE (MAIN_ICD_NR) " + parallel());
                caseDbQry.executeUpdate(DbBuilder.getImexPkDefinition("IMEX_CASE"));

                printTime("IMEX_CASE created!");

                printCaseStornoInformations(caseDbQry);

                determineCasesToIgnore(caseDbQry);

                printCaseIgnoreInformations(caseDbQry);

                printTime("Cancel already existing case details...");
                getProgressor().sendProgress("Storniere T_CASE_DETAILS");
                int canceledVersions = caseDbQry.executeUpdate("UPDATE /*+ PARALLEL(T_CASE_DETAILS 5)*/ T_CASE_DETAILS SET MODIFICATION_DATE = " + currentDate() + ", MODIFICATION_USER = " + jobUserId + ", CANCEL_DATE = " + currentDate() + ", CANCEL_REASON_EN = " + CASE_DETAILS_CANCEL_REASON_KIS
                        + " WHERE T_CASE_DETAILS.CANCEL_DATE IS NULL AND CANCEL_REASON_EN IS NULL"
                        + "   AND EXISTS (SELECT 1 FROM IMEX_CASE WHERE IMEX_CASE.NEW_STORNIERT = 1 AND IMEX_CASE.OLD_CASE_ID = T_CASE_DETAILS.T_CASE_ID AND IMEX_CASE.OLD_CASE_ID IS NOT NULL)");
//                if (isOracle()) {
//                    caseDbQry.executeUpdate("UPDATE /*+ PARALLEL(T_CASE_DETAILS 5)*/ T_CASE_DETAILS SET (MODIFICATION_DATE, CANCEL_DATE, CANCEL_REASON_EN) = ("
//                            + "  SELECT " + currentDate() + ", " + currentDate() + ", '" + CASE_DETAILS_CANCEL_REASON_KIS + "' FROM IMEX_CASE "
//                            + "  WHERE IMEX_CASE.OLD_CASE_ID = T_CASE_DETAILS.T_CASE_ID AND OLD_CASE_ID IS NOT NULL "
//                            + "    AND T_CASE_DETAILS.CANCEL_DATE IS NULL AND CANCEL_REASON_EN IS NULL "
//                            + " ) WHERE EXISTS (SELECT 1 FROM IMEX_CASE WHERE IMEX_CASE.OLD_CASE_ID = T_CASE_DETAILS.T_CASE_ID AND OLD_CASE_ID IS NOT NULL)");
//                } else {
//                    caseDbQry.executeUpdate("UPDATE T_PATIENT SET MODIFICATION_DATE = " + currentDate() + ", CANCEL_DATE = " + currentDate() + ", CANCEL_REASON_EN = '" + CASE_DETAILS_CANCEL_REASON_KIS + "' "
//                            + "  FROM T_PATIENT "
//                            + "  INNER JOIN IMEX_PATIENT ON IMEX_PATIENT.OLD_PATIENT_ID = T_PATIENT.ID "
//                            + "  WHERE OLD_PATIENT_ID IS NOT NULL AND EXISTS (SELECT 1 FROM IMEX_PATIENT WHERE IMEX_PATIENT.OLD_PATIENT_ID = T_PATIENT.ID AND OLD_PATIENT_ID IS NOT NULL)");
//                }
                //caseDbQry.executeUpdate("UPDATE T_CASE_DETAILS SET CANCEL_DATE = " + currentDate() + ", CANCEL_REASON_EN = '" + CASE_DETAILS_CANCEL_REASON_KIS + "'  ON IMEX_CASE (ACT_HOSD_EXTERN_ID) " + parallel());
                printTime("Already existing case details canceled (" + canceledVersions + " versions affected)!");

                
                //DROP EXISTING CASES IF OVERWRITE = TRUE
                //printTime("Update/Insert T_PATIENT...");
                printTime("Check for new patient version...");
                checkNewPatientVersion(caseDbQry);
                printTime("Checked new patient version!");

                printTime("Check for new patient insurance version...");
                checkNewInsuranceVersion(caseDbQry);
                printTime("Checked new patient insurance version!");

                printTime("Let me tell you what I've found out about patients...");
                printPatientVersionInformations(caseDbQry);
                printTime("I told you what I've found out about patients!");

                //Update existing patients (T_PATIENT)
                printTime("Update existing T_PATIENT...");
                getProgressor().sendProgress("Aktualisiere Einträge in T_PATIENT");
                if (isOracle()) {
                    caseDbQry.executeUpdate("UPDATE /*+ PARALLEL(T_PATIENT 5)*/ T_PATIENT SET (MODIFICATION_DATE, MODIFICATION_USER, PAT_DATE_OF_BIRTH, PAT_FIRST_NAME, PAT_GENDER_EN, PAT_NUMBER, PAT_SEC_NAME, PAT_TITLE) = ("
                            + "  SELECT " + currentDate() + ", " + jobUserId + ", GEBURTSDATUM, VORNAME, GESCHLECHT, PATNR, NACHNAME, TITEL FROM IMEX_PATIENT "
                            + "  WHERE IMEX_PATIENT.OLD_PATIENT_ID = T_PATIENT.ID AND OLD_PATIENT_ID IS NOT NULL "
                            + " ) WHERE EXISTS (SELECT 1 FROM IMEX_PATIENT WHERE IMEX_PATIENT.OLD_PATIENT_ID = T_PATIENT.ID AND OLD_PATIENT_ID IS NOT NULL)");
                } else {
                    caseDbQry.executeUpdate("UPDATE T_PATIENT SET MODIFICATION_DATE = " + currentDate() + ", MODIFICATION_USER = " + jobUserId + ", PAT_DATE_OF_BIRTH = GEBURTSDATUM, PAT_FIRST_NAME = VORNAME, PAT_GENDER_EN = GESCHLECHT, PAT_NUMBER = PATNR, PAT_SEC_NAME = NACHNAME, PAT_TITLE = TITEL "
                            + "  FROM T_PATIENT "
                            + "  INNER JOIN IMEX_PATIENT ON IMEX_PATIENT.OLD_PATIENT_ID = T_PATIENT.ID "
                            + "  WHERE OLD_PATIENT_ID IS NOT NULL AND EXISTS (SELECT 1 FROM IMEX_PATIENT WHERE IMEX_PATIENT.OLD_PATIENT_ID = T_PATIENT.ID AND OLD_PATIENT_ID IS NOT NULL)");
                }
                printTime("Updating existing T_PATIENT finished!");

                //Create new patients (T_PATIENT)
                printTime("Creating new T_PATIENT...");
                getProgressor().sendProgress("Erzeuge neue Einträge in T_PATIENT");
                caseDbQry.executeUpdate("INSERT /*+ PARALLEL(T_PATIENT 5)*/ INTO T_PATIENT (ID, CREATION_DATE, CREATION_USER, PAT_DATE_OF_BIRTH, PAT_FIRST_NAME, PAT_GENDER_EN, PAT_NUMBER, PAT_SEC_NAME, PAT_TITLE) "
                        + " SELECT NEW_PATIENT_ID, " + currentDate() + ", " + jobUserId + ", GEBURTSDATUM, VORNAME, GESCHLECHT, PATNR, NACHNAME, TITEL "
                        + " FROM IMEX_PATIENT "
                        + " WHERE IMEX_PATIENT.OLD_PATIENT_ID IS NULL");
                printTime("Creating new T_PATIENT finished!");

                printTime("Create IDs for new patient version...");
                createNewPatientIds(caseDbQry);
                printTime("IDs for new patient version created!");

                printTime("Create IDs for new patient insurance version...");
                createNewInsuranceIds(caseDbQry);
                printTime("IDs for new patient insurance version created!");

                //printTime("Drop indexes...");
                //dropIndexes(qry, indexNames);
                //printTime("Indexes dropped!");
                printTime("  Create new patient detail version...");
                getProgressor().sendProgress("Erzeuge neue Einträge in T_PATIENT_DETAILS");
                caseDbQry.executeUpdate("UPDATE /*+ PARALLEL(T_PATIENT_DETAILS 5)*/ T_PATIENT_DETAILS SET PATD_IS_ACTUAL_FL = 0 WHERE PATD_IS_ACTUAL_FL = 1 AND EXISTS (SELECT 1 FROM IMEX_PATIENT WHERE OLD_PATIENT_ID = T_PATIENT_DETAILS.T_PATIENT_ID AND NEW_VERSION = 1)");
                caseDbQry.executeUpdate("INSERT /*+ PARALLEL(T_PATIENT_DETAILS 5)*/ INTO T_PATIENT_DETAILS (ID, CREATION_DATE, CREATION_USER, T_PATIENT_ID, PATD_IS_ACTUAL_FL, PATD_POST_DIFF_FL, PATD_CELL_NUMBER, PATD_PHONE_NUMBER, PATD_ADDRESS, PATD_CITY, PATD_COUNTRY, PATD_STATE, PATD_ZIPCODE) "
                        + " SELECT NEW_PATIENT_ID.NEW_DETAILS_ID, " + currentDate() + ", " + jobUserId + ", IMEX_PATIENT.NEW_PATIENT_ID, 1, 0, IMEX_PATIENT.MOBIL, IMEX_PATIENT.TELEFON, IMEX_PATIENT.ADRESSE, IMEX_PATIENT.ORT, IMEX_PATIENT.LAND, IMEX_PATIENT.BUNDESLAND, IMEX_PATIENT.PLZ "
                        + " FROM IMEX_PATIENT "
                        + " INNER JOIN IMEX_PATIENT_NEW_ID NEW_PATIENT_ID ON IMEX_PATIENT.PATNR = NEW_PATIENT_ID.PATNR ");
                printTime("  New patient detail version created!");

                printTime("  Create new patient insurance version...");
                getProgressor().sendProgress("Erzeuge neue Einträge in T_INSURANCE");
                caseDbQry.executeUpdate("UPDATE /*+ PARALLEL(T_INSURANCE 5)*/ T_INSURANCE SET INS_IS_ACTUAL_FL = 0 WHERE INS_IS_ACTUAL_FL = 1 AND EXISTS (SELECT 1 FROM IMEX_PATIENT WHERE OLD_PATIENT_ID = T_INSURANCE.T_PATIENT_ID AND NEW_INSURANCE_VERSION = 1)");
                caseDbQry.executeUpdate("INSERT /*+ PARALLEL(T_INSURANCE 5)*/ INTO T_INSURANCE (ID, CREATION_DATE, CREATION_USER, T_PATIENT_ID, INS_IS_ACTUAL_FL, INS_INS_COMPANY, INS_NUMBER) "
                        + " SELECT NEW_INSURANCE_ID.NEW_INS_ID, " + currentDate() + ", " + jobUserId + ", IMEX_PATIENT.NEW_PATIENT_ID, 1, IMEX_PATIENT.KASSE, IMEX_PATIENT.VERSICHERTENNR " //(SELECT NVL(MAX(CSD_VERSION), 0) + 1 FROM T_CASE_DETAILS TCD WHERE TCD.HOSC_ID = HOSC_ID AND TCD.CSD_VERSION = " + isLocal + ")
                        + " FROM IMEX_PATIENT "
                        + " INNER JOIN IMEX_PAT_INSURANCE_NEW_ID NEW_INSURANCE_ID ON IMEX_PATIENT.PATNR = NEW_INSURANCE_ID.PATNR ");
                printTime("  New patient detail insurance created!");

                printTime("Check for new case version...");
                checkNewCaseVersion(caseDbQry);
                printTime("Checked new case version!");

                printTime("Let me tell you what I've found out about hospital cases...");
                printCaseVersionInformations(caseDbQry);
                printTime("I told you what I've found out about hospital cases!");

                printTime("Update existing T_CASE...");
                //Update existing hospital cases (T_CASE)
                getProgressor().sendProgress("Aktualisiere Einträge in T_CASE");
                if (isOracle()) {
                    //Vorhandenen Fall aktualisieren
                    caseDbQry.executeUpdate("UPDATE /*+ PARALLEL(T_CASE 5)*/ T_CASE SET (MODIFICATION_DATE, MODIFICATION_USER, CS_CASE_NUMBER, CS_CASE_TYPE_EN, CANCEL_FL, CS_FEE_GROUP_EN, CS_HOSPITAL_IDENT, INSURANCE_IDENTIFIER, INSURANCE_NUMBER_PATIENT, CS_KIS_STATUS_FL, CS_STATUS_EN, T_PATIENT_ID, NUMERIC_01, NUMERIC_02, NUMERIC_03, NUMERIC_04, NUMERIC_05, NUMERIC_06, NUMERIC_07, NUMERIC_08, NUMERIC_09, NUMERIC_10, STRING_01, STRING_02, STRING_03, STRING_04, STRING_05, STRING_06, STRING_07, STRING_08, STRING_09, STRING_10, CS_BILLING_DATE) = ( "
                            + " SELECT " + currentDate() + ", " + jobUserId + ", IMEX_CASE.FALLNR, FALLART, NEW_STORNIERT, ENTGELTBEREICH, IMEX_CASE.IKZ, IMEX_CASE.KASSE, IMEX_CASE.VERSICHERTENNR, IMEX_CASE.KIS_STATUS_FL, CASE WHEN IMEX_CASE.NEW_VERSION = 1 THEN 'NEW_VERS' ELSE T_CASE.CS_STATUS_EN END, IMEX_CASE.NEW_PATIENT_ID, IMEX_CASE.NUMERIC1, IMEX_CASE.NUMERIC2, IMEX_CASE.NUMERIC3, IMEX_CASE.NUMERIC4, IMEX_CASE.NUMERIC5, IMEX_CASE.NUMERIC6, IMEX_CASE.NUMERIC7, IMEX_CASE.NUMERIC8, IMEX_CASE.NUMERIC9, IMEX_CASE.NUMERIC10, IMEX_CASE.STRING1, IMEX_CASE.STRING2, IMEX_CASE.STRING3, IMEX_CASE.STRING4, IMEX_CASE.STRING5, IMEX_CASE.STRING6, IMEX_CASE.STRING7, IMEX_CASE.STRING8, IMEX_CASE.STRING9, IMEX_CASE.STRING10, IMEX_CASE.BILLING_DATE FROM IMEX_CASE "
                            + " WHERE IMEX_CASE.OLD_CASE_ID = T_CASE.ID AND OLD_CASE_ID IS NOT NULL "
                            + " ) WHERE EXISTS (SELECT 1 FROM IMEX_CASE WHERE IMEX_CASE.OLD_CASE_ID = T_CASE.ID AND OLD_CASE_ID IS NOT NULL) ");
                } else {
                    caseDbQry.executeUpdate("UPDATE T_CASE SET MODIFICATION_DATE = " + currentDate() + ", MODIFICATION_USER = " + jobUserId + ", CS_CASE_NUMBER = FALLNR, CS_CASE_TYPE_EN = FALLART, CANCEL_FL = NEW_STORNIERT, CS_FEE_GROUP_EN = ENTGELTBEREICH, CS_HOSPITAL_IDENT = IKZ, INSURANCE_IDENTIFIER = KASSE, INSURANCE_NUMBER_PATIENT = VERSICHERTENNR, CS_KIS_STATUS_FL = IMEX_CASE.KIS_STATUS_FL, CS_STATUS_EN = CASE WHEN IMEX_CASE.NEW_VERSION = 1 THEN 'NEW_VERS' ELSE T_CASE.CS_STATUS_EN END, T_PATIENT_ID = NEW_PATIENT_ID, NUMERIC_01 = NUMERIC1, NUMERIC_02 = NUMERIC2, NUMERIC_03 = NUMERIC3, NUMERIC_04 = NUMERIC4, NUMERIC_05 = NUMERIC5, NUMERIC_06 = NUMERIC6, NUMERIC_07 = NUMERIC7, NUMERIC_08 = NUMERIC8, NUMERIC_09 = NUMERIC9, NUMERIC_10 = NUMERIC10, STRING_01 = STRING1, STRING_02 = STRING2, STRING_03 = STRING3, STRING_04 = STRING4, STRING_05 = STRING5, STRING_06 = STRING6, STRING_07 = STRING7, STRING_08 = STRING8, STRING_09 = STRING9, STRING_10 = STRING10, CS_BILLING_DATE = BILLING_DATE "
                            + " FROM T_CASE "
                            + " INNER JOIN IMEX_CASE ON IMEX_CASE.OLD_CASE_ID = T_CASE.ID ");
                    // + " WHERE OLD_CASE_ID IS NOT NULL AND EXISTS (SELECT 1 FROM IMEX_CASE WHERE IMEX_CASE.OLD_CASE_ID = T_CASE.ID AND OLD_CASE_ID IS NOT NULL) ");
                }
                printTime("Updating existing T_CASE finished!");

                //Create new hospital cases (T_CASE)
                printTime("Creating new T_CASE...");
                //Neuen Fall anlegen
                getProgressor().sendProgress("Erzeuge neue Einträge in T_CASE");
                caseDbQry.executeUpdate("INSERT /*+ PARALLEL(T_CASE 5)*/ INTO T_CASE (ID, CREATION_DATE, CREATION_USER, CS_CASE_NUMBER, CS_CASE_TYPE_EN, CANCEL_FL, CS_FEE_GROUP_EN, CS_HOSPITAL_IDENT, INSURANCE_IDENTIFIER, INSURANCE_NUMBER_PATIENT, CS_KIS_STATUS_FL, CS_STATUS_EN, T_PATIENT_ID, NUMERIC_01, NUMERIC_02, NUMERIC_03, NUMERIC_04, NUMERIC_05, NUMERIC_06, NUMERIC_07, NUMERIC_08, NUMERIC_09, NUMERIC_10, STRING_01, STRING_02, STRING_03, STRING_04, STRING_05, STRING_06, STRING_07, STRING_08, STRING_09, STRING_10, CS_BILLING_DATE) "
                        + " SELECT NEW_CASE_ID, " + currentDate() + ", " + jobUserId + ", IMEX_CASE.FALLNR, FALLART, NEW_STORNIERT, ENTGELTBEREICH, IMEX_CASE.IKZ, IMEX_CASE.KASSE, IMEX_CASE.VERSICHERTENNR, 0, 'NEW', IMEX_CASE.NEW_PATIENT_ID, IMEX_CASE.NUMERIC1, IMEX_CASE.NUMERIC2, IMEX_CASE.NUMERIC3, IMEX_CASE.NUMERIC4, IMEX_CASE.NUMERIC5, IMEX_CASE.NUMERIC6, IMEX_CASE.NUMERIC7, IMEX_CASE.NUMERIC8, IMEX_CASE.NUMERIC9, IMEX_CASE.NUMERIC10, IMEX_CASE.STRING1, IMEX_CASE.STRING2, IMEX_CASE.STRING3, IMEX_CASE.STRING4, IMEX_CASE.STRING5, IMEX_CASE.STRING6, IMEX_CASE.STRING7, IMEX_CASE.STRING8, IMEX_CASE.STRING9, IMEX_CASE.STRING10, IMEX_CASE.BILLING_DATE "
                        + " FROM IMEX_CASE "
                        + " WHERE IMEX_CASE.OLD_CASE_ID IS NULL ");
                printTime("Creating new T_CASE finished!");

                printTime("Create IDs for new case version...");
                createNewCaseIds(caseDbQry);
                printTime("IDs for new case version created!");

                //Execute this after createNewCaseIds!
                //        printTime("Create IMEX_MAIN_ICD...");
                //        createImexMainIcd(qry);
                //        printTime("IMEX_MAIN_ICD created!");
                printTime("Create new case version...");
                String comment = ", ' " +  Lang.toDateTime(new Date()) + "; Importiert aus KIS ////'";
                //Create new T_CASE_DETAILS
                caseDbQry.executeUpdate("UPDATE /*+ PARALLEL(T_CASE_DETAILS 5)*/ T_CASE_DETAILS SET ACTUAL_FL = 0, MODIFICATION_DATE = " + currentDate() + ", MODIFICATION_USER = " + jobUserId + " WHERE ACTUAL_FL = 1 AND EXISTS (SELECT 1 FROM IMEX_CASE WHERE OLD_CASE_ID = T_CASE_DETAILS.T_CASE_ID AND NEW_VERSION = 1)");
                for (int isLocal = 0; isLocal <= 1; isLocal++) {
                    final String pNewIdTableName = (isLocal == 0 ? "EXTERN" : "LOCAL");
                    final String pLogLabel = (isLocal == 0 ? "external" : "local");
                    final String pCommentLabel = (isLocal == 0 ? "extern" : "lokal");

                    printTime("  Create new " + pLogLabel + " case detail version...");
                    getProgressor().sendProgress("Erzeuge neue Einträge in T_CASE_DETAILS (" + pCommentLabel + ")");
                    caseDbQry.executeUpdate("INSERT /*+ PARALLEL(T_CASE_DETAILS 5)*/ INTO T_CASE_DETAILS (ID, CREATION_DATE, CREATION_USER, ADMISSION_CAUSE_EN, ADMISSION_REASON_12_EN, ADMISSION_REASON_34_EN, ADMISSION_DATE, ADMISSION_WEIGHT, AGE_DAYS, AGE_YEARS, DISCHARGE_REASON_12_EN, DISCHARGE_REASON_3_EN, DISCHARGE_DATE, HMV, ACTUAL_FL, LOCAL_FL, VERSION_NUMBER, LOS_INTENSIV, ADMISSION_LAW_EN, HD_ICD_CODE, LEAVE, LOS, LOS_ALTERATION, CSD_LOS_MD_ALTERATION, SUM_OF_OPS, SUM_OF_ICD, ADMISSION_MODE_EN, GENDER_EN, CANCEL_DATE, CANCEL_REASON_EN, PARENT_ID, T_CASE_ID" + (isLocal == 1 ? ", EXTERN_ID" : "") 
                            +", CSD_COMMENT) "
                            + " SELECT NEW_CASE_ID.NEW_DETAILS_ID, " + currentDate() + ", " + jobUserId + ", AUFNAHMEANLASS, AUFNAHMEGRUND1, AUFNAHMEGRUND2, AUFNAHMEDATUM, GEWICHT, ALTER_IN_TAGEN, ALTER_IN_JAHREN, ENTLASSUNGSGRUND12, ENTLASSUNGSGRUND3, ENTLASSUNGSDATUM, BEATMUNGSSTUNDEN, 1, " + isLocal + ", IMEX_CASE.NEW_" + pNewIdTableName + "_CSD_VERSION, IMEX_CASE.VWD_INTENSIV, IMEX_CASE.GESETZL_PSYCHSTATUS,IMEX_CASE.MAIN_ICD_CODE ,IMEX_CASE.URLAUBSTAGE, IMEX_CASE.VWD, IMEX_CASE.VWD_SIMULIERT, IMEX_CASE.MD_TOB, IMEX_CASE.SUM_OF_OPS, IMEX_CASE.SUM_OF_ICD, IMEX_CASE.ERBRINGUNGSART, IMEX_CASE.NEW_GESCHLECHT, CASE WHEN IMEX_CASE.NEW_STORNIERT = 1 THEN " + currentDate() + " ELSE NULL END, CASE WHEN IMEX_CASE.NEW_STORNIERT = 1 THEN " + CASE_DETAILS_CANCEL_REASON_KIS + " ELSE NULL END, " + (isLocal == 1 ? "NEW_CASE_ID.NEW_EXT_DETAILS_ID" : "NULL") + ", IMEX_CASE.NEW_CASE_ID" + (isLocal == 1 ? ", NEW_CASE_ID.NEW_EXT_DETAILS_ID" : ""                                    )
                            + comment
                            + " FROM IMEX_CASE "
                            + " INNER JOIN IMEX_CASE_NEW_" + pNewIdTableName + "_ID NEW_CASE_ID ON IMEX_CASE.NR = NEW_CASE_ID.NR "
                            + " LEFT JOIN IMEX_DIAGNOSE_NEW_" + pNewIdTableName + "_ID MAIN_ICD ON MAIN_ICD.NR = IMEX_CASE.MAIN_ICD_NR ");
                    printTime("  New " + pLogLabel + " case detail version created!");

                    printTime("  Create new " + pLogLabel + " case department version...");
                    getProgressor().sendProgress("Erzeuge neue Einträge in T_CASE_DEPARTMENT (" + pCommentLabel + ")");
                    caseDbQry.executeUpdate("INSERT /*+ PARALLEL(T_CASE_DEPARTMENT 5)*/ INTO T_CASE_DEPARTMENT (ID, CREATION_DATE, CREATION_USER, T_CASE_DETAILS_ID, DEP_KEY_301, DEP_SHORT_NAME, DEPC_ADM_DATE, DEPC_ADMOD_EN, DEPC_DIS_DATE, DEPC_HMV, DEPC_IS_TREATING_FL, DEPC_IS_ADMISSION_FL, DEPC_IS_DISCHARGE_FL, DEPC_IS_BED_INTENSIV_FL, DEPC_LOCATE_NUMBER) "
                            + " SELECT NEW_DEP_ID, " + currentDate() + ", " + jobUserId + ", NEW_DETAILS_ID, IMEX_DEPARTMENT_TMP.CODE, IMEX_DEPARTMENT_TMP.CODE_INTERN, IMEX_DEPARTMENT_TMP.VERLEGUNGSDATUM, IMEX_DEPARTMENT_TMP.ERBRINGUNGSART, IMEX_DEPARTMENT_TMP.ENTLASSUNGSDATUM, 0, " + isNull("IMEX_BEHANDELNDE_FAB.BEHANDELNDE_FAB", "0") + ", " + isNull("IMEX_AUFNEHMENDE_FAB.AUFNEHMENDE_FAB", "0") + ", " + isNull("IMEX_ENTLASSENDE_FAB.ENTLASSENDE_FAB", "0") + ", IMEX_DEPARTMENT_TMP.IS_BED_INTENSIV, IMEX_DEPARTMENT_TMP.LOCATION_NUMBER "
                            + " FROM IMEX_DEPARTMENT_TMP "
                            + " LEFT JOIN IMEX_BEHANDELNDE_FAB ON IMEX_BEHANDELNDE_FAB.NR = IMEX_DEPARTMENT_TMP.NR "
                            + " LEFT JOIN IMEX_AUFNEHMENDE_FAB ON IMEX_AUFNEHMENDE_FAB.NR = IMEX_DEPARTMENT_TMP.NR "
                            + " LEFT JOIN IMEX_ENTLASSENDE_FAB ON IMEX_ENTLASSENDE_FAB.NR = IMEX_DEPARTMENT_TMP.NR "
                            + " INNER JOIN IMEX_DEPARTMENT_NEW_" + pNewIdTableName + "_ID NEW_ID ON IMEX_DEPARTMENT_TMP.NR = NEW_ID.NR ");
                    printTime("  New " + pLogLabel + " case department version created!");

                    printTime("  Create new " + pLogLabel + " case ward version...");
                    getProgressor().sendProgress("Erzeuge neue Einträge in T_CASE_WARD (" + pCommentLabel + ")");
                    caseDbQry.executeUpdate("INSERT /*+ PARALLEL(T_CASE_WARD 5)*/ INTO T_CASE_WARD (ID, CREATION_DATE, CREATION_USER, WARDC_ADMDATE, WARDC_DISDATE, WARDC_IDENT, T_CASE_DEPARTMENT_ID) "
                            + " SELECT NEW_WARD_ID, " + currentDate() + ", " + jobUserId + ", IMEX_WARD_TMP.VERLEGUNGSDATUM, IMEX_WARD_TMP.ENTLASSUNGSDATUM, IMEX_WARD_TMP.CODE, NEW_ID.NEW_DEP_ID "
                            + " FROM IMEX_WARD_TMP "
                            + " INNER JOIN IMEX_WARD_NEW_" + pNewIdTableName + "_ID NEW_ID ON IMEX_WARD_TMP.NR = NEW_ID.NR ");
                    printTime("  New " + pLogLabel + " case department version created!");

                    printTime("  Create new " + pLogLabel + " case procedure version...");
                    getProgressor().sendProgress("Erzeuge neue Einträge in T_CASE_OPS (" + pCommentLabel + ")");
                    caseDbQry.executeUpdate("INSERT /*+ PARALLEL(T_CASE_OPS 5)*/ INTO T_CASE_OPS (ID, CREATION_DATE, CREATION_USER, T_CASE_DEPARTMENT_ID, TO_GROUP_FL, OPSC_CODE, OPSC_DATUM, OPSC_LOC_EN, T_CASE_WARD_ID) "
                            + " SELECT NEW_OPS_ID, " + currentDate() + ", " + jobUserId + ", NEW_ID.NEW_DEP_ID, TO_GROUP, IMEX_PROCEDURE_TMP.CODE, DATUM, CASE WHEN LOKALISATION  IS NULL THEN 'E'  ELSE LOKALISATION END  , NEW_WARD_ID.NEW_WARD_ID "
                            + " FROM IMEX_PROCEDURE_TMP "
                            + " INNER JOIN IMEX_PROCEDURE_NEW_" + pNewIdTableName + "_ID NEW_ID ON IMEX_PROCEDURE_TMP.NR = NEW_ID.NR "
                            + " LEFT JOIN IMEX_WARD_NEW_" + pNewIdTableName + "_ID NEW_WARD_ID ON IMEX_PROCEDURE_TMP.WARD_NR = NEW_WARD_ID.NR ");
                    printTime("  New " + pLogLabel + " case procedure version created!");

                    printTime("  Create new " + pLogLabel + " case diagnose version...");
                    getProgressor().sendProgress("Erzeuge neue Einträge in T_CASE_ICD (" + pCommentLabel + ")");
                    caseDbQry.executeUpdate("INSERT /*+ PARALLEL(T_CASE_ICD 5)*/ INTO T_CASE_ICD (ID, CREATION_DATE, CREATION_USER, T_CASE_DEPARTMENT_ID, TO_GROUP_FL, ICDC_CODE, ICDC_TYPE_EN, MAIN_DIAG_DEP_FL, MAIN_DIAG_CASE_FL, ICDC_LOC_EN ,T_CASE_ICD_ID, ICD_REFERENCE_EN, T_CASE_WARD_ID) "
                            + " SELECT NEW_ID.NEW_ICD_ID, " + currentDate() + ", " + jobUserId + ", NEW_ID.NEW_DEP_ID, IMEX_DIAGNOSE.TO_GROUP, IMEX_DIAGNOSE.CODE, IMEX_DIAGNOSE.ICD_TYPE, IMEX_DIAGNOSE.HDB, IMEX_DIAGNOSE.HDX, CASE WHEN IMEX_DIAGNOSE.LOKALISATION  IS NULL THEN 'E'  ELSE IMEX_DIAGNOSE.LOKALISATION END , NEW_REF_ID.NEW_ICD_ID, IMEX_DIAGNOSE.REF_ICD_TYPE, NEW_WARD_ID.NEW_WARD_ID "
                            + " FROM IMEX_DIAGNOSE "
                            + " LEFT JOIN IMEX_DIAGNOSE REF_DIAGNOSE ON REF_DIAGNOSE.NR = IMEX_DIAGNOSE.REF_ICD_NR "
                            + " LEFT JOIN IMEX_DIAGNOSE_NEW_" + pNewIdTableName + "_ID NEW_REF_ID ON REF_DIAGNOSE.NR = NEW_REF_ID.NR "
                            + " INNER JOIN IMEX_DIAGNOSE_NEW_" + pNewIdTableName + "_ID NEW_ID ON IMEX_DIAGNOSE.NR = NEW_ID.NR "
                            + " LEFT JOIN IMEX_WARD_NEW_" + pNewIdTableName + "_ID NEW_WARD_ID ON IMEX_DIAGNOSE.WARD_NR = NEW_WARD_ID.NR ");
                    printTime("  New " + pLogLabel + " case diagnose version created!");

                    printTime("  Create new " + pLogLabel + " case fee version...");
                    getProgressor().sendProgress("Erzeuge neue Einträge in T_CASE_FEE (" + pCommentLabel + ")");
                    caseDbQry.executeUpdate("INSERT /*+ PARALLEL(T_CASE_FEE 5)*/ INTO T_CASE_FEE (ID, CREATION_DATE, CREATION_USER, T_CASE_DETAILS_ID, FEEC_COUNT, FEEC_FEEKEY, FEEC_FROM, FEEC_TO, FEEC_UNBILLED_DAYS, FEEC_VALUE, FEEC_INSURANCE, FEEC_IS_BILL_FL) "
                            + " SELECT NEW_FEE_ID, " + currentDate() + ", " + jobUserId + ", NEW_DETAILS_ID, IMEX_FEE_TMP.ANZAHL, IMEX_FEE_TMP.ENTGELTSCHLUESSEL, IMEX_FEE_TMP.VON, IMEX_FEE_TMP.BIS, IMEX_FEE_TMP.TOB, IMEX_FEE_TMP.BETRAG, IMEX_FEE_TMP.KASSE, IMEX_FEE_TMP.IS_RECHNUNG "
                            + " FROM IMEX_FEE_TMP "
                            + " INNER JOIN IMEX_FEE_NEW_" + pNewIdTableName + "_ID NEW_ID ON IMEX_FEE_TMP.NR = NEW_ID.NR ");
                    printTime("  New " + pLogLabel + " case fee version created!");

                    //printTime("  Set Main ICD in " + pLogLabel + " case detail version...");
                    //
                    //printTime("  Main ICD in " + pLogLabel + " case detail version setted!");
                }

                printTime("New case version created!");

                //            if (pImportConfig.isOverwriteCases()) {
                //                printTime("Add formerly dropped cases to T_WM_PROCESS_T_CASE again...");
                //
                //                qry.executeUpdate("INSERT /*+ PARALLEL(T_WM_PROCESS_T_CASE 5)*/ INTO T_WM_PROCESS_T_CASE (ID, CREATION_DATE, MODIFICATION_DATE, T_WM_PROCESS_ID, T_CASE_ID, MAIN_CASE_FL) " + 
                //                        " SELECT " + nextSqVal("T_WM_PROCESS_T_CASE_SQ") + ", " + currentDate() + ", " + currentDate() + ", T_WM_PROCESS_ID, NEW_CASE_ID, MAIN_CASE_FL FROM IMEX_DROP_PROCESS_CASE_IDS " +
                //                        " INNER JOIN IMEX_CASE ON IMEX_CASE.IKZ = IMEX_DROP_PROCESS_CASE_IDS.IKZ AND IMEX_CASE.FALLNR = IMEX_DROP_PROCESS_CASE_IDS.FALLNR ");
                //
                //                printTime("Formerly dropped cases added to T_WM_PROCESS_T_CASE again!");
                //            }
// AGe: cancel cases that where cancelled by case merging by db import( now orbis)
                printTime("Cancel cases used for case merging by db import from kis(orbis)");
                cancelMergedCases(caseDbQry);
                printTime("Create new KAINs...");
                createNewKains(caseDbQry, commonDbQry);
                printTime("New KAINs created!");

                printTime("Create new FI_DATEN...");
                createNewFiDaten(caseDbQry);
                printTime("New FI_DATEN created!");

                printTime("Create new LABs...");
                createNewLaborDaten(caseDbQry);
                printTime("New LABs created!");

                printTime("Create new DRUGs...");
                createNewMedDaten(caseDbQry);
                printTime("New DRUGs created!");

                printTime("Refresh indexes...");
                refreshIndexes(caseDbQry);
                printTime("Indexes refreshed!");

                int numOfPatients = getPatientCount(caseDbQry);
                int numOfCases = getCaseCount(caseDbQry);
                int numOfImportedCases = getImexCaseCount(caseDbQry);
                int numOfIgnoredCases = getImexCaseIgnoredCount(caseDbQry);
                pImportConfig.setNumberOfPatients(numOfPatients);
                pImportConfig.setNumberOfCases(numOfCases);
                pImportConfig.setNumberOfImportedCases(numOfImportedCases);
                pImportConfig.setNumberOfIgnoredCases(numOfIgnoredCases);
                printTime("I have finished, there are " + numOfPatients + " patients and " + numOfCases + " hospital cases in the database right now");
                getProgressor().sendProgress("Verteilung abgeschlossen");
            }
        }
    }

    /*
    public void dropCase(final Query pQry, final long pCaseId) {
      
    }
     */
    protected void checkNewPatientVersion(final Query pQry) throws IOException, SQLException, ParseException {
        pQry.executeUpdate("ALTER TABLE IMEX_PATIENT ADD NEW_VERSION " + (isOracle() ? "NUMBER(1, 0)" : "BIT") + " DEFAULT 0 NOT NULL ");
        pQry.executeUpdate("ALTER TABLE IMEX_PATIENT ADD VERSION_REASON VARCHAR(30)");
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_PATIENT_NEW ON IMEX_PATIENT (NEW_VERSION, IS_NEW_PATIENT)");
        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_PATIENT 5)*/ IMEX_PATIENT SET NEW_VERSION = 1, VERSION_REASON='NEW_PATIENT' WHERE IS_NEW_PATIENT = 1 ");
        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_PATIENT 5)*/ IMEX_PATIENT SET NEW_VERSION = 1, VERSION_REASON='NO_ACTUAL_PAT_DETAILS' WHERE NEW_VERSION = 0 AND ACT_PAT_DETAILS_ID IS NULL ");

        printTime("  Check differences in patient details...");
        checkPatientDetailsVersion(pQry);
        printTime("  Differences in case patient checked!");
    }

    protected void checkPatientDetailsVersion(final Query pQry) throws IOException, SQLException, ParseException {
        dropTable(pQry, "IMEX_PAT_DETAILS_VERSION_ACT");
        dropTable(pQry, "IMEX_PAT_DETAILS_VERSION_NEW");

        getProgressor().sendProgress("Erzeuge IMEX_PAT_DETAILS_VERSION_ACT");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_PAT_DETAILS_VERSION_ACT NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT "
                + "  IMEX_PATIENT.OLD_PATIENT_ID PATIENT_ID,"
                + "  IMEX_PATIENT.ACT_PAT_DETAILS_ID,"
                + "  PATD.PATD_CELL_NUMBER, "
                + "  PATD.PATD_PHONE_NUMBER, "
                + "  PATD.PATD_ADDRESS, "
                + "  PATD.PATD_CITY, "
                + "  PATD.PATD_COUNTRY,"
                + "  PATD.PATD_STATE,"
                + "  PATD.PATD_ZIPCODE "
                + (isOracle() ? "" : " INTO IMEX_PAT_DETAILS_VERSION_ACT ")
                + " FROM T_PATIENT_DETAILS PATD "
                + " INNER JOIN IMEX_PATIENT ON IMEX_PATIENT.ACT_PAT_DETAILS_ID = PATD.ID"
                + " WHERE IMEX_PATIENT.ACT_PAT_DETAILS_ID IS NOT NULL AND IMEX_PATIENT.OLD_PATIENT_ID IS NOT NULL AND IMEX_PATIENT.NEW_VERSION = 0");

        getProgressor().sendProgress("Erzeuge IMEX_PAT_DETAILS_VERSION_NEW");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_PAT_DETAILS_VERSION_NEW NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT "
                + "  IMEX_PATIENT.OLD_PATIENT_ID PATIENT_ID,"
                + "  IMEX_PATIENT.ACT_PAT_DETAILS_ID,"
                + "  IMEX_PATIENT.MOBIL PATD_CELL_NUMBER, "
                + "  IMEX_PATIENT.TELEFON PATD_PHONE_NUMBER, "
                + "  IMEX_PATIENT.ADRESSE PATD_ADDRESS, "
                + "  IMEX_PATIENT.ORT PATD_CITY, "
                + "  IMEX_PATIENT.LAND PATD_COUNTRY,"
                + "  IMEX_PATIENT.BUNDESLAND PATD_STATE, "
                + "  IMEX_PATIENT.PLZ PATD_ZIPCODE "
                + (isOracle() ? "" : " INTO IMEX_PAT_DETAILS_VERSION_NEW ")
                + " FROM IMEX_PATIENT"
                + " WHERE IMEX_PATIENT.ACT_PAT_DETAILS_ID IS NOT NULL AND IMEX_PATIENT.OLD_PATIENT_ID IS NOT NULL AND IMEX_PATIENT.NEW_VERSION = 0");

        pQry.executeUpdate("CREATE INDEX PAT_VERSION_DETAILS_ACT_IDX ON IMEX_PAT_DETAILS_VERSION_ACT (PATIENT_ID) " + parallel());

        pQry.executeUpdate("CREATE INDEX PAT_VERSION_DETAILS_NEW_IDX ON IMEX_PAT_DETAILS_VERSION_NEW (PATIENT_ID) " + parallel());

        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_PATIENT 5)*/ IMEX_PATIENT SET NEW_VERSION = 1, VERSION_REASON = 'PAT_DETAILS_COUNT' WHERE NEW_VERSION = 0 AND IMEX_PATIENT.OLD_PATIENT_ID IS NOT NULL "
                + "  AND (EXISTS ("
                + "  SELECT COUNT(*) FROM IMEX_PAT_DETAILS_VERSION_ACT WHERE PATIENT_ID = IMEX_PATIENT.OLD_PATIENT_ID "
                + "  " + minus() + " "
                + "  SELECT COUNT(*) FROM IMEX_PAT_DETAILS_VERSION_NEW WHERE PATIENT_ID = IMEX_PATIENT.OLD_PATIENT_ID "
                + "  ) OR EXISTS ( "
                + "  SELECT COUNT(*) FROM IMEX_PAT_DETAILS_VERSION_NEW WHERE PATIENT_ID = IMEX_PATIENT.OLD_PATIENT_ID "
                + "  " + minus() + " "
                + "  SELECT COUNT(*) FROM IMEX_PAT_DETAILS_VERSION_ACT WHERE PATIENT_ID = IMEX_PATIENT.OLD_PATIENT_ID "
                + "))");

        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_PATIENT 5)*/ IMEX_PATIENT SET NEW_VERSION = 1, VERSION_REASON = 'PAT_DETAILS_DIFF' WHERE NEW_VERSION = 0 AND IMEX_PATIENT.OLD_PATIENT_ID IS NOT NULL "
                + "  AND (EXISTS ("
                + "  SELECT * FROM IMEX_PAT_DETAILS_VERSION_ACT WHERE PATIENT_ID = IMEX_PATIENT.OLD_PATIENT_ID"
                + "  " + minus() + " "
                + "  SELECT * FROM IMEX_PAT_DETAILS_VERSION_NEW WHERE PATIENT_ID = IMEX_PATIENT.OLD_PATIENT_ID"
                + "  ) OR EXISTS ( "
                + "  SELECT * FROM IMEX_PAT_DETAILS_VERSION_NEW WHERE PATIENT_ID = IMEX_PATIENT.OLD_PATIENT_ID"
                + "  " + minus() + " "
                + "  SELECT * FROM IMEX_PAT_DETAILS_VERSION_ACT WHERE PATIENT_ID = IMEX_PATIENT.OLD_PATIENT_ID"
                + "))");

        //dropTable(pQry, "IMEX_PAT_DETAILS_VERSION_ACT");
        //dropTable(pQry, "IMEX_PAT_DETAILS_VERSION_NEW");
    }

    protected void checkNewInsuranceVersion(final Query pQry) throws IOException, SQLException, ParseException {
        pQry.executeUpdate("ALTER TABLE IMEX_PATIENT ADD NEW_INSURANCE_VERSION " + (isOracle() ? "NUMBER(1, 0)" : "BIT") + " DEFAULT 0 NOT NULL ");
        pQry.executeUpdate("ALTER TABLE IMEX_PATIENT ADD INSURANCE_VERSION_REASON VARCHAR(30)");
        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_PATIENT 5)*/ IMEX_PATIENT SET NEW_INSURANCE_VERSION = 1, INSURANCE_VERSION_REASON='NEW_PATIENT' WHERE IS_NEW_PATIENT = 1 ");
        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_PATIENT 5)*/ IMEX_PATIENT SET NEW_INSURANCE_VERSION = 1, INSURANCE_VERSION_REASON='NO_ACTUAL_INSURANCE' WHERE NEW_VERSION = 0 AND ACT_INS_ID IS NULL ");

        printTime("  Check differences in patient insurance...");
        checkPatientInsuranceVersion(pQry);
        printTime("  Differences in case patient insurance!");
    }

    protected void checkPatientInsuranceVersion(final Query pQry) throws IOException, SQLException, ParseException {
        dropTable(pQry, "IMEX_PAT_INSURANCE_VERSION_ACT");
        dropTable(pQry, "IMEX_PAT_INSURANCE_VERSION_NEW");

        getProgressor().sendProgress("Erzeuge IMEX_PAT_INSURANCE_VERSION_ACT");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_PAT_INSURANCE_VERSION_ACT NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT "
                + "  IMEX_PATIENT.OLD_PATIENT_ID PATIENT_ID,"
                + "  IMEX_PATIENT.ACT_INS_ID,"
                + "  INS.INS_INS_COMPANY, "
                + "  INS.INS_NUMBER "
                + (isOracle() ? "" : " INTO IMEX_PAT_INSURANCE_VERSION_ACT ")
                + " FROM T_INSURANCE INS "
                + " INNER JOIN IMEX_PATIENT ON IMEX_PATIENT.ACT_INS_ID = INS.ID "
                + " WHERE IMEX_PATIENT.ACT_INS_ID IS NOT NULL AND IMEX_PATIENT.OLD_PATIENT_ID IS NOT NULL AND IMEX_PATIENT.NEW_INSURANCE_VERSION = 0"); //INS.INS_INS_COMPANY IS NOT NULL AND 

        getProgressor().sendProgress("Erzeuge IMEX_PAT_INSURANCE_VERSION_NEW");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_PAT_INSURANCE_VERSION_NEW NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT "
                + "  IMEX_PATIENT.OLD_PATIENT_ID PATIENT_ID,"
                + "  IMEX_PATIENT.ACT_INS_ID,"
                + "  IMEX_PATIENT.KASSE INS_INS_COMPANY, "
                + "  IMEX_PATIENT.VERSICHERTENNR INS_NUMBER "
                + (isOracle() ? "" : " INTO IMEX_PAT_INSURANCE_VERSION_NEW ")
                + " FROM IMEX_PATIENT "
                + " WHERE IMEX_PATIENT.ACT_INS_ID IS NOT NULL AND IMEX_PATIENT.OLD_PATIENT_ID IS NOT NULL AND IMEX_PATIENT.NEW_INSURANCE_VERSION = 0"); //IMEX_PATIENT.KASSE IS NOT NULL AND 

        pQry.executeUpdate("CREATE INDEX PAT_INSURANCE_DETAILS_ACT_IDX ON IMEX_PAT_INSURANCE_VERSION_ACT (PATIENT_ID) " + parallel());

        pQry.executeUpdate("CREATE INDEX PAT_INSURANCE_DETAILS_NEW_IDX ON IMEX_PAT_INSURANCE_VERSION_NEW (PATIENT_ID) " + parallel());

        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_PATIENT 5)*/ IMEX_PATIENT SET NEW_INSURANCE_VERSION = 1, INSURANCE_VERSION_REASON = 'INS_DETAILS_COUNT' WHERE NEW_INSURANCE_VERSION = 0 AND IMEX_PATIENT.OLD_PATIENT_ID IS NOT NULL "
                + "  AND (EXISTS ("
                + "  SELECT COUNT(*) FROM IMEX_PAT_INSURANCE_VERSION_ACT WHERE PATIENT_ID = IMEX_PATIENT.OLD_PATIENT_ID "
                + "  " + minus() + " "
                + "  SELECT COUNT(*) FROM IMEX_PAT_INSURANCE_VERSION_NEW WHERE PATIENT_ID = IMEX_PATIENT.OLD_PATIENT_ID "
                + "  ) OR EXISTS ( "
                + "  SELECT COUNT(*) FROM IMEX_PAT_INSURANCE_VERSION_NEW WHERE PATIENT_ID = IMEX_PATIENT.OLD_PATIENT_ID "
                + "  " + minus() + " "
                + "  SELECT COUNT(*) FROM IMEX_PAT_INSURANCE_VERSION_ACT WHERE PATIENT_ID = IMEX_PATIENT.OLD_PATIENT_ID "
                + "))");

        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_PATIENT 5)*/ IMEX_PATIENT SET NEW_INSURANCE_VERSION = 1, INSURANCE_VERSION_REASON = 'INS_DETAILS_DIFF' WHERE NEW_INSURANCE_VERSION = 0 AND IMEX_PATIENT.OLD_PATIENT_ID IS NOT NULL "
                + "  AND (EXISTS ("
                + "  SELECT * FROM IMEX_PAT_INSURANCE_VERSION_ACT WHERE PATIENT_ID = IMEX_PATIENT.OLD_PATIENT_ID"
                + "  " + minus() + " "
                + "  SELECT * FROM IMEX_PAT_INSURANCE_VERSION_NEW WHERE PATIENT_ID = IMEX_PATIENT.OLD_PATIENT_ID"
                + "  ) OR EXISTS ( "
                + "  SELECT * FROM IMEX_PAT_INSURANCE_VERSION_NEW WHERE PATIENT_ID = IMEX_PATIENT.OLD_PATIENT_ID"
                + "  " + minus() + " "
                + "  SELECT * FROM IMEX_PAT_INSURANCE_VERSION_ACT WHERE PATIENT_ID = IMEX_PATIENT.OLD_PATIENT_ID"
                + "))");

        //dropTable(pQry, "IMEX_PAT_INSURANCE_VERSION_ACT");
        //dropTable(pQry, "IMEX_PAT_INSURANCE_VERSION_NEW");
    }

    private int getCaseDeleteCount(final Query pQry) throws SQLException {
        ResultSet rs1 = pQry.executeQuery("SELECT COUNT(*) CNT FROM IMEX_DROP_HOSC_IDS");
        int numberOfCases = 0;
        while (rs1.next()) {
            numberOfCases = rs1.getInt("CNT");
        }
        return numberOfCases;
    }

    protected int getImexCaseCount(final Query pQry) throws SQLException {
        ResultSet rs1 = pQry.executeQuery("SELECT COUNT(*) CNT FROM IMEX_CASE");
        int numberOfCases = 0;
        while (rs1.next()) {
            numberOfCases = rs1.getInt("CNT");
        }
        return numberOfCases;
    }

    protected int getImexCaseIgnoredCount(final Query pQry) throws SQLException {
        ResultSet rs1 = pQry.executeQuery("SELECT COUNT(*) CNT FROM IMEX_CASE WHERE IGNORE_CASE = 1");
        int numberOfIgnoredCases = 0;
        while (rs1.next()) {
            numberOfIgnoredCases = rs1.getInt("CNT");
        }
        return numberOfIgnoredCases;
    }

    protected int getCaseCount(final Query pQry) throws SQLException {
        ResultSet rs1 = pQry.executeQuery("SELECT COUNT(*) CNT FROM T_CASE");
        int numberOfCases = 0;
        while (rs1.next()) {
            numberOfCases = rs1.getInt("CNT");
        }
        return numberOfCases;
    }

    protected int getPatientCount(final Query pQry) throws SQLException {
        ResultSet rs1 = pQry.executeQuery("SELECT COUNT(*) CNT FROM T_PATIENT");
        int numberOfCases = 0;
        while (rs1.next()) {
            numberOfCases = rs1.getInt("CNT");
        }
        return numberOfCases;
    }

    protected void printPatientVersionInformations(final Query pQry) throws SQLException {
        ResultSet rs4 = pQry.executeQuery("SELECT COUNT(*) CNT FROM IMEX_PATIENT");
        int numberOfPatients = 0;
        while (rs4.next()) {
            numberOfPatients = rs4.getInt("CNT");
        }
        printTime("There are already " + getPatientCount(pQry) + " patients in the database at the moment");
        ResultSet rs5 = pQry.executeQuery("SELECT COUNT(*) CNT FROM IMEX_PATIENT WHERE OLD_PATIENT_ID IS NULL");
        StringBuilder sb = new StringBuilder();
        //printTime(numberOfPatients + " patients where found to import in IMEX_PATIENT:");
        sb.append(numberOfPatients + " patients where found to import in IMEX_PATIENT:");
        while (rs5.next()) {
            //printTime("  * " + rs5.getInt("CNT") + " of them are new to me");
            sb.append("\n  * " + rs5.getInt("CNT") + " of them are new to me");
        }
        ResultSet rs6 = pQry.executeQuery("SELECT NEW_VERSION, " + isNull("VERSION_REASON", "NO_DIFFERENCES_FOUND") + " VERSION_REASON, COUNT(*) CNT FROM IMEX_PATIENT WHERE IMEX_PATIENT.OLD_PATIENT_ID IS NOT NULL GROUP BY VERSION_REASON, NEW_VERSION ORDER BY 1, 2");
        while (rs6.next()) {
            //printTime("  * " + rs6.getInt("CNT") + " of them will " + (rs6.getBoolean("NEW_VERSION") ? "" : "NOT") + " get a new patient details version because of " + rs6.getString("VERSION_REASON"));
            sb.append("\n  * " + rs6.getInt("CNT") + " of them will " + (rs6.getBoolean("NEW_VERSION") ? "" : "NOT") + " get a new patient details version because of " + rs6.getString("VERSION_REASON"));
        }
        ResultSet rs7 = pQry.executeQuery("SELECT NEW_INSURANCE_VERSION, " + isNull("INSURANCE_VERSION_REASON", "NO_DIFFERENCES_FOUND") + " INSURANCE_VERSION_REASON, COUNT(*) CNT FROM IMEX_PATIENT WHERE IMEX_PATIENT.OLD_PATIENT_ID IS NOT NULL GROUP BY INSURANCE_VERSION_REASON, NEW_INSURANCE_VERSION ORDER BY 1, 2");
        while (rs7.next()) {
            //printTime("  * " + rs7.getInt("CNT") + " of them will " + (rs7.getBoolean("NEW_INSURANCE_VERSION") ? "" : "NOT") + " get a new patient insurance version because of " + rs7.getString("INSURANCE_VERSION_REASON"));
            sb.append("\n  * " + rs7.getInt("CNT") + " of them will " + (rs7.getBoolean("NEW_INSURANCE_VERSION") ? "" : "NOT") + " get a new patient insurance version because of " + rs7.getString("INSURANCE_VERSION_REASON"));
        }
        printTime(sb.toString());
    }

    protected void printCaseVersionInformations(final Query pQry) throws SQLException {
        ResultSet rs1 = pQry.executeQuery("SELECT COUNT(*) CNT FROM IMEX_CASE");
        int numberOfCases = 0;
        while (rs1.next()) {
            numberOfCases = rs1.getInt("CNT");
        }
        printTime("There are already " + getCaseCount(pQry) + " hospital cases in the database at the moment");
        //printTime(numberOfCases + " hospital cases where found to import in IMEX_CASE:");
        StringBuilder sb = new StringBuilder();
        sb.append(numberOfCases + " hospital cases where found to import in IMEX_CASE:");
        ResultSet rs2 = pQry.executeQuery("SELECT COUNT(*) CNT FROM IMEX_CASE WHERE OLD_CASE_ID IS NULL"); // AND NEW_STORNIERT_IMPORT = 1
        while (rs2.next()) {
            //printTime("  * " + rs2.getInt("CNT") + " of them are new to me");
            sb.append("\n  * " + rs2.getInt("CNT") + " of them are new to me");
        }
        ResultSet rs3 = pQry.executeQuery("SELECT NEW_VERSION, " + isNull("VERSION_REASON", "NO_DIFFERENCES_FOUND") + " VERSION_REASON, COUNT(*) CNT FROM IMEX_CASE WHERE IMEX_CASE.OLD_CASE_ID IS NOT NULL GROUP BY VERSION_REASON, NEW_VERSION ORDER BY 1, 2"); // AND NEW_STORNIERT_IMPORT = 1
        while (rs3.next()) {
            //printTime("  * " + rs3.getInt("CNT") + " of them will " + (rs3.getBoolean("NEW_VERSION") ? "" : "NOT") + " get a new case details version because of " + rs3.getString("VERSION_REASON"));
            sb.append("\n  * " + rs3.getInt("CNT") + " of them will " + (rs3.getBoolean("NEW_VERSION") ? "" : "NOT") + " get a new case details version because of " + rs3.getString("VERSION_REASON"));
        }
//        ResultSet rs4 = pQry.executeQuery("SELECT COUNT(*) CNT FROM IMEX_CASE WHERE NEW_STORNIERT_IMPORT = 0");
//        if (rs4.getInt("CNT") > 0) {
//            while (rs4.next()) {
//                //printTime("  * " + rs2.getInt("CNT") + " of them are new to me");
//                sb.append("\n  * BUT: " + rs4.getInt("CNT") + " of them will NOT be imported because they are already canceled");
//            }
//        }
        printTime(sb.toString());
    }

    protected void determineCasesToIgnore(final Query pQry) throws SQLException {
        printTime("Create IMEX_IGNORE_CASES...");
        getProgressor().sendProgress("Erzeuge IMEX_IGNORE_CASES");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_IGNORE_CASES NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT "
                + "   NR, IKZ, FALLNR, CAST(NULL AS VARCHAR(255)) REASON "
                + (isOracle() ? "" : " INTO IMEX_IGNORE_CASES ")
                + " FROM IMEX_CASE_TMP WHERE 0=1 ");

        int alreadyCanceledCases = pQry.executeUpdate("INSERT /*+ PARALLEL(IMEX_IGNORE_CASES 5)*/ INTO IMEX_IGNORE_CASES SELECT NR, IKZ, FALLNR, 'Already canceled hospital cases' FROM IMEX_CASE WHERE IMEX_CASE.ACT_STORNIERT = 1 AND IMEX_CASE.NEW_STORNIERT = 1 AND NOT EXISTS (SELECT 1 FROM IMEX_IGNORE_CASES WHERE IMEX_IGNORE_CASES.NR = IMEX_CASE.NR)");
        printTime(alreadyCanceledCases + " hospital cases marked as already canceled");

        getProgressor().sendProgress("Fülle IMEX_IGNORE_CASES");
        //false, because this solution is quite slow (missing indexes? wrong place here?)
        final boolean checkDuplicates = false;
        if (checkDuplicates) {

            //qry.executeUpdate("ALTER TABLE IMEX_CASE_TMP ADD DUPLICATE " + (isOracle() ? "NUMBER(1, 0)" : "BIT") + " DEFAULT 0 NOT NULL ");
            //int c = qry.executeUpdate("UPDATE IMEX_CASE_TMP SET DUPLICATE = 1 WHERE EXISTS (SELECT IKZ, FALLNR FROM IMEX_CASE_TMP TMP WHERE TMP.IKZ = IMEX_CASE_TMP.IKZ AND TMP.FALLNR = IMEX_CASE_TMP.FALLNR GROUP BY IKZ, FALLNR HAVING COUNT(*) > 1) ");
            //qry.executeUpdate("ALTER TABLE IMEX_CASE_TMP ADD IGNORE_REASON VARCHAR(50) ");
            //int c = qry.executeUpdate("UPDATE IMEX_CASE_TMP SET IGNORE_REASON = 'Duplicated hospital cases' WHERE EXISTS (SELECT IKZ, FALLNR FROM IMEX_CASE_TMP TMP WHERE TMP.IKZ = IMEX_CASE_TMP.IKZ AND TMP.FALLNR = IMEX_CASE_TMP.FALLNR GROUP BY IKZ, FALLNR HAVING COUNT(*) > 1) ");
            //int c = pQry.executeUpdate("INSERT /*+ PARALLEL(IMEX_IGNORE_CASES 5)*/ INTO IMEX_IGNORE_CASES SELECT NR, IKZ, FALLNR, 'Duplicated hospital cases' FROM IMEX_CASE_TMP WHERE EXISTS (SELECT IKZ, FALLNR FROM IMEX_CASE_TMP TMP WHERE TMP.IKZ = IMEX_CASE_TMP.IKZ AND TMP.FALLNR = IMEX_CASE_TMP.FALLNR GROUP BY IKZ, FALLNR HAVING COUNT(*) > 1) ");
            int c = pQry.executeUpdate("INSERT /*+ PARALLEL(IMEX_IGNORE_CASES 5)*/ INTO IMEX_IGNORE_CASES SELECT NR, IKZ, FALLNR, 'Duplicated hospital cases' FROM IMEX_CASE WHERE EXISTS (SELECT 1 FROM IMEX_CASE TMP WHERE TMP.IKZ = IMEX_CASE.IKZ AND TMP.FALLNR = IMEX_CASE.FALLNR AND TMP.NR <> IMEX_CASE.NR)");

            printTime(c + " hospital cases marked as duplicate");
        }

        final License license = getLicense();
        if (license == null) {
            LOG.log(Level.SEVERE, "Why is there no license in CpxDistributor?! That should not happen!");
        } else {
            if (!license.isDrgModule()) {
                int missingLicenseDrgCases = pQry.executeUpdate("INSERT /*+ PARALLEL(IMEX_IGNORE_CASES 5)*/ INTO IMEX_IGNORE_CASES SELECT NR, IKZ, FALLNR, 'Missing license for DRG cases' FROM IMEX_CASE WHERE IMEX_CASE.FALLART = 'DRG' AND NOT EXISTS (SELECT 1 FROM IMEX_IGNORE_CASES WHERE IMEX_IGNORE_CASES.NR = IMEX_CASE.NR)");
                printTime(missingLicenseDrgCases + " hospital cases marked as missing license for DRG");
            }
            if (!license.isPeppModule()) {
                int missingLicensePeppCases = pQry.executeUpdate("INSERT /*+ PARALLEL(IMEX_IGNORE_CASES 5)*/ INTO IMEX_IGNORE_CASES SELECT NR, IKZ, FALLNR, 'Missing license for PEPP cases' FROM IMEX_CASE WHERE IMEX_CASE.FALLART = 'PEPP' AND NOT EXISTS (SELECT 1 FROM IMEX_IGNORE_CASES WHERE IMEX_IGNORE_CASES.NR = IMEX_CASE.NR)");
                printTime(missingLicensePeppCases + " hospital cases marked as missing license for PEPP");
            }
            if (!license.isAllowAllHospitals()) {
                //2019-09-30 DNi - Ticket CPX-724: ignore cases with unlicensed hospital identifiers
                StringBuilder sb = new StringBuilder();
                for (String ikz : license.getHospList()) {
                    if (sb.length() > 0) {
                        sb.append(" AND");
                    }
                    sb.append(" IMEX_CASE.IKZ <> '" + ikz + "'");
                }
                int missingLicenseIkzCases = pQry.executeUpdate("INSERT /*+ PARALLEL(IMEX_IGNORE_CASES 5)*/ INTO IMEX_IGNORE_CASES SELECT NR, IKZ, FALLNR, 'Missing license for this hospital case IKZ' FROM IMEX_CASE WHERE (" + sb.toString() + ") AND NOT EXISTS (SELECT 1 FROM IMEX_IGNORE_CASES WHERE IMEX_IGNORE_CASES.NR = IMEX_CASE.NR)");
                printTime(missingLicenseIkzCases + " hospital cases marked as missing license for IKZ");
            }
            if (license.getCaseLimit() != null) {
                //2020-01-02 DNi: check if case limit is reached
                pQry.executeUpdate("ALTER TABLE IMEX_CASE ADD CASE_LIMIT_NUMBER " + (isOracle() ? "NUMBER(19, 0)" : "BIGINT")); // + " DEFAULT 0 NOT NULL ");
                //add sequential number
                pQry.executeUpdate("UPDATE IMEX_CASE SET CASE_LIMIT_NUMBER = (SELECT COUNT(*) FROM IMEX_CASE IC WHERE IC.NR <= IMEX_CASE.NR)");
                //int caseLimitReachedCases = pQry.executeUpdate("INSERT /*+ PARALLEL(IMEX_IGNORE_CASES 5)*/ INTO IMEX_IGNORE_CASES SELECT NR, IKZ, FALLNR, 'License case limit of " + license.getCaseLimit() + " reached' FROM (SELECT IMEX_CASE.*, " + (isOracle() ? "ROWNUM" : "ROWNUM = ROW_NUMBER() OVER (ORDER BY NR)") + " FROM IMEX_CASE WHERE IMEX_CASE.OLD_CASE_ID IS NULL AND NOT EXISTS (SELECT 1 FROM IMEX_IGNORE_CASES WHERE IMEX_IGNORE_CASES.NR = IMEX_CASE.NR)" + (isOracle() ? " ORDER BY IKZ, FALLNR": "") + ") IMEX_CASE WHERE ROWNUM > " + license.getCaseLimit() + " - (SELECT COUNT(*) FROM T_CASE)");
                int caseLimitReachedCases = pQry.executeUpdate("INSERT /*+ PARALLEL(IMEX_IGNORE_CASES 5)*/ INTO IMEX_IGNORE_CASES SELECT NR, IKZ, FALLNR, 'License case limit of " + license.getCaseLimit() + " reached' FROM IMEX_CASE WHERE IMEX_CASE.OLD_CASE_ID IS NULL AND NOT EXISTS (SELECT 1 FROM IMEX_IGNORE_CASES WHERE IMEX_IGNORE_CASES.NR = IMEX_CASE.NR) AND CASE_LIMIT_NUMBER > " + license.getCaseLimit() + " - (SELECT COUNT(*) FROM T_CASE)");
                printTime(caseLimitReachedCases + " hospital cases marked as becase case limit of " + license.getCaseLimit() + " is reached");
            }
        }

        final Date admissionDateFrom = getImportConstraint().getAdmissionDateFrom();
        final Date admissionDateTo = getImportConstraint().getAdmissionDateTo();

        final Date dischargeDateFrom = getImportConstraint().getDischargeDateFrom();
        final Date dischargeDateTo = getImportConstraint().getDischargeDateTo();

        final String[] admissionCauses = getImportConstraint().getAdmissionCauseValues();
        final String[] admissionReasons = getImportConstraint().getAdmissionReasonValues();
        final String[] icdTypes = getImportConstraint().getIcdTypeValues();
        final String[] caseNumbers = getImportConstraint().getCaseNumberValues();

        if (admissionDateFrom != null) {
            final String dateStr = Lang.toIsoDate(admissionDateFrom);
            int affectedCases = pQry.executeUpdate("INSERT /*+ PARALLEL(IMEX_IGNORE_CASES 5)*/ INTO IMEX_IGNORE_CASES SELECT NR, IKZ, FALLNR, 'Admission date is older than " + dateStr + "' FROM IMEX_CASE WHERE IMEX_CASE.AUFNAHMEDATUM < " + StrUtils.toStaticDate(admissionDateFrom, isSqlSrv()) + " AND NOT EXISTS (SELECT 1 FROM IMEX_IGNORE_CASES WHERE IMEX_IGNORE_CASES.NR = IMEX_CASE.NR)");
            printTime(affectedCases + " hospital cases marked because admission date older than " + dateStr);
        }

        if (admissionDateTo != null) {
            final Date admissionDateToPlus1d = getImportConstraint().getAdmissionDateTo(1);
            final String dateStr = Lang.toIsoDate(admissionDateTo);
            int affectedCases = pQry.executeUpdate("INSERT /*+ PARALLEL(IMEX_IGNORE_CASES 5)*/ INTO IMEX_IGNORE_CASES SELECT NR, IKZ, FALLNR, 'Admission date is newer than " + dateStr + "' FROM IMEX_CASE WHERE IMEX_CASE.AUFNAHMEDATUM >= " + StrUtils.toStaticDate(admissionDateToPlus1d, isSqlSrv()) + " AND NOT EXISTS (SELECT 1 FROM IMEX_IGNORE_CASES WHERE IMEX_IGNORE_CASES.NR = IMEX_CASE.NR)");
            printTime(affectedCases + " hospital cases marked because admission date newer than " + dateStr);
        }

        if (dischargeDateFrom != null) {
            final String dateStr = Lang.toIsoDate(dischargeDateFrom);
            int affectedCases = pQry.executeUpdate("INSERT /*+ PARALLEL(IMEX_IGNORE_CASES 5)*/ INTO IMEX_IGNORE_CASES SELECT NR, IKZ, FALLNR, 'Discharge date is older than " + dateStr + "' FROM IMEX_CASE WHERE IMEX_CASE.ENTLASSUNGSDATUM < " + StrUtils.toStaticDate(dischargeDateFrom, isSqlSrv()) + " AND NOT EXISTS (SELECT 1 FROM IMEX_IGNORE_CASES WHERE IMEX_IGNORE_CASES.NR = IMEX_CASE.NR)");
            printTime(affectedCases + " hospital cases marked because discharge date older than " + dateStr);
        }

        if (dischargeDateTo != null) {
            final Date dischargeDateToPlus1d = getImportConstraint().getDischargeDateTo(1);
            final String dateStr = Lang.toIsoDate(dischargeDateTo);
            int affectedCases = pQry.executeUpdate("INSERT /*+ PARALLEL(IMEX_IGNORE_CASES 5)*/ INTO IMEX_IGNORE_CASES SELECT NR, IKZ, FALLNR, 'Admission date is newer than " + dateStr + "' FROM IMEX_CASE WHERE IMEX_CASE.ENTLASSUNGSDATUM >= " + StrUtils.toStaticDate(dischargeDateToPlus1d, isSqlSrv()) + " AND NOT EXISTS (SELECT 1 FROM IMEX_IGNORE_CASES WHERE IMEX_IGNORE_CASES.NR = IMEX_CASE.NR)");
            printTime(affectedCases + " hospital cases marked because discharge date newer than " + dateStr);
        }

        if (admissionCauses.length > 0) {
            StringBuilder sb = new StringBuilder();
            final String delim = "IMEX_CASE.AUFNAHMEANLASS = ";

            for (String value : admissionCauses) {
                if (sb.length() > 0) {
                    sb.append(" OR ");
                }
                sb.append(delim + "'" + value + "'");
            }
            final String tmp = sb.toString();
            int affectedCases = pQry.executeUpdate("INSERT /*+ PARALLEL(IMEX_IGNORE_CASES 5)*/ INTO IMEX_IGNORE_CASES SELECT NR, IKZ, FALLNR, 'Admission cause is not in " + StringUtils.join(admissionCauses, ",") + "' FROM IMEX_CASE WHERE NOT (" + tmp + ") AND NOT EXISTS (SELECT 1 FROM IMEX_IGNORE_CASES WHERE IMEX_IGNORE_CASES.NR = IMEX_CASE.NR)");
            printTime(affectedCases + " hospital cases marked because admission cause is not in " + StringUtils.join(admissionCauses, ","));
        }

        if (admissionReasons.length > 0) {
            StringBuilder sb = new StringBuilder();
            final String delim = "IMEX_CASE.AUFNAHMEGRUND1 = ";

            for (String value : admissionReasons) {
                if (sb.length() > 0) {
                    sb.append(" OR ");
                }
                sb.append(delim + "'" + value + "'");
            }
            final String tmp = sb.toString();
            int affectedCases = pQry.executeUpdate("INSERT /*+ PARALLEL(IMEX_IGNORE_CASES 5)*/ INTO IMEX_IGNORE_CASES SELECT NR, IKZ, FALLNR, 'Admission reason 1/2 is not in " + StringUtils.join(admissionReasons, ",") + "' FROM IMEX_CASE WHERE NOT (" + tmp + ") AND NOT EXISTS (SELECT 1 FROM IMEX_IGNORE_CASES WHERE IMEX_IGNORE_CASES.NR = IMEX_CASE.NR)");
            printTime(affectedCases + " hospital cases marked because admission cause is not in " + StringUtils.join(admissionReasons, ","));
        }

        if (icdTypes.length > 0) {
            StringBuilder sb = new StringBuilder();
            final String delim = "IMEX_DIAGNOSE.ICD_TYPE = ";

            for (String value : icdTypes) {
                if (sb.length() > 0) {
                    sb.append(" OR ");
                }
                sb.append(delim + "'" + value + "'");
            }
            final String tmp = sb.toString();
            int affectedCases = pQry.executeUpdate("INSERT /*+ PARALLEL(IMEX_IGNORE_CASES 5)*/ INTO IMEX_IGNORE_CASES SELECT NR, IKZ, FALLNR, 'ICD type is not in " + StringUtils.join(icdTypes, ",") + "' FROM IMEX_CASE WHERE EXISTS (SELECT 1 FROM IMEX_DIAGNOSE WHERE IMEX_DIAGNOSE.IKZ = IMEX_CASE.IKZ AND IMEX_DIAGNOSE.FALLNR = IMEX_CASE.FALLNR AND (IMEX_DIAGNOSE.ICD_TYPE IS NOT NULL OR NOT (" + tmp + "))) AND NOT EXISTS (SELECT 1 FROM IMEX_IGNORE_CASES WHERE IMEX_IGNORE_CASES.NR = IMEX_CASE.NR)");
            printTime(affectedCases + " hospital cases marked because icd type is not in " + StringUtils.join(icdTypes, ","));
        }

        if (caseNumbers.length > 0) {
            StringBuilder sb = new StringBuilder();
            final String delim = "IMEX_CASE.FALLNR = ";

            for (String value : caseNumbers) {
                if (sb.length() > 0) {
                    sb.append(" OR ");
                }
                sb.append(delim + "'" + value + "'");
            }
            final String tmp = sb.toString();
            int affectedCases = pQry.executeUpdate("INSERT /*+ PARALLEL(IMEX_IGNORE_CASES 5)*/ INTO IMEX_IGNORE_CASES SELECT NR, IKZ, FALLNR, 'Case number is not in list' FROM IMEX_CASE WHERE NOT (" + tmp + ") AND NOT EXISTS (SELECT 1 FROM IMEX_IGNORE_CASES WHERE IMEX_IGNORE_CASES.NR = IMEX_CASE.NR)");
            printTime(affectedCases + " hospital cases marked because case number is not in " + StringUtils.join(caseNumbers, ","));
        }

        pQry.executeUpdate("CREATE INDEX IDX_IMEX_IGNORE_CASES_FALL ON IMEX_IGNORE_CASES (IKZ, FALLNR) " + parallel());
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_IGNORE_CASES_NR ON IMEX_IGNORE_CASES (NR) " + parallel());

        printTime("IMEX_IGNORE_CASES created!");

        pQry.executeUpdate("ALTER TABLE IMEX_CASE ADD IGNORE_CASE " + (isOracle() ? "NUMBER(1, 0)" : "BIT") + " DEFAULT 0 NOT NULL ");
        int casesToIgnore = pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_CASE 5)*/ IMEX_CASE SET IGNORE_CASE = 1 WHERE IGNORE_CASE = 0 AND EXISTS (SELECT 1 FROM IMEX_IGNORE_CASES WHERE IMEX_IGNORE_CASES.NR = IMEX_CASE.NR)");
        printTime(casesToIgnore + " hospital cases marked to ignore");
    }

    protected void printCaseIgnoreInformations(final Query pQry) throws SQLException {
        ResultSet rs1 = pQry.executeQuery("SELECT COUNT(*) CNT FROM IMEX_IGNORE_CASES");
        int casesToIgnore = 0;
        while (rs1.next()) {
            casesToIgnore = rs1.getInt("CNT");
        }

        if (casesToIgnore == 0) {
            printTime("No cases were marked to ignore in IMEX_IGNORE_CASES");
            return;
        }

        StringBuilder sb = new StringBuilder();
        //printTime(numberOfPatients + " patients where found to import in IMEX_PATIENT:");
        //printTime(numberOfPatients + " patients where found to import in IMEX_PATIENT:");
        sb.append(casesToIgnore + " hospital cases marked to ignore in IMEX_IGNORE_CASES:");
        ResultSet rs2 = pQry.executeQuery("SELECT REASON, COUNT(*) CNT FROM IMEX_IGNORE_CASES GROUP BY REASON");
        while (rs2.next()) {
            //printTime("  * " + rs6.getInt("CNT") + " of them will " + (rs6.getBoolean("NEW_VERSION") ? "" : "NOT") + " get a new patient details version because of " + rs6.getString("VERSION_REASON"));
            sb.append("\n  * " + rs2.getInt("CNT") + " of them because of " + rs2.getString("REASON"));
        }
        printTime(sb.toString());
    }

    protected void printCaseStornoInformations(final Query pQry) throws SQLException {
        ResultSet rs1 = pQry.executeQuery("SELECT COUNT(*) CNT FROM IMEX_CASE");
        int numberOfCases = 0;
        while (rs1.next()) {
            numberOfCases = rs1.getInt("CNT");
        }
        //printTime("There are already " + getCaseCount(pQry) + " hospital cases in the database at the moment");
        //printTime(numberOfCases + " hospital cases where found to import in IMEX_CASE:");
        StringBuilder sb = new StringBuilder();
        sb.append("Result of cancellation flags for " + numberOfCases + " hospital cases that where found to import in IMEX_CASE:");
//        ResultSet rs2 = pQry.executeQuery("SELECT COUNT(*) CNT FROM IMEX_CASE WHERE OLD_CASE_ID IS NULL");
//        while (rs2.next()) {
//            //printTime("  * " + rs2.getInt("CNT") + " of them are new to me");
//            sb.append("\n  * " + rs2.getInt("CNT") + " of them are new to me");
//        }
        ResultSet rs2 = pQry.executeQuery("SELECT ACT_STORNIERT, STORNIERT, NEW_STORNIERT, COUNT(*) CNT FROM IMEX_CASE GROUP BY ACT_STORNIERT, STORNIERT, NEW_STORNIERT ORDER BY 1, 2, 3");
        final int columnWidth = 17;
        sb.append(
                "\n"
                + StrUtils.centerPad("Number of cases", columnWidth)
                + StrUtils.centerPad("CPX CANCEL_FL", columnWidth)
                + StrUtils.centerPad("KIS CANCEL_FL", columnWidth)
                + "     "
                //                + StrUtils.centerPad("Import case?", columnWidth)
                + StrUtils.centerPad("NEW CPX CANCEL_FL", columnWidth)
        );
        while (rs2.next()) {
            Integer actStorniert = rs2.getInt("ACT_STORNIERT");
            if (rs2.wasNull()) {
                actStorniert = null;
            }
            Integer storniert = rs2.getInt("STORNIERT");
            if (rs2.wasNull()) {
                storniert = null;
            }
//            Integer newStorniertImport = rs2.getInt("NEW_STORNIERT_IMPORT");
//            if (rs2.wasNull()) {
//                newStorniertImport = null;
//            }
            Integer newStorniert = rs2.getInt("NEW_STORNIERT");
            if (rs2.wasNull()) {
                newStorniert = null;
            }
            //printTime("  * " + rs3.getInt("CNT") + " of them will " + (rs3.getBoolean("NEW_VERSION") ? "" : "NOT") + " get a new case details version because of " + rs3.getString("VERSION_REASON"));
            sb.append(
                    "\n"
                    + StrUtils.centerPad(rs2.getInt("CNT") + "", columnWidth)
                    + StrUtils.centerPad(actStorniert + "", columnWidth)
                    + StrUtils.centerPad(storniert + "", columnWidth)
                    + " => "
                    //                    + StrUtils.centerPad(newStorniertImport + "", columnWidth)
                    + StrUtils.centerPad(newStorniert + "", columnWidth)
            );
        }
        printTime(sb.toString());
    }

    protected void checkNewCaseVersion(final Query pQry) throws IOException, SQLException, ParseException {
        pQry.executeUpdate("ALTER TABLE IMEX_CASE ADD NEW_VERSION " + (isOracle() ? "NUMBER(1, 0)" : "BIT") + " DEFAULT 0 NOT NULL ");
        pQry.executeUpdate("ALTER TABLE IMEX_CASE ADD VERSION_REASON VARCHAR(30)");
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_CASE_NEW ON IMEX_CASE (NEW_VERSION, IS_NEW_CASE)");
        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_CASE 5)*/ IMEX_CASE SET NEW_VERSION = 1, VERSION_REASON='NEW_CASE' WHERE NEW_VERSION = 0 AND IS_NEW_CASE = 1 ");
        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_CASE 5)*/ IMEX_CASE SET NEW_VERSION = 1, VERSION_REASON='NO_ACTUAL_EXT_DETAILS' WHERE NEW_VERSION = 0 AND ACT_HOSD_EXTERN_ID IS NULL ");
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_CASE_FALL_C_ID ON IMEX_CASE (NEW_VERSION, ACT_HOSD_EXTERN_ID, OLD_CASE_ID) " + parallel());
        //pQry.executeUpdate("CREATE INDEX IDX_IMEX_CASE_NEW_VERSION ON IMEX_CASE (NEW_VERSION)");

        printTime("  Check differences in case cancel status...");
        checkCaseUncanceledVersion(pQry);
        printTime("  Differences in case cancel status checked!");

        printTime("  Check differences in case details...");
        checkCaseDetailsVersion(pQry);
        printTime("  Differences in case details checked!");

        printTime("  Check differences in case departments...");
        checkCaseDepartmentVersion(pQry);
        printTime("  Differences in case departments checked!");

        printTime("  Check differences in case wards...");
        checkCaseWardVersion(pQry);
        printTime("  Differences in case wards checked!");

        printTime("  Check differences in case diagnosis...");
        checkCaseDiagnosisVersion(pQry);
        printTime("  Differences in case diagnosis checked!");

        printTime("  Check differences in case procedures...");
        checkCaseProcedureVersion(pQry);
        printTime("  Differences in case procedures checked!");

        printTime("  Check differences in case fees...");
        checkCaseFeeVersion(pQry);
        printTime("  Differences in case fees checked!");
    }

    protected void checkCaseUncanceledVersion(final Query pQry) throws IOException, SQLException, ParseException {
        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_CASE 5)*/ IMEX_CASE SET NEW_VERSION = 1, VERSION_REASON = 'EXISTING_CASE_UNCANCELED' WHERE NEW_VERSION = 0 AND IMEX_CASE.OLD_CASE_ID IS NOT NULL AND IMEX_CASE.ACT_STORNIERT = 1 AND IMEX_CASE.NEW_STORNIERT = 0 AND IMEX_CASE.IGNORE_CASE = 0");
    }

    protected void checkCaseDetailsVersion(final Query pQry) throws IOException, SQLException, ParseException {
        dropTable(pQry, "IMEX_CASE_DETAILS_VERSION_ACT");
        dropTable(pQry, "IMEX_CASE_DETAILS_VERSION_NEW");

        getProgressor().sendProgress("Erzeuge IMEX_CASE_DETAILS_VERSION_ACT");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_CASE_DETAILS_VERSION_ACT NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT "
                + "  IMEX_CASE.OLD_CASE_ID CASE_ID,"
                + "  IMEX_CASE.ACT_HOSD_EXTERN_ID,"
                + "  HOSD.ADMISSION_CAUSE_EN, "
                + "  HOSD.ADMISSION_REASON_12_EN, "
                + "  HOSD.ADMISSION_REASON_34_EN, "
                + "  HOSD.ADMISSION_DATE, "
                + "  HOSD.ADMISSION_WEIGHT,"
                + "  HOSD.AGE_DAYS, "
                + "  HOSD.AGE_YEARS,"
                + "  HOSD.DISCHARGE_REASON_12_EN, "
                + "  HOSD.DISCHARGE_REASON_3_EN, "
                + "  HOSD.DISCHARGE_DATE, "
                + "  HOSD.HMV, "
                + "  HOSD.LOS_INTENSIV, "
                + "  HOSD.ADMISSION_LAW_EN, "
                + "  HOSD.HD_ICD_CODE, "
                + "  HOSD.LEAVE, "
                // + "  HOSD.LOS, "
                + "  HOSD.LOS_ALTERATION, "
                + "  HOSD.CSD_LOS_MD_ALTERATION, "
                + "  HOSD.SUM_OF_OPS, "
                + "  HOSD.SUM_OF_ICD, "
                + "  HOSD.ADMISSION_MODE_EN, "
                + "  HOSD.GENDER_EN "
                + (isOracle() ? "" : " INTO IMEX_CASE_DETAILS_VERSION_ACT ")
                + " FROM T_CASE_DETAILS HOSD "
                + " INNER JOIN IMEX_CASE ON IMEX_CASE.ACT_HOSD_EXTERN_ID = HOSD.ID"
                + " WHERE IMEX_CASE.ACT_HOSD_EXTERN_ID IS NOT NULL AND IMEX_CASE.OLD_CASE_ID IS NOT NULL AND IMEX_CASE.NEW_VERSION = 0 AND IMEX_CASE.IGNORE_CASE = 0");

        getProgressor().sendProgress("Erzeuge IMEX_CASE_DETAILS_VERSION_NEW");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_CASE_DETAILS_VERSION_NEW NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT "
                + "  IMEX_CASE.OLD_CASE_ID CASE_ID,"
                + "  IMEX_CASE.ACT_HOSD_EXTERN_ID,"
                + "  IMEX_CASE.AUFNAHMEANLASS ADMISSION_CAUSE_EN, "
                + "  IMEX_CASE.AUFNAHMEGRUND1 ADMISSION_REASON_12_EN, "
                + "  IMEX_CASE.AUFNAHMEGRUND2 ADMISSION_REASON_34_EN, "
                + "  IMEX_CASE.AUFNAHMEDATUM ADMISSION_DATE, "
                + "  IMEX_CASE.GEWICHT ADMISSION_WEIGHT,"
                + "  IMEX_CASE.ALTER_IN_TAGEN AGE_DAYS, "
                + "  IMEX_CASE.ALTER_IN_JAHREN AGE_YEARS, "
                + "  IMEX_CASE.ENTLASSUNGSGRUND12 DISCHARGE_REASON_12_EN, "
                + "  IMEX_CASE.ENTLASSUNGSGRUND3 DISCHARGE_REASON_3_EN, "
                + "  IMEX_CASE.ENTLASSUNGSDATUM DISCHARGE_DATE, "
                + "  IMEX_CASE.BEATMUNGSSTUNDEN HMV, "
                + "  IMEX_CASE.VWD_INTENSIV LOS_INTENSIV, "
                + "  IMEX_CASE.GESETZL_PSYCHSTATUS ADMISSION_LAW_EN, "
                + "  IMEX_CASE.MAIN_ICD_CODE HD_ICD_CODE, "
                + "  IMEX_CASE.URLAUBSTAGE LEAVE, "
                // + "  IMEX_CASE.VWD LOS, "
                + "  IMEX_CASE.VWD_SIMULIERT LOS_ALTERATION, "
                + "  IMEX_CASE.MD_TOB CSD_LOS_MD_ALTERATION, "
                + "  IMEX_CASE.SUM_OF_OPS, "
                + "  IMEX_CASE.SUM_OF_ICD, "
                + "  IMEX_CASE.ERBRINGUNGSART ADMISSION_MODE_EN, "
                + "  IMEX_CASE.NEW_GESCHLECHT GENDER_EN "
                + (isOracle() ? "" : " INTO IMEX_CASE_DETAILS_VERSION_NEW ")
                + " FROM IMEX_CASE"
                + " WHERE IMEX_CASE.ACT_HOSD_EXTERN_ID IS NOT NULL AND IMEX_CASE.OLD_CASE_ID IS NOT NULL AND IMEX_CASE.NEW_VERSION = 0 AND IMEX_CASE.IGNORE_CASE = 0");

        pQry.executeUpdate("CREATE INDEX VERSION_DETAILS_ACT_IDX ON IMEX_CASE_DETAILS_VERSION_ACT (CASE_ID) " + parallel());

        pQry.executeUpdate("CREATE INDEX VERSION_DETAILS_NEW_IDX ON IMEX_CASE_DETAILS_VERSION_NEW (CASE_ID) " + parallel());

        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_CASE 5)*/ IMEX_CASE SET NEW_VERSION = 1, VERSION_REASON = 'CASE_DETAILS_COUNT' WHERE NEW_VERSION = 0 AND IMEX_CASE.OLD_CASE_ID IS NOT NULL AND IMEX_CASE.IGNORE_CASE = 0 "
                + "  AND (EXISTS ("
                + "  SELECT COUNT(*) FROM IMEX_CASE_DETAILS_VERSION_ACT WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID "
                + "  " + minus() + " "
                + "  SELECT COUNT(*) FROM IMEX_CASE_DETAILS_VERSION_NEW WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID "
                + "  ) OR EXISTS ( "
                + "  SELECT COUNT(*) FROM IMEX_CASE_DETAILS_VERSION_NEW WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID "
                + "  " + minus() + " "
                + "  SELECT COUNT(*) FROM IMEX_CASE_DETAILS_VERSION_ACT WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID "
                + "))");

        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_CASE 5)*/ IMEX_CASE SET NEW_VERSION = 1, VERSION_REASON = 'CASE_DETAILS_DIFF' WHERE NEW_VERSION = 0 AND IMEX_CASE.OLD_CASE_ID IS NOT NULL AND IMEX_CASE.IGNORE_CASE = 0 "
                + "  AND (EXISTS ("
                + "  SELECT * FROM IMEX_CASE_DETAILS_VERSION_ACT WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID"
                + "  " + minus() + " "
                + "  SELECT * FROM IMEX_CASE_DETAILS_VERSION_NEW WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID"
                + "  ) OR EXISTS ( "
                + "  SELECT * FROM IMEX_CASE_DETAILS_VERSION_NEW WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID"
                + "  " + minus() + " "
                + "  SELECT * FROM IMEX_CASE_DETAILS_VERSION_ACT WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID"
                + "))");

        /*
pQry.executeUpdate("UPDATE IMEX_CASE SET NEW_VERSION = 1, VERSION_REASON = 'CASE_DETAILS_COUNT' WHERE NEW_VERSION = 0 AND IMEX_CASE.OLD_CASE_ID IN ("
+ "  SELECT CASE_ID FROM ("
+ "    SELECT TMP.CASE_ID, TMP.CNT_ACT, COUNT(*) CNT_NEW"
+ "    FROM ("
+ "      SELECT CASE_ID, COUNT(*) CNT_ACT"
+ "      FROM IMEX_CASE_DETAILS_VERSION_ACT"
+ "      GROUP BY CASE_ID"
+ "    ) TMP"
+ "    INNER JOIN IMEX_CASE_DETAILS_VERSION_NEW ON IMEX_CASE_DETAILS_VERSION_NEW.CASE_ID = TMP.CASE_ID"
+ "    GROUP BY TMP.CASE_ID, TMP.CNT_ACT, IMEX_CASE_DETAILS_VERSION_NEW.CASE_ID"
+ "  ) TMP2 WHERE CNT_ACT <> CNT_NEW"
+ ")");

pQry.executeUpdate("UPDATE IMEX_CASE SET NEW_VERSION = 1, VERSION_REASON = 'CASE_DETAILS_DIFF' WHERE NEW_VERSION = 0 AND IMEX_CASE.OLD_CASE_ID IN ("
+ "  SELECT CASE_ID "
+ "  FROM ( "
+ "    SELECT * FROM IMEX_CASE_DETAILS_VERSION_ACT"
+ "    MINUS "
+ "    SELECT * FROM IMEX_CASE_DETAILS_VERSION_NEW"
+ "    UNION ALL "
+ "    SELECT * FROM IMEX_CASE_DETAILS_VERSION_NEW"
+ "    MINUS "
+ "    SELECT * FROM IMEX_CASE_DETAILS_VERSION_ACT"
+ "  ) TMP "
+ ")");
         */
        //dropTable(pQry, "IMEX_CASE_DETAILS_VERSION_ACT");
        //dropTable(pQry, "IMEX_CASE_DETAILS_VERSION_NEW");
    }

    protected void checkCaseDepartmentVersion(final Query pQry) throws IOException, SQLException, ParseException {
        dropTable(pQry, "IMEX_CASE_DEP_VERSION_ACT");
        dropTable(pQry, "IMEX_CASE_DEP_VERSION_NEW");

        getProgressor().sendProgress("Erzeuge IMEX_CASE_DEP_VERSION_ACT");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_CASE_DEP_VERSION_ACT NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT "
                + "  IMEX_CASE.OLD_CASE_ID CASE_ID,"
                + "  IMEX_CASE.ACT_HOSD_EXTERN_ID,"
                + "  DEP.DEP_KEY_301, "
                + "  DEP.DEP_SHORT_NAME, "
                + "  DEP.DEPC_ADM_DATE, "
                + "  DEP.DEPC_ADMOD_EN, "
                + "  DEP.DEPC_DIS_DATE, "
                + "  DEP.DEPC_HMV, "
                + "  DEP.DEPC_IS_ADMISSION_FL,  "
                + "  DEP.DEPC_IS_DISCHARGE_FL, "
                + "  DEP.DEPC_IS_TREATING_FL, "
                + "  DEP.DEPC_IS_BED_INTENSIV_FL "
                + (isOracle() ? "" : " INTO IMEX_CASE_DEP_VERSION_ACT ")
                + " FROM T_CASE_DEPARTMENT DEP "
                + " INNER JOIN T_CASE_DETAILS HOSD ON HOSD.ID = DEP.T_CASE_DETAILS_ID"
                + " INNER JOIN IMEX_CASE ON IMEX_CASE.ACT_HOSD_EXTERN_ID = HOSD.ID"
                + " WHERE IMEX_CASE.ACT_HOSD_EXTERN_ID IS NOT NULL AND IMEX_CASE.OLD_CASE_ID IS NOT NULL AND IMEX_CASE.NEW_VERSION = 0 AND IMEX_CASE.IGNORE_CASE = 0");

        getProgressor().sendProgress("Erzeuge IMEX_CASE_DEP_VERSION_NEW");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_CASE_DEP_VERSION_NEW NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT "
                + "  IMEX_CASE.OLD_CASE_ID CASE_ID, "
                + "  IMEX_CASE.ACT_HOSD_EXTERN_ID, "
                + "  IMEX_DEPARTMENT_TMP.CODE DEP_KEY_301, "
                + "  IMEX_DEPARTMENT_TMP.CODE_INTERN DEP_SHORT_NAME, "
                + "  IMEX_DEPARTMENT_TMP.VERLEGUNGSDATUM DEPC_ADM_DATE, "
                + "  IMEX_DEPARTMENT_TMP.ERBRINGUNGSART DEPC_ADMOD_EN, "
                + "  IMEX_DEPARTMENT_TMP.ENTLASSUNGSDATUM DEPC_DIS_DATE, "
                + "  0 DEPC_HMV,"
                + isNull("IMEX_AUFNEHMENDE_FAB.AUFNEHMENDE_FAB", "0") + " DEPC_IS_ADMISSION_FL, "
                + isNull("IMEX_ENTLASSENDE_FAB.ENTLASSENDE_FAB", "0") + " DEPC_IS_DISCHARGE_FL, "
                + isNull("IMEX_BEHANDELNDE_FAB.BEHANDELNDE_FAB", "0") + " DEPC_IS_TREATING_FL, "
                + "  IMEX_DEPARTMENT_TMP.IS_BED_INTENSIV DEPC_IS_BED_INTENSIV_FL "
                + (isOracle() ? "" : " INTO IMEX_CASE_DEP_VERSION_NEW ")
                + " FROM IMEX_DEPARTMENT_TMP"
                + " INNER JOIN IMEX_CASE ON (IMEX_CASE.IKZ = IMEX_DEPARTMENT_TMP.IKZ AND IMEX_CASE.FALLNR = IMEX_DEPARTMENT_TMP.FALLNR)"
                + " LEFT JOIN IMEX_BEHANDELNDE_FAB ON IMEX_BEHANDELNDE_FAB.NR = IMEX_DEPARTMENT_TMP.NR "
                + " LEFT JOIN IMEX_AUFNEHMENDE_FAB ON IMEX_AUFNEHMENDE_FAB.NR = IMEX_DEPARTMENT_TMP.NR "
                + " LEFT JOIN IMEX_ENTLASSENDE_FAB ON IMEX_ENTLASSENDE_FAB.NR = IMEX_DEPARTMENT_TMP.NR "
                + " WHERE IMEX_CASE.ACT_HOSD_EXTERN_ID IS NOT NULL AND IMEX_CASE.OLD_CASE_ID IS NOT NULL AND IMEX_CASE.NEW_VERSION = 0 AND IMEX_CASE.IGNORE_CASE = 0");

        pQry.executeUpdate("CREATE INDEX VERSION_DEP_ACT_IDX ON IMEX_CASE_DEP_VERSION_ACT (CASE_ID) " + parallel());

        pQry.executeUpdate("CREATE INDEX VERSION_DEP_NEW_IDX ON IMEX_CASE_DEP_VERSION_NEW (CASE_ID) " + parallel());

        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_CASE 5)*/ IMEX_CASE SET NEW_VERSION = 1, VERSION_REASON = 'CASE_DEP_COUNT' WHERE NEW_VERSION = 0 AND IMEX_CASE.OLD_CASE_ID IS NOT NULL AND IMEX_CASE.IGNORE_CASE = 0 "
                + "  AND (EXISTS ("
                + "  SELECT COUNT(*) FROM IMEX_CASE_DEP_VERSION_ACT WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID "
                + "  " + minus() + " "
                + "  SELECT COUNT(*) FROM IMEX_CASE_DEP_VERSION_NEW WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID "
                + "  ) OR EXISTS ( "
                + "  SELECT COUNT(*) FROM IMEX_CASE_DEP_VERSION_NEW WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID "
                + "  " + minus() + " "
                + "  SELECT COUNT(*) FROM IMEX_CASE_DEP_VERSION_ACT WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID "
                + "))");

        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_CASE 5)*/ IMEX_CASE SET NEW_VERSION = 1, VERSION_REASON = 'CASE_DEP_DIFF' WHERE NEW_VERSION = 0 AND IMEX_CASE.OLD_CASE_ID IS NOT NULL AND IMEX_CASE.IGNORE_CASE = 0 "
                + "  AND (EXISTS ("
                + "  SELECT * FROM IMEX_CASE_DEP_VERSION_ACT WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID"
                + "  " + minus() + " "
                + "  SELECT * FROM IMEX_CASE_DEP_VERSION_NEW WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID"
                + "  ) OR EXISTS ( "
                + "  SELECT * FROM IMEX_CASE_DEP_VERSION_NEW WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID"
                + "  " + minus() + " "
                + "  SELECT * FROM IMEX_CASE_DEP_VERSION_ACT WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID"
                + "))");
    }

    protected void checkCaseWardVersion(final Query pQry) throws IOException, SQLException, ParseException {
        dropTable(pQry, "IMEX_CASE_WARD_VERSION_ACT");
        dropTable(pQry, "IMEX_CASE_WARD_VERSION_NEW");

        getProgressor().sendProgress("Erzeuge IMEX_CASE_WARD_VERSION_ACT");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_CASE_WARD_VERSION_ACT NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT "
                + "  IMEX_CASE.OLD_CASE_ID CASE_ID,"
                + "  IMEX_CASE.ACT_HOSD_EXTERN_ID,"
                + "  WARD.WARDC_ADMDATE, "
                + "  WARD.WARDC_DISDATE, "
                + "  WARD.WARDC_IDENT, "
                + "  DEP.DEP_KEY_301 "
                + (isOracle() ? "" : " INTO IMEX_CASE_WARD_VERSION_ACT ")
                + " FROM T_CASE_WARD WARD "
                + " INNER JOIN T_CASE_DEPARTMENT DEP ON DEP.ID = WARD.T_CASE_DEPARTMENT_ID"
                + " INNER JOIN T_CASE_DETAILS HOSD ON HOSD.ID = DEP.T_CASE_DETAILS_ID"
                + " INNER JOIN IMEX_CASE ON IMEX_CASE.ACT_HOSD_EXTERN_ID = HOSD.ID"
                + " WHERE IMEX_CASE.ACT_HOSD_EXTERN_ID IS NOT NULL AND IMEX_CASE.OLD_CASE_ID IS NOT NULL AND IMEX_CASE.NEW_VERSION = 0 AND IMEX_CASE.IGNORE_CASE = 0");

        getProgressor().sendProgress("Erzeuge IMEX_CASE_WARD_VERSION_NEW");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_CASE_WARD_VERSION_NEW NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT "
                + "  IMEX_CASE.OLD_CASE_ID CASE_ID, "
                + "  IMEX_CASE.ACT_HOSD_EXTERN_ID, "
                + "  IMEX_WARD_TMP.VERLEGUNGSDATUM WARDC_ADMDATE, "
                + "  IMEX_WARD_TMP.ENTLASSUNGSDATUM WARDC_DISDATE, "
                + "  IMEX_WARD_TMP.CODE WARDC_IDENT, "
                + "  IMEX_DEPARTMENT_TMP.CODE DEP_KEY_301 "
                + (isOracle() ? "" : " INTO IMEX_CASE_WARD_VERSION_NEW ")
                + " FROM IMEX_WARD_TMP"
                + " INNER JOIN IMEX_DEPARTMENT_TMP ON IMEX_DEPARTMENT_TMP.NR = IMEX_WARD_TMP.DEP_NR "
                + " INNER JOIN IMEX_CASE ON (IMEX_CASE.IKZ = IMEX_DEPARTMENT_TMP.IKZ AND IMEX_CASE.FALLNR = IMEX_DEPARTMENT_TMP.FALLNR)"
                + " LEFT JOIN IMEX_BEHANDELNDE_FAB ON IMEX_BEHANDELNDE_FAB.NR = IMEX_DEPARTMENT_TMP.NR "
                + " LEFT JOIN IMEX_AUFNEHMENDE_FAB ON IMEX_AUFNEHMENDE_FAB.NR = IMEX_DEPARTMENT_TMP.NR "
                + " LEFT JOIN IMEX_ENTLASSENDE_FAB ON IMEX_ENTLASSENDE_FAB.NR = IMEX_DEPARTMENT_TMP.NR "
                + " WHERE IMEX_CASE.ACT_HOSD_EXTERN_ID IS NOT NULL AND IMEX_CASE.OLD_CASE_ID IS NOT NULL AND IMEX_CASE.NEW_VERSION = 0 AND IMEX_CASE.IGNORE_CASE = 0");

        pQry.executeUpdate("CREATE INDEX VERSION_WARD_ACT_IDX ON IMEX_CASE_WARD_VERSION_ACT (CASE_ID) " + parallel());

        pQry.executeUpdate("CREATE INDEX VERSION_WARD_NEW_IDX ON IMEX_CASE_WARD_VERSION_NEW (CASE_ID) " + parallel());

        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_CASE 5)*/ IMEX_CASE SET NEW_VERSION = 1, VERSION_REASON = 'CASE_WARD_COUNT' WHERE NEW_VERSION = 0 AND IMEX_CASE.OLD_CASE_ID IS NOT NULL AND IMEX_CASE.IGNORE_CASE = 0 "
                + "  AND (EXISTS ("
                + "  SELECT COUNT(*) FROM IMEX_CASE_WARD_VERSION_ACT WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID "
                + "  " + minus() + " "
                + "  SELECT COUNT(*) FROM IMEX_CASE_WARD_VERSION_NEW WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID "
                + "  ) OR EXISTS ( "
                + "  SELECT COUNT(*) FROM IMEX_CASE_WARD_VERSION_NEW WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID "
                + "  " + minus() + " "
                + "  SELECT COUNT(*) FROM IMEX_CASE_WARD_VERSION_ACT WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID "
                + "))");

        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_CASE 5)*/ IMEX_CASE SET NEW_VERSION = 1, VERSION_REASON = 'CASE_WARD_DIFF' WHERE NEW_VERSION = 0 AND IMEX_CASE.OLD_CASE_ID IS NOT NULL AND IMEX_CASE.IGNORE_CASE = 0 "
                + "  AND (EXISTS ("
                + "  SELECT * FROM IMEX_CASE_WARD_VERSION_ACT WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID"
                + "  " + minus() + " "
                + "  SELECT * FROM IMEX_CASE_WARD_VERSION_NEW WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID"
                + "  ) OR EXISTS ( "
                + "  SELECT * FROM IMEX_CASE_WARD_VERSION_NEW WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID"
                + "  " + minus() + " "
                + "  SELECT * FROM IMEX_CASE_WARD_VERSION_ACT WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID"
                + "))");
    }

    protected void checkCaseDiagnosisVersion(final Query pQry) throws IOException, SQLException, ParseException {
        dropTable(pQry, "IMEX_CASE_ICD_VERSION_ACT");
        dropTable(pQry, "IMEX_CASE_ICD_VERSION_NEW");

        getProgressor().sendProgress("Erzeuge IMEX_CASE_ICD_VERSION_ACT");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_CASE_ICD_VERSION_ACT NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT "
                + "  IMEX_CASE.OLD_CASE_ID CASE_ID,"
                + "  IMEX_CASE.ACT_HOSD_EXTERN_ID,"
                + "  ICD.TO_GROUP_FL, "
                + "  ICD.ICDC_CODE, "
                + "  ICD.ICDC_TYPE_EN, "
                + "  ICD.MAIN_DIAG_DEP_FL, "
                + "  ICD.MAIN_DIAG_CASE_FL, "
                + "  ICD.ICDC_LOC_EN, "
                + "  ICD.ICD_REFERENCE_EN, "
                + "  REF_ICD.ICDC_CODE REF_ICDC_CODE, "
                + "  REF_ICD.ICDC_LOC_EN REF_ICDC_LOC_EN, "
                + "  WARD.WARDC_IDENT "
                + (isOracle() ? "" : " INTO IMEX_CASE_ICD_VERSION_ACT ")
                + " FROM T_CASE_ICD ICD "
                + " LEFT JOIN T_CASE_WARD WARD ON WARD.ID = ICD.T_CASE_WARD_ID "
                + " INNER JOIN T_CASE_DEPARTMENT DEP ON DEP.ID = ICD.T_CASE_DEPARTMENT_ID "
                + " LEFT JOIN T_CASE_ICD REF_ICD ON REF_ICD.ID = ICD.T_CASE_ICD_ID "
                + " INNER JOIN IMEX_CASE ON IMEX_CASE.ACT_HOSD_EXTERN_ID = DEP.T_CASE_DETAILS_ID"
                + " WHERE IMEX_CASE.ACT_HOSD_EXTERN_ID IS NOT NULL AND IMEX_CASE.OLD_CASE_ID IS NOT NULL AND IMEX_CASE.NEW_VERSION = 0 AND IMEX_CASE.IGNORE_CASE = 0");

        getProgressor().sendProgress("Erzeuge IMEX_CASE_ICD_VERSION_NEW");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_CASE_ICD_VERSION_NEW NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT "
                + "  IMEX_CASE.OLD_CASE_ID CASE_ID,"
                + "  IMEX_CASE.ACT_HOSD_EXTERN_ID,"
                + "  IMEX_DIAGNOSE.TO_GROUP TO_GROUP_FL, "
                + "  IMEX_DIAGNOSE.CODE ICDC_CODE, "
                + "  IMEX_DIAGNOSE.ICD_TYPE ICDC_TYPE_EN, "
                + "  IMEX_DIAGNOSE.HDB MAIN_DIAG_DEP_FL, "
                + "  IMEX_DIAGNOSE.HDX MAIN_DIAG_CASE_FL, "
                + "  IMEX_DIAGNOSE.LOKALISATION ICDC_LOC_EN, "
                + "  IMEX_DIAGNOSE.REF_ICD_TYPE ICD_REFERENCE_EN, "
                + "  REF_ICD.CODE REF_ICDC_CODE, "
                + "  REF_ICD.LOKALISATION REF_ICDC_LOC_EN, "
                + "  IMEX_WARD_TMP.CODE WARDC_IDENT "
                + (isOracle() ? "" : " INTO IMEX_CASE_ICD_VERSION_NEW ")
                + " FROM IMEX_DIAGNOSE"
                + " LEFT JOIN IMEX_WARD_TMP ON IMEX_WARD_TMP.NR = IMEX_DIAGNOSE.WARD_NR "
                + " INNER JOIN IMEX_CASE ON (IMEX_CASE.IKZ = IMEX_DIAGNOSE.IKZ AND IMEX_CASE.FALLNR = IMEX_DIAGNOSE.FALLNR)"
                + " LEFT JOIN IMEX_DIAGNOSE REF_ICD ON REF_ICD.NR = IMEX_DIAGNOSE.REF_ICD_NR"
                + " WHERE IMEX_CASE.ACT_HOSD_EXTERN_ID IS NOT NULL AND IMEX_CASE.OLD_CASE_ID IS NOT NULL AND IMEX_CASE.NEW_VERSION = 0 AND IMEX_CASE.IGNORE_CASE = 0");

        pQry.executeUpdate("CREATE INDEX VERSION_ICD_ACT_IDX ON IMEX_CASE_ICD_VERSION_ACT (CASE_ID) " + parallel());

        pQry.executeUpdate("CREATE INDEX VERSION_ICD_NEW_IDX ON IMEX_CASE_ICD_VERSION_NEW (CASE_ID) " + parallel());

        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_CASE 5)*/ IMEX_CASE SET NEW_VERSION = 1, VERSION_REASON = 'CASE_ICD_COUNT' WHERE NEW_VERSION = 0 AND IMEX_CASE.OLD_CASE_ID IS NOT NULL AND IMEX_CASE.IGNORE_CASE = 0 "
                + "  AND (EXISTS ("
                + "  SELECT COUNT(*) FROM IMEX_CASE_ICD_VERSION_ACT WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID "
                + "  " + minus() + " "
                + "  SELECT COUNT(*) FROM IMEX_CASE_ICD_VERSION_NEW WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID "
                + "  ) OR EXISTS ( "
                + "  SELECT COUNT(*) FROM IMEX_CASE_ICD_VERSION_NEW WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID "
                + "  " + minus() + " "
                + "  SELECT COUNT(*) FROM IMEX_CASE_ICD_VERSION_ACT WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID "
                + "))");

        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_CASE 5)*/ IMEX_CASE SET NEW_VERSION = 1, VERSION_REASON = 'CASE_ICD_DIFF' WHERE NEW_VERSION = 0 AND IMEX_CASE.OLD_CASE_ID IS NOT NULL AND IMEX_CASE.IGNORE_CASE = 0 "
                + "  AND (EXISTS ("
                + "  SELECT * FROM IMEX_CASE_ICD_VERSION_ACT WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID"
                + "  " + minus() + " "
                + "  SELECT * FROM IMEX_CASE_ICD_VERSION_NEW WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID"
                + "  ) OR EXISTS ( "
                + "  SELECT * FROM IMEX_CASE_ICD_VERSION_NEW WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID"
                + "  " + minus() + " "
                + "  SELECT * FROM IMEX_CASE_ICD_VERSION_ACT WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID"
                + "))");
    }

    protected void checkCaseProcedureVersion(final Query pQry) throws IOException, SQLException, ParseException {
        dropTable(pQry, "IMEX_CASE_OPS_VERSION_ACT");
        dropTable(pQry, "IMEX_CASE_OPS_VERSION_NEW");

        getProgressor().sendProgress("Erzeuge IMEX_CASE_OPS_VERSION_ACT");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_CASE_OPS_VERSION_ACT NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT "
                + "  IMEX_CASE.OLD_CASE_ID CASE_ID,"
                + "  IMEX_CASE.ACT_HOSD_EXTERN_ID,"
                + "  OPS.TO_GROUP_FL, "
                + "  OPS.OPSC_CODE, "
                + "  OPS.OPSC_DATUM, "
                + "  OPS.OPSC_LOC_EN, "
                + "  WARD.WARDC_IDENT "
                + (isOracle() ? "" : " INTO IMEX_CASE_OPS_VERSION_ACT ")
                + " FROM T_CASE_OPS OPS "
                + " LEFT JOIN T_CASE_WARD WARD ON WARD.ID = OPS.T_CASE_WARD_ID "
                + " INNER JOIN T_CASE_DEPARTMENT DEP ON DEP.ID = OPS.T_CASE_DEPARTMENT_ID "
                + " INNER JOIN IMEX_CASE ON IMEX_CASE.ACT_HOSD_EXTERN_ID = DEP.T_CASE_DETAILS_ID "
                + " WHERE IMEX_CASE.ACT_HOSD_EXTERN_ID IS NOT NULL AND IMEX_CASE.OLD_CASE_ID IS NOT NULL AND IMEX_CASE.NEW_VERSION = 0 AND IMEX_CASE.IGNORE_CASE = 0");

        getProgressor().sendProgress("Erzeuge IMEX_CASE_OPS_VERSION_NEW");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_CASE_OPS_VERSION_NEW NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT "
                + "  IMEX_CASE.OLD_CASE_ID CASE_ID,"
                + "  IMEX_CASE.ACT_HOSD_EXTERN_ID,"
                + "  IMEX_PROCEDURE_TMP.TO_GROUP TO_GROUP_FL, "
                + "  IMEX_PROCEDURE_TMP.CODE OPSC_CODE, "
                + "  IMEX_PROCEDURE_TMP.DATUM OPSC_DATUM, "
                + "  IMEX_PROCEDURE_TMP.LOKALISATION OPSC_LOC_EN, "
                + "  IMEX_WARD_TMP.CODE WARDC_IDENT "
                + (isOracle() ? "" : " INTO IMEX_CASE_OPS_VERSION_NEW ")
                + " FROM IMEX_PROCEDURE_TMP"
                + " LEFT JOIN IMEX_WARD_TMP ON IMEX_WARD_TMP.NR = IMEX_PROCEDURE_TMP.WARD_NR "
                + " INNER JOIN IMEX_CASE ON (IMEX_CASE.IKZ = IMEX_PROCEDURE_TMP.IKZ AND IMEX_CASE.FALLNR = IMEX_PROCEDURE_TMP.FALLNR)"
                + " WHERE IMEX_CASE.ACT_HOSD_EXTERN_ID IS NOT NULL AND IMEX_CASE.OLD_CASE_ID IS NOT NULL AND IMEX_CASE.NEW_VERSION = 0 AND IMEX_CASE.IGNORE_CASE = 0");

        pQry.executeUpdate("CREATE INDEX VERSION_OPS_ACT_IDX ON IMEX_CASE_OPS_VERSION_ACT (CASE_ID) " + parallel());

        pQry.executeUpdate("CREATE INDEX VERSION_OPS_NEW_IDX ON IMEX_CASE_OPS_VERSION_NEW (CASE_ID) " + parallel());

        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_CASE 5)*/ IMEX_CASE SET NEW_VERSION = 1, VERSION_REASON = 'CASE_OPS_COUNT' WHERE NEW_VERSION = 0 AND IMEX_CASE.OLD_CASE_ID IS NOT NULL AND IMEX_CASE.IGNORE_CASE = 0 "
                + "  AND (EXISTS ("
                + "  SELECT COUNT(*) FROM IMEX_CASE_OPS_VERSION_ACT WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID "
                + "  " + minus() + " "
                + "  SELECT COUNT(*) FROM IMEX_CASE_OPS_VERSION_NEW WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID "
                + "  ) OR EXISTS ( "
                + "  SELECT COUNT(*) FROM IMEX_CASE_OPS_VERSION_NEW WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID "
                + "  " + minus() + " "
                + "  SELECT COUNT(*) FROM IMEX_CASE_OPS_VERSION_ACT WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID "
                + "))");

        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_CASE 5)*/ IMEX_CASE SET NEW_VERSION = 1, VERSION_REASON = 'CASE_OPS_DIFF' WHERE NEW_VERSION = 0 AND IMEX_CASE.OLD_CASE_ID IS NOT NULL AND IMEX_CASE.IGNORE_CASE = 0 "
                + "  AND (EXISTS ("
                + "  SELECT * FROM IMEX_CASE_OPS_VERSION_ACT WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID"
                + "  " + minus() + " "
                + "  SELECT * FROM IMEX_CASE_OPS_VERSION_NEW WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID"
                + "  ) OR EXISTS ( "
                + "  SELECT * FROM IMEX_CASE_OPS_VERSION_NEW WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID"
                + "  " + minus() + " "
                + "  SELECT * FROM IMEX_CASE_OPS_VERSION_ACT WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID"
                + "))");
    }

    protected void checkCaseFeeVersion(final Query pQry) throws IOException, SQLException, ParseException {
        dropTable(pQry, "IMEX_CASE_FEE_VERSION_ACT");
        dropTable(pQry, "IMEX_CASE_FEE_VERSION_NEW");

        getProgressor().sendProgress("Erzeuge IMEX_CASE_FEE_VERSION_ACT");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_CASE_FEE_VERSION_ACT NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT "
                + "  IMEX_CASE.OLD_CASE_ID CASE_ID, "
                + "  IMEX_CASE.ACT_HOSD_EXTERN_ID, "
                + "  FEE.FEEC_COUNT, "
                + "  FEE.FEEC_FEEKEY, "
                + "  FEE.FEEC_FROM, "
                + "  FEE.FEEC_TO, "
                + "  FEE.FEEC_UNBILLED_DAYS, "
                + "  FEE.FEEC_VALUE, "
                + "  FEE.FEEC_INSURANCE, "
                + "  FEE.FEEC_IS_BILL_FL "
                //+ "  BILL.BILLC_NUMBER, "
                //+ "  BILL.BILLC_FROM "
                + (isOracle() ? "" : " INTO IMEX_CASE_FEE_VERSION_ACT ")
                + " FROM T_CASE_FEE FEE "
                //+ " LEFT JOIN T_CASE_BILL BILL ON BILL.ID = FEE.T_CASE_BILL_ID "
                + " INNER JOIN T_CASE_DETAILS HOSD ON HOSD.ID = FEE.T_CASE_DETAILS_ID "
                + " INNER JOIN IMEX_CASE ON IMEX_CASE.ACT_HOSD_EXTERN_ID = HOSD.ID "
                + " WHERE IMEX_CASE.ACT_HOSD_EXTERN_ID IS NOT NULL AND IMEX_CASE.OLD_CASE_ID IS NOT NULL AND IMEX_CASE.NEW_VERSION = 0 AND IMEX_CASE.IGNORE_CASE = 0");

        getProgressor().sendProgress("Erzeuge IMEX_CASE_FEE_VERSION_NEW");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_CASE_FEE_VERSION_NEW NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT "
                + "  IMEX_CASE.OLD_CASE_ID CASE_ID, "
                + "  IMEX_CASE.ACT_HOSD_EXTERN_ID, "
                + "  IMEX_FEE_TMP.ANZAHL FEEC_COUNT, "
                + "  IMEX_FEE_TMP.ENTGELTSCHLUESSEL FEEC_FEEKEY, "
                + "  IMEX_FEE_TMP.VON FEEC_FROM, "
                + "  IMEX_FEE_TMP.BIS FEEC_TO, "
                + "  IMEX_FEE_TMP.TOB FEEC_UNBILLED_DAYS, "
                + "  IMEX_FEE_TMP.BETRAG FEEC_VALUE, "
                + "  IMEX_FEE_TMP.KASSE FEEC_INSURANCE, "
                + "  IMEX_FEE_TMP.IS_RECHNUNG FEEC_IS_BILL_FL "
                //+ "  IMEX_BILL.RECHNUNGSNR BILLC_NUMBER, "
                //+ "  IMEX_BILL.RECHNUNGSDATUM BILLC_FROM "
                + (isOracle() ? "" : " INTO IMEX_CASE_FEE_VERSION_NEW ")
                + " FROM IMEX_FEE_TMP "
                //+ " LEFT JOIN IMEX_BILL ON IMEX_BILL.NR = IMEX_FEE.RECHNUNG_NR "
                + " INNER JOIN IMEX_CASE ON (IMEX_CASE.IKZ = IMEX_FEE_TMP.IKZ AND IMEX_CASE.FALLNR = IMEX_FEE_TMP.FALLNR) "
                + " WHERE IMEX_CASE.ACT_HOSD_EXTERN_ID IS NOT NULL AND IMEX_CASE.OLD_CASE_ID IS NOT NULL AND IMEX_CASE.NEW_VERSION = 0 AND IMEX_CASE.IGNORE_CASE = 0");

        pQry.executeUpdate("CREATE INDEX VERSION_FEE_ACT_IDX ON IMEX_CASE_FEE_VERSION_ACT (CASE_ID) " + parallel());

        pQry.executeUpdate("CREATE INDEX VERSION_FEE_NEW_IDX ON IMEX_CASE_FEE_VERSION_NEW (CASE_ID) " + parallel());

        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_CASE 5)*/ IMEX_CASE SET NEW_VERSION = 1, VERSION_REASON = 'CASE_FEE_COUNT' WHERE NEW_VERSION = 0 AND IMEX_CASE.OLD_CASE_ID IS NOT NULL AND IMEX_CASE.IGNORE_CASE = 0 "
                + "  AND EXISTS ("
                + "  SELECT COUNT(*) FROM IMEX_CASE_FEE_VERSION_ACT WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID "
                + "  " + minus() + " "
                + "  SELECT COUNT(*) FROM IMEX_CASE_FEE_VERSION_NEW WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID "
                + "  ) OR EXISTS ( "
                + "  SELECT COUNT(*) FROM IMEX_CASE_FEE_VERSION_NEW WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID "
                + "  " + minus() + " "
                + "  SELECT COUNT(*) FROM IMEX_CASE_FEE_VERSION_ACT WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID "
                + ")");

        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_CASE 5)*/ IMEX_CASE SET NEW_VERSION = 1, VERSION_REASON = 'CASE_FEE_DIFF' WHERE NEW_VERSION = 0 AND IMEX_CASE.IGNORE_CASE = 0 "
                + "  AND (EXISTS ("
                + "  SELECT * FROM IMEX_CASE_FEE_VERSION_ACT WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID"
                + "  " + minus() + " "
                + "  SELECT * FROM IMEX_CASE_FEE_VERSION_NEW WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID"
                + "  ) OR EXISTS ( "
                + "  SELECT * FROM IMEX_CASE_FEE_VERSION_NEW WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID"
                + "  " + minus() + " "
                + "  SELECT * FROM IMEX_CASE_FEE_VERSION_ACT WHERE CASE_ID = IMEX_CASE.OLD_CASE_ID"
                + "))");

        /*
pQry.executeUpdate("UPDATE IMEX_CASE SET NEW_VERSION = 1, VERSION_REASON = 'CASE_FEE_COUNT' WHERE NEW_VERSION = 0 AND IMEX_CASE.OLD_CASE_ID IN ("
+ "  SELECT CASE_ID FROM ("
+ "    SELECT TMP.CASE_ID, TMP.CNT_ACT, COUNT(*) CNT_NEW"
+ "    FROM ("
+ "      SELECT CASE_ID, COUNT(*) CNT_ACT"
+ "      FROM IMEX_CASE_FEE_VERSION_ACT"
+ "      GROUP BY CASE_ID"
+ "    ) TMP"
+ "    INNER JOIN IMEX_CASE_FEE_VERSION_NEW ON IMEX_CASE_FEE_VERSION_NEW.CASE_ID = TMP.CASE_ID"
+ "    GROUP BY TMP.CASE_ID, TMP.CNT_ACT, IMEX_CASE_FEE_VERSION_NEW.CASE_ID"
+ "  ) TMP2 WHERE CNT_ACT <> CNT_NEW"
+ ")");

pQry.executeUpdate("UPDATE IMEX_CASE SET NEW_VERSION = 1, VERSION_REASON = 'CASE_FEE_DIFF' WHERE NEW_VERSION = 0 AND IMEX_CASE.OLD_CASE_ID IN ("
+ "  SELECT CASE_ID "
+ "  FROM ( "
+ "    SELECT * FROM IMEX_CASE_FEE_VERSION_ACT"
+ "    MINUS "
+ "    SELECT * FROM IMEX_CASE_FEE_VERSION_NEW"
+ "    UNION ALL "
+ "    SELECT * FROM IMEX_CASE_FEE_VERSION_NEW"
+ "    MINUS "
+ "    SELECT * FROM IMEX_CASE_FEE_VERSION_ACT"
+ "  ) TMP "
+ ")");
         */
        //dropTable(pQry, "IMEX_CASE_FEE_VERSION_ACT");
        //dropTable(pQry, "IMEX_CASE_FEE_VERSION_NEW");
    }

    protected void createNewPatientIds(final Query pQry) throws SQLException, IOException, ParseException {

        //dropTable(qry, "IMEX_CASE_ACT_VERSION");
        //dropTable(qry, "IMEX_CASE_NEW_VERSION");
        dropTable(pQry, "IMEX_PATIENT_NEW_ID");

        printTime("  Create new patient detail ids...");
        getProgressor().sendProgress("Erzeuge IMEX_PATIENT_NEW_ID");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_PATIENT_NEW_ID NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT IMEX_PATIENT.NR, IMEX_PATIENT.PATNR, " + nextSqVal("T_PATIENT_DETAILS_SQ") + " NEW_DETAILS_ID "
                + (isOracle() ? "" : " INTO IMEX_PATIENT_NEW_ID ")
                + " FROM IMEX_PATIENT "
                + " WHERE NEW_VERSION = 1");
        //pQry.executeUpdate("CREATE UNIQUE INDEX IMEX_PATIENT_NEW_NR_IDX ON IMEX_PATIENT_NEW_ID (NR)");
        pQry.executeUpdate("CREATE UNIQUE INDEX IMEX_PATIENT_NEW_PATNR_IDX ON IMEX_PATIENT_NEW_ID (PATNR) " + parallel());
        printTime("  New patient detail ids created!");
    }

    protected void createNewInsuranceIds(final Query pQry) throws SQLException, IOException, ParseException {

        //dropTable(qry, "IMEX_CASE_ACT_VERSION");
        //dropTable(qry, "IMEX_CASE_NEW_VERSION");
        dropTable(pQry, "IMEX_PAT_INSURANCE_NEW_ID");

        printTime("  Create new patient insurance ids...");
        getProgressor().sendProgress("Erzeuge IMEX_PAT_INSURANCE_NEW_ID");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_PAT_INSURANCE_NEW_ID NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT IMEX_PATIENT.NR, IMEX_PATIENT.PATNR, " + nextSqVal("T_INSURANCE_SQ") + " NEW_INS_ID "
                + (isOracle() ? "" : " INTO IMEX_PAT_INSURANCE_NEW_ID ")
                + " FROM IMEX_PATIENT "
                + " WHERE NEW_INSURANCE_VERSION = 1");
        //pQry.executeUpdate("CREATE UNIQUE INDEX IMEX_PAT_INS_NEW_NR_IDX ON IMEX_PAT_INSURANCE_NEW_ID (NR)");
        pQry.executeUpdate("CREATE UNIQUE INDEX IMEX_PAT_INS_NEW_PATNR_IDX ON IMEX_PAT_INSURANCE_NEW_ID (PATNR) " + parallel());
        printTime("  New patient insurance ids created!");
    }

    protected void refreshIndexes(final Query pQry) throws SQLException, IOException, ParseException {
        printTime("Rebuild Indexes...");
        getProgressor().sendProgress("Erneuere Indizes");
        if (!getImportConfig().isRebuildIndexes()) {
            printTime("Rebuilding Indexes was skipped!");
            return;
        }
        final List<String[]> indexes = new ArrayList<>();
//        final String[] notLike = new String[]{"'GRP_%'", "'P21_%'", "'IMEX_%'"};
        final String query;
        if (isOracle()) {
            query = "SELECT "
                    + "   TABLE_NAME, "
                    + "   INDEX_NAME "
                    + " FROM ALL_INDEXES "
                    + " WHERE TABLE_NAME LIKE 'T\\_%' ESCAPE '\\' "
                    //                    + " WHERE OWNER = '" + getImportConfig().getDatabase() + "' "
                    //                    + "   AND TABLE_NAME NOT LIKE " + String.join(" AND TABLE_NAME NOT LIKE ", notLike)
                    + " ORDER BY TABLE_NAME, INDEX_NAME";
        } else {
            query = "SELECT "
                    //+ "  -- SC.NAME AS SCHEMA_NAME, "
                    + "  O.NAME AS TABLE_NAME, "
                    + "  I.NAME AS INDEX_NAME "
                    //+ "  -- I.TYPE_DESC AS INDEX_TYPE "
                    + " FROM SYS.INDEXES I "
                    + " INNER JOIN SYS.OBJECTS O ON I.OBJECT_ID = O.OBJECT_ID "
                    + " INNER JOIN SYS.SCHEMAS SC ON O.SCHEMA_ID = SC.SCHEMA_ID "
                    + " WHERE I.NAME IS NOT NULL "
                    + "   AND O.TYPE = 'U' "
                    + "   AND O.NAME LIKE 'T_%' "
                    //+ "   AND O.NAME NOT LIKE " + String.join(" AND O.NAME NOT LIKE ", notLike)
                    + " ORDER BY O.NAME, I.TYPE";
        }
        try (ResultSet rs = pQry.executeQuery(query)) {
            while (rs.next()) {
                final String table = rs.getString("TABLE_NAME");
                final String index = rs.getString("INDEX_NAME");
                indexes.add(new String[]{table, index});
            }
        }

        for (String[] tmp : indexes) {
            final String table = tmp[0];
            final String index = tmp[1];
            final String rebuildQuery;
            if (isOracle()) {
                rebuildQuery = "ALTER INDEX " + index + " REBUILD /* ONLINE */ PARALLEL 5 NOLOGGING";
            } else {
                rebuildQuery = "ALTER INDEX " + index + " ON " + table + " REORGANIZE";
            }
            LOG.log(Level.FINEST, "rebuild index " + table + "." + index);
            try {
                pQry.execute(rebuildQuery);
            } catch (SQLException ex) {
                LOG.log(Level.WARNING, "cannot rebuild index: " + rebuildQuery, ex);
            }
        }

        printTime("  Indexes rebuild!");
    }

    //import of drugs (Medikamente, EK_MED)
    protected void createNewMedDaten(final Query pQry) throws SQLException, IOException, ParseException {
        printTime("Create IMEX_DRUG...");

        dropTable(pQry, "IMEX_DRUG");

        getProgressor().sendProgress("Erzeuge IMEX_DRUG");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_DRUG NOLOGGING PARALLEL 5 AS " : "")
                + "SELECT "
                + "   TMP.* "
                //+ "   CASE WHEN DRUG_ID IS NULL THEN 1 ELSE 0 END IS_NEW_DRUG "
                + (isOracle() ? "" : " INTO IMEX_DRUG ")
                + " FROM ("
                + "   SELECT "
                + "     IMEX_DRUG_TMP.*, "
                + "     IMEX_CASE.NEW_CASE_ID, "
                + "     IMEX_CASE.NEW_PATIENT_ID "
                // + "     IMEX_CASE.ACT_HOSD_EXTERN_ID, "
                // + "     IMEX_CASE.ACT_HOSD_LOCAL_ID, "
                //  + "     IMEX_PATIENT.NEW_PATIENT_ID, "
                // + "     T_DRUG.ID DRUG_ID "
                //+ "     NULL DRUG_ID "
                + "   FROM IMEX_DRUG_TMP "
                //  + "   INNER JOIN IMEX_PATIENT ON IMEX_PATIENT.PATNR = IMEX_LAB_TMP.PATNR "
                + "   INNER JOIN IMEX_CASE ON IMEX_CASE.IKZ = IMEX_DRUG_TMP.IKZ AND IMEX_CASE.FALLNR = IMEX_DRUG_TMP.FALLNR "
                // + "   LEFT JOIN T_DRUG ON T_DRUG.T_CASE_ID = IMEX_CASE.NEW_CASE_ID AND IMEX_CASE.IGNORE_CASE = 0 "
                + " ) TMP ");

        //pQry.executeUpdate("ALTER TABLE IMEX_LAB ADD IMEX_LAB_ID " + (isOracle() ? "NUMBER(19, 0)" : "BIGINT"));
        pQry.executeUpdate("ALTER TABLE IMEX_DRUG ADD DRUG_ID " + (isOracle() ? "NUMBER(19, 0)" : "BIGINT"));
        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_DRUG 5)*/ IMEX_DRUG SET DRUG_ID = " + nextSqVal("T_DRUG_SQ") + " WHERE DRUG_ID IS NULL");

        pQry.executeUpdate("CREATE INDEX IDX_IMEX_DRUG_FALL ON IMEX_DRUG (IKZ, FALLNR) " + parallel());
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_DRUG_PATNR ON IMEX_DRUG (PATNR) " + parallel());
        //pQry.executeUpdate("CREATE INDEX IDX_IMEX_DRUG_IS_NEW_DRUG ON IMEX_DRUG (IS_NEW_DRUG)");
        pQry.executeUpdate(DbBuilder.getImexPkDefinition("IMEX_DRUG"));

        printTime("IMEX_DRUG created!");

        //DELETE ALL DRUG FOR SPECIFIC HOSPITAL CASES (FULL REPLACE IS INTENDED HERE!)
        printTime("  delete from T_DRUG...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_DRUG 5)*/ FROM T_DRUG WHERE T_DRUG.T_CASE_ID IN (SELECT NEW_CASE_ID FROM IMEX_DRUG WHERE NEW_CASE_ID IS NOT NULL)");
        printTime("  T_DRUG deleted!");

        int c = pQry.executeUpdate("INSERT /*+ PARALLEL(T_DRUG 5)*/ INTO T_DRUG (ID, T_PATIENT_ID, T_CASE_ID, CREATION_DATE, CREATION_USER, DG_ATC, DG_BMG, DG_DF, DG_DOCTOR_IDENT, DG_FACTOR, DG_GENERICS, DG_GROSS, DG_GROSS_TOTAL, DG_PACK_SIZE, DG_PHARMACY_IDENT, DG_PIC_NO, DG_PRESCRIPTION_DATE, DG_PZN, DG_STANDARD_SIZE, DG_SUBMISSION_DATE, DG_UNFK) "
                + " SELECT DRUG_ID, NEW_PATIENT_ID, NEW_CASE_ID, " + currentDate() + ", " + jobUserId + ", ATC, BMG, DF, ARZTNR, FAKTOR, GENERIKA, BRUTTO, BRUTTO_GESAMT, PACKUNGSGR, APOIK, PICNR, VERORD, PZN, NORMGR, ABG, UNFK FROM IMEX_DRUG"); // WHERE IS_NEW_DRUG = 1

        printTime("  " + c + " new T_DRUGs created!");
    }

    protected void createNewLaborDaten(final Query pQry) throws SQLException, IOException, ParseException {
        int c;
        printTime("Create IMEX_LAB...");

        dropTable(pQry, "IMEX_LAB");

        getProgressor().sendProgress("Erzeuge IMEX_LAB");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_LAB NOLOGGING PARALLEL 5 AS " : "")
                + "SELECT "
                + "   TMP.*, "
                + "   CASE WHEN LAB_ID IS NULL THEN 1 ELSE 0 END IS_NEW_LAB "
                + (isOracle() ? "" : " INTO IMEX_LAB ")
                + " FROM ("
                + "   SELECT "
                + "     IMEX_LAB_TMP.*, "
                + "     IMEX_CASE.NEW_CASE_ID, "
                // + "     IMEX_CASE.ACT_HOSD_EXTERN_ID, "
                // + "     IMEX_CASE.ACT_HOSD_LOCAL_ID, "
                //  + "     IMEX_PATIENT.NEW_PATIENT_ID, "
                + "     T_LAB.ID LAB_ID "
                + "   FROM IMEX_LAB_TMP "
                //  + "   INNER JOIN IMEX_PATIENT ON IMEX_PATIENT.PATNR = IMEX_LAB_TMP.PATNR "
                + "   INNER JOIN IMEX_CASE ON IMEX_CASE.IKZ = IMEX_LAB_TMP.IKZ AND IMEX_CASE.FALLNR = IMEX_LAB_TMP.FALLNR "
                + "   LEFT JOIN T_LAB ON T_LAB.LAB_KIS_EXTERN_KEY = IMEX_LAB_TMP.LAB_KIS_EXTERN_KEY AND T_LAB.T_CASE_ID = IMEX_CASE.NEW_CASE_ID AND IMEX_CASE.IGNORE_CASE = 0 "
                + " ) TMP ");

        //pQry.executeUpdate("ALTER TABLE IMEX_LAB ADD IMEX_LAB_ID " + (isOracle() ? "NUMBER(19, 0)" : "BIGINT"));
        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_LAB 5)*/ IMEX_LAB SET LAB_ID = " + nextSqVal("T_LAB_SQ") + " WHERE LAB_ID IS NULL");

        pQry.executeUpdate("CREATE INDEX IDX_IMEX_LAB_FALL ON IMEX_LAB (IKZ, FALLNR) " + parallel());
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_LAB_PATNR ON IMEX_LAB (PATNR) " + parallel());
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_LAB_IS_NEW_LAB ON IMEX_LAB (IS_NEW_LAB)");
        pQry.executeUpdate(DbBuilder.getImexPkDefinition("IMEX_LAB"));

        printTime("IMEX_LAB created!");

        c = pQry.executeUpdate("INSERT /*+ PARALLEL(T_LAB 5)*/ INTO T_LAB (ID, T_CASE_ID, CREATION_DATE, CREATION_USER, LAB_ANALYSIS, LAB_BENCHMARK, LAB_COMMENT, LAB_DESCRIPTION, LAB_GROUP, LAB_ANALYSIS_DATE, LAB_CATEGORY, LAB_DATE, LAB_KIS_EXTERN_KEY, LAB_LOCKDEL, LAB_MAX_LIMIT, LAB_MIN_LIMIT, LAB_POSITION, LAB_VALUE, LAB_VALUE_2, LAB_METHOD, LAB_RANGE, LAB_TEXT, LAB_UNIT) "
                + " SELECT LAB_ID, NEW_CASE_ID, " + currentDate() + ", " + jobUserId + ", LAB_ANALYSIS, LAB_BENCHMARK, LAB_COMMENT, LAB_DESCRIPTION, LAB_GROUP, LAB_ANALYSIS_DATE, LAB_CATEGORY, LAB_DATE, LAB_KIS_EXTERN_KEY, LAB_LOCKDEL, LAB_MAX_LIMIT, LAB_MIN_LIMIT, LAB_POSITION, LAB_VALUE, LAB_VALUE_2, LAB_METHOD, LAB_RANGE, LAB_TEXT, LAB_UNIT FROM IMEX_LAB WHERE IS_NEW_LAB = 1");

        printTime("  " + c + " new T_LABs created!");
    }

    protected void createNewFiDaten(final Query pQry) throws SQLException, IOException, ParseException {
        int c;

        printTime("Create IMEX_FI_BILL...");

        dropTable(pQry, "IMEX_FI_BILL");

        getProgressor().sendProgress("Erzeuge IMEX_FI_BILL");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_FI_BILL NOLOGGING PARALLEL 5 AS " : "")
                + "SELECT "
                + "   TMP.*, "
                + "   CASE WHEN FI_BILL_ID IS NULL THEN 1 ELSE 0 END IS_NEW_BILL "
                + (isOracle() ? "" : " INTO IMEX_FI_BILL ")
                + " FROM ("
                + "   SELECT "
                + "     IMEX_FI_BILL_TMP.*, "
                + "     IMEX_CASE.NEW_CASE_ID, "
                + "     IMEX_PATIENT.NEW_PATIENT_ID, "
                + "     T_SAP_FI_BILL.ID FI_BILL_ID "
                + "   FROM IMEX_FI_BILL_TMP "
                + "   INNER JOIN IMEX_PATIENT ON IMEX_PATIENT.PATNR = IMEX_FI_BILL_TMP.PATNR "
                + "   INNER JOIN IMEX_CASE ON IMEX_CASE.IKZ = IMEX_FI_BILL_TMP.IKZ AND IMEX_CASE.FALLNR = IMEX_FI_BILL_TMP.FALLNR "
                + "   LEFT JOIN T_SAP_FI_BILL ON T_SAP_FI_BILL.INVOICE = IMEX_FI_BILL_TMP.INVOICE AND T_SAP_FI_BILL.T_CASE_ID = IMEX_CASE.NEW_CASE_ID AND IMEX_CASE.IGNORE_CASE = 0 "
                + " ) TMP ");

        //DELETE ALL FI DATA FOR SPECIFIC HOSPITAL CASES (FULL REPLACE IS INTENDED HERE!)
        printTime("  delete from T_SAP_FI_OPEN_ITEMS...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_SAP_FI_OPEN_ITEMS 5)*/ FROM T_SAP_FI_OPEN_ITEMS WHERE T_SAP_FI_OPEN_ITEMS.T_CASE_ID IN (SELECT NEW_CASE_ID FROM IMEX_FI_BILL WHERE NEW_CASE_ID IS NOT NULL)");
        printTime("  T_SAP_FI_OPEN_ITEMS deleted!");

        printTime("  delete from T_SAP_FI_BILLPOSITION...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_SAP_FI_BILLPOSITION 5)*/ FROM T_SAP_FI_BILLPOSITION WHERE T_SAP_FI_BILLPOSITION.T_SAP_FI_BILL_ID IN (SELECT FI_BILL_ID FROM IMEX_FI_BILL)");
        printTime("  T_SAP_FI_BILLPOSITION deleted!");

        printTime("  delete from T_SAP_FI_BILL...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_SAP_FI_BILL 5)*/ FROM T_SAP_FI_BILL WHERE T_SAP_FI_BILL.ID IN (SELECT FI_BILL_ID FROM IMEX_FI_BILL)");
        printTime("  T_SAP_FI_BILL deleted!");

        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_FI_BILL 5)*/ IMEX_FI_BILL SET FI_BILL_ID = " + nextSqVal("T_SAP_FI_BILL_SQ") + " WHERE FI_BILL_ID IS NULL");

        pQry.executeUpdate("CREATE INDEX IDX_IMEX_FI_BILL_FALL ON IMEX_FI_BILL (IKZ, FALLNR) " + parallel());
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_FI_BILL_PATNR ON IMEX_FI_BILL (PATNR) " + parallel());
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_FI_BILL_IS_NEW_BILL ON IMEX_FI_BILL (IS_NEW_BILL)");
        pQry.executeUpdate(DbBuilder.getImexPkDefinition("IMEX_FI_BILL"));

        printTime("IMEX_FI_BILL created!");

        printTime("Create IMEX_FI_BILLPOS...");

        dropTable(pQry, "IMEX_FI_BILLPOS");

        getProgressor().sendProgress("Erzeuge IMEX_FI_BILLPOS");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_FI_BILLPOS NOLOGGING PARALLEL 5 AS " : "")
                + "SELECT "
                + "   TMP.* "
                // + "   CASE WHEN FI_BILLPOS_ID IS NULL THEN 1 ELSE 0 END IS_NEW_BILLPOS "
                + (isOracle() ? "" : " INTO IMEX_FI_BILLPOS ")
                + " FROM ("
                + "   SELECT "
                + "     IMEX_FI_BILLPOS_TMP.*, "
                + "     IMEX_FI_BILL.NEW_CASE_ID, "
                + "     IMEX_FI_BILL.NEW_PATIENT_ID, "
                + "     IMEX_FI_BILL.FI_BILL_ID "
                //  + "     T_SAP_FI_BILLPOSITION.ID FI_BILLPOS_ID "
                + "   FROM IMEX_FI_BILLPOS_TMP "
                + "   INNER JOIN IMEX_FI_BILL ON IMEX_FI_BILL.NR = IMEX_FI_BILLPOS_TMP.SAP_FI_BILL_NR "
                //                + "   LEFT JOIN T_SAP_FI_BILLPOSITION ON T_SAP_FI_BILLPOSITION.INVOICE = IMEX_FI_BILLPOS_TMP.INVOICE AND T_SAP_FI_BILLPOSITION.POSITION_NUMBER = IMEX_FI_BILLPOS_TMP.POSITION_NUMBER "
                + " ) TMP ");
        pQry.executeUpdate("ALTER TABLE IMEX_FI_BILLPOS ADD FI_BILLPOS_ID " + (isOracle() ? "NUMBER(19, 0)" : "BIGINT"));
        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_FI_BILLPOS 5)*/ IMEX_FI_BILLPOS SET FI_BILLPOS_ID = " + nextSqVal("T_SAP_FI_BILLPOSITION_SQ") + " WHERE FI_BILLPOS_ID IS NULL");

        pQry.executeUpdate("CREATE INDEX IDX_IMEX_FI_BILLPOS_FALL ON IMEX_FI_BILLPOS (IKZ, FALLNR) " + parallel());
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_FI_BILLPOS_PATNR ON IMEX_FI_BILLPOS (PATNR) " + parallel());
        //pQry.executeUpdate("CREATE INDEX IDX_IMEX_FI_BILLPOS_IS_NEW_POS ON IMEX_FI_BILLPOS (IS_NEW_BILLPOS)");
        pQry.executeUpdate(DbBuilder.getImexPkDefinition("IMEX_FI_BILLPOS"));

        printTime("IMEX_FI_BILLPOS created!");

        printTime("Create IMEX_FI_OPEN_ITEMS...");

        dropTable(pQry, "IMEX_FI_OPEN_ITEMS");

        //2018-03-14 DNi: Key in T_SAP_FI_OPEN_ITEMS is not really known! I think the combination of NUMBER_RECEIPT and POSTING_KEY is unique.
        getProgressor().sendProgress("Erzeuge IMEX_FI_OPEN_ITEMS");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_FI_OPEN_ITEMS NOLOGGING PARALLEL 5 AS " : "")
                + "SELECT "
                + "   TMP.* "
                // + "   CASE WHEN FI_OPEN_ITEMS_ID IS NULL THEN 1 ELSE 0 END IS_NEW_OPEN_ITEMS "
                + (isOracle() ? "" : " INTO IMEX_FI_OPEN_ITEMS ")
                + " FROM ("
                + "   SELECT "
                + "     IMEX_FI_OPEN_ITEMS_TMP.*, "
                + "     IMEX_CASE.NEW_CASE_ID, "
                + "     IMEX_PATIENT.NEW_PATIENT_ID "
                // + "     T_SAP_FI_OPEN_ITEMS.ID FI_OPEN_ITEMS_ID "
                + "   FROM IMEX_FI_OPEN_ITEMS_TMP "
                + "   INNER JOIN IMEX_PATIENT ON IMEX_PATIENT.PATNR = IMEX_FI_OPEN_ITEMS_TMP.PATNR "
                + "   INNER JOIN IMEX_CASE ON IMEX_CASE.IKZ = IMEX_FI_OPEN_ITEMS_TMP.IKZ AND IMEX_CASE.FALLNR = IMEX_FI_OPEN_ITEMS_TMP.FALLNR "
                //                + "   LEFT JOIN T_SAP_FI_OPEN_ITEMS ON T_SAP_FI_OPEN_ITEMS.T_CASE_ID = IMEX_CASE.NEW_CASE_ID AND T_SAP_FI_OPEN_ITEMS.NUMBER_RECEIPT = IMEX_FI_OPEN_ITEMS_TMP.NUMBER_RECEIPT AND T_SAP_FI_OPEN_ITEMS.POSTING_KEY = IMEX_FI_OPEN_ITEMS_TMP.POSTING_KEY "
                + " ) TMP ");
        pQry.executeUpdate("ALTER TABLE IMEX_FI_OPEN_ITEMS ADD FI_OPEN_ITEMS_ID " + (isOracle() ? "NUMBER(19, 0)" : "BIGINT"));
        pQry.executeUpdate("UPDATE /*+ PARALLEL(IMEX_FI_OPEN_ITEMS 5)*/ IMEX_FI_OPEN_ITEMS SET FI_OPEN_ITEMS_ID = " + nextSqVal("T_SAP_FI_OPEN_ITEMS_SQ") + " WHERE FI_OPEN_ITEMS_ID IS NULL");

        pQry.executeUpdate("CREATE INDEX IDX_IMEX_FI_OPEN_ITEMS_FALL ON IMEX_FI_OPEN_ITEMS (IKZ, FALLNR) " + parallel());
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_FI_OPEN_ITEMS_PATNR ON IMEX_FI_OPEN_ITEMS (PATNR) " + parallel());
        //pQry.executeUpdate("CREATE INDEX IDX_IMEX_FI_OPEN_ITEMS_IS_NEW ON IMEX_FI_OPEN_ITEMS (IS_NEW_OPEN_ITEMS)");
        pQry.executeUpdate(DbBuilder.getImexPkDefinition("IMEX_FI_OPEN_ITEMS"));

        printTime("IMEX_FI_OPEN_ITEMS created!");

        printTime("  Create new SAP_FI_BILLs...");

        c = pQry.executeUpdate("INSERT /*+ PARALLEL(T_SAP_FI_BILL 5)*/ INTO T_SAP_FI_BILL (ID, CREATION_DATE, CREATION_USER, INVOICE, FISCAL_YEAR, INVOICE_DATE, INVOICE_KIND, INVOICE_TYPE, NET_VALUE, RECEIVER_REF, REFERENCE_CURRENCY, REFERENCE_TYPE, STATE, STORNO_REF, T_CASE_ID) "
                + " SELECT FI_BILL_ID, " + currentDate() + ", " + jobUserId + ", INVOICE, FISCAL_YEAR, INVOICE_DATE, INVOICE_KIND, INVOICE_TYPE, NET_VALUE, RECEIVER_REF, REFERENCE_CURRENCY, REFERENCE_TYPE, STATE, STORNO_REF, NEW_CASE_ID FROM IMEX_FI_BILL "); //WHERE IS_NEW_BILL = 1 ");

        printTime("  " + c + " new SAP_FI_BILLs created!");

        printTime("  Create new SAP_FI_BILLPOSITIONs...");

        c = pQry.executeUpdate("INSERT /*+ PARALLEL(T_SAP_FI_BILLPOSITION 5)*/ INTO T_SAP_FI_BILLPOSITION (ID, T_SAP_FI_BILL_ID, CREATION_DATE, CREATION_USER, AMOUNT, BASE_VALUE, INVOICE, NET_VALUE, POSITION_NUMBER, REFERENCE_ID, TEXT) "
                + " SELECT FI_BILLPOS_ID, FI_BILL_ID, " + currentDate() + ", " + jobUserId + ", AMOUNT, BASE_VALUE, INVOICE, NET_VALUE, POSITION_NUMBER, REFERENCE_ID, TEXT FROM IMEX_FI_BILLPOS "); // WHERE IS_NEW_BILLPOS = 1 ");

        printTime("  " + c + " new SAP_FI_BILLPOSITIONs created!");

        printTime("  Create new SAP_FI_OPEN_ITEMs...");

        c = pQry.executeUpdate("INSERT /*+ PARALLEL(T_SAP_FI_OPEN_ITEMS 5)*/ INTO T_SAP_FI_OPEN_ITEMS (ID, T_CASE_ID, CREATION_DATE, CREATION_USER, COMPANY_CODE, CURRENCY_KEY, CUSTOMER_NUMBER, DEBIT_CREDIT_KEY, FISCAL_YEAR, KIND_OF_RECEIPT, NET_VALUE, NUMBER_RECEIPT, ORDERDATE_RECEIPT, POSTING_KEY, RECEIPTDATE_RECEIPT, RECORDINGDATE_RECEIPT, REF_NUMBER, REFNUMBER__RECEIPT, TEXT, VALUE) "
                + " SELECT FI_OPEN_ITEMS_ID, NEW_CASE_ID, " + currentDate() + ", " + jobUserId + ", COMPANY_CODE, CURRENCY_KEY, CUSTOMER_NUMBER, DEBIT_CREDIT_KEY, FISCAL_YEAR, KIND_OF_RECEIPT, NET_VALUE, NUMBER_RECEIPT, ORDERDATE_RECEIPT, POSTING_KEY, RECEIPTDATE_RECEIPT, RECORDINGDATE_RECEIPT, REF_NUMBER, REFNUMBER_RECEIPT, TEXT, VALUE FROM IMEX_FI_OPEN_ITEMS "); // WHERE IS_NEW_OPEN_ITEMS = 1 ");

        printTime("  " + c + " new SAP_FI_OPEN_ITEMs created!");

        printTime("  Update T_CASE.CS_BILLING_DATE...");
        pQry.executeUpdate(BILLING_DATE_SQL + ", MODIFICATION_DATE = " + currentDate() + ", MODIFICATION_USER = " + jobUserId + " WHERE EXISTS (SELECT 1 FROM IMEX_FI_BILL WHERE IMEX_FI_BILL.NEW_CASE_ID = T_CASE.ID) AND T_CASE.CS_BILLING_DATE IS NULL");
        printTime("  T_CASE.CS_BILLING_DATE updated!");
    }
    
    protected void cancelMergedCases(final Query pQry)throws SQLException, IOException, ParseException {
        ResultSet rs1 = pQry.executeQuery("SELECT COUNT(*) CNT FROM IMEX_CANCEL_CASE_TMP");
        int numberOfCases = 0;
        while (rs1.next()) {
            numberOfCases = rs1.getInt("CNT");
        }
        printTime("There are " + numberOfCases + "to be cancelled after merging by kis import");
        pQry.executeUpdate("ALTER TABLE IMEX_CANCEL_CASE_TMP ADD CASE_ID " + (isOracle() ? "NUMBER(19, 0)" : "BIGINT")); 
        pQry.executeUpdate("ALTER TABLE IMEX_CANCEL_CASE_TMP ADD CASE_EXTERN_ID " + (isOracle() ? "NUMBER(19, 0)" : "BIGINT")); 
        pQry.executeUpdate("ALTER TABLE IMEX_CANCEL_CASE_TMP ADD CASE_LOCAL_ID " + (isOracle() ? "NUMBER(19, 0)" : "BIGINT")); 

        pQry.executeUpdate("CREATE INDEX IDX_IMEX_C_CASE_TMP_IKZ ON IMEX_CANCEL_CASE_TMP (IKZ)");
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_C_CASE_TMP_FALLNR ON IMEX_CANCEL_CASE_TMP (FALLNR)");

        printTime("Find cases that are to be cancelled in cpx db");
        pQry.executeUpdate("update IMEX_CANCEL_CASE_TMP  set case_id = (select id from T_CASE "
                + "where IMEX_CANCEL_CASE_TMP.fallnr = T_CASE.CS_CASE_NUMBER" 
                + " and IMEX_CANCEL_CASE_TMP.ikz = T_CASE.CS_HOSPITAL_IDENT)");

        printTime("Find case details that are to be cancelled in cpx db");
        pQry.executeUpdate(" update IMEX_CANCEL_CASE_TMP  set CASE_EXTERN_ID = " 
            + " (select id from T_CASE_DETAILS where T_CASE_DETAILS.T_CASE_ID = IMEX_CANCEL_CASE_TMP.CASE_ID"
            + " and T_CASE_DETAILS.ACTUAL_FL = 1 and T_CASE_DETAILS.LOCAL_FL = 0),"
            + " CASE_LOCAL_ID = (select id from T_CASE_DETAILS where T_CASE_DETAILS.T_CASE_ID = IMEX_CANCEL_CASE_TMP.CASE_ID"
            + " and T_CASE_DETAILS.ACTUAL_FL = 1 and T_CASE_DETAILS.LOCAL_FL = 1)");

        pQry.executeUpdate("CREATE INDEX IDX_IME_C_CASE_TMP_CASE_ID ON IMEX_CANCEL_CASE_TMP (CASE_ID)");
        pQry.executeUpdate("CREATE INDEX IDX_IME_C_CASE_TMP_CASE_EX_ID ON IMEX_CANCEL_CASE_TMP (CASE_EXTERN_ID)");
        pQry.executeUpdate("CREATE INDEX IDX_IME_C_CASE_TMP_CASE_LO_ID ON IMEX_CANCEL_CASE_TMP (CASE_LOCAL_ID)");

        printTime("now update T_CASE with cancel flag");
        pQry.executeUpdate("UPDATE T_CASE SET MODIFICATION_DATE = "
                + currentDate()
                + ", CANCEL_FL = 1"
            + " WHERE EXISTS(select 1 from IMEX_CANCEL_CASE_TMP where T_CASE.ID = IMEX_CANCEL_CASE_TMP.CASE_ID)");

        printTime("now write cancellation reason in T_CASE_DETAILS, extern case");
        pQry.executeUpdate(" UPDATE T_CASE_DETAILS SET MODIFICATION_DATE = " 
            +  currentDate()
            + ", CANCEL_DATE = "
            +  currentDate()
            + ", CANCEL_REASON_EN = (SELECT CANCEL_REASON FROM IMEX_CANCEL_CASE_TMP WHERE T_CASE_DETAILS.ID = IMEX_CANCEL_CASE_TMP.CASE_EXTERN_ID)"
            + " WHERE EXISTS(select 1 from IMEX_CANCEL_CASE_TMP where T_CASE_DETAILS.id = IMEX_CANCEL_CASE_TMP.CASE_EXTERN_ID)");

        printTime("now write cancellation reason in T_CASE_DETAILS, local case");

        pQry.executeUpdate(" UPDATE T_CASE_DETAILS SET MODIFICATION_DATE = " 
            +  currentDate()
            + ", CANCEL_DATE = "
            +  currentDate()
            + ", CANCEL_REASON_EN = (SELECT CANCEL_REASON FROM IMEX_CANCEL_CASE_TMP WHERE T_CASE_DETAILS.ID = IMEX_CANCEL_CASE_TMP.CASE_LOCAL_ID)"
            + " WHERE EXISTS(select 1 from IMEX_CANCEL_CASE_TMP where T_CASE_DETAILS.id = IMEX_CANCEL_CASE_TMP.CASE_LOCAL_ID)");

    }

    protected void createNewKains(final Query pQry, final Query pCommonDbQuery) throws SQLException, IOException, ParseException {
        int c;
//        printTime("Create IMEX_KAIN_INKA_PROCESS...");
//
//        dropTable(pQry, "IMEX_KAIN_INKA_PROCESS");
//
//        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_KAIN_INKA_PROCESS NOLOGGING PARALLEL 5 AS " : "")
//                + "SELECT " 
//                + "   TMP.*, "
//                + "   CASE WHEN OLD_PROCESS_ID IS NOT NULL THEN OLD_PROCESS_ID ELSE NULL END NEW_PROCESS_ID "
//                + (isOracle() ? "" : " INTO IMEX_KAIN_INKA_PROCESS ")
//                + " FROM ("
//                + "   SELECT " 
//                + "     TMP.*, "
//                + "     (SELECT MAX(T_WM_PROCESS.ID) FROM T_WM_PROCESS INNER JOIN T_WM_PROCESS_T_CASE ON T_WM_PROCESS_T_CASE.CASE_ID = OLD_CASE_ID WHERE T_WM_PROCESS.PROCESS_TYPE = 'HOSPITAL' AND T_WM_PROCESS.MODIFICATION_DATE IS NOT NULL AND T_WM_PROCESS.MODIFICATION_DATE = (SELECT MAX(PROC.MODIFICATION_DATE) FROM T_WM_PROCESS PROC INNER JOIN T_WM_PROCESS_T_CASE PROC_CASE ON PROC_CASE.PROCESS_ID = PROC.ID WHERE PROC_CASE.CASE_ID = OLD_CASE_ID)) OLD_PROCESS_ID "
//                + "   FROM ("
//                + "     SELECT "
//                + "       TMP.*, "
//                + "       IMEX_CASE.OLD_CASE_ID, "
//                + "       IMEX_CASE.NEW_CASE_ID "
//                + "     FROM ("
//                + "       SELECT "
//                + "         IMEX_KAIN_INKA_TMP.IKZ, "
//                + "         IMEX_KAIN_INKA_TMP.FALLNR "
//                + "       FROM IMEX_KAIN_INKA_TMP "
//                + "       GROUP BY IMEX_KAIN_INKA_TMP.IKZ, IMEX_KAIN_INKA_TMP.FALLNR "        
//                + "     ) TMP "
//                + "     LEFT JOIN IMEX_CASE ON IMEX_CASE.IKZ = TMP.IKZ AND IMEX_CASE.FALLNR = TMP.FALLNR "
//                + "   ) TMP "
//                + " ) TMP ");
//        pQry.executeUpdate("UPDATE IMEX_KAIN_INKA_PROCESS SET NEW_PROCESS_ID = " + nextSqVal("T_WM_PROCESS_SQ") + " WHERE NEW_PROCESS_ID IS NULL");
//
//        pQry.executeUpdate("CREATE INDEX IDX_IMEX_KAIN_INKA_PROC_FALL ON IMEX_KAIN_INKA_PROCESS (IKZ, FALLNR) " + parallel());
//        //qry.executeUpdate("CREATE INDEX IDX_IMEX_KAIN_INKA_PROC_PATNR ON IMEX_KAIN_INKA_PROCESS (PATNR) " + parallel());
//        //qry.executeUpdate("CREATE INDEX IDX_IMEX_KAIN_INKA_PROC_IS_NEW_CASE ON IMEX_CASE (IS_NEW_PROCESS)");
//        //qry.executeUpdate(DbBuilder.getImexPkDefinition("IMEX_KAIN_INKA_PROCESS"));
//
//        printTime("IMEX_KAIN_INKA_PROCESS created!");

        pQry.executeUpdate("CREATE INDEX IDX_IMEX_KAIN_INKA_TMP_EXT_MSG ON IMEX_KAIN_INKA_TMP (CPX_EXTERNAL_MSG_NR)");

        Long kainReminderId = getKainReminderInternalId(pCommonDbQuery);
        Long adminId = getAdminId(pCommonDbQuery);
        Long countExistingKAINMessages = getCountExistingKAINMessages(pQry);

        printTime("KAIN Reminder Internal ID is " + (kainReminderId == null ? "NULL" : kainReminderId));
        printTime("Admin-ID is " + (adminId == null ? "NULL" : adminId));

        printTime("Count of already existing KAIN-Messages  " + (adminId == null ? "NULL" : countExistingKAINMessages));
        printTime("Delete of already existing KAIN-Messages in IMEX_KAIN_INKA_TMP");
        getProgressor().sendProgress("Lösche bereits existierende KAIN-Nachrichten in IMEX_KAIN_INKA_TMP");

        c = pQry.executeUpdate("DELETE /*+ PARALLEL(IMEX_KAIN_INKA_TMP 5)*/ IMEX_KAIN_INKA_TMP "
                + " WHERE EXISTS "
                + " (SELECT 1 FROM T_P301_KAIN_INKA "
                + " WHERE IMEX_KAIN_INKA_TMP.CPX_EXTERNAL_MSG_NR = T_P301_KAIN_INKA.CPX_EXTERNAL_MSG_NR)");

        printTime(" " + c + " already existing KAIN-Messages deleted!");

        getProgressor().sendProgress("Lösche PVV-Segmente zu bereits existierenden KAIN-Nachrichten in IMEX_KAIN_INKA_PVV_TMP");
        pQry.executeUpdate("DELETE /*+ PARALLEL(IMEX_KAIN_INKA_PVV_TMP 5)*/ IMEX_KAIN_INKA_PVV_TMP "
                + " WHERE NOT EXISTS "
                + " (SELECT 1 FROM IMEX_KAIN_INKA_TMP "
                + " WHERE IMEX_KAIN_INKA_TMP.NR = IMEX_KAIN_INKA_PVV_TMP.KAIN_INKA_NR)");

        getProgressor().sendProgress("Lösche PVT-Segmente zu bereits existierenden KAIN-Nachrichten in IMEX_KAIN_INKA_PVT_TMP");
        pQry.executeUpdate("DELETE /*+ PARALLEL(IMEX_KAIN_INKA_PVT_TMP 5)*/ IMEX_KAIN_INKA_PVT_TMP "
                + " WHERE NOT EXISTS "
                + " (SELECT 1 FROM IMEX_KAIN_INKA_PVV_TMP "
                + " WHERE IMEX_KAIN_INKA_PVV_TMP.NR = IMEX_KAIN_INKA_PVT_TMP.KAIN_INKA_PVV_NR)");
        
        printTime("Determine new Workflow Processes");
        printTime("CREATE IMEX_NEW_PROCESSES");

        dropTable(pQry, "IMEX_NEW_PROCESSES");

        final String wmStateOffen = "1";

        getProgressor().sendProgress("Erzeuge IMEX_NEW_PROCESSES");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_NEW_PROCESSES NOLOGGING PARALLEL 5 AS " : "")
                + "SELECT "
                + "   TMP.* "
                + (isOracle() ? "" : " INTO IMEX_NEW_PROCESSES ")
                + " FROM ("
                + "   SELECT "
                + "     TMP.*, "
                + "     CASE WHEN PROCESS_ID IS NULL THEN 1 ELSE 0 END IS_NEW_PROCESS, "
                + "     CASE WHEN PROCESS_ID IS NULL THEN NULL ELSE T_WM_PROCESS.WORKFLOW_NUMBER END NEW_WORKFLOW_NUMBER "
                + "   FROM ("
                + "     SELECT "
                + "       MIN(IMEX_KAIN_INKA_TMP.NR) NR, "
                + "       IMEX_KAIN_INKA_TMP.IKZ, "
                + "       IMEX_KAIN_INKA_TMP.FALLNR, "
                + "       T_CASE.T_PATIENT_ID NEW_PATIENT_ID, "
                + "       T_CASE.ID NEW_CASE_ID, "
                + "       T_CASE_DETAILS.ID NEW_CASE_DETAILS_ID, "
                + "       T_PATIENT.PAT_NUMBER PATNR, "
                + "       (SELECT T_WM_PROCESS_T_CASE.T_WM_PROCESS_ID FROM T_WM_PROCESS_T_CASE "
                + "         INNER JOIN T_WM_PROCESS ON T_WM_PROCESS.ID = T_WM_PROCESS_T_CASE.T_WM_PROCESS_ID "
                + "         WHERE T_WM_PROCESS_T_CASE.T_CASE_ID = T_CASE.ID AND T_WM_PROCESS_T_CASE.MAIN_CASE_FL = 1 AND "
                + "         T_WM_PROCESS.CANCEL_FL = 0 AND " 
                + isNull2("T_WM_PROCESS.LAST_PROCESS_MODIFICATION", "T_WM_PROCESS.MODIFICATION_DATE") + " = "
                + "         (SELECT MAX(" + isNull2("T_WM_PROCESS.LAST_PROCESS_MODIFICATION", "T_WM_PROCESS.MODIFICATION_DATE") + ") "
                + "             FROM T_WM_PROCESS INNER JOIN T_WM_PROCESS_T_CASE ON (T_WM_PROCESS_T_CASE.T_WM_PROCESS_ID = T_WM_PROCESS.ID AND "
                + "             T_WM_PROCESS_T_CASE.MAIN_CASE_FL = 1 AND T_WM_PROCESS.CANCEL_FL = 0)"
                + "             WHERE T_WM_PROCESS_T_CASE.T_CASE_ID = T_CASE.ID)) PROCESS_ID "
                + "     FROM IMEX_KAIN_INKA_TMP "
                //  + "     INNER JOIN IMEX_PATIENT ON IMEX_PATIENT.PATNR = IMEX_KAIN_INKA_TMP.PATNR "        
                //  + "     INNER JOIN IMEX_CASE ON IMEX_CASE.IKZ = IMEX_KAIN_INKA_TMP.IKZ AND IMEX_CASE.FALLNR = IMEX_KAIN_INKA_TMP.FALLNR "        
                + "     INNER JOIN T_CASE ON T_CASE.CS_HOSPITAL_IDENT = IMEX_KAIN_INKA_TMP.IKZ AND T_CASE.CS_CASE_NUMBER = IMEX_KAIN_INKA_TMP.FALLNR "
                + "     LEFT JOIN T_CASE_DETAILS ON (T_CASE.ID = T_CASE_DETAILS.T_CASE_ID AND T_CASE_DETAILS.LOCAL_FL = 0 AND T_CASE_DETAILS.ACTUAL_FL = 1) "
                + "     LEFT JOIN T_PATIENT ON T_CASE.T_PATIENT_ID = T_PATIENT.ID "
                + "     LEFT JOIN T_P301_KAIN_INKA ON T_P301_KAIN_INKA.CPX_EXTERNAL_MSG_NR = IMEX_KAIN_INKA_TMP.CPX_EXTERNAL_MSG_NR AND T_P301_KAIN_INKA.T_CASE_ID = T_CASE.ID "
                + "     GROUP BY IMEX_KAIN_INKA_TMP.IKZ, IMEX_KAIN_INKA_TMP.FALLNR, T_CASE.T_PATIENT_ID, T_CASE.ID, T_CASE_DETAILS.ID, T_PATIENT.PAT_NUMBER "
                + "   ) TMP "
                + "   LEFT JOIN T_WM_PROCESS ON T_WM_PROCESS.ID = TMP.PROCESS_ID "
                + " ) TMP ");
        pQry.executeUpdate("UPDATE IMEX_NEW_PROCESSES SET PROCESS_ID = " + nextSqVal("T_WM_PROCESS_SQ") + " WHERE PROCESS_ID IS NULL");
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_NEW_PROCESSES_FALL ON IMEX_NEW_PROCESSES (IKZ, FALLNR) " + parallel());
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_NEW_PROCESSES_PATNR ON IMEX_NEW_PROCESSES (PATNR) " + parallel());
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_NEW_PROCESSES_CASE_ID ON IMEX_NEW_PROCESSES (NEW_CASE_ID) " + parallel());
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_NEW_PROC_IS_NEW_PROC ON IMEX_NEW_PROCESSES (IS_NEW_PROCESS)");
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_NEW_PROC_NR ON IMEX_NEW_PROCESSES (NR)");
        //pQry.executeUpdate(DbBuilder.getImexPkDefinition("IMEX_NEW_PROCESSES"));

        printTime("IMEX_NEW_PROCESSES created!");

        List<Long> list = new ArrayList<>();
        try (ResultSet rs = pQry.executeQuery("SELECT NR FROM IMEX_NEW_PROCESSES WHERE IS_NEW_PROCESS = 1")) {
            while (rs.next()) {
                long nr = rs.getLong("NR");
                list.add(nr);
            }
        }
        for (Long nr : list) {
            long workflowNumber = getNewWorkflowNumber(pCommonDbQuery);
            pQry.executeUpdate("UPDATE IMEX_NEW_PROCESSES SET NEW_WORKFLOW_NUMBER = '" + workflowNumber + "' WHERE NR = " + nr);
        }

        printTime("Create new T_WM_PROCESSes...");

        getProgressor().sendProgress("Erzeuge neue Einträge in T_WM_PROCESS");
        pQry.executeUpdate("INSERT /*+ PARALLEL(T_WM_PROCESS 5)*/ INTO T_WM_PROCESS (ID, T_PATIENT_ID, PROCESS_TYPE, CREATION_DATE, CREATION_USER, MODIFICATION_DATE, LAST_PROCESS_MODIFICATION, PROCESS_MODIFICATION_USER, WORKFLOW_NUMBER, WM_STATE, WM_TYPE, CANCEL_FL) "
                + " SELECT PROCESS_ID, NEW_PATIENT_ID, 'HOSPITAL', " + currentDate() + ", " + jobUserId + ", " + currentDate() + ", " + currentDate() + ", " + jobUserId + ", NEW_WORKFLOW_NUMBER, " + wmStateOffen + ", 'statKH', 0 FROM IMEX_NEW_PROCESSES WHERE IS_NEW_PROCESS = 1 ");

        c = pQry.executeUpdate("INSERT /*+ PARALLEL(T_WM_PROCESS_HOSPITAL 5)*/ INTO T_WM_PROCESS_HOSPITAL (ID, IS_CLOSED) "
                + " SELECT PROCESS_ID, 0 FROM IMEX_NEW_PROCESSES WHERE IS_NEW_PROCESS = 1 ");

        printTime(" " + c + " new T_WM_PROCESSes created!");

        printTime("Create new T_WM_PROCESS_T_CASEs...");

        c = pQry.executeUpdate("INSERT /*+ PARALLEL(T_WM_PROCESS_T_CASE 5)*/ INTO T_WM_PROCESS_T_CASE (ID, T_WM_PROCESS_ID, T_CASE_ID, CREATION_DATE, CREATION_USER, MAIN_CASE_FL, T_CASE_DETAILS_ID) "
                + " SELECT " + nextSqVal("T_WM_PROCESS_T_CASE_SQ") + ", PROCESS_ID, NEW_CASE_ID, " + currentDate() + ", " + jobUserId + ", 1, NEW_CASE_DETAILS_ID FROM IMEX_NEW_PROCESSES WHERE IS_NEW_PROCESS = 1 ");

        printTime(" " + c + " new T_WM_PROCESS_T_CASEs created!");

        printTime("Create IMEX_KAIN_INKA...");

        dropTable(pQry, "IMEX_KAIN_INKA");

        getProgressor().sendProgress("Erzeuge IMEX_KAIN_INKA");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_KAIN_INKA NOLOGGING PARALLEL 5 AS " : "")
                + "SELECT "
                + "   TMP.* "
                + (isOracle() ? "" : " INTO IMEX_KAIN_INKA ")
                + " FROM ("
                + "   SELECT "
                + "     TMP.*, "
                + "     CASE WHEN P301_KAIN_INKA_ID IS NULL THEN 1 ELSE 0 END IS_NEW_KAIN_INKA "
                + "   FROM ("
                + "     SELECT "
                + "       IMEX_KAIN_INKA_TMP.*, "
                + "       T_CASE.T_PATIENT_ID NEW_PATIENT_ID, "
                + "       T_CASE.ID NEW_CASE_ID, "
                + "       T_PATIENT.PAT_NUMBER, "
                + "       T_P301_KAIN_INKA.ID P301_KAIN_INKA_ID, "
                + "       IMEX_NEW_PROCESSES.PROCESS_ID, "
                + "       IMEX_NEW_PROCESSES.IS_NEW_PROCESS, "
                + "       IMEX_NEW_PROCESSES.NEW_WORKFLOW_NUMBER "
                + "     FROM IMEX_KAIN_INKA_TMP "
                //  + "     INNER JOIN IMEX_PATIENT ON IMEX_PATIENT.PATNR = IMEX_KAIN_INKA_TMP.PATNR "        
                //  + "     INNER JOIN IMEX_CASE ON IMEX_CASE.IKZ = IMEX_KAIN_INKA_TMP.IKZ AND IMEX_CASE.FALLNR = IMEX_KAIN_INKA_TMP.FALLNR "        
                + "     INNER JOIN T_CASE ON T_CASE.CS_HOSPITAL_IDENT = IMEX_KAIN_INKA_TMP.IKZ AND T_CASE.CS_CASE_NUMBER = IMEX_KAIN_INKA_TMP.FALLNR "
                + "     LEFT JOIN T_PATIENT ON T_CASE.T_PATIENT_ID = T_PATIENT.ID "
                //+ "     LEFT JOIN (SELECT MIN(ID) ID, T_CASE_ID, CURRENT_TRANSACTION_NR FROM T_P301_KAIN_INKA GROUP BY T_CASE_ID, CURRENT_TRANSACTION_NR) T_P301_KAIN_INKA ON T_P301_KAIN_INKA.CURRENT_TRANSACTION_NR = IMEX_KAIN_INKA_TMP.CURRENT_TRANSACTION_NR AND T_P301_KAIN_INKA.T_CASE_ID = T_CASE.ID "
                + "     LEFT JOIN (SELECT MIN(ID) ID, T_CASE_ID, CPX_EXTERNAL_MSG_NR FROM T_P301_KAIN_INKA GROUP BY T_CASE_ID, CPX_EXTERNAL_MSG_NR) T_P301_KAIN_INKA ON T_P301_KAIN_INKA.CPX_EXTERNAL_MSG_NR = IMEX_KAIN_INKA_TMP.CPX_EXTERNAL_MSG_NR AND T_P301_KAIN_INKA.T_CASE_ID = T_CASE.ID "
                + "     INNER JOIN IMEX_NEW_PROCESSES ON IMEX_NEW_PROCESSES.IKZ = IMEX_KAIN_INKA_TMP.IKZ AND IMEX_NEW_PROCESSES.FALLNR = IMEX_KAIN_INKA_TMP.FALLNR "
                + "   ) TMP "
                + "   LEFT JOIN T_WM_PROCESS ON T_WM_PROCESS.ID = TMP.PROCESS_ID "
                + " ) TMP ");
        pQry.executeUpdate("UPDATE IMEX_KAIN_INKA SET P301_KAIN_INKA_ID = " + nextSqVal("T_P301_KAIN_INKA_SQ") + " WHERE P301_KAIN_INKA_ID IS NULL");

        //getNewWorkflowNumber(pCommonDbQuery);
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_KAIN_INKA_FALL ON IMEX_KAIN_INKA (IKZ, FALLNR) " + parallel());
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_KAIN_INKA_PATNR ON IMEX_KAIN_INKA (PATNR) " + parallel());
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_KAIN_INKA_IS_NEW_KAIN ON IMEX_KAIN_INKA (IS_NEW_KAIN_INKA)");
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_KAIN_INKA_IS_NEW_PROC ON IMEX_KAIN_INKA (IS_NEW_PROCESS)");
        pQry.executeUpdate(DbBuilder.getImexPkDefinition("IMEX_KAIN_INKA"));

        printTime("IMEX_KAIN_INKA created!");

        printTime("Create IMEX_KAIN_INKA_PVV...");

        dropTable(pQry, "IMEX_KAIN_INKA_PVV");

        getProgressor().sendProgress("Erzeuge IMEX_KAIN_INKA_PVV");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_KAIN_INKA_PVV NOLOGGING PARALLEL 5 AS " : "")
                + "SELECT "
                + "   TMP.*, "
                + "   CASE WHEN P301_KAIN_INKA_PVV_ID IS NULL THEN 1 ELSE 0 END IS_NEW_KAIN_INKA_PVV "
                + (isOracle() ? "" : " INTO IMEX_KAIN_INKA_PVV ")
                + " FROM ("
                + "   SELECT "
                + "     IMEX_KAIN_INKA_PVV_TMP.*, "
                + "     IMEX_KAIN_INKA.NEW_PATIENT_ID, "
                + "     IMEX_KAIN_INKA.NEW_CASE_ID, "
                + "     IMEX_KAIN_INKA.P301_KAIN_INKA_ID, "
                + "     IMEX_KAIN_INKA.PROCESS_ID, "
                + "     T_P301_KAIN_INKA_PVV.ID P301_KAIN_INKA_PVV_ID "
                + "   FROM IMEX_KAIN_INKA_PVV_TMP "
                + "   INNER JOIN IMEX_KAIN_INKA ON IMEX_KAIN_INKA.NR = IMEX_KAIN_INKA_PVV_TMP.KAIN_INKA_NR "
                + "   LEFT JOIN T_P301_KAIN_INKA_PVV ON (T_P301_KAIN_INKA_PVV.T_P301_KAIN_INKA_ID = IMEX_KAIN_INKA.P301_KAIN_INKA_ID AND T_P301_KAIN_INKA_PVV.INFORMATION_KEY_30 = IMEX_KAIN_INKA_PVV_TMP.INFORMATION_KEY_30) "
                + " ) TMP ");
        pQry.executeUpdate("UPDATE IMEX_KAIN_INKA_PVV SET P301_KAIN_INKA_PVV_ID = " + nextSqVal("T_P301_KAIN_INKA_PVV_SQ") + " WHERE P301_KAIN_INKA_PVV_ID IS NULL");

        pQry.executeUpdate("CREATE INDEX IDX_IMEX_KAIN_INKA_PVV_FALL ON IMEX_KAIN_INKA_PVV (IKZ, FALLNR) " + parallel());
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_KAIN_INKA_PVV_PATNR ON IMEX_KAIN_INKA_PVV (PATNR) " + parallel());
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_KAIN_INKA_PVV_IS_NEW ON IMEX_KAIN_INKA_PVV (IS_NEW_KAIN_INKA_PVV)");
        pQry.executeUpdate(DbBuilder.getImexPkDefinition("IMEX_KAIN_INKA_PVV"));

        printTime("IMEX_KAIN_INKA_PVV created!");

        printTime("  Create new KAINs...");

        pQry.executeUpdate("INSERT /*+ PARALLEL(T_P301_KAIN_INKA 5)*/ INTO T_P301_KAIN_INKA (ID, CREATION_DATE, CREATION_USER, CONTRACT_REFERENCE, COST_UNIT_SAP, CURRENT_TRANSACTION_NR, CPX_EXTERNAL_MSG_NR, HOSPITAL_IDENTIFIER, HOSPITAL_NUMBER_PATIENT, INSURANCE_CASE_NUMBER, INSURANCE_IDENTIFIER, INSURANCE_REF_NUMBER, MESSAGE_TYPE, PROCESSING_REF, T_CASE_ID) "
                + " SELECT P301_KAIN_INKA_ID, " + currentDate() + ", " + jobUserId + ", CONTRACT_REFERENCE, COST_UNIT_SAP, CURRENT_TRANSACTION_NR, CPX_EXTERNAL_MSG_NR, HOSPITAL_IDENTIFIER, HOSPITAL_NUMBER_PATIENT, INSURANCE_CASE_NUMBER, INSURANCE_IDENTIFIER, INSURANCE_REF_NUMBER, MESSAGE_TYPE, PROCESSING_REF, NEW_CASE_ID FROM IMEX_KAIN_INKA WHERE IS_NEW_KAIN_INKA = 1 ");

        c = pQry.executeUpdate("INSERT /*+ PARALLEL(T_P301_KAIN 5)*/ INTO T_P301_KAIN (ID, RECEIVING_DATE) "
                + " SELECT P301_KAIN_INKA_ID, RECEIVING_DATE FROM IMEX_KAIN_INKA WHERE IS_NEW_KAIN_INKA = 1 ");

        printTime("  " + c + " new KAINs created!");

        printTime("  Create new KAIN PVVs...");

        c = pQry.executeUpdate("INSERT /*+ PARALLEL(T_P301_KAIN_INKA_PVV 5)*/ INTO T_P301_KAIN_INKA_PVV (ID, T_P301_KAIN_INKA_ID, CREATION_DATE, CREATION_USER, BILL_DATE, BILL_NR, INFORMATION_KEY_30) "
                + " SELECT P301_KAIN_INKA_PVV_ID, P301_KAIN_INKA_ID, " + currentDate() + ", " + jobUserId + ", BILL_DATE, BILL_NR, INFORMATION_KEY_30 FROM IMEX_KAIN_INKA_PVV WHERE IS_NEW_KAIN_INKA_PVV = 1 ");

        printTime("  " + c + " new KAIN PVVs created!");

        printTime("  Create new KAIN PVTs...");

        c = pQry.executeUpdate("INSERT /*+ PARALLEL(T_P301_KAIN_INKA_PVT 5)*/ INTO T_P301_KAIN_INKA_PVT (ID, T_P301_KAIN_INKA_PVV_ID, CREATION_DATE, CREATION_USER, OPS_LOCALISATION_EN, MAIN_DIAG_ICD, MAIN_DIAG_LOC_EN, MAIN_DIAG_SECONDARY_ICD, MAIN_DIAG_SECONDARY_LOC_EN, OPS_CODE, SECONDARY_DIAG_ICD, SECONDARY_DIAG_LOC_EN, SECONDARY_SECOND_DIAG_ICD, SECONDARY_SECOND_DIAG_LOC_EN, TEXT) "
                + " SELECT " + nextSqVal("T_P301_KAIN_INKA_PVT_SQ") + ", P301_KAIN_INKA_PVV_ID, " + currentDate() + ", " + jobUserId + ", OPS_LOCALISATION_EN, MAIN_DIAG_ICD, MAIN_DIAG_LOC_EN, MAIN_DIAG_SECONDARY_ICD, MAIN_DIAG_SECONDARY_LOC_EN, OPS_CODE, SECONDARY_DIAG_ICD, SECONDARY_DIAG_LOC_EN, SECONDARY_SECOND_DIAG_ICD, SECONDARY_SECOND_DIAG_LOC_EN, TEXT "
                + " FROM IMEX_KAIN_INKA_PVT_TMP "
                + " INNER JOIN IMEX_KAIN_INKA_PVV ON IMEX_KAIN_INKA_PVV.NR = IMEX_KAIN_INKA_PVT_TMP.KAIN_INKA_PVV_NR "
                + " WHERE IMEX_KAIN_INKA_PVV.IS_NEW_KAIN_INKA_PVV = 1 ");

        printTime("  " + c + " new KAIN PVTs created!");

        pQry.executeUpdate("ALTER TABLE IMEX_KAIN_INKA ADD REMINDER_ID " + (isOracle() ? "NUMBER(19, 0)" : "BIGINT"));

        if (kainReminderId != null) {
            printTime("  Create new Reminders...");

            pQry.executeUpdate("UPDATE IMEX_KAIN_INKA SET REMINDER_ID = " + nextSqVal("T_WM_REMINDER_SQ") + " WHERE IS_NEW_KAIN_INKA = 1");

            Long assignedUserId = adminId;
            c = pQry.executeUpdate("INSERT /*+ PARALLEL(T_WM_REMINDER 5)*/ INTO T_WM_REMINDER (ID, T_WM_PROCESS_ID, CREATION_DATE, CREATION_USER, FINISHED_FL, HIGH_PRIO_FL, SUBJECT, ASSIGNED_USER_ID, DUE_DATE) "
                    + " SELECT REMINDER_ID, PROCESS_ID, " + currentDate() + ", " + jobUserId + ", 0, 0, " + kainReminderId + ", " + (assignedUserId == null ? "NULL" : assignedUserId) + ", " + currentDate()
                    + " FROM IMEX_KAIN_INKA "
                    + " WHERE IMEX_KAIN_INKA.IS_NEW_KAIN_INKA = 1 ");
            printTime("  " + c + " new Reminders created!");
        } else {
            printTime("  No reminder subject for KAIN found, so I cannot create reminders for incoming KAIN messages!");
        }

        printTime("  Create new Events...");

//        pQry.executeUpdate("ALTER TABLE IMEX_KAIN_INKA_PVV ADD EVENT_SUBJECT VARCHAR(256)");
//
//        pQry.executeUpdate("UPDATE IMEX_KAIN_INKA_PVV SET EVENT_SUBJECT = " + currentGermanDate() + " || ' SAP-Import\n\rKAIN Nachricht empfangen: Schlüssel: ' || INFORMATION_KEY_30 || ' ' || " + Key30.getSqlCase("INFORMATION_KEY_30") + " WHERE IMEX_KAIN_INKA_PVV.IS_NEW_KAIN_INKA_PVV = 1");
//        String subject = "<Datum Uhrzeit> <Ersteller>\n" +
//                         "KAIN Nachricht empfangen: <Schlüssel 30 -Code> <Schlüssel 30 Text zum Code>";
        int eventType = 701;
        c = pQry.executeUpdate("INSERT /*+ PARALLEL(T_WM_EVENT 5)*/ INTO T_WM_EVENT (ID, EVENT_TYPE, CREATION_DATE, CREATION_USER, SUBJECT, T_WM_PROCESS_ID, T_CASE_ID, T_WM_REMINDER_ID, T_P301_KAIN_INKA_ID) "
                + " SELECT " + nextSqVal("T_WM_EVENT_SQ") + ", " + eventType + ", " + currentDate() + ", " + jobUserId + ", 'SAP-Import', IMEX_KAIN_INKA.PROCESS_ID, IMEX_KAIN_INKA.NEW_CASE_ID, IMEX_KAIN_INKA.REMINDER_ID, IMEX_KAIN_INKA.P301_KAIN_INKA_ID "
                + " FROM IMEX_KAIN_INKA "
                + //   " INNER JOIN IMEX_KAIN_INKA_PVV ON IMEX_KAIN_INKA_PVV.KAIN_INKA_NR = IMEX_KAIN_INKA.NR " + 
                " WHERE IMEX_KAIN_INKA.IS_NEW_KAIN_INKA = 1 ");

        printTime("  " + c + " new Events created!");

//        printTime("  Create new process...");
//        
//        final String wmState = "1"; //1 = offen
//        final String wmType = "statKH"; //statKH = offen
//        final String processType = "HOSPITAL";
//        final String processModificationUser = "0";
//        
//        pQry.executeUpdate("INSERT /*+ PARALLEL(T_WM_PROCESS 5)*/ INTO T_WM_PROCESS (ID, PROCESS_TYPE, PROCESS_COMMENT, CREATION_DATE, MODIFICATION_DATE, WM_STATE, WM_TYPE, PROCESS_MODIFICATION_USER,) " + 
//                " SELECT NEW_PROCESS_ID, '" + processType + "', 'automatically created by SAP Import', " + currentDate() + ", " + currentDate() + ", '" + wmState + "', '" + wmType + "', " + processModificationUser + ", NEW_PATIENT_ID FROM IMEX_KAIN_INKA WHERE IS_NEW_PROCESS = 1 ");
//        
//        pQry.executeUpdate("INSERT /*+ PARALLEL(T_WM_PROCESS_HOSPITAL 5)*/ INTO T_WM_PROCESS_HOSPITAL (ID, IS_CLOSED) " + 
//                " SELECT NEW_PROCESS_ID, 0 FROM IMEX_KAIN_INKA WHERE IS_NEW_PROCESS = 1 ");
//        
//        pQry.executeUpdate("INSERT /*+ PARALLEL(T_WM_PROCESS_T_CASE 5)*/ INTO T_WM_PROCESS_T_CASE (ID, CREATION_DATE, MODIFICATION_DATE, PROCESS_ID, CASE_ID, MAIN_CASE_FL) " + 
//                " SELECT " + nextSqVal("T_WM_PROCESS_T_CASE_SQ") + ", " + currentDate() + ", " + currentDate() + ", NEW_PROCESS_ID, NEW_CASE_ID, 1 FROM IMEX_KAIN_INKA WHERE IS_NEW_PROCESS = 1 ");
//        printTime("  New process created!");
    }

    protected void createNewCaseIds(final Query pQry) throws SQLException, IOException, ParseException {

        //dropTable(qry, "IMEX_CASE_ACT_VERSION");
        //dropTable(qry, "IMEX_CASE_NEW_VERSION");
        dropTable(pQry, "IMEX_CASE_NEW_LOCAL_ID");
        dropTable(pQry, "IMEX_CASE_NEW_EXTERN_ID");
        dropTable(pQry, "IMEX_DEPARTMENT_NEW_LOCAL_ID");
        dropTable(pQry, "IMEX_DEPARTMENT_NEW_EXTERN_ID");
        dropTable(pQry, "IMEX_DIAGNOSE_NEW_LOCAL_ID");
        dropTable(pQry, "IMEX_DIAGNOSE_NEW_EXTERN_ID");
        dropTable(pQry, "IMEX_PROCEDURE_NEW_LOCAL_ID");
        dropTable(pQry, "IMEX_PROCEDURE_NEW_EXTERN_ID");
        dropTable(pQry, "IMEX_FEE_NEW_LOCAL_ID");
        dropTable(pQry, "IMEX_FEE_NEW_EXTERN_ID");

        printTime("  Create new case detail ids...");
        getProgressor().sendProgress("Erzeuge IMEX_CASE_NEW_EXTERN_ID");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_CASE_NEW_EXTERN_ID NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT IMEX_CASE.NR, IMEX_CASE.NEW_CASE_ID, IMEX_CASE.IKZ, IMEX_CASE.FALLNR, " + nextSqVal("T_CASE_DETAILS_SQ") + " NEW_DETAILS_ID "
                + (isOracle() ? "" : " INTO IMEX_CASE_NEW_EXTERN_ID ")
                + " FROM IMEX_CASE "
                + " WHERE IMEX_CASE.NEW_VERSION = 1 AND IMEX_CASE.IGNORE_CASE = 0");

        getProgressor().sendProgress("Erzeuge IMEX_CASE_NEW_LOCAL_ID");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_CASE_NEW_LOCAL_ID NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT IMEX_CASE.NR, IMEX_CASE.NEW_CASE_ID, IMEX_CASE.IKZ, IMEX_CASE.FALLNR, " + nextSqVal("T_CASE_DETAILS_SQ") + " NEW_DETAILS_ID, IMEX_CASE_NEW_EXTERN_ID.NEW_DETAILS_ID NEW_EXT_DETAILS_ID "
                + (isOracle() ? "" : " INTO IMEX_CASE_NEW_LOCAL_ID ")
                + " FROM IMEX_CASE "
                + " INNER JOIN IMEX_CASE_NEW_EXTERN_ID ON IMEX_CASE_NEW_EXTERN_ID.NR = IMEX_CASE.NR "
                + " WHERE IMEX_CASE.NEW_VERSION = 1 AND IMEX_CASE.IGNORE_CASE = 0");

        //pQry.executeUpdate("CREATE UNIQUE INDEX IMEX_CASE_NEW_LOC_ID_IDX ON IMEX_CASE_NEW_LOCAL_ID (NEW_CASE_ID)");
        //pQry.executeUpdate("CREATE UNIQUE INDEX IMEX_CASE_NEW_EXT_ID_IDX ON IMEX_CASE_NEW_EXTERN_ID (NEW_CASE_ID)");
        pQry.executeUpdate("CREATE UNIQUE INDEX IMEX_CASE_NEW_LOC_NR_IDX ON IMEX_CASE_NEW_LOCAL_ID (NR) " + parallel());
        pQry.executeUpdate("CREATE UNIQUE INDEX IMEX_CASE_NEW_EXT_NR_IDX ON IMEX_CASE_NEW_EXTERN_ID (NR) " + parallel());
        pQry.executeUpdate("CREATE UNIQUE INDEX IMEX_CASE_NEW_LOC_FA_IDX ON IMEX_CASE_NEW_LOCAL_ID (IKZ, FALLNR) " + parallel());
        pQry.executeUpdate("CREATE UNIQUE INDEX IMEX_CASE_NEW_EXT_FA_IDX ON IMEX_CASE_NEW_EXTERN_ID (IKZ, FALLNR) " + parallel());
        printTime("  New case detail ids created!");

        printTime("  Create new case department ids...");
        getProgressor().sendProgress("Erzeuge IMEX_DEPARTMENT_NEW_LOCAL_ID");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_DEPARTMENT_NEW_LOCAL_ID NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT IMEX_DEPARTMENT_TMP.NR, " + nextSqVal("T_CASE_DEPARTMENT_SQ") + " NEW_DEP_ID, IMEX_CASE_NEW_LOCAL_ID.NEW_DETAILS_ID "
                + (isOracle() ? "" : " INTO IMEX_DEPARTMENT_NEW_LOCAL_ID ")
                + " FROM IMEX_DEPARTMENT_TMP "
                + " INNER JOIN IMEX_CASE_NEW_LOCAL_ID ON (IMEX_CASE_NEW_LOCAL_ID.IKZ = IMEX_DEPARTMENT_TMP.IKZ AND IMEX_CASE_NEW_LOCAL_ID.FALLNR = IMEX_DEPARTMENT_TMP.FALLNR)"); // WHERE EXISTS (SELECT 1 FROM IMEX_CASE WHERE IMEX_CASE.NEW_VERSION = 1 AND IMEX_CASE.NEW_CASE_ID = IMEX_DEPARTMENT.NEW_CASE_ID)

        getProgressor().sendProgress("Erzeuge IMEX_DEPARTMENT_NEW_EXTERN_ID");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_DEPARTMENT_NEW_EXTERN_ID NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT IMEX_DEPARTMENT_TMP.NR, " + nextSqVal("T_CASE_DEPARTMENT_SQ") + " NEW_DEP_ID, IMEX_CASE_NEW_EXTERN_ID.NEW_DETAILS_ID "
                + (isOracle() ? "" : " INTO IMEX_DEPARTMENT_NEW_EXTERN_ID ")
                + " FROM IMEX_DEPARTMENT_TMP "
                + " INNER JOIN IMEX_CASE_NEW_EXTERN_ID ON (IMEX_CASE_NEW_EXTERN_ID.IKZ = IMEX_DEPARTMENT_TMP.IKZ AND IMEX_CASE_NEW_EXTERN_ID.FALLNR = IMEX_DEPARTMENT_TMP.FALLNR)"); // WHERE EXISTS (SELECT 1 FROM IMEX_CASE WHERE IMEX_CASE.NEW_VERSION = 1 AND IMEX_CASE.NEW_CASE_ID = IMEX_DEPARTMENT.NEW_CASE_ID)

        //pQry.executeUpdate("CREATE INDEX IMEX_DEPARTMENT_NEW_LOC_ID_IDX ON IMEX_DEPARTMENT_NEW_LOCAL_ID (NEW_CASE_ID)");
        //pQry.executeUpdate("CREATE INDEX IMEX_DEPARTMENT_NEW_EXT_ID_IDX ON IMEX_DEPARTMENT_NEW_EXTERN_ID (NEW_CASE_ID)");
        pQry.executeUpdate("CREATE UNIQUE INDEX IMEX_DEPARTMENT_NEW_LOC_NR_IDX ON IMEX_DEPARTMENT_NEW_LOCAL_ID (NR) " + parallel());
        pQry.executeUpdate("CREATE UNIQUE INDEX IMEX_DEPARTMENT_NEW_EXT_NR_IDX ON IMEX_DEPARTMENT_NEW_EXTERN_ID (NR) " + parallel());
        //pQry.executeUpdate("CREATE INDEX IMEX_DEPARTMENT_NEW_EXT_FA_IDX ON IMEX_DEPARTMENT_NEW_EXTERN_ID (IKZ, FALLNR)");
        printTime("  New case department ids created!");

        printTime("  Create new case wards ids...");
        getProgressor().sendProgress("Erzeuge IMEX_WARD_NEW_LOCAL_ID");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_WARD_NEW_LOCAL_ID NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT IMEX_WARD_TMP.NR, " + nextSqVal("T_CASE_WARD_SQ") + " NEW_WARD_ID, IMEX_DEPARTMENT_NEW_LOCAL_ID.NEW_DEP_ID "
                + (isOracle() ? "" : " INTO IMEX_WARD_NEW_LOCAL_ID ")
                + " FROM IMEX_WARD_TMP "
                + " INNER JOIN IMEX_DEPARTMENT_NEW_LOCAL_ID ON IMEX_DEPARTMENT_NEW_LOCAL_ID.NR = IMEX_WARD_TMP.DEP_NR"); // WHERE EXISTS (SELECT 1 FROM IMEX_CASE WHERE IMEX_CASE.NEW_VERSION = 1 AND IMEX_CASE.NEW_CASE_ID = IMEX_DIAGNOSE.NEW_CASE_ID)

        getProgressor().sendProgress("Erzeuge IMEX_WARD_NEW_EXTERN_ID");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_WARD_NEW_EXTERN_ID NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT IMEX_WARD_TMP.NR, " + nextSqVal("T_CASE_WARD_SQ") + " NEW_WARD_ID, IMEX_DEPARTMENT_NEW_EXTERN_ID.NEW_DEP_ID "
                + (isOracle() ? "" : " INTO IMEX_WARD_NEW_EXTERN_ID ")
                + " FROM IMEX_WARD_TMP "
                + " INNER JOIN IMEX_DEPARTMENT_NEW_EXTERN_ID ON IMEX_DEPARTMENT_NEW_EXTERN_ID.NR = IMEX_WARD_TMP.DEP_NR"); // WHERE EXISTS (SELECT 1 FROM IMEX_CASE WHERE IMEX_CASE.NEW_VERSION = 1 AND IMEX_CASE.NEW_CASE_ID = IMEX_DIAGNOSE.NEW_CASE_ID)

        //pQry.executeUpdate("CREATE INDEX IMEX_DEPARTMENT_NEW_LOC_ID_IDX ON IMEX_DEPARTMENT_NEW_LOCAL_ID (NEW_CASE_ID)");
        //pQry.executeUpdate("CREATE INDEX IMEX_DEPARTMENT_NEW_EXT_ID_IDX ON IMEX_DEPARTMENT_NEW_EXTERN_ID (NEW_CASE_ID)");
        pQry.executeUpdate("CREATE UNIQUE INDEX IMEX_WARD_NEW_LOC_NR_IDX ON IMEX_WARD_NEW_LOCAL_ID (NR) " + parallel());
        pQry.executeUpdate("CREATE UNIQUE INDEX IMEX_WARD_NEW_EXT_NR_IDX ON IMEX_WARD_NEW_EXTERN_ID (NR) " + parallel());
        //pQry.executeUpdate("CREATE INDEX IMEX_DEPARTMENT_NEW_EXT_FA_IDX ON IMEX_DEPARTMENT_NEW_EXTERN_ID (IKZ, FALLNR)");
        printTime("  New case wards ids created!");

        printTime("  Create new case diagnosis ids...");
        getProgressor().sendProgress("Erzeuge IMEX_DIAGNOSE_NEW_LOCAL_ID");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_DIAGNOSE_NEW_LOCAL_ID NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT IMEX_DIAGNOSE.NR, " + nextSqVal("T_CASE_ICD_SQ") + " NEW_ICD_ID, IMEX_DEPARTMENT_NEW_LOCAL_ID.NEW_DEP_ID "
                + (isOracle() ? "" : " INTO IMEX_DIAGNOSE_NEW_LOCAL_ID ")
                + " FROM IMEX_DIAGNOSE "
                + " INNER JOIN IMEX_DEPARTMENT_NEW_LOCAL_ID ON IMEX_DEPARTMENT_NEW_LOCAL_ID.NR = IMEX_DIAGNOSE.DEP_NR"); // WHERE EXISTS (SELECT 1 FROM IMEX_CASE WHERE IMEX_CASE.NEW_VERSION = 1 AND IMEX_CASE.NEW_CASE_ID = IMEX_DIAGNOSE.NEW_CASE_ID)

        getProgressor().sendProgress("Erzeuge IMEX_DIAGNOSE_NEW_EXTERN_ID");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_DIAGNOSE_NEW_EXTERN_ID NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT IMEX_DIAGNOSE.NR, " + nextSqVal("T_CASE_ICD_SQ") + " NEW_ICD_ID, IMEX_DEPARTMENT_NEW_EXTERN_ID.NEW_DEP_ID "
                + (isOracle() ? "" : " INTO IMEX_DIAGNOSE_NEW_EXTERN_ID ")
                + " FROM IMEX_DIAGNOSE "
                + " INNER JOIN IMEX_DEPARTMENT_NEW_EXTERN_ID ON IMEX_DEPARTMENT_NEW_EXTERN_ID.NR = IMEX_DIAGNOSE.DEP_NR"); // WHERE EXISTS (SELECT 1 FROM IMEX_CASE WHERE IMEX_CASE.NEW_VERSION = 1 AND IMEX_CASE.NEW_CASE_ID = IMEX_DIAGNOSE.NEW_CASE_ID)

        //pQry.executeUpdate("CREATE INDEX IMEX_DIAGNOSE_NEW_LOC_ID_IDX ON IMEX_DIAGNOSE_NEW_LOCAL_ID (NEW_CASE_ID)");
        //pQry.executeUpdate("CREATE INDEX IMEX_DIAGNOSE_NEW_EXT_ID_IDX ON IMEX_DIAGNOSE_NEW_EXTERN_ID (NEW_CASE_ID)");
        pQry.executeUpdate("CREATE UNIQUE INDEX IMEX_DIAGNOSE_NEW_LOC_NR_IDX ON IMEX_DIAGNOSE_NEW_LOCAL_ID (NR) " + parallel());
        pQry.executeUpdate("CREATE UNIQUE INDEX IMEX_DIAGNOSE_NEW_EXT_NR_IDX ON IMEX_DIAGNOSE_NEW_EXTERN_ID (NR) " + parallel());
        //pQry.executeUpdate("CREATE INDEX IMEX_DIAGNOSE_NEW_EXT_FA_IDX ON IMEX_DIAGNOSE_NEW_EXTERN_ID (IKZ, FALLNR)");
        printTime("  New case diagnosis ids created!");

        printTime("  Create new case procedure ids...");
        getProgressor().sendProgress("Erzeuge IMEX_PROCEDURE_NEW_LOCAL_ID");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_PROCEDURE_NEW_LOCAL_ID NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT IMEX_PROCEDURE_TMP.NR, " + nextSqVal("T_CASE_OPS_SQ") + " NEW_OPS_ID, IMEX_DEPARTMENT_NEW_LOCAL_ID.NEW_DEP_ID "
                + (isOracle() ? "" : " INTO IMEX_PROCEDURE_NEW_LOCAL_ID ")
                + " FROM IMEX_PROCEDURE_TMP "
                + " INNER JOIN IMEX_PROCEDURE_DEP ON IMEX_PROCEDURE_DEP.NR = IMEX_PROCEDURE_TMP.NR"
                + " INNER JOIN IMEX_DEPARTMENT_NEW_LOCAL_ID ON IMEX_DEPARTMENT_NEW_LOCAL_ID.NR = IMEX_PROCEDURE_DEP.DEP_NR"); // WHERE EXISTS (SELECT 1 FROM IMEX_CASE WHERE IMEX_CASE.NEW_VERSION = 1 AND IMEX_CASE.NEW_CASE_ID = IMEX_PROCEDURE.NEW_CASE_ID)

        getProgressor().sendProgress("Erzeuge IMEX_PROCEDURE_NEW_EXTERN_ID");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_PROCEDURE_NEW_EXTERN_ID NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT IMEX_PROCEDURE_TMP.NR, " + nextSqVal("T_CASE_OPS_SQ") + " NEW_OPS_ID, IMEX_DEPARTMENT_NEW_EXTERN_ID.NEW_DEP_ID "
                + (isOracle() ? "" : " INTO IMEX_PROCEDURE_NEW_EXTERN_ID ")
                + " FROM IMEX_PROCEDURE_TMP "
                + " INNER JOIN IMEX_PROCEDURE_DEP ON IMEX_PROCEDURE_DEP.NR = IMEX_PROCEDURE_TMP.NR"
                + " INNER JOIN IMEX_DEPARTMENT_NEW_EXTERN_ID ON IMEX_DEPARTMENT_NEW_EXTERN_ID.NR = IMEX_PROCEDURE_DEP.DEP_NR"); // WHERE EXISTS (SELECT 1 FROM IMEX_CASE WHERE IMEX_CASE.NEW_VERSION = 1 AND IMEX_CASE.NEW_CASE_ID = IMEX_PROCEDURE.NEW_CASE_ID)

        //pQry.executeUpdate("CREATE INDEX IMEX_PROCEDURE_NEW_LOC_ID_IDX ON IMEX_PROCEDURE_NEW_LOCAL_ID (NEW_CASE_ID)");
        //pQry.executeUpdate("CREATE INDEX IMEX_PROCEDURE_NEW_EXT_ID_IDX ON IMEX_PROCEDURE_NEW_EXTERN_ID (NEW_CASE_ID)");
        pQry.executeUpdate("CREATE UNIQUE INDEX IMEX_PROCEDURE_NEW_LOC_NR_IDX ON IMEX_PROCEDURE_NEW_LOCAL_ID (NR) " + parallel());
        pQry.executeUpdate("CREATE UNIQUE INDEX IMEX_PROCEDURE_NEW_EXT_NR_IDX ON IMEX_PROCEDURE_NEW_EXTERN_ID (NR) " + parallel());
        //pQry.executeUpdate("CREATE INDEX IMEX_PROCEDURE_NEW_EXT_FA_IDX ON IMEX_PROCEDURE_NEW_EXTERN_ID (IKZ, FALLNR)");
        printTime("  New case procedure ids created!");

        printTime("  Create new case fee ids...");
        getProgressor().sendProgress("Erzeuge IMEX_FEE_NEW_LOCAL_ID");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_FEE_NEW_LOCAL_ID NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT IMEX_FEE_TMP.NR, " + nextSqVal("T_CASE_FEE_SQ") + " NEW_FEE_ID, IMEX_CASE_NEW_LOCAL_ID.NEW_DETAILS_ID "
                + (isOracle() ? "" : " INTO IMEX_FEE_NEW_LOCAL_ID ")
                + " FROM IMEX_FEE_TMP "
                + " INNER JOIN IMEX_CASE_NEW_LOCAL_ID ON (IMEX_CASE_NEW_LOCAL_ID.IKZ = IMEX_FEE_TMP.IKZ AND IMEX_CASE_NEW_LOCAL_ID.FALLNR = IMEX_FEE_TMP.FALLNR)"); // WHERE EXISTS (SELECT 1 FROM IMEX_CASE WHERE IMEX_CASE.NEW_VERSION = 1 AND IMEX_CASE.NEW_CASE_ID = IMEX_FEE.NEW_CASE_ID)

        getProgressor().sendProgress("Erzeuge IMEX_FEE_NEW_EXTERN_ID");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_FEE_NEW_EXTERN_ID NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT IMEX_FEE_TMP.NR, " + nextSqVal("T_CASE_FEE_SQ") + " NEW_FEE_ID, IMEX_CASE_NEW_EXTERN_ID.NEW_DETAILS_ID "
                + (isOracle() ? "" : " INTO IMEX_FEE_NEW_EXTERN_ID ")
                + " FROM IMEX_FEE_TMP "
                + " INNER JOIN IMEX_CASE_NEW_EXTERN_ID ON (IMEX_CASE_NEW_EXTERN_ID.IKZ = IMEX_FEE_TMP.IKZ AND IMEX_CASE_NEW_EXTERN_ID.FALLNR = IMEX_FEE_TMP.FALLNR)"); // WHERE EXISTS (SELECT 1 FROM IMEX_CASE WHERE IMEX_CASE.NEW_VERSION = 1 AND IMEX_CASE.NEW_CASE_ID = IMEX_FEE.NEW_CASE_ID)

        //pQry.executeUpdate("CREATE INDEX IMEX_FEE_NEW_LOC_ID_IDX ON IMEX_FEE_NEW_LOCAL_ID (NEW_CASE_ID)");
        //pQry.executeUpdate("CREATE INDEX IMEX_FEE_NEW_EXT_ID_IDX ON IMEX_FEE_NEW_EXTERN_ID (NEW_CASE_ID)");
        pQry.executeUpdate("CREATE UNIQUE INDEX IMEX_FEE_NEW_LOC_NR_IDX ON IMEX_FEE_NEW_LOCAL_ID (NR) " + parallel());
        pQry.executeUpdate("CREATE UNIQUE INDEX IMEX_FEE_NEW_EXT_NR_IDX ON IMEX_FEE_NEW_EXTERN_ID (NR) " + parallel());
        //pQry.executeUpdate("CREATE INDEX IMEX_FEE_NEW_EXT_FA_IDX ON IMEX_FEE_NEW_EXTERN_ID (IKZ, FALLNR)");
        printTime("  New case fee ids created!");
    }

    //@TransactionAttribute(REQUIRES_NEW)
    //@TransactionTimeout(value = 60, unit = TimeUnit.MINUTES)
    protected void dropCases(final Query pQry) throws SQLException, IOException, ParseException {
        printTime("  Create IMEX_DROP_HOSC_IDS...");
        dropTable(pQry, "IMEX_DROP_HOSC_IDS");
        //Yup, Truncate Database is exactly the same as overwrite existing cases
        //except from the fact, that we consider ALL T_CASE_IDs and not only the 
        //ones that were found in the dataset that comes from P21 or whatever
        if (mImportConfig.isTruncateDb()) {
            LOG.log(Level.WARNING, "Database will be truncated!");
        }
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_DROP_HOSC_IDS NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT "
                + "   T_CASE.ID HOSC_ID, "
                + "   T_CASE.CS_HOSPITAL_IDENT IKZ, "
                + "   T_CASE.CS_CASE_NUMBER FALLNR "
                + (isOracle() ? "" : " INTO IMEX_DROP_HOSC_IDS ")
                + " FROM T_CASE "
                + (mImportConfig.isTruncateDb() ? "" : " INNER JOIN IMEX_CASE_TMP ON IMEX_CASE_TMP.IKZ = T_CASE.CS_HOSPITAL_IDENT AND IMEX_CASE_TMP.FALLNR = T_CASE.CS_CASE_NUMBER"));
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_HOSC_IDS_FALL ON IMEX_DROP_HOSC_IDS (IKZ, FALLNR) " + parallel());
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_HOSC_IDS_HOSC_ID ON IMEX_DROP_HOSC_IDS (HOSC_ID) " + parallel());
        printTime("  IMEX_DROP_HOSC_IDS created!");

        printTime("  Create IMEX_DROP_HOSD_IDS...");
        dropTable(pQry, "IMEX_DROP_HOSD_IDS");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_DROP_HOSD_IDS NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT "
                + "   T_CASE_DETAILS.ID HOSD_ID "
                + (isOracle() ? "" : " INTO IMEX_DROP_HOSD_IDS ")
                + " FROM T_CASE_DETAILS "
                + " INNER JOIN IMEX_DROP_HOSC_IDS ON IMEX_DROP_HOSC_IDS.HOSC_ID = T_CASE_DETAILS.T_CASE_ID");
        //pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_IDS_DEPC_ID ON IMEX_DROP_IDS (DEPC_ID)");
        //pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_IDS_HOSD_ID ON IMEX_DROP_IDS (HOSD_ID)");
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_HOSD_IDS_HOSD_ID ON IMEX_DROP_HOSD_IDS (HOSD_ID) " + parallel());
        printTime("  IMEX_DROP_HOSD_IDS created!");

        printTime("  Create IMEX_DROP_GRPRES_IDS...");
        dropTable(pQry, "IMEX_DROP_GRPRES_IDS");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_DROP_GRPRES_IDS NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT "
                + "   T_GROUPING_RESULTS.ID GRPRES_ID "
                + (isOracle() ? "" : " INTO IMEX_DROP_GRPRES_IDS ")
                + " FROM T_GROUPING_RESULTS "
                + " INNER JOIN IMEX_DROP_HOSD_IDS ON IMEX_DROP_HOSD_IDS.HOSD_ID = T_GROUPING_RESULTS.T_CASE_DETAILS_ID");
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_GRPR_IDS_GRPR_ID ON IMEX_DROP_GRPRES_IDS (GRPRES_ID) " + parallel());
        printTime("  IMEX_DROP_GRPRES_IDS created!");

        printTime("  Create IMEX_DROP_FEE_IDS...");
        dropTable(pQry, "IMEX_DROP_FEE_IDS");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_DROP_FEE_IDS NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT "
                + "   T_CASE_FEE.ID FEEC_ID "
                + (isOracle() ? "" : " INTO IMEX_DROP_FEE_IDS ")
                + " FROM T_CASE_FEE "
                + " INNER JOIN IMEX_DROP_HOSD_IDS ON IMEX_DROP_HOSD_IDS.HOSD_ID = T_CASE_FEE.T_CASE_DETAILS_ID");
        //pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_IDS_DEPC_ID ON IMEX_DROP_IDS (DEPC_ID)");
        //pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_IDS_HOSD_ID ON IMEX_DROP_IDS (HOSD_ID)");
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_FEE_IDS_ID ON IMEX_DROP_FEE_IDS (FEEC_ID) " + parallel());
        printTime("  IMEX_DROP_FEE_IDS created!");

        //2017-10-17 DNi: What about Fees that are in relation to a bills instead of hospital details?
        printTime("  Create IMEX_DROP_DEP_IDS...");
        dropTable(pQry, "IMEX_DROP_DEP_IDS");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_DROP_DEP_IDS NOLOGGING PARALLEL 5 AS " : "")
                + "  SELECT "
                + "   T_CASE_DEPARTMENT.ID T_CASE_DEPARTMENT_ID "
                + (isOracle() ? "" : " INTO IMEX_DROP_DEP_IDS ")
                + " FROM T_CASE_DEPARTMENT "
                + " INNER JOIN IMEX_DROP_HOSD_IDS ON IMEX_DROP_HOSD_IDS.HOSD_ID = T_CASE_DEPARTMENT.T_CASE_DETAILS_ID");
        //pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_IDS_DEPC_ID ON IMEX_DROP_IDS (T_CASE_DEPARTMENT_ID)");
        //pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_IDS_HOSD_ID ON IMEX_DROP_IDS (HOSD_ID)");
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_DEP_IDS_DEPC_ID ON IMEX_DROP_DEP_IDS (T_CASE_DEPARTMENT_ID) " + parallel());
        printTime("  IMEX_DROP_DEP_IDS created!");

        printTime("  Create IMEX_DROP_WARD_IDS...");
        dropTable(pQry, "IMEX_DROP_WARD_IDS");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_DROP_WARD_IDS NOLOGGING PARALLEL 5 AS " : "")
                + "  SELECT "
                + "   T_CASE_WARD.ID WARDC_ID "
                + (isOracle() ? "" : " INTO IMEX_DROP_WARD_IDS ")
                + " FROM T_CASE_WARD "
                + " INNER JOIN IMEX_DROP_DEP_IDS ON IMEX_DROP_DEP_IDS.T_CASE_DEPARTMENT_ID = T_CASE_WARD.T_CASE_DEPARTMENT_ID");
        //pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_IDS_DEPC_ID ON IMEX_DROP_IDS (DEPC_ID)");
        //pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_IDS_HOSD_ID ON IMEX_DROP_IDS (HOSD_ID)");
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_WARD_IDS_WARD_ID ON IMEX_DROP_WARD_IDS (WARDC_ID) " + parallel());
        printTime("  IMEX_DROP_WARD_IDS created!");

        printTime("  Create IMEX_DROP_ICD_IDS...");
        dropTable(pQry, "IMEX_DROP_ICD_IDS");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_DROP_ICD_IDS NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT "
                + "   T_CASE_ICD.ID T_CASE_ICD_ID "
                + (isOracle() ? "" : " INTO IMEX_DROP_ICD_IDS ")
                + " FROM T_CASE_ICD "
                + " INNER JOIN IMEX_DROP_DEP_IDS ON IMEX_DROP_DEP_IDS.T_CASE_DEPARTMENT_ID = T_CASE_ICD.T_CASE_DEPARTMENT_ID");
        //pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_IDS_DEPC_ID ON IMEX_DROP_IDS (DEPC_ID)");
        //pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_IDS_HOSD_ID ON IMEX_DROP_IDS (HOSD_ID)");
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_ICD_IDS_ID ON IMEX_DROP_ICD_IDS (T_CASE_ICD_ID) " + parallel());
        printTime("  IMEX_DROP_ICD_IDS created!");

        printTime("  Create IMEX_DROP_OPS_IDS...");
        dropTable(pQry, "IMEX_DROP_OPS_IDS");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_DROP_OPS_IDS NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT "
                + "   T_CASE_OPS.ID OPSC_ID "
                + (isOracle() ? "" : " INTO IMEX_DROP_OPS_IDS ")
                + " FROM T_CASE_OPS "
                + " INNER JOIN IMEX_DROP_DEP_IDS ON IMEX_DROP_DEP_IDS.T_CASE_DEPARTMENT_ID = T_CASE_OPS.T_CASE_DEPARTMENT_ID");
        //pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_IDS_DEPC_ID ON IMEX_DROP_IDS (DEPC_ID)");
        //pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_IDS_HOSD_ID ON IMEX_DROP_IDS (HOSD_ID)");
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_OPS_IDS_ID ON IMEX_DROP_OPS_IDS (OPSC_ID) " + parallel());
        printTime("  IMEX_DROP_OPS_IDS created!");

        printTime("  Create IMEX_DROP_PROCESS_CASE_IDS...");
        dropTable(pQry, "IMEX_DROP_PROCESS_CASE_IDS");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_DROP_PROCESS_CASE_IDS NOLOGGING PARALLEL 5 AS " : "")
                + "  SELECT "
                + "   T_WM_PROCESS_T_CASE.ID PROCESS_CASE_ID, "
                + "   T_WM_PROCESS_T_CASE.T_WM_PROCESS_ID, "
                + "   T_WM_PROCESS_T_CASE.MAIN_CASE_FL, "
                + "   IMEX_DROP_HOSC_IDS.IKZ, "
                + "   IMEX_DROP_HOSC_IDS.FALLNR "
                + (isOracle() ? "" : " INTO IMEX_DROP_PROCESS_CASE_IDS ")
                + " FROM T_WM_PROCESS_T_CASE "
                + " INNER JOIN IMEX_DROP_HOSC_IDS ON IMEX_DROP_HOSC_IDS.HOSC_ID = T_WM_PROCESS_T_CASE.T_CASE_ID");
        //pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_IDS_DEPC_ID ON IMEX_DROP_IDS (DEPC_ID)");
        //pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_IDS_HOSD_ID ON IMEX_DROP_IDS (HOSD_ID)");
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_PROCESS_FALL ON IMEX_DROP_PROCESS_CASE_IDS (IKZ, FALLNR) " + parallel());
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_PROCESS_CASE_ID ON IMEX_DROP_PROCESS_CASE_IDS (PROCESS_CASE_ID) " + parallel());
        printTime("  IMEX_DROP_PROCESS_CASE_IDS created!");

        printTime("  Create IMEX_DROP_KAIN_INKA_IDS...");
        dropTable(pQry, "IMEX_DROP_KAIN_INKA_IDS");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_DROP_KAIN_INKA_IDS NOLOGGING PARALLEL 5 AS " : "")
                + "  SELECT "
                + "   T_P301_KAIN_INKA.ID KAIN_INKA_ID "
                + (isOracle() ? "" : " INTO IMEX_DROP_KAIN_INKA_IDS ")
                + " FROM T_P301_KAIN_INKA "
                + " INNER JOIN IMEX_DROP_HOSC_IDS ON IMEX_DROP_HOSC_IDS.HOSC_ID = T_P301_KAIN_INKA.T_CASE_ID");
        //pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_IDS_DEPC_ID ON IMEX_DROP_IDS (DEPC_ID)");
        //pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_IDS_HOSD_ID ON IMEX_DROP_IDS (HOSD_ID)");
        //pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_KAIN_INKA_FALL ON IMEX_DROP_KAIN_INKA_IDS (IKZ, FALLNR) " + parallel());
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_DROP_KAIN_INKA_ID ON IMEX_DROP_KAIN_INKA_IDS (KAIN_INKA_ID) " + parallel());
        printTime("  IMEX_DROP_KAIN_INKA_IDS created!");

        printTime("  To overwrite hospital cases I will delete " + getCaseDeleteCount(pQry) + " hospital cases from the current database!");

        printTime("  delete from T_CHECK_RESULT...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_CHECK_RESULT 5)*/ FROM T_CHECK_RESULT WHERE T_CHECK_RESULT.T_GROUPING_RESULTS_ID IN (SELECT GRPRES_ID FROM IMEX_DROP_GRPRES_IDS)");
        printTime("  T_CHECK_RESULT deleted!");

        printTime("  delete from T_ROLE_2_RESULT...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_ROLE_2_RESULT 5)*/ FROM T_ROLE_2_RESULT WHERE T_ROLE_2_RESULT.T_GROUPING_RESULTS_ID IN (SELECT GRPRES_ID FROM IMEX_DROP_GRPRES_IDS)");
        printTime("  T_ROLE_2_RESULT deleted!");

        printTime("  delete from T_CASE_ICD_GROUPED...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE_ICD_GROUPED 5)*/ FROM T_CASE_ICD_GROUPED WHERE T_CASE_ICD_GROUPED.T_CASE_ICD_ID IN (SELECT T_CASE_ICD_ID FROM IMEX_DROP_ICD_IDS)");
        printTime("  T_CASE_ICD_GROUPED deleted!");

        printTime("  delete from T_CASE_SUPPL_FEE...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE_SUPPL_FEE 5)*/ FROM T_CASE_SUPPL_FEE WHERE T_CASE_SUPPL_FEE.T_CASE_OPS_GROUPED_ID IN (SELECT OPSC_ID FROM IMEX_DROP_OPS_IDS)");
        printTime("  T_CASE_SUPPL_FEE deleted!");

        printTime("  delete from T_CASE_OPS_GROUPED...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE_OPS_GROUPED 5)*/ FROM T_CASE_OPS_GROUPED WHERE T_CASE_OPS_GROUPED.T_CASE_OPS_ID IN (SELECT OPSC_ID FROM IMEX_DROP_OPS_IDS)");
        printTime("  T_CASE_OPS_GROUPED deleted!");

        printTime("  delete from T_GROUPING_RESULTS...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_GROUPING_RESULTS 5)*/ FROM T_GROUPING_RESULTS WHERE T_GROUPING_RESULTS.ID IN (SELECT GRPRES_ID FROM IMEX_DROP_GRPRES_IDS)");
        printTime("  T_ROLE_2_RESULT deleted!");

        printTime("  delete from T_LAB...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_LAB 5)*/ FROM T_LAB WHERE T_LAB.T_CASE_ID IN (SELECT HOSC_ID FROM IMEX_DROP_HOSC_IDS)");
        printTime("  T_LAB deleted!");

//        printTime("  delete from T_LAB...");
//        pQry.executeUpdate("DELETE /*+ PARALLEL(T_LAB 5)*/ FROM T_LAB WHERE T_LAB.T_CASE_DETAILS_EXTERN_ID IN (SELECT HOSD_ID FROM IMEX_DROP_HOSD_IDS) OR T_LAB.T_CASE_DETAILS_LOCAL_ID IN (SELECT HOSD_ID FROM IMEX_DROP_HOSD_IDS)");
//        printTime("  T_LAB deleted!");
        printTime("  delete from T_CASE_ICD...");
        pQry.executeUpdate("UPDATE /*+ PARALLEL(T_CASE_ICD 5)*/ T_CASE_ICD SET T_CASE_ICD_ID = NULL WHERE T_CASE_ICD_ID IS NOT NULL AND T_CASE_ICD.ID IN (SELECT T_CASE_ICD_ID FROM IMEX_DROP_ICD_IDS)");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE_ICD 5)*/ FROM T_CASE_ICD WHERE T_CASE_ICD.ID IN (SELECT T_CASE_ICD_ID FROM IMEX_DROP_ICD_IDS)");
        printTime("  T_CASE_ICD deleted!");

        printTime("  delete from T_CASE_OPS...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE_OPS 5)*/ FROM T_CASE_OPS WHERE T_CASE_OPS.ID IN (SELECT OPSC_ID FROM IMEX_DROP_OPS_IDS)");
        printTime("  T_CASE_OPS deleted!");

        printTime("  delete from T_CASE_WARD...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE_WARD 5)*/ FROM T_CASE_WARD WHERE T_CASE_WARD.ID IN (SELECT WARDC_ID FROM IMEX_DROP_WARD_IDS)");
        printTime("  T_CASE_DEPARTMENT deleted!");

        printTime("  delete from T_CASE_DEPARTMENT...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE_DEPARTMENT 5)*/ FROM T_CASE_DEPARTMENT WHERE T_CASE_DEPARTMENT.ID IN (SELECT T_CASE_DEPARTMENT_ID FROM IMEX_DROP_DEP_IDS)");
        printTime("  T_CASE_DEPARTMENT deleted!");

        printTime("  delete from T_CASE_FEE...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE_FEE 5)*/ FROM T_CASE_FEE WHERE T_CASE_FEE.ID IN (SELECT FEEC_ID FROM IMEX_DROP_FEE_IDS)");
        printTime("  T_CASE_FEE deleted!");

        printTime("  prepare T_WM_PROCESS_T_CASE to delete; set T_CASE_DETAILS_ID to NULL...");               
        pQry.executeUpdate("UPDATE T_WM_PROCESS_T_CASE SET T_CASE_DETAILS_ID = NULL WHERE T_WM_PROCESS_T_CASE.ID IN (SELECT PROCESS_CASE_ID FROM IMEX_DROP_PROCESS_CASE_IDS)");
        printTime(" T_WM_PROCESS_T_CASE updated; set T_CASE_DETAILS_ID to NULL...");   
 
        printTime("  prepare T_WM_REQUEST to delete; set T_CASE_DETAILS_ID to NULL...");               
        pQry.executeUpdate("UPDATE T_WM_REQUEST SET T_CASE_DETAILS_ID = NULL WHERE T_CASE_DETAILS_ID IN (SELECT HOSD_ID FROM IMEX_DROP_HOSD_IDS)");
        printTime(" T_WM_REQUEST updated; set T_CASE_DETAILS_ID to NULL...");   
 
        printTime("  delete from T_WM_PROCESS_T_CASE...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_WM_PROCESS_T_CASE 5)*/ FROM T_WM_PROCESS_T_CASE WHERE T_WM_PROCESS_T_CASE.ID IN (SELECT PROCESS_CASE_ID FROM IMEX_DROP_PROCESS_CASE_IDS)");
        printTime("  T_WM_PROCESS_T_CASE deleted!");

        printTime("  delete from T_WM_EVENT...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_WM_EVENT 5)*/ FROM T_WM_EVENT WHERE T_WM_EVENT.T_CASE_ID IN (SELECT HOSC_ID FROM IMEX_DROP_HOSC_IDS)");
        printTime("  T_WM_EVENT deleted!");

        printTime("  delete from T_WM_EVENT...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_WM_EVENT 5)*/ FROM T_WM_EVENT WHERE T_WM_EVENT.T_P301_KAIN_INKA_ID IN (SELECT KAIN_INKA_ID FROM IMEX_DROP_KAIN_INKA_IDS)");
        printTime("  T_WM_EVENT deleted!");

        //2018-02-26 DNi: Column T_WM_DOCUMENT.T_CASE_ID was dropped and does not exist anymore!
        //printTime("  delete from T_WM_DOCUMENT...");
        //pQry.executeUpdate("DELETE /*+ PARALLEL(T_WM_DOCUMENT 5)*/ FROM T_WM_DOCUMENT WHERE T_WM_DOCUMENT.T_CASE_ID IN (SELECT HOSC_ID FROM IMEX_DROP_HOSC_IDS)");
        //printTime("  T_WM_DOCUMENT deleted!");
        printTime("  delete from T_P301_KAIN...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_P301_KAIN 5)*/ FROM T_P301_KAIN WHERE T_P301_KAIN.ID IN (SELECT KAIN_INKA_ID FROM IMEX_DROP_KAIN_INKA_IDS)");
        printTime("  T_P301_KAIN deleted!");

        printTime("  delete from T_P301_INKA...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_P301_INKA 5)*/ FROM T_P301_INKA WHERE T_P301_INKA.ID IN (SELECT KAIN_INKA_ID FROM IMEX_DROP_KAIN_INKA_IDS)");
        printTime("  T_P301_KAIN deleted!");

        printTime("  delete from T_P301_KAIN_INKA...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_P301_KAIN_INKA 5)*/ FROM T_P301_KAIN_INKA WHERE T_P301_KAIN_INKA.ID IN (SELECT KAIN_INKA_ID FROM IMEX_DROP_KAIN_INKA_IDS)");
        printTime("  T_P301_KAIN deleted!");

        printTime("  delete from T_MIBI...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_MIBI 5)*/ FROM T_MIBI WHERE T_MIBI.T_CASE_ID IN (SELECT HOSC_ID FROM IMEX_DROP_HOSC_IDS)");
        printTime("  T_MIBI deleted!");

        printTime("  delete from T_DRUG...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_DRUG 5)*/ FROM T_DRUG WHERE T_DRUG.T_CASE_ID IN (SELECT HOSC_ID FROM IMEX_DROP_HOSC_IDS)");
        printTime("  T_DRUG deleted!");

        printTime("  delete from T_WM_RISK...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_WM_RISK 5)*/ FROM T_WM_RISK WHERE T_WM_RISK.T_CASE_DETAILS_ID IN (SELECT HOSD_ID FROM IMEX_DROP_HOSD_IDS)");
        printTime("  T_WM_RISK deleted!");

        printTime("  delete from T_CASE_DETAILS...");
        pQry.executeUpdate("UPDATE /*+ PARALLEL(T_CASE_DETAILS 5)*/ T_CASE_DETAILS SET EXTERN_ID = NULL, PARENT_ID = NULL WHERE (EXTERN_ID IS NOT NULL OR PARENT_ID IS NOT NULL) AND T_CASE_DETAILS.ID IN (SELECT HOSD_ID FROM IMEX_DROP_HOSD_IDS)");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE_DETAILS 5)*/ FROM T_CASE_DETAILS WHERE T_CASE_DETAILS.ID IN (SELECT HOSD_ID FROM IMEX_DROP_HOSD_IDS)");
        printTime("  T_CASE_DETAILS deleted!");

        printTime("  delete from T_SAP_FI_OPEN_ITEMS...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE 5)*/ FROM T_SAP_FI_OPEN_ITEMS WHERE T_SAP_FI_OPEN_ITEMS.T_CASE_ID IN (SELECT HOSC_ID FROM IMEX_DROP_HOSC_IDS)");
        printTime("  T_SAP_FI_OPEN_ITEMS deleted!");

        printTime("  delete from T_SAP_FI_BILLPOSITION...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE 5)*/ FROM T_SAP_FI_BILLPOSITION WHERE T_SAP_FI_BILLPOSITION.T_SAP_FI_BILL_ID IN (SELECT ID FROM T_SAP_FI_BILL "
                + "WHERE T_CASE_ID IN  (SELECT HOSC_ID FROM IMEX_DROP_HOSC_IDS))");
        printTime("  T_SAP_FI_BILLPOSITION deleted!");

        printTime("  delete from T_SAP_FI_BILL...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE 5)*/ FROM T_SAP_FI_BILL WHERE T_SAP_FI_BILL.T_CASE_ID IN (SELECT HOSC_ID FROM IMEX_DROP_HOSC_IDS)");
        printTime("  T_SAP_FI_BILL deleted!");

        printTime("  delete from T_CASE_2_RULE_SELECTION...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE 5)*/ FROM T_CASE_2_RULE_SELECTION WHERE T_CASE_2_RULE_SELECTION.T_CASE_ID IN (SELECT HOSC_ID FROM IMEX_DROP_HOSC_IDS)");
        printTime("  T_CASE_2_RULE_SELECTION deleted!");

        printTime("  delete from T_CASE...");
        pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE 5)*/ FROM T_CASE WHERE T_CASE.ID IN (SELECT HOSC_ID FROM IMEX_DROP_HOSC_IDS)");
        printTime("  T_CASE deleted!");

        if (mImportConfig.isTruncateDb()) {
// workflow tables must be deleted too
            printTime("  delete from T_WM_EVENT...");
            pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE 5)*/ FROM T_WM_EVENT");
            printTime("  T_WM_EVENT deleted!");

            printTime("  delete from T_WM_REQUEST_MDK...");
            pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE 5)*/ FROM T_WM_REQUEST_MDK");
            printTime("  T_WM_REQUEST_MDK deleted!");

            printTime("  delete from T_WM_REQUEST_OTHER...");
            pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE 5)*/ FROM T_WM_REQUEST_OTHER");
            printTime("  T_WM_REQUEST_OTHER deleted!");

            printTime("  delete from T_WM_REQUEST_AUDIT...");
            pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE 5)*/ FROM T_WM_REQUEST_AUDIT");
            printTime("  T_WM_REQUEST_AUDIT deleted!");

            printTime("  delete from T_WM_REQUEST_BEGE...");
            pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE 5)*/ FROM T_WM_REQUEST_BEGE");
            printTime("  T_WM_REQUEST_BEGE deleted!");

            printTime("  delete from T_WM_REQUEST_INSURANCE...");
            pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE 5)*/ FROM T_WM_REQUEST_INSURANCE");
            printTime("  T_WM_REQUEST_INSURANCE deleted!");

            printTime("  delete from T_WM_REQUEST_REVIEW...");
            pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE 5)*/ FROM T_WM_REQUEST_REVIEW");
            printTime("  T_WM_REQUEST_REVIEW deleted!");

            printTime("  delete from T_WM_REQUEST...");
            pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE 5)*/ FROM T_WM_REQUEST");
            printTime("  T_WM_REQUEST deleted!");

            printTime("  delete from T_WM_FINALIS_RISK_DETAILS...");
            pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE 5)*/ FROM T_WM_FINALIS_RISK_DETAILS");
            printTime("  T_WM_FINALIS_RISK_DETAILS deleted!");

            printTime("  delete from T_WM_FINALIS_RISK...");
            pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE 5)*/ FROM T_WM_FINALIS_RISK");
            printTime("  T_WM_FINALIS_RISK deleted!");

            printTime("  delete from T_WM_PROCESS_HOSPITAL_FINALIS...");
            pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE 5)*/ FROM T_WM_PROCESS_HOSPITAL_FINALIS");
            printTime("  T_WM_PROCESS_HOSPITAL_FINALIS deleted!");

            printTime("  delete from T_WM_PROCESS_HOSPITAL...");
            pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE 5)*/ FROM T_WM_PROCESS_HOSPITAL");
            printTime("  T_WM_PROCESS_HOSPITAL deleted!");

            printTime("  delete from T_WM_PROCESS...");
            pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE 5)*/ FROM T_WM_PROCESS");
            printTime("  T_WM_PROCESS deleted!");

            printTime("  delete from T_ACG_DATA...");
            pQry.executeUpdate("DELETE /*+ PARALLEL(T_ACG_DATA 5)*/ FROM T_ACG_DATA");
            printTime("  T_ACG_DATA deleted!");

            printTime("  delete from T_ACG_DATA_INFO...");
            pQry.executeUpdate("DELETE /*+ PARALLEL(T_ACG_DATA_INFO 5)*/ FROM T_ACG_DATA_INFO");
            printTime("  T_ACG_DATA_INFO deleted!");

            printTime("  delete from T_Patient...");
            pQry.executeUpdate("DELETE /*+ PARALLEL(T_CASE 5)*/ FROM T_Patient");
            printTime("  T_Patient deleted!");
        }
        
        if (mImportConfig.isTruncateDb()) {
            LOG.log(Level.WARNING, "Database was successfully truncated!");
        }
    }

    protected void dropImexTables(final Query pQry) throws SQLException {
        //ORA: SELECT table_name FROM user_tables where table_name like 'IMEX_%' and table_name not like '%_TMP';
        String query;
        if (isOracle()) {
            query = "SELECT table_name name FROM user_tables where 1=1 ";
        } else {
            query = "SELECT TABLE_NAME name FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE='BASE TABLE' ";
        }
        query += " and table_name like 'IMEX_%' and table_name not like '%_TMP'";

        ResultSet rs = pQry.executeQuery(query);
        List<String> tables = new ArrayList<>();
        while (rs.next()) {
            String tblName = rs.getString("name");
            tblName = (tblName == null) ? "" : tblName.trim().toUpperCase();
            if (!tblName.isEmpty()) {
                tables.add(tblName);
            }
        }
        for (String tableName : tables) {
            pQry.execute("DROP TABLE " + tableName);
        }
    }

    protected void dropImexViews(final Query pQry) throws SQLException {
        //ORA: SELECT table_name FROM user_tables where table_name like 'IMEX_%' and table_name not like '%_TMP';
        String query;
        if (isOracle()) {
            query = "SELECT view_name name FROM user_views where view_name like 'IMEX_%'";
        } else {
            query = "SELECT TABLE_NAME name FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_NAME LIKE 'IMEX_%'";
        }

        ResultSet rs = pQry.executeQuery(query);
        List<String> views = new ArrayList<>();
        while (rs.next()) {
            String tblName = rs.getString("name");
            tblName = (tblName == null) ? "" : tblName.trim().toUpperCase();
            if (!tblName.isEmpty()) {
                views.add(tblName);
            }
        }
        for (String tableName : views) {
            pQry.execute("DROP VIEW " + tableName);
        }
    }

    protected void printTime(final String pMessage) {
        final long mEndTime = System.currentTimeMillis();
        final long mDiff = mEndTime - mMeanTime;
        final Date endDate = new Date(mEndTime);
        final Date diffDate = new Date(mDiff);
        final String dateFormat = "yyyy-MM-dd";
        final String timeFormat = "HH:mm:ss";
        final String datetimeFormat = dateFormat + " " + timeFormat;
        final SimpleDateFormat dfStart = new SimpleDateFormat(datetimeFormat);
        dfStart.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        final SimpleDateFormat dfEnd = new SimpleDateFormat(timeFormat);
        dfEnd.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        final SimpleDateFormat dfDiff = new SimpleDateFormat(timeFormat);
        dfDiff.setTimeZone(TimeZone.getTimeZone("GMT"));
        final String endDateStr = dfEnd.format(endDate);
        final String diffDateStr = dfDiff.format(diffDate);
        LOG.log(Level.INFO, "Zwischenzeit {0} -> Dauer {1}{2}", new Object[]{endDateStr, diffDateStr, (pMessage != null && !pMessage.trim().isEmpty()) ? (": " + pMessage) : ""});
    }

    protected void createIndexes(final Query pQry, final String[][] pTmpIndexes) throws SQLException {
        for (String[] index : pTmpIndexes) {
            pQry.execute(DbBuilder.getIndexCreateSilent(index[0], index[1], index[2], isOracle()));
            //pQry.execute(DbBuilder.getIndexCreate(index[0], index[1], index[2]));
        }
    }

    protected void dropIndexes(final Query pQry, final String[][] pTmpIndexes) throws SQLException {
        for (String[] index : pTmpIndexes) {
            pQry.execute(DbBuilder.getIndexDropSilent(index[0], index[1], isOracle()));
        }
    }

    protected void dropTable(final Query pQry, final String pTableName) throws IOException, SQLException, ParseException {
        pQry.execute(DbBuilder.getTableDropSilent(pTableName, isOracle()));
    }

    protected String nextSqVal(final String pSequenceName) {
        if (isOracle()) {
            return pSequenceName + ".nextval";
        } else {
            return "NEXT VALUE FOR " + pSequenceName;
        }
    }

    protected long getNewWorkflowNumber(final Query pCommonDbQry) throws SQLException {
        final String sequenceName = "CDB_SEQUENCE_SQ";
        final String query;
        if (mImportConfig.isCaseDbOracle()) {
            query = "SELECT " + sequenceName + ".nextval PROCESS_NUMBER FROM DUAL";
        } else {
            query = "SELECT NEXT VALUE FOR " + sequenceName + " PROCESS_NUMBER";
        }
        long processNumber = 0;
        try (ResultSet rs = pCommonDbQry.executeQuery(query)) {
            while (rs.next()) {
                processNumber = rs.getLong("PROCESS_NUMBER");
            }
        }
        return processNumber;
    }

    protected String isNull(final String pField, final String pValue) {
        if (isOracle()) {
            return "NVL(" + pField + ", '" + pValue + "')";
        } else {
            return "ISNULL(" + pField + ", '" + pValue + "')";
        }
    }

    protected String isNull2(final String pField, final String pValue) {
        if (isOracle()) {
            return "NVL(" + pField + ", " + pValue + ")";
        } else {
            return "ISNULL(" + pField + ", " + pValue + ")";
        }
    }

    protected String minus() {
        return (isOracle() ? "MINUS" : "EXCEPT");
    }

    protected String currentDate() {
        return (isOracle() ? "SYSDATE" : "GETDATE()");
    }

    protected String currentGermanDate() {
        if (isOracle()) {
            return "TO_CHAR(SYSDATE, 'DD.MM.YYYY HH24:MI')"; //HH24:MI:SS
        } else {
            return "FORMAT(GETDATE(), 'dd.MM.yyyy HH:mm')";
        }
    }

    protected String parallel() {
        return (isOracle() ? "NOLOGGING PARALLEL 5" : "");
    }

    protected boolean isOracle() {
        return mImportConfig.isCaseDbOracle();
    }

    protected boolean isSqlSrv() {
        return mImportConfig.isCaseDbSqlSrv();
    }

//    private void createImexMainIcd(final Query pQry) throws IOException, SQLException, ParseException {
//        pQry.executeUpdate("CREATE INDEX IDX_IMEX_DEP_TMP_ERBRINGUNGSAR ON IMEX_DEPARTMENT_TMP (ERBRINGUNGSART) " + parallel());
//        pQry.executeUpdate("CREATE INDEX IDX_IMEX_DIAGNOSE_TMP_HDX ON IMEX_DIAGNOSE_TMP (HDX) " + parallel());
//        pQry.executeUpdate("CREATE TABLE IMEX_MAIN_ICD NOLOGGING PARALLEL 5 AS "
//                + "  SELECT IMEX_DIAGNOSE_TMP.IKZ, IMEX_DIAGNOSE_TMP.FALLNR, "
//                + "         IMEX_DIAGNOSE_TMP.CODE, IMEX_DIAGNOSE_TMP.NR, "
//                + "         IMEX_CASE_NEW_LOCAL_ID.NEW_CASE_ID, "
//                + "         IMEX_CASE_NEW_LOCAL_ID.NEW_DETAILS_ID NEW_LOCAL_DETAILS_ID, "
//                + "         IMEX_CASE_NEW_EXTERN_ID.NEW_DETAILS_ID NEW_EXTERN_DETAILS_ID, "
//                + "         IMEX_DIAGNOSE_NEW_LOCAL_ID.NEW_ICD_ID NEW_ICD_LOCAL_ID, "
//                + "         IMEX_DIAGNOSE_NEW_EXTERN_ID.NEW_ICD_ID NEW_ICD_EXTERN_ID "
//                + "  FROM IMEX_DIAGNOSE_TMP " 
//                + "  INNER JOIN IMEX_DEPARTMENT_TMP ON IMEX_DEPARTMENT_TMP.NR = IMEX_DIAGNOSE_TMP.DEP_NR "
//                + "  INNER JOIN IMEX_CASE_NEW_LOCAL_ID ON (IMEX_DIAGNOSE_TMP.IKZ = IMEX_CASE_NEW_LOCAL_ID.IKZ AND IMEX_DIAGNOSE_TMP.FALLNR = IMEX_CASE_NEW_LOCAL_ID.FALLNR) "
//                + "  INNER JOIN IMEX_CASE_NEW_EXTERN_ID ON (IMEX_DIAGNOSE_TMP.IKZ = IMEX_CASE_NEW_EXTERN_ID.IKZ AND IMEX_DIAGNOSE_TMP.FALLNR = IMEX_CASE_NEW_EXTERN_ID.FALLNR) "
//                + "  INNER JOIN IMEX_DIAGNOSE_NEW_LOCAL_ID ON IMEX_DIAGNOSE_NEW_LOCAL_ID.NR = IMEX_DIAGNOSE_TMP.NR "
//                + "  INNER JOIN IMEX_DIAGNOSE_NEW_EXTERN_ID ON IMEX_DIAGNOSE_NEW_EXTERN_ID.NR = IMEX_DIAGNOSE_TMP.NR "
//                + "  WHERE IMEX_DEPARTMENT_TMP.ERBRINGUNGSART = 'HA' "
//                + "    AND IMEX_DIAGNOSE_TMP.HDX = 1 ");
//        pQry.executeUpdate("CREATE INDEX IDX_IMEX_MAIN_ICD_FALL ON IMEX_MAIN_ICD (IKZ, FALLNR) " + parallel());
//        pQry.executeUpdate("CREATE INDEX IDX_IMEX_MAIN_ICD_NR ON IMEX_MAIN_ICD (NR) " + parallel());
//        pQry.executeUpdate("CREATE INDEX IDX_IMEX_MAIN_NEW_CASE_ID ON IMEX_MAIN_ICD (NEW_CASE_ID) " + parallel());
//        pQry.executeUpdate("CREATE INDEX IDX_IMEX_MAIN_NEW_LOC_DET_ID ON IMEX_MAIN_ICD (NEW_LOCAL_DETAILS_ID) " + parallel());
//        pQry.executeUpdate("CREATE INDEX IDX_IMEX_MAIN_NEW_EXT_DET_ID ON IMEX_MAIN_ICD (NEW_EXTERN_DETAILS_ID) " + parallel());
//        pQry.executeUpdate("CREATE INDEX IDX_IMEX_MAIN_NEW_ICD_LOC_ID ON IMEX_MAIN_ICD (NEW_ICD_LOCAL_ID) " + parallel());
//        pQry.executeUpdate("CREATE INDEX IDX_IMEX_MAIN_NEW_ICD_EXT_ID ON IMEX_MAIN_ICD (NEW_ICD_EXTERN_ID) " + parallel());
//    }
    protected void createImexAufnehmendeFab(final Query pQry) throws SQLException, IOException, ParseException {
        printTime("  Create IMEX_AUFNEHMENDE_FAB...");

        getProgressor().sendProgress("Erzeuge IMEX_AUFNEHMENDE_FAB");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_AUFNEHMENDE_FAB NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT "
                + "   TMP.*, "
                + "   (SELECT " + (isOracle() ? "" : "TOP 1") + " NR FROM IMEX_DEPARTMENT "
                + "    WHERE IMEX_DEPARTMENT.IKZ = TMP.IKZ AND IMEX_DEPARTMENT.FALLNR = TMP.FALLNR AND IMEX_DEPARTMENT.VERLEGUNGSDATUM = TMP.VERLEGUNGSDATUM " + (isOracle() ? "AND ROWNUM <= 1" : "ORDER BY VERLEGUNGSDATUM, ENTLASSUNGSDATUM, CODE") + ") NR, "
                + "   1 AUFNEHMENDE_FAB "
                + (isOracle() ? "" : " INTO IMEX_AUFNEHMENDE_FAB ")
                + " FROM ( "
                + "   SELECT IKZ, FALLNR, VERLEGUNGSDATUM FROM IMEX_DEPARTMENT "
                + "   WHERE VERLEGUNGSDATUM = VERLDATUM_AUFNEHMENDE_FAB "
                + "   GROUP BY IKZ, FALLNR, VERLEGUNGSDATUM "
                + " ) TMP");
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_AUFNEHMENDE_FAB_FALL ON IMEX_AUFNEHMENDE_FAB (IKZ, FALLNR) " + parallel());
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_AUFNEHMENDE_FAB_NR ON IMEX_AUFNEHMENDE_FAB (NR) " + parallel());
        //pQry.executeUpdate(DbBuilder.getImexPkDefinition("IMEX_AUFNEHMENDE_FAB"));

        printTime("  IMEX_AUFNEHMENDE_FAB created!");
    }

    protected void createImexEntlassendeFab(final Query pQry) throws SQLException, IOException, ParseException {
        printTime("  Create IMEX_ENTLASSENDE_FAB...");

        getProgressor().sendProgress("Erzeuge IMEX_ENTLASSENDE_FAB");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_ENTLASSENDE_FAB NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT "
                + "   TMP.*, "
                + "   (SELECT " + (isOracle() ? "" : "TOP 1") + " NR FROM IMEX_DEPARTMENT "
                + "    WHERE IMEX_DEPARTMENT.IKZ = TMP.IKZ AND IMEX_DEPARTMENT.FALLNR = TMP.FALLNR AND IMEX_DEPARTMENT.VERLEGUNGSDATUM = TMP.VERLEGUNGSDATUM " + (isOracle() ? "AND ROWNUM <= 1" : "ORDER BY VERLEGUNGSDATUM DESC, ENTLASSUNGSDATUM DESC, CODE") + ") NR, "
                + "   1 ENTLASSENDE_FAB "
                + (isOracle() ? "" : " INTO IMEX_ENTLASSENDE_FAB ")
                + " FROM ( "
                + "   SELECT IKZ, FALLNR, VERLEGUNGSDATUM FROM IMEX_DEPARTMENT "
                + "   WHERE VERLEGUNGSDATUM = VERLDATUM_ENTLASSENDE_FAB "
                + "   GROUP BY IKZ, FALLNR, VERLEGUNGSDATUM "
                + " ) TMP");
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_ENTLASSENDE_FAB_FALL ON IMEX_ENTLASSENDE_FAB (IKZ, FALLNR) " + parallel());
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_ENTLASSENDE_FAB_NR ON IMEX_ENTLASSENDE_FAB (NR) " + parallel());
        //pQry.executeUpdate(DbBuilder.getImexPkDefinition("IMEX_ENTLASSENDE_FAB"));

        printTime("  IMEX_ENTLASSENDE_FAB created!");
    }

    protected void createImexBehandelndeFab(final Query pQry) throws SQLException, IOException, ParseException {
        printTime("  Create IMEX_BEHANDELNDE_FAB...");

        //dropTable(pQry, "IMEX_BEHANDELNDE_FAB_TMP");
        //dropTable(pQry, "IMEX_ERBRINGUNGSART_TMP2");
        dropTable(pQry, "IMEX_BEHANDELNDE_FAB");

        //pQry.executeUpdate("CREATE INDEX IDX_IMEX_DEP_TMP_ENTLDATUM ON IMEX_DEPARTMENT_TMP (ENTLASSUNGSDATUM) " + parallel());
        //pQry.executeUpdate("CREATE INDEX IDX_IMEX_DEP_TMP_VERLDATUM ON IMEX_DEPARTMENT_TMP (VERLEGUNGSDATUM) " + parallel());
//    pQry.executeUpdate((isOracle()?"CREATE TABLE IMEX_BEHANDELNDE_FAB_TMP NOLOGGING PARALLEL 5 AS ":"")
//            + " SELECT TMP.* "
//            + (isOracle()?"":" INTO IMEX_BEHANDELNDE_FAB_TMP ")
//            + " FROM ( "
//            + "   SELECT IMEX_DEPARTMENT_TMP.NR, IMEX_DEPARTMENT_TMP.IKZ, IMEX_DEPARTMENT_TMP.FALLNR, IMEX_DEPARTMENT_TMP.ERBRINGUNGSART, IMEX_DEPARTMENT_TMP.VERLEGUNGSDATUM, IMEX_DEPARTMENT_TMP.ENTLASSUNGSDATUM, "
//            + "     (CASE "
//            + "      WHEN ERBRINGUNGSART = 'HA' THEN 1 "
//            + "      WHEN ERBRINGUNGSART = 'HaBh' THEN 2 "
//            + "      WHEN ERBRINGUNGSART = 'Bo' THEN 3 "
//            + "      WHEN ERBRINGUNGSART = 'BoBa' THEN 4 "
//            + "      WHEN ERBRINGUNGSART = 'BoBh' THEN 5 "
//            + "      WHEN ERBRINGUNGSART = 'BoBaBh' THEN 6 "
//            + "      WHEN ERBRINGUNGSART = 'HaBha' THEN 7 "
//            + "      ELSE -1 "
//            + "      END) ERBRINGUNGSART_INT "
//            + "   FROM IMEX_DEPARTMENT_TMP "
//            + "   WHERE (IS_PSEUDO = 0 AND IS_HOSPITAL = 0) "
//            + "     AND DAUER = (SELECT MAX(DEP.DAUER) FROM IMEX_DEPARTMENT_TMP DEP WHERE DEP.IKZ = IMEX_DEPARTMENT_TMP.IKZ AND DEP.FALLNR = IMEX_DEPARTMENT_TMP.FALLNR AND DEP.IS_PSEUDO = 0 AND DEP.IS_HOSPITAL = 0) "
//            + " ) TMP "
//            + " ORDER BY IKZ ASC, FALLNR ASC, "
//            + "          ERBRINGUNGSART_INT, ENTLASSUNGSDATUM DESC, VERLEGUNGSDATUM DESC "); //Order is important if there is more than 1 department with our criteria
//
//    pQry.executeUpdate("CREATE INDEX IDX_IMEX_BEHAND_FAB_TMP_FALL ON IMEX_BEHANDELNDE_FAB_TMP (IKZ, FALLNR) " + parallel());
//    pQry.executeUpdate("CREATE INDEX IDX_IMEX_BEHAND_FAB_TMP_NR ON IMEX_BEHANDELNDE_FAB_TMP (NR) " + parallel());
//    pQry.executeUpdate(DbBuilder.getImexPkDefinition("IMEX_BEHANDELNDE_FAB_TMP"));
        //printTime("IMEX_ERBRINGUNGSART_TMP created!");
        //printTime("Create IMEX_ERBRINGUNGSART_TMP2...");
//    pQry.executeUpdate((isOracle()?"CREATE TABLE IMEX_ERBRINGUNGSART_TMP2 NOLOGGING PARALLEL 5 AS ":"")
//          + " SELECT IKZ, FALLNR, ERBRINGUNGSART, ERBRINGUNGSART_INT "
//            + (isOracle()?"":" INTO IMEX_ERBRINGUNGSART_TMP2 ")
//          + " FROM IMEX_ERBRINGUNGSART_TMP "
//          + " WHERE ERBRINGUNGSART_INT = (SELECT MIN(ERBRINGUNGSART_INT) FROM IMEX_ERBRINGUNGSART_TMP ERBRINGART WHERE ERBRINGART.IKZ = IMEX_ERBRINGUNGSART_TMP.IKZ AND ERBRINGART.FALLNR = IMEX_ERBRINGUNGSART_TMP.FALLNR) "
//          + " GROUP BY IKZ, FALLNR, ERBRINGUNGSART, ERBRINGUNGSART_INT");
//    pQry.executeUpdate("CREATE INDEX IDX_IMEX_ERBRINGART_TMP2_FALL ON IMEX_ERBRINGUNGSART_TMP2 (IKZ, FALLNR) " + parallel());
        //printTime("IMEX_ERBRINGUNGSART_TMP2 created!");
        //printTime("Create IMEX_ERBRINGUNGSART...");
        //dropTable(pQry, "IMEX_ERBRINGUNGSART");
        getProgressor().sendProgress("Erzeuge IMEX_BEHANDELNDE_FAB");
        pQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_BEHANDELNDE_FAB NOLOGGING PARALLEL 5 AS " : "")
                + " SELECT "
                + "   TMP.*, "
                + "   (SELECT " + (isOracle() ? "" : "TOP 1") + " NR FROM IMEX_DEPARTMENT "
                + "    WHERE IMEX_DEPARTMENT.IKZ = TMP.IKZ AND IMEX_DEPARTMENT.FALLNR = TMP.FALLNR AND IMEX_DEPARTMENT.DAUER = TMP.DAUER AND IMEX_DEPARTMENT.ERBRINGUNGSART_INT = TMP.ERBRINGUNGSART_INT AND IMEX_DEPARTMENT.VERLEGUNGSDATUM = TMP.VERLEGUNGSDATUM " + (isOracle() ? "AND ROWNUM <= 1" : "ORDER BY VERLEGUNGSDATUM, ENTLASSUNGSDATUM, CODE") + ") NR, "
                + "   1 BEHANDELNDE_FAB "
                + (isOracle() ? "" : " INTO IMEX_BEHANDELNDE_FAB ")
                + " FROM ( "
                + "   SELECT IKZ, FALLNR, VERLEGUNGSDATUM, ERBRINGUNGSART, ERBRINGUNGSART_INT, DAUER FROM IMEX_DEPARTMENT "
                + "   WHERE DAUER = DAUER_BEHANDELNDE_FAB "
                + "     AND ERBRINGUNGSART_INT = VERLGRUND_BEHANDELNDE_FAB "
                + "     AND VERLEGUNGSDATUM = VERLDATUM_BEHANDELNDE_FAB "
                + "   GROUP BY IKZ, FALLNR, VERLEGUNGSDATUM, ERBRINGUNGSART, ERBRINGUNGSART_INT, DAUER "
                + " ) TMP");
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_BEHANDELNDE_FAB_FALL ON IMEX_BEHANDELNDE_FAB (IKZ, FALLNR) " + parallel());
        pQry.executeUpdate("CREATE INDEX IDX_IMEX_BEHANDELNDE_FAB_NR ON IMEX_BEHANDELNDE_FAB (NR) " + parallel());
        //pQry.executeUpdate(DbBuilder.getImexPkDefinition("IMEX_BEHANDELNDE_FAB"));

        printTime("  IMEX_BEHANDELNDE_FAB created!");
    }

    public List<Long> getReminderInternalId(final Query pCommonDbQry, final String pName) throws SQLException {
        final String query = "SELECT WM_RS_INTERNAL_ID FROM C_WM_LIST_REMINDER_SUBJECT "
                + " WHERE WM_RS_NAME = '" + pName + "' "
                + "   AND WM_RS_DELETED = 0 "
                + "   AND WM_RS_VALID = 1 "
                // + "   AND WM_RS_VALID_FROM <= ? AND WM_WS_VALID_TO >= ? "
                + " ORDER BY WM_RS_INTERNAL_ID, ID";
        List<Long> list = new ArrayList<>();
        try (ResultSet rs = pCommonDbQry.executeQuery(query)) {
            while (rs.next()) {
                Long internalId = rs.getLong("WM_RS_INTERNAL_ID");
                list.add(internalId);
            }
        }
        return list;
    }

    public Long getAdminId(final Query pCommonDbQry) throws SQLException {
        final List<Long> list = getUserId(pCommonDbQry, "admin");
        return list.isEmpty() ? null : list.iterator().next();
    }

    public long getJobUserId(final Query pCommonDbQry) throws SQLException {
        final List<String> userNames = new ArrayList<>();
        final long userIdForFallback;
        if (getInputConfig().isP21Import()) {
            userNames.add("imortJobP21");
            userNames.add("importJobP21");
            userIdForFallback = 1000001L;
            isP21Import = true;
        } else if (getInputConfig().isSapImport()) {
            userNames.add("imortJobSAP");
            userNames.add("importJobSAP");
            userIdForFallback = 1000002L;
        } else {
            userNames.add("imortJobDB");
            userNames.add("imortJobDB");
            userIdForFallback = 1000003L;
        }
        for (final String userName : userNames) {
            final List<Long> list = getUserId(pCommonDbQry, userName);
            if (!list.isEmpty()) {
                final long userId = list.iterator().next();
                LOG.log(Level.INFO, "found id={0} for job user name {1}", new Object[]{userId, userName});
                return userId;
            }
        }
        LOG.log(Level.INFO, "was not able to find user id for job, will use fallback id {0}", new Object[]{userIdForFallback});
        return userIdForFallback;
    }

    public List<Long> getUserId(final Query pCommonDbQry, final String pName) throws SQLException {
        final String query = "SELECT ID FROM CDB_USERS "
                + " WHERE U_NAME = '" + pName + "' "
                + "   AND U_IS_DELETED = 0 "
                + "   AND U_IS_VALID = 1 "
                // + "   AND WM_RS_VALID_FROM <= ? AND WM_WS_VALID_TO >= ? "
                + " ORDER BY ID";
        List<Long> list = new ArrayList<>();
        try (ResultSet rs = pCommonDbQry.executeQuery(query)) {
            while (rs.next()) {
                Long id = rs.getLong("ID");
                list.add(id);
            }
        }
        return list;
    }

    public Long getKainReminderInternalId(final Query pCommonDbQry) throws SQLException {
        List<Long> list = getReminderInternalId(pCommonDbQry, "Kain Nachricht eingegangen");
        return list.isEmpty() ? null : list.get(0);
    }

    public Long getCountExistingKAINMessages(final Query pDbQry) throws SQLException {
        final String query = "SELECT COUNT(NR) CNT FROM IMEX_KAIN_INKA_TMP "
                + " WHERE EXISTS "
                + " (SELECT 1 FROM T_P301_KAIN_INKA "
                + " WHERE IMEX_KAIN_INKA_TMP.CPX_EXTERNAL_MSG_NR = T_P301_KAIN_INKA.CPX_EXTERNAL_MSG_NR)";
        long caseCount = -1;

        try (ResultSet rs = pDbQry.executeQuery(query)) {
            while (rs.next()) {
                caseCount = rs.getLong("CNT");
            }
        }
        return caseCount;
    }

    
    public ImportConfig<T> getImportConfig() {
        return mImportConfig;
    }

    public T getModule() {
        return getImportConfig().getModule();
    }

    public CpxJobImportConfig getInputConfig() {
        return getModule().getInputConfig();
    }

    public License getLicense() {
        return mImportConfig == null ? null : mImportConfig.getLicense();
    }

    public CpxJobConstraints getImportConstraint() {
        return mImportConfig.getImportConstraint();
    }

    
    private void generateAdmissionMode(final Query caseDbQry) throws IOException, SQLException, ParseException
    {
        if(!isP21Import) {
            return;
        }
        
        printTime("Create IMEX_ADM_MODE_PROC...");

        dropTable(caseDbQry, "IMEX_ADM_MODE_PROC");

                getProgressor().sendProgress("Erzeuge IMEX_ADM_MODE_PROC");
                caseDbQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_ADMISSION_MODE_TMP NOLOGGING PARALLEL 5 AS " : "")
                        + "SELECT "
                        + "   TMP2.DEP_NR, TMP2.IKZ, TMP2.FALLNR, TMP2.BELEG_OP, TMP2.BELEG_ANA, TMP2.BELEG_HEB "
                        + (isOracle() ? "" : " INTO IMEX_ADM_MODE_PROC ")
                        + "FROM ("
                        + "  SELECT "
                        + "     IMEX_PROCEDURE_DEP.DEP_NR, IMEX_PROCEDURE_DEP.IKZ, IMEX_PROCEDURE_DEP.FALLNR, " 
                        + "     MAX(IMEX_PROCEDURE_TMP.BELEG_OP) BELEG_OP, MAX(IMEX_PROCEDURE_TMP.BELEG_ANA) BELEG_ANA, MAX(IMEX_PROCEDURE_TMP.BELEG_HEB) BELEG_HEB "
                        + "      FROM IMEX_PROCEDURE_DEP LEFT JOIN IMEX_PROCEDURE_TMP ON IMEX_PROCEDURE_DEP.NR = IMEX_PROCEDURE_TMP.NR " 
                        + "      GROUP BY IMEX_PROCEDURE_DEP.DEP_NR, IMEX_PROCEDURE_DEP.IKZ, IMEX_PROCEDURE_DEP.FALLNR"
                        + " ) TMP2");

                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_ADM_MODE_FALL ON IMEX_ADM_MODE_PROC (IKZ, FALLNR) " + parallel());
                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_ADM_MODE_DEP ON IMEX_ADM_MODE_PROC (DEP_NR) " + parallel());

                printTime("IMEX_ADM_MODE_PROC created!");
                
                
        printTime("Create IMEX_ADM_MODE_DEP...");

        dropTable(caseDbQry, "IMEX_ADM_MODE_DEP");

                getProgressor().sendProgress("Erzeuge IMEX_ADM_MODE_DEP");
                caseDbQry.executeUpdate((isOracle() ? "CREATE TABLE IMEX_ADM_MODE_DEP NOLOGGING PARALLEL 5 AS " : "")
                        + "SELECT "
                        + "   TMP2.DEP_NR, TMP2.IKZ, TMP2.FALLNR, TMP2.ERBRINGUNGSART, TMP2.CODE, TMP2.BELEG_OP, TMP2.BELEG_ANA, TMP2.BELEG_HEB "
                        + (isOracle() ? "" : " INTO IMEX_ADM_MODE_DEP ")
                        + "FROM ("
                        + "  SELECT "
                        + "     IMEX_ADM_MODE_PROC.DEP_NR, IMEX_ADM_MODE_PROC.IKZ, IMEX_ADM_MODE_PROC.FALLNR, IMEX_DEPARTMENT_TMP.ERBRINGUNGSART, " 
                        + "     IMEX_DEPARTMENT_TMP.CODE, IMEX_ADM_MODE_PROC.BELEG_OP, IMEX_ADM_MODE_PROC.BELEG_ANA, IMEX_ADM_MODE_PROC.BELEG_HEB "
                        + "      FROM IMEX_ADM_MODE_PROC LEFT JOIN IMEX_DEPARTMENT_TMP ON IMEX_ADM_MODE_PROC.DEP_NR = IMEX_DEPARTMENT_TMP.NR " 
                        + " ) TMP2");

                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_ADM_MODE_DEP_FALL ON IMEX_ADM_MODE_DEP (IKZ, FALLNR) " + parallel());
                caseDbQry.executeUpdate("CREATE INDEX IDX_IMEX_ADM_MODE_DEP_DEP ON IMEX_ADM_MODE_DEP (DEP_NR) " + parallel());

                printTime("IMEX_ADM_MODE_DEP created!");

             
                printTime("Update IMEX_DEPARTMENT_TMP with Admission-Mode...");

                caseDbQry.executeUpdate("UPDATE IMEX_DEPARTMENT_TMP SET ERBRINGUNGSART = (Select"
                         + " CASE "
                         + "    WHEN ERBRINGUNGSART = 'HA' AND BELEG_HEB = 1 THEN 'HaBh'"
                         + "    WHEN ERBRINGUNGSART = 'Bo' AND BELEG_OP = 1 AND BELEG_ANA = 0 AND BELEG_HEB = 0 THEN 'Bo'"
                         + "    WHEN ERBRINGUNGSART = 'Bo' AND BELEG_OP = 1 AND BELEG_ANA = 1 AND BELEG_HEB = 0 THEN 'BoBa'"
                         + "    WHEN ERBRINGUNGSART = 'Bo' AND BELEG_OP = 1 AND BELEG_ANA = 0 AND BELEG_HEB = 1 THEN 'BoBh'"
                         + "    WHEN ERBRINGUNGSART = 'Bo' AND BELEG_OP = 1 AND BELEG_ANA = 1 AND BELEG_HEB = 1 THEN 'BoBaBh'"
                         + "    ELSE 'HA'   END " 
                         + "   FROM IMEX_ADM_MODE_DEP "
                         + "   WHERE IMEX_DEPARTMENT_TMP.NR = IMEX_ADM_MODE_DEP.DEP_NR), "
                         + " ERBRINGUNGSART_INT = (Select "
                         + "   CASE "
                         + "    WHEN ERBRINGUNGSART = 'HA' AND BELEG_HEB = 1 THEN 2"
                         + "    WHEN ERBRINGUNGSART = 'Bo' AND BELEG_OP = 1 AND BELEG_ANA = 0 AND BELEG_HEB = 0 THEN 3"
                         + "    WHEN ERBRINGUNGSART = 'Bo' AND BELEG_OP = 1 AND BELEG_ANA = 1 AND BELEG_HEB = 0 THEN 4"
                         + "    WHEN ERBRINGUNGSART = 'Bo' AND BELEG_OP = 1 AND BELEG_ANA = 0 AND BELEG_HEB = 1 THEN 5"
                         + "    WHEN ERBRINGUNGSART = 'Bo' AND BELEG_OP = 1 AND BELEG_ANA = 1 AND BELEG_HEB = 1 THEN 6"
                         + "    ELSE 1      END "
                         + "   FROM IMEX_ADM_MODE_DEP "
                         + "   WHERE IMEX_DEPARTMENT_TMP.NR = IMEX_ADM_MODE_DEP.DEP_NR) "
                         + " WHERE NR IN (SELECT DEP_NR FROM IMEX_ADM_MODE_DEP)");
                 
                 
                printTime("Update IMEX_DEPARTMENT_TMP with Admission-Mode finished...");
 
    }
}
