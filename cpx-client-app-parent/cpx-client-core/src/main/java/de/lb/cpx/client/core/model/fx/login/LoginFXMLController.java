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

import de.lb.cpx.app.crypter.PasswordDecrypter;
import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.connection.database.CpxDbManager;
import de.lb.cpx.client.core.connection.database.CpxLanguage;
import de.lb.cpx.client.core.connection.jms.BasicStatusBroadcastHandler;
import de.lb.cpx.client.core.menu.fx.ToolbarMenuFXMLController;
import de.lb.cpx.client.core.menu.fx.dialog.LicenseInfoDialog;
import de.lb.cpx.client.core.menu.fx.dialog.SupportInfoDialog;
import de.lb.cpx.client.core.menu.fx.dialog.SystemInfoDialog;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.alert.ConfirmDialog;
import de.lb.cpx.client.core.model.fx.labeled.LabeledPasswortField;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.client.core.model.task.CpxTask;
import de.lb.cpx.client.core.model.task.LoginTask;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.config.ExtendedXMLConfiguration;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.cpx.license.crypter.License;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.exceptions.CpxLoginException;
import de.lb.cpx.service.ejb.AuthServiceEJBRemote;
import de.lb.cpx.service.ejb.ResourceBundleEJBRemote;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.version.Version;
import de.lb.cpx.shared.version.VersionHistory;
import de.lb.cpx.system.properties.CpxCustomSystemProperties;
import de.lb.cpx.system.properties.CpxCustomSystemPropertiesInterface;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javax.naming.NamingException;
import javax.persistence.PersistenceException;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

/**
 * FXML Controller class
 *
 * @author Dirk Niemeier
 */
public class LoginFXMLController extends Controller<LoginScene> {

    private static final Logger LOG = Logger.getLogger(LoginFXMLController.class.getName());
    private static Class<? extends BasicStatusBroadcastHandler<? extends Serializable>> STATUS_BROADCAST_HANDLER_CLASS;

    @FXML
    private LabeledPasswortField password;
    @FXML
    private Text message;
    @FXML
    private Button loginBtn;
    @FXML
    private Label version;
    @FXML
    private Label build;
    @FXML
    private DatabaseCombobox databases;

    private EjbProxy<AuthServiceEJBRemote> authServiceBean;
//    private EjbProxy<LoginServiceEJBRemote> loginServiceBean;
    private final CpxCustomSystemPropertiesInterface cpxCustomProps = CpxCustomSystemProperties.getInstance();
    private final CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();

    @FXML
    private ImageView cpxLogo;
    //PNa: 25.04.19 cpx-1506 (Login dialog: Remove option for creating a database)
    // remove truncate (ALT + click on db name) and drop (SHIFT + click on db name) feature as well.
//    @FXML
//    private Button createDatabaseBtn;
    @FXML
    private Button webAppBtn;

//    private boolean truncateDatabaseKeyPressed = false;
//    private boolean dropDatabaseKeyPressed = false;
    protected static BasicStatusBroadcastHandler<? extends Serializable> statusBroadcastHandler;

    @FXML
    private LabeledTextField userName;
    @FXML
    private AnchorPane apRoot;
    @FXML
    private VBox vBoxImageWrapper;
    @FXML
    private Button glyphSupportInfo;
    @FXML
    private Button glyphSystemInfo;
    @FXML
    private Button glyphHelp;
    @FXML
    private Button glyphLicenseInfo;

    static {
        Locale.setDefault(Locale.GERMAN);
    }

    private StringProperty loginMessageProperty = new SimpleStringProperty("");
    private StringProperty otherRoleProperty = new SimpleStringProperty("");
    
    public static synchronized CpxLanguage loadLanguage(final String pLocale) throws IOException, NamingException {
        EjbProxy<ResourceBundleEJBRemote> resourceBundleBean = Session.instance().getEjbConnector().connectResourceBundleBean();
        resourceBundleBean.getWithEx(); //provokes exception
        String resourceBundle = resourceBundleBean.get().getResourceBundle(pLocale);
        String customResourceBundle = resourceBundleBean.get().getResourceBundle("custom"); //Custom extensions
        CpxLanguage cpxLanguage = CpxLanguage.instance();
        Lang.setCpxLanguage(cpxLanguage);
        cpxLanguage.setLanguageFile(pLocale, resourceBundle + "\r\n" + customResourceBundle);
        return cpxLanguage;
    }

    public static synchronized void setLanguage(final String pLocale) {
        try {
            loadLanguage(pLocale);
//            Locale.setDefault(Locale.GERMANY);
        } catch (IOException | NamingException ex) {
            LOG.log(Level.SEVERE, "Was not able to load language '" + pLocale + "'", ex);
            BasicMainApp.showErrorMessageDialog(ex, "Cannot load resource bundle for locale '" + pLocale + "'");
        }
    }

