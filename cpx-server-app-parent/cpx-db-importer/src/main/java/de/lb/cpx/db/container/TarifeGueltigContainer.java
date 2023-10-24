/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.db.container;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * �berschrift: Checkpoint DRG</p>
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
public class TarifeGueltigContainer implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date von = null;
    private Date bis = null;
    private String p301 = "0000";
    private boolean beleg = false;
    private boolean isDrg = false;

    /**
     * @return the von
     */
    public Date getVon() {
        return von == null ? null : new Date(von.getTime());
    }

    /**
     * @return the bis
     */
    public Date getBis() {
        return bis == null ? null : new Date(bis.getTime());
    }

    /**
     * @return the p301
     */
    public String getP301() {
        return p301;
    }

    /**
     * @return the beleg
     */
    public boolean isBeleg() {
        return beleg;
    }

    /**
     * @return the isDrg
     */
    public boolean isIsDrg() {
        return isDrg;
    }

    /**
     * @param pVon the von to set
     */
    public void setVon(Date pVon) {
        this.von = pVon == null ? null : new Date(pVon.getTime());
    }

    /**
     * @param pBis the bis to set
     */
    public void setBis(Date pBis) {
        this.bis = pBis == null ? null : new Date(pBis.getTime());
    }

    /**
     * @param p301 the p301 to set
     */
    public void setP301(String p301) {
        this.p301 = p301;
    }

    /**
     * @param beleg the beleg to set
     */
    public void setBeleg(boolean beleg) {
        this.beleg = beleg;
    }

    /**
     * @param isDrg the isDrg to set
     */
    public void setIsDrg(boolean isDrg) {
        this.isDrg = isDrg;
    }

}
