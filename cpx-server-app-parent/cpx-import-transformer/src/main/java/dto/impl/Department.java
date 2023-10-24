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
import dto.types.Erbringungsart;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import line.AbstractLine;
import line.impl.DepartmentLine;

/**
 *
 * @author Dirk Niemeier
 */
public class Department extends AbstractDto<Department> {

    private static final long serialVersionUID = 1L;

    public static Date getDummyAufnahmedatum() {
        return new GregorianCalendar(1899, Calendar.JANUARY, 1).getTime();
    }

    public static Date getDummyEntlassungsdatum() {
        return new GregorianCalendar(2100, Calendar.DECEMBER, 31).getTime();
    }
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

    private String mCode = "";
    private String mCodeIntern = "";
    private Date mVerlegungsdatum = null;
    private Erbringungsart mErbringungsart = Erbringungsart.NR;
    private String mAufnehmendeIk = "";
    private Date mEntlassungsdatum = null;
    private boolean mBedIntensiv = false;
    private Integer mLocationNumber = null;
    //private String mErbringungsart = "HA";

    private final Set<Diagnose<?>> diagnosisList = new LinkedHashSet<>();
    private final Set<Procedure> procedureList = new LinkedHashSet<>();
    private final Set<Ward> wardsList = new LinkedHashSet<>();

    public final String mIkz;
    public final String mFallNr;
    public final Case mCase;

    public Department(final Case pCase) {
        if (pCase == null) {
            throw new IllegalArgumentException("Case is null! Department must be assigned to a case!");
        }
        mCase = pCase;
        mIkz = pCase.getIkz();
        mFallNr = pCase.getFallNr();
        pCase.addDepartment(this);
    }

