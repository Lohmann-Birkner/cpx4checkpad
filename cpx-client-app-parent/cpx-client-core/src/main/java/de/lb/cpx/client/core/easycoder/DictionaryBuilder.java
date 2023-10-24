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
 *    2018 Halabieh,Khaled
 */
package de.lb.cpx.client.core.easycoder;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.service.information.CatalogTypeEn;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.search.suggest.analyzing.AnalyzingInfixSuggester;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NoLockFactory;

public class DictionaryBuilder {

//
    private static final Logger LOG = Logger.getLogger(DictionaryBuilder.class.getName());

    private final CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();

//    private String dictionaryPath = cpxProps.getCpxClientDictionariesDir(); //NOT WORKING
//
//    private final String catalogsPath = System.getProperty("user.home") + "\\AppData\\Local\\CPX\\catalog\\";
    private String catalogName = "";
    private String thesaurusName = "";
//    private String sqlCatalogQuery = "";
//    private String sqlThesaurusQuery = "";
//    private String dictionaryName = "";
//    private String catalogColumnName = "";
//    private String thesaurusColomnName = "";
//    private String autocompleteDir = "";
//    private String correctionDir = "";
//    private AnalyzingInfixSuggester suggester;
//    private StandardAnalyzer analyzer;
//    private SpellChecker spellChecker;
    
    public static final String SPELL_CHECK_DIC = "spellCheckDic";

