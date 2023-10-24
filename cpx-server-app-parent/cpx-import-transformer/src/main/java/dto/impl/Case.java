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
import dto.types.Fallstatus;
import dto.types.Geschlecht;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import line.AbstractLine;
import line.impl.CaseLine;

/**
 *
 * @author Dirk Niemeier
 */
public class Case extends AbstractDto<Case> {

    private static final long serialVersionUID = 1L;

    public static Date getDummyAufnahmedatum() {
        return new GregorianCalendar(1899, Calendar.JANUARY, 1).getTime();
    }

    public static Date getDummyEntlassungsdatum() {
        return new GregorianCalendar(2100, Calendar.DECEMBER, 31).getTime();
    }

    private String mIkz = "";
    private String mFallNr = "";
    private Date mAufnahmedatum = null;
    private String mEntgeltbereich = "";
    private Date mEntlassungsdatum = null;
    private Integer mAlterInJahren = null;
    private Integer mAlterInTagen = null;
    private Integer mGewicht = null;
    private String mEntlassungsgrund12 = "";
    private String mEntlassungsgrund3 = "";
    private String mAufnahmegrund1 = "";
    private String mAufnahmegrund2 = "";
    private String mAufnahmeanlass = "";
    private Integer mBeatmungsstunden = null;
    private String mFallart = "";
    private Boolean mStorniert = null;
    private int mVwdIntensiv = 0;
    //private String mErbringungsart = "HA";
    private int mGesetzlPsychstatus = 1;
    private Integer mUrlaubstage = null;
    private Integer mVwd = null;
    private Integer mVwdSimuliert = null;
    private String mVersichertennr = "";
    private String mKasse = "";
    private Fallstatus mFallstatus = null;
    private Geschlecht mGeschlecht = null;

    private String mString1 = "";
    private String mString2 = "";
    private String mString3 = "";
    private String mString4 = "";
    private String mString5 = "";
    private String mString6 = "";
    private String mString7 = "";
    private String mString8 = "";
    private String mString9 = "";
    private String mString10 = "";
    private Integer mNumeric1 = null;
    private Integer mNumeric2 = null;
    private Integer mNumeric3 = null;
    private Integer mNumeric4 = null;
    private Integer mNumeric5 = null;
    private Integer mNumeric6 = null;
    private Integer mNumeric7 = null;
    private Integer mNumeric8 = null;
    private Integer mNumeric9 = null;
    private Integer mNumeric10 = null;
    private Integer mMDTob = null;
    private Date mBillingDate = null;

    private transient Integer mUrlaubVon = null;
    private transient Integer mUrlaubBis = null;

    private final Set<Department> departmentList = new LinkedHashSet<>();
    private final Set<Bill> billList = new LinkedHashSet<>();
    private final Set<Drug> drugList = new LinkedHashSet<>();
    private final Set<Fee> feeList = new LinkedHashSet<>();
//    private final Set<Lab> labList = new LinkedHashSet<>();

    public final String mPatNr;
    public final Patient mPatient;
    //private BigDecimal betrag = BigDecimal.ZERO;
    //private BigDecimal betrag = BigDecimal.ZERO;

    /*
  @Override
  public String getCaseKey() {
  return ikz + ":" + fallNr;
  }
     */
    public Case(final Patient pPatient) {
        if (pPatient == null) {
            throw new IllegalArgumentException("Patient is null! Case must be assigned to a patient!");
        }
        mPatient = pPatient;
        mPatNr = pPatient.getPatNr();
        pPatient.addCase(this);
    }

    public Case(final String pPatNr) {
        String patNr = pPatNr == null ? "" : pPatNr.trim();
        if (patNr.isEmpty()) {
            throw new IllegalArgumentException("Patient number is empty!");
        }
        mPatNr = patNr;
        mPatient = null;
    }

    /**
     * @return the ikz REMOVING THIS METHODS RESULTS IN AN STACK OVERFLOW!
     */
    @Override
    public String getIkz() {
        return mIkz;
    }

