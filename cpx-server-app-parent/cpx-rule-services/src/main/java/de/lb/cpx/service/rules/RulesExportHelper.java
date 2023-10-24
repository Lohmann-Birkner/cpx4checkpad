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
import de.checkpoint.server.data.caseRules.DatCaseRuleAttributes;
import de.lb.cpx.server.commonDB.model.rules.CrgRule2Table;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.shared.rules.util.RulesConverter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 *
 * @author gerschmann
 */
public class RulesExportHelper {

    private static final Logger LOG = Logger.getLogger(RulesExportHelper.class.getName());

    /**
     * creates a DOM Document from the List of CrgRule Objects for export into
     * file in CP format
     *
     * @param doc document
     * @param rules rules
     * @param helper helper
     * @return document
     */
    public static synchronized Document createExportRuleFile(Document doc, List<CrgRules> rules, RulesExchangeHelper helper) {
        return createExportRuleFile(doc, rules, helper, true);
    }

    public static synchronized Document createInternalRuleFile(Document doc, List<CrgRules> rules, RulesExchangeHelper helper) {
        return createExportRuleFile(doc, rules, helper, false);
    }

    private static synchronized Document createExportRuleFile(Document doc, List<CrgRules> rules, RulesExchangeHelper helper, boolean doFull) {
        return createExportRuleFile(doc, rules, helper, doFull, null, null);
    }

    public static synchronized Document createExportRuleFile(Document doc,
            List<CrgRules> rules,
            RulesExchangeHelper helper,
            boolean doFull,
            List<CrgRuleTables> usedTables,
            List<CrgRuleTypes> usedTypes) {

        try {
            Element root = doc.createElement(DatCaseRuleAttributes.ELEMENT_CASE_RULES);

            doc.appendChild(root);

            ArrayList<CrgRuleTables> tables = new ArrayList<>();
            ArrayList<CrgRuleTypes> types = new ArrayList<>();

            List<String> typeNames = new ArrayList<>();
            for (CrgRules cpxRule : rules) {
                CRGRule cpRule = RulesConverter.getCpRule(cpxRule);
                RulesConverter.checkRoles4CpRule(cpRule, cpxRule);
                Element ele = doc.createElement(DatCaseRuleAttributes.ELEMENT_RULES);
                cpRule.getValuesByElement(ele, doc);
// will be filled for internal export only
                if (!doFull) {
                    ele.setAttribute(DatCaseRuleAttributes.ATT_RULES_ID, cpxRule.getCrgrRid());

                }
                root.appendChild(ele);
                if (usedTables == null) {
                    collectTables(tables, cpxRule, helper);
                }
                if (usedTypes == null) {
                    String type = cpxRule.getCrgRuleTypes() == null ? null : cpxRule.getCrgRuleTypes().getCrgtShortText();
                    if (type != null && typeNames.contains(type.toUpperCase())) {
                        typeNames.add(type.toUpperCase());
                        types.add(cpxRule.getCrgRuleTypes());
                    }
                }

                helper.addLoggerText(cpRule.toString());

            }
            if (doFull) {

                addRulesTables(usedTables == null ? tables : usedTables, doc, root, helper);
                addRulesTypes(usedTypes == null ? types : usedTypes, doc, root, helper);
            } else {
                helper.setRuleTables(tables);
                helper.setTypes(types);
            }
            return doc;
        } catch (DOMException ex) {
            LOG.log(Level.SEVERE, " Error on creating DOM - Document from Rules list", ex);
            return null;
        }
    }

