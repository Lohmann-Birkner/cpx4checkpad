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
package de.lb.cpx.shared.dto;

import java.util.Date;

/**
 *
 * @author Wilde
 */
public class WorkflowListItemDTO extends SearchItemDTO {

    private static final long serialVersionUID = 1L;

    private Long workflowNumber; //Vorgangnsr.
    private Date creationDate; //Anlegedatum
    private String subject;
    private Integer wmState; //
//    private String wmType;
    private String patNumber; //Pat.-Nummer
    private String insNumber;
    private String csCaseNumber; ///Fallnummer
    private Date csdAdmissionDate; //Aufnahmedatum
    private Date csdDischargeDate;
    private String csHospitalIdent; //IKZ
    private Long reminderId; //Wiedervorlage-ID
    private Long assSubject; //Wiedervorlage
    private Long assSender; //Wiedervorlage Sender
    // private Date assCreationDate; //Wiedervorlage angelegt
    private Long assReceiver; //Wiedervorlage Empfänger
    private Date assLastModificationDate;// Änderungsdatum Dokument und Aktion
    private String csTypeOfService;
    private String insInsCompany; // IKZ Versicherung
    private String insInsCompanyName; //Versicherungsnamen
    private String insInsCompanyShortName; //Versicherungs-/Krankenkassengruppen
    private Date requestStartAudit;// Einleitung Prüfverfahren
    private Date requestReportDate; // Gutachtendatum
    //private Date remCreated; // Datum älteste WV
    private Date remLatestCreationDate;// Datum jüngste WV
    //private Integer remSubject;//WV Bezeichnung
    private Long vmModUser;// letzter Bearbeiter Vorgang
    private Long assUser;// letzter Bearbeiter Vorgang
    private Boolean remFinished;
    private String csdAdmCauseEn;
    private Long processTopic;
    private Long processResult;
    private Date billCorrectionDeadline;
    private Date mdkAuditCompletionDeadline;
    private Date mdkDocumentDeliverDeadline;
    private Date preliminaryProceedingsClosedDeadline;
    private Date preliminaryProceedingAnswerDeadline;
    private Date dataRecordCorrectionDeadline;
    private Integer mdkAuditCompletionDeadlineDays;
    private Integer mdkDocumentDeliverDeadlineDays;
    private Integer preliminaryProceedingsClosedDeadlineDays;
    private Integer preliminaryProceedingAnswerDeadlineDays;
    private Integer dataRecordCorrectionDeadlineDays;
    private Integer billCorrectionDeadlineDays;
    private Integer requestType;
    private String wvComment;
    private String actionComment;
    private Boolean wvPrio;
//    private Long mainMdkAuditReasons;
    private Double cwInitial;
    private Double cwDiff;
    private Double cwFinal;
    private String drgInitial;

    private String drgFinal;
    private Double supFeeInitial;
    private Double supFeeDiff;
    private Double supFeeFinal;
    private Integer losInitial;
    private Integer losDiff;
    private Integer losFinal;
    private String caseType; //Abrechnungsart
    private Double revenueDiff;// ErlösDiff
    private Date closingDate;
    private Long mdkState;
    private Date mdkSubseqProcDate;
    private boolean isCancel;
    private String courtFileNumber;
    private String lawerFileNumber;
    private Date insuranceRecivedBillDate;
    private String auditNames;
    private Double cwCareFinal;
    private Double cwCareDiff;
    private Double cwCareInitial;
    private Double resultDelta;
    private Double savedMoney;
    private Long latestActionSubject;
    private Double revenueInit;// ErlösDiff
    private Double revenueFinal;// ErlösDiff
// review deadlines
    private Date reviewDeadline;
    private Date reviewRenewalDeadline;
    private Date reviewInsReplyDeadline;
    private Date reviewReplySendDocDeadline;
    private Date reviewCompletionDeadline;
    private Integer reviewDeadlineDays;
    private Integer reviewRenewalDeadlineDays;
    private Integer reviewInsReplyDeadlineDays;
    private Integer reviewReplySendDocDeadlineDays;
    private Integer reviewCompletionDeadlineDays;
    private Double penaltyFee;
    //kain/inka keys
    private String kainKeyEn;
    private String inkaKeyEn;
    private Date mdHosStartAudit;   
    private Date dateOfBenefitLawDecision;
    /**
     * @return the workflowNumber
     */
    public Long getWorkflowNumber() {
        return workflowNumber;
    }

    /**
     * @param workflowNumber the workflowNumber to set
     */
    public void setWorkflowNumber(Long workflowNumber) {
        this.workflowNumber = workflowNumber;
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate == null ? null : new Date(creationDate.getTime());
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate == null ? null : new Date(creationDate.getTime());
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the wmState
     */
    public Integer getWmState() {
        return wmState;
        //return WorkflowState.fromInteger(Integer.valueOf(wmState)).toString();
    }

    /**
     * @param wmState the wmState to set
     */
    public void setWmState(Integer wmState) {
        this.wmState = wmState;
    }

//    /**
//     * @return the wmType
//     */
//    public String getWmType() {
//        return wmType;
//    }
//
//    /**
//     * @param wmType the wmType to set
//     */
//    public void setWmType(String wmType) {
//        this.wmType = wmType;
//    }
    /**
     * @return the patNumber
     */
    @Override
    public String getPatNumber() {
        return patNumber;
    }

    /**
     * @param patNumber the patNumber to set
     */
    public void setPatNumber(String patNumber) {
        this.patNumber = patNumber;
    }

    /**
     * @return the csCaseNumber
     */
    public String getCsCaseNumber() {
        return csCaseNumber;
    }

    /**
     * @param csCaseNumber the csCaseNumber to set
     */
    public void setCsCaseNumber(String csCaseNumber) {
        this.csCaseNumber = csCaseNumber;
    }

    /**
     * @return the csdAdmissionDate
     */
    public Date getCsdAdmissionDate() {
        return csdAdmissionDate == null ? null : new Date(csdAdmissionDate.getTime());
    }

    /**
     * @param csdAdmissionDate the csdAdmissionDate to set
     */
    public void setCsdAdmissionDate(Date csdAdmissionDate) {
        this.csdAdmissionDate = csdAdmissionDate == null ? null : new Date(csdAdmissionDate.getTime());
    }

    /**
     * @return the closingDate
     */
    public Date getClosingDate() {
        return closingDate == null ? null : new Date(closingDate.getTime());
    }

    /**
     * @param closingDate the closingDate to set
     */
    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate == null ? null : new Date(closingDate.getTime());
    }
    //RSH - CPX-629 :20170829

