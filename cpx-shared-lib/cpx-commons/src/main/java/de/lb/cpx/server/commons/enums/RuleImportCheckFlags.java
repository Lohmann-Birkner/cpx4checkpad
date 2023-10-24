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
 *    2019  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commons.enums;

/**
 *
 * @author gerschmann
 */
public enum RuleImportCheckFlags {
    CHECK_4_COLLISIONS("Prüfung auf Duplikate", "Es erfolgt eine Prüfung im Zielpool, ob eine oder mehrere Regeln in diesem vorhanden existieren.\nDieser Vorgang kann zusätzliche Zeit in Anspruch nehmen."),
    NO_CHECK_4_COLLISIONS("Keine Prüfung auf Duplikate", "Die Prüfung auf Duplikate im Zielpool entfällt.");

    private final String displayText;
    private final String description;

    private RuleImportCheckFlags(String pDisplayText, String pDescription) {
        displayText = pDisplayText;
        description = pDescription;
    }

    public String getDisplayText() {
        return displayText;
    }

    public String getDescription() {
        return description;
    }

}
