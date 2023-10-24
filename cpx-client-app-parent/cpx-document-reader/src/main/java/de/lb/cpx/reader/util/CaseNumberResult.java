/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.reader.util;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author niemeier
 */
public class CaseNumberResult implements Comparable<CaseNumberResult> {

    public static final String FALL_NR_DES_KRANKENHAUSES = "fall-nr. des krankenhauses";
    public static final String FALL_NR = "fall-nr";
    public static final String FALLNUMMER_DES_KRANKENHAUSES = "fallnummer des krankenhauses";
    public static final String FALLNUMMER = "fallnummer";
    public static final String AUFN_NR = "aufn.-nr";
    public static final String AUFNAHME_NR = "aufnahme-nr";
    public static final String AUFNAHMENR = "aufnahmenr";
    public static final String AUFNAHMENUMMER = "aufnahmenummer";

    protected static final Map<String, String> KEYS;

    static {
        KEYS = new LinkedHashMap<>();
        KEYS.put(FALL_NR_DES_KRANKENHAUSES, "Fall-Nr. des Krankenhauses");
        KEYS.put(FALL_NR, "Fall-Nr.");
        KEYS.put(FALLNUMMER_DES_KRANKENHAUSES, "Fallnummer des Krankenhauses");
        KEYS.put(FALLNUMMER, "Fallnummer");
        KEYS.put(AUFN_NR, "Aufn.-Nr.");
        KEYS.put(AUFNAHME_NR, "Aufnahme-Nr.");
        KEYS.put(AUFNAHMENR, "Aufnahmenr.");
        KEYS.put(AUFNAHMENUMMER, "Aufnahmenummer");
    }

    public static Map<String, String> getKeys() {
        return new LinkedHashMap<>(KEYS);
    }

    public final String key;
    public final String name;
    public final String[] value;

    public CaseNumberResult(final String pKey, final String pName, final String[] pValues) {
        this.key = StringUtils.trimToEmpty(pKey);
        this.name = StringUtils.trimToEmpty(pName);
        if (pValues == null || pValues.length == 0) {
            this.value = new String[0];
        } else {
            String[] tmp = new String[pValues.length];
            System.arraycopy(pValues, 0, tmp, 0, pValues.length);
            this.value = tmp;
        }
    }

    @Override
    public int compareTo(CaseNumberResult o) {
        return this.key.compareTo(o.key);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.key);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CaseNumberResult other = (CaseNumberResult) obj;
        if (!Objects.equals(this.key, other.key)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " (" + key + "): " + Arrays.toString(value);
    }

}
