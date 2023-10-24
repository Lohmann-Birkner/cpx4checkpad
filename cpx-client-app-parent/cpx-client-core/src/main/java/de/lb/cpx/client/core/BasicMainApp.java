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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core;

import de.checkpoint.enums.AppTypeEn;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.connection.database.CpxDbManager;
import de.lb.cpx.client.core.menu.fx.BasicToolbarMenuScene;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.alert.AuthorizationDialog;
import de.lb.cpx.client.core.model.fx.alert.LockCallback;
import de.lb.cpx.client.core.model.fx.alert.LockDialog;
import de.lb.cpx.client.core.model.fx.file_chooser.FileChooserControl;
import de.lb.cpx.client.core.model.fx.labeled.LabeledListView;
import de.lb.cpx.client.core.model.fx.listview.AsyncListView;
import de.lb.cpx.client.core.model.fx.login.LoginScene;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.cpx.license.crypter.License;
import de.lb.cpx.reader.DocumentReader;
import de.lb.cpx.reader.exception.ReaderException;
import de.lb.cpx.reader.exception.ReaderExceptionTypeEn;
import de.lb.cpx.service.properties.CpxAuthorizationException;
import de.lb.cpx.shared.dto.LockException;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.version.Version;
import de.lb.cpx.shared.version.VersionHistory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javax.naming.NamingException;
import org.controlsfx.tools.ValueExtractor;

/**
 * BasicMainApp class for the Application, handles basic stuff regarding its
 * stage initialize content on start and shutdown on stop TODO: Maybe put stage
 * elsewhere in the client, maybe some static client class is better suited,
 * also for refactoring client properties handling
 *
 * @author wilde
 */
public abstract class BasicMainApp extends Application {

    private static AppTypeEn type;
    protected static final ObjectProperty<Logger> LOG = new SimpleObjectProperty<>(null);
//    public static final StringProperty LOGFILE_NAME = new SimpleStringProperty();
//    public static final StringProperty LOGFILE_PATH = new SimpleStringProperty();
//    protected static final Logger LOG;
    protected static BasicMainApp INSTANCE;
    protected static final StringProperty STAGE_TITLE_PROPERTY = new SimpleStringProperty(null);
    protected static Stage stage = null;

    static {
        System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
    }

    public static void setType(final AppTypeEn pAppTypeEn) {
        if (type == null) {
            type = pAppTypeEn;
        } else {
            throw new IllegalStateException("client type already set");
        }
    }

    public static void setDefaultLocale() {
        //CPX-1450
//        Locale.setDefault(new Locale("de", "DE"));
//        System.setProperty("user.language", "de-DE");
    }

    public static AppTypeEn getType() {
        return type;
    }

    public static boolean isCore() {
        return type == null ? false : type.isCore();
    }

    public static boolean isRuleEditor() {
        return type == null ? false : type.isRuleEditor();
    }

    public static boolean isClient() {
        return type == null ? false : type.isClient();
    }

    public static boolean getNeedsDatabase() {
        return type == null ? true : type.getNeedsDatabase();
    }

    private static boolean checkReaderException(Throwable ex) {
        if(ex instanceof ReaderException ){
            LOG.get().log(Level.INFO, "ReaderException message=" + ((ReaderException)ex).getMessage() + " ReaderExceptionTypeEn = " + ((ReaderException)ex).getReaderExceptionId().toString());
            if(((ReaderException)ex).getReaderExceptionId().equals(ReaderExceptionTypeEn.NOT_SET)){
                return false;
            }
        }else{
            return false;
        }

        ReaderExceptionTypeEn exId = ((ReaderException)ex).getReaderExceptionId();
         if(exId.equals(ReaderExceptionTypeEn.EXCEL_TOO_OLD) 
                || exId.equals(ReaderExceptionTypeEn.WORD_TOO_OLD)
                || exId.equals(ReaderExceptionTypeEn.OUTLOOK_TOO_OLD)
                || exId.equals(ReaderExceptionTypeEn.OFFICE_TOO_OLD) ){
            showWarningMessageDialog(ex.getMessage() 
                    + "\n" + Lang.getSomeFeachersAreDeactivated()
            );
        }else {
             showWarningMessageDialog(ex.getMessage());

        }
            return true;
       
    }

//    protected static void setLogger(final Logger pLogger) {
//        LOG.set(pLogger);
//    }
    public final String version;
//    private final boolean isCoreVersion;

