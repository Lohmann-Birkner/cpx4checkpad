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

import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.shared.lang.Lang;

/**
 * Pepp column
 *
 * @author wilde
 * @param <T> table item
 */
public abstract class PcclColumn<T> extends IntegerColumn<T> {

    public PcclColumn() {
        super(Lang.getPCCL());
    }

    @Override
    public Integer extractValue(T pValue) {
        TGroupingResults drg = getValue(pValue);
        if (drg != null) {
            return drg.getGrpresPccl();
        }
        return 0;
    }

    public abstract TGroupingResults getValue(T pValue);

}
