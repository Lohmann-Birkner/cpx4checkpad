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
 *    2019  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.grouper.model.transfer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gerschmann
 */
public abstract class TransferHospitalUnit implements Serializable {

    private final List<TransferOps> m_ops = new ArrayList<>();
    private final List<TransferIcd> m_icds = new ArrayList<>();

    public List<TransferIcd> getIcds() {
        return new ArrayList<>(m_icds);
    }

    public List<TransferOps> getOps() {
        return new ArrayList<>(m_ops);
    }

    public void addIcd(long id, String diagnosis, int refType, int localisation, boolean isHdx, boolean isAdm) {
        m_icds.add(new TransferIcd(id, diagnosis, refType, localisation, isHdx, isAdm));
    }

    public void addOps(TransferOps ops) {
        m_ops.add(ops);
    }

    public void addIcd(TransferIcd icd) {
        m_icds.add(icd);
    }
}
