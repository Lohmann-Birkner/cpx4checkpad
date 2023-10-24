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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * CPZN CDoctor initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">C_PZN : Tabelle der PZN
 * (Pharmazentralnummer)</p>
 */
@Entity
@Table(name = "C_PZN")
@SuppressWarnings("serial")
public class CPzn extends AbstractCatalogEntity {

    private static final long serialVersionUID = 1L;

//     private long id;
    private CountryEn countryEn;
    private String pznIdent;
    private String pznNormSize;
    private String pznDesc;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_PZN_SQ", allocationSize = 1)
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

    /**
     *
     * @return pznIdent :Pharmazentralnummern (PZN) des Medikaments.
     */
    @Column(name = "PZN_IDENT", nullable = false, length = 20)
    public String getPznIdent() {
        return this.pznIdent;
    }

    /**
     *
     * @param pznIdent Column PZN_IDENT :Pharmazentralnummern (PZN) des
     * Medikaments.
     */
    public void setPznIdent(String pznIdent) {
        this.pznIdent = pznIdent;
    }

    /**
     *
     * @return pznNormSize: Angabe der Normgröße N1, N2, N3.
     */
    @Column(name = "PZN_NORMSIZE", length = 20)
    public String getPznNormSize() {
        return this.pznNormSize;
    }

    /**
     *
     * @param pznNormSize Column PZN_NORMSIZE: Angabe der Normgröße N1, N2, N3.
     */
    public void setPznNormSize(String pznNormSize) {
        this.pznNormSize = pznNormSize;
    }

    /**
     *
     * @return pznDesc: Beschreibung/Benennung des Medikamentes zu der PZN.
     */
    @Column(name = "PZN_DESC", length = 255)
    public String getPznDesc() {
        return this.pznDesc;
    }

    /**
     *
     * @param pznDesc Column PZN_DESC : Beschreibung/Benennung des Medikamentes
     * zu der PZN.
     */
    public void setPznDesc(String pznDesc) {
        this.pznDesc = pznDesc;
    }

}
