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
@Table(name = "C_WM_LIST_ACTION_SUBJECT", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"WM_AS_INTERNAL_ID"})
})
@SuppressWarnings("serial")
public class CWmListActionSubject extends AbstractEntity implements MenuCacheEntity {

    private static final long serialVersionUID = 1L;

    //    private long id;
    private long wmAsInternalId;
    private String wmAsName;
    private int wmAsSort;
    private boolean wmAsValid;
    private Date wmAsValidFrom;
    private Date wmAsValidTo;
    private boolean wmAsDeleted;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_WM_LIST_ACTION_SUBJECT_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @SequenceGenerator(name = "default_gen", sequenceName = "C_WM_LIST_AS_INTERNAL_ID_SQ", allocationSize = 1)
    @Column(name = "WM_AS_INTERNAL_ID", unique = true, nullable = false)
    public long getWmAsInternalId() {
        return wmAsInternalId;
    }

    public void setWmAsInternalId(long wmAsInternalId) {
        this.wmAsInternalId = wmAsInternalId;
    }

    @Column(name = "WM_AS_NAME", nullable = false, length = 100)
    public String getWmAsName() {
        return wmAsName;
    }

    public void setWmAsName(String wmAsName) {
        this.wmAsName = wmAsName;
    }

    @Column(name = "WM_AS_SORT", nullable = false)
    public int getWmAsSort() {
        return wmAsSort;
    }

    public void setWmAsSort(int wmAsSort) {
        this.wmAsSort = wmAsSort;
    }

    @Column(name = "WM_AS_VALID", length = 3, nullable = false)
    public boolean getWmAsValid() {
        return wmAsValid;
    }

    public void setWmAsValid(boolean wmAsValid) {
        this.wmAsValid = wmAsValid;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "WM_AS_VALID_FROM", length = 11, nullable = false)
    public Date getWmAsValidFrom() {
        return wmAsValidFrom == null ? null : new Date(wmAsValidFrom.getTime());
    }

    public void setWmAsValidFrom(Date wmAsValidFrom) {
        this.wmAsValidFrom = wmAsValidFrom == null ? null : new Date(wmAsValidFrom.getTime());
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "WM_AS_VALID_TO", length = 11, nullable = false)
    public Date getWmAsValidTo() {
        return wmAsValidTo == null ? null : new Date(wmAsValidTo.getTime());
    }

    public void setWmAsValidTo(Date wmAsValidTo) {
        this.wmAsValidTo = wmAsValidTo == null ? null : new Date(wmAsValidTo.getTime());
    }

    @Column(name = "WM_AS_DELETED", length = 3, nullable = false)
    public boolean getWmAsDeleted() {
        return wmAsDeleted;
    }

    public void setWmAsDeleted(boolean wmAsDeleted) {
        this.wmAsDeleted = wmAsDeleted;
    }

//    @Override
//    public int compareTo(final CWmListActionSubject o) {
//        return StringUtils.trimToEmpty(this.getWmAsName())
//                .compareToIgnoreCase(StringUtils.trimToEmpty(o.getWmAsName()));
//    }
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (int) (this.wmAsInternalId ^ (this.wmAsInternalId >>> 32));
        hash = 13 * hash + Objects.hashCode(this.wmAsName);
        hash = 13 * hash + Objects.hashCode(this.wmAsSort);
        hash = 13 * hash + (this.wmAsValid ? 1 : 0);
        hash = 13 * hash + Objects.hashCode(this.wmAsValidFrom);
        hash = 13 * hash + Objects.hashCode(this.wmAsValidTo);
        hash = 13 * hash + (this.wmAsDeleted ? 1 : 0);
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
        final CWmListActionSubject other = (CWmListActionSubject) obj;
        if (this.wmAsInternalId != other.wmAsInternalId) {
            return false;
        }
        if (this.wmAsValid != other.wmAsValid) {
            return false;
        }
        if (this.wmAsDeleted != other.wmAsDeleted) {
            return false;
        }
        if (!Objects.equals(this.wmAsName, other.wmAsName)) {
            return false;
        }
        if (!Objects.equals(this.wmAsSort, other.wmAsSort)) {
            return false;
        }
        if (!Objects.equals(this.wmAsValidFrom, other.wmAsValidFrom)) {
            return false;
        }
        if (!Objects.equals(this.wmAsValidTo, other.wmAsValidTo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return wmAsName;
    }

    @Transient
    @Override
    public String getName() {
        return MenuCacheEntity.getName(this, wmAsName);
    }

    @Transient
    @Override
    public Long getMenuCacheId() {
        return wmAsInternalId;
    }

    @Transient
    @Override
    public Date getValidFrom() {
        return wmAsValidFrom;
    }

    @Transient
    @Override
    public Date getValidTo() {
        return wmAsValidTo;
    }

    @Transient
    @Override
    public boolean isValid() {
        return wmAsValid;
    }

    @Override
    public boolean isValid(final Date pDate) {
        return MenuCacheEntity.isValid(pDate, getValidFrom(), getValidTo(), isValid());
    }

    @Transient
    @Override
    public boolean isDeleted() {
        return wmAsDeleted;
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
        return wmAsSort;
    }

}
