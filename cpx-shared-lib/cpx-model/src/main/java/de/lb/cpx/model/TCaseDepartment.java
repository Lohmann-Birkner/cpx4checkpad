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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import de.lb.cpx.model.enums.AdmissionModeEn;
import de.lb.cpx.model.util.ModelUtil;
import de.lb.cpx.server.commons.dao.AbstractCaseEntity;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

/**
 * TCaseDepartment initially generated at 21.01.2016 17:07:59 by Hibernate Tools
 * 3.2.2.GA
 * <p style="font-size:1em; color:green;"> Die Tabelle "T_CASE_DEPARTMENT"
 * speichert Informationen zu einer Abteilung. </p>
 */
@Entity
@Table(name = "T_CASE_DEPARTMENT",
        indexes = {
            @Index(name = "IDX_CASE_DEPAR4TCASE_DETAIL_ID", columnList = "T_CASE_DETAILS_ID", unique = false)})
@SuppressWarnings("serial")
public class TCaseDepartment extends AbstractCaseEntity implements HospitalDevisionIF {

    private static final long serialVersionUID = 1L;

    private TCaseDetails caseDetails;
    private String depShortName;
    private String depKey301;
    private boolean depcIsAdmissionFl = false;
    private boolean depcIsDischargeFl = false;
    private boolean depcIsTreatingFl = false;
    private boolean depcIsBedIntensivFl = false;
    private Date depcAdmDate;
    private Date depcDisDate;
    private AdmissionModeEn depcAdmodEn = AdmissionModeEn.HA;
    private int depcHmv = 0; // breathing hours for department
//    private long id;
    private Integer depcLocateNumber = 0; // Number of Location from P21
    private Set<TCaseWard> caseWards = new HashSet<>(0);
    private Set<TCaseOps> caseOpses = new HashSet<>(0);
    private Set<TCaseIcd> caseIcds = new HashSet<>(0);

    public TCaseDepartment() {
        setIgnoreFields();
    }
    
    public TCaseDepartment(Long pCurrentUser){
        super(pCurrentUser);
        setIgnoreFields();
    }
    

    @Override
    protected final void setIgnoreFields() {
         ignoredFields = new String[]{"caseDetails", "principalCaseIcd"};
        }   
    
    public void addCaseIcds(final List<TCaseIcd> newCaseIcds) {
        for (final TCaseIcd tCaseIcd : newCaseIcds) {
            tCaseIcd.setCaseDepartment(this);
            caseIcds.add(tCaseIcd);

        }
    }

    public void addOpsList(final List<TCaseOps> newOpsList) {
        for (final TCaseOps tCaseOps : newOpsList) {
            tCaseOps.setCaseDepartment(this);
            getCaseOpses().add(tCaseOps);
        }
    }

    /**
     * Gibt Verweis auf die ID der Tabelle T_CASE_DETAILS zurück.
     *
     * @return caseDetails
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_CASE_DETAILS_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_CASE_DEP4T_CASE_DETAILS_ID"))
    @JsonBackReference(value = "dep")
    public TCaseDetails getCaseDetails() {
        return this.caseDetails;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseDepartment")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference(value = "icd")
    public Set<TCaseIcd> getCaseIcds() {
        return this.caseIcds;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseDepartment")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference(value = "ops")
    public Set<TCaseOps> getCaseOpses() {
        return this.caseOpses;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseDepartment")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference(value = "ward")
    public Set<TCaseWard> getCaseWards() {
        return this.caseWards;
    }

    /**
     * Gibt Datum zurück, an dem die Bewegung begann.
     *
     * @return depcAdmDate
     */
    //@Temporal(TemporalType.DATE)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DEPC_ADM_DATE", length = 7, nullable = false)
    public Date getDepcAdmDate() {
        return this.depcAdmDate;
    }

    /**
     * Gibt Fachabteilungstyp 1-7 zurück.d.h. HA, Bo, HaBh,
     * BoBa,BoBh,BoBaBh,HaBha.
     *
     * @return depcAdmodEn
     */
    @Column(name = "DEPC_ADMOD_EN", length = 25, nullable = false)
    @Enumerated(EnumType.STRING)
    public AdmissionModeEn getDepcAdmodEn() {
        return this.depcAdmodEn;
    }

    /**
     * Gibt Entlassungsdatum aus der Abteilung zurück.
     *
     * @return depcDisDate
     */
    //@Temporal(TemporalType.DATE)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DEPC_DIS_DATE", length = 7, nullable = true)
    public Date getDepcDisDate() {
        return this.depcDisDate;
    }

    /**
     * Gibt Flag 0/1 zurück, ob diese Abteilung auch die aufnehmende Abteilung
     * ist .
     *
     * @return depcIsAdmissionFl
     */
    @Column(name = "DEPC_IS_ADMISSION_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean getDepcIsAdmissionFl() {
        return this.depcIsAdmissionFl;
    }

