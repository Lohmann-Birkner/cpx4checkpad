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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.db.container;

import java.io.Serializable;

/**
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
 * @author unbekannt
 * @version 2.0
 */
public class KisPatientContainer implements Serializable {

    private final String patNr;
    private final String versNr;
    private final String deb;

    public KisPatientContainer(String pPatNr, String pVersNr, String pDeb) {
        this.patNr = pPatNr;
        this.versNr = pVersNr;
        this.deb = pDeb;
    }

    /**
     * @return the patNr
     */
    public String getPatNr() {
        return patNr;
    }

    /**
     * @return the versNr
     */
    public String getVersNr() {
        return versNr;
    }

    /**
     * @return the deb
     */
    public String getDeb() {
        return deb;
    }

}
