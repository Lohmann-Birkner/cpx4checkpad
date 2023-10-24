/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.core.model.fx.login;

import de.checkpoint.drg.GDRGModel;
import de.checkpoint.drg.GrouperInterfaceBasic;
import de.lb.cpx.client.core.connection.database.CpxDbManager;
import de.lb.cpx.client.core.easycoder.DictionaryBuilder;
import de.lb.cpx.service.information.CatalogTodoEn;
import de.lb.cpx.service.information.CatalogTypeEn;
import de.lb.cpx.service.information.CpxCatalogOverview;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gerschmann
 */
public class DictionaryChecker {
    

    private static final Logger LOG = Logger.getLogger(DictionaryChecker.class.getName());
    private DictionaryBuilder builder = new DictionaryBuilder();
    private Map<Integer, List<CatalogTypeEn>> icdOpsDict = new HashMap<>();
    private int refreshedCount = 0; 
    
    private final CpxSystemPropertiesInterface cpxProperties = CpxSystemProperties.getInstance();

    public DictionaryChecker(){
        createDictionaryDir();
        
    };


    public  String getDictionaryAutocompletePath(CatalogTypeEn pType, int pYear){
        return (pType.equals(CatalogTypeEn.OPS)?cpxProperties.getCpxClientDictionariesOpsDir():cpxProperties.getCpxClientDictionariesIcdDir())
                        + pType.name() + "AutocompleteDir_" + pYear;
    }

    public String getDictionaryCorrectionPath(CatalogTypeEn pType, int pYear){
        return (pType.equals(CatalogTypeEn.OPS)?cpxProperties.getCpxClientDictionariesOpsDir():cpxProperties.getCpxClientDictionariesIcdDir())
                        + pType.name() + "CorrectionDir_" + pYear;
    }
    
    public String getDictionaryPath(){
        return cpxProperties.getCpxClientDictionariesDir() + File.separator + DictionaryBuilder.SPELL_CHECK_DIC + File.separator ;
    }
    
    public String getFullDictionaryPath(CatalogTypeEn pType, int pYear){
        return getDictionaryPath() +  pType.name() + "_dictionary_" + pYear + ".txt";
    }
    
    public boolean hasDictionary4Year(CatalogTypeEn pType, int pYear) {
        File autoComplDir = new File(getDictionaryAutocompletePath(pType, pYear));
        File corrDir = new File(getDictionaryCorrectionPath(pType, pYear));
        File spellCheck = new File(getFullDictionaryPath(pType, pYear));
        return spellCheck.exists() && spellCheck.isFile() 
                && autoComplDir.exists() && autoComplDir.isDirectory() && autoComplDir.list()!= null && autoComplDir.list().length == 4
                && corrDir.exists() && corrDir.isDirectory() && corrDir.list() != null && corrDir.list().length == 4;
                       
    }

     public void checkIcdOpsDictionaries() {

        // check dictionaries for all grouper Years
        final int currentYear = GrouperInterfaceBasic.LAST_CHECKPOINT_YEAR;
        final int minYear = GDRGModel.getMinModelYear();
        for(int year = minYear; year <= currentYear; year++){
            if(!hasDictionary4Year(CatalogTypeEn.ICD, year)){
                add2DictionaryCheck(year, CatalogTypeEn.ICD);
            }
            if(!hasDictionary4Year(CatalogTypeEn.OPS, year)){
                add2DictionaryCheck(year, CatalogTypeEn.OPS);
            }
        }
//        if(!icdOpsDict.isEmpty()){
//       
//            Set<Integer> years = icdOpsDict.keySet();
//            int numberOfThreads = years.size();
//            ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(numberOfThreads);
//            CompletionService<Boolean> completionService = new ExecutorCompletionService<Boolean>(executor);
//            for(int year: years){
//                List<CatalogTypeEn> types = icdOpsDict.get(year);
//                WriteDictionaryTask task = new WriteDictionaryTask(year, types);
//                completionService.submit(task);
////                for(CatalogTypeEn type:types){
////                    writeDictionary( year, type);
////                }
//            }
//                try {
//                    for (int t = 0; t < numberOfThreads ; t++) {
//                        Future<Boolean > f = completionService.take();
//                        Boolean result = f.get();
//
//                    }
//                } catch (InterruptedException ex) {
//                    //termination of all started tasks (it returns all not started tasks in queue)
//                    executor.shutdownNow();
//                    LOG.log(Level.SEVERE, "error on creating dictionaries", ex);
//
//                } catch (ExecutionException ex) {
//                    LOG.log(Level.SEVERE, "error on creating dictionaries", ex);
//
//                }
//            
//        }
    }
     public Map<Integer, List<CatalogTypeEn>> getIcdOpsDict (){
         return icdOpsDict;
     }

     public boolean writeDictionaries4Year(int pYear, List<CatalogTypeEn> pTypes){
         if(pTypes == null || pTypes.isEmpty()){
             return false;
         }
         for(CatalogTypeEn type:pTypes){
             writeDictionary(pYear, type);
         }
         return true;
     }
     
