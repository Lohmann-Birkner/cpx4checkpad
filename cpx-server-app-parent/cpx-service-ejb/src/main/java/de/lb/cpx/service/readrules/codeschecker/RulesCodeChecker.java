/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.readrules.codeschecker;

import de.checkpoint.ruleGrouper.CRGRule;
import de.checkpoint.server.data.caseRules.DatRuleTerm;
import de.checkpoint.server.data.caseRules.DatRuleVal;
import de.lb.cpx.server.commonDB.dao.Rules4DbInteractBean;
import de.lb.cpx.server.commonDB.model.enums.RuleTableCategoryEn;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.server.commonDB.model.rules.RuleTablesIF;
import de.lb.cpx.shared.json.RuleMessage;
import de.lb.cpx.shared.json.RuleMessageBuilder;
import de.lb.cpx.shared.json.RuleMessageReader;
import de.lb.cpx.shared.json.RuleTableMessage;
import de.lb.cpx.shared.json.RuleTableMessageBuilder;
import de.lb.cpx.shared.json.RuleTableMessageReader;
import de.lb.cpx.shared.json.enums.MessageReasonEn;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.rules.util.RulesConverter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author gerschmann
 */

@Stateless
public class RulesCodeChecker {

    private static final Logger LOG = Logger.getLogger(RulesCodeChecker.class.getName());
    
    @Inject
    private TransferCodeCheckStore transferCodeCheckStore;
    
 
    @EJB
    private Rules4DbInteractBean rules4DbInteractBean;      
    
    @PostConstruct
    public void init(){
        LOG.log(Level.INFO, "RulesTransferFactoryBean: for initialising of TransferCodeCheckStore");
    }


    
 /**
     * checks rules codes with transfer tables, it is asumed that the tables are alteady checked and errors are saved in its CpTable objects
     * @param rule checkpoint rule for client(CRGRuleElement is null)
     * @param pSrcYear from year; 
     * @param pDestYear to year.
     * Error message will be saved in rule.errorMessage as byte array of the json string
     * @param pTables
     * @throws java.lang.Exception
     */

