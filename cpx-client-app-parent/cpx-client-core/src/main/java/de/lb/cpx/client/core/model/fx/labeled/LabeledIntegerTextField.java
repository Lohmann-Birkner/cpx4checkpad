/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.core.model.fx.labeled;

import de.lb.cpx.client.core.model.fx.textfield.IntegerTextField;

/**
 *
 * @author gerschmann
 */
public class LabeledIntegerTextField extends LabeledSizedNumberTextField {

    public LabeledIntegerTextField() {
        this("LabeledIntegerTextField", new IntegerTextField());
    }

    public LabeledIntegerTextField(String pLabel, IntegerTextField pCtrl) {
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
////        valueProperty().bindBidirectional(pCtrl.valueProperty());

    }

    @Override
    public Integer getValue() {
        if(valueProperty().get() == null){
            return 0;
        }
        return valueProperty().get().intValue();
    }

//    public final void setValue(Integer pValue) {
//        valueProperty().set(pValue);
//    }
//    
////    @Override
//    public final IntegerTextField getControl() {
//        return (IntegerTextField) super.getControl();
//    }    
}
