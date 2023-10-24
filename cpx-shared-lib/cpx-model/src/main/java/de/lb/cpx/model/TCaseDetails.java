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
import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.model.converter.AdmissionByLawConverter;
import de.lb.cpx.model.converter.AdmissionReason2Converter;
import de.lb.cpx.model.converter.AdmissionReasonConverter;
import de.lb.cpx.model.converter.CaseDetailsCancelReasonConverter;
import de.lb.cpx.model.converter.DischargeReason2Converter;
import de.lb.cpx.model.converter.DischargeReasonConverter;
import de.lb.cpx.model.enums.AdmissionByLawEn;
import de.lb.cpx.model.enums.AdmissionCauseEn;
import de.lb.cpx.model.enums.AdmissionModeEn;
import de.lb.cpx.model.enums.AdmissionReason2En;
import de.lb.cpx.model.enums.AdmissionReasonEn;
import de.lb.cpx.model.enums.CaseDetailsCancelReasonEn;
import de.lb.cpx.model.enums.DischargeReason2En;
import de.lb.cpx.model.enums.DischargeReasonEn;
import de.lb.cpx.model.enums.GenderEn;
import de.lb.cpx.model.enums.VersionRiskTypeEn;
import de.lb.cpx.model.util.ModelUtil;
import de.lb.cpx.server.commons.dao.AbstractCaseEntity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
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
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

/**
 * TCaseDetails initially generated at 21.01.2016 17:07:59 by Hibernate Tools
 * 3.2.2.GA
 * <p style="font-size:1em; color:green;">Die Tabelle "T_CASE_DETAILS" speichert
 * alle Fälle, die, wie in die Tabelle T_CASE , in die Datenbank eingelesen
 * wurden und die durch CPX bearbeitet wurden/ Außerdem werden hier Details zu
 * den Fällen gespeichert. </p>
 */
@Entity
@Table(name = "T_CASE_DETAILS",
        indexes = {
            @Index(name = "IDX_CASE_DETAILS4TCASE_ID", columnList = "T_CASE_ID", unique = false),
            @Index(name = "IDX_CASE_DET4PARENT_ID", columnList = "PARENT_ID", unique = false)}
/*, indexes = {
        @Index(columnList = "CSD_IS_ACTUAL_FL", name = "TCD_IS_ACTUAL_FL_IDX"),
        @Index(columnList = "CSD_IS_LOCAL_FL", name = "TCD_IS_LOCAL_FL_IDX")
       
}  */
/*, uniqueConstraints = {
  @UniqueConstraint(columnNames = {"HOSC_ID", "CSD_IS_LOCAL_FL"}) //
} */)
//@Check(constraints = "CSD_IS_LOCAL_FL IN (0, 1)")
@NamedEntityGraph(
        name = "fetchBatchDetailGraph",
        attributeNodes = {
            @NamedAttributeNode(value = "caseDepartments"
            //                    , subgraph = "detailsGraph"),
            //        },
            //        subgraphs = {
            //            @NamedSubgraph(name = "detailsGraph",attributeNodes = {
            //                        @NamedAttributeNode(value = "caseOpses"),
            //                        @NamedAttributeNode(value = "caseIcds"
            //
            //                    
            )
        })
//        }
//    )
@SuppressWarnings("serial")
public class TCaseDetails extends AbstractCaseEntity {

    private static final Logger LOG = Logger.getLogger(TCaseDetails.class.getName());

    private static final long serialVersionUID = 1L;
    //    @PrePersist
//    public void prePersist() {
//        Set<TCaseDepartment> caseDepartments1 = getCaseDepartments();
//        int sum_sd = 0;
//        int sum_ops = 0;
//        for (TCaseDepartment c : caseDepartments1) {
//            sum_sd += c.getCaseIcds().size();
//            sum_ops += c.getCaseOpses().size();
//        }
//        if (sum_sd > 1) {
//            sum_sd--;
//        }
//        setSumOfOps(sum_ops);
//        SetSumOfIcd(sum_sd);
//    }

//    private long id;
    private TCaseDetails caseDetailsByCsdParentId;
    private TCase hospitalCase;
    private TCaseDetails caseDetailsByCsdExternId;
    //private String csdInsCompany;
    private Date csdAdmissionDate;
    private Date csdDischargeDate;
    private Integer csdAdmissionWeight = 0;
    private Integer csdAgeYears = 0;
    private Integer csdAgeDays = 0;
    private Integer csdHmv = 0;
    private Long csdLos = 0L;
    private Integer csdLeave = 0;
    private Long csdLosAlteration = 0L;
    private AdmissionReasonEn csdAdmReason12En = AdmissionReasonEn.ar01;
    private AdmissionCauseEn csdAdmCauseEn = AdmissionCauseEn.E;
    private DischargeReasonEn csdDisReason12En = DischargeReasonEn.dr01;
    private AdmissionModeEn csdAdmodEn = AdmissionModeEn.HA;
    private AdmissionByLawEn csdAdmLawEn = AdmissionByLawEn.Freiwillig;
    private int csdVersion = 1;
    private boolean csdIsLocalFl = false;
    private boolean csdIsActualFl = false;
    private AdmissionReason2En csdAdmReason34En = AdmissionReason2En.ar201;
    private DischargeReason2En csdDisReason3En = DischargeReason2En.dr201;
    private Set<TCaseDepartment> caseDepartments = new HashSet<>(0);
    private Set<TGroupingResults> groupingResultses = new HashSet<>(0);
    private Set<TCaseFee> caseFees = new HashSet<>(0);
    private Set<TCaseBill> caseBills = new HashSet<>(0);
    private Set<TCaseDetails> caseDetailsesForCsdExternId = new HashSet<>(0);
    private Set<TCaseDetails> caseDetailsesForCsdParentId = new HashSet<>(0);
    private String csdTransferringHospIdent; // ident of the transferring hospital
    private Date csdDateOfAccident; // date of accident
    private int csdLosIntensiv = 0;
    private String comment;
    private Integer sumOfIcd = 0;
    private Integer sumOfOps = 0;
    private String HdIcdCode;
    private CaseDetailsCancelReasonEn csdCancelReasonEn = null;
    private Date csdCancelDate = null;
    private GenderEn csdGenderEn;
    private Integer csdLosMdAlteration;
    //CSD_VERS_RISK_TYPE
    private VersionRiskTypeEn csdVersRiskTypeEn;
    private Boolean hasTransferFlag4Merge = false;
//    private Set <TWmRisk> wmRisks; 
            
