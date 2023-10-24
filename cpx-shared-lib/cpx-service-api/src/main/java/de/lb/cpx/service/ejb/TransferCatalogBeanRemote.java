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
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author wilde
 */
@Remote
public interface TransferCatalogBeanRemote {
    
    /**
     * @param pCatalogCode code set in specific Catalog
     * @param pCategory catalog category 
     * @param pSourceYear initial year from which the catalog data originated
     * @param pDestinationYear target year in which the catalog should transfered
     * @param pRuleTableId ruleTableId in which the catalog code is set
     * @return String representation of all possible suggestions valid for this code - null if there is no suggestion
     */
    String getCodeSuggestion(String pCatalogCode,RuleTableCategoryEn pCategory, int pSourceYear, int pDestinationYear, long pRuleTableId);
    
    /**
     * @param pCatalogCode code set in specific Catalog
     * @param pSourceYear initial year from which the catalog data originated
     * @param pDestinationYear target year in which the catalog should transfered
     * @param pRuleTableId ruleTableId in which the catalog code is set
     * @return String representation of all possible suggestions valid for this icd code - null if there is no suggestionuleTableId
     */
    String getIcdCodeSuggestion(String pCatalogCode, int pSourceYear, int pDestinationYear, long pRuleTableId);
    
    /**
     * @param pCatalogCode code set in specific Catalog
     * @param pSourceYear initial year from which the catalog data originated
     * @param pDestinationYear target year in which the catalog should transfered
     * @param pRuleTableId ruleTableId in which the catalog code is set
     * @return String representation of all possible suggestions valid for this ops code - null if there is no suggestionuleTableId
     */
    String getOpsCodeSuggestion(String pCatalogCode, int pSourceYear, int pDestinationYear, long pRuleTableId);
    
    /**
     * @param pContent content in byte to be imported
     * @param pSourceYear initial year
     * @param pDestinationYear destination year
     * @return indicator if import was successful
     */
    Boolean importCatalogTransferTable(Byte[] pContent, int pSourceYear, int pDestinationYear);
    
    /**
     * @param pCategory catalog to get transfer table for
     * @param pSourceYear destination year
     * @param pDestinationYear target year
     * @return string representation of the content of the transfer table
     */
    String getTransferTableContent(RuleTableCategoryEn pCategory, int pSourceYear, int pDestinationYear);
    
    /**
     * @param pSourceYear destination year
     * @param pDestinationYear target year
     * @return string representation of the content of the transfer table
     */
    String getIcdTransferTableContent(int pSourceYear, int pDestinationYear);
    
    /**
     * @param pSourceYear destination year
     * @param pDestinationYear target year
     * @return string representation of the content of the transfer table
     */
    String getOpsTransferTableContent(int pSourceYear, int pDestinationYear);
    
    /**
     * @param pCatalogCode code set in specific Catalog
     * @param pCategory catalog category
     * @param pSourceYear destination year
     * @param pDestinationYear target year
     * @param pRuleTableId ruleTableId in which the catalog code is set
     * @return indicator if the catalog code has an suggestion in the corresponding transfer table
     */
    boolean hasCodeSuggestion(String pCatalogCode, RuleTableCategoryEn pCategory, int pSourceYear, int pDestinationYear, long pRuleTableId);
    
    /**
     * @param pCatalogCode code set in specific Catalog
     * @param pSourceYear destination year
     * @param pDestinationYear target year
     * @param pRuleTableId ruleTableId in which the catalog code is set
     * @return indicator if the catalog code has an suggestion in the corresponding icd transfer table
     */
    boolean hasIcdCodeSuggestion(String pCatalogCode, int pSourceYear, int pDestinationYear, long pRuleTableId);
    
    /**
     * @param pCatalogCode code set in specific Catalog
     * @param pSourceYear destination year
     * @param pDestinationYear target year
     * @param pRuleTableId ruleTableId in which the catalog code is set
     * @return indicator if the catalog code has an suggestion in the corresponding ops transfer table
     */
    boolean hasOpsCodeSuggestion(String pCatalogCode, int pSourceYear, int pDestinationYear, long pRuleTableId);
    
    /**
     * @param pYear
     * @param pRuleTable
     * @return byte representation of possible rule table messages - null if ruleTable contains no errors
     * @throws java.lang.Exception
     */
    public byte[] validateRuleTable(int pYear, CrgRuleTables pRuleTable)  throws Exception;

    /**
     *
     * @param pPoolId pool id
     * @param pYear pool year
     * @param pType pool type
     * @param pRule rule
     * @return rule message
     * @throws Exception
     */
    public byte[] validateRule(long pPoolId, PoolTypeEn pType, CrgRules pRule)  throws Exception;
    
    public List<Long> validateRules4Table(long pPoolId, PoolTypeEn pType, long pTableId) throws Exception;
}
