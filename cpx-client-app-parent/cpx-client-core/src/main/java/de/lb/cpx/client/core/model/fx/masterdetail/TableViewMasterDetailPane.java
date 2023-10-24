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
package de.lb.cpx.client.core.model.fx.masterdetail;

import de.lb.cpx.client.core.model.fx.contextmenu.CtrlContextMenu;
import de.lb.cpx.shared.filter.ColumnOption;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;

/**
 * Implements a Masterdetailpane with a TableView in master pane part of the
 * pane
 *
 * @author wilde
 * @param <T> object type handelt by tableview
 */
public class TableViewMasterDetailPane<T> extends MasterDetailSplitPane {

    @Deprecated(since = "1.05")
    private ObjectProperty<TableView<T>> masterTableViewProperty;
    private final ContextMenu rowContextMenu;
    protected boolean doReload = true;
    protected final List<ColumnOption> savedSortOrder = new ArrayList<>();
    protected final ReadOnlyBooleanWrapper isEmptyProperty = new ReadOnlyBooleanWrapper(true);
    protected final ListChangeListener<T> listChangeListener = new ListChangeListener<T>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends T> change) {
            isEmptyProperty.set(change.getList().isEmpty());
        }
    };
    @SuppressWarnings("rawtypes")
    private final Callback<TableView.ResizeFeatures, Boolean> resizePolicy;

    /**
     * contruct new Master detail Pane uses default constrained_resize_policy
     * with a tableview in the Master Part of the Pane
     */
    public TableViewMasterDetailPane() {
        this(new TableView<T>(), TableView.UNCONSTRAINED_RESIZE_POLICY);
    }

    /**
     * contruct new Master detail Pane uses default constrained_resize_policy
     * with a tableview in the Master Part of the Pane
     *
     * @param pTableView tableview to show
     */
    public TableViewMasterDetailPane(TableView<T> pTableView) {
        this(pTableView, TableView.UNCONSTRAINED_RESIZE_POLICY);
    }

    /**
     * contruct new Master detail Pane, sets given resize policy as column
     * resize policy with a tableview in the Master Part of the Pane
     *
     * @param pTableView tableview to be displayed
     * @param pResizePolicy resize policy to set
     */
    @SuppressWarnings("rawtypes")
    public TableViewMasterDetailPane(TableView<T> pTableView, Callback<TableView.ResizeFeatures, Boolean> pResizePolicy) {
        super(DetailOrientation.BOTTOM);
        resizePolicy = pResizePolicy;
        setTableView(pTableView);
        rowContextMenu = new CtrlContextMenu<>();

        HBox detailBox = new HBox();

        detailBox.setAlignment(Pos.CENTER);
        setDetail(detailBox);
        setDividerDefaultPosition(1.0 / 3.0);
    }

    public ObjectProperty<TableView<T>> masterTableViewProperty() {
        if (masterTableViewProperty == null) {
            masterTableViewProperty = new SimpleObjectProperty<>();
        }
        return masterTableViewProperty;
    }

    public final void setTableView(TableView<T> pTableView) {
        masterTableViewProperty().set(pTableView);
        //TODO: add listener to master table view property
        pTableView.getItems().addListener(listChangeListener);
        pTableView.setColumnResizePolicy(resizePolicy);
        pTableView.setRowFactory(new TableRowCallback());
        HBox.setHgrow(pTableView, Priority.ALWAYS);
        setMaster(pTableView);
    }

    /**
     * set items in the Listview NOTE:Methodes replace old list, all listeners
     * on oldlist will be lost
     *
     * @param pItems lsit of items to set
     *
     */
    public void setTableItems(ObservableList<T> pItems) {
        getTableView().getItems().removeListener(listChangeListener);
        getTableView().getItems().addListener(listChangeListener);
        getTableView().setItems(pItems);
        isEmptyProperty.get();
    }

    public void addTableItems(ObservableList<T> pItems) {
        getTableView().getItems().addAll(pItems);
    }

    /**
     * gets the current list of items
     *
     * @return the list of the current values
     */
    public ObservableList<T> getTableItems() {
        return getTableView().getItems();
    }

    /**
     * get the selected item in the Listview
     *
     * @return selected item
     */
    public T getSelectedItem() {
        return getTableView().getSelectionModel().getSelectedItem();
    }

    /**
     * get the selected Item property from the list view
     *
     * @return selected item prooperty from the list view
     */
    public final ReadOnlyObjectProperty<T> getSelectedItemProperty() {
        return getTableView().getSelectionModel().selectedItemProperty();
    }

    /**
     * select the given object in the list view
     *
     * @param pSelect item to select
     */
    public void select(T pSelect) {
        getTableView().getSelectionModel().select(pSelect);
    }

    /*
     overriden, because set Details is set to protected
     */
    @Override
    public final void setDetail(Parent pNode) {
        super.setDetail(pNode);
    }

    /**
     * get the currentyl set tableview from the master part of the
     * masterdetailpane
     *
     * @return current tableview object
     */
    public final TableView<T> getTableView() {
        return masterTableViewProperty().get();
    }

    /**
     * add a column to the tableview
     *
     * @param pColumn column to add
     */
    public void addColumn(TableColumn<T, ?> pColumn) {
        getColumns().add(pColumn);
    }

    /**
     * get list of currently set columns in the tableview
     *
     * @return list of columns
     */
    public ObservableList<TableColumn<T, ?>> getColumns() {
        return getTableView().getColumns();
    }

    /**
     * remove a specific columns from the tableview
     *
     * @param pColumn column to remove
     * @return indicator if removing was successful
     */
    public boolean removeColumn(TableColumn<T, ?> pColumn) {
        return getTableView().getColumns().remove(pColumn);
    }

    /**
     * clear all columns
     */
    public void clearColumns() {
        getTableView().getColumns().clear();
    }

    /**
     * refresh the tableview
     */
    public void refreshTableView() {
        getTableView().refresh();
    }

    /**
     * get the context menu placed on the row of the table view
     *
     * @return the context menu shown for the row
     */
    public final ContextMenu getRowContextMenu() {
        return rowContextMenu;
    }

    /**
     * @param rowMouseClickListener this Mouse Listener will be added to the the
     * Table Row in the Row Factory e.g. to detect a double click on a row
     */
    public void setRowMouseListener(EventHandler<MouseEvent> rowMouseClickListener) {
        TableRowCallback c = new TableRowCallback();
        c.onMouseClickedListener = rowMouseClickListener;
        this.getTableView().setRowFactory(c);
    }

    /**
     * @param pText new placeholder text NOTE: Replaces old placeholder node
     * with label, if node is not type of label
     */
    public final void setPlaceholderText(String pText) {
        if (getPlaceholder() instanceof Label) {
            ((Labeled) getPlaceholder()).setText(pText);
            return;
        }
        setPlaceholder(new Label(pText));
    }

    /**
     * @return Placeholder Node of master tableview
     */
    public Node getPlaceholder() {
        return getTableView().getPlaceholder();
    }

    /**
     * @param pNode set placeholder node in master tableview
     */
    public void setPlaceholder(Node pNode) {
        getTableView().setPlaceholder(pNode);
    }

    public void setSortOrder() {

    }

    /**
     * @return readonly property if items of master table view is empty
     */
    public ReadOnlyBooleanProperty isEmptyProperty() {
        return isEmptyProperty.getReadOnlyProperty();
    }

    /**
     * @return boolean if master table view is empty
     */
    public Boolean isEmpty() {
        return isEmptyProperty().get();
    }

    private class TableRowCallback implements Callback<TableView<T>, TableRow<T>> {

        private EventHandler<MouseEvent> onMouseClickedListener;

        @Override
        public TableRow<T> call(TableView<T> tableView) {
            final TableRow<T> row = new TableRow<>();
            // Set context menu on row, but use a binding to make it only show for non-empty rows:
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(rowContextMenu)
            );

            if (onMouseClickedListener != null) {
                row.setOnMouseClicked(onMouseClickedListener);
            }
            return row;
        }

    }
}
