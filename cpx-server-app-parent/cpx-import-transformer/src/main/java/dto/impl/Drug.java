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
import line.impl.DrugLine;

/**
 *
 * @author Dirk Niemeier
 */
public class Drug extends AbstractDto<Drug> {

    private static final long serialVersionUID = 1L;

    private String mPicNr = "";
    private Double mFaktor = null;
    private Double mBrutto = null;
    private String mPzn = null;
    private String mAtc = null;
    private Integer mGenerika = null;
    private String mDf = null;
    private String mPackungsgr = null;
    private Integer mNormgr = null;
    private Integer mArztnr = null;
    private Double mBruttoGesamt = null;
    private String mApoik = null;
    private Integer mBmg = null;
    private Integer mUnfk = null;
    private Date mVerord = null;
    private Date mAbg = null;

    public final String mIkz;
    public final String mFallNr;
    public final String mPatNr;
    public final Case mCase;

    public Drug(final Case pCase) {
        if (pCase == null) {
            throw new IllegalArgumentException("Case is null! Drug has to be assigned to a patient!");
        }
        mCase = pCase;
        mIkz = pCase.getIkz();
        mFallNr = pCase.getFallNr();
        mPatNr = pCase.getPatNr();
        pCase.addDrug(this);
    }

    public Drug(final Patient pPatient) {
        if (pPatient == null) {
            throw new IllegalArgumentException("Patient is null! Drug has to be assigned to a patient!");
        }
        mCase = null;
        mIkz = "";
        mFallNr = "";
        mPatNr = pPatient.getPatNr();
    }

//    public SapFiBillposition(final String pIkz, final String pFallNr) {
//        this(pIkz, pFallNr, "");
//    }
    public Drug(final String pIkz, final String pFallNr, final String pPatNr) {
        String ikz = pIkz == null ? "" : pIkz.trim();
        String fallNr = pFallNr == null ? "" : pFallNr.trim();
        String patNr = pPatNr == null ? "" : pPatNr.trim();
        //String rechnungsNr = pRechnungsNr == null ? "" : pRechnungsNr.trim();
//        if (ikz.isEmpty()) {
//            throw new IllegalArgumentException("IKZ is empty!");
//        }
        if (patNr.isEmpty()) {
            throw new IllegalArgumentException("Patnr. is empty!");
        }
//        if (fallNr.isEmpty()) {
//            throw new IllegalArgumentException("Fallnr. is empty!");
//        }
        mIkz = ikz;
        mFallNr = fallNr;
        mCase = null;
        mPatNr = patNr;
    }

    @Override
    public LineI toLine() {
        DrugLine line = (DrugLine) AbstractDto.toLine(this);
        line.setValueObj(DrugLine.PICNR, getPicNr());
        line.setValueObj(DrugLine.FAKTOR, getFaktor());
        line.setValueObj(DrugLine.BRUTTO, getBrutto());
        line.setValueObj(DrugLine.PZN, getPzn());
        line.setValueObj(DrugLine.ATC, getAtc());
        line.setValueObj(DrugLine.GENERIKA, getGenerika());
        line.setValueObj(DrugLine.DF, getDf());
        line.setValueObj(DrugLine.PACKUNGSGR, getPackungsgr());
        line.setValueObj(DrugLine.NORMGR, getNormgr());
        line.setValueObj(DrugLine.ARZTNR, getArztnr());
        line.setValueObj(DrugLine.BRUTTO_GESAMT, getBruttoGesamt());
        line.setValueObj(DrugLine.APOIK, getApoik());
        line.setValueObj(DrugLine.BMG, getBmg());
        line.setValueObj(DrugLine.UNFK, getUnfk());
        line.setValueObj(DrugLine.VERORD, getVerord());
        line.setValueObj(DrugLine.ABG, getAbg());
        return line;
    }

    @Override
    public Class<? extends AbstractLine> getLineClass() {
        return DrugLine.class;
    }

    @Override
    public Set<DtoI> getDtos() {
        Set<DtoI> dtoSet = new LinkedHashSet<>();
        dtoSet.add(this);
        return dtoSet;
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

    public String getPicNr() {
        return mPicNr;
    }

    public void setPicNr(String pPicNr) {
        this.mPicNr = toStr(pPicNr);
    }

    public Double getFaktor() {
        return mFaktor;
    }

    public void setFaktor(Double pFaktor) {
        this.mFaktor = pFaktor;
    }

    public Double getBrutto() {
        return mBrutto;
    }

    public void setBrutto(Double pBrutto) {
        this.mBrutto = pBrutto;
    }

    public String getPzn() {
        return mPzn;
    }

    public void setPzn(String pPzn) {
        this.mPzn = toStr(pPzn);
    }

    public String getAtc() {
        return mAtc;
    }

    public void setAtc(String pAtc) {
        this.mAtc = toStr(pAtc);
    }

    public Integer getGenerika() {
        return mGenerika;
    }

    public void setGenerika(Integer pGenerika) {
        this.mGenerika = pGenerika;
    }

    public String getDf() {
        return mDf;
    }

    public void setDf(String pDf) {
        this.mDf = pDf;
    }

    public String getPackungsgr() {
        return mPackungsgr;
    }

    public void setPackungsgr(String pPackungsgr) {
        this.mPackungsgr = toStr(pPackungsgr);
    }

    public Integer getNormgr() {
        return mNormgr;
    }

    public void setNormgr(Integer pNormgr) {
        this.mNormgr = pNormgr;
    }

    public Integer getArztnr() {
        return mArztnr;
    }

    public void setArztnr(Integer pArztnr) {
        this.mArztnr = pArztnr;
    }

    public Double getBruttoGesamt() {
        return mBruttoGesamt;
    }

    public void setBruttoGesamt(Double pBruttoGesamt) {
        this.mBruttoGesamt = pBruttoGesamt;
    }

    public String getApoik() {
        return mApoik;
    }

    public void setApoik(String pApoik) {
        this.mApoik = toStr(pApoik);
    }

    public Integer getBmg() {
        return mBmg;
    }

    public void setBmg(Integer pBmg) {
        this.mBmg = pBmg;
    }

    public Integer getUnfk() {
        return mUnfk;
    }

    public void setUnfk(Integer pUnfk) {
        this.mUnfk = pUnfk;
    }

    public Date getVerord() {
        return mVerord == null ? null : new Date(mVerord.getTime());
    }

    public void setVerord(Date pVerord) {
        this.mVerord = pVerord == null ? null : new Date(pVerord.getTime());;
    }

    public Date getAbg() {
        return mAbg == null ? null : new Date(mAbg.getTime());
    }

    public void setAbg(Date pAbg) {
        this.mAbg = pAbg == null ? null : new Date(pAbg.getTime());
    }

    @Override
    public void set(Drug pOther) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
