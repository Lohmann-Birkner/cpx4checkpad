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
package de.lb.cpx.shared.dto.job.config.database;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.server.commons.enums.DbDriverEn;
import de.lb.cpx.shared.dto.job.config.CpxDatabaseBasedImportJob;
import de.lb.cpx.shared.dto.job.config.CpxJobConstraints;
import de.lb.cpx.shared.dto.job.config.ImportMode;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 *
 * @author niemeier
 */
//@XmlRootElement(name = "Sap")
public class KissmedJob extends CpxDatabaseBasedImportJob {

    private static final long serialVersionUID = 1L;

    public KissmedJob(String pName, String pTargetDatabase, ImportMode pImportMode, DbDriverEn pDriver, String pSourceDatabase, String pServer, int pPort, 
            String pUser, String pPassword, String pDefaultHosIdent, boolean pRebuildIndexes, GDRGModel pGrouperModel, CpxJobConstraints pConstraints, 
            long pTimePeriodValue, ChronoUnit pTimePeriodUnit, Date pBeginDate, Date pEndDate, boolean pActive, 
            String backupPath, boolean useBackup, boolean doBackup,  String pWhatGroup) {
        super(pName, pTargetDatabase, pImportMode, pDriver, pSourceDatabase, pServer, pPort, pUser, pPassword, pDefaultHosIdent, 
                pRebuildIndexes, pGrouperModel, pConstraints, pTimePeriodValue, pTimePeriodUnit, pBeginDate, pEndDate, pActive, 
                backupPath, useBackup, doBackup, pWhatGroup);
    }

    public KissmedJob(DbDriverEn pDriver, String pSourceDatabase, String pServer, int pPort, String pUser, String pPassword, String pDefaultHosIdent, ImportMode pImportMode, boolean pRebuildIndexes, CpxJobConstraints pConstraints) {
        super(pDriver, pSourceDatabase, pServer, pPort, pUser, pPassword, pDefaultHosIdent, pImportMode, pRebuildIndexes, pConstraints);
    }

}
