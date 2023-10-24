/* 
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.util.auditquota;

import java.math.BigDecimal;

/**
 *
 * @author niemeier
 */
public class MdkAuditQuotaResult {

    public final String hospitalIdent;
    public final String insuranceIdent;
    public final Integer year;
    public final Integer quarter;
    public final long caseCount;
    public final long actualComplaints;
    public final BigDecimal givenQuota;
    public final long givenComplaints;

    public MdkAuditQuotaResult(final String pHospitalIdent, final String pInsuranceIdent,
            final Integer pYear, final Integer pQuarter,
            final long pCaseCount,
            final long pActualComplaints,
            final BigDecimal pGivenQuota, final long pGivenComplaints) {
        this.hospitalIdent = pHospitalIdent;
        this.insuranceIdent = pInsuranceIdent;
        this.year = pYear;
        this.quarter = pQuarter;
        this.caseCount = pCaseCount;
        this.actualComplaints = pActualComplaints;
        this.givenQuota = pGivenQuota;
        this.givenComplaints = pGivenComplaints;
    }

    public boolean isQuotaExceeded() {
        return (actualComplaints + 1) > givenComplaints;
    }

}
