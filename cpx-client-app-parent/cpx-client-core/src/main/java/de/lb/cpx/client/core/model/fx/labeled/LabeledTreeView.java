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
 *    2017  Adameck - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.labeled;

import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 *
 * @author adameck
 * @param <T> tree view
 */
public final class LabeledTreeView<T> extends LabeledControl<TreeView<T>> {

    public LabeledTreeView(TreeView<T> pCtrl) {
        super(pCtrl);

    }

    public LabeledTreeView() {
        this(new TreeView<T>());
        getTreeView().setId("bordered-tree-view");
    }

    /**
     *
     * @return the actual TreeView
     */
    public TreeView<T> getTreeView() {
        return getControl();
    }

    /**
     * refresh treeview async
     */
    public void refresh() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                getTreeView().refresh();
            }
        });
    }

    /**
     *
     * @param pItem remove item and try to select previous entry in the children
     * list, select first if index of the item is less or equals 1 return
     * TreeItem selecctItem
     * @return treeitem
     */
    public TreeItem<T> removeAndSelectPrevious(TreeItem<T> pItem) {
        int index = getTreeView().getRoot().getChildren().indexOf(pItem);
        if (index >= 1) {
            getTreeView().getSelectionModel().select(getTreeView().getRoot().getChildren().get(index - 1));
        } else {
            getTreeView().getSelectionModel().selectFirst();

        }
        getTreeView().getRoot().getChildren().remove(pItem);

        return getTreeView().getSelectionModel().getSelectedItem();
    }

    /**
     * @param pItem item of the treeeview
     * @return index of the item in the treeview, -1 if item is not found
     */
    public int indexOf(T pItem) {

        if (contains(pItem)) {
            for (TreeItem<T> node : getTreeView().getRoot().getChildren()) {
                if (node.getValue() != null && node.getValue().equals(pItem)) {
                    return getTreeView().getRoot().getChildren().indexOf(node);
                }
            }
        }
        return -1;
    }

    public boolean contains(T pItem) {
        for (TreeItem<T> node : getTreeView().getRoot().getChildren()) {
            if (node.getValue() != null && node.getValue().equals(pItem)) {
                return true;
            }
        }
        return false;
    }
}
