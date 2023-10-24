/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.ruleeditor.connection.jms;

import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.connection.jms.BasicStatusBroadcastHandler;
import de.lb.cpx.shared.dto.broadcast.BroadcastOriginEn;
import de.lb.cpx.shared.dto.broadcast.StatusBroadcastDTO;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.util.Callback;

/**
 *
 * @author wilde
 */
public class StatusBroadcastHandler extends BasicStatusBroadcastHandler<long[]> {
    
    public StatusBroadcastHandler() {
        super();
    }

    @Override
    public String getFilter() {
        //final String filter = super.getFilter();
        String filter = "(Origin = '" + BroadcastOriginEn.CASE_TO_COMMON.name() + "')";
        long userId = Session.instance().getCpxUserId();
        if (userId != 0L) {
            filter += " AND (UserId = '" + userId + "')";
        }
        return filter;
    }

    @Override
    protected Callback<StatusBroadcastDTO<long[]>, Void> createDtoListener() {
        return new Callback<StatusBroadcastDTO<long[]>, Void>() {
            @Override
            public Void call(StatusBroadcastDTO<long[]> dto) {
                //customize here
                long[] ccaseIds = dto.getResult();
                resultProperty.set(ccaseIds);
                showNotification(dto, dto.getComment());
                return null;
            }
        };
    }

    private final ReadOnlyObjectWrapper<long[]> resultProperty = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<long[]> resultProperty() {
        return resultProperty.getReadOnlyProperty();
    }

    public long[] getResult() {
        return resultProperty().get();
    }

}
