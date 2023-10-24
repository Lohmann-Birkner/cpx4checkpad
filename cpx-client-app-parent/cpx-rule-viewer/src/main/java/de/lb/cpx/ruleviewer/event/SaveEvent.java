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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.event;

import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author wilde
 */
public class SaveEvent extends Event {

    private static final long serialVersionUID = 1L;

    private static final EventType<SaveEvent> SAVE_EVENT
            = new EventType<>("SaveEvent");

    public static final EventType<SaveEvent> ANY = SAVE_EVENT;

    @SuppressWarnings("unchecked")
    public static <T> EventType<SaveEvent> saveEvent() {
        return SAVE_EVENT;
    }
    private final transient Object object;

    public SaveEvent(EventType<? extends Event> eventType, Object pObj) {
        super(eventType);
        this.object = pObj;
    }

    public SaveEvent() {
        this(ANY, null);
    }

    public Object getObject() {
        return object;
    }

}
