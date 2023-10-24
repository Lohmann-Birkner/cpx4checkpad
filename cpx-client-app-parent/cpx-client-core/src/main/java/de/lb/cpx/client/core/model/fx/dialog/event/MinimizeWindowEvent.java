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
package de.lb.cpx.client.core.model.fx.dialog.event;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.stage.WindowEvent;

/**
 * Event to minimize current window
 *
 * @author wilde
 */
public class MinimizeWindowEvent extends WindowEvent {

    /**
     * This event should occure when window .
     */
    public static final EventType<MinimizeWindowEvent> WINDOW_MINIMIZE
            = new EventType<>(WindowEvent.ANY, "WINDOW_MINIMIZE");
    private static final long serialVersionUID = 1L;
    private final transient Node node;

    /**
     * creates new window event
     *
     * @param source source pane who wants enclosing window to be minimized
     * @param eventType type of the event
     */
    public MinimizeWindowEvent(Node source, EventType<? extends Event> eventType) {
        super(source.getScene().getWindow(), eventType);
        node = source;
    }

    /**
     * @return get calling pane
     */
    public Node getNode() {
        return node;
    }

}
