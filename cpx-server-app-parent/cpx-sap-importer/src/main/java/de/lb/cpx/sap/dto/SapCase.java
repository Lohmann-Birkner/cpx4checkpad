/* 
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.sap.dto;

import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;
import de.lb.cpx.sap.container.BewegungContainer;
import de.lb.cpx.sap.container.DiagnosenContainer;
import de.lb.cpx.sap.container.ProzedurenContainer;
//import static de.lb.cpx.sap.importer.ImportProcessSap.ANONYMOUSIZE_DATA;
import de.lb.cpx.sap.importer.SapConnection;
import static de.lb.cpx.sap.importer.utils.SapStrUtils.*;
import de.lb.cpx.sap.results.SapAdmissionSearchResult;
import de.lb.cpx.sap.results.SapDiagnosisSearchResult;
import de.lb.cpx.sap.results.SapDischargeSearchResult;
import de.lb.cpx.sap.results.SapMovementSearchResult;
import de.lb.cpx.sap.results.SapPatientDetailResult;
import de.lb.cpx.sap.results.SapPatientSearchResult;
import de.lb.cpx.sap.results.SapProcedureSearchResult;
import de.lb.cpx.sap.results.SapReturnResult;
import dto.RmeDiagnose;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 */
public class SapCase {

    private static final Logger LOG = Logger.getLogger(SapCase.class.getName());

    private static final int STATE_NO = 0;
    private static final int STATE_LEADING = 1;
    private static final int STATE_WK = 2;
    private static final int STATE_NOTEXISTS = 3;
    private static final long MSEC_PER_DAY = 60L * 60 * 24 * 1000;
    private static final int DAYS_NON_LEAP_YEAR = 365;
    private static final int DAYS_LEAP_YEAR = 366;

    private boolean isLeading = false;

    private boolean isWK = false;
    private int state = STATE_NO;
    private String fallnrLeading = "";
    private String institution = "";
    private String fallnr = "";
    private int admissionWeight = 0;
    private String beatmungsDauer = "";
    private int kisState = 0;
    private String aufnahmeanlass = "";
    private String ausnahmeanlassSap = "";

    private final List<String> mWkCases = new ArrayList<>();
    private final List<ReferenzCase> mRefCases = new ArrayList<>();
    private String refCase = "";
    private String refCaseType = "";
    private SapCaseDrg mSapdrg = null;
    private final List<String> primDiags = new ArrayList<>();
    private final List<String> sekDiags = new ArrayList<>();
    private final List<String> allDiags = new ArrayList<>();
    private Date kisstateFirstDate = null;
    private Date kisstateSecondDate = null;
    private String insKOSTR = "";
    private int erbringungsart = 0;

    private int falltyp = 1;
    private String ikz = "";
    private Insurance insurance;

    private Date aufnahmedatum = null;
    private Date entlassungdatum = null;
    private int aufnahmegewicht = 0;
    private String geschlecht = "u";
    private String aufnahmegrund12 = "0";
    private String aufnahmegrund34 = "0";
    private String entlassungsstation = "";
    private String entlassungsabteilung = "";
    private String entlassungsgrund12 = "0";
    private String entlassungsgrund3 = "9";
    private String beatmungsstunden = "0";
    private String plz = "";
    private Date geburtsdatum = null;
    private String nachname = "";
    private String vorname = "";
    private String patientnr = "";
    private String titel = "";
    private String telefon = "";
    private String mobiltelefon = "";
//  public int falltyp = 1;
    private String land = "";
    private String ort = "";
    private String adresse = "";
    private String bundesland = "";
    private boolean valid = false;
    private int urlaub = 0;
    private int alterInTagen = 0;
    private int alterInJahren = 0;
    private String kasse = "";
    private int tob = 0;
    private int mdTOB = 0;
//    private int erbringungsart = 0;
    private int vwdIntensiv = 0;
    private Date datumRechnung = null;
    private final List<BewegungContainer> wards = new ArrayList<>();
    private final List<CaseEntg> fees = new ArrayList<>();
    private SapExportResult fiDaten = null;
    private Map<String, RmlLaborDocument> labordaten;
    private SapExportResult fiOpenCaseStatus = null;
//    private final List<SapKainDetailSearchResult> kainMessages = new ArrayList<>();
    private int stasp; //staatbürgerkennzeichen 
    private int resid;//kein Staatsbürger 
    private int forei;//Ausländerkennzeichen
    private String bekat;
    private String caseState;

    /**
     *
     */
    public SapCase() {
        super();
    }

    /**
     * @return the STATE_NO
     */
    public static int getSTATE_NO() {
        return STATE_NO;
    }

    /**
     * @return the STATE_LEADING
     */
    public static int getSTATE_LEADING() {
        return STATE_LEADING;
    }

    /**
     * @return the STATE_WK
     */
    public static int getSTATE_WK() {
        return STATE_WK;
    }

    /**
     * @return the STATE_NOTEXISTS
     */
    public static int getSTATE_NOTEXISTS() {
        return STATE_NOTEXISTS;
    }

    /**
     * @return the MSEC_PER_DAY
     */
    public static long getMSEC_PER_DAY() {
        return MSEC_PER_DAY;
    }

    /**
     * @return the DAYS_NON_LEAP_YEAR
     */
    public static int getDAYS_NON_LEAP_YEAR() {
        return DAYS_NON_LEAP_YEAR;
    }

    /**
     * @return the DAYS_LEAP_YEAR
     */
    public static int getDAYS_LEAP_YEAR() {
        return DAYS_LEAP_YEAR;
    }

    /**
     * @return the isLeading
     */
    public boolean isIsLeading() {
        return isLeading;
    }

    /**
     * @param isLeading the isLeading to set
     */
    public void setIsLeading(boolean isLeading) {
        this.isLeading = isLeading;
    }

    /**
     * @return the isWK
     */
    public boolean isIsWK() {
        return isWK;
    }

    /**
     * @param isWK the isWK to set
     */
    public void setIsWK(boolean isWK) {
        this.isWK = isWK;
    }

    /**
     * @return the state
     */
    public int getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * @return the fallnrLeading
     */
    public String getFallnrLeading() {
        return fallnrLeading;
    }

    /**
     * @param fallnrLeading the fallnrLeading to set
     */
    public void setFallnrLeading(String fallnrLeading) {
        this.fallnrLeading = fallnrLeading;
    }

    /**
     * @return the institution
     */
    public String getInstitution() {
        return institution;
    }

    /**
     * @param institution the institution to set
     */
    public void setInstitution(String institution) {
        this.institution = institution;
    }

    /**
     * @return the fallnr
     */
    public String getFallnr() {
        return fallnr;
    }

    /**
     * @param fallnr the fallnr to set
     */
    public void setFallnr(final String fallnr) {
//        String correctedFallNr = getCorrectSAPCaseNumber(fallnr);
//        if (fallnr != null && correctedFallNr != null && !fallnr.equals(correctedFallNr)) {
//            if (fallnr.length() < correctedFallNr.length()) {
//                LOG.log(Level.INFO, "Corrected case number (added leading zeros to fixed length of 10 characters): {0} -> {1}", new Object[]{fallnr, correctedFallNr});
//            } else {
//                LOG.log(Level.WARNING, "Corrected case number (cut off anything beyond 10 characters): {0} -> {1}", new Object[]{fallnr, correctedFallNr});
//            }
//        }
        this.fallnr = fallnr;
    }

