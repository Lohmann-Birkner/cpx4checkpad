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
 *    2019  Shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commonDB.model;

import de.lb.cpx.model.enums.CategoryEn;
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

/**
 *
 * @author shahin
 */
@Entity
@Table(name = "C_WM_LIST_DRAFT_TYPE", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"WM_DRT_INTERNAL_ID"})
})
@SuppressWarnings("serial")
public class CWmListDraftType extends AbstractEntity implements MenuCacheEntity {

    private static final long serialVersionUID = 1L;

    private long wmDrtInternalId;
    private String wmDrtName;
    private Integer wmDrtSort;
    private boolean wmDrtValid;
    private Date wmDrtValidFrom;
    private Date wmDrtValidTo;
    private boolean wmDrtDeleted;
    private CategoryEn wmDrtCategory;
    private String wmDrtdescription;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_WM_LIST_DRAFT_TYPE_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @SequenceGenerator(name = "default_gen", sequenceName = "C_WM_LIST_DRT_INTERNAL_ID_SQ", allocationSize = 1)
    @Column(name = "WM_DRT_INTERNAL_ID", unique = true, nullable = false)
    public long getWmDrtInternalId() {
        return wmDrtInternalId;
    }

    public void setWmDrtInternalId(long wmDrtInternalId) {
        this.wmDrtInternalId = wmDrtInternalId;
    }

    @Column(name = "WM_DRT_NAME", nullable = false, length = 100)
    public String getWmDrtName() {
        return wmDrtName;
    }

    public void setWmDrtName(String wmDrtName) {
        this.wmDrtName = wmDrtName;
    }

    @Column(name = "WM_DRT_SORT", nullable = false)
    public Integer getWmDrtSort() {
        return wmDrtSort;
    }

    public void setWmDrtSort(Integer wmDrtSort) {
        this.wmDrtSort = wmDrtSort;
    }

    @Column(name = "WM_DRT_VALID", length = 3, nullable = false)
    public boolean getWmDrtValid() {
        return wmDrtValid;
    }

    public void setWmDrtValid(boolean wmDrtValid) {
        this.wmDrtValid = wmDrtValid;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "WM_DRT_VALID_FROM", length = 11, nullable = true)
    public Date getWmDrtValidFrom() {
        return wmDrtValidFrom == null ? null : new Date(wmDrtValidFrom.getTime());
    }

    public void setWmDrtValidFrom(Date wmDrtValidFrom) {
        this.wmDrtValidFrom = wmDrtValidFrom == null ? null : new Date(wmDrtValidFrom.getTime());
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "WM_DRT_VALID_TO", length = 11, nullable = true)
    public Date getWmDrtValidTo() {
        return wmDrtValidTo == null ? null : new Date(wmDrtValidTo.getTime());
    }

    public void setWmDrtValidTo(Date wmDrtValidTo) {
        this.wmDrtValidTo = wmDrtValidTo == null ? null : new Date(wmDrtValidTo.getTime());
    }

    @Column(name = "WM_DRT_DELETED", length = 3, nullable = false)
    public boolean isWmDrtDeleted() {
        return wmDrtDeleted;
    }

    public void setWmDrtDeleted(boolean wmDrtDeleted) {
        this.wmDrtDeleted = wmDrtDeleted;
    }

    @Column(name = "WM_DRT_CATEGORY", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    public CategoryEn getWmDrtCategory() {
        return this.wmDrtCategory;
    }

    public void setWmDrtCategory(CategoryEn pCategory) {
        this.wmDrtCategory = pCategory;
    }

    /**
     *
     * @return wmDrtdescription : Beschreibung ders Typs der Dokumentenvorlage
     * eingegeben werden
     */
    @Column(name = "WM_DRT_DESCRIPTION", length = 200)
    public String getWmDrtDescription() {
        return this.wmDrtdescription;
    }

    /**
     *
     * @param wmDrtdescription Column DESCRIPTION : Beschreibung ders Typs der
     * Dokumentenvorlage eingegeben werden
     */
    public void setWmDrtDescription(String wmDrtdescription) {
        this.wmDrtdescription = wmDrtdescription;
    }

//    @Override
//    public int compareTo(final CWmListDraftType o) {
//        return StringUtils.trimToEmpty(this.getWmDrtName())
//                .compareToIgnoreCase(StringUtils.trimToEmpty(o.getWmDrtName()));
//    }
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (int) (this.wmDrtInternalId ^ (this.wmDrtInternalId >>> 32));
        hash = 31 * hash + Objects.hashCode(this.wmDrtName);
        hash = 31 * hash + Objects.hashCode(this.wmDrtSort);
        hash = 31 * hash + (this.wmDrtValid ? 1 : 0);
        hash = 31 * hash + Objects.hashCode(this.wmDrtValidFrom);
        hash = 31 * hash + Objects.hashCode(this.wmDrtValidTo);
        hash = 31 * hash + (this.wmDrtDeleted ? 1 : 0);
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
        final CWmListDraftType other = (CWmListDraftType) obj;
        if (this.wmDrtInternalId != other.wmDrtInternalId) {
            return false;
        }
        if (this.wmDrtValid != other.wmDrtValid) {
            return false;
        }
        if (this.wmDrtDeleted != other.wmDrtDeleted) {
            return false;
        }
        if (!Objects.equals(this.wmDrtName, other.wmDrtName)) {
            return false;
        }
        if (!Objects.equals(this.wmDrtSort, other.wmDrtSort)) {
            return false;
        }
        if (!Objects.equals(this.wmDrtValidFrom, other.wmDrtValidFrom)) {
            return false;
        }
        if (!Objects.equals(this.wmDrtValidTo, other.wmDrtValidTo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return wmDrtName;
    }

    @Transient
    @Override
    public String getName() {
        return MenuCacheEntity.getName(this, wmDrtName);
    }

    @Transient
    @Override
    public Long getMenuCacheId() {
        return wmDrtInternalId;
    }

    @Transient
    @Override
    public Date getValidFrom() {
        return wmDrtValidFrom;
    }

    @Transient
    @Override
    public Date getValidTo() {
        return wmDrtValidTo;
    }

    @Transient
    @Override
    public boolean isValid() {
        return wmDrtValid;
    }

    @Override
    public boolean isValid(final Date pDate) {
        return MenuCacheEntity.isValid(pDate, getValidFrom(), getValidTo(), isValid());
    }

    @Transient
    @Override
    public boolean isDeleted() {
        return wmDrtDeleted;
    }

    @Transient
    @Override
    public boolean isInActive() {
        return isInActive(new Date());
    }

    @Transient
    @Override
    public boolean isInActive(final Date pDate) {
        return isDeleted() || !isValid(pDate);
    }

    @Transient
    @Override
    public int getSort() {
        return Objects.requireNonNullElse(wmDrtSort, 0);
    }

}