    public static void main(String[] args) throws ClassNotFoundException, IOException {
////        if(args.length < 2){
////            LOG.log(Level.INFO, "usage dictionaryPath, catalogPath");
////            System.exit(0);
////        }
        DictionaryBuilder dbu = new DictionaryBuilder();
        Calendar cal = Calendar.getInstance();
        final int currentYear = cal.get(Calendar.YEAR) + 1;
        final int minYear = GDRGModel.getMinModelYear();
        File newPath = new File("D:\\Projekte\\cpx\\WD_CPX_Client\\dictionaries\\");
        String catalogPath = "D:\\Projekte\\cpx\\WD_CPX_Client\\temp\\gerschmann\\catalog\\";
        String dictionaryPath = newPath.getAbsolutePath() + File.separator + SPELL_CHECK_DIC + "\\";


        //Build Dictionaries 2004-2019
        for (int i = 2023; i < 2024; i++) {
//            dbu.buildDictionary(CatalogTypeEn.OPS, i, dictionaryPath, catalogPath);
            dbu.buildDictionary(CatalogTypeEn.ICD, i, dictionaryPath, catalogPath);
        }
        //Build Idexes from the Dictionaries 2004-2019
        for (int i = 2023; i < 2024; i++) {
            dbu.buildIndexes(CatalogTypeEn.ICD, i, dictionaryPath);
//            dbu.buildIndexes(CatalogTypeEn.OPS, i, dictionaryPath);
        }

    }

    
    public void buildDictionary(CatalogTypeEn pType, int pYear, String pDictPath, String pCatalogPath) {
        //Class.forName("org.sqlite.JDBC");

        //Fix dir Path 
        String dictionaryName = "";
        // create spellCheckDic directory if it dosn't exit
        if (!Files.isDirectory(Paths.get(pDictPath), LinkOption.NOFOLLOW_LINKS)) {
            File spellCheckDicDir = new File(pDictPath);
            Boolean b = spellCheckDicDir.mkdir();
            LOG.log(Level.INFO, "dictionary path: {0}", pDictPath);
            //if the directory path is wrong
        }
        //Thesaurus will be added automatically if the year is later than 2017 (2018,2019...) 
        boolean withThesaurus = false;
        if (pYear >= 2018) {
            withThesaurus = true;
        }
        switch (pType) {
            case ICD:
                catalogName = "cpx_catalog_icd_" + pYear + ".db";
                dictionaryName = "icd_dictionary_" + pYear + ".txt";
                if (withThesaurus) {
                    thesaurusName = "cpx_catalog_icd_thesaurus_" + pYear + ".db";
                }
                break;

            case OPS:
                catalogName = "cpx_catalog_ops_" + pYear + ".db";
                dictionaryName = "ops_dictionary_" + pYear + ".txt";
                if (withThesaurus) {
                    thesaurusName = "cpx_catalog_ops_thesaurus_" + pYear + ".db";
                }
                break;
        }
        Connection connection2 = null;
        try ( Connection connection = DriverManager.getConnection("jdbc:sqlite:" + pCatalogPath + catalogName)) {
            if(withThesaurus){
                connection2 = DriverManager.getConnection("jdbc:sqlite:" + pCatalogPath + thesaurusName);
            }
            createDictionaryFromCatalog(connection, connection2, pDictPath + dictionaryName,
                             getSqlCatalogQuery(pType, pYear),
                            getCatalogColumnName(pType),
                            getSqlThesaurusQuery(pType, pYear),
                            getThesaurusColomnName(pType));
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }
    
    public static String getSqlCatalogQuery(CatalogTypeEn pType, int pYear){
        switch (pType) {
            case ICD:
                
                return "SELECT ICd_DESCRIPTION FROM ICD_DE_" + pYear;

            case OPS:
                return "SELECT OPS_DESCRIPTION FROM OPS_DE_" + pYear;

        }
        return "";
    }
    
    public static String getCatalogColumnName(CatalogTypeEn pType){
        switch (pType) {
            case ICD:
                    return "ICD_DESCRIPTION";
            case OPS:

                return  "OPS_DESCRIPTION";

        }
        return "";
        
    }
    
    public static String getSqlThesaurusQuery(CatalogTypeEn pType, int pYear){
        if(pYear < 2018){
            return "";
        }
        switch (pType) {
            case ICD:
                return "SELECT description from ICD_THESAURUS_DE_" + pYear;


            case OPS:
                return "SELECT description from OPS_THESAURUS_DE_" + pYear;

        }
        return "";
        
    }
    
    public static String getThesaurusColomnName(CatalogTypeEn pType){
        switch (pType) {
            case ICD:
            case OPS:
                return "description";
        }
        return "";
    }
    
    public void createDictionaryFromCatalog(Connection pCatalogConnection, 
            Connection pThesaurusConnection, 
            String pDictPath,
            String sqlCatalogQuery,
            String catalogColumnName,
            String sqlThesaurusQuery,
            String thesaurusColomnName
    ){
            try ( Statement statement = pCatalogConnection.createStatement()) {
                statement.setQueryTimeout(30);

                try ( ResultSet rs = statement.executeQuery(sqlCatalogQuery)) {
                    Set<String> lines = new LinkedHashSet<>();

                    try ( PrintWriter out = new PrintWriter(pDictPath, "UTF-8")) {
                        while (rs.next()) {

                            String[] words = rs.getString(catalogColumnName).replaceAll("[^a-zA-ZäÄüÜöÖß]+", " ").toLowerCase().split("\\s+");
                            for (String word : words) {
                                //remove words longer than 3
                                if (word.length() <= 3) {
                                    continue;
                                }
                                //remove duplicate
                                if (lines.contains(word)) {
                                    continue;
                                }
                                lines.add(word);
                                out.println(word);
                            }
                        }
                        //get thesurus in dictionary wihtout closing the Printwriter
                        if (pThesaurusConnection != null) {
                                try ( Statement statment2 = pThesaurusConnection.createStatement()) {
                                    statment2.setQueryTimeout(30);
                                    try ( ResultSet rs2 = statment2.executeQuery(sqlThesaurusQuery)) {
                                        while (rs2.next()) {
                                            String[] words2 = rs2.getString(thesaurusColomnName).replaceAll("[^a-zA-ZäÄüÜöÖß]+", " ").toLowerCase().split("\\s+");
                                            for (String word2 : words2) {
                                                if (word2.length() <= 3) {
                                                    continue;
                                                }
                                                if (lines.contains(word2)) {
                                                    continue;
                                                }
                                                lines.add(word2);
                                                out.println(word2);
                                            }
                                        }
                                    }
                                }
                         }
                    }
                    LOG.log(Level.INFO, "{0}: SUCCESS", pDictPath);
                }
            } catch (SQLException e) {
                LOG.log(Level.SEVERE, e.getMessage(), e);
            } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }        
    }

