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
 *    2019  niemeier - DTO which is transferred to the client to show how much objects are grouped.
 */
package de.lb.cpx.shared.dto.broadcast;

import java.io.Serializable;

/**
 *
 * @author niemeier
 */
public enum BroadcastOriginEn implements Serializable {

    BATCHGROUPING("Batchgrouping"),
    IMPORT("Import"),
    MERGING("Fallzusammenführung"),
    CASE_TO_COMMON("Fall in CommonDB kopieren"),
    CORE_DATA("Stammdaten update"),
    DELETE_CASE("Fall gelöscht"),
    DELETE_PROCESS("Vorgang gelöscht"),
    ADD_CASE("Fall hinzugefügt"),
    ADD_PROCESS("Vorgang hinzugefügt");

    private final String name;

    BroadcastOriginEn(final String pName) {
        name = pName;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}