    /**
     * Gibt Flag 0/1 zurück, ob diese Abteilung auch die entlassende Abteilung
     * ist.
     *
     * @return depcIsDischargeFl
     */
    @Column(name = "DEPC_IS_DISCHARGE_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean getDepcIsDischargeFl() {
        return this.depcIsDischargeFl;
    }

    /**
     * Gibt Flag 0/1 zurück , ob diese Abteilung auch die behandelnde Abteilung
     * ist.
     *
     * @return depcIsTreatingFl
     */
    @Column(name = "DEPC_IS_TREATING_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean getDepcIsTreatingFl() {
        return this.depcIsTreatingFl;
    }

    /**
     * Gibt Flag 0/1 zurück , Intensivbett
     *
     * @return depcIsBedIntensivFl
     */
    @Column(name = "DEPC_IS_BED_INTENSIV_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean getDepcIsBedIntensivFl() {
        return this.depcIsBedIntensivFl;
    }

    /**
     * Kurzname der (Fach)Abteilung im KH
     *
     * @return depShortName
     */
    @Column(name = "DEP_SHORT_NAME", nullable = true, length = 10)
    public String getDepShortName() {
        return this.depShortName;
    }

    /**
     * Fachabteilungsschlüssel nach §301
     *
     * @return depKey301
     */
    @Column(name = "DEP_KEY_301", nullable = false, length = 10)
    public String getDepKey301() {
        return this.depKey301;
    }

