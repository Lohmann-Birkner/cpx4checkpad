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
 *    2017  adameck - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.settings;

import de.checkpoint.enums.AppTypeEn;
import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.InitLogger;
import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.menu.cache.MenuCacheEntry;
import de.lb.cpx.client.core.menu.fx.ToolbarMenuFXMLController;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.cell.SimpleCellValueFactory;
import de.lb.cpx.client.core.model.fx.alert.NotificationsFactory;
import de.lb.cpx.client.core.model.fx.labeled.LabeledIntegerTextField;
import de.lb.cpx.client.core.model.fx.login.StartupLoaderFXMLController;
import de.lb.cpx.client.core.model.fx.login.StartupLoaderScene;
import de.lb.cpx.client.core.model.task.CpxTask;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.cpx.license.crypter.License;
import de.lb.cpx.model.lang.CpxLanguageInterface;
import de.lb.cpx.service.ejb.AuthServiceEJBRemote;
import de.lb.cpx.service.information.CatalogTodoEn;
import de.lb.cpx.service.information.CatalogTypeEn;
import de.lb.cpx.service.information.CpxCatalogOverview;
import de.lb.cpx.shared.dto.CpxSession;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.menucache.MenuCacheEntryEn;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Collectors;
import static javafx.animation.Animation.INDEFINITE;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Duration;
import javax.naming.NamingException;
import org.controlsfx.control.Notifications;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 * FXML Controller class
 *
 * @author adameck
 */
public class SettingsFXMLController extends Controller<SettingsDialogScene> {

    private static Thread catalogUpdaterThread = null;

    @FXML
    private SplitPane spContent;
    @FXML
    private ListView<SettingsCategory> lvCategory;

    private final ObservableList<SettingsCategory> categoryList = FXCollections.observableArrayList();
    @FXML
    private Button btnCatalogUpdate;
    @FXML
    private ScrollPane updatePane;
    @FXML
    private VBox searchLists;
    @FXML
    private CheckBox checkBoxLocal;
    @FXML
    private CheckBox checkBoxAllReminders;
    @FXML
    private CheckBox checkBoxShowDetailsOverview;
    private boolean catalogUpdateNotificationShown = false;
    @FXML
    private ScrollPane webAppPane;
    @FXML
    private Button btnOpenWebApp;
    @FXML
    private Button btnOpenManagementIf;
    @FXML
    private ScrollPane searchListsPane;
    @FXML
    private LabeledIntegerTextField listFetchSizeTxt;
    @FXML
    private StackPane stckPneSelectedEntry;
    @FXML
    private ScrollPane workflowPane;
    @FXML
    private ScrollPane sessionsPane;
    @FXML
    private TableView<CpxSession> sessionTable;
    @FXML
    private VBox searchLists1;
    @FXML
    private CheckBox checkBoxWvState;
    @FXML
    private CheckBox checkBoxWvNumber;
    @FXML
    private CheckBox checkBoxWvFNLawer;
    @FXML
    private CheckBox checkBoxWvFNCourt;
    @FXML
    private CheckBox checkBoxWvShowAlwaysInfoForExaminateQuota;
    @FXML
    private CheckBox checkBoxWvUser;
    @FXML
    private CheckBox checkBoxWvSubject;
    @FXML
    private TableColumn<CpxSession, CpxSession> clientId;
    @FXML
    private TableColumn<CpxSession, CpxSession> userName;
    @FXML
    private TableColumn<CpxSession, CpxSession> userDb;
    @FXML
    private TableColumn<CpxSession, CpxSession> userRole;
    @FXML
    private TableColumn<CpxSession, CpxSession> loginSince;
    @FXML
    private TableColumn<CpxSession, CpxSession> lastAction;
    @FXML
    private TableColumn<CpxSession, CpxSession> appType;
    @FXML
    private ScrollPane protocolPane;

