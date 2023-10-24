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
 *    2020  wilde - initial API and implementation and/or initial documentation
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

/**
 *
 * @author wilde
 */
@Entity
@Table(name = "T_WM_FINALIS_RISK_DETAILS", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"T_WM_FINALIS_RISK_ID", "RISK_AREA"}, name = "Uni_Risk_RiskArea")
})
@SuppressWarnings("serial")
public class TWmFinalisationRiskDetail extends AbstractEntity implements Comparable<TWmRiskDetails> {

    private static final long serialVersionUID = 1L;

    private TWmFinalisationRisk finalisationRisk;
    private BigDecimal riskValue;
    private BigDecimal riskCalcValue;
    private RiskAreaEn riskArea;
    private String riskComment;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_WM_FINALIS_RISK_DETAILS_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public void setFinalisRisk(final TWmFinalisationRisk risk) {
        this.finalisationRisk = risk;
    }

    /**
     * Gibt Verweis auf die ID der Tabelle T_RISK zurück
     *
     * @return hospitalCase
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_WM_FINALIS_RISK_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_FINALIS_RISK_DETAILS4T_FINALIS_RISK_ID"))
    public TWmFinalisationRisk getFinalisationRisk() {
        return this.finalisationRisk;
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
}
