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
package de.lb.cpx.server.commons.dao;

import java.util.Objects;

/**
 *
 * @author Husser
 */
public abstract class AbstractVersionEntity extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    public static String toNull(final String pValue) {
        String value = pValue == null ? null : pValue.trim();
        if (value != null && value.isEmpty()) {
            value = null;
        }
        return value;
    }

    public static boolean isStringEquals(final String pValue1, final String pValue2) {
        return Objects.equals(toNull(pValue1), toNull(pValue2));
    }

    public abstract boolean versionEquals(Object object);

}
