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
package de.lb.cpx.client.core.model.fx.login;

import de.FileUtils;
import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.connection.database.CpxDbManager;
import de.lb.cpx.client.core.menu.fx.BasicToolbarMenuScene;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.task.CpxTask;
import de.lb.cpx.client.core.util.cpx_handler.BasicCpxHandleManager;
import de.lb.cpx.client.core.util.cpx_handler.DummyCpxHandleManager;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.server.commons.dao.AbstractCatalogEntity;
import de.lb.cpx.service.ejb.CatalogImportServiceEJBRemote;
import de.lb.cpx.service.information.CatalogTodoEn;
import de.lb.cpx.service.information.CatalogTypeEn;
import de.lb.cpx.service.information.CpxCatalogOverview;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.system.properties.CpxSystemProperties;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Dirk Niemeier
 */
public class StartupLoaderFXMLController extends Controller<CpxScene> {

    private static final HashSet<Task<?>> RUNNING_TASKS = new HashSet<>();
    private static Map<CatalogTypeEn, List<CpxCatalogOverview>> sCatalogOverview = null;
    private static final Logger LOG = Logger.getLogger(StartupLoaderFXMLController.class.getName());
    public static final File LOCK_FILE = new File(CpxDbManager.CATALOG_DIR + "update_is_running.lock");
    public static final File LOCK_DICTIONARY_FILE = new File(CpxSystemProperties.getInstance().getCpxClientDictionariesDir() + "update_is_running.lock");
    public static final File LOCK_INFO_FILE = new File(CpxDbManager.CATALOG_DIR + "update_is_running.info");
    private static File currentLockFile = LOCK_FILE;
    private static final Set<CatalogTypeEn> ALLOWED_CATALOG_TYPES = getAllCatalogTypes();
    private static Class<? extends BasicToolbarMenuScene> TOOLBAR_MENU_SCENE_CLASS = BasicToolbarMenuScene.class;
    private static Class<? extends BasicCpxHandleManager> CPX_HANDLE_MANAGER_CLASS = DummyCpxHandleManager.class;
//    private final CpxSystemPropertiesInterface cpxProperties = CpxSystemProperties.getInstance();
    private  DictionaryChecker checker = new DictionaryChecker();
    private final AtomicInteger taskCounter = new AtomicInteger();
    private final AtomicInteger tasksDoneCounter = new AtomicInteger();

    private final AtomicLong entryCounter = new AtomicLong();
    private final AtomicLong entryDoneCounter = new AtomicLong();

    @FXML
    private CheckBox icdDone;
    @FXML
    private CheckBox opsDone;
    @FXML
    private CheckBox aopDone;
    @FXML
    private CheckBox drgDone;
    @FXML
    private CheckBox peppDone;
    @FXML
    private CheckBox atcDone;
    @FXML
    private CheckBox pznDone;
    @FXML
    private CheckBox hospitalsDone;
    @FXML
    private CheckBox insuranceCompanysDone;
    @FXML
    private CheckBox doctorsDone;
    @FXML
    private CheckBox departmentsDone;
    @FXML
    private CheckBox mdkDone;
    @FXML
    private CheckBox drgFeeDone;
    @FXML
    private CheckBox peppFeeDone;
    @FXML
    private CheckBox dailyFeeDone;
    @FXML
    private CheckBox baserateDone;
    @FXML
    private ProgressBar progress;
    @FXML
    private Label icdLabel;
    @FXML
    private Label opsLabel;
    @FXML
    private Label aopLabel;
    @FXML
    private Label drgLabel;
    @FXML
    private Label peppLabel;
    @FXML
    private Label atcLabel;
    @FXML
    private Label pznLabel;
    @FXML
    private Label progressLabel;
    @FXML
    private Label hospitalsLabel;
    @FXML
    private Label insuranceCompanysLabel;
    @FXML
    private Label doctorsLabel;
    @FXML
    private Label departmentsLabel;
    @FXML
    private Label drgFeeLabel;
    @FXML
    private Label peppFeeLabel;
    @FXML
    private Label dailyFeeLabel;
    @FXML
    private Label mdkLabel;
    @FXML
    private Label baserateLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label coreDataLabel;
    @FXML
    private Label catalogsLabel;
    @FXML
    private ProgressIndicator icdThrobber;
    @FXML
    private ProgressIndicator opsThrobber;
   @FXML
    private ProgressIndicator aopThrobber;
    @FXML
    private ProgressIndicator drgThrobber;
    @FXML
    private ProgressIndicator peppThrobber;
    @FXML
    private ProgressIndicator atcThrobber;
    @FXML
    private ProgressIndicator pznThrobber;
    @FXML
    private ProgressIndicator hospitalsThrobber;
    @FXML
    private ProgressIndicator insuranceCompanysThrobber;
    @FXML
    private ProgressIndicator doctorsThrobber;
    @FXML
    private ProgressIndicator departmentsThrobber;
    @FXML
    private ProgressIndicator mdkThrobber;
    @FXML
    private ProgressIndicator drgFeeThrobber;
    @FXML
    private ProgressIndicator peppFeeThrobber;
    @FXML
    private ProgressIndicator dailyFeeThrobber;
    @FXML
    private ProgressIndicator baserateThrobber;
    @FXML
    private CheckBox icdThesaurusDone;
    @FXML
    private Label icdThesaurusLabel;
    @FXML
    private ProgressIndicator icdThesaurusThrobber;
    @FXML
    private CheckBox opsThesaurusDone;
    @FXML
    private Label opsThesaurusLabel;
    @FXML
    private ProgressIndicator opsThesaurusThrobber;
    @FXML
    private CheckBox lexikonDone;
    @FXML
    private Label lexikonLabel;
    @FXML
    private ProgressIndicator lexikonThrobber;

    public static synchronized void setToolbarMenuSceneClass(final Class<? extends BasicToolbarMenuScene> pClass) {
        if (pClass == null) {
            throw new IllegalArgumentException("Toolbar Menu Scene Class cannot be null!");
        }
        TOOLBAR_MENU_SCENE_CLASS = pClass;
    }

    public static synchronized void setCpxHandleManagerClass(final Class<? extends BasicCpxHandleManager> pClass) {
        if (pClass == null) {
            throw new IllegalArgumentException("CPX Handle Manager Class cannot be null!");
        }
        CPX_HANDLE_MANAGER_CLASS = pClass;
    }

    public static void setCatalogOverview(final Map<CatalogTypeEn, List<CpxCatalogOverview>> pCatalogOverview) {
        sCatalogOverview = pCatalogOverview;
    }
    //  @Override
//  public String getTitle() {
//    return Lang.getCatalogDownloadWindow_title();
//  }

    public static Map<CatalogTypeEn, List<CpxCatalogOverview>> checkCatalogUpdate() {
        return checkCatalogUpdate(true);
    }

    private static File getLockFile(){
        return currentLockFile;
    }
    