    private final Callback<Void, Void> onSessionsOpened = new Callback<>() {
        @Override
        public Void call(Void param) {
            EjbProxy<AuthServiceEJBRemote> authServiceBean = Session.instance().getEjbConnector().connectAuthServiceBean();
            List<CpxSession> sessions = authServiceBean.get().getCpxSessions();
            Collections.sort(sessions, new Comparator<CpxSession>() {
                @Override
                public int compare(CpxSession o1, CpxSession o2) {
//                        int c = o1.getUserName().compareToIgnoreCase(o2.getUserName());
//                        if (c != 0) {
//                            return c;
//                        }
                    if (o1.getLastActionAt() == null && o2.getLastActionAt() == null) {
                        return 0;
                    }
                    if (o1.getLastActionAt() == null) {
                        return -1;
                    }
                    if (o2.getLastActionAt() == null) {
                        return 1;
                    }
                    return o1.getLastActionAt().compareTo(o2.getLastActionAt());
                }
            });
            sessionTable.getItems().clear();
//                sessionTable.getColumns().clear();
//
//                TableColumn<CpxSession, CpxSession> clientId = new TableColumn<>("Client-ID");
//                TableColumn<CpxSession, CpxSession> userName = new TableColumn<>("Name");
//                TableColumn<CpxSession, CpxSession> userDb = new TableColumn<>("Datenbank");
//                TableColumn<CpxSession, CpxSession> loginSince = new TableColumn<>("Login");
//                TableColumn<CpxSession, CpxSession> lastAction = new TableColumn<>("Letzte Aktion");
//                TableColumn<CpxSession, CpxSession> type = new TableColumn<>("Typ");
//
//                sessionTable.getColumns().addAll(clientId, userName, userDb, loginSince, lastAction, type);

            clientId.setCellValueFactory(new SimpleCellValueFactory<>());
            clientId.setCellFactory(new Callback<TableColumn<CpxSession, CpxSession>, TableCell<CpxSession, CpxSession>>() {
                @Override
                public TableCell<CpxSession, CpxSession> call(TableColumn<CpxSession, CpxSession> p) {
                    return new TableCell<>() {
                        @Override
                        protected void updateItem(CpxSession item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null) {
                                return;
                            }
                            setText(item.getClientId());
                        }
                    };
                }
            });

            userName.setCellValueFactory(new SimpleCellValueFactory<>());
            userName.setCellFactory(new Callback<TableColumn<CpxSession, CpxSession>, TableCell<CpxSession, CpxSession>>() {
                @Override
                public TableCell<CpxSession, CpxSession> call(TableColumn<CpxSession, CpxSession> p) {
                    return new TableCell<>() {
                        @Override
                        protected void updateItem(CpxSession item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null) {
                                return;
                            }
                            setText(item.getUserName());
                        }
                    };
                }
            });

