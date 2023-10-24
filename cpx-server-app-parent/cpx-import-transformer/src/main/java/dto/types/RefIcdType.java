/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  niemeier - initial API and implementation and/or initial documentation
 */
package dto.types;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author niemeier
 */
public enum RefIcdType {
    Plus(1, '+'), //1 = +
    Star(2, '*'), //2 = *
    Mark(3, '!'), //3 = !
    ToMark(4, ' '); 

    public final int number;
    public final char value;
    private static final Map<String, RefIcdType> lookup = new HashMap<>();

    static {
        for (RefIcdType value : RefIcdType.values()) {
            //lookup.put(value.getValue().trim().toLowerCase(), value);
            lookup.put(value.name().trim().toLowerCase(), value);
        }
    }

    public char getValue() {
        return value;
    }

    public int getNumber() {
        return number;
    }

    RefIcdType(final int pNumber, final char pValue) {
        number = pNumber;
        value = pValue;
    }

    public static RefIcdType findByNumber(final int pNumber) {
        for (RefIcdType type : RefIcdType.values()) {
            if (type != null && type.getNumber() == pNumber) {
                return type;
            }
        }
        return null;
    }

    public static RefIcdType findByValue(final String pValue) {
        if (pValue == null) {
            throw new IllegalArgumentException("Value is null!");
        }
        return lookup.get(pValue.trim().toLowerCase());
        //return lookup.get(pValue);
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }

}
