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

import de.lb.cpx.client.core.model.fx.labeled.LabeledFilterTextField;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.client.core.model.fx.textfield.DoubleConverter;
import de.lb.cpx.shared.filter.enums.SearchListAttribute;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Callback;

/**
 *
 * @author wilde
 */
public class DoubleChooser extends FilterOptionChooser<Double, LabeledFilterTextField<Double>> {

    public DoubleChooser(SearchListAttribute pAttribute) {
        super(pAttribute);
    }

//    private LabeledDoubleTextField findTextField(Control ctrl) {
//        if (ctrl instanceof LabeledDoubleTextField) {
//            return (LabeledDoubleTextField) ctrl;
//        }
//        return null;
//    }
    @Override
    public void registerLowerThreshold(LabeledFilterTextField<Double> pCtrl, ObjectProperty<Double> pValueProperty) {
        DoubleConverter converter = new DoubleConverter();
        converter.maxValueProperty().bind(pValueProperty);
        converter.setValidationErrorCallback(new Callback<String, Void>() {
            @Override
            public Void call(String param) {
                showErrorPopOver(pCtrl, param, 5);
                return null;
            }
        });
        pCtrl.setConverter(converter);
    }

    @Override
    public void registerUpperThreshold(LabeledFilterTextField<Double> pCtrl, ObjectProperty<Double> pValueProperty) {
        DoubleConverter converter = new DoubleConverter();
        converter.minValueProperty().bind(pValueProperty);
        pCtrl.setConverter(converter);
        converter.setValidationErrorCallback(new Callback<String, Void>() {
            @Override
            public Void call(String param) {
                showErrorPopOver(pCtrl, param, 5);
                return null;
            }
        });
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
                    if (ctrl instanceof LabeledTextField) {
                        TextField textField = ((LabeledTextField) ctrl).getControl();
                        isDirty = isDirty ? isDirty : textField.getText() != null;
                        textField.setText(null);
                    }
                    if (ctrl instanceof Label) {
                        isDirty = true;
                    }
                }
//                if (newValue) {
//                    if (getSingleFilterDelete() != null) {
//                        getSingleFilterDelete().call(pAttribute.getKey());//pAttribute.getKey());
//                    }
//                    LabeledDoubleTextField field = findTextField(ctrl);
//                    if (field != null) {
//                        isDirty = field.getValue() != null;
//                        field.setValue(null);
//                    }
//                }
            }
        });
        return ctrl;
    }

    @Override
    public Boolean hasValue(Control ctrl) {
        if (ctrl instanceof LabeledFilterTextField) {
            if (((LabeledFilterTextField) ctrl).getValue() != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ObjectProperty<Double> getValueProperty(LabeledFilterTextField<Double> pCtrl) {
        return pCtrl.valueProperty();
    }

}
