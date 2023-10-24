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
import de.lb.cpx.server.commonDB.model.CWmListReminderSubject;
import de.lb.cpx.service.ejb.MenuCacheBeanRemote;
import de.lb.cpx.shared.menucache.MenuCacheEntryEn;
import java.util.Map;
import javax.naming.NamingException;

/**
 *
 * @author niemeier
 */
public class MenuCacheReminderSubjects extends MenuCacheEntryMenuCacheEntity<CWmListReminderSubject> {

    //load all rules, if role map is null or use stored one
    @Override
    protected Map<Long, CWmListReminderSubject> initialize() {
        EjbProxy<MenuCacheBeanRemote> bean = Session.instance().getEjbConnector().connectMenuCacheBean();
        return bean.get().getReminderSubjects();
    }

    @Override
    public MenuCacheReminderSubjects getCopy() {
        return (MenuCacheReminderSubjects) super.getCopy(new MenuCacheReminderSubjects());
    }

    /**
     * @param pReminderInternalId id of the reminder (uncached!)
     * @return Reminder
     */
    public CWmListReminderSubject getReminderForInternalId(long pReminderInternalId) {
        //EjbProxy<ProcessServiceBeanRemote> processServiceBean = Session.instance().getEjbConnector().connectProcessServiceBean();
        //return processServiceBean.get().getReminderSubjectByInternalId(pReminderInternalId);
        return get(pReminderInternalId);
    }

    /**
     *
     * @param pReminderSubject reminder subject name
     * @return of of the reminder subject, null if name is not found in map
     * @throws NamingException thrown when bean is not found
     */
    public Long getReminderSubjectsId(String pReminderSubject) throws NamingException {
        return getId(pReminderSubject);
//        if (pReminderSubject == null) {
//            return null;
//        }
//        Iterator<Long> it = keySet().iterator();
//        while (it.hasNext()) {
//            Long next = it.next();
//            if (pReminderSubject.equals(get(next).getName())) {
//                return next;
//            }
//        }
//        return null;
    }

    @Override
    public MenuCacheEntryEn getType() {
        return MenuCacheEntryEn.REMINDER_SUBJECTS;
    }

}
