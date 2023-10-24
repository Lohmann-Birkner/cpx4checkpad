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
import java.util.LinkedHashSet;
import java.util.Set;
import line.AbstractLine;
import line.LineI;
import line.impl.FeeLine;

/**
 *
 * @author Dirk Niemeier
 */
public class Fee extends AbstractDto<Fee> {

    private static final long serialVersionUID = 1L;

    private String mEntgeltschluessel = "";
    private int mAnzahl = 0;
    private double mBetrag = 0.0f;
    //protected float mSumme = "";
    private Date mVon = null;
    private Date mBis = null;
    private int mTob = 0;
    private String mKasse;

    public final String mIkz;
    public final String mFallNr;
    public final String mRechnungsNr;
    public final Bill mBill;
    public final Case mCase;

    public Fee(final Case pCase) {
        if (pCase == null) {
            throw new IllegalArgumentException("Bill is null! Fee must be assigned to a bill or case!");
        }
        mCase = pCase;
        mBill = null;
        mIkz = pCase.getIkz();
        mFallNr = pCase.getFallNr();
        mRechnungsNr = "";
        mCase.addFee(this);
    }

    public Fee(final Bill pBill) {
        if (pBill == null) {
            throw new IllegalArgumentException("Bill is null! Fee must be assigned to a bill or case!");
        }
        mCase = null;
        mBill = pBill;
        mIkz = pBill.getIkz();
        mFallNr = pBill.getFallNr();
        mRechnungsNr = pBill.getRechnungsnr();
        mBill.addFee(this);
    }

    public Fee(final String pIkz, final String pFallNr) {
        this(pIkz, pFallNr, "");
    }

    public Fee(final String pIkz, final String pFallNr, final String pRechnungsNr) {
        String ikz = pIkz == null ? "" : pIkz.trim();
        String fallNr = pFallNr == null ? "" : pFallNr.trim();
        String rechnungsNr = pRechnungsNr == null ? "" : pRechnungsNr.trim();
        if (ikz.isEmpty()) {
            throw new IllegalArgumentException("IKZ is empty!");
        }
        if (fallNr.isEmpty()) {
            throw new IllegalArgumentException("Fallnr. is empty!");
        }
        mIkz = ikz;
        mFallNr = fallNr;
        mRechnungsNr = rechnungsNr;
        mCase = null;
        mBill = null;
    }

    public Bill getBill() {
        return mBill;
    }

    @Override
    public LineI toLine() {
        FeeLine line = (FeeLine) AbstractDto.toLine(this);
        line.setValueObj(FeeLine.ENTGELTSCHLUESSEL, getEntgeltschluessel());
        line.setValueObj(FeeLine.ANZAHL, getAnzahl());
        line.setValueObj(FeeLine.BETRAG, getBetrag());
        line.setValueObj(FeeLine.SUMME, getSumme());
        line.setValueObj(FeeLine.VON, getVon());
        line.setValueObj(FeeLine.BIS, getBis());
        line.setValueObj(FeeLine.TOB, getTob());
        line.setValueObj(FeeLine.RECHNUNG_NR, getRechnungsNr());
        line.setValueObj(FeeLine.KASSE, getKasse());
        line.setValueObj(FeeLine.IS_RECHNUNG, isRechnung());
        return line;
    }

    @Override
    public Class<? extends AbstractLine> getLineClass() {
        return FeeLine.class;
    }

    @Override
    public Set<DtoI> getDtos() {
        Set<DtoI> dtoSet = new LinkedHashSet<>();
        dtoSet.add(this);
        return dtoSet;
    }

    public double getSumme() {
        double summe = getBetrag() * getAnzahl();
        return getKaufmaennischGerundet(summe, 2);
    }

    /**
     * @return the Entgeltschluessel
     */
    public String getEntgeltschluessel() {
        return mEntgeltschluessel;
    }

    /**
     * @param pEntgeltschluessel the code to set
     */
    public void setEntgeltschluessel(final String pEntgeltschluessel) {
        this.mEntgeltschluessel = toStr(pEntgeltschluessel);
    }

    @Override
    public Case getCase() {
        if (mCase != null) {
            return mCase;
        }
        if (mBill != null) {
            return mBill.mCase;
        }
        return null;
    }

    /**
     * @return the mAnzahl
     */
    public int getAnzahl() {
        return mAnzahl;
    }

    /**
     * @param pAnzahl the mAnzahl to set
     */
    public void setAnzahl(final int pAnzahl) {
        this.mAnzahl = pAnzahl;
    }

    /**
     * @return the mBetrag
     */
    public double getBetrag() {
        return mBetrag;
    }

    /**
     * @param pBetrag the mBetrag to set
     */
    public void setBetrag(final float pBetrag) {
        this.mBetrag = pBetrag;
    }

    /**
     * @param pBetrag the mBetrag to set
     */
    public void setBetrag(final double pBetrag) {
        this.mBetrag = pBetrag;
    }

    /**
     * @return the mVon
     */
    public Date getVon() {
        return mVon == null ? null : new Date(mVon.getTime());
    }

    /**
     * @param pVon the mVon to set
     */
    public void setVon(final Date pVon) {
        this.mVon = pVon == null ? null : new Date(pVon.getTime());
    }

    /**
     * @return the mBis
     */
    public Date getBis() {
        return mBis == null ? null : new Date(mBis.getTime());
    }

    /**
     * @param pBis the mBis to set
     */
    public void setBis(final Date pBis) {
        this.mBis = pBis == null ? null : new Date(pBis.getTime());
    }

    /**
     * @return the mTob
     */
    public int getTob() {
        return mTob;
    }

    /**
     * @param pTob the mTob to set
     */
    public void setTob(final int pTob) {
        this.mTob = pTob;
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

    public String getRechnungsNr() {
        return mRechnungsNr;
    }

    /**
     * @return the mKasse
     */
    public String getKasse() {
        return mKasse;
    }

    /**
     * @param pKasse the mKasse to set
     */
    public void setKasse(final String pKasse) {
        this.mKasse = toStr(pKasse);
    }

    public boolean isRechnung() {
        return !getRechnungsNr().isEmpty();
    }

    @Override
    public void set(Fee pOther) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
