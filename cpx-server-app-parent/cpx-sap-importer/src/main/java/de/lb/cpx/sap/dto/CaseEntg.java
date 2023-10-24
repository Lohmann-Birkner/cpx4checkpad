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
public class CaseEntg {

    private String caseNr;
    private String entga;
    private String betrag;
    private String anzahl;

    /**
     *
     * @param pFallnr case number
     */
    public CaseEntg(final String pFallnr) {
        caseNr = pFallnr;
    }

    /**
     * @return the caseNr
     */
    public String getCaseNr() {
        return caseNr;
    }

    /**
     * @param caseNr the caseNr to set
     */
    public void setCaseNr(String caseNr) {
        this.caseNr = caseNr;
    }

    /**
     * @return the entga
     */
    public String getEntga() {
        return entga;
    }

    /**
     * @param entga the entga to set
     */
    public void setEntga(String entga) {
        this.entga = entga;
    }

    /**
     * @return the betrag
     */
    public String getBetrag() {
        return betrag;
    }

    /**
     * @param betrag the betrag to set
     */
    public void setBetrag(String betrag) {
        this.betrag = betrag;
    }

    /**
     * @return the anzahl
     */
    public String getAnzahl() {
        return anzahl;
    }

    /**
     * @param anzahl the anzahl to set
     */
    public void setAnzahl(String anzahl) {
        this.anzahl = anzahl;
    }

    @Override
    public String toString() {
        return "Fallnummer: " + getCaseNr() + "\n"
                + "Schluessel: " + getEntga() + "\n"
                + "Betrage " + getBetrag() + "\n"
                + "Anzahl: " + getAnzahl();
    }
}
