/* 
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.wm.model;

import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.enums.PlaceOfRegEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

/**
 * TCase initially generated at 21.01.2016 17:07:59 by Hibernate Tools 3.2.2.GA
 * <p style="font-size:1em; color:green;"> Die Tabelle "T_CASE" speichert alle
 * Krankenhausfälle, die in die Datenbank eingelesen wurden. </p>
 */
@Entity
@Table(name = "T_WM_RISK", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"T_CASE_DETAILS_ID", "T_WM_REQUEST_ID", "T_WM_PROCESS_HOSPITAL_FINAL_ID", "RISK_PLACE_OF_REG"}, name = "Uni_CaseDetailsId_Risk")
})
@SuppressWarnings("serial")
//@NamedEntityGraph(
//        name = "fetchBatchCaseGraph",
//        attributeNodes = {
//            @NamedAttributeNode(value = "caseDetails", subgraph = "detailsGraph"),
////            @NamedAttributeNode(value = "caseOpsGroupeds", subgraph = "opsGraph"),
////            @NamedAttributeNode(value = "checkResults"),
////            @NamedAttributeNode(value = "caseIcd")
//        },
//        subgraphs = {
//            @NamedSubgraph(name = "detailsGraph",attributeNodes = {
//                        @NamedAttributeNode(value = "caseDepartments"
////            , subgraph = "opsIcdGraph")
////                    }),
////            @NamedSubgraph(name = "opsIcdGraph",attributeNodes = {
////                        @NamedAttributeNode("caseOpses"),
////                        @NamedAttributeNode("caseIcds"
//                    
//                        )})
//        }
//    )
public class TWmRisk extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    private TCaseDetails caseDetails;
    private TWmRequest request;
    private TWmProcessHospitalFinalisation processHospitalFinalisation;
    private PlaceOfRegEn riskPlaceOfReg;
    private Set<TWmRiskDetails> riskDetails = new HashSet<>(0);
    private int riskPercentTotal;
    private BigDecimal riskValueTotal;
    private Integer riskCalcPercentTotal;
    private BigDecimal riskCalcValueTotal;
    private String riskComment;
    private boolean riskActual4Req = false;
//[oracle]::ALTER TABLE T_WM_RISK ADD RISK_AUDIT_PERCENT NUMBER(10, 0) DEFAULT 0 NOT NULL;
    private Integer riskAuditPercent;
    private Integer riskAuditPercentSugg;
    private Integer riskWastePercent;
    private Integer riskWastePercentSugg;
    private String riskSourceSugg;
    private BigDecimal riskBaseFee;
    private BigDecimal riskNotCalculatedFee;
//[oracle]::ALTER TABLE T_WM_RISK ADD RISK_AUDIT_PERCENT_SUGG NUMBER(10, 0) DEFAULT 0 NOT NULL;

//[oracle]::ALTER TABLE T_WM_RISK ADD RISK_WASTE_PERCENT NUMBER(10, 0) DEFAULT 0 NOT NULL;

//[oracle]::ALTER TABLE T_WM_RISK ADD RISK_WASTE_PERCENT_SUGG NUMBER(10, 0) DEFAULT 0 NOT NULL;

