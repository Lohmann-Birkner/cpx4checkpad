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

import de.lb.cpx.client.core.model.fx.tableview.column.DoubleColumn;
import de.lb.cpx.model.TCaseDrg;
import de.lb.cpx.shared.lang.Lang;

/**
 *
 * @author wilde
 * @param <T> type
 */
public abstract class DrgCwEffColumn<T> extends DoubleColumn<T> {

    public DrgCwEffColumn() {
        super(Lang.getCaseResolveCWEff(), 3);
    }

    @Override
    public Double extractValue(T pValue) {
        TCaseDrg drg = getValue(pValue);
        if (drg != null) {
            return drg.getDrgcCwEffectiv();
        }
        return 0.0;
    }

    public abstract TCaseDrg getValue(T pValue);

}
