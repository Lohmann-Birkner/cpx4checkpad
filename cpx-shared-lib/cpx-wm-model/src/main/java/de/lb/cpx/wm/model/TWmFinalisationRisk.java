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

import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 *
 * @author wilde
 */
@Entity
@Table(name = "T_WM_FINALIS_RISK", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"T_WM_PROCESS_HOSPITAL_FINAL_ID"}, name = "Uni_TWmProcessFinalId_Risk")
})
@SuppressWarnings("serial")
public class TWmFinalisationRisk extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    private TWmProcessHospitalFinalisation processHospitalFinalisation;
    private Set<TWmFinalisationRiskDetail> finalisationRiskDetails = new HashSet<>(0);
    private BigDecimal riskValueTotal;
    private BigDecimal riskCalcValueTotal;
    private String riskComment;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_WM_FINALIS_RISK_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @Column(name= "RISK_COMMENT", nullable = true)
    public String getRiskComment() {
        return riskComment;
    }

    public void setRiskComment(String riskComment) {
        this.riskComment = riskComment;
    }
                                                                            
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "finalisationRisk", orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TWmFinalisationRiskDetail> getFinalisationRiskDetails() {
        return this.finalisationRiskDetails;
    }

    public void setFinalisationRiskDetails(final Set<TWmFinalisationRiskDetail> riskDetails) {
        this.finalisationRiskDetails = riskDetails;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_WM_PROCESS_HOSPITAL_FINAL_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_RISK4T_WM_PROC_HOS_FIN_ID"))
    public TWmProcessHospitalFinalisation getProcessHospitalFinalisation() {
        return this.processHospitalFinalisation;
    }

    public void setProcessHospitalFinalisation(final TWmProcessHospitalFinalisation processHospitalFinalisation) {
        this.processHospitalFinalisation = processHospitalFinalisation;
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
}
