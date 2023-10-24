/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.model;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Separator;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.skin.TableViewSkin;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author wilde
 * @param <T> type
 */
public class FixedTableSkin<T> extends SkinBase<FixedTable<T>> {

    private static final Logger LOG = Logger.getLogger(FixedTableSkin.class.getName());

    private TableView<T> fixedTable;
    private TableView<T> mainTable;

    /**
     *
     * @param control control to skin
     */
    public FixedTableSkin(FixedTable<T> control) {
        super(control);
        getChildren().add(createRoot());
        fixedTable.getItems().addListener(new ListChangeListener<T>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends T> c) {
                computeFixedTableHeight(fixedTable.getFixedCellSize(), c.getList().size());
            }
        });
        computeFixedTableHeight(fixedTable.getFixedCellSize(), fixedTable.getItems().size());
        
        control.displayModeProperty().addListener(new ChangeListener<FixedTable.DisplayMode>() {
            @Override
            public void changed(ObservableValue<? extends FixedTable.DisplayMode> ov, FixedTable.DisplayMode t, FixedTable.DisplayMode t1) {
                fixedTable.refresh();
                mainTable.refresh();
            }
        });
    }

    private void computeFixedTableHeight(double pFixedHeight, int pNumberOfItems) {
        if (fixedTable == null) {
            return;
        }
        fixedTable.setMinHeight(pFixedHeight * pNumberOfItems);
        fixedTable.setPrefHeight(pFixedHeight * pNumberOfItems);
        fixedTable.setMaxHeight(pFixedHeight * pNumberOfItems);
    }

    private Parent createRoot() {
        mainTable = new TableView<>();
        mainTable.setFixedCellSize(32);
        mainTable.setSelectionModel(new NoSelectionModel<>(mainTable));
        mainTable.getStyleClass().add("remove-h-scroll-bar");
        mainTable.getStyleClass().add("stay-selected-table-view");
        mainTable.getStyleClass().add("resize-column-table-view");
        mainTable.getStyleClass().add("fixed-table-view");
        mainTable.setSkin(new TableViewSkin<>(mainTable));
        Bindings.bindContent(mainTable.getColumns(), getSkinnable().getColumns());
        Bindings.bindContent(mainTable.getItems(), getSkinnable().getItems());

        fixedTable = new TableView<>();
        fixedTable.setFixedCellSize(32);
        fixedTable.setSelectionModel(new NoSelectionModel<>(mainTable));
        fixedTable.getStyleClass().add("remove-h-scroll-bar");
        fixedTable.getStyleClass().add("remove-v-scroll-bar");
        fixedTable.getStyleClass().add("tableview-header-hidden");
        fixedTable.getStyleClass().add("stay-selected-table-view");
        fixedTable.getStyleClass().add("resize-column-table-view");
        mainTable.getStyleClass().add("fixed-table-view");
        fixedTable.setSkin(new TableViewSkin<>(fixedTable));
        Bindings.bindContent(fixedTable.getColumns(), getSkinnable().getFixedColumns());
        Bindings.bindContent(fixedTable.getItems(), getSkinnable().getFixedItems());

        // synchronize scrollbars (must happen after table was made visible)
        ScrollBar mainTableHorizontalScrollBar = findScrollBar(mainTable, Orientation.HORIZONTAL);
        ScrollBar mainTableVertivalScrollBar = findScrollBar(mainTable, Orientation.VERTICAL);
        if (mainTableVertivalScrollBar != null) {
            getSkinnable().vBarShowingProperty.bind(mainTableVertivalScrollBar.visibleProperty());
        }

        ScrollBar fixedTableHorizontalScrollBar = findScrollBar(fixedTable, Orientation.HORIZONTAL);
//        mainTableHorizontalScrollBar.valueProperty().bindBidirectional( sumTableHorizontalScrollBar.valueProperty());
        ScrollBar hBar = new ScrollBar();
        hBar.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (mainTableHorizontalScrollBar != null) {
                    mainTableHorizontalScrollBar.setValue(newValue.doubleValue());
                }
                if (fixedTableHorizontalScrollBar != null) {
                    fixedTableHorizontalScrollBar.setValue(newValue.doubleValue());
                }
            }
        });
        hBar.setOrientation(Orientation.HORIZONTAL);

        syncBar(hBar, mainTableHorizontalScrollBar);
        syncBar(hBar, fixedTableHorizontalScrollBar);
