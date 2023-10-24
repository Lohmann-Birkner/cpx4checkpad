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
package de.lb.cpx.server.wm.dao;

import de.lb.cpx.server.dao.AbstractCpxDao;
import de.lb.cpx.wm.model.TWmAction;
import de.lb.cpx.wm.model.TWmDocument;
import de.lb.cpx.wm.model.TWmEvent;
import de.lb.cpx.wm.model.TWmEvent_;
import de.lb.cpx.wm.model.TWmReminder;
import de.lb.cpx.wm.model.TWmRequest;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.Hibernate;

/**
 *
 * @author Dirk Niemeier
 */
@Stateless
public class TWmEventDao extends AbstractCpxDao<TWmEvent> {

    private static final Logger LOG = Logger.getLogger(TWmEventDao.class.getName());

    public TWmEventDao() {
        super(TWmEvent.class);
    }

    /**
     * load all TCase Entities from Database
     *
     * @return all events ordered by creationdate(desc)
     */
    public List<TWmEvent> getAllCases() {
        final TypedQuery<TWmEvent> query = getEntityManager().createQuery("from TWmEvent order by creationDate desc", TWmEvent.class);
        return query.getResultList();
    }

    public void removeDocumentEvent(TWmDocument document) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TWmEvent> query = criteriaBuilder.createQuery(TWmEvent.class);

        Root<TWmEvent> from = query.from(TWmEvent.class);
        //add two Where-Conditions like csCaseNamber and equals hospitalIdent 
        query.where(criteriaBuilder.equal(from.get(TWmEvent_.document), document));

        TypedQuery<TWmEvent> criteriaQuery = getEntityManager().createQuery(query);
        TWmEvent result = getSingleResultOrNull(criteriaQuery);
        if (result != null) {
            result.setDocument(null);
            merge(result);
        }
    }

    public void removeActionEvent(TWmAction action) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TWmEvent> query = criteriaBuilder.createQuery(TWmEvent.class);

        Root<TWmEvent> from = query.from(TWmEvent.class);
        //add two Where-Conditions like csCaseNamber and equals hospitalIdent 
        query.where(criteriaBuilder.equal(from.get(TWmEvent_.action), action));

        TypedQuery<TWmEvent> criteriaQuery = getEntityManager().createQuery(query);
        TWmEvent result = getSingleResultOrNull(criteriaQuery);
        if (result != null) {
            result.setAction(null);
            merge(result);
        }
    }

    public void removeReminderEvent(TWmReminder reminder) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TWmEvent> query = criteriaBuilder.createQuery(TWmEvent.class);

        Root<TWmEvent> from = query.from(TWmEvent.class);

        query.where(criteriaBuilder.equal(from.get(TWmEvent_.reminder), reminder));

        TypedQuery<TWmEvent> criteriaQuery = getEntityManager().createQuery(query);
        List<TWmEvent> res = criteriaQuery.getResultList();
        if (res != null) {
            for (TWmEvent event : res) {
                event.setReminder(null);
                merge(event);
            }
        }
//        TWmEvent result = getSingleResultOrNull(criteriaQuery);
//        if (result != null) {
//            result.setReminder(null);
//            merge(result);
//        }
    }

    public void removeRequestEvent(TWmRequest request) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TWmEvent> query = criteriaBuilder.createQuery(TWmEvent.class);

        Root<TWmEvent> from = query.from(TWmEvent.class);

        query.where(criteriaBuilder.equal(from.get(TWmEvent_.request), request));

        TypedQuery<TWmEvent> criteriaQuery = getEntityManager().createQuery(query);
        List<TWmEvent> res = criteriaQuery.getResultList();
        if (res != null) {
            for (TWmEvent event : res) {
                event.setRequest(null);
                merge(event);
            }
        }
    }

    public List<TWmEvent> findAllForProcess(long pProcessId) {
        return findAllForProcess(pProcessId, true, null);
    }

    public List<TWmEvent> findAllForProcess(long pProcessId, final boolean pHistoryDeleted) {
        return findAllForProcess(pProcessId, pHistoryDeleted, null);
    }

    public List<TWmEvent> findAllForProcess(long pProcessId, final boolean pHistoryDeleted, final Integer pLimit) {
        LOG.log(Level.FINE, "Get list of events with limitation of events of " + String.valueOf(pLimit) + " for process id " + pProcessId);
        final long startTime = System.currentTimeMillis();
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TWmEvent> query = criteriaBuilder.createQuery(TWmEvent.class);

        Root<TWmEvent> from = query.from(TWmEvent.class);
        //add two Where-Conditions like csCaseNamber and equals hospitalIdent 
        query.where(criteriaBuilder.equal(from.get(TWmEvent_.process), pProcessId));

        if (pLimit != null && pLimit > 0) {
            query.orderBy(
                    criteriaBuilder.desc(from.get(TWmEvent_.creationDate)),
                    criteriaBuilder.desc(from.get(TWmEvent_.modificationDate)),
                    criteriaBuilder.desc(from.get(TWmEvent_.id))
            );
        }

        TypedQuery<TWmEvent> criteriaQuery = getEntityManager().createQuery(query);
//        setMaxResults does no work approriatly        
//        if (pLimit != null && pLimit > 0) {
//            criteriaQuery.setMaxResults(pLimit);
//        }
        List<TWmEvent> results = criteriaQuery.getResultList();

        if (!pHistoryDeleted) {
            //remove deleted elements (would be better to remove them within query)
            Iterator<TWmEvent> it = results.iterator();
            while (it.hasNext()) {
                TWmEvent elem = it.next();
                if (elem.isOrphaned()) {
                    it.remove();
                }
            }
        }

        int n = 0;
        Iterator<TWmEvent> it = results.iterator();
        while (it.hasNext()) {
            TWmEvent e = it.next();
            n++;
            if (pLimit != null && pLimit > 0 && n > pLimit) {
                it.remove();
                continue;
            }
            Hibernate.initialize(e.getAction());
            if (e.getKainInka() != null) {
                Hibernate.initialize(e.getKainInka().getKainInkaPvvs());
            }
        }
        LOG.log(Level.FINER, "Got list with " + results.size() + " events for process id " + pProcessId + " in " + (System.currentTimeMillis() - startTime) + " ms ");
        return results;
    }

}
