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
public class SapKisStateSearchResult {

    private String mandt; // Mandant
    private String objnr; // Objektnummer
    private String stat; // Einzelstatus eines Objekts
    private int chgnr; // Änderungsnummer
    private String usnam; // Benutzername des Änderers im Änderungsbeleg
    private Date udate; // Erstellungsdatum des Änderungsbelegs
    private Date utime; // Uhrzeit der Änderung
    private String tcode; // Transaktionscode
    private String cdtcode; // Transaktion, in der eine Änderung durchgeführt wurde
    private String inact; // Kennzeichen: Status inaktiv
    private String chind; // Änderungskennzeichen
    private String dataaging; // Datenfilterwert für Data Aging

    /**
     *
     */
    public SapKisStateSearchResult() {
        super();
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
     * @return the objnr
     */
    public String getObjnr() {
        return objnr;
    }

    /**
     * @param objnr the objnr to set
     */
    public void setObjnr(String objnr) {
        this.objnr = objnr;
    }

    /**
     * @return the stat
     */
    public String getStat() {
        return stat;
    }

    /**
     * @param stat the stat to set
     */
    public void setStat(String stat) {
        this.stat = stat;
    }

    /**
     * @return the chgnr
     */
    public int getChgnr() {
        return chgnr;
    }

    /**
     * @param chgnr the chgnr to set
     */
    public void setChgnr(int chgnr) {
        this.chgnr = chgnr;
    }

    /**
     * @return the usnam
     */
    public String getUsnam() {
        return usnam;
    }

    /**
     * @param usnam the usnam to set
     */
    public void setUsnam(String usnam) {
        this.usnam = usnam;
    }

    /**
     * @return the udate
     */
    public Date getUdate() {
        return udate == null ? null : new Date(udate.getTime());
    }

    /**
     * @param udate the udate to set
     */
    public void setUdate(Date udate) {
        this.udate = udate == null ? null : new Date(udate.getTime());
    }

    /**
     * @return the utime
     */
    public Date getUtime() {
        return utime == null ? null : new Date(utime.getTime());
    }

    /**
     * @param utime the utime to set
     */
    public void setUtime(Date utime) {
        this.utime = utime == null ? null : new Date(utime.getTime());
    }

    /**
     * @return the tcode
     */
    public String getTcode() {
        return tcode;
    }

    /**
     * @param tcode the tcode to set
     */
    public void setTcode(String tcode) {
        this.tcode = tcode;
    }

    /**
     * @return the cdtcode
     */
    public String getCdtcode() {
        return cdtcode;
    }

    /**
     * @param cdtcode the cdtcode to set
     */
    public void setCdtcode(String cdtcode) {
        this.cdtcode = cdtcode;
    }

    /**
     * @return the inact
     */
    public String getInact() {
        return inact;
    }

    /**
     * @param inact the inact to set
     */
    public void setInact(String inact) {
        this.inact = inact;
    }

    /**
     * @return the chind
     */
    public String getChind() {
        return chind;
    }

    /**
     * @param chind the chind to set
     */
    public void setChind(String chind) {
        this.chind = chind;
    }

    /**
     * @return the dataaging
     */
    public String getDataaging() {
        return dataaging;
    }

    /**
     * @param dataaging the dataaging to set
     */
    public void setDataaging(String dataaging) {
        this.dataaging = dataaging;
    }

    /**
     *
     * @param aTable JCO table
     */
    public void readFromTable(final JCoTable aTable) {
        setMandt(aTable.getString(0)); //  Mandant
        setObjnr(aTable.getString(1)); // Objektnummer
        setStat(aTable.getString(2)); // Einzelstatus eines Objekts
        setChgnr(aTable.getInt(3)); // Änderungsnummer
        setUsnam(aTable.getString(4)); // Benutzername des Änderers im Änderungsbeleg
        setUdate(aTable.getDate(5)); // Erstellungsdatum des Änderungsbelegs
        setUtime(aTable.getDate(6)); // Uhrzeit der Änderung
        setTcode(aTable.getString(7)); // Transaktionscode
        setCdtcode(aTable.getString(8)); // Transaktion, in der eine Änderung durchgeführt wurde
        setInact(aTable.getString(9)); // Kennzeichen: Status inaktiv
        setChind(aTable.getString(10)); // Änderungskennzeichen

        if (aTable.getNumColumns() >= 12) {
            setDataaging(aTable.getString(11)); // Datenfilterwert für Data Aging
        }
    }

}
