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

import line.LineI;
import line.impl.DiagnoseLine;

/**
 *
 * @author Dirk Niemeier
 */
public class Nebendiagnose extends Diagnose<Nebendiagnose> {

    private static final long serialVersionUID = 1L;

    private Diagnose<?> mRefIcd = null;

    public Nebendiagnose(final Department pDepartment) {
        this(pDepartment, null);
    }

    public Nebendiagnose(final Department pDepartment, final Ward pWard) {
        super(pDepartment, pWard, false);
    }

    public Nebendiagnose(final String pIkz, final String pFallNr, final Long pDepartmentNr) {
        this(pIkz, pFallNr, pDepartmentNr, null);
    }

    public Nebendiagnose(final String pIkz, final String pFallNr, final Long pDepartmentNr, final Long pWardNr) {
        super(pIkz, pFallNr, pDepartmentNr, false);
    }

    @Override
    public LineI toLine() {
        DiagnoseLine line = (DiagnoseLine) super.toLine();
        line.setValueObj(DiagnoseLine.REF_ICD_NR, (mRefIcd == null ? "" : mRefIcd.getNr()));
        return line;
    }

    /**
     * @param pRefIcdType the mRefIcdType to set
     */
    /*
  public void setRefIcdType(final Integer pRefIcdType) {
    this.mRefIcdType = pRefIcdType;
  }
     */
    /**
     * @return the code
     */
    public Diagnose<?> getRefIcd() {
        return mRefIcd;
    }

    public Long getRefIcdNr() {
        return mRefIcd == null ? null : mRefIcd.getNr();
    }

//    public void removeRefIcd() {
//        setRefIcd(null, (RefIcdType) null);
//    }
//    public void setRefIcd(final Diagnose pRefIcd, final Character pRefIcdType) {
//        final RefIcdType refIcdType;
//        if (pRefIcdType != null && !pRefIcdType.equals(' ')) {
//            refIcdType = RefIcdType.findByValue(pRefIcdType.toString());
//            if (refIcdType == null) {
//                throw new IllegalArgumentException("Unknown type of referencing diagnosis: " + pRefIcdType);
//            }
//        } else {
//            refIcdType = null;
//        }
//        setRefIcd(pRefIcd, refIcdType);
//    }
//    public void setRefIcd(final Diagnose pRefIcd, final Integer pRefIcdType) {
//        final RefIcdType refIcdType;
//        if (pRefIcdType == null || pRefIcdType.equals(0)) {
//            refIcdType = null;
//        } else {
//            refIcdType = RefIcdType.findByNumber(pRefIcdType);
//        }
//        if (refIcdType == null) {
//            throw new IllegalArgumentException("Unknown type number of referencing diagnosis: " + pRefIcdType);
//        }
//        setRefIcd(pRefIcd, refIcdType);
//    }
    /**
     * @param pRefIcd the code to set
     */
    public void setRefIcd(final Diagnose<?> pRefIcd /*, final RefIcdType pRefIcdType */) {
        if (pRefIcd != null) {
            if (pRefIcd.getCase() != this.getCase()) {
                throw new IllegalArgumentException("Diagnose and referencing diagnose are not assigned to the same case!");
            }
        }
//        if (pRefIcd != null && pRefIcdType == null) {
//            throw new IllegalArgumentException("Type of referencing diagnosis cannot be null or empty!");
//        }
        this.mRefIcd = pRefIcd;
//        this.mRefIcdType = pRefIcdType;
    }

    @Override
    public void set(Nebendiagnose pOther) {
        super.set(pOther);
    }

}
