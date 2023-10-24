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
 *    2019  shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.tooltip;

import de.lb.cpx.model.TCaseOpsGrouped;
import de.lb.cpx.model.TCaseSupplFee;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 *
 * @author shahin
 */
public class DrgSuppFeeLayout extends SimpleMathLayout {

    public DrgSuppFeeLayout(double pResult, Collection<TCaseOpsGrouped> pOpGrps) {
        super("+", Lang.toDecimal(pResult, 2) + Lang.getCurrencySymbol());
        setLineObjects(computeList(pOpGrps));
    }

    private LineObject getLineObjectForGrade(TCaseSupplFee supFee) {
        String desc = "";
        if (supFee.getCsuplTypeEn().equals(SupplFeeTypeEn.ZE)) {
            desc = supFee.getCsuplfeeCode();
        }

        String res = Lang.toDecimal(supFee.getCsuplValue(), 2) + Lang.getCurrencySymbol();
        return new LineObject(desc, res);

    }

    private LineObject[] computeList(Collection<TCaseOpsGrouped> pOpGrps) {

        if (!pOpGrps.isEmpty()) {
            List<LineObject> objects = new ArrayList<>();

            for (TCaseOpsGrouped pOpGrp : pOpGrps) {
                TCaseSupplFee supFee = pOpGrp.getCaseSupplFees();
                if (supFee != null && Double.doubleToRawLongBits(supFee.getCsuplValue()) != Double.doubleToRawLongBits(0.0d)) {
                    objects.add(getLineObjectForGrade(supFee));
                }

            }
            return objects.toArray(new LineObject[objects.size()]);
        }
        return new LineObject[]{new LineObject("Keine Ops vorhanden", "")};
    }

}
