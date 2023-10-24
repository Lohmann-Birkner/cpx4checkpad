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

import de.lb.cpx.server.commons.dao.AbstractCatalogEntity;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 * CCatalog initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">C_CATALOG : Tabelle der
 * Kataloges. </p>
 *
 */
@NamedQueries({
    @NamedQuery(
            name = "findCatalogByModelId",
            query = "select catalog from CCatalog catalog join catalog.CCountry country where catalog.modelIdEn = :model_id and country.shortName = :country "
    )
})
@Entity
@Table(name = "C_CATALOG")
@SuppressWarnings("serial")
@SecurityDomain("cpx")
public class CCatalog extends AbstractCatalogEntity {

    private static final long serialVersionUID = 1L;

//     private long id;
    private CCountry CCountry;
    private String modelIdEn;
    //private Set<CIcdCatalog> CIcdCatalogs = new HashSet<>(0);
    private Set<CFeeCatalog> CFeeCatalogs = new HashSet<>(0);
    //private Set<COpsCatalog> COpsCatalogs = new HashSet<>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_CATALOG_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return CCountry : Referenz auf die Tabelle C_COUNTRY
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "C_COUNTRY_ID", nullable = false)
    public CCountry getCCountry() {
        return this.CCountry;
    }

    /**
     * @param CCountry Column C_COUNTRY_ID: Referenz auf die Tabelle C_COUNTRY
     */
    public void setCCountry(CCountry CCountry) {
        this.CCountry = CCountry;
    }

    /**
     * @return modelIdEn: Enumeration für Groupermodell.
     */
    @Column(name = "MODEL_ID_EN", nullable = false, length = 25)
    public String getModelIdEn() {
        return this.modelIdEn;
    }

    /**
     * @param modelIdEn Column MODEL_ID_EN: Enumeration für Groupermodell.
     */
    public void setModelIdEn(String modelIdEn) {
        this.modelIdEn = modelIdEn;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "CCatalog")
    public Set<CFeeCatalog> getCFeeCatalogs() {
        return this.CFeeCatalogs;
    }

    public void setCFeeCatalogs(Set<CFeeCatalog> CFeeCatalogs) {
        this.CFeeCatalogs = CFeeCatalogs;
    }

}
