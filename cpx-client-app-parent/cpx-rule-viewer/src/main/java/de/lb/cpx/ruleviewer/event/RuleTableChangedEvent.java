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
public class RuleTableChangedEvent extends Event {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    public static <T> EventType<RuleTableChangedEvent> ruleTableChangedEvent() {
        return RULE_TABLE_CHANGED_EVENT;
    }
    private static final EventType<RuleTableChangedEvent> RULE_TABLE_CHANGED_EVENT
            = new EventType<>("RuleTableChangedEvent");

    public static final EventType<RuleTableChangedEvent> ANY = RULE_TABLE_CHANGED_EVENT;

    public RuleTableChangedEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

}