    private void writeDictionary( int pYear, CatalogTypeEn pType) {
        if(pType.equals(CatalogTypeEn.OPS) || pType.equals(CatalogTypeEn.ICD)){
            Connection thesaurusConn = null;
            Connection catalogConn = CpxDbManager.instance().getCatalogDb(pType, pYear, true);
            if(pYear >= 2018){
                thesaurusConn = CpxDbManager.instance().getCatalogDb(pType.equals(CatalogTypeEn.OPS)?CatalogTypeEn.OPS_THESAURUS:CatalogTypeEn.ICD_THESAURUS, pYear, true);
            }
            try{
                if(createDictionaryDir(getDictionaryPath())){
                    builder.createDictionaryFromCatalog(catalogConn, thesaurusConn, getFullDictionaryPath(pType, pYear),
                            DictionaryBuilder.getSqlCatalogQuery(pType, pYear),
                            DictionaryBuilder.getCatalogColumnName(pType),
                            DictionaryBuilder.getSqlThesaurusQuery(pType, pYear),
                            DictionaryBuilder.getThesaurusColomnName(pType)
                        );
                    if(createDictionaryDir(getDictionaryCorrectionPath(pType, pYear)) && createDictionaryDir(getDictionaryAutocompletePath(pType, pYear))){
                        builder.buildIndexes4Catalog(getFullDictionaryPath(pType, pYear),getDictionaryAutocompletePath(pType, pYear), getDictionaryCorrectionPath(pType, pYear));
                    }

                }
             }catch (IOException ex){
                LOG.log(Level.SEVERE, "Error on write Dictionary", ex);
            }
        }else{
            LOG.log(Level.INFO, "Unknown type {0}", pType.name());
        }
        
    }
    
        private synchronized boolean createDictionaryDir() {
            
            String dictDir = cpxProperties.getCpxClientDictionariesDir();
            return createDictionaryDir(dictDir);
        }
        
        private synchronized boolean createDictionaryDir(String pDictDir) {
            return createDictionaryDir(pDictDir, false);
        }
        private synchronized boolean createDictionaryDir(String pDictDir, boolean pClear) {
            
        if (pDictDir == null || pDictDir.trim().isEmpty()) {
            return true;
        }
        final File pFile = new File(pDictDir);
        if (pFile.exists() && pFile.isDirectory()) {
             //TODO: remove all files from this directory
             if(pClear){
                 return deleteChildren(pFile);
             }
            return true;
        }
        if (pFile.setExecutable(false)) {
            LOG.log(Level.FINEST, "Was successfully set to unexecutable: " + pFile.getName());
        }
        if (pFile.setReadable(true)) {
            LOG.log(Level.FINEST, "Was successfully set to readable: " + pFile.getName());
        }
        if (pFile.setWritable(true)) {
            LOG.log(Level.FINEST, "Was successfully set to writeable: " + pFile.getName());
        }
//        pFile.setExecutable(false);
//        pFile.setReadable(true);
//        pFile.setWritable(true);
        if (!pFile.mkdirs()) {
            //throw new FileSystemException("Was not able to create directory '" + pFile.getAbsolutePath() + "'");
            LOG.log(Level.SEVERE, "Was not able to create directory '" + pFile.getAbsolutePath() + "'");
        }
        return pFile.exists() && pFile.isDirectory();
    }

    public void add2DictionaryCheck(List<CpxCatalogOverview> pCatalogOverview) {
            for(CpxCatalogOverview overview: pCatalogOverview){
                CatalogTypeEn type = overview.getCatalogType();
                if(overview.getTodo().equals(CatalogTodoEn.IMPORT) || overview.getTodo().equals(CatalogTodoEn.REIMPORT)){
                {
                    add2DictionaryCheck(overview.getYear(), type);
                }
            }
        }
    }
    
    public void add2DictionaryCheck(int pYear, CatalogTypeEn pType){
        if(!pType.name().startsWith(CatalogTypeEn.ICD.name()) 
                && !pType.name().startsWith(CatalogTypeEn.OPS.name())){
            return;
        }
       List<CatalogTypeEn> list = icdOpsDict.get(pYear);
       if(list == null){
           list = new ArrayList<CatalogTypeEn>();
           icdOpsDict.put(pYear, list);
       }
       CatalogTypeEn tmpType = pType.name().startsWith(CatalogTypeEn.ICD.name())?CatalogTypeEn.ICD:CatalogTypeEn.OPS;
       if( !list.contains(tmpType) ){
           list.add(tmpType);
       }
       
    }

    public void refreshCount() {
        refreshedCount++;
    }
    
    public boolean isWriteCompleted(){
        return icdOpsDict.keySet().size() == refreshedCount;
    }
    
    public void resetCount(){
        refreshedCount = 0;
    }

    private boolean deleteChildren(File pFile) {
        File[] files = pFile.listFiles();
        if(files == null || files.length == 0){
            return true;
        }
        for(File file: files){
            if(file.isDirectory()){
                deleteChildren(file);
            }
            if(!file.delete()){
                return false;
            }
        }
        return true;
    }

    class WriteDictionaryTask implements Callable<Boolean>{

        private final int year;
        private final List<CatalogTypeEn> catalogTypes;
        public WriteDictionaryTask(
                final int pYear,
                final List<CatalogTypeEn> pCatalogTypes
        ){
            year = pYear;
            catalogTypes = pCatalogTypes;
        }

            @Override
            public Boolean call() throws Exception {
                for(CatalogTypeEn type: catalogTypes){
                    writeDictionary( year, type);
                }
                return true;
            }

    }

   
}
