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
package de.lb.cpx.server.dao;

import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseIcdGrouped;
import de.lb.cpx.model.TCaseIcdGrouped_;
import de.lb.cpx.model.TCaseIcd_;
import de.lb.cpx.server.commons.enums.EntityGraphType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

/**
 *
 * @author wilde
 */
@Stateless
@SuppressWarnings("unchecked")
public class TCaseIcdGroupedDao extends AbstractCpxDao<TCaseIcdGrouped> {

    /**
     * Creates a new instance.
     */
    public TCaseIcdGroupedDao() {
        super(TCaseIcdGrouped.class);
    }

    public List<TCaseIcdGrouped> findListByCaseDetailsId(Long icdId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCaseIcdGrouped> query = criteriaBuilder.createQuery(TCaseIcdGrouped.class);
        Root<TCaseIcdGrouped> from = query.from(TCaseIcdGrouped.class);
//        from.fetch(TCaseIcdGrouped_.groupingResults, JoinType.LEFT);

        Join<TCaseIcdGrouped, TCaseIcd> depJoin = from.join(TCaseIcdGrouped_.caseIcd);
        query.where(criteriaBuilder.equal(depJoin.get(TCaseIcd_.id), icdId));
        TypedQuery<TCaseIcdGrouped> criteriaQuery = getEntityManager().createQuery(query);
        EntityGraph<TCaseIcdGrouped> toFetch = getEntityManager().createEntityGraph(TCaseIcdGrouped.class);

        toFetch.addAttributeNodes(TCaseIcdGrouped_.groupingResults);

        criteriaQuery.setHint(EntityGraphType.getLoadGraphType(), toFetch);

        return criteriaQuery.getResultList();
    }

}
