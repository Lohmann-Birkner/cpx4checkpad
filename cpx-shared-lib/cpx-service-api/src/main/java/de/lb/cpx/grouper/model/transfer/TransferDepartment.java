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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author gerschmann
 */
public class TransferDepartment extends TransferHospitalUnit {

    private static final long serialVersionUID = 1L;

    private final String m_department;
    private Date m_transferDate = null;
    private Date m_adm = null;

    private final List<TransferWard> m_wards = new ArrayList<>();

    private boolean m_isAdmission = false;
    private boolean m_isDischarge = false;
    private boolean m_isTreating = false;

    /* fuer Abteilungsgrouping muss erweitert werden, so dass diagnosen und Prozeduren 
    gefuellt sind
     */
//    private TransferResult m_result;
    public TransferDepartment(String department) {
        m_department = department;

    }

    public TransferDepartment(String department, Date transferDate, Date pAdm, boolean isAdm, boolean isDis, boolean isTreat) {
        m_department = department;
        m_transferDate = transferDate == null ? null : new Date(transferDate.getTime());
        m_adm = pAdm == null ? null : new Date(pAdm.getTime());
        m_isAdmission = isAdm;
        m_isDischarge = isDis;
        m_isTreating = isTreat;
    }

    public Date getAdm() {
        return m_adm == null ? null : new Date(m_adm.getTime());
    }

    public String getDepartment() {
        return m_department;
    }

    public Date getTransferDate() {
        return m_transferDate == null ? null : new Date(m_transferDate.getTime());
    }

    public boolean isAdmission() {
        return m_isAdmission;
    }

    public boolean isDischarge() {
        return m_isDischarge;
    }

    public boolean isTreating() {
        return m_isTreating;
    }

    public void addWard(String ward) {
        m_wards.add(new TransferWard(ward));
    }

    public List<TransferWard> getWards() {
        return new ArrayList<>(m_wards);
    }

}
