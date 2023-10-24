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
//
//import de.lb.cpx.client.core.config.Session;
//import de.lb.cpx.connector.EjbProxy;
//import de.lb.cpx.server.commonDB.model.CMdkAuditquota;
//import de.lb.cpx.service.ejb.MenuCacheBeanRemote;
//import de.lb.cpx.shared.menucache.MenuCacheEntryEn;
//import java.util.Map;
//
///**
// *
// * key = insurance identifier (IKZ)
// *
// * @author niemeier
// */
//public class MenuCacheMdkAuditQuotas extends MenuCacheEntry<String, CMdkAuditquota> {
//
//    //load all rules, if role map is null or use stored one
//    @Override
//    protected Map<String, CMdkAuditquota> initialize() {
//        EjbProxy<MenuCacheBeanRemote> bean = Session.instance().getEjbConnector().connectMenuCacheBean();
//        return bean.get().getMdkAuditQuotas();
//    }
//
//    @Override
//    public MenuCacheMdkAuditQuotas getCopy() {
//        return (MenuCacheMdkAuditQuotas) super.getCopy(new MenuCacheMdkAuditQuotas());
//    }
//
//    @Override
//    public String getName(final String pKey) {
//        throw new UnsupportedOperationException("mdk audit quota has no name");
//    }
//
//    @Override
//    public MenuCacheEntryEn getType() {
//        return MenuCacheEntryEn.MDK_AUDIT_QUOTAS;
//    }
//
//}
