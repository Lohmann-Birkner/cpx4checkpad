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
 *    2019  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.rulefilter.dialog;

/**
 *
 * @author nandola
 */
public enum RuleFilterApplicationUsage {

    CaseList("CL", "FallList"),
    BatchAdministration("BA", "Batchverwaltung");

    private final String id;
    private final String langKey;

    private RuleFilterApplicationUsage(final String id, final String langKey) {
        this.id = id;
        this.langKey = langKey;
    }

    public String getLangKey() {
        return langKey;
    }

    public String getId() {
        return id;
    }

}
