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
import de.lb.cpx.model.enums.CountryEn;
import de.lb.cpx.model.enums.StateEn;
import de.lb.cpx.server.commons.dao.AbstractVersionEntity;
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
import org.hibernate.annotations.Type;

/**
 * TPatientDetails initially generated at 21.01.2016 17:07:59 by Hibernate Tools
 * 3.2.2.GA
 * <p style="font-size:1em; color:green;"> Die Tabelle "T_PATIENT_DETAILS"
 * speichert detaillierte Informationen zu einem Patienten.</p>
 */
@Entity
@Table(name = "T_PATIENT_DETAILS",
        indexes = {
            @Index(name = "IDX_PATIENT_DETAILS4PATIENT_ID", columnList = "T_PATIENT_ID", unique = false)})
@SuppressWarnings("serial")
public class TPatientDetails extends AbstractVersionEntity {

    private static final long serialVersionUID = 1L;

    // private long id;
    private TPatient patient;
    private StateEn patdState; // Bundesland
    private CountryEn patdCountry; //Country short name as in CCountry
    private String patdZipcode;// PLZ
    private String patdAddress;//Strasse + Hausnr
    private String patdCity;//City of the ResidentailAdress
    private String patPhoneNumber;//tel. Nr
    private String patCellNumber;// handy nr
    private StateEn patdPostState; // Bundesland
    private CountryEn patdPostCountry; //Staat
    private String patdPostZipcode;// PLZ
    private String patdPostAddress;//Strasse + Hausnr
    private String patdPostCity;//City of the Postaladdress
    private boolean patdPostDiffFl = false; // true when the post address differs from home address
    private boolean patdIsActualFl = false;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_PATIENT_DETAILS_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    /**
     * Gibt Wohnadresse des Patients zurück.d.h.Strasse + Hausnr
     *
     * @return patdAddress
     */
    @Column(name = "PATD_ADDRESS", length = 400)
    public String getPatdAddress() {
        return this.patdAddress;
    }

    /**
     * Gibt Bundesland der wohnadresse zurück.
     *
     * @return patdState
     */
    @Column(name = "PATD_STATE", length = 25)
    @Enumerated(EnumType.STRING)
    public StateEn getPatdState() {
        return this.patdState;
    }

    /**
     * Gibt Postleitzahl der wohnadresse zurück.
     *
     * @return patdZipcode
     */
    @Column(name = "PATD_ZIPCODE", length = 5)
    public String getPatdZipcode() {
        return this.patdZipcode;
    }

    /**
     * Gibt Verweis auf ID der Tablle T_PATIENT zurück.
     *
     * @return patient
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_PATIENT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_PATIENT_DETAILS4PATIENT_ID"))
    @JsonBackReference(value = "patDetails")
    public TPatient getPatient() {
        return this.patient;
    }

    /**
     * Gibt Telefon-Nummer des Patients zurück.
     *
     * @return patPhoneNumber
     */
    @Column(name = "PATD_PHONE_NUMBER", length = 20)
    public String getPatPhoneNumber() {
        return patPhoneNumber;
    }

    /**
     * Gibt Mobile-Nummer des Patients zurück.
     *
     * @return patCellNumber
     */
    @Column(name = "PATD_CELL_NUMBER", length = 20)
    public String getPatCellNumber() {
        return patCellNumber;
    }

    /**
     *
     * @return patdIsActualFl
     */
    @Column(name = "PATD_IS_ACTUAL_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean getPatdIsActualFl() {
        return this.patdIsActualFl;
    }

    /**
     *
     * @param patPhoneNumber Column PATD_PHONE_NUMBER: Telefon-Nummer des
     * Patients.
     */
    public void setPatPhoneNumber(String patPhoneNumber) {
        this.patPhoneNumber = patPhoneNumber;
    }

    /**
     *
     * @param patCellNumber Column PATD_CELL_NUMBER: Mobile-Nummer des Patients
     * .
     */
    public void setPatCellNumber(String patCellNumber) {
        this.patCellNumber = patCellNumber;
    }

    /**
     *
     * @return patdCity : Stadt der Wohnadresse .
     */
    @Column(name = "PATD_CITY", length = 250)
    public String getPatdCity() {
        return patdCity;
    }

    /**
     *
     * @param patdCity Column PATD_CITY: Stadt der Wohnadresse .
     */
    public void setPatdCity(String patdCity) {
        this.patdCity = patdCity;
    }

    public void setId(final long id) {
        this.id = id;
    }

    /**
     *
     * @param patdAddress Column PATD_POST_ADDRESS: Postadresse des Patients.
     */
    public void setPatdAddress(final String patdAddress) {
        this.patdAddress = patdAddress;
    }

    /**
     *
     * @param patdState Column PATD_POST_STATE: Bundesland der Postanschrift.
     */
    public void setPatdState(final StateEn patdState) {
        this.patdState = patdState;
    }

    /**
     *
     * @param patdZipcode Column PATD_POST_ZIPCODE: Postleitzhal der
     * Postanschrift .
     */
    public void setPatdZipcode(final String patdZipcode) {
        this.patdZipcode = patdZipcode;
    }