    public void checkRule(CRGRule rule, int pSrcYear, int pDestYear, Map<String, RuleTablesIF> pTables ) throws Exception{
        long start = System.currentTimeMillis();
       RuleMessageBuilder msgBuilder = new RuleMessageBuilder();
        List<DatRuleTerm> simpleTerms = rule.getAllSimpleTerms();
        if(simpleTerms == null|| simpleTerms.isEmpty()){
           // message: empty rule
           msgBuilder.setTerm("").setDescription(Lang.getRuleNoContentFound()).setReason(MessageReasonEn.VALIDATION_RULE_NO_CONTENT);
           rule.setErrorMessage(msgBuilder.buildUtf8());
           return;
        }

        List<RuleMessage> errMessages = new ArrayList<>();
        for(DatRuleTerm term: simpleTerms){
            if(term.getCriteriumName().isEmpty() && !term.getOperation().isEmpty()){
                RuleMessage msg = new RuleMessage();
                msg.setSrcYear(pSrcYear);
                msg.setDestYear(pDestYear);
                msg.setTerm(term.toString());
                msg.setReason(MessageReasonEn.VALIDATION_RULE_NOT_VALID);
                msg.setDescription(term instanceof DatRuleVal?Lang.getRuleTermNotComplete():Lang.getRuleSuggTermNotComplete());
                errMessages.add(msg);
                continue;
            }
           if(RuleTableCategoryEn.isCriterium4Check(term.getCriteriumName())&& !term.getOperation().isEmpty()){
               if(term.getCriteriumValue().isEmpty()){
                   RuleMessage msg = new RuleMessage();
                   msg.setTerm(term.toString());
                   msg.setSrcYear(pSrcYear);
                   msg.setDestYear(pDestYear);
                   msg.setReason(MessageReasonEn.VALIDATION_NO_VALUE);
                   msg.setDescription(term instanceof DatRuleVal?Lang.getRuleTermNotComplete():Lang.getRuleSuggTermNotComplete());
                   errMessages.add(msg);

                   // message error in term
                   continue;
               }

               RuleMessage msg = checkMessage4Term(term.getCriteriumValue(),
                       term.getOperation(), 
                       term.toString(),
                       pSrcYear,  
                       pDestYear,
                       term.getCriteriumName().contains("iagnose")?RuleTableCategoryEn.ICD:
                               RuleTableCategoryEn.OPS,
//                       errMessages,
                       pTables );
               if(!term.getConditionOperation().isEmpty()){
                   if(term.getConditionCritValue().isEmpty()){
                       if(msg == null){
                            msg = new RuleMessage();
                            msg.setSrcYear(pSrcYear);
                            msg.setDestYear(pDestYear);
                            msg.setTerm(term.toString());
                            msg.setReason(MessageReasonEn.VALIDATION_SUGG_NO_VALUE);
                            msg.setDescription(Lang.getRuleSuggConditionNotComplete());
                       }else{
                           msg.setDescription(msg.getDescription()  + "\n" + Lang.getRuleSuggConditionNotComplete());
                           msg.setReason(MessageReasonEn.VALIDATION_SUGG_MERGED_REASON);
                       }
//                   errMessages.add(msg);
                      
                   }
                   else{
                       RuleMessage msgCondition = checkMessage4Term(term.getConditionCritValue(),
                       term.getConditionOperation(), 
                       msg == null?term.toString():msg.getTerm(), //term syntax could be changed already when table id was replaced with table name 
                       pSrcYear,  
                       pDestYear,
                       term.getCriteriumName().contains("iagnose")?RuleTableCategoryEn.ICD:
                               RuleTableCategoryEn.OPS,
//                       errMessages, 
                       pTables );
                       if(msg != null){
                           msg.merge4Term(msgCondition);
                       }else{
                           msg = msgCondition;
                       }
                       
                   }
                   
               }else{// we have condition operation but no value
                   if(!term.getConditionCritValue().isEmpty()){
                      if(msg == null){
                            msg = new RuleMessage();
                            msg.setSrcYear(pSrcYear);
                            msg.setDestYear(pDestYear);
                            msg.setTerm(term.toString());
                            msg.setReason(MessageReasonEn.VALIDATION_SUGG_NO_VALUE);
                            msg.setDescription(Lang.getRuleSuggConditionNotComplete());
                       }else{
                           msg.setDescription(msg.getDescription()  + "\n" + Lang.getRuleSuggConditionNotComplete());
                           msg.setReason(MessageReasonEn.VALIDATION_SUGG_MERGED_REASON);
                       }
                       
                   }
               }
               
                if(msg != null){

                    errMessages.add(msg);
                    LOG.log(Level.INFO, msg.toString());
                }   

            }else{
                if(!term.isValid()){
                   
                   RuleMessage msg = new RuleMessage();
                   msg.setSrcYear(pSrcYear);
                   msg.setDestYear(pDestYear);
                   msg.setTerm(term.toString());
                   msg.setReason(MessageReasonEn.VALIDATION_RULE_NOT_VALID);
                   msg.setDescription(term instanceof DatRuleVal?Lang.getRuleTermNotComplete():Lang.getRuleSuggTermNotComplete());
                   errMessages.add(msg);

                }
            }
        
        } 
        if(!errMessages.isEmpty()){
            RuleMessage[] arr = new RuleMessage[errMessages.size()];
            msgBuilder.setAll(errMessages.toArray(arr));
             rule.setErrorMessage(msgBuilder.buildUtf8()); 

        }else{
            rule.setErrorMessage(null);
        }
        LOG.log(Level.INFO, "checkRule: ruleNumber = " + rule.getRuleNumber() + " time=" + String.valueOf(System.currentTimeMillis() - start));
    }
    
