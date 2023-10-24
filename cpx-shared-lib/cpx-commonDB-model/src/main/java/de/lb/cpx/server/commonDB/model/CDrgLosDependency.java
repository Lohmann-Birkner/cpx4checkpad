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
 */
package de.lb.cpx.server.commonDB.model;

import de.lb.cpx.server.commons.dao.AbstractCatalogEntity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 * CDrgLosDependency initially generated at 03.02.2016 10:32:45 by Hibernate
 * Tools 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">wurde in der die Tabelle
 * C_DRG_CATALOG gelöst</p>
 */
@Entity
@Table(name = "C_DRG_LOS_DEPENDENCY")
@SuppressWarnings("serial")
@SecurityDomain("cpx")
public class CDrgLosDependency extends AbstractCatalogEntity {

    private static final long serialVersionUID = 1L;

//     private long id;
    private CDrgCatalog CDrgCatalog;
    private Integer drgl1DeductionDay;
    private BigDecimal drglCwDeductionDay;
    private Integer drgl1SurchargeDay;
    private BigDecimal drglCwSurchargeDay;
    private BigDecimal drglCwDeductTransfDay;
    private Boolean drgTransferLumpSumFl;
    private Boolean drglReadmExceptionFl;
    private BigDecimal drglAlos;
    private Date drglValidFrom;
    private Date drglValidTo;
    private Set<CDrgCw2DepType> CDrgCw2DepTypes = new HashSet<>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_DRG_LOS_DEPENDENCY_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DRG_ID", nullable = false)
    public CDrgCatalog getCDrgCatalog() {
        return this.CDrgCatalog;
    }

    public void setCDrgCatalog(CDrgCatalog CDrgCatalog) {
        this.CDrgCatalog = CDrgCatalog;
    }

    @Column(name = "DRGL_1_DEDUCTION_DAY", precision = 5, scale = 0)
    public Integer getDrgl1DeductionDay() {
        return this.drgl1DeductionDay;
    }

    public void setDrgl1DeductionDay(Integer drgl1DeductionDay) {
        this.drgl1DeductionDay = drgl1DeductionDay;
    }

    @Column(name = "DRGL_CW_DEDUCTION_DAY", precision = 10, scale = 3)
    public BigDecimal getDrglCwDeductionDay() {
        return this.drglCwDeductionDay;
    }

    public void setDrglCwDeductionDay(BigDecimal drglCwDeductionDay) {
        this.drglCwDeductionDay = drglCwDeductionDay;
    }

    @Column(name = "DRGL_1_SURCHARGE_DAY", precision = 5, scale = 0)
    public Integer getDrgl1SurchargeDay() {
        return this.drgl1SurchargeDay;
    }

    public void setDrgl1SurchargeDay(Integer drgl1SurchargeDay) {
        this.drgl1SurchargeDay = drgl1SurchargeDay;
    }

    @Column(name = "DRGL_CW_SURCHARGE_DAY", precision = 10, scale = 3)
    public BigDecimal getDrglCwSurchargeDay() {
        return this.drglCwSurchargeDay;
    }

    public void setDrglCwSurchargeDay(BigDecimal drglCwSurchargeDay) {
        this.drglCwSurchargeDay = drglCwSurchargeDay;
    }

    @Column(name = "DRGL_CW_DEDUCT_TRANSF_DAY", precision = 10, scale = 3)
    public BigDecimal getDrglCwDeductTransfDay() {
        return this.drglCwDeductTransfDay;
    }

    public void setDrglCwDeductTransfDay(BigDecimal drglCwDeductTransfDay) {
        this.drglCwDeductTransfDay = drglCwDeductTransfDay;
    }

    @Column(name = "DRG__TRANSFER_LUMP_SUM_FL", precision = 1, scale = 0)
    public Boolean getDrgTransferLumpSumFl() {
        return this.drgTransferLumpSumFl;
    }

    public void setDrgTransferLumpSumFl(Boolean drgTransferLumpSumFl) {
        this.drgTransferLumpSumFl = drgTransferLumpSumFl;
    }

    @Column(name = "DRGL_READM_EXCEPTION_FL", precision = 1, scale = 0)
    public Boolean getDrglReadmExceptionFl() {
        return this.drglReadmExceptionFl;
    }

    public void setDrglReadmExceptionFl(Boolean drglReadmExceptionFl) {
        this.drglReadmExceptionFl = drglReadmExceptionFl;
    }

    @Column(name = "DRGL_ALOS", precision = 5, scale = 1)
    public BigDecimal getDrglAlos() {
        return this.drglAlos;
    }

    public void setDrglAlos(BigDecimal drglAlos) {
        this.drglAlos = drglAlos;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DRGL_VALID_FROM", length = 7)
    public Date getDrglValidFrom() {
        return drglValidFrom == null ? null : new Date(drglValidFrom.getTime());
    }

    public void setDrglValidFrom(Date drglValidFrom) {
        this.drglValidFrom = drglValidFrom == null ? null : new Date(drglValidFrom.getTime());
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DRGL_VALID_TO", length = 7)
    public Date getDrglValidTo() {
        return drglValidTo == null ? null : new Date(drglValidTo.getTime());
    }

    public void setDrglValidTo(Date drglValidTo) {
        this.drglValidTo = drglValidTo == null ? null : new Date(drglValidTo.getTime());
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "CDrgLosDependency")
    public Set<CDrgCw2DepType> getCDrgCw2DepTypes() {
        return this.CDrgCw2DepTypes;
    }

    public void setCDrgCw2DepTypes(Set<CDrgCw2DepType> CDrgCw2DepTypes) {
        this.CDrgCw2DepTypes = CDrgCw2DepTypes;
    }

}
