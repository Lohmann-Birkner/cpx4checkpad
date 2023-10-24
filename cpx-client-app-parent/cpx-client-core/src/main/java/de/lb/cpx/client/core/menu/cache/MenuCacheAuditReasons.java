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
import de.lb.cpx.server.commonDB.model.CMdkAuditreason;
import de.lb.cpx.service.ejb.MenuCacheBeanRemote;
import de.lb.cpx.shared.menucache.MenuCacheEntryEn;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.naming.NamingException;

/**
 * key = mdkArNumber
 *
 * @author niemeier
 */
public class MenuCacheAuditReasons extends MenuCacheEntryMenuCacheEntity<CMdkAuditreason> {

    @Override
    protected Map<Long, CMdkAuditreason> initialize() {
        EjbProxy<MenuCacheBeanRemote> bean = Session.instance().getEjbConnector().connectMenuCacheBean();
        return bean.get().getMdkAuditReasons();
    }

    @Override
    public MenuCacheAuditReasons getCopy() {
        return (MenuCacheAuditReasons) super.getCopy(new MenuCacheAuditReasons());
    }

    private Map<Long, CMdkAuditreason> getAuditReasonsMap(final Date pDate) {
        return map(pDate, MenuCacheOptionsEn.IGNORE_INACTIVE);
    }

    public Set<Map.Entry<Long, CMdkAuditreason>> getAuditReasonsEntries(final Date pDate) {
        return getAuditReasonsMap(pDate).entrySet();
    }

    /**
     * @param pAuditReason audit reason name
     * @return number of the auditreason, null if name is not found in map
     * @throws NamingException thrown if bean is not found
     */
    public Long getAuditReasonNumber(String pAuditReason) throws NamingException {
        if (pAuditReason == null) {
            return null;
        }
        Iterator<Long> it = keySet().iterator();
        while (it.hasNext()) {
            Long next = it.next();
            if (pAuditReason.equals(get(next).getName())) {
                return next;
            }
        }
        return null;
    }

    @Override
    public MenuCacheEntryEn getType() {
        return MenuCacheEntryEn.AUDIT_REASONS;
    }

}
