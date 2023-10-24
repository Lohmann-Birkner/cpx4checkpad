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
import java.util.Collections;
import java.util.List;

/**
 * Implementation of the Case Version Tooltip to unify behaviour
 *
 * @author wilde
 */
public class PeppRevenueLayout extends SimpleMathLayout {

//    private final double baserate;
    private final int yearOfValidity;
    private final int gradeNumber;

    public PeppRevenueLayout(double pResult, Collection<TCasePeppGrades> pPeppGrades, int pGradeNumber, int pYearOfValidity) {
        super("+", Lang.toDecimal(pResult, 2) + Lang.getCurrencySymbol());
//        baserate = pBaserate;
        yearOfValidity = pYearOfValidity;
        gradeNumber = pGradeNumber;

        setLineObjects(computeList(pPeppGrades));
    }

    private LineObject getLineObjectForGrade(TCasePeppGrades pGrade, int pYearOfValidity) {

        String gradeNum = pYearOfValidity < 2015?String.valueOf(pGrade.getPeppcgrNumber()):String.valueOf(this.gradeNumber);
//        String cw = Lang.toDecimal(pGrade.getPeppcgrCw(), 4);
        String cwDay = Lang.toDecimal(pGrade.getPeppcgrDays() > 0 ? pGrade.getPeppcgrCw() / pGrade.getPeppcgrDays() : 0, 4);
        String br = Lang.toDecimal(pGrade.getPeppcgrBaserate(), 2) + Lang.getCurrencySymbol();
        String days = String.valueOf(pGrade.getPeppcgrDays() > 0 ? pGrade.getPeppcgrDays() : 0);
        String brFrom = Lang.toDate(pGrade.getPeppcgrFrom());
        String brTo = Lang.toDate(pGrade.getPeppcgrTo());
        String desc = pYearOfValidity < 2015 ? Lang.getRevenueMathPeppLt2015(gradeNum, cwDay, brFrom, brTo, br, days) : Lang.getRevenueMathPeppGt2015(gradeNum, cwDay, brFrom, brTo, br, days);
//            String desc = "Vergütungsklasse " + gradeNumber +  " , Erlös= CW pro Tag(" + cwDay + " ) x Baserate von " + brFrom + " bis " + brTo + " (" + br + " ) x ( "+days+" )Tage=";
        String res = "";
//        if (Double.doubleToRawLongBits(pGrade.getPeppcgrBaserate()) != Double.doubleToRawLongBits(0.0d)) {
            res = Lang.toDecimal((Math.round((pGrade.getPeppcgrCw() * pGrade.getPeppcgrBaserate() / pGrade.getPeppcgrDays()) * 100.0) / 100.0) * pGrade.getPeppcgrDays(), 2) + Lang.getCurrencySymbol();
 //       }

        return new LineObject(desc, res);
    }

    private LineObject[] computeList(Collection<TCasePeppGrades> pPeppGrades) {//muss noch sortiert werden
        if (pPeppGrades.isEmpty()) {
//            if (yearOfValidity > 2015) {
                return new LineObject[]{new LineObject("Keine Stufe/Klasse vorhanden", "0,00" + Lang.getCurrencySymbol())};
            }
//        }
        List <TCasePeppGrades> sortList = new ArrayList<>(pPeppGrades);
        Collections.sort(sortList);
        List<LineObject> objects = new ArrayList<>();

        for (TCasePeppGrades grade : sortList) {
//            if (Double.doubleToRawLongBits(grade.getPeppcgrBaserate()) != Double.doubleToRawLongBits(0.0d)) {
                objects.add(getLineObjectForGrade(grade, yearOfValidity));
//            }
        }
        return !objects.isEmpty() ? objects.toArray(new LineObject[objects.size()]) : new LineObject[]{new LineObject("Kein Baserate vorhanden", "0,00" + Lang.getCurrencySymbol())};
    }

}
