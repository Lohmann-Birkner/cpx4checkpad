/*
 * Copyright (c) 2018 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 *
 * Contributors:
 *    2018  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commons.enums;

/**
 * Flags set for handling of double rules, tables, rule types
 *
 * @author gerschmann
 */
public enum RuleOverrideFlags {
    SAVE_OLD("Regel(n) behalten", "Wenn eine Regel bereits im Pool existiert, wird die Regel behalten."), // old rule wins by collisions, 
    SAVE_NEW("Regel(n) überschreiben", "Wenn eine Regel bereits im Pool existiert, wird die Regel überschrieben."), // new rule wins by collisions
    SAVE_BOTH("Regel(n) als Kopie erzeugen", "Wenn eine Regel bereits im Pool existiert, wird eine neue Kopie erstellt."), // new rule will be saved with new ident
    CHECK_ONLY("", ""); //nothing will be saved in DB - used in web application only

    private final String displayText;
    private final String description;

    private RuleOverrideFlags(String pDisplayText, String pDescription) {
        displayText = pDisplayText;
        description = pDescription;
    }

    public String getDisplayText() {
        return displayText;
    }

    public String getDescription() {
        return description;
    }

    public static RuleOverrideFlags[] requiredValues() {
        return new RuleOverrideFlags[]{SAVE_OLD, SAVE_NEW, SAVE_BOTH};
    }
}
