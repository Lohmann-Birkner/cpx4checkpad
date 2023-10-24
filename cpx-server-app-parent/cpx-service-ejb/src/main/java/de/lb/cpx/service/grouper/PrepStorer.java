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
package de.lb.cpx.service.grouper;

import de.lb.cpx.service.helper.ProgressCallback;
import de.lb.cpx.service.helper.AbstractPrepStorer;
import de.lb.cpx.grouper.model.transfer.BatchGroupParameter;
import de.lb.cpx.grouper.model.transfer.TransferCase;
import de.lb.cpx.grouper.model.transfer.TransferGroupResult;
import de.lb.cpx.grouper.model.transfer.TransferIcd;
import de.lb.cpx.grouper.model.transfer.TransferIcdResult;
import de.lb.cpx.grouper.model.transfer.TransferOpsResult;
import de.lb.cpx.grouper.model.transfer.TransferSupplementaryFee;
import de.lb.cpx.model.TBatchCheckResult;
import de.lb.cpx.model.TBatchResult;
import de.lb.cpx.model.TBatchResult2Role;
import de.lb.cpx.model.TCaseDrgCareGrades;
import de.lb.cpx.model.TCasePepp;
import de.lb.cpx.model.TCasePeppGrades;
import de.lb.cpx.model.TCheckResult;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.TRole2Check;
import de.lb.cpx.model.TRole2Result;
import de.lb.cpx.model.enums.OpsResTypeEn;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.str.utils.StrUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.Dependent;

/**
 *
 * @author niemeier
 */
@Singleton
@Dependent
@TransactionManagement(TransactionManagementType.BEAN) 
public class PrepStorer extends AbstractPrepStorer{

//    public static final int CHUNK_INSERT_SIZE = 1000;
//    public static final boolean STORE_GRPRESULTS = true;
    private static final Logger LOG = Logger.getLogger(PrepStorer.class.getName());
    public final Set<Long> grpResIdStorage = new HashSet<>();
    public final Set<Long> opsResIdStorage = new HashSet<>();
    public final Set<Long> opsCheckResultIdStorage = new HashSet<>();
    public static final int SEQUENCE_SIZE_GRPRES = 30; //Each Sequence has a range of 50 ids, so it's 30 * 50 = 1500 ids
    public static final int SEQUENCE_SIZE_OPS_GRP = SEQUENCE_SIZE_GRPRES * 3; //based on personal guesses and calculations. There are around 2.7 multiple entries in T_CASE_OPS_GROUPED than in T_GROUPING_RESULTS
    public static final int SEQUENCE_SIZE_CHECKRESULT = SEQUENCE_SIZE_GRPRES * 4; //based on personal guesses and calculations. There are around 3.7 multiple entries in T_CHECK_RESULT than in T_GROUPING_RESULTS;

    //@Inject @CpxConnection
//    private Connection connection;
//    public final boolean isSqlsrv;
//    public final boolean isOracle;

//    private final AtomicInteger counterInsert = new AtomicInteger(0);
//    private final AtomicBoolean start = new AtomicBoolean(true);
    private PreparedStatement pstmtInsertGroupingResult;
    private PreparedStatement pstmtInsertIcdGrouped;
    private PreparedStatement pstmtInsertOpsGrouped;
    private PreparedStatement pstmtInsertSupplFee;
    private PreparedStatement pstmtInsertCheckResult;
    private PreparedStatement pstmtInsertRole2Check;
    private PreparedStatement pstmtInsertRole2Result;
    private PreparedStatement pstmtInsertPeppGrades;
    private PreparedStatement pstmtInsertDrgCareGrades;
    private PreparedStatement pstmtUpdateCaseDetailsLos;
//    private long executionId;
    private String groupingResultsTable;
    private String caseIcdGroupedTable;
    private String caseOpsGroupedTable;
    private String caseSupplFeeTable;
    private String checkResultTable;
    private String role2ResultTable;
    private String role2CheckTable;
    private String casePeppGradesTable;
    private String caseDrgCareGradesTable;
    private String caseDetailsLosTable;
//    private int waitingInserts;
    private Long userId;
    private String groupingResultsToDeleteTable;

    public PrepStorer() {
        super();
//        isSqlsrv = false;
//        isOracle = false;
    }

    public PrepStorer(final Connection pConn) throws SQLException {
        super(pConn);
//        connection = pConn;
//        String connStr = AbstractDao.getConnectionUrl(pConn);
//        isOracle = AbstractDao.isOracle(connStr);
//        isSqlsrv = AbstractDao.isSqlSrv(connStr);
//        initialize();
//        open();
    }
//
//    public boolean executeInsert(final int pMinimumWaitingInserts) throws SQLException {
//        if (waitingInserts >= pMinimumWaitingInserts) {
//            executeInsert();
//            return true;
//        } else {
//            return false;
//        }
//    }

    @Override
    public void executeInsert() throws SQLException {
        waitingInserts = 0;
        if (STORE_GRPRESULTS) {
            LOG.log(Level.INFO, "executing batch...");
            pstmtInsertGroupingResult.executeBatch();
            pstmtInsertIcdGrouped.executeBatch();
            pstmtInsertOpsGrouped.executeBatch();
            pstmtInsertSupplFee.executeBatch();
            pstmtInsertCheckResult.executeBatch();
            pstmtInsertRole2Check.executeBatch();
            pstmtInsertRole2Result.executeBatch();
            pstmtInsertPeppGrades.executeBatch();
            pstmtUpdateCaseDetailsLos.executeBatch();
            pstmtInsertDrgCareGrades.executeBatch();
        }
    }
//
//    public int getWaitingInserts() {
//        return waitingInserts;
//    }
//
    public void setLos(long pCaseDetailsId, int pLos) throws SQLException {
        if (STORE_GRPRESULTS) {
// update t_case_details     

            pstmtUpdateCaseDetailsLos.setLong(1, pCaseDetailsId);
            pstmtUpdateCaseDetailsLos.setLong(2, pLos);
            pstmtUpdateCaseDetailsLos.addBatch();
            pstmtUpdateCaseDetailsLos.clearParameters();
        }
       
    }
    