    /**
     * @return the CsdDischargeDate
     */
    public Date getCsdDischargeDate() {
        return csdDischargeDate == null ? null : new Date(csdDischargeDate.getTime());
    }

    /**
     * @param csdDischargeDate the csdDischargeDate to set
     */
    public void setCsdDischargeDate(Date csdDischargeDate) {
        this.csdDischargeDate = csdDischargeDate == null ? null : new Date(csdDischargeDate.getTime());
    }

    /**
     * @return the csHospitalIdent
     */
    public String getCsHospitalIdent() {
        return csHospitalIdent;
    }

    /**
     * @param csHospitalIdent the csHospitalIdent to set
     */
    public void setCsHospitalIdent(String csHospitalIdent) {
        this.csHospitalIdent = csHospitalIdent;
    }

    @Override
    public String getHospitalIdent() {
        return getCsHospitalIdent();
    }

    @Override
    public String getCaseNumber() {
        return getCsCaseNumber();
    }

    /**
     * @return the assSubject
     */
    public Long getAssSubject() {
        return assSubject;
    }

    /**
     * @return the reminderId
     */
    public Long getReminderId() {
        return reminderId;
    }

    /**
     * @param assSubject the assSubject to seft
     */
    public void setAssSubject(Long assSubject) {
        this.assSubject = assSubject;
    }

    /**
     * @param reminderId the reminderId to set
     */
    public void setReminderId(Long reminderId) {
        this.reminderId = reminderId;
    }

    /**
     * @return the assSender
     */
    public Long getAssSender() {
        return assSender;
    }

    /**
     * @param assSender the assSender to set
     */
    public void setAssSender(Long assSender) {
        this.assSender = assSender;
    }

    /**
     * @return the assCreationDate
     */
//  public Date getAssCreationDate() {
//    return assCreationDate;
//  }
//
//  /**
//   * @param assCreationDate the assCreationDate to set
//   */
//  public void setAssCreationDate(Date assCreationDate) {
//    this.assCreationDate = assCreationDate;
//  }
    /**
     * @return the assReceiver
     */
    public Long getAssReceiver() {
        return assReceiver;
    }

    /**
     * @param assReceiver the assReceiver to set
     */
    public void setAssReceiver(Long assReceiver) {
        this.assReceiver = assReceiver;
    }

    /**
     * @return the insNumber
     */
    @Override
    public String getInsNumber() {
        return insNumber;
    }

    /**
     * @param insNumber the insNumber to set
     */
    @Override
    public void setInsNumber(String insNumber) {
        this.insNumber = insNumber;
    }

    /**
     * @return the assLastModificationDate
     */
    public Date getAssLastModificationDate() {
        return assLastModificationDate == null ? null : new Date(assLastModificationDate.getTime());
    }

    /**
     * @param assLastModificationDate the assLastModificationDate to set
     */
    public void setAssLastModificationDate(Date assLastModificationDate) {
        this.assLastModificationDate = assLastModificationDate == null ? null : new Date(assLastModificationDate.getTime());
    }

    /**
     * @return the csTypeOfService
     */
    public String getCsTypeOfService() {
        return csTypeOfService;
    }

    /**
     * @param csTypeOfService the csTypeOfService to set
     */
    public void setCsTypeOfService(String csTypeOfService) {
        this.csTypeOfService = csTypeOfService;
    }

    /**
     * @return the insInsCompany
     */
    public String getInsInsCompany() {
        return insInsCompany;
    }

    /**
     * @param insInsCompany the insInsCompany to set
     */
    public void setInsInsCompany(String insInsCompany) {
        this.insInsCompany = insInsCompany;
    }

    /**
     * @return the insInsCompanyName
     */
    public String getInsInsCompanyName() {
        return insInsCompanyName;
    }

    /**
     * @param insInsCompanyName the insInsCompanyName to set
     */
    public void setInsInsCompanyName(String insInsCompanyName) {
        this.insInsCompanyName = insInsCompanyName;
    }

    /**
     * @return the insInsCompanyShortName
     */
    public String getInsInsCompanyShortName() {
        return insInsCompanyShortName;
    }

    /**
     * @param insInsCompanyShortName the insInsCompanyShortName to set
     */
    public void setInsInsCompanyShortName(String insInsCompanyShortName) {
        this.insInsCompanyShortName = insInsCompanyShortName;
    }

//    /**
//     * @return the assPrio
//     */
//    public boolean getAssPrio() {
//        return assPrio;
//    }
//
//    /**
//     * @param assPrio the assPrio to set
//     */
//    public void setAssPrio(boolean assPrio) {
//        this.assPrio = assPrio;
//    }
    /**
     * @return the requestStartAudit war assTestProcesses
     */
    public Date getRequestStartAudit() {
        return requestStartAudit == null ? null : new Date(requestStartAudit.getTime());
    }

