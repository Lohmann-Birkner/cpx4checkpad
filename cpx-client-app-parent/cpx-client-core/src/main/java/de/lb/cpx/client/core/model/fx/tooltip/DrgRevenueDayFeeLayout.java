/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.tooltip;

import de.lb.cpx.model.TCaseDrg;
import de.lb.cpx.shared.lang.Lang;
import java.util.Objects;

/**
 *
 * @author gerschmann
 */
public class DrgRevenueDayFeeLayout extends SimpleMathLayout {

    public DrgRevenueDayFeeLayout(double pResult, double pDayFee, int pDays, double pDayFeeRevenue, Double pCareBaserate, double pCareCwDay, int pCareDays, double pCareRevenue) {
        super("+", Lang.toDecimal(pResult, 2) + Lang.getCurrencySymbol());
        Objects.requireNonNullElse(pCareBaserate, 0.0);
        setLineObjects(new LineObject(Lang.getRevenueMathDrgDayFee(Lang.toDecimal(pDayFee, 2), Lang.getCurrencySymbol(), pDays),
                Lang.toDecimal(pDayFeeRevenue, 2) + Lang.getCurrencySymbol()),
                new LineObject(Lang.getRevenueMathDrgCareLayout(Lang.toDecimal(pCareBaserate, 2), Lang.getCurrencySymbol(), Lang.toDecimal(pCareCwDay, 4), pCareDays),
                        Lang.toDecimal(pCareRevenue, 2) + Lang.getCurrencySymbol()));
    }
    
    public DrgRevenueDayFeeLayout(double pResult,Double pCareBaserate, TCaseDrg pGrouperResult){
        this(pResult, pGrouperResult.getDrgcNegoFee2Day(), pGrouperResult.getDrgcNegoFeeDays(),pGrouperResult.getNegotiatedFeeRevenue(),pCareBaserate,pGrouperResult.getCareCwDays(),pGrouperResult.getDrgcCareDays(),pGrouperResult.getCareRevenue(pCareBaserate));
        
    }

}