    /**
     * creates DOM entries for rule tables
     *
     * @param tables tables
     * @param doc document
     * @param root root
     * @param helper helper
     */
    private static void addRulesTables(List<CrgRuleTables> tables, Document doc, Element root, RulesExchangeHelper helper) {
        try {

            helper.addLoggerText("tables:");
            List<String> tableNames = new ArrayList<>();
            for (CrgRuleTables table : tables) {

                Element ele = doc.createElement(DatCaseRuleAttributes.ELEMENT_EXPORT_TABLE);
                String tableName = table.getCrgtTableName();
                if (tableName != null && !tableNames.contains(tableName.toUpperCase())) {
                    tableNames.add(tableName.toUpperCase());
                    ele.setAttribute(DatCaseRuleAttributes.ATT_EXPORT_TABLE_NAME,
                            "'" + table.getCrgtTableName() + "'");
//                    if(table.getCrgtComment() != null){
//                        ele.setAttribute(DatCaseRuleAttributes.ATT_EXPORT_TABLE_NOTE, )
//                    }
                    ele.setAttribute(DatCaseRuleAttributes.ATT_EXPORT_TABLE_COMMENT, table.getCrgtComment() == null?"":  CRGRule.getXMLText(table.getCrgtComment()));
                    ele.setAttribute(DatCaseRuleAttributes.ATT_EXPORT_TABLE_CATEGORY, table.getCrgtCategory() == null?"":table.getCrgtCategory().name());
                    ele.setAttribute(DatCaseRuleAttributes.ATT_EXPORT_TABLE_YEAR, helper.getRulesYear() == 0?"":String.valueOf(helper.getRulesYear()));
                    String content = table.getCrgtContent();

                    if (content != null && !content.isEmpty()) {
                        String[] tblVals = content.split(",");
                        for (String tblVal : tblVals) {

                            Element val = doc.createElement(DatCaseRuleAttributes.ELEMENT_EXPORT_VAL);
                            Text valT = doc.createTextNode(tblVal);
                            val.appendChild(valT);
                            ele.appendChild(val);
                        }
                    }
                    root.appendChild(ele);
                }
                helper.addLoggerText(table.getCrgtTableName());
            }

        } catch (DOMException ex) {
            LOG.log(Level.SEVERE, "error on creating DOM document for rule tables", ex);
            helper.addLoggerText("error on creating DOM document for rule tables " + ex.getMessage());
        }
    }

    /**
     * collects tables for exported rules
     *
     * @param tables tables
     * @param cpxRule cpx rule
     * @param helper helper
     */
    private static void collectTables(List<CrgRuleTables> tables, CrgRules cpxRule, RulesExchangeHelper helper) {
//        try {
        Set<CrgRule2Table> r2ts = cpxRule.getCrgRule2Tables();
        if (r2ts == null || r2ts.isEmpty()) {
            return;
        }
        for (CrgRule2Table r2t : r2ts) {
            tables.add(r2t.getCrgRuleTables());
        }
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "error on collecting of tables to exported rules ", ex);
//            helper.addLoggerText("error on collecting of tables to exported rules " + ex.getMessage());
//        }
    }

    /**
     * creates dom entries for rule types
     *
     * @param types types
     * @param doc document
     * @param root root
     * @param helper helper
     */
    private static void addRulesTypes(List<CrgRuleTypes> types, Document doc, Element root, RulesExchangeHelper helper) {
        try {

            for (CrgRuleTypes type : types) {

                Element ele = doc.createElement(DatCaseRuleAttributes.ELEMENT_RULES_TYPE);
                ele.setAttribute(DatCaseRuleAttributes.ATT_RULES_TYPE_SHORT_TEXT, type.getCrgtShortText());
                ele.setAttribute(DatCaseRuleAttributes.ATT_RULES_TYPE_DISPLAY_TEXT, type.getCrgtDisplayText());
                ele.setAttribute(DatCaseRuleAttributes.ATT_RULES_TYPE_ID, type.isCrgtReadonly() ? "1" : "0");
                ele.setAttribute(DatCaseRuleAttributes.ATT_RULES_TYPE_ORGID, String.valueOf(type.getCrgtIdent()));
                root.appendChild(ele);
                helper.addLoggerText("rule type added " + type.getCrgtShortText());

            }
        } catch (DOMException ex) {
            LOG.log(Level.SEVERE, " error on creating of rule types entry for export rules ", ex);
            helper.addLoggerText(" error on creating of rule types entry for export rules " + ex.getMessage());
        }
    }

}
