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

import java.util.Date;

/**
 *
 * @author niemeier
 */
public class RmlLaborDocument {

    private String labdKey;    // VARCHAR 100 (mit Index)
    //public String labd_value;  //VARCHAR 100
    private Date labdUpdate;
    private Date labdDocDate;
    private RmlLabor lab;

    /**
     *
     */
    public RmlLaborDocument() {
        //super(TABLE_NAME, true);
    }

    /**
     * @return the labdKey
     */
    public String getLabdKey() {
        return labdKey;
    }

    /**
     * @param labdKey the labdKey to set
     */
    public void setLabdKey(String labdKey) {
        this.labdKey = labdKey;
    }

    /**
     * @return the labdUpdate
     */
    public Date getLabdUpdate() {
        return labdUpdate == null ? null : new Date(labdUpdate.getTime());
    }

    /**
     * @param labdUpdate the labdUpdate to set
     */
    public void setLabdUpdate(Date labdUpdate) {
        this.labdUpdate = labdUpdate == null ? null : new Date(labdUpdate.getTime());
    }

    /**
     * @return the labdDocDate
     */
    public Date getLabdDocDate() {
        return labdDocDate == null ? null : new Date(labdDocDate.getTime());
    }

    /**
     * @param labdDocDate the labdDocDate to set
     */
    public void setLabdDocDate(Date labdDocDate) {
        this.labdDocDate = labdDocDate == null ? null : new Date(labdDocDate.getTime());
    }

    /**
     * @return the lab
     */
    public RmlLabor getLab() {
        return lab;
    }

    /**
     * @param lab the lab to set
     */
    public void setLab(RmlLabor lab) {
        this.lab = lab;
    }
}
