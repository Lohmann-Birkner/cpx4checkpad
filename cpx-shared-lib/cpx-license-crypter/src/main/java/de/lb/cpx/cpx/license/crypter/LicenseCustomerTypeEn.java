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

import java.io.Serializable;

/**
 *
 * @author niemeier
 */
public enum LicenseCustomerTypeEn implements Serializable {

    Hospital("Krankenhaus"),
    Insurance("Versicherung"),
    Bege("Berufsgenossenschaft"),
    Other("Anderer");

    private final String shortName;

    public String getShortName() {
        return shortName;
    }

    LicenseCustomerTypeEn(final String pShortName) {
        shortName = pShortName;
    }

    @Override
    public String toString() {
        return shortName;
    }

}