//        hBar.visibleAmountProperty().bind(mainTableHorizontalScrollBar.visibleAmountProperty());
//        hBar.visibleProperty().bind(mainTableHorizontalScrollBar.visibleProperty());
//        mainTableHorizontalScrollBar.valueProperty().bindBidirectional(hBar.valueProperty());
//        fixedTableHorizontalScrollBar.valueProperty().bindBidirectional(hBar.valueProperty());
        Separator sep = new Separator(Orientation.HORIZONTAL);
        sep.setPadding(new Insets(10, 0, 0, 0));
        VBox content = new VBox(mainTable, sep, fixedTable);
        VBox root = new VBox(content, hBar);
        root.setFillWidth(true);
        VBox.setVgrow(mainTable, Priority.ALWAYS);
        return root;
    }

    private ScrollBar findScrollBar(TableView<?> table, Orientation orientation) {

        // this would be the preferred solution, but it doesn't work. it always gives back the vertical scrollbar
        //		return (ScrollBar) table.lookup(".scroll-bar:horizontal");
        //		
        // => we have to search all scrollbars and return the one with the proper orientation
        Set<Node> set = table.lookupAll(".scroll-bar");
        for (Node node : set) {
            ScrollBar bar = (ScrollBar) node;
            if (bar.getOrientation() == orientation) {
                return bar;
            }
        }
        LOG.log(Level.WARNING, "was not able to find scrollbar with this orientiation: {0}", orientation);
        return null;
    }

    private void syncBar(ScrollBar hBar, ScrollBar bar) {
        if (hBar == null) {
            LOG.log(Level.WARNING, "hBar should not be null!");
            return;
        }
        if (bar == null) {
            LOG.log(Level.WARNING, "bar should not be null!");
            return;
        }
        hBar.visibleProperty().bindBidirectional(bar.visibleProperty());
        hBar.visibleAmountProperty().bindBidirectional(bar.visibleAmountProperty());
//                            getVScrollBar().valueProperty().bindBidirectional(bar.valueProperty());
        hBar.minProperty().bindBidirectional(bar.minProperty());
        hBar.maxProperty().bindBidirectional(bar.maxProperty());
    }

    public class NoSelectionModel<T> extends TableViewSelectionModel<T> {

        public NoSelectionModel(TableView<T> tableView) {
            super(tableView);
        }

        @Override
        public ObservableList<Integer> getSelectedIndices() {
            return FXCollections.emptyObservableList();
        }

        @Override
        public ObservableList<T> getSelectedItems() {
            return FXCollections.emptyObservableList();
        }

        @Override
        public void selectIndices(int index, int... indices) {
            //empty implementation
        }

        @Override
        public void selectAll() {
            //empty implementation
        }

        @Override
        public void selectFirst() {
            //empty implementation
        }

        @Override
        public void selectLast() {
            //empty implementation
        }

        @Override
        public void clearAndSelect(int index) {
            //empty implementation
        }

        @Override
        public void select(int index) {
            //empty implementation
        }

        @Override
        public void select(T obj) {
            //empty implementation
        }

        @Override
        public void clearSelection(int index) {
            //empty implementation
        }

        @Override
        public void clearSelection() {
            //empty implementation
        }

        @Override
        public boolean isSelected(int index) {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public void selectPrevious() {
            //empty implementation
        }

        @Override
        public void selectNext() {
            //empty implementation
        }

        @Override
        @SuppressWarnings("rawtypes")
        public ObservableList<TablePosition> getSelectedCells() {
            return FXCollections.emptyObservableList();
        }

        @Override
        public boolean isSelected(int row, TableColumn<T, ?> column) {
            return false;
        }

        @Override
        public void select(int row, TableColumn<T, ?> column) {
            //empty implementation
        }

        @Override
        public void clearAndSelect(int row, TableColumn<T, ?> column) {
            //empty implementation
        }

        @Override
        public void clearSelection(int row, TableColumn<T, ?> column) {
            //empty implementation
        }

        @Override
        public void selectLeftCell() {
            //empty implementation
        }

        @Override
        public void selectRightCell() {
            //empty implementation
        }

        @Override
        public void selectAboveCell() {
            //empty implementation
        }

        @Override
        public void selectBelowCell() {
            //empty implementation
        }
    }
}
