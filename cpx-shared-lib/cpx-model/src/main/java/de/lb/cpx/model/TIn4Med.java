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
package de.lb.cpx.model;

import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * CAtc initially generated at 03.02.2016 10:32:45 by Hibernate Tools 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;"> C_ATC: Tabelle der ATC
 * (Anatomisch-Therapeutisch-Chemisches Klassifikationssystem).</p>
 *
 *
 */
@Entity
@Table(name = "IN4MED")
@SuppressWarnings("serial")
public class TIn4Med extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    //private long id = 1;
    private Date lastUpdate;
    private boolean isNewDb;
    private String createdBy;

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    //@SequenceGenerator(name = "default_gen", sequenceName = "C_IN4MED_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * Last update
     *
     * @return feecFrom
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "LAST_UPDATE", length = 7)
    public Date getLastUpdate() {
        return lastUpdate == null ? null : new Date(lastUpdate.getTime());
    }

    public void setLastUpdate(final Date pDate) {
        this.lastUpdate = pDate == null ? null : new Date(pDate.getTime());
    }

    /**
     * Gibt an, ob die Datenbank neu angelegt wurde, nein=0,ja=1.
     *
     * @return isNewDb
     */
    @Column(name = "IS_NEW_DB", nullable = false)
    public boolean isNewDb() {
        return isNewDb;
    }

    /**
     *
     * @param isNewDb Column IS_NEW_DB: Wurde die Datenbank neu angelegt?
     * nein=0,ja=1.
     */
    public void setNewDb(boolean isNewDb) {
        this.isNewDb = isNewDb;
    }

    @Column(name = "CREATED_BY", nullable = true, length = 50)
    public String getCreatedBy() {
        return this.createdBy;
    }

    /**
     *
     * @param createdBy created by this user name
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

}
