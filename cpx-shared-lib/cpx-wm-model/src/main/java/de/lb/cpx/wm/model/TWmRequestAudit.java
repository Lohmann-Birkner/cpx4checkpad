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
package de.lb.cpx.wm.model;

//import static de.lb.cpx.wm.model.TWmRequestMdk.REASON_DELIMITER;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author nandola
 */
@Entity
@DiscriminatorValue(value = "3") //enum value in WmRequestType
@Table(name = "T_WM_REQUEST_AUDIT")
public class TWmRequestAudit extends TWmRequest {

    private static final long serialVersionUID = 1L;

    //final public static String REASON_DELIMITER = ",";
//    private Long insuInternalId;
    private String editor;
    private String auditComment;
    private String userComment;
//    private String auditReasons;
//    private Long state;
    private Long pre_trial_State;
//    private Date ensuranceStartAudit;//ENSURANCE_START_AUDIT
    private Date dataRecordCorrectionDeadline;
    private Date answerDeadline;
    private Date preliminaryProceedingsClosedDeadline;
    private Date caseDialogBillCorrDeadline;
    private boolean preTrialEndFl = false;
    private Date preTrialEnd;
    private boolean createReminderFl = false;
    private boolean sentOnFl = false;
    private Date sentOn;
    private boolean caseDialogFl = false;
    private Date requestDate;
    private String insuranceIdentifier;
    private boolean caseDialogEndFl = false;
    private Date caseDialogEndDate;
    
    /**
     * @return the Insurance Id
     */
//    @Column(name = "INSURANCE_INTERNAL_ID", nullable = false)
//    public Long getInsuranceInternalId() {
//        return insuInternalId;
//    }
    /**
     * @param insuInternalId: set the Insurance Id
     */
//    public void setInsuranceInternalId(Long insuInternalId) {
//        this.insuInternalId = insuInternalId;
//    }
    /**
     * @return the Insurance identifier
     */
//    @Column(name = "INSURANCE_IDENTIFIER", nullable = false)
//    public String getInsuranceIdentifier() {
//        return insuIdentifier;
//    }
    /**
     * @param insuIdentifier: set the Insurance identifier
     */
//    public void setInsuranceInternalId(String insuIdentifier) {
//        this.insuIdentifier = insuIdentifier;
//    }
    /**
     * @return the Editor name
     */
    @Column(name = "EDITOR", length = 50)
    public String getEditor() {
        return editor;
    }

    /**
     * @param editor: the Editor to set
     */
    public void setEditor(String editor) {
        this.editor = editor;
    }

    /**
     * @return the auditComment
     */
    @Column(name = "AUDIT_COMMENT", length = 255)
    public String getAuditComment() {
        return auditComment;
    }

    /**
     * @param auditComment: the auditComment to set
     */
    public void setAuditComment(String auditComment) {
        this.auditComment = auditComment;
    }

    /**
     * @return the userComment
     */
    @Column(name = "USER_COMMENT", length = 255)
    public String getUserComment() {
        return userComment;
    }

