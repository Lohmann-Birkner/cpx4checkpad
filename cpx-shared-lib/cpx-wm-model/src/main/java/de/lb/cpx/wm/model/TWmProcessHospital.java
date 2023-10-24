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
 *    2017  Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.wm.model;

import de.lb.cpx.model.util.ModelUtil;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

/**
 * Entity to manage process data for a hospital process
 *
 * @author Dirk Niemeier
 */
@Entity
@DiscriminatorValue(value = "HOSPITAL") //enum value in WmProcessType
@Table(name = "T_WM_PROCESS_HOSPITAL")
public class TWmProcessHospital extends TWmProcess {

    private static final long serialVersionUID = 1L;

    private TWmProcessHospitalFinalisation processHospitalFinalisation;
    private Set<TWmRequest> requests = new HashSet<>();
//    private Set<TWmRequestMdk> mdkRequests = new HashSet<>();

    private Long processTopic;
    private boolean isClosed = false;
    private Date mdkBillCorrectionDeadline;
    private Date mdkDocDeliverDeadline;
    private Date mdkAuditCompletionDeadline;
    private Date auditDataRecordCorrectionDeadline;
    private Date auditPrelProcAnsDeadline;
    private Date auditPrelProcClosedDeadline;
    private Date reviewDeadline; // EV frist bis
    private Date reviewRenewalDeadline; // ev frist verlängerung
    private Date reviewInsReplyDeadline;//Antwortfrist KK
    private Date reviewReplySendDocDeadline;// Nachsendefrist dokumente
    private Date reviewCompletionDeadline;



    @OneToMany(mappedBy = "processHospital", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
//    @Override
    public Set<TWmRequest> getRequests() {
        return requests;
    }

//    @OneToMany(mappedBy = "process", cascade = CascadeType.ALL)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    public Set<TWmRequestMdk> getMdkRequests() {
//        return mdkRequests;
//    }
//    @Override
    public void setRequests(Set<TWmRequest> requests) {
        this.requests = requests;
    }

//    public void setMdkRequests(Set<TWmRequestMdk> mdkRequests) {
//        this.mdkRequests = mdkRequests;
//    }
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "processHospital", cascade = CascadeType.ALL, orphanRemoval = true)
    public TWmProcessHospitalFinalisation getProcessHospitalFinalisation() {
        return this.processHospitalFinalisation;
    }

    public void setProcessHospitalFinalisation(final TWmProcessHospitalFinalisation processHospitalFinalisation) {
        this.processHospitalFinalisation = processHospitalFinalisation;
    }

    /**
     * @return the topic of the process, id of c_wm_list_process_topic
     */
    @Column(name = "PROCESS_TOPIC", scale = 0)
    public Long getProcessTopic() {
        return processTopic;
    }

    /**
     * @param processTopic sets new topic, should be id of
     * c_wm_list_process_topic
     */
    public void setProcessTopic(Long processTopic) {
        this.processTopic = processTopic;
    }

    /**
     * @return closed status of the process
     */
    @Column(name = "IS_CLOSED", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean isIsClosed() {
        return isClosed;
    }

    /**
     * @param isClosed set new close status of the process
     */
    public void setIsClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }

    //private String drgInitial;
    /**
     * Gibt DRG oder PEPP - Code zurück
     *
     * @return drgInitial
     */
//  @Column(name = "DRG_INITIAL", length = 10)
//  public String getDrgInitial() {
//    return this.drgInitial;
//  }
//  
//  public void setDrgInitial(final String pDrgInitial) {
//    this.drgInitial = pDrgInitial;
//  }
    /**
     * @return the mdkBillCorrectionDeadline
     */
    @Column(name = "MDK_BILL_CORRECTION_DEADLINE")
    @Temporal(TemporalType.DATE)
    public Date getMdkBillCorrectionDeadline() {
        return mdkBillCorrectionDeadline == null ? null : new Date(mdkBillCorrectionDeadline.getTime());
    }

    /**
     * @param mdkBillCorrectionDeadline the mdkBillCorrectionDeadline to set
     */
    public void setMdkBillCorrectionDeadline(Date mdkBillCorrectionDeadline) {
        this.mdkBillCorrectionDeadline = mdkBillCorrectionDeadline == null ? null : new Date(mdkBillCorrectionDeadline.getTime());
    }

    /**
     * @return the mdkDocDeliverDeadline
     */
    @Column(name = "MDK_DOC_DELIVER_DEADLINE")
    @Temporal(TemporalType.DATE)
    public Date getMdkDocDeliverDeadline() {
        return mdkDocDeliverDeadline == null ? null : new Date(mdkDocDeliverDeadline.getTime());
    }

    /**
     * @param mdkDocDeliverDeadline the mdkDocDeliverDeadline to set
     */
    public void setMdkDocDeliverDeadline(Date mdkDocDeliverDeadline) {
        this.mdkDocDeliverDeadline = mdkDocDeliverDeadline == null ? null : new Date(mdkDocDeliverDeadline.getTime());
    }

    /**
     * @return the mdkAuditCompletionDeadline
     */
    // Actually the final date is calculated based on the auditCompletion and continuationFee(optional) deadlines.
    @Column(name = "MDK_AUDIT_COMPLETION_DEADLINE")
    @Temporal(TemporalType.DATE)
    public Date getMdkAuditCompletionDeadline() {
        return mdkAuditCompletionDeadline == null ? null : new Date(mdkAuditCompletionDeadline.getTime());
    }

    @Column(name = "REVIEW_DEADLINE")
    @Temporal(TemporalType.DATE)
    public Date getReviewDeadline() {
        return reviewDeadline == null ? null : new Date(reviewDeadline.getTime());
    }

