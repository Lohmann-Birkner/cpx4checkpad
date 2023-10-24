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
 *    2017  Shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.dao;

import de.lb.cpx.model.TP301KainInka;
import de.lb.cpx.model.TP301KainInka_;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Shahin
 */
@Stateless
@SuppressWarnings("unchecked")
public class TP301KainInkaDao extends AbstractCpxDao<TP301KainInka> {

    private static final Logger LOG = Logger.getLogger(TP301KainInkaDao.class.getName());

    public TP301KainInkaDao() {
        super(TP301KainInka.class);
    }

    public List<TP301KainInka> findAllForCase(long tCaseId) {
        LOG.log(Level.FINE, "Find all kain inkas for case with id " + tCaseId);
        final long startTime = System.currentTimeMillis();
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TP301KainInka> query = criteriaBuilder.createQuery(TP301KainInka.class);

        Root<TP301KainInka> from = query.from(TP301KainInka.class);
//        query.where(criteriaBuilder.equal(from.get(TP301KainInka_.process), pProcessId));
        query.where(criteriaBuilder.equal(from.get(TP301KainInka_.TCaseId), tCaseId));

        TypedQuery<TP301KainInka> criteriaQuery = getEntityManager().createQuery(query);
        List<TP301KainInka> result = criteriaQuery.getResultList();
        LOG.log(Level.FINER, "Found " + result.size() + " kain inkas for case id " + tCaseId + " in " + (System.currentTimeMillis() - startTime) + " ms");
        return result;
    }

    public TP301KainInka getTP301KainInkaById(long kainInkaId) {
        String queryName = "select u from " + TP301KainInka.class.getSimpleName() + " u where id =:idNbr";
        Query query = getEntityManager().createQuery(queryName);
        query.setParameter("idNbr", kainInkaId);
        List<TP301KainInka> lResults = query.getResultList();
        if (lResults == null || lResults.isEmpty()) {
            return null;
        }
        return lResults.get(0);
    }

}
