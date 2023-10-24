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

import de.lb.cpx.model.TCheckResult;
import de.lb.cpx.model.TCheckResult_;
import de.lb.cpx.model.TRole2Check;
import de.lb.cpx.model.TRole2Check_;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

/**
 * Implement Database Access for Results of Rule-Evaluation(Regelanschlaege für
 * die Fallpruefung)
 *
 * @author wilde
 */
@Stateless
public class TCheckResultDao extends AbstractCpxDao<TCheckResult> {

    public TCheckResultDao() {
        super(TCheckResult.class);
    }

    /**
     * find list of detected rules for a grouping result, specified by role to
     * check
     *
     * @param groupingResultsId id of the grouping result
     * @param roleId role to check grouping results
     * @return List of detected rules
     */
    public List<TCheckResult> findDetectedRules(long groupingResultsId, long roleId) {

        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCheckResult> query = criteriaBuilder.createQuery(TCheckResult.class);
        Root<TCheckResult> from = query.from(TCheckResult.class);

        SetJoin<TCheckResult, TRole2Check> join = from.join(TCheckResult_.role2Check, JoinType.LEFT);
        query.where(criteriaBuilder.equal(from.get(TCheckResult_.groupingResults), groupingResultsId), criteriaBuilder.equal(join.get(TRole2Check_.roleId), roleId));
        TypedQuery<TCheckResult> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
    }

    /**
     * find list rukeIds for a grouping result, specified by role to check
     *
     * @param resultId id of the grouping result
     * @param roleId role to check grouping results
     * @return List of detected ruleIds
     */
    public List<Long> findDetectedRuleIds(long resultId, long roleId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<TCheckResult> from = query.from(TCheckResult.class);

        query.select(from.get(TCheckResult_.id));

        SetJoin<TCheckResult, TRole2Check> join = from.join(TCheckResult_.role2Check, JoinType.LEFT);
        query.where(criteriaBuilder.equal(from.get(TCheckResult_.groupingResults), resultId), criteriaBuilder.equal(join.get(TRole2Check_.roleId), roleId));

        TypedQuery<Long> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();

    }

}
