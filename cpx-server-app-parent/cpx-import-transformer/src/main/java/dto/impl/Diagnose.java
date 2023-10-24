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
import dto.types.IcdResult;
import dto.types.Lokalisation;
import dto.types.RefIcdType;
import java.util.LinkedHashSet;
import java.util.Set;
import line.AbstractLine;
import line.LineI;
import line.impl.DiagnoseLine;

/**
 *
 * @author Dirk Niemeier
 * @param <T> type of diagnose
 */
public abstract class Diagnose<T extends Diagnose<T>> extends AbstractDto<T> {

//    private static final Logger LOG = Logger.getLogger(Diagnose.class.getName());
    private static final long serialVersionUID = 1L;

    private String mCode = "";
    //private final DiagnoseArt mArt;
    //protected String mFab = "";
    //private String mStation = "";
    private Lokalisation mLokalisation = null;
    private boolean mToGroup = true;
    private boolean mHdb = false;
    private boolean mHdx = false;
    private Integer mIcdType = null;
    private RefIcdType mRefIcdType = null;

    public final long mDepNr;
    public final Long mWardNr;
    public final String mIkz;
    public final String mFallNr;
    public final Department mDepartment;
    public final Ward mWard;

    public Diagnose(final Department pDepartment, final boolean pHdb) {
        this(pDepartment, null, pHdb);
    }

    public Diagnose(final Department pDepartment, final Ward pWard, final boolean pHdb) {
        if (pDepartment == null) {
            throw new IllegalArgumentException("Primary diagnose is null! Diagnose must be assigned to a department!");
        }
        mHdb = pHdb;
        mHdx = pHdb; //rly?!?
        mDepartment = pDepartment;
        mWard = pWard;
        mDepNr = pDepartment.getNr();
        mWardNr = pWard == null ? null : pWard.getNr();
        mIkz = pDepartment.getIkz();
        mFallNr = pDepartment.getFallNr();
        pDepartment.addDiagnose(this);
        if (pWard != null) {
            pWard.addDiagnose(this);
        }
    }

    public Diagnose(final String pIkz, final String pFallNr, final Long pDepartmentNr, final boolean pHdb) {
        this(pIkz, pFallNr, pDepartmentNr, null, pHdb);
    }

    public Diagnose(final String pIkz, final String pFallNr, final Long pDepartmentNr, final Long pWardNo, final boolean pHdb) {
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
        mHdb = pHdb;
        mHdx = pHdb; //rly?!?
        mDepNr = pDepartmentNr;
        mWardNr = (pWardNo != null && pWardNo.equals(0L)) ? null : pWardNo;
        mDepartment = null;
        mWard = null;
    }

    public Department getDepartment() {
        return mDepartment;
    }

    @Override
    public LineI toLine() {
        DiagnoseLine line = (DiagnoseLine) AbstractDto.toLine(this);
        line.setValueObj(DiagnoseLine.CODE, mCode);
        line.setValueObj(DiagnoseLine.DEP_NR, getDepartmentNr());
        //line.setValueObj(DiagnoseLine.ART, getArt().getValue());
        line.setValueObj(DiagnoseLine.WARD_NR, getWardNr());
        line.setValueObj(DiagnoseLine.LOKALISATION, (getLokalisation() == null) ? "" : getLokalisation().getValue());
        line.setValueObj(DiagnoseLine.TO_GROUP, isToGroup());
        line.setValueObj(DiagnoseLine.HDB, isHdb());
        line.setValueObj(DiagnoseLine.HDX, isHdx());
        line.setValueObj(DiagnoseLine.ICD_TYPE, getIcdType());
        line.setValueObj(DiagnoseLine.REF_ICD_TYPE, (getRefIcdType() == null ? null : getRefIcdType().getNumber()));
        //line.setValueObj(DiagnoseLine.REF_ICD_NR, (mRefIcd == null?"":mRefIcd.getNr()));
        //line.setValueObj(DiagnoseLine.REF_ICD_TYPE, getRefIcdType());
        return line;
    }

    @Override
    public Class<? extends AbstractLine> getLineClass() {
        return DiagnoseLine.class;
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
            if (code.length() >= 4 && code.charAt(3) != '.') {
                code = code.substring(0, 3) + '.' + code.substring(3);
            }
        }
        this.mCode = code;
    }

    /**
     * @param pIcdResult the icd result to set (combines setCode and
     * setRefIcdType)
     */
    public void setIcdResult(final IcdResult pIcdResult) {
        this.mCode = pIcdResult == null ? "" : pIcdResult.code;
        this.mRefIcdType = pIcdResult == null ? null : pIcdResult.refType;
    }

    @Override
    public Case getCase() {
        if (mDepartment != null) {
            return mDepartment.mCase;
        }
        return null;
    }

    /**
     * @return the mRefIcdType
     */
    public RefIcdType getRefIcdType() {
        return mRefIcdType;
    }

    /**
     * set the ref type
     *
     * @param pRefIcdType the ref type
     */
    public void setRefIcdType(final RefIcdType pRefIcdType) {
        mRefIcdType = pRefIcdType;
    }

    /**
     * set the ref type
     *
     * @param pRefIcdType the ref type
     */
    public void setRefIcdType(final Character pRefIcdType) {
        final RefIcdType refIcdType;
        if (pRefIcdType != null && !pRefIcdType.equals(' ')) {
            refIcdType = RefIcdType.findByValue(pRefIcdType.toString());
            if (refIcdType == null) {
                throw new IllegalArgumentException("Unknown type of referencing diagnosis: " + pRefIcdType);
            }
        } else {
            refIcdType = null;
        }
        mRefIcdType = refIcdType;
    }

    /**
     * set the ref type
     *
     * @param pRefIcdType the ref type
     */
    public void setRefIcdType(final Integer pRefIcdType) {
        final RefIcdType refIcdType;
        if (pRefIcdType == null || pRefIcdType.equals(0)) {
            refIcdType = null;
        } else {
            refIcdType = RefIcdType.findByNumber(pRefIcdType);
        }
        if (refIcdType == null) {
            throw new IllegalArgumentException("Unknown type number of referencing diagnosis: " + pRefIcdType);
        }
        mRefIcdType = refIcdType;
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
     * @return the mHdb
     */
    public boolean isHdb() {
        return mHdb;
    }

    /**
     * @return the mHdx
     */
    public boolean isHdx() {
        return mHdx;
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

    /**
     * @return the mIcdType
     */
    public Integer getIcdType() {
        return mIcdType;
    }

    public void setIcdType(final Integer pIcdType) {
        this.mIcdType = pIcdType;
    }

//    public void setIcdType(final String pIcdType) {
//        if (pIcdType == null) {
//            return;
//        }
//        try {
//            int icdType = Integer.parseInt(pIcdType);
//            setIcdType(icdType);
//        } catch (NumberFormatException ex) {
//            LOG.log(Level.WARNING, "Cannot set icd type. This is not a valid integer: " + pIcdType, ex);
//        }
//    }
    @Override
    public void set(T pOther) {
        mCode = pOther.getCode();
        mLokalisation = pOther.getLokalisation();
        mToGroup = pOther.isToGroup();
        mHdb = pOther.isHdb();
        mHdx = pOther.isHdx();
        mIcdType = pOther.getIcdType();
        mRefIcdType = pOther.getRefIcdType();
    }

}
