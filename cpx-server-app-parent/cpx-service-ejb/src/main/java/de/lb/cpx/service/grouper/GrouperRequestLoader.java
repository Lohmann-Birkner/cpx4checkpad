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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.grouper;

import de.lb.cpx.service.helper.FailureCallback;
import de.lb.cpx.service.helper.Callback;
import de.lb.cpx.service.helper.DbCallback;
import de.lb.cpx.service.helper.ProgressCallback;
import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.grouper.model.transfer.BatchGroupParameter;
import de.lb.cpx.grouper.model.transfer.TransferCase;
import de.lb.cpx.grouper.model.transfer.TransferDepartment;
import de.lb.cpx.grouper.model.transfer.TransferIcd;
import de.lb.cpx.grouper.model.transfer.TransferLabor;
import de.lb.cpx.grouper.model.transfer.TransferOps;
import de.lb.cpx.grouper.model.transfer.TransferPatient;
import de.lb.cpx.grouper.model.util.GrouperConstant;
import de.lb.cpx.model.enums.AdmissionByLawEn;
import de.lb.cpx.model.enums.AdmissionCauseEn;
import de.lb.cpx.model.enums.AdmissionModeEn;
import de.lb.cpx.model.enums.AdmissionReason2En;
import de.lb.cpx.model.enums.AdmissionReasonEn;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.DischargeReason2En;
import de.lb.cpx.model.enums.DischargeReasonEn;
import de.lb.cpx.model.enums.GenderEn;
import de.lb.cpx.model.enums.IcdcRefTypeEn;
import de.lb.cpx.model.enums.IcdcTypeEn;
import de.lb.cpx.model.enums.InsStatusEn;
import de.lb.cpx.model.enums.LocalisationEn;
import de.lb.cpx.server.config.CpxServerConfigLocal;
import de.lb.cpx.service.helper.RequestLoader;
import de.lb.cpx.str.utils.StrUtils;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 *
 * @author niemeier
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class GrouperRequestLoader extends RequestLoader{ 

    private static final Logger LOG = Logger.getLogger(GrouperRequestLoader.class.getName());

    @EJB(name = "CpxServerConfig")
    private CpxServerConfigLocal cpxServerConfig;

    public static String getFromWhereQuery(final BatchGroupParameter pParameter, final boolean isOracle) {
        Date adf = pParameter.getAdmissionDateFrom();
        Date adu = pParameter.getAdmissionDateUntil();
        Date ddf = pParameter.getDischargeDateFrom();
        Date ddu = pParameter.getDischargeDateUntil();

        //now, let's switch values if they are not in the right chronical order
        if (adf != null && adu != null && adf.after(adu)) {
            Date tmp = adu;
            adu = adf;
            adf = tmp;
        }
        if (ddf != null && ddu != null && ddf.after(ddu)) {
            Date tmp = ddu;
            ddu = ddf;
            ddf = tmp;
        }

        final Set<Long> caseIds = Collections.unmodifiableSet(toFilteredSet(pParameter.getCaseIds()));
        final Date admissionDateFrom = adf; //pParameter.getAdmissionDateFrom();
        final Date admissionDateToPlus1 = addDays(adu, 1); //addDays(pParameter.getAdmissionDateUntil(), 1);
        final Date dischargeDateFrom = ddf; //pParameter.getDischargeDateFrom();
        final Date dischargeDateToPlus1 = addDays(ddu, 1); //addDays(pParameter.getDischargeDateUntil(), 1);
        final Date minAdmissionDateFrom = getMinAdmissionDateFrom();

        String newQuery = " FROM T_CASE CS "
                + " INNER JOIN T_CASE_DETAILS CSD ON CSD.T_CASE_ID = CS.ID "
                + (pParameter.doInclNonActual() ? "" : " AND CSD.ACTUAL_FL = 1 ")
                + (pParameter.doInclHis() ? "" : " AND CSD.LOCAL_FL = 1 ");
        if (caseIds.isEmpty()) {
            newQuery += (admissionDateFrom == null ? "" : " AND CSD.ADMISSION_DATE >= " + StrUtils.toStaticDate(admissionDateFrom, !isOracle))
                    + (admissionDateToPlus1 == null ? "" : " AND CSD.ADMISSION_DATE < " + StrUtils.toStaticDate(admissionDateToPlus1, !isOracle))
                    + (dischargeDateFrom == null ? "" : " AND CSD.DISCHARGE_DATE >= " + StrUtils.toStaticDate(dischargeDateFrom, !isOracle))
                    + (dischargeDateToPlus1 == null ? "" : " AND CSD.DISCHARGE_DATE < " + StrUtils.toStaticDate(dischargeDateToPlus1, !isOracle));
        } else {
            String values = caseIds.stream()
                    .map(n -> n.toString())
                    .collect(Collectors.joining(" OR CS.ID = "));
            newQuery += " AND (CS.ID = " + values + ")";
        }
        newQuery += (pParameter.isGrouped() ? "" : " AND NOT EXISTS (SELECT 1 FROM T_GROUPING_RESULTS GR WHERE CSD.ID = GR.T_CASE_DETAILS_ID AND " + (pParameter.getModel() == null || GDRGModel.AUTOMATIC.equals(pParameter.getModel()) ? " GR.GRPRES_IS_AUTO_FL = 1 " : (" GR.GRPRES_IS_AUTO_FL = 0 AND GR.MODEL_ID_EN = '" + pParameter.getModel().name() + "'")) + ")");
        newQuery += " AND CSD.DISCHARGE_DATE IS NOT NULL"; //only group cases with discharge date
        newQuery += " AND CSD.ADMISSION_DATE >= " + StrUtils.toStaticDate(minAdmissionDateFrom, !isOracle);
        newQuery += " AND CS.CS_CASE_TYPE_EN IN ('DRG', 'PEPP')";
        return newQuery;
    }

    public void start(
            final Connection pConn,
            //final Collection<Long> pCaseIds,
            //final Date pAdmissionDateFrom,
            //final Date pAdmissionDateTo,
            //final Date pDischargeDateFrom,
            //final Date pDischargeDateTo,
            //final boolean isGrouped,
            final BatchGroupParameter pParameter,
            final boolean isOracle,
            final BlockingQueue<TransferPatient> pTargetQueue,
            final AtomicInteger caseCount,
            final AtomicInteger caseDetailsCount,
            final AtomicBoolean stopSignal,
            final Callback stoppedCb,
            final AtomicBoolean requestLoaderStopped,
            final Callback requestLoaderFinishedCb,
            final ProgressCallback requestLoaderProgressCb,
            final FailureCallback failureCb,
            final AtomicLong timer,
            final AtomicLong grouperRequestLoaderTotalTime,
            final AtomicLong grouperResponseLoaderTotalTime) {

        this.stopSignal = stopSignal;
        this.requestLoaderStopped = requestLoaderStopped;
        this.stoppedCb = stoppedCb;

        grouperRequestLoaderTotalTime.set(System.currentTimeMillis());

        final long executionId = System.currentTimeMillis();
        final int fetchRows = 10000; //very relevant for performance!
        final String tmpTable = "GRP_JOB_" + executionId;
        final String tmpSeq = tmpTable + "_SQ";
        LOG.log(Level.INFO, "Write temporary case ids to table " + tmpTable);

        final int maxSteps = 10;
        final AtomicInteger step = new AtomicInteger(0);
        String newQuery =   getCreateJobIdsTableQry(isOracle, tmpTable, tmpSeq, pParameter);

//        String newQuery = (isOracle ? "CREATE TABLE " + tmpTable + " NOLOGGING PARALLEL 5 AS " : "")
//
////                + "SELECT " + nextSqVal(isOracle, tmpSeq) + " ID, CS.ID T_CASE_ID, CSD.ID T_CASE_DETAILS_ID, CS.T_PATIENT_ID, CSD.LOCAL_FL, CS.CS_HOSPITAL_IDENT, CS.CS_CASE_NUMBER "
////        String newQuery = (isOracle ? "CREATE TABLE " + tmpTable + " NOLOGGING PARALLEL 5 AS " : "")
////                + "SELECT " + nextSqVal(isOracle, tmpSeq) + (isOracle?" ID,":"  over (order by CS.T_PATIENT_ID, CSD.ADMISSION_DATE) AS ID, ")
////                + "CS.ID T_CASE_ID, CSD.ID T_CASE_DETAILS_ID, CS.T_PATIENT_ID, CSD.LOCAL_FL, CS.CS_HOSPITAL_IDENT, CS.CS_CASE_NUMBER "
//                + (isOracle ? "" : " INTO " + tmpTable);
//        newQuery += getFromWhereQuery(pParameter, isOracle);

        final String caseTable = tmpTable + "_CS";
        final String icdTable = tmpTable + "_ICD";
        final String opsTable = tmpTable + "_OPS";
        final String feeTable = tmpTable + "_FEE";
        final String labTable = tmpTable + "_LAB";
        final String depTable = tmpTable + "_DEP";
        final String wardTable = tmpTable + "_WRD";
//        File zipFile = null;
        final boolean useLabor = cpxServerConfig.getLaboratoryDataDisplayTab();
        try ( Statement stmt = pConn.createStatement()) {
            try {
                requestLoaderProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Lösche alte Tabellen...");
                if (checkStopped()) {
                    return;
                }
                dropOldTables(stmt, isOracle);

                dropTable(stmt, tmpTable);
                dropSequence(stmt, tmpSeq);
                requestLoaderProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Erzeuge temporäre Tabelle " + tmpTable + "...");
                if (checkStopped()) {
                    return;
                }
                LOG.log(Level.INFO, "List of hospital cases for batchgrouping:\n" + newQuery);
                stmt.execute("CREATE SEQUENCE " + tmpSeq);
                stmt.execute(newQuery);

                requestLoaderProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Erzeuge Indizes...");
                if (checkStopped()) {
                    return;
                }
                stmt.execute("ALTER TABLE " + tmpTable + " ADD CONSTRAINT " + tmpTable + "_PK PRIMARY KEY (ID)");
                stmt.execute("CREATE INDEX I_" + tmpTable + "_CS ON " + tmpTable + " (T_CASE_ID)");
                stmt.execute("CREATE INDEX I_" + tmpTable + "_CSD ON " + tmpTable + " (T_CASE_DETAILS_ID)");
                stmt.execute("CREATE INDEX I_" + tmpTable + "_PT ON " + tmpTable + " (T_PATIENT_ID)");

                requestLoaderProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Entferen Fälle mit mehreren Hauptdiagnosen aus der temporären Tabelle " + tmpTable + "...");
                String qry = "DELETE FROM " + tmpTable + " where T_CASE_DETAILS_ID IN ("
                        + " SELECT DEP.T_CASE_DETAILS_ID from T_CASE_ICD ICD "
                        + "INNER JOIN T_CASE_DEPARTMENT DEP ON DEP.ID = ICD.T_CASE_DEPARTMENT_ID "
                        + "INNER JOIN " + tmpTable + " TMP "
                        + " ON TMP.T_CASE_DETAILS_ID = DEP.T_CASE_DETAILS_ID WHERE ICD.MAIN_DIAG_CASE_FL = 1 GROUP BY(DEP.T_CASE_DETAILS_ID) "
                        + " HAVING COUNT(DEP.T_CASE_DETAILS_ID) > 1)";
                LOG.log(Level.INFO, qry);
                stmt.execute(qry);

                dropTable(stmt, caseTable);
                requestLoaderProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Erzeuge temporäre Tabelle " + caseTable + "...");
                if (checkStopped()) {
                    return;
                }
                qry = (isOracle ? "CREATE TABLE " + caseTable + " NOLOGGING PARALLEL 5 AS " : "")
                        + "SELECT "
                        + "TMP.ID TMP_ID, "
                        + "TMP.T_CASE_ID, "
                        + "TMP.T_PATIENT_ID, "
                        + "TMP.T_CASE_DETAILS_ID, "
                        + "TMP.LOCAL_FL, "
                        + "CS.CS_HOSPITAL_IDENT, "
                        + "CS.CS_CASE_NUMBER, "
                        + "CS.CS_CASE_TYPE_EN, "
                        + "CS.CS_DOCTOR_IDENT, "
                        + "PAT.PAT_DATE_OF_BIRTH, "
                        + "CSD.GENDER_EN, "
                        + "CSD.ADMISSION_DATE, "
                        + "CSD.ADMISSION_CAUSE_EN, "
                        + "CSD.ADMISSION_REASON_12_EN, "
                        + "CSD.ADMISSION_REASON_34_EN, "
                        + "CSD.ADMISSION_WEIGHT, "
                        + "CSD.DISCHARGE_DATE, "
                        + "CSD.DISCHARGE_REASON_12_EN, "
                        + "CSD.DISCHARGE_REASON_3_EN, "
                        + "CSD.AGE_DAYS, "
                        + "CSD.AGE_YEARS, "
                        + "CSD.HMV, "
                        + "CSD.ADMISSION_LAW_EN, "
                        + "CSD.LEAVE, "
                        + "CSD.LOS_ALTERATION, "
                        + "CSD.ADMISSION_MODE_EN, "
                        + "CS.STRING_01, "
                        + "CS.STRING_02, "
                        + "CS.STRING_03, "
                        + "CS.STRING_04, "
                        + "CS.STRING_05, "
                        + "CS.STRING_06, "
                        + "CS.STRING_07, "
                        + "CS.STRING_08, "
                        + "CS.STRING_09, "
                        + "CS.STRING_10, "
                        + "CS.NUMERIC_01, "
                        + "CS.NUMERIC_02, "
                        + "CS.NUMERIC_03, "
                        + "CS.NUMERIC_04, "
                        + "CS.NUMERIC_05, "
                        + "CS.NUMERIC_06, "
                        + "CS.NUMERIC_07, "
                        + "CS.NUMERIC_08, "
                        + "CS.NUMERIC_09, "
                        + "CS.NUMERIC_10, "
                        + "PATD.PATD_CITY, "
                        + "PATD.PATD_ZIPCODE, "
                        + "INS.INS_STATUS_EN, "
                        + "CSD_LOS_MD_ALTERATION "
                        + (isOracle ? "" : " INTO " + caseTable)
                        + " FROM T_CASE CS "
                        + "INNER JOIN " + tmpTable + " TMP ON CS.ID = TMP.T_CASE_ID "
                        + "INNER JOIN T_CASE_DETAILS CSD ON CSD.ID = TMP.T_CASE_DETAILS_ID "
                        + "INNER JOIN T_PATIENT PAT ON PAT.ID = TMP.T_PATIENT_ID "
                        + "LEFT JOIN T_PATIENT_DETAILS PATD ON PATD.T_PATIENT_ID = TMP.T_PATIENT_ID AND PATD.PATD_IS_ACTUAL_FL = 1 "
                        + "LEFT JOIN T_INSURANCE INS ON INS.T_PATIENT_ID = TMP.T_PATIENT_ID AND INS.INS_IS_ACTUAL_FL = 1 ";

                stmt.execute(qry);
                LOG.log(Level.INFO, qry);
                dropTable(stmt, depTable);
                requestLoaderProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Erzeuge temporäre Tabelle " + depTable + "...");
                if (checkStopped()) {
                    return;
                }
                qry = (isOracle ? "CREATE TABLE " + depTable + " NOLOGGING PARALLEL 5 AS " : "")
                        + "SELECT "
                        + "TMP.ID TMP_ID, "
                        + "DEP.ID T_CASE_DEPARTMENT_ID, "
                        //                        + "DEP.DEP_SHORT_NAME, "
                        + "DEP.dep_key_301, "
                        + "DEP.DEPC_ADM_DATE, "
                        + "DEP.DEPC_DIS_DATE, "
                        + "DEP.DEPC_IS_ADMISSION_FL, "
                        + "DEP.DEPC_IS_DISCHARGE_FL, "
                        + "DEP.DEPC_IS_TREATING_FL "
                        + (isOracle ? "" : " INTO " + depTable)
                        + " FROM T_CASE_DEPARTMENT DEP "
                        + "INNER JOIN " + tmpTable + " TMP ON DEP.T_CASE_DETAILS_ID = TMP.T_CASE_DETAILS_ID ";
                stmt.execute(qry);
                LOG.log(Level.INFO, qry);

                dropTable(stmt, icdTable);
                requestLoaderProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Erzeuge temporäre Tabelle " + icdTable + "...");
                if (checkStopped()) {
                    return;
                }
                qry = (isOracle ? "CREATE TABLE " + icdTable + " NOLOGGING PARALLEL 5 AS " : "")
                        + "SELECT "
                        + "TMP.ID TMP_ID, "
                        + "ICD.T_CASE_DEPARTMENT_ID, "
                        + "ICD.TO_GROUP_FL, "
                        + "ICD.ID T_CASE_ICD_ID, "
                        + "ICD.ICDC_TYPE_EN, "
                        + "ICD.MAIN_DIAG_CASE_FL, "
                        + "ICD.ICDC_CODE, "
                        + "ICD.ICDC_LOC_EN, "
                        + "ICD.ICD_REFERENCE_EN, "
                        + "ICD_REF.ID REF_T_CASE_ICD_ID, "
                        + "ICD_REF.ICDC_TYPE_EN REF_ICDC_TYPE_EN, "
                        + "ICD_REF.MAIN_DIAG_CASE_FL MAIN_DIAG_CASE_FL_REF, "
                        + "ICD_REF.ICDC_CODE ICDC_CODE_REF, "
                        + "ICD_REF.ICDC_LOC_EN ICDC_LOC_EN_REF, "
                        + "DEP.DEPC_ADM_DATE "
                        + (isOracle ? "" : " INTO " + icdTable)
                        + " FROM T_CASE_ICD ICD "
                        + "INNER JOIN T_CASE_DEPARTMENT DEP ON DEP.ID = ICD.T_CASE_DEPARTMENT_ID "
                        + "INNER JOIN " + tmpTable + " TMP ON DEP.T_CASE_DETAILS_ID = TMP.T_CASE_DETAILS_ID "
                        + "LEFT JOIN T_CASE_ICD ICD_REF ON ICD_REF.ID = ICD.T_CASE_ICD_ID ";
//                        + "WHERE ICD.T_CASE_ICD_ID IS NOT NULL OR NOT EXISTS (SELECT 1 FROM T_CASE_ICD ICD2 WHERE ICD2.T_CASE_ICD_ID = ICD.ID) ";
                LOG.log(Level.INFO, qry);
                stmt.execute(qry);

                dropTable(stmt, opsTable);
                requestLoaderProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Erzeuge temporäre Tabelle " + opsTable + "...");
                if (checkStopped()) {
                    return;
                }
                qry = (isOracle ? "CREATE TABLE " + opsTable + " NOLOGGING PARALLEL 5 AS " : "")
                        + "SELECT "
                        + "TMP.ID TMP_ID, "
                        + "OPS.T_CASE_DEPARTMENT_ID, "
                        + "OPS.ID T_CASE_OPS_ID, "
                        + "OPS.OPSC_CODE, "
                        + "OPS.OPSC_LOC_EN, "
                        + "OPS.OPSC_DATUM, "
                        + "DEP.DEPC_ADM_DATE "
                        + (isOracle ? "" : " INTO " + opsTable)
                        + " FROM T_CASE_OPS OPS "
                        + "INNER JOIN T_CASE_DEPARTMENT DEP ON DEP.ID = OPS.T_CASE_DEPARTMENT_ID "
                        + "INNER JOIN " + tmpTable + " TMP ON DEP.T_CASE_DETAILS_ID = TMP.T_CASE_DETAILS_ID "
                        + "WHERE OPS.TO_GROUP_FL = 1";
                LOG.log(Level.INFO, qry);
                stmt.execute(qry);

                dropTable(stmt, wardTable);
                requestLoaderProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Erzeuge temporäre Tabelle " + wardTable + "...");
                if (checkStopped()) {
                    return;
                }
                qry = (isOracle ? "CREATE TABLE " + wardTable + " NOLOGGING PARALLEL 5 AS " : "")
                        + "SELECT "
                        + "TMP.ID TMP_ID, "
                        + "WARD.T_CASE_DEPARTMENT_ID, "
                        + "WARD.ID T_CASE_WARD_ID, "
                        + "WARD.WARDC_IDENT, "
                        + "WARD.WARDC_ADMDATE, "
                        + "DEP.DEPC_ADM_DATE "
                        + (isOracle ? "" : " INTO " + wardTable)
                        + " FROM T_CASE_WARD WARD "
                        + "INNER JOIN T_CASE_DEPARTMENT DEP ON DEP.ID = WARD.T_CASE_DEPARTMENT_ID "
                        + "INNER JOIN " + tmpTable + " TMP ON DEP.T_CASE_DETAILS_ID = TMP.T_CASE_DETAILS_ID ";
                LOG.log(Level.INFO, qry);
                stmt.execute(qry);

                dropTable(stmt, feeTable);
                requestLoaderProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Erzeuge temporäre Tabelle " + feeTable + "...");
                if (checkStopped()) {
                    return;
                }
                stmt.execute((isOracle ? "CREATE TABLE " + feeTable + " NOLOGGING PARALLEL 5 AS " : "")
                        + "SELECT "
                        + "TMP.ID TMP_ID, "
                        // + "TMP.CS_CASE_NUMBER, "
                        // + "TMP.CS_HOSPITAL_IDENT, "
                        //                                    + "FEE.FEEC_INSURANCE, "
                        + "FEE.FEEC_FEEKEY, "
                        + "FEE.FEEC_VALUE, "
                        + "FEE.FEEC_FROM, "
                        + "FEE.FEEC_TO, "
                        + "FEE.FEEC_COUNT, "
                        + "FEE.FEEC_UNBILLED_DAYS "
                        + (isOracle ? "" : " INTO " + feeTable)
                        + " FROM T_CASE_FEE FEE "
                        + "INNER JOIN " + tmpTable + " TMP ON FEE.T_CASE_DETAILS_ID = TMP.T_CASE_DETAILS_ID ");
                //+ "INNER JOIN T_CASE CS ON CS.ID = TMP.T_CASE_ID "
                // + "ORDER BY TMP.ID, FEE.FEEC_FROM, FEE.FEEC_TO");
// Labor 

                dropTable(stmt, labTable);
                requestLoaderProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Erzeuge temporäre Tabelle " + labTable + "...");
                if (checkStopped()) {
                    return;
                }
                qry = (isOracle ? "CREATE TABLE " + labTable + " NOLOGGING PARALLEL 5 AS " : "")
                        + "SELECT "
                        + "TMP.ID TMP_ID, "
                        // + "TMP.CS_CASE_NUMBER, "
                        // + "TMP.CS_HOSPITAL_IDENT, "
                        //                                    + "FEE.FEEC_INSURANCE, "
                        + "LAB.LAB_VALUE, "
                        + "LAB.LAB_TEXT, "
                        + "LAB.LAB_UNIT, "
                        + "LAB.LAB_DESCRIPTION, "
                        + "LAB.LAB_ANALYSIS_DATE "
                        + (isOracle ? "" : " INTO " + labTable)
                        + " FROM T_LAB LAB "
                        + "INNER JOIN " + tmpTable + " TMP ON LAB.T_CASE_ID = TMP.T_CASE_ID "
                        + (useLabor ? "" : " WHERE 1 = 2");
                LOG.log(Level.INFO, qry);
                stmt.execute(qry);
                requestLoaderProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Erzeuge Indizes...");
                if (checkStopped()) {
                    return;
                }
                stmt.execute("CREATE INDEX IX_" + caseTable + " ON " + caseTable + " (TMP_ID)");
                stmt.execute("CREATE INDEX IX_" + depTable + " ON " + depTable + " (TMP_ID, DEPC_ADM_DATE, T_CASE_DEPARTMENT_ID)");
                stmt.execute("CREATE INDEX IX_" + icdTable + " ON " + icdTable + " (TMP_ID, DEPC_ADM_DATE, T_CASE_DEPARTMENT_ID)");
                stmt.execute("CREATE INDEX IX_" + opsTable + " ON " + opsTable + " (TMP_ID, DEPC_ADM_DATE, T_CASE_DEPARTMENT_ID)");
                stmt.execute("CREATE INDEX IX_" + wardTable + " ON " + wardTable + " (TMP_ID, T_CASE_WARD_ID, DEPC_ADM_DATE, WARDC_ADMDATE, T_CASE_DEPARTMENT_ID)");
                stmt.execute("CREATE INDEX IX_" + feeTable + " ON " + feeTable + " (TMP_ID)");
                stmt.execute("CREATE INDEX IX_" + labTable + " ON " + labTable + " (TMP_ID)");

                requestLoaderProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Sammel Grouping-Daten...");
                if (checkStopped()) {
                    return;
                }
                
                
                executeStatement(pConn, fetchRows, "SELECT * FROM " + caseTable + " ORDER BY TMP_ID", (rsCs) -> {
                    executeStatement(pConn, fetchRows, "SELECT * FROM " + labTable + " ORDER BY TMP_ID", (rsLab) -> {
                        executeStatement(pConn, fetchRows, "SELECT * FROM " + feeTable + " ORDER BY TMP_ID", (rsFee) -> { //TMP_ID, DEPC_ADM_DATE, DEPC_DIS_DATE, DEP_SHORT_NAME
                            executeStatement(pConn, fetchRows, "SELECT * FROM " + depTable + " ORDER BY TMP_ID, DEPC_ADM_DATE, T_CASE_DEPARTMENT_ID", (rsDep) -> { //TMP_ID, DEPC_ADM_DATE, DEPC_DIS_DATE, DEP_SHORT_NAME
                                executeStatement(pConn, fetchRows, "SELECT * FROM " + wardTable + " ORDER BY TMP_ID, DEPC_ADM_DATE, T_CASE_DEPARTMENT_ID", (rsWard) -> { //TMP_ID, DEPC_ADM_DATE, DEPC_DIS_DATE, DEP_SHORT_NAME
                                    executeStatement(pConn, fetchRows, "SELECT * FROM " + icdTable + " ORDER BY TMP_ID, DEPC_ADM_DATE, T_CASE_DEPARTMENT_ID", (rsIcd) -> { //TMP_ID, CASE WHEN ICDC_CODE_REF IS NULL THEN ICDC_CODE ELSE ICDC_CODE_REF END, ICDC_CODE_REF
                                        executeStatement(pConn, fetchRows, "SELECT * FROM " + opsTable + " ORDER BY TMP_ID, DEPC_ADM_DATE, T_CASE_DEPARTMENT_ID", (rsOps) -> { //TMP_ID, OPSC_DATUM, OPSC_CODE

                                            //                try (
                                            //                        final ResultSet rsCs = executeStatement(pConn, fetchRows, "SELECT * FROM " + caseTable + " ORDER BY TMP_ID");
                                            //                        final ResultSet rsDep = executeStatement(pConn, fetchRows, "SELECT * FROM " + depTable + " ORDER BY TMP_ID, DEPC_ADM_DATE, T_CASE_DEPARTMENT_ID"); //TMP_ID, DEPC_ADM_DATE, DEPC_DIS_DATE, DEP_SHORT_NAME
                                            //                        final ResultSet rsIcd = executeStatement(pConn, fetchRows, "SELECT * FROM " + icdTable + " ORDER BY TMP_ID, DEPC_ADM_DATE, T_CASE_DEPARTMENT_ID"); //TMP_ID, CASE WHEN ICDC_CODE_REF IS NULL THEN ICDC_CODE ELSE ICDC_CODE_REF END, ICDC_CODE_REF
                                            //                        final ResultSet rsOps = executeStatement(pConn, fetchRows, "SELECT * FROM " + opsTable + " ORDER BY TMP_ID, DEPC_ADM_DATE, T_CASE_DEPARTMENT_ID"); //TMP_ID, OPSC_DATUM, OPSC_CODE
                                            //                        //                                    final ResultSet rsFee = executeStatement(conn, fetchRows, "SELECT * FROM " + feeTable + " ORDER BY TMP_ID"); //TMP_ID, FEEC_FROM, FEEC_TO, FEEC_FEEKEY
                                            //                        ) {
                                            requestLoaderProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Beginne Daten an den Grouper zu übergeben...");
                                            if (checkStopped()) {
                                                return;
                                            }

                                            try ( ResultSet rs = stmt.executeQuery("SELECT COUNT(*) CNT FROM " + tmpTable)) {
                                                while (rs.next()) {
                                                    caseDetailsCount.set(rs.getInt("CNT"));
                                                }
                                            }
                                            LOG.log(Level.INFO, "Found " + caseDetailsCount + " case details for batch grouping");

                                            try ( ResultSet rs = stmt.executeQuery("SELECT COUNT(DISTINCT T_CASE_ID) CNT FROM " + tmpTable)) {
                                                while (rs.next()) {
                                                    caseCount.set(rs.getInt("CNT"));
                                                }
                                            }
                                            LOG.log(Level.INFO, "Found " + caseCount + " cases for batch grouping");

                                            //set cursors on first entry
                                            final boolean depEmpty = !rsDep.next();
                                            final boolean feeEmpty = !rsFee.next();
                                            final boolean opsEmpty = !rsOps.next();
                                            final boolean icdEmpty = !rsIcd.next();
                                            final boolean labEmpty = !rsLab.next();
                                            final boolean wardEmpty = !rsWard.next();

                                            grouperRequestLoaderTotalTime.set(System.currentTimeMillis() - grouperRequestLoaderTotalTime.get());
                                            grouperResponseLoaderTotalTime.set(System.currentTimeMillis());
                                            TransferPatient trPatientLocal = new TransferPatient(pParameter.getModelId());
                                            TransferPatient trPatientKIS = new TransferPatient(pParameter.getModelId());
                                            long tmpPatIdLocal = trPatientLocal.getPatientId();
                                            long tmpPatIdKIS = trPatientKIS.getPatientId();
                                            TransferCase req = null;
                                            try {

                                                //int caseDetailsNo = 0;
                                                while (rsCs.next()) {
                                                    if (checkStopped()) {
                                                        return;
                                                    }
                                                    final long timeStart = System.nanoTime();
                                                    //caseDetailsNo++;
                                                    final long tmpId = rsCs.getLong(1);
                                                    final long caseId = rsCs.getLong(2); //T_CASE_ID
                                                    final long tmpPatientId = rsCs.getLong(3);// T_PATIENT_ID
                                                    final long caseDetailsId = rsCs.getLong(4); //T_CASE_DETAILS_ID
                                                    final boolean isLocalFl = rsCs.getBoolean(5); //LOCAL_FL
                                                    final String hosIdent = rsCs.getString(6); //CS_HOSPITAL_IDENT
                                                    final String caseNumber = rsCs.getString(7); //CS_CASE_NUMBER
                                                    //LOG.log(Level.FINEST, "TMP_ID: " + tmpId + ", Case Key: " + hosIdent + "_" + caseNumber);
                                                    final CaseTypeEn caseType = CaseTypeEn.valueOf(formatCaseType(rsCs.getString(8))); //CS_CASE_TYPE_EN
                                                    final String doctorIdent = rsCs.getString(9); //CS_DOCTOR_IDENT
                                                    final Date dateOfBirth = rsCs.getDate(10); //PAT_DATE_OF_BIRTH
                                                    final GenderEn gender = GenderEn.findByName(rsCs.getString(11)); //CSD_GENDER_EN
                                                    final Date admissionDate = rsCs.getTimestamp(12); //ADMISSION_DATE // we need time too
                                                    final AdmissionCauseEn admissionCause = AdmissionCauseEn.findByName(rsCs.getString(13)); //ADMISSION_CAUSE_EN
                                                    final AdmissionReasonEn admissionReason12 = AdmissionReasonEn.findById(rsCs.getString(14)); //ADMISSION_REASON_12_EN
                                                    final AdmissionReason2En admissionReason34 = AdmissionReason2En.findById(rsCs.getString(15)); //ADMISSION_REASON_34_EN
                                                    final int admissionWeight = rsCs.getInt(16); //ADMISSION_WEIGHT
                                                    final Date dischargeDate = rsCs.getTimestamp(17); //DISCHARGE_DATE
                                                    final DischargeReasonEn dischargeReason12 = DischargeReasonEn.findById(rsCs.getString(18)); //DISCHARGE_REASON_12_EN
                                                    final DischargeReason2En dischargeReason3 = DischargeReason2En.findById(rsCs.getString(19)); //DISCHARGE_REASON_3_EN
                                                    final int ageInDays = rsCs.getInt(20); //AGE_DAYS
                                                    final int ageInYears = rsCs.getInt(21); //AGE_YEARS
                                                    final int hmv = rsCs.getInt(22);
                                                    final AdmissionByLawEn admissionLaw = AdmissionByLawEn.findById(rsCs.getString(23));
                                                    final int leave = rsCs.getInt(24);
                                                    final int losAlteration = rsCs.getInt(25);
                                                    final AdmissionModeEn admissionMode = AdmissionModeEn.findByName(rsCs.getString(26));
                                                    final String string01 = rsCs.getString(27);
                                                    final String string02 = rsCs.getString(28);
                                                    final String string03 = rsCs.getString(29);
                                                    final String string04 = rsCs.getString(30);
                                                    final String string05 = rsCs.getString(31);
                                                    final String string06 = rsCs.getString(32);
                                                    final String string07 = rsCs.getString(33);
                                                    final String string08 = rsCs.getString(34);
                                                    final String string09 = rsCs.getString(35);
                                                    final String string10 = rsCs.getString(36);
                                                    final Integer numeric01 = getNull(rsCs, 37);
                                                    final Integer numeric02 = getNull(rsCs, 38);
                                                    final Integer numeric03 = getNull(rsCs, 39);
                                                    final Integer numeric04 = getNull(rsCs, 40);
                                                    final Integer numeric05 = getNull(rsCs, 41);
                                                    final Integer numeric06 = getNull(rsCs, 42);
                                                    final Integer numeric07 = getNull(rsCs, 43);
                                                    final Integer numeric08 = getNull(rsCs, 44);
                                                    final Integer numeric09 = getNull(rsCs, 45);
                                                    final Integer numeric10 = getNull(rsCs, 46);
                                                    final String city = rsCs.getString(47);
                                                    final String zipCode = rsCs.getString(48);
                                                    final String insStr = rsCs.getString(49);
                                                    final InsStatusEn insStateEn = insStr == null ? null : InsStatusEn.valueOf(insStr);
                                                    final Integer losSMdSim = getNull(rsCs, 50);
                                                    if(isLocalFl){
                                                        tmpPatIdLocal = tmpPatientId;
                                                    }else{
                                                        tmpPatIdKIS = tmpPatientId;
                                                    }
                                                    req = new TransferCase();
                                                    req.setBatchGrouperParameter(pParameter);
                                                    req.setGrouperModelId(pParameter.getModelId());
                                                    //                                    req.setGrouperModelId(pParameter.getModel() == null ? GDRGModel.AUTOMATIC.getGDRGVersion() : pParameter.getModel().getGDRGVersion());
                                                    req.setIkz(hosIdent);
                                                    req.setCaseNumber(caseNumber);
                                                    req.setCaseId(caseId);
                                                    req.setCaseDetailsId(caseDetailsId);
                                                    //isDRGCase        
                                                    req.setCaseType(caseType.getId()); //DRG/PEPP und sw.
                                                    req.setDoctorIdent(doctorIdent);
                                                    req.setSex(gender == null ? 0 : gender.getId());// grouper likes to have 0 for null here
                                                    if (dateOfBirth != null) {
                                                        req.setDateOfBirth(dateOfBirth);
                                                    }
                                                    req.setIsLocal(isLocalFl);
                                                    // Kasse
                                                    //grouperRequest.setKasse(csd.getCsdInsCompany());
                                                    //Alter        
                                                    req.setAgeD(ageInDays);
                                                    req.setAgeY(ageInYears);
                                                    // Aufnahmedatum        
                                                    req.setAdmissionDate(admissionDate);
                                                    // Entlassungsdatum        
                                                    req.setDischargeDate(dischargeDate);
                                                    //Aufnahmeanlass        
                                                    req.setAdmissionCause(admissionCause.getId());
                                                    //Aufnahmegrund12        
                                                    req.setAdmissionReason12(admissionReason12 == null ? 0 : admissionReason12.getIdInt());
                                                    //Aufnahmegrund34        
                                                    req.setAdmissionReason34(admissionReason34 == null ? 0 : admissionReason34.getIdInt());
                                                    // aufenthalt ausserhalb
                                                    req.setNALOS(leave);
                                                    // Aufnahmegewicht
                                                    req.setWeight(admissionWeight);
                                                    // Simulationsänderung an dem VWD
                                                    req.setLosAlteration(losAlteration);
                                                    // Erbringungsart
                                                    req.setDepartmentType(admissionMode.getId());
                                                    //Beatmungsstunden  
                                                    req.setRespirationLength(hmv);
                                                    //Art der Behandlung
                                                    req.setInvoluntary(admissionLaw.getId());
                                                    //Entlassungsgrund12
                                                    req.setDiscargeReason12(dischargeReason12 == null ? /* DischargeReasonEn.dr01.getIdInt() */ 0 : dischargeReason12.getIdInt());
                                                    //Entlassungsgrund3
                                                    req.setDiscargeReason3(dischargeReason3 == null ? /* DischargeReason2En.dr201.getIdInt() */ 0 : dischargeReason3.getIdInt());
                                                    req.setCity(city == null ? "" : city.toUpperCase());
                                                    req.setZipCode(zipCode == null ? "" : zipCode);
                                                    req.setInsuranceState(insStateEn == null ? 0 : insStateEn.getIdInt());
                                                    req.setLosMdAlteration(losSMdSim == null?0:losSMdSim);
                                                    // String and Numeric values from KIS-Inteface
                                                    req.setKisInterfaceValues(1, string01, numeric01);
                                                    req.setKisInterfaceValues(2, string02, numeric02);
                                                    req.setKisInterfaceValues(3, string03, numeric03);
                                                    req.setKisInterfaceValues(4, string04, numeric04);
                                                    req.setKisInterfaceValues(5, string05, numeric05);
                                                    req.setKisInterfaceValues(6, string06, numeric06);
                                                    req.setKisInterfaceValues(7, string07, numeric07);
                                                    req.setKisInterfaceValues(8, string08, numeric08);
                                                    req.setKisInterfaceValues(9, string09, numeric09);
                                                    req.setKisInterfaceValues(10, string10, numeric10);
                                                    // Labor    
                                                    req.setDoLabor(useLabor);
                                                    if (!labEmpty) {
                                                        while (true) {
                                                            if (rsLab.isAfterLast()) {
                                                                break;
                                                            }
                                                            if (tmpId != rsLab.getLong(1)) {
                                                                break;
                                                            }

                                                            // new TransferLabor(lab.getLabValue(), lab.getText(), lab.getUnit(), lab.getDescription(), lab.getLabAnalysisDate())
                                                            final double labValue = rsLab.getDouble(2);
                                                            final String labText = rsLab.getString(3);
                                                            final String labUnit = rsLab.getString(4);
                                                            final String labDescr = rsLab.getString(5);
                                                            final Date labAnalDate = rsLab.getDate(6);
                                                            req.addLabor(new TransferLabor(labValue, labText, labUnit, labDescr, labAnalDate));
                                                            if (!rsLab.next()) {
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    // Entgelte

                                                    if (!feeEmpty) {
                                                        while (true) {
                                                            if (rsFee.isAfterLast()) {
                                                                break;
                                                            }
                                                            if (tmpId != rsFee.getLong(1)) {
                                                                break;
                                                            }
                                                            //                                                if(!rsFee.next()){
                                                            //                                                    break;
                                                            //                                                }
                                                            final String feeKey = rsFee.getString(2); //FEEC_FEEKEY
                                                            final double feeValue = rsFee.getDouble(3); // FEEC_VALUE
                                                            final Date feeFrom = rsFee.getDate(4);//FEEC_FROM
                                                            final Date feeTo = rsFee.getDate(5); //FEEC_TO
                                                            final short feeCount = rsFee.getShort(6);//FEEC_COUNT
                                                            final int unbilledDays = rsFee.getInt(7);// FEEC_UNBILLED_DAYS
                                                            //String feeType, double value, Date calcFrom, Date calcTo, int count, int daysNotConsidered
                                                            req.addFeeRecord(feeKey, feeValue, feeFrom, feeTo, feeCount, unbilledDays);
                                                            if (!rsFee.next()) {
                                                                break;
                                                            }
                                                        }
                                                    }

                                                    //                                        req.addLabor(new TransferLabor(lab.getLabValue(), lab.getText(), lab.getUnit(), lab.getDescription(), lab.getLabAnalysisDate()));
                                                    if (!depEmpty) {
                                                        while (true) { //rsOps.next() && hosIdent.equals(rsOps.getString("CS_HOSPITAL_IDENT")) && caseNumber.equals(rsOps.getString("CS_CASE_NUMBER"))) {
                                                            if (rsDep.isAfterLast()) {
                                                                break;
                                                            }
                                                            if (tmpId != rsDep.getLong(1)) {
                                                                break;
                                                            }

                                                            final long departmentId = rsDep.getLong(2); //T_CASE_DEPARTMENT_ID
                                                            final String shortName = rsDep.getString(3); //DEP_SHORT_NAME
                                                            final Date depAdmissionDate = rsDep.getTimestamp(4); //DEPC_ADM_DATE
                                                            final Date depDischargeDate = rsDep.getTimestamp(5); //DEPC_DIS_DATE
                                                            final boolean isAdmissionFl = rsDep.getBoolean(6); //DEPC_IS_ADMISSION_FL
                                                            final boolean isDischargeFl = rsDep.getBoolean(7); //DEPC_IS_DISCHARGE_FL
                                                            final boolean isTreatingFl = rsDep.getBoolean(8); //DEPC_IS_TREATING_FL

                                                            TransferDepartment tDep = new TransferDepartment(
                                                                    shortName,
                                                                    depDischargeDate,
                                                                    depAdmissionDate,
                                                                    isAdmissionFl,
                                                                    isDischargeFl,
                                                                    isTreatingFl
                                                            );
                                                            req.addDepartment(tDep);

/// WARD                                                         
                                                            if (!wardEmpty) {
                                                                while (true) {
                                                                    if (rsWard.isAfterLast()) {
                                                                        break;
                                                                    }
                                                                    if (tmpId != rsWard.getLong(1)) {
                                                                        break;
                                                                    }
                                                                    if (departmentId != rsWard.getLong(2)) {
                                                                        break;
                                                                    }
// Z.z. nur Ward.Ident
                                                                    final String wardIdent = rsWard.getString(4);
                                                                    if (wardIdent != null) {

                                                                        tDep.addWard(wardIdent);
                                                                    }
                                                                    if (!rsWard.next()) {
                                                                        break;
                                                                    }
                                                                }
                                                            }

                                                            if (!icdEmpty) {
                                                                while (true) {
                                                                    if (rsIcd.isAfterLast()) {
                                                                        break;
                                                                    }
                                                                    if (tmpId != rsIcd.getLong(1)) {
                                                                        break;
                                                                    }
                                                                    if (departmentId != rsIcd.getLong(2)) {
                                                                        break;
                                                                    }

                                                                    final boolean toGroupFl = rsIcd.getBoolean(3); //TO_GROUP_FL
                                                                    final IcdcRefTypeEn icdRef = IcdcRefTypeEn.findById(rsIcd.getString(9)); //ICD_REFERENCE_EN

                                                                    final long icdIdTmp = rsIcd.getLong(4); //T_CASE_ICD_ID
                                                                    final IcdcTypeEn icdTypeTmp = IcdcTypeEn.findByName(rsIcd.getString(5)); //ICDC_TYPE_EN
                                                                    final boolean mainDiagFlTmp = rsIcd.getBoolean(6); //MAIN_DIAG_CASE_FL
                                                                    final String icdCodeTmp = rsIcd.getString(7); //ICDC_CODE
                                                                    final LocalisationEn icdLocTmp = LocalisationEn.findByName(rsIcd.getString(8)); //ICDC_LOC_EN

                                                                    final long icdRefIdTmp = rsIcd.getLong(10); //REF_T_CASE_ICD_ID
                                                                    final IcdcTypeEn icdRefTypeTmp = IcdcTypeEn.findById(rsIcd.getString(11)); //REF_ICDC_TYPE_EN
                                                                    final boolean mainDiagFlRefTmp = rsIcd.getBoolean(12); //MAIN_DIAG_CASE_FL_REF wird nicht benutzt
                                                                    final String icdRefCodeTmp = rsIcd.getString(13); //ICDC_CODE_REF
                                                                    final LocalisationEn icdRefLocTmp = LocalisationEn.findByName(rsIcd.getString(14)); //ICDC_LOC_EN_REF

                                                                    //switch values
                                                                    //                                                        final long icdId = (icdRefCodeTmp == null || icdRefCodeTmp.isEmpty()) ? icdIdTmp : icdRefIdTmp;
                                                                    //                                                       final IcdcTypeEn icdType = (icdRefCodeTmp == null || icdRefCodeTmp.isEmpty()) ? icdTypeTmp : icdRefTypeTmp;
                                                                    //                                                        final boolean mainDiagFl = (icdRefCodeTmp == null || icdRefCodeTmp.isEmpty()) ? mainDiagFlTmp : mainDiagFlRefTmp;
                                                                    //                                                        final String icdCode = (icdRefCodeTmp == null || icdRefCodeTmp.isEmpty()) ? icdCodeTmp : icdRefCodeTmp;
                                                                    //                                                        final LocalisationEn icdLoc = (icdRefCodeTmp == null || icdRefCodeTmp.isEmpty()) ? icdLocTmp : icdRefLocTmp;
                                                                    //                                            final long icdRefId = (icdRefCodeTmp == null || icdRefCodeTmp.isEmpty()) ? 0L : icdIdTmp;
                                                                    //                                                        final IcdcTypeEn icdRefType = (icdRefCodeTmp == null || icdRefCodeTmp.isEmpty()) ? null : icdTypeTmp;
                                                                    //                                            final boolean mainRefDiagFl = (icdRefCodeTmp == null || icdRefCodeTmp.isEmpty()) ? false : mainDiagFlTmp;
                                                                    //                                                       final String icdRefCode = (icdRefCodeTmp == null || icdRefCodeTmp.isEmpty()) ? "" : icdCodeTmp;
                                                                    //                                            final String icdRefLoc = (icdRefCodeTmp == null || icdRefCodeTmp.isEmpty()) ? null : icdLocTmp;
                                                                    if (!toGroupFl) {
                                                                        //AWi 07.04.2016, in Db TCaseIcd have no default Type, it returns null -> grouper explodes
                                                                        if (icdTypeTmp != null && IcdcTypeEn.Aufnahme.equals(icdTypeTmp)) {
                                                                            // Aufnahmediagnose wird an GRouper übergeben wegen dem Regelkriterium
                                                                            req.addAdmissionDiagnose(icdIdTmp, icdCodeTmp,
                                                                                    icdTypeTmp == null ? 0 : icdTypeTmp.getId(),
                                                                                    icdLocTmp == null ? 0 : icdLocTmp.ordinal());
                                                                        }
                                                                        //skip this icd
                                                                        if (!rsIcd.next()) {
                                                                            break;
                                                                        }
                                                                        continue;
                                                                    }
                                                                    TransferIcd tIcd = new TransferIcd(icdIdTmp,
                                                                            icdCodeTmp,
                                                                            icdRef == null ? 0 : icdRef.getId(),
                                                                            icdLocTmp == null ? 0 : icdLocTmp.ordinal(),
                                                                            mainDiagFlTmp,
                                                                            icdTypeTmp == null ? false : IcdcTypeEn.Aufnahme.equals(icdTypeTmp));
                                                                    if (mainDiagFlTmp) {
                                                                        req.setPrincipalIcd(tIcd);
                                                                    }
                                                                    tDep.addIcd(tIcd);

                                                                    req.addIcd(tIcd);
                                                                    if (icdRefCodeTmp != null && !icdRefCodeTmp.isEmpty() && icdRef != null && (IcdcRefTypeEn.Kreuz.equals(icdRef) || IcdcRefTypeEn.Zusatz.equals(icdRef))) {
                                                                        tIcd.setPrimIcd(icdRefCodeTmp);
                                                                    }
                                                                    if (!rsIcd.next()) {
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            if (!opsEmpty) {
                                                                while (true) {
                                                                    if (rsOps.isAfterLast()) {
                                                                        break;
                                                                    }
                                                                    if (tmpId != rsOps.getLong(1)) {
                                                                        break;
                                                                    }
                                                                    if (departmentId != rsOps.getLong(2)) {
                                                                        break;
                                                                    }

                                                                    final long opsId = rsOps.getLong(3); //T_CASE_OPS_ID
                                                                    final String opsCode = rsOps.getString(4); //OPSC_CODE
                                                                    final LocalisationEn opsLoc = LocalisationEn.findByName(rsOps.getString(5)); //OPSC_LOC_EN
                                                                    final Date opsDateTmp = rsOps.getTimestamp(6); //OPSC_DATUM

                                                                    Date opsDate = opsDateTmp;
                                                                    // do not correct ops - date, use as  coded
//                                                                    if (CaseTypeEn.PEPP.equals(caseType)) {
//                                                                        if (opsDateTmp == null || opsDateTmp.before(tDep.getAdm())) {
//                                                                            opsDate = tDep.getAdm();
//                                                                        } else if (opsDateTmp != null) {
//                                                                            if (tDep.getTransferDate() != null && opsDateTmp.after(tDep.getTransferDate())) {
//                                                                                opsDate = tDep.getTransferDate();
//                                                                            }
//                                                                        }
//                                                                    }

                                                                    TransferOps tOps = new TransferOps(opsId, opsCode,
                                                                            opsLoc == null ? 0 : opsLoc.getId(), opsDate);
                                                                    tDep.addOps(tOps);
                                                                    req.addOps(tOps);
                                                                    if (!rsOps.next()) {
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            if (!rsDep.next()) {
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    //                                        if (!feeEmpty) {
                                                    //                                            while (true) { //rsOps.next() && hosIdent.equals(rsOps.getString("CS_HOSPITAL_IDENT")) && caseNumber.equals(rsOps.getString("CS_CASE_NUMBER"))) {
                                                    ////                                        if (rsFee.getRow() == 0) {
                                                    ////                                            rsFee.next();
                                                    ////                                        }
                                                    //                                                if (rsFee.isAfterLast()) {
                                                    //                                                    break;
                                                    //                                                }
                                                    //                                                if (tmpId != rsFee.getLong(1)) {
                                                    //                                                    break;
                                                    //                                                }

                                                    //                            if (caseDetailsNo % 1000 == 0 || caseDetailsNo == caseDetailsCount.get()) {
                                                    //                                LOG.log(Level.INFO, "Created request for case details " + caseDetailsNo + "/" + caseDetailsCount);
                                                    //                            }
                                                    try {
                                                        //                                    LOG.log(Level.INFO, "Put request on queue");
                                                        while (pTargetQueue.remainingCapacity() <= 0) {
                                                            Thread.sleep(500L);
                                                            if (checkStopped()) {
                                                                return;
                                                            }
                                                        }
                                                        if(req.getIsLocal()){
                                                            trPatientLocal = addPatientCase(pTargetQueue, trPatientLocal, tmpPatIdLocal, req);
                                                        }else{
                                                            trPatientKIS = addPatientCase(pTargetQueue, trPatientKIS, tmpPatIdKIS, req);
                                                        }
//                                                        if(tmpPatientId == trPatientLocal.getPatientId() ){
//                                                            trPatientLocal.addHistoryCase(req);
//                                                        }
//                                                        
//                                                        else{
//                                                            pTargetQueue.put(trPatientLocal);
//                                                            trPatientLocal = new TransferPatient(pParameter.getModelId());
//                                                            trPatientLocal.setPatientId(tmpPatientId);
//                                                            trPatientLocal.addHistoryCase(req);
//                                                        }
                                                        timer.addAndGet(System.nanoTime() - timeStart);
                                                        
                                                    } catch (InterruptedException ex) {
                                                        LOG.log(Level.SEVERE, "Blocking queue is probably full and writer thread died", ex);
                                                        failureCb.execute("error occured in GrouperRequestLoader", ex);
                                                        stopSignal.set(true);
                                                        requestLoaderStopped.set(true);
                                                        stoppedCb.execute();
                                                        Thread.currentThread().interrupt();
                                                    }
                                                }

                                                try {
                                                    LOG.log(Level.INFO, "add last case from queue");
                                                    while (pTargetQueue.remainingCapacity() <= 0) {
                                                        Thread.sleep(500L);
                                                        if (checkStopped()) {
                                                            return;
                                                        }
                                                    }
                                                    
                                                    if(req != null){ // req has to be not null for the next actions
                                                        if(req.getIsLocal()){
                                                            trPatientLocal = addPatientCase(pTargetQueue, trPatientLocal, tmpPatIdLocal, req);

                                                        }else{
                                                           trPatientKIS  = addPatientCase(pTargetQueue, trPatientKIS, tmpPatIdKIS, req);

                                                        }
                                                        if(!trPatientLocal.getHistoryCases().isEmpty() ){
                                                            pTargetQueue.put(trPatientLocal);
                                                        }
                                                        if(!trPatientKIS.getHistoryCases().isEmpty()){
                                                            pTargetQueue.put(trPatientKIS);
                                                        }
                                                    }

                                                }catch(InterruptedException ex){
                                                        LOG.log(Level.SEVERE, "Blocking queue is probably full and writer thread died", ex);
                                                        failureCb.execute("error occured in GrouperRequestLoader", ex);
                                                        stopSignal.set(true);
                                                        requestLoaderStopped.set(true);
                                                        stoppedCb.execute();
                                                        Thread.currentThread().interrupt();

                                                }
                                                requestLoaderFinishedCb.execute();
                                            } catch (SQLException ex) {
                                                LOG.log(Level.SEVERE, "Blocking queue is probably full and writer thread died", ex);
                                                failureCb.execute("error occured in GrouperRequestLoader", ex);
                                                stopSignal.set(true);
                                                requestLoaderStopped.set(true);
                                                stoppedCb.execute();
                                            }
                                        });
                                    });
                                });
                            });
                        });
                    });
                });
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, "Error in sql statement", ex);
                failureCb.execute("error occured in GrouperRequestLoader", ex);
                stopSignal.set(true);
                requestLoaderStopped.set(true);
                stoppedCb.execute();
            } finally {
                dropTable(stmt, tmpTable);
                dropSequence(stmt, tmpSeq);
                dropTable(stmt, caseTable);
                dropTable(stmt, icdTable);
                dropTable(stmt, opsTable);
                dropTable(stmt, feeTable);
                dropTable(stmt, depTable);
                dropTable(stmt, wardTable);
                dropTable(stmt, labTable);
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error in sql statement", ex);
            failureCb.execute("error occured in GrouperRequestLoader", ex);
            stopSignal.set(true);
            requestLoaderStopped.set(true);
            stoppedCb.execute();
        } finally {
            requestLoaderStopped.set(true);
        }
    }
    
    private TransferPatient addPatientCase(BlockingQueue<TransferPatient> pTargetQueue, TransferPatient trPatient, long tmpPatientId, TransferCase trCase) throws InterruptedException{

        if(trPatient.getPatientId() < 0 ){
            trPatient.setPatientId(tmpPatientId);
        }
        if(tmpPatientId == trPatient.getPatientId()){
           trPatient.addHistoryCase(trCase);
        }else{
           pTargetQueue.put(trPatient);
           trPatient = new TransferPatient(trPatient.getGrouperModelId());
           trPatient.setPatientId(tmpPatientId);
           trPatient.addHistoryCase(trCase);
        }
       return trPatient;
    }

    private static Date addDays(final Date pDate, final int pDays) {
        if (pDate == null) {
            return pDate;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(pDate);
        cal.add(Calendar.DATE, pDays);
        return cal.getTime();
    }

    private static <T> Set<T> toFilteredSet(final Collection<T> pCollection) {
        if (pCollection == null || pCollection.isEmpty()) {
            return new LinkedHashSet<>();
        }
        final List<T> list = new ArrayList<>();
        for (final T value : new ArrayList<>(pCollection)) {
            if (value == null) {
                continue;
            }
            list.add(value);
        }
        final Set<T> result = new LinkedHashSet<>(list);
        return result;
        //String[] tmp = new String[list.size()];
        //list.toArray(tmp);
        //return tmp;
    }


    private static int dropOldTables(final Statement pStmt, final boolean pIsOracle) throws SQLException {
        //ORA: SELECT table_name FROM user_tables where table_name like 'IMEX_%' and table_name not like '%_TMP';
        String query;
        final String[] tables = new String[]{
            "'GRP_EXPORT_%'",
            "'GRP_JOB_%'",
            "'T_GROUPING_RES_%'",
            "'T_CASE_ICD_GRO_%'",
            "'T_CASE_OPS_GRO_%'",
            "'T_CASE_SUPPL_F_%'",
            "'T_CHECK_RESULT_%'",
            "'T_ROLE_2_CHECK_%'",
            "'T_ROLE_2_RESUL_%'",
            "'T_CASE_PEPP_GR_%'",
            "'T_CASE_DET_LOS_%'",};
        if (pIsOracle) {
            query = "SELECT table_name name FROM user_tables where table_name like " + String.join(" or table_name like ", tables);
        } else {
            query = "SELECT TABLE_NAME name FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME LIKE " + String.join(" OR TABLE_NAME LIKE ", tables);
        }

        int deletedTables = 0;
        try ( ResultSet rs = pStmt.executeQuery(query)) {
            List<String> tableNames = new ArrayList<>();
            final Pattern pattern = Pattern.compile("^T\\_.*(\\d{13})(\\_.*)?$");
            while (rs.next()) {
                String tblName = rs.getString("name");
                tblName = (tblName == null) ? "" : tblName.trim().toUpperCase();
                if (!tblName.isEmpty()) {
                    tableNames.add(tblName);
                }
            }
            //final Calendar today = Calendar.getInstance();
            //today.setTimeInMillis(System.currentTimeMillis());
            final long now = System.currentTimeMillis();
            for (String tableName : tableNames) {
                //pQry.execute("DROP VIEW " + tableName);
                Matcher m = pattern.matcher(tableName);
                while (m.find()) {
                    String number = m.group(1);
                    long executionId = 0;
                    try {
                        executionId = Long.valueOf(number);
                    } catch (NumberFormatException ex) {
                        LOG.log(Level.FINEST, "This table has an invalid execution id: " + tableName, ex);
                    }
                    if (executionId > 0) {
                        //Calendar cal = Calendar.getInstance();
                        //cal.setTimeInMillis(executionId);
                        //long diff = today.getTime() - cal.getTime();
                        long diff = now - executionId;
                        long hoursDiff = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
                        if (hoursDiff > 24) {
                            if (dropTable(pStmt, tableName)) {
                                deletedTables++;
                            }
                            dropSequence(pStmt, tableName + "_SQ");
                        }
                    }
                }
            }
            LOG.log(Level.INFO, "Deleted " + deletedTables + " old temporary tables");
        }
        return deletedTables;
    }

//    private static Statement createStatement(final Connection pConn, final int pFetchSize) throws SQLException {
//        Statement stmt = pConn.createStatement();
//        stmt.setFetchSize(pFetchSize);
//        return stmt;
//    }
    private static String formatCaseType(final String pValue) {
        if (pValue == null) {
            return "DRG";
        }
        final String val = pValue.trim();
        if (val.equalsIgnoreCase("PSY")) {
            return "PEPP";
        }
        return val;
    }
    private Integer getNull(final ResultSet pRs, int pIndex) throws SQLException {
        int val = pRs.getInt(pIndex);
        if (val == 0 && pRs.wasNull()) {
            return null;
        }
        return val;
    }

    private static Date getMinAdmissionDateFrom() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.YEAR, GrouperConstant.GROUPER_MIN_YEAR);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private String getCreateJobIdsTableQry(boolean isOracle, String tmpTableName, String tmpSeqName, BatchGroupParameter pParameter) {
        String qryString = "";
        if(isOracle){
            qryString = "CREATE TABLE " + tmpTableName + " AS SELECT " + nextSqVal(isOracle, tmpSeqName) + " ID,"

                + " T_CASE_ID, T_CASE_DETAILS_ID, T_PATIENT_ID, LOCAL_FL, CS_HOSPITAL_IDENT, CS_CASE_NUMBER, ADMISSION_DATE " +
                   "FROM ( select CS.ID T_CASE_ID, CSD.ID T_CASE_DETAILS_ID, CS.T_PATIENT_ID, CSD.LOCAL_FL, CS.CS_HOSPITAL_IDENT, CS.CS_CASE_NUMBER, csd.ADMISSION_DATE " +
                   getFromWhereQuery(pParameter, isOracle) + 
                   "order by cs.T_PATIENT_ID, CSD.ADMISSION_DATE)";
        }else{
            qryString = "SELECT "+ nextSqVal(isOracle, tmpSeqName)+
             " OVER (ORDER BY cs.T_PATIENT_ID, CSD.ADMISSION_DATE) as ID, "
             + "CS.ID T_CASE_ID, "
                     + "CSD.ID T_CASE_DETAILS_ID, "
                     + "CS.T_PATIENT_ID, "
                     + "CSD.LOCAL_FL, "
                     + "CS.CS_HOSPITAL_IDENT, "
                     + "CS.CS_CASE_NUMBER, "
                     + "csd.ADMISSION_DATE INTO " + tmpTableName +  getFromWhereQuery(pParameter, isOracle) ;

        }
        return qryString;
    }

}
