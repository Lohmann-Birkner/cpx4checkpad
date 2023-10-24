/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.core.model.catalog;

import de.lb.cpx.client.core.connection.database.CpxDbManager;
import static java.lang.Boolean.TRUE;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
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
public class TestCpxCatalog //extends StartupLoaderFXMLController
{

    private static final Logger LOG = Logger.getLogger(TestCpxCatalog.class.getName());
    private final int numberOfThreads = 50;
   
    
    public static void main(String args[]){
        TestCpxCatalog testCatalog = new TestCpxCatalog();
        if(testCatalog.checkSqliteDb()){
            testCatalog.startReadIcds();
        }else{
            System.exit(-1);
        }
    }
 /**
     * Check if a connection to local SQLite database can be established
     *
     * @return is a connection to local SQLite database available?
     */
    private boolean checkSqliteDb() {
        LOG.log(Level.INFO, "Check if local SQLite database is available...");
        try {
            CpxDbManager cacheManager = CpxDbManager.instance();
            cacheManager.testLanguageDb();
            cacheManager.testCatalogDb();
        } catch (SQLException ex) {
            final String msg = "Cannot connect local SQLite database";
            LOG.log(Level.SEVERE, msg, ex);

            return false;
        }
        LOG.log(Level.INFO, "done getting local db");
        return true;
    }
    

    public void initialize(URL url, ResourceBundle rb) {
        
    }

    public void startReadIcds() {

        
                ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numberOfThreads); 
                CompletionService<Boolean > completionService = new ExecutorCompletionService<Boolean>(executor);
                for(int i = 0; i < numberOfThreads; i++){
                     CheckCatalogTask<Boolean> task = new CheckCatalogTask<Boolean>( i);
                    completionService.submit(task);
                }
                executor.shutdown();
                try {
                    for (int t = 0; t < numberOfThreads ; t++) {
                        Future<Boolean > f = completionService.take();
                        Boolean result = f.get();

                    }
                } catch (InterruptedException ex) {
                    //termination of all started tasks (it returns all not started tasks in queue)
                    executor.shutdownNow();
                    LOG.log(Level.SEVERE, "error on getting rules from db", ex);
                    
                } catch (ExecutionException ex) {
                    LOG.log(Level.SEVERE, "error on getting rules from db", ex);
                    
                }
                


    }
    
//    public Boolean executeUpdate(){
//        try{
//            Method executeUpdateMethod = StartupLoaderFXMLController.class.getDeclaredMethod("executeUpdate", int.class);
//            boolean ret = (boolean)executeUpdateMethod.invoke(StartupLoaderFXMLController.class, 1);
//            return ret;
//        }catch(Exception ex){
//            LOG.log(Level.SEVERE, "could not find method executeUpdate", ex);
//            return false;
//        }
//
//    }
//    public void setProgress(final String pText) {
//        LOG.log(Level.INFO, pText);
//    }

}
class CheckCatalogTask<Boolean> implements Callable<Boolean >{

    private static final Logger LOG = Logger.getLogger(CheckCatalogTask.class.getName());
    

    private final int threadNr;

    private final CpxIcdCatalog icdCatalog;
//    public CheckCatalogTask(TestCpxCatalog testCatalog){
//        this.testCatalog = testCatalog;
//    }

   public CheckCatalogTask(final int i){
        this.threadNr = i;
        CpxDbManager.instance().getConnection("cpx_catalog_icd_2020", true);
        CpxDbManager.instance().getConnection("cpx_catalog_icd_thesaurus_2020", true);
        icdCatalog = CpxIcdCatalog.instance();
    }

    @Override
    public Boolean call() throws Exception {
        return (Boolean) readIcdCatalog();
    }
    
    
    public Boolean readIcdCatalog(){
        Map<Long, CpxIcd> map = icdCatalog.getAll("DE", 2020);
        Set<Long> keys = map.keySet();
        keys.stream().map((key) -> {
            CpxIcd icd = map.get(key);
            LOG.log(Level.INFO, "Thread {0}: key: {1} icd: {2}, descriprion: {3}", 
                    new Object[]{threadNr, String.valueOf(key), icd.getCode(), icdCatalog.getDescriptionByCode(icd.getCode(), "DE", 2020)});
            return icd;
        }).map((icd) -> icd.getChildren()).forEachOrdered((children) -> {
            printIcds(children);
        });
        return (Boolean) TRUE;
    }
    
    private void printIcds(List<ICpxTreeItem<CpxIcdThesaurus>>  children){
        if(children == null){
            return;
        }
        children.stream().map((child) -> {
            LOG.log(Level.INFO, "Thread {0}, code: {1}, descriprion: {2}" , 
                    new Object[]{threadNr, child.getCode(), icdCatalog.getDescriptionByCode(child.getCode(), "DE", 2020)});
            return child;
        }).forEachOrdered((child) -> {
            printIcds(child.getChildren());
        });
    }
}