    private RuleMessage checkMessage4Term(String pCodes, String operation, String termAsString,
            int pSrcYear, int pDestYear,
            RuleTableCategoryEn pType, //List<RuleMessage> pErrMessages, 
            Map<String, RuleTablesIF> pTables ){

    RuleMessage msg  = null;    
        if(operation.contains("@")){
// tables are checked already
            if(pTables != null){
                
                RuleTablesIF tab = pTables.get(pCodes);
                if(tab == null){
                    // message table not found
                    msg = new RuleMessage();
                    msg.setTerm(termAsString);
                    msg.setReason(MessageReasonEn.VALIDATION_NO_VALUE);
                    msg.setDescription(Lang.getRuleTermNotComplete());
                       msg.setSrcYear(pSrcYear);
                       msg.setDestYear(pDestYear);
//                    pErrMessages.add(msg);

                } else {
                    if(tab.hasErrorMessage()){
                    // message error in table tab
                    
                    // rules that are already saved in db have tableid as table name in term
                        msg = new RuleMessage();
                        if(!pCodes.equals(String.valueOf(tab.getRuleTableName()))){
                            termAsString = termAsString.replaceAll(pCodes, tab.getRuleTableName());
                        }
                        msg.setTerm(termAsString);
                        msg.setDescription(Lang.getRuleTableCodesNotValid(tab.getRuleTableName(), ""));
                        msg.setReason(MessageReasonEn.VALIDATION_CHECK_CATALOG_TRANSFER_TABLE);
                        msg.setSrcYear(pSrcYear);
                        msg.setDestYear(pDestYear);
//                        pErrMessages.add(msg);
                    }
                }
            }else{
                msg = new RuleMessage();
                msg.setTerm(termAsString);
                msg.setDescription(pCodes);
                msg.setReason(MessageReasonEn.VALIDATION_RULE_WITHOUT_TABLES);
                msg.setSrcYear(pSrcYear);
                msg.setDestYear(pDestYear);
//                pErrMessages.add(msg);
                
            }
        }else{

            try{
                String errorCodes = transferCodeCheckStore.checkTableContent(pType, pSrcYear, pDestYear, pCodes);

                if(!errorCodes.isEmpty()){
                    String[] parts = errorCodes.split("<>");
                    msg = new RuleMessage();
                    msg.setCodes(parts[0]);
                    msg.setTerm(termAsString);
                    msg.setSrcYear(pSrcYear);
                    msg.setDestYear(pDestYear);
                    msg.setReason(MessageReasonEn.VALIDATION_CHECK_CATALOG_TRANSFER_TABLE);
                    msg.setDescription(Lang.getRuleCodes4YearNotValid(stripMessageFromReason(parts[0]), String.valueOf(pDestYear))
                    + (parts.length > 1 && parts[1] != null?("\nFolgende Änderungen müssen berücksichtigt werden:\n" + parts[1]):""));
//                    pErrMessages.add(msg);

                }
                    
                
            }catch(Exception ex){
                // message error on checkValue
                msg = new RuleMessage();
                msg.setTerm(termAsString);
                msg.setReason(MessageReasonEn.VALIDATION_CODE_ERROR);
                msg.setDescription(Lang.getRuleCheckCodeError( pCodes));
//                pErrMessages.add(msg);
               LOG.log(Level.SEVERE, "error on check code " + pCodes + ": " + pType.name(), ex);
            }
        }
        return msg;
    }