    /**
     * @param  assTestProcesses the requestStartAudit war assTestProcesses to set
     */
    public void setRequestStartAudit(Date requestStartAudit) {
        this.requestStartAudit = requestStartAudit == null ? null : new Date(requestStartAudit.getTime());
    }
    /**
     * @return the assTestProcesses
     */
    public Date getRequestReportDate() {
        return requestReportDate == null ? null : new Date(requestReportDate.getTime());
    }

    /**
     * @param assTestProcesses the assTestProcesses to set
     */
    public void setRequestReportDate(Date requestReportDate) {
        this.requestReportDate = requestReportDate == null ? null : new Date(requestReportDate.getTime());
    }

    /**
     * @return the remCreated
     */
//    public Date getRemCreated() {
//        return remCreated;
//    }
//
//    /**
//     * @param remCreated the remCreated to set
//     */
//    public void setRemCreated(Date remCreated) {
//        this.remCreated = remCreated;
//    }
    /**
     * @return the remLatestCreationDate
     */
    public Date getRemLatestCreationDate() {
        return remLatestCreationDate == null ? null : new Date(remLatestCreationDate.getTime());
    }

    /**
     * @param remLatestCreationDate the remLatestCreationDate to set
     */
    public void setRemLatestCreationDate(Date remLatestCreationDate) {
        this.remLatestCreationDate = remLatestCreationDate == null ? null : new Date(remLatestCreationDate.getTime());
    }

//    /**
//     * @return the remSubject
//     */
//    public Integer getRemSubject() {
//        return remSubject;
//    }
//
//    /**
//     * @param remSubject the remSubject to set
//     */
//    public void setRemSubject(Integer remSubject) {
//        this.remSubject = remSubject;
//    }
    /**
     * @return the vmModUser
     */
    public Long getVmModUser() {
        return vmModUser;
    }

    /**
     * @param vmModUser the vmModUser to set
     */
    public void setVmModUser(Long vmModUser) {
        this.vmModUser = vmModUser;
    }

    /**
     * @return the assUser
     */
    public Long getAssUser() {
        return assUser;
    }

    /**
     * @param assUser the assUser to set
     */
    public void setAssUser(Long assUser) {
        this.assUser = assUser;
    }

    /**
     * @return the remFinished
     */
    public Boolean getRemFinished() {
        return remFinished;
    }

    /**
     * @param remFinished the remFinished to set
     */
    public void setRemFinished(Boolean remFinished) {
        this.remFinished = remFinished;
    }

    /**
     * @return the csdAdmCauseEn
     */
    public String getCsdAdmCauseEn() {
        return csdAdmCauseEn;
    }

    /**
     * @param csdAdmCauseEn the csdAdmCauseEn to set
     */
    public void setCsdAdmCauseEn(String csdAdmCauseEn) {
        this.csdAdmCauseEn = csdAdmCauseEn;
    }

    /**
     * @return the processTopic
     */
    public Long getProcessTopic() {
        return processTopic;
    }

    /**
     * @param processTopic the processTopic to set
     */
    public void setProcessTopic(Long processTopic) {
        this.processTopic = processTopic;
    }

    /**
     * @return the processResult
     */
    public Long getProcessResult() {
        return processResult;
    }

    /**
     * @param processResult the processResult to set
     */
    public void setProcessResult(Long processResult) {
        this.processResult = processResult;
    }

    /**
     * @return the billCorrectionDeadline
     */
    public Date getBillCorrectionDeadline() {
        return billCorrectionDeadline == null ? null : new Date(billCorrectionDeadline.getTime());
    }

    /**
     * @param bill_correction_deadline the billCorrectionDeadline to set
     */
    public void setBillCorrectionDeadline(Date bill_correction_deadline) {
        this.billCorrectionDeadline = bill_correction_deadline == null ? null : new Date(bill_correction_deadline.getTime());
    }

    /**
     * @return the mdkAuditCompletionDeadline
     */
    public Date getMdkAuditCompletionDeadline() {
        return mdkAuditCompletionDeadline == null ? null : new Date(mdkAuditCompletionDeadline.getTime());
    }

    /**
     * @param mdk_audit_completion_deadline the mdkAuditCompletionDeadline to
     * set
     */
    public void setMdkAuditCompletionDeadline(Date mdk_audit_completion_deadline) {
        this.mdkAuditCompletionDeadline = mdk_audit_completion_deadline == null ? null : new Date(mdk_audit_completion_deadline.getTime());
    }

    /**
     * @return the mdkDocumentDeliverDeadline
     */
    public Date getMdkDocumentDeliverDeadline() {
        return mdkDocumentDeliverDeadline == null ? null : new Date(mdkDocumentDeliverDeadline.getTime());
    }

    /**
     * @param mdk_document_deliver_deadline the mdkDocumentDeliverDeadline to
     * set
     */
    public void setMdkDocumentDeliverDeadline(Date mdk_document_deliver_deadline) {
        this.mdkDocumentDeliverDeadline = mdk_document_deliver_deadline == null ? null : new Date(mdk_document_deliver_deadline.getTime());
    }

    /**
     * @return the preliminaryProceedingsClosedDeadline
     */
    public Date getPreliminaryProceedingsClosedDeadline() {
        return preliminaryProceedingsClosedDeadline == null ? null : new Date(preliminaryProceedingsClosedDeadline.getTime());
    }

    /**
     * @param preliminary_proceedings_closed_deadline the
     * preliminaryProceedingsClosedDeadline to set
     */
    public void setPreliminaryProceedingsClosedDeadline(Date preliminary_proceedings_closed_deadline) {
        this.preliminaryProceedingsClosedDeadline = preliminary_proceedings_closed_deadline == null ? null : new Date(preliminary_proceedings_closed_deadline.getTime());
    }

