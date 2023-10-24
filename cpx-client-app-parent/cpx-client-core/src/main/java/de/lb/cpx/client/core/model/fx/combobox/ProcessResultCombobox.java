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
package de.lb.cpx.client.core.model.fx.combobox;

import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import de.lb.cpx.server.commonDB.model.CWmListProcessResult;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * Combobox to display CwmListProcessResult
 *
 * @author wilde
 */
public class ProcessResultCombobox extends LabeledComboBox<CWmListProcessResult> {

    /**
     * no arg constructor for scene builder
     */
    public ProcessResultCombobox() {
        this("Label");
    }

    /**
     * create new instance of the labeledcombobox with the given title
     *
     * @param pTitle title of the combobox
     */
    public ProcessResultCombobox(String pTitle) {
        super(pTitle);
        setButtonCell(new ProcessResultListCell());
        setCellFactory(new Callback<ListView<CWmListProcessResult>, ListCell<CWmListProcessResult>>() {
            @Override
            public ListCell<CWmListProcessResult> call(ListView<CWmListProcessResult> param) {
                return new ProcessResultListCell();
            }
        });
    }
//
//    /**
//     * @param pId select item by db id
//     */
//    public void selectById(Long pId) {
//        if (pId == null || pId.equals(-1L)) {
//            return;
//        }
//        for (CWmListProcessResult item : getItems()) {
//            if (item.getId() == pId) {
//                select(item);
//            }
//        }
//    }

    public void selectByInternalId(Long pInternalId) {
        if (pInternalId == null || pInternalId.equals(-1L)) {
            return;
        }
        for (CWmListProcessResult item : getItems()) {
            if (item.getWmPrInternalId() == pInternalId) {
                select(item);
            }
        }
    }

    /**
     * @return selected id of the process result, null if nothing is selected
     */
    public Long getSelectedInternalId() {
        return getSelectedItem() != null ? getSelectedItem().getWmPrInternalId() : null;
    }
//  public Long getSelectedId() {
//        return getSelectedItem() != null ? getSelectedItem().getId() : null;
//    }

    private class ProcessResultListCell extends ListCell<CWmListProcessResult> {

        @Override
        protected void updateItem(CWmListProcessResult item, boolean empty) {
            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
            if (item == null || empty) {
                setText("");
                return;
            }
            setText(item.getWmPrName());
        }

    }
}
