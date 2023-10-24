/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.ruleviewer.util;

/**
 *
 * @author gerschmann
 */
public class HistoryFeeTableViewHelper {
    private final String caseNr;
    private  final String feeKey;
    private final String feeValue;
    private final int feeCount;
    
    public HistoryFeeTableViewHelper(String pCaseNr, String pFeeKey, String pFeeValue, int pFeeCount){
        caseNr = pCaseNr;
        feeKey = pFeeKey;
        feeValue = pFeeValue;
        feeCount = pFeeCount;
    }

    public String getCaseNr() {
        return caseNr;
    }

    public String getFeeKey() {
        return feeKey;
    }

    public String getFeeValue() {
        return feeValue;
    }

    public int getFeeCount() {
        return feeCount;
    }
    
}
