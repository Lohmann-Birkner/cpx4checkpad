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
package de.lb.cpx.wm.model.enums;

import de.lb.cpx.model.enums.AbstractCpxEnumMap;
import de.lb.cpx.model.enums.CpxEnumInterface;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Husser
 */
public enum WmEventTypeEn implements CpxEnumInterface<Integer> {

    //Just examples, can be still changed completely! (DNi 2016-10-19)
    //100-199 Document
    documentAdded(101, EventOperation.ADD, Lang.EVENT_TYPE_DOCUMENT_ADDED),
    documentRemoved(105, EventOperation.REMOVE, Lang.EVENT_TYPE_DOCUMENT_REMOVED),
    //200-299 Reminder
    reminderCreated(201, EventOperation.ADD, Lang.EVENT_TYPE_REMINDER_CREATED),
    //    reminderAssigned(202, Lang.EVENT_TYPE_REMINDER_ASSIGNED),
    reminderChanged(203, EventOperation.CHANGE, Lang.EVENT_TYPE_REMINDER_CHANGED),
    //    reminderExpires(204, Lang.EVENT_TYPE_REMINDER_EXPIRES),
    reminderRemoved(205, EventOperation.REMOVE, Lang.EVENT_TYPE_REMINDER_REMOVED),
    //300-399 Request
    requestCreated(301, EventOperation.ADD, Lang.EVENT_TYPE_REQUEST_CREATED),
    requestUpdated(302, EventOperation.CHANGE, Lang.EVENT_TYPE_REQUEST_UPDATED),
    requestRemoved(303, EventOperation.REMOVE, Lang.EVENT_TYPE_REQUEST_REMOVED),
    //400-499 Case
    caseAdded(401, EventOperation.ADD, Lang.EVENT_TYPE_CASE_ADDED),
    caseRemoved(405, EventOperation.REMOVE, Lang.EVENT_TYPE_CASE_REMOVED),
    //500-599 Action
    actionAdded(501, EventOperation.ADD, Lang.EVENT_TYPE_ACTION_ADDED),
    actionChanged(502, EventOperation.CHANGE, Lang.EVENT_TYPE_ACTION_CHANGED),
    actionRemoved(505, EventOperation.REMOVE, Lang.EVENT_TYPE_ACTION_REMOVED),
    //600-699 Process
    processUserChanged(601, EventOperation.CHANGE, Lang.EVENT_TYPE_PROCESS_USER_CHANGED),
    processSubjectChanged(602, EventOperation.CHANGE, Lang.EVENT_TYPE_PROCESS_SUBJECT_CHANGED),
    processClosed(603, EventOperation.CHANGE, Lang.EVENT_TYPE_PROCESS_CLOSED),
    processReopened(604, EventOperation.CHANGE, Lang.EVENT_TYPE_PROCESS_REOPENED),
    processPaused(605, EventOperation.CHANGE, Lang.EVENT_TYPE_PROCESS_PAUSED),
    processContinued(606, EventOperation.CHANGE, Lang.EVENT_TYPE_PROCESS_CONTINUED),
    //700-799 Kain
    kainReceived(701, EventOperation.ADD, Lang.EVENT_TYPE_KAIN_RECEIVED),
    //800-899 Inka
    inkaStored(801, EventOperation.ADD, Lang.EVENT_TYPE_INKA_STORED),
    inkaUpdated(802, EventOperation.CHANGE, Lang.EVENT_TYPE_INKA_UPDATED),
    inkaSent(803, EventOperation.OTHER, Lang.EVENT_TYPE_INKA_SENT),
    inkaCancelled(804, EventOperation.OTHER, Lang.EVENT_TYPE_INKA_CANCELLED);

    public enum EventOperation {
        ADD, CHANGE, REMOVE, OTHER
    }

    private static final Logger LOG = Logger.getLogger(WmEventTypeEn.class.getName());

    private final int id;
    private final EventOperation operation;
    private final String langKey;

    private WmEventTypeEn(int id, final EventOperation pOperation, String langKey) {
        this.id = id;
        this.operation = pOperation == null ? EventOperation.OTHER : pOperation;
        this.langKey = langKey;
    }

