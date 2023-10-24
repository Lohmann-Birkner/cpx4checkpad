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

/**
 *
 * @author gerschmann
 */
public class TransferIcdResult extends TransferResult {

    private static final long serialVersionUID = 1L;

    private int m_ccl;

    public TransferIcdResult(long id) {
        super(id);
    }

    public int getCCL() {
        return m_ccl;
    }

    public void setCCL(int ccl) {
        m_ccl = ccl;
    }
}
