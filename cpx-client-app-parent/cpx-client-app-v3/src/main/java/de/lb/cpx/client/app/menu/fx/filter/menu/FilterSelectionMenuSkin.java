/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.menu.fx.filter.menu;

import de.FileUtils;
import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.menu.dialog.ColumnSelectDialog;
import de.lb.cpx.client.app.menu.dialog.ColumnSelectFXMLController;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.alert.SendMail;
import de.lb.cpx.client.core.model.fx.button.CopyButton;
import de.lb.cpx.client.core.model.fx.checked_combobox.CpxCheckComboBox;
import de.lb.cpx.client.core.model.fx.dialog.CpxTextInputDialog;
import de.lb.cpx.client.core.model.fx.file_chooser.FileChooserFactory;
import de.lb.cpx.client.core.model.fx.listview.AsyncListView;
import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.service.searchlist.SearchListResult;
import de.lb.cpx.shared.dto.UserDTO;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.system.properties.CpxSystemProperties;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.skin.ButtonSkin;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.ejb.AsyncResult;
import javax.mail.MessagingException;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.Validator;

/**
 * Skin fot the selection Menu styles button as combobox
 *
 * @author wilde
 * @param <E> type of the filter to be shown in the menu
 *
 * TODO: Update hardcoded string with values from lang-file
 */
public class FilterSelectionMenuSkin<E extends SearchListResult> extends ButtonSkin {

    private static final Logger LOG = Logger.getLogger(FilterSelectionMenuSkin.class.getName());
    //popover reference to not redraw every time
    private FilterSelectionPopOver popOver;

    public FilterSelectionMenuSkin(FilterSelectionMenu<E> pSkinable) {
        super(pSkinable);
        //show popover on action
        popOver = new FilterSelectionPopOver();
        pSkinable.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                if (popOver == null) {
//                    popOver = new FilterSelectionPopOver();
//                }
                if (!popOver.isShowing()) {
                    //2018-08-17 DNi CPX-1053: Get a brandly new list of filters from server each time popup opens
                    //2019-02-07 AWi: Load whole popover anew, otherwise with setAll, selection gets lost reselect value, will cause new loading of tableviews!
//                    popOver.reload();
                    popOver.show(getNode());
                } else {
                    popOver.hide();
                }
            }
        });
//        getFilterSelectionMenu().textProperty().bind(Bindings.);
    }

    /**
     * Helper-Mthode, to avoid casting the skinnable every time from button to
     * Filterselection
     *
     * @return current skinnable as FilterSelectionMenu
     */
    @SuppressWarnings("unchecked")
    private FilterSelectionMenu<E> getFilterSelectionMenu() {
        return ((FilterSelectionMenu) getSkinnable());
    }

    public final void loadData() {
//        popOver.listView.reload();
    }
    protected class FilterSelectionPopOver extends PopOver {

        private AsyncListView<E> listView;
        public FilterSelectionPopOver() {
            super();
            setAutoFix(true);
            setArrowLocation(ArrowLocation.TOP_CENTER);
//            setDefaultArrowLocation(ArrowLocation.TOP_CENTER);
//            setFitOrientation(Orientation.HORIZONTAL);
        }

        @Override
        protected void show() {
//            listView.getSelectionModel().select(getFilterSelectionMenu().getSelectedItem());
            setUpContent();
            super.show();
        }

        //creates content 
        //with listview of items and filter creation button
        private VBox createContent() {
            Button btnCreateFilter = getCreateFilterButton();
            btnCreateFilter.fontProperty().bind(getSkinnable().fontProperty());
            HBox createContainer = new HBox(btnCreateFilter);
            Tooltip.install(createContainer, new Tooltip(getFilterSelectionMenu().isDisableFilterCreation() ? "Diese Funktion ist deaktiviert" : "Neuen Filter mit den Standardspalten erstellen"));
            listView = getFilterListView();
            listView.reload();
            VBox root = new VBox(listView, btnCreateFilter);
            root.setPadding(new Insets(5));
            root.setFillWidth(true);
            return root;
        }
        private void addItemAndScrollTo(E pItem) {
            if (pItem != null) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        listView.getItems().add(pItem);
                        listView.getItems().sort(new Comparator<E>() {
                            @Override
                            public int compare(E o1, E o2) {
                                return o1.getName().compareTo(o2.getName());
                            }
                        });
                        listView.getSelectionModel().select(pItem);
                        listView.scrollTo(pItem);
                    }
                });
