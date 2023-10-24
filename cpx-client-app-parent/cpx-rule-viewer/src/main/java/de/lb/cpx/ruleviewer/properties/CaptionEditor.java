/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.properties;

import de.lb.cpx.client.core.model.fx.tooltip.CpxTooltip;
import de.lb.cpx.client.core.util.TextLimiter;
import de.lb.cpx.rule.element.CaseRuleManager;
import java.util.logging.Logger;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.controlsfx.control.PropertySheet;

/**
 * Editor to edit caption, casts text to xml text
 *
 * @author wilde
 */
public class CaptionEditor extends StringEditor {

    private static final Logger LOG = Logger.getLogger(CaptionEditor.class.getName());
    
    public CaptionEditor(PropertySheet.Item property) {
        super(property);
        new TextLimiter(255).setTextInputControl(getTextField()).buildAndApply();
//        getTextField().widthProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
//                setOverrunTooltip(getTextField());
//                getTextField().widthProperty().removeListener(this);
//            }
//        });
//        getTextField().focusedProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
//                if(!t1){
//                    setOverrunTooltip(getTextField());
//                }
//            }
//        });
    }
    protected final void setOverrunTooltip(TextField pTextField) {
        if(pTextField == null){
            return;
        }
        pTextField.setTooltip(isContentOverrunWidth(pTextField)?new CpxTooltip(pTextField.getText(), 100, 5000, 100, true):null);
    }
    protected final boolean isContentOverrunWidth(TextField pTextField){
        if(pTextField == null){
            LOG.warning("Overrun-Check Failed: Textfield-Node is null!");
            return false;
        }
        if(pTextField.getWidth() == 0.0d){
            LOG.warning("Overrun-Check Failed: Textfield-Node might not be shown in the Scenegraph yet!");
            return false;
        }
        if(pTextField.lookup(".text") == null){
            LOG.warning("Overrun-Check Failed: Text-Node on Textfield might not be instantiated, skin is properly not set yet!");
            return false;
        }
        Text text = (Text) pTextField.lookup(".text");
        double textWidth = text.getBoundsInParent().getWidth();
        double nodeWidth = pTextField.getWidth();
        return (textWidth+10) > nodeWidth;
    }
    @Override
    public String getValue() {
        return CaseRuleManager.getXMLText(super.getValue()); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setValue(String value) {
        super.setValue((CaseRuleManager.getDisplayText(value))); //To change body of generated methods, choose Tools | Templates.
    }

}
