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
package de.lb.cpx.client.core.model.fx.tableview.column.pepp;

import de.lb.cpx.client.core.model.fx.tableview.column.IntegerColumn;
import de.lb.cpx.model.TCasePepp;
import de.lb.cpx.shared.lang.Lang;

/**
 * Column for pepp code
 *
 * @author wilde
 * @param <T> type
 */
public abstract class PeppDaysIntensiveColumn<T> extends IntegerColumn<T> {

    public PeppDaysIntensiveColumn() {
        super(Lang.getDaysIntensiv());
    }

    public abstract TCasePepp getValue(T pValue);

    @Override
    public Integer extractValue(T pValue) {
        TCasePepp pepp = getValue(pValue);
        if (pepp != null) {
            return pepp.getPeppcDaysIntensiv();
        }
        return null;
    }
}
