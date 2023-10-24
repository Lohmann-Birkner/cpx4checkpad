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
package de.lb.cpx.client.core.model.fx.tableview.column;

import de.lb.cpx.shared.lang.Lang;

/**
 * revenue (erloes) column
 *
 * @author wilde
 * @param <T> table item
 */
public abstract class RevenueColumn<T> extends CurrencyColumn<T> {

    public RevenueColumn() {
        super(Lang.getRevenue());
    }

    @Override
    public Double extractValue(T pValue) {
        return getValue(pValue);
    }

    public abstract Double getValue(T pValue);

}
