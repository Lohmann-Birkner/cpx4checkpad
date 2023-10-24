/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2017  adameck - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.labeled;

import de.lb.cpx.client.core.model.fx.spinner.NumberSpinner;
import javafx.beans.property.StringProperty;

/**
 *
 * Labeled NumberSpinner class handles a NumberSpinner and an corresponting
 * label
 *
 * @author adameck
 */
public class LabeledNumberSpinner extends LabeledControl<NumberSpinner> {

    public LabeledNumberSpinner() {
        super("Label", new NumberSpinner(0));
    }

    public LabeledNumberSpinner(String pLabel) {
        super(pLabel, new NumberSpinner());
    }

    public LabeledNumberSpinner(String pLabel, NumberSpinner pSpinner) {
        super(pLabel, pSpinner);
    }

    public String getText() {
        return getControl().getText();
    }

    public void setText(final String pText) {
        getControl().setText(pText);
    }

    public StringProperty textProperty() {
        return getControl().textProperty();
    }
    
    @Override
    public NumberSpinner getControl(){
        return super.getControl();
    }
//    
//    public Integer getValue() {
//        return getControl().getValue();
//    }
//    
//    public void setValue(final Integer pValue) {
//        getControl().setInteger(pValue);
//    }
//    
//    public Integer getInteger() {
//        return getControl().getInteger();
//    }
//    
//    public void setInteger(final Integer pValue) {
//        getControl().setInteger(pValue);
//    }
//    
//    public Integer getNumber() {
//        return getControl().getInteger();
//    }
//    
//    public void setNumber(final Number pValue) {
//        getControl().setNumber(pValue.intValue());
//    }

}
