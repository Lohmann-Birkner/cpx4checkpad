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
package de.lb.cpx.model;

//import de.lb.cpx.model.converter.CsCaseTypeConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import de.lb.cpx.server.commons.dao.AbstractVersionEntity;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TCase initially generated at 21.01.2016 17:07:59 by Hibernate Tools 3.2.2.GA
 * <p style="font-size:1em; color:green;"> Die Tabelle "T_CASE" speichert alle
 * Krankenhausfälle, die in die Datenbank eingelesen wurden. </p>
 */
@Entity
@Table(name = "T_LAB")
@SuppressWarnings("serial")
public class TLab extends AbstractVersionEntity {

    private static final long serialVersionUID = 1L;

//    private long id;
    private TCase hospitalCase;
//    private TCaseDetails caseDetailsLocal; //fall_lokalid         NUMBER(20,0) NULL,
//    private TCaseDetails caseDetailsExtern; //fall_externid        NUMBER(20,0) NULL,
    private Double labValue; //labv_value		FLOAT NULL,
    private Double labValue2; //labv_value2		FLOAT NULL,
    private Date labDate; //labv_date		DATE NULL,
    private String labRange; //labv_range 		varchar2(200) NULL,
    private String labBenchmark; //labv_benchmark 		varchar2(10) NULL,
    private Double labMaxLimit; //labv_maxlimit		FLOAT NULL,
    private Double labMinLimit; //labv_minlimit		FLOAT NULL,
    private String labText; //labv_text 		varchar2(100) NULL,
    private String labComment; //labv_comment 		varchar2(200) NULL,
    private Date labAnalysisDate; //labv_analysisdtm	DATE NULL,
    private Integer labPosition; //labv_position 		NUMBER(20,0) NULL,
    private String labAnalysis; //labv_analysis 		varchar2(100) NULL,
    private String labDescription; //labv_descr 		varchar2(40) NULL,
    private String labUnit; //labv_unit 		varchar2(20) NULL,
    private String labMethod; //labv_methode 		varchar2(40) NULL,
    private Integer labCategory; //labv_category 		NUMBER(20,0) NULL,
    private String labGroup; //labv_group		varchar2(100) NULL,
    private Integer labLockdel; //labv_lockdel		NUMBER(3) NULL,
    private String labKisExternKey; //kis_extern_key		varchar2(100) null;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_LAB_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    /**
     * Schlüssel aus
     * (Mandant_Dokumentenart_Dokumentennummer_Dokumentenversion_Dokumententitel),
     * VARCHAR 100 (mit Index)
     *
     * @return Schlüssel aus
     * (Mandant_Dokumentenart_Dokumentennummer_Dokumentenversion_Dokumententitel),
     * VARCHAR 100 (mit Index)
     */
    @Column(name = "LAB_KIS_EXTERN_KEY", nullable = true, length = 100)
    public String getLabKisExternKey() {
        return this.labKisExternKey;
    }

    /**
     * Schlüssel aus
     * (Mandant_Dokumentenart_Dokumentennummer_Dokumentenversion_Dokumententitel),
     * VARCHAR 100 (mit Index)
     *
     * @param pLabKisExternKey Schlüssel aus
     * (Mandant_Dokumentenart_Dokumentennummer_Dokumentenversion_Dokumententitel),
     * VARCHAR 100 (mit Index)
     */
    public void setLabKisExternKey(String pLabKisExternKey) {
        this.labKisExternKey = pLabKisExternKey;
    }

    /**
     * (wird nirgendwo verwendet) (nicht aus SAP)
     *
     * @return (wird nirgendwo verwendet) (nicht aus SAP)
     */
    @Column(name = "LAB_LOCKDEL", nullable = true)
    public Integer getLabLockdel() {
        return this.labLockdel;
    }

    /**
     * (wird nirgendwo verwendet) (nicht aus SAP)
     *
     * @param pLabLockdel (wird nirgendwo verwendet) (nicht aus SAP)
     */
    public void setLabLockdel(final Integer pLabLockdel) {
        this.labLockdel = pLabLockdel;
    }

