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

import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * CdbUser2Role initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">CDB_USER_2_ROLE : Mapping der
 * Benutzereinträgen zu Rollen </p>
 */
@Entity
@Table(name = "CDB_USER_2_ROLE")
@SuppressWarnings("serial")
public class CdbUser2Role extends AbstractEntity {

    private static final long serialVersionUID = 1L;

//     private long id;
    private CdbUserRoles cdbUserRoles;
    private CdbUsers cdbUsers;
    private boolean u2rIsActual;
    private Date u2rValidFrom;
    private Date u2rValidTo;
//    private Set<CdbFilter> cdbFilters = new HashSet<>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "CDB_USER_2_ROLE_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return cdbUserRoles: Referenz auf die Tabelle CDB_USER_ROLES
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CDBUR_ID", nullable = false)
    public CdbUserRoles getCdbUserRoles() {
        return this.cdbUserRoles;
    }

    /**
     *
     * @param cdbUserRoles Column CDBUR_ID :Referenz auf die Tabelle
     * CDB_USER_ROLES
     */
    public void setCdbUserRoles(CdbUserRoles cdbUserRoles) {
        this.cdbUserRoles = cdbUserRoles;
    }

    /**
     *
     * @return cdbUsers: Referenz auf die Tabelle CDB_USERS
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CDBU_ID", nullable = false)
    public CdbUsers getCdbUsers() {
        return this.cdbUsers;
    }

    /**
     *
     * @param cdbUsers Column CDBU_ID :Referenz auf die Tabelle CDB_USERS
     */
    public void setCdbUsers(CdbUsers cdbUsers) {
        this.cdbUsers = cdbUsers;
    }

    /**
     *
     * @return u2rIsActual:Flag = 1 - Aktuelle Rolle des Benutzers, mit der er
     * CPX verlassen hat
     */
    @Column(name = "U2R_IS_ACTUAL", nullable = false, precision = 1, scale = 0)
    public boolean isU2rIsActual() {
        return this.u2rIsActual;
    }

    /**
     *
     * @param u2rIsActual Column U2R_IS_ACTUAL :Flag = 1 - Aktuelle Rolle des
     * Benutzers, mit der er CPX verlassen hat
     */
    public void setU2rIsActual(boolean u2rIsActual) {
        this.u2rIsActual = u2rIsActual;
    }

    /**
     *
     * @return u2rValidFrom: Anfang des Gültigkeitsbereichs
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "U2R_VALID_FROM", length = 7)
    public Date getU2rValidFrom() {
        return u2rValidFrom == null ? null : new Date(u2rValidFrom.getTime());
    }

    /**
     *
     * @param u2rValidFrom Column U2R_VALID_FROM :Anfang des Gültigkeitsbereichs
     */
    public void setU2rValidFrom(Date u2rValidFrom) {
        this.u2rValidFrom = u2rValidFrom == null ? null : new Date(u2rValidFrom.getTime());
    }

    /**
     *
     * @return U2R_VALID_TO : Ende des Gültigkeitsbereichs
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "U2R_VALID_TO", length = 7)
    public Date getU2rValidTo() {
        return u2rValidTo == null ? null : new Date(u2rValidTo.getTime());
    }

    /**
     *
     * @param u2rValidTo Column U2R_VALID_TO: Ende des Gültigkeitsbereichs
     */
    public void setU2rValidTo(Date u2rValidTo) {
        this.u2rValidTo = u2rValidTo == null ? null : new Date(u2rValidTo.getTime());
    }

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cdbUser2Role")
//    public Set<CdbFilter> getCdbFilters() {
//        return this.cdbFilters;
//    }
//
//    public void setCdbFilters(Set<CdbFilter> cdbFilters) {
//        this.cdbFilters = cdbFilters;
//    }

    @Transient
    public boolean isValid() {
        final Date lNow = new Date();
        if (u2rValidFrom != null && u2rValidFrom.after(lNow)) {
            //Rolle noch nicht gültig
            return false;
        }
        if (u2rValidTo != null && u2rValidTo.before(lNow)) {
            //Rolle nicht mehr gültig
            return false;
        }
        return true;
    }

}
