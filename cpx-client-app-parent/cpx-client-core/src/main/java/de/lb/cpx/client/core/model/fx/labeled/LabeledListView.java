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
 *    2017  Bohm - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.labeled;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 *
 * @author Bohm
 * @param <T> type
 */
public class LabeledListView<T> extends LabeledControl<ListView<T>> {
    
    BooleanProperty emptyProperty = new SimpleBooleanProperty(true);

    public LabeledListView(ListView<T> pCtrl, Insets pTitlePadding) {
        super(pCtrl);
        ((LabeledControlSkin) getSkin()).setTitlePadding(pTitlePadding);
    }

    public LabeledListView(ListView<T> pCtrl) {
        this(pCtrl, Insets.EMPTY);
    }

    public LabeledListView() {
        this(new ListView<T>());
        getListView().setId("bordered-list-view");
    }

    /**
     * @return the actual ListView
     */
    public final ListView<T> getListView() {
        return getControl();
    }

    /**
     * sets a list of items in the listview
     *
     * @param pItems items to set
     */
    public void setItems(ObservableList<T> pItems) {
        getControl().setItems(pItems);
        emptyProperty.set(getItems().isEmpty());
    }

    /**
     * @return list of items
     */
    public ObservableList<T> getItems() {
        return getControl().getItems();
    }

    /**
     * sets the cell factory to the listview
     *
     * @param pCallback callback for the cellfactory
     */
    public void setCellFactory(Callback<ListView<T>, ListCell<T>> pCallback) {
        getControl().setCellFactory(pCallback);
    }

    /**
     * add an item to the listview
     *
     * @param pItem item to add
     */
    public void addItem(T pItem) {
        getListView().getItems().add(pItem);
        emptyProperty.set(getItems().isEmpty());
    }

    public void removeItem(T pItem) {
        getListView().getItems().remove(pItem);
        emptyProperty.set(getItems().isEmpty());
    }

    /**
     * refresh the listview
     */
    public void refresh() {
        getListView().refresh();
        emptyProperty.set(getItems().isEmpty());
    }

    /**
     * select the item if it is present in the list
     *
     * @param pItem item to select
     */
    public void select(T pItem) {
        getListView().getSelectionModel().select(pItem);
    }

    /**
     * get the selcted item property from the listview
     *
     * @return selected item property
     */
    public ReadOnlyObjectProperty<T> getSelectedItemProperty() {
        return getListView().getSelectionModel().selectedItemProperty();
    }

    /**
     * get the currently selected item
     *
     * @return selected item
     */
    public T getSelectedItem() {
        return getSelectedItemProperty().get();
    }

    /**
     * @param pItem item to get index of
     * @return index of the item
     */
    public int getIndexOf(T pItem) {
        return getControl().getItems().indexOf(pItem);
    }

    /**
     * clear the selection of the choosen item
     *
     * @param pItem item to clear selection in the list
     */
    public void clearSelectionOf(T pItem) {
        getControl().getSelectionModel().clearSelection(getIndexOf(pItem));
    }

    public BooleanProperty emptyProperty() {
        return emptyProperty;
    }

}
