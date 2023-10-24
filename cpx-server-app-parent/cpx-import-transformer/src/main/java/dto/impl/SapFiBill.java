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
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import line.AbstractLine;
import line.LineI;
import line.impl.SapFiBillLine;

/**
 *
 * @author Dirk Niemeier
 */
public class SapFiBill extends AbstractDto<SapFiBill> {

    private static final long serialVersionUID = 1L;

    private String mInvoice = "";
    private Integer mFiscalYear = null;
    private Date mInvoiceDate = null;
    private String mInvoiceKind = "";
    private String mInvoiceType = "";
    private Double mNetValue = null;
    private String mReceiverRef = "";
    private String mReferenceCurrency = "";
    private String mReferenceType = "";
    private String mState = "";
    private String mStornoRef = "";

    public final Case mCase;

    private final Set<SapFiBillposition> positionList = new LinkedHashSet<>();

    public final String mIkz;
    public final String mFallNr;
    public final String mPatNr;

    public SapFiBill(final Case pCase, final Patient pPatient) {
        if (pCase == null) {
            throw new IllegalArgumentException("Case is null! SAP Bill must be assigned to a case!");
        }
        if (pPatient == null) {
            throw new IllegalArgumentException("Patient is null!");
        }
        mCase = pCase;
        mIkz = pCase.getIkz();
        mFallNr = pCase.getFallNr();
        mPatNr = pPatient.getPatNr();
        //pCase.addBill(this);
    }

    public SapFiBill(final String pIkz, final String pFallNr, final String pPatNr) {
        String ikz = pIkz == null ? "" : pIkz.trim();
        String fallNr = pFallNr == null ? "" : pFallNr.trim();
        String patNr = pPatNr == null ? "" : pPatNr.trim();
        if (ikz.isEmpty()) {
            throw new IllegalArgumentException("IKZ is empty!");
        }
        if (fallNr.isEmpty()) {
            throw new IllegalArgumentException("Fallnr. is empty!");
        }
        if (patNr.isEmpty()) {
            throw new IllegalArgumentException("Patnr. is empty!");
        }
        mIkz = ikz;
        mFallNr = fallNr;
        mPatNr = patNr;
        mCase = null;
    }

    @Override
    public LineI toLine() {
        SapFiBillLine line = (SapFiBillLine) AbstractDto.toLine(this);
        line.setValueObj(SapFiBillLine.INVOICE, getInvoice());
        line.setValueObj(SapFiBillLine.FISCAL_YEAR, getFiscalYear());
        line.setValueObj(SapFiBillLine.INVOICE_DATE, getInvoiceDate());
        line.setValueObj(SapFiBillLine.INVOICE_KIND, getInvoiceKind());
        line.setValueObj(SapFiBillLine.INVOICE_TYPE, getInvoiceType());
        line.setValueObj(SapFiBillLine.NET_VALUE, getNetValue());
        line.setValueObj(SapFiBillLine.RECEIVER_REF, getReceiverRef());
        line.setValueObj(SapFiBillLine.REFERENCE_CURRENCY, getReferenceCurrency());
        line.setValueObj(SapFiBillLine.REFERENCE_TYPE, getReferenceType());
        line.setValueObj(SapFiBillLine.STATE, getState());
        line.setValueObj(SapFiBillLine.STORNO_REF, getStornoRef());
        return line;
    }

    @Override
    public Class<? extends AbstractLine> getLineClass() {
        return SapFiBillLine.class;
    }

    @Override
    public Set<DtoI> getDtos() {
        Set<DtoI> dtoSet = new LinkedHashSet<>();
        dtoSet.add(this);
        Iterator<SapFiBillposition> it = positionList.iterator();
        while (it.hasNext()) {
            SapFiBillposition sapFiBillposition = it.next();
            if (sapFiBillposition == null) {
                continue;
            }
            dtoSet.addAll(sapFiBillposition.getDtos());
        }
        return dtoSet;
    }

    protected boolean addPosition(final SapFiBillposition pSapFiBillposition) {
        if (pSapFiBillposition == null) {
            throw new IllegalArgumentException("SAP bill position is null!");
        }
        return positionList.add(pSapFiBillposition);
    }

    /**
     * @return the FiscalYear
     */
    public Integer getFiscalYear() {
        return mFiscalYear;
    }

    /**
     * @param FiscalYear the FiscalYear to set
     */
    public void setFiscalYear(final Integer FiscalYear) {
        this.mFiscalYear = FiscalYear;
    }

    /**
     * @return the InvoiceDate
     */
    public Date getInvoiceDate() {
        return mInvoiceDate == null ? null : new Date(mInvoiceDate.getTime());
    }

    /**
     * @param pInvoiceDate the InvoiceDate to set
     */
    public void setInvoiceDate(final Date pInvoiceDate) {
        this.mInvoiceDate = pInvoiceDate == null ? null : new Date(pInvoiceDate.getTime());
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
     * @return the InvoiceKind
     */
    public String getInvoiceKind() {
        return mInvoiceKind;
    }

    /**
     * @param pInvoiceKind the InvoiceKind to set
     */
    public void setInvoiceKind(final String pInvoiceKind) {
        this.mInvoiceKind = toStr(pInvoiceKind);
    }

    /**
     * @return the InvoiceType
     */
    public String getInvoiceType() {
        return mInvoiceType;
    }

    /**
     * @param pInvoiceType the InvoiceType to set
     */
    public void setInvoiceType(final String pInvoiceType) {
        this.mInvoiceType = toStr(pInvoiceType);
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
     * @return the ReceiverRef
     */
    public String getReceiverRef() {
        return mReceiverRef;
    }

    /**
     * @param pReceiverRef the ReceiverRef to set
     */
    public void setReceiverRef(final String pReceiverRef) {
        this.mReceiverRef = toStr(pReceiverRef);
    }

    /**
     * @return the ReferenceCurrency
     */
    public String getReferenceCurrency() {
        return mReferenceCurrency;
    }

    /**
     * @param pReferenceCurrency the ReferenceCurrency to set
     */
    public void setReferenceCurrency(final String pReferenceCurrency) {
        this.mReferenceCurrency = toStr(pReferenceCurrency);
    }

    /**
     * @return the ReferenceType
     */
    public String getReferenceType() {
        return mReferenceType;
    }

    /**
     * @param pReferenceType the ReferenceType to set
     */
    public void setReferenceType(final String pReferenceType) {
        this.mReferenceType = toStr(pReferenceType);
    }

    /**
     * @return the State
     */
    public String getState() {
        return mState;
    }

    /**
     * @param pState the State to set
     */
    public void setState(final String pState) {
        this.mState = toStr(pState);
    }

    /**
     * @return the StornoRef
     */
    public String getStornoRef() {
        return mStornoRef;
    }

    /**
     * @param pStornoRef the StornoRef to set
     */
    public void setStornoRef(final String pStornoRef) {
        this.mStornoRef = toStr(pStornoRef);
    }

    @Override
    public Case getCase() {
        return mCase;
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
    public void set(SapFiBill pOther) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
