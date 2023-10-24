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

import de.lb.cpx.model.TSapFiBillposition;
import de.lb.cpx.model.TSapFiBillposition_;
import java.util.List;
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
public class TSapFiBillpositionDao extends AbstractCpxDao<TSapFiBillposition> {

    public TSapFiBillpositionDao() {
        super(TSapFiBillposition.class);
    }

    public List<TSapFiBillposition> findAllBillPositionsForBill(long billId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TSapFiBillposition> query = criteriaBuilder.createQuery(TSapFiBillposition.class);
        Root<TSapFiBillposition> from = query.from(TSapFiBillposition.class);
        query.where(criteriaBuilder.equal(from.get(TSapFiBillposition_.TSapFiBill), billId));
        TypedQuery<TSapFiBillposition> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
    }

}
