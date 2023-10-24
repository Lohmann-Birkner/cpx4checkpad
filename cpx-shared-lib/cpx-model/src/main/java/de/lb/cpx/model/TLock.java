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
package de.lb.cpx.model;

//import de.lb.cpx.model.converter.CsCaseTypeConverter;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * TCase initially generated at 21.01.2016 17:07:59 by Hibernate Tools 3.2.2.GA
 * <p style="font-size:1em; color:green;"> "T_LOCK" enthält die Sperren für
 * einzelne Fälle oder für die ganze Datenbank </p>
 */
@Entity
@Table(name = "T_LOCK", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"CASE_ID", "PROCESS_ID"}, name = "Uni_Id"), //Each case can only have one lock!
})
@SuppressWarnings("serial")
public class TLock extends AbstractEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    //private long id;
    private long caseId;
    private long processId;
    private Date since;
    private Date expires;
    private Long userId;
    private String userName;
    private String cause;
    private String clientId;

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    //@SequenceGenerator(name = "default_gen", sequenceName = "T_LOCK_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    /**
     * Gibt Verweis auf die Tabelle T_CASE zurück,d.h. Jeder Fall kann nur ein
     * Schloss haben
     *
     * @return caseId
     */
    @Column(name = "CASE_ID", nullable = false)
    public long getCaseId() {
        return caseId;
    }

    /**
     * @param caseId Column CASE_ID: Verweis auf die Tablle T_CASE . Jeder Fall
     * kann nur ein Schloss haben! .
     */
    public void setCaseId(final long caseId) {
        this.caseId = caseId;
    }

    /**
     * Gibt Verweis auf die Tabelle T_WM_PROCESS zurück,d.h. Jeder Vorgang kann
     * nur ein Schloss haben
     *
     * @return processId
     */
    @Column(name = "PROCESS_ID", nullable = false)
    public long getProcessId() {
        return processId;
    }

    /**
     * @param processId Column PROCESS_ID: Verweis auf die Tablle T_WM_PROCESS .
     * Jeder Vorgang kann nur ein Schloss haben! .
     */
    public void setProcessId(final long processId) {
        this.processId = processId;
    }

    /**
     * Gibt Datum zurück , ab dem der Fall gesperrt ist .
     *
     * @return since:
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SINCE", length = 7, nullable = false)
    public Date getSince() {
        return since == null ? null : new Date(since.getTime());
    }

    /**
     * @param since Column SINCE: Der Fall ist ab diesem Datum gesperrt.
     */
    public void setSince(Date since) {
        this.since = since == null ? null : new Date(since.getTime());
    }

    /**
     * Gibt Datum zurück , bis dem der Fall gesperrt ist .
     *
     * @return expires
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXPIRES", length = 7, nullable = true)
    public Date getExpires() {
        return expires == null ? null : new Date(expires.getTime());
    }

    /**
     * @param expires Column EXPIRES: Der Fall ist bis diesem Datum gesperrt.
     */
    public void setExpires(Date expires) {
        this.expires = expires == null ? null : new Date(expires.getTime());
    }

    /**
     * Gibt ID des Benutzer zurück.
     *
     * @return userId
     */
    @Column(name = "USER_ID", nullable = true, scale = 0)
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId Column USER_ID : Benutzer ID .
     */
    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    /**
     * Gibt Benutzer zurück.
     *
     * @return userName
     */
    @Column(name = "USER_NAME", nullable = true, length = 50)
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName Column USER_NAME: Benutzername .
     */
    public void setUserName(final String userName) {
        this.userName = userName;
    }

    /**
     * Gibt Sperrursache zurück. z.b.IMPORT, BATCH_GROUPING
     *
     * @return cause
     */
    @Column(name = "CAUSE", nullable = true, length = 255)
    public String getCause() {
        return cause;
    }

    /**
     * @param cause Column CAUSE: Sperrursache (IMPORT, BATCH_GROUPING).
     */
    public void setCause(final String cause) {
        this.cause = cause;
    }

    /**
     * Gibt Kunden ID zurück.
     *
     * @return clientId
     */
    @Column(name = "CLIENT_ID", nullable = true, length = 50)
    public String getClientId() {
        return clientId;
    }

    /**
     * @param clientId Column CLIENT_ID: Kunden ID .
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

}
