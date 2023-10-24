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
 *    2017  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package dto.types;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Dirk Niemeier
 */
public enum Geschlecht {
    M("W"),
    W("M"),
    U("U"),
    I("I"),
    D("D");

    public final String value;
    private static final Map<String, Geschlecht> lookup = new HashMap<>();

    static {
        for (Geschlecht value : Geschlecht.values()) {
            lookup.put(value.getValue().trim().toLowerCase(), value);
        }
    }

    public String getValue() {
        return value;
    }

    Geschlecht(final String pValue) {
        value = (pValue == null) ? "" : pValue.trim().toUpperCase();
    }

    public static Geschlecht findByValue(final String pValue) {
        if (pValue == null) {
            throw new IllegalArgumentException("Value is null!");
        }
        String value = pValue.trim().toLowerCase();
        if ("x".equalsIgnoreCase(value)) {
            //only in P21 specification 2018
            return Geschlecht.U;
        } else {
            return lookup.get(value);
        }
    }

    public static Geschlecht findByValue(final Character pValue) {
        if (pValue == null) {
            throw new IllegalArgumentException("Value is null!");
        }
        return findByValue(pValue.toString());
    }

    @Override
    public String toString() {
        return getValue();
    }
}