    /**
     *
     * @param pFallNr case number
     * @return SAP corrected case number
     */
    protected static String getCorrectSAPCaseNumber(final String pFallNr) {
        String fallNr = pFallNr;
        int lg = pFallNr != null ? pFallNr.length() : 0;
        if (lg < 10 && lg > 0) {
            fallNr = leftPad(fallNr, 10, '0');
//            for (int i = lg; i < 10; i++) {
//                fallNr = "0" + pFallNr;
//            }
        }
        if (lg > 10 && fallNr != null) {
            fallNr = fallNr.substring(0, 10);
        }
        return fallNr;
    }

    /**
     * @return the admissionWeight
     */
    public int getAdmissionWeight() {
        return admissionWeight;
    }

    /**
     * @param admissionWeight the admissionWeight to set
     */
    public void setAdmissionWeight(int admissionWeight) {
        this.admissionWeight = admissionWeight;
    }

    /**
     * @return the beatmungsDauer
     */
    public String getBeatmungsDauer() {
        return beatmungsDauer;
    }

    /**
     * @param beatmungsDauer the beatmungsDauer to set
     */
    public void setBeatmungsDauer(String beatmungsDauer) {
        this.beatmungsDauer = beatmungsDauer;
    }

    /**
     * @return the kisState
     */
    public int getKisState() {
        return kisState;
    }

    /**
     * @param kisState the kisState to set
     */
    public void setKisState(int kisState) {
        this.kisState = kisState;
    }

    /**
     * @return the aufnahmeanlass
     */
    public String getAufnahmeanlass() {
        return aufnahmeanlass;
    }

    /**
     * @param aufnahmeanlass the aufnahmeanlass to set
     */
    public void setAufnahmeanlass(String aufnahmeanlass) {
        this.aufnahmeanlass = aufnahmeanlass;
    }

    /**
     * @return the ausnahmeanlassSap
     */
    public String getAusnahmeanlassSap() {
        return ausnahmeanlassSap;
    }

    /**
     * @param ausnahmeanlassSap the ausnahmeanlassSap to set
     */
    public void setAusnahmeanlassSap(String ausnahmeanlassSap) {
        this.ausnahmeanlassSap = ausnahmeanlassSap;
    }

//    /**
//     * @return the m_wkCases
//     */
//    public List<String> getM_wkCases() {
//        return m_wkCases;
//    }
//    /**
//     * @param m_wkCases the m_wkCases to set
//     */
//    public void setM_wkCases(List<String> m_wkCases) {
//        this.m_wkCases = m_wkCases;
//    }
//    /**
//     * @return the m_refCases
//     */
//    public List<ReferenzCase> getM_refCases() {
//        return m_refCases;
//    }
//
//    /**
//     * @param m_refCases the m_refCases to set
//     */
//    public void setM_refCases(List<ReferenzCase> m_refCases) {
//        this.m_refCases = m_refCases;
//    }
    /**
     * @return the refCase
     */
    public String getRefCase() {
        return refCase;
    }

    /**
     * @param refCase the refCase to set
     */
    public void setRefCase(String refCase) {
        this.refCase = refCase;
    }

    /**
     * @return the refCaseType
     */
    public String getRefCaseType() {
        return refCaseType;
    }

    /**
     * @param refCaseType the refCaseType to set
     */
    public void setRefCaseType(String refCaseType) {
        this.refCaseType = refCaseType;
    }

    /**
     * @return the mSapdrg
     */
    public SapCaseDrg getmSapdrg() {
        return mSapdrg;
    }

    /**
     * @param mSapdrg the mSapdrg to set
     */
    public void setmSapdrg(SapCaseDrg mSapdrg) {
        this.mSapdrg = mSapdrg;
    }

//    /**
//     * @return the primDiags
//     */
//    public List<String> getPrimDiags() {
//        return primDiags;
//    }
//
//    /**
//     * @param primDiags the primDiags to set
//     */
//    public void setPrimDiags(List<String> primDiags) {
//        this.primDiags = primDiags;
//    }
//    /**
//     * @return the sekDiags
//     */
//    public List<String> getSekDiags() {
//        return sekDiags;
//    }
//
//    /**
//     * @param sekDiags the sekDiags to set
//     */
//    public void setSekDiags(List<String> sekDiags) {
//        this.sekDiags = sekDiags;
//    }
//    /**
//     * @return the allDiags
//     */
//    public List<String> getAllDiags() {
//        return allDiags;
//    }
//
//    /**
//     * @param allDiags the allDiags to set
//     */
//    public void setAllDiags(List<String> allDiags) {
//        this.allDiags = allDiags;
//    }
    /**
     * @return the kisstateFirstDate
     */
    public Date getKisstateFirstDate() {
        return kisstateFirstDate == null ? null : new Date(kisstateFirstDate.getTime());
    }

    /**
     * @param kisstateFirstDate the kisstateFirstDate to set
     */
    public void setKisstateFirstDate(Date kisstateFirstDate) {
        this.kisstateFirstDate = kisstateFirstDate == null ? null : new Date(kisstateFirstDate.getTime());
    }

    /**
     * @return the kisstateSecondDate
     */
    public Date getKisstateSecondDate() {
        return kisstateSecondDate == null ? null : new Date(kisstateSecondDate.getTime());
    }

    /**
     * @param kisstateSecondDate the kisstateSecondDate to set
     */
    public void setKisstateSecondDate(Date kisstateSecondDate) {
        this.kisstateSecondDate = kisstateSecondDate == null ? null : new Date(kisstateSecondDate.getTime());
    }

    /**
     * @return the insKOSTR
     */
    public String getInsKOSTR() {
        return insKOSTR;
    }

    /**
     * @param insKOSTR the insKOSTR to set
     */
    public void setInsKOSTR(String insKOSTR) {
        this.insKOSTR = insKOSTR;
    }

    /**
     * @return the mErbringungsart
     */
    public int getErbringungsart() {
        return erbringungsart;
    }

    /**
     * @param erbringungsart the mErbringungsart to set
     */
    public void setErbringungsart(int erbringungsart) {
        this.erbringungsart = erbringungsart;
    }

    /**
     * @return the falltyp
     */
    public int getFalltyp() {
        return falltyp;
    }

    /**
     * @param falltyp the falltyp to set
     */
    public void setFalltyp(int falltyp) {
        this.falltyp = falltyp;
    }

    /**
     * @return the ikz
     */
    public String getIkz() {
        return ikz;
    }

    /**
     * @param ikz the ikz to set
     */
    public void setIkz(String ikz) {
        this.ikz = ikz;
    }

    /**
     * @return the insurance
     */
    public Insurance getInsurance() {
        return insurance;
    }

