/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.grouper.model.transfer;

import java.util.Date;

/**
 * Transfer class for labor values for rules application
 *
 * @author gerschmann
 */
public class TransferLabor {

    private final double labValue;
    private final String labText;
    private final String labUnit;
    private final String labDescr;
    private final Date labAnalysedate;

    public TransferLabor(Double pLabValue, String pLabText, String pLabUnit, String pLabDescr, Date pLabAnalysedate) {
        labValue = pLabValue == null ? 0 : pLabValue.floatValue();
        labText = pLabText == null ? "" : pLabText;
        labUnit = pLabUnit == null ? "" : pLabUnit;
        labDescr = pLabDescr == null ? "" : pLabDescr;
        labAnalysedate = pLabAnalysedate;
    }

    public double getLabValue() {
        return labValue;
    }

    public String getLabText() {
        return labText;
    }

    public String getLabUnit() {
        return labUnit;
    }

    public String getLabDescr() {
        return labDescr;
    }

    public Date getLabAnalysedate() {
        return labAnalysedate;
    }

}
