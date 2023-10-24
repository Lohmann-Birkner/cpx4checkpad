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

import de.checkpoint.ruleGrouper.CRGRisk;
import de.checkpoint.ruleGrouper.CRGRule;
import de.checkpoint.ruleGrouper.data.CRGRulePool;
import de.checkpoint.ruleGrouper.data.CRGRuleTypes;
import de.checkpoint.server.data.caseRules.DatCaseRuleAttributes;
import de.checkpoint.server.xml.XMLDOMWriter; 
import de.lb.cpx.model.enums.RuleTypeEn;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.server.commonDB.model.enums.RuleTableCategoryEn;
import de.lb.cpx.server.commonDB.model.rules.CrgRule2Role;
import de.lb.cpx.server.commonDB.model.rules.CrgRule2Table;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.server.rule.services.RuleExchangeError;
import de.lb.cpx.shared.dto.rules.CpxSimpleRisk;
import de.lb.cpx.shared.lang.Lang;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * static method for converting between CRGRule and CrgRules
 *
 * @author gerschmann
 */
public class RulesConverter {

    private static final Logger LOG = Logger.getLogger(RulesConverter.class.getName());
//    private  SimpleDateFormat mSdf = new SimpleDateFormat("dd.MM.yyyy");

    private static boolean compareMessages(byte[] pOldMessage, byte[] pNewMessage) {
        //TODO: check other constellations
       return (pOldMessage== null && pNewMessage == null);
    }

    private RulesConverter() {
        //utility class needs no public constructor
    }

    /**
     * Converts CrgRules entity into CRGRule object
     *
     * @param cpxRule CrgRules entity
     * @return CRGRule Object
     */
    public static CRGRule getCpRule(CrgRules cpxRule) {
        CRGRule tmpRule = getCpRuleAsCaption(cpxRule);
        if (cpxRule.getCrgrDefinition() == null) {
            return tmpRule;
        } else {
            Element rule = getRuleDomElementFromDbString(cpxRule.getCrgrDefinition());
            if (rule == null) {
                return tmpRule;
            }

            CRGRule retRule = new CRGRule(rule, null, null, CRGRule.RULE_ANALYSE_NO_TABLES);
            return checkCaption(retRule, tmpRule);
        }
    }

    public static CRGRule getCpRuleFromRuleDefinition(CrgRules cpxRule) {
        return getCpRuleFromRuleDefinition(cpxRule, null);
    }
    public static CRGRule getCpRuleFromRuleDefinition(CrgRules cpxRule, Map<String, RuleTableCategoryEn> tableCategories) {
        CRGRule retRule = getCpRuleFromRuleDefinition(cpxRule.getCrgrDefinition() , tableCategories);
        if(retRule != null){
            
        
            if (cpxRule.getId() != 0) {
                retRule.m_rid = String.valueOf(cpxRule.getId());
            }
        }
        return retRule;
        

    }
    
    public static CRGRule getCpRuleFromRuleDefinition(byte[] pDefinition, Map<String, RuleTableCategoryEn> tableCategories) {
        if (pDefinition == null) {
            return null;
        } else {
            Element rule = getRuleDomElementFromDbString(pDefinition);
            if (rule == null) {
                return null;
            }
            if(tableCategories != null){
               getTableCategories(rule, tableCategories);
            }
            String errType = rule.getAttribute(DatCaseRuleAttributes.ATT_CAPTION_ERROR_TYPE);
            CRGRule retRule = new CRGRule(rule, null, null, CRGRule.RULE_ANALYSE_NO_TABLES);
            retRule.m_errorTypeText = errType == null ? "" : errType;
            return retRule;
        }
    
    }
    
      /**
     *  find table types from DOM Element
     * @param root
     * @param retMap
     * @return 
     */
    public static void getTableCategories(Element root, Map<String, RuleTableCategoryEn> retMap) {
        
        NodeList ruleValues = root.getElementsByTagName(DatCaseRuleAttributes.ELEMENT_RULES_VALUES);
         for(int i = 0, len =ruleValues.getLength(); i < len; i++){
            Element ele = (Element)ruleValues.item(i);
            String op = ele.getAttribute(DatCaseRuleAttributes.ATT_RULES_OPERATOR);
            if(op == null || !op.contains("@")){
                continue;
            }
            String tabName = ele.getAttribute(DatCaseRuleAttributes.ATT_RULES_VALUE);
            if(tabName == null || tabName.isEmpty()){
                continue; 
            }
            String critName = ele.getAttribute(DatCaseRuleAttributes.ATT_RULES_TYPE);
            retMap.put(tabName, RuleTableCategoryEn.getTableCategory2crit(critName));
        }
// check suggestions        
        NodeList suggValues = root.getElementsByTagName(DatCaseRuleAttributes.ELEMENT_RULES_SUGG);
        for(int i = 0, len =suggValues.getLength(); i < len; i++){
            Element ele = (Element)suggValues.item(i);
            String op = ele.getAttribute(DatCaseRuleAttributes.ATT_SUGG_OP);
            if(op == null || !op.contains("@")){
                continue;
            }
            String tabName = ele.getAttribute(DatCaseRuleAttributes.ATT_SUGG_VALUE);
            if(tabName == null || tabName.isEmpty()){
                continue; 
            }
            String critName = ele.getAttribute(DatCaseRuleAttributes.ATT_SUGG_CRIT);
            retMap.put(tabName, RuleTableCategoryEn.getTableCategory2crit(critName));

        }

    }
    


    @SuppressWarnings("unchecked")
    public static List<String> getRuleTablesFromRuleDefinition(String xmlString) {
        if (xmlString == null || xmlString.isEmpty()) {
            return new ArrayList<>();
        }
        Element rule = getRuleDomElementFromDbString(xmlString.getBytes());
        if (rule == null) {
            return null;
        }
        CRGRule cpRule = new CRGRule(rule, null, null, CRGRule.RULE_ANALYSE_NO_TABLES);
        return new ArrayList<>(cpRule.getAllRuleTables());

    }

