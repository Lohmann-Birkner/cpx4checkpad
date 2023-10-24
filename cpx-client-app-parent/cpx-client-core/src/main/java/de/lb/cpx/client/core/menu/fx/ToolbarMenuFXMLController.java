/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.menu.fx;

import de.FileUtils;
import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.fx.dialog.ChangeActualRoleDialog;
import de.lb.cpx.client.core.menu.fx.dialog.ChangeDatabaseDialog;
import de.lb.cpx.client.core.menu.fx.dialog.ChangeUserDialog;
import de.lb.cpx.client.core.menu.fx.dialog.LicenseInfoDialog;
import de.lb.cpx.client.core.menu.fx.dialog.SupportInfoDialog;
import de.lb.cpx.client.core.menu.fx.dialog.SystemInfoDialog;
import de.lb.cpx.client.core.menu.model.ToolbarMenuItem;
import de.lb.cpx.client.core.menu.model.ToolbarMultiMenuItem;
import de.lb.cpx.client.core.menu.model.ToolbarSingleMenuItem;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import de.lb.cpx.client.core.model.fx.tooltip.BasicTooltip;
import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.client.core.model.image.CropImageDialog;
import de.lb.cpx.client.core.model.image.CropImageFXMLController;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.server.commonDB.model.CdbUsers;
import de.lb.cpx.service.ejb.AuthServiceEJBRemote;
import de.lb.cpx.service.ejb.LoginServiceEJBRemote;
import de.lb.cpx.service.ejb.ReadonlyDocumentEJBRemote;
import de.lb.cpx.service.information.DatabaseInfo;
import de.lb.cpx.shared.enums.ReadonlyDocumentsEn;
import de.lb.cpx.shared.lang.Lang;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.HostServices;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.util.Callback;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.ArrayUtils;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * FXML Controller class
 *
 * TODO: remove switch for old workflow and working list remove code for old
 * workflow and working list
 *
 * @author wilde
 */
public class ToolbarMenuFXMLController extends Controller<BasicToolbarMenuScene> {

    private static final Logger LOG = Logger.getLogger(ToolbarMenuFXMLController.class.getName());

    @FXML
    private VBox vBoxMenuPane;
    @FXML
    private ToggleButton tbMenu;
    @FXML
    private AnchorPane apContent;
    @FXML
    private Label labelDatabase;
    @FXML
    private Button buttonServerMenu;
    @FXML
    private Button buttonInfoMenu;
    @FXML
    private Label labelProfileUsername;
    @FXML
    private AnchorPane apProfile;
    @FXML
    private VBox vBoxProfile;
    @FXML
    private ImageView profileImageView;
    @FXML
    private Button buttonProfileImage;
    @FXML
    private VBox vBoxToolbar;
    @FXML
    private VBox vBoxImgSpace;
    @FXML
    private Button btnTbInfo;
    @FXML
    private Button btnTbDbInfo;
    private ServerMenuPopover serverMenu;
    private InfoMenuPopover infoMenu;
    private EjbProxy<AuthServiceEJBRemote> authServiceEjb;

    private Toolbar toolbar;
    private final BooleanProperty disableMenuProperty = new SimpleBooleanProperty(false);

    @FXML
    private VBox vBoxReducedMenu;
    @FXML
    private VBox vBoxExtendedMenu;

    public boolean isDisableMenu() {
        return disableMenuProperty().get();
    }

    public void setDisableMenu(Boolean pMenu) {
        disableMenuProperty().set(pMenu);
    }