//                                getFilterSelectionMenu().setActive(item);
            }
        }
        //creates filter creaion button
        private Button getCreateFilterButton() {
            Button btn = new Button("Neuen Filter hinzufügen", ResourceLoader.getGlyph(FontAwesome.Glyph.PLUS));
            btn.disableProperty().bind(getFilterSelectionMenu().disableFilterCreationProperty());
            btn.setAlignment(Pos.CENTER_LEFT);
            btn.getStyleClass().add("cpx-icon-button");
            btn.setMaxWidth(Double.MAX_VALUE);
            //start creation dialog
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
//                    popOver.getScene().getWindow();
                    CpxTextInputDialog dialog = new CpxTextInputDialog("Neuen Filter erstellen", "Bitte Geben Sie dem Filter einen Namen", getScene().getWindow());//MainApp.getStage());
                    dialog.registerValidator(new Validator<String>() {
                        @Override
                        public ValidationResult apply(Control t, String u) {
                            ValidationResult res = new ValidationResult();
                            res.addErrorIf(t, "Dieser Filtername existiert bereits!", u.trim().isEmpty() || getFilterSelectionMenu().isExisting(u));
                            return res;
                        }
                    });
//prevent popover from hiding when
//Creation Dialog is shown
                    disableStage(true);
                    dialog.showAndWait().ifPresent(new Consumer<String>() {
                        @Override
                        public void accept(String t) {
                            E item = getFilterSelectionMenu().getOnCreateCallback().call(t);
                            addItemAndScrollTo(item);

                        }
                    });
                    disableStage(false);
                }
            });
            return btn;
        }

        /**
         * methode to disable stage if dialog is shown blures/unblures stage and
         * disable/enables autohide to force popover to stay shown even focus is
         * moved
         *
         * @param pDisable
         */
        private void disableStage(boolean pDisable) {
            if (pDisable) {
//                blurStage(MainApp.getStage(), true);
                setAutoHide(false);
            } else {
                setAutoHide(true);
//                blurStage(MainApp.getStage(), false);
            }
        }

        //creates list view
        private AsyncListView<E> getFilterListView() {
//            if (listView == null) {
            AsyncListView<E> listview = new AsyncListView<E>(false) {
                @Override
                public Future<List<E>> getFuture() {
                    return new AsyncResult<>(getFilterSelectionMenu().getOnLoadCallback().call(null));
                }

                @Override
                public void afterTask(Worker.State pState) {
                    super.afterTask(pState); //To change body of generated methods, choose Tools | Templates.
                    //after loading was sccessful
                    //selection listener is set and the active item is selected in list view
                    //NOTE: this is done after the popover is shown initially
                    if (Worker.State.SUCCEEDED.equals(pState)) {
//                        getFilterSelectionMenu().setItems(listView.getItems());
//                        getFilterSelectionMenu().selectedItemProperty().bind(listView.getSelectionModel().selectedItemProperty());

                        E active = getFilterSelectionMenu().getActive();
                        getFilterSelectionMenu().setSelectedItem(active);
                        getSelectionModel().select(active);
                        LOG.log(Level.INFO, "get active filter {0}", active ==null?"is null":active.getName());
                        getFilterSelectionMenu().setText("Filter: " + (active == null ? "" : active.getName()));
//                        getFilterSelectionMenu().getItems().addAll(getItems());
                        Bindings.bindContentBidirectional(getFilterSelectionMenu().getItems(), getItems());
//                        getFilterSelectionMenu().selectedItemProperty().addListener(new ChangeListener<E>() {
//                            @Override
//                            public void changed(ObservableValue<? extends E> observable, E oldValue, E newValue) {
//                                LOG.info("selectedItem " + (newValue != null ? newValue.getName() : "is null"));
//                                getSelectionModel().select(newValue);
//                            }
//                        });
                        getSelectionModel().selectedItemProperty().addListener(new ChangeListener<E>() {
                            @Override
                            public void changed(ObservableValue<? extends E> observable, E oldValue, E newValue) {
                                if (newValue == null) {
                                    return;
                                }
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        getFilterSelectionMenu().getOnChangeCallback().call(newValue);
                                        //save old value if oldvalue was not deleted
                                        //otherwise after deleting selected item it will be created anew
                                        getFilterSelectionMenu().setActive(newValue);
                                        getFilterSelectionMenu().setSelectedItem(newValue);
                                        getFilterSelectionMenu().setText("Filter: " + newValue.getName());
                                    }
                                });
                                getFilterSelectionMenu().setText("Filter: " + newValue.getName());
                            }
                        });
                    }
                }

            };
