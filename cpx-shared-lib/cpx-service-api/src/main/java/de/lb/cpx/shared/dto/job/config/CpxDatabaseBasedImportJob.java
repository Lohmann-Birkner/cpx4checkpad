/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.dto.job.config;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.server.commons.enums.DbDriverEn;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 *
 * @author niemeier
 */
//@XmlRootElement(name = "Sap")
public abstract class CpxDatabaseBasedImportJob extends CpxExternalSystemBasedJobImportConfig {

    private static final long serialVersionUID = 1L;

    private final DbDriverEn driver;
    private final String sourceDatabase;
    private final String backupPath;
    private final boolean useBackup;
    private final boolean doBackup;

    public CpxDatabaseBasedImportJob(
            final String pName,
            final String pTargetDatabase,
            final ImportMode pImportMode,
            final DbDriverEn pDriver,
            final String pSourceDatabase,
            final String pServer,
            final int pPort,
            final String pUser,
            final String pPassword,
            final String pDefaultHosIdent,
            final boolean pRebuildIndexes,
            final GDRGModel pGrouperModel,
            final CpxJobConstraints pConstraints,
            final long pTimePeriodValue,
            final ChronoUnit pTimePeriodUnit,
            final Date pBeginDate,
            final Date pEndDate,
            final boolean pActive,
            final String backupPath,
            final boolean useBackup,
            final boolean doBackup,  String pWhatGroup) 
     {
        super(pName, pTargetDatabase, pImportMode, pServer, pPort, pUser, pPassword, pDefaultHosIdent, pRebuildIndexes, pGrouperModel, pConstraints, 
                pTimePeriodValue, pTimePeriodUnit, pBeginDate, pEndDate, pActive, pWhatGroup);
        this.driver = pDriver;
        this.sourceDatabase = pSourceDatabase;
        this.backupPath = backupPath;
        this.useBackup = useBackup;
        this.doBackup = doBackup;
    }

    public CpxDatabaseBasedImportJob(
            final DbDriverEn pDriver,
            final String pSourceDatabase,
            final String pServer,
            final int pPort,
            final String pUser,
            final String pPassword,
            final String pDefaultHosIdent,
            final ImportMode pImportMode,
            final boolean pRebuildIndexes,
            final CpxJobConstraints pConstraints
    ) {
        super(pServer, pPort, pUser, pPassword, pDefaultHosIdent, pImportMode, pRebuildIndexes, pConstraints);
        this.driver = pDriver;
        this.sourceDatabase = pSourceDatabase;
        this.backupPath = "";
        this.useBackup = false;
        doBackup = false;
    }

    public DbDriverEn getDriver() {
        return driver;
    }

//    public CpxExternalSystemBasedJobImportConfig setDriver(final String pDriver) {
//        return setDriver(DbDriverEn.findById(pDriver));
//    }
//
//    public CpxExternalSystemBasedJobImportConfig setDriver(final DbDriverEn pDriver) {
//        this.driver = pDriver;
//        return this;
//    }
//
//    public CpxExternalSystemBasedJobImportConfig setDatabase(final String pDatabase) {
//        this.database = pDatabase;
//        return this;
//    }
    public String getSourceDatabase() {
        return sourceDatabase;
    }

    public String getNullFunction() {
        return driver == null ? "" : driver.getNullFunction();
    }

    public boolean isIngres() {
        return driver == null ? false : driver.isIngres();
    }

    public boolean isOracle() {
        return driver == null ? false : driver.isOracle();
    }

    public boolean isSqlsrv() {
        return driver == null ? false : driver.isSqlsrv();
    }

    public String getJdbcDriver() {
        return driver == null ? "" : driver.getJdbcDriver();
    }

//    public String getDriverName() {
//        return driver == null ? "" : driver.getName();
//    }
    @Override
    public String toString() {
        return "Server: " + getServer() + ", User: " + getUser() + ", Database: " + getSourceDatabase() + ", Driver: " + driver;
    }

    public Connection createConnection() throws SQLException {
        return driver == null ? null : driver.getConnection(getServer(), getPort() + "", getSourceDatabase(), getUser(), getPassword());
    }

    public String getBackupPath() {
        return backupPath;
    }

    public boolean isUseBackup() {
        return useBackup;
    }

    public boolean isDoBackup() {
        return doBackup;
    }

}
