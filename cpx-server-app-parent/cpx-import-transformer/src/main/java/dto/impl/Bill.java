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
import line.impl.BillLine;

/**
 *
 * @author Dirk Niemeier
 */
public class Bill extends AbstractDto<Bill> {

    private static final long serialVersionUID = 1L;

    private String mRechnungsnr = "";
    private Date mRechnungsdatum = null;
    private String mRechnungsart = "";

    private final Set<Fee> feeList = new LinkedHashSet<>();

    public final String mIkz;
    public final String mFallNr;
    public final Case mCase;

    public Bill(final Case pCase) {
        if (pCase == null) {
            throw new IllegalArgumentException("Case is null! Bill must be assigned to a case!");
        }
        mCase = pCase;
        mIkz = pCase.getIkz();
        mFallNr = pCase.getFallNr();
        pCase.addBill(this);
    }

    public Bill(final String pIkz, final String pFallNr) {
        String ikz = pIkz == null ? "" : pIkz.trim();
        String fallNr = pFallNr == null ? "" : pFallNr.trim();
        if (ikz.isEmpty()) {
            throw new IllegalArgumentException("IKZ is empty!");
        }
        if (fallNr.isEmpty()) {
            throw new IllegalArgumentException("Fallnr. is empty!");
        }
        mIkz = ikz;
        mFallNr = fallNr;
        mCase = null;
    }

    @Override
    public LineI toLine() {
        BillLine line = (BillLine) AbstractDto.toLine(this);
        line.setValueObj(BillLine.RECHNUNGSNR, mRechnungsnr);
        line.setValueObj(BillLine.RECHNUNGSDATUM, getRechnungsdatum());
        line.setValueObj(BillLine.RECHNUNGSART, getRechnungsart());
        return line;
    }

    @Override
    public Class<? extends AbstractLine> getLineClass() {
        return BillLine.class;
    }

    @Override
    public Set<DtoI> getDtos() {
        Set<DtoI> dtoSet = new LinkedHashSet<>();
        dtoSet.add(this);
        Iterator<Fee> it = feeList.iterator();
        while (it.hasNext()) {
            Fee fee = it.next();
            if (fee == null) {
                continue;
            }
            dtoSet.addAll(fee.getDtos());
        }
        return dtoSet;
    }

    protected boolean addFee(final Fee pFee) {
        if (pFee == null) {
            throw new IllegalArgumentException("Fee is null!");
        }
        return feeList.add(pFee);
    }

    /**
     * @return the Rechnungsnr
     */
    public String getRechnungsnr() {
        return mRechnungsnr;
    }

    /**
     * @param pRechnungsnr the code to set
     */
    public void setRechnungsnr(final String pRechnungsnr) {
        this.mRechnungsnr = toStr(pRechnungsnr);
    }

    /**
     * @return the mRechnungsdatum
     */
    public Date getRechnungsdatum() {
        return mRechnungsdatum == null ? null : new Date(mRechnungsdatum.getTime());
    }

    /**
     * @param pRechnungsdatum the mRechnungsdatum to set
     */
    public void setRechnungsdatum(final Date pRechnungsdatum) {
        this.mRechnungsdatum = pRechnungsdatum == null ? null : new Date(pRechnungsdatum.getTime());
    }

    /**
     * @return the mRechnungsart
     */
    public String getRechnungsart() {
        return mRechnungsart;
    }

    /**
     * @param pRechnungsart the mRechnungsart to set
     */
    public void setRechnungsart(String pRechnungsart) {
        this.mRechnungsart = toStr(pRechnungsart);
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
    public void set(Bill pOther) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