//style clas and set list cell
            listview.getStyleClass().add("stay-selected-list-view");
            listview.setCellFactory(new Callback<ListView<E>, ListCell<E>>() {
                @Override
                public ListCell<E> call(ListView<E> param) {
                    return new PopOverListCell();
                }
            });
//            }
            return listview;
        }

//        private void reload() {
//            setUpContent();
//        }
        private void setUpContent() {
            setContentNode(createContent());
            //bind sizes with definition in skinnable
            ((Region) getContentNode()).setMaxHeight(getFilterSelectionMenu().getPopOverHeight());
            ((Region) getContentNode()).setMinWidth(getFilterSelectionMenu().getPopOverWidth());
        }

        //creates button to share filter to another person or group
        private Button createShareButton(E pItem) {
            Button btn = new Button();
            if (pItem.isShared()) {
                btn.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.SHARE_ALT_SQUARE));
            } else {
                btn.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.SHARE_ALT));
            }
            btn.getStyleClass().add("cpx-icon-button");
            btn.disableProperty().bind(getFilterSelectionMenu().disableFilterEditProperty());
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    //disableStage(true);
//                    blurStage(MainApp.getStage(), true);
                    //final SearchList searchList = CpxClientConfig.instance().getSearchList(pItem);
                    SearchListResult searchList = MenuCache.getMenuCacheSearchLists().get(pItem.getId());
                    if (searchList == null) {
                        AlertDialog alert = AlertDialog.createErrorDialog("Der Filter '" + pItem.getName() + "' existiert nicht mehr",
                                /*MainApp.getWindow()*/ getScene().getWindow());
                        alert.show();
                        disableStage(false);
                        return;
                    }
                    Set<Map.Entry<Long, UserDTO>> userNames = MenuCache.instance().getUserMapEntries();
                    Set<Map.Entry<Long, CdbUserRoles>> roleNames = MenuCache.instance().getRoleMapEntries();

                    CpxCheckComboBox<Map.Entry<Long, UserDTO>> comboUsers = new CpxCheckComboBox<>();
                    comboUsers.getItems().addAll(userNames);

                    CpxCheckComboBox<Map.Entry<Long, CdbUserRoles>> comboRoles = new CpxCheckComboBox<>();
                    comboRoles.getItems().addAll(roleNames);

                    for (final long userId : searchList.getVisibleToUserIds()) {
                        Map.Entry<Long, UserDTO> userName = null;
                        for (Map.Entry<Long, UserDTO> u : userNames) {
                            if (u.getKey() == userId) {
                                userName = u;
                                break;
                            }
                        }
                        comboUsers.getCheckModel().check(userName);
                    }

                    for (final long roleId : searchList.getVisibleToRoleIds()) {
                        Map.Entry<Long, CdbUserRoles> roleName = null;
                        for (Map.Entry<Long, CdbUserRoles> r : roleNames) {
                            if (r.getKey() == roleId) {
                                roleName = r;
                                break;
                            }
                        }
                        comboRoles.getCheckModel().check(roleName);
                    }

                    comboUsers.setConverter(new StringConverter<Map.Entry<Long, UserDTO>>() {
                        @Override
                        public String toString(Map.Entry<Long, UserDTO> element) {
                            return element == null ? "" : element.getValue().getUserName();
                        }

                        @Override
                        public Map.Entry<Long, UserDTO> fromString(String string) {
                            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        }
                    });

                    comboRoles.setConverter(new StringConverter<Map.Entry<Long, CdbUserRoles>>() {
                        @Override
                        public String toString(Map.Entry<Long, CdbUserRoles> element) {
                            return element == null ? "" : element.getValue().getCdburName();
                        }

                        @Override
                        public Map.Entry<Long, CdbUserRoles> fromString(String string) {
                            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        }
                    });

                    AlertDialog dialog = AlertDialog.createConfirmationDialog(null, null, /*MainApp.getWindow()*/ getScene().getWindow());
                    //final Alert dialog = new Alert(Alert.AlertType.NONE);
                    dialog.setGraphic(null);
                    dialog.setAlertType(Alert.AlertType.NONE);
                    dialog.setHeaderText("Filter teilen mit...");
                    dialog.getButtonTypes().clear();
                    dialog.getButtonTypes().addAll(ButtonType.OK, ButtonType.CLOSE);
                    //dialog.initOwner(popOver);
                    //PopOver visibleToPopOver = new PopOver();
                    //visibleToPopOver.setDetachable(false);
                    //visibleToPopOver.setArrowLocation(PopOver.ArrowLocation.RIGHT_CENTER);
                    //visibleToPopOver.setAutoHide(false);
                    //VBox box = new VBox();
                    //box.setSpacing(5d);
                    //box.getChildren().add(comboUsers);
                    //box.getChildren().add(comboRoles);
                    //visibleToPopOver.setContentNode(box);
                    //visibleToPopOver.setTitle("Sichtbar für folgende Anwender und/oder Rollen");

                    GridPane gpInfos = new GridPane();
                    gpInfos.setPadding(new Insets(10, 10, 10, 10));
                    gpInfos.setVgap(10.0);
                    gpInfos.setHgap(10.0);
                    gpInfos.setPrefHeight(GridPane.USE_COMPUTED_SIZE);
                    Label userNameText = new Label("Anwendern");
                    Label roleNameText = new Label("Rollenn");
                    //Label title = new Label("Sichtbar für folgende Anwender und/oder Rollen");

                    //gpInfos.add(title, 0, 0);
                    //GridPane.setColumnSpan(title, 2);
                    gpInfos.add(userNameText, 0, 1);
                    gpInfos.add(comboUsers, 1, 1);
                    gpInfos.add(roleNameText, 0, 2);
                    gpInfos.add(comboRoles, 1, 2);

                    //visibleToPopOver.setContentNode(gpInfos);
                    dialog.getDialogPane().setContent(gpInfos);

                    comboUsers.setMinWidth(300d);
                    comboUsers.setMaxWidth(300d);
                    comboRoles.minWidthProperty().bind(comboUsers.minWidthProperty());
                    comboRoles.maxWidthProperty().bind(comboUsers.maxWidthProperty());

                    final ListChangeListener<Map.Entry<Long, UserDTO>> usersChangeListener = new ListChangeListener<>() {
                        @Override
                        public void onChanged(ListChangeListener.Change<? extends Map.Entry<Long, UserDTO>> c) {
                            userNameText.setText("Anwendern (" + comboUsers.getCheckModel().getCheckedItems().size() + ")");
                        }
                    };

                    final ListChangeListener<Map.Entry<Long, CdbUserRoles>> rolesChangeListener = new ListChangeListener<>() {
                        @Override
                        public void onChanged(ListChangeListener.Change<? extends Map.Entry<Long, CdbUserRoles>> c) {
                            roleNameText.setText("Rollen (" + comboRoles.getCheckModel().getCheckedItems().size() + ")");
                        }
                    };

                    comboUsers.getCheckModel().getCheckedItems().addListener(usersChangeListener);
                    comboRoles.getCheckModel().getCheckedItems().addListener(rolesChangeListener);

                    FilterSelectionPopOver.this.setAutoHide(false);
                    usersChangeListener.onChanged(null);
                    rolesChangeListener.onChanged(null);

                    Optional<ButtonType> result = dialog.showAndWait();

                    if (result.isPresent()) {
                        FilterSelectionPopOver.this.setAutoHide(true);
//                        blurStage(MainApp.getStage(), false);
                        if (result.get().equals(ButtonType.OK)) {
                            //LOG.log(Level.INFO, "setOnHidden");
                            List<Long> userIds = new ArrayList<>();
                            Iterator<Map.Entry<Long, UserDTO>> itUser = comboUsers.getCheckModel().getCheckedItems().iterator();
                            while (itUser.hasNext()) {
                                Map.Entry<Long, UserDTO> entry = itUser.next();
                                //LOG.log(Level.INFO, "USER-ID: " + entry.getKey());
                                userIds.add(entry.getKey());
                            }

                            List<Long> roleIds = new ArrayList<>();
                            Iterator<Map.Entry<Long, CdbUserRoles>> itRole = comboRoles.getCheckModel().getCheckedItems().iterator();
                            while (itRole.hasNext()) {
                                Map.Entry<Long, CdbUserRoles> entry = itRole.next();
                                //LOG.log(Level.INFO, "ROLE-ID: " + entry.getKey());
                                roleIds.add(entry.getKey());
                            }

                            searchList.setVisibleToUserIds(userIds);
                            searchList.setVisibleToRoleIds(roleIds);

                            //LOG.log(Level.INFO, "SELECTED USERS: " + Arrays.toString(searchList.getVisibleToUserIds()));
                            //LOG.log(Level.INFO, "SELECTED ROLES: " + Arrays.toString(searchList.getVisibleToRoleIds()));
                            E updated = getFilterSelectionMenu().getOnUpdateCallback().call(searchList);
                            if (updated == null) {
                                LOG.log(Level.WARNING, "updated search lists results dto is null!");
                                return;
                            }
                            //replace old item with new one, maybe use only index?
                            FilterSelectionPopOver.this.listView.getItems().set(listView.getItems().indexOf(pItem), updated);
                            //disableStage(false);
                        }
                    }
                }

                //disableStage(false);
            });
            return btn;
        }

        //set list cell for popover
        //shows name of filter 
        //and two buttons for editing filter and deleting filter
        //TODO: 
        private class PopOverListCell extends ListCell<E> {

            @Override
            protected void updateItem(E item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                    return;
                }
                Label lblName = new Label(item.getName());
                lblName.fontProperty().bind(getSkinnable().fontProperty());
                Glyph lockSymbol = ResourceLoader.getGlyph(FontAwesome.Glyph.LOCK);
                lockSymbol.setVisible(item.isReadonly());
                lblName.setGraphic(lockSymbol);
                if (item.isReadonly() && item.getCreationUser() != null) {
                    String fullUserName = MenuCache.instance().getUserFullNameForId(item.getCreationUser());
                    Tooltip.install(lblName, new Tooltip("erstellt von " + fullUserName));
                }
                HBox.setHgrow(lblName, Priority.ALWAYS);
                lblName.setMaxWidth(Double.MAX_VALUE);
                lblName.setMaxHeight(Double.MAX_VALUE);

                Button edit = createEditButton(item);
                HBox editContainer = new HBox(edit);
                Button copy = createCopyButton(item);
                HBox copyContainer = new HBox(copy);
//                Tooltip.install(editContainer, new CpxTooltip(getFilterSelectionMenu().isDisableFilterEdit()?"Diese Funktion ist deaktiviert":"Spalteneinstellungen bearbeiten"));
                Button delete = createDeleteButton(item);
                HBox deleteContainer = new HBox(delete);
//                Tooltip.install(deleteContainer, new CpxTooltip(getFilterSelectionMenu().isDisableFilterEdit()?"Diese Funktion ist deaktiviert":"Filter löschen"));
                Button download = createDownloadButton(item);
                HBox downloadContainer = new HBox(download);
                Tooltip.install(download, new CpxTooltip("Filter herunterladen"));
                Button mail = createMailButton(item);
                HBox mailContainer = new HBox(mail);
                Tooltip.install(mail, new CpxTooltip("Filter weiterleiten"));
                Button share = createShareButton(item);
                HBox shareContainer = new HBox(share);
                Tooltip.install(share, new CpxTooltip("Filter teilen" + (item.isShared() ? "\n(wird bereits mit " + item.getVisibleToUserIds().size() + " Anwendern und " + item.getVisibleToRoleIds().size() + " Rollen geteilt)" : "")));

                if (item.isReadonly()) {
                    share.setVisible(false);
                }

                HBox root = new HBox(lblName, editContainer, copyContainer, deleteContainer, downloadContainer, mailContainer, shareContainer);
                root.setFillHeight(true);
                root.setSpacing(5);
                root.setFillHeight(true);
                setGraphic(root);
            }

            //creates button to handle editing of existing filter
            private Button createEditButton(E pItem) {
                Button btn = new Button();
//                btn.setTooltip(new Tooltip("Spalteneinstellungen bearbeiten"));
                btn.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.PENCIL));
                btn.getStyleClass().add("cpx-icon-button");
                BooleanProperty readonly = new SimpleBooleanProperty(pItem.isReadonly());
                btn.disableProperty().bind(Bindings
                        .when(readonly.not())
                        .then(getFilterSelectionMenu().disableFilterEditProperty())
                        .otherwise(Boolean.TRUE)
                );
                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        disableStage(true);
                        ColumnSelectDialog column_select_dialog = new ColumnSelectDialog(
                                /*MainApp.getWindow()*/getScene().getWindow(), Modality.APPLICATION_MODAL, getFilterSelectionMenu().getListTyp().getTranslation().getValue() + " konfigurieren");
                        ColumnSelectFXMLController controller = (ColumnSelectFXMLController) column_select_dialog.getScene().getController();
                        //controller.setSearchList(pItem.getType(), CpxClientConfig.instance().getSearchList(pItem.getType(), pItem.getId()));
                        controller.setSearchList(pItem.getType(), MenuCache.getMenuCacheSearchLists().get(pItem.getId(), pItem.getType()));
                        
                        column_select_dialog.setIsFilterNameExistCallback(new Callback<String, Boolean>() {
                            @Override
                            public Boolean call(String param) {
                                return controller.isFilterNameChanged() &&  getFilterSelectionMenu().isExisting(controller.getSearchListName());
                            }
                        });
    

                        column_select_dialog.registerValidator();

