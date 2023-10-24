/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package dto.types;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author niemeier
 */
public enum Fallstatus {
    DEFAULT(0, "DEFAULT"),
    PROCESSED(1, "PROCESSED"),
    NEW(2, "NEW"),
    NEW_VERS(3, "NEW_VERS"),
    SUGG(4, "SUGG"),
    CLOSED(5, "CLOSED"),
    SAP_CLOSED(1001, "SAP_CLOSED");

    private final int number;
    private final String value;
    private static final Map<String, Fallstatus> lookup = new HashMap<>();

    static {
        for (Fallstatus value : Fallstatus.values()) {
            //lookup.put(value.getValue().trim().toLowerCase(), value);
            lookup.put(value.name().trim().toLowerCase(), value);
        }
    }

    Fallstatus(final int pNumber, final String pValue) {
        number = pNumber;
        value = (pValue == null) ? "" : pValue.trim();
    }

    public static Fallstatus findByNumber(final int pNumber) {
        for (Fallstatus type : Fallstatus.values()) {
            if (type != null && type.getNumber() == pNumber) {
                return type;
            }
        }
        return null;
    }

    public static Fallstatus findByValue(final String pValue) {
        if (pValue == null) {
            throw new IllegalArgumentException("Value is null!");
        }
        return lookup.get(pValue.trim().toLowerCase());
        //return lookup.get(pValue);
    }

    public int getNumber() {
        return number;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getValue();
    }

}
