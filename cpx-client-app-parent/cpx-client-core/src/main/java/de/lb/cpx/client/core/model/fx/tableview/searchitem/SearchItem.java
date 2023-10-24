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
package de.lb.cpx.client.core.model.fx.tableview.searchitem;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Skin;

/**
 * Item to show the search value for a specific field in the workflow and
 * working list (
 *
 * @author wilde
 */
public class SearchItem extends Button {

    private final String dataKey;

    public SearchItem(String pDataKey, String pText) {
        setText(pText);
        dataKey = pDataKey;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new SearchItemSkin(this);
    }

    public String getDataKey() {
        return dataKey;
    }
    //event property to be called when close event is called on filter item
    private final ObjectProperty<EventHandler<ActionEvent>> onCloseEventProperty = new SimpleObjectProperty<>();

    /**
     * @return close event handler to be fired if searchbutton is closed
     */
    public EventHandler<ActionEvent> getOnCloseEvent() {
        return onCloseEventProperty.get();
    }

    /**
     * @param pEvent set eventhandler to react to close event
     */
    public void setOnCloseEvent(EventHandler<ActionEvent> pEvent) {
        onCloseEventProperty.set(pEvent);
    }
}
