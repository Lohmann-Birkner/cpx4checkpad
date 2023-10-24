/*
 * Copyright (c) 2021 Lohmann & Birkner.
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
 *    2021  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.merge;

import de.checkpoint.drg.GDRGModel;
import de.checkpoint.server.rmServer.caseManager.RmcWiederaufnahmeIF;
import de.lb.cpx.grouper.model.transfer.TransferMergeCandidate;
import de.lb.cpx.grouper.model.transfer.TransferMergePatient;
import de.lb.cpx.grouper.model.transfer.TransferPatient;
import de.lb.cpx.model.enums.AdmissionReasonEn;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.DrgPartitionEn;
import de.lb.cpx.model.enums.GrouperMdcOrSkEn;
import de.lb.cpx.service.helper.Callback;
import de.lb.cpx.service.helper.DbCallback;
import de.lb.cpx.service.helper.FailureCallback;
import de.lb.cpx.service.helper.ProgressCallback;
import de.lb.cpx.service.helper.RequestLoader;
import static de.lb.cpx.service.merge.MergePrepStorer.TMP_CASE_MERGE_MAPPING;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 *
 * @author gerschmann
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class MergeRequestLoader extends RequestLoader{

    private static final Logger LOG = Logger.getLogger(MergeRequestLoader.class.getName());


    public void start(
            final Connection pConn,
            final CaseTypeEn pGrpresType,
            final GDRGModel grouperModel,
            final boolean isOracle,
            final BlockingQueue<TransferMergePatient> pTargetQueue,
            final AtomicInteger patientCount,
//            final AtomicInteger caseCount,
            final AtomicBoolean stopSignal,
            final Callback stoppedCb,
            final AtomicBoolean requestLoaderStopped,
            final Callback requestLoaderFinishedCb,
            final ProgressCallback requestLoaderProgressCb,
//            final ProgressCallback requestLoaderProgress1,
            final FailureCallback failureCb,
            final AtomicLong timer,
            final AtomicLong mergeRequestLoaderTotalTime,
            final AtomicLong mergeResponseLoaderTotalTime) {

        this.stopSignal = stopSignal;
        this.requestLoaderStopped = requestLoaderStopped;
        this.stoppedCb = stoppedCb;

        mergeRequestLoaderTotalTime.set(System.currentTimeMillis());
        final long executionId = System.currentTimeMillis();
        final int fetchRows = 10000; //very relevant for performance!
        final String tmpTable = "MRG_JOB";// + executionId;
        final String tmpSeq = tmpTable + "_SQ";

        final int maxSteps = 7; 
        final AtomicInteger step = new AtomicInteger(0);
        final long startTime = System.currentTimeMillis();
        try ( Statement stmt = pConn.createStatement()) {

            try {
                requestLoaderProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Lösche alte Tabellen...");
                dropTable(stmt, tmpTable);
                dropSequence(stmt, tmpSeq);

                dropTable(stmt, TMP_CASE_MERGE_MAPPING);
                dropSequence(stmt, TMP_CASE_MERGE_MAPPING + "_SQ");

                if (checkStopped()) {
                    return;
                }

                requestLoaderProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Erzeuge die job - tabelle " + tmpTable + "...");
                

                // create temporary table for cases with grouping results 
                String qry = (isOracle ? "CREATE TABLE " + tmpTable + " NOLOGGING PARALLEL 5 AS " : "")
                        + "SELECT CS.T_PATIENT_ID, CS.ID T_CASE_ID, TD.ID T_CASE_DETAILS_ID, GR.ID T_GRPRES_ID, "
                        + "CS.CS_HOSPITAL_IDENT, CS.INSURANCE_IDENTIFIER, CS.CS_CASE_NUMBER, "
                        + "TD.ADMISSION_DATE, TD.DISCHARGE_DATE, TD.ADMISSION_REASON_12_EN, "
                        + "TD.LOS, "
                        + "GR.GRPRES_CODE, GR.GRPRES_GROUP, "
                        + "GR.DRGC_PARTITION_EN, GR.EXCEPTION_DRG_FL, GR.DRGC_HTP, "
                        + "GR.DAY_CARE_FL, GR.NEGOTIATED_FL   "
                        + (isOracle ? "" : " INTO " + tmpTable)
                        + " FROM T_CASE CS  "
                        + "INNER JOIN T_CASE_DETAILS TD ON TD.T_CASE_ID = CS.ID "
                        + "INNER JOIN T_CASE_DEPARTMENT DEP ON DEP.T_CASE_DETAILS_ID = TD.ID "
                        + "INNER JOIN T_CASE_ICD ICD ON ICD.T_CASE_DEPARTMENT_ID = DEP.ID "
                        + "INNER JOIN T_GROUPING_RESULTS GR ON GR.T_CASE_ICD_ID = ICD.ID "
                        + "WHERE CS.T_PATIENT_ID IN(SELECT T_PATIENT_ID FROM T_CASE WHERE CANCEL_FL = 0 AND CS_CASE_TYPE_EN = '" + pGrpresType.name() + "' "
                        + "GROUP BY T_PATIENT_ID HAVING COUNT(T_PATIENT_ID) > 1) "
                        + "AND TD.ACTUAL_FL = 1 AND TD.LOCAL_FL = 1 AND "
                        + (grouperModel.equals(GDRGModel.AUTOMATIC)?"GR.GRPRES_IS_AUTO_FL = 1 ":("GR.GRPRES_IS_AUTO_FL = 0 AND GR.MODEL_ID_EN =  '" + grouperModel.name() + "'"))
                        + " AND ICD.MAIN_DIAG_CASE_FL = 1 "
                        + "AND TD.DISCHARGE_DATE IS NOT NULL AND GR.GRPRES_CODE IS NOT NULL "
                        + "AND GR.GRPRES_TYPE_EN = '" + pGrpresType.name() + "' "
                        + " AND  CS.CANCEL_FL = 0 "
                        + "AND CS.CS_CASE_NUMBER NOT LIKE '%_m' " // merged case suffix  will be replaced with merge flag
                        + " AND CS.ID NOT IN (SELECT MERGE_MEMBER_CASE_ID FROM T_CASE_MERGE_MAPPING WHERE T_CASE_ID IS NOT NULL)"; // already merged
//                + "ORDER BY CS.T_PATIENT_ID, CS.CS_HOSPITAL_IDENT, TD.ADMISSION_DATE"; 
                LOG.log(Level.INFO, qry);
                stmt.execute(qry);

                if (checkStopped()) {
                    return;
                }

                
                requestLoaderProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Erzeuge die indizes...");

                stmt.execute("CREATE INDEX I_" + tmpTable + "_PAT ON " + tmpTable + " (T_PATIENT_ID)");
//             stmt.execute("CREATE INDEX I_" + tmpTable + "_CS ON " + tmpTable + " (T_CASE_ID)");
//             stmt.execute("CREATE INDEX I_" + tmpTable + "_TD ON " + tmpTable + " (T_DETAILS_ID)");
//             stmt.execute("CREATE INDEX I_" + tmpTable + "_GR ON " + tmpTable + " (T_GRPRES_ID)");

                if (checkStopped()) {
                    return;
                }

                requestLoaderProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Ermittle die Anzahl der zu analysierenden Patienten...");
                try ( ResultSet rs = stmt.executeQuery("SELECT COUNT(DISTINCT T_PATIENT_ID) CNT FROM " + tmpTable)) {
                   while (rs.next()) {
                       patientCount.set(rs.getInt("CNT"));
                   }
               }
                LOG.log(Level.INFO, "Found " + patientCount.toString() + " patients for check merging");

                requestLoaderProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Ermittle die Anzahl der zu analysierenden Fälle...");
//                try ( ResultSet rs = stmt.executeQuery("SELECT COUNT(DISTINCT T_CASE_ID) CNT FROM " + tmpTable)) {
//                   while (rs.next()) {
//                       caseCount.set(rs.getInt("CNT"));
//                   }
//               }
//                LOG.log(Level.INFO, "Found " + caseCount.toString() + " patients for check merging");
//
                requestLoaderProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Vorbereitung der Daten...");
                executeStatement(pConn, fetchRows, "SELECT * FROM " + tmpTable + " ORDER BY T_PATIENT_ID, CS_HOSPITAL_IDENT ", (rsCs) -> {
                    requestLoaderProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Beginne mit der Datenübergabe...");
                    if (checkStopped()) {
                        return;
                    }

                   TransferMergePatient currentPatient = new TransferMergePatient(); 
                   TransferMergeCandidate newCandidate = null;
                   int patientCntTemp = 0;
                   long patientId  = -1;
                    String hospIdent = null;
                   try{
                    while (rsCs.next()) {
                        if (checkStopped()) {
                            return;
                        }
                        final long timeStart = System.nanoTime();
                        patientId = rsCs.getLong(1);//CS.T_PATIENT_ID, 
                        final long caseId = rsCs.getLong(2);//CS.ID T_CASE_ID, 
                        final long caseDetailsId = rsCs.getLong(3);//TD.ID T_CASE_DETAILS_ID, 
                        final long grpResId = rsCs.getLong(4);//GR.ID T_GRPRES_ID, "
                        hospIdent = rsCs.getString(5);//"CS.CS_HOSPITAL_IDENT, 
                        final String insuranceIdent = rsCs.getString(6);//CS.INSURANCE_IDENTIFIER, "
                        final String caseNr = rsCs.getString(7);//CS_CASE_NUMBER
                        final Date admDate = rsCs.getDate(8);// "TD.ADMISSION_DATE, 
                        final Date disDate = rsCs.getDate(9);//TD.DISCHARGE_DATE, 
                        AdmissionReasonEn admissionReason12 = AdmissionReasonEn.findById(rsCs.getString(10));  //TD.ADMISSION_REASON_12_EN, "
                        final int los = rsCs.getInt(11);//"TD.LOS, "
                        final String grpResCode = rsCs.getString(12);//"GR.GRPRES_CODE, 
                        final GrouperMdcOrSkEn grpResGroup = rsCs.getString(13) == null?null:GrouperMdcOrSkEn.findByName(rsCs.getString(13));//GR.GRPRES_GROUP, "
                        final DrgPartitionEn drgPartition = rsCs.getString(14) == null?null:DrgPartitionEn.getValue2name(rsCs.getString(14));//"GR.DRGC_PARTITION_EN, 
                        final boolean isDrgException = rsCs.getBoolean(15);//GR.EXCEPTION_DRG_FL, 
                        final int drgHtp = rsCs.getInt(16);//GR.DRGC_HTP, "
                        final boolean isDayCare = rsCs.getBoolean(17);//"GR.DAY_CARE_FL, 
                        final boolean isNegotiated = rsCs.getBoolean(18);//GR.NEGOTIATED_FL   
                    
                        newCandidate = new TransferMergeCandidate();
                        newCandidate.setCaseId(caseId);
                        newCandidate.setGrpResId(grpResId);
                        newCandidate.setGrpresType(pGrpresType);
                        RmcWiederaufnahmeIF ret = newCandidate.createRmcCase(hospIdent, insuranceIdent, caseNr, admDate, disDate,
                        admissionReason12, los, grpResCode, grpResGroup, drgPartition, 
                        isDrgException, drgHtp, isDayCare, isNegotiated);
  
                        if(ret == null){
                            continue;
                        }
                        try{
                            //                                    LOG.log(Level.INFO, "Put request on queue");
                            while (pTargetQueue.remainingCapacity() <= 0) {
                                Thread.sleep(500L);
                                if (checkStopped()) {
                                    return;
                                }
                            }
//                            LOG.log(Level.INFO, "add patientid: {0}, caseID: {1}", new Object[]{String.valueOf(patientId), String.valueOf(caseId)});
                           
                            long tmpPatientId = currentPatient.getPatientId();
                            currentPatient = addPatientCase(currentPatient, patientId, hospIdent, newCandidate, pTargetQueue);
                            if(tmpPatientId != currentPatient.getPatientId() ){ 
                                if(patientCntTemp % 1000 == 0){
//                                    requestLoaderProgressCb.execute(executionId, step.get(), maxSteps, "Patienten geprüft: "  + patientCntTemp
//                                        + " / " + patientCount);
//                                    requestLoaderProgress1.execute(startTime, patientCntTemp, patientCount.get(), "Patienten geprüft: "  + patientCntTemp
//                                        + " / " + patientCount);
                                    requestLoaderProgressCb.execute(executionId, step.get(), maxSteps, patientCntTemp, patientCount.get(),
                                            "Patienten geprüft: "  + patientCntTemp
                                        + " / " + patientCount);
                               }
                                
                                 patientCntTemp++;
                            }
                            
                           
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
                         long tmpPatientId = currentPatient.getPatientId();
                        if(newCandidate != null && patientId > 0){ // req has to be not null for the next actions
                            currentPatient = addPatientCase(currentPatient, patientId, hospIdent, newCandidate, pTargetQueue);
                        }

                        // last candidate was appended to the currentPaitent, patient is the same, it was not put in queue
                         if(tmpPatientId == patientId){
                              pTargetQueue.put(currentPatient);
                         }

                        }catch(InterruptedException ex){
                                LOG.log(Level.SEVERE, "Blocking queue is probably full and writer thread died", ex);
                                failureCb.execute("error occured in GrouperRequestLoader", ex);
                                stopSignal.set(true);
                                requestLoaderStopped.set(true);
                                stoppedCb.execute();
                                Thread.currentThread().interrupt();

                        }

                        requestLoaderProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Datenübergabe beendet...");
                        if (checkStopped()) {
                            return;
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
                
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, "Error in sql statement", ex);
                failureCb.execute("error occured in GrouperRequestLoader", ex);
                stopSignal.set(true);
                requestLoaderStopped.set(true);
                stoppedCb.execute();
            } finally {
                dropTable(stmt, tmpTable);
                dropSequence(stmt, tmpSeq);

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

    public void executeStatement(final Connection pConn, final int pFetchSize, final String pQuery, DbCallback pCallback) throws SQLException {
        try ( Statement stmt = pConn.createStatement()) {
            stmt.setFetchSize(pFetchSize);
            try ( ResultSet rs = stmt.executeQuery(pQuery)) {
                pCallback.call(rs);
            }
        }
    }

    private TransferMergePatient addPatientCase(TransferMergePatient currentPatient, long patientId,  String pHosIdent,
            TransferMergeCandidate newCandidate, BlockingQueue<TransferMergePatient> pTargetQueue) throws InterruptedException{
        if(currentPatient.getPatientId() <0){
           currentPatient.setPatientId(patientId);
           currentPatient.setHospitalIdent(pHosIdent);
        }
        if(currentPatient.getPatientId() == patientId && (pHosIdent == null && currentPatient.getHospitalIdent() == null
                || pHosIdent != null && pHosIdent.equals(currentPatient.getHospitalIdent()))){
            currentPatient.addCandidate(newCandidate);
        }else{
            pTargetQueue.put(currentPatient);
            currentPatient = new TransferMergePatient();
            currentPatient.setPatientId(patientId);
            currentPatient.setHospitalIdent(pHosIdent);
            currentPatient.addCandidate(newCandidate);
        }
        return currentPatient;
    }

}