    /**
     * CPX Client Version (including svn revision/build number and build
     * date/time)
     *
     * @return version
     */
    public final String getVersion() {
        return version;
    }

    public static void centerWindow(final Window pWindow) {
        if (pWindow == null) {
            return;
        }
        pWindow.addEventHandler(WindowEvent.WINDOW_SHOWN, new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                BasicMainApp.getStage().setIconified(false);
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                double CENTER_ON_SCREEN_X_FRACTION = 1.0d / 2;
                double CENTER_ON_SCREEN_Y_FRACTION = 1.0d / 3;
                pWindow.setX((screenBounds.getWidth() - pWindow.getWidth()) * CENTER_ON_SCREEN_X_FRACTION);
                pWindow.setY((screenBounds.getHeight() - pWindow.getHeight()) * CENTER_ON_SCREEN_Y_FRACTION);
            }
        });
    }

    /**
     * Get all the currently running threads
     *
     * @return running threads
     */
    public static Thread[] getThreads() {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        return threadSet.toArray(new Thread[threadSet.size()]);
    }

    public BasicMainApp(/* final boolean pIsCoreVersion, */final String pVersion) {
//        isCoreVersion = pIsCoreVersion;
        version = pVersion == null ? "" : pVersion.trim();
        Version recentVersion = VersionHistory.getRecentVersion();
        LOG.get().log(Level.INFO, "CPX Client Version: " + recentVersion.getVersion() + " (veröffentlicht am " + recentVersion.getDateGerman() + "), " + version);
        Locale.setDefault(new Locale("de", "DE"));
    }

    /**
     * don't use logger in this method, overwise in Javas Logger class the
     * property manager is initialized by Java Util Logger (JUL) before we are
     * able to setup LOG4J2 with
     * System.getProperty("java.util.logging.manager").
     */
    protected static void initApp() {
        final File logFile = InitLogger.instance().getLogFile();
        LOG.set(Logger.getLogger(BasicMainApp.class.getName()));
        if (logFile == null) {
            LOG.get().log(Level.SEVERE, "log file is null!");
        }
//        LOGFILE_NAME.set(logFile.getName());
//        LOGFILE_PATH.set(logFile.getParentFile().getAbsolutePath() + System.getProperty("file.separator"));
        if(!isRuleEditor()){
            // rule editor does not use ms office, don't need check
                initDocumentReader();
        }
        initValidators();
    }

    private static void initDocumentReader() {
        Thread th = new Thread(() -> {
            try{
                //AGE, CPX-2410: we need sleep, because the exception with the message "office not found" 
                // can be shown only when the main sceen is completely built
                // otherwise the exception message will go lost.
                // Is not a very good solution, but the cheap one and it works
                Thread.sleep(500);
            }catch(Exception ex){
                
            }
            DocumentReader.checkOfficeFound();
//            }catch(Exception ex){
//                LOG.get().log(Level.INFO, ex.getMessage());
//                checkReaderException(ex);
//            }
        });
        th.start();
    }

    private static void initValidators() {
//Costum value extractors for different controls 
        //used by validation tool to mark controls as invalid
        ValueExtractor.addObservableValueExtractor(new Predicate<Control>() {
            @Override
            public boolean test(Control t) {
                return t instanceof LabeledListView;
            }
            //calls registered validators on changes of the selected item
        }, c -> ((LabeledListView) c).getSelectedItemProperty());
        ValueExtractor.addObservableValueExtractor(new Predicate<Control>() {
            @Override
            public boolean test(Control t) {
                return t instanceof TreeView;
            }
        }, c -> ((TreeView) c).editableProperty());
        ValueExtractor.addObservableValueExtractor(new Predicate<Control>() {
            @Override
            public boolean test(Control t) {
                return t instanceof TreeCell;
            }
        }, c -> ((TreeCell) c).editableProperty());
        ValueExtractor.addObservableValueExtractor(new Predicate<Control>() {
            @Override
            public boolean test(Control t) {
                return t instanceof Label;
            }
        }, c -> ((Label) c).textProperty());
        ValueExtractor.addObservableValueExtractor(new Predicate<Control>() {
            @Override
            public boolean test(Control t) {
                return t instanceof FileChooserControl;
            }
        }, c -> ((FileChooserControl) c).fileProperty());
        ValueExtractor.addObservableValueExtractor(new Predicate<Control>() {
            @Override
            public boolean test(Control t) {
                return t instanceof AsyncListView;
            }
            //calls registered validators on changes of the selected item
        }, c -> ((AsyncListView) c).getSelectionModel().selectedItemProperty());
//        ValueExtractor.addObservableValueExtractor(new Predicate<Control>() {
//            @Override
//            public boolean test(Control t) {
//                return t instanceof AsyncTableView;
//            }
//        }, c -> ((AsyncTableView) c).isEmptyProperty());
    }

