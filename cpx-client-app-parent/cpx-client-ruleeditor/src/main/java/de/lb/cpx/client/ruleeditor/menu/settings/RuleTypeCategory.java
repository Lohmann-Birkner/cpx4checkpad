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
package de.lb.cpx.client.ruleeditor.menu.settings;

import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.server.rule.services.RuleEditorProcessException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 *
 * @author wilde
 */
public class RuleTypeCategory extends Control {

    public RuleTypeCategory() {
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        try {
            return new RuleTypeCategorySkin(this);
//        return super.createDefaultSkin(); //To change body of generated methods, choose Tools | Templates.
        } catch (IOException ex) {
            Logger.getLogger(RuleTypeCategory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return super.createDefaultSkin();
    }

    public List<CrgRuleTypes> fetchRuleTypes(PoolTypeEn pType) {
        return new ArrayList<>();
    }

    public void updateRuleType(CrgRuleTypes ruleType) {
        try {
            Session.instance().getEjbConnector().connectRuleEditorBean().get().updateRuleType(ruleType, getType());
        } catch (RuleEditorProcessException ex) {
            Logger.getLogger(RuleTypeCategory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CrgRuleTypes addRuleType() throws RuleEditorProcessException {
        return Session.instance().getEjbConnector().connectRuleEditorBean().get().createRuleType(getType());
    }

    public void deleteRuleType(CrgRuleTypes pRuleType) throws RuleEditorProcessException {
        Session.instance().getEjbConnector().connectRuleEditorBean().get().deleteRuleType(pRuleType.getId(), getType());
    }

    public long getSizeOfTypeRuleRelation(CrgRuleTypes ruleType) {
        return Session.instance().getEjbConnector().connectRuleEditorBean().get().getSizeOfTypeRuleRelation(ruleType.getId(), getType());
    }

    private ObjectProperty<PoolTypeEn> typeProperty;

    public final ObjectProperty<PoolTypeEn> typeProperty() {
        if (typeProperty == null) {
            typeProperty = new SimpleObjectProperty<>();
        }
        return typeProperty;
    }

    public PoolTypeEn getType() {
        return typeProperty().get();
    }

    public final void setType(PoolTypeEn pType) {
        typeProperty().set(pType);
    }

    public boolean isEditable() {
        return !PoolTypeEn.PROD.equals(getType());
    }
}