//                            column_select_dialog.getDialogSkin().getButton(ButtonType.OK).setOnAction(new EventHandler<ActionEvent>(){
//                               
//                                @Override
//                                public void handle(ActionEvent event) {
//
//                                    controller.updateSearchListName();
//                                    SearchListResult filter = controller.getSearchList();
//                                    getFilterSelectionMenu().setText("Filter: " + (filter == null ? "" : filter.getName()));
//                                    E updated = getFilterSelectionMenu().getOnUpdateCallback().call(filter);
//                                    if (updated == null) {
//                                        LOG.log(Level.WARNING, "updated search lists results dto is null!");
//                                        return;
//                                    }
//                                    //replace old item with new one, maybe use only index?
//                                    getListView().getItems().set(getListView().getItems().indexOf(pItem), updated);
//                                    //CPX-1476
//                                    if (getListView().getSelectionModel().getSelectedItem() != null) {
//                                        if (getListView().getSelectionModel().getSelectedItem().equals(updated)) {
//                                            //trigger reload with updated values
//                                            getSkinnable().getProperties().put(com.sun.javafx.scene.control.Properties.REFRESH, null);
//                                        }
//                                    }
//                                    getListView().getSelectionModel().select(updated);
//                                    column_select_dialog.close();
//                                }
//                            });
//                            column_select_dialog.show();
                        column_select_dialog.showAndWait().ifPresent(new Consumer<ButtonType>() 
                            {

                            @Override
                            public void accept(ButtonType t) {
                                if (t.getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) {
                                    // check FilterName
                                    if(controller.isFilterNameChanged() &&  getFilterSelectionMenu().isExisting(controller.getSearchListName())
//                                            && getFilterSelectionMenu().getOnCheckFilterNameExistsCallback().call(controller.getSearchListName())
                                            ){
                                        // message to change the Filter Name
                                        MainApp.showWarningMessageDialog("Filter mit dem Namen " +  
                                                controller.getSearchListName() + 
                                                " ist schon vorhanden\n Bitte ändern Sie die Eingabe"
                                               );
                                        controller.selectSearchListName();

                                        return;
                                    }
                                    controller.updateSearchListName();
                                    SearchListResult filter = controller.getSearchList();
                                    getFilterSelectionMenu().setText("Filter: " + (filter == null ? "" : filter.getName()));
                                    E updated = getFilterSelectionMenu().getOnUpdateCallback().call(filter);
                                    if (updated == null) {
                                        LOG.log(Level.WARNING, "updated search lists results dto is null!");
                                        return;
                                    }
                                    //replace old item with new one, maybe use only index?
                                    getListView().getItems().set(getListView().getItems().indexOf(pItem), updated);
                                    //CPX-1476
                                    if (getListView().getSelectionModel().getSelectedItem() != null) {
                                        if (getListView().getSelectionModel().getSelectedItem().equals(updated)) {
                                            //trigger reload with updated values
                                            getSkinnable().getProperties().put(com.sun.javafx.scene.control.Properties.REFRESH, null);
                                        }
                                    }
                                    getListView().getSelectionModel().select(updated);
                                }
                            }
                        });
                        disableStage(false);
                    }
                });
                return btn;
            }
            
            private Button createCopyButton(E pItem) {
                Button btn = new CopyButton();
                //disable button when disabableEditProperty is true, or when list items size is less than 1
                //avoid possible issue when user deletes all filters for the list
                btn.disableProperty().bind(getFilterSelectionMenu().disableFilterEditProperty());
                btn.setTooltip(new Tooltip("Filter kopieren"));
//                BooleanProperty readonly = new SimpleBooleanProperty(pItem.isReadonly());
//                btn.disableProperty().bind(Bindings
//                        .when(Bindings.greaterThan(Bindings.size(listView.getItems()), 1)
//                                .and(readonly.not()))
//                        .then(getFilterSelectionMenu().disableFilterEditProperty())
//                        .otherwise(Boolean.TRUE)
//                );
                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        disableStage(true);
                        AlertDialog alert = AlertDialog.createConfirmationDialog("Sind Sie sicher das Sie den Filter '" + pItem.getName() + "' kopiere möchten?",
                                /*"",*/
                                /*MainApp.getWindow()*/ getScene().getWindow());
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            if(getFilterSelectionMenu().getOnCopyCallback()== null){
                                MainApp.showErrorMessageDialog("Copy can not be executed!");
                                disableStage(false);
                                return;
                            }
                            E copy = getFilterSelectionMenu().getOnCopyCallback().call(pItem);
                            addItemAndScrollTo(copy);
//                            listView.getItems().add(copy);
                        }
                        disableStage(false);
                    }
                });
                return btn;
            }
            
            //creates button to handle delting of existing button
            private Button createDeleteButton(E pItem) {
                Button btn = new Button();
                btn.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.TRASH));
                btn.getStyleClass().add("cpx-icon-button");
                //disable button when disabableEditProperty is true, or when list items size is less than 1
                //avoid possible issue when user deletes all filters for the list
                BooleanProperty readonly = new SimpleBooleanProperty(pItem.isReadonly());
                btn.disableProperty().bind(Bindings
                        .when(Bindings.greaterThan(Bindings.size(listView.getItems()), 1)
                                .and(readonly.not()))
                        .then(getFilterSelectionMenu().disableFilterEditProperty())
                        .otherwise(Boolean.TRUE)
                );
                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        disableStage(true);
                        AlertDialog alert = AlertDialog.createConfirmationDialog("Sind Sie sicher das Sie den Filter '" + pItem.getName() + "' löschen möchten?",
                                /*"",*/
                                /*MainApp.getWindow()*/ getScene().getWindow());
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            getFilterSelectionMenu().getOnDeleteCallback().call(pItem);
//                            if(pItem.equals(listView.getSelectionModel().getSelectedItem())){
//                                listView.getSelectionModel().select(null);
//                            }
                            listView.getItems().remove(pItem);
                        }
                        disableStage(false);
                    }
                });
                return btn;
            }

            //creates button to export/download filter
            private Button createDownloadButton(E pItem) {
                Button btn = new Button();
                btn.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.DOWNLOAD));
                btn.getStyleClass().add("cpx-icon-button");
                btn.disableProperty().bind(getFilterSelectionMenu().disableFilterEditProperty());
                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        disableStage(true);
                        final SearchListResult searchList = MenuCache.getMenuCacheSearchLists().get(pItem.getId());