    public TCaseDetails() {
    
        setIgnoreFields();
    }
    
    public TCaseDetails(Long pCurrentUser){
        super(pCurrentUser);
        setIgnoreFields();
    }
    

    @Override
    protected final void setIgnoreFields() {
        ignoredFields = new String[]{
            "caseDetailsByCsdParentId",
            "caseDetailsByCsdExternId",
            "csdLos",
            "csdVersion",
            "csdIsLocalFl",
            "groupingResultses",
            "caseDetailsesForCsdExternId",
            "caseDetailsesForCsdParentId",
            "hospitalCase",
            "principalCaseIcd",
            "sortedDepartments"
        };
    }   

    public void addDepartment(final TCaseDepartment caseDepartment) {
        getCaseDepartments().add(caseDepartment);
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseDetails")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference(value = "bill")
    public Set<TCaseBill> getCaseBills() {
        return this.caseBills;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseDetails")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference(value = "dep")
    public Set<TCaseDepartment> getCaseDepartments() {
        return this.caseDepartments;
    }

    @Transient
    public TCaseDepartment getCaseDepartmentDischarge() {
        final List<TCaseDepartment> result = new ArrayList<>();
        this.caseDepartments.forEach((dep) -> {
            if (dep != null && dep.getDepcIsDischargeFl()) {
                result.add(dep);
            }
        });
        if (result.isEmpty()) {
            return null;
        }
        if (result.size() > 1) {
            LOG.log(Level.WARNING, "multiple ({0}) discharging departments found for case details id {1} -> will pick up first one", new Object[]{result.size(), id});
        }
        return result.iterator().next();
    }

    @Transient
    public TCaseDepartment getCaseDepartmentTreating() {
        final List<TCaseDepartment> result = new ArrayList<>();
        this.caseDepartments.forEach((dep) -> {
            if (dep != null && dep.getDepcIsTreatingFl()) {
                result.add(dep);
            }
        });
        if (result.isEmpty()) {
            return null;
        }
        if (result.size() > 1) {
            LOG.log(Level.WARNING, "multiple ({0}) treating departments found for case details id {1} -> will pick up first one", new Object[]{result.size(), id});
        }
        return result.iterator().next();
    }

    @Transient
    public TCaseDepartment getCaseDepartmentAdmission() {
        final List<TCaseDepartment> result = new ArrayList<>();
        this.caseDepartments.forEach((dep) -> {
            if (dep != null && dep.getDepcIsAdmissionFl()) {
                result.add(dep);
            }
        });
        if (result.isEmpty()) {
            return null;
        }
        if (result.size() > 1) {
            LOG.log(Level.WARNING, "multiple ({0}) admission departments found for case details id {1} -> will pick up first one", new Object[]{result.size(), id});
        }
        return result.iterator().next();
    }

    /**
     * Gibt Referenz von Lokalfall (CPX-FALL) auf seinen externe Fall (KIS-Fall)
     * in der selben Tabelle T_CASE_DETAILS zurück.
     *
     * @return caseDetailsByCsdExternId
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXTERN_ID", nullable = true)
    @JsonIgnore
    public TCaseDetails getCaseDetailsByCsdExternId() {
        return this.caseDetailsByCsdExternId;
    }

    /**
     * Gibt Referenz von lokalen Fall(neue Version) auf die übergeordnete knote
     * ( lokal oder extern) in der selben Tabelle T_CASE_DETAILS zurück.
     *
     * @return caseDetailsByCsdParentId
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_CASE_DET4CASE_DET"))
    @JsonIgnore
    public TCaseDetails getCaseDetailsByCsdParentId() {
        return this.caseDetailsByCsdParentId;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseDetailsByCsdExternId")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JsonIgnore
    public Set<TCaseDetails> getCaseDetailsesForCsdExternId() {
        return this.caseDetailsesForCsdExternId;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseDetailsByCsdParentId")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JsonIgnore
    public Set<TCaseDetails> getCaseDetailsesForCsdParentId() {
        return this.caseDetailsesForCsdParentId;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseDetails")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TCaseFee> getCaseFees() {
        return this.caseFees;
    }

    /**
     * Gibt Aufnahmeanlass zurück,d.h.Die Ursache, warum ein Patient aufgenommen
     * wurde
     *
     * @return csdAdmCauseEn
     */
    @Column(name = "ADMISSION_CAUSE_EN", nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    public AdmissionCauseEn getCsdAdmCauseEn() {
        return this.csdAdmCauseEn;
    }

    /**
     * Gibt Aufnamedatum zurück
     *
     * @return csdAdmissionDate
     */
    //@Temporal(TemporalType.DATE)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ADMISSION_DATE", length = 7, nullable = false)
    public Date getCsdAdmissionDate() {
        return this.csdAdmissionDate;
    }

    @Transient
    @JsonIgnore
    public Integer getCsdAdmissionYear() {
        Date admissionDate = csdAdmissionDate;
        if (admissionDate == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(admissionDate);
        return cal.get(Calendar.YEAR);
    }

    /**
     * Gibt Aufnahmegewicht zurück
     *
     * @return csdAdmissionWeight
     */
    @Column(name = "ADMISSION_WEIGHT", precision = 4, scale = 0)
    public Integer getCsdAdmissionWeight() {
        return this.csdAdmissionWeight;
    }

    /**
     * Gibt Gesätzliche Psychstatus zurück
     *
     * @return csdAdmLawEn
     */
    @Column(name = "ADMISSION_LAW_EN", length = 1, nullable = false)
    @Convert(converter = AdmissionByLawConverter.class)
    public AdmissionByLawEn getCsdAdmLawEn() {
        return this.csdAdmLawEn;
    }

    /**
     * Gibt Erbringungsart 1-7 zurück, z.b.HA,HaBh,Bo,BoBa,BoBh,BoBaBh,HaBhZ .
     *
     * @return ADMISSION_MODE_EN
     */
    @Column(name = "ADMISSION_MODE_EN", nullable = false, length = 25)
    @Enumerated(EnumType.STRING)
    public AdmissionModeEn getCsdAdmodEn() {
        return this.csdAdmodEn;
    }

    /**
     * Gibt Aufnahmegrund12 nach §301 des 5. Sozialgesetzbuches (SGB) zurück.
     *
     * @return csdAdmReason12En
     */
    @Column(name = "ADMISSION_REASON_12_EN", nullable = false, length = 2)
    @Convert(converter = AdmissionReasonConverter.class)
    public AdmissionReasonEn getCsdAdmReason12En() {
        return this.csdAdmReason12En;
    }

    /**
     * Gibt Aufnahmegrund34 nach §301 des 5. Sozialgesetzbuches (SGB) zurück.
     *
     * @return csdAdmReason34En:
     */
    @Column(name = "ADMISSION_REASON_34_EN", nullable = false, length = 5)
    @Convert(converter = AdmissionReason2Converter.class)
    public AdmissionReason2En getCsdAdmReason34En() {
        return this.csdAdmReason34En;
    }

    /**
     * Gibt Stornierungsgrund zurück.
     *
     * @return csdCancelReasonEn:
     */
    @Column(name = "CANCEL_REASON_EN", nullable = true, length = 10)
    @Convert(converter = CaseDetailsCancelReasonConverter.class)
    public CaseDetailsCancelReasonEn getCsdCancelReasonEn() {
        return this.csdCancelReasonEn;
    }

    /**
     * Gibt Alter in Tagen zurück. 0, wenn der Patient älter als 1 Jahr ist.
     *
     * @return csdAgeDays
     */
    @Column(name = "AGE_DAYS", precision = 3, scale = 0, nullable = false)
    public Integer getCsdAgeDays() {
        return this.csdAgeDays;
    }

    /**
     * Gibt Alter in Jahren zurück.
     *
     * @return csdAgeYears
     */
    @Column(name = "AGE_YEARS", precision = 3, scale = 0)
    public Integer getCsdAgeYears() {
        return this.csdAgeYears;
    }

    /**
     * Gibt Entlassungsdatum zurück
     *
     * @return csdDischargeDate
     */
    //@Temporal(TemporalType.DATE)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DISCHARGE_DATE", length = 7)
    public Date getCsdDischargeDate() {
        return this.csdDischargeDate;
    }

    /**
     * Gibt Entlassungsgrund nach § 301 V. Sozialgesetzbuch, 1. und 2. Stelle
     * zurück.
     *
     * @return csdDisReason12En
     */
    @Column(name = "DISCHARGE_REASON_12_EN", length = 2)
    @Convert(converter = DischargeReasonConverter.class)
    public DischargeReasonEn getCsdDisReason12En() {
        return this.csdDisReason12En;
    }

    /**
     * Gibt Entlassungsgrund nach § 301 V. Sozialgesetzbuch, 3. Stelle zurück.
     *
     * @return csdDisReason3En
     */
    @Column(name = "DISCHARGE_REASON_3_EN", length = 1)
    @Convert(converter = DischargeReason2Converter.class)
    public DischargeReason2En getCsdDisReason3En() {
        return this.csdDisReason3En;
    }

    /**
     * Gibt Beatmungsstunden zurück.
     *
     * @return csdHmv
     */
    @Column(name = "HMV", precision = 5, scale = 0)
    public Integer getCsdHmv() {
        return this.csdHmv;
    }

    /**
     * Gibt Nummer der Krankenkasse zurück , die in der Tabelle
     * CPX_COMMON.C_INSURANCE_COMPANY steht.
     *
     * @return csdInsCompany
     */
//    @Column(name = "CSD_INS_COMPANY", nullable = false, length = 10)
//    public String getCsdInsCompany() {
//        return this.csdInsCompany;
//    }
    /**
     * Gibt Flag 1/0 zurück, ob es ein local oder etern fall ist. extern(KIS
     * Fall)=0 / local(CPX Fall)=1
     *
     * @return csdIsLocalFl
     */
    @Column(name = "LOCAL_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean getCsdIsLocalFl() {
        return this.csdIsLocalFl;
    }

    /**
     * Gibt Flag 1/0 zurück, ob es die akulle Configuration des Falles ist.
     *
     * @return csdIsActualFl
     */
    @Column(name = "ACTUAL_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean getCsdIsActualFl() {
        return this.csdIsActualFl;
    }

    /**
     * Gibt Anzahl an Tagen zurück, die während der Behandlung/Aufenthalt nicht
     * im Krankenhaus verbracht wurden.
     *
     * @return csdLeave:
     */
    @Column(name = "LEAVE", precision = 10, scale = 0, nullable = false)
    public Integer getCsdLeave() {
        return this.csdLeave;
    }

    /**
     * Gibt Verweildauer zurück
     *
     * @return csdLos
     */
    @Column(name = "LOS", precision = 10, scale = 0, nullable = false)
    public Long getCsdLos() {
        return this.csdLos;
    }

    /**
     * Gibt Änderung der Verweildauer zurück
     *
     * @return csdLosAlteration
     */
    @Column(name = "LOS_ALTERATION", precision = 10, scale = 0, nullable = false)
    public Long getCsdLosAlteration() {
        return this.csdLosAlteration;
    }

    /**
     * Gibt Nummer der Versionierung zurück
     *
     * @return csdVersion
     */
    @Column(name = "VERSION_NUMBER", nullable = false, precision = 3, scale = 0)
    public int getCsdVersion() {
        return this.csdVersion;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseDetails")/*, orphanRemoval = true)*/
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TGroupingResults> getGroupingResultses() {
        return this.groupingResultses;
    }

    /**
     * Gibt Verweis auf die ID der Tabelle T_CASE zurück
     *
     * @return hospitalCase
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_CASE_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_CASE_DETAILS4T_CASE_ID"))
    @JsonBackReference(value = "details")
    public TCase getHospitalCase() {
        return this.hospitalCase;
    }

    /**
     * Gibt Verweildauer des Patienten auf der Intensivstation zurück.
     *
     * @return csdLosIntensiv
     */
    @Column(name = "LOS_INTENSIV", precision = 10, scale = 0, nullable = false)
    public int getCsdLosIntensiv() {
        return this.csdLosIntensiv;
    }

    /**
     * Gibt Beschreibung zu dem Fall zurück
     *
     * @return comment
     */
    @Column(name = "CSD_COMMENT", nullable = true, length = 800)
    public String getComment() {
        return this.comment;
    }

    /**
     * Column GENDER_EN
     *
     * @return csdGenderEn
     */
    @Column(name = "GENDER_EN", length = 1)
    @Enumerated(EnumType.STRING)
    public GenderEn getCsdGenderEn() {
        return this.csdGenderEn;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_CASE_DETAILS_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    @Column(name = "HAS_TRANSFER_4_MERGE_FL", precision = 1, scale = 0, nullable = true)
    @Type(type = "numeric_boolean")
    public Boolean getHasTransferFlag4Merge() {
        return hasTransferFlag4Merge;
    }

    public void setHasTransferFlag4Merge(Boolean hasTrnasferFlag4Merge) {
        this.hasTransferFlag4Merge = hasTrnasferFlag4Merge;
    }

    
    
//    @Column(name = "ID_DEP_ADMI")
//    public Integer getID_DEP_ADMI() {
//        return this.ID_DEP_ADMI;
//    }
//
//    public void setID_DEP_ADMI(Integer ID_DEP) {
//        this.ID_DEP_ADMI = ID_DEP;
//    }
//
//    @Column(name = "ID_DEP_TREAT")
//    public Integer getID_DEP_TREAT() {
//        return this.ID_DEP_TREAT;
//    }
//
//    public void setID_DEP_TREAT(Integer ID_DEP) {
//        this.ID_DEP_TREAT = ID_DEP;
//    }
//
//    @Column(name = "ID_DEP_DISC")
//    public Integer getID_DEP_DISC() {
//        return this.ID_DEP_DISC;
//    }
//
//    public void setID_DEP_DISC(Integer ID_DEP) {
//        this.ID_DEP_DISC = ID_DEP;
//    }
//
//    @Column(name = "ID_ICD_HDX", nullable = true)
//    public Long getID_ICD_HDX() {
//        return this.ID_ICD_HDX;
//    }
//
//    public void setID_ICD_HDX(Long ID_DEP) {
//        this.ID_ICD_HDX = ID_DEP;
//    }
    public void setCaseBills(final Set<TCaseBill> caseBills) {
        this.caseBills = caseBills;
    }

    public void setCaseDepartments(final Set<TCaseDepartment> caseDepartments) {
        this.caseDepartments = caseDepartments;
    }

    /**
     *
     * @param caseDetailsByCsdExternId Column EXTERN_ID :Referenz von Lokalfall
     * (CPX-FALL) auf seinen externe Fall (KIS-Fall) in der selben Tabelle
     * T_CASE_DETAILS
     */
    public void setCaseDetailsByCsdExternId(final TCaseDetails caseDetailsByCsdExternId) {
        this.caseDetailsByCsdExternId = caseDetailsByCsdExternId;
    }

    /**
     *
     * @param caseDetailsByCsdParentId Column EXTERN_ID : Referenz von lokalen
     * Fall(neue Version) auf die übergeordnete knote ( lokal oder extern) in
     * der Tabelle selben T_CASE_DETAILS
     */
    public void setCaseDetailsByCsdParentId(final TCaseDetails caseDetailsByCsdParentId) {
        this.caseDetailsByCsdParentId = caseDetailsByCsdParentId;
    }

    public void setCaseDetailsesForCsdExternId(final Set<TCaseDetails> caseDetailsesForCsdExternId) {
        this.caseDetailsesForCsdExternId = caseDetailsesForCsdExternId;
    }

    public void setCaseDetailsesForCsdParentId(final Set<TCaseDetails> caseDetailsesForCsdParentId) {
        this.caseDetailsesForCsdParentId = caseDetailsesForCsdParentId;
    }

    public void setCaseFees(final Set<TCaseFee> caseFees) {
        this.caseFees = caseFees;
    }

    /**
     *
     * @param csdAdmCauseEn Column ADM_CAUSE_EN : Aufnahmeanlass,d.h.Die
     * Ursache, warum ein Patient aufgenommen wurde, z.B. Notfall, Einweisung
     * durch Arzt etc. Der Aufnahmeanlass ist in § 301 geregelt.
     */
    public void setCsdAdmCauseEn(final AdmissionCauseEn csdAdmCauseEn) {
        this.csdAdmCauseEn = csdAdmCauseEn;
    }

    /**
     *
     * @param csdAdmissionDate Column ADMISSION_DATE: Datum, an dem die Aufnahme
     * in das Krankenhaus erfolgte.
     */
    public void setCsdAdmissionDate(final Date csdAdmissionDate) {
        this.csdAdmissionDate = csdAdmissionDate == null ? null : new Date(csdAdmissionDate.getTime());
    }

    /**
     *
     * @param csdAdmissionWeight Column ADMISSION_WEIGHT: Gewicht des Patienten
     * (bei Aufnahme).
     */
    public void setCsdAdmissionWeight(final Integer csdAdmissionWeight) {
        this.csdAdmissionWeight = csdAdmissionWeight;
    }

    /**
     *
     * @param csdAdmLawEn Column ADMISSION_LAW_EN	: Enumeration für Gesätzliche
     * Psychstatus 1-2 (Freiwillig,Unfreiwillig) , default 1.
     */
    public void setCsdAdmLawEn(final AdmissionByLawEn csdAdmLawEn) {
        this.csdAdmLawEn = csdAdmLawEn;
    }

    /**
     *
     * @param csdAdmodEn Column ADMISSION_MODE_EN: Erbringungsart 1-7
     * (HA,HaBh,Bo,BoBa,BoBh,BoBaBh,HaBha) ,default HA.
     */
    public void setCsdAdmodEn(final AdmissionModeEn csdAdmodEn) {
        this.csdAdmodEn = csdAdmodEn;
    }

    /**
     *
     * @param csdAdmReason12En Column ADMISSION_REASON_12_EN:
     * Aufnahmegrund12,d.h. Die Art, wie ein Patient aufgenommen wurde:
     * voll-/teilstationär, etc. Diese Spalte enthält die 1. und 2. Stelle des
     * Aufnahmegrundes nach §301 des 5. Sozialgesetzbuches (SGB), default 01.
     */
    public void setCsdAdmReason12En(final AdmissionReasonEn csdAdmReason12En) {
        this.csdAdmReason12En = csdAdmReason12En;
    }

    /**
     *
     * @param csdAdmReason34En Column ADMISSION_REASON_34_EN: Enumeration für
     * Aufnahmegrund34,d.h,Weitergehende Information zur Ursache des
     * Aufnahmeanlasses. Diese Spalte enthält die 3. Stelle des Aufnahmegrundes
     * nach §301 des 5. Sozialgesetzbuches (SGB).
     */
    public void setCsdAdmReason34En(final AdmissionReason2En csdAdmReason34En) {
        this.csdAdmReason34En = csdAdmReason34En;
    }

    /**
     *
     * @param csdCancelReasonEn Column CANCLE_REASON_EN: Stornierungsgrund
     */
    public void setCsdCancelReasonEn(final CaseDetailsCancelReasonEn csdCancelReasonEn) {
        this.csdCancelReasonEn = csdCancelReasonEn;
    }

    /**
     *
     * @param csdAgeDays Column AGE_DAYS: Alter in Tagen.
     */
    public void setCsdAgeDays(final Integer csdAgeDays) {
        this.csdAgeDays = csdAgeDays;
    }

    /**
     *
     * @param csdAgeYears Column AGE_YEARS :Alter des Patientes in Jahren.
     */
    public void setCsdAgeYears(final Integer csdAgeYears) {
        this.csdAgeYears = csdAgeYears;
    }

    /**
     *
     * @param csdDischargeDate Column DISCHARGE_DATE: Datum, an dem die
     * Entlassung aus dem Krankenhaus erfolgte.
     */
    public void setCsdDischargeDate(final Date csdDischargeDate) {
        this.csdDischargeDate = csdDischargeDate == null ? null : new Date(csdDischargeDate.getTime());
    }

    /**
     *
     * @param csdDisReason12En Column DISCHARGE_REASON_12_EN: Entlassungsgrund
     * nach § 301 V. Sozialgesetzbuch, 1. und 2. Stelle.
     */
    public void setCsdDisReason12En(final DischargeReasonEn csdDisReason12En) {
        this.csdDisReason12En = csdDisReason12En;
    }

    /**
     *
     * @param csdDisReason3En Column DISCHARGE_REASON_3_EN: Entlassungsgrund
     * nach § 301 V. Sozialgesetzbuch, 3. Stelle. 1=arbeitsfähig entlassen,
     * 2=arbeitsunfähig entlassen, 9=keine Angabe.
     */
    public void setCsdDisReason3En(final DischargeReason2En csdDisReason3En) {
        this.csdDisReason3En = csdDisReason3En;
    }

    /**
     *
     * @param csdHmv Column HMV: Beatmungsstunden,d.h. Dauer der Beatmung eines
     * Patienten in Stunden.
     */
    public void setCsdHmv(final Integer csdHmv) {
        this.csdHmv = csdHmv;
    }

    /**
     *
     * @param csdInsCompany Column CSD_INS_COMPANY: Nummer der Krankenkasse, die
     * in der Tabelle CPX_COMMON.C_INSURANCE_COMPANY steht.
     */
//    public void setCsdInsCompany(final String csdInsCompany) {
//        this.csdInsCompany = csdInsCompany;
//    }
    /**
     *
     * @param csdIsLocalFl Column LOCAL_FL : Flag für die Unterscheidung der
     * importierten (KIS=erterner Fall) und bearbeiteten Version (CPX
     * Fall=Local) des Falles.
     */
    public void setCsdIsLocalFl(final boolean csdIsLocalFl) {
        this.csdIsLocalFl = csdIsLocalFl;
    }

    /**
     *
     * @param csdIsActualFl Column ACTUAL_FL: Flag 1/0, die akulle Configuration
     * des Falles
     */
    public void setCsdIsActualFl(final boolean csdIsActualFl) {
        this.csdIsActualFl = csdIsActualFl;
    }

    /**
     *
     * @param csdLeave Column LEAVE: Anzahl an Tagen, die während der
     * Behandlung/Aufenthalt nicht im Krankenhaus verbracht wurden.
     */
    public void setCsdLeave(final Integer csdLeave) {
        this.csdLeave = csdLeave;
    }

    /**
     *
     * @param csdLos Column LOS:Verweildauer des Patienten im Krankenhaus.
     */
    public void setCsdLos(final Long csdLos) {
        this.csdLos = csdLos;
    }

    /**
     *
     * @param csdLosAlteration Column LOS_ALTERATION: Änderung der Verweildauer,
     * die der Benutzer zu dem berechneten Verweildauer bei der Simulation des
     * Falles angewendet hat.
     */
    public void setCsdLosAlteration(final Long csdLosAlteration) {
        this.csdLosAlteration = csdLosAlteration;
    }

    /**
     *
     * @param csdVersion Column VERSION:Nummer der Versionierung, falls mehrere
     * Versionen des Falles angelegt wurden, default 1.
     */
    public void setCsdVersion(final int csdVersion) {
        this.csdVersion = csdVersion;
    }

    public void setGroupingResultses(final Set<TGroupingResults> groupingResultses) {
        this.groupingResultses = groupingResultses;
    }

    /**
     *
     * @param hospitalCase Column T_CASE_DETAILS_ID:Verweis auf die ID der
     * Tabelle T_CASE.
     */
    public void setHospitalCase(final TCase hospitalCase) {
        this.hospitalCase = hospitalCase;
    }

    /**
     *
     * @param csdLosIntensiv Column LOS_INTENSIV: Verweildauer des Patienten auf
     * der Intensivstation.
     */
    public void setCsdLosIntensiv(final int csdLosIntensiv) {
        this.csdLosIntensiv = csdLosIntensiv;
    }

    /**
     *
     * @param csdGenderEn patient gender
     */
    public void setCsdGenderEn(final GenderEn csdGenderEn) {
        this.csdGenderEn = csdGenderEn;
    }

    public void setId(final long id) {
        this.id = id;
    }

    /**
     * sets comment in TCaseDetails Max lenght of Comment in Db is 800 chars
     *
     * @param comment comment to be set, if lenght &gt; 750 remaining chars get
     * cut off
     */
    public void setComment(String comment) {

        if (comment != null && comment.length() > 750) {
            comment = comment.substring(0, 749);
        }
        this.comment = comment;
    }
//CPX-693 RSH 21.11.2017 Extensions of the versionable case data should be stored in a separate table.
//    public void setCsdString1(final String pCsdString1) {
//        this.csdString1 = pCsdString1;
//    }
//
//    public void setCsdString2(final String pCsdString2) {
//        this.csdString2 = pCsdString2;
//    }
//
//    public void setCsdString3(final String pCsdString3) {
//        this.csdString3 = pCsdString3;
//    }
//
//    public void setCsdString4(final String pCsdString4) {
//        this.csdString4 = pCsdString4;
//    }
//
//    public void setCsdString5(final String pCsdString5) {
//        this.csdString5 = pCsdString5;
//    }
//
//    public void setCsdString6(final String pCsdString6) {
//        this.csdString6 = pCsdString6;
//    }
//
//    public void setCsdString7(final String pCsdString7) {
//        this.csdString7 = pCsdString7;
//    }
//
//    public void setCsdString8(final String pCsdString8) {
//        this.csdString8 = pCsdString8;
//    }
//
//    public void setCsdString9(final String pCsdString9) {
//        this.csdString9 = pCsdString9;
//    }
//
//    public void setCsdString10(final String pCsdString10) {
//        this.csdString10 = pCsdString10;
//    }
//
//    public void setCsdInt1(final Integer pCsdInt1) {
//        this.csdInt1 = pCsdInt1;
//    }
//
//    public void setCsdInt2(final Integer pCsdInt2) {
//        this.csdInt2 = pCsdInt2;
//    }
//
//    public void setCsdInt3(final Integer pCsdInt3) {
//        this.csdInt3 = pCsdInt3;
//    }
//
//    public void setCsdInt4(final Integer pCsdInt4) {
//        this.csdInt4 = pCsdInt4;
//    }
//
//    public void setCsdInt5(final Integer pCsdInt5) {
//        this.csdInt5 = pCsdInt5;
//    }
//
//    public void setCsdInt6(final Integer pCsdInt6) {
//        this.csdInt6 = pCsdInt6;
//    }
//
//    public void setCsdInt7(final Integer pCsdInt7) {
//        this.csdInt7 = pCsdInt7;
//    }
//
//    public void setCsdInt8(final Integer pCsdInt8) {
//        this.csdInt8 = pCsdInt8;
//    }
//
//    public void setCsdInt9(final Integer pCsdInt9) {
//        this.csdInt9 = pCsdInt9;
//    }
//
//    public void setCsdInt10(final Integer pCsdInt10) {
//        this.csdInt10 = pCsdInt10;
//    }
//
//    public void setCsdDouble1(final Double pCsdDouble1) {
//        this.csdDouble1 = pCsdDouble1;
//    }
//
//    public void setCsdDouble2(final Double pCsdDouble2) {
//        this.csdDouble2 = pCsdDouble2;
//    }
//
//    public void setCsdDouble3(final Double pCsdDouble3) {
//        this.csdDouble3 = pCsdDouble3;
//    }
//
//    public void setCsdDouble4(final Double pCsdDouble4) {
//        this.csdDouble4 = pCsdDouble4;
//    }
//
//    public void setCsdDouble5(final Double pCsdDouble5) {
//        this.csdDouble5 = pCsdDouble5;
//    }
//
//    public void setCsdDate1(final Date pCsdDate1) {
//        this.csdDate1 = pCsdDate1;
//    }
//
//    public void setCsdDate2(final Date pCsdDate2) {
//        this.csdDate2 = pCsdDate2;
//    }
//
//    public void setCsdDate3(final Date pCsdDate3) {
//        this.csdDate3 = pCsdDate3;
//    }
//
//    public void setCsdDate4(final Date pCsdDate4) {
//        this.csdDate4 = pCsdDate4;
//    }
//
//    public void setCsdDate5(final Date pCsdDate5) {
//        this.csdDate5 = pCsdDate5;
//    }

    /**
     * Gibt ID des übertragenden Krankenhauses zurück.
     *
     * @return csdTransferringHospIdent
     */
    @Column(name = "TRANSFERRING_HOSP_IDENT", nullable = true, length = 25)
    public String getCsdTransferringHospIdent() {
        return csdTransferringHospIdent;
    }

    /**
     *
     * @param csdTransferringHospIdent Column TRANSFERRING_HOSP_IDENT:ID des
     * übertragenden Krankenhauses.
     */
    public void setCsdTransferringHospIdent(String csdTransferringHospIdent) {
        this.csdTransferringHospIdent = csdTransferringHospIdent;
    }

    /**
     * Gibt Datum des/eines Unfalles zurück.
     *
     * @return csdDateOfAccident
     */
    //@Temporal(TemporalType.DATE)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_OF_ACCIDENT", length = 7)
    public Date getCsdDateOfAccident() {
        return csdDateOfAccident == null ? null : new Date(csdDateOfAccident.getTime());
    }

    /**
     * Gibt Datum der Stornierung
     *
     * @return csdCancelDate
     */
    //@Temporal(TemporalType.DATE)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CANCEL_DATE", length = 7)
    public Date getCsdCancelDate() {
        return csdCancelDate == null ? null : new Date(csdCancelDate.getTime());
    }

    /**
     *
     * @param csdDateOfAccident Column DATE_OF_ACCIDENT :Datum des/eines
     * Unfalles.
     */
    public void setCsdDateOfAccident(Date csdDateOfAccident) {
        this.csdDateOfAccident = csdDateOfAccident == null ? null : new Date(csdDateOfAccident.getTime());
    }

    /**
     *
     * @param csdCancelDate Column CANCLE_DATE :Datum der Stornierung
     */
    public void setCsdCancelDate(Date csdCancelDate) {
        this.csdCancelDate = csdCancelDate == null ? null : new Date(csdCancelDate.getTime());
    }

    //////////////// Transiente Methoden
    /**
     * Ermittelt die entlassende Abteilung
     *
     * @return Entlassende Abteilung, null, wenn keine Abteilungen dem Fall
     * zugewiesen sind
     */
    @javax.persistence.Transient
    @JsonIgnore
    public TCaseDepartment getLastDepartment() {

        if (caseDepartments.isEmpty()) {
            return null;
        }

        final List<TCaseDepartment> sortedDepartments = getSortedDepartments();
        return sortedDepartments.get(sortedDepartments.size() - 1);
    }

    /**
     * Sortiert die Abteilungen des Falles nach Aufnahmezeit
     *
     * @return List of departments
     */
    @javax.persistence.Transient
    @JsonIgnore
    public List<TCaseDepartment> getSortedDepartments() {

        List<TCaseDepartment> caseDepartmentsSorted = new ArrayList<>();
        if (!caseDepartments.isEmpty()) {
            //Provocates NullPointerException if admission date is null!
            caseDepartmentsSorted.addAll(caseDepartments);
            Collections.sort(caseDepartmentsSorted,
                    (o1, o2) -> o1.getDepcAdmDate().compareTo(o2.getDepcAdmDate()));
        }

        return caseDepartmentsSorted;
    }

    /**
     * setzt die Flags für aufnehmende, behandelnde und entlassende Abteilung
     *
     */
    public void setDepartmentFlags() {
        final List<TCaseDepartment> sortedDepartments = getSortedDepartments();
        if (sortedDepartments.isEmpty()) {
            return;
        }
        
       


        TCaseDepartment depTreatm = sortedDepartments.get(0);
        long duration = depTreatm.getDurationLong();
        for (int i = 0; i < sortedDepartments.size(); i++) {
            TCaseDepartment dep = sortedDepartments.get(i);
            dep.setDepcIsTreatingFl(false);
            // aufnehmende Abteilung
            if(i ==  0){
                 dep.setDepcIsAdmissionFl(true);
            }else{
                dep.setDepcIsAdmissionFl(false);
            }
        // entlassende Fachabteilung
        if( i == sortedDepartments.size() - 1){
                dep.setDepcIsDischargeFl(true);
            }else{
                dep.setDepcIsDischargeFl(false);
            }
            if (dep.isPseudo() || dep.isHospital()) {
                continue;
            }
            if (dep.getDurationLong() > duration) {
                depTreatm = dep;
                duration = depTreatm.getDurationLong();
            } else if (dep.getDurationLong() == duration
                    && dep.getDepcAdmodEn().getId() <= AdmissionModeEn.HaBh.getId()
                    && depTreatm.getDepcAdmodEn().getId() > AdmissionModeEn.HaBh.getId()) {
                depTreatm = dep;
                duration = depTreatm.getDurationLong();
            }
        }
        depTreatm.setDepcIsTreatingFl(true);
        this.setCsdAdmodEn(depTreatm.getDepcAdmodEn());
    }

    /**
     * Clone this object
     *
     * @return cloned object
     * @throws CloneNotSupportedException cloning exception
     */
    @Override
    public Object clone() throws CloneNotSupportedException {

        TCaseDetails cloned = (TCaseDetails) super.clone();
// parent und Extern wird fuer clone auf null gesetzt
        cloned.setCaseDetailsByCsdExternId(null);
        cloned.setCaseDetailsByCsdParentId(null);
        Set<TCaseDepartment> clonedDeps = new HashSet<>(0);

        for (TCaseDepartment dep : this.getCaseDepartments()) {
            TCaseDepartment cloneDep = (TCaseDepartment) dep.clone();
            clonedDeps.add(cloneDep);
            cloneDep.setCaseDetails(cloned);
        }
        cloned.setCaseDepartments(clonedDeps);
// fees
        Set<TCaseFee> cloneFees = new HashSet<>(0);
        for (TCaseFee fee : this.getCaseFees()) {
            TCaseFee cloneFee = fee.clone();
            cloneFee.setCaseDetails(cloned);
            cloneFee.setCaseBill(null);
            cloneFees.add(fee);
        }
        cloned.setCaseFees(cloneFees);
// bills
        Set<TCaseBill> cloneBills = new HashSet<>(0);
        for (TCaseBill bill : this.getCaseBills()) {
            TCaseBill cloneBill = bill.clone();
            cloneBill.setCaseDetails(cloned);
        }
        cloned.setCaseBills(cloneBills);
        cloned.setGroupingResultses(new HashSet<>());
        cloned.setCaseDetailsesForCsdExternId(new HashSet<>());
        cloned.setCaseDetailsesForCsdParentId(new HashSet<>());
        return cloned;
    }

    @Override
        public TCaseDetails cloneWithoutIds(Long currentCpxUserId) throws CloneNotSupportedException {

        TCaseDetails cloned = (TCaseDetails) super.cloneWithoutIds(currentCpxUserId);
        cloned.setId(0);
        if (!getCsdIsLocalFl()) {
            cloned.setCaseDetailsByCsdExternId(this);
        } else {
            cloned.setCaseDetailsByCsdExternId(this.getCaseDetailsByCsdExternId());
        }
        cloned.setCaseDetailsByCsdParentId(this);
        Set<TCaseDepartment> clonedDeps = new HashSet<>(0);

        for (TCaseDepartment dep : this.getCaseDepartments()) {
            TCaseDepartment cloneDep = (TCaseDepartment) dep.cloneWithoutIds(currentCpxUserId);
            clonedDeps.add(cloneDep);
            cloneDep.setCaseDetails(cloned);
        }
        cloned.setCaseDepartments(clonedDeps);
        // fees
        Set<TCaseFee> cloneFees = new HashSet<>(0);
        for (TCaseFee fee : this.getCaseFees()) {
            TCaseFee cloneFee = (TCaseFee)fee.cloneWithoutIds(currentCpxUserId);
            cloneFee.setId(0);
            cloneFee.setCaseDetails(cloned);
            cloneFee.setCaseBill(null);
            cloneFees.add(cloneFee);
        }
        cloned.setCaseFees(cloneFees);
        // bills
        Set<TCaseBill> cloneBills = new HashSet<>(0);
        for (TCaseBill bill : this.getCaseBills()) {
            TCaseBill cloneBill = (TCaseBill)bill.cloneWithoutIds(currentCpxUserId);
            cloneBill.setId(0);
            cloneBill.setCaseDetails(cloned);
        }
        cloned.setCaseBills(cloneBills);

        cloned.setGroupingResultses(new HashSet<>());
        cloned.setCaseDetailsesForCsdExternId(new HashSet<>());
        cloned.setCaseDetailsesForCsdParentId(new HashSet<>());
        //Awi-01112017:
        //Reset sum of opses and icds - in these entities are prePersist-Methodes
        //on saving valus would be added, and wrong number whould be stored
        cloned.setSumOfOps(0);
        cloned.setSumOfIcd(0);

        return cloned;
    }

    @javax.persistence.Transient
    @JsonIgnore
    public TCaseIcd getPrincipalCaseIcd() {
        TCaseIcd icd = null;
        for (TCaseDepartment dep : caseDepartments) {
            if ((icd = dep.getPrincipalCaseIcd()) != null) {
                return icd;
            }
        }
        return icd;
    }

    @Override
    public boolean versionEquals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TCaseDetails)) {
            return false;
        }
        final TCaseDetails other = (TCaseDetails) obj;
//        if (!isStringEquals(this.csdInsCompany, other.getCsdInsCompany())) {
//            return false;
//        }
        if (!Objects.equals(this.csdAdmissionDate, other.getCsdAdmissionDate())) {
            return false;
        }
        if (!Objects.equals(this.csdDischargeDate, other.getCsdDischargeDate())) {
            return false;
        }
        if (!Objects.equals(this.csdAdmissionWeight, other.getCsdAdmissionWeight())) {
            return false;
        }
        if (!Objects.equals(this.csdAgeYears, other.getCsdAgeYears())) {
            return false;
        }
        if (!Objects.equals(this.csdAgeDays, other.getCsdAgeDays())) {
            return false;
        }
        if (!Objects.equals(this.csdHmv, other.getCsdHmv())) {
            return false;
        }
        if (!Objects.equals(this.csdLeave, other.getCsdLeave())) {
            return false;
        }
        if (!Objects.equals(this.csdLosAlteration, other.getCsdLosAlteration())) {
            return false;
        }
        if (this.csdAdmReason12En != other.getCsdAdmReason12En()) {
            return false;
        }
        if (this.csdAdmCauseEn != other.getCsdAdmCauseEn()) {
            return false;
        }
        if (this.csdDisReason12En != other.getCsdDisReason12En()) {
            return false;
        }
        if (this.csdAdmodEn != other.getCsdAdmodEn()) {
            return false;
        }
        if (this.csdAdmLawEn != other.getCsdAdmLawEn()) {
            return false;
        }
        if (this.csdAdmReason34En != other.getCsdAdmReason34En()) {
            return false;
        }
        if (this.csdDisReason3En != other.getCsdDisReason3En()) {
            return false;
        }
        if (this.csdGenderEn != other.getCsdGenderEn()) {
            return false;
        }
        /*        if (!Objects.equals(this.comment, other.getComment())) { // AGE: I think, can be ignored when  checked for  differences
            return false;
        }*/
        if (!ModelUtil.versionSetEquals(this.caseDepartments, other.getCaseDepartments())) {
            return false;
        }
        /*        if (!ModelUtil.versionSetEquals(this.caseFees, other.getCaseFees())) { // AGE fees will be reached through bills
            return false;
        }*/
        if (!ModelUtil.versionSetEquals(this.caseBills, other.getCaseBills())) {
            return false;
        }

        return true;
    }

    /**
     * @return the sumOfIcd
     */
    @Column(name = "SUM_OF_ICD")
    public Integer getSumOfIcd() {
        return sumOfIcd;
    }

    /**
     * @param sumOfIcd the SUM_SD to set
     */
    public void setSumOfIcd(Integer sumOfIcd) {
        this.sumOfIcd = sumOfIcd;
    }

    /**
     * @return the sumOfOps
     */
    @Column(name = "SUM_OF_OPS")
    public Integer getSumOfOps() {
        return sumOfOps;
    }

    /**
     * @param sumOfOps the sumOfOps to set
     */
    public void setSumOfOps(Integer sumOfOps) {
        this.sumOfOps = sumOfOps;
    }

    /**
     * @return the HD_ICD_CODE
     */
    @Column(name = "HD_ICD_CODE", length = 10, nullable = true)
    public String getHdIcdCode() {
        return HdIcdCode;
    }

    /**
     * @param HdIcdCode the HD_ICD_CODE to set
     */
    public void setHdIcdCode(String HdIcdCode) {
        this.HdIcdCode = HdIcdCode;
    }

    /**
     * Warning: On Clientside Methode may throw lazyLoadingException due to sets
     * of Departments and icds are fetched as lazy will only work on server side
     * or when entity is loaded through special entitygraph!
     *
     * @return MainDiagnosis of that case details
     */
    @Transient
    @JsonIgnore
    public TCaseIcd getMainDiagnosis() {
        for (TCaseDepartment dep : getCaseDepartments()) {
            for (TCaseIcd icd : dep.getCaseIcds()) {
                if (icd.getIcdcIsHdxFl()) {
                    return icd;
                }
            }
        }
        return null;
    }

    @Transient
    @JsonIgnore
    public List<TCaseIcd> getAllIcds() {
        List<TCaseIcd> icds = new ArrayList<>();
        for (TCaseDepartment dep : getCaseDepartments()) {
            icds.addAll(dep.getCaseIcds());
        }
        return icds;
    }

    @Transient
    @JsonIgnore
    public List<TCaseOps> getAllOpses() {
        List<TCaseOps> opses = new ArrayList<>();
        for (TCaseDepartment dep : getCaseDepartments()) {
            opses.addAll(dep.getCaseOpses());
        }
        return opses;
    }

    public TGroupingResults getGroupingResult2Model(GDRGModel model, boolean isAuto) {
        Set<TGroupingResults> allRes = this.getGroupingResultses();
        if (allRes == null) {
            return null;
        }
        for (TGroupingResults grpRes : allRes) {
            if (isAuto && grpRes.getGrpresIsAutoFl() 
                    || !isAuto && ! grpRes.getGrpresIsAutoFl()  && grpRes.getModelIdEn().equals(model)) {
                if(grpRes.getCaseIcd() != null && grpRes.getCaseIcd().getIcdcIsHdxFl() )
                    return grpRes;
            }
        }
        return null;
    }


    /**
     * @return the reduction days which are not concidered by calculation of care days
     */
    @Column(name = "CSD_LOS_MD_ALTERATION")
    public Integer getCsdLosMdAlteration() {
        return csdLosMdAlteration;
    }

    public void setCsdLosMdAlteration(Integer csdLosMdAlteration) {
        this.csdLosMdAlteration = csdLosMdAlteration;
    }

    @Column(name = "CSD_VERS_RISK_TYPE_EN", length = 20)
    @Enumerated(EnumType.STRING)
    public VersionRiskTypeEn getCsdVersRiskTypeEn() {
        return csdVersRiskTypeEn;
    }

    public void setCsdVersRiskTypeEn(VersionRiskTypeEn csdVersRiskTypeEn) {
        this.csdVersRiskTypeEn = csdVersRiskTypeEn;
    }
//
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseDetails")
//    @OnDelete(action = OnDeleteAction.NO_ACTION)
//    public Set<TWmRisk> getWmRisks() {
//        return wmRisks;
//    }
//
//    public void setWmRisks(Set<TWmRisk> wmRisks) {
//        this.wmRisks = wmRisks;
//    }
//

}
