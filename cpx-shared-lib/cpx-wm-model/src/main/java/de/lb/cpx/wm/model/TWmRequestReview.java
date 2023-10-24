/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.wm.model;

import de.lb.cpx.wm.model.enums.WmAuditTypeEn;
import java.util.Date;
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

/**
 * Erörterungsverfahren
 * @author gerschmann
 */
@Entity
@DiscriminatorValue(value = "6") //enum value in WmRequestType
@Table(name = "T_WM_REQUEST_REVIEW",
        indexes = {
            @Index(name = "IDX_WM_REQUEST_REVIEW4REMINDER_ID", columnList = "T_WM_REMINDER_ID", unique = false)})
public class TWmRequestReview extends TWmRequest{
    
private static final long serialVersionUID = 1L;

private TWmReminder reminder; //T_WM_REMINDER_ID

//insurance
private String insIdentifier;
private String insEditor;
//startAudit from TWmRequest is for "Einleitung Prüfverfahren Kasse

// md
private Long mdInternalId; //MD_INTERNAL_ID from C_MDK in CommonDB
private String mdEditor; //MD_EDITOR
private Date mdStartAudit; // einleitung Prüfverfahren MD

private Date reportReceiveDate; // Eingang Gutachten
private boolean reportFl = false;
private Date reviewDeadline; // EV frist bis
private boolean reviewDeadlineFl = false;
private Date renewalDeadline; // ev frist verlängerung
private boolean renewalDeadlineFl = false;
private Date reviewStart; // eingeleitet
private boolean reviewStartFl = false;
private Date insReplyDeadline;//Antwortfris KK
private Date insReplyDate;//KK geantwortet am
private boolean insReplyDateFl = false;
private Date replySendDocDeadline;// Nachsendefrist dokumente
private boolean replySendDocDeadlineFl = false;
private Date replySendDocDate; // dokumente nachgesendet am
private boolean replySendDocFl = false;
private Date completionDeadline;
private boolean completionDeadlineFl = false;
private Date completedDate;
private boolean completedFl = false;


    @ManyToOne
    @JoinColumn(name = "T_WM_REMINDER_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_WM_REQUEST_REVIEW4REMINDER_ID"))
    public TWmReminder getReminder() {
        return reminder;
    }

