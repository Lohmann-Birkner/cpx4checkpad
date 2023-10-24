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
 * Contributors:
 *    2016  Husser - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.wm.model;

import de.lb.cpx.server.commons.dao.AbstractVersionEntity;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * <p style="font-size:1em; color:green;"> Verwaltung von Wiedervorlagen.. </p>
 *
 * @author Husser
 */
@Entity
@Table(name = "T_WM_REMINDER",
        indexes = {
            @Index(name = "IDX_WM_REMINDER4PROCESS_ID", columnList = "T_WM_PROCESS_ID", unique = false)})
public class TWmReminder extends AbstractVersionEntity {

    private static final long serialVersionUID = 1L;

    private long assignedUserId;

    private Date dueDate;

    private Date finishedDate;

    private boolean finishedFl;

    // private Date finishingDate;
    //private TWmReminderSubject subject;
//    private WmReminderSubject subject;
//    private CWmListReminderSubject subject;
    private long subject;

    private String comment;

    private boolean highPrioFl;

    private TWmProcess process;
//    private WmReminderTypeEn reminderTyp;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_WM_REMINDER_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }
//
//    @Column(name = "TYPE_OF_REMINDER_EN", length = 10, nullable = true)
//    @Convert(converter = ReminderTypeConverter.class)
//    public WmReminderTypeEn getReminderType() {
//        return reminderTyp;
//    }
//
//    /**
//     * @param reminderTyp new audit type (single or collection)
//     */
//    public void setReminderType(WmReminderTypeEn reminderTyp) {
//        this.reminderTyp = reminderTyp;
//    }

    /**
     * Gibt Verweis auf den Nutzer zurück, der die Wiedervorlage empfangen soll
     * .
     *
     * @return assignedUserId
     */
    @Column(name = "ASSIGNED_USER_ID", nullable = false)
    public long getAssignedUserId() {
        return assignedUserId;
    }

    /**
     * Gibt Dauer bis diesem Datum zurück.
     *
     * @return dueDate
     */
    @Column(name = "DUE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDueDate() {
        return dueDate == null ? null : new Date(dueDate.getTime());
    }

    /**
     * Gibt Abgearbeitet datum zurück.
     *
     * @return finishedDate
     */
    @Column(name = "FINISHED_DATE")
    @Temporal(TemporalType.DATE)
    public Date getFinishedDate() {
        return finishedDate == null ? null : new Date(finishedDate.getTime());
    }

    /**
     * Gibt ein Antwort ,ob diese Wiedervolage fertig ist zurück, nein=0,ja=1.
     *
     * @return finishedFl
     */
    @Column(name = "FINISHED_FL ", nullable = false)

    public boolean isFinished() {
        return finishedFl;
    }

//    /**
//     * Gibt Datum zurück , wann die Wiedervorlage erledigt ist.
//     *
//     * @return finishingDate
//     */
//    @Column(name = "FINISHING_DATE")
//    @Temporal(TemporalType.TIMESTAMP)
//    public Date getFinishingDate() {
//        return finishingDate == null ? null : new Date(finishingDate.getTime());
//    }
    /**
     * Gibt Subject der Wiedervolage zurück , z.b.Warten auf MDK-Gutachten=1 .
     *
     * @return subject
     */
    @Column(name = "SUBJECT", length = 10, nullable = false)
//    @Convert(converter = ReminderSubjectConverter.class)
    public long getSubject() {
        return subject;
    }

    /**
     * Gibt Text zurück ,der in dieser Wiedervorlage steht.
     *
     * @return comment
     */
    @Column(name = "REMINDER_COMMENT")
    public String getComment() {
        return comment;
    }

    /**
     * Gibt ob hohe Priorität ist zurück . nein=0,ja=1
     *
     * @return highPrio
     */
    @Column(name = "HIGH_PRIO_FL", nullable = false)
    public boolean isHighPrio() {
        return highPrioFl;
    }

    /**
     * Gibt Verweis auf T_WM_PROCESS zurück.
     *
     * @return process
     */
    @ManyToOne
    @JoinColumn(name = "T_WM_PROCESS_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_WM_REMINDER4PROCESS_ID"))
    public TWmProcess getProcess() {
        return process;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @param assignedUserId Column ASSIGNED_USER_ID :Verweis auf den Nutzer,
     * der die Wiedervorlage empfangen soll .
     */
    public void setAssignedUserId(long assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    /**
     *
     * @param dueDate Column DUE_DATE :Dauer bis diesem Datum .
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate == null ? null : new Date(dueDate.getTime());

    }

    /**
     *
     * @param finishedDate Column FINISHED_DATE :Abgearbeitet Datum .
     */
    public void setFinishedDate(Date finishedDate) {
        this.finishedDate = finishedDate == null ? null : new Date(finishedDate.getTime());

    }

    /**
     *
     * @param finishedFl Column FINISHED :Ist diese Wiedervolage fertig?
     * nein=0,ja=1.
     */
    public void setFinished(boolean finishedFl) {
        this.finishedFl = finishedFl;
    }
//
//    /**
//     *
//     * @param finishingDate Column FINISHING_DATE :Datum, wann die Wiedervorlage
//     * erledigt ist.
//     */
//    public void setFinishingDate(Date finishingDate) {
//        this.finishingDate = finishingDate == null ? null : new Date(finishingDate.getTime());
//    }

    /**
     *
     * @param subject Column SUBJECT: Subject der Wiedervolage , z.b.Warten auf
     * MDK-Gutachten=1 .
     */
    public void setSubject(long subject) {
        this.subject = subject;
    }

    /**
     *
     * @param comment Column REMINDER_COMMENT :Text ,der in dieser Wiedervorlage
     * steht.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     *
     * @param highPrioFl Column HIGH_PRIO: Ist hohe Priorität?. nein=0,ja=1
     */
    public void setHighPrio(boolean highPrioFl) {
        this.highPrioFl = highPrioFl;
    }

    /**
     *
     * @param process Column T_WM_PROCESS_ID :Verweis auf T_WM_PROCESS.
     */
    public void setProcess(TWmProcess process) {
        this.process = process;
    }

    @Override
    public boolean versionEquals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof TWmReminder)) {
            return false;
        }
        final TWmReminder other = (TWmReminder) o;
        if (!Objects.equals(this.assignedUserId, other.assignedUserId)) {
            return false;
        }
        if (!Objects.equals(this.comment, other.comment)) {
            return false;
        }
        if (!Objects.equals(this.dueDate, other.dueDate)) {
            return false;
        }
        if (!Objects.equals(this.finishedFl, other.finishedFl)) {
            return false;
        }

        if (!Objects.equals(this.highPrioFl, other.highPrioFl)) {
            return false;
        }
        if (!Objects.equals(this.process, other.process)) {
            return false;
        }
        if (!Objects.equals(this.subject, other.subject)) {
            return false;
        }
        return true;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        TWmReminder clone = (TWmReminder) super.clone();
        clone.setId(0);
        return clone;
    }

}
