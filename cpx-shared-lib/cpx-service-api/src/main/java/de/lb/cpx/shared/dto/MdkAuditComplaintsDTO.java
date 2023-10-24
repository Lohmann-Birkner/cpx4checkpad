/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.shared.dto;

import java.io.Serializable;

/**
 *
 * @author niemeier
 */
public class MdkAuditComplaintsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String hospitalIdent;
    private final String insuranceIdent;
    private final Integer year;
    private final Integer quarter;
    private final long caseCount;
    private final long complaintCount;

    public MdkAuditComplaintsDTO(final String pHospitalIdent, final String pInsuranceIdent, final Integer pYear, final Integer pQuarter, final long pCaseCount, final long pComplaintCount) {
        this.hospitalIdent = pHospitalIdent;
        this.insuranceIdent = pInsuranceIdent;
        this.year = pYear;
        this.quarter = pQuarter;
        this.caseCount = pCaseCount;
        this.complaintCount = pComplaintCount;
    }

    @Override
    public String toString() {
        return "MdkAuditComplaintsDTO{" + "hospitalIdent=" + hospitalIdent + ", insuranceIdent=" + insuranceIdent + ", year=" + year + ", quarter=" + quarter + ", caseCount=" + caseCount + ", complaintCount=" + complaintCount + '}';
    }

    public long getCaseCount() {
        return caseCount;
    }

    public long getComplaintCount() {
        return complaintCount;
    }

    public String getHospitalIdent() {
        return hospitalIdent;
    }

    public String getInsuranceIdent() {
        return insuranceIdent;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getQuarter() {
        return quarter;
    }

}
