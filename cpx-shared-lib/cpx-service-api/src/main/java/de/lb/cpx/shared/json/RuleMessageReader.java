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

import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author wilde
 */
public class RuleMessageReader extends JsonErrorMessageReader<RuleMessage>{

    private static final Logger LOG = Logger.getLogger(RuleMessageReader.class.getName());
    
    public RuleMessageReader() {
        super(RuleMessage[].class);
    }
    
    public String readRuleForDisplay(CrgRules pRule){
        if(pRule == null || pRule.getCrgrMessage() == null){
            return null;
        }
        try {
            return new RuleMessageReader().read(pRule.getCrgrMessage(), "UTF-8").stream().map((t) -> {
                return new StringBuilder(t.getTerm()).append("-").append(t.getDescription()).toString();
            }).collect(Collectors.joining(","));
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public List<RuleMessage> read(CrgRules pRule){
        if(pRule == null || pRule.getCrgrMessage() == null){
            return null;
        }
        try {
            return new RuleMessageReader().read(pRule.getCrgrMessage(), "UTF-8");
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
