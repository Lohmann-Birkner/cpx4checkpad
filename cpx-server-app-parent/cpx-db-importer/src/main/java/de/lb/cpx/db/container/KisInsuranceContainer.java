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

import java.io.Serializable;

/**
 * <p>
 * Überschrift: Checkpoint DRG</p>
 *
 * <p>
 * Beschreibung: Fallmanagement DRG</p>
 *
 * <p>
 * Copyright: </p>
 *
 * <p>
 * Organisation: </p>
 *
 * @author unbekannt
 * @version 2.0
 */
public class KisInsuranceContainer implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String ppvepsEpsnr;
    private final String ppvbztBehnr;
    private final String ppvpatPatid;
    private final String ppvpatPatnr;
    private String mitglNr;
    private String kasseNr;
    private final String ppvrepLfdnr;
    private final String pstpvkPverskreis;
    private final String pstmitArt;
    private final String pstremIdent;
    private final String ppvrepHre;
    private final String ppvrepAv;

    public KisInsuranceContainer(String pPpvepsEpsnr, String pPpvbztBehnr, String pPpvpatPatid,
            String pPpvpatPatnr, String pMitglNr, String pKasseNr, String pPpvrepLfdnr,
            String pPstpvkPverskreis, String pPstmitArt, String pPstremIdent, String pPpvrepHre, String pPpvrepAv) {
        this.ppvepsEpsnr = pPpvepsEpsnr;
        this.ppvbztBehnr = pPpvbztBehnr;
        this.ppvpatPatid = pPpvpatPatid;
        this.ppvpatPatnr = pPpvpatPatnr;
        this.mitglNr = pMitglNr;
        this.kasseNr = pKasseNr;
        this.ppvrepLfdnr = pPpvrepLfdnr;
        this.pstpvkPverskreis = pPstpvkPverskreis;
        this.pstmitArt = pPstmitArt;
        this.pstremIdent = pPstremIdent;
        this.ppvrepHre = pPpvrepHre;
        this.ppvrepAv = pPpvrepAv;
    }

    /**
     * @return the ppvepsEpsnr
     */
    public String getPpvepsEpsnr() {
        return ppvepsEpsnr;
    }

    /**
     * @return the ppvbztBehnr
     */
    public String getPpvbztBehnr() {
        return ppvbztBehnr;
    }

    /**
     * @return the ppvpatPatid
     */
    public String getPpvpatPatid() {
        return ppvpatPatid;
    }

    /**
     * @return the ppvpatPatnr
     */
    public String getPpvpatPatnr() {
        return ppvpatPatnr;
    }

    /**
     * @return the mitglNr
     */
    public String getMitglNr() {
        return mitglNr;
    }

    /**
     * @return the kasseNr
     */
    public String getKasseNr() {
        return kasseNr;
    }

    /**
     * @return the ppvrepLfdnr
     */
    public String getPpvrepLfdnr() {
        return ppvrepLfdnr;
    }

    /**
     * @return the pstpvkPverskreis
     */
    public String getPstpvkPverskreis() {
        return pstpvkPverskreis;
    }

    /**
     * @return the pstmitArt
     */
    public String getPstmitArt() {
        return pstmitArt;
    }

    /**
     * @return the pstremIdent
     */
    public String getPstremIdent() {
        return pstremIdent;
    }

    /**
     * @return the ppvrepHre
     */
    public String getPpvrepHre() {
        return ppvrepHre;
    }

    /**
     * @return the ppvrepAv
     */
    public String getPpvrepAv() {
        return ppvrepAv;
    }

    /**
     * @param mitglNr the mitglNr to set
     */
    public void setMitglNr(String mitglNr) {
        this.mitglNr = mitglNr;
    }

    /**
     * @param kasseNr the kasseNr to set
     */
    public void setKasseNr(String kasseNr) {
        this.kasseNr = kasseNr;
    }

}
