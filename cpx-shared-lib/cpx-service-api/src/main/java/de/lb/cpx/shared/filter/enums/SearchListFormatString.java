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

import java.util.regex.Pattern;

/**
 *
 * @author Dirk Niemeier
 */
public class SearchListFormatString extends SearchListFormat<Class<? extends String>> {

    private static final long serialVersionUID = 1L;

//    private static final Logger LOG = Logger.getLogger(SearchListFormatString.class.getName());
//    private StringConverter<String> converter;
    private Pattern pattern;

    public SearchListFormatString() {
        super(String.class);
    }

    public SearchListFormatString setPattern(Pattern pPattern) {
        pattern = pPattern;
        return this;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public String format(String pString, String pReplacement) {
        if (pString == null) {
            return "";
        }
        if (pattern == null) {
            return pString;
        }
        return pattern.matcher(pString).replaceAll(pReplacement);
    }

    public String format(String pString) {
        return format(pString, "");
    }

    public SearchListFormatString setMaxLength(final int pMaxLength) {
        maxLength = pMaxLength;
        return this;
    }

    /*
    @Override
    public SearchListDisplayResult getDisplayFormat(final Object pItem, final SearchListDisplayFormatter pFormatter) {
        String value = toStr(pItem);
        if(getPattern()!= null){
            //optional formating can be specified in formatString
            value = format(value);
        }
        if (pFormatter != null) {
            return pFormatter.format(pItem, value);
        }
        final String tooltip = "";
        return new SearchListDisplayResult(value, tooltip);
    }
     */
}
