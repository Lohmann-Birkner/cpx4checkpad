/* 
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commonDB.dao;

import de.lb.cpx.server.commonDB.model.CDeadline;
import de.lb.cpx.server.commons.dao.MenuCacheEntity;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.hibernate.Hibernate;

/**
 * Data access object for domain model class CDoctor. Initially generated at
 * 02.02.2016 17:27:35 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools, sklarow
 */
@Stateless
@SuppressWarnings("unchecked")
public class CDeadlineDao extends AbstractCommonDao<CDeadline> {

    /**
     * Creates a new instance.
     */
    public CDeadlineDao() {
        super(CDeadline.class);
    }

    /**
     * Get all entries from table of deadlines
     *
     * @return entries list
     */
    public List<CDeadline> getEntries() {
        List<CDeadline> list = null;
        Query query = getEntityManager().createQuery("from " + CDeadline.class.getSimpleName() + " b order by b.dlValidFrom, b.dlValidTo");
        list = query.getResultList();
        if (list != null) {
            for (CDeadline dl : list) {
                Hibernate.initialize(dl.getDlReminderType());
            }
        }
        return list;
    }

//    /**
//     * Add new deadline
//     *
//     * @param pCDeadline deadline
//     * @return id for new deadlines
//     */
//    public long addNewDeadline(CDeadline pCDeadline) {
//        return addNewItem(pCDeadline);
////        long deadlineId = 0L;
////        if (pCDeadline != null) {
////            try {
////                persist(pCDeadline);
////                flush();
////                deadlineId = pCDeadline.getId();
////            } catch (Exception ex) {
////                LOG.log(Level.SEVERE, "Can't add new deadline", ex);
////            }
////        }
////        return deadlineId;
//    }
//
//    /**
//     * update the deadline
//     *
//     * @param pCDeadline deadline to update
//     * @return state of update process
//     */
//    public boolean updateDeadline(CDeadline pCDeadline) {
//        boolean checkUpdateState = false;
//        if (pCDeadline.getId() <= 0L) {
//            checkUpdateState = false;
//        } else {
//            merge(pCDeadline);
//            checkUpdateState = true;
//        }
//        return checkUpdateState;
//    }
//
//    /**
//     * Get deadline item by id
//     *
//     * @param pDeadlineId deadline id
//     * @return searched deadline
//     */
//    public CDeadline getDeadlineById(Long pDeadlineId) {
//        List<CDeadline> lResults = null;
//        String queryName = "select u from " + CDeadline.class.getSimpleName() + " u where id =:idNbr";
//        Query query = getEntityManager().createQuery(queryName);
//        query.setParameter("idNbr", pDeadlineId);
//        lResults = query.getResultList();
//        if (lResults == null || lResults.isEmpty()) {
//            return null;
//        }
//        return lResults.get(0);
//    }
//
//    /**
//     * Delete deadline from database
//     *
//     * @param id deadline id
//     * @return boolean value for delete state
//     */
//    public boolean removeDeadlineById(Long id) {
//        deleteById(id);
//        boolean isDeleted = true;
//        return isDeleted;
//    }
    public Map<Long, CDeadline> getDeadlines() {
        return getMenuCacheItems();
    }

    public Map<Long, CDeadline> getMenuCacheItems() {
        List<CDeadline> list = findAll();
        for (CDeadline dl : list) {
            Hibernate.initialize(dl.getDlReminderType());
        }
        return MenuCacheEntity.toMap(findAll());
    }

}
