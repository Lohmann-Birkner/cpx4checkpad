/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package dto.impl;

/**
 *
 * @author Dirk Niemeier
 */
public class Hauptdiagnose extends Diagnose<Hauptdiagnose> {

    private static final long serialVersionUID = 1L;

    public Hauptdiagnose(final Department pDepartment) {
        this(pDepartment, null);
    }

    public Hauptdiagnose(final Department pDepartment, final Ward pWard) {
        super(pDepartment, pWard, true);
    }

    public Hauptdiagnose(final String pIkz, final String pFallNr, final Long pDepartmentNr) {
        this(pIkz, pFallNr, pDepartmentNr, null);
    }

    public Hauptdiagnose(final String pIkz, final String pFallNr, final Long pDepartmentNr, final Long pWardNr) {
        super(pIkz, pFallNr, pDepartmentNr, true);
    }

    @Override
    public void set(Hauptdiagnose pOther) {
        super.set(pOther);
    }

}
