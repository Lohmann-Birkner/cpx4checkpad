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
package de.lb.cpx.shared.json;

import de.lb.cpx.shared.json.enums.MessageReasonEn;

/**
 *
 * @author wilde
 */
public class RuleTableMessageBuilder extends JsonErrorMessageBuilder<RuleTableMessage>{
    
    public RuleTableMessageBuilder() {
        super(new RuleTableMessage());
    }
    
    public RuleTableMessageBuilder setCodes(String pCodes){
        message.setCodes(pCodes);
        return this;
    }

    @Override
    public RuleTableMessageBuilder add(RuleTableMessage pMessage) {
        return (RuleTableMessageBuilder) super.add(pMessage); //To change body of generated methods, choose Tools | Templates.
    }
    
    public RuleTableMessageBuilder add(){
        return add(new RuleTableMessage());
    }
    @Override
    public RuleTableMessageBuilder setSeverity(String pSeverity) {
        return (RuleTableMessageBuilder) super.setSeverity(pSeverity); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public RuleTableMessageBuilder setReason(MessageReasonEn pReason) {
        return (RuleTableMessageBuilder) super.setReason(pReason); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public RuleTableMessageBuilder setDescription(String pDescription) {
        return (RuleTableMessageBuilder) super.setDescription(pDescription); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RuleTableMessageBuilder setType(String pType) {
        return (RuleTableMessageBuilder) super.setType(pType); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public RuleTableMessageBuilder setSrcYear(int pYear){
        return (RuleTableMessageBuilder) super.setSrcYear(pYear);

    }
    
    @Override
    public RuleTableMessageBuilder setDestYear(int pYear){
        return (RuleTableMessageBuilder) super.setDestYear(pYear);
    }
        
    
}
