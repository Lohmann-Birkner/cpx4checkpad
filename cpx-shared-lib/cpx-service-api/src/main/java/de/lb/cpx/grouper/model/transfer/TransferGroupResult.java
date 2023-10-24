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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Groupingergebnis für die entsprechende Hauptdiagnose
 *
 * @author gerschmann
 */
public class TransferGroupResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code; //DRG/PEPP
    private String group;//MDC/SK
    private String gst = "00";
    private String gpdx = "0";
    private int modelId;
    private boolean isAuto;
    private int pccl = 0;
    private boolean isNegotiated;
    private int caseYear;

    private double cwEffectiv;
    private double cwCatalog;
    private double careCwDay;

//    private TransferResult caseResult; // DRG oder Pepp, abhängig von dem Falltyp
    private HashMap<Long, TransferOpsResult> opsRes = new HashMap<>();
    private List<TransferIcdResult> icdRes = new ArrayList<>();


    public TransferGroupResult() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String m_code) {
        this.code = m_code;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String m_group) {
        this.group = m_group;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String m_gst) {
        this.gst = m_gst;
    }

    public String getGpdx() {
        return gpdx;
    }

    public void setGpdx(String m_gpdx) {
        this.gpdx = m_gpdx;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int m_modelId) {
        this.modelId = m_modelId;
    }

    public boolean isAuto() {
        return isAuto;
    }

    public void setIsAuto(boolean m_isAuto) {
        this.isAuto = m_isAuto;
    }

    public int getPccl() {
        return pccl;
    }

    public void setPccl(Integer m_pccl) {
        this.pccl = m_pccl;
    }

    public void setPccl(int m_pccl) {
        this.pccl = m_pccl;
    }

    public double getCwEffectiv() {
        return cwEffectiv;
    }

    public void setCwEffectiv(double m_cwEffectiv) {
        this.cwEffectiv = m_cwEffectiv;
    }

    public HashMap<Long, TransferOpsResult> getOpsRes() {
        return opsRes;
    }

    public void setOpsRes(HashMap<Long, TransferOpsResult> m_opsRes) {
        this.opsRes = m_opsRes;
    }

    public List<TransferIcdResult> getIcdRes() {
        return icdRes;
    }

    public void setIcdRes(List<TransferIcdResult> m_icdRes) {
        this.icdRes = m_icdRes;
    }

    public void setSupplementaryFee(Long procId, TransferSupplementaryFee addFee) {
        TransferOpsResult opsRes = getOpsRes().get(procId);
        if (opsRes != null) {
            opsRes.setSupplFee(addFee);
//            Logger.getLogger(TransferIcd.class.getName()).log(Level.INFO, "added: procId = " + procId + "fee= " + addFee.toString());
        }

    }

    public boolean isNegotiated() {
        return isNegotiated;
    }

    public void setIsNegotiated(boolean isNegotiated) {
        this.isNegotiated = isNegotiated;
    }

    public int getCaseYear() {
        return caseYear;
    }

    public void setCaseYear(int caseYear) {
        this.caseYear = caseYear;
    }

    void setCwCatalog(double cw) {
        cwCatalog = cw;
    }

    public double getCwCatalog() {
        return cwCatalog;
    }
    public double getCareCwDay() {
        return careCwDay;
    }

    public void setCareCwDay(double pCareCwDay) {
        this.careCwDay = pCareCwDay;
    }


}
