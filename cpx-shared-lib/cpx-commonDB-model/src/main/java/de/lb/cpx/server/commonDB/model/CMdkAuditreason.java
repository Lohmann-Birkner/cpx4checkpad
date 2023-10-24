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

import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.server.commonDB.model.enums.RiskAreaEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.server.commons.dao.MenuCacheEntity;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

/**
 * CBASERATE initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;"> C_MDK_AUDITREASON: MDK
 * Prüfgründe</p>
 *
 */
@Entity
@Table(name = "C_MDK_AUDITREASON",
        uniqueConstraints = @UniqueConstraint(columnNames = {"MDK_AR_NUMBER" /*, "MDK_AR_SORT" */}))
@SuppressWarnings("serial")
public class CMdkAuditreason extends AbstractEntity implements MenuCacheEntity {

    private static final long serialVersionUID = 1L;

//     private long id;
    private String mdkArName;
    private Long mdkArNumber;
    private Date mdkArValidFrom;
    private Date mdkArValidTo;
    private Integer mdkArSort;
    private Boolean mdkArValid = true; //boolean indicator if value is valid
    private Boolean mdkArDeleted = false; //boolean indicator if value is deleted
    private RiskAreaEn mdkArRiskArea;
    private CaseTypeEn mdkArCaseType;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_MDK_AUDITREASON_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the mdkArName
     */
    @Column(name = "MDK_AR_NAME", nullable = false, length = 80)
    public String getMdkArName() {
        return mdkArName;
    }

    /**
     * @param mdkArName the mdkArName to set
     */
    public void setMdkArName(String mdkArName) {
        this.mdkArName = mdkArName;
    }

    /**
     * @return the mdkArNumber
     */
    @SequenceGenerator(name = "default_gen", sequenceName = "C_MDK_AUDREASON_INTERNAL_ID_SQ", allocationSize = 1)
    @Column(name = "MDK_AR_NUMBER", nullable = false)
    public Long getMdkArNumber() {
        return mdkArNumber;
    }

    /**
     * @param mdkArNumber the mdkArNumber to set
     */
    public void setMdkArNumber(Long mdkArNumber) {
        this.mdkArNumber = mdkArNumber;
    }

    /**
     * @return the mdkArValidFrom
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MDK_AR_VALID_FROM", nullable = false, length = 11)
    public Date getMdkArValidFrom() {
        return mdkArValidFrom == null ? null : new Date(mdkArValidFrom.getTime());
    }

    /**
     * @param mdkArValidFrom the mdkArValidFrom to set
     */
    public void setMdkArValidFrom(Date mdkArValidFrom) {
        this.mdkArValidFrom = mdkArValidFrom == null ? null : new Date(mdkArValidFrom.getTime());
    }

    /**
     * @return the mdkArValidTo
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MDK_AR_VALID_TO", nullable = false, length = 11)
    public Date getMdkArValidTo() {
        return mdkArValidTo == null ? null : new Date(mdkArValidTo.getTime());
    }

    /**
     * @param mdkArValidTo the mdkArValidTo to set
     */
    public void setMdkArValidTo(Date mdkArValidTo) {
        this.mdkArValidTo = mdkArValidTo == null ? null : new Date(mdkArValidTo.getTime());
    }

    /**
     * @return the mdkArNumber
     */
    @Column(name = "MDK_AR_SORT")
    public Integer getMdkArSort() {
        return mdkArSort;
    }

    /**
     * @param mdkArSort the mdkArNumber to set
     */
    public void setMdkArSort(Integer mdkArSort) {
        this.mdkArSort = mdkArSort;
    }

    /**
     * @return indicator if value is valid or invalid
     */
    @Column(name = "MDK_AR_VALID", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public Boolean getMdkArValid() {
        return mdkArValid;
    }

    /**
     * @param mdkArValid mark value to valid / invalid
     */
    public void setMdkArValid(Boolean mdkArValid) {
        this.mdkArValid = mdkArValid;
    }

    /**
     * @return indicator if value is deleted or not
     */
    @Column(name = "MDK_AR_DELETED", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public Boolean getMdkArDeleted() {
        return mdkArDeleted;
    }

    /**
     * @param mdkArDeleted mark value as deleted or not deleted
     */
    public void setMdkArDeleted(Boolean mdkArDeleted) {
        this.mdkArDeleted = mdkArDeleted;
    }

    @Column(name = "MDK_AR_RISK_AREA", nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("OTHER")
    public RiskAreaEn getMdkArRiskArea() {
        return mdkArRiskArea;
    }

    public void setMdkArRiskArea(RiskAreaEn mdkArRiskArea) {
        this.mdkArRiskArea = mdkArRiskArea;
    }
    

    @Column(name = "MDK_AR_CASE_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("DRG")
    public CaseTypeEn getMdkArCaseType() {
        return mdkArCaseType;
    }

    public void setMdkArCaseType(CaseTypeEn mdkArCaseType) {
        this.mdkArCaseType = mdkArCaseType;
    }
    
    
    
//    @Override
//    public int compareTo(final CMdkAuditreason o) {
//        return StringUtils.trimToEmpty(this.getMdkArName())
//                .compareToIgnoreCase(StringUtils.trimToEmpty(o.getMdkArName()));
//    }
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.mdkArName);
        hash = 19 * hash + Objects.hashCode(this.mdkArNumber);
        hash = 19 * hash + Objects.hashCode(this.mdkArValidFrom);
        hash = 19 * hash + Objects.hashCode(this.mdkArValidTo);
        hash = 19 * hash + Objects.hashCode(this.mdkArSort);
        hash = 19 * hash + Objects.hashCode(this.mdkArValid);
        hash = 19 * hash + Objects.hashCode(this.mdkArDeleted);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CMdkAuditreason other = (CMdkAuditreason) obj;
        if (!Objects.equals(this.mdkArName, other.mdkArName)) {
            return false;
        }
        if (!Objects.equals(this.mdkArNumber, other.mdkArNumber)) {
            return false;
        }
        if (!Objects.equals(this.mdkArValidFrom, other.mdkArValidFrom)) {
            return false;
        }
        if (!Objects.equals(this.mdkArValidTo, other.mdkArValidTo)) {
            return false;
        }
        if (!Objects.equals(this.mdkArSort, other.mdkArSort)) {
            return false;
        }
        if (!Objects.equals(this.mdkArValid, other.mdkArValid)) {
            return false;
        }
        if (!Objects.equals(this.mdkArDeleted, other.mdkArDeleted)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return mdkArName;
    }

    @Transient
    @Override
    public String getName() {
        return MenuCacheEntity.getName(this, mdkArName);
    }

    @Transient
    @Override
    public Long getMenuCacheId() {
        return mdkArNumber;
    }

    @Transient
    @Override
    public Date getValidFrom() {
        return mdkArValidFrom;
    }

    @Transient
    @Override
    public Date getValidTo() {
        return mdkArValidTo;
    }

    @Transient
    @Override
    public boolean isValid() {
        return mdkArValid;
    }

    @Override
    public boolean isValid(final Date pDate) {
        return MenuCacheEntity.isValid(pDate, getValidFrom(), getValidTo(), isValid());
    }

    @Transient
    @Override
    public boolean isDeleted() {
        return mdkArDeleted;
    }

    @Transient
    @Override
    public boolean isInActive() {
        return isInActive(new Date());
    }

    @Override
    public boolean isInActive(final Date pDate) {
        return isDeleted() || !isValid(pDate);
    }

    @Transient
    @Override
    public int getSort() {
        return Objects.requireNonNullElse(mdkArSort, 0);
    }

}
