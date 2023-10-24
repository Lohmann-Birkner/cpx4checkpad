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
package de.lb.cpx.server.commonDB.model;

import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.server.commons.dao.MenuCacheEntity;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 * @author sklarow
 */
@Entity
@Table(name = "C_WM_LIST_DOCUMENT_TYPE", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"WM_DT_INTERNAL_ID"})
})
@SuppressWarnings("serial")
public class CWmListDocumentType extends AbstractEntity implements MenuCacheEntity {

    private static final long serialVersionUID = 1L;

    //    private long id;
    private long wmDtInternalId;
    private String wmDtName;
    private int wmDtSort;
    private boolean wmDtValid;
    private Date wmDtValidFrom;
    private Date wmDtValidTo;
    private boolean wmDtDeleted;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_WM_LIST_DOCUMENT_TYPE_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @SequenceGenerator(name = "default_gen", sequenceName = "C_WM_LIST_DT_INTERNAL_ID_SQ", allocationSize = 1)
    @Column(name = "WM_DT_INTERNAL_ID", unique = true, nullable = false)
    public long getWmDtInternalId() {
        return wmDtInternalId;
    }

    public void setWmDtInternalId(long wmDtInternalId) {
        this.wmDtInternalId = wmDtInternalId;
    }

    @Column(name = "WM_DT_NAME", nullable = false, length = 100)
    public String getWmDtName() {
        return wmDtName;
    }

    public void setWmDtName(String wmDtName) {
        this.wmDtName = wmDtName;
    }

    @Column(name = "WM_DT_SORT", nullable = false)
    public int getWmDtSort() {
        return wmDtSort;
    }

    public void setWmDtSort(int wmDtSort) {
        this.wmDtSort = wmDtSort;
    }

    @Column(name = "WM_DT_VALID", length = 3, nullable = false)
    public boolean getWmDtValid() {
        return wmDtValid;
    }

    public void setWmDtValid(boolean wmDtValid) {
        this.wmDtValid = wmDtValid;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "WM_DT_VALID_FROM", length = 11, nullable = false)
    public Date getWmDtValidFrom() {
        return wmDtValidFrom == null ? null : new Date(wmDtValidFrom.getTime());
    }

    public void setWmDtValidFrom(Date wmDtValidFrom) {
        this.wmDtValidFrom = wmDtValidFrom == null ? null : new Date(wmDtValidFrom.getTime());
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "WM_DT_VALID_TO", length = 11, nullable = false)
    public Date getWmDtValidTo() {
        return wmDtValidTo == null ? null : new Date(wmDtValidTo.getTime());
    }

    public void setWmDtValidTo(Date wmDtValidTo) {
        this.wmDtValidTo = wmDtValidTo == null ? null : new Date(wmDtValidTo.getTime());
    }

    @Column(name = "WM_DT_DELETED", length = 3, nullable = false)
    public boolean isWmDtDeleted() {
        return wmDtDeleted;
    }

    public void setWmDtDeleted(boolean wmDtDeleted) {
        this.wmDtDeleted = wmDtDeleted;
    }

//    @Override
//    public int compareTo(final CWmListDocumentType o) {
//        return StringUtils.trimToEmpty(this.getWmDtName())
//                .compareToIgnoreCase(StringUtils.trimToEmpty(o.getWmDtName()));
//    }
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (int) (this.wmDtInternalId ^ (this.wmDtInternalId >>> 32));
        hash = 31 * hash + Objects.hashCode(this.wmDtName);
        hash = 31 * hash + Objects.hashCode(this.wmDtSort);
        hash = 31 * hash + (this.wmDtValid ? 1 : 0);
        hash = 31 * hash + Objects.hashCode(this.wmDtValidFrom);
        hash = 31 * hash + Objects.hashCode(this.wmDtValidTo);
        hash = 31 * hash + (this.wmDtDeleted ? 1 : 0);
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
        final CWmListDocumentType other = (CWmListDocumentType) obj;
        if (this.wmDtInternalId != other.wmDtInternalId) {
            return false;
        }
        if (this.wmDtValid != other.wmDtValid) {
            return false;
        }
        if (this.wmDtDeleted != other.wmDtDeleted) {
            return false;
        }
        if (!Objects.equals(this.wmDtName, other.wmDtName)) {
            return false;
        }
        if (!Objects.equals(this.wmDtSort, other.wmDtSort)) {
            return false;
        }
        if (!Objects.equals(this.wmDtValidFrom, other.wmDtValidFrom)) {
            return false;
        }
        if (!Objects.equals(this.wmDtValidTo, other.wmDtValidTo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return wmDtName;
    }

    @Transient
    @Override
    public String getName() {
        return MenuCacheEntity.getName(this, wmDtName);
    }

    @Transient
    @Override
    public Long getMenuCacheId() {
        return wmDtInternalId;
    }

    @Transient
    @Override
    public Date getValidFrom() {
        return wmDtValidFrom;
    }

    @Transient
    @Override
    public Date getValidTo() {
        return wmDtValidTo;
    }

    @Transient
    @Override
    public boolean isValid() {
        return wmDtValid;
    }

    @Override
    public boolean isValid(final Date pDate) {
        return MenuCacheEntity.isValid(pDate, getValidFrom(), getValidTo(), isValid());
    }

    @Transient
    @Override
    public boolean isDeleted() {
        return wmDtDeleted;
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
        return wmDtSort;
    }

}