    private void setLockFile(File file){
        currentLockFile = file;
    }
    public static void waitForLock(final Callback<Properties, Void> pCallback) throws TimeoutException {
        LOG.log(Level.INFO, "Obtain lock for CPX catalog update on " + getLockFile().getParentFile().getAbsolutePath());
        long startTime = System.currentTimeMillis();
        final long maxWaitTime = 180000; //in ms
        final long timeStep = 5000; //in ms
        Properties props = null;
        int i = 0;
        while (true) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime > maxWaitTime) {
                throw new TimeoutException("cannot obtain lock within " + maxWaitTime + " ms");
            }
            if (!getLockFile().exists() || !FileUtils.isFileLock(getLockFile())) {
                return;
            }
            if (props == null) {
                props = new Properties();
                if (LOCK_INFO_FILE.exists()) {
                    try ( BufferedReader br = new BufferedReader(new FileReader(LOCK_INFO_FILE))) {
                        props.load(br);
                    } catch (IOException ex) {
                        LOG.log(Level.SEVERE, "cannot read from lock info file: " + LOCK_INFO_FILE.getAbsolutePath(), ex);
                    }
                }
                startTime = System.currentTimeMillis();
                elapsedTime = System.currentTimeMillis() - startTime;
            }
            long timeLeft = maxWaitTime - elapsedTime;
            if (pCallback != null) {
                props.setProperty("timeLeft", String.valueOf(timeLeft));
                pCallback.call(props);
            }
            long sleepTime = timeStep > timeLeft ? timeLeft : (timeStep - 25L);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {
                LOG.log(Level.FINE, "wait for lock was interrupted", ex);
                Thread.currentThread().interrupt();
            }
            i++;
        }
    }

    private static Set<CatalogTypeEn> getAllCatalogTypes() {
        final Set<CatalogTypeEn> typeSet = Collections.newSetFromMap(new ConcurrentHashMap<>());
        typeSet.addAll(Arrays.asList(CatalogTypeEn.values()));
        return typeSet;
    }

    public static void setAllowAllCatalogTypes() {
        ALLOWED_CATALOG_TYPES.clear();
        ALLOWED_CATALOG_TYPES.addAll(getAllCatalogTypes());
    }

    public static void setIgnoreAllCatalogTypes() {
        ALLOWED_CATALOG_TYPES.clear();
    }

//    public static boolean allowCatalogType(final CatalogTypeEn pCatalogType) {
//        if (pCatalogType == null) {
//            return false;
//        }
//        return ALLOWED_CATALOG_TYPES.add(pCatalogType);
//    }
    public static void setAllowedCatalogType(final CatalogTypeEn... pCatalogTypes) {
        setIgnoreAllCatalogTypes();
        for (CatalogTypeEn type : pCatalogTypes) {
            if (type != null) {
                ALLOWED_CATALOG_TYPES.add(type);
            }
        }
    }

