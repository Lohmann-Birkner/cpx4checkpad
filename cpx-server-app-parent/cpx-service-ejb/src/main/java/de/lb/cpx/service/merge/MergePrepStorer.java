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
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.service.grouper.PrepStorer;
import de.lb.cpx.service.helper.AbstractPrepStorer;
import de.lb.cpx.service.helper.ProgressCallback;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.Dependent;

/**
 *
 * @author gerschmann
 */
@Singleton
@Dependent
@TransactionManagement(TransactionManagementType.BEAN)
public class MergePrepStorer extends AbstractPrepStorer{
    public static final String TMP_CASE_MERGE_MAPPING = "TMP_CASE_MERGE_MAPPING";
    public static final String IX_TMP_CASE_MERGE_MAPPING = "IX_TMP_CASE_MERGE_MAPPING";

    private static final Logger LOG = Logger.getLogger(MergePrepStorer.class.getName());
    private String  tmpCaseMergeMappingTable; 
    private String tmpCaseMergeMappingIdx;
    private PreparedStatement pstmtCaseMergeMapping;
    private final Set<Long> mergeResIdStorage = new HashSet<>();
    public static final int SEQUENCE_SIZE_GRPRES = 30; //AGe: got from PrepSorer: Each Sequence has a range of 50 ids, so it's 30 * 50 = 1500 ids    
    private final Set<Long> mergeResIdentStorage = new HashSet<>();


    public MergePrepStorer() {
        super();
    }

    public MergePrepStorer(final Connection pConn) throws SQLException {
        super(pConn);
    }

    @Override
    public void createPStatements() throws SQLException {
        if (!start.get()) {
            return;
        }
        start.set(false);
        connection.setAutoCommit(false);
        pstmtCaseMergeMapping = createPstmt("INSERT INTO  /*+ PARALLEL(" + tmpCaseMergeMappingTable + " 5)*/ " + tmpCaseMergeMappingTable + " ("
            + "GRPRES_TYPE_EN,"
            + "MERGE_MEMBER_CASE_ID,"
            + "T_GROUPING_RESULTS_ID,"
            + "MRG_MERGE_IDENT,"
            + "MRG_CONDITION_1,"
            + "MRG_CONDITION_2,"
            + "MRG_CONDITION_3,"
            + "MRG_CONDITION_4,"
            + "MRG_CONDITION_5,"
            + "MRG_CONDITION_6,"
            + "MRG_CONDITION_7,"
            + "MRG_CONDITION_8,"
            + "MRG_CONDITION_9,"
            + "MRG_CONDITION_10,"
            + "CREATION_DATE,"
            + "CREATION_USER,"
            + "MODIFICATION_DATE,"
            + "MODIFICATION_USER,"
             +"ID, "   
             + "VERSION "

            + ") VALUES ("
            + "?, " // 1 GRPRES_TYPE_EN
            + "?, " //2 "MERGE_MEMBER_CASE_ID,"
            + "?, " //3 "T_GROUPING_RESULTS_ID,"
            + "?, " //4 "MRG_MERGE_IDENT,"
            + "?, " //5 "MRG_CONDITION_1,"
            + "?, " //6 "MRG_CONDITION_2,"
            + "?, " //7 "MRG_CONDITION_3,"
            + "?, " //8 "MRG_CONDITION_4,"
            + "?, " //9 "MRG_CONDITION_5,"
            + "?, " //10 "MRG_CONDITION_6,"
            + "?, " //11 "MRG_CONDITION_7,"
            + "?, " //12 "MRG_CONDITION_8,"
            + "?, " //13 "MRG_CONDITION_9,"
            + "?, " //14 "MRG_CONDITION_10,"
            + "?, " //15 "CREATION_DATE,"
            + "?, " //16 "CREATION_USER,"
            + "?, " //17 "MODIFICATION_DATE,"
            + "?, " //18 "MODIFICATION_USER,"
            + "?, "//19 ID
            + "0 "//20  version

                +")"
                
        );
    }