    /**
     * Converts CrgRules entity into CRGRule object with full content for
     * Grouping Will be generated from rule definition, caption is not
     * interesting
     *
     * @param cpxRule CrgRules entity
     * @param mgr manager
     * @return CRGRule Object
     */
    public static synchronized CRGRule getCpxCpRule(CrgRules cpxRule, CpxRuleManagerIF mgr) {

        if (cpxRule.getCrgrDefinition() == null) {
            return null;
        } else {
            Element rule = getRuleDomElementFromDbString(cpxRule.getCrgrDefinition());
            if (rule == null) {
                return null;
            }
            CrgRulePools pool = cpxRule.getCrgRulePools();
            if (pool == null) {
                return null;
            }

            CRGRule retRule = new CpxCRGRule(mgr, rule, pool.getCrgplIdentifier(), null, CRGRule.RULE_ANALYSE_FULL);
// set rule roles
            Set<CrgRule2Role> rule2roles = cpxRule.getCrgRule2Roles();
            if (rule2roles == null || rule2roles.isEmpty()) {
// rule without roles cannot be used
                return null;
            }
            retRule.m_rid = cpxRule.getCrgrRid() == null ? String.valueOf(cpxRule.getId()) : cpxRule.getCrgrRid();
            long[] usedRoles = new long[rule2roles.size()];
            int ind = 0;
            for (CrgRule2Role r2r : rule2roles) {
                CdbUserRoles role = r2r.getCdbUserRoles();
                if (role == null) {
                    continue;
                }
                usedRoles[ind] = role.getId();
                ind++;
            }
            if (ind < rule2roles.size()) {
                long[] newRoles = new long[ind];
                System.arraycopy(usedRoles, 0, newRoles, 0, ind);
                usedRoles = newRoles;
            }
            if (usedRoles.length > 1) {
// sort for save       
                Arrays.sort(usedRoles);
            }
            retRule.m_roles = usedRoles;
            return retRule;
        }
    }

    public static synchronized CRGRule getCpRuleFromCpxRuleDefinition(byte[] pDefinition, String pPoolIdent, CpxRuleManagerIF mgr) {
        if (pDefinition == null) {
            return null;
        } else {
            Element rule = getRuleDomElementFromDbString(pDefinition);
            if (rule == null) {
                return null;
            }
            return new CpxCRGRule(mgr, rule, pPoolIdent, null, CRGRule.RULE_ANALYSE_FULL);
        }
    }

    /**
     * Converts CRGRule object into CrgRules entity
     *
     * @param cpRule CRGRule Object
     * @return CrgRules entity
     */
    public static CrgRules getCpxRule(CRGRule cpRule) {
        return getCpxRule(cpRule, null, false);
    }

    public static CrgRules getCpxRule(CRGRule cpRule, CrgRules cpxRule) {
        return getCpxRule(cpRule, cpxRule, false);
    }

    public static CrgRules getCpxRule(CRGRule cpRule, CrgRules cpxRule, boolean onlyRuleTag) {
        return getCpxRule(cpRule, cpxRule, onlyRuleTag, PoolTypeEn.PROD);
    }

    public static CrgRules getCpxRule(CRGRule cpRule, CrgRules cpxRule, boolean onlyRuleTag, PoolTypeEn pType) {
        return getCpxRule(cpRule, cpxRule, onlyRuleTag, pType, null, null, false);
    }

    public static CrgRules getCpxRule(CRGRule cpRule,
            CrgRules cpxRule,
            boolean onlyRuleTag,
            PoolTypeEn pType,
            CrgRuleTypes cpxType,
            Map<String, CrgRuleTables> ruleTables, boolean headerOnly) {
        if (cpxRule == null) {
            cpxRule = CrgRules.getTypeInstance(pType);
        }
        if (cpRule == null) {
            return null;
        }
        cpxRule.setCrgrRid(cpRule.m_rid);
        cpxRule.setCrgrCaption(cpRule.m_caption);
        cpxRule.setCrgrNumber(cpRule.m_number == null ? "?" : cpRule.m_number);
        cpxRule.setCrgrCategory(cpRule.m_text);
        cpxRule.setCrgrIdentifier(cpRule.m_ruleNumber);
//        setCrgRulePools(rulePool2IdentYear);
        cpxRule.setCrgrValidFrom(cpRule.getRuleValidFrom());
        cpxRule.setCrgrValidTo(cpRule.getRuleValidTo());
        cpxRule.setCrgrRuleErrorType(RuleTypeEn.get2RuleType(cpRule.m_type));
        if (!headerOnly) {
            cpxRule.setCrgrDefinition(getRuleContent(cpRule, onlyRuleTag, cpxType, ruleTables));
        }
        cpxRule.setCrgrNote(cpRule.m_notice);
        cpxRule.setCrgrSuggText(cpRule.m_suggestion);
        cpxRule.setCrgrFeeGroup(getLongArrayAsCommaString(cpRule.m_feeGroups));
        cpxRule.setCrgrMessage(cpRule.getErrorMessage());
        return cpxRule;
    }

    private static String getLongArrayAsCommaString(long[] longArray) {
        StringBuilder sb = new StringBuilder();
        if (longArray != null) {
            for (long l : longArray) {
                sb.append(l).append(",");
            }
        }
        String ret = sb.toString();
        if (ret.endsWith(",")) {
            ret = ret.substring(0, ret.length() - 1);
        }
        return ret;
    }