    public void checkRuleTable4Year(int crgplPoolYear, RuleTablesIF pTable) throws Exception {
        checkRuleTable(crgplPoolYear, crgplPoolYear, pTable);
    }
    public void checkRuleTable(int pSrcYear, int pDestYear, RuleTablesIF pTable) throws Exception {
    
        if(pTable.getRuleTableCategory() != null &&
                (pTable.getRuleTableCategory().equals(RuleTableCategoryEn.ICD) || pTable.getRuleTableCategory().equals(RuleTableCategoryEn.OPS))){
                    long start = System.currentTimeMillis();
                    String message = transferCodeCheckStore.checkTableContent(pTable.getRuleTableCategory(), pSrcYear, pDestYear, 
                           pTable.getRuleTableContent()); 
                    LOG.log(Level.INFO, "checkRuleTable: Time for checkTableContent, Table " + pTable.getRuleTableName() + " time= " + String.valueOf(System.currentTimeMillis() - start));       
                    if(!message.isEmpty()){
                        LOG.log(Level.FINE, " table: " + pTable.getRuleTableName()+ " code not found:" + message.toString());
                        try{
                            String[] parts = message.split("<>");
                            RuleTableMessage msg = new RuleTableMessage();
                            msg.setCodes(parts[0]);
                            parts[0] = stripMessageFromReason(parts[0]);
                            msg.setDescription(pSrcYear == pDestYear?(Lang.getRuleTableCodesNotValid(pTable.getRuleTableName(), parts[0])
                                    + (parts.length > 1&& parts[1] != null? parts[1]:"")):
                                    (Lang.getRuleCodes4YearNotValid(parts[0], String.valueOf(pDestYear))
                    + (parts.length > 1 && parts[1] != null?("\nFolgende Änderungen müssen berücksichtigt werden:\n" + parts[1]):"")));
                            msg.setSrcYear(pSrcYear);
                            msg.setDestYear(pDestYear);
                            msg.setReason(pSrcYear == pDestYear?MessageReasonEn.VALIDATION_VALUE_NOT_IN_CATALOG:
                                    MessageReasonEn.VALIDATION_CHECK_CATALOG_TRANSFER_TABLE);
                            msg.setSeverity("false");
                            byte[] msg1 = new RuleTableMessageBuilder().setAll(msg).buildUtf8();
                            pTable.setRuleTableMessage(msg1);
                        }catch(IOException ex){
                            LOG.log(Level.SEVERE, "error by creation of json error message for rule table " + pTable.getRuleTableName()
                                    + ", codes " + message.toString(), ex);
                        }

                    }else{
                        pTable.setRuleTableMessage(null);
                }
        }
    }

    public void checkRule4Year(int pYear, CrgRules pRule,  List <CrgRuleTables> pTables) throws Exception {
        Map<String, RuleTablesIF> tables = new HashMap<>();
        if(pTables != null){
            for(CrgRuleTables table: pTables){
                tables.put(String.valueOf(table.getId()), table);
            }
        }
        checkRule4Year(pYear, pRule, tables) ;
        RuleMessageReader reader = new RuleMessageReader();
        String str = reader.readRuleForDisplay(pRule);
        LOG.log(Level.INFO, str == null?"":str);
    }
    
    public void checkRule4Year(int pYear, CrgRules pRule, Map<String, RuleTablesIF> pTables ) throws Exception {
        CRGRule cpRule = RulesConverter.getCpRuleFromRuleDefinition(pRule);
        this.checkRule(cpRule, pYear, pYear, pTables);
        RuleMessageReader reader = new RuleMessageReader();
        String str = reader.readRuleForDisplay(pRule);
        LOG.log(Level.INFO, str == null?"":str);
        pRule.setCrgrMessage(cpRule.getErrorMessage());
    }

    public String checkTransferCode(RuleTableCategoryEn pType, int pSourceYear, int pDestinationYear, String pCatalogCode) {
        return transferCodeCheckStore.checkTransferCode(pType, pSourceYear, pDestinationYear, pCatalogCode);
    }

