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
package de.lb.cpx.ruleviewer.properties.risk;

import de.lb.cpx.client.core.model.fx.textfield.NumberTextField;
import de.lb.cpx.client.core.model.fx.textfield.PercentTextField;
import java.util.Objects;
import org.controlsfx.control.PropertySheet;

/**
 *
 * @author wilde
 */
public class RiskAuditValueEditor extends RiskNumberEditor{

    public RiskAuditValueEditor(PropertySheet.Item property) {
        super(property);
    }

    @Override
    public NumberTextField getNumberEditor() {
        return new PercentTextField();
    }

    @Override
    public void setNumberValue(Number pValue) {
        Integer intVal = pValue!=null?pValue.intValue():null;
        getRiskItem().setRiskAuditValue(intVal!=null?String.valueOf(intVal):"");
    }

    @Override
    public Number getNumberValue() {
        String value = Objects.requireNonNullElse(getRiskItem().getRiskAuditValue(),"0");
        if(value.isEmpty()){
            return null;
        }
        int val = Double.valueOf(value).intValue();
        return val;
    }
//    private PercentTextField valueTextField;
//
//    public RiskAuditValueEditor(PropertySheet.Item property) {
//        super(property);
//    }
//
//    @Override
//    public Node getEditor() {
//        if (valueTextField == null) {
//            valueTextField = new PercentTextField();
//            valueTextField.setMaxWidth(Double.MAX_VALUE);
//            valueTextField.setMinWidth(70);
//            valueTextField.setPrefWidth(150);
//            valueTextField.valueProperty().addListener(new ChangeListener<Number>() {
//                @Override
//                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                    getRiskItem().setRiskAuditValue(String.valueOf(newValue.intValue()));
//                    getRiskItem().getProperties().put(RiskEditorItem.UPDATE_RISK, null);
//                    RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), this);
//                    Event.fireEvent(valueTextField, saveEvent);
//                }
//            });
//            getRiskItem().getProperties().addListener(new MapChangeListener<String, Object>() {
//                @Override
//                public void onChanged(MapChangeListener.Change<? extends String, ? extends Object> change) {
//                    if(change.wasAdded()){
//                        if(RiskEditorItem.UPDATE_RISK.equals(change.getKey())){
//                            valueTextField.setDisable(!getRiskItem().hasAreas());
//                        }
//                    }
//                }
//            });
//        }
//        return valueTextField;
//    }
//
//    @Override
//    public String getValue() {
//        getEditor();
//        return valueTextField.getValue().toString();
//    }
//
//    @Override
//    public void setValue(String value) {
//        getEditor();
//        int val = Double.valueOf(Objects.requireNonNullElse(getRiskItem().getRiskAuditValue(),"0")).intValue();
//        valueTextField.setValue(val);
//    }
}
