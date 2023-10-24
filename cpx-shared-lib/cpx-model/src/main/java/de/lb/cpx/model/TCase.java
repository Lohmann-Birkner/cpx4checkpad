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

//import de.lb.cpx.model.converter.CsCaseTypeConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import de.lb.cpx.model.converter.FeeGroupConverter;
import de.lb.cpx.model.enums.CaseStatusEn;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.FeeGroupEn;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import static javax.persistence.TemporalType.DATE;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;
import de.lb.cpx.model.enums.AdmissionReasonEn;
import de.lb.cpx.server.commons.dao.AbstractCaseEntity;

/**
 * TCase initially generated at 21.01.2016 17:07:59 by Hibernate Tools 3.2.2.GA
 * <p style="font-size:1em; color:green;"> Die Tabelle "T_CASE" speichert alle
 * Krankenhausfälle, die in die Datenbank eingelesen wurden. </p>
 */
@Entity
@Table(name = "T_CASE", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"CS_CASE_NUMBER", "CS_HOSPITAL_IDENT"}, name = "Uni_CaseNumber_Hospitalident")
}, indexes = {
    @Index(name = "IDX_CASE4PATIENT_ID", columnList = "T_PATIENT_ID", unique = false),
    @Index(name = "IDX_CASE4INSURANCE_IDENTIFIER", columnList = "INSURANCE_IDENTIFIER", unique = false)})
@SuppressWarnings("serial")
//@NamedEntityGraph(
//        name = "fetchBatchCaseGraph",
//        attributeNodes = {
//            @NamedAttributeNode(value = "caseDetails", subgraph = "detailsGraph"),
////            @NamedAttributeNode(value = "caseOpsGroupeds", subgraph = "opsGraph"),
////            @NamedAttributeNode(value = "checkResults"),
////            @NamedAttributeNode(value = "caseIcd")
//        },
//        subgraphs = {
//            @NamedSubgraph(name = "detailsGraph",attributeNodes = {
//                        @NamedAttributeNode(value = "caseDepartments"
////            , subgraph = "opsIcdGraph")
////                    }),
////            @NamedSubgraph(name = "opsIcdGraph",attributeNodes = {
////                        @NamedAttributeNode("caseOpses"),
////                        @NamedAttributeNode("caseIcds"
//                    
//                        )})
//        }
//    )
public class TCase extends AbstractCaseEntity {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(TCase.class.getName());

//    private long id;
    private TPatient patient;
    private String csHospitalIdent;
    private String csDoctorIdent;
    private String csCaseNumber;
    private String insuranceIdentifier;
    private String insuranceNumberPatient;
    private CaseTypeEn csCaseTypeEn = CaseTypeEn.DRG;
    private CaseStatusEn csStatusEn = CaseStatusEn.NEW;
    private boolean csKisStatusFl = false;
    private boolean csCancellationReasonEn; // TODO Welcher Enum?
    private FeeGroupEn csFeeGroupEn; // zu überprüfen, ob Entgeltbereich nicht in TCaseDetails gehört: Kann fall ein Entgeltbereich wechseln?
    private TCaseDetails currentLocal;
    private TCaseDetails currentExtern;
    private Date csBillingDate;//CS_BILLING_DATE   DATE  Null

    private Set<TCaseDetails> caseDetails = new HashSet<>(0);
    private Set<TCaseMergeMapping> caseMergeMappingsForMergeMemberCaseId = new HashSet<>(0);
    private Set<TCaseMergeMapping> caseMergeMappingsForHoscId = new HashSet<>(0);
    private Set<TCaseComment> caseComments = new HashSet<>(0);
    private Set<TLab> caseLabor = new HashSet<>(0);
    // Customer extension fields
    private String string1;
    private String string2;
    private String string3;
    private String string4;
    private String string5;
    private String string6;
    private String string7;
    private String string8;
    private String string9;
    private String string10;

    private Integer numeric1;
    private Integer numeric2;
    private Integer numeric3;
    private Integer numeric4;
    private Integer numeric5;
    private Integer numeric6;
    private Integer numeric7;
    private Integer numeric8;
    private Integer numeric9;
    private Integer numeric10;

    public TCase() {
        setIgnoreFields();
    }
    
