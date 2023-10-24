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
import com.fasterxml.jackson.annotation.JsonManagedReference;
import de.lb.cpx.model.enums.GenderEn;
import de.lb.cpx.server.commons.dao.AbstractVersionEntity;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * TPatient initially generated at 21.01.2016 17:07:59 by Hibernate Tools
 * 3.2.2.GA
 * <p style="font-size:1em; color:green;"> Die Tabelle "T_PATIENT" speichert
 * Informationen zu den Patienten, die CPX verwaltet.</p>
 */
@Entity
@Table(name = "T_PATIENT", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"PAT_NUMBER"}, name = "Uni_PatientNumber")
})
@SuppressWarnings("serial")
public class TPatient extends AbstractVersionEntity {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(TPatient.class.getName());

//  private long id;
    private String patNumber;
    private String patTitle;
    private String patSecName;
    private String patFirstName;
    private Date patDateOfBirth;
    private GenderEn patGenderEn;
    private Set<TInsurance> insurances = new HashSet<>(0);
    private Set<TCase> cases = new HashSet<>(0);
    private Set<TPatientDetails> patientDetailList = new HashSet<>(0);
    //private TInsurance patInsAct;
    //private TPatientDetails patDetAct;

    public TPatient() {
        ignoredFields = new String[]{"insurances", "cases", "patientDetailList"};// todo add actual PatientDetails
    }

    @Transient
    public TPatientDetails getPatDetailsActual() {
        return getCurrentDetail();
    }

    @Transient
    public TPatientDetails getCurrentDetail() {
        Iterator<TPatientDetails> it = this.patientDetailList.iterator();
        while (it.hasNext()) {
            TPatientDetails patd = it.next();
            if (patd == null) {
                continue;
            }
            if (!patd.getPatdIsActualFl()) {
                continue;
            }
            return patd;
        }
        return null;
    }

    @Transient
    public TInsurance getPatInsuranceActual() {
        return getCurrentInsurance();
    }

    @Transient
    public TInsurance getCurrentInsurance() {
        Iterator<TInsurance> it = this.insurances.iterator();
        while (it.hasNext()) {
            TInsurance ins = it.next();
            if (ins == null) {
                continue;
            }
            if (!ins.getInsIsActualFl()) {
                continue;
            }
            return ins;
        }
        return null;
    }

    public void setPatDetailsActual(TPatientDetails patDetAct) {
        setCurrentDetail(patDetAct);
    }

    public void setCurrentDetail(final TPatientDetails pPatientDetails) {
        if (pPatientDetails == null) {
            LOG.log(Level.SEVERE, "Passed TPatientDetails object is null!");
            return;
        }
        pPatientDetails.setPatdIsActualFl(true);
        addDetail(pPatientDetails);
    }

    public void setPatInsuranceActual(TInsurance patInsAct) {
        setCurrentInsurance(patInsAct);
    }

    public void setCurrentInsurance(final TInsurance pInsurance) {
        if (pInsurance == null) {
            LOG.log(Level.SEVERE, "Passed TInsurance object is null!");
            return;
        }
        pInsurance.setInsIsActualFl(true);
        addInsurance(pInsurance);
    }

    public void addPatientDetails(final TPatientDetails patientDetails) {
        addDetail(patientDetails);
    }

    public void addDetail(final TPatientDetails pPatientDetails) {
        if (pPatientDetails == null) {
            return;
        }
        pPatientDetails.setPatient(this);
        this.patientDetailList.remove(pPatientDetails);
        if (pPatientDetails.getPatdIsActualFl()) {
            TPatientDetails patd = getCurrentDetail();
            if (patd != null && patd != pPatientDetails) {
                patd.setPatdIsActualFl(false);
            }
        }
        this.patientDetailList.add(pPatientDetails);
    }

    public void addInsurance(final TInsurance pInsurance) {
        if (pInsurance == null) {
            return;
        }
        pInsurance.setPatient(this);
        this.insurances.remove(pInsurance);
        if (pInsurance.getInsIsActualFl()) {
            TInsurance ins = getCurrentInsurance();
            if (ins != null && ins != pInsurance) {
                ins.setInsIsActualFl(false);
            }
        }
        this.insurances.add(pInsurance);
    }