    /**
     *
     * @param patient Column PATIENT_ID: Verweis auf ID der Tablle T_PATIENT.
     */
    public void setPatient(final TPatient patient) {
        this.patient = patient;
    }

    /**
     * Gibt Staat der Wohnadresse (Kurzer Name) zurück.
     *
     * @return patdCountry
     */
    @Column(name = "PATD_COUNTRY", length = 25)
    @Enumerated(EnumType.STRING)
    public CountryEn getPatdCountry() {
        return patdCountry;
    }

    /**
     *
     * @param patdCountry Column PATD_COUNTRY: Staat der Wohnadresse (Kurzer
     * Name) .
     */
    public void setPatdCountry(CountryEn patdCountry) {
        this.patdCountry = patdCountry;
    }

    /**
     * Gibt Bundesland der Postanschrift zurück.
     *
     * @return patdPostState
     */
    @Column(name = "PATD_POST_STATE", length = 25)
    @Enumerated(EnumType.STRING)
    public StateEn getPatdPostState() {
        return patdPostState;
    }

    /**
     *
     * @param patdPostState Column PATD_POST_STATE: Bundesland der
     * Postanschrift.
     */
    public void setPatdPostState(StateEn patdPostState) {
        this.patdPostState = patdPostState;
    }

    /**
     * Gibt Staat der Postanschrift zurück.
     *
     * @return patdPostCountry
     */
    @Column(name = "PATD_POST_COUNTRY", length = 25)
    @Enumerated(EnumType.STRING)
    public CountryEn getPatdPostCountry() {
        return patdPostCountry;
    }

    /**
     *
     * @param patdPostCountry Column PATD_POST_COUNTRY : Staat der
     * Postanschrift.
     */
    public void setPatdPostCountry(CountryEn patdPostCountry) {
        this.patdPostCountry = patdPostCountry;
    }

    /**
     * Gibt Postleitzahl der Postanschrift zurück.
     *
     * @return patdPostZipcode
     */
    @Column(name = "PATD_POST_ZIPCODE", length = 5)
    public String getPatdPostZipcode() {
        return patdPostZipcode;
    }

    /**
     *
     * @param patdPostZipcode Column PATD_POST_ZIPCODE: POSTLEITZAHL der
     * Postanschrift .
     */
    public void setPatdPostZipcode(String patdPostZipcode) {
        this.patdPostZipcode = patdPostZipcode;
    }

    /**
     * Gibt Postadresse des Patienten zurück.
     *
     * @return patdPostAddress
     */
    @Column(name = "PATD_POST_ADDRESS", length = 400)
    public String getPatdPostAddress() {
        return patdPostAddress;
    }

    /**
     *
     * @param patdPostAddress Column PATD_POST_ADDRESS: Postadresse des
     * Patients.
     */
    public void setPatdPostAddress(String patdPostAddress) {
        this.patdPostAddress = patdPostAddress;
    }

    /**
     * Gibt Stadt der Postanschrift zurück.
     *
     * @return patdPostCity
     */
    @Column(name = "PATD_POST_CITY", length = 250)
    public String getPatdPostCity() {
        return patdPostCity;
    }

    /**
     *
     * @param patdPostCity Column PATD_POST_CITY: Stadt der Postanschrift.
     */
    public void setPatdPostCity(String patdPostCity) {
        this.patdPostCity = patdPostCity;
    }

    /**
     * Gibt 1 zurück, wenn sich die Postadresse von der wohnadresse
     * unterscheidet.
     *
     * @return patdPostDiffFl
     */
    @Column(name = "PATD_POST_DIFF_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean getPatdPostDiff() {
        return patdPostDiffFl;
    }

    /**
     *
     * @param patdPostDiff Column PATD_POST_DIFF_FL: 1, wenn sich die
     * Postadresse von der wohnadresse unterscheidet.
     */
    public void setPatdPostDiff(boolean patdPostDiff) {
        this.patdPostDiffFl = patdPostDiff;
    }

    public void setPatdIsActualFl(final boolean patdIsActualFl) {
        this.patdIsActualFl = patdIsActualFl;
    }

    @Override
    public boolean versionEquals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TPatientDetails)) {
            return false;
        }
        final TPatientDetails other = (TPatientDetails) obj;
        if (!isStringEquals(this.patCellNumber, other.getPatCellNumber())) {
            return false;
        }
        if (!isStringEquals(this.patPhoneNumber, other.getPatPhoneNumber())) {
            return false;
        }
        if (!isStringEquals(this.patdAddress, other.getPatdAddress())) {
            return false;
        }
        if (!isStringEquals(this.patdCity, other.getPatdCity())) {
            return false;
        }
        if (!Objects.equals(this.patdCountry, other.getPatdCountry())) {
            return false;
        }
        /*
        if (!Objects.equals(this.patdIsActualFl, other.getPatdIsActualFl())) {
            return false;
        }
         */
        if (!Objects.equals(this.patdState, other.getPatdState())) {
            return false;
        }
        if (!isStringEquals(this.patdZipcode, other.getPatdZipcode())) {
            return false;
        }
        return true;
    }

}
