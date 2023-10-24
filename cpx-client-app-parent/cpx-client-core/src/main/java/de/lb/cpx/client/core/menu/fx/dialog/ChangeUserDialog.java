/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.menu.fx.dialog;

import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.button.ReloadButton;
import de.lb.cpx.client.core.model.fx.dialog.TitledDialog;
import de.lb.cpx.client.core.model.fx.labeled.LabeledLabel;
import de.lb.cpx.client.core.model.fx.labeled.LabeledListView;
import de.lb.cpx.client.core.model.fx.labeled.LabeledPasswortField;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.client.core.model.fx.listview.AsyncListView;
import de.lb.cpx.client.core.model.fx.login.DatabaseListCell;
import de.lb.cpx.client.core.model.task.LoginTask;
import de.lb.cpx.connector.EjbConnector;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.service.ejb.LockService;
import de.lb.cpx.service.ejb.LoginServiceEJBRemote;
import de.lb.cpx.service.information.CpxDatabase;
import de.lb.cpx.shared.lang.Lang;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import javafx.util.Callback;
import javax.ejb.AsyncResult;
import javax.naming.NamingException;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

/**
 * Change Database dialog, handels display of login loading, self closing maybe
 * make extends of formular dialog? needed to refactor that onSave methode
 *
 * @author wilde
 */
public class ChangeUserDialog extends TitledDialog {

    private static final Logger LOG = Logger.getLogger(ChangeUserDialog.class.getName());
    private static final String PROGRESS_INDICATOR_STYLE = "async-progress-indicator";
    private static final String LIST_VIEW_STYLE = "stay-selected-list-view";

    private final ValidationSupport validation = new ValidationSupport();
    private final LabeledLabel server = new LabeledLabel();
    private final LabeledTextField user = new LabeledTextField();
    private final LabeledPasswortField password = new LabeledPasswortField();
    private final Label lblstatus = new Label();
    private final ReadOnlyBooleanWrapper successProperty = new ReadOnlyBooleanWrapper(false);
    private final String initialUserValue = Session.instance().getCpxUserName();
    private final String initialDatabaseValue = Session.instance().getCpxDatabase();
    //set up listview with async list
    private final LabeledListView<CpxDatabase> databases = new LabeledListView<>(new AsyncListView<CpxDatabase>() {
        @Override
        public void beforeTask() {
            super.beforeTask(); //To change body of generated methods, choose Tools | Templates.
            if(reloadBtn != null){
                reloadBtn.setDisable(true);
            }
        }
        
        @Override
        public Future<List<CpxDatabase>> getFuture() {
            List<CpxDatabase> result = Session.instance().getAllowedCpxDatabases();
            Collections.sort(result);
            return new AsyncResult<>(result);
        }

        @Override
        public void afterTask(Worker.State pState) {
            super.afterTask(pState);
            //after loading on succeeded select database
            if (pState.equals(Worker.State.SUCCEEDED)) {
                selectDatabase(initialDatabaseValue);
            }
            if(reloadBtn != null){
                reloadBtn.setDisable(false);
            }
        }

    });
    private ReloadButton reloadBtn;