    public EventOperation getOperation() {
        return operation;
    }

    public boolean isAddEvent() {
        return EventOperation.ADD.equals(operation);
    }

    public boolean isChangeEvent() {
        return EventOperation.CHANGE.equals(operation);
    }

    public boolean isRemoveEvent() {
        return EventOperation.REMOVE.equals(operation);
    }

    public boolean isOtherEvent() {
        return EventOperation.OTHER.equals(operation);
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getIdStr() {
        return String.valueOf(id);
    }

    @Override
    public int getIdInt() {
        return id;
    }

    @Override
    public String getLangKey() {
        return langKey;
    }

    /*
    @Override
    public String toString(final CpxLanguageInterface cpxLanguage) {
        return this.getViewId() + " - " + cpxLanguage.get(langKey);
    }
     */
    @Override
    public String toString() {
        return CpxEnumInterface.toStaticString(getViewId(), getLangKey());
    }

    @Override
    public String getViewId() {
        return String.valueOf(id);
    }

    @Override
    public boolean isViewRelevant() {
        return true;
    }

//    @Override
//    public CpxEnumInterface getEnum(String value) {
//        try {
//            return CpxEnumInterface.findEnum(values(), value);
//        } catch (IllegalArgumentException ex) {
//            Logger.getLogger(WmEventTypeEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
//        }
//        return null;
//    }
    @Override
    public Translation getTranslation() {
        return getTranslation((Object[]) null);
    }

    @Override
    public Translation getTranslation(Object... pParams) {
        return Lang.get(getLangKey(), pParams);
    }

    public static WmEventTypeEn findById(final Integer pId) {
        return WmEventTypeEnMap.getInstance().get(pId);
    }

    public static WmEventTypeEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find WmEventTypeEn, because this is not a valid integer: " + id + " (maybe invalid enum value is stored in database?)", ex);
            return null;
        }
    }

    public static WmEventTypeEn[] getAddEvents() {
        return getByOperation(EventOperation.ADD);
    }

    public static WmEventTypeEn[] getChangeEvents() {
        return getByOperation(EventOperation.CHANGE);
    }

    public static WmEventTypeEn[] getRemoveEvents() {
        return getByOperation(EventOperation.REMOVE);
    }

    public static WmEventTypeEn[] getOtherEvents() {
        return getByOperation(EventOperation.OTHER);
    }

    public static WmEventTypeEn[] getByOperation(final EventOperation pOperation) {
        if (pOperation == null) {
            return new WmEventTypeEn[0];
        }
        final List<WmEventTypeEn> list = new ArrayList<>();
        for (WmEventTypeEn type : values()) {
            if (pOperation.equals(type.getOperation())) {
                list.add(type);
            }
        }
        final WmEventTypeEn[] result = new WmEventTypeEn[list.size()];
        list.toArray(result);
        return result;
    }

    public boolean isDocumentRelated() {
        return id >= 100 && id <= 199;
    }

    public boolean isReminderRelated() {
        return id >= 200 && id <= 299;
    }

    public boolean isRequestRelated() {
        return id >= 300 && id <= 399;
    }

    public boolean isHosCaseRelated() {
        return id >= 400 && id <= 499;
    }

    public boolean isActionRelated() {
        return id >= 500 && id <= 599;
    }

    public boolean isProcessFinalisationRelated() {
        return id >= 603 && id <= 604;
    }

    public boolean isProcessRelated() {
        return id >= 600 && id <= 699;
    }

    public boolean isKainRelated() {
        return id >= 700 && id <= 799;
    }

    public boolean isInkaRelated() {
        return id >= 800 && id <= 899;
    }

    public boolean isKainInkaRelated() {
        return isKainRelated() || isInkaRelated();
    }

}

final class WmEventTypeEnMap extends AbstractCpxEnumMap<WmEventTypeEn, Integer> {

    private static final WmEventTypeEnMap INSTANCE;

    static {
        INSTANCE = new WmEventTypeEnMap();
    }

    protected WmEventTypeEnMap() {
        super(WmEventTypeEn.class);
    }

    public static WmEventTypeEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public WmEventTypeEn[] getValues() {
        return WmEventTypeEn.values();
    }

}
