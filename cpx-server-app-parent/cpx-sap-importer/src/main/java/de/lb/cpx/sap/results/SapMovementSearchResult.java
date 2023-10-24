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
package de.lb.cpx.sap.results;

import com.sap.conn.jco.JCoTable;
import java.util.Date;

/**
 *
 * @author niemeier
 */
public class SapMovementSearchResult {

    private String mandt = "";
    private String einri = "";
    private String falnr = "";
    private String lfdnr = "";
    private String bewty = "";
    private String bewtx = "";
    private String bwart = "";
    private String bwatx = "";
    private Date bwidt = null;
    private Date bwizt = null;
    private String planb = "";
    private String statu = "";
    private Date bwpdt = null;
    private Date bwpzt = null;
    private Date bwedt = null;
    private Date bwezp = null;
    private String plane = "";
    private String orgfa = "";
    private String orgkbfa = "";
    private String okurzfa = "";
    private String orgpf = "";
    private String orgkbpf = "";
    private String okurzpf = "";
    private String zimmr = "";
    private String zikurz = "";
    private String zimkb = "";
    private String bett = "";
    private String bekurz = "";
    private String bettkb = "";
    private String planr = "";
    private String dauer = "";
    private String orgau = "";
    private String bekat = "";
    private String storn = "";
    private String notkz = "";
    private String unfkz = "";
    private String unftx = "";
    private String unfnr = "";
    private String wlpri = "";
    private String prtxt = "";
    private String aufds = "";
    private String ezust = "";
    private String eztxt = "";
    private String arbun = "";
    private String telnr = "";

    private String bwgr1 = "";
    private String bwgr2 = "";

    /**
     *
     */
    public SapMovementSearchResult() {
    }

    /**
     * @return the mandt
     */
    public String getMandt() {
        return mandt;
    }

    /**
     * @param mandt the mandt to set
     */
    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    /**
     * @return the einri
     */
    public String getEinri() {
        return einri;
    }

    /**
     * @param einri the einri to set
     */
    public void setEinri(String einri) {
        this.einri = einri;
    }

    /**
     * @return the falnr
     */
    public String getFalnr() {
        return falnr;
    }

    /**
     * @param falnr the falnr to set
     */
    public void setFalnr(String falnr) {
        this.falnr = falnr;
    }

    /**
     * @return the lfdnr
     */
    public String getLfdnr() {
        return lfdnr;
    }

    /**
     * @param lfdnr the lfdnr to set
     */
    public void setLfdnr(String lfdnr) {
        this.lfdnr = lfdnr;
    }

    /**
     * @return the bewty
     */
    public String getBewty() {
        return bewty;
    }

    /**
     * @param bewty the bewty to set
     */
    public void setBewty(String bewty) {
        this.bewty = bewty;
    }

    /**
     * @return the bewtx
     */
    public String getBewtx() {
        return bewtx;
    }

    /**
     * @param bewtx the bewtx to set
     */
    public void setBewtx(String bewtx) {
        this.bewtx = bewtx;
    }

    /**
     * @return the bwart
     */
    public String getBwart() {
        return bwart;
    }

    /**
     * @param bwart the bwart to set
     */
    public void setBwart(String bwart) {
        this.bwart = bwart;
    }

    /**
     * @return the bwatx
     */
    public String getBwatx() {
        return bwatx;
    }

    /**
     * @param bwatx the bwatx to set
     */
    public void setBwatx(String bwatx) {
        this.bwatx = bwatx;
    }

    /**
     * @return the bwidt
     */
    public Date getBwidt() {
        return bwidt == null ? null : new Date(bwidt.getTime());
    }

    /**
     * @param bwidt the bwidt to set
     */
    public void setBwidt(Date bwidt) {
        this.bwidt = bwidt == null ? null : new Date(bwidt.getTime());
    }

    /**
     * @return the bwizt
     */
    public Date getBwizt() {
        return bwizt == null ? null : new Date(bwizt.getTime());
    }

    /**
     * @param bwizt the bwizt to set
     */
    public void setBwizt(Date bwizt) {
        this.bwizt = bwizt == null ? null : new Date(bwizt.getTime());
    }

    /**
     * @return the planb
     */
    public String getPlanb() {
        return planb;
    }

    /**
     * @param planb the planb to set
     */
    public void setPlanb(String planb) {
        this.planb = planb;
    }

