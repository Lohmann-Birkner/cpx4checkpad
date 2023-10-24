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

import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes;
import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author wilde
 */
public class UpdateRuleTypeEvent extends Event {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    public static EventType<UpdateRuleTypeEvent> updateRuleTypeEvent() {
        return (EventType<UpdateRuleTypeEvent>) UPDATE_RULE_TYPE_EVENT;
    }
    private static final EventType<?> UPDATE_RULE_TYPE_EVENT
            = new EventType<>("UpdateRuleTypeEvent");

    @SuppressWarnings("unchecked")
    public static EventType<UpdateRuleTypeEvent> addRuleTypeEvent() {
        return (EventType<UpdateRuleTypeEvent>) ADD_RULE_TYPE_EVENT;
    }
    private static final EventType<?> ADD_RULE_TYPE_EVENT
            = new EventType<>("AddRuleTypeEvent");

    @SuppressWarnings("unchecked")
    public static EventType<UpdateRuleTypeEvent> removeRuleTypeEvent() {
        return (EventType<UpdateRuleTypeEvent>) REMOVE_RULE_TYPE_EVENT;
    }
    private static final EventType<?> REMOVE_RULE_TYPE_EVENT
            = new EventType<>("removeRuleTypeEvent");

    public static final EventType<?> ANY = UPDATE_RULE_TYPE_EVENT;

    private final transient CrgRuleTypes type;

    public CrgRuleTypes getRule() {
        return type;
    }

    public UpdateRuleTypeEvent(EventType<? extends Event> eventType, CrgRuleTypes pType) {
        super(eventType);
        this.type = pType;
    }

}
