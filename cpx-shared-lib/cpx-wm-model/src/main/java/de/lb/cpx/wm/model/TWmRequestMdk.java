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
 *    2016  Husser - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.wm.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Husser
 */
@Entity
@DiscriminatorValue(value = "2") //enum value in WmRequestType
@Table(name = "T_WM_REQUEST_MDK",
        indexes = {
            @Index(name = "IDX_WM_REQUEST_MDK4REMINDER_ID", columnList = "T_WM_REMINDER_ID", unique = false)})
//@Entity
//@DiscriminatorValue("2") //enum value in WmRequestType
//@Table(name = "T_WM_REQUEST")
public class TWmRequestMdk extends TWmRequest {

    private static final long serialVersionUID = 1L;

    public static final int QUOTA_NOT_RELEVANT = 0;
    public static final int QUOTA_NOT_EXCEEDED = 1;
    public static final int QUOTA_EXCEEDED = 2;

    //final public static String REASON_DELIMITER = ",";
    private TWmReminder reminder; //T_WM_REMINDER_ID
    private Long mdkInternalId; //MDK_INTERNAL_ID from C_MDK in CommonDB
    private String mdkEditor; //MDK_EDITOR
    private String mdkComment; //MDK_COMMENT
    private String userComment; //USER_COMMENT
//    private Date mdkReportCreationDate; //MDK_REPORT_CREATION_DATE
    private Date mdkReportReceiveDate; //MDK_REPORT_RECEIVE_DATE
    //private String mdkAuditReasons; //MDK_AUDIT_REASONS
    //private String mdkAuditReasonsExtended; //MDK_AUDIT_REASONS_EXTENDED
    private Date healthEnsuranceStartAudit; //HEALTH_ENSURANCE_START_AUDIT
    private Date mdkStartAudit; //MDK_START_AUDIT
    private Date mdkStartAuditExtended; //MDK_START_AUDIT_EXTENDED
    private Date billCorrectionDeadline; //BILL_CORRECTION_DEADLINE
    private Date mdkDocumentRequest; //MDK_DOCUMENT_REQUEST
    private Date mdkDocumentDeliverDeadline; //MDK_DOCUMENT_DELIVER_DEADLINE Dokumenten liefern
    private Date mdkDocumentDelivered; //MDK_DOCUMENT_DELIVERED
    private Date mdkAuditCompletionDeadline; //MDK_AUDIT_COMPLETION_DEADLINE
    private Date mdkContinuationFeePaidDeadline;//CONTINUATION_FEE_PAID_DATE
    private Date mdkSubProceedingDeadline;//SUBSEQUENT_PROCEEDING_DATE nachverfahren
    private boolean mdkSubProceedingDeadlineFl = false;//SUBSEQUENT_PROCEEDING_FL
    private boolean mdkDocumentRequestFl = false; //IS_MDK_DOCUMENT_REQUEST
    private boolean mdkDocumentDeliveredFl = false; //IS_MDK_DOCUMENT_DELIVERED
    private boolean mdkReportFl = false;
    private boolean reminderDeliverDeadlineFl = false; //IS_REMINDER_DELIVER_DEADLINE
    private boolean continuationFeePaidFl = false; //CONTINUATION_FEE_PAID
//    private Long mdkState; //MDK_STATE
    private Date insuranceRecivedBillDate; //INSURANCE_RECIVED_BILL
    private Integer mdkRequestQuotaExceededFl = QUOTA_NOT_RELEVANT; //MDK_REQUEST_QUOTA_EXCEEDED_FL
    private String mdkQuotaHospitalIdent; //MDK_QUOTA_HOSPITALIDENT
    private String mdkQuotaInsuranceIdent; //MDK_QUOTA_INSURANCEIDENT
    private Integer mdkQuotaQuarter; //MDK_QUOTA_QUARTER
    private Integer mdkQuotaYear; //MDK_QUOTA_YEAR
    private Long mdkQuotaInPatientCaseCount; //MDK_QUOTA_INPATIENT_CASECOUNT
    private Long mdkQuotaActComplInpatCase; //MDK_QUOTA_ACT_COMPL_INPAT_CASE
    private Long mdkMaxComplaintsForQuota = 0L; //MDK_MAX_COMPLAINTS_FOR_QUOTA
    private BigDecimal mdkGivenQuotaForQuarter; //MDK_GIVEN_QUOTA_FOR_QUARTER
//    private Date mdInsuranceStartAudit;

