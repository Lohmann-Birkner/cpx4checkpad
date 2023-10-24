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
 * COpsCatalog initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">C_OPS_CATALOG: Kataloge der OPSs
 * (Operationen- und Prozedurenschlüssel) </p>
 */
@Entity
@Table(name = "C_OPS_CATALOG"
)
@SuppressWarnings("serial")
public class COpsCatalog extends AbstractCatalogEntity {

    private static final long serialVersionUID = 1L;

    //private long id;
    private COpsCatalog COpsCatalog;
    private CountryEn countryEn;
    private int opsYear;
    private String opsCode;
    private String opsDescription;
    private Boolean opsIsCompleteFl;
    private String opsInclusion;
    private String opsExclusion;
    private String opsNote;
    private int opsDepth;
    private Set<COpsCatalog> COpsCatalogs = new HashSet<>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_OPS_CATALOG_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return COpsCatalog:Referenz auf den Übergeordneter Knoten in den selben
     * Tabelle C_ICD_CATALOG
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OPS_PARENT_ID")
    public COpsCatalog getCOpsCatalog() {
        return this.COpsCatalog;
    }

    /**
     *
     * @param COpsCatalog OPS_PARENT_ID:Referenz auf den Übergeordneter Knoten
     * in den selben Tabelle C_ICD_CATALOG
     */
    public void setCOpsCatalog(COpsCatalog COpsCatalog) {
        this.COpsCatalog = COpsCatalog;
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
     * @return opsCode: Kode der OPS oder OPS- Gruoppe
     */
    @Column(name = "OPS_CODE", nullable = false, length = 15)
    public String getOpsCode() {
        return this.opsCode;
    }

    /**
     *
     * @param opsCode Column OPS_CODE: Kode der OPS oder OPS- Gruoppe
     */
    public void setOpsCode(String opsCode) {
        this.opsCode = opsCode;
    }

    /**
     *
     * @return opsDescription: OPS - Definition
     */
    @Lob
    @Column(name = "OPS_DESCRIPTION")
    public String getOpsDescription() {
        return this.opsDescription;
    }

    /**
     *
     * @param opsDescription Column OPS_DESCRIPTION :OPS - Definition
     */
    public void setOpsDescription(String opsDescription) {
        this.opsDescription = opsDescription;
    }

    /**
     *
     * @return opsIsCompleteFl
     */
    @Column(name = "OPS_IS_COMPLETE_FL", precision = 1, scale = 0)
    public Boolean getOpsIsCompleteFl() {
        return this.opsIsCompleteFl;
    }

    /**
     *
     * @param opsIsCompleteFl Column OPS_IS_COMPLETE_FL: 1/0 Flag -
     * Vollständiger Code
     */
    public void setOpsIsCompleteFl(Boolean opsIsCompleteFl) {
        this.opsIsCompleteFl = opsIsCompleteFl;
    }

    /**
     *
     * @return opsInclusion: Inklusions - Text
     */
    @Lob
    @Column(name = "OPS_INCLUSION")
    public String getOpsInclusion() {
        return this.opsInclusion;
    }

    /**
     *
     * @param opsInclusion Column OPS_INCLUSION :Inklusions - Text
     */
    public void setOpsInclusion(String opsInclusion) {
        this.opsInclusion = opsInclusion;
    }

    /**
     *
     * @return opsExclusion :Exclusionstext
     */
    @Lob
    @Column(name = "OPS_EXCLUSION")
    public String getOpsExclusion() {
        return this.opsExclusion;
    }

    /**
     *
     * @param opsExclusion Column OPS_EXCLUSION: Exclusionstext
     */
    public void setOpsExclusion(String opsExclusion) {
        this.opsExclusion = opsExclusion;
    }

    /**
     *
     * @return opsNote :Bemerkungstext
     */
    @Lob
    @Column(name = "OPS_NOTE")
    public String getOpsNote() {
        return this.opsNote;
    }

    /**
     *
     * @param opsNote Column OPS_NOTE Bemerkungstext
     */
    public void setOpsNote(String opsNote) {
        this.opsNote = opsNote;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "COpsCatalog")
    public Set<COpsCatalog> getCOpsCatalogs() {
        return this.COpsCatalogs;
    }

    public void setCOpsCatalogs(Set<COpsCatalog> COpsCatalogs) {
        this.COpsCatalogs = COpsCatalogs;
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
    @Column(name = "OPS_YEAR", nullable = false, length = 4)
    public int getOpsYear() {
        return opsYear;
    }

    public void setOpsYear(final int opsYear) {
        this.opsYear = opsYear;
    }

    @Column(name = "OPS_DEPTH", nullable = false)
    public int getOpsDepth() {
        return opsDepth;
    }

    public void setOpsDepth(Integer opsDepth) {
        this.opsDepth = (opsDepth == null) ? 0 : opsDepth;
    }

}
