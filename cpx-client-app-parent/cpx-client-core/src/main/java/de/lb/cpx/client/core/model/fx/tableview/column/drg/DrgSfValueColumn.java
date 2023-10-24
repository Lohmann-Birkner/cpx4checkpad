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
package de.lb.cpx.client.core.model.fx.tableview.column.drg;

import de.lb.cpx.client.core.model.fx.tableview.column.CurrencyColumn;
import de.lb.cpx.model.TCaseOpsGrouped;
import de.lb.cpx.model.TCaseSupplFee;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.shared.lang.Lang;

/**
 *
 * @author wilde
 * @param <T> type
 */
public abstract class DrgSfValueColumn<T> extends CurrencyColumn<T> {

    public DrgSfValueColumn() {
        super(Lang.getCaseResolveSupplementaryValue());
        setEmptyText("");
    }

    @Override
    public Double extractValue(T pValue) {
        TCaseOpsGrouped grpOps = getValue(pValue);
        if (grpOps != null && grpOps.getCaseSupplFees() != null) {
            TCaseSupplFee supFee = grpOps.getCaseSupplFees();
            if (supFee.getCsuplTypeEn().equals(SupplFeeTypeEn.ZE)) {
                return supFee.getCsuplValue();
            }
        }
        return 0.0;
    }

    public abstract TCaseOpsGrouped getValue(T pValue);

}
