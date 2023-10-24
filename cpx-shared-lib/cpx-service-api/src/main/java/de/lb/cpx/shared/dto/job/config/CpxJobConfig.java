/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.dto.job.config;

import de.lb.cpx.shared.dto.job.config.database.KissmedJob;
import de.lb.cpx.shared.dto.job.config.database.MedicoJob;
import de.lb.cpx.shared.dto.job.config.database.NexusJob;
import de.lb.cpx.shared.dto.job.config.database.OrbisJob;
import de.lb.cpx.shared.dto.job.config.file.FdseJob;
import de.lb.cpx.shared.dto.job.config.file.P21Job;
import de.lb.cpx.shared.dto.job.config.file.SampleJob;
import de.lb.cpx.shared.dto.job.config.other.BatchgroupingJob;
import de.lb.cpx.shared.dto.job.config.other.BillJob;
import de.lb.cpx.shared.dto.job.config.other.GdvImportDocumentJob;
import de.lb.cpx.shared.dto.job.config.other.SapJob;
import java.io.Serializable;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 *
 * @author niemeier
 */
public class CpxJobConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long id = System.currentTimeMillis();
    private final String name;
    private final String targetDatabase;
    private final CpxJobConstraints constraints;
    private final long timePeriodValue;
    private final ChronoUnit timePeriodUnit;
    private final Date beginDate;
    private final Date endDate;
    private final boolean active;

    public CpxJobConfig(
            final String pName,
            final String pTargetDatabase,
            final CpxJobConstraints pConstraints,
            final long pTimePeriodValue,
            final ChronoUnit pTimePeriodUnit,
            final Date pBeginDate,
            final Date pEndDate,
            final boolean pActive
    ) {
        this.name = pName;
        this.targetDatabase = pTargetDatabase;
        this.constraints = pConstraints == null ? new CpxJobConstraints() : pConstraints;
//        this.constraints = pConstraints;
        this.timePeriodValue = pTimePeriodValue;
        this.timePeriodUnit = pTimePeriodUnit;
        this.beginDate = pBeginDate == null ? null : new Date(pBeginDate.getTime());
        this.endDate = pEndDate == null ? null : new Date(pEndDate.getTime());
        this.active = pActive;
    }

    public CpxJobConfig(
            //final String pName,
            //final String pTargetDatabase,
            final CpxJobConstraints pConstraints
    ) {
        this(null, null, pConstraints, 0L, null, null, null, true);
    }

    public String getClassName() {
        return getClass().getName();
    }

    public long getId() {
        return id;
    }

//    @XmlElement(name = "name")
    /**
     * name or title of this configuration (customized by user)
     *
     * @return name or title of config
     */
    public String getName() {
        return name;
    }

//    public CpxJobConfig setName(final String pName) {
//        this.name = pName;
//        return this;
//    }
    @Override
    public String toString() {
        return name;
    }

    /**
     * @return the constraints
     */
    public CpxJobConstraints getConstraints() {
        return constraints;
    }

//    public CpxJobConfig setConstraints(CpxJobConstraints pConstraints) {
//        this.constraints = pConstraints == null ? new CpxJobConstraints() : pConstraints;
//        return this;
//    }
    /**
     * @return the targetDatabase
     */
    public String getTargetDatabase() {
        return targetDatabase;
    }

//    public CpxJobConfig setTargetDatabase(String pDatabase) {
//        this.targetDatabase = pDatabase;
//        return this;
//    }
    /**
     * @return the timePeriodValue
     */
    public long getTimePeriodValue() {
        return timePeriodValue;
    }

//    /**
//     * @param pTimePeriodValue the timePeriodValue to set
//     */
//    public void setTimePeriodValue(long pTimePeriodValue) {
//        this.timePeriodValue = pTimePeriodValue;
//    }
    /**
     * @return the timePeriodUnit
     */
    public ChronoUnit getTimePeriodUnit() {
        return timePeriodUnit;
    }

