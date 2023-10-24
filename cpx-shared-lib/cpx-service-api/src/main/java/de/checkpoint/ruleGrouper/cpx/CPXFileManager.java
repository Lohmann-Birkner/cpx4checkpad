/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  gerschmann - initial API and implementation and/or initial documentation
 */
package de.checkpoint.ruleGrouper.cpx;

import de.checkpoint.ruleGrouper.CRGCheckpointGrouperFileManager;
import de.checkpoint.ruleGrouper.CRGRule;
import de.checkpoint.ruleGrouper.CRGRuleGroupException;
import de.checkpoint.ruleGrouper.data.CRGRulePool;
import de.checkpoint.ruleGrouper.data.CRGRuleTypes;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.RuleTablesIF;
import de.lb.cpx.shared.rules.util.CpTable;
import de.lb.cpx.shared.rules.util.CpxCRGRule;
import de.lb.cpx.shared.rules.util.CpxRuleManagerIF;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author gerschmann
 */
public class CPXFileManager extends CRGCheckpointGrouperFileManager implements CpxRuleManagerIF {

    private static final Logger LOG = Logger.getLogger(CPXFileManager.class.getName());

    public CPXFileManager() {
        super();
    }

    /**
     * overrides the method of CRGCheckpointGrouperFileManager to initialize the
     * file manager CPXFileManager
     *
     * @return RulefileManager
     */
    public static synchronized CRGCheckpointGrouperFileManager ruleManager() {
        if (m_ruleManager == null) {
            m_ruleManager = new CPXFileManager();
        }
        return m_ruleManager;
    }

    /**
     * overrides this method in order to skip all back_up directories
     *
     * @param rulePath rule path
     * @param xmlRuleFile xml rule file
     * @return crg rules
     * @throws Exception error
     */
    @Override
    public synchronized CRGRule[] getRulesForFile(String rulePath, InputStream xmlRuleFile) throws Exception {
        if (rulePath.contains("_backup")) {
            return null;
        }
        return super.getRulesForFile(rulePath, xmlRuleFile);
    }

    /**
     *
     * @param path path to Rules on the File System
     * @return get InputStream for Rules
     */
    @Override
    protected InputStream getRulesInputStream(String path) {
        InputStream is = super.getRulesInputStream(path);
        if (is == null) {
            try {
                is = new FileInputStream(path);
            } catch (FileNotFoundException ex) {
                LOG.log(Level.SEVERE, "rule table not found: {0}", path);
                LOG.log(Level.FINEST, null, ex);
            }
        }
        return is;
    }

