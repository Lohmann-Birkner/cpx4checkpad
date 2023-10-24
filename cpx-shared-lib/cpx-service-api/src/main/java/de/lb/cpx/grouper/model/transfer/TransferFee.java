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

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author gerschmann
 */
public class TransferFee implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int m_daysNotConsidered;
    private final int m_count;
    private final Date m_calcTo;
    private final Date m_calcFrom;
    private final double m_value;
    private final String m_feeType;

    public TransferFee(String feeType, double value, Date calcFrom, Date calcTo, int count, int daysNotConsidered) {
        m_feeType = feeType;
        m_value = value;
        m_calcFrom = calcFrom == null ? null : new Date(calcFrom.getTime());
        m_calcTo = calcTo == null ? null : new Date(calcTo.getTime());
        m_count = count;
        m_daysNotConsidered = daysNotConsidered;

    }

    public int getDaysNotConsidered() {
        return m_daysNotConsidered;
    }

    public int getCount() {
        return m_count;
    }

    public Date getCalcTo() {
        return m_calcTo == null ? null : new Date(m_calcTo.getTime());
    }

    public Date getCalcFrom() {
        return m_calcFrom == null ? null : new Date(m_calcFrom.getTime());
    }

    public double getValue() {
        return m_value;
    }

    public String getFeeType() {
        return m_feeType;
    }
}
