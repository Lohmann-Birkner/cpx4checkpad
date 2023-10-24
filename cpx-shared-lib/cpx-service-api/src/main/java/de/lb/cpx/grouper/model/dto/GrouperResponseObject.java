/* 
 * Copyright (c) 2015 Lohmann & Birkner.
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
 *    2015  Wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.grouper.model.dto;

import de.lb.cpx.grouper.model.transfer.BatchGroupResult;
import de.lb.cpx.grouper.model.transfer.TransferCase;
import de.lb.cpx.grouper.model.transfer.TransferRule;
import de.lb.cpx.grouper.model.transfer.TransferSupplementaryFee;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Klasse zur Darstellung der Antwort des Servers auf eine Anfrage an den
 * Grouper Service
 *
 * @author Wilde
 */
public class GrouperResponseObject implements Serializable {

    private static final long serialVersionUID = 1L;

    private final TransferCase m_result;
    private final BatchGroupResult m_batchResult;
    private final List<TransferRule> m_detectedRules = new ArrayList<>();
    private boolean m_vwdValid = true;
    private double m_supplFeeSum = 0;

    public GrouperResponseObject(TransferCase tCase, BatchGroupResult batchGroupResult) {

        m_result = tCase;
        m_batchResult = batchGroupResult;
    }

    public TransferCase getResult() {
        return m_result;
    }

    public List<TransferRule> getDetectedRules() {
        return new ArrayList<>(m_detectedRules);
    }

    public void addRule(TransferRule rule) {
        m_detectedRules.add(rule);
    }

    public void addAdditionalFee2Ops(int type, int procId, String etCode, int etCount, double cw, double value, Date etStart, Date etEnd) {
        TransferSupplementaryFee addFee = new TransferSupplementaryFee(type, etCode, etCount, cw, value, etStart, etEnd);
        m_result.setSupplementaryFee2Ops(procId, addFee);
        m_supplFeeSum+= value;
    }

    public BatchGroupResult getBatchResult() {
        return m_batchResult;
    }

    public boolean isVwdValid() {
        return m_vwdValid;
    }

    public void setVwdValid(boolean valid) {
        m_vwdValid = valid;
    }
    public double getSupplFeeSum() {
        return m_supplFeeSum;
    }

}
