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

import static de.lb.cpx.str.utils.StrUtils.*;
import dto.AbstractDto;
import dto.DtoI;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import line.AbstractLine;
import line.LineI;
import line.impl.KainInkaPvvLine;

/**
 *
 * @author niemeier
 */
public class KainInkaPvv extends AbstractDto<KainInkaPvv> {

    private static final long serialVersionUID = 1L;

    private Date mBillDate = null;
    private String mBillNr = "";
    private String mInformationKey30 = "";

    public final String mIkz;
    public final String mFallNr;
    public final String mPatNr;
    public final KainInka mKainInka;
    private final Long mKainInkaNr;
    private final Set<KainInkaPvt> kainInkaPvtList = new LinkedHashSet<>();

    public KainInkaPvv(final KainInka pKainInka) {
        if (pKainInka == null) {
            throw new IllegalArgumentException("KainInka is null!");
        }
        mKainInka = pKainInka;
        mKainInkaNr = pKainInka.getNr();
        mIkz = pKainInka.getIkz();
        mFallNr = pKainInka.getFallNr();
        mPatNr = pKainInka.getPatNr();
        mKainInka.addKainInkaPvv(this);
    }

    public KainInkaPvv(final String pIkz, final String pFallNr, final String pPatNr, final Long pKainInkaNr) {
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
        if (pKainInkaNr == null || pKainInkaNr.equals(0L)) {
            throw new IllegalArgumentException("KainInka number is empty!");
        }
        mKainInka = null;
        mKainInkaNr = pKainInkaNr;
        mIkz = ikz;
        mFallNr = fallNr;
        mPatNr = patNr;
    }

    @Override
    public LineI toLine() {
        KainInkaPvvLine line = (KainInkaPvvLine) AbstractDto.toLine(this);
        line.setValueObj(KainInkaPvvLine.KAIN_INKA_NR, getKainInkaNr());
        line.setValueObj(KainInkaPvvLine.BILL_DATE, getBillDate());
        line.setValueObj(KainInkaPvvLine.BILL_NR, getBillNr());
        line.setValueObj(KainInkaPvvLine.INFORMATION_KEY_30, getInformationKey30());
        return line;
    }

    @Override
    public Class<? extends AbstractLine> getLineClass() {
        return KainInkaPvvLine.class;
    }

    @Override
    public Set<DtoI> getDtos() {
        Set<DtoI> dtoSet = new LinkedHashSet<>();
        dtoSet.add(this);
        Iterator<KainInkaPvt> it = kainInkaPvtList.iterator();
        while (it.hasNext()) {
            KainInkaPvt kainInkaPvt = it.next();
            if (kainInkaPvt == null) {
                continue;
            }
            dtoSet.addAll(kainInkaPvt.getDtos());
        }
        return dtoSet;
    }

    @Override
    public String getIkz() {
        return mIkz;
    }

    @Override
    public String getFallNr() {
        return mFallNr;
    }

    @Override
    public String getPatNr() {
        return mPatNr;
    }

    @Override
    public Case getCase() {
        if (mKainInka != null) {
            return mKainInka.getCase();
        }
        return null;
    }

    protected boolean addKainInkaPvt(final KainInkaPvt pKainInkaPvt) {
        if (pKainInkaPvt == null) {
            throw new IllegalArgumentException("KainInkaPvt is null!");
        }
        return kainInkaPvtList.add(pKainInkaPvt);
    }

    /**
     * @return the mBillDate
     */
    public Date getBillDate() {
        return mBillDate == null ? null : new Date(mBillDate.getTime());
    }

    /**
     * @param pBillDate the mBillDate to set
     */
    public void setBillDate(final Date pBillDate) {
        this.mBillDate = pBillDate == null ? null : new Date(pBillDate.getTime());
    }

    /**
     * @return the mBillNr
     */
    public String getBillNr() {
        return mBillNr;
    }

    /**
     * @param pBillNr the mBillNr to set
     */
    public void setBillNr(final String pBillNr) {
        this.mBillNr = toStr(pBillNr);
    }

    /**
     * @return the mInformationKey30
     */
    public String getInformationKey30() {
        return mInformationKey30;
    }

    /**
     * @param pInformationKey30 the mInformationKey30 to set
     */
    public void setInformationKey30(final String pInformationKey30) {
        this.mInformationKey30 = toStr(pInformationKey30);
    }

    /**
     * @return the mKainInkaNr
     */
    public Long getKainInkaNr() {
        return mKainInkaNr;
    }

    @Override
    public void set(KainInkaPvv pOther) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