    /**
     * overrides the getXMLDocument method, so that it does not need to be
     * staticas it is in CRGCheckpointGrouperFileManager
     *
     * @param is InputStream of the XML files
     * @return XML Document
     * @throws java.lang.Exception throws Exception when Document can not be
     * build
     */
    @Override
    protected Document getXMLDocument(InputStream is) throws Exception {
        if (is != null) {
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                DocumentBuilder db = dbf.newDocumentBuilder();
//				FileInputStream fin = new FileInputStream(file);
                Document doc = db.parse(is);
                return doc;
            } catch (ParserConfigurationException | SAXException | IOException ex) {
                throw new CRGRuleGroupException("error by building XML Document", ex);
            }
        }
        return null;
    }

    /**
     * returns rulePath
     *
     * @return rulePath on FileSystem
     */
    @Override
    protected synchronized String getRulesPath() {
        if (m_rulesPath.isEmpty()) {
// get rules path from properties                
            CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
            m_rulesPath = cpxProps.getCpxServerRulesDir();
        }
        return m_rulesPath;

    }

    @Override
    public CRGRule[] getAllRulesFromPath(String rulesPath, long[] roles) throws Exception {
        CRGRule[] ret = super.getAllRulesFromPath(rulesPath, roles);
//        saveTestData(ret);
        return ret;
    }

    /**
     * we get the internal structure of rules without its tables
     *
     * @param pool pool
     * @param year year
     * @return list of crg rules
     */
    public List<CRGRule> getRules4Pool(String pool, int year) {
        try {
            String path = this.getRulesPath() + pool;
            if (!pool.equals(String.valueOf(year))) {
                path += "_" + String.valueOf(year);
            }
            File rDir = new File(path);
            if (rDir.isDirectory()) {
                String[] ruleFiles = rDir.list();
                if (ruleFiles == null) {
                    LOG.log(Level.WARNING, "no rule files on path {0} found", path);
                    return null;
                }
                List<CRGRule> retList = new ArrayList<>();
                for (String ruleFile : ruleFiles) {
                    if (!ruleFile.endsWith(m_rulesName)) {
                        continue;
                    }
                    InputStream is = getRulesInputStream("/" + path + File.separator + ruleFile);
                    CRGRule[] rules = getRulesForFile(path, is, CRGRule.RULE_ANALYSE_NO_TABLES);
                    retList.addAll(Arrays.asList(rules));
                }
                return retList;

            } else {
                return null;
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, " error on reading rules for pool " + pool + " year " + year, ex);
            return null;
        }
    }

    public Map<String, RuleTablesIF> getRuleTables4Pool(String pool, int year) {

        Map<String, RuleTablesIF> lRetMap = new HashMap<>();
        try {
            String path = this.getRulesPath() + pool;
            if (!pool.equals(String.valueOf(year))) {
                path += "_" + String.valueOf(year);
            }
            File rDir = new File(path + File.separator + "table");
            if (rDir.isDirectory()) {
                String[] tableFiles = rDir.list();
                if (tableFiles == null) {
                    LOG.log(Level.WARNING, "no rule files on path {0} found", path);
                    return null;
                }
                for (String tableName : tableFiles) {
                    tableName = tableName.toUpperCase();
                    if (tableName.endsWith(".LB")) {
                        tableName = tableName.substring(0, tableName.length() - 3);
                    }
                    String content = "";
                    String[] tableContent = this.getTableStringValues(tableName, pool, year);
                    if (tableContent != null) {
                        Arrays.sort(tableContent);
                        content = Arrays.toString(tableContent);
                        content = content.substring(content.indexOf('[') + 1, content.indexOf(']'));
                    }
                    lRetMap.put(tableName, new CpTable(tableName, content, ""));

                }
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, " error on reading rule tables for pool " + pool + " year " + year, ex);
            return null;
        }
        return lRetMap;
    }

    public static String getPoolIdentifier(String identifier, int year) {
        if (year != 0 && !identifier.equals(String.valueOf(year))) {
            identifier += "_" + String.valueOf(year);
        }
        return identifier;
    }

    public File createBackupRulesDirectory(String poolName, int year) {
        try {

            String poolPath = this.getRulesPath() + File.separator + getPoolIdentifier(poolName, year);
            File poolDir = new File(poolPath);
            if (!poolDir.exists() && !poolDir.mkdir()) {
                LOG.log(Level.SEVERE, " pool directory not found and could not be created on path {0}", poolPath);
                return null;
            }
// check backup directory         
            File backup = new File(poolPath + "_backup");
// delete old backup     
            int count = 0;
            while (backup.exists()) {
                backup = new File(poolPath + "_backup(" + String.valueOf(++count) + ")");
            }
// rename pool into backup
            if (!poolDir.renameTo(backup)) {
                LOG.log(Level.SEVERE, " could not rename old  directory {0}into {1}_backup", new Object[]{poolPath, poolPath});
                return null;
            }
// create new poolDir
            if (!poolDir.mkdir()) {
                LOG.log(Level.SEVERE, " could not create  directory {0}", poolPath);
                return null;
            }
            return poolDir;
        } catch (SecurityException ex) {
            LOG.log(Level.SEVERE, " error on saving rule tables for pool " + poolName + " year " + year, ex);
            return null;
        }
    }

    public boolean saveRuleTables4Pool(File destDir, List<CrgRuleTables> ruleTables4Export) {
        String path = "";
        try {
            if (destDir == null || !destDir.isDirectory()) {
                LOG.log(Level.SEVERE, "destination directory not found");
                return false;
            }
            path = destDir.getAbsolutePath();
            if (!path.endsWith("table")) {
                path += File.separator + "table";
            }
            destDir = new File(path);
            if (!destDir.exists() && !destDir.mkdir()) {
                LOG.log(Level.SEVERE, "destination directory not found");
                return false;

            }
            if (ruleTables4Export == null || ruleTables4Export.isEmpty()) {
                LOG.log(Level.INFO, " ther are no tables to be saved");
                return true;
            }
            String tabPath = destDir.getAbsolutePath() + File.separator;
            for (CrgRuleTables table : ruleTables4Export) {
                String tabName = table.getCrgtTableName();
                String tabContent = table.getCrgtContent();
                if (tabName != null && !tabName.isEmpty()) {
                    saveOneTable(tabPath + tabName + ".lb", tabContent);
                }
            }
            return true;
        } catch (SecurityException ex) {
            LOG.log(Level.SEVERE, " error on saving rule tables on path: " + path, ex);
            return false;
        }
    }

    private void saveOneTable(String tabPath, String content) {
        //FileOutputStream fos = null;
        //OutputStreamWriter osw = null;
        //BufferedWriter out = null;

        try {
            File f = new File(tabPath);
            String[] tabContent = new String[0];
            if (content != null && content.length() > 0) {
                tabContent = content.split(",");
            }
            if (!f.exists()) {
                if (f.createNewFile()) {
                    LOG.log(Level.FINEST, "new file was created: {0}", f.getAbsolutePath());
                }
            }
            try (FileOutputStream fos = new FileOutputStream(f); OutputStreamWriter osw = new OutputStreamWriter(fos); BufferedWriter out = new BufferedWriter(osw)) {
                for (int i = 0; i < tabContent.length; i++) {
                    String line = tabContent[i].trim();
                    out.write(line);
                    out.newLine();
                }
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, " error on save of table " + tabPath, ex);
        }
    }

    public List<CRGRuleTypes> getCpRuleTypes() {
        try {
            @SuppressWarnings("unchecked")
            Vector<CRGRuleTypes> types = super.getRuleTypes(this.getRulesPath() + File.separator + "csrules_types.xml");
            if (types == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>(types);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, " error on reading of rule types from file ", ex);
            return new ArrayList<>();
        }
    }

    @Override
    protected CRGRule getNewRule(Element ele, String rulePath, int analyseType) {
        return new CpxCRGRule(this, ele, rulePath, null, analyseType);
    }

    @Override
    public String[] getStringArrayFromRuleTables(String tableName, String ruleIdent, int year) throws Exception {
        return super.getTableStringValues(tableName, ruleIdent, year);
    }

    private void saveTestData(CRGRule[] ret) {

        if (ret != null) {
            for (CRGRule rule : ret) {
                LOG.log(Level.INFO, "{0};{1};{2}", new Object[]{rule.m_ruleNumber, rule.m_number, rule.m_rid});
            }
        }
    }

    @Override
    public int[] getIntArrayFromRuleTables(String tableName, String ruleIdent, int year) throws Exception {
        return super.getTableIntValues(tableName, ruleIdent, year);
    }

    @Override
    public double[] getDoubleArrayFromRuleTables(String tableName, String ruleIdent, int year) throws Exception {
        return super.getTableDoubleValues(tableName, ruleIdent, year);
    }

    @Override
    public long[] getLongArrayFromRuleTables(String tableName, String ruleIdent, int year) throws Exception {
        return super.getTableLongValues(tableName, ruleIdent, year);
    }

    @Override
    public Date[] getDateArrayFromRuleTables(String tableName, String ruleIdent, int year) throws Exception {
        return super.getTableDateValues(tableName, ruleIdent);
    }

    @Override
    public List<CRGRulePool> getRulePools() throws Exception {
        File ruleDir = new File(getRulesPath());
        if (!ruleDir.exists()) {
            LOG.log(Level.INFO, "rules directory {0} not found", ruleDir.getAbsolutePath());
            return new ArrayList<>();
        }

        String[] subDirs = ruleDir.list();
        int sdSz = subDirs != null ? subDirs.length : 0;
        if (sdSz > 0) {
            List<CRGRulePool> poolList = new ArrayList<>();
            for (int i = 0; i < sdSz; i++) {
                CRGRulePool rpool = new CRGRulePool();
                poolList.add(rpool);
                rpool.id = i;
                rpool.crgpl_active = true;
                rpool.crgpl_identifier = subDirs[i];
                try {
                    rpool.crgpl_year = Integer.parseInt(subDirs[i]);
                } catch (NumberFormatException ex) {
                    rpool.crgpl_year = 0;
                    LOG.log(Level.INFO, "pool name {0} is not a year", subDirs[i]);
                }
                rpool.id = rpool.crgpl_year;
            }
            return poolList;
        } else {
            LOG.log(Level.INFO, "Root path (rulePath) {0} has noch child folders", ruleDir.getAbsolutePath());
            return new ArrayList<>();
        }
        //return null;
    }

}
