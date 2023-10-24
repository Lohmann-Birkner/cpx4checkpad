/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.core.model.fx.labeled;

import de.lb.cpx.client.core.model.fx.textfield.NumberTextField;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.TextField;

/**
 *
 * @author gerschmann
 * @param <T> number type
 */
public class LabeledSizedNumberTextField extends LabeledTextField{
    
    public LabeledSizedNumberTextField() {
        this("LabeledSizedNumberTextField", new NumberTextField());
    }
    
    public LabeledSizedNumberTextField(String pLabel, NumberTextField pCtrl) {
        super(pLabel, pCtrl);
        pCtrl.setMaxHeight(TextField.USE_PREF_SIZE);
        pCtrl.setMinHeight(TextField.USE_PREF_SIZE);
        pCtrl.setMinWidth(TextField.USE_PREF_SIZE);
        pCtrl.setMaxWidth(TextField.USE_PREF_SIZE);

        setMaxHeight(TextField.USE_PREF_SIZE);
        setMinHeight(TextField.USE_PREF_SIZE);
        setMinWidth(TextField.USE_PREF_SIZE);
        setMaxWidth(TextField.USE_PREF_SIZE);
        pCtrl.prefWidthProperty().bind(widthProperty());
   }
    
    @Override
    public final NumberTextField getControl() {
        return (NumberTextField) super.getControl();
    }
    
    public final ObjectProperty<Number> valueProperty() {
//        if (valueProperty == null) {
//            valueProperty = new SimpleDoubleProperty();
//        }
//        return valueProperty;

        return getControl().valueProperty();
    }

    public final void setValue(Number pValue) {
        valueProperty().set(pValue);
    }
    
    public Number getValue(){
        return valueProperty().get();
    }
}
