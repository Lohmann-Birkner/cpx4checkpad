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
import de.lb.cpx.model.util.ModelUtil;
import de.lb.cpx.server.commons.dao.AbstractCaseEntity;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * TCaseWard initially generated at 21.01.2016 17:07:59 by Hibernate Tools
 * 3.2.2.GA Die Tabelle "T_CASE_WARD" enthält die Bewegungen, die zu einem Fall
 * besuchten Abteilungen.
 */
@Entity
@Table(name = "T_CASE_WARD", indexes = {
    @Index(name = "IDX_CASE_WARD4DEPC_ID", columnList = "T_CASE_DEPARTMENT_ID", unique = false)})
@SuppressWarnings("serial")
public class TCaseWard extends AbstractCaseEntity implements HospitalDevisionIF {

    private static final long serialVersionUID = 1L;

//    private long id;
    private TCaseDepartment caseDepartment;
    private String wardcIdent;
    private Date wardcAdmdate;
    private Date wardcDisdate;

    private Set<TCaseIcd> caseIcds = new HashSet<>(0);
    private Set<TCaseOps> caseOpses = new HashSet<>(0);

    public TCaseWard() {
        setIgnoreFields();
    }
    
    public TCaseWard(Long pCurrentUserId){
        super(pCurrentUserId);
        setIgnoreFields();
    }
    @Override
    protected final void setIgnoreFields() {
         ignoredFields = new String[]{"caseDepartment", "getPrincipalCaseIcd"};
    }


    /**
     * Gibt Verweis auf die Tablle T_CASE_DEPARTMENT zurück , zu der diese
     * Station gehört.
     *
     * @return case department
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_CASE_DEPARTMENT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_CASE_WARD4DEPC_ID"))
    @JsonBackReference(value = "ward")
    public TCaseDepartment getCaseDepartment() {
        return this.caseDepartment;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseWard")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TCaseIcd> getCaseIcds() {
        return this.caseIcds;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseWard")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TCaseOps> getCaseOpses() {
        return this.caseOpses;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_CASE_WARD_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    /**
     * Gibt Aufnahmedatum auf eine Station zurück.
     *
     * @return wardcAdmdate
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "WARDC_ADMDATE", length = 7)
    public Date getWardcAdmdate() {
        return wardcAdmdate == null ? null : new Date(wardcAdmdate.getTime());
    }

    /**
     * Gibt Entlassungsdatum aus einer Station zurück.
     *
     * @return wardcDisdate
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "WARDC_DISDATE", length = 7)
    public Date getWardcDisdate() {
        return wardcDisdate == null ? null : new Date(wardcDisdate.getTime());
    }

    /**
     * Gibt Identifikation der Station zurück.
     *
     * @return wardcIdent
     */
    @Column(name = "WARDC_IDENT", length = 10)
    public String getWardcIdent() {
        return this.wardcIdent;
    }

    /**
     * Gibt Verweis auf die Tablle T_CASE_DEPARTMENT zurück, zu der diese
     * Station gehört.
     *
     * @param caseDepartment Case department
     */
    public void setCaseDepartment(final TCaseDepartment caseDepartment) {
        this.caseDepartment = caseDepartment;
    }


    @Override
    public void setCaseIcds(final Set<TCaseIcd> caseIcds) {
        this.caseIcds = caseIcds;
    }

    @Override
    public void setCaseOpses(final Set<TCaseOps> caseOpses) {
        this.caseOpses = caseOpses;
    }

    public void setId(final long id) {
        this.id = id;
    }

    /**
     *
     * @param wardcAdmdate Column WARDC_ADMDATE: Aufnahmedatum auf eine Station.
     */
    public void setWardcAdmdate(final Date wardcAdmdate) {
        this.wardcAdmdate = wardcAdmdate == null ? null : new Date(wardcAdmdate.getTime());
    }

    /**
     *
     * @param wardcDisdate Column WARDC_DISDATE : Entlassungsdatum aus einer
     * Station.
     */
    public void setWardcDisdate(final Date wardcDisdate) {
        this.wardcDisdate = wardcDisdate == null ? null : new Date(wardcDisdate.getTime());
    }

    /**
     *
     * @param wardcIdent Column WARDC_IDENT: Identifikation der Station .
     */
    public void setWardcIdent(final String wardcIdent) {
        this.wardcIdent = wardcIdent;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {

        TCaseWard cloned = (TCaseWard) super.clone();

        ModelUtil.cloneAndCheckIcdsOps4Case(caseOpses, caseIcds, cloned);
        return cloned;
    }

    @Override
    public Object cloneWithoutIds(Long currentCpxUserId) throws CloneNotSupportedException {

        TCaseWard cloned = (TCaseWard) super.cloneWithoutIds(currentCpxUserId);
        cloned.setId(0);
        cloned.setCaseIcds(null);
        cloned.setCaseOpses(null);

        ModelUtil.cloneWithoutIdAndCheckIcdsOps4Case(caseOpses, caseIcds, cloned, currentCpxUserId);
        return cloned;
    }

    public Object cloneWithoutIds(Long currentCpxUserId, Map<TCaseIcd, TCaseIcd>icds, Map<TCaseOps, TCaseOps>ops) throws CloneNotSupportedException {

        TCaseWard cloned = (TCaseWard) super.cloneWithoutIds(currentCpxUserId);
        cloned.setId(0);
        cloned.setCaseIcds(null);
        cloned.setCaseOpses(null);

        ModelUtil.cloneWithoutIdAndCheckIcds4Case(caseIcds, cloned, currentCpxUserId, icds);
        ModelUtil.cloneWithoutIdAndCheckOps4Case(caseOpses, cloned, currentCpxUserId, ops);
        return cloned;
    }

    @javax.persistence.Transient
    public TCaseIcd getPrincipalCaseIcd() {
        if (caseIcds == null) {
            return null;
        }
        for (TCaseIcd icd : caseIcds) {
            if (icd.getIcdcIsHdxFl()) {
                return icd;
            }
        }

        return null;
    }

    @Override
    public boolean versionEquals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TCaseWard)) {
            return false;
        }
        final TCaseWard other = (TCaseWard) obj;
        if (!Objects.equals(this.wardcIdent, other.wardcIdent)) {
            return false;
        }
        if (!Objects.equals(this.wardcAdmdate, other.wardcAdmdate)) {
            return false;
        }
        if (!Objects.equals(this.wardcDisdate, other.wardcDisdate)) {
            return false;
        }
        if (!ModelUtil.versionSetEquals(this.caseIcds, other.caseIcds)) {
            return false;
        }
        if (!ModelUtil.versionSetEquals(this.caseOpses, other.caseOpses)) {
            return false;
        }
        return true;
    }

    @Transient
    @Override
    public Date getStartDate(){
        return this.getWardcAdmdate();
    }
    
    
    @Transient
    @Override
    public Date getEndDate(){
        return this.getWardcDisdate();
    }
}
