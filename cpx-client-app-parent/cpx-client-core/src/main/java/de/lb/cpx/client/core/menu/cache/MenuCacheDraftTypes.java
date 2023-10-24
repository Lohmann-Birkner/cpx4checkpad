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
import de.lb.cpx.model.enums.CategoryEn;
import de.lb.cpx.server.commonDB.model.CWmListDraftType;
import de.lb.cpx.service.ejb.MenuCacheBeanRemote;
import de.lb.cpx.shared.menucache.MenuCacheEntryEn;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

/**
 *
 * @author niemeier
 */
public class MenuCacheDraftTypes extends MenuCacheEntryMenuCacheEntity<CWmListDraftType> {

    private static final Logger LOG = Logger.getLogger(MenuCacheDraftTypes.class.getName());

    //load all rules, if role map is null or use stored one
    @Override
    protected Map<Long, CWmListDraftType> initialize() {
        EjbProxy<MenuCacheBeanRemote> bean = Session.instance().getEjbConnector().connectMenuCacheBean();
        return bean.get().getDraftTypes();
    }

    @Override
    public MenuCacheDraftTypes getCopy() {
        return (MenuCacheDraftTypes) super.getCopy(new MenuCacheDraftTypes());
    }

    /**
     *
     * @param pDraftType templates Type
     * @return templates
     * @throws NamingException ejb exception
     */
    public Long getDraftTypesInternalId(String pDraftType) throws NamingException {
        return getId(pDraftType);
//        if (pDraftType == null) {
//            return null;
//        }
//        Iterator<Long> it = keySet().iterator();
//        while (it.hasNext()) {
//            Long next = it.next();
//            if (pDraftType.equals(get(next).getName())) {
//                return next;
//            }
//        }
//        return null;
    }

    /**
     * map of draft types separated by category
     *
     * @param pDate check valid from/to
     * @return draft types
     */
    public Map<CategoryEn, List<CWmListDraftType>> getDeadlineTypes(final Date pDate) {
        return getDraftTypes(pDate, null);
//        return new HashMap<>(deadlineTypes);
    }

    /**
     * get draft types with specific category
     *
     * @param pCategory deadline type
     * @return list of deadlines
     */
    public List<CWmListDraftType> getDraftTypes(final CategoryEn pCategory) {
        List<CWmListDraftType> draftTypes = getDraftTypes(null, pCategory).get(pCategory);
        if (draftTypes == null) {
            LOG.log(Level.INFO, "no draft types found for category {0}", (pCategory == null ? null : pCategory.name()));
            return new ArrayList<>();
        }
        return new ArrayList<>(draftTypes);
    }

    /**
     * map of draft types separated by category
     *
     * @param pDate check valid from/to
     * @param pType check draft type
     * @return draft types
     */
    private Map<CategoryEn, List<CWmListDraftType>> getDraftTypes(final Date pDate, final CategoryEn pType) {
        final Map<CategoryEn, List<CWmListDraftType>> result = new HashMap<>();
        Map<Long, CWmListDraftType> m = map(pDate);
        if (m == null) {
            return result;
        }
        for (Iterator<Map.Entry<Long, CWmListDraftType>> it = m.entrySet().iterator(); it.hasNext();) {
            Map.Entry<Long, CWmListDraftType> entry = it.next();
            if (entry != null && entry.getValue() != null /* && dl.getDlType() != null */) {
                CWmListDraftType draftType = entry.getValue();
                if (pType != null && (draftType.getWmDrtCategory() == null || !pType.equals(draftType.getWmDrtCategory()))) {
                    continue;
                }
                if (pDate != null && !draftType.isValid(pDate)) {
                    continue;
                }
//                if (pDate != null) {
//                    if (deadline.getDlValidFrom() != null && deadline.getDlValidFrom().after(pDate)) {
//                        continue;
//                    }
//                    if (deadline.getDlValidTo() != null && deadline.getDlValidTo().before(pDate)) {
//                        continue;
//                    }
//                }
                CategoryEn type = draftType.getWmDrtCategory();
                List<CWmListDraftType> draftTypes = result.get(type);
                if (draftTypes == null) {
                    draftTypes = new ArrayList<>();
                    result.put(type, draftTypes);
                }
                draftTypes.add(draftType);
            }
        }
        return result;
//        return new HashMap<>(deadlineTypes);
    }

    /**
     * map of draft types separated by category
     *
     * @return deadlines
     */
    public Map<CategoryEn, List<CWmListDraftType>> getDeadlineTypes() {
        return getDraftTypes(null, null);
    }

    @Override
    public MenuCacheEntryEn getType() {
        return MenuCacheEntryEn.DRAFT_TYPES;
    }

}