    @Override
    protected void destroy() {
       if (start.get()) {
            return;
        }
        LOG.log(Level.INFO, "close prepared statements...");
        LOG.log(Level.INFO, "close pstmtInsertGroupingResult...");
        closePstmt(pstmtCaseMergeMapping);
        counterInsert.set(0);
        try {
           
            if(!connection.getAutoCommit()){
                 connection.commit();
            }
        } catch (SQLException ex) {
            Logger.getLogger(PrepStorer.class.getName()).log(Level.SEVERE, null, ex);
        }
        connection = null;
        start.set(true);
        LOG.log(Level.INFO, "close prepared statements...ready");
    }

    @Override
    protected void initialize() throws SQLException {
        tmpCaseMergeMappingTable = TMP_CASE_MERGE_MAPPING; 
        tmpCaseMergeMappingIdx = IX_TMP_CASE_MERGE_MAPPING;
          LOG.log(Level.INFO, "Temporary result in {0}", tmpCaseMergeMappingTable);
          final String parallel = isOracle ? " NOLOGGING PARALLEL 5" : "";
          String createQry =  (isOracle ? "CREATE TABLE " + tmpCaseMergeMappingTable + parallel + " AS " : "") + "SELECT T_CASE_MERGE_MAPPING.* "
                  + (isSqlsrv ? "INTO " + tmpCaseMergeMappingTable : "") + " FROM T_CASE_MERGE_MAPPING WHERE 1 = 2";
          LOG.log(Level.INFO, createQry);
           try (Statement stmt = connection.createStatement()) {
               stmt.executeUpdate(createQry);
           }  
    }

    @Override
    public void executeInsert() throws SQLException {
        waitingInserts = 0;
        if (STORE_GRPRESULTS) {
            LOG.log(Level.INFO, "executing batch...");
            pstmtCaseMergeMapping.executeBatch();
        }
    }

    @Override
    protected void rebuildIndexes() {
        final String[][] indexes = new String[][]{
            new String[]{"T_CASE_MERGE_MAPPING", "IDX_C_MERGE_MAP4GRPRES_ID"},
           new String[]{"T_CASE_MERGE_MAPPING", "IDX_C_MERGE_MAP4MERG_MEMB_C_ID"},
           new String[]{"T_CASE_MERGE_MAPPING", "IDX_C_MERGE_MAP4TCASE_ID"},
           new String[]{"T_CASE_MERGE_MAPPING", "IDX_CASE_MERGE_TYPE"}
        };
        rebuildIndexes(indexes);
    }

    @Override
    protected void dropTempTables() throws SQLException {
//         final String[] dropTables = new String[]{
//             tmpCaseMergeMappingTable
//         };
//         dropTempTables(dropTables);
    }
    
    public void insertMappingData(TransferMergePatient transferPatient, CaseTypeEn pType)throws SQLException {
        if(transferPatient == null || transferPatient.getCases2merge().isEmpty() || pType == null){
            return;
        }
       int counter = counterInsert.incrementAndGet();
        java.sql.Timestamp date = new java.sql.Timestamp(System.currentTimeMillis());
        Long userId = ClientManager.getCurrentCpxUserId();// in job is null

        if (STORE_GRPRESULTS) {
            List<TransferMergeCandidate> candidates = transferPatient.getCases2mergeSortedWithMergeId();
            int mergeId = -1;
            int tmpMergeId = -1;
            for(TransferMergeCandidate candidate: candidates){
                RmcWiederaufnahmeIF wa = candidate.getRmcCase();
                if(wa == null){
                    return;
                }
                if(wa.getMergeId() != tmpMergeId){
                   mergeId = getMergeIdentId(); 
                   tmpMergeId = wa.getMergeId();
                }
                long grpResId = getMergeLineId();
                pstmtCaseMergeMapping.setString(1, pType.name());// 1 GRPRES_TYPE_EN
                pstmtCaseMergeMapping.setLong(2, candidate.getCaseId()); //2 "MERGE_MEMBER_CASE_ID,"
                pstmtCaseMergeMapping.setLong(3,candidate.getGrpResId()); //3 "T_GROUPING_RESULTS_ID,"
                pstmtCaseMergeMapping.setInt(4, mergeId);//4 "MRG_MERGE_IDENT,"
                pstmtCaseMergeMapping.setInt(5, wa.getEins()); //5 "MRG_CONDITION_1,"
                pstmtCaseMergeMapping.setInt(6, wa.getZwei()); //6 "MRG_CONDITION_2,"
                pstmtCaseMergeMapping.setInt(7, wa.getDrei());//7 "MRG_CONDITION_3,"
                pstmtCaseMergeMapping.setInt(8, wa.getVier()); //8 "MRG_CONDITION_4,"
                pstmtCaseMergeMapping.setInt(9, wa.getFuenf());//9 "MRG_CONDITION_5,"
                pstmtCaseMergeMapping.setInt(10, wa.getSechs()); //10 "MRG_CONDITION_6,"
                pstmtCaseMergeMapping.setInt(11, wa.getSieben()); //11 "MRG_CONDITION_7,"
                pstmtCaseMergeMapping.setInt(12, wa.getAcht());//12 "MRG_CONDITION_8,"
                pstmtCaseMergeMapping.setInt(13, wa.getNeun()); //13 "MRG_CONDITION_9,"
                pstmtCaseMergeMapping.setInt(14, wa.getZehn());//14 "MRG_CONDITION_10,"
                pstmtCaseMergeMapping.setTimestamp(15,date);//15 "CREATION_DATE,"
                pstmtCaseMergeMapping.setLong(16, userId == null?0L:userId);//16 "CREATION_USER,"
                pstmtCaseMergeMapping.setTimestamp(17, date); //17 "MODIFICATION_DATE,"
                pstmtCaseMergeMapping.setLong(18, userId == null?0L:userId); //18 "MODIFICATION_USER,"
                pstmtCaseMergeMapping.setLong(19, grpResId); // id
                //20  version
                pstmtCaseMergeMapping.addBatch();
                pstmtCaseMergeMapping.clearParameters();
            }

        }        
    }


