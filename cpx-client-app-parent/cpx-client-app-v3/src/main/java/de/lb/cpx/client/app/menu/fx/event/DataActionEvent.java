/*
 * Copyright (c) 2017 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.menu.fx.event;

import de.lb.cpx.client.app.menu.model.ListType;
import java.io.Serializable;
import javafx.event.ActionEvent;

/**
 * Implements action event to pass through data
 *
 * @author wilde
 * @param <T> datatype of the Data
 */
public class DataActionEvent<T extends Serializable> extends ActionEvent {

    private static final long serialVersionUID = 1L;

    private T data;
    private ListType listType;

    /**
     * @param pType sets new action event for list type
     */
    public DataActionEvent(ListType pType) {
        listType = pType;
    }

    /**
     * @param pData sets new action event with data
     * @param pType sets new action event for list type
     */
    public DataActionEvent(T pData, ListType pType) {
        this(pType);
        data = pData;
    }

    /**
     * get the data stored in the Event
     *
     * @return stored data
     */
    public T getData() {
        return data;
    }

    /**
     * set the data stored in the Event
     *
     * @param pData data to set
     */
    public void setData(T pData) {
        data = pData;
    }

    /**
     * @return list type of action event
     */
    public ListType getListType() {
        return listType;
    }

}