    /**
     * mappt CPX - rule type to Checkpoint rule type
     *
     * @param cpxType cpx rule type
     * @return cp rule type
     */
    public static CRGRuleTypes getCpType(CrgRuleTypes cpxType) {
        CRGRuleTypes type = new CRGRuleTypes();
        type.crgrt_displaytext = cpxType.getCrgtDisplayText();
        type.crgrt_shorttext = cpxType.getCrgtShortText();
        type.crgrt_ident = cpxType.getCrgtIdent();
        type.crgrt_type = cpxType.isCrgtReadonly() ? 0 : 1;
        return type;
    }

    /**
     * mappt Checkpoint - rule type to CPX rule type
     *
     * @param cpType cp rule type
     * @return cpx rule type
     */
    public static CrgRuleTypes getCpxRuleType(CRGRuleTypes cpType) {
        return getCpxRuleType(cpType, PoolTypeEn.PROD);
    }

    public static CrgRuleTypes getCpxRuleType(CRGRuleTypes cpType, PoolTypeEn poolTypeEn) {
        CrgRuleTypes cpxType = CrgRuleTypes.getTypeInstance(poolTypeEn);
        return updatecpxTypeFromCpType(cpType, cpxType, poolTypeEn);
    }

    public static CrgRuleTypes updatecpxTypeFromCpType(CRGRuleTypes cpType, CrgRuleTypes cpxType) {
        return updatecpxTypeFromCpType(cpType, cpxType, PoolTypeEn.PROD);
    }

    public static CrgRuleTypes updatecpxTypeFromCpType(CRGRuleTypes cpType, CrgRuleTypes cpxType, PoolTypeEn poolTypeEn) {

        if (cpxType == null) {
            cpxType = CrgRuleTypes.getTypeInstance(poolTypeEn);
        }
        cpxType.setCrgtShortText("");
        cpxType.setCrgtDisplayText("");
        if (cpType == null) {
            return cpxType;
        }
        cpxType.setCrgtIdent(cpType.crgrt_ident);
        cpxType.setCrgtReadonly(cpType.crgrt_type == 1);
        cpxType.setCrgtShortText(cpType.crgrt_shorttext == null ? "" : cpType.crgrt_shorttext);
        cpxType.setCrgtDisplayText(cpType.crgrt_displaytext == null ? "" : cpType.crgrt_displaytext);
        return cpxType;
    }

