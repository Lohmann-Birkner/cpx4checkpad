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

import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.shared.rules.enums.CaseValidationGroupErrList;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Result class for rule analyser
 *
 * @author gerschmann
 */
public class TransferRuleAnalyseResult implements Serializable {

    private static final Logger LOG = Logger.getLogger(TransferRuleAnalyseResult.class.getName());
    private static final long serialVersionUID = 1L;

    private TransferRuleResult ruleResult = null;
    private TransferGroupResult groupResult = null;
    private double baserate;
    private CaseTypeEn caseTypeEn;
    private int lengthOfStay;
    private double suggResult;
    private double deltaCw;
    private List<CaseValidationGroupErrList> caseValidationGroupErrList = null;
    private String suggDrg = null;
    private int caseYear = 0;

    public void setRuleResult(TransferRuleResult pRuleResult) {
        ruleResult = pRuleResult;
    }

    public TransferRuleResult getRuleResult() {
        return ruleResult;
    }

    public TransferGroupResult getGroupResult() {
        return groupResult;
    }

    public void setGroupResult(TransferGroupResult mGroupResult) {
        this.groupResult = mGroupResult;
    }

    public void setCaseType(CaseTypeEn pCaseTypeEn) {
        caseTypeEn = pCaseTypeEn;
    }

    public void setBaserate(double pBaseRate) {
        baserate = pBaseRate;
    }

    public void setLengthOfStay(int pLengthOfStay) {
        lengthOfStay = pLengthOfStay;
    }

    public double getBaserate() {
        return baserate;
    }

    public CaseTypeEn getCaseTypeEn() {
        return caseTypeEn;
    }

    public int getLengthOfStay() {
        return lengthOfStay;
    }

    public double getSuggResult() {
        return suggResult;
    }

    public void setSuggResult(double suggResult) {
        this.suggResult = suggResult;
    }

    public void setSuggResults(double suggResult, String suggDrg) {
        this.suggResult = suggResult;
        if (groupResult != null) {
            deltaCw = suggResult - groupResult.getCwEffectiv();
        }
        this.suggDrg = suggDrg;
    }

    public void setDeltaCw(double pDelta) {
        deltaCw = pDelta;
    }

    public double getDeltaCw() {
        return deltaCw;
    }

    public String getSuggDrg() {
        return suggDrg;
    }

    public void setSuggDrg(String pSuggDrg) {
        suggDrg = pSuggDrg;
    }

    public void setGroupErrorValidationList(List<CaseValidationGroupErrList> errList) {
        caseValidationGroupErrList = errList;
    }

    public List<CaseValidationGroupErrList> getCaseValidationGroupErrList() {
        return caseValidationGroupErrList;
    }

    public void setCaseValidationGroupErrList(List<CaseValidationGroupErrList> errorList) {
        caseValidationGroupErrList = errorList;
    }

    public void printErrorList() {
        if (caseValidationGroupErrList == null || caseValidationGroupErrList.isEmpty()) {
            LOG.log(Level.INFO, "there are no errors detected");
        } else {
            LOG.log(Level.INFO, "detected errors:");
            caseValidationGroupErrList.forEach((err) -> {
                LOG.log(Level.INFO, "{0}: {1}", new Object[]{err.name(), err.getLangKey()});
            });
        }
    }

    public int getCaseYear() {
        return caseYear;
    }

    public void setCaseYear(int caseYear) {
        this.caseYear = caseYear;
    }

}
