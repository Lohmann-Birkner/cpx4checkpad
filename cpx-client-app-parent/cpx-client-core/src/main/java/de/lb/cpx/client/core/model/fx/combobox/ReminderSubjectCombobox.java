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
import de.lb.cpx.server.commonDB.model.CWmListReminderSubject;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * Combobox to display CWMLISTREMINDERSUBJECT
 *
 * @author shahin
 */
public class ReminderSubjectCombobox extends LabeledComboBox<CWmListReminderSubject> {

    /**
     * no arg constructor for scene builder
     */
    public ReminderSubjectCombobox() {
        this("Label");
    }

    /**
     * create new instance of the labeledcombobox with the given title
     *
     * @param pTitle title of the combobox
     */
    public ReminderSubjectCombobox(String pTitle) {
        super(pTitle);
        setButtonCell(new ProcessResultListCell());
        setCellFactory(new Callback<ListView<CWmListReminderSubject>, ListCell<CWmListReminderSubject>>() {
            @Override
            public ListCell<CWmListReminderSubject> call(ListView<CWmListReminderSubject> param) {
                return new ProcessResultListCell();
            }
        });
    }

    /**
     * @param pInternalId select item by db id
     */
    public void selectByInternalId(Long pInternalId) {
        if (pInternalId == null || pInternalId.equals(-1L)) {
            return;
        }
        for (CWmListReminderSubject item : getItems()) {
            if (item.getWmRsInternalId() == pInternalId) {
                select(item);
            }
        }
    }

    /**
     * @return selected id of the process result, null if nothing is selected
     */
    public Long getSelectedInternalId() {
        return getSelectedItem() != null ? getSelectedItem().getWmRsInternalId() : null;
    }

    private class ProcessResultListCell extends ListCell<CWmListReminderSubject> {

        @Override
        protected void updateItem(CWmListReminderSubject item, boolean empty) {
            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
            if (item == null || empty) {
                setText("");
                return;
            }
            setText(item.getWmRsName());
        }

    }
}