    /**
     * @param insurance the insurance to set
     */
    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
    }

    /**
     * @return the aufnahmedatum
     */
    public Date getAufnahmedatum() {
        return aufnahmedatum == null ? null : new Date(aufnahmedatum.getTime());
    }

    /**
     * @param aufnahmedatum the aufnahmedatum to set
     */
    public void setAufnahmedatum(Date aufnahmedatum) {
        this.aufnahmedatum = aufnahmedatum == null ? null : new Date(aufnahmedatum.getTime());
    }

    /**
     * @return the entlassungdatum
     */
    public Date getEntlassungdatum() {
        return entlassungdatum == null ? null : new Date(entlassungdatum.getTime());
    }

    /**
     * @param entlassungdatum the entlassungdatum to set
     */
    public void setEntlassungdatum(Date entlassungdatum) {
        this.entlassungdatum = entlassungdatum == null ? null : new Date(entlassungdatum.getTime());
    }

    /**
     * @return the aufnahmegewicht
     */
    public int getAufnahmegewicht() {
        return aufnahmegewicht;
    }

    /**
     * @param aufnahmegewicht the aufnahmegewicht to set
     */
    public void setAufnahmegewicht(int aufnahmegewicht) {
        this.aufnahmegewicht = aufnahmegewicht;
    }

    /**
     * @return the geschlecht
     */
    public String getGeschlecht() {
        return geschlecht;
    }

    /**
     * @param geschlecht the geschlecht to set
     */
    public void setGeschlecht(String geschlecht) {
        this.geschlecht = geschlecht;
    }

    /**
     * @return the aufnahmegrund12
     */
    public String getAufnahmegrund12() {
        return aufnahmegrund12;
    }

    /**
     * @param aufnahmegrund12 the aufnahmegrund12 to set
     */
    public void setAufnahmegrund12(String aufnahmegrund12) {
        this.aufnahmegrund12 = aufnahmegrund12;
    }

    /**
     * @return the aufnahmegrund34
     */
    public String getAufnahmegrund34() {
        return aufnahmegrund34;
    }

    /**
     * @param aufnahmegrund34 the aufnahmegrund34 to set
     */
    public void setAufnahmegrund34(String aufnahmegrund34) {
        this.aufnahmegrund34 = aufnahmegrund34;
    }

    /**
     * @return the entlassungsstation
     */
    public String getEntlassungsstation() {
        return entlassungsstation;
    }

    /**
     * @param entlassungsstation the entlassungsstation to set
     */
    public void setEntlassungsstation(String entlassungsstation) {
        this.entlassungsstation = entlassungsstation;
    }

    /**
     * @return the entlassungsabteilung
     */
    public String getEntlassungsabteilung() {
        return entlassungsabteilung;
    }

    /**
     * @param entlassungsabteilung the entlassungsabteilung to set
     */
    public void setEntlassungsabteilung(String entlassungsabteilung) {
        this.entlassungsabteilung = entlassungsabteilung;
    }

    /**
     * @return the entlassungsgrund12
     */
    public String getEntlassungsgrund12() {
        return entlassungsgrund12;
    }

    /**
     * @param entlassungsgrund12 the entlassungsgrund12 to set
     */
    public void setEntlassungsgrund12(String entlassungsgrund12) {
        this.entlassungsgrund12 = entlassungsgrund12;
    }

    /**
     * @return the entlassungsgrund3
     */
    public String getEntlassungsgrund3() {
        return entlassungsgrund3;
    }

    /**
     * @param entlassungsgrund3 the entlassungsgrund3 to set
     */
    public void setEntlassungsgrund3(String entlassungsgrund3) {
        this.entlassungsgrund3 = entlassungsgrund3;
    }

    /**
     * @return the beatmungsstunden
     */
    public String getBeatmungsstunden() {
        return beatmungsstunden;
    }

    /**
     * @param beatmungsstunden the beatmungsstunden to set
     */
    public void setBeatmungsstunden(String beatmungsstunden) {
        this.beatmungsstunden = beatmungsstunden;
    }

    /**
     * @return the plz
     */
    public String getPlz() {
        return plz;
    }

    /**
     * @param plz the plz to set
     */
    public void setPlz(String plz) {
        this.plz = plz;
    }

    /**
     * @return the geburtsdatum
     */
    public Date getGeburtsdatum() {
        return geburtsdatum == null ? null : new Date(geburtsdatum.getTime());
    }

    /**
     * @param geburtsdatum the geburtsdatum to set
     */
    public void setGeburtsdatum(Date geburtsdatum) {
        this.geburtsdatum = geburtsdatum == null ? null : new Date(geburtsdatum.getTime());
    }

    /**
     * @return the nachname
     */
    public String getNachname() {
        return nachname;
    }

    /**
     * @param nachname the nachname to set
     */
    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    /**
     * @return the vorname
     */
    public String getVorname() {
        return vorname;
    }

    /**
     * @param vorname the vorname to set
     */
    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    /**
     * @return the patientnr
     */
    public String getPatientnr() {
        return patientnr;
    }

    /**
     * @param patientnr the patientnr to set
     */
    public void setPatientnr(String patientnr) {
        this.patientnr = patientnr;
    }

    /**
     * @return the titel
     */
    public String getTitel() {
        return titel;
    }

    /**
     * @param titel the titel to set
     */
    public void setTitel(String titel) {
        this.titel = titel;
    }

    /**
     * @return the telefon
     */
    public String getTelefon() {
        return telefon;
    }

    /**
     * @param telefon the telefon to set
     */
    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    /**
     * @return the mobiltelefon
     */
    public String getMobiltelefon() {
        return mobiltelefon;
    }

    /**
     * @param mobiltelefon the mobiltelefon to set
     */
    public void setMobiltelefon(String mobiltelefon) {
        this.mobiltelefon = mobiltelefon;
    }

    /**
     * @return the land
     */
    public String getLand() {
        return land;
    }

    /**
     * @param land the land to set
     */
    public void setLand(String land) {
        this.land = land;
    }

    /**
     * @return the ort
     */
    public String getOrt() {
        return ort;
    }

    /**
     * @param ort the ort to set
     */
    public void setOrt(String ort) {
        this.ort = ort;
    }

    /**
     * @return the adresse
     */
    public String getAdresse() {
        return adresse;
    }

    /**
     * @param adresse the adresse to set
     */
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    /**
     * @return the bundesland
     */
    public String getBundesland() {
        return bundesland;
    }

    /**
     * @param bundesland the bundesland to set
     */
    public void setBundesland(String bundesland) {
        this.bundesland = bundesland;
    }

    /**
     * @return the valid
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * @param valid the valid to set
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * @return the urlaub
     */
    public int getUrlaub() {
        return urlaub;
    }

    /**
     * @param urlaub the urlaub to set
     */
    public void setUrlaub(int urlaub) {
        this.urlaub = urlaub;
    }

    /**
     * @return the alterInTagen
     */
    public int getAlterInTagen() {
        return alterInTagen;
    }

    /**
     * @param alterInTagen the alterInTagen to set
     */
    public void setAlterInTagen(int alterInTagen) {
        this.alterInTagen = alterInTagen;
    }

    /**
     * @return the alterInJahren
     */
    public int getAlterInJahren() {
        return alterInJahren;
    }

    /**
     * @param alterInJahren the alterInJahren to set
     */
    public void setAlterInJahren(int alterInJahren) {
        this.alterInJahren = alterInJahren;
    }

    /**
     * @return the kasse
     */
    public String getKasse() {
        return kasse;
    }

    /**
     * @param kasse the kasse to set
     */
    public void setKasse(String kasse) {
        this.kasse = kasse;
    }

    /**
     * @return the tob
     */
    public int getTob() {
        return tob;
    }

    /**
     * @param tob the tob to set
     */
    public void setTob(int tob) {
        this.tob = tob;
    }

    /**
     * @return the mdTOB
     */
    public int getMDTob() {
        return mdTOB;
    }

    /**
     * @param mdtob the mdtob to set
     */
    public void setMDTob(int mdtob) {
        this.mdTOB = mdtob;
    }

    /**
     * @return the datumRechnung
     */
    public Date getRechnungsdatum() {
        return datumRechnung == null ? null : new Date(datumRechnung.getTime());
    }

    /**
     * @param datumRechnung the datumRechnung to set
     */
    public void setRechnungsdatum(Date datumRechnung) {
        this.datumRechnung = datumRechnung == null ? null : new Date(datumRechnung.getTime());
    }

