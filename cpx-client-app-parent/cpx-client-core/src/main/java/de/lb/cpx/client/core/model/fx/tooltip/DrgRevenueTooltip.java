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
package de.lb.cpx.client.core.model.fx.tooltip;

import de.lb.cpx.model.TCaseDrg;

/**
 * Implementation of the Case Version Tooltip to unify behaviour
 *
 * @author wilde
 */
public class DrgRevenueTooltip extends BasicTooltip/*SimpleMathTooltip*/ {

    public DrgRevenueTooltip(double pResult, double pBaserate, double pCareBaserate, TCaseDrg pGrouperResult) {
        super(200, 5000, 100, true);
//        super("", Lang.toDecimal(pResult, 2) + Lang.getCurrencySymbol());
//        setLineObjects(new LineObject(Lang.getRevenueMathDrgCase(pCw, Lang.toDecimal(pBaserate, 2) + Lang.getCurrencySymbol()), Lang.toDecimal(pBaserate * pCw, 2) + Lang.getCurrencySymbol()));
        setGraphic(new DrgRevenueLayout(pResult, pBaserate, pCareBaserate, pGrouperResult));
    }

}