    /**
     * @param pIkz the ikz to set
     */
    public void setIkz(final String pIkz) {
        this.mIkz = toStr(pIkz);
    }

    /**
     * @return the fallNr REMOVING THIS METHODS RESULTS IN AN STACK OVERFLOW!
     */
    @Override
    public String getFallNr() {
        return mFallNr;
    }

    /**
     * @param pFallNr the ikz to set
     */
    public void setFallNr(final String pFallNr) {
        this.mFallNr = toStr(pFallNr);
    }

    @Override
    public CaseLine toLine() {
        CaseLine line = (CaseLine) AbstractDto.toLine(this);
        line.setValueObj(CaseLine.AUFNAHMEDATUM, getAufnahmedatum());
        line.setValueObj(CaseLine.ENTGELTBEREICH, getEntgeltbereich());
        line.setValueObj(CaseLine.ENTLASSUNGSDATUM, getEntlassungsdatum());
        line.setValueObj(CaseLine.ALTER_IN_JAHREN, getAlterInJahren());
        line.setValueObj(CaseLine.ALTER_IN_TAGEN, getAlterInTagen());
        line.setValueObj(CaseLine.GEWICHT, getGewicht());
        line.setValueObj(CaseLine.ENTLASSUNGSGRUND12, getEntlassungsgrund12());
        line.setValueObj(CaseLine.ENTLASSUNGSGRUND3, getEntlassungsgrund3());
        line.setValueObj(CaseLine.AUFNAHMEGRUND1, getAufnahmegrund1());
        line.setValueObj(CaseLine.AUFNAHMEGRUND2, getAufnahmegrund2());
        line.setValueObj(CaseLine.AUFNAHMEANLASS, getAufnahmeanlass());
        line.setValueObj(CaseLine.BEATMUNGSSTUNDEN, getBeatmungsstunden());
        line.setValueObj(CaseLine.FALLART, getFallart());
        line.setValueObj(CaseLine.STORNIERT, getStorniert());
        line.setValueObj(CaseLine.VWD_INTENSIV, getVwdIntensiv());
        //line.setValueObj(CaseLine.ERBRINGUNGSART, getErbringungsart());
        line.setValueObj(CaseLine.GESETZL_PSYCHSTATUS, getGesetzlPsychstatus());
        line.setValueObj(CaseLine.URLAUBSTAGE, getUrlaubstage());
        line.setValueObj(CaseLine.VWD, getVwd());
        line.setValueObj(CaseLine.VWD_SIMULIERT, getVwdSimuliert());
        line.setValueObj(CaseLine.VERSICHERTENNR, getVersichertennr());
        line.setValueObj(CaseLine.KASSE, getKasse());
        line.setValueObj(CaseLine.FALLSTATUS, (getFallstatus() == null) ? "" : getFallstatus().getValue());
        line.setValueObj(CaseLine.GESCHLECHT, (getGeschlecht() == null) ? "" : getGeschlecht().getValue());
        line.setValueObj(CaseLine.STRING1, getString1());
        line.setValueObj(CaseLine.STRING2, getString2());
        line.setValueObj(CaseLine.STRING3, getString3());
        line.setValueObj(CaseLine.STRING4, getString4());
        line.setValueObj(CaseLine.STRING5, getString5());
        line.setValueObj(CaseLine.STRING6, getString6());
        line.setValueObj(CaseLine.STRING7, getString7());
        line.setValueObj(CaseLine.STRING8, getString8());
        line.setValueObj(CaseLine.STRING9, getString9());
        line.setValueObj(CaseLine.STRING10, getString10());
        line.setValueObj(CaseLine.NUMERIC1, getNumeric1());
        line.setValueObj(CaseLine.NUMERIC2, getNumeric2());
        line.setValueObj(CaseLine.NUMERIC3, getNumeric3());
        line.setValueObj(CaseLine.NUMERIC4, getNumeric4());
        line.setValueObj(CaseLine.NUMERIC5, getNumeric5());
        line.setValueObj(CaseLine.NUMERIC6, getNumeric6());
        line.setValueObj(CaseLine.NUMERIC7, getNumeric7());
        line.setValueObj(CaseLine.NUMERIC8, getNumeric8());
        line.setValueObj(CaseLine.NUMERIC9, getNumeric9());
        line.setValueObj(CaseLine.NUMERIC10, getNumeric10());
        line.setValueObj(CaseLine.MD_TOB, getMDTob());
        line.setValueObj(CaseLine.BILLING_DATE, getBillingDate());
        return line;
    }