    /**
     * @param userComment the userComment to set
     */
    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

//    /**
//     * @return the auditReasons
//     */
//    @Column(name = "AUDIT_REASONS", length = 255)
//    public String getAuditReasons() {
//        return auditReasons;
//    }
//
//    /**
//     * Returns a list of audit reason numbers Attention: There is a duplicate of
//     * this code in TWmRequestMdk!
//     *
//     * @return audit reason numbers
//     */
//    @Transient
//    public Integer[] getAuditReasonsSplitted() {
//        String[] separateReasonNumbersTmp = auditReasons == null ? new String[]{} : auditReasons.trim().split(REASON_DELIMITER);
//        List<Integer> separateReasonNumbers = new ArrayList<>();
//        for (String separateReasonNumber : separateReasonNumbersTmp) {
//            if (separateReasonNumber.trim().isEmpty()) {
//                LOG.log(Level.WARNING, "Audit reason number is empty");
//                continue;
//            }
//            int reasonNumber = 0;
//            try {
//                reasonNumber = Integer.parseInt(separateReasonNumber.trim());
//            } catch (NumberFormatException ex) {
//                LOG.log(Level.SEVERE, "This is not a valid Audit reason number: '" + separateReasonNumber + "'", ex);
//                continue;
//            }
//            if (reasonNumber == 0) {
//                LOG.log(Level.WARNING, "Audit reason number is equal to 0");
//                continue;
//            }
//            separateReasonNumbers.add(reasonNumber);
//        }
//
//        Integer[] arr = new Integer[separateReasonNumbers.size()];
//        separateReasonNumbers.toArray(arr);
//        return arr;
//    }
//
//    /**
//     * @param auditReasons: the auditReasons to set
//     */
//    public void setAuditReasons(String[] auditReasons) {
//        this.auditReasons = auditReasons == null ? null : String.join(REASON_DELIMITER, auditReasons);
//    }
//
//    /**
//     * @param auditReasons: the auditReasons to set
//     */
//    public void setAuditReasons(String auditReasons) {
//        this.auditReasons = auditReasons;
//    }
    /**
     * @return the insuranceIdentifier
     */
    @Column(name = "INSURANCE_IDENTIFIER", length = 20, nullable = false)
    public String getInsuranceIdentifier() {
        return insuranceIdentifier;
    }

    /**
     * @param insuranceIdentifier: IKZ für die Prüfanfrage/Vorverfahren Anfrage
     */
    public void setInsuranceIdentifier(String insuranceIdentifier) {
        this.insuranceIdentifier = insuranceIdentifier;
    }
//
//    /**
//     * @return the state
//     */
//    @Column(name = "STATE")
//    public Long getState() {
//        return state;
//    }
//
//    /**
//     * @param state: the state to set
//     */
//    public void setState(Long state) {
//        this.state = state;
//    }

    /**
     * @return the Pre-trial State
     */
    @Column(name = "PRE_TRIAL_STATE")
    public Long getPreTrialState() {
        return pre_trial_State;
    }

    /**
     * @param pre_trial_State: the pre_trial_State to set
     */
    public void setPreTrialState(Long pre_trial_State) {
        this.pre_trial_State = pre_trial_State;
    }

    /**
     * @return the dataRecordCorrectionDeadline
     */
    @Column(name = "DATA_RECORD_CORR_DEADLINE")
    @Temporal(TemporalType.DATE)
    public Date getDataRecordCorrectionDeadline() {
        return dataRecordCorrectionDeadline == null ? null : new Date(dataRecordCorrectionDeadline.getTime());
    }

    /**
     * @param dataRecordCorrectionDeadline: the dataRecordCorrectionDeadline to
     * set
     */
    public void setDataRecordCorrectionDeadline(Date dataRecordCorrectionDeadline) {
        this.dataRecordCorrectionDeadline = dataRecordCorrectionDeadline == null ? null : new Date(dataRecordCorrectionDeadline.getTime());
    }

    /**
     * @return the ensuranceStartAudit
//     */
//    @Column(name = "ENSURANCE_START_AUDIT")
//    @Temporal(TemporalType.DATE)
//    public Date getEnsuranceStartAudit() {
//        return ensuranceStartAudit == null ? null : new Date(ensuranceStartAudit.getTime());
//    }
//
//    /**
//     * @param ensuranceStartAudit: the ensuranceStartAudit to set
//     */
//    public void setEnsuranceStartAudit(Date ensuranceStartAudit) {
//        this.ensuranceStartAudit = ensuranceStartAudit == null ? null : new Date(ensuranceStartAudit.getTime());
//    }
//
    /**
     * @return the answerDeadline
     */
//    @Column(name = "ANSWER_DEADLINE")
    @Column(name = "PREL_PROC_ANSWER_DEADLINE")
    @Temporal(TemporalType.DATE)
    public Date getAnswerDeadline() {
        return answerDeadline == null ? null : new Date(answerDeadline.getTime());
    }

    /**
     * @param answerDeadline: the answerDeadline to set
     */
    public void setAnswerDeadline(Date answerDeadline) {
        this.answerDeadline = answerDeadline == null ? null : new Date(answerDeadline.getTime());
    }

