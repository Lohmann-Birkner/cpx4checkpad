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
 * CDepartment initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">C_DEPARTMENT: Tabelle der
 * Fachabteilungen (Benennung der Abteilungen eines Krankenhauses).</p>
 *
 */
@Entity
@Table(name = "C_DEPARTMENT")
@SuppressWarnings("serial")
public class CDepartment extends AbstractCatalogEntity {

    private static final long serialVersionUID = 1L;

//     private long id;
    private CountryEn countryEn;
    private String depKey301;
    private String depDescription301;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_DEPARTMENT_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return countryEn:Country Enumeration (de,en) .
     */
    @Column(name = "COUNTRY_EN", length = 25)
    @Enumerated(EnumType.STRING)
    public CountryEn getCountryEn() {
        return countryEn;
    }

    /**
     *
     * @param countryEn Country Enumeration (de,en) .
     */
    public void setCountryEn(CountryEn countryEn) {
        this.countryEn = countryEn;
    }

    /**
     *
     * @return depKey301: Abteilungsschlüssel nach §301.
     */
    @Column(name = "DEP_KEY_301", nullable = false, length = 10)
    public String getDepKey301() {
        return this.depKey301;
    }

    /**
     *
     * @param depKey301 Column DEP_KEY_301 : Abteilungsschlüssel nach §301.
     */
    public void setDepKey301(String depKey301) {
        this.depKey301 = depKey301;
    }

    /**
     *
     * @return depDescription301: Dekodierungsschlüssel -Verschlüsselung(
     * Schlüssel der Definition in dem ResourceBundle).
     */
    @Column(name = "DEP_DESCRIPTION_301")
    public String getDepDescription301() {
        return this.depDescription301;
    }

    /**
     *
     * @param depDescription301 Column DEP_DESCRIPTION_301
     * :Dekodierungsschlüssel -Verschlüsselung (Schlüssel der Definition in dem
     * ResourceBundle).
     */
    public void setDepDescription301(String depDescription301) {
        this.depDescription301 = depDescription301;
    }

}
