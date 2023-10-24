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
 * CAtc initially generated at 03.02.2016 10:32:45 by Hibernate Tools 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;"> C_ATC: Tabelle der ATC
 * (Anatomisch-Therapeutisch-Chemisches Klassifikationssystem).</p>
 *
 *
 */
@Entity
@Table(name = "C_ATC")
@SuppressWarnings("serial")
public class CAtc extends AbstractCatalogEntity {

    private static final long serialVersionUID = 1L;

//     private long id;
    private CountryEn countryEn;
    private String atcCode1;
    private String atcCode2;
    private String atcDesc;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_ATC_SQ", allocationSize = 1)
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
     * @return atcCode1 : Code nach ATC Klassifikation
     */
    @Column(name = "ATC_CODE1", nullable = false, length = 30)
    public String getAtcCode1() {
        return this.atcCode1;
    }

    /**
     * @param atcCode1 Column ATC_CODE1 : Code nach ATC Klassifikation.
     */
    public void setAtcCode1(String atcCode1) {
        this.atcCode1 = atcCode1;
    }

    /**
     * @return atcCode2: Verkürzter ATC-Code
     */
    @Column(name = "ATC_CODE2", length = 30)
    public String getAtcCode2() {
        return this.atcCode2;
    }

    /**
     * @param atcCode2 Column ATC_CODE2 : Verkürzter ATC-Code (Obergruppe).
     */
    public void setAtcCode2(String atcCode2) {
        this.atcCode2 = atcCode2;
    }

    /**
     * @return atcDesc :Beschreibung zum Code in Spalte ATC-CODE
     */
    @Column(name = "ATC_DESC", length = 255)
    public String getAtcDesc() {
        return this.atcDesc;
    }

    /**
     * @param atcDesc Column ATC_DESC :Beschreibung zum Code in Spalte ATC-CODE.
     */
    public void setAtcDesc(String atcDesc) {
        this.atcDesc = atcDesc;
    }

}
