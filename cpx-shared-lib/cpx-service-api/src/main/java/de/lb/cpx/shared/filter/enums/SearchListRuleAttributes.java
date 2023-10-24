/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  hasse - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.filter.enums;

import de.lb.cpx.model.enums.RuleTypeEn;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author hasse
 */
public class SearchListRuleAttributes extends SearchListAttributes {

    private static SearchListRuleAttributes instance = null;
    
    public static final String ruleMessage = "crgrMessage";
    public static final String ruleNumber = "crgrNumber";
    public static final String ruleCaption = "crgrCaption";
    public static final String ruleCategory = "crgrCategory";
    public static final String ruleIdent = "crgrIdentifier";
    public static final String ruleSuggestion = "crgrSuggText";
    public static final String ruleStatus = "crgrRuleErrorType";
    public static final String ruleType = "crgRuleTypes";

    //These Columns are selected if a empty,new Filter is created
    protected static final List<String> DEFAULT_COLUMNS = Arrays.asList(ruleNumber, ruleCaption, ruleCategory, ruleIdent, ruleSuggestion, ruleStatus, ruleType);
    protected static final List<String> HAS_MESSAGE_COLUMNS = Arrays.asList(ruleMessage, ruleNumber, ruleCaption, ruleCategory, ruleIdent, ruleSuggestion, ruleStatus, ruleType);

    public SearchListRuleAttributes() {
        
        add(ruleMessage, "CRG_MESSAGE", "crgrMessage", Lang.RULE_COLUMN_MESSAGE)
                .setSize(27);
        add(ruleNumber, "CRG_RULES", "crgrNumber", Lang.RULE_COLUMN_NUMBER) //Regelnummer
                .setFormat(new SearchListFormatString())
                .setSize(120);

        add(ruleCaption, "CRG_RULES", "crgrCaption", Lang.RULE_COLUMN_CAPTION) //Bezeichnung
                .setFormat(new SearchListFormatString())
                .setSize(420);

        add(ruleCategory, "CRG_RULES", "crgrCategory", Lang.RULE_COLUMN_CATEGORY) //Kategorie
                .setFormat(new SearchListFormatString())
                .setSize(150);

//        add(ruleCategory, "CRG_RULE", "ruleCategory", "Kategorie") //Kategorie
//        .setFormat(new SearchListFormatEnum(RuleCategroyEn.class))
//        .setSize(130);
        add(ruleIdent, "CRG_RULES", "crgrIdentifier", Lang.RULE_COLUMN_IDENT) //Identnr
                .setFormat(new SearchListFormatString())
                .setSize(180);

        add(ruleSuggestion, "CRG_RULES", "crgrSuggText", Lang.RULE_COLUMN_SUGGESTION) //Vorschlag
                .setFormat(new SearchListFormatString())
                .setSize(220);

        add(ruleStatus, "CRG_RULES", "crgrRuleErrorType", Lang.RULE_COLUMN_STATUS) //Status
                .setFormat(new SearchListFormatEnum(RuleTypeEn.class))
                .setSize(190);
        add(ruleType, "CRG_RULES", "crgRuleTypes", Lang.RULE_COLUMN_TYPE) //Typ
                //                .setFormat(new SearchListFormatString())
                //                .setSize(130);
                .setFormat(new SearchListFormatMap(CrgRuleTypesMap.class))
                .setSize(90);

//        add(ruleYear, "CRG_RULES", "ruleYear", "Jahr") //Jahr
//        .setFormat(new SearchListFormatString())
//        .setSize(130);
    }

    public static synchronized SearchListRuleAttributes instance() {
        if (instance == null) {
            instance = new SearchListRuleAttributes();
        }
        return instance;
    }

    public static List<String> getDefaultColumns() {
        return new ArrayList<>(DEFAULT_COLUMNS);
    }
    
