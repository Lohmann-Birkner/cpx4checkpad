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
//import java.util.logging.Logger;

/**
 *
 * @author gerschmann
 */
public class TransferIcd extends TransferCode {

    private static final long serialVersionUID = 1L;

    private int m_refType = 0;
    private String m_prim = null;
    private boolean m_isHdx = false;
    private boolean m_isAdm = false;

    private TransferGroupResult m_groupResult;

    public TransferIcd(String diagnosis) {
        super(diagnosis, 0);
    }

    public TransferIcd(String diagnosis, int localisation) {
        super(diagnosis, localisation);
    }

    public TransferIcd(long diagId, String diagnosis, int refType, int localisation, boolean isHdx, boolean isAdm) {
        super(diagId, diagnosis, localisation);

        m_refType = refType;

        m_isAdm = isAdm;
        m_isHdx = isHdx;
    }

    public boolean isAdm() {
        return m_isAdm;
    }

    public boolean isHdx() {
        return m_isHdx;
    }

    public int getRefType() {
        return m_refType;
    }

    public String getPrim() {
        return m_prim;
    }

    public void setPrimIcd(String prim) {
        this.m_prim = prim;
    }

    public void setGroupResult(TransferGroupResult result) {
        m_groupResult = result;
    }

    public TransferGroupResult getGroupResult() {
        return m_groupResult;
    }

    void setSupplementartyFee2OpsResult(int procId, TransferSupplementaryFee addFee) {
//        Logger.getLogger(TransferIcd.class.getName()).log(Level.INFO, "to add: procId = " + procId + "fee= " + addFee.toString());
        if (m_groupResult == null) {
            return;
        }
        m_groupResult.setSupplementaryFee((long) procId, addFee);
    }

}
