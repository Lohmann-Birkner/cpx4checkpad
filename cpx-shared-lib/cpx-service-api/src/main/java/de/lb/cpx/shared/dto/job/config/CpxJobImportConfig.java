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
import de.lb.cpx.shared.dto.job.config.other.BatchgroupingJob;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 *
 * @author niemeier
 */
public abstract class CpxJobImportConfig extends CpxJobConfig {

    private static final long serialVersionUID = 1L;

    //public static final ImportMode DEFAULT_IMPORT_MODE = ImportMode.Version;
    private final ImportMode importMode;
    private final boolean rebuildIndexes;
    private final GDRGModel grouperModel;
    private final String whatGroup;
    
    public CpxJobImportConfig(
            final String pName,
            final String pTargetDatabase,
            final ImportMode pImportMode,
            final boolean pRebuildIndexes,
            final GDRGModel pGrouperModel,
            final CpxJobConstraints pConstraints,
            final long pTimePeriodValue,
            final ChronoUnit pTimePeriodUnit,
            final Date pBeginDate,
            final Date pEndDate,
            final boolean pActive,
            final String pWhatGroup
    ) {
        super(pName, pTargetDatabase, pConstraints, pTimePeriodValue, pTimePeriodUnit, pBeginDate, pEndDate, pActive);
        this.importMode = pImportMode;
        this.rebuildIndexes = pRebuildIndexes;
        this.grouperModel = pGrouperModel;
        whatGroup = pWhatGroup;
    }

    public CpxJobImportConfig(
            final ImportMode pImportMode,
            final boolean pRebuildIndexes,
            //final String pTargetDatabase, 
            final CpxJobConstraints pConstraints) {
        super(pConstraints);
        this.importMode = pImportMode;
        this.rebuildIndexes = pRebuildIndexes;
        this.grouperModel = null;
        whatGroup = null;
    }

    public ImportMode getImportMode() {
        //return importMode == null ? DEFAULT_IMPORT_MODE : importMode;
        return importMode;
    }

//    public CpxJobImportConfig setImportMode(final ImportMode pImportMode) {
//        this.importMode = pImportMode;
//        return this;
//    }
//    public CpxJobImportConfig setImportMode(final String pImportMode) {
//        if (pImportMode == null || pImportMode.trim().isEmpty()) {
//            this.importMode = null;
//        } else {
//            this.importMode = ImportMode.getImportMode(pImportMode);
//        }
//        return this;
//    }
    /**
     * @return the rebuildIndexes
     */
    public boolean isRebuildIndexes() {
        return rebuildIndexes;
    }

    /**
     * @return the grouperModel
     */
    public GDRGModel getGrouperModel() {
        return grouperModel;
    }

    public BatchgroupingJob getBatchgroupingJob() {
        final GDRGModel model = grouperModel;
        if (model == null) {
            return null;
        }
        return new BatchgroupingJob(getName() + "_Batchgrouping", getTargetDatabase(), model, getConstraints(), 0L /* getTimePeriodValue() */, 
                null /* getTimePeriodUnit() */, null /* getBeginDate() */, null /* getEndDate() */, 
                true, whatGroup);
    }

//    /**
//     * @param pGrouperModel the grouperModel to set
//     * @return self reference
//     */
//    public CpxJobImportConfig setGrouperModel(GDRGModel pGrouperModel) {
//        this.grouperModel = pGrouperModel;
//        return this;
//    }
//    /**
//     * @param pGrouperModel the grouperModel to set
//     */
//    public void setGrouperModel(String pGrouperModel) {
//        final String model = pGrouperModel == null ? "" : pGrouperModel.trim();
//        if (model.isEmpty()) {
//            this.grouperModel = null;
//        } else {
//            GDRGModel m = null;
//            for (GDRGModel tmp : GDRGModel.values()) {
//                if (model.equalsIgnoreCase(tmp.name())) {
//                    m = tmp;
//                    break;
//                }
//            }
//            if (m == null) {
//                LOG.log(Level.WARNING, "was not able to find grouper model: " + model);
//            }
//            this.grouperModel = m;
//        }
//    }
//    /**
//     * @param rebuildIndexes the rebuildIndexes to set
//     * @return self reference
//     */
//    public CpxJobImportConfig setRebuildIndexes(boolean rebuildIndexes) {
//        this.rebuildIndexes = rebuildIndexes;
//        return this;
//    }
}