    /*
  public void addPatientDetails(final TPatientDetails patientDetails) {
    patientDetails.setPatient(this);
    getPatientDetailList().add(patientDetails);
  }
     */
    /**
     * Get active insurance by insurance identifier
     *
     * @param pIk insurance identifier
     * @return active insurance (can be null!)
     */
    public TInsurance getActiveInsuranceByIk(final String pIk) {
        List<TInsurance> results = getInsurancesByIk(pIk);
        for (TInsurance ins : results) {
            if (ins != null && ins.getInsIsActualFl()) {
                return ins;
            }
        }
        return null;
    }

    /**
     * Get insurance entries by insurance identifier
     *
     * @param pIk insurance identifier
     * @return list of matching insurances
     */
    public List<TInsurance> getInsurancesByIk(final String pIk) {
        List<TInsurance> results = new ArrayList<>();
        final String ik = pIk == null ? "" : pIk.trim();
        for (final TInsurance ins : new HashSet<>(insurances)) {
            if (ins == null) {
                continue;
            }
            final String ik2 = ins.getInsInsuranceCompany() == null ? "" : ins.getInsInsuranceCompany().trim();
            if (ik2.equalsIgnoreCase(ik)) {
                results.add(ins);
            }
        }
        return results;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "patient")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JsonBackReference(value = "case")
    public Set<TCase> getCases() {
        return this.cases;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_PATIENT_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "patient")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference(value = "insurance")
    public Set<TInsurance> getInsurances() {
        return this.insurances;
    }

    /**
     * Gibt Geburtsdatum des Patienten zurück.
     *
     * @return patDateOfBirth
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PAT_DATE_OF_BIRTH", length = 7)
    public Date getPatDateOfBirth() {
        return patDateOfBirth == null ? null : new Date(patDateOfBirth.getTime());
    }

    @Transient
    public Integer getPatYearOfBirth() {
        Date birth = patDateOfBirth;
        if (birth == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(birth);
        return cal.get(Calendar.YEAR);
    }

    @Transient
    public Integer getPatMonthOfBirth() {
        Date birth = patDateOfBirth;
        if (birth == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(birth);
        return cal.get(Calendar.MONTH) + 1; //0 = January!
    }

    /**
     * Gibt Vorname des Patienten zurück.
     *
     * @return patFirstName
     */
    @Column(name = "PAT_FIRST_NAME", length = 50)
    public String getPatFirstName() {
        return this.patFirstName;
    }

    /**
     * Gibt Geschlecht des Patienten zurück.
     *
     * @return patGenderEn
     */
    @Column(name = "PAT_GENDER_EN", length = 1)
    @Enumerated(EnumType.STRING)
    public GenderEn getPatGenderEn() {
        return this.patGenderEn;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "patient")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference(value = "patDetails")
    public Set<TPatientDetails> getPatientDetailList() {
        return this.patientDetailList;
    }

    /**
     * Gibt Identifikationsnummer des Patienten zurück.
     *
     * @return patNumber: .
     */
    @Column(name = "PAT_NUMBER", nullable = false, length = 50)
    public String getPatNumber() {
        return this.patNumber;
    }

    /**
     * Gibt Name des Patienten zurück.
     *
     * @return patSecName
     */
    @Column(name = "PAT_SEC_NAME", length = 50)
    public String getPatSecName() {
        return this.patSecName;
    }

    //2017-04-19 DNi: Syntactic sugar :-)
    /**
     * Returns patient name as "family name, first name"
     *
     * @return patient name
     */
    @Transient
    public String getPatFullName() {
        return getPatFullName(getPatFirstName(), getPatSecName());
    }
    @Transient
    public String getPatAbbrFirstName(){
        String firstName = getPatFirstName() == null ? "" : getPatFirstName();
        return Lang.abbreviate(firstName, 1);
    }
    @Transient
    public String getPatAbrrName() {
        return getPatFullName(getPatAbbrFirstName(), getPatSecName());
    }
    