//    public static List<InputStream> loadResources(final String name, final ClassLoader pClassLoader) throws IOException {
//        final List<InputStream> list = new ArrayList<>();
//        final Enumeration<URL> systemResources = (pClassLoader == null ? ClassLoader.getSystemClassLoader() : pClassLoader).getResources(name);
//        while (systemResources.hasMoreElements()) {
//            list.add(systemResources.nextElement().openStream());
//        }
//        return list;
//    }
    protected static String readApiVersion() {
//        if (pStream == null) {
//            throw new IllegalArgumentException("Input stream cannot be null. Ensure that passed input stream belongs to the project that you want to read MANIFEST.MF");
//        }
        final String path = "/version.prop";
//        List<InputStream> streams;
//        try {
//            streams = loadResources(path, null);
//        } catch (IOException ex) {
//            LOG.get().log(Level.SEVERE, "Cannot fetch input stream for " + path, ex);
//            return "INPUT STREAM ERROR";
//        }
//        try(InputStream stream = streams.get(streams.size() - 1)) {
        try ( InputStream stream = BasicMainApp.class.getResourceAsStream(path)) {
            if (stream == null) {
                return "UNKNOWN";
            }
            Properties props = new Properties();
            //try {
            props.load(stream);
            //stream.close();
            final String version = (String) props.get("version");

            final String buildRevision = (String) props.get("buildRevision");
            Integer revision = null;
            try {
                revision = Integer.valueOf(buildRevision);
            } catch (NumberFormatException ex) {
                LOG.get().log(Level.FINEST, "Cannot read client build number (svn revision), " + buildRevision + " is not a valid integer", ex);
            }

            final String buildTimestamp = (String) props.get("buildTimestamp");
            Date buildDate = null;
            try {
                final long timestamp = Long.valueOf(buildTimestamp);
                buildDate = new Date(timestamp);
            } catch (NumberFormatException ex) {
                LOG.get().log(Level.FINEST, "Cannot read client build date, " + buildTimestamp + " is not a valid timestamp", ex);
            }

            if (revision == null && buildDate == null) {
                LOG.get().log(Level.WARNING, "Was not able able to fetch build number (svn revision) and build timestamp -> maybe buildnumber-maven-plugin failed, check your pom.xml settings and maven output. Maybe you want to install TortoiseSVN with command line client tools if svn.exe is missing.");
                return version;
            } else {
                return version + " (Rev. " + (revision == null ? buildRevision : revision) + ") vom " + (buildDate == null ? "UNKNOWN" : Lang.toDate(buildDate) + ", " + Lang.toTime(buildDate));
            }
        } catch (IOException e) {
            LOG.get().log(Level.WARNING, (MessageFormat.format("Was not able to read version from file {0}", path)), e);
            return "UNKNOWN";
        }
    }

    public static void setStageTitle(String pText) {
        final License license = Session.instance().getLicense();
//        DatabaseInfo databaseInfo = Session.instance().getCpxDatabaseInfo();
//        if (databaseInfo == null) {
//            databaseInfo = Session.instance().getCpxDatabaseInfoCommon();
//        }
        final String licenseInfo = (license == null ? "Nicht lizensiert" : license.getTitle());
        if (pText == null) {
            pText = "checkpoint x";
        }
        pText += " - " + licenseInfo;
//        if (databaseInfo != null) {
//            pText += " (" + databaseInfo.getIdentifier() + ")";
//        }
        STAGE_TITLE_PROPERTY.setValue(pText);
    }

    protected static void setStageTitleBinding(StringProperty pText) {
        stage.titleProperty().bind(pText);
    }

    public static void setStageIcon(String pImageName) {
        stage.getIcons().add(ResourceLoader.getImage(pImageName));
    }

    /**
     * force close the application, destroys the session exit on Platform and
     * exit in runtime
     */
    public static void closeApplication() {
        LOG.get().log(Level.INFO, "Will close application now...");
        Session.destroy();
        Platform.exit();
        Runtime.getRuntime().exit(0);
        //Runtime.getRuntime().halt(0);
    }

    /**
     * sets scene in stage, replaces the old one attempts resize of the scene to
     * meet its bounds resize only when root is derived from pane
     *
     * @param scene to add to the stage
     */
    public static void setScene(Scene scene) {
        if (scene.getRoot() instanceof Pane) {
            ((Region) scene.getRoot()).prefWidthProperty().bind(stage.getScene().widthProperty());
            ((Region) scene.getRoot()).prefHeightProperty().bind(stage.getScene().heightProperty());
        }
        stage.setScene(scene);
    }

    public static BasicToolbarMenuScene getToolbarMenuScene() {
        return (BasicToolbarMenuScene) stage.getScene();
    }

    /**
     * Stage
     *
     * @return stage
     */
    public static Stage getStage() {
        return stage;
    }

    /**
     * Window
     *
     * @return window
     */
    public static Window getWindow() {
        return (stage == null || stage.getScene() == null) ? null : stage.getScene().getWindow();
    }

    /**
     * creates error dialog shown on top
     *
     * @param ex throwable, message is shown
     * @param pText text to show besides the message of the throwable
     */
    public static void showErrorMessageDialog(Throwable ex, String pText) {
        if (ex instanceof LockException) {
            showLockMessage((LockException) ex);
            return;
        }
        if(checkReaderException(ex)){
           
            return;
        }
        String text = pText == null ? "" : pText.trim();
        String exMessage = "";
        //boolean isException = false;
        if (ex != null) {
            //isException = true;
            if (ex.getMessage() != null) {
                if (ex.getMessage().equals(text)) {
                    AlertDialog.createErrorDialog(text, ex);
                    return;
                } else {
                    if (/* !ex.getMessage().contains(text) && */!text.contains(ex.getMessage())) {
                        exMessage = ex.getMessage();
                    }
                }
            }
        }
        AlertDialog.createErrorDialog((text.isEmpty() ? "" : text + "\n\n") + exMessage, "", stage, ex, ButtonType.OK).showAndWait();
//    	 else {
//            AlertDialog.createErrorDialog(Lang.getErrorOccured() + "\n"+ (text.isEmpty() ? "" : text + "\n"), stage, ButtonType.OK).showAndWait();
//            return;    
    }

    /**
     * creates error dialog shown on top
     *
     * @param ex throwable, message is shown
     */
    public static void showErrorMessageDialog(Throwable ex) {
        final Throwable exception = null;
        if(checkReaderException(ex)){
            return;
        }
        AlertDialog.createErrorDialog(ex.getMessage(), stage, ex, ButtonType.OK).showAndWait();
    }

    /**
     * creates error dialog shown on top
     *
     * @param ex Exception, message is shown
     */
    public static void showErrorMessageDialog(Exception ex) {
        if (ex instanceof LockException) {
            showLockMessage((LockException) ex);
            return;
        }
        if(checkReaderException(ex)){
            return;
        }

        final Throwable exception = null;
        AlertDialog.createErrorDialog(ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage(), stage, exception, ButtonType.OK).showAndWait();
    }

    /**
     * creates lock message dialog
     *
     * @param ex lock exception
     */
    public static void showLockMessage(final LockException ex) {
        showLockMessage(ex, "", true);
    }

    public static void showAuthorizationMessage(final CpxAuthorizationException ex) {
        showAuthorizationMessage(ex, ex.getMessage(), true);
    }

    public static void showAuthorizationMessage(final String message) {
        showAuthorizationMessage(null, message, true);
    }

    /**
     * creates lock message dialog
     *
     * @param ex lock exception
     * @param pText message
     */
    public static void showLockMessage(final LockException ex, final String pText) {
        showLockMessage(ex, pText, true);
    }

    public static <T> T execWithLockDialog(final LockCallback<?, T> pCallback) {
        final Window owner = null;
        return execWithLockDialog("", pCallback, null, owner, true);
    }

    public static <T> T execWithLockDialog(final LockCallback<?, T> pCallback, final Window pOwner) {
        return execWithLockDialog("", pCallback, null, pOwner, true);
    }

    public static <T> T execWithLockDialog(final String pText, final LockCallback<?, T> pCallback, final Window pOwner) {
        return execWithLockDialog(pText, pCallback, null, pOwner, true);
    }

    public static <T> T execWithLockDialog(final String pText, final LockCallback<?, T> pCallback) {
        final Window owner = null;
        return execWithLockDialog(pText, pCallback, null, owner, true);
    }

    public static <T> T execWithLockDialog(final String pText, final LockCallback<?, T> pCallback, final boolean pAlwaysShow) {
        final Window owner = null;
        return execWithLockDialog(pText, pCallback, null, owner, pAlwaysShow);
    }

    public static <T> T execWithLockDialog(final LockCallback<?, T> pCallback, final Callback<?, T> pAbortCallback) {
        final Window owner = null;
        return execWithLockDialog("", pCallback, pAbortCallback, owner, true);
    }

    public static <T> T execWithLockDialog(final LockCallback<?, T> pCallback, final Callback<?, T> pAbortCallback, final Window pOwner) {
        return execWithLockDialog("", pCallback, pAbortCallback, pOwner, true);
    }

    public static <T> T execWithLockDialog(final String pText, final LockCallback<?, T> pCallback, final Callback<?, T> pAbortCallback, final Window pOwner) {
        return execWithLockDialog(pText, pCallback, pAbortCallback, pOwner, true);
    }

    public static <T> T execWithLockDialog(final String pText, final LockCallback<?, T> pCallback, final Callback<?, T> pAbortCallback) {
        final Window owner = null;
        return execWithLockDialog(pText, pCallback, pAbortCallback, owner, true);
    }

    public static <T> T execWithLockDialog(final String pText, final LockCallback<?, T> pCallback, final Callback<?, T> pAbortCallback, final boolean pAlwaysShow) {
        final Window owner = null;
        return execWithLockDialog(pText, pCallback, pAbortCallback, owner, pAlwaysShow);
    }
    public static <T> T execWithLockDialog(final String pText, final LockCallback<?, T> pCallback, final Callback<?, T> pAbortCallback, final Window pOwner, final boolean pAlwaysShow) {
        if (pCallback == null) {
            throw new IllegalArgumentException("no callback defined!");
        }
        while (true) {
            try {
                return pCallback.call(null);
            } catch (LockException ex) {
                ObjectProperty<ButtonType> btnProp = new SimpleObjectProperty<>();
                if (Platform.isFxApplicationThread()) {
                    //Fantastic, we are already in FX Thread
                    ButtonType btn = showLockMessage(ex, pText, pOwner, pAlwaysShow);
                    btnProp.set(btn);
                } else {
                    //For batchgrouping: explicitly run code in FX Thread
                    BooleanProperty wait = new SimpleBooleanProperty(true);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            ButtonType btn = showLockMessage(ex, pText, pAlwaysShow);
                            btnProp.set(btn);
                            wait.set(false);
                        }
                    });

                    while (wait.get()) {
                        try {
                            Thread.sleep(50L);
                        } catch (InterruptedException ex1) {
                            LOG.get().log(Level.SEVERE, "Lock dialog was interrupted", ex1);
                            Thread.currentThread().interrupt();
                        }
                    }
                }

                if (btnProp.get() == ButtonType.CANCEL) {
                    if (pAbortCallback != null) {
                        pAbortCallback.call(null);
                    }
                    break;
                }
            }
        }
        return null;
    }

    /**
     * creates lock message dialog
     *
     * @param ex lock exception
     * @param pText message
     * @param pAlwaysShow show without any condition or only show dialog if lock
     * are acquired by other clients? This could be useful because on our
     * running client we can close case tabs
     * @return displayed lock message?
     */
    public static ButtonType showLockMessage(final LockException ex, final String pText, final boolean pAlwaysShow) {
        return showLockMessage(ex, pText, null, pAlwaysShow);
    }
    
    public static ButtonType showAuthorizationMessage(final CpxAuthorizationException ex, final String pText, final boolean pAlwaysShow) {
        return showAuthorizationMessage(ex, pText, null, pAlwaysShow);
    }

    public static ButtonType showAuthorizationMessage(final CpxAuthorizationException ex, final String pText, final Window pOwner, final boolean pAlwaysShow) {
        boolean result = true;

        if (result) {
            AuthorizationDialog dlg = AuthorizationDialog.createAuthorizationDialog(pText);
            Optional<ButtonType> res = dlg.showAndWait();
            if (res.isPresent()) {
                return res.get();
            }
            //AWi:is this code block ever reached??
            //the first ress variable should always have value that is returned?
            final FutureTask<ButtonType> task = new FutureTask<>(new Callable<ButtonType>() {
                @Override
                public ButtonType call() throws Exception {
                    final AuthorizationDialog dlg = AuthorizationDialog.createAuthorizationDialog(pText);
                    Optional<ButtonType> res = dlg.showAndWait();
                    if (res.isPresent()) {
                        return res.get();
                    }
                    return null;
                }
            });

            Platform.runLater(task);
            try {
                return task.get();
            } catch (ExecutionException ex1) {
                LOG.get().log(Level.SEVERE, "Authorization dialog was interrupted", ex1);
            } catch (InterruptedException ex1) {
                LOG.get().log(Level.SEVERE, "Authorization dialog was interrupted", ex1);
                Thread.currentThread().interrupt();
            }
        }
        return null;
    }

    /**
     * creates lock message dialog
     *
     * @param ex lock exception
     * @param pText message
     * @param pOwner window owner
     * @param pAlwaysShow show without any condition or only show dialog if lock
     * are acquired by other clients? This could be useful because on our
     * running client we can close case tabs
     * @return displayed lock message?
     */
    public static ButtonType showLockMessage(final LockException ex, final String pText, final Window pOwner, final boolean pAlwaysShow) {
        boolean result = true;
        //AWi: cool if clause but its useless .. following code will be executed regardless of the check!
        if (!pAlwaysShow && ex != null && ex.getLockDtosByOtherClients(Session.instance().getClientId()).length == 0) {
            //show only lock message if there are locks acquired by other clients, because on our running client we can close case tabs
        }
        if (result) {
            LockDialog dlg = LockDialog.createLockDialog(pText, "", pOwner == null ? stage : pOwner, ex);
            Optional<ButtonType> res = dlg.showAndWait();
            if (res.isPresent()) {
                return res.get();
            }

//            ObjectProperty<LockDialog> dlg = new SimpleObjectProperty<>(null);
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    dlg.set(LockDialog.createLockDialog(pText, "", stage, ex));
//                }
//            });
//
//            while (dlg.get() == null) {
//                try {
//                    Thread.sleep(50L);
//                } catch (InterruptedException ex1) {
//                    LOG.log(Level.SEVERE, "Lock dialog was interrupted", ex1);
//                }
//            }
//
//            Optional<ButtonType> res = dlg.get().showAndWait();
//            if (res.isPresent()) {
//                return res.get();
//            }
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    final LockDialog dlg = LockDialog.createLockDialog(pText, "", stage, ex);
//                    Optional<ButtonType> res = dlg.showAndWait();
//                    if (res.isPresent()) {
//                        //return res.get();
//                    }
//                }
//            });
////            return null;
//            if (pCallback != null) {
//                dlg.getButtonTypes().add(1, ButtonType.CANCEL);
//            }
            final FutureTask<ButtonType> task = new FutureTask<>(new Callable<ButtonType>() {
                @Override
                public ButtonType call() throws Exception {
                    final LockDialog dlg = LockDialog.createLockDialog(pText, "", stage, ex);
                    Optional<ButtonType> res = dlg.showAndWait();
                    if (res.isPresent()) {
                        return res.get();
                    }
                    return null;
                }
            });

            Platform.runLater(task);
            try {
                return task.get();
            } catch (ExecutionException ex1) {
                LOG.get().log(Level.SEVERE, "Lock dialog was interrupted", ex1);
            } catch (InterruptedException ex1) {
                LOG.get().log(Level.SEVERE, "Lock dialog was interrupted", ex1);
                Thread.currentThread().interrupt();
            }
        }

        return null;
    }
    /**
     * creates error dialog shown on top
     *
     * @param ex exception
     * @param pText text to show
     * @param pOwner window owner
     */
    public static void showErrorMessageDialog(final Exception ex, String pText, Window pOwner) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                AlertDialog.createErrorDialog(pText, pOwner == null ? stage : pOwner, ex, ButtonType.OK).showAndWait();
            }
        });
    }

    /**
     * creates error dialog shown on top
     *
     * @param pText text to show
     * @param pOwner window owner
     */
    public static void showErrorMessageDialog(String pText, Window pOwner) {
        showErrorMessageDialog((Exception) null, pText, pOwner);
    }

    /**
     * creates error dialog shown on top
     *
     * @param pText text to show
     * @param pDetails details text to show
     * @param pOwner window owner
     */
    public static void showErrorMessageDialog(String pText, String pDetails, Window pOwner) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                AlertDialog.createErrorDialog(pText, pDetails, pOwner == null ? stage : pOwner, null, ButtonType.OK).showAndWait();
            }
        });
    }

    /**
     * creates error dialog shown on top
     *
     * @param pText text to show
     */
    public static void showErrorMessageDialog(String pText) {
        showErrorMessageDialog(pText, null);
    }

    /**
     * shows an info dialog with the given text
     *
     * @param pText text of the info to display
     */
    public static void showInfoMessageDialog(String pText) {
        AlertDialog.createInformationDialog(pText, stage, ButtonType.OK).showAndWait();
    }

    /**
     * shows an info dialog with the given text
     *
     * @param pText text of the info to display
     * @param pOwner window owner
     */
    public static void showInfoMessageDialog(String pText, final Window pOwner) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                AlertDialog.createInformationDialog(pText, pOwner == null ? stage : pOwner, ButtonType.OK).showAndWait();
            }
        });

    }

    /**
     *
     * @param pTitel text of the header to display
     * @param pNode node
     */
    public static void showInfoMessageDialog(String pTitel, Node pNode) {
        AlertDialog dialog = AlertDialog.createInformationDialog("", stage, ButtonType.OK);
        dialog.setHeaderText(pTitel);
        dialog.getDialogPane().setContent(pNode);
        dialog.showAndWait();
    }

    /**
     *
     * @param pTitel text of the header to display
     * @param pText text of the info to display
     * @param pOwner window owner
     */
    public static void showInfoMessageDialog(String pTitel, String pText, final Window pOwner) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                AlertDialog dilaog = AlertDialog.createInformationDialog(pText, pOwner == null ? stage : pOwner, ButtonType.OK);
                dilaog.setHeaderText(pTitel);
                dilaog.showAndWait();
            }
        });

    }

    /**
     * shows an warning dialog with the given text
     *
     * @param pText text of the info to display
     */
    public static void showWarningMessageDialog(String pText) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                AlertDialog.createWarningDialog(pText, stage, ButtonType.OK).showAndWait();
            }
        });
    }

    /**
     * shows an warning dialog with the given text
     *
     * @param pOwner owner window to show content into
     * @param pText text of the info to display
     */
    public static void showWarningMessageDialog(String pText, Window pOwner) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                AlertDialog.createWarningDialog(pText, pOwner == null ? stage : pOwner, ButtonType.OK).showAndWait();
            }
        });
    }

    /*
  public static long getRevisionNumber(String localPath) throws SVNException {
  //        final SVNStatus status = SVNClientManager.newInstance().getStatusClient().doStatus(new File("C:\\D\\CPX\\trunk"), false);
  //       final SVNInfo status = SVNClientManager.newInstance().getWCClient().doInfo(new File(localPath), SVNRevision.WORKING);
  //        return status != null ? status.getRevision().getNumber() : -1;
  
  DAVRepositoryFactory.setup();
  SVNRepositoryFactoryImpl.setup();
  SVNClientManager clientManager = SVNClientManager.newInstance();
  SVNWCClient wcClient = clientManager.getWCClient();
  File baseFile = new File(localPath);
  //        System.out.println(baseFile.getAbsolutePath());
  SVNInfo svninfo = null;
  try {
  //cl.doGetFileContents(wcFile, SVNRevision.UNDEFINED, SVNRevision.WORKING, false, System.out);
  svninfo = wcClient.doInfo(baseFile, SVNRevision.WORKING);
  } catch (SVNException e) {
  e.printStackTrace();
  }
  long workingRevision = svninfo.getRevision().getNumber();
  SVNURL version = svninfo.getURL();
  
  
  // this Impl. can give the latest revision number from the SVN Repository.
  DAVRepositoryFactory.setup();
  String url = "https://lbsvn-02.l-b.local/svn/CheckpointX_Repository";
  String name = "nandola";
  String password = "Fourth789";
  SVNRepository repository = null;
  repository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(url));
  ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
  repository.setAuthenticationManager(authManager);
  long headRevision = repository.getLatestRevision();
  SVNDirEntry entry = repository.info(".", -1);
  //        System.out.println("Latest Rev: " + entry.getRevision());
  
  return workingRevision;
  }
     */
    /**
     * start the application and get the stage
     *
     * @param pStage stage to show content into
     * @throws Exception thrown when start screen is not found
     */
    @Override
    public synchronized void start(Stage pStage) throws Exception {
        INSTANCE = this;
        stage = pStage;
//        stage.initStyle(StageStyle.UTILITY);
        stage.setMaximized(true);
        stage.setScene(new Scene(new AnchorPane(), 300, 300, Color.WHITE));
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    stop();
                } catch (NamingException ex) {
                    LOG.get().log(Level.SEVERE, null, ex);
                }
            }
        });
        setStageTitleBinding(STAGE_TITLE_PROPERTY);
