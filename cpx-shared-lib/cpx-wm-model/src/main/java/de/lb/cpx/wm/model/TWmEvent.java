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
package de.lb.cpx.wm.model;

import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TP301KainInka;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.server.commons.dao.AbstractVersionEntity;
import de.lb.cpx.wm.converter.EventTypeConverter;
import de.lb.cpx.wm.model.enums.WmEventTypeEn;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Convert;
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
import javax.persistence.Transient;

/**
 * <p style="font-size:1em; color:green;"> Tabelle der Events (Aktion,
 * Wiedervorlage, Anfrage, Leistung, etc.). </p>
 *
 * @author Dirk Niemeier
 */
@Entity
@Table(name = "T_WM_EVENT",
        indexes = {
            @Index(name = "IDX_WM_EVENT4DOCUMENT_ID", columnList = "T_WM_DOCUMENT_ID", unique = false),
            @Index(name = "IDX_WM_EVENT4CASE_ID", columnList = "T_CASE_ID", unique = false),
            @Index(name = "IDX_WM_EVENT4REQUEST_ID", columnList = "T_WM_REQUEST_ID", unique = false),
            @Index(name = "IDX_WM_EVENT4REMINDER_ID", columnList = "T_WM_REMINDER_ID", unique = false),
            @Index(name = "IDX_WM_EVENT4ACTION_ID", columnList = "T_WM_ACTION_ID", unique = false),
            @Index(name = "IDX_WM_EVENT4PROCESS_ID", columnList = "T_WM_PROCESS_ID", unique = false),
            @Index(name = "IDX_P301_EVENT4KAIN_INKA_ID", columnList = "T_P301_KAIN_INKA_ID", unique = false)})
public class TWmEvent extends AbstractVersionEntity {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(TWmEvent.class.getName());
    //private CdbUsers createdBy;
    //private Long creationUserId;
    private String subject;
    private String description;
    private WmEventTypeEn eventType;
    private TWmProcess process;

    private TWmReminder reminder = null;
    private TWmDocument document = null;
    private TWmRequest request = null;
    private TCase hosCase = null;
    private TWmAction action = null;
    private TP301KainInka kainInka = null;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_WM_EVENT_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gibt Art des Events zurück.z.b. Aktion, Wiedervorlage, Anfrage, Leistung,
     * etc.
     *
     * @return eventType
     */
    @Column(name = "EVENT_TYPE", length = 10, nullable = false)
    @Convert(converter = EventTypeConverter.class)
    public WmEventTypeEn getEventType() {
        return this.eventType;
    }

    /**
     *
     * @param eventType Column EVENT_TYPE: Art des Events (Aktion,
     * Wiedervorlage, Anfrage, Leistung, etc.) .
     */
    public void setEventType(WmEventTypeEn eventType) {
        this.eventType = eventType;
    }

    /**
     * Gibt Verweis auf T_WM_REMINDER zurück.
     *
     * @return reminder
     */
    @ManyToOne
    @JoinColumn(name = "T_WM_REMINDER_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_WM_EVENT4REMINDER_ID"))
    public TWmReminder getReminder() {
        return reminder;
    }

    /**
     * @param reminder Column T_WM_REMINDER_ID: Verweis auf T_WM_REMINDER.
     */
    public void setReminder(TWmReminder reminder) {
        this.reminder = reminder;
    }

    /**
     * Gibt Verweis auf T_WM_DOCUMENT zurück , wenn ein Dokument hinzugefügt
     * wurde.
     *
     * @return document
     */
    @ManyToOne
    @JoinColumn(name = "T_WM_DOCUMENT_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_WM_EVENT4DOCUMENT_ID"))
    public TWmDocument getDocument() {
        return document;
    }

    /**
     * @param document Column T_WM_DOCUMENT_ID :Verweis auf T_WM_DOCUMENT , wenn
     * ein Dokument hinzugefügt wurde.
     */
    public void setDocument(TWmDocument document) {
        this.document = document;
    }

    /**
     * Gibt Verweis auf T_WM_REQUEST. zurück.
     *
     * @return request
     */
    @ManyToOne
    @JoinColumn(name = "T_WM_REQUEST_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_WM_EVENT4REQUEST_ID"))
    public TWmRequest getRequest() {
        return request;
    }

    /**
     * @param request Column T_WM_REQUEST_ID :Verweis auf T_WM_REQUEST.
     */
    public void setRequest(TWmRequest request) {
        this.request = request;
    }

    /**
     * Gibt Verweis auf T_CASE zurück.
     *
     * @return case
     */
    @ManyToOne
    @JoinColumn(name = "T_CASE_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_WM_EVENT4CASE_ID"))
    public TCase getHosCase() {
        return hosCase;
    }

    /**
     * @param hosCase Column T_CASE_ID: Verweis auf T_CASE.
     */
    public void setHosCase(TCase hosCase) {
        this.hosCase = hosCase;
    }

    /**
     * Gibt Verweis auf T_WM_ACTION zurück.
     *
     * @return action
     */
    @ManyToOne
    @JoinColumn(name = "T_WM_ACTION_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_WM_EVENT4ACTION_ID"))
    public TWmAction getAction() {
        return action;
    }

    /**
     * @param action T_WM_ACTION_ID: Column Verweis auf T_WM_ACTION.
     */
    public void setAction(TWmAction action) {
        this.action = action;
    }

    /**
     * Gibt Verweis auf T_P301_KAIN_INKA zurück.
     *
     * @return kainInka
     */
    @ManyToOne
    @JoinColumn(name = "T_P301_KAIN_INKA_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_P301_EVENT4KAIN_INKA_ID"))
    public TP301KainInka getKainInka() {
        return kainInka;
    }

    /**
     * @param kainInka T_P301_KAIN_INKA_ID: Column Verweis auf T_P301_KAIN_INKA.
     */
    public void setKainInka(TP301KainInka kainInka) {
        this.kainInka = kainInka;
    }

    @Override
    public boolean versionEquals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof TWmEvent)) {
            return false;
        }
        final TWmEvent other = (TWmEvent) object;