//    /**
//     * @return the erbringungsart
//     */
//    public int getErbringungsart() {
//        return erbringungsart;
//    }
//
//    /**
//     * @param erbringungsart the erbringungsart to set
//     */
//    public void setErbringungsart(int erbringungsart) {
//        this.erbringungsart = erbringungsart;
//    }
    /**
     * @return the vwdIntensiv
     */
    public int getVwdIntensiv() {
        return vwdIntensiv;
    }

    /**
     * @param vwdIntensiv the vwdIntensiv to set
     */
    public void setVwdIntensiv(int vwdIntensiv) {
        this.vwdIntensiv = vwdIntensiv;
    }

//    /**
//     * @param wards the wards to set
//     */
//    public void setWards(List<BewegungContainer> wards) {
//        this.wards = wards;
//    }
//    /**
//     * @param fees the fees to set
//     */
//    public void setFees(List<CaseEntg> fees) {
//        this.fees = fees;
//    }
    /**
     * @return the fiDaten
     */
    public SapExportResult getFiDaten() {
        return fiDaten;
    }

    /**
     * @param fiDaten the fiDaten to set
     */
    public void setFiDaten(SapExportResult fiDaten) {
        this.fiDaten = fiDaten;
    }

    /**
     * @return the labordaten
     */
    public Map<String, RmlLaborDocument> getLabordaten() {
        return labordaten;
    }

    /**
     * @param labordaten the labordaten to set
     */
    public void setLabordaten(Map<String, RmlLaborDocument> labordaten) {
        this.labordaten = labordaten;
    }

    /**
     * @return the fiOpenCaseStatus
     */
    public SapExportResult getFiOpenCaseStatus() {
        return fiOpenCaseStatus;
    }

    /**
     * @param fiOpenCaseStatus the fiOpenCaseStatus to set
     */
    public void setFiOpenCaseStatus(SapExportResult fiOpenCaseStatus) {
        this.fiOpenCaseStatus = fiOpenCaseStatus;
    }

