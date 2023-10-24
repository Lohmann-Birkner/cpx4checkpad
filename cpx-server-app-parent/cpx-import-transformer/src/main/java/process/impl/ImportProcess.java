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
 *    2016  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package process.impl;

import de.lb.cpx.server.commons.enums.DbDriverEn;
import de.lb.cpx.server.config.CpxServerConfig;
import de.lb.cpx.service.information.ConnectionString;
import de.lb.cpx.shared.dto.job.config.CpxDatabaseBasedImportJob;
import de.lb.cpx.shared.dto.job.config.CpxExternalSystemBasedJobImportConfig;
import de.lb.cpx.shared.dto.job.config.CpxJobImportConfig;
import static de.lb.cpx.str.utils.StrUtils.*;
import distributor.impl.Constraints;
import distributor.impl.CpxDistributor;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Callback;
import javax.batch.runtime.BatchStatus;
import module.ImportModuleI;
import module.impl.ImportConfig;
import process.ImportProcessI;
import progressor.ProgressorI;
import progressor.impl.Progressor;
import transformer.CpxTransformerI;
import transformer.impl.AbstractCpxTransformerCallback;
import transformer.impl.TransformResult;
import uploader.impl.CpxUploader;

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
/**
 * Import process (overall class for all imports like SAP or P21)
 *
 * @author niemeier
 * @param <T> config type
 */
public abstract class ImportProcess<T extends ImportModuleI<? extends CpxJobImportConfig>> implements ImportProcessI<T> {
    //public final static boolean isOracle = false; //false = Microsoft SQL Server

    public static final boolean REMOVE_CONSTRAINTS = false; //Set true to gain much more better performance when you overwrite hospital cases and if there are no indexes!
    public static final boolean LOG_QUERIES = true; //Set true to see all the SQL stuff!
    static final Logger LOG = Logger.getLogger(ImportProcess.class.getName());
    //private AbstractCpxTransformerCallback<T> transformerFactory;
    //private long mExecutionId = -1;
    private TransformResult mTransformResult = null;
    //private long mStartTime = System.currentTimeMillis();
    private ImportConfig<?> mImportConfig = null;
    private long mMeanTime = 0L;
    private ConnectionSettings mCommonDb = null;
    private ConnectionSettings mCaseDb = null;
    public static final ConnectionString COMMON_CONN = new ConnectionString(CpxServerConfig.COMMONDB);
    private final Progressor mProgressor = new Progressor(new Callback<UpdateImportsEntryDto, Integer>() {
        @Override
        public Integer call(UpdateImportsEntryDto pDto) {
            try {
                return updateImportsEntry(mImportConfig, pDto);
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, "Cannot update imports entry", ex);
                return 0;
            }
        }
    });
//    private CpxTransformerI transformer;

//    private void setTransformer(final CpxTransformerI pTransformer) {
//        transformer = pTransformer;
//    }
//    
//    private CpxTransformerI getTransformer() {
//        return transformer;
//    }
    private final class ConnectionSettings {

        public final Connection mConnection;
//        public final boolean mIsOracle;
//        public final boolean mIsSqlSrv;
        public final DbDriverEn driver;
        public final String mConnectionUrl;
        public final String mConnectionDatabase; //DATABASE

        public ConnectionSettings(final Connection pConnection) {
            if (pConnection == null) {
                throw new IllegalArgumentException("No database connection was passed!");
            }

            final String connectionUrl = getConnectionUrl(pConnection);
            //final boolean isOracle = isOracle(connectionUrl);
            driver = DbDriverEn.findByConnectionUrl(connectionUrl);
            final String connectionDatabase = getConnectionDatabase(pConnection);

            //final String connectionString = pConnection.getMetaData().getURL();
            if (connectionUrl.isEmpty()) {
                throw new IllegalArgumentException("This database connection has no connection url!");
            }

            mConnection = pConnection;
            mConnectionUrl = toStr(connectionUrl.contains(";") ? connectionUrl.substring(0, connectionUrl.indexOf(';')) : connectionUrl); //Chop off!
            //mIsOracle = isOracle;
            //mIsSqlSrv = !isOracle;
            mConnectionDatabase = connectionDatabase;
        }

        public String getDatabase() {
            return mConnectionDatabase + " on " + mConnectionUrl;
        }

        public boolean isOracle() {
            return driver == null ? false : driver.isOracle();
        }

        public boolean isSqlsrv() {
            return driver == null ? false : driver.isSqlsrv();
        }
    }

