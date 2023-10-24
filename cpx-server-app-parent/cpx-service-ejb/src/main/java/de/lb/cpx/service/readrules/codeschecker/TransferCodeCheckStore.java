/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.readrules.codeschecker;

import de.checkpoint.ruleGrouper.CheckpointRuleGrouper;
import de.lb.cpx.server.commonDB.dao.CIcdTransferCatalogDao;
import de.lb.cpx.server.commonDB.dao.COpsTransferCatalogDao;
import de.lb.cpx.server.commonDB.dao.CTransferCatalogDao;
import de.lb.cpx.server.commonDB.model.enums.RuleTableCategoryEn;
import de.lb.cpx.server.commonDB.model.rules.CTransferCatalog;
import de.lb.cpx.shared.json.enums.MessageReasonEn;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author gerschmann
 */
@ApplicationScoped
@Local
public class TransferCodeCheckStore {

    private static final Logger LOG = Logger.getLogger(TransferCodeCheckStore.class.getName());

    
    @EJB
    private CIcdTransferCatalogDao icdTransferCatalogDao;
    
    @EJB
    private COpsTransferCatalogDao opsTransferCatalogDao;
    
    private static final Map<CodeYearCheckEntry, TreeSet<String>>  CODE_YEAR_CHECK_MAP = new HashMap<>();
    private static final Map<CodeTransferCheckEntry, TreeSet<CTransferCatalog>> CODE_TRANSFERCHECK_MAP = new HashMap<>();
    private static final List<Character> OPS_CATEGORIES = new ArrayList<>(){
        {
         add('1');
         add('3');
         add('5');
         add('6');
         add('8');
          add('9');
       }};

    private CTransferCatalogDao<? extends CTransferCatalog> getDao(RuleTableCategoryEn pType) {
        switch(pType){
            case ICD:
                return icdTransferCatalogDao; 
            case OPS:
                return opsTransferCatalogDao; 
            default: return null;
        }
    }

    protected TreeSet<CTransferCatalog> getTransferCatalog(CodeTransferCheckEntry entry) {
         TreeSet<CTransferCatalog> transferCatalog = CODE_TRANSFERCHECK_MAP.get(entry);
         if(transferCatalog == null){
             CTransferCatalogDao dao = getDao(entry.mType);
             if(dao != null){
                transferCatalog = dao.getTransferCatalog4Years(entry.mSrcYear, entry.mDestYear);
                CODE_TRANSFERCHECK_MAP.put(entry, transferCatalog);
             }
         }
         return transferCatalog;
    }

    private boolean check4WildCard(String pCode, int indp, RuleTableCategoryEn pType) {

           char ch = pCode.charAt(0);
           if(pType.equals(RuleTableCategoryEn.ICD) && !Character.isLetter(ch) && !Character.isUpperCase(ch)){
               return false;
           }
           if(pType.equals(RuleTableCategoryEn.OPS) && !OPS_CATEGORIES.contains(ch) ){
               return false;
           }
       

        if( indp > 0 ){
            pCode = pCode.substring(0, indp);
        }
        int len = pCode.length();
        if( pType.equals(RuleTableCategoryEn.ICD)){
            // check point; if there than false
            if(pCode.contains(".")){
                return len >= 3;
            }
        }
        if(pType.equals(RuleTableCategoryEn.OPS) && len > 1){
            boolean ret = pCode.indexOf("-") == 1;
            if(len > 5){
                ret = ret && pCode.indexOf(".") == 5;
            }
            return ret;
        }
//        if(len <= 3){
//            // todo check with catalog
//            return true;// 5-1% A01%
//        }

        return true;
    }

    public class CodeYearCheckEntry{
        private final int mYear;
        private final RuleTableCategoryEn mType;
        
        public CodeYearCheckEntry(int pYear, RuleTableCategoryEn pType){
            mYear = pYear;
            mType = pType;
        }
        
        /**
         *
         * @param obj
         * @return
         */
        @Override
        public boolean equals(Object obj){
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final CodeYearCheckEntry other = (CodeYearCheckEntry) obj;
            
            return this.mYear==other.mYear && this.mType.equals(other.mType);
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 97 * hash + this.mYear;
            hash = 97 * hash + Objects.hashCode(this.mType);
            return hash;
        }
    }
     
    public class CodeTransferCheckEntry implements Comparable<CodeTransferCheckEntry>{
        
        private final RuleTableCategoryEn mType;
        private final int mSrcYear;
        private final int mDestYear;
        
        public CodeTransferCheckEntry(int pSrcYear, int pDestYear, RuleTableCategoryEn pType){
            mType = pType;
            mSrcYear = pSrcYear;
            mDestYear = pDestYear;
        }
        
        @Override
        public boolean equals(Object obj){
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final CodeTransferCheckEntry other = (CodeTransferCheckEntry) obj;
            
            return this.mSrcYear==other.mSrcYear && this.mType.equals(other.mType) && this.mDestYear == other.mDestYear;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 67 * hash + Objects.hashCode(this.mType);
            hash = 67 * hash + this.mSrcYear;
            hash = 67 * hash + this.mDestYear;
            return hash;
        }

