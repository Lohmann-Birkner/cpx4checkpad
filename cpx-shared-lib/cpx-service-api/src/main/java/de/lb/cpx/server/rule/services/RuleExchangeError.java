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
package de.lb.cpx.server.rule.services;

import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.shared.rules.util.RuleValidationStatusEn;
import java.io.Serializable;

/**
 * any information about import or copy process of rules, rule types, rule
 * tables
 *
 * @author gerschmann
 */
public class RuleExchangeError implements Serializable {

    private static final long serialVersionUID = 1L;
    private final long id; // object in DB
    private final RuleValidationStatusEn ruleValidationStatusEn;
    private final PoolTypeEn poolType;
    private String xmlRuleIdent = "";
    private String textFromXml = "";

    public RuleExchangeError(RuleValidationStatusEn ruleValidationStatusEn, PoolTypeEn pPoolType, String xmlRuleIdent, String text, boolean isValidation) {
        this(0, ruleValidationStatusEn, pPoolType, text);
        this.xmlRuleIdent = xmlRuleIdent;
        if(isValidation){
            textFromXml = text;
        }

    }

    public RuleExchangeError(RuleValidationStatusEn ruleValidationStatusEn, PoolTypeEn pPoolType, String xmlRuleIdent, String text) {
        this(0, ruleValidationStatusEn, pPoolType, text);
        this.xmlRuleIdent = xmlRuleIdent;

    }

    public RuleExchangeError(long id, RuleValidationStatusEn ruleValidationStatusEn, PoolTypeEn pPoolType, String text) {
        this.id = id;
        this.ruleValidationStatusEn = ruleValidationStatusEn;
        this.poolType = pPoolType;
//        textFromXml = text;
    }

    public long getId() {
        return id;
    }

    public RuleValidationStatusEn getRuleValidationStatusEn() {
        return ruleValidationStatusEn;
    }

    public PoolTypeEn getPoolType() {
        return poolType;
    }
    
    public String getXmlRuleIdent(){
        return xmlRuleIdent;
    }

    public String getTextFromXml() {
        return textFromXml;
    }

    
    @Override
    public String toString() {
        return " id = " + id
                + " RuleValidationStatusEn = " + ruleValidationStatusEn.name()
                + " poolType " + poolType.name()
                + " xmlRuleIdent = " + xmlRuleIdent
                + " text = " + textFromXml;
    }
}
