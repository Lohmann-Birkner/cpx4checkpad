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

import de.lb.cpx.server.commonDB.model.CBookmarksCustomer;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author nandola
 */
@Stateless
@SuppressWarnings("unchecked")
public class CBookmarksCustomerDao extends AbstractCommonDao<CBookmarksCustomer> {

    public CBookmarksCustomerDao() {
        super(CBookmarksCustomer.class);
    }

//    /**
//     * @return list of all BookmarksCustomer Entries.
//     */
//    public List<CBookmarksCustomer> getAllBookmarksCustomerEntries() {
//        final TypedQuery<CBookmarksCustomer> query = getEntityManager().createQuery("from CBookmarksCustomer", CBookmarksCustomer.class);
//        return query.getResultList();
//    }
//    /**
//     * returns list of BookmarksCustomer IDs.
//     *
//     * @return IDs
//     */
//    public List<Long> getAllBookmarksCustomerIds() {
//        final TypedQuery<Long> query = getEntityManager().createQuery("select id from CBookmarksCustomer c", Long.class);
//        return query.getResultList();
//    }
//    /**
//     * Get BookmarksCustomer by id
//     *
//     * @param BookmarksCustomerId BookmarksCustomer id
//     * @return searched BookmarksCustomer (object) item
//     */
//    public CBookmarksCustomer getBookmarksCustomerById(Long BookmarksCustomerId) {
//        List<CBookmarksCustomer> lResults = null;
//        String queryName = "select u from " + CBookmarksCustomer.class.getSimpleName() + " u where id =:idNbr";
//        Query query = getEntityManager().createQuery(queryName);
//        query.setParameter("idNbr", BookmarksCustomerId);
//        lResults = query.getResultList();
//        if (lResults == null || lResults.isEmpty()) {
//            return null;
//        }
//        return lResults.get(0);
//    }
    /**
     * Get all entries from table of bookmarks
     *
     * @return entries list
     */
    public List<CBookmarksCustomer> getBookmarksCatalog() {
        List<CBookmarksCustomer> list = null;
        Query query = getEntityManager().createQuery("Select b from " + CBookmarksCustomer.class.getSimpleName() + " b order by b.bookmarkName");
        list = query.getResultList();
        return list;
    }

//    /**
//     * Add new bookmark
//     *
//     * @param pCBookmarksCustomer bookmark
//     * @return id for new bookmark
//     */
//    public long addNewBookmark(CBookmarksCustomer pCBookmarksCustomer) {
//        long bookmarkId = 0L;
//        if (pCBookmarksCustomer != null) {
//            try {
//                persist(pCBookmarksCustomer);
//                flush();
//                bookmarkId = pCBookmarksCustomer.getId();
//            } catch (Exception ex) {
//                LOG.log(Level.SEVERE, "Can't add new bookmark", ex);
//            }
//        }
//        return bookmarkId;
//    }
//
//    /**
//     * update the bookmark
//     *
//     * @param pCBookmarksCustomer bookmark to update
//     * @return state of update process
//     */
//    public boolean updateBookmark(CBookmarksCustomer pCBookmarksCustomer) {
//        boolean checkUpdateState = false;
//        if (pCBookmarksCustomer.getId() <= 0L) {
//            checkUpdateState = false;
//        } else {
//            merge(pCBookmarksCustomer);
//            checkUpdateState = true;
//        }
//        return checkUpdateState;
//    }
//
//    /**
//     * Get bookmark item by id
//     *
//     * @param pBookmarkId bookmark id
//     * @return searched bookmark
//     */
//    public CBookmarksCustomer getBookmarkById(Long pBookmarkId) {
//        List<CBookmarksCustomer> lResults = null;
//        String queryName = "select u from " + CBookmarksCustomer.class.getSimpleName() + " u where id =:idNbr";
//        Query query = getEntityManager().createQuery(queryName);
//        query.setParameter("idNbr", pBookmarkId);
//        lResults = query.getResultList();
//        if (lResults == null || lResults.isEmpty()) {
//            return null;
//        }
//        return lResults.get(0);
//    }
//
//    /**
//     * Delete bookmark from database
//     *
//     * @param id bookmark id
//     * @return boolean value for delete state
//     */
//    public boolean removeBookmarkById(Long id) {
//        deleteById(id);
//        boolean isDeleted = true;
//        return isDeleted;
//    }
}
