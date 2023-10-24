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
package de.lb.cpx.model.enums;

import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author Dirk Niemeier
 * @param <T> enum class
 */
public interface CpxEnumInterface<T> extends Serializable {

    String getLangKey();

    //public String toString();
    //public String toString(final CpxLanguageInterface cpxLanguage);
    Translation getTranslation();

    Translation getTranslation(Object... pParams);

    public static String toStaticString(final String pId, final String pLangKey) {
        String id = (pId == null) ? "" : pId.trim();
        if (pLangKey == null || pLangKey.trim().isEmpty()) {
            return "";
        }
        Translation trans = Lang.get(pLangKey);
        final String val = trans.value.equalsIgnoreCase(id) ? "" : trans.value;
        final String abbr = trans.abbreviation.equalsIgnoreCase(id) || trans.abbreviation.equalsIgnoreCase(val) ? "" : trans.abbreviation;
        StringBuilder sb = new StringBuilder(id);
        if (!val.isEmpty()) {
            if (sb.length() > 0) {
                sb.append(" - ");
            }
            sb.append(val);
        }
        if (!abbr.isEmpty()) {
            sb.append(" (" + abbr + ")");
        }
        //return id + " - " + trans.value + (trans.hasAbbreviation() ? " (" + trans.getAbbreviation() + ")" : "");
        return sb.toString();
    }

    /**
     * This value is displayed in CPX Client (search lists)
     *
     * @return id that is shown to the customer/user
     */
    String getViewId();

    /**
     * is this entry relevant (means selectable in CheckComboBox e.g.) in view?
     *
     * @return is relevant for client?
     */
    boolean isViewRelevant();

    /**
     * id represented as a string
     *
     * @return id
     */
    String getIdStr();

    /**
     * id as an integer (may throw an UnsupportedOperationException if original
     * id cannot be represented or parsed as an int) Can be also useful to avoid
     * boxing/unboxing of integers in comparison to getId() method
     *
     * @return id
     */
    int getIdInt();

    /**
     * id as native data type
     *
     * @return id
     */
    T getId();

//    public abstract CpxEnumInterface getEnum(String value);
    public static CpxEnumInterface<?> findEnum(CpxEnumInterface<?>[] values, String pValue) throws IllegalArgumentException {
        String value = (pValue == null) ? "" : pValue.trim();
        if (value.equalsIgnoreCase("\"\"")) {
            return null;
        }
        if (value.isEmpty()) {
            return null;
        }
        //for GrouperMdcOrSk
        if (value.equalsIgnoreCase("PRÄ")) {
            value = "PRE";
        }
        for (CpxEnumInterface<?> enumeration : Arrays.asList(values)) {
            if (enumeration.getViewId().equalsIgnoreCase(value)) {
                return enumeration;
            }
        }
        throw new IllegalArgumentException("Enum for this value seems to be missing: " + value);
    }

}
