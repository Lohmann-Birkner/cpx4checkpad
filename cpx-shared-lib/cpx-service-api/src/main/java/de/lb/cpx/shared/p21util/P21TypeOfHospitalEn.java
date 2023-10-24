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
package de.lb.cpx.shared.p21util;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public enum P21TypeOfHospitalEn implements Serializable {

    B("Bundeswehrkrankenhaus"),
    H("Hochschulklinik/Universitätsklinikum"),
    P("Plankrankenhaus"),
    U("Krankenhaus in Trägerschaft der gesetzlichen Unfallversicherung"),
    V("Krankenhaus mit einem Versorgungsvertrag (nach § 18 Nr. 3 SGB V)");

    private static final Logger LOG = Logger.getLogger(P21TypeOfHospitalEn.class.getName());

    private final String name;

    P21TypeOfHospitalEn(final String pName) {
        name = pName;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name() + " - " + name;
    }

    public static P21TypeOfHospitalEn find(final String pName) {
        if (pName == null) {
            return null;
        }
        for (P21TypeOfHospitalEn item : values()) {
            if (pName.trim().equalsIgnoreCase(item.name())) {
                return item;
            }
        }
        LOG.log(Level.WARNING, "This is not a valid P21 Hospital Type: " + pName);
        return null;
    }

}
