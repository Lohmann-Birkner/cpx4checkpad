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
public class AdgFarbe implements Serializable {

    private static final long serialVersionUID = 1L;

    public final int farbeNr;
    public final int adg;

    public AdgFarbe(final Integer pFarbeNr, final Integer pAdg) {
        farbeNr = pFarbeNr == null ? 0 : pFarbeNr;
        adg = pAdg == null ? 0 : pAdg;
    }

    public boolean hasValues() {
        return !(farbeNr == 0 && adg == 0);
    }

    @Override
    public String toString() {
        return "AdgFarbe{" + "farbeNr=" + farbeNr + ", adg=" + adg + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + this.adg;
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
        final AdgFarbe other = (AdgFarbe) obj;
        if (this.adg != other.adg) {
            return false;
        }
        return true;
    }

}
