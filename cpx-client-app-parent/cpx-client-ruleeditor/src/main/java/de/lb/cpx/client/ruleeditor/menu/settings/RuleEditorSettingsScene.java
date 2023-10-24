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
import de.lb.cpx.client.core.settings.SettingsCategory;
import de.lb.cpx.client.core.settings.SettingsDialogScene;
import de.lb.cpx.client.ruleeditor.events.UpdateRuleTypeEvent;
import de.lb.cpx.ruleviewer.cache.RuleMetaDataCache;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import de.lb.cpx.server.rule.services.RuleEditorBeanRemote;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import javafx.event.EventHandler;

/**
 * RuleEditor Settings to display items only for this specific client
 *
 * @author wilde
 */
public class RuleEditorSettingsScene extends SettingsDialogScene {

    private static final Logger LOG = Logger.getLogger(RuleEditorSettingsScene.class.getName());

    private RuleEditorBeanRemote ruleEditorBean;

    public RuleEditorSettingsScene() throws IOException {
        super();
        addEventFilter(UpdateRuleTypeEvent.updateRuleTypeEvent(), new EventHandler<UpdateRuleTypeEvent>() {
            @Override
            public void handle(UpdateRuleTypeEvent t) {
                LOG.info("catch update Event");
            }
        });
    }

    @Override
    protected List<SettingsCategory> getAdditionalCategorys() {
        List<SettingsCategory> categories = super.getAdditionalCategorys();
        categories.add(new SettingsCategory("Regeltypen", new RuleTypeCategory() {
            @Override
            public List<CrgRuleTypes> fetchRuleTypes(PoolTypeEn pType) {
                List<CrgRuleTypes> list = super.fetchRuleTypes(pType);
                list.addAll(getRuleTypes(pType));
                return list;
            }

        }));
        return categories;
    }

    public RuleEditorBeanRemote getRuleEditorBean() {
        if (ruleEditorBean == null) {
            ruleEditorBean = Session.instance().getEjbConnector().connectRuleEditorBean().get();
        }
        return ruleEditorBean;
    }

    private List<CrgRuleTypes> getRuleTypes(PoolTypeEn pType) {
        return RuleMetaDataCache.instance().getRuleTypes(pType);
    }
}
