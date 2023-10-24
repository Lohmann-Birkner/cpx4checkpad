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
package de.lb.cpx.ruleviewer.util;

import de.lb.cpx.rule.element.model.RulesElement;
import de.lb.cpx.rule.element.model.RulesOperator;
import de.lb.cpx.rule.element.model.RulesValue;
import de.lb.cpx.rule.element.model.Sugg;
import de.lb.cpx.ruleviewer.enums.ViewMode;
import de.lb.cpx.ruleviewer.model.Element;
import de.lb.cpx.ruleviewer.model.Link;
import de.lb.cpx.ruleviewer.model.SelectableControl;
import de.lb.cpx.ruleviewer.model.Suggestion;
import de.lb.cpx.ruleviewer.model.Term;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory to create SelectableControls from a specific definition String or
 * other RuleComponent types RuleElemnt,RuleValue etc
 *
 * @author wilde
 */
public final class SelectableControlFactory {

    private static SelectableControlFactory INSTANCE;

    private SelectableControlFactory() {
    }

    public static synchronized SelectableControlFactory instance() {
        if (INSTANCE == null) {
            INSTANCE = new SelectableControlFactory();
        }
        return INSTANCE;
    }

    public SelectableControl createControl(Object pObj) {
        //TODO continue
        if (pObj instanceof String) {
            return createControlFromString((String) pObj);
        }
        if (pObj instanceof RulesElement) {
            return createControlFromElement((RulesElement) pObj);
        }
        if (pObj instanceof RulesValue) {
            return createControlFromRuleValue((RulesValue) pObj);
        }
        if (pObj instanceof RulesOperator) {
            return createControlFromRuleOperator((RulesOperator) pObj);
        }
        if (pObj instanceof Sugg) {
            return createControlFromSugg((Sugg) pObj);
        }
        return null;
    }

    public List<SelectableControl> createControls(Object... pList) {
        List<SelectableControl> ctrls = new ArrayList<>();
        for (Object obj : pList) {
            ctrls.add(createControl(obj));
        }
        return ctrls;
    }

    public List<SelectableControl> createControls(List<Object> pList) {
        return createControls(pList.toArray(new Object[pList.size()]));
    }

    private SelectableControl createControlFromString(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private SelectableControl createControlFromElement(RulesElement rulesElement) {
        Element element = new Element();
        element.setRulesElement(0, rulesElement);
        element.setViewMode(ViewMode.READ_WRITE);
        return element;
    }

    private SelectableControl createControlFromRuleValue(RulesValue rulesValue) {
        Term term = new Term();
        term.setRulesValue(rulesValue);
        term.setViewMode(ViewMode.READ_WRITE);
        return term;
    }

    private SelectableControl createControlFromRuleOperator(RulesOperator rulesOperator) {
        Link link = new Link();
        link.setRulesOperator(rulesOperator);
        link.setViewMode(ViewMode.READ_WRITE);
        return link;
    }

    private SelectableControl createControlFromSugg(Sugg sugg) {
        Suggestion suggestion = new Suggestion();
        suggestion.setSuggestion(sugg);
        suggestion.setViewMode(ViewMode.READ_WRITE);
        return suggestion;
    }

}
