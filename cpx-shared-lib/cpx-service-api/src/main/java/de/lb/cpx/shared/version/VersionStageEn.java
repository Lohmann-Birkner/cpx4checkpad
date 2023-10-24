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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.version;

import java.io.Serializable;

/**
 *
 * @author niemeier
 */
public enum VersionStageEn implements Serializable {

//    PREALPHA("prea", "Pre-Alpha", 0),
    ALPHA("a", "Alpha", 1),
    BETA("b", "Beta", 2),
    RC("rc", "Release Candidate", 3),
    FINAL("", "Final Release", 4);

    public final String suffix;
    public final String description;
    public final int maturityLevel;

    VersionStageEn(final String pSuffix, final String pDescription, final int pMaturityLevel) {
        suffix = pSuffix;
        description = pDescription;
        maturityLevel = pMaturityLevel;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getDescription() {
        return description;
    }

    public int getMaturityLevel() {
        return maturityLevel;
    }

}
