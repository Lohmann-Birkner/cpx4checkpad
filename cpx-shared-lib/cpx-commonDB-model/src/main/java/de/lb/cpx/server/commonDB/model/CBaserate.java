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
import java.util.Date;
import javax.persistence.Column;
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

/**
 * CBASERATE initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;"> C_BASERATE: Basisrate für
 * Entgelte eines Krankenhauses.</p>
 *
 */
@Entity
@Table(name = "C_BASERATE")
@SuppressWarnings("serial")
public class CBaserate extends AbstractCatalogEntity {

    private static final long serialVersionUID = 1L;

//     private long id;
    private CountryEn countryEn;
    private String baseHosIdent;
    private String baseFeeKey;
    private Double baseFeeValue;
    private String baseCurrency;
    private Date baseValidFrom;
    private Date baseValidTo;
    private Double baseLos;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_BASERATE_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return countryEn: Country Enumeration (de,en)
     */
    @Column(name = "COUNTRY_EN", length = 25)
    @Enumerated(EnumType.STRING)
    public CountryEn getCountryEn() {
        return countryEn;
    }

    /**
     * @param countryEn Column COUNTRY_EN : Country Enumeration (de,en) .
     */
    public void setCountryEn(CountryEn countryEn) {
        this.countryEn = countryEn;
    }

    /**
     * @return baseHosIdent: Referenz auf die tabelle C_HOSPITAL.
     */
    @Column(name = "BASE_HOS_IDENT", nullable = false, length = 10)
    public String getBaseHosIdent() {
        return baseHosIdent;
    }

    /**
     * @param baseHosIdent Column BASE_HOS_IDENT: Referenz auf die tabelle
     * C_HOSPITAL.
     */
    public void setBaseHosIdent(String baseHosIdent) {
        this.baseHosIdent = baseHosIdent;
    }

    /**
     * @return baseFeeKey :Kodierung des Entgeltes
     */
    @Column(name = "BASE_FEE_KEY", nullable = false, length = 20)
    public String getBaseFeeKey() {
        return baseFeeKey;
    }

    /**
     * @param baseFeeKey Column BASE_FEE_KEY :Kodierung des Entgeltes.
     */
    public void setBaseFeeKey(String baseFeeKey) {
        this.baseFeeKey = baseFeeKey;
    }

    /**
     * @return the baseFeeValue: Basisrate für die in BASE_FEE_KEY benannte
     * Kodierung.
     */
    @Column(name = "BASE_FEE_VALUE", nullable = false)
    public Double getBaseFeeValue() {
        return baseFeeValue;
    }

    /**
     * @param baseFeeValue Column BASE_FEE_VALUE: Basisrate für die in
     * BASE_FEE_KEY benannte Kodierung.
     */
    public void setBaseFeeValue(Double baseFeeValue) {
        this.baseFeeValue = baseFeeValue;
    }

    /**
     * @return baseCurrency :Währungsbezeichnung für den Zahlbetrag
     */
    @Column(name = "BASE_CURRENCY", nullable = false, length = 20)
    public String getBaseCurrency() {
        return baseCurrency;
    }

    /**
     * @param baseCurrency Column BASE_CURRENCY: Währungsbezeichnung für den
     * Zahlbetrag .
     */
    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    /**
     * @return baseValidFrom :Datum ab dem das genannte Entgelt gültig ist
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "BASE_VALID_FROM", length = 11)
    public Date getBaseValidFrom() {
        return baseValidFrom == null ? null : new Date(baseValidFrom.getTime());
    }

    /**
     * @param baseValidFrom Column BASE_VALID_FROM: Datum ab dem das genannte
     * Entgelt gültig ist.
     */
    public void setBaseValidFrom(Date baseValidFrom) {
        this.baseValidFrom = baseValidFrom == null ? null : new Date(baseValidFrom.getTime());
    }

    /**
     * @return baseValidTo :Datum bis zu dem das genannte Entgelt gültig ist.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "BASE_VALID_TO", length = 11)
    public Date getBaseValidTo() {
        return baseValidTo == null ? null : new Date(baseValidTo.getTime());
    }

    /**
     * @param baseValidTo Column BASE_VALID_TO: Datum bis zu dem das genannte
     * Entgelt gültig ist.
     */
    public void setBaseValidTo(Date baseValidTo) {
        this.baseValidTo = baseValidTo == null ? null : new Date(baseValidTo.getTime());
    }

    /**
     * @return baseLos: Verweildauer
     */
    @Column(name = "BASE_LOS", length = 11)
    public Double getBaseLos() {
        return baseLos;
    }

    /**
     * @param baseLos Column BASE_LOS :Verweildauer.
     */
    public void setBaseLos(Double baseLos) {
        this.baseLos = baseLos;
    }

    @Override
    public String toString() {
        return "IKZ: " + (this.getBaseHosIdent() == null ? "null" : getBaseHosIdent())
                + ", feeKey: " + (this.getBaseFeeKey() == null ? "null" : getBaseFeeKey())
                + ", value: " + (getBaseFeeValue() == null ? "null" : String.valueOf(getBaseFeeValue()))
                + ", validFrom: " + (this.getBaseValidFrom() == null ? "null" : getBaseValidFrom().toString())
                + ", validTo: " + (this.getBaseValidTo() == null ? "null" : getBaseValidTo().toString());
    }
}
