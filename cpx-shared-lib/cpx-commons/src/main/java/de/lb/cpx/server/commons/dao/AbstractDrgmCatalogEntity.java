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
 *    2019  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commons.dao;

import de.lb.cpx.service.information.CatalogTypeEn;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 *
 * @author Dirk Niemeier
 */
@MappedSuperclass
public abstract class AbstractDrgmCatalogEntity extends AbstractCatalogEntity {

    private static final long serialVersionUID = 1L;

    @Transient
    public abstract String get2DrgmMapping();

    protected String checkDate4MappingSuppl(Date date) {
        if (date == null || !showDrgmDate()) {
            return "";
        }
        SimpleDateFormat test = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return test.format(date);

    }

    protected String checkDate4Mapping(Date date) {
        if (date == null || !showDrgmDate()) {
            return "";
        }
        SimpleDateFormat test = new SimpleDateFormat("yyyyMMdd");
        return test.format(date);
    }

    protected String checkFloat4Mapping(CatalogTypeEn pCatalogType, BigDecimal pValue) {
        if (pValue == null) {
            return "0.0";
        }
        float f = pValue.floatValue();
        return String.valueOf(f);
//        switch(pCatalogType){
//            case PEPP:
//            case ET:
//                return mNumberFormatPepp.format(f);
//            case ZE:
//            case ZP:
//            return mNumberFormatSuppl.format(f);
//            case DRG:
//            default:
//               return mNumberFormatDrg.format(f);
//        }
    }

    /**
     * Prüft die Bedignung ob das Datum(von/bis) in die DRGM - Datei übernommen
     * sein soll
     *
     * @return show drgm date?
     */
    protected abstract boolean showDrgmDate();

}
