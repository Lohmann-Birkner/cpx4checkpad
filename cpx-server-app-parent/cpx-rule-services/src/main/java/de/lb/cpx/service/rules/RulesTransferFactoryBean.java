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
import de.checkpoint.ruleGrouper.CRGRule;
import de.checkpoint.ruleGrouper.cpx.CPXFileManager;
import de.checkpoint.ruleGrouper.data.CRGRulePool;
import de.checkpoint.ruleGrouper.data.CRGRuleTypes;
import de.checkpoint.server.xml.XMLDOMWriter;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.commonDB.model.rules.RuleTablesIF;
import de.lb.cpx.server.config.CpxServerConfigLocal;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.text.MessageFormat;
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
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author gerschmann
 */
@Stateless
public class RulesTransferFactoryBean {

    private static final Logger LOG = Logger.getLogger(RulesTransferFactoryBean.class.getName());


    public enum TRANSFER_TYPE {
        IMPORT_INTERN, IMPORT_EXTERN, EXPORT_INTERN, EXPORT_EXTERN
    };
    private static final String EXTERNAL_IMPORT_PATH = "C:\\rules_import\\import\\";
    private static final String EXTERNAL_EXPORT_PATH = "C:\\rules_import\\export\\";

//    @EJB(beanName = "RuleReadServiceBean")
//    private RuleReadServicBeanLocal ruleStartUpbean;
    @EJB(name = "CpxServerConfig")
    private CpxServerConfigLocal cpxServerConfig;

    @Inject
//    TransferCodeCheckStore transferCodeCheckStore;
    RulesCodeChecker rulesCodeChecker;
    
    @PostConstruct
    public void init(){
        LOG.log(Level.INFO, "RulesTransferFactoryBean: for initialising of RulesCodeChecker");
    }


    /**
     * Gets rule types from the internal file csruletypes.xml on path
     * SW_SERVER/rules
     *
     * @param type type
     * @return list of crg rule types
     */
    public List<CRGRuleTypes> getRuleTypes(TRANSFER_TYPE type) {
        if (type.equals(TRANSFER_TYPE.IMPORT_INTERN)
                || type.equals(TRANSFER_TYPE.EXPORT_INTERN)//                && ruleStartUpbean != null
                ) {
            return ((CPXFileManager) (CPXFileManager.ruleManager())).getCpRuleTypes();
//                    ruleStartUpbean.getRuleTypes();
        }
        return new ArrayList<>();
    }

    /**
     * gets rules from the internal path SW_SERVER/rules/pool_year
     *
     * @param pool pool
     * @param year year
     * @param type type
     * @return list of crg rules
     */
    public List<CRGRule> getRules4Pool(String pool, int year, TRANSFER_TYPE type) {
        if (type == TRANSFER_TYPE.IMPORT_INTERN //                && ruleStartUpbean != null
                ) {
            return ((CPXFileManager) (CPXFileManager.ruleManager())).getRules4Pool(pool, year);
        }
        return new ArrayList<>();
    }

    /**
     * gets rule tables form internal path SW_SERVER/rules/pool_year/tables
     *
     * @param pool pool
     * @param year year
     * @param type type
     * @return rule tables for pool
     */
    public Map<String, RuleTablesIF> getRuleTables4Pool(String pool, int year, TRANSFER_TYPE type) {
        if (type == TRANSFER_TYPE.IMPORT_INTERN //                && ruleStartUpbean != null
                ) {
            return ((CPXFileManager) (CPXFileManager.ruleManager())).getRuleTables4Pool(pool, year);
//            return ruleStartUpbean.getRuleTables4Pool(pool, year);
        }
        return new HashMap<>();

    }


    public RulesImportHelper doRuleImportFromFile(String poolName, int year, 
            String fileName, String ruleString, Object object, long[] roleIdsList) throws Exception{
        return  doRuleImportFromFile(
             poolName, 
            year,
            fileName, 
            ruleString, 
            null, 
            roleIdsList,
            0,
            false);
    }

