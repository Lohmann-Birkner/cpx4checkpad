/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.core.model.fx.labeled;

import de.lb.cpx.client.core.model.fx.textfield.DoubleTextField;

/**
 *
 * @author gerschmann
 */
public class LabeledDoubleTextField extends LabeledSizedNumberTextField {
    
    /**
     * no-arg constructor for scene builder
     */
    public LabeledDoubleTextField() {
        this("LabeledDoubleTextField", new DoubleTextField());
    }

    /**
     * creates new instance
     *
     * @param pLabel label text
     * @param pTextField number textfield to display
     */
    public LabeledDoubleTextField(String pLabel, DoubleTextField pTextField) {
        super(pLabel, pTextField);

    }

//    @Override
//    public final PercentTextField getControl() {
//        return (PercentTextField) super.getControl();
//    }
//
//    public final ObjectProperty<Number> valueProperty() {
////        if (valueProperty == null) {
////            valueProperty = new SimpleDoubleProperty();
////        }
////        return valueProperty;
//        return getControl().valueProperty();
//    }

    @Override
    public Double getValue() {
       if(valueProperty().get() == null){
            return 0.0;
        }
        return valueProperty().get().doubleValue();
    }
}
