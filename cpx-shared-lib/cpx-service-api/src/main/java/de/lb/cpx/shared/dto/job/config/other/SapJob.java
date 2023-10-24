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
package de.lb.cpx.shared.dto.job.config.other;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.shared.dto.job.config.CpxExternalSystemBasedJobImportConfig;
import de.lb.cpx.shared.dto.job.config.CpxJobConstraints;
import de.lb.cpx.shared.dto.job.config.ImportMode;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 *
 * @author niemeier
 */
//@XmlRootElement(name = "Sap")
public class SapJob extends CpxExternalSystemBasedJobImportConfig {

    private static final long serialVersionUID = 1L;

    private final String sysNr;
    private final String mandant;
    private final String institution;
    private  boolean importWard;
    private  boolean useJsonDump;
    private  boolean writeJsonDump;
    private  boolean doAnonymize;
    private  String mJsonPath;

    public SapJob(
            final String pName,
            final String pTargetDatabase,
            final ImportMode pImportMode,
            final String pServer,
            final int pPort,
            final String pUser,
            final String pPassword,
            final String pDefaultHosIdent,
            final String pSysNr,
            final String pMandant,
            final String pInstitution,
            final boolean pRebuildIndexes,
            final GDRGModel pGrouperModel,
            final CpxJobConstraints pConstraints,
            final long pTimePeriodValue,
            final ChronoUnit pTimePeriodUnit,
            final Date pBeginDate,
            final Date pEndDate,
            final boolean pActive,
            final boolean pImportWard,
            final boolean readJson,
            final boolean writeJson,
            final boolean anonymize,
            final String jsonPath,
             String pWhatGroup
    ) {
        super(pName, pTargetDatabase, pImportMode, pServer, pPort, pUser, pPassword, pDefaultHosIdent, pRebuildIndexes, pGrouperModel, pConstraints,
                pTimePeriodValue, pTimePeriodUnit, pBeginDate, pEndDate, pActive,  pWhatGroup);
        this.sysNr = pSysNr;
        this.mandant = pMandant;
        this.institution = pInstitution;
        this.importWard = pImportWard;
        useJsonDump = readJson;
        writeJsonDump = writeJson;
        doAnonymize = anonymize;
        mJsonPath = jsonPath;
    }

    public SapJob(
            final ImportMode pImportMode,
            final String pServer,
            final int pPort,
            final String pUser,
            final String pPassword,
            final String pDefaultHosIdent,
            final String pSysNr,
            final String pMandant,
            final String pInstitution,
            final boolean pRebuildIndexes,
            final CpxJobConstraints pConstraints,
            final boolean pImportWard,
            final boolean readJson,
            final boolean writeJson,
            final boolean anonymize,
            final String jsonPath
    ) {
        super(pServer, pPort, pUser, pPassword, pDefaultHosIdent, pImportMode, pRebuildIndexes, pConstraints);
        this.sysNr = pSysNr;
        this.mandant = pMandant;
        this.institution = pInstitution;
        this.importWard = pImportWard;
        useJsonDump = readJson;
        writeJsonDump = writeJson;
        doAnonymize = anonymize;
        mJsonPath = jsonPath;
    }

    public String getSysNr() {
        return sysNr;
    }

//    public CpxExternalSystemBasedJobImportConfig setSysNr(final String pSysNr) {
//        this.sysNr = pSysNr;
//        return this;
//    }
    public String getMandant() {
        return mandant;
    }

//    public CpxExternalSystemBasedJobImportConfig setMandant(final String pMandant) {
//        this.mandant = pMandant;
//        return this;
//    }
    public String getInstitution() {
        return institution;
    }

//    public CpxExternalSystemBasedJobImportConfig setInstitution(final String pInstitution) {
//        this.institution = pInstitution;
//        return this;
//    }
    @Override
    public String toString() {
        return "Server: " + getServer() + ",\n User: " + getUser() + ",\n Mandant: " + getMandant() 
                + ",\n Institution: " + getInstitution() + ",\n Sys-Nr.: " + getSysNr() + ",\n Import Ward: " + String.valueOf(getImportWard())
                +",\n use Json dump: " + String.valueOf(isUseJsonDump())
                +",\n write Json dump: " + String.valueOf(isWriteJsonDump())
                +",\n do anonymize: " + String.valueOf(isDoAnonymize())
                +",\n Json path: " + String.valueOf(getmJsonPath())
                ;
    }

    public boolean getImportWard() {
        return importWard;
    }

    public boolean isUseJsonDump() {
        return useJsonDump;
    }

    public boolean isWriteJsonDump() {
        return writeJsonDump;
    }

    public boolean isDoAnonymize() {
        return doAnonymize;
    }

    public String getmJsonPath() {
        return mJsonPath;
    }

    public void setUseJsonDump(boolean useJsonDump) {
        this.useJsonDump = useJsonDump;
    }

    public void setWriteJsonDump(boolean writeJsonDump) {
        this.writeJsonDump = writeJsonDump;
    }

    public void setDoAnonymize(boolean doAnonymize) {
        this.doAnonymize = doAnonymize;
    }

    public void setmJsonPath(String mJsonPath) {
        this.mJsonPath = mJsonPath;
    }

    public void setImportWard(boolean importWard) {
        this.importWard = importWard;
    }

    }
