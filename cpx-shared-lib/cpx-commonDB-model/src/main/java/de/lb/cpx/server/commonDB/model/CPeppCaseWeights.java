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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 * CPeppCaseWeights initially generated at 03.02.2016 10:32:45 by Hibernate
 * Tools 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">Es wurde in der Tabelle
 * C_PEPP_CATALOG gelöst.</p>
 */
@Entity
@Table(name = "C_PEPP_CASE_WEIGHTS")
@SuppressWarnings("serial")
@SecurityDomain("cpx")
public class CPeppCaseWeights extends AbstractCatalogEntity {

    private static final long serialVersionUID = 1L;

//     private long id;
////    private CPeppCatalog CPeppCatalog;
    private int pepprRelationNumber;
    private int pepprRelationFrom;
    private Integer pepprRelationTo;
    private BigDecimal pepprRelationCostWeight;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_PEPP_CASE_WEIGHTS_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /*   @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PEPP_ID", nullable = false)
    public CPeppCatalog getCPeppCatalog() {
        return this.CPeppCatalog;
    }

    public void setCPeppCatalog(CPeppCatalog CPeppCatalog) {
        this.CPeppCatalog = CPeppCatalog;
    }
     */
    @Column(name = "PEPPR_RELATION_NUMBER", nullable = false, precision = 5, scale = 0)
    public int getPepprRelationNumber() {
        return this.pepprRelationNumber;
    }

    public void setPepprRelationNumber(int pepprRelationNumber) {
        this.pepprRelationNumber = pepprRelationNumber;
    }

    @Column(name = "PEPPR_RELATION_FROM", nullable = false, precision = 5, scale = 0)
    public int getPepprRelationFrom() {
        return this.pepprRelationFrom;
    }

    public void setPepprRelationFrom(int pepprRelationFrom) {
        this.pepprRelationFrom = pepprRelationFrom;
    }

    @Column(name = "PEPPR_RELATION_TO", precision = 5, scale = 0)
    public Integer getPepprRelationTo() {
        return this.pepprRelationTo;
    }

    public void setPepprRelationTo(Integer pepprRelationTo) {
        this.pepprRelationTo = pepprRelationTo;
    }

    @Column(name = "PEPPR_RELATION_COST_WEIGHT", precision = 10, scale = 4)
    public BigDecimal getPepprRelationCostWeight() {
        return this.pepprRelationCostWeight;
    }

    public void setPepprRelationCostWeight(BigDecimal pepprRelationCostWeight) {
        this.pepprRelationCostWeight = pepprRelationCostWeight;
    }

}
