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

import de.lb.cpx.server.commonDB.model.enums.RiskAreaEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.math.BigDecimal;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.Type;

/**
 * TCase initially generated at 21.01.2016 17:07:59 by Hibernate Tools 3.2.2.GA
 * <p style="font-size:1em; color:green;"> Die Tabelle "T_CASE" speichert alle
 * Krankenhausfälle, die in die Datenbank eingelesen wurden. </p>
 */
@Entity
@Table(name = "T_WM_RISK_DETAILS", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"T_WM_RISK_ID", "RISK_AREA"}, name = "Uni_Risk_RiskArea")
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
public class TWmRiskDetails extends AbstractEntity implements Comparable<TWmRiskDetails> {

    private static final long serialVersionUID = 1L;

    private TWmRisk risk;
    private int riskPercent;
    private BigDecimal riskValue;
    private Integer riskCalcPercent;
    private BigDecimal riskCalcValue;
    private RiskAreaEn riskArea;
    private String riskComment;
    private Integer riskAuditPercent;
    private Integer riskAuditPercentSugg;
    private Integer riskWastePercent;
    private Integer riskWastePercentSugg;
    private String riskSourceSugg;
    private BigDecimal riskBaseFee;
    private boolean riskUsedForAuditFl;
    private boolean riskUsedForFinalFl;
    private BigDecimal riskNotCalculatedFee;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_WM_RISK_DETAILS_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    /**
     *
     * @param risk Column T_RISK_DETAILS_ID:Verweis auf die ID der Tabelle
     * T_RISK.
     */
    public void setRisk(final TWmRisk risk) {
        this.risk = risk;
    }

    /**
     * Gibt Verweis auf die ID der Tabelle T_RISK zurück
     *
     * @return hospitalCase
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_WM_RISK_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_RISK_DETAILS4T_RISK_ID"))
    public TWmRisk getRisk() {
        return this.risk;
    }

    @Column(name = "RISK_PERCENT", nullable = false)
    public int getRiskPercent() {
        return riskPercent;
    }

    public void setRiskPercent(int riskPercent) {
        this.riskPercent = riskPercent;
    }
    @Column(name = "RISK_CALC_PERCENT", nullable = true)
    public Integer getRiskCalcPercent() {
        return riskCalcPercent;
    }

    public void setRiskCalcPercent(Integer riskPercent) {
        this.riskCalcPercent = riskPercent;
    }

    @Column(name = "RISK_VALUE", precision = 10, scale = 2, nullable = false)
    public BigDecimal getRiskValue() {
        return riskValue;
    }

    public void setRiskValue(BigDecimal riskValue) {
        this.riskValue = riskValue;
    }


    @Column(name = "RISK_CALC_VALUE", precision = 10, scale = 2, nullable = true)
    public BigDecimal getRiskCalcValue() {
        return riskCalcValue;
    }

    public void setRiskCalcValue(BigDecimal riskValue) {
        this.riskCalcValue = riskValue;
    }

    @Column(name = "RISK_AREA", nullable = false)
    @Enumerated(EnumType.STRING)
    public RiskAreaEn getRiskArea() {
        return riskArea;
    }

    public void setRiskArea(RiskAreaEn riskArea) {
        this.riskArea = riskArea;
    }

    @Column(name = "RISK_COMMENT", nullable = true)
    public String getRiskComment() {
        return riskComment;
    }

    public void setRiskComment(String riskComment) {
        this.riskComment = riskComment;
    }

    @Override
    public int compareTo(TWmRiskDetails o) {
        return this.getRiskArea().getId().compareTo(o.getRiskArea().getId());
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

//[sqlsrv]::ALTER TABLE T_WM_RISK_DETAILS ADD RISK_USED_FOR_AUDIT_FL INT NOT NULL DEFAULT 0; 

    @Column(name = "RISK_USED_FOR_AUDIT_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean getRiskUsedForAuditFl() {
        return riskUsedForAuditFl;
    }

    public void setRiskUsedForAuditFl(boolean riskUsedForAuditFl) {
        this.riskUsedForAuditFl = riskUsedForAuditFl;
    }

//[oracle]::ALTER TABLE T_WM_RISK_DETAILS ADD RISK_USED_FOR_FINAL_FL NUMBER(1,0) DEFAULT 0 NOT NULL; 
    @Column(name = "RISK_USED_FOR_FINAL_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean getRiskUsedForFinalFl() {
        return riskUsedForFinalFl;
    }

    public void setRiskUsedForFinalFl(boolean riskUsedForFinalFl) {
        this.riskUsedForFinalFl = riskUsedForFinalFl;
    }

    @Column(name = "RISK_NOT_CALCULATED_FEE", precision = 10, scale = 2, nullable = false)
    public BigDecimal getRiskNotCalculatedFee() {
        return riskNotCalculatedFee;
    }

    public void setRiskNotCalculatedFee(BigDecimal riskNotCalculatedFee) {
        this.riskNotCalculatedFee = riskNotCalculatedFee;
    }

}
