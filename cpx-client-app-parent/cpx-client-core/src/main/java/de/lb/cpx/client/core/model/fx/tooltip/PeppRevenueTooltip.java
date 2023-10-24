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

import de.lb.cpx.model.TCasePeppGrades;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implementation of the Case Version Tooltip to unify behaviour
 *
 * @author wilde
 */
public class PeppRevenueTooltip extends SimpleMathTooltip {

    private final double baserate;
    private final int yearOfValidity;

    public PeppRevenueTooltip(double pResult, double pBaserate, Collection<TCasePeppGrades> pPeppGrades, int pYearOfValidity) {
        super("-", Lang.toDecimal(pResult, 2) + Lang.getCurrencySymbol());
        baserate = pBaserate;
        yearOfValidity = pYearOfValidity;
        setLineObjects(computeList(pPeppGrades));
    }

    private LineObject getLineObjectForGrade(TCasePeppGrades pGrade, int pYearOfValidity) {
        String gradeNumber = String.valueOf(pGrade.getPeppcgrNumber());
        String cw = Lang.toDecimal(pGrade.getPeppcgrCw(), 4);
        String br = Lang.toDecimal(baserate, 2) + Lang.getCurrencySymbol();
        String desc = pYearOfValidity < 2015 ? Lang.getRevenueMathPeppLt2015(gradeNumber, cw, br) : Lang.getRevenueMathPeppGt2015(gradeNumber, cw, br);
        String res = Lang.toDecimal(pGrade.getPeppcgrCw() * baserate, 2) + Lang.getCurrencySymbol();
        return new LineObject(desc, res);
    }

    private LineObject[] computeList(Collection<TCasePeppGrades> pPeppGrades) {
        if (pPeppGrades.isEmpty()) {
//            if (yearOfValidity > 2015) {
                return new LineObject[]{new LineObject("Keine Stufe/Klasse vorhanden", "0,00" + Lang.getCurrencySymbol())};
            }
//        }
        List<LineObject> objects = new ArrayList<>();
        for (TCasePeppGrades grade : pPeppGrades) {
            objects.add(getLineObjectForGrade(grade, yearOfValidity));
        }
        return objects.toArray(new LineObject[objects.size()]);
    }

}
