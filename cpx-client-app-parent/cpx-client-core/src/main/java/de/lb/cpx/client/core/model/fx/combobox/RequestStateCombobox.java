/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.combobox;

import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import de.lb.cpx.server.commonDB.model.CWmListMdkState;
import javafx.util.StringConverter;

/**
 * Combobox to display CwmListMdkState
 *
 * @author shahin
 */
public class RequestStateCombobox extends LabeledComboBox<CWmListMdkState> {

    /**
     * no arg constructor for scene builder
     */
    public RequestStateCombobox() {
        this("Label");
    }

    /**
     * create new instance of the labeledcombobox with the given title
     *
     * @param pTitle title of the combobox
     */
    public RequestStateCombobox(String pTitle) {
        super(pTitle);
        setConverter(new StringConverter<CWmListMdkState>() {
            @Override
            public String toString(CWmListMdkState t) {
                return t == null ? "" : t.getWmMsName();
            }

            @Override
            public CWmListMdkState fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
//        setButtonCell(new MdkStateListCell());
//        setCellFactory(new Callback<ListView<CWmListMdkState>, ListCell<CWmListMdkState>>() {
//            @Override
//            public ListCell<CWmListMdkState> call(ListView<CWmListMdkState> param) {
//                return new MdkStateListCell();
//            }
//        });
    }

    /**
     * @param pId select item by db id
     */
    public void selectById(Long pId) {
        if (pId == null || pId.equals(-1L)) {
            return;
        }
        for (CWmListMdkState item : getItems()) {
            if (item.getId() == pId) {
                select(item);
            }
        }
    }

    /**
     * @param pInId select item by db WmMsInternalId
     */
    public void selectByInternalId(Long pInId) {
        if (pInId == null || pInId.equals(-1L)) {
            return;
        }
        for (CWmListMdkState item : getItems()) {
            if (item.getWmMsInternalId() == pInId) {
                select(item);
            }
        }
    }

    /**
     * @return selected id of the mdk state, null if nothing is selected
     */
    public Long getSelectedId() {
        return getSelectedItem() != null ? getSelectedItem().getId() : null;
    }

    /**
     * @return selected wmMsInternalId of the mdk state, null if nothing is
     * selected
     */
    public Long getSelectedInternalId() {
        return getSelectedItem() != null ? getSelectedItem().getWmMsInternalId() : null;
    }

//    private class MdkStateListCell extends ListCell<CWmListMdkState> {
//
//        @Override
//        protected void updateItem(CWmListMdkState item, boolean empty) {
//            super.updateItem(item, empty);
//            if (item == null || empty) {
//                setText("");
//                return;
//            }
//            setText(item.getWmMsName());
//        }
//
//    }
}
