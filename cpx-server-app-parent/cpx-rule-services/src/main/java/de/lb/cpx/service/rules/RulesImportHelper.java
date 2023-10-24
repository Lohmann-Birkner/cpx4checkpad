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

import de.lb.cpx.service.readrules.codeschecker.RulesCodeChecker;
import de.lb.cpx.shared.rules.util.CpTable;
import de.checkpoint.ruleGrouper.CRGRule;
import de.checkpoint.ruleGrouper.data.CRGRuleTypes;
import de.checkpoint.server.data.caseRules.DatCaseRuleAttributes;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.server.rule.services.RuleExchangeError;
import de.lb.cpx.server.commonDB.model.enums.RuleTableCategoryEn;
import de.lb.cpx.server.commonDB.model.rules.RuleTablesIF;
import de.lb.cpx.shared.json.RuleTableMessageReader;
import de.lb.cpx.shared.json.enums.MessageReasonEn;
import de.lb.cpx.shared.rules.util.RuleValidationStatusEn;
import de.lb.cpx.shared.rules.util.RulesConverter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * used for transfer of data read from rules.xml into Checkpoint objects
 *
 * @author gerschmann
 */

public class RulesImportHelper {
//    @EJB
    RulesCodeChecker rulesCodeChecker;

////    @Inject
//    private TransferCodeCheckStore transferCodeCheckStore;
//    
    private static final Logger LOG = Logger.getLogger(RulesImportHelper.class.getName());

    private List<CRGRule> cpRules = null;
    private List<CRGRuleTypes> cpRuleTypes = null;
    private Map<String, CRGRuleTypes> mapCpRuleTypes = null;
    private Map<String, RuleTablesIF> cpTables = null;
//    private List<CrgRules> errRules = null;
//    private List<CrgRuleTables>errTables = null;
   private List<CRGRule> errCpRules = null;
    private List<CpTable>errCpTables = null;
    
    public RulesImportHelper(){
        
    };

    public RulesImportHelper(RulesCodeChecker pTransferCodeCheckStore){
        rulesCodeChecker = pTransferCodeCheckStore;
    };

    public List<CRGRule> getCpRules() {
        return cpRules == null ? new ArrayList<>() : cpRules;
    }


    public List<CRGRuleTypes> getCpRuleTypes() {
        return cpRuleTypes == null ? new ArrayList<>() : cpRuleTypes;
    }

    public Map<String, RuleTablesIF> getCpTables() {
        return cpTables == null ? new HashMap<>() : cpTables;
    }

    public void setCpRules(List<CRGRule> cpRules) {
        this.cpRules = cpRules;
    }

    public void setCpRuleTypes(List<CRGRuleTypes> cpRuleTypes) {
        this.cpRuleTypes = cpRuleTypes;
        setMapCpRuleTypes(cpRuleTypes);
    }

    public Map<String, CRGRuleTypes> getMapCpRuleTypes() {
        if(mapCpRuleTypes == null){
            mapCpRuleTypes = new HashMap<>();
        }
        return mapCpRuleTypes;
    }

    private void setMapCpRuleTypes(List<CRGRuleTypes> cpRuleTypes) {
        getMapCpRuleTypes();
        if(cpRuleTypes == null){
            return;
        }
        for(CRGRuleTypes type: cpRuleTypes){
            mapCpRuleTypes.put(type.crgrt_shorttext, type);
        }
       
    }

    public void setCpTables(Map<String, RuleTablesIF> cpTables) {
        this.cpTables = cpTables;
    }
    
    public boolean isRules(Element root) {
        return (root != null && root.getTagName().equals(DatCaseRuleAttributes.ELEMENT_CASE_RULES));
    }

