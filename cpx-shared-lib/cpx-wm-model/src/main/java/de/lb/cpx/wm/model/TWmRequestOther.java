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
 * Entity for request type other
 *
 * @author wilde
 */
@Entity
@DiscriminatorValue(value = "5") //enum value in WmRequestTypeEn
@Table(name = "T_WM_REQUEST_OTHER")
public class TWmRequestOther extends TWmRequest {

    private static final long serialVersionUID = 1L;

//    private Date startAudit;
//    private Date reportStart;
    private WmAuditTypeEn auditType;
    private String resultComment;
    private String editor;
    private String institutionName;
//    private Long requestState;
    private String requestName;
    private String institutionCity;
    private String institutionZipCode;
    private String institutionAddress;
    private String institutionAreaCode;
    private String institutionTelefon;
    private String institutionFax;

    public TWmRequestOther() {
        super();
        //setRequestEnumType(WmRequestTypeEn.other);
    }

    /**
     * @return name of the request "Anfrageart", in this request type it is an
     * userdefined textfield
     */
    @Column(name = "REQUEST_NAME", length = 50, nullable = false)
    public String getRequestName() {
        return requestName;
    }

    /**
     * @param pName name of the request
     */
    public void setRequestName(String pName) {
        requestName = pName;
    }

//    /**
//     * @return audit start date (Einleitung Prüfverfahren)
//     */
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
//
//    /**
//     * @param reportStart start date of the report (Gutachtendatum)
//     */
//    public void setReportStart(Date reportStart) {
//        this.reportStart = reportStart == null ? null : new Date(reportStart.getTime());
//    }

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
     * @return INSTITUTION name, specified target INSTITUTION where the request
     * is directed to
     */
    @Column(name = "INSTITUTION_NAME", length = 250, nullable = false)
    public String getInstitutionName() {
        return institutionName;
    }

    /**
     * @param institutionName INSTITUTION name
     */
    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    /**
     * @return INSTITUTION city, specified target INSTITUTION where the request
     * is directed to
     */
    @Column(name = "INSTITUTION_CITY", length = 250)
    public String getInstitutionCity() {
        return institutionCity;
    }

    /**
     * @param institutionCity institution city
     */
    public void setInstitutionCity(String institutionCity) {
        this.institutionCity = institutionCity;
    }

    /**
     * @return INSTITUTION zip code, specified target INSTITUTION where the
     * request is directed to
     */
    @Column(name = "INSTITUTION_ZIP_CODE", length = 10)
    public String getInstitutionZipCode() {
        return institutionZipCode;
    }

    /**
     * @param institutionZipCode institution zip code
     */
    public void setInstitutionZipCode(String institutionZipCode) {
        this.institutionZipCode = institutionZipCode;
    }

    /**
     * @return INSTITUTION address, specified target INSTITUTION where the
     * request is directed to
     */
    @Column(name = "INSTITUTION_ADDRESS", length = 250)
    public String getInstitutionAddress() {
        return institutionAddress;
    }

    /**
     * @param institutionAddress institution address
     */
    public void setInstitutionAddress(String institutionAddress) {
        this.institutionAddress = institutionAddress;
    }

    /**
     * @return INSTITUTION Area code(phone prefix), specified target INSTITUTION
     * where the request is directed to
     */
    @Column(name = "INSTITUTION_AREA_CODE", length = 50)
    public String getInstitutionAreaCode() {
        return institutionAreaCode;
    }

    /**
     * @param institutionAreaCode institution areaCode
     */
    public void setInstitutionAreaCode(String institutionAreaCode) {
        this.institutionAreaCode = institutionAreaCode;
    }

    /**
     * @return INSTITUTION telefon, specified target INSTITUTION where the
     * request is directed to
     */
    @Column(name = "INSTITUTION_TELEFON", length = 50)
    public String getInstitutionTelefon() {
        return institutionTelefon;
    }

    /**
     * @param institutionTelefon institution telefon number
     */
    public void setInstitutionTelefon(String institutionTelefon) {
        this.institutionTelefon = institutionTelefon;
    }

    /**
     * @return INSTITUTION fax, specified target INSTITUTION where the request
     * is directed to
     */
    @Column(name = "INSTITUTION_FAX", length = 50)
    public String getInstitutionFax() {
        return institutionFax;
    }

    /**
     * @param institutionFax institution fax
     */
    public void setInstitutionFax(String institutionFax) {
        this.institutionFax = institutionFax;
    }
}
