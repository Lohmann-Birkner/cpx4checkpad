/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.menu.fx.event;

import de.lb.cpx.client.app.menu.model.ListType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Event handler to handle open events in menu (open case / open process) TODO:
 * refactor, make some general event handling?
 *
 * @author wilde
 */
public class Events {

    private static Events instance;
    private final ObjectProperty<DataActionEvent<Long>> actionEventProperty = new SimpleObjectProperty<>();

    public static synchronized Events instance() {
        return instance(true);
    }

    public static synchronized Events instance(boolean bAutoCreateNewIfNull) {
        if (bAutoCreateNewIfNull) {
            if (instance == null) {
                create();
            }
        }
        return instance;
    }

    public static synchronized void create() {
        instance = new Events();
    }

    public ObjectProperty<DataActionEvent<Long>> actionEventProperty() {
        return actionEventProperty;
    }

    public void setNewEvent(DataActionEvent<Long> event) {
        actionEventProperty.set(event);
    }

    public DataActionEvent<Long> getLastEvent() {
        return actionEventProperty.get();
    }

    /**
     * set new open event
     *
     * @param pId id to open
     * @param pType type of the list to open
     */
    public void setNewOpenEvent(Long pId, ListType pType) {
        setNewEvent(new DataActionEvent<>(pId, pType));
    }
}
