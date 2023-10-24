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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.menu.cache;

import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.rule.services.RuleEditorBeanRemote;
import de.lb.cpx.shared.menucache.MenuCacheEntryEn;
import java.util.Map;

/**
 *
 * @author niemeier
 */
public class MenuCacheRules extends MenuCacheEntryMenuCacheEntity<CrgRules> {

    //load all rules, if role map is null or use stored one
    @Override
    protected Map<Long, CrgRules> initialize() {
//        if (rules.getMap() == null) {
        EjbProxy<RuleEditorBeanRemote> bean = Session.instance().getEjbConnector().connectRuleEditorBean();
        return bean.get().getRules4user();
//        List<CrgRules> result = bean.get().getRules4user();
//        Map<Long, CrgRules> map = new TreeMap<>();
//        for (CrgRules rule : result) {
//            map.put(rule.id, rule);
//        }
//        return map;
    }

    @Override
    public MenuCacheRules getCopy() {
        return (MenuCacheRules) super.getCopy(new MenuCacheRules());
    }

    public String getRuleNumber(long ruleId) {
        CrgRules rule = get(ruleId);
        // for the case, that new rules are created after this cache       
        if (rule == null && CpxClientConfig.instance().isRuleEditorClient()) {
//            rules = null;
            uninitialize();
//            rules.removeMap();
            rule = get(ruleId);
        }
        return rule == null ? null : rule.getCrgrNumber() + "(" + rule.getCrgrValidFromYear() + ")";
    }

    @Override
    public String getName(final Long pKey) {
        CrgRules obj = get(pKey);
        return obj == null ? null : obj.getCrgrNumber();
//        if (pKey == null) {
//            return null;
//        }
//        return getRuleNumber(pKey);
    }

    @Override
    public MenuCacheEntryEn getType() {
        return MenuCacheEntryEn.RULES;
    }

}
