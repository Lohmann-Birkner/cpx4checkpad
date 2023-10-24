/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.db.container;

//import de.lb.cpx.db.importer.utils.Constants;
//import dto.impl.Department;
//import java.util.Date;
//
//public class KisBewegungContainer {
//
//    private java.util.Date anfang;
//    private java.util.Date ende;
//    private String abtId = "";
//    private String abteilung301 = "0000";
//    private String abteilung = "<" + Constants.CHECKRESULT_TYP_UNKNOWN + ">"; //AppResources.getResource(AppResourceBundle.CHECKRESULT_TYP_UNKNOW)
//    private String standort = "";
//    private String station = "";
//    private byte aufnehmende = 1;
//    private byte behandelnde = 1;
//    private byte entlassende = 1;
//    private String typ = "HA";
//    private byte typschluessel = 1;
//    private byte isDefault = 1;
//    private int nummer = 1;
//    private int id = -1;
//    private Integer tarifId = null;
//    private int vwdInt = 0;
//    private double vwdIntDouble = 0;
//    private String kennungIb = "";
//    private long depNr;
//
//    public KisBewegungContainer() {
//    }
//
//    public void addVwdIntensiv(int vwd) {
//        setVwdInt(getVwdInt() + vwd);
//    }
//
//    /**
//     * addVWDIntensiv
//     *
//     * @param vwd length of stay
//     */
//    public void addVwdIntensiv(double vwd) {
//        setVwdIntDouble(getVwdIntDouble() + vwd);
//    }
//
//    /**
//     * @return the anfang
//     */
//    public Date getAnfang() {
//        return anfang == null ? null : new Date(anfang.getTime());
//    }
//
//    /**
//     * @return the ende
//     */
//    public Date getEnde() {
//        return ende == null ? null : new Date(ende.getTime());
//    }
//
//    /**
//     * @return the abtId
//     */
//    public String getAbtId() {
//        return abtId;
//    }
//
//    /**
//     * @return the abteilung301
//     */
//    public String getAbteilung301() {
//        return abteilung301;
//    }
//
//    /**
//     * @return the abteilung
//     */
//    public String getAbteilung() {
//        return abteilung;
//    }
//
//    /**
//     * @return the standort
//     */
//    public String getStandort() {
//        return standort;
//    }
//
//    /**
//     * @return the station
//     */
//    public String getStation() {
//        return station;
//    }
//
//    /**
//     * @return the aufnehmende
//     */
//    public byte getAufnehmende() {
//        return aufnehmende;
//    }
//
//    /**
//     * @return the behandelnde
//     */
//    public byte getBehandelnde() {
//        return behandelnde;
//    }
//
//    /**
//     * @return the entlassende
//     */
//    public byte getEntlassende() {
//        return entlassende;
//    }
//
//    /**
//     * @return the typ
//     */
//    public String getTyp() {
//        return typ;
//    }
//
//    /**
//     * @return the typschluessel
//     */
//    public byte getTypschluessel() {
//        return typschluessel;
//    }
//
//    /**
//     * @return the isDefault
//     */
//    public byte getIsDefault() {
//        return isDefault;
//    }
//
//    /**
//     * @return the nummer
//     */
//    public int getNummer() {
//        return nummer;
//    }
//
//    /**
//     * @return the id
//     */
//    public int getId() {
//        return id;
//    }
//
//    /**
//     * @return the tarifId
//     */
//    public Integer getTarifId() {
//        return tarifId;
//    }
//
//    /**
//     * @return the vwdInt
//     */
//    public int getVwdInt() {
//        return vwdInt;
//    }
//
//    /**
//     * @return the vwdIntDouble
//     */
//    public double getVwdIntDouble() {
//        return vwdIntDouble;
//    }
//
//    /**
//     * @return the kennungIb
//     */
//    public String getKennungIb() {
//        return kennungIb;
//    }
//
//    /**
//     * @param anfang the anfang to set
//     */
//    public void setAnfang(Date anfang) {
//        this.anfang = anfang == null ? null : new Date(anfang.getTime());
//    }
//
//    /**
//     * @param ende the ende to set
//     */
//    public void setEnde(Date ende) {
//        this.ende = ende == null ? null : new Date(ende.getTime());
//    }
//
//    /**
//     * @param abtId the abtId to set
//     */
//    public void setAbtId(String abtId) {
//        this.abtId = abtId;
//    }
//
//    /**
//     * @param abteilung301 the abteilung301 to set
//     */
//    public void setAbteilung301(String abteilung301) {
//        this.abteilung301 = abteilung301;
//    }
//
//    /**
//     * @param abteilung the abteilung to set
//     */
//    public void setAbteilung(String abteilung) {
//        this.abteilung = abteilung;
//    }
//
//    /**
//     * @param standort the standort to set
//     */
//    public void setStandort(String standort) {
//        this.standort = standort;
//    }
//
//    /**
//     * @param station the station to set
//     */
//    public void setStation(String station) {
//        this.station = station;
//    }
//
//    /**
//     * @param aufnehmende the aufnehmende to set
//     */
//    public void setAufnehmende(byte aufnehmende) {
//        this.aufnehmende = aufnehmende;
//    }
//
//    /**
//     * @param behandelnde the behandelnde to set
//     */
//    public void setBehandelnde(byte behandelnde) {
//        this.behandelnde = behandelnde;
//    }
//
//    /**
//     * @param entlassende the entlassende to set
//     */
//    public void setEntlassende(byte entlassende) {
//        this.entlassende = entlassende;
//    }
//
//    /**
//     * @param typ the typ to set
//     */
//    public void setTyp(String typ) {
//        this.typ = typ;
//    }
//
//    /**
//     * @param typschluessel the typschluessel to set
//     */
//    public void setTypschluessel(byte typschluessel) {
//        this.typschluessel = typschluessel;
//    }
//
//    /**
//     * @param isDefault the isDefault to set
//     */
//    public void setIsDefault(byte isDefault) {
//        this.isDefault = isDefault;
//    }
//
//    /**
//     * @param nummer the nummer to set
//     */
//    public void setNummer(int nummer) {
//        this.nummer = nummer;
//    }
//
//    /**
//     * @param id the id to set
//     */
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    /**
//     * @param tarifId the tarifId to set
//     */
//    public void setTarifId(Integer tarifId) {
//        this.tarifId = tarifId;
//    }
//
//    /**
//     * @param vwdInt the vwdInt to set
//     */
//    public void setVwdInt(int vwdInt) {
//        this.vwdInt = vwdInt;
//    }
//
//    /**
//     * @param vwdIntDouble the vwdIntDouble to set
//     */
//    public void setVwdIntDouble(double vwdIntDouble) {
//        this.vwdIntDouble = vwdIntDouble;
//    }
//
//    /**
//     * @param kennungIb the kennungIb to set
//     */
//    public void setKennungIb(String kennungIb) {
//        this.kennungIb = kennungIb;
//    }
//
//    /**
//     * @return the depNr
//     */
//    public long getDepNr() {
//        return depNr;
//    }
//
//    /**
//     * @param depNr the depNr to set
//     */
//    public void setDepNr(long depNr) {
//        this.depNr = depNr;
//    }
//
//}
