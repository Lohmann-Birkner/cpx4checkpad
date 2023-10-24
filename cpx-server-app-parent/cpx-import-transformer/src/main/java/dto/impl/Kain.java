/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  niemeier - initial API and implementation and/or initial documentation
 */
package dto.impl;

import java.util.Date;
import line.LineI;
import line.impl.KainInkaLine;

/**
 *
 * @author niemeier
 */
public class Kain extends KainInka {

    private static final long serialVersionUID = 1L;

    private Date mReceivingDate = null;

    public Kain(String pIkz, String pFallNr, String pPatNr) {
        super(pIkz, pFallNr, pPatNr);
    }

    public Kain(Case pCase, Patient pPatient) {
        super(pCase, pPatient);
    }

    @Override
    public LineI toLine() {
        LineI line = super.toLine();
        //KAIN
        line.setValueObj(KainInkaLine.RECEIVING_DATE, getReceivingDate());
        return line;
    }

    @Override
    public String getMessageType() {
        return "KAIN";
    }

    /**
     * @return the mReceivingDate
     */
    public Date getReceivingDate() {
        return mReceivingDate == null ? null : new Date(mReceivingDate.getTime());
    }

    /**
     * @param pReceivingDate the mReceivingDate to set
     */
    public void setReceivingDate(final Date pReceivingDate) {
        this.mReceivingDate = pReceivingDate == null ? null : new Date(pReceivingDate.getTime());
    }

    public void set(Kain pOther) {
        super.set(pOther);
    }

}
