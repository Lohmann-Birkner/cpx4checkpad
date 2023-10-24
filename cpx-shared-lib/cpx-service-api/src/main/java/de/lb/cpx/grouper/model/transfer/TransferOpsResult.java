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
public class TransferOpsResult extends TransferResult {

    private static final long serialVersionUID = 1L;

    private char m_resType; // Type der OPS. von Grouper ermittelt 0-A

    private TransferSupplementaryFee m_supplementaryFee;

    public TransferOpsResult(long id) {
        super(id);
    }

    public char getResType() {
        return m_resType;
    }

    public void setResType(char m_resType) {
        this.m_resType = m_resType;
    }

    public TransferSupplementaryFee getSupplementaryFee() {
        return m_supplementaryFee;
    }

    public void setSupplFee(TransferSupplementaryFee m_additionalFee) {
        this.m_supplementaryFee = m_additionalFee;
    }

}
