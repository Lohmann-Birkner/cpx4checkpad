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

/**
 *
 * @author gerschmann
 */
public class BatchGroupResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean m_isGrouped;
    private boolean m_isHtp;
    private boolean m_isLtp;
    private boolean m_isTransfer;
    private boolean m_isErrDrg;
    private int m_auxIcdCount;
    private boolean m_isDead;
    private boolean m_isDayCare;
    private int m_pccl;
    private int m_careDays;
    private int m_losIntensiv;
    private boolean m_isIntensiv;
    private double m_cwEff;
    private double m_cwCatalog;
    private int m_aux9count;
    private int m_nalos;

    public boolean isGrouped() {
        return m_isGrouped;
    }

    public void isGrouped(boolean m_isGrouped) {
        this.m_isGrouped = m_isGrouped;
    }

    public boolean isHtp() {
        return m_isHtp;
    }

    public void isHtp(boolean m_isHtp) {
        this.m_isHtp = m_isHtp;
    }

    public boolean isLtp() {
        return m_isLtp;
    }

    public void isLtp(boolean m_isLtp) {
        this.m_isLtp = m_isLtp;
    }

    public boolean isTransfer() {
        return m_isTransfer;
    }

    public void isTransfer(boolean m_isTransfer) {
        this.m_isTransfer = m_isTransfer;
    }

    public boolean isErrDrg() {
        return m_isErrDrg;
    }

    public void isErrDrg(boolean m_isErrDrg) {
        this.m_isErrDrg = m_isErrDrg;
    }

    public int getAuxIcdCount() {
        return m_auxIcdCount;
    }

    public void setAuxIcdCount(int m_auxIcdCount) {
        this.m_auxIcdCount = m_auxIcdCount;
    }

    public boolean isDead() {
        return m_isDead;
    }

    public void isDead(boolean m_isDead) {
        this.m_isDead = m_isDead;
    }

    public boolean isDayCare() {
        return m_isDayCare;
    }

    public void isDayCare(boolean m_isDayCare) {
        this.m_isDayCare = m_isDayCare;
    }

    public int getPccl() {
        return m_pccl;
    }

    public void setPccl(int m_pccl) {
        this.m_pccl = m_pccl;
    }

    public int getCareDays() {
        return m_careDays;
    }

    public void setCareDays(int m_careDays) {
        this.m_careDays = m_careDays;
    }

    public int getLosIntensiv() {
        return m_losIntensiv;
    }

    public void setLosIntensiv(int m_losIntensiv) {
        this.m_losIntensiv = m_losIntensiv;
        m_isIntensiv = (m_losIntensiv > 0);
    }

    public boolean isIntensiv() {
        return m_isIntensiv;
    }

    public void isIntensiv(boolean m_isIntensiv) {
        this.m_isIntensiv = m_isIntensiv;
    }

    public double getCwEff() {
        return m_cwEff;
    }

    public void setCwEff(double m_cwEff) {
        this.m_cwEff = m_cwEff;
    }

    public double getCwCatalog() {
        return m_cwCatalog;
    }

    public void setCwCatalog(double m_cw) {
        this.m_cwCatalog = m_cw;
    }

    public int getAux9count() {
        return m_aux9count;
    }

    public void setAux9count(int m_aux9count) {
        this.m_aux9count = m_aux9count;
    }

    public int getNalos() {
        return m_nalos;
    }

    public void setNalos(int m_nalos) {
        this.m_nalos = m_nalos;
    }

}