    /**
     * @return the preliminary_proceeding_answer_deadline
     */
    public Date getPreliminaryProceedingAnswerDeadline() {
        return preliminaryProceedingAnswerDeadline == null ? null : new Date(preliminaryProceedingAnswerDeadline.getTime());
    }

    /**
     * @param preliminary_proceeding_answer_deadline the
     * preliminary_proceeding_answer_deadline to set
     */
    public void setPreliminaryProceedingAnswerDeadline(Date preliminary_proceeding_answer_deadline) {
        this.preliminaryProceedingAnswerDeadline = preliminary_proceeding_answer_deadline == null ? null : new Date(preliminary_proceeding_answer_deadline.getTime());
    }

    /**
     * @return the dataRecordCorrectionDeadline
     */
    public Date getDataRecordCorrectionDeadline() {
        return dataRecordCorrectionDeadline == null ? null : new Date(dataRecordCorrectionDeadline.getTime());
    }

    /**
     * @param data_record_correction_deadline the dataRecordCorrectionDeadline
     * to set
     */
    public void setDataRecordCorrectionDeadline(Date data_record_correction_deadline) {
        this.dataRecordCorrectionDeadline = data_record_correction_deadline == null ? null : new Date(data_record_correction_deadline.getTime());
    }

    /**
     * @return the mdk_audit_completion_deadline_days
     */
    public Integer getMdkAuditCompletionDeadlineDays() {
        return mdkAuditCompletionDeadlineDays;
    }

    /**
     * @param mdk_audit_completion_deadline_days the
     * mdk_audit_completion_deadline_days to set
     */
    public void setMdkAuditCompletionDeadlineDays(Integer mdk_audit_completion_deadline_days) {
        this.mdkAuditCompletionDeadlineDays = mdk_audit_completion_deadline_days;
    }

    /**
     * @return the mdk_document_deliver_deadline_days
     */
    public Integer getMdkDocumentDeliverDeadlineDays() {
        return mdkDocumentDeliverDeadlineDays;
    }

    /**
     * @param mdk_document_deliver_deadline_days the
     * mdk_document_deliver_deadline_days to set
     */
    public void setMdkDocumentDeliverDeadlineDays(Integer mdk_document_deliver_deadline_days) {
        this.mdkDocumentDeliverDeadlineDays = mdk_document_deliver_deadline_days;
    }

    /**
     * @return the preliminaryProceedingsClosedDeadlineDays
     */
    public Integer getPreliminaryProceedingsClosedDeadlineDays() {
        return preliminaryProceedingsClosedDeadlineDays;
    }

    /**
     * @param preliminary_proceedings_closed_deadline_days the
     * preliminaryProceedingsClosedDeadlineDays to set
     */
    public void setPreliminaryProceedingsClosedDeadlineDays(Integer preliminary_proceedings_closed_deadline_days) {
        this.preliminaryProceedingsClosedDeadlineDays = preliminary_proceedings_closed_deadline_days;
    }

    /**
     * @return the preliminaryProceedingAnswerDeadlineDays
     */
    public Integer getPreliminaryProceedingAnswerDeadlineDays() {
        return preliminaryProceedingAnswerDeadlineDays;
    }

    /**
     * @param preliminary_proceeding_answer_deadline_days the
     * preliminaryProceedingAnswerDeadlineDays to set
     */
    public void setPreliminaryProceedingAnswerDeadlineDays(Integer preliminary_proceeding_answer_deadline_days) {
        this.preliminaryProceedingAnswerDeadlineDays = preliminary_proceeding_answer_deadline_days;
    }

    /**
     * @return the dataRecordCorrectionDeadlineDays
     */
    public Integer getDataRecordCorrectionDeadlineDays() {
        return dataRecordCorrectionDeadlineDays;
    }

    /**
     * @param data_record_correction_deadline_days the
     * dataRecordCorrectionDeadlineDays to set
     */
    public void setDataRecordCorrectionDeadlineDays(Integer data_record_correction_deadline_days) {
        this.dataRecordCorrectionDeadlineDays = data_record_correction_deadline_days;
    }

    /**
     * @return the billCorrectionDeadlineDays
     */
    public Integer getBillCorrectionDeadlineDays() {
        return billCorrectionDeadlineDays;
    }

    /**
     * @param bill_correction_deadline_days the billCorrectionDeadlineDays to
     * set
     */
    public void setBillCorrectionDeadlineDays(Integer bill_correction_deadline_days) {
        this.billCorrectionDeadlineDays = bill_correction_deadline_days;
    }

    /**
     * @return the request_typ
     */
    public Integer getRequestType() {
        return requestType;
    }

    /**
     * @param request_type the request_typ to set
     */
    public void setRequestType(Integer request_type) {
        this.requestType = request_type;
    }

    /**
     *
     * @return WvComment
     */
    public String getWvComment() {
        return wvComment;
    }

    /**
     * @param wvComment the wvComment to set
     */
    public void setWvComment(String wvComment) {
        this.wvComment = wvComment;
    }

    /**
     *
     * @return wvPrio
     */
    public Boolean getWvPrio() {
        return wvPrio;
    }

    /**
     *
     * @param wvPrio the wvPrio to set
     */
    public void setWvPrio(Boolean wvPrio) {
        this.wvPrio = wvPrio;
    }

//    /**
//     *
//     * @return mainMdkAuditReasons
//     */
//    public Long getMainMdkAuditReasons() {
//        return mainMdkAuditReasons;
//    }
//
//    /**
//     *
//     * @param mainMdkAuditReasons the mainMdkAuditReasons to set
//     */
//    public void setMainMdkAuditReasons(Long mainMdkAuditReasons) {
//        this.mainMdkAuditReasons = mainMdkAuditReasons;
//    }
    /**
     *
     * @return cwInitial
     */
    public Double getCwInitial() {
        return cwInitial;
    }

