/* 
 * Copyright (c) 2019 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2019  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commonDB.dao;

import de.lb.cpx.server.commonDB.model.CBookmarks;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author nandola
 */
@Stateless
@SuppressWarnings("unchecked")
public class CBookmarksDao extends AbstractCommonDao<CBookmarks> {

    public CBookmarksDao() {
        super(CBookmarks.class);
    }

    /**
     * Get all entries (ordered by bookmark name) from table of bookmarks
     *
     * @return entries list
     */
    public List<CBookmarks> getBookmarksCatalog() {
        List<CBookmarks> list = null;
        Query query = getEntityManager().createQuery("Select b from " + CBookmarks.class.getSimpleName() + " b order by b.bookmarkName");
        list = query.getResultList();
        return list;
    }

    /**
     * @return list of all BookmarksCustomer Entries.
     */
//    public List<CBookmarks> getAllBookmarksCustomerEntries() {
//        final TypedQuery<CBookmarks> query = getEntityManager().createQuery("from CBookmarks", CBookmarks.class);
//        return query.getResultList();
//    }
    /**
     * Get bookmark item by id
     *
     * @param pBookmarkId bookmark id
     * @return searched bookmark
     */
    public CBookmarks getBookmarkById(Long pBookmarkId) {
        List<CBookmarks> lResults = null;
        String queryName = "select u from " + CBookmarks.class.getSimpleName() + " u where id =:idNbr";
        Query query = getEntityManager().createQuery(queryName);
        query.setParameter("idNbr", pBookmarkId);
        lResults = query.getResultList();
        if (lResults == null || lResults.isEmpty()) {
            return null;
        }
        return lResults.get(0);
    }

}
