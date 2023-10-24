/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.layout;

import de.lb.cpx.ruleviewer.facade.RuleViewFacade;
import de.lb.cpx.ruleviewer.model.Element;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.shared.json.RuleMessage;
import java.util.List;
import javafx.scene.control.Skin;

/**
 * View of the Rule, displays rule xml
 *
 * @author wilde
 */
public class RuleView extends BasicEditorView {

    public static final String DEFAULT_STYLE_CLASS = "rule-view";
    private List<RuleMessage> messages;

    /**
     * construct new rule view to display a xml rule
     *
     * @param pPool pool
     * @param pRule rule
     */
    public RuleView(CrgRulePools pPool, CrgRules pRule) {
        super();
        getStyleClass().add(0, DEFAULT_STYLE_CLASS);
        setCrgRules(pRule);
        setCrgRulePools(pPool);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new RuleViewSkin(this);
    }
    //TODO: REMOVE! when server connection is established there should be another solution
    //static facade to access test data 
    private static final RuleViewFacade RULE_FACADE = new RuleViewFacade();

    /**
     * @return get static facade
     */
    public static RuleViewFacade getFacade() {
        return RULE_FACADE;
    }
//    /**
//     * @param pRules set rules object
//     */

    protected final void setCrgRules(CrgRules pRules) {
        RULE_FACADE.setRule(pRules);
    }

    protected final void setCrgRulePools(CrgRulePools pPool) {
        RULE_FACADE.setPool(pPool);
    }

    public Element getRoot(){
        if(getControls().isEmpty()){
            return null;
        }
        return (Element) getControls().get(0);
    }

    public void setRuleMessages(List<RuleMessage> pMessages) {
        messages = pMessages;
    }
    
    public List<RuleMessage> getRuleMessages(){
        return messages;
    }
}
