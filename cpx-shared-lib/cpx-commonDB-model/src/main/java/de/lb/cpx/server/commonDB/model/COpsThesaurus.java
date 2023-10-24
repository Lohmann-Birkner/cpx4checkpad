/**
 * Copyright (c) 2018 Lohmann & Birkner.
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
 * CHospital initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">C_HOSPITAL : Tabelle der
 * Krankenhäuser .</p>
 *
 */
@Entity
@Table(name = "C_OPS_THESAURUS")
@SuppressWarnings("serial")
public class COpsThesaurus extends AbstractCatalogEntity {

    private static final long serialVersionUID = 1L;

    private CountryEn countryEn;
    private int typeOfCode;
    private int dimdiInternalNo;
    private String keyNo1;
    private String keyNo2;
    private String description;
    private String reference;
    private int opsYear;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_OPS_THESAURUS_SQ", allocationSize = 1)
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
     * Feld 1: Art der Kodierung 0 = reiner Verweissatz Diese Datensätze haben
     * keine Schlüsselnummer, sondern innerhalb des Textfeldes Verweise auf
     * andere Begriffe, unter denen ihre Schlüsselnummern nachgewiesen sind. 1 =
     * Kodierung nur mit einer Schlüsselnummer 2 = Kodierung mit zwei
     * Schlüsselnummern
     *
     * @return Feld 1: Art der Kodierung
     */
    @Column(name = "TYPE_OF_CODE", nullable = false)
    public int getTypeOfCode() {
        return typeOfCode;
    }

    /**
     * Feld 1: Art der Kodierung 0 = reiner Verweissatz Diese Datensätze haben
     * keine Schlüsselnummer, sondern innerhalb des Textfeldes Verweise auf
     * andere Begriffe, unter denen ihre Schlüsselnummern nachgewiesen sind. 1 =
     * Kodierung nur mit einer Schlüsselnummer 2 = Kodierung mit zwei
     * Schlüsselnummern
     *
     * @param typeOfCode Feld 1: Art der Kodierung
     */
    public void setTypeOfCode(int typeOfCode) {
        this.typeOfCode = typeOfCode;
    }

    /**
     * Feld 2: Id (DIMDI-interne Nummer)
     *
     * @return Feld 2: Id (DIMDI-interne Nummer)
     */
    @Column(name = "DIMDI_INTERNAL_NO", nullable = false)
    public int getDimdiInternalNo() {
        return dimdiInternalNo;
    }

    /**
     * Feld 2: Id (DIMDI-interne Nummer)
     *
     * @param dimdiInternalNo Feld 2: Id (DIMDI-interne Nummer)
     */
    public void setDimdiInternalNo(int dimdiInternalNo) {
        this.dimdiInternalNo = dimdiInternalNo;
    }

    /**
     * Feld 3: erste Schlüsselnummer
     *
     * @return Feld 3: erste Schlüsselnummer
     */
    @Column(name = "KEY_NO_1", length = 10, nullable = true)
    public String getKeyNo1() {
        return keyNo1;
    }

    /**
     * Feld 3: erste Schlüsselnummer
     *
     * @param keyNo1 Feld 3: erste Schlüsselnummer
     */
    public void setKeyNo1(String keyNo1) {
        this.keyNo1 = keyNo1;
    }

    /**
     * Feld 4: zweite Schlüsselnummer
     *
     * @return Feld 4: zweite Schlüsselnummer
     */
    @Column(name = "KEY_NO_2", length = 10)
    public String getKeyNo2() {
        return keyNo2;
    }

    /**
     * Feld 4: zweite Schlüsselnummer
     *
     * @param keyNo2 Feld 4: zweite Schlüsselnummer
     */
    public void setKeyNo2(String keyNo2) {
        this.keyNo2 = keyNo2;
    }

    /**
     * Feld 5 (a): zugehöriger Text
     *
     * @return Feld 5 (a): zugehöriger Text
     */
    @Column(name = "DESCRIPTION", length = 255)
    public String getDescription() {
        return description;
    }

    /**
     * Feld 5 (a): zugehöriger Text
     *
     * @param description Feld 5 (a): zugehöriger Text
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Feld 5 (b): zugehöriger Text
     *
     * @return Feld 5 (b): zugehöriger Text
     */
    @Column(name = "REFERENCE", length = 255)
    public String getReference() {
        return reference;
    }

    /**
     * Feld 5 (b): zugehöriger Text
     *
     * @param reference Feld 5 (b): zugehöriger Text
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * year
     *
     * @return opsYear
     */
    @Column(name = "OPS_YEAR", nullable = false, length = 4)
    public int getOpsYear() {
        return opsYear;
    }

    /**
     * year
     *
     * @param opsYear year
     */
    public void setOpsYear(final int opsYear) {
        this.opsYear = opsYear;
    }

}
