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
public class Inka extends KainInka {

    private static final long serialVersionUID = 1L;

    private Long mCurrentNrSending = null;
    private boolean mIsSendedFl = false;
    private boolean mReady4SendingFl = false;
    private Date mSendingDate = null;

    public Inka(String pIkz, String pFallNr, String pPatNr) {
        super(pIkz, pFallNr, pPatNr);
    }

    public Inka(Case pCase, Patient pPatient) {
        super(pCase, pPatient);
    }

    @Override
    public LineI toLine() {
        LineI line = super.toLine();
        //INKA
        line.setValueObj(KainInkaLine.CURRENT_NR_SENDING, getCurrentNrSending());
        line.setValueObj(KainInkaLine.IS_SENDED_FL, isSendedFl());
        line.setValueObj(KainInkaLine.READY_4_SENDING_FL, isReady4SendingFl());
        line.setValueObj(KainInkaLine.SENDING_DATE, getSendingDate());
        return line;
    }

    @Override
    public String getMessageType() {
        return "INKA";
    }

    /**
     * @return the mCurrentNrSending
     */
    public Long getCurrentNrSending() {
        return mCurrentNrSending;
    }

    /**
     * @param pCurrentNrSending the mCurrentNrSending to set
     */
    public void setCurrentNrSending(final Long pCurrentNrSending) {
        this.mCurrentNrSending = pCurrentNrSending;
    }

    /**
     * @return the mIsSendedFl
     */
    public boolean isSendedFl() {
        return mIsSendedFl;
    }

    /**
     * @param pIsSendedFl the mIsSendedFl to set
     */
    public void isSendedFl(final boolean pIsSendedFl) {
        this.mIsSendedFl = pIsSendedFl;
    }

    /**
     * @return the mReady4SendingFl
     */
    public boolean isReady4SendingFl() {
        return mReady4SendingFl;
    }

    /**
     * @param pReady4SendingFl the mReady4SendingFl to set
     */
    public void setReady4SendingFl(final boolean pReady4SendingFl) {
        this.mReady4SendingFl = pReady4SendingFl;
    }

    /**
     * @return the mSendingDate
     */
    public Date getSendingDate() {
        return mSendingDate == null ? null : new Date(mSendingDate.getTime());
    }

    /**
     * @param pSendingDate the mSendingDate to set
     */
    public void setSendingDate(final Date pSendingDate) {
        this.mSendingDate = pSendingDate == null ? null : new Date(pSendingDate.getTime());
    }

    public void set(Inka pOther) {
        super.set(pOther);
    }

}
