/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.rulefilter.model.table;

import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Skin;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;

/**
 * CheckBoxTableCell with bidirectional binding..
 *
 * @author nandola
 */
public class CreateCheckBoxTableCell extends TableCell<CrgRules, Boolean> {

    private CheckBox box;

    public CreateCheckBoxTableCell() {
        // for each cell create a CheckBox..
        box = new CheckBox();

        box.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
                TableRow<CrgRules> row = getTableRow();
                if (row != null) {
//                    if (newValue) {
                    if (box.isSelected()) {
                        getTableView().getSelectionModel().select(row.getIndex());
                    } else {
                        getTableView().getSelectionModel().clearSelection(row.getIndex());
                    }
                }
            }
        });

//        selectedProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
//                LOG.log(Level.INFO, "change selection: {0}", t1);
//            }
//        });
        tableRowProperty().addListener(new ChangeListener<TableRow<CrgRules>>() {
            @Override
            public void changed(ObservableValue<? extends TableRow<CrgRules>> ov, TableRow<CrgRules> t, TableRow<CrgRules> t1) {
                tableRowProperty().removeListener(this);
            }
        });
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        Skin<?> sk = super.createDefaultSkin();

        getTableRow().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                box.setSelected(t1);
//                LOG.log(Level.INFO, "row is selected {0}", t1);
            }
        });
        return sk;
    }

    @Override
    protected void updateItem(Boolean t, boolean bln) {
        super.updateItem(t, bln); //To change body of generated methods, choose Tools | Templates.
        if (t == null || bln) {
            setGraphic(null);
            return;
        }
        setGraphic(box);
    }
}