    /**
     * construct new instance
     *
     * @param pTitle title of the dialog
     * @param pOwner owner of the dialog
     */
    public ChangeUserDialog(String pTitle, Window pOwner) {
        super(pTitle, pOwner, true);
        //register validation/ avoid exception
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                registerValidation();
            }
        });
        //set interceptor to button, that dialog is not closed on ok
        Button btn = getDialogSkin().getButton(ButtonType.OK);
        btn.addEventFilter(ActionEvent.ACTION, event -> {
            try {
                event.consume();
                //start change db change/logout-login
                loginTo(getSelectedItem());
            } catch (NamingException ex) {
                Logger.getLogger(ChangeUserDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        setContent(getSelectionContent());

        //CPX-987 - Allow double click to change user/database immediately
        databases.getListView().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (MouseButton.PRIMARY == event.getButton()
                        && event.getClickCount() == 2) {
                    if (!btn.isDisabled()) {
                        btn.fire();
                    }
                }
            }
        });
    }

    /*
    * GETTER/SETTER
     */
    /**
     * @return currently selctedItem in listview
     */
    public CpxDatabase getSelectedItem() {
        return databases.getSelectedItem();
    }

    /**
     * @return indicator if changing of database/login was successful
     */
    public Boolean isSuccess() {
        return successProperty.get();
    }

    /**
     * @return readonly indicator property if chagne database/login was
     * successful - for bindings
     */
    public ReadOnlyBooleanProperty successProperty() {
        return successProperty.getReadOnlyProperty();
    }
    private void reloadListView(){
        if(databases.getListView() instanceof AsyncListView){
            ((AsyncListView)databases.getListView()).reload();
        }
    }
    /*
    * UI
     */
    //selection content shows database, server, username, password
    private Pane getSelectionContent() {
        //setup list view with pref Sizes and style class
        databases.setTitle(Lang.getLoginDatabase());
        databases.getSkin();
        databases.setPrefSize(300.0d, 300.0d);
        databases.getControl().getStyleClass().add(LIST_VIEW_STYLE);
        databases.setCellFactory(new Callback<ListView<CpxDatabase>, ListCell<CpxDatabase>>() {
            @Override
            public ListCell<CpxDatabase> call(ListView<CpxDatabase> param) {
                return new DatabaseListCell();
            }
        });
        reloadBtn = new ReloadButton();
        reloadBtn.setOnAction(new EventHandler() {
            @Override
            public void handle(Event t) {
                reloadListView();
            }
        });
        databases.getMenuItems().add(reloadBtn);
        //textfields for selection
        //server might not be needed
        server.setTitle(Lang.getServer());
        Session.instance();
        server.setText(CpxClientConfig.instance().getServerSocket());

        user.setText(initialUserValue);
        user.setTitle(Lang.getLoginUser());

        password.setTitle(Lang.getLoginPassword());

        //general layout
        VBox wrapper = new VBox(server, user, password, databases);
        wrapper.setAlignment(Pos.CENTER_LEFT);
        wrapper.setSpacing(10.0d);
        setContent(wrapper);

        //bind ok button to validation
        Button btn = getDialogSkin().getButton(ButtonType.OK);
        btn.disableProperty().bind(validation.invalidProperty());
        //load at last databases async
        ((AsyncListView) databases.getControl()).reload();

        return wrapper;
    }

    //loading pane - progressIndicator + status label
    private Pane getLoadingContent() {
        ProgressIndicator progInd = new ProgressIndicator(-1);
        progInd.getStyleClass().add(PROGRESS_INDICATOR_STYLE);
        VBox wrapper = new VBox(progInd, lblstatus);
        wrapper.setAlignment(Pos.CENTER);
        disableButtons(true);
        return wrapper;
    }

    // select current bd with string(connectionString)
    private void selectDatabase(String cpxDatabase) {
        if (cpxDatabase == null || cpxDatabase.isEmpty()) {
            return;
        }
        for (CpxDatabase db : databases.getItems()) {
            if (db.getConnectionString().equalsIgnoreCase(cpxDatabase)) {
                databases.select(db);
                databases.getControl().scrollTo(db);
                return;
            }
        }
    }

    //disable/enables all buttons present in the buttonbar of the dialog
    private void disableButtons(boolean b) {
        for (ButtonType type : getDialogSkin().getButtonTypes()) {
            Button btn = getDialogSkin().getButton(type);
            if (btn != null) {
                if (btn.disableProperty().isBound()) {
                    btn.disableProperty().unbind();
                }
                btn.setDisable(b);
            }
        }
    }

    //register validation
    private void registerValidation() {
        //defines value extractor for custom control 
//        ValueExtractor.addObservableValueExtractor(new Predicate<Control>() {
//            @Override
//            public boolean test(Control t) {
//                return t instanceof LabeledListView;
//            }
//            //calls registered validators on changes of the selected item
//        }, c -> ((LabeledListView<CpxDatabase>) c).getSelectedItemProperty());

        validation.registerValidator(databases, new Validator<Object>() {
            @Override
            public ValidationResult apply(Control t, Object u) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, Lang.getMenuDatabaseChangeValidationDbSelection(), databases.getSelectedItem() == null);
                return res;
            }
        });
        validation.registerValidator(user.getControl(), Validator.createPredicateValidator(new Predicate<String>() {
            @Override
            public boolean test(String t) {
                return !t.isEmpty();
            }
        }, Lang.getLoginFailedUsernameMissing(), Severity.ERROR));
        validation.registerValidator(password.getControl(), Validator.createPredicateValidator(new Predicate<String>() {
            @Override
            public boolean test(String t) {
                return !t.isEmpty();
            }
        }, Lang.getLoginFailedPasswordMissing(), Severity.ERROR));
    }

    /*
    * LOGIN / LOGOUT
     */
    private void loginTo(CpxDatabase pDatabase) throws NamingException {
        EjbProxy<LoginServiceEJBRemote> loginServiceBean = Session.instance().getEjbConnector().connectLoginServiceBean();
        EjbProxy<LockService> lockServiceBean = Session.instance().getEjbConnector().connectLockServiceBean();
        if (Session.instance().getEjbConnector().connectAuthServiceBean().get().getUserRoleProperties(Session.instance().getCpxActualRoleId()).isDatabaseAllowed(pDatabase.getName())) //        logout if user is present
        {
            if (Session.instance().getCdbUser() != null) {
                MainApp.getToolbarMenuScene().cleanUp();
                lockServiceBean.get().removeAllForUser(Session.instance().getCpxUserId());
                loginServiceBean.get().doLogout();
            }
        }
        String usr = this.user.getText();
        String pw = this.password.getControl().getText();

        Session sess = Session.instance(false);
        EjbConnector existingEjbConnector = sess == null ? null : sess.getEjbConnector();
        Session.destroy();
        Session.instance().setEjbConnector(existingEjbConnector);

        Session.instance().getEjbConnector().initContexts(
                CpxClientConfig.instance().getServerHost(),
                CpxClientConfig.instance().getServerPort(),
                CpxClientConfig.instance().getServerUser(),
                CpxClientConfig.instance().getServerPassword());
        Session.instance().setCpxLocale(CpxClientConfig.instance().getLanguage());

        startLoginTask(usr, pw, pDatabase);
    }

    private void startLoginTask(String pUser, String pPassword, CpxDatabase pDatabase) {
        LoginTask task = new LoginTask(pUser, pPassword, pDatabase.getConnectionString(), false);
        task.setOnRunning(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                Pane loading = getLoadingContent();
                setContent(loading);
                lblstatus.setText(Lang.getPleaseWait());
            }
        });
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                successProperty.set(true);
                MainApp.getToolbarMenuScene().restoreRecentClientScene();
                ChangeUserDialog.this.close();
                //Session.instance().getCaseCount(true);
                //Session.instance().getProcessCount(true);

//                BasicMainApp.getToolbarMenuScene().getController().showWorkingList();
            }
        });
        task.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                LOG.severe("Can not change user, reason " + task.getException().getMessage());
                disableButtons(false);
                AlertDialog.createErrorDialog(task.getException(), getDialogSkin().getStage())
                        .showAndWait()
                        .ifPresent(new Consumer<ButtonType>() {
                            @Override
                            public void accept(ButtonType t) {
                                if (t.equals(ButtonType.OK)) {
                                    Pane selection = getSelectionContent();
                                    setContent(selection);
                                }
                            }
                        });
            }
        });
        task.start();
    }

}