    /**
     * converts CRGRule object into XML - String of UTF-16 format
     *
     * @param rule CRGRule object
     * @param onlyRuleTag only rule tag?
     * @param cpxType type
     * @param ruleTables rules table
     * @return xml - String
     */
    public static byte[] getRuleContent(CRGRule rule, boolean onlyRuleTag,
            CrgRuleTypes cpxType,
            Map<String, CrgRuleTables> ruleTables) {
        try {
            Document doc = getDocument4CpRule(rule, onlyRuleTag, cpxType, ruleTables);
            return getXMLByteArray(doc, "UTF-16").getBytes();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "cannot create rule content", ex);
        }
        return null;

    }

    private static Document getDocument4CpRule(CRGRule rule, boolean onlyRuleTag,
            CrgRuleTypes cpxType,
            Map<String, CrgRuleTables> ruleTables) throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        Element ele;
        if (onlyRuleTag) {
            ele = doc.createElement(DatCaseRuleAttributes.ELEMENT_RULES);
            ele.setAttribute(DatCaseRuleAttributes.ATT_RULES_ID, rule.m_rid);
            rule.getValuesByElement(ele, doc);
            replaceRuleTypesAndTableNamesWithIds(ele, cpxType, ruleTables);
            doc.appendChild(ele);
        } else {
            Element root = doc.createElement(DatCaseRuleAttributes.ELEMENT_CASE_RULES);
            doc.appendChild(root);
            ele = doc.createElement(DatCaseRuleAttributes.ELEMENT_RULES);
            ele.setAttribute(DatCaseRuleAttributes.ATT_RULES_ID, rule.m_rid);
            rule.getValuesByElement(ele, doc);
            replaceRuleTypesAndTableNamesWithIds(ele, cpxType, ruleTables);
            root.appendChild(ele);

        }
        // if the rule is empty, add empty <rules_element nested="false">
        NodeList nl = ele.getElementsByTagName(DatCaseRuleAttributes.ELEMENT_RULES_ELEMENT);
        if (nl == null || nl.getLength() == 0) {
            ele.appendChild(doc.createElement(DatCaseRuleAttributes.ELEMENT_RULES_ELEMENT));
        }
        return doc;
    }

    public static CRGRule getCpRuleWithTablesIds(CRGRule cpRule, CrgRuleTypes cpxType, Map<String, CrgRuleTables> ruleTables) {
        try {
            Document doc = getDocument4CpRule(cpRule, true, cpxType, ruleTables);
            Element el = doc.getDocumentElement();
            return new CRGRule(el, cpRule.getRuleIdentifier(), null, CRGRule.RULE_ANALYSE_NO_TABLES);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "cannot create rule content", ex);
        }
        return null;

    }

    /**
     * Converts DOM - Document into String
     *
     * @param doc DOM - Document
     * @param charcode String format
     * @return converted into String Document
     * @throws Exception
     */
    private static String getXMLByteArray(Document doc, String charcode) throws Exception {

//        XMLDOMWriter writer = new XMLDOMWriter(false);
        java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(os, charcode));
        XMLDOMWriter.setWriterEncoding(charcode);
        XMLDOMWriter.print(doc, pw, false);
        return new String(os.toByteArray());

    }

    /**
     * Converts the rule definition, which is saved in the DB to DOM Element
     * which can be used for creation of the CRGRule object
     *
     * @param dbString serialized UTF-16 String from DB
     * @return DOM Element of rule - type
     */
    public static Element getRuleDomElementFromDbString(byte[] dbString) {
        try {
            if (dbString != null) {
                InputStream in = new ByteArrayInputStream(dbString);

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(in);
                if (doc != null) {
                    NodeList nl = doc.getElementsByTagName(DatCaseRuleAttributes.ELEMENT_RULES);

                    if (nl != null && nl.getLength() > 0) {
                        return (Element) nl.item(0);
                    }

                }
            }
        } catch (IOException | ParserConfigurationException | SAXException ex) {
            LOG.log(Level.SEVERE, "cannot create rule content", ex);
        }
        return null;
    }

    private static Element replaceRuleTypesAndTableNamesWithIds(Element ruleElement, CrgRuleTypes cpxType, Map<String, CrgRuleTables> ruleTables) {

        if (ruleElement == null) {
            return ruleElement;
        }
        if (cpxType != null) {

            Element caption = null;
            if (ruleElement.getNodeName().equals(DatCaseRuleAttributes.ELEMENT_RULES)) {
                caption = ruleElement;
            } else {
                NodeList nl = ruleElement.getElementsByTagName(DatCaseRuleAttributes.ELEMENT_RULES);
                if (nl != null && nl.getLength() > 0) {
                    caption = (Element) nl.item(0);

                }
            }
            if (caption != null) {
                caption.setAttribute(DatCaseRuleAttributes.ATT_CAPTION_ERROR_TYPE, String.valueOf(cpxType.getId()));
            }
        }
        // we have to check all values because of quotas
            NodeList nl = ruleElement.getElementsByTagName(DatCaseRuleAttributes.ELEMENT_RULES_VALUES);
            if (nl != null && nl.getLength() > 0) {
                for (int i = 0, len = nl.getLength(); i < len; i++) {
                    Element val = (Element) nl.item(i);
                    String op = val.getAttribute(DatCaseRuleAttributes.ATT_RULES_OPERATOR);
                    String value = val.getAttribute(DatCaseRuleAttributes.ATT_RULES_VALUE);
                    if(!value.isEmpty()){
                        value = value.replaceAll("'", "");
                    }
                    if (ruleTables != null) {
                        if (op != null && !op.isEmpty() && op.indexOf('@') >= 0) {


                            CrgRuleTables table = ruleTables.get(value);
                            if (table != null) {
                                value = String.valueOf(table.getId());
                            }
                        }
                    }
                    val.setAttribute(DatCaseRuleAttributes.ATT_RULES_VALUE, String.valueOf(value));
                }
            }
// now suggestions sugg values have no quotas, so we check usage of tables only
         if (ruleTables != null) {
            nl = ruleElement.getElementsByTagName(DatCaseRuleAttributes.ELEMENT_RULES_SUGG);
            if (nl != null && nl.getLength() > 0) {
                for (int i = 0, len = nl.getLength(); i < len; i++) {
                    Element val = (Element) nl.item(i);
                    String op = val.getAttribute(DatCaseRuleAttributes.ATT_SUGG_OP);
                    if (op != null && !op.isEmpty() && op.indexOf('@') >= 0) {
                        String tabName = val.getAttribute(DatCaseRuleAttributes.ATT_SUGG_VALUE);
                        tabName = tabName.replaceAll("'", "");
                        CrgRuleTables table = ruleTables.get(tabName);
                        if (table != null) {
                            val.setAttribute(DatCaseRuleAttributes.ATT_SUGG_VALUE, String.valueOf(table.getId()));
                        }

                    }
                    op = val.getAttribute(DatCaseRuleAttributes.ATT_SUGG_CONDITION_OP);
                    if (op != null && !op.isEmpty() && op.indexOf('@') >= 0) {
                        String tabName = val.getAttribute(DatCaseRuleAttributes.ATT_SUGG_CONDITION_VALUE);
                        tabName = tabName.replaceAll("'", "");
                        CrgRuleTables table = ruleTables.get(tabName);
                        if (table != null) {
                            val.setAttribute(DatCaseRuleAttributes.ATT_SUGG_CONDITION_VALUE, String.valueOf(table.getId()));
                        }

                    }

                }

            }
        }
        
        return ruleElement;
    }

    /**
     * creates emty rule with caption only from help - fields of the cpx rule
     */
    private static CRGRule getCpRuleAsCaption(CrgRules rule) {
        CRGRule cpRule = new CRGRule();
        cpRule.m_rid = rule.getCrgrRid();
        cpRule.m_number = rule.getCrgrNumber();
        cpRule.m_ruleNumber = rule.getCrgrIdentifier();
        cpRule.m_ruleText = rule.getCrgrCaption();
        cpRule.m_text = rule.getCrgrCategory();
        cpRule.m_suggestion = rule.getCrgrSuggText();
        cpRule.m_adt = rule.getCrgrValidFrom();
        cpRule.m_ddt = rule.getCrgrValidTo();
        cpRule.m_validFromTime = rule.getCrgrValidFrom() == null ? 0 : rule.getCrgrValidFrom().getTime();
        cpRule.m_validToTime = rule.getCrgrValidTo() == null ? 0 : rule.getCrgrValidTo().getTime();
        cpRule.m_errorTypeText = rule.getCrgRuleTypes() == null ? "" : rule.getCrgRuleTypes().getCrgtShortText();
        cpRule.m_year = rule.getCrgRulePools() == null ? 0 : rule.getCrgRulePools().getCrgplPoolYear();
//        cpRule.m_typeText = rule.getCrgrRuleErrorType() == null?RuleTypeEn.STATE_NO.toString():rule.getCrgrRuleErrorType().toString().toLowerCase();
        cpRule.m_notice = rule.getCrgrNote();
        cpRule.m_typeText = rule.getCrgrRuleErrorType() == null
                ? RuleTypeEn.STATE_NO.getInternalKey()//.getTranslation().getValue()
                : rule.getCrgrRuleErrorType().getInternalKey();
        return cpRule;
    }

    /**
     * converts set of rules from pool to HashMap for convenient handling
     *
     * @param rules rules
     * @return map of cpx rules
     */
    public static Map<String, CrgRules> getCpxRulesFromPoolAsMap(Set<CrgRules> rules) {
//        try {
        Map<String, CrgRules> rulesMap = new HashMap<>();
        if (rules == null || rules.isEmpty()) {
            return rulesMap;
        }
        for (CrgRules rule : rules) {
            String ident = getIdentConstantPart(rule.getCrgrIdentifier());
            if (ident != null && !ident.isEmpty()) {
                rulesMap.put(ident, rule);
            }
        }
        return rulesMap;
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "error on converting of rules set into hashMap", ex);
//            return null;
//        }
    }

    /**
     * Rules Ident nr contains of 3 parts pool_rid_timestamp. rid_timestamp has
     * to be unique
     *
     * @param ident identifier
     * @return whatever(?)
     */
    public static String getIdentConstantPart(String ident) {
        if (ident == null) {
            return null;
        }
        String[] parts = ident.split("_");
        if (parts.length < 3) {
            return null;
        }
        return parts[parts.length-2] + "_" + parts[parts.length-1];
    }

    /**
     * creates CrgRuleTables object on the CP data
     *
     * @param tableName table name
     * @param pCpTable table
     * @return crg rules tables
     */
    public static CrgRuleTables getCpxRuleTable(String tableName, CpTable pCpTable) {
        return getCpxRuleTable(tableName, pCpTable, PoolTypeEn.PROD);
    }

    public static CrgRuleTables getCpxRuleTable(String tableName, CpTable cpTable, PoolTypeEn pType) {
        CrgRuleTables table = CrgRuleTables.getTypeInstance(pType);
        table.setCrgtTableName(tableName);
        table.setCrgtContent(cpTable.getContent());
        table.setCrgtComment(cpTable.getComment());
        table.setCrgtCategory(cpTable.getCategory());
        table.setCrgtMessage(cpTable.getMessage());
        return table;
    }

    /**
     * compares the content of rule tables designed as Strings with comma
     * separated values
     *
     * @param tabContentOld first table content
     * @param tabContentNew secon table content
     * @return true wenn equals
     */
    public static boolean tableContentEquals(String tabContentOld, String tabContentNew) {
//        try {
         String tmpOld = tabContentOld == null?"":tabContentOld;
         String tmpNew = tabContentNew == null?"":tabContentNew;

        if (tmpOld.equals(tmpNew)) {
            return true;
        }

        String[] tabOld = tmpOld.trim().split(", ");
        String[] tabNew = tmpNew.trim().split(", ");
        Arrays.sort(tabOld);
        Arrays.sort(tabNew);
        tabContentOld = Arrays.toString(tabOld);
        tabContentNew = Arrays.toString(tabNew);
        return tabContentOld.equalsIgnoreCase(tabContentNew);
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "error on compare of rule tables content:" + tabContentOld + " " + tabContentNew, ex);
//            return false;
//
//        }
    }
    /**
     * compares table conmments
     * @param pCommentOld old comment
     * @param pCommentNew new comment
     * @return result of equal
     */
    public static boolean tableCommentEquals(String pCommentOld, String pCommentNew) {
         String tmpOld = pCommentOld == null?"":pCommentOld;
         String tmpNew = pCommentNew == null?"":pCommentNew;
         return tmpOld.equals(tmpNew);
    }


    public static void checkRoles4CpRule(CRGRule cpRule, CrgRules cpxRule) {
        if (cpRule != null && cpxRule != null) {
            Set<CrgRule2Role> r2rr = cpxRule.getCrgRule2Roles();

            if (r2rr != null && !r2rr.isEmpty()) {
                long[] roles = new long[r2rr.size()];
                int i = 0;
                for (CrgRule2Role r2r : r2rr) {
                    if (r2r.getCdbUserRoles() != null) {
                        roles[i] = r2r.getCdbUserRoles().getId();
                        i++;
                    }
                }
                if (i < roles.length) {
                    long[] newRoles = new long[i];
                    System.arraycopy(roles, 0, newRoles, 0, i);
                    cpRule.m_roles = newRoles;
                } else {
                    cpRule.m_roles = roles;
                }
            }
        }
    }

    /**
     * transfer the caption values from tmpRule into retRule
     *
     */
    private static CRGRule checkCaption(CRGRule retRule, CRGRule tmpRule) {

        retRule.m_rid = tmpRule.m_rid;
        retRule.m_number = tmpRule.m_number;
        retRule.m_ruleNumber = tmpRule.m_ruleNumber;
        retRule.m_ruleText = tmpRule.m_ruleText;
        retRule.m_text = tmpRule.m_text;
        retRule.m_suggestion = tmpRule.m_suggestion;
        retRule.m_validFromTime = tmpRule.m_validFromTime;
        retRule.m_validToTime = tmpRule.m_validToTime;
        retRule.m_errorTypeText = tmpRule.m_errorTypeText;
        retRule.m_year = tmpRule.m_year;
        retRule.m_typeText = tmpRule.m_typeText;// 
        retRule.m_notice = tmpRule.m_notice;
        return retRule;
    }

    public static List<Long> getRuleTableIds(CRGRule cpRule) throws NumberFormatException {
        List<Long> tableIds = new ArrayList<>();
        @SuppressWarnings("unchecked")
        List<String> tables = new ArrayList<>(cpRule.getTableNames());
        for (String table : tables) {
            tableIds.add(Long.parseLong(table));

        }
        return tableIds;
    }

    public static Long getRuleTypeId(CRGRule cpRule) throws NumberFormatException {
        String ruleType = cpRule.m_errorTypeText;
        return Long.parseLong(ruleType);
    }

    /**
     * from rules definitions, that are saved in CrgRulesObjects creates an
     * export String which is compatible with CP
     *
     * @param rule rule to convert
     * @param ruleType id as String-&gt;ruleType
     * @param tables Map id as String-&gt;table
     * @param pPoolType pool type
     * @param usedTables list where i gather used tables
     * @return result new transient rule
     */
    public static CrgRules getRule4Export(CrgRules rule, PoolTypeEn pPoolType, CrgRuleTypes ruleType, Map<String, CrgRuleTables> tables, List<CrgRuleTables> usedTables) {
        return getRule4Export( rule,  pPoolType,  ruleType, 
             tables,
            usedTables,
            new Long[0]);
    }
    
    /**
     * from rules definitions, that are saved in CrgRulesObjects creates an
     * export String which is compatible with CP
     *
     * @param rule rule to convert
     * @param ruleType id as String-&gt;ruleType
     * @param tables Map id as String-&gt;table
     * @param pPoolType pool type
     * @param usedTables list where i gather used tables
     * @param pUserRoles roles for exported rules, for later, when we have the role concept
     * @return result new transient rule
     */
    public static CrgRules getRule4Export(CrgRules rule, PoolTypeEn pPoolType, CrgRuleTypes ruleType, 
            Map<String, CrgRuleTables> tables,
            List<CrgRuleTables> usedTables,
            Long[] pUserRoles) {
        Element ruleElement = getRuleDomElementFromDbString(rule.getCrgrDefinition());
        if (ruleElement == null) {
            return null;
        }
// replace typeId

        Element caption = null;
        if (ruleElement.getTagName().equals(DatCaseRuleAttributes.ELEMENT_RULES)) {
            caption = ruleElement;
        } else {
            NodeList nl = ruleElement.getElementsByTagName(DatCaseRuleAttributes.ELEMENT_RULES);
            if (nl != null && nl.getLength() > 0) {
                caption = (Element) nl.item(0);

            }
        }
        if (caption != null) {
            ruleElement.setAttribute(DatCaseRuleAttributes.ATT_CAPTION_ERROR_TYPE, ruleType.getCrgtShortText());
        }

// replace tableIds
        NodeList nl = ruleElement.getElementsByTagName(DatCaseRuleAttributes.ELEMENT_RULES_VALUES);
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0, len = nl.getLength(); i < len; i++) {
                Element val = (Element) nl.item(i);
                String crit = val.getAttribute(DatCaseRuleAttributes.ATT_RULES_TYPE);
                String op = val.getAttribute(DatCaseRuleAttributes.ATT_RULES_OPERATOR);
                if (op != null && !op.isEmpty() && op.contains("@")) {
                    String tabName = val.getAttribute(DatCaseRuleAttributes.ATT_RULES_VALUE);
                    CrgRuleTables table = tables.get(tabName);
                    if (table != null) {
                        if(table.getCrgtCategory() == null){
                            table.setCrgtCategory(RuleTableCategoryEn.getTableCategory2crit(crit));
                        }
                        val.setAttribute(DatCaseRuleAttributes.ATT_RULES_VALUE, "'" + table.getCrgtTableName() + "'");
                        if (!usedTables.contains(table)) {
                            usedTables.add(table);
                        }
                    }
                }
            }
        }
