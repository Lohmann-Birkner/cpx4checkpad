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
import de.lb.cpx.model.TCaseDrgCareGrades;
import de.lb.cpx.model.enums.DrgCorrTypeEn;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.NotNull;

/**
 * Implementation of the Case Version Tooltip to unify behaviour
 *
 * @author wilde
 */
public class DrgRevenueLayout extends SimpleMathLayout {

    public DrgRevenueLayout(double pResult, double pBaserate, double pCw) {
        super("+", Lang.toDecimal(pResult, 2) + Lang.getCurrencySymbol());
        setLineObjects(new LineObject(Lang.getRevenueMathDrgCase(pCw, Lang.toDecimal(pBaserate, 2) + Lang.getCurrencySymbol()), Lang.toDecimal(pBaserate * pCw, 2) + Lang.getCurrencySymbol()));
    }

    public DrgRevenueLayout(double pResult, double pBaserate,double pCareBaserate, @NotNull TCaseDrg pDrg) {
        super("+", Lang.toDecimal(pResult, 2) + Lang.getCurrencySymbol());
        Objects.requireNonNull(pDrg, "GrouperResult can not be null!");
        List <LineObject> lineList = new ArrayList<>();
        DrgCorrTypeEn corrType = pDrg.getDrgcTypeOfCorrEn();
        lineList.add(getDrgLine(pBaserate, pDrg.getDrgcCwCatalog(), pDrg.getCatalogCwRevenue(pBaserate)));
        if(isDeduction(corrType)){

                    lineList.add(getDeductionLine(pBaserate, pDrg.getDrgcCwCorrDay(), pDrg.getDrgcDaysCorr(), pDrg.getRevenueCatalogCorrected(pBaserate)));
//                    getCareLine(pCareBaserate, pGrouperResult.getDrgcCareCwDay(), pGrouperResult.getDrgcCareDays(), pGrouperResult.getCareRevenue(pCareBaserate)));
        }else if(isSurcharge(corrType)){
            lineList.add(getSurchargeLine(pBaserate, pDrg.getDrgcCwCorrDay(), pDrg.getDrgcDaysCorr(), pDrg.getRevenueCatalogCorrected(pBaserate)));
//                    getCareLines(pCareBaserate, pGrouperResult));
//                    getCareLine(pCareBaserate, pGrouperResult.getDrgcCareCwDay(), pGrouperResult.getDrgcCareDays(), pGrouperResult.getCareRevenue(pCareBaserate)));
        }
        if(pDrg.getDrgcCareDays() > 0){
            lineList.addAll(getCareLines(pCareBaserate, pDrg));
        }

        setLineObjects(lineList.toArray(new LineObject[lineList.size()]));
    }
    
    private List<LineObject> getCareLines(double pCareBaserate, @NotNull TCaseDrg pDrg){
        List<LineObject> retList = new ArrayList<>();
        Set<TCaseDrgCareGrades> grades = pDrg.getDrgCareGrades();
        if(grades == null || grades.isEmpty() || grades.size() == 1){
            retList.add(getCareLine(pCareBaserate, pDrg.getDrgcCareCwDay(), pDrg.getDrgcCareDays(), pDrg.getCareRevenue(pCareBaserate)));
        }else{

            List<TCaseDrgCareGrades>sortGrades = new ArrayList<>(grades);
            Collections.sort(sortGrades);
             retList.add(new LineObject(Lang.getRevenueMathDrgCareGradeLayout(
                       Lang.toDecimal(pDrg.getDrgcCareCw(), 4), Lang.toDecimal(pDrg.getDrgcCareCwDay()), String.valueOf(pDrg.getDrgcCareDays())),
                       Lang.toDecimal(pDrg.getCareRevenue(pCareBaserate), 2) + Lang.getCurrencySymbol()));

            for(TCaseDrgCareGrades grade: sortGrades){
                retList.add(new LineObject(grade.getDrgCareSortInd() == 0?"davon:":"     ",
                Lang.getRevenueMathDrgCareGrade(Lang.toDecimal(grade.getDrgCareBaserate(), 2),
                Lang.getCurrencySymbol(),
                Lang.toDate(grade.getDrgCareFrom()),
                Lang.toDate(grade.getDrgCareTo()),
                String.valueOf(grade.getDrgCareDays())),
                Lang.toDecimal(grade.getGradeRevenue(), 2) +
                Lang.getCurrencySymbol()));
            }
        }
        return retList;
    }
    
    private LineObject getDrgLine(Double pBaserate, double pCatalogCw, double pCatalogRevenue){
        pBaserate = Objects.requireNonNullElse(pBaserate, 0.0);
        return new LineObject(Lang.getRevenueMathDrgCatalogLayout(
                    Lang.toDecimal(pBaserate, 2), Lang.getCurrencySymbol(), Lang.toDecimal(pCatalogCw, 3)),
                    Lang.toDecimal(pCatalogRevenue, 2) + Lang.getCurrencySymbol());
    }
    private LineObject getCareLine(Double pBaserate, double pCareCwDay, int pCareDays, double pCareRevenue){
        pBaserate = Objects.requireNonNullElse(pBaserate, 0.0);
        return new LineObject(Lang.getRevenueMathDrgCareLayout(
                    Lang.toDecimal(pBaserate, 2), Lang.getCurrencySymbol(), Lang.toDecimal(pCareCwDay, 4), pCareDays),
                    Lang.toDecimal(pCareRevenue, 2) + Lang.getCurrencySymbol());
    }
    private LineObject getDeductionLine(Double pBaserate, double pCwCorrDay, int pDaysCorr, double pCorrRevenue){
        pBaserate = Objects.requireNonNullElse(pBaserate, 0.0);
        return new LineObject("-",Lang.getRevenueMathDrgCorrDeductionLayout(
                    Lang.toDecimal(pBaserate, 2), Lang.getCurrencySymbol(), Lang.toDecimal(pCwCorrDay, 3), pDaysCorr),
                    Lang.toDecimal(pCorrRevenue, 2) + Lang.getCurrencySymbol());
    }
    private LineObject getSurchargeLine(Double pBaserate, double pCwCorrDay, int pDaysCorr, double pCorrRevenue){
        pBaserate = Objects.requireNonNullElse(pBaserate, 0.0);
        return new LineObject("+",Lang.getRevenueMathDrgCorrSurchargeLayout(
                    Lang.toDecimal(pBaserate, 2), Lang.getCurrencySymbol(), Lang.toDecimal(pCwCorrDay, 3), pDaysCorr),
                    Lang.toDecimal(pCorrRevenue, 2) + Lang.getCurrencySymbol());
    }
    private boolean isDeduction(DrgCorrTypeEn pCorrType){
        pCorrType = Objects.requireNonNullElse(pCorrType,DrgCorrTypeEn.no);
        switch(pCorrType){
            case Deduction:
            case DeductionTransfer:
            case DeductionTransferAdm:
            case DeductionTransferDis:
                return true;
            default:
                return false;
        }
    }
    private boolean isSurcharge(DrgCorrTypeEn pCorrType){
        pCorrType = Objects.requireNonNullElse(pCorrType,DrgCorrTypeEn.no);
        switch(pCorrType){
            case Surcharge:
                return true;
            default:
                return false;
        }
    }
}
