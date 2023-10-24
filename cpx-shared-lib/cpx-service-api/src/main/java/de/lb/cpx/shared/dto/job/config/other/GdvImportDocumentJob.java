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
package de.lb.cpx.shared.dto.job.config.other;


import de.lb.cpx.shared.dto.job.config.CpxJobConfig;
import de.lb.cpx.shared.dto.job.config.CpxJobConstraints;
import de.lb.cpx.shared.lang.Lang;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 *
 * @author gerschmann
 */
public class GdvImportDocumentJob extends CpxJobConfig{
    private final String gdvDirectory;
    private final String targetDirectory;
    private final String emailFrom;
    private final String emailPassword;
    private final String emailTo;
    private final String archivDirectory;
    private final String emailPort;
    private final String emailHost;
    private final String emailDebug;

    
    public GdvImportDocumentJob(
            final String pName,
            final String pTargetDatabase,
            final CpxJobConstraints pConstraints,
            final long pTimePeriodValue,
            final ChronoUnit pTimePeriodUnit,
            final Date pBeginDate,
            final Date pEndDate,
            final boolean pActive,
            final String pGdvDirectory,
            final String pTargetDirectory,
            final String pArchivDirectory,
            final String pEmailFrom,
            final String pEmailPassword,
            final String pEmailTo,
            final String pEmailPort,
            final String pEmailHost,
            final String pEmailDebug

    ){
        super(pName, pTargetDatabase, pConstraints, pTimePeriodValue, pTimePeriodUnit, pBeginDate, pEndDate, pActive);

        gdvDirectory = pGdvDirectory;
        targetDirectory = pTargetDirectory;
        emailFrom = pEmailFrom;
        emailPassword = pEmailPassword;
        emailTo = pEmailTo;
        archivDirectory = pArchivDirectory;
        emailHost = pEmailHost;
        emailPort = pEmailPort;
        emailDebug = pEmailDebug;
    }

    public String getGdvDirectory() {
        return gdvDirectory;
    }

    public String getTargetDirectory() {
        return targetDirectory;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public String getArchivDirectory() {
        return archivDirectory;
    }

    public String getEmailPort() {
        return emailPort;
    }

    public String getEmailHost() {
        return emailHost;
    }

    public String getEmailDebug() {
        return emailDebug;
    }
    
    @Override
    public String toString(){
        return "Name: " + getName() + "\n" +
                "begin time: " + Lang.toDateTime(this.getBeginDate()) +
                "TargetDatabase: " + this.getTargetDatabase() + "\n" +
                " GdvDirectory: " + this.getGdvDirectory() + "\n" +
                " TargetDirectory: " + this.getTargetDirectory() + "\n" + 
                " ArchivDirectory: " + this.getArchivDirectory() + "\n" + 
                " EmailFrom: " + this.getEmailFrom() + "\n" + 
                " EmailTo: " + this.getEmailTo() + "\n" + 
                " Email host: " + this.getEmailHost()  + "\n" + 
                " Email port: " + this.getEmailPort() + "\n" +
                " Email debug:" + this.getEmailDebug();

    }
}
