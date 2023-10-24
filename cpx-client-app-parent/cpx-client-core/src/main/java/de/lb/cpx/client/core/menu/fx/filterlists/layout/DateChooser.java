/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.menu.fx.filterlists.layout;

import de.lb.cpx.client.core.model.fx.datepicker.DateConverter;
import de.lb.cpx.client.core.model.fx.labeled.LabeledDatePicker;
import de.lb.cpx.shared.filter.enums.SearchListAttribute;
import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Control;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.util.Callback;

/**
 * chooser for date objects
 *
 * @author wilde
 */
public class DateChooser extends FilterOptionChooser<LocalDate, LabeledDatePicker> {

    public DateChooser(SearchListAttribute pAttribute) {
        super(pAttribute);
    }

    @Override
    public Control registerControl(SearchListAttribute pAttribute) {
        Control ctrl = getControlFactory().call(pAttribute);
        ctrl.setDisable(true);
        ctrl.disabledProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    if (getSingleFilterDelete() != null) {
                        getSingleFilterDelete().call(pAttribute.getKey());//pAttribute.getKey());
                    }
                    if (ctrl instanceof LabeledDatePicker) {
                        DatePicker picker = ((LabeledDatePicker) ctrl).getControl();
                        isDirty = isDirty ? isDirty : picker.getValue() != null;
                        picker.setValue(null);
                    }
                    if (ctrl instanceof Label) {
                        isDirty = true;
                    }
                }
            }
        });
        return ctrl;
    }

    @Override
    public Boolean hasValue(Control ctrl) {
        if (ctrl instanceof LabeledDatePicker) {
            if (((LabeledDatePicker) ctrl).getControl().getValue() != null) {
                return true;
            }
        }
        if (ctrl instanceof Label) {
            if (((Label) ctrl).getUserData() != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void registerLowerThreshold(LabeledDatePicker pCtrl, ObjectProperty<LocalDate> pValueProperty) {
        pCtrl.getControl().setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        LocalDate date = pValueProperty.get();
                        if (date == null) {
                            return;
                        }
                        if (item.isAfter(date)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        });
        DateConverter converter = new DateConverter();
        converter.maxDateProperty().bind(pValueProperty);
        converter.setValidationErrorCallback(new Callback<String, Void>() {
            @Override
            public Void call(String param) {
                showErrorPopOver(pCtrl.getControl(), param, 5);
                return null;
            }
        });
        pCtrl.getControl().setConverter(converter);
    }

    @Override
    public void registerUpperThreshold(LabeledDatePicker pCtrl, ObjectProperty<LocalDate> pValueProperty) {
        pCtrl.getControl().setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        LocalDate date = pValueProperty.get();
                        if (date == null) {
                            return;
                        }
                        if (item.isBefore(date)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        });
        DateConverter converter = new DateConverter();
        converter.minDateProperty().bind(pValueProperty);
        pCtrl.getControl().setConverter(converter);
        converter.setValidationErrorCallback(new Callback<String, Void>() {
            @Override
            public Void call(String param) {
                showErrorPopOver(pCtrl, param, 5);
                return null;
            }
        });
    }

    @Override
    public ObjectProperty<LocalDate> getValueProperty(LabeledDatePicker pCtrl) {
        return pCtrl.getControl().valueProperty();
    }

}
