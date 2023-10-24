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
package de.lb.cpx.shared.dto.job.config.other;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.grouper.model.transfer.BatchGroupParameter;
import de.lb.cpx.model.enums.DetailsFilterEn;
import de.lb.cpx.shared.dto.job.config.CpxJobConfig;
import de.lb.cpx.shared.dto.job.config.CpxJobConstraints;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 *
 * @author niemeier
 */
//@XmlRootElement(name = "Sap")
public class BatchgroupingJob extends CpxJobConfig {

    private static final long serialVersionUID = 1L;

    //public static final GDRGModel DEFAULT_GROUPER_MODEL = GDRGModel.AUTOMATIC;
    private final GDRGModel grouperModel;
    private final DetailsFilterEn details;

    public BatchgroupingJob(
            final String pName,
            final String pTargetDatabase,
            final GDRGModel pGrouperModel,
            final CpxJobConstraints pConstraints,
            final long pTimePeriodValue,
            final ChronoUnit pTimePeriodUnit,
            final Date pBeginDate,
            final Date pEndDate,
            final boolean pActive,
            final String pDetails
    ) {
        super(pName, pTargetDatabase, pConstraints, pTimePeriodValue, pTimePeriodUnit, pBeginDate, pEndDate, pActive);
        this.grouperModel = pGrouperModel;
        details = pDetails == null?DetailsFilterEn.ACTUAL_LOCAL:(pDetails.equalsIgnoreCase("both")?DetailsFilterEn.ACTUAL_BOTH:DetailsFilterEn.ACTUAL_LOCAL);
    }

    public BatchgroupingJob(
            final GDRGModel pGrouperModel,
            final CpxJobConstraints pConstraints
    ) {
        super(pConstraints);
        this.grouperModel = pGrouperModel;
        details = DetailsFilterEn.ACTUAL_LOCAL;
    }

    /**
     * @return the grouperModel
     */
    public GDRGModel getGrouperModel() {
        return grouperModel;
    }
//    /**
//     * @param pGrouperModel the grouperModel to set
//     * @return self
//     */
//    public BatchgroupingJob setGrouperModel(final GDRGModel pGrouperModel) {
//        this.grouperModel = pGrouperModel;
//        return this;
//    }
//
//    /**
//     * @param pGrouperModel the grouperModel to set
//     * @return self
//     */
//    public BatchgroupingJob setGrouperModel(final String pGrouperModel) {
//        final String model = pGrouperModel == null ? "" : pGrouperModel.trim();
//        GDRGModel m = null;
//        if (!model.isEmpty()) {
//            for (GDRGModel item : GDRGModel.values()) {
//                if (model.equalsIgnoreCase(item.name())) {
//                    m = item;
//                }
//            }
//
//            if (m == null) {
//                LOG.log(Level.WARNING, "invalid grouper model found in job config: " + model + " (will use default instead)");
//            }
//        }
//
//        this.grouperModel = m;
//        return this;
//    }

    public BatchGroupParameter getBatchParameter() {
        BatchGroupParameter param = new BatchGroupParameter();
        param.setModel(grouperModel);
        param.setDetailsFilter(details);
        return param;
    }

}
