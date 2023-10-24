/*
 * Copyright (c) 2016 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 */
package de.lb.cpx.server.commonDB.model;

import de.checkpoint.drg.GrouperInterfaceBasic;
import de.lb.cpx.model.converter.GrouperMdcOrSkConverter;
import de.lb.cpx.model.enums.CountryEn;
import de.lb.cpx.model.enums.DrgPartitionEn;
import de.lb.cpx.model.enums.GrouperMdcOrSkEn;
import de.lb.cpx.server.commons.dao.AbstractDrgmCatalogEntity;
import static de.lb.cpx.service.information.CatalogTypeEn.DRG;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.Type;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 * CDrgCatalog initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br><p style="font-size:1em; color:green ;">C_DRG_CATALOG : Kataloge der
 * Diagnosis Related Groups (DRG), aufgeteilt nach Jahren.</p>
 *
 */
@Entity
@Table(name = "C_DRG_CATALOG")
@SuppressWarnings("serial")
@SecurityDomain("cpx")
public class CDrgCatalog extends AbstractDrgmCatalogEntity implements CCatalogIF { 

    private static final long serialVersionUID = 1L;

//     private long id;
    private CountryEn countryEn;
    //private CHospital hospital; // for negotiated DRGs only
    private String drgHosIdent; // for negotiated DRGs only
    private int drgYear;
    private String drgDrg;
    private String drgDescription;
    private DrgPartitionEn drgPartitionEn; // 
    private GrouperMdcOrSkEn mdcSkEn;
    private boolean drgIsNegotiatedFl;
    private boolean drgIsDayCareFl;
    private Date drgValidFrom;
    private Date drgValidTo;
    private int drgMd1DeductionDay; // first day with deduction for main department
    private int drgMd1SurchargeDay; // first day with surcharge for main department
    private BigDecimal drgMdAlos; // average Length of stay for main department
    private BigDecimal drgMdCw; // cost weigth for main department
    private BigDecimal drgMdMCw; // cost weigth for main department with midwife
    private BigDecimal drgMdCwDeduction; // deduction cost weight for one day for main department
    private BigDecimal drgMdCwSurcharge;//surcharge cost weight for one day for main department
    private BigDecimal drgMdCwTransfDeduct; // deduction cost weight for one day by transferring from other hospital to main department
    private boolean drgMdIsTransferFl; // flag for lump sum transfer for main department
    private boolean drgMdIsReadmFl; // flag for lump sum readmission for main department
    private int drgEo1DeductionDay; // first day with deduction for external occupancy department
    private int drgEo1SurchargeDay; // first day with surcharge for external occupancy department
    private BigDecimal drgEoAlos; // average Length of stay for external occupancy department
    private BigDecimal drgEoCw; // cost weigth for external occupancy department, surgery 
    private BigDecimal drgEoaCw; // cost weigth for external occupancy department, surgery and anesthesiologist
    private BigDecimal drgEomCw; // cost weigth for external occupancy department, surgery and midwife
    private BigDecimal drgEoamCw; // cost weigth for external occupancy department, surgery, anesthesiologist and midwife
    private BigDecimal drgEoCwDeduction; // deduction cost weight for one day for external occupancy department
    private BigDecimal drgEoCwSurcharge;//surcharge cost weight for one day for external occupancy department
    private BigDecimal drgEoCwTransfDeduct; // deduction cost weight for one day by transferring from other hospital to external occupancy department
    private boolean drgEoIsTransferFl; // flag for lump sum transfer for external occupancy department
    private boolean drgEoIsReadmFl; // flag for lump sum readmission for external occupancy department
    private double drgNegoDayFee; // monetary negotiated fee value for day
    private int drgMdMedianCaseCount;// median case count for main department
    private int drgEoMedianCaseCount; //median case count for main department
    private BigDecimal drgMdCareCwDay;// median case count for main department
    private BigDecimal drgEoCareCwDay; //median case count for main department

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_DRG_CATALOG_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "COUNTRY_EN", length = 25)
    @Enumerated(EnumType.STRING)
    public CountryEn getCountryEn() {
        return countryEn;
    }

    public void setCountryEn(CountryEn countryEn) {
        this.countryEn = countryEn;
    }

    /*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HOSPITAL_ID", nullable = true)
    public CHospital getHospital() {
        return this.hospital;
    }
    
    public void setHospital(final CHospital hosp){
        hospital = hosp;
    }
     */
    /**
     *
     * @return drgHosIdent : Ident.Nummer des Krankenhauses für verhandelbare
     * Drgs
     */
    @Column(name = "DRG_HOS_IDENT", nullable = true, length = 10)
    public String getDrgHosIdent() {
        return this.drgHosIdent;
    }

    /**
     *
     * @param drgHosIdent DRG_HOS_IDENT : Ident.Nummer des Krankenhauses für
     * verhandelbare Drgs
     */
    public void setDrgHosIdent(String drgHosIdent) {
        this.drgHosIdent = drgHosIdent;
    }

    /**
     *
     * @return drgYear : Katalogjahr
     */
    @Column(name = "DRG_YEAR", nullable = false, length = 4)
    public int getDrgYear() {
        return drgYear;
    }

    public void setDrgYear(final int icdYear) {
        this.drgYear = icdYear;
    }

    /**
     *
     * @return mdcSkEn :Enumeration für GrouperMdcOrSk (PRE,01-23,,PSO,PSY,...)
     */
    @Column(name = "MDC_SK_EN", length = 25)
    //@Enumerated(EnumType.STRING)
    @Convert(converter = GrouperMdcOrSkConverter.class)
    public GrouperMdcOrSkEn getCMdcSkCatalog() {
        return this.mdcSkEn;
    }

    public void setCMdcSkCatalog(GrouperMdcOrSkEn sk) {
        this.mdcSkEn = sk;
    }

    /**
     *
     * @return drgDrg: DRG (Diagnosis Related Groups ).
     */
    @Column(name = "DRG_DRG", nullable = false, length = 10)
    public String getDrgDrg() {
        return this.drgDrg;
    }

    /**
     *
     * @param drgDrg Column DRG_DRG: DRG (Diagnosis Related Groups ).
     */
    public void setDrgDrg(String drgDrg) {
        this.drgDrg = drgDrg;
    }

    /**
     *
     * @return drgDescription : DRG Definition
     */
    @Column(name = "DRG_DESCRIPTION", length = 1000)
    public String getDrgDescription() {
        return this.drgDescription;
    }

    /**
     *
     * @param drgDescription Column DRG_DESCRIPTION : DRG Definition
     */
    public void setDrgDescription(String drgDescription) {
        this.drgDescription = drgDescription;
    }

    /**
     *
     * @return drgPartitionEn: Enumeration für DRG Partition(
     * A=other,O=surgical,M=medical).
     */
    @Column(name = "DRG_PARTITION_EN", length = 25)
    @Enumerated(EnumType.STRING)
    public DrgPartitionEn getDrgPartitionEn() {
        return this.drgPartitionEn;
    }

    /**
     *
     * @param drgPartitionEn Column DRG_PARTITION_EN: Enumeration für DRG
     * Partition( A=other,O=surgical,M=medical).
     */
    public void setDrgPartitionEn(DrgPartitionEn drgPartitionEn) {
        this.drgPartitionEn = drgPartitionEn;
    }

    /**
     *
     * @return drgIsNegotiatedFl: Verhandelbar Ja/Nein 1/0
     */
    @Column(name = "DRG_IS_NEGOTIATED_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean getDrgIsNegotiatedFl() {
        return this.drgIsNegotiatedFl;
    }

    public void setDrgIsNegotiatedFl(final boolean isNg) {
        this.drgIsNegotiatedFl = isNg;
    }

    /**
     *
     * @return drgIsDayCareFl: Teilstationär Ja/Nein: 1/0
     */
    @Column(name = "DRG_IS_DAY_CARE_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean getDrgIsDayCareFl() {
        return this.drgIsDayCareFl;
    }

    public void setDrgIsDayCareFl(final boolean dayCare) {
        this.drgIsDayCareFl = dayCare;
    }

    /**
     *
     * @return drgValidFrom : Anfang der Gültigkeit
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DRG_VALID_FROM", length = 7)
    public Date getDrgValidFrom() {
        return drgValidFrom == null ? null : new Date(drgValidFrom.getTime());
    }

    public void setDrgValidFrom(final Date validFrom) {
        drgValidFrom = validFrom == null ? null : new Date(validFrom.getTime());
    }

    /**
     *
     * @return drgValidTo :Ende der Gültigkeit
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DRG_VALID_TO", length = 7)
    public Date getDrgValidTo() {
        return drgValidTo == null ? null : new Date(drgValidTo.getTime());
    }

    public void setDrgValidTo(final Date validTo) {
        drgValidTo = validTo == null ? null : new Date(validTo.getTime());
    }

    /**
     *
     * @return drgMd1DeductionDay: Hauptabteilung: Erster Tag mit Abschlag
     */
    @Column(name = "DRG_MD_1_DEDUCTION_DAY", precision = 3, scale = 0, nullable = false)
    public int getDrgMd1DeductionDay() {
        return drgMd1DeductionDay;
    }

    /**
     *
     * @param drgMd1DeductionDay Column DRG_MD_1_DEDUCTION_DAY: Hauptabteilung:
     * Erster Tag mit Abschlag
     */
    public void setDrgMd1DeductionDay(int drgMd1DeductionDay) {
        this.drgMd1DeductionDay = drgMd1DeductionDay;
    }

    /**
     *
     * @return drgMd1SurchargeDay :Hauptabteilung: Erster Tag mit Zusatzentgelt
     */
    @Column(name = "DRG_MD_1_SURCHARGE_DAY", precision = 3, scale = 0, nullable = false)
    public int getDrgMd1SurchargeDay() {
        return drgMd1SurchargeDay;
    }

    /**
     *
     * @param drgMd1SurchargeDay Column DRG_MD_1_SURCHARGE_DAY :Hauptabteilung:
     * Erster Tag mit Zusatzentgelt
     */
    public void setDrgMd1SurchargeDay(int drgMd1SurchargeDay) {
        this.drgMd1SurchargeDay = drgMd1SurchargeDay;
    }

    /**
     *
     * @return drgMdAlos :Hauptablteilung Mittlere Verweildauer
     */
    @Column(name = "DRG_MD_ALOS", precision = 5, scale = 1, nullable = false)
    public BigDecimal getDrgMdAlos() {
        return drgMdAlos;
    }

    /**
     *
     * @param drgMdAlos Column DRG_MD_ALOS :Hauptablteilung Mittlere
     * Verweildauer
     */
    public void setDrgMdAlos(BigDecimal drgMdAlos) {
        this.drgMdAlos = drgMdAlos;
    }

    /**
     *
     * @return drgMdCw :Hauptablteilung: Bewertungsrelation
     */
    @Column(name = "DRG_MD_CW", precision = 10, scale = 3, nullable = false)
    public BigDecimal getDrgMdCw() {
        return drgMdCw;
    }

    /**
     *
     * @param drgMdCw Column DRG_MD_CW : Hauptablteilung: Bewertungsrelation
     */
    public void setDrgMdCw(BigDecimal drgMdCw) {
        this.drgMdCw = drgMdCw;
    }

    /**
     *
     * @param drgNegoDayFee Column DRG_NEGO_DAY_FEE: Tagesgleiche Entgelte
     */
    public void setDrgNegoDayFee(double drgNegoDayFee) {
        this.drgNegoDayFee = drgNegoDayFee;
    }

    /**
     *
     * @param drgMdMedianCaseCount Column DRG_MD_MEDIAN_CASE_COUNT:
     * Median-Fallzahl für Hauptableilung
     */
    public void setDrgMdMedianCaseCount(int drgMdMedianCaseCount) {
        this.drgMdMedianCaseCount = drgMdMedianCaseCount;
    }

    /**
     *
     * @param drgEoMedianCaseCount Column DRG_EO_MEDIAN_CASE_COUNT: Median -
     * Fallzahl für Belegabteilung
     */
    public void setDrgEoMedianCaseCount(int drgEoMedianCaseCount) {
        this.drgEoMedianCaseCount = drgEoMedianCaseCount;
    }

    /**
     *
     * @return drgNegoDayFee: Tagesgleiche Entgelt, wenn vereinbahrt
     */
    @Column(name = "DRG_NEGO_DAY_FEE", precision = 10, scale = 2, nullable = false)
    public double getDrgNegoDayFee() {
        return drgNegoDayFee;
    }

    /**
     *
     * @return drgMdMedianCaseCount Medianzahl für Hauptabteilung oder 0
     */
    @Column(name = "DRG_MD_MEDIAN_CASE_COUNT", precision = 5, nullable = false)
    public int getDrgMdMedianCaseCount() {
        return drgMdMedianCaseCount;
    }

    /**
     *
     * @return drgMdMedianCaseCount Medianzahl für Belegabteilung oder 0
     */
    @Column(name = "DRG_EO_MEDIAN_CASE_COUNT", precision = 5, nullable = false)
    public int getDrgEoMedianCaseCount() {
        return drgEoMedianCaseCount;
    }

    /**
     *
     * @return drgMdMCw :Hauptablteilung + Beleghebamme: Bewertungsrelation
     */
    @Column(name = "DRG_MDM_CW", precision = 10, scale = 3, nullable = false)
    public BigDecimal getDrgMdMCw() {
        return drgMdMCw;
    }

    public void setDrgMdMCw(BigDecimal drgMdCw) {
        this.drgMdMCw = drgMdCw;
    }

    /**
     *
     * @return drgMdCwDeduction Hauptabteilung: Abschlagsbewertungsrelation pro
     * Tag
     */
    @Column(name = "DRG_MD_CW_DEDUCTION", precision = 10, scale = 3, nullable = false)
    public BigDecimal getDrgMdCwDeduction() {
        return drgMdCwDeduction;
    }

    /**
     *
     * @param drgMdCwDeduction Column DRG_MD_CW_DEDUCTION Hauptabteilung:
     * Abschlagsbewertungsrelation pro Tag
     */
    public void setDrgMdCwDeduction(BigDecimal drgMdCwDeduction) {
        this.drgMdCwDeduction = drgMdCwDeduction;
    }

    /**
     *
     * @return drgMdCwSurcharge: Hauptabteilung: Abschlagsbewertungsrelation pro
     * Tag
     */
    @Column(name = "DRG_MD_CW_SURCHARGE", precision = 10, scale = 3, nullable = false)
    public BigDecimal getDrgMdCwSurcharge() {
        return drgMdCwSurcharge;
    }

    /**
     *
     * @param drgMdCwSurcharge Column DRG_MD_CW_SURCHARGE: Hauptabteilung:
     * Abschlagsbewertungsrelation pro Tag
     */
    public void setDrgMdCwSurcharge(BigDecimal drgMdCwSurcharge) {
        this.drgMdCwSurcharge = drgMdCwSurcharge;
    }

    /**
     *
     * @return drgMdCwTransfDeduct: Hauptabteilung: Externe Verlegung
     * Abschlag/Tag (Bewertungsrelation)
     */
    @Column(name = "DRG_MD_CW_TRANSF_DEDUCT", precision = 10, scale = 3, nullable = false)
    public BigDecimal getDrgMdCwTransfDeduct() {
        return drgMdCwTransfDeduct;
    }

    /**
     *
     * @param drgMdCwTransfDeduct Column DRG_MD_CW_TRANSF_DEDUCT Hauptabteilung:
     * Externe Verlegung Abschlag/Tag (Bewertungsrelation)
     */
    public void setDrgMdCwTransfDeduct(BigDecimal drgMdCwTransfDeduct) {
        this.drgMdCwTransfDeduct = drgMdCwTransfDeduct;
    }

    /**
     *
     * @return drgMdIsTransferFl : Hauptabteilung: Verlegungsfallpauschale
     */
    @Column(name = "DRG_MD_IS_TRANSFER_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean getDrgMdIsTransferFl() {
        return drgMdIsTransferFl;
    }

    /**
     *
     * @param drgMdIsTransferFl Column DRG_MD_IS_TRANSFER_FL : Hauptabteilung:
     * Verlegungsfallpauschale
     */
    public void setDrgMdIsTransferFl(boolean drgMdIsTransferFl) {
        this.drgMdIsTransferFl = drgMdIsTransferFl;
    }

    /**
     *
     * @return drgMdIsReadmFl :Hauptabteilung: Ausnahme von Wiederaufnahme
     */
    @Column(name = "DRG_MD_IS_READM_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean getDrgMdIsReadmFl() {
        return drgMdIsReadmFl;
    }

    /**
     *
     * @param drgMdIsReadmFl Column DRG_MD_IS_READM_FL :Hauptabteilung: Ausnahme
     * von Wiederaufnahme
     */
    public void setDrgMdIsReadmFl(boolean drgMdIsReadmFl) {
        this.drgMdIsReadmFl = drgMdIsReadmFl;
    }

    /**
     *
     * @return drgEo1DeductionDay :Belegabteilung: Erster Tag mit Abschlag
     */
    @Column(name = "DRG_EO_1_DEDUCTION_DAY", precision = 3, scale = 0, nullable = false)
    public int getDrgEo1DeductionDay() {
        return drgEo1DeductionDay;
    }

    /**
     *
     * @param drgEo1DeductionDay Column DRG_EO_1_DEDUCTION_DAY :Belegabteilung:
     * Erster Tag mit Abschlag
     */
    public void setDrgEo1DeductionDay(int drgEo1DeductionDay) {
        this.drgEo1DeductionDay = drgEo1DeductionDay;
    }

    /**
     *
     * @return drgEo1SurchargeDay :Belegabteilung: Erster Tag mit Zusatzentgelt
     */
    @Column(name = "DRG_EO_1_SURCHARGE_DAY", precision = 3, scale = 0, nullable = false)
    public int getDrgEo1SurchargeDay() {
        return drgEo1SurchargeDay;
    }

    /**
     *
     * @param drgEo1SurchargeDay Column DRG_EO_1_SURCHARGE_DAY :Belegabteilung:
     * Erster Tag mit Zusatzentgelt
     */
    public void setDrgEo1SurchargeDay(int drgEo1SurchargeDay) {
        this.drgEo1SurchargeDay = drgEo1SurchargeDay;
    }

    /**
     *
     * @return drgEoAlos : Belegabteilung: Mittlere Verweildauer
     */
    @Column(name = "DRG_EO_ALOS", precision = 5, scale = 1, nullable = false)
    public BigDecimal getDrgEoAlos() {
        return drgEoAlos;
    }

    /**
     *
     * @param drgEoAlos Column DRG_EO_ALOS: Belegabteilung: Mittlere
     * Verweildauer
     */
    public void setDrgEoAlos(BigDecimal drgEoAlos) {
        this.drgEoAlos = drgEoAlos;
    }

    /**
     *
     * @return drgEoCw :Belegoperateur: Bewertungsrelation
     */
    @Column(name = "DRG_EO_CW", precision = 10, scale = 3, nullable = false)
    public BigDecimal getDrgEoCw() {
        return drgEoCw;
    }

    /**
     *
     * @param drgEoCw Column DRG_EO_CW :Belegoperateur: Bewertungsrelation
     */
    public void setDrgEoCw(BigDecimal drgEoCw) {
        this.drgEoCw = drgEoCw;
    }

    /**
     *
     * @return drgEoaCw :Belegabteilung: Operateur + Abnastesist: CW
     */
    @Column(name = "DRG_EOA_CW", precision = 10, scale = 3, nullable = false)
    public BigDecimal getDrgEoaCw() {
        return drgEoaCw;
    }

    /**
     *
     * @param drgEoaCw Column DRG_EOA_CW :Belegabteilung: Operateur +
     * Abnastesist: CW
     */
    public void setDrgEoaCw(BigDecimal drgEoaCw) {
        this.drgEoaCw = drgEoaCw;
    }

    /**
     *
     * @return drgEomCw: Belegabteilung: Operateur + Hebamme: CW
     */
    @Column(name = "DRG_EOM_CW", precision = 10, scale = 3, nullable = false)
    public BigDecimal getDrgEomCw() {
        return drgEomCw;
    }

    /**
     *
     * @param drgEomCw Column DRG_EOM_CW: Belegabteilung: Operateur + Hebamme:
     * CW
     */
    public void setDrgEomCw(BigDecimal drgEomCw) {
        this.drgEomCw = drgEomCw;
    }

    /**
     *
     * @return DdrgEoamCw : Belegabteilung: Operateur + Abnastesist + Hebamme:
     * CW
     */
    @Column(name = "DRG_EOAM_CW", precision = 10, scale = 3, nullable = false)
    public BigDecimal getDrgEoamCw() {
        return drgEoamCw;
    }

    /**
     *
     * @param drgEoamCw Column DRG_EOAM_CW : Belegabteilung: Operateur +
     * Abnastesist + Hebamme: CW
     */
    public void setDrgEoamCw(BigDecimal drgEoamCw) {
        this.drgEoamCw = drgEoamCw;
    }

    /**
     *
     * @return drgEoCwDeduction: Belegabteilung: Abschlagsbewertungsrelation pro
     * Tag
     */
    @Column(name = "DRG_EO_CW_DEDUCTION", precision = 10, scale = 3, nullable = false)
    public BigDecimal getDrgEoCwDeduction() {
        return drgEoCwDeduction;
    }

    /**
     *
     * @param drgEoCwDeduction Column DRG_EO_CW_DEDUCTION: Belegabteilung:
     * Abschlagsbewertungsrelation pro Tag
     */
    public void setDrgEoCwDeduction(BigDecimal drgEoCwDeduction) {
        this.drgEoCwDeduction = drgEoCwDeduction;
    }

    /**
     *
     * @return drgEoCwSurcharge: Belegabteilung: Abschlagsbewertungsrelation pro
     * Tag
     */
    @Column(name = "DRG_EO_CW_SURCHARGE", precision = 10, scale = 3, nullable = false)
    public BigDecimal getDrgEoCwSurcharge() {
        return drgEoCwSurcharge;
    }

    /**
     *
     * @param drgEoCwSurcharge Column DRG_EO_CW_SURCHARGE : Belegabteilung:
     * Abschlagsbewertungsrelation pro Tag
     */
    public void setDrgEoCwSurcharge(BigDecimal drgEoCwSurcharge) {
        this.drgEoCwSurcharge = drgEoCwSurcharge;
    }

    /**
     *
     * @return drgEoCwTransfDeduct :Belegabteilung: Externe Verlegung
     * Abschlag/Tag (Bewertungsrelation)
     */
    @Column(name = "DRG_EO_CW_TRANSF_DEDUCT", precision = 10, scale = 3, nullable = false)
    public BigDecimal getDrgEoCwTransfDeduct() {
        return drgEoCwTransfDeduct;
    }

    /**
     *
     * @param drgEoCwTransfDeduct Column DRG_EO_CW_TRANSF_DEDUCT Belegabteilung:
     * Externe Verlegung Abschlag/Tag (Bewertungsrelation)
     */
    public void setDrgEoCwTransfDeduct(BigDecimal drgEoCwTransfDeduct) {
        this.drgEoCwTransfDeduct = drgEoCwTransfDeduct;
    }

    /**
     *
     * @return drgEoIsTransferFl: Hauptabteilung: Verlegungsfallpauschale
     */
    @Column(name = "DRG_EO_IS_TRANSFER_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean getDrgEoIsTransferFl() {
        return drgEoIsTransferFl;
    }

    /**
     *
     * @param drgEoIsTransferFl Column DRG_EO_IS_TRANSFER_FL: Hauptabteilung:
     * Verlegungsfallpauschale
     */
    public void setDrgEoIsTransferFl(boolean drgEoIsTransferFl) {
        this.drgEoIsTransferFl = drgEoIsTransferFl;
    }

    /**
     *
     * @return drgEoIsReadmFl : Belegabteilung: Ausnahme von Wiederaufnahme
     */
    @Column(name = "DRG_EO_IS_READM_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean getDrgEoIsReadmFl() {
        return drgEoIsReadmFl;
    }

    @Column(name = "DRG_MD_CARE_CW_DAY", precision = 10, scale = 4, nullable = false)
    public BigDecimal getDrgMdCareCwDay() {
        return drgMdCareCwDay;
    }

    @Column(name = "DRG_EO_CARE_CW_DAY", precision = 10, scale = 4, nullable = false)
    public BigDecimal getDrgEoCareCwDay() {
        return drgEoCareCwDay;
    }

    public void setDrgMdCareCwDay(BigDecimal drgMdCareCwDay) {
        this.drgMdCareCwDay = drgMdCareCwDay;
    }

    public void setDrgEoCareCwDay(BigDecimal drgEoCareCwDay) {
        this.drgEoCareCwDay = drgEoCareCwDay;
    }

    /**
     *
     * @param drgEoIsReadmFl Column DRG_EO_IS_READM_FL : Belegabteilung:
     * Ausnahme von Wiederaufnahme
     */
    public void setDrgEoIsReadmFl(boolean drgEoIsReadmFl) {
        this.drgEoIsReadmFl = drgEoIsReadmFl;
    }

    /**
     * gibt die Darstellung des Objektes in dem Format drg drgm - Datei aus
     *
     * @return ein DRGM - String
     */
    @Transient
    @Override
    public String get2DrgmMapping() {
//        try {

        StringBuilder br = new StringBuilder();
        br.append(String.valueOf(getId())); //id
        br.append(';');
        br.append("\"" + (getDrgHosIdent() == null ? "" : getDrgHosIdent()) + "\""); //ikz
        br.append(';');
        br.append(String.valueOf(GrouperInterfaceBasic.getModelIdByYear(getDrgYear()))); //DRG Model ID
        br.append(';');
        br.append("\"" + (this.getDrgDrg() == null ? "" : getDrgDrg()) + "\""); //DRG
        br.append(';');

        br.append(checkFloat4Mapping(DRG, this.getDrgMdCw())); //ha_cw
        br.append(';');
        br.append(checkFloat4Mapping(DRG, this.getDrgMdMCw())); //ha_cw_heb
        br.append(';');
        br.append(String.valueOf(this.getDrgMd1DeductionDay())); //ha_1tag_mit_abschlag
        br.append(';');
        br.append(checkFloat4Mapping(DRG, this.getDrgMdCwDeduction())); //ha_cw_abschlag_jt
        br.append(';');
        br.append(String.valueOf(this.getDrgMd1SurchargeDay())); //ha_1tag_mit_zuschlag
        br.append(';');
        br.append(checkFloat4Mapping(DRG, this.getDrgMdCwSurcharge())); //ha_cw_zuschlag_jt
        br.append(';');
        br.append(checkFloat4Mapping(DRG, this.getDrgMdCwTransfDeduct())); //ha_cw_abschlag_verl_jt
        br.append(';');
        br.append(this.getDrgMdIsTransferFl() ? '1' : '0'); //ha_verl_pauschale
        br.append(';');
        br.append(this.getDrgMdIsReadmFl() ? '1' : '0'); //ha_wiederaufn_ausnahme
        br.append(';');

        br.append(checkFloat4Mapping(DRG, this.getDrgEoCw())); //ba_cw_op
        br.append(';');
        br.append(checkFloat4Mapping(DRG, this.getDrgEoaCw())); //ba_cw_op_ana
        br.append(';');
        br.append(checkFloat4Mapping(DRG, this.getDrgEomCw())); //ba_cw_op_heb
        br.append(';');
        br.append(checkFloat4Mapping(DRG, this.getDrgEoamCw())); //ba_cw_op_ana_heb
        br.append(';');
        br.append(this.getDrgEo1DeductionDay()); //ba_1tag_mit_abschlag
        br.append(';');
        br.append(checkFloat4Mapping(DRG, this.getDrgEoCwDeduction())); //ba_cw_abschlag_jt
        br.append(';');
        br.append(this.getDrgEo1SurchargeDay()); //ba_1tag_mit_zuschlag
        br.append(';');
        br.append(checkFloat4Mapping(DRG, this.getDrgEoCwSurcharge())); //ba_cw_zuschlag_jt
        br.append(';');
        br.append(checkFloat4Mapping(DRG, this.getDrgEoCwTransfDeduct())); //ba_cw_abschlag_verl_jt
        br.append(';');
        br.append(this.getDrgEoIsTransferFl() ? '1' : '0'); //ba_verl_pauschale
        br.append(';');
        br.append(this.getDrgEoIsReadmFl() ? '1' : '0'); //ba_wiederaufn_ausnahme
        br.append(';');

        br.append(checkFloat4Mapping(DRG, this.getDrgMdAlos())); //alos
        br.append(';');
        br.append(this.getDrgIsNegotiatedFl() ? '1' : '0');
        br.append(";\"");
        br.append(checkDate4Mapping(this.getDrgValidFrom()));
        br.append("\";");
        br.append(this.getDrgIsNegotiatedFl() ? '1' : '0');
        br.append(';');
        br.append(this.getDrgIsDayCareFl() ? '1' : '0');
        br.append(";");
        br.append(this.getDrgPartitionEn().name());
        br.append(";\"");
        br.append(checkDate4Mapping(this.getDrgValidTo()));
        br.append("\";");
        br.append(checkFloat4Mapping(DRG, this.getDrgEoAlos())); //alos_BA
        br.append(";");
        br.append(getDrgNegoDayFee()); //TGE
        br.append(";");
        br.append(this.getDrgMdMedianCaseCount()); //ha_medialCaseCount
        br.append(";");
        br.append(this.getDrgEoMedianCaseCount()); //ba_medialCaseCount
        br.append(";");
        br.append(getDrgMdCareCwDay() == null ? "0.0" : this.getDrgMdCareCwDay().floatValue()); //ha_care_cw_day
        br.append(";");
        br.append(getDrgEoCareCwDay() == null ? "0.0" : this.getDrgEoCareCwDay().floatValue()); //ba_care_cw_day
        br.append('\r');
        br.append('\n');
        return br.toString();
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Error on creating of the DRGM String for drg ", ex);
//
//        }
//        return null;
    }

    @Override
    protected boolean showDrgmDate() {
        return getDrgIsNegotiatedFl() && getDrgHosIdent() != null && !getDrgHosIdent().isEmpty();
    }

    public boolean useCareCw() {
        return this.getDrgYear() >= 2020;
    }

    @Transient
    @Override
    public String getCode() {
        return this.getDrgDrg();
    }

    @Transient
    @Override
    public Date getValidFrom() {
        return getDrgValidFrom();
    }

    @Transient
    @Override
    public Date getValidTo() {
        return getDrgValidTo();
    }
    @Transient
    @Override
    public String getIk() {
        return this.getDrgHosIdent();
    }
    
    @Transient
    @Override
    public int getCatalogYear(){
        return this.getDrgYear();
    }
    
    
    @Transient
    @Override
    public String getAdditionalCode(){
        return "";
    }
    
   
    
}
