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
package de.lb.cpx.client.core.model.fx.tableview.column;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 *
 *
 * @author wilde
 * @param <T> table item
 */
public abstract class IntegerColumn<T> extends TableColumn<T, Integer> {

    private static final Logger LOG = Logger.getLogger(IntegerColumn.class.getName());

    private static final Integer DEFAULT = 0;
    private SimpleObjectProperty<Integer> property;

    public IntegerColumn(String pTitle) {
        super(pTitle);
        setCellValueFactory(new Callback<TableColumn.CellDataFeatures<T, Integer>, ObservableValue<Integer>>() {
//            private final Map<T, ObjectProperty<Integer>> valueMap = new HashMap<>();

            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<T, Integer> param) {
                if (param.getValue() != null) {
                    try {
                        T val = param.getValue();
//                        ObjectProperty<Integer> property = valueMap.get(val);
//                        if (property == null) {
//                            property = getProperty(val);
//                            valueMap.put(val, property);
//                        }
                        property = new SimpleObjectProperty<>();
                        property.set(getDisplayNumber(val));
                        return property;
                    } catch (ClassCastException ex) {
                        LOG.log(Level.WARNING, "ItemValue is not expected Type!", ex);
                    }
                }
                return null;
            }
        });
    }

    protected Integer getDisplayNumber(T pValue) {
        Integer number = extractValue(pValue);
        if (number != null) {
            return number;
        } else {
            return DEFAULT;
        }
    }

//    public ObjectProperty<Integer> getProperty(T pValue) {
//        return new SimpleObjectProperty<>();
//    }
    public abstract Integer extractValue(T pValue);

    public ObjectProperty<Integer> getProperty() {
        return property;
    }

    public void setValue(Integer pValue) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                getProperty().set(pValue);
            }
        });
    }

}
