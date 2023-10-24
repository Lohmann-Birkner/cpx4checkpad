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
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 *
 * @author wilde
 * @param <T> table item
 */
public abstract class CurrencyColumn<T> extends TableColumn<T, Number> {

    private static final Logger LOG = Logger.getLogger(CurrencyColumn.class.getName());

    private String emptyText = Lang.toDecimal(0.0d, 2) + " " + Lang.getCurrencySymbol();

    public CurrencyColumn(String pTitle) {
        super(pTitle);
        setCellValueFactory(new DefaultCurrencyCellValueFactory());
        setCellFactory(new DefaultCurrencyCellFactory());
    }

    public abstract Double extractValue(T pValue);

    protected String getDisplayText(Double pValue) {
        Double number = pValue;//extractValue(pValue);
        if (number != null) {
            return /*Lang.toDecimal(number, 2)*/ formatNumber(pValue) + " " + Lang.getCurrencySymbol();
        } else {
            return emptyText;
        }
    }

    public DoubleProperty getProperty() {
        return new SimpleDoubleProperty();
    }

    public void setEmptyText(String pText) {
        emptyText = pText;
    }

    public String getEmptyText() {
        return emptyText;
    }

    protected class DefaultCurrencyCellFactory implements Callback<TableColumn<T, Number>, TableCell<T, Number>> {

        @Override
        public TableCell<T, Number> call(TableColumn<T, Number> param) {
            return new DefaultCurrencyTableCell();
        }
    }

    protected final String formatNumber(Number pNumber) {
        Double number = Double.doubleToRawLongBits(pNumber.doubleValue()) != Double.doubleToRawLongBits(0.0d) ? pNumber.doubleValue() : 0.0d;
        return Lang.toDecimal(number, 2);
    }

    protected class DefaultCurrencyTableCell extends TableCell<T, Number> {

        @Override
        protected void updateItem(Number item, boolean empty) {
            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
            if (item == null || empty) {
                setText("");
                setGraphic(null);
                return;
            }
            setText(getDisplayText(item.doubleValue()));
        }
    }

    protected class DefaultCurrencyCellValueFactory implements Callback<CellDataFeatures<T, Number>, ObservableValue<Number>> {

//        private final Map<T, DoubleProperty> valueMap = new HashMap<>();
        @Override
        public ObservableValue<Number> call(CellDataFeatures<T, Number> param) {
            if (param.getValue() != null) {
                try {
//                    T val = param.getValue();
//                    DoubleProperty property = valueMap.get(val);
//                    if (property == null) {
//                        property = getProperty(val);
//                        valueMap.put(val, property);
//                    }
//                    Double item = extractValue(val);
//
//                    property.set(item != null ? item : 0.0d);

                    T val = param.getValue();
                    DoubleProperty property = getProperty();//valueMap.get(val);
//                    if (property == null) {
//                        property = getProperty(val);
//                        valueMap.put(val, property);
//                    }
                    Double item = extractValue(val);

                    property.set(item != null ? item : 0.0d);
                    return property;
                } catch (ClassCastException ex) {
                    LOG.log(Level.WARNING, "ItemValue is not expected Type!", ex);
                }
            }
            return null;
        }
    }
}
