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

import de.lb.cpx.db.importer.utils.Constants;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
 * @param <T> type of id
 */
public class KisAbteilungContainer<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    private T id;
    private String name = "<" + Constants.CHECKRESULT_TYP_UNKNOWN + ">"; //AppResources.getResource(AppResourceBundle.CHECKRESULT_TYP_UNKNOW)
    private String p301 = "0000";
    private int belegart = 0;
    private boolean isNalos = false;
    private int belegType = 0;
    private final List<BigDecimal> orgType = new ArrayList<>();

    /**
     * @return the id
     */
    public T getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the p301
     */
    public String getP301() {
        return p301;
    }

    /**
     * @return the belegart
     */
    public int getBelegart() {
        return belegart;
    }

    /**
     * @return the isNalos
     */
    public boolean isNalos() {
        return isNalos;
    }

    /**
     * @return the belegType
     */
    public int getBelegType() {
        return belegType;
    }

    /**
     * @param id the id to set
     */
    public void setId(T id) {
        this.id = id;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param p301 the p301 to set
     */
    public void setP301(String p301) {
        this.p301 = p301;
    }

    /**
     * @param belegart the belegart to set
     */
    public void setBelegart(int belegart) {
        this.belegart = belegart;
    }

    /**
     * @param isNalos the isNalos to set
     */
    public void setNalos(boolean isNalos) {
        this.isNalos = isNalos;
    }

    /**
     * @param belegType the belegType to set
     */
    public void setBelegType(int belegType) {
        this.belegType = belegType;
    }

    public void addOrgType(BigDecimal obj) {
        orgType.add(obj);
    }

    public boolean isGwiBeleg() {
        if (getBelegType() <= 0) {
            if (orgType.contains(new BigDecimal(34))) {
                setBelegType(2);
            } else {
                setBelegType(1);
            }
        }
        return getBelegType() == 2;
    }

    public int getGwiBelegType() {
        if (getBelegart() <= 0) {
            boolean isBeleg = orgType.contains(new BigDecimal(34));
            boolean isBelegOp = orgType.contains(new BigDecimal(45));
            boolean isBelegAna = orgType.contains(new BigDecimal(46));
            boolean isBelegHeb = orgType.contains(new BigDecimal(47));
            if (isBeleg && isBelegOp && isBelegAna && isBelegHeb) {
                setBelegart(6);
            } else if (isBeleg && isBelegOp && isBelegHeb) {
                setBelegart(5);
            } else if (isBeleg && isBelegOp && isBelegAna) {
                setBelegart(4);
            } else if (isBeleg && isBelegOp) {
                setBelegart(3);
            } else if (isBelegHeb) {
                setBelegart(2);
            } else {
                setBelegart(1);
            }
        }
        return getBelegart();
    }

}