//[oracle]::ALTER TABLE T_WM_RISK ADD RISK_SOURCE_SUGG VARCHAR2(255 CHAR)  NULL;

    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_WM_RISK_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    /**
     *
     * @param hospitalCase Column T_CASE_ID:Verweis auf die ID der Tabelle
     * T_CASE.
     */
    public void setCaseDetails(final TCaseDetails hospitalCase) {
        this.caseDetails = hospitalCase;
    }

    /**
     * Gibt Verweis auf die ID der Tabelle T_CASE zurück
     *
     * @return hospitalCase
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_CASE_DETAILS_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_RISK4T_CASE_DETAILS_ID"))
    public TCaseDetails getCaseDetails() {
        return this.caseDetails;
    }

    /**
     *
     * @param request Column T_WM_REQUEST_ID:Verweis auf die ID der Tabelle
     * T_WM_REQUEST.
     */
    public void setRequest(final TWmRequest request) {
        this.request = request;
    }

    /**
     * Gibt Verweis auf die ID der Tabelle T_WM_REQUEST zurück
     *
     * @return request
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_WM_REQUEST_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_RISK4T_WM_REQUEST_ID"))
    public TWmRequest getRequest() {
        return this.request;
    }

    @Column(name = "RISK_PLACE_OF_REG", nullable = false)
    @Enumerated(EnumType.STRING)
    public PlaceOfRegEn getRiskPlaceOfReg() {
        return riskPlaceOfReg;
    }

    @Column(name= "RISK_COMMENT", nullable = true)
    public String getRiskComment() {
        return riskComment;
    }

    public void setRiskComment(String riskComment) {
        this.riskComment = riskComment;
    }

    public void setRiskPlaceOfReg(PlaceOfRegEn riskPlaceOfReg) {
        this.riskPlaceOfReg = riskPlaceOfReg;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "risk", orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TWmRiskDetails> getRiskDetails() {
        return this.riskDetails;
    }

    public void setRiskDetails(final Set<TWmRiskDetails> riskDetails) {
        this.riskDetails = riskDetails;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_WM_PROCESS_HOSPITAL_FINAL_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_RISK4T_WM_PROC_HOS_FIN_ID"))
    public TWmProcessHospitalFinalisation getProcessHospitalFinalisation() {
        return this.processHospitalFinalisation;
    }

    public void setProcessHospitalFinalisation(final TWmProcessHospitalFinalisation processHospitalFinalisation) {
        this.processHospitalFinalisation = processHospitalFinalisation;
    }

    @Column(name = "RISK_PERCENT_TOTAL", nullable = false)
    public int getRiskPercentTotal() {
        return riskPercentTotal;
    }

    public void setRiskPercentTotal(int riskPercentTotal) {
        this.riskPercentTotal = riskPercentTotal;
    }

    @Column(name = "RISK_VALUE_TOTAL", precision = 10, scale = 2, nullable = false)
    public BigDecimal getRiskValueTotal() {
        return riskValueTotal;
    }

    public void setRiskValueTotal(BigDecimal riskValueTotal) {
        this.riskValueTotal = riskValueTotal;
    }

    @Column(name = "RISK_CALC_VALUE_TOTAL", precision = 10, scale = 2, nullable = true)
    public BigDecimal getRiskCalcValueTotal() {
        return riskCalcValueTotal;
    }

    public void setRiskCalcValueTotal(BigDecimal riskValueTotal) {
        this.riskCalcValueTotal = riskValueTotal;
    }

   @Column(name = "RISK_CALC_PERCENT_TOTAL", nullable = true)
    public Integer getRiskCalcPercentTotal() {
        return riskCalcPercentTotal;
    }

    public void setRiskCalcPercentTotal(Integer riskPercentTotal) {
        this.riskCalcPercentTotal = riskPercentTotal;
    }

    @Column(name = "RISK_ACTUAL_4_REG", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean getRiskActual4Req() {
        return riskActual4Req;
    }

    public void setRiskActual4Req(boolean riskActual4Req) {
        this.riskActual4Req = riskActual4Req;
    }

    @Column(name = "RISK_AUDIT_PERCENT", nullable = false)
    public Integer getRiskAuditPercent() {
        return riskAuditPercent;
    }

    public void setRiskAuditPercent(Integer riskAuditPercent) {
        this.riskAuditPercent = riskAuditPercent;
    }

    @Column(name = "RISK_AUDIT_PERCENT_SUGG", nullable = false)
    public Integer getRiskAuditPercentSugg() {
        return riskAuditPercentSugg;
    }

    public void setRiskAuditPercentSugg(Integer riskAuditPercentSugg) {
        this.riskAuditPercentSugg = riskAuditPercentSugg;
    }

    @Column(name = "RISK_WASTE_PERCENT", nullable = false)
    public Integer getRiskWastePercent() {
        return riskWastePercent;
    }

    public void setRiskWastePercent(Integer riskWastePercent) {
        this.riskWastePercent = riskWastePercent;
    }

    @Column(name = "RISK_WASTE_PERCENT_SUGG", nullable = false)
    public Integer getRiskWastePercentSugg() {
        return riskWastePercentSugg;
    }

    public void setRiskWastePercentSugg(Integer riskWastePercentSugg) {
        this.riskWastePercentSugg = riskWastePercentSugg;
    }

    @Column(name = "RISK_SOURCE_SUGG", nullable = true, length=255)
    public String getRiskSourceSugg() {
        return riskSourceSugg;
    }

    public void setRiskSourceSugg(String riskSourceSugg) {
        this.riskSourceSugg = riskSourceSugg;
    }

    @Column(name = "RISK_BASE_FEE", precision = 10, scale = 2, nullable = false)
    public BigDecimal getRiskBaseFee() {
        return riskBaseFee;
    }

    public void setRiskBaseFee(BigDecimal riskBaseFee) {
        this.riskBaseFee = riskBaseFee;
    }
    @Column(name = "RISK_NOT_CALCULATED_FEE", precision = 10, scale = 2, nullable = false)
    public BigDecimal getRiskNotCalculatedFee() {
        return riskNotCalculatedFee;
    }

    public void setRiskNotCalculatedFee(BigDecimal riskNotCalculatedFee) {
        this.riskNotCalculatedFee = riskNotCalculatedFee;
    }

}