//                        final SearchListResult searchList = CpxClientConfig.instance().getSearchList(pItem);
                        if (searchList == null) {
                            //search list was maybe deleted by another client meantime
                            AlertDialog alert = AlertDialog.createErrorDialog("Der Filter '" + pItem.getName() + "' existiert nicht mehr",
                                    /*MainApp.getWindow()*/ getScene().getWindow());
                            alert.show();
                            disableStage(false);
                            return;
                        }
                        List<String> supportedFileTypes = java.util.Arrays.asList(new String[]{"*.cpxf"});
                        FileChooser fileChooser = FileChooserFactory.instance().createFileChooser();
                        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("cpxf", supportedFileTypes));
                        fileChooser.setTitle("Filter herunterladen");
                        String defaultFileName = getFilterFileName(searchList);
                        fileChooser.setInitialFileName(defaultFileName);
                        File file = fileChooser.showSaveDialog(getScene().getWindow());
                        CpxClientConfig.instance().setUserRecentFileChooserPath(file);

                        if (file != null) {
                            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                                    new FileOutputStream(file), CpxSystemProperties.DEFAULT_ENCODING))) {
                                writer.write(searchList.serialize());
                            } catch (IOException ex) {
                                LOG.log(Level.SEVERE, "Was not able to serialize search list filter and to store it to " + file.getAbsolutePath(), ex);
                                AlertDialog alert = AlertDialog.createErrorDialog("Der Filter '" + pItem.getName() + "' konnte nicht gespeichert werden",
                                        ex,
                                        /*MainApp.getWindow()*/ getScene().getWindow());
                                alert.show();
                            }
                        }

                        disableStage(false);
                    }
                });
                return btn;
            }

            //creates button to mail/forward filter to another person or group
            private Button createMailButton(E pItem) {
                Button btn = new Button();
                btn.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ENVELOPE));
                btn.getStyleClass().add("cpx-icon-button");
                btn.disableProperty().bind(getFilterSelectionMenu().disableFilterEditProperty());
                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        disableStage(true);
                        //final SearchListResult searchList = CpxClientConfig.instance().getSearchList(pItem);
                        final SearchListResult searchList = MenuCache.getMenuCacheSearchLists().get(pItem.getId());
                        if (searchList == null) {
                            //search list was maybe deleted by another client meantime
                            AlertDialog alert = AlertDialog.createErrorDialog("Der Filter '" + pItem.getName() + "' existiert nicht mehr",
                                    /*MainApp.getWindow()*/ getScene().getWindow());
                            alert.show();
                            disableStage(false);
                            return;
                        }
                        String fileName = getFilterFileName(searchList);
                        File file = new File(System.getProperty("java.io.tmpdir") + fileName);
                        try {
                            //file = File.createTempFile(fileName + " ", ".cpxf");
                            try (PrintWriter pw = new PrintWriter(file)) {
                                pw.write(searchList.serialize());
                            }
                            //byte[] content = searchList.serialize().getBytes(CpxSystemProperties.DEFAULT_ENCODING);
                            //file = DocumentManager.createFileInTempOrSpecificDir(content, fileName);
                        } catch (IOException ex) {
                            LOG.log(Level.SEVERE, "Was not able to serialize search list filter and to store it to " + file.getAbsolutePath(), ex);
                            AlertDialog alert = AlertDialog.createErrorDialog("Der Filter '" + pItem.getName() + "' konnte nicht als Mail versendet werden",
                                    ex,
                                    /*MainApp.getWindow()*/ getScene().getWindow());
                            alert.show();
                            disableStage(false);
                            return;
                        }

                        try {
                            final String subject = "CPX " + pItem.getType().getTranslation().getValue() + ": " + pItem.getName();
                            //final String receiverMail = "cpx_team@lohmann-birkner.de"; //later: support@lohmann-birkner.de
                            final String receiverMail = "";
                            final String message = "";
                            final SendMail sendMail = new SendMail();
                            final boolean html = true;
                            sendMail.openDraft(receiverMail, subject, message, html, file);
                        } catch (IOException | MessagingException ex) {
                            LOG.log(Level.SEVERE, "Was not able to open mail draft", ex);
                            AlertDialog alert = AlertDialog.createErrorDialog("Für den Filter '" + pItem.getName() + "' konnte kein E-Mail-Entwurf erstellt werden",
                                    /*MainApp.getWindow()*/ getScene().getWindow());
                            alert.show();
                            return;
                        }

                        disableStage(false);
                    }
                });
                return btn;
            }

            private String getFilterFileName(final SearchListResult pSearchList) {
                final Date date = new Date();
                final String userName = MenuCache.instance().getUserFullNameForId(Session.instance().getCpxUserId());
                final String fileName = pSearchList.getType().getTranslation().getValue()
                        + " '" + FileUtils.validateFilename(pSearchList.getName()) + "'"
                        + " von " + userName
                        + " am " + Lang.toDate(date)
                        + " um " + Lang.toTime(date)
                        + " Uhr"
                        + ".cpxf";
                return FileUtils.validateFilename(fileName);
            }

//            private String getNameForFileName(String name) {
//                return name.replaceAll("[\\\\/:*?\"<>|]", "_");
//            }
        }
    }

}
