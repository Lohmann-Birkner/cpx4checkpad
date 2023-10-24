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
@Table(name = "C_ICD_THESAURUS")
@SuppressWarnings("serial")
public class CIcdThesaurus extends AbstractCatalogEntity {

    private static final long serialVersionUID = 1L;

    private CountryEn countryEn;
    private int typeOfCode;
    private int dimdiInternalNo;
    private boolean printFl;
    private String primKeyNo1;
    private String starKeyNo;
    private String addKeyNo;
    private String primKeyNo2;
    private String description;
    private String reference;
    private int icdYear;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_ICD_THESAURUS_SQ", allocationSize = 1)
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
     * Kodierung nur mit einer Primärschlüsselnummer 2 = Kodierung mit einer
     * Kreuz- und einer Sternschlüsselnummer 3 = Kodierung mit einer
     * Primärschlüsselnummer und mit einer Zusatzschlüsselnummer mit
     * Ausrufezeichen 4 = Kodierung mit einer Kreuz-, einer Stern- und einer
     * Zusatzschlüsselnummer 5 = Kodierung nur als Zusatzschlüsselnummer möglich
     * 6 = Kodierung mit zwei Primärschlüsselnummern
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
     * Kodierung nur mit einer Primärschlüsselnummer 2 = Kodierung mit einer
     * Kreuz- und einer Sternschlüsselnummer 3 = Kodierung mit einer
     * Primärschlüsselnummer und mit einer Zusatzschlüsselnummer mit
     * Ausrufezeichen 4 = Kodierung mit einer Kreuz-, einer Stern- und einer
     * Zusatzschlüsselnummer 5 = Kodierung nur als Zusatzschlüsselnummer möglich
     * 6 = Kodierung mit zwei Primärschlüsselnummern
     *
     * @param typeOfCode Feld 1: Art der Kodierung
     */
    public void setTypeOfCode(int typeOfCode) {
        this.typeOfCode = typeOfCode;
    }

    /**
     * Feld 2: DIMDI-interne fortlaufende Nummer
     *
     * @return Feld 2: DIMDI-interne fortlaufende Nummer
     */
    @Column(name = "DIMDI_INTERNAL_NO", nullable = false)
    public int getDimdiInternalNo() {
        return dimdiInternalNo;
    }

    /**
     * Feld 2: DIMDI-interne fortlaufende Nummer
     *
     * @param dimdiInternalNo Feld 2: DIMDI-interne fortlaufende Nummer
     */
    public void setDimdiInternalNo(int dimdiInternalNo) {
        this.dimdiInternalNo = dimdiInternalNo;
    }

    /**
     * Feld 3: Druckkennzeichen (0 = Satz erscheint nicht in der Buchversion, 1
     * = Satz erscheint in der Buchversion
     *
     * @return Feld 3: Druckkennzeichen
     */
    @Column(name = "IS_PRINT_FL", nullable = false)
    public boolean isPrintFl() {
        return printFl;
    }

    /**
     * Feld 3: Druckkennzeichen (0 = Satz erscheint nicht in der Buchversion, 1
     * = Satz erscheint in der Buchversion
     *
     * @param printFl Feld 3: Druckkennzeichen
     */
    public void setPrintFl(boolean printFl) {
        this.printFl = printFl;
    }

    /**
     * Feld 4: Primärschlüsselnummer 1 (ggf. mit Kreuz)
     *
     * @return Feld 4: Primärschlüsselnummer 1 (ggf. mit Kreuz)
     */
    @Column(name = "PRIM_KEY_NO_1", length = 10, nullable = true)
    public String getPrimKeyNo1() {
        return primKeyNo1;
    }

    /**
     * Feld 4: Primärschlüsselnummer 1 (ggf. mit Kreuz)
     *
     * @param primKeyNo1 Feld 4: Primärschlüsselnummer 1 (ggf. mit Kreuz)
     */
    public void setPrimKeyNo1(String primKeyNo1) {
        this.primKeyNo1 = primKeyNo1;
    }

    /**
     * Feld 5: Sternschlüsselnummer (mit Stern)
     *
     * @return Feld 5: Sternschlüsselnummer (mit Stern)
     */
    @Column(name = "STAR_KEY_NO", length = 10)
    public String getStarKeyNo() {
        return starKeyNo;
    }

    /**
     * Feld 5: Sternschlüsselnummer (mit Stern)
     *
     * @param starKeyNo Feld 5: Sternschlüsselnummer (mit Stern)
     */
    public void setStarKeyNo(String starKeyNo) {
        this.starKeyNo = starKeyNo;
    }

    /**
     * Feld 6: Zusatzschlüsselnummer (mit Ausrufezeichen)
     *
     * @return Feld 6: Zusatzschlüsselnummer (mit Ausrufezeichen)
     */
    @Column(name = "ADD_KEY_NO", length = 10)
    public String getAddKeyNo() {
        return addKeyNo;
    }

    /**
     * Feld 6: Zusatzschlüsselnummer (mit Ausrufezeichen)
     *
     * @param addKeyNo Feld 6: Zusatzschlüsselnummer (mit Ausrufezeichen)
     */
    public void setAddKeyNo(String addKeyNo) {
        this.addKeyNo = addKeyNo;
    }

    /**
     * Feld 7: Primärschlüsselnummer 2 (ggf. mit Kreuz)
     *
     * @return Feld 7: Primärschlüsselnummer 2 (ggf. mit Kreuz)
     */
    @Column(name = "PRIM_KEY_NO_2", length = 10)
    public String getPrimKeyNo2() {
        return primKeyNo2;
    }

    /**
     * Feld 7: Primärschlüsselnummer 2 (ggf. mit Kreuz)
     *
     * @param primKeyNo2 Feld 7: Primärschlüsselnummer 2 (ggf. mit Kreuz)
     */
    public void setPrimKeyNo2(String primKeyNo2) {
        this.primKeyNo2 = primKeyNo2;
    }

    /**
     * Feld 8 (a): zugehöriger Text
     *
     * @return Feld 8 (a): zugehöriger Text
     */
    @Column(name = "DESCRIPTION", length = 255)
    public String getDescription() {
        return description;
    }

    /**
     * Feld 8 (a): zugehöriger Text
     *
     * @param description Feld 8 (a): zugehöriger Text
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Feld 8 (b): ggf. mit Verweis
     *
     * @return Feld 8 (b): ggf. mit Verweis
     */
    @Column(name = "REFERENCE", length = 255)
    public String getReference() {
        return reference;
    }

    /**
     * Feld 8 (b): ggf. mit Verweis
     *
     * @param reference Feld 8: (b)ggf. mit Verweis
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * year
     *
     * @return icdYear
     */
    @Column(name = "ICD_YEAR", nullable = false, length = 4)
    public int getIcdYear() {
        return icdYear;
    }

    /**
     * year
     *
     * @param icdYear year
     */
    public void setIcdYear(final int icdYear) {
        this.icdYear = icdYear;
    }

}
