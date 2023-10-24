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
package de.lb.cpx.shared.rules.util;

import de.lb.cpx.server.commonDB.model.rules.CrgRules;

/**
 * Class will be used for logging of rule import/export for WebApp
 *
 * @author gerschmann
 */
public class RuleHeader4ValidationLog extends RuleValidationLog {

    private static final long serialVersionUID = 1L;

    private String ruleNumber = "";
    private String ruleCaption = "";
    private String ruleCategory = "";
    private String ruleErrType = "";
    private String ruleType = "";

    private String errMessage = "";

    public RuleHeader4ValidationLog(CrgRules cpxRule) {
        this(cpxRule, null);
    }

    public RuleHeader4ValidationLog(CrgRules cpxRule, String message) {

        super(cpxRule.getCrgrIdentifier() == null ? "" : cpxRule.getCrgrIdentifier());
        ruleNumber = cpxRule.getCrgrNumber() == null ? "" : cpxRule.getCrgrNumber();
        ruleCaption = cpxRule.getCrgrCaption() == null ? "" : cpxRule.getCrgrCaption();
        ruleCategory = cpxRule.getCrgrCategory() == null ? "" : cpxRule.getCrgrCategory();
        ruleType = cpxRule.getCrgrRuleErrorType() == null ? "" : cpxRule.getCrgrRuleErrorType().name();
        ruleErrType = cpxRule.getCrgRuleTypes() == null ? ""
                : (cpxRule.getCrgRuleTypes().getCrgtDisplayText() == null ? "" : cpxRule.getCrgRuleTypes().getCrgtDisplayText());
        errMessage = message == null ? "" : message;
    }

    public String getRuleNumber() {
        return ruleNumber;
    }

    public void setRuleNumber(String ruleNumber) {
        this.ruleNumber = ruleNumber;
    }

    public String getRuleCaption() {
        return ruleCaption;
    }

    public void setRuleCaption(String ruleCaption) {
        this.ruleCaption = ruleCaption;
    }

    public String getRuleCategory() {
        return ruleCategory;
    }

    public void setRuleCategory(String ruleCategory) {
        this.ruleCategory = ruleCategory;
    }

    public String getRuleErrType() {
        return ruleErrType;
    }

    public void setRuleErrType(String ruleErrType) {
        this.ruleErrType = ruleErrType;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

}