//    /**
//     * @param pTimePeriodUnit the timePeriodUnit to set
//     */
//    public void setTimePeriodUnit(ChronoUnit pTimePeriodUnit) {
//        this.timePeriodUnit = pTimePeriodUnit;
//    }
//    /**
//     * @param pTimePeriodUnit the timePeriodUnit to set
//     */
//    public void setTimePeriodUnit(String pTimePeriodUnit) {
//        final String tmp = pTimePeriodUnit == null ? "" : pTimePeriodUnit.trim();
//        ChronoUnit result = null;
//        if (!tmp.isEmpty()) {
//            for (ChronoUnit item : ChronoUnit.values()) {
//                if (tmp.equalsIgnoreCase(item.name())) {
//                    result = item;
//                    break;
//                }
//            }
//        }
//        this.timePeriodUnit = result;
//    }
    /**
     * @return the beginDate
     */
    public Date getBeginDate() {
        return beginDate == null ? null : new Date(beginDate.getTime());
    }

//    /**
//     * @param pBeginDate the beginDate to set
//     */
//    public void setBeginDate(Date pBeginDate) {
//        this.beginDate = pBeginDate == null ? null : new Date(pBeginDate.getTime());
//    }
    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate == null ? null : new Date(endDate.getTime());
    }

//    /**
//     * @param pEndDate the endDate to set
//     */
//    public void setEndDate(Date pEndDate) {
//        this.endDate = pEndDate == null ? null : new Date(pEndDate.getTime());
//    }
    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

