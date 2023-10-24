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
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.service.ejb.MenuCacheBeanRemote;
import de.lb.cpx.shared.menucache.MenuCacheEntryEn;
import java.util.Map;

/**
 *
 * @author niemeier
 */
public class MenuCacheRoles extends MenuCacheEntryMenuCacheEntity<CdbUserRoles> {

    //load all rules, if role map is null or use stored one
    @Override
    protected Map<Long, CdbUserRoles> initialize() {
        EjbProxy<MenuCacheBeanRemote> bean = Session.instance().getEjbConnector().connectMenuCacheBean();
        return bean.get().getUserRoles();
//        EjbProxy<LoginServiceEJBRemote> bean;
//        try {
//            bean = Session.instance().getEjbConnector().connectLoginServiceBean();
//            List<CdbUserRoles> result = bean.get().getRoles();
//            Map<Long, CdbUserRoles> map = new TreeMap<>();
//            for (CdbUserRoles role : result) {
//                map.put(role.id, role);
//            }
//            return map;
//        } catch (NullPointerException ex2) {
//            LOG.log(Level.WARNING, "Server not found!\n" + ex2.getMessage(), ex2);
////                Logger.getLogger(MenuCache.class.getName()).log(Level.SEVERE, null, ex2);
////                BasicMainApp.showErrorMessageDialog(ex2, "Server konnte nicht erreicht werden!");
//            return new HashMap<>();
//        }
    }

    @Override
    public MenuCacheRoles getCopy() {
        return (MenuCacheRoles) super.getCopy(new MenuCacheRoles());
    }

    @Override
    public String getName(final Long pKey) {
        CdbUserRoles obj = get(pKey);
        return obj == null ? null : obj.getCdburName();
//        if (pKey == null) {
//            return null;
//        }
//        Iterator<Long> it = keySet().iterator();
//        while (it.hasNext()) {
//            Long next = it.next();
//            CdbUserRoles obj = get(next);
//            if (obj == null) {
//                continue;
//            }
//            if (pKey.equals(obj.getId())) {
//                return obj.getCdburName();
//            }
//        }
//        return null;
    }

    @Override
    public MenuCacheEntryEn getType() {
        return MenuCacheEntryEn.ROLES;
    }

}
