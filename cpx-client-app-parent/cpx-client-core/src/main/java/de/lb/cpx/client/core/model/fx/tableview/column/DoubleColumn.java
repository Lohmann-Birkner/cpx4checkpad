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

import de.lb.cpx.shared.lang.Lang;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 *
 * @author wilde
 * @param <T> table item
 */
public abstract class DoubleColumn<T> extends TableColumn<T, String> {

    private static final Logger LOG = Logger.getLogger(DoubleColumn.class.getName());

    private static final Double DEFAULT = 0.0d;
    private final Integer precision;
    private SimpleObjectProperty<String> property;

    public DoubleColumn(String pTitle, Integer pPrecision) {
        super(pTitle);
        precision = pPrecision;
        setCellValueFactory(new Callback<TableColumn.CellDataFeatures<T, String>, ObservableValue<String>>() {
//            private final Map<T, ObjectProperty<String>> valueMap = new HashMap<>();

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<T, String> param) {
                if (param.getValue() != null) {
                    try {
                        T val = param.getValue();
//                        ObjectProperty<String> property = valueMap.get(val);
//                        if (property == null) {
//                            property = getProperty(val);
//                            valueMap.put(val, property);
//                        }
                        property = new SimpleObjectProperty<>();
                        property.set(getDisplayText(val));
                        return property;
                    } catch (ClassCastException ex) {
                        LOG.log(Level.WARNING, "ItemValue is not expected Type!", ex);
                    }
                }
                return null;
            }
        });
    }

    protected String getDisplayText(T pValue) {
        Double number = extractValue(pValue);
        if (number != null) {
            return Lang.toDecimal(number, precision);
        } else {
            return Lang.toDecimal(DEFAULT, precision);
        }
    }

//    public ObjectProperty<String> getProperty(T pValue) {
//        return new SimpleObjectProperty<>();
//    }
    public ObjectProperty<String> getProperty() {
        return property;
    }

    public abstract Double extractValue(T pValue);

}
