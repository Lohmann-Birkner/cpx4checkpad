/* 
 * Copyright (c) 2016 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import de.lb.cpx.model.enums.InsStatusEn;
import de.lb.cpx.server.commons.dao.AbstractVersionEntity;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Type;

/**
 * TInsurance initially generated at 21.01.2016 17:07:59 by Hibernate Tools
 * 3.2.2.GA
 * <p style="font-size:1em; color:green;">Die Tabelle "T_INSURANCE" speichert
 * Informationen zu den Krankenversicherungen eines Patienten . </p>
 */
@Entity
@Table(name = "T_INSURANCE",
        indexes = {
            @Index(name = "IDX_INSURANCE4PATIENT_ID", columnList = "T_PATIENT_ID", unique = false)})
@SuppressWarnings("serial")
public class TInsurance extends AbstractVersionEntity {

    private static final long serialVersionUID = 1L;

//  private long id;
    private TPatient patient;
    private String insNumber;
    private String insInsuranceCompany;
    private Date insValidFrom;
    private Date insValidTo;
    private InsStatusEn insStatusEn;
    private String insureeFirstName;
    private String insureeLastName;
    private boolean insIsActualFl = false;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_INSURANCE_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    /**
     * Gibt Identifikationsnummer der Versicherungsanstalt zurück.
     *
     * @return insInsuranceCompany
     */
    @Column(name = "INS_INS_COMPANY", length = 20)
    public String getInsInsuranceCompany() {
        return this.insInsuranceCompany;
    }

    /**
     * Gibt Versichertennummer zurück.
     *
     * @return insNumber
     */
    @Column(name = "INS_NUMBER", length = 50)
    public String getInsNumber() {
        return this.insNumber;
    }

    /**
     * Gibt Versichertensatus 1-3 zurück.z.b. self,family,senior.
     *
     * @return insStatusEn
     */
    @Column(name = "INS_STATUS_EN", length = 50)
    @Enumerated(EnumType.STRING)
    public InsStatusEn getInsStatusEn() {
        return insStatusEn;
    }

    /**
     * Gibt Verweis auf die Tablle T_PATIENT zurück.
     *
     * @return patient: .
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_PATIENT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_INSURANCE4PATIENT_ID"))
    @JsonBackReference(value = "insurance")
    public TPatient getPatient() {
        return this.patient;
    }

    /**
     * Gibt Anfang der Versicherungsmitgliedschaft zurück.
     *
     * @return insValidFrom
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INS_VALID_FROM", length = 7)
    public Date getInsValidFrom() {
        return insValidFrom == null ? null : new Date(insValidFrom.getTime());
    }

    /**
     * Gibt Flag 0/1 zurück ,ob es aktuelle Versicherung ist .
     *
     * @return insIsActualFl
     */
    @Column(name = "INS_IS_ACTUAL_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean getInsIsActualFl() {
        return this.insIsActualFl;
    }

    /**
     *
     * @param insValidFrom Column INS_VALID_FROM : Anfang der
     * Versicherungsmitgliedschaft.
     */
    public void setInsValidFrom(Date insValidFrom) {
        this.insValidFrom = insValidFrom == null ? null : new Date(insValidFrom.getTime());
    }

    /**
     * Gibt Ende der Versicherungsmitgliedschaft zurück.
     *
     * @return insValidTo
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INS_VALID_TO", length = 7)
    public Date getInsValidTo() {
        return insValidTo == null ? null : new Date(insValidTo.getTime());
    }

    /**
     * get the Firstname of the Insurance-Owner, may differ from Patient
     *
     * @return insureeFirstName: Vorname des Versicherers.
     */
    @Column(name = "INS_INSUREE_FIRST_NAME", length = 50)
    public String getInsureeFirstName() {
        return insureeFirstName;
    }

    /**
     * set the FirstName of the Insurance-Owner, may differ from Patient
     *
     * @param insureeFirstName Column INS_INSUREE_FIRST_NAME: Vorname des
     * Versicherers.
     */
    public void setInsureeFirstName(String insureeFirstName) {
        this.insureeFirstName = insureeFirstName;
    }

    /**
     * get the Lastname of the Insurance-Owner, may differ from Patient
     *
     * @return insureeLastName: Lastname des Versicherers
     */
    @Column(name = "INS_INSUREE_LAST_NAME", length = 50)
    public String getInsureeLastName() {
        return insureeLastName;
    }

    /**
     * set the LastName of the Insurance-Owner, may differ from Patient
     *
     * @param insureeLastName Column INS_INSUREE_LAST_NAME: Lastname des
     * Versicherers .
     */
    public void setInsureeLastName(String insureeLastName) {
        this.insureeLastName = insureeLastName;
    }

    /**
     *
     * @param insValidTo Column INS_VALID_TO: Ende der
     * Versicherungsmitgliedschaft.
     */
    public void setInsValidTo(Date insValidTo) {
        this.insValidTo = insValidTo == null ? null : new Date(insValidTo.getTime());
    }

    /**
     *
     * @param insStatusEn Column INS_STATUS_EN: Versichertensatus 1-3
     * (self,family,senior) .
     */
    public void setInsStatusEn(InsStatusEn insStatusEn) {
        this.insStatusEn = insStatusEn;
    }

    public void setId(final long id) {
        this.id = id;
    }

    /**
     *
     * @param insInsuranceCompany Column INS_INS_COMPANY: Identifikationsnummer
     * der Versicherungsanstalt.
     */
    public void setInsInsuranceCompany(final String insInsuranceCompany) {
        this.insInsuranceCompany = insInsuranceCompany;
    }

    /**
     *
     * @param insNumber Column INS_NUMBER: Versichertennummer.
     */
    public void setInsNumber(final String insNumber) {
        this.insNumber = insNumber;
    }

    public void setPatient(final TPatient patient) {
        this.patient = patient;
    }

    public void setInsIsActualFl(final boolean insIsActualFl) {
        this.insIsActualFl = insIsActualFl;
    }

    @Override
    public boolean versionEquals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TInsurance)) {
            return false;
        }
        final TInsurance other = (TInsurance) obj;
        if (!Objects.equals(this.insInsuranceCompany, other.getInsInsuranceCompany())) {
            return false;
        }
        if (!Objects.equals(this.insNumber, other.getInsNumber())) {
            return false;
        }
        return true;
    }

}