    /*
  protected boolean addDiagnose(final Diagnose pDiagnose) {
    if (pDiagnose == null) {
      throw new CpxIllegalArgumentException("Diagnosis is null!");
    }
    return diagnoseList.add(pDiagnose);
  }
     */
    /**
     * @return the betrag
     */
    /*
  public BigDecimal getBetrag() {
    return betrag;
  }
     */
//  /**
//   * @param pBetrag the betrag to set
//   * @return 
//   */
    /*
  public void setBetrag(final BigDecimal pBetrag) {
    //this.betrag = (pBetrag == null)?BigDecimal.ZERO:pBetrag;
    this.betrag = pBetrag;
  }
     */
    @Override
    public Class<? extends AbstractLine> getLineClass() {
        return CaseLine.class;
    }

    public void removeProcedures() {
        for (Department dep : getDepartments()) {
            dep.removeProcedures();
        }
    }

    public void removeDiagnosis() {
        for (Department dep : getDepartments()) {
            dep.removeDiagnosis();
        }
    }

    public void removeWards() {
        for (Department dep : getDepartments()) {
            dep.removeWards();
        }
    }

    public Set<Procedure> getProcedures() {
        Set<Procedure> procedures = new LinkedHashSet<>();
        for (Department dep : getDepartments()) {
            procedures.addAll(dep.getProcedures());
        }
        return procedures;
    }

    public Set<Diagnose<?>> getDiagnosis() {
        Set<Diagnose<?>> diagnosis = new LinkedHashSet<>();
        for (Department dep : getDepartments()) {
            diagnosis.addAll(dep.getDiagnosis());
        }
        return diagnosis;
    }

    public Set<Hauptdiagnose> getMainDiagnosis() {
        Set<Hauptdiagnose> diagnosis = new LinkedHashSet<>();
        for (Department dep : getDepartments()) {
            for (Diagnose<?> diag : dep.getDiagnosis()) {
                if (diag.isHdb()) {
                    diagnosis.add((Hauptdiagnose) diag);
                }
            }
        }
        return diagnosis;
    }

    public Set<Nebendiagnose> getSekDiagnosis() {
        Set<Nebendiagnose> diagnosis = new LinkedHashSet<>();
        for (Department dep : getDepartments()) {
            for (Diagnose<?> diag : dep.getDiagnosis()) {
                if (!diag.isHdb()) {
                    diagnosis.add((Nebendiagnose) diag);
                }
            }
        }
        return diagnosis;
    }

    public Set<Ward> getWards() {
        Set<Ward> wards = new LinkedHashSet<>();
        for (Department dep : getDepartments()) {
            wards.addAll(dep.getWards());
        }
        return wards;
    }

    public Set<Department> getDepartments() {
        return new LinkedHashSet<>(departmentList);
    }

    public Set<Fee> getFees() {
        return new LinkedHashSet<>(feeList);
    }

    public Set<Bill> getBills() {
        return new LinkedHashSet<>(billList);
    }

    public Set<Drug> getDrugs() {
        return new LinkedHashSet<>(drugList);
    }

    public void removeDepartments() {
        departmentList.clear();
    }

    public void removeFees() {
        feeList.clear();
    }

    public void removeBills() {
        billList.clear();
    }

    public void removeDrugs() {
        drugList.clear();
    }

    protected boolean addDepartment(final Department pDepartment) {
        if (pDepartment == null) {
            throw new IllegalArgumentException("Department is null!");
        }
        return departmentList.add(pDepartment);
    }

    protected boolean addBill(final Bill pBill) {
        if (pBill == null) {
            throw new IllegalArgumentException("Bill is null!");
        }
        return billList.add(pBill);
    }

