/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.listview.entries.subject;

import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmEvent;
import de.lb.cpx.wm.model.TWmReminder;
import de.lb.cpx.wm.model.enums.WmEventTypeEn;
import java.text.MessageFormat;

/**
 * Simple Pojo to store values for ListView
 *
 * @author niemeier
 */
public class ReminderEventSubject extends EventSubject<TWmReminder> {

    public ReminderEventSubject(final WmEventTypeEn pType, final TWmReminder pItem) {
        super(pType, pItem);
    }

    public ReminderEventSubject(TWmEvent pEvent) {
        super(pEvent);
    }

    @Override
    public Object[] getTextParameters() {
        return new Object[]{
            getReminderSubject(),
            getReminderDueDate(),
            getReminderAssignedUser(),
            getReminderFinishedStatus()
        };
    }

    private String getReminderSubject() {
//        if (pEvent.getReminder() == null) {
//            return PLACEHOLDER; //"----";
//        }
        long internalId = item.getSubject();
        if (internalId == 0L) {
            return PLACEHOLDER;
        }
        String cache = MenuCache.instance().getReminderSubjectsForInternalId(internalId);
        return cache == null ? MessageFormat.format(PLACEHOLDER_ID, internalId) /* "----" */ : cache;
    }

    private String getReminderDueDate() {
        return item.getDueDate() != null ? Lang.toDate(item.getDueDate()) : Lang.getReminderDuedateIsBlank();
    }

    private String getReminderAssignedUser() {
        return getFullUserName(item.getAssignedUserId());
    }

    private String getFullUserName(long pUserId) {
        return pUserId != 0L ? MenuCache.instance().getUserFullNameForId(pUserId) : Lang.getReminderUserIsBlank();
    }

    private String getReminderFinishedStatus() {
        return item.isFinished() ? Lang.getReminderFinishedStatus() : Lang.getReminderUnfinishedStatus();
    }

    @Override
    public String getRemovedDescription() {
        return super.getRemovedDescription(Lang.getEventNameReminder()); //Wiedervorlage wurde gelöscht
    }

}
