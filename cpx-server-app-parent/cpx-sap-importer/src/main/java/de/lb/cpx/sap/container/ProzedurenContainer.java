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
package de.lb.cpx.sap.container;

import static de.lb.cpx.sap.importer.utils.SapStrUtils.*;
import de.lb.cpx.sap.results.SapProcedureSearchResult;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Container für eine Prozedur (Vorbereitend für den DB-Import)
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
public class ProzedurenContainer {

    private static final Logger LOG = Logger.getLogger(ProzedurenContainer.class.getName());

    private String code = "";
    private Date modDate = null;
    private String modUser = "";
    private Date beginDate = null;
    private String version = "2005";
    private int isDrg = 1;
    private int mainCode = 0;
    private int storno = 0;
    private String eingabeText = "";
    private String lok = "";
    private String zusatz = "";
    private int bewegungnr = 0;
    private long kisKey = 0;
    private String specKis = "";
    private String wardKis = "";

    /**
     * Bereitet Prozedurendaten für den DB-Import auf
     *
     * @param pProcedure SapProcedureSearchResult
     */
    public ProzedurenContainer(SapProcedureSearchResult pProcedure) {
        try {
            kisKey = Integer.parseInt(pProcedure.getSurprocSeqno());
        } catch (NumberFormatException ex) {
            LOG.log(Level.WARNING, "KIS Key ungültig: " + pProcedure.getSurprocSeqno(), ex);
        }
        modDate = pProcedure.getUpdateDate();
        modUser = pProcedure.getUpdateUser();
        beginDate = combineDate(pProcedure.getBegindate(), pProcedure.getBegintime());
        code = pProcedure.getSgPrCode().toLowerCase();
        if (code.length() > 12) {
            code = code.substring(0, 12);
        }
        eingabeText = checkQuotes(pProcedure.getProcShorttext());
        if (eingabeText.length() > 250) {
            eingabeText = eingabeText.substring(0, 250);
        }
        mainCode = "X".equals(pProcedure.getMaincode()) ? 1 : 0;
        storno = "X".equals(pProcedure.getCancelInd()) ? 1 : 0;
        lok = pProcedure.getLocalis();
        isDrg = "S".equals(pProcedure.getDrgProcCategory()) || "P".equals(pProcedure.getDrgProcCategory()) ? 1 : 0;
        try {
            bewegungnr = Integer.parseInt(pProcedure.getMovemntSeqno());
        } catch (NumberFormatException e) {
            LOG.log(Level.WARNING, "Bewegungsnr. ungültig: " + pProcedure.getMovemntSeqno(), e);
        }
        specKis = pProcedure.getDeptou();
        wardKis = pProcedure.getPerfou();
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the modDate
     */
    public Date getModDate() {
        return modDate == null ? null : new Date(modDate.getTime());
    }

    /**
     * @return the modUser
     */
    public String getModUser() {
        return modUser;
    }

    /**
     * @return the beginDate
     */
    public Date getBeginDate() {
        return beginDate == null ? null : new Date(beginDate.getTime());
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @return the isDrg
     */
    public int getIsDrg() {
        return isDrg;
    }

    /**
     * @return the mainCode
     */
    public int getMainCode() {
        return mainCode;
    }

    /**
     * @return the storno
     */
    public int getStorno() {
        return storno;
    }

    /**
     * @return the eingabeText
     */
    public String getEingabeText() {
        return eingabeText;
    }

    /**
     * @return the lok
     */
    public String getLok() {
        return lok;
    }

    /**
     * @return the zusatz
     */
    public String getZusatz() {
        return zusatz;
    }

    /**
     * @return the bewegungnr
     */
    public int getBewegungnr() {
        return bewegungnr;
    }

    /**
     * @return the kisKey
     */
    public long getKisKey() {
        return kisKey;
    }

    /**
     * @return the spec_kis
     */
    public String getSpecKis() {
        return specKis;
    }

    /**
     * @return the ward_kis
     */
    public String getWardKis() {
        return wardKis;
    }
}