    /**
     *
     * @param cwInitial the cwInitial to set
     */
    public void setCwInitial(Double cwInitial) {
        this.cwInitial = cwInitial;
    }

    /**
     *
     * @return cwDiff
     */
    public Double getCwDiff() {
        return cwDiff;
    }

    /**
     *
     * @param cwDiff the cwFinal to set
     */
    public void setCwDiff(Double cwDiff) {
        this.cwDiff = cwDiff;
    }

    /**
     *
     * @return cwFinal
     */
    public Double getCwFinal() {
        return cwFinal;
    }

    /**
     *
     * @param cwFinal the cwFinal to set
     */
    public void setCwFinal(Double cwFinal) {
        this.cwFinal = cwFinal;
    }

    /**
     *
     * @return drgInitial
     */
    public String getDrgInitial() {
        return drgInitial;
    }

    /**
     *
     * @param drgInitial the drgInitial to set
     */
    public void setDrgInitial(String drgInitial) {
        this.drgInitial = drgInitial;
    }

    /**
     *
     * @return drgFinal
     */
    public String getDrgFinal() {
        return drgFinal;
    }

    /**
     *
     * @param drgFinal the drgFinal to set
     */
    public void setDrgFinal(String drgFinal) {
        this.drgFinal = drgFinal;
    }

    /**
     *
     * @return supFeeInitial
     */
    public Double getSupFeeInitial() {
        return supFeeInitial;
    }

    /**
     *
     * @param supFeeInitial the supFeeInitial to set
     */
    public void setSupFeeInitial(Double supFeeInitial) {
        this.supFeeInitial = supFeeInitial;
    }

    /**
     *
     * @return supFeeDiff
     */
    public Double getSupFeeDiff() {
        return supFeeDiff;
    }

    /**
     *
     * @param supFeeDiff the supFeeFinal to set
     */
    public void setSupFeeDiff(Double supFeeDiff) {
        this.supFeeDiff = supFeeDiff;
    }

    /**
     *
     * @return supFeeFinal
     */
    public Double getSupFeeFinal() {
        return supFeeFinal;
    }

    /**
     *
     * @param supFeeFinal the supFeeFinal to set
     */
    public void setSupFeeFinal(Double supFeeFinal) {
        this.supFeeFinal = supFeeFinal;
    }

    /**
     *
     * @return losInitial
     */
    public Integer getLosInitial() {
        return losInitial;
    }

    /**
     *
     * @param losInitial the losInitial to set
     */
    public void setLosInitial(Integer losInitial) {
        this.losInitial = losInitial;
    }

    /**
     *
     * @return losDiff
     */
    public Integer getLosDiff() {
        return losDiff;
    }

    /**
     *
     * @param losDiff the losFinal to set
     */
    public void setLosDiff(Integer losDiff) {
        this.losDiff = losDiff;
    }

    /**
     *
     * @return losFinal
     */
    public Integer getLosFinal() {
        return losFinal;
    }

    /**
     *
     * @param losFinal the losFinal to set
     */
    public void setLosFinal(Integer losFinal) {
        this.losFinal = losFinal;
    }

    /**
     *
     * @param caseType Abrechnungart(FallArt)
     */
    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getCaseType() {
        return caseType;
    }

    /**
     *
     * @return revenueDiff
     */
    public Double getRevenueDiff() {
        return revenueDiff;
    }

    /**
     *
     * @param revenueDiff the revenueDiff to set
     */
    public void setRevenueDiff(Double revenueDiff) {
        this.revenueDiff = revenueDiff;
    }

    /**
     * @return the mdkState
     */
    public Long getMdkState() {
        return mdkState;
    }

    /**
     * @param mdkState the mdkState to set
     */
    public void setMdkState(Long mdkState) {
        this.mdkState = mdkState;
    }

    /**
     * @return the mdkSubseqProcDate
     */
    public Date getMdkSubseqProcDate() {
        return mdkSubseqProcDate == null ? null : new Date(mdkSubseqProcDate.getTime());
    }

    /**
     * @param mdkSubseqProcDate the mdkSubseqProcDate to set
     */
    public void setMdkSubseqProcDate(Date mdkSubseqProcDate) {
        this.mdkSubseqProcDate = mdkSubseqProcDate == null ? null : new Date(mdkSubseqProcDate.getTime());
    }

    /**
     *
     * @param isCancel Cancel_Fl to set
     */
    public void setIsCancel(boolean isCancel) {
        this.isCancel = isCancel;
    }

    /**
     *
     * @return the Cancel_Fl
     */
    public boolean isIsCancel() {
        return isCancel;
    }

    /**
     *
     * @param courtFileNumber courtFileNumber to set
     */
    public void setCourtFileNumber(String courtFileNumber) {
        this.courtFileNumber = courtFileNumber;
    }

    /**
     *
     * @param lawerFileNumber lawerFileNumber to set
     */
    public void setLawerFileNumber(String lawerFileNumber) {
        this.lawerFileNumber = lawerFileNumber;
    }

    /**
     *
     * @return courtFileNumber
     */
    public String getCourtFileNumber() {
        return courtFileNumber;
    }

    /**
     *
     * @return lawerFileNumber
     */
    public String getLawerFileNumber() {
        return lawerFileNumber;
    }

    /**
     *
     * @return actionComment
     */
    public String getActionComment() {
        return actionComment;
    }

    /**
     *
     * @param actionComment actionComment to set
     */
    public void setActionComment(String actionComment) {

//        if(actionComment.length()>255)
//        this.actionComment = actionComment.substring(0,254);
//       else
        this.actionComment = actionComment == null ? "" : actionComment.length() > 255 ? actionComment.substring(0, 254) : actionComment;

    }

