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
 *    2016  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.grouper.model.transfer;

import de.lb.cpx.service.information.BaserateTypeEn;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author gerschmann
 */
public class TransferBaseRate implements Serializable {

    private static final long serialVersionUID = 1L;

    private double mValue;
    private Date mValidFrom;
    private Date mValidTo;
    private BaserateTypeEn mBaserateType;

    public TransferBaseRate(BaserateTypeEn pBaserateType, double value, Date from, Date to) {
        mBaserateType = pBaserateType;
        mValue = value;
        mValidFrom = from == null ? null : new Date(from.getTime());
        mValidTo = to == null ? null : new Date(to.getTime());
    }

    public double getValue() {
        return mValue;
    }

    public void seValue(double m_value) {
        this.mValue = m_value;
    }

    public void setValidFrom(Date m_validFrom) {
        this.mValidFrom = m_validFrom == null ? null : new Date(m_validFrom.getTime());
    }

    public void setValidTo(Date m_validTo) {
        this.mValidTo = m_validTo == null ? null : new Date(m_validTo.getTime());
    }

    public Date getValidTo() {
        return mValidTo == null ? null : new Date(mValidTo.getTime());
    }

    public Date getValidFrom() {
        return mValidFrom == null ? null : new Date(mValidFrom.getTime());
    }

    public BaserateTypeEn getBaserateType() {
        return mBaserateType;
    }

    public void setBaserateType(BaserateTypeEn pBaserateType) {
        this.mBaserateType = pBaserateType;
    }

}
