/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.dto;

import java.util.Date;

/**
 *
 * @author niemeier
 */
public class QuotaListItemDTO extends WorkingListItemDTO {

    private static final long serialVersionUID = 1L;

    private Date csBillingDate;
    private Integer csBillingQuarter;
    private Integer riskPercentTotal;
    private Double riskValueTotal;
    private String riskPlaceOfReg;
    private Long workflowNumber; //Vorgangnsr.
    private Integer wmState; //
    private Long requestState;
    private Long processTopic;
    private Long processResult;
    private Date insuranceRecivedBillDate;
    private Date startAudit;
    private Date mdkStartAuditExtendedDate;
    private Integer mdkStartAuditQuarter;
    private Date reportDate;
    private Integer mdkReportCreationQuarter;
    private Long mdk;
    private Long requestId;
    private Long processId;

    @Override
    public String getCsCaseNumber() {
        return csCaseNumber;
    }

    @Override
    public void setCsCaseNumber(String csCaseNumber) {
        this.csCaseNumber = csCaseNumber;
    }

    @Override
    public String getCsHospitalIdent() {
        return csHospitalIdent;
    }

    @Override
    public void setCsHospitalIdent(String csHospitalIdent) {
        this.csHospitalIdent = csHospitalIdent;
    }

    @Override
    public String getCsdAdmReason12En() {
        return csdAdmReason12En;
    }

    @Override
    public void setCsdAdmReason12En(String csdAdmReason12En) {
        this.csdAdmReason12En = csdAdmReason12En;
    }

    @Override
    public Date getCsdAdmissionDate() {
        return csdAdmissionDate == null ? null : new Date(csdAdmissionDate.getTime());
    }

    @Override
    public void setCsdAdmissionDate(Date csdAdmissionDate) {
        this.csdAdmissionDate = csdAdmissionDate == null ? null : new Date(csdAdmissionDate.getTime());
    }

    @Override
    public Date getCsdDischargeDate() {
        return csdDischargeDate == null ? null : new Date(csdDischargeDate.getTime());
    }

    @Override
    public void setCsdDischargeDate(Date csdDischargeDate) {
        this.csdDischargeDate = csdDischargeDate == null ? null : new Date(csdDischargeDate.getTime());
    }

    @Override
    public Date getCsBillingDate() {
        return csBillingDate == null ? null : new Date(csBillingDate.getTime());
    }

    @Override
    public void setCsBillingDate(Date csBillingDate) {
        this.csBillingDate = csBillingDate == null ? null : new Date(csBillingDate.getTime());
    }

    public Integer getCsBillingQuarter() {
        return csBillingQuarter;
    }

    public void setCsBillingQuarter(Integer csBillingQuarter) {
        this.csBillingQuarter = csBillingQuarter;
    }

    public Integer getRiskPercentTotal() {
        return riskPercentTotal;
    }

    public void setRiskPercentTotal(Integer riskPercentTotal) {
        this.riskPercentTotal = riskPercentTotal;
    }

    public Double getRiskValueTotal() {
        return riskValueTotal;
    }

    public void setRiskValueTotal(Double riskValueTotal) {
        this.riskValueTotal = riskValueTotal;
    }

    public String getRiskPlaceOfReg() {
        return riskPlaceOfReg;
    }

    public void setRiskPlaceOfReg(String riskPlaceOfReg) {
        this.riskPlaceOfReg = riskPlaceOfReg;
    }

    public Long getWorkflowNumber() {
        return workflowNumber;
    }

    public void setWorkflowNumber(Long workflowNumber) {
        this.workflowNumber = workflowNumber;
    }

    public Integer getWmState() {
        return wmState;
    }

    public void setWmState(Integer wmState) {
        this.wmState = wmState;
    }

    public Long getRequestState() {
        return requestState;
    }

    public void setRequestState(Long mdkState) {
        this.requestState = mdkState;
    }

    public Long getProcessTopic() {
        return processTopic;
    }

    public void setProcessTopic(Long processTopic) {
        this.processTopic = processTopic;
    }

    public Long getProcessResult() {
        return processResult;
    }

    public void setProcessResult(Long processResult) {
        this.processResult = processResult;
    }

    public Date getInsuranceRecivedBillDate() {
        return insuranceRecivedBillDate == null ? null : new Date(insuranceRecivedBillDate.getTime());
    }

    public void setInsuranceRecivedBillDate(Date insuranceRecivedBillDate) {
        this.insuranceRecivedBillDate = insuranceRecivedBillDate == null ? null : new Date(insuranceRecivedBillDate.getTime());
    }

    public Date getStartAudit() {
        return startAudit;
    }

    public void setStartAudit(Date mdkStartAuditDate) {
        this.startAudit = mdkStartAuditDate == null ? null : new Date(mdkStartAuditDate.getTime());
    }

    public Date getMdkStartAuditExtendedDate() {
        return mdkStartAuditExtendedDate == null ? null : new Date(mdkStartAuditExtendedDate.getTime());
    }

    public void setMdkStartAuditExtendedDate(Date mdkStartAuditExtendedDate) {
        this.mdkStartAuditExtendedDate = mdkStartAuditExtendedDate == null ? null : new Date(mdkStartAuditExtendedDate.getTime());
    }

    public Integer getMdkStartAuditQuarter() {
        return mdkStartAuditQuarter;
    }

    public void setMdkStartAuditQuarter(Integer mdkStartAuditQuarter) {
        this.mdkStartAuditQuarter = mdkStartAuditQuarter;
    }

    public Date getReportDate() {
        return reportDate == null ? null : new Date(reportDate.getTime());
    }

    public void setReportDate(Date mdkReportCreationDate) {
        this.reportDate = mdkReportCreationDate == null ? null : new Date(mdkReportCreationDate.getTime());
    }

    public Integer getMdkReportCreationQuarter() {
        return mdkReportCreationQuarter;
    }

    public void setMdkReportCreationQuarter(Integer mdkReportCreationQuarter) {
        this.mdkReportCreationQuarter = mdkReportCreationQuarter;
    }

    public Long getMdk() {
        return mdk;
    }

    public void setMdk(Long mdk) {
        this.mdk = mdk;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

}
