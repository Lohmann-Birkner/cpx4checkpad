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
public class ReferenzCase {

    private String caseNr;
    private String refFallnr = "";
    private String refType = "";
    private String refTypeText = "";
    private String caseType = "";
    private String caseTypeText = "";

    /**
     *
     * @param pFallnr case number
     */
    public ReferenzCase(final String pFallnr) {
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
     * @return the refFallnr
     */
    public String getRefFallnr() {
        return refFallnr;
    }

    /**
     * @param refFallnr the refFallnr to set
     */
    public void setRefFallnr(String refFallnr) {
        this.refFallnr = refFallnr;
    }

    /**
     * @return the refType
     */
    public String getRefType() {
        return refType;
    }

    /**
     * @param refType the refType to set
     */
    public void setRefType(String refType) {
        this.refType = refType;
    }

    /**
     * @return the refTypeText
     */
    public String getRefTypeText() {
        return refTypeText;
    }

    /**
     * @param refTypeText the refTypeText to set
     */
    public void setRefTypeText(String refTypeText) {
        this.refTypeText = refTypeText;
    }

    /**
     * @return the caseType
     */
    public String getCaseType() {
        return caseType;
    }

    /**
     * @param caseType the caseType to set
     */
    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    /**
     * @return the caseTypeText
     */
    public String getCaseTypeText() {
        return caseTypeText;
    }

    /**
     * @param caseTypeText the caseTypeText to set
     */
    public void setCaseTypeText(String caseTypeText) {
        this.caseTypeText = caseTypeText;
    }

    @Override
    public String toString() {
        return "\tFallnummer : " + getCaseNr() + "\n"
                + "\tRef-Fallnummer : " + getRefFallnr() + "\n"
                + "\tRef-Typ : " + getRefType() + "\n"
                + "\tRef-Typ-Text : " + getRefTypeText() + "\n"
                + "\tFalltyp : " + getCaseType() + "\n"
                + "\tFalltyp-Text : " + getCaseTypeText() + "\n";
    }
}
