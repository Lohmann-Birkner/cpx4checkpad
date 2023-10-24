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
package de.lb.cpx.client.core.model.fx.tab;

/**
 * ActionType for the Master-Detail, defines what should be updated
 *
 * @author wilde
 */
public class ShowTabAction {

    private final TwoLineTab.TabType type;
    private final long id;

    public ShowTabAction(TwoLineTab.TabType lookup, long idOfDbObject) {
        this.type = lookup;
        this.id = idOfDbObject;
    }

    public TwoLineTab.TabType getType() {
        return type;
    }

    public long getId() {
        return id;
    }

}