    public void insertGroupingResult(final TGroupingResults pGrpRes, final TransferCase pTransferCase, final TransferGroupResult pTransferGroupResult, final TransferIcd pTransferIcd) throws SQLException {
        if (pGrpRes == null) {
            return;
        }
        if (pGrpRes.getGrpresType() == null) {
            LOG.log(Level.INFO, "grouping result type is null for case id " + pTransferCase.getCaseId() + " and case details id " + pTransferCase.getCaseDetailsId());
            return;
        }
        int counter = counterInsert.incrementAndGet();
        if (STORE_GRPRESULTS) {
// update t_case_details     
            
//            pstmtUpdateCaseDetailsLos.setLong(1, pTransferCase.getCaseDetailsId());
//            pstmtUpdateCaseDetailsLos.setLong(2, pTransferCase.getLengthOfStay());
//            pstmtUpdateCaseDetailsLos.addBatch();
//            pstmtUpdateCaseDetailsLos.clearParameters();
            
            long grpResId = getGrpResId();
            pGrpRes.setId(grpResId);

            // 1. GRPRES_TYPE_EN            VARCHAR2(31 CHAR)   NOT NULL
            pstmtInsertGroupingResult.setString(1, pGrpRes.getGrpresType() == null ? null : pGrpRes.getGrpresType().name());
            // 2. GRPRES_CODE               VARCHAR2(10 CHAR)
            pstmtInsertGroupingResult.setString(2, pGrpRes.getGrpresCode());
            // 3. GRPRES_GPDX               VARCHAR2(25 CHAR)
            pstmtInsertGroupingResult.setString(3, pGrpRes.getGrpresGpdx() == null ? null : pGrpRes.getGrpresGpdx().name()); //Unknown name value [0] for enum class [de.lb.cpx.model.enums.GroupResultPdxEn]
            if (pGrpRes.getGrpresGroup() == null) {
                // 4. GRPRES_GROUP              VARCHAR2(25 CHAR)
                pstmtInsertGroupingResult.setNull(4, Types.VARCHAR);
            } else {
                // 4. GRPRES_GROUP              VARCHAR2(25 CHAR)
                pstmtInsertGroupingResult.setString(4, pGrpRes.getGrpresGroup().name());
            }
            // 5. GRPRES_GST                VARCHAR2(25 CHAR)
            pstmtInsertGroupingResult.setString(5, pGrpRes.getGrpresGst() == null ? null : pGrpRes.getGrpresGst().name());
            // 6. GRPRES_IS_AUTO_FL         NUMBER(10,0)
            pstmtInsertGroupingResult.setBoolean(6, pGrpRes.getGrpresIsAutoFl());
            // 7. GRPMODEL_ID_ENRES_PCCL    NUMBER(10,0)
            pstmtInsertGroupingResult.setLong(7, pGrpRes.getGrpresPccl());
            // 8. MODEL_ID_EN               VARCHAR2(25 CHAR)   NOT NULL
            pstmtInsertGroupingResult.setString(8, pGrpRes.getModelIdEn().name());
            if (pGrpRes.getCaseDrg() == null) {
                // 9. DRGC_CW_CORR              FLOAT
                pstmtInsertGroupingResult.setNull(9, Types.FLOAT);
                //10. DRGC_CW_EFFECTIV          FLOAT
                pstmtInsertGroupingResult.setNull(10, Types.FLOAT);
                //11. DRGC_DAYS_CORR            NUMBER(5,0)
                pstmtInsertGroupingResult.setNull(11, Types.FLOAT);
                //12. DRGC_TYPE_OF_CORR_EN      VARCHAR2(25 CHAR)
                pstmtInsertGroupingResult.setNull(12, Types.VARCHAR);
            } else {
                // 9. DRGC_CW_CORR              FLOAT
                pstmtInsertGroupingResult.setDouble(9, pGrpRes.getCaseDrg().getDrgcCwCorr());
                //10. DRGC_CW_EFFECTIV          FLOAT
                pstmtInsertGroupingResult.setDouble(10, pGrpRes.getCaseDrg().getDrgcCwEffectiv());
                //11. DRGC_DAYS_CORR            NUMBER(5,0)
                pstmtInsertGroupingResult.setLong(11, pGrpRes.getCaseDrg().getDrgcDaysCorr());
                //12. DRGC_TYPE_OF_CORR_EN      VARCHAR2(25 CHAR)
                pstmtInsertGroupingResult.setString(12, pGrpRes.getCaseDrg().getDrgcTypeOfCorrEn().name());
            }
            if (pGrpRes.getCasePepp() == null) {
                //13. PEPPC_CW_EFFECTIV         FLOAT
                pstmtInsertGroupingResult.setNull(13, Types.FLOAT);
                //14. PEPPC_DAYS_INTENSIV       NUMBER(10,0)
                pstmtInsertGroupingResult.setNull(14, Types.FLOAT);
                //15. PEPPC_DAYS_PERSCARE_ADULT NUMBER(10,0)
                pstmtInsertGroupingResult.setNull(15, Types.FLOAT);
                //16. PEPPC_DAYS_PERSCARE_INF   NUMBER(10,0)
                pstmtInsertGroupingResult.setNull(16, Types.FLOAT);
                //17. PEPPC_PERSENTAGE_INTENS   NUMBER(10,0)
                pstmtInsertGroupingResult.setNull(17, Types.FLOAT);
                //18. PEPPC_TYPE_EN             VARCHAR2(20 CHAR)
                pstmtInsertGroupingResult.setNull(18, Types.VARCHAR);
             // 37. PEPP_PAY_CLASS_CW_DAY   
                pstmtInsertGroupingResult.setNull(37, Types.DOUBLE);
            } else {
                //13. PEPPC_CW_EFFECTIV         FLOAT
                pstmtInsertGroupingResult.setDouble(13, pGrpRes.getCasePepp().getPeppcCwEffectiv());
                //14. PEPPC_DAYS_INTENSIV       NUMBER(10,0)
//                if (pGrpRes.getCasePepp().getPeppcDaysIntensiv() == null) {
//                    pstmtInsertGroupingResult.setNull(14, Types.FLOAT);
//                } else {
                    pstmtInsertGroupingResult.setLong(14, pGrpRes.getCasePepp().getPeppcDaysIntensiv());
//                }
                //15. PEPPC_DAYS_PERSCARE_ADULT NUMBER(10,0)
//                if (pGrpRes.getCasePepp().getPeppcDaysPerscareAdult() == null) {
//                    pstmtInsertGroupingResult.setNull(15, Types.FLOAT);
//                } else {
                    pstmtInsertGroupingResult.setLong(15, pGrpRes.getCasePepp().getPeppcDaysPerscareAdult());
//                }
                //16. PEPPC_DAYS_PERSCARE_INF   NUMBER(10,0)
//                if (pGrpRes.getCasePepp().getPeppcDaysPerscareInf() == null) {
//                    pstmtInsertGroupingResult.setNull(16, Types.FLOAT);
//                } else {
                    pstmtInsertGroupingResult.setLong(16, pGrpRes.getCasePepp().getPeppcDaysPerscareInf());
//                }
                //17. PEPPC_PERSENTAGE_INTENS   NUMBER(10,0)
//                if (pGrpRes.getCasePepp().getPeppcPersentageIntens() == null) {
//                    pstmtInsertGroupingResult.setNull(17, Types.FLOAT);
//                } else {
                    pstmtInsertGroupingResult.setLong(17, pGrpRes.getCasePepp().getPeppcPersentageIntens());
//                }
                //18. PEPPC_TYPE_EN             VARCHAR2(20 CHAR)
                if (pGrpRes.getCasePepp().getPeppcType() == null) {
                    pstmtInsertGroupingResult.setNull(18, Types.FLOAT);
                } else {
                    pstmtInsertGroupingResult.setString(18, pGrpRes.getCasePepp().getPeppcType().name());
                }
            }
            //19. T_CASE_DETAILS_ID                   NUMBER(19,0)        NOT NULL
            pstmtInsertGroupingResult.setLong(19, pTransferCase.getCaseDetailsId());
            if (pGrpRes.getCaseIcd() == null) {
                //20. T_CASE_ICD_ID                   NUMBER(19,0)
                if (pTransferIcd != null && pTransferIcd.getId() > 0) {
                    pstmtInsertGroupingResult.setLong(20, pTransferIcd.getId());
                } else {
                    pstmtInsertGroupingResult.setNull(20, Types.INTEGER);
                }
            } else {
                //20. T_CASE_ICD_ID                   NUMBER(19,0)
                pstmtInsertGroupingResult.setLong(20, pGrpRes.getCaseIcd().getId());
            }
            //21. NEGOTIATED_FL   NUMBER(1,0)
            pstmtInsertGroupingResult.setBoolean(21, pGrpRes.isGrpresIsNegotiatedFl());
            //22. DAY_CARE_FL     NUMBER(1,0)
            pstmtInsertGroupingResult.setBoolean(22, pGrpRes.isGrpresIsDayCareFl());
            //23. EXCEPTION_DRG_FL      NUMBER(1,0)
            pstmtInsertGroupingResult.setBoolean(23, pGrpRes.getCaseDrg() == null ? false : pGrpRes.getCaseDrg().getIsDrgcIsExceptionFl());
            //24. DRGC_PARTITION_EN         VARCHAR2(25 BYTE)
            pstmtInsertGroupingResult.setString(24, (pGrpRes.getCaseDrg() == null || pGrpRes.getCaseDrg().getDrgcPartitionEn() == null) ? "A" : pGrpRes.getCaseDrg().getDrgcPartitionEn().name());
            //25. DRGC_HTP                  NUMBER(3,0)
            pstmtInsertGroupingResult.setLong(25, pGrpRes.getCaseDrg() == null ? 0 : pGrpRes.getCaseDrg().getDrgcHtp());
            //26. ID NOT NULL
            pstmtInsertGroupingResult.setLong(26, grpResId);
            //27. DRGC_NEGO_FEE_2_DAY	FLOAT	No
            pstmtInsertGroupingResult.setDouble(27, pGrpRes.getCaseDrg() == null ? 0 : pGrpRes.getCaseDrg().getDrgcNegoFee2Day());
            //28. DRGC_NEGO_FEE_DAYS	NUMBER(5,0)	No              
            pstmtInsertGroupingResult.setLong(28, pGrpRes.getCaseDrg() == null ? 0 : pGrpRes.getCaseDrg().getDrgcNegoFeeDays());
            final TCasePepp casePepp = pGrpRes.getCasePepp();
            if (casePepp == null || casePepp.getPeppcPayClass() == null) {
                pstmtInsertGroupingResult.setNull(29, Types.INTEGER);
            } else {
                pstmtInsertGroupingResult.setLong(29, casePepp.getPeppcPayClass());
            // 37. PEPP_PAY_CLASS_CW_DAY
                if(casePepp.getPeppPayClassCwDay() == null){
                    pstmtInsertGroupingResult.setNull(37, Types.DOUBLE);
                }else {
                    pstmtInsertGroupingResult.setDouble(37, casePepp.getPeppPayClassCwDay());
                }
            }
            //30. DRGC_LTP	int      
            pstmtInsertGroupingResult.setLong(30, pGrpRes.getCaseDrg() == null ? 0 : pGrpRes.getCaseDrg().getDrgcLtp());
            //31. DRGC_ALOS	 float     
            pstmtInsertGroupingResult.setDouble(31, pGrpRes.getCaseDrg() == null ? 0 : pGrpRes.getCaseDrg().getDrgcAlos());
            //32. DRGC_CW_CATALOG	float      
            pstmtInsertGroupingResult.setDouble(32, pGrpRes.getCaseDrg() == null ? 0 : pGrpRes.getCaseDrg().getDrgcCwCatalog());
            //33. DRGC_CW_CORR_DAY	float      
            pstmtInsertGroupingResult.setDouble(33, pGrpRes.getCaseDrg() == null ? 0 : pGrpRes.getCaseDrg().getDrgcCwCorrDay());
            //34. DRGC_CARE_CW_DAY	float      
            pstmtInsertGroupingResult.setDouble(34, pGrpRes.getCaseDrg() == null ? 0 : pGrpRes.getCaseDrg().getDrgcCareCwDay());
            //35. DRGC_CARE_DAYS	int      
            pstmtInsertGroupingResult.setInt(35, pGrpRes.getCaseDrg() == null ? 0 : pGrpRes.getCaseDrg().getDrgcCareDays());
            //36. DRGC_CARE_CW	int      
            pstmtInsertGroupingResult.setDouble(36, pGrpRes.getCaseDrg() == null ? 0 : pGrpRes.getCaseDrg().getDrgcCareCw());
            //38. CREATION_DATE	  
            java.sql.Timestamp date = new java.sql.Timestamp(System.currentTimeMillis());
            userId = ClientManager.getCurrentCpxUserId();// in job is null
            pstmtInsertGroupingResult.setTimestamp(38, date);
            //39. CREATION_USER	      
            pstmtInsertGroupingResult.setLong(39, userId == null?0L:userId);//check with null
            //40. MODIFICATION_DATE	      
            pstmtInsertGroupingResult.setTimestamp(40, date);
            //41. MODIFICATION_USER	      
            pstmtInsertGroupingResult.setLong(41, userId == null?0L:userId);//check with null
//            pstmtInsertGroupingResult.setLong(38, pTransferCase.getCaseId());

            pstmtInsertGroupingResult.addBatch();
            pstmtInsertGroupingResult.clearParameters();

//        TCaseDetails caseDetails = pGrpRes.getCaseDetails();
//        pstmtUpdateCaseDetails.setLong(1, caseDetails.getCsdLos());
//        pstmtUpdateCaseDetails.setLong(2, caseDetails.getId());
//        pstmtUpdateCaseDetails.addBatch();
//        pstmtUpdateCaseDetails.clearParameters();
// for hdx only, if pTransferIcd == null it was the special pTransferGroupResult for not grouped object
            if (pTransferIcd != null && pTransferIcd.isHdx() || pTransferIcd == null) {
                Iterator<TransferIcdResult> itIcdGrouped = pTransferGroupResult.getIcdRes().iterator();
                while (itIcdGrouped.hasNext()) {
                    TransferIcdResult icdGrouped = itIcdGrouped.next();
                    if (icdGrouped == null) {
                        continue;
                    }
                    //ICD_RES_CCL, ICD_RES_U4G_FL, ICD_RES_VALID_EN, T_CASE_ICD_ID, T_GROUPING_RESULTS_ID
                    pstmtInsertIcdGrouped.setLong(1, icdGrouped.getCCL());
                    pstmtInsertIcdGrouped.setBoolean(2, icdGrouped.isUsed4grouping());
                    pstmtInsertIcdGrouped.setLong(3, icdGrouped.getValid());
                    pstmtInsertIcdGrouped.setLong(4, icdGrouped.getId());
                    pstmtInsertIcdGrouped.setLong(5, grpResId);
                    //                pstmtInsertIcdGrouped.setLong(6, pTransferCase.getCaseId());
                    //                pstmtInsertIcdGrouped.setLong(7, pTransferCase.getCaseDetailsId());
                    pstmtInsertIcdGrouped.addBatch();
                    pstmtInsertIcdGrouped.clearParameters();
                }

                Iterator<Map.Entry<Long, TransferOpsResult>> itOpsGrouped = pTransferGroupResult.getOpsRes().entrySet().iterator();
                while (itOpsGrouped.hasNext()) {
                    Map.Entry<Long, TransferOpsResult> opsGroupedEntry = itOpsGrouped.next();
                    if (opsGroupedEntry == null) {
                        continue;
                    }
                    final TransferOpsResult opsGrouped = opsGroupedEntry.getValue();
                    long opsResId = getOpsResId();
                    //pGrpRes.setId(opsResId);

                    //OPS_RES_TYPE_EN, OPS_RES_U4G_FL, OPS_RES_VALID_EN, T_CASE_OPS_ID, T_GROUPING_RESULTS_ID
                    pstmtInsertOpsGrouped.setLong(1, opsResId);
                    pstmtInsertOpsGrouped.setString(2, opsGrouped.getResType() == Character.MIN_VALUE ? null : OpsResTypeEn.findById(opsGrouped.getResType()).name());
                    pstmtInsertOpsGrouped.setBoolean(3, opsGrouped.isUsed4grouping());
                    pstmtInsertOpsGrouped.setLong(4, opsGrouped.getValid());
                    pstmtInsertOpsGrouped.setLong(5, opsGrouped.getId());
                    pstmtInsertOpsGrouped.setLong(6, grpResId);
                    //                pstmtInsertOpsGrouped.setLong(7, pTransferCase.getCaseId());
                    //                pstmtInsertOpsGrouped.setLong(8, pTransferCase.getCaseDetailsId());
                    pstmtInsertOpsGrouped.addBatch();
                    pstmtInsertOpsGrouped.clearParameters();

                    TransferSupplementaryFee supplFee = opsGrouped.getSupplementaryFee();
                    if (supplFee != null) {
                        //1. CSUPL_COUNT	NUMBER(10,0)
                        pstmtInsertSupplFee.setInt(1, supplFee.getCount());
                        //2. CSUPL_CW_VALUE	FLOAT
                        pstmtInsertSupplFee.setDouble(2, supplFee.getCw());
                        //3. CSUPL_FROM	TIMESTAMP(6)
                        pstmtInsertSupplFee.setDate(3, supplFee.getFrom() == null ? null : new java.sql.Date(supplFee.getFrom().getTime()));
                        //4. CSUPL_TO	TIMESTAMP(6)
                        pstmtInsertSupplFee.setDate(4, supplFee.getTo() == null ? null : new java.sql.Date(supplFee.getTo().getTime()));
                        //5. CSUPL_TYPE_EN	VARCHAR2(255 CHAR)
                        pstmtInsertSupplFee.setString(5, supplFee.getTypeid() == 0 ? null : SupplFeeTypeEn.findById(supplFee.getTypeid()).name());
                        //6. CSUPL_VALUE	FLOAT
                        pstmtInsertSupplFee.setDouble(6, supplFee.getValue());
                        //7. SUPPL_FEE_CODE	VARCHAR2(255 CHAR)
                        pstmtInsertSupplFee.setString(7, supplFee.getCode());
                        //8. T_CASE_OPS_GROUPED_ID	NUMBER(19,0)
                        pstmtInsertSupplFee.setLong(8, opsResId);
                        //                    pstmtInsertSupplFee.setLong(9, pTransferCase.getCaseId());
                        //                    pstmtInsertSupplFee.setLong(10, pTransferCase.getCaseDetailsId());
                        pstmtInsertSupplFee.addBatch();
                        pstmtInsertSupplFee.clearParameters();
                    }
                }
            }
            Iterator<TCheckResult> itCheckResult = pGrpRes.getCheckResults().iterator();
            while (itCheckResult.hasNext()) {
                TCheckResult checkResult = itCheckResult.next();
                if (checkResult == null) {
                    continue;
                }
                long checkResultId = getCheckResultId();
//            + " (ID, "
//                        + "CHK_CW_SIMUL_DIFF,"
//                        + "CHK_CW_CARE_SIMUL_DIFF,"
//                        + " CHK_SIMUL_DRG, "
//                        + "CHK_FEE_SIMUL_DIFF, "
//                        + "CHK_SIMUL_REFERENCES, "
//                        + "CRGR_ID, "
//                        + "T_GROUPING_RESULTS_ID, "
                pstmtInsertCheckResult.setLong(1, checkResultId);
                pstmtInsertCheckResult.setDouble(2, checkResult.getChkCwSimulDiff());
                pstmtInsertCheckResult.setDouble(3, checkResult.getChkCwCareSimulDiff());
                pstmtInsertCheckResult.setString(4, checkResult.getChkDrg());
                pstmtInsertCheckResult.setDouble(5, checkResult.getChkFeeSimulDiff());
                pstmtInsertCheckResult.setString(6, checkResult.getChkReferences());
                pstmtInsertCheckResult.setLong(7, checkResult.getRuleid());
                pstmtInsertCheckResult.setLong(8, grpResId);
                pstmtInsertCheckResult.setInt(9, checkResult.getChkRskAuditPercentVal()== null?0:checkResult.getChkRskAuditPercentVal());//  "CHK_RSK_AUDIT_PERCENT_VAL, "
                pstmtInsertCheckResult.setDouble(10,checkResult.getChkRskAuditVal()== null?0:checkResult.getChkRskAuditVal());// "CHK_RSK_AUDIT_VAL, "
                pstmtInsertCheckResult.setInt(11,checkResult.getChkRskWastePercentVal() == null?0:checkResult.getChkRskWastePercentVal());// "CHK_RSK_WASTE_PERCENT_VAL, "
                pstmtInsertCheckResult.setDouble(12, checkResult.getChkRskWasteVal()==null?0:checkResult.getChkRskWasteVal());//"CHK_RSK_WASTE_VAL, "
                pstmtInsertCheckResult.setDouble(13, checkResult.getChkRskDefaultWasteVal() == null?0:checkResult.getChkRskDefaultWasteVal());// "CHK_RSK_DEFAULT_WASTE_VAL"
//                pstmtInsertCheckResult.setLong(8, checkResult.getCaseId());
//                pstmtInsertCheckResult.setLong(9, pTransferCase.getCaseDetailsId());
                pstmtInsertCheckResult.addBatch();
                pstmtInsertCheckResult.clearParameters();

                Iterator<TRole2Check> itRole2Check = checkResult.getRole2Check().iterator();
                while (itRole2Check.hasNext()) {
                    TRole2Check role2Check = itRole2Check.next();
                    if (role2Check == null) {
                        continue;
                    }
                    pstmtInsertRole2Check.setLong(1, role2Check.getRoleId());
                    pstmtInsertRole2Check.setLong(2, checkResultId);
//                    pstmtInsertRole2Check.setLong(3, pTransferCase.getCaseId());
//                    pstmtInsertRole2Check.setLong(4, pTransferCase.getCaseDetailsId());
                    pstmtInsertRole2Check.addBatch();
                    pstmtInsertRole2Check.clearParameters();
                }
            }

            Iterator<TRole2Result> itRole2Result = pGrpRes.getRole2Results().iterator();
            while (itRole2Result.hasNext()) {
                TRole2Result role2Result = itRole2Result.next();
                if (role2Result == null) {
                    continue;
                }
//                + "R2R_ADWISE_COUNT, "
//                + "R2R_ERROR_COUNT, "
//                + "R2R_WARNING_COUNT, "
//                + "USER_ROLE_ID, "
//                + "T_GROUPING_RESULTS_ID, "
//                + "MAX_DCW_POSITIVE, "
//                + "MIN_DCW_NEGATIVE, "
//                + "MIN_DCW_CARE_NEGATIVE, "
//                + "MAX_DCW_CARE_POSITIVE, "
//                + "MAX_DFEE_POSITIVE, "
//                + "MIN_DFEE_NEGATIVE, "
                pstmtInsertRole2Result.setLong(1, role2Result.getR2rAdwiseCount());
                pstmtInsertRole2Result.setLong(2, role2Result.getR2rErrorCount());
                pstmtInsertRole2Result.setLong(3, role2Result.getR2rWarningCount());
                pstmtInsertRole2Result.setLong(4, role2Result.getRoleId());
                pstmtInsertRole2Result.setLong(5, grpResId);
                pstmtInsertRole2Result.setDouble(6, role2Result.getMaxDcwPositive());
                pstmtInsertRole2Result.setDouble(7, role2Result.getMinDcwNegative());
                pstmtInsertRole2Result.setDouble(8, role2Result.getMinDcwCareNegative());
                pstmtInsertRole2Result.setDouble(9, role2Result.getMaxDcwCarePositive());
                pstmtInsertRole2Result.setDouble(10, role2Result.getMaxDfeePositive());
                pstmtInsertRole2Result.setDouble(11, role2Result.getMinDfeeNegative());
//                pstmtInsertRole2Result.setLong(10, pTransferCase.getCaseId());
//                pstmtInsertRole2Result.setLong(11, pTransferCase.getCaseDetailsId());
//      pstmt_insertRole2Result.setLong(10,role2Result.getMaxDcwPosRef().getRole2Check()  );
//      pstmt_insertRole2Result.setLong(11,role2Result.getMinDcwNegRef().getId());
//      pstmt_insertRole2Result.setLong(12,role2Result.getMaxDfeePosRef().getId());
//      pstmt_insertRole2Result.setLong(13,role2Result.getMinDfeeNegRef().getId());
                pstmtInsertRole2Result.addBatch();
                pstmtInsertRole2Result.clearParameters();
            }
            if (pGrpRes.getCasePepp() != null && pGrpRes.getCasePepp().getPeppcGrades() != null) {
                Iterator<TCasePeppGrades> itPeppGrades = pGrpRes.getCasePepp().getPeppcGrades().iterator();
                while (itPeppGrades.hasNext()) {
                    TCasePeppGrades peppGrades = itPeppGrades.next();
                    if (peppGrades == null) {
                        continue;
                    }

                    pstmtInsertPeppGrades.setInt(1, peppGrades.getPeppcgrNumber());
                    pstmtInsertPeppGrades.setInt(2, peppGrades.getPeppcgrDays());
                    pstmtInsertPeppGrades.setDouble(3, peppGrades.getPeppcgrCw());
                    pstmtInsertPeppGrades.setDouble(4, peppGrades.getOneFee());
                    pstmtInsertPeppGrades.setDouble(5, peppGrades.getPeppcgrBaserate());
                    pstmtInsertPeppGrades.setDate(6, peppGrades.getPeppcgrFrom() == null ? null : new java.sql.Date(peppGrades.getPeppcgrFrom().getTime()));
                    pstmtInsertPeppGrades.setDate(7, peppGrades.getPeppcgrTo() == null ? null : new java.sql.Date(peppGrades.getPeppcgrTo().getTime()));
                    pstmtInsertPeppGrades.setLong(8, grpResId);
//                    pstmtInsertPeppGrades.setLong(9, pTransferCase.getCaseId());
//                    pstmtInsertPeppGrades.setLong(10, pTransferCase.getCaseDetailsId());
                    pstmtInsertPeppGrades.addBatch();
                    pstmtInsertPeppGrades.clearParameters();
                }
            }
            if(pGrpRes.getCaseDrg() != null && pGrpRes.getCaseDrg().getDrgCareGrades() != null){
                Iterator<TCaseDrgCareGrades> itCareGrades = pGrpRes.getCaseDrg().getDrgCareGrades().iterator();
                while (itCareGrades.hasNext()) {
                    TCaseDrgCareGrades careGrades = itCareGrades.next();
                    if (careGrades == null) {
                        continue;
                    }

                pstmtInsertDrgCareGrades.setInt(1, careGrades.getDrgCareSortInd());// "SORT_INDEX, "
                pstmtInsertDrgCareGrades.setInt(2, careGrades.getDrgCareDays());//) "CARE_DAYS, "
                pstmtInsertDrgCareGrades.setDouble(3, careGrades.getDrgCareCwDay());// "CARE_CW_DAY, "
                pstmtInsertDrgCareGrades.setDouble(4, careGrades.getDrgCareBaserate());// "CARE_BASERATE, "
                pstmtInsertDrgCareGrades.setDate(5, careGrades.getDrgCareFrom() == null?
                        new java.sql.Date(pTransferCase.getDateOfAdmission().getTime()):new java.sql.Date(careGrades.getDrgCareFrom().getTime()));//"ACCOUNTED_FROM, "
                pstmtInsertDrgCareGrades.setDate(6, careGrades.getDrgCareTo() == null?
                        new java.sql.Date(pTransferCase.getDateOfDischarge().getTime()): new java.sql.Date(careGrades.getDrgCareTo().getTime()));// "ACCOUNTED_TO, "
                pstmtInsertDrgCareGrades.setLong(7, grpResId);// "T_CASE_DRG_ID, "                
                pstmtInsertDrgCareGrades.addBatch();
                pstmtInsertDrgCareGrades.clearParameters();                }     

            }
            waitingInserts++;
//        if (counter % 1000 == 0) {
//            LOG.log(Level.INFO, "store grouper result " + counter);
//        }
            executeInsert(CHUNK_INSERT_SIZE);
//        if (counter % CHUNK_INSERT_SIZE == 0) {
//            executeInsert();
//        }
        }
    }
//
//    public long getNextId(final String pSequenceName) throws SQLException {
//        final String query = getNextIdQuery(pSequenceName, 1);
//        try (Statement stmt = connection.createStatement()) {
//            try (ResultSet rs = stmt.executeQuery(query)) {
//                while (rs.next()) {
//                    long id = rs.getLong("VAL");
//                    return id;
//                }
//            }
//        }
//        return 0L;
//    }
//
//    public String getNextIdQuery(final String pSequenceName, final int pSize) {
//        final String query;
//        if (isOracle) {
//            query = "select " + nextSqVal(pSequenceName) + " VAL "
//                    + " from dual "
//                    + " connect by level <= " + pSize;
//        } else {
//            query = "select " + nextSqVal(pSequenceName) + " VAL "
//                    + " FROM (SELECT TOP " + pSize + " ID FROM T_CASE) TMP ";
//        }
//        return query;
//    }