        @Override
        public int compareTo(CodeTransferCheckEntry o) {
            if(this.equals(o)){
                return 0;
            }
            if(this.mSrcYear < o.mSrcYear){
                return -1;
            }
            if(this.mSrcYear > o.mSrcYear){
                return 1;
            
            }
            if(this.mDestYear < o.mDestYear){
                return -1;
                
            }
            if(this.mDestYear > o.mDestYear){
                return 1;
            }
            
            return 0;
        }        
    }
    
    
    protected TreeSet<String> getCodes(CodeYearCheckEntry entry, RuleTableCategoryEn pType, int pYear ){
                TreeSet<String> codes = CODE_YEAR_CHECK_MAP.get(entry);
        if(codes == null){
            CTransferCatalogDao dao = getDao(pType);
            if(dao == null){
                return null;// we don't check other types
            }
            codes = dao.getValidCodes4Year( pYear);// codes cannot be null, only empty

            CODE_YEAR_CHECK_MAP.put(entry, codes);
        }
        return codes;
    }
//    
//    public boolean checkCode(RuleTableCategoryEn pType, int pSrcYear, int pDestYear, String pCode, List<RuleTableMessage> pMessages) throws Exception{
//        if(pSrcYear == pDestYear){
//            return checkCode(pType, pSrcYear, pCode);
//        }else{
//            return checkTransferCode(pType, pSrcYear, pDestYear, pCode, pMessages);
//        }
//        // TODO: checkTransfer
//
//    }
    
    /**
     * checks code for specifffic year
     * @param pType
     * @param pYear
     * @param pCode
     * @return
     * @throws Exception 
     */
    public boolean checkCode(RuleTableCategoryEn pType, int pYear, String pCode) throws Exception{
       CodeYearCheckEntry entry = new CodeYearCheckEntry(pYear, pType); 
       TreeSet<String> codes = getCodes(entry, pType, pYear);
       if (codes == null){
           return true;
       }

//Map<CodeYearCheckEntry, TreeSet<String>>  CODE_YEAR_CHECK_MAP = new HashMap<>();
         if(codes.isEmpty()){
            return true;
        }
                
         
        boolean fromStart = true;
//        boolean checkParts = false;
        if(pCode.startsWith("%") || pCode.startsWith("?")){
            fromStart = false;
        }
        if(pCode.equals("%")){
            return true;
        }
                // TODO: check first sign of code A-Z for ICDs and 1-9 for OPS
        int indp = pCode.indexOf("%");
        int indq = pCode.indexOf("?");

//        if(fromStart){
//           char ch = pCode.charAt(0);
//           if(pType.equals(RuleTableCategoryEn.ICD) && !Character.isLetter(ch)){
//               return false;
//           }
//           if(pType.equals(RuleTableCategoryEn.OPS) && !Character.isDigit(ch)){
//               return false;
//           }
//       }
//
//        if(fromStart && indp > 0 ){
//            if(indp < 3 && pType.equals(RuleTableCategoryEn.ICD)){
//                // check point; if there than false
//                return !pCode.contains(".");
//            }
//            if(pType.equals(RuleTableCategoryEn.OPS) && indp > 1){
//                boolean ret = pCode.indexOf("-") == 1;
//                if(indp > 4){
//                    ret = ret && pCode.indexOf(".") == 4;
//                }
//                return ret;
//            }
//            if(indp < 3){
//                return true;// 5-1% A01%
//            }
//        }
//        
       
        if(fromStart){
            if( !check4WildCard(pCode, indp, pType)){
                return false;
            }
        }

        int ind = fromStart?((indp > 0 && indq > 0)?Math.min(indp, indq):(indp > 0?indp:(indq > 0?indq:pCode.length()- 1))):pCode.toLowerCase().length()- 1;

        NavigableSet<String> checkCodes = fromStart?codes.tailSet(pCode.substring(0, ind),true):codes.headSet(codes.last(), true);
        if(checkCodes == null){
            checkCodes = codes;
        }
        Iterator<String> itr = checkCodes.iterator();
        while(itr.hasNext()){
            String test = itr.next().toUpperCase();
            if(CheckpointRuleGrouper.compareStringValue(test, pCode.toUpperCase())){
                return true;
            }
            if(fromStart &&(test.charAt(0) > pCode.charAt(0))){
                break;
            }
        }
        return false;

    }
    
    
    public String checkTableContent(RuleTableCategoryEn pType, int srcYear, int destYear, String pContent) throws Exception{

        if(pContent ==null ||
                pContent.trim().isEmpty()
                ){
            return "";// we check only same or next year now
        }
        StringBuilder message = new StringBuilder();
        StringBuilder description = new StringBuilder();
        String[] parts = pContent.split(",");

            for(String code:parts){
                if(code == null || code.trim().isEmpty()){
                    continue;
                }
                if(srcYear == destYear){
                    if(!checkCode(pType, destYear, code.trim())) {
                        message.append(code).append(":").append(MessageReasonEn.VALIDATION_NO_VALUE.getIndexString())
                                .append(",");
                    }
                }else{
                    String res = checkTransferCode(pType, srcYear, destYear, code.trim(), description);
                    if(!res.isEmpty()){
                         message.append(code).append(":").append(MessageReasonEn.VALIDATION_CHECK_CATALOG_TRANSFER_TABLE.getIndexString())
                                 .append(",");
                    }else if(!checkCode(pType, destYear, code.trim())) {
                        message.append(code).append(":").append(MessageReasonEn.VALIDATION_NO_VALUE.getIndexString())
                                .append(",");
                        description.append(code).append("->").append(MessageReasonEn.VALIDATION_NO_VALUE.getTranslation().getValue()).append(";\r\n");
                    }
                }

            }
            String ret = message.toString();
            if(ret.endsWith(",")){
                ret = ret.substring(0,ret.lastIndexOf(","));
            }
            if(!ret.isEmpty()){
                ret += "<>" + description.toString();
            }
            return ret;
        }



    
    public String checkTransferCode(RuleTableCategoryEn pType, int pSrcYear, int pDestYear,
            String pCode) {
        return checkTransferCode(pType,  pSrcYear,  pDestYear,
             pCode, null);
    }
    
