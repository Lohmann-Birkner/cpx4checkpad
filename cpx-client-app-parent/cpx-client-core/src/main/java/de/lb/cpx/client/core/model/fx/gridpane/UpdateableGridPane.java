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
package de.lb.cpx.client.core.model.fx.gridpane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import static javafx.scene.layout.GridPane.getRowIndex;

/**
 * SOMETHING TO NOTICE: If added by Scenebuilder, check if row contraint is set!
 * Otherwise methodes may malfunction
 *
 * @param <T> type of items
 * @author wilde
 */
public abstract class UpdateableGridPane<T> extends GridPane {

    private final ObjectProperty<ObservableList<T>> itemsProperty = new SimpleObjectProperty<>(FXCollections.observableArrayList());
    private int columns = 0;

    public UpdateableGridPane() {
        super();
        getItems().addListener(new ListChangeListener<T>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends T> c) {
                while (c.next()) {
                    c.wasUpdated();
                    if (c.wasAdded()) {
                        for (T item : c.getAddedSubList()) {
                            int index = c.getList().indexOf(item) + 1;
                            addRow(index, item, updateRow(item));
                        }
                    }
                    if (c.wasRemoved()) {
                        for (T removed : c.getRemoved()) {
                            int removedIndex = getRowIndexOf(removed);
                            clearRow(removedIndex);
                            for (int i = removedIndex + 1; i <= itemsProperty.get().size() + 1; i++) {
                                List<Label> content = getRow(i);
                                clearRow(i);
                                addRow(i - 1, c.getList().get(i - 2), content.toArray(new Label[content.size()]));

                            }
                        }

                    }
                    if (c.wasReplaced()) {
                        List<? extends T> addedSize = c.getList();
                    }
                }
            }
        });
    }

    public UpdateableGridPane(Label... header) {
        this();
        setHeader(header);
    }

    public void setItems(List<T> pItems) {
        getItems().clear();
        getItems().addAll(pItems);
        refresh();
    }

    public final void setHeader(Label... header) {
        columns = header.length;
        clearRow(0);
        addRow(0, header);
        resizeColumns();
    }

    public void refresh() {
        int counter = 1;
        for (T item : getItems()) {
            refreshRowItem(counter, item);
        }
    }

    public abstract void setRowIndexTo(int pRowIndex, T pItem);

    public abstract int getRowIndexOf(T pItem);

    public abstract Node[] updateRow(T pItem);

    public Node[] updateRow(int pRow) {
        return updateRow(getItems().get(pRow));
    }

    public List<Label> getRow(int pRow) {
        List<Label> result = new ArrayList<>();
        for (Node n : getChildren()) {
            Integer index = getRowIndex(n);
            if (index != null && index.equals(pRow)) {
                result.add((Label) n);
            }
        }

        return result;
    }

    private void resizeColumns() {
        getChildren().removeIf((Node t) -> GridPane.getColumnIndex(t) > columns);
    }

    private void clearRow(int pRow) {
        getChildren().removeIf(new Predicate<Node>() {
            @Override
            public boolean test(Node t) {
                Integer index = getRowIndex(t);
                if (index != null && index == pRow) {
                    return index.equals(pRow);
                }
                return false;
            }
        });
//        getRowConstraints().remove(pRow);
    }

    private int setColumnsSize() {
        if (columns == 0) {
            columns = getRow(0).size() + 1;
        }
        return columns;
    }

    public void refreshRowItem(T item) {
        int index = getItems().indexOf(item) + 1;
        //not found
        if (index == -1) {
            return;
        }
        refreshRowItem(index, item);
    }

    private void refreshRowItem(int pIndex, T item) {
        List<Node> result = fitToColumnSize(Arrays.asList(updateRow(item)));
        clearRow(pIndex);
        addRow(pIndex, item, result.toArray(new Node[result.size()]));
    }

    public void addItem(T pItem) {
        getItems().add(pItem);
    }

    public final ObservableList<T> getItems() {
        return itemsProperty.get();
    }

    public ObjectProperty<ObservableList<T>> getItemProperty() {
        return itemsProperty;
    }

    private List<Node> fitToColumnSize(List<Node> asList) {
        setColumnsSize();
        if (columns < asList.size()) {
            return asList.subList(0, columns);
        }
        return asList;
    }

    public void addRow(int pIndex, T item, Node... pNodes) {
        setRowIndexTo(pIndex, item);
//        getRowConstraints().add(pIndex,new RowConstraints(10, 30, USE_COMPUTED_SIZE));
        addRow(pIndex, pNodes);
    }
}
