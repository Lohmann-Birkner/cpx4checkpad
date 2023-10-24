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
public class IdcAdgEdcMedc implements Serializable {

    private static final long serialVersionUID = 1L;

    public final String icd;
    public final int adgCode;
    public final String edcCode;
    public final String medcCode;

    public IdcAdgEdcMedc(final String pIcd, final Integer pAdgCode, final String pEdcCode, final String pMedcCode) {
        icd = pIcd == null ? "" : pIcd.trim().toUpperCase();
        adgCode = pAdgCode == null ? 0 : pAdgCode;
        edcCode = pEdcCode == null ? "" : pEdcCode.trim().toUpperCase();
        medcCode = pMedcCode == null ? "" : pMedcCode.trim().toUpperCase();
    }

    public boolean hasValues() {
        return !(icd.isEmpty() && adgCode == 0 && edcCode.isEmpty() && medcCode.isEmpty());
    }

    @Override
    public String toString() {
        return "IdcAdgEdcMedc{" + "icd=" + icd + ", adgCode=" + adgCode + ", edcCode=" + edcCode + ", medcCode=" + medcCode + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.icd);
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
        final IdcAdgEdcMedc other = (IdcAdgEdcMedc) obj;
        if (!Objects.equals(this.icd, other.icd)) {
            return false;
        }
        return true;
    }

}
