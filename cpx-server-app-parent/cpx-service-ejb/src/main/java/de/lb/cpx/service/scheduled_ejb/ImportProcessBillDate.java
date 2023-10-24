/* 
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  urbach
 */

package de.lb.cpx.service.scheduled_ejb;

import de.lb.cpx.server.commons.enums.DbDriverEn;
import de.lb.cpx.service.information.DatabaseInfo;
import de.lb.cpx.service.ejb.LockServiceBean;
import de.lb.cpx.str.utils.StrUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Lock;
import static javax.ejb.LockType.READ;
import javax.ejb.Stateless;
import line.AbstractLine;
import line.Field;
import static line.Field.Type.STRING;
import util.CPxReaderSinglefile;
import util.DbBuilder;
/**
 *
 * @author urbach
 */
@Stateless
//@TransactionManagement(TransactionManagementType.BEAN)
//@TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
//@TransactionTimeout(value = 360, unit = TimeUnit.MINUTES)
//@ConcurrencyManagement(BEAN) //Bean-Managed Concurrency
@Lock(READ)
public class ImportProcessBillDate {

    private static final Logger LOG = Logger.getLogger(ImportProcessBillDate.class.getName());
    @EJB
    private LockServiceBean dBLockService;

    public static final Field IKZ = new Field(1, "IKZ", STRING, 20);
    public static final Field FALLNR = new Field(2, "FALLNR", STRING, 25);
    public static final Field FINALBILLDATE = new Field(3, "FINALBILLDATE", STRING, 15);

    private  Set<Field> setFields ;
    private final Comparator<Field> fieldComparator = new Comparator<>() {
            @Override
            public int compare(Field o1, Field o2) {
                return o1.compareTo(o2);
            }
        };
    private final TreeSet<Field> treeSetFields = new TreeSet(fieldComparator);
    
    private long mJobId = -1;
    private Connection dbConnection =  null;
    private final String imexTblNameBillDate = "IMEX_BILL_DATE";
    private String billDateDirectory = null;
    private boolean isOracleConnection = false;
    private boolean isSQLConnection = false;


    public void ImportProcessBillDate(){

    }

    public long getJobId() {
        return mJobId;
    }

    
        /**
     * starts GroupingJob and returns ExecutionId of the newly started job
     *
     * @param pExecutionId job id
     * @param groupingParameter batch grouping parameters
     * @return Exectution Id of the Job
     */
//    @TransactionTimeout(value = 360, unit = TimeUnit.MINUTES)    
    @Lock(READ)
    public long startBillDateImportJob(final long pExecutionId, final Connection sqlConn, final String importPath, final DatabaseInfo dbInfo) {
        final long jobId = pExecutionId; //ThreadLocalRandom.current().nextInt(min, max);
        mJobId = jobId;
        setDatabaseType(dbInfo);
        dbConnection = sqlConn;

        treeSetFields.add(IKZ);
        treeSetFields.add(FALLNR);
        treeSetFields.add(FINALBILLDATE);
        
        
        File fImportFile = new File(importPath);
        if(fImportFile != null && fImportFile.exists() && fImportFile.isFile()) {
            billDateDirectory = fImportFile.getPath();

            try {
                uploadImexTable();
                updateBillDateInTCase();
            } catch (Exception ex) {
                boolean e = false;
            }
        }
        
        
        return jobId;
    }

    private boolean uploadImexTable() throws InstantiationException, IllegalAccessException, IOException, SQLException, NoSuchFieldException, ParseException
    {
        boolean result = true;

       try (CPxReaderSinglefile cpxReader = CPxReaderSinglefile.getInstance(billDateDirectory))  {
            try (Statement stmt = dbConnection.createStatement()) {
                //1st step: drop all (temporary and non-temporary) imex tables
                    dropTable(stmt, imexTblNameBillDate, isOracleConnection);
//2nd step: create all temporary imex tables
                    createImexTable(stmt, imexTblNameBillDate, isOracleConnection);

//                getProgressor().sendProgress("Erzeuge IMEX-Tabellen");
                //3rd step: fill all temporary imex tables with data
                    try (BufferedReader br = cpxReader.getReader()) {
                        LOG.log(Level.INFO, "Start bulk insert into " + imexTblNameBillDate + "...");
//                        getProgressor().sendProgress("Fülle IMEX-Tabelle: " + lineEntity.imexTmpTableName);
                        uploadImexTable(br, dbConnection, stmt);
                        LOG.log(Level.INFO, "Bulk insert into " + imexTblNameBillDate + " successful!");
                    }
//                getProgressor().sendProgress("Erzeuge Indizes");
                //4th step: create temporary imex table indizes
                int updatedRows;
                updatedRows = stmt.executeUpdate(String.format("CREATE INDEX IDX_IMEX_BILL_DATE_IKZ ON " + imexTblNameBillDate + " (IKZ) %s", (isOracleConnection ? "NOLOGGING PARALLEL 5" : "")));
                updatedRows = stmt.executeUpdate(String.format("CREATE INDEX IDX_IMEX_BILL_DATE_CASE_NR ON " + imexTblNameBillDate + " (FALLNR) %s", (isOracleConnection ? "NOLOGGING PARALLEL 5" : "")));
            }
       }

       
        
        return result;
    }

