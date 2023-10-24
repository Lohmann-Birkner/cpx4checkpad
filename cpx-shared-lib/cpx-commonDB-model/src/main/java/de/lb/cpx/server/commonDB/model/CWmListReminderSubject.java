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
@Table(name = "C_WM_LIST_REMINDER_SUBJECT", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"WM_RS_INTERNAL_ID"})
})
@SuppressWarnings("serial")
public class CWmListReminderSubject extends AbstractEntity implements MenuCacheEntity {

    private static final long serialVersionUID = 1L;

    //    private long id;
    private long wmRsInternalId;
    private String wmRsName;
    private int wmRsSort;
    private boolean wmRsValid;
    private Date wmRsValidFrom;
    private Date wmRsValidTo;
    private boolean wmRsDeleted;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_WM_LIST_REMINDER_SUBJECT_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @SequenceGenerator(name = "default_gen", sequenceName = "C_WM_LIST_RS_INTERNAL_ID_SQ", allocationSize = 1)
    @Column(name = "WM_RS_INTERNAL_ID", unique = true, nullable = false)
    public long getWmRsInternalId() {
        return wmRsInternalId;
    }

    public void setWmRsInternalId(long wmRsInternalId) {
        this.wmRsInternalId = wmRsInternalId;
    }

    @Column(name = "WM_RS_NAME", nullable = false, length = 100)
    public String getWmRsName() {
        return wmRsName;
    }

    public void setWmRsName(String wmRsName) {
        this.wmRsName = wmRsName;
    }

    @Column(name = "WM_RS_SORT", nullable = false)
    public int getWmRsSort() {
        return wmRsSort;
    }

    public void setWmRsSort(int wmRsSort) {
        this.wmRsSort = wmRsSort;
    }

    @Column(name = "WM_RS_VALID", length = 3, nullable = false)
    public boolean getWmRsValid() {
        return wmRsValid;
    }

    public void setWmRsValid(boolean wmRsValid) {
        this.wmRsValid = wmRsValid;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "WM_RS_VALID_FROM", length = 11, nullable = false)
    public Date getWmRsValidFrom() {
        return wmRsValidFrom == null ? null : new Date(wmRsValidFrom.getTime());
    }

    public void setWmRsValidFrom(Date wmRsValidFrom) {
        this.wmRsValidFrom = wmRsValidFrom == null ? null : new Date(wmRsValidFrom.getTime());
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "WM_RS_VALID_TO", length = 11, nullable = false)
    public Date getWmRsValidTo() {
        return wmRsValidTo == null ? null : new Date(wmRsValidTo.getTime());
    }

    public void setWmRsValidTo(Date wmRsValidTo) {
        this.wmRsValidTo = wmRsValidTo == null ? null : new Date(wmRsValidTo.getTime());
    }

    @Column(name = "WM_RS_DELETED", length = 3, nullable = false)
    public boolean isWmRsDeleted() {
        return wmRsDeleted;
    }

    public void setWmRsDeleted(boolean wmRsDeleted) {
        this.wmRsDeleted = wmRsDeleted;
    }

//    @Override
//    public int compareTo(final CWmListReminderSubject o) {
//        return StringUtils.trimToEmpty(this.getWmRsName())
//                .compareToIgnoreCase(StringUtils.trimToEmpty(o.getWmRsName()));
//    }
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (this.wmRsInternalId ^ (this.wmRsInternalId >>> 32));
        hash = 59 * hash + Objects.hashCode(this.wmRsName);
        hash = 59 * hash + Objects.hashCode(this.wmRsSort);
        hash = 59 * hash + (this.wmRsValid ? 1 : 0);
        hash = 59 * hash + Objects.hashCode(this.wmRsValidFrom);
        hash = 59 * hash + Objects.hashCode(this.wmRsValidTo);
        hash = 59 * hash + (this.wmRsDeleted ? 1 : 0);
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
        final CWmListReminderSubject other = (CWmListReminderSubject) obj;
        if (this.wmRsInternalId != other.wmRsInternalId) {
            return false;
        }
        if (this.wmRsValid != other.wmRsValid) {
            return false;
        }
        if (this.wmRsDeleted != other.wmRsDeleted) {
            return false;
        }
        if (!Objects.equals(this.wmRsName, other.wmRsName)) {
            return false;
        }
        if (!Objects.equals(this.wmRsSort, other.wmRsSort)) {
            return false;
        }
        if (!Objects.equals(this.wmRsValidFrom, other.wmRsValidFrom)) {
            return false;
        }
        if (!Objects.equals(this.wmRsValidTo, other.wmRsValidTo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return wmRsName;
    }

    @Transient
    @Override
    public String getName() {
        return MenuCacheEntity.getName(this, wmRsName);
    }

    @Transient
    @Override
    public Long getMenuCacheId() {
        return wmRsInternalId;
    }

    @Transient
    @Override
    public Date getValidFrom() {
        return wmRsValidFrom;
    }

    @Transient
    @Override
    public Date getValidTo() {
        return wmRsValidTo;
    }

    @Transient
    @Override
    public boolean isValid() {
        return wmRsValid;
    }

    @Override
    public boolean isValid(final Date pDate) {
        return MenuCacheEntity.isValid(pDate, getValidFrom(), getValidTo(), isValid());
    }

    @Transient
    @Override
    public boolean isDeleted() {
        return wmRsDeleted;
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
        return wmRsSort;
    }

}
