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
import dto.types.Lokalisation;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import line.AbstractLine;
import line.LineI;
import line.impl.ProcedureLine;

/**
 *
 * @author Dirk Niemeier
 */
public class Procedure extends AbstractDto<Procedure> {

    private static final long serialVersionUID = 1L;

    private String mCode = "";
    //protected String mFab = "";
    private Date mDatum;
    //private String mStation = "";
    private Lokalisation mLokalisation = null;
    private boolean mToGroup = true;
    private int mBelegOP = 0;
    private int mBelegAna = 0;
    private int mBbelegHeb = 0;

    public final long mDepNr;
    public final Long mWardNr;
    public final String mIkz;
    public final String mFallNr;
    public final Department mDepartment;
    public final Ward mWard;

    public Procedure(final Department pDepartment) {
        this(pDepartment, null);
    }

    public Procedure(final Department pDepartment, final Ward pWard) {
        if (pDepartment == null) {
            throw new IllegalArgumentException("Department is null! Department must be assigned to a diagnosis!");
        }
        mDepartment = pDepartment;
        mWard = pWard;
        mDepNr = pDepartment.getNr();
        mWardNr = pWard == null ? null : pWard.getNr();
        mIkz = pDepartment.getIkz();
        mFallNr = pDepartment.getFallNr();
        pDepartment.addProcedure(this);
        if (pWard != null) {
            pWard.addProcedure(this);
        }
    }

    public Procedure(final String pIkz, final String pFallNr, final Long pDepartmentNr) {
        this(pIkz, pFallNr, pDepartmentNr, null);
    }

    public Procedure(final String pIkz, final String pFallNr, final Long pDepartmentNr, final Long pWardNo) {
        String ikz = pIkz == null ? "" : pIkz.trim();
        String fallNr = pFallNr == null ? "" : pFallNr.trim();
        if (ikz.isEmpty()) {
            throw new IllegalArgumentException("IKZ is empty!");
        }
        if (fallNr.isEmpty()) {
            throw new IllegalArgumentException("Fallnr. is empty!");
        }
        if (pDepartmentNr == null || pDepartmentNr.equals(0L)) {
            throw new IllegalArgumentException("Department number is empty!");
        }
        mIkz = ikz;
        mFallNr = fallNr;
        mDepNr = pDepartmentNr;
        mWardNr = (pWardNo != null && pWardNo.equals(0L)) ? null : pWardNo;
        mDepartment = null;
        mWard = null;
    }

    public Department getDepartment() {
        return mDepartment;
    }

    public Ward getWard() {
        return mWard;
    }

    @Override
    public LineI toLine() {
        ProcedureLine line = (ProcedureLine) AbstractDto.toLine(this);
        line.setValueObj(ProcedureLine.CODE, getCode());
        line.setValueObj(ProcedureLine.DATUM, getDatum());
        line.setValueObj(ProcedureLine.DEP_NR, getDepartmentNr());
        line.setValueObj(ProcedureLine.WARD_NR, getWardNr());
        line.setValueObj(ProcedureLine.LOKALISATION, (getLokalisation() == null) ? "" : getLokalisation().getValue());
        line.setValueObj(ProcedureLine.TO_GROUP, isToGroup());
        line.setValueObj(ProcedureLine.BELEG_OP, getBelegOP());
        line.setValueObj(ProcedureLine.BELEG_ANA, getBelegAna());
        line.setValueObj(ProcedureLine.BELEG_HEB, getBelegHeb());
        return line;
    }

    @Override
    public Class<? extends AbstractLine> getLineClass() {
        return ProcedureLine.class;
    }

    @Override
    public Set<DtoI> getDtos() {
        Set<DtoI> dtoSet = new LinkedHashSet<>();
        dtoSet.add(this);
        return dtoSet;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return mCode;
    }

    /**
     * @param pCode the code to set
     */
    public void setCode(final String pCode) {
        String code = toStr(pCode);
        if (!code.isEmpty()) {
            if (code.length() >= 2 && code.charAt(1) != '-') {
                code = code.substring(0, 1) + '-' + code.substring(1);
            }
            if (code.length() >= 6 && code.charAt(5) != '.') {
                code = code.substring(0, 5) + '.' + code.substring(5);
            }
        }
        this.mCode = code;
    }

    @Override
    public Case getCase() {
        if (mDepartment != null) {
            return mDepartment.mCase;
        }
        return null;
    }

    /**
     * @return the mDatum
     */
    public Date getDatum() {
        return mDatum == null ? null : new Date(mDatum.getTime());
    }

    /**
     * @param pDatum the mDatum to set
     */
    public void setDatum(final Date pDatum) {
        this.mDatum = pDatum == null ? null : new Date(pDatum.getTime());
    }

//  /**
//   * @return the mStation
//   */
//  public String getStation() {
//    return mStation;
//  }
//
//  /**
//   * @param pStation the mStation to set
//   */
//  public void setStation(final String pStation) {
//    this.mStation = toStr(pStation);
//  }
    /**
     * @return the mLokalisation
     */
    public Lokalisation getLokalisation() {
        return mLokalisation;
    }

    /**
     * @param pLokalisation the pLokalisation to set
     */
    public void setLokalisation(final Lokalisation pLokalisation) {
        this.mLokalisation = pLokalisation;
    }

    /**
     * @param pLokalisation the pLokalisation to set
     */
    public void setLokalisation(final String pLokalisation) {
        final String lokalisation = pLokalisation == null ? "" : pLokalisation.trim();
        if (lokalisation.isEmpty()) {
            this.mLokalisation = null;
        } else {
            this.mLokalisation = Lokalisation.findByValue(lokalisation);
        }
    }

    /**
     * @return the mToGroup
     */
    public boolean isToGroup() {
        return mToGroup;
    }

    /**
     * @param pToGroup the mToGroup to set
     */
    public void setToGroup(final boolean pToGroup) {
        this.mToGroup = pToGroup;
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

    public long getDepartmentNr() {
        return mDepNr;
    }

    public Long getWardNr() {
        return (mWard == null) ? null : mWard.getNr();
    }

    @Override
    public void set(Procedure pOther) {
        mCode = pOther.mCode;
        mDatum = pOther.mDatum;
        mLokalisation = pOther.mLokalisation;
        mToGroup = pOther.mToGroup;
    }

    /**
     * @return the mBelegOP
     */
    public int getBelegOP() {
        return mBelegOP;
    }

    /**
     * @param pBelegOP the mBelegOP to set
     */
    public void setBelegOP(final int pBelegOP) {
        this.mBelegOP = pBelegOP;
    }

    /**
     * @return the mBelegAna
     */
    public int getBelegAna() {
        return mBelegAna;
    }

    /**
     * @param pBelegAna the mBelegAna to set
     */
    public void setBelegAna(final int pBelegAna) {
        this.mBelegAna = pBelegAna;
    }

    /**
     * @return the mBbelegHeb
     */
    public int getBelegHeb() {
        return mBbelegHeb;
    }

    /**
     * @param pBelegHeb the mBbelegHeb to set
     */
    public void setBelegHeb(final int pBelegHeb) {
        this.mBbelegHeb = pBelegHeb;
    }

}
