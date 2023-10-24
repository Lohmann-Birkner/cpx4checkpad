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
 *    2016  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.dto;

import de.lb.cpx.model.enums.CaseStatusEn;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author niemeier
 */
public abstract class DocumentSearchItemDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long patientId;
    private String patientNumber;
    private String patientSecName;
    private String patientFirstName;
    private Date patientDateOfBirth;
    private Long caseId;
    private CaseStatusEn caseStatusEn;
    private String hospitalIdent;
    private String caseNumber;
    private String insuranceNumberPatient;
    private Date admissionDate;
    private Date dischargeDate;
    private boolean isMainFl;
    private Long processId;
    private Long workflowNumber;
    //public int processesAll = 0;
    //public int processesMain = 0;
    //public Long processId;
    //public Long workflowNumber;

    /**
     * @return the patientId
     */
    public Long getPatientId() {
        return patientId;
    }

    /**
     * @param pPatientId the patientId to set
     */
    public final void setPatientId(final Long pPatientId) {
        this.patientId = pPatientId != null && pPatientId.equals(0L) ? null : pPatientId;
    }

    /**
     * @return the patientNumber
     */
    public String getPatientNumber() {
        return patientNumber;
    }

    /**
     * @param pPatientNumber the patientNumber to set
     */
    public final void setPatientNumber(final String pPatientNumber) {
        this.patientNumber = pPatientNumber == null ? "" : pPatientNumber.trim();
    }

    /**
     * @return the patientSecName
     */
    public String getPatientSecName() {
        return patientSecName;
    }

    /**
     * @param pPatientSecName the patientSecName to set
     */
    public final void setPatientSecName(final String pPatientSecName) {
        this.patientSecName = pPatientSecName == null ? "" : pPatientSecName.trim();
    }

    /**
     * @return the patientFirstName
     */
    public String getPatientFirstName() {
        return patientFirstName;
    }

    /**
     * @param pPatientFirstName the patientFirstName to set
     */
    public final void setPatientFirstName(final String pPatientFirstName) {
        this.patientFirstName = pPatientFirstName == null ? "" : pPatientFirstName.trim();
    }

    /**
     * @return the patientDateOfBirth
     */
    public Date getPatientDateOfBirth() {
        return patientDateOfBirth == null ? null : new Date(patientDateOfBirth.getTime());
    }

    /**
     * @param pPatientDateOfBirth the patientDateOfBirth to set
     */
    public final void setPatientDateOfBirth(final Date pPatientDateOfBirth) {
        this.patientDateOfBirth = pPatientDateOfBirth == null ? null : new Date(pPatientDateOfBirth.getTime());
    }

    /**
     * @return the caseId
     */
    public Long getCaseId() {
        return caseId;
    }

    /**
     * @param pCaseId the caseId to set
     */
    public final void setCaseId(final Long pCaseId) {
        this.caseId = pCaseId != null && pCaseId.equals(0L) ? null : pCaseId;
    }

    /**
     * @return the caseStatusEn
     */
    public CaseStatusEn getCaseStatusEn() {
        return caseStatusEn;
    }

    /**
     * @param pCaseStatusEn the caseStatusEn to set
     */
    public final void setCaseStatusEn(final CaseStatusEn pCaseStatusEn) {
        this.caseStatusEn = pCaseStatusEn;
    }

    /**
     * @return the hospitalIdent
     */
    public String getHospitalIdent() {
        return hospitalIdent;
    }

    /**
     * @param pHospitalIdent the hospitalIdent to set
     */
    public final void setHospitalIdent(final String pHospitalIdent) {
        this.hospitalIdent = pHospitalIdent == null ? "" : pHospitalIdent.trim();
    }

    /**
     * @return the caseNumber
     */
    public String getCaseNumber() {
        return caseNumber;
    }

    /**
     * @param pCaseNumber the caseNumber to set
     */
    public final void setCaseNumber(final String pCaseNumber) {
        this.caseNumber = pCaseNumber == null ? "" : pCaseNumber.trim();
    }

    /**
     * @return the caseNumber
     */
    public String getInsuranceNumberPatient() {
        return insuranceNumberPatient;
    }

    /**
     * @param insuranceNumberPatient the insuranceNumberPatient to set
     */
    public final void setInsuranceNumberPatient(final String insuranceNumberPatient) {
        this.insuranceNumberPatient = insuranceNumberPatient == null ? "" : insuranceNumberPatient.trim();
    }

    /**
     * @return the admissionDate
     */
    public Date getAdmissionDate() {
        return admissionDate == null ? null : new Date(admissionDate.getTime());
    }

    /**
     * @param pAdmissionDate the admissionDate to set
     */
    public final void setAdmissionDate(final Date pAdmissionDate) {
        this.admissionDate = pAdmissionDate == null ? null : new Date(pAdmissionDate.getTime());
    }

    /**
     * @return the dischargeDate
     */
    public Date getDischargeDate() {
        return dischargeDate == null ? null : new Date(dischargeDate.getTime());
    }

    /**
     * @param pDischargeDate the dischargeDate to set
     */
    public final void setDischargeDate(final Date pDischargeDate) {
        this.dischargeDate = pDischargeDate == null ? null : new Date(pDischargeDate.getTime());
    }

    /**
     * @return the isMainFl
     */
    public boolean isMainFl() {
        return isMainFl;
    }

    /**
     * @param pIsMainFl the isMainFl to set
     */
    public final void setMainFl(final boolean pIsMainFl) {
        this.isMainFl = pIsMainFl;
    }

    /**
     * @return the processId
     */
    public Long getProcessId() {
        return processId;
    }

    /**
     * @param pProcessId the processId to set
     */
    public final void setProcessId(final Long pProcessId) {
        this.processId = pProcessId != null && pProcessId.equals(0L) ? null : pProcessId;
    }

    /**
     * @return the workflowNumber
     */
    public Long getWorkflowNumber() {
        return workflowNumber;
    }

    /**
     * @param pWorkflowNumber the workflowNumber to set
     */
    public final void setWorkflowNumber(final Long pWorkflowNumber) {
        this.workflowNumber = pWorkflowNumber;
    }

//    public DocumentSearchItemDto(final Long pCaseId, final CaseStatusEn pCaseStatusEn, 
//            final String pCaseNumber, final String pPatientNumber, 
//            final String pPatientSecName, final String pPatientFirstName, 
//            final Date pPatientDateOfBirth, final Date pAdmissionDate, 
//            final Date pDischargeDate) {
//        this.caseId = pCaseId;
//        this.caseStatusEn = pCaseStatusEn;
//        this.caseNumber = pCaseNumber;
//        this.patientNumber = pPatientNumber;
//        this.patientSecName = pPatientSecName;
//        this.patientFirstName = pPatientFirstName;
//        this.patientDateOfBirth = pPatientDateOfBirth;
//        this.admissionDate = pAdmissionDate;
//        this.dischargeDate = pDischargeDate;
//    }
    public abstract String[] toArray();

    @Override
    public String toString() {
        return String.join(";", toArray());
    }

    public String getPatientFullName() {
        final String secName = getPatientSecName();
        final String firstName = getPatientFirstName();
        if (secName != null && !secName.trim().isEmpty()
                && firstName != null && !firstName.trim().isEmpty()) {
            return secName + ", " + firstName;
        }
        if (secName != null && !secName.trim().isEmpty()) {
            return secName;
        }
        return firstName;
    }

}