    /**
     * import of xml - rule file from EXTERNAL_IMPORT_PATH
     *
     * @param fileName file name
     * @param poolName pool name
     * @param year year
     * @return rules import helper
     * @throws Exception error
     */
    public synchronized RulesImportHelper doRuleImportFromFile(String poolName, int year, String fileName) throws Exception {
        return doRuleImportFromFile(poolName, year, fileName, null, null, null, 0, true);
    }

//    public synchronized RulesImportHelper doRuleImportFromFile(String poolName, int year, String fileName, String ruleString) throws Exception {
//        return doRuleImportFromFile(poolName, year, fileName, ruleString, null, null, year, false);
//    }
//
    public synchronized RulesImportHelper doRuleImportFromFile(
            String pPoolName, 
            int pDestYear,
            String pFileName, 
            String pRuleString, 
            List<String> pRuleIdents4import, 
            long[] pRoleIdList,
            int pSrcYear,
            boolean pDoCheck
    ) throws Exception {

        RulesImportHelper helper = pDoCheck?new RulesImportHelper(rulesCodeChecker):new RulesImportHelper();
        try {
            if ((pFileName == null || pFileName.isEmpty()) && (pRuleString == null || pRuleString.isEmpty())) {
                LOG.log(Level.WARNING, " there is no information to import");
                return null;

            }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document impDoc = null;
            if (pFileName != null && !pFileName.isEmpty()) {
                if (!pFileName.endsWith(".xml") && !pFileName.contains(".")) {
                    pFileName += ".xml";
                }
                File file = new File(EXTERNAL_IMPORT_PATH + pFileName);
                if (!file.exists()) {
                    LOG.log(Level.WARNING, " file " + EXTERNAL_IMPORT_PATH + pFileName + " not found");
                    return null;
                }
                String path = file.getAbsolutePath();
                URL url = new URL("file", null, path);

                impDoc = db.parse(url.openStream());

            } else {
                if (pRuleString != null && !pRuleString.isEmpty()) {
                    impDoc = db.parse(new ByteArrayInputStream(pRuleString.getBytes()));
                }
            }
            if (impDoc == null) {
                LOG.log(Level.WARNING, " there is no information to import");
                return null;

            }
            Element impRoot = impDoc.getDocumentElement();
            //is it checkpoint rules document?
            if(!helper.isRules(impRoot)){
                return null;
            }
//get rule types 
            helper.setCpRuleTypes(helper.createRuleTypesFromXml(impRoot));
// get rule tables
              helper.createTablesFromXml(impRoot, pDoCheck, pSrcYear, pDestYear);
//            if(pDoCheck){
//                Collection<CpTable> tabs = cpTables.values();
//                tabs.stream().filter((tab) -> ( tab.getCategory().equals(RuleTableCategoryEn.ICD) ||  tab.getCategory().equals(RuleTableCategoryEn.OPS))).forEachOrdered((tab) -> {
//                    transferCodeCheckStore.checkTable(pSrcYear, pDestYear, tab);
//                });
//            }
 //           helper.setCpTables(cpTables);
// get rules
            List<CRGRule> cpRules = helper.createRulesFromXml(impRoot, pPoolName, pDestYear, helper.getCpRuleTypes(),
                    pRuleIdents4import, pRoleIdList, pSrcYear, pDoCheck);
            helper.setCpRules(cpRules);

            return helper;
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, " error on reading " + EXTERNAL_IMPORT_PATH + pFileName, ex);
            return null;
        }
    }

    public synchronized RulesExchangeHelper doRulesExportInFile(List<CrgRules> rules,
            List<CrgRuleTables> usedTables,
            List<CrgRuleTypes> usedTypes, int poolYear) throws Exception {
        
            RulesExchangeHelper helper = new RulesExchangeHelper();
            helper.setRulesYear(poolYear);
        return doRulesExportInFile(rules,
                helper,
                TRANSFER_TYPE.EXPORT_EXTERN,
                null,
                usedTables,
                usedTypes);
    }

    /**
     * performs export of the rules list into file on the defined path
     * EXTERNAL_EXPORT_PATH
     *
     * @param fileName file name
     * @param rules rules
     * @return rules exchange helper
     * @throws Exception error
     */
    public synchronized RulesExchangeHelper doRulesExportInFile(List<CrgRules> rules, String fileName, int poolYear) throws Exception {
        File xmlFile = null;
        RulesExchangeHelper helper = new RulesExchangeHelper();
        helper.setRulesYear(poolYear);
        if (fileName != null && !fileName.isEmpty()) {
            if (fileName.endsWith(".xml")) {
                fileName = fileName.substring(0, fileName.indexOf(".xml"));
            }
            xmlFile = new File(EXTERNAL_EXPORT_PATH + fileName + ".xml");
            int count = 0;
            while (xmlFile.exists()) {

                xmlFile = new File(EXTERNAL_EXPORT_PATH + fileName + "(" + String.valueOf(++count) + ").xml");

            }

            helper.addLoggerText("we save in " + xmlFile.getAbsolutePath());
        } else {
            helper.addLoggerText("we save temporary in helper for return as string");
        }
        return doRulesExportInFile(rules, helper, TRANSFER_TYPE.EXPORT_EXTERN, xmlFile);

    }

    private synchronized RulesExchangeHelper doRulesExportInFile(List<CrgRules> rules,
            RulesExchangeHelper helper,
            TRANSFER_TYPE type,
            File xmlFile) throws Exception {
        return doRulesExportInFile(rules, helper, type, xmlFile, null, null);
    }

    private synchronized RulesExchangeHelper doRulesExportInFile(List<CrgRules> rules,
            RulesExchangeHelper helper,
            TRANSFER_TYPE type,
            File xmlFile,
            List<CrgRuleTables> usedTables,
            List<CrgRuleTypes> usedTypes) throws Exception {

        //FileOutputStream fos = null;
        //DataOutputStream output = null;
        if (xmlFile == null) {
            helper.addLoggerText("xmlFile is null");
        }
        String fileName = xmlFile == null ? "temporary save" : xmlFile.getAbsolutePath();
        try {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
// rules            
            if (type.equals(TRANSFER_TYPE.EXPORT_EXTERN)) {
                doc = RulesExportHelper.createExportRuleFile(doc, rules, helper, true, usedTables, usedTypes);
            } else {

                doc = RulesExportHelper.createInternalRuleFile(doc, rules, helper);

            }
            if (doc != null) {
                OutputStream os = new ByteArrayOutputStream();
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(os,
                        "UTF-16"));

                XMLDOMWriter.setWriterEncoding("UTF-16");
                XMLDOMWriter.print(doc, pw, false);

                if (xmlFile != null) {
                    try (FileOutputStream fos = new FileOutputStream(xmlFile); DataOutputStream output = new DataOutputStream(fos)) {

                        output.writeBytes(os.toString());
                    }
                } else {
                    helper.saveXmlString(os.toString());
                }
            } else {
                helper.addLoggerText(" error on writing " + fileName);
            }

        } catch (IOException | ParserConfigurationException ex) {
            LOG.log(Level.SEVERE, MessageFormat.format(" error on writing {0}", fileName), ex);
            helper.addLoggerText(" error on writing " + fileName + " " + ex.getMessage());
            throw ex;
        }
        return helper;
    }

    /**
     * saves all rules for pool on the internal path. All old rules and all
     * tables will be saved in backup directory
     *
     * @param poolName pool name
     * @param year year
     * @param crgRules rules
     * @return rules exchange helper
     */
    public RulesExchangeHelper doExportInternalRules(String poolName, int year, List<CrgRules> crgRules) {
        RulesExchangeHelper helper = new RulesExchangeHelper();
        helper.setRulesYear(year);
        try {
// create backup directory
//            if (ruleStartUpbean == null) {
//                helper.addLoggerText("ruleStartUpbean is null");
//                return helper;
//            }
            File destDir = ((CPXFileManager) (CPXFileManager.ruleManager())).createBackupRulesDirectory(poolName, year);
//                    ruleStartUpbean.createBackupRulesDirectory(poolName, year); 
            if (destDir == null) {
                helper.addLoggerText(" error on creating backup directory");
                return helper;
            }
            String path = destDir.getAbsolutePath() + File.separator + CPXFileManager.getPoolIdentifier(poolName, year) + "csrules.xml";

// create rules document and save rules
            try {
                helper = doRulesExportInFile(crgRules, 
                        helper, 
                        TRANSFER_TYPE.IMPORT_INTERN, 
                        new File(path));
            } catch (Exception ex) {
                LOG.log(Level.WARNING, "error on writing inernal rules", ex);
                return helper;
            }

// save tables
//            if(ruleStartUpbean.saveRuleTables4Pool(destDir, helper.getRuleTables4Export()))
            if (((CPXFileManager) (CPXFileManager.ruleManager())).saveRuleTables4Pool(destDir, helper.getRuleTables4Export())) {
                helper.addLoggerText("rules for pool " + poolName + " year " + String.valueOf(year) + " saved");
                helper = checkDoRefresh(poolName, year, TRANSFER_TYPE.EXPORT_INTERN, helper);
                helper.addLoggerText(" rules refreshed");
            } else {
                helper.addLoggerText(" error on saving of rule tables in pool " + poolName + " year " + String.valueOf(year));
            }
            return helper;
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, " error on saving rules in server work space", ex);
            helper.addLoggerText(" error on saving rules in server work space");
            return helper;
        }
    }

    /**
     * after rules update checks what rule configuration uses RuleReadBean in
     * order to update its rules
     *
     * @param poolName pool name
     * @param year year
     * @param type type
     * @param helper helper
     * @return rules exchange helper
     */
    public RulesExchangeHelper checkDoRefresh(String poolName, int year, TRANSFER_TYPE type, RulesExchangeHelper helper) {
        try {
//            if (cpxServerConfig.getRulesDatabaseConfig()) {
//                LOG.log(Level.INFO, " we use rules from database");
//                if (type.equals(TRANSFER_TYPE.EXPORT_EXTERN) || type.equals(TRANSFER_TYPE.IMPORT_INTERN)) {
//                    ruleStartUpbean.reloadRules4Pool(poolName, year);
//                }
//
//            } else {
//                if (type.equals(TRANSFER_TYPE.EXPORT_INTERN)) {
//                    ruleStartUpbean.reloadRules4Pool(poolName, year);
//                }
//                LOG.log(Level.INFO, " we read all rules for all roles from Path ");
//            }
            return helper;
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, " cannot update rules for grouper after rules import", ex);
            helper.addLoggerText(" cannot update rules for grouper after rules import" + ex.getMessage());
            return helper;
        }
    }

//    public String doExportRulesAsString(String poolName, int poolYear, List<CrgRules> rules){
//        
//    }
    public List<CRGRulePool> getRulePoolsIntern() {
        try {
            if (!cpxServerConfig.getRulesDatabaseConfig()) {
                return ((CPXFileManager) (CPXFileManager.ruleManager())).getRulePools();

            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, " cannot read intern rule pools from file system", ex);

        }
        return new ArrayList<>();
    }
}
