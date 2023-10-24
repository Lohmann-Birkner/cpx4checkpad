/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.dto.job.config.other;

import de.lb.cpx.shared.dto.job.config.CpxJobConfig;
import de.lb.cpx.shared.dto.job.config.CpxJobConstraints;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 *
 * @author niemeier
 */
//@XmlRootElement(name = "Sap")
public class BillJob extends CpxJobConfig {

    private static final long serialVersionUID = 1L;

    private final String inputDirectory;

    public BillJob(
            final String pName,
            final String pTargetDatabase,
            final String pInputDirectory,
            final CpxJobConstraints pConstraints,
            final long pTimePeriodValue,
            final ChronoUnit pTimePeriodUnit,
            final Date pBeginDate,
            final Date pEndDate,
            final boolean pActive
    ) {
        super(pName, pTargetDatabase, pConstraints, pTimePeriodValue, pTimePeriodUnit, pBeginDate, pEndDate, pActive);
        inputDirectory = pInputDirectory;
    }

    public BillJob(
            final String pInputDirectory,
            final CpxJobConstraints pConstraints
    ) {
        super(pConstraints);
        inputDirectory = pInputDirectory;
    }

    public String getInputDirectory() {
        return inputDirectory;
    }

}
