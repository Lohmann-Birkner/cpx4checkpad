/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.core.model.fx.labeled;

import de.lb.cpx.client.core.model.fx.textfield.PercentTextField;

/**
 *
 * @author gerschmann
 */
public class LabeledPercentTextField extends LabeledPromptedSizedNumberTextField {

    /**
     * no-arg constructor for scene builder
     */
    public LabeledPercentTextField() {
        this("LabeledPercentTextField", new PercentTextField());
    }

    /**
     * creates new instance
     *
     * @param pLabel label text
     * @param pTextField number textfield to display
     */
    public LabeledPercentTextField(String pLabel, PercentTextField pTextField) {
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
    public Integer getValue() {
        return (Integer)valueProperty().get();
    }

//    public final void setValue(Integer pValue) {
//        valueProperty().set(pValue);
//    }
}
