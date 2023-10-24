/*
 * Copyright (c) 2018 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer;

import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * Implemenation of rule view displays an CrgRules object based on xml
 * definition
 *
 * @author wilde
 */
public class CrgRuleView extends Control {

    @Override
    protected Skin<?> createDefaultSkin() {
        try {
            return new CrgRuleViewSkin(this);
        } catch (IOException ex) {
            Logger.getLogger(CrgRuleView.class.getName()).log(Level.SEVERE, null, ex);
        }
        return super.createDefaultSkin();
    }

    //rule Property
    private ObjectProperty<CrgRules> ruleProperty;

    /**
     * @return property storing currently displayed rule
     */
    public ObjectProperty<CrgRules> ruleProperty() {
        if (ruleProperty == null) {
            ruleProperty = new SimpleObjectProperty<>();
        }
        return ruleProperty;
    }

    /**
     * @return currently displayed rule
     */
    public CrgRules getRule() {
        return ruleProperty().get();
    }

    /**
     * @param pRule rule to display
     */
    public void setRules(CrgRules pRule) {
        ruleProperty().set(pRule);
    }

    public CrgRulePools getPool() {
        //risky, possible nullpointer further on should query server if pool is not set??
        //but for now its ok .. lol =D
        return getRule().getCrgRulePools();
    }
}
