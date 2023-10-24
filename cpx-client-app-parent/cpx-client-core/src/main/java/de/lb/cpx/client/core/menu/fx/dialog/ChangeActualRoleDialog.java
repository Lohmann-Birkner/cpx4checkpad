/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.menu.fx.dialog;

import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.alert.ConfirmDialog;
import de.lb.cpx.client.core.model.fx.button.ReloadButton;
import de.lb.cpx.client.core.model.fx.dialog.TitledDialog;
import de.lb.cpx.client.core.model.fx.labeled.LabeledLabel;
import de.lb.cpx.client.core.model.fx.labeled.LabeledListView;
import de.lb.cpx.client.core.model.fx.listview.AsyncListView;
import de.lb.cpx.client.core.model.task.ChangeActualRoleTask;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.server.commonDB.model.CdbUser2Role;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.service.ejb.AuthServiceEJBRemote;
import de.lb.cpx.service.properties.RoleProperties;
import de.lb.cpx.shared.lang.Lang;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import javafx.util.Callback;
import javax.ejb.AsyncResult;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

/**
 * Dialog for changing the User Actual Role
 *
 * @author shahib
 */
public class ChangeActualRoleDialog extends TitledDialog {

    private static final Logger LOG = Logger.getLogger(ChangeActualRoleDialog.class.getName());
    private static final String PROGRESS_INDICATOR_STYLE = "async-progress-indicator";
    private static final String LIST_VIEW_STYLE = "stay-selected-list-view";
    private final ValidationSupport validation = new ValidationSupport();
    private final LabeledLabel server = new LabeledLabel();
    private final Label lblstatus = new Label();
    private final LabeledLabel user = new LabeledLabel();
    private final LabeledLabel actualRole = new LabeledLabel();
    private final ReadOnlyBooleanWrapper successProperty = new ReadOnlyBooleanWrapper(false);
    private Callback<Void, Void> refreshToolBarCallback ;
    private EjbProxy<AuthServiceEJBRemote> authService;
    //set up listview with async list
    private final LabeledListView<CdbUser2Role> roles = new LabeledListView<>(new AsyncListView<CdbUser2Role>() {
        @Override
        public Future<List<CdbUser2Role>> getFuture() {
            authService = Session.instance().getEjbConnector().connectAuthServiceBean();
            List<CdbUser2Role> result = authService.get().getUser2RoleByUserId(Session.instance().getCpxUserId());
            return new AsyncResult<>(result);
        }

        @Override
        public void afterTask(Worker.State pState) {
            super.afterTask(pState);
            //after loading on succeeded select role
            if (pState.equals(Worker.State.SUCCEEDED)) {
                selectActualRole();
            }
        }
    });

    /**
     * construct new instance
     *
     * @param pTitle title of the dialog
     * @param pOwner owner of the dialog
     */
    public ChangeActualRoleDialog(String pTitle, Window pOwner) {
        super(pTitle, pOwner, true);
        isRoleChanged(false);
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
            event.consume();
            check4ChangeRole();
            //start change role
        });
        setContent(getSelectionContent());
        roles.getListView().setOnMouseClicked(new EventHandler<MouseEvent>() {
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
    public CdbUser2Role getSelectedItem() {
        return roles.getSelectedItem();
    }

    /**
     * @return indicator if changing of actuall role was successful
     */
    public Boolean isSuccess() {
        return successProperty.get();
    }

    /**
     * @return readonly indicator property if chagne actuall role was successful
     * - for bindings
     */
    public ReadOnlyBooleanProperty successProperty() {
        return successProperty.getReadOnlyProperty();
    }
    public void reloadListView(){
        if(roles.getListView() instanceof AsyncListView){
            ((AsyncListView)roles.getListView()).reload();
        }
    }
    /*
    * UI
     */
    private Pane getSelectionContent() {
        //setup list view with pref Sizes and style class
        roles.setTitle("Verfügbare Rollen");
        roles.addStyleClassToTitle("cpx-main-label");
        roles.getSkin();
        ReloadButton reloadButton = new ReloadButton();
        reloadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                reloadListView();
            }
        });
        roles.getMenuItems().add(reloadButton);
        roles.setPrefSize(300.0d, 300.0d);
        roles.getControl().getStyleClass().add(LIST_VIEW_STYLE);
        roles.setCellFactory(new Callback<ListView<CdbUser2Role>, ListCell<CdbUser2Role>>() {
            @Override
            public ListCell<CdbUser2Role> call(ListView<CdbUser2Role> param) {
                return new RoleListCell();
            }
        });
        server.setTitle(Lang.getServer());
        server.setText(CpxClientConfig.instance().getServerSocket());
        server.addStyleClassToTitle("cpx-detail-label");
        user.setTitle(Lang.getLoginUser());
        user.addStyleClassToTitle("cpx-detail-label");

        user.setText(Session.instance().getCpxUserName());
        actualRole.setTitle("Aktive Rolle");
        actualRole.addStyleClassToTitle("cpx-detail-label");
        String roleName = Session.instance().getEjbConnector().connectLoginServiceBean().get().getActualRoleName();
        actualRole.setText(roleName);
        roles.addStyleClassToTitle("cpx-detail-label");
        VBox wrapper = new VBox(server, user, actualRole, roles);
        wrapper.setAlignment(Pos.CENTER_LEFT);
        wrapper.setSpacing(10.0d);
        setContent(wrapper);
        disableButtons(false);
        Button btn = getDialogSkin().getButton(ButtonType.OK);
        btn.disableProperty().bind(validation.invalidProperty());
        ((AsyncListView) roles.getControl()).reload();

