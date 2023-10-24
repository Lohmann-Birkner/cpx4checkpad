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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.menu.fx.list_master_detail;

import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.fx.contextmenu.CtrlContextMenu;
import de.lb.cpx.client.core.model.fx.masterdetail.ListViewMasterDetailPane;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * FXML Controller class For the master detail List
 *
 * @author wilde
 * @param <T> type
 */
@Deprecated(since = "1.05")
public class ListMasterDetailFXMLController<T> extends Controller<ListMasterDetailScene<T>> {

    private final ObjectProperty<ListViewMasterDetailPane<T>> masterDetailPaneProperty = new SimpleObjectProperty<>();
    @FXML
    private AnchorPane apContent;
    @FXML
    private VBox root;

    /**
     * sets the master detail pane in the root pane removes old one if any is
     * stored
     *
     * @param pListMdPane master detail pane to set
     */
    public void setMasterDetailPane(ListViewMasterDetailPane<T> pListMdPane) {
        CpxFXMLLoader.setAnchorsInNode(pListMdPane);
        apContent.getChildren().clear();
        apContent.getChildren().add(pListMdPane);
        masterDetailPaneProperty.setValue(pListMdPane);
//        apContent.setPadding(new Insets(48, 0, 0, 0));
        apContent.setPadding(new Insets(10, 0, 0, 0));
    }

    /**
     * get the currently set master detail pane
     *
     * @return master detail pane
     */
    public ListViewMasterDetailPane<T> getMasterDetailPane() {
        return masterDetailPaneProperty.getValue();
    }

    /**
     * get the current property of the master detail pane
     *
     * @return object property storing the current master detail pane
     */
    public ObjectProperty<ListViewMasterDetailPane<T>> getMasterDetailPaneProperty() {
        return masterDetailPaneProperty;
    }

    /**
     * set CellFactory to the list to render its content in the desired way
     *
     * @param pCellFactory factory to defines rendering of the list cell
     */
    public void setCellFactory(Callback<ListView<T>, ListCell<T>> pCellFactory) {
        if (masterDetailPaneProperty.getValue() != null) {
            masterDetailPaneProperty.getValue().setCellFactory(pCellFactory);
        }
    }

    /**
     * get the selected item property from the list view
     *
     * @return selected item property form the list view
     */
    public ReadOnlyObjectProperty<T> getSelectedItemProperty() {
        return masterDetailPaneProperty.getValue().getSelectedItemProperty();
    }

    /**
     * gets the instance of the current listview in the master area of the pane
     *
     * @return list view instance
     */
    public ListView<T> getListView() {
        return masterDetailPaneProperty.getValue().getListView();
    }

    /**
     * get the current context menu from the list view
     *
     * @return contextmenu of the list view
     */
    public ContextMenu getListViewContextMenu() {
        return getListView().getContextMenu();
    }

    /**
     * sets a contect menu to the listview, overrides the old one
     *
     * @param pMenu menu to set
     */
    public void setListViewContextMenu(ContextMenu pMenu) {
        getListView().setContextMenu(pMenu);
    }

    /**
     * adds a menu item to the context menu, if no context menu is present an
     * new one is initialized context menu may not update its view if it already
     * shown in the ui
     *
     * @param pItem item to set
     */
    public void addMenuItemToListViewContextMenu(MenuItem pItem) {
        if (getListView().getContextMenu() == null) {
            getListView().setContextMenu(new CtrlContextMenu<>());
        }
        getListView().getContextMenu().getItems().add(pItem);
    }

    /**
     * adds a menu item to the context menu, if no context menu is present an
     * new one is initialized context menu may not update its view if it already
     * shown in the ui
     *
     * @param pItems items to set
     */
    public void addMenuItemsToListViewContextMenu(MenuItem... pItems) {
        if (getListView().getContextMenu() == null) {
            getListView().setContextMenu(new CtrlContextMenu<>());
        }
        getListView().getContextMenu().getItems().addAll(pItems);
    }

    public void addToRoot(int pIndex, Node pNode) {
        apContent.getChildren();
        root.getChildren().add(pIndex, pNode);
    }

    public void addToRoot(Node pNode) {
        root.getChildren().add(pNode);
    }
}
