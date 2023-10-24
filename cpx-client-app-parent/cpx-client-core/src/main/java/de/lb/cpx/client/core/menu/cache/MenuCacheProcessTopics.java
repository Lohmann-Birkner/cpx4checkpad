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

import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.server.commonDB.model.CWmListProcessTopic;
import de.lb.cpx.service.ejb.MenuCacheBeanRemote;
import de.lb.cpx.shared.menucache.MenuCacheEntryEn;
import java.util.Map;

/**
 *
 * @author niemeier
 */
public class MenuCacheProcessTopics extends MenuCacheEntryMenuCacheEntity<CWmListProcessTopic> {

    //load all rules, if role map is null or use stored one
    @Override
    protected Map<Long, CWmListProcessTopic> initialize() {
        EjbProxy<MenuCacheBeanRemote> bean = Session.instance().getEjbConnector().connectMenuCacheBean();
        return bean.get().getProcessTopics();
    }

    @Override
    public MenuCacheProcessTopics getCopy() {
        return (MenuCacheProcessTopics) super.getCopy(new MenuCacheProcessTopics());
    }

    /**
     * @param pProcessTopic process topic name
     * @return id of the process topic, null if name is not found in map
     */
    public Long getProcessTopicId(String pProcessTopic) {
        return getId(pProcessTopic);
//        if (pProcessTopic == null) {
//            return null;
//        }
//        Iterator<Long> it = keySet().iterator();
//        while (it.hasNext()) {
//            Long next = it.next();
//            if (pProcessTopic.equals(get(next).getName())) {
//                return next;
//            }
//        }
//        return null;
    }

    @Override
    public MenuCacheEntryEn getType() {
        return MenuCacheEntryEn.PROCESS_TOPICS;
    }

}
