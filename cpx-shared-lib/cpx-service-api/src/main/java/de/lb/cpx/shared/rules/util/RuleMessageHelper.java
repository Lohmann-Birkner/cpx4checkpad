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
package de.lb.cpx.shared.rules.util;

import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.shared.json.JsonErrorMessage;
import de.lb.cpx.shared.json.RuleMessage;
import de.lb.cpx.shared.json.RuleMessageReader;
import de.lb.cpx.shared.json.RuleTableMessage;
import de.lb.cpx.shared.json.RuleTableMessageReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author wilde
 */
public class RuleMessageHelper {
    
    public static final boolean diffSrcAndDestYear(JsonErrorMessage pMessage){
        return Integer.compare(pMessage.getSrcYear(),pMessage.getDestYear())!=0;
    }
    
    public static final boolean hasTransferCatalogError(List<? extends JsonErrorMessage> pMessages){
        pMessages = Objects.requireNonNullElse(pMessages, new ArrayList<>());
        for(JsonErrorMessage message : pMessages){
            if(diffSrcAndDestYear(message)){
                return true;
            }
        }
        return false;
    }
    
    public static final boolean hasTransferCatalogError(CrgRules pRule){
        return hasTransferCatalogError(new RuleMessageReader().read(pRule));
    }
    
    public static final boolean hasTransferCatalogError(CrgRuleTables pTable){
        return hasTransferCatalogError(new RuleTableMessageReader().read(pTable));
    }
    
    public static final JsonErrorMessage getFirstTransferCatalogError(List<? extends JsonErrorMessage> pMessages){
        pMessages = Objects.requireNonNullElse(pMessages, new ArrayList<>());
        for(JsonErrorMessage message : pMessages){
            if(diffSrcAndDestYear(message)){
                return message;
            }
        }
        return null;
    }
    
    public static final RuleMessage getFirstTransferCatalogError(CrgRules pRule){
        return (RuleMessage) getFirstTransferCatalogError(new RuleMessageReader().read(pRule));
    }
    
    public static final RuleTableMessage getFirstTransferCatalogError(CrgRuleTables pTable){
        return (RuleTableMessage) getFirstTransferCatalogError(new RuleTableMessageReader().read(pTable));
    }
}