    public long getGrpResId() throws SQLException {
        return getId4Storage(grpResIdStorage, "T_GROUPING_RESULTS_SQ", SEQUENCE_SIZE_GRPRES);
    }
    
    public long getOpsResId() throws SQLException {
        return getId4Storage(opsResIdStorage, "T_CASE_OPS_GROUPED_SQ", SEQUENCE_SIZE_OPS_GRP);
    }

    public long getCheckResultId() throws SQLException {
        return getId4Storage(opsCheckResultIdStorage, "T_CHECK_RESULTS_SQ", SEQUENCE_SIZE_CHECKRESULT);
    }

    @Override
    public synchronized void createPStatements() throws SQLException {
        if (!start.get()) {
            return;
        }
        start.set(false);

        //LOGGER.info("CREATE PREPSTORER " + this.toString() + "...");
        connection.setAutoCommit(false);

        pstmtInsertGroupingResult = createPstmt("INSERT INTO /*+ PARALLEL(" + groupingResultsTable + " 5)*/ " + groupingResultsTable + " ("
                + "GRPRES_TYPE_EN, "
                + "GRPRES_CODE, "
                + "GRPRES_GPDX, "
                + "GRPRES_GROUP, "
                + "GRPRES_GST, "
                + "GRPRES_IS_AUTO_FL, "
                + "GRPRES_PCCL, "
                + "MODEL_ID_EN, "
                + "DRGC_CW_CORR, "
                + "DRGC_CW_EFFECTIV, "
                + "DRGC_DAYS_CORR, "
                + "DRGC_TYPE_OF_CORR_EN, "
                + "PEPPC_CW_EFFECTIV, "
                + "PEPPC_DAYS_INTENSIV, "
                + "PEPPC_DAYS_PERSCARE_ADULT, "
                + "PEPPC_DAYS_PERSCARE_INF, "
                + "PEPPC_PERSENTAGE_INTENS, "
                + "PEPPC_TYPE_EN, "
                + "T_CASE_DETAILS_ID, "
                + "T_CASE_ICD_ID, "
                + "NEGOTIATED_FL, "
                + "DAY_CARE_FL, "
                + "EXCEPTION_DRG_FL, "
                + "DRGC_PARTITION_EN, "
                + "DRGC_HTP, "
                + "ID, "
                + "DRGC_NEGO_FEE_2_DAY, "
                + "DRGC_NEGO_FEE_DAYS, "
                + "PEPPC_PAY_CLASS, "
                + "DRGC_LTP, "
                + "DRGC_ALOS, "
                + "DRGC_CW_CATALOG, "
                + "DRGC_CW_CORR_DAY, "
                + "DRGC_CARE_CW_DAY, "
                + "DRGC_CARE_DAYS, "
                + "DRGC_CARE_CW, "
                + "PEPP_PAY_CLASS_CW_DAY, "
                + "CREATION_DATE, "
                + "CREATION_USER, "
                + "MODIFICATION_DATE, "
                + "MODIFICATION_USER, "
                // + "T_CASE_ID, "
                + "VERSION "
                + ") VALUES ("
                + "?, "
                + // 1. GRPRES_TYPE_EN            VARCHAR2(31 CHAR)   NOT NULL
                "?, "
                + // 2. GRPRES_CODE               VARCHAR2(10 CHAR)
                "?, "
                + // 3. GRPRES_GPDX               VARCHAR2(25 CHAR)
                "?, "
                + // 4. GRPRES_GROUP              VARCHAR2(25 CHAR)
                "?, "
                + // 5. GRPRES_GST                VARCHAR2(25 CHAR)
                "?, "
                + // 6. GRPRES_IS_AUTO_FL         NUMBER(10,0)
                "?, "
                + // 7. GRPMODEL_ID_ENRES_PCCL    NUMBER(10,0)
                "?, "
                + // 8. MODEL_ID_EN               VARCHAR2(25 CHAR)   NOT NULL
                "?, "
                + // 9. DRGC_CW_CORR              FLOAT
                "?, "
                + //10. DRGC_CW_EFFECTIV          FLOAT
                "?, "
                + //11. DRGC_DAYS_CORR            NUMBER(5,0)
                "?, "
                + //12. DRGC_TYPE_OF_CORR_EN      VARCHAR2(25 CHAR)
                "?, "
                + //13. PEPPC_CW_EFFECTIV         FLOAT
                "?, "
                + //14. PEPPC_DAYS_INTENSIV       NUMBER(10,0)
                "?, "
                + //15. PEPPC_DAYS_PERSCARE_ADULT NUMBER(10,0)
                "?, "
                + //16. PEPPC_DAYS_PERSCARE_INF   NUMBER(10,0)
                "?, "
                + //17. PEPPC_PERSENTAGE_INTENS   NUMBER(10,0)
                "?, "
                + //18. PEPPC_TYPE_EN             VARCHAR2(20 CHAR)
                "?, "
                + //19. T_CASE_DETAILS_ID                   NUMBER(19,0)        NOT NULL
                "?, "
                + //20. T_CASE_ICD_ID                   NUMBER(19,0)
                "?, "
                + //21. NEGOTIATED_FL   NUMBER(1,0)
                "?, "
                + //22. DAY_CARE_FL     NUMBER(1,0)
                "?, "
                + //23. EXCEPTION_DRG_FL      NUMBER(1,0)
                "?, "
                + //24. DRGC_PARTITION_EN         VARCHAR2(25 BYTE)
                "?, "
                + //25. DRGC_HTP                  NUMBER(3,0)
                "?,  "
                + //26. ID                                            NOT NULL
                "?, "
                + //27. DRGC_NEGO_FEE_2_DAY	FLOAT	No
                "? ,"
                + //28. DRGC_NEGO_FEE_DAYS	NUMBER(5,0)	No   
                "?,  "
                + //29. PEPPC_PAY_CLASS	      
                "?,  "
                + //30. DRGC_LTP	      
                "?,  "
                + //31. DRGC_ALOS	      
                "?,  "
                + //32. DRGC_CW_CATALOG	      
                "?,  "
                + //33. DRGC_CW_CORR_DAY	      
                "?,  "
                + //34. DRGC_CARE_CW_DAY	      
                "?,  "
                + //35. DRGC_CARE_DAYS	      
                "?,  "
                + //36. DRGC_CARE_CW	      
                "?,  "
                + //37. PEPP_PAY_CLASS_CW_DAY	      
                "?,  "
                + //38. CREATION_DATE	      
                "?,  "
                + //39. CREATION_USER	      
                "?,  "
                + //40. MODIFICATION_DATE	      
                "?,  "
                + //41. MODIFICATION_USER	  
                //"?,  "
                //+ //42. T_CASE_ID
                "0"
                + ")");

        pstmtInsertIcdGrouped = createPstmt("INSERT INTO /*+ PARALLEL(" + caseIcdGroupedTable + " 5)*/ " + caseIcdGroupedTable + " (ID, ICD_RES_CCL, ICD_RES_U4G_FL, ICD_RES_VALID_EN, T_CASE_ICD_ID, T_GROUPING_RESULTS_ID, VERSION) VALUES (" + nextSqVal("T_CASE_ICD_GROUPED_SQ") + ", ?, ?, ?, ?, ?, 0)");
//        pstmtUpdateCaseDetails = createPstmt("UPDATE T_CASE_DETAILS SET LOS= ? WHERE ID = ?");

        pstmtInsertOpsGrouped = createPstmt("INSERT INTO /*+ PARALLEL(" + caseOpsGroupedTable + " 5)*/ " + caseOpsGroupedTable + " (ID, OPS_RES_TYPE_EN, OPS_RES_U4G_FL, OPS_RES_VALID_EN, T_CASE_OPS_ID, T_GROUPING_RESULTS_ID, VERSION) VALUES (?, ?, ?, ?, ?, ?, 0)");

        pstmtInsertSupplFee = createPstmt("INSERT INTO /*+ PARALLEL(" + caseSupplFeeTable + " 5)*/ " + caseSupplFeeTable + " (ID, COUNT, CW_VALUE, ACCOUNTED_FROM, ACCOUNTED_TO, SUPPL_FEE_TYPE_EN, VALUE, SUPPL_FEE_CODE, T_CASE_OPS_GROUPED_ID, VERSION) VALUES (" + nextSqVal("T_CASE_SUPPL_FEE_SQ") + ", ?, ?, ?, ?, ?, ?, ?, ?, 0)");
        pstmtInsertPeppGrades = createPstmt("INSERT INTO /*+ PARALLEL(" + casePeppGradesTable + " 5)*/ " + casePeppGradesTable + " ("
                + "ID, "
                + "GRADE, "
                + "DAYS, "
                + "CW, "
                + "ONE_FEE, "
                + "BASERATE, "
                + "ACCOUNTED_FROM, "
                + "ACCOUNTED_TO, "
                + "T_CASE_PEPP_ID, "
                + "VERSION "
                + ") VALUES ("
                + nextSqVal("T_CASE_PEPP_GRADES_SQ") + ", ?, ?, ?, ?, ?, ?, ?, ?, 0)");
        pstmtInsertCheckResult = createPstmt("INSERT INTO /*+ PARALLEL(" + checkResultTable + " 5)*/ " + checkResultTable 
                + " (ID, "
                        + "CHK_CW_SIMUL_DIFF,"
                        + "CHK_CW_CARE_SIMUL_DIFF,"
                        + " CHK_SIMUL_DRG, "
                        + "CHK_FEE_SIMUL_DIFF, "
                        + "CHK_SIMUL_REFERENCES, "
                        + "CRGR_ID, "
                        + "T_GROUPING_RESULTS_ID, "
                        + "CHK_RSK_AUDIT_PERCENT_VAL, "
                        + "CHK_RSK_AUDIT_VAL, "
                        + "CHK_RSK_WASTE_PERCENT_VAL, "
                        + "CHK_RSK_WASTE_VAL, "
                        + "CHK_RSK_DEFAULT_WASTE_VAL, "
                        + "VERSION) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)");

        pstmtInsertRole2Check = createPstmt("INSERT INTO /*+ PARALLEL(" + role2CheckTable + " 5)*/ " + role2CheckTable + " (ID, USER_ROLE_ID, T_CHECK_RESULT_ID, VERSION) VALUES (" + nextSqVal("T_ROLE_2_CHECK_SQ") + ", ?, ?, 0)");

//  pstmt_insertRole2Result = createPstmt("INSERT INTO T_ROLE_2_RESULT (ID, R2R_ADWISE_COUNT, R2R_ERROR_COUNT, R2R_WARNING_COUNT, ROLE_ID, T_GROUPING_RESULTS_ID) VALUES (T_ROLE_2_RESULT_SQ.nextval, ?, ?, ?, ?, ?)");
//   pstmt_insertRole2Result = createPstmt("INSERT INTO T_ROLE_2_RESULT (ID, R2R_ADWISE_COUNT, R2R_ERROR_COUNT, R2R_WARNING_COUNT, ROLE_ID, T_GROUPING_RESULTS_ID, MAX_DCW_POSITIVE, MIN_DCW_NEGATIVE, MAX_DFEE_POSITIVE, MIN_DFEE_NEGATIVE, MAX_DCW_POS_REF, MIN_DCW_NEG_REF, MAX_DFEE_POS_REF, MIN_DFEE_NEG_REF  ) VALUES (T_ROLE_2_RESULT_SQ.nextval, ?, ?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?)");
        pstmtInsertRole2Result = createPstmt("INSERT INTO /*+ PARALLEL(" + role2ResultTable + " 5)*/ " + role2ResultTable + " (ID, "
                + "R2R_ADWISE_COUNT, "
                + "R2R_ERROR_COUNT, "
                + "R2R_WARNING_COUNT, "
                + "USER_ROLE_ID, "
                + "T_GROUPING_RESULTS_ID, "
                + "MAX_DCW_POSITIVE, "
                + "MIN_DCW_NEGATIVE, "
                + "MIN_DCW_CARE_NEGATIVE, "
                + "MAX_DCW_CARE_POSITIVE, "
                + "MAX_DFEE_POSITIVE, "
                + "MIN_DFEE_NEGATIVE, "
                + "VERSION) VALUES (" + nextSqVal("T_ROLE_2_RESULT_SQ") + ", ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, 0)");

        pstmtUpdateCaseDetailsLos = createPstmt("INSERT INTO /*+ PARALLEL(" + caseDetailsLosTable + " 5)*/ " + caseDetailsLosTable + " (ID, LOS) VALUES (?, ?)"
        );
        
        pstmtInsertDrgCareGrades = createPstmt("INSERT INTO /*+ PARALLEL(" + caseDrgCareGradesTable + " 5)*/ " + caseDrgCareGradesTable + " ("
                + "ID, "
                + "SORT_INDEX, "
                + "CARE_DAYS, "
                + "CARE_CW_DAY, "
                + "CARE_BASERATE, "
                + "ACCOUNTED_FROM, "
                + "ACCOUNTED_TO, "
                + "T_CASE_DRG_ID, "
                + "VERSION "
                + ") VALUES ("
                + nextSqVal("T_CASE_DRG_CARE_GRADES_SQ") + ", ?, ?, ?, ?, ?, ?, ?, 0)");
    }
//
//    protected String nextSqVal(final String pSequenceName) {
//        if (isOracle) {
//            return pSequenceName + ".nextval";
//        } else {
//            return "NEXT VALUE FOR " + pSequenceName;
//        }
//    }

//    public PreparedStatement createPstmt(final String pQuery) throws SQLException {
//        PreparedStatement pstmt = connection.prepareStatement(pQuery);
//        return pstmt;
//    }
//
//    public synchronized void closePstmt(final PreparedStatement pStmt) {
//        if (pStmt != null) {
//            if (STORE_GRPRESULTS) {
//                try (pStmt) {
//                    pStmt.executeBatch();
//                } catch (SQLException ex) {
//                    LOG.log(Level.SEVERE, null, ex);
//                }
//            }
//            try {
//                if (!pStmt.isClosed()) {
//                    pStmt.close();
//                }
//                LOG.log(Level.INFO, " closed? {0}", pStmt.isClosed());
//            } catch (SQLException ex) {
//                LOG.log(Level.SEVERE, null, ex);
//            }
//        }
//    }
//
    public void distributeData(GrouperResponseWriter pWriter, ProgressCallback distributionProgressCb, AtomicReference<TBatchResult> pBatchResult, BatchGroupParameter pParameter) throws SQLException {
        final int maxSteps = 29;
        final AtomicInteger step = new AtomicInteger(0);

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
            final String[] queries = new String[]{
                //Indexes on temporary T_GROUPING_RESULTS
                "CREATE INDEX IX_GRPRES_MODEL_" + executionId + " ON " + groupingResultsTable + " (MODEL_ID_EN) " + parallel,
//                "CREATE UNIQUE INDEX IX_GRPRES_CSDID_" + executionId + " ON " + groupingResultsTable + " (T_CASE_DETAILS_ID) " + parallel,
                "CREATE INDEX IX_GRPRES_CSDID_" + executionId + " ON " + groupingResultsTable + " (T_CASE_DETAILS_ID) " + parallel,
//                "CREATE UNIQUE INDEX IX_CASE_DET_LOS_" + executionId + " ON " + caseDetailsLosTable + " (ID) " + parallel, //            "CREATE UNIQUE INDEX IX_GRPRES_ID_" + executionId + " ON " + groupingResultsTable + " (ID) " + parallel,
               "CREATE INDEX IX_CASE_DET_LOS_" + executionId + " ON " + caseDetailsLosTable + " (ID) " + parallel, //            "CREATE UNIQUE INDEX IX_GRPRES_ID_" + executionId + " ON " + groupingResultsTable + " (ID) " + parallel,
            //            "CREATE INDEX IX_GRPRES_CS_ID_" + executionId + " ON " + groupingResultsTable + " (T_CASE_ID) " + parallel,
            //            "CREATE INDEX IX_GRPRES_CSD_ID_" + executionId + " ON " + groupingResultsTable + " (T_CASE_DETAILS_ID) " + parallel,
            //            "CREATE INDEX IX_GRPRES_ICD_ID_" + executionId + " ON " + groupingResultsTable + " (T_CASE_ICD_ID) " + parallel,
            //            //Indexes on temporary T_CASE_ICD_GROUPED
            //            "CREATE UNIQUE INDEX IX_GRPICD_ID_" + executionId + " ON " + caseIcdGroupedTable + " (ID) " + parallel,
            //            "CREATE INDEX IX_GRPICD_CS_ID_" + executionId + " ON " + caseIcdGroupedTable + " (T_CASE_ID) " + parallel,
            //            "CREATE INDEX IX_GRPICD_CSD_ID_" + executionId + " ON " + caseIcdGroupedTable + " (T_CASE_DETAILS_ID) " + parallel,
            //            "CREATE INDEX IX_GRPICD_ICD_ID_" + executionId + " ON " + caseIcdGroupedTable + " (T_CASE_ICD_ID) " + parallel,
            //            "CREATE INDEX IX_GRPICD_GRP_ID_" + executionId + " ON " + caseIcdGroupedTable + " (T_GROUPING_RESULTS_ID) " + parallel,
            //            //Indexes on temporary T_CASE_OPS_GROUPED
            //            "CREATE UNIQUE INDEX IX_GRPOPS_ID_" + executionId + " ON " + caseOpsGroupedTable + " (ID) " + parallel,
            //            "CREATE INDEX IX_GRPOPS_CS_ID_" + executionId + " ON " + caseOpsGroupedTable + " (T_CASE_ID) " + parallel,
            //            "CREATE INDEX IX_GRPOPS_CSD_ID_" + executionId + " ON " + caseOpsGroupedTable + " (T_CASE_DETAILS_ID) " + parallel,
            //            "CREATE INDEX IX_GRPOPS_OPS_ID_" + executionId + " ON " + caseOpsGroupedTable + " (T_CASE_OPS_ID) " + parallel,
            //            "CREATE INDEX IX_GRPOPS_GRP_ID_" + executionId + " ON " + caseOpsGroupedTable + " (T_GROUPING_RESULTS_ID) " + parallel,
            //            //Indexes on temporary T_CASE_SUPPL_FEE
            //            "CREATE UNIQUE INDEX IX_GRPFEE_ID_" + executionId + " ON " + caseSupplFeeTable + " (ID) " + parallel,
            //            "CREATE INDEX IX_GRPFEE_CS_ID_" + executionId + " ON " + caseSupplFeeTable + " (T_CASE_ID) " + parallel,
            //            "CREATE INDEX IX_GRPFEE_CSD_ID_" + executionId + " ON " + caseSupplFeeTable + " (T_CASE_DETAILS_ID) " + parallel,
            //            "CREATE INDEX IX_GRPFEE_OPS_ID_" + executionId + " ON " + caseSupplFeeTable + " (T_CASE_OPS_GROUPED_ID) " + parallel,
            //            //"CREATE INDEX IX_GRPFEE_GRP_ID_" + executionId + " ON " + caseSupplFeeTable + " (T_GROUPING_RESULTS_ID) " + parallel,
            //
            //            //Indexes on temporary T_CHECK_RESULT
            //            "CREATE UNIQUE INDEX IX_GRPCHK_ID_" + executionId + " ON " + checkResultTable + " (ID) " + parallel,
            //            "CREATE INDEX IX_GRPCHK_CS_ID_" + executionId + " ON " + checkResultTable + " (T_CASE_ID) " + parallel,
            //            "CREATE INDEX IX_GRPCHK_CSD_ID_" + executionId + " ON " + checkResultTable + " (T_CASE_DETAILS_ID) " + parallel,
            //            "CREATE INDEX IX_GRPCHK_GRP_ID_" + executionId + " ON " + checkResultTable + " (T_GROUPING_RESULTS_ID) " + parallel,
            //            //Indexes on temporary T_ROLE_2_CHECK
            //            "CREATE UNIQUE INDEX IX_GRPR2C_ID_" + executionId + " ON " + role2CheckTable + " (ID) " + parallel,
            //            "CREATE INDEX IX_GRPR2C_CS_ID_" + executionId + " ON " + role2CheckTable + " (T_CASE_ID) " + parallel,
            //            "CREATE INDEX IX_GRPR2C_CSD_ID_" + executionId + " ON " + role2CheckTable + " (T_CASE_DETAILS_ID) " + parallel,
            //            "CREATE INDEX IX_GRPR2C_GRP_ID_" + executionId + " ON " + role2CheckTable + " (T_CHECK_RESULT_ID) " + parallel,
            //            //Indexes on temporary T_ROLE_2_RESULT
            //            "CREATE UNIQUE INDEX IX_GRPR2R_ID_" + executionId + " ON " + role2ResultTable + " (ID) " + parallel,
            //            "CREATE INDEX IX_GRPR2R_CS_ID_" + executionId + " ON " + role2ResultTable + " (T_CASE_ID) " + parallel,
            //            "CREATE INDEX IX_GRPR2R_CSD_ID_" + executionId + " ON " + role2ResultTable + " (T_CASE_DETAILS_ID) " + parallel,
            //            "CREATE INDEX IX_GRPR2R_GRP_ID_" + executionId + " ON " + role2ResultTable + " (T_GROUPING_RESULTS_ID) " + parallel,
            //            //Indexes on temporary T_CASE_PEPP_GRADES
            //            "CREATE UNIQUE INDEX IX_GRPPGR_ID_" + executionId + " ON " + casePeppGradesTable + " (ID) " + parallel,
            //            "CREATE INDEX IX_GRPPGR_CS_ID_" + executionId + " ON " + casePeppGradesTable + " (T_CASE_ID) " + parallel,
            //            "CREATE INDEX IX_GRPPGR_CSD_ID_" + executionId + " ON " + casePeppGradesTable + " (T_CASE_DETAILS_ID) " + parallel,
            //            "CREATE INDEX IX_GRPPGR_GRP_ID_" + executionId + " ON " + casePeppGradesTable + " (T_CASE_PEPP_ID) " + parallel,
            };
            try (Statement stmt = connection.createStatement()) {
                for (String query : queries) {
                    try {
                        stmt.execute(query);
                    } catch (SQLException ex) {
                        LOG.log(Level.SEVERE, "cannot create index: " + query, ex);
                    }
                }
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }

            //let's to some real work here!
            distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Beginne mit der Verteilung der temporären Grouping-Ergebnisse in die Produktivtabellen...");
            if (pWriter.checkStopped()) {
                return;
            }
            LOG.log(Level.INFO, "distribute temporary grouping results...");
            try (Statement stmt = connection.createStatement()) {
                //connection.setAutoCommit(false);
                distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Ermittle zu überschreibende Grouping-Ergebnisse...");
                if (pWriter.checkStopped()) {
                    return;
                }
                String qry = (isOracle ? "CREATE TABLE " + groupingResultsToDeleteTable + " " + parallel + " AS " : "") + "SELECT T_GROUPING_RESULTS.ID " + (isSqlsrv ? "INTO " + groupingResultsToDeleteTable : "") + " FROM T_GROUPING_RESULTS WHERE EXISTS (SELECT 1 FROM " + groupingResultsTable + " TMP WHERE TMP.T_CASE_DETAILS_ID = T_GROUPING_RESULTS.T_CASE_DETAILS_ID AND ((TMP.GRPRES_IS_AUTO_FL = 1 AND TMP.GRPRES_IS_AUTO_FL = T_GROUPING_RESULTS.GRPRES_IS_AUTO_FL) OR (TMP.GRPRES_IS_AUTO_FL = 0 AND TMP.MODEL_ID_EN = T_GROUPING_RESULTS.MODEL_ID_EN)))";
                //int marked = stmt.execute((isOracle ? "CREATE TABLE " + groupingResultsToDeleteTable + parallel + " AS " : "") + "SELECT T_GROUPING_RESULTS.* " + (isSqlsrv ? "INTO " + groupingResultsTable : "") + " FROM T_GROUPING_RESULTS");
                stmt.execute(qry);
                stmt.execute("CREATE INDEX IX_" + groupingResultsToDeleteTable + " ON " + groupingResultsToDeleteTable + " (ID) " + parallel);
                LOG.log(Level.INFO, qry);
                LOG.log(Level.INFO, "Marked entries from T_GROUPING_RESULTS for deletion in " + groupingResultsToDeleteTable);

                distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Lösche alte Grouping-Ergebnisse in T_CASE_ICD_GROUPED...");
                if (pWriter.checkStopped()) {
                    return;
                }
                int deletedIcdGrouped = stmt.executeUpdate("DELETE FROM /*+ PARALLEL(T_CASE_ICD_GROUPED 5)*/ T_CASE_ICD_GROUPED WHERE EXISTS (SELECT 1 FROM " + groupingResultsToDeleteTable + " TMP WHERE TMP.ID = T_CASE_ICD_GROUPED.T_GROUPING_RESULTS_ID)");
                LOG.log(Level.INFO, "Deleted " + deletedIcdGrouped + " entries from T_CASE_ICD_GROUPED");

                distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Lösche alte Grouping-Ergebnisse in T_CASE_OPS_GROUPED...");
                if (pWriter.checkStopped()) {
                    return;
                }

// supplementary fees are to delete for ops_grouped
                qry = "DELETE FROM  /*+ PARALLEL(T_CASE_SUPPL_FEE 5)*/ T_CASE_SUPPL_FEE  "
                        + "WHERE EXISTS (SELECT 1 FROM " + groupingResultsToDeleteTable + " TMP "
                        + "inner join T_CASE_OPS_GROUPED ops on ops.T_GROUPING_RESULTS_ID = tmp.id WHERE ops.ID = T_CASE_SUPPL_FEE.T_CASE_OPS_GROUPED_ID)";
                LOG.log(Level.INFO, qry);
                int deletedSupplFees = stmt.executeUpdate(qry);
                LOG.log(Level.INFO, "Deleted " + deletedSupplFees + " entries from T_CASE_SUPPL_FEE");

                qry = "DELETE FROM /*+ PARALLEL(T_CASE_OPS_GROUPED 5)*/ T_CASE_OPS_GROUPED WHERE EXISTS (SELECT 1 FROM " + groupingResultsToDeleteTable + " TMP WHERE TMP.ID = T_CASE_OPS_GROUPED.T_GROUPING_RESULTS_ID)";
                LOG.log(Level.INFO, qry);
                int deletedOpsGrouped = stmt.executeUpdate(qry);
                LOG.log(Level.INFO, "Deleted " + deletedOpsGrouped + " entries from T_CASE_OPS_GROUPED");

                distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Lösche alte Grouping-Ergebnisse in T_CASE_PEPP_GRADES...");
                if (pWriter.checkStopped()) {
                    return;
                }
                qry = "DELETE FROM /*+ PARALLEL(T_CASE_PEPP_GRADES 5)*/ T_CASE_PEPP_GRADES WHERE EXISTS (SELECT 1 FROM " + groupingResultsToDeleteTable + " TMP WHERE TMP.ID = T_CASE_PEPP_GRADES.T_CASE_PEPP_ID)";
                int deletedPeppGrades = stmt.executeUpdate(qry);
                LOG.log(Level.INFO, qry);
                LOG.log(Level.INFO, "Deleted " + deletedPeppGrades + " entries from T_CASE_PEPP_GRADES");

                distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Lösche alte Grouping-Ergebnisse in T_CHECK_RESULT...");
                if (pWriter.checkStopped()) {
                    return;
                }
                qry = "DELETE FROM /*+ PARALLEL(T_CASE_DRG_CARE_GRADES 5)*/ T_CASE_DRG_CARE_GRADES WHERE EXISTS (SELECT 1 FROM " + groupingResultsToDeleteTable + " TMP WHERE TMP.ID = T_CASE_DRG_CARE_GRADES.T_CASE_DRG_ID)";
                int deletedDrgCareGrades = stmt.executeUpdate(qry);
                LOG.log(Level.INFO, qry);
                LOG.log(Level.INFO, "Deleted " + deletedDrgCareGrades + " entries from T_CASE_DRG_CARE_GRADES");

                distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Lösche alte Grouping-Ergebnisse in T_CHECK_RESULT...");
                if (pWriter.checkStopped()) {
                    return;
                }
                int deletedCheckResult = stmt.executeUpdate("DELETE FROM /*+ PARALLEL(T_CHECK_RESULT 5)*/ T_CHECK_RESULT WHERE EXISTS (SELECT 1 FROM " + groupingResultsToDeleteTable + " TMP WHERE TMP.ID = T_CHECK_RESULT.T_GROUPING_RESULTS_ID)");
                LOG.log(Level.INFO, "Deleted " + deletedCheckResult + " entries from T_CHECK_RESULT");

                distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Lösche alte Grouping-Ergebnisse in T_CHECK_RESULT...");
                if (pWriter.checkStopped()) {
                    return;
                }
                int deletedRole2Result = stmt.executeUpdate("DELETE FROM /*+ PARALLEL(T_ROLE_2_RESULT 5)*/ T_ROLE_2_RESULT WHERE EXISTS (SELECT 1 FROM " + groupingResultsToDeleteTable + " TMP WHERE TMP.ID = T_ROLE_2_RESULT.T_GROUPING_RESULTS_ID)");
                LOG.log(Level.INFO, "Deleted " + deletedRole2Result + " entries from T_ROLE_2_RESULT");

                distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Lösche alte Grouping-Ergebnisse in T_CASE_MERGE_MAPPING...");
                if (pWriter.checkStopped()) {
                    return;
                }
                int deletedMapping = stmt.executeUpdate("DELETE FROM /*+ PARALLEL(T_CASE_MERGE_MAPPING 5)*/ T_CASE_MERGE_MAPPING WHERE EXISTS (SELECT 1 FROM " + groupingResultsToDeleteTable + " TMP WHERE TMP.ID = T_CASE_MERGE_MAPPING.T_GROUPING_RESULTS_ID)");
                LOG.log(Level.INFO, "Deleted " + deletedMapping + " entries from T_CASE_MERGE_MAPPING");

                distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Lösche alte Grouping-Ergebnisse in T_GROUPING_RESULTS...");
                if (pWriter.checkStopped()) {
                    return;
                }
                int deletedGrpRes = stmt.executeUpdate("DELETE FROM /*+ PARALLEL(T_GROUPING_RESULTS 5)*/ T_GROUPING_RESULTS WHERE EXISTS (SELECT 1 FROM " + groupingResultsToDeleteTable + " TMP WHERE TMP.ID = T_GROUPING_RESULTS.ID)");
                LOG.log(Level.INFO, "Deleted " + deletedGrpRes + " entries from T_GROUPING_RESULTS");

                distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Erzeuge neue Einträge in T_GROUPING_RESULTS...");
                if (pWriter.checkStopped()) {
                    return;
                }

                qry = "INSERT INTO /*+ PARALLEL(T_GROUPING_RESULTS 5)*/ T_GROUPING_RESULTS ("
                        + "GRPRES_TYPE_EN, ID, "
                        + "CREATION_DATE, CREATION_USER, MODIFICATION_DATE, MODIFICATION_USER, VERSION,"
                        + " GRPRES_CODE, GRPRES_GPDX, GRPRES_GROUP, "
                        + "GRPRES_GST, GRPRES_IS_AUTO_FL, DAY_CARE_FL, NEGOTIATED_FL, "
                        + "GRPRES_PCCL, MODEL_ID_EN, DRGC_CW_CORR, DRGC_CW_EFFECTIV, DRGC_DAYS_CORR, DRGC_HTP, DRGC_NEGO_FEE_2_DAY, DRGC_NEGO_FEE_DAYS, "
                        + "DRGC_PARTITION_EN, DRGC_TYPE_OF_CORR_EN, EXCEPTION_DRG_FL, PEPPC_CW_EFFECTIV, PEPPC_DAYS_INTENSIV, PEPPC_DAYS_PERSCARE_ADULT, "
                        + "PEPPC_DAYS_PERSCARE_INF, PEPPC_PAY_CLASS, PEPPC_PERSENTAGE_INTENS, PEPPC_TYPE_EN, T_CASE_DETAILS_ID, T_CASE_ICD_ID, DRGC_LTP, "
                        + "DRGC_ALOS, DRGC_CW_CATALOG, DRGC_CW_CORR_DAY, DRGC_CARE_CW_DAY, DRGC_CARE_DAYS, DRGC_CARE_CW, PEPP_PAY_CLASS_CW_DAY ) "
                        + "SELECT GRPRES_TYPE_EN, ID, "
                        + currentDate() + ", "
                        + (userId == null ? "null" : String.valueOf(userId)) + ", "
                        + currentDate() + ", "
                        + (userId == null ? "null" : String.valueOf(userId)) + ", "
                        + "VERSION, GRPRES_CODE, GRPRES_GPDX, GRPRES_GROUP, "
                        + "GRPRES_GST, GRPRES_IS_AUTO_FL, DAY_CARE_FL, NEGOTIATED_FL, "
                        + "GRPRES_PCCL, MODEL_ID_EN, DRGC_CW_CORR, DRGC_CW_EFFECTIV, DRGC_DAYS_CORR, DRGC_HTP, DRGC_NEGO_FEE_2_DAY, "
                        + "DRGC_NEGO_FEE_DAYS, DRGC_PARTITION_EN, DRGC_TYPE_OF_CORR_EN, EXCEPTION_DRG_FL, PEPPC_CW_EFFECTIV, "
                        + "PEPPC_DAYS_INTENSIV, PEPPC_DAYS_PERSCARE_ADULT, PEPPC_DAYS_PERSCARE_INF, PEPPC_PAY_CLASS, "
                        + "PEPPC_PERSENTAGE_INTENS, PEPPC_TYPE_EN, T_CASE_DETAILS_ID, T_CASE_ICD_ID, DRGC_LTP, "
                        + "DRGC_ALOS, DRGC_CW_CATALOG, DRGC_CW_CORR_DAY, DRGC_CARE_CW_DAY, DRGC_CARE_DAYS, DRGC_CARE_CW , PEPP_PAY_CLASS_CW_DAY"
                        + " FROM " + groupingResultsTable;
               LOG.log(Level.FINE, qry);
                int insertedGrpRes = stmt.executeUpdate(qry);
                LOG.log(Level.INFO, "Inserted " + insertedGrpRes + " entries into T_GROUPING_RESULTS from " + groupingResultsTable);

                distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Erzeuge neue Einträge in T_CASE_ICD_GROUPED...");
                if (pWriter.checkStopped()) {
                    return;
                }
                qry = "INSERT INTO /*+ PARALLEL(T_CASE_ICD_GROUPED 5)*/ T_CASE_ICD_GROUPED (ID, CREATION_DATE, CREATION_USER, MODIFICATION_DATE, MODIFICATION_USER, VERSION, ICD_RES_CCL, ICD_RES_U4G_FL, ICD_RES_VALID_EN, T_CASE_ICD_ID, T_GROUPING_RESULTS_ID) "
                        + "SELECT ID, "
                        + currentDate() + ", "
                        + (userId == null ? "null" : String.valueOf(userId)) + ", "
                        + currentDate() + ", "
                        + (userId == null ? "null" : String.valueOf(userId)) + ", "
                        + "VERSION, ICD_RES_CCL, ICD_RES_U4G_FL, ICD_RES_VALID_EN, T_CASE_ICD_ID, T_GROUPING_RESULTS_ID FROM " + caseIcdGroupedTable;
               LOG.log(Level.FINE, qry);
                int insertedIcdGrp = stmt.executeUpdate(qry);

                LOG.log(Level.INFO, "Inserted " + insertedIcdGrp + " entries into T_CASE_ICD_GROUPED from " + caseIcdGroupedTable);

                distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Erzeuge neue Einträge in T_CASE_OPS_GROUPED...");
                if (pWriter.checkStopped()) {
                    return;
                }
                qry = "INSERT INTO /*+ PARALLEL(T_CASE_OPS_GROUPED 5)*/ T_CASE_OPS_GROUPED (ID, CREATION_DATE, CREATION_USER, MODIFICATION_DATE, MODIFICATION_USER, VERSION, OPS_RES_TYPE_EN, OPS_RES_U4G_FL, OPS_RES_VALID_EN, T_CASE_OPS_ID, T_GROUPING_RESULTS_ID) "
                        + "SELECT ID, "
                        + currentDate() + ", "
                        + (userId == null ? "null" : String.valueOf(userId)) + ", "
                        + currentDate() + ", "
                        + (userId == null ? "null" : String.valueOf(userId)) + ", "
                        + "VERSION, OPS_RES_TYPE_EN, OPS_RES_U4G_FL, OPS_RES_VALID_EN, T_CASE_OPS_ID, T_GROUPING_RESULTS_ID FROM " + caseOpsGroupedTable;
                int insertedOpsRes = stmt.executeUpdate(qry);
                LOG.log(Level.INFO, "Inserted " + insertedOpsRes + " entries into T_CASE_OPS_GROUPED from " + caseOpsGroupedTable);
//TODO insert suppl fees
                distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Erzeuge neue Einträge in T_CASE_SUPPL_FEE...");
                if (pWriter.checkStopped()) {
                    return;
                }
                qry = "INSERT INTO /*+ PARALLEL(T_CASE_SUPPL_FEE 5)*/ T_CASE_SUPPL_FEE (ID, CREATION_DATE, CREATION_USER, MODIFICATION_DATE, MODIFICATION_USER, VERSION, "
                        + " COUNT, CW_VALUE, ACCOUNTED_FROM, ACCOUNTED_TO, SUPPL_FEE_TYPE_EN, VALUE, SUPPL_FEE_CODE, T_CASE_OPS_GROUPED_ID )"
                        + " SELECT ID, "
                        + currentDate() + ", "
                        + (userId == null ? "null" : String.valueOf(userId)) + ", "
                        + currentDate() + ", "
                        + (userId == null ? "null" : String.valueOf(userId)) + ", "
                        + "VERSION, "
                        + " COUNT, CW_VALUE, ACCOUNTED_FROM, ACCOUNTED_TO, SUPPL_FEE_TYPE_EN, VALUE, SUPPL_FEE_CODE, T_CASE_OPS_GROUPED_ID "
                        + " FROM " + caseSupplFeeTable;
                LOG.log(Level.INFO, qry);
                int insertedSupplFee = stmt.executeUpdate(qry);
                LOG.log(Level.INFO, "Inserted " + insertedSupplFee + " entries into T_CASE_SUPPL_FEE from " + caseSupplFeeTable);

                distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Erzeuge neue Einträge in T_CHECK_RESULT...");
                if (pWriter.checkStopped()) {
                    return;
                }
                int insertedChkRes = stmt.executeUpdate("INSERT INTO /*+ PARALLEL(T_CHECK_RESULT 5)*/ T_CHECK_RESULT (ID, CREATION_DATE, CREATION_USER, MODIFICATION_DATE, MODIFICATION_USER,"
                        + " VERSION, CHK_CW_SIMUL_DIFF, CHK_CW_CARE_SIMUL_DIFF, CHK_SIMUL_DRG, "
                        + "CHK_FEE_SIMUL_DIFF, CHK_SIMUL_REFERENCES, CRGR_ID, T_GROUPING_RESULTS_ID,"
                        + "CHK_RSK_AUDIT_PERCENT_VAL, CHK_RSK_AUDIT_VAL, CHK_RSK_WASTE_PERCENT_VAL, CHK_RSK_WASTE_VAL, CHK_RSK_DEFAULT_WASTE_VAL) "
                        + "SELECT ID, "
                        + currentDate() + ", "
                        + (userId == null ? "null" : String.valueOf(userId)) + ", "
                        + currentDate() + ", "
                        + (userId == null ? "null" : String.valueOf(userId)) + ", "
                        + "VERSION, CHK_CW_SIMUL_DIFF, CHK_CW_CARE_SIMUL_DIFF, CHK_SIMUL_DRG, CHK_FEE_SIMUL_DIFF, CHK_SIMUL_REFERENCES, CRGR_ID, T_GROUPING_RESULTS_ID,"
                        + "CHK_RSK_AUDIT_PERCENT_VAL, CHK_RSK_AUDIT_VAL, CHK_RSK_WASTE_PERCENT_VAL, CHK_RSK_WASTE_VAL, CHK_RSK_DEFAULT_WASTE_VAL"
                        + " FROM " + checkResultTable);
                LOG.log(Level.INFO, "Inserted " + insertedChkRes + " entries into T_CHECK_RESULT from " + checkResultTable);

                distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Erzeuge neue Einträge in T_ROLE_2_CHECK...");
                if (pWriter.checkStopped()) {
                    return;
                }
                int insertedR2c = stmt.executeUpdate("INSERT INTO /*+ PARALLEL(T_ROLE_2_CHECK 5)*/ T_ROLE_2_CHECK (ID, CREATION_DATE, CREATION_USER, MODIFICATION_DATE, MODIFICATION_USER, VERSION, USER_ROLE_ID, T_CHECK_RESULT_ID) "
                        + "SELECT ID, "
                        + currentDate() + ", "
                        + (userId == null ? "null" : String.valueOf(userId)) + ", "
                        + currentDate() + ", "
                        + (userId == null ? "null" : String.valueOf(userId)) + ", "
                        + "VERSION, USER_ROLE_ID, T_CHECK_RESULT_ID FROM " + role2CheckTable);
                LOG.log(Level.INFO, "Inserted " + insertedR2c + " entries into T_ROLE_2_CHECK from " + role2CheckTable);

                distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Erzeuge neue Einträge in T_ROLE_2_RESULT...");
                if (pWriter.checkStopped()) {
                    return;
                }
                int insertedR2r = stmt.executeUpdate("INSERT INTO /*+ PARALLEL(T_ROLE_2_RESULT 5)*/ T_ROLE_2_RESULT"
                        + " (ID, CREATION_DATE, CREATION_USER, MODIFICATION_DATE, MODIFICATION_USER, VERSION, MAX_DCW_POSITIVE, MAX_DFEE_POSITIVE, MIN_DCW_NEGATIVE, "
                        + "MIN_DFEE_NEGATIVE, R2R_ADWISE_COUNT, R2R_ERROR_COUNT, R2R_WARNING_COUNT, USER_ROLE_ID, T_GROUPING_RESULTS_ID, MAX_DCW_POS_REF, "
                        + "MAX_DFEE_POS_REF, MIN_DCW_NEG_REF, MIN_DFEE_NEG_REF"
                        + ", MAX_DCW_CARE_POSITIVE,  MIN_DCW_CARE_NEGATIVE, MAX_DCW_CARE_POS_REF, MIN_DCW_CARE_NEG_REF) "
                        + "SELECT ID, "
                        + currentDate() + ", "
                        + (userId == null ? "null" : String.valueOf(userId)) + ", "
                        + currentDate() + ", "
                        + (userId == null ? "null" : String.valueOf(userId)) + ", "
                        + "VERSION, MAX_DCW_POSITIVE, MAX_DFEE_POSITIVE, MIN_DCW_NEGATIVE, MIN_DFEE_NEGATIVE, R2R_ADWISE_COUNT, "
                                + "R2R_ERROR_COUNT, R2R_WARNING_COUNT, USER_ROLE_ID, T_GROUPING_RESULTS_ID, MAX_DCW_POS_REF, MAX_DFEE_POS_REF, MIN_DCW_NEG_REF, MIN_DFEE_NEG_REF,"
                                + " MAX_DCW_CARE_POSITIVE,  MIN_DCW_CARE_NEGATIVE, MAX_DCW_CARE_POS_REF, MIN_DCW_CARE_NEG_REF"
                                + " FROM " + role2ResultTable);
                LOG.log(Level.INFO, "Inserted " + insertedR2r + " entries into T_ROLE_2_RESULT from " + role2ResultTable);

                distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Erzeuge neue Einträge in T_CASE_PEPP_GRADES...");
                if (pWriter.checkStopped()) {
                    return;
                }
                int insertedPeppGr = stmt.executeUpdate("INSERT INTO /*+ PARALLEL(T_CASE_PEPP_GRADES 5)*/ T_CASE_PEPP_GRADES (ID, CREATION_DATE, CREATION_USER, MODIFICATION_DATE, MODIFICATION_USER, VERSION, ONE_FEE, BASERATE, CW, DAYS, ACCOUNTED_FROM, GRADE, ACCOUNTED_TO, T_CASE_PEPP_ID) "
                        + "SELECT ID, "
                        + currentDate() + ", "
                        + (userId == null ? "null" : String.valueOf(userId)) + ", "
                        + currentDate() + ", "
                        + (userId == null ? "null" : String.valueOf(userId)) + ", "
                        + "VERSION, ONE_FEE, BASERATE, CW, DAYS, ACCOUNTED_FROM, GRADE, ACCOUNTED_TO, T_CASE_PEPP_ID FROM " + casePeppGradesTable);
                LOG.log(Level.INFO, "Inserted " + insertedPeppGr + " entries into T_CASE_PEPP_GRADES from " + casePeppGradesTable);

                distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "ERzeuge die Eintrage in T_CASE_DRG_CARE_GRADES...");
                if (pWriter.checkStopped()) {
                    return;
                }
               int insertedCareGr = stmt.executeUpdate("INSERT INTO /*+ PARALLEL(T_CASE_DRG_CARE_GRADES 5)*/ T_CASE_DRG_CARE_GRADES (ID, CREATION_DATE, CREATION_USER, MODIFICATION_DATE, MODIFICATION_USER, VERSION,"
                       + " CARE_BASERATE, CARE_CW_DAY, CARE_DAYS, ACCOUNTED_FROM, SORT_INDEX, ACCOUNTED_TO, T_CASE_DRG_ID) "
                        + "SELECT ID, "
                        + currentDate() + ", "
                        + (userId == null ? "null" : String.valueOf(userId)) + ", "
                        + currentDate() + ", "
                        + (userId == null ? "null" : String.valueOf(userId)) + ", "
                        + "VERSION, CARE_BASERATE, CARE_CW_DAY, CARE_DAYS, ACCOUNTED_FROM, SORT_INDEX, ACCOUNTED_TO, T_CASE_DRG_ID FROM " + caseDrgCareGradesTable);
                LOG.log(Level.INFO, "Inserted " + insertedCareGr + " entries into T_CASE_DRG_CARE_GRADES from " + caseDrgCareGradesTable);

                distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Lösche Eintrag in T_BATCH_RESULT...");
                if (pWriter.checkStopped()) {
                    return;
                }
 
                int deletedBatchResult = stmt.executeUpdate("DELETE FROM /*+ PARALLEL(T_BATCH_RESULT 5)*/ T_BATCH_RESULT  WHERE MODEL_ID_EN = '" + pParameter.getModel().name() + "'");
                LOG.log(Level.INFO, "Deleted " + deletedBatchResult + " entries from T_BATCH_RESULT");
                //stmt.executeUpdate("INSERT INTO T_BATCH_RESULT ..."); //TBD

                distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Lösche Eintrag in T_BATCH_RESULT_2_ROLE...");
                if (pWriter.checkStopped()) {
                    return;
                }
                int deletedBatchResult2Role = stmt.executeUpdate("DELETE FROM /*+ PARALLEL(T_BATCH_RESULT_2_ROLE 5)*/ T_BATCH_RESULT_2_ROLE WHERE T_BATCH_RESULT_ID NOT IN (SELECT ID FROM T_BATCH_RESULT)");
                LOG.log(Level.INFO, "Deleted " + deletedBatchResult2Role + " entries from T_BATCH_RESULT_2_ROLE");

                distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Lösche Eintrag in T_BATCH_CHECK_RESULT...");
                if (pWriter.checkStopped()) {
                    return;
                }
                int deletedBatchCheckResult = stmt.executeUpdate("DELETE FROM /*+ PARALLEL(T_BATCH_CHECK_RESULT 5)*/ T_BATCH_CHECK_RESULT WHERE T_BATCH_RESULT_2_ROLE_ID NOT IN (SELECT ID FROM T_BATCH_RESULT_2_ROLE) ");
                LOG.log(Level.INFO, "Deleted " + deletedBatchCheckResult + " entries from T_BATCH_CHECK_RESULT");
                //stmt.executeUpdate("INSERT INTO T_BATCH_RESULT ..."); //TBD

                distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Erzeuge neue Eintrag in T_BATCH_RESULT...");
                if (pWriter.checkStopped()) {
                    return;
                }
                final TBatchResult batchResult = pBatchResult.get();
                final long batchResultId = getNextId("T_BATCH_RESULT_SQ");
                int insertedBatchResult = stmt.executeUpdate("INSERT INTO /*+ PARALLEL(T_BATCH_RESULT 5)*/ T_BATCH_RESULT (ID, CREATION_DATE, CREATION_USER, MODIFICATION_DATE, MODIFICATION_USER, VERSION, BATCH_AUX9_COUNT, BATCH_AUXD_COUNT, BATCH_CARE_DAYS_SUM, BATCHRES_CASE_COUNT, BATCH_CASE_INTENSIV_COUNT, BATCH_CW_CATALOG_SUM, BATCH_CW_EFFECTIV_SUM, BATCH_DAY_CARE_COUNT, BATCH_DEAD_COUNT, BATCH_ERR_DRG_COUNT, BATCHRES_GROUPED_COUNT, BATCHRES_HTP_COUNT, BATCHRES_IS_LOCAL_FL, BATCH_LOS_INTENSIV_SUM, BATCH_LTP_COUNT, BATCH_NALOS_SUM, BATCH_PCCL_SUM, BATCH_TRASF_COUNT, MODEL_ID_EN, RULES_COUNT) "
                        + "SELECT " + batchResultId + " ID, " + currentDate() + " CREATION_DATE, " + (userId == null ? "NULL" : String.valueOf(userId)) + " CREATION_USER, "
                        + currentDate() + " MODIFICATION_DATE, "
                        + (userId == null ? "NULL" : String.valueOf(userId)) + " MODIFICATION_USER, "
                        + "0 VERSION, " + batchResult.getBatchresAux9Count() + " BATCH_AUX9_COUNT, " + batchResult.getBatchresAuxdCount() + " BATCH_AUXD_COUNT, " + batchResult.getBatchresCareDaysSum() + " BATCH_CARE_DAYS_SUM, " + batchResult.getBatchresCaseCount() + " BATCHRES_CASE_COUNT, " + batchResult.getBatchresCaseIntensivCount() 
                        + " BATCH_CASE_INTENSIV_COUNT, " + batchResult.getBatchresCwCatalogSum() 
                        + " BATCH_CW_CATALOG_SUM, " + batchResult.getBatchresCwEffectivSum() 
                        + " BATCH_CW_EFFECTIV_SUM, " + batchResult.getBatchresDayCareCount() + " BATCH_DAY_CARE_COUNT, " + batchResult.getBatchresDeadCount()
                        + " BATCH_DEAD_COUNT, " + batchResult.getBatchresErrDrgCount() + " BATCH_ERR_DRG_COUNT, " + batchResult.getBatchresGroupedCount() 
                        + " BATCHRES_GROUPED_COUNT, " + batchResult.getBatchresHtpCount() + " BATCHRES_HTP_COUNT, 1 BATCHRES_IS_LOCAL_FL, " 
                        + batchResult.getBatchresLosIntensivSum() + " BATCH_LOS_INTENSIV_SUM, " + batchResult.getBatchresLtpCount() 
                        + " BATCH_LTP_COUNT, " + batchResult.getBatchresNalosSum() + " BATCH_NALOS_SUM, " + batchResult.getBatchresPcclSum() 
                        + " BATCH_PCCL_SUM, " + batchResult.getBatchresTransfCount() + " BATCH_TRASF_COUNT, '" + pParameter.getModel().name() 
                        + "' MODEL_ID_EN, "
                        + batchResult.getRulesCount() + "  RULES_COUNT "
                         + (isOracle ? "FROM DUAL" : "")
                );
                LOG.log(Level.INFO, "Inserted " + insertedBatchResult + " entries into T_BATCH_RESULT");
                batchResult.setId(batchResultId);
//TODO: batch parameter
                final long groupParameterId =  getNextId("T_BATCH_GROUP_PARAMETER_SQ");
                qry = "INSERT  INTO /*+ PARALLEL(T_BATCH_GROUP_PARAMETER 5)*/ T_BATCH_GROUP_PARAMETER (ID, CREATION_DATE, CREATION_USER, MODIFICATION_DATE, MODIFICATION_USER, VERSION, " +
                       "	ADMISSION_DATE_FROM, ADMISSION_DATE_TO, " +
                       "	DETAILS_FILTER_EN," +
                       "	DISCHARGE_DATE_FROM, DISCHARGE_DATE_TO, " +
                        "	DO_ACTUAL_ROLE_ONLY_FL, "
                        +       "DO_CARE_DATA_FL," +
                        "	DO_DEPARTMENT_GR_FL, " +
                        "	DO_HISTORY_FL," +
                        "	DO_LABOR_FL," +
                        "	DO_MED_AND_REMEDIES_FL," +
                        "	DO_RULES_FL," +
                        "	DO_RULES_SIMUL_FL," +
                        "	DO_SIMULATE_FL," +
                        "	DO_WARD_GR_FL," +
                        "	DO_SUPPL_FFEES_FL," +
                        "	GROUPED_FL," +
                        "       DO_USE_ALL_RULES_FL, " +
                        
                        "	T_BATCH_RESULT_ID) "
                        + "  VALUES ("
                        + groupParameterId +","  + currentDate() +","+ (userId == null ? "NULL" : String.valueOf(userId)) +","+  currentDate()+"," + (userId == null ? "NULL" : String.valueOf(userId)) + ", 0, "
                        + date2string(pParameter.getAdmissionDateFrom())
                         +","+ date2string(pParameter.getAdmissionDateUntil())
                         +", '"+  pParameter.getDetailsFilter().getId()
                         +"'," +  date2string(pParameter.getDischargeDateFrom())
                         +","+  date2string(pParameter.getDischargeDateUntil())
                         +","+  StrUtils.toStrAsNumber(pParameter.isDo4actualRoleOnly())
                         +","+  StrUtils.toStrAsNumber(pParameter.isDoCareData())
                         +","+  StrUtils.toStrAsNumber(pParameter.isDoDepartmentGrouping())
                         +","+  StrUtils.toStrAsNumber(pParameter.isDoHistoryCases())
                         +","+  StrUtils.toStrAsNumber(pParameter.isDoLabor())
                         +","+  StrUtils.toStrAsNumber(pParameter.isDoMedAndRemedies())
                         +","+  StrUtils.toStrAsNumber(pParameter.isDoRules())
                         +","+  StrUtils.toStrAsNumber(pParameter.isDoRulesSimulate())
                         +","+  StrUtils.toStrAsNumber(pParameter.isDoSimulate())
                         +","+  StrUtils.toStrAsNumber(pParameter.isDoStationsgrouping())
                         +","+  StrUtils.toStrAsNumber(pParameter.isDoSupplementaryFees())
                         +","+  StrUtils.toStrAsNumber(pParameter.isGrouped())
                         +","+  StrUtils.toStrAsNumber(pParameter.getRuleIds() == null || pParameter.getRuleIds().isEmpty())
                         +","+  batchResultId
                        + ")" ;
                LOG.log(Level.INFO, qry);
                 int insertedBatchParameter = stmt.executeUpdate(qry);
                LOG.log(Level.INFO, "Inserted " + insertedBatchParameter + " entries into T_BATCH_GROUP_PARAMETER");
                 
                distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Erzeuge neue Einträge in T_BATCH_RESULT_2_ROLE...");
                if (pWriter.checkStopped()) {
                    return;
                }
                for (TBatchResult2Role role : new HashSet<>(batchResult.getBatchres2role())) {
                    final long batchResult2RoleId = getNextId("T_BATCH_RESULT_2_ROLE_SQ");
                    qry = "INSERT INTO /*+ PARALLEL(T_BATCH_RESULT_2_ROLE 5)*/ T_BATCH_RESULT_2_ROLE "
                            + "(ID, CREATION_DATE, CREATION_USER, MODIFICATION_DATE, MODIFICATION_USER, VERSION, B2R_MAX_DCW_POS_SUM, B2R_MAX_DFEE_POS_SUM, B2R_MIN_DCW_NEG_SUM, B2R_MIN_DFEE_NEG_SUM, ROLE_ID, T_BATCH_RESULT_ID) "
                            + "SELECT " + batchResult2RoleId + " ID, T_BATCH_RESULT.CREATION_DATE, T_BATCH_RESULT.CREATION_USER, T_BATCH_RESULT.MODIFICATION_DATE, T_BATCH_RESULT.MODIFICATION_USER, T_BATCH_RESULT.VERSION, "
                            + role.getB2rMaxDcwPosSum() + " B2R_MAX_DCW_POS_SUM, "
                            + role.getB2rMaxDfeePosSum() + " B2R_MAX_DFEE_POS_SUM, "
                            + role.getB2rMinDcwNegSum() + " B2R_MIN_DCW_NEG_SUM, "
                            + role.getB2rMinDfeeNegSum() + " B2R_MIN_DFEE_NEG_SUM, "
                            + role.getRoleId() + " ROLE_ID,"
                            + " T_BATCH_RESULT.ID "
                            + "FROM T_BATCH_RESULT WHERE T_BATCH_RESULT.ID = " + batchResultId;
               LOG.log(Level.FINE, qry);

                    int insertedBatchResult2Role = stmt.executeUpdate(qry);
                    LOG.log(Level.INFO, "Inserted " + insertedBatchResult2Role + " entries into T_BATCH_RESULT_2_ROLE");
                    role.setId(batchResult2RoleId);
                    for (TBatchCheckResult batchCheckRes2role : new HashSet<>(role.getBatchCheckResult())) {
                        qry = "INSERT INTO /*+ PARALLEL(T_BATCH_CHECK_RESULT 5)*/ T_BATCH_CHECK_RESULT "
                                + "(ID, CREATION_DATE, CREATION_USER, MODIFICATION_DATE, MODIFICATION_USER, VERSION, BCHECKRES_ADVICE_COUNT, BCHECKRES_ERR_COUNT, BCHECKRES_WARN_COUNT, BCHKRES_RULE_TYPE, T_BATCH_RESULT_2_ROLE_ID) "
                                + "SELECT " + nextSqVal("T_BATCH_CHECK_RESULT_SQ")
                                + " ID, T_BATCH_RESULT_2_ROLE.CREATION_DATE, T_BATCH_RESULT_2_ROLE.CREATION_USER, T_BATCH_RESULT_2_ROLE.MODIFICATION_DATE, T_BATCH_RESULT_2_ROLE.MODIFICATION_USER, T_BATCH_RESULT_2_ROLE.VERSION, "
                                + batchCheckRes2role.getBatchresAdviceCount() + " BCHECKRES_ADVICE_COUNT, "
                                + batchCheckRes2role.getBchechresErrCount() + " BCHECKRES_ERR_COUNT, "
                                + batchCheckRes2role.getBchechresWarnCount() + " BCHECKRES_WARN_COUNT, '"
                                + batchCheckRes2role.getBcheckresRuleType() + "' BCHKRES_RULE_TYPE, "
                                + batchResult2RoleId + " T_BATCH_RESULT_2_ROLE_ID "
                                + " FROM T_BATCH_RESULT_2_ROLE WHERE T_BATCH_RESULT_2_ROLE.ID = " + batchResult2RoleId;
               LOG.log(Level.FINE, qry);

                        int insertedBatchCheckRes2role = stmt.executeUpdate(qry);
                        LOG.log(Level.INFO, "Inserted {0} entries into T_BATCH_CHECK_RESULT", insertedBatchCheckRes2role);

                    }
                }
                // update t_case_details
                distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Aktualisiere Verweildauer in T_CASE_DETAILS...");
                if (pWriter.checkStopped()) {
                    return;
                }
                qry = "UPDATE T_CASE_DETAILS SET"
                        + " MODIFICATION_DATE = " + currentDate() + ", "
                        + "MODIFICATION_USER = " + (userId == null ? "null" : String.valueOf(userId)) + ", "
                        + "LOS = (SELECT LOS FROM "
                        + caseDetailsLosTable
                        + " WHERE T_CASE_DETAILS.ID = " + caseDetailsLosTable + ".ID  )"
                        + " WHERE EXISTS (SELECT 1 FROM " + caseDetailsLosTable + " WHERE T_CASE_DETAILS.ID = " + caseDetailsLosTable + ".ID  )";
                LOG.log(Level.FINE, qry);
                int updatedCaseDetailsLos = stmt.executeUpdate(qry);

                LOG.log(Level.INFO, "Updated {0} entries in T_CASE_CASE_DETAILS from {1}", new Object[]{updatedCaseDetailsLos, caseDetailsLosTable});

                distributionProgressCb.execute(executionId, step.incrementAndGet(), maxSteps, "Persistiere Ergebnisse...");
                if (pWriter.checkStopped()) {
                    return;
                }

                if (!connection.getAutoCommit()) {
                    connection.commit();
                }
//                connection.setAutoCommit(true);
                //stmt.executeUpdate("INSERT INTO T_BATCH_RESULT ..."); //TBD
//            stmt.executeBatch();
            }
//            } catch (SQLException ex) {
//                LOG.log(Level.SEVERE, null, ex);
//            }
        } finally {
//            try {
//                connection.setAutoCommit(true);
//            } catch (SQLException ex) {
//                LOG.log(Level.FINEST, "cannot execute setAutoCommit(true)", ex);
//            }

            distributionProgressCb.execute(executionId, maxSteps - 1, maxSteps, "Lösche die Tabellen für die temporären Grouping-Ergebnisse...");
            LOG.log(Level.INFO, "delete temporary grouping result tables...");
            dropTempTables();

            distributionProgressCb.execute(executionId, maxSteps, maxSteps, "Erneuere die Indizes...");
            LOG.log(Level.INFO, "rebuild indizes on grouping tables tables...");
            rebuildIndexes();
        }
    }
    
    private String date2string(Date dt){
        String ret = StrUtils.toStaticDate(dt, !isOracle);
        if(ret.length() == 0){
            return "NULL";            
        }
        return ret;
    }
