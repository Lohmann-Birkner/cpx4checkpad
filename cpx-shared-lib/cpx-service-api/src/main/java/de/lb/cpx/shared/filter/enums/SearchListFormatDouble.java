/* 
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.filter.enums;

/**
 *
 * @author Dirk Niemeier
 */
public class SearchListFormatDouble extends SearchListFormatNumber {

    private static final long serialVersionUID = 1L;

    public SearchListFormatDouble() {
        super(Double.class);
    }

    /*
    @Override
    public SearchListDisplayResult getDisplayFormat(final Object pItem, final SearchListDisplayFormatter pFormatter) {
        final String value;
        Serializable dataType = getDateType();
        if (Long.class == dataType) {
            value = Lang.toDecimal((Long) pItem);
        } else if (Double.class == dataType) {
            //LONG, NUMBER
            value = Lang.toDecimal((Double) pItem);
        } else if (Float.class == dataType) {
            //LONG, NUMBER
            value = Lang.toDecimal(BigDecimal.valueOf((Float) pItem));
        } else if (BigDecimal.class == dataType) {
            //BIGDECIMAL
            final BigDecimal bd;
            if (pItem instanceof Long) {
                bd = BigDecimal.valueOf((Long) pItem);
            } else if (dataType instanceof BigDecimal) {
                bd = ((BigDecimal) pItem).setScale(((BigDecimal) dataType).scale(), RoundingMode.HALF_UP);
            } else if (pItem instanceof Number) {
                bd = BigDecimal.valueOf(((Number) pItem).intValue());
            } else {
                LOG.log(Level.WARNING, "Unhandled big decimal datatype " + pItem.getClass().getSimpleName() + " / " + dataType.toString() + " for value " + toStr(pItem) + ", use empty value instead");
                bd = null;
            }
            value = bd == null ? "" : Lang.toDecimal(bd);
        } else {
            LOG.log(Level.WARNING, "Unhandled decimal datatype " + pItem.getClass().getSimpleName() + " / " + dataType.toString() + " for value " + toStr(pItem) + ", use empty string instead");
            value = "";
        }
        if (pFormatter != null) {
            return pFormatter.format(pItem, value);
        }
        final String tooltip = "";
        return new SearchListDisplayResult(value, tooltip);
    }
     */
}