    public void buildIndexes(CatalogTypeEn pType, int year, String pDictPath) throws IOException {
        String dictionaryName = "";

        String autocompleteDir = "";
        String correctionDir = "";

        if (!Files.isDirectory(Paths.get(pDictPath), LinkOption.NOFOLLOW_LINKS)) {
            File spellCheckDicDir = new File(pDictPath);
            spellCheckDicDir.mkdir();
        }

        switch (pType) {
            case ICD:
                dictionaryName = "icd_dictionary_" + year + ".txt";
                //fixed dir
                autocompleteDir = new File(cpxProps.getCpxClientDictionariesDir()).getParentFile().getParentFile().getParent() + "\\WD_CPX_Client\\dictionaries\\icd\\icdAutocompleteDir_" + year;
                correctionDir = new File(cpxProps.getCpxClientDictionariesDir()).getParentFile().getParentFile().getParent() + "\\WD_CPX_Client\\dictionaries\\icd\\icdCorrectionDir_" + year;
                break;
            case OPS:
                dictionaryName = "ops_dictionary_" + year + ".txt";
                autocompleteDir = new File(cpxProps.getCpxClientDictionariesDir()).getParentFile().getParentFile().getParent() + "\\WD_CPX_Client\\dictionaries\\ops\\opsAutocompleteDir_" + year;
                correctionDir = new File(cpxProps.getCpxClientDictionariesDir()).getParentFile().getParentFile().getParent() + "\\WD_CPX_Client\\dictionaries\\ops\\opsCorrectionDir_" + year;
                break;
        }
        buildIndexes4Catalog(pDictPath + dictionaryName, autocompleteDir, correctionDir);

//        analyzer = new StandardAnalyzer();
//        //Index autocomplete
//        if (new File(pDictPath + dictionaryName).exists()) {
//            FSDirectory directory = FSDirectory.open(Paths.get(autocompleteDir), NoLockFactory.INSTANCE);
//            suggester = new AnalyzingInfixSuggester(directory, analyzer);
//            suggester.build(new PlainTextDictionary(Paths.get(pDictPath + dictionaryName)));
//            suggester.commit();
//
//            LOG.log(Level.INFO, "{0}: SUCCESS", autocompleteDir);
//        } else {
//            LOG.log(Level.SEVERE, "{0} wurde nicht gefunden!!", dictionaryName);
//        }
//
//        //index correction
//        if (new File(pDictPath + dictionaryName).exists()) {
//            FSDirectory spellIndexDir = FSDirectory.open(Paths.get(correctionDir), NoLockFactory.INSTANCE);
//            spellChecker = new SpellChecker(spellIndexDir);
//            spellChecker.indexDictionary(new PlainTextDictionary(Paths.get(pDictPath + dictionaryName)), new IndexWriterConfig(null), false);
//
//            LOG.log(Level.INFO, "{0}: SUCCESS", spellIndexDir);
//        }

    }
    
    public void buildIndexes4Catalog(String pDictPath, //String pDictionaryName,
            String pAutocompleteDir, String correctionDir)throws IOException{
        StandardAnalyzer analyzer = new StandardAnalyzer();
        //Index autocomplete
        if (new File(pDictPath).exists()) {
            FSDirectory directory = FSDirectory.open(Paths.get(pAutocompleteDir), NoLockFactory.INSTANCE);
            AnalyzingInfixSuggester suggester = new AnalyzingInfixSuggester(directory, analyzer);
            suggester.build(new PlainTextDictionary(Paths.get(pDictPath)));
            suggester.commit();

            LOG.log(Level.INFO, "{0}: SUCCESS", pAutocompleteDir);
        } else {
            LOG.log(Level.SEVERE, "{0} wurde nicht gefunden!!", pDictPath);
        }

        //index correction
        if (new File(pDictPath).exists()) {
            FSDirectory spellIndexDir = FSDirectory.open(Paths.get(correctionDir), NoLockFactory.INSTANCE);
            SpellChecker spellChecker = new SpellChecker(spellIndexDir);
            spellChecker.indexDictionary(new PlainTextDictionary(Paths.get(pDictPath)), new IndexWriterConfig(null), false);

            LOG.log(Level.INFO, "{0}: SUCCESS", spellIndexDir);
        }
        
    }

}
