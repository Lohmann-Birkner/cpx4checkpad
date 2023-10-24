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
package de.lb.cpx.cpx.license.crypter;

import static de.lb.cpx.cpx.license.crypter.LicenseCustomerTypeEn.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author niemeier
 */
public enum LicenseCustomerEn implements Serializable {

    SanaBB("SanaBB" /*, "Sana Kliniken Berlin-Brandenburg" */, Hospital),
    ContiliaEssen("Contilia Essen" /*, "Elisabeth-Krankenhaus Essen" */, Hospital),
    LundBDev("L&B Entwicklung" /*, "Lohmann und Birkner Software Solutions GmbH" */, Other),
    LundBRePrue("L&B RePrue" /*, "Lohmann und Birkner Health Care Solutions GmbH" */, Other),
    BarmerKK("L&B HUK" /*, "Lohmann und Birkner Health Care Solutions GmbH" */, Insurance);

    private final String shortName;
    //private final String description;
    private final LicenseCustomerTypeEn type;

    LicenseCustomerEn(final String pShortName /*, final String pDescription */, final LicenseCustomerTypeEn pType) {
        shortName = pShortName;
//        description = pDescription;
        type = pType == null ? LicenseCustomerTypeEn.Other : pType;
    }

//    /**
//     * @return the description
//     */
//    public String getDescription() {
//        return description;
//    }
    /**
     * @return the shortName
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * @return the type
     */
    public LicenseCustomerTypeEn getType() {
        return type;
    }

    @Override
    public String toString() {
        return getShortName();
    }

    public static Set<LicenseCustomerEn> getSortedCustomers() {
        Set<LicenseCustomerEn> set = new TreeSet<>(new Comparator<LicenseCustomerEn>() {
            @Override
            public int compare(LicenseCustomerEn o1, LicenseCustomerEn o2) {
                return o1.shortName.compareToIgnoreCase(o2.shortName);
            }
        });
        set.addAll(Arrays.asList(LicenseCustomerEn.values()));
        return set;
    }

}
