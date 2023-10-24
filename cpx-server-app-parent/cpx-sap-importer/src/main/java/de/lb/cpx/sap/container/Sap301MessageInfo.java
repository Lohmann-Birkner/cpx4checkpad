/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  urbach - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.sap.container;

import java.util.Date;

/**
 * Container für Nachrichtenummer und SAP-Importdatum von KAIN-Nachrichten
 * <p>
 * Überschrift: Checkpoint DRG</p>
 *
 * <p>
 * Beschreibung: Fallmanagement DRG</p>
 *
 * <p>
 * Copyright: </p>
 *
 * <p>
 * Organisation: </p>
 *
 * @author urbach
 * @version 2.0
 */
public class Sap301MessageInfo {

    private final String extRefNumber;
    private final Date kainImportDate;

    public Sap301MessageInfo(String pExtRefNumber, Date pKainImportDate) {
        extRefNumber = pExtRefNumber;
        kainImportDate = pKainImportDate;
    }

    public String getExtRefNumber() {
        return extRefNumber;
    }

    public Date getKainImportDate() {
        return kainImportDate;
    }
}
