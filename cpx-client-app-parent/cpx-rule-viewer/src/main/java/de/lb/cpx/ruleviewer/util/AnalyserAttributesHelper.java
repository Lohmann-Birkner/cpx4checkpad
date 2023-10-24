/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.util;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author wilde
 */
public class AnalyserAttributesHelper {

    public static final String getWriteMethodeForKey(String pKey) {
        pKey = pKey.trim();
        pKey = StringUtils.capitalize(pKey);
        return new StringBuilder().append("set").append(pKey).toString();
    }

    public static final String getReadMethodeForKey(String pKey) {
        pKey = pKey.trim();
        pKey = StringUtils.capitalize(pKey);
        return new StringBuilder().append("get").append(pKey).toString();
    }
}