    protected void dropTable(final Statement pStmt, final String pTableName, final boolean pIsOracle) throws IOException, SQLException, ParseException {
        pStmt.execute(DbBuilder.getTableDropSilent(pTableName, pIsOracle));
    }

    protected void createImexTable(final Statement pStmt, final String pTableName, final boolean pIsOracle) throws IOException, SQLException, ParseException, IllegalArgumentException, IllegalAccessException {
        
        pStmt.execute(DbBuilder.getTmpImexTableDefinition(pTableName, treeSetFields, pIsOracle));
    }

       private void setDatabaseType(final DatabaseInfo dbInfo){
           if(dbInfo != null) {
               final DbDriverEn driver = dbInfo.getDriver();
               if(driver != null) {
                   if(driver.isSqlsrv()) {
                       isSQLConnection = true;
                   } else if(driver.isOracle()) {
                       isOracleConnection = true;
                   }
               }
           }
       }

    protected void uploadImexTable(final BufferedReader pBr, final Connection pConnection, final Statement pStmt) throws IOException, SQLException, ParseException {
        /*
      String imexTmpTableName;
      if (pDatabaseName == null || pDatabaseName.trim().isEmpty()) {
        imexTmpTableName = DbBuilder.getImexTmpTableName(pLineEntity.imexTmpTableName);
      } else {
        imexTmpTableName = DbBuilder.getImexTmpTableName(pDatabaseName, pLineEntity.imexTmpTableName);
      }
         */
        String tableName = imexTblNameBillDate;
        //pStmt.execute(DbBuilder.getTableDropSilent(imexTmpTableName));
        //pStmt.execute(DbBuilder.getTmpImexTableDefinition(imexTmpTableName, pLineEntity.fieldSet));

        try (PreparedStatement prepStmt = pConnection.prepareStatement(DbBuilder.getImexPreparedStatement(tableName, treeSetFields))) {
            //pConnection.setAutoCommit(false);

            int counter = 0;
            Field[] fieldSet = treeSetFields.toArray(new Field[treeSetFields.size()]);

            String line = pBr.readLine(); //skip 1st line (headline)
            while ((line = pBr.readLine()) != null) {
                counter++;
                String[] sa = AbstractLine.splitLine(line);
                for (int i = 0; i < sa.length; i++) {
                    Field field = fieldSet[i];
                    String value = sa[i];
                    int idx = i + 1;
                    if (value.isEmpty()) {
                        int jdbcType = field.getType().getJdbcType();
                        if (value.isEmpty() && jdbcType == 16) {
                            //BOOLEAN and value==""
                            prepStmt.setNull(idx, Types.VARCHAR); //Probably you have to chose the right data type for some database systems here!
                        } else {
                            prepStmt.setNull(idx, jdbcType); //Probably you have to chose the right data type for some database systems here!
                        }
                        continue;
                    }
                    switch (field.getType()) {
                        case STRING:
                            prepStmt.setString(idx, value);
                            //continue;
                            break;
                        case INT:
                            prepStmt.setInt(idx, Integer.valueOf(value));
                            //continue;
                            break;
                        case FLOAT:
                            prepStmt.setFloat(idx, Float.valueOf(value));
                            //continue;
                            break;
                        case MONEY:
                            prepStmt.setFloat(idx, Float.valueOf(value));
                            //continue;
                            break;
                        case DATETIME:
                            prepStmt.setTimestamp(idx, Field.parseTimestamp(value));
                            //continue;
                            break;
                        case DATE:
                            prepStmt.setDate(idx, Field.parseDate(value));
                            //continue;
                            break;
                        case TIME:
                            prepStmt.setDate(idx, Field.parseTime(value));
                            //continue;
                            break;
                        case BOOLEAN:
                            prepStmt.setBoolean(idx, StrUtils.toBool(value));
                            //continue;
                            break;
                        case LONG:
                            prepStmt.setLong(idx, StrUtils.toLong(value));
                            //continue;
                            break;
                        default:
                            throw new IllegalArgumentException("Cannot decide which datatype to use for prepared statement: " + field.getType().name());
                    }
                }
                prepStmt.addBatch();
                if (counter % 250000 == 0) {
                    prepStmt.executeBatch();
                }
            }

            prepStmt.executeBatch();
        }
    }

    private boolean updateBillDateInTCase() throws SQLException{

        String dateConvert = "";
        if (!isOracleConnection) {
            dateConvert = "CONVERT(DATETIME, "+FINALBILLDATE.getName()+", 102)";
        } else {
            dateConvert = "TO_DATE(" + FINALBILLDATE.getName()+", 'YYYY:MM:DD')";
        }

        try (Statement stmt = dbConnection.createStatement()) {
            stmt.executeUpdate(String.format("UPDATE T_CASE SET CS_BILLING_DATE = (SELECT " + dateConvert + " FROM "+imexTblNameBillDate+" "
                    + "WHERE "+imexTblNameBillDate+"."+IKZ.getName()+"=T_CASE.CS_HOSPITAL_IDENT AND "+imexTblNameBillDate+"."+FALLNR.getName()+"=T_CASE.CS_CASE_NUMBER "
                    + "AND "+FINALBILLDATE.getName()+" IS NOT NULL) WHERE CS_BILLING_DATE IS NULL"));
        }

        return true;
    }
}
