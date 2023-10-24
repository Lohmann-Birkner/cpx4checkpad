/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.event;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;

/**
 * Event to toggle ui refresh, if ui needs to be re-rendered
 *
 * @author wilde
 */
public class RefreshEvent extends Event {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    public static <T> EventType<RefreshEvent> refreshEvent() {
        return (EventType<RefreshEvent>) REFRESH_EVENT;
    }
    private static final EventType<?> REFRESH_EVENT
            = new EventType<>("RefreshEvent");

    public static final EventType<?> ANY = REFRESH_EVENT;
    private final transient Node node;

    public Node getNode() {
        return node;
    }

    public RefreshEvent(EventType<? extends Event> eventType, Node pNode) {
        super(eventType);
        this.node = pNode;
    }
}