    @Column(name = "REVIEW_RENEWAL_DEADLINE")
    @Temporal(TemporalType.DATE)
    public Date getReviewRenewalDeadline() {
        return reviewRenewalDeadline == null ? null : new Date(reviewRenewalDeadline.getTime());
    }

    @Column(name = "REVIEW_INS_REPLY_DEADLINE")
    @Temporal(TemporalType.DATE)
    public Date getReviewInsReplyDeadline() {
        return reviewInsReplyDeadline == null ? null : new Date(reviewInsReplyDeadline.getTime());
    }

    @Column(name = "REVIEW_REPLY_SEND_DOC_DL")
    @Temporal(TemporalType.DATE)
    public Date getReviewReplySendDocDeadline() {
        return reviewReplySendDocDeadline == null ? null : new Date(reviewReplySendDocDeadline.getTime());
    }

    @Column(name = "REVIEW_COMPLETION_DEADLINE")
    @Temporal(TemporalType.DATE)
    public Date getReviewCompletionDeadline() {
        return reviewCompletionDeadline == null ? null : new Date(reviewCompletionDeadline.getTime());
    }

    public void setReviewDeadline(Date reviewDeadline) {
        this.reviewDeadline = reviewDeadline;
    }

    public void setReviewRenewalDeadline(Date reviewRenewalDeadline) {
        this.reviewRenewalDeadline = reviewRenewalDeadline;
    }

    public void setReviewInsReplyDeadline(Date reviewInsReplyDeadline) {
        this.reviewInsReplyDeadline = reviewInsReplyDeadline;
    }

    public void setReviewReplySendDocDeadline(Date reviewReplySendDocDeadline) {
        this.reviewReplySendDocDeadline = reviewReplySendDocDeadline;
    }

    public void setReviewCompletionDeadline(Date reviewCompletionDeadline) {
        this.reviewCompletionDeadline = reviewCompletionDeadline;
    }

    
    /**
     * @param mdkAuditCompletionDeadline the mdkAuditCompletionDeadline to set
     */
    public void setMdkAuditCompletionDeadline(Date mdkAuditCompletionDeadline) {
        this.mdkAuditCompletionDeadline = mdkAuditCompletionDeadline == null ? null : new Date(mdkAuditCompletionDeadline.getTime());
    }

    /**
     * @return the auditDataRecordCorrectionDeadline
     */
    @Column(name = "AUDIT_DATA_REC_CORR_DEADLINE")
    @Temporal(TemporalType.DATE)
    public Date getAuditDataRecordCorrectionDeadline() {
        return auditDataRecordCorrectionDeadline == null ? null : new Date(auditDataRecordCorrectionDeadline.getTime());
    }

    /**
     * @param auditDataRecordCorrectionDeadline: the
     * auditDataRecordCorrectionDeadline to set
     */
    public void setAuditDataRecordCorrectionDeadline(Date auditDataRecordCorrectionDeadline) {
        this.auditDataRecordCorrectionDeadline = auditDataRecordCorrectionDeadline == null ? null : new Date(auditDataRecordCorrectionDeadline.getTime());
    }

    /**
     * @return the auditPrelProcAnsDeadline
     */
    @Column(name = "AUDIT_PREL_PROC_ANS_DEADLINE")
    @Temporal(TemporalType.DATE)
    public Date getAuditPrelProcAnsDeadline() {
        return auditPrelProcAnsDeadline == null ? null : new Date(auditPrelProcAnsDeadline.getTime());
    }

    /**
     * @param auditPrelProcAnsDeadline: the auditPrelProcAnsDeadline to set
     */
    public void setAuditPrelProcAnsDeadline(Date auditPrelProcAnsDeadline) {
        this.auditPrelProcAnsDeadline = auditPrelProcAnsDeadline == null ? null : new Date(auditPrelProcAnsDeadline.getTime());
    }

    /**
     * @return the auditPrelProcClosedDeadline
     */
    @Column(name = "AUDIT_PREL_PROC_CL_DEADLINE")
    @Temporal(TemporalType.DATE)
    public Date getAuditPrelProcClosedDeadline() {
        return auditPrelProcClosedDeadline == null ? null : new Date(auditPrelProcClosedDeadline.getTime());
    }

    /**
     * @param auditPrelProcClosedDeadline: the auditPrelProcClosedDeadline to
     * set
     */
    public void setAuditPrelProcClosedDeadline(Date auditPrelProcClosedDeadline) {
        this.auditPrelProcClosedDeadline = auditPrelProcClosedDeadline == null ? null : new Date(auditPrelProcClosedDeadline.getTime());
    }

    @Transient
    public TWmRequest getLastRequest() {
        TWmRequest lastReq = null;
        Iterator<TWmRequest> it = requests.iterator();
        while (it.hasNext()) {
            TWmRequest nextReq = it.next();
            if (lastReq == null) {
                lastReq = nextReq;
            } else if (lastReq.getCreationDate().before(nextReq.getCreationDate())) {
                lastReq = nextReq;
            }
        }
        return lastReq;
    }

    @Override
    public boolean versionEquals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof TWmProcess)) {
            return false;
        }
        final TWmProcessHospital other = (TWmProcessHospital) object;
        boolean returnVal = super.versionEquals(other);
        if (returnVal) {
            if (!ModelUtil.versionSetEquals(this.requests, other.requests)) {
                return false;
            }
//            if (!ModelUtil.versionSetEquals(this.mdkRequests, other.mdkRequests)) {
//                return false;
//            }
        }
        return returnVal;
    }

}
