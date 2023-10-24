/**
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

import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * CBASERATE initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;"> C_MDK_AUDITQUOTA: MDK
 * Prüfquoten</p>
 *
 */
@Entity
@Table(name = "C_MDK_AUDITQUOTA",
        uniqueConstraints = @UniqueConstraint(columnNames = {"MDK_AQ_HOS_IDENT", "MDK_AQ_YEAR", "MDK_AQ_QUARTER"}))
@SuppressWarnings("serial")
public class CMdkAuditquota extends AbstractEntity {

    private static final long serialVersionUID = 1L;

//     private long id;
    private String mdkAqHosIdent;
    private int mdkAqYear;
    private int mdkAqQuarter;
    private BigDecimal mdkAqQuota;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_MDK_AUDITQUOTA_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gibt Identifikationsnumme des Krankenhauses zurück.
     *
     * @return mdkAqHosIdent Weitere Informationen in der Tabelle
     * CPX_common.C_HOSPITAL (HOS_IDENT)
     */
    @Column(name = "MDK_AQ_HOS_IDENT", length = 10, nullable = false)
    public String getMdkAqHosIdent() {
        return mdkAqHosIdent;
    }

    public void setMdkAqHosIdent(String mdkAqHosIdent) {
        this.mdkAqHosIdent = mdkAqHosIdent;
    }

    @Column(name = "MDK_AQ_YEAR", nullable = false)
    public int getMdkAqYear() {
        return mdkAqYear;
    }

    public void setMdkAqYear(int mdkAqYear) {
        this.mdkAqYear = mdkAqYear;
    }

    @Column(name = "MDK_AQ_QUARTER", nullable = false)
    public int getMdkAqQuarter() {
        return mdkAqQuarter;
    }

    public void setMdkAqQuarter(int mdkAqQuarter) {
        this.mdkAqQuarter = mdkAqQuarter;
    }

    @Column(name = "MDK_AQ_QUOTA", precision = 5, scale = 2, nullable = false)
    public BigDecimal getMdkAqQuota() {
        return mdkAqQuota;
    }

    public void setMdkAqQuota(BigDecimal mdkAqQuota) {
        this.mdkAqQuota = mdkAqQuota;
    }

    public long getGivenComplaints(final long pCaseCount) {
        return BigDecimal.valueOf(pCaseCount)
                .multiply(mdkAqQuota)
                .divide(BigDecimal.valueOf(100D))
                .setScale(0, RoundingMode.DOWN)
                .longValue();
    }

}