    protected boolean addDrug(final Drug pDrug) {
        if (pDrug == null) {
            throw new IllegalArgumentException("Drug is null!");
        }
        return drugList.add(pDrug);
    }

    protected boolean addFee(final Fee pFee) {
        if (pFee == null) {
            throw new IllegalArgumentException("Fee is null!");
        }
        return feeList.add(pFee);
    }

//    protected boolean addLab(final Lab pLab) {
//        if (pLab == null) {
//            throw new IllegalArgumentException("Fee is null!");
//        }
//        return labList.add(pLab);
//    }
    @Override
    public Set<DtoI> getDtos() {
        Set<DtoI> dtoSet = new LinkedHashSet<>();
        dtoSet.add(this);
        Iterator<Department> it = departmentList.iterator();
        while (it.hasNext()) {
            Department department = it.next();
            if (department == null) {
                continue;
            }
            dtoSet.addAll(department.getDtos());
        }
        Iterator<Bill> it2 = billList.iterator();
        while (it2.hasNext()) {
            Bill bill = it2.next();
            if (bill == null) {
                continue;
            }
            dtoSet.addAll(bill.getDtos());
        }
        Iterator<Fee> it3 = feeList.iterator();
        while (it3.hasNext()) {
            Fee fee = it3.next();
            if (fee == null) {
                continue;
            }
            dtoSet.addAll(fee.getDtos());
        }
        Iterator<Drug> it4 = drugList.iterator();
        while (it4.hasNext()) {
            Drug drug = it4.next();
            if (drug == null) {
                continue;
            }
            dtoSet.addAll(drug.getDtos());
        }
//        Iterator<Lab> it4 = labList.iterator();
//        while (it4.hasNext()) {
//            Lab lab = it4.next();
//            if (lab == null) {
//                continue;
//            }
//            dtoSet.addAll(lab.getDtos());
//        }
        return dtoSet;
    }

    @Override
    public Case getCase() {
        return this;
    }

    /**
     * @return the mAufnahmedatum
     */
    public Date getAufnahmedatum() {
        return mAufnahmedatum == null ? null : new Date(mAufnahmedatum.getTime());
    }

    /**
     * @param pAufnahmedatum the mAufnahmedatum to set
     */
    public void setAufnahmedatum(final Date pAufnahmedatum) {
        this.mAufnahmedatum = pAufnahmedatum == null ? null : new Date(pAufnahmedatum.getTime());
    }

    /**
     * @return the mEntgeltbereich
     */
    public String getEntgeltbereich() {
        return mEntgeltbereich;
    }

