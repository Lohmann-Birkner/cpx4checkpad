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
import de.lb.cpx.client.core.model.fx.dialog.MessageNode;
import de.lb.cpx.client.core.model.fx.dialog.TitledDialog;
import de.lb.cpx.client.core.model.fx.labeled.LabeledLabel;
import de.lb.cpx.client.core.model.fx.labeled.LabeledListView;
import de.lb.cpx.client.core.model.fx.listview.AsyncListView;
import de.lb.cpx.client.core.model.fx.login.DatabaseListCell;
import de.lb.cpx.client.core.model.fx.login.LoginFXMLController;
import de.lb.cpx.client.core.model.task.ChangeDbTask;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.service.ejb.LockService;
import de.lb.cpx.service.information.CpxDatabase;
import de.lb.cpx.shared.lang.Lang;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleObjectProperty;
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
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

/**
 * Dialog for changing the database
 *
 * @author wilde
 */
public class ChangeDatabaseDialog extends TitledDialog {

    private static final Logger LOG = Logger.getLogger(ChangeDatabaseDialog.class.getName());
    private static final String PROGRESS_INDICATOR_STYLE = "async-progress-indicator";
    private static final String LIST_VIEW_STYLE = "stay-selected-list-view";
    private CdbUserRoles role = null; 
    private final ValidationSupport validation = new ValidationSupport();
    private final LabeledLabel server = new LabeledLabel();
    private final Label lblstatus = new Label();
    private final ReadOnlyBooleanWrapper successProperty = new ReadOnlyBooleanWrapper(false);
    private String initialDatabaseValue = Session.instance().getCpxDatabase();
    //set up listview with async list
    private ObjectProperty<CpxDatabase> selectedDb4Role = new SimpleObjectProperty<>();
    private final LabeledListView<CpxDatabase> databases;
    private Callback<Void, Void> refreshToolBarCallback;
    Callback<CdbUserRoles, Void> changeToRoleCallback;
//    static{
//        //Define in mainApp?
//        ValueExtractor.addObservableValueExtractor(new Predicate<Control>() {
//            @Override
//            public boolean test(Control t) {
//                return t instanceof LabeledListView;
//            }
//            //calls registered validators on changes of the selected item
//        }, c -> ((LabeledListView<CpxDatabase>) c).getSelectedItemProperty());
//    }
    private ReloadButton reloadBtn;

    /**
     * construct new instance
     *
     * @param pTitle title of the dialog
     * @param pOwner owner of the dialog
     */
    public ChangeDatabaseDialog(String pTitle, Window pOwner){
        this(pTitle, pOwner, null);

    }
    
    public ChangeDatabaseDialog(String pTitle, Window pOwner, CdbUserRoles pRole) {
        super(pTitle, pOwner, true);
        role = pRole;
//        getDialogSkin().setMaxHeight(100);
//        getDialogSkin().setMaxWidth(100);
        //register validation/ avoid exception
        databases = new LabeledListView<>(new AsyncListView<CpxDatabase>() {
        @Override
        public void beforeTask() {
            super.beforeTask();
            if(reloadBtn != null){
                reloadBtn.setDisable(true);
            }
        }
        
        @Override
        public Future<List<CpxDatabase>> getFuture() {
            List<CpxDatabase> result = (role == null?Session.instance().getAllowedCpxDatabases():Session.instance().getAllowedCpxDatabases4RoleId(role.getId()));
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
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                registerValidation();
            }
        });
        //set interceptor to button, that dialog is not closed on ok
        Button btn = getDialogSkin().getButton(ButtonType.OK);
        btn.addEventFilter(ActionEvent.ACTION, event -> {
            event.consume();
            //start change db change/logout-login
            changeTo(getSelectedItem());
        });
        setContent(getSelectionContent());

        //CPX-987 - Allow double click to change database immediately
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

//        setOnCloseRequest(new EventHandler<DialogEvent>() {
//            @Override
//            public void handle(DialogEvent event) {
//                databases.getItems().clear();
//            }
//        });
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
    public void reloadListView(){
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
        reloadBtn = new ReloadButton();
        reloadBtn.setOnAction(new EventHandler() {
            @Override
            public void handle(Event t) {
                reloadListView();
            }
        });
        databases.getMenuItems().add(reloadBtn);
        databases.setCellFactory(new Callback<ListView<CpxDatabase>, ListCell<CpxDatabase>>() {
            @Override
            public ListCell<CpxDatabase> call(ListView<CpxDatabase> param) {
                return new DatabaseListCell();
            }
        });
        //textfields for selection
        //server might not be needed
        server.setTitle(Lang.getServer());
        server.setText(CpxClientConfig.instance().getServerSocket());

        //general layout
        VBox wrapper;
        if(role == null){
            wrapper = new VBox(server, databases);

            wrapper.setAlignment(Pos.CENTER_LEFT);
            wrapper.setSpacing(10.0d);
            setContent(wrapper);
        }else{
            MessageNode node = new MessageNode();
            node.setMessageText("Für die von Ihnen ausgewählte Rolle sind folgende Datenbanken freigegeben.\nWenn sie die Auswahl der Datenbank mit \"OK\" bestätigen, werden wie Rolle\n als auch die Datenbank gewechselt");

            wrapper = new VBox(node, server, databases);
        }

        //bind ok button to validation
        disableButtons(false);
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
        //validator uses extractor specified in MainApp! no out of the box behavior
        validation.registerValidator(databases, new Validator<Object>() {
            @Override
            public ValidationResult apply(Control t, Object u) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, Lang.getMenuDatabaseChangeValidationDbSelection(), databases.getSelectedItem() == null);
                if (databases.getSelectedItem() != null) {
                    res.addErrorIf(t, Lang.getMenuDatabaseChangeValidationDbSame(), databases.getSelectedItem().getConnectionString().equals(initialDatabaseValue));
                }
                return res;
            }
        });
    }

    /*
    * chanage db methodes
     */
    private void changeTo(CpxDatabase pDatabase) {
        if(role != null && changeToRoleCallback != null){
            changeToRoleCallback.call(role);
        }

        EjbProxy<LockService> lockServiceBean = Session.instance().getEjbConnector().connectLockServiceBean();
        lockServiceBean.get().removeAllForUser(Session.instance().getCpxUserId());
        MainApp.getToolbarMenuScene().cleanUp();
        startChangeDbTask(pDatabase);
        
    }


    private void startChangeDbTask(CpxDatabase pDatabase) {
        ChangeDbTask task = new ChangeDbTask(pDatabase.getConnectionString());
        task.setOnRunning(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                Pane loading = getLoadingContent();
                setContent(loading);
                lblstatus.setText(Lang.getPleaseWait());
//                initialDatabaseValue = databases.getSelectedItem().getConnectionString();
            }
        });
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                successProperty.set(true);
                MainApp.getToolbarMenuScene().restoreRecentClientScene();
                LoginFXMLController.initBroadcastHandler();
                ChangeDatabaseDialog.this.close();
                if(refreshToolBarCallback != null){
                    refreshToolBarCallback.call(null);
                }
                //Session.instance().getCaseCount(true);
                //Session.instance().getProcessCount(true);

//                BasicMainApp.getToolbarMenuScene().getController().showWorkingList();
            }
        });
        task.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                LOG.severe("Can not change database, reason " + task.getException().getMessage());
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

    void setChangeToRole(Callback<CdbUserRoles, Void> callback) {
        changeToRoleCallback = callback;
    }

    void setRefreshToolbar(Callback<Void, Void> pRefreshToolBarCallback) {
        refreshToolBarCallback = pRefreshToolBarCallback;
    }

}
