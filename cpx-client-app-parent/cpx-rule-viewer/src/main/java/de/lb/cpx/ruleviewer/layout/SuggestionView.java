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
 *    2018  Your Organisation - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.layout;

import de.lb.cpx.ruleviewer.model.SelectableControl;
import de.lb.cpx.ruleviewer.model.Suggestion;
import de.lb.cpx.shared.json.RuleMessage;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Skin;

/**
 * View that display the rule suggestion
 *
 * @author wilde
 */
public class SuggestionView extends BasicEditorView {

    public static final String DEFAULT_STYLE_CLASS = "suggestion-view";

    public SuggestionView() {
        super();
        getStyleClass().add(0, DEFAULT_STYLE_CLASS);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new SuggestionViewSkin(this);
    }

    @Override
    public void selectFirst() {
        if (getControls().isEmpty()) {
            return;
        }
        setSelectedNode(getControls().get(0));
    }
    private ObservableList<RuleMessage> ruleMessages;
    public ObservableList<RuleMessage> getRuleMessages(){
        if(ruleMessages == null){
            ruleMessages = FXCollections.observableArrayList();
        }
        return ruleMessages;
    }
    public void setRuleMessages(List<RuleMessage> pMessage){
        if(pMessage == null || pMessage.isEmpty()){
            getRuleMessages().clear();
            return;
        }
        getRuleMessages().setAll(pMessage);
    }

    public Suggestion getSuggestionForText(String term) {
        for(SelectableControl ctrl : getControls()){
            if(ctrl instanceof Suggestion){
                if(((Suggestion)ctrl).toString().equalsIgnoreCase(term)){
                    return (Suggestion) ctrl;
                }
            }
        }
        return null;
    }
    
}
