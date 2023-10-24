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
package de.lb.cpx.client.app.wm.fx.process.completion.risk;

import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author wilde
 */
public class SaveOrUpdateRiskEvent extends Event {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    public static EventType<SaveOrUpdateRiskEvent> saveOrUpdateRiskEvent() {
        return (EventType<SaveOrUpdateRiskEvent>) SAVE_OR_UPDATE_EVENT;
    }
    private static final EventType<?> SAVE_OR_UPDATE_EVENT
            = new EventType<>("SaveOrUpdateEvent");
    public static final EventType<?> ANY = SAVE_OR_UPDATE_EVENT;
    
    public SaveOrUpdateRiskEvent(){
        this(ANY);
    }
    public SaveOrUpdateRiskEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
    
}
