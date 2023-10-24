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
package de.lb.cpx.sap.dto;

/**
 *
 * @author niemeier
 */
public class SapCaseDrg {

    private String fallnr;
    private String drg;
    private String cw;

    /**
     *
     * @param pFallnr case number
     */
    public SapCaseDrg(final String pFallnr) {
        fallnr = pFallnr;
    }

    /**
     * @return the fallnr
     */
    public String getFallnr() {
        return fallnr;
    }

    /**
     * @param fallnr the fallnr to set
     */
    public void setFallnr(String fallnr) {
        this.fallnr = fallnr;
    }

    /**
     * @return the drg
     */
    public String getDrg() {
        return drg;
    }

    /**
     * @param drg the drg to set
     */
    public void setDrg(String drg) {
        this.drg = drg;
    }

    /**
     * @return the cw
     */
    public String getCw() {
        return cw;
    }

    /**
     * @param cw the cw to set
     */
    public void setCw(String cw) {
        this.cw = cw;
    }
}
