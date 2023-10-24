/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.ruleeditor.events;

import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import java.util.List;
import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author wilde
 */
public class UpdateRulesEvent extends Event {

    private static final long serialVersionUID = 1L;

    private static final EventType<?> UPDATE_RULES_EVENT
            = new EventType<>("UpdateRulesEvent");

    public static final EventType<?> ANY = UPDATE_RULES_EVENT;

    private final transient List<CrgRules> rules;
    private final transient CrgRulePools pool;
    private final transient boolean validationInvalid;

    public UpdateRulesEvent(EventType<? extends Event> eventType, CrgRulePools pPool, List<CrgRules> pRules, boolean pValidationInvalid) {
        super(eventType);
        this.rules = pRules;
        this.pool = pPool;
        this.validationInvalid = pValidationInvalid;
    }

    @SuppressWarnings("unchecked")
    public static <T> EventType<UpdateRulesEvent> updateRulesEvent() {
        return (EventType<UpdateRulesEvent>) UPDATE_RULES_EVENT;
    }

    public List<CrgRules> getRules() {
        return rules;
    }

    public CrgRulePools getPool() {
        return pool;
    }
    
    public boolean isValidationInvalid(){
        return validationInvalid;
    }
}