    public byte[] validateRuleTable(int pDestYear, CrgRuleTables pRuleTable) throws Exception {

        try {
            byte[] oldMessage = pRuleTable.getCrgtMessage();
            int srcYear = 0;
            srcYear = getSrcYearFromMessage(oldMessage);

            if(srcYear == 0){
                srcYear = pDestYear;
            }
            checkRuleTable(srcYear, pDestYear, pRuleTable);
            return pRuleTable.getCrgtMessage();
        } catch (Exception ex) {
            Logger.getLogger(RulesCodeChecker.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    public byte[] validateRule(long pPoolId, PoolTypeEn pType, CrgRules pRule) throws Exception{
        // for this validation we do not check table errors
        // when the rule has tables, it gets the message that the tables were not validated. We have to move old table messages into the new message 
        if(pRule.getCrgrDefinition() == null){
            pRule.setCrgrDefinition(rules4DbInteractBean.findRuleDefinition(pRule.getId(), pPoolId, pType));
        }
        CRGRule cpRule = RulesConverter.getCpRuleFromRuleDefinition(pRule);
        
        if(cpRule == null){
            return pRule.getCrgrMessage();
        }
        
        List<String> tables = cpRule.getAllRuleTables();
        Map<String, RuleTablesIF>  mapTables = new HashMap<>();
        if(tables != null && !tables.isEmpty()){
            List<CrgRuleTables> ruleTables = rules4DbInteractBean.findRuleTables4IdAsStringList(pPoolId, tables, pType);
            if (ruleTables != null && !ruleTables.isEmpty()){
                ruleTables.forEach((tab) -> {
                    mapTables.put(String.valueOf(tab.getId()), tab);
                });
            }
        }
        byte[] oldMessage = pRule.getCrgrMessage();

        int destYear = rules4DbInteractBean.getRulePool2Id(pPoolId, pType).getCrgplPoolYear();
        int srcYear = 0;

        srcYear = getSrcYearFromMessage(oldMessage);

        if(srcYear == 0){
            srcYear = destYear;
        }
        checkRule(cpRule, srcYear, destYear, mapTables);
        return cpRule.getErrorMessage();
    }

    private String stripMessageFromReason(String pMessage){
        String [] parts = pMessage.split(",");
        StringBuilder res = new StringBuilder();
        for(String part:parts){
            if(part != null && !part.isEmpty()){
                int ind = part.indexOf(":");
                res.append(part.substring(0, ind)).append(",");
            }
            
        }
        return res.toString();
    }

    public List<Long> validateRules4Table(long pPoolId, PoolTypeEn pType, long pTableId) throws Exception {
        CrgRuleTables table = rules4DbInteractBean.findRuleTable4StringId(String.valueOf(pTableId), pType, pPoolId);
        Map<String, RuleTablesIF>  mapTables = new HashMap<>();
        mapTables.put(String.valueOf(table.getId()), table);
        RuleTableMessage tabMsg = new RuleTableMessageReader().readUtf8SingleResultOrNull(table.getCrgtMessage());
        List<CrgRules> rules = rules4DbInteractBean.findFullRulesForTable(pPoolId, pType, pTableId);
        int year = rules4DbInteractBean.getRulePool2Id(pPoolId, pType).getCrgplPoolYear();
        List<Long> rules2update = new ArrayList<>();
        for(CrgRules rule: rules){
             List<RuleMessage> msgs = new RuleMessageReader().read(rule);
            if(msgs == null || msgs.isEmpty()){
                if( tabMsg != null){
                // validate and save rule

                    checkRule4Year(year, rule, mapTables);
                    rules4DbInteractBean.updateRule(rule, pPoolId, pType);
                    rules2update.add(rule.getId());
                }
                continue;
            }
            List<RuleMessage> newMessages = new ArrayList<>();
            boolean hasErrorTerm = false;
            for(RuleMessage msg:msgs){
                if(msg.getTerm().contains("@") &&(msg.getTerm().contains(String.valueOf(table.getId()))
                        || msg.getTerm().contains(table.getCrgtTableName()))){
                    hasErrorTerm = true;
                    if( tabMsg != null){
                        newMessages.add(msg);
                    }
                }else{
                    newMessages.add(msg);
                }
            }
            if(!hasErrorTerm){
                // validate and save rule
                checkRule4Year(year, rule, mapTables);
                 rules4DbInteractBean.updateRule(rule, pPoolId, pType);
                rules2update.add(rule.getId());
            }else{
                if(newMessages.isEmpty()){
                    rule.setCrgrMessage(null);
                }else{
                    if(newMessages.size() == msgs.size()){
                        continue;
                    }
                    RuleMessageBuilder msgBuilder = new RuleMessageBuilder();
                    RuleMessage[] arr = new RuleMessage[newMessages.size()];
                    msgBuilder.setAll(newMessages.toArray(arr));
                    rule.setCrgrMessage(msgBuilder.buildUtf8());
                }
                 rules4DbInteractBean.updateRule(rule, pPoolId, pType);
                rules2update.add(rule.getId());
            }

        }
        return rules2update;
    }

    private int getSrcYearFromMessage(byte[] pMessage) {
        try{
            if(pMessage == null){
                return 0;
            }
            return new RuleTableMessageReader().readUtf8AndGetSrcYear(pMessage);

        }catch(IOException ex){
            LOG.log(Level.SEVERE, "Error on converting of rule message to get srcYear of rule, will be set to 0", ex);
        }
        return 0;
    }
}
