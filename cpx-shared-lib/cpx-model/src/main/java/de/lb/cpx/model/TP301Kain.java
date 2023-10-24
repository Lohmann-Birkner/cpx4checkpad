/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2017  Shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Shahin
 */
@Entity
@Table(name = "T_P301_KAIN")
@DiscriminatorValue(value = "KAIN")
public class TP301Kain extends TP301KainInka {

    private static final long serialVersionUID = 1L;

    private Date receivingDate;//RECEIVING_DATE

    /**
     *
     * @return receivingDate
     */
    @Column(name = "RECEIVING_DATE", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getReceivingDate() {
        return receivingDate == null ? null : new Date(receivingDate.getTime());
    }

    /**
     *
     * @param receivingDate :Datum und Zeit des Speicherns/Empfanges in SAP
     */
    public void setReceivingDate(Date receivingDate) {
        this.receivingDate = receivingDate == null ? null : new Date(receivingDate.getTime());
    }

}
