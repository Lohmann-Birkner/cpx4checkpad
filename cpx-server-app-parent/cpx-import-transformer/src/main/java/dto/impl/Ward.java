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
import line.impl.WardLine;

/**
 *
 * @author niemeier
 */
public class Ward extends AbstractDto<Ward> {

    private static final long serialVersionUID = 1L;

    private String mCode = "";
    private Date mVerlegungsdatum = null;
    private Date mEntlassungsdatum = null;
    //private String mErbringungsart = "HA";
    public final long mDepNr;

    private final Set<Diagnose<?>> diagnosisList = new LinkedHashSet<>();
    private final Set<Procedure> procedureList = new LinkedHashSet<>();

    public final String mIkz;
    public final String mFallNr;
    public final Department mDepartment;

    public Ward(final Department pDepartment) {
        if (pDepartment == null) {
            throw new IllegalArgumentException("Primary diagnose is null! Diagnose must be assigned to a department!");
        }
        mDepartment = pDepartment;
        mDepNr = pDepartment.getNr();
        mIkz = pDepartment.getIkz();
        mFallNr = pDepartment.getFallNr();
        pDepartment.addWard(this);
    }

    public Ward(final String pIkz, final String pFallNr, final Long pDepartmentNr) {
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
        mDepartment = null;
    }

    public Department getDepartment() {
        return mDepartment;
    }

    @Override
    public WardLine toLine() {
        WardLine line = (WardLine) AbstractDto.toLine(this);
        line.setValueObj(WardLine.CODE, mCode);
        line.setValueObj(WardLine.DEP_NR, getDepartmentNr());
        line.setValueObj(WardLine.VERLEGUNGSDATUM, getVerlegungsdatum());
        line.setValueObj(WardLine.ENTLASSUNGSDATUM, getEntlassungsdatum());
        //line.setValueObj(DepartmentLine.ERBRINGUNGSART, getErbringungsart());
        return line;
    }

    @Override
    public Class<? extends AbstractLine> getLineClass() {
        return WardLine.class;
    }

    protected boolean addDiagnose(final Diagnose<?> pDiagnose) {
        if (pDiagnose == null) {
            throw new IllegalArgumentException("Diagnose is null!");
        }
        return diagnosisList.add(pDiagnose);
    }

    protected boolean addProcedure(final Procedure pProcedure) {
        if (pProcedure == null) {
            throw new IllegalArgumentException("Procedure is null!");
        }
        return procedureList.add(pProcedure);
    }

    @Override
    public Set<DtoI> getDtos() {
        Set<DtoI> dtoSet = new LinkedHashSet<>();
        dtoSet.add(this);
        Iterator<Diagnose<?>> it = diagnosisList.iterator();
        while (it.hasNext()) {
            Diagnose<?> diagnose = it.next();
            if (diagnose == null) {
                continue;
            }
            dtoSet.addAll(diagnose.getDtos());
        }
        Iterator<Procedure> it2 = procedureList.iterator();
        while (it2.hasNext()) {
            Procedure procedure = it2.next();
            if (procedure == null) {
                continue;
            }
            dtoSet.addAll(procedure.getDtos());
        }
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
        this.mCode = toStr(pCode);
    }

    @Override
    public Case getCase() {
        if (mDepartment != null) {
            return mDepartment.mCase;
        }
        return null;
    }

    /**
     * @return the mVerlegungsdatum
     */
    public Date getVerlegungsdatum() {
        return mVerlegungsdatum == null ? null : new Date(mVerlegungsdatum.getTime());
    }

    /**
     * @param pVerlegungsdatum the mVerlegungsdatum to set
     */
    public void setVerlegungsdatum(final Date pVerlegungsdatum) {
        this.mVerlegungsdatum = pVerlegungsdatum == null ? null : new Date(pVerlegungsdatum.getTime());
    }

    /**
     * @return the mEntlassungsdatum
     */
    public Date getEntlassungsdatum() {
        return mEntlassungsdatum == null ? null : new Date(mEntlassungsdatum.getTime());
    }

    /**
     * @param pEntlassungsdatum the mEntlassungsdatum to set
     */
    public void setEntlassungsdatum(final Date pEntlassungsdatum) {
        this.mEntlassungsdatum = pEntlassungsdatum == null ? null : new Date(pEntlassungsdatum.getTime());
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

    public Set<Procedure> getProcedures() {
        return new LinkedHashSet<>(procedureList);
    }

    public Set<Diagnose<?>> getDiagnosis() {
        return new LinkedHashSet<>(diagnosisList);
    }

    public void removeProcedures() {
        procedureList.clear();
    }

    public void removeDiagnosis() {
        diagnosisList.clear();
    }

    @Override
    public void set(Ward pOther) {
        mCode = pOther.mCode;
        mVerlegungsdatum = pOther.mVerlegungsdatum;
        mEntlassungsdatum = pOther.mEntlassungsdatum;
//
//        for(Diagnose otherDiagnose: pOther.getDiagnosis()) {
//            
//        }
//        for(Procedure otherProcedure: pOther.getProcedures()) {
//            
//        }
    }

}
