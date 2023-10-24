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
import de.lb.cpx.client.ruleeditor.menu.filterlists.model.CdbUserRolesItem;
import de.lb.cpx.client.ruleeditor.menu.filterlists.tabs.UserRolesDetail.SortMode;
import de.lb.cpx.ruleviewer.enums.ViewMode;
import de.lb.cpx.ruleviewer.util.PoolTypeHelper;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 *
 * @author wilde
 */
public class UserRolesDetail extends Control {

    private ObjectProperty<CdbUserRolesItem> userRolesItemProperty;
    private EventHandler<ActionEvent> onSaveRequested;
    private ObjectProperty<SortMode> sortModeProperty;
    private ObjectProperty<CrgRulePools> poolProperty;
    private ObjectProperty<ViewMode> viewModeProperty;

    public UserRolesDetail(CdbUserRolesItem pRoleItem) {
        super();
        setUserRolesItem(pRoleItem);
        if (!pRoleItem.getStateManager().isInitialized()) {
            pRoleItem.getStateManager().init(pRoleItem.getActiveItems());
        }
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        try {
            return new UserRolesDetailSkin(this);
        } catch (IOException ex) {
            Logger.getLogger(UserRolesDetail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return super.createDefaultSkin(); //To change body of generated methods, choose Tools | Templates.
    }

    public CdbUserRolesItem getUserRolesItem() {
        return userRolesItemProperty().get();
    }

    public final void setUserRolesItem(CdbUserRolesItem pRoleItem) {
        userRolesItemProperty().set(pRoleItem);
    }

    public ObjectProperty<CdbUserRolesItem> userRolesItemProperty() {
        if (userRolesItemProperty == null) {
            userRolesItemProperty = new SimpleObjectProperty<>();
        }
        return userRolesItemProperty;
    }

    public void setOnSaveRequested(EventHandler<ActionEvent> pOnSave) {
        onSaveRequested = pOnSave;
    }

    public EventHandler<ActionEvent> getOnSaveRequested() {
        return onSaveRequested;
    }

    public ObjectProperty<SortMode> sortModeProperty() {
        if (sortModeProperty == null) {
            sortModeProperty = new SimpleObjectProperty<>(SortMode.TYPE);
        }
        return sortModeProperty;
    }

    public SortMode getSortMode() {
        return sortModeProperty().get();
    }

    public void setSortMode(SortMode pSortMode) {
        sortModeProperty().set(pSortMode);
    }

    public ObjectProperty<CrgRulePools> poolProperty() {
        if (poolProperty == null) {
            poolProperty = new SimpleObjectProperty<>();
        }
        return poolProperty;
    }

    public CrgRulePools getPool() {
        return poolProperty().get();
    }

    public void setPool(CrgRulePools pool) {
        poolProperty().set(pool);
    }

    public boolean isEditable() {
        return ViewMode.READ_WRITE.equals(getViewMode());
    }

    public ObjectProperty<ViewMode> viewModeProperty() {
        if (viewModeProperty == null) {
            viewModeProperty = new SimpleObjectProperty<>(ViewMode.READ_ONLY);
        }
        return viewModeProperty;
    }

    public void setViewMode(ViewMode pMode) {
        viewModeProperty().set(pMode);
    }

    public ViewMode getViewMode() {
        return viewModeProperty().get();
    }

    public byte[] fetchRuleContent(CrgRules rule) {
        byte[] ruleContent = Session.instance().getEjbConnector().connectRuleEditorBean().get().findRuleDefinition(
                rule.getId(),
                getPool().getId(),
                PoolTypeHelper.getPoolType(getPool())
        );
        return ruleContent;
    }

    protected enum SortMode {
        FIRST_LETTER("Nach ersten Buchstaben"), RULE_TYPE("Nach Fehlerart"), TYPE("Nach Regeltyp");
        private final String description;

        private SortMode(String pDescription) {
            description = pDescription;
        }

        public String getDescription() {
            return description;
        }
    }
}