    /**
     * @return the preliminaryProceedingsClosedDeadline
     */
//    @Column(name = "PREL_CLOSED_DEADLINE")
    @Column(name = "PREL_PROC_CLOSED_DEADLINE")
    @Temporal(TemporalType.DATE)
    public Date getPreliminaryProceedingsClosedDeadline() {
        return preliminaryProceedingsClosedDeadline == null ? null : new Date(preliminaryProceedingsClosedDeadline.getTime());
    }

    /**
     * @param preliminaryProceedingsClosedDeadline: the
     * preliminaryProceedingsClosedDeadline to set
     */
    public void setPreliminaryProceedingsClosedDeadline(Date preliminaryProceedingsClosedDeadline) {
        this.preliminaryProceedingsClosedDeadline = preliminaryProceedingsClosedDeadline == null ? null : new Date(preliminaryProceedingsClosedDeadline.getTime());
    }

    /**
     * @return the caseDialogBillCorrDeadline
     */
    @Column(name = "CASE_DIALOG_BILL_CORR_DEADLINE")
    @Temporal(TemporalType.DATE)
    public Date getCaseDialogBillCorrDeadline() {
        return caseDialogBillCorrDeadline == null ? null : new Date(caseDialogBillCorrDeadline.getTime());
    }

    /**
     * @param caseDialogBillCorrDeadline: the caseDialogBillCorrDeadline to set
     */
    public void setCaseDialogBillCorrDeadline(Date caseDialogBillCorrDeadline) {
        this.caseDialogBillCorrDeadline = caseDialogBillCorrDeadline == null ? null : new Date(caseDialogBillCorrDeadline.getTime());
    }

    /**
     * @return the PreTrialEndFl
     */
    @Column(name = "PRE_TRIAL_END_FL", nullable = false)
    public boolean isPreTrialEndFl() {
        return preTrialEndFl;
    }

    /**
     * @param PreTrialEndFl: the PreTrialEndFl to set
     */
    public void setPreTrialEndFl(boolean PreTrialEndFl) {
        this.preTrialEndFl = PreTrialEndFl;
    }

    /**
     * @return the preTrialEnd
     */
    @Column(name = "PRE_TRIAL_END")
    @Temporal(TemporalType.DATE)
    public Date getPreTrialEnd() {
        return preTrialEnd == null ? null : new Date(preTrialEnd.getTime());
    }

    /**
     * @param preTrialEnd: the preTrialEnd to set
     */
    public void setPreTrialEnd(Date preTrialEnd) {
        this.preTrialEnd = preTrialEnd == null ? null : new Date(preTrialEnd.getTime());
    }

    /**
     * @return the isCreateReminder
     */
    @Column(name = "CREATE_REMINDER_FL", nullable = false)
    public boolean isCreateReminderFl() {
        return createReminderFl;
    }

    /**
     * @param createReminderFl: the isCreateReminder to set
     */
    public void setCreateReminderFl(boolean createReminderFl) {
        this.createReminderFl = createReminderFl;
    }

    /**
     * @return the isSentOn
     */
    @Column(name = "SENT_ON_FL", nullable = false)
    public boolean isSentOnFl() {
        return sentOnFl;
    }

    /**
     * @param sentOnFl: the IsSentOn to set
     */
    public void setSentOnFl(boolean sentOnFl) {
        this.sentOnFl = sentOnFl;
    }

    /**
     * @return the sentOn
     */
    @Column(name = "SENT_ON")
    @Temporal(TemporalType.DATE)
    public Date getSentOn() {
        return sentOn == null ? null : new Date(sentOn.getTime());
    }

    /**
     * @param sentOn: the sentOn to set
     */
    public void setSentOn(Date sentOn) {
        this.sentOn = sentOn == null ? null : new Date(sentOn.getTime());
    }

    /**
     * @return the caseDialogFl
     */
    @Column(name = "CASE_DIALOG_FL", nullable = false)
    public boolean isCaseDialogFl() {
        return caseDialogFl;
    }