//    /**
//     * Method to import from P21 directory to target database
//     *
//     * @param args Arguments
//     * @author Dirk Niemeier
//     * @throws Exception Exception
//     */
//    public static void main(String[] args) throws Exception {
//        CpxSystemProperties.useUseDirAsCpxHome(true);
//        LOG.log(Level.INFO, "P21-Import runs in this directory: " + CpxSystemProperties.getInstance().getCpxHome());
//
//        final String inputDirectory = (args != null && args.length >= 1) ? args[0].trim() : "";
//        final String caseDb = (args != null && args.length >= 2) ? args[1].trim() : "";
//        final String importModeTmp = (args != null && args.length >= 3) ? args[2].trim() : "";
//
//        if (inputDirectory.isEmpty()) {
//            throw new IllegalArgumentException("No input directory (where are you're P21 files are lieing) passed (arg 1)!");
//        }
//
//        if (caseDb.isEmpty()) {
//            throw new IllegalArgumentException("No case database (e.g. dbsys1:CPX) passed (arg 2)!");
//        }
//
//        final ImportMode importMode = ImportMode.getImportMode(importModeTmp, ImportMode.Version);
//
//        LOG.log(Level.INFO, "Will import P21 files from: " + inputDirectory);
//        LOG.log(Level.INFO, "Will import to database: " + caseDb);
//        LOG.log(Level.INFO, "Will use this import mode: " + importMode.name);
//
//        final ImportConfig<P21> importConfig = new ImportConfig<>(caseDb, new P21(inputDirectory), importMode);
//        ImportProcess.start(importConfig, caseDb);
//    }
    public static Connection createTestConnection(final DbConfigTest pDbConfig) throws SQLException, IllegalArgumentException {
        if (pDbConfig == null) {
            throw new IllegalArgumentException("You have not passed a DbConfig object!");
        }
        //Connection connection = DriverManager.getConnection(pDbConfig.getUrl(), pDbConfig.getUser(), pDbConfig.getPassword());
        //return connection;
        return pDbConfig.createConnection();
    }

