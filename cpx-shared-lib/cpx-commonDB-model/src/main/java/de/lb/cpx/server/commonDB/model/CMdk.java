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
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * CMDK initially generated at 03.02.2016 10:32:45 by Hibernate Tools 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">C_MDK: Tabelle der Medizinischen
 * Dienste der Krankenkassen (MDK).</p>
 */
@Entity
@Table(name = "C_MDK", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"MDK_INTERNAL_ID"})
})
//@Check(constraints = "USER_DEFINED_ENTRY = 0 OR (USER_DEFINED_ENTRY = 1 AND ID >= 1000000)")
@SuppressWarnings("serial")
public class CMdk extends AbstractCatalogEntity /* implements MenuCacheEntity */ {

    private static final long serialVersionUID = 1L;

//     private long id;
    private long mdkInternalId;
    private CountryEn countryEn;
    private String mdkName;
    private String mdkDepartment;
    private int mdkDepartmentNo;
    private String mdkStreet;
    private String mdkZipCode;
    private String mdkCity;
    private String mdkPhonePrefix;
    private String mdkPhone;
    private String mdkFax;
    private String mdkComment;
    private String mdkNotice;
    private Integer mdkDistrictNo;
    private String mdkEmail;
    private String mdkIdent;
    private boolean mdkValid;
    private boolean userDefinedEntry;
    //private IdentClassEn docIdentClass;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_MDK_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @SequenceGenerator(name = "default_gen", sequenceName = "C_MDK_INTERNAL_ID_SQ", allocationSize = 1)
    @Column(name = "MDK_INTERNAL_ID", nullable = false)
    public long getMdkInternalId() {
        return mdkInternalId;
    }