    public TCase(Long pCurrentUser){
        super(pCurrentUser);
        setIgnoreFields();
    }
    

    @Override
    protected final void setIgnoreFields() {
        ignoredFields = new String[]{"patient",
            "csStatusEn",
            "caseDetails",
            "csKisStatusFl",
            "csCancellationReasonEn",
            "caseMergeMappingsForMergeMemberCaseId",
            "caseMergeMappingsForHoscId",
            "currentLocal", "currentExtern"
        };
    }
   

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "hospitalCase", orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference(value = "details")
    public Set<TCaseDetails> getCaseDetails() {
        return this.caseDetails;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "hospitalCase")
    @JsonManagedReference(value = "labor")
    public Set<TLab> getCaseLabor() {
        return this.caseLabor;
    }

    @Transient
    @JsonIgnore
    public TCaseDetails getCurrentLocal() {
        Iterator<TCaseDetails> it = this.getCaseDetails().iterator();
        while (it.hasNext()) {
            TCaseDetails csd = it.next();
            if (csd == null) {
                continue;
            }
            if (!csd.getCsdIsActualFl()) {
                continue;
            }
            if (csd.getCsdIsLocalFl()) {
                return csd;
            }
        }
        return null;
    }

    @Transient
    @JsonIgnore
    public TCaseDetails getCurrentExtern() {
        Iterator<TCaseDetails> it = this.caseDetails.iterator();
        while (it.hasNext()) {
            TCaseDetails csd = it.next();
            if (csd == null) {
                continue;
            }
            if (!csd.getCsdIsActualFl()) {
                continue;
            }
            if (!csd.getCsdIsLocalFl()) {
                return csd;
            }
        }
        return null;
    }

    @Transient
    @JsonIgnore
    public Set<TCaseDetails> getLocals() {
        Set<TCaseDetails> locals = new HashSet<>(0);
        Iterator<TCaseDetails> it = this.caseDetails.iterator();
        while (it.hasNext()) {
            TCaseDetails csd = it.next();
            if (csd == null) {
                continue;
            }
            if (csd.getCsdIsLocalFl()) {
                locals.add(csd);
            }
        }
        return locals;
    }

    @Transient
    @JsonIgnore
    public Set<TCaseDetails> getExterns() {
        Set<TCaseDetails> externs = new HashSet<>(0);
        Iterator<TCaseDetails> it = this.caseDetails.iterator();
        while (it.hasNext()) {
            TCaseDetails csd = it.next();
            if (csd == null) {
                continue;
            }
            if (!csd.getCsdIsLocalFl()) {
                externs.add(csd);
            }
        }
        return externs;
    }

    /**
     * @param pCaseDetails case details local
     */
    public void setCurrentLocal(final TCaseDetails pCaseDetails) {
        if (pCaseDetails == null) {
            LOG.log(Level.SEVERE, "Passed TCaseDetails object is null!");
            return;
        }
        pCaseDetails.setCsdIsLocalFl(true);
        pCaseDetails.setCsdIsActualFl(true);
        addCaseDetails(pCaseDetails);
    }

    /**
     * @param pCaseDetails case details extern
     */
    public void setCurrentExtern(final TCaseDetails pCaseDetails) {
        if (pCaseDetails == null) {
            LOG.log(Level.SEVERE, "Passed TCaseDetails object is null!");
            return;
        }
        pCaseDetails.setCsdIsLocalFl(false);
        pCaseDetails.setCsdIsActualFl(true);
        addCaseDetails(pCaseDetails);
    }

    /**
     * @return local copy of current extern WARNING: is not reflected back to
     * the set of case details support methode for batch grouping
     */
    @Transient
    @JsonIgnore
    public TCaseDetails getLocalCopyOfCurrentExtern() {
        return currentExtern;
    }

    /**
     * @return local copy of local extern WARNING: is not reflected back to the
     * set of case details support methode for batch grouping
     */
    @Transient
    @JsonIgnore
    public TCaseDetails getLocalCopyOfCurrentLocal() {
        return currentLocal;
    }

    /**
     * @param pDetails set details als local copy of current extern WARNING: is
     * not connected to set of case details support methode of batch grouping
     */
    public void setLocalCopyOfCurrentExtern(TCaseDetails pDetails) {
        currentExtern = pDetails;
    }