//    public static boolean ignoreCatalogType(final CatalogTypeEn pCatalogType) {
//        if (pCatalogType == null) {
//            return false;
//        }
//        return ALLOWED_CATALOG_TYPES.remove(pCatalogType);
//    }
    public static void setIgnoredCatalogType(final CatalogTypeEn... pCatalogTypes) {
        setAllowAllCatalogTypes();
        for (CatalogTypeEn type : pCatalogTypes) {
            ALLOWED_CATALOG_TYPES.remove(type);
        }
    }

    public static Set<CatalogTypeEn> getAllowedCatalogTypes() {
//        if (ALLOWED_CATALOG_TYPES == null) {
//            return null;
//        }
        return new HashSet<>(ALLOWED_CATALOG_TYPES);
    }

    public static Set<CatalogTypeEn> getIgnoredCatalogTypes() {
        final Set<CatalogTypeEn> allowedCatalogTypes = getAllowedCatalogTypes();
        final Set<CatalogTypeEn> allCatalogTypes = getAllCatalogTypes();
        final Set<CatalogTypeEn> ignoreCatalogTypes = new HashSet<>();
        for (CatalogTypeEn type : allCatalogTypes) {
            if (!allowedCatalogTypes.contains(type)) {
                ignoreCatalogTypes.add(type);
            }
        }
        return ignoreCatalogTypes;
    }

    public static boolean isCatalogTypeAllowed(final CatalogTypeEn pCatalogType) {
        return pCatalogType != null && ALLOWED_CATALOG_TYPES.contains(pCatalogType);
    }

    public static Map<CatalogTypeEn, List<CpxCatalogOverview>> checkCatalogUpdate(final boolean pGetOutdatedOnly) {
//        obtainLock(null);
        EjbProxy<CatalogImportServiceEJBRemote> catalogImportService = Session.instance().getEjbConnector().connectCatalogImportServiceBean();
        AbstractCpxCatalog.createCatalogOverviewDb();
        //cpxCatalog.createCatalogDbs();
        List<CpxCatalogOverview> catalogOverviewListTmp = AbstractCpxCatalog.getCatalogOverviews();

        //listTmp.add(new CpxCatalogOverview("C_ICD_CATALOG", "de", 2015, 1000, 1L, 10000L, new Date()));
        List<CpxCatalogOverview> catalogOverviewList = catalogImportService.get().checkCatalogOverview(catalogOverviewListTmp);
        Map<CatalogTodoEn, List<CpxCatalogOverview>> catalogOverviewMapByTodo = new LinkedHashMap<>();
        Map<CatalogTypeEn, List<CpxCatalogOverview>> catalogOverviewMapByCatalog = new LinkedHashMap<>();
        Iterator<CpxCatalogOverview> it = catalogOverviewList.iterator();
        while (it.hasNext()) {
            CpxCatalogOverview entry = it.next();
            //for(CpxCatalogOverview entry: catalogOverviewList) {
            if (entry == null) {
                continue;
            }
            //By Todo
            CatalogTodoEn todo = entry.getTodo();
            CatalogTypeEn type = entry.getCatalogType();

            if (pGetOutdatedOnly && todo == CatalogTodoEn.NONE) {
                it.remove();
                continue;
            }
            if (!isCatalogTypeAllowed(type)) {
                LOG.log(Level.FINER, "catalog of type '" + (type == null ? "null" : type.name()) + "' is not handled in this application");
                it.remove();
                continue;
            }

            if (catalogOverviewMapByTodo.get(todo) == null) {
                catalogOverviewMapByTodo.put(todo, new ArrayList<>());
            }
            catalogOverviewMapByTodo.get(todo).add(entry);

            //By Catalog
            CatalogTypeEn catalogType = entry.getCatalogType();
            if (catalogOverviewMapByCatalog.get(catalogType) == null) {
                catalogOverviewMapByCatalog.put(catalogType, new ArrayList<>());
            }
            catalogOverviewMapByCatalog.get(catalogType).add(entry);
        }
        for (Map.Entry<CatalogTodoEn, List<CpxCatalogOverview>> entry : catalogOverviewMapByTodo.entrySet()) {
            final boolean todoNone = entry.getKey() == CatalogTodoEn.NONE;
            LOG.log(Level.FINE, entry.getValue().size() + " catalogs with " + entry.getKey().name() + (!todoNone ? ": " : ""));
            if (!todoNone) {
                for (CpxCatalogOverview cpxCatalogOverview : entry.getValue()) {
                    LOG.log(Level.INFO, "-> " + cpxCatalogOverview.toString());
                }
            }
        }

        return catalogOverviewMapByCatalog;
    }

    public static Map<CatalogTodoEn, Integer> getCatalogChanges(final Map<CatalogTypeEn, List<CpxCatalogOverview>> pCatalogOverview) {
        if (pCatalogOverview == null) {
            LOG.log(Level.SEVERE, "No map of catalog overviews given");
            return new HashMap<>();
        }
        final Map<CatalogTodoEn, Integer> pTodoMap = new LinkedHashMap<>();
        for (Map.Entry<CatalogTypeEn, List<CpxCatalogOverview>> entry : pCatalogOverview.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            for (CpxCatalogOverview catalogOverview : entry.getValue()) {
                if (catalogOverview == null) {
                    continue;
                }
                //By Todo
                CatalogTodoEn todo = catalogOverview.getTodo();
                if (todo == CatalogTodoEn.NONE) {
                    continue;
                }
                if (pTodoMap.get(todo) == null) {
                    pTodoMap.put(todo, 1);
                } else {
                    int count = pTodoMap.get(todo);
                    count++;
                    pTodoMap.put(todo, count);
                }
            }
        }
        return pTodoMap;
    }

    public static boolean isCatalogOutdated(final Map<CatalogTypeEn, List<CpxCatalogOverview>> pCatalogOverview) {
        if (pCatalogOverview == null) {
            LOG.log(Level.SEVERE, "No map of catalog overviews given");
            return false;
        }
        return !getCatalogChanges(pCatalogOverview).isEmpty();
    }

    public static void initializeCatalogDbConnections() {
        //Initialize SQLite connections
        List<CpxCatalogOverview> catalogOverviewListTmp = AbstractCpxCatalog.getCatalogOverviews();
        if (catalogOverviewListTmp != null) {
            for (CpxCatalogOverview catalogOverview : catalogOverviewListTmp) {
                final boolean autoCreateConnection = true;
                LOG.log(Level.FINE, "Initialize catalog connection: " + catalogOverview.getTitleWithYear());
                if (AbstractCpxCatalog.getCatalogDb(catalogOverview, autoCreateConnection) != null) {
                    LOG.log(Level.FINER, "Catalog initialization successful: " + catalogOverview.getTitleWithYear());
                } else {
                    LOG.log(Level.SEVERE, "Catalog initialization failed: " + catalogOverview.getTitleWithYear());
                }
            }
        }
    }

    public static void startApp() throws CpxIllegalArgumentException {
        long start = System.currentTimeMillis();
        initializeCatalogDbConnections();
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        BasicMainApp.setScene(TOOLBAR_MENU_SCENE_CLASS.getDeclaredConstructor().newInstance());
                    } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        LOG.log(Level.SEVERE, null, ex);
                    }
                }
            });
            BasicCpxHandleManager.setInstance(CPX_HANDLE_MANAGER_CLASS.getDeclaredConstructor().newInstance());
            BasicCpxHandleManager.instance().start();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException ex) {
            LOG.log(Level.SEVERE, "Cannot initiate toolbar menu scene or CPX handle manager. Toolbar class and CPX handle manager each have to provide a public constructor with no arguments", ex);
        }
        LOG.finer("start app in " + (System.currentTimeMillis() - start));
    }

    private static boolean obtainLock(final Callback<Properties, Void> pCallback) {
        return obtainLock(1, pCallback);
    }

    private static boolean obtainLock(final int pTry, final Callback<Properties, Void> pCallback) {
        LOG.log(Level.INFO, pTry + ". try for catalog update");
        if (pTry >= 3) {
            LOG.log(Level.SEVERE, "no more tries left to check catalog update");
            Platform.runLater(() -> {
                AlertDialog dlg = AlertDialog.createErrorDialog("Das Herunterladen der Kataloge wurde durch einen anderen Client blockiert. Anzahl der Versuche aufgebraucht.\nDie Datei " + LOCK_FILE.getAbsolutePath() + " konnte nicht erstellt werden.\n\nEin fehlerfreier Betrieb kann nicht gewährleistet werden!", BasicMainApp.getWindow());
                dlg.showAndWait();
            });
            return false;
        }
        try {
            waitForLock(pCallback);
        } catch (TimeoutException ex) {
            LOG.log(Level.SEVERE, "timeout occured (wait for lock)", ex);
            return obtainLock(pTry + 1, pCallback);
        }
        return true;
    }

    public Map<CatalogTypeEn, List<CpxCatalogOverview>> getCatalogOverview() {
        return sCatalogOverview;
    }

    //private static final Logger log = Logger.getLogger(StartupLoaderFXMLController.class.getName());
    /**
     * Initializes the controller class.
     *
     * @param url URL
     * @param rb ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // CatalogTodoEn
        hospitalsLabel.setText(Lang.getCatalogDownloadHospitals());
        insuranceCompanysLabel.setText(Lang.getCatalogDownloadInsurance_companys());
        departmentsLabel.setText(Lang.getCatalogDownloadDepartments());
        doctorsLabel.setText(Lang.getCatalogDownloadDoctors());
        titleLabel.setText(Lang.getCatalogDownloadTitle());
        catalogsLabel.setText(Lang.getCatalogDownloadCatalogs());
        coreDataLabel.setText(Lang.getCatalogDownloadCore_data());
        drgFeeLabel.setText(Lang.getCatalogDownloadDrgFee());
        peppFeeLabel.setText(Lang.getCatalogDownloadPeppFee());
        dailyFeeLabel.setText(Lang.getCatalogDownloadDailyFee());
        afterShow();
    }

    protected Task<Boolean> downloadCatalog(final CpxCatalogOverview pCatalogOverview, final Callback<CpxCatalogOverview, CpxCatalogOverview> pStartCallback, final Callback<CpxCatalogOverview, CpxCatalogOverview> pFinishCallback) {
        if (pCatalogOverview == null) {
            return null;
        }
        //List<? extends Object> list = catalogImportService.getCatalog(pCatalogOverview);
        //cpxCatalog.insertCatalogData(pCatalogOverview, list);
        final EjbProxy<CatalogImportServiceEJBRemote> catalogImportService = Session.instance().getEjbConnector().connectCatalogImportServiceBean();
        Task<Boolean> futureTask = new Task<Boolean>() {
            @Override
            protected Boolean call() throws CpxIllegalArgumentException, SQLException {
                if (pStartCallback != null) {
                    pStartCallback.call(pCatalogOverview);
                }
                long start = System.currentTimeMillis();
                List<? extends AbstractCatalogEntity> list = catalogImportService.get().getCatalog(pCatalogOverview);
                LOG.log(Level.INFO, "get catalog list from db:" + pCatalogOverview.getTitleWithYear() + " " + String.valueOf(System.currentTimeMillis() - start));
                start = System.currentTimeMillis();
                boolean result = AbstractCpxCatalog.insertCatalogData(pCatalogOverview, list);
                LOG.log(Level.INFO, "save in sqllite:" + pCatalogOverview.getTitleWithYear() + " " + String.valueOf(System.currentTimeMillis() - start));
                if (pFinishCallback != null) {
                    pFinishCallback.call(pCatalogOverview);
                }
                return result;
            }
        };
        new Thread(futureTask).start();
        return futureTask;
    }

    protected void dropCatalog(final CpxCatalogOverview pCatalogOverview, final Callback<CpxCatalogOverview, CpxCatalogOverview> pStartCallback, final Callback<CpxCatalogOverview, CpxCatalogOverview> pFinishCallback) {
        if (pCatalogOverview == null) {
            return;
        }
        if (pStartCallback != null) {
            pStartCallback.call(pCatalogOverview);
        }
        AbstractCpxCatalog.dropCatalogOverview(pCatalogOverview);
        if (pFinishCallback != null) {
            pFinishCallback.call(pCatalogOverview);
        }
    }

    protected void doNothingWithCatalog(final CpxCatalogOverview pCatalogOverview, final Callback<CpxCatalogOverview, CpxCatalogOverview> pStartCallback, final Callback<CpxCatalogOverview, CpxCatalogOverview> pFinishCallback) {
        if (pStartCallback != null) {
            pStartCallback.call(pCatalogOverview);
        }
        if (pFinishCallback != null) {
            pFinishCallback.call(pCatalogOverview);
        }
    }

    public void setProgress(final String pText) {
        if (Platform.isFxApplicationThread()) {
            progressLabel.setText(pText);
        } else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    progressLabel.setText(pText);
                }
            });
        }

        //progressLabel.textProperty().bind(new SimpleStringProperty(pText));
        /*
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        progressLabel.setText(pText);
      }
    });
         */
    }


    /*
  public static boolean isCatalogOutdated(final List<CpxCatalogOverview> pCatalogOverview) {
    if (pCatalogOverview == null) {
      throw new CpxIllegalArgumentException("No catalog overview given");
    }
    boolean isOutdated = false;
    for(CpxCatalogOverview catalogOverview: pCatalogOverview) {
       if (catalogOverview == null) {
         continue;
       }
       //By Todo
       CatalogTodoEn todo = catalogOverview.getTodo();
       if (todo != null && todo != CatalogTodoEn.NONE) {
         isOutdated = true;
         break;
       }
    }
    return isOutdated;
  }
     */
    @Override
    public synchronized void afterShow() {
        startUpdate();
    }

    public void startUpdate() {
        for (CatalogTypeEn catalogType : CatalogTypeEn.values()) {
            ControlSet cs = new ControlSet(catalogType);
            final boolean isAllowed = isCatalogTypeAllowed(catalogType);
            //cs.label.setOpacity(isAllowed ? 1.0d : 0.2d);
            //cs.doneCheckBox.setOpacity(isAllowed ? 1.0d : 0.2d);
            //cs.progressThrobber.setOpacity(isAllowed ? 1.0d : 0.2d);
            //deactivate (fade-out) controls when they are not used in this application
            final String opacity = "-fx-opacity: " + (isAllowed ? "1.0" : "0.3") + ";";
            cs.label.setStyle(opacity);
            cs.doneCheckBox.setStyle(opacity);
            cs.progressThrobber.setStyle(opacity);
        }
// add Lexikon controlset
        ControlSet cs = new LexikonControlSet();
            final String opacity = "-fx-opacity: 1.0;";
            cs.label.setStyle(opacity);
            cs.doneCheckBox.setStyle(opacity);
            cs.progressThrobber.setStyle(opacity);

        setProgress(Lang.getCatalogDownloadProgressPriming());

        final CpxTask<Boolean> worker = new CpxTask<Boolean>(this) {
            @Override
            protected Boolean call() throws Exception {
                return executeUpdate();
            }
        };
        worker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            startApp();
                        } catch (CpxIllegalArgumentException ex) {
                            LOG.log(Level.SEVERE, null, ex);
                        }

                    }
                });
            }
        });
        new Thread(worker).start();
    }

    private boolean executeUpdate() {
        return executeUpdate(1);
    }

    private boolean executeUpdate(final int pTry) {
        boolean result = obtainLock((props) -> {
            final long timeLeft = Long.valueOf(props.getProperty("timeLeft"));
            setProgress("Warte auf anderen Client (" + props.getProperty("user_name") + " auf " + props.getProperty("host") + ")\n... noch " + (timeLeft / 1000) + " Sekunden");
            return null;
        });
        if (!result) {
            return false;
        }
        LOG.log(Level.INFO, "Write lock file for catalog update to " + LOCK_FILE.getAbsolutePath());
        try ( FileChannel channel = new RandomAccessFile(LOCK_FILE, "rw").getChannel();  FileLock lock = channel.tryLock(); //BufferedWriter writer = new BufferedWriter(new FileWriter(LOCK_FILE))) {
                ) {
            LOCK_FILE.deleteOnExit();
            try ( BufferedWriter writer = new BufferedWriter(new FileWriter(LOCK_INFO_FILE))) {
                writer.write("user_name=" + Session.instance().getCpxUserName() + "\r\n");
                writer.write("user_id=" + Session.instance().getCpxUserId() + "\r\n");
                writer.write("database=" + Session.instance().getCpxDatabase() + "\r\n");
                writer.write("host=" + CpxSystemProperties.getInstance().getHostName() + "\r\n");
                writer.write("since=" + Lang.toIsoDateTime(new Date()));
            }

            Map<CatalogTypeEn, List<CpxCatalogOverview>> catalogOverviewMapByCatalog = getCatalogOverview();
            setCatalogOverview(null);

            setProgress(Lang.getCatalogDownloadProgressSynchronization_started());

            if (catalogOverviewMapByCatalog == null) {
                catalogOverviewMapByCatalog = checkCatalogUpdate(false);
            }

            setProgress(Lang.getCatalogDownloadProgressSynchronization_finished());

            //final int taskCount = tasks;
            setProgress(Lang.getCatalogDownloadProgressDownload_started());

            //Calculcate task counter for progress bar
            int tasks = 0;
            int entries = 0;

            int[] arrTmp;

            for (CatalogTypeEn catalogType : CatalogTypeEn.values()) {
                arrTmp = load(catalogOverviewMapByCatalog.remove(catalogType), new ControlSet(catalogType));
                tasks += arrTmp[0];
                entries += arrTmp[1];
            }

            taskCounter.set(tasks);
            entryCounter.set(entries);

            List<String> ignoredCatalogs = new ArrayList<>();
            for (Map.Entry<CatalogTypeEn, List<CpxCatalogOverview>> entry : catalogOverviewMapByCatalog.entrySet()) {
                CatalogTypeEn catType = entry.getKey();
                if (catType != null) {
                    ignoredCatalogs.add(catType.name());
                }
            }
            String[] arr = new String[ignoredCatalogs.size()];
            ignoredCatalogs.toArray(arr);
            if (arr.length > 0) {
                BasicMainApp.showErrorMessageDialog("Server transmitted one or more catalogs which are not imported by this client: " + String.join(", ", arr));
            }

            if (taskCounter.get() > 0) {
                while (true) {
                    int tasksRunning = taskCounter.intValue() - tasksDoneCounter.intValue();
                    if (entryCounter.get() > 0 && progress != null) {
                        progress.setProgress(entryDoneCounter.doubleValue() / entryCounter.doubleValue());
                    }
                    if (tasksRunning <= 0) {
                        setProgress(Lang.getCatalogDownloadProgressDownload_finished());
                        
                         if (LOCK_INFO_FILE.exists()) {
                            try {
                                Files.delete(LOCK_INFO_FILE.toPath());
                                //LOG.log(Level.INFO, "deleted file: {0}", LOCK_INFO_FILE.getAbsolutePath());
                                LOG.log(Level.FINEST, "lock info file successfully deleted: " + LOCK_INFO_FILE.getAbsolutePath());
                            } catch (IOException ex) {
                                LOG.log(Level.WARNING, "was not able to delete lock info file: " + LOCK_INFO_FILE.getAbsolutePath(), ex);
                            }
//                            if (LOCK_INFO_FILE.delete()) {
//                                LOG.log(Level.FINEST, "lock info file successfully deleted: " + LOCK_INFO_FILE.getAbsolutePath());
//                            }
                        }
                        // check changed icd/ops catalog or thesaurus, they are to be finished,and locks exist
                        setProgress("Easycoderunterstützung runterladen");
                        setLockFile(LOCK_DICTIONARY_FILE);
                        boolean result1 = obtainLock((props) -> {
                            final long timeLeft = Long.valueOf(props.getProperty("timeLeft"));
                            setProgress("Warte auf anderen Client (" + props.getProperty("user_name") + " auf " + props.getProperty("host") + ")\n... noch " + (timeLeft / 1000) + " Sekunden");
                            return null;
                        });
                        if (!result1) {
                            return false;
                        }

                        LOG.log(Level.INFO, "Write lock file for catalog update to " + LOCK_DICTIONARY_FILE.getAbsolutePath());
                        try ( FileChannel channel1 = new RandomAccessFile(LOCK_DICTIONARY_FILE, "rw").getChannel();  
                                FileLock lock1 = channel1.tryLock(); //BufferedWriter writer = new BufferedWriter(new FileWriter(LOCK_FILE))) {
                                ) {
                            LOCK_DICTIONARY_FILE.deleteOnExit();

                            checker.checkIcdOpsDictionaries();
                            LOG.log(Level.INFO, "Download catalogs for {0} years", checker.getIcdOpsDict().size());
                            startLexikonTask(checker.getIcdOpsDict(), new LexikonControlSet());
                            
                            while(!checker.isWriteCompleted()){
                                try {
                                    Thread.sleep(500L);
                                } catch (InterruptedException ex) {
                                    LOG.log(Level.SEVERE, null, ex);
                                    Thread.currentThread().interrupt();
                                }
                            }
                            setProgress("Easycoderunterstützung runterladen beendet");
                        }
                        return true;
                    } else {
                        try {
                            Thread.sleep(500L);
                        } catch (InterruptedException ex) {
                            LOG.log(Level.SEVERE, null, ex);
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            } else {
                if (LOCK_INFO_FILE.exists()) {
                    try {
                        Files.delete(LOCK_INFO_FILE.toPath());
                        //LOG.log(Level.INFO, "deleted file: {0}", LOCK_INFO_FILE.getAbsolutePath());
                        LOG.log(Level.FINEST, "lock info file successfully deleted: " + LOCK_INFO_FILE.getAbsolutePath());
                    } catch (IOException ex) {
                        LOG.log(Level.WARNING, "was not able to delete lock info file: " + LOCK_INFO_FILE.getAbsolutePath(), ex);
                    }
//                    if (LOCK_INFO_FILE.delete()) {
//                        LOG.log(Level.FINEST, "lock info file successfully deleted: " + LOCK_INFO_FILE.getAbsolutePath());
//                    }
                }
                //taskCounter.get() <= 0
                //Nothing to do! All catalogs are (hopefully) up to date already
                return false;
            }
        } catch (OverlappingFileLockException | IOException ex) {
            LOG.log(Level.SEVERE, "Was not able to write lock file: " + LOCK_FILE.getAbsolutePath(), ex);
            return executeUpdate(pTry + 1);
        } finally {
            if (LOCK_FILE.exists()) {
                try {
                    Files.delete(LOCK_FILE.toPath());
                    //LOG.log(Level.INFO, "deleted file: {0}", LOCK_INFO_FILE.getAbsolutePath());
                    LOG.log(Level.FINEST, "lock file successfully deleted: " + LOCK_FILE.getAbsolutePath());
                } catch (IOException ex) {
                    LOG.log(Level.WARNING, "was not able to delete lock file: " + LOCK_FILE.getAbsolutePath() + ", will delete it on exit", ex);
                    LOCK_FILE.deleteOnExit();
                }
            }
            if (LOCK_DICTIONARY_FILE.exists()) {
                try {
                    Files.delete(LOCK_DICTIONARY_FILE.toPath());
                    //LOG.log(Level.INFO, "deleted file: {0}", LOCK_INFO_FILE.getAbsolutePath());
                    LOG.log(Level.FINEST, "lock file successfully deleted: " + LOCK_DICTIONARY_FILE.getAbsolutePath());
                } catch (IOException ex) {
                    LOG.log(Level.WARNING, "was not able to delete lock file: " + LOCK_DICTIONARY_FILE.getAbsolutePath() + ", will delete it on exit", ex);
                    LOCK_DICTIONARY_FILE.deleteOnExit();
                }
//                if (!LOCK_FILE.delete()) {
//                    LOCK_FILE.deleteOnExit();
//                }
            }
        }
    }
    
    
    //Ein wenig Dynamik hineinbringen. Spaeter wieder entfernen!
//  public static int getRandomInt(int pMin, int pMax) {
//    Random lRandomizer = new Random();
//    return lRandomizer.nextInt(pMax - pMin + 1) + pMin;
//  }
    private boolean proceedCatalog(final CpxCatalogOverview catalogOverview) throws InterruptedException, ExecutionException {
        boolean inAction = false;
        Callback<CpxCatalogOverview, CpxCatalogOverview> finishCallback = new Callback<CpxCatalogOverview, CpxCatalogOverview>() {
            @Override
            public CpxCatalogOverview call(final CpxCatalogOverview catalogOverview) {
                if (catalogOverview == null) {
                    LOG.log(Level.SEVERE, "catalogOverview is null!");
                    return null;
                }
                tasksDoneCounter.incrementAndGet();
                entryDoneCounter.addAndGet(getCatalogOverviewEntrySize(catalogOverview));
                String catalog = catalogOverview.getTitleWithYear();
                String text = "";
                //boolean refreshLabel = false;
                switch (catalogOverview.getTodo()) {
                    case DELETE:
                        text = Lang.getCatalogDownloadProgressCatalog_dropped(catalog);
                        //text += " was dropped";
                        break;
                    case IMPORT:
                    case REIMPORT:
                        text = Lang.getCatalogDownloadProgressCatalog_downloaded(catalog);
                        checker.add2DictionaryCheck(catalogOverview.getYear(), catalogOverview.getCatalogType());
                        //text += " was downloaded";
                        break;
                    case NONE:
                        //nothing to do here!
                        break;
                    default:
                        LOG.log(Level.WARNING, "Unknown todo type: " + catalogOverview.getTodo());
                }
                if (text != null && !text.isEmpty()) {
                    //final String labelText = text;
                    setProgress(text);
                    /*
          Platform.runLater(new Runnable() {
            @Override
            public void run() {
              setProgress(labelText);
            }
          });
                     */
                }
                return catalogOverview;
            }
        };

        Callback<CpxCatalogOverview, CpxCatalogOverview> startCallback = new Callback<CpxCatalogOverview, CpxCatalogOverview>() {
            @Override
            public CpxCatalogOverview call(final CpxCatalogOverview catalogOverview) {
                String catalog = catalogOverview.getTitleWithYear();
                String text = "";
                switch (catalogOverview.getTodo()) {
                    case DELETE:
                        text = Lang.getCatalogDownloadProgressCatalog_will_drop(catalog);
                        //text += " was dropped";
                        break;
                    case IMPORT:
                    case REIMPORT:
                        text = Lang.getCatalogDownloadProgressCatalog_will_download(catalog);
                        //text += " was downloaded";
                        break;
                    case NONE:
                        //nothing to do here!
                        break;
                    default:
                        LOG.log(Level.WARNING, "Unknown todo type: " + catalogOverview.getTodo());
                }
                if (text != null && !text.isEmpty()) {
                    //final String labelText = text;
                    setProgress(text);
                    /*
          Platform.runLater(new Runnable() {
            @Override
            public void run() {
              setProgress(labelText);
            }
          });
                     */
                }
                return catalogOverview;
            }
        };

        String signature = catalogOverview.getCatalog() + "/" + catalogOverview.getCountryEn() + "/" + catalogOverview.getYear() + "/" + catalogOverview.getTodo().name();

        if (catalogOverview.getTodo().equals(CatalogTodoEn.NONE)) {
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "Nothing to do for catalog " + signature + " (catalog on client is up to date)");
            doNothingWithCatalog(catalogOverview, startCallback, finishCallback);
        }
        if (catalogOverview.getTodo().equals(CatalogTodoEn.DELETE)) {
            inAction = true;
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Will drop catalog " + signature + " (catalog was removed from server)");
            dropCatalog(catalogOverview, startCallback, finishCallback);
        }
        if (catalogOverview.getTodo().equals(CatalogTodoEn.IMPORT) || catalogOverview.getTodo().equals(CatalogTodoEn.REIMPORT)) {
//            //TODO: CHECK AGAIN TO PREVENT DOUBLE IMPORT WHEN MULTIPLE CLIENTS ARE DOWNLOADING NEW STUFF!
//            List<CpxCatalogOverview> catalogOverviewListTmp = AbstractCpxCatalog.getCatalogOverviews(catalogOverview.countryEn, catalogOverview.catalog, catalogOverview.year);
//            if (!catalogOverviewListTmp.isEmpty()) {
//                EjbProxy<CatalogImportServiceEJBRemote> catalogImportService = Session.instance().getEjbConnector().connectCatalogImportServiceBean();
//                List<CpxCatalogOverview> catalogOverviewList = catalogImportService.get().checkCatalogOverview(catalogOverviewListTmp, true);
//                if (!catalogOverviewList.isEmpty()) {
//                    if (CatalogTodoEn.NONE.equals(catalogOverviewList.get(0).todo)) {
//                        //Another client has imported this catalog in meantime, don't reimport it again!
//                        return false;
//                    }
//                }
//            }

            inAction = true;
            if (catalogOverview.getTodo().equals(CatalogTodoEn.REIMPORT)) {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Will download catalog " + signature + " again (catalog on client is outdated or somehow corrupted or incomplete)");
                dropCatalog(catalogOverview, null, null);
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Will download catalog " + signature + " first time");
            }
            Task<Boolean> futureTask = downloadCatalog(catalogOverview, startCallback, finishCallback);
            futureTask.get();
            //Insert into catalog overview ALWAYS AFTER the download was successful!
            //cpxCatalog.insertCatalogOverview(entry.getCatalog(), entry.getCountryEn(), entry.getYear(), entry.getNewCount(), entry.getNewMinId(), entry.getNewMaxId(), entry.getNewDate());
        }
        return inAction;
    }

    private Thread startAsyncTask(final List<CpxCatalogOverview> pCatalogOverviewList, final ControlSet pControlSet) {
        if (pCatalogOverviewList == null) {
            //Nothing to import - disable/fade-out controls to make this clear
            return null;
        }
        //final Label label = pControlSet.label;
        final CheckBox doneChkBox = pControlSet.doneCheckBox;
        final ProgressIndicator progressThrobber = pControlSet.progressThrobber;
        /*
    boolean somethingToDoTmp = false; //are there entries to proceed with anyway?!?
    for(CpxCatalogOverview entry: pCatalogOverviewList) {    
      if (!entry.getTodo().equals(CatalogTodoEn.NONE)) {
        somethingToDoTmp = true;
        break;
      }
    }
    final boolean somethingToDo = somethingToDoTmp;
         */

        final int taskCount = pCatalogOverviewList.size();
        CpxTask<Void> worker = new CpxTask<Void>(this) {
            @Override
            protected Void call() throws Exception {
                //CatalogTodoEn: Stammdaten hier laden
                for (CpxCatalogOverview entry : pCatalogOverviewList) {
                    if (!entry.getTodo().equals(CatalogTodoEn.NONE) && progressThrobber != null) {
                        progressThrobber.setVisible(true);
                    }
                    boolean inAction = proceedCatalog(entry);
                }
                return null;
            }
        };
        worker.setOnSucceeded(getSucceedHandler(doneChkBox, progressThrobber));
        worker.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                LOG.log(Level.SEVERE, "failed to load catalogs", worker.getException());
                BasicMainApp.showErrorMessageDialog(worker.getException(), "failed to load catalogs");
//            MessageDialogBuilder
//                    .build()
//                    .setException(worker.getException())
//                    .show();
            }
        });
        RUNNING_TASKS.add(worker);
        Thread thread = new Thread(worker);
        thread.start();
        return thread;
    }

    private Thread startLexikonTask(final Map<Integer, List<CatalogTypeEn>> pIcdOpsDict, final ControlSet pControlSet) {
        //final Label label = pControlSet.label;
        final CheckBox doneChkBox = pControlSet.doneCheckBox;
        final ProgressIndicator progressThrobber = pControlSet.progressThrobber;
        if (pIcdOpsDict == null || pIcdOpsDict.isEmpty()) {
            doneChkBox.setSelected(true);
            //Nothing to import - disable/fade-out controls to make this clear
            return null;
        }
        /*

         */
        Set<Integer> years = pIcdOpsDict.keySet();
        final int taskCount = years.size();
        CpxTask<Void> worker = new CpxTask<Void>(this) {
            @Override
            protected Void call() throws Exception {
                //CatalogTodoEn: Stammdaten hier laden
                if ( progressThrobber != null) {
                    progressThrobber.setVisible(true);
                }
                checker.resetCount();
                for (int year: years) {
                    List<CatalogTypeEn> types = pIcdOpsDict.get(year);
                    if( checker.writeDictionaries4Year(year, types)){
                        checker.refreshCount();
                    }
                }
                return null;
            }
        };
        worker.setOnSucceeded(getSucceedHandler(doneChkBox, progressThrobber));
        worker.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                LOG.log(Level.SEVERE, "failed to load dictionary", worker.getException());
                BasicMainApp.showErrorMessageDialog(worker.getException(), "failed to load dictionary");
//            MessageDialogBuilder
//                    .build()
//                    .setException(worker.getException())
//                    .show();
            }
        });

        Thread thread = new Thread(worker);
        thread.start();
        return thread;
    }

    public EventHandler<WorkerStateEvent> getSucceedHandler(final CheckBox pDoneChkBox, final ProgressIndicator pProgressThrobber) {
        return new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (pDoneChkBox != null) {
                            pDoneChkBox.setSelected(true);
                        }
                        if (pProgressThrobber != null) {
                            pProgressThrobber.setVisible(false);
                        }
                        //icdDone.setSelected(true);
                    }
                });
            }
        };
    }

    private int getCatalogOverviewTaskSize(final List<CpxCatalogOverview> pCatalogOverviewList) {
        if (pCatalogOverviewList == null) {
            return 0;
        }
        return pCatalogOverviewList.size();
    }

    private int getCatalogOverviewEntrySize(final List<CpxCatalogOverview> pCatalogOverviewList) {
        int size = 0;
        if (pCatalogOverviewList == null) {
            return 0;
        }
        for (CpxCatalogOverview entry : pCatalogOverviewList) {
            if (entry == null) {
                continue;
            }
            size += getCatalogOverviewEntrySize(entry);
        }
        return size;
    }

    private int getCatalogOverviewEntrySize(final CpxCatalogOverview pCatalogOverview) {
        if (pCatalogOverview == null) {
            return 0;
        }
        if (pCatalogOverview.getNewCount() > 0) {
            return pCatalogOverview.getNewCount();
        } else {
            return pCatalogOverview.getCount();
        }
    }

    private int[] load(final List<CpxCatalogOverview> pCatalogOverviewList, final ControlSet pControlSet) {
        startAsyncTask(pCatalogOverviewList, pControlSet);
        return new int[]{getCatalogOverviewTaskSize(pCatalogOverviewList), getCatalogOverviewEntrySize(pCatalogOverviewList)};
    }