    /**
     *
     * @param insuranceRecivedBillDate insuranceRecivedBillDate to set
     */
    public void setInsuranceRecivedBillDate(Date insuranceRecivedBillDate) {
        this.insuranceRecivedBillDate = insuranceRecivedBillDate;
    }

    /**
     *
     * @return insuranceRecivedBillDate
     */
    public Date getInsuranceRecivedBillDate() {
        return insuranceRecivedBillDate == null ? null : new Date(insuranceRecivedBillDate.getTime());
    }

    /**
     *
     * @return cwCareInitial
     */
    public Double getCwCareInitial() {
        return cwCareInitial;
    }

    /**
     *
     * @param cwCareInitial the cwCareInitial to set
     */
    public void setCwCareInitial(Double cwCareInitial) {
        this.cwCareInitial = cwCareInitial;
    }

    /**
     *
     * @return cwCareDiff
     */
    public Double getCwCareDiff() {
        return cwCareDiff;
    }

    /**
     *
     * @param cwCareDiff the cwCareFinal to set
     */
    public void setCwCareDiff(Double cwCareDiff) {
        this.cwCareDiff = cwCareDiff;
    }

    /**
     *
     * @return cwCareFinal
     */
    public Double getCwCareFinal() {
        return cwCareFinal;
    }

    /**
     *
     * @param cwCareFinal the cwCareFinal to set
     */
    public void setCwCareFinal(Double cwCareFinal) {
        this.cwCareFinal = cwCareFinal;
    }
    
    /**
     *
     * @return savedMoney
     */
    public Double getSavedMoney() {
        return savedMoney;
    }

    /**
     *
     * @param savedMoney the savedMoney to set
     */
    public void setSavedMoney(Double savedMoney) {
        this.savedMoney = savedMoney;
    }
    
    /**
     *
     * @return resultDelta
     */
    public Double getResultDelta() {
        return resultDelta;
    }

    /**
     *
     * @param resultDelta the resultDelta to set
     */
    public void setResultDelta(Double resultDelta) {
        this.resultDelta = resultDelta;
    }
    
    /**
     * @return the latestActionSubject
     */
    public Long getLatestActionSubject() {
        return latestActionSubject;
    }

    /**
     * @param latestActionSubject the latestActionSubject to set
     */
    public void setLatestActionSubject(Long latestActionSubject) {
        this.latestActionSubject = latestActionSubject;
    }
    