    /**
     * creates rule tables from input xml file
     *
     * @param root root
     * @return map of whatever(?)
     */
    public  void createTablesFromXml(Element root) {
        createTablesFromXml(root, false, 0,0);
    }
    public  void createTablesFromXml(Element root, boolean doTransferCheck, int srcYear, int destYear) {
        try {
            // find table types from full rules xml - document
            Map<String, RuleTableCategoryEn> tableCategories = new HashMap <>();
            RulesConverter.getTableCategories(root, tableCategories);
            int tableYear = RulesConverter.getYearFromFirstRule(root); 
            srcYear = srcYear == 0?(tableYear== 0?destYear:tableYear):srcYear;
            cpTables = new HashMap<>();

            NodeList lst = root.getElementsByTagName(DatCaseRuleAttributes.ELEMENT_EXPORT_TABLE);
            int size = lst.getLength();
            for (int i = 0; i < size; i++) {
                Element ele = (Element) lst.item(i);
                String name = ele.getAttribute(DatCaseRuleAttributes.ATT_EXPORT_TABLE_NAME);
                String comment = ele.getAttribute(DatCaseRuleAttributes.ATT_EXPORT_TABLE_COMMENT);
                RuleTableCategoryEn category = tableCategories.get(name);
                String catString = ele.getAttribute(DatCaseRuleAttributes.ATT_EXPORT_TABLE_CATEGORY);
                String yearString = ele.getAttribute(DatCaseRuleAttributes.ATT_EXPORT_TABLE_YEAR);
                int tabYear = srcYear;
                if(yearString != null && !yearString.isEmpty()){
                    try{
                        tabYear = Integer.parseInt(yearString);
                    }catch(NumberFormatException ex){
                        LOG.log(Level.SEVERE, "could not find out rule year from " + tableYear + " for table " + name, ex);
                    
                    }
                }
                srcYear = tabYear;
                if(catString != null && !catString.isEmpty()){

                    category = RuleTableCategoryEn.valueOf(catString);
                }
                String content = getTableContent(ele);
                name = name.replaceAll("'", "");
                CpTable table = new CpTable(name, content, comment == null ? null : CRGRule.getDisplayText(comment),
                        category == null?RuleTableCategoryEn.NOT_SET:category);
                table.setSrcYear(srcYear);
                if(rulesCodeChecker != null && doTransferCheck ){
                    rulesCodeChecker.checkRuleTable(srcYear, destYear, table);
                    
                }
                cpTables.put(name, table);
            }

            for (int i = size - 1; i >= 0; i--) {// to release memory
                Element ele = (Element) lst.item(i);
                root.removeChild(ele);
            }

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, " Error on creating rule tables from xml file ", ex);

        }

    }
    /**
     * creates rule table content from XML Element ATT_EXPORT_TABLE_NAME
     *
     */
    private String getTableContent(Element ele) throws Exception {
//        StringBuilder content = new StringBuilder();
        NodeList vals = ele.getElementsByTagName(DatCaseRuleAttributes.ELEMENT_EXPORT_VAL);
        List<String> valList = new ArrayList<>();
        for (int j = 0; j < vals.getLength(); j++) {
            Element valNode = (Element) vals.item(j);
            Node node = valNode.getFirstChild();
            while (!(node instanceof Text) && node != null) {
                node = node.getNextSibling();
            }

            if (node != null) {
                String val = node.getNodeValue();
                if (val != null && val.length() != 0) {
                    val = val.replaceAll("'", "");
                    valList.add(val);
    //                content.append(val).append(",");
                }

            }
            Collections.sort(valList);
        }
//        String ret = content.toString();
//        if (ret.endsWith(",")) {
//            ret = ret.substring(0, ret.length() - 1);
//        }
        String ret = StringUtils.join(valList, ","); 
        return ret;
    }

    /**
     * creates rule types list from xml file
     *
     * @param root root
     * @return list of crg rule types
     * @throws Exception error
     */
    public List<CRGRuleTypes> createRuleTypesFromXml(Element root) throws Exception {
        try {
//<rules_type display_text="Medizinverständnis" rtype_id="1" rtype_orgid="5" short_text="MV"/>
            List<CRGRuleTypes> pCpRuleTypes = new ArrayList<>();
            NodeList lst = root.getElementsByTagName(DatCaseRuleAttributes.ELEMENT_RULES_TYPE);
            for (int i = 0; i < lst.getLength(); i++) {
                Element ele = (Element) lst.item(i);
                CRGRuleTypes type = new CRGRuleTypes();
                type.setDisplayText(ele.getAttribute(DatCaseRuleAttributes.ATT_RULES_TYPE_DISPLAY_TEXT));
                type.setText(ele.getAttribute(DatCaseRuleAttributes.ATT_RULES_TYPE_SHORT_TEXT));
                type.setIdent(Integer.parseInt(ele.getAttribute(DatCaseRuleAttributes.ATT_RULES_TYPE_ORGID)));
                type.setType(Integer.parseInt(ele.getAttribute(DatCaseRuleAttributes.ATT_RULES_TYPE_ID)));
                pCpRuleTypes.add(type);
            }
            for (int i = 0; i < lst.getLength(); i++) {// to release memory
                Element ele = (Element) lst.item(i);
                root.removeChild(ele);
            }
            return pCpRuleTypes;
        } catch (NumberFormatException | DOMException ex) {
            LOG.log(Level.SEVERE, " Error on creating rule types from xml file ", ex);
            return null;
        }
    }

    /**
     * creates rules from xml file
     *
     * @param root root
     * @param poolName pool name
     * @param year year
     * @param cpRuleTypes rule types
     * @return list of rules
     * @throws Exception error
     */
    public  List<CRGRule> createRulesFromXml(Element root, String poolName, int year, List<CRGRuleTypes> cpRuleTypes) throws Exception {
        return createRulesFromXml(root, poolName, year, cpRuleTypes, null, null);
    }

    public  List<CRGRule> createRulesFromXml(Element root, String poolName, int year, List<CRGRuleTypes> cpRuleTypes, 
            List<String> RuleIdents4import, long[] roleIdsList) throws Exception {
        return createRulesFromXml(root, poolName, year, cpRuleTypes, RuleIdents4import, roleIdsList, 0, false);
    }
    
    public  List<CRGRule> createRulesFromXml(Element pRoot, String pPoolName, int pDestYear, List<CRGRuleTypes> pCpRuleTypes, 
            List<String> pRuleIdents4import, long[] pRoleIdsList,
            int pSrcYear,
            boolean pDoCheck
    ) throws Exception {
        try {
            List<CRGRule> pCpRules = new ArrayList<>();
            NodeList lst = pRoot.getElementsByTagName(DatCaseRuleAttributes.ELEMENT_RULES);
            Calendar calendar = Calendar.getInstance();
            for (int i = 0; i < lst.getLength(); i++) {
                Element ele = (Element) lst.item(i);
                CRGRule rule = new CRGRule(ele, pPoolName, pCpRuleTypes, 2);
                int ruleYear = 0;
                rule.setAdmDateBackup(rule.m_adt);
                rule.setDisDateBackup(rule.m_ddt);
                rule.setYearBackup(rule.m_year);
                if (rule.m_adt != null) {
                    
                    calendar.setTime(rule.m_adt);
                    ruleYear = calendar.get(Calendar.YEAR);
                    calendar.set(Calendar.YEAR, pDestYear);

                } else {
                    calendar.set(pDestYear, Calendar.JANUARY, 1);
                }
                if(pSrcYear == 0){
 // get year from the rule attribute from(m_adt)
                    pSrcYear = ruleYear;
                }
// korrektur auf aktuellen Jahr                 
                rule.m_adt = calendar.getTime();
                if (rule.m_ddt != null) {
                    calendar.setTime(rule.m_ddt);
                    calendar.set(Calendar.YEAR, pDestYear);

                } else {
                    calendar.set(pDestYear, Calendar.DECEMBER, 31);
                }
                rule.m_ddt = calendar.getTime();
                rule.m_year = pDestYear;
                if (pRuleIdents4import == null || pRuleIdents4import.contains(rule.m_ruleNumber)) {
                    pCpRules.add(rule);
                }
// korrektur rule_number mit dem pool ident
                rule.m_ruleNumber = pPoolName + "_" + RulesConverter.getIdentConstantPart(rule.m_ruleNumber);
                if (pRoleIdsList != null && pRoleIdsList.length > 0) {
                    rule.m_roles = new long[pRoleIdsList.length];
                    System.arraycopy(pRoleIdsList, 0, rule.m_roles, 0, pRoleIdsList.length);
                }
                if(pDoCheck && rulesCodeChecker != null){
                    rulesCodeChecker.checkRule(rule, pSrcYear, pDestYear, this.getCpTables());
                }
            }
            for (int i = 0; i < lst.getLength(); i++) {// to release memory
                Element ele = (Element) lst.item(i);
                pRoot.removeChild(ele);
            }
            return pCpRules;
        } catch (NumberFormatException | DOMException ex) {
            LOG.log(Level.SEVERE, " Error on creating rule from xml file ", ex);
            return null;
        }
    }

    public List<CrgRuleTypes> getRuleTypesAsCpx(PoolTypeEn pPoolType) {
        List<CrgRuleTypes> cpxRuleTypes = new ArrayList<>();
        if (cpRuleTypes == null) {
            return cpxRuleTypes;
        }
        cpRuleTypes.forEach((type) -> {
            cpxRuleTypes.add(RulesConverter.getCpxRuleType(type, pPoolType));
        });
        return cpxRuleTypes;
    }

    public List<CrgRuleTables> getRuleTablesAsCpx(PoolTypeEn pPoolType) {
        return getRuleTablesAsCpx(pPoolType, null);
    }
    
    public List<CrgRuleTables> getValidRuleTablesAsCpx(PoolTypeEn pPoolType) {
        return getRuleTablesAsCpx(pPoolType, new ArrayList<RuleExchangeError>());
    }
    
    public List<CrgRuleTables> getRuleTablesAsCpx(PoolTypeEn pPoolType, List<RuleExchangeError> result) {
    
        List<CrgRuleTables> cpxRuleTables = new ArrayList<>();
        if (cpTables == null) {
            return cpxRuleTables;
        }
        Set<String> tabNames = cpTables.keySet();
        RuleTableMessageReader msgReader = new RuleTableMessageReader();
        for( String tableName: tabNames) {
            CrgRuleTables tab = RulesConverter.getCpxRuleTable(tableName, (CpTable)cpTables.get(tableName), pPoolType);
            if(result != null){
                // for web - import, we use result as a flag to save erronious tables in different list
                try{
                    if(tab.hasErrorMessage()){
                        result.add(new RuleExchangeError(RuleValidationStatusEn.RULE_TABLE_HAS_ERRORS, pPoolType, tableName, msgReader.readUtf8AndGetReason(tab.getCrgtMessage()).toString(), result != null)); 
                        this.addErrTable((CpTable)cpTables.get(tableName));
//                        continue;
                    }else{
                        cpxRuleTables.add(tab);
                    }
                   
                }catch(IOException ex){
                        LOG.log(Level.SEVERE, "error in rule message", ex);
                    
                }
            }else{
                cpxRuleTables.add(tab);
            }
        };
        return cpxRuleTables;
    }

    /**
     * rules from xml are converted in the CrgRules - objects. All new rule
     * types and new tables are already imported and saved in DB by the
     * conversion we check, whether types and tables, that are used in these
     * rules, are already in DB, so that we can change the rule definition into
     * its internal description. If not found, rule will not be imported
     *
     * @param pPoolType pool type
     * @param name2type map Cpx typeName to type
     * @param name2table map Cpx table name to table
     * @param errors array of errors
     * @return list of cpx rules ready to be saved in db
     */
    public List<CrgRules> getRulesAsCpx(PoolTypeEn pPoolType, Map<String, CrgRuleTypes> name2type, Map<String, CrgRuleTables> name2table, List<RuleExchangeError> errors) {
        return getRulesAsCpx(pPoolType, name2type, name2table, errors, false);
    }

    /**
     * 
     * @param pPoolType pool type
     * @param name2type for rule types name-> CrgRuleTypes
     * @param name2table for rule tables name-> CrgRuleTables
     * @param errors list of errors found by import
     * @param isValidation flag, that is true for webapp only.
     * @return list of rules
     */
    public List<CrgRules> getRulesAsCpx(PoolTypeEn pPoolType, Map<String, CrgRuleTypes> name2type,
            Map<String, CrgRuleTables> name2table, List<RuleExchangeError> errors, boolean isValidation) {
        List<CrgRules> cpxRules = new ArrayList<>();
        if (cpRules == null) {
            return cpxRules;
        }

        for (CRGRule cpRule : cpRules) {
//            if(checkName(cpRule.m_number)){
//                int i = 0;
//            }
            CrgRuleTypes cpxType = name2type.get(cpRule.m_errorTypeText);
            if (cpxType == null) {
                // regeltyp is not defined
                errors.add(new RuleExchangeError(isValidation ? RuleValidationStatusEn.NO_RULE_TYPE_FOUND_4_RULE_VALIDATION : RuleValidationStatusEn.NO_RULE_TYPE_FOUND_4_RULE, pPoolType, cpRule.m_number, cpRule.m_errorTypeText, isValidation));
//                continue;
//if the rule type is not found by validation - process we tell user, that this types are not there and if it is ok for him
//we create the new types before we import the rules and save them. The type names we get from the error
// by import are new types already saved in db, so it is hardly possible that by import type is null
                if(isValidation){ 
                    cpxType = CrgRuleTypes.getTypeInstance(pPoolType);
                    cpxType.setCrgtShortText(cpRule.m_errorTypeText);
                    cpxType.setCrgtDisplayText(cpRule.m_errorTypeText);
                    name2type.put(cpRule.m_errorTypeText, cpxType);
                }else{
                    continue;
                }
            }
            
            // now check the Tables
            @SuppressWarnings("unchecked")
            List<String> tabNames = new ArrayList<>(cpRule.getTableNames());
            boolean checked = true;
            for (String tabName : tabNames) {
                if (name2table.get(tabName) == null) {
                    errors.add(new RuleExchangeError(isValidation ? RuleValidationStatusEn.NO_TABLE_FOUND_VALIDATION : RuleValidationStatusEn.NO_TABLE_FOUND, pPoolType, cpRule.m_number, tabName, isValidation));
                    LOG.log(Level.INFO, "tabName ={0} not found", new Object[]{tabName});

                    checked = false;
                    break;
                }
                LOG.log(Level.FINE, "tabName ={0} tableid ={1}", new Object[]{tabName, name2table.get(tabName).getId()});

            }
            
            // rules with tables that are not found, would not be imported
            if (checked) {
                CrgRules cpxRule = RulesConverter.getCpxRule(cpRule, null, true, pPoolType, 
                        cpxType, name2table, false);
                if(isValidation ){
                    if( cpxRule.getCrgrMessage()== null){
                        cpxRules.add(cpxRule);
                    }

                }else{

//                LOG.info("cpRule = " + cpRule.toString());
                    cpxRules.add(cpxRule);
                }
            }

        }
        return cpxRules;
    }

    /**
     * checks whether all rules have rule types in the DB
     *
     * @param pPoolType pool type
     * @param name2type name 2 rule type in db
     * @return list of rule types that not found in DB
     */
    public List<CrgRuleTypes> checkRuleTypes(PoolTypeEn pPoolType, Map<String, CrgRuleTypes> name2type) {
        if (cpRules == null) {
            return new ArrayList<>();
        }
        Map<String, CrgRuleTypes> cpxRuleTypes = new HashMap<>();

        for (CRGRule cpRule : cpRules) {
            CrgRuleTypes cpxType = name2type.get(cpRule.m_errorTypeText);
            if (cpxType == null && cpxRuleTypes.get(cpRule.m_errorTypeText) == null) {
                // regeltyp is not defined create new
                cpxType = CrgRuleTypes.getTypeInstance(pPoolType);
                cpxType.setCrgtShortText(cpRule.m_errorTypeText);
                cpxType.setCrgtDisplayText(cpRule.m_errorTypeText);
                cpxRuleTypes.put(cpRule.m_errorTypeText, cpxType);
            }
        }
        return new ArrayList<>(cpxRuleTypes.values());
    }

    /**
     * removes rule types and rule tables from the imported lists, that are not
     * used in imported rules
     */
    public void checkTypesAndTables4Rules() {
        List<String> usedRuleTypes = new ArrayList<>();
        List<String> usedRuleTables = new ArrayList<>();
        if (cpRules != null) {
            for (CRGRule rule : cpRules) {
                if (rule.m_errorTypeText != null && !usedRuleTypes.contains(rule.m_errorTypeText)) {
                    usedRuleTypes.add(rule.m_errorTypeText);
                }
                @SuppressWarnings("unchecked")
                List<String> tabNames = new ArrayList<>(rule.m_tableNames);
                tabNames.stream().filter((tabName) -> (!usedRuleTables.contains(tabName))).forEachOrdered((tabName) -> {
                    usedRuleTables.add(tabName);
                });
            }
            if (cpRuleTypes != null) {
                List<CRGRuleTypes> types = new ArrayList<>();
                types.stream().filter((type) -> (usedRuleTypes.contains(type.crgrt_shorttext))).forEachOrdered((type) -> {
                    types.add(type);
                });
                cpRuleTypes = types;
            }
            if (cpTables != null) {
                Set<String> tabNames = cpTables.keySet();
                Map<String, RuleTablesIF> tables = new HashMap<>();
                tabNames.stream().filter((tabName) -> (usedRuleTables.contains(tabName))).forEachOrdered((tabName) -> {
                    tables.put(tabName, cpTables.get(tabName));
                });
                cpTables = tables;
            }
        }
    }

    public List<CRGRule> getErrRules() {
        if(this.errCpRules == null){
            errCpRules = new ArrayList<>();
        }

        return errCpRules;
    }

    public void setErrRules(List<CRGRule> errRules) {
        this.errCpRules = errRules;
    }

    public List<CpTable> getErrTables() {
        if(errCpTables == null){
            errCpTables = new ArrayList<>();
        }
        return errCpTables;
    }

    public void setErrTables(List<CpTable> errTables) {
        this.errCpTables = errTables;
    }
    
    
    private void addErrRule(CRGRule pRule, List<String> tabNames){
        if(!getErrRules().contains(pRule)){
            getErrRules().add(pRule);
           if(tabNames == null || this.cpTables == null){
               return;
           }
           tabNames.forEach((tabName) -> {
               addErrTable((CpTable)cpTables.get(tabName));
            });

        }
    }
    public void addErrTable(CpTable pTable){
        if(pTable == null){
            return;
        }
        if(!getErrTables().contains(pTable)){
            getErrTables().add(pTable);
        }
    }

    private boolean checkName(String pNumber) {
        String arr[] = {
            "002a ",
            "002b ",
            "004b",
            "011 ",
            "026a ",
            "026b ",
            "048 ",
            "080",
            "083 ",
            "089",
            "142 ",
            "224 ",
            "280 ",
            "293",
            "300",
            "305",
            "M024b",
            "M030 ",
            "M079 ",
            "M095 ",
            "512",
            "515",
            "AOP01",
            "AOP02",
            "AOP03",
            "AOP05",
            "AOP06",
            "AOP04"
        };
        
        for(int i = 0; i < arr.length; i++){
            if(pNumber.trim().equalsIgnoreCase(arr[i].trim())){
                return true;
            }
        }
        return false;        
    }

    public void validateCpRules(List<RuleExchangeError> errors, PoolTypeEn pPoolType) {

        RuleTableMessageReader msgReader = new RuleTableMessageReader();
        for (CRGRule cpRule : cpRules) {
            if(cpRule.getErrorMessage() != null && cpRule.getErrorMessage().length > 0){

                try{
                    MessageReasonEn r = msgReader.readUtf8AndGetReason(cpRule.getErrorMessage());
                    errors.add(new RuleExchangeError(RuleValidationStatusEn.RULE_HAS_ERRORS, pPoolType, cpRule.m_number,r.toString(), true)); 
                    cpRule.m_rid = cpRule.m_ruleNumber;// workarround for equals is on rid
                    addErrRule(cpRule, cpRule.getTableNames());
                }catch(IOException ex){
                    LOG.log(Level.SEVERE, "error in rule message", ex);
                }
            }
        }
    }

    public List<CrgRules> getCpxErrRules(PoolTypeEn pPoolType){
        List <CrgRules> ret = new ArrayList<>();
        List<CRGRule> errRules = getErrRules();
        for(CRGRule rule: errRules){
            if(rule.getAdmDateBackup() != null){
                rule.m_adt=rule.getAdmDateBackup();
            }
            if(rule.getDisDateBackup() != null){
                rule.m_ddt=rule.getDisDateBackup();
            }
            if(rule.getYearBackup() != 0){
                rule.m_year=rule.getYearBackup();
            }
            rule.m_rid = "";// remove workarround

            CrgRules cpxRule = RulesConverter.getCpxRule(rule, null, true, pPoolType, 
                         null, null, false);
            cpxRule.setCrgRuleTypes(RulesConverter.getCpxRuleType(getMapCpRuleTypes().get(rule.m_errorTypeText)));
            ret.add(cpxRule);
        }
         return ret;   
    }
    
    public List<CrgRuleTables>getErrCpxTables(PoolTypeEn pPoolType){
        List <CpTable>cpTabs = getErrTables();
        List<CrgRuleTables> ret = new ArrayList<>();
        for(CpTable table:cpTabs){
            CrgRuleTables tab = RulesConverter.getCpxRuleTable(table.getName(), table, pPoolType);
            ret.add(tab);
        }
        return ret;
    }

}
