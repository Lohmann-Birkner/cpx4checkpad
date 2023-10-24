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
package module.impl;

import de.lb.cpx.cpx.license.crypter.License;
import de.lb.cpx.shared.dto.job.config.CpxExternalSystemBasedJobImportConfig;
import de.lb.cpx.shared.dto.job.config.CpxFileBasedImportJob;
import de.lb.cpx.shared.dto.job.config.CpxJobConstraints;
import de.lb.cpx.shared.dto.job.config.CpxJobImportConfig;
import de.lb.cpx.shared.dto.job.config.ImportMode;
import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.AbstractImportContainerModule;
import module.AbstractImportDbModule;
import module.AbstractImportFileModule;
import module.ImportModuleI;

/**
 * This class holds the import configuration for a specific module (SAP, P21...)
 *
 * @author Dirk Niemeier
 * @param <T> Import module (e.g. P21)
 */
public class ImportConfig<T extends ImportModuleI<? extends CpxJobImportConfig>> {

    private static final Logger LOG = Logger.getLogger(ImportConfig.class.getName());

    public final T mModule;
//    public final ImportMode mMode;
    private boolean mIsCaseDbOracle = true;
    private boolean mIsCommonDbOracle = true;
    //public final boolean isOracle;
    //public final boolean mOverwriteCases;
    //public final boolean mGroupCases;
    private long mStartTime = 0;
    private Long id;
    private Integer numOfPatients;
    private Integer numOfCases;
    private Integer numOfImportedCases;
    private Integer numOfIgnoredCases;
//    private CpxTransformerI<T> mTransformer = null;
    public final String mDatabase;
    public License mLicense;
    public CpxJobConstraints mImportConstraint;

//    /**
//     * Creates an import config
//     *
//     * @param pDatabase database
//     * @param pModule module
//     * @param pImportMode import mode
//     * @param pLicense license
//     */
//    public ImportConfig(final String pDatabase, final T pModule, final String pImportMode /*, final boolean pOverwriteCases, final boolean pGroupCases */, final License pLicense) {
//        this(pDatabase, pModule, ImportMode.getImportMode(pImportMode), pLicense);
//    }
    /**
     * Creates an import config
     *
     * @param pDatabase database (use physical address like ip:port/dbname)
     * @param pModule module
     * @param pLicense license
     * @param pImportConstraint runtime constraints (overwrites config
     * constraints)
     */
    public ImportConfig(final String pDatabase, final T pModule /*, final boolean pOverwriteCases, final boolean pGroupCases */, final License pLicense, final CpxJobConstraints pImportConstraint) {
        final String database = pDatabase == null ? "" : pDatabase.trim();
        if (database.isEmpty()) {
            throw new IllegalArgumentException("Database connection string is null or empty (format is like dbsys1:MY_CPX_DB)!");
        }
        if (!database.contains("/") && !database.contains(":")) {
            throw new IllegalArgumentException("Database connection string is not valid (e.g. LBBatch-01:1433/TESTDB)!");
        }
        if (pModule == null) {
            throw new IllegalArgumentException("Import module is null!");
        }
        if (pLicense == null) {
            throw new IllegalArgumentException("No CPX license passed!");
        }
        LOG.log(Level.INFO, "license for import: " + String.valueOf(pLicense));
        mModule = pModule;
        mLicense = pLicense;
        mDatabase = database;
        mImportConstraint = pImportConstraint;
        //isOracle = pIsOracle;
        //mOverwriteCases = pOverwriteCases;
        //mGroupCases = pGroupCases;
    }

