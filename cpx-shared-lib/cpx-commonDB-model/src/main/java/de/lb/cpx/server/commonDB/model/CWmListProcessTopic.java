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
@Table(name = "C_WM_LIST_PROCESS_TOPIC",
        uniqueConstraints = @UniqueConstraint(columnNames = {"WM_PT_INTERNAL_ID"}))
@SuppressWarnings("serial")
public class CWmListProcessTopic extends AbstractEntity implements MenuCacheEntity {

    private static final long serialVersionUID = 1L;

    //    private long id;
    private long wmPtInternalId;
    private String wmPtName;
    private int wmPtSort;
    private boolean wmPtValid;
    private Date wmPtValidFrom;
    private Date wmPtValidTo;
    private boolean wmPtDeleted;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_WM_LIST_PROCESS_TOPIC_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @SequenceGenerator(name = "default_gen", sequenceName = "C_WM_LIST_PT_INTERNAL_ID_SQ", allocationSize = 1)
    @Column(name = "WM_PT_INTERNAL_ID", unique = true, nullable = false)
    public long getWmPtInternalId() {
        return wmPtInternalId;
    }

    public void setWmPtInternalId(long wmPtInternalId) {
        this.wmPtInternalId = wmPtInternalId;
    }

    @Column(name = "WM_PT_NAME", nullable = false, length = 100)
    public String getWmPtName() {
        return wmPtName;
    }

    public void setWmPtName(String wmPtName) {
        this.wmPtName = wmPtName;
    }

    @Column(name = "WM_PT_SORT", nullable = false)
    public int getWmPtSort() {
        return wmPtSort;
    }

    public void setWmPtSort(int wmPtSort) {
        this.wmPtSort = wmPtSort;
    }

    @Column(name = "WM_PT_VALID", length = 3, nullable = false)
    public boolean getWmPtValid() {
        return wmPtValid;
    }

    public void setWmPtValid(boolean wmPtValid) {
        this.wmPtValid = wmPtValid;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "WM_PT_VALID_FROM", length = 11, nullable = false)
    public Date getWmPtValidFrom() {
        return wmPtValidFrom == null ? null : new Date(wmPtValidFrom.getTime());
    }

    public void setWmPtValidFrom(Date wmPtValidFrom) {
        this.wmPtValidFrom = wmPtValidFrom == null ? null : new Date(wmPtValidFrom.getTime());
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "WM_PT_VALID_TO", length = 11, nullable = false)
    public Date getWmPtValidTo() {
        return wmPtValidTo == null ? null : new Date(wmPtValidTo.getTime());
    }

    public void setWmPtValidTo(Date wmPtValidTo) {
        this.wmPtValidTo = wmPtValidTo == null ? null : new Date(wmPtValidTo.getTime());
    }

    @Column(name = "WM_PT_DELETED", length = 3, nullable = false)
    public boolean isWmPtDeleted() {
        return wmPtDeleted;
    }

    public void setWmPtDeleted(boolean wmPtDeleted) {
        this.wmPtDeleted = wmPtDeleted;
    }

//    @Override
//    public int compareTo(final CWmListProcessTopic o) {
//        return StringUtils.trimToEmpty(this.getWmPtName())
//                .compareToIgnoreCase(StringUtils.trimToEmpty(o.getWmPtName()));
//    }
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (int) (this.wmPtInternalId ^ (this.wmPtInternalId >>> 32));
        hash = 71 * hash + Objects.hashCode(this.wmPtName);
        hash = 71 * hash + Objects.hashCode(this.wmPtSort);
        hash = 71 * hash + (this.wmPtValid ? 1 : 0);
        hash = 71 * hash + Objects.hashCode(this.wmPtValidFrom);
        hash = 71 * hash + Objects.hashCode(this.wmPtValidTo);
        hash = 71 * hash + (this.wmPtDeleted ? 1 : 0);
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
        final CWmListProcessTopic other = (CWmListProcessTopic) obj;
        if (this.wmPtInternalId != other.wmPtInternalId) {
            return false;
        }
        if (this.wmPtValid != other.wmPtValid) {
            return false;
        }
        if (this.wmPtDeleted != other.wmPtDeleted) {
            return false;
        }
        if (!Objects.equals(this.wmPtName, other.wmPtName)) {
            return false;
        }
        if (!Objects.equals(this.wmPtSort, other.wmPtSort)) {
            return false;
        }
        if (!Objects.equals(this.wmPtValidFrom, other.wmPtValidFrom)) {
            return false;
        }
        if (!Objects.equals(this.wmPtValidTo, other.wmPtValidTo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return wmPtName;
    }

    @Transient
    @Override
    public String getName() {
        return MenuCacheEntity.getName(this, wmPtName);
    }

    @Transient
    @Override
    public Long getMenuCacheId() {
        return wmPtInternalId;
    }

    @Transient
    @Override
    public Date getValidFrom() {
        return wmPtValidFrom;
    }

    @Transient
    @Override
    public Date getValidTo() {
        return wmPtValidTo;
    }

    @Transient
    @Override
    public boolean isValid() {
        return wmPtValid;
    }

    @Override
    public boolean isValid(final Date pDate) {
        return MenuCacheEntity.isValid(pDate, getValidFrom(), getValidTo(), isValid());
    }

    @Transient
    @Override
    public boolean isDeleted() {
        return wmPtDeleted;
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
        return wmPtSort;
    }

}
