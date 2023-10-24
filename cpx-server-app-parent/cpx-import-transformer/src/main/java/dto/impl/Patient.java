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
import dto.types.Geschlecht;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import line.AbstractLine;
import line.impl.PatientLine;

/**
 *
 * @author Dirk Niemeier
 */
public class Patient extends AbstractDto<Patient> {

    private static final long serialVersionUID = 1L;

    private String mPatNr = "";
    private String mNachname = "";
    private String mVorname = "";
    private Geschlecht mGeschlecht = null;
    private Date mGeburtsdatum = null;
    private String mTitel = "";
    private String mAdresse = "";
    private String mPlz = "";
    private String mOrt = "";
    private String mBundesland = "";
    private String mLand = "";
    private String mTelefon = "";
    private String mMobil = "";
    private String mVersichertennr = "";
    private String mKasse = "";

    protected final Set<Case> caseList = new LinkedHashSet<>();

    /**
     * @return the patNr REMOVING THIS METHODS RESULTS IN AN STACK OVERFLOW!
     */
    @Override
    public String getPatNr() {
        return mPatNr;
    }

    /**
     * @param pPatNr the ikz to set
     */
    public void setPatNr(final String pPatNr) {
        this.mPatNr = toStr(pPatNr);
    }

    @Override
    public PatientLine toLine() {
        PatientLine line = (PatientLine) AbstractDto.toLine(this);
        line.setValueObj(PatientLine.NACHNAME, getNachname());
        line.setValueObj(PatientLine.VORNAME, getVorname());
        line.setValueObj(PatientLine.GESCHLECHT, (getGeschlecht() == null) ? "" : getGeschlecht().getValue());
        line.setValueObj(PatientLine.GEBURTSDATUM, getGeburtsdatum());
        line.setValueObj(PatientLine.TITEL, getTitel());
        line.setValueObj(PatientLine.ADRESSE, getAdresse());
        line.setValueObj(PatientLine.PLZ, getPlz());
        line.setValueObj(PatientLine.ORT, getOrt());
        line.setValueObj(PatientLine.BUNDESLAND, getBundesland());
        line.setValueObj(PatientLine.LAND, getLand());
        line.setValueObj(PatientLine.TELEFON, getTelefon());
        line.setValueObj(PatientLine.MOBIL, getMobil());
        line.setValueObj(PatientLine.VERSICHERTENNR, getVersichertennr());
        line.setValueObj(PatientLine.KASSE, getKasse());
        return line;
    }

    @Override
    public Class<? extends AbstractLine> getLineClass() {
        return PatientLine.class;
    }

    protected boolean addCase(final Case pCase) {
        if (pCase == null) {
            throw new IllegalArgumentException("Hospital case is null!");
        }
        return caseList.add(pCase);
    }

    @Override
    public Set<DtoI> getDtos() {
        Set<DtoI> dtoSet = new LinkedHashSet<>();
        dtoSet.add(this);
        Iterator<Case> it = caseList.iterator();
        while (it.hasNext()) {
            Case cs = it.next();
            if (cs == null) {
                continue;
            }
            dtoSet.addAll(cs.getDtos());
        }
        return dtoSet;
    }

    @Override
    public Case getCase() {
        return null;
    }

    /**
     * @return the mNachname
     */
    public String getNachname() {
        return mNachname;
    }

    /**
     * @param pNachname the mNachname to set
     */
    public void setNachname(final String pNachname) {
        this.mNachname = toStr(pNachname);
    }

    /**
     * @return the mVorname
     */
    public String getVorname() {
        return mVorname;
    }

    /**
     * @param pVorname the mVorname to set
     */
    public void setVorname(final String pVorname) {
        this.mVorname = toStr(pVorname);
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

    /**
     * @return the mGeburtsdatum
     */
    public Date getGeburtsdatum() {
        return mGeburtsdatum == null ? null : new Date(mGeburtsdatum.getTime());
    }

    /**
     * @param pGeburtsdatum the mGeburtsdatum to set
     */
    public void setGeburtsdatum(final Date pGeburtsdatum) {
        this.mGeburtsdatum = pGeburtsdatum == null ? null : new Date(pGeburtsdatum.getTime());
    }

    /**
     * @return the mTitel
     */
    public String getTitel() {
        return mTitel;
    }

    /**
     * @param pTitel the mTitel to set
     */
    public void setTitel(final String pTitel) {
        this.mTitel = toStr(pTitel);
    }

    /**
     * @return the mAdresse
     */
    public String getAdresse() {
        return mAdresse;
    }

    /**
     * @param pAdresse the mAdresse to set
     */
    public void setAdresse(final String pAdresse) {
        this.mAdresse = toStr(pAdresse);
    }

    /**
     * @return the mPlz
     */
    public String getPlz() {
        return mPlz;
    }

    /**
     * @param pPlz the mPlz to set
     */
    public void setPlz(final String pPlz) {
        this.mPlz = toStr(pPlz);
    }

    /**
     * @return the mOrt
     */
    public String getOrt() {
        return mOrt;
    }

    /**
     * @param pOrt the mOrt to set
     */
    public void setOrt(final String pOrt) {
        this.mOrt = toStr(pOrt);
    }

    /**
     * @return the mBundesland
     */
    public String getBundesland() {
        return mBundesland;
    }

    /**
     * @param pBundesland the mBundesland to set
     */
    public void setBundesland(final String pBundesland) {
        this.mBundesland = toStr(pBundesland);
    }

    /**
     * @return the mLand
     */
    public String getLand() {
        return mLand;
    }

    /**
     * @param pLand the mLand to set
     */
    public void setLand(final String pLand) {
        this.mLand = toStr(pLand);
    }

    /**
     * @return the mTelefon
     */
    public String getTelefon() {
        return mTelefon;
    }

    /**
     * @param pTelefon the mTelefon to set
     */
    public void setTelefon(final String pTelefon) {
        this.mTelefon = toStr(pTelefon);
    }

    /**
     * @return the mMobil
     */
    public String getMobil() {
        return mMobil;
    }

    /**
     * @param pMobil the mMobil to set
     */
    public void setMobil(final String pMobil) {
        this.mMobil = toStr(pMobil);
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

    public boolean removeCase(final Case pCase) {
        if (pCase == null) {
            throw new IllegalArgumentException("Case is null!");
        }
        return caseList.remove(pCase);
    }

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
        return ((DtoI) pObj).getPatNr().equals(this.getPatNr());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.mPatNr);
        return hash;
    }

    @Override
    public String getIkz() {
        return "";
    }

    @Override
    public String getFallNr() {
        return "";
    }

    @Override
    public void set(Patient pOther) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