//        LoginServiceEJBRemote service = Session.instance().getEjbConnector().connectLoginServiceBean().get();//        final LoginServiceEJBRemote loginService = service;
//        rolles.getSelectedItemProperty().addListener(new ChangeListener<CdbUser2Role>() {
//            @Override
//            public void changed(ObservableValue<? extends CdbUser2Role> ov, CdbUser2Role t, CdbUser2Role t1) {
//                roleDetails.getItems().clear();
//                if (loginService != null) {
//                    final RoleProperties props = authService.get().getUserRoleProperties(getSelectedItem().getCdbUserRoles().id);
//                    roleDetails.getItems().clear();
//                    if (props != null) {
//                        List<PropertyEntry<?>> entries = FXCollections.observableArrayList(props.getEntries().getLeafEntries());
//                        roleDetails.getItems().addAll(entries);
//                    }
//                }
//            }
//        });
        return wrapper;
    }

    /*
    private Pane getRoleDetails(final CpxProperties pProps) {
        Pane pane = new Pane();

        final Set<PropertyEntry<?>> entries = pProps.getLeafEntries();

        final TableColumn<PropertyEntry<?>, PropertyEntry<?>> propertyName = new TableColumn<>("Eigenschaft");
        //propertyName.setPrefWidth(1f * Integer.MAX_VALUE * 80); // 80% width
        propertyName.setPrefWidth(Control.USE_COMPUTED_SIZE);
        //propertyName.prefWidthProperty().bind(Bindings.multiply(roleDetails.widthProperty(), (2 / 3d)));
        propertyName.setCellValueFactory(new Callback() {
            @Override
            public Object call(Object param) {
                return new SimpleObjectProperty<>(((TableColumn.CellDataFeatures) param).getValue());
            }
        });
        //propertyName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String, PropertyEntry<?>>, ObservableValue<PropertyEntry<?>>>());
//        
        propertyName.setCellFactory(new Callback<TableColumn<PropertyEntry<?>, PropertyEntry<?>>, TableCell<PropertyEntry<?>, PropertyEntry<?>>>() {
            @Override
            public TableCell<PropertyEntry<?>, PropertyEntry<?>> call(TableColumn<PropertyEntry<?>, PropertyEntry<?>> p) {
                TableCell<PropertyEntry<?>, PropertyEntry<?>> cell = new TableCell<>() {
                    @Override
                    protected void updateItem(PropertyEntry<?> item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            return;
                        }
                        Label label = new Label(item.getPath());
                        setGraphic(label);
                    }
                };
                return cell;
            }
        });

        final TableColumn<PropertyEntry<?>, PropertyEntry<?>> propertyValue = new TableColumn<>("Wert");
        //propertyValue.prefWidthProperty().bind(Bindings.multiply(roleDetails.widthProperty(), (1 / 3d)));
        //propertyValue.setPrefWidth(1f * Integer.MAX_VALUE * 20); // 20% width
        propertyValue.setPrefWidth(Control.USE_COMPUTED_SIZE);
        propertyValue.setCellValueFactory(new Callback() {
            @Override
            public Object call(Object param) {
                return new SimpleObjectProperty<>(((TableColumn.CellDataFeatures) param).getValue());
            }
        });

        propertyValue.setCellFactory(new Callback<TableColumn<PropertyEntry<?>, PropertyEntry<?>>, TableCell<PropertyEntry<?>, PropertyEntry<?>>>() {
            @Override
            public TableCell<PropertyEntry<?>, PropertyEntry<?>> call(TableColumn<PropertyEntry<?>, PropertyEntry<?>> p) {
                TableCell<PropertyEntry<?>, PropertyEntry<?>> cell = new TableCell<>() {
                    @Override
                    protected void updateItem(PropertyEntry<?> item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            return;
                        }
                        Label label;
                        if (item.getValue() == null) {
                            label = new Label();
                        } else if (item.isPrimitiveBoolean()) {
                            boolean value = (boolean) item.getValue();
                            label = new Label(value ? Lang.getConfirmationYes() : Lang.getConfirmationNo());
                        } else if (item.isBoolean()) {
                            Boolean value = (Boolean) item.getValue();
                            label = new Label(value ? Lang.getConfirmationYes() : Lang.getConfirmationNo());
                        } else {
                            label = new Label(item.getValueAsString());
                        }
                        setGraphic(label);
                    }
                };
                return cell;
            }
        });

        final TableView<PropertyEntry<?>> tableView = new TableView<>();
//        roleDetails.getColumns().addAll(propertyName, propertyValue);
        tableView.getStyleClass().add("stay-selected-table-view");
        tableView.getColumns().addAll(propertyName, propertyValue);
        //tableView.setPrefHeight(150d);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getItems().setAll(entries);
        //tableView.setPrefWidth(Region.USE_COMPUTED_SIZE);
        //tableView.setPrefWidth(250d);
//            AutoFitPopOver popOver = new AutoFitPopOver(new VBox(new Label(name.getText()), tv));
//            popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
//            popOver.show(name);

        Label label = new Label(pProps.getName());
        VBox box = new VBox(label, tableView);
        box.setPadding(new Insets(10d));
        VBox.getVgrow(tableView);
        pane.getChildren().add(box);
        return pane;
    }
     */
    private Pane getLoadingContent() {
        ProgressIndicator progInd = new ProgressIndicator(-1);
        progInd.getStyleClass().add(PROGRESS_INDICATOR_STYLE);
        VBox wrapper = new VBox(progInd, lblstatus);
        wrapper.setAlignment(Pos.CENTER);
        disableButtons(true);
        return wrapper;
    }

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
        validation.registerValidator(roles, new Validator<Object>() {
            @Override
            public ValidationResult apply(Control t, Object u) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, Lang.getMenuRoleChangeValidationSelection(), roles.getSelectedItem() == null);
               if (roles.getSelectedItem() != null) {
                    res.addErrorIf(t, Lang.getMenuDatabaseChangeValidationDbSame(), roles.getSelectedItem().getCdbUserRoles().getCdburName().equals(actualRole.getText()));
                }
                return res;
            }
        });
    }

    /*
    * chanage  methodes
     */
    private void changeTo(CdbUserRoles cdbUserRoles) {
        // check whether the active db is for the new role allowed


        startChangeActualRoleTask(cdbUserRoles);
        Session.instance().setCpxActualRoleId(cdbUserRoles.getId());
        Session.instance().getEjbConnector().connectLoginServiceBean().get().setActualRole(cdbUserRoles.getId());
        isRoleChanged(true);
    }
    
    private BooleanProperty roleChangedProperty;
    
    public BooleanProperty roleChangedProperty(){
        if(roleChangedProperty == null){
            roleChangedProperty = new SimpleBooleanProperty();
        }
        return roleChangedProperty;
    }
    
    public boolean isRoleChanged(){
       return roleChangedProperty().get();
    }
    
    public void isRoleChanged(boolean b){
        roleChangedProperty().set(b);
    }

    private void startChangeActualRoleTask(CdbUserRoles pUserRole) {
        ChangeActualRoleTask task = new ChangeActualRoleTask(pUserRole.getId());
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
                //MainApp.getToolbarMenuScene().restoreRecentClientScene();
                ChangeActualRoleDialog.this.close();
            }
        });
        task.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                LOG.severe("Can not change User Aktive Rolle, reason " + task.getException().getMessage());
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

    private void check4ChangeRole() {
// check whether the active db is allowed for the new role
// yes: show confirm dialog and change role; no: show allowed dbs for this role with explanation message
    CdbUserRoles newRole = getSelectedItem().getCdbUserRoles();
    if(newRole.getCdburName().equals(actualRole.getText())){
        return;
    }
    final RoleProperties props = authService.get().getUserRoleProperties(newRole.getId());
     Window window = getDialogPane().getScene().getWindow();
        if(props.isDatabaseAllowed(Session.instance().getCpxDatabase())){
               
                ConfirmDialog confirm = new ConfirmDialog(window, "Wollen Sie mit der Rolle " + getSelectedItem().getCdbUserRoles().getCdburName() + " fortfahren?\n"
                //                  +  getRoleDescription(getSelectedItem().getCdbUserRoles())
                );
                confirm.showAndWait().ifPresent(new Consumer<ButtonType>() {
                    @Override
                    public void accept(ButtonType t) {
                        if (t.equals(ButtonType.YES)) {
    //                                    openItem(newValue.getProcessHospital().getId(), ListType.WORKFLOW_LIST);
                            changeTo(newRole);
                            ChangeActualRoleDialog.this.close();


                        }
                    }
                });
        }else{
            ChangeDatabaseDialog dialog = new ChangeDatabaseDialog("Datenbank für die Rolle wechseln...", window, newRole);
            dialog.setChangeToRole(new Callback<CdbUserRoles, Void>(){
                public Void call(CdbUserRoles role){
                    Session.instance().setCpxActualRoleId(role.getId());
                    Session.instance().getEjbConnector().connectLoginServiceBean().get().setActualRole(role.getId());
                    isRoleChanged(true);
                    return null;
                }
            });
            dialog.setRefreshToolbar(refreshToolBarCallback);
            
             dialog.setOnCloseRequest(new EventHandler<DialogEvent>() {
                @Override
                public void handle(DialogEvent event) {
                    if (!dialog.isSuccess()) {
                        return;
                    }else{
                        ChangeActualRoleDialog.this.close();
                    }
                };
             });
            dialog.showAndWait();

//                    @Override
//                    public void accept(ButtonType t) {
//                        if (t.equals(ButtonType.YES)) {
//    //                                    openItem(newValue.getProcessHospital().getId(), ListType.WORKFLOW_LIST);
//                            changeTo(newRole);
//                            dialog.startChangeDbTask();
//                            if(dialog.isSuccess() && refreshToolBarCallback != null){
//                                refreshToolBarCallback.call(null);
//                                ChangeActualRoleDialog.this.close();
//                            }
//                        }
//                    }
//                });

        }
    }

    public void setRefreshToolbarCallback(Callback<Void, Void> callback) {
        refreshToolBarCallback = callback;
    }

    private void selectActualRole() {
         ObservableList<CdbUser2Role> list = roles.getItems();
         long roleId =  Session.instance().getEjbConnector().connectLoginServiceBean().get().getActualRoleId();
         for(CdbUser2Role u2r: list){
             if(u2r.getCdbUserRoles().getId() == roleId){
                 roles.select(u2r);
                 return;
             }
         }
    }

