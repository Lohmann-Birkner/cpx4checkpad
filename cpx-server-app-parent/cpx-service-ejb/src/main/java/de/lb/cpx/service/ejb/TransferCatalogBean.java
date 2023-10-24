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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.lb.cpx.server.commonDB.model.enums.RuleTableCategoryEn;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.service.readrules.codeschecker.RulesCodeChecker;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author wilde
 */
@Stateless
public class TransferCatalogBean implements TransferCatalogBeanRemote {
//    
//    @Inject
//    TransferCodeCheckStore transferCodeCheckStore;
    
    @EJB 
    private RulesCodeChecker ruleCodeChecker;
    
    private static final Logger LOG = Logger.getLogger(TransferCatalogBean.class.getName());
    
    
   @PostConstruct
    public void init(){
        LOG.log(Level.INFO, "TransferCatalogBean: for initialising of TransferCodeCheckStore");
    }

    
    @Override
    public String getCodeSuggestion(String pCatalogCode, RuleTableCategoryEn pType, int pSourceYear, int pDestinationYear, long pRuleTableId) {
        return ruleCodeChecker.checkTransferCode(pType, pSourceYear, pDestinationYear, pCatalogCode);
    }
    
    @Override
    public String getIcdCodeSuggestion(String pCode, int pSourceYear, int pDestinationYear, long pRuleTableId) {
        return getCodeSuggestion(pCode, RuleTableCategoryEn.ICD, pSourceYear, pDestinationYear, pRuleTableId);
    }
    
    @Override
    public String getOpsCodeSuggestion(String pCode, int pSourceYear, int pDestinationYear, long pRuleTableId) {
        return getCodeSuggestion(pCode, RuleTableCategoryEn.OPS, pSourceYear, pDestinationYear, pRuleTableId);
    }

    @Override
    public Boolean importCatalogTransferTable(Byte[] pContent, int pSourceYear, int pDestinationYear) {
        return false;
    }
    
    @Override
    public String getTransferTableContent(RuleTableCategoryEn pCategory, int pSourceYear, int pDestinationYear) {
        return "1-520.7,*.*,2-098.*";
    }
    
    @Override
    public String getIcdTransferTableContent(int pSourceYear, int pDestinationYear) {
        return getTransferTableContent(RuleTableCategoryEn.ICD, pSourceYear, pDestinationYear);
    }
    @Override
    public String getOpsTransferTableContent(int pSourceYear, int pDestinationYear) {
        return getTransferTableContent(RuleTableCategoryEn.OPS, pSourceYear, pDestinationYear);
    }
    
    @Override
    public boolean hasCodeSuggestion(String pCatalogCode, RuleTableCategoryEn pCategory, int pSourceYear, int pDestinationYear, long pRuleTableId) {
        return getCodeSuggestion(pCatalogCode, pCategory, pSourceYear, pDestinationYear, pRuleTableId)!= null;
    }
    
    @Override
    public boolean hasIcdCodeSuggestion(String pCatalogCode, int pSourceYear, int pDestinationYear, long pRuleTableId) {
        return hasCodeSuggestion(pCatalogCode, RuleTableCategoryEn.ICD, pSourceYear, pDestinationYear, pRuleTableId);
    }
    
    @Override
    public boolean hasOpsCodeSuggestion(String pCatalogCode, int pSourceYear, int pDestinationYear, long pRuleTableId) {
        return hasCodeSuggestion(pCatalogCode, RuleTableCategoryEn.OPS, pSourceYear, pDestinationYear, pRuleTableId);
    }

    @Override
    public byte[] validateRuleTable(int pYear, CrgRuleTables pRuleTable) throws Exception {
        return ruleCodeChecker.validateRuleTable(pYear, pRuleTable);
    }

    @Override
    public byte[] validateRule(long pPoolId, PoolTypeEn pType,  CrgRules pRule)  throws Exception{
        // get whole rule - ca not get database object changes to could or could not be commited already!
        return ruleCodeChecker.validateRule(pPoolId, pType, pRule);
    }

    @Override
    public List<Long> validateRules4Table(long pPoolId, PoolTypeEn pType, long pTableId) throws Exception {
        return  ruleCodeChecker.validateRules4Table(pPoolId, pType, pTableId);
    }
    
}