@Column(name = "INS_IDENTIFIER", nullable = true)
    public String getInsIdentifier() {
        return insIdentifier;
    }

    @Column(name = "INS_EDITOR", nullable = true, length = 50)
    public String getInsEditor() {
        return insEditor;
    }


    @Column(name = "MD_INTERNAL_ID", nullable = true)
    public Long getMdInternalId() {
        return mdInternalId;
    }

    @Column(name = "MD_EDITOR", nullable = true, length = 50)
    public String getMdEditor() {
        return mdEditor;
    }

    @Column(name = "MD_START_AUDIT")
    @Temporal(TemporalType.DATE)
    public Date getMdStartAudit() {
        return mdStartAudit;
    }

    @Column(name = "REPORT_FL", nullable = false)
    public boolean isReportFl() {
        return reportFl;
    }

    @Column(name = "REPORT_RECEIVE_DATE")
    @Temporal(TemporalType.DATE)
    public Date getReportReceiveDate() {
        return reportReceiveDate;
    }

    @Column(name = "REVIEW_DEADLINE")
    @Temporal(TemporalType.DATE)
    public Date getReviewDeadline() {
        return reviewDeadline;
    }

    @Column(name = "REVIEW_DEADLINE_FL", nullable = false)
    public boolean isReviewDeadlineFl() {
        return reviewDeadlineFl;
    }

    @Column(name = "RENEWAL_DEADLINE")
    @Temporal(TemporalType.DATE)
    public Date getRenewalDeadline() {
        return renewalDeadline;
    }

    @Column(name = "RENEWAL_DEADLINE_FL", nullable = false)
    public boolean isRenewalDeadlineFl() {
        return renewalDeadlineFl;
    }

    @Column(name = "REVIEW_START")
    @Temporal(TemporalType.DATE)
    public Date getReviewStart() {
        return reviewStart;
    }

    @Column(name = "REVIEW_START_FL", nullable = false)
    public boolean isReviewStartFl() {
        return reviewStartFl;
    }

    @Column(name = "INS_REPLY_DEADLINE")
    @Temporal(TemporalType.DATE)
    public Date getInsReplyDeadline() {
        return insReplyDeadline;
    }

    @Column(name = "INS_REPLY_DATE")
    @Temporal(TemporalType.DATE)
    public Date getInsReplyDate() {
        return insReplyDate;
    }

    @Column(name = "INS_REPLY_DATE_FL", nullable = false)
     public boolean isInsReplyDateFl() {
        return insReplyDateFl;
    }

    @Column(name = "REPLY_SEND_DOC_DEADLINE")
    @Temporal(TemporalType.DATE)
    public Date getReplySendDocDeadline() {
        return replySendDocDeadline;
    }

    @Column(name = "REPLY_SEND_DOC_DEADLINE_FL", nullable = false)
    public boolean isReplySendDocDeadlineFl() {
        return replySendDocDeadlineFl;
    }

    @Column(name = "REPLY_SEND_DOC_DATE")
    @Temporal(TemporalType.DATE)
    public Date getReplySendDocDate() {
        return replySendDocDate;
    }

    @Column(name = "REPLY_SEND_DOC_FL", nullable = false)
    public boolean isReplySendDocFl() {
        return replySendDocFl;
    }

    @Column(name = "COMPLETION_DEADLINE")
    @Temporal(TemporalType.DATE)
    public Date getCompletionDeadline() {
        return completionDeadline;
    }

    @Column(name = "COMPLETION_DEADLINE_FL", nullable = false)
    public boolean isCompletionDeadlineFl() {
        return completionDeadlineFl;
    }

    @Column(name = "COMPLETED_DATE")
    @Temporal(TemporalType.DATE)
    public Date getCompletedDate() {
        return completedDate;
    }

    @Column(name = "COMPLETED_FL", nullable = false)
    public boolean isCompletedFl() {
        return completedFl;
    }

    /**
     * @param reminder the reminder to set
     */
    public void setReminder(TWmReminder reminder) {
        this.reminder = reminder;
    }

    public void setInsIdentifier(String insIdentifier) {
        this.insIdentifier = insIdentifier;
    }

    public void setInsEditor(String insEditor) {
        this.insEditor = insEditor;
    }

    public void setMdInternalId(Long mdInternalId) {
        this.mdInternalId = mdInternalId;
    }

    public void setMdEditor(String mdEditor) {
        this.mdEditor = mdEditor;
    }

    public void setMdStartAudit(Date mdStartAudit) {
        this.mdStartAudit = mdStartAudit;
    }

    public void setReportFl(boolean reportFl) {
        this.reportFl = reportFl;
    }

    public void setReportReceiveDate(Date reportReceiveDate) {
        this.reportReceiveDate = reportReceiveDate;
    }

    public void setReviewDeadline(Date reviewDeadline) {
        this.reviewDeadline = reviewDeadline;
    }

    public void setReviewDeadlineFl(boolean reviewDeadlineFl) {
        this.reviewDeadlineFl = reviewDeadlineFl;
    }

    public void setRenewalDeadline(Date renewalDeadline) {
        this.renewalDeadline = renewalDeadline;
    }

    public void setRenewalDeadlineFl(boolean renewalDeadlineFl) {
        this.renewalDeadlineFl = renewalDeadlineFl;
    }

    public void setReviewStart(Date reviewStart) {
        this.reviewStart = reviewStart;
    }

    public void setReviewStartFl(boolean reviewStartFl) {
        this.reviewStartFl = reviewStartFl;
    }

    public void setInsReplyDeadline(Date insReplyDeadline) {
        this.insReplyDeadline = insReplyDeadline;
    }

    public void setInsReplyDate(Date insReplyDate) {
        this.insReplyDate = insReplyDate;
    }

    public void setInsReplyDateFl(boolean insReplyDateFl) {
        this.insReplyDateFl = insReplyDateFl;
    }

    public void setReplySendDocDeadline(Date replySendDocDeadline) {
        this.replySendDocDeadline = replySendDocDeadline;
    }

    public void setReplySendDocDeadlineFl(boolean replySendDocDeadlineFl) {
        this.replySendDocDeadlineFl = replySendDocDeadlineFl;
    }

    public void setReplySendDocDate(Date replySendDoc) {
        this.replySendDocDate = replySendDoc;
    }

    public void setReplySendDocFl(boolean replySendDocFl) {
        this.replySendDocFl = replySendDocFl;
    }

    public void setCompletionDeadline(Date completionDeadline) {
        this.completionDeadline = completionDeadline;
    }

    public void setCompletionDeadlineFl(boolean completionDeadlineFl) {
        this.completionDeadlineFl = completionDeadlineFl;
    }

    public void setCompletedDate(Date completed) {
        this.completedDate = completed;
    }

    public void setCompletedFl(boolean completedFl) {
        this.completedFl = completedFl;
    }



}
