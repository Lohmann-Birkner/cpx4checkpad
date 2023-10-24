/**
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

import de.lb.cpx.model.enums.CountryEn;
import de.lb.cpx.server.commons.dao.AbstractCatalogEntity;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * CIcdCatalog initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">C_ICD_CATALOG: Kataloge der ICDs
 * (International Classification of Diseases)</p>
 */
@Entity
@Table(name = "C_ICD_CATALOG"
)
@SuppressWarnings("serial")
public class CIcdCatalog extends AbstractCatalogEntity {

    private static final long serialVersionUID = 1L;

    //private long id;
    private CIcdCatalog CIcdCatalog;
    private CountryEn countryEn;
    private int icdYear;
    private String icdCode;
    private String icdDescription;
    private Boolean icdIsCompleteFl;
    private String icdInclusion;
    private String icdExclusion;
    private String icdNote;
    private int icdDepth;
    private Set<CIcdCatalog> CIcdCatalogs = new HashSet<>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_ICD_CATALOG_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return CIcdCatalog :Referenz auf den Übergeordneter Knoten in der
     * Tabelle C_ICD_CATALOG
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ICD_PARENT_ID")
    public CIcdCatalog getCIcdCatalog() {
        return this.CIcdCatalog;
    }

    /**
     *
     * @param CIcdCatalog Column ICD_PARENT_ID :Referenz auf den Übergeordneter
     * Knoten in der selben Tabelle C_ICD_CATALOG
     */
    public void setCIcdCatalog(CIcdCatalog CIcdCatalog) {
        this.CIcdCatalog = CIcdCatalog;
    }

    @Column(name = "COUNTRY_EN", length = 25)
    @Enumerated(EnumType.STRING)
    public CountryEn getCountryEn() {
        return countryEn;
    }

    public void setCountryEn(CountryEn countryEn) {
        this.countryEn = countryEn;
    }

    /**
     *
     * @return icdCode: ICD - Code
     */
    @Column(name = "ICD_CODE", nullable = false, length = 10)
    public String getIcdCode() {
        return this.icdCode;
    }

    /**
     *
     * @param icdCode Column ICD_CODE
     */
    public void setIcdCode(String icdCode) {
        this.icdCode = icdCode;
    }

    /**
     *
     * @return icdDescription: ICD Bezeichnung
     */
    @Lob
    @Column(name = "ICD_DESCRIPTION")
    public String getIcdDescription() {
        return this.icdDescription;
    }

    /**
     *
     * @param icdDescription Column ICD_DESCRIPTION: ICD Bezeichnung
     */
    public void setIcdDescription(String icdDescription) {
        this.icdDescription = icdDescription;
    }

    /**
     *
     * @return icdIsCompleteFl : 1/0 - Flag Vollständuger Code
     */
    @Column(name = "ICD_IS_COMPLETE_FL", precision = 1, scale = 0)
    public Boolean getIcdIsCompleteFl() {
        return this.icdIsCompleteFl;
    }

    /**
     *
     * @param icdIsCompleteFl ICD_IS_COMPLETE_FL : 1/0 - Flag Vollständuger Code
     */
    public void setIcdIsCompleteFl(Boolean icdIsCompleteFl) {
        this.icdIsCompleteFl = icdIsCompleteFl;
    }

    /**
     *
     * @return icdInclusion: Inklusionstext
     */
    @Lob
    @Column(name = "ICD_INCLUSION")
    public String getIcdInclusion() {
        return this.icdInclusion;
    }

    /**
     *
     * @param icdInclusion Column ICD_INCLUSION: Inklusionstext
     */
    public void setIcdInclusion(String icdInclusion) {
        this.icdInclusion = icdInclusion;
    }

    /**
     *
     * @return icdExclusion: Exclusionstext
     */
    @Lob
    @Column(name = "ICD_EXCLUSION")
    public String getIcdExclusion() {
        return this.icdExclusion;
    }

    /**
     *
     * @param icdExclusion Column ICD_EXCLUSION: Exclusionstext
     */
    public void setIcdExclusion(String icdExclusion) {
        this.icdExclusion = icdExclusion;
    }

    /**
     *
     * @return icdNote: Bemerkungstext
     */
    @Lob
    @Column(name = "ICD_NOTE")
    public String getIcdNote() {
        return this.icdNote;
    }

    /**
     *
     * @param icdNote Column ICD_NOTE: Bemerkungstext
     */
    public void setIcdNote(String icdNote) {
        this.icdNote = icdNote;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "CIcdCatalog")
    public Set<CIcdCatalog> getCIcdCatalogs() {
        return this.CIcdCatalogs;
    }

    public void setCIcdCatalogs(Set<CIcdCatalog> CIcdCatalogs) {
        this.CIcdCatalogs = CIcdCatalogs;
    }

    /**
     *
     * @return icdYear: Jahresgrouper
     */
    @Column(name = "ICD_YEAR", nullable = false, length = 4)
    public int getIcdYear() {
        return icdYear;
    }

    /**
     *
     * @param icdYear Column ICD_YEAR: Jahresgrouper
     */
    public void setIcdYear(final int icdYear) {
        this.icdYear = icdYear;
    }

    /*
    @Column(name = "MODEL_ID_EN", nullable = false, length = 25)
    @Enumerated(EnumType.STRING)
    public GDRGModel getModelIdEn() {
      return modelIdEn;
    }
    public void setModelIdEn(final GDRGModel modelIdEn) {
      this.modelIdEn = modelIdEn;
    }
     */
    /**
     *
     * @return icdDepth :Tief des ICD in der ICD_CODE(Tief wurde nach "-"
     * gerechnet) .
     */
    @Column(name = "ICD_DEPTH", nullable = false)
    public int getIcdDepth() {
        return icdDepth;
    }

    /**
     *
     * @param icdDepth Column ICD_DEPTH : Tief des ICD in der ICD_CODE(Tief
     * wurde nach "-" gerechnet) .
     */
    public void setIcdDepth(Integer icdDepth) {
        this.icdDepth = (icdDepth == null) ? 0 : icdDepth;
    }

}
