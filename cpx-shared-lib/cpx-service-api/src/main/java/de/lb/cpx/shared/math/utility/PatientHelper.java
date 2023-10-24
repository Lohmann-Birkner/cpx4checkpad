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

import de.lb.cpx.shared.lang.Lang;
import java.time.LocalDate;
import java.util.Date;

/**
 * Helper class for all Patient related computation
 *
 * @author wilde
 */
public class PatientHelper {

    private PatientHelper() {
        //utility class needs no public constructor
    }

    /**
     * @param pDateOfBirth Date of birth of the patient
     * @return Year value of the age
     */
    public static Integer getCurrentAgeInYears(Date pDateOfBirth) {
        return getCurrentAge(pDateOfBirth).getYear();
    }

    /**
     * @param pDateOfBirth Date of birth of the patient
     * @return Localdate containing the age
     */
    public static LocalDate getCurrentAge(Date pDateOfBirth) {
        return Lang.getElapsedTimeToNow(pDateOfBirth);
    }

    /**
     * @param pDateOfBirth date of birth of the patient
     * @return localizied string with age in years or days, depends of year part
     * is greater than 0
     */
    public static String getCurrentAgeLocalizedInYearsOrDays(Date pDateOfBirth) {
        return getLocalizedAge(getCurrentAge(pDateOfBirth));
    }

    /**
     * @param pAge age of the patient as local date
     * @return localizied string with age in years or days, depends of year part
     * is greater than 0
     */
    public static String getLocalizedAge(LocalDate pAge) {
        if (pAge.getYear() > 0) {
            return pAge.getYear() + " " + Lang.getAgeYears();
        } else {
            return pAge.getDayOfYear() + " " + Lang.getAgeDays();
        }
    }
}
