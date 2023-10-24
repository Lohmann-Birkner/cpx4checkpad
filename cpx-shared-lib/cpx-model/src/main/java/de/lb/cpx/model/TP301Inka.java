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
import javax.persistence.TemporalType;

/**
 *
 * @author Shahin
 */
@Entity
@Table(name = "T_P301_INKA")
@DiscriminatorValue(value = "INKA")
public class TP301Inka extends TP301KainInka {

    private static final long serialVersionUID = 1L;

    private Long currentNrSending;//CURRENT_NR_SENDING
    private boolean ready4sendingFl = false;//READY_4_SENDING_FL
    private boolean isSendedFl = false;//IS_SENDED_FL
    private boolean isCancelledFl = false;//IS_CANCELLED_Fl
    private Date sendingDate;//SENDING_DATE

    /**
     *
     * @return currentNrSending
     */
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "curr_no_for_sending_inka_gen")
//    @SequenceGenerator(name = "curr_no_for_sending_inka_gen", sequenceName = "CURRENT_NO_4_SENDING_INKA_SQ")
    @Column(name = "CURRENT_NR_SENDING", nullable = true)
    public Long getCurrentNrSending() {
        return currentNrSending;
    }

    /**
     *
     * @param currentNrSending :laufende Nummer für das Versenden (interne
     * Daten)
     */
    public void setCurrentNrSending(Long currentNrSending) {
        this.currentNrSending = currentNrSending;
    }

    /**
     *
     * @return ready4sendingFl
     */
    @Column(name = "READY_4_SENDING_FL", nullable = false)
    public boolean getReady4sendingFl() {
        return ready4sendingFl;
    }

    /**
     *
     * @param ready4sendingFl :TRUE = Nachricht in CPX gespeichert und bereit
     * zum Übergabe an das KIS
     */
    public void setReady4sendingFl(boolean ready4sendingFl) {
        this.ready4sendingFl = ready4sendingFl;
    }

    /**
     *
     * @return isSendedFl
     */
    @Column(name = "IS_SENDED_FL", nullable = false)
    public boolean getIsSendedFl() {
        return isSendedFl;
    }

    /**
     *
     * @param isSendedFl :Nachricht wurde an das KIS übergeben
     */
    public void setIsSendedFl(boolean isSendedFl) {
        this.isSendedFl = isSendedFl;
    }

    /**
     *
     * @return isCancelledFl
     */
    @Column(name = "IS_CANCELLED_FL", nullable = false)
    public boolean getIsCancelledFl() {
        return isCancelledFl;
    }

    /**
     *
     * @param isCancelledFl :Nachricht wurde an das KIS storniert.
     */
    public void setIsCancelledFl(boolean isCancelledFl) {
        this.isCancelledFl = isCancelledFl;
    }

    /**
     *
     * @return sendingDate
     */
    @Column(name = "SENDING_DATE")
    @Temporal(TemporalType.DATE)
    public Date getSendingDate() {
        return sendingDate == null ? null : new Date(sendingDate.getTime());
    }

    /**
     *
     * @param sendingDate :Datum und Zeit der Übergabe an das KIS
     */
    public void setSendingDate(Date sendingDate) {
        this.sendingDate = sendingDate == null ? null : new Date(sendingDate.getTime());
    }

}
