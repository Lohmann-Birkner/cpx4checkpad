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
import java.util.ArrayList;
import java.util.List;
import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author wilde
 */
public class UpdateRoleEvent extends Event {

    private static final long serialVersionUID = 1L;

    private static final EventType<?> UPDATE_ROLE_EVENT
            = new EventType<>("UpdateRoleEvent");

    public static final EventType<?> ANY = UPDATE_ROLE_EVENT;

    @SuppressWarnings("unchecked")
    public static <T> EventType<UpdateRoleEvent> updateRoleEvent() {
        return (EventType<UpdateRoleEvent>) UPDATE_ROLE_EVENT;
    }

    private final transient List<Long> ruleIds;
    private final transient CrgRulePools pool;
    private final long role;

    public UpdateRoleEvent(EventType<? extends Event> eventType, CrgRulePools pPool, long pRole, List<Long> pRuleIds) {
        super(eventType);
        this.ruleIds = pRuleIds == null ? new ArrayList<>() : new ArrayList<>(pRuleIds);
        this.role = pRole;
        this.pool = pPool;
    }

    public List<Long> getRuleIds() {
        return new ArrayList<>(ruleIds);
    }

    public CrgRulePools getPool() {
        return pool;
    }

    public long getRole() {
        return role;
    }

}
