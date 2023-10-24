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

/**
 *
 * @author niemeier
 */
public class Insurance {

    private String einri;
    private String falnr;
    private String lfdnr;
    private String kostr;
    private String rangf;
    private String vernr;
    private String vbgdt;
    private String verab;
    private String verbi;
    private String instituteInd;
    private String lastNameBp;

    @Override
    public String toString() {
        return "\tEinrichtung : " + getEinri() + "\n"
                + "\tFallnummer : " + getFalnr() + "\n"
                + "\tLFDNR : " + getLfdnr() + "\n"
                + "\tKostenträger : " + getKostr() + "\n"
                + "\tRangfolge : " + getRangf() + "\n"
                + "\tVersicherungnr : " + getVernr() + "\n"
                + "\tVBGDT : " + getVbgdt() + "\n"
                + "\tVerAB : " + getVerab() + "\n"
                + "\tVerBI : " + getVerbi() + "\n"
                + "\tInstitute : " + getInstituteInd() + "\n"
                + "\tLast Name : " + getLastNameBp() + "\n";
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
     * @return the kostr
     */
    public String getKostr() {
        return kostr;
    }

    /**
     * @param kostr the kostr to set
     */
    public void setKostr(String kostr) {
        this.kostr = kostr;
    }

    /**
     * @return the rangf
     */
    public String getRangf() {
        return rangf;
    }

    /**
     * @param rangf the rangf to set
     */
    public void setRangf(String rangf) {
        this.rangf = rangf;
    }

    /**
     * @return the vernr
     */
    public String getVernr() {
        return vernr;
    }

    /**
     * @param vernr the vernr to set
     */
    public void setVernr(String vernr) {
        this.vernr = vernr;
    }

    /**
     * @return the vbgdt
     */
    public String getVbgdt() {
        return vbgdt;
    }

    /**
     * @param vbgdt the vbgdt to set
     */
    public void setVbgdt(String vbgdt) {
        this.vbgdt = vbgdt;
    }

    /**
     * @return the verab
     */
    public String getVerab() {
        return verab;
    }

    /**
     * @param verab the verab to set
     */
    public void setVerab(String verab) {
        this.verab = verab;
    }

    /**
     * @return the verbi
     */
    public String getVerbi() {
        return verbi;
    }

    /**
     * @param verbi the verbi to set
     */
    public void setVerbi(String verbi) {
        this.verbi = verbi;
    }

    /**
     * @return the instituteInd
     */
    public String getInstituteInd() {
        return instituteInd;
    }

    /**
     * @param instituteInd the instituteInd to set
     */
    public void setInstituteInd(String instituteInd) {
        this.instituteInd = instituteInd;
    }

    /**
     * @return the lastNameBp
     */
    public String getLastNameBp() {
        return lastNameBp;
    }

    /**
     * @param lastNameBp the lastNameBp to set
     */
    public void setLastNameBp(String lastNameBp) {
        this.lastNameBp = lastNameBp;
    }

}
