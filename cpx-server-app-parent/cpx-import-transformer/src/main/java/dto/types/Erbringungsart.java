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
 *    2017  niemeier - initial API and implementation and/or initial documentation
 */
package dto.types;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 */
public enum Erbringungsart {

    HA(1, "HA"), //1 = HA
    HaBh(2, "HaBh"), //2 = HA/B-Heb.
    Bo(3, "Bo"), //3 = B-Op.
    BoBa(4, "BoBa"), //4 = B-Op./B-Anäst.
    BoBh(5, "BoBh"), //5 = B-Op./B-Heb.
    BoBaBh(6, "BoBaBh"), //6 = B-Op./B-Anäst./B-Heb.
    //  TeStVe(7, Lang.ADMISSION_MODE_DAY_CARE), //Teilstationaere Versorgung - not used in cp and therefore ignored mabe added later?
    HaBha(7, "HaBha"),//7 = HA/B-Hon.Arzt
    NR(8, "NR"); //  8 =NotRelevant

    private static final Logger LOG = Logger.getLogger(Erbringungsart.class.getName());

    public final int number;
    public final String value;
    private static final Map<String, Erbringungsart> lookup = new HashMap<>();

    static {
        for (Erbringungsart value : Erbringungsart.values()) {
            //lookup.put(value.getValue().trim().toLowerCase(), value);
            lookup.put(value.name().trim().toLowerCase(), value);
        }
    }

    public String getValue() {
        return value;
    }

    public int getNumber() {
        return number;
    }

    Erbringungsart(final int pNumber, final String pValue) {
        number = pNumber;
        value = (pValue == null) ? "" : pValue.trim();
    }
    
    public static Erbringungsart findByNumber(final String pNumber) {

        int numberOfAdmissionMode = 1;
        if (pNumber != null) {
            try {
                numberOfAdmissionMode = Integer.parseInt(pNumber);
            } catch (NumberFormatException ex) {
                LOG.log(Level.WARNING, "was not able to convert Erbringungsart for value {0}", pNumber);
            }

            for (Erbringungsart val: Erbringungsart.values()) {
                if (val.getNumber() == numberOfAdmissionMode) {
                    return val;
                }
            }
        }
        return null;
    }

    public static Erbringungsart findByValue(final String pValue) {
        if (pValue == null) {
            throw new IllegalArgumentException("Value is null!");
        }
        String value = pValue.trim().toLowerCase();
        if (value.isEmpty()) {
            return null;
        }
        int i = 0;
        while (!value.isEmpty()) {
            Erbringungsart art = lookup.get(value);
            if (art != null) {
                if (i > 0) {
                    LOG.log(Level.WARNING, "detected Erbringungsart {0} through auto-sensing: {1} (this is maybe incorrect!)", new Object[]{pValue, art.getValue()});
                }
                return art;
            }
            value = value.substring(0, value.length() - 1);
            i++;
        }
        LOG.log(Level.WARNING, "was not able to detect Erbringungsart for value {0}", pValue);
        return null;
        //return lookup.get(pValue);
    }

    @Override
    public String toString() {
        return getValue();
    }

}
