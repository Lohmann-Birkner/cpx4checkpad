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
import de.lb.cpx.model.enums.DrgCorrTypeEn;
import de.lb.cpx.shared.lang.Lang;

/**
 * Deduction (Abschlag) column
 *
 * @author wilde
 * @param <T> table item
 */
public abstract class DrgDeductionColumn<T> extends DoubleColumn<T> {

//    private static final String DEFAULT = "0,000";
    public DrgDeductionColumn() {
        super(Lang.getDeduction(), 3);
    }

    @Override
    public Double extractValue(T pValue) {
        return getDeduction(getValue(pValue));
    }

    public abstract TCaseDrg getValue(T pValue);

    private Double getDeduction(TCaseDrg pDrg) {
        if (pDrg == null) {
            return null;
        }
        double cwCorr = pDrg.getDrgcCwCorr();
        DrgCorrTypeEn cwEnum = pDrg.getDrgcTypeOfCorrEn();
        if (Double.doubleToRawLongBits(cwCorr) != Double.doubleToRawLongBits(0.0d) && cwEnum != null
                && (cwEnum.equals(DrgCorrTypeEn.Deduction) || cwEnum.equals(DrgCorrTypeEn.DeductionTransfer)
                || cwEnum.equals(DrgCorrTypeEn.DeductionTransferAdm) || cwEnum.equals(DrgCorrTypeEn.DeductionTransferDis))) {
            return cwCorr;
        }
        return 0.0;
    }
}