    public String getPatFullName(String pFirstName, String pSecName) {
        String firstName = pFirstName == null ? "" : pFirstName.trim();
        String secName = pSecName == null ? "" : pSecName.trim();

        if (firstName.isEmpty() && secName.isEmpty()) {
            return "";
        }
        if (firstName.isEmpty()) {
            return secName;
        }
        if (secName.isEmpty()) {
            return firstName;
        }

        return Lang.capitalize(secName + ", " + firstName);
    }
    
    /**
     * Gibt Titel des Patienten zurück.
     *
     * @return patTitle
     */
    @Column(name = "PAT_TITLE", length = 50)
    public String getPatTitle() {
        return this.patTitle;
    }

    /*
@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
@JoinColumn(name = "PAT_INS_ACT_ID", nullable = true)
public TInsurance getPatInsuranceActual() {
    return patInsAct;
}

@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
@JoinColumn(name = "PAT_DETAILS_ACT_ID", nullable = true)
public TPatientDetails getPatDetailsActual() {
    return patDetAct;
}

    public void setPatInsuranceActual(TInsurance patInsAct) {
        this.patInsAct = patInsAct;
    }

    public void setPatDetailsActual(TPatientDetails patDetAct) {
        this.patDetAct = patDetAct;
    }
     */
    /**
     * Convient Methode, creats new Insurance entity when the Patient dont have
     * an insurance with given insurance Ident newly created Insurance is not
     * set as new actualInsurance
     *
     * @param ikKrankenkasse insurance Ident
     */
    public void setActiveInsurance(final String ikKrankenkasse) {
        final String ik = ikKrankenkasse == null ? "" : ikKrankenkasse.trim();
        //maybe a check of ik.isEmpty() would be useful here to prevent empty insurance entries
        TInsurance ins = getActiveInsuranceByIk(ik);
        if (ins == null) {
            final TInsurance insurance = new TInsurance();
            insurance.setPatient(this);
            insurance.setInsInsuranceCompany(ik);
            setCurrentInsurance(insurance);
        }
    }

    public void setCases(final Set<TCase> cases) {
        this.cases = cases;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public void setInsurances(final Set<TInsurance> insurances) {
        this.insurances = insurances;
    }

    public void setPatDateOfBirth(final Date patDateOfBirth) {
        this.patDateOfBirth = patDateOfBirth == null ? null : new Date(patDateOfBirth.getTime());
    }

    public void setPatFirstName(final String patFirstName) {
        this.patFirstName = patFirstName;
    }

    public void setPatGenderEn(final GenderEn patGenderEn) {
        this.patGenderEn = patGenderEn;
    }

    public void setPatientDetailList(final Set<TPatientDetails> patientDetails) {
        this.patientDetailList = patientDetails;
    }

    public void setPatNumber(final String patNumber) {
        this.patNumber = patNumber;
    }

    public void setPatSecName(final String patSecName) {
        this.patSecName = patSecName;
    }

    public void setPatTitle(final String patTitle) {
        this.patTitle = patTitle;
    }

    @Override
    public boolean versionEquals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TPatient)) {
            return false;
        }
        final TPatient other = (TPatient) obj;
        if (!Objects.equals(this.patNumber, other.patNumber)) {
            return false;
        }
        if (!Objects.equals(this.patTitle, other.patTitle)) {
            return false;
        }
        if (!Objects.equals(this.patSecName, other.patSecName)) {
            return false;
        }
        if (!Objects.equals(this.patFirstName, other.patFirstName)) {
            return false;
        }
        if (!Objects.equals(this.patDateOfBirth, other.patDateOfBirth)) {
            return false;
        }
        if (this.patGenderEn != other.patGenderEn) {
            return false;
        }
        return true;
    }

    @Transient
    public String getPatientKey() {
        String patientKey = patNumber == null ? "NULL" : patNumber.trim();
        return patientKey;
    }

    @Transient
    public String getPatientSignature() {
        String patientKey = getPatientKey();
        String patientId = String.valueOf(this.id);
        return "pat_" + patientKey + ";id_" + patientId;
    }

    @Override
    public String toString() {
        final String hash = Integer.toHexString(this.hashCode());
        final String signature = getPatientSignature();
        return signature + "@" + hash;
    }

}
