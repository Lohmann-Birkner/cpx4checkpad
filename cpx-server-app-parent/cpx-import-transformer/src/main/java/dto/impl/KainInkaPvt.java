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
import dto.types.Lokalisation;
import java.util.LinkedHashSet;
import java.util.Set;
import line.AbstractLine;
import line.LineI;
import line.impl.KainInkaPvtLine;

/**
 *
 * @author niemeier
 */
public class KainInkaPvt extends AbstractDto<KainInkaPvt> {

    private static final long serialVersionUID = 1L;

    private String mMainDiagIcd = "";
    private Lokalisation mMainDiagLoc = null;
    private String mMainDiagSecondaryIcd = "";
    private Lokalisation mMainDiagSecondaryLoc = null;
    private String mOpsCode = "";
    private Lokalisation mOpsLocalisation = null;
    private String mSecondaryDiagIcd = "";
    private Lokalisation mSecondaryDiagLoc = null;
    private String mSecondarySecondDiagIcd = "";
    private Lokalisation mSecondarySecondDiagLoc = null;
    private String mText = "";

    public final String mIkz;
    public final String mFallNr;
    public final String mPatNr;
    public final KainInkaPvv mKainInkaPvv;
    private final Long mKainInkaPvvNr;

    public KainInkaPvt(final KainInkaPvv pKainInkaPvv) {
        if (pKainInkaPvv == null) {
            throw new IllegalArgumentException("KainInkaPvv is null!");
        }
        mKainInkaPvv = pKainInkaPvv;
        mKainInkaPvvNr = pKainInkaPvv.getNr();
        mIkz = pKainInkaPvv.getIkz();
        mFallNr = pKainInkaPvv.getFallNr();
        mPatNr = pKainInkaPvv.getPatNr();
        mKainInkaPvv.addKainInkaPvt(this);
    }

    public KainInkaPvt(final String pIkz, final String pFallNr, final String pPatNr, final Long pKainInkaPvvNr) {
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
        if (pKainInkaPvvNr == null || pKainInkaPvvNr.equals(0L)) {
            throw new IllegalArgumentException("KainInkaPvv number is empty!");
        }
        mKainInkaPvv = null;
        mKainInkaPvvNr = pKainInkaPvvNr;
        mIkz = ikz;
        mFallNr = fallNr;
        mPatNr = patNr;
    }

    @Override
    public LineI toLine() {
        KainInkaPvtLine line = (KainInkaPvtLine) AbstractDto.toLine(this);
        line.setValueObj(KainInkaPvtLine.KAIN_INKA_PVV_NR, getKainInkaPvvNr());
        line.setValueObj(KainInkaPvtLine.MAIN_DIAG_ICD, getMainDiagIcd());
        line.setValueObj(KainInkaPvtLine.MAIN_DIAG_LOC_EN, (getMainDiagLoc() == null) ? "" : getMainDiagLoc().getValue());
        line.setValueObj(KainInkaPvtLine.MAIN_DIAG_SECONDARY_ICD, getMainDiagSecondaryIcd());
        line.setValueObj(KainInkaPvtLine.MAIN_DIAG_SECONDARY_LOC_EN, (getMainDiagSecondaryLoc() == null) ? "" : getMainDiagSecondaryLoc().getValue());
        line.setValueObj(KainInkaPvtLine.OPS_CODE, getOpsCode());
        line.setValueObj(KainInkaPvtLine.OPS_LOCALISATION_EN, (getOpsLocalisation() == null) ? "" : getOpsLocalisation().getValue());
        line.setValueObj(KainInkaPvtLine.SECONDARY_DIAG_ICD, getSecondaryDiagIcd());
        line.setValueObj(KainInkaPvtLine.SECONDARY_DIAG_LOC_EN, (getSecondaryDiagLoc() == null) ? "" : getSecondaryDiagLoc().getValue());
        line.setValueObj(KainInkaPvtLine.SECONDARY_SECOND_DIAG_ICD, getSecondarySecondDiagIcd());
        line.setValueObj(KainInkaPvtLine.SECONDARY_SECOND_DIAG_LOC_EN, (getSecondarySecondDiagLoc() == null) ? "" : getSecondarySecondDiagLoc().getValue());
        line.setValueObj(KainInkaPvtLine.TEXT, getText());
        return line;
    }

    @Override
    public Class<? extends AbstractLine> getLineClass() {
        return KainInkaPvtLine.class;
    }

    @Override
    public Set<DtoI> getDtos() {
        Set<DtoI> dtoSet = new LinkedHashSet<>();
        dtoSet.add(this);
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
        if (mKainInkaPvv != null) {
            return mKainInkaPvv.getCase();
        }
        return null;
    }

