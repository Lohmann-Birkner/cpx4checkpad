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
package de.lb.cpx.client.core.menu.model;

import de.lb.cpx.client.core.menu.fx.ToolbarMenuFXMLController.Toolbar;
import de.lb.cpx.client.core.util.ResourceLoader;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * Toolbar menu item to show list of items
 *
 * @author wilde
 * @param <T> List content
 */
public class ToolbarListMenuItem<T> extends ToolbarMenuItem {

    private PopOver popover;
    private Button btnReducedTitle;
    private Button btnReducedListView;
    private TitledPane extendedTiteledPane;
    private ListView<T> listView;
    //action property
    private ObjectProperty<EventHandler<ActionEvent>> onActionProperty;
    //selected item in list
    private ObjectProperty<T> selectedItemProperty;
    //cell factory property to render list item
    private ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactoryProperty;
    //items to be shown in listview
    private ObservableList<T> items;
    //remove callback
    private Callback<T, Boolean> onRemoveCallback;

    /**
     * creates new instance
     *
     * @param pBar bar
     */
    public ToolbarListMenuItem(Toolbar pBar) {
        super(pBar);
        btnReducedListView.textProperty().bind(Bindings.size(getItems()).asString());
        glyphProperty().addListener(new ChangeListener<FontAwesome.Glyph>() {
            @Override
            public void changed(ObservableValue<? extends FontAwesome.Glyph> observable, FontAwesome.Glyph oldValue, FontAwesome.Glyph newValue) {
                extendedTiteledPane.setGraphic(ResourceLoader.getGlyph(newValue));
                btnReducedTitle.setGraphic(ResourceLoader.getGlyph(newValue));
            }
        });
    }

