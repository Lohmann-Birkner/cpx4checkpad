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
 *    2018  shahin - initial API and implementation and/or initial documentation
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
 * @author shahin
 */
@Entity
@Table(name = "C_WM_LIST_MDK_STATE", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"WM_MS_INTERNAL_ID"})
})
@SuppressWarnings("serial")
public class CWmListMdkState extends AbstractEntity implements MenuCacheEntity {

    private static final long serialVersionUID = 1L;

    private long wmMsInternalId;
    private String wmMsName;
    private int wmMsSort;
    private boolean wmMsValid;
    private Date wmMsValidFrom;
    private Date wmMsValidTo;
    private boolean wmMsDeleted;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_WM_LIST_MDK_STATE_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @SequenceGenerator(name = "default_gen", sequenceName = "C_WM_LIST_MS_INTERNAL_ID_SQ", allocationSize = 1)
    @Column(name = "WM_MS_INTERNAL_ID", unique = true, nullable = false)
    public long getWmMsInternalId() {
        return wmMsInternalId;
    }

    public void setWmMsInternalId(long wmMsInternalId) {
        this.wmMsInternalId = wmMsInternalId;
    }

    @Column(name = "WM_MS_NAME", nullable = false, length = 100)
    public String getWmMsName() {
        return wmMsName;
    }

    public void setWmMsName(String wmMsName) {
        this.wmMsName = wmMsName;
    }

    @Column(name = "WM_MS_SORT", nullable = false)
    public int getWmMsSort() {
        return wmMsSort;
    }

    public void setWmMsSort(int wmMsSort) {
        this.wmMsSort = wmMsSort;
    }

    @Column(name = "WM_MS_VALID", length = 3, nullable = false)
    public boolean getWmMsValid() {
        return wmMsValid;
    }

    public void setWmMsValid(boolean wmMsValid) {
        this.wmMsValid = wmMsValid;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "WM_MS_VALID_FROM", length = 11, nullable = false)
    public Date getWmMsValidFrom() {
        return wmMsValidFrom == null ? null : new Date(wmMsValidFrom.getTime());
    }

    public void setWmMsValidFrom(Date wmMsValidFrom) {
        this.wmMsValidFrom = wmMsValidFrom == null ? null : new Date(wmMsValidFrom.getTime());
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "WM_MS_VALID_TO", length = 11, nullable = false)
    public Date getWmMsValidTo() {
        return wmMsValidTo == null ? null : new Date(wmMsValidTo.getTime());
    }

    public void setWmMsValidTo(Date wmMsValidTo) {
        this.wmMsValidTo = wmMsValidTo == null ? null : new Date(wmMsValidTo.getTime());
    }

    @Column(name = "WM_MS_DELETED", length = 3, nullable = false)
    public boolean getWmMsDeleted() {
        return wmMsDeleted;
    }

    public void setWmMsDeleted(boolean wmMsDeleted) {
        this.wmMsDeleted = wmMsDeleted;
    }

//    @Override
//    public int compareTo(CWmListMdkState o) {
//        return StringUtils.trimToEmpty(this.getWmMsName())
//                .compareToIgnoreCase(StringUtils.trimToEmpty(o.getWmMsName()));
//    }
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (int) (this.wmMsInternalId ^ (this.wmMsInternalId >>> 32));
        hash = 29 * hash + Objects.hashCode(this.wmMsName);
        hash = 29 * hash + Objects.hashCode(this.wmMsSort);
        hash = 29 * hash + (this.wmMsValid ? 1 : 0);
        hash = 29 * hash + Objects.hashCode(this.wmMsValidFrom);
        hash = 29 * hash + Objects.hashCode(this.wmMsValidTo);
        hash = 29 * hash + (this.wmMsDeleted ? 1 : 0);
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
        final CWmListMdkState other = (CWmListMdkState) obj;
        if (this.wmMsInternalId != other.wmMsInternalId) {
            return false;
        }
        if (this.wmMsValid != other.wmMsValid) {
            return false;
        }
        if (this.wmMsDeleted != other.wmMsDeleted) {
            return false;
        }
        if (!Objects.equals(this.wmMsName, other.wmMsName)) {
            return false;
        }
        if (!Objects.equals(this.wmMsSort, other.wmMsSort)) {
            return false;
        }
        if (!Objects.equals(this.wmMsValidFrom, other.wmMsValidFrom)) {
            return false;
        }
        if (!Objects.equals(this.wmMsValidTo, other.wmMsValidTo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return wmMsName;
    }

    @Transient
    @Override
    public String getName() {
        return MenuCacheEntity.getName(this, wmMsName);
    }

    @Transient
    @Override
    public Long getMenuCacheId() {
        return wmMsInternalId;
    }

    @Transient
    @Override
    public Date getValidFrom() {
        return wmMsValidFrom;
    }

    @Transient
    @Override
    public Date getValidTo() {
        return wmMsValidTo;
    }

    @Transient
    @Override
    public boolean isValid() {
        return wmMsValid;
    }

    @Override
    public boolean isValid(final Date pDate) {
        return MenuCacheEntity.isValid(pDate, getValidFrom(), getValidTo(), isValid());
    }

    @Transient
    @Override
    public boolean isDeleted() {
        return wmMsDeleted;
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
        return wmMsSort;
    }

}
