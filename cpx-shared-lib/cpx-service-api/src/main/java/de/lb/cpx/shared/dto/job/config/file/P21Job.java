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
package de.lb.cpx.shared.dto.job.config.file;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.shared.dto.job.config.CpxFileBasedImportJob;
import de.lb.cpx.shared.dto.job.config.CpxJobConstraints;
import de.lb.cpx.shared.dto.job.config.ImportMode;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 *
 * @author niemeier
 */
//@XmlRootElement(name = "Sap")
public class P21Job extends CpxFileBasedImportJob {

    private static final long serialVersionUID = 1L;

//    public P21Job() {
//
//    }
//    public P21Job(final String pInputDirectory) {
//        setInputDirectory(pInputDirectory);
//    }
    public P21Job(String pName, String pTargetDatabase, ImportMode pImportMode, String pInputDirectory, boolean pRebuildIndexes, GDRGModel pGrouperModel, CpxJobConstraints pConstraints, long pTimePeriodValue, ChronoUnit pTimePeriodUnit, Date pBeginDate, 
            Date pEndDate, boolean pActive, String pWhatGroup) {
        super(pName, pTargetDatabase, pImportMode, pInputDirectory, pRebuildIndexes, pGrouperModel, pConstraints, pTimePeriodValue, pTimePeriodUnit, pBeginDate, pEndDate, pActive, pWhatGroup);
    }

    public P21Job(String pInputDirectory, ImportMode pImportMode, boolean pRebuildIndexes, CpxJobConstraints pConstraints) {
        super(pInputDirectory, pImportMode, pRebuildIndexes, pConstraints);
    }

}
