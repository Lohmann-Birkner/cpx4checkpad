/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.grouper.model.transfer;

import de.lb.cpx.shared.rules.enums.CaseValidationGroupErrList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A communication class for transfer of the test values of the rule editor
 *
 * @author gerschmann
 */
public class Transfer4RuleAnalyse {

    private TransferCase mTransferCase;
    private String mRuleXmlString;
    private Map<String, String> mRuleTables = null;
    private Map<String, String> mRuleTablesId2NameMapping = null;
    private List<CaseValidationGroupErrList> caseValidationGroupErrList = null;
    private List<TransferCase> mHistoryCases = null;

    public Transfer4RuleAnalyse() {

    }

    public Transfer4RuleAnalyse(TransferCase pCase, String pRule) {
        mTransferCase = pCase;
        mRuleXmlString = pRule;

    }

    public Transfer4RuleAnalyse(TransferCase pCase, String pRule, Map<String, String> pRuleTables) {
        mRuleTables = new HashMap<>(pRuleTables);
        mTransferCase = pCase;
        mRuleXmlString = pRule;
    }

    public Transfer4RuleAnalyse(TransferCase cse, String pRule, Map<String, String> pTables, List<CaseValidationGroupErrList> errorList) {
        this(cse, pRule, pTables);
        caseValidationGroupErrList = errorList;
    }

    public Transfer4RuleAnalyse(TransferCase cse, String pRule, Map<String, String> pTables, List<CaseValidationGroupErrList> errorList, List<TransferCase> pHistoryCases) {
        this(cse, pRule, pTables, errorList);
        mHistoryCases = pHistoryCases;
    }

    public TransferCase getTransferCase() {
        return mTransferCase;
    }

    public void setTransferCase(TransferCase pTransferCase) {
        mTransferCase = pTransferCase;
    }

    public String getRuleXmlString() {
        return mRuleXmlString;
    }

    public void setRuleXmlString(String pRuleXmlString) {
        mRuleXmlString = pRuleXmlString;
    }

    public Map<String, String> getRuleTables() {
        if (mRuleTables == null) {
            mRuleTables = new HashMap<>();
        }
        return mRuleTables;
    }

    public void addRuleTable(String pTableName, String pTableContent) {
        getRuleTables().put(pTableName, pTableContent);
    }

    public void setTableId2NameMapping(Map<String, String> ruleTableNames4Map) {
        mRuleTablesId2NameMapping = ruleTableNames4Map;
    }

    public Map<String, String> getRuleTablesId2NameMapping() {
        return mRuleTablesId2NameMapping;
    }

    public List<CaseValidationGroupErrList> getCaseValidationGroupErrList() {
        return caseValidationGroupErrList;
    }

    public void setCaseValidationGroupErrList(List<CaseValidationGroupErrList> caseValidationGroupErrList) {
        this.caseValidationGroupErrList = caseValidationGroupErrList;
    }

    public boolean hasHistoryCases() {
        return mHistoryCases != null && !mHistoryCases.isEmpty();
    }

    public List<TransferCase> getHistoryCases() {
        return mHistoryCases;
    }

}