    /**
     * @param pDetails set details als local copy of current local WARNING: is
     * not connected to set of case details support methode of batch grouping
     */
    public void setLocalCopyOfCurrentLocal(TCaseDetails pDetails) {
        currentLocal = pDetails;
    }

    public void addCaseDetails(final TCaseDetails pCaseDetails) {
        if (pCaseDetails == null) {
            return;
        }
        pCaseDetails.setHospitalCase(this);
        this.caseDetails.remove(pCaseDetails);
        if (pCaseDetails.getCsdIsActualFl()) {
            TCaseDetails csd;
            if (pCaseDetails.getCsdIsLocalFl()) {
                csd = getCurrentLocal();
            } else {
                csd = getCurrentExtern();
            }
            if (csd != null && csd != pCaseDetails) {
                csd.setCsdIsActualFl(false);
            }
        }
        this.caseDetails.add(pCaseDetails);
    }

//    public boolean removeCaseDetails(final TCaseDetails pCaseDetails) {
//      if (pCaseDetails == null) {
//        return false;
//      }
//      boolean ret = false;
//      Iterator<TCaseDetails> it = getCaseDetails().iterator();
//      while(it.hasNext()) {
//        TCaseDetails csd = it.next();
//        if (csd == null) {
//          continue;
//        }
//        if (csd.getId() == pCaseDetails.getId()) {
//          it.remove();
//          csd.setHospitalCase(null); //does not really work, case cannot be null in case details
//          pCaseDetails.setHospitalCase(null); //does not really work, case cannot be null in case details
//          ret = true;
//          break;
//        }
//      }
//      return ret;
//    }
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseByHoscId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TCaseMergeMapping> getCaseMergeMappingsForHoscId() {
        return this.caseMergeMappingsForHoscId;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseByMergeMemberCaseId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TCaseMergeMapping> getCaseMergeMappingsForMergeMemberCaseId() {
        return this.caseMergeMappingsForMergeMemberCaseId;
    }

    /**
     * Gibt die Storno - Ursache definiert, 0 - Aktiv zurück .
     *
     * @return csCancellationReasonEn
     */
    @Column(name = "CANCEL_FL", precision = 1, scale = 0, nullable = false)
    public boolean getCsCancellationReasonEn() {
        return this.csCancellationReasonEn;
    }

    /**
     * Gibt Fallnummer in Bezug auf das Krankenhaus (Krankenhausinterne
     * Fallnummer) zurück. if casenumber ends with _c than the case is indicated
     * as canceled
     *
     * @return csCaseNumber
     */
    @Column(name = "CS_CASE_NUMBER", length = 25, nullable = false)
    public String getCsCaseNumber() {
        return this.csCaseNumber;
    }

    /**
     * Gibt Art des Falles(DRG ,PEPP ,PIA,PSY:AmbuOP,vorstatAbbrecher,OTHER)
     * zurück.
     *
     * @return csCaseTypeEn
     */
    @Column(name = "CS_CASE_TYPE_EN", length = 25, nullable = false)
    @Enumerated(EnumType.STRING)
    public CaseTypeEn getCsCaseTypeEn() {
        return this.csCaseTypeEn;
    }

    /**
     * Gibt Identifikationsnummer des einweisenden Arztes (Doktors) zurück.
     * weitere Informationen in der Tabelle CPX_common.C_DOCTOR(DOC_IDENT)
     *
     * @return CS_DOCTOR_IDENT
     */
    @Column(name = "CS_DOCTOR_IDENT", length = 10)
    public String getCsDoctorIdent() {
        return this.csDoctorIdent;
    }

    /**
     * Gibt Entgeltebereich zurück. z.b. DRG, DakIntEnt,VorStat,...
     *
     * @return csFeeGroupEn
     */
    @Column(name = "CS_FEE_GROUP_EN", length = 2)
    @Convert(converter = FeeGroupConverter.class)
    public FeeGroupEn getCsFeeGroupEn() {
        return this.csFeeGroupEn;
    }

    /**
     * Gibt Identifikationsnumme des Krankenhauses, das den Fall bearbeitet
     * zurück.
     *
     * @return csHospitalIdent Weitere Informationen in der Tabelle
     * CPX_common.C_HOSPITAL (HOS_IDENT)
     */
    @Column(name = "CS_HOSPITAL_IDENT", length = 10, nullable = false)
    public String getCsHospitalIdent() {
        return this.csHospitalIdent;
    }

    /**
     * Gibt Kennung zurück, ob der KIS_STATUS gesetzt ist, 0=false, 1=true.
     *
     * @return csKisStatusFl
     */
    @Column(name = "CS_KIS_STATUS_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean getCsKisStatusFl() {
        return this.csKisStatusFl;
    }

    /**
     * Gibt Status der Bearbeitung des Falles zurück.
     * z.b.DEFAULT,PROCESSED,NEW_VERS,NEW,SUGG,CLOSED,SAP_CLOSED.
     *
     * @return csStatusEn
     */
    @Column(name = "CS_STATUS_EN", nullable = false, length = 25)
    @Enumerated(EnumType.STRING)
    public CaseStatusEn getCsStatusEn() {
        return this.csStatusEn;
    }

    /**
     * 10 String Spalten für Kundenerweiterungen
     */
    /**
     *
     * @return string1
     */
    @Column(name = "STRING_01", nullable = true, length = 100)
    public String getString1() {
        return this.string1;
    }

    /**
     *
     * @return string2
     */
    @Column(name = "STRING_02", nullable = true, length = 100)
    public String getString2() {
        return this.string2;
    }

    /**
     *
     * @return string3
     */
    @Column(name = "STRING_03", nullable = true, length = 100)
    public String getString3() {
        return this.string3;
    }

    /**
     *
     * @return string4
     */
    @Column(name = "STRING_04", nullable = true, length = 100)
    public String getString4() {
        return this.string4;
    }

    /**
     *
     * @return string5
     */
    @Column(name = "STRING_05", nullable = true, length = 100)
    public String getString5() {
        return this.string5;
    }

    /**
     *
     * @return string6
     */
    // CPX-2675: this field contains the list of numbers of cases that ware merged. In the error case there ware 14 cases, so the string length was > 100; we change it to 500
    @Column(name = "STRING_06", nullable = true, length = 500)
    public String getString6() {
        return this.string6;
    }

    /**
     *
     * @return string7
     */
    @Column(name = "STRING_07", nullable = true, length = 100)
    public String getString7() {
        return this.string7;
    }

    /**
     *
     * @return string8
     */
    @Column(name = "STRING_08", nullable = true, length = 100)
    public String getString8() {
        return this.string8;
    }

    /**
     *
     * @return string9
     */
    @Column(name = "STRING_09", nullable = true, length = 100)
    public String getString9() {
        return this.string9;
    }

    /**
     *
     * @return string10
     */
    @Column(name = "STRING_10", nullable = true, length = 100)
    public String getString10() {
        return this.string10;
    }

    /**
     *
     * 10 Int Spalten für Kundenerweiterungen
     */
    /**
     *
     * @return numeric1 Numeric1
     */
    @Column(name = "NUMERIC_01", precision = 10, scale = 0, nullable = true)
    public Integer getNumeric1() {
        return this.numeric1;
    }

    /**
     *
     * @return numeric2 Numeric2
     */
    @Column(name = "NUMERIC_02", precision = 10, scale = 0, nullable = true)
    public Integer getNumeric2() {
        return this.numeric2;
    }

    /**
     *
     * @return numeric3 Numeric3
     */
    @Column(name = "NUMERIC_03", precision = 10, scale = 0, nullable = true)
    public Integer getNumeric3() {
        return this.numeric3;
    }

    /**
     *
     * @return numeric4 Numeric4
     */
    @Column(name = "NUMERIC_04", precision = 10, scale = 0, nullable = true)
    public Integer getNumeric4() {
        return this.numeric4;
    }

    /**
     *
     * @return numeric5 Numeric5
     */
    @Column(name = "NUMERIC_05", precision = 10, scale = 0, nullable = true)
    public Integer getNumeric5() {
        return this.numeric5;
    }

    /**
     *
     * @return numeric6 Numeric6
     */
    @Column(name = "NUMERIC_06", precision = 10, scale = 0, nullable = true)
    public Integer getNumeric6() {
        return this.numeric6;
    }

    /**
     *
     * @return numeric7 Numeric7
     */
    @Column(name = "NUMERIC_07", precision = 10, scale = 0, nullable = true)
    public Integer getNumeric7() {
        return this.numeric7;
    }

    /**
     *
     * @return numeric8 Numeric8
     */
    @Column(name = "NUMERIC_08", precision = 10, scale = 0, nullable = true)
    public Integer getNumeric8() {
        return this.numeric8;
    }

    /**
     *
     * @return numeric9 Numeric9
     */
    @Column(name = "NUMERIC_09", precision = 10, scale = 0, nullable = true)
    public Integer getNumeric9() {
        return this.numeric9;
    }

    /**
     *
     * @return numeric10 Numeric10
     */
    @Column(name = "NUMERIC_10", precision = 10, scale = 0, nullable = true)
    public Integer getNumeric10() {
        return this.numeric10;
    }

    /*
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "CS_EXTERN_ACTUAL_ID", nullable = true)
    public TCaseDetails getCurrentExtern() {
        return currentExtern;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "CS_LOCAL_ACTUAL_ID", nullable = true)
    public TCaseDetails getCurrentLocal() {
        return currentLocal;
    }
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_CASE_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    /**
     * Gibt Verweis auf die ID der Tabelle T_Patient zurück.
     *
     * @return patient
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "T_PATIENT_ID", foreignKey = @ForeignKey(name = "FK_T_CASE4T_PATIENT_ID"), nullable = false)
    public TPatient getPatient() {
        return this.patient;
    }

    public void setCaseDetails(final Set<TCaseDetails> caseDetails) {
        this.caseDetails = caseDetails;
    }

    public void setCaseLabor(final Set<TLab> caseLabor) {
        this.caseLabor = caseLabor;
    }

    public void setCaseMergeMappingsForHoscId(
            final Set<TCaseMergeMapping> caseMergeMappingsForHoscId) {
        this.caseMergeMappingsForHoscId = caseMergeMappingsForHoscId;
    }

    public void setCaseMergeMappingsForMergeMemberCaseId(
            final Set<TCaseMergeMapping> caseMergeMappingsForMergeMemberCaseId) {
        this.caseMergeMappingsForMergeMemberCaseId = caseMergeMappingsForMergeMemberCaseId;
    }

    /**
     *
     * @param csCancellationReasonEn Column CANCEL_FL :Member der Enumeration,
     * die Storno - Ursache definiert.
     */
    public void setCsCancellationReasonEn(final boolean csCancellationReasonEn) {
        this.csCancellationReasonEn = csCancellationReasonEn;
    }

    /**
     *
     * @param csCaseNumber Column CS_CASE_NUMBER :Krankenhausinterne Fallnummer.
     */
    public void setCsCaseNumber(final String csCaseNumber) {
        this.csCaseNumber = csCaseNumber;
    }

    /**
     *
     * @param csCaseTypeEn Column CS_CASE_TYPE_EN:Art des Falles(DRG ,PEPP
     * ,PIA,PSY:AmbuOP,vorstatAbbrecher,OTHER).
     */
    public void setCsCaseTypeEn(final CaseTypeEn csCaseTypeEn) {
        this.csCaseTypeEn = csCaseTypeEn;
    }

    /**
     *
     * @param csDoctorIdent Column CS_DOCTOR_IDENT:Identifikationsnummer des
     * einweisenden Arztes (Doktors).
     */
    public void setCsDoctorIdent(final String csDoctorIdent) {
        this.csDoctorIdent = csDoctorIdent;
    }

    /**
     *
     * @param csFeeGroupEn Column CS_FEE_GROUP_EN :Entgeltebereich z.b. DRG,
     * DakIntEnt,VorStat,.. .
     */
    public void setCsFeeGroupEn(final FeeGroupEn csFeeGroupEn) {
        this.csFeeGroupEn = csFeeGroupEn;
    }

    /**
     *
     * @param csHospitalIdent Column CS_HOSPITAL_IDENT:Identifikationsnumme des
     * Krankenhauses, das den Fall bearbeitet.
     */
    public void setCsHospitalIdent(final String csHospitalIdent) {
        this.csHospitalIdent = csHospitalIdent;
    }

    /**
     *
     * @param csKisStatusFl Column CS_KIS_STATUS_FL:Kennung, ob der KIS_STATUS
     * gesetzt ist, 0=false, 1=true.
     */
    public void setCsKisStatusFl(final boolean csKisStatusFl) {
        this.csKisStatusFl = csKisStatusFl;
    }

    /**
     *
     * @param csStatusEn Column CS_STATUS_EN :Enumeration für die Status der
     * Bearbeitung des Falles (DEFAULT,PROCESSED,NEW_VERS ,
     * NEW,SUGG,CLOSED,SAP_CLOSED).
     */
    public void setCsStatusEn(final CaseStatusEn csStatusEn) {
        this.csStatusEn = csStatusEn;
    }

    /*
    public void setCurrentExtern(final TCaseDetails currentExtern) {
        this.currentExtern = currentExtern;
    }

    public void setCurrentLocal(final TCaseDetails currentLocal) {
        this.currentLocal = currentLocal;
    }
     */
    public void setId(final long id) {
        this.id = id;
    }

    /**
     *
     * @param patient :Verweis auf die ID der Tabelle T_Patient.
     */
    public void setPatient(final TPatient patient) {
        this.patient = patient;
    }

    /**
     *
     * @param string1 String1 für Kundenerweiterungen
     */
    public void setString1(String string1) {
        this.string1 = string1;
    }

    /**
     *
     * @param string2 String2 für Kundenerweiterungen
     */
    public void setString2(String string2) {
        this.string2 = string2;
    }

    /**
     *
     * @param string3 String3 für Kundenerweiterungen
     */
    public void setString3(String string3) {
        this.string3 = string3;
    }

    /**
     *
     * @param string4 String4 für Kundenerweiterungen
     */
    public void setString4(String string4) {
        this.string4 = string4;
    }

    /**
     *
     * @param string5 String5 für Kundenerweiterungen
     */
    public void setString5(String string5) {
        this.string5 = string5;
    }

    /**
     *
     * @param string6 String6 für Kundenerweiterungen
     */
    public void setString6(String string6) {
        this.string6 = string6;
    }

    /**
     *
     * @param string7 String7 für Kundenerweiterungen
     */
    public void setString7(String string7) {
        this.string7 = string7;
    }

    /**
     *
     * @param string8 String8 für Kundenerweiterungen
     */
    public void setString8(String string8) {
        this.string8 = string8;
    }

    /**
     *
     * @param string9 String9 für Kundenerweiterungen
     */
    public void setString9(String string9) {
        this.string9 = string9;
    }

    /**
     *
     * @param string10 String10 für Kundenerweiterungen
     */
    public void setString10(String string10) {
        this.string10 = string10;
    }

    /**
     *
     * @param numeric1 numeric1 für Kundenerweiterungen
     */
    public void setNumeric1(Integer numeric1) {
        this.numeric1 = numeric1;
    }

    /**
     *
     * @param numeric2 Numeric2 für Kundenerweiterungen
     */
    public void setNumeric2(Integer numeric2) {
        this.numeric2 = numeric2;
    }

    /**
     *
     * @param numeric3 Numeric3 für Kundenerweiterungen
     */
    public void setNumeric3(Integer numeric3) {
        this.numeric3 = numeric3;
    }

    /**
     *
     * @param numeric4 Numeric4 für Kundenerweiterungen
     */
    public void setNumeric4(Integer numeric4) {
        this.numeric4 = numeric4;
    }

    /**
     *
     * @param numeric5 Numeric5 für Kundenerweiterungen
     */
    public void setNumeric5(Integer numeric5) {
        this.numeric5 = numeric5;
    }

    /**
     *
     * @param numeric6 Numeric6 für Kundenerweiterungen
     */
    public void setNumeric6(Integer numeric6) {
        this.numeric6 = numeric6;
    }

    /**
     *
     * @param numeric7 Numeric7 für Kundenerweiterungen
     */
    public void setNumeric7(Integer numeric7) {
        this.numeric7 = numeric7;
    }

    /**
     *
     * @param numeric8 Numeric8 für Kundenerweiterungen
     */
    public void setNumeric8(Integer numeric8) {
        this.numeric8 = numeric8;
    }

    /**
     *
     * @param numeric9 Numeric9 für Kundenerweiterungen
     */
    public void setNumeric9(Integer numeric9) {
        this.numeric9 = numeric9;
    }

    /**
     *
     * @param numeric10 Numeric10 für Kundenerweiterungen
     */
    public void setNumeric10(Integer numeric10) {
        this.numeric10 = numeric10;
    }

    /**
     * Gibt Identifikator (IKZ) der Versicherung für diesen Fall zurück
     *
     * @return csInsCompany
     */
    @Column(name = "INSURANCE_IDENTIFIER ", nullable = true, length = 20)
    public String getInsuranceIdentifier() {
        return this.insuranceIdentifier;
    }

    /**
     *
     * @param insuranceIdentifier Column CS_INS_COMPANY: Nummer der
     * Krankenkasse, die in der Tabelle CPX_COMMON.C_INSURANCE_COMPANY steht.
     */
    public void setInsuranceIdentifier(final String insuranceIdentifier) {
        this.insuranceIdentifier = insuranceIdentifier;
    }

    /**
     * Gibt Versicherungsnummer des Patienten bei der Versicherung zurück.
     *
     * @return INSURANCE_NUMBER_PATIENT
     */
    @Column(name = "INSURANCE_NUMBER_PATIENT", length = 20)
    public String getInsuranceNumberPatient() {
        return this.insuranceNumberPatient;
    }

    /**
     * Set the insurance number
     *
     * @param insuranceNumberPatient Column CS_INS_NUMBER: Versichertennummer.
     */
    public void setInsuranceNumberPatient(final String insuranceNumberPatient) {
        this.insuranceNumberPatient = insuranceNumberPatient;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "TCaseId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TCaseComment> getCaseComments() {
        return this.caseComments;
    }

    public void setCaseComments(final Set<TCaseComment> pCaseComments) {
        this.caseComments = pCaseComments;
    }

    /**
     * Gibt Rechnungsdatum (SAP) zurück
     *
     * @return csBillingDate
     */
    //@Temporal(TemporalType.DATE)
    @Temporal(DATE)
    @Column(name = "CS_BILLING_DATE", nullable = true)
    public Date getCsBillingDate() {
        return this.csBillingDate;
    }

    @Transient
    public Integer getCsBillingYear() {
        Date dt = csBillingDate;
        if (dt == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return cal.get(Calendar.YEAR);
    }

    @Transient
    public Integer getCsBillingQuarter() {
        Date dt = csBillingDate;
        if (dt == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return (cal.get(Calendar.MONTH) / 3) + 1;
    }

    /**
     *
     * @param csBillingDate Column CS_BILLING_DATE: Datum der Rechnung
     */
    public void setCsBillingDate(final Date csBillingDate) {
        this.csBillingDate = csBillingDate == null ? null : new Date(csBillingDate.getTime());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        TCase clone = (TCase) super.cloneWithoutIds(null);
        clone.setId(0);
        Set<TCaseDetails> clonedDet = new HashSet<>();
//        for(TCaseDetails det : getCaseDetails()){
//            clonedDet.add(det.cloneWithoutIds());
//        }
        clonedDet.add(getCurrentExtern().cloneWithoutIds(null));
        clonedDet.add(getCurrentLocal().cloneWithoutIds(null));
        for (TCaseDetails det : clonedDet) {
            det.setHospitalCase(clone);
            if (det.getCsdIsLocalFl()) {
                det.setCaseDetailsByCsdParentId(clone.getCurrentExtern());
            } else {
                det.setCaseDetailsByCsdParentId(null);
            }
        }
        clone.setCaseDetails(clonedDet);
        clone.setCaseMergeMappingsForHoscId(new HashSet<>());
        clone.setCaseMergeMappingsForMergeMemberCaseId(new HashSet<>());
        clone.setCaseComments(new HashSet<>());
        clone.setCaseLabor(new HashSet<>()); //fixes this error: Found shared references to a collection
        Hibernate.initialize(clone.getPatient());
//        getPatient().getCases().add(clone);
        return clone;
    }

    @Override
    public Object cloneWithoutIds(Long currentUserId) throws CloneNotSupportedException {
        TCase clone = (TCase) super.cloneWithoutIds(currentUserId);
        clone.setId(0);
        Set<TCaseDetails> clonedDet = new HashSet<>();
//        for(TCaseDetails det : getCaseDetails()){
//            clonedDet.add(det.cloneWithoutIds());
//        }
        clonedDet.add(getCurrentExtern().cloneWithoutIds(currentUserId));
        clonedDet.add(getCurrentLocal().cloneWithoutIds(currentUserId));
        for (TCaseDetails det : clonedDet) {
            det.setHospitalCase(clone);
            if (det.getCsdIsLocalFl()) {
                det.setCaseDetailsByCsdParentId(clone.getCurrentExtern());
            } else {
                det.setCaseDetailsByCsdParentId(null);
            }
        }
        clone.setCaseDetails(clonedDet);
        clone.setCaseMergeMappingsForHoscId(new HashSet<>());
        clone.setCaseMergeMappingsForMergeMemberCaseId(new HashSet<>());
        clone.setCaseComments(new HashSet<>());
        clone.setCaseLabor(new HashSet<>()); //fixes this error: Found shared references to a collection
        Hibernate.initialize(clone.getPatient());
//        getPatient().getCases().add(clone);
        return clone;
    }

    @Override
    public boolean versionEquals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TCase)) {
            return false;
        }
        final TCase other = (TCase) obj;
        if (!Objects.equals(this.csHospitalIdent, other.csHospitalIdent)) {
            return false;
        }
        if (!Objects.equals(this.csDoctorIdent, other.csDoctorIdent)) {
            return false;
        }
        if (!Objects.equals(this.csCaseNumber, other.csCaseNumber)) {
            return false;
        }
        if (!Objects.equals(this.insuranceIdentifier, other.insuranceIdentifier)) {
            return false;
        }
        if (this.csCaseTypeEn != other.csCaseTypeEn) {
            return false;
        }
        if (this.csFeeGroupEn != other.csFeeGroupEn) {
            return false;
        }
        return true;
    }

