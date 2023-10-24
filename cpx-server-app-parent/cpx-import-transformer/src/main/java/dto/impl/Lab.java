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
import line.impl.LabLine;

/**
 *
 * @author Dirk Niemeier
 */
public class Lab extends AbstractDto<Lab> {

    private static final long serialVersionUID = 1L;

    //  /**
//   * @return the mErbringungsart
//   */
//  public String getErbringungsart() {
//    return mErbringungsart;
//  }
//
//  /**
//   * @param pErbringungsart the mErbringungsart to set
//   */
//  public void setErbringungsart(final String pErbringungsart) {
//    this.mErbringungsart = toStr(pErbringungsart);
//  }
    private String mAnalysis = "";
    private String mBenchmark = "";
    private String mComment = "";
    private String mDescription = "";
    private String mGroup = "";
    private Date mAnalysisDate = null;
    private Integer mCategory = null;
    private Date mDate = null;
    private String mKisExternKey = "";
    private Integer mLockdel = null;
    private Double mMaxLimit = null;
    private Double mMinLimit = null;
    private Integer mPosition = null;
    private Double mValue = null;
    private Double mValue2 = null;
    private String mMethod = "";
    private String mRange = "";
    private String mText = "";
    private String mUnit = "";

    public final String mIkz;
    public final String mFallNr;
    public final Case mCase;

    public Lab(final Case pCase) {
        if (pCase == null) {
            throw new IllegalArgumentException("Case is null! Laboratory must be assigned to a case!");
        }
        mCase = pCase;
        mIkz = pCase.getIkz();
        mFallNr = pCase.getFallNr();
//        pCase.addLab(this);
    }

