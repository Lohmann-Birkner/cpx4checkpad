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

import de.checkpoint.ruleGrouper.CRGInputOutput;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gerschmann
 */
public class TransferPatient {
    private final List<TransferCase>patientCases = new ArrayList<>();

    // By SingleCaseGrouping we have to check one case only. 
    // By batchGrouping is mMainCase not set. All cases from the paientCases -list are to be grouped and checked with rules

    private TransferCase mMainCase = null; 
    private int mModelId = 0;
    private final List<CRGInputOutput> tempHistoryCasesGrouped = new ArrayList<>();
    private long patientId = -1;

    public TransferPatient(int pModelId) {
        mModelId = pModelId;
    }

    public void setMainCase(TransferCase pMainCase) {
        mMainCase = pMainCase;
    }

    public void addHistoryCase(TransferCase trHisCase) {
        if(!patientCases.contains(trHisCase)){
            patientCases.add(trHisCase);
        }
    }

    public List<TransferCase> getHistoryCases() {
        return patientCases;
    }
    
    public TransferCase getMainCase(){
        return mMainCase;
    }

    public int getGrouperModelId() {
        return mModelId;
    }

    public void setGrouperModelId(int mModelId) {
        this.mModelId = mModelId;
    }
    
    public void addGroupingResult4HistoryCase(CRGInputOutput inout){
        this.tempHistoryCasesGrouped.add(inout);
    }

    public List<CRGInputOutput>  getGroupingResult4HistoryCase() {
        return tempHistoryCasesGrouped;
    }

    public void clearHistory() {
        tempHistoryCasesGrouped.clear();
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

}
