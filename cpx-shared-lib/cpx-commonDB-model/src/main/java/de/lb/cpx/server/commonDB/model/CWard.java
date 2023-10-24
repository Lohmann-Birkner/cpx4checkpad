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

/**
 * CWard initially generated at 03.02.2016 10:32:45 by Hibernate Tools 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">C_WARD: Tabelle der Stationen
 * innerhalb einer Abteilung eines Krankenhauses.</p>
 */
@Entity
@Table(name = "C_WARD")
@SuppressWarnings("serial")
public class CWard extends AbstractCatalogEntity {

    private static final long serialVersionUID = 1L;

//     private long id;
    private CHospital CHospital;
    private String wardShort;
    private String wardDescriptKey;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_WARD_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return CHospital:Referenz auf die Tabelle C_HOSPITAL, die
     * Stationsnamenvorgabe ist Krankehausspeziffisch
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HOS_ID", nullable = false)
    public CHospital getCHospital() {
        return this.CHospital;
    }

    /**
     *
     * @param CHospital Column HOS_ID :Referenz auf die Tabelle C_HOSPITAL, die
     * Stationsnamenvorgabe ist Krankehausspeziffisch
     */
    public void setCHospital(CHospital CHospital) {
        this.CHospital = CHospital;
    }

    /**
     *
     * @return wardShort :Stationschlüssel .
     */
    @Column(name = "WARD_SHORT", nullable = false, length = 10)
    public String getWardShort() {
        return this.wardShort;
    }

    /**
     *
     * @param wardShort Column WARD_SHORT: Stationschlüssel .
     */
    public void setWardShort(String wardShort) {
        this.wardShort = wardShort;
    }

    /**
     *
     * @return wardDescriptKey :Verschlüsselung(Schlüssel der Definition in dem
     * ResourceBundle)
     */
    @Column(name = "WARD_DESCRIPT_KEY")
    public String getWardDescriptKey() {
        return this.wardDescriptKey;
    }

    /**
     *
     * @param wardDescriptKey Column WARD_DESCRIPT_KEY
     * :Verschlüsselung(Schlüssel der Definition in dem ResourceBundle)
     */
    public void setWardDescriptKey(String wardDescriptKey) {
        this.wardDescriptKey = wardDescriptKey;
    }

}
