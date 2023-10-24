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

import de.lb.cpx.model.TP301KainInkaPvt;
import de.lb.cpx.model.TP301KainInkaPvt_;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Shahin
 */
@Stateless
public class TP301KainInkaPvtDao extends AbstractCpxDao<TP301KainInkaPvt> {

    public TP301KainInkaPvtDao() {
        super(TP301KainInkaPvt.class);
    }

    public List<TP301KainInkaPvt> findAllPvtsForPvv(long pvvId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TP301KainInkaPvt> query = criteriaBuilder.createQuery(TP301KainInkaPvt.class);
        Root<TP301KainInkaPvt> from = query.from(TP301KainInkaPvt.class);
        query.where(criteriaBuilder.equal(from.get(TP301KainInkaPvt_.p301KainInkaPvvId), pvvId));
        TypedQuery<TP301KainInkaPvt> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
    }

}
