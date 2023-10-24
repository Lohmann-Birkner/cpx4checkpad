/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model.enums;

/**
 *
 * @author gerschmann
 */
public enum Rmc2CpxReadmRulesFieldsEn {

    setMrgCondition1("eins"),
    setMrgCondition2("zwei"),
    setMrgCondition3("drei"),
    setMrgCondition4("vier"),
    setMrgCondition5("fuenf"),
    setMrgCondition6("sechs"),
    setMrgCondition7("sieben"),
    setMrgCondition8("acht"),
    setMrgCondition9("neun"),
    setMrgCondition10("zehn");

    private final String rmcName;

    private Rmc2CpxReadmRulesFieldsEn(String str) {
        rmcName = str;
    }

    public String getValue() {
        return rmcName;
    }

}
