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
public class IcdFarbeOrgan implements Serializable, Comparable<IcdFarbeOrgan> {

    private static final long serialVersionUID = 1L;

    public final String icd;
    //public int adgCode;
    //public final String edcCode;
    public final String edcText;
    //public String medcCode;
    public final int farbeNr;
    //public final String farbeText;
    public final int organNr;
    //public final String organText;
    private String icdDescription = "";

    public IcdFarbeOrgan(final String pIcd,
            final String pEdcText,
            final Integer pFarbeNr, //Color number=Severity
            final Integer pOrganNr) {
        this(pIcd, pEdcText, pFarbeNr, pOrganNr, "");
    }

    public IcdFarbeOrgan(final String pIcd,
            //final Integer pAdgCode, 
            //final String pEdcCode, 
            final String pEdcText,
            //final String pMedcCode, 
            final Integer pFarbeNr, //Color number=Severity
            //final String pFarbeText, 
            final Integer pOrganNr,
            //final String pOrganText,
            final String pIcdDescription
    ) {
        icd = pIcd == null ? "" : pIcd.trim().toUpperCase();
        //adgCode = pAdgCode == null ? 0 : pAdgCode;
        //edcCode = pEdcCode == null ? "" : pEdcCode.trim().toUpperCase();
        edcText = pEdcText == null ? "" : pEdcText.trim();
        //medcCode = pMedcCode == null ? "" : pMedcCode.trim().toUpperCase();
        farbeNr = pFarbeNr == null ? 0 : pFarbeNr;
        //farbeText = pFarbeText == null ? "" : pFarbeText.trim();
        organNr = pOrganNr == null ? 0 : pOrganNr;
        //organText = pOrganText == null ? "" : pOrganText.trim();     
        setIcdDescription(pIcdDescription);
    }

    public final void setIcdDescription(final String pIcdDescription) {
        icdDescription = pIcdDescription == null ? "" : pIcdDescription.trim();
    }

    public String getIcdDescription() {
        return icdDescription;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.icd);
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
        final IcdFarbeOrgan other = (IcdFarbeOrgan) obj;
        if (!Objects.equals(this.icd, other.icd)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(IcdFarbeOrgan o) {
        return this.icd.compareTo(o.icd);
    }

    @Override
    public String toString() {
        return "IcdFarbeOrgan{" + "icd=" + icd + ", farbeNr=" + farbeNr + ", organNr=" + organNr + '}';
    }

}
