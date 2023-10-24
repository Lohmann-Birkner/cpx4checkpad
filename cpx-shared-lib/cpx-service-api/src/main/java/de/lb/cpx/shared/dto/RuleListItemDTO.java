/*
 * Copyright (c) 2017 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2017  Bohm - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.dto;

/**
 *
 * @author Bohm
 */
public class RuleListItemDTO extends WorkingListItemDTO {

    private static final long serialVersionUID = 1L;

    private Double chkCwSimulDiff;
    private String ruleDescription;
    private Integer crgrId;
    private String ruleSuggestion;
    private String typeText;
    private Long checkResultId;
    private String xRuleNumber;
    private String rule;
    private String crgrCaption;
    private String crgrCategory;
    private Boolean ruleSelected;

//    @Override
//    public String getHospitalIdent() {
//        return "not implemented";
//    }
//
//    @Override
//    public String getPatNumber() {
//        return "not implemented";
//    }
    /**
     * @return the chkCwSimulDiff
     */
    public Double getChkCwSimulDiff() {
        return chkCwSimulDiff;
    }

    /**
     * @param chkCwSimulDiff the chkCwSimulDiff to set
     */
    public void setChkCwSimulDiff(Double chkCwSimulDiff) {
        this.chkCwSimulDiff = chkCwSimulDiff;
    }

    /**
     * @return the ruleDescription
     */
    public String getRuleDescription() {
        return ruleDescription;
    }

    /**
     * @param ruleDescription the ruleDescription to set
     */
    public void setRuleDescription(String ruleDescription) {
        this.ruleDescription = ruleDescription;
    }

    /**
     * @return the crgrId
     */
    public Integer getCrgrId() {
        return crgrId;
    }

    /**
     * @param crgrId the crgrId to set
     */
    public void setCrgrId(Integer crgrId) {
        this.crgrId = crgrId;
    }

    /**
     * @return the ruleSuggestion
     */
    public String getRuleSuggestion() {
        return ruleSuggestion;
    }

    /**
     * @param ruleSuggestion the ruleSuggestion to set
     */
    public void setRuleSuggestion(String ruleSuggestion) {
        this.ruleSuggestion = ruleSuggestion;
    }

    /**
     * @return the typeText
     */
    public String getTypeText() {
        return typeText;
    }

    /**
     * @param typeText the typeText to set
     */
    public void setTypeText(String typeText) {
        this.typeText = typeText;
    }

    /**
     * @return the checkResultId
     */
    public Long getCheckResultId() {
        return checkResultId;
    }

    /**
     * @param checkResultId the tCaseId to set
     */
    public void setCheckResultId(Long checkResultId) {
        this.checkResultId = checkResultId;
    }

    /**
     * @return the xRuleNumber
     */
    public String getXRuleNumber() {
        return xRuleNumber;
    }

    /**
     * @param xRuleNumber the xRuleNumber to set
     */
    public void setXRuleNumber(String xRuleNumber) {
        this.xRuleNumber = xRuleNumber;
    }

    public String getCrgrCaption() {
        return crgrCaption;
    }

    public void setCrgrCaption(String crgrCaption) {
        this.crgrCaption = crgrCaption;
    }

    public String getCrgrCategory() {
        return crgrCategory;
    }

    public void setCrgrCategory(String crgrCategory) {
        this.crgrCategory = crgrCategory;
    }
    
//    @Override
    @Override
    public String getRule() {
        return rule;
    }

//    @Override
    @Override
    public void setRule(String rule) {
        this.rule = rule;
    }

    public Boolean getRuleSelected() {
        return ruleSelected;
    }

    public void setRuleSelected(Boolean ruleSelected) {
        this.ruleSelected = ruleSelected;
    }

}
