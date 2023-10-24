/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Husser - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.wm.dao;

import de.lb.cpx.server.dao.AbstractCpxDao;
import de.lb.cpx.wm.model.TWmReminder;
import de.lb.cpx.wm.model.TWmReminder_;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Husser
 */
@Stateless
public class TWmReminderDao extends AbstractCpxDao<TWmReminder> {

    private static final Logger LOG = Logger.getLogger(TWmReminderDao.class.getName());

    public TWmReminderDao() {
        super(TWmReminder.class);
    }

    public List<TWmReminder> findRemindersByUser(long assignedUserId) {
        String sql = "select a from TWmReminder a where a.assignedUserId = :assignedUserId";
        TypedQuery<TWmReminder> query = getEntityManager().createQuery(sql, TWmReminder.class);
        query.setParameter("assignedUserId", assignedUserId);
        return query.getResultList();
    }

    public List<TWmReminder> findAllForProcess(long pProcessId) {
        return findAllForProcess(pProcessId, null);
    }

    public List<TWmReminder> findAllForProcess(long pProcessId, final Boolean pFinishedFl) {
        LOG.log(Level.FINE, "Find reminders with finish flag of " + String.valueOf(pFinishedFl) + " for process with id " + pProcessId + " and finished fl " + String.valueOf(pFinishedFl));
        final long startTime = System.currentTimeMillis();
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TWmReminder> query = criteriaBuilder.createQuery(TWmReminder.class);

        Root<TWmReminder> from = query.from(TWmReminder.class);
        //add two Where-Conditions like csCaseNamber and equals hospitalIdent
        Predicate pred = criteriaBuilder.equal(from.get(TWmReminder_.process), pProcessId);
        if (pFinishedFl != null) {
            pred = criteriaBuilder.and(pred, criteriaBuilder.equal(from.get(TWmReminder_.finished), pFinishedFl));
        }
        query.where(pred);

        TypedQuery<TWmReminder> criteriaQuery = getEntityManager().createQuery(query);
        List<TWmReminder> result = criteriaQuery.getResultList();
        LOG.log(Level.FINER, "Found " + result.size() + " reminders for process id " + pProcessId + " in " + (System.currentTimeMillis() - startTime) + " ms");
        return result;
    }

    public void closeRemindersByIds(List<Long> reminderIds) {
        final long startTime = System.currentTimeMillis();
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery(String.format("UPDATE T_WM_REMINDER SET FINISHED_FL=1 WHERE id IN (:ids) "));
        query.setParameter("ids", reminderIds);
        int updatedRows = query.executeUpdate();
        LOG.log(Level.INFO, "close " + updatedRows + " Reminders from " + reminderIds.size() + " Reminders in " + +(System.currentTimeMillis() - startTime) + " ms");

    }

    public int findCountForProcess(long pProcessId, Boolean pClosed) {
        List<TWmReminder> lst = this.findAllForProcess(pProcessId, pClosed);
        return lst == null?0:lst.size();
    }

}