    public static synchronized void setStatusBroadcastHandlerClass(final Class<? extends BasicStatusBroadcastHandler<? extends Serializable>> pClass) {
        if (pClass == null) {
            throw new IllegalArgumentException("Status Broadcast Handler Class cannot be null!");
        }
        STATUS_BROADCAST_HANDLER_CLASS = pClass;
    }

    /**
     * Check if the Java Version (JRE) is fulfilled
     *
     * @return does the CPX Client run the with the right Java Version?
     */
    private boolean checkJavaVersion() {
        LOG.log(Level.FINE, "Check used Java Version (JRE)...");
        if (!cpxProps.isRequiredJavaVersion()) {
            String msg = "Java Version (JRE) is too old";
            LOG.log(Level.WARNING, msg);
            BasicMainApp.getStage().setOnShown(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            BasicMainApp.showWarningMessageDialog(
                                    "You're running CPX Client with an outdated Java Version (" + cpxProps.getJavaVersion() + ").\n"
                                    + "Please update to at least Java " + CpxSystemPropertiesInterface.REQUIRED_JAVA_VERSION + ".\n"
                                    + "Otherwise there can be a serious lack of performance or unexpected errors can occur!"
                            );
                            //Maybe too less heap space should be also checked here?!
                        }
                    });
                }
            });
            return false;
        }
        LOG.log(Level.FINE, "Java Version is currently enough (found " + cpxProps.getJavaVersion() + ")");
        return true;
    }

    /**
     * Check if the CPX Client config file exists
     *
     * @return does the CPX Client config file exist?
     */
    private boolean checkConfigFileExists() {
        LOG.log(Level.FINE, "Check if CPX Client config file exists...");
        File xmlConfigFile = CpxClientConfig.instance().getXmlConfigFile();
        if (xmlConfigFile.exists() && xmlConfigFile.isFile()) {
            //Everything is fine! :-)
        } else {
            String error = "CheckpointX cannot find its client configuration file '" + xmlConfigFile.getAbsolutePath() + "'";
            LOG.log(Level.SEVERE, error);

            AlertDialog alertDialog = AlertDialog.createConfirmationDialog("Konfigurationsdatei nicht gefunden");
            alertDialog.setHeaderText("Konfigurationsdatei nicht gefunden");
            alertDialog.initOwner(BasicMainApp.getWindow());
            VBox box = new VBox();
            box.getChildren().add(new Label("Die Konfigurationsdatei '" + xmlConfigFile.getAbsolutePath() + "' konnte nicht gefunden werden."));
            box.getChildren().add(new Label("Möchten Sie die Datei jetzt anlegen?"));
            alertDialog.getDialogPane().setContent(box);

            final Window window = alertDialog.getDialogPane().getScene().getWindow();
            BasicMainApp.centerWindow(window);

            Optional<ButtonType> result = alertDialog.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                NewClientConfigDialog configDialog = new NewClientConfigDialog();
                BasicMainApp.centerWindow(configDialog.getDialogPane().getScene().getWindow());
                configDialog.getResultsProperty().addListener(new ChangeListener<NewClientConfig>() {
                    @Override
                    public void changed(ObservableValue<? extends NewClientConfig> observable, NewClientConfig oldValue, NewClientConfig newValue) {
                        if (newValue == null) {
                            return;
                        }

                        configDialog.setResult(null);
                        final File parentFile = xmlConfigFile.getParentFile();
                        if (!parentFile.exists()) {
                            if (!parentFile.mkdirs()) {
                                LOG.log(Level.SEVERE, "Cannot create directory for new configuration file: " + parentFile.getAbsolutePath());
                                AlertDialog dlg = AlertDialog.createErrorDialog("Das Verzeichnis für die Konfigurationsdatei in '" + parentFile.getAbsolutePath() + "' konnte nicht angelegt werden.");
                                BasicMainApp.centerWindow(dlg.getDialogPane().getScene().getWindow());
                                dlg.showAndWait();
                                return;
                            }
                        }
                        try ( BufferedWriter writer = new BufferedWriter(new FileWriter(xmlConfigFile))) {
                            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                            writer.write("<configuration>\n");
                            writer.write("  <server>\n");
                            writer.write("    <host>" + newValue.host + "</host>\n");
                            writer.write("    <port>" + String.valueOf(newValue.port) + "</port>\n");
                            writer.write("    <user>" + newValue.user + "</user>\n");
                            writer.write("    <password>" + newValue.password + "</password>\n");
                            writer.write("  </server>\n");
                            writer.write("</configuration>\n");
                            AlertDialog dlg = AlertDialog.createInformationDialog("Die Konfigurationsdatei '" + xmlConfigFile.getAbsolutePath() + "' wurde angelegt!");
                            BasicMainApp.centerWindow(dlg.getDialogPane().getScene().getWindow());
                            dlg.showAndWait();
                        } catch (IOException ex) {
                            LOG.log(Level.SEVERE, "Cannot write new configuration file: " + xmlConfigFile.getAbsolutePath(), ex);
                            AlertDialog dlg = AlertDialog.createErrorDialog("Die Konfigurationsdatei '" + xmlConfigFile.getAbsolutePath() + "' konnte nicht angelegt werden.", ex);
                            BasicMainApp.centerWindow(dlg.getDialogPane().getScene().getWindow());
                            dlg.showAndWait();
                            return;
                        }
                        configDialog.resultProperty().set(ButtonType.OK);
                        //return checkConfigFileExists();
                    }
                });
                Optional<ButtonType> configResult = configDialog.showAndWait();
                if (configResult.isPresent() && configResult.get().equals(ButtonType.OK)) {
                    return checkConfigFileExists();
                }
            }
            //MainApp.showErrorMessageDialog(error);
            return false;
        }

        LOG.log(Level.FINE,
                "done checking CPX Client config file '" + xmlConfigFile.getAbsolutePath() + "' exists");

        return true;
    }

    /**
     * Check if the CPX Client config files are valid
     *
     * @return are the CPX Client config files valid?
     */
    private boolean checkConfigFile() {
        LOG.log(Level.FINE, "Check if CPX Client config files are valid...");
        try {
            ExtendedXMLConfiguration config;
            config = CpxClientConfig.instance().getXmlConfig();
            config = CpxClientConfig.instance().getXmlUserConfig();
        } catch (IllegalStateException ex) {
            String error = "CheckpointX cannot load its configuration files";
            LOG.log(Level.SEVERE, error, ex);
//                BasicMainApp.showErrorMessageDialog(ex, error);
            BasicMainApp.showErrorMessageDialog(ex, Lang.getLoginErrorMessage() + " \r\n"
                    + "1. " + Lang.getLoginErrorMessageConfigurationInvalid() + " \r\n"
                    + "2. " + Lang.getLoginErrorMessageConfiguration() + " \r\n"
                    + "3. " + Lang.getLoginErrorMessageServer() + "\r\n");
            return false;
        }
        LOG.log(Level.FINE, "done checking CPX Client config files");
        return true;
    }

    /**
     * Check if a connection to local SQLite database can be established
     *
     * @return is a connection to local SQLite database available?
     */
    private boolean checkSqliteDb() {
        LOG.log(Level.FINE, "Check if local SQLite database is available...");
        try {
            CpxDbManager cacheManager = CpxDbManager.instance();
            cacheManager.testLanguageDb();
            cacheManager.testCatalogDb();
        } catch (SQLException ex) {
            final String msg = "Cannot connect local SQLite database";
            LOG.log(Level.SEVERE, msg, ex);
            BasicMainApp.showErrorMessageDialog(ex, msg);
            return false;
        }
        LOG.log(Level.FINE, "done getting local db");
        return true;
    }

    /**
     * Check if a connection to CPX Server can be established
     *
     * @return is a connection to CPX Server available?
     */
    private boolean checkCpxServerConnection() {
        LOG.log(Level.FINE, "Check if connection to CPX Server is available...");
        try {
            Session.instance().getEjbConnector().initContexts(
                    CpxClientConfig.instance().getServerHost(),
                    CpxClientConfig.instance().getServerPort(),
                    CpxClientConfig.instance().getServerUser(),
                    CpxClientConfig.instance().getServerPassword());
            authServiceBean = Session.instance().getEjbConnector().connectAuthServiceBean();
            authServiceBean.getWithEx(); //provokes exception

            Session.instance().setCpxLocale(CpxClientConfig.instance().getLanguage()); //set common server language first
            setLanguage(Session.instance().getCpxLocale());

        } catch (NamingException ex) {
            String pConnectionParams
                    = "host: " + CpxClientConfig.instance().getServerHost() + ", "
                    + "port: " + CpxClientConfig.instance().getServerPort() + ", "
                    + "user: " + CpxClientConfig.instance().getServerUser() + ", "
                    + "password: " + CpxClientConfig.instance().getServerPassword();
            LOG.log(Level.SEVERE, "pConnectionParams:" + pConnectionParams);
            BasicMainApp.showErrorMessageDialog(ex,
                    "Checkpoint X seems to be unable to connect its server. Error happened when I tried to connect configuration service. Please check these possible reasons: \r\n"
                    + "1. Wildfly application server wasn't started \r\n"
                    + "2. CPX server application wasn't deployed \r\n"
                    + "3. CPX server is not connectable with these settings: " + pConnectionParams + " (check Wildfly settings on server or cpx_client_config.xml on client please!) \r\n"
                    + "4. Check server log file for malformed code (perhaps there are internal server errors when connecting AuthServiceBean or something similar) \r\n");
            return false;
        }
        LOG.log(Level.FINE, "Connection to CPX Server is available!");
        return true;
    }

    /**
     * Check if all the requirements are fulfilled to run the CPX Client in a
     * correctly way
     *
     * @return are the requirements fulfilled?
     */
    private boolean checkPrerequisites() {
        LOG.log(Level.INFO, "check prerequisites...");
        Session.destroy();

        //Pay attention: Missing Java Version is just a warning, thus it has to be true here!
        final boolean resultJavaVersion = checkJavaVersion() || true;
        final boolean resultSqlite = checkSqliteDb();
        final boolean resultConfigExists = checkConfigFileExists();
        boolean resultConfigValid = false;
        boolean resultServerConnection = false;
        if (resultConfigExists) {
            resultConfigValid = checkConfigFile();
            if (resultConfigValid) {
                resultServerConnection = checkCpxServerConnection();
            }
        }

        final boolean initBroadcastHandler = initBroadcastHandler();
        LOG.log(Level.INFO, "Broadcast Handler initialization: " + initBroadcastHandler);

        final boolean result = (resultJavaVersion && resultSqlite && resultConfigExists && resultConfigValid && resultServerConnection);
        LOG.log(Level.FINE, "done checking prerequisites, result is " + result);
        return result;
    }

    /**
     * Initializes the controller class.
     *
     * @param url URL
     * @param rb ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOG.log(Level.INFO, "init login controller");
//        cpxLogo.setImage(getScene().getLogo());
//        Locale.setDefault(new Locale(AbstractCpxCatalog.DEFAULT_COUNTRY));
        //Translations
        databases.setTitle(Lang.getLoginDatabase());
        userName.setTitle(Lang.getLoginUser());
        password.setTitle(Lang.getLoginPassword());
        loginBtn.setText(Lang.getLoginLogin());

        if (!MainApp.getNeedsDatabase()) {
            //don't show database combobox
            HBox hbox = (HBox) databases.getParent();
            VBox vbox = (VBox) hbox.getParent();
            vbox.getChildren().remove(hbox);
        }

        final String fontFile = cpxProps.getCpxClientFontFile();
        try {
            registerFontFile(fontFile);
        } catch (IllegalArgumentException | FileNotFoundException | ExceptionInInitializerError ex) {
            LOG.log(Level.SEVERE, "Was not able to register font file: " + fontFile, ex);
            BasicMainApp.showErrorMessageDialog(ex, "Was not able to register font file: " + fontFile);
        }
//        createDatabaseBtn.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.PLUS));
        webAppBtn.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.PLUS));

        if (!checkPrerequisites()) {
            LOG.log(Level.SEVERE, "Missing prerequisites, so I'll stop initializing the CPX Client!");
            //close clients
            closeLogin();
            return;
        }

        License license = Session.instance().getLicense();
        if (license == null) {
            LOG.log(Level.WARNING, "No license file found!");
        } else {
            LOG.log(Level.INFO, "License found: {0}", String.valueOf(license));
            //Set<String> moduleList = license.getModuleList();
        }

        //clear notice/error field immediately when username or password changes
        userName.getControl().textProperty().addListener(e -> clearMessage());
        password.getControl().textProperty().addListener(e -> clearMessage());

        String lDatabase = cpxCustomProps.getCpxDatabase();
        if (lDatabase.isEmpty()) {
            lDatabase = CpxClientConfig.instance().getLastSessionDatabase();
        }

        try {
            databases.loadDatabases(lDatabase);
        } catch (NamingException ex) {
            LOG.log(Level.SEVERE, "Was not able to retrieve a list of CPX databases", ex);
        }

        glyphHelp.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.BOOK));
        glyphSupportInfo.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.HEADPHONES));
        glyphSystemInfo.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.COG));
        glyphLicenseInfo.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.CERTIFICATE));
        loginMessageProperty.addListener(new ChangeListener<String>(){
 
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue == null || newValue.isEmpty()){
                    return;
                }
                doLoginWithAnotherRole(newValue);
            }

        });

//        databases.setOnTruncate(new Callback<String, Void>() {
//            @Override
//            public Void call(String param) {
//                truncateDatabase(param);
//                return null;
//            }
//        });
//        databases.setOnDrop(new Callback<String, Void>() {
//            @Override
//            public Void call(String param) {
//                dropDatabase(param);
//                return null;
//            }
//        });
    }

//    public void truncateDatabase(final String pDatabase) {
//        final String database = (pDatabase == null) ? "" : pDatabase.trim();
//        truncateDatabaseKeyPressed = false;
//        if (database.isEmpty()) {
//            return;
//        }
////        Date lastDate = getLastDate(database);
////        Long days = null;
////        if (lastDate != null) {
////            days = ChronoUnit.DAYS.between(lastDate.toInstant(), (new Date()).toInstant());
////        }
//
//        //Confirmation if database shall be truncated...
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Confirmation");
//        alert.setHeaderText("Truncate database " + database);
//        alert.setContentText("Do you really want to truncate database " + database + "?\nAll data will be lost!");
////                + "\n\n" + (lastDate==null?"":"Last creation or modification date is " + Lang.toIsoDateTime(lastDate) + " (" + days + " days ago)"));
//
//        Optional<ButtonType> result = alert.showAndWait();
//        if (result.isPresent() && result.get() == ButtonType.OK) {
//            //OK, so delete now!
//            CpxTask<Boolean> cpxTask = new CpxTask<Boolean>(LoginFXMLController.this) {
//                @Override
//                protected Boolean call() throws Exception {
//                    Long userId = null;
//                    boolean success = authServiceBean.get().createDatabase(database, userId, true);
//                    return success;
//                }
//            };
//            cpxTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
//                @Override
//                public void handle(WorkerStateEvent event) {
//                    Throwable ex = cpxTask.getException();
//                    if (ex instanceof CpxIllegalArgumentException) {
//                        BasicMainApp.showErrorMessageDialog(ex.getMessage());
//                    } else {
//                        BasicMainApp.showErrorMessageDialog(ex, "Truncation of database '" + database + "' failed so hard!");
//                    }
//                }
//            });
//            cpxTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
//                @Override
//                public void handle(WorkerStateEvent event) {
//                    try {
//                        databases.loadDatabases(database);
//                    } catch (NamingException ex) {
//                        LOG.log(Level.SEVERE, "Was not able to load list of CPX databases", ex);
//                    }
//                    BasicMainApp.showInfoMessageDialog("Database '" + database + "' was successfully truncated!");
//                }
//            });
//            toggleControls(cpxTask);
//            new Thread(cpxTask).start();
//
//        } else {
//            //Cancel
//        }
//    }
    public Date getLastDate(final String pDatabase) {
        try {
            return authServiceBean.get().getLastDate(pDatabase);
        } catch (CpxIllegalArgumentException ex) {
            LOG.log(Level.SEVERE, "Was not able to get last date of database: " + pDatabase);
            BasicMainApp.showErrorMessageDialog(ex, "Das letzte Änderungs-/Erstellungsdatum der Datenbank '" + pDatabase + "' konnte nicht ermittelt werden!");
        }
        return null;
    }

//    public void dropDatabase(final String pDatabase) {
//        final String database = (pDatabase == null) ? "" : pDatabase.trim();
//        dropDatabaseKeyPressed = false;
//        if (database.isEmpty()) {
//            return;
//        }
////        Date lastDate = getLastDate(database);
////        Long days = null;
////        if (lastDate != null) {
////            days = ChronoUnit.DAYS.between(lastDate.toInstant(), (new Date()).toInstant());
////        }
//
//        //Confirmation if database shall be dropped...
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Confirmation");
//        alert.setHeaderText("Drop database " + database);
//        alert.setContentText("Do you really want to delete database " + database + "?\nAll data will be lost!");
////                + "\n\n" + (lastDate==null?"":"Last creation or modification date is " + Lang.toIsoDateTime(lastDate) + " (" + days + " days ago)"));
//
//        Optional<ButtonType> result = alert.showAndWait();
//        if (result.isPresent() && result.get() == ButtonType.OK) {
//            //OK, so delete now!
//            CpxTask<Boolean> cpxTask = new CpxTask<Boolean>(LoginFXMLController.this) {
//                @Override
//                protected Boolean call() throws Exception {
//                    boolean success = authServiceBean.get().dropDatabase(database);
//                    return success;
//                }
//            };
//            cpxTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
//                @Override
//                public void handle(WorkerStateEvent event) {
//                    Throwable ex = cpxTask.getException();
//                    if (ex instanceof CpxIllegalArgumentException) {
//                        BasicMainApp.showErrorMessageDialog(ex.getMessage());
//                    } else {
//                        BasicMainApp.showErrorMessageDialog(ex, "Deletion of database '" + database + "' failed so hard!");
//                    }
//                }
//            });
//            cpxTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
//                @Override
//                public void handle(WorkerStateEvent event) {
//                    try {
//                        databases.loadDatabases(null);
//                    } catch (NamingException ex) {
//                        LOG.log(Level.SEVERE, "Was not able to load list of CPX databases", ex);
//                    }
//                    BasicMainApp.showInfoMessageDialog("Database '" + database + "' was successfully dropped!");
//                }
//            });
//            toggleControls(cpxTask);
//            new Thread(cpxTask).start();
//
//        } else {
//            //Cancel
//        }
//    }
    public void closeLogin() {
        if (statusBroadcastHandler != null) {
            statusBroadcastHandler.close();
        }
        BasicMainApp.closeApplication();
    }

//    private void onUsernameAction(ActionEvent event) {
//        onLoginAction(event);
//    }
//
//    private void onPasswordAction(ActionEvent event) {
//        onLoginAction(event);
//    }
    public String getSelectedDatabase() {
        if (!MainApp.getType().getNeedsDatabase()) {
            return "";
        }
        if (databases.getControl().getSelectionModel().getSelectedItem() == null) {
            return "";
        }
        return databases.getControl().getSelectionModel().getSelectedItem().getConnectionString();
    }
    
    private void doLoginWithAnotherRole(String pMessage){
         AlertDialog dialog = ConfirmDialog.createConfirmationDialog(pMessage);
         dialog.showAndWait().ifPresent(new Consumer<ButtonType>() {
            @Override
            public void accept(ButtonType t) {
               if (ButtonType.OK.equals(t)) {
                    if(otherRoleProperty.get() != null && !otherRoleProperty.get().isEmpty()){
                        loginMessageProperty.set("");
                        otherRoleProperty.set("");
                         LoginTask cpxTask = createLoginTask(true);
                        cpxTask.start();
                    }
                }
            }
         });
    }

    @FXML
    private void onLoginAction(ActionEvent event) {
//        String database = getSelectedDatabase();
//        String user = userName.getControl().getText();
//        String pw = password.getControl().getText();

//        LoginTask cpxTask = new LoginTask(user, pw, database);
//        cpxTask.setController(this);
//        cpxTask.setOnSucceeded(e -> {
//            MainApp.setStageTitle(null); //show selected database in title
////            StartupLoaderFXMLController.setIgnoredCatalogType(CatalogTypeEn.DOCTOR, CatalogTypeEn.DRG);
////            StartupLoaderFXMLController.setAllowedCatalogType(CatalogTypeEn.DOCTOR);
////            StartupLoaderFXMLController.getAllowedCatalogTypes();
////            StartupLoaderFXMLController.getIgnoredCatalogTypes();
////            StartupLoaderFXMLController.setIgnoreAllCatalogTypes();
////            StartupLoaderFXMLController.setAllowedCatalogType(CatalogTypeEn.DOCTOR);
//            initBroadcastHandler();
//            try {
//                if (cpxCustomProps.getCpxSkipUpdate() || StartupLoaderFXMLController.getAllowedCatalogTypes().isEmpty()) {
//                    StartupLoaderFXMLController.startApp();
//                } else {
//                    BasicMainApp.setScene(new StartupLoaderScene());
//                }
//            } catch (CpxIllegalArgumentException | IOException ex) {
//                LOG.log(Level.SEVERE, null, ex);
//            }
//        });
//        cpxTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
//            @Override
//            public void handle(WorkerStateEvent event) {
//                LOG.log(Level.SEVERE, "User login failed: " + cpxTask.getException().getMessage());
////                 RSH 24.04.2018  CPX-923
//                Throwable cause = cpxTask.getException().getCause();
//                String text = cpxTask.getException().getMessage();
////                Throwable c = cause.getCause();
////                while(c != null) {
////                    text += "\n" + c.getMessage();
////                    c = c.getCause();
////                }
//                if (cause instanceof PersistenceException) {
//                    Throwable c = cause.getCause();
//                    while (c != null) {
//                        text += "\n" + c.getMessage();
//                        c = c.getCause();
//                    }
//                }
//                if(text.endsWith(Lang.getLoginFailedDatabaseNoRightOtherRole1())){
//                    otherRoleProperty.set(((CpxLoginException)cpxTask.getException()).getCode());
//                   loginMessageProperty.set(text);
//                   message.setText("Wait for decision");
//                }else{
//                    BasicMainApp.showErrorMessageDialog(cause, text);
//                
//                    message.setText(cause != null ? text : " User login failed");
//                    userName.getControl().requestFocus();
//                }
//            }
//        });
//        cpxTask.setOnRunning(new EventHandler<WorkerStateEvent>() {
//            @Override
//            public void handle(WorkerStateEvent event) {
//                message.setText("Bitte warten...");
//            }
//        });
//        toggleControls(cpxTask);
        LoginTask cpxTask = createLoginTask(false);
        cpxTask.start();
//        new Thread(cpxTask).start();
    }
    
    

    public void toggleControls(final CpxTask<?> pCpxTask) {
        message.setText("");
        loginBtn.disableProperty().bind(pCpxTask.runningProperty());
        userName.disableProperty().bind(pCpxTask.runningProperty());
        password.disableProperty().bind(pCpxTask.runningProperty());
        databases.disableProperty().bind(pCpxTask.runningProperty());
//        createDatabaseBtn.disableProperty().bind(pCpxTask.runningProperty());
        webAppBtn.disableProperty().bind(pCpxTask.runningProperty());
    }

//    private void onExitAction(ActionEvent event) {
//        closeLogin();
//    }
    private void clearMessage() {
        message.setText("");
    }

    @Override
    public void afterShow() {
        final Version serverVersion = authServiceBean.get().getVersion();
        final Version clientVersion = VersionHistory.getRecentVersion();
        final boolean versionMatch = serverVersion.equals(clientVersion);

        version.setStyle("-fx-text-fill:white");
        version.setText(clientVersion.getVersion() + (clientVersion.getCodeName().isEmpty() ? "" : " (" + clientVersion.getCodeName() + ")") + " vom " + clientVersion.getDateGerman());
        if (!versionMatch) {
            version.setStyle("-fx-text-fill:red;-fx-font-weight:bold;");
            Tooltip.install(version, new Tooltip("Server Version: " + serverVersion.getVersion()));
        }

        build.setStyle("-fx-text-fill:white");
        build.setText(MainApp.instance().getVersion());
        cpxLogo.setImage(getScene().getLogo());
        //Refresh scene title with license information
        getScene().setSceneTitle(null);

        //String currentDir = System.getProperty("user.dir");
        /*
        try {
            long revisionNumber = BasicMainApp.getRevisionNumber(currentDir);
//             version.setText(String.valueOf("Revision: " + revisionNumber));
//             version.setText(String.valueOf("Revision: "));
        } catch (SVNException ex) {
            Logger.getLogger(LoginFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
         */
        initBgImage();
        /* automatic logon, when client application was started with non-empty parameter "username" */
        String lUsername = cpxCustomProps.getCpxUsername();
        String lPassword = cpxCustomProps.getCpxPassword();
        if (!lPassword.isEmpty()) {
            password.setPassword(PasswordDecrypter.getInstance().decrypt2(lPassword));
        }
        if (!lUsername.isEmpty()) {
            userName.setText(lUsername);
        }

        if (!lUsername.isEmpty() && !lPassword.isEmpty() && !cpxCustomProps.getCpxDatabase().isEmpty() && !getSelectedDatabase().isEmpty()) {
            onLoginAction(null);
        }

        userName.requestFocus();
    }