    /**
     * Creates an import config
     *
     * @param pDatabase database (use physical address like ip:port/dbname)
     * @param pModule module
     * @param pLicense license
     */
    public ImportConfig(final String pDatabase, final T pModule /*, final boolean pOverwriteCases, final boolean pGroupCases */, final License pLicense) {
        this(pDatabase, pModule, pLicense, null);
//        final String database = pDatabase == null ? "" : pDatabase.trim();
//        if (database.isEmpty()) {
//            throw new IllegalArgumentException("Database connection string is null or empty (format is like dbsys1:MY_CPX_DB)!");
//        }
//        if (!database.contains("/") && !database.contains(":")) {
//            throw new IllegalArgumentException("Database connection string is not valid (e.g. LBBatch-01:1433/TESTDB)!");
//        }
//        if (pModule == null) {
//            throw new IllegalArgumentException("Import module is null!");
//        }
//        if (pLicense == null) {
//            throw new IllegalArgumentException("No CPX license passed!");
//        }
//        LOG.log(Level.INFO, "license for import: " + String.valueOf(pLicense));
//        mModule = pModule;
//        mLicense = pLicense;
//        mDatabase = database;
//        //isOracle = pIsOracle;
//        //mOverwriteCases = pOverwriteCases;
//        //mGroupCases = pGroupCases;
    }

//    /**
//     * Creates an import config
//     *
//     * @param pDatabase database (connection string e.g. dbsys1:MY_CPX_DB)
//     * @param pModule module
//     * @param pImportMode import mode
//     * @param pLicense license
//     */
//    public ImportConfig(final String pDatabase, final T pModule, final ImportMode pImportMode /*, final boolean pOverwriteCases, final boolean pGroupCases */, final License pLicense) {
//        this(pDatabase, pModule, pImportMode, pLicense, null);
//    }
//
//    /**
//     * Creates an import config
//     *
//     * @param pDatabase database (connection string e.g. dbsys1:MY_CPX_DB)
//     * @param pModule module
//     * @param pImportMode import mode
//     * @param pLicense license
//     * @param pImportConstraint import constraints
//     */
//    public ImportConfig(final String pDatabase, final T pModule, final ImportMode pImportMode /*, final boolean pOverwriteCases, final boolean pGroupCases */, final License pLicense, final CpxJobConstraints pImportConstraint) {
//        final String database = pDatabase == null ? "" : pDatabase.trim();
//        if (database.isEmpty()) {
//            throw new IllegalArgumentException("Database connection string is null or empty (format is like dbsys1:MY_CPX_DB)!");
//        }
//        if (pModule == null) {
//            throw new IllegalArgumentException("Import module is null!");
//        }
//        if (pImportMode == null) {
//            throw new IllegalArgumentException("No import mode passed (overwrite existing cases or create a new version?)!");
//        }
//        if (pLicense == null) {
//            throw new IllegalArgumentException("No CPX license passed!");
//        }
//        LOG.log(Level.INFO, "license for import: " + String.valueOf(pLicense));
//        mModule = pModule;
//        mMode = pImportMode;
//        mDatabase = database;
//        mLicense = pLicense;
//        mImportConstraint = pImportConstraint == null ? new CpxJobConstraints() : pImportConstraint;
//        //isOracle = pIsOracle;
//        //mOverwriteCases = pOverwriteCases;
//        //mGroupCases = pGroupCases;
//    }
    /**
     * Gives the module name
     *
     * @return module name
     */
    public String getModuleName() {
        return mModule.getName();
    }

    public String getModuleType() {
        if (isFileModule()) {
            return "file based import";
        }
        if (isDatabaseModule()) {
            return "database based import";
        }
        if (isContainerModule()) {
            return "container based import";
        }
        return "";
    }

    /**
     * Gives the module
     *
     * @return module
     */
    public T getModule() {
        return mModule;
    }

    /**
     * Gives the import mode
     *
     * @return import mode
     */
    public ImportMode getMode() {
        return mModule.getInputConfig().getImportMode();
    }

    /**
     * Should the import drop/clear the database before the import starts?
     *
     * @return truncate the database before import?
     */
    public boolean isTruncateDb() {
        return mModule.getInputConfig().getImportMode().truncate;
    }

    /**
     * Should the import delete versions for existing hospital cases?
     *
     * @return delete versions for existing cases?
     */
    public boolean isOverwriteCases() {
        return mModule.getInputConfig().getImportMode().overwrite;
    }

    /**
     * Should the import create new versions for existing hospital cases?
     *
     * @return new version for existing cases?
     */
    public boolean isNewVersion() {
        return !mModule.getInputConfig().getImportMode().overwrite;
    }

    /**
     * Returns module class
     *
     * @return module class
     */
    @SuppressWarnings("unchecked")
    public Class<? extends ImportModuleI<? extends CpxJobImportConfig>> getModuleClass() {
        return (Class<? extends ImportModuleI<? extends CpxJobImportConfig>>) mModule.getClass();
    }

    /**
     * Is this a module class for P21, SAP... ?
     *
     * @param pClazz class to test
     * @return is module class?
     */
    public boolean isModuleClass(final Class<? extends ImportModuleI<? extends CpxJobImportConfig>> pClazz) {
        return getModuleClass() == pClazz;
    }

    /**
     * Gives the output directory for transformation files (intermedia files)
     *
     * @return output directory
     */
    public String getOutputDirectory() {
        return getModule().getOutputDirectory();
    }
    
    public void setOutputDirectory(String path){
        getModule().setOutputDirectory(path);
    }

    public boolean isContainerModule() {
        return AbstractImportContainerModule.class.isAssignableFrom(getModule().getClass());
    }

    public boolean isFileModule() {
        return AbstractImportFileModule.class.isAssignableFrom(getModule().getClass());
    }

    public boolean isDatabaseModule() {
        return AbstractImportDbModule.class.isAssignableFrom(getModule().getClass());
    }

    public String getInputDirectory() {
        T module = getModule();
        //if (isModuleClass(ImportFileModuleI.class)) {
        if (isFileModule()) {
            AbstractImportFileModule<? extends CpxFileBasedImportJob> m = (AbstractImportFileModule<? extends CpxFileBasedImportJob>) module;
            return m.getInputDirectory();
        } else {
            LOG.log(Level.FINEST, "cannot return file input directory, because this is not file import");
            return "";
        }
    }

