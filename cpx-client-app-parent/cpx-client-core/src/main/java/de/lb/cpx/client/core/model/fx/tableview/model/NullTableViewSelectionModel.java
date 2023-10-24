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
package de.lb.cpx.client.core.model.fx.tableview.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;

/**
 * NullSelectionModel, prevents the selection of a specific row Sometimes it is
 * not neccessary for design und usage purpose to support that function of a
 * table view
 *
 * @author wilde
 * @param <S> type
 */
public class NullTableViewSelectionModel<S> extends TableView.TableViewSelectionModel<S> {

    /**
     * creates a new instance with empty handler methodes
     *
     * @param tableView tableview to add model to
     */
    public NullTableViewSelectionModel(TableView<S> tableView) {
        super(tableView);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public ObservableList<TablePosition> getSelectedCells() {
        return FXCollections.emptyObservableList();
    }

    @Override
    public void selectLeftCell() {

    }

    @Override
    public void selectRightCell() {

    }

    @Override
    public void selectAboveCell() {

    }

    @Override
    public void selectBelowCell() {

    }

    @Override
    public void clearSelection(int i, TableColumn<S, ?> tableColumn) {

    }

    @Override
    public void clearAndSelect(int i, TableColumn<S, ?> tableColumn) {

    }

    @Override
    public void select(int i, TableColumn<S, ?> tableColumn) {

    }

    @Override
    public boolean isSelected(int i, TableColumn<S, ?> tableColumn) {
        return false;
    }

    @Override
    public ObservableList<Integer> getSelectedIndices() {
        return FXCollections.emptyObservableList();
    }

    @Override
    public ObservableList<S> getSelectedItems() {
        return FXCollections.emptyObservableList();
    }

    @Override
    public void selectIndices(int i, int... ints) {

    }

    @Override
    public void selectAll() {

    }

    @Override
    public void clearAndSelect(int i) {

    }

    @Override
    public void select(int i) {

    }

    @Override
    public void select(Object o) {

    }

    @Override
    public void clearSelection(int i) {

    }

    @Override
    public void clearSelection() {

    }

    @Override
    public boolean isSelected(int i) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void selectPrevious() {

    }

    @Override
    public void selectNext() {

    }

    @Override
    public void selectFirst() {

    }

    @Override
    public void selectLast() {

    }
}
