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
package de.lb.cpx.ruleviewer.model.control;

import de.lb.cpx.shared.json.RuleMessage;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;

/**
 *
 * @author wilde
 */
public class EditorControl extends Control{
    
    private ObjectProperty<RuleMessage> ruleMessageProperty;
    public final ObjectProperty<RuleMessage> ruleMessageProperty(){
        if(ruleMessageProperty == null){
            ruleMessageProperty = new SimpleObjectProperty<>();
        }
        return ruleMessageProperty;
    }
    public void setRuleMessage(RuleMessage pMessage) {
        ruleMessageProperty().set(pMessage);
    }
    public RuleMessage getRuleMessage(){
        return ruleMessageProperty().get();
    }
}