    public Department(final String pIkz, final String pFallNr) {
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

    @Override
    public DepartmentLine toLine() {
        DepartmentLine line = (DepartmentLine) AbstractDto.toLine(this);
        line.setValueObj(DepartmentLine.CODE, mCode);
        line.setValueObj(DepartmentLine.CODE_INTERN, mCodeIntern);
        line.setValueObj(DepartmentLine.VERLEGUNGSDATUM, getVerlegungsdatum());
        line.setValueObj(DepartmentLine.ERBRINGUNGSART, (getErbringungsart() == null) ? Erbringungsart.NR : getErbringungsart().getValue());
        line.setValueObj(DepartmentLine.ERBRINGUNGSART_INT, (getErbringungsart() == null) ? "8" : getErbringungsart().getNumber());
        line.setValueObj(DepartmentLine.AUFNEHMENDE_IK, getAufnehmendeIk());
        line.setValueObj(DepartmentLine.ENTLASSUNGSDATUM, getEntlassungsdatum());
        line.setValueObj(DepartmentLine.IS_HOSPITAL, isHospital());
        line.setValueObj(DepartmentLine.IS_PSEUDO, isPseudo());
        line.setValueObj(DepartmentLine.DAUER, getDauer());
        line.setValueObj(DepartmentLine.IS_BED_INTENSIV, isBedIntensiv());
        line.setValueObj(DepartmentLine.LOCATION_NUMBER, getLocationNumber());
        //line.setValueObj(DepartmentLine.ERBRINGUNGSART, getErbringungsart());
        return line;
    }

    @Override
    public Class<? extends AbstractLine> getLineClass() {
        return DepartmentLine.class;
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

    protected boolean addWard(final Ward pWard) {
        if (pWard == null) {
            throw new IllegalArgumentException("Ward is null!");
        }
        return wardsList.add(pWard);
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
        //maybe circular to add wardsList entries?!
        Iterator<Ward> it3 = wardsList.iterator();
        while (it3.hasNext()) {
            Ward ward = it3.next();
            if (ward == null) {
                continue;
            }
            dtoSet.addAll(ward.getDtos());
        }
        return dtoSet;
    }

    /**
     * @return the code intern
     */
    public String getCodeIntern() {
        return mCodeIntern;
    }

    /**
     * @param pCodeIntern the code to set
     */
    public void setCodeIntern(final String pCodeIntern) {
        this.mCodeIntern = toStr(pCodeIntern);
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
        return mCase;
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
     * @return the mBedIntensiv
     */
    public boolean isBedIntensiv() {
        return mBedIntensiv;
    }

    /**
     * @param pBedIntensiv the mBedIntensiv to set
     */
    public void setBedIntensiv(final boolean pBedIntensiv) {
        this.mBedIntensiv = pBedIntensiv;
    }

    /**
     * @return the mErbringungsart
     */
    public Erbringungsart getErbringungsart() {
        return mErbringungsart;
    }

    /**
     * @param pErbringungsart the mErbringungsart to set
     */
    public void setErbringungsart(final Erbringungsart pErbringungsart) {
        if (pErbringungsart != null) {
            this.mErbringungsart = pErbringungsart;
        } else {
            this.mErbringungsart = Erbringungsart.NR;
        }
    }

    /**
     * @return the mAufnehmendeIk
     */
    public String getAufnehmendeIk() {
        return mAufnehmendeIk;
    }

    /**
     * @param pAufnehmendeIk the mAufnehmendeIk to set
     */
    public void setAufnehmendeIk(final String pAufnehmendeIk) {
        this.mAufnehmendeIk = toStr(pAufnehmendeIk);
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
     * @return the mLocationNumber
     */
    public Integer getLocationNumber() {
        return mLocationNumber;
    }

    /**
     * @param pLocationNumber the mLocationNumber to set
     */
    public void setLocationNumber(final Integer pLocationNumber) {
        this.mLocationNumber = pLocationNumber;
    }

    /**
     * @return Dauer
     */
    public int getDauer() {
        if (mVerlegungsdatum == null) {
            return 0;
        }
        if (mEntlassungsdatum == null) {
            return 0;
        }
        long dauer = mEntlassungsdatum.getTime() - mVerlegungsdatum.getTime();
        return (int) (dauer / 1000);
    }

    /**
     * @return is hospital
     */
    public boolean isHospital() {
        return is0001() || is0002() || is0003();
    }

    /**
     * is code 0000?
     *
     * @return boolean
     */
    public boolean is0000() {
        return "0000".equals(mCode);
    }

    /**
     * is code 0001?
     *
     * @return boolean
     */
    public boolean is0001() {
        return "0001".equals(mCode);
    }

    /**
     * is code 0002?
     *
     * @return boolean
     */
    public boolean is0002() {
        return "0002".equals(mCode);
    }

    /**
     * is code 0003?
     *
     * @return boolean
     */
    public boolean is0003() {
        return "0003".equals(mCode);
    }

    /**
     * @return is hospital
     */
    public boolean isPseudo() {
        return is0000();
    }

    public Set<Procedure> getProcedures() {
        return new LinkedHashSet<>(procedureList);
    }

    public Set<Diagnose<?>> getDiagnosis() {
        return new LinkedHashSet<>(diagnosisList);
    }

    public Set<Ward> getWards() {
        return new LinkedHashSet<>(wardsList);
    }

    public void removeProcedures() {
        procedureList.clear();
    }

    public void removeDiagnosis() {
        diagnosisList.clear();
    }

    public void removeWards() {
        wardsList.clear();
    }

    @Override
    public void set(Department pOther) {
        mCode = pOther.mCode;
        mVerlegungsdatum = pOther.mVerlegungsdatum;
        mErbringungsart = pOther.mErbringungsart;
        mAufnehmendeIk = pOther.mAufnehmendeIk;
        mEntlassungsdatum = pOther.mEntlassungsdatum;
        mBedIntensiv = pOther.mBedIntensiv;
        mLocationNumber = pOther.mLocationNumber;
        Map<Long, Ward> newWards = new HashMap<>();
        for (Ward otherWard : pOther.getWards()) {
            Ward ward = new Ward(this);
            ward.set(otherWard);
            newWards.put(otherWard.getNr(), ward);
        }
        Map<Long, Diagnose<?>> newDiagnosis = new HashMap<>();
        for (Diagnose<?> otherDiagnose : pOther.getDiagnosis()) {
            Ward newWard = newWards.get(otherDiagnose.getWardNr());
            final Diagnose<?> newDiagnose;
            if (otherDiagnose.isHdb()) {
                Hauptdiagnose other = (Hauptdiagnose) otherDiagnose;
                Hauptdiagnose newHauptdiagnose = new Hauptdiagnose(this, newWard);
                newDiagnose = newHauptdiagnose;
                newHauptdiagnose.set(other);
            } else {
                Nebendiagnose other = (Nebendiagnose) otherDiagnose;
                Nebendiagnose newNebendiagnose = new Nebendiagnose(this, newWard);
                newDiagnose = newNebendiagnose;
                newNebendiagnose.set(other);
                Diagnose<?> newRefIcd = newDiagnosis.get(other.getRefIcdNr());
                newNebendiagnose.setRefIcd(newRefIcd);
            }
            newDiagnosis.put(otherDiagnose.getNr(), newDiagnose);
        }
        for (Procedure otherProcedure : pOther.getProcedures()) {
            Ward newWard = newWards.get(otherProcedure.getWardNr());
            Procedure newProcedure = new Procedure(this, newWard);
            newProcedure.set(otherProcedure);
        }
    }

}