//    private int[] loadDrg(final List<CpxCatalogOverview> pCatalogOverviewList) {
//        startAsyncTask(pCatalogOverviewList, /* CatalogTypeEn.DRG, */ drgLabel, drgDone, drgThrobber);
//        return new int[]{getCatalogOverviewTaskSize(pCatalogOverviewList), getCatalogOverviewEntrySize(pCatalogOverviewList)};
//    }
//
//    private int[] loadPepp(final List<CpxCatalogOverview> pCatalogOverviewList) {
//        startAsyncTask(pCatalogOverviewList, /* CatalogTypeEn.PEPP, */ peppLabel, peppDone, peppThrobber);
//        return new int[]{getCatalogOverviewTaskSize(pCatalogOverviewList), getCatalogOverviewEntrySize(pCatalogOverviewList)};
//    }
//
//    private int[] loadIcd(final List<CpxCatalogOverview> pCatalogOverviewList) {
//        startAsyncTask(pCatalogOverviewList, /* CatalogTypeEn.ICD, */ icdLabel, icdDone, icdThrobber);
//        return new int[]{getCatalogOverviewTaskSize(pCatalogOverviewList), getCatalogOverviewEntrySize(pCatalogOverviewList)};
//    }
//
//    private int[] loadOps(final List<CpxCatalogOverview> pCatalogOverviewList) {
//        startAsyncTask(pCatalogOverviewList, /* CatalogTypeEn.OPS, */ opsLabel, opsDone, opsThrobber);
//        return new int[]{getCatalogOverviewTaskSize(pCatalogOverviewList), getCatalogOverviewEntrySize(pCatalogOverviewList)};
//    }
//
//    private int[] loadIcdThesaurus(final List<CpxCatalogOverview> pCatalogOverviewList) {
//        startAsyncTask(pCatalogOverviewList, /* CatalogTypeEn.ICD_THESAURUS, */ icdThesaurusLabel, icdThesaurusDone, icdThesaurusThrobber);
//        return new int[]{getCatalogOverviewTaskSize(pCatalogOverviewList), getCatalogOverviewEntrySize(pCatalogOverviewList)};
//    }
//
//    private int[] loadOpsThesaurus(final List<CpxCatalogOverview> pCatalogOverviewList) {
//        startAsyncTask(pCatalogOverviewList, /* CatalogTypeEn.OPS_THESAURUS, */ opsThesaurusLabel, opsThesaurusDone, opsThesaurusThrobber);
//        return new int[]{getCatalogOverviewTaskSize(pCatalogOverviewList), getCatalogOverviewEntrySize(pCatalogOverviewList)};
//    }
//
//    private int[] loadHospitals(final List<CpxCatalogOverview> pCatalogOverviewList) {
//        startAsyncTask(pCatalogOverviewList, /* CatalogTypeEn.HOSPITAL, */ hospitalsLabel, hospitalsDone, hospitalsThrobber);
//        return new int[]{getCatalogOverviewTaskSize(pCatalogOverviewList), getCatalogOverviewEntrySize(pCatalogOverviewList)};
//    }
//
//    private int[] loadInsuranceCompanys(final List<CpxCatalogOverview> pCatalogOverviewList) {
//        startAsyncTask(pCatalogOverviewList, /* CatalogTypeEn.INSURANCE_COMPANY, */ insuranceCompanysLabel, insuranceCompanysDone, insuranceCompanysThrobber);
//        return new int[]{getCatalogOverviewTaskSize(pCatalogOverviewList), getCatalogOverviewEntrySize(pCatalogOverviewList)};
//    }
//
//    private int[] loadDoctors(final List<CpxCatalogOverview> pCatalogOverviewList) {
//        startAsyncTask(pCatalogOverviewList, /* CatalogTypeEn.DOCTOR, */ doctorsLabel, doctorsDone, doctorsThrobber);
//        return new int[]{getCatalogOverviewTaskSize(pCatalogOverviewList), getCatalogOverviewEntrySize(pCatalogOverviewList)};
//    }
//
//    private int[] loadDepartments(final List<CpxCatalogOverview> pCatalogOverviewList) {
//        startAsyncTask(pCatalogOverviewList, /* CatalogTypeEn.DEPARTMENT, */ departmentsLabel, departmentsDone, departmentsThrobber);
//        return new int[]{getCatalogOverviewTaskSize(pCatalogOverviewList), getCatalogOverviewEntrySize(pCatalogOverviewList)};
//    }
//
//    private int[] loadAtc(final List<CpxCatalogOverview> pCatalogOverviewList) {
//        startAsyncTask(pCatalogOverviewList, /* CatalogTypeEn.ATC, */ atcLabel, atcDone, atcThrobber);
//        return new int[]{getCatalogOverviewTaskSize(pCatalogOverviewList), getCatalogOverviewEntrySize(pCatalogOverviewList)};
//    }
//
//    private int[] loadPzn(final List<CpxCatalogOverview> pCatalogOverviewList) {
//        startAsyncTask(pCatalogOverviewList, /* CatalogTypeEn.PZN, */ pznLabel, pznDone, pznThrobber);
//        return new int[]{getCatalogOverviewTaskSize(pCatalogOverviewList), getCatalogOverviewEntrySize(pCatalogOverviewList)};
//    }
//
//    private int[] loadMdk(final List<CpxCatalogOverview> pCatalogOverviewList) {
//        startAsyncTask(pCatalogOverviewList, /* CatalogTypeEn.MDK, */ mdkLabel, mdkDone, mdkThrobber);
//        return new int[]{getCatalogOverviewTaskSize(pCatalogOverviewList), getCatalogOverviewEntrySize(pCatalogOverviewList)};
//    }
//
//    private int[] loadDrgFee(final List<CpxCatalogOverview> pCatalogOverviewList) {
//        startAsyncTask(pCatalogOverviewList, /* CatalogTypeEn.ZE, */ drgFeeLabel, drgFeeDone, drgFeeThrobber);
//        return new int[]{getCatalogOverviewTaskSize(pCatalogOverviewList), getCatalogOverviewEntrySize(pCatalogOverviewList)};
//    }
//
//    private int[] loadPeppFee(final List<CpxCatalogOverview> pCatalogOverviewList) {
//        startAsyncTask(pCatalogOverviewList, /* CatalogTypeEn.ZP, */ peppFeeLabel, peppFeeDone, peppFeeThrobber);
//        return new int[]{getCatalogOverviewTaskSize(pCatalogOverviewList), getCatalogOverviewEntrySize(pCatalogOverviewList)};
//    }
//
//    private int[] loadDailyFee(final List<CpxCatalogOverview> pCatalogOverviewList) {
//        startAsyncTask(pCatalogOverviewList, /* CatalogTypeEn.ET, */ dailyFeeLabel, dailyFeeDone, dailyFeeThrobber);
//        return new int[]{getCatalogOverviewTaskSize(pCatalogOverviewList), getCatalogOverviewEntrySize(pCatalogOverviewList)};
//    }
//
//    private int[] loadBaserate(final List<CpxCatalogOverview> pCatalogOverviewList) {
//        startAsyncTask(pCatalogOverviewList, /* CatalogTypeEn.BASERATE, */ baserateLabel, baserateDone, baserateThrobber);
//        return new int[]{getCatalogOverviewTaskSize(pCatalogOverviewList), getCatalogOverviewEntrySize(pCatalogOverviewList)};
//    } 
    private class ControlSet {

        public final CatalogTypeEn catalogType;
        public final Label label;
        public final CheckBox doneCheckBox;
        public final ProgressIndicator progressThrobber;
        
        public ControlSet(Label pLabel, CheckBox pCheckBox, ProgressIndicator pProgressThrobber){
            catalogType = null;
            doneCheckBox = pCheckBox;
            progressThrobber = pProgressThrobber;
            label = pLabel;
        }

        public ControlSet(final CatalogTypeEn pCatalogType) {
            catalogType = pCatalogType;
            if (pCatalogType == null) {
                LOG.log(Level.WARNING, "catalog type is null!");
                label = null;
                doneCheckBox = null;
                progressThrobber = null;
            } else {
                switch (pCatalogType) {
                    case BASERATE:
                        label = baserateLabel;
                        doneCheckBox = baserateDone;
                        progressThrobber = baserateThrobber;
                        break;
                    case ET: //Daily Fee
                        label = dailyFeeLabel;
                        doneCheckBox = dailyFeeDone;
                        progressThrobber = dailyFeeThrobber;
                        break;
                    case ZP: //Pepp Fee
                        label = peppFeeLabel;
                        doneCheckBox = peppFeeDone;
                        progressThrobber = peppFeeThrobber;
                        break;
                    case ZE: //Drg Fee
                        label = drgFeeLabel;
                        doneCheckBox = drgFeeDone;
                        progressThrobber = drgFeeThrobber;
                        break;
                    case MDK:
                        label = mdkLabel;
                        doneCheckBox = mdkDone;
                        progressThrobber = mdkThrobber;
                        break;
                    case PZN:
                        label = pznLabel;
                        doneCheckBox = pznDone;
                        progressThrobber = pznThrobber;
                        break;
                    case ATC:
                        label = atcLabel;
                        doneCheckBox = atcDone;
                        progressThrobber = atcThrobber;
                        break;
                    case DEPARTMENT:
                        label = departmentsLabel;
                        doneCheckBox = departmentsDone;
                        progressThrobber = departmentsThrobber;
                        break;
                    case DOCTOR:
                        label = doctorsLabel;
                        doneCheckBox = doctorsDone;
                        progressThrobber = doctorsThrobber;
                        break;
                    case INSURANCE_COMPANY:
                        label = insuranceCompanysLabel;
                        doneCheckBox = insuranceCompanysDone;
                        progressThrobber = insuranceCompanysThrobber;
                        break;
                    case HOSPITAL:
                        label = hospitalsLabel;
                        doneCheckBox = hospitalsDone;
                        progressThrobber = hospitalsThrobber;
                        break;
                    case OPS_THESAURUS:
                        label = opsThesaurusLabel;
                        doneCheckBox = opsThesaurusDone;
                        progressThrobber = opsThesaurusThrobber;
                        break;
                    case ICD_THESAURUS:
                        label = icdThesaurusLabel;
                        doneCheckBox = icdThesaurusDone;
                        progressThrobber = icdThesaurusThrobber;
                        break;
                    case OPS_TRANSFER:
                    case OPS:
                        label = opsLabel;
                        doneCheckBox = opsDone;
                        progressThrobber = opsThrobber;
                        break;
                    case OPS_AOP:
                        label = aopLabel;
                        doneCheckBox = aopDone;
                        progressThrobber = aopThrobber;
                        break;
                    case ICD_TRANSFER:
                    case ICD:
                        label = icdLabel;
                        doneCheckBox = icdDone;
                        progressThrobber = icdThrobber;
                        break;
                    case PEPP:
                        label = peppLabel;
                        doneCheckBox = peppDone;
                        progressThrobber = peppThrobber;
                        break;
                    case DRG:
                        label = drgLabel;
                        doneCheckBox = drgDone;
                        progressThrobber = drgThrobber;
                        break;


                    default:
                        throw new IllegalArgumentException("this catalog type is not supported: " + pCatalogType.name());
                }
            }
        }

    }
    private final class LexikonControlSet extends ControlSet{
        
        public LexikonControlSet(){
            super( lexikonLabel, lexikonDone, lexikonThrobber);
        }
        
    }

}