    public void setContent(Boolean pExtended) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (pExtended) {
                    if (extendedTiteledPane == null) {
                        getExtendedNode();
                    }
                    if (popover != null) {
                        popover.setContentNode(null);
                    }
                    extendedTiteledPane.setContent(getListView());
                } else {
                    extendedTiteledPane.setContent(null);
                }
            }
        });
    }

    @Override
    public Node getExtendedNode() {
        if (getExtendedRoot() == null) {
            extendedTiteledPane = new TitledPane();
            extendedTiteledPane.disableProperty().bind(disableProperty());
            extendedTiteledPane.tooltipProperty().bind(tooltipProperty());
            extendedTiteledPane.textProperty().bind(extendedTitleProperty());
            extendedTiteledPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (getOnAction() != null) {
                        getOnAction().handle(new ActionEvent());
                        focusNode(extendedTiteledPane);
                    }
                }
            });
            extendedTiteledPane.getStyleClass().add("tab-titled-pane");
            extendedTiteledPane.setMaxHeight(Double.MAX_VALUE);
            extendedTiteledPane.setExpanded(true);
            extendedTiteledPane.setCollapsible(false);
            VBox.setVgrow(extendedTiteledPane, Priority.ALWAYS);
            setExtendedRoot(extendedTiteledPane);
            setContent(bar.isExtended());
        }
        return getExtendedRoot();
    }

    /**
     * @return objectproperty with handler to define behavior when item is
     * selected
     */
    public ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        if (onActionProperty == null) {
            onActionProperty = new SimpleObjectProperty<>();
        }
        return onActionProperty;
    }

    /**
     * @param pEvent handler to be executed when item is selected
     */
    public void setOnAction(EventHandler<ActionEvent> pEvent) {
        onActionProperty().set(pEvent);
    }

    /**
     * @return handler that whould be executed if imte is selected
     */
    public EventHandler<ActionEvent> getOnAction() {
        return onActionProperty.get();
    }

    @Override
    public Node getReducedNode() {
        if (getReducedRoot() == null) {
            btnReducedTitle = new Button();
            btnReducedTitle.disableProperty().bind(disableProperty());
            btnReducedTitle.setMaxWidth(Double.MAX_VALUE);
            btnReducedTitle.tooltipProperty().bind(tooltipProperty());
            btnReducedTitle.getStyleClass().add("menu-tab-button");
            btnReducedTitle.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    getOnAction().handle(new ActionEvent());
                }
            });
            btnReducedListView = new Button();
            VBox.setVgrow(btnReducedListView, Priority.ALWAYS);
            btnReducedListView.disableProperty().bind(disableProperty());
            btnReducedListView.getStyleClass().add("list-info-button");
            btnReducedListView.setMaxHeight(Double.MAX_VALUE);
            btnReducedListView.setMaxWidth(Double.MAX_VALUE);
            btnReducedListView.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (popover == null) {
                        popover = new PopOver();
                        popover.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
                        popover.setDetachable(false);
                    }
                    if (!popover.isShowing()) {
                        getListView().getItems().size();
                        popover.setContentNode(getListView());
                        popover.show(btnReducedListView);
                    } else {
                        popover.hide();
                    }
                }
            });
            setReducedRoot(new VBox(btnReducedTitle, btnReducedListView));
            getReducedRoot().getStyleClass().add("reduced-item");
            VBox.setVgrow(getReducedRoot(), Priority.ALWAYS);
        }
        return getReducedRoot();
    }

    /**
     * @return selected item property in list
     */
    public ObjectProperty<T> selectedItemProperty() {
        if (selectedItemProperty == null) {
            selectedItemProperty = new SimpleObjectProperty<>();
        }
        return selectedItemProperty;
    }

    /**
     * @param pItem select item in list
     */
    public void select(T pItem) {
        selectedItemProperty().set(pItem);
    }

    /**
     * @return selected list item
     */
    public T getSelectedItem() {
        return selectedItemProperty().get();
    }

    /**
     * clear selection and focus of the menu item
     */
    public void clearSelection() {
        select(null);
        focusNode(null);
    }

    /**
     * @return cellfactory property to render item in listview
     */
    public ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactoryProperty() {
        if (cellFactoryProperty == null) {
            cellFactoryProperty = new SimpleObjectProperty<>();
        }
        return cellFactoryProperty;
    }

    /**
     * @param pCallback cell factory for cell rendering
     */
    public void setCellFactory(Callback<ListView<T>, ListCell<T>> pCallback) {
        cellFactoryProperty().set(pCallback);
    }

    /**
     * @return get current cell factory for listview
     */
    public Callback<ListView<T>, ListCell<T>> getCellFactory() {
        return cellFactoryProperty().get();
    }

    /**
     * @return list of items for listview
     */
    public ObservableList<T> getItems() {
        if (items == null) {
            items = FXCollections.observableArrayList();
        }
        return items;
    }

    protected ListView<T> getListView() {
        if (listView == null) {
            listView = createListView();
        }
        return listView;
    }

    @Override
    public void refresh() {
        //needed update otherwise cellfactory will show empty lists
        super.refresh(); //To change body of generated methods, choose Tools | Templates.
        setContent(bar.isExtended());
        getListView().refresh();
    }

    private ListView<T> createListView() {
        ListView<T> listView = new ListView<>();
        listView.setItems(getItems());
        listView.setId("listView");
        listView.getStyleClass().add("stay-selected-list-view");
        if (getSelectedItem() != null) {
            listView.getSelectionModel().select(getSelectedItem());
        }
        listView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends T> observable, T oldValue, T newValue) -> {
            select(newValue);
            if (newValue != null) {
                focusNode(listView);
            } else {
                focusNode(null);
            }
        });
        selectedItemProperty().addListener(new ChangeListener<T>() {
            @Override
            public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
                listView.getSelectionModel().select(newValue);
            }
        });
        listView.cellFactoryProperty().bind(cellFactoryProperty());
        listView.setPrefSize(200, 200);
        listView.setMaxHeight(Double.MAX_VALUE);
        listView.getItems().addListener(new ListChangeListener<T>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends T> c) {
                if (c.getList().isEmpty()) {
                    if (popover != null) {
                        if (popover.isShowing()) {
                            popover.hide();
                        }
                    }
                } else {
                    //try to fix empty listview issue with force reloading of popover listview, if item count changes
                    //listcell should update last and correct value should be shown
                    listView.refresh();
                }
            }
        });

        return listView;
    }

    /**
     * clear items and call the removeCallback
     */
    public void clearItems() {
        Iterator<T> it = new ArrayList<>(getItems()).iterator();
        while (it.hasNext()) {
            T next = it.next();
            if (getOnRemoveCallback() != null) {
                getOnRemoveCallback().call(next);
            }
        }
    }

    /**
     * @param pCallback callback to be executed if remove was called
     */
    public void setOnRemoveCallback(Callback<T, Boolean> pCallback) {
        onRemoveCallback = pCallback;
    }

    /**
     * @return get the current remove callback
     */
    public Callback<T, Boolean> getOnRemoveCallback() {
        return onRemoveCallback;
    }
}
