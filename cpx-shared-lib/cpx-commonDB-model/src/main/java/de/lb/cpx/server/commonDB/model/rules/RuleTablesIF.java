/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.server.commonDB.model.rules;

import de.lb.cpx.server.commonDB.model.enums.RuleTableCategoryEn;

/**
 *
 * @author gerschmann
 */
public interface RuleTablesIF {
    public boolean hasErrorMessage();
    public String getRuleTableName();
    public RuleTableCategoryEn getRuleTableCategory();
    public void setRuleTableMessage(byte[] msg);
    public String getRuleTableContent();
    
}