    /**
     * Laborgruppe
     *
     * @return Laborgruppe
     */
    @Column(name = "LAB_GROUP", nullable = true, length = 100)
    public String getLabGroup() {
        return this.labGroup;
    }

    /**
     * Laborgruppe
     *
     * @param pLabGroup Laborgruppe
     */
    public void setLabGroup(String pLabGroup) {
        this.labGroup = pLabGroup;
    }

    /**
     * Kategorie (nicht aus SAP)
     *
     * @return Kategorie (nicht aus SAP)
     */
    @Column(name = "LAB_CATEGORY", nullable = true)
    public Integer getLabCategory() {
        return this.labCategory;
    }

    /**
     * Kategorie (nicht aus SAP)
     *
     * @param pLabCategory Kategorie (nicht aus SAP)
     */
    public void setLabCategory(final Integer pLabCategory) {
        this.labCategory = pLabCategory;
    }

    /**
     * Methode (nicht aus SAP)
     *
     * @return Methode (nicht aus SAP)
     */
    @Column(name = "LAB_METHOD", nullable = true, length = 50)
    public String getLabMethod() {
        return this.labMethod;
    }

    /**
     * Methode (nicht aus SAP)
     *
     * @param pLabMethod Methode (nicht aus SAP)
     */
    public void setLabMethod(String pLabMethod) {
        this.labMethod = pLabMethod;
    }

    /**
     * Ergebniseinheit
     *
     * @return Ergebniseinheit
     */
    @Column(name = "LAB_UNIT", nullable = true, length = 25)
    public String getLabUnit() {
        return this.labUnit;
    }

    /**
     * Ergebniseinheit
     *
     * @param pLabUnit Ergebniseinheit
     */
    public void setLabUnit(String pLabUnit) {
        this.labUnit = pLabUnit;
    }

    /**
     * Katalogleistungstext
     *
     * @return Katalogleistungstext
     */
    @Column(name = "LAB_DESCRIPTION", nullable = true, length = 50)
    public String getLabDescription() {
        return this.labDescription;
    }

    /**
     * Katalogleistungstext
     *
     * @param pLabDescription Katalogleistungstext
     */
    public void setLabDescription(String pLabDescription) {
        this.labDescription = pLabDescription;
    }

    /**
     * Auffälligkeitskennzeichen bei Laborwerten
     *
     * @return Auffälligkeitskennzeichen bei Laborwerten
     */
    @Column(name = "LAB_ANALYSIS", nullable = true, length = 100)
    public String getLabAnalysis() {
        return this.labAnalysis;
    }

    /**
     * Auffälligkeitskennzeichen bei Laborwerten
     *
     * @param pLabAnalysis Auffälligkeitskennzeichen bei Laborwerten
     */
    public void setLabAnalysis(String pLabAnalysis) {
        this.labAnalysis = pLabAnalysis;
    }

    /**
     * Befund-Position (nicht aus SAP)
     *
     * @return Befund-Position (nicht aus SAP)
     */
    @Column(name = "LAB_POSITION", nullable = true)
    public Integer getLabPosition() {
        return this.labPosition;
    }

    /**
     * Befund-Position (nicht aus SAP)
     *
     * @param pLabPosition Befund-Position (nicht aus SAP)
     */
    public void setLabPosition(final Integer pLabPosition) {
        this.labPosition = pLabPosition;
    }

    /**
     * Analysedatum
     *
     * @return Analysedatum
     */
    //@Temporal(TemporalType.DATE)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAB_ANALYSIS_DATE", length = 7, nullable = true)
    public Date getLabAnalysisDate() {
        return this.labAnalysisDate == null ? null : new Date(this.labAnalysisDate.getTime());
    }

    /**
     * Analysedatum
     *
     * @param pLabAnalysisDate Analysedatum
     */
    public void setLabAnalysisDate(final Date pLabAnalysisDate) {
        this.labAnalysisDate = pLabAnalysisDate == null ? null : new Date(pLabAnalysisDate.getTime());
    }

    /**
     * Obergrenze
     *
     * @return Obergrenze
     */
    @Column(name = "LAB_MAX_LIMIT", nullable = true)
    public Double getLabMaxLimit() {
        return this.labMaxLimit;
    }

