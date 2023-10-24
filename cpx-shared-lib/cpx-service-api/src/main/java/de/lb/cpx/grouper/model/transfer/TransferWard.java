/*
 * Copyright (c) 2016 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2016  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.grouper.model.transfer;

/**
 * Bei der Stationsgrouping muss erweitert werden, z.Z. nur Stationsname
 *
 * @author gerschmann
 */
public class TransferWard extends TransferHospitalUnit {

    private static final long serialVersionUID = 1L;

    private final String m_ward;

    // TODO: resultate der Stationsgrouping 
    public TransferWard(String ward) {
        m_ward = ward;
    }

    public String getIdent() {
        return m_ward;
    }
}
