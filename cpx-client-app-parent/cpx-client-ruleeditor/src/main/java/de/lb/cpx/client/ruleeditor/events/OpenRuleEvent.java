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
package de.lb.cpx.client.ruleeditor.events;

import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author wilde
 */
public class OpenRuleEvent extends Event {

    private static final long serialVersionUID = 1L;

    private static final EventType<?> OPEN_RULE_EVENT
            = new EventType<>("OpenRuleEvent");

    public static final EventType<?> ANY = OPEN_RULE_EVENT;

    @SuppressWarnings("unchecked")
    public static <T> EventType<OpenRuleEvent> openRuleEvent() {
        return (EventType<OpenRuleEvent>) OPEN_RULE_EVENT;
    }

    private final transient CrgRules rule;
    private final transient CrgRulePools pool;

    public OpenRuleEvent(EventType<? extends Event> eventType, CrgRulePools pPool, CrgRules pRule) {
        super(eventType);
        this.rule = pRule;
        this.pool = pPool;

    }

    public CrgRules getRule() {
        return rule;
    }

    public CrgRulePools getPool() {
        return pool;
    }

}
