/*
 * Copyright (c) 2021 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2021  gerschmann - initial API and implementation and/or initial documentation
 */
package dto.impl;

import dto.AbstractDto;
import dto.DtoI;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import line.AbstractLine;
import line.LineI;
import line.impl.CancelCaseLine;

/**
 *
 * @author gerschmann
 */
public class CancelCase extends AbstractDto<CancelCase>{
    private String mIkz = "";
    private String mFallNr = "";
    private String mCancelReason = "";


       @Override
    public boolean equals(final Object pObj) {
        if (pObj == null) {
            return false;
        }
        if (this.getClass() != pObj.getClass()) {
            return false;
        }
        //if (!(pObj instanceof Patient)) {
        //  return false;
        //}
        return ((DtoI) pObj).getIkz().equals(this.getIkz()) && ((DtoI) pObj).getFallNr().equals(this.getFallNr());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.mFallNr) + Objects.hashCode(this.mIkz);
        return hash;
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
    public void set(CancelCase pOther) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public LineI toLine() {
        CancelCaseLine line = (CancelCaseLine) AbstractDto.toLine(this);
        line.setValueObj(CancelCaseLine.CANCEL_REASON, getCancelReason());
        return line;
    }

    @Override
    public Class<? extends AbstractLine> getLineClass() {
        return CancelCaseLine.class;
    }

    @Override
    public Set<DtoI> getDtos() {
        Set<DtoI> dtoSet = new LinkedHashSet<>();
        dtoSet.add(this);
        return dtoSet;
    }


    @Override
    public Case getCase() {
        return null;
    }

    public String getCancelReason() {
        return this.mCancelReason;
    }


    public void setIkz(String mIkz) {
        this.mIkz = mIkz;
    }

    public void setFallNr(String mFallNr) {
        this.mFallNr = mFallNr;
    }

    public void setCancelReason(String mCancelReason) {
        this.mCancelReason = mCancelReason;
    }


}
