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
package de.lb.cpx.client.core.menu.fx.filterlists;

import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author wilde
 */
public class FilterEvent extends Event {

    private static final long serialVersionUID = 1L;

    private static final EventType<?> FILTER_EVENT
            = new EventType<>("FilterEvent");

    public static final EventType<?> ANY = FILTER_EVENT;

    @SuppressWarnings("unchecked")
    public static EventType<FilterEvent> filterEvent() {
        return (EventType<FilterEvent>) FILTER_EVENT;
    }

//    private final FilterBasicItem[] filterItems;
    public FilterEvent() {
        super(filterEvent());
//        this.filterItems = pFilterItems;
    }

    public FilterEvent(EventType<? extends Event> eventType) {
        super(eventType);
//        this.filterItems = pFilterItems;
    }
//
//    public FilterBasicItem[] getFilterItems() {
//        return filterItems;
//    }

}
