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
public abstract class SearchListFormatNumber extends SearchListFormat<Class<? extends Number>> {

    private static final long serialVersionUID = 1L;

    public enum SIGN_TYPE {
        POSITIVE, NEGATIVE, BOTH, BOTH_EXCLUDING_ZERO
        /*, POSITIVE_EXCLUDING_ZERO, NEGATIVE_EXCLUDING_ZERO */ }

//    private StringConverter<String> converter;
//    private Pattern pattern;
    private SIGN_TYPE signType = SIGN_TYPE.BOTH;

    public SearchListFormatNumber() {
        super(Number.class);
    }

    protected SearchListFormatNumber(final Class<? extends Number> pDataType) {
        super(pDataType);
    }

//    public SearchListFormatInteger setPattern(Pattern pPattern) {
//        pattern = pPattern;
//        return this;
//    }
//
//    public Pattern getPattern() {
//        return pattern;
//    }
//    public String format(String pString, String pReplacement) {
//        if (pString == null) {
//            return "";
//        }
//        if (pattern == null) {
//            return pString;
//        }
//        return pattern.matcher(pString).replaceAll(pReplacement);
//    }
//
//    public String format(String pString) {
//        return format(pString, "");
//    }
    public SearchListFormatNumber setMaxLength(final int pMaxLength) {
        maxLength = pMaxLength;
        return this;
    }

    public SearchListFormatNumber setSignType(final SIGN_TYPE pType) {
        signType = pType == null ? SIGN_TYPE.BOTH : pType;
        return this;
    }

    public SIGN_TYPE getSignType() {
        return signType;
    }

    public boolean isNegative() {
        return SIGN_TYPE.NEGATIVE.equals(signType);
    }

    public boolean isPositive() {
        return SIGN_TYPE.POSITIVE.equals(signType);
    }

    public boolean isBoth() {
        return SIGN_TYPE.BOTH.equals(signType);
    }

//    public boolean isBothExcludingZero() {
//        return SIGN_TYPE.BOTH_EXCLUDING_ZERO.equals(signType);
//    }
//    
//    public boolean isNegativeExcludingZero() {
//        return SIGN_TYPE.NEGATIVE_EXCLUDING_ZERO.equals(signType);
//    }
//    
//    public boolean isPositiveExcludingZero() {
//        return SIGN_TYPE.POSITIVE_EXCLUDING_ZERO.equals(signType);
//    }
    public boolean canBeNegative() {
        return isBoth() || isNegative() /* || isNegativeExcludingZero() */;
    }

    public boolean canBePositive() {
        return isBoth() || isPositive() /* || isPositiveExcludingZero() */;
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
