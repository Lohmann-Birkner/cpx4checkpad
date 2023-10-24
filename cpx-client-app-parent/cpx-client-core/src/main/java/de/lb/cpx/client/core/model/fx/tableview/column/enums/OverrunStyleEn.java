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
 *    2019  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.tableview.column.enums;

/**
 *
 * @author nandola
 */
public enum OverrunStyleEn {

    None("k", "Keine"),
    Button("B", "Button"),
    Tooltip("T", "Tooltip");

    private final String id;
    private final String langKey;

    private OverrunStyleEn(final String id, final String langKey) {
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