    /*
    @Override
    public String toString() {
        return "workflowNumber=" + addParamValues(workflowNumber)
                + ", creationDate=" + addParamValues(creationDate)
                + ", subject=" + addParamValues(subject)
                + ", wmState=" + addParamValues(wmState)
                + ", patNumber=" + addParamValues(patNumber)
                + ", insNumber=" + addParamValues(insNumber)
                + ", csCaseNumber=" + addParamValues(csCaseNumber)
                + ", csdAdmissionDate=" + addParamValues(csdAdmissionDate)
                + ", csdDischargeDate=" + addParamValues(csdDischargeDate)
                + ", csHospitalIdent=" + addParamValues(csHospitalIdent)
                + ", reminderId=" + addParamValues(reminderId)
                + ", assSubject=" + addParamValues(assSubject)
                + ", assSender=" + addParamValues(assSender)
                + ", assReceiver=" + addParamValues(assReceiver)
                + ", assLastModificationDate=" + addParamValues(assLastModificationDate)
                + ", csTypeOfService=" + addParamValues(csTypeOfService)
                + ", insInsCompany=" + addParamValues(insInsCompany)
                + ", insInsCompanyName=" + addParamValues(insInsCompanyName)
                + ", assTestProcesses=" + addParamValues(assTestProcesses)
                + ", remLatestCreationDate=" + addParamValues(remLatestCreationDate)
                + ", vmModUser=" + addParamValues(vmModUser)
                + ", assUser=" + addParamValues(assUser)
                + ", remFinished=" + addParamValues(remFinished)
                + ", csdAdmCauseEn=" + addParamValues(csdAdmCauseEn)
                + ", processTopic=" + addParamValues(processTopic)
                + ", processResult=" + addParamValues(processResult)
                + ", billCorrectionDeadline=" + addParamValues(billCorrectionDeadline)
                + ", mdkAuditCompletionDeadline=" + addParamValues(mdkAuditCompletionDeadline)
                + ", mdkDocumentDeliverDeadline=" + addParamValues(mdkDocumentDeliverDeadline)
                + ", preliminaryProceedingsClosedDeadline=" + addParamValues(preliminaryProceedingsClosedDeadline)
                + ", preliminaryProceedingAnswerDeadline=" + addParamValues(preliminaryProceedingAnswerDeadline)
                + ", dataRecordCorrectionDeadline=" + addParamValues(dataRecordCorrectionDeadline)
                + ", mdkAuditCompletionDeadlineDays=" + addParamValues(mdkAuditCompletionDeadlineDays)
                + ", mdkDocumentDeliverDeadlineDays=" + addParamValues(mdkDocumentDeliverDeadlineDays)
                + ", preliminaryProceedingsClosedDeadlineDays=" + addParamValues(preliminaryProceedingsClosedDeadlineDays)
                + ", preliminaryProceedingAnswerDeadlineDays=" + addParamValues(preliminaryProceedingAnswerDeadlineDays)
                + ", dataRecordCorrectionDeadlineDays=" + addParamValues(dataRecordCorrectionDeadlineDays)
                + ", billCorrectionDeadlineDays=" + addParamValues(billCorrectionDeadlineDays)
                + ", requestType=" + addParamValues(requestType)
                + ", dischargDepartment=" + addParamValues(dischargDepartment)
                + ", dischargDepartment301=" + addParamValues(dischargDepartment301)
                + ", dischargDepartmentName=" + addParamValues(dischargDepartmentName)
                + ", wvComment=" + addParamValues(wvComment)
                + ", actionComment=" + addParamValues(actionComment)
                + ", wvPrio=" + addParamValues(wvPrio)
                + ", cwInitial=" + addParamValues(cwInitial)
                + ", cwDiff=" + addParamValues(cwDiff)
                + ", cwFinal=" + addParamValues(cwFinal)
                + ", drgInitial=" + addParamValues(drgInitial)
                + ", drgFinal=" + addParamValues(drgFinal)
                + ", supFeeInitial=" + addParamValues(supFeeInitial)
                + ", supFeeDiff=" + addParamValues(supFeeDiff)
                + ", supFeeFinal=" + addParamValues(supFeeFinal)
                + ", losInitial=" + addParamValues(losInitial)
                + ", losDiff=" + addParamValues(losDiff)
                + ", losFinal=" + addParamValues(losFinal)
                + ", caseType=" + addParamValues(caseType)
                + ", revenueDiff=" + addParamValues(revenueDiff)
                + ", closingDate=" + addParamValues(closingDate)
                + ", mdkState=" + addParamValues(mdkState)
                + ", mdkSubseqProcDate=" + addParamValues(mdkSubseqProcDate)
                + ", isCancel=" + addParamValues(isCancel)
                + ", courtFileNumber=" + addParamValues(courtFileNumber)
                + ", lawerFileNumber=" + addParamValues(lawerFileNumber);
    }
     */
//    public Map<String, String> createWorkflowListAtrrHashMap() {
//        final Map<String, String> h = new HashMap<>();
//
//        addParamValues(h, "workflowNumber", workflowNumber);
//        addParamValues(h, "creationDate", creationDate);
//        addParamValues(h, "subject", subject);
//        addParamValues(h, "wmState", wmState);
//        addParamValues(h, "patNumber", patNumber);
//        addParamValues(h, "insNumber", insNumber);
//        addParamValues(h, "csCaseNumber", csCaseNumber);
//        addParamValues(h, "csdAdmissionDate", csdAdmissionDate);
//        addParamValues(h, "csdDischargeDate", csdDischargeDate);
//        addParamValues(h, "csHospitalIdent", csHospitalIdent);
//        addParamValues(h, "reminderId", reminderId);
//        addParamValues(h, "assSubject", assSubject);
//        addParamValues(h, "assSender", assSender);
//        addParamValues(h, "assReceiver", assReceiver);
//        addParamValues(h, "assLastModificationDate", assLastModificationDate);
//        addParamValues(h, "csTypeOfService", csTypeOfService);
//        addParamValues(h, "insInsCompany", insInsCompany);
//        addParamValues(h, "insInsCompanyName", insInsCompanyName);
//        addParamValues(h, "assTestProcesses", assTestProcesses);
//        addParamValues(h, "remLatestCreationDate", remLatestCreationDate);
//        addParamValues(h, "vmModUser", vmModUser);
//        addParamValues(h, "assUser", assUser);
//        addParamValues(h, "remFinished", remFinished);
//        addParamValues(h, "csdAdmCauseEn", csdAdmCauseEn);
//        addParamValues(h, "processTopic", processTopic);
//        addParamValues(h, "processResult", processResult);
//        addParamValues(h, "billCorrectionDeadline", billCorrectionDeadline);
//        addParamValues(h, "mdkAuditCompletionDeadline", mdkAuditCompletionDeadline);
//        addParamValues(h, "mdkDocumentDeliverDeadline", mdkDocumentDeliverDeadline);
//        addParamValues(h, "preliminaryProceedingsClosedDeadline", preliminaryProceedingsClosedDeadline);
//        addParamValues(h, "preliminaryProceedingAnswerDeadline", preliminaryProceedingAnswerDeadline);
//        addParamValues(h, "dataRecordCorrectionDeadline", dataRecordCorrectionDeadline);
//        addParamValues(h, "mdkAuditCompletionDeadlineDays", mdkAuditCompletionDeadlineDays);
//        addParamValues(h, "mdkDocumentDeliverDeadlineDays", mdkDocumentDeliverDeadlineDays);
//        addParamValues(h, "preliminaryProceedingsClosedDeadlineDays", preliminaryProceedingsClosedDeadlineDays);
//        addParamValues(h, "preliminaryProceedingAnswerDeadlineDays", preliminaryProceedingAnswerDeadlineDays);
//        addParamValues(h, "dataRecordCorrectionDeadlineDays", dataRecordCorrectionDeadlineDays);
//        addParamValues(h, "billCorrectionDeadlineDays", billCorrectionDeadlineDays);
//        addParamValues(h, "requestType", requestType);
//        addParamValues(h, "dischargDepartment", dischargDepartment);
//        addParamValues(h, "dischargDepartment301", dischargDepartment301);
//        addParamValues(h, "dischargDepartmentName", dischargDepartmentName);
//        addParamValues(h, "wvComment", wvComment);
//        addParamValues(h, "actionComment", actionComment);
//        addParamValues(h, "wvPrio", wvPrio);
//        addParamValues(h, "cwInitial", cwInitial);
//        addParamValues(h, "cwDiff", cwDiff);
//        addParamValues(h, "cwFinal", cwFinal);
//        addParamValues(h, "drgInitial", drgInitial);
//        addParamValues(h, "drgFinal", drgFinal);
//        addParamValues(h, "supFeeInitial", supFeeInitial);
//        addParamValues(h, "supFeeDiff", supFeeDiff);
//        addParamValues(h, "supFeeFinal", supFeeFinal);
//        addParamValues(h, "losInitial", losInitial);
//        addParamValues(h, "losDiff", losDiff);
//        addParamValues(h, "losFinal", losFinal);
//        addParamValues(h, "caseType", caseType);
//        addParamValues(h, "revenueDiff", revenueDiff);
//        addParamValues(h, "closingDate", closingDate);
//        addParamValues(h, "mdkState", mdkState);
//        addParamValues(h, "mdkSubseqProcDate", mdkSubseqProcDate);
//        addParamValues(h, "isCancel", isCancel);
//        addParamValues(h, "courtFileNumber", courtFileNumber);
//        addParamValues(h, "lawerFileNumber", lawerFileNumber);
//
//        addParamValues(h, "patDateOfBirth", getPatDateOfBirth());
//        addParamValues(h, "patName", getPatName());
//        addParamValues(h, "lock", getLock());
//        addParamValues(h, "rowNum", getRowNum());
//
//        return h;
//    }