    public CpxExternalSystemBasedJobImportConfig getInputConfig() {
        T module = getModule();
        //if (isModuleClass(ImportFileModuleI.class)) {
        if (isDatabaseModule()) {
            AbstractImportDbModule<?> m = (AbstractImportDbModule) module;
            return m.getInputConfig();
        } else if (isContainerModule()) {
            AbstractImportContainerModule<?, ?> m = (AbstractImportContainerModule) module;
            return m.getInputConfig();
        } else {
            LOG.log(Level.WARNING, "No database and no container import");
            return null;
        }
    }

    /**
     * Sets if this case db is running on Oracle
     *
     * @param pIsOracle is Oracle?
     */
    public void isCaseDbOracle(final boolean pIsOracle) {
        mIsCaseDbOracle = pIsOracle;
    }

    /**
     * Runs this case db on Oracle?
     *
     * @return is Oracle?
     */
    public boolean isCaseDbOracle() {
        return mIsCaseDbOracle;
    }

    /**
     * Runs this case db on Microsoft SQL Server?
     *
     * @return is Microsoft SQL Server?
     */
    public boolean isCaseDbSqlSrv() {
        return !isCaseDbOracle();
    }

    /**
     * Sets if this common db is running on Oracle
     *
     * @param pIsOracle is Oracle?
     */
    public void isCommonDbOracle(final boolean pIsOracle) {
        mIsCommonDbOracle = pIsOracle;
    }

    /**
     * Runs this common db on Oracle?
     *
     * @return is Oracle?
     */
    public boolean isCommonDbOracle() {
        return mIsCommonDbOracle;
    }

    /**
     * Runs this common db on Microsoft SQL Server?
     *
     * @return is Microsoft SQL Server?
     */
    public boolean isCommonDbSqlSrv() {
        return !isCommonDbOracle();
    }

    /**
     * Sets the start time
     *
     * @param pStartTime start time
     */
    public void setStartTime(final long pStartTime) {
        mStartTime = (pStartTime == 0) ? System.currentTimeMillis() : pStartTime;
    }

    /**
     * start time
     *
     * @return time
     */
    public long getStartTime() {
        return mStartTime;
    }

//    /**
//     * Sets the transformer (is responsible to create CPX intermedia
//     * transformation files)
//     *
//     * @param pTransformer transformer
//     */
//    public void setTransformer(CpxTransformerI<T> pTransformer) {
//        mTransformer = pTransformer;
//    }
//
//    /**
//     * Transformer is responsible to create CPX intermedia transformation files
//     *
//     * @return transformer
//     */
//    public CpxTransformerI<T> getTransformer() {
//        return mTransformer;
//    }
    public void setId(final long pId) {
        id = pId;
    }

    public Long getId() {
        return id;
    }

    public void setNumberOfPatients(final int pNumOfPatients) {
        numOfPatients = pNumOfPatients;
    }

    public Integer getNumberOfPatients() {
        return numOfPatients;
    }

    public void setNumberOfCases(final int pNumOfCases) {
        numOfCases = pNumOfCases;
    }

    public Integer getNumberOfCases() {
        return numOfCases;
    }

    public void setNumberOfImportedCases(int pNumOfImportedCases) {
        numOfImportedCases = pNumOfImportedCases;
    }

    public Integer getNumberOfImportedCases() {
        return numOfImportedCases;
    }

    public void setNumberOfIgnoredCases(int pNumOfIgnoredCases) {
        numOfIgnoredCases = pNumOfIgnoredCases;
    }

    public Integer getNumberOfIgnoredCases() {
        return numOfIgnoredCases;
    }

//    public void setDatabase(final String pDatabase) {
//        database = pDatabase == null ? "" : pDatabase.trim();
//    }
    /**
     * physical database (ip:port/dbname)
     *
     * @return database address
     */
    public String getDatabase() {
        return mDatabase;
    }

    /**
     * logical database (dbsysX:dbname)
     *
     * @return database
     */
    public String getTargetDatabase() {
        return mModule.getInputConfig().getTargetDatabase();
    }

    public License getLicense() {
        return mLicense;
    }

    public CpxJobConstraints getImportConstraint() {
        if (mImportConstraint != null) {
            return mImportConstraint;
        }
        return mModule.getInputConfig().getConstraints();
    }

    public Date getAdmissionDateFrom() {
        return getImportConstraint().getAdmissionDateFrom();
    }

    public Date getAdmissionDateTo() {
        return getImportConstraint().getAdmissionDateTo();
    }

    public Date getAdmissionDateTo(final int pDays) {
        return getImportConstraint().getAdmissionDateTo(pDays);
    }

    public Date getDischargeDateFrom() {
        return getImportConstraint().getDischargeDateFrom();
    }

    public Date getDischargeDateTo() {
        return getImportConstraint().getDischargeDateTo();
    }

    public Date getDischargeDateTo(final int pDays) {
        return getImportConstraint().getDischargeDateTo(pDays);
    }

    public Set<String> getCaseNumbers() {
        return getImportConstraint().getCaseNumbers();
    }

    public boolean hasCaseNumbers() {
        return getImportConstraint().hasCaseNumbers();
    }

    public boolean isRebuildIndexes() {
        return mModule.getInputConfig().isRebuildIndexes();
    }

    public long getJobId() {
        return mModule.getInputConfig().getId();
    }

}
