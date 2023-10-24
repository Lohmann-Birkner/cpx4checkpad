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
import de.lb.cpx.sap.results.SapDiagnosisSearchResult;
import dto.RmeDiagnose;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Container für die Diagnosen (vorbereitet für den DB-Import)
 *
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
public class DiagnosenContainer {

    private static final Logger LOG = Logger.getLogger(DiagnosenContainer.class.getName());

    private String code = "";
    private String typ = "";
    private String eingabeText = "";
    private String lok = "";
    private String version = "2005";
    private Date createDate = null;
    private String createUser = "";
    private Date modDate = null;
    private String modUser = "";
    private String zusatz = "";
    private int isDrg = 0;
    private int isAdmission = 0;
    private int isDeptPdx = 0;
    private int isCasePdx = 0;
    private int isOPPdx = 0;
    //public int grouped = 0;
    //public int opID = 0;
    private int storno = 0;
    private int bewegungnr = 0;
    private int art = 0;
    private long kisKey = 0;
    private int refType = 0;
    private long refKisKey = 0;

    /**
     * Bereitet Diagnosen für den DB-Import auf
     *
     * @param pDiagnosis SapDiagnosisSearchResult
     */
    public DiagnosenContainer(SapDiagnosisSearchResult pDiagnosis) {
        try {
            kisKey = Integer.parseInt(pDiagnosis.getDiagSeqno());
        } catch (NumberFormatException ex) {
            LOG.log(Level.WARNING, "KIS Key ungültig: " + pDiagnosis.getDiagSeqno(), ex);
        }
        createDate = pDiagnosis.getCreationDate();
        createUser = pDiagnosis.getCreationUser();
        modDate = pDiagnosis.getUpdateDate();
        modUser = pDiagnosis.getUpdateUser();
        lok = pDiagnosis.getDiagLocation();
        isDrg = "S".equals(pDiagnosis.getDrgCategory()) || "P".equals(pDiagnosis.getDrgCategory()) ? 1 : 0;
        isAdmission = "X".equals(pDiagnosis.getAdmissionDia()) ? 1 : 0;
        isDeptPdx = "X".equals(pDiagnosis.getDeptMainDia()) ? 1 : 0;
        isCasePdx = "P".equals(pDiagnosis.getDrgCategory()) ? 1 : 0;
        isOPPdx = (pDiagnosis.getSurgeryDia() == null || pDiagnosis.getSurgeryDia().isEmpty()) ? 0 : 1;
        typ = pDiagnosis.getDiagTyp1().length() > 0 ? pDiagnosis.getDiagTyp1() : pDiagnosis.getDiagTyp2();
        code = pDiagnosis.getDiagKey1().length() > 0 ? pDiagnosis.getDiagKey1() : pDiagnosis.getDiagKey2();
        if (code.length() > 10) {
            code = code.substring(0, 10);
        }
        eingabeText = checkQuotes(pDiagnosis.getDiagText());
        if (eingabeText.length() > 250) {
            eingabeText = eingabeText.substring(0, 250);
        }
        zusatz = pDiagnosis.getDiagAddition();
        if (zusatz.length() > 20) {
            zusatz = zusatz.substring(0, 20);
        }
        lok = pDiagnosis.getDiagLocation();
        storno = "X".equals(pDiagnosis.getCancelInd()) ? 1 : 0;
        if (isDrg == 1) {
            art = 6;
        } else if (pDiagnosis.getReferralDia() != null && pDiagnosis.getReferralDia().length() > 0) {
            art = 1;
        } else if (pDiagnosis.getAdmissionDia() != null && pDiagnosis.getAdmissionDia().length() > 0) {
            art = 2;
        } else if (pDiagnosis.getWorkDiagInd() != null && pDiagnosis.getWorkDiagInd().length() > 0) {
            art = 12;
        } else if (pDiagnosis.getDischargeDia() != null && pDiagnosis.getDischargeDia().length() > 0) {
            art = 4;
        } else if (pDiagnosis.getSurgeryDia() != null && pDiagnosis.getSurgeryDia().length() > 0) {
            art = 7;
        } else if (pDiagnosis.getPreopDiagInd() != null && pDiagnosis.getPreopDiagInd().length() > 0) {
            art = 8;
        } else if (pDiagnosis.getTreatmentDia() != null && pDiagnosis.getTreatmentDia().length() > 0) {
            art = 5;
        }
        try {
            bewegungnr = Integer.parseInt(pDiagnosis.getMovemntSeqno());
        } catch (NumberFormatException ex) {
            LOG.log(Level.WARNING, "Cannot parse bewegungnr string to integer: " + pDiagnosis.getMovemntSeqno(), ex);
        }
        String reft = pDiagnosis.getDiagRefTyp();
        refType = getDiagRefType(reft);
        if (refType == RmeDiagnose.REF_TYPE_STERN || refType == RmeDiagnose.REF_TYPE_ADDITIONAL) {
            try {
                refKisKey = Integer.parseInt(pDiagnosis.getDiaLink());
            } catch (NumberFormatException ex) {
                LOG.log(Level.WARNING, "Ref KIS Key ungültig: " + pDiagnosis.getDiaLink(), ex);
            }
        }
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the typ
     */
    public String getTyp() {
        return typ;
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
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate == null ? null : new Date(createDate.getTime());
    }

    /**
     * @return the createUser
     */
    public String getCreateUser() {
        return createUser;
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
     * @return the zusatz
     */
    public String getZusatz() {
        return zusatz;
    }

    /**
     * @return the isDrg
     */
    public int getIsDrg() {
        return isDrg;
    }

    /**
     * @return the isAdmission
     */
    public int getIsAdmission() {
        return isAdmission;
    }

    /**
     * @return the isDeptPdx
     */
    public int getIsDeptPdx() {
        return isDeptPdx;
    }

    /**
     * @return the isCasePdx
     */
    public int getIsCasePdx() {
        return isCasePdx;
    }

    /**
     * @return the isOPPdx
     */
    public int getIsOPPdx() {
        return isOPPdx;
    }

    /**
     * @return the storno
     */
    public int getStorno() {
        return storno;
    }

    /**
     * @return the bewegungnr
     */
    public int getBewegungnr() {
        return bewegungnr;
    }

    /**
     * @return the art
     */
    public int getArt() {
        return art;
    }

    /**
     * @return the kis_key
     */
    public long getKisKey() {
        return kisKey;
    }

    /**
     * @return the refType
     */
    public int getRefType() {
        return refType;
    }

    /**
     * @return the ref_kis_key
     */
    public long getRefKisKey() {
        return refKisKey;
    }
}
