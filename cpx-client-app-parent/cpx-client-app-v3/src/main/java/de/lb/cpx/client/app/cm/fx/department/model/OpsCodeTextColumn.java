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

import de.lb.cpx.client.core.model.catalog.CpxOpsCatalog;
import de.lb.cpx.client.core.util.OverrunHelper;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.shared.lang.Lang;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 *
 * @author wilde
 */
public class OpsCodeTextColumn extends DepartmentWardColumn<TCaseOps, String> {

    public OpsCodeTextColumn() {
        super(Lang.getCaseResolveOPS_TEXT());
//            setMinWidth(50.0d);
        setMaxWidth(Double.MAX_VALUE);
        setSortable(false);
        setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TCaseOps, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TCaseOps, String> param) {
                return new SimpleStringProperty(param.getValue().getOpscCode());
            }
        });
        setCellFactory(new Callback<TableColumn<TCaseOps, String>, TableCell<TCaseOps, String>>() {
            @Override
            public TableCell<TCaseOps, String> call(TableColumn<TCaseOps, String> param) {
                return new TableCell<TCaseOps, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        if (item == null || empty) {
                            setGraphic(null);
                            return;
                        }
//                        int year = Lang.toYear(objectConverter.to(getCurrentDetails()).getCsdAdmissionDate());
//                        String desc = CpxOpsCatalog.instance().getDescriptionByCode(item, "de", year);
//                        if (desc == null || desc.isEmpty()) {
//                            desc = Lang.getCatalogOpsError(String.valueOf(year));
//                        }
                        String desc = getOpsConverter().toString(getTableRow()!=null?getTableRow().getItem():null);
                        Label label = new Label(desc);
                        OverrunHelper.addOverrunInfoButton(label);
                        setGraphic(label);
                    }
                };
            }
        });
    }
    
    private final StringConverter<TCaseOps> DEFAULT_OPS_CONVERTER = new StringConverter<TCaseOps>() {
        @Override
        public String toString(TCaseOps t) {
            if(t == null){
                return null;
            }
            return t.getOpscCode();
        }

        @Override
        public TCaseOps fromString(String string) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    };
    private StringConverter<TCaseOps> opsConverter = DEFAULT_OPS_CONVERTER;
    public void setOpsConverter(StringConverter<TCaseOps> pConverter){
        opsConverter = Objects.requireNonNullElse(pConverter, DEFAULT_OPS_CONVERTER);
    }
    public StringConverter<TCaseOps> getOpsConverter(){
        return opsConverter;
    }
}
    
