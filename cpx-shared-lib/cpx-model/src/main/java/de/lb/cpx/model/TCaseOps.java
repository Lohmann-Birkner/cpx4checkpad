/* 
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import de.lb.cpx.model.enums.LocalisationEn;
import de.lb.cpx.server.commons.dao.AbstractCaseEntity;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
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
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

/**
 * TCaseOps initially generated at 21.01.2016 17:07:59 by Hibernate Tools
 * 3.2.2.GA
 * <p style="font-size:1em; color:green;"> "T_CASE_OPS" ist Tabelle der
 * Prozeduren</p>
 */
@Entity
@Table(name = "T_CASE_OPS",
        indexes = {
            @Index(name = "IDX_OPS4DEPC_ID", columnList = "T_CASE_DEPARTMENT_ID", unique = false),
            @Index(name = "IDX_CASE_OPS4WARDC_ID", columnList = "T_CASE_WARD_ID", unique = false)})
@SuppressWarnings("serial")
public class TCaseOps extends AbstractCaseEntity {

    private static final long serialVersionUID = 1L;

    //   private long id;
    private TCaseWard caseWard;
    private TCaseDepartment caseDepartment;
    private LocalisationEn opscLocEn = LocalisationEn.E;
    private Date opscDatum;
    private String opscCode;
    private boolean opscIsToGroupFl = true;
    private Set<TCaseOpsGrouped> caseOpsGroupeds = new HashSet<>(0);

    public TCaseOps() {    
        setIgnoreFields();
    }
    
    public TCaseOps(Long pCurrentUser){
        super(pCurrentUser);
        setIgnoreFields();
    }
    

    @Override
    protected final void setIgnoreFields() {
        ignoredFields = new String[]{"caseWard", "caseDepartment", "caseOpsGroupeds"};

    }
    /**
     * Gibt Verweis auf die Tablle T_CASE_DEPARTMENT zurück.
     *
     * @return caseDepartment
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_CASE_DEPARTMENT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_CASE_OPS4CASE_DEPARTMENT_ID"))
    @JsonBackReference(value = "ops")
    public TCaseDepartment getCaseDepartment() {
        return this.caseDepartment;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseOps")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TCaseOpsGrouped> getCaseOpsGroupeds() {
        return this.caseOpsGroupeds;
    }

    /**
     * Gibt Verweis auf die Tablle T_CASE_WARD zurück.
     *
     * @return caseWard
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_CASE_WARD_ID", foreignKey = @ForeignKey(name = "FK_CASE_OPS4T_CASE_WARD_ID"))
    public TCaseWard getCaseWard() {
        return this.caseWard;
    }

    /**
     *
     * @return id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_CASE_OPS_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    /**
     * Gibt opscCode zurück, wie es in der Spalte C_OPS_CATALOG.OPS_CODE steht .
     *
     * @return opscCode
     */
    @Column(name = "OPSC_CODE", nullable = false, length = 10)
    public String getOpscCode() {
        return this.opscCode;
    }

    /**
     * Gibt Datum der OPS - Durchführung(mit Uhrzeit) zurück.
     *
     * @return opscDatum
     */
    //@Temporal(TemporalType.DATE)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "OPSC_DATUM", length = 7)
    public Date getOpscDatum() {
        return opscDatum == null ? null : new Date(opscDatum.getTime());
    }

    /**
     * Gibt Lokalisation des OPS 0-3 zurück, z.b. E,R,L,B.
     *
     * @return opscLocEn
     */
    @Column(name = "OPSC_LOC_EN", length = 1, nullable = false)
    @Enumerated(EnumType.STRING)
    public LocalisationEn getOpscLocEn() {
        return this.opscLocEn;
    }

    /**
     * Gibt 1/0 Flag zurück, der markiert OPS, die zum Groupen hingezogen werden
     * muss.
     *
     * @return opscIsToGroupFl
     */
    @Column(name = "TO_GROUP_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean getOpsIsToGroupFl() {
        return opscIsToGroupFl;
    }

    public void setOpsIsToGroupFl(boolean b) {
        opscIsToGroupFl = b;
    }

    /**
     *
     * @param caseDepartment Column T_CASE_DEPARTMENT_ID: Verweis auf die Tablle
     * T_CASE_DEPARTMENT.
     */
    public void setCaseDepartment(final TCaseDepartment caseDepartment) {
        this.caseDepartment = caseDepartment;
    }

    public void setCaseOpsGroupeds(final Set<TCaseOpsGrouped> caseOpsGroupeds) {
        this.caseOpsGroupeds = caseOpsGroupeds;
    }

    /**
     *
     * @param caseWard Column T_CASE_WARD_ID : Verweis auf die Tablle
     * T_CASE_WARD .
     */
    public void setCaseWard(final TCaseWard caseWard) {
        this.caseWard = caseWard;
    }

    public void setId(final long id) {
        this.id = id;
    }

    /**
     *
     * @param opscCode Column OPSC_CODE: wie es in der Spalte
     * C_OPS_CATALOG.OPS_CODE steht .
     */
    public void setOpscCode(final String opscCode) {
        this.opscCode = opscCode;
    }

    /**
     *
     * @param opscDatum Column OPSC_DATUM: Datum der OPS - Durchführung(mit
     * Uhrzeit) .
     */
    public void setOpscDatum(final Date opscDatum) {
        this.opscDatum = opscDatum == null ? null : new Date(opscDatum.getTime());
    }

    /**
     *
     * @param opscLocEn Column OPSC_LOC_EN: Lokalisation 0,3 (E,R,L,B) .
     */
    public void setOpscLocEn(final LocalisationEn opscLocEn) {
        this.opscLocEn = opscLocEn;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {

        TCaseOps cloned = (TCaseOps) super.clone();
        return cloned;
    }

    @Override
    public Object cloneWithoutIds(Long currentCpxUserId) throws CloneNotSupportedException {

        TCaseOps cloned = (TCaseOps) super.cloneWithoutIds(currentCpxUserId);
        cloned.setId(0);
        cloned.setCaseOpsGroupeds(new HashSet<>(0));
        return cloned;
    }

    @Override
    public boolean versionEquals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TCaseOps)) {
            return false;
        }
        final TCaseOps other = (TCaseOps) obj;
        if (this.opscIsToGroupFl != other.getOpsIsToGroupFl()) {
            return false;
        }
        if (!isStringEquals(this.opscCode, other.getOpscCode())) {
            return false;
        }
        if (this.opscLocEn != other.getOpscLocEn()) {
            return false;
        }
        //AWI: field internaly stored as timestamp, comparison will fail, in Objects.equals()
        if (!Objects.equals(getOpscDatum(), other.getOpscDatum())) {
            return false;
        }
        return true;
    }

    @PrePersist
    public void incrementSumOps() {
        TCaseDetails caseDetails = caseDepartment.getCaseDetails();
        Integer sum = caseDetails.getSumOfOps();
        if (sum == null) {
            caseDetails.setSumOfOps(0);
            sum = 0;
        }
        caseDetails.setSumOfOps((sum + 1));
    }

    @PreRemove
    public void decrementSumOps() {
        TCaseDetails caseDetails = caseDepartment.getCaseDetails();
        Integer sum = caseDetails.getSumOfOps();
        if (sum == null) {
            caseDetails.setSumOfOps(0);
            sum = 0;
        }
        caseDetails.setSumOfOps((sum - 1));
    }
}