    @Transient
    public String getDepCodes() {
        return getDepKey301() + (getDepShortName() != null ? "/" + getDepShortName() : "");
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_CASE_DEPARTMENT_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    /**
     *
     * @param caseDetails Column T_CASE_DETAILS_ID :Verweis auf die ID der
     * Tabelle T_CASE_DETAILS.
     */
    public void setCaseDetails(final TCaseDetails caseDetails) {
        this.caseDetails = caseDetails;
    }

    @Override
    public void setCaseIcds(final Set<TCaseIcd> caseIcds) {
        this.caseIcds = caseIcds;
    }

    @Override
    public void setCaseOpses(final Set<TCaseOps> caseOpses) {
        this.caseOpses = caseOpses;
    }

    public void setCaseWards(final Set<TCaseWard> caseWards) {
        this.caseWards = caseWards;
    }

    /**
     *
     * @param depcAdmDate Column DEPC_ADM_DATE: Datum, an dem die Bewegung
     * begann.
     */
    public void setDepcAdmDate(final Date depcAdmDate) {
        this.depcAdmDate = depcAdmDate == null ? null : new Date(depcAdmDate.getTime());
    }

    /**
     *
     * @param depcAdmodEn Column DEPC_ADMOD_EN: Enumeration für
     * Fachabteilungstyp 1-7 ( HA, Bo, HaBh, BoBa,BoBh,BoBaBh,HaBha)
     */
    public void setDepcAdmodEn(final AdmissionModeEn depcAdmodEn) {
        this.depcAdmodEn = depcAdmodEn;
    }

    /**
     *
     * @param depcDisDate Column DEPC_DIS_DATE:Entlassungsdatum aus der
     * Abteilung.
     */
    public void setDepcDisDate(final Date depcDisDate) {
        this.depcDisDate = depcDisDate == null ? null : new Date(depcDisDate.getTime());
    }

    /**
     *
     * @param depcIsAdmissionFl Column DEPC_IS_ADMISSION_FL: 0/1 ,ob diese
     * Abteilung auch die aufnehmende Abteilung ist
     */
    public void setDepcIsAdmissionFl(final boolean depcIsAdmissionFl) {
        this.depcIsAdmissionFl = depcIsAdmissionFl;
    }

    /**
     *
     * @param depcIsDischargeFl Column DEPC_IS_DISCHARGE_FL :0/1, ob diese
     * Abteilung auch die entlassende Abteilung ist
     */
    public void setDepcIsDischargeFl(final boolean depcIsDischargeFl) {
        this.depcIsDischargeFl = depcIsDischargeFl;
    }

    /**
     *
     * @param depcIsTreatingFl Column DEPC_IS_TREATING_FL: 0/1,ob diese
     * Abteilung auch die behandelnde Abteilung ist.
     */
    public void setDepcIsTreatingFl(final boolean depcIsTreatingFl) {
        this.depcIsTreatingFl = depcIsTreatingFl;
    }

    /**
     *
     * @param depcIsBedIntensivFl Column DEPC_IS_BED_INTENSIV_FL: 0/1,
     * Intensivbett
     */
    public void setDepcIsBedIntensivFl(final boolean depcIsBedIntensivFl) {
        this.depcIsBedIntensivFl = depcIsBedIntensivFl;
    }

    /**
     *
     * @param depShortName Column DEP_SHORT_NAME: Kurzname der (Fach)Abteilung
     * im KH
     */
    public void setDepShortName(final String depShortName) {
        this.depShortName = depShortName;
    }

    /**
     *
     * @param depKey301 Column DEP_KEY_301: Fachabteilungsschlüssel nach §301
     */
    public void setDepKey301(final String depKey301) {
        this.depKey301 = depKey301;
    }

    public void setId(final long id) {
        this.id = id;
    }

    /**
     * Gibt Beatmungsstunden in der Abteilung zurück.
     *
     * @return depcHmv
     */
    @Column(name = "DEPC_HMV", precision = 3, scale = 0, nullable = false)
    public int getDepcHmv() {
        return depcHmv;
    }

    /**
     *
     * @param depcHmv Column DEPC_HMV: Beatmungsstunden in der Abteilung.
     */
    public void setDepcHmv(int depcHmv) {
        this.depcHmv = depcHmv;
    }

    /**
     * Gibt Standort-Nummer aus §21 zurück.
     *
     * @return depcLocateNumber
     */
    @Column(name = "DEPC_LOCATE_NUMBER", precision = 9, scale = 0, nullable = true)
    public Integer getDepcLocateNumber() {
        return this.depcLocateNumber;
    }

    /**
     *
     * @param depcLocateNumber Column DEPC_LOCATE_NUMBER: 
     * Standortnummer uas dem §21-Datensatz ab Version 2020.
     */
    public void setDepcLocateNumber(final Integer depcLocateNumber) {
        this.depcLocateNumber = depcLocateNumber;
    }

    /**
     * berechnet das Dauer der Abteilungsaufenthalt als Long
     *
     * @return duration
     */
    @javax.persistence.Transient
    @JsonIgnore
    public long getDurationLong() {

        if (this.depcAdmDate == null || this.depcDisDate == null) {
            return 0;
        }

        return depcDisDate.getTime() - depcAdmDate.getTime();
    }

    /**
     * Clones this object
     *
     * @return cloned object
     * @throws CloneNotSupportedException cloning exception
     */
    @Override
    public Object clone() throws CloneNotSupportedException {

        TCaseDepartment cloned = (TCaseDepartment) super.clone();

        if (!caseWards.isEmpty()) {
            Set<TCaseWard> clonedWards = new HashSet<>(0);
            for (TCaseWard ward : caseWards) {
                TCaseWard cloneWard = (TCaseWard) ward.clone();
                cloneWard.setCaseDepartment(cloned);
                clonedWards.add(cloneWard);
// OPS und ICDs der Station müssen auch die Abteilung referenzieren
                cloneWard.getCaseOpses().forEach((ops) -> {
                    ops.setCaseDepartment(cloned);
                });
                cloneWard.getCaseIcds().forEach((icd) -> {
                    icd.setCaseDepartment(cloned);
                });
            }
        } else {
            ModelUtil.cloneAndCheckIcdsOps4Case(caseOpses, caseIcds, cloned);

        }
        return cloned;
    }

    @Override
    public Object cloneWithoutIds(Long currentCpxUserId) throws CloneNotSupportedException {

        TCaseDepartment cloned = (TCaseDepartment) super.cloneWithoutIds(currentCpxUserId);
        cloned.setCaseWards(null);
        cloned.setId(0);
        cloned.setCaseIcds(null);
        cloned.setCaseOpses(null);
        Set<TCaseWard> clonedWards = new HashSet<>(caseWards.size());
        if(caseWards.isEmpty()){
             ModelUtil.cloneWithoutIdAndCheckIcdsOps4Case(this.getCaseOpses(), this.getCaseIcds(), cloned, currentCpxUserId);
        }else{
           
            for (TCaseWard ward : caseWards) {
                TCaseWard cloneWard = (TCaseWard) ward.cloneWithoutIds(currentCpxUserId);
                cloneWard.setCaseDepartment(cloned);
                clonedWards.add(cloneWard);
                // OPS und ICDs der Station müssen auch die Abteilung referenzieren
                cloneWard.getCaseOpses().forEach((ops) -> {
                    ops.setCaseDepartment(cloned);
                });
                cloneWard.getCaseIcds().forEach((icd) -> {
                    icd.setCaseDepartment(cloned);
                });
            }
            
                }
        cloned.setCaseWards(clonedWards);
        return cloned;
    }
    
    @Transient
    public Set<TCaseIcd> getAllIcds(){
        if(getCaseWards().isEmpty() ){
            return getCaseIcds();
        }
        Set<TCaseIcd> allIcds = new HashSet<>();
        for(TCaseWard ward: caseWards){
            if(ward.getCaseIcds() != null){
                allIcds.addAll(ward.getCaseIcds());
            }
        }
        return allIcds;
    }
    
    @Transient
    public Set<TCaseOps> getAllOps(){
        if(getCaseWards().isEmpty() ){
            return getCaseOpses();
        }
        Set<TCaseOps> allOpses = new HashSet<>();
        for(TCaseWard ward: caseWards){
            if(ward.getCaseOpses() != null){
                allOpses.addAll(ward.getCaseOpses());
            }
        }
        return allOpses;
    }
    
    @javax.persistence.Transient
    @JsonIgnore
    public TCaseIcd getPrincipalCaseIcd() {
        if (caseWards != null && !caseWards.isEmpty()) {
            TCaseIcd icd = null;
            for (TCaseWard ward : caseWards) {
                if ((icd = ward.getPrincipalCaseIcd()) != null) {
                    return icd;
                }
            }

        } else {
            if (caseIcds == null) {
                return null;
            }
            for (TCaseIcd icd : caseIcds) {
                if (icd.getIcdcIsHdxFl()) {
                    return icd;
                }
            }
        }
        return null;
    }

    @Transient
    @JsonIgnore
    public boolean isPseudo() {
        return Objects.equals(depKey301, "0001") || Objects.equals(depKey301, "0002") || Objects.equals(depKey301, "0003") || Objects.equals(depKey301, "0004");
    }

    @Transient
    @JsonIgnore
    public boolean isHospital() {
        return Objects.equals(depKey301, "0000");
    }

    @Transient
    @JsonIgnore
    public boolean isHA() {
        return Objects.equals(depcAdmodEn, AdmissionModeEn.HA);
    }

    @Transient
    @JsonIgnore
    public boolean isTreating() {
        caseDetails.setDepartmentFlags();
        return Objects.equals(depcIsTreatingFl, true);
    }

    @Override
    public boolean versionEquals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TCaseDepartment)) {
            return false;
        }
        final TCaseDepartment other = (TCaseDepartment) obj;
        if (this.depcIsAdmissionFl != other.getDepcIsAdmissionFl()) {
            return false;
        }
        if (this.depcIsDischargeFl != other.getDepcIsDischargeFl()) {
            return false;
        }
        if (this.depcIsTreatingFl != other.getDepcIsTreatingFl()) {
            return false;
        }
        if (!isStringEquals(this.depShortName, other.getDepShortName())) {
            return false;
        }
        if (!isStringEquals(this.depKey301, other.getDepKey301())) {
            return false;
        }
        if (!Objects.equals(this.depcAdmDate, other.getDepcAdmDate())) {
            return false;
        }
        if (!Objects.equals(this.depcDisDate, other.getDepcDisDate())) {
            return false;
        }
        if (this.depcAdmodEn != other.getDepcAdmodEn()) {
            return false;
        }
        if (!ModelUtil.versionSetEquals(this.caseWards, other.getCaseWards())) {
            return false;
        }
        if (!ModelUtil.versionSetEquals(this.caseOpses, other.getCaseOpses())) {
            return false;
        }
        if (!ModelUtil.versionSetEquals(this.caseIcds, other.getCaseIcds())) {
            return false;
        }
        if (this.depcHmv != other.getDepcHmv()) {
            return false;
        }
        if (this.depcLocateNumber != other.getDepcLocateNumber()) {
            return false;
        }
        return true;
    }

    @PrePersist
    @Override
    public void prePersist() {

    }

    @PostPersist
    public void postPersist() {

//        if (depcIsAdmissionFl) {
//            caseDetails.setID_DEP_ADMI(((Long) id).intValue());
//        }
//        if (depcIsDischargeFl) {
//            caseDetails.setID_DEP_DISC(((Long) id).intValue());
//        }
//        if (depcIsTreatingFl) {
//            caseDetails.setID_DEP_TREAT(((Long) id).intValue());
//        }
    }

    @PreRemove
    public void preRemove() {
//        if (depcIsAdmissionFl) {
//            caseDetails.setID_DEP_ADMI(null);
//        }
//        if (depcIsDischargeFl) {
//            caseDetails.setID_DEP_DISC(null);
//        }
//        if (depcIsTreatingFl) {
//            caseDetails.setID_DEP_TREAT(null);
//        }
        //20170529 Awi CPX-426: clear array before deleting to avoid ConcurrentModifactionException, when delete child elements
        getCaseIcds().clear();
    }
    
    @Transient
    @Override
    public Date getStartDate(){
        return this.getDepcAdmDate();
    }
    
    
    @Transient
    @Override
    public Date getEndDate(){
        return this.getDepcDisDate();
    }

}
