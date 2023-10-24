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

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author Dirk Niemeier
 * @param <T> data type
 */
public abstract class SearchListFormat<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final T dataType;
    protected int maxLength = 0; //amount of characters in text field
    private final boolean isDate;
    private final boolean isDouble;
    private final boolean isBoolean;
    private final boolean isString;
    private final boolean isInteger;
    private final boolean isNumber;
    private final boolean isEnum;
    private final boolean isMap;
    private final boolean isUserMap;
    private final boolean isReminderSubjectMap;
    private final boolean isProcessTopicMap;
    private final boolean isMdkAuditReasonsMap;
    private final boolean isActionSubjectMap;
    private final boolean isMdkStatesMap;
//    private final boolean isDepartmentMap;

    public final T getDataType() {
        return dataType;
    }

    public int getMaxLength() {
        return maxLength;
    }

    protected SearchListFormat(final T pDataType) {
        if (pDataType == null) {
            throw new IllegalArgumentException("data type cannot be null!");
        }
        dataType = pDataType;

        isNumber = Number.class.isAssignableFrom(((Class<?>) dataType));
        isInteger = isNumber && (Integer.class == dataType || dataType instanceof Integer);
        isDouble = isNumber && (Double.class == dataType || dataType instanceof Double);

        isDate = Date.class == dataType || dataType instanceof Date;

        isBoolean = Boolean.class == dataType || dataType instanceof Boolean;

        isString = String.class == dataType || dataType instanceof String;

        isEnum = Enum.class.isAssignableFrom(((Class<?>) dataType));

        isMap = Map.class.isAssignableFrom(((Class<?>) dataType));

        isUserMap = isMap && (UserMap.class == dataType || dataType instanceof UserMap);
        isReminderSubjectMap = isMap && (ReminderSubjectMap.class == dataType || dataType instanceof ReminderSubjectMap);
        isProcessTopicMap = isMap && (ProcessTopicMap.class == dataType || dataType instanceof ProcessTopicMap);
        isMdkAuditReasonsMap = isMap && (MdkAuditReasonsMap.class == dataType || dataType instanceof MdkAuditReasonsMap);
        isActionSubjectMap = isMap && (ActionSubjectMap.class == dataType || dataType instanceof ActionSubjectMap);
        isMdkStatesMap = isMap && (MdkStatesMap.class == dataType || dataType instanceof MdkStatesMap);
//        isDepartmentMap = isMap && (DepartmentShortNameMap.class == dataType || dataType instanceof DepartmentShortNameMap);
    }

    public boolean isDate() {
        return isDate;
    }

    public boolean isDouble() {
        return isDouble;
    }

    public boolean isBoolean() {
        return isBoolean;
    }

    public boolean isString() {
        return isString;
    }

    public boolean isInteger() {
        return isInteger;
    }

    public boolean isNumber() {
        return isNumber;
    }

    public boolean isEnum() {
        return isEnum;
    }

    public boolean isMap() {
        return isMap;
    }

//    public boolean isHospital() {
//        return SearchListFormatHospital.class == getDataType() || getDataType() instanceof SearchListFormatHospital;
//    }
//
//    public boolean isInsurance() {
//        return SearchListFormatInsurance.class == getDataType() || getDataType() instanceof SearchListFormatInsurance;
//    }
//
//    public boolean isDepartment() {
//        return SearchListFormatDepartment.class == getDataType() || getDataType() instanceof SearchListFormatDepartment;
//    }
//    
//    public boolean isMdkAuditReason() {
//        return SearchListFormatMdkAuditReasons.class == getDataType() || getDataType() instanceof SearchListFormatMdkAuditReasons;
//    }
    public boolean isUserMap() {
        return isUserMap;
    }

    public boolean isReminderSubjectMap() {
        return isReminderSubjectMap;
    }

    public boolean isProcessTopicMap() {
        return isProcessTopicMap;
    }

    public boolean isMdkAuditReasonsMap() {
        return isMdkAuditReasonsMap;
    }

    public boolean isActionSubjectMap() {
        return isActionSubjectMap;
    }

    public boolean isMdkStatesMap() {
        return isMdkStatesMap;
    }

//    public boolean isDepartmentMap() {
//        return isDepartmentMap;
//    }

    /*
    public SearchListDisplayResult getDisplayFormat(final Object pValue) {
        return getDisplayFormat(pValue, null);
    }
    
    public abstract SearchListDisplayResult getDisplayFormat(final Object pItem, final SearchListDisplayFormatter pFormatter);

    protected String toStr(final Object pValue) {
        String str = (pValue == null) ? "" : pValue.toString().trim();
        if (pValue == null || str.isEmpty() || str.equalsIgnoreCase("null")) {
            return "";
        }
        return str;
    }

    protected boolean isEmpty(final Object pValue) {
        return toStr(pValue).isEmpty();
    }
     */
}
