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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.math.utility;

import de.lb.cpx.model.TCaseDetails;

/**
 * Math helper class to reuse all math operations used for ca case version
 *
 * @author wilde
 */
public class CaseVersionHelper {

    private CaseVersionHelper() {
        //utility class needs no public constructor
    }

    /**
     * add value to leave in version
     *
     * @param pDetails case version
     * @param pValue value to add
     */
    public static void addToLeave(TCaseDetails pDetails, Integer pValue) {
        pDetails.setCsdLeave(pDetails.getCsdLeave() + pValue);
    }

    /**
     * remove value to leave in version, if leave is 0 nothing will happen
     *
     * @param pDetails case version
     * @param pValue value to add
     */
    public static void removeToLeave(TCaseDetails pDetails, Integer pValue) {
        if (pDetails.getCsdLeave() < 0) {
            return;
        }
        pDetails.setCsdLeave(pDetails.getCsdLeave() - pValue);
    }

}