    /**
     * @param caseDialogFl: the caseDialogFl to set
     */
    public void setCaseDialogFl(boolean caseDialogFl) {
        this.caseDialogFl = caseDialogFl;
    }

    /**
     * @return the caseDialogEndFl
     */
    @Column(name = "CASE_DIALOG_END_FL", nullable = false)
    public boolean isCaseDialogEndFl() {
        return caseDialogEndFl;
    }

    /**
     * @param caseDialogEndFl: the caseDialogEndFl to set
     */
    public void setCaseDialogEndFl(boolean bCaseDialogEndFl) {
        this.caseDialogEndFl = bCaseDialogEndFl;
    }

    
    /**
     * @return the requestDate
     */
    @Column(name = "REQUEST_DATE")
    @Temporal(TemporalType.DATE)
    public Date getRequestDate() {
        return requestDate == null ? null : new Date(requestDate.getTime());
    }

    /**
     * @param requestDate: the sentOn to set
     */
    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate == null ? null : new Date(requestDate.getTime());
    }

    /**
     * @return the caseDialogEndDate
     */
    @Column(name = "CASE_DIALOG_END_DATE")
    @Temporal(TemporalType.DATE)
    public Date getCaseDialogEndDate() {
        return caseDialogEndDate == null ? null : new Date(caseDialogEndDate.getTime());
    }

    /**
     * @param caseDialogEndDate: the endDate to set
     */
    public void setCaseDialogEndDate(Date caseDialogEndDate) {
        this.caseDialogEndDate = caseDialogEndDate == null ? null : new Date(caseDialogEndDate.getTime());
    }

    @Override
    public boolean versionEquals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof TWmRequestAudit)) {
            return false;
        }
        if(!super.versionEquals(object)){
            return false;
        }
        final TWmRequestAudit other = (TWmRequestAudit) object;
        if (!Objects.equals(this.insuranceIdentifier, other.insuranceIdentifier)) {
            return false;
        }
        if (!Objects.equals(this.editor, other.editor)) {
            return false;
        }
        if (!Objects.equals(this.auditComment, other.auditComment)) {
            return false;
        }
        if (!Objects.equals(this.userComment, other.userComment)) {
            return false;
        }

//        if (!Objects.equals(this.auditReasons, other.auditReasons)) {
//            return false;
//        }
//        if (!Objects.equals(this.state, other.state)) {
//            return false;
//        }
        if (!Objects.equals(this.pre_trial_State, other.pre_trial_State)) {
            return false;
        }
        if (!Objects.equals(this.dataRecordCorrectionDeadline, other.dataRecordCorrectionDeadline)) {
            return false;
        }
        if (!Objects.equals(this.caseDialogBillCorrDeadline, other.caseDialogBillCorrDeadline)) {
            return false;
        }
        if (!Objects.equals(this.answerDeadline, other.answerDeadline)) {
            return false;
        }
        if (!Objects.equals(this.preliminaryProceedingsClosedDeadline, other.preliminaryProceedingsClosedDeadline)) {
            return false;
        }
        if (!Objects.equals(this.preTrialEndFl, other.preTrialEndFl)) {
            return false;
        }
        if (!Objects.equals(this.preTrialEnd, other.preTrialEnd)) {
            return false;
        }
        if (!Objects.equals(this.createReminderFl, other.createReminderFl)) {
            return false;
        }
        if (!Objects.equals(this.sentOnFl, other.sentOnFl)) {
            return false;
        }
        if (!Objects.equals(this.sentOn, other.sentOn)) {
            return false;
        }
        if (!Objects.equals(this.caseDialogFl, other.caseDialogFl)) {
            return false;
        }
        if (!Objects.equals(this.requestDate, other.requestDate)) {
            return false;
        }
        if (!Objects.equals(this.caseDialogEndFl, other.caseDialogEndFl)) {
            return false;
        }
        if (!Objects.equals(this.caseDialogEndDate, other.caseDialogEndDate)) {
            return false;
        }
        return true;
    }

}
