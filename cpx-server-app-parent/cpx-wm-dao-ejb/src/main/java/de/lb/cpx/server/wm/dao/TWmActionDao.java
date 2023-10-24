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
import de.lb.cpx.wm.model.TWmAction;
import de.lb.cpx.wm.model.TWmAction_;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Husser
 */
@Stateless
public class TWmActionDao extends AbstractCpxDao<TWmAction> {

    private static final Logger LOG = Logger.getLogger(TWmActionDao.class.getName());

    public TWmActionDao() {
        super(TWmAction.class);
    }

    public List<TWmAction> findAllForProcess(long pProcessId) {
        LOG.log(Level.FINE, "Find all actions for process with id " + pProcessId);
        final long startTime = System.currentTimeMillis();
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TWmAction> query = criteriaBuilder.createQuery(TWmAction.class);

        Root<TWmAction> from = query.from(TWmAction.class);
        query.where(criteriaBuilder.equal(from.get(TWmAction_.process), pProcessId));

        TypedQuery<TWmAction> criteriaQuery = getEntityManager().createQuery(query);
        List<TWmAction> result = criteriaQuery.getResultList();
        LOG.log(Level.FINER, "Found " + result.size() + " actions for process id " + pProcessId + " in " + (System.currentTimeMillis() - startTime) + " ms");
        return result;
    }

}