    /**
     * @return the statu
     */
    public String getStatu() {
        return statu;
    }

    /**
     * @param statu the statu to set
     */
    public void setStatu(String statu) {
        this.statu = statu;
    }

    /**
     * @return the bwpdt
     */
    public Date getBwpdt() {
        return bwpdt == null ? null : new Date(bwpdt.getTime());
    }

    /**
     * @param bwpdt the bwpdt to set
     */
    public void setBwpdt(Date bwpdt) {
        this.bwpdt = bwpdt == null ? null : new Date(bwpdt.getTime());
    }

    /**
     * @return the bwpzt
     */
    public Date getBwpzt() {
        return bwpzt == null ? null : new Date(bwpzt.getTime());
    }

    /**
     * @param bwpzt the bwpzt to set
     */
    public void setBwpzt(Date bwpzt) {
        this.bwpzt = bwpzt == null ? null : new Date(bwpzt.getTime());
    }

    /**
     * @return the bwedt
     */
    public Date getBwedt() {
        return bwedt == null ? null : new Date(bwedt.getTime());
    }

    /**
     * @param bwedt the bwedt to set
     */
    public void setBwedt(Date bwedt) {
        this.bwedt = bwedt == null ? null : new Date(bwedt.getTime());
    }

    /**
     * @return the bwezp
     */
    public Date getBwezp() {
        return bwezp == null ? null : new Date(bwezp.getTime());
    }

    /**
     * @param bwezp the bwezp to set
     */
    public void setBwezp(Date bwezp) {
        this.bwezp = bwezp == null ? null : new Date(bwezp.getTime());
    }

    /**
     * @return the plane
     */
    public String getPlane() {
        return plane;
    }

    /**
     * @param plane the plane to set
     */
    public void setPlane(String plane) {
        this.plane = plane;
    }

    /**
     * @return the orgfa
     */
    public String getOrgfa() {
        return orgfa;
    }

    /**
     * @param orgfa the orgfa to set
     */
    public void setOrgfa(String orgfa) {
        this.orgfa = orgfa;
    }

    /**
     * @return the orgkbfa
     */
    public String getOrgkbfa() {
        return orgkbfa;
    }

    /**
     * @param orgkbfa the orgkbfa to set
     */
    public void setOrgkbfa(String orgkbfa) {
        this.orgkbfa = orgkbfa;
    }

    /**
     * @return the okurzfa
     */
    public String getOkurzfa() {
        return okurzfa;
    }

    /**
     * @param okurzfa the okurzfa to set
     */
    public void setOkurzfa(String okurzfa) {
        this.okurzfa = okurzfa;
    }

    /**
     * @return the orgpf
     */
    public String getOrgpf() {
        return orgpf;
    }

    /**
     * @param orgpf the orgpf to set
     */
    public void setOrgpf(String orgpf) {
        this.orgpf = orgpf;
    }

    /**
     * @return the orgkbpf
     */
    public String getOrgkbpf() {
        return orgkbpf;
    }

    /**
     * @param orgkbpf the orgkbpf to set
     */
    public void setOrgkbpf(String orgkbpf) {
        this.orgkbpf = orgkbpf;
    }

    /**
     * @return the okurzpf
     */
    public String getOkurzpf() {
        return okurzpf;
    }

    /**
     * @param okurzpf the okurzpf to set
     */
    public void setOkurzpf(String okurzpf) {
        this.okurzpf = okurzpf;
    }

    /**
     * @return the zimmr
     */
    public String getZimmr() {
        return zimmr;
    }

    /**
     * @param zimmr the zimmr to set
     */
    public void setZimmr(String zimmr) {
        this.zimmr = zimmr;
    }

    /**
     * @return the zikurz
     */
    public String getZikurz() {
        return zikurz;
    }

    /**
     * @param zikurz the zikurz to set
     */
    public void setZikurz(String zikurz) {
        this.zikurz = zikurz;
    }

    /**
     * @return the zimkb
     */
    public String getZimkb() {
        return zimkb;
    }

    /**
     * @param zimkb the zimkb to set
     */
    public void setZimkb(String zimkb) {
        this.zimkb = zimkb;
    }

    /**
     * @return the bett
     */
    public String getBett() {
        return bett;
    }

