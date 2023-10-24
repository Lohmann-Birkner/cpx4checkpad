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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 * CDrgCw2DepType initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">wurde in der die Tabelle
 * C_DRG_CATALOG gelöst</p>
 *
 */
@Entity
@Table(name = "C_DRG_CW_2_DEP_TYPE")
@SuppressWarnings("serial")
@SecurityDomain("cpx")
public class CDrgCw2DepType extends AbstractCatalogEntity {

    private static final long serialVersionUID = 1L;

    private CDrgLosDependency CDrgLosDependency;
    private String cwAdmodEn;
    private BigDecimal cw;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_DRG_CW_2_DEP_TYPE_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DRGL_ID", nullable = false)
    public CDrgLosDependency getCDrgLosDependency() {
        return this.CDrgLosDependency;
    }

    public void setCDrgLosDependency(CDrgLosDependency CDrgLosDependency) {
        this.CDrgLosDependency = CDrgLosDependency;
    }

    @Column(name = "CW_ADMOD_EN", length = 25)
    public String getCwAdmodEn() {
        return this.cwAdmodEn;
    }

    public void setCwAdmodEn(String cwAdmodEn) {
        this.cwAdmodEn = cwAdmodEn;
    }

    @Column(name = "CW", precision = 10, scale = 3)
    public BigDecimal getCw() {
        return this.cw;
    }

    public void setCw(BigDecimal cw) {
        this.cw = cw;
    }

}
