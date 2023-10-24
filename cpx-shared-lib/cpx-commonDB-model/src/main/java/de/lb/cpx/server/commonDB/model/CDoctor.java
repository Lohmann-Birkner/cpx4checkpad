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
 * CDoctor initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">C_DOCTOR : Tabelle der Ärzte
 * .</p>
 *
 */
@Entity
@Table(name = "C_DOCTOR")
@SuppressWarnings("serial")
public class CDoctor extends AbstractCatalogEntity {

    private static final long serialVersionUID = 1L;
    /*
  @Column(name = "DOC_IDENT_CLASS", nullable=false, length = 25)
  //@Enumerated(EnumType.STRING)
  @Convert(converter = IdentClassConverter.class)
  public IdentClassEn getDocIdentClassEn() {
  return docIdentClass;
  }
  
  
  public void setDocIdentClassEn(IdentClassEn identClass) {
  this.docIdentClass = identClass;
  }
     */

//     private long id;
    private CountryEn countryEn;
    private String docIdent;
    //private IdentClassEn docIdentClass;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_DOCTOR_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return countryEn: Country Enumeration (de,en).
     */
    @Column(name = "COUNTRY_EN", length = 25)
    @Enumerated(EnumType.STRING)
    public CountryEn getCountryEn() {
        return countryEn;
    }

    /**
     *
     * @param countryEn Column COUNTRY_EN: Country Enumeration (de,en).
     */
    public void setCountryEn(CountryEn countryEn) {
        this.countryEn = countryEn;
    }

    /**
     *
     * @return docIdent :Identifikatiosnummer des Arztes.
     */
    @Column(name = "DOC_IDENT", length = 10)
    public String getDocIdent() {
        return this.docIdent;
    }

    /**
     *
     * @param docIdent Column DOC_IDENT: Identifikatiosnummer des Arztes.
     */
    public void setDocIdent(String docIdent) {
        this.docIdent = docIdent;
    }

}
