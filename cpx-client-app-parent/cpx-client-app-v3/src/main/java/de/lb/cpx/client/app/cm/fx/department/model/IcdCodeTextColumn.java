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

import de.lb.cpx.client.core.model.catalog.CpxIcdCatalog;
import de.lb.cpx.client.core.util.OverrunHelper;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.shared.lang.Lang;
import java.util.Objects;
import javafx.beans.property.SimpleObjectProperty;
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
public class IcdCodeTextColumn extends TableColumn<TCaseIcd, String> {

    IcdCodeTextColumn() {
        super(Lang.getCaseResolveICD_Text());
//            setMinWidth(50.0d);
        setMaxWidth(Double.MAX_VALUE);
        setSortable(false);
        setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TCaseIcd, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TCaseIcd, String> param) {
                return new SimpleObjectProperty<>(param.getValue().getIcdcCode());
            }
        });
        setCellFactory(new Callback<TableColumn<TCaseIcd, String>, TableCell<TCaseIcd, String>>() {
            @Override
            public TableCell<TCaseIcd, String> call(TableColumn<TCaseIcd, String> param) {
                return new TableCell<TCaseIcd, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        if (item == null || empty) {
                            setGraphic(null);
                            return;
                        }

//                        int year = Lang.toYear(objectConverter.to(getCurrentDetails()).getCsdAdmissionDate());
//                        String icdText = CpxIcdCatalog.instance().getDescriptionByCode(item, "de", year);
                        Label lbIcdText = new Label(getIcdConverter().toString(getTableRow()!=null?getTableRow().getItem():null));
                        OverrunHelper.addOverrunInfoButton(lbIcdText);
                        setGraphic(lbIcdText);
                    }

                };
            }
        });
    }
    
    private final StringConverter<TCaseIcd> DEFAULT_ICD_CONVERTER = new StringConverter<TCaseIcd>() {
        @Override
        public String toString(TCaseIcd t) {
            if(t == null){
                return null;
            }
            return t.getIcdcCode();
        }

        @Override
        public TCaseIcd fromString(String string) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    };
    private StringConverter<TCaseIcd> icdConverter = DEFAULT_ICD_CONVERTER;
    public void setIcdConverter(StringConverter<TCaseIcd> pConverter){
        icdConverter = Objects.requireNonNullElse(pConverter, DEFAULT_ICD_CONVERTER);
    }
    public StringConverter<TCaseIcd> getIcdConverter(){
        return icdConverter;
    }
}