//    public static void start(final ImportConfig<?> pImportConfig, final String pConnCaseDb) throws IllegalArgumentException, IllegalStateException, InvocationTargetException, NoSuchFieldException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, ParseException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchMethodException, InterruptedException {
//        start(pImportConfig, CpxServerConfig.COMMONDB, pConnCaseDb);
//    }
    public void start(final ImportConfig<T> pImportConfig, /* final String pConnCommonDb, */ final String pConnCaseDb) throws IllegalArgumentException, IllegalStateException, InvocationTargetException, NoSuchFieldException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, ParseException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchMethodException, InterruptedException {
        //final String commonDb = pConnCommonDb == null ? "" : pConnCommonDb.trim();
        final String caseDb = pConnCaseDb == null ? "" : pConnCaseDb.trim();
        //final ConnectionString commonConn = new ConnectionString(commonDb);
        final ConnectionString caseConn = new ConnectionString(caseDb);
        start(pImportConfig, COMMON_CONN, caseConn);
    }

    public void start(final ImportConfig<T> pImportConfig, final ConnectionString pConnCommonDb, final ConnectionString pConnCaseDb) throws IllegalArgumentException, IllegalStateException, InvocationTargetException, NoSuchFieldException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, ParseException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchMethodException, InterruptedException {
        if (pConnCommonDb == null) {
            throw new IllegalArgumentException("No common database passed!");
        }
        if (pConnCaseDb == null) {
            throw new IllegalArgumentException("No case database passed!");
        }

        if (!pConnCommonDb.isValidCaseDb()) {
            throw new IllegalArgumentException("No valid common database passed: " + pConnCommonDb);
        }
        if (!pConnCaseDb.isValidCaseDb()) {
            throw new IllegalArgumentException("No valid case database passed: " + pConnCaseDb);
        }

        LOG.log(Level.INFO, "Try to connect to common database '" + pConnCommonDb.connectionString + "' and case database '" + pConnCaseDb.connectionString + "'...");
        final CpxServerConfig serverConfig = new CpxServerConfig();
        try (final Connection commonDbConnection = serverConfig.getJdbcConnection(pConnCommonDb.connectionString); final Connection caseDbConnection = serverConfig.getJdbcConnection(pConnCaseDb.connectionString)) {
            LOG.log(Level.INFO, "Hurray! Connection to common database '" + pConnCommonDb.connectionString + "' and case database '" + pConnCaseDb.connectionString + "' are established!");
            start(pImportConfig, commonDbConnection, caseDbConnection);
        }
    }

    public void start(final ImportConfig<T> pImportConfig, final DbConfigTest pConnCommonDb, final DbConfigTest pConnCaseDb) throws IllegalArgumentException, IllegalStateException, InvocationTargetException, NoSuchFieldException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, ParseException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchMethodException, InterruptedException {
        if (pConnCommonDb == null) {
            throw new IllegalArgumentException("No common database test config passed!");
        }
        if (pConnCaseDb == null) {
            throw new IllegalArgumentException("No case database test config passed!");
        }

        LOG.log(Level.INFO, "Try to connect to common database '" + pConnCommonDb + "' and case database '" + pConnCaseDb + "'...");
        try (final Connection commonDbConnection = createTestConnection(pConnCommonDb); final Connection caseDbConnection = createTestConnection(pConnCaseDb)) {
            LOG.log(Level.INFO, "Hurray! Connection to common database '" + pConnCommonDb + "' and case database '" + pConnCaseDb + "' are established!");
            start(pImportConfig, commonDbConnection, caseDbConnection);
        }
    }

    public void start(final ImportConfig<T> pImportConfig, final Connection pConnCommonDb, final Connection pConnCaseDb) throws IllegalArgumentException, IllegalStateException, InvocationTargetException, NoSuchFieldException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, ParseException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchMethodException, InterruptedException {
        try (final Connection commonDbConnection = pConnCommonDb; final Connection caseDbConnection = pConnCaseDb) {

            //ImportProcess importProcess = new ImportProcess();
//            if (pImportConfig != null) {
//                CpxTransformerI transformer = getTransformer(pImportConfig);
//                setTransformer(transformer);
//            }
            setCommonDbConnection(commonDbConnection);
            setCaseDbConnection(caseDbConnection);

            startImportProcess(pImportConfig);
        }
    }

    private long getNextImportsId() throws SQLException {
        final String sequenceQuery;
        if (isCommonDbOracle()) {
            sequenceQuery = "SELECT C_JOB_LOG_SQ.nextval FROM dual";
        } else {
            sequenceQuery = "SELECT NEXT VALUE FOR C_JOB_LOG_SQ";
        }
        try (Statement stmt = getCommonDbConnection().createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sequenceQuery)) {
                while (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        throw new SQLException("Cannot retrieve next Imports Sequence ID");
    }

    private int removeExpiredEntries() throws SQLException {
        final java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        final int minutesSinceLastUpdate = 360;
        cal.add(Calendar.MINUTE, -minutesSinceLastUpdate);
        final java.sql.Date expireDate = new java.sql.Date(cal.getTimeInMillis());
        final String query = "UPDATE C_JOB_LOG SET STATUS = ?, END_DATE = ?, MESSAGE = ? WHERE MODIFICATION_DATE < ? AND END_DATE IS NULL";
        try (PreparedStatement pstmt = getCommonDbConnection().prepareStatement(query)) {
            pstmt.setString(1, BatchStatus.ABANDONED.name());
            pstmt.setDate(2, currentDate);
            pstmt.setString(3, "Imports seems to be expired!");
            pstmt.setDate(4, expireDate);
            int updateRows = pstmt.executeUpdate();
            LOG.log(Level.INFO, updateRows + " entries in C_JOB_LOG were marked as expired");
            return updateRows;
        }
    }

    private long createImportsEntry(final ImportConfig<?> pImportConfig) throws SQLException {
        removeExpiredEntries();
        long newId = getNextImportsId();
        LOG.log(Level.INFO, "C_JOB_LOG.ID " + newId + " generiert");
        pImportConfig.setId(newId);
        final java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
        final String database = pImportConfig.getDatabase();
        final String query = "INSERT INTO C_JOB_LOG (ID, JOB_ID, CREATION_DATE, USER_NAME, DB, IMPORT_NAME, START_DATE, STATUS, INPUT_SOURCE) SELECT ?, ?, ?, ?, ?, ?, ?, ?, ? " + (isCommonDbOracle() ? " FROM DUAL " : "") + " WHERE NOT EXISTS (SELECT 1 FROM C_JOB_LOG C_IMP WHERE C_IMP.DB = ? AND C_IMP.END_DATE IS NULL)";
        try (PreparedStatement pstmt = getCommonDbConnection().prepareStatement(query)) {
            final String inputDirectory = pImportConfig.getInputDirectory();
            final CpxExternalSystemBasedJobImportConfig inputConfig = pImportConfig.getInputConfig();
            pstmt.setLong(1, pImportConfig.getId());
            pstmt.setLong(2, pImportConfig.getModule().getInputConfig().getId());
            pstmt.setDate(3, currentDate);
            pstmt.setString(4, System.getProperty("user.name")); //yes, this is windows user name, not CPX user name!
            pstmt.setString(5, database);
            pstmt.setString(6, pImportConfig.getModuleName());
            pstmt.setDate(7, currentDate);
            pstmt.setString(8, "STARTED");
            pstmt.setString(9, inputConfig == null ? inputDirectory : inputConfig.getName());
            pstmt.setString(10, database);
            int insertedRows = pstmt.executeUpdate();
            if (insertedRows < 1) {
                LOG.log(Level.SEVERE, "Was not able to create imports entry with id " + newId);
                final String message = "Auf der nachfolgenden Datenbank läuft bereits ein Import: " + database;
                getProgressor().sendFailure(message, null);
                throw new IllegalArgumentException(message + "\nUm das Problem manuell zu fixen führen Sie auf der CommonDB folgenden Befehl aus: UPDATE C_JOB_LOG SET END_DATE = " + (mImportConfig.isCommonDbOracle() ? "SYSDATE" : "GETDATE()") + " WHERE END_DATE IS NULL AND DB = '" + database + "';" + (mImportConfig.isCommonDbOracle() ? " COMMIT;" : ""));
            } else {
                if (insertedRows > 1) {
                    LOG.log(Level.WARNING, "Es wurden mehrere Einträge mit C_JOB_LOG.ID " + newId + " erzeugt! Das darf eigentlich nicht passieren...");
                } else {
                    LOG.log(Level.INFO, "Eintrag C_JOB_LOG.ID " + newId + " wurde erzeugt");
                }
            }
        }
        return newId;
    }

    private int finishImportsEntry(final ImportConfig<?> pImportConfig, final String pStatus) throws SQLException {
        final java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
        final String query = "UPDATE C_JOB_LOG SET MODIFICATION_DATE = ?, END_DATE = ?, NUM_OF_CASES = ?, NUM_OF_PATIENTS = ?, NUM_OF_IMPORTED_CASES = ?, NUM_OF_IGNORED_CASES = ?, STATUS = ? WHERE ID = ?";
        final String status = pStatus == null ? "" : pStatus.trim().toUpperCase();
        final long id = pImportConfig.getId();
        try (PreparedStatement pstmt = getCommonDbConnection().prepareStatement(query)) {
            pstmt.setDate(1, currentDate);
            pstmt.setDate(2, currentDate);
            if (pImportConfig.getNumberOfCases() == null) {
                pstmt.setNull(3, Types.VARCHAR);
            } else {
                pstmt.setInt(3, pImportConfig.getNumberOfCases());
            }
            if (pImportConfig.getNumberOfPatients() == null) {
                pstmt.setNull(4, Types.VARCHAR);
            } else {
                pstmt.setInt(4, pImportConfig.getNumberOfPatients());
            }
            if (pImportConfig.getNumberOfImportedCases() == null) {
                pstmt.setNull(5, Types.VARCHAR);
            } else {
                pstmt.setInt(5, pImportConfig.getNumberOfImportedCases());
            }
            if (pImportConfig.getNumberOfIgnoredCases() == null) {
                pstmt.setNull(6, Types.VARCHAR);
            } else {
                pstmt.setInt(6, pImportConfig.getNumberOfIgnoredCases());
            }
            pstmt.setString(7, status.isEmpty() ? "UNDEFINED" : status);
            pstmt.setLong(8, id);
            int changedRows = pstmt.executeUpdate();
            if (changedRows < 1) {
                LOG.log(Level.WARNING, "Was not able to finish imports entry with id " + id);
            } else {
                if (changedRows > 1) {
                    LOG.log(Level.WARNING, "Es wurden mehrere Einträge mit C_JOB_LOG.ID " + id + " finalisiert! Das darf eigentlich nicht passieren...");
                } else {
                    LOG.log(Level.INFO, "Eintrag C_JOB_LOG.ID " + id + " wurde finalisiert");
                }
            }
            return changedRows;
        }
        //throw new SQLException("Cannot update Import with ID " + pImportConfig.getId() + " and status " + pStatus);
    }

    private int updateImportsEntry(final ImportConfig<?> pImportConfig, final UpdateImportsEntryDto pDto) throws SQLException {
        if (pImportConfig == null || pImportConfig.getId() == null) {
            return 0;
        }
        final java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
        final String query = "UPDATE C_JOB_LOG SET MODIFICATION_DATE = ?, MESSAGE = ? WHERE ID = ?";
        final long id = pImportConfig.getId();
        try (PreparedStatement pstmt = getCommonDbConnection().prepareStatement(query)) {
            pstmt.setDate(1, currentDate);
            pstmt.setString(2, pDto.message);
            pstmt.setLong(3, id);
            int changedRows = pstmt.executeUpdate();
            if (changedRows < 1) {
                LOG.log(Level.WARNING, "Was not able to update imports entry with id " + id);
            } else {
                if (changedRows > 1) {
                    LOG.log(Level.WARNING, "Es wurden mehrere Einträge mit C_JOB_LOG.ID " + id + " aktualisiert! Das darf eigentlich nicht passieren...");
                } else {
                    LOG.log(Level.FINEST, "Eintrag C_JOB_LOG.ID " + id + " wurde aktualisiert");
                }
            }
            return changedRows;
        }
        //throw new SQLException("Cannot update Import with ID " + pImportConfig.getId() + " and status " + pStatus);
    }

//    public void setTransformerFactory(final CpxTransformerFactory pTransformerFactory) {
//        transformerFactory = pTransformerFactory;
//    }
//
//    public CpxTransformerFactory getTransformerFactory() {
//        return transformerFactory;
//    }
//    public CpxTransformerFactory instantiateTransformerClass() {
//        if (transformerFactory == null) {
//            LOG.log(Level.WARNING, "No transformer class defined!");
//            return null;
//        }
//        try {
//            return transformerFactory.getDeclaredConstructor().newInstance();
//        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
//            LOG.log(Level.SEVERE, "Cannot instantiate transformer class (does it have an public contructor without any arguments?)", ex);
//        }
//        return null;
//    }
    @Override
    public void startImportProcess(final ImportConfig<T> pImportConfig) {
        boolean success = true;
        try {
            getProgressor().setMaxPhases(5);
            getProgressor().setMaxSubPhases(3);
            getProgressor().incrementPhase();

            getProgressor().sendProgress("Import wird gestartet");
            if (pImportConfig == null) {
                throw new IllegalArgumentException("Dead end, because you did not pass an import configuration object!");
            }
            if (getCaseDbConnection() == null) {
                throw new IllegalArgumentException("Dead end, because you did not pass a jdbc connection object for the case database via setConnection!");
            }
            if (getCommonDbConnection() == null) {
                throw new IllegalArgumentException("Dead end, because you did not pass a jdbc connection object for the common database via setConnection!");
            }
            getProgressor().sendProgress("Konfiguration wird erstellt");
            mImportConfig = pImportConfig;
            //Store some params before we start to do some nice things
            pImportConfig.isCaseDbOracle(this.isCaseDbOracle());
            pImportConfig.isCommonDbOracle(this.isCommonDbOracle());
            pImportConfig.setStartTime(System.currentTimeMillis());
            mMeanTime = pImportConfig.getStartTime();

            getProgressor().sendProgress("Erzeuge Eintrag in C_JOB_LOG");
            createImportsEntry(pImportConfig);

            final String moduleType = pImportConfig.getModuleType();

            LOG.log(Level.INFO, "Import will start now with this configuration:\n"
                    + "   -> Module: " + pImportConfig.getModuleName() + (moduleType.isEmpty() ? "" : " (" + moduleType + ")") + "\n"
                    + "   -> (Target) Common database: " + getCommonDbDatabase() + " (" + (isCommonDbOracle() ? "Oracle" : isCommonDbSqlSrv() ? "Microsoft SQL Server" : "Unknown") + ")\n"
                    + "   -> (Target) Case database: " + getCaseDbDatabase() + " (" + (isCaseDbOracle() ? "Oracle" : isCaseDbSqlSrv() ? "Microsoft SQL Server" : "Unknown") + ")\n"
                    + "   -> Import mode (what to do with already existing hospital cases?): " + pImportConfig.getMode().modeName + " (" + pImportConfig.getMode().description + ")\n");
//                    + "   -> Output directory (for transformation and intermediate format): " + pImportConfig.getOutputDirectory());

            //mStartTime = System.currentTimeMillis();
            //sendStatusJobMessage(1, MAX_STEPS); 
            //Main Phase 1 (no database connection neccessary in this phase!)
            //CpxTransformerFactory transformer = new CpxTransformerFactory();
            AbstractCpxTransformerCallback<T> transformer = new AbstractCpxTransformerCallback<T>() {
                @Override
                public CpxTransformerI<T> getTransformer(ImportConfig<T> pImportConfig) throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, InterruptedException, NoSuchMethodException, InvocationTargetException, NoSuchAlgorithmException, SQLException {
                    getProgressor().sendProgress("Erzeuge Transformator");
                    return ImportProcess.this.getTransformer(pImportConfig);
                }
            };
            String inputPath = null;
            boolean doExport = true;
            boolean doImport = true;
            if(pImportConfig.getModule().getInputConfig().isDbImport()){
                CpxDatabaseBasedImportJob impJobConfig = (CpxDatabaseBasedImportJob)pImportConfig.getModule().getInputConfig();
                doExport = impJobConfig.isDoBackup();
                doImport = impJobConfig.isUseBackup();
                if( doExport||doImport){
                   inputPath =  impJobConfig.getBackupPath();
                   if(!checkDir(inputPath, doExport)){
                       LOG.log(Level.INFO, "cannot use on path{0}, we use default path", inputPath);

                       inputPath = null;
                       doExport = true;
                       doImport = true;
                   }
                }else{
                    doExport = true;
                    doImport = true;
                    
                }
            }

            if(inputPath != null){
//                LOG.log(Level.INFO, "we import data from the backup path: {0}", inputPath);
                
                pImportConfig.setOutputDirectory(inputPath);
//                mTransformResult = transformer.getDataFromPath(pImportConfig, inputPath);
            }
            LOG.log(Level.INFO, "   -> Output directory (for transformation and intermediate format): " + pImportConfig.getOutputDirectory() + "\n"
            + ((doExport && doImport)?"full process":(doExport?"do export only":"do import only")));     
            getProgressor().incrementPhase();
            getProgressor().sendProgress("Beginne mit der Verarbeitung der Quelldaten");
            
            if(doExport)
            {

                mTransformResult = transformer.transform(pImportConfig);
            
                if (mTransformResult != null && mTransformResult.hasExceptions()) {
                    int c = mTransformResult.getExceptions().size();
                    String msg = "Transformation ended with " + c + " exceptions";
                    Exception cause = new Exception("\r\n" + String.join("\r\n\r\n", mTransformResult.getExceptionStackTraces()));
                    IllegalStateException ex = new IllegalStateException(msg, cause);
                    //getProgressor().sendFailure(ex.getMessage() + "\r\n" + cause.getMessage(), ex);
                    throw ex;
                }
                //sendStatusJobMessage(2, MAX_STEPS); //Phase 2 of MAX_STEPS
                getProgressor().sendProgress("Transformation beendet: " + (mTransformResult == null ? "???" : mTransformResult.getPatientCount()) + " Patienten und " + (mTransformResult == null ? "???" : mTransformResult.getCaseCount()) + " Fälle gefunden"); //Phase 2 of MAX_STEPS
                logTime("Found " + (mTransformResult == null ? "???" : mTransformResult.getPatientCount()) + " patients and " + (mTransformResult == null ? "???" : mTransformResult.getCaseCount()) + " hospital cases in file");
            }
            if(doImport){
            //Okay, from now on you need a JDBC connection aiming your target database...
            //Main Phase 2 (here we start to upload our CPX-defined common format files into IMEX tables!)
                getProgressor().incrementPhase();
                getProgressor().setMaxSubPhases(21);

                getProgressor().sendProgress("Beginne mit dem Hochladen der transformierten Daten in die IMEX-Tabellen");
                logTime("Transformation in das CPX-Format abgeschlossen, starte Upload der IMEX-Dateien...");
                CpxUploader<T> uploader = new CpxUploader<>(getProgressor());
                uploader.uploadImex(pImportConfig, getCaseDbConnection());
                //sendStatusJobMessage(3, MAX_STEPS); //Phase 3 of MAX_STEPS
                getProgressor().sendProgress("Hochladen wurde beendet");

                logTime("Upload der IMEX-Dateien abgeschlossen, starte Verteilung der Daten");

                try {
                    //Drop foreign key constraints to get much more better performance (especially when you overwrite/delete hospital cases)
                    getProgressor().sendProgress("Lösche die Constraints in der Falldatenbank");
                    if (REMOVE_CONSTRAINTS) {
                        logTime("Lösche die Constraints in der Falldatenbank...");
                        //sendStatusJobMessage(4, MAX_STEPS); //Phase 4 of MAX_STEPS
                        Constraints constraints = new Constraints();
                        constraints.dropConstraints(getCaseDbConnection(), pImportConfig);
                        logTime("Constraints in der Falldatenbank wurden gelöscht");
                    }

                    getProgressor().incrementPhase();
                    getProgressor().setMaxSubPhases(80);

                    //Main Phase 3 (here we start to distribute our CPX-defined common format files!)
                    getProgressor().sendProgress("Starte mit der Verteilung der Daten");
                    CpxDistributor<T> distributor = new CpxDistributor<>(getProgressor());
                    distributor.distributeData(pImportConfig, getCommonDbConnection(), getCaseDbConnection(), LOG_QUERIES);
                    logTime("Verteilung der IMEX-Dateien abgeschlossen");
                    logTime("Import wurde vollständig abgeschlossen");
                    //sendStatusJobMessage(5, MAX_STEPS); //Phase 5 of MAX_STEPS
                    getProgressor().sendProgress("Import wurde vollständig abgeschlossen");

                } finally {
                    //Recreate foreign key constraints
                    getProgressor().sendProgress("Erzeuge neue Constraints in der Falldatenbank");
                    if (REMOVE_CONSTRAINTS) {
                        logTime("Erzeuge neue Constraints in der Falldatenbank...");
                        //sendStatusJobMessage(6, MAX_STEPS); //Phase 6 of MAX_STEPS
                        Constraints constraints = new Constraints();
                        constraints.createConstraints(getCaseDbConnection(), pImportConfig);
                        logTime("Constraints in der Falldatenbank wurden neu angelegt");
                    }
                }
            }
            getProgressor().incrementPhase();
            getProgressor().setMaxSubPhases(1);
            getProgressor().sendProgress("Aktualisiere Eintrag in C_JOB_LOG");
            finishImportsEntry(pImportConfig, "COMPLETED");
            getProgressor().sendSuccess("Import erfolgreich abgeschlossen!\nEs wurden " + pImportConfig.getNumberOfImportedCases() + " Fälle importiert und " + pImportConfig.getNumberOfIgnoredCases() + " Fälle ignoriert.");
                //} catch (IllegalArgumentException | IllegalStateException | IllegalAccessException | SQLException | IOException | ParseException | InstantiationException | NoSuchFieldException ex) {
                
            
            } catch (Exception ex) {
            success = false;
            LOG.log(Level.SEVERE, "import failed with exception", ex);
            getProgressor().incrementPhase();
            getProgressor().setMaxSubPhases(1);
            getProgressor().sendProgress("Aktualisiere Eintrag in C_JOB_LOG");
            getProgressor().sendFailure(ex.getMessage(), ex);
            try {
                finishImportsEntry(pImportConfig, "FAILED");
            } catch (SQLException ex1) {
                LOG.log(Level.SEVERE, "cannot set entry with id " + (pImportConfig == null ? null : pImportConfig.getId()) + " in C_JOB_LOG to status FAILED", ex1);
                getProgressor().sendFailure(ex1.getMessage(), ex1);
            }
            //try {
            //  sendStatusFailureJobMessage(ex.getMessage(), MAX_STEPS);
            //} catch (JMSException ex1) {
            //  Logger.getLogger(StartImportProcess.class.getName()).log(Level.SEVERE, null, ex1);
            //}
        }
        logTime("Import on this database has finished" + (success ? "" : " with error(s)") + ": " + getCaseDbDatabase());
        logTime("ENDE " + (success ? ":-)" : ":'-("));
    }

    @Override
    public void setCommonDbConnection(final Connection pConnection) throws IllegalArgumentException, SQLException {
        if (pConnection == null) {
            throw new IllegalArgumentException("No common database connection was passed!");
        }

        mCommonDb = new ConnectionSettings(pConnection);
    }

    @Override
    public void setCaseDbConnection(final Connection pConnection) throws IllegalArgumentException, SQLException {
        if (pConnection == null) {
            throw new IllegalArgumentException("No case database connection was passed!");
        }

        mCaseDb = new ConnectionSettings(pConnection);
    }

    @Override
    public String getCaseDbDatabase() {
        return mCaseDb == null ? "" : mCaseDb.getDatabase();
    }

    @Override
    public Connection getCaseDbConnection() {
        return mCaseDb == null ? null : mCaseDb.mConnection;
    }

    @Override
    public boolean isCaseDbOracle() {
        return mCaseDb.isOracle();
    }

    @Override
    public boolean isCaseDbSqlSrv() {
        return mCaseDb.isSqlsrv();
    }

    @Override
    public String getCommonDbDatabase() {
        return mCommonDb == null ? "" : mCommonDb.getDatabase();
    }

    @Override
    public Connection getCommonDbConnection() {
        return mCommonDb == null ? null : mCommonDb.mConnection;
    }

    @Override
    public boolean isCommonDbOracle() {
        return mCommonDb.isOracle();
    }

    @Override
    public boolean isCommonDbSqlSrv() {
        return mCommonDb.isSqlsrv();
    }

    public void logTime(final long pStartTime, final String pMessage) {
        final long mEndTime = System.currentTimeMillis();
        final long mDiff = mEndTime - pStartTime;
        final Date startDate = new Date(pStartTime);
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
        final String startDateStr = dfStart.format(startDate);
        final String endDateStr = dfEnd.format(endDate);
        final String diffDateStr = dfDiff.format(diffDate);
        LOG.log(Level.INFO, "Start " + startDateStr + " - Ende " + endDateStr + " -> Dauer " + diffDateStr + ": " + pMessage);
        //mStartTime = System.currentTimeMillis();
    }

    private void logTime(final String pMessage) {
        //logTime(mMeanTime, pMessage);
        //mMeanTime = System.currentTimeMillis();
        logTime(mImportConfig.getStartTime(), pMessage);
    }

    private void printMemory() {
        String lMaxMemoryStr = getMaxMemoryAsString();
        String lTotalMemoryStr = getTotalMemoryAsString();
        String lFreeMemoryStr = getFreeMemoryAsString();
        String lUsedMemoryStr = getUsedMemoryAsString();
        String lHeapInUseStr = getHeapInUseAsString();
        LOG.log(Level.INFO, lMaxMemoryStr + " maximal / " + lTotalMemoryStr + " total / " + lFreeMemoryStr + " frei / " + lUsedMemoryStr + " benutzt (" + lHeapInUseStr + "%)", Level.INFO);
    }

    private void printTime() {
        printTime("");
    }

    //ETA = Estimated Time of Arrival
    private Date getEtaTime(final int pNumberOfProcessedItems, final int pNumberOfAllItems) {
        final long mCurrTime = System.currentTimeMillis();
        if (pNumberOfProcessedItems == 0) {
            return new Date(mMeanTime);
        }
        final long mEstimatedEndTime = mMeanTime + (((mCurrTime - mMeanTime) * pNumberOfAllItems) / pNumberOfProcessedItems);
        return new Date(mEstimatedEndTime);
    }

    private void printTime(final String pMessage) {
        printTime(pMessage, -1, -1);
    }

    private void printTime(final String pMessage, final int pNumberOfProcessedItems, final int pNumberOfAllItems) {
        final long mEndTime = System.currentTimeMillis();
        final long mDiff = mEndTime - mMeanTime;
        final Date endDate = new Date(mEndTime);
        final Date diffDate = new Date(mDiff);
        final Date etaDate = (pNumberOfProcessedItems > -1 && pNumberOfAllItems > -1) ? getEtaTime(pNumberOfProcessedItems, pNumberOfAllItems) : null;
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
        final String etaDateStr = (etaDate != null) ? ", ETA: " + dfEnd.format(etaDate) : "";
        LOG.log(Level.INFO, "Zwischenzeit " + endDateStr + " -> Dauer " + diffDateStr + etaDateStr + ((pMessage != null && !pMessage.trim().isEmpty()) ? (": " + pMessage) : ""));
    }

    @Override
    public ProgressorI getProgressor() {
        return mProgressor;
    }

    private boolean checkDir(String pPath, boolean has2write){
        File dir = new File(pPath);
        if (dir.exists() && dir.isDirectory()){
            if( dir.canRead() && !has2write){
                return dir.listFiles().length > 0;
            }
            if(dir.canWrite() && has2write){
                return true;
            }
        }else{
            if(has2write){
                File parent = dir.getParentFile();
                if(parent.exists() && parent.isDirectory() && parent.canWrite()){
                    return dir.mkdir();
                }
            }
        }
        
        
        return false;
    }
    
    public abstract CpxTransformerI<T> getTransformer(final ImportConfig<T> pImportConfig) throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, InterruptedException, NoSuchMethodException, InvocationTargetException, NoSuchAlgorithmException, SQLException;

}
