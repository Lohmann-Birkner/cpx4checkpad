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
public class RuleMessageBuilder extends JsonErrorMessageBuilder<RuleMessage>{
    
    public RuleMessageBuilder() {
        super(new RuleMessage());
    }
    
    public RuleMessageBuilder setTerm(String pTerm){
        message.setTerm(pTerm);
        return this;
    }
    
    public RuleMessageBuilder setCodes(String pCodes){
        message.setCodes(pCodes);
        return this;
    }
    
    public RuleMessageBuilder add(){
        return add(new RuleMessage());
    }
    
    @Override
    public RuleMessageBuilder add(RuleMessage pMessage) {
        return (RuleMessageBuilder) super.add(pMessage); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RuleMessageBuilder setSeverity(String pSeverity) {
        return (RuleMessageBuilder) super.setSeverity(pSeverity); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public RuleMessageBuilder setReason(MessageReasonEn pReason) {
        return (RuleMessageBuilder) super.setReason(pReason); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public RuleMessageBuilder setDescription(String pDescription) {
        return (RuleMessageBuilder) super.setDescription(pDescription); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RuleMessageBuilder setType(String pType) {
        return (RuleMessageBuilder) super.setType(pType); //To change body of generated methods, choose Tools | Templates.
    }

        
    @Override
    public RuleMessageBuilder  setSrcYear(int pYear){
        return (RuleMessageBuilder) super.setSrcYear(pYear);

    }
    
    @Override
    public RuleMessageBuilder  setDestYear(int pYear){
        return (RuleMessageBuilder) super.setDestYear(pYear);

    }
    

    
}
