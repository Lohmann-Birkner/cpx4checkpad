/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.scheduled_ejb;

import de.lb.cpx.system.properties.CpxSystemProperties;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import java.nio.file.Path;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.batch.runtime.BatchStatus;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.NoSuchObjectLocalException;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timer;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ejb3.annotation.TransactionTimeout;

/**
 *
 * @author Dirk Niemeier
 */
@Singleton
//@Startup
@DependsOn("InitBean")
@SecurityDomain("cpx")
public class CatalogImportBean {

    private static final Logger LOG = Logger.getLogger(CatalogImportBean.class.getName());

    @EJB(beanName = "CatalogImportHelperBean")
    private CatalogImportHelperBean catalogImportHelper;
    private WatchService watchService;
    private Thread thread;
    private BatchStatus batchStatus = BatchStatus.ABANDONED;

    public CatalogImportBean() {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "*** CATALOG IMPORT BEAN CREATED ***");
    }

    //@PostConstruct
    @Schedule(hour = "*", minute = "*", second = "*", persistent = false)
    @TransactionTimeout(value = 360, unit = TimeUnit.MINUTES)
    public void init(Timer timer) {
        if (timer != null) {
            try {
                timer.cancel();
            } catch (NoSuchObjectLocalException ex) {
                LOG.log(Level.FINEST, "Timer canceled with error", ex);
            }
        }

        if (CpxSystemProperties.getInstance().getCpxWebAppFile()) {
            LOG.log(Level.WARNING, "This code is not running in CPX EAR File, but in CPX Web App. Catalog Import will be skipped to prevent multiple imports!");
            return;
        }

        //First catalog import on startup
        startCatalogImport();
        try {
            //Start import again when there are changes in catalog directory
            startWatchService(catalogImportHelper.getCatalogDirectory());
        } catch (ClassNotFoundException | IOException | InterruptedException ex) {
            LOG.log(Level.SEVERE, null, ex);
            if (ex instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        }
    }

    //@AccessTimeout(value = 30, unit = TimeUnit.MINUTES)
    @Asynchronous
    public synchronized Future<Boolean> startCatalogImport() {
        final List<File> catalogFiles = catalogImportHelper.getCatalogFileList();
        return startCatalogImport(catalogFiles);
    }
    //@AccessTimeout(value = 30, unit = TimeUnit.MINUTES)
    @Asynchronous
    public synchronized Future<Boolean> startCatalogImport(List<File> catalogFiles) {
        if (BatchStatus.ABANDONED.equals(batchStatus)
                || BatchStatus.FAILED.equals(batchStatus)
                || BatchStatus.COMPLETED.equals(batchStatus)) {
            LOG.log(Level.INFO, "*** CATALOG IMPORT IS STARTING (LAST IMPORT FINISHED WITH WITH STATUS ''{0}'') ***", batchStatus);
        } else {
            LOG.log(Level.WARNING, "*** CATALOG IMPORT IS ALREADY RUNNING WITH STATUS ''{0}'' ***", batchStatus);
            return new AsyncResult<>(false);
        }
        batchStatus = BatchStatus.STARTING;
        
        if (catalogFiles.isEmpty()) {
            LOG.log(Level.WARNING, "No catalog files found!");
            return new AsyncResult<>(true);
        }
        batchStatus = BatchStatus.STARTED;
        List<CatalogImportResult> lstFailedCatalogs = new ArrayList<>();
        List<CatalogImportResult> lstSuccessesCatalogs = new ArrayList<>();
        for (final File file : catalogFiles) {
            Future<CatalogImportResult> importCatalog;// = importOneCatalog(file, lstSuccessesCatalogs, lstFailedCatalogs);
            try {
                importCatalog = catalogImportHelper.importCatalog(file);
                try {
                    CatalogImportResult result = importCatalog.get();
                    if (result != null && result.imported) {
                        lstSuccessesCatalogs.add(result);
                    }
                } catch (InterruptedException | ExecutionException  ex) {
                    lstFailedCatalogs.add(new CatalogImportResult(false, file, null, 0, "", 0, 0, "Import of catalog file failed: " + file.getAbsolutePath()));
                    LOG.log(Level.SEVERE, "Import of catalog file failed: " + file.getAbsolutePath(), ex);
                    if (ex instanceof InterruptedException) {
                        Thread.currentThread().interrupt();
                    }
                }
            } catch (IOException ex) {
                lstFailedCatalogs.add(new CatalogImportResult(false, file, null, 0, "", 0, 0, "File seems to be invalid: " + file.getAbsolutePath()));
                LOG.log(Level.SEVERE, "File seems to be invalid: " + file.getAbsolutePath(), ex);
            }
        }
        BatchStatus newStatus;
        if (lstFailedCatalogs.isEmpty()) {
            newStatus = BatchStatus.COMPLETED;
        } else {
            newStatus = BatchStatus.FAILED;
        }

        if (lstSuccessesCatalogs.isEmpty() && lstFailedCatalogs.isEmpty()) {
            LOG.log(Level.INFO, "Nothing was imported");
        } else {
            if (!lstFailedCatalogs.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (final CatalogImportResult result : lstFailedCatalogs) {
                    sb.append("\r\n  * " + result.toString());
                }
                LOG.log(Level.SEVERE, "Failures occured when I tried to import these {0} files: {1}\r\n(see log file above to get more information)", new Object[]{lstFailedCatalogs.size(), sb});
            }
            if (!lstSuccessesCatalogs.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (final CatalogImportResult result : lstSuccessesCatalogs) {
                    sb.append("\r\n  * " + result.toString());
                }
                LOG.log(Level.INFO, "Successfully imported these {0} files: {1}\r\n(see log file above to get more information)", new Object[]{lstSuccessesCatalogs.size(), sb});
            }
        }
        LOG.log(Level.INFO, "*** CATALOG IMPORT FINISHED WITH STATUS ''{0}'' ***", newStatus);
        batchStatus = newStatus;
        return new AsyncResult<>(true);
    }
    

//    private Future<CatalogImportResult> importOneCatalog(final File file, 
//            List<CatalogImportResult> lstSuccessesCatalogs,
//            List<CatalogImportResult> lstFailedCatalogs){
//        
//
//            try {
//                Future<CatalogImportResult> importCatalog = catalogImportHelper.importCatalog(file);
//                try {
//                    CatalogImportResult result = importCatalog.get();
//                    if (result != null && result.imported) {
//                        lstSuccessesCatalogs.add(result);
//                    }
//                } catch (InterruptedException | ExecutionException ex) {
//                    lstFailedCatalogs.add(new CatalogImportResult(false, file, null, 0, "", 0, 0, "Import of catalog file failed: " + file.getAbsolutePath()));
//                    LOG.log(Level.SEVERE, "Import of catalog file failed: " + file.getAbsolutePath(), ex);
//                    if (ex instanceof InterruptedException) {
//                        Thread.currentThread().interrupt();
//                    }
//                }
//                return importCatalog;
//            } catch (IOException ex) {
//                lstFailedCatalogs.add(new CatalogImportResult(false, file, null, 0, "", 0, 0, "File seems to be invalid: " + file.getAbsolutePath()));
//                LOG.log(Level.SEVERE, "File seems to be invalid: " + file.getAbsolutePath(), ex);
//                return null;
//            }
//            
//    }
//    
    

    @PreDestroy
    public void destroy() {
        LOG.log(Level.INFO, "*** CATALOG IMPORT BEAN DESTROYED ***");
    }

//  private void stopWatchService() {
//    if (watchService != null) {
//      try {
//        watchService.close();
//      } catch (IOException ex) {
//        Logger.getLogger(CatalogImportBean.class.getName()).log(Level.SEVERE, null, ex);
//      }
//      watchService = null;
//    }
//    if (thread != null) {
//      thread.interrupt();
//      thread = null;
//    }
//  }
    private void startWatchService(final File pCatalogDir) throws IOException, InterruptedException, ClassNotFoundException {
        // Sanity check - Check if path is a folder
        if (pCatalogDir == null) {
            return;
        }
        LOG.log(Level.INFO, "I''m going to observe catalog directory {0} for changes", pCatalogDir.getAbsolutePath());
        final Path catalogPath = pCatalogDir.toPath();
        Boolean isFolder = (Boolean) Files.getAttribute(catalogPath, "basic:isDirectory", NOFOLLOW_LINKS);
        if (!isFolder) {
            throw new IllegalArgumentException("Path: " + catalogPath + " is not a folder!");
        }

        // We obtain the file system of the Path
        final FileSystem fs = catalogPath.getFileSystem();
        //Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Watching path: " + catalogPath);

        Runnable runnableWatchservice = new Runnable() {
            @Override
            public void run() {
                try {
                    watchService = fs.newWatchService();
                    // We register the path to the service
                    // We watch for creation events
                    catalogPath.register(watchService, ENTRY_CREATE, ENTRY_MODIFY /*, ENTRY_DELETE */);

                    // Start the infinite polling loop
                    WatchKey key;
                    while (!Thread.currentThread().isInterrupted()) {
                        key = watchService.take();

                        if (key == null) {
                            continue;
                        }

//                        List<String> changedFiles = new LinkedList<>();

                        // Dequeueing events
                        Kind<?> kind;
                        for (WatchEvent<?> watchEvent : key.pollEvents()) {
                            // Get the type of the event
                            kind = watchEvent.kind();
                            if (OVERFLOW == kind) {
                                continue; // loop
                            } else if (ENTRY_CREATE == kind || ENTRY_MODIFY == kind) {
                                Path newPath = ((WatchEvent<Path>) watchEvent).context();
                                File file = newPath.toFile();
                                if (file.isDirectory()) {
                                    //Unterverzeichnisse sind irrelevant!
                                    continue;
                                }
                                //It makes sense to abort here, if change is not concerning .ser.zip file!
                                if (!catalogImportHelper.isSerializedCatalog(file) && !catalogImportHelper.isCoreData(file)) {
                                    //Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Is not a serialized catalog file (.ser.zip), so I don't care: " + newPath);
                                    continue;
                                }
//                                if (!changedFiles.contains(file.getName())) {
//                                    changedFiles.add(file.getName());
//                                } else {
//                                    //Already processed
//                                    continue;
//                                }
                                if (ENTRY_CREATE == kind) {
                                    LOG.log(Level.INFO, "New entry created: {0}", newPath);
                                }
                                if (ENTRY_MODIFY == kind) {
                                    LOG.log(Level.INFO, "Entry was modified: {0}", newPath);
                                }
                                if(catalogImportHelper.fileCanAutoUpdate(file)){
                                    LOG.log(Level.INFO, "Starting catalog import again... ");
//                                }else{
//                                     LOG.log(Level.INFO, "File {0} don't belong to auto update list... ", newPath);
//                                }
                                    try {
                                        /* 
                                        * sleep shall prevent Exception ("Der Prozess kann nicht auf die Datei zugreifen, da sie von einem anderen Prozess verwendet wird").
                                        * This exception appears when the imports immediatly starts before windows has completed the copy process
                                         */
                                        Thread.sleep(500L);
                                    } catch (InterruptedException ex) {
                                        LOG.log(Level.INFO, "Thread was interrupted", ex);
                                        Thread.currentThread().interrupt();
                                    }
                                    List<File> file2Update = catalogImportHelper.getCatalogFile2Name(newPath.toString());
                                    if(file2Update != null){
                                        startCatalogImport(file2Update);
                                    }
                                }else{
                                     LOG.log(Level.INFO, "File {0} dooesn't belong to auto update list... ", newPath);
                                }

//                                startCatalogImport();
                            }
                        }
//                        changedFiles.clear();

                        if (!key.reset()) {
                            break; // loop
                        }
                    }
                } catch (InterruptedException | IOException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                    if (ex instanceof InterruptedException) {
                        Thread.currentThread().interrupt();
                    }
                } finally {
                    if (watchService != null) {
                        try {
                            watchService.close();
                        } catch (IOException ex) {
                            LOG.log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } //end of run method
        };

        thread = new Thread(runnableWatchservice);
        thread.start();
    }

}
