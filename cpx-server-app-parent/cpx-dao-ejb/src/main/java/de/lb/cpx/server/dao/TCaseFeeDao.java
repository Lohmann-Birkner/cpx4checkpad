/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.dao;

import de.lb.cpx.model.TCaseFee;
import de.lb.cpx.model.TCaseFee_;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * sateless dao ejb to access the T_CASE_FEE Table
 *
 * @author wilde
 */
@Stateless
public class TCaseFeeDao extends AbstractCpxDao<TCaseFee> {

    /**
     * Creates a new instance.
     */
    public TCaseFeeDao() {
        super(TCaseFee.class);
    }

    /**
     * @param pDetailsId case details id
     * @return list of all case fees for the case details
     */
    public List<TCaseFee> findTCaseFeesForDetails(long pDetailsId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCaseFee> query = criteriaBuilder.createQuery(TCaseFee.class);
        Root<TCaseFee> from = query.from(TCaseFee.class);
        query.where(criteriaBuilder.equal(from.get(TCaseFee_.caseDetails), pDetailsId));
        return getEntityManager().createQuery(query).getResultList();
    }
}