//  @Override
//  public String getTitle() {
//    return "Checkpoint X Login";
//  }
//    public boolean logout() {
//        if (loginServiceBean == null) {
//            return true;
//        }
//        return loginServiceBean.doLogout();
//    }
//    private void createDatabase(ActionEvent event) {
//        TextInputDialog inputDlg = new TextInputDialog("");
//        inputDlg.setTitle("Create new database");
//        inputDlg.setContentText("Name of new database");
//        inputDlg.setHeaderText("Please specify the name of the \nnew database (e.g. dbsys1:CPX_NEW)");
//        Optional<String> newName = inputDlg.showAndWait();
//        if (newName.isPresent()) {
//            String name = newName.get();
//            if (name == null || name.trim().isEmpty()) {
//                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setTitle("Information");
//                alert.setHeaderText("Name is empty");
//                alert.setContentText("A database needs an unique name!");
//                alert.showAndWait();
//            } else {
//
//                CpxTask<Boolean> cpxTask = new CpxTask<Boolean>(this) {
//                    @Override
//                    protected Boolean call() throws Exception {
//                        Long userId = null;
//                        boolean success = authServiceBean.get().createDatabase(name, userId);
//                        return success;
//                    }
//                };
//                cpxTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
//                    @Override
//                    public void handle(WorkerStateEvent event) {
//                        Throwable ex = cpxTask.getException();
//                        if (ex instanceof CpxIllegalArgumentException) {
//                            BasicMainApp.showErrorMessageDialog(ex.getMessage());
//                        } else {
//                            BasicMainApp.showErrorMessageDialog(ex, "Creation of database '" + name + "' failed so hard!");
//                        }
//                    }
//                });
//                cpxTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
//                    @Override
//                    public void handle(WorkerStateEvent event) {
//                        try {
//                            databases.loadDatabases(name);
//                        } catch (NamingException ex) {
//                            LOG.log(Level.SEVERE, "Was not able to load list of CPX databases", ex);
//                        }
//                        BasicMainApp.showInfoMessageDialog("Database '" + name + "' was successfully created!");
//                    }
//                });
//                toggleControls(cpxTask);
//                new Thread(cpxTask).start();
//            }
//        }
//    }
    @FXML
    private void openWebApp(ActionEvent event) throws NamingException {
        final String url = Session.instance().getEjbConnector()
                .connectAuthServiceBean().get().getWebAppUrl();
        BasicMainApp.openUrl(url);
    }

    private void initBgImage() {
        //Awi-20170821
        //moved declaration of the bg image to source code - previously bg image was declared in css-style class
        //there was an issue, when not the wohle image was shown due to preserving ratios
//        vBoxImageWrapper.getStyleClass().add("menu-bg");

        //bind min ratios to the root element to prevent image view from resizing on larger images
        vBoxImageWrapper.minWidthProperty().bind(apRoot.widthProperty());
        vBoxImageWrapper.minHeightProperty().bind(apRoot.heightProperty());
        //declare iamge to use and bind properties to wrapper size
        ImageView imgView = getScene().getBackgroundView();//new ImageView("/img/menu_background.jpg");
        if (imgView != null) {
            imgView.fitHeightProperty().bind(vBoxImageWrapper.heightProperty());
            imgView.fitWidthProperty().bind(vBoxImageWrapper.widthProperty());
            vBoxImageWrapper.getChildren().add(imgView);
        }
    }

    private void registerFontFile(final String pFontfile) throws FileNotFoundException {
        if (pFontfile == null || pFontfile.trim().isEmpty()) {
            throw new IllegalArgumentException("No path passed to font file");
        }
        File fontFile = new File(pFontfile.trim());
        LOG.log(Level.FINER, "Load font file from " + pFontfile);
        if (!fontFile.exists()) {
            throw new IllegalArgumentException("Font file does not exist: " + fontFile.getAbsolutePath());
        }
        if (!fontFile.isFile()) {
            throw new IllegalArgumentException("This is not a file: " + fontFile.getAbsolutePath());
        }
        if (!fontFile.canRead()) {
            throw new IllegalArgumentException("File is not readable: " + fontFile.getAbsolutePath());
        }
        Font.loadFont(new FileInputStream(fontFile), fontFile.length());
        GlyphFont font = new FontAwesome(new FileInputStream(fontFile)); //path to font.ttf
        GlyphFontRegistry.register(font);
        LOG.log(Level.FINE, "Font file was registered successfully");
    }

    public synchronized static boolean initBroadcastHandler() {
        if (STATUS_BROADCAST_HANDLER_CLASS == null) {
            return false;
        }
        if (statusBroadcastHandler != null) {
            statusBroadcastHandler.close();
        }
        try {
            statusBroadcastHandler = STATUS_BROADCAST_HANDLER_CLASS.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOG.log(Level.SEVERE, "Cannot initiate status broadcast handler", ex);
            return false;
        }
        return true;
    }

    public synchronized static BasicStatusBroadcastHandler<? extends Serializable> getBroadcastHandler() {
        return statusBroadcastHandler;
    }

    public synchronized static boolean isBroadcastHandlerHandlerInitialized() {
        return getBroadcastHandler() != null;
    }

    @FXML
    private void showSupportInfo(ActionEvent event) {
        SupportInfoDialog.showDialog();
    }

    @FXML
    private void showSystemInfo(ActionEvent event) {
        SystemInfoDialog.showDialog();
    }

    @FXML
    private void showHelp(ActionEvent event) {
        ToolbarMenuFXMLController.showHelp();
    }

    @FXML
    private void showLicenseInfo(ActionEvent event) {
        LicenseInfoDialog.showDialog();
    }

    @Override
    public boolean shortcutF1Help(KeyEvent pEvent) {
        showHelp(null);
        return true;
    }

    private LoginTask createLoginTask(boolean pChangeRole){
       String database = getSelectedDatabase();
        String user = userName.getControl().getText();
        String pw = password.getControl().getText();
        LoginTask cpxTask = new LoginTask(user, pw, database, pChangeRole);
        cpxTask.setController(this);
        cpxTask.setOnSucceeded(e -> {
            MainApp.setStageTitle(null); //show selected database in title
//            StartupLoaderFXMLController.setIgnoredCatalogType(CatalogTypeEn.DOCTOR, CatalogTypeEn.DRG);
//            StartupLoaderFXMLController.setAllowedCatalogType(CatalogTypeEn.DOCTOR);
//            StartupLoaderFXMLController.getAllowedCatalogTypes();
//            StartupLoaderFXMLController.getIgnoredCatalogTypes();
//            StartupLoaderFXMLController.setIgnoreAllCatalogTypes();
//            StartupLoaderFXMLController.setAllowedCatalogType(CatalogTypeEn.DOCTOR);
            initBroadcastHandler();
            try {
                if (cpxCustomProps.getCpxSkipUpdate() || StartupLoaderFXMLController.getAllowedCatalogTypes().isEmpty()) {
                    StartupLoaderFXMLController.startApp();
                } else {
                    BasicMainApp.setScene(new StartupLoaderScene());
                }
            } catch (CpxIllegalArgumentException | IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        });
        cpxTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                LOG.log(Level.SEVERE, "User login failed: " + cpxTask.getException().getMessage());
//                 RSH 24.04.2018  CPX-923
                Throwable cause = cpxTask.getException().getCause();
                String text = cpxTask.getException().getMessage();
//                Throwable c = cause.getCause();
//                while(c != null) {
//                    text += "\n" + c.getMessage();
//                    c = c.getCause();
//                }
                if (cause instanceof PersistenceException) {
                    Throwable c = cause.getCause();
                    while (c != null) {
                        text += "\n" + c.getMessage();
                        c = c.getCause();
                    }
                }
                if(text.endsWith(Lang.getLoginFailedDatabaseNoRightOtherRole1())){
                    otherRoleProperty.set(((CpxLoginException)cpxTask.getException()).getCode());
                   loginMessageProperty.set(text);
                   message.setText("Wait for decision");
                }else{
                    BasicMainApp.showErrorMessageDialog(cause, text);
                
                    message.setText(cause != null ? text : " User login failed");
                    userName.getControl().requestFocus();
                }
            }
        });
        cpxTask.setOnRunning(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                message.setText("Bitte warten...");
            }
        });
        toggleControls(cpxTask);
        return cpxTask;
    }

}
