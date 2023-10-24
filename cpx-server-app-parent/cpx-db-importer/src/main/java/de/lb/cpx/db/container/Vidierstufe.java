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

/**
 *
 * @author niemeier
 */
public class Vidierstufe implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer minFaVid;
    private Integer fallVid;
    private Integer drgVid;
    private Integer fall;

//    public Vidierstufe(Integer minFaVid, Integer fallVid, Integer drgVid, Integer fall) {
//        this.minFaVid = minFaVid;
//        this.fallVid = fallVid;
//        this.drgVid = drgVid;
//        this.fall = fall;
//    }
    /**
     * @return the minFaVid
     */
    public Integer getMinFaVid() {
        return minFaVid;
    }

    /**
     * @return the fallVid
     */
    public Integer getFallVid() {
        return fallVid;
    }

    /**
     * @return the drgVid
     */
    public Integer getDrgVid() {
        return drgVid;
    }

    /**
     * @return the fall
     */
    public Integer getFall() {
        return fall;
    }

    /**
     * @param minFaVid the minFaVid to set
     */
    public void setMinFaVid(Integer minFaVid) {
        this.minFaVid = minFaVid;
    }

    /**
     * @param fallVid the fallVid to set
     */
    public void setFallVid(Integer fallVid) {
        this.fallVid = fallVid;
    }

    /**
     * @param drgVid the drgVid to set
     */
    public void setDrgVid(Integer drgVid) {
        this.drgVid = drgVid;
    }

    /**
     * @param fall the fall to set
     */
    public void setFall(Integer fall) {
        this.fall = fall;
    }

}
