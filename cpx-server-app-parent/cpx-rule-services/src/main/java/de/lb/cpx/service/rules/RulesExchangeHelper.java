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
package de.lb.cpx.service.rules;

import de.checkpoint.ruleGrouper.CRGRule;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.rule.services.RuleExchangeError;
import de.lb.cpx.shared.rules.util.RuleHeader4ValidationLog;
import de.lb.cpx.shared.rules.util.RuleTableValidationLog;
import de.lb.cpx.shared.rules.util.RuleTypeValidationLog;
import de.lb.cpx.shared.rules.util.RuleValidationLog;
import de.lb.cpx.shared.rules.util.RuleValidationStatusEn;
import de.lb.cpx.shared.rules.util.RulesImportStatus;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * collects infos and objects to import/export of rules
 *
 * @author gerschmann
 */
public class RulesExchangeHelper implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(RulesExchangeHelper.class.getName());

    private final List<String> newRules = new ArrayList<>();
    private final List<String> rules4NewTypes = new ArrayList<>();
    private final List<String> changedRules = new ArrayList<>();
    private final List<String> newTables = new ArrayList<>();
    private final List<String> changedTables = new ArrayList<>();// old table->new table  to check the changes
    private final List<String> newRuleTypes = new ArrayList<>();
    private final List<String> notFoundTables = new ArrayList<>();
    private final Map<CRGRule, ArrayList<String>> rules4notFoundTables = new HashMap<>();
    private final StringBuilder outputText = new StringBuilder();
    private List<CrgRuleTables> ruleTables4Export = null;
    private List<CrgRuleTypes> ruleTypes4export = null;
    private String xmlString4Export = null;
    private final RulesImportStatus rulesImportStatus = new RulesImportStatus();
    private int rulesYear = 0;
    public RulesExchangeHelper() {

    }

    public List<String> getNewRules() {
        return newRules;
    }

    public List<String> getChangedRules() {
        return changedRules;
    }

    public List<String> getNewTables() {
        return newTables;
    }

    public List<String> getChangedTables() {
        return changedTables;
    }

    public List<String> getNewRuleTypes() {
        return newRuleTypes;
    }

    public List<String> getRules4NewTypes() {
        return rules4NewTypes;
    }

    public void addNewTableList(StringBuilder lRet) {

        addStringList(lRet, "new tables ", this.newTables);
        outputText.append(lRet);
    }

    public void addChangedTableList(StringBuilder lRet) {

        addStringList(lRet, "changed tables ", this.changedTables);
        outputText.append(lRet);
    }

    private void addStringList(StringBuilder lRet, String header, List<String> list) {

        lRet.append(header).append(list.isEmpty() ? "not found" : "saved:").append("<br><ul>");
        list.forEach((tName) -> {
            lRet.append("<li>").append(tName).append("</li>");
        });
        lRet.append(list.isEmpty() ? "" : "</ul><br>");
    }

    public void addNotFoundTable4Rule(CRGRule cpxRule, String tableName) {
        if (!notFoundTables.contains(tableName)) {
            notFoundTables.add(tableName);
        }
        ArrayList<String> tabNames = rules4notFoundTables.get(cpxRule);
        if (tabNames == null) {
            tabNames = new ArrayList<>();
            rules4notFoundTables.put(cpxRule, tabNames);
        }
        tabNames.add(tableName);
    }

    public void addLoggerText(String string) {
        outputText.append(string).append("<br>");
    }

    @Override
    public String toString() {
        return this.outputText.toString();
    }

    public void add2LogNotFoundTables4Rules() {
        if (!rules4notFoundTables.isEmpty()) {
            outputText.append(" rules with not found tables:<br>");
            Set<CRGRule> rules = rules4notFoundTables.keySet();
            for (CRGRule rule : rules) {
                ArrayList<String> tabNames = rules4notFoundTables.get(rule);
                if (tabNames != null && !tabNames.isEmpty()) {
                    outputText.append("RID: ").append(rule.getRuleID()).append(" content: ").append(rule.toString()).append("<br><ul>");
                    for (String tabName : tabNames) {
                        outputText.append("<li>").append(tabName).append("</li>");
                    }
                    outputText.append("</ul><br>");
                }
            }
        }
    }

    public void setRuleTables(List<CrgRuleTables> tables) {
        this.ruleTables4Export = tables;
    }

    public void setTypes(List<CrgRuleTypes> types) {
        ruleTypes4export = types;
    }

    public List<CrgRuleTables> getRuleTables4Export() {
        return ruleTables4Export == null ? new ArrayList<>() : new ArrayList<>(ruleTables4Export);
    }

    public List<CrgRuleTypes> getRuleTypes4export() {
        return ruleTypes4export == null ? new ArrayList<>() : new ArrayList<>(ruleTypes4export);
    }

    public void addTable(RuleValidationStatusEn ruleValidationStatusEn, String tableName) {
        rulesImportStatus.addRuleImportResult(new RuleTableValidationLog(tableName), ruleValidationStatusEn);
        addValidation2Array(ruleValidationStatusEn, tableName);
    }

    public void addTable(RuleValidationStatusEn ruleValidationStatusEn, String tableName, String oldContent, String newContent) {
        rulesImportStatus.addRuleImportResult(new RuleTableValidationLog(tableName, oldContent, newContent), ruleValidationStatusEn);
        addValidation2Array(ruleValidationStatusEn, tableName);
    }

    public void addRuleType(RuleValidationStatusEn ruleValidationStatusEn, CrgRuleTypes ruleType) {
        rulesImportStatus.addRuleImportResult(new RuleTypeValidationLog(ruleType.getCrgtShortText(), ruleType.getCrgtDisplayText()), ruleValidationStatusEn);

        addValidation2Array(ruleValidationStatusEn, ruleType.getCrgtShortText());
    }

    public String getJsonString() {
        return rulesImportStatus.getJsonString();
    }

    public RulesImportStatus getRulesImportStatus() {
        return rulesImportStatus.getRulesImportStatus();
    }

    public void setImportStatus(RuleValidationStatusEn ruleValidationStatusEn) {
        rulesImportStatus.setEndImportStatus(ruleValidationStatusEn);
    }

    public void addRuleType(RuleValidationStatusEn ruleValidationStatusEn, String m_errorTypeText, CrgRules fromCpRule) {
        // type of rule is not found     
        rulesImportStatus.addRuleImportResult(new RuleHeader4ValidationLog(fromCpRule, ruleValidationStatusEn.getTranslation() + ":" + m_errorTypeText), ruleValidationStatusEn);
        addValidation2Array(ruleValidationStatusEn, m_errorTypeText);
    }

    public void addErrorStatus(RuleValidationStatusEn ruleValidationStatusEn, String pErrorMessage) {
        rulesImportStatus.addErrorStatus(ruleValidationStatusEn, pErrorMessage);
    }

    public void addRule(RuleValidationStatusEn ruleValidationStatusEn, CrgRules fromCpRule) {
// new rule
        addValidation2Array(ruleValidationStatusEn, fromCpRule.getCrgrIdentifier());
    }

    /**
     *
     * @param ruleValidationStatusEn Flag, which marks the difference
     * @param oldRule rule which was found in DB
     * @param in4message information from the imported rule, which differs from
     * the rule found in DB
     */
    public void addRule(RuleValidationStatusEn ruleValidationStatusEn, CrgRules oldRule, String in4message) {
// SAME_RULE_FOUND        
        rulesImportStatus.addRuleImportResult(new RuleHeader4ValidationLog(oldRule, (in4message == null || in4message.isEmpty()) ? null : ruleValidationStatusEn.getTranslation() + ":" + in4message), ruleValidationStatusEn);
        addValidation2Array(ruleValidationStatusEn, oldRule.getCrgrIdentifier());
    }

    /**
     * compares rules header fields(not logic) for Warning - Messages
     *
     * @param oldRule old rule
     * @param fromCpRule from cp rule
     */
    public void addCompareRuleHeader(CrgRules oldRule, CrgRules fromCpRule) {
        RuleValidationStatusEn cmpFlag = RuleValidationStatusEn.SAME_RULE_FOUND;
        String message = getMessageSafeCompareStrings(oldRule.getCrgrNumber(), fromCpRule.getCrgrNumber());
        if (!message.isEmpty()) {
            cmpFlag = RuleValidationStatusEn.SAME_RULE_OTHER_NUMBER;

        } else {
            message = getMessageSafeCompareStrings(oldRule.getCrgrCaption(), fromCpRule.getCrgrCaption());
            if (!message.isEmpty()) {
                cmpFlag = RuleValidationStatusEn.SAME_RULE_OTHER_CAPTION;

            } else {
                message = getMessageSafeCompareStrings(oldRule.getCrgrCategory(), fromCpRule.getCrgrCategory());
                if (!message.isEmpty()) {
                    cmpFlag = RuleValidationStatusEn.SAME_RULE_OTHER_CATEGORY;
                } else {
                    if (oldRule.getCrgrRuleErrorType() != null
                            && fromCpRule.getCrgrRuleErrorType() != null
                            && !oldRule.getCrgrRuleErrorType().equals(fromCpRule.getCrgrRuleErrorType())) {
                        cmpFlag = RuleValidationStatusEn.SAME_RULE_OTHER_ERR_TYPE;
                        message = oldRule.getCrgrRuleErrorType().getLangKey() + " : " + fromCpRule.getCrgrRuleErrorType().getLangKey();
                    } else if (oldRule.getCrgRuleTypes().getCrgtShortText() != null
                            && fromCpRule.getCrgRuleTypes() != null
                            && !oldRule.getCrgRuleTypes().getCrgtShortText().equals(fromCpRule.getCrgRuleTypes().getCrgtShortText())) {
                        cmpFlag = RuleValidationStatusEn.SAME_RULE_OTHER_RULE_TYPE;
                        message = oldRule.getCrgRuleTypes().getCrgtShortText() + " : " + fromCpRule.getCrgRuleTypes().getCrgtShortText();
                    } else {
                        if (oldRule.getCrgRuleTypes() == null
                                && fromCpRule.getCrgRuleTypes() != null) {
                            message = getMessageSafeCompareStrings(null, fromCpRule.getCrgRuleTypes().getCrgtShortText());
                            cmpFlag = RuleValidationStatusEn.SAME_RULE_OTHER_RULE_TYPE;
                        } else if (oldRule.getCrgRuleTypes() != null
                                && fromCpRule.getCrgRuleTypes() == null) {
                            message = getMessageSafeCompareStrings(oldRule.getCrgRuleTypes().getCrgtShortText(), null);
                            cmpFlag = RuleValidationStatusEn.SAME_RULE_OTHER_RULE_TYPE;

                        }
                    }

                }
            }
        }
        addRule(cmpFlag, oldRule, message);

    }

    private String getMessageSafeCompareStrings(String oldStr, String newStr) {
        if (oldStr != null && newStr != null && !oldStr.equals(newStr)) {
            return oldStr + " : " + newStr;
        }
        if ((oldStr == null || oldStr.isEmpty()) && (newStr == null || newStr.isEmpty())) {
            return "";
        }
        if ((oldStr == null || oldStr.isEmpty()) && newStr != null && !newStr.isEmpty()) {
            return "... : " + newStr;
        }
        if (oldStr != null && oldStr.isEmpty() && (newStr == null || newStr.isEmpty())) {
            return oldStr + " : ...";
        }
        return "";
    }

    /**
     * adds the names to defined arrays according to validation status
     *
     * @param ruleValidationStatusEn
     * @param name
     */
    private void addValidation2Array(RuleValidationStatusEn ruleValidationStatusEn, String name) {
        switch (ruleValidationStatusEn) {
            case NEW_RULE:
                newRules.add(name);
                break;
            case NEW_RULE_TABLE:
                newTables.add(name);
                break;
            case NEW_RULE_TYPE:
                newRuleTypes.add(name);
                break;
            case RULE_TYPE_FOUND_IN_DB:
                break;
            case SAME_RULE_OTHER_NUMBER:
                break;
            case SAME_RULE_OTHER_CAPTION:
                break;
            case SAME_RULE_OTHER_ERR_TYPE:
                break;
            case SAME_RULE_OTHER_RULE_TYPE:
                break;
            case SAME_RULE_OTHER_CATEGORY:
                break;
            case SAME_RULE_OTHER_LOGIC:
                this.changedRules.add(name);
                break;
            case SAME_TABLE_OTHER_CONTENT:
                this.changedTables.add(name);
                break;
            case NO_TABLE_FOUND:
                this.notFoundTables.add(name);
                break;
            case RULE_TYPE_NOT_FOUND:
                this.newRuleTypes.add(name);
                break;
            default:
                LOG.log(Level.WARNING, "Unknown rule validation status enum type: " + ruleValidationStatusEn);
        }
    }

    public void saveXmlString(String xmlString) {
        xmlString4Export = xmlString;
    }

    public void saveErrorXmlString(String xmlString) {
        rulesImportStatus.setErrorXml(xmlString);
    }
    public String getXmlString4Export() {
        return xmlString4Export;
    }

    public static Map<String, CrgRuleTables> getTablesMap(List<CrgRuleTables> cpxTables) {
        return getTablesMap(cpxTables, null);
    }
    
    public static Map<String, CrgRuleTables> getTablesMap(List<CrgRuleTables> cpxTables, List<CrgRuleTables> errTables) {
        Map<String, CrgRuleTables> retMap = new HashMap<>();
        for (CrgRuleTables table : cpxTables) {
            retMap.put(table.getCrgtTableName(), table);
        }
        if(errTables != null){
            for (CrgRuleTables table : errTables) {
                retMap.put(table.getCrgtTableName(), table);
            }
            
        }
        return retMap;
    }

    public static Map<String, CrgRuleTypes> getTypesMap(List<CrgRuleTypes> cpxTypes) {
        Map<String, CrgRuleTypes> retMap = new HashMap<>();
        for (CrgRuleTypes type : cpxTypes) {
            retMap.put(type.getCrgtShortText(), type);
        }
        return retMap;
    }

    public void createCollisionStatus(List<RuleExchangeError> result) {
        if (result == null || result.isEmpty()) {
            return;
        }
        for (RuleExchangeError error : result) {

            rulesImportStatus.addRuleImportResult(new RuleValidationLog(error.getXmlRuleIdent(), error.getRuleValidationStatusEn(), error.getTextFromXml()), error.getRuleValidationStatusEn());
            LOG.log(Level.SEVERE, "error: {0}", error);
        }
//        rulesImportStatus.setEndImportStatus(RuleValidationStatusEn.COLLISION);
    }

    public int getRulesYear() {
        return rulesYear;
    }

    public void setRulesYear(int rulesYear) {
        this.rulesYear = rulesYear;
    }

//    public RulesImportStatus getRulesImportStatus(RulesImportHelper imp) {
//        rulesImportStatus.setErrRuleNames(imp.getErrRuleNames());
//        rulesImportStatus.setErrTableNames(imp.getErrRuleNames());
//        return getRulesImportStatus();
//    }


}