//    /**
//     * @param active the active to set
//     */
//    public void setActive(boolean active) {
//        this.active = active;
//    }
    public boolean isImport() {
        return isImportStatic(getClass());
        //return CpxJobImportConfig.class.isAssignableFrom(getClass());
        //return getClassName().equalsIgnoreCase(SapJob.class.getName());
    }

    public boolean isSapImport() {
        return isImportStatic(getClass());
        //return SapJob.class.isAssignableFrom(getClass());
        //return getClassName().equalsIgnoreCase(SapJob.class.getName());
    }

    public boolean isDbImport() {
        return isDbImportStatic(getClass());
        //return CpxDatabaseBasedImportJob.class.isAssignableFrom(getClass());
        //return getClassName().equalsIgnoreCase(CpxDatabaseBasedImportJob.class.getName());
    }

    public boolean isExternalImport() {
        return isExternalImportStatic(getClass());
        //return CpxExternalSystemBasedJobImportConfig.class.isAssignableFrom(getClass());
        //return getClassName().equalsIgnoreCase(CpxDatabaseBasedImportJob.class.getName());
    }

    public boolean isFileImport() {
        return isFileImportStatic(getClass());
        //return CpxFileBasedImportJob.class.isAssignableFrom(getClass());
        //return getClassName().equalsIgnoreCase(DbInterfaceConfig.class.getName());
    }

    public boolean isOrbisImport() {
        return isOrbisImportStatic(getClass());
        //return OrbisJob.class.isAssignableFrom(getClass());
        //return getClassName().equalsIgnoreCase(OrbisJob.class.getName());
    }

    public boolean isNexusImport() {
        return isNexusImportStatic(getClass());
        //return NexusJob.class.isAssignableFrom(getClass());
        //return getClassName().equalsIgnoreCase(OrbisJob.class.getName());
    }

    public boolean isMedicoImport() {
        return isMedicoImportStatic(getClass());
        //return MedicoJob.class.isAssignableFrom(getClass());
        //return getClassName().equalsIgnoreCase(MedicoJob.class.getName());
    }

    public boolean isKissmedImport() {
        return isKissmedImportStatic(getClass());
        //return KissmedJob.class.isAssignableFrom(getClass());
        //return getClassName().equalsIgnoreCase(KissmedInterfaceConfig.class.getName());
    }

    public boolean isP21Import() {
        return isP21ImportStatic(getClass());
        //return P21Job.class.isAssignableFrom(getClass());
        //return getClassName().equalsIgnoreCase(SapInterfaceConfig.class.getName());
    }

    public boolean isSampleImport() {
        return isSampleImportStatic(getClass());
        //return SampleJob.class.isAssignableFrom(getClass());
        //return getClassName().equalsIgnoreCase(SapInterfaceConfig.class.getName());
    }

    public boolean isFdseImport() {
        return isFdseImportStatic(getClass());
        //return FdseJob.class.isAssignableFrom(getClass());
        //return getClassName().equalsIgnoreCase(OrbisInterfaceConfig.class.getName());
    }

    public boolean isBatchgrouping() {
        return BatchgroupingJob.class.isAssignableFrom(getClass());
        //return getClassName().equalsIgnoreCase(OrbisInterfaceConfig.class.getName());
    }

    public boolean isGdvImportDocumentJob() {
        return GdvImportDocumentJob.class.isAssignableFrom(getClass());
        //return getClassName().equalsIgnoreCase(OrbisInterfaceConfig.class.getName());
    }

    public boolean isBill() {
        return BillJob.class.isAssignableFrom(getClass());
        //return getClassName().equalsIgnoreCase(OrbisInterfaceConfig.class.getName());
    }

    public static boolean isImportStatic(final Class<?> pClass) {
        return CpxJobImportConfig.class.isAssignableFrom(pClass);
    }

    public static boolean isSapImportStatic(final Class<?> pClass) {
        return SapJob.class.isAssignableFrom(pClass);
    }

    public static boolean isDbImportStatic(final Class<?> pClass) {
        return CpxDatabaseBasedImportJob.class.isAssignableFrom(pClass);
    }

    public static boolean isExternalImportStatic(final Class<?> pClass) {
        return CpxExternalSystemBasedJobImportConfig.class.isAssignableFrom(pClass);
    }

    public static boolean isFileImportStatic(final Class<?> pClass) {
        return CpxFileBasedImportJob.class.isAssignableFrom(pClass);
    }

    public static boolean isOrbisImportStatic(final Class<?> pClass) {
        return OrbisJob.class.isAssignableFrom(pClass);
    }

    public static boolean isNexusImportStatic(final Class<?> pClass) {
        return NexusJob.class.isAssignableFrom(pClass);
    }

    public static boolean isMedicoImportStatic(final Class<?> pClass) {
        return MedicoJob.class.isAssignableFrom(pClass);
    }

    public static boolean isKissmedImportStatic(final Class<?> pClass) {
        return KissmedJob.class.isAssignableFrom(pClass);
    }

    public static boolean isP21ImportStatic(final Class<?> pClass) {
        return P21Job.class.isAssignableFrom(pClass);
    }

    public static boolean isSampleImportStatic(final Class<?> pClass) {
        return SampleJob.class.isAssignableFrom(pClass);
    }

    public static boolean isFdseImportStatic(final Class<?> pClass) {
        return FdseJob.class.isAssignableFrom(pClass);
    }

    public static boolean isBatchgroupingStatic(final Class<?> pClass) {
        return BatchgroupingJob.class.isAssignableFrom(pClass);
    }
    
    public static boolean isBillStatic(final Class<?> pClass) {
        return BillJob.class.isAssignableFrom(pClass);
    }
    
    public static boolean isGdvImportDocumentJobStatic(final Class<?> pClass) {
        return GdvImportDocumentJob.class.isAssignableFrom(pClass);
    }
    
    public boolean isActive(final Date pDate) {
        if (!isActive()) {
            return false;
        }
//        if (getTimePeriodUnit() == null) {
//            return false;
//        }
        if (getBeginDate() != null && getBeginDate().after(pDate)) {
            return false;
        }
        if (getEndDate() != null && getEndDate().before(pDate)) {
            return false;
        }
        return true;
    }

}