    /**
     * Case Key is a short unique identifier for a case (this is also used in
     * Checkpoint, especially for iskv21c)
     *
     * @return HospitalIdent_CaseNo
     */
    @Transient
    @JsonIgnore
    public String getCaseKey() {
        final String hosIdent = csHospitalIdent == null ? "NULL" : csHospitalIdent.trim();
        final String caseNumber = csCaseNumber == null ? "NULL" : csCaseNumber.trim();
        return hosIdent + "_" + caseNumber;
    }

    /**
     * Returns the unique Case Key along with the case id in a very compact
     * style
     *
     * @return case signature
     */
    @Transient
    @JsonIgnore
    public String getCaseSignature() {
        String caseKey = getCaseKey();
        String caseId = String.valueOf(this.id);
        return "cs_" + caseKey + ";id_" + caseId;
    }

    @Override
    public String toString() {
        final String hash = Integer.toHexString(this.hashCode());
        final String signature = getCaseSignature();
        return signature + "@" + hash;
    }

    @Transient
    @JsonIgnore
    public boolean isCurrentCaseExternInPatient() {
        boolean result = false;
        TCaseDetails caseExternDetails = this.getCurrentExtern();

        if (caseExternDetails != null
                && (AdmissionReasonEn.ar01.equals(caseExternDetails.getCsdAdmReason12En())
                || AdmissionReasonEn.ar02.equals(caseExternDetails.getCsdAdmReason12En()))) {
            result = true;
        }
        return result;
    }

    @Transient
    @JsonIgnore
    public boolean isCaseBillRelevantForQuote() {
        Integer billingYear = this.getCsBillingYear();

        if (billingYear != null && billingYear >= 2020) {
            return true;
        }
        return false;
    }
    
    public TCaseDetails getCaseDetails4Version(boolean isLocal, int pVersionNumber){
        Set<TCaseDetails> details = isLocal?this.getLocals():this.getExterns();
        for(TCaseDetails det : details){
            if(det.getCsdVersion() == pVersionNumber){
                return det;
            }
        }
        return null;
    }

}
