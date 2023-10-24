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

import de.lb.cpx.model.converter.IdentClassConverter;
import de.lb.cpx.model.converter.StateConverter;
import de.lb.cpx.model.enums.CountryEn;
import de.lb.cpx.model.enums.IdentClassEn;
import de.lb.cpx.model.enums.StateEn;
import de.lb.cpx.server.commons.dao.AbstractCatalogEntity;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * CInsuranceCompany initially generated at 03.02.2016 10:32:45 by Hibernate
 * Tools 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">C_INSURANCE_COMPANY: Tabelle der
 * Krankenkassen</p>
 */
@Entity
@Table(name = "C_INSURANCE_COMPANY", indexes = {
    @Index(name = "IDX_INS_COMPANY_SHORT_NAME", columnList = "INSC_SHORT", unique = false)})
@SuppressWarnings("serial")
public class CInsuranceCompany extends AbstractCatalogEntity {

    private static final long serialVersionUID = 1L;
    /*
  @Column(name = "INSC_ASSOCIATION", nullable=true, length = 20)
  public String getInscAssociation() {
  return inscAssociation;
  }
  
  public void setInscAssociation(String inscAssociation) {
  this.inscAssociation = inscAssociation;
  }
     */

    //private long id;
    private CountryEn countryEn;
    private StateEn stateEn;
    private String inscIdent;
    private String inscShort;
    private String inscName;
    private IdentClassEn inscIdentClass;
    private String inscAddress;
    private String inscZipCode;
    private String inscCity;
    private String inscPhonePrefix;
    private String inscPhone;
    private String inscFax;
    private Integer inscChangeService;
    private Integer inscClass;
    private Integer inscRegion;
    private Integer inscKbvIndicator;
    private Integer inscKbvzIndicator;
    //private String inscAssociation;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_INSURANCE_COMPANY_SQ", allocationSize = 1)
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
     * @return inscIdent : Identifikationsnummer der Versicherung
     */
    @Column(name = "INSC_IDENT", length = 10)
    public String getInscIdent() {
        return this.inscIdent;
    }

    /**
     *
     * @param inscIdent Column INSC_IDENT : Identifikationsnummer der
     * Versicherung
     */
    public void setInscIdent(String inscIdent) {
        this.inscIdent = inscIdent;
    }

    /**
     *
     * @return inscShort: Kurzname der Versicherung
     */
    @Column(name = "INSC_SHORT", length = 50)
    //@Convert(converter = InsShortConverter.class)
    public String getInscShort() {
        return this.inscShort;
    }

    /**
     *
     * @param inscShort Column INSC_SHORT: Kurzname der Versicherung
     */
    public void setInscShort(String inscShort) {
        this.inscShort = inscShort;
    }

    /**
     *
     * @return inscName : Name der Versicherungsanstalts
     */
    @Column(name = "INSC_NAME")
    public String getInscName() {
        return this.inscName;
    }

    /**
     *
     * @param inscName Column INSC_NAME : Name der Versicherungsanstalts
     */
    public void setInscName(String inscName) {
        this.inscName = inscName;
    }

    /**
     *
     * @return stateEn: Enumeration für Versichertensatus 1-3
     * (self,family,senior) .
     */
    @Column(name = "STATE_EN", nullable = false, length = 25)
    //@Enumerated(EnumType.STRING)
    @Convert(converter = StateConverter.class)
    public StateEn getStateEn() {
        return stateEn;
    }

    /**
     *
     * @param stateEn Column STATE_EN: Enumeration für Versichertensatus 1-3
     * (self,family,senior) ..
     */
    public void setStateEn(StateEn stateEn) {
        this.stateEn = stateEn;
    }

    /**
     *
     * @return identClass: Enumeration für Class der Versicherungsanstalts 10-97
     * .
     */
    @Column(name = "INSC_IDENT_CLASS", nullable = false, length = 25)
    //@Enumerated(EnumType.STRING)
    @Convert(converter = IdentClassConverter.class)
    public IdentClassEn getInscIdentClassEn() {
        return inscIdentClass;
    }

    /**
     *
     * @param identClass Column INSC_IDENT_CLASS :Enumeration für Class der
     * Versicherungsanstalts 10-97 .
     */
    public void setInscIdentClassEn(IdentClassEn identClass) {
        this.inscIdentClass = identClass;
    }

    /**
     *
     * @return inscAddress: Adresse der Versicherungsanstalts.
     */
    @Column(name = "INSC_ADDRESS", nullable = true, length = 255)
    public String getInscAddress() {
        return inscAddress;
    }

    /**
     * @param inscAddress Column INSC_ADDRESS: Adresse der
     * Versicherungsanstalts.
     */
    public void setInscAddress(String inscAddress) {
        this.inscAddress = inscAddress;
    }

    /**
     *
     * @return inscZipCode: Postleizahl der Versicherungsanstalts .
     */
    @Column(name = "INSC_ZIP_CODE", nullable = true, length = 10)
    public String getInscZipCode() {
        return inscZipCode;
    }

