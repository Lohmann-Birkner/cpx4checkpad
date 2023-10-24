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
public enum Lokalisation {
    LINKS("L"),
    RECHTS("R"),
    BEIDE("B"),
    KEINE("E");

    public final String value;
    private static final Map<String, Lokalisation> lookup = new HashMap<>();

    static {
        for (Lokalisation value : Lokalisation.values()) {
            lookup.put(value.getValue().trim().toLowerCase(), value);
        }
    }

    public String getValue() {
        return value;
    }

    Lokalisation(final String pValue) {
        value = (pValue == null) ? "" : pValue.trim().toUpperCase();
    }

    public static Lokalisation findByValue(final String pValue) {
        if (pValue == null) {
            throw new IllegalArgumentException("Value is null!");
        }
        Lokalisation lok = lookup.get(pValue.trim().toLowerCase());
        if (lok == null) {
            return KEINE;
        }
        return lok;
        /*
    for(Lokalisation lokalisation: values()) {
      if (lokalisation.getValue().equalsIgnoreCase(pValue)) {
        return lokalisation;
      }
    }
    return null;
         */
    }

    @Override
    public String toString() {
        return getValue();
    }
}
