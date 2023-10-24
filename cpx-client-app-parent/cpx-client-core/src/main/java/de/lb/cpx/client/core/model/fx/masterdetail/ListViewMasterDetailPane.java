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
package de.lb.cpx.client.core.model.fx.masterdetail;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * Creates a master detail pane with a list view as Master and a Anchorpane as
 * detail Master will occupy 1/3 of the available space and detail 2/3
 *
 * @author wilde
 * @param <T> type of list content
 */
@Deprecated(since = "1.05")
public class ListViewMasterDetailPane<T> extends MasterDetailBorderPane {

    private ListView<T> lvItems;
//    TreeView<T> lvItems;

    /**
     * contruct new Master detail Pane
     *
     * @param pListView list view to show in master
     */
    public ListViewMasterDetailPane(ListView<T> pListView) {
        super();
        setListView(pListView);
        HBox detailBox = new HBox();
        detailBox.setAlignment(Pos.CENTER);
        setDetail(detailBox);
//        getDetailPane().prefWidthProperty().bind(this.widthProperty().divide(masterDetailRatioProperty()));
    }
//    private DoubleProperty masterDetailRatioProperty;
//    public final DoubleProperty masterDetailRatioProperty(){
//        if(masterDetailRatioProperty == null){
//            masterDetailRatioProperty = new SimpleDoubleProperty(1.3);
//        }
//        return masterDetailRatioProperty;
//    }
//    public void setMasterDetailRatio(Double pRatio){
//        masterDetailRatioProperty().set(pRatio);
//    }
//    public Double getMasterDetailRatio(){
//        return masterDetailRatioProperty().get();
//    }

    /**
     * contruct new Master detail Pane
     */
    public ListViewMasterDetailPane() {
        this(new ListView<>());
    }

    /**
     * set items in the Listview
     *
     * @param pItems lsit of items to set
     */
    public void setItems(ObservableList<T> pItems) {
        lvItems.setItems(pItems);
    }

    /**
     * gets the current list of items
     *
     * @return the list of the current values
     */
    public ObservableList<T> getItems() {
        return lvItems.getItems();
    }

    /**
     * get the selected item in the Listview
     *
     * @return selected item
     */
    public T getSelectedItem() {
        return lvItems.getSelectionModel().getSelectedItem();
    }

    /**
     * get the selected Item property from the list view
     *
     * @return selected item prooperty from the list view
     */
    public ReadOnlyObjectProperty<T> getSelectedItemProperty() {
        return lvItems.getSelectionModel().selectedItemProperty();
    }

    /**
     * select the given object in the list view
     *
     * @param pSelect item to select
     */
    public void select(T pSelect) {
        lvItems.getSelectionModel().select(pSelect);
    }

    /**
     * sets the CellFactory to the Listview
     *
     * @param pCellFactory cellfactory to set
     */
    public void setCellFactory(Callback<ListView<T>, ListCell<T>> pCellFactory) {
        lvItems.setCellFactory(pCellFactory);
    }

    @Override
    public final void setDetail(Parent pNode) {
        super.setDetail(pNode);
    }

    public ListView<T> getListView() {
        return lvItems;
    }

    public final void setListView(ListView<T> pListView) {
        HBox.setHgrow(pListView, Priority.ALWAYS);
        setMaster(new HBox(pListView));
        lvItems = pListView;
    }

    public void setListView(ListView<T> pListView, Node pMenu) {
        VBox.setVgrow(pListView, Priority.ALWAYS);
        setMaster(new VBox(pMenu, pListView));
        lvItems = pListView;
    }
}