    /**
     * @param inscZipCode Column INSC_ZIP_CODE: Postleizahl der
     * Versicherungsanstalts .
     */
    public void setInscZipCode(String inscZipCode) {
        this.inscZipCode = inscZipCode;
    }

    /**
     *
     * @return inscCity :Enumeration für Stadt der Versicherungsanstalts 01-16.
     */
    @Column(name = "INSC_CITY", nullable = true, length = 50)
    public String getInscCity() {
        return inscCity;
    }

    /**
     * @param inscCity Column INSC_CITY :Enumeration für Stadt der
     * Versicherungsanstalts 01-16.
     */
    public void setInscCity(String inscCity) {
        this.inscCity = inscCity;
    }

    /**
     *
     * @return inscPhonePrefix :Telefon-Vorwahl der Versicherungsanstalts .
     */
    @Column(name = "INSC_PHONE_PREFIX", nullable = true, length = 50)
    public String getInscPhonePrefix() {
        return inscPhonePrefix;
    }

    /**
     * @param inscPhonePrefix Column INSC_PHONE_PREFIX :Telefon-Vorwahl der
     * Versicherungsanstalts .
     */
    public void setInscPhonePrefix(String inscPhonePrefix) {
        this.inscPhonePrefix = inscPhonePrefix;
    }

    /**
     *
     * @return inscPhone: Telefonnummer der Versicherungsanstalts.
     */
    @Column(name = "INSC_PHONE", nullable = true, length = 50)
    public String getInscPhone() {
        return inscPhone;
    }

    /**
     * @param inscPhone Column INSC_PHONE: Telefonnummer der
     * Versicherungsanstalts.
     */
    public void setInscPhone(String inscPhone) {
        this.inscPhone = inscPhone;
    }

    /**
     *
     * @return inscFax :Faxnummer der Versicherungsanstalts.
     */
    @Column(name = "INSC_FAX", nullable = true, length = 50)
    public String getInscFax() {
        return inscFax;
    }

    /**
     * @param inscFax Column INSC_FAX :Faxnummer der Versicherungsanstalts.
     */
    public void setInscFax(String inscFax) {
        this.inscFax = inscFax;
    }

    /**
     *
     * @return inscChangeService: Änderungsdienst
     */
    @Column(name = "INSC_CHANGE_SERVICE", nullable = true)
    public int getInscChangeService() {
        return inscChangeService;
    }

    /**
     * @param inscChangeService Column INSC_CHANGE_SERVICE: Änderungsdienst
     */
    public void setInscChangeService(int inscChangeService) {
        this.inscChangeService = inscChangeService;
    }

    /**
     *
     * @return inscClass: Enumeration für Class der Versicherungsanstalts 10-97
     * (Kranken,Renten,Unfall,Sozial,...).
     */
    @Column(name = "INSC_CLASS", nullable = true)
    public int getInscClass() {
        return inscClass;
    }

    /**
     * @param inscClass Column INSC_CLASS :Enumeration für Class der
     * Versicherungsanstalts 10-97 (Kranken,Renten,Unfall,Sozial,...).
     */
    public void setInscClass(int inscClass) {
        this.inscClass = inscClass;
    }

    /**
     *
     * @return inscRegion: Bezirk(REGION) der Versicherungsanstalts.
     */
    @Column(name = "INSC_REGION", nullable = true)
    public int getInscRegion() {
        return inscRegion;
    }

    /**
     * @param inscRegion Column INSC_REGION Bezirk(REGION) der
     * Versicherungsanstalts.
     */
    public void setInscRegion(int inscRegion) {
        this.inscRegion = inscRegion;
    }

    /**
     *
     * @return inscKbvIndicator : Kassenärztliche Bundesvereinigung
     */
    @Column(name = "INSC_KBV_INDICATOR", nullable = true)
    public int getInscKbvIndicator() {
        return inscKbvIndicator;
    }

    /**
     * @param inscKbvIndicator Column INSC_KBV_INDICATOR : Kassenärztliche
     * Bundesvereinigung
     */
    public void setInscKbvIndicator(int inscKbvIndicator) {
        this.inscKbvIndicator = inscKbvIndicator;
    }

    /**
     *
     * @return inscKbvzIndicator : Kassenzahnärztliche Bundesvereinigung
     * (Kassenärztliche Bundesvereinigung der Zahnärzte)
     */
    @Column(name = "INSC_KBVZ_INDICATOR", nullable = true)
    public int getInscKbvzIndicator() {
        return inscKbvzIndicator;
    }

    /**
     * @param inscKbvzIndicator Column INSC_KBVZ_INDICATOR Kassenzahnärztliche
     * Bundesvereinigung (Kassenärztliche Bundesvereinigung der Zahnärzte)
     */
    public void setInscKbvzIndicator(int inscKbvzIndicator) {
        this.inscKbvzIndicator = inscKbvzIndicator;
    }

}