    /**
     * @param bett the bett to set
     */
    public void setBett(String bett) {
        this.bett = bett;
    }

    /**
     * @return the bekurz
     */
    public String getBekurz() {
        return bekurz;
    }

    /**
     * @param bekurz the bekurz to set
     */
    public void setBekurz(String bekurz) {
        this.bekurz = bekurz;
    }

    /**
     * @return the bettkb
     */
    public String getBettkb() {
        return bettkb;
    }

    /**
     * @param bettkb the bettkb to set
     */
    public void setBettkb(String bettkb) {
        this.bettkb = bettkb;
    }

    /**
     * @return the planr
     */
    public String getPlanr() {
        return planr;
    }

    /**
     * @param planr the planr to set
     */
    public void setPlanr(String planr) {
        this.planr = planr;
    }

    /**
     * @return the dauer
     */
    public String getDauer() {
        return dauer;
    }

    /**
     * @param dauer the dauer to set
     */
    public void setDauer(String dauer) {
        this.dauer = dauer;
    }

    /**
     * @return the orgau
     */
    public String getOrgau() {
        return orgau;
    }

    /**
     * @param orgau the orgau to set
     */
    public void setOrgau(String orgau) {
        this.orgau = orgau;
    }

    /**
     * @return the bekat
     */
    public String getBekat() {
        return bekat;
    }

    /**
     * @param bekat the bekat to set
     */
    public void setBekat(String bekat) {
        this.bekat = bekat;
    }

    /**
     * @return the storn
     */
    public String getStorn() {
        return storn;
    }

    /**
     * @param storn the storn to set
     */
    public void setStorn(String storn) {
        this.storn = storn;
    }

    /**
     * @return the notkz
     */
    public String getNotkz() {
        return notkz;
    }

    /**
     * @param notkz the notkz to set
     */
    public void setNotkz(String notkz) {
        this.notkz = notkz;
    }

    /**
     * @return the unfkz
     */
    public String getUnfkz() {
        return unfkz;
    }

    /**
     * @param unfkz the unfkz to set
     */
    public void setUnfkz(String unfkz) {
        this.unfkz = unfkz;
    }

    /**
     * @return the unftx
     */
    public String getUnftx() {
        return unftx;
    }

    /**
     * @param unftx the unftx to set
     */
    public void setUnftx(String unftx) {
        this.unftx = unftx;
    }

    /**
     * @return the unfnr
     */
    public String getUnfnr() {
        return unfnr;
    }

    /**
     * @param unfnr the unfnr to set
     */
    public void setUnfnr(String unfnr) {
        this.unfnr = unfnr;
    }

    /**
     * @return the wlpri
     */
    public String getWlpri() {
        return wlpri;
    }

    /**
     * @param wlpri the wlpri to set
     */
    public void setWlpri(String wlpri) {
        this.wlpri = wlpri;
    }

    /**
     * @return the prtxt
     */
    public String getPrtxt() {
        return prtxt;
    }

    /**
     * @param prtxt the prtxt to set
     */
    public void setPrtxt(String prtxt) {
        this.prtxt = prtxt;
    }

    /**
     * @return the aufds
     */
    public String getAufds() {
        return aufds;
    }

    /**
     * @param aufds the aufds to set
     */
    public void setAufds(String aufds) {
        this.aufds = aufds;
    }

    /**
     * @return the ezust
     */
    public String getEzust() {
        return ezust;
    }

    /**
     * @param ezust the ezust to set
     */
    public void setEzust(String ezust) {
        this.ezust = ezust;
    }

    /**
     * @return the eztxt
     */
    public String getEztxt() {
        return eztxt;
    }

    /**
     * @param eztxt the eztxt to set
     */
    public void setEztxt(String eztxt) {
        this.eztxt = eztxt;
    }

    /**
     * @return the arbun
     */
    public String getArbun() {
        return arbun;
    }

    /**
     * @param arbun the arbun to set
     */
    public void setArbun(String arbun) {
        this.arbun = arbun;
    }

    /**
     * @return the telnr
     */
    public String getTelnr() {
        return telnr;
    }

    /**
     * @param telnr the telnr to set
     */
    public void setTelnr(String telnr) {
        this.telnr = telnr;
    }

    /**
     * @return the bwgr1
     */
    public String getBwgr1() {
        return bwgr1;
    }

