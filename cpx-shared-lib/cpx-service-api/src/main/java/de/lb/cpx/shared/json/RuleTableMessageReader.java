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

import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.shared.json.enums.MessageReasonEn;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author wilde
 */
public class RuleTableMessageReader extends JsonErrorMessageReader<RuleTableMessage>{
    
    public RuleTableMessageReader() {
        super(RuleTableMessage[].class);
    }
    
    
    public String getRuleTableMessageTooltip(CrgRuleTables pRuleTable){
        if(pRuleTable == null){
            return null;
        }
        if(pRuleTable.getCrgtMessage() == null){
            return null;
        }
        try {
            return read(pRuleTable.getCrgtMessage(), "UTF-8").stream().map((t) -> {
                return t.getDescription();
            }).collect(Collectors.joining("\n"));
        } catch (IOException ex) {
            Logger.getLogger(RuleTableMessageReader.class.getName()).log(Level.SEVERE, null, ex);
            return new StringBuilder("Parsing of Json-Object failed! Reason:\n").append(ex.getMessage()).toString();
        }
    }
    
    public String readUtf8AndGetCodes(byte[] pJsonObject) throws IOException{
        RuleTableMessage msg = readSingleResultOrNull(pJsonObject, "UTF-8");
        return msg!=null?msg.getCodes():null;
    }
    
    public int readUtf8AndGetSrcYear(byte[]  pJsonObject) throws IOException{
        List<RuleTableMessage> msgs = readUtf8(pJsonObject);
        if(msgs != null && msgs.size() > 0){
            return msgs.get(0).getSrcYear();
        }
        return 0;
    }

    public  MessageReasonEn readUtf8AndGetReason(byte[]  pJsonObject) throws IOException{
        List<RuleTableMessage> msgs = readUtf8(pJsonObject);
        if(msgs != null && msgs.size() > 0){
            return msgs.get(0).getReason();
        }
        return MessageReasonEn.VALIDATION_RULE_NOT_VALID;
    }
    public List<RuleMessage> read(CrgRuleTables pTable){
        if(pTable == null || pTable.getCrgtMessage() == null){
            return null;
        }
        try {
            return new RuleMessageReader().read(pTable.getCrgtMessage(), "UTF-8");
        } catch (IOException ex) {
            Logger.getLogger(RuleTableMessageReader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
