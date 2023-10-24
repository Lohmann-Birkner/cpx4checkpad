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

import java.util.Date;

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
public class RmlLabor {

    private double labvValue; //Ergebniswert 
    private double labvValue2; //2. Ergebniswert (nicht aus SAP)
    private Date labvDate; //Dokumenten-Datum
    private String labvRange; //Normalwert bei Laborwerten
    private String labvBenchmark; //Bewertung (zu hoch, zu niedrig) (nicht aus SAP)
    private double labvMaxLimit; //Obergrenze
    private double labvMinlimit; //Untergrenze
    private String labvText; //Ergebniswert als Text
    private String labvComment; //Kommentar
    private java.sql.Date labvAnalysisdtm; //Analysedatum
    private int labvPosition; //Befund-Position (nicht aus SAP)
    private String labvAnalysis; //Auffälligkeitskennzeichen bei Laborwerten 
    private String labvDescr; //Katalogleistungstext 
    private String labvUnit; //Ergebniseinheit 
    private String labvMethode; //Methode (nicht aus SAP)
    private int labvCategory; //Kategorie (nicht aus SAP)
    private String labvGroup; //Laborgruppe
    private boolean labvLockdel; //(wird nirgendwo verwendet) (nicht aus SAP)
    private String kisExternKey; //Schlüssel aus (Mandant_Dokumentenart_Dokumentennummer_Dokumentenversion_Dokumententitel), VARCHAR 100 (mit Index)

    /**
     *
     */
    public RmlLabor() {
        //super(TABLE_NAME, true);
    }

    /**
     * @return the labvValue
     */
    public double getLabvValue() {
        return labvValue;
    }

    /**
     * @param labvValue the labvValue to set
     */
    public void setLabvValue(double labvValue) {
        this.labvValue = labvValue;
    }

    /**
     * @return the labvValue2
     */
    public double getLabvValue2() {
        return labvValue2;
    }

    /**
     * @param labvValue2 the labvValue2 to set
     */
    public void setLabvValue2(double labvValue2) {
        this.labvValue2 = labvValue2;
    }

    /**
     * @return the labvDate
     */
    public Date getLabvDate() {
        return labvDate == null ? null : new Date(labvDate.getTime());
    }

    /**
     * @param labvDate the labvDate to set
     */
    public void setLabvDate(Date labvDate) {
        this.labvDate = labvDate == null ? null : new Date(labvDate.getTime());
    }

    /**
     * @return the labvRange
     */
    public String getLabvRange() {
        return labvRange;
    }

    /**
     * @param labvRange the labvRange to set
     */
    public void setLabvRange(String labvRange) {
        this.labvRange = labvRange;
    }

    /**
     * @return the labvBenchmark
     */
    public String getLabvBenchmark() {
        return labvBenchmark;
    }

    /**
     * @param labvBenchmark the labvBenchmark to set
     */
    public void setLabvBenchmark(String labvBenchmark) {
        this.labvBenchmark = labvBenchmark;
    }

    /**
     * @return the labvMaxLimit
     */
    public double getLabvMaxLimit() {
        return labvMaxLimit;
    }

    /**
     * @param labvMaxLimit the labvMaxLimit to set
     */
    public void setLabvMaxLimit(double labvMaxLimit) {
        this.labvMaxLimit = labvMaxLimit;
    }

    /**
     * @return the labvMinlimit
     */
    public double getLabvMinlimit() {
        return labvMinlimit;
    }

    /**
     * @param labvMinlimit the labvMinlimit to set
     */
    public void setLabvMinlimit(double labvMinlimit) {
        this.labvMinlimit = labvMinlimit;
    }

    /**
     * @return the labvText
     */
    public String getLabvText() {
        return labvText;
    }

    /**
     * @param labvText the labvText to set
     */
    public void setLabvText(String labvText) {
        this.labvText = labvText;
    }

    /**
     * @return the labvComment
     */
    public String getLabvComment() {
        return labvComment;
    }

    /**
     * @param labvComment the labvComment to set
     */
    public void setLabvComment(String labvComment) {
        this.labvComment = labvComment;
    }

    /**
     * @return the labvAnalysisdtm
     */
    public java.sql.Date getLabvAnalysisdtm() {
        return labvAnalysisdtm == null ? null : new java.sql.Date(labvAnalysisdtm.getTime());
    }

    /**
     * @param labvAnalysisdtm the labvAnalysisdtm to set
     */
    public void setLabvAnalysisdtm(java.sql.Date labvAnalysisdtm) {
        this.labvAnalysisdtm = labvAnalysisdtm == null ? null : new java.sql.Date(labvAnalysisdtm.getTime());
    }

    /**
     * @return the labvPosition
     */
    public int getLabvPosition() {
        return labvPosition;
    }

    /**
     * @param labvPosition the labvPosition to set
     */
    public void setLabvPosition(int labvPosition) {
        this.labvPosition = labvPosition;
    }

    /**
     * @return the labvAnalysis
     */
    public String getLabvAnalysis() {
        return labvAnalysis;
    }

    /**
     * @param labvAnalysis the labvAnalysis to set
     */
    public void setLabvAnalysis(String labvAnalysis) {
        this.labvAnalysis = labvAnalysis;
    }

    /**
     * @return the labvDescr
     */
    public String getLabvDescr() {
        return labvDescr;
    }

    /**
     * @param labvDescr the labvDescr to set
     */
    public void setLabvDescr(String labvDescr) {
        this.labvDescr = labvDescr;
    }

    /**
     * @return the labvUnit
     */
    public String getLabvUnit() {
        return labvUnit;
    }

    /**
     * @param labvUnit the labvUnit to set
     */
    public void setLabvUnit(String labvUnit) {
        this.labvUnit = labvUnit;
    }

    /**
     * @return the labvMethode
     */
    public String getLabvMethode() {
        return labvMethode;
    }

    /**
     * @param labvMethode the labvMethode to set
     */
    public void setLabvMethode(String labvMethode) {
        this.labvMethode = labvMethode;
    }

    /**
     * @return the labvCategory
     */
    public int getLabvCategory() {
        return labvCategory;
    }

    /**
     * @param labvCategory the labvCategory to set
     */
    public void setLabvCategory(int labvCategory) {
        this.labvCategory = labvCategory;
    }

    /**
     * @return the labvGroup
     */
    public String getLabvGroup() {
        return labvGroup;
    }

    /**
     * @param labvGroup the labvGroup to set
     */
    public void setLabvGroup(String labvGroup) {
        this.labvGroup = labvGroup;
    }

    /**
     * @return the labvLockdel
     */
    public boolean isLabvLockdel() {
        return labvLockdel;
    }

    /**
     * @param labvLockdel the labvLockdel to set
     */
    public void setLabvLockdel(boolean labvLockdel) {
        this.labvLockdel = labvLockdel;
    }

    /**
     * @return the kisExternKey
     */
    public String getKisExternKey() {
        return kisExternKey;
    }

    /**
     * @param kisExternKey the kisExternKey to set
     */
    public void setKisExternKey(String kisExternKey) {
        this.kisExternKey = kisExternKey;
    }

}
