/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.dto.acg;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author niemeier
 */
public class EdcOrgan implements Serializable {

    private static final long serialVersionUID = 1L;

    public final int organNr;
    public final String edc;
    public final String edcText;

    public EdcOrgan(final Integer pOrganNr, final String pEdc, final String pEdcText) {
        organNr = pOrganNr == null ? 0 : pOrganNr;
        edc = pEdc == null ? "" : pEdc.trim().toUpperCase();
        edcText = pEdcText == null ? "" : pEdcText.trim();
    }

    public boolean hasValues() {
        return !(organNr == 0 && edc.isEmpty() && edcText.isEmpty());
    }

    @Override
    public String toString() {
        return "EdcOrgan{" + "organNr=" + organNr + ", edc=" + edc + ", edcText=" + edcText + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.edc);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EdcOrgan other = (EdcOrgan) obj;
        if (!Objects.equals(this.edc, other.edc)) {
            return false;
        }
        return true;
    }

}