    /**
     * @param bwgr1 the bwgr1 to set
     */
    public void setBwgr1(String bwgr1) {
        this.bwgr1 = bwgr1;
    }

    /**
     * @return the bwgr2
     */
    public String getBwgr2() {
        return bwgr2;
    }

    /**
     * @param bwgr2 the bwgr2 to set
     */
    public void setBwgr2(String bwgr2) {
        this.bwgr2 = bwgr2;
    }

    /**
     *
     * @param aTable JCO table
     */
    public void readFromTable(JCoTable aTable) {
        setMandt(aTable.getString(0));
        setEinri(aTable.getString(1));
        setFalnr(aTable.getString(2));
        setLfdnr(aTable.getString(3));
        setBewty(aTable.getString(4).trim());
        setBewtx(aTable.getString(5));
        setBwart(aTable.getString(6));
        setBwatx(aTable.getString(7));
        setBwidt(aTable.getDate(8));
        setBwizt(aTable.getTime(9));
        setPlanb(aTable.getString(10));
        setStatu(aTable.getString(11));
        setBwpdt(aTable.getDate(12));
        setBwpzt(aTable.getTime(13));
        setBwedt(aTable.getDate(14));
        setBwezp(aTable.getTime(15));
        setPlane(aTable.getString(16));
        setOrgfa(aTable.getString(17));
        setOrgkbfa(aTable.getString(18));
        setOkurzfa(aTable.getString(19));
        setOrgpf(aTable.getString(20));
        setOrgkbpf(aTable.getString(21));
        setOkurzpf(aTable.getString(22));
        setZimmr(aTable.getString(23));
        setZikurz(aTable.getString(24));
        setZimkb(aTable.getString(25));
        setBett(aTable.getString(26));
        setBekurz(aTable.getString(27));
        setBettkb(aTable.getString(28));
        setPlanr(aTable.getString(29));
        setDauer(aTable.getString(30));
        setOrgau(aTable.getString(31));
        setBekat(aTable.getString(32));
        setStorn(aTable.getString(33).trim());
        setNotkz(aTable.getString(34));
        setUnfkz(aTable.getString(35));
        setUnftx(aTable.getString(36));
        setUnfnr(aTable.getString(37));
        setWlpri(aTable.getString(38));
        setPrtxt(aTable.getString(39));
        setAufds(aTable.getString(40));
        setEzust(aTable.getString(41));
        setEztxt(aTable.getString(42));
        setArbun(aTable.getString(43));
        setTelnr(aTable.getString(44));
    }

    /**
     *
     * @param aTable JCO table
     */
    public void readFromStructurTable(final JCoTable aTable) {
        setMandt(aTable.getString(0));
        setEinri(aTable.getString(1));
        setFalnr(aTable.getString(2));
        setLfdnr(aTable.getString(3));
        setBewty(aTable.getString(4).trim());
        setBewtx("");                                           //nv, nb
        setBwart(aTable.getString(5));
        setBwatx("");                                           //nv, nb
        setBwidt(aTable.getDate(6));
        setBwizt(aTable.getTime(7));
        setPlanb(aTable.getString(8));
        setStatu(aTable.getString(9));
        setBwpdt(aTable.getDate(10));
        setBwpzt(aTable.getTime(11));
        setBwedt(aTable.getDate(12));
        setBwezp(aTable.getTime(13));
        setPlane(aTable.getString(14));
        setOrgfa(aTable.getString(30));
        setOrgkbfa(aTable.getString("FACHR"));                                           //nv, nb
        setOkurzfa("");                                           //nv, nb
        setOrgpf(aTable.getString(31));
        setOrgkbpf("");                                           //nv, nb
        setOkurzpf("");                                           //nv, nb
        setZimmr(aTable.getString(32));                           //nv, nb
        setZikurz("");                                            //nv, nb
        setZimkb("");                                            //nv, nb
        setBett(aTable.getString(33));
        setBekurz("");                                           //nv, nb
        setBettkb("");                                           //nv, nb
        setPlanr(aTable.getString(34));
        setDauer(aTable.getString(35));
        setOrgau(aTable.getString(37));
        setBekat(aTable.getString(50));
        setStorn(aTable.getString(46).trim());
        setNotkz(aTable.getString(20));
        setUnfkz(aTable.getString(21));
        setUnftx(aTable.getString(26));
        setUnfnr(aTable.getString(22));
        setWlpri(aTable.getString(28));
        setPrtxt("");                                           //nv, nb
        setAufds(aTable.getString(29));
        setEzust("");                                           //nv, nb
        setArbun(aTable.getString(40));
        setTelnr("");                                           //nv, nb

        setBwgr1(aTable.getString(60));
        setBwgr2(aTable.getString(61));
    }