//        if (!Objects.equals(this.creationUserId, other.creationUserId)) {
//            return false;
//        }
        if (!Objects.equals(this.subject, other.subject)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.process, other.process)) {
            return false;
        }
        return true;
    }

//    /**
//     * @return the createdBy
//     */
//    @Column(name = "CREATION_USER_ID")
//    public Long getCreationUserId() {
//        return creationUserId;
//    }
//
//    /**
//     * @param pCreationUserId the user that created this event entry
//     */
//    public void setCreationUserId(final Long pCreationUserId) {
//        this.creationUserId = (pCreationUserId != null && pCreationUserId.equals(0L) ? null : pCreationUserId);
//    }
    /**
     * @return the subject
     */
    @Column(name = "SUBJECT", length = 256)
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        if (subject != null && subject.length() >= 250) {
            subject = subject.substring(0, 249);
        }
        this.subject = subject;
    }

    /**
     * @return the description
     */
    @Column(name = "DESCRIPTION", nullable = true, length = 800)
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the process
     */
    @ManyToOne
    @JoinColumn(name = "T_WM_PROCESS_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_WM_EVENT4PROCESS_ID"))
    public TWmProcess getProcess() {
        return process;
    }

    /**
     * @param process the process to set
     */
    public void setProcess(TWmProcess process) {
        this.process = process;
    }

    /**
     * transient methode to access the current set Content Can be
     * TWmDocument,TWmRequest,TWmReminder,TWmAction or TCase
     *
     * @return returns current content or null
     */
    @Transient
    public AbstractEntity getContent() {
        if (isDocumentEvent() && document != null) {
            return document;
        }
        if (isHosCaseEvent() && hosCase != null) {
            return hosCase;
        }
        if (isReminderEvent() && reminder != null) {
            return reminder;
        }
        if (isRequestEvent() && request != null) {
            return request;
        }
        if (isActionEvent() && action != null) {
            return action;
        }
        if (isKainInkaEvent() && kainInka != null) {
            return kainInka;
        }
        if (isProcessEvent() && process != null) {
            return process;
        }
        return null;
    }

    /**
     * convenient methode to set the content. Awaits entity of type
     * TWmAction,TwmRequest,TWmReminder,TwmDocument,TCase
     *
     * @param entity entity to set
     */
    public void setContent(AbstractEntity entity) {
        if (entity != null) {
            if (isDocumentEvent() && entity instanceof TWmDocument) {
                setDocument((TWmDocument) entity);
                return;
            }
            if (isReminderEvent() && entity instanceof TWmReminder) {
                setReminder((TWmReminder) entity);
                return;
            }
            if (isRequestEvent() && entity instanceof TWmRequest) {
                setRequest((TWmRequest) entity);
                return;
            }
            if (isActionEvent() && entity instanceof TWmAction) {
                setAction((TWmAction) entity);
                return;
            }
            if (isHosCaseEvent() && entity instanceof TCase) {
                setHosCase((TCase) entity);
                return;
            }
            if (isKainInkaEvent() && entity instanceof TP301KainInka) {
                setKainInka((TP301KainInka) entity);
                return;
            }
            if (isProcessEvent() && entity instanceof TWmProcess) {
                setProcess((TWmProcess) entity);
                return;
            }
            LOG.log(Level.WARNING, "Unknown event type, cannot set content for entity '" + entity.getClass().getSimpleName() + "'");
        }
    }

    public void removeContent() {
        if (isOrphaned()) {
            LOG.log(Level.FINEST, "cannot remove content from event, because it's already orphaned!");
            return;
        }
        if (isActionEvent()) {
            setAction(null);
        } else if (isDocumentEvent()) {
            setDocument(null);
        } else if (isRequestEvent()) {
            setRequest(null);
        } else if (isReminderEvent()) {
            setReminder(null);
        } else if (isHosCaseEvent()) {
            setHosCase(null);
        } else if (isKainInkaEvent()) {
            setKainInka(null);
        } else if (isProcessEvent()) {
            setProcess(null);
        } else {
            //LOG.log(Level.SEVERE, "cannot remove content from event, please implement this missing functionality here!");
            throw new IllegalStateException(MessageFormat.format("cannot remove content from event of type {0}, please implement this missing functionality!", getEventType()));
        }
    }

    /**
     * Alternative: Maybe we should check the event type instead of event
     * references here (or we should combine both!). But as far as I see the
     * event type does not change when you delete an entry.
     *
     * @return orphaned event?
     */
    @Transient
    public boolean isOrphaned() {
        return (getDocument() == null && getEventType().isDocumentRelated())
                || (getReminder() == null && getEventType().isReminderRelated())
                || (getRequest() == null && getEventType().isRequestRelated())
                || (getAction() == null && getEventType().isActionRelated())
                || (getHosCase() == null && getEventType().isHosCaseRelated())
                || (getKainInka() == null && getEventType().isKainInkaRelated()
                || (getProcess() == null && getEventType().isProcessRelated()));
    }

    public boolean isDocumentInEvent(final AbstractEntity pDocument) {
        return getEventType().isDocumentRelated() && document != null && document.equals(pDocument);
    }

    public boolean isReminderInEvent(final AbstractEntity pReminder) {
        return getEventType().isReminderRelated() && reminder != null && reminder.equals(pReminder);
    }

    public boolean isRequestInEvent(final AbstractEntity pRequest) {
        return getEventType().isRequestRelated() && request != null && request.equals(pRequest);
    }

    public boolean isActionInEvent(final AbstractEntity pAction) {
        return getEventType().isActionRelated() && action != null && action.equals(pAction);
    }

    public boolean isHosCaseInEvent(final AbstractEntity pHosCase) {
        return getEventType().isHosCaseRelated() && hosCase != null && hosCase.equals(pHosCase);
    }

    public boolean isKainInkaInEvent(final AbstractEntity pKainInka) {
        return getEventType().isKainInkaRelated() && kainInka != null && kainInka.equals(pKainInka);
    }

    public boolean isProcessInEvent(final AbstractEntity pProcess) {
        return getEventType().isProcessRelated() && process != null && process.equals(pProcess);
    }

    public boolean isContentInEvent(final AbstractEntity pEntity) {
        if (pEntity == null) {
            return false;
        }
        return isDocumentInEvent(pEntity)
                || isReminderInEvent(pEntity)
                || isRequestInEvent(pEntity)
                || isActionInEvent(pEntity)
                || isHosCaseInEvent(pEntity)
                || isKainInkaInEvent(pEntity)
                || isProcessInEvent(pEntity);
    }

    @Transient
    public boolean isDocumentEvent() {
        return getEventType().isDocumentRelated();
    }

    @Transient
    public boolean isReminderEvent() {
        return getEventType().isReminderRelated();
    }

    @Transient
    public boolean isRequestEvent() {
        return getEventType().isRequestRelated();
    }

    @Transient
    public boolean isHosCaseEvent() {
        return getEventType().isHosCaseRelated();
    }

    @Transient
    public boolean isActionEvent() {
        return getEventType().isActionRelated();
    }

    @Transient
    public boolean isProcessFinalisationEvent() {
        return getEventType().isProcessFinalisationRelated();
    }

    @Transient
    public boolean isProcessEvent() {
        return getEventType().isProcessRelated();
    }

    @Transient
    public boolean isKainEvent() {
        return getEventType().isKainRelated();
    }

    @Transient
    public boolean isInkaEvent() {
        return getEventType().isInkaRelated();
    }

    @Transient
    public boolean isKainInkaEvent() {
        return getEventType().isKainInkaRelated();
    }

}
