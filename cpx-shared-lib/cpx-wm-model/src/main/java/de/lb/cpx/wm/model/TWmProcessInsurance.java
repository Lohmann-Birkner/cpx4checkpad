/* 
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.wm.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author Dirk Niemeier
 */
@Entity
@DiscriminatorValue(value = "INSURANCE") //enum value in WmProcessType
@Table(name = "T_WM_PROCESS_INSURANCE")
public class TWmProcessInsurance extends TWmProcess {

    private static final long serialVersionUID = 1L;

    private Double incomeAdmission;

    /**
     * Admission Income
     *
     * @return incomeAdmission
     */
    @Column(name = "INCOME_ADMISSION", precision = 10, scale = 2)
    public Double getIncomeAdmission() {
        return this.incomeAdmission;
    }

    public void setIncomeAdmission(final Double pIncomeAdmission) {
        this.incomeAdmission = pIncomeAdmission;
    }

    @Override
    public boolean versionEquals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof TWmProcessInsurance)) {
            return false;
        }
        final TWmProcessInsurance other = (TWmProcessInsurance) object;
        boolean returnVal = super.versionEquals(other);
        //if (returnVal) {
        //Check additional fields here...
        //}
        return returnVal;
    }
}
