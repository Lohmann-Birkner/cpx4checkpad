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
 *    2017  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package dto.impl;

import static de.lb.cpx.str.utils.StrUtils.*;
import dto.AbstractDto;
import dto.DtoI;
import java.util.LinkedHashSet;
import java.util.Set;
import line.AbstractLine;
import line.LineI;
import line.impl.SapFiBillpositionLine;

/**
 *
 * @author Dirk Niemeier
 */
public class SapFiBillposition extends AbstractDto<SapFiBillposition> {

    private static final long serialVersionUID = 1L;

    private Double mAmount = null;
    private Double mBaseValue = null;
    private String mInvoice = "";
    private Double mNetValue = null;
    private String mPositionNumber = "";
    private String mReferenceId = "";
    private String mText = "";
    //private String mSapFiBillNr = "";

    public final String mIkz;
    public final String mFallNr;
    public final String mPatNr;
    public final long mSapFiBillNr;
    public final SapFiBill mSapFiBill;

    public SapFiBillposition(final SapFiBill pSapFiBill) {
        if (pSapFiBill == null) {
            throw new IllegalArgumentException("SAP bill is null! SAP bill position must be assigned to a SAP bill!");
        }
        mSapFiBill = pSapFiBill;
        mIkz = pSapFiBill.getIkz();
        mFallNr = pSapFiBill.getFallNr();
        mSapFiBillNr = pSapFiBill.getNr();
        mSapFiBill.addPosition(this);
        mPatNr = pSapFiBill.getPatNr();
    }

//    public SapFiBillposition(final String pIkz, final String pFallNr) {
//        this(pIkz, pFallNr, "");
//    }
    public SapFiBillposition(final String pIkz, final String pFallNr, final String pPatNr, final Long pSapFiBillNr) {
        String ikz = pIkz == null ? "" : pIkz.trim();
        String fallNr = pFallNr == null ? "" : pFallNr.trim();
        String patNr = pPatNr == null ? "" : pPatNr.trim();
        //String rechnungsNr = pRechnungsNr == null ? "" : pRechnungsNr.trim();
        if (ikz.isEmpty()) {
            throw new IllegalArgumentException("IKZ is empty!");
        }
        if (fallNr.isEmpty()) {
            throw new IllegalArgumentException("Fallnr. is empty!");
        }
        if (patNr.isEmpty()) {
            throw new IllegalArgumentException("Patnr. is empty!");
        }
        if (pSapFiBillNr == null || pSapFiBillNr.equals(0L)) {
            throw new IllegalArgumentException("SAP bill number is empty!");
        }
        mIkz = ikz;
        mFallNr = fallNr;
        mSapFiBillNr = pSapFiBillNr;
        mSapFiBill = null;
        mPatNr = patNr;
    }

    public SapFiBill getSapFiBill() {
        return mSapFiBill;
    }

    @Override
    public LineI toLine() {
        SapFiBillpositionLine line = (SapFiBillpositionLine) AbstractDto.toLine(this);
        line.setValueObj(SapFiBillpositionLine.SAP_FI_BILL_NR, getSapFiBillNr());
        line.setValueObj(SapFiBillpositionLine.AMOUNT, getAmount());
        line.setValueObj(SapFiBillpositionLine.BASE_VALUE, getBaseValue());
        line.setValueObj(SapFiBillpositionLine.INVOICE, getInvoice());
        line.setValueObj(SapFiBillpositionLine.NET_VALUE, getNetValue());
        line.setValueObj(SapFiBillpositionLine.POSITION_NUMBER, getPositionNumber());
        line.setValueObj(SapFiBillpositionLine.REFERENCE_ID, getReferenceId());
        line.setValueObj(SapFiBillpositionLine.TEXT, getText());
        return line;
    }

    @Override
    public Class<? extends AbstractLine> getLineClass() {
        return SapFiBillpositionLine.class;
    }

    @Override
    public Set<DtoI> getDtos() {
        Set<DtoI> dtoSet = new LinkedHashSet<>();
        dtoSet.add(this);
        return dtoSet;
    }

    /**
     * @return the Amount
     */
    public Double getAmount() {
        return mAmount;
    }

    /**
     * @param pAmount the Amount to set
     */
    public void setAmount(final Double pAmount) {
        this.mAmount = pAmount;
    }

    /**
     * @return the BaseValue
     */
    public Double getBaseValue() {
        return mBaseValue;
    }

    /**
     * @param pBaseValue the BaseValue to set
     */
    public void setBaseValue(final Double pBaseValue) {
        this.mBaseValue = pBaseValue;
    }

    /**
     * @return the Invoice
     */
    public String getInvoice() {
        return mInvoice;
    }

    /**
     * @param pInvoice the Invoice to set
     */
    public void setInvoice(final String pInvoice) {
        this.mInvoice = toStr(pInvoice);
    }

    /**
     * @return the NetValue
     */
    public Double getNetValue() {
        return mNetValue;
    }

    /**
     * @param pNetValue the NetValue to set
     */
    public void setNetValue(final Double pNetValue) {
        this.mNetValue = pNetValue;
    }

    /**
     * @return the PositionNumber
     */
    public String getPositionNumber() {
        return mPositionNumber;
    }

    /**
     * @param pPositionNumber the PositionNumber to set
     */
    public void setPositionNumber(final String pPositionNumber) {
        this.mPositionNumber = toStr(pPositionNumber);
    }

    /**
     * @return the ReferenceId
     */
    public String getReferenceId() {
        return mReferenceId;
    }

    /**
     * @param pReferenceId the ReferenceId to set
     */
    public void setReferenceId(final String pReferenceId) {
        this.mReferenceId = toStr(pReferenceId);
    }

    /**
     * @return the Text
     */
    public String getText() {
        return mText;
    }

    /**
     * @param pText the Text to set
     */
    public void setText(final String pText) {
        this.mText = toStr(pText);
    }

    /**
     * @return the SapFiBillNr
     */
    public long getSapFiBillNr() {
        return mSapFiBillNr;
    }

    @Override
    public Case getCase() {
        if (mSapFiBill != null) {
            return mSapFiBill.mCase;
        }
        return null;
    }

    /**
     * @return the ikz REMOVING THIS METHODS RESULTS IN AN STACK OVERFLOW!
     */
    @Override
    public String getIkz() {
        return mIkz;
    }

    /**
     * @return the fallNr REMOVING THIS METHODS RESULTS IN AN STACK OVERFLOW!
     */
    @Override
    public String getFallNr() {
        return mFallNr;
    }

    @Override
    public String getPatNr() {
        return mPatNr;
    }

    @Override
    public void set(SapFiBillposition pOther) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
