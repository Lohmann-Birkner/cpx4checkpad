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

import java.util.Date;

/**
 *
 * @author gerschmann
 */
public class TransferOps extends TransferCode {

    private static final long serialVersionUID = 1L;

    private final Date m_opsDate;
    private boolean m_used4grouping;

    public TransferOps(long id, String ops, int localisation, Date opsDate) {
        super(id, ops, localisation);
        m_opsDate = opsDate == null ? null : new Date(opsDate.getTime());
    }

    public Date getOpsDate() {
        return m_opsDate == null ? null : new Date(m_opsDate.getTime());
    }

    public void setUsed4grouping(boolean used) {
        m_used4grouping = used;
    }

    public boolean isUsed4grouping() {
        return m_used4grouping;
    }

}
