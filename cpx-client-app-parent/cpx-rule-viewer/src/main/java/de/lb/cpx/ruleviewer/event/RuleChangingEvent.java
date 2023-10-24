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
public class RuleChangingEvent extends Event {

    private static final long serialVersionUID = 1L;

    private static final EventType<RuleChangingEvent> RULE_CHANGING_EVENT
            = new EventType<>("RuleChangingEvent");

    public static final EventType<RuleChangingEvent> ANY = RULE_CHANGING_EVENT;

    @SuppressWarnings("unchecked")
    public static <T> EventType<RuleChangingEvent> ruleChangingEvent() {
        return RULE_CHANGING_EVENT;
    }
    private final transient Object object;
    private final String changeText;

    public RuleChangingEvent(EventType<? extends Event> eventType, Object pObj, String pChangeText) {
        super(eventType);
        this.object = pObj;
        this.changeText = pChangeText;
    }

    public RuleChangingEvent(EventType<? extends Event> eventType, String pChangeText) {
        this(eventType, null, pChangeText);
    }

    public Object getObject() {
        return object;
    }
}
