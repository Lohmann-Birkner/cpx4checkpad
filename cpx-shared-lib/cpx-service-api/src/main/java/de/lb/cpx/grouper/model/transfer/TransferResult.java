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

/**
 * PEPP oder DRG
 *
 * @author gerschmann
 */
public class TransferResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long m_codeId;
    private boolean m_used4grouping;
    private int m_valid;

    public TransferResult(long id) {
        m_codeId = id;

    }

    public int getValid() {
        return m_valid;
    }

    public void setValid(int m_valid) {
        this.m_valid = m_valid;
    }

    public boolean isUsed4grouping() {
        return m_used4grouping;
    }

    public void setUsed4grouping(boolean m_used4grouping) {
        this.m_used4grouping = m_used4grouping;
    }

    public long getId() {
        return m_codeId;
    }

}
