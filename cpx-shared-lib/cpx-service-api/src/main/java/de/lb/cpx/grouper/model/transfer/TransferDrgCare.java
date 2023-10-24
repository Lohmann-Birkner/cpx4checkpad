/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.grouper.model.transfer;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author gerschmann
 */
public class TransferDrgCare implements Serializable {

    private static final long serialVersionUID = 1L;
    private int sortInd;
    private int care_days;
    private double care_cw_day;
    private double baserate;
    private Date startBaserate;
    private Date endBaserate;
    
    public TransferDrgCare(int pSortind, int pCareDays, double pCareCwDay, double pBaserate, Date pStartDate, Date pEndDate){
        sortInd = pSortind;
        care_days = pCareDays;
        care_cw_day = pCareCwDay;
        baserate = pBaserate;
        startBaserate = pStartDate;
        endBaserate = pEndDate;

    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getSortInd() {
        return sortInd;
    }

    public int getCare_days() {
        return care_days;
    }

    public double getCare_cw_day() {
        return care_cw_day;
    }

    public double getBaserate() {
        return baserate;
    }

    public Date getStartBaserate() {
        return startBaserate;
    }

    public Date getEndBaserate() {
        return endBaserate;
    }
    
    
}
