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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 * CFeeCatalog initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">C_FEE_CATALOG : Kataloge der
 * Entgelte.</p>
 *
 *
 */
@Entity
@Table(name = "C_FEE_CATALOG")
@SuppressWarnings("serial")
@SecurityDomain("cpx")
public class CFeeCatalog extends AbstractCatalogEntity {

    private static final long serialVersionUID = 1L;

//     private long id;
    private CHospital CHospital;
    private CCatalog CCatalog;
    private String feeCatCategoryEn;
    private String CSupplTypeEn;
    private boolean feeCatIsNegotiatedFl;
    private boolean feeCatIsDayCareFl;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_FEE_CATALOG_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return CHospital: Referenz auf die Tabelle C_Hospital .
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HOSPITAL_ID")
    public CHospital getCHospital() {
        return this.CHospital;
    }

    /**
     *
     * @param CHospital Column HOSPITAL_ID: Referenz auf die Tabelle C_Hospital
     * .
     */
    public void setCHospital(CHospital CHospital) {
        this.CHospital = CHospital;
    }

    /**
     *
     * @return CCatalog : Referenz auf die Tabelle C_Catalog.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATALOG_ID", nullable = false)
    public CCatalog getCCatalog() {
        return this.CCatalog;
    }

    /**
     *
     * @param CCatalog Column CATALOG_ID : Referenz auf die Tabelle C_Catalog.
     */
    public void setCCatalog(CCatalog CCatalog) {
        this.CCatalog = CCatalog;
    }

    @Column(name = "FEE_CAT_CATEGORY_EN", nullable = false, length = 10)
    public String getFeeCatCategoryEn() {
        return this.feeCatCategoryEn;
    }

    public void setFeeCatCategoryEn(String feeCatCategoryEn) {
        this.feeCatCategoryEn = feeCatCategoryEn;
    }

    /**
     *
     * @return CSupplTypeEn: Enumeration für Art des Zusatzentgelt 1-3 :
     * ZE(Zusatzentgelt ) , ZP(Zusatzentgelt PEPP) , ET(Tagesentgelt)...
     */
    @Column(name = "C_SUPPL_TYPE_EN", nullable = false, length = 25)
    public String getCSupplTypeEn() {
        return this.CSupplTypeEn;
    }

    /**
     *
     * @param CSupplTypeEn Column C_SUPPL_TYPE_EN: Enumeration für Art des
     * Zusatzentgelt 1-3 : ZE(Zusatzentgelt ) , ZP(Zusatzentgelt PEPP) ,
     * ET(Tagesentgelt)...
     */
    public void setCSupplTypeEn(String CSupplTypeEn) {
        this.CSupplTypeEn = CSupplTypeEn;
    }

    /**
     *
     * @return feeCatIsNegotiatedFl: Verhandelbar Ja/Nein 1/0.
     */
    @Column(name = "FEE_CAT_IS_NEGOTIATED_FL", nullable = false, precision = 1, scale = 0)
    public boolean isFeeCatIsNegotiatedFl() {
        return this.feeCatIsNegotiatedFl;
    }

    /**
     *
     * @param feeCatIsNegotiatedFl Column FEE_CAT_IS_NEGOTIATED_FL: Verhandelbar
     * Ja/Nein 1/0.
     */
    public void setFeeCatIsNegotiatedFl(boolean feeCatIsNegotiatedFl) {
        this.feeCatIsNegotiatedFl = feeCatIsNegotiatedFl;
    }

    /**
     *
     * @return feeCatIsDayCareFl: Teilstationär Ja/Nein: 1/0.
     */
    @Column(name = "FEE_CAT_IS_DAY_CARE_FL", nullable = false, precision = 1, scale = 0)
    public boolean isFeeCatIsDayCareFl() {
        return this.feeCatIsDayCareFl;
    }

    /**
     *
     * @param feeCatIsDayCareFl Column FEE_CAT_IS_DAY_CARE_FL : Teilstationär
     * Ja/Nein: 1/0.
     */
    public void setFeeCatIsDayCareFl(boolean feeCatIsDayCareFl) {
        this.feeCatIsDayCareFl = feeCatIsDayCareFl;
    }

}
