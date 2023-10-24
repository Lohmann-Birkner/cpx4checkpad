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
@Table(name = "C_WM_LIST_PROCESS_RESULT", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"WM_PR_INTERNAL_ID"})
})
@SuppressWarnings("serial")
public class CWmListProcessResult extends AbstractEntity implements MenuCacheEntity {

    private static final long serialVersionUID = 1L;

    //    private long id;
    private long wmPrInternalId;
    private String wmPrName;
    private int wmPrSort;
    private boolean wmPrValid;
    private Date wmPrValidFrom;
    private Date wmPrValidTo;
    private boolean wmPrDeleted;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_WM_LIST_PROCESS_RESULT_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @SequenceGenerator(name = "default_gen", sequenceName = "C_WM_LIST_PR_INTERNAL_ID_SQ", allocationSize = 1)
    @Column(name = "WM_PR_INTERNAL_ID", unique = true, nullable = false)
    public long getWmPrInternalId() {
        return wmPrInternalId;
    }

    public void setWmPrInternalId(long wmPrInternalId) {
        this.wmPrInternalId = wmPrInternalId;
    }

    @Column(name = "WM_PR_NAME", nullable = false, length = 100)
    public String getWmPrName() {
        return wmPrName;
    }

    public void setWmPrName(String wmPrName) {
        this.wmPrName = wmPrName;
    }

    @Column(name = "WM_PR_SORT", nullable = false)
    public int getWmPrSort() {
        return wmPrSort;
    }

    public void setWmPrSort(int wmPrSort) {
        this.wmPrSort = wmPrSort;
    }

    @Column(name = "WM_PR_VALID", length = 3, nullable = false)
    public boolean getWmPrValid() {
        return wmPrValid;
    }

    public void setWmPrValid(boolean wmPrValid) {
        this.wmPrValid = wmPrValid;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "WM_PR_VALID_FROM", length = 11, nullable = false)
    public Date getWmPrValidFrom() {
        return wmPrValidFrom == null ? null : new Date(wmPrValidFrom.getTime());
    }

    public void setWmPrValidFrom(Date wmPrValidFrom) {
        this.wmPrValidFrom = wmPrValidFrom == null ? null : new Date(wmPrValidFrom.getTime());
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "WM_PR_VALID_TO", length = 11, nullable = false)
    public Date getWmPrValidTo() {
        return wmPrValidTo == null ? null : new Date(wmPrValidTo.getTime());
    }

    public void setWmPrValidTo(Date wmPrValidTo) {
        this.wmPrValidTo = wmPrValidTo == null ? null : new Date(wmPrValidTo.getTime());
    }

    @Column(name = "WM_PR_DELETED", length = 3, nullable = false)
    public boolean getWmPrDeleted() {
        return wmPrDeleted;
    }

    public void setWmPrDeleted(boolean wmPrDeleted) {
        this.wmPrDeleted = wmPrDeleted;
    }

//    @Override
//    public int compareTo(final CWmListProcessResult o) {
//        return StringUtils.trimToEmpty(this.getWmPrName())
//                .compareToIgnoreCase(StringUtils.trimToEmpty(o.getWmPrName()));
//    }
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (int) (this.wmPrInternalId ^ (this.wmPrInternalId >>> 32));
        hash = 41 * hash + Objects.hashCode(this.wmPrName);
        hash = 41 * hash + Objects.hashCode(this.wmPrSort);
        hash = 41 * hash + (this.wmPrValid ? 1 : 0);
        hash = 41 * hash + Objects.hashCode(this.wmPrValidFrom);
        hash = 41 * hash + Objects.hashCode(this.wmPrValidTo);
        hash = 41 * hash + (this.wmPrDeleted ? 1 : 0);
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
        final CWmListProcessResult other = (CWmListProcessResult) obj;
        if (this.wmPrInternalId != other.wmPrInternalId) {
            return false;
        }
        if (this.wmPrValid != other.wmPrValid) {
            return false;
        }
        if (this.wmPrDeleted != other.wmPrDeleted) {
            return false;
        }
        if (!Objects.equals(this.wmPrName, other.wmPrName)) {
            return false;
        }
        if (!Objects.equals(this.wmPrSort, other.wmPrSort)) {
            return false;
        }
        if (!Objects.equals(this.wmPrValidFrom, other.wmPrValidFrom)) {
            return false;
        }
        if (!Objects.equals(this.wmPrValidTo, other.wmPrValidTo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return wmPrName;
    }

    @Transient
    @Override
    public String getName() {
        return MenuCacheEntity.getName(this, wmPrName);
    }

    @Transient
    @Override
    public Long getMenuCacheId() {
        return wmPrInternalId;
    }

    @Transient
    @Override
    public Date getValidFrom() {
        return wmPrValidFrom;
    }

    @Transient
    @Override
    public Date getValidTo() {
        return wmPrValidTo;
    }

    @Transient
    @Override
    public boolean isValid() {
        return wmPrValid;
    }

    @Override
    public boolean isValid(final Date pDate) {
        return MenuCacheEntity.isValid(pDate, getValidFrom(), getValidTo(), isValid());
    }

    @Transient
    @Override
    public boolean isDeleted() {
        return wmPrDeleted;
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
        return wmPrSort;
    }

}