//
//    protected String currentDate() {
//        return (isOracle ? "SYSDATE" : "GETDATE()");
//    }

    @Override
    protected void dropTempTables() throws SQLException {
        final String[] dropTables = new String[]{
            groupingResultsTable,
            caseIcdGroupedTable,
            caseOpsGroupedTable,
            caseSupplFeeTable,
            checkResultTable,
            role2CheckTable,
            role2ResultTable,
            casePeppGradesTable,
            caseDrgCareGradesTable,
            groupingResultsToDeleteTable,
            caseDetailsLosTable
        };
        dropTempTables(dropTables);
    }

    @Override
    protected void rebuildIndexes() {
        final String[][] indexes = new String[][]{
            new String[]{"T_CASE_ICD_GROUPED", "IDX_CASE_ICD_GRD4CASE_ICD_ID"},
            new String[]{"T_CASE_ICD_GROUPED", "IDX_CASE_ICD_GROUPED_GRPRES_ID"},
            new String[]{"T_CASE_OPS_GROUPED", "IDX_CASE_OPS_GROUPED4OPSC_ID"},
            new String[]{"T_CASE_OPS_GROUPED", "IDX_CASE_OPS_GROUPED_GRPRES_ID"},
            new String[]{"T_CASE_PEPP_GRADES", "IDX_CASE4PEPP_ID"},
            new String[]{"T_BATCH_CHECK_RESULT", "IDX_BAT_CHECK_RES4B2R_ID"},
            new String[]{"T_BATCH_RESULT_2_ROLE", "IDX_BATCH_RES_2_ROLE4BATCH_ID"},
            new String[]{"T_CHECK_RESULT", "IDX_CHECK_RESULT4GRPRES_ID"},
            new String[]{"T_ROLE_2_CHECK", "IDX_ROLE2CHECK4T_CHECK_RES_ID"},
            new String[]{"T_ROLE_2_RESULT", "IDX_ROLE_2_RESULT_GRPRES_ID"},
            new String[]{"T_CASE_MERGE_MAPPING", "IDX_C_MERGE_MAP4TCASE_ID"},
            new String[]{"T_CASE_MERGE_MAPPING", "IDX_C_MERGE_MAP4GRPRES_ID"},
            new String[]{"T_CASE_MERGE_MAPPING", "IDX_C_MERGE_MAP4MERG_MEMB_C_ID"},
            new String[]{"T_GROUPING_RESULTS", "IDX_GRP_RES4TCASE_DETAIL_ID"},
            new String[]{"T_GROUPING_RESULTS", "IDX_GROUPING_RESULTS4ICDC_ID"}, //            new String[] { "T_GROUPING_RESULTS", "IDX_GROUPING_RESULTS4MODEL_ID" },
        };
        rebuildIndexes(indexes);
    }

    @Override
    protected synchronized void destroy() {
        if (start.get()) {
            return;
        }
        LOG.log(Level.INFO, "close prepared statements...");
        LOG.log(Level.INFO, "close pstmtInsertGroupingResult...");
        closePstmt(pstmtInsertGroupingResult);
        LOG.log(Level.INFO, "close pstmtInsertGroupingResult...");
        closePstmt(pstmtInsertIcdGrouped);
        LOG.log(Level.INFO, "close pstmtInsertOpsGrouped...");
        closePstmt(pstmtInsertOpsGrouped);
        LOG.log(Level.INFO, "close pstmtInsertSupplFee...");
        closePstmt(pstmtInsertSupplFee);
        LOG.log(Level.INFO, "close pstmtInsertCheckResult...");
        closePstmt(pstmtInsertCheckResult);
        LOG.log(Level.INFO, "close pstmtInsertRole2Check...");
        closePstmt(pstmtInsertRole2Check);
        LOG.log(Level.INFO, "close pstmtInsertRole2Result...");
        closePstmt(pstmtInsertRole2Result);
        LOG.log(Level.INFO, "close pstmtInsertPeppGrades...");
        closePstmt(pstmtInsertPeppGrades);
        LOG.log(Level.INFO, "close pstmtInsertDrgCareGrades...");
        closePstmt(pstmtInsertDrgCareGrades);
        LOG.log(Level.INFO, "close pstmtUpdateCaseDetailsLos...");
        closePstmt(pstmtUpdateCaseDetailsLos);
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

//    protected synchronized void open() throws SQLException {
////      if (start.get()) {
////        return;
////      }
//        //destroy();
//        createPStatements();
//        start.set(false);
//    }
//
//    @Override
//    public void close() {
//        destroy();
//    }

    @Override
    protected void initialize() throws SQLException {
        executionId = System.currentTimeMillis();
        //String id = String.valueOf(executionId).substring(3);
        groupingResultsTable = "T_GROUPING_RES_" + executionId;
        caseIcdGroupedTable = "T_CASE_ICD_GRO_" + executionId;
        caseOpsGroupedTable = "T_CASE_OPS_GRO_" + executionId;
        caseSupplFeeTable = "T_CASE_SUPPL_F_" + executionId;
        checkResultTable = "T_CHECK_RESULT_" + executionId;
        role2CheckTable = "T_ROLE_2_CHECK_" + executionId;
        role2ResultTable = "T_ROLE_2_RESUL_" + executionId;
        casePeppGradesTable = "T_CASE_PEPP_GR_" + executionId;
        caseDrgCareGradesTable = "T_CS_CARE_G_" + executionId;
        caseDetailsLosTable = "T_CASE_DET_LOS_" + executionId;
        groupingResultsToDeleteTable = "T_GRPRES_DELE_" + executionId;
        LOG.log(Level.INFO, "Temporary grouping result tables: "
                + groupingResultsTable + ", "
                + caseIcdGroupedTable + ", "
                + caseOpsGroupedTable + ", "
                + caseSupplFeeTable + ", "
                + checkResultTable + ", "
                + role2CheckTable + ", "
                + role2ResultTable + ", "
                + casePeppGradesTable + ", "
                + caseDrgCareGradesTable + ", "
                + caseDetailsLosTable
        );
        //final String emptyGrpResIds = "CAST(NULL AS " + (isOracle ? "NUMBER(19,0)" : "BIGINT") + ") T_GROUPING_RESULTS_ID";
        //final String emptyCaseIds = "CAST(NULL AS " + (isOracle ? "NUMBER(19,0)" : "BIGINT") + ") T_CASE_ID";
        //final String emptyCaseDetailsId = "CAST(NULL AS " + (isOracle ? "NUMBER(19,0)" : "BIGINT") + ") T_CASE_DETAILS_ID";
        final String parallel = isOracle ? " NOLOGGING PARALLEL 5" : "";
        final String[] queries = new String[]{
            //"CREATE TABLE " + groupingResultsTable + " (ID NUMBER(19,0), GRPRES_TYPE_EN VARCHAR2(31 CHAR), GRPRES_CODE VARCHAR2(10 CHAR), GRPRES_GPDX VARCHAR2(25 CHAR), GRPRES_GROUP VARCHAR2(25 CHAR), GRPRES_GST VARCHAR2(25 CHAR), GRPRES_IS_AUTO_FL NUMBER(10,0), DAY_CARE_FL NUMBER(10,0), NEGOTIATED_FL NUMBER(10,0), GRPRES_PCCL NUMBER(10,0), MODEL_ID_EN VARCHAR2(25 CHAR), DRGC_CW_CORR FLOAT, DRGC_CW_EFFECTIV FLOAT, DRGC_DAYS_CORR NUMBER(5,0), DRGC_HTP NUMBER(10,0), DRGC_NEGO_FEE_2_DAY FLOAT, DRGC_NEGO_FEE_DAYS NUMBER(10,0), DRGC_PARTITION_EN VARCHAR2(25 CHAR), DRGC_TYPE_OF_CORR_EN VARCHAR2(25 CHAR), EXCEPTION_DRG_FL NUMBER(10,0), PEPPC_CW_EFFECTIV FLOAT, PEPPC_DAYS_INTENSIV NUMBER(10,0), PEPPC_DAYS_PERSCARE_ADULT NUMBER(10,0), PEPPC_DAYS_PERSCARE_INF NUMBER(10,0), PEPPC_PAY_CLASS NUMBER(10,0), PEPPC_PERSENTAGE_INTENS NUMBER(10,0), PEPPC_TYPE_EN VARCHAR2(20 CHAR), T_CASE_DETAILS_ID NUMBER(19,0), T_CASE_ICD_ID NUMBER(19,0), DRGC_LTP NUMBER(5,0), DRGC_ALOS FLOAT, DRGC_CW_CATALOG FLOAT, DRGC_CW_CORR_DAY FLOAT)",
            (isOracle ? "CREATE TABLE " + groupingResultsTable + parallel + " AS " : "") + "SELECT T_GROUPING_RESULTS.* "
                + (isSqlsrv ? "INTO " + groupingResultsTable : "") + " FROM T_GROUPING_RESULTS WHERE 1 = 2",
            (isOracle ? "CREATE TABLE " + caseIcdGroupedTable + parallel + " AS " : "") + "SELECT T_CASE_ICD_GROUPED.* " + (isSqlsrv ? "INTO " + caseIcdGroupedTable : "") + " FROM T_CASE_ICD_GROUPED WHERE 1 = 2",
            (isOracle ? "CREATE TABLE " + caseOpsGroupedTable + parallel + " AS " : "") + "SELECT T_CASE_OPS_GROUPED.* " + (isSqlsrv ? "INTO " + caseOpsGroupedTable : "") + " FROM T_CASE_OPS_GROUPED WHERE 1 = 2",
            (isOracle ? "CREATE TABLE " + caseSupplFeeTable + parallel + " AS " : "") + "SELECT T_CASE_SUPPL_FEE.* " + (isSqlsrv ? "INTO " + caseSupplFeeTable : "") + " FROM T_CASE_SUPPL_FEE WHERE 1 = 2",
            (isOracle ? "CREATE TABLE " + checkResultTable + parallel + " AS " : "") + "SELECT T_CHECK_RESULT.* " + (isSqlsrv ? "INTO " + checkResultTable : "") + " FROM T_CHECK_RESULT WHERE 1 = 2",
            (isOracle ? "CREATE TABLE " + role2CheckTable + parallel + " AS " : "") + "SELECT T_ROLE_2_CHECK.* " + (isSqlsrv ? "INTO " + role2CheckTable : "") + " FROM T_ROLE_2_CHECK WHERE 1 = 2",
            (isOracle ? "CREATE TABLE " + role2ResultTable + parallel + " AS " : "") + "SELECT T_ROLE_2_RESULT.* " + (isSqlsrv ? "INTO " + role2ResultTable : "") + " FROM T_ROLE_2_RESULT WHERE 1 = 2",
            (isOracle ? "CREATE TABLE " + casePeppGradesTable + parallel + " AS " : "") + "SELECT T_CASE_PEPP_GRADES.* " + (isSqlsrv ? "INTO " + casePeppGradesTable : "") + " FROM T_CASE_PEPP_GRADES WHERE 1 = 2",
            (isOracle ? "CREATE TABLE " + caseDrgCareGradesTable + parallel + " AS " : "") + "SELECT T_CASE_DRG_CARE_GRADES.* " + (isSqlsrv ? "INTO " + caseDrgCareGradesTable : "") + " FROM T_CASE_DRG_CARE_GRADES WHERE 1 = 2",
            (isOracle ? "CREATE TABLE " + caseDetailsLosTable + parallel + " AS " : "") + "SELECT T_CASE_DETAILS.ID,  T_CASE_DETAILS.LOS " + (isSqlsrv ? "INTO " + caseDetailsLosTable : "") + " FROM T_CASE_DETAILS WHERE 1 = 2",};
        try (Statement stmt = connection.createStatement()) {
            for (String query : queries) {
                stmt.execute(query);
            }
        }
    }

}
