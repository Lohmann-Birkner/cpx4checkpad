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
public class RuleMessage extends RuleTableMessage{
    private String term;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
    
    
    public void merge4Term(RuleMessage pOther){
        if(pOther == null){
            return;
        }
        setTerm(pOther.getTerm()); // term syntax could be changed already when table id was replaced with table name 
        if(getCodes() != null && pOther.getCodes() != null && getCodes().equals(pOther.getCodes())){
            return;
        }
        setCodes(getCodes() == null ?(pOther.getCodes() == null?"":pOther.getCodes()):
                (getCodes() + (pOther.getCodes() == null?"":("," + pOther.getCodes()))));
        setDescription(getDescription() == null?(pOther.getDescription() == null?"":pOther.getDescription() ):
                (getDescription() + (pOther.getDescription() == null?"":( "\n" + pOther.getDescription()))));
        if(!getReason().equals(pOther.getReason())){
            setReason(MessageReasonEn.VALIDATION_SUGG_MERGED_REASON);
        }
        
    }
    @Override
    public String toString(){
        return "term:" + (term == null?"null":term) + "\n" +
                "codes:" + (getCodes() == null?"null":getCodes()) +"\n" +
                "description:" + (getDescription() == null?"null":getDescription()) + "\n" +
                "reason:" + (getReason() == null?"null":getReason().toString());
    }
}