    /**
     * Obergrenze
     *
     * @param pLabMaxLimit Obergrenze
     */
    public void setLabMaxLimit(final Double pLabMaxLimit) {
        this.labMaxLimit = pLabMaxLimit;
    }

    /**
     * Untergrenze
     *
     * @return Untergrenze
     */
    @Column(name = "LAB_MIN_LIMIT", nullable = true)
    public Double getLabMinLimit() {
        return this.labMinLimit;
    }

    /**
     * Untergrenze
     *
     * @param pLabMinLimit Untergrenze
     */
    public void setLabMinLimit(final Double pLabMinLimit) {
        this.labMinLimit = pLabMinLimit;
    }

    /**
     * Bewertung (zu hoch, zu niedrig) (nicht aus SAP)
     *
     * @return Bewertung (zu hoch, zu niedrig) (nicht aus SAP)
     */
    @Column(name = "LAB_BENCHMARK", nullable = true, length = 10)
    public String getLabBenchmark() {
        return this.labBenchmark;
    }

    /**
     * Bewertung (zu hoch, zu niedrig) (nicht aus SAP)
     *
     * @param pLabBenchmark Bewertung (zu hoch, zu niedrig) (nicht aus SAP)
     */
    public void setLabBenchmark(String pLabBenchmark) {
        this.labBenchmark = pLabBenchmark;
    }

    /**
     * Normalwert bei Laborwerten
     *
     * @return Normalwert bei Laborwerten
     */
    @Column(name = "LAB_RANGE", nullable = true, length = 255)
    public String getLabRange() {
        return this.labRange;
    }

    /**
     * Normalwert bei Laborwerten
     *
     * @param pLabRange Normalwert bei Laborwerten
     */
    public void setLabRange(String pLabRange) {
        this.labRange = pLabRange;
    }

    /**
     * Ergebniswert als Text
     *
     * @return Ergebniswert als Text
     */
    @Column(name = "LAB_TEXT", nullable = true, length = 100)
    public String getLabText() {
        return this.labText;
    }

    /**
     * Ergebniswert als Text
     *
     * @param pLabText Ergebniswert als Text
     */
    public void setLabText(String pLabText) {
        this.labText = pLabText;
    }

    /**
     * Kommentar
     *
     * @return Kommentar
     */
    @Column(name = "LAB_COMMENT", nullable = true, length = 255)
    public String getLabComment() {
        return this.labComment;
    }

    /**
     * Kommentar
     *
     * @param pLabComment Kommentar
     */
    public void setLabComment(String pLabComment) {
        this.labComment = pLabComment;
    }

    /**
     * Dokumenten-Datum
     *
     * @return Dokumenten-Datum
     */
    //@Temporal(TemporalType.DATE)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAB_DATE", length = 7, nullable = true)
    public Date getLabDate() {
        return this.labDate == null ? null : new Date(this.labDate.getTime());
    }

    /**
     * Dokumenten-Datum
     *
     * @param pLabDate Dokumenten-Datum
     */
    public void setLabDate(final Date pLabDate) {
        this.labDate = pLabDate == null ? null : new Date(pLabDate.getTime());
    }

    /**
     * Ergebniswert
     *
     * @return Ergebniswert
     */
    @Column(name = "LAB_VALUE", nullable = true)
    public Double getLabValue() {
        return this.labValue;
    }

    /**
     * Ergebniswert
     *
     * @param pLabValue Ergebniswert
     */
    public void setLabValue(final Double pLabValue) {
        this.labValue = pLabValue;
    }

    /**
     * 2. Ergebniswert (nicht aus SAP)
     *
     * @return 2. Ergebniswert (nicht aus SAP)
     */
    @Column(name = "LAB_VALUE_2", nullable = true)
    public Double getLabValue2() {
        return this.labValue2;
    }