    /**
     * @return the mMainDiagIcd
     */
    public String getMainDiagIcd() {
        return mMainDiagIcd;
    }

    /**
     * @param pMainDiagIcd the mMainDiagIcd to set
     */
    public void setMainDiagIcd(final String pMainDiagIcd) {
        this.mMainDiagIcd = toStr(pMainDiagIcd);
    }

    /**
     * @return the mMainDiagLoc
     */
    public Lokalisation getMainDiagLoc() {
        return mMainDiagLoc;
    }

    /**
     * @param pMainDiagLoc the mMainDiagLoc to set
     */
    public void setMainDiagLoc(final Lokalisation pMainDiagLoc) {
        this.mMainDiagLoc = pMainDiagLoc;
    }

    /**
     * @return the mMainDiagSecondaryIcd
     */
    public String getMainDiagSecondaryIcd() {
        return mMainDiagSecondaryIcd;
    }

    /**
     * @param pMainDiagSecondaryIcd the mMainDiagSecondaryIcd to set
     */
    public void setMainDiagSecondaryIcd(final String pMainDiagSecondaryIcd) {
        this.mMainDiagSecondaryIcd = toStr(pMainDiagSecondaryIcd);
    }

    /**
     * @return the mOpsCode
     */
    public String getOpsCode() {
        return mOpsCode;
    }

    /**
     * @param pOpsCode the mOpsCode to set
     */
    public void setOpsCode(final String pOpsCode) {
        this.mOpsCode = toStr(pOpsCode);
    }

    /**
     * @return the mOpsLocalisation
     */
    public Lokalisation getOpsLocalisation() {
        return mOpsLocalisation;
    }

    /**
     * @param pOpsLocalisation the mOpsLocalisation to set
     */
    public void setOpsLocalisation(final Lokalisation pOpsLocalisation) {
        this.mOpsLocalisation = pOpsLocalisation;
    }

    /**
     * @return the mSecondaryDiagIcd
     */
    public String getSecondaryDiagIcd() {
        return mSecondaryDiagIcd;
    }

    /**
     * @param pSecondaryDiagIcd the mSecondaryDiagIcd to set
     */
    public void setSecondaryDiagIcd(final String pSecondaryDiagIcd) {
        this.mSecondaryDiagIcd = toStr(pSecondaryDiagIcd);
    }

    /**
     * @return the mSecondaryDiagLoc
     */
    public Lokalisation getSecondaryDiagLoc() {
        return mSecondaryDiagLoc;
    }

    /**
     * @param pSecondaryDiagLoc the mSecondaryDiagLoc to set
     */
    public void setSecondaryDiagLoc(final Lokalisation pSecondaryDiagLoc) {
        this.mSecondaryDiagLoc = pSecondaryDiagLoc;
    }

    /**
     * @return the mSecondarySeconddDiagIcd
     */
    public String getSecondarySecondDiagIcd() {
        return mSecondarySecondDiagIcd;
    }

    /**
     * @param pSecondarySecondDiagIcd the mSecondarySeconddDiagIcd to set
     */
    public void setSecondarySecondDiagIcd(final String pSecondarySecondDiagIcd) {
        this.mSecondarySecondDiagIcd = toStr(pSecondarySecondDiagIcd);
    }

    /**
     * @return the mSecondarySecondDiagLoc
     */
    public Lokalisation getSecondarySecondDiagLoc() {
        return mSecondarySecondDiagLoc;
    }

    /**
     * @param pSecondarySecondDiagLoc the mSecondarySecondDiagLoc to set
     */
    public void setSecondarySecondDiagLoc(final Lokalisation pSecondarySecondDiagLoc) {
        this.mSecondarySecondDiagLoc = pSecondarySecondDiagLoc;
    }

    /**
     * @return the mText
     */
    public String getText() {
        return mText;
    }

    /**
     * @param pText the mText to set
     */
    public void setText(final String pText) {
        this.mText = toStr(pText);
    }

    /**
     * @return the mKainInkaPvvNr
     */
    public Long getKainInkaPvvNr() {
        return mKainInkaPvvNr;
    }

    /**
     * @return the mMainDiagSecondaryLoc
     */
    public Lokalisation getMainDiagSecondaryLoc() {
        return mMainDiagSecondaryLoc;
    }

    /**
     * @param pMainDiagSecondaryLoc the mMainDiagSecondaryLoc to set
     */
    public void setMainDiagSecondaryLoc(final Lokalisation pMainDiagSecondaryLoc) {
        this.mMainDiagSecondaryLoc = pMainDiagSecondaryLoc;
    }

    @Override
    public void set(KainInkaPvt pOther) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
