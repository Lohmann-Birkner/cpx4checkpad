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
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * CBASERATE initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;"> C_MDK_AUDITQUOTA: MDK
 * Prüfquoten</p>
 *
 */
@Entity
@Table(name = "C_MDK_NEGOTIABLE_AUDITQUOTA")
        //uniqueConstraints = @UniqueConstraint(columnNames = {"MDK_AQ_HOS_IDENT", "MDK_AQ_YEAR", "MDK_AQ_QUARTER"}))
@SuppressWarnings("serial")
public class CMdkNegotiableAuditquota extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    private String mdkNaqInscIdent;
    private Date mdkNaqValidFrom;
    private Date mdkNaqValidTo;
    private BigDecimal mdkNaqCaseDialogQuota;
    private BigDecimal mdkNaqAuditQuota;
    private String mdkNaqComment;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_MDK_NEGOTIABLE_AUDITQUOTA_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    /**
     * Gibt Identifikationsnumme der Krankenkasse zurück.
     *
     * @return mdkNaqInscIdent Weitere Informationen in der Tabelle
     * CPX_common.C_INSURANCE_COMPANY (INSC_IDENT)
     */
    @Column(name = "MDK_NEGO_AQ_INSC_IDENT", length = 10, nullable = false)
    public String getMdkNaqInscIdent() {
        return mdkNaqInscIdent;
    }

    public void setMdkNaqInscIdent(String mdkNaqInsIdent) {
        this.mdkNaqInscIdent = mdkNaqInsIdent;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MDK_NEGO_AQ_VALID_FROM", nullable = false, length = 11)
    public Date getMdkNaqValidFrom() {
        return mdkNaqValidFrom;
    }

    public void setMdkNaqValidFrom(Date mdkNaqValidFrom) {
        this.mdkNaqValidFrom = mdkNaqValidFrom;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MDK_NEGO_AQ_VALID_TO", nullable = false, length = 11)
    public Date getMdkNaqValidTo() {
        return mdkNaqValidTo;
    }

    public void setMdkNaqValidTo(Date mdkNaqValidTo) {
        this.mdkNaqValidTo = mdkNaqValidTo;
    }
    
    @Column(name = "MDK_NEGO_AQ_CASE_DIALOG_QUOTA", precision = 5, scale = 2, nullable = false)
    public BigDecimal getMdkNaqCaseDialogQuota() {
        return mdkNaqCaseDialogQuota;
    }

    public void setMdkNaqCaseDialogQuota(BigDecimal mdkNaqCaseDialogQuota) {
        this.mdkNaqCaseDialogQuota = mdkNaqCaseDialogQuota;
    }
    
    @Column(name = "MDK_NEGO_AQ_AUDIT_QUOTA", precision = 5, scale = 2, nullable = false)
    public BigDecimal getMdkNaqAuditQuota() {
        return mdkNaqAuditQuota;
    }

    public void setMdkNaqAuditQuota(BigDecimal mdkNaqAuditQuota) {
        this.mdkNaqAuditQuota = mdkNaqAuditQuota;
    }
    
    @Column(name = "MDK_NEGO_AQ_COMMENT", length = 500, nullable = true)
    public String getMdkNaqComment() {
        return mdkNaqComment;
    }

    public void setMdkNaqComment(String mdkNaqComment) {
        this.mdkNaqComment = mdkNaqComment;
    }
    
    
}