    public Lab(final String pIkz, final String pFallNr) {
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

    /**
     * @return the mAnalysis
     */
    public String getAnalysis() {
        return mAnalysis;
    }

    /**
     * @param pAnalysis the mAnalysis to set
     */
    public void setAnalysis(String pAnalysis) {
        this.mAnalysis = toStr(pAnalysis);
    }

    /**
     * @return the mBenchmark
     */
    public String getBenchmark() {
        return mBenchmark;
    }

    /**
     * @param pBenchmark the mBenchmark to set
     */
    public void setBenchmark(String pBenchmark) {
        this.mBenchmark = toStr(pBenchmark);
    }

    /**
     * @return the mComment
     */
    public String getComment() {
        return mComment;
    }

    /**
     * @param pComment the mComment to set
     */
    public void setComment(String pComment) {
        this.mComment = toStr(pComment);
    }

    /**
     * @return the mDescription
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * @param pDescription the mDescription to set
     */
    public void setDescription(String pDescription) {
        this.mDescription = toStr(pDescription);
    }

    /**
     * @return the mGroup
     */
    public String getGroup() {
        return mGroup;
    }

    /**
     * @param pGroup the mGroup to set
     */
    public void setGroup(String pGroup) {
        this.mGroup = toStr(pGroup);
    }

    /**
     * @return the mAnalysisDate
     */
    public Date getAnalysisDate() {
        return mAnalysisDate;
    }

    /**
     * @param pAnalysisDate the mAnalysisDate to set
     */
    public void setAnalysisDate(Date pAnalysisDate) {
        this.mAnalysisDate = pAnalysisDate;
    }

    /**
     * @return the mCategory
     */
    public Integer getCategory() {
        return mCategory;
    }

    /**
     * @param pCategory the mCategory to set
     */
    public void setCategory(Integer pCategory) {
        this.mCategory = pCategory;
    }

    /**
     * @return the mDate
     */
    public Date getDate() {
        return mDate;
    }

    /**
     * @param pDate the mDate to set
     */
    public void setDate(Date pDate) {
        this.mDate = pDate;
    }

    /**
     * @return the mKisExternKey
     */
    public String getKisExternKey() {
        return mKisExternKey;
    }

    /**
     * @param pKisExternKey the mKisExternKey to set
     */
    public void setKisExternKey(String pKisExternKey) {
        this.mKisExternKey = toStr(pKisExternKey);
    }

    /**
     * @return the mLockdel
     */
    public Integer getLockdel() {
        return mLockdel;
    }

    /**
     * @param pLockdel the mLockdel to set
     */
    public void setLockdel(Integer pLockdel) {
        this.mLockdel = pLockdel;
    }

    /**
     * @return the mMaxLimit
     */
    public Double getMaxLimit() {
        return mMaxLimit;
    }

    /**
     * @param pMaxLimit the mMaxLimit to set
     */
    public void setMaxLimit(Double pMaxLimit) {
        this.mMaxLimit = pMaxLimit;
    }

    /**
     * @return the mMinLimit
     */
    public Double getMinLimit() {
        return mMinLimit;
    }

    /**
     * @param pMinLimit the mMinLimit to set
     */
    public void setMinLimit(Double pMinLimit) {
        this.mMinLimit = pMinLimit;
    }

    /**
     * @return the mPosition
     */
    public Integer getPosition() {
        return mPosition;
    }

    /**
     * @param pPosition the mPosition to set
     */
    public void setPosition(Integer pPosition) {
        this.mPosition = pPosition;
    }

    /**
     * @return the mValue
     */
    public Double getValue() {
        return mValue;
    }

    /**
     * @param pValue the mValue to set
     */
    public void setValue(Double pValue) {
        this.mValue = pValue;
    }

    /**
     * @return the mValue2
     */
    public Double getValue2() {
        return mValue2;
    }

    /**
     * @param pValue2 the mValue2 to set
     */
    public void setValue2(Double pValue2) {
        this.mValue2 = pValue2;
    }

    /**
     * @return the mMethod
     */
    public String getMethod() {
        return mMethod;
    }

    /**
     * @param pMethod the mMethod to set
     */
    public void setMethod(String pMethod) {
        this.mMethod = toStr(pMethod);
    }

    /**
     * @return the mRange
     */
    public String getRange() {
        return mRange;
    }

    /**
     * @param pRange the mRange to set
     */
    public void setRange(String pRange) {
        this.mRange = toStr(pRange);
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
    public void setText(String pText) {
        this.mText = toStr(pText);
    }

    /**
     * @return the mUnit
     */
    public String getUnit() {
        return mUnit;
    }

    /**
     * @param pUnit the mUnit to set
     */
    public void setUnit(String pUnit) {
        this.mUnit = toStr(pUnit);
    }

    @Override
    public LabLine toLine() {
        LabLine line = (LabLine) AbstractDto.toLine(this);
        line.setValueObj(LabLine.LAB_ANALYSIS, getAnalysis());
        line.setValueObj(LabLine.LAB_BENCHMARK, getBenchmark());
        line.setValueObj(LabLine.LAB_COMMENT, getComment());
        line.setValueObj(LabLine.LAB_DESCRIPTION, getDescription());
        line.setValueObj(LabLine.LAB_GROUP, getGroup());
        line.setValueObj(LabLine.LAB_ANALYSIS_DATE, getAnalysisDate());
        line.setValueObj(LabLine.LAB_CATEGORY, getCategory());
        line.setValueObj(LabLine.LAB_DATE, getDate());
        line.setValueObj(LabLine.LAB_KIS_EXTERN_KEY, getKisExternKey());
        line.setValueObj(LabLine.LAB_LOCKDEL, getLockdel());
        line.setValueObj(LabLine.LAB_MAX_LIMIT, getMaxLimit());
        line.setValueObj(LabLine.LAB_MIN_LIMIT, getMinLimit());
        line.setValueObj(LabLine.LAB_POSITION, getPosition());
        line.setValueObj(LabLine.LAB_VALUE, getValue());
        line.setValueObj(LabLine.LAB_VALUE_2, getValue2());
        line.setValueObj(LabLine.LAB_METHOD, getMethod());
        line.setValueObj(LabLine.LAB_RANGE, getRange());
        line.setValueObj(LabLine.LAB_TEXT, getText());
        line.setValueObj(LabLine.LAB_UNIT, getUnit());
        //line.setValueObj(DepartmentLine.ERBRINGUNGSART, getErbringungsart());
        return line;
    }

    @Override
    public Class<? extends AbstractLine> getLineClass() {
        return LabLine.class;
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

    @Override
    public void set(Lab pOther) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
