/*
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

import de.lb.cpx.server.commonDB.model.rules.CrgRule2RoleDev;
import de.lb.cpx.server.commonDB.model.rules.CrgRule2RoleProd;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.server.commons.dao.MenuCacheEntity;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.annotation.security.PermitAll;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * CdbUserRoles initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">CRG_RULES : Tabelle definiert die
 * Regel </p>
 */
@Entity
@Table(name = "CDB_USER_ROLES")
@SuppressWarnings("serial")
@PermitAll
public class CdbUserRoles extends AbstractEntity implements MenuCacheEntity {

    private static final long serialVersionUID = 1L;

//     private long id;
    private boolean cdburIsvalid;
    private Date cdburValidFrom;
    private Date cdburValidTo;
    private Set<CrgRule2RoleProd> crgRule2RolesProd = new HashSet<>(0);
    private Set<CrgRule2RoleDev> crgRule2RolesDev = new HashSet<>(0);
    private Set<CdbUser2Role> cdbUser2Roles = new HashSet<>(0);
    private String cdburName;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "CDB_USER_ROLES_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return cdburIsvalid: Flag = 1 wenn die Rolle gültig ist
     */
    @Column(name = "CDBUR_ISVALID", nullable = false, precision = 1, scale = 0)
    public boolean isCdburIsvalid() {
        return this.cdburIsvalid;
    }

    /**
     *
     * @param cdburIsvalid Column CDBUR_ISVALID :Flag = 1 wenn die Rolle gültig
     * ist
     */
    public void setCdburIsvalid(boolean cdburIsvalid) {
        this.cdburIsvalid = cdburIsvalid;
    }

    /**
     *
     * @return cdburValidFrom:Anfang des Gültigkeitsraumes
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CDBUR_VALID_FROM", length = 7)
    public Date getCdburValidFrom() {
        return cdburValidFrom == null ? null : new Date(cdburValidFrom.getTime());
    }

    /**
     *
     * @param cdburValidFrom Column CDBUR_VALID_FROM: Anfang des
     * Gültigkeitsraumes
     */
    public void setCdburValidFrom(Date cdburValidFrom) {
        this.cdburValidFrom = cdburValidFrom == null ? null : new Date(cdburValidFrom.getTime());
    }

    /**
     *
     * @return cdburValidTo:Ende der Gültigkeitsraumes
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CDBUR_VALID_TO", length = 7)
    public Date getCdburValidTo() {
        return this.cdburValidTo == null ? null : new Date(cdburValidTo.getTime());
    }

    /**
     *
     * @param cdburValidTo Column CDBUR_VALID_TO: Ende der Gültigkeitsraumes
     */
    public void setCdburValidTo(Date cdburValidTo) {
        this.cdburValidTo = cdburValidTo == null ? null : new Date(cdburValidTo.getTime());
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cdbUserRoles")
    public Set<CrgRule2RoleProd> getCrgRule2RolesProd() {
        return this.crgRule2RolesProd;
    }

    public void setCrgRule2RolesProd(Set<CrgRule2RoleProd> crgRule2Roles) {
        this.crgRule2RolesProd = crgRule2Roles;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cdbUserRoles")
    public Set<CrgRule2RoleDev> getCrgRule2RolesDev() {
        return this.crgRule2RolesDev;
    }

    public void setCrgRule2RolesDev(Set<CrgRule2RoleDev> crgRule2Roles) {
        this.crgRule2RolesDev = crgRule2Roles;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cdbUserRoles")
    public Set<CdbUser2Role> getCdbUser2Roles() {
        return this.cdbUser2Roles;
    }

    public void setCdbUser2Roles(Set<CdbUser2Role> cdbUser2Roles) {
        this.cdbUser2Roles = cdbUser2Roles;
    }

    public void setCdburName(String pCdburName_String) {
        this.cdburName = pCdburName_String;
    }

    /**
     *
     * @return cdburName: Name der Role
     */
    @Column(name = "CDBUR_NAME", nullable = true, length = 255)
    public String getCdburName() {
        return this.cdburName;
    }

//    @Override
//    public int compareTo(final CdbUserRoles o) {
//        return StringUtils.trimToEmpty(this.getCdburName())
//                .compareToIgnoreCase(StringUtils.trimToEmpty(o.getCdburName()));
//    }
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.cdburIsvalid ? 1 : 0);
        hash = 97 * hash + Objects.hashCode(this.cdburValidFrom);
        hash = 97 * hash + Objects.hashCode(this.cdburValidTo);
        hash = 97 * hash + Objects.hashCode(this.cdburName);
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
        final CdbUserRoles other = (CdbUserRoles) obj;
        if (this.cdburIsvalid != other.cdburIsvalid) {
            return false;
        }
        if (!Objects.equals(this.cdburName, other.cdburName)) {
            return false;
        }
        if (!Objects.equals(this.cdburValidFrom, other.cdburValidFrom)) {
            return false;
        }
        if (!Objects.equals(this.cdburValidTo, other.cdburValidTo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
//        return cdburName + " (" + id + ")";
        String roleId = String.valueOf(this.id);
        final String hash = Integer.toHexString(this.hashCode());
        return "role_" + cdburName + ";id_" + roleId + "@" + hash;
    }

    @Transient
    @Override
    public String getName() {
        return MenuCacheEntity.getName(this, cdburName);
    }

    @Transient
    @Override
    public Long getMenuCacheId() {
        return id;
    }

    @Transient
    @Override
    public Date getValidFrom() {
        return cdburValidFrom;
    }

    @Transient
    @Override
    public Date getValidTo() {
        return cdburValidTo;
    }

    @Transient
    @Override
    public boolean isValid() {
        return cdburIsvalid;
    }

    @Override
    public boolean isValid(final Date pDate) {
        return MenuCacheEntity.isValid(pDate, getValidFrom(), getValidTo(), isValid());
    }

    @Transient
    @Override
    public boolean isDeleted() {
        return false; //why is there no cdburIsDeleted field?
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
        return 0; //does not exist for role!
    }

//    @Transient
//    public boolean isValid() {
//        if (!cdburIsvalid) {
//            return false;
//        }
//        final Date lNow = new Date();
//        if (cdburValidFrom != null && cdburValidFrom.after(lNow)) {
//            //Rolle noch nicht gültig
//            return false;
//        }
//        if (cdburValidTo != null && cdburValidTo.before(lNow)) {
//            //Rolle nicht mehr gültig
//            return false;
//        }
//        return true;
//    }
}
