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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.ruleeditor.enums;

/**
 * Enum to store Pooltypes
 *
 * @author wilde
 */
public enum PoolType {
    WORKING("Arbeitspool", "Pools an dennen gearbeitet wird"), PRODUCTIVE("Produktionspools", "Pools die produktiv genutzt werden");

    private final String title;
    private final String description;

    private PoolType(String pTitle, String pDescription) {
        title = pTitle;
        description = pDescription;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

}
