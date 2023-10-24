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
import de.lb.cpx.server.commonDB.model.CMdkAuditreason;
import javafx.util.StringConverter;

/**
 * Labeled combobox for displaying the main auditreason
 *
 * @author wilde
 */
public class AuditReasonCombobox extends LabeledComboBox<CMdkAuditreason> {

    /**
     * no arg constructor for scene builder
     */
    public AuditReasonCombobox() {
        this("Label");
    }

    /**
     * creates new instance of the labeled comobox with title
     *
     * @param pTitle title to set
     */
    public AuditReasonCombobox(String pTitle) {
        super(pTitle);
        setConverter(new StringConverter<CMdkAuditreason>() {
            @Override
            public String toString(CMdkAuditreason t) {
                return t == null ? "" : t.getMdkArName();
            }

            @Override
            public CMdkAuditreason fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
//        setButtonCell(new AuditReasonListCell());
//        setCellFactory((ListView<CMdkAuditreason> param) -> new AuditReasonListCell());
    }

//    /**
//     * @param pId select item by database id
//     */
//    public void selectById(Long pId) {
//        if (pId == null || pId.equals(-1L)) {
//            return;
//        }
//        for (CMdkAuditreason item : getItems()) {
//            if (item.getId() == pId) {
//                select(item);
//            }
//        }
//    }
    /**
     * @param pNumber select item by database id
     */
    public void selectByNumber(Long pNumber) {
        if (pNumber == null || pNumber.equals(-1L)) {
            return;
        }
        for (CMdkAuditreason item : getItems()) {
            if (pNumber.equals(item.getMdkArNumber())) {
                select(item);
            }
        }
    }

//    /**
//     * @return get the db id of the currently selected item, null if nothing is
//     * selected
//     */
//    public Long getSelectedId() {
//        return getSelectedItem() != null ? getSelectedItem().getId() : null;
//    }
    /**
     * @return get the db id of the currently selected item, null if nothing is
     * selected
     */
    public Long getSelectedNumber() {
        return getSelectedItem() != null ? getSelectedItem().getMdkArNumber() : null;
    }

//    private class AuditReasonListCell extends ListCell<CMdkAuditreason> {
//
//        @Override
//        protected void updateItem(CMdkAuditreason item, boolean empty) {
//            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
//            if (item == null || empty) {
//                setText("");
//                return;
//            }
//            setText(item.getMdkArName());
//        }
//
//    }
}
