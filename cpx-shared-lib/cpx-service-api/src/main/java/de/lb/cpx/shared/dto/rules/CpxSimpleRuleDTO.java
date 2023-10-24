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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.dto.rules;

import de.checkpoint.ruleGrouper.CRGRule;
import de.lb.cpx.model.enums.RuleTypeEn;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import java.io.Serializable;

/**
 * Simple DTO for Ruletransfer from the Server to the Client Contains
 * Information: -ruleId : rid -ruleTyp : typ -ruleNotice : rule_notice
 * -ruleCaption : caption -ruleErrorTyp : errrorTyp -ruleNumber : number
 * -ruleCategory : text
 *
 * @author wilde
 */
public class CpxSimpleRuleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

//    private String ruleId;
//    private String ruleNumber;
//    private String ruleTyp;
//    private String errorTyp;
//    private String ruleCategory;
//    private String caption;
//    private String ruleNotice;
//    private String ruleSuggestion;
    private Double chkCwSimulDiff;
    private String chkCwDrg;
    private String chkReference;
    private CrgRules rule;
    private Double chkCwCareSimulDiff;
    private Double chkFeeSimulDiff;
    private CpxSimpleRisk mRisks; // rule cann have one risk only
    private boolean selectedRuleFlag = false;
    
    public CpxSimpleRuleDTO(CRGRule cpRule){
//currentRule.m_rid, currentRule.m_number, currentRule.m_typeText,
//                        currentRule.m_errorTypeText, currentRule.m_text, currentRule.m_caption, currentRule.m_notice, currentRule.m_suggestion        
        CrgRules newRule = new CrgRules();
        newRule.setCrgrRid(cpRule.m_rid);
        newRule.setCrgrNumber(cpRule.m_number);
        newRule.setCrgrRuleErrorType(getRuleTyp(cpRule.m_typeText));
        CrgRuleTypes type = new CrgRuleTypes();
        type.setCrgtDisplayText(cpRule.m_errorTypeText);
        type.setCrgtShortText(cpRule.m_typeText);
        newRule.setCrgRuleTypes(type);
        newRule.setCrgrCategory(cpRule.m_text);
        newRule.setCrgrCaption(cpRule.m_caption);
        newRule.setCrgrNote(cpRule.m_notice);
        newRule.setCrgrSuggText(cpRule.m_suggestion);
        this.rule = newRule;

    }

//    public CpxSimpleRuleDTO(String ruleId, String ruleNumber, String ruleTyp, String errorTyp, String ruleCategory, String caption, String ruleNotice, String suggestion) {
////        this.ruleId = ruleId;
////        this.ruleNumber = ruleNumber;
////        this.ruleTyp = ruleTyp;
////        this.errorTyp = errorTyp;
////        this.ruleCategory = ruleCategory;
////        this.caption = caption;
////        this.ruleNotice = ruleNotice;
////        this.ruleSuggestion = suggestion;
//        CrgRules newRule = new CrgRules();
//        newRule.setCrgrRid(ruleId);
//        newRule.setCrgrNumber(ruleNumber);
//        newRule.setCrgrRuleErrorType(getRuleTyp(ruleTyp));
//        CrgRuleTypes type = new CrgRuleTypes();
//        type.setCrgtDisplayText(errorTyp);
//        type.setCrgtShortText(errorTyp);
//        newRule.setCrgRuleTypes(type);
//        newRule.setCrgrCategory(ruleCategory);
//        newRule.setCrgrCaption(caption);
//        newRule.setCrgrNote(ruleNotice);
//        newRule.setCrgrSuggText(suggestion);
//        this.rule = newRule;
//    }
//
//    public CpxSimpleRuleDTO(String pRid, String pRuleNumber, String pRuleType, String pErrorType, String pCategory, String pCaption, String pNotice, String suggestion, Double chkCwSimulDiff, String chkCwDrg, String chkReference,Double chkFeeSimulDiff, Double chkCareSimulDiff) {
//        this(pRid, pRuleNumber, pRuleType, pErrorType, pCategory, pCaption, pNotice, suggestion);
////        this.ruleId = rid;
////        this.ruleNumber = ruleNumber;
////        this.ruleTyp = type;
////        this.errorTyp = errorType;
////        this.ruleCategory = category;
////        this.caption = caption;
////        this.ruleNotice = notice;
////        this.ruleSuggestion = suggestion;
//        this.chkCwSimulDiff = chkCwSimulDiff;
//        this.chkCwDrg = chkCwDrg;
//        this.chkCwCareSimulDiff = chkCareSimulDiff;
//        this.chkFeeSimulDiff = chkFeeSimulDiff;
//        this.chkReference = chkReference;
//    }

    public CpxSimpleRuleDTO(CrgRules pRule) {
        super();
        this.rule = pRule;
    }

    public String getRuleId() {
        return rule.getCrgrRid();//ruleId;
    }

    public void setRuleId(String ruleId) {
//        this.ruleId = ruleId;
        rule.setCrgrRid(ruleId);
    }

    public String getRuleNumber() {
        return rule.getCrgrNumber();//ruleNumber;
    }

    public void setRuleNumber(String ruleNumber) {
//        this.ruleNumber = ruleNumber;
        rule.setCrgrNumber(ruleNumber);
    }

    public boolean isError() {
        return getRuleTyp() == RuleTypeEn.STATE_ERROR;
    }

    public boolean isWarning() {
        return getRuleTyp() == RuleTypeEn.STATE_WARNING;
    }

    public boolean isInformation() {
        return getRuleTyp() == RuleTypeEn.STATE_SUGG;
    }

    public boolean isNone() {
        return getRuleTyp() == RuleTypeEn.STATE_NO;
    }

    public final RuleTypeEn getRuleTyp(String pRuleType) {
        switch (pRuleType) {
            case "error":
                return RuleTypeEn.STATE_ERROR;
            case "warning":
                return RuleTypeEn.STATE_WARNING;
            case "suggestion":
                return RuleTypeEn.STATE_SUGG;
            default:
                return RuleTypeEn.STATE_NO;
        }
    }

    public RuleTypeEn getRuleTyp() {
        return rule.getCrgrRuleErrorType();
    }
