/*
 * Copyright (c) 2019 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.tableview.column.drg;

import de.lb.cpx.client.core.model.fx.tableview.column.DoubleColumn;
import de.lb.cpx.model.TCaseDrg;
import de.lb.cpx.model.enums.DrgCorrTypeEn;
import javafx.scene.control.Tooltip;

/**
 * Drg correction Column, 
 * @author wilde
 * @param <T> type of object to fetch drg data from
 */
public abstract class DrgCorrectionColumn<T> extends DoubleColumn<T>{
    
    public DrgCorrectionColumn() {
        super("Zu-/Abschlag", 3);
    }
    @Override
    public Double extractValue(T pValue) {
        return getCorrection(getValue(pValue));
    }

    public abstract TCaseDrg getValue(T pValue);

    private Double getCorrection(TCaseDrg pDrg) {
        if (pDrg == null) {
            return 0.0d;
        }
        double cwCorr = pDrg.getDrgcCwCorr();
        DrgCorrTypeEn cwEnum = pDrg.getDrgcTypeOfCorrEn();
        
        switch(cwEnum){
            case Surcharge:
                return cwCorr;
            case Deduction:
            case DeductionTransfer:
            case DeductionTransferAdm:
            case DeductionTransferDis:
                return Math.abs(cwCorr) * -1;
            default:
                return 0.0;
        }
//        if (Double.doubleToRawLongBits(cwCorr) != Double.doubleToRawLongBits(0.0d) && cwEnum != null && cwEnum.equals(DrgCorrTypeEn.Surcharge)) {
//            return cwCorr;
//        }
//        return 0.0;
    }
    protected Tooltip getCellTooltip(TCaseDrg pDrg){
        
        if(pDrg == null){
            return null;
        }
        switch(pDrg.getDrgcTypeOfCorrEn()){
            case Surcharge:
                return new Tooltip("Zuschlag, wegen überschreiten der OGVD!");
            case Deduction:
                return new Tooltip("Abschlag, wegen unterschreiten der UGVD!");
            case DeductionTransfer:
                return new Tooltip("Abschlag, wegen Verlegung!");
            case DeductionTransferAdm:
                return new Tooltip("Abschlag, wegen Verlegung - Aufnahme!");
            case DeductionTransferDis:
                return new Tooltip("Abschlag, wegen Verlegung - Entlassung!");
            default:
                return null;
        }
    }
}
