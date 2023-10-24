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

/**
 *
 * @author niemeier
 */
public class Farbe implements Serializable {

    private static final long serialVersionUID = 1L;

    public final int farbeNr;
    public final String farbeText;

    public Farbe(final Integer pFarbeNr, final String pFarbeText) {
        farbeNr = pFarbeNr == null ? 0 : pFarbeNr;
        farbeText = pFarbeText == null ? "" : pFarbeText.trim();
    }

    public boolean hasValues() {
        return !(farbeNr == 0 && farbeText.isEmpty());
    }

    @Override
    public String toString() {
        return "Farbe{" + "farbeNr=" + farbeNr + ", farbeText=" + farbeText + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + this.farbeNr;
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
        final Farbe other = (Farbe) obj;
        if (this.farbeNr != other.farbeNr) {
            return false;
        }
        return true;
    }

}
