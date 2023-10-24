/*
 * Copyright (c) 2021 Lohmann & Birkner.
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
 *    2021  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.department.model;

import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.shared.lang.Lang;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 *
 * @author wilde
 */
public class OpsCodeColumn extends DepartmentWardColumn<TCaseOps, String> {

    OpsCodeColumn() {
        super(Lang.getCaseResolveOPS());
        setMinWidth(80.0d);
        setMaxWidth(80.0d);
        setResizable(false);
        setSortable(false);
        setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TCaseOps, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TCaseOps, String> param) {
                return new SimpleStringProperty(param.getValue().getOpscCode());
            }
        });
    }

}
