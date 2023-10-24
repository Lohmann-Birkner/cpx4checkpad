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

import java.util.Date;

/**
 *
 * @author Dirk Niemeier
 */
public class SearchListFormatDateTime extends SearchListFormat<Class<? extends Date>> {

    private static final long serialVersionUID = 1L;

    public boolean truncTime = false;
    private boolean showTruncTime = true;

    public SearchListFormatDateTime() {
        super(Date.class);
    }

    public SearchListFormatDateTime setTruncTime(final boolean pTruncTime) {
        truncTime = pTruncTime;
        return this;
    }

    public boolean isTruncTime() {
        return truncTime;
    }

    public SearchListFormatDateTime setShowTruncTime(final boolean pShowTruncTime) {
        showTruncTime = pShowTruncTime;
        return this;
    }

    public boolean isShowTruncTime() {
        return showTruncTime;
    }

    /*
    @Override
    public SearchListDisplayResult getDisplayFormat(final Object pItem, final SearchListDisplayFormatter pFormatter) {
        final String value = Lang.toDate((Date) pItem);
        if (pFormatter != null) {
            return pFormatter.format(pItem, value);
        }
        String tooltip = "";
        if (isShowTruncTime()) {
            tooltip = Lang.toTime((Date) pItem);
        }
        return new SearchListDisplayResult(value, tooltip);
    }
     */
}