            userDb.setCellValueFactory(new SimpleCellValueFactory<>());
            userDb.setCellFactory(new Callback<TableColumn<CpxSession, CpxSession>, TableCell<CpxSession, CpxSession>>() {
                @Override
                public TableCell<CpxSession, CpxSession> call(TableColumn<CpxSession, CpxSession> p) {
                    return new TableCell<>() {
                        @Override
                        protected void updateItem(CpxSession item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null) {
                                return;
                            }
                            setText(item.getActualDatabase());
                        }
                    };
                }
            });

            userRole.setCellValueFactory(new SimpleCellValueFactory<>());
            userRole.setCellFactory(new Callback<TableColumn<CpxSession, CpxSession>, TableCell<CpxSession, CpxSession>>() {
                @Override
                public TableCell<CpxSession, CpxSession> call(TableColumn<CpxSession, CpxSession> p) {
                    return new TableCell<>() {
                        @Override
                        protected void updateItem(CpxSession item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null) {
                                return;
                            }
                            setText(item.getActualRoleName());
                        }
                    };
                }
            });

            loginSince.setCellValueFactory(new SimpleCellValueFactory<>());
            loginSince.setCellFactory(new Callback<TableColumn<CpxSession, CpxSession>, TableCell<CpxSession, CpxSession>>() {
                @Override
                public TableCell<CpxSession, CpxSession> call(TableColumn<CpxSession, CpxSession> p) {
                    return new TableCell<>() {
                        @Override
                        protected void updateItem(CpxSession item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null) {
                                return;
                            }
                            setText(Lang.toDateTime(item.getLoginDate()));
                        }
                    };
                }
            });

            lastAction.setCellValueFactory(new SimpleCellValueFactory<>());
            lastAction.setCellFactory(new Callback<TableColumn<CpxSession, CpxSession>, TableCell<CpxSession, CpxSession>>() {
                @Override
                public TableCell<CpxSession, CpxSession> call(TableColumn<CpxSession, CpxSession> p) {
                    return new TableCell<>() {
                        @Override
                        protected void updateItem(CpxSession item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null) {
                                return;
                            }
                            setText(Lang.toDateTime(item.getLastActionAt()));
                        }
                    };
                }
            });

            appType.setCellValueFactory(new SimpleCellValueFactory<>());
            appType.setCellFactory(new Callback<TableColumn<CpxSession, CpxSession>, TableCell<CpxSession, CpxSession>>() {
                @Override
                public TableCell<CpxSession, CpxSession> call(TableColumn<CpxSession, CpxSession> p) {
                    return new TableCell<>() {
                        @Override
                        protected void updateItem(CpxSession item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null) {
                                return;
                            }
                            setText(item.getAppType() == null ? "---" : item.getAppType().getTitle());
                        }
                    };
                }
            });

            sessionTable.getItems().addAll(sessions);

            return null;
        }
    };
    @FXML
    private Label logFileLabel;
    @FXML
    private ComboBox<org.apache.logging.log4j.Level> logLevelCombobox;
    @FXML
    private Button viewLogFileButton;
    @FXML
    private Button btnMenuCacheClear;

    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb resource bundle
     */
    @Override
    public synchronized void initialize(URL url, ResourceBundle rb) {
//        setUpCategories();
        // remove "checkBoxAllReminders" option, if no "FM module" license is found..
        removeProcessListRemindersOption();
        // remove "checkBoxLocal" option, if no "DRG & PEPP modules" license is found..
        removeCaseListLocalCasesOption();
        spContent.setDividerPositions(0.25);
        btnCatalogUpdate.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.REFRESH));
        btnCatalogUpdate.setText(Lang.getCatalogMenuUpdate());
        btnMenuCacheClear.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ERASER));
        btnMenuCacheClear.setText(Lang.getSettingFreeCache());
        //lbWv.setText("Vorgangsliste Konfiguration");
        checkBoxAllReminders.setText(Lang.getSettingShowAllReminders());
        checkBoxShowDetailsOverview.setText(Lang.getSettingShowPreview());
        checkBoxWvState.setText(Lang.getWorkflowStateObj().getAbbreviation());
        checkBoxWvNumber.setText(Lang.getWorkflowNumber());
        checkBoxWvFNLawer.setText(Lang.getLawerFileNumber());
        checkBoxWvFNCourt.setText(Lang.getCourtFileNumber());
        checkBoxWvUser.setText(Lang.getAuditEditor());
        checkBoxWvSubject.setText(Lang.getProcessTopic());
        checkBoxWvShowAlwaysInfoForExaminateQuota.setText(Lang.getSettingShowAlwaysInfoForExaminateQuota()); 
        if (catalogUpdaterThread != null) {
            catalogUpdaterThread.interrupt();
//            if (catalogUpdaterThread.isAlive()) {
//                catalogUpdaterThread.stop();
//            }
        }
        catalogUpdateNotificationShown = false;
        catalogUpdaterThread = new Thread(new Runnable() {
            private final double intervalMinutes = 10;
            private final int intervalSeconds = (int) Math.round(intervalMinutes * 60);

            @Override
            public void run() {
                LOG.log(Level.INFO, "Catalog updater thread interval: {0} minutes / {1} seconds", new Object[]{intervalMinutes, intervalSeconds});
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Thread.sleep(intervalSeconds * 1000L);
                    } catch (InterruptedException ex) {
                        LOG.log(Level.FINEST, null, ex);
                        Thread.currentThread().interrupt();
                    }
                    if (Thread.currentThread().isInterrupted()) {
                        break;
                    }
                    CpxTask<Map<CatalogTypeEn, List<CpxCatalogOverview>>> worker = checkCatalogUpdate(false);
                    try {
                        worker.get();
                    } catch (ExecutionException ex) {
                        LOG.log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        LOG.log(Level.SEVERE, null, ex);
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
        catalogUpdaterThread.start();

        final StringProperty listFetchSizeValue = new SimpleStringProperty();
        listFetchSizeTxt.setText(String.valueOf(CpxClientConfig.instance().getSearchListFetchSize()));
        listFetchSizeTxt.getControl().focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                //focus lost -> store value
                final String text = listFetchSizeTxt.getText();
                if (text.equalsIgnoreCase(listFetchSizeValue.get())) {
                    LOG.log(Level.INFO, "fetch list size unchanged");
                    return;
                }
                boolean stored;
                try {
                    final int newSize = Integer.parseInt(text);
                    LOG.log(Level.INFO, "try to set fetch list size to " + newSize);
                    Session.instance().setSearchListFetchSize(newSize);
                    stored = true;
                } catch (NumberFormatException ex) {
                    LOG.log(Level.FINEST, "This is not a valid integer: " + text, ex);
                    stored = false;
                }
                int serverSize = Session.instance().getSearchListFetchSize();
                if (stored) {
                    LOG.log(Level.INFO, "fetch list size is now " + serverSize);
                }
                listFetchSizeTxt.setText(String.valueOf(Session.instance().getSearchListFetchSize()));
            } else {
                listFetchSizeValue.set(listFetchSizeTxt.getText());
            }
        });
        setLogFileBox();
    }

    @Override
    public void afterInitialisingScene() {
        super.afterInitialisingScene(); //To change body of generated methods, choose Tools | Templates.
        setUpCategories();
    }
    /**
     * select a specific category by name
     * @param pCategory Category as string, use Lang.getWhatEverNameYouWant
     */
    public void selectCategory(String pCategory){
        if(pCategory == null || pCategory.isEmpty()){
            return;
        }
        if(lvCategory == null){
            return;
        }
        if(categoryList.isEmpty()){
            return;
        }
        SettingsCategory category = categoryList.stream().filter(new Predicate<SettingsCategory>() {
            @Override
            public boolean test(SettingsCategory t) {
                return t.label.toLowerCase().equals(pCategory.toLowerCase());
            }
        }).findFirst().orElse(null); //get first entry there is to find or null
        if(category == null){
            return;
        }
        lvCategory.getSelectionModel().select(category);
    }
    private void setUpCategories() {
        //SearchList selectedSearchList = CpxClientConfig.instance().getSelectedSearchListResult(LIST.WORKING);
        checkBoxLocal.setSelected(Session.instance().isCaseLocal());
        checkBoxAllReminders.setSelected(Session.instance().isShowAllRemindersConfig());
        checkBoxShowDetailsOverview.setSelected(Session.instance().isShowFilterListDetailsOverview());
        checkBoxWvFNCourt.setSelected(Session.instance().isWmMainFrameFNCourt());
        checkBoxWvFNLawer.setSelected(Session.instance().isWmMainFrameFNLawer());
        checkBoxWvNumber.setSelected(Session.instance().isWmMainFrameWVNumber());
        checkBoxWvState.setSelected(Session.instance().isWmMainFrameState());
        checkBoxWvSubject.setSelected(Session.instance().isWmMainFrameSubject());
        checkBoxWvUser.setSelected(Session.instance().isWmMainFrameWvUser());
        checkBoxWvShowAlwaysInfoForExaminateQuota.setSelected(Session.instance().showAlwaysInfoForExaminateQuota());
        categoryList.add(new SettingsCategory(Lang.getSettingCategoryWebApp(), webAppPane));
        if (MainApp.getType() == AppTypeEn.CLIENT) {
            categoryList.addAll(new SettingsCategory(Lang.getSettingSearchLists(), searchListsPane), new SettingsCategory(Lang.getSettingWorkflowSight(), workflowPane));
        }
        categoryList.add(new SettingsCategory(Lang.getCatalogDownloadCatalogs(), updatePane));

        if (Session.instance().getRoleProperties().isConfigSystemAllowed()) {
            categoryList.add(new SettingsCategory(Lang.getSettingSession(), sessionsPane, onSessionsOpened));
        }
        categoryList.add(new SettingsCategory(Lang.getSettingProtocolFile(), protocolPane));
        categoryList.addAll(getScene().getAdditionalCategorys());

        checkBoxLocal.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Session.instance().setCaseLocal(checkBoxLocal.isSelected());
            }
        });

        checkBoxAllReminders.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Session.instance().setShowAllRemindersConfig(checkBoxAllReminders.isSelected());
            }
        });

        checkBoxShowDetailsOverview.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Session.instance().setFilterListDetailsOverview(checkBoxShowDetailsOverview.isSelected());
            }
        });
        checkBoxWvSubject.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Session.instance().setWmMainFrameSubject(checkBoxWvSubject.isSelected());
            }
        });
        checkBoxWvState.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Session.instance().setWmMainFrameState(checkBoxWvState.isSelected());
            }
        });
        checkBoxWvNumber.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Session.instance().setWmMainFrameWVNumber(checkBoxWvNumber.isSelected());
            }
        });
        checkBoxWvFNLawer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Session.instance().setWmMainFrameFNLawer(checkBoxWvFNLawer.isSelected());
            }
        });
        checkBoxWvFNCourt.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Session.instance().setWmMainFrameFNCourt(checkBoxWvFNCourt.isSelected());
            }
        });
        checkBoxWvUser.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Session.instance().setWmMainFrameWvUser(checkBoxWvUser.isSelected());
            }
        });
        checkBoxWvShowAlwaysInfoForExaminateQuota.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Session.instance().setAlwaysInfoForExaminateQuota(checkBoxWvShowAlwaysInfoForExaminateQuota.isSelected());
            }
        });
        lvCategory.setItems(categoryList);

        lvCategory.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                SettingsCategory category = categoryList.get(observable.getValue().intValue());
                if (!stckPneSelectedEntry.getChildren().contains(category.pane)) {
                    stckPneSelectedEntry.getChildren().add(category.pane);
                }
                //category.pane.toFront();
                if (category.pane != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            category.pane.toFront();
                        }
                    });
                }
                LOG.log(Level.FINEST, "selected category: " + category.toString());
                if (category.onOpenCb != null) {
                    category.onOpenCb.call(null);
                }
            }
        });
        btnOpenWebApp.setText(Lang.getWebappOpenBtn());
        btnOpenWebApp.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.LINK));
        btnOpenWebApp.setTooltip(new Tooltip(Lang.getWebappOpenBtnTooltip()));
        btnOpenManagementIf.setVisible(Session.instance().getRoleProperties().isConfigSystemAllowed());
        btnOpenManagementIf.setText(Lang.getWebappOpenManagement());
        btnOpenManagementIf.setTooltip(new Tooltip(Lang.getWebappOpenManagementTooltip()));
        btnOpenManagementIf.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.LINK));
        /*
        lvCategory.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
          @Override
          public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            System.out.println(observable);
            System.out.println(oldValue);
            System.out.println(newValue);
          }
        });
         */
        lvCategory.getSelectionModel().select(0);
    }

    @FXML
    private void onCatalogUpdate(ActionEvent event) {
        checkCatalogUpdate(true);
//        Notifications.create().owner(BasicMainApp.getWindow())
//                .title("CPX: " + Lang.getCatalogMenuUpdate())
//                .text(Lang.getCatalogUpdateNotification())
//                .hideAfter(Duration.seconds(15))
//                .show();
    }

    public CpxTask<Map<CatalogTypeEn, List<CpxCatalogOverview>>> checkCatalogUpdate(final boolean pExecuteAutomatically) {
        LOG.log(Level.FINE, "Will check for catalog changes now...");
        Glyph glyph = (Glyph) btnCatalogUpdate.getGraphic();
        final RotateTransition rt = new RotateTransition(Duration.seconds(3), glyph);
        rt.setByAngle(720);
        //rt.setToAngle(360);
        rt.setCycleCount(INDEFINITE);
        //rt.setAutoReverse(true);
        rt.play();
        CpxTask<Map<CatalogTypeEn, List<CpxCatalogOverview>>> worker = new CpxTask<Map<CatalogTypeEn, List<CpxCatalogOverview>>>(this) {
            @Override
            protected Map<CatalogTypeEn, List<CpxCatalogOverview>> call() throws Exception {
                Map<CatalogTypeEn, List<CpxCatalogOverview>> catalogOverview = StartupLoaderFXMLController.checkCatalogUpdate(false);
                return catalogOverview;
            }
        };
        worker.setOnRunning(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                btnCatalogUpdate.setDisable(true);
                btnCatalogUpdate.setWrapText(true);
                btnCatalogUpdate.setText(Lang.getCatalogMenuUpdate() + "\r\n" + Lang.getPleaseWait());
                //while(true) {
                /*
              btnCatalogUpdate.getGraphic().rotateProperty().add(40d);
              Platform.runLater(new Runnable() {
                @Override
                public void run() {
                  btnCatalogUpdate.getGraphic().rotateProperty().add(40d);
                }
              });
                 */
 /*
              try {
                Thread.sleep(100);
              } catch (InterruptedException ex) {
                LOG.log(Level.SEVERE, null, ex);
              }
                 */
                //}
            }
        });
        worker.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                btnCatalogUpdate.setText(Lang.getCatalogMenuUpdate());
                btnCatalogUpdate.setDisable(false);
                rt.stop();
            }
        });
        worker.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                btnCatalogUpdate.setText(Lang.getCatalogMenuUpdate());
                btnCatalogUpdate.setDisable(false);
                rt.stop();
            }
        });
        worker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public final void handle(WorkerStateEvent t) {
                boolean executed = false;
//                final DialogPane pane = getDialogPane();
                try {
                    Map<CatalogTypeEn, List<CpxCatalogOverview>> catalogOverview = worker.get();
                    if (catalogUpdaterThread != null && catalogUpdaterThread.isInterrupted()) {
                        return;
                    }
                    if (!StartupLoaderFXMLController.isCatalogOutdated(catalogOverview)) {
                        if (pExecuteAutomatically) {
                            BasicMainApp.showInfoMessageDialog(Lang.getCatalogMenuNo_updates_found(), getWindow());
                        }
                        //btnCatalogUpdate.setText(Lang.getCatalogMenu_update() + "\r\nLast check at " + CpxLanguageInterface.toDateTime(new Date(), "HH:mm:ss") + ", no changes");
                        btnCatalogUpdate.setText(Lang.getCatalogMenuUpdate() + "\r\n" + Lang.getCatalogMenuNo_update_changes_found(CpxLanguageInterface.toDateTime(new Date(), "HH:mm:ss")));
                        btnCatalogUpdate.setDisable(false);
                        catalogUpdateNotificationShown = false;
                        rt.stop();
                        return;
                    } else {
                        //btnCatalogUpdate.setText(Lang.getCatalogMenuUpdate() + "\r\nLast check at " + CpxLanguageInterface.toDateTime(new Date(), "HH:mm:ss") + ", " + catalogOverview.size() + " changes!");
                        Map<CatalogTodoEn, Integer> todoMap = StartupLoaderFXMLController.getCatalogChanges(catalogOverview);
                        Integer count = 0;
                        for (Map.Entry<CatalogTodoEn, Integer> entry : todoMap.entrySet()) {
                            count += entry.getValue();
                        }
                        btnCatalogUpdate.setText(Lang.getCatalogMenuUpdate() + "\r\n" + Lang.getCatalogMenuUpdate_changes_found(count, CpxLanguageInterface.toDateTime(new Date(), "HH:mm:ss")));
                        if (!catalogUpdateNotificationShown) {
                            catalogUpdateNotificationShown = true;
                            Notifications notif = NotificationsFactory.instance().createInformationNotification();
                            notif.owner(BasicMainApp.getWindow())
                                    .onAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent t) {
                                            checkCatalogUpdate(true);
                                        }
                                    })
                                    .title("CPX: " + Lang.getCatalogMenuUpdate())
                                    .text(Lang.getCatalogUpdateNotification())
                                    .hideAfter(Duration.seconds(15))
                                    .show();
                        }
                    }
                    StartupLoaderFXMLController.setCatalogOverview(catalogOverview);
                    if (pExecuteAutomatically) {
                        StartupLoaderScene startupLoaderScene = new StartupLoaderScene();
                        BasicMainApp.setScene(startupLoaderScene);
                        executed = true;
                    }
                } catch (IOException | ExecutionException ex) {
                    LOG.log(Level.SEVERE, "An error occured", ex);
                } catch (InterruptedException ex) {
                    LOG.log(Level.SEVERE, "task was interrupted", ex);
                    Thread.currentThread().interrupt();
                }
                if (executed) {
                    btnCatalogUpdate.setText(Lang.getCatalogMenuUpdate());
                }
                btnCatalogUpdate.setDisable(false);
                rt.stop();
            }
        });
        new Thread(worker).start();
        return worker;
    }

    @FXML
    private void openWebApp(ActionEvent event) throws NamingException {
        //String server = CpxClientConfig.instance().getServerHost();
        //String port = String.valueOf(CpxClientConfig.instance().getServerPort());
        //String url = "http://"+server+":"+port+"/cpx-web-app/";
        final String url = Session.instance().getEjbConnector()
                .connectAuthServiceBean().get().getWebAppUrl();
        BasicMainApp.openUrl(url);
    }

    @FXML
    private void openManagementIf(ActionEvent event) throws NamingException {
        final String url = Session.instance().getEjbConnector()
                .connectAuthServiceBean().get().getManagementInterfaceUrl();
        BasicMainApp.openUrl(url);
    }

    private void removeProcessListRemindersOption() {
        License license = Session.instance().getLicense();
        if (!license.isFmModule()) {
            searchLists.getChildren().remove(checkBoxAllReminders);
        }
    }

    private void removeCaseListLocalCasesOption() {
        License license = Session.instance().getLicense();
        if (!license.isDrgModule() && !license.isPeppModule()) {
            searchLists.getChildren().remove(checkBoxLocal);
        }
    }

    public Window getWindow() {
        return spContent.getScene().getWindow();
    }

    public DialogPane getDialogPane() {
        return (DialogPane) spContent.getScene().getRoot();
    }

    private void setLogFileBox() {
        final File logFile = InitLogger.instance().getLogFile();
        logFileLabel.setText(logFile == null ? "LOG FILE IS NULL" : logFile.getName());
        logFileLabel.setTooltip(new Tooltip("Click to select file in Windows Explorer"));
        logFileLabel.setOnMouseClicked((event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                ToolbarMenuFXMLController.openInExplorer(logFile);
            }
        });
        org.apache.logging.log4j.Level[] levels = org.apache.logging.log4j.Level.values();
        ObservableList<org.apache.logging.log4j.Level> items = FXCollections.observableArrayList(levels);
        items.sort(new Comparator<org.apache.logging.log4j.Level>() {
            @Override
            public int compare(org.apache.logging.log4j.Level o1, org.apache.logging.log4j.Level o2) {
                return o1.compareTo(o2);
            }
        });
        logLevelCombobox.setItems(items);
        logLevelCombobox.getSelectionModel().select(InitLogger.instance().getLogLevel());
        logLevelCombobox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<org.apache.logging.log4j.Level>() {
            @Override
            public void changed(ObservableValue<? extends org.apache.logging.log4j.Level> ov, org.apache.logging.log4j.Level oldValue, org.apache.logging.log4j.Level newValue) {
                InitLogger.instance().setLogLevel(newValue);
            }
        });

        Callback<ListView<org.apache.logging.log4j.Level>, ListCell<org.apache.logging.log4j.Level>> cellFactory = new Callback<ListView<org.apache.logging.log4j.Level>, ListCell<org.apache.logging.log4j.Level>>() {

            @Override
            public ListCell<org.apache.logging.log4j.Level> call(ListView<org.apache.logging.log4j.Level> l) {
                return new ListCell<org.apache.logging.log4j.Level>() {

                    @Override
                    protected void updateItem(org.apache.logging.log4j.Level level, boolean empty) {
                        super.updateItem(level, empty);
                        if (level == null || empty) {
                            setGraphic(null);
                        } else {
//                            final String gerText;
                            final String tooltipText;
                            if (level.equals(org.apache.logging.log4j.Level.ALL)) {
//                                gerText = "Alle";
                                tooltipText = Lang.getLevelAll();
                            } else if (level.intLevel() == 700) { //FINEST
                                //FINEST level is dynamically added at runtime. I think it is internally created by Java through this command: new Level("FINEST", 700).
//                                gerText = "Feinstes";
                                tooltipText = Lang.getLevelFinest();
                            } else if (level.equals(org.apache.logging.log4j.Level.TRACE)) {
//                                gerText = "Nachverfolgung";
                                tooltipText = Lang.getLevelTrace();
                            } else if (level.equals(org.apache.logging.log4j.Level.DEBUG)) {
//                                gerText = "Entwanzung";
                                tooltipText = Lang.getLevelDebug();
                            } else if (level.equals(org.apache.logging.log4j.Level.INFO)) {
//                                gerText = "Information";
                                tooltipText = Lang.getLevelInfo(); // (Programm gestartet, Programm beendet, Verbindung zu Host Foo aufgebaut, Verarbeitung dauerte SoUndSoviel Sekunden …)";
                            } else if (level.intLevel() == 450) { //CONFIG
                                //CONFIG level is dynamically added at runtime. I think it is internally created by Java through this command: new Level("CONFIG", 450).
//                                gerText = "Konfiguration";
                                tooltipText = Lang.getLevelConfig();
                            } else if (level.equals(org.apache.logging.log4j.Level.WARN)) {
//                                gerText = "Warnung";
                                tooltipText = Lang.getLevelWarning();
                            } else if (level.equals(org.apache.logging.log4j.Level.ERROR)) {
//                                gerText = "Fehler";
                                tooltipText = Lang.getLevelError();
                            } else if (level.equals(org.apache.logging.log4j.Level.FATAL)) {
//                                gerText = "Verhängnis";
                                tooltipText = Lang.getLevelFatal();
                            } else if (level.equals(org.apache.logging.log4j.Level.OFF)) {
//                                gerText = "Aus";
                                tooltipText = Lang.getLevelOff(); 
                            } else {
                                LOG.log(Level.WARNING, "Unknown log level: {0} ({1})", new Object[]{level.name(), level.intLevel()});
//                                gerText = null;
                                tooltipText = null;
                            }

                            //setText(level.name() + (text == null ? "" : " (" + text + ")"));
                            final String text = level.name(); // + (gerText == null ? "" : " (" + gerText + ")");
                            if (tooltipText != null) {
                                Tooltip tt = new Tooltip(tooltipText);
                                setTooltip(tt);
                            }
                            setText(text);
                            setGraphic(null);
                        }
                    }
                };
            }
        };

        logLevelCombobox.setButtonCell(cellFactory.call(null));
        logLevelCombobox.setCellFactory(cellFactory);
    }

    @FXML
    private void viewLogFileClick(ActionEvent event) {
        final File logFile = InitLogger.instance().getLogFile();
        ToolbarMenuFXMLController.editFile(logFile);
    }

    @FXML
    private void onMenuCacheUpdate(ActionEvent event) {
//        MenuCache.instance().destroy();
        Map<MenuCacheEntryEn, MenuCacheEntry<? extends Serializable, ? extends Serializable>> cacheEntries = MenuCache.getMenuCacheEntriesCopy();
        if (MenuCache.uninitialize()) {
//            StringBuilder sb = new StringBuilder();
//            Iterator<Map.Entry<MenuCacheEntryEn, MenuCacheEntry<? extends Serializable, ? extends Serializable>>> it = cacheEntries.entrySet().iterator();
//            while (it.hasNext()) {
//                Map.Entry<MenuCacheEntryEn, MenuCacheEntry<? extends Serializable, ? extends Serializable>> entry = it.next();
//                if (!entry.getValue().isEmpty()) {
//                    if (sb.length() > 0) {
//                        sb.append("\n");
//                    }
//                    //sb.append(entry.getKeyName() + " -> " + entry.getValueName() + ": " + entry.size());
//                    sb.append(entry.getValue().getValueTranslation().getValue() + ": " + entry.getValue().size());
//                }
//            }
//            BasicMainApp.showInfoMessageDialog(Lang.getMsgClearCache() + sb.toString(), getWindow());
            String clearedItems = cacheEntries.entrySet().stream()
                    .filter((t) -> {
                        return t.getValue().size() > 0;
                    }).map((t) -> {
                        return new StringBuilder(t.getValue().getValueTranslation().getValue()).append(" (").append(t.getValue().size()).append(")").toString();
                    })
                    .sorted()
                    .collect(Collectors.joining("\n"));
            BasicMainApp.showInfoMessageDialog(Lang.getMsgClearCache() + "\n" + clearedItems, getWindow());
        } else {
            BasicMainApp.showInfoMessageDialog(Lang.getMsgCacheAlreadyClear(), getWindow());
        }
    }

}