    /**
     * @param pEntgeltbereich the mEntgeltbereich to set
     */
    public void setEntgeltbereich(final String pEntgeltbereich) {
        this.mEntgeltbereich = toStr(pEntgeltbereich);
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
     * @return the mAlterInJahren
     */
    public Integer getAlterInJahren() {
        return mAlterInJahren;
    }

    /**
     * @param pAlterInJahren the mAlterInJahren to set
     */
    public void setAlterInJahren(final Integer pAlterInJahren) {
        this.mAlterInJahren = pAlterInJahren;
    }

    /**
     * @return the mAlterInTagen
     */
    public Integer getAlterInTagen() {
        return mAlterInTagen;
    }

    /**
     * @param pAlterInTagen the mAlterInTagen to set
     */
    public void setAlterInTagen(final Integer pAlterInTagen) {
        this.mAlterInTagen = pAlterInTagen;
    }

    /**
     * @return the mGewicht
     */
    public Integer getGewicht() {
        return mGewicht;
    }

    /**
     * @param pGewicht the mGewicht to set
     */
    public void setGewicht(final Integer pGewicht) {
        this.mGewicht = pGewicht;
    }

    /**
     * @return the mEntlassungsgrund12
     */
    public String getEntlassungsgrund12() {
        return mEntlassungsgrund12;
    }

    /**
     * @param pEntlassungsgrund12 the mEntlassungsgrund12 to set
     */
    public void setEntlassungsgrund12(final String pEntlassungsgrund12) {
        String entlassungsgrund12 = toStr(pEntlassungsgrund12);
        if (entlassungsgrund12.length() == 1) {
            entlassungsgrund12 = "0" + entlassungsgrund12;
        }
        this.mEntlassungsgrund12 = entlassungsgrund12;
    }

    /**
     * @return the mEntlassungsgrund3
     */
    public String getEntlassungsgrund3() {
        return mEntlassungsgrund3;
    }

    /**
     * @param pEntlassungsgrund3 the mEntlassungsgrund3 to set
     */
    public void setEntlassungsgrund3(final String pEntlassungsgrund3) {
        this.mEntlassungsgrund3 = toStr(pEntlassungsgrund3);
    }

    /**
     * @return the mAufnahmegrund1
     */
    public String getAufnahmegrund1() {
        return mAufnahmegrund1;
    }

    /**
     * @param pAufnahmegrund1 the mAufnahmegrund1 to set
     */
    public void setAufnahmegrund1(final String pAufnahmegrund1) {
        String aufnahmegrund1 = toStr(pAufnahmegrund1);
        if (aufnahmegrund1.length() == 1) {
            aufnahmegrund1 = "0" + aufnahmegrund1;
        }
        this.mAufnahmegrund1 = aufnahmegrund1;
    }

    /**
     * @return the mAufnahmegrund1
     */
    public String getAufnahmeanlass() {
        return mAufnahmeanlass;
    }

    /**
     * @param pAufnahmeanlass the mAufnahmegrund1 to set
     */
    public void setAufnahmeanlass(final String pAufnahmeanlass) {
        this.mAufnahmeanlass = toStr(pAufnahmeanlass);
    }

    /**
     * @param pAufnahmeanlass the mAufnahmegrund1 to set
     */
    public void setAufnahmeanlass(final Integer pAufnahmeanlass) {
        setAufnahmeanlass(pAufnahmeanlass == null ? "" : pAufnahmeanlass.toString());
    }

    /**
     * @return the mAufnahmegrund2
     */
    public String getAufnahmegrund2() {
        return mAufnahmegrund2;
    }

    /**
     * @param pAufnahmegrund2 the mAufnahmegrund2 to set
     */
    public void setAufnahmegrund2(final String pAufnahmegrund2) {
        String aufnahmegrund2 = toStr(pAufnahmegrund2);

        if (aufnahmegrund2.length() == 1) {
            aufnahmegrund2 = "0" + aufnahmegrund2;
        }

        if ("00".equalsIgnoreCase(aufnahmegrund2)) {
            //What is 00?! Discard this trashy value!
            aufnahmegrund2 = "01"; //Regular Case
        }
        this.mAufnahmegrund2 = aufnahmegrund2;
    }

    /**
     * @return the mBeatmungsstunden
     */
    public Integer getBeatmungsstunden() {
        return mBeatmungsstunden;
    }

    /**
     * @param pBeatmungsstunden the mBeatmungsstunden to set
     */
    public void setBeatmungsstunden(final Integer pBeatmungsstunden) {
        this.mBeatmungsstunden = pBeatmungsstunden;
    }

    /**
     * @return the mFallart
     */
    public String getFallart() {
        return mFallart;
    }

    /**
     * @param pFallart the mFallart to set
     */
    public void setFallart(final String pFallart) {
        String fallart = toStr(pFallart);
        if ("PSY".equalsIgnoreCase(fallart)) {
            fallart = "PEPP";
        }
        this.mFallart = fallart;
    }

    @Override
    public String getPatNr() {
        return mPatNr;
    }

    /**
     * @return the mStorniert
     */
    public Boolean getStorniert() {
        return mStorniert;
    }

    /**
     * @param pStorniert the mStorniert to set
     */
    public void setStorniert(final boolean pStorniert) {
        this.mStorniert = pStorniert;
    }

    /**
     * @return the mVwdIntensiv
     */
    public int getVwdIntensiv() {
        return mVwdIntensiv;
    }

    /**
     * @param pVwdIntensiv the mVwdIntensiv to set
     */
    public void setVwdIntensiv(final Integer pVwdIntensiv) {
        this.mVwdIntensiv = pVwdIntensiv == null ? 0 : pVwdIntensiv;
    }

    /**
     * @param pVwdIntensiv the mVwdIntensiv to set
     */
    public void setVwdIntensiv(final BigDecimal pVwdIntensiv) {
        setVwdIntensiv(pVwdIntensiv == null ? null : pVwdIntensiv.intValue());
    }

    /**
     * @return the mErbringungsart
     */
//  public String getErbringungsart() {
//    return mErbringungsart;
//  }
    /**
     * @param pErbringungsart the mErbringungsart to set
     */
//  public void setErbringungsart(final String pErbringungsart) {
//    String erbringungsart = toStr(pErbringungsart);
//    if (erbringungsart.isEmpty()) {
//        erbringungsart = "HA";
//    }
//    this.mErbringungsart = erbringungsart;
//  }
    /**
     * @return the mGesetzlPsychstatus
     */
    public int getGesetzlPsychstatus() {
        return mGesetzlPsychstatus;
    }

    /**
     * @param pGesetzlPsychstatus the mGesetzlPsychstatus to set 1 = freiwillig
     * 2 = unfreiwillig
     */
    public void setGesetzlPsychstatus(final Integer pGesetzlPsychstatus) {
        this.mGesetzlPsychstatus = pGesetzlPsychstatus == null ? 1 : pGesetzlPsychstatus;
    }

    /**
     * @return the mUrlaubstage
     */
    public Integer getUrlaubstage() {
        return mUrlaubstage;
    }

    /**
     * @param pUrlaubstage the pUrlaubstage to set
     */
    public void setUrlaubstage(final Integer pUrlaubstage) {
        this.mUrlaubstage = pUrlaubstage;
    }

    /**
     * adds holidays
     *
     * @param pUrlaubstage the pUrlaubstage to set
     * @return sum of holidays
     */
    public Integer addUrlaubstage(final Integer pUrlaubstage) {
        if (pUrlaubstage == null) {
            return mUrlaubstage;
        }
        mUrlaubstage = Objects.requireNonNullElse(mUrlaubstage, 0) + pUrlaubstage;
        return mUrlaubstage;
    }

    /**
     * @return the mMDTob
     */
    public Integer getMDTob() {
        return mMDTob;
    }

    /**
     * @param pMDTob the pMDTob to set
     */
    public void setMDTob(final Integer pMDTob) {
        this.mMDTob = pMDTob;
    }

    /**
     * adds MDTob
     *
     * @param pMDTob the pMDTob to set
     * @return sum of MDTob
     */
    public Integer addMDTob(final Integer pMDTob) {
        if (pMDTob == null) {
            return mMDTob;
        }
        mMDTob = Objects.requireNonNullElse(mMDTob, 0) + pMDTob;
        return mMDTob;
    }

    /**
     * @return the mBillingDate
     */
    public java.util.Date getBillingDate() {
        return mBillingDate;
    }

    /**
     * @param pBillingDate the pBillingDate to set
     */
    public void setBillingDate(final java.util.Date pBillingDate) {
        this.mBillingDate = pBillingDate;
    }

    /**
     * @return the mVwd
     */
    public Integer getVwd() {
        return mVwd;
    }

    /**
     * @param pVwd the mVwd to set
     */
    public void setVwd(final Integer pVwd) {
        this.mVwd = pVwd;
    }

    /**
     * @return the mVwdSimuliert
     */
    public Integer getVwdSimuliert() {
        return mVwdSimuliert;
    }

    /**
     * @param pVwdSimuliert the mVwdSimuliert to set
     */
    public void setVwdSimuliert(final Integer pVwdSimuliert) {
        this.mVwdSimuliert = pVwdSimuliert;
    }

    /**
     * @return the mVersicherung
     */
    public String getVersichertennr() {
        return mVersichertennr;
    }

    /**
     * @param pVersichertennr the mVersichertennr to set
     */
    public void setVersichertennr(final String pVersichertennr) {
        this.mVersichertennr = toStr(pVersichertennr);
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

    /**
     * @return the mStatus
     */
    public Fallstatus getFallstatus() {
        return mFallstatus;
    }

    /**
     * @param pFallstatus the pStatus to set
     */
    public void setFallstatus(final Fallstatus pFallstatus) {
        this.mFallstatus = pFallstatus;
    }

    /**
     * @param pFallstatus the pStatus to set
     */
    public void setFallstatus(final String pFallstatus) {
        final String status = pFallstatus == null ? "" : pFallstatus.trim();
        if (status.isEmpty()) {
            this.mFallstatus = null;
        } else {
            this.mFallstatus = Fallstatus.findByValue(status);
        }
    }

    /**
     * @param pFallstatus the pStatus to set
     */
    public void setFallstatus(final Integer pFallstatus) {
        if (pFallstatus == null) {
            this.mFallstatus = null;
        } else {
            this.mFallstatus = Fallstatus.findByNumber(pFallstatus);
        }
    }

    @Override
    public void set(Case pOther) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the mString1
     */
    public String getString1() {
        return mString1;
    }

    /**
     * @return the mString2
     */
    public String getString2() {
        return mString2;
    }

    /**
     * @return the mString3
     */
    public String getString3() {
        return mString3;
    }

    /**
     * @return the mString4
     */
    public String getString4() {
        return mString4;
    }

    /**
     * @return the mString5
     */
    public String getString5() {
        return mString5;
    }

    /**
     * @return the mString6
     */
    public String getString6() {
        return mString6;
    }

    /**
     * @return the mString7
     */
    public String getString7() {
        return mString7;
    }

    /**
     * @return the mString8
     */
    public String getString8() {
        return mString8;
    }

    /**
     * @return the mString9
     */
    public String getString9() {
        return mString9;
    }

    /**
     * @return the mString10
     */
    public String getString10() {
        return mString10;
    }

    /**
     * @return the mNumeric1
     */
    public Integer getNumeric1() {
        return mNumeric1;
    }

    /**
     * @return the mNumeric2
     */
    public Integer getNumeric2() {
        return mNumeric2;
    }

    /**
     * @return the mNumeric3
     */
    public Integer getNumeric3() {
        return mNumeric3;
    }

    /**
     * @return the mNumeric4
     */
    public Integer getNumeric4() {
        return mNumeric4;
    }

    /**
     * @return the mNumeric5
     */
    public Integer getNumeric5() {
        return mNumeric5;
    }

    /**
     * @return the mNumeric6
     */
    public Integer getNumeric6() {
        return mNumeric6;
    }

    /**
     * @return the mNumeric7
     */
    public Integer getNumeric7() {
        return mNumeric7;
    }

    /**
     * @return the mNumeric8
     */
    public Integer getNumeric8() {
        return mNumeric8;
    }

    /**
     * @return the mNumeric9
     */
    public Integer getNumeric9() {
        return mNumeric9;
    }

    /**
     * @return the mNumeric10
     */
    public Integer getNumeric10() {
        return mNumeric10;
    }

    /**
     * @param pString1 the mString1 to set
     */
    public void setString1(String pString1) {
        this.mString1 = toStr(pString1);
    }

    /**
     * @param pString2 the mString2 to set
     */
    public void setString2(String pString2) {
        this.mString2 = toStr(pString2);
    }

    /**
     * @param pString3 the mString3 to set
     */
    public void setString3(String pString3) {
        this.mString3 = toStr(pString3);
    }

    /**
     * @param pString4 the mString4 to set
     */
    public void setString4(String pString4) {
        this.mString4 = toStr(pString4);
    }

    /**
     * @param pString5 the mString5 to set
     */
    public void setString5(String pString5) {
        this.mString5 = toStr(pString5);
    }

    /**
     * @param pString6 the mString6 to set
     */
    public void setString6(String pString6) {
        this.mString6 = toStr(pString6);
    }

    /**
     * @param pString7 the mString7 to set
     */
    public void setString7(String pString7) {
        this.mString7 = toStr(pString7);
    }

    /**
     * @param pString8 the mString8 to set
     */
    public void setString8(String pString8) {
        this.mString8 = toStr(pString8);
    }

    /**
     * @param pString9 the mString9 to set
     */
    public void setString9(String pString9) {
        this.mString9 = toStr(pString9);
    }

    /**
     * @param pString10 the mString10 to set
     */
    public void setString10(String pString10) {
        this.mString10 = toStr(pString10);
    }

    /**
     * @param pNumeric1 the mNumeric1 to set
     */
    public void setNumeric1(Integer pNumeric1) {
        this.mNumeric1 = pNumeric1;
    }

    /**
     * @param pNumeric2 the mNumeric2 to set
     */
    public void setNumeric2(Integer pNumeric2) {
        this.mNumeric2 = pNumeric2;
    }

    /**
     * @param pNumeric3 the mNumeric3 to set
     */
    public void setNumeric3(Integer pNumeric3) {
        this.mNumeric3 = pNumeric3;
    }

    /**
     * @param pNumeric4 the mNumeric4 to set
     */
    public void setNumeric4(Integer pNumeric4) {
        this.mNumeric4 = pNumeric4;
    }

    /**
     * @param pNumeric5 the mNumeric5 to set
     */
    public void setNumeric5(Integer pNumeric5) {
        this.mNumeric5 = pNumeric5;
    }

    /**
     * @param pNumeric6 the mNumeric6 to set
     */
    public void setNumeric6(Integer pNumeric6) {
        this.mNumeric6 = pNumeric6;
    }

    /**
     * @param pNumeric7 the mNumeric7 to set
     */
    public void setNumeric7(Integer pNumeric7) {
        this.mNumeric7 = pNumeric7;
    }

    /**
     * @param pNumeric8 the mNumeric8 to set
     */
    public void setNumeric8(Integer pNumeric8) {
        this.mNumeric8 = pNumeric8;
    }

    /**
     * @param pNumeric9 the mNumeric9 to set
     */
    public void setNumeric9(Integer pNumeric9) {
        this.mNumeric9 = pNumeric9;
    }

    /**
     * @param pNumeric10 the mNumeric10 to set
     */
    public void setNumeric10(Integer pNumeric10) {
        this.mNumeric10 = pNumeric10;
    }

    /**
     * @return the mUrlaubVon
     */
    public Integer getUrlaubVon() {
        return mUrlaubVon;
    }

    /**
     * @return the mUrlaubBis
     */
    public Integer getUrlaubBis() {
        return mUrlaubBis;
    }

    /**
     * @param pUrlaubVon the mUrlaubVon to set
     */
    public void setUrlaubVon(Integer pUrlaubVon) {
        this.mUrlaubVon = pUrlaubVon;
    }

    /**
     * @param pUrlaubBis the mUrlaubBis to set
     */
    public void setUrlaubBis(Integer pUrlaubBis) {
        this.mUrlaubBis = pUrlaubBis;
    }

    /**
     * @return the mGeschlecht
     */
    public Geschlecht getGeschlecht() {
        return mGeschlecht;
    }

    /**
     * @param pGeschlecht the mGeschlecht to set
     */
    public void setGeschlecht(final Geschlecht pGeschlecht) {
        this.mGeschlecht = pGeschlecht;
    }

    /**
     * @param pGeschlecht the mGeschlecht to set
     */
    public void setGeschlecht(final Character pGeschlecht) {
        if (pGeschlecht == null) {
            this.mGeschlecht = null;
        } else {
            this.mGeschlecht = Geschlecht.findByValue(pGeschlecht);
        }
    }

    /**
     * @param pGeschlecht the mGeschlecht to set
     */
    public void setGeschlecht(final String pGeschlecht) {
        final String geschlecht = pGeschlecht == null ? "" : pGeschlecht.trim();
        if (geschlecht.isEmpty()) {
            this.mGeschlecht = null;
        } else {
            this.mGeschlecht = Geschlecht.findByValue(geschlecht);
        }
    }

}