//    private String getRoleDescription(CdbUserRoles cdbUserRoles) {
//
//        return cdbUserRoles.getCdburName()
//                + cdbUserRoles.getCdburValidFrom() != null ? Lang.toDate(cdbUserRoles.getCdburValidFrom()) : ""
//                + cdbUserRoles.getCdburValidFrom() != null ? Lang.toDate(cdbUserRoles.getCdburValidTo()) : "";
//
//    }
//    private PopOver popOver;
    class RoleListCell extends ListCell<CdbUser2Role> {

        private final Label name = new Label();
        private final HBox cell;

        public RoleListCell() {
            super();
            cell = new HBox();
            name.setAlignment(Pos.CENTER_LEFT);
            cell.getChildren().add(name);
        }

        @Override
        protected void updateItem(CdbUser2Role item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setGraphic(null);
            } else {
                name.setText(item.getCdbUserRoles().getCdburName());
//                name.setTooltip(new Tooltip(getRoleDescription(item.getCdbUserRoles())));
                setGraphic(cell);

//                final RoleProperties props = authService.get().getUserRoleProperties(item.getCdbUserRoles().id);
//                if (props != null) {
//                    cell.setOnMouseClicked((t) -> {
//                        if (popOver != null) {
//                            popOver.hide();
//                        }
//                        rolles.select(item);
//                        popOver = new PopOver(getRoleDetails(props));
//                        //PopOver popOver = new PopOver(getRoleDetails(props));
//                        //popOver.setFadeInDuration(Duration.ZERO);
//                        //popOver.setFadeOutDuration(Duration.ZERO);
//                        //popOver.hide(Duration.ZERO);
//                        //popOver.setContentNode(getRoleDetails(props));
//                        popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
//                        //Bounds boundsInScene = cell.localToScene(cell.getBoundsInLocal());
//                        Bounds bounds = cell.localToScreen(cell.getBoundsInLocal());
//                        Bounds bounds2 = rolles.localToScreen(rolles.getBoundsInLocal());
//                        double offset = 0d;
//                        popOver.show(rolles, bounds2.getMinX() + bounds2.getWidth() + offset, bounds.getMinY() + bounds.getHeight() / 2);
//                    });
//                }
            }
        }

    }
}
