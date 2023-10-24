/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.labeled;

import de.lb.cpx.client.core.model.fx.textfield.CurrencyTextField;
import de.lb.cpx.shared.lang.Lang;

/**
 *
 * @author wilde
 */
public class LabeledCurrencyTextField extends LabeledPromptedSizedNumberTextField {

    /**
     * no-arg for scenebuilder, default title is: Label
     */
    public LabeledCurrencyTextField() {
        this("LabeledCurrencyTextField", new CurrencyTextField());
    }
    public LabeledCurrencyTextField(String pLabel, CurrencyTextField pCtrl) {
        super(pLabel, pCtrl);
////        setValue(getConverter().fromString(pCtrl.getText()));
//        pCtrl.setMaxHeight(TextField.USE_PREF_SIZE);
//        pCtrl.setMinHeight(TextField.USE_PREF_SIZE);
//        pCtrl.setMinWidth(TextField.USE_PREF_SIZE);
//        pCtrl.setMaxWidth(TextField.USE_PREF_SIZE);
//
////        pCtrl.setPrefWidth(150);
////        pCtrl.setPrefHeight(29);
//        setMaxHeight(TextField.USE_PREF_SIZE);
//        setMinHeight(TextField.USE_PREF_SIZE);
//        setMinWidth(TextField.USE_PREF_SIZE);
//        setMaxWidth(TextField.USE_PREF_SIZE);
//        valueProperty().bindBidirectional(pCtrl.valueProperty());
       
    }
////    private DoubleProperty valueProperty;
//    public final ObjectProperty<Number> valueProperty() {
////        if (valueProperty == null) {
////            valueProperty = new SimpleDoubleProperty();
////        }
////        return valueProperty;
//        return getControl().valueProperty();
//    }
//
    @Override
    public Double getValue() {
        return (Double)valueProperty().get();
    }

//    public final void setValue(Double pValue) {
//        valueProperty().set(pValue);
//    }
    
//    @Override
//    public final CurrencyTextField getControl() {
//        return (CurrencyTextField) super.getControl();
//    }

    @Override
    public void addDefaultLabel(Number value){
        super.addDefaultLabel(Lang.round(value.doubleValue(), 2));
    }
}