    public String getAuditNames() {
        return auditNames;
    }

    public void setAuditNames(String auditNames) {
        this.auditNames = auditNames;
    }

    public void setRevenueInit(Double ini) {
        revenueInit = ini;
    }

    public void setRevenueFinal(Double fin) {
        revenueFinal = fin;
    }

    public Double getRevenueInit() {
        return revenueInit;
    }

    public Double getRevenueFinal() {
        return revenueFinal;
    }

    public Date getReviewDeadline() {
        return reviewDeadline == null ? null : new Date(reviewDeadline.getTime());
    }

    public void setReviewDeadline(Date reviewDeadline) {
        this.reviewDeadline = reviewDeadline;
    }

    public Date getReviewRenewalDeadline() {
        return reviewRenewalDeadline == null ? null : new Date(reviewRenewalDeadline.getTime());
    }

    public void setReviewRenewalDeadline(Date reviewRenewalDeadline) {
        this.reviewRenewalDeadline = reviewRenewalDeadline;
    }

    public Date getReviewInsReplyDeadline() {
        return reviewInsReplyDeadline == null ? null : new Date(reviewInsReplyDeadline.getTime());
    }

    public void setReviewInsReplyDeadline(Date reviewInsReplyDeadline) {
        this.reviewInsReplyDeadline = reviewInsReplyDeadline;
    }

    public Date getReviewReplySendDocDeadline() {
        return reviewReplySendDocDeadline == null ? null : new Date(reviewReplySendDocDeadline.getTime());
    }

    public void setReviewReplySendDocDeadline(Date reviewReplySendDocDeadline) {
        this.reviewReplySendDocDeadline = reviewReplySendDocDeadline;
    }

    public Date getReviewCompletionDeadline() {
        return reviewCompletionDeadline == null ? null : new Date(reviewCompletionDeadline.getTime());
    }

    public void setReviewCompletionDeadline(Date reviewCompletionDeadline) {
        this.reviewCompletionDeadline = reviewCompletionDeadline;
    }

    public Integer getReviewDeadlineDays() {
        return reviewDeadlineDays;
    }

    public void setReviewDeadlineDays(Integer reviewDeadlineDays) {
        this.reviewDeadlineDays = reviewDeadlineDays;
    }

    public Integer getReviewRenewalDeadlineDays() {
        return reviewRenewalDeadlineDays;
    }

    public void setReviewRenewalDeadlineDays(Integer reviewRenewalDeadlineDays) {
        this.reviewRenewalDeadlineDays = reviewRenewalDeadlineDays;
    }

    public Integer getReviewInsReplyDeadlineDays() {
        return reviewInsReplyDeadlineDays;
    }

    public void setReviewInsReplyDeadlineDays(Integer reviewInsReplyDeadlineDays) {
        this.reviewInsReplyDeadlineDays = reviewInsReplyDeadlineDays;
    }

    public Integer getReviewReplySendDocDeadlineDays() {
        return reviewReplySendDocDeadlineDays;
    }

    public void setReviewReplySendDocDeadlineDays(Integer reviewReplySendDocDeadlineDays) {
        this.reviewReplySendDocDeadlineDays = reviewReplySendDocDeadlineDays;
    }

    public Integer getReviewCompletionDeadlineDays() {
        return reviewCompletionDeadlineDays;
    }

    public void setReviewCompletionDeadlineDays(Integer reviewCompletionDeadlineDays) {
        this.reviewCompletionDeadlineDays = reviewCompletionDeadlineDays;
    }

    /**
     *
     * @return penaltyFee
     */
    public Double getPenaltyFee() {
        return penaltyFee;
    }

    /**
     *
     * @param resultDelta the resultDelta to set
     */
    public void setPenaltyFee(Double penaltyFee) {
        this.penaltyFee = penaltyFee;
    }

    public String getKainKeyEn() {
        return kainKeyEn;
    }

    public void setKainKeyEn(String kainKey) {
        this.kainKeyEn = kainKey;
    }

    public String getInkaKeyEn() {
        return inkaKeyEn;
    }

    public void setInkaKeyEn(String inkaKey) {
        this.inkaKeyEn = inkaKey;
    }

    public Date getMdHosStartAudit() {
        return mdHosStartAudit  == null ? null : new Date(mdHosStartAudit.getTime());
    }

    public void setMdHosStartAudit(Date mdHosStartAudit) {
        this.mdHosStartAudit = mdHosStartAudit;
    }

    public Date getDateOfBenefitLawDecision() {
        return dateOfBenefitLawDecision  == null ? null : new Date(dateOfBenefitLawDecision.getTime());
    }

    public void setDateOfBenefitLawDecision(Date dateOfBenefitLawDecision) {
        this.dateOfBenefitLawDecision = dateOfBenefitLawDecision;
    }
    

}