    public void setMdkInternalId(final long pMdkInternalId) {
        this.mdkInternalId = pMdkInternalId;
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
     * @return mdkIdent :Identifier der Dienststelle der MDK .
     */
    @Column(name = "MDK_IDENT", length = 10)
    public String getMdkIdent() {
        return this.mdkIdent;
    }

    /**
     *
     * @param mdkIdent Column MDK_IDENT :Identifier der Dienststelle der MDK .
     */
    public void setMdkIdent(String mdkIdent) {
        this.mdkIdent = mdkIdent;
    }

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
    /**
     * @return mdkName :Name der Region, der diese Dienststelle des MDK
     * zugehoert.
     */
    @Column(name = "MDK_NAME", nullable = false, length = 250)
    public String getMdkName() {
        return mdkName;
    }

    /**
     * @param mdkName Column MDK_NAME :Name der Region, der diese Dienststelle
     * des MDK zugehoert.
     */
    public void setMdkName(String mdkName) {
        this.mdkName = mdkName;
    }

    /**
     * @return mdkDepartment: Abteilung der MDK-Dienststelle.
     */
    @Column(name = "MDK_DEPARTMENT", nullable = false, length = 250)
    public String getMdkDepartment() {
        return mdkDepartment;
    }

    /**
     * @param mdkDepartment Column MDK_DEPARTMENT: Abteilung der
     * MDK-Dienststelle.
     */
    public void setMdkDepartment(String mdkDepartment) {
        this.mdkDepartment = mdkDepartment;
    }

    /**
     * @return mdkDepartmentNo: Abteilungnummer der MDK-Dienststelle.
     */
    @Column(name = "MDK_DEPARTMENT_NO", nullable = false, length = 10)
    public int getMdkDepartmentNo() {
        return mdkDepartmentNo;
    }

    /**
     * @param mdkDepartmentNo Column MDK_DEPARTMENT_NO: Abteilungnummer der
     * MDK-Dienststelle.
     */
    public void setMdkDepartmentNo(int mdkDepartmentNo) {
//    this.mdkDepartmentNo = (mdkDepartmentNo!=null && mdkDepartmentNo.equals(0))?null:mdkDepartmentNo;
        this.mdkDepartmentNo = mdkDepartmentNo;
    }

    /**
     * @return mdkStreet: Straße der Dienststelle der MDK.
     */
    @Column(name = "MDK_STREET", length = 250)
    public String getMdkStreet() {
        return mdkStreet;
    }

    /**
     * @param mdkStreet Column MDK_STREET : Straße der Dienststelle der MDK.
     */
    public void setMdkStreet(String mdkStreet) {
        this.mdkStreet = mdkStreet;
    }

    /**
     * @return mdkZipCode : Postleitzahl der Dienststelle der MDK.
     */
    @Column(name = "MDK_ZIP_CODE", length = 10)
    public String getMdkZipCode() {
        return mdkZipCode;
    }

    /**
     * @param mdkZipCode Column MDK_ZIP_CODE: Postleitzahl der Dienststelle der
     * MDK.
     */
    public void setMdkZipCode(String mdkZipCode) {
        this.mdkZipCode = mdkZipCode;
    }

    /**
     * @return mdkCity: Stadt der Dienststelle der MDK.
     */
    @Column(name = "MDK_CITY", nullable = false, length = 250)
    public String getMdkCity() {
        return mdkCity;
    }

    /**
     * @param mdkCity Column MDK_CITY Stadt der Dienststelle der MDK.
     */
    public void setMdkCity(String mdkCity) {
        this.mdkCity = mdkCity;
    }

    /**
     * @return mdkPhonePrefix: Vorwahlnummer der Dienststelleder MDK.
     */
    @Column(name = "MDK_PHONE_PREFIX", length = 50)
    public String getMdkPhonePrefix() {
        return mdkPhonePrefix;
    }

    /**
     * @param mdkPhonePrefix Column MDK_PHONE_PREFIX :Vorwahlnummer der
     * Dienststelleder MDK.
     */
    public void setMdkPhonePrefix(String mdkPhonePrefix) {
        this.mdkPhonePrefix = mdkPhonePrefix;
    }

    /**
     * @return mdkPhone:Telefonnummer der Dienststelle der MDK.
     */
    @Column(name = "MDK_PHONE", length = 50)
    public String getMdkPhone() {
        return mdkPhone;
    }

    /**
     * @param mdkPhone Column MDK_PHONE: Telefonnummer der Dienststelle der MDK.
     */
    public void setMdkPhone(String mdkPhone) {
        this.mdkPhone = mdkPhone;
    }

    /**
     * @return mdkFax : Faxnummer der Dienststelle der MDK.
     */
    @Column(name = "MDK_FAX", length = 50)
    public String getMdkFax() {
        return mdkFax;
    }

    /**
     * @param mdkFax Column MDK_FAX : Faxnummer der Dienststelle der MDK.
     */
    public void setMdkFax(String mdkFax) {
        this.mdkFax = mdkFax;
    }

    /**
     * @return mdkComment: Bemerkung zu der Dienststelle der MDK.
     */
    @Column(name = "MDK_COMMENT", length = 250)
    public String getMdkComment() {
        return mdkComment;
    }

    /**
     * @param mdkComment Column MDK_COMMENT: Bemerkung zu der Dienststelle der
     * MDK.
     */
    public void setMdkComment(String mdkComment) {
        this.mdkComment = mdkComment;
    }

    /**
     * @return mdkNotice: Notiz zu der Dienststelle der MDK.
     */
    @Column(name = "MDK_NOTICE", length = 250)
    public String getMdkNotice() {
        return mdkNotice;
    }

    /**
     * @param mdkNotice Column MDK_NOTICE: Notiz zu der Dienststelle der MDK.
     */
    public void setMdkNotice(String mdkNotice) {
        this.mdkNotice = mdkNotice;
    }

    /**
     * @return mdkDistrictNo: Dienststellennummer des MDK.
     */
    @Column(name = "MDK_DISTRICT_NO", length = 10)
    public Integer getMdkDistrictNo() {
        return mdkDistrictNo;
    }

    /**
     * @param mdkDistrictNo Column MDK_DISTRICT_NO: Dienststellennummer des MDK.
     */
    public void setMdkDistrictNo(Integer mdkDistrictNo) {
        this.mdkDistrictNo = (mdkDistrictNo != null && mdkDistrictNo.equals(0)) ? null : mdkDistrictNo;
    }

    /**
     * @return mdkEmail : E-Mail der Dienststelle der MDK.
     */
    @Column(name = "MDK_EMAIL", length = 50)
    public String getMdkEmail() {
        return mdkEmail;
    }

    /**
     * @param mdkEmail Column MDK_EMAIL: E-Mail der Dienststelle der MDK.
     */
    public void setMdkEmail(String mdkEmail) {
        this.mdkEmail = mdkEmail;
    }

    /**
     * @return mdkValid: Gueltigkeit des MDK.
     */
    @Column(name = "MDK_VALID", length = 3)
    public boolean getMdkValid() {
        return mdkValid;
    }

    /**
     * @param mdkValid Column MDK_VALID :Gueltigkeit des MDK.
     */
    public void setMdkValid(boolean mdkValid) {
        this.mdkValid = mdkValid;
    }

    /**
     * @return mdkValid: Gibt an, ob der Eintrag von einem Nutzer angelegt wurde
     * oder durch ein Skript.
     */
    @Column(name = "USER_DEFINED_ENTRY", length = 3, nullable = false)
    public boolean getUserDefinedEntry() {
        return userDefinedEntry;
    }

    /**
     * @param userDefinedEntry Column USER_DEFINED_ENTRY: Gibt an, ob der
     * Eintrag von einem Nutzer angelegt wurde oder durch ein Skript.
     */
    public void setUserDefinedEntry(final boolean userDefinedEntry) {
        this.userDefinedEntry = userDefinedEntry;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(Objects.requireNonNullElse(mdkName,"----"));
        sb.append(", ").append(Objects.requireNonNullElse(mdkCity,"----"));
        return sb.toString();
    }

//    @Transient
//    @Override
//    public String getName() {
//        return MenuCacheEntity.getName(this, mdkName);
//    }
//
//    @Transient
//    @Override
//    public Long getMenuCacheId() {
//        return mdkInternalId;
//    }
//
//    @Transient
//    @Override
//    public Date getValidFrom() {
//        return null; //mdk entries don't have valid from field(?)
//    }
//
//    @Transient
//    @Override
//    public Date getValidTo() {
//        return null; //mdk entries don't have valid to field(?)
//    }
//
//    @Transient
//    @Override
//    public boolean isValid() {
//        return mdkValid;
//    }
//
//    @Override
//    public boolean isValid(final Date pDate) {
//        return MenuCacheEntity.isValid(pDate, getValidFrom(), getValidTo(), isValid());
//    }
//
//    @Transient
//    @Override
//    public boolean isDeleted() {
//        return false; //mdk has no delete field(?)
//    }
//
//    @Transient
//    @Override
//    public boolean isInActive() {
//        return isInActive(new Date());
//    }
//
//    @Override
//    public boolean isInActive(final Date pDate) {
//        return isDeleted() || !isValid(pDate);
//    }
//
//    @Transient
//    @Override
//    public int getSort() {
//        return 0; //mdk has no sort field(?)
//    }
}