//    /**
//     * @return the kain_messages
//     */
//    public List<SapKainDetailSearchResult> getKain_messages() {
//        return kain_messages;
//    }
//
//    /**
//     * @param kain_messages the kain_messages to set
//     */
//    public void setKain_messages(List<SapKainDetailSearchResult> kain_messages) {
//        this.kain_messages = kain_messages;
//    }
    /**
     *
     * @param pDiagnose icd
     */
    public void addPrimSekDiag(final SapDiagnosisSearchResult pDiagnose) {
        allDiags.add(pDiagnose.getDiagSeqno());
        int refType = getDiagRefType(pDiagnose.getDiagRefTyp());
        if (refType == RmeDiagnose.REF_TYPE_KREUZ
                || refType == RmeDiagnose.REF_TYPE_ADDITIONAL_TO) {
            addPrimDiag(pDiagnose);
        } else if (refType == RmeDiagnose.REF_TYPE_STERN
                || refType == RmeDiagnose.REF_TYPE_ADDITIONAL) {
            addSekDiag(pDiagnose);
        }
    }

    /**
     *
     * @param pMandant client
     * @param pInstitution institution
     * @return state
     */
    public int checkPrimSekDiagReferences(final String pMandant, final String pInstitution) {
        int stateTmp = 0;
        String sekDiag;
        int n = sekDiags.size();
        LOG.log(Level.INFO, "Prüfe Sekundaer-Diagnosen {0}", String.valueOf(n));
        for (int i = 0; i < n; i++) {
            sekDiag = sekDiags.get(i);
            if (primDiags.contains(sekDiag)) {
                primDiags.remove(sekDiag);
                LOG.log(Level.INFO, "Sekundaer Diagnose {0} in Primaer-Diagnosen gefunden {1}", new Object[]{sekDiag, String.valueOf(n)});
            } else {
                if (allDiags != null && allDiags.contains(sekDiag)) {
                    LOG.log(Level.INFO, "Sekundaer Diagnose {0} in Diagnosen gefunden {1}", new Object[]{sekDiag, String.valueOf(n)});
                } else {
                    LOG.log(Level.INFO, "Sekundaer Diagnose {0} in NICHT Diagnosen gefunden {1}", new Object[]{sekDiag, String.valueOf(n)});
                    stateTmp = 1;
                }
            }
        }

        int k = primDiags.size();
        LOG.log(Level.INFO, "Prüfe Primaere-Diagnosen {0}", String.valueOf(k));
        if (k > 0) {
            stateTmp = 10 + stateTmp;
        }
        return stateTmp;
    }

    /**
     *
     * @param pDiagnose icd
     */
    public void addPrimDiag(final SapDiagnosisSearchResult pDiagnose) {
        if (pDiagnose == null) {
            return;
        }
        primDiags.add(pDiagnose.getDiagSeqno());
    }

    /**
     *
     * @param pDiagnose icd
     */
    public void addSekDiag(final SapDiagnosisSearchResult pDiagnose) {
        if (pDiagnose == null) {
            return;
        }
        sekDiags.add(pDiagnose.getDiaLink());
    }

    /**
     *
     * @return line break separated stuff
     */
    public String getWKCasesText() {
        StringBuilder tt = new StringBuilder();
        for (int i = 0; i < mWkCases.size(); i++) {
            tt.append(mWkCases.get(i));
            tt.append("\n");
        }
        return tt.toString();
    }

    /**
     *
     * @param pStructCase JCO structured case
     */
    public void readFromCaseStructur(final JCoStructure pStructCase) {
        setAufnahmegewicht(transformAdmissionWeight(pStructCase.getString("PATGEW"), pStructCase.getString("GWEIN")));
        LOG.log(Level.FINE, "Aufnahmegewicht: {0} {1}", new Object[]{pStructCase.getString("PATGEW"), pStructCase.getString("GWEIN")});

        final String respi = pStructCase.getString("RESPI");
        try {
            int j = Integer.parseInt(respi);
            setBeatmungsstunden(String.valueOf(j));
        } catch (NumberFormatException ex) {
            LOG.log(Level.WARNING, "Cannot parse respi string to integer: " + respi, ex);
        }
    }

    /**
     *
     * @param pSapConnection sap connection
     * @param pPatient patient
     * @param pPatientDetails details
     * @param pMovementList department list
     * @param pDiagnosisList icd list
     * @param pProcedureList ops list
     * @param pKainKeys4Cases whatever, some keys
     * @throws JCoException Exception
     */
    public void setData(final SapConnection pSapConnection,
            final SapPatientSearchResult pPatient,
            final SapPatientDetailResult pPatientDetails,
            final List<SapMovementSearchResult> pMovementList,
            final List<SapDiagnosisSearchResult> pDiagnosisList,
            final List<SapProcedureSearchResult> pProcedureList,
            final Set<String> pKainKeys4Cases/*,
            final List<SapNleiSearchResult> pNleis */) throws JCoException {

        final Insurance ins = pSapConnection.getCurrentInsurance(getInstitution(), getFallnr());

        setInsurance(ins);
        setInsKOSTR(ins != null ? toStr(ins.getKostr()) : "");
        setKasse(ins != null ? toStr(ins.getInstituteInd()) : "");

        setGeburtsdatum(pPatient.getDob());
        setNachname(pSapConnection.isDoAnonymize() ? "Anonym" : checkQuotes(pPatient.getLastNamePat()));
        setVorname(pSapConnection.isDoAnonymize() ? "Anonym" : checkQuotes(pPatient.getFrstNamePat()));
        setPatientnr(pSapConnection.isDoAnonymize() ? "ANO_" + getHash(pPatient.getPatientId(), 20) : pPatient.getPatientId());
        setTitel(pSapConnection.isDoAnonymize() ? "Anonym" : pPatient.getTitle());
        setTelefon(pSapConnection.isDoAnonymize() ? "Anonym" : pPatient.getPhoneNo());
        setMobiltelefon(pSapConnection.isDoAnonymize() ? "Anonym" : pPatientDetails.getOtherPhones());
        setLand(pSapConnection.isDoAnonymize() ? "" : pPatient.getCountry());
        setOrt(pSapConnection.isDoAnonymize() ? "" : pPatient.getCity());
        setAdresse(pSapConnection.isDoAnonymize() ? "Anonym" : pPatient.getAddress());
        setBundesland(pSapConnection.isDoAnonymize() ? "" : pPatient.getDistrict());
        //plz = pPatientDetials.REGION;
        setPlz(pSapConnection.isDoAnonymize() ? "" : pPatientDetails.getPcd());
        setTob(pPatientDetails.getTob());
        setMDTob(pPatientDetails.getMDTob());
        // übertragen der Werte aus patientDetails für staatbürgerkennzeichen ,"kein Staatsbürger ,Ausländerkennzeichen 

        setStasp(pPatientDetails.getStasp());
        setResid(pPatientDetails.getResid());
        setForei(pPatientDetails.getForei());
        setBekat(pPatientDetails.getBekat());
        setCaseState(pPatientDetails.getCaseState());

        SapMovementSearchResult mv; //
        SapAdmissionSearchResult admission = null; //Aufnahmedatensatz
        SapDischargeSearchResult discharge = null; //Entlassungsdatensatz
        BewegungContainer aufIndex = null, behIndex = null, entIndex = null;
        long dauer = 0;
        String anl;
        BewegungContainer bewegung = null;
        BewegungContainer statBew = null;
        SapMovementSearchResult opBewegung = null;

        for (int i = 0, n = pMovementList.size(); i < n; i++) {
            mv = pMovementList.get(i);
            if (mv.getStorn().length() > 0) {
                continue;
            }
            //LOG.log(Level.INFO, "getFallnr(): " + getFallnr() + " -> mv.getBEWTY(): " + mv.getBEWTY() + ", mv.getBWART(): " + mv.getBWART());
            //1. gefundener Aufnahemdatensatz
            if ("1".equals(mv.getBewty()) || (importAmbulantOP() && "AO".equals(mv.getBwart()))
                    || (importAmbulantCancels() && "VO".equals(mv.getBwart()))) {
                if (admission == null || "1".equals(mv.getBewty())) {
                    admission = new SapAdmissionSearchResult();
                    setAufnahmedatum(combineDate(mv.getBwidt(), mv.getBwizt()));
                    setAufnahmegrund12(mv.getBwgr1());
                    if ((importAmbulantOP() && "AO".equals(mv.getBwart())) || (importAmbulantCancels() && "VO".equals(mv.getBwart()))) {
                        setAufnahmegrund12("4");
                    }
                    if (getAufnahmegrund12() != null && "97".equals(getAufnahmegrund12())) {
                        setAufnahmegrund12("1");
                    }
                    setAufnahmegrund34(mv.getBwgr2());
                    anl = getAufnahmeArt(pSapConnection, mv.getBwart(), this);
                    if (getAufnahmegrund12().length() == 0) {
                        setAufnahmegrund12("0");
                    }
                    try {
                        int ii = Integer.parseInt(getAufnahmegrund12());
                        setAufnahmegrund12(String.valueOf(ii));
                    } catch (NumberFormatException e) {
                        setAufnahmegrund12("0");
                    }
                    if (getAufnahmegrund34().length() == 0) {
                        setAufnahmegrund34("0");
                    }
                    try {
                        int ii = Integer.parseInt(getAufnahmegrund34());
                        setAufnahmegrund34(String.valueOf(ii));
                    } catch (NumberFormatException e) {
                        setAufnahmegrund34("0");
                    }
                    if (pSapConnection.sapProperties.getSSTVersion() <= 1 && pSapConnection.sapProperties.getP301AufnahmeartMap() != null) {
                        if (anl != null) {
                            setAufnahmeanlass(SapConnection.getCpAufnahmeanlass(anl));
                        }
                    }
                }
            }
            //1. gefundenen Entlassung
            if ("2".equals(mv.getBewty()) || (importAmbulantOP() && "AO".equals(mv.getBwart()))
                    || (importAmbulantCancels() && "VO".equals(mv.getBwart()))) {
                if (discharge == null || "2".equals(mv.getBewty())) {
                    discharge = new SapDischargeSearchResult();
                    setEntlassungsgrund12(mv.getBwgr1());
                    setEntlassungsgrund3(mv.getBwgr2());
                    if (getEntlassungsgrund12().length() == 0) {
                        setEntlassungsgrund12("0");
                    }
                    try {
                        int ii = Integer.parseInt(getEntlassungsgrund12());
                        setEntlassungsgrund12(String.valueOf(ii));
                    } catch (NumberFormatException e) {
                        setEntlassungsgrund12("0");
                    }
                    if (getEntlassungsgrund3().length() != 1) {
                        setEntlassungsgrund3("9");
                    }
                    try {
                        int ii = Integer.parseInt(getEntlassungsgrund3());
                        setEntlassungsgrund3(String.valueOf(ii));
                    } catch (NumberFormatException e) {
                        setEntlassungsgrund3("9");
                    }
                    setEntlassungdatum(combineDate(mv.getBwidt(), mv.getBwizt()));
                    setEntlassungsstation(mv.getOrgpf());
                    setEntlassungsabteilung(mv.getOrgfa());
                }
            }
        }
        if (getAufnahmedatum() == null && getEntlassungdatum() == null) {
            return;
        }
        if (getPlz() != null && getPlz().length() > 10) {
            setPlz(getPlz().substring(0, 10));
        }
        // Korrektur: Entlassungsdatum ist Abgangsdatum der letzten stat. Bewegung
        for (int i = 0, n = pMovementList.size(); i < n; i++) {
            mv = pMovementList.get(i);
            if (mv.getStorn().length() > 0) {
                continue;
            }
            boolean isIntensiv;
            if ("1".equals(mv.getBewty()) || (importAmbulantOP() && "AO".equals(mv.getBwart()))
                    || (importAmbulantCancels() && "VO".equals(mv.getBwart()))) {
                isIntensiv = isWardIntensiv(mv.getOrgpf(), getAufnahmedatum());
                bewegung = new BewegungContainer(pSapConnection, mv, pSapConnection.mMandant, getInstitution(), getErbringungsart(), isIntensiv);
                LOG.log(Level.FINEST, "Füge Bewegung in Wards-Liste: {0}, {1}", new Object[]{bewegung.getSpecDescription(), bewegung.getSpecKey()});
                wards.add(bewegung);
                statBew = bewegung;
                aufIndex = bewegung; // falls es nur eine aufnehmende und eine entlassende bewegung gibt
                behIndex = bewegung; // kommen BEWTY = 1 und BEWTY = 2 vor, aber BEWTY = 3 niemals.
                entIndex = bewegung; // Deshalb ist die 1te schon aufn, beh, und entl
                checkCreateDiagnosis(bewegung, bewegung.getSeqNo(), pDiagnosisList);
                checkCreateProcedure(bewegung, bewegung.getSeqNo(), pProcedureList);
                if (opBewegung != null) {
                    checkCreateDiagnosis(bewegung, opBewegung.getLfdnr(), pDiagnosisList);
                    checkCreateProcedure(bewegung, opBewegung.getLfdnr(), pProcedureList);
                    opBewegung = null;
                }
                setVwdIntensiv(getVwdIntensiv() + bewegung.getBeatmungsdauer());
            } else if ("3".equals(mv.getBewty())
                    || "7".equals(mv.getBewty())
                    || (importAmbulantOP() && "AO".equals(mv.getBwart()))
                    || (importAmbulantCancels() && "VO".equals(mv.getBwart()))) { // Verlegung oder Ende Abwesenheit
                isIntensiv = isWardIntensiv(mv.getOrgpf(), getAufnahmedatum());
                bewegung = new BewegungContainer(pSapConnection, mv, pSapConnection.mMandant, getInstitution(), getErbringungsart(), isIntensiv);
                setEntlassungdatum(combineDate(mv.getBwedt(), mv.getBwezp()));
                wards.add(bewegung);
                statBew = bewegung;
                entIndex = bewegung; // die letzte Verlegung ist auch immer die entlassende
                if (bewegung.getDauer() > dauer) {
                    dauer = bewegung.getDauer();
                    behIndex = bewegung;
                }
                LOG.log(Level.FINEST, "Füge Bewegung in Wards-Liste: {0}, {1}", new Object[]{bewegung.getSpecDescription(), bewegung.getSpecKey()});
                checkCreateDiagnosis(bewegung, bewegung.getSeqNo(), pDiagnosisList);
                checkCreateProcedure(bewegung, bewegung.getSeqNo(), pProcedureList);
                if (opBewegung != null) {
                    checkCreateDiagnosis(bewegung, opBewegung.getLfdnr(), pDiagnosisList);
                    checkCreateProcedure(bewegung, opBewegung.getLfdnr(), pProcedureList);
                    opBewegung = null;
                }
                setVwdIntensiv(getVwdIntensiv() + bewegung.getBeatmungsdauer());
            } else if ("6".equals(mv.getBewty())) { // Beginn Abwesenheit
                setUrlaub(getUrlaub() + getVWD(this, mv.getBwidt(), mv.getBwedt()));
            } else if (pSapConnection.sapProperties.getOpBewegungsartMap().get(mv.getBwart()) != null) {
                if (bewegung != null) {
                    checkCreateDiagnosis(bewegung, mv.getLfdnr(), pDiagnosisList);
                    checkCreateProcedure(bewegung, mv.getLfdnr(), pProcedureList);
                } else {
                    opBewegung = mv;
                }
            } else {
                if (statBew != null) {
                    checkCreateDiagnosis(statBew, mv.getLfdnr(), pDiagnosisList);
                    checkCreateProcedure(statBew, mv.getLfdnr(), pProcedureList);
                }
            }
        }
        appendUpdateMovementLessProcedures(pProcedureList, wards);
        int ws = wards.size();
        if (ws > 0) {
            if (aufIndex != null) {
                aufIndex.setAufnehmende("1");
            } else {
                LOG.log(Level.WARNING, "Keine aufnehmende FA für Fall {0}", getFallnr());
                bewegung = wards.get(0);
                bewegung.setAufnehmende("1");
            }
            if (behIndex != null) {
                behIndex.setBehandelnde("1");
            } else {
                LOG.log(Level.WARNING, "Keine behandelnde FA für Fall {0}", getFallnr());
                bewegung = wards.get(ws - 1);
                bewegung.setAufnehmende("1");
            }
            if (entIndex != null) {
                entIndex.setEntlassende("1");
            } else {
                LOG.log(Level.WARNING, "Keine entlassende FA für Fall {0}", getFallnr());
                bewegung = wards.get(ws - 1);
                bewegung.setEntlassende("1");
            }
        } else {
            LOG.log(Level.WARNING, "Keine Bewegung!");
        }
        setGeschlecht(getSex(pPatient.getSex()));
        if (pPatient.getDob() != null && getAufnahmedatum() != null) {
            long adD = getAufnahmedatum().getTime() / getMSEC_PER_DAY();
            long dobD = pPatient.getDob().getTime() / getMSEC_PER_DAY();
            if (adD >= dobD) {
                int diff = (int) (adD - dobD);
                if (diff >= (isLeap(getAufnahmedatum()) ? getDAYS_LEAP_YEAR() : getDAYS_NON_LEAP_YEAR())) {
                    setAlterInJahren(((diff + 1) * 4) / (3 * getDAYS_NON_LEAP_YEAR() + getDAYS_LEAP_YEAR()));
                    setAlterInTagen(0);
                } else {
                    setAlterInJahren(0);
                    setAlterInTagen(diff <= 0 ? 1 : diff);
                }
            }
        }

        //List<String> kain_messages = pSapConnection.getAllKainMessagesForCase(pSapConnection, institution, fallnr, insKOSTR);
        pKainKeys4Cases.add(getFallnr() + "_" + getInstitution() + "_" + getInsKOSTR());
        //kainMessages.addAll(pSapConnection.getKainMessagesForCase(getInstitution(), getFallnr(), getInsKOSTR(), getIkz()));

        setValid(true);
    }

    /**
     *
     * @return state
     */
    public int getVorschlag() {
        if (getState() == SapCase.getSTATE_LEADING()) {
            return 2;
        }
        if (getState() == SapCase.getSTATE_WK()) {
            return 3;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Status: " + String.valueOf(getState()) + "\n"
                + "Fallführend: " + (isIsLeading() ? "yes" : "no") + "\n"
                + "WK-Status: " + (isIsWK() ? "yes" : "no") + "\n"
                + "Fallnr führend: " + getFallnrLeading() + "\n"
                + "Institution: " + getInstitution() + "\n"
                + "IKZ WK: " + getIkz() + "\n"
                + "Fallnr WK: " + getFallnr() + "\n"
                + "Aufnahmegewicht: " + getAufnahmegewicht() + "\n"
                + "Respi: " + getBeatmungsDauer() + "\n"
                + getWKCasesText();
    }

    /**
     *
     * @param pEntgelt case fee
     */
    public void addEntgelt(final CaseEntg pEntgelt) {
        if (pEntgelt == null) {
            return;
        }
        fees.add(pEntgelt);
    }

//    /**
//     *
//     * @return has fees?
//     */
//    public boolean hasEntgelte() {
//        return fees != null;
//    }
    /**
     *
     * @param pSapConnection sap connection
     * @param pInstitution institution
     * @param pFallnr case number
     * @return discharge result
     * @throws JCoException exception from SAP
     */
    public SapDischargeSearchResult getEntlassung(final SapConnection pSapConnection, final String pInstitution, final String pFallnr) throws JCoException {
        SapDischargeSearchResult result = null;
        final String methodName = "BAPI_PATCASE_GETDISCHARGE";
        JCoFunction function = pSapConnection.createFunction(methodName);

        JCoParameterList parameter = function.getImportParameterList();
        parameter.setValue(pInstitution, "INSTITUTION");
        parameter.setValue(pFallnr, "PATCASEID");
        pSapConnection.callFunction(function);
        JCoTable returnStructure = function.getTableParameterList().getTable("RETURN");
        if (returnStructure.getNumRows() > 0
                && !(returnStructure.getString("TYPE").isEmpty()
                || "S".equals(returnStructure.getString("TYPE"))
                || "W".equals(returnStructure.getString("TYPE")))) {
            LOG.log(Level.WARNING, returnStructure.getString("MESSAGE"));
            logReturnResults(returnStructure);
        } else {
            JCoStructure discharge
                    = function.getExportParameterList().getStructure("INPAT_DISCHARGE_DATA");
            result = new SapDischargeSearchResult();
            result.readFromStructure(discharge);
        }
        return result;
    }

    /**
     *
     * @param pSapConnection sap connection
     * @param pInstitution institution
     * @param pFallnr case number
     * @return admission result
     * @throws JCoException exception from SAP
     */
    public SapAdmissionSearchResult getAufnahme(final SapConnection pSapConnection, final String pInstitution, final String pFallnr) throws JCoException {
        SapAdmissionSearchResult result = null;
        final String methodName = "BAPI_PATCASE_GETINPATADMISS";
        JCoFunction function = pSapConnection.createFunction(methodName);

        JCoParameterList parameter = function.getImportParameterList();
        parameter.setValue(pInstitution, "INSTITUTION");
        parameter.setValue(pFallnr, "PATCASEID");
        pSapConnection.callFunction(function);
        JCoTable returnStructure = function.getTableParameterList().getTable("RETURN");
        if (returnStructure.getNumRows() > 0
                && !(returnStructure.getString("TYPE").isEmpty()
                || "S".equals(returnStructure.getString("TYPE"))
                || "W".equals(returnStructure.getString("TYPE")))) {
            logReturnResults(returnStructure);
        } else {
            JCoStructure aufnahme
                    = function.getExportParameterList().getStructure("INPAT_ADMISS_DATA");
            result = new SapAdmissionSearchResult();
            result.readFromStructure(aufnahme);
        }
        return result;
    }

    /**
     *
     * @param pSapConnection sap connection
     * @param pSapArt sap type
     * @param pWkCase case
     * @return admission cause
     */
    public String getAufnahmeArt(final SapConnection pSapConnection, final String pSapArt, final SapCase pWkCase) {
        return pWkCase.getAufnahmeanlass();
    }

    /**
     *
     * @param pSapConnection sap connection
     * @param pSapArt sap type
     * @return admission type
     */
    public String getAufnahmeArt(final SapConnection pSapConnection, final String pSapArt) {
        return pSapConnection.sapProperties.getP301AufnahmeartMap().get(pSapArt);
    }

    /**
     *
     * @param pReturnStructure return structure(?)
     */
    protected void logReturnResults(final JCoTable pReturnStructure) {
        if (pReturnStructure != null) {
            SapReturnResult aResult = new SapReturnResult();
            pReturnStructure.firstRow();
            for (int i = 0, n = pReturnStructure.getNumRows(); i < n; i++) {
                aResult.readFromTable(pReturnStructure);
                pReturnStructure.nextRow();
                LOG.log(Level.WARNING, "Return Results !\n{0}", aResult);
            }
        }
    }

    /**
     * TO BE IMPLEMENTED!
     *
     * @param pKey Key
     * @param pAdmissionDate Admission Date TODO ist zu implementieren
     * @return is intensive ward?
     */
    public boolean isWardIntensiv(final String pKey, final Date pAdmissionDate) {
        return false;
    }

    /**
     * Ordnet die Diagnosen den Bewegungen zu
     *
     * @param pBewegung BewegungContainer
     * @param MOVEMENT_NO String
     * @param pDiagnosisList List
     */
    protected void checkCreateDiagnosis(final BewegungContainer pBewegung,
            final String MOVEMENT_NO,
            final List<SapDiagnosisSearchResult> pDiagnosisList) {
        SapDiagnosisSearchResult diag;
        for (int i = 0, n = pDiagnosisList.size(); i < n; i++) {
            diag = pDiagnosisList.get(i);
            if (diag.getMovemntSeqno().equals(MOVEMENT_NO)
                    && (diag.getCancelInd() == null || diag.getCancelInd().length() == 0)) {
                pBewegung.addDiag(new DiagnosenContainer(diag));
            }
        }
    }

    /**
     * Ordnet die PRozeduren den Bewegungen zu
     *
     * @param pBewegung BewegungContainer
     * @param MOVEMENT_NO String
     * @param pProcedureList List
     */
    protected void checkCreateProcedure(final BewegungContainer pBewegung,
            final String MOVEMENT_NO,
            final List<SapProcedureSearchResult> pProcedureList) {
        SapProcedureSearchResult proc;
        for (int i = 0, n = pProcedureList.size(); i < n; i++) {
            proc = pProcedureList.get(i);
            if (proc.getMovemntSeqno().equals(MOVEMENT_NO)
                    && (proc.getCancelInd() == null || proc.getCancelInd().length() == 0)) {
                pBewegung.addProc(new ProzedurenContainer(proc));
            }
        }
    }

    /**
     * Berechnet die Verweildauer
     *
     * @param pWkCase Case
     * @param pAnfang Date
     * @param pEnde Date
     * @return int
     */
    protected int getVWD(final SapCase pWkCase, final Date pAnfang, final Date pEnde) {
        GregorianCalendar tempCalendar = new GregorianCalendar(TimeZone.getTimeZone("ECT"));
        if (pAnfang != null && pEnde != null) {
            tempCalendar.setTime(pAnfang);
            tempCalendar.set(Calendar.HOUR_OF_DAY, 0);
            tempCalendar.set(Calendar.MINUTE, 0);
            tempCalendar.set(Calendar.SECOND, 0);
            tempCalendar.set(Calendar.MILLISECOND, 0);
            Date anfang = tempCalendar.getTime();
            tempCalendar.setTime(pEnde);
            tempCalendar.set(Calendar.HOUR_OF_DAY, 0);
            tempCalendar.set(Calendar.MINUTE, 0);
            tempCalendar.set(Calendar.SECOND, 0);
            tempCalendar.set(Calendar.MILLISECOND, 0);
            Date ende = tempCalendar.getTime();
            int e = (int) (ende.getTime() / (1000 * 60 * 60 * 24));
            int a = (int) (anfang.getTime() / (1000 * 60 * 60 * 24));
            long diff = (long) e - a;
            if (diff > 0) {
                if (!pWkCase.isIsWK() && !pWkCase.isIsLeading()) {
                    return (int) diff - 1;
                } else {
                    return (int) diff;
                }
            }
        }
        return 0;
    }

    /**
     * nicht zugeordnete Prozeduren werden den Bewegungen zu
     *
     * @param pProcedureList List
     * @param pBewegungen List
     */
    protected void appendUpdateMovementLessProcedures(final List<SapProcedureSearchResult> pProcedureList, final List<BewegungContainer> pBewegungen) {
        SapProcedureSearchResult proc;
        BewegungContainer bewegung;
        for (int i = 0, n = pProcedureList.size(); i < n; i++) {
            proc = pProcedureList.get(i);
            if ("00000".equals(proc.getMovemntSeqno())) {
                bewegung = sucheBewegung(proc.getDeptou(), combineDate(proc.getBegindate(), proc.getBegintime()), pBewegungen);
                if (bewegung != null) {
                    bewegung.addProc(new ProzedurenContainer(proc));
                } else {
                    bewegung = pBewegungen.get(0);
                    bewegung.addProc(new ProzedurenContainer(proc));
                }
            }
        }
    }

    /**
     * Ordnet eine Prozedur den Bewegungen anhand des Datums zu
     *
     * @param pDepartment String
     * @param pProcedureDate Date
     * @param pBewegungen List
     * @return BewegungContainer
     */
    protected BewegungContainer sucheBewegung(final String pDepartment, final Date pProcedureDate, final List<BewegungContainer> pBewegungen) {
        BewegungContainer result = null;
        BewegungContainer bewegung;
        if (pProcedureDate == null) {
            if (LOG != null) {
                LOG.log(Level.SEVERE, "Prozedurdatum ist null");
            }
            return pBewegungen.get(pBewegungen.size() - 1);
        }
        for (int i = 0, n = pBewegungen.size(); i < n; i++) {
            bewegung = pBewegungen.get(i);
            if ((bewegung.getStart() != null && pProcedureDate.equals(bewegung.getStart()))
                    || (bewegung.getEnde() != null && pProcedureDate.equals(bewegung.getEnde()))
                    || (bewegung.getStart() != null && bewegung.getEnde() != null
                    && (pProcedureDate.after(bewegung.getStart())
                    && pProcedureDate.before(bewegung.getEnde())))) {
                return bewegung;
            }
        }
        for (int i = pBewegungen.size() - 1; i >= 0; i--) {
            bewegung = pBewegungen.get(i);
            if (bewegung.getStart() != null && bewegung.getStart().before(pProcedureDate)) {
                return bewegung;
            }
        }
        for (int i = pBewegungen.size() - 1; i >= 0; i--) {
            bewegung = pBewegungen.get(i);
            if (bewegung.getAbteilung() != null && bewegung.getAbteilung().equals(pDepartment)) {
                return bewegung;
            }
        }
        return result;
    }

    /**
     * Prüft, ob es sich um ein Schaltjahr handelt.
     *
     * @param pAufnahmedatum Admission Date
     * @return is leap year?
     */
    protected boolean isLeap(final Date pAufnahmedatum) {
        Calendar c = Calendar.getInstance();
        int year;
        c.setTime(pAufnahmedatum);
        year = c.get(Calendar.YEAR);
        return ((year) % 4 == 0 && ((year) % 100 != 0 || (year) % 400 == 0));
    }

    /**
     * Muss eigentlich aus Einstellungsdatei geladen werden!
     *
     * @return
     */
    private boolean importAmbulantOP() {
        return false;
    }

    /**
     * Muss eigentlich aus Einstellungsdatei geladen werden!
     *
     * @return
     */
    private boolean importAmbulantCancels() {
        return false;
    }

    /**
     *
     * @return case key
     */
    public String getCaseKey() {
        return getInstitution() + "_" + getFallnr();
    }

    /**
     *
     * @param val value
     */
    public void addWkCase(final String val) {
        mWkCases.add(val);
    }

    /**
     *
     * @param refCase reference case
     */
    public void addRefCase(final ReferenzCase refCase) {
        if (refCase == null) {
            return;
        }
        mRefCases.add(refCase);
    }

    /**
     *
     * @return list of fees
     */
    public List<CaseEntg> getFees() {
        return new ArrayList<>(fees);
    }

    /**
     *
     * @return list of wards
     */
    public List<BewegungContainer> getWards() {
        return new ArrayList<>(wards);
    }

//    /**
//     *
//     * @return list of kain messages
//     */
//    public List<SapKainDetailSearchResult> getKainMessages() {
//        return getKain_messages() == null ? null : new ArrayList<>(getKain_messages());
//    }g
    /**
     * @return the stasp
     */
    public int getStasp() {
        return stasp;
    }

    /**
     * @param stasp the stasp to set
     */
    public void setStasp(String stasp) {
        this.stasp = stasp != null && stasp.equals("X")?1:0;
    }

    /**
     * @return the resid
     */
    public int getResid() {
        return resid;
    }

    /**
     * @param resid the resid to set
     */
    public void setResid(String resid) {
        this.resid = resid != null && resid.equals("X")?1:0;
    }

    /**
     * @return the forei
     */
    public int getForei() {
        return forei;
    }

    /**
     * @param forei the forei to set
     */
    public void setForei(String forei) {
        this.forei = forei != null && forei.equals("X")?1:0;
    }

    private void setCaseState(String pCaseState) {
        caseState  = pCaseState;
    }

    private void setBekat(String pBekat) {
       bekat = pBekat;
    }

    public String getBekat() {
        return bekat;
    }

    public String getCaseState() {
        return caseState;
    }

}
