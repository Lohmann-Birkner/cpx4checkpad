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

import de.lb.cpx.client.core.model.fx.tableview.column.CurrencyColumn;
import de.lb.cpx.shared.lang.Lang;

/**
 * Supplementary fee (Zusatzentgeltbetrag) column
 *
 * @author wilde
 * @param <T> table type
 */
public abstract class PeppSuppFeeColumn<T> extends CurrencyColumn<T> {

    public PeppSuppFeeColumn() {
        super(Lang.getSupplFeeValuePEPP());
    }

    @Override
    public Double extractValue(T pValue) {
        return getValue(pValue);
    }

    public abstract Double getValue(T pValue);
}
