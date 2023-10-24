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
package de.lb.cpx.server.dao;

import de.lb.cpx.model.TLab;
import de.lb.cpx.model.TLab_;
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
 * @author nandola
 */
@Stateless
public class TLabDao extends AbstractCpxDao<TLab> {

    private static final Logger LOG = Logger.getLogger(TLabDao.class.getName());

    public TLabDao() {
        super(TLab.class);
    }

    public List<TLab> findAllForCase(long tCaseId) {
        LOG.log(Level.FINE, "Find all Labs for case with id {0}", tCaseId);
        final long startTime = System.currentTimeMillis();
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TLab> query = criteriaBuilder.createQuery(TLab.class);

        Root<TLab> from = query.from(TLab.class);
        query.where(criteriaBuilder.equal(from.get(TLab_.hospitalCase), tCaseId));

        TypedQuery<TLab> criteriaQuery = getEntityManager().createQuery(query);
        List<TLab> result = criteriaQuery.getResultList();
        LOG.log(Level.FINER, "Found {0} Labs for case id {1} in {2} ms", new Object[]{result.size(), tCaseId, System.currentTimeMillis() - startTime});
        return result;
    }

}
