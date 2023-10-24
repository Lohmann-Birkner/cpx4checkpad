/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.core.model.fx.labeled;

import de.lb.cpx.client.core.model.fx.textfield.NumberTextField;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author gerschmann
 */
public class LabeledPromptedSizedNumberTextField extends LabeledSizedNumberTextField{
    
    Label lblDefaultValue;

    
    public LabeledPromptedSizedNumberTextField() {
        this("LabeledPromptedSizedNumberTextField", new NumberTextField());
    }
    
    public LabeledPromptedSizedNumberTextField(String pLabel, NumberTextField pCtrl) {
        super(pLabel, pCtrl);

    }
    

    
    public void addDefaultLabel(Number value){
        if(lblDefaultValue == null){
            getControl().prefWidthProperty().unbind();
            lblDefaultValue = new Label();
//            lblDefaultValue.setStyle("-fx-text-fill:white;-fx-background-color:#86a897;");
            lblDefaultValue.getStyleClass().add("labeled-control-label-prompt");
            lblDefaultValue.setMaxHeight(TextField.USE_PREF_SIZE);
            lblDefaultValue.setMinHeight(TextField.USE_PREF_SIZE);
            lblDefaultValue.setMinWidth(TextField.USE_PREF_SIZE);
            lblDefaultValue.setMaxWidth(TextField.USE_PREF_SIZE);

            lblDefaultValue.prefHeightProperty().bind(getControl().heightProperty());
            lblDefaultValue.prefWidthProperty().bind(widthProperty().divide(2));
            getControl().prefWidthProperty().bind(widthProperty().divide(2));
            ((LabeledTextFieldSkin)getSkin()).getContent().getChildren().add(0, lblDefaultValue);
        }
        lblDefaultValue.setText("  " + getControl().checkTextDigits(getControl().getConverter().toString(value)));
    }
    
    public void setDefaultValue(Number value){
        addDefaultLabel(value);
    }
    
    public Label getDefaultLabel(){
        return lblDefaultValue;
    }
}