    public BooleanProperty disableMenuProperty() {
        return disableMenuProperty;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpLabels();
        setUpProfile();
        authServiceEjb = Session.instance().getEjbConnector().connectAuthServiceBean();
        loadProfileImageFromDb();

        apContent.sceneProperty().addListener(new ChangeListener<Scene>() {
            @Override
            public void changed(ObservableValue<? extends Scene> observable, Scene oldValue, Scene newValue) {
                tbMenu.isVisible();
            }
        });

        tbMenu.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                tbMenu.setGraphic(newValue ? ResourceLoader.getGlyph("\uf04a") : ResourceLoader.getGlyph("\uf0c9"));
            }
        });

        tbMenu.setGraphic(ResourceLoader.getGlyph("\uf0c9"));
        setContentPane(apContent);

        buttonServerMenu.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.DATABASE));
        toolbar = new Toolbar();
        tbMenu.selectedProperty().bindBidirectional(toolbar.extendedProperty());
    }

    /**
     * load profile image stored in database loads default iamge if nothing is
     * stored TODO: Default Image is female, should at least be a universal
     * avatar or depending of the gender of the current user
     */
    public void loadProfileImageFromDb() {
        try {
            Byte[] image = authServiceEjb.get().getUserImage(Session.instance().getCpxUserId());
            if (image == null) {
                //Use dummy profile image?!
                Image imgDummy = ResourceLoader.getImage("/img/checkpoint_x_logo_480x480.png");
                profileImageView.setImage(imgDummy);
            } else {
                byte[] image2 = ArrayUtils.toPrimitive(image);
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(image2));
                Image img2 = SwingFXUtils.toFXImage(img, null);
                profileImageView.setImage(img2);
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    private void onSetting(ActionEvent pEvent) {

        /* Opens SettingsDialog
        *  Already implemented, commented because content is not set
         */
        if (disableMenuProperty.get()) {
            return;
        }
//        getScene().getSettingsDialog().showAndWait();
        getScene().showSettingsDialog();
    }

    //sets the labels with lang values and static user values, like user name and so on
    private void setUpLabels() {
        updateDatabaseName();
        buttonProfileImage.setText(Lang.getMenuUserChangeProfileImage()); //"Profilbild ändern"
        updateUserName();
        buttonInfoMenu.setText("");
        buttonInfoMenu.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.INFO));
    }

    /**
     * sets the Engine for the ImageView, with clickEvents and Open Dialog
     */
    private void setUpProfile() {

        buttonProfileImage.setVisible(false);

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        vBoxProfile.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                profileImageView.setFitWidth(new_val.doubleValue());

                profileImageView.setPreserveRatio(true);
            }
        });

        profileImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {

                timeline.getKeyFrames().add(
                        new KeyFrame(Duration.seconds(3),
                                new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {
                                timeline.stop();
                                buttonProfileImage.setVisible(false);
                            }
                        }
                        )
                );
                timeline.playFromStart();
                buttonProfileImage.setVisible(true);
            }
        });

        buttonProfileImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                timeline.stop();
                buttonProfileImage.setVisible(false);

                CropImageDialog crop_image_dialog = new CropImageDialog(
                        MainApp.getWindow(), Modality.APPLICATION_MODAL, "Profilbild bearbeiten");
                CropImageFXMLController controller = (CropImageFXMLController) crop_image_dialog.getScene().getController();
                crop_image_dialog.showAndWait().ifPresent(new Consumer<ButtonType>() {
                    @Override
                    public void accept(ButtonType t) {
                        if (t.getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) {
                            try {
                                authServiceEjb.get().setUserImage(Session.instance().getCpxUserId(), controller.getCroppedImageData());
                                loadProfileImageFromDb();
                            } catch (IOException ex) {
                                LOG.log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * forces the menu to hide
     */
    public void hideMenu() {
        toolbar.setExtended(Boolean.FALSE);
//        showMenuProperty.setValue(Boolean.FALSE);
    }

    /**
     * show the menu
     */
    public void showMenu() {
        toolbar.setExtended(Boolean.TRUE);
//        showMenuProperty.setValue(Boolean.TRUE);
    }

    /**
     * @return toolbar object
     */
    public Toolbar getToolbar() {
        return toolbar;
    }

    /**
     * sets the value of menu should show or not
     *
     * @param pShowMenu indicator if menu should be shown
     */
    public void setShowMenu(boolean pShowMenu) {
//        showMenuProperty.setValue(pShowMenu);
        toolbar.setExtended(pShowMenu);
    }

    /**
     * checks if the given points are in the bounds of the menu
     *
     * @param pX x value of the point
     * @param pY y value of the point
     * @return indicator if point is in the layout bounds of the menu
     */
    public boolean isInMenuBounds(double pX, double pY) {
        return vBoxMenuPane.getBoundsInLocal().contains(pX, pY);// && !tbMenu.getBoundsInLocal().contains(pX,pY);
    }

    @FXML
    private void onServerMenu(ActionEvent event) {
        showServerMenu(buttonServerMenu);
    }

    private void showServerMenu(Parent pParent) {
        if (serverMenu == null) {
            serverMenu = new ServerMenuPopover();
//        }else{
//            serverMenu.refresh();
        }
        if (!serverMenu.isShowing()) {
            serverMenu.show(pParent);
        } else {
            serverMenu.hide();
        }
    }

    @FXML
    private void onInfoMenu(ActionEvent event) {
        showInfoMenu(buttonInfoMenu);
    }

    private void showInfoMenu(Parent pParent) {
        if (infoMenu == null) {
            infoMenu = new InfoMenuPopover();
//        }else{
//            infoMenu.refresh();
        }
        if (!infoMenu.isShowing()) {
            infoMenu.show(pParent);
        } else {
            infoMenu.hide();
        }
    }

    public ToggleButton getMenuButton() {
        return tbMenu;
    }

    private void updateUserName() {
        labelProfileUsername.setText(Session.instance().getCpxUserName());
        labelProfileUsername.setTooltip(new Tooltip("Eingeloggt seit: " + Lang.toTime(Session.instance().getCreatedAt())));
    }

    private void updateDatabaseName() {
        labelDatabase.setText(Session.instance().getCpxDatabase());
        Tooltip.install(labelDatabase, new Tooltip("Aktuelle Datenbank"));
    }

    private class InfoMenuPopover extends AutoFitPopOver {

        InfoMenuPopover() {
            super();
            initMenu();
        }

        private void initMenu() {
            String roleName = Session.instance().getEjbConnector().connectLoginServiceBean().get().getActualRoleName();
            CdbUsers cdbUser = Session.instance().getCdbUser();

            VBox vBox = new VBox(headerLabel("Nutzer & Rolle"));
            vBox.setSpacing(10);
            
            GridPane gpInfo = new GridPane();
            gpInfo.setHgap(10);
            gpInfo.setVgap(10);
            gpInfo.addRow(gpInfo.getRowCount(), keyLabelCompWidth("Benutzername"),getUserWrapper());
            gpInfo.addRow(gpInfo.getRowCount(), keyInfoPair("Name", cdbUser.getUFullName()));
            gpInfo.addRow(gpInfo.getRowCount(), keyLabelCompWidth("Rolle"),getUserRoleWrapper(roleName));
            gpInfo.addRow(gpInfo.getRowCount(), keyInfoPair("Eingeloggt seit",
                    new StringBuilder(Lang.toTime(Session.instance().getCreatedAt()))
                            .append(" Uhr")
                            .toString()));
            
            vBox.getChildren().add(gpInfo);
            vBox.setPadding(new Insets(10, 10, 10, 10));
            setHideOnEscape(true);
            setDetachable(false);
            setAutoHide(true);
            setContentNode(vBox);
            setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
        }

        private Pane getUserWrapper() {
            Button btn = new Button(Lang.getMenuChange());
            btn.disableProperty().bind(disableMenuProperty);
            btn.setTooltip(new CpxTooltip(Lang.getMenuUserChange(), 100, 5000, 50, true));
            btn.setOnAction(new ChangeUserEvent());
            HBox wrapper = new HBox(infoLabel(Session.instance().getCpxUserName(), null), btn);
            wrapper.setSpacing(5);
            wrapper.setAlignment(Pos.CENTER_LEFT);
            return wrapper;
        }
        
        private Pane getUserRoleWrapper(String roleName) {
            Button btn = new Button(Lang.getMenuChange());
            btn.disableProperty().bind(disableMenuProperty);
            btn.setTooltip(new CpxTooltip(Lang.getMenuRoleChange(), 100, 5000, 50, true));
            btn.setOnAction(new ChangeRoleEvent());
            HBox wrapper = new HBox(infoLabel(roleName, null), btn);
            wrapper.setSpacing(5);
            wrapper.setAlignment(Pos.CENTER_LEFT);
            return wrapper;
        }

        private void refresh() {
            initMenu();
        }
    }

    /**
     * tries to find a good editor application for you (because everything is
     * better than default Windows Notepad, believe me!)
     *
     * @return file to editor programm (can be null if no program was found)
     */
    public static File findTxtEditorExe() {
        final Map<String, String> editors = new LinkedHashMap<>();
        editors.put("Notepad++", "C:\\Program Files\\Notepad++\\notepad++.exe");
        editors.put("UltraEdit (64 Bit)", "C:\\Program Files\\IDM Computer Solutions\\UltraEdit\\Uedit64.exe");
        editors.put("UltraEdit (32 Bit)", "C:\\Program Files\\IDM Computer Solutions\\UltraEdit\\Uedit32.exe");
        editors.put("TextPad", "C:\\Program Files\\TextPad 7\\TextPad.exe");
        editors.put("PSPad", "C:\\Program Files (x86)\\PSPad editor\\PSPad.exe");
        editors.put("Visual Studio Code", "%LOCALAPPDATA%\\Programs\\Microsoft VS Code\\Code.exe");
        editors.put("Sublime Text 3", "C:\\Program Files\\Sublime Text 3\\sublime_text.exe");
        editors.put("Sublime Text 2", "C:\\Program Files\\Sublime Text 2\\sublime_text.exe");
        editors.put("Hidemaru", "C:\\Program Files (x86)\\Hidemaru\\Hidemaru.exe");
        editors.put("Sakura", "C:\\Program Files (x86)\\sakura\\sakura.exe");
        editors.put("EditPlus", "C:\\Program Files\\EditPlus 3\\editplus.exe");
        editors.put("EmEditor", "C:\\Program Files\\EmEditor\\EmEditor.exe");
        //editors.put("Adobe Dreamweaver", "C:\\Program Files\\Adobe\\Adobe Dreamweaver CC 2014.1\\Dreamweaver.exe");
        //editors.put("gVim", "C:\\Program Files\\Vim\\vim74\\gvim.exe");
        editors.put("Atom", "%LOCALAPPDATA%\\atom\\atom.exe");
        editors.put("Notepad", "C:\\Windows\\System32\\notepad.exe"); //last choice :/

        final Runtime rs = Runtime.getRuntime();
        final Iterator<Map.Entry<String, String>> it = editors.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            final String name = entry.getKey();
            final File file = new File(entry.getValue());
            LOG.log(Level.FINEST, "check " + name + " with default path " + file.getAbsolutePath());
            for (int i = 1; i <= 3; i++) {
                if (i == 1) {
                    if (file.exists()) {
                        return file;
                    }
                }
                if (i == 2) {
                    File file2 = new File(file.getAbsolutePath().replace("Program Files", "Program Files (x86)"));
                    if (file2.exists()) {
                        return file2;
                    }
                }
                if (i == 3) {
                    final String command = "where \"" + file.getName() + "\"";
                    try {
                        Process proc = rs.exec(command);
                        String line;
                        try (InputStream is = proc.getInputStream(); InputStreamReader isr = new InputStreamReader(is); BufferedReader in = new BufferedReader(isr)) {
                            while ((line = in.readLine()) != null) {
                                File file3 = new File(line);
                                if (file3.exists()) {
                                    return file3;
                                }
                            }
                        }
                    } catch (IOException ex) {
                        LOG.log(Level.FINEST, "Cannot find file: " + command, ex);
                    }
                }
            }
        }
        LOG.log(Level.WARNING, "no editor program was found!");
        return null;
    }

    public static boolean editFile(File pFile) {
        if (pFile == null) {
            return false;
        }
        LOG.log(Level.INFO, "will open file in editor " + pFile.getAbsolutePath());

//        Map<LogRecord, Exception> exceptions = new LinkedHashMap<>();
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().edit(pFile);
                return true;
            } catch (IOException ex) {
                LOG.log(Level.FINEST, "Cannot edit file with Desktop: " + pFile.getAbsolutePath(), ex);
            }
        } else {
            LOG.log(Level.FINEST, "cannot edit file with Desktop, because this feature is not supported: " + pFile.getAbsolutePath());
        }

        final File editorExe = findTxtEditorExe();
        if (editorExe != null && editorExe.exists()) {
            final Runtime rs = Runtime.getRuntime();
            final String command = "\"" + editorExe.getAbsolutePath() + "\" \"" + pFile.getAbsolutePath() + "\"";
            try {
                rs.exec(command);
                return true;
            } catch (IOException ex) {
                LOG.log(Level.FINEST, "Cannot edit file: " + command, ex);
            }
        }

        LOG.log(Level.SEVERE, "Cannot edit file, because no suitable editor program was found: " + pFile.getAbsolutePath());
        return false;
    }

    /**
     * Open Windows Explorer an select a single file or directory
     *
     * @param pFile file or directory
     * @return true if there's no exception
     */
    public static boolean openInExplorer(final File pFile) {
        if (pFile == null) {
            return false;
        }
        return openInExplorer(pFile.getAbsolutePath());
    }

    /**
     * Open Windows Explorer an select a single file or directory
     *
     * @param pPath path to file or directory
     * @return true if there's no exception
     */
    public static boolean openInExplorer(final String pPath) {
        final String path = pPath == null ? "" : pPath.trim();
        if (path.isEmpty()) {
            return false;
        }
        try {
            Runtime.getRuntime().exec(String.format("explorer.exe /select,\"%s\"", path));
            //Desktop.getDesktop().open(new File(BasicMainApp.LOGFILE_PATH));
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot select directory or file '" + path + "' in Windows Explorer", ex);
            return false;
        }
        return true;
    }

    private class ServerMenuPopover extends AutoFitPopOver {

        ServerMenuPopover() {
            super();
            initMenu();
        }

        private void initMenu() {
            VBox vBox = new VBox();
            vBox.setSpacing(10);
            final DatabaseInfo databaseInfoCommon = Session.instance().getCpxDatabaseInfoCommon(true);
            final DatabaseInfo databaseInfo = MainApp.getNeedsDatabase() ? Session.instance().getCpxDatabaseInfo(true) : null;
//            final String databaseVersionShort = databaseInfo.getVersionShort();

            //add menu for database info separately
            //to determine which items should be added, in some cases the client do not have a mandatory database connection to a case db
            //if thats the case, hide some menu items
            vBox.getChildren().addAll(getDatabaseInfo(databaseInfo, databaseInfoCommon));
            vBox.setPadding(new Insets(10, 10, 10, 10));
            setHideOnEscape(true);
            setDetachable(false);
            setAutoHide(true);
            setContentNode(vBox);
            //setDefaultArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
            setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);

            setOnShowing((e) -> {
                if(MainApp.getNeedsDatabase() && databaseInfo!= null){
                Session.instance().getPatientCount(); //force update if empty, discard return value
                Session.instance().getCaseCount(); //force update if empty, discard return value
                Session.instance().getProcessCount(); //force update if empty, discard return value
                }
            });

        }

        private HBox getDatabaseWrapper(final DatabaseInfo pDatabaseInfo, final boolean pIsCommonDb) {
            Button btn = null;
            if (!pIsCommonDb) {
                btn = new Button(Lang.getMenuChange());
                btn.disableProperty().bind(disableMenuProperty);
                btn.setOnAction(new ChangeDatabaseEvent());
            }
            final String identifier = pDatabaseInfo == null ? null : pDatabaseInfo.getIdentifier(); //physical server address
            final String connectionString = pDatabaseInfo == null ? Session.instance().getCpxDatabase() : pDatabaseInfo.getConnectionString().connectionString; //logical server address (dbsysX:...)
            HBox wrapper = new HBox(infoLabel(identifier, connectionString));
            if (btn != null) {
                wrapper.getChildren().add(btn);
            }
            wrapper.setSpacing(5d);
            wrapper.setAlignment(Pos.CENTER_LEFT);
            return wrapper;
        }
        private void refresh() {
            initMenu();
        }

        /**
         * @param databaseInfo db info object to store case database
         * informations
         * @param databaseInfoCommon db info object to store common database
         * informations
         * @return list of nodes to be added for the case and common database
         */
        private List<Node> getDatabaseInfo(DatabaseInfo databaseInfo, DatabaseInfo databaseInfoCommon) {
            List<Node> nodes = new ArrayList<>();
            nodes.add(headerLabel("Verbindung & Datenbank"));
            GridPane gpDbInfo = new GridPane();
            gpDbInfo.setHgap(10);
            gpDbInfo.setVgap(10);
            nodes.add(gpDbInfo);
            gpDbInfo.addRow(gpDbInfo.getRowCount(), keyInfoPair("Server",CpxClientConfig.instance().getServerSocket(),
                    "CPX Server, Client-ID " + Session.instance().getClientId() + ", User-ID " + Session.instance().getCpxUserId()));
            if (databaseInfoCommon == null) {
                return nodes;
            }
            //common db
            final String databaseVersionShortCommon = databaseInfoCommon.getVersionShort();
            gpDbInfo.addRow(gpDbInfo.getRowCount(),keyLabelCompWidth("Datenbank"),getDatabaseWrapper(databaseInfoCommon, true));
            gpDbInfo.addRow(gpDbInfo.getRowCount(),keyInfoPair("Version",(databaseVersionShortCommon.isEmpty() ? databaseInfoCommon.version : databaseVersionShortCommon) + " auf " + databaseInfoCommon.hostName, databaseVersionShortCommon.isEmpty() ? null : databaseInfoCommon.version));
            //flag by dni, to determine if the client needs a case database connection,
            //if not ignore some case database required menu items
            if (BasicMainApp.getNeedsDatabase() && databaseInfo != null) {
//                nodes.add(getDatabaseMenu());
                //case db
                final String databaseVersionShort = databaseInfo.getVersionShort();
                gpDbInfo.addRow(gpDbInfo.getRowCount(),keyLabelCompWidth("Datenbank"),getDatabaseWrapper(databaseInfo, false));
                gpDbInfo.addRow(gpDbInfo.getRowCount(),keyInfoPair("Version",(databaseVersionShort.isEmpty() ? databaseInfo.version : databaseVersionShort) + " auf " + databaseInfo.hostName, databaseVersionShort.isEmpty() ? null : databaseInfo.version));
                //                    countBox
                gpDbInfo.addRow(gpDbInfo.getRowCount(),keyLabelCompWidth("Anzahl"),getCountBox());
            }
            return nodes;
        }
        private HBox getCountBox() {
            
            HBox countBox = new HBox();
            countBox.setSpacing(5d);
            countBox.setAlignment(Pos.CENTER_LEFT);
            final Label countPatient = new Label();
            final Label countCase = new Label();
            final Label countProcess = new Label();
            final Button caseRefreshBtn = new Button("Aktualisieren");
            countBox.getChildren().addAll(/*new Label("Anzahl:"),*/
                    new Label("Patienten"), countPatient, new Label("-"),
                    new Label("Fälle"), countCase, new Label("-"),
                    new Label("Vorgänge"), countProcess,
                    caseRefreshBtn);

            countPatient.textProperty().bind(Session.instance().patientCount.asString());
            countCase.textProperty().bind(Session.instance().caseCount.asString());
            countProcess.textProperty().bind(Session.instance().processCount.asString());

            caseRefreshBtn.setOnAction((event) -> {
                caseRefreshBtn.setDisable(true);
//                countPatient.setText(Session.instance().getPatientCount(true) + "");
//                countCase.setText(Session.instance().getPatientCount(true) + "");
//                countProcess.setText(Session.instance().getProcessCount(true) + "");
                Session.instance().getPatientCount(true);
                Session.instance().getPatientCount(true);
                Session.instance().getProcessCount(true);
                Session.instance().resetCanceledCaseCount();
                Session.instance().resetCanceledProcessCount();
                //release button after a few seconds to prevent multiple clicks
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        caseRefreshBtn.setDisable(false);
                    }
                }, 2000);
            });
            return countBox;
        }

    }

    private class ChangeUserEvent implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            ChangeUserDialog dialog = new ChangeUserDialog(Lang.getMenuUserChange(), MainApp.getStage());
            dialog.setOnCloseRequest(new EventHandler<DialogEvent>() {
                @Override
                public void handle(DialogEvent event) {
                    if (!dialog.isSuccess()) {
                        return;
                    }
                    //detect and react to what is currently shown
                    //either workflow list or working list is shown
//                    handleOpenendDetailScreen();
                    if (serverMenu != null) {
                        serverMenu.refresh();
                    }
                    if (infoMenu != null) {
                        infoMenu.refresh();
                        updateDatabaseName();
                    }
                    //refresh user icon
                    loadProfileImageFromDb();
                    //refrest user name 
                    updateUserName();
                    //refresh filter
                }
            });
            dialog.showAndWait();
        }

    }

    private class ChangeRoleEvent implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            ChangeActualRoleDialog dialog = new ChangeActualRoleDialog(Lang.getMenuRoleChange(), MainApp.getStage());
            dialog.setRefreshToolbarCallback(new Callback<Void, Void>(){
                public Void call(Void vd){
                    if (serverMenu != null) {
                        serverMenu.refresh();
                        
                    }
                    if (infoMenu != null) {
                        infoMenu.refresh();
                    }
                    updateDatabaseName();
                    return null;
                }
            });
            dialog.roleChangedProperty().addListener(new ChangeListener<Boolean> (){
                 @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if(newValue){
                        infoMenu.refresh();
                    }
                }
            });

            dialog.showAndWait().ifPresent(new Consumer<ButtonType>() {
                @Override
                public void accept(ButtonType t) {
                    if(!ButtonType.OK.equals(t)){
                        return;
                    }
                    if (serverMenu != null) {
                        serverMenu.refresh();
                    }
//                    if (infoMenu != null) {
//                        infoMenu.refresh();
//                    }
                }
            });

        }

    }

    private class ChangeDatabaseEvent implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            ChangeDatabaseDialog dialog = new ChangeDatabaseDialog(Lang.getMenuDatabaseChange(), BasicMainApp.getStage());
            dialog.setOnCloseRequest(new EventHandler<DialogEvent>() {
                @Override
                public void handle(DialogEvent event) {
                    if (!dialog.isSuccess()) {
                        return;
                    }
                    //close open tabs
//                    closeAllOpenCases();
//                    closeAllOpenProcesses();
//                    closeJobs();
                    //detect and react to what is currently shown
                    //either workflow list or working list is shown
//                    handleOpenendDetailScreen();
                    if (serverMenu != null) {
                        serverMenu.refresh();
                        updateDatabaseName();
                    }
                    if (infoMenu != null) {
                        infoMenu.refresh();
                    }
                }

            });
            dialog.showAndWait();
        }

    }

    //TODO:Make menu control class?!
    public class Toolbar {

        /**
         * creates new instance with default entries! default is settings and
         * help!
         */
        Toolbar() {
            initialize();
//            vBoxMenuPane.visibleProperty().bind(extendedProperty());
            extendedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    extendToolbar(newValue);
                }
            });
            extendToolbar(CpxClientConfig.instance().getExtendToolbar());
        }

        private void extendToolbar(Boolean newValue) {
            CpxClientConfig.instance().setExtendToolbar(newValue);
            if (newValue) {
                vBoxMenuPane.setMaxWidth(VBox.USE_COMPUTED_SIZE);
                vBoxMenuPane.setVisible(true);
                vBoxToolbar.setMaxWidth(0);
                vBoxToolbar.setVisible(false);
            } else {
                vBoxMenuPane.setMaxWidth(0);
                vBoxMenuPane.setVisible(false);
                vBoxToolbar.setMaxWidth(30);
                vBoxToolbar.setVisible(true);
            }
            refresh();
        }

        /**
         * refresh layout of menu items
         */
        public void refresh() {
            for (ToolbarMenuItem itm : getItems()) {
                itm.refresh();
            }
        }
        //menu items to show in toolbar
        private List<ToolbarMenuItem> items = new ArrayList<>();

        /**
         * @return menu items list
         */
        public List<ToolbarMenuItem> getItems() {
            return new ArrayList<>(items);
        }

        /**
         * @param pItems list of menu items to show
         */
        public void setItems(List<ToolbarMenuItem> pItems) {
            items = pItems == null ? new ArrayList<>() : new ArrayList<>(pItems);
        }

        //extended property to handle both layouts
        private BooleanProperty extendedProperty;

        /**
         * @return property to indicate toolbar mode
         */
        public BooleanProperty extendedProperty() {
            if (extendedProperty == null) {
                extendedProperty = new SimpleBooleanProperty(false);
//                extendedProperty.bindBidirectional(showMenuProperty);
            }
            return extendedProperty;
        }

        //could malfunction because of binding
        /**
         * @param pExtended set mode for toolbar, if extended or not
         */
        public void setExtended(boolean pExtended) {
            extendedProperty().set(pExtended);
        }

        /**
         * indicator if toolbar should be considered extended
         *
         * @return is toolbar expanded?
         */
        public boolean isExtended() {
            return extendedProperty().get();
        }

        private void initialize() {
            //register default menu
            btnTbDbInfo.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.DATABASE));
            btnTbDbInfo.setTooltip(new BasicTooltip(Lang.getLoginDatabase(), 0, 5000, 200, true));
            btnTbDbInfo.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    showServerMenu(btnTbDbInfo);
                }
            });
            btnTbInfo.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.INFO));
            btnTbInfo.setTooltip(new CpxTooltip(Lang.getInformation(), 0, 5000, 200, true));
            btnTbInfo.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    showInfoMenu(btnTbInfo);
                }
            });

            ToolbarSingleMenuItem settings = new ToolbarSingleMenuItem(this);
            settings.setExtendedTitle(Lang.getMenuBatchGroupingSettings());
            settings.setGlyph(FontAwesome.Glyph.COG);
            settings.setTooltip(new BasicTooltip(Lang.getMenuBatchGroupingSettings(), 0, 5000, 200, true));
            settings.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (disableMenuProperty.get()) {
                        return;
                    }
                    onSetting(event);
                }
            });
            DocumentationMenuItem docu = new DocumentationMenuItem();
            docu.setExtendedTitle("Kataloge");
            docu.setGlyph(FontAwesome.Glyph.BOOK);
            docu.setTooltip(new BasicTooltip("Kataloge", 0, 5000, 200, true));
            
            HelpMenuItem help = new HelpMenuItem();
            
            help.setExtendedTitle("Hilfe & Infos");
            help.setGlyph(FontAwesome.Glyph.QUESTION_CIRCLE);
            help.setTooltip(new BasicTooltip("Hilfe & Infos", 0, 5000, 200, true));
            
            add(settings);
            add(docu);
            add(help);
        }

        /**
         * @param item item to add to menu
         */
        public void add(ToolbarMenuItem item) {
            vBoxReducedMenu.getChildren().add(item.getReducedNode());
            vBoxExtendedMenu.getChildren().add(item.getExtendedNode());
            items.add(item);
        }

        /**
         * @param pIndex index to add item
         * @param item item to add
         */
        public void add(int pIndex, ToolbarMenuItem item) {
            vBoxReducedMenu.getChildren().add(pIndex, item.getReducedNode());
            vBoxExtendedMenu.getChildren().add(pIndex, item.getExtendedNode());
            items.add(pIndex, item);
        }
    }

    @Override
    public boolean shortcutF1Help(KeyEvent pEvent) {
        onShowHelp(pEvent);
        return true;
    }
    
    public List<String> getCatalogYears(ReadonlyDocumentsEn pType ) {
        try{
            ReadonlyDocumentEJBRemote readonlyService = Session.instance().getEjbConnector().connectReadonlyDocumentBean().get();
            return readonlyService.getCatalogYears(pType);
        }catch(Exception ex){
            LOG.log(Level.SEVERE, "no aop years found", ex);
            return null;
        }
        }

    
    private class DocumentationMenuItem extends ToolbarMultiMenuItem{
        protected static final String DEFAULT_ALIGNMENT = "-fx-alignment: CENTER-LEFT;";
        private Map<Button, CatalogPopOver> popoverMap = new HashMap<>();
//        private List<String> years;
        
        public DocumentationMenuItem(){
             super(getToolbar());
             List<String> years = getCatalogYears(ReadonlyDocumentsEn.AOP);
             if(years != null && !years.isEmpty()){
                Button btnAopCatalog = new Button(ReadonlyDocumentsEn.AOP.getMenuItem());
                btnAopCatalog.setStyle(DEFAULT_ALIGNMENT);
                btnAopCatalog.setMaxWidth(Double.MAX_VALUE);
                CatalogPopOver popOver = new CatalogPopOver(ReadonlyDocumentsEn.AOP, years);
                popoverMap.put(btnAopCatalog, popOver);
                btnAopCatalog.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        showCatalogPopOver(btnAopCatalog );
                    }
                });
                add(btnAopCatalog);
            }
            //dkr drg
            years = getCatalogYears(ReadonlyDocumentsEn.DKR_DRG);
            if(years != null && !years.isEmpty()){
                Button dkrDrg = new Button(ReadonlyDocumentsEn.DKR_DRG.getMenuItem());
                dkrDrg.setStyle(DEFAULT_ALIGNMENT);
                dkrDrg.setMaxWidth(Double.MAX_VALUE);
                CatalogPopOver popOver = new CatalogPopOver(ReadonlyDocumentsEn.DKR_DRG, years);
                popoverMap.put(dkrDrg, popOver);
                dkrDrg.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                         showCatalogPopOver(dkrDrg );
                    }
                });
                add(dkrDrg);
            }
            years = getCatalogYears(ReadonlyDocumentsEn.DKR_PEPP);
            if(years != null && !years.isEmpty()){
                Button dkrPepp = new Button(ReadonlyDocumentsEn.DKR_PEPP.getMenuItem());
                dkrPepp.setStyle(DEFAULT_ALIGNMENT);
                dkrPepp.setMaxWidth(Double.MAX_VALUE);
                CatalogPopOver popOver = new CatalogPopOver(ReadonlyDocumentsEn.DKR_PEPP, years);
                popoverMap.put(dkrPepp, popOver);
                dkrPepp.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                         showCatalogPopOver(dkrPepp );
                    }
                });
                add(dkrPepp);
            }
            Button md = new Button(ReadonlyDocumentsEn.MD_RECOMENDATION.getMenuItem());
            md.setStyle(DEFAULT_ALIGNMENT);
            md.setMaxWidth(Double.MAX_VALUE);
            md.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                     showReadonlyFile(ReadonlyDocumentsEn.MD_RECOMENDATION);
                }
            });
            add(md);
        }
        
        public void showCatalogPopOver(Button parent){
            CatalogPopOver popover = popoverMap.get(parent);
            if(popover == null){
                return;
            }
            if (!popover.isShowing()) {
                popover.show(parent);
            } else {
                popover.hide();
            }
           
        }
    
        private class CatalogPopOver extends  AutoFitPopOver {

        CatalogPopOver(ReadonlyDocumentsEn pType, List<String> years) {
            super();
            initMenu(pType, years);
        }

        private void initMenu(ReadonlyDocumentsEn pType, List<String> years) {
 
            VBox vBox = new VBox();

            if(years != null && years.size() > 0){
                for(String year: years){
                    Button bYear = new Button(year);
                    bYear.setStyle(DEFAULT_ALIGNMENT);
                    bYear.setMaxWidth(Double.MAX_VALUE);
                    bYear.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            showReadonlyFile(pType, year);
                        }
                    });

                    vBox.getChildren().add(bYear);

                }

            }

            setHideOnEscape(true);
            setDetachable(false);
            setAutoHide(true);
            setContentNode(vBox);
            setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
        }


    }
    }

    

    private class HelpMenuItem extends ToolbarMultiMenuItem {

        protected static final String DEFAULT_ALIGNMENT = "-fx-alignment: CENTER-LEFT;";

        public HelpMenuItem() {
            super(getToolbar());
            Button btnHelpPdf = new Button("Handbuch");
            btnHelpPdf.setStyle(DEFAULT_ALIGNMENT);
            btnHelpPdf.setMaxWidth(Double.MAX_VALUE);
            btnHelpPdf.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    onShowHelp(event);
                }
            });
            Button btnLicense = new Button("Lizenz");
            btnLicense.setStyle(DEFAULT_ALIGNMENT);
            btnLicense.setMaxWidth(Double.MAX_VALUE);
            btnLicense.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    onShowLicenseInfo();
                }
            });
            Button btnSystemInfo = new Button("System-Informationen");
            btnSystemInfo.setStyle(DEFAULT_ALIGNMENT);
            btnSystemInfo.setMaxWidth(Double.MAX_VALUE);
            btnSystemInfo.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    onShowSystemInfo();
                }
            });
            Button btnSupportInfo = new Button("Support");
            btnSupportInfo.setStyle(DEFAULT_ALIGNMENT);
            btnSupportInfo.setMaxWidth(Double.MAX_VALUE);
            btnSupportInfo.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    onShowSupportInfo();
                }
            });
            add(btnHelpPdf);
            add(btnLicense);
            add(btnSystemInfo);
            add(btnSupportInfo);
        }

        private void onShowSystemInfo() {
            SystemInfoDialog.showDialog();
        }

        private void onShowLicenseInfo() {
            LicenseInfoDialog.showDialog();
        }

        private void onShowSupportInfo() {
            SupportInfoDialog.showDialog();
        }
    }

    public static Label infoLabel(String string, String tooltipString) {
        Label label = new Label(string);
        if (tooltipString != null) {
            label.setTooltip(new Tooltip(tooltipString));
        }
        return label;
    }

    public static Label headerLabel(String string) {
        Label label = new Label(string);
        label.setStyle("-fx-font-weight: bold");
        return label;
    }

    public static Label keyLabel(String pText) {
        Label label = new Label(pText);
        label.getStyleClass().add("cpx-detail-label");
        label.setPrefWidth(150);
        return label;
    }
    public static Label keyLabelCompWidth(String pText) {
        Label label = new Label(pText);
        label.setStyle("-fx-text-fill:-black05");
        return label;
    }
    public static Label[] keyInfoPair(String pKey, String pInfo){
        return keyInfoPair(pKey, pInfo, null);
    }
    public static Label[] keyInfoPair(String pKey,String pInfo, String pInfoTooltip){
        return new Label[]{keyLabelCompWidth(pKey),infoLabel(pInfo, pInfoTooltip)};
    }
    
    public void onShowHelp(Event pEvent) {
        showHelp();
    }

    public static void showHelp() {
        //from Server: WD_CPX_Server\help
        showReadonlyFile(ReadonlyDocumentsEn.HELP);
    }   
    
    private static void showReadonlyFile(ReadonlyDocumentsEn pType){
        showReadonlyFile(pType, "");
    }
    private static void showReadonlyFile(ReadonlyDocumentsEn pType, String year){
        final File file;
        try {
            file = getFile(pType, year);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Unable to get help file from server", ex);
            MainApp.showErrorMessageDialog(ex, "Das Hilfedokument kann aufgrund eines Fehlers nicht angezeigt werden");
            return;
        }
        HostServices hostServices = MainApp.instance().getHostServices();
        hostServices.showDocument(file.toURI().toString());
    }
        
    
    


    public static File getFile(ReadonlyDocumentsEn pType) throws IOException {
        return getFile(pType, "");
    }
    
    public static File getFile(ReadonlyDocumentsEn pType, String pYear) throws IOException {
        final File file = new File(System.getProperty("java.io.tmpdir"), pType.getDocName(pYear));
        if (file.exists()) {
            if (FileUtils.isFileLock(file)) {
                LOG.log(Level.INFO, "help file already exists and is opened in application: " + file.getAbsolutePath());
                return file;
            }
            //maybe you want to check if file is recent enough to display it without downloading it from server again -> implement here
            if (file.length() > 0 && file.lastModified() >= System.currentTimeMillis() - (60 * 60 * 1000)) { //example lifetime: 1 hour
                return file;
            }
            FileUtils.deleteFile(file);
        }

        ReadonlyDocumentEJBRemote readonlyService = Session.instance().getEjbConnector().connectReadonlyDocumentBean().get();
        final byte[] content;
        LOG.log(Level.FINEST, "will load help file content from server...");
        content = readonlyService.getDocumentContent(pType, pYear);

        if (content == null || content.length == 0) {
            throw new IOException("content of help document is null or empty");
        }
        LOG.log(Level.FINEST, "will write content of {0} bytes to temporary file: {1}...", new Object[]{content.length, file.getAbsolutePath()});
        try (FileOutputStream out = new FileOutputStream(file)) {
            out.write(content);
            out.flush();
        } catch (IOException ex) {
            throw new IOException("Unable to create temporary help file: " + file.getAbsolutePath(), ex);
        }
        return file;
    }


}
