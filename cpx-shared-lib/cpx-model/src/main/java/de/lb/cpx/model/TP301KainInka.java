/*
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
 * Contributors:
 *    2017  Shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model;

import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 *
 * @author Shahin
 */
@Entity
@Table(name = "T_P301_KAIN_INKA ",
        indexes = {
            @Index(name = "IDX_KAIN_INKA4T_CASE", columnList = "T_CASE_ID", unique = false)})
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "MESSAGE_TYPE", discriminatorType = DiscriminatorType.STRING)
public class TP301KainInka extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    private TCase tCaseId;//T_CASE_ID
    private String contactReference;//CONTRACT_REFERENCE
    //private String messageType;//MESSAGE_TYPE
    private String processingRef;//PROCESSING_REF
    private String currentTransactionNr;//CURRENT_TRANSACTION_NR
    private String hospitalIdentifier;//HOSPITAL_IDENTIFIER
    private String insuranceIdentifier;//INSURANCE_IDENTIFIER
    private String hospitalNumberPatient;//HOSPITAL_NUMBER_PATIENT
    private String insuranceCaseNumber;//INSURANCE_CASE_NUMBER
    private String insuranceRefNumber;//INSURANCE_REF_NUMBER
    private String costUnitSap;//COST_UNIT_SAP
    private String cpxExternalMsgNr;//CPX_EXTERNAL_MSG_NR (LF301 in SAP)
    private List<TP301KainInkaPvv> kainInkaPvvs;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_P301_KAIN_INKA_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Transient
    public boolean isKain() {
        return "KAIN".equalsIgnoreCase(getMessageType());
    }

    @Transient
    public boolean isInka() {
        return "INKA".equalsIgnoreCase(getMessageType());
    }

    @Transient
    public String getMessageType() {
        if (this.getClass() != TP301KainInka.class) {
            String val = this.getClass().getAnnotation(DiscriminatorValue.class).value();
            if (val == null || val.trim().isEmpty()) {
                return null;
            }
            return val;
        }
        return null;
    }

    /**
     *
     * @return costUnitSap
     */
    @Column(name = "COST_UNIT_SAP", nullable = false, length = 15)
    public String getCostUnitSap() {
        return costUnitSap;
    }

    /**
     *
     * @param costUnitSap :Bezeichnung des Kostenträgers aus SAP (interne Daten)
     */
    public void setCostUnitSap(String costUnitSap) {
        this.costUnitSap = costUnitSap;
    }

    /**
     *
     * @return cpxExternalMsgNr
     */
    @Column(name = "CPX_EXTERNAL_MSG_NR", nullable = true, length = 50)
    public String getCpxExternalMsgNr() {
        return cpxExternalMsgNr;
    }

    /**
     *
     * @param cpxExternalMsgNr :Bezeichnung der externen Message Number aus SAP
     * (LF301)
     */
    public void setCpxExternalMsgNr(String cpxExternalMsgNr) {
        this.cpxExternalMsgNr = cpxExternalMsgNr;
    }

    /**
     *
     * @return processingRef laut Spec 2 Zeichen
     */
    @Column(name = "PROCESSING_REF", nullable = false, length = 10)
    public String getProcessingRef() {
        return processingRef;
    }

    /**
     *
     * @param processingRef :Verarbeitungskennzeichen (FKT)
     */
    public void setProcessingRef(String processingRef) {
        this.processingRef = processingRef;
    }

    /**
     *
     * @return currentTransactionNr laut Spec 2 Zeichen
     */
    @Column(name = "CURRENT_TRANSACTION_NR", nullable = false, length = 10)
    public String getCurrentTransactionNr() {
        return currentTransactionNr;
    }

    /**
     *
     * @param currentTransactionNr :Laufende Nummer des Geschäftsvorfalls (FKT)
     */
    public void setCurrentTransactionNr(String currentTransactionNr) {
        this.currentTransactionNr = currentTransactionNr;
    }

    /**
     *
     * @return hospitalIdentifier laut Spec 9 Zeichen
     */
    @Column(name = "HOSPITAL_IDENTIFIER", nullable = false, length = 20)
    public String getHospitalIdentifier() {
        return hospitalIdentifier;
    }

    /**
     *
     * @param hospitalIdentifier :IK des Krankenhauses (FKT)
     */
    public void setHospitalIdentifier(String hospitalIdentifier) {
        this.hospitalIdentifier = hospitalIdentifier;
    }

    /**
     *
     * @return insuranceIdentifier laut Spec 9 Zeichen
     */
    @Column(name = "INSURANCE_IDENTIFIER", nullable = false, length = 20)
    public String getInsuranceIdentifier() {
        return insuranceIdentifier;
    }

    /**
     *
     * @param insuranceIdentifier :IK der Krankenkasse (FKT)
     */
    public void setInsuranceIdentifier(String insuranceIdentifier) {
        this.insuranceIdentifier = insuranceIdentifier;
    }

    /**
     *
     * @return hospitalNumberPatient laut Spec 15 Zeichen
     */
    @Column(name = "HOSPITAL_NUMBER_PATIENT", nullable = false, length = 25)
    public String getHospitalNumberPatient() {
        return hospitalNumberPatient;
    }

    /**
     *
     * @param hospitalNumberPatient :KH-internes Kennzeichen des
     * Versicherten/Patienten (INV)
     */
    public void setHospitalNumberPatient(String hospitalNumberPatient) {
        this.hospitalNumberPatient = hospitalNumberPatient;
    }

    /**
     *
     * @return insuranceCaseNumber laut Spec 17 Zeichen
     */
    @Column(name = "INSURANCE_CASE_NUMBER", nullable = true, length = 25)
    public String getInsuranceCaseNumber() {
        return insuranceCaseNumber;
    }

    /**
     *
     * @param insuranceCaseNumber :Fall-Nummer der Krankenkasse (INV)
     */
    public void setInsuranceCaseNumber(String insuranceCaseNumber) {
        this.insuranceCaseNumber = insuranceCaseNumber;
    }

    /**
     *
     * @return insuranceRefNumber laut Spec 8 Zeichen
     */
    @Column(name = "INSURANCE_REF_NUMBER", nullable = true, length = 20)
    public String getInsuranceRefNumber() {
        return insuranceRefNumber;
    }

    /**
     *
     * @param insuranceRefNumber :Aktenzeichen der Krankenkasse (INV)
     */
    public void setInsuranceRefNumber(String insuranceRefNumber) {
        this.insuranceRefNumber = insuranceRefNumber;
    }

    /**
     *
     * @return contactReference laut Spec 25 Zeichen
     */
    @Column(name = "CONTRACT_REFERENCE", nullable = true, length = 50)
    public String getContactReference() {
        return contactReference;
    }

    /**
     *
     * @param contactReference :Vertragskennzeichen (INV)
     */
    public void setContactReference(String contactReference) {
        this.contactReference = contactReference;
    }

    /**
     *
     * @return processHospitalId
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "T_CASE_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_KAIN_INKA4T_CASE_ID"))
    public TCase getTCaseId() {
        return tCaseId;
    }

    /**
     *
     * @param tCaseId :Verweis auf zugehörigen Fall (Ausprägung für
     * Krankenhäuser)
     */
    public void setTCaseId(TCase tCaseId) {
        this.tCaseId = tCaseId;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "p301KainInkaId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public List<TP301KainInkaPvv> getKainInkaPvvs() {
        return this.kainInkaPvvs;
    }

//    public void setKainInkaPvvs(final List<TP301KainInkaPvv> pKainInkaPvvs) {
//        if (this.kainInkaPvvs == null) {
//            this.kainInkaPvvs = pKainInkaPvvs;
//        } else {
//            this.kainInkaPvvs.clear();
//            if (pKainInkaPvvs != null) {
//                this.kainInkaPvvs.addAll(pKainInkaPvvs);
//            }
//        }
//    }
    public void setKainInkaPvvs(final List<TP301KainInkaPvv> kainInkaPvvs) {
        this.kainInkaPvvs = kainInkaPvvs;
    }

}