    public long getMergeLineId() throws SQLException {
        return getId4Storage(mergeResIdStorage,"T_CASE_MERGE_MAPPING_SQ", SEQUENCE_SIZE_GRPRES);
    }
    
    public void distributeData(MergeResponseWriter pWriter, ProgressCallback distributionProgressCb, CaseTypeEn pType, GDRGModel pGrouperModel) throws SQLException {
       final int maxSteps = 6;
        final AtomicInteger step = new AtomicInteger(0);
        executionId = System.currentTimeMillis();
        try {
            distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Finalisiere die temporären Grouping-Ergebnisse...");
            if (pWriter.checkStopped()) {
                return;
            }
            LOG.log(Level.INFO, "finalize data to temporary grouping tables...");
            executeInsert();

            distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Erzeuge Indizes...");
            if (pWriter.checkStopped()) {
                return;
            }
            final String parallel = (isOracle ? "NOLOGGING PARALLEL 5" : "");
            String qry = "CREATE INDEX " + tmpCaseMergeMappingIdx + " ON " + tmpCaseMergeMappingTable + " (ID) " + parallel;
            try (Statement stmt = connection.createStatement()) {
                try {
                    stmt.execute(qry);
                } catch (SQLException ex) {
                    LOG.log(Level.SEVERE, "cannot create index: " + qry, ex);
                }
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
           if (pWriter.checkStopped()) {
                return;
            }

            if (!connection.getAutoCommit()) {
                connection.commit();
            }
            try (Statement stmt = connection.createStatement()) {
                try {
            //create index on GRP_TYPE_EN in T_CASE_MERGE_MAPPING
                    distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Lösche alte ergebnisse aus T_CASE_MERGE_MAPPING zu " + pType.name() + "...");
//                    qry = "CREATE INDEX IDX_CS_MRG_TYPE ON T_CASE_MERGE_MAPPING (GRPRES_TYPE_EN) " + parallel;
//                    LOG.log(Level.INFO, qry);
//                    stmt.execute(qry);
                 // delete from T_CASE_MERGE_MAPPING for pType
                    qry = "DELETE FROM  /*+ PARALLEL(T_CASE_SUPPL_FEE 5)*/ T_CASE_MERGE_MAPPING "
                            + "WHERE EXISTS("
                            + " SELECT 1 FROM  T_GROUPING_RESULTS WHERE T_CASE_MERGE_MAPPING.T_GROUPING_RESULTS_ID = T_GROUPING_RESULTS.ID"
                            
                            + " AND "
                            + (pGrouperModel.equals(GDRGModel.AUTOMATIC)?"T_GROUPING_RESULTS.GRPRES_IS_AUTO_FL = 1 )":
                            ("T_GROUPING_RESULTS.GRPRES_IS_AUTO_FL = 0 AND T_GROUPING_RESULTS.MODEL_ID_EN =  '" + pGrouperModel.name())
                            + "')")
                            + " AND T_CASE_MERGE_MAPPING.GRPRES_TYPE_EN = '" + pType.name() + "'"
                            + " AND T_CASE_MERGE_MAPPING.T_CASE_ID IS NULL "
                            ;
                    LOG.log(Level.INFO, qry);
                    int del = stmt.executeUpdate(qry);
                    LOG.log(Level.INFO, "There are {0} entries for {1} deleted", new Object[]{del, pType.name()});
                    distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Scheibe neue  ergebnisse aus T_CASE_MERGE_MAPPING zu " + pType.name() + "...");
     //insert data from tmpCaseMergeMappingTable into T_CASE_MEGE_MAPPING                    
                    qry = "INSERT INTO /*+ PARALLEL(T_GROUPING_RESULTS 5)*/ T_CASE_MERGE_MAPPING ("
                        + "GRPRES_TYPE_EN,"
                        + "MERGE_MEMBER_CASE_ID,"
                        + "T_GROUPING_RESULTS_ID,"
                        + "MRG_MERGE_IDENT,"
                        + "MRG_CONDITION_1,"
                        + "MRG_CONDITION_2,"
                        + "MRG_CONDITION_3,"
                        + "MRG_CONDITION_4,"
                        + "MRG_CONDITION_5,"
                        + "MRG_CONDITION_6,"
                        + "MRG_CONDITION_7,"
                        + "MRG_CONDITION_8,"
                        + "MRG_CONDITION_9,"
                        + "MRG_CONDITION_10,"
                        + "CREATION_DATE,"
                        + "CREATION_USER,"
                        + "MODIFICATION_DATE,"
                        + "MODIFICATION_USER,"
                        +"ID, "   
                        + "VERSION ) "
                        + " SELECT "    
                        + "GRPRES_TYPE_EN,"
                        + "MERGE_MEMBER_CASE_ID,"
                        + "T_GROUPING_RESULTS_ID,"
                        + "MRG_MERGE_IDENT,"
                        + "MRG_CONDITION_1,"
                        + "MRG_CONDITION_2,"
                        + "MRG_CONDITION_3,"
                        + "MRG_CONDITION_4,"
                        + "MRG_CONDITION_5,"
                        + "MRG_CONDITION_6,"
                        + "MRG_CONDITION_7,"
                        + "MRG_CONDITION_8,"
                        + "MRG_CONDITION_9,"
                        + "MRG_CONDITION_10,"
                        + "CREATION_DATE,"
                        + "CREATION_USER,"
                        + "MODIFICATION_DATE,"
                        + "MODIFICATION_USER,"
                        +"ID, "   
                        + "VERSION "
                        + "FROM " + tmpCaseMergeMappingTable;
                    LOG.log(Level.INFO, qry);
                    int inserted = stmt.executeUpdate(qry);
                    LOG.log(Level.INFO, "There are {0} entries for {1} inserted", new Object[]{inserted, pType.name()});
                 } catch (SQLException ex) {
                     LOG.log(Level.SEVERE, "cannot create index: " + qry, ex);
                 }
                if (pWriter.checkStopped()) {
                    return;
                }

                if (!connection.getAutoCommit()) {
                    connection.commit();
                }
            }

        } finally {

            distributionProgressCb.execute(executionId, maxSteps - 1, maxSteps, "Lösche die Tabellen für die temporären Grouping-Ergebnisse...");
            LOG.log(Level.INFO, "delete temporary grouping result tables...");
            dropTempTables();

            distributionProgressCb.execute(executionId, maxSteps, maxSteps, "Erneuere die Indizes...");
            LOG.log(Level.INFO, "rebuild indizes on grouping tables tables...");
            rebuildIndexes();
        }
    }

    private int getMergeIdentId() throws SQLException {
         return (int)getId4Storage(mergeResIdentStorage,"CASE_MERGE_IDENT_SEQ", 1);
    }

}
