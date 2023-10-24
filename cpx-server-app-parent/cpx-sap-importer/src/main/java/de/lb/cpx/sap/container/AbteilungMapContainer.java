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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.sap.container;

/**
 * Mapping Klasse für den FA's nach 301
 *
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
public class AbteilungMapContainer {

    private String p301;
    private String typ;
    private String typSchluessel;

    /**
     * @return the p301
     */
    public String getP301() {
        return p301;
    }

    /**
     * @param p301 the p301 to set
     */
    public void setP301(String p301) {
        this.p301 = p301;
    }

    /**
     * @return the typ
     */
    public String getTyp() {
        return typ;
    }

    /**
     * @param typ the typ to set
     */
    public void setTyp(String typ) {
        this.typ = typ;
    }

    /**
     * @return the typSchluessel
     */
    public String getTypSchluessel() {
        return typSchluessel;
    }

    /**
     * @param typSchluessel the typSchluessel to set
     */
    public void setTypSchluessel(String typSchluessel) {
        this.typSchluessel = typSchluessel;
    }
}
