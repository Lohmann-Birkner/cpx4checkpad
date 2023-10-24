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

import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import de.lb.cpx.wm.model.TWmEvent;
import de.lb.cpx.wm.model.enums.WmEventTypeEn;

/**
 * Simple Pojo to store values for ListView
 *
 * @author niemeier
 * @param <E> type
 */
public abstract class EventSubject<E extends AbstractEntity> {

    public static final String PLACEHOLDER = "----";
    public static final String PLACEHOLDER_ID = "--{0}--";

//    private static final Logger LOG = Logger.getLogger(EventText.class.getName());
    protected final E item;
    protected final WmEventTypeEn type;

    public EventSubject(final WmEventTypeEn pType, final E pItem) {
        type = pType; //Objects.requireNonNull(pType, "Event Type can not be null");
        item = pItem; //Objects.requireNonNull(pItem, "Item (Event Content) can not be null");
    }

    @SuppressWarnings("unchecked")
    public EventSubject(final TWmEvent pEvent) {
        type = pEvent == null ? null : pEvent.getEventType();
        item = pEvent == null ? null : (E) pEvent.getContent();
    }

    public E getItem() {
        return item;
    }

    public WmEventTypeEn getType() {
        return type;
    }

    public String getRemovedDescription() {
        throw new UnsupportedOperationException("remove operation is not supported by event of type " + type.name());
    }

    protected String getRemovedDescription(final String pItemName) {
        return Lang.getEventRemoved(pItemName);
    }

    public final String getText() {
//        if (event == null) {
//            return "";
//        }
//        final WmEventTypeEn type = event.getEventType();
        if (type == null) {
            return PLACEHOLDER; //"----";
        }
//        if (event.getSubject() != null && !event.getSubject().isEmpty()) {
//            return event.getSubject();
//        }
//        if (event.isOrphaned()) {
//            //content was deleted!
//            return getDefaultText();
//        }
        return getTranslation(type).value;
    }

    protected Translation getTranslation(final WmEventTypeEn pType) {
        return pType.getTranslation(getTextParameters());
    }

    public abstract Object[] getTextParameters();

}