//        stage.setIconified(true);
//        stage.set
        setStageIcon("/img/cpxLogo4_.png");
        try {
            LoginScene login = new LoginScene();
            setScene(login);
            stage.show();
        } catch (IOException ex) {
            LOG.get().log(Level.SEVERE, "Maybe FXML is missing or corrupted", ex);
            MainApp.showErrorMessageDialog(ex);
        } catch (IllegalAccessError ex) {
            LOG.get().log(Level.SEVERE, "Maybe you have to adjust your exports/opens", ex);
            MainApp.showErrorMessageDialog(ex);
        } catch (Throwable ex) {
            LOG.get().log(Level.SEVERE, "Unknown error occured in start method", ex);
            MainApp.showErrorMessageDialog(ex);
        }
    }

    /**
     * stops application, disconnect local db, logout in server is neccessary
     * and clla closeApplication
     *
     * @throws javax.naming.NamingException wildfly naming error
     */
    @Override
    public void stop() throws NamingException {
        LOG.get().log(Level.INFO, "Checkpoint X will shutdown now...");
        LOG.get().log(Level.INFO, "Closing current Scene...");
        ((CpxScene) stage.getScene()).close();
        LOG.get().log(Level.INFO, "Closing all local SQLite database connections...");
        CpxDbManager.instance().stopAll();
        LOG.get().log(Level.INFO, "User logged out successfully: " + ((Session.instance().getClientId() == null) ? "No Login" : Session.instance().getEjbConnector().doCpxLogout()));
//    try {
//      //saveProperties();
//      super.stop();
//    } catch (Exception ex) {
//      Logger.getLogger(BasicMainApp.class.getName()).log(Level.SEVERE, null, ex);
//    }
        closeApplication();
    }

    /**
     * Opens an url (can be used to open mail client with mailto: protocol or to
     * open a file)
     *
     * uses awt.Desktop class TODO: -use not awt class to open default browser
     *
     * @param pUrl open Web url in default browser
     */
    public static void openUrl(String pUrl) {
        if (BasicMainApp.instance() == null) {
            if (LOG.get() != null) {
                LOG.get().severe("App is not initialized!");
            } else {
                System.err.println("App not initialized!");
            }
            return;
        }
        HostServices hostServices = BasicMainApp.instance().getHostServices();
        hostServices.showDocument(pUrl);
        //try {
        //    URI u = new URI(pUrl);
        //    java.awt.Desktop.getDesktop().browse(u);
        //} catch (URISyntaxException | IOException ex) {
        //    LOG.log(Level.SEVERE, null, ex);
        //}
    }

    /**
     * Brings your CPX application to the top of all applications on the stack!
     * For dialogs it is highly recommended to pass BasicMainApp.getStage() as
     * the owner!
     *
     * @param pStage stage that should jump into user's eyes
     */
    public static void bringToFront(final Stage pStage) {
        if (pStage == null) {
            return;
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                pStage.setMaximized(true);
                pStage.setAlwaysOnTop(true);
                pStage.setAlwaysOnTop(false);
                pStage.toFront();
                pStage.requestFocus();
            }
        });
    }

    /**
     * Brings your CPX application to the top of all applications on the stack!
     * For dialogs it is highly recommended to pass BasicMainApp.getStage() as
     * the owner!
     */
    public static void bringToFront() {
        bringToFront(stage);
    }

    /**
     * Reference to the current application
     *
     * @return BasicMainApp instance
     */
    public static BasicMainApp instance() {
        return INSTANCE;
    }

}
