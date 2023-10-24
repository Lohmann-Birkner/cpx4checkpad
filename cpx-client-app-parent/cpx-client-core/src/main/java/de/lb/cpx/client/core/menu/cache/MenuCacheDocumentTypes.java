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
import de.lb.cpx.server.commonDB.model.CWmListDocumentType;
import de.lb.cpx.service.ejb.MenuCacheBeanRemote;
import de.lb.cpx.shared.menucache.MenuCacheEntryEn;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author niemeier
 */
public class MenuCacheDocumentTypes extends MenuCacheEntryMenuCacheEntity<CWmListDocumentType> {

    //load all rules, if role map is null or use stored one
    @Override
    protected Map<Long, CWmListDocumentType> initialize() {
        EjbProxy<MenuCacheBeanRemote> bean = Session.instance().getEjbConnector().connectMenuCacheBean();
        return bean.get().getDocumentTypes();
    }

    @Override
    public MenuCacheDocumentTypes getCopy() {
        return (MenuCacheDocumentTypes) super.getCopy(new MenuCacheDocumentTypes());
    }

//    /**
//     * @param pDocumentType document type name
//     * @return id of the document type, null if name is not found in map
//     */
//    public Long getDocumentTypeId(String pDocumentType) {
//        return getId(pDocumentType);
////        if (pDocumentType == null) {
////            return null;
////        }
////        Iterator<Long> it = keySet().iterator();
////        while (it.hasNext()) {
////            Long next = it.next();
////            if (pProcessTopic.equals(get(next).getName())) {
////                return next;
////            }
////        }
////        return null;
//    }
    public List<CWmListDocumentType> getDocumentTypes(final Date pDate) {
        return values(pDate);
//        List<CWmListDocumentType> result = new ArrayList<>();
//        Map<Long, CWmListDocumentType> m = map();
//        if (m == null) {
//            return result;
//        }
//        for (Iterator<Map.Entry<Long, CWmListDocumentType>> it = m.entrySet().iterator(); it.hasNext();) {
//            Map.Entry<Long, CWmListDocumentType> entry = it.next();
//            CWmListDocumentType documentType = entry.getValue();
//            if (pDate != null) {
//                if (documentType.getWmDtValidFrom() != null && documentType.getWmDtValidFrom().before(pDate)) {
//                    continue;
//                }
//                if (documentType.getWmDtValidTo() != null && documentType.getWmDtValidTo().after(pDate)) {
//                    continue;
//                }
//            }
//            result.add(documentType);
//        }
//        return result;
    }

    @Override
    public MenuCacheEntryEn getType() {
        return MenuCacheEntryEn.DOCUMENT_TYPES;
    }

}