// now suggestions
        nl = ruleElement.getElementsByTagName(DatCaseRuleAttributes.ELEMENT_RULES_SUGG);
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0, len = nl.getLength(); i < len; i++) {
                Element val = (Element) nl.item(i);
                String crit = val.getAttribute(DatCaseRuleAttributes.ATT_SUGG_CRIT);
                String op = val.getAttribute(DatCaseRuleAttributes.ATT_SUGG_OP);
                if (op != null && !op.isEmpty() && op.contains("@")) {
                    String tabName = val.getAttribute(DatCaseRuleAttributes.ATT_SUGG_VALUE);
                    CrgRuleTables table = tables.get(tabName);
                    if (table != null) {
                        val.setAttribute(DatCaseRuleAttributes.ATT_SUGG_VALUE, table.getCrgtTableName());
                         if(table.getCrgtCategory() == null){
                            table.setCrgtCategory(RuleTableCategoryEn.getTableCategory2crit(crit));
                        }
                       if (!usedTables.contains(table)) {
                            usedTables.add(table);
                        }

                    }

                }
                op = val.getAttribute(DatCaseRuleAttributes.ATT_SUGG_CONDITION_OP);
                if (op != null && !op.isEmpty() && op.indexOf('@') >= 0) {
                    String tabName = val.getAttribute(DatCaseRuleAttributes.ATT_SUGG_CONDITION_VALUE);
                    tabName = tabName.replaceAll("'", "");
                    CrgRuleTables table = tables.get(tabName);
                    
                    if (table != null) {
                        if(table.getCrgtCategory() == null){
                            table.setCrgtCategory(RuleTableCategoryEn.getTableCategory2crit(crit));
                        }
                        if (!usedTables.contains(table)) {
                            usedTables.add(table);
                        }
                        val.setAttribute(DatCaseRuleAttributes.ATT_SUGG_CONDITION_VALUE, table.getCrgtTableName());
                    }

                }
            }
        }
        CRGRule cpRule = new CRGRule(ruleElement, null, null, CRGRule.RULE_ANALYSE_NO_TABLES);