//    public ErrorLevel getRuleTyp() {
//        switch (ruleTyp) {
//            case "error":
//                return ErrorLevel.ERROR;
//            case "warning":
//                return ErrorLevel.WARNING;
//            case "suggestion":
//                return ErrorLevel.INFORMATION;
//            default:
//                return ErrorLevel.NONE;
//        }
//    }

    public int getRuleTypeSeverity() {
        if (getRuleTyp() == null) {
            return -1;
        }
        switch (getRuleTyp()) {
            case STATE_ERROR:
                return 3;
            case STATE_WARNING:
                return 2;
            case STATE_SUGG:
                return 1;
            case STATE_NO:
                return 0;
            default:
                return -1;
        }
//        return getRuleTyp().getSeverity();
    }

    public void setRuleTyp(String pRuleType) {
        //this.ruleTyp = ruleTyp;
        rule.setCrgrRuleErrorType(getRuleTyp(pRuleType));
    }

    public String getErrorTyp() {
        return rule.getCrgRuleTypes().getCrgtDisplayText();//errorTyp;
    }

    public void setErrorTyp(String errorTyp) {
        rule.getCrgRuleTypes().setCrgtDisplayText(errorTyp);
        //this.errorTyp = errorTyp;
    }

    public String getRuleCategory() {
        return rule.getCrgrCategory();//ruleCategory;
    }

    public void setRuleCategory(String ruleCategory) {
        //this.ruleCategory = ruleCategory;
        rule.setCrgrCategory(ruleCategory);
    }

    public String getCaption() {
        return rule.getCrgrCaption();//caption;
    }

    public void setCaption(String caption) {
        //this.caption = caption;
        rule.setCrgrCaption(caption);
    }

    public String getRuleNotice() {
        return rule.getCrgrNote();//ruleNotice;
    }

    public void setRuleNotice(String ruleNotice) {
//        this.ruleNotice = ruleNotice;
        rule.setCrgrNote(ruleNotice);
    }

    public String getRuleSuggestion() {
        return rule.getCrgrSuggText();//ruleSuggestion;
    }

    public void setRuleSuggestion(String ruleSuggestion) {
        rule.setCrgrSuggText(ruleSuggestion);
//        this.ruleSuggestion = ruleSuggestion;
    }

    public Double getChkCwSimulDiff() {
        return chkCwSimulDiff;
    }

    public void setChkCwSimulDiff(Double chkCwSimulDiff) {
        this.chkCwSimulDiff = chkCwSimulDiff;
    }

    public String getChkCwDrg() {
        return chkCwDrg;
    }

    public void setChkCwDrg(String chkCwDrg) {
        this.chkCwDrg = chkCwDrg;
    }

    public String getChkReference() {
        return chkReference;
    }

    public void setChkReference(String chkReference) {
        this.chkReference = chkReference;
    }

    public Double getChkCwCareSimulDiff() {
        return chkCwCareSimulDiff;
    }

    public void setChkCwCareSimulDiff(Double chkCwCareSimulDiff) {
        this.chkCwCareSimulDiff = chkCwCareSimulDiff;
    }
    
     public Double getChkFeeSimulDiff() {
        return chkFeeSimulDiff;
    }

    public void setChkFeeSimulDiff(Double chkFeeSimulDiff) {
        this.chkFeeSimulDiff = chkFeeSimulDiff;
    }

    public CpxSimpleRisk getRisks() {

        return mRisks;
    }

    public void setRisks(CpxSimpleRisk risks){
        mRisks = risks;
    }
    
    public Integer getRiskPercentValue(){
        return mRisks == null?0:mRisks.getRiskWastePercentValueAsNumber();
    }
    public Integer getRiskAuditPercentValue(){
        return mRisks == null?0:mRisks.getRiskAuditPercentValueAsNumber();
    }
    public String getTranslatedRiskAreas(){
        return mRisks == null?null:mRisks.getRiskAreasAsEn().getTranslation().getValue();

    }

    public void setSelectedRuleFl(boolean b) {
        selectedRuleFlag = b;
    }

    public boolean isSelectedRuleFlag() {
        return selectedRuleFlag;
    }
    
    
}