    /*
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_WM_REQUEST_MDK_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }
     */
    /**
     * @return the reminder
     */
    @ManyToOne
    @JoinColumn(name = "T_WM_REMINDER_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_WM_REQUEST_MDK4REMINDER_ID"))
    public TWmReminder getReminder() {
        return reminder;
    }

    /**
     * @param reminder the reminder to set
     */
    public void setReminder(TWmReminder reminder) {
        this.reminder = reminder;
    }

    /**
     * @return the mdkId
     */
    @Column(name = "MDK_INTERNAL_ID", nullable = true)
    public Long getMdkInternalId() {
        return mdkInternalId;
    }

    /**
     * @param mdkInternalId the mdkId to set
     */
    public void setMdkInternalId(Long mdkInternalId) {
        this.mdkInternalId = mdkInternalId;
    }

    /**
     * @return the mdkEditor
     */
    @Column(name = "MDK_EDITOR", length = 50)
    public String getMdkEditor() {
        return mdkEditor;
    }

    /**
     * @param mdkEditor the mdkEditor to set
     */
    public void setMdkEditor(String mdkEditor) {
        this.mdkEditor = mdkEditor;
    }

    /**
     * @return the mdkComment
     */
    @Column(name = "MDK_COMMENT", length = 255)
    public String getMdkComment() {
        return mdkComment;
    }

    /**
     * @param mdkComment the mdkComment to set
     */
    public void setMdkComment(String mdkComment) {
        this.mdkComment = mdkComment;
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
//     * @return the mdkReportCreationDate
//     */
//    @Column(name = "MDK_REPORT_CREATION_DATE")
//    @Temporal(TemporalType.DATE)
//    public Date getMdkReportCreationDate() {
//        return mdkReportCreationDate == null ? null : new Date(mdkReportCreationDate.getTime());
//    }
//
//    /**
//     * @param mdkReportCreationDate the mdkReportCreationDate to set
//     */
//    public void setMdkReportCreationDate(Date mdkReportCreationDate) {
//        this.mdkReportCreationDate = mdkReportCreationDate == null ? null : new Date(mdkReportCreationDate.getTime());
//    }

    /**
     * @return the mdkReportReceiveDate
     */
    @Column(name = "MDK_REPORT_RECEIVE_DATE")
    @Temporal(TemporalType.DATE)
    public Date getMdkReportReceiveDate() {
        return mdkReportReceiveDate == null ? null : new Date(mdkReportReceiveDate.getTime());
    }

    /**
     * @param mdkReportReceiveDate the mdkReportReceiveDate to set
     */
    public void setMdkReportReceiveDate(Date mdkReportReceiveDate) {
        this.mdkReportReceiveDate = mdkReportReceiveDate == null ? null : new Date(mdkReportReceiveDate.getTime());
    }

//    /**
//     * @return the mdkAuditReasons
//     */
//    @Column(name = "MDK_AUDIT_REASONS", length = 255)
//    public String getMdkAuditReasons() {
//        return mdkAuditReasons;
//    }
//    
//    /**
//     * Returns a list of audit reason numbers
//     * Attention: There is a duplicate of this code in TWmRequestAudit!
//     * @return audit reason numbers
//     */
//    @Transient
//    public Integer[] getAuditReasonsSplitted() {
//        String[] separateReasonNumbersTmp = mdkAuditReasons == null ? new String[]{} : mdkAuditReasons.trim().split(REASON_DELIMITER);
//        List<Integer> separateReasonNumbers = new ArrayList<>();
//        for (String separateReasonNumber : separateReasonNumbersTmp) {
//            if (separateReasonNumber.trim().isEmpty()) {
//                LOG.log(Level.WARNING, "MDK audit reason number is empty");
//                continue;
//            }
//            int reasonNumber = 0;
//            try {
//                reasonNumber = Integer.parseInt(separateReasonNumber.trim());
//            } catch (NumberFormatException ex) {
//                LOG.log(Level.SEVERE, "This is not a valid MDK audit reason number: '" + separateReasonNumber + "'", ex);
//                continue;
//            }
//            if (reasonNumber == 0) {
//                LOG.log(Level.WARNING, "MDK audit reason number is equal to 0");
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
//     * @param mdkAuditReasons the mdkAuditReasons to set
//     */
//    public void setMdkAuditReasons(String[] mdkAuditReasons) {
//        this.mdkAuditReasons = mdkAuditReasons == null ? null : String.join(REASON_DELIMITER, mdkAuditReasons);
//    }
//
//    /**
//     * @param mdkAuditReasons the mdkAuditReasons to set
//     */
//    public void setMdkAuditReasons(String mdkAuditReasons) {
//        this.mdkAuditReasons = mdkAuditReasons;
//    }
//
//    /**
//     * @return the mdkAuditReasonsExtended
//     */
//    @Column(name = "MDK_AUDIT_REASONS_EXTENDED", length = 255)
//    public String getMdkAuditReasonsExtended() {
//        return mdkAuditReasonsExtended;
//    }
//
//    /**
//     * @param mdkAuditReasonsExtended the mdkAuditReasonsExtended to set
//     */
//    public void setMdkAuditReasonsExtended(String[] mdkAuditReasonsExtended) {
//        this.mdkAuditReasonsExtended = mdkAuditReasonsExtended == null ? null : String.join(REASON_DELIMITER, mdkAuditReasonsExtended);
//    }
//
//    /**
//     * @param mdkAuditReasonsExtended the mdkAuditReasonsExtended to set
//     */
//    public void setMdkAuditReasonsExtended(String mdkAuditReasonsExtended) {
//        this.mdkAuditReasonsExtended = mdkAuditReasonsExtended;
//    }
    /**
     *
     * @return insuranceRecivedBillDate Eingang Rechnungsdatum Kasse
     */
    @Column(name = "INSURANCE_RECIVED_BILL")
    @Temporal(TemporalType.DATE)
    public Date getInsuranceRecivedBill() {
        return insuranceRecivedBillDate;
    }

    /**
     *
     * @param insuranceRecivedBillDate Eingang Rechnungsdatum Kasse
     */
    public void setInsuranceRecivedBill(Date insuranceRecivedBillDate) {
        this.insuranceRecivedBillDate = insuranceRecivedBillDate;
    }

    /**
     * @return the healthEnsuranceStartAudit
     */
    @Column(name = "HEALTH_ENSURANCE_START_AUDIT")
    @Temporal(TemporalType.DATE)
    public Date getHealthEnsuranceStartAudit() {
        return healthEnsuranceStartAudit;
    }

    /**
     * @param healthEnsuranceStartAudit the healthEnsuranceStartAudit to set
     */
    public void setHealthEnsuranceStartAudit(Date healthEnsuranceStartAudit) {
        this.healthEnsuranceStartAudit = healthEnsuranceStartAudit == null ? null : new Date(healthEnsuranceStartAudit.getTime());
    }

    /**
     * @return the mdkStartAudit
     */
    @Column(name = "MDK_START_AUDIT")
    @Temporal(TemporalType.DATE)
    public Date getMdkStartAudit() {
        return mdkStartAudit == null ? null : new Date(mdkStartAudit.getTime());
    }

    /**
     * @param mdkStartAudit the mdkStartAudit to set
     */
    public void setMdkStartAudit(Date mdkStartAudit) {
        this.mdkStartAudit = mdkStartAudit == null ? null : new Date(mdkStartAudit.getTime());
    }

    /**
     * @return the mdkStartAuditExtended
     */
    @Column(name = "MDK_START_AUDIT_EXTENDED")
    @Temporal(TemporalType.DATE)
    public Date getMdkStartAuditExtended() {
        return mdkStartAuditExtended == null ? null : new Date(mdkStartAuditExtended.getTime());
    }

    /**
     * @param mdkStartAuditExtended the mdkStartAuditExtended to set
     */
    public void setMdkStartAuditExtended(Date mdkStartAuditExtended) {
        this.mdkStartAuditExtended = mdkStartAuditExtended == null ? null : new Date(mdkStartAuditExtended.getTime());
    }

    /**
     * @return the billCorrectionDeadline
     */
    @Column(name = "BILL_CORRECTION_DEADLINE")
    @Temporal(TemporalType.DATE)
    public Date getBillCorrectionDeadline() {
        return billCorrectionDeadline == null ? null : new Date(billCorrectionDeadline.getTime());
    }

    /**
     * @param billCorrectionDeadline the billCorrectionDeadline to set
     */
    public void setBillCorrectionDeadline(Date billCorrectionDeadline) {
        this.billCorrectionDeadline = billCorrectionDeadline == null ? null : new Date(billCorrectionDeadline.getTime());
    }

    /**
     * @return the mdkDocumentRequest
     */
    @Column(name = "MDK_DOCUMENT_REQUEST")
    @Temporal(TemporalType.DATE)
    public Date getMdkDocumentRequest() {
        return mdkDocumentRequest == null ? null : new Date(mdkDocumentRequest.getTime());
    }

    /**
     * @param mdkDocumentRequest the mdkDocumentRequest to set
     */
    public void setMdkDocumentRequest(Date mdkDocumentRequest) {
        this.mdkDocumentRequest = mdkDocumentRequest == null ? null : new Date(mdkDocumentRequest.getTime());
    }

    /**
     * @return the mdkDocumentDeliverDeadline
     */
    @Column(name = "MDK_DOCUMENT_DELIVER_DEADLINE")
    @Temporal(TemporalType.DATE)
    public Date getMdkDocumentDeliverDeadline() {
        return mdkDocumentDeliverDeadline == null ? null : new Date(mdkDocumentDeliverDeadline.getTime());
    }

    /**
     * @param mdkDocumentDeliverDeadline the mdkDocumentDeliverDeadline to set
     */
    public void setMdkDocumentDeliverDeadline(Date mdkDocumentDeliverDeadline) {
        this.mdkDocumentDeliverDeadline = mdkDocumentDeliverDeadline == null ? null : new Date(mdkDocumentDeliverDeadline.getTime());
    }

    /**
     * @return the mdkDocumentDelivered
     */
    @Column(name = "MDK_DOCUMENT_DELIVERED")
    @Temporal(TemporalType.DATE)
    public Date getMdkDocumentDelivered() {
        return mdkDocumentDelivered == null ? null : new Date(mdkDocumentDelivered.getTime());
    }

    /**
     * @param mdkDocumentDelivered the mdkDocumentDelivered to set
     */
    public void setMdkDocumentDelivered(Date mdkDocumentDelivered) {
        this.mdkDocumentDelivered = mdkDocumentDelivered == null ? null : new Date(mdkDocumentDelivered.getTime());
    }

    /**
     * @return the mdkAuditCompletionDeadline
     */
    @Column(name = "MDK_AUDIT_COMPLETION_DEADLINE")
    @Temporal(TemporalType.DATE)
    public Date getMdkAuditCompletionDeadline() {
        return mdkAuditCompletionDeadline == null ? null : new Date(mdkAuditCompletionDeadline.getTime());
    }

    /**
     * @param mdkAuditCompletionDeadline the mdkAuditCompletionDeadline to set
     */
    public void setMdkAuditCompletionDeadline(Date mdkAuditCompletionDeadline) {
        this.mdkAuditCompletionDeadline = mdkAuditCompletionDeadline == null ? null : new Date(mdkAuditCompletionDeadline.getTime());
    }

    /**
     * @return the mdkContinuationFeePaidDeadline
     */
    @Column(name = "CONTINUATION_FEE_PAID_DATE")
    @Temporal(TemporalType.DATE)
    public Date getMdkContinuationFeePaidDeadline() {
        return mdkContinuationFeePaidDeadline == null ? null : new Date(mdkContinuationFeePaidDeadline.getTime());
    }

    /**
     * @param mdkContinuationFeePaidDeadline the mdkContinuationFeePaidDeadline
     * to set
     */
    public void setMdkContinuationFeePaidDeadline(Date mdkContinuationFeePaidDeadline) {
        this.mdkContinuationFeePaidDeadline = mdkContinuationFeePaidDeadline == null ? null : new Date(mdkContinuationFeePaidDeadline.getTime());
    }

    /**
     * @return the mdkSubProceedingDeadline
     */
    @Column(name = "SUBSEQUENT_PROCEEDING_DATE")
    @Temporal(TemporalType.DATE)
    public Date getMdkSubProceedingDeadline() {
        return mdkSubProceedingDeadline == null ? null : new Date(mdkSubProceedingDeadline.getTime());
    }

    /**
     * @param mdkSubProceedingDeadline the mdkSubProceedingDeadline to set
     */
    public void setMdkSubProceedingDeadline(Date mdkSubProceedingDeadline) {
        this.mdkSubProceedingDeadline = mdkSubProceedingDeadline == null ? null : new Date(mdkSubProceedingDeadline.getTime());
    }

    /**
     * @return the mdkSubProceedingDeadlineFl
     */
    @Column(name = "SUBSEQUENT_PROCEEDING_FL", nullable = false)
    public boolean isMdkSubProceedingDeadlineFl() {
        return mdkSubProceedingDeadlineFl;
    }

    /**
     * @param mdkSubProceedingDeadlineFl the mdkSubProceedingDeadlineFl to set
     */
    public void setMdkSubProceedingDeadlineFl(boolean mdkSubProceedingDeadlineFl) {
        this.mdkSubProceedingDeadlineFl = mdkSubProceedingDeadlineFl;
    }

    /**
     * @return the mdkDocumentRequestFl
     */
    @Column(name = "MDK_DOCUMENT_REQUEST_FL", nullable = false)
    public boolean isMdkDocumentRequestFl() {
        return mdkDocumentRequestFl;
    }

    /**
     * @param mdkDocumentRequestFl the mdkDocumentRequestFl to set
     */
    public void setMdkDocumentRequestFl(boolean mdkDocumentRequestFl) {
        this.mdkDocumentRequestFl = mdkDocumentRequestFl;
    }

    /**
     * @return the isMdkDocumentDelivered
     */
    @Column(name = "MDK_DOCUMENT_DELIVERED_FL", nullable = false)
    public boolean isMdkDocumentDeliveredFl() {
        return mdkDocumentDeliveredFl;
    }

    /**
     * @param mdkDocumentDeliveredFl the mdkDocumentDeliveredFl to set
     */
    public void setMdkDocumentDeliveredFl(boolean mdkDocumentDeliveredFl) {
        this.mdkDocumentDeliveredFl = mdkDocumentDeliveredFl;
    }

    /**
     *
     * @return isMdkReportFl
     */
    @Column(name = "MDK_REPORT_FL", nullable = false)
    public boolean isMdkReportFl() {
        return mdkReportFl;
    }

    /**
     * @param mdkReportFl the mdkReportFl to set
     */
    public void setMdkReportFl(boolean mdkReportFl) {
        this.mdkReportFl = mdkReportFl;
    }

    /**
     * @return the isReminderDeliverDeadline
     */
    @Column(name = "REMINDER_DELIVER_DEADLINE_FL", nullable = false)
    public boolean isReminderDeliverDeadlineFl() {
        return reminderDeliverDeadlineFl;
    }

    /**
     * @param reminderDeliverDeadlineFl the reminderDeliverDeadlineFl to set
     */
    public void setReminderDeliverDeadlineFl(boolean reminderDeliverDeadlineFl) {
        this.reminderDeliverDeadlineFl = reminderDeliverDeadlineFl;
    }

    /**
     * @return the isContinuationFeePaid
     */
    @Column(name = "CONTINUATION_FEE_PAID_FL", nullable = false)
    public boolean isContinuationFeePaidFl() {
        return continuationFeePaidFl;
    }

    /**
     * @param continuationFeePaidFl the continuationFeePaidFl to set
     */
    public void setContinuationFeePaidFl(boolean continuationFeePaidFl) {
        this.continuationFeePaidFl = continuationFeePaidFl;
    }

//    /**
//     * @return the mdkState
//     */
//    @Column(name = "MDK_STATE")
//    public Long getMdkState() {
//        return mdkState;
//    }
//
//    /**
//     * @param mdkState the mdkState to set
//     */
//    public void setMdkState(Long mdkState) {
//        this.mdkState = mdkState;
//    }
//
    /**
     * Fucking pay attention regarding this awkward pseudo-flag. It is not 0
     * (false) or 1 (true) like any other flag in this program, but 0 (not
     * relevant, whatever that means), 1 (false [sic!]) and 2 (true)
     *
     * @return the isMdkRequestQuotaExceeded
     */
    @Column(name = "MDK_REQUEST_QUOTA_EXCEEDED_FL", nullable = false)
    public int getMdkRequestQuotaExceededFl() {
        return mdkRequestQuotaExceededFl;
    }

    /**
     * @param mdkRequestQuotaExceededFl the mdkRequestQuotaExceededFl to set
     */
    public void setMdkRequestQuotaExceededFl(int mdkRequestQuotaExceededFl) {
        this.mdkRequestQuotaExceededFl = mdkRequestQuotaExceededFl;
    }

    /**
     * @return the isRequestQuotaExceeded
     */
    @Column(name = "MDK_QUOTA_HOSPITALIDENT", nullable = true)
    public String getMdkQuotaHospitalIdent() {
        return mdkQuotaHospitalIdent;
    }

    /**
     * @param mdkQuotaHospitalIdent the mdkQuotaHospitalIdent to set
     */
    public void setMdkQuotaHospitalIdent(String mdkQuotaHospitalIdent) {
        this.mdkQuotaHospitalIdent = mdkQuotaHospitalIdent;
    }

    /**
     * @return the isRequestQuotaExceeded
     */
    @Column(name = "MDK_QUOTA_INSURANCEIDENT", nullable = true)
    public String getMdkQuotaInsuranceIdent() {
        return mdkQuotaInsuranceIdent;
    }

    /**
     * @param mdkQuotaInsuranceIdent the mdkQuotaInsuranceIdent to set
     */
    public void setMdkQuotaInsuranceIdent(String mdkQuotaInsuranceIdent) {
        this.mdkQuotaInsuranceIdent = mdkQuotaInsuranceIdent;
    }

    /**
     * @return the mdkQuotaQuarter
     */
    @Column(name = "MDK_QUOTA_QUARTER", nullable = true)
    public Integer getMdkQuotaQuarter() {
        return mdkQuotaQuarter;
    }

    /**
     * @param mdkQuotaQuarter the mdkQuotaQuarter to set
     */
    public void setMdkQuotaQuarter(Integer mdkQuotaQuarter) {
        this.mdkQuotaQuarter = mdkQuotaQuarter;
    }

    /**
     * @return the mdkQuotaYear
     */
    @Column(name = "MDK_QUOTA_YEAR", nullable = true)
    public Integer getMdkQuotaYear() {
        return mdkQuotaYear;
    }

    /**
     * @param mdkQuotaYear the mdkQuotaYear to set
     */
    public void setMdkQuotaYear(Integer mdkQuotaYear) {
        this.mdkQuotaYear = mdkQuotaYear;
    }

    /**
     * @return the mdkQuotaInPatientCaseCount
     */
    @Column(name = "MDK_QUOTA_INPATIENT_CASECOUNT", nullable = true)
    public Long getMdkQuotaInPatientCaseCount() {
        return mdkQuotaInPatientCaseCount;
    }

    /**
     * @param mdkQuotaInPatientCaseCount the mdkQuotaInPatientCaseCount to set
     */
    public void setMdkQuotaInPatientCaseCount(Long mdkQuotaInPatientCaseCount) {
        this.mdkQuotaInPatientCaseCount = mdkQuotaInPatientCaseCount;
    }

    /**
     * @return the mdkQuotaActComplaintInPatCase
     */
    @Column(name = "MDK_QUOTA_ACT_COMPL_INPAT_CASE", nullable = true)
    public Long getMdkQuotaActComplInpatCase() {
        return mdkQuotaActComplInpatCase;
    }

    /**
     * @param mdkQuotaActComplInpatCase the mdkQuotaActComplInpatCase to set
     */
    public void setMdkQuotaActComplInpatCase(Long mdkQuotaActComplInpatCase) {
        this.mdkQuotaActComplInpatCase = mdkQuotaActComplInpatCase;
    }

    /**
     * @return the mdkMaxComplaintsForQuota
     */
    @Column(name = "MDK_MAX_COMPLAINTS_FOR_QUOTA", nullable = false)
    public Long getMdkMaxComplaintsForQuota() {
        return mdkMaxComplaintsForQuota;
    }

    /**
     * @param mdkMaxComplaintsForQuota the mdkMaxComplaintsForQuota to set
     */
    public void setMdkMaxComplaintsForQuota(Long mdkMaxComplaintsForQuota) {
        this.mdkMaxComplaintsForQuota = mdkMaxComplaintsForQuota;
    }

    /**
     * @return the mdkGivenQuotaForQuarter
     */
    @Column(name = "MDK_GIVEN_QUOTA_FOR_QUARTER", precision = 5, scale = 2, nullable = true)
    public BigDecimal getMdkGivenQuotaForQuarter() {
        return mdkGivenQuotaForQuarter;
    }

    /**
     * @param mdkGivenQuotaForQuarter the mdkGivenQuotaForQuarter to set
     */
    public void setMdkGivenQuotaForQuarter(BigDecimal mdkGivenQuotaForQuarter) {
        this.mdkGivenQuotaForQuarter = mdkGivenQuotaForQuarter;
    }

//    /**
//     * @return the ensuranceStartAudit
//     */
//    @Column(name = "MD_INSURANCE_START_AUDIT")
//    @Temporal(TemporalType.DATE)
//    public Date getMdInsuranceStartAudit() {
//        return mdInsuranceStartAudit == null ? null : new Date(mdInsuranceStartAudit.getTime());
//    }
//
//    /**
//     * @param ensuranceStartAudit: the ensuranceStartAudit to set
//     */
//    public void setInsuranceStartAudit(Date insuranceStartAudit) {
//        this.mdInsuranceStartAudit = insuranceStartAudit == null ? null : new Date(insuranceStartAudit.getTime());
//    }
//    

    @Transient
    public Integer getAuditYear() {
        Date dt = mdkStartAudit;
        if (dt == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return cal.get(Calendar.YEAR);
    }

    @Transient
    public Integer getCsBillingYearForMdAudit() {
        Date dt = mdkStartAudit;
        if (dt == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.add(Calendar.MONTH, -6);
        return cal.get(Calendar.YEAR);
    }

    
    @Transient
    public Integer getAuditQuarter() {
        Date dt = mdkStartAudit;
        if (dt == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return (cal.get(Calendar.MONTH) / 3) + 1;
    }

    @Transient
    public Integer getCsBillingQuarterForMdAudit() {
        Date dt = mdkStartAudit;
        if (dt == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.add(Calendar.MONTH, -6);
        return (cal.get(Calendar.MONTH) / 3) + 1;
    }



    //private Long MDK_STATE = null;
    @Override
    public boolean versionEquals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof TWmRequestMdk)) {
            return false;
        }
        if(!super.versionEquals(object)){
            return false;
        }
        final TWmRequestMdk other = (TWmRequestMdk) object;
        if (!Objects.equals(this.reminder, other.reminder)) {
            return false;
        }
        if (!Objects.equals(this.mdkInternalId, other.mdkInternalId)) {
            return false;
        }
        if (!Objects.equals(this.mdkEditor, other.mdkEditor)) {
            return false;
        }
        if (!Objects.equals(this.mdkComment, other.mdkComment)) {
            return false;
        }
        if (!Objects.equals(this.userComment, other.userComment)) {
            return false;
        }
//        if (!Objects.equals(this.mdkReportCreationDate, other.mdkReportCreationDate)) {
//            return false;
//        }
//        if (!Objects.equals(this.mdInsuranceStartAudit, other.mdInsuranceStartAudit)) {
//            return false;
//        }
        if (!Objects.equals(this.mdkReportReceiveDate, other.mdkReportReceiveDate)) {
            return false;
        }
//        if (!Objects.equals(this.mdkAuditReasons, other.mdkAuditReasons)) {
//            return false;
//        }
//        if (!Objects.equals(this.mdkAuditReasonsExtended, other.mdkAuditReasonsExtended)) {
//            return false;
//        }
        if (!Objects.equals(this.healthEnsuranceStartAudit, other.healthEnsuranceStartAudit)) {
            return false;
        }
        if (!Objects.equals(this.mdkStartAudit, other.mdkStartAudit)) {
            return false;
        }
        if (!Objects.equals(this.mdkStartAuditExtended, other.mdkStartAuditExtended)) {
            return false;
        }
        if (!Objects.equals(this.billCorrectionDeadline, other.billCorrectionDeadline)) {
            return false;
        }
        if (!Objects.equals(this.mdkDocumentRequest, other.mdkDocumentRequest)) {
            return false;
        }
        if (!Objects.equals(this.mdkDocumentDeliverDeadline, other.mdkDocumentDeliverDeadline)) {
            return false;
        }
        if (!Objects.equals(this.mdkDocumentDelivered, other.mdkDocumentDelivered)) {
            return false;
        }
        if (!Objects.equals(this.mdkDocumentRequestFl, other.mdkDocumentRequestFl)) {
            return false;
        }
        if (!Objects.equals(this.mdkDocumentDeliveredFl, other.mdkDocumentDeliveredFl)) {
            return false;
        }
        if (!Objects.equals(this.reminderDeliverDeadlineFl, other.reminderDeliverDeadlineFl)) {
            return false;
        }
//        if (!Objects.equals(this.mdkState, other.mdkState)) {
//            return false;
//        }
        if (!Objects.equals(this.mdkRequestQuotaExceededFl, other.mdkRequestQuotaExceededFl)) {
            return false;
        }
        if (!Objects.equals(this.mdkQuotaHospitalIdent, other.mdkQuotaHospitalIdent)) {
            return false;
        }
        if (!Objects.equals(this.mdkQuotaInsuranceIdent, other.mdkQuotaInsuranceIdent)) {
            return false;
        }
        if (!Objects.equals(this.mdkQuotaQuarter, other.mdkQuotaQuarter)) {
            return false;
        }
        if (!Objects.equals(this.mdkQuotaYear, other.mdkQuotaYear)) {
            return false;
        }
        if (!Objects.equals(this.mdkQuotaInPatientCaseCount, other.mdkQuotaInPatientCaseCount)) {
            return false;
        }
        if (!Objects.equals(this.mdkQuotaActComplInpatCase, other.mdkQuotaActComplInpatCase)) {
            return false;
        }
        if (!Objects.equals(this.mdkMaxComplaintsForQuota, other.mdkMaxComplaintsForQuota)) {
            return false;
        }
        if (!Objects.equals(this.mdkGivenQuotaForQuarter, other.mdkGivenQuotaForQuarter)) {
            return false;
        }
        return true;
    }

//    @Transient
//    public String getMdkQuotaHospitalIdentWithoutNULL() {
//        return getMdkQuotaHospitalIdent() == null ? "" : getMdkQuotaHospitalIdent();
//    }
//
//    @Transient
//    public String getMdkQuotaInsuranceIdentWithoutNULL() {
//        return getMdkQuotaInsuranceIdent() == null ? "" : getMdkQuotaInsuranceIdent();
//    }
//
//    @Transient
//    public Integer getMdkQuotaQuarterWithoutNULL() {
//        return getMdkQuotaQuarter() == null ? 0 : getMdkQuotaQuarter();
//    }
//
//    @Transient
//    public Integer getMdkQuotaYearWithoutNULL() {
//        return getMdkQuotaYear() == null ? 0 : getMdkQuotaYear();
//    }
//
//    @Transient
//    public Long getMdkQuotaInPatientCaseCountWithoutNULL() {
//        return getMdkQuotaInPatientCaseCount() == null ? 0 : getMdkQuotaInPatientCaseCount();
//    }
//
//    @Transient
//    public Long getMdkQuotaActComplInpatCaseWithoutNULL() {
//        return getMdkQuotaActComplInpatCase() == null ? 0 : getMdkQuotaActComplInpatCase();
//    }
//
//    @Transient
//    public Long getMdkMaxComplaintsForQuotaWithoutNULL() {
//        return getMdkMaxComplaintsForQuota() == null ? 0 : getMdkMaxComplaintsForQuota();
//    }
//
//    @Transient
//    public Double getMdkGivenQuotaForQuarterWithoutNULL() {
//        return getMdkGivenQuotaForQuarter() == null ? 0d : getMdkGivenQuotaForQuarter();
//    }
}