// for later, when we developed the role concept 
//        cpRule.m_roles = new long[pUserRoles.length];
//        if(pUserRoles.length > 0){
//            System.arraycopy(pUserRoles, 0, cpRule.m_roles, 0, pUserRoles.length);
//        }
        checkRoles4CpRule(cpRule, rule);
        return getCpxRule(cpRule, null, true, pPoolType);
    }

    /**
     * copies all fields from rule to destRule
     *
     * @param srcRule source rule
     * @param destRule destination rule
     *
     * @return crg rule
     */
    public static CrgRules copyCrgRuleFileds(CrgRules srcRule, CrgRules destRule) {
        if (srcRule == null || destRule == null) {
            return destRule;
        }        
        destRule.setCrgRulePools(srcRule.getCrgRulePools());
        CrgRuleTypes type = srcRule.getCrgRuleTypes();
        destRule.setCrgRuleTypes(type);
        type.getCrgRuleses().add(destRule);
        destRule.setCrgrCaption(srcRule.getCrgrCaption());
        destRule.setCrgrCategory(srcRule.getCrgrCategory());
        destRule.setCrgrDefinition(srcRule.getCrgrDefinition());
        destRule.setCrgrNote(srcRule.getCrgrNote());
        destRule.setCrgrNumber("(" + srcRule.getCrgrNumber() + "*)");
        destRule.setCrgrSuggText(srcRule.getCrgrSuggText());
        destRule.setCrgrSuggestion(srcRule.getCrgrSuggestion());
        destRule.setCrgrValidFrom(srcRule.getCrgrValidFrom());
        destRule.setCrgrValidTo(srcRule.getCrgrValidTo());
        destRule.setCrgrRuleErrorType(srcRule.getCrgrRuleErrorType());
        destRule.setCrgrMessage(srcRule.getCrgrMessage());
        destRule.setIsReadOnlyFl(false);
        destRule.setCrgrFeeGroup(srcRule.getCrgrFeeGroup());
        return destRule;
    }

    public static CrgRules copyR2rAndr2tRelations(CrgRules srcRule, CrgRules destRule, PoolTypeEn pPoolType, Long creationUserId) {
        if (srcRule == null || destRule == null) {
            return destRule;
        }
// r2r
// rule to rolle
        Set<CrgRule2Role> r2rs = srcRule.getCrgRule2Roles();
        if (r2rs != null) {
            for (CrgRule2Role r2r : r2rs) {
                CrgRule2Role destR2r = CrgRule2Role.getTypeInstance(pPoolType);
                destR2r.setCreationUser(creationUserId);
                destR2r.setCreationDate(new Date());
                destR2r.setCrgRules(destRule);
                destR2r.setCdbUserRoles(r2r.getCdbUserRoles());
                destRule.getCrgRule2Roles().add(destR2r);
            }
        }
        // r2t rule to table
        Set<CrgRule2Table> r2ts = srcRule.getCrgRule2Tables();
        if (r2ts != null) {
            for (CrgRule2Table r2t : r2ts) {
                CrgRule2Table destR2t = CrgRule2Table.getTypeInstance(pPoolType);
                destR2t.setCreationUser(creationUserId);
                destR2t.setCreationDate(new Date());
                destR2t.setCrgRules(destRule);
                destR2t.setCrgRuleTables(r2t.getCrgRuleTables());
                destRule.getCrgRule2Tables().add(destR2t);
            }
        }

        return destRule;
    }

    public static boolean compareCpxRules(CrgRules oldRule, CrgRules newRule, String poolIdent, PoolTypeEn pPoolType, List<RuleExchangeError> errors) {
//   SAME_RULE_OTHER_LOGIC(16, Lang.SAME_RULE_OTHER_LOGIC, ErrPrioEn.SEVERE), // Regel mit gleichen Ident und abweichenden Logik in dem Pool vorhanden
        CRGRule cpxOld = new CRGRule(RulesConverter.getRuleDomElementFromDbString(oldRule.getCrgrDefinition()), poolIdent, null, CRGRule.RULE_ANALYSE_NO_TABLES);
        CRGRule cpxNew = new CRGRule(RulesConverter.getRuleDomElementFromDbString(newRule.getCrgrDefinition()), poolIdent, null, CRGRule.RULE_ANALYSE_NO_TABLES);
        if (!cpxOld.toString().equalsIgnoreCase(cpxNew.toString())) {
            errors.add(new RuleExchangeError(oldRule.getId(), RuleValidationStatusEn.SAME_RULE_OTHER_LOGIC, pPoolType, cpxNew.toString()));
            return false;
        }
//   SAME_RULE_OTHER_LOGIC(16, Lang.SAME_RULE_OTHER_LOGIC, ErrPrioEn.SEVERE), // Regel mit gleichen Ident und abweichenden Logik in dem Pool vorhanden
        if (!getAllSuggLogicAsString(cpxOld).equalsIgnoreCase(getAllSuggLogicAsString(cpxNew))) {
            errors.add(new RuleExchangeError(oldRule.getId(), RuleValidationStatusEn.SAME_RULE_OTHER_SUGG_LOGIC, pPoolType, getAllSuggLogicAsString(cpxNew)));
            return false;
        }
//    SAME_RULE_OTHER_NUMBER(8, Lang.SAME_RULE_OTHER_NUMBER, ErrPrioEn.WARNING), // Regel mit gleichen Ident und anderem Nummer in dem Pool vorhanden
        if (!oldRule.getCrgrNumber().equalsIgnoreCase(newRule.getCrgrNumber())) {
            errors.add(new RuleExchangeError(oldRule.getId(), RuleValidationStatusEn.SAME_RULE_OTHER_NUMBER, pPoolType, newRule.getCrgrNumber()));
            return false;
        }
//    SAME_RULE_OTHER_CAPTION(9, Lang.SAME_RULE_OTHER_CAPTION, ErrPrioEn.WARNING), // Regel mit gleichen Ident und abweichender Bezeichnung in dem Pool vorhanden
        if (!oldRule.getCrgrCaption().equalsIgnoreCase(newRule.getCrgrCaption())) {
            errors.add(new RuleExchangeError(oldRule.getId(), RuleValidationStatusEn.SAME_RULE_OTHER_CAPTION, pPoolType, newRule.getCrgrCaption()));
            return false;
        }
//    SAME_RULE_OTHER_ERR_TYPE(10, Lang.SAME_RULE_OTHER_ERR_TYPE, ErrPrioEn.WARNING), // Regel mit gleichen Ident und anderem Regelverletzungstyp in dem Pool vorhanden
        if (!cpxOld.m_typeText.equals(cpxNew.m_typeText)) {
            errors.add(new RuleExchangeError(oldRule.getId(), RuleValidationStatusEn.SAME_RULE_OTHER_ERR_TYPE, pPoolType, newRule.getCrgrRuleErrorType().name()));
            return false;
        }
//    SAME_RULE_OTHER_RULE_TYPE(11, Lang.SAME_RULE_OTHER_RULE_TYPE, ErrPrioEn.WARNING), // Regel mit gleichen Ident und anderem Regeltyp in dem Pool vorhanden
        if (!cpxOld.m_errorTypeText.equals(cpxNew.m_errorTypeText)) {
            errors.add(new RuleExchangeError(oldRule.getId(), RuleValidationStatusEn.SAME_RULE_OTHER_RULE_TYPE, pPoolType, cpxNew.m_errorTypeText));
            return false;
        }
//    SAME_RULE_OTHER_CATEGORY(12, Lang.SAME_RULE_OTHER_CATEGORY, ErrPrioEn.WARNING), // Regel mit gleichen Ident und anderer Kategorie in dem Pool vorhanden
        if (!oldRule.getCrgrCategory().equalsIgnoreCase(newRule.getCrgrCategory())) {
            errors.add(new RuleExchangeError(oldRule.getId(), RuleValidationStatusEn.SAME_RULE_OTHER_CATEGORY, pPoolType, newRule.getCrgrCategory()));
            return false;

        }
        if(!compareMessages(oldRule.getCrgrMessage(), newRule.getCrgrMessage())){
            errors.add(new RuleExchangeError(oldRule.getId(), RuleValidationStatusEn.SAME_RULE_DIFFERENT_MESSAGE, pPoolType, "Different Messages"));
            return false;
        
    }
// compare rules messages
        return true;
    }

    private static String getAllSuggLogicAsString(CRGRule rule) {
        @SuppressWarnings("unchecked")
        List<String> suggs = rule.m_lstSuggs == null ? new ArrayList<>() : new ArrayList<>(rule.m_lstSuggs);
        StringBuilder builder = new StringBuilder();
        suggs.forEach((sugg) -> {
            builder.append(sugg).append(";");
        });
        return builder.toString();
    }

    public static CrgRulePools getCpxRulePool(CRGRulePool pCpPool, PoolTypeEn pType) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        CrgRulePools cpxPool = CrgRulePools.getTypeInstance(PoolTypeEn.PROD);
        cpxPool.setId(pCpPool.id);
        cpxPool.setCrgplPoolYear(pCpPool.crgpl_year);
        cpxPool.setCrgplIdentifier(String.valueOf(pCpPool.crgpl_year));
        cal.set(pCpPool.crgpl_year, 0, 1);
        cpxPool.setCrgplFrom(cal.getTime());
        cal.set(pCpPool.crgpl_year, 11, 31);
        cpxPool.setCrgplTo(cal.getTime());

        return cpxPool;
    }

    public static CrgRulePools getCpxRulePool4Year(int year, PoolTypeEn pType) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        CrgRulePools cpxPool = CrgRulePools.getTypeInstance(PoolTypeEn.PROD);
        cpxPool.setId(year);
        cpxPool.setCrgplPoolYear(year);
        cpxPool.setCrgplIdentifier(String.valueOf(year));
        cal.set(year, 0, 1);
        cpxPool.setCrgplFrom(cal.getTime());
        cal.set(year, 11, 31);
        cpxPool.setCrgplTo(cal.getTime());

        return cpxPool;
    }

        public static  CpxSimpleRisk createCpxRisksFromCpRule( CRGRule currentRule){

        List <CRGRisk> risks = currentRule.getRuleRisks();
        if(risks != null && !risks.isEmpty()){
        CRGRisk cpRisk = risks.get(0);
            return new CpxSimpleRisk(cpRisk.getRiskName(), cpRisk.getRiskComment(), 
                    cpRisk.getRiskDefaultWasteValue(), cpRisk.getRiskWastePercentValue(), 
                    cpRisk.getRiskAuditPercentValue(),
            !currentRule.m_hasSuggestions);

        
        }
        return null;
    }
        
        public static  CpxSimpleRisk createCpxRisksFromCpxRule( CrgRules currentRule){
            CRGRule cpRule = getCpRule(currentRule);
            return createCpxRisksFromCpRule(cpRule);
        }
    
        /**
         * used to find out the year for rule tables by import of rules from xml, which was exported from cp-old
         * 
     * @param root root
     * @return 
         **/
    public static int getYearFromFirstRule(Element root) {
        NodeList rules = root.getElementsByTagName(DatCaseRuleAttributes.ELEMENT_RULES);
        // in cp old all rules in one export file belong to the same pool, same year
        if(rules.getLength() > 0){
            Element rule = (Element)rules.item(0);
            String from = rule.getAttribute(DatCaseRuleAttributes.ATT_CAPTION_FROM);
            if(from != null && !from.isEmpty()){
               if(from.length() == 10){
                   Date dt = Lang.toDate(from);
                   if(dt != null){ 
                       return Lang.toYear(dt);
                   
                   }
               }
            }
        }
        return 0;
    }


}