    public String checkTransferCode(RuleTableCategoryEn pType, int pSrcYear, int pDestYear,
            String pCode, StringBuilder pDescription) {
        if(RuleTableCategoryEn.getType2CatalogType(pType) == null){ 
            return "";// would not be checked
        }
        CodeTransferCheckEntry entry = new CodeTransferCheckEntry(pSrcYear, pDestYear, pType);
        TreeSet<CTransferCatalog> transferCatalog  = getTransferCatalog(entry);
         try{
             if(transferCatalog.isEmpty()){// we do not have the transfer tables for pSrcYear, we check the validity of code in destination year only
                if(!checkCode(pType, pDestYear, pCode)) {
                    return pCode;
                }
                return "";
             }

        boolean fromStart = true;
//        boolean checkParts = false;
        if(pCode.startsWith("%") || pCode.startsWith("?")){
            fromStart = false;
        }
        if(pCode.equals("%")){
            return "";
        }
        
        CTransferCatalog checkEntry = CTransferCatalog.getTypeInstance(RuleTableCategoryEn.getType2CatalogType(pType));
        int indp = pCode.indexOf("%");
        int indq = pCode.indexOf("?");
//        if(fromStart && indp > 0 && indp <= 3){
//            return "";// 5-1% A01%
//        }
        if(fromStart && !check4WildCard(pCode, indp, pType)){
            return "";
        }
        int ind = fromStart?((indp > 0 && indq > 0)?Math.min(indp, indq):(indp > 0?indp:(indq > 0?indq:pCode.length()- 1))):pCode.length()- 1;
        checkEntry.setSrcCode(pCode.substring(0, ind));
        checkEntry.setSrcYear(pSrcYear);
        checkEntry.setDestYear(pDestYear);
        checkEntry.setDestCode(checkEntry.getSrcCode());
        NavigableSet<CTransferCatalog> checkCodes = fromStart?transferCatalog.tailSet(checkEntry,true):transferCatalog.headSet(transferCatalog.last(), true);
        if(checkCodes == null){
            checkCodes = transferCatalog;
        }
        StringBuilder codes = new StringBuilder();
        Iterator<CTransferCatalog> itr = checkCodes.iterator();
        StringBuffer buf = new StringBuffer();
        while(itr.hasNext()){
            CTransferCatalog testEntry =  itr.next();
            String test = testEntry.getSrcCode().toUpperCase();
            if(CheckpointRuleGrouper.compareStringValue(test, pCode)){
                while(itr.hasNext() && CheckpointRuleGrouper.compareStringValue(test, pCode)){

                    if(!testEntry.getSrcCode().equals(testEntry.getDestCode())){
                        if(buf.length() == 0){
                           buf.append(pCode).append(":\r\n");
                        }
                        codes.append(testEntry.getDestCode()).append(",");

                        buf.append(testEntry.getSrcCode()).append("->").append(testEntry.getDestCode()).append(";\r\n");
                        
                    }
                    testEntry =  itr.next();
                    test = testEntry.getSrcCode().toUpperCase();
                }
                if(codes.length() >0 && (pCode.contains("%") ||pCode.contains("?"))){
                    codes.insert(0, pCode + ",");
                }
                if(pDescription != null && buf.toString().length() > 0){
                    pDescription.append(buf).append("\n\r");
                }
                return codes.toString();
            }
            if(fromStart &&(test.charAt(0) > pCode.charAt(0))){
                break;
            }
        }
         }catch(Exception ex){
             LOG.log(Level.SEVERE, " error on check of transfer codes", ex);
         }
        return "";
    }
    
}
