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
package de.lb.cpx.client.ruleeditor.menu.filterlists.tabs;

import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.ruleviewer.util.PoolTypeHelper;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * RulePreview control to show rule data
 *
 * @author wilde
 */
public class RulePreview extends Control {

    @Override
    protected Skin<?> createDefaultSkin() {
        try {
            return new RulePreviewSkin(this);
        } catch (IOException ex) {
            Logger.getLogger(RulePreview.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private ObjectProperty<CrgRules> ruleProperty;

    public ObjectProperty<CrgRules> ruleProperty() {
        if (ruleProperty == null) {
            ruleProperty = new SimpleObjectProperty<>();
        }
        return ruleProperty;
    }

    public void setRule(CrgRules pRule) {
        ruleProperty().set(pRule);
    }

    public CrgRules getRule() {
        return ruleProperty().get();
    }

    private ObjectProperty<CrgRulePools> poolProperty;

    public ObjectProperty<CrgRulePools> poolProperty() {
        if (poolProperty == null) {
            poolProperty = new SimpleObjectProperty<>();
        }
        return poolProperty;
    }

    public void setPool(CrgRulePools pPool) {
        poolProperty().set(pPool);
    }

    public CrgRulePools getPool() {
        return poolProperty().get();
    }

    public byte[] getRuleDefinition(CrgRules pRule) {
        byte[] definition = Session.instance().getEjbConnector().connectRuleEditorBean().get().findRuleDefinition(pRule.getId(), getPool().getId(), PoolTypeHelper.getPoolType(getPool()));
        pRule.setCrgrDefinition(definition);
        return definition;
    }

    public List<CdbUserRoles> getUserRoles4Rule(CrgRules pRule) {
        List<CdbUserRoles> roles = Session.instance().getEjbConnector().connectRuleEditorBean().get().findUserRoles2Rule(pRule.getId(), PoolTypeHelper.getPoolType(getPool()));
        return roles;
    }

    public String getRuleTypeName(CrgRules pRule) {
        return Session.instance().getEjbConnector().connectRuleEditorBean().get().getRuleTypeNameForRule(pRule.getId(), getPool().getId(), PoolTypeHelper.getPoolType(getPool()));
    }

    public String getRuleTypeDisplayName(CrgRules pRule) {
        return Session.instance().getEjbConnector().connectRuleEditorBean().get().getRuleTypeDisplayNameForRule(pRule.getId(), getPool().getId(), PoolTypeHelper.getPoolType(getPool()));
    }
}
