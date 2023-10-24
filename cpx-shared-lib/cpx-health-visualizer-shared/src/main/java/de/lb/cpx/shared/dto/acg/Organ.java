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
public class Organ implements Serializable {

    private static final long serialVersionUID = 1L;

    public final int organNr;
    public final String organText;

    public Organ(final Integer pOrganNr, final String pOrganText) {
        organNr = pOrganNr == null ? 0 : pOrganNr;
        organText = pOrganText == null ? "" : pOrganText.trim();
    }

    public boolean hasValues() {
        return !(organNr == 0 && organText.isEmpty());
    }

    @Override
    public String toString() {
        return "Organ{" + "organNr=" + organNr + ", organText=" + organText + '}';
    }

}
