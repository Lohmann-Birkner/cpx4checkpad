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
import de.lb.cpx.server.commonDB.model.CWmListActionSubject;
import de.lb.cpx.service.ejb.MenuCacheBeanRemote;
import de.lb.cpx.shared.menucache.MenuCacheEntryEn;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;

/**
 *
 * @author niemeier
 */
public class MenuCacheActionSubjects extends MenuCacheEntryMenuCacheEntity<CWmListActionSubject> {

    //load all rules, if role map is null or use stored one
    @Override
    protected Map<Long, CWmListActionSubject> initialize() {
        EjbProxy<MenuCacheBeanRemote> bean = Session.instance().getEjbConnector().connectMenuCacheBean();
        return bean.get().getActionSubjects();
    }

    @Override
    public MenuCacheActionSubjects getCopy() {
        return (MenuCacheActionSubjects) super.getCopy(new MenuCacheActionSubjects());
    }

    /**
     *
     * @param pActionSubject action subject name
     * @return of of the reminder subject, null if name is not found in map
     * @throws NamingException thrown when bean is not found
     */
    public Long getActionSubjectsId(String pActionSubject) throws NamingException {
        return getId(pActionSubject);
//        if (pActionSubject == null) {
//            return null;
//        }
//        Iterator<Long> it = keySet().iterator();
//        while (it.hasNext()) {
//            Long next = it.next();
//            if (pActionSubject.equals(get(next).getName())) {
//                return next;
//            }
//        }
//        return null;
    }

    public List<CWmListActionSubject> getActionSubjects(Date pDate) {
        pDate = java.util.Objects.requireNonNullElse(pDate, new Date());
        List<CWmListActionSubject> subjects = new ArrayList<>();
        for (CWmListActionSubject subject : values()) {
            if (subject.isValid(pDate)) {
                //if (subject.getWmAsValidTo().after(pDate) && subject.getWmAsValidFrom().before(pDate)) {
                subjects.add(subject);
            }
        }
        return subjects;
    }

    @Override
    public MenuCacheEntryEn getType() {
        return MenuCacheEntryEn.ACTION_SUBJECTS;
    }

}
