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
public class RuleChangedEvent extends Event {

    private static final long serialVersionUID = 1L;

    private static final EventType<RuleChangedEvent> RULE_CHANGED_EVENT
            = new EventType<>("RuleChangedEvent");

    public static final EventType<RuleChangedEvent> ANY = RULE_CHANGED_EVENT;

    @SuppressWarnings("unchecked")
    public static <T> EventType<RuleChangedEvent> ruleChangedEvent() {
        return RULE_CHANGED_EVENT;
    }
    private final transient Object object;
    private final boolean revalidateRule;

    public RuleChangedEvent(EventType<? extends Event> eventType, Object pObj,boolean pRevalidateRule) {
        super(eventType);
        this.object = pObj;
        this.revalidateRule = pRevalidateRule;
    }
    public boolean isRevalidateRule(){
        return revalidateRule;
    }
    public RuleChangedEvent() {
        this(true);
    }
     public RuleChangedEvent(boolean pRevalidateRule) {
        this(ANY, null,pRevalidateRule);
    }
     public RuleChangedEvent(EventType<? extends Event> eventType, Object pObj) {
        this(eventType, pObj, true);
    }
    public Object getObject() {
        return object;
    }

}
