/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.lang;

import de.lb.cpx.model.lang.CpxLanguageInterface;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Dirk Niemeier
 */
public class Translation implements Serializable, Comparable<Translation> {

    private static final long serialVersionUID = 1L;
    //final public static Function<Translation, String> toString = (Translation trans) -> trans.toString();

    public final String value;
    public final boolean hasValue;
    public final String abbreviation;
    public final boolean hasAbbreviation;
    public final String description;
    public final boolean hasDescription;
    public final String tooltip;
    public final boolean hasTooltip;

    public Translation(final String pValue) {
        //String val = (pValue == null)?"":pValue.trim();
        String[] arr = CpxLanguageInterface.splitValue(pValue);
        this.value = arr[0].trim();
        String tmp = "";
        if (arr.length > 1) {
            tmp = arr[1].trim();
            if (isEmpty(tmp)) {
                tmp = "";
            }
            this.abbreviation = tmp;
        } else {
            this.abbreviation = "";
        }
        if (arr.length > 2) {
            tmp = arr[2].trim();
            if (isEmpty(tmp)) {
                tmp = "";
            }
            this.description = tmp;
        } else {
            this.description = "";
        }
        this.tooltip = buildTooltip(this.value, this.abbreviation, this.description);

        hasValue = (value != null && !value.isEmpty());
        hasAbbreviation = (abbreviation != null && !abbreviation.isEmpty());
        hasDescription = (description != null && !description.isEmpty());
        hasTooltip = (tooltip != null && !tooltip.isEmpty());
    }

    public static boolean isEmpty(final String pValue) {
        if (pValue == null) {
            return true;
        }
        String value = pValue.trim();
        if (value.isEmpty()) {
            return true;
        }
        if (value.equalsIgnoreCase("NONE")) {
            return true;
        }
        if (value.equalsIgnoreCase("EMPTY")) {
            return true;
        }
        return false;
    }

    public static String buildTooltip(final String pValue, final String pAbbreviation, final String pDescription) {
        String tooltip = "";
        if (!pAbbreviation.isEmpty()) {
            if (!pDescription.isEmpty()) {
                tooltip = pDescription;
            } else {
                tooltip = pValue;
            }
        } else {
            tooltip = pDescription;
        }
        return tooltip;
    }

    public boolean hasValue() {
        return hasValue;
    }

    public boolean hasAbbreviation() {
        return hasAbbreviation;
    }

    public boolean hasDescription() {
        return hasDescription;
    }

    public boolean hasTooltip() {
        return hasTooltip;
    }

    public String getValue() {
        return value;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getDescription() {
        return description;
    }

    public String getTooltip() {
        return tooltip;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int compareTo(Translation o) {
        if (o == null) {
            return -1;
        }
        return this.toString().compareTo(o.toString());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.value);
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
        final Translation other = (Translation) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }

}
