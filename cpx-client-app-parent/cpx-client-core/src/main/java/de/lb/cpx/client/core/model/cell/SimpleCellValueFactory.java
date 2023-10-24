/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.cell;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * Costum CallBack for a SimpleCellFactory returns in Call a
 * SimpleObjectProperty
 *
 * @author wilde
 * @param <S> type
 * @param <T> type
 */
public class SimpleCellValueFactory<S, T> implements Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>> {

    //private static final Logger LOG = Logger.getLogger(SimpleCellValueFactory.class.getName());
    @Override
    @SuppressWarnings("unchecked")
    public ObservableValue<T> call(TableColumn.CellDataFeatures<S, T> param) {
        S value = param == null ? null : param.getValue();
        if (value == null) {
            return new SimpleObjectProperty<>(null);
        }
        //this type cast from <S> to <T> is actually wrong, but astonishing to everyone it works! (precondition: S and T are of the same type!)
        return new SimpleObjectProperty<>((T) value);
//        TableView<S> tv = param.getTableView();
//        TableColumn<S, T> column = param.getTableColumn();
//        S value = param.getValue();
//        int index = tv.getItems().indexOf(value);
//        T cellData = column.getCellData(index);
//        return new SimpleObjectProperty<>(cellData);
    }

}