    @Override
    public String toString() {
        return "Mandant >" + getMandt() + "<\n"
                + "Einrichtung >" + getEinri() + "<\n"
                + "Fallnummer >" + getFalnr() + "<\n"
                + "Laufende Nummer einer Bewegung >" + getLfdnr() + "<\n"
                + "Bewegungstyp >" + getBewty() + "<\n"
                + "Bewegungstypentext >" + getBewtx() + "<\n"
                + "Bewegungsart >" + getBwart() + "<\n"
                + "Bewegungsartentext >" + getBwatx() + "<\n"
                + "Datum der Bewegung >" + getBwidt() + "<\n"
                + "Uhrzeit der Bewegung >" + getBwizt() + "<\n"
                + "Plankennzeichen Datum der Bewegung >" + getPlanb() + "<\n"
                + "Interner Status eines ambulanten Besuchs >" + getStatu() + "<\n"
                + "Plan-Datum der Bewegung >" + getBwpdt() + "<\n"
                + "Plan-Zeit der Bewegung >" + getBwpzt() + "<\n"
                + "Bewegungsendedatum, Beginndatum der Folgebewegung >" + getBwedt() + "<\n"
                + "Bewegungsendezeit, Beginnzeit der Folgebewegung >" + getBwezp() + "<\n"
                + "Plankennzeichen des Endedatums einer Bewegung >" + getPlane() + "<\n"
                + "OrgEinheit, die einem Fall fachl. zugewiesen wird >" + getOrgfa() + "<\n"
                + "OrgEinheit, die einem Fall pfleg. zugewiesen wird >" + getOrgpf() + "<\n"
                + "Kurzbezeichnung einer Organisationseinheit >" + getOrgkbpf() + "<\n"
                + "Kürzel einer Organisationseinheit in der Anwendung >" + getOkurzpf() + "<\n"
                + "BauId eines Zimmers >" + getZimmr() + "<\n"
                + "Kürzel einer baulichen Einheit >" + getZikurz() + "<\n"
                + "Kurzbezeichnung einer baulichen Einheit >" + getZimkb() + "<\n"
                + "Bauid eines Bettenstellplatzes >" + getBett() + "<\n"
                + "Kürzel einer baulichen Einheit >" + getBekurz() + "<\n"
                + "Kurzbezeichnung einer baulichen Einheit >" + getBettkb() + "<\n"
                + "Plankennzeichen für räumliche Zuweisung >" + getPlanr() + "<\n"
                + "Vorraussichtl. Aufenthalts-bzw. Behandlungsdauer >" + getDauer() + "<\n"
                + "OrgId der Aufnahmestelle >" + getOrgau() + "<\n"
                + "Behandlungskategorie >" + getBekat() + "<\n"
                + "Stornokennzeichen >" + getStorn() + "<\n"
                + "Notfallkennzeichen >" + getNotkz() + "<\n"
                + "Unfallart >" + getUnfkz() + "<\n"
                + "Unfallartentext >" + getUnftx() + "<\n"
                + "Aktenzeichen / Bearbeitungsnummer für Unfall >" + getUnfnr() + "<\n"
                + "Priorität für die Vormerkungs-/Wartelistenverwaltung >" + getWlpri() + "<\n"
                + "Text zur Priorität >" + getPrtxt() + "<\n"
                + "spätestes Aufnahmedatum >" + getAufds() + "<\n"
                + "Entlassungszustand >" + getEzust() + "<\n"
                + "Entlassungszustandstext >" + getEztxt() + "<\n"
                + "Arbeitsunfähigkeits-Bis-Datum >" + getArbun() + "<\n"
                + "Telefonnummer d. Patienten in der Einrichtung >" + getTelnr() + "<"
                + "Bewegungsgrund 1. und 2.Stelle >" + getBwgr1() + "<"
                + "Bewegungsgrund 3. und 4.Stelle >" + getBwgr2() + "<";
    }

}