    /**
     * 2. Ergebniswert (nicht aus SAP)
     *
     * @param pLabValue2 2. Ergebniswert (nicht aus SAP)
     */
    public void setLabValue2(final Double pLabValue2) {
        this.labValue2 = pLabValue2;
    }

//    /**
//     * Gibt Verweis auf die ID der Tabelle T_CASE_DETAILS zurück.
//     *
//     * @return caseDetailsLocal
//     */
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "T_CASE_DETAILS_LOCAL_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_LAB4T_CSD_LOCAL_ID"))
//    public TCaseDetails getCaseDetailsLocal() {
//        return this.caseDetailsLocal;
//    }
//
//    /**
//     *
//     * @param caseDetailsLocal Column T_CASE_DETAILS_ID :Verweis auf die ID der
//     * Tabelle T_CASE_DETAILS.
//     */
//    public void setCaseDetailsLocal(final TCaseDetails caseDetailsLocal) {
//        this.caseDetailsLocal = caseDetailsLocal;
//    }
//
//    /**
//     * Gibt Verweis auf die ID der Tabelle T_CASE_DETAILS zurück.
//     *
//     * @return caseDetailsLocal
//     */
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "T_CASE_DETAILS_EXTERN_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_LAB4T_CSD_EXTERN_ID"))
//    public TCaseDetails getCaseDetailsExtern() {
//        return this.caseDetailsExtern;
//    }
//
//    /**
//     *
//     * @param caseDetailsExtern Column T_CASE_DETAILS_ID :Verweis auf die ID der
//     * Tabelle T_CASE_DETAILS.
//     */
//    public void setCaseDetailsExtern(final TCaseDetails caseDetailsExtern) {
//        this.caseDetailsExtern = caseDetailsExtern;
//    }
//
    /**
     * Gibt Verweis auf die ID der Tabelle T_CASE zurück
     *
     * @return hospitalCase
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_CASE_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_LAB4T_CASE_ID"))
    @JsonBackReference(value = "labor")
    public TCase getHospitalCase() {
        return this.hospitalCase;
    }

    /**
     *
     * @param hospitalCase Column T_CASE_DETAILS_ID:Verweis auf die ID der
     * Tabelle T_CASE.
     */
    public void setHospitalCase(final TCase hospitalCase) {
        this.hospitalCase = hospitalCase;
    }

    @Override
    public boolean versionEquals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TLab)) {
            return false;
        }
        final TLab other = (TLab) obj;
//        if (!isStringEquals(this.csdInsCompany, other.getCsdInsCompany())) {
//            return false;
//        }
        if (!Objects.equals(this.labAnalysis, other.getLabAnalysis())) {
            return false;
        }
        if (!Objects.equals(this.labAnalysisDate, other.getLabAnalysisDate())) {
            return false;
        }
        if (!Objects.equals(this.labBenchmark, other.getLabBenchmark())) {
            return false;
        }
        if (!Objects.equals(this.labCategory, other.getLabCategory())) {
            return false;
        }
        if (!Objects.equals(this.labComment, other.getLabComment())) {
            return false;
        }
        if (!Objects.equals(this.labDate, other.getLabDate())) {
            return false;
        }
        if (!Objects.equals(this.labDescription, other.getLabDescription())) {
            return false;
        }
        if (!Objects.equals(this.labGroup, other.getLabGroup())) {
            return false;
        }
        if (!Objects.equals(this.labKisExternKey, other.getLabKisExternKey())) {
            return false;
        }
        if (!Objects.equals(this.labLockdel, other.getLabLockdel())) {
            return false;
        }
        if (!Objects.equals(this.labMaxLimit, other.getLabMaxLimit())) {
            return false;
        }
        if (!Objects.equals(this.labMethod, other.getLabMethod())) {
            return false;
        }
        if (!Objects.equals(this.labMinLimit, other.getLabMinLimit())) {
            return false;
        }
        if (!Objects.equals(this.labPosition, other.getLabPosition())) {
            return false;
        }
        if (!Objects.equals(this.labRange, other.getLabRange())) {
            return false;
        }
        if (!Objects.equals(this.labText, other.getLabText())) {
            return false;
        }
        if (!Objects.equals(this.labUnit, other.getLabUnit())) {
            return false;
        }
        if (!Objects.equals(this.labValue, other.getLabValue())) {
            return false;
        }
        if (!Objects.equals(this.labValue2, other.getLabValue2())) {
            return false;
        }

        return true;

    }

}
