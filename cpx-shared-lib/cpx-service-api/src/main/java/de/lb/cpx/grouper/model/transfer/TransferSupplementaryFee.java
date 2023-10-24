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
 *
 * @author gerschmann
 */
public class TransferSupplementaryFee implements Serializable {

    private static final long serialVersionUID = 1L;

    private String m_code; // Code of additional fee
    private int m_typeid; // id of type(Enum?)
    private double m_cw; // value cw for ETs
    private double m_value; // euro value
    private int m_count; // 1 for ZE/ZP ET can be > 1
    private Date m_from; // for ET not null
    private Date m_to; // for ET not null

    public TransferSupplementaryFee(int type, String code, int count, double cw, double value, Date start, Date end) {
        m_typeid = type;
        m_code = code;
        m_cw = cw;
        m_value = value;
        m_count = count;
        m_from = start == null ? null : new Date(start.getTime());
        m_to = end == null ? null : new Date(end.getTime());
    }

    public String getCode() {
        return m_code;
    }

    public void setCode(String m_code) {
        this.m_code = m_code;
    }

    public int getTypeid() {
        return m_typeid;
    }

    public void setTypeid(int m_typeid) {
        this.m_typeid = m_typeid;
    }

    public double getCw() {
        return m_cw;
    }

    public void setCw(double m_cw) {
        this.m_cw = m_cw;
    }

    public double getValue() {
        return m_value;
    }

    public void setValue(double m_value) {
        this.m_value = m_value;
    }

    public int getCount() {
        return m_count;
    }

    public void setCount(int m_count) {
        this.m_count = m_count;
    }

    public Date getFrom() {
        return m_from == null ? null : new Date(m_from.getTime());
    }

    public void setFrom(Date m_from) {
        this.m_from = m_from == null ? null : new Date(m_from.getTime());
    }

    public Date getTo() {
        return m_to == null ? null : new Date(m_to.getTime());
    }

    public void setTo(Date m_to) {
        this.m_to = m_to == null ? null : new Date(m_to.getTime());
    }

    @Override
    public String toString() {
        return m_code + " " + m_count;
    }
}
