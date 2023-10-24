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
import de.lb.cpx.server.commonDB.model.CWmListProcessTopic;
import javafx.util.StringConverter;

/**
 * labeled Combobox to display CWmListProcessTopic
 *
 * @author wilde
 */
public class ProcessTopicCombobox extends LabeledComboBox<CWmListProcessTopic> {

    /**
     * no arg constructor for scenebuilder
     */
    public ProcessTopicCombobox() {
        this("Label");
    }

    /**
     * creates new instance with the given title as title
     *
     * @param pTitle title as title
     */
    public ProcessTopicCombobox(String pTitle) {
        super(pTitle);
        setConverter(new StringConverter<CWmListProcessTopic>() {
            @Override
            public String toString(CWmListProcessTopic object) {
                return object == null ? "" : object.getWmPtName();
            }

            @Override
            public CWmListProcessTopic fromString(String string) {
                return null;
            }
        });
//        setButtonCell(new ProcessTopicListCell());
//        setCellFactory(new Callback<ListView<CWmListProcessTopic>, ListCell<CWmListProcessTopic>>() {
//            @Override
//            public ListCell<CWmListProcessTopic> call(ListView<CWmListProcessTopic> param) {
//                return new ProcessTopicListCell();
//            }
//        });
    }

    /**
     * @param pId set item by db id
     */
    public void selectById(Long pId) {
        if (pId == null) {
            return;
        }
        for (CWmListProcessTopic item : getItems()) {
            if (item.getId() == pId) {
                select(item);
            }
        }
    }

    public boolean selectByIdent(Long pIdent) {
        if (pIdent == null) {
            return false;
        }
        for (CWmListProcessTopic item : getItems()) {
            if (item.getWmPtInternalId() == pIdent) {
                select(item);
                return true;
            }
        }
        return false;
    }

    /**
     * @return get selected item id, null if nothing is selected
     */
    public Long getSelectedId() {
        return getSelectedItem() != null ? getSelectedItem().getId() : null;
    }

//    private class ProcessTopicListCell extends ListCell<CWmListProcessTopic> {
//
//        @Override
//        protected void updateItem(CWmListProcessTopic item, boolean empty) {
//            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
//            if (item == null || empty) {
//                setText("");
//                return;
//            }
//            setText(item.getWmPtName());
//        }
//
//    }
}
