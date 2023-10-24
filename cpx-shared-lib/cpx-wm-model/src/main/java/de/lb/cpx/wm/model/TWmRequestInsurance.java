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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.wm.model;

import de.lb.cpx.wm.converter.AuditTypeConverter;
import de.lb.cpx.wm.model.enums.WmAuditTypeEn;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CPX-1211 New Requesttype for Insurances, handles private and public
 * insurances
 *
 * @author wilde
 */
@Entity
@DiscriminatorValue(value = "4") //enum value in WmRequestTypeEn
@Table(name = "T_WM_REQUEST_INSURANCE")
public class TWmRequestInsurance extends TWmRequest {

    private static final long serialVersionUID = 1L;

//    private Date startAudit;
//    private Date reportStart;
    private WmAuditTypeEn auditType;
    private String resultComment;
    private String editor;
    private String insuranceIdentifier;
//    private Long requestState;
    private boolean publicInsured;

    public TWmRequestInsurance() {
        super();
        //setRequestEnumType(WmRequestTypeEn.insurance);
    }

    /**
     * @return audit start date (Einleitung Prüfverfahren)
     */
//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "START_AUDIT", length = 7)
//    public Date getStartAudit() {
//        return startAudit == null ? null : new Date(startAudit.getTime());
//    }
//
//    /**
//     * @param startAudit audit start date (Einleitung Pruefverfahren)
//     */
//    public void setStartAudit(Date startAudit) {
//        this.startAudit = startAudit == null ? null : new Date(startAudit.getTime());
//    }
//
//    /**
//     * @return report start date (Gutachtendatum)
//     */
//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "REPORT_START", length = 7)
//    public Date getReportStart() {
//        return reportStart == null ? null : new Date(reportStart.getTime());
//    }

//    /**
//     * @param reportStart start date of the report (Gutachtendatum)
//     */
//    public void setReportStart(Date reportStart) {
//        this.reportStart = reportStart == null ? null : new Date(reportStart.getTime());
//    }
//
    /**
     * @see WmAuditTypeEn
     * @return audit type
     */
    @Column(name = "TYPE_OF_AUDIT_EN", length = 10, nullable = false)
    @Convert(converter = AuditTypeConverter.class)
    public WmAuditTypeEn getAuditType() {
        return auditType;
    }

    /**
     * @param auditType new audit type (single or collection)
     */
    public void setAuditType(WmAuditTypeEn auditType) {
        this.auditType = auditType;
    }

    /**
     * @return get result comment of the request
     */
    @Column(name = "RESULT_COMMENT", length = 256)
    public String getResultComment() {
        return resultComment;
    }

    /**
     * @param resultComment comment to the result of the request, should not
     * exceed 255 chars
     */
    public void setResultComment(String resultComment) {
        this.resultComment = resultComment;
    }

    /**
     * @return identifier if the insurance is the handled as privte or public
     */
    @Column(name = "PUBLIC_INSURED_FL", nullable = false)
    public boolean isPublicInsured() {
        return publicInsured;
    }

    /**
     * @param publicInsured changes insurance type
     */
    public void setPublicInsured(boolean publicInsured) {
        this.publicInsured = publicInsured;
    }

    /**
     * @return recipient(insurance-employee) of the request on insurance side
     */
    @Column(name = "EDITOR", length = 50)
    public String getEditor() {
        return editor;
    }

    /**
     * @param editor recipent of the request
     */
    public void setEditor(String editor) {
        this.editor = editor;
    }

//    /**
//     * @return state of the request, should be moved to twmrequest!?!
//     */
//    @Column(name = "REQUEST_STATE")
//    public Long getRequestState() {
//        return requestState;
//    }
//
//    /**
//     * @param state state as long: 0 = open, 1 = yes, 2 = no
//     */
//    public void setRequestState(Long state) {
//        this.requestState = state;
//    }

    /**
     * @return insurance ident, unique number of the insurance for catalog data
     */
    @Column(name = "INSURANCE_IDENTIFIER", length = 20, nullable = false)
    public String getInsuranceIdentifier() {
        return insuranceIdentifier;
    }

    /**
     * @param insuranceIdentifier unique insurance number
     */
    public void setInsuranceIdentifier(String insuranceIdentifier) {
        this.insuranceIdentifier = insuranceIdentifier;
    }

}