    public static List<String> getMessageColumns(){
        return new ArrayList<>(HAS_MESSAGE_COLUMNS);
    }
//    @Override
//    public SearchListAttribute add(final String pKey, final SearchListAttribute pParentAttribute, final String pLanguageKey) {
//        if (pParentAttribute == null) {
//            throw new IllegalArgumentException("parent cannot be null for attribute '" + pKey + "'");
//        }
//        SearchListAttribute attr = add(pKey, pParentAttribute.getDatabaseTable(), pParentAttribute.getDatabaseField(), pLanguageKey);
//        attr.setParent(pParentAttribute);
//        return attr;
//    }
//    
//    public SearchListAttribute add(final String pKey, final String pDatabaseTable, final String pDatabaseField, final String pLanguageKey) {
//        return super.add(pKey, pDatabaseTable, pDatabaseField, pLanguageKey);
//        //SearchListAttribute attribute = new SearchListAttribute(pKey, pDatabaseTable, pDatabaseField, pLanguageKey);
//        //attributeMap.put(pKey, attribute);
//        //attribute.setNumber(attributeMap.size());
//        //return attribute;
//    }
    //protected abstract void initKeys();
    @Override
    public SearchListAttribute get(final String pKey) {
        return getByKey(pKey);
    }

//    @Override
//    public SearchListAttribute getByKey(final String pKey) {
//        return super.getByKey(pKey);
////        SearchListAttribute attr = null;
////        String key = (pKey == null) ? "" : pKey.trim();
////        for (Map.Entry<String, SearchListAttribute> entry : attributeMap.entrySet()) {
////            //System.out.println(entry.getKey() + "/" + entry.getValue());
////            //String key = entry.getKey();
////            //if (entry.getKey().getName().equalsIgnoreCase(pKey)) {
////            if (entry.getKey().equalsIgnoreCase(key)) {
////                attr = entry.getValue();
////                break;
////            }
////        }
////        return attr;
//    }
    /**
     *
     * @param pField Database Field
     * @return List of WorkingListAttributes
     */
    @Override
    public List<SearchListAttribute> getByField(final String pField) {
        return super.getByField(pField);
//        List<SearchListAttribute> results = new LinkedList<>();
//        String field = (pField == null) ? "" : pField.trim();
//        for (Map.Entry<String, SearchListAttribute> entry : attributeMap.entrySet()) {
//            //System.out.println(entry.getKey() + "/" + entry.getValue());
//            //String key = entry.getKey();
//            //if (entry.getKey().getName().equalsIgnoreCase(pKey)) {
//            if (entry.getValue().getDatabaseField().equalsIgnoreCase(field)) {
//                results.add(entry.getValue());
//            }
//        }
//        return results;
    }

    /**
     * There are probably more than one result, but it returns only the first
     * match!
     *
     * @param pTable Database Table
     * @param pField Database Field
     * @return SearchListAttribute
     */
    @Override
    public SearchListAttribute getByQualifiedField(final String pTable, final String pField) {
        SearchListAttribute attr = null;
        String table = (pTable == null) ? "" : pTable.trim();
        String field = (pField == null) ? "" : pField.trim();
        for (SearchListAttribute wla : getByField(field)) {
            //System.out.println(entry.getKey() + "/" + entry.getValue());
            //String key = entry.getKey();
            //if (entry.getKey().getName().equalsIgnoreCase(pKey)) {
            if (wla.getDatabaseTable().equalsIgnoreCase(table)) {
                attr = wla;
                break;
            }
        }
        return attr;
    }

    /**
     *
     * @return Copy of attributes list
     */
    @Override
    public Map<String, SearchListAttribute> getAll() {
        return super.getAll();
//        Map<String, SearchListAttribute> attributeListCopy = new LinkedHashMap<>();
//        attributeListCopy.putAll(attributeMap);
//        return attributeListCopy;
    }

    @Override
    public Set<String> getKeys() {
        return super.getKeys();
//        Set<String> resultList = new HashSet<>();
//        for (Map.Entry<String, SearchListAttribute> entry : attributeMap.entrySet()) {
//            resultList.add(entry.getKey());
//        }
//        return resultList;
    }

    @Override
    public Set<SearchListAttribute> getAttributes() {
        return super.getAttributes();
//        Set<SearchListAttribute> resultList = new HashSet<>();
//        for (Map.Entry<String, SearchListAttribute> entry : attributeMap.entrySet()) {
//            resultList.add(entry.getValue());
//        }
//        return resultList;
    }

    @Override
    protected void initKeys() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
