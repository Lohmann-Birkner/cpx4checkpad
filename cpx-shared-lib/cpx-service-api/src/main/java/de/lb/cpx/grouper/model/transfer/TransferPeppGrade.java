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
 *    2016  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.grouper.model.transfer;

import java.io.Serializable;
import java.util.Date;

/**
 * Transer class for one calculated PayGrade/PayClass value
 *
 * @author gerschmann
 */
public class TransferPeppGrade implements Serializable {

    private static final long serialVersionUID = 1L;

    private int m_grade; // grade/class number
    private double m_cw; // calculated cw for this grade/class
    private double m_baserate; // baserate for this grade/class
    private int m_duration; // days to this grade/class and baserate
    private Date m_from; // first day for this this grade/class and baserate
    private Date m_to; // last day for this this grade/class and baserate

    public void setGrade(int grade) {
        m_grade = grade;
    }

    public void setCw(double cw) {
        m_cw = cw;
    }

    public void setBaserate(double br) {
        m_baserate = br;
    }

    public void setDuration(int len) {
        m_duration = len;
    }

    public void setBaserateFrom(Date date) {
        m_from = date == null ? null : new Date(date.getTime());
    }

    public void setBaserateTo(Date date) {
        m_to = date == null ? null : new Date(date.getTime());
    }

    public double getCw() {
        return m_cw;
    }

    public double getBaserate() {
        return m_baserate;
    }

    public int getDuration() {
        return m_duration;
    }

    public Date getFrom() {
        return m_from == null ? null : new Date(m_from.getTime());
    }

    public Date getTo() {
        return m_to == null ? null : new Date(m_to.getTime());
    }

    public int getGrade() {
        return m_grade;
    }

}
